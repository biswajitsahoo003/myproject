package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * WanStaticRoute Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="wan_static_routes")
@NamedQuery(name="WanStaticRoute.findAll", query="SELECT w FROM WanStaticRoute w")
public class WanStaticRoute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="wanstaticroute_id")
	private Integer wanstaticrouteId;

	private String advalue;

	private String description;

	@Column(name="end_date")
	private Timestamp endDate;

	private Byte global;

	private String ipsubnet;

	@Column(name="is_cewan")
	private Byte isCewan;

	@Column(name="is_cpelan_staticroutes")
	private Byte isCpelanStaticroutes;

	@Column(name="is_cpewan_staticroutes")
	private Byte isCpewanStaticroutes;

	@Column(name="is_pewan")
	private Byte isPewan;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="next_hopid")
	private String nextHopid;

	@Column(name="pop_community")
	private String popCommunity;

	@Column(name="regional_community")
	private String regionalCommunity;

	@Column(name="service_community")
	private String serviceCommunity;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to Bgp
	@ManyToOne(fetch=FetchType.LAZY)
	private Bgp bgp;

	//bi-directional many-to-one association to Static
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="static_staticprotocol_id")
	private StaticProtocol staticProtocol;

	public WanStaticRoute() {
	}

	public Integer getWanstaticrouteId() {
		return this.wanstaticrouteId;
	}

	public void setWanstaticrouteId(Integer wanstaticrouteId) {
		this.wanstaticrouteId = wanstaticrouteId;
	}

	public String getAdvalue() {
		return this.advalue;
	}

	public void setAdvalue(String advalue) {
		this.advalue = advalue;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getGlobal() {
		return this.global;
	}

	public void setGlobal(Byte global) {
		this.global = global;
	}

	public String getIpsubnet() {
		return this.ipsubnet;
	}

	public void setIpsubnet(String ipsubnet) {
		this.ipsubnet = ipsubnet;
	}

	public Byte getIsCewan() {
		return this.isCewan;
	}

	public void setIsCewan(Byte isCewan) {
		this.isCewan = isCewan;
	}

	public Byte getIsCpelanStaticroutes() {
		return this.isCpelanStaticroutes;
	}

	public void setIsCpelanStaticroutes(Byte isCpelanStaticroutes) {
		this.isCpelanStaticroutes = isCpelanStaticroutes;
	}

	public Byte getIsCpewanStaticroutes() {
		return this.isCpewanStaticroutes;
	}

	public void setIsCpewanStaticroutes(Byte isCpewanStaticroutes) {
		this.isCpewanStaticroutes = isCpewanStaticroutes;
	}

	public Byte getIsPewan() {
		return this.isPewan;
	}

	public void setIsPewan(Byte isPewan) {
		this.isPewan = isPewan;
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

	public String getNextHopid() {
		return this.nextHopid;
	}

	public void setNextHopid(String nextHopid) {
		this.nextHopid = nextHopid;
	}

	public String getPopCommunity() {
		return this.popCommunity;
	}

	public void setPopCommunity(String popCommunity) {
		this.popCommunity = popCommunity;
	}

	public String getRegionalCommunity() {
		return this.regionalCommunity;
	}

	public void setRegionalCommunity(String regionalCommunity) {
		this.regionalCommunity = regionalCommunity;
	}

	public String getServiceCommunity() {
		return this.serviceCommunity;
	}

	public void setServiceCommunity(String serviceCommunity) {
		this.serviceCommunity = serviceCommunity;
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

	public StaticProtocol getStaticProtocol() {
		return staticProtocol;
	}

	public void setStaticProtocol(StaticProtocol staticProtocol) {
		this.staticProtocol = staticProtocol;
	}

	 
}