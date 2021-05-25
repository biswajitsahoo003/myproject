package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

/**
 * Bean class to store organisation list response data from Tiger service API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrganisationListResponse extends TigerServiceResponse {
	private Integer startIndex;
	private Integer endIndex;
	private Integer totalCount;
	private List<Organisation> organizations;

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

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<Organisation> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organisation> organizations) {
		this.organizations = organizations;
	}
}
