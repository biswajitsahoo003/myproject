/**
 * 
 */
package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Deliver RF Equipment details.
 * 
 * @author arjayapa
 */
public class DeliverRfEquipmentDetails extends TaskDetailsBaseBean {
	
	private String shippingNo;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String materialDeliveryDate; 
	
	private String materialTrackingStatus;
	private String materialSentBy;
	private String couriorName;

	private String distributionCenterName;
	private String distributionCenterAddress;

	public String getShippingNo() {
		return shippingNo;
	}
	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}	
	public String getMaterialDeliveryDate() {
		return materialDeliveryDate;
	}
	public void setMaterialDeliveryDate(String materialDeliveryDate) {
		this.materialDeliveryDate = materialDeliveryDate;
	}
	public String getMaterialTrackingStatus() {
		return materialTrackingStatus;
	}
	public void setMaterialTrackingStatus(String materialTrackingStatus) {
		this.materialTrackingStatus = materialTrackingStatus;}
	public String getMaterialSentBy() { return materialSentBy; }
	public void setMaterialSentBy(String materialSentBy) { this.materialSentBy = materialSentBy; }
	public String getCouriorName() { return couriorName; }
	public void setCouriorName(String couriorName) { this.couriorName = couriorName; }

	public String getDistributionCenterName() {
		return distributionCenterName;
	}

	public void setDistributionCenterName(String distributionCenterName) {
		this.distributionCenterName = distributionCenterName;
	}

	public String getDistributionCenterAddress() {
		return distributionCenterAddress;
	}

	public void setDistributionCenterAddress(String distributionCenterAddress) {
		this.distributionCenterAddress = distributionCenterAddress;
	}
}
