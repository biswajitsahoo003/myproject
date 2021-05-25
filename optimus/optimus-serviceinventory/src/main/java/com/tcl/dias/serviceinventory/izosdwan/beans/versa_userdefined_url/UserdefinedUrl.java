
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_userdefined_url;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "url-category" })
public class UserdefinedUrl {

	@JsonProperty("url-category")
	private List<UrlCategory> urlCategory = null;

	@JsonProperty("url-category")
	public List<UrlCategory> getUrlCategory() {
		return urlCategory;
	}

	@JsonProperty("url-category")
	public void setUrlCategory(List<UrlCategory> urlCategory) {
		this.urlCategory = urlCategory;
	}

}
