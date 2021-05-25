package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * AclPolicyCriteria Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class AclPolicyCriteriaBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer aclPolicyId;
	private String action;
	private String description;
	private Boolean destinationAny;
	private String destinationCondition;
	private String destinationPortnumber;
	private String destinationSubnet;
	private Timestamp endDate;
	private String inboundIpv4AclName;
	private String inboundIpv6AclName;
	private boolean isEdited;
	private Boolean isinboundaclIpv4Applied;
	private Boolean isinboundaclIpv4Preprovisioned;
	private Boolean isinboundaclIpv6Applied;
	private Boolean isinboundaclIpv6Preprovisioned;
	private Boolean isoutboundaclIpv4Applied;
	private Boolean isoutboundaclIpv4Preprovisioned;
	private Boolean isoutboundaclIpv6Applied;
	private Boolean isoutboundaclIpv6Preprovisioned;
	private Boolean issvcQosCoscriteriaIpaddrAcl;
	private String issvcQosCoscriteriaIpaddrAclName;
	private Boolean issvcQosCoscriteriaIpaddrPreprovisioned;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String outboundIpv4AclName;
	private String outboundIpv6AclName;
	private String protocol;
	private String sequence;
	private Boolean sourceAny;
	private String sourceCondition;
	private String sourcePortnumber;
	private String sourceSubnet;
	private Timestamp startDate;


	public Integer getAclPolicyId() {
		return aclPolicyId;
	}

	public void setAclPolicyId(Integer aclPolicyId) {
		this.aclPolicyId = aclPolicyId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDestinationAny() {
		return destinationAny;
	}

	public void setDestinationAny(Boolean destinationAny) {
		this.destinationAny = destinationAny;
	}

	public String getDestinationCondition() {
		return destinationCondition;
	}

	public void setDestinationCondition(String destinationCondition) {
		this.destinationCondition = destinationCondition;
	}

	public String getDestinationPortnumber() {
		return destinationPortnumber;
	}

	public void setDestinationPortnumber(String destinationPortnumber) {
		this.destinationPortnumber = destinationPortnumber;
	}

	public String getDestinationSubnet() {
		return destinationSubnet;
	}

	public void setDestinationSubnet(String destinationSubnet) {
		this.destinationSubnet = destinationSubnet;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getInboundIpv4AclName() {
		return inboundIpv4AclName;
	}

	public void setInboundIpv4AclName(String inboundIpv4AclName) {
		this.inboundIpv4AclName = inboundIpv4AclName;
	}

	public String getInboundIpv6AclName() {
		return inboundIpv6AclName;
	}

	public void setInboundIpv6AclName(String inboundIpv6AclName) {
		this.inboundIpv6AclName = inboundIpv6AclName;
	}

	public Boolean getIsinboundaclIpv4Applied() {
		return isinboundaclIpv4Applied;
	}

	public void setIsinboundaclIpv4Applied(Boolean isinboundaclIpv4Applied) {
		this.isinboundaclIpv4Applied = isinboundaclIpv4Applied;
	}

	public Boolean getIsinboundaclIpv4Preprovisioned() {
		return isinboundaclIpv4Preprovisioned;
	}

	public void setIsinboundaclIpv4Preprovisioned(Boolean isinboundaclIpv4Preprovisioned) {
		this.isinboundaclIpv4Preprovisioned = isinboundaclIpv4Preprovisioned;
	}

	public Boolean getIsinboundaclIpv6Applied() {
		return isinboundaclIpv6Applied;
	}

	public void setIsinboundaclIpv6Applied(Boolean isinboundaclIpv6Applied) {
		this.isinboundaclIpv6Applied = isinboundaclIpv6Applied;
	}

	public Boolean getIsinboundaclIpv6Preprovisioned() {
		return isinboundaclIpv6Preprovisioned;
	}

	public void setIsinboundaclIpv6Preprovisioned(Boolean isinboundaclIpv6Preprovisioned) {
		this.isinboundaclIpv6Preprovisioned = isinboundaclIpv6Preprovisioned;
	}

	public Boolean getIsoutboundaclIpv4Applied() {
		return isoutboundaclIpv4Applied;
	}

	public void setIsoutboundaclIpv4Applied(Boolean isoutboundaclIpv4Applied) {
		this.isoutboundaclIpv4Applied = isoutboundaclIpv4Applied;
	}

	public Boolean getIsoutboundaclIpv4Preprovisioned() {
		return isoutboundaclIpv4Preprovisioned;
	}

	public void setIsoutboundaclIpv4Preprovisioned(Boolean isoutboundaclIpv4Preprovisioned) {
		this.isoutboundaclIpv4Preprovisioned = isoutboundaclIpv4Preprovisioned;
	}

	public Boolean getIsoutboundaclIpv6Applied() {
		return isoutboundaclIpv6Applied;
	}

	public void setIsoutboundaclIpv6Applied(Boolean isoutboundaclIpv6Applied) {
		this.isoutboundaclIpv6Applied = isoutboundaclIpv6Applied;
	}

	public Boolean getIsoutboundaclIpv6Preprovisioned() {
		return isoutboundaclIpv6Preprovisioned;
	}

	public void setIsoutboundaclIpv6Preprovisioned(Boolean isoutboundaclIpv6Preprovisioned) {
		this.isoutboundaclIpv6Preprovisioned = isoutboundaclIpv6Preprovisioned;
	}

	public Boolean getIssvcQosCoscriteriaIpaddrAcl() {
		return issvcQosCoscriteriaIpaddrAcl;
	}

	public void setIssvcQosCoscriteriaIpaddrAcl(Boolean issvcQosCoscriteriaIpaddrAcl) {
		this.issvcQosCoscriteriaIpaddrAcl = issvcQosCoscriteriaIpaddrAcl;
	}

	public String getIssvcQosCoscriteriaIpaddrAclName() {
		return issvcQosCoscriteriaIpaddrAclName;
	}

	public void setIssvcQosCoscriteriaIpaddrAclName(String issvcQosCoscriteriaIpaddrAclName) {
		this.issvcQosCoscriteriaIpaddrAclName = issvcQosCoscriteriaIpaddrAclName;
	}

	public Boolean getIssvcQosCoscriteriaIpaddrPreprovisioned() {
		return issvcQosCoscriteriaIpaddrPreprovisioned;
	}

	public void setIssvcQosCoscriteriaIpaddrPreprovisioned(Boolean issvcQosCoscriteriaIpaddrPreprovisioned) {
		this.issvcQosCoscriteriaIpaddrPreprovisioned = issvcQosCoscriteriaIpaddrPreprovisioned;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOutboundIpv4AclName() {
		return outboundIpv4AclName;
	}

	public void setOutboundIpv4AclName(String outboundIpv4AclName) {
		this.outboundIpv4AclName = outboundIpv4AclName;
	}

	public String getOutboundIpv6AclName() {
		return outboundIpv6AclName;
	}

	public void setOutboundIpv6AclName(String outboundIpv6AclName) {
		this.outboundIpv6AclName = outboundIpv6AclName;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public Boolean getSourceAny() {
		return sourceAny;
	}

	public void setSourceAny(Boolean sourceAny) {
		this.sourceAny = sourceAny;
	}

	public String getSourceCondition() {
		return sourceCondition;
	}

	public void setSourceCondition(String sourceCondition) {
		this.sourceCondition = sourceCondition;
	}

	public String getSourcePortnumber() {
		return sourcePortnumber;
	}

	public void setSourcePortnumber(String sourcePortnumber) {
		this.sourcePortnumber = sourcePortnumber;
	}

	public String getSourceSubnet() {
		return sourceSubnet;
	}

	public void setSourceSubnet(String sourceSubnet) {
		this.sourceSubnet = sourceSubnet;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}