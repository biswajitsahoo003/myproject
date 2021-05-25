package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetails;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Internal Cabling details POJO
 * 
 * @author arjayapa
 */

public class InternalCablingDetails extends TaskDetailsBaseBean {
	
	private String workOrderNumber;
	private String workOrderRequired;
	
	public String getWorkOrderRequired() {
		return workOrderRequired;
	}

	public void setWorkOrderRequired(String workOrderRequired) {
		this.workOrderRequired = workOrderRequired;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String workOrderDate;
	
	private List<AttachmentIdBean> documentIds;

	private String vendorId;
	private String vendorName;
	private String vendorAddress;
	private String vendorEmailId;
	private String scopeofWork;
	private String remark;
	
	private String cablingManagedByCustomer;
	private String cablingRequiredAtPop;
	private String cableSwappingRequired;

	private String cablingBy;

	private String testingBy;

	private String cableSwappingByCustomer;
	private String cableSwappingBy;

	
	private List<FieldEngineerDetails> feDetails;
	
	
	
	
	

	/**
	 * @return the cableSwappingBy
	 */
	public String getCableSwappingBy() {
		return cableSwappingBy;
	}

	/**
	 * @param cableSwappingBy the cableSwappingBy to set
	 */
	public void setCableSwappingBy(String cableSwappingBy) {
		this.cableSwappingBy = cableSwappingBy;
	}

	/**
	 * @return the feDetails
	 */
	public List<FieldEngineerDetails> getFeDetails() {
		return feDetails;
	}

	/**
	 * @param feDetails the feDetails to set
	 */
	public void setFeDetails(List<FieldEngineerDetails> feDetails) {
		this.feDetails = feDetails;
	}

	/**
	 * @return the cablingBy
	 */
	public String getCablingBy() {
		return cablingBy;
	}

	/**
	 * @param cablingBy the cablingBy to set
	 */
	public void setCablingBy(String cablingBy) {
		this.cablingBy = cablingBy;
	}

	/**
	 * @return the testingBy
	 */
	public String getTestingBy() {
		return testingBy;
	}

	/**
	 * @param testingBy the testingBy to set
	 */
	public void setTestingBy(String testingBy) {
		this.testingBy = testingBy;
	}

	/**
	 * @return the cableSwappingByCustomer
	 */
	public String getCableSwappingByCustomer() {
		return cableSwappingByCustomer;
	}

	/**
	 * @param cableSwappingByCustomer the cableSwappingByCustomer to set
	 */
	public void setCableSwappingByCustomer(String cableSwappingByCustomer) {
		this.cableSwappingByCustomer = cableSwappingByCustomer;
	}

	public String getScopeofWork() {
		return scopeofWork;
	}

	public void setScopeofWork(String scopeofWork) {
		this.scopeofWork = scopeofWork;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public String getWorkOrderDate() {
		return workOrderDate;
	}

	public void setWorkOrderDate(String workOrderDate) {
		this.workOrderDate = workOrderDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getVendorEmailId() {
		return vendorEmailId;
	}

	public void setVendorEmailId(String vendorEmailId) {
		this.vendorEmailId = vendorEmailId;
	}

	public String getRemark() { return remark; }

	public void setRemark(String remark) { this.remark = remark; }

	public String getCablingManagedByCustomer() {
		return cablingManagedByCustomer;
	}

	public void setCablingManagedByCustomer(String cablingManagedByCustomer) {
		this.cablingManagedByCustomer = cablingManagedByCustomer;
	}

	public String getCableSwappingRequired() {
		return cableSwappingRequired;
	}

	public void setCableSwappingRequired(String cableSwappingRequired) {
		this.cableSwappingRequired = cableSwappingRequired;
	}

	public String getCablingRequiredAtPop() {
		return cablingRequiredAtPop;
	}

	public void setCablingRequiredAtPop(String cablingRequiredAtPop) {
		this.cablingRequiredAtPop = cablingRequiredAtPop;
	}
	
}
