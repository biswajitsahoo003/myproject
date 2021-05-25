package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

/**
 * Bean will have TX details.
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 *
 */
public class TxDetailsBean {

    private List<NodeToConfigureBean> nodesToConfigure;

    public List<NodeToConfigureBean> getNodesToConfigure() {
        return nodesToConfigure;
    }

    public void setNodesToConfigure(List<NodeToConfigureBean> nodesToConfigure) {
        this.nodesToConfigure = nodesToConfigure;
    }
}
