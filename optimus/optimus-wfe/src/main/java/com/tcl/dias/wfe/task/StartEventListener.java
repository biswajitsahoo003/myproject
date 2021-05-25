package com.tcl.dias.wfe.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.wfe.dto.ProcessInput;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

public class StartEventListener implements ExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartEventListener.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.processFeasibilty}") })
	public String processFeasibiltyMessage(String responseBody) throws TclCommonException {
		String response = null;
		try {
			JSONParser jsonParser = new JSONParser();
			try {
				JSONObject top = (JSONObject) jsonParser.parse(responseBody);
				JSONArray arr = (JSONArray) top.get("input_data");
				List<Map<String, Object>> list = (List<Map<String, Object>>) arr;

				for (Map map : list) {
					System.out.println("map entry set::: " + map.entrySet().toString());
					// map.forEach((k,v)->System.out.println("Key ::: " + k + " ::: Value : " +
					// v));;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			throw new TclCommonException(e);
		}
		return response;
	}

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Random r = new Random(System.currentTimeMillis());
		/*
		 * List<Map<String, Object>> siteArray = new ArrayList<>(); for (int i = 0; i <
		 * 10; i++) { Map<String, Object> map = new HashMap<>(); map.put("ID",
		 * r.nextInt()); if (i == 0) map.put("bandwidth", 10); if (i == 1)
		 * map.put("bandwidth", 11); if (i == 2) map.put("bandwidth", 20); if (i == 3)
		 * map.put("bandwidth", 5); if (i == 4) map.put("bandwidth", 12); if (i == 5)
		 * map.put("bandwidth", 13); if (i == 6) map.put("bandwidth", 14); if (i == 7)
		 * map.put("bandwidth", 15); if (i == 8) map.put("bandwidth", 17); if (i == 9)
		 * map.put("bandwidth", 19); map.put("LAT", r.nextFloat()); map.put("LONG",
		 * r.nextFloat()); map.put("FEASIBILITY", null); siteArray.add(map);
		 * System.out.println("Site Information:" + map.entrySet().toString()); }
		 */

		List<Map<String, Object>> siteArray = new ArrayList<>();

		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject top = (JSONObject) jsonParser.parse(responseBody());
			JSONArray arr = (JSONArray) top.get("input_data");
			siteArray = (List<Map<String, Object>>) arr;

			for (Map map : siteArray) {
				System.out.println("map entry set::: " + map.entrySet().toString());
				// map.forEach((k,v)->System.out.println("Key ::: " + k + " ::: Value : " +
				// v));;
			}

			// runtimeService.setVariable("new_process_1", "siteArray", siteArray);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// LOGGER.info("All site information:::::{}",siteArray);

		execution.setVariable("siteArray", siteArray);

		/*
		 * List<List<Map<String, Object>>> r1Quotes = new ArrayList<>();
		 * List<List<Map<String, Object>>> r2Quotes = new ArrayList<>();
		 * List<List<Map<String, Object>>> r3Quotes = new ArrayList<>();
		 */

		List<Map<String, Object>> r1Sites = new ArrayList<>();
		List<Map<String, Object>> r2Sites = new ArrayList<>();
		List<Map<String, Object>> r3Sites = new ArrayList<>();
		execution.setVariable("r1site", r1Sites);
		execution.setVariable("r2site", r2Sites);
		execution.setVariable("r3site", r3Sites);
	}

	private String responseBody() {
		return "{\"input_data\":[\r\n" + "		{\"Latitude_final\" : \"30.19512\",\r\n"
				+ "                    \"Longitude_final\" : \"78.191955\",\r\n"
				+ "                    \"prospect_name\" : \"Aircel Limited\",\r\n"
				+ "                    \"BW_mbps\" : \"2\",\r\n" + "					\"Burstable_BW\" : \"2\",\r\n"
				+ "                    \"resp_city\" : \"delhi\",\r\n"
				+ "                    \"resp_state\" : \"delhi\",                  \r\n"
				+ "                    \"Customer_Segment\" : \"Carriers\",\r\n"
				+ "                    \"Sales.Org\" : \"Service Provider\",\r\n"
				+ "                    \"Product.Name\" : \"Global VPN\",                    \r\n"
				+ "                    \"Feasibility.Response..Created.Date\" : \"2016-10-18\",\r\n"
				+ "                    \"local_loop_interface\" : \"FE\",\r\n"
				+ "                    \"last_mile_contract_term\" : \"2 Years\"\r\n"
				+ "					\"Site_id\":\"1\"},\r\n" + "	{\"Latitude_final\" : \"28.4743879\",\r\n"
				+ "                    \"Longitude_final\" : \"77.5039904\",\r\n"
				+ "                    \"prospect_name\" : \"Aircel Limited\",\r\n"
				+ "                    \"BW_mbps\" : \"10\",\r\n" + "					\"Burstable_BW\" : \"2\",\r\n"
				+ "                    \"resp_city\" : \"Noida\",\r\n"
				+ "                    \"resp_state\" : \"Uttar Pradesh\",                  \r\n"
				+ "                    \"Customer_Segment\" : \"Carriers\",\r\n"
				+ "                    \"Sales.Org\" : \"Service Provider\",\r\n"
				+ "                    \"Product.Name\" : \"Global VPN\",                    \r\n"
				+ "                    \"Feasibility.Response..Created.Date\" : \"2017-02-01\",\r\n"
				+ "                    \"local_loop_interface\" : \"100-Base-TX\",\r\n"
				+ "                    \"last_mile_contract_term\" : \"2 Years\",\r\n"
				+ "					\"Site_id\":\"2\"},\r\n" + "	{\"Latitude_final\" : \"15.3534664\",\r\n"
				+ "                    \"Longitude_final\" : \"75.1385137\",\r\n"
				+ "                    \"prospect_name\" : \"ICICI Group\",\r\n"
				+ "                    \"BW_mbps\" : \"5\",\r\n" + "					\"Burstable_BW\" : \"2\",\r\n"
				+ "                    \"resp_city\" : \"Hubli\",\r\n"
				+ "                    \"resp_state\" : \"Karnataka\",                  \r\n"
				+ "                    \"Customer_Segment\" : \"Enterprise - Strategic\",\r\n"
				+ "                    \"Sales.Org\" : \"Enterprise\",\r\n"
				+ "                    \"Product.Name\" : \"Global VPN\",                    \r\n"
				+ "                    \"Feasibility.Response..Created.Date\" : \"2017-02-01\",\r\n"
				+ "                    \"local_loop_interface\" : \"FE\",\r\n"
				+ "                    \"last_mile_contract_term\" : \"1 Year\",\r\n"
				+ "					\"Site_id\":\"3\"},\r\n" + "	{\"Latitude_final\" : \"13.0668368\",\r\n"
				+ "                    \"Longitude_final\" : \"80.2512367\",\r\n"
				+ "                    \"prospect_name\" : \"Euronet Worldwide Inc\",\r\n"
				+ "                    \"BW_mbps\" : \"9\",\r\n" + "					\"Burstable_BW\" : \"2\",\r\n"
				+ "                    \"resp_city\" : \"Chennai\",\r\n"
				+ "                    \"resp_state\" : \"Tamil Nadu\",                  \r\n"
				+ "                    \"Customer_Segment\" : \"Enterprise-Partners Account\",\r\n"
				+ "                    \"Sales.Org\" : \"Enterprise\",\r\n"
				+ "                    \"Product.Name\" : \"Global VPN\",                    \r\n"
				+ "                    \"Feasibility.Response..Created.Date\" : \"2017-01-01\",\r\n"
				+ "                    \"local_loop_interface\" : \"100-Base-TX\",\r\n"
				+ "                    \"last_mile_contract_term\" : \"1 Year\",\r\n"
				+ "					\"Site_id\":\"4\"},\r\n" + "	{\"Latitude_final\" : \"25.3254276\",\r\n"
				+ "                    \"Longitude_final\" : \"83.0092803\",\r\n"
				+ "                    \"prospect_name\" : \"Sify Technologies Limited\",\r\n"
				+ "                    \"BW_mbps\" : \"1\",\r\n" + "					\"Burstable_BW\" : \"2\",\r\n"
				+ "                    \"resp_city\" : \"Varanasi\",\r\n"
				+ "                    \"resp_state\" : \"Uttar Pradesh\",                  \r\n"
				+ "                    \"Customer_Segment\" : \"Carriers\",\r\n"
				+ "                    \"Sales.Org\" : \"Service Provider\",\r\n"
				+ "                    \"Product.Name\" : \"Global VPN\",                    \r\n"
				+ "                    \"Feasibility.Response..Created.Date\" : \"2017-02-01\",\r\n"
				+ "                    \"local_loop_interface\" : \"100-Base-TX\",\r\n"
				+ "                    \"last_mile_contract_term\" : \"1 Year\",\r\n"
				+ "					\"Site_id\":\"5\"}\r\n" + "]}";
	}

}
