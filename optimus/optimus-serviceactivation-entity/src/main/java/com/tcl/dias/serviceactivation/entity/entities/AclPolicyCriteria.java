package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * AclPolicyCriteria Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="acl_policy_criteria")
@NamedQuery(name="AclPolicyCriteria.findAll", query="SELECT a FROM AclPolicyCriteria a")
public class AclPolicyCriteria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="acl_policy_id")
	private Integer aclPolicyId;

	private String action;

	private String description;

	@Column(name="destination_any")
	private Byte destinationAny;

	@Column(name="destination_condition")
	private String destinationCondition;

	@Column(name="destination_portnumber")
	private String destinationPortnumber;

	@Column(name="destination_subnet")
	private String destinationSubnet;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="inbound_ipv4_acl_name")
	private String inboundIpv4AclName;

	@Column(name="inbound_ipv6_acl_name")
	private String inboundIpv6AclName;

	@Column(name="isinboundacl_ipv4_applied")
	private Byte isinboundaclIpv4Applied;

	@Column(name="isinboundacl_ipv4_preprovisioned")
	private Byte isinboundaclIpv4Preprovisioned;

	@Column(name="isinboundacl_ipv6_applied")
	private Byte isinboundaclIpv6Applied;

	@Column(name="isinboundacl_ipv6_preprovisioned")
	private Byte isinboundaclIpv6Preprovisioned;

	@Column(name="isoutboundacl_ipv4_applied")
	private Byte isoutboundaclIpv4Applied;

	@Column(name="isoutboundacl_ipv4_preprovisioned")
	private Byte isoutboundaclIpv4Preprovisioned;

	@Column(name="isoutboundacl_ipv6_applied")
	private Byte isoutboundaclIpv6Applied;

	@Column(name="isoutboundacl_ipv6_preprovisioned")
	private Byte isoutboundaclIpv6Preprovisioned;

	@Column(name="issvc_qos_coscriteria_ipaddr_acl")
	private Byte issvcQosCoscriteriaIpaddrAcl;

	@Column(name="issvc_qos_coscriteria_ipaddr_acl_name")
	private String issvcQosCoscriteriaIpaddrAclName;

	@Column(name="issvc_qos_coscriteria_ipaddr_preprovisioned")
	private Byte issvcQosCoscriteriaIpaddrPreprovisioned;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="outbound_ipv4_acl_name")
	private String outboundIpv4AclName;

	@Column(name="outbound_ipv6_acl_name")
	private String outboundIpv6AclName;

	private String protocol;

	private String sequence;

	@Column(name="source_any")
	private Byte sourceAny;

	@Column(name="source_condition")
	private String sourceCondition;

	@Column(name="source_portnumber")
	private String sourcePortnumber;

	@Column(name="source_subnet")
	private String sourceSubnet;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to ChannelizedE1serialInterface
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ChannelizedE1_Serial_interface_e1serial_interface_id")
	private ChannelizedE1serialInterface channelizedE1serialInterface;

	//bi-directional many-to-one association to ChannelizedSdhInterface
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="Channelized_sdh_interface_sdh_interface_id")
	private ChannelizedSdhInterface channelizedSdhInterface;

	//bi-directional many-to-one association to EthernetInterface
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="Ethernet_Interface_ethernet_interface_id")
	private EthernetInterface ethernetInterface;

	//bi-directional many-to-one association to ServiceCosCriteria
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="service_cos_criteria_service_cos_id")
	private ServiceCosCriteria serviceCosCriteria;

	//bi-directional many-to-one association to PolicyCriteria
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="policy_criteria_policy_criteria_id")
	private PolicyCriteria policyCriteria;

	public AclPolicyCriteria() {
	}

	public Integer getAclPolicyId() {
		return this.aclPolicyId;
	}

	public void setAclPolicyId(Integer aclPolicyId) {
		this.aclPolicyId = aclPolicyId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getDestinationAny() {
		return this.destinationAny;
	}

	public void setDestinationAny(Byte destinationAny) {
		this.destinationAny = destinationAny;
	}

	public String getDestinationCondition() {
		return this.destinationCondition;
	}

	public void setDestinationCondition(String destinationCondition) {
		this.destinationCondition = destinationCondition;
	}

	public String getDestinationPortnumber() {
		return this.destinationPortnumber;
	}

	public void setDestinationPortnumber(String destinationPortnumber) {
		this.destinationPortnumber = destinationPortnumber;
	}

	public String getDestinationSubnet() {
		return this.destinationSubnet;
	}

	public void setDestinationSubnet(String destinationSubnet) {
		this.destinationSubnet = destinationSubnet;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getInboundIpv4AclName() {
		return this.inboundIpv4AclName;
	}

	public void setInboundIpv4AclName(String inboundIpv4AclName) {
		this.inboundIpv4AclName = inboundIpv4AclName;
	}

	public String getInboundIpv6AclName() {
		return this.inboundIpv6AclName;
	}

	public void setInboundIpv6AclName(String inboundIpv6AclName) {
		this.inboundIpv6AclName = inboundIpv6AclName;
	}

	public Byte getIsinboundaclIpv4Applied() {
		return this.isinboundaclIpv4Applied;
	}

	public void setIsinboundaclIpv4Applied(Byte isinboundaclIpv4Applied) {
		this.isinboundaclIpv4Applied = isinboundaclIpv4Applied;
	}

	public Byte getIsinboundaclIpv4Preprovisioned() {
		return this.isinboundaclIpv4Preprovisioned;
	}

	public void setIsinboundaclIpv4Preprovisioned(Byte isinboundaclIpv4Preprovisioned) {
		this.isinboundaclIpv4Preprovisioned = isinboundaclIpv4Preprovisioned;
	}

	public Byte getIsinboundaclIpv6Applied() {
		return this.isinboundaclIpv6Applied;
	}

	public void setIsinboundaclIpv6Applied(Byte isinboundaclIpv6Applied) {
		this.isinboundaclIpv6Applied = isinboundaclIpv6Applied;
	}

	public Byte getIsinboundaclIpv6Preprovisioned() {
		return this.isinboundaclIpv6Preprovisioned;
	}

	public void setIsinboundaclIpv6Preprovisioned(Byte isinboundaclIpv6Preprovisioned) {
		this.isinboundaclIpv6Preprovisioned = isinboundaclIpv6Preprovisioned;
	}

	public Byte getIsoutboundaclIpv4Applied() {
		return this.isoutboundaclIpv4Applied;
	}

	public void setIsoutboundaclIpv4Applied(Byte isoutboundaclIpv4Applied) {
		this.isoutboundaclIpv4Applied = isoutboundaclIpv4Applied;
	}

	public Byte getIsoutboundaclIpv4Preprovisioned() {
		return this.isoutboundaclIpv4Preprovisioned;
	}

	public void setIsoutboundaclIpv4Preprovisioned(Byte isoutboundaclIpv4Preprovisioned) {
		this.isoutboundaclIpv4Preprovisioned = isoutboundaclIpv4Preprovisioned;
	}

	public Byte getIsoutboundaclIpv6Applied() {
		return this.isoutboundaclIpv6Applied;
	}

	public void setIsoutboundaclIpv6Applied(Byte isoutboundaclIpv6Applied) {
		this.isoutboundaclIpv6Applied = isoutboundaclIpv6Applied;
	}

	public Byte getIsoutboundaclIpv6Preprovisioned() {
		return this.isoutboundaclIpv6Preprovisioned;
	}

	public void setIsoutboundaclIpv6Preprovisioned(Byte isoutboundaclIpv6Preprovisioned) {
		this.isoutboundaclIpv6Preprovisioned = isoutboundaclIpv6Preprovisioned;
	}

	public Byte getIssvcQosCoscriteriaIpaddrAcl() {
		return this.issvcQosCoscriteriaIpaddrAcl;
	}

	public void setIssvcQosCoscriteriaIpaddrAcl(Byte issvcQosCoscriteriaIpaddrAcl) {
		this.issvcQosCoscriteriaIpaddrAcl = issvcQosCoscriteriaIpaddrAcl;
	}

	public String getIssvcQosCoscriteriaIpaddrAclName() {
		return this.issvcQosCoscriteriaIpaddrAclName;
	}

	public void setIssvcQosCoscriteriaIpaddrAclName(String issvcQosCoscriteriaIpaddrAclName) {
		this.issvcQosCoscriteriaIpaddrAclName = issvcQosCoscriteriaIpaddrAclName;
	}

	public Byte getIssvcQosCoscriteriaIpaddrPreprovisioned() {
		return this.issvcQosCoscriteriaIpaddrPreprovisioned;
	}

	public void setIssvcQosCoscriteriaIpaddrPreprovisioned(Byte issvcQosCoscriteriaIpaddrPreprovisioned) {
		this.issvcQosCoscriteriaIpaddrPreprovisioned = issvcQosCoscriteriaIpaddrPreprovisioned;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOutboundIpv4AclName() {
		return this.outboundIpv4AclName;
	}

	public void setOutboundIpv4AclName(String outboundIpv4AclName) {
		this.outboundIpv4AclName = outboundIpv4AclName;
	}

	public String getOutboundIpv6AclName() {
		return this.outboundIpv6AclName;
	}

	public void setOutboundIpv6AclName(String outboundIpv6AclName) {
		this.outboundIpv6AclName = outboundIpv6AclName;
	}

	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public Byte getSourceAny() {
		return this.sourceAny;
	}

	public void setSourceAny(Byte sourceAny) {
		this.sourceAny = sourceAny;
	}

	public String getSourceCondition() {
		return this.sourceCondition;
	}

	public void setSourceCondition(String sourceCondition) {
		this.sourceCondition = sourceCondition;
	}

	public String getSourcePortnumber() {
		return this.sourcePortnumber;
	}

	public void setSourcePortnumber(String sourcePortnumber) {
		this.sourcePortnumber = sourcePortnumber;
	}

	public String getSourceSubnet() {
		return this.sourceSubnet;
	}

	public void setSourceSubnet(String sourceSubnet) {
		this.sourceSubnet = sourceSubnet;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public ChannelizedE1serialInterface getChannelizedE1serialInterface() {
		return this.channelizedE1serialInterface;
	}

	public void setChannelizedE1serialInterface(ChannelizedE1serialInterface channelizedE1serialInterface) {
		this.channelizedE1serialInterface = channelizedE1serialInterface;
	}

	public ChannelizedSdhInterface getChannelizedSdhInterface() {
		return this.channelizedSdhInterface;
	}

	public void setChannelizedSdhInterface(ChannelizedSdhInterface channelizedSdhInterface) {
		this.channelizedSdhInterface = channelizedSdhInterface;
	}

	public EthernetInterface getEthernetInterface() {
		return this.ethernetInterface;
	}

	public void setEthernetInterface(EthernetInterface ethernetInterface) {
		this.ethernetInterface = ethernetInterface;
	}

	public ServiceCosCriteria getServiceCosCriteria() {
		return this.serviceCosCriteria;
	}

	public void setServiceCosCriteria(ServiceCosCriteria serviceCosCriteria) {
		this.serviceCosCriteria = serviceCosCriteria;
	}

	public PolicyCriteria getPolicyCriteria() {
		return this.policyCriteria;
	}

	public void setPolicyCriteria(PolicyCriteria policyCriteria) {
		this.policyCriteria = policyCriteria;
	}

}