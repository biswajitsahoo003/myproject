package com.tcl.dias.serviceassurance.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * Entity Class
 * 
 *The persistent class for the product_to_service_catalog database table.
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="product_to_service_catalog")
@NamedQuery(name="ProductToServiceCatalog.findAll", query="SELECT n FROM ProductToServiceCatalog n")
public class ProductToServiceCatalog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="erf_prd_catalog_product_catalog_id")
	private Integer  erfproductCatalogId;
	
	@Column(name="erf_prd_catalog_product_name")
	private String  erfPrdcatalogProductName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service_catalog_id")
	private MstServiceCatalog mstServiceCatalog;
	
	public MstServiceCatalog getMstServiceCatalog() {
		return mstServiceCatalog;
	}

	public void setMstServiceCatalog(MstServiceCatalog mstServiceCatalog) {
		this.mstServiceCatalog = mstServiceCatalog;
	}
	@Column(name="is_active")
	private String  isActive;
	
	public ProductToServiceCatalog() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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


	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
}