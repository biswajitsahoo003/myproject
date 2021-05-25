package com.tcl.dias.common.teamsdr.beans;

/**
 * Bean for carrying Service level charges
 *
 */
public class ServiceLevelChargesBean {

	private String hsnCode;
	private String componentVariant;
	private String componentSubVariant;
	private String chargeType;
	private Double price;
	private String currencyCode;
	private String chargeUom;

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getComponentVariant() {
		return componentVariant;
	}

	public void setComponentVariant(String componentVariant) {
		this.componentVariant = componentVariant;
	}

	public String getComponentSubVariant() {
		return componentSubVariant;
	}

	public void setComponentSubVariant(String componentSubVariant) {
		this.componentSubVariant = componentSubVariant;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getChargeUom() {
		return chargeUom;
	}

	public void setChargeUom(String chargeUom) {
		this.chargeUom = chargeUom;
	}
}
