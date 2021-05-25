
package com.tcl.dias.common.sfdc.response.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ProductServicesResponseBean.class is used for sfdc
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductServicesResponseBean {

	private String status;
	private List<ProductsserviceResponse> productsservices = null;
	private String productId;
	private String message;
	private String sfdcid;
	private String productSolutionCode;
	private boolean isError;
	private String errorMessage;
	private String type;
	private Integer quoteToLeId;
	private Boolean isCancel;

	// for teamsdr
	private String parentTpsSfdcOptyId;
	private Integer parentQuoteToLeId;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the productsservices
	 */
	public List<ProductsserviceResponse> getProductsservices() {
		if (productsservices == null) {
			productsservices = new ArrayList<>();
		}
		return productsservices;
	}

	/**
	 * @param productsservices the productsservices to set
	 */
	public void setProductsservices(List<ProductsserviceResponse> productsservices) {

		this.productsservices = productsservices;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the sfdcid
	 */
	public String getSfdcid() {
		return sfdcid;
	}

	/**
	 * @param sfdcid the sfdcid to set
	 */
	public void setSfdcid(String sfdcid) {
		this.sfdcid = sfdcid;
	}

	public String getProductSolutionCode() {
		return productSolutionCode;
	}

	public void setProductSolutionCode(String productSolutionCode) {
		this.productSolutionCode = productSolutionCode;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

	public String getParentTpsSfdcOptyId() {
		return parentTpsSfdcOptyId;
	}

	public void setParentTpsSfdcOptyId(String parentTpsSfdcOptyId) {
		this.parentTpsSfdcOptyId = parentTpsSfdcOptyId;
	}

	public Integer getParentQuoteToLeId() {
		return parentQuoteToLeId;
	}

	public void setParentQuoteToLeId(Integer parentQuoteToLeId) {
		this.parentQuoteToLeId = parentQuoteToLeId;
	}

	/**
	 * toString
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "ProductServicesResponseBean [status=" + status + ", productsservices=" + productsservices
				+ ", productId=" + productId + ", message=" + message + ", sfdcid=" + sfdcid +  ", parentTpsSfdcOptyId="
				+ parentTpsSfdcOptyId + ", parentQuoteToLeId=" + parentQuoteToLeId
				+ "]";
	}

}
