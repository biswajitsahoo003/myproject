package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Class creates feasibility request
 * 
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "fReq",
    "RecordTypeName",
    "ProductsServices"
})
public class CreateFeasibilityRequest {

    @JsonProperty("fReq")
    private FRequest fReq;
    @JsonProperty("RecordTypeName")
    private String recordTypeName;
    @JsonProperty("ProductsServices")
    private String productsServices;
    @JsonProperty("CityAEnd")
    private String cityAEnd;  
    @JsonProperty("CityBEnd")
    private String cityBEnd;

    @JsonProperty("fReq")
    public FRequest getFReq() {
        return fReq;
    }

    @JsonProperty("fReq")
    public void setFReq(FRequest fReq) {
        this.fReq = fReq;
    }

    @JsonProperty("RecordTypeName")
    public String getRecordTypeName() {
        return recordTypeName;
    }

    @JsonProperty("RecordTypeName")
    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

    @JsonProperty("ProductsServices")
    public String getProductsServices() {
        return productsServices;
    }

    @JsonProperty("ProductsServices")
    public void setProductsServices(String productsServices) {
        this.productsServices = productsServices;
    }

	public String getCityAEnd() {
		return cityAEnd;
	}

	public void setCityAEnd(String cityAEnd) {
		this.cityAEnd = cityAEnd;
	}

	public String getCityBEnd() {
		return cityBEnd;
	}

	public void setCityBEnd(String cityBEnd) {
		this.cityBEnd = cityBEnd;
	}
    
}
