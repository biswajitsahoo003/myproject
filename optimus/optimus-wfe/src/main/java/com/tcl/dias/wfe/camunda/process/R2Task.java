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
public class R2Task implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(R2Task.class);
		
	@Value("${onnet.wireless.request}")
	String requestUrl;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		try {
			List<Map<String, Object>> r2Sites = (List<Map<String, Object>>) execution.getVariable("r2site");

			JSONArray arr = new JSONArray();
			arr.addAll(r2Sites);
			JSONObject topObj = new JSONObject();
			topObj.put("input_data", arr);
			List<Map<String, Object>> r2Output = postURL(topObj.toString());

			execution.setVariable("r2Output", r2Output);

			for (Map<String, Object> map1 : r2Sites) {
				for (Map<String, Object> map2 : r2Output) {
					if (map1.get("site_id").equals(map2.get("site_id"))) {
						map1.putAll(map2);
						// break;
					}
				}
			}
			LOGGER.info("Inside the R2 Task :::::: " + r2Sites.size());
		} catch (Exception e) {
			LOGGER.info("Inside the R2 Task::: Exception:::: ");
			throw new Exception();
		}
	}

	private List<Map<String, Object>> postURL(String request) throws Exception {
		//String query = "http://10.133.208.121/onnet-wireless/api";
		String json = request;
		List<Map<String, Object>> r2Array = null;
		// try {
		LOGGER.info("Inside the R2 post URL");
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
		if (result.contains("NaN"))
			result = result.replaceAll("NaN", "null");
		JSONObject jsonObject = (JSONObject) new JSONParser().parse(result);
		JSONArray arr = (JSONArray) jsonObject.get("results");
		r2Array = (List<Map<String, Object>>) arr;
		
		for(Map map : r2Array) {
			Double errorMessage =0.0;	
			if(map.get("error_flag") instanceof Double) {
				 errorMessage = (Double)map.get("error_flag");
				LOGGER.info("Double instance:::: ");
			} else if(map.get("error_flag") instanceof Integer) {
				errorMessage = new Double((Integer)map.get("error_flag"));
				LOGGER.info("Integer instance:::: ");
			} 
			
			if(errorMessage.equals(1.0)) {  // If any error from R model response then will send the error message to response queue
				LOGGER.info("R2 Model Exception flag 1::::");
				throw new Exception();
			}
		}

		in.close();
		conn.disconnect();
		
		return r2Array;
	}
}
