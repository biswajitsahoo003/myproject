package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "engagement_service_solution")
@NamedQuery(name = "EngagementServiceSolution.findAll", query = "SELECT e FROM EngagementServiceSolution e")
public class EngagementServiceSolution implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "reference_id")
	private Integer referenceId;

	@Column(name = "reference_name")
	private String referenceName;

	@Column(name = "tps_m6_service_id")
	private String tpsM6ServiceId;

	// bi-directional many-to-one association to Engagement
	@ManyToOne(fetch = FetchType.LAZY)
	private Engagement engagement;

	public EngagementServiceSolution() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceName() {
		return this.referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getTpsM6ServiceId() {
		return this.tpsM6ServiceId;
	}

	public void setTpsM6ServiceId(String tpsM6ServiceId) {
		this.tpsM6ServiceId = tpsM6ServiceId;
	}

	public Engagement getEngagement() {
		return this.engagement;
	}

	public void setEngagement(Engagement engagement) {
		this.engagement = engagement;
	}

}