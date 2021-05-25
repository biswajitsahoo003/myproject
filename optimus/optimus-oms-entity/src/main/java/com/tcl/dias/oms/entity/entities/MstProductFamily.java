package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "mst_product_family")
@NamedQuery(name = "MstProductFamily.findAll", query = "SELECT m FROM MstProductFamily m")
public class MstProductFamily implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	private Byte status;
	
	@Column(name = "erf_prod_catalog_product_family_id")
	private Integer productCatalogFamilyId;
	
	// bi-directional many-to-one association to Engagement
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<Engagement> engagements;

	// bi-directional many-to-one association to MstProductOffering
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<MstProductOffering> mstProductOfferings;

	// bi-directional many-to-one association to OrderPrice
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<OrderPrice> orderPrices;

	// bi-directional many-to-one association to OrderProductComponent
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<OrderProductComponent> orderProductComponents;

	// bi-directional many-to-one association to OrderToLeProductFamily
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<OrderToLeProductFamily> orderToLeProductFamilies;

	// bi-directional many-to-one association to QuotePrice
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<QuotePrice> quotePrices;

	// bi-directional many-to-one association to QuoteProductComponent
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<QuoteProductComponent> quoteProductComponents;

	// bi-directional many-to-one association to QuoteProvisioningFeasibility
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<QuoteProvisioningFeasibility> quoteProvisioningFeasibilities;

	// bi-directional many-to-one association to QuoteToLeProductFamily
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<QuoteToLeProductFamily> quoteToLeProductFamilies;
	
	// bi-directional many-to-one association to QuoteToLeProductFamily
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<QuoteCloud> quoteClouds;
	
	// bi-directional many-to-one association to QuoteToLeProductFamily
	@OneToMany(mappedBy = "mstProductFamily")
	private Set<OrderCloud> orderClouds;
	
	// Enum column
	@Column(name="product_category")
	private String productCategory;
	
	public MstProductFamily() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Set<Engagement> getEngagements() {
		return this.engagements;
	}

	public void setEngagements(Set<Engagement> engagements) {
		this.engagements = engagements;
	}

	public Engagement addEngagement(Engagement engagement) {
		getEngagements().add(engagement);
		engagement.setMstProductFamily(this);

		return engagement;
	}

	public Engagement removeEngagement(Engagement engagement) {
		getEngagements().remove(engagement);
		engagement.setMstProductFamily(null);

		return engagement;
	}

	public Set<MstProductOffering> getMstProductOfferings() {
		return this.mstProductOfferings;
	}

	public void setMstProductOfferings(Set<MstProductOffering> mstProductOfferings) {
		this.mstProductOfferings = mstProductOfferings;
	}

	public MstProductOffering addMstProductOffering(MstProductOffering mstProductOffering) {
		getMstProductOfferings().add(mstProductOffering);
		mstProductOffering.setMstProductFamily(this);

		return mstProductOffering;
	}

	public MstProductOffering removeMstProductOffering(MstProductOffering mstProductOffering) {
		getMstProductOfferings().remove(mstProductOffering);
		mstProductOffering.setMstProductFamily(null);

		return mstProductOffering;
	}

	public Set<OrderPrice> getOrderPrices() {
		return this.orderPrices;
	}

	public void setOrderPrices(Set<OrderPrice> orderPrices) {
		this.orderPrices = orderPrices;
	}

	public OrderPrice addOrderPrice(OrderPrice orderPrice) {
		getOrderPrices().add(orderPrice);
		orderPrice.setMstProductFamily(this);

		return orderPrice;
	}

	public OrderPrice removeOrderPrice(OrderPrice orderPrice) {
		getOrderPrices().remove(orderPrice);
		orderPrice.setMstProductFamily(null);

		return orderPrice;
	}

	public Set<OrderProductComponent> getOrderProductComponents() {
		return this.orderProductComponents;
	}

	public void setOrderProductComponents(Set<OrderProductComponent> orderProductComponents) {
		this.orderProductComponents = orderProductComponents;
	}

	public OrderProductComponent addOrderProductComponent(OrderProductComponent orderProductComponent) {
		getOrderProductComponents().add(orderProductComponent);
		orderProductComponent.setMstProductFamily(this);

		return orderProductComponent;
	}

	public OrderProductComponent removeOrderProductComponent(OrderProductComponent orderProductComponent) {
		getOrderProductComponents().remove(orderProductComponent);
		orderProductComponent.setMstProductFamily(null);

		return orderProductComponent;
	}

	public Set<OrderToLeProductFamily> getOrderToLeProductFamilies() {
		return this.orderToLeProductFamilies;
	}

	public void setOrderToLeProductFamilies(Set<OrderToLeProductFamily> orderToLeProductFamilies) {
		this.orderToLeProductFamilies = orderToLeProductFamilies;
	}

	public OrderToLeProductFamily addOrderToLeProductFamily(OrderToLeProductFamily orderToLeProductFamily) {
		getOrderToLeProductFamilies().add(orderToLeProductFamily);
		orderToLeProductFamily.setMstProductFamily(this);

		return orderToLeProductFamily;
	}

	public OrderToLeProductFamily removeOrderToLeProductFamily(OrderToLeProductFamily orderToLeProductFamily) {
		getOrderToLeProductFamilies().remove(orderToLeProductFamily);
		orderToLeProductFamily.setMstProductFamily(null);

		return orderToLeProductFamily;
	}

	public Set<QuotePrice> getQuotePrices() {
		return this.quotePrices;
	}

	public void setQuotePrices(Set<QuotePrice> quotePrices) {
		this.quotePrices = quotePrices;
	}

	public QuotePrice addQuotePrice(QuotePrice quotePrice) {
		getQuotePrices().add(quotePrice);
		quotePrice.setMstProductFamily(this);

		return quotePrice;
	}

	public QuotePrice removeQuotePrice(QuotePrice quotePrice) {
		getQuotePrices().remove(quotePrice);
		quotePrice.setMstProductFamily(null);

		return quotePrice;
	}

	public Set<QuoteProductComponent> getQuoteProductComponents() {
		return this.quoteProductComponents;
	}

	public void setQuoteProductComponents(Set<QuoteProductComponent> quoteProductComponents) {
		this.quoteProductComponents = quoteProductComponents;
	}

	public QuoteProductComponent addQuoteProductComponent(QuoteProductComponent quoteProductComponent) {
		getQuoteProductComponents().add(quoteProductComponent);
		quoteProductComponent.setMstProductFamily(this);

		return quoteProductComponent;
	}

	public QuoteProductComponent removeQuoteProductComponent(QuoteProductComponent quoteProductComponent) {
		getQuoteProductComponents().remove(quoteProductComponent);
		quoteProductComponent.setMstProductFamily(null);

		return quoteProductComponent;
	}

	public Set<QuoteProvisioningFeasibility> getQuoteProvisioningFeasibilities() {
		return this.quoteProvisioningFeasibilities;
	}

	public void setQuoteProvisioningFeasibilities(Set<QuoteProvisioningFeasibility> quoteProvisioningFeasibilities) {
		this.quoteProvisioningFeasibilities = quoteProvisioningFeasibilities;
	}

	public QuoteProvisioningFeasibility addQuoteProvisioningFeasibility(
			QuoteProvisioningFeasibility quoteProvisioningFeasibility) {
		getQuoteProvisioningFeasibilities().add(quoteProvisioningFeasibility);
		quoteProvisioningFeasibility.setMstProductFamily(this);

		return quoteProvisioningFeasibility;
	}

	public QuoteProvisioningFeasibility removeQuoteProvisioningFeasibility(
			QuoteProvisioningFeasibility quoteProvisioningFeasibility) {
		getQuoteProvisioningFeasibilities().remove(quoteProvisioningFeasibility);
		quoteProvisioningFeasibility.setMstProductFamily(null);

		return quoteProvisioningFeasibility;
	}

	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilies() {
		return this.quoteToLeProductFamilies;
	}

	public void setQuoteToLeProductFamilies(Set<QuoteToLeProductFamily> quoteToLeProductFamilies) {
		this.quoteToLeProductFamilies = quoteToLeProductFamilies;
	}

	public QuoteToLeProductFamily addQuoteToLeProductFamily(QuoteToLeProductFamily quoteToLeProductFamily) {
		getQuoteToLeProductFamilies().add(quoteToLeProductFamily);
		quoteToLeProductFamily.setMstProductFamily(this);

		return quoteToLeProductFamily;
	}

	public QuoteToLeProductFamily removeQuoteToLeProductFamily(QuoteToLeProductFamily quoteToLeProductFamily) {
		getQuoteToLeProductFamilies().remove(quoteToLeProductFamily);
		quoteToLeProductFamily.setMstProductFamily(null);

		return quoteToLeProductFamily;
	}

	
	public Integer getProductCatalogFamilyId() {
		return productCatalogFamilyId;
	}

	public void setProductCatalogFamilyId(Integer productCatalogFamilyId) {
		this.productCatalogFamilyId = productCatalogFamilyId;
	}

	public Set<QuoteCloud> getQuoteClouds() {
		return this.quoteClouds;
	}

	public void setQuoteClouds(Set<QuoteCloud> quoteClouds) {
		this.quoteClouds = quoteClouds;
	}

	public QuoteCloud addQuoteCloud(QuoteCloud quoteCloud) {
		getQuoteClouds().add(quoteCloud);
		quoteCloud.setMstProductFamily(this);
		return quoteCloud;
	}

	public QuoteCloud removeQuoteCloud(QuoteCloud quoteCloud) {
		getQuoteClouds().remove(quoteCloud);
		quoteCloud.setMstProductFamily(null);
		return quoteCloud;
	}

	public Set<OrderCloud> getOrderClouds() {
		return this.orderClouds;
	}

	public void setOrderClouds(Set<OrderCloud> orderClouds) {
		this.orderClouds = orderClouds;
	}

	public OrderCloud addOrderCloud(OrderCloud orderCloud) {
		getOrderClouds().add(orderCloud);
		orderCloud.setMstProductFamily(this);
		return orderCloud;
	}

	public OrderCloud removeOrderCloud(OrderCloud orderCloud) {
		getOrderClouds().remove(orderCloud);
		orderCloud.setMstProductFamily(null);
		return orderCloud;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	
}
