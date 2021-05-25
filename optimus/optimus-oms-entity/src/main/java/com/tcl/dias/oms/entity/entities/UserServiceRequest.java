package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the customer_request database table.
 *  @author Kusuma Kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="user_service_request")
@NamedQuery(name="UserServiceRequest.findAll", query="SELECT c FROM UserServiceRequest c")
public class UserServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_time")
	private Date createdTime;

	@Lob
	private String message;

	@Column(name="requested_time")
	private Date requestedTime;

	@Column(name="subject")
	private String subject;

	//bi-directional many-to-one association to User
	@Column(name="requested_by")
	private String requestedBy;

	public UserServiceRequest() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getRequestedTime() {
		return this.requestedTime;
	}

	public void setRequestedTime(Date requestedTime) {
		this.requestedTime = requestedTime;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUser() {
		return this.requestedBy;
	}

	public void setUser(String user) {
		this.requestedBy = user;
	}

}