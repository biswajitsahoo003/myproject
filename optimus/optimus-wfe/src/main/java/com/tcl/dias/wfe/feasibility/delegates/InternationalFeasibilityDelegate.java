package com.tcl.dias.wfe.feasibility.delegates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.wfe.constants.WFEConstants;
import com.tcl.dias.wfe.feasibility.utils.RFeasibilityPostUtil;

/**
 * This delegate is used to trigger the international engine CQ model.
 * 
 * @author PAULRAJ SUNDAR
 *
 */
@Component
public class InternationalFeasibilityDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(InternationalFeasibilityDelegate.class);

	@Autowired
	RFeasibilityPostUtil feasibilityUtil;

	@Value("${cq.ticket.url}")
	String ticketUrl;
	
	@Value("${cq.service.ticket.url}")
	String serviceTicketUrl;
	
	@Value("${cq.getQuote.url}")
	String getQuoteUrl;

	@Value("${cq.service.tkt.quote.url}")
	String getServiceTktQuoteUrl;
	
	@Value("${intl.access.pricing.url}")
	String requestUrl;
	
	@Value("${intl.access.url}")
	String requestIntlUrl;
	
	@Autowired
	RestClientService restClientService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<Map<String, Object>> intlSites = (List<Map<String, Object>>) execution.getVariable("intlSites");
		try {
			JSONArray arr = new JSONArray();
			arr.addAll(intlSites);
			JSONObject topObj = new JSONObject();
			topObj.put("input_data", arr);
			LOGGER.info("value::: " + intlSites);
			LOGGER.info("topObje;;;::: " + topObj);
			
			/***** CQ model *****/
			/*String ticketId = getTicket();
			String serviceTicket = getServiceTicket(ticketId.trim());
			getQuoteUrl(serviceTicket.trim(), topObj, execution);*/
			/****** MCG international model ******/
			//getInternationalAccessDetails(topObj,execution);
			
			getAccessDetails(topObj,execution);
		} catch (Exception e) {
			LOGGER.info("Inside the Intl delegate:: Exception:::: ");
			throw new Exception();
		}
	}
	
	private void  getAccessDetails(JSONObject input, DelegateExecution execution) throws Exception {
		
		try {
			/*
			 * Map<String, List<Map<String, Object>>> intFeaiblelDetails = new HashMap<>();
			 * Map<String, List<Map<String, Object>>> intNotFeaiblelDetails = new
			 * HashMap<>();
			 */
		List<Map<String, Object>> intFeaiblelDetails = new ArrayList<>();
		List<Map<String, Object>> intNotFeaiblelDetails = new ArrayList();
		
		//List<Map<String, Object>> accessResult = feasibilityUtil.postURL(input.toString(), requestIntlUrl);
			RestResponse response;
			try {
				response = restClientService.post(requestIntlUrl, input.toString());
			} catch (Exception e) {
				LOGGER.warn("Error from international feasibility response", e.getMessage());
				throw new Exception(e);
			}
			LOGGER.info("response::: "+response);
		JSONArray r1Output=null;
		try {
			//r1Output = feasibilityUtil.postURL(topObj.toString(), requestUrl);
			r1Output = (JSONArray) new JSONParser().parse(response.getData());
			
		} catch (Exception e) {
			LOGGER.info("Inside the Intl Feasibility Task::: Exception:::: ");
			throw new RuntimeException();
		}
		List<Map<String, Object>> intlResponse = (List<Map<String, Object>>) r1Output;
		
		LOGGER.info("Inside the Intl Feasiblity WFE response:::"+r1Output.toString());
		
		System.out.println("intlResponse :::::::: "+intlResponse.size());
		execution.setVariable("testSites", intlResponse);
		
		execution.setVariable("IntlCqResponse", intlResponse);
		execution.setVariable("intlNotFeasible", intlResponse);
		
		}catch(Exception e) {
			LOGGER.info("getAccessDetails exception::::::");
		}
		
	}
    	
	
/**
 * This method is used to trigger the ticket url and get the Ticket ID
 * @return
 * @throws Exception
 */
	private String getTicket() throws Exception {
 
		String response = "";
		String ticketId="";
		RestResponse tktResponse = feasibilityUtil.postCqUrl(ticketUrl, WFEConstants.TICKET_URL_PARAMETERS, "Ticket");
		if (tktResponse.getStatus() == Status.SUCCESS) {
			response = tktResponse.getData();
		}
		if(StringUtils.isNotEmpty(response)) {
			ticketId = response.substring(62, response.length() - 1);
		}
		return ticketId;
	}
/**
 * This method is used to trigger the service ticket url and get the serviceTicket ID
 * @param ticketId
 * @return
 */
	private String getServiceTicket(String ticketId) {
		LOGGER.info("ticket Id::: ",ticketId);

		String response = "";
		String urlService = serviceTicketUrl;
		String tktName = ticketId;
		StringBuilder url = new StringBuilder();
		url.append(urlService).append(tktName);
		String requestBody = "service="+getServiceTktQuoteUrl;
		RestResponse tktResponse = feasibilityUtil.postCqUrl(url.toString(), requestBody, "ServiceTicket");
		if (tktResponse.getStatus() == Status.SUCCESS) {
			response = tktResponse.getData();
		}
		LOGGER.info("service ticket response::: ",response);
		return response;
	}

	/**
	 * This method is used to get the Quote response for international sites.
	 * @param serviceTicket
	 * @param input
	 * @param execution
	 * @throws Exception
	 */
	private void getQuoteUrl(String serviceTicket, JSONObject input, DelegateExecution execution) throws Exception {

		JSONArray arr = (JSONArray) input.get("input_data");
		List<Map<String, Object>> list = (List<Map<String, Object>>) arr;
		//String url = "";
		String ticket = serviceTicket;
		//String getQuoteUrl = "https://rfeueigprod.ondemand.sas.com/ClickQuote2/getQuote?";
		String resp = "";
		Map<String, List<Map<String, Object>>> intFeaiblelDetails = new HashMap<>();
		Map<String, List<Map<String, Object>>> intNotFeaiblelDetails = new HashMap<>();

		//List<Map<String, Object>> notFeasible = null;
		//for (Map map : list) {
		list.stream().forEach( map ->{
			String country = (String) map.get("country");
			int contractTerm = getMothsforLastMileContractTerm((String) map.get("last_mile_contract_term"));

			Double bw_mbps = (Double) map.get("bw_mbps");
			Double latitude_final = (Double) map.get("latitude_final");
			Double longitude_final = (Double) map.get("longitude_final");
			String product_name = (String) map.get("product_name");
			String site_id = (String) map.get("site_id");
			String topology = (String) map.get("topology");

			String bandWidth = "";

			if (bw_mbps.equals(0.25) || bw_mbps.equals(0.5)) {
				bandWidth = bw_mbps.toString();
			} else {
				Integer bw = bw_mbps.intValue();
				bandWidth = String.valueOf(bw);
			}

			/*String test = "https://rfeueigprod.ondemand.sas.com/ClickQuote2/getQuote?RQ_Lat=40.75749697&RQ_Product_Type=Global%20VPN&RQ_Contract_Term=12&RQ_Bandwidth=16M&"
					+ "RQ_Long=-73.971162&RQ_Country=UNITED%20STATES&ticket=ST-382194-Ko1cA3elVuV3wfiXYq9F-cas";*/

			final String url = org.apache.commons.lang3.StringUtils.join(getQuoteUrl,
					"RQ_Lat=" + latitude_final + "&RQ_Product_Type=" + product_name + "&RQ_Contract_Term="
							+ contractTerm + "&RQ_Bandwidth=" + bandWidth + "M" + "&RQ_Long=" + longitude_final
							+ "&RQ_Country=" + country + "&ticket=" + ticket);

			// String newUrl =
			// getQuoteUrl+"RQ_Lat="+latitude_final+"&RQ_Product_Type="+product_name+"&RQ_Contract_Term="+contractTerm+"&RQ_Bandwidth="+bandWidth+"M"+"&RQ_Long="+longitude_final+"&RQ_Country="+country+"&ticket="+ticket;
			if (!site_id.contains("secondary")) {  // No need to process secondary site, CQ will process and provide primary & secondary details...
				RestResponse response = feasibilityUtil.get(url);

				if (response.getStatus() == Status.SUCCESS) {
					//resp = response.getData();
					JSONArray jsonObject;
					try {
						jsonObject = (JSONArray) new JSONParser().parse(response.getData());
					} catch (ParseException e) {
						throw new RuntimeException(e);
					}
					List<Map<String, Object>> rArray = (List<Map<String, Object>>) jsonObject;
					System.out.println("jsonArray:: " + jsonObject.toString());
					rArray.stream().forEach(respon -> { // Require all the portal input should be appended in all the CQ responses (List of responses)- R-Code dependency
							respon.put(WFEConstants.RANK, "1");
							respon.put(WFEConstants.TYPE, "INTL");
							map.put(WFEConstants.SELECTED, true);
							respon.putAll(map);
					});
					intFeaiblelDetails.put(site_id, rArray);
				} else {
					List<Map<String, Object>> notFeasible = new ArrayList<>();
					map.put(WFEConstants.TYPE, "INTL");
					map.put(WFEConstants.SELECTED, false);
					notFeasible.add(map);
					intNotFeaiblelDetails.put(site_id, notFeasible);
				}
			} else {
				String[] splitter = site_id.split("_");
				String siteId = splitter[0];
				String type = splitter[1];
				if (null != intFeaiblelDetails.get(siteId + "_primary")) {
					List<Map<String, Object>> listValues = intFeaiblelDetails.get(siteId + "_primary");
					listValues.stream().forEach(secondary -> {
						secondary.put("topology_secondary", topology);
					});

				} else if (null != intNotFeaiblelDetails.get(siteId + "_primary")) {
					List<Map<String, Object>> listValues = intNotFeaiblelDetails.get(siteId + "_primary");
					listValues.get(listValues.size() - 1).put("topology_secondary", topology);
				}
			}
		});

		JSONObject top = new JSONObject();
		top.put("input_data", mapToJsonWithoutPrimaryKey(intFeaiblelDetails));  // This format is expected from solution Prioritization API
		execution.setVariable("IntlCqResponse", intFeaiblelDetails);
		execution.setVariable("intlNotFeasible", intNotFeaiblelDetails);

		LOGGER.info("Json value for details GETqUOTE cq:::: ",top.toJSONString());

	}
/**
 * This method is used to get the year
 * @param year
 * @return
 */
	private Integer getMothsforLastMileContractTerm(String year) {
		Integer month = 0;
		if (year != null) {
			String reg[] = year.split(" ");
			if (reg.length > 0) {
				if (StringUtils.isNumeric(reg[0])) {
					month = Integer.valueOf(reg[0]);
				}
			}
		}
		return month * 12;
	}

	/**
	 * This method is used to convert json without primary attribute 
	 * @param topMap
	 * @return
	 */
	private static JSONArray mapToJsonWithoutPrimaryKey(Map<String, List<Map<String, Object>>> topMap) {
		JSONArray jArray = new JSONArray();
		//for (Entry<String, List<Map<String, Object>>> entry : topMap.entrySet()) {
		topMap.entrySet().stream().forEach(entry -> {
			JSONArray arr = new JSONArray();
			//for (Map<String, Object> map : entry.getValue()) {
			entry.getValue().stream().forEach(map ->{
				JSONObject jObj = new JSONObject();
				jObj.putAll(map);
				arr.add(jObj);
			});
			jArray.addAll(arr);
		});
		return jArray;
	}
	
	
	/**
	 * This method is used to get the Quote response for international sites.
	 * @param serviceTicket
	 * @param input
	 * @param execution
	 * @throws Exception
	 */
	private void getInternationalAccessDetails(JSONObject input, DelegateExecution execution) throws Exception {

		try {
			
		JSONArray arr = (JSONArray) input.get("input_data");
		List<Map<String, Object>> list = (List<Map<String, Object>>) arr;
		//String url = "";
		//String ticket = serviceTicket;
		//String getQuoteUrl = "https://rfeueigprod.ondemand.sas.com/ClickQuote2/getQuote?";
		String resp = "";
		Map<String, List<Map<String, Object>>> intFeaiblelDetails = new HashMap<>();
		Map<String, List<Map<String, Object>>> intNotFeaiblelDetails = new HashMap<>();

		//List<Map<String, Object>> notFeasible = null;
		//for (Map map : list) {
		list.stream().forEach( map ->{
			String country = (String) map.get("country");
			int contractTerm = getMothsforOpportunityTerms((String) map.get("last_mile_contract_term"));

			Double bw_mbps = (Double) map.get("bw_mbps");
			Double latitude_final = (Double) map.get("latitude_final");
			Double longitude_final = (Double) map.get("longitude_final");
			String product_name = (String) map.get("product");
			String site_id = (String) map.get("site_id");
			String topology = (String) map.get("topology");
			String address = (String)map.get("address");
			String zip_code = (String)map.get("zip_code");

			String bandWidth = "";

			if (bw_mbps.equals(0.25) || bw_mbps.equals(0.5)) { // Bandwidth is KBs bw_mbps>=0 and bw_mbps<=0.99
				bandWidth = bw_mbps.toString() + "M";
			}else if(bw_mbps >= 1000) {
				bandWidth = String.valueOf(bw_mbps.intValue()/1000) + "G";
			}else {
				Integer bw = bw_mbps.intValue();
				bandWidth = String.valueOf(bw) + "M";
			}

			JSONObject topObj = new JSONObject();
			topObj.put("bandwidth", bandWidth);
			topObj.put("latitude", latitude_final);
			topObj.put("longitude", longitude_final);
			topObj.put("product", product_name);
			topObj.put("address", address);
			topObj.put("zip_code", zip_code);
			topObj.put("cont_term", contractTerm);
						
			if (!site_id.contains("secondary")) {  // No need to process secondary site, CQ will process and provide primary & secondary details...
		//		RestResponse response = feasibilityUtil.get(url);
				//List<Map<String, Object>> r1Output=null;
				LOGGER.info("Input Object to pricing : " + topObj.toString());
				RestResponse response = restClientService.post(requestUrl, topObj.toString());
				JSONArray r1Output=null;
				try {
					//r1Output = feasibilityUtil.postURL(topObj.toString(), requestUrl);
					r1Output = (JSONArray) new JSONParser().parse(response.getData());
					
				} catch (Exception e) {
					LOGGER.info("Inside the Intl Feasibility Task::: Exception:::: ");
					throw new RuntimeException();
				}
				List<Map<String, Object>> intlResponse = (List<Map<String, Object>>) r1Output;
				execution.setVariable("testSites", intlResponse);
				//intlResponse.contains("error")
				if(Objects.nonNull(intlResponse) && !intlResponse.get(0).keySet().contains("error")) {
					intlResponse.stream().forEach(respon -> { // Require all the portal input should be appended in all the CQ responses (List of responses)- R-Code dependency
							respon.put(WFEConstants.RANK, "1");
							respon.put(WFEConstants.TYPE, "INTL");
							map.put(WFEConstants.SELECTED, true);
							respon.putAll(map);
					});
					intFeaiblelDetails.put(site_id, intlResponse);
				} else {
					List<Map<String, Object>> notFeasible = new ArrayList<>();
					map.put(WFEConstants.TYPE, "INTL");
					map.put(WFEConstants.SELECTED, false);
					if(Objects.nonNull(intlResponse) && !intlResponse.isEmpty())
						map.put("error", intlResponse.get(0).get("error"));
					notFeasible.add(map);
					intNotFeaiblelDetails.put(site_id, notFeasible);
				}
			} else {
				String[] splitter = site_id.split("_");
				String siteId = splitter[0];
				String type = splitter[1];
				if (null != intFeaiblelDetails.get(siteId + "_primary")) {
					List<Map<String, Object>> listValues = intFeaiblelDetails.get(siteId + "_primary");
					listValues.stream().forEach(secondary -> {
						secondary.put("topology_secondary", topology);
					});
				} else if (null != intNotFeaiblelDetails.get(siteId + "_primary")) {
					List<Map<String, Object>> listValues = intNotFeaiblelDetails.get(siteId + "_primary");
					listValues.get(listValues.size() - 1).put("topology_secondary", topology);
				}
			}
			
		});

		JSONObject top = new JSONObject();
		top.put("input_data", mapToJsonWithoutPrimaryKey(intFeaiblelDetails));  // This format is expected from solution Prioritization API
		execution.setVariable("IntlCqResponse", intFeaiblelDetails);
		execution.setVariable("intlNotFeasible", intNotFeaiblelDetails);

		LOGGER.info("Json value for details GETqUOTE cq:::: ",top.toJSONString());
		}catch(Exception e) {
			throw new RuntimeException();
		}
	}
	
	private Integer getMothsforOpportunityTerms(String termPeriod) {
		String reg[] = termPeriod.split(CommonConstants.MULTI_SPACE);
		Integer month =Integer.valueOf(reg[0]);
		if (reg.length > 0) {
			if (termPeriod.contains("year")) {
				return month * 12;
			}
		}
		return month;
	}

}
