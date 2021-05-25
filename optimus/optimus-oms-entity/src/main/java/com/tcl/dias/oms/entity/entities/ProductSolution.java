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
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "product_solutions")
@NamedQuery(name = "ProductSolution.findAll", query = "SELECT p FROM ProductSolution p")
public class ProductSolution implements Serializable {
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

	// bi-directional many-to-one association to MstProductOffering
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_offering_id")
	private MstProductOffering mstProductOffering;

	// bi-directional many-to-one association to QuoteToLeProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_le_product_family_id")
	private QuoteToLeProductFamily quoteToLeProductFamily;

	// bi-directional many-to-one association to QuoteIllSite
	@OneToMany(mappedBy = "productSolution")
	private Set<QuoteIllSite> quoteIllSites;
	
	// bi-directional many-to-one association to QuoteIllSite
	@OneToMany(mappedBy = "productSolution")
	private Set<QuoteIzosdwanSite> quoteIzoSdwanSites;

	public Set<QuoteIzosdwanSite> getQuoteIzoSdwanSites() {
		return quoteIzoSdwanSites;
	}

	public void setQuoteIzoSdwanSites(Set<QuoteIzosdwanSite> quoteIzoSdwanSites) {
		this.quoteIzoSdwanSites = quoteIzoSdwanSites;
	}

	@Column(name = "is_bundle")
	private Character isBundle;

	@Column(name = "bundle_solution_id")
	private Integer bundleSolutionId;

	public ProductSolution() {
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

	public MstProductOffering getMstProductOffering() {
		return this.mstProductOffering;
	}

	public void setMstProductOffering(MstProductOffering mstProductOffering) {
		this.mstProductOffering = mstProductOffering;
	}

	public QuoteToLeProductFamily getQuoteToLeProductFamily() {
		return this.quoteToLeProductFamily;
	}

	public void setQuoteToLeProductFamily(QuoteToLeProductFamily quoteToLeProductFamily) {
		this.quoteToLeProductFamily = quoteToLeProductFamily;
	}

	public Set<QuoteIllSite> getQuoteIllSites() {
		return this.quoteIllSites;
	}

	public void setQuoteIllSites(Set<QuoteIllSite> quoteIllSites) {
		this.quoteIllSites = quoteIllSites;
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

	public QuoteIllSite addQuoteIllSite(QuoteIllSite quoteIllSite) {
		getQuoteIllSites().add(quoteIllSite);
		quoteIllSite.setProductSolution(this);

		return quoteIllSite;
	}

	public QuoteIllSite removeQuoteIllSite(QuoteIllSite quoteIllSite) {
		getQuoteIllSites().remove(quoteIllSite);
		quoteIllSite.setProductSolution(null);

		return quoteIllSite;
	}

}