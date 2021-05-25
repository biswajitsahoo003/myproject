package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Entity
@Table(name = "order_site_billing_details")
@NamedQuery(name = "OrderSiteBillingDetails.findAll", query = "SELECT m FROM OrderSiteBillingDetails m")
public class OrderSiteBillingDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private OrderIllSite orderIllSite;
	
	@Column(name = "order_code")
	private String orderCode;
	
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "cus_le_id")
	private Integer cusLeId;
	
	@Column(name = "etc_cus_billing_info_id")
	private Integer etcCusBillingInfoId;
	
	@Column(name = "contact_id")
	private String contactId;
	
	@Column(name = "gst_no")
	private String gstNo;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	@Column(name = "product_name")
	private String productName;

	@Column(name = "site_type")
	private String siteType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_id")
	private OrderNplLink orderNplLink;
	
	@Column(name = "city")
	private String city;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OrderIllSite getOrderIllSite() {
		return orderIllSite;
	}

	public void setOrderIllSite(OrderIllSite orderIllSite) {
		this.orderIllSite = orderIllSite;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getCusLeId() {
		return cusLeId;
	}

	public void setCusLeId(Integer cusLeId) {
		this.cusLeId = cusLeId;
	}

	public Integer getEtcCusBillingInfoId() {
		return etcCusBillingInfoId;
	}

	public void setEtcCusBillingInfoId(Integer etcCusBillingInfoId) {
		this.etcCusBillingInfoId = etcCusBillingInfoId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public OrderNplLink getOrderNplLink() {
		return orderNplLink;
	}

	public void setOrderNplLink(OrderNplLink orderNplLink) {
		this.orderNplLink = orderNplLink;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
	
}
