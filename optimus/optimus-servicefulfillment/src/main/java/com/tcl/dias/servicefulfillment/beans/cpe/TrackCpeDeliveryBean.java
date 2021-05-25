package com.tcl.dias.servicefulfillment.beans.cpe;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class TrackCpeDeliveryBean extends TaskDetailsBaseBean {
    private String status;
    
    private String cpeDeliveredDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCpeDeliveredDate() {
		return cpeDeliveredDate;
	}

	public void setCpeDeliveredDate(String cpeDeliveredDate) {
		this.cpeDeliveredDate = cpeDeliveredDate;
	}

	@Override
    public String toString() {
        return "TrackCpeDeliveryBean{" +
                "status='" + status + '\'' +
                "cpeDeliveredDate='" + cpeDeliveredDate + '\'' +
                '}';
    }
}
