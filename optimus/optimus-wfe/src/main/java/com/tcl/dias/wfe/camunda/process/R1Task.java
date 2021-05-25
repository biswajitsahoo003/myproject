package com.tcl.dias.wfe.camunda.process;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class R1Task implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(R1Task.class);

	@Value("${onnet.wireline.request}")
	String requestUrl;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		try {
			List<Map<String, Object>> r1Sites = (List<Map<String, Object>>) execution.getVariable("r1site");

			JSONArray arr = new JSONArray();
			arr.addAll(r1Sites);
			JSONObject topObj = new JSONObject();
			topObj.put("input_data", arr);
			List<Map<String, Object>> r1Output = postURL(topObj.toString());

			execution.setVariable("r1Output", r1Output);

			LOGGER.info("Inside the R1 Task :::::: " + r1Sites.size());

			for (Map<String, Object> map1 : r1Sites) {
				for (Map<String, Object> map2 : r1Output) {
					if (map1.get("site_id").equals(map2.get("site_id"))) {
						map1.putAll(map2);
						// break;
					}
				}
			}

		} catch (Exception e) {
			LOGGER.info("Inside the R1 Task::: Exception:::: ");
			throw new Exception();
		}
	}

	private List<Map<String, Object>> postURL(String request) throws Exception {

		// String query = "http://10.133.208.121/onnet-wireline/api";
		String json = request;
		List<Map<String, Object>> r1Array = null;
		// try {
		LOGGER.info("Inside the R1 post URL");
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");

		OutputStream os = conn.getOutputStream();
		os.write(json.getBytes("UTF-8"));
		os.close();

		// read the response
		InputStream in = new BufferedInputStream(conn.getInputStream());
		String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
		if (result.contains("NaN")) {
			result = result.replaceAll("NaN", "null");
		}
		JSONObject jsonObject = (JSONObject) new JSONParser().parse(result);
		JSONArray arr = (JSONArray) jsonObject.get("results");
		r1Array = (List<Map<String, Object>>) arr;

		for (Map map : r1Array) {
			Double errorMessage =0.0;	
			if(map.get("error_flag") instanceof Double) {
				 errorMessage = (Double)map.get("error_flag");
				LOGGER.info("Double instance:::: ");
			} else if(map.get("error_flag") instanceof Integer) {
				errorMessage = new Double((Integer)map.get("error_flag"));
				LOGGER.info("Integer instance:::: ");
			}
			if (errorMessage.equals(1)) { // If any error from R model response then will send the error message to
				LOGGER.info("R1 Model Exception flag 1::::");						// response queue
				throw new Exception();
			}
		}

		in.close();
		conn.disconnect();
		/*
		 * }catch(Exception e) {
		 * System.out.println("Inside the R1 Task Exception block::: "); //throw new
		 * Exception("Please do it Mannualy"); //e.printStackTrace(); }
		 */
		return r1Array;
	}

	public static void main(String[] args) throws Exception {
		String result = new R1Task().result();
		result.toString().replaceAll(String.valueOf(Double.NaN), "0");
		System.out.println(result.contains("NaN"));
		result = result.replaceAll("NaN", "null");
		System.out.println("result::: " + result);
		JSONObject jsonObject = (JSONObject) new JSONParser().parse(result);
		JSONArray arr = (JSONArray) jsonObject.get("results");
		List<Map<String, Object>> r1Array = (List<Map<String, Object>>) arr;
		System.out.println("Array Size::: " + r1Array.size());
	}

	private String result() {
		return "{\r\n" + "    \"results\": [\r\n" + "        {\r\n" + "            \"BW_mbps\": 4,\r\n"
				+ "            \"BW_mbps_2\": 4,\r\n" + "            \"BW_mbps_act\": 4,\r\n"
				+ "            \"Burstable_BW\": 2,\r\n" + "            \"Customer_Segment\": \"Carriers\",\r\n"
				+ "            \"FATG_Building_Type\": \"Commercial\",\r\n"
				+ "            \"FATG_Category\": \"Wimax Site\",\r\n"
				+ "            \"FATG_DIST_KM\": 95857.35146717311,\r\n"
				+ "            \"FATG_Network_Location_Type\": \"Mega POP\",\r\n"
				+ "            \"FATG_PROW\": \"xxx\",\r\n" + "            \"FATG_Ring_type\": \"SDH\",\r\n"
				+ "            \"FATG_TCL_Access\": \"Yes\",\r\n"
				+ "            \"Feasibility.Response..Created.Date\": 17193,\r\n"
				+ "            \"HH_0_5_access_rings_F\": \"NA\",\r\n"
				+ "            \"HH_0_5_access_rings_NF\": \"NA\",\r\n" + "            \"HH_0_5km\": \"NA\",\r\n"
				+ "            \"HH_DIST_KM\": NaN,\r\n" + "            \"Latitude_final\": 20.009962,\r\n"
				+ "            \"Longitude_final\": 80.21421020000002,\r\n"
				+ "            \"Network_F_NF_CC\": \"NA\",\r\n" + "            \"Network_F_NF_CC_Flag\": \"NA\",\r\n"
				+ "            \"Network_F_NF_HH\": \"NA\",\r\n" + "            \"Network_F_NF_HH_Flag\": \"NA\",\r\n"
				+ "            \"Network_Feasibility_Check\": \"Feasible\",\r\n"
				+ "            \"OnnetCity_tag\": 1,\r\n" + "            \"Orch_BW\": \"ge_2_10_MB\",\r\n"
				+ "            \"Orch_Category\": \"Capex greater than 175m\",\r\n"
				+ "            \"Orch_Connection\": \"Wireline\",\r\n" + "            \"Orch_LM_Type\": \"Onnet\",\r\n"
				+ "            \"POP_Building_Type\": \"Commercial\",\r\n"
				+ "            \"POP_Category\": \"Wimax Site\",\r\n"
				+ "            \"POP_Construction_Status\": \"In Service\",\r\n"
				+ "            \"POP_DIST_KM\": 95.8573514671731,\r\n" + "            \"POP_DIST_KM_SERVICE\": 0,\r\n"
				+ "            \"POP_DIST_KM_SERVICE_MOD\": NaN,\r\n"
				+ "            \"POP_Network_Location_Type\": \"Mega POP\",\r\n"
				+ "            \"POP_TCL_Access\": \"Yes\",\r\n"
				+ "            \"Predicted_Access_Feasibility\": \"Not Feasible\",\r\n"
				+ "            \"Probabililty_Access_Feasibility\": 0.09333333333333334,\r\n"
				+ "            \"Product.Name\": \"NPL\",\r\n" + "            \"Service_ID\": 0,\r\n"
				+ "            \"Site_id\": \"6\",\r\n" + "            \"X0.5km_avg_bw\": 9999999,\r\n"
				+ "            \"X0.5km_avg_dist\": 9999999,\r\n" + "            \"X0.5km_cust_count\": 0,\r\n"
				+ "            \"X0.5km_max_bw\": 9999999,\r\n" + "            \"X0.5km_min_bw\": 9999999,\r\n"
				+ "            \"X0.5km_min_dist\": 9999999,\r\n"
				+ "            \"X0.5km_prospect_avg_bw\": 9999999,\r\n"
				+ "            \"X0.5km_prospect_avg_dist\": 9999999,\r\n"
				+ "            \"X0.5km_prospect_count\": 0,\r\n"
				+ "            \"X0.5km_prospect_max_bw\": 9999999,\r\n"
				+ "            \"X0.5km_prospect_min_bw\": 9999999,\r\n"
				+ "            \"X0.5km_prospect_min_dist\": 9999999,\r\n"
				+ "            \"X0.5km_prospect_num_feasible\": 0,\r\n"
				+ "            \"X0.5km_prospect_perc_feasible\": 0,\r\n" + "            \"X2km_avg_bw\": 9999999,\r\n"
				+ "            \"X2km_avg_dist\": 9999999,\r\n" + "            \"X2km_cust_count\": 0,\r\n"
				+ "            \"X2km_max_bw\": 9999999,\r\n" + "            \"X2km_min_bw\": 9999999,\r\n"
				+ "            \"X2km_min_dist\": 9999999,\r\n" + "            \"X2km_prospect_avg_bw\": 9999999,\r\n"
				+ "            \"X2km_prospect_avg_dist\": 9999999,\r\n" + "            \"X2km_prospect_count\": 0,\r\n"
				+ "            \"X2km_prospect_max_bw\": 9999999,\r\n"
				+ "            \"X2km_prospect_min_bw\": 9999999,\r\n"
				+ "            \"X2km_prospect_min_dist\": 9999999,\r\n"
				+ "            \"X2km_prospect_num_feasible\": 0,\r\n"
				+ "            \"X2km_prospect_perc_feasible\": 0,\r\n" + "            \"X5km_avg_bw\": 9999999,\r\n"
				+ "            \"X5km_avg_dist\": 9999999,\r\n" + "            \"X5km_cust_count\": 0,\r\n"
				+ "            \"X5km_max_bw\": 9999999,\r\n" + "            \"X5km_min_bw\": 9999999,\r\n"
				+ "            \"X5km_min_dist\": 9999999,\r\n" + "            \"X5km_prospect_avg_bw\": 9999999,\r\n"
				+ "            \"X5km_prospect_avg_dist\": 9999999,\r\n" + "            \"X5km_prospect_count\": 0,\r\n"
				+ "            \"X5km_prospect_max_bw\": 9999999,\r\n"
				+ "            \"X5km_prospect_min_bw\": 9999999,\r\n"
				+ "            \"X5km_prospect_min_dist\": 9999999,\r\n"
				+ "            \"X5km_prospect_num_feasible\": 0,\r\n"
				+ "            \"X5km_prospect_perc_feasible\": 0,\r\n" + "            \"access_check_CC\": \"NA\",\r\n"
				+ "            \"access_check_hh\": \"NA\",\r\n" + "            \"bw_arc_cost\": 30630,\r\n"
				+ "            \"bw_otc_cost\": 0,\r\n" + "            \"city_tier\": \"Tier1\",\r\n"
				+ "            \"connected_building_tag\": 0,\r\n" + "            \"connected_cust_tag\": 0,\r\n"
				+ "            \"core_check_CC\": \"NA\",\r\n" + "            \"core_check_hh\": \"NA\",\r\n"
				+ "            \"cost_permeter\": 800,\r\n" + "            \"hh_flag\": \"NA\",\r\n"
				+ "            \"in_building_capex_cost\": 40000,\r\n"
				+ "            \"local_loop_interface\": \"FE\",\r\n"
				+ "            \"min_hh_fatg\": 95857.35146717311,\r\n" + "            \"mux_cost\": 58810,\r\n"
				+ "            \"ne_rental_cost\": 0,\r\n" + "            \"net_pre_feasible_flag\": 1,\r\n"
				+ "            \"num_connected_building\": 0,\r\n" + "            \"num_connected_cust\": 0,\r\n"
				+ "            \"osp_capex_cost\": 76685881.1737385,\r\n"
				+ "            \"prospect_name\": \"Aircel Limited\",\r\n"
				+ "            \"resp_city\": \"CHENNAI\",\r\n" + "            \"resp_state\": \"maharashtra\",\r\n"
				+ "            \"scenario_1\": 1,\r\n" + "            \"scenario_2\": 0,\r\n"
				+ "            \"seq_no\": 2,\r\n" + "            \"sno\": 1,\r\n"
				+ "            \"total_cost\": 76815321.1737385\r\n" + "        }\r\n" + "    ]\r\n" + "}";
	}

}
