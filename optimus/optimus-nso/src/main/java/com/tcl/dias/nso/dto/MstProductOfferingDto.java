package com.tcl.dias.nso.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * This file contains the MstProductComponentDto.java class.
 * Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstProductOfferingDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer erfProductOfferingId;

	private String productDescription;

	private String productName;

	private Byte status;

	private Set<ProductSolutionDto> productSolutions;

	/**
	 * @return the productSolutions
	 */
	public Set<ProductSolutionDto> getProductSolutions() {
		return productSolutions;
	}

	/**
	 * @param productSolutions the productSolutions to set
	 */
	public void setProductSolutions(Set<ProductSolutionDto> productSolutions) {
		this.productSolutions = productSolutions;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfProductOfferingId() {
		return this.erfProductOfferingId;
	}

	public void setErfProductOfferingId(Integer erfProductOfferingId) {
		this.erfProductOfferingId = erfProductOfferingId;
	}

	public String getProductDescription() {
		return this.productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * toString
	 * @return
	 */
	@Override
	public String toString() {
		return "MstProductOfferingDto [id=" + id + ", erfProductOfferingId=" + erfProductOfferingId
				+ ", productDescription=" + productDescription + ", productName=" + productName + ", status=" + status
				+ ", productSolutions=" + productSolutions + "]";
	}
	
	

}