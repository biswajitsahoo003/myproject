package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is used for dashboard related details based on legal entity
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DashboardLegalEntityBean {

	private String stage;

	private Integer familyCount;

	private Integer legEntityId;

	private DashboardQuoteBean quoteBean;

	private List<DashBoardFamilyBean> familyBeans;

	private boolean status;

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * @return the familyBeans
	 */
	public List<DashBoardFamilyBean> getFamilyBeans() {
		if (familyBeans == null) {
			familyBeans = new ArrayList<>();
		}
		return familyBeans;
	}

	/**
	 * @param familyBeans
	 *            the familyBeans to set
	 */
	public void setFamilyBeans(List<DashBoardFamilyBean> familyBeans) {
		this.familyBeans = familyBeans;
	}

	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the familyCount
	 */
	public Integer getFamilyCount() {
		return familyCount;
	}

	/**
	 * @param familyCount
	 *            the familyCount to set
	 */
	public void setFamilyCount(Integer familyCount) {
		this.familyCount = familyCount;
	}

	/**
	 * @return the quoteBean
	 */
	public DashboardQuoteBean getQuoteBean() {
		return quoteBean;
	}

	/**
	 * @param quoteBean
	 *            the quoteBean to set
	 */
	public void setQuoteBean(DashboardQuoteBean quoteBean) {
		this.quoteBean = quoteBean;
	}

	/**
	 * @return the legEntityId
	 */
	public Integer getLegEntityId() {
		return legEntityId;
	}

	/**
	 * @param legEntityId
	 *            the legEntityId to set
	 */
	public void setLegEntityId(Integer legEntityId) {
		this.legEntityId = legEntityId;
	}

}
