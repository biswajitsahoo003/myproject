package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;


/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the si_service_sla database table.
 * 
 */
@Entity
@Table(name="si_service_sla")
public class SIServiceSla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_time")
	private Timestamp createdTime;

	@Column(name="is_active")
	private String isActive;

	@Column(name="sla_component")
	private String slaComponent;

	@Column(name="sla_value")
	private String slaValue;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_time")
	private Timestamp updatedTime;

	//bi-directional many-to-one association to SiServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SI_service_detail_id")
	private SIServiceDetail siServiceDetail;

    public SIServiceSla() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getSlaComponent() {
		return this.slaComponent;
	}

	public void setSlaComponent(String slaComponent) {
		this.slaComponent = slaComponent;
	}

	public String getSlaValue() {
		return this.slaValue;
	}

	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public SIServiceDetail getSiServiceDetail() {
		return this.siServiceDetail;
	}

	public void setSiServiceDetail(SIServiceDetail siServiceDetail) {
		this.siServiceDetail = siServiceDetail;
	}
	
}