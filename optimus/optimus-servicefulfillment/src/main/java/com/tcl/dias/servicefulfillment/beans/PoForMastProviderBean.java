package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * PoForMastProviderBean -
 *
 * @author Yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PoForMastProviderBean extends TaskDetailsBaseBean {

    private String mastPoNumber;
    
    private String prNumber;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String mastPoDate;

    List<AttachmentIdBean> documentIds;

    public String getMastPoNumber() {
        return mastPoNumber;
    }

    public void setMastPoNumber(String mastPoNumber) {
        this.mastPoNumber = mastPoNumber;
    }

    public String getMastPoDate() {
        return mastPoDate;
    }

    public void setMastPoDate(String mastPoDate) {
        this.mastPoDate = mastPoDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
    
    public String getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}
}
