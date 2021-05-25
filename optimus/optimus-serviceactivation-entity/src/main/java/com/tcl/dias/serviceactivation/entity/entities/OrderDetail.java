package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * OrderDetail Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="order_details")
@NamedQuery(name="OrderDetail.findAll", query="SELECT o FROM OrderDetail o")
public class OrderDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="order_id")
	private Integer orderId;

	@Column(name="account_id")
	private String accountId;
	
	@Column(name="ext_orderref_id")
	private String extOrderrefId;

	@Column(name="address_line1")
	private String addressLine1;

	@Column(name="address_line2")
	private String addressLine2;

	@Column(name="address_line3")
	private String addressLine3;

	@Column(name="alu_customer_id")
	private String aluCustomerId;

	@Column(name="asd_oppty_flag")
	private Byte asdOpptyFlag;

	private String city;

	@Column(name="copf_id")
	private String copfId;

	private String country;

	@Column(name="cust_cu_id")
	private Integer custCuId;

	@Column(name="customer_category")
	private String customerCategory;

	@Column(name="customer_contact")
	private String customerContact;

	@Column(name="customer_crn_id")
	private String customerCrnId;

	@Column(name="customer_email")
	private String customerEmail;

	@Column(name="customer_name")
	private String customerName;

	@Column(name="customer_phoneno")
	private String customerPhoneno;

	@Column(name="customer_type")
	private String customerType;

	@Column(name="end_customer_name")
	private String endCustomerName;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="group_id")
	private String groupId;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	private String location;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="order_category")
	private String orderCategory;
	
	/*@Column(name = "order_sub_category")
	private String orderSubCategory;
*/
	@Column(name="order_status")
	private String orderStatus;

	@Column(name="order_type")
	private String orderType;

	@Column(name="order_uuid")
	private String orderUuid;

	@Column(name="originator_date")
	private Timestamp originatorDate;

	@Column(name="originator_name")
	private String originatorName;

	private String pincode;

	@Column(name="sam_customer_description")
	private String samCustomerDescription;

	@Column(name="sfdc_oppty_id")
	private String sfdcOpptyId;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="opty_bid_category")
	private String optyBidCategory;
	
	
	@Column(name="project_name")
	private String projectName;
	
	

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	private String state;

	//bi-directional many-to-one association to ServiceDetail
	@OneToMany(mappedBy="orderDetail",fetch=FetchType.LAZY)
	private Set<ServiceDetail> serviceDetails;

	public OrderDetail() {
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAddressLine1() {
		return this.addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return this.addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return this.addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getAluCustomerId() {
		return this.aluCustomerId;
	}

	public void setAluCustomerId(String aluCustomerId) {
		this.aluCustomerId = aluCustomerId;
	}

	public Byte getAsdOpptyFlag() {
		return this.asdOpptyFlag;
	}

	public void setAsdOpptyFlag(Byte asdOpptyFlag) {
		this.asdOpptyFlag = asdOpptyFlag;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCopfId() {
		return this.copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getCustCuId() {
		return this.custCuId;
	}

	public void setCustCuId(Integer custCuId) {
		this.custCuId = custCuId;
	}

	public String getCustomerCategory() {
		return this.customerCategory;
	}

	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}

	public String getCustomerContact() {
		return this.customerContact;
	}

	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	public String getCustomerCrnId() {
		return this.customerCrnId;
	}

	public void setCustomerCrnId(String customerCrnId) {
		this.customerCrnId = customerCrnId;
	}

	public String getCustomerEmail() {
		return this.customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhoneno() {
		return this.customerPhoneno;
	}

	public void setCustomerPhoneno(String customerPhoneno) {
		this.customerPhoneno = customerPhoneno;
	}

	public String getCustomerType() {
		return this.customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getEndCustomerName() {
		return this.endCustomerName;
	}

	public void setEndCustomerName(String endCustomerName) {
		this.endCustomerName = endCustomerName;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOrderCategory() {
		return this.orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderUuid() {
		return this.orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public Timestamp getOriginatorDate() {
		return this.originatorDate;
	}

	public void setOriginatorDate(Timestamp originatorDate) {
		this.originatorDate = originatorDate;
	}

	public String getOriginatorName() {
		return this.originatorName;
	}

	public void setOriginatorName(String originatorName) {
		this.originatorName = originatorName;
	}

	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getSamCustomerDescription() {
		return this.samCustomerDescription;
	}

	public void setSamCustomerDescription(String samCustomerDescription) {
		this.samCustomerDescription = samCustomerDescription;
	}

	public String getSfdcOpptyId() {
		return this.sfdcOpptyId;
	}

	public void setSfdcOpptyId(String sfdcOpptyId) {
		this.sfdcOpptyId = sfdcOpptyId;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set<ServiceDetail> getServiceDetails() {
		return this.serviceDetails;
	}

	public void setServiceDetails(Set<ServiceDetail> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public ServiceDetail addServiceDetail(ServiceDetail serviceDetail) {
		getServiceDetails().add(serviceDetail);
		serviceDetail.setOrderDetail(this);

		return serviceDetail;
	}

	public ServiceDetail removeServiceDetail(ServiceDetail serviceDetail) {
		getServiceDetails().remove(serviceDetail);
		serviceDetail.setOrderDetail(null);

		return serviceDetail;
	}

	public String getExtOrderrefId() {
		return extOrderrefId;
	}

	public void setExtOrderrefId(String extOrderrefId) {
		this.extOrderrefId = extOrderrefId;
	}

	public String getOptyBidCategory() { return optyBidCategory; }

	public void setOptyBidCategory(String optyBidCategory) { this.optyBidCategory = optyBidCategory; }

	/*public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}*/
	
}