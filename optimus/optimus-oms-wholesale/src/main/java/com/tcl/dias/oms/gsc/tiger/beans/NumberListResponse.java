package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

/**
 * Bean class to store number list response from Tiger service API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NumberListResponse extends TigerServiceResponse {
	private Integer totalCount;
	private Integer startIndex;
	private Integer endIndex;
	private List<NumberDetails> numInv;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public List<NumberDetails> getNumInv() {
		return numInv;
	}

	public void setNumInv(List<NumberDetails> numInv) {
		this.numInv = numInv;
	}
}
