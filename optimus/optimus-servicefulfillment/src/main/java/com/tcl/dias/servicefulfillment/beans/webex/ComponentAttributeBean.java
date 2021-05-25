package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.Map;

public class ComponentAttributeBean {
	private Integer componentId;
	private String componentName;
	private Map<String,String> componentAttributes;
	
	public Integer getComponentId() {
		return componentId;
	}
	
	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	
	public Map<String, String> getComponentAttributes() {
		return componentAttributes;
	}
	
	public void setComponentAttributes(Map<String, String> componentAttributes) {
		this.componentAttributes = componentAttributes;
	}
	
	
}
