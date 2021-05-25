package com.tcl.dias.oms.ipc.beans;

public class DeletedVmsBean {

	private String flavour;
	private String hostName;
	private String deletionDate;

	public DeletedVmsBean(String flavour, String hostName, String deletionDate) {
		this.flavour = flavour;
		this.hostName = hostName;
		this.deletionDate = deletionDate;
	}

	public String getFlavour() {
		return flavour;
	}

	public void setFlavour(String flavour) {
		this.flavour = flavour;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(String deletionDate) {
		this.deletionDate = deletionDate;
	}
}
