package com.tcl.dias.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the CategoriesBean.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriesBean {
	
	private String impactName;
	
	
	private List<String> categories;


	public String getImpactName() {
		return impactName;
	}


	public void setImpactName(String impactName) {
		this.impactName = impactName;
	}


	public List<String> getCategories() {
		
		if(categories==null) {
			categories=new ArrayList<>();
		}
		return categories;
	}


	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	

}
