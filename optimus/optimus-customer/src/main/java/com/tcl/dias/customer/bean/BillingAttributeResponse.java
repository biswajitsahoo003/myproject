/**
 * 
 */
package com.tcl.dias.customer.bean;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author KeerSoun
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"billingType",
"billingMethod",
"billingFrequency",
"invoiceMethod",
"billingCurrency",
"paymentCurrency",
"paymentTerm"
})
public class BillingAttributeResponse {

@JsonProperty("billingType")
private List<String> billingType = null;
@JsonProperty("billingMethod")
private List<String> billingMethod = null;
@JsonProperty("billingFrequency")
private List<String> billingFrequency = null;
@JsonProperty("invoiceMethod")
private List<String> invoiceMethod = null;
@JsonProperty("billingCurrency")
private List<String> billingCurrency = null;
@JsonProperty("paymentCurrency")
private List<String> paymentCurrency = null;
@JsonProperty("paymentTerm")
private List<String> paymentTerm = null;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
*
*/
public BillingAttributeResponse() {
}

/**
*
* @param billingFrequency
* @param billingType
* @param invoiceMethod
* @param billingCurrency
* @param billingMethod
* @param paymentCurrency
* @param paymentTerm
*/
public BillingAttributeResponse(List<String> billingType, List<String> billingMethod, List<String> billingFrequency, List<String> invoiceMethod, List<String> billingCurrency, List<String> paymentCurrency, List<String> paymentTerm) {
super();
this.billingType = billingType;
this.billingMethod = billingMethod;
this.billingFrequency = billingFrequency;
this.invoiceMethod = invoiceMethod;
this.billingCurrency = billingCurrency;
this.paymentCurrency = paymentCurrency;
this.paymentTerm = paymentTerm;
}

@JsonProperty("billingType")
public List<String> getBillingType() {
return billingType;
}

@JsonProperty("billingType")
public void setBillingType(List<String> billingType) {
this.billingType = billingType;
}

@JsonProperty("billingMethod")
public List<String> getBillingMethod() {
return billingMethod;
}

@JsonProperty("billingMethod")
public void setBillingMethod(List<String> billingMethod) {
this.billingMethod = billingMethod;
}

@JsonProperty("billingFrequency")
public List<String> getBillingFrequency() {
return billingFrequency;
}

@JsonProperty("billingFrequency")
public void setBillingFrequency(List<String> billingFrequency) {
this.billingFrequency = billingFrequency;
}

@JsonProperty("invoiceMethod")
public List<String> getInvoiceMethod() {
return invoiceMethod;
}

@JsonProperty("invoiceMethod")
public void setInvoiceMethod(List<String> invoiceMethod) {
this.invoiceMethod = invoiceMethod;
}

@JsonProperty("billingCurrency")
public List<String> getBillingCurrency() {
return billingCurrency;
}

@JsonProperty("billingCurrency")
public void setBillingCurrency(List<String> billingCurrency) {
this.billingCurrency = billingCurrency;
}

@JsonProperty("paymentCurrency")
public List<String> getPaymentCurrency() {
return paymentCurrency;
}

@JsonProperty("paymentCurrency")
public void setPaymentCurrency(List<String> paymentCurrency) {
this.paymentCurrency = paymentCurrency;
}

@JsonProperty("paymentTerm")
public List<String> getPaymentTerm() {
return paymentTerm;
}

@JsonProperty("paymentTerm")
public void setPaymentTerm(List<String> paymentTerm) {
this.paymentTerm = paymentTerm;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}