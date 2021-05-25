package com.tcl.dias.products.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;

/**
 * Component Dto for custom profile
 * 
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class ProfileComponentDto {

	@NotNull(message = Constants.ID_NULL)
	private int id;

	private String description;

	private String name;

	private boolean isActive;
	
	private boolean display;

	private Set<Integer> attributes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public Set<Integer> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<Integer> attributes) {
		this.attributes = attributes;
	}
	
	
	
}
