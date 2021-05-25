
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "applianceName", "applianceUuid", "locationId", "latitude", "longitude", "type" })
public class ApplianceLocation implements Serializable {

	@JsonProperty("applianceName")
	private String applianceName;
	@JsonProperty("applianceUuid")
	private String applianceUuid;
	@JsonProperty("locationId")
	private String locationId;
	@JsonProperty("latitude")
	private Double latitude;
	@JsonProperty("longitude")
	private Double longitude;
	@JsonProperty("type")
	private String type;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = -4413866550988233015L;

	@JsonProperty("applianceName")
	public String getApplianceName() {
		return applianceName;
	}

	@JsonProperty("applianceName")
	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}

	@JsonProperty("applianceUuid")
	public String getApplianceUuid() {
		return applianceUuid;
	}

	@JsonProperty("applianceUuid")
	public void setApplianceUuid(String applianceUuid) {
		this.applianceUuid = applianceUuid;
	}

	@JsonProperty("locationId")
	public String getLocationId() {
		return locationId;
	}

	@JsonProperty("locationId")
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	@JsonProperty("latitude")
	public Double getLatitude() {
		return latitude;
	}

	@JsonProperty("latitude")
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@JsonProperty("longitude")
	public Double getLongitude() {
		return longitude;
	}

	@JsonProperty("longitude")
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
