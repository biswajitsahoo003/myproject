package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * NeighbourCommunityConfig Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="neighbour_community_config")
@NamedQuery(name="NeighbourCommunityConfig.findAll", query="SELECT n FROM NeighbourCommunityConfig n")
public class NeighbourCommunityConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="neighbour_community_id")
	private Integer neighbourCommunityId;

	@Column(name="nc_action")
	private String action;

	private String community;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="iscommunity_extended")
	private Byte iscommunityExtended;

	private Byte ispreprovisioned;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	private String name;

	@Column(name="nc_option")
	private String option;

	@Column(name="start_date")
	private Timestamp startDate;

	private String type;

	//bi-directional many-to-one association to PolicyCriteria
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="Routing_Policy_Criteria_policy_criteria_id")
	private PolicyCriteria policyCriteria;

	public NeighbourCommunityConfig() {
	}

	public Integer getNeighbourCommunityId() {
		return this.neighbourCommunityId;
	}

	public void setNeighbourCommunityId(Integer neighbourCommunityId) {
		this.neighbourCommunityId = neighbourCommunityId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCommunity() {
		return this.community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getIscommunityExtended() {
		return this.iscommunityExtended;
	}

	public void setIscommunityExtended(Byte iscommunityExtended) {
		this.iscommunityExtended = iscommunityExtended;
	}

	public Byte getIspreprovisioned() {
		return this.ispreprovisioned;
	}

	public void setIspreprovisioned(Byte ispreprovisioned) {
		this.ispreprovisioned = ispreprovisioned;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOption() {
		return this.option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PolicyCriteria getPolicyCriteria() {
		return this.policyCriteria;
	}

	public void setPolicyCriteria(PolicyCriteria policyCriteria) {
		this.policyCriteria = policyCriteria;
	}

}