package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * Entity Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_product_solutions")
@NamedQuery(name = "OrderProductSolution.findAll", query = "SELECT o FROM OrderProductSolution o")
public class OrderProductSolution implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Lob
	@Column(name = "product_profile_data")
	private String productProfileData;

	@Column(name = "solution_code")
	private String solutionCode;

	@Column(name = "tps_sfdc_product_id")
	private String tpsSfdcProductId;

	@Column(name = "tps_sfdc_product_name")
	private String tpsSfdcProductName;

	// bi-directional many-to-one association to OrderIllSite
	@OneToMany(mappedBy = "orderProductSolution")
	private Set<OrderIllSite> orderIllSites;

	// bi-directional many-to-one association to MstProductOffering
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_offering_id")
	private MstProductOffering mstProductOffering;

	// bi-directional many-to-one association to OrderToLeProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_le_product_family_id")
	private OrderToLeProductFamily orderToLeProductFamily;

	@Column(name = "is_bundle")
	private Character isBundle;

	@Column(name = "bundle_solution_id")
	private Integer bundleSolutionId;
	
	@OneToMany(mappedBy="orderProductSolution")
	private Set<OrderIzosdwanSite> orderIzosdwanSites;

	public OrderProductSolution() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductProfileData() {
		return this.productProfileData;
	}

	public void setProductProfileData(String productProfileData) {
		this.productProfileData = productProfileData;
	}

	public String getSolutionCode() {
		return this.solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

	public Set<OrderIllSite> getOrderIllSites() {
		return this.orderIllSites;
	}

	public void setOrderIllSites(Set<OrderIllSite> orderIllSites) {
		this.orderIllSites = orderIllSites;
	}

	public OrderIllSite addOrderIllSite(OrderIllSite orderIllSite) {
		getOrderIllSites().add(orderIllSite);
		orderIllSite.setOrderProductSolution(this);

		return orderIllSite;
	}

	public OrderIllSite removeOrderIllSite(OrderIllSite orderIllSite) {
		getOrderIllSites().remove(orderIllSite);
		orderIllSite.setOrderProductSolution(null);

		return orderIllSite;
	}

	public MstProductOffering getMstProductOffering() {
		return this.mstProductOffering;
	}

	public void setMstProductOffering(MstProductOffering mstProductOffering) {
		this.mstProductOffering = mstProductOffering;
	}

	public OrderToLeProductFamily getOrderToLeProductFamily() {
		return this.orderToLeProductFamily;
	}

	public void setOrderToLeProductFamily(OrderToLeProductFamily orderToLeProductFamily) {
		this.orderToLeProductFamily = orderToLeProductFamily;
	}

	public String getTpsSfdcProductId() {
		return tpsSfdcProductId;
	}

	public void setTpsSfdcProductId(String tpsSfdcProductId) {
		this.tpsSfdcProductId = tpsSfdcProductId;
	}

	public String getTpsSfdcProductName() {
		return tpsSfdcProductName;
	}

	public void setTpsSfdcProductName(String tpsSfdcProductName) {
		this.tpsSfdcProductName = tpsSfdcProductName;
	}

	public Character getIsBundle() {
		return isBundle;
	}

	public void setIsBundle(Character isBundle) {
		this.isBundle = isBundle;
	}

	public Integer getBundleSolutionId() {
		return bundleSolutionId;
	}

	public void setBundleSolutionId(Integer bundleSolutionId) {
		this.bundleSolutionId = bundleSolutionId;
	}

	public Set<OrderIzosdwanSite> getOrderIzosdwanSites() {
		return orderIzosdwanSites;
	}

	public void setOrderIzosdwanSites(Set<OrderIzosdwanSite> orderIzosdwanSites) {
		this.orderIzosdwanSites = orderIzosdwanSites;
	}

}