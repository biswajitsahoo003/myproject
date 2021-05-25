package com.tcl.dias.serviceactivation.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * Tx Jeopardy bean class.
 */
public class TxJeopardyBean extends BaseRequest {

    private String action;
    private String remarks;
    private String delayReason;
    
    private String owner;
    private String reason;
    private String txResourcePathType;
    private String internalCablingRequiredAt;
    private Boolean isTxDowntimeReqd;
    
    

    public Boolean getIsTxDowntimeReqd() {
		return isTxDowntimeReqd;
	}

	public void setIsTxDowntimeReqd(Boolean isTxDowntimeReqd) {
		this.isTxDowntimeReqd = isTxDowntimeReqd;
	}

	public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelayReason() {
        return delayReason;
    }

    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTxResourcePathType() {
		return txResourcePathType;
	}
	
	public void setTxResourcePathType(String txResourcePathType) {
		this.txResourcePathType = txResourcePathType;
	}

	public String getInternalCablingRequiredAt() {
		return internalCablingRequiredAt;
	}

	public void setInternalCablingRequiredAt(String internalCablingRequiredAt) {
		this.internalCablingRequiredAt = internalCablingRequiredAt;
	}
  
}
