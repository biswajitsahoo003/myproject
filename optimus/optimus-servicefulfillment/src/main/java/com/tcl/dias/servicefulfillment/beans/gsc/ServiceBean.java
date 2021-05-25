package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;
import java.util.Map;

public class ServiceBean {

	private Integer id;
	private String serviceId;
	private String originCountry;
	private String destCountry;
	private String originCity;
	private String qty;
	private String portingQty;
	private String status;
	private String remarks;
	private String accesstype;
	private String callTypeFixed;
	private String callTypeMobile;
	private String callTypePayphone;
	private String customerLegalEntity;
	private String supplierContractingEntity;
	private String customerLEAddress;
	private String contractServices;
	private Integer cuId;
	private Integer secsId;
	private Integer cmsId;
	private List<OutpulsePortingNumBean> outpulsePortingNumbers;
	private List<Map<String,String>> bridgeDetails;
	
	public String getAccesstype() {
		return accesstype;
	}

	public void setAccesstype(String accesstype) {
		this.accesstype = accesstype;
	}

	public String getCallTypeFixed() {
		return callTypeFixed;
	}

	public void setCallTypeFixed(String callTypeFixed) {
		this.callTypeFixed = callTypeFixed;
	}

	public String getCallTypeMobile() {
		return callTypeMobile;
	}

	public void setCallTypeMobile(String callTypeMobile) {
		this.callTypeMobile = callTypeMobile;
	}

	public String getCallTypePayphone() {
		return callTypePayphone;
	}

	public void setCallTypePayphone(String callTypePayphone) {
		this.callTypePayphone = callTypePayphone;
	}

	public String getCustomerLegalEntity() {
		return customerLegalEntity;
	}

	public void setCustomerLegalEntity(String customerLegalEntity) {
		this.customerLegalEntity = customerLegalEntity;
	}

	public String getSupplierContractingEntity() {
		return supplierContractingEntity;
	}

	public void setSupplierContractingEntity(String supplierContractingEntity) {
		this.supplierContractingEntity = supplierContractingEntity;
	}

	public String getCustomerLEAddress() {
		return customerLEAddress;
	}

	public void setCustomerLEAddress(String customerLEAddress) {
		this.customerLEAddress = customerLEAddress;
	}

	public String getContractServices() {
		return contractServices;
	}

	public void setContractServices(String contractServices) {
		this.contractServices = contractServices;
	}

	public Integer getCuId() {
		return cuId;
	}

	public void setCuId(Integer cuId) {
		this.cuId = cuId;
	}

	public Integer getSecsId() {
		return secsId;
	}

	public void setSecsId(Integer secsId) {
		this.secsId = secsId;
	}

	public Integer getCmsId() {
		return cmsId;
	}

	public void setCmsId(Integer cmsId) {
		this.cmsId = cmsId;
	}

	private List<DocumentBean> documents;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getPortingQty() {
		return portingQty;
	}

	public void setPortingQty(String portingQty) {
		this.portingQty = portingQty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DocumentBean> getDocumentsList() {
		return documents;
	}

	public void setDocumentsList(List<DocumentBean> documentsList) {
		this.documents = documentsList;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<OutpulsePortingNumBean> getOutpulsePortingNumbers() {
		return outpulsePortingNumbers;
	}

	public void setOutpulsePortingNumbers(List<OutpulsePortingNumBean> outpulsePortingNumbers) {
		this.outpulsePortingNumbers = outpulsePortingNumbers;
	}
	
	@Override
	public String toString() {
		return "ServiceBean [id=" + id + ", serviceId=" + serviceId + ", originCountry=" + originCountry
				+ ", destCountry=" + destCountry + ", originCity=" + originCity + ", qty=" + qty + ", portingQty="
				+ portingQty + ", status=" + status + ", remarks=" + remarks + ", accesstype=" + accesstype
				+ ", callTypeFixed=" + callTypeFixed + ", callTypeMobile=" + callTypeMobile + ", callTypePayphone="
				+ callTypePayphone + ", customerLegalEntity=" + customerLegalEntity + ", supplierContractingEntity="
				+ supplierContractingEntity + ", customerLEAddress=" + customerLEAddress + ", contractServices="
				+ contractServices + ", cuId=" + cuId + ", secsId=" + secsId + ", cmsId=" + cmsId + ", documents="
				+ documents + "]";
	}

	public List<Map<String, String>> getBridgeDetails() {
		return bridgeDetails;
	}

	public void setBridgeDetails(List<Map<String, String>> bridgeDetails) {
		this.bridgeDetails = bridgeDetails;
	}
	
	

}
