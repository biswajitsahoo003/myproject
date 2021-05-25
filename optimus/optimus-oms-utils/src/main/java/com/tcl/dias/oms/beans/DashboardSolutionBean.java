package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is used for dashboard details of solution
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DashboardSolutionBean {
	
	private Integer siteCount;

	private List<DashBoardSiteBean> site;

	/**
	 * @return the siteCount
	 */
	public Integer getSiteCount() {
		return siteCount;
	}

	/**
	 * @param siteCount the siteCount to set
	 */
	public void setSiteCount(Integer siteCount) {
		this.siteCount = siteCount;
	}

	/**
	 * @return the site
	 */
	public List<DashBoardSiteBean> getSite() {
		if(site==null) {
			site=new ArrayList<>();
		}
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(List<DashBoardSiteBean> site) {
		this.site = site;
	}


}
