
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the RecipientStatus.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Type", "Email", "UserName", "RoutingOrder", "Sent", "Delivered", "Signed", "DeclineReason",
		"Status", "RecipientIPAddress", "CustomFields", "TabStatuses", "AccountStatus", "RecipientId" })
public class RecipientStatus {

	@JsonProperty("Type")
	private String type;
	@JsonProperty("Email")
	private String email;
	@JsonProperty("UserName")
	private String userName;
	@JsonProperty("RoutingOrder")
	private String routingOrder;
	@JsonProperty("Sent")
	private String sent;
	@JsonProperty("Delivered")
	private String delivered;
	@JsonProperty("Signed")
	private String signed;
	@JsonProperty("DeclineReason")
	private Object declineReason;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("RecipientIPAddress")
	private String recipientIPAddress;
	@JsonProperty("CustomFields")
	private Object customFields;
	@JsonProperty("TabStatuses")
	private TabStatuses tabStatuses;
	@JsonProperty("AccountStatus")
	private String accountStatus;
	@JsonProperty("RecipientId")
	private String recipientId;

	@JsonProperty("Type")
	public String getType() {
		return type;
	}

	@JsonProperty("Type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("Email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("Email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("UserName")
	public String getUserName() {
		return userName;
	}

	@JsonProperty("UserName")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonProperty("RoutingOrder")
	public String getRoutingOrder() {
		return routingOrder;
	}

	@JsonProperty("RoutingOrder")
	public void setRoutingOrder(String routingOrder) {
		this.routingOrder = routingOrder;
	}

	@JsonProperty("Sent")
	public String getSent() {
		return sent;
	}

	@JsonProperty("Sent")
	public void setSent(String sent) {
		this.sent = sent;
	}

	@JsonProperty("Delivered")
	public String getDelivered() {
		return delivered;
	}

	@JsonProperty("Delivered")
	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}

	@JsonProperty("Signed")
	public String getSigned() {
		return signed;
	}

	@JsonProperty("Signed")
	public void setSigned(String signed) {
		this.signed = signed;
	}

	@JsonProperty("DeclineReason")
	public Object getDeclineReason() {
		return declineReason;
	}

	@JsonProperty("DeclineReason")
	public void setDeclineReason(Object declineReason) {
		this.declineReason = declineReason;
	}

	@JsonProperty("Status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("Status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("RecipientIPAddress")
	public String getRecipientIPAddress() {
		return recipientIPAddress;
	}

	@JsonProperty("RecipientIPAddress")
	public void setRecipientIPAddress(String recipientIPAddress) {
		this.recipientIPAddress = recipientIPAddress;
	}

	@JsonProperty("CustomFields")
	public Object getCustomFields() {
		return customFields;
	}

	@JsonProperty("CustomFields")
	public void setCustomFields(Object customFields) {
		this.customFields = customFields;
	}

	@JsonProperty("TabStatuses")
	public TabStatuses getTabStatuses() {
		return tabStatuses;
	}

	@JsonProperty("TabStatuses")
	public void setTabStatuses(TabStatuses tabStatuses) {
		this.tabStatuses = tabStatuses;
	}

	@JsonProperty("AccountStatus")
	public String getAccountStatus() {
		return accountStatus;
	}

	@JsonProperty("AccountStatus")
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	@JsonProperty("RecipientId")
	public String getRecipientId() {
		return recipientId;
	}

	@JsonProperty("RecipientId")
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

}
