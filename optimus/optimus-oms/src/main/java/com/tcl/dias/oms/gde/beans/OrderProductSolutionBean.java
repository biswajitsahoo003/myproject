package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.OrderIllSiteBean;


/**
 * Bean class to hold OrderProductSolution
 * @author archchan
 *
 */
@JsonInclude(Include.NON_NULL)
public class OrderProductSolutionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer orderVersion;

	private String productProfileData;

	private String offeringDescription;

	private String offeringName;

	private Byte status;

	
	private List<GdeLinkBean> nplLinkBean;

	private List<OrderIllSiteBean> orderCrossConnectSite;

	public List<OrderIllSiteBean> getOrderCrossConnectSite() {
		return orderCrossConnectSite;
	}

	public void setOrderCrossConnectSite(List<OrderIllSiteBean> orderCrossConnectSite) {
		this.orderCrossConnectSite = orderCrossConnectSite;
	}
	

	public List<GdeLinkBean> getNplLinkBean() {
		return nplLinkBean;
	}

	public void setNplLinkBean(List<GdeLinkBean> nplLinkBean) {
		this.nplLinkBean = nplLinkBean;
	}

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

	
	
	

}