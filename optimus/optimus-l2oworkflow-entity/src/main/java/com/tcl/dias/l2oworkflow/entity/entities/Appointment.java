/**
 * 
 */
package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "appointment")
@NamedQuery(name = "Appointment.findAll", query = "SELECT a FROM Appointment a")
public class Appointment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8107523214468029802L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "appointment_date")
	private Timestamp appointmentDate;


	@Column(name = "local_it_contact_mobile")
	private String localItContactMobile;

	@Column(name = "local_it_name")
	private String localItname;

	@Column(name = "local_it_email")
	private String localItEmail;


	@Column(name = "reason")
	private String reason;

	@Column(name = "scopeOfWork")
	private String scopeOfWork;
	
	public String getScopeOfWork() {
		return scopeOfWork;
	}

	public void setScopeOfWork(String scopeOfWork) {
		this.scopeOfWork = scopeOfWork;
	}

	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "description")
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slot_id")
	private MstAppointmentSlots mstAppointmentSlot;

	// bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private MstStatus mstStatus;

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", referencedColumnName = "id")
	private Task task;
	
	@Column(name = "type")
	private String appointmentType;
	
	@Column(name = "reschedule")
	private String reschedule="N";
	
	
	
	@OneToMany(mappedBy = "appointment", cascade=CascadeType.ALL)
	private Set<AppointmentDocuments> appointmentDocuments;

	public Integer getId() {
		return id;
	}
	
	public String getReschedule() {
		return reschedule;
	}

	public void setReschedule(String reschedule) {
		this.reschedule = reschedule;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Timestamp appointmentDate) {
		this.appointmentDate = appointmentDate;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public MstStatus getMstStatus() {
		return mstStatus;
	}

	public void setMstStatus(MstStatus mstStatus) {
		this.mstStatus = mstStatus;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public Set<AppointmentDocuments> getAppointmentDocuments() {
		
		if(appointmentDocuments==null) {
			appointmentDocuments=new HashSet<>();
		}
		return appointmentDocuments;
	}

	public void setAppointmentDocuments(Set<AppointmentDocuments> appointmentDocuments) {
		this.appointmentDocuments = appointmentDocuments;
	}

	public MstAppointmentSlots getMstAppointmentSlot() {
		return mstAppointmentSlot;
	}

	public void setMstAppointmentSlot(MstAppointmentSlots mstAppointmentSlot) {
		this.mstAppointmentSlot = mstAppointmentSlot;
	}



	public String getLocalItContactMobile() {
		return localItContactMobile;
	}

	public void setLocalItContactMobile(String localItContactMobile) {
		this.localItContactMobile = localItContactMobile;
	}

	public String getLocalItname() {
		return localItname;
	}

	public void setLocalItname(String localItname) {
		this.localItname = localItname;
	}

	public String getLocalItEmail() {
		return localItEmail;
	}

	public void setLocalItEmail(String localItEmail) {
		this.localItEmail = localItEmail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
