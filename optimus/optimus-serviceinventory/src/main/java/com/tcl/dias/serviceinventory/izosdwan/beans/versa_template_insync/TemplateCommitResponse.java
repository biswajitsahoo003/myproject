package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean to template commit response
 * @author archchan
 *
 */
public class TemplateCommitResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("versanms.templateResponse")
	private CommitTemplateVersa versanmsTemplateResponse;

	public CommitTemplateVersa getVersanmsTemplateResponse() {
		return versanmsTemplateResponse;
	}

	public void setVersanmsTemplateResponse(CommitTemplateVersa versanmsTemplateResponse) {
		this.versanmsTemplateResponse = versanmsTemplateResponse;
	}

	@Override
	public String toString() {
		return "TemplateCommitResponse [versanmsTemplateResponse=" + versanmsTemplateResponse + "]";
	}
	
	

}
