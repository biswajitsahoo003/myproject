package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 
 * This file contains the QuoteGsc.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_gsc")
@NamedQuery(name = "QuoteGsc.findAll", query = "SELECT q FROM QuoteGsc q")
public class QuoteGsc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "access_type", length = 100)
	private String accessType;

	private Double arc;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "created_time", nullable = false)
	private Timestamp createdTime;

	@Column(name = "image_url", length = 100)
	private String imageUrl;

	private Double mrc;

	@Column(length = 150)
	private String name;

	private Double nrc;

	@Column(name = "product_name", length = 150)
	private String productName;

	private Byte status;

	private Double tcv;

	// bi-directional many-to-one association to ProductSolution
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_solution_id")
	private ProductSolution productSolution;

	// bi-directional many-to-one association to QuoteToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_to_le_id")
	private QuoteToLe quoteToLe;

	// bi-directional many-to-one association to QuoteGscDetail
	@OneToMany(mappedBy = "quoteGsc")
	private Set<QuoteGscDetail> quoteGscDetails;

	// bi-directional many-to-one association to QuoteGscSla
	@OneToMany(mappedBy = "quoteGsc")
	private Set<QuoteGscSla> quoteGscSlas;

	public QuoteGsc() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessType() {
		return this.accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
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

	public Double getTcv() {
		return this.tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public ProductSolution getProductSolution() {
		return this.productSolution;
	}

	public void setProductSolution(ProductSolution productSolution) {
		this.productSolution = productSolution;
	}

	public QuoteToLe getQuoteToLe() {
		return this.quoteToLe;
	}

	public void setQuoteToLe(QuoteToLe quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

	public Set<QuoteGscDetail> getQuoteGscDetails() {
		return this.quoteGscDetails;
	}

	public void setQuoteGscDetails(Set<QuoteGscDetail> quoteGscDetails) {
		this.quoteGscDetails = quoteGscDetails;
	}

	public QuoteGscDetail addQuoteGscDetail(QuoteGscDetail quoteGscDetail) {
		getQuoteGscDetails().add(quoteGscDetail);
		quoteGscDetail.setQuoteGsc(this);

		return quoteGscDetail;
	}

	public QuoteGscDetail removeQuoteGscDetail(QuoteGscDetail quoteGscDetail) {
		getQuoteGscDetails().remove(quoteGscDetail);
		quoteGscDetail.setQuoteGsc(null);

		return quoteGscDetail;
	}

	public Set<QuoteGscSla> getQuoteGscSlas() {
		return this.quoteGscSlas;
	}

	public void setQuoteGscSlas(Set<QuoteGscSla> quoteGscSlas) {
		this.quoteGscSlas = quoteGscSlas;
	}

	public QuoteGscSla addQuoteGscSla(QuoteGscSla quoteGscSla) {
		getQuoteGscSlas().add(quoteGscSla);
		quoteGscSla.setQuoteGsc(this);

		return quoteGscSla;
	}

	public QuoteGscSla removeQuoteGscSla(QuoteGscSla quoteGscSla) {
		getQuoteGscSlas().remove(quoteGscSla);
		quoteGscSla.setQuoteGsc(null);

		return quoteGscSla;
	}

}