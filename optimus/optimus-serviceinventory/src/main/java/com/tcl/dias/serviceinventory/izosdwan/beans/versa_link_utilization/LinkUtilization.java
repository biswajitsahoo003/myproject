
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_link_utilization;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "qTime",
    "sEcho",
    "iTotalDisplayRecords",
    "iTotalRecords",
    "columnMetaData",
    "aaData"
})
public class LinkUtilization {

    @JsonProperty("qTime")
    private Integer qTime;
    @JsonProperty("sEcho")
    private Integer sEcho;
    @JsonProperty("iTotalDisplayRecords")
    private Integer iTotalDisplayRecords;
    @JsonProperty("iTotalRecords")
    private Integer iTotalRecords;
    @JsonProperty("columnMetaData")
    private List<ColumnMetaDatum> columnMetaData = null;
    @JsonProperty("aaData")
    private List<List<String>> aaData = null;

    @JsonProperty("qTime")
    public Integer getQTime() {
        return qTime;
    }

    @JsonProperty("qTime")
    public void setQTime(Integer qTime) {
        this.qTime = qTime;
    }

    @JsonProperty("sEcho")
    public Integer getSEcho() {
        return sEcho;
    }

    @JsonProperty("sEcho")
    public void setSEcho(Integer sEcho) {
        this.sEcho = sEcho;
    }

    @JsonProperty("iTotalDisplayRecords")
    public Integer getITotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    @JsonProperty("iTotalDisplayRecords")
    public void setITotalDisplayRecords(Integer iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    @JsonProperty("iTotalRecords")
    public Integer getITotalRecords() {
        return iTotalRecords;
    }

    @JsonProperty("iTotalRecords")
    public void setITotalRecords(Integer iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    @JsonProperty("columnMetaData")
    public List<ColumnMetaDatum> getColumnMetaData() {
        return columnMetaData;
    }

    @JsonProperty("columnMetaData")
    public void setColumnMetaData(List<ColumnMetaDatum> columnMetaData) {
        this.columnMetaData = columnMetaData;
    }

    @JsonProperty("aaData")
    public List<List<String>> getAaData() {
        return aaData;
    }

    @JsonProperty("aaData")
    public void setAaData(List<List<String>> aaData) {
        this.aaData = aaData;
    }

}
