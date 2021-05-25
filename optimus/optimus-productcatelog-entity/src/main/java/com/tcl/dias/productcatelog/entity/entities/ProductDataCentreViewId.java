package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
/**
 * Id class for ProductDataCentreView entity
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductDataCentreViewId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2006170282408738240L;

	private Integer productCatalogId;

	private String dataCenterCd;

	private Integer providerId;
	
	public ProductDataCentreViewId() {
		
	}

	public ProductDataCentreViewId(Integer productCatalogId, String dataCenterCd, Integer providerId) {
		super();
		this.productCatalogId = productCatalogId;
		this.dataCenterCd = dataCenterCd;
		this.providerId = providerId;
	}

	

}
