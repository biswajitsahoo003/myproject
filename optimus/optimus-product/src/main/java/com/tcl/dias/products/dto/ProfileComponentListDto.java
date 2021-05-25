package com.tcl.dias.products.dto;

import java.util.Set;

/**
 * This file contains the CustomProfileComponentListDto.java class.
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProfileComponentListDto {
	
	private Set<ProfileComponentDto> profileComponents;

	public Set<ProfileComponentDto> getProfileComponents() {
		return profileComponents;
	}

	public void setProfileComponents(Set<ProfileComponentDto> profileComponents) {
		this.profileComponents = profileComponents;
	}
	
}
