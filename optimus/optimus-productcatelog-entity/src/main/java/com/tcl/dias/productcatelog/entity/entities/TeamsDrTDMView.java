package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_mstmdr_mediagateway_AC_TDM database table.
 * 
 */
@Entity
@Table(name = "vw_mstmdr_mediagateway_AC_TDM")
@NamedQuery(name = "TeamsDrTDMView.findAll", query = "SELECT v FROM TeamsDrTDMView v")
public class TeamsDrTDMView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "AC_DC_power_adapter_Partcode")
	private String acDcPowerAdapterPartcode;

	@Column(name = "Media_Gateway_nm")
	private String mediaGatewayNm;

	@Id
	@Column(name = "Model_Number")
	private String modelNumber;

	@Column(name = "OVOC_License_Partcode")
	private String OVOCLicensePartcode;

	@Column(name = "OVOC_License_qty_per_MG")
	private Integer OVOCLicenseQtyPerMG;

	@Column(name = "Partcode")
	private String partcode;

	@Column(name = "PRI_Value")
	private int priValue;

	@Column(name = "SBC_License_Partcode")
	private String sbcLicensePartcode;

	@Column(name = "SBC_License_qty")
	private Integer sbcLicenseQty;

	@Column(name = "SBC_License_session")
	private Integer sbcLicenseSession;

	@Column(name = "vendor_name")
	private String vendorName;

	public TeamsDrTDMView() {
	}

	public String getAcDcPowerAdapterPartcode() {
		return acDcPowerAdapterPartcode;
	}

	public void setAcDcPowerAdapterPartcode(String acDcPowerAdapterPartcode) {
		this.acDcPowerAdapterPartcode = acDcPowerAdapterPartcode;
	}

	public String getMediaGatewayNm() {
		return mediaGatewayNm;
	}

	public void setMediaGatewayNm(String mediaGatewayNm) {
		this.mediaGatewayNm = mediaGatewayNm;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getOVOCLicensePartcode() {
		return OVOCLicensePartcode;
	}

	public void setOVOCLicensePartcode(String OVOCLicensePartcode) {
		this.OVOCLicensePartcode = OVOCLicensePartcode;
	}

	public Integer getOVOCLicenseQtyPerMG() {
		return OVOCLicenseQtyPerMG;
	}

	public void setOVOCLicenseQtyPerMG(Integer OVOCLicenseQtyPerMG) {
		this.OVOCLicenseQtyPerMG = OVOCLicenseQtyPerMG;
	}

	public String getPartcode() {
		return partcode;
	}

	public void setPartcode(String partcode) {
		this.partcode = partcode;
	}

	public int getPriValue() {
		return priValue;
	}

	public void setPriValue(int priValue) {
		this.priValue = priValue;
	}

	public String getSbcLicensePartcode() {
		return sbcLicensePartcode;
	}

	public void setSbcLicensePartcode(String sbcLicensePartcode) {
		this.sbcLicensePartcode = sbcLicensePartcode;
	}

	public Integer getSbcLicenseQty() {
		return sbcLicenseQty;
	}

	public void setSbcLicenseQty(Integer sbcLicenseQty) {
		this.sbcLicenseQty = sbcLicenseQty;
	}

	public Integer getSbcLicenseSession() {
		return sbcLicenseSession;
	}

	public void setSbcLicenseSession(Integer sbcLicenseSession) {
		this.sbcLicenseSession = sbcLicenseSession;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
}