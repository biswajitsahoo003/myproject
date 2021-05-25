package com.tcl.dias.performance.service.v1;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.beans.FaultRate;
import com.tcl.dias.beans.MTTRSeverityResponseBean;
import com.tcl.dias.beans.MTTRTrendRFOWise;
import com.tcl.dias.beans.ServerityTicketResponse;
import com.tcl.dias.beans.ServiceInventoryBean;
import com.tcl.dias.beans.TicketByMTTRDuration;
import com.tcl.dias.beans.TicketTrend;
import com.tcl.dias.beans.TrendInfoBean;
import com.tcl.dias.beans.TrendResponseBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.performance.constants.ExceptionConstants;
import com.tcl.dias.performance.constants.HighChartConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * A class to service all data collection for PDF creation
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class PDFReportService {

	@Autowired
	ExportServerService exportService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PDFReportService.class);

	/**
	 * Method to generate PIE chart Image in base 64 string
	 * 
	 * @param rfoPieMap - map of pie data
	 * @param title
	 * @return base 64 string
	 * @throws TclCommonException
	 */
	public String getRFOPieChart(Map<String, Object> rfoPieMap, String title) throws TclCommonException {
		LOGGER.info("Inside getRFOPieChart");
		String base64Str = null;
		StringBuilder sb = new StringBuilder();

		rfoPieMap.forEach((key, value) -> {
			sb.append("{").append("name:").append("\'" + key + "\'").append(",").append(" y:").append(value).append("}")
					.append(",");
		});
		String piechart = HighChartConstants.piechart.replace("VALUES", removeLastIndex(sb.toString()));
		piechart = piechart.replace("CHART_TITLE", title);

		try {
			LOGGER.info("---------------------------------------------------");
			LOGGER.info(" The Query String for PIE " + title + " is " + piechart);
			LOGGER.info("---------------------------------------------------");

			InputStream is = exportService.connectExportServer(piechart);
			if (is != null) {
				base64Str = getBase64EncondedImage(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_GENERATING_PDF, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.ERROR_GENERATING_PDF, e, ResponseResource.R_CODE_ERROR);
		}
		return base64Str;
	}

	/**
	 * A method to get basic column chart as base 64 encoded image string
	 * 
	 * @param productWiseUptimeTrendList
	 * @param xTitle
	 * @param yTitle
	 * @param monthYearListForTrends
	 * @param fromSLA
	 * @return Object
	 * @throws TclCommonException
	 */
	public Object getBasicColumnChart(List<TrendResponseBean> productWiseUptimeTrendList, String xTitle, String yTitle,
			List<String> monthYearListForTrends, boolean fromSLA) throws TclCommonException {

		// add zeros to unavailable months
		Map<String, List<TrendInfoBean>> mapper = productWiseUptimeTrendList.stream()
				.collect(Collectors.toMap(TrendResponseBean::getProduct, TrendResponseBean::getTrendInfo));

		mapper.forEach((key, values) -> {
			List<String> tempList = monthYearListForTrends.stream().map(String::new).collect(Collectors.toList());
			List<String> productsMonthInfoList = values.stream().map(TrendInfoBean::getMonth)
					.collect(Collectors.toList());
			tempList.removeAll(productsMonthInfoList); // contains unavailable months.
		
		// populate zeros..
					tempList.forEach(x -> {
						TrendInfoBean bean = new TrendInfoBean();
						bean.setMonth(x);
						bean.setSlaBreachCount(0);
						bean.setUptimePercentage("0");
						values.add(bean);
					});
				});
		
		
		// list of list -> sort each list in it.
		mapper.values().stream().map(x -> x.stream().sorted(
				Comparator.comparing(TrendInfoBean::getMonth, Comparator.nullsFirst(Comparator.naturalOrder()))));
		

		String columnchart = HighChartConstants.columnchart;
		ArrayList<String> columnList = new ArrayList<String>();
		
		columnList.addAll(
				productWiseUptimeTrendList.stream().map(TrendResponseBean::getProduct).collect(Collectors.toList()));
		
		String commaSeperatedColmnKeys = columnList.stream().collect(Collectors.joining("','", "'", "'"));
		LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
		productWiseUptimeTrendList.forEach(x -> {
			x.getTrendInfo().forEach(y -> {
				StringJoiner joiner = new StringJoiner(",");
				String decider= !fromSLA ? y.getUptimePercentage() : String.valueOf(y.getSlaBreachCount());
				if (!dataMap.containsKey(y.getMonth())) {
					dataMap.put(y.getMonth(), decider);
				} else {
					StringJoiner newStr = joiner.add(dataMap.get(y.getMonth())).add(decider);
					dataMap.put(y.getMonth(), newStr.toString());
				}
			});
		});

		StringBuilder sbstack = new StringBuilder();
		dataMap.forEach((key, value) -> {
			sbstack.append("{name:\'"+ convertDateFormat(key)+ "\', data:["+ value+"]},");
		});

		columnchart = columnchart.replace("VALUES", removeLastIndex(sbstack.toString()));
		columnchart = columnchart.replace("KEYS", commaSeperatedColmnKeys);
		columnchart = columnchart.replace("TITLE", xTitle);
		columnchart = columnchart.replace("YAXIZ", yTitle);

		LOGGER.debug("DATA STRING.......\n");
		LOGGER.debug(columnchart);
		String base64Str = null;
		try {
			InputStream is = exportService.connectExportServer(columnchart);
			if (is != null) {
				base64Str = getBase64EncondedImage(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_GENERATING_PDF, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.ERROR_GENERATING_PDF, e, ResponseResource.R_CODE_ERROR);
		}
		return base64Str;
	}

	/**
	 * A method to get basic line chart image in base 64 format
	 * @param mttrLine
	 * @param title, map object
	 * @return Object
	 * @throws TclCommonException
	 */
	public Object getBasicLineChart(LinkedHashMap<String, HashMap<String, String>> mttrLine, String title)
			throws TclCommonException {
		LOGGER.info("Inside getBasicLineChart");
		String lineChartBasic = HighChartConstants.mttrLine;

		LinkedHashMap<String, String> xAxisMap = new LinkedHashMap<String, String>();

		ArrayList<String> custList = new ArrayList<String>();
		ArrayList<String> tataList = new ArrayList<String>();
		ArrayList<String> unidentifiedList = new ArrayList<String>();
		ArrayList<String> yearList = new ArrayList<String>();

		mttrLine.forEach((k, v) -> {
			yearList.add(k);
			v.forEach((x, y) -> {
				String[] beforeDecimal = y.split("\\.");
				if (beforeDecimal.length > 1) {
					y = Integer.valueOf(beforeDecimal[0]) + "." + beforeDecimal[1];
				} else {
					y = Integer.valueOf(beforeDecimal[0]).toString();
				}
				if (x.equals("Customer")) {
					custList.add(y);
				}
				if (x.equals("TATA")) {
					tataList.add(y);
				}
				if (x.equals("Unidentified")) {
					unidentifiedList.add(y);
				}
			});
		});

		xAxisMap.put("Customer", custList.stream().collect(Collectors.joining(",")));
		xAxisMap.put("Tata comm", tataList.stream().collect(Collectors.joining(",")));
		xAxisMap.put("Unidentified", unidentifiedList.stream().collect(Collectors.joining(",")));

		String commaSeperatedLineKeys = yearList.stream().map(x -> convertDateFormat(x))
				.collect(Collectors.joining("','", "'", "'"));

		StringBuilder sbstack = new StringBuilder();
		xAxisMap.forEach((key, value) -> {
			sbstack.append("{").append("name:").append("\'" + key + "\'").append(",").append(" data:")
					.append("[" + value + "]").append("}").append(",");
		});

		lineChartBasic = lineChartBasic.replace("SERIES_ARR", removeLastIndex(sbstack.toString()));
		lineChartBasic = lineChartBasic.replace("XAXIS_CATEGORIES", commaSeperatedLineKeys);
		lineChartBasic = lineChartBasic.replace("MTTR_TITLE", title);

		LOGGER.debug("DATA STRING.......\n");
		LOGGER.debug(lineChartBasic);
		String base64Str = null;
		try {
			InputStream is = exportService.connectExportServer(lineChartBasic);
			if (is != null) {
				base64Str = getBase64EncondedImage(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_GENERATING_PDF, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.ERROR_GENERATING_PDF, e, ResponseResource.R_CODE_ERROR);
		}
		return base64Str;
	}

	/**
	 * @param mttrSeverityTLBean
	 * @param mttrSeverityPLBean
	 * @param title
	 * @param yTitle
	 * @return Object
	 * @throws TclCommonException
	 */
	public Object getMTTRColumn(List<MTTRSeverityResponseBean> mttrSeverityTLBean,
			List<MTTRSeverityResponseBean> mttrSeverityPLBean, String title, String yTitle) throws TclCommonException {
		LOGGER.info("Inside getMTTRColumn");
		String columnchart = HighChartConstants.columnchart;

		mttrSeverityTLBean.forEach(x -> {
			x.getDurationWise().sort(Comparator.comparing(TicketByMTTRDuration::getHrsBucket,
					Comparator.nullsFirst(Comparator.naturalOrder())));
		});

		mttrSeverityPLBean.forEach(x -> {
			x.getDurationWise().sort(Comparator.comparing(TicketByMTTRDuration::getHrsBucket,
					Comparator.nullsFirst(Comparator.naturalOrder())));
		});

		LinkedHashMap<String, String> mainMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Integer> pLossMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> tlossMap = new LinkedHashMap<String, Integer>();
		TreeSet<String> tLossSet = new TreeSet<String>();
		TreeSet<String> partialLossSet = new TreeSet<String>();
		mttrSeverityTLBean.forEach(x -> {
			x.getDurationWise().forEach(y -> {
				tLossSet.add(y.getHrsBucket());
			});
		});

		// Partial Loss
		mttrSeverityPLBean.forEach(x -> {
			x.getDurationWise().forEach(y -> {
				partialLossSet.add(y.getHrsBucket());
			});
		});
		tLossSet.addAll(partialLossSet);
		tLossSet.forEach(x -> {
			tlossMap.put(x, 0);
			pLossMap.put(x, 0);
		});
		getSevirityMap(mttrSeverityTLBean, tlossMap);
		getSevirityMap(mttrSeverityPLBean, pLossMap);

		mainMap.put("Total Loss of Service",
				tlossMap.values().stream().map(Object::toString).collect(Collectors.joining(",")));
		mainMap.put("Partial Loss", pLossMap.values().stream().map(Object::toString).collect(Collectors.joining(",")));

		StringBuilder sbstack = new StringBuilder();

		mainMap.forEach((key, value) -> {
			sbstack.append("{").append("name:").append("\'" + key + "\'").append(",").append(" data:")
					.append("[" + value + "]").append("}").append(",");

		});

		String commaSeperatedColmnKeys = tLossSet.stream().collect(Collectors.joining("','", "'", "'"));
		columnchart = columnchart.replace("VALUES", removeLastIndex(sbstack.toString()));
		columnchart = columnchart.replace("KEYS", commaSeperatedColmnKeys);
		columnchart = columnchart.replace("TITLE", title);
		columnchart = columnchart.replace("YAXIZ", yTitle);

		LOGGER.debug("DATA STRING MTTR COLUMN.......\n");
		LOGGER.debug(columnchart);
		String base64Str = null;
		try {
			InputStream is = exportService.connectExportServer(columnchart);
			if (is != null) {
				base64Str = getBase64EncondedImage(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_GENERATING_PDF, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.ERROR_GENERATING_PDF, e, ResponseResource.R_CODE_ERROR);
		}
		return base64Str;
	}

	/**
	 * A method to get severity Map
	 * 
	 * @param mttrSeverityBean
	 * @param hrsMap
	 */
	private void getSevirityMap(List<MTTRSeverityResponseBean> mttrSeverityBean, Map<String, Integer> hrsMap) {
		mttrSeverityBean.forEach(x -> {

			x.getDurationWise().forEach(y -> {
				int[] val = { 0 };
				if (hrsMap.get(y.getHrsBucket()) != null) {
					if (y.getTicketCount() != null) {
						val[0] = val[0] + y.getTicketCount();
						hrsMap.put(y.getHrsBucket(), val[0]);
					}
				}
			});
		});

	}

	/**
	 * A Method to get a line chart for MTTR trend
	 * 
	 * @param mttrTrendTLRFOResponsible
	 * @param mttrTrendPLRFOResponsible
	 * @param title
	 * @return Object
	 * @throws TclCommonException
	 */
	public Object getBasicLineChartForMTTRTrend(List<MTTRTrendRFOWise> mttrTrendTLRFOResponsible,
			List<MTTRTrendRFOWise> mttrTrendPLRFOResponsible, String title) throws TclCommonException {
		LOGGER.info("Inside getBasicLineChartForMTTRTrend");
		String lineChartBasic = HighChartConstants.mttrLine;

		Set<String> tLMonthYrSet = mttrTrendTLRFOResponsible.stream().map(MTTRTrendRFOWise::getMonthYear)
				.collect(Collectors.toSet());
		Set<String> partialLossMonthYrSet = mttrTrendPLRFOResponsible.stream().map(MTTRTrendRFOWise::getMonthYear)
				.collect(Collectors.toSet());
		Set<String> monthYrSet = tLMonthYrSet.stream().collect(Collectors.toSet());
		monthYrSet.addAll(partialLossMonthYrSet);

		Set<String> pLNotPresentSet = CollectionUtils.subtract(monthYrSet, partialLossMonthYrSet).stream()
				.collect(Collectors.toSet());
		Set<String> tLNotPresentSet = CollectionUtils.subtract(monthYrSet, tLMonthYrSet).stream()
				.collect(Collectors.toSet());

		if (pLNotPresentSet != null) {

			mttrTrendPLRFOResponsible.addAll(pLNotPresentSet.stream().map(p -> {
				MTTRTrendRFOWise mttrObj = new MTTRTrendRFOWise();
				mttrObj.setMonthYear(p);
				mttrObj.setAvgMttr("0.00");
				return mttrObj;
			}).collect(Collectors.toList()));
		}

		if (tLNotPresentSet != null) {
			mttrTrendTLRFOResponsible.addAll(tLNotPresentSet.stream().map(p -> {
				MTTRTrendRFOWise mttrObj = new MTTRTrendRFOWise();
				mttrObj.setMonthYear(p);
				mttrObj.setAvgMttr("0.00");
				return mttrObj;
			}).collect(Collectors.toList()));
		}
		// now we have even number of entries in both TL and PL. we have to sort it
		// before constructing json for highcharts.

		List<MTTRTrendRFOWise> sortedTL = mttrTrendTLRFOResponsible.stream().sorted((a, b) -> {
			return a.getMonthYear().compareTo(b.getMonthYear());
		}).collect(Collectors.toList());

		List<MTTRTrendRFOWise> sortedPL = mttrTrendPLRFOResponsible.stream().sorted((a, b) -> {
			return a.getMonthYear().compareTo(b.getMonthYear());
		}).collect(Collectors.toList());

		correctLeadingZerosForHChart(sortedTL);
		List<String> tlListOfAvgMTTR = sortedTL.stream().map(MTTRTrendRFOWise::getAvgMttr).collect(Collectors.toList());

		correctLeadingZerosForHChart(sortedPL);
		List<String> plListOfAvgMTTR = sortedPL.stream().map(MTTRTrendRFOWise::getAvgMttr).collect(Collectors.toList());

		String commaSeperatedLineKeys = sortedPL.stream().map(x -> (convertDateFormat(x.getMonthYear())))
				.collect(Collectors.joining("','", "'", "'"));
		LinkedHashMap<String, String> xAxisMap = new LinkedHashMap<String, String>();

		xAxisMap.put("Total Loss of Service", tlListOfAvgMTTR.stream().collect(Collectors.joining(",")));
		xAxisMap.put("Partial Loss", plListOfAvgMTTR.stream().collect(Collectors.joining(",")));

		StringBuilder sbstack = new StringBuilder();
		xAxisMap.forEach((key, value) -> {
			sbstack.append("{").append("name:").append("\'" + key + "\'").append(",").append(" data:")
					.append("[" + value + "]").append("}").append(",");
		});

		lineChartBasic = lineChartBasic.replace("SERIES_ARR", removeLastIndex(sbstack.toString()));
		lineChartBasic = lineChartBasic.replace("XAXIS_CATEGORIES", commaSeperatedLineKeys);
		lineChartBasic = lineChartBasic.replace("MTTR_TITLE", title);

		LOGGER.debug("DATA STRING.......\n");
		LOGGER.debug(lineChartBasic);
		String base64Str = null;
		try {
			InputStream is = exportService.connectExportServer(lineChartBasic);
			if (is != null) {
				base64Str = getBase64EncondedImage(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_GENERATING_PDF, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.ERROR_GENERATING_PDF, e, ResponseResource.R_CODE_ERROR);
		}
		return base64Str;
	}

	/**
	 * A method to get stacked column chart's base 64 Image
	 * 
	 * @param serviceWiseIncidentPerMonth
	 * @param title
	 * @param productWise
	 * @return Object
	 * @throws TclCommonException
	 */
	public Object getStackedColumnChart(ServerityTicketResponse serviceWiseIncidentPerMonth, String title,
			boolean productWise, String yAxisTitle) throws TclCommonException {
		LOGGER.info("Inside getStackedColumnChart");
		String stackedColumnChart = HighChartConstants.stackedColumnchart;

		LinkedHashMap<String, Map<String, Integer>> mainMap = new LinkedHashMap<String, Map<String, Integer>>();
		Map<String, String> yAxisMap = new HashMap<String, String>();
		ArrayList<String> xAxis = new ArrayList<String>();

		if (!productWise) {
			SimpleDateFormat f = new SimpleDateFormat("MMM-yyyy");
			List<TicketTrend> sorted = serviceWiseIncidentPerMonth.getTrends().stream().sorted((a, b) -> {
				try {
					return f.parse(a.getProductOrMonth()).compareTo(f.parse(b.getProductOrMonth()));
				} catch (ParseException e) {
					LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
				}
				return 0;
			}).collect(Collectors.toList());
			serviceWiseIncidentPerMonth.setTrends(sorted);
		}

		serviceWiseIncidentPerMonth.getTrends().forEach(x -> {
			Map<String, Integer> subMap = new HashMap<String, Integer>();
			subMap.put("Partial Loss", 0);
			subMap.put("No Impact", 0);
			subMap.put("Total Loss of Service", 0);

			if (Objects.nonNull(x.getImpactTickets())) {
				x.getImpactTickets().forEach(y -> {
					if (subMap.containsKey(y.getImpact())) {
						subMap.put(y.getImpact(), y.getCount());
					}
				});
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
			try {
				sdf.parse(x.getProductOrMonth());
				mainMap.put(convertDateFormat(x.getProductOrMonth()), subMap);
			} catch (ParseException e) {
				// x.productOrMonth contains product. put that in map as it is..
				mainMap.put(x.getProductOrMonth(), subMap);
			}
		});

		mainMap.forEach((key, value) -> {
			xAxis.add(key);
			value.forEach((subKey, subVal) -> {
				if (yAxisMap.get(subKey) == null) {
					yAxisMap.put(subKey, String.valueOf(subVal));
				} else {
					yAxisMap.put(subKey, yAxisMap.get(subKey) + "," + subVal);
				}
			});
		});

		StringBuilder sbstack = new StringBuilder();
		yAxisMap.forEach((key, value) -> {
			sbstack.append("{").append("name:").append("\'" + key + "\'").append(",").append(" data:")
					.append("[" + value + "]").append("}").append(",");
		});

		String commaSeperatedStack = xAxis.stream().collect(Collectors.joining("','", "'", "'"));

		stackedColumnChart = stackedColumnChart.replace("SERIES_ARR", removeLastIndex(sbstack.toString()))
				.replace("KEYS", commaSeperatedStack).replace("TITLE", title).replace("YAXIZ", yAxisTitle);

		LOGGER.debug("Data String for Stacked column chart .......\n");
		LOGGER.debug(stackedColumnChart);
		String base64Str = null;
		try {
			InputStream is = exportService.connectExportServer(stackedColumnChart);
			if (is != null) {
				base64Str = getBase64EncondedImage(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_GENERATING_PDF, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.ERROR_GENERATING_PDF, e, ResponseResource.R_CODE_ERROR);
		}
		return base64Str;
	}

	/**
	 * A method to get fault rate column chart in base64 format
	 * 
	 * @param List  of faultBean
	 * @param title
	 * @param y     axis Title
	 * @return object - base 64 string object
	 * @throws TclCommonException
	 */
	public Object getFaultRateColumnChart(List<FaultRate> faultBean, String title, String yTitle)
			throws TclCommonException {

		String columnchart = HighChartConstants.columnchart;
		ArrayList<String> xAxis = new ArrayList<String>();
		Map<String, String> subMap = new HashMap<String, String>();
		faultBean.forEach(x -> {
			xAxis.add(x.getProductOrMonthWise());
			if (x.getCiruitDetail() != null) {

				if (subMap.get(x.getCiruitDetail().getFaultMetricsFaultRate()) != null) {
					subMap.put(x.getCiruitDetail().getFaultMetricsFaultRate(), subMap
							.get(x.getCiruitDetail().getFaultMetricsFaultRate()) + ","
							+ correctLeadingZerosForFaultChart(x.getCiruitDetail().getFaultMetricsFaultRateCount()));
				} else {
					subMap.put(x.getCiruitDetail().getFaultMetricsFaultRate(),
							correctLeadingZerosForFaultChart(x.getCiruitDetail().getFaultMetricsFaultRateCount()));
				}
				if (subMap.get(x.getCiruitDetail().getFaultMetricsImpactedCircuit()) != null) {
					subMap.put(x.getCiruitDetail().getFaultMetricsImpactedCircuit(),
							subMap.get(x.getCiruitDetail().getFaultMetricsImpactedCircuit()) + ","
									+ correctLeadingZerosForFaultChart(
											x.getCiruitDetail().getFaultMetricsImpactedCircuitCount()));
				} else {
					subMap.put(x.getCiruitDetail().getFaultMetricsImpactedCircuit(), correctLeadingZerosForFaultChart(
							x.getCiruitDetail().getFaultMetricsImpactedCircuitCount()));
				}
				if (subMap.get(x.getCiruitDetail().getFaultMetricsTotalCircuit()) != null) {
					subMap.put(x.getCiruitDetail().getFaultMetricsTotalCircuit(), subMap
							.get(x.getCiruitDetail().getFaultMetricsTotalCircuit()) + ","
							+ correctLeadingZerosForFaultChart(x.getCiruitDetail().getFaultMetricsTotalCircuitCount()));
				} else {
					subMap.put(x.getCiruitDetail().getFaultMetricsTotalCircuit(),
							correctLeadingZerosForFaultChart(x.getCiruitDetail().getFaultMetricsTotalCircuitCount()));
				}
			}
		});

		StringBuilder sbstack = new StringBuilder();

		subMap.forEach((key, value) -> {
			if (key.equals("Fault Rate")) {
				key = "Fault Rate%";
			}
			sbstack.append("{").append("name:").append("\'" + key + "\'").append(",").append(" data:")
					.append("[" + value + "]").append("}").append(",");

		});

		String commaSeperatedColmnKeys = xAxis.stream().collect(Collectors.joining("','", "'", "'"));
		columnchart = columnchart.replace("VALUES", removeLastIndex(sbstack.toString()));
		columnchart = columnchart.replace("KEYS", commaSeperatedColmnKeys);
		columnchart = columnchart.replace("TITLE", title);
		columnchart = columnchart.replace("YAXIZ", yTitle);
		LOGGER.debug("DATA STRING.......\n");
		LOGGER.debug(columnchart);
		String base64Str = null;
		try {
			InputStream is = exportService.connectExportServer(columnchart);
			if (is != null) {
				base64Str = getBase64EncondedImage(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_GENERATING_PDF, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.ERROR_GENERATING_PDF, e, ResponseResource.R_CODE_ERROR);
		}
		return base64Str;
	}

	/**
	 * Method to get donut chart from Map's data
	 * 
	 * @param rfoPieMap
	 * @param title
	 * @return
	 * @throws TclCommonException
	 */
	public Object getDonutChart(Map<String, String> rfoPieMap, String title) throws TclCommonException {

		String base64Str = null;
		StringBuilder sb = new StringBuilder();

		rfoPieMap.forEach((key, value) -> {
			sb.append("{").append("name:").append("\'" + key + "\'").append(",").append(" y:").append(value).append("}")
					.append(",");
		});
		String piechart = HighChartConstants.dochart.replace("VALUES", removeLastIndex(sb.toString()));
		piechart = piechart.replace("CHART_TITLE", title);

		try {
			LOGGER.info("---------------------------------------------------");
			LOGGER.info(" The Query String for donut " + title + " is " + piechart);
			LOGGER.info("---------------------------------------------------");

			InputStream is = exportService.connectExportServer(piechart);
			if (is != null) {
				base64Str = getBase64EncondedImage(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_GENERATING_PDF, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.ERROR_GENERATING_PDF, e, ResponseResource.R_CODE_ERROR);
		}
		return base64Str;

	}

	/**
	 * Method to correct the leading zeroes of Average MTTR. Note : for example,
	 * highcharts export server expects 0.20 not 00.20
	 * 
	 * @param String mttrTrendTLRFOResponsible
	 * @return String string without leading zeroes
	 */
	private void correctLeadingZerosForHChart(List<MTTRTrendRFOWise> mttrTrendTLRFOResponsible) {
		mttrTrendTLRFOResponsible.forEach(x -> {
			if (x.getAvgMttr() != null) {
				String[] beforeDecimalTCL = x.getAvgMttr().split("\\.");
				if (beforeDecimalTCL[1].length() > 1) {
					x.setAvgMttr(Integer.valueOf(beforeDecimalTCL[0]) + "." + beforeDecimalTCL[1]);
				} else {
					x.setAvgMttr("" + Integer.valueOf(beforeDecimalTCL[0]));
				}
			}
		});
	}

	/**
	 * Method to correct the leading zeroes of fault value
	 * 
	 * @param input string
	 * @return string without leading zeroes
	 */
	private String correctLeadingZerosForFaultChart(String input) {
		if (input != null) {
			String[] beforeDecimalTCL = input.split("\\.");
			if (beforeDecimalTCL[1].length() > 1) {
				input = Integer.valueOf(beforeDecimalTCL[0]) + "." + beforeDecimalTCL[1];
			} else {
				input = Integer.valueOf(beforeDecimalTCL[0]) + "";
			}
		}
		return input;
	}

	/**
	 * Method to convert byte array to base64 encoded string
	 * 
	 * @param inByte
	 * @return String
	 */
	private String getBase64EncondedImage(byte[] inByte) {
		return Base64.getEncoder().encodeToString(inByte);
	}

	/**
	 * A method to remove last index of string
	 * 
	 * @param inputString
	 * @return stringWithoutLastIndex
	 */
	private String removeLastIndex(String input) {
		return input.substring(0, input.length() - 1);

	}

	/**
	 * A method to sort mttrTrendList - year wise
	 * 
	 * @param mttrTrendTLRFOResponsible - list of MTTR Trends
	 */
	private void sortMttrMonthYrWise(List<MTTRTrendRFOWise> mttrTrendTLRFOResponsible) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
		mttrTrendTLRFOResponsible.forEach(y -> {
			try {
				Date date = formatter.parse(y.getMonthYear());
				y.setMonthYrInDate(date);
			} catch (ParseException e) {
				LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
			}
		});

		mttrTrendTLRFOResponsible.sort(Comparator.comparing(MTTRTrendRFOWise::getMonthYrInDate,
				Comparator.nullsFirst(Comparator.naturalOrder())));
	}

	/**
	 * Method to convert date format
	 * 
	 * @param dateStr
	 * @return MMM-yy format date String
	 */
	private String convertDateFormat(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
		Date dNow = null;
		try {
			dNow = sdf.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
		}

		SimpleDateFormat ft1 = new SimpleDateFormat("MMM-yy");
		LOGGER.info("Product wis uptime trend X axis dates :: " + ft1.format(dNow));
		return ft1.format(dNow);
	}

	/**
	 * Converting a string date in one format to date string of another format.
	 * 
	 * @author KRUTSRIN
	 * @param sourceForm
	 * @param destinationForm
	 * @param strDate
	 * @return dateString
	 */
	public static String convertDateStrFormat(String sourceForm, String destinationForm, String strDate) {
		SimpleDateFormat sdfSource = new SimpleDateFormat(sourceForm);
		Date date = null;
		try {
			date = sdfSource.parse(strDate);
		} catch (ParseException e) {
			LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
		}
		SimpleDateFormat sdfDestination = new SimpleDateFormat(destinationForm);
		strDate = sdfDestination.format(date);
		return strDate;
	}
}
