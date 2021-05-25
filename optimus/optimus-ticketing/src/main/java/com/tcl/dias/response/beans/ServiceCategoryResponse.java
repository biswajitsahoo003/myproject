package com.tcl.dias.response.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.beans.CategoriesBean;

/**
 * This file contains the ServiceCategoryResponse.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCategoryResponse {
	
	private String status;

	private String message;
	
	private List<CategoriesBean> categories;

	public List<CategoriesBean> getCategories() {
		if(categories==null) {
			categories=new ArrayList<>();
		}
		return categories;
	}

	public void setCategories(List<CategoriesBean> categories) {
		this.categories = categories;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	

}
