package com.tcl.dias.servicefulfillmentutils.beans.teamsdr;

import com.tcl.dias.servicefulfillment.entity.entities.ScTeamsDRServiceCommercial;
import com.tcl.dias.servicefulfillmentutils.beans.MstCatalogueBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScTeamsDRServiceCommercialBean;

import java.util.List;
import java.util.Map;

/**
 * TeamsDR Managed Services Components bean
 *
 * @author Srinivasa Raghavan
 * @createdAt 02/02/2021, Tuesday, 15:43
 */
public class TeamsDRManagedServicesComponentBean {
	private Integer componentId;
	private String componentName;
	private Map<String, String> attributes;
	private List<ScTeamsDRServiceCommercialBean> scTeamsDRServiceCommercialBeans;

	public TeamsDRManagedServicesComponentBean() {
	}

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

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<ScTeamsDRServiceCommercialBean> getScTeamsDRServiceCommercialBeans() {
		return scTeamsDRServiceCommercialBeans;
	}

	public void setScTeamsDRServiceCommercialBeans(
			List<ScTeamsDRServiceCommercialBean> scTeamsDRServiceCommercialBeans) {
		this.scTeamsDRServiceCommercialBeans = scTeamsDRServiceCommercialBeans;
	}

	@Override
	public String toString() {
		return "TeamsDRMgComponentBean{" + "componentId=" + componentId + ", componentName='" + componentName + '\'' + ", attributes=" + attributes + '}';
	}
}
