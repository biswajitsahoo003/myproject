package com.tcl.dias.servicefulfillment.beans;


import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used for task - Provide PO details to LM Provider.
 *
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ProvidePOToLMProvider extends TaskDetailsBaseBean {

    private String offnetInterStatePoNumber;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String offnetInterStatePoDate;
    private String offnetLocalPoNumber;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String offnetLocalPoDate;
    private List<AttachmentIdBean> documentIds;
    private String supplierCAFNumber;
    private String suplierOrderId;
    private String suplierCircuitId;
    private String delayReason;
    

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    public String getSupplierCAFNumber() {
        return supplierCAFNumber;
    }

    public void setSupplierCAFNumber(String supplierCAFNumber) {
        this.supplierCAFNumber = supplierCAFNumber;
    }

	public String getSuplierOrderId() {
		return suplierOrderId;
	}

	public void setSuplierOrderId(String suplierOrderId) {
		this.suplierOrderId = suplierOrderId;
	}

	public String getSuplierCircuitId() {
		return suplierCircuitId;
	}

	public void setSuplierCircuitId(String suplierCircuitId) {
		this.suplierCircuitId = suplierCircuitId;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

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

	public String getOffnetLocalPoNumber() {
		return offnetLocalPoNumber;
	}

	public void setOffnetLocalPoNumber(String offnetLocalPoNumber) {
		this.offnetLocalPoNumber = offnetLocalPoNumber;
	}

	public String getOffnetLocalPoDate() {
		return offnetLocalPoDate;
	}

	public void setOffnetLocalPoDate(String offnetLocalPoDate) {
		this.offnetLocalPoDate = offnetLocalPoDate;
	}
	
	
}
