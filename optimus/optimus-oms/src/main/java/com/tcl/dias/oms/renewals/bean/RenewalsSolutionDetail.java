package com.tcl.dias.oms.renewals.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



/**
 * 
 * Bean class to hold solution information for IZOPC
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewalsSolutionDetail implements Serializable {

	private static final long serialVersionUID = 5042900815778142832L;

	private String solutionCode;
	private String offeringName;
	private String image;
	private String bandwidth;
	private String bandwidthUnit;
	private String serviceId;
	private List<RenewalsComponentDetail> components = new ArrayList<RenewalsComponentDetail>();
	private List<RenewalsSiteDetail> siteDetail =new ArrayList<RenewalsSiteDetail>();;
    private Map<String,  RenewalsPriceBean>  renewalsPriceBean;
	private String accessType;
	private String ismultiVrf;
	private boolean isDual;
	private String copfId;
	private Character taxException;
	/**
	 * @return
	 */
	public String getSolutionCode() {
		return solutionCode;
	}

	/**
	 * @param solutionCode
	 */
	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
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
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the bandwidth
	 */
	public String getBandwidth() {
		return bandwidth;
	}

	/**
	 * @param bandwidth
	 *            the bandwidth to set
	 */
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	/**
	 * @return the bandwidthUnit
	 */
	public String getBandwidthUnit() {
		return bandwidthUnit;
	}

	/**
	 * @param bandwidthUnit
	 *            the bandwidthUnit to set
	 */
	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

	/**
	 * @return the components
	 */
	public List<RenewalsComponentDetail> getComponents() {
		return components;
	}

	/**
	 * @param components
	 *            the components to set
	 */
	public void setComponents(List<RenewalsComponentDetail> components) {
		this.components = components;
	}
	

	/**
	 * @return the siteDetail
	 */
	public List<RenewalsSiteDetail> getSiteDetail() {
		return siteDetail;
	}

	/**
	 * @param siteDetail the siteDetail to set
	 */
	public void setSiteDetail(List<RenewalsSiteDetail> siteDetail) {
		this.siteDetail = siteDetail;
	}
	

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Map<String, RenewalsPriceBean> getRenewalsPriceBean() {
		return renewalsPriceBean;
	}

	public void setRenewalsPriceBean(Map<String, RenewalsPriceBean> renewalsPriceBean) {
		this.renewalsPriceBean = renewalsPriceBean;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
		
	public String getIsmultiVrf() {
		return ismultiVrf;
	}

	public void setIsmultiVrf(String ismultiVrf) {
		this.ismultiVrf = ismultiVrf;
	}

	
	public boolean isDual() {
		return isDual;
	}

	public void setDual(boolean isDual) {
		this.isDual = isDual;
	}

	
	public String getCopfId() {
		return copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public Character getTaxException() {
		return taxException;
	}

	public void setTaxException(Character taxException) {
		this.taxException = taxException;
	}

	@Override
	public String toString() {
		return "RenewalsSolutionDetail [solutionCode=" + solutionCode + ", offeringName=" + offeringName + ", image="
				+ image + ", bandwidth=" + bandwidth + ", bandwidthUnit=" + bandwidthUnit + ", serviceId=" + serviceId
				+ ", components=" + components + ", siteDetail=" + siteDetail + ", renewalsPriceBean="
				+ renewalsPriceBean + ", accessType=" + accessType + ", ismultiVrf=" + ismultiVrf + ", isDual=" + isDual
				+ "]";
	}



}
