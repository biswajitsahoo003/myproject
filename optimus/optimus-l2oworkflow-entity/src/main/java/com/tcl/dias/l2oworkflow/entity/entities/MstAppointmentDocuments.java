package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mst_appointment_documents")
@NamedQuery(name = "MstAppointmentDocuments.findAll", query = "SELECT m FROM MstAppointmentDocuments m")
public class MstAppointmentDocuments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8228825285078572430L;
	
	@Id
	private Integer id;

	@Column(name = "document_name")
	private String documentName;

	@Column(name = "status")
	private String status;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
