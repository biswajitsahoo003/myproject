package com.tcl.dias.servicefulfillment.beans.teamsdr;

import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;


/**
 * @author Syed Ali.
 * @createdAt 01/02/2021, Monday, 15:25
 */

public class InstallEndpointBean extends TaskDetailsBaseBean {

	private String endpointCardSerialNumber;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String dateOfEndpointInstallation;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String endpointAmcStartDate;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String endpointAmcEndDate;

	private String endpointConsoleCableConnected;

	private List<AttachmentIdBean> documentIds;

	private List<SerialNumberBean> serialNumber;

	public String getEndpointCardSerialNumber() {
		return endpointCardSerialNumber;
	}

	public void setEndpointCardSerialNumber(String endpointCardSerialNumber) {
		this.endpointCardSerialNumber = endpointCardSerialNumber;
	}

	public String getDateOfEndpointInstallation() {
		return dateOfEndpointInstallation;
	}

	public void setDateOfEndpointInstallation(String dateOfEndpointInstallation) {
		this.dateOfEndpointInstallation = dateOfEndpointInstallation;
	}

	public String getEndpointAmcStartDate() {
		return endpointAmcStartDate;
	}

	public void setEndpointAmcStartDate(String endpointAmcStartDate) {
		this.endpointAmcStartDate = endpointAmcStartDate;
	}

	public String getEndpointAmcEndDate() {
		return endpointAmcEndDate;
	}

	public void setEndpointAmcEndDate(String endpointAmcEndDate) {
		this.endpointAmcEndDate = endpointAmcEndDate;
	}

	public String getEndpointConsoleCableConnected() {
		return endpointConsoleCableConnected;
	}

	public void setEndpointConsoleCableConnected(String endpointConsoleCableConnected) {
		this.endpointConsoleCableConnected = endpointConsoleCableConnected;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public List<SerialNumberBean> getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(List<SerialNumberBean> serialNumber) {
		this.serialNumber = serialNumber;
	}
}
