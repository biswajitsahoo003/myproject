package com.tcl.dias.beans;

import java.util.List;

/**
 * This file contains the MTTR trend data.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MTTRTrendResponseBean {

	private List<MTTRTrendInfo> trendInfo;

	/**
	 * @return the trendInfo
	 */
	public List<MTTRTrendInfo> getTrendInfo() {
		return trendInfo;
	}

	/**
	 * @param trendInfo the trendInfo to set
	 */
	public void setTrendInfo(List<MTTRTrendInfo> trendInfo) {
		this.trendInfo = trendInfo;
	}

	
	
}
