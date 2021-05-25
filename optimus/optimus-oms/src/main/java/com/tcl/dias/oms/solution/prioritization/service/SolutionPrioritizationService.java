package com.tcl.dias.oms.solution.prioritization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.service.OmsUtilService;

/**
 * 
 * This class is used to prioritize the solution
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class SolutionPrioritizationService {

	@Autowired
	OmsUtilService omsUtilService;

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SolutionPrioritizationService.class);

	/**
	 * 
	 * Solution Prioritize the Input
	 * 
	 * @param inputData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> solutionPrioritizeTheInput(List<Map<String, Object>> inputData) {
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> accessListToReturn = new ArrayList<Map<String, Object>>();

		try {
			if (inputData == null || inputData.isEmpty()) {
				LOGGER.info("Empty input data for solution prioritization");
			}
			inputData.stream().forEach(ipData -> {
				if (ipData.containsKey("access")) {
					LOGGER.info("Feasible response came");
					Map<String, Object> ipMap = new HashMap<>();
					ipMap.putAll(ipData);
					ipMap.remove("access");
					List<Map<String, Object>> accessList = (List<Map<String, Object>>) ipData.get("access");
					if (accessList != null && !accessList.isEmpty()) {
						accessList.stream().forEach(map -> {
							Double providerMrc = 0D;
							Double xConnectMrc = 0D;
							Double totalMrc = 0D;
							if (map.containsKey("provider_MRC_BW_Currency")) {
								LOGGER.info("provider_MRC_BW_Currency present!!");
								if (!map.get("provider_MRC_BW_Currency").toString().equals("USD")) {
									LOGGER.info("Found a different currency {}", map.get("provider_MRC_BW_Currency"));
									providerMrc = convertCostForProvider(map);
									map.put("provider_MRC_BW_Currency", CommonConstants.USD);
									map.put("provider_MRCCost", providerMrc);
									LOGGER.info("Provider MRC {}", providerMrc);
									convertNrcCostForProvider(map);
								}
								else {
									LOGGER.info("Found a USD currency");
									providerMrc = Double.parseDouble(map.get("provider_MRCCost").toString());
								}

							}
							if (map.containsKey("x_connect_Xconnect_MRC_Currency")) {
								LOGGER.info("x_connect_Xconnect_MRC_Currency present!!");
								if (!map.get("x_connect_Xconnect_MRC_Currency").toString().equals("USD")) {
									LOGGER.info("Found a different currency {}",
											map.get("x_connect_Xconnect_MRC_Currency"));
									xConnectMrc = convertCostForXConnect(map);
									map.put("x_connect_Xconnect_MRC_Currency", CommonConstants.USD);
									map.put("x_connect_Xconnect_MRC", xConnectMrc);
									LOGGER.info("XConnect MRC {}", xConnectMrc);
									convertNrcCostForXConnect(map);
								}
								else {
									LOGGER.info("Found a USD currency");
									xConnectMrc = Double.parseDouble(map.get("x_connect_Xconnect_MRC").toString());
								}
							}
							totalMrc = Double.sum(providerMrc, xConnectMrc);
							map.put("total_MRC", totalMrc);
							map.putAll(ipMap);
							LOGGER.info("Total MRC {}", totalMrc);
						});
						// Getting the Total MRC Sorted
						SortedSet<Double> totalMrcSet = new TreeSet<>();
						accessList.stream().forEach(map -> {
							if (map.containsKey("total_MRC")) {
								totalMrcSet.add(Double.parseDouble(map.get("total_MRC").toString()));
							}
						});
						LOGGER.info("least total MRC in {} is {} ", totalMrcSet, totalMrcSet.first());
						accessList.stream().forEach(map -> {
							if (map.containsKey("total_MRC") && map.get("total_MRC").equals(totalMrcSet.first())) {
								/*map.put(CommonConstants.PREDICTED_ACCESS_FEASIBILITY, CommonConstants.FEASIBLE);
								map.put(CommonConstants.SELECTED, true);*/
								/** ALL request should be go to not-feasible - new requirement for Jan-08-2020 release**/
								map.put(CommonConstants.PREDICTED_ACCESS_FEASIBILITY, CommonConstants.NOT_FEASIBLE);
								map.put(CommonConstants.SELECTED, false);
							} else {
								map.put(CommonConstants.PREDICTED_ACCESS_FEASIBILITY, CommonConstants.NOT_FEASIBLE);
								map.put(CommonConstants.SELECTED, false);
							}
							if(map.get("backup_port_requested").equals("Yes")) {
								map.put(CommonConstants.PREDICTED_ACCESS_FEASIBILITY, CommonConstants.NOT_FEASIBLE);
								map.put(CommonConstants.SELECTED, false);								
							}
							map.put(CommonConstants.RANK, "1");
							map.put(CommonConstants.TYPE, "INTL");
							accessListToReturn.add(map);
						});
					}else {
						ipData.put(CommonConstants.PREDICTED_ACCESS_FEASIBILITY, CommonConstants.NOT_FEASIBLE);
						ipData.put(CommonConstants.SELECTED, false);
						ipData.put(CommonConstants.RANK, "1");
						ipData.put(CommonConstants.TYPE, "INTL");
						ipData.put("access_empty", true);
						accessListToReturn.add(ipData);
					}
				}
			});
			LOGGER.info("Prediction completed for access Lists");
			//response.put("results", accessListToReturn);
		} catch (Exception e) {
			LOGGER.error("Error in prioritizing", e);
		}
		return accessListToReturn;
	}

	private Double convertCostForProvider(Map<String, Object> map) {
		try {
			if (map.containsKey("provider_MRCCost")) {
				String mrc = map.get("provider_MRCCost").toString();
				return omsUtilService.convertCurrency(map.get("provider_MRC_BW_Currency").toString().toUpperCase(),
						CommonConstants.USD, Double.parseDouble(mrc));
			}
		} catch (Exception e) {
			LOGGER.error("Error on converting the Provider MRC ", e);
		}
		return 0D;
	}

	private Double convertCostForXConnect(Map<String, Object> map) {
		try {
			if (map.containsKey("x_connect_Xconnect_MRC")) {
				String mrc = map.get("x_connect_Xconnect_MRC").toString();
				return omsUtilService.convertCurrency(
						map.get("x_connect_Xconnect_MRC_Currency").toString().toUpperCase(), CommonConstants.USD,
						Double.parseDouble(mrc));
			}
		} catch (Exception e) {
			LOGGER.error("Error on converting the XConnect MRC ", e);
		}
		return 0D;
	}

	private void convertNrcCostForProvider(Map<String, Object> map) {
		try {
			if (map.containsKey("provider_NRCCost")) {
				map.put("provider_NRCCost",
						omsUtilService.convertCurrency(map.get("provider_MRC_BW_Currency").toString().toUpperCase(),
								CommonConstants.USD, Double.parseDouble(map.get("provider_NRCCost").toString())));
			}
		} catch (Exception e) {
			LOGGER.error("Error on converting the Provider NRC ", e);
		}
	}

	private void convertNrcCostForXConnect(Map<String, Object> map) {
		try {
			if (map.containsKey("x_connect_Xconnect_NRC")) {
				map.put("x_connect_Xconnect_NRC",
						omsUtilService.convertCurrency(map.get("provider_MRC_BW_Currency").toString().toUpperCase(),
								CommonConstants.USD, Double.parseDouble(map.get("x_connect_Xconnect_NRC").toString())));
			}
		} catch (Exception e) {
			LOGGER.error("Error on converting the Xconnect NRC ", e);
		}
	} 

}
