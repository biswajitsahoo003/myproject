package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
/**
 * This bean is used for CPE Installation.
 * 
 * @author yogesh
 */
public class InstallCPEBean extends TaskDetailsBaseBean {
	
	private String cpeSerialNumber;
	
	private String cpeCardSerialNumber;
		
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String dateOfCpeInstallation;
	
		
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeAmcEndDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeAmcStartDate;
	
	private String cpeWanInterface;
	
	private String cpeLanInterface;
	
	private String cpeConsoleCableConnected;
	
	private List<AttachmentIdBean> documentIds;

	
	public String getCpeSerialNumber() {
		return cpeSerialNumber;
	}

	public void setCpeSerialNumber(String cpeSerialNumber) {
		this.cpeSerialNumber = cpeSerialNumber;
	}

	public String getCpeCardSerialNumber() {
		return cpeCardSerialNumber;
	}

	public void setCpeCardSerialNumber(String cpeCardSerialNumber) {
		this.cpeCardSerialNumber = cpeCardSerialNumber;
	}

	public String getCpeConsoleCableConnected() {
		return cpeConsoleCableConnected;
	}

	public void setCpeConsoleCableConnected(String cpeConsoleCableConnected) {
		this.cpeConsoleCableConnected = cpeConsoleCableConnected;
	}

	public String getCpeAmcEndDate() {
		return cpeAmcEndDate;
	}

	public void setCpeAmcEndDate(String cpeAmcEndDate) {
		this.cpeAmcEndDate = cpeAmcEndDate;
	}

	public String getCpeAmcStartDate() {
		return cpeAmcStartDate;
	}

	public void setCpeAmcStartDate(String cpeAmcStartDate) {
		this.cpeAmcStartDate = cpeAmcStartDate;
	}

	public String getCpeWanInterface() {
		return cpeWanInterface;
	}

	public void setCpeWanInterface(String cpeWanInterface) {
		this.cpeWanInterface = cpeWanInterface;
	}

	public String getCpeLanInterface() {
		return cpeLanInterface;
	}

	public void setCpeLanInterface(String cpeLanInterface) {
		this.cpeLanInterface = cpeLanInterface;
	}
	
	
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getDateOfCpeInstallation() { return dateOfCpeInstallation; }

	public void setDateOfCpeInstallation(String dateOfCpeInstallation) { this.dateOfCpeInstallation = dateOfCpeInstallation; }


}
