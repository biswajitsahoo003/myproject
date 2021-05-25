package com.tcl.dias.oms.teamsdr.beans;

/**
 * Pricing request bean for license.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDrLicensePricingRequestBean {
	private Integer id;
	private String licenseName;
	private String provider;
	private String agreementType;
	private Integer noOfLicenses;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;

	public TeamsDrLicensePricingRequestBean() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public Integer getNoOfLicenses() {
		return noOfLicenses;
	}

	public void setNoOfLicenses(Integer noOfLicenses) {
		this.noOfLicenses = noOfLicenses;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	@Override
	public String toString() {
		return "TeamsDrLicenseRequestBean{" + "id=" + id + ", licenseName='" + licenseName + '\'' + ", provider='"
				+ provider + '\'' + ", agreementType='" + agreementType + '\'' + ", noOfLicenses=" + noOfLicenses
				+ ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + '}';
	}
}
