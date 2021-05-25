package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.beans.AddonsBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.VproxySolutionBean;
/**
 * Bean class to hold Izosdwan order product solution data
 * @author Madhumiethaa Palanisamy
 *
 */
@JsonInclude(Include.NON_NULL)
public class OrderProductSolutionsBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer orderVersion;

	private String productProfileData;

	private String offeringDescription;

	private String offeringName;

	private Byte status;

	private List<OrderIzosdwanSiteBean> orderIzosdwanSiteBeans;
	
	private List<AddonsBean> izosdwanAddonsBeans;
	
	private VproxySolutionBean  vproxySolutionBean;

	private List<OrderProductComponentBean> components;

	public VproxySolutionBean getVproxySolutionBean() {
		return vproxySolutionBean;
	}

	public void setVproxySolutionBean(VproxySolutionBean vproxySolutionBean) {
		this.vproxySolutionBean = vproxySolutionBean;
	}

	public List<AddonsBean> getIzosdwanAddonsBeans() {
		return izosdwanAddonsBeans;
	}

	public void setIzosdwanAddonsBeans(List<AddonsBean> izosdwanAddonsBeans) {
		this.izosdwanAddonsBeans = izosdwanAddonsBeans;
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

	/**
	 * @return the orderIzosdwanSiteBeans
	 */
	public List<OrderIzosdwanSiteBean> getOrderIzosdwanSiteBeans() {
		if(orderIzosdwanSiteBeans==null) {
			orderIzosdwanSiteBeans=new ArrayList<>();
		}
		return orderIzosdwanSiteBeans;
	}

	/**
	 * @param orderIzosdwanSiteBeans the orderIzosdwanSiteBeans to set
	 */
	public void setOrderIzosdwanSiteBeans(List<OrderIzosdwanSiteBean> orderIzosdwanSiteBeans) {
		this.orderIzosdwanSiteBeans = orderIzosdwanSiteBeans;
	}

	public List<OrderProductComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<OrderProductComponentBean> components) {
		this.components = components;
	}
}
