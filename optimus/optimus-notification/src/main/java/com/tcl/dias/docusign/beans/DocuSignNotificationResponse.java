package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the DocuSignNotificationResponse.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DocuSignNotificationResponse {

	@JsonProperty("DocuSignEnvelopeInformation")
	private DocusignEnvelope docuSignEnvelopeInformation;

	public DocusignEnvelope getDocuSignEnvelopeInformation() {
		return docuSignEnvelopeInformation;
	}

	public void setDocuSignEnvelopeInformation(DocusignEnvelope docuSignEnvelopeInformation) {
		this.docuSignEnvelopeInformation = docuSignEnvelopeInformation;
	}

}
