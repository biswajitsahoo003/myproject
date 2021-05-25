package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "product_catalog")
@NamedQuery(name = "ProductCatalog.findAll", query = "SELECT p FROM ProductCatalog p")
public class ProductCatalog extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "effective_from_dt")
	private Date effectiveFrom;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "effective_to_dt")
	private Date effectiveTo;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	@Column(name = "url")
	private String url;

	@Column(name = "service_details")
	private String serviceDetails;
	
	
	@Column(name="is_macd_enabled_flg") 
	private String isMacdEnabledFlag;
	
	@Column(name="product_short_nm")
	private String productShortName;

	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	// bi-directional many-to-one association to Product
	@OneToMany(mappedBy = "productFamily")
	private List<Product> products;

	// bi-directional many-to-one association to ProductSegment
	@ManyToOne
	@JoinColumn(name = "product_segment_id")
	private ProductSegment productSegment;


	public ProductCatalog() {
		// default constructor
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Product addProduct(Product product) {
		getProducts().add(product);
		product.setProductFamily(this);

		return product;
	}

	public Product removeProduct(Product product) {
		getProducts().remove(product);
		product.setProductFamily(null);

		return product;
	}

	public ProductSegment getProductSegment() {
		return this.productSegment;
	}

	public void setProductSegment(ProductSegment productSegment) {
		this.productSegment = productSegment;
	}

	

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(String serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public String getIsMacdEnabledFlag() {
		return isMacdEnabledFlag;
	}

	public void setIsMacdEnabledFlag(String isMacdEnabledFlag) {
		this.isMacdEnabledFlag = isMacdEnabledFlag;
	}
	

}