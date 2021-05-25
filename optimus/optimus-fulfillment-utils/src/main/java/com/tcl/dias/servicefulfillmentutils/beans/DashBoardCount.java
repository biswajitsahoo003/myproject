package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;
import java.util.Map;

public class DashBoardCount {

	private List<Map<String, String>> stageCount;
	private Map<String, String> orderCount;

	private List<Map<String, String>> productWiseCount;
	
	private List<Map<String, String>> lastMileCount;

	private List<Map<String, String>> activeServiceRecordCount;

	private Map<String, String> activeRecordsCount;

	private Integer totalBilledCircuits;

	private Integer totalDeliveredCircuit;

	public List<Map<String, String>> getLastMileCount() {
		return lastMileCount;
	}

	public void setLastMileCount(List<Map<String, String>> lastMileCount) {
		this.lastMileCount = lastMileCount;
	}

	public List<Map<String, String>> getProductWiseCount() {
		return productWiseCount;
	}

	public void setProductWiseCount(List<Map<String, String>> productWiseCount) {
		this.productWiseCount = productWiseCount;
	}

	public List<Map<String, String>> getStageCount() {
		return stageCount;
	}

	public void setStageCount(List<Map<String, String>> stageCount) {
		this.stageCount = stageCount;
	}

	public Map<String, String> getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Map<String, String> orderCount) {
		this.orderCount = orderCount;
	}

	public List<Map<String, String>> getActiveServiceRecordCount() { return activeServiceRecordCount; }

	public void setActiveServiceRecordCount(List<Map<String, String>> activeServiceRecordCount) { this.activeServiceRecordCount = activeServiceRecordCount; }

	public Map<String, String> getActiveRecordsCount() {
		return activeRecordsCount;
	}

	public void setActiveRecordsCount(Map<String, String> activeRecordsCount) {
		this.activeRecordsCount = activeRecordsCount;
	}

	public Integer getTotalBilledCircuits() {
		return totalBilledCircuits;
	}

	public void setTotalBilledCircuits(Integer totalBilledCircuits) {
		this.totalBilledCircuits = totalBilledCircuits;
	}

	public Integer getTotalDeliveredCircuit() {
		return totalDeliveredCircuit;
	}

	public void setTotalDeliveredCircuit(Integer totalDeliveredCircuit) {
		this.totalDeliveredCircuit = totalDeliveredCircuit;
	}
}
