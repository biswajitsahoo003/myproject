package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "thirdparty_service_jobs_audit")
public class ThirdPartyServiceJobAudit  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "ref_id")
	private String refId;
	
	@Column(name = "make_success")
	private Byte makeSuccess;
	
	@Column(name = "success_reason")
	private String successReason;
	
	@Column(name = "thirdparty_service_jobs_id")
	private Integer thirdPartyServiceJobsId;
	
	@Lob
	@Column(name = "from_json")
	private String fromJSON;
	
	@Lob
	@Column(name = "to_json")
	private String toJSON;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Integer getThirdPartyServiceJobsId() {
		return thirdPartyServiceJobsId;
	}

	public void setThirdPartyServiceJobsId(Integer thirdPartyServiceJobsId) {
		this.thirdPartyServiceJobsId = thirdPartyServiceJobsId;
	}

	public String getFromJSON() {
		return fromJSON;
	}

	public void setFromJSON(String fromJSON) {
		this.fromJSON = fromJSON;
	}

	public String getToJSON() {
		return toJSON;
	}

	public void setToJSON(String toJSON) {
		this.toJSON = toJSON;
	}

	public Byte getMakeSuccess() {
		return makeSuccess;
	}

	public void setMakeSuccess(Byte makeSuccess) {
		this.makeSuccess = makeSuccess;
	}

	public String getSuccessReason() {
		return successReason;
	}

	public void setSuccessReason(String successReason) {
		this.successReason = successReason;
	}
	
	

}
