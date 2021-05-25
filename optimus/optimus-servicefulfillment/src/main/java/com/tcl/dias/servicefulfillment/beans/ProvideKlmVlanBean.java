package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to track and complete Lm Delivery
 * 
 *
 * @author Sarath Kumar.M
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ProvideKlmVlanBean extends BaseRequest{
	
	private HandoffDetailsBean handoffDetails;
	private String cableLengthCustomerServerRoom;
	private String addtionalHardwareAtCustomer;
	private String neededHardware;
	private String neededHardwareOwner;
	
	public HandoffDetailsBean getHandoffDetails() {
		return handoffDetails;
	}
	public void setHandoffDetails(HandoffDetailsBean handoffDetails) {
		this.handoffDetails = handoffDetails;
	}
	public String getCableLengthCustomerServerRoom() {
		return cableLengthCustomerServerRoom;
	}
	public void setCableLengthCustomerServerRoom(String cableLengthCustomerServerRoom) {
		this.cableLengthCustomerServerRoom = cableLengthCustomerServerRoom;
	}
	public String getAddtionalHardwareAtCustomer() {
		return addtionalHardwareAtCustomer;
	}
	public void setAddtionalHardwareAtCustomer(String addtionalHardwareAtCustomer) {
		this.addtionalHardwareAtCustomer = addtionalHardwareAtCustomer;
	}
	public String getNeededHardware() {
		return neededHardware;
	}
	public void setNeededHardware(String neededHardware) {
		this.neededHardware = neededHardware;
	}
	public String getNeededHardwareOwner() {
		return neededHardwareOwner;
	}
	public void setNeededHardwareOwner(String neededHardwareOwner) {
		this.neededHardwareOwner = neededHardwareOwner;
	}
}
