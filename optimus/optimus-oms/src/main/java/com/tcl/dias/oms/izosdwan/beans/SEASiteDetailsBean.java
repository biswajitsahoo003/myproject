
package com.tcl.dias.oms.izosdwan.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the SEASiteDetailsBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonPropertyOrder({
	"id",
    "locationId",
    "localLoopBw",
    "defaultBw",
    "maxBw",
    "selectedBw",
    "isSelected",
    "breakupLocation"
})
public class SEASiteDetailsBean {
	
	@JsonProperty("id")
    private Integer id;

    @JsonProperty("locationId")
    private Integer locationId;
    @JsonProperty("localLoopBw")
    private String localLoopBw;
    @JsonProperty("defaultBw")
    private String defaultBw;
    @JsonProperty("maxBw")
    private String maxBw;
    @JsonProperty("selectedBw")
    private String selectedBw;
    @JsonProperty("isSelected")
    private Integer isSelected;
    @JsonProperty("breakupLocation")
    private String breakupLocation;

    @JsonProperty("locationId")
    public Integer getLocationId() {
        return locationId;
    }

    @JsonProperty("locationId")
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @JsonProperty("localLoopBw")
    public String getLocalLoopBw() {
        return localLoopBw;
    }

    @JsonProperty("localLoopBw")
    public void setLocalLoopBw(String localLoopBw) {
        this.localLoopBw = localLoopBw;
    }

    @JsonProperty("defaultBw")
    public String getDefaultBw() {
        return defaultBw;
    }

    @JsonProperty("defaultBw")
    public void setDefaultBw(String defaultBw) {
        this.defaultBw = defaultBw;
    }

    @JsonProperty("maxBw")
    public String getMaxBw() {
        return maxBw;
    }

    @JsonProperty("maxBw")
    public void setMaxBw(String maxBw) {
        this.maxBw = maxBw;
    }

    @JsonProperty("selectedBw")
    public String getSelectedBw() {
        return selectedBw;
    }

    @JsonProperty("selectedBw")
    public void setSelectedBw(String selectedBw) {
        this.selectedBw = selectedBw;
    }

    @JsonProperty("isSelected")
    public Integer getIsSelected() {
        return isSelected;
    }

    @JsonProperty("isSelected")
    public void setIsSelected(Integer isSelected) {
        this.isSelected = isSelected;
    }

    @JsonProperty("breakupLocation")
    public String getBreakupLocation() {
        return breakupLocation;
    }

    @JsonProperty("breakupLocation")
    public void setBreakupLocation(String breakupLocation) {
        this.breakupLocation = breakupLocation;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
