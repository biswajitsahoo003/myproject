package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

import java.sql.Timestamp;
import java.util.List;

public class CimHoldRequest extends BaseRequest {

    private String action;
    private String holdReason;
    private String isCancellation;
    private String pmEmailId;
    private String salesSupportEmailId;
    private String salesPersonEmailId;
    private List<AttachmentIdBean> documentIds;
    private boolean isDateAvailable;
    private String tentativeDateforHold;
    private boolean isDeferredDelivery;
    private String onHoldCategory;
    private String rrfsDate;
    private Timestamp crfsDate;
    
    
    

    /**
	 * @return the crfsDate
	 */
	public Timestamp getCrfsDate() {
		return crfsDate;
	}

	/**
	 * @param crfsDate the crfsDate to set
	 */
	public void setCrfsDate(Timestamp crfsDate) {
		this.crfsDate = crfsDate;
	}

	public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

	public String getHoldReason() {
		return holdReason;
	}

	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}

    public String getIsCancellation() {
        return isCancellation;
    }

    public void setIsCancellation(String isCancellation) {
        this.isCancellation = isCancellation;
    }

    public String getPmEmailId() {
        return pmEmailId;
    }

    public void setPmEmailId(String pmEmailId) {
        this.pmEmailId = pmEmailId;
    }

    public String getSalesSupportEmailId() {
        return salesSupportEmailId;
    }

    public void setSalesSupportEmailId(String salesSupportEmailId) {
        this.salesSupportEmailId = salesSupportEmailId;
    }

    public String getSalesPersonEmailId() {
        return salesPersonEmailId;
    }

    public void setSalesPersonEmailId(String salesPersonEmailId) {
        this.salesPersonEmailId = salesPersonEmailId;
    }
    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

	public boolean isDateAvailable() {
		return isDateAvailable;
	}

	public void setDateAvailable(boolean isDateAvailable) {
		this.isDateAvailable = isDateAvailable;
	}

	public String getTentativeDateforHold() {
		return tentativeDateforHold;
	}

	public void setTentativeDateforHold(String tentativeDateforHold) {
		this.tentativeDateforHold = tentativeDateforHold;
	}

	public boolean isIsDeferredDelivery() {
		return isDeferredDelivery;
	}

	public void setDeferredDelivery(boolean isDeferredDelivery) {
		this.isDeferredDelivery = isDeferredDelivery;
	}

	public String getOnHoldCategory() {
		return onHoldCategory;
	}

	public void setOnHoldCategory(String onHoldCategory) {
		this.onHoldCategory = onHoldCategory;
	}

    public String getRrfsDate() {
        return rrfsDate;
    }

    public void setRrfsDate(String rrfsDate) {
        this.rrfsDate = rrfsDate;
    }
	
    
}
