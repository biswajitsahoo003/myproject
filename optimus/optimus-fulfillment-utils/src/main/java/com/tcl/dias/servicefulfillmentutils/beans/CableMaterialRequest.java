/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;

/**
 * @author vivek
 *
 */
public class CableMaterialRequest {

	private Integer id;

	private Integer scServiceDetailId;

	private String serviceCode;

	private String materialCode;

	private String plant;

	private String storageLocation;

	private String valuationtType;

	private String purchaseGroup;

	private String categoryOfInventory;

	private String quantity;

	private String available;

	private String catagory;

	private String prNumber;

	private String poNumber;

	private String cpePurchaseType;

	private String toWbsNumber;

	private String formWbsNumber;

	private Timestamp wbsTransferDate;

	private String wbsTransferStatus;

	private String unitOfMeasure;

	private String sapSerialNumber;

	private String oemSerialNumber;

	private String mrnNumber;

	private String minNumber;

	/**
	 * @return the mrnNumber
	 */
	public String getMrnNumber() {
		return mrnNumber;
	}

	/**
	 * @param mrnNumber the mrnNumber to set
	 */
	public void setMrnNumber(String mrnNumber) {
		this.mrnNumber = mrnNumber;
	}

	/**
	 * @return the minNumber
	 */
	public String getMinNumber() {
		return minNumber;
	}

	/**
	 * @param minNumber the minNumber to set
	 */
	public void setMinNumber(String minNumber) {
		this.minNumber = minNumber;
	}

	/**
	 * @return the wbsTransferDate
	 */
	public Timestamp getWbsTransferDate() {
		return wbsTransferDate;
	}

	/**
	 * @param wbsTransferDate the wbsTransferDate to set
	 */
	public void setWbsTransferDate(Timestamp wbsTransferDate) {
		this.wbsTransferDate = wbsTransferDate;
	}

	/**
	 * @return the sapSerialNumber
	 */
	public String getSapSerialNumber() {
		return sapSerialNumber;
	}

	/**
	 * @param sapSerialNumber the sapSerialNumber to set
	 */
	public void setSapSerialNumber(String sapSerialNumber) {
		this.sapSerialNumber = sapSerialNumber;
	}

	/**
	 * @return the oemSerialNumber
	 */
	public String getOemSerialNumber() {
		return oemSerialNumber;
	}

	/**
	 * @param oemSerialNumber the oemSerialNumber to set
	 */
	public void setOemSerialNumber(String oemSerialNumber) {
		this.oemSerialNumber = oemSerialNumber;
	}

	/**
	 * @return the unitOfMeasure
	 */
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	/**
	 * @param unitOfMeasure the unitOfMeasure to set
	 */
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScServiceDetailId() {
		return scServiceDetailId;
	}

	public void setScServiceDetailId(Integer scServiceDetailId) {
		this.scServiceDetailId = scServiceDetailId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getValuationtType() {
		return valuationtType;
	}

	public void setValuationtType(String valuationtType) {
		this.valuationtType = valuationtType;
	}

	public String getPurchaseGroup() {
		return purchaseGroup;
	}

	public void setPurchaseGroup(String purchaseGroup) {
		this.purchaseGroup = purchaseGroup;
	}

	public String getCategoryOfInventory() {
		return categoryOfInventory;
	}

	public void setCategoryOfInventory(String categoryOfInventory) {
		this.categoryOfInventory = categoryOfInventory;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public String getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getCpePurchaseType() {
		return cpePurchaseType;
	}

	public void setCpePurchaseType(String cpePurchaseType) {
		this.cpePurchaseType = cpePurchaseType;
	}

	/**
	 * @return the toWbsNumber
	 */
	public String getToWbsNumber() {
		return toWbsNumber;
	}

	/**
	 * @param toWbsNumber the toWbsNumber to set
	 */
	public void setToWbsNumber(String toWbsNumber) {
		this.toWbsNumber = toWbsNumber;
	}

	/**
	 * @return the formWbsNumber
	 */
	public String getFormWbsNumber() {
		return formWbsNumber;
	}

	/**
	 * @param formWbsNumber the formWbsNumber to set
	 */
	public void setFormWbsNumber(String formWbsNumber) {
		this.formWbsNumber = formWbsNumber;
	}

	/**
	 * @return the wbsTransferStatus
	 */
	public String getWbsTransferStatus() {
		return wbsTransferStatus;
	}

	/**
	 * @param wbsTransferStatus the wbsTransferStatus to set
	 */
	public void setWbsTransferStatus(String wbsTransferStatus) {
		this.wbsTransferStatus = wbsTransferStatus;
	}

}
