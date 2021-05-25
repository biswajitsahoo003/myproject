package com.tcl.dias.auth.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean class
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class UserLeMappingRequest {

	private String userMode;
	private Integer userId;
	private List<UserMappingResponse> additionalLegalEntites = new ArrayList<>();
	private List<UserMappingResponse> reductionalLegalEntites = new ArrayList<>();

	public String getUserMode() {
		return userMode;
	}

	public void setUserMode(String userMode) {
		this.userMode = userMode;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<UserMappingResponse> getAdditionalLegalEntites() {
		return additionalLegalEntites;
	}

	public void setAdditionalLegalEntites(List<UserMappingResponse> additionalLegalEntites) {
		this.additionalLegalEntites = additionalLegalEntites;
	}

	public List<UserMappingResponse> getReductionalLegalEntites() {
		return reductionalLegalEntites;
	}

	public void setReductionalLegalEntites(List<UserMappingResponse> reductionalLegalEntites) {
		this.reductionalLegalEntites = reductionalLegalEntites;
	}

	@Override
	public String toString() {
		return "UserLeMappingRequest [userMode=" + userMode + ", userId=" + userId + ", additionalLegalEntites="
				+ additionalLegalEntites + ", reductionalLegalEntites=" + reductionalLegalEntites + "]";
	}

}
