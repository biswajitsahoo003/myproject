package com.tcl.dias.servicefulfillmentutils.beans.sap;



import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "MRN_CREATION"
})
public class MrnPlaceOrderRequest {

    @JsonProperty("MRN_CREATION")
    private List<MrnCreation> mRNCREATION = null;

    @JsonProperty("MRN_CREATION")
    public List<MrnCreation> getMRNCREATION() {
    	
    	if(mRNCREATION==null) {
    		mRNCREATION=new ArrayList<MrnCreation>();
    	}
        return mRNCREATION;
    }

    @JsonProperty("MRN_CREATION")
    public void setMRNCREATION(List<MrnCreation> mRNCREATION) {
        this.mRNCREATION = mRNCREATION;
    }

}
