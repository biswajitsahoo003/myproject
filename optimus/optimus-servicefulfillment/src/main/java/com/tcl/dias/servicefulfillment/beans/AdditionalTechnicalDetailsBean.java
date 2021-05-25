package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.beans.CosDetail;

/**
 * Bean class for AdditionalTechnicalDetails
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalTechnicalDetailsBean extends TaskDetailsBaseBean {

    private String extendedLanRequired;
    private String routingProtocol;
    private String isAuthenticationRequiredForProtocol;
    private String asNumber;
    private String bfdRequired;
    private String bgpPeeringOn;
    private String routesExchanged;
    private String resiliency;
    private String connectorType;
    private String accessRequired;
    private String cpe;
    private String wanIpProvidedBy;
    private String wanIpProvidedByCust;
    private String dns;
    private String bgpAsNumber;
    private String customerPrefixes;
    private String sharedLastMile;
    private String sharedLastMileServiceId;
    private String additionalIps;
    private String asNumberFormat;
    private String authenticationMode;
    private String authenticationProtocol;
    private String bfdMultiplier;
    private String asPassword;
    private String bfdReceiveInterval;
    private String bfdTransmitInterval;
    
    private String techpersonEmail;
    private String techpersonName;
    private String techpersonNumber;
    private String multiLinkLoadBalancing;
    private String ebgpLoadSharing;
    private String billingInvoiceMethod;

	private String programName;

	private String wanIpAddress;
	private String lanIpAddress;
	private String lanIpProvidedBy;

    private String demarcationWing;
    private String demarcationFloor;
    private String demarcationRoom;
    private String demarcationBuildingName;
    private List<CosDetail> cosDetails;
    
    private String siteType;
    private String vpnTopology;
    private String primarySecondary;
    private String prisecLink;
    private String productOfferingName;
    private String changedVRFName;
    private String lanPoolRoutingNeeded;

    public String getWanIpAddress() {
		return wanIpAddress;
	}

	public void setWanIpAddress(String wanIpAddress) {
		this.wanIpAddress = wanIpAddress;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	private List<AttachmentIdBean> documentIds;
    
    public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getExtendedLanRequired() {
        return extendedLanRequired;
    }

    public void setExtendedLanRequired(String extendedLanRequired) {
        this.extendedLanRequired = extendedLanRequired;
    }

    public String getRoutingProtocol() {
        return routingProtocol;
    }

    public void setRoutingProtocol(String routingProtocol) {
        this.routingProtocol = routingProtocol;
    }

    public String getIsAuthenticationRequiredForProtocol() {
        return isAuthenticationRequiredForProtocol;
    }

    public void setIsAuthenticationRequiredForProtocol(String isAuthenticationRequiredForProtocol) {
        this.isAuthenticationRequiredForProtocol = isAuthenticationRequiredForProtocol;
    }

    public String getAsNumber() {
        return asNumber;
    }

    public void setAsNumber(String asNumber) {
        this.asNumber = asNumber;
    }

    public String getBfdRequired() {
        return bfdRequired;
    }

    public void setBfdRequired(String bfdRequired) {
        this.bfdRequired = bfdRequired;
    }

    public String getBgpPeeringOn() {
        return bgpPeeringOn;
    }

    public void setBgpPeeringOn(String bgpPeeringOn) {
        this.bgpPeeringOn = bgpPeeringOn;
    }

    public String getRoutesExchanged() {
        return routesExchanged;
    }

    public void setRoutesExchanged(String routesExchanged) {
        this.routesExchanged = routesExchanged;
    }

    public String getResiliency() {
        return resiliency;
    }

    public void setResiliency(String resiliency) {
        this.resiliency = resiliency;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public String getAccessRequired() {
        return accessRequired;
    }

    public void setAccessRequired(String accessRequired) {
        this.accessRequired = accessRequired;
    }

    public String getCpe() {
        return cpe;
    }

    public void setCpe(String cpe) {
        this.cpe = cpe;
    }

    public String getWanIpProvidedBy() {
        return wanIpProvidedBy;
    }

    public void setWanIpProvidedBy(String wanIpProvidedBy) {
        this.wanIpProvidedBy = wanIpProvidedBy;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getBgpAsNumber() {
        return bgpAsNumber;
    }

    public void setBgpAsNumber(String bgpAsNumber) {
        this.bgpAsNumber = bgpAsNumber;
    }

    public String getCustomerPrefixes() {
        return customerPrefixes;
    }

    public void setCustomerPrefixes(String customerPrefixes) {
        this.customerPrefixes = customerPrefixes;
    }

    public String getSharedLastMile() {
        return sharedLastMile;
    }

    public void setSharedLastMile(String sharedLastMile) {
        this.sharedLastMile = sharedLastMile;
    }

    public String getSharedLastMileServiceId() {
        return sharedLastMileServiceId;
    }

    public void setSharedLastMileServiceId(String sharedLastMileServiceId) {
        this.sharedLastMileServiceId = sharedLastMileServiceId;
    }

    public String getAdditionalIps() {
        return additionalIps;
    }

    public void setAdditionalIps(String additionalIps) {
        this.additionalIps = additionalIps;
    }

    public String getAsNumberFormat() {
        return asNumberFormat;
    }

    public void setAsNumberFormat(String asNumberFormat) {
        this.asNumberFormat = asNumberFormat;
    }

    public String getAuthenticationMode() {
        return authenticationMode;
    }

    public void setAuthenticationMode(String authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public String getAuthenticationProtocol() {
        return authenticationProtocol;
    }

    public void setAuthenticationProtocol(String authenticationProtocol) {
        this.authenticationProtocol = authenticationProtocol;
    }

    public String getBfdMultiplier() {
        return bfdMultiplier;
    }

    public void setBfdMultiplier(String bfdMultiplier) {
        this.bfdMultiplier = bfdMultiplier;
    }

    public String getAsPassword() {
        return asPassword;
    }

    public void setAsPassword(String asPassword) {
        this.asPassword = asPassword;
    }

    public String getBfdReceiveInterval() {
        return bfdReceiveInterval;
    }

    public void setBfdReceiveInterval(String bfdReceiveInterval) {
        this.bfdReceiveInterval = bfdReceiveInterval;
    }

    public String getBfdTransmitInterval() {
        return bfdTransmitInterval;
    }

    public void setBfdTransmitInterval(String bfdTransmitInterval) {
        this.bfdTransmitInterval = bfdTransmitInterval;
    }

    public String getWanIpProvidedByCust() {
		return wanIpProvidedByCust;
	}

	public void setWanIpProvidedByCust(String wanIpProvidedByCust) {
		this.wanIpProvidedByCust = wanIpProvidedByCust;
	}

	@Override
    public String toString() {
        return "AdditionalTechnicalDetailsBean{" +
                "extendedLanRequired='" + extendedLanRequired + '\'' +
                ", routingProtocol='" + routingProtocol + '\'' +
                ", isAuthenticationRequiredForProtocol='" + isAuthenticationRequiredForProtocol + '\'' +
                ", asNumber='" + asNumber + '\'' +
                ", bfdRequired='" + bfdRequired + '\'' +
                ", bgpPeeringOn='" + bgpPeeringOn + '\'' +
                ", routesExchanged='" + routesExchanged + '\'' +
                ", resiliency='" + resiliency + '\'' +
                ", connectorType='" + connectorType + '\'' +
                ", accessRequired='" + accessRequired + '\'' +
                ", cpe='" + cpe + '\'' +
                ", wanIpProvidedBy='" + wanIpProvidedBy + '\'' +
                ", dns='" + dns + '\'' +
                ", bgpAsNumber='" + bgpAsNumber + '\'' +
                ", customerPrefixes='" + customerPrefixes + '\'' +
                ", sharedLastMile='" + sharedLastMile + '\'' +
                ", sharedLastMileServiceId='" + sharedLastMileServiceId + '\'' +
                ", additionalIps='" + additionalIps + '\'' +
                ", asNumberFormat='" + asNumberFormat + '\'' +
                ", authenticationMode='" + authenticationMode + '\'' +
                ", authenticationProtocol='" + authenticationProtocol + '\'' +
                ", bfdMultiplier='" + bfdMultiplier + '\'' +
                ", asPassword='" + asPassword + '\'' +
                ", bfdReceiveInterval='" + bfdReceiveInterval + '\'' +
                ", bfdTransmitInterval='" + bfdTransmitInterval + '\'' +
                ", siteType='" + siteType + '\'' +
                ", lanIpAddress='" + lanIpAddress + '\'' +
                ", lanIpProvidedBy='" + lanIpProvidedBy + '\'' +
                ", changedVRFName='" + changedVRFName + '\'' +
                ", wanIpAddress='" + wanIpAddress + '\'' +
                '}';
    }

	 
	public String getTechpersonEmail() {
		return techpersonEmail;
	}

	public void setTechpersonEmail(String techpersonEmail) {
		this.techpersonEmail = techpersonEmail;
	}

	public String getTechpersonName() {
		return techpersonName;
	}

	public void setTechpersonName(String techpersonName) {
		this.techpersonName = techpersonName;
	}

	public String getTechpersonNumber() {
		return techpersonNumber;
	}

	public void setTechpersonNumber(String techpersonNumber) {
		this.techpersonNumber = techpersonNumber;
	}

	public String getMultiLinkLoadBalancing() {
		return multiLinkLoadBalancing;
	}

	public void setMultiLinkLoadBalancing(String multiLinkLoadBalancing) {
		this.multiLinkLoadBalancing = multiLinkLoadBalancing;
	}

	public String getEbgpLoadSharing() {
		return ebgpLoadSharing;
	}

	public void setEbgpLoadSharing(String ebgpLoadSharing) {
		this.ebgpLoadSharing = ebgpLoadSharing;
	}

	public String getBillingInvoiceMethod() {
		return billingInvoiceMethod;
	}

	public void setBillingInvoiceMethod(String billingInvoiceMethod) {
		this.billingInvoiceMethod = billingInvoiceMethod;
	}

    public String getDemarcationWing() { return demarcationWing; }

    public void setDemarcationWing(String demarcationWing) { this.demarcationWing = demarcationWing; }

    public String getDemarcationFloor() { return demarcationFloor; }

    public void setDemarcationFloor(String demarcationFloor) { this.demarcationFloor = demarcationFloor; }

    public String getDemarcationRoom() { return demarcationRoom; }

    public void setDemarcationRoom(String demarcationRoom) { this.demarcationRoom = demarcationRoom; }

    public String getDemarcationBuildingName() { return demarcationBuildingName; }

    public void setDemarcationBuildingName(String demarcationBuildingName) { this.demarcationBuildingName = demarcationBuildingName; }
    
    public String getLanIpAddress() {
		return lanIpAddress;
	}

	public void setLanIpAddress(String lanIpAddress) {
		this.lanIpAddress = lanIpAddress;
	}

	public String getLanIpProvidedBy() {
		return lanIpProvidedBy;
	}

	public void setLanIpProvidedBy(String lanIpProvidedBy) {
		this.lanIpProvidedBy = lanIpProvidedBy;
	}

    public List<CosDetail> getCosDetails() {
        return cosDetails;
    }

    public void setCosDetails(List<CosDetail> cosDetails) {
        this.cosDetails = cosDetails;
    }

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getVpnTopology() {
		return vpnTopology;
	}

	public void setVpnTopology(String vpnTopology) {
		this.vpnTopology = vpnTopology;
	}

	public String getPrimarySecondary() {
		return primarySecondary;
	}

	public void setPrimarySecondary(String primarySecondary) {
		this.primarySecondary = primarySecondary;
	}

	public String getPrisecLink() {
		return prisecLink;
	}

	public void setPrisecLink(String prisecLink) {
		this.prisecLink = prisecLink;
	}

	public String getProductOfferingName() {
		return productOfferingName;
	}

	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}

	public String getChangedVRFName() {
		return changedVRFName;
	}

	public void setChangedVRFName(String changedVRFName) {
		this.changedVRFName = changedVRFName;
	}

	public String getLanPoolRoutingNeeded() {
		return lanPoolRoutingNeeded;
	}

	public void setLanPoolRoutingNeeded(String lanPoolRoutingNeeded) {
		this.lanPoolRoutingNeeded = lanPoolRoutingNeeded;
	}
	
}
