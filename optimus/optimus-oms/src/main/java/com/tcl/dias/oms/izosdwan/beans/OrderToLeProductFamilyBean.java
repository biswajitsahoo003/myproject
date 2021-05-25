package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.OrderProductComponentBean;

/**
 * Bean class to hold Izosdwan order to le product family data
 * @author Madhumiethaa Palanisamy
 *
 */
@JsonInclude(Include.NON_NULL)
public class OrderToLeProductFamilyBean implements Serializable {
	private static final long serialVersionUID = -2587417786966320066L;

	private Integer id;

	private Integer orderVersion;

	private List<OrderProductSolutionsBean> orderProductSolutions;

	private List<OrderProductComponentBean> orderComponents;

	private String productName;

	private Byte status;

	private String termsAndCondition;

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
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
	 * @return the termsAndCondition
	 */
	public String getTermsAndCondition() {
		return termsAndCondition;
	}

	/**
	 * @param termsAndCondition
	 *            the termsAndCondition to set
	 */
	public void setTermsAndCondition(String termsAndCondition) {
		this.termsAndCondition = termsAndCondition;
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
	 * @return the orderProductSolutions
	 */
	public List<OrderProductSolutionsBean> getOrderProductSolutions() {
		if(orderProductSolutions==null) {
			orderProductSolutions=new ArrayList<>();
		}
		return orderProductSolutions;
	}

	public List<OrderProductComponentBean> getOrderComponents() {
		return orderComponents;
	}

	public void setOrderComponents(List<OrderProductComponentBean> orderComponents) {
		this.orderComponents = orderComponents;
	}

	/**
	 * @param orderProductSolutions
	 *            the orderProductSolutions to set
	 */
	public void setOrderProductSolutions(List<OrderProductSolutionsBean> orderProductSolutions) {
		this.orderProductSolutions = orderProductSolutions;
	}

}