package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

/**
 * Id class for  IzoPcSlaView entity
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzoPcSlaViewId implements Serializable{
	private Integer productCatalogId;
	private Integer slaId;
	private Integer slaMetricId;
	private Integer slaIdNo;
	
	public IzoPcSlaViewId(Integer productCatalogId, Integer slaId, Integer slaMetricId, Integer slaIdNo) {
		super();
		this.productCatalogId = productCatalogId;
		this.slaId = slaId;
		this.slaMetricId = slaMetricId;
		this.slaIdNo = slaIdNo;
	}
	
	public IzoPcSlaViewId() {
		
	}

}
