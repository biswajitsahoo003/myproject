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
import javax.persistence.OneToOne;
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
@Table(name = "orders")
@NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "effective_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDate;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "is_demo")
	private Byte isDemo;

	@Column(name = "order_code")
	private String orderCode;

	@Column(name = "is_order_to_cash_enabled")
	private Byte isOrderToCashEnabled;

	private String stage;

	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	private Byte status;

	@Column(name = "term_in_months")
	private Integer termInMonths;

	// bi-directional many-to-one association to OrderToLe
	@OneToMany(mappedBy = "order")
	private Set<OrderToLe> orderToLes;

	// bi-directional many-to-one association to Quote
	@ManyToOne(fetch = FetchType.LAZY)
	private Quote quote;

	// bi-directional many-to-one association to Customer
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cof_oms_attachment_id")
	private OmsAttachment omsAttachment;

	@Column(name = "quote_created_by")
	private Integer quoteCreatedBy;

	@Column(name = "engagement_to_opportunity_id")
	private String engagementOptyId;

	@Column(name = "order_to_cash_order")
	private Byte orderToCashOrder;
	
	// bi-directional many-to-one association to QuoteIzosdwanCgwDetail
	@OneToMany(mappedBy = "order")
	private Set<OrderIzosdwanCgwDetail> orderIzosdwanCgwDetails;

	@OneToMany(mappedBy = "order")
	private Set<OrderIzosdwanAttributeValue> orderIzosdwanAttributeValue;
		
	public Set<OrderIzosdwanCgwDetail> getOrderIzosdwanCgwDetails() {
		return orderIzosdwanCgwDetails;
	}

	public void setOrderIzosdwanCgwDetails(Set<OrderIzosdwanCgwDetail> orderIzosdwanCgwDetails) {
		this.orderIzosdwanCgwDetails = orderIzosdwanCgwDetails;
	}

	public Order() {
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

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Byte getIsDemo() {
		return this.isDemo;
	}

	public void setIsDemo(Byte isDemo) {
		this.isDemo = isDemo;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getStage() {
		return this.stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	public Set<OrderToLe> getOrderToLes() {
		return this.orderToLes;
	}

	public void setOrderToLes(Set<OrderToLe> orderToLes) {
		this.orderToLes = orderToLes;
	}

	public OrderToLe addOrderToLe(OrderToLe orderToLe) {
		getOrderToLes().add(orderToLe);
		orderToLe.setOrder(this);

		return orderToLe;
	}

	public OrderToLe removeOrderToLe(OrderToLe orderToLe) {
		getOrderToLes().remove(orderToLe);
		orderToLe.setOrder(null);

		return orderToLe;
	}

	public Quote getQuote() {
		return this.quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the omsAttachment
	 */
	public OmsAttachment getOmsAttachment() {
		return omsAttachment;
	}

	/**
	 * @param omsAttachment the omsAttachment to set
	 */
	public void setOmsAttachment(OmsAttachment omsAttachment) {
		this.omsAttachment = omsAttachment;
	}

	/**
	 * @return the quoteCreatedBy
	 */
	public Integer getQuoteCreatedBy() {
		return quoteCreatedBy;
	}

	/**
	 * @param quoteCreatedBy the quoteCreatedBy to set
	 */
	public void setQuoteCreatedBy(Integer quoteCreatedBy) {
		this.quoteCreatedBy = quoteCreatedBy;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public Byte getIsOrderToCashEnabled() {
		return isOrderToCashEnabled;
	}

	public void setIsOrderToCashEnabled(Byte isOrderToCashEnabled) {
		this.isOrderToCashEnabled = isOrderToCashEnabled;
	}

	public Byte getOrderToCashOrder() {
		return orderToCashOrder;
	}

	public void setOrderToCashOrder(Byte orderToCashOrder) {
		this.orderToCashOrder = orderToCashOrder;
	}

	public Set<OrderIzosdwanAttributeValue> getOrderIzosdwanAttributeValue() {
		return orderIzosdwanAttributeValue;
	}

	public void setOrderIzosdwanAttributeValue(Set<OrderIzosdwanAttributeValue> orderIzosdwanAttributeValue) {
		this.orderIzosdwanAttributeValue = orderIzosdwanAttributeValue;
	}
}