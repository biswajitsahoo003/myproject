package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "appointment_documents")
@NamedQuery(name = "AppointmentDocuments.findAll", query = "SELECT m FROM AppointmentDocuments m")
public class AppointmentDocuments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9013035405907855177L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", referencedColumnName = "id")
	private Appointment appointment;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mst_document_id",referencedColumnName="id")
	private MstAppointmentDocuments mstAppointmentDocuments;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public MstAppointmentDocuments getMstAppointmentDocuments() {
		return mstAppointmentDocuments;
	}

	public void setMstAppointmentDocuments(MstAppointmentDocuments mstAppointmentDocuments) {
		this.mstAppointmentDocuments = mstAppointmentDocuments;
	}



}
