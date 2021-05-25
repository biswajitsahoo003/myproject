
package com.tcl.dias.oms.izosdwan.beans;

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
 * 
 * This file contains the PricingUpdateRequest.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "priceUpdateRequestList",
})
public class PricingUpdateRequest {

    @JsonProperty("priceUpdateRequestList")
    private List<PriceUpdateBean> priceUpdateRequestList = null;

	public List<PriceUpdateBean> getPriceUpdateRequestList() {
		return priceUpdateRequestList;
	}

	public void setPriceUpdateRequestList(List<PriceUpdateBean> priceUpdateRequestList) {
		this.priceUpdateRequestList = priceUpdateRequestList;
	}
    
    

}
