package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * Bean class for Update Dependency Remarks
 *
 * @author
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class UpdateDependencyRemarksBean extends BaseRequest{
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
