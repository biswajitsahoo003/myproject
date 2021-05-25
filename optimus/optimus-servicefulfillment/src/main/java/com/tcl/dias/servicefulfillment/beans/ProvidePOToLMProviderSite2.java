package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used for task - Provide PO details to LM Provider for site 2.
 *
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class ProvidePOToLMProviderSite2 extends TaskDetailsBaseBean {

	private String offnetInterStatePoNumber;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String offnetInterStatePoDate;
	private String offnetLocalPoNumber3;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String offnetLocalPoDate3;
	private List<AttachmentIdBean> documentIds;
	private String delayReason;

	public String getOffnetInterStatePoNumber() {
		return offnetInterStatePoNumber;
	}

	public void setOffnetInterStatePoNumber(String offnetInterStatePoNumber) {
		this.offnetInterStatePoNumber = offnetInterStatePoNumber;
	}

	public String getOffnetInterStatePoDate() {
		return offnetInterStatePoDate;
	}

	public void setOffnetInterStatePoDate(String offnetInterStatePoDate) {
		this.offnetInterStatePoDate = offnetInterStatePoDate;
	}

	public String getOffnetLocalPoNumber3() {
		return offnetLocalPoNumber3;
	}

	public void setOffnetLocalPoNumber3(String offnetLocalPoNumber3) {
		this.offnetLocalPoNumber3 = offnetLocalPoNumber3;
	}

	public String getOffnetLocalPoDate3() {
		return offnetLocalPoDate3;
	}

	public void setOffnetLocalPoDate3(String offnetLocalPoDate3) {
		this.offnetLocalPoDate3 = offnetLocalPoDate3;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

}
