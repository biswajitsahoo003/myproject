
package com.tcl.dias.sfdc.response.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file contains the SfdcProductServicesResponseBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "status",
    "productsservices",
    "ProductId",
    "message"
})
public class SfdcProductServicesResponseBean {

    @JsonProperty("status")
    private String status;
    @JsonProperty("productsservices")
    private List<SfdcProductsservice> productsservices = null;
    @JsonProperty("ProductId")
    private String productId;
    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("productsservices")
    public List<SfdcProductsservice> getProductsservices() {
        return productsservices;
    }

    @JsonProperty("productsservices")
    public void setProductsservices(List<SfdcProductsservice> productsservices) {
        this.productsservices = productsservices;
    }

    @JsonProperty("ProductId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

}
