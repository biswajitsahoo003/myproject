
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the TabStatus.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "TabType",
    "Status",
    "XPosition",
    "YPosition",
    "TabLabel",
    "TabName",
    "TabValue",
    "DocumentID",
    "PageNumber"
})
public class TabStatus {

    @JsonProperty("TabType")
    private String tabType;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("XPosition")
    private String xPosition;
    @JsonProperty("YPosition")
    private String yPosition;
    @JsonProperty("TabLabel")
    private String tabLabel;
    @JsonProperty("TabName")
    private String tabName;
    @JsonProperty("TabValue")
    private Object tabValue;
    @JsonProperty("DocumentID")
    private String documentID;
    @JsonProperty("PageNumber")
    private String pageNumber;

    @JsonProperty("TabType")
    public String getTabType() {
        return tabType;
    }

    @JsonProperty("TabType")
    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("XPosition")
    public String getXPosition() {
        return xPosition;
    }

    @JsonProperty("XPosition")
    public void setXPosition(String xPosition) {
        this.xPosition = xPosition;
    }

    @JsonProperty("YPosition")
    public String getYPosition() {
        return yPosition;
    }

    @JsonProperty("YPosition")
    public void setYPosition(String yPosition) {
        this.yPosition = yPosition;
    }

    @JsonProperty("TabLabel")
    public String getTabLabel() {
        return tabLabel;
    }

    @JsonProperty("TabLabel")
    public void setTabLabel(String tabLabel) {
        this.tabLabel = tabLabel;
    }

    @JsonProperty("TabName")
    public String getTabName() {
        return tabName;
    }

    @JsonProperty("TabName")
    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    @JsonProperty("TabValue")
    public Object getTabValue() {
        return tabValue;
    }

    @JsonProperty("TabValue")
    public void setTabValue(Object tabValue) {
        this.tabValue = tabValue;
    }

    @JsonProperty("DocumentID")
    public String getDocumentID() {
        return documentID;
    }

    @JsonProperty("DocumentID")
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    @JsonProperty("PageNumber")
    public String getPageNumber() {
        return pageNumber;
    }

    @JsonProperty("PageNumber")
    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

}
