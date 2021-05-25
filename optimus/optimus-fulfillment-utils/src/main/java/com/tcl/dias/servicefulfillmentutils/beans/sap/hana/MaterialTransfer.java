
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Optimus_Id",
    "Document_Date",
    "Posting_Date",
    "MATERIAL_DETAILS"
})
public class MaterialTransfer {

    @JsonProperty("Optimus_Id")
    private String optimusId;
    @JsonProperty("Document_Date")
    private String documentDate;
    @JsonProperty("Posting_Date")
    private String postingDate;
    @JsonProperty("MATERIAL_DETAILS")
    private MATERIALDETAILS mATERIALDETAILS;

    @JsonProperty("Optimus_Id")
    public String getOptimusId() {
        return optimusId;
    }

    @JsonProperty("Optimus_Id")
    public void setOptimusId(String optimusId) {
        this.optimusId = optimusId;
    }

    @JsonProperty("Document_Date")
    public String getDocumentDate() {
        return documentDate;
    }

    @JsonProperty("Document_Date")
    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    @JsonProperty("Posting_Date")
    public String getPostingDate() {
        return postingDate;
    }

    @JsonProperty("Posting_Date")
    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    @JsonProperty("MATERIAL_DETAILS")
    public MATERIALDETAILS getMATERIALDETAILS() {
        return mATERIALDETAILS;
    }

    @JsonProperty("MATERIAL_DETAILS")
    public void setMATERIALDETAILS(MATERIALDETAILS mATERIALDETAILS) {
        this.mATERIALDETAILS = mATERIALDETAILS;
    }

}
