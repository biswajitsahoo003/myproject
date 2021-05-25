package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * Advanced Enrichment Media Gateway
 *
 * @author srraghav
 */
public class AdvancedEnrichmentMediaGatewayBean extends TaskDetailsBaseBean {
	private List<AdvancedEnrichmentAttributes> advancedEnrichmentAttributes;

	public AdvancedEnrichmentMediaGatewayBean() {
	}

	public List<AdvancedEnrichmentAttributes> getAdvancedEnrichmentAttributes() {
		return advancedEnrichmentAttributes;
	}

	public void setAdvancedEnrichmentAttributes(List<AdvancedEnrichmentAttributes> advancedEnrichmentAttributes) {
		this.advancedEnrichmentAttributes = advancedEnrichmentAttributes;
	}
}
