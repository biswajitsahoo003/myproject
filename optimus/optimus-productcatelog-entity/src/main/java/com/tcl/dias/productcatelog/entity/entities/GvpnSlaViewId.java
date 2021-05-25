package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Custom Id class for GVPN SLA view
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GvpnSlaViewId implements Serializable {

	private Integer pdtCatalogId;
	private Integer slaIdNo;
	private Integer slaId;



	public GvpnSlaViewId() {
		
	}

	public GvpnSlaViewId(Integer pdtCatalogId, 
			Integer slaIdNo,Integer slaId) {
		super();
		this.pdtCatalogId = pdtCatalogId;
		this.slaIdNo = slaIdNo;
		this.slaId=slaId;
	}

}
