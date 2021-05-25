package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

/**
 * This file contains the composite id for NPL SLA view
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


public class NplSlaViewId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer productCatalogId;
	private Integer slaId;
	private Integer slaMetricId;
	private String serviceVarient;
	private String accessTopology;
	

	public NplSlaViewId() {
		
	}

	public NplSlaViewId(Integer productCatalogId, Integer slaId, Integer slaMetricId, String serviceVarient,
			String accessTopology) {
		super();
		this.productCatalogId = productCatalogId;
		this.slaId = slaId;
		this.slaMetricId = slaMetricId;
		this.serviceVarient = serviceVarient;
		this.accessTopology = accessTopology;
	}
	

}
