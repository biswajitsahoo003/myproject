package com.tcl.dias.ticketing.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.beans.AdditionalVariablesBean;
import com.tcl.dias.beans.CommonVariablesBean;
import com.tcl.dias.beans.NotesBean;

/**
 * used for the creation of
 * ticket in service request management
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSRRequest {

	private String catalogName;
	private CommonVariablesBean commonVariables;
	private AdditionalVariablesBean additionalVariables;
	private List<NotesBean> notes;

	/**
	 * @return the catalogName
	 */
	public String getCatalogName() {
		return catalogName;
	}

	/**
	 * @param catalogName
	 *            the catalogName to set
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	
		/**
	 * @return the commonVariables
	 */
	public CommonVariablesBean getCommonVariables() {
		return commonVariables;
	}

	/**
	 * @param commonVariables the commonVariables to set
	 */
	public void setCommonVariables(CommonVariablesBean commonVariables) {
		this.commonVariables = commonVariables;
	}

	/**
	 * @return the additionalVariables
	 */
	public AdditionalVariablesBean getAdditionalVariables() {
		return additionalVariables;
	}

	/**
	 * @param additionalVariables the additionalVariables to set
	 */
	public void setAdditionalVariables(AdditionalVariablesBean additionalVariables) {
		this.additionalVariables = additionalVariables;
	}

		/**
	 * @return the notes
	 */
	public List<NotesBean> getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(List<NotesBean> notes) {
		this.notes = notes;
	}

}
