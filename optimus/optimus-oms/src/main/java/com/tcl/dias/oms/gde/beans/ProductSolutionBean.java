package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.npl.beans.NplLinkBean;

/**
 * This ProductSolutionBean is for solution related beans
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSolutionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer productSolutionId;

	private String productProfileData;

	private Integer erfProductOfferingId;

	private String offeringDescription;

	private String offeringName;

	private Byte status;

	private SolutionDetail solution;

	
	private List<GdeLinkBean> links;
	
	private List<QuoteIllSiteBean> quoteIllSitesBean;

	public Integer getProductSolutionId() {
		return productSolutionId;
	}

	public void setProductSolutionId(Integer productSolutionId) {
		this.productSolutionId = productSolutionId;
	}

	public String getProductProfileData() {
		return productProfileData;
	}

	public void setProductProfileData(String productProfileData) {
		this.productProfileData = productProfileData;
	}

	public Integer getErfProductOfferingId() {
		return erfProductOfferingId;
	}

	public void setErfProductOfferingId(Integer erfProductOfferingId) {
		this.erfProductOfferingId = erfProductOfferingId;
	}

	public String getOfferingDescription() {
		return offeringDescription;
	}

	public void setOfferingDescription(String offeringDescription) {
		this.offeringDescription = offeringDescription;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public SolutionDetail getSolution() {
		return solution;
	}

	public void setSolution(SolutionDetail solution) {
		this.solution = solution;
	}

	public List<GdeLinkBean> getLinks() {
		return links;
	}

	public void setLinks(List<GdeLinkBean> links) {
		this.links = links;
	}

	public List<QuoteIllSiteBean> getQuoteIllSitesBean() {
		return quoteIllSitesBean;
	}

	public void setQuoteIllSitesBean(List<QuoteIllSiteBean> quoteIllSitesBean) {
		this.quoteIllSitesBean = quoteIllSitesBean;
	}

	@Override
	public String toString() {
		return "ProductSolutionBean [productSolutionId=" + productSolutionId + ", productProfileData="
				+ productProfileData + ", erfProductOfferingId=" + erfProductOfferingId + ", offeringDescription="
				+ offeringDescription + ", offeringName=" + offeringName + ", status=" + status + ", solution="
				+ solution + ", links=" + links + ", quoteIllSitesBean=" + quoteIllSitesBean + "]";
	}

	
}