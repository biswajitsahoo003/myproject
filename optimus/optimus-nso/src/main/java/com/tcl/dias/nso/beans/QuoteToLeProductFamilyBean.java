/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.nso.beans.ProductSolutionBean;

/**
 * @author KarMani
 *
 */

@JsonInclude(Include.NON_NULL)
public class QuoteToLeProductFamilyBean implements Serializable {
	private static final long serialVersionUID = 4688523181743367780L;

	private Integer id;

	private List<ProductSolutionBean> solutions;

	private String productName;

	private Byte status;

	private String termsAndCondition;

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
	 * @return the solutions
	 */
	public List<ProductSolutionBean> getSolutions() {
		return solutions;
	}

	/**
	 * @param solutions the solutions to set
	 */
	public void setSolutions(List<ProductSolutionBean> solutions) {
		this.solutions = solutions;
	}

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

}
