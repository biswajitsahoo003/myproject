package com.tcl.dias.response.beans;

import java.io.Serializable;
import com.tcl.dias.serviceassurance.entity.entities.ProductToServiceCatalog;
/**
 * This file contains the ProductToServiceCatalogBean.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductToServiceCatalogBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer erfproductCatalogId;

	private String erfPrdcatalogProductName;

	private MstServiceCatalogBean mstServiceCatalogBean;

	private String isactive;

	public MstServiceCatalogBean getMstServiceCatalogBean() {
		return mstServiceCatalogBean;
	}

	public void setMstServiceCatalogBean(MstServiceCatalogBean mstServiceCatalogBean) {
		this.mstServiceCatalogBean = mstServiceCatalogBean;
	}

	public Integer getErfproductCatalogId() {
		return erfproductCatalogId;
	}

	public void setErfproductCatalogId(Integer erfproductCatalogId) {
		this.erfproductCatalogId = erfproductCatalogId;
	}

	public String getErfPrdcatalogProductName() {
		return erfPrdcatalogProductName;
	}

	public void setErfPrdcatalogProductName(String erfPrdcatalogProductName) {
		this.erfPrdcatalogProductName = erfPrdcatalogProductName;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public ProductToServiceCatalogBean(ProductToServiceCatalog productToServiceCatalog) {
		this.erfproductCatalogId = productToServiceCatalog.getErfproductCatalogId();
		this.erfPrdcatalogProductName = productToServiceCatalog.getErfPrdcatalogProductName();
		this.isactive = productToServiceCatalog.getIsActive();
		if (productToServiceCatalog.getMstServiceCatalog() != null) {
			this.mstServiceCatalogBean = new MstServiceCatalogBean(productToServiceCatalog.getMstServiceCatalog());
		}
	}

}
