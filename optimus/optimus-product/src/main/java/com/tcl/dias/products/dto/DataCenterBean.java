package com.tcl.dias.products.dto;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.tcl.dias.productcatelog.entity.entities.ViewIasDataCenterSam;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixDataCenter;
/**
 * This file contains the DataCenterBean.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DataCenterBean {
	@NotNull(message = Constants.ID_NULL)
	private Integer id;
	private String dcType; 

	private String townsDtl; 

	private String regionDtl;

	private String addressTxt; 

	private String longitudeNbr;

	private String latitudeNbr; 
	
	private String locationId; 
	
	private String pincode;
	
	private String locality;
	
	private String stateDtl;
	
	private List<String> floors;

	private Integer erfLocId;

	public DataCenterBean(ViewIasDataCenterSam viewIasDataCenterSam) {
		this.id = viewIasDataCenterSam.getId();
		this.dcType = viewIasDataCenterSam.getDcType();
		this.erfLocId=viewIasDataCenterSam.getErfLocId();
		this.townsDtl = viewIasDataCenterSam.getTownsDtl();
		this.regionDtl = viewIasDataCenterSam.getRegionDtl();
		this.addressTxt = viewIasDataCenterSam.getAddressTxt();
		this.longitudeNbr = viewIasDataCenterSam.getLongitudeNbr();
		this.latitudeNbr = viewIasDataCenterSam.getLatitudeNbr();
		this.locationId = viewIasDataCenterSam.getLocationId();
		this.pincode = viewIasDataCenterSam.getPincodeNbr();
		this.locality = viewIasDataCenterSam.getLocalityDtl();
		this.stateDtl = viewIasDataCenterSam.getStateDtl();
		this.floors = StringUtils.isNotEmpty(viewIasDataCenterSam.getFloorDtls()) ? Arrays.asList(viewIasDataCenterSam.getFloorDtls().split(","))
				: new ArrayList<String>();
	}

	public String getStateDtl() {
		return stateDtl;
	}

	public void setStateDtl(String stateDtl) {
		this.stateDtl = stateDtl;
	}

	public DataCenterBean() {
		
	}
	
	public DataCenterBean(ServiceAreaMatrixDataCenter dc) {
		this.id = dc.getId();
		this.dcType = dc.getDcType();
		this.townsDtl = dc.getTownsDtl();
		this.regionDtl = dc.getRegionDtl();
		this.addressTxt = dc.getAddressTxt();
		this.longitudeNbr = dc.getLongitudeNbr();
		this.latitudeNbr = dc.getLatitudeNbr();
		this.locationId = dc.getLocationId();
		this.pincode = dc.getPincodeNbr();
		this.locality = dc.getLocalityDtl();
		this.stateDtl = dc.getStateDtl();
		this.floors = StringUtils.isNotEmpty(dc.getFloorDtls()) ? Arrays.asList(dc.getFloorDtls().split(","))
				: new ArrayList<String>();

	}
	
	/**
	 * @param id
	 * @param dcType
	 * @param townsDtl
	 * @param regionDtl
	 * @param addressTxt
	 * @param longitudeNbr
	 * @param latitudeNbr
	 * @param locationId
	 */
	public DataCenterBean(@NotNull(message = "Id shouldn't be null") Integer id, String dcType, String townsDtl,
			String regionDtl, String addressTxt, String longitudeNbr, String latitudeNbr, String locationId) {
		super();
		this.id = id;
		this.dcType = dcType;
		this.townsDtl = townsDtl;
		this.regionDtl = regionDtl;
		this.addressTxt = addressTxt;
		this.longitudeNbr = longitudeNbr;
		this.latitudeNbr = latitudeNbr;
		this.locationId = locationId;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getDcType() {
		return dcType;
	}

	public void setDcType(String dcType) {
		this.dcType = dcType;
	}

	public String getTownsDtl() {
		return townsDtl;
	}

	public void setTownsDtl(String townsDtl) {
		this.townsDtl = townsDtl;
	}

	public String getRegionDtl() {
		return regionDtl;
	}

	public void setRegionDtl(String regionDtl) {
		this.regionDtl = regionDtl;
	}

	public String getAddressTxt() {
		return addressTxt;
	}

	public void setAddressTxt(String addressTxt) {
		this.addressTxt = addressTxt;
	}

	public String getLongitudeNbr() {
		return longitudeNbr;
	}

	public void setLongitudeNbr(String longitudeNbr) {
		this.longitudeNbr = longitudeNbr;
	}

	public String getLatitudeNbr() {
		return latitudeNbr;
	}

	public void setLatitudeNbr(String latitudeNbr) {
		this.latitudeNbr = latitudeNbr;
	}


	public String getPincode() {
		return pincode;
	}


	public void setPincode(String pincode) {
		this.pincode = pincode;
	}


	public String getLocality() {
		return locality;
	}


	public void setLocality(String locality) {
		this.locality = locality;
	}

	public List<String> getFloors() {
		return floors;
	}

	public void setFloors(List<String> floors) {
		this.floors = floors;
	}

	public Integer getErfLocId() {
		return erfLocId;
	}

	public void setErfLocId(Integer erfLocId) {
		this.erfLocId = erfLocId;
	}

}
