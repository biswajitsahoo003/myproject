package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillment.entity.entities.MstAppointmentDocuments;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 * this class is used to return master document list
 */
public class MstAppointmentDocumentsBean {
	
	private String documentName;
	
	private Integer id;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
	public MstAppointmentDocumentsBean(MstAppointmentDocuments mstAppointmentDocuments) {
		this.documentName=mstAppointmentDocuments.getDocumentName();
		this.id=mstAppointmentDocuments.getId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	 

}
