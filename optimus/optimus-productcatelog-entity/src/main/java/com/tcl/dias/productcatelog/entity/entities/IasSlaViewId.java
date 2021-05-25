package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

/**
 * This file contains the composite id for SLA view
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


public class IasSlaViewId implements Serializable{

	private Integer factorValueId;
	private Integer factorNameId;
	private Integer slaId;
	private Integer slaIdNo;


	
	public IasSlaViewId() {
		
	}
	
	

	public IasSlaViewId(int factorValueId, int factorNameId, Integer slaId, Integer slaIdNo) {
		super();
		this.factorValueId = factorValueId;
		this.factorNameId = factorNameId;
		this.slaId = slaId;
		this.slaIdNo = slaIdNo;
	}


}
