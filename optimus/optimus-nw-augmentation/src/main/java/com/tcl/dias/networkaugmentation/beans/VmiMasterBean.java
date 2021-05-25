package com.tcl.dias.networkaugmentation.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the VmiMasterBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class VmiMasterBean {

	private String description;
	private Integer grandTotal;
	private String productCode;
	private Integer qtyLeft;
	private Map<String, String> quantityMapper = new HashMap<>();
	private String rentalMaterialCode;
	private String saleMaterialCode;
	private Integer sumOfQty;
	private String soNumber;
	private String poLandingDate;
	private String poNumber;
	private String shippedDateCisco;
	private String receivedDateWarehouse;
	private String vmiStatusDate;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Integer grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getQtyLeft() {
		return qtyLeft;
	}

	public void setQtyLeft(Integer qtyLeft) {
		this.qtyLeft = qtyLeft;
	}

	public Map<String, String> getQuantityMapper() {
		return quantityMapper;
	}

	public void setQuantityMapper(Map<String, String> quantityMapper) {
		this.quantityMapper = quantityMapper;
	}

	public String getRentalMaterialCode() {
		return rentalMaterialCode;
	}

	public void setRentalMaterialCode(String rentalMaterialCode) {
		this.rentalMaterialCode = rentalMaterialCode;
	}

	public String getSaleMaterialCode() {
		return saleMaterialCode;
	}

	public void setSaleMaterialCode(String saleMaterialCode) {
		this.saleMaterialCode = saleMaterialCode;
	}

	public Integer getSumOfQty() {
		return sumOfQty;
	}

	public void setSumOfQty(Integer sumOfQty) {
		this.sumOfQty = sumOfQty;
	}

	public String getSoNumber() {
		return soNumber;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
	}

	public String getPoLandingDate() {
		return poLandingDate;
	}

	public void setPoLandingDate(String poLandingDate) {
		this.poLandingDate = poLandingDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getShippedDateCisco() {
		return shippedDateCisco;
	}

	public void setShippedDateCisco(String shippedDateCisco) {
		this.shippedDateCisco = shippedDateCisco;
	}

	public String getReceivedDateWarehouse() {
		return receivedDateWarehouse;
	}

	public void setReceivedDateWarehouse(String receivedDateWarehouse) {
		this.receivedDateWarehouse = receivedDateWarehouse;
	}

	public String getVmiStatusDate() {
		return vmiStatusDate;
	}

	public void setVmiStatusDate(String vmiStatusDate) {
		this.vmiStatusDate = vmiStatusDate;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "VmiMasterBean [description=" + description + ", grandTotal=" + grandTotal + ", productCode="
				+ productCode + ", qtyLeft=" + qtyLeft + ", quantityMapper=" + quantityMapper + ", rentalMaterialCode="
				+ rentalMaterialCode + ", saleMaterialCode=" + saleMaterialCode + ", sumOfQty=" + sumOfQty
				+ ", soNumber=" + soNumber + ", poLandingDate=" + poLandingDate + ", poNumber=" + poNumber
				+ ", shippedDateCisco=" + shippedDateCisco + ", receivedDateWarehouse=" + receivedDateWarehouse
				+ ", vmiStatusDate=" + vmiStatusDate + "]";
	}

}
