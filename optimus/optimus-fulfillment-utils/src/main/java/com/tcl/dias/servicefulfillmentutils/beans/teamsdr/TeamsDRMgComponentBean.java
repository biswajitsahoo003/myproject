package com.tcl.dias.servicefulfillmentutils.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.EndpointMaterialsBean;
import com.tcl.dias.servicefulfillmentutils.beans.MstCatalogueBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScTeamsDRServiceCommercialBean;

import java.util.List;
import java.util.Map;

/**
 * @author Syed Ali.
 * @createdAt 02/02/2021, Tuesday, 15:43
 */
public class TeamsDRMgComponentBean {
	private Integer componentId;
	private String componentName;
	private Map<String, String> attributes;
	private String cpeBOM;
	private List<ScTeamsDRServiceCommercialBean> commercials;
	private List<MstCatalogueBean> mstCatalogues;


	public TeamsDRMgComponentBean() {
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

	public List<ScTeamsDRServiceCommercialBean> getCommercials() {
		return commercials;
	}

	public void setCommercials(List<ScTeamsDRServiceCommercialBean> commercials) {
		this.commercials = commercials;
	}

	public String getCpeBOM() {
		return cpeBOM;
	}

	public void setCpeBOM(String cpeBOM) {
		this.cpeBOM = cpeBOM;
	}

	public List<MstCatalogueBean> getMstCatalogues() {
		return mstCatalogues;
	}

	public void setMstCatalogues(List<MstCatalogueBean> mstCatalogues) {
		this.mstCatalogues = mstCatalogues;
	}

	@Override
	public String toString() {
		return "TeamsDRMgComponentBean{" + "componentId=" + componentId + ", componentName='" + componentName + '\''
				+ ", attributes=" + attributes + ", cpeBOM='" + cpeBOM + '\'' + ", commercials=" + commercials
				+ ", mstCatalogues=" + mstCatalogues + '}';
	}
}
