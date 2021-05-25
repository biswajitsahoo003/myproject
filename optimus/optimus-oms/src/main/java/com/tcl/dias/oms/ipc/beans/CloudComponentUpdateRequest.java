package com.tcl.dias.oms.ipc.beans;

import java.io.Serializable;
import java.util.List;

public class CloudComponentUpdateRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer cloudId;

	private String componentName;

	private List<AttributeUpdateRequest> attributes;

	public Integer getCloudId() {
		return cloudId;
	}

	public void setCloudId(Integer cloudId) {
		this.cloudId = cloudId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public List<AttributeUpdateRequest> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeUpdateRequest> attributes) {
		this.attributes = attributes;
	}

}
