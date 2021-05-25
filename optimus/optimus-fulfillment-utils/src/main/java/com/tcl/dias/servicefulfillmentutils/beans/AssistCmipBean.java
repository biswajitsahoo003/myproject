package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;
/**
 * 
 * This file contains the AssistCmipBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AssistCmipBean extends TaskDetailsBaseBean {
	private Integer taskId;
	private Integer serviceId;
	private List<ServiceLevelUpdateBean> serviceLevelUpdateBeans;
	private String wfTaskId;

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public List<ServiceLevelUpdateBean> getServiceLevelUpdateBeans() {
		return serviceLevelUpdateBeans;
	}

	public void setServiceLevelUpdateBeans(List<ServiceLevelUpdateBean> serviceLevelUpdateBeans) {
		this.serviceLevelUpdateBeans = serviceLevelUpdateBeans;
	}

	public String getWfTaskId() {
		return wfTaskId;
	}

	public void setWfTaskId(String wfTaskId) {
		this.wfTaskId = wfTaskId;
	}

}
