package com.tcl.dias.oms.macd.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This is the bean class for MACD Attributes list for primary and secondary components
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MACDAttributesBean {
	
	private List<MACDAttributesComparisonBean> primaryAttributesList;
	private List<MACDAttributesComparisonBean> secondaryAttributesList;
	/**
	 * @return the primaryAttributesList
	 */
	public List<MACDAttributesComparisonBean> getPrimaryAttributesList() {
		return primaryAttributesList;
	}
	/**
	 * @param primaryAttributesList the primaryAttributesList to set
	 */
	public void setPrimaryAttributesList(List<MACDAttributesComparisonBean> primaryAttributesList) {
		this.primaryAttributesList = primaryAttributesList;
	}
	/**
	 * @return the secondaryAttributesList
	 */
	public List<MACDAttributesComparisonBean> getSecondaryAttributesList() {
		return secondaryAttributesList;
	}
	/**
	 * @param secondaryAttributesList the secondaryAttributesList to set
	 */
	public void setSecondaryAttributesList(List<MACDAttributesComparisonBean> secondaryAttributesList) {
		this.secondaryAttributesList = secondaryAttributesList;
	}

	@Override
	public String toString() {
		return "MACDAttributesBean{" +
				"primaryAttributesList=" + primaryAttributesList +
				", secondaryAttributesList=" + secondaryAttributesList +
				'}';
	}
}
