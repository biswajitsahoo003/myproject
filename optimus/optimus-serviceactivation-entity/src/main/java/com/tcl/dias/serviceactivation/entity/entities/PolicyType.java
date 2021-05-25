package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * PolicyType Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="policy_type")
@NamedQuery(name="PolicyType.findAll", query="SELECT p FROM PolicyType p")
public class PolicyType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="policy_id")
	private Integer policyId;

	@Column(name="aludefaultoriginatev4_key")
	private Byte aludefaultoriginatev4Key;

	@Column(name="aludefaultoriginatev6_key")
	private Byte aludefaultoriginatev6Key;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="inbound_ipv4_policy_name")
	private String inboundIpv4PolicyName;

	@Column(name="inbound_ipv4_preprovisioned")
	private Byte inboundIpv4Preprovisioned;

	@Column(name="inbound_ipv6_policy_name")
	private String inboundIpv6PolicyName;

	@Column(name="inbound_ipv6_preprovisioned")
	private Byte inboundIpv6Preprovisioned;

	@Column(name="inbound_istandardpolicyv4")
	private Byte inboundIstandardpolicyv4;

	@Column(name="inbound_istandardpolicyv6")
	private Byte inboundIstandardpolicyv6;

	@Column(name="inbound_routing_ipv4_policy")
	private Byte inboundRoutingIpv4Policy;

	@Column(name="inbound_routing_ipv6_policy")
	private Byte inboundRoutingIpv6Policy;

	@Column(name="isadditionalpolicyterm_reqd")
	private Byte isadditionalpolicytermReqd;

	private Byte islpinvpnpolicyconfigured;

	@Column(name="isvprn_export_preprovisioned")
	private Byte isvprnExportPreprovisioned;

	@Column(name="isvprn_exportpolicy")
	private Byte isvprnExportpolicy;

	@Column(name="isvprn_exportpolicy_name")
	private String isvprnExportpolicyName;

	@Column(name="isvprn_import_preprovisioned")
	private Byte isvprnImportPreprovisioned;

	@Column(name="isvprn_importpolicy")
	private Byte isvprnImportpolicy;

	@Column(name="isvprn_importpolicy_name")
	private String isvprnImportpolicyName;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="localpreferencev4_vpnpolicy")
	private String localpreferencev4Vpnpolicy;

	@Column(name="localpreferencev6_vpnpolicy")
	private String localpreferencev6Vpnpolicy;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="originatedefault_flag")
	private Byte originatedefaultFlag;

	@Column(name="outbound_ipv4_policy_name")
	private String outboundIpv4PolicyName;

	@Column(name="outbound_ipv4_preprovisioned")
	private Byte outboundIpv4Preprovisioned;

	@Column(name="outbound_ipv6_policy_name")
	private String outboundIpv6PolicyName;

	@Column(name="outbound_ipv6_preprovisioned")
	private Byte outboundIpv6Preprovisioned;

	@Column(name="outbound_istandardpolicyv4")
	private Byte outboundIstandardpolicyv4;

	@Column(name="outbound_istandardpolicyv6")
	private Byte outboundIstandardpolicyv6;

	@Column(name="outbound_routing_ipv4_policy")
	private Byte outboundRoutingIpv4Policy;

	@Column(name="outbound_routing_ipv6_policy")
	private Byte outboundRoutingIpv6Policy;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to Bgp
	@ManyToOne(fetch=FetchType.LAZY)
	private Bgp bgp;

	//bi-directional many-to-one association to Ospf
	@ManyToOne(fetch=FetchType.LAZY)
	private Ospf ospf;

	//bi-directional many-to-one association to Rip
	@ManyToOne(fetch=FetchType.LAZY)
	private Rip rip;

	//bi-directional many-to-one association to Static
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="static_staticprotocol_id")
	private StaticProtocol staticProtocol;

	//bi-directional many-to-one association to Vrf
	@ManyToOne(fetch=FetchType.LAZY)
	private Vrf vrf;

	//bi-directional many-to-one association to PolicyTypeCriteriaMapping
	@OneToMany(mappedBy="policyType")
	private Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings;

	public PolicyType() {
	}

	public Integer getPolicyId() {
		return this.policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public Byte getAludefaultoriginatev4Key() {
		return this.aludefaultoriginatev4Key;
	}

	public void setAludefaultoriginatev4Key(Byte aludefaultoriginatev4Key) {
		this.aludefaultoriginatev4Key = aludefaultoriginatev4Key;
	}

	public Byte getAludefaultoriginatev6Key() {
		return this.aludefaultoriginatev6Key;
	}

	public void setAludefaultoriginatev6Key(Byte aludefaultoriginatev6Key) {
		this.aludefaultoriginatev6Key = aludefaultoriginatev6Key;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getInboundIpv4PolicyName() {
		return this.inboundIpv4PolicyName;
	}

	public void setInboundIpv4PolicyName(String inboundIpv4PolicyName) {
		this.inboundIpv4PolicyName = inboundIpv4PolicyName;
	}

	public Byte getInboundIpv4Preprovisioned() {
		return this.inboundIpv4Preprovisioned;
	}

	public void setInboundIpv4Preprovisioned(Byte inboundIpv4Preprovisioned) {
		this.inboundIpv4Preprovisioned = inboundIpv4Preprovisioned;
	}

	public String getInboundIpv6PolicyName() {
		return this.inboundIpv6PolicyName;
	}

	public void setInboundIpv6PolicyName(String inboundIpv6PolicyName) {
		this.inboundIpv6PolicyName = inboundIpv6PolicyName;
	}

	public Byte getInboundIpv6Preprovisioned() {
		return this.inboundIpv6Preprovisioned;
	}

	public void setInboundIpv6Preprovisioned(Byte inboundIpv6Preprovisioned) {
		this.inboundIpv6Preprovisioned = inboundIpv6Preprovisioned;
	}

	public Byte getInboundIstandardpolicyv4() {
		return this.inboundIstandardpolicyv4;
	}

	public void setInboundIstandardpolicyv4(Byte inboundIstandardpolicyv4) {
		this.inboundIstandardpolicyv4 = inboundIstandardpolicyv4;
	}

	public Byte getInboundIstandardpolicyv6() {
		return this.inboundIstandardpolicyv6;
	}

	public void setInboundIstandardpolicyv6(Byte inboundIstandardpolicyv6) {
		this.inboundIstandardpolicyv6 = inboundIstandardpolicyv6;
	}

	public Byte getInboundRoutingIpv4Policy() {
		return this.inboundRoutingIpv4Policy;
	}

	public void setInboundRoutingIpv4Policy(Byte inboundRoutingIpv4Policy) {
		this.inboundRoutingIpv4Policy = inboundRoutingIpv4Policy;
	}

	public Byte getInboundRoutingIpv6Policy() {
		return this.inboundRoutingIpv6Policy;
	}

	public void setInboundRoutingIpv6Policy(Byte inboundRoutingIpv6Policy) {
		this.inboundRoutingIpv6Policy = inboundRoutingIpv6Policy;
	}

	public Byte getIsadditionalpolicytermReqd() {
		return this.isadditionalpolicytermReqd;
	}

	public void setIsadditionalpolicytermReqd(Byte isadditionalpolicytermReqd) {
		this.isadditionalpolicytermReqd = isadditionalpolicytermReqd;
	}

	public Byte getIslpinvpnpolicyconfigured() {
		return this.islpinvpnpolicyconfigured;
	}

	public void setIslpinvpnpolicyconfigured(Byte islpinvpnpolicyconfigured) {
		this.islpinvpnpolicyconfigured = islpinvpnpolicyconfigured;
	}

	public Byte getIsvprnExportPreprovisioned() {
		return this.isvprnExportPreprovisioned;
	}

	public void setIsvprnExportPreprovisioned(Byte isvprnExportPreprovisioned) {
		this.isvprnExportPreprovisioned = isvprnExportPreprovisioned;
	}

	public Byte getIsvprnExportpolicy() {
		return this.isvprnExportpolicy;
	}

	public void setIsvprnExportpolicy(Byte isvprnExportpolicy) {
		this.isvprnExportpolicy = isvprnExportpolicy;
	}

	public String getIsvprnExportpolicyName() {
		return this.isvprnExportpolicyName;
	}

	public void setIsvprnExportpolicyName(String isvprnExportpolicyName) {
		this.isvprnExportpolicyName = isvprnExportpolicyName;
	}

	public Byte getIsvprnImportPreprovisioned() {
		return this.isvprnImportPreprovisioned;
	}

	public void setIsvprnImportPreprovisioned(Byte isvprnImportPreprovisioned) {
		this.isvprnImportPreprovisioned = isvprnImportPreprovisioned;
	}

	public Byte getIsvprnImportpolicy() {
		return this.isvprnImportpolicy;
	}

	public void setIsvprnImportpolicy(Byte isvprnImportpolicy) {
		this.isvprnImportpolicy = isvprnImportpolicy;
	}

	public String getIsvprnImportpolicyName() {
		return this.isvprnImportpolicyName;
	}

	public void setIsvprnImportpolicyName(String isvprnImportpolicyName) {
		this.isvprnImportpolicyName = isvprnImportpolicyName;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLocalpreferencev4Vpnpolicy() {
		return this.localpreferencev4Vpnpolicy;
	}

	public void setLocalpreferencev4Vpnpolicy(String localpreferencev4Vpnpolicy) {
		this.localpreferencev4Vpnpolicy = localpreferencev4Vpnpolicy;
	}

	public String getLocalpreferencev6Vpnpolicy() {
		return this.localpreferencev6Vpnpolicy;
	}

	public void setLocalpreferencev6Vpnpolicy(String localpreferencev6Vpnpolicy) {
		this.localpreferencev6Vpnpolicy = localpreferencev6Vpnpolicy;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Byte getOriginatedefaultFlag() {
		return this.originatedefaultFlag;
	}

	public void setOriginatedefaultFlag(Byte originatedefaultFlag) {
		this.originatedefaultFlag = originatedefaultFlag;
	}

	public String getOutboundIpv4PolicyName() {
		return this.outboundIpv4PolicyName;
	}

	public void setOutboundIpv4PolicyName(String outboundIpv4PolicyName) {
		this.outboundIpv4PolicyName = outboundIpv4PolicyName;
	}

	public Byte getOutboundIpv4Preprovisioned() {
		return this.outboundIpv4Preprovisioned;
	}

	public void setOutboundIpv4Preprovisioned(Byte outboundIpv4Preprovisioned) {
		this.outboundIpv4Preprovisioned = outboundIpv4Preprovisioned;
	}

	public String getOutboundIpv6PolicyName() {
		return this.outboundIpv6PolicyName;
	}

	public void setOutboundIpv6PolicyName(String outboundIpv6PolicyName) {
		this.outboundIpv6PolicyName = outboundIpv6PolicyName;
	}

	public Byte getOutboundIpv6Preprovisioned() {
		return this.outboundIpv6Preprovisioned;
	}

	public void setOutboundIpv6Preprovisioned(Byte outboundIpv6Preprovisioned) {
		this.outboundIpv6Preprovisioned = outboundIpv6Preprovisioned;
	}

	public Byte getOutboundIstandardpolicyv4() {
		return this.outboundIstandardpolicyv4;
	}

	public void setOutboundIstandardpolicyv4(Byte outboundIstandardpolicyv4) {
		this.outboundIstandardpolicyv4 = outboundIstandardpolicyv4;
	}

	public Byte getOutboundIstandardpolicyv6() {
		return this.outboundIstandardpolicyv6;
	}

	public void setOutboundIstandardpolicyv6(Byte outboundIstandardpolicyv6) {
		this.outboundIstandardpolicyv6 = outboundIstandardpolicyv6;
	}

	public Byte getOutboundRoutingIpv4Policy() {
		return this.outboundRoutingIpv4Policy;
	}

	public void setOutboundRoutingIpv4Policy(Byte outboundRoutingIpv4Policy) {
		this.outboundRoutingIpv4Policy = outboundRoutingIpv4Policy;
	}

	public Byte getOutboundRoutingIpv6Policy() {
		return this.outboundRoutingIpv6Policy;
	}

	public void setOutboundRoutingIpv6Policy(Byte outboundRoutingIpv6Policy) {
		this.outboundRoutingIpv6Policy = outboundRoutingIpv6Policy;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Bgp getBgp() {
		return this.bgp;
	}

	public void setBgp(Bgp bgp) {
		this.bgp = bgp;
	}

	public Ospf getOspf() {
		return this.ospf;
	}

	public void setOspf(Ospf ospf) {
		this.ospf = ospf;
	}

	public Rip getRip() {
		return this.rip;
	}

	public void setRip(Rip rip) {
		this.rip = rip;
	}

	 
	public StaticProtocol getStaticProtocol() {
		return staticProtocol;
	}

	public void setStaticProtocol(StaticProtocol staticProtocol) {
		this.staticProtocol = staticProtocol;
	}

	public Vrf getVrf() {
		return this.vrf;
	}

	public void setVrf(Vrf vrf) {
		this.vrf = vrf;
	}

	public Set<PolicyTypeCriteriaMapping> getPolicyTypeCriteriaMappings() {
		return this.policyTypeCriteriaMappings;
	}

	public void setPolicyTypeCriteriaMappings(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings) {
		this.policyTypeCriteriaMappings = policyTypeCriteriaMappings;
	}

	public PolicyTypeCriteriaMapping addPolicyTypeCriteriaMapping(PolicyTypeCriteriaMapping policyTypeCriteriaMapping) {
		getPolicyTypeCriteriaMappings().add(policyTypeCriteriaMapping);
		policyTypeCriteriaMapping.setPolicyType(this);

		return policyTypeCriteriaMapping;
	}

	public PolicyTypeCriteriaMapping removePolicyTypeCriteriaMapping(PolicyTypeCriteriaMapping policyTypeCriteriaMapping) {
		getPolicyTypeCriteriaMappings().remove(policyTypeCriteriaMapping);
		policyTypeCriteriaMapping.setPolicyType(null);

		return policyTypeCriteriaMapping;
	}

}