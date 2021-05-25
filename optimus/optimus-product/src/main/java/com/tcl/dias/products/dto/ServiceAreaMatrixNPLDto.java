package com.tcl.dias.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.common.utils.Constants;

import javax.validation.constraints.NotNull;

/**
 * This file contains the ServiceAreaMatrixNPLDto.java class.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceAreaMatrixNPLDto {

	@NotNull(message = Constants.ID_NULL)
	private int id;

	@NotNull(message = Constants.POP_LOCATION_NULL)
	private String popLocationId;

	@NotNull(message = Constants.TOWNS_DETAIL_NULL)
	private String townsDtl;

	private String regionDtl;

	private String popAddressTxt;

	@NotNull
	private String longitudeNbr;

	@NotNull
	private String latitudeNbr;

	public ServiceAreaMatrixNPLDto() {

	}

	public ServiceAreaMatrixNPLDto(@NotNull(message = Constants.ID_NULL) int id,
			@NotNull(message = Constants.POP_LOCATION_NULL) String popLocationId,
			@NotNull(message = Constants.TOWNS_DETAIL_NULL) String townsDtl, String regionDtl, String popAddressTxt,
			@NotNull String longitudeNbr, @NotNull String latitudeNbr) {
		this.id = id;
		this.popLocationId = popLocationId;
		this.townsDtl = townsDtl;
		this.regionDtl = regionDtl;
		this.popAddressTxt = popAddressTxt;
		this.longitudeNbr = longitudeNbr;
		this.latitudeNbr = latitudeNbr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPopLocationId() {
		return popLocationId;
	}

	public void setPopLocationId(String popLocationId) {
		this.popLocationId = popLocationId;
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

	public String getPopAddressTxt() {
		return popAddressTxt;
	}

	public void setPopAddressTxt(String popAddressTxt) {
		this.popAddressTxt = popAddressTxt;
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
}
