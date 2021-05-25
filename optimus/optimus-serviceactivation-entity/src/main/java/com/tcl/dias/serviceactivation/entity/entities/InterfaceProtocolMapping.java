package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * InterfaceProtocolMapping Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="interface_protocol_mapping")
@NamedQuery(name="InterfaceProtocolMapping.findAll", query="SELECT i FROM InterfaceProtocolMapping i")
public class InterfaceProtocolMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="interface_protocol_mapping_id")
	private Integer interfaceProtocolMappingId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="iscpe_lan_interface")
	private Byte iscpeLanInterface;

	@Column(name="iscpe_wan_interface")
	private Byte iscpeWanInterface;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	private Integer version;

	//bi-directional many-to-one association to Cpe
	@ManyToOne(fetch=FetchType.LAZY)
	private Cpe cpe;

	//bi-directional many-to-one association to RouterDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Router_details_router_id")
	private RouterDetail routerDetail;

	//bi-directional many-to-one association to Bgp
	@ManyToOne(fetch=FetchType.LAZY)
	private Bgp bgp;

	//bi-directional many-to-one association to ChannelizedE1serialInterface
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="channelized_e1serial_interface_e1serial_interface_id")
	private ChannelizedE1serialInterface channelizedE1serialInterface;

	//bi-directional many-to-one association to ChannelizedSdhInterface
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="channelized_sdh_interface_sdh_interface_id")
	private ChannelizedSdhInterface channelizedSdhInterface;

	//bi-directional many-to-one association to Eigrp
	@ManyToOne(fetch=FetchType.LAZY)
	private Eigrp eigrp;

	//bi-directional many-to-one association to EthernetInterface
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ethernet_interface_ethernet_interface_id")
	private EthernetInterface ethernetInterface;

	//bi-directional many-to-one association to Ospf
	@ManyToOne(fetch=FetchType.LAZY)
	private Ospf ospf;

	//bi-directional many-to-one association to Rip
	@ManyToOne(fetch=FetchType.LAZY)
	private Rip rip;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_details_id")
	private ServiceDetail serviceDetail;

	//bi-directional many-to-one association to Static
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="static_staticprotocol_id")
	private StaticProtocol staticProtocol;

	public InterfaceProtocolMapping() {
	}

	public Integer getInterfaceProtocolMappingId() {
		return this.interfaceProtocolMappingId;
	}

	public void setInterfaceProtocolMappingId(Integer interfaceProtocolMappingId) {
		this.interfaceProtocolMappingId = interfaceProtocolMappingId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getIscpeLanInterface() {
		return this.iscpeLanInterface;
	}

	public void setIscpeLanInterface(Byte iscpeLanInterface) {
		this.iscpeLanInterface = iscpeLanInterface;
	}

	public Byte getIscpeWanInterface() {
		return this.iscpeWanInterface;
	}

	public void setIscpeWanInterface(Byte iscpeWanInterface) {
		this.iscpeWanInterface = iscpeWanInterface;
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

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Cpe getCpe() {
		return this.cpe;
	}

	public void setCpe(Cpe cpe) {
		this.cpe = cpe;
	}

	public RouterDetail getRouterDetail() {
		return this.routerDetail;
	}

	public void setRouterDetail(RouterDetail routerDetail) {
		this.routerDetail = routerDetail;
	}

	public Bgp getBgp() {
		return this.bgp;
	}

	public void setBgp(Bgp bgp) {
		this.bgp = bgp;
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

	public Eigrp getEigrp() {
		return this.eigrp;
	}

	public void setEigrp(Eigrp eigrp) {
		this.eigrp = eigrp;
	}

	public EthernetInterface getEthernetInterface() {
		return this.ethernetInterface;
	}

	public void setEthernetInterface(EthernetInterface ethernetInterface) {
		this.ethernetInterface = ethernetInterface;
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

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	public StaticProtocol getStaticProtocol() {
		return staticProtocol;
	}

	public void setStaticProtocol(StaticProtocol staticProtocol) {
		this.staticProtocol = staticProtocol;
	}

}