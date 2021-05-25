package com.tcl.dias.location.beans;

import java.util.List;

/**
 * This file contains the solution information for IZOPC
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SolutionBean {
	
	Integer id ;
	
	 List<SiteBean> sites;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<SiteBean> getSites() {
		return sites;
	}

	public void setSites(List<SiteBean> sites) {
		this.sites = sites;
	}


}
