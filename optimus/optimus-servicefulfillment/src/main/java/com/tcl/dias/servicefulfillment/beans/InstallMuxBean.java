package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to Install Mux
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class InstallMuxBean extends TaskDetailsBaseBean {

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String muxInstallationDate;
	private String muxInstallationStatus;
	
	private String muxMakeModelLatitude;
	
	private String muxMakeModelLongitude;

	private String peEndPhysicalPort;
	private List<AttachmentIdBean> documentIds;

	
	public String getMuxMakeModelLatitude() {
		return muxMakeModelLatitude;
	}
	public void setMuxMakeModelLatitude(String muxMakeModelLatitude) {
		this.muxMakeModelLatitude = muxMakeModelLatitude;
	}
	public String getMuxMakeModelLongitude() {
		return muxMakeModelLongitude;
	}
	public void setMuxMakeModelLongitude(String muxMakeModelLongitude) {
		this.muxMakeModelLongitude = muxMakeModelLongitude;
	}
	public String getMuxInstallationDate() {
		return muxInstallationDate;
	}
	public void setMuxInstallationDate(String muxInstallationDate) {
		this.muxInstallationDate = muxInstallationDate;
	}
	public String getMuxInstallationStatus() {
		return muxInstallationStatus;
	}
	public void setMuxInstallationStatus(String muxInstallationStatus) {
		this.muxInstallationStatus = muxInstallationStatus;
	}

	public String getPeEndPhysicalPort() {
		return peEndPhysicalPort;
	}

	public void setPeEndPhysicalPort(String peEndPhysicalPort) {
		this.peEndPhysicalPort = peEndPhysicalPort;
	}


	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
