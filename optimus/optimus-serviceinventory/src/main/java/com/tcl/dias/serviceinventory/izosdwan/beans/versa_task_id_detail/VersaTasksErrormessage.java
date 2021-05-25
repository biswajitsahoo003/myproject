package com.tcl.dias.serviceinventory.izosdwan.beans.versa_task_id_detail;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "versa-tasks.error-code", "versa-tasks.error-message" })
public class VersaTasksErrormessage {

	@JsonProperty("versa-tasks.error-code")
	private String versaTasksErrorCode;
	@JsonProperty("versa-tasks.error-message")
	private String versaTasksErrorMessage;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("versa-tasks.error-code")
	public String getVersaTasksErrorCode() {
		return versaTasksErrorCode;
	}

	@JsonProperty("versa-tasks.error-code")
	public void setVersaTasksErrorCode(String versaTasksErrorCode) {
		this.versaTasksErrorCode = versaTasksErrorCode;
	}

	@JsonProperty("versa-tasks.error-message")
	public String getVersaTasksErrorMessage() {
		return versaTasksErrorMessage;
	}

	@JsonProperty("versa-tasks.error-message")
	public void setVersaTasksErrorMessage(String versaTasksErrorMessage) {
		this.versaTasksErrorMessage = versaTasksErrorMessage;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("versaTasksErrorCode", versaTasksErrorCode)
				.append("versaTasksErrorMessage", versaTasksErrorMessage)
				.append("additionalProperties", additionalProperties).toString();
	}

}