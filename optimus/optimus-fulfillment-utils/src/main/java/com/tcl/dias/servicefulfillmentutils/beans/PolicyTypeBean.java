package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * PolicyType Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class PolicyTypeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer policyId;
	private Boolean aludefaultoriginatev4Key;
	private Boolean aludefaultoriginatev6Key;
	private Timestamp endDate;
	private String inboundIpv4PolicyName;
	private Boolean inboundIpv4Preprovisioned;
	private String inboundIpv6PolicyName;
	private Boolean inboundIpv6Preprovisioned;
	private Boolean inboundIstandardpolicyv4;
	private Boolean inboundIstandardpolicyv6;
	private Boolean inboundRoutingIpv4Policy;
	private Boolean inboundRoutingIpv6Policy;
	private Boolean isadditionalpolicytermReqd;
	private Boolean islpinvpnpolicyconfigured;
	private Boolean isvprnExportPreprovisioned;
	private Boolean isvprnExportpolicy;
	private String isvprnExportpolicyName;
	private Boolean isvprnImportPreprovisioned;
	private Boolean isvprnImportpolicy;
	private String isvprnImportpolicyName;
	private Timestamp lastModifiedDate;
	private String localpreferencev4Vpnpolicy;
	private String localpreferencev6Vpnpolicy;
	private String modifiedBy;
	private Boolean originatedefaultFlag;
	private String outboundIpv4PolicyName;
	private Boolean outboundIpv4Preprovisioned;
	private String outboundIpv6PolicyName;
	private Boolean outboundIpv6Preprovisioned;
	private Boolean outboundIstandardpolicyv4;
	private Boolean outboundIstandardpolicyv6;
	private Boolean outboundRoutingIpv4Policy;
	private Boolean outboundRoutingIpv6Policy;
	private Timestamp startDate;
	private boolean isEdited;
	
	private Set<PolicyCriteriaBean> policyCriteria;
	
	


	public Set<PolicyCriteriaBean> getPolicyCriteria() {
		
		if(policyCriteria==null) {
			policyCriteria=new HashSet<>();
		}
		return policyCriteria;
	}

	public void setPolicyCriteria(Set<PolicyCriteriaBean> policyCriteria) {
		this.policyCriteria = policyCriteria;
	}

	public Integer getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public Boolean getAludefaultoriginatev4Key() {
		return aludefaultoriginatev4Key;
	}

	public void setAludefaultoriginatev4Key(Boolean aludefaultoriginatev4Key) {
		this.aludefaultoriginatev4Key = aludefaultoriginatev4Key;
	}

	public Boolean getAludefaultoriginatev6Key() {
		return aludefaultoriginatev6Key;
	}

	public void setAludefaultoriginatev6Key(Boolean aludefaultoriginatev6Key) {
		this.aludefaultoriginatev6Key = aludefaultoriginatev6Key;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getInboundIpv4PolicyName() {
		return inboundIpv4PolicyName;
	}

	public void setInboundIpv4PolicyName(String inboundIpv4PolicyName) {
		this.inboundIpv4PolicyName = inboundIpv4PolicyName;
	}

	public Boolean getInboundIpv4Preprovisioned() {
		return inboundIpv4Preprovisioned;
	}

	public void setInboundIpv4Preprovisioned(Boolean inboundIpv4Preprovisioned) {
		this.inboundIpv4Preprovisioned = inboundIpv4Preprovisioned;
	}

	public String getInboundIpv6PolicyName() {
		return inboundIpv6PolicyName;
	}

	public void setInboundIpv6PolicyName(String inboundIpv6PolicyName) {
		this.inboundIpv6PolicyName = inboundIpv6PolicyName;
	}

	public Boolean getInboundIpv6Preprovisioned() {
		return inboundIpv6Preprovisioned;
	}

	public void setInboundIpv6Preprovisioned(Boolean inboundIpv6Preprovisioned) {
		this.inboundIpv6Preprovisioned = inboundIpv6Preprovisioned;
	}

	public Boolean getInboundIstandardpolicyv4() {
		return inboundIstandardpolicyv4;
	}

	public void setInboundIstandardpolicyv4(Boolean inboundIstandardpolicyv4) {
		this.inboundIstandardpolicyv4 = inboundIstandardpolicyv4;
	}

	public Boolean getInboundIstandardpolicyv6() {
		return inboundIstandardpolicyv6;
	}

	public void setInboundIstandardpolicyv6(Boolean inboundIstandardpolicyv6) {
		this.inboundIstandardpolicyv6 = inboundIstandardpolicyv6;
	}

	public Boolean getInboundRoutingIpv4Policy() {
		return inboundRoutingIpv4Policy;
	}

	public void setInboundRoutingIpv4Policy(Boolean inboundRoutingIpv4Policy) {
		this.inboundRoutingIpv4Policy = inboundRoutingIpv4Policy;
	}

	public Boolean getInboundRoutingIpv6Policy() {
		return inboundRoutingIpv6Policy;
	}

	public void setInboundRoutingIpv6Policy(Boolean inboundRoutingIpv6Policy) {
		this.inboundRoutingIpv6Policy = inboundRoutingIpv6Policy;
	}

	public Boolean getIsadditionalpolicytermReqd() {
		return isadditionalpolicytermReqd;
	}

	public void setIsadditionalpolicytermReqd(Boolean isadditionalpolicytermReqd) {
		this.isadditionalpolicytermReqd = isadditionalpolicytermReqd;
	}

	public Boolean getIslpinvpnpolicyconfigured() {
		return islpinvpnpolicyconfigured;
	}

	public void setIslpinvpnpolicyconfigured(Boolean islpinvpnpolicyconfigured) {
		this.islpinvpnpolicyconfigured = islpinvpnpolicyconfigured;
	}

	public Boolean getIsvprnExportPreprovisioned() {
		return isvprnExportPreprovisioned;
	}

	public void setIsvprnExportPreprovisioned(Boolean isvprnExportPreprovisioned) {
		this.isvprnExportPreprovisioned = isvprnExportPreprovisioned;
	}

	public Boolean getIsvprnExportpolicy() {
		return isvprnExportpolicy;
	}

	public void setIsvprnExportpolicy(Boolean isvprnExportpolicy) {
		this.isvprnExportpolicy = isvprnExportpolicy;
	}

	public String getIsvprnExportpolicyName() {
		return isvprnExportpolicyName;
	}

	public void setIsvprnExportpolicyName(String isvprnExportpolicyName) {
		this.isvprnExportpolicyName = isvprnExportpolicyName;
	}

	public Boolean getIsvprnImportPreprovisioned() {
		return isvprnImportPreprovisioned;
	}

	public void setIsvprnImportPreprovisioned(Boolean isvprnImportPreprovisioned) {
		this.isvprnImportPreprovisioned = isvprnImportPreprovisioned;
	}

	public Boolean getIsvprnImportpolicy() {
		return isvprnImportpolicy;
	}

	public void setIsvprnImportpolicy(Boolean isvprnImportpolicy) {
		this.isvprnImportpolicy = isvprnImportpolicy;
	}

	public String getIsvprnImportpolicyName() {
		return isvprnImportpolicyName;
	}

	public void setIsvprnImportpolicyName(String isvprnImportpolicyName) {
		this.isvprnImportpolicyName = isvprnImportpolicyName;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLocalpreferencev4Vpnpolicy() {
		return localpreferencev4Vpnpolicy;
	}

	public void setLocalpreferencev4Vpnpolicy(String localpreferencev4Vpnpolicy) {
		this.localpreferencev4Vpnpolicy = localpreferencev4Vpnpolicy;
	}

	public String getLocalpreferencev6Vpnpolicy() {
		return localpreferencev6Vpnpolicy;
	}

	public void setLocalpreferencev6Vpnpolicy(String localpreferencev6Vpnpolicy) {
		this.localpreferencev6Vpnpolicy = localpreferencev6Vpnpolicy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Boolean getOriginatedefaultFlag() {
		return originatedefaultFlag;
	}

	public void setOriginatedefaultFlag(Boolean originatedefaultFlag) {
		this.originatedefaultFlag = originatedefaultFlag;
	}

	public String getOutboundIpv4PolicyName() {
		return outboundIpv4PolicyName;
	}

	public void setOutboundIpv4PolicyName(String outboundIpv4PolicyName) {
		this.outboundIpv4PolicyName = outboundIpv4PolicyName;
	}

	public Boolean getOutboundIpv4Preprovisioned() {
		return outboundIpv4Preprovisioned;
	}

	public void setOutboundIpv4Preprovisioned(Boolean outboundIpv4Preprovisioned) {
		this.outboundIpv4Preprovisioned = outboundIpv4Preprovisioned;
	}

	public String getOutboundIpv6PolicyName() {
		return outboundIpv6PolicyName;
	}

	public void setOutboundIpv6PolicyName(String outboundIpv6PolicyName) {
		this.outboundIpv6PolicyName = outboundIpv6PolicyName;
	}

	public Boolean getOutboundIpv6Preprovisioned() {
		return outboundIpv6Preprovisioned;
	}

	public void setOutboundIpv6Preprovisioned(Boolean outboundIpv6Preprovisioned) {
		this.outboundIpv6Preprovisioned = outboundIpv6Preprovisioned;
	}

	public Boolean getOutboundIstandardpolicyv4() {
		return outboundIstandardpolicyv4;
	}

	public void setOutboundIstandardpolicyv4(Boolean outboundIstandardpolicyv4) {
		this.outboundIstandardpolicyv4 = outboundIstandardpolicyv4;
	}

	public Boolean getOutboundIstandardpolicyv6() {
		return outboundIstandardpolicyv6;
	}

	public void setOutboundIstandardpolicyv6(Boolean outboundIstandardpolicyv6) {
		this.outboundIstandardpolicyv6 = outboundIstandardpolicyv6;
	}

	public Boolean getOutboundRoutingIpv4Policy() {
		return outboundRoutingIpv4Policy;
	}

	public void setOutboundRoutingIpv4Policy(Boolean outboundRoutingIpv4Policy) {
		this.outboundRoutingIpv4Policy = outboundRoutingIpv4Policy;
	}

	public Boolean getOutboundRoutingIpv6Policy() {
		return outboundRoutingIpv6Policy;
	}

	public void setOutboundRoutingIpv6Policy(Boolean outboundRoutingIpv6Policy) {
		this.outboundRoutingIpv6Policy = outboundRoutingIpv6Policy;
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