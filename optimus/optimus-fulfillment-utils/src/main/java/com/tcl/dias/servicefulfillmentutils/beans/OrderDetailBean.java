package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDetail Bean Class
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class OrderDetailBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer orderId;
    private String accountId;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String aluCustomerId;
    private Boolean asdOpptyFlag;
    private String city;
    private String copfId;
    private String country;
    private String customerCategory;
    private String customerContact;
    private String customerEmail;
    private String customerId;
    private String customerName;
    private String customerPhoneno;
    private String customerType;
    private Integer custCuId;
    private String endCustomerName;
    private Timestamp endDate;
    private String groupId;
    private Timestamp lastModifiedDate;
    private String location;
    private String modifiedBy;
    private String optyBidCategory;
    private String orderCategory;
    private String orderStatus;
    private String orderType;
    private String orderUuid;
    private Timestamp originatorDate;
    private String originatorName;
    private String pincode;
    private String samCustomerDescription;
    private String sfdcOpptyId;
    private Timestamp startDate;
    private String state;
    private boolean isEdited;
    private String customerCrnId;
    private String projectName;
    private String extOrderrefId;

    private List<ServiceDetailBean> serviceDetailBeans;

    public List<ServiceDetailBean> getServiceDetailBeans() {

        if (serviceDetailBeans == null) {
            serviceDetailBeans = new ArrayList<>();

        }
        return serviceDetailBeans;
    }

    public void setServiceDetailBeans(List<ServiceDetailBean> serviceDetailBeans) {
        this.serviceDetailBeans = serviceDetailBeans;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAluCustomerId() {
        return aluCustomerId;
    }

    public void setAluCustomerId(String aluCustomerId) {
        this.aluCustomerId = aluCustomerId;
    }

    public Boolean getAsdOpptyFlag() {
        return asdOpptyFlag;
    }

    public void setAsdOpptyFlag(Boolean asdOpptyFlag) {
        this.asdOpptyFlag = asdOpptyFlag;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCopfId() {
        return copfId;
    }

    public void setCopfId(String copfId) {
        this.copfId = copfId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneno() {
        return customerPhoneno;
    }

    public void setCustomerPhoneno(String customerPhoneno) {
        this.customerPhoneno = customerPhoneno;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getEndCustomerName() {
        return endCustomerName;
    }

    public void setEndCustomerName(String endCustomerName) {
        this.endCustomerName = endCustomerName;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public Timestamp getOriginatorDate() {
        return originatorDate;
    }

    public void setOriginatorDate(Timestamp originatorDate) {
        this.originatorDate = originatorDate;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getSamCustomerDescription() {
        return samCustomerDescription;
    }

    public void setSamCustomerDescription(String samCustomerDescription) {
        this.samCustomerDescription = samCustomerDescription;
    }

    public String getSfdcOpptyId() {
        return sfdcOpptyId;
    }

    public void setSfdcOpptyId(String sfdcOpptyId) {
        this.sfdcOpptyId = sfdcOpptyId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCustCuId() { return custCuId; }

    public void setCustCuId(Integer custCuId) { this.custCuId = custCuId; }

    public String getOptyBidCategory() { return optyBidCategory; }

    public void setOptyBidCategory(String optyBidCategory) { this.optyBidCategory = optyBidCategory; }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public String getCustomerCrnId() {
        return customerCrnId;
    }

    public void setCustomerCrnId(String customerCrnId) {
        this.customerCrnId = customerCrnId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getExtOrderrefId() {
        return extOrderrefId;
    }

    public void setExtOrderrefId(String extOrderrefId) {
        this.extOrderrefId = extOrderrefId;
    }
}