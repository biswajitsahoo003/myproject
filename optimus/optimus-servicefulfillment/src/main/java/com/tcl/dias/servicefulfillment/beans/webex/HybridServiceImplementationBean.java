package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class HybridServiceImplementationBean extends TaskDetailsBaseBean {
	
	private String dcDeployment;
	private String adContainerForUserSync;
	private String sso;
	private String cc;
	private String callService;
	private String videoMesh;
	private String dataSecurity;
	private String messageServices;
	public String getDcDeployment() {
		return dcDeployment;
	}
	public void setDcDeployment(String dcDeployment) {
		this.dcDeployment = dcDeployment;
	}
	public String getAdContainerForUserSync() {
		return adContainerForUserSync;
	}
	public void setAdContainerForUserSync(String adContainerForUserSync) {
		this.adContainerForUserSync = adContainerForUserSync;
	}
	public String getSso() {
		return sso;
	}
	public void setSso(String sso) {
		this.sso = sso;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getCallService() {
		return callService;
	}
	public void setCallService(String callService) {
		this.callService = callService;
	}
	public String getVideoMesh() {
		return videoMesh;
	}
	public void setVideoMesh(String videoMesh) {
		this.videoMesh = videoMesh;
	}
	public String getDataSecurity() {
		return dataSecurity;
	}
	public void setDataSecurity(String dataSecurity) {
		this.dataSecurity = dataSecurity;
	}
	public String getMessageServices() {
		return messageServices;
	}
	public void setMessageServices(String messageServices) {
		this.messageServices = messageServices;
	}

}
