package com.tcl.dias.serviceinventory.beans;

import java.util.List;

import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.Priority;

public class SdwanPathPriorityBean {
	private String priority;
	private List<String> local;
	private List<String> remote;

	public static SdwanPathPriorityBean fromPriority(Priority priority) {
		SdwanPathPriorityBean sdwanPathPriorityBean = new SdwanPathPriorityBean();
		sdwanPathPriorityBean.setPriority(priority.getValue());
		sdwanPathPriorityBean.setLocal(priority.getCircuitNames().getLocal());
		sdwanPathPriorityBean.setRemote(priority.getCircuitNames().getRemote());
		return sdwanPathPriorityBean;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public List<String> getLocal() {
		return local;
	}

	public void setLocal(List<String> local) {
		this.local = local;
	}

	public List<String> getRemote() {
		return remote;
	}

	public void setRemote(List<String> remote) {
		this.remote = remote;
	}
}
