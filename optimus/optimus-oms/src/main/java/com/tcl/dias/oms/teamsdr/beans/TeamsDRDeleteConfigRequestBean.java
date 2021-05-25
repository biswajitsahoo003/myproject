package com.tcl.dias.oms.teamsdr.beans;

/**
 * Request bean for delete config
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRDeleteConfigRequestBean {
	private Integer configId;
	private Integer cityId;

	public TeamsDRDeleteConfigRequestBean() {

	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	@Override
	public String toString() {
		return "TeamsDRDeleteConfigRequestBean{" + "configId=" + configId + ", cityId=" + cityId + '}';
	}
}
