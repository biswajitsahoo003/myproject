
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_userdefined_url;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "category-name", "urls" })
public class UrlCategory {

	@JsonProperty("category-name")
	private String categoryName;
	@JsonProperty("urls")
	private Urls urls;

	@JsonProperty("category-name")
	public String getCategoryName() {
		return categoryName;
	}

	@JsonProperty("category-name")
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@JsonProperty("urls")
	public Urls getUrls() {
		return urls;
	}

	@JsonProperty("urls")
	public void setUrls(Urls urls) {
		this.urls = urls;
	}

}
