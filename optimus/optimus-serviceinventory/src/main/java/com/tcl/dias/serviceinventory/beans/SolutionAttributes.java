package com.tcl.dias.serviceinventory.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * This file contains the SolutionAttributes.java class.
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SolutionAttributes {

    private String offeringName;
    private String siteAddress;
    private String siteAlias;
    private String slaCommittment;
    private String accessType;
    private String accessProvider;
    private String cpeProvider;
    private String location;
    private String latLong;
    private String usageModel;
    private String serviceStatus;
    private String commissioningDate;
    private String lastMileProvider;
    private String billingAccountId;
    private String billingAddress;
    private String billingGstNumber;
    private Integer siServiceDetailId;
    private String leId;
    private String leName;
    private String supplierLeId;
    private String supplierLeName;
    private String primaryServiceId;
    private String secondaryServiceId;
    private Integer siOrderId;
    private String taxExemptionFlag;
    private Timestamp contractStartDate;
    private Timestamp contractEndDate;
    private Double termInMonths;
    private String ipAddressProvidedBy;
    private String serviceId;
    private Integer productId;
    private String customerId;
    private String vpnName;
    private String serviceTopology;
    private String siteLocationId;
    private String localItName;
    private String localItEmail;
    private String localItPhoneno;
    private String portSpeed;
    private String portSpeedUnit;
    private String showCosMessage;
    private String demoType;
    private String demoFlag;
    private Timestamp circuitExpiryDate;
    private String associateBillableId;
    private String demarcFloor;
    private String demarcRoom;
    private String demarcRack;
    private String demarcApartment;

    /**
     * @return the offeringName
     */
    public String getOfferingName() {
        return offeringName;
    }

    /**
     * @param offeringName the offeringName to set
     */
    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
    }

    /**
     * @return the components
     *//*
	public List<ComponentBean> getComponents() {
		return components;
	}

	*//**
     * @param components the components to set
     *//*
	public void setComponents(List<ComponentBean> components) {
		this.components = components;
	}
*/
    /**
     * @return the siteAddress
     */
    public String getSiteAddress() {
        return siteAddress;
    }

    /**
     * @param siteAddress the siteAddress to set
     */
    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    /**
     * @return the siteAlias
     */
    public String getSiteAlias() {
        return siteAlias;
    }

    /**
     * @param siteAlias the siteAlias to set
     */
    public void setSiteAlias(String siteAlias) {
        this.siteAlias = siteAlias;
    }

    /**
     * @return the slaCommittment
     */
    public String getSlaCommittment() {
        return slaCommittment;
    }

    /**
     * @param slaCommittment the slaCommittment to set
     */
    public void setSlaCommittment(String slaCommittment) {
        this.slaCommittment = slaCommittment;
    }

    /**
     * @return the accessType
     */
    public String getAccessType() {
        return accessType;
    }

    /**
     * @param accessType the accessType to set
     */
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    /**
     * @return the accessProvider
     */
    public String getAccessProvider() {
        return accessProvider;
    }

    /**
     * @param accessProvider the accessProvider to set
     */
    public void setAccessProvider(String accessProvider) {
        this.accessProvider = accessProvider;
    }


    /**
     * @return the cpeProvider
     */
    public String getCpeProvider() {
        return cpeProvider;
    }

    /**
     * @param cpeProvider the cpeProvider to set
     */
    public void setCpeProvider(String cpeProvider) {
        this.cpeProvider = cpeProvider;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the latLong
     */
    public String getLatLong() {
        return latLong;
    }

    /**
     * @param latLong the latLong to set
     */
    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    /**
     * @return the usageModel
     */
    public String getUsageModel() {
        return usageModel;
    }

    /**
     * @param usageModel the usageModel to set
     */
    public void setUsageModel(String usageModel) {
        this.usageModel = usageModel;
    }

    /**
     * @return the serviceStatus
     */
    public String getServiceStatus() {
        return serviceStatus;
    }

    /**
     * @param serviceStatus the serviceStatus to set
     */
    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    /**
     * @return the commissioningDate
     */
    public String getCommissioningDate() {
        return commissioningDate;
    }

    /**
     * @param commissioningDate the commissioningDate to set
     */
    public void setCommissioningDate(String commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

    /**
     * @return the lastMileProvider
     */
    public String getLastMileProvider() {
        return lastMileProvider;
    }

    /**
     * @param lastMileProvider the lastMileProvider to set
     */
    public void setLastMileProvider(String lastMileProvider) {
        this.lastMileProvider = lastMileProvider;
    }


    public String getBillingAccountId() {
        return billingAccountId;
    }

    public void setBillingAccountId(String billingAccountId) {
        this.billingAccountId = billingAccountId;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingGstNumber() {
        return billingGstNumber;
    }

    public void setBillingGstNumber(String billingGstNumber) {
        this.billingGstNumber = billingGstNumber;
    }

    public Integer getSiServiceDetailId() {
        return siServiceDetailId;
    }

    public void setSiServiceDetailId(Integer siServiceDetailId) {
        this.siServiceDetailId = siServiceDetailId;
    }

    public String getLeId() {
        return leId;
    }

    public void setLeId(String leId) {
        this.leId = leId;
    }

    public String getLeName() {
        return leName;
    }

    public void setLeName(String leName) {
        this.leName = leName;
    }

    public String getSupplierLeId() {
        return supplierLeId;
    }

    public void setSupplierLeId(String supplierLeId) {
        this.supplierLeId = supplierLeId;
    }

    public String getSupplierLeName() {
        return supplierLeName;
    }

    public void setSupplierLeName(String supplierLeName) {
        this.supplierLeName = supplierLeName;
    }

    public String getPrimaryServiceId() {
        return primaryServiceId;
    }

    public void setPrimaryServiceId(String primaryServiceId) {
        this.primaryServiceId = primaryServiceId;
    }

    public String getSecondaryServiceId() {
        return secondaryServiceId;
    }

    public void setSecondaryServiceId(String secondaryServiceId) {
        this.secondaryServiceId = secondaryServiceId;
    }

    public Integer getSiOrderId() {
        return siOrderId;
    }

    public void setSiOrderId(Integer siOrderId) {
        this.siOrderId = siOrderId;
    }

    public String getTaxExemptionFlag() {
        return taxExemptionFlag;
    }

    public void setTaxExemptionFlag(String taxExemptionFlag) {
        this.taxExemptionFlag = taxExemptionFlag;
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

    public Double getTermInMonths() {
        return termInMonths;
    }

    public void setTermInMonths(Double termInMonths) {
        this.termInMonths = termInMonths;
    }

    public String getIpAddressProvidedBy() {
        return ipAddressProvidedBy;
    }

    public void setIpAddressProvidedBy(String ipAddressProvidedBy) {
        this.ipAddressProvidedBy = ipAddressProvidedBy;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getServiceTopology() {
        return serviceTopology;
    }

    public void setServiceTopology(String serviceTopology) {
        this.serviceTopology = serviceTopology;
    }


    public String getSiteLocationId() {
        return siteLocationId;
    }

    public void setSiteLocationId(String siteLocationId) {
        this.siteLocationId = siteLocationId;
    }

    public String getLocalItName() {
        return localItName;
    }

    public void setLocalItName(String localItName) {
        this.localItName = localItName;
    }

    public String getLocalItEmail() {
        return localItEmail;
    }

    public void setLocalItEmail(String localItEmail) {
        this.localItEmail = localItEmail;
    }

    public String getLocalItPhoneno() {
        return localItPhoneno;
    }

    public void setLocalItPhoneno(String localItPhoneno) {
        this.localItPhoneno = localItPhoneno;
    }

    public String getPortSpeed() {
        return portSpeed;
    }

    public void setPortSpeed(String portSpeed) {
        this.portSpeed = portSpeed;
    }

    public String getPortSpeedUnit() {
        return portSpeedUnit;
    }

    public void setPortSpeedUnit(String portSpeedUnit) {
        this.portSpeedUnit = portSpeedUnit;
    }

    public String getShowCosMessage() {
		return showCosMessage;
	}

	public void setShowCosMessage(String showCosMessage) {
		this.showCosMessage = showCosMessage;
	}

    public String getDemoType() {
        return demoType;
    }

    public void setDemoType(String demoType) {
        this.demoType = demoType;
    }

    public String getDemoFlag() {
        return demoFlag;
    }

    public void setDemoFlag(String demoFlag) {
        this.demoFlag = demoFlag;
    }

	public Timestamp getCircuitExpiryDate() {
		return circuitExpiryDate;
	}

	public void setCircuitExpiryDate(Timestamp circuitExpiryDate) {
		this.circuitExpiryDate = circuitExpiryDate;
	}

	public String getAssociateBillableId() {
		return associateBillableId;
	}

	public void setAssociateBillableId(String associateBillableId) {
		this.associateBillableId = associateBillableId;
	}


    public String getDemarcFloor() {
        return demarcFloor;
    }

    public void setDemarcFloor(String demarcFloor) {
        this.demarcFloor = demarcFloor;
    }

    public String getDemarcRoom() {
        return demarcRoom;
    }

    public void setDemarcRoom(String demarcRoom) {
        this.demarcRoom = demarcRoom;
    }

    public String getDemarcRack() {
        return demarcRack;
    }

    public void setDemarcRack(String demarcRack) {
        this.demarcRack = demarcRack;
    }

    public String getDemarcApartment() {
        return demarcApartment;
    }

    public void setDemarcApartment(String demarcApartment) {
        this.demarcApartment = demarcApartment;
    }

    @Override
    public String toString() {
        return "SolutionAttributes{" +
                "offeringName='" + offeringName + '\'' +
                ", siteAddress='" + siteAddress + '\'' +
                ", siteAlias='" + siteAlias + '\'' +
                ", slaCommittment='" + slaCommittment + '\'' +
                ", accessType='" + accessType + '\'' +
                ", accessProvider='" + accessProvider + '\'' +
                ", cpeProvider='" + cpeProvider + '\'' +
                ", location='" + location + '\'' +
                ", latLong='" + latLong + '\'' +
                ", usageModel='" + usageModel + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", commissioningDate='" + commissioningDate + '\'' +
                ", lastMileProvider='" + lastMileProvider + '\'' +
                ", billingAccountId='" + billingAccountId + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", billingGstNumber='" + billingGstNumber + '\'' +
                ", siServiceDetailId=" + siServiceDetailId +
                ", leId='" + leId + '\'' +
                ", leName='" + leName + '\'' +
                ", supplierLeId='" + supplierLeId + '\'' +
                ", supplierLeName='" + supplierLeName + '\'' +
                ", primaryServiceId='" + primaryServiceId + '\'' +
                ", secondaryServiceId='" + secondaryServiceId + '\'' +
                ", siOrderId=" + siOrderId +
                ", taxExemptionFlag='" + taxExemptionFlag + '\'' +
                ", contractStartDate=" + contractStartDate +
                ", contractEndDate=" + contractEndDate +
                ", termInMonths=" + termInMonths +
                ", ipAddressProvidedBy='" + ipAddressProvidedBy + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", productId=" + productId +
                ", customerId='" + customerId + '\'' +
                ", vpnName='" + vpnName + '\'' +
                ", serviceTopology='" + serviceTopology + '\'' +
                ", siteLocationId='" + siteLocationId + '\'' +
                ", localItName='" + localItName + '\'' +
                ", localItEmail='" + localItEmail + '\'' +
                ", localItPhoneno='" + localItPhoneno + '\'' +
                ", portSpeed='" + portSpeed + '\'' +
                ", portSpeedUnit='" + portSpeedUnit + '\'' +
                ", showCosMessage='" + showCosMessage + '\'' +
                ", demoType='" + demoType + '\'' +
                ", demoFlag='" + demoFlag + '\'' +
                ", circuitExpiryDate=" + circuitExpiryDate +
                ", associateBillableId='" + associateBillableId + '\'' +
                ", demarcFloor='" + demarcFloor + '\'' +
                ", demarcRoom='" + demarcRoom + '\'' +
                ", demarcRack='" + demarcRack + '\'' +
                ", demarcApartment='" + demarcApartment + '\'' +
                '}';
    }
}
