package com.tcl.dias.common.sfdc.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductServicesQueryBean {
	
	private List<ProductServicesRecord> record;
	
	private String totalSize;
	
	private String done;

	/**
	 * @return the record
	 */
	public List<ProductServicesRecord> getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(List<ProductServicesRecord> record) {
		this.record = record;
	}

	/**
	 * @return the totalSize
	 */
	public String getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the done
	 */
	public String getDone() {
		return done;
	}

	/**
	 * @param done the done to set
	 */
	public void setDone(String done) {
		this.done = done;
	}

	@Override
	public String toString() {
		return "ProductServicesQueryBean [record=" + record + ", totalSize=" + totalSize + ", done=" + done + "]";
	}
	
	
	
	
	
	

}
