package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class TestNumberWithRoutingBean {
    private String tollfree;
    private Integer tollfreeId;
    private List<RoutingNumberBean> routingNumberBeans;
    private String customerOutpulse;
    private Integer customerOutpulseId;
    private String billingStartDate;
    private String cdrLog;
    private String reservationId;
    private Boolean isReserved;

    public String getTollfree() {
        return tollfree;
    }

    public void setTollfree(String tollfree) {
        this.tollfree = tollfree;
    }

    public List<RoutingNumberBean> getRoutingNumberBeans() {
        return routingNumberBeans;
    }

    public void setRoutingNumberBeans(List<RoutingNumberBean> routingNumberBeans) {
        this.routingNumberBeans = routingNumberBeans;
    }

    public String getCustomerOutpulse() {
        return customerOutpulse;
    }

    public void setCustomerOutpulse(String customerOutpulse) {
        this.customerOutpulse = customerOutpulse;
    }

    public String getBillingStartDate() {
        return billingStartDate;
    }

    public void setBillingStartDate(String billingStartDate) {
        this.billingStartDate = billingStartDate;
    }

    public String getCdrLog() {
        return cdrLog;
    }

    public void setCdrLog(String cdrLog) {
        this.cdrLog = cdrLog;
    }

    public Integer getTollfreeId() {
        return tollfreeId;
    }

    public void setTollfreeId(Integer tollfreeId) {
        this.tollfreeId = tollfreeId;
    }

    public Integer getCustomerOutpulseId() {
        return customerOutpulseId;
    }

    public void setCustomerOutpulseId(Integer customerOutpulseId) {
        this.customerOutpulseId = customerOutpulseId;
    }

	public String getReservationId() {
		return reservationId;
	}

	public Boolean getIsReserved() {
		return isReserved;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public void setIsReserved(Boolean isReserved) {
		this.isReserved = isReserved;
	}
}
