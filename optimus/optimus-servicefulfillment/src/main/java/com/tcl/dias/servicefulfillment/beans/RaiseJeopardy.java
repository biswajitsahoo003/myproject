package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used for task - Raise Jeopardy.
 *
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class RaiseJeopardy extends TaskDetailsBaseBean {

    private String jeopardyReason;
    private String comment;

    public String getJeopardyReason() {
        return jeopardyReason;
    }

    public void setJeopardyReason(String jeopardyReason) {
        this.jeopardyReason = jeopardyReason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
