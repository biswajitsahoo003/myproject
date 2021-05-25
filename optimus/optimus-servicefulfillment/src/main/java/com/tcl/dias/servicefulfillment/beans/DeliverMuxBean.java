package com.tcl.dias.servicefulfillment.beans;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to Deliver Mux
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class DeliverMuxBean extends TaskDetailsBaseBean  {
	
	private String shipmentNumber; 
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String materialDeliveryDate;
	private String materialTrackingStatus;
	
	public String getShipmentNumber() {
		return shipmentNumber;
	}
	public void setShipmentNumber(String shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
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
		this.materialTrackingStatus = materialTrackingStatus;
	}
}
