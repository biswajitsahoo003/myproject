package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_product_offerings")
@NamedQuery(name = "MstProductOffering.findAll", query = "SELECT m FROM MstProductOffering m")
public class MstProductOffering implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "erf_product_offering_id")
	private Integer erfProductOfferingId;

	@Column(name = "product_description")
	private String productDescription;

	@Column(name = "product_name")
	private String productName;

	private Byte status;
	
	@Column(name= "vendor_cd")
	private String vendorCd;

	public String getVendorCd() {
		return vendorCd;
	}

	public void setVendorCd(String vendorCd) {
		this.vendorCd = vendorCd;
	}

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_family_id")
	private MstProductFamily mstProductFamily;

	// bi-directional many-to-one association to OrderProductSolution
	@OneToMany(mappedBy = "mstProductOffering")
	private Set<OrderProductSolution> orderProductSolutions;

	// bi-directional many-to-one association to ProductSolution
	@OneToMany(mappedBy = "mstProductOffering")
	private Set<ProductSolution> productSolutions;

	public MstProductOffering() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getErfProductOfferingId() {
		return this.erfProductOfferingId;
	}

	public void setErfProductOfferingId(Integer erfProductOfferingId) {
		this.erfProductOfferingId = erfProductOfferingId;
	}

	public String getProductDescription() {
		return this.productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	public Set<OrderProductSolution> getOrderProductSolutions() {
		return this.orderProductSolutions;
	}

	public void setOrderProductSolutions(Set<OrderProductSolution> orderProductSolutions) {
		this.orderProductSolutions = orderProductSolutions;
	}

	public OrderProductSolution addOrderProductSolution(OrderProductSolution orderProductSolution) {
		getOrderProductSolutions().add(orderProductSolution);
		orderProductSolution.setMstProductOffering(this);

		return orderProductSolution;
	}

	public OrderProductSolution removeOrderProductSolution(OrderProductSolution orderProductSolution) {
		getOrderProductSolutions().remove(orderProductSolution);
		orderProductSolution.setMstProductOffering(null);

		return orderProductSolution;
	}

	public Set<ProductSolution> getProductSolutions() {
		return this.productSolutions;
	}

	public void setProductSolutions(Set<ProductSolution> productSolutions) {
		this.productSolutions = productSolutions;
	}

	public ProductSolution addProductSolution(ProductSolution productSolution) {
		getProductSolutions().add(productSolution);
		productSolution.setMstProductOffering(this);

		return productSolution;
	}

	public ProductSolution removeProductSolution(ProductSolution productSolution) {
		getProductSolutions().remove(productSolution);
		productSolution.setMstProductOffering(null);

		return productSolution;
	}

}