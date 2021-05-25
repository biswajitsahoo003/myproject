package com.tcl.dias.cramer.controller;

import java.util.List;


import com.tcl.dias.cramer.model.*;
import com.tcl.dias.cramer.service.CramerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.tcl.dias.cramer.controller.CramerInterface;

@Component
public class CramerImpl implements CramerInterface{

	@Autowired
    CramerService service;
	
	@Override
	public ResponseEntity<?> getCountryList() {
		
		
		List<Country> countrylist=service.getCountryList();
		
		if(countrylist.size()>0) {
			
			FinalResponse<Country> response=new FinalResponse<>("Success", "Fetched Country list Successfully",countrylist);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		FinalResponse<Country> response=new FinalResponse<>("Success", "Failed To Fetch Country list");
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@Override
	public ResponseEntity<?> getStateList(String countryname) {
		
		List<State> statelist=service.getStateList(countryname);
		if(statelist.size()>0) {
			
			FinalResponse<State> response=new FinalResponse<>("Success", "Fetched State list Successfully",statelist);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		FinalResponse<State> response=new FinalResponse<>("Success", "Failed To Fetch State list");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
	@Override
	public ResponseEntity<?> getCityList(String locationName, String locationType, String cityname) {
		
		List<City> citylist=service.getCityList(locationName,locationType,cityname);
		if(citylist.size()>0) {
			
			FinalResponse<City> response=new FinalResponse<>("Success", "Fetched City list Successfully",citylist);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		FinalResponse<City> response=new FinalResponse<>("Success", "Failed To Fetch City list");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
	@Override
	public ResponseEntity<?> getAreaList(String cityName) {
		
		List<Area> arealist=service.getAreaList(cityName);
		if(arealist.size()>0) {
			
			FinalResponse<Area> response=new FinalResponse<>("Success", "Fetched Area list Successfully",arealist);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		FinalResponse<Area> response=new FinalResponse<>("Success", "Failed To Fetch Area list");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
	@Override
	public ResponseEntity<?> getIsDeviceExistsList(String deviceName) {
		
		List<IsDeviceExist> isdevicelist=service.getisdeviceexistsList(deviceName);
		if(isdevicelist.size()>0) {
			
			FinalResponse<IsDeviceExist> response=new FinalResponse<>("Success", "Fetched IsDeviceExists list Successfully",isdevicelist);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		FinalResponse<IsDeviceExist> response=new FinalResponse<>("Success", "Failed To Fetch IsDeviceExists list");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
	@Override
	public ResponseEntity<?> getBuilding4AreaList(String areaId) {
		
		List<Building4area> building4arealist=service.getbuilding4areaList(areaId);
		if(building4arealist.size()>0) {
			
			FinalResponse<Building4area> response=new FinalResponse<>("Success", "Fetched Building4area list Successfully",building4arealist);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		FinalResponse<Building4area> response=new FinalResponse<>("Success", "Failed To Fetch Building4area list");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@Override
	public ResponseEntity<?> createObject(String orderId, String orderType, String technology) {

		Boolean result = service.createObject(orderId,orderType,technology);

		if(result != null) {
			FinalResponse<Boolean> response=new FinalResponse<Boolean>("Success", "Object Created Successfully");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		FinalResponse<Boolean> response=new FinalResponse<Boolean>("Success", "Failed To Create Object");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@Override
	public ResponseEntity<?> updateObject(String orderId, String orderType, String provisioningStatus) {

		Boolean result = service.updateObject(orderId,orderType,provisioningStatus);

		if(result != null) {
			FinalResponse<Boolean> response=new FinalResponse<Boolean>("Success", "Object Updated Successfully");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		FinalResponse<Boolean> response=new FinalResponse<Boolean>("Success", "Failed To Update Object");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@Override
	public ResponseEntity<?> getSupportCardTypes(String nodeName) {
		SupportedCardTypeResult result = service.getSupportCardTypes(nodeName);
		if(result != null) {
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}

		FinalResponse<Boolean> response=new FinalResponse<Boolean>("Failed", "Cannot get supported card types.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
