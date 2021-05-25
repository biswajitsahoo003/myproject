package com.tcl.dias.servicehandover.beans.renewal;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

import java.util.List;

public class RenewalCommercialChargeLineItemBean extends BaseRequest {

    private List<RenewalChargeLineItemBean> renewableChargeLineItemBeans;

    public List<RenewalChargeLineItemBean> getRenewableChargeLineItemBeans() {
        return renewableChargeLineItemBeans;
    }

    public void setRenewableChargeLineItemBeans(List<RenewalChargeLineItemBean> renewableChargeLineItemBeans) {
        this.renewableChargeLineItemBeans = renewableChargeLineItemBeans;
    }
}
