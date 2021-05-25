package com.tcl.dias.common.beans;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file Contains MDM Contact information
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "srcKeys",
    "addresses",
    "contactMethods",
    "role",
    "contactRelationshipType",
    "decisionMakerInfluencer",
    "hasOptedOutOfFax",
    "technicalContact",
    "primaryRoamingServiceContact",
    "failedEmail",
    "billingCollectionsContact",
    "doNotCall",
    "recordTypeId",
    "primaryServiceAssuranceContact",
    "createdById",
    "lastName",
    "ownerId",
    "executive",
    "customer24X7Noc",
    "serviceAssuranceContact",
    "sourceIdentifierValue",
    "firstName",
    "hasOptedOutOfEmail",
})
public class MDMContact {
	@JsonProperty("srcKeys")
	private MDMSrcKeys srcKeys;
	@JsonProperty("addresses")
	private ArrayList<MDMAddress> addresses;
	@JsonProperty("contactMethods")
	private ArrayList<MDMContactMethod> contactMethods;
	@JsonProperty("role")
	private ArrayList<String> role;
	@JsonProperty("contactRelationshipType")
	private String contactRelationshipType;
	@JsonProperty("createdDate")
	private String createdDate;
	@JsonProperty("decisionMakerInfluencer")
	private String decisionMakerInfluencer;
	@JsonProperty("hasOptedOutOfFax")
	private String hasOptedOutOfFax;
	@JsonProperty("technicalContact")
	private String technicalContact;
	@JsonProperty("primaryRoamingServiceContact")
	private String primaryRoamingServiceContact;
	@JsonProperty("failedEmail")
	private String failedEmail;
	@JsonProperty("billingCollectionsContact")
	private String billingCollectionsContact;
	@JsonProperty("doNotCall")
	private String doNotCall;
	@JsonProperty("recordTypeId")
	private String recordTypeId;
	@JsonProperty("primaryServiceAssuranceContact")
	private String primaryServiceAssuranceContact;
	@JsonProperty("createdById")
	private String createdById;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("ownerId")
	private String ownerId;
	@JsonProperty("executive")
	private String executive;
	@JsonProperty("customer24X7Noc")
	private String customer24X7Noc;
	@JsonProperty("serviceAssuranceContact")
	private String serviceAssuranceContact;
	@JsonProperty("sourceIdentifierValue")
	private String sourceIdentifierValue;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("hasOptedOutOfEmail")
	private String hasOptedOutOfEmail;
	
	@JsonProperty("contactRelationshipType")
	public String getContactRelationshipType() {
		return contactRelationshipType;
	}
	@JsonProperty("contactRelationshipType")
	public void setContactRelationshipType(String contactRelationshipType) {
		this.contactRelationshipType = contactRelationshipType;
	}
	
	@JsonProperty("createdDate")
	public String getCreatedDate() {
		return createdDate;
	}
	@JsonProperty("createdDate")
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	@JsonProperty("decisionMakerInfluencer")
	public String getDecisionMakerInfluencer() {
		return decisionMakerInfluencer;
	}
	@JsonProperty("decisionMakerInfluencer")
	public void setDecisionMakerInfluencer(String decisionMakerInfluencer) {
		this.decisionMakerInfluencer = decisionMakerInfluencer;
	}
	
	@JsonProperty("hasOptedOutOfFax")
	public String getHasOptedOutOfFax() {
		return hasOptedOutOfFax;
	}
	@JsonProperty("hasOptedOutOfFax")
	public void setHasOptedOutOfFax(String hasOptedOutOfFax) {
		this.hasOptedOutOfFax = hasOptedOutOfFax;
	}
	
	@JsonProperty("technicalContact")
	public String getTechnicalContact() {
		return technicalContact;
	}
	
	@JsonProperty("technicalContact")
	public void setTechnicalContact(String technicalContact) {
		this.technicalContact = technicalContact;
	}
	
	@JsonProperty("primaryRoamingServiceContact")
	public String getPrimaryRoamingServiceContact() {
		return primaryRoamingServiceContact;
	}
	@JsonProperty("primaryRoamingServiceContact")
	public void setPrimaryRoamingServiceContact(String primaryRoamingServiceContact) {
		this.primaryRoamingServiceContact = primaryRoamingServiceContact;
	}
	
	@JsonProperty("failedEmail")
	public String getFailedEmail() {
		return failedEmail;
	}
	@JsonProperty("failedEmail")
	public void setFailedEmail(String failedEmail) {
		this.failedEmail = failedEmail;
	}
	
	@JsonProperty("billingCollectionsContact")
	public String getBillingCollectionsContact() {
		return billingCollectionsContact;
	}
	@JsonProperty("billingCollectionsContact")
	public void setBillingCollectionsContact(String billingCollectionsContact) {
		this.billingCollectionsContact = billingCollectionsContact;
	}
	
	@JsonProperty("doNotCall")
	public String getDoNotCall() {
		return doNotCall;
	}
	@JsonProperty("doNotCall")
	public void setDoNotCall(String doNotCall) {
		this.doNotCall = doNotCall;
	}
	
	@JsonProperty("recordTypeId")
	public String getRecordTypeId() {
		return recordTypeId;
	}
	@JsonProperty("recordTypeId")
	public void setRecordTypeId(String recordTypeId) {
		this.recordTypeId = recordTypeId;
	}
	
	@JsonProperty("primaryServiceAssuranceContact")
	public String getPrimaryServiceAssuranceContact() {
		return primaryServiceAssuranceContact;
	}
	@JsonProperty("primaryServiceAssuranceContact")
	public void setPrimaryServiceAssuranceContact(String primaryServiceAssuranceContact) {
		this.primaryServiceAssuranceContact = primaryServiceAssuranceContact;
	}
	
	@JsonProperty("createdById")
	public String getCreatedById() {
		return createdById;
	}
	@JsonProperty("createdById")
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	
	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}
	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@JsonProperty("ownerId")
	public String getOwnerId() {
		return ownerId;
	}
	@JsonProperty("ownerId")
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	@JsonProperty("executive")
	public String getExecutive() {
		return executive;
	}
	@JsonProperty("executive")
	public void setExecutive(String executive) {
		this.executive = executive;
	}
	
	@JsonProperty("customer24X7Noc")
	public String getCustomer24X7Noc() {
		return customer24X7Noc;
	}
	@JsonProperty("customer24X7Noc")
	public void setCustomer24X7Noc(String customer24x7Noc) {
		customer24X7Noc = customer24x7Noc;
	}
	
	@JsonProperty("serviceAssuranceContact")
	public String getServiceAssuranceContact() {
		return serviceAssuranceContact;
	}
	@JsonProperty("serviceAssuranceContact")
	public void setServiceAssuranceContact(String serviceAssuranceContact) {
		this.serviceAssuranceContact = serviceAssuranceContact;
	}
	
	@JsonProperty("sourceIdentifierValue")
	public String getSourceIdentifierValue() {
		return sourceIdentifierValue;
	}
	@JsonProperty("sourceIdentifierValue")
	public void setSourceIdentifierValue(String sourceIdentifierValue) {
		this.sourceIdentifierValue = sourceIdentifierValue;
	}
	
	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}
	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@JsonProperty("hasOptedOutOfEmail")
	public String getHasOptedOutOfEmail() {
		return hasOptedOutOfEmail;
	}
	@JsonProperty("hasOptedOutOfEmail")
	public void setHasOptedOutOfEmail(String hasOptedOutOfEmail) {
		this.hasOptedOutOfEmail = hasOptedOutOfEmail;
	}
	
	@JsonProperty("role")
	public ArrayList<String> getRole() {
		return role;
	}
	@JsonProperty("role")
	public void setRole(ArrayList<String> role) {
		this.role = role;
	}
	
	@JsonProperty("contactMethods")
	public ArrayList<MDMContactMethod> getContactMethods() {
		return contactMethods;
	}
	@JsonProperty("contactMethods")
	public void setContactMethods(ArrayList<MDMContactMethod> contactMethods) {
		this.contactMethods = contactMethods;
	}
	
	@JsonProperty("srcKeys")
	public MDMSrcKeys getSrcKeys() {
		return srcKeys;
	}
	@JsonProperty("srcKeys")
	public void setSrcKeys(MDMSrcKeys srcKeys) {
		this.srcKeys = srcKeys;
	}
	
	@JsonProperty("addresses")
	public ArrayList<MDMAddress> getAddresses() {
		return addresses;
	}
	@JsonProperty("addresses")
	public void setAddresses(ArrayList<MDMAddress> addresses) {
		this.addresses = addresses;
	}
	
	
	}
