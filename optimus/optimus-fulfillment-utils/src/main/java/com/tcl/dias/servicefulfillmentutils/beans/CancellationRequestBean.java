/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * @author vivek
 *
 */
public class CancellationRequestBean extends BaseRequest {

	private Integer serviceId;
	private List<AttachmentIdBean> documentIds;
	private String customerRequestorDate;
	private String cancellationReason;
	private String retainExistingNwresource;
	private String cancellationInitiatedBy;
	private String pmEmailId;
	private String salesSupportEmailId;
	private String salesPersonEmailId;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getCustomerRequestorDate() {
		return customerRequestorDate;
	}

	public void setCustomerRequestorDate(String customerRequestorDate) {
		this.customerRequestorDate = customerRequestorDate;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}
	public String getRetainExistingNwresource() {
		return retainExistingNwresource;
	}

	public void setRetainExistingNwresource(String retainExistingNwresource) {
		this.retainExistingNwresource = retainExistingNwresource;
	}

	public String getCancellationInitiatedBy() {
		return cancellationInitiatedBy;
	}

	public void setCancellationInitiatedBy(String cancellationInitiatedBy) {
		this.cancellationInitiatedBy = cancellationInitiatedBy;
	}
	public String getPmEmailId() {
		return pmEmailId;
	}

	public void setPmEmailId(String pmEmailId) {
		this.pmEmailId = pmEmailId;
	}

	public String getSalesSupportEmailId() {
		return salesSupportEmailId;
	}

	public void setSalesSupportEmailId(String salesSupportEmailId) {
		this.salesSupportEmailId = salesSupportEmailId;
	}

	public String getSalesPersonEmailId() {
		return salesPersonEmailId;
	}

	public void setSalesPersonEmailId(String salesPersonEmailId) {
		this.salesPersonEmailId = salesPersonEmailId;
	}
}
