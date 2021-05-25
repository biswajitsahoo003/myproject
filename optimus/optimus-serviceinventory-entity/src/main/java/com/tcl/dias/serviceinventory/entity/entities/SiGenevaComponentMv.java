package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class of si_geneva_component_mv database table.
 * @author archchan
 */
@Entity
@Table(name="si_geneva_component_mv")
@NamedQuery(name="SiGenevaComponentMv.findAll", query="SELECT s FROM SiGenevaComponentMv s")
public class SiGenevaComponentMv implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="ACCOUNT_STATUS")
	private String accountStatus;

	private String arc;

	@Column(name="BILL_START_DATE")
	private String billStartDate;

	@Column(name="BILLING_ACCOUNT_NUMBER")
	private String billingAccountNumber;

	@Column(name="BILLING_ENTITY")
	private String billingEntity;

	@Column(name="BILLING_FREQUENCY")
	private String billingFrequency;

	@Column(name="CONTRACT_TERM")
	private String contractTerm;

	@Column(name="COPF_ID")
	private String copfId;

	@Column(name="CURRENCY")
	private String currency;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="etl_load_dt")
	private Date etlLoadDt;

	private String nrc;

	@Column(name="ONNET_ARC")
	private String onnetArc;

	@Column(name="PAYMENT_TYPE")
	private String paymentType;

	@Column(name="PRICE_REV_DATE")
	private String priceRevDate;

	private String prodquantity;

	@Lob
	@Column(name="PRODUCT_FLAVOUR")
	private String productFlavour;

	@Column(name="PRODUCT_NAME")
	private String productName;

	@Column(name="PRODUCT_SEQ")
	private String productSeq;

	@Column(name="SERVICE_ID")
	private String serviceId;

	@Lob
	@Column(name="SERVICE_TYPE")
	private String serviceType;

	@Lob
	@Column(name="src_system")
	private String srcSystem;

	private String status;

	@Column(name="TIGER_ID")
	private String tigerId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	public SiGenevaComponentMv() {
	}

	public String getAccountStatus() {
		return this.accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getArc() {
		return this.arc;
	}

	public void setArc(String arc) {
		this.arc = arc;
	}

	public String getBillStartDate() {
		return this.billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getBillingAccountNumber() {
		return this.billingAccountNumber;
	}

	public void setBillingAccountNumber(String billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}

	public String getBillingEntity() {
		return this.billingEntity;
	}

	public void setBillingEntity(String billingEntity) {
		this.billingEntity = billingEntity;
	}

	public String getBillingFrequency() {
		return this.billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public String getContractTerm() {
		return this.contractTerm;
	}

	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}

	public String getCopfId() {
		return this.copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getEtlLoadDt() {
		return this.etlLoadDt;
	}

	public void setEtlLoadDt(Date etlLoadDt) {
		this.etlLoadDt = etlLoadDt;
	}

	public String getNrc() {
		return this.nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getOnnetArc() {
		return this.onnetArc;
	}

	public void setOnnetArc(String onnetArc) {
		this.onnetArc = onnetArc;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPriceRevDate() {
		return this.priceRevDate;
	}

	public void setPriceRevDate(String priceRevDate) {
		this.priceRevDate = priceRevDate;
	}

	public String getProdquantity() {
		return this.prodquantity;
	}

	public void setProdquantity(String prodquantity) {
		this.prodquantity = prodquantity;
	}

	public String getProductFlavour() {
		return this.productFlavour;
	}

	public void setProductFlavour(String productFlavour) {
		this.productFlavour = productFlavour;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSeq() {
		return this.productSeq;
	}

	public void setProductSeq(String productSeq) {
		this.productSeq = productSeq;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSrcSystem() {
		return this.srcSystem;
	}

	public void setSrcSystem(String srcSystem) {
		this.srcSystem = srcSystem;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTigerId() {
		return this.tigerId;
	}

	public void setTigerId(String tigerId) {
		this.tigerId = tigerId;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

}
