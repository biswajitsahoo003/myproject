package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class CustomerHandoverBean extends TaskDetailsBaseBean {
    private String customerHandoverCompleted;
    private String customerHandoverCompletedDate;

    public String getCustomerHandoverCompleted() {
        return customerHandoverCompleted;
    }

    public void setCustomerHandoverCompleted(String customerHandoverCompleted) {
        this.customerHandoverCompleted = customerHandoverCompleted;
    }

    public String getCustomerHandoverCompletedDate() {
        return customerHandoverCompletedDate;
    }

    public void setCustomerHandoverCompletedDate(String customerHandoverCompletedDate) {
        this.customerHandoverCompletedDate = customerHandoverCompletedDate;
    }
}
