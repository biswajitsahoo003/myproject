package com.tcl.dias.oms.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Dto Class for product solution details
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductSolutionDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String productProfileData;

	private MstProductOfferingDto mstProductOffering;
	
	
	private List<QuoteIllSiteDto> illSiteDtos;


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductProfileData() {
		return this.productProfileData;
	}

	public void setProductProfileData(String productProfileData) {
		this.productProfileData = productProfileData;
	}

	/**
	 * @return the mstProductOffering
	 */
	public MstProductOfferingDto getMstProductOffering() {
		return mstProductOffering;
	}

	/**
	 * @param mstProductOffering the mstProductOffering to set
	 */
	public void setMstProductOffering(MstProductOfferingDto mstProductOffering) {
		this.mstProductOffering = mstProductOffering;
	}

	/**
	 * @return the illSiteDtos
	 */
	public List<QuoteIllSiteDto> getIllSiteDtos() {
		
		if(illSiteDtos==null) {
			illSiteDtos=new ArrayList<>();
		}
		return illSiteDtos;
	}

	/**
	 * @param illSiteDtos the illSiteDtos to set
	 */
	public void setIllSiteDtos(List<QuoteIllSiteDto> illSiteDtos) {
		this.illSiteDtos = illSiteDtos;
	}
	
	
	
	

}