package com.tcl.dias.servicehandover.beans.renewal;

import java.sql.Timestamp;
import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;

public class RenewalContractSiteDetails {

    private Integer serviceId;
    private String serviceCode;
    private String orderCode;
    private String contractGstNumber;
    private String orderLeStatGstAddress;
    private String contractGstFlatNumber;
    private String contractGstBuildingNumber;
    private String contractGstAddressOne;
    private String contractGstAddressTwo;
    private String contractGstAddressThree;
    private String contractGstAddressCity;
    private String contractGstAddressState;
    private String contractGstAddressPincode;
    private String contractGstAddressCountry;
    private String siteAGstAddressId;
    private String siteBGstAddressId;
    private String siteAGstAddressOne;
    private String siteBGstAddressOne;
    private String siteAGstAddressTwo;
    private String siteBGstAddressTwo;
    private String siteAGstAddressThree;
    private String siteBGstAddressThree;
    private String siteAGstFlatNumber;
    private String siteBGstFlatNumber;
    private String siteAGstBuildingName;
    private String siteBGstBuildingName;
    private String siteAGstBuildingNumber;
    private String siteBGstBuildingNumber;
    private String siteAGstStreet;
    private String siteBGstStreet;
    private String siteAGstLocality;
    private String siteBGstLocality;
    private String siteAGstPincode;
    private String siteBGstPincode;
    private String siteAGstDistrict;
    private String siteBGstDistrict;
    private String siteAGstState;
    private String siteBGstState;
    private String siteAGstCountry;
    private String siteBGstCountry;
    private String siteAAddressOne;
    private String siteBAddressOne;
    private String siteAAddressTwo;
    private String siteBAddressTwo;
    private String siteAPincode;
    private String siteBPincode;
    private String siteAAddressThree;
    private String siteBAddressThree;
    private String siteACity;
    private String siteBCity;
    private String siteAState;
    private String siteBState;
    private String siteACountry;
    private String siteBCountry;
    private String siteAGstNumber;
    private String siteBGstNumber;
    private String siteACustomerPoNumber;
    private String siteBCustomerPoNumber;
    private String siteACustomerPoDate;
    private String siteBCustomerPoDate;
    private String siteATaxExemption;
    private String siteBTaxExemption;
    private String siteATaxExemptionReason;
    private String siteBTaxExemptionReason;
    private String siteAPODocType;
    private String siteBPODocType;
    private String siteALocalLoopBandwidth;
    private String siteBLocalLoopBandwidth;
    private String siteALocalLoopBandwidthUnit;
    private String siteBLocalLoopBandwidthUnit;
    private String siteAPortBandwidth;
    private String siteBPortBandwidth;
    private String siteABwUnit;
    private String siteBBwUnit;
    private String siteASupplierAddress;
    private String siteBSupplierAddress;
    private String primarySecondary;
    private String siteACpeManagementType;
    private String siteBCpeManagementType;
    private String siteABurstableBandwidth;
    private String siteBBurstableBandwidth;
    private String siteABurstableBandwidthUnit;
    private String siteBBurstableBandwidthUnit;
    private String siteAInterface;
    private String siteBInterface;
    private String siteASiteCode;
    private String siteBSiteCode;
    private String siteAEventSource;
    private String siteBEventSource;
    private String siteASiteAddress;
    private String siteBSiteAddress;
    private String multiVrfSolution;
    private String siteACustomerRef;
    private String siteBCustomerRef;
    private String siteABillingAddress;
    private String siteBBillingAddress;
    private String siteAGstnAddress;
    private String siteBGstnAddress;
    private Timestamp contractStartDate;
    private Timestamp contractEndDate;
    private List<AttachmentBean> documents;
    
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getContractGstNumber() {
		return contractGstNumber;
	}
	public void setContractGstNumber(String contractGstNumber) {
		this.contractGstNumber = contractGstNumber;
	}
	public String getOrderLeStatGstAddress() {
		return orderLeStatGstAddress;
	}
	public void setOrderLeStatGstAddress(String orderLeStatGstAddress) {
		this.orderLeStatGstAddress = orderLeStatGstAddress;
	}
	public String getContractGstFlatNumber() {
		return contractGstFlatNumber;
	}
	public void setContractGstFlatNumber(String contractGstFlatNumber) {
		this.contractGstFlatNumber = contractGstFlatNumber;
	}
	public String getContractGstBuildingNumber() {
		return contractGstBuildingNumber;
	}
	public void setContractGstBuildingNumber(String contractGstBuildingNumber) {
		this.contractGstBuildingNumber = contractGstBuildingNumber;
	}
	public String getContractGstAddressOne() {
		return contractGstAddressOne;
	}
	public void setContractGstAddressOne(String contractGstAddressOne) {
		this.contractGstAddressOne = contractGstAddressOne;
	}
	public String getContractGstAddressTwo() {
		return contractGstAddressTwo;
	}
	public void setContractGstAddressTwo(String contractGstAddressTwo) {
		this.contractGstAddressTwo = contractGstAddressTwo;
	}
	public String getContractGstAddressThree() {
		return contractGstAddressThree;
	}
	public void setContractGstAddressThree(String contractGstAddressThree) {
		this.contractGstAddressThree = contractGstAddressThree;
	}
	public String getContractGstAddressCity() {
		return contractGstAddressCity;
	}
	public void setContractGstAddressCity(String contractGstAddressCity) {
		this.contractGstAddressCity = contractGstAddressCity;
	}
	public String getContractGstAddressState() {
		return contractGstAddressState;
	}
	public void setContractGstAddressState(String contractGstAddressState) {
		this.contractGstAddressState = contractGstAddressState;
	}
	public String getContractGstAddressPincode() {
		return contractGstAddressPincode;
	}
	public void setContractGstAddressPincode(String contractGstAddressPincode) {
		this.contractGstAddressPincode = contractGstAddressPincode;
	}
	public String getContractGstAddressCountry() {
		return contractGstAddressCountry;
	}
	public void setContractGstAddressCountry(String contractGstAddressCountry) {
		this.contractGstAddressCountry = contractGstAddressCountry;
	}
	public String getSiteAGstAddressId() {
		return siteAGstAddressId;
	}
	public void setSiteAGstAddressId(String siteAGstAddressId) {
		this.siteAGstAddressId = siteAGstAddressId;
	}
	public String getSiteBGstAddressId() {
		return siteBGstAddressId;
	}
	public void setSiteBGstAddressId(String siteBGstAddressId) {
		this.siteBGstAddressId = siteBGstAddressId;
	}
	public String getSiteAGstAddressOne() {
		return siteAGstAddressOne;
	}
	public void setSiteAGstAddressOne(String siteAGstAddressOne) {
		this.siteAGstAddressOne = siteAGstAddressOne;
	}
	public String getSiteBGstAddressOne() {
		return siteBGstAddressOne;
	}
	public void setSiteBGstAddressOne(String siteBGstAddressOne) {
		this.siteBGstAddressOne = siteBGstAddressOne;
	}
	public String getSiteAGstAddressTwo() {
		return siteAGstAddressTwo;
	}
	public void setSiteAGstAddressTwo(String siteAGstAddressTwo) {
		this.siteAGstAddressTwo = siteAGstAddressTwo;
	}
	public String getSiteBGstAddressTwo() {
		return siteBGstAddressTwo;
	}
	public void setSiteBGstAddressTwo(String siteBGstAddressTwo) {
		this.siteBGstAddressTwo = siteBGstAddressTwo;
	}
	public String getSiteAGstAddressThree() {
		return siteAGstAddressThree;
	}
	public void setSiteAGstAddressThree(String siteAGstAddressThree) {
		this.siteAGstAddressThree = siteAGstAddressThree;
	}
	public String getSiteBGstAddressThree() {
		return siteBGstAddressThree;
	}
	public void setSiteBGstAddressThree(String siteBGstAddressThree) {
		this.siteBGstAddressThree = siteBGstAddressThree;
	}
	public String getSiteAGstFlatNumber() {
		return siteAGstFlatNumber;
	}
	public void setSiteAGstFlatNumber(String siteAGstFlatNumber) {
		this.siteAGstFlatNumber = siteAGstFlatNumber;
	}
	public String getSiteBGstFlatNumber() {
		return siteBGstFlatNumber;
	}
	public void setSiteBGstFlatNumber(String siteBGstFlatNumber) {
		this.siteBGstFlatNumber = siteBGstFlatNumber;
	}
	public String getSiteAGstBuildingName() {
		return siteAGstBuildingName;
	}
	public void setSiteAGstBuildingName(String siteAGstBuildingName) {
		this.siteAGstBuildingName = siteAGstBuildingName;
	}
	public String getSiteBGstBuildingName() {
		return siteBGstBuildingName;
	}
	public void setSiteBGstBuildingName(String siteBGstBuildingName) {
		this.siteBGstBuildingName = siteBGstBuildingName;
	}
	public String getSiteAGstBuildingNumber() {
		return siteAGstBuildingNumber;
	}
	public void setSiteAGstBuildingNumber(String siteAGstBuildingNumber) {
		this.siteAGstBuildingNumber = siteAGstBuildingNumber;
	}
	public String getSiteBGstBuildingNumber() {
		return siteBGstBuildingNumber;
	}
	public void setSiteBGstBuildingNumber(String siteBGstBuildingNumber) {
		this.siteBGstBuildingNumber = siteBGstBuildingNumber;
	}
	public String getSiteAGstStreet() {
		return siteAGstStreet;
	}
	public void setSiteAGstStreet(String siteAGstStreet) {
		this.siteAGstStreet = siteAGstStreet;
	}
	public String getSiteBGstStreet() {
		return siteBGstStreet;
	}
	public void setSiteBGstStreet(String siteBGstStreet) {
		this.siteBGstStreet = siteBGstStreet;
	}
	public String getSiteAGstLocality() {
		return siteAGstLocality;
	}
	public void setSiteAGstLocality(String siteAGstLocality) {
		this.siteAGstLocality = siteAGstLocality;
	}
	public String getSiteBGstLocality() {
		return siteBGstLocality;
	}
	public void setSiteBGstLocality(String siteBGstLocality) {
		this.siteBGstLocality = siteBGstLocality;
	}
	public String getSiteAGstPincode() {
		return siteAGstPincode;
	}
	public void setSiteAGstPincode(String siteAGstPincode) {
		this.siteAGstPincode = siteAGstPincode;
	}
	public String getSiteBGstPincode() {
		return siteBGstPincode;
	}
	public void setSiteBGstPincode(String siteBGstPincode) {
		this.siteBGstPincode = siteBGstPincode;
	}
	public String getSiteAGstDistrict() {
		return siteAGstDistrict;
	}
	public void setSiteAGstDistrict(String siteAGstDistrict) {
		this.siteAGstDistrict = siteAGstDistrict;
	}
	public String getSiteBGstDistrict() {
		return siteBGstDistrict;
	}
	public void setSiteBGstDistrict(String siteBGstDistrict) {
		this.siteBGstDistrict = siteBGstDistrict;
	}
	public String getSiteAGstState() {
		return siteAGstState;
	}
	public void setSiteAGstState(String siteAGstState) {
		this.siteAGstState = siteAGstState;
	}
	public String getSiteBGstState() {
		return siteBGstState;
	}
	public void setSiteBGstState(String siteBGstState) {
		this.siteBGstState = siteBGstState;
	}
	public String getSiteAGstCountry() {
		return siteAGstCountry;
	}
	public void setSiteAGstCountry(String siteAGstCountry) {
		this.siteAGstCountry = siteAGstCountry;
	}
	public String getSiteBGstCountry() {
		return siteBGstCountry;
	}
	public void setSiteBGstCountry(String siteBGstCountry) {
		this.siteBGstCountry = siteBGstCountry;
	}
	public String getSiteAAddressOne() {
		return siteAAddressOne;
	}
	public void setSiteAAddressOne(String siteAAddressOne) {
		this.siteAAddressOne = siteAAddressOne;
	}
	public String getSiteBAddressOne() {
		return siteBAddressOne;
	}
	public void setSiteBAddressOne(String siteBAddressOne) {
		this.siteBAddressOne = siteBAddressOne;
	}
	public String getSiteAAddressTwo() {
		return siteAAddressTwo;
	}
	public void setSiteAAddressTwo(String siteAAddressTwo) {
		this.siteAAddressTwo = siteAAddressTwo;
	}
	public String getSiteBAddressTwo() {
		return siteBAddressTwo;
	}
	public void setSiteBAddressTwo(String siteBAddressTwo) {
		this.siteBAddressTwo = siteBAddressTwo;
	}
	public String getSiteAPincode() {
		return siteAPincode;
	}
	public void setSiteAPincode(String siteAPincode) {
		this.siteAPincode = siteAPincode;
	}
	public String getSiteBPincode() {
		return siteBPincode;
	}
	public void setSiteBPincode(String siteBPincode) {
		this.siteBPincode = siteBPincode;
	}
	public String getSiteAAddressThree() {
		return siteAAddressThree;
	}
	public void setSiteAAddressThree(String siteAAddressThree) {
		this.siteAAddressThree = siteAAddressThree;
	}
	public String getSiteBAddressThree() {
		return siteBAddressThree;
	}
	public void setSiteBAddressThree(String siteBAddressThree) {
		this.siteBAddressThree = siteBAddressThree;
	}
	public String getSiteACity() {
		return siteACity;
	}
	public void setSiteACity(String siteACity) {
		this.siteACity = siteACity;
	}
	public String getSiteBCity() {
		return siteBCity;
	}
	public void setSiteBCity(String siteBCity) {
		this.siteBCity = siteBCity;
	}
	public String getSiteAState() {
		return siteAState;
	}
	public void setSiteAState(String siteAState) {
		this.siteAState = siteAState;
	}
	public String getSiteBState() {
		return siteBState;
	}
	public void setSiteBState(String siteBState) {
		this.siteBState = siteBState;
	}
	public String getSiteACountry() {
		return siteACountry;
	}
	public void setSiteACountry(String siteACountry) {
		this.siteACountry = siteACountry;
	}
	public String getSiteBCountry() {
		return siteBCountry;
	}
	public void setSiteBCountry(String siteBCountry) {
		this.siteBCountry = siteBCountry;
	}
	public String getSiteAGstNumber() {
		return siteAGstNumber;
	}
	public void setSiteAGstNumber(String siteAGstNumber) {
		this.siteAGstNumber = siteAGstNumber;
	}
	public String getSiteBGstNumber() {
		return siteBGstNumber;
	}
	public void setSiteBGstNumber(String siteBGstNumber) {
		this.siteBGstNumber = siteBGstNumber;
	}
	public String getSiteACustomerPoNumber() {
		return siteACustomerPoNumber;
	}
	public void setSiteACustomerPoNumber(String siteACustomerPoNumber) {
		this.siteACustomerPoNumber = siteACustomerPoNumber;
	}
	public String getSiteBCustomerPoNumber() {
		return siteBCustomerPoNumber;
	}
	public void setSiteBCustomerPoNumber(String siteBCustomerPoNumber) {
		this.siteBCustomerPoNumber = siteBCustomerPoNumber;
	}
	public String getSiteACustomerPoDate() {
		return siteACustomerPoDate;
	}
	public void setSiteACustomerPoDate(String siteACustomerPoDate) {
		this.siteACustomerPoDate = siteACustomerPoDate;
	}
	public String getSiteBCustomerPoDate() {
		return siteBCustomerPoDate;
	}
	public void setSiteBCustomerPoDate(String siteBCustomerPoDate) {
		this.siteBCustomerPoDate = siteBCustomerPoDate;
	}
	public String getSiteATaxExemption() {
		return siteATaxExemption;
	}
	public void setSiteATaxExemption(String siteATaxExemption) {
		this.siteATaxExemption = siteATaxExemption;
	}
	public String getSiteBTaxExemption() {
		return siteBTaxExemption;
	}
	public void setSiteBTaxExemption(String siteBTaxExemption) {
		this.siteBTaxExemption = siteBTaxExemption;
	}
	public String getSiteATaxExemptionReason() {
		return siteATaxExemptionReason;
	}
	public void setSiteATaxExemptionReason(String siteATaxExemptionReason) {
		this.siteATaxExemptionReason = siteATaxExemptionReason;
	}
	public String getSiteBTaxExemptionReason() {
		return siteBTaxExemptionReason;
	}
	public void setSiteBTaxExemptionReason(String siteBTaxExemptionReason) {
		this.siteBTaxExemptionReason = siteBTaxExemptionReason;
	}
	public String getSiteAPODocType() {
		return siteAPODocType;
	}
	public void setSiteAPODocType(String siteAPODocType) {
		this.siteAPODocType = siteAPODocType;
	}
	public String getSiteBPODocType() {
		return siteBPODocType;
	}
	public void setSiteBPODocType(String siteBPODocType) {
		this.siteBPODocType = siteBPODocType;
	}
	public List<AttachmentBean> getDocuments() {
		return documents;
	}
	public void setDocuments(List<AttachmentBean> documents) {
		this.documents = documents;
	}
	public String getSiteALocalLoopBandwidth() {
		return siteALocalLoopBandwidth;
	}
	public void setSiteALocalLoopBandwidth(String siteALocalLoopBandwidth) {
		this.siteALocalLoopBandwidth = siteALocalLoopBandwidth;
	}
	public String getSiteBLocalLoopBandwidth() {
		return siteBLocalLoopBandwidth;
	}
	public void setSiteBLocalLoopBandwidth(String siteBLocalLoopBandwidth) {
		this.siteBLocalLoopBandwidth = siteBLocalLoopBandwidth;
	}
	public String getSiteALocalLoopBandwidthUnit() {
		return siteALocalLoopBandwidthUnit;
	}
	public void setSiteALocalLoopBandwidthUnit(String siteALocalLoopBandwidthUnit) {
		this.siteALocalLoopBandwidthUnit = siteALocalLoopBandwidthUnit;
	}
	public String getSiteBLocalLoopBandwidthUnit() {
		return siteBLocalLoopBandwidthUnit;
	}
	public void setSiteBLocalLoopBandwidthUnit(String siteBLocalLoopBandwidthUnit) {
		this.siteBLocalLoopBandwidthUnit = siteBLocalLoopBandwidthUnit;
	}
	public String getSiteAPortBandwidth() {
		return siteAPortBandwidth;
	}
	public void setSiteAPortBandwidth(String siteAPortBandwidth) {
		this.siteAPortBandwidth = siteAPortBandwidth;
	}
	public String getSiteBPortBandwidth() {
		return siteBPortBandwidth;
	}
	public void setSiteBPortBandwidth(String siteBPortBandwidth) {
		this.siteBPortBandwidth = siteBPortBandwidth;
	}
	public String getSiteABwUnit() {
		return siteABwUnit;
	}
	public void setSiteABwUnit(String siteABwUnit) {
		this.siteABwUnit = siteABwUnit;
	}
	public String getSiteBBwUnit() {
		return siteBBwUnit;
	}
	public void setSiteBBwUnit(String siteBBwUnit) {
		this.siteBBwUnit = siteBBwUnit;
	}
	public String getSiteASupplierAddress() {
		return siteASupplierAddress;
	}
	public void setSiteASupplierAddress(String siteASupplierAddress) {
		this.siteASupplierAddress = siteASupplierAddress;
	}
	public String getSiteBSupplierAddress() {
		return siteBSupplierAddress;
	}
	public void setSiteBSupplierAddress(String siteBSupplierAddress) {
		this.siteBSupplierAddress = siteBSupplierAddress;
	}
	public String getPrimarySecondary() {
		return primarySecondary;
	}
	public void setPrimarySecondary(String primarySecondary) {
		this.primarySecondary = primarySecondary;
	}
	public String getSiteACpeManagementType() {
		return siteACpeManagementType;
	}
	public void setSiteACpeManagementType(String siteACpeManagementType) {
		this.siteACpeManagementType = siteACpeManagementType;
	}
	public String getSiteBCpeManagementType() {
		return siteBCpeManagementType;
	}
	public void setSiteBCpeManagementType(String siteBCpeManagementType) {
		this.siteBCpeManagementType = siteBCpeManagementType;
	}
	public String getSiteABurstableBandwidth() {
		return siteABurstableBandwidth;
	}
	public void setSiteABurstableBandwidth(String siteABurstableBandwidth) {
		this.siteABurstableBandwidth = siteABurstableBandwidth;
	}
	public String getSiteBBurstableBandwidth() {
		return siteBBurstableBandwidth;
	}
	public void setSiteBBurstableBandwidth(String siteBBurstableBandwidth) {
		this.siteBBurstableBandwidth = siteBBurstableBandwidth;
	}
	public String getSiteABurstableBandwidthUnit() {
		return siteABurstableBandwidthUnit;
	}
	public void setSiteABurstableBandwidthUnit(String siteABurstableBandwidthUnit) {
		this.siteABurstableBandwidthUnit = siteABurstableBandwidthUnit;
	}
	public String getSiteBBurstableBandwidthUnit() {
		return siteBBurstableBandwidthUnit;
	}
	public void setSiteBBurstableBandwidthUnit(String siteBBurstableBandwidthUnit) {
		this.siteBBurstableBandwidthUnit = siteBBurstableBandwidthUnit;
	}
	public String getSiteAInterface() {
		return siteAInterface;
	}
	public void setSiteAInterface(String siteAInterface) {
		this.siteAInterface = siteAInterface;
	}
	public String getSiteBInterface() {
		return siteBInterface;
	}
	public void setSiteBInterface(String siteBInterface) {
		this.siteBInterface = siteBInterface;
	}
	public String getSiteASiteCode() {
		return siteASiteCode;
	}
	public void setSiteASiteCode(String siteASiteCode) {
		this.siteASiteCode = siteASiteCode;
	}
	public String getSiteBSiteCode() {
		return siteBSiteCode;
	}
	public void setSiteBSiteCode(String siteBSiteCode) {
		this.siteBSiteCode = siteBSiteCode;
	}
	public String getSiteAEventSource() {
		return siteAEventSource;
	}
	public void setSiteAEventSource(String siteAEventSource) {
		this.siteAEventSource = siteAEventSource;
	}
	public String getSiteBEventSource() {
		return siteBEventSource;
	}
	public void setSiteBEventSource(String siteBEventSource) {
		this.siteBEventSource = siteBEventSource;
	}
	public String getSiteASiteAddress() {
		return siteASiteAddress;
	}
	public void setSiteASiteAddress(String siteASiteAddress) {
		this.siteASiteAddress = siteASiteAddress;
	}
	public String getSiteBSiteAddress() {
		return siteBSiteAddress;
	}
	public void setSiteBSiteAddress(String siteBSiteAddress) {
		this.siteBSiteAddress = siteBSiteAddress;
	}
	public String getMultiVrfSolution() {
		return multiVrfSolution;
	}
	public void setMultiVrfSolution(String multiVrfSolution) {
		this.multiVrfSolution = multiVrfSolution;
	}
	public String getSiteACustomerRef() {
		return siteACustomerRef;
	}
	public void setSiteACustomerRef(String siteACustomerRef) {
		this.siteACustomerRef = siteACustomerRef;
	}
	public String getSiteBCustomerRef() {
		return siteBCustomerRef;
	}
	public void setSiteBCustomerRef(String siteBCustomerRef) {
		this.siteBCustomerRef = siteBCustomerRef;
	}
	public String getSiteABillingAddress() {
		return siteABillingAddress;
	}
	public void setSiteABillingAddress(String siteABillingAddress) {
		this.siteABillingAddress = siteABillingAddress;
	}
	public String getSiteBBillingAddress() {
		return siteBBillingAddress;
	}
	public void setSiteBBillingAddress(String siteBBillingAddress) {
		this.siteBBillingAddress = siteBBillingAddress;
	}
	public String getSiteAGstnAddress() {
		return siteAGstnAddress;
	}
	public void setSiteAGstnAddress(String siteAGstnAddress) {
		this.siteAGstnAddress = siteAGstnAddress;
	}
	public String getSiteBGstnAddress() {
		return siteBGstnAddress;
	}
	public void setSiteBGstnAddress(String siteBGstnAddress) {
		this.siteBGstnAddress = siteBGstnAddress;
	}
	public Timestamp getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public Timestamp getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	
}
