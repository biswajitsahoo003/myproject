package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Bean class to hold OrderToLeProductFamily details
 * @author archchan
 *
 */
@JsonInclude(Include.NON_NULL)
public class OrderToLeProductFamilyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2587417786966320066L;

	private Integer id;

	private Integer orderVersion;

	private List<OrderProductSolutionBean> orderProductSolutions;

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
	public List<OrderProductSolutionBean> getOrderProductSolutions() {
		if(orderProductSolutions==null) {
			orderProductSolutions=new ArrayList<>();
		}
		return orderProductSolutions;
	}

	/**
	 * @param orderProductSolutions
	 *            the orderProductSolutions to set
	 */
	public void setOrderProductSolutions(List<OrderProductSolutionBean> orderProductSolutions) {
		this.orderProductSolutions = orderProductSolutions;
	}

}