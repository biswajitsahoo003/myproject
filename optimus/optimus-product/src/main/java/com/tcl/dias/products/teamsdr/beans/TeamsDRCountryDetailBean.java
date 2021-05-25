package com.tcl.dias.products.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.products.teamsdr.constants.TeamsDRProductConstants;

/**
 * Country detail bean for Teamsdr.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamsDRCountryDetailBean {
	private String name;
	private String code;
	private String isdCode;
	private Boolean isGsc;
	private Boolean isRegulatedCountry;
	private Boolean isExceptionalCountry;
	private Boolean isDomesticVoice;
	private Boolean isGlobalOutbound;
	private Boolean isTataLeAvailable;

	public TeamsDRCountryDetailBean() {
	}

	public TeamsDRCountryDetailBean(String name, String code, String isdCode, String isRegulatedCountry,
			String isExceptionalCountry) {
		this.name = name;
		this.code = code;
		this.isdCode = isdCode;
		this.isRegulatedCountry = isRegulatedCountry.contains(TeamsDRProductConstants.Y);
		this.isExceptionalCountry = isExceptionalCountry.contains(TeamsDRProductConstants.Y);
	}

	public TeamsDRCountryDetailBean(String name, String code, String isdCode, String isGsc, String isRegulatedCountry,
									String isExceptionalCountry, String isDomesticVoice,String isGlobalOutbound) {
		this.name = name;
		this.code = code;
		this.isdCode = isdCode;
		this.isGsc = isGsc.contains(TeamsDRProductConstants.Y);
		this.isRegulatedCountry = isRegulatedCountry.contains(TeamsDRProductConstants.Y);
		this.isExceptionalCountry = isExceptionalCountry.contains(TeamsDRProductConstants.Y);
		this.isDomesticVoice = isDomesticVoice.contains(TeamsDRProductConstants.Y);
		this.isGlobalOutbound = isGlobalOutbound.contains(TeamsDRProductConstants.Y);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsdCode() {
		return isdCode;
	}

	public void setIsdCode(String isdCode) {
		this.isdCode = isdCode;
	}

	public Boolean getIsGsc() {
		return isGsc;
	}

	public void setGsc(Boolean gsc) {
		isGsc = gsc;
	}

	public Boolean getIsRegulatedCountry() {
		return isRegulatedCountry;
	}

	public void setIsRegulatedCountry(Boolean regulatedCountry) {
		isRegulatedCountry = regulatedCountry;
	}

	public Boolean getIsExceptionalCountry() {
		return isExceptionalCountry;
	}

	public void setIsExceptionalCountry(Boolean isExceptionalCountry) {
		this.isExceptionalCountry = isExceptionalCountry;
	}

	public Boolean getIsDomesticVoice() {
		return isDomesticVoice;
	}

	public void setIsDomesticVoice(Boolean domesticVoice) {
		isDomesticVoice = domesticVoice;
	}

	public Boolean getIsGlobalOutbound() {
		return isGlobalOutbound;
	}

	public void setIsGlobalOutbound(Boolean globalOutbound) {
		isGlobalOutbound = globalOutbound;
	}

	public Boolean getIsTataLeAvailable() {
		return isTataLeAvailable;
	}

	public void setIsTataLeAvailable(Boolean tataLe) {
		isTataLeAvailable = tataLe;
	}

	@Override
	public String toString() {
		return "TeamsDRCountryDetailBean{" +
				"name='" + name + '\'' +
				", code='" + code + '\'' +
				", isdCode='" + isdCode + '\'' +
				", isGsc=" + isGsc +
				", isRegulatedCountry=" + isRegulatedCountry +
				", isExceptionalCountry=" + isExceptionalCountry +
				", isDomesticVoice=" + isDomesticVoice +
				", isGlobalOutbound=" + isGlobalOutbound +
				", isTataLeAvailable=" + isTataLeAvailable +
				'}';
	}
}
