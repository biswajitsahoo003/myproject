package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * VlanQosProfile Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vlan_qos_profile")
@NamedQuery(name="VlanQosProfile.findAll", query="SELECT v FROM VlanQosProfile v")
public class VlanQosProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="vlan_qos_profile_id")
	private Integer vlanQosProfileId;

	@Column(name="downstream_qos_profile")
	private String downstreamQosProfile;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="upstream_qos_profile")
	private String upstreamQosProfile;

	@Column(name="vlan_id")
	private Integer vlanId;

	//bi-directional many-to-one association to WimaxLastmile
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="wimax_lastmile_wimax_lastmile_id")
	private WimaxLastmile wimaxLastmile;

	public VlanQosProfile() {
	}

	public Integer getVlanQosProfileId() {
		return this.vlanQosProfileId;
	}

	public void setVlanQosProfileId(Integer vlanQosProfileId) {
		this.vlanQosProfileId = vlanQosProfileId;
	}

	public String getDownstreamQosProfile() {
		return this.downstreamQosProfile;
	}

	public void setDownstreamQosProfile(String downstreamQosProfile) {
		this.downstreamQosProfile = downstreamQosProfile;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
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

	public String getUpstreamQosProfile() {
		return this.upstreamQosProfile;
	}

	public void setUpstreamQosProfile(String upstreamQosProfile) {
		this.upstreamQosProfile = upstreamQosProfile;
	}

	public Integer getVlanId() {
		return this.vlanId;
	}

	public void setVlanId(Integer vlanId) {
		this.vlanId = vlanId;
	}

	public WimaxLastmile getWimaxLastmile() {
		return this.wimaxLastmile;
	}

	public void setWimaxLastmile(WimaxLastmile wimaxLastmile) {
		this.wimaxLastmile = wimaxLastmile;
	}

}