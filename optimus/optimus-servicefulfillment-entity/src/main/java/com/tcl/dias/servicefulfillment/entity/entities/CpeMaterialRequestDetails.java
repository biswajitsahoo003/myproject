package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author NKalipan
 *
 */
@Entity
@Table(name = "cpe_material_request_details")
@NamedQuery(name = "CpeMaterialRequestDetails.findAll", query = "SELECT s FROM CpeMaterialRequestDetails s")
public class CpeMaterialRequestDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "sc_service_detail_id")
	private Integer scServiceDetailId;
	
	@Column(name = "service_code")
	private String serviceCode;
	
	@Column(name = "material_code")
	private String materialCode;
	
	@Column(name = "plant")
	private String plant;
	
	@Column(name = "storage_location")
	private String storageLocation;
	
	@Column(name = "valuation_type")
	private String valuationtType;
	
	@Column(name = "purchase_group")
	private String purchaseGroup;
	
	@Column(name = "category_of_inventory")
	private String categoryOfInventory;
	
	@Column(name = "quantity")
	private String quantity;
	
	@Column(name = "available")
	private String available;
	
	@Column(name = "catagory")
	private String catagory;
	
	@Column(name = "pr_number")
	private String prNumber;
	
	@Column(name = "po_number")
	private String poNumber;
	
	@Column(name = "cpe_purchase_type")
	private String cpePurchaseType;
	
	@Column(name = "to_wbs_number")
	private String toWbsNumber;
	
	@Column(name = "from_wbs_number")
	private String formWbsNumber;
	
	@Column(name = "wbs_transfer_date")
	private Timestamp wbsTransferDate;
	
	
	@Column(name = "wbs_transfer_status")
	private String wbsTransferStatus;
	
	@Column(name = "unit_of_measure")
	private String unitOfMeasure;
	
	@Column(name = "sap_serial_number")
	private String sapSerialNumber;
	
	@Column(name = "oem_serial_number")
	private String oemSerialNumber;
	

	
	
	@Column(name = "mrn_number")
	private String mrnNumber;
	
	
	@Column(name = "min_number")
	private String minNumber;
	
	
	@Column(name = "created_date")
	private String createdDate;
	
	
	@Column(name = "bundled_bom")
	private String bundledBom;
	
	

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getBundledBom() {
		return bundledBom;
	}

	public void setBundledBom(String bundledBom) {
		this.bundledBom = bundledBom;
	}

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
