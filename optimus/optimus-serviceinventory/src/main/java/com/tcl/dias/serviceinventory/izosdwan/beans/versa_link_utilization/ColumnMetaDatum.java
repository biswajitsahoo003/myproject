
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_link_utilization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "drillKey",
    "renderType",
    "name",
    "sortable",
    "defaultSortable",
    "defaultSortOrder"
})
public class ColumnMetaDatum {

    @JsonProperty("id")
    private String id;
    @JsonProperty("drillKey")
    private String drillKey;
    @JsonProperty("renderType")
    private String renderType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("sortable")
    private Boolean sortable;
    @JsonProperty("defaultSortable")
    private Boolean defaultSortable;
    @JsonProperty("defaultSortOrder")
    private String defaultSortOrder;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("drillKey")
    public String getDrillKey() {
        return drillKey;
    }

    @JsonProperty("drillKey")
    public void setDrillKey(String drillKey) {
        this.drillKey = drillKey;
    }

    @JsonProperty("renderType")
    public String getRenderType() {
        return renderType;
    }

    @JsonProperty("renderType")
    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("sortable")
    public Boolean getSortable() {
        return sortable;
    }

    @JsonProperty("sortable")
    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    @JsonProperty("defaultSortable")
    public Boolean getDefaultSortable() {
        return defaultSortable;
    }

    @JsonProperty("defaultSortable")
    public void setDefaultSortable(Boolean defaultSortable) {
        this.defaultSortable = defaultSortable;
    }

    @JsonProperty("defaultSortOrder")
    public String getDefaultSortOrder() {
        return defaultSortOrder;
    }

    @JsonProperty("defaultSortOrder")
    public void setDefaultSortOrder(String defaultSortOrder) {
        this.defaultSortOrder = defaultSortOrder;
    }

}
