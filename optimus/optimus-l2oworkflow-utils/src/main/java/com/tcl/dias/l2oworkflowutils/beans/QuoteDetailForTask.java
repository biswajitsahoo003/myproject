package com.tcl.dias.l2oworkflowutils.beans;

import java.util.ArrayList;

/**
 * 
 * Bean to hold Quote details
 * @author krutsrin
 *
 */
public class QuoteDetailForTask {
	
	private String quotecode;
	private Integer quoteId;
	private String sitecode;
	private Integer siteId;
	private String feasibilityId;
	private Integer aEndResponseId;
	private Integer bEndResponseId;
	private boolean bEndSystemFeasible;
	private boolean aEndSystemFeasible;
	private Integer mfDetailIdForSystemFeasibleEnd;
	private String columnName;
	private Object columnValue;
	private Integer linkId;
	private String type;
	
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getMfDetailIdForSystemFeasibleEnd() {
		return mfDetailIdForSystemFeasibleEnd;
	}
	public void setMfDetailIdForSystemFeasibleEnd(Integer mfDetailIdForSystemFeasibleEnd) {
		this.mfDetailIdForSystemFeasibleEnd = mfDetailIdForSystemFeasibleEnd;
	}
	public boolean isbEndSystemFeasible() {
		return bEndSystemFeasible;
	}
	public void setbEndSystemFeasible(boolean bEndSystemFeasible) {
		this.bEndSystemFeasible = bEndSystemFeasible;
	}
	public boolean isaEndSystemFeasible() {
		return aEndSystemFeasible;
	}
	public void setaEndSystemFeasible(boolean aEndSystemFeasible) {
		this.aEndSystemFeasible = aEndSystemFeasible;
	}
	
	public Integer getaEndResponseId() {
		return aEndResponseId;
	}
	public void setaEndResponseId(Integer aEndResponseId) {
		this.aEndResponseId = aEndResponseId;
	}
	public Integer getbEndResponseId() {
		return bEndResponseId;
	}
	public void setbEndResponseId(Integer bEndResponseId) {
		this.bEndResponseId = bEndResponseId;
	}

	public Object getColumnValue() {
		return columnValue;
	}
	public void setColumnValue(Object columnValue) {
		this.columnValue = columnValue;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	private Integer mfDetailId;
	public Integer getMfDetailId() {
		return mfDetailId;
	}
	public void setMfDetailId(Integer mfDetailId) {
		this.mfDetailId = mfDetailId;
	}
	
	public ArrayList<MfDetailManualUpdateBean> getListofFields() {
		return listofFields;
	}
	public void setListofFields(ArrayList<MfDetailManualUpdateBean> listofFields) {
		this.listofFields = listofFields;
	}
	private ArrayList<MfDetailManualUpdateBean> listofFields;
	
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getQuotecode() {
		return quotecode;
	}
	public void setQuotecode(String quotecode) {
		this.quotecode = quotecode;
	}
	
	public String getSitecode() {
		return sitecode;
	}
	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}
	
	public String getFeasibilityId() {
		return feasibilityId;
	}
	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}
	

}
