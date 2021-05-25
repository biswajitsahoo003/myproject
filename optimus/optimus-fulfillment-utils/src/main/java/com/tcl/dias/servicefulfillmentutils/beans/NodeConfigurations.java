package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

/**
 * NodeConfigurations.class
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 *
 */
public class NodeConfigurations {

    private List<NodeToConfigureBean> nodeToBeConfigured;

	public List<NodeToConfigureBean> getNodeToBeConfigured() {
		return nodeToBeConfigured;
	}

	public void setNodeToBeConfigured(List<NodeToConfigureBean> nodeToBeConfigured) {
		this.nodeToBeConfigured = nodeToBeConfigured;
	}
}
