package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "sfdc_jobs")
@NamedQuery(name = "SfdcJob.findAll", query = "SELECT s FROM SfdcJob s")
public class SfdcJob implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Column(name = "created_time")
	private Timestamp createdTime;

	private String operation;

	@Lob
	@Column(name = "request_payload")
	private String requestPayload;

	@Column(name = "queue_name")
	private String queueName;

	private String status;

	private String type;

	public SfdcJob() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRequestPayload() {
		return this.requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

}