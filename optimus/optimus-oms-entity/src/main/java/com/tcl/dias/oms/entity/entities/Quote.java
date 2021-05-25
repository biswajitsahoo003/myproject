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
@Table(name = "quote")
@NamedQuery(name = "Quote.findAll", query = "SELECT q FROM Quote q")
public class Quote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "effective_date")
	private Date effectiveDate;

	@Column(name = "quote_code")
	private String quoteCode;

	@Column(name = "ns_quote")
	private String nsQuote;
	
	@Column(name = "quote_status")
	private String quoteStatus;
	
	@Column(name = "is_customer_view")
	private Byte isCustomerView;

	@Column(name = "is_sales_view")
	private Byte isSalesView;

	private Byte status;

	@Column(name = "term_in_months")
	private Integer termInMonths;

	// bi-directional many-to-one association to Order
	@OneToMany(mappedBy = "quote")
	private Set<Order> orders;

	// bi-directional many-to-one association to Customer
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;

	// bi-directional many-to-one association to QuoteToLe
	@OneToMany(mappedBy = "quote")
	private Set<QuoteToLe> quoteToLes;

	@Column(name = "engagement_to_opportunity_id")
	private String engagementOptyId;
	
	@Column(name = "izosdwan_flavour")
	private String izosdwanFlavour;

	public String getIzosdwanFlavour() {
		return izosdwanFlavour;
	}

	public void setIzosdwanFlavour(String izosdwanFlavour) {
		this.izosdwanFlavour = izosdwanFlavour;
	}

	// bi-directional many-to-one association to QuoteTnc
	@OneToMany(mappedBy = "quote")
	private Set<QuoteTnc> quoteTncs;
	
	//bi-directional many-to-one association to QuoteIzosdwanCgwDetail
	@OneToMany(mappedBy="quote")
	private Set<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetails;
	
	@Column(name = "order_to_cash_order")
	private Byte orderToCashOrder;

	public Quote() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getQuoteCode() {
		return this.quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getTermInMonths() {
		return this.termInMonths;
	}

	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}

	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Order addOrder(Order order) {
		getOrders().add(order);
		order.setQuote(this);

		return order;
	}

	public Order removeOrder(Order order) {
		getOrders().remove(order);
		order.setQuote(null);

		return order;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<QuoteToLe> getQuoteToLes() {
		return this.quoteToLes;
	}

	public void setQuoteToLes(Set<QuoteToLe> quoteToLes) {
		this.quoteToLes = quoteToLes;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public String getNsQuote() {
		return nsQuote;
	}

	public void setNsQuote(String nsQuote) {
		this.nsQuote = nsQuote;
	}

	public Set<QuoteTnc> getQuoteTncs() {
		return quoteTncs;
	}

	public void setQuoteTncs(Set<QuoteTnc> quoteTncs) {
		this.quoteTncs = quoteTncs;
	}

	public QuoteToLe addQuoteToLe(QuoteToLe quoteToLe) {
		getQuoteToLes().add(quoteToLe);
		quoteToLe.setQuote(this);

		return quoteToLe;
	}

	public QuoteToLe removeQuoteToLe(QuoteToLe quoteToLe) {
		getQuoteToLes().remove(quoteToLe);
		quoteToLe.setQuote(null);

		return quoteToLe;
	}
	
	@OneToMany(mappedBy = "quote",fetch = FetchType.LAZY)
	private Set<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails;	
	

	public Set<QuoteIzosdwanByonUploadDetail> getQuoteIzosdwanByonUploadDetails() {
		return quoteIzosdwanByonUploadDetails;
	}

	public void setQuoteIzosdwanByonUploadDetails(Set<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails) {
		this.quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetails;
	}

	public String getQuoteStatus() {
		return quoteStatus;
	}

	public void setQuoteStatus(String quoteStatus) {
		this.quoteStatus = quoteStatus;
	}

	public Set<QuoteIzosdwanCgwDetail> getQuoteIzosdwanCgwDetails() {
		return quoteIzosdwanCgwDetails;
	}

	public void setQuoteIzosdwanCgwDetails(Set<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetails) {
		this.quoteIzosdwanCgwDetails = quoteIzosdwanCgwDetails;
	}

	public Byte getIsCustomerView() {
		return isCustomerView;
	}

	public void setIsCustomerView(Byte isCustomerView) {
		this.isCustomerView = isCustomerView;
	}

	public Byte getIsSalesView() {
		return isSalesView;
	}

	public void setIsSalesView(Byte isSalesView) {
		this.isSalesView = isSalesView;
	}

	public Byte getOrderToCashOrder() {
		return orderToCashOrder;
	}

	public void setOrderToCashOrder(Byte orderToCashOrder) {
		this.orderToCashOrder = orderToCashOrder;
	}
	
	

}