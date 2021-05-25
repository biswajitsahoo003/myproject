package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillment.entity.entities.MstVendor;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited Bean class for Vendor Details
 */
public class VendorDetailsRequest extends TaskDetailsBaseBean {

	private String name;
	private String contactNumber;
	private String emailId;
	private Integer mstVendorId;
	private String vendorType;
	private String vendorId;
	private String circle;
	private String contactName;
	private String corrId;

	private String ospFeContactNumber;
	private String ospFeName;
	private String ospFeEmailId;

	private String ospSecondaryFeContactNumber;
	private String ospSecondaryFeName;
	private String ospSecondaryFeEmailId;
	private String vendorPoForCpeRecovery;

	public String getOspFeContactNumber() {
		return ospFeContactNumber;
	}

	public void setOspFeContactNumber(String ospFeContactNumber) {
		this.ospFeContactNumber = ospFeContactNumber;
	}

	public String getOspFeName() {
		return ospFeName;
	}

	public void setOspFeName(String ospFeName) {
		this.ospFeName = ospFeName;
	}

	public String getOspFeEmailId() {
		return ospFeEmailId;
	}

	public void setOspFeEmailId(String ospFeEmailId) {
		this.ospFeEmailId = ospFeEmailId;
	}

	public String getVendorType() {
		return vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	public VendorDetailsRequest() {

	}

	public static VendorDetailsRequest mapToBean(MstVendor vendor) {
		VendorDetailsRequest vendorDetailsBean = new VendorDetailsRequest();
		vendorDetailsBean.setName(vendor.getName());
		vendorDetailsBean.setContactNumber(vendor.getPhoneNumber());
		vendorDetailsBean.setEmailId(vendor.getEmail());
		vendorDetailsBean.setMstVendorId(vendor.getId());
		return vendorDetailsBean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getMstVendorId() {
		return mstVendorId;
	}

	public void setMstVendorId(Integer mstVendorId) {
		this.mstVendorId = mstVendorId;
	}

	public String getOspSecondaryFeContactNumber() {
		return ospSecondaryFeContactNumber;
	}

	public void setOspSecondaryFeContactNumber(String ospSecondaryFeContactNumber) {
		this.ospSecondaryFeContactNumber = ospSecondaryFeContactNumber;
	}

	public String getOspSecondaryFeName() {
		return ospSecondaryFeName;
	}

	public void setOspSecondaryFeName(String ospSecondaryFeName) {
		this.ospSecondaryFeName = ospSecondaryFeName;
	}

	public String getOspSecondaryFeEmailId() {
		return ospSecondaryFeEmailId;
	}

	public void setOspSecondaryFeEmailId(String ospSecondaryFeEmailId) {
		this.ospSecondaryFeEmailId = ospSecondaryFeEmailId;
	}
	
	
	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCorrId() {
		return corrId;
	}

	public void setCorrId(String corrId) {
		this.corrId = corrId;
	}

	@Override
	public String toString() {
		return "VendorDetailsBean{" + "name='" + name + '\'' + ", contactNumber='" + contactNumber + '\''
				+ ", emailId='" + emailId + '\'' + ", mstVendorId=" + mstVendorId + '}';
	}

	public String getVendorPoForCpeRecovery() {
		return vendorPoForCpeRecovery;
	}

	public void setVendorPoForCpeRecovery(String vendorPoForCpeRecovery) {
		this.vendorPoForCpeRecovery = vendorPoForCpeRecovery;
	}
   
}
