package com.tcl.dias.productcatelog.entity.entities;

/**
 * To Store SLA Related Primary IDs
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscSlaViewId {

	private Integer pdtCatalogId;
	private Integer slaIdNo;
	private Integer slaId;
	
	public GscSlaViewId() {
		
	}
	
	public GscSlaViewId(Integer pdtCatalogId, Integer slaIdNo, Integer slaId) {
		this.pdtCatalogId = pdtCatalogId;
		this.slaIdNo = slaIdNo;
		this.slaId = slaId;
	}

	public Integer getPdtCatalogId() {
		return pdtCatalogId;
	}

	public void setPdtCatalogId(Integer pdtCatalogId) {
		this.pdtCatalogId = pdtCatalogId;
	}

	public Integer getSlaIdNo() {
		return slaIdNo;
	}

	public void setSlaIdNo(Integer slaIdNo) {
		this.slaIdNo = slaIdNo;
	}

	public Integer getSlaId() {
		return slaId;
	}

	public void setSlaId(Integer slaId) {
		this.slaId = slaId;
	}

}
