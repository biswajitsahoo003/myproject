package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderProductSolutionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer orderVersion;

	private String productProfileData;

	private String offeringDescription;

	private String offeringName;

	private Byte status;

	private List<OrderIllSiteBean> orderIllSiteBeans;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the orderVersion
	 */
	public Integer getOrderVersion() {
		return orderVersion;
	}

	/**
	 * @param orderVersion
	 *            the orderVersion to set
	 */
	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	/**
	 * @return the productProfileData
	 */
	public String getProductProfileData() {
		return productProfileData;
	}

	/**
	 * @param productProfileData
	 *            the productProfileData to set
	 */
	public void setProductProfileData(String productProfileData) {
		this.productProfileData = productProfileData;
	}

	

	/**
	 * @return the offeringDescription
	 */
	public String getOfferingDescription() {
		return offeringDescription;
	}

	/**
	 * @param offeringDescription
	 *            the offeringDescription to set
	 */
	public void setOfferingDescription(String offeringDescription) {
		this.offeringDescription = offeringDescription;
	}

	/**
	 * @return the offeringName
	 */
	public String getOfferingName() {
		return offeringName;
	}

	/**
	 * @param offeringName
	 *            the offeringName to set
	 */
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * @return the orderIllSiteBeans
	 */
	public List<OrderIllSiteBean> getOrderIllSiteBeans() {
		if(orderIllSiteBeans==null) {
			orderIllSiteBeans=new ArrayList<>();
		}
		return orderIllSiteBeans;
	}

	/**
	 * @param orderIllSiteBeans the orderIllSiteBeans to set
	 */
	public void setOrderIllSiteBeans(List<OrderIllSiteBean> orderIllSiteBeans) {
		this.orderIllSiteBeans = orderIllSiteBeans;
	}
	
	

}
