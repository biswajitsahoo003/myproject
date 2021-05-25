package com.tcl.dias.servicefulfillmentutils.sap.response;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.utils.Status;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MRN_CREATION"
})
public class MrnCreationReponse {
	
	private Status status;
	private String errorMessage;

    @JsonProperty("MRN_CREATION")
    private MRNCREATION mRNCREATION;

    @JsonProperty("MRN_CREATION")
    public MRNCREATION getMRNCREATION() {
        return mRNCREATION;
    }

    @JsonProperty("MRN_CREATION")
    public void setMRNCREATION(MRNCREATION mRNCREATION) {
        this.mRNCREATION = mRNCREATION;
    }

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
    
    
    
   

}
