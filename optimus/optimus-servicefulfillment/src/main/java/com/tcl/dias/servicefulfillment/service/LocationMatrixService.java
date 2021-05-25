package com.tcl.dias.servicefulfillment.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;

@Component
public class LocationMatrixService {

	@Autowired
	RestClientService restClientService;

	@Value("${api.googlemap.key}")
	private String googleMapApiKey;

	@Value("${api.googlemap}")
	private String googleMapAPI;

	public Double findDistance(String sourceLatitude, String sourceLongitude, String desLat, String desLon) {
		Double latitudeRad = Double.parseDouble(sourceLatitude) * 0.0174533;
		Double longitudeRad = Double.parseDouble(sourceLongitude) * 0.0174533;

		Double latRad = Double.parseDouble(desLat) * 0.0174533;
		Double lonRad = Double.parseDouble(desLon) * 0.0174533;

		return Math.acos(Math.sin(latitudeRad) * Math.sin(latRad)
				+ Math.cos(latitudeRad) * Math.cos(latRad) * Math.cos(lonRad - (longitudeRad))) * 6371;

	}

	public String  getLatLong(String address) throws ParseException {
		String googleurl = StringUtils.join(googleMapAPI, "?key=",googleMapApiKey,address.replaceAll(" ", ""));

		RestResponse response = restClientService.get(googleurl);
		String geocodeReponse = null;

		if (response.getStatus() == Status.SUCCESS) {
			geocodeReponse = response.getData();
		}

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(geocodeReponse);
		JSONObject jb = (JSONObject) obj;

		JSONArray jsonResultsObject = (JSONArray) jb.get("results");
		if (!jsonResultsObject.isEmpty()) {
			JSONObject jsonObject2 = (JSONObject) jsonResultsObject.get(0);
			JSONObject jsonObject3 = (JSONObject) jsonObject2.get("geometry");
			JSONObject location = (JSONObject) jsonObject3.get("location");

			return location.get("lat") + "," + location.get("lng");

		}
		
		return null;
	}

}
