package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

/**
 * Custom Id class for GVPN SLA COS view
 * 
 *
* @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GvpnSlaCosViewId implements Serializable {

	private Integer pdtCatalogId;
	private Integer slaIdNo;

	public GvpnSlaCosViewId() {

	}

	public GvpnSlaCosViewId(Integer pdtCatalogId, Integer slaIdNo) {
		super();
		this.pdtCatalogId = pdtCatalogId;
		this.slaIdNo = slaIdNo;
	}

}
