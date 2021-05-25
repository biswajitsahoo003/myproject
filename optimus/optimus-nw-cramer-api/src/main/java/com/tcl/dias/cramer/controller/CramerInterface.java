package com.tcl.dias.cramer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public interface CramerInterface {
 
	@GetMapping("/GetCountries")
	public ResponseEntity<?> getCountryList();
	
	@GetMapping("/GetStates")
	public ResponseEntity<?> getStateList(@RequestParam("countryname") String countryname);

	@GetMapping("/GetCities")
	public ResponseEntity<?> getCityList(@RequestParam("locationName") String locationName, @RequestParam("locationType") String locationType, @RequestParam("cityname") String cityname);

	@GetMapping("/GetAreas")
	public ResponseEntity<?> getAreaList(@RequestParam("cityName") String cityName);

	@GetMapping("/GetIsDeviceExists")
	public ResponseEntity<?> getIsDeviceExistsList(@RequestParam("deviceName") String deviceName);

	@GetMapping("/GetBuilding4Areas")
	public ResponseEntity<?> getBuilding4AreaList(@RequestParam("areaId") String areaId);

	@GetMapping("/CreateObject")
	public ResponseEntity<?> createObject(@RequestParam("orderId") String orderId, @RequestParam("orderType") String orderType, @RequestParam("technology") String technology);

	@GetMapping("/UpdateObject")
	public ResponseEntity<?> updateObject(@RequestParam("orderId") String orderId,
										  @RequestParam("orderType") String orderType,
										  @RequestParam("provisioningStatus") String provisioningStatus);

	@GetMapping("/GetSupportedCardTypes4Node")
	public ResponseEntity<?> getSupportCardTypes(@RequestParam(value = "nodeName", required = false) String nodeName);

}
