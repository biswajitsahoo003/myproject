package com.tcl.dias.servicefulfillmentutils.beans.teamsdr;

import java.util.Map;

/**
 * Batch details bean
 */
public class TeamsDRBatchDetails {
	private Integer batchId;
	private Map<String, Object> batchAttributes;

	public TeamsDRBatchDetails() {
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Map<String, Object> getBatchAttributes() {
		return batchAttributes;
	}

	public void setBatchAttributes(Map<String, Object> batchAttributes) {
		this.batchAttributes = batchAttributes;
	}
}
