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
 * The persistent class for the order_ucaas database table.
 *
 * @author S Syed Ali.
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "order_ucaas")
@NamedQuery(name = "OrderUcaas.findAll", query = "SELECT q FROM OrderUcaas q")
public class OrderUcaas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	// bi-directional many-to-one association to OrderToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_to_le_id")
	private OrderToLe orderToLe;

	// bi-directional many-to-one association to orderProductSolution
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_product_solution_id")
	private OrderProductSolution orderProductSolution;

	@Column(name = "is_config")
	private Byte isConfig;

	@Column(name = "deal_id")
	private Integer dealId;

	private String name;

	private String description;

	private Integer quantity;

	private Double arc;

	@Column(name = "unit_mrc")
	private Double unitMrc;

	private Double mrc;

	private Double nrc;

	@Column(name = "unit_nrc")
	private Double unitNrc;

	private Double tcv;

	@Column(name = "cisco_unit_list_price")
	private Double ciscoUnitListPrice;

	@Column(name = "cisco_discnt_prct")
	private Double ciscoDiscountPercent;

	@Column(name = "cisco_unit_net_price")
	private Double ciscoUnitNetPrice;

	private String deal_status;

	private String deal_message;

	@Column(name = "deal_attributes")
	private String dealAttributes;

	@Column(name = "license_provider")
	private String licenseProvider;

	@Column(name = "primary_region")
	private String primaryRegion;

	@Column(name = "cug_required")
	private Byte cugRequired;

	@Column(name = "audio_model")
	private String audioModel;

	@Column(name = "payment_model")
	private String paymentModel;

	@Column(name = "audio_type")
	private String audioType;

	@Column(name = "gvpn_mode")
	private String gvpnMode;

	@Column(name = "dial_in")
	private Byte dialIn;

	@Column(name = "dial_out")
	private Byte dialOut;

	@Column(name = "dial_back")
	private Byte dialBack;

	@Column(name = "is_lns")
	private Byte isLns;

	@Column(name = "is_itfs")
	private Byte isItfs;

	@Column(name = "is_outbound")
	private Byte isOutbound;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "quote_version")
	private Integer quoteVersion;

	private Byte status;

	private String uom;

	@Column(name = "hsn_code")
	private String hsnCode;

	@Column(name = "endpoint_location_id")
	private Integer endpointLocationId;

	@Column(name = "contract_type")
	private String contractType;

	@Column(name = "endpoint_management_type")
	private String endpointManagementType;

	@Column(name = "is_updated")
	private Byte isUpdated;

	public OrderUcaas() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OrderToLe getOrderToLe() {
		return orderToLe;
	}

	public void setOrderToLe(OrderToLe orderToLe) {
		this.orderToLe = orderToLe;
	}

	public Byte getIsConfig() {
		return isConfig;
	}

	public void setIsConfig(Byte isConfig) {
		this.isConfig = isConfig;
	}

	public String getName() {
		return name;
	}

	public Integer getDealId() {
		return dealId;
	}

	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public String getLicenseProvider() {
		return licenseProvider;
	}

	public void setLicenseProvider(String licenseProvider) {
		this.licenseProvider = licenseProvider;
	}

	public String getPrimaryRegion() {
		return primaryRegion;
	}

	public void setPrimaryRegion(String primaryRegion) {
		this.primaryRegion = primaryRegion;
	}

	public Byte getCugRequired() {
		return cugRequired;
	}

	public void setCugRequired(Byte cugRequired) {
		this.cugRequired = cugRequired;
	}

	public String getAudioModel() {
		return audioModel;
	}

	public void setAudioModel(String audioModel) {
		this.audioModel = audioModel;
	}

	public String getPaymentModel() {
		return paymentModel;
	}

	public void setPaymentModel(String paymentModel) {
		this.paymentModel = paymentModel;
	}

	public String getAudioType() {
		return audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	public String getGvpnMode() {
		return gvpnMode;
	}

	public void setGvpnMode(String gvpnMode) {
		this.gvpnMode = gvpnMode;
	}

	public Byte getDialIn() {
		return dialIn;
	}

	public void setDialIn(Byte dialIn) {
		this.dialIn = dialIn;
	}

	public Byte getDialOut() {
		return dialOut;
	}

	public void setDialOut(Byte dialOut) {
		this.dialOut = dialOut;
	}

	public Byte getDialBack() {
		return dialBack;
	}

	public void setDialBack(Byte dialBack) {
		this.dialBack = dialBack;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getQuoteVersion() {
		return quoteVersion;
	}

	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public OrderProductSolution getProductSolutionId() {
		return orderProductSolution;
	}

	public void setProductSolutionId(OrderProductSolution orderProductSolution) {
		this.orderProductSolution = orderProductSolution;
	}

	public String getDeal_status() {
		return deal_status;
	}

	public void setDeal_status(String deal_status) {
		this.deal_status = deal_status;
	}

	public String getDeal_message() {
		return deal_message;
	}

	public void setDeal_message(String deal_message) {
		this.deal_message = deal_message;
	}

	public String getDealAttributes() {
		return dealAttributes;
	}

	public void setDealAttributes(String dealAttributes) {
		this.dealAttributes = dealAttributes;
	}

	public Byte getIsLns() {
		return isLns;
	}

	public void setIsLns(Byte lns) {
		isLns = lns;
	}

	public Byte getIsItfs() {
		return isItfs;
	}

	public void setIsItfs(Byte itfs) {
		isItfs = itfs;
	}

	public Byte getIsOutbound() {
		return isOutbound;
	}

	public void setIsOutbound(Byte outbound) {
		isOutbound = outbound;
	}

	public Double getUnitMrc() {
		return unitMrc;
	}

	public void setUnitMrc(Double unitMrc) {
		this.unitMrc = unitMrc;
	}

	public Double getUnitNrc() {
		return unitNrc;
	}

	public void setUnitNrc(Double unitNrc) {
		this.unitNrc = unitNrc;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Double getCiscoUnitListPrice() {
		return ciscoUnitListPrice;
	}

	public void setCiscoUnitListPrice(Double ciscoUnitListPrice) {
		this.ciscoUnitListPrice = ciscoUnitListPrice;
	}

	public Double getCiscoDiscountPercent() {
		return ciscoDiscountPercent;
	}

	public void setCiscoDiscountPercent(Double ciscoDiscountPercent) {
		this.ciscoDiscountPercent = ciscoDiscountPercent;
	}

	public Double getCiscoUnitNetPrice() {
		return ciscoUnitNetPrice;
	}

	public void setCiscoUnitNetPrice(Double ciscoUnitNetPrice) {
		this.ciscoUnitNetPrice = ciscoUnitNetPrice;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Integer getEndpointLocationId() {
		return endpointLocationId;
	}

	public void setEndpointLocationId(Integer endpointLocationId) {
		this.endpointLocationId = endpointLocationId;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getEndpointManagementType() {
		return endpointManagementType;
	}

	public void setEndpointManagementType(String endpointManagementType) {
		this.endpointManagementType = endpointManagementType;
	}

	public Byte getIsUpdated() {
		return isUpdated;
	}

	public void setIsUpdated(Byte isUpdated) {
		this.isUpdated = isUpdated;
	}
}
