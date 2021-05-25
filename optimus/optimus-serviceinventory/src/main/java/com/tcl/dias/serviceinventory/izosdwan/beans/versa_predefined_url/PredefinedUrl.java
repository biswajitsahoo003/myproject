
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_predefined_url;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "category-name", "category-id", "category-description" })
public class PredefinedUrl {

	@JsonProperty("category-name")
	private String categoryName;
	@JsonProperty("category-id")
	private String categoryId;
	@JsonProperty("category-description")
	private String categoryDescription;

	@JsonProperty("category-name")
	public String getCategoryName() {
		return categoryName;
	}

	@JsonProperty("category-name")
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@JsonProperty("category-id")
	public String getCategoryId() {
		return categoryId;
	}

	@JsonProperty("category-id")
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@JsonProperty("category-description")
	public String getCategoryDescription() {
		return categoryDescription;
	}

	@JsonProperty("category-description")
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

}
