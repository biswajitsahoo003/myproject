package com.tcl.dias.servicefulfillment.request;



import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MRN_CREATION"
})
public class MrnCreationRequest {

    @JsonProperty("MRN_CREATION")
    private List<MRNCREATION> mRNCREATION = null;

    @JsonProperty("MRN_CREATION")
    public List<MRNCREATION> getMRNCREATION() {
    	
    	if(mRNCREATION==null) {
    		mRNCREATION=new ArrayList<MRNCREATION>();
    	}
        return mRNCREATION;
    }

    @JsonProperty("MRN_CREATION")
    public void setMRNCREATION(List<MRNCREATION> mRNCREATION) {
        this.mRNCREATION = mRNCREATION;
    }

}
