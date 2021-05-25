package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
/**
 * 
 * This the bean binds the chargaeble line item prices
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ChargeableLineItemSummaryBean implements Serializable{
	
	private String name;
	private String arc;
	private String mrc;
	private String nrc;
	private String tcv;
	private String annexRefNo;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArc() {
		return arc;
	}
	public void setArc(String arc) {
		this.arc = arc;
	}
	public String getNrc() {
		return nrc;
	}
	public void setNrc(String nrc) {
		this.nrc = nrc;
	}
	public String getTcv() {
		return tcv;
	}
	public void setTcv(String tcv) {
		this.tcv = tcv;
	}

	public String getAnnexRefNo() {
		return annexRefNo;
	}

	public void setAnnexRefNo(String annexRefNo) {
		this.annexRefNo = annexRefNo;
	}

	public String getMrc() {
		return mrc;
	}

	public void setMrc(String mrc) {
		this.mrc = mrc;
	}
}
