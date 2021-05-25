package com.tcl.dias.common.beans;


import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
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
	
	public DataCenterBean() {
		
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

	
}
