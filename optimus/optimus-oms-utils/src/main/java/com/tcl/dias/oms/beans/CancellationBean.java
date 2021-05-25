package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tcl.dias.common.beans.MDMServiceDetailBean;

public class CancellationBean implements Serializable {
	private static final long serialVersionUID = 1L;
	 	List<MDMServiceDetailBean> serviceDetailBeanForCancellation;
	    List<String> siteCodes;
	    boolean isQuoteCreated ;
	    boolean isO2c;
	    private int quoteId;
	    private int quoteToLeId;
	    private String parentOrderCode;    
	    private Date quoteCreatedDate;
	    private String quoteCode;
	    private boolean isOrderCreated;
	    
	    
		
		public List<MDMServiceDetailBean> getServiceDetailBeanForCancellation() {
			return serviceDetailBeanForCancellation;
		}
		public void setServiceDetailBeanForCancellation(List<MDMServiceDetailBean> serviceDetailBeanForCancellation) {
			this.serviceDetailBeanForCancellation = serviceDetailBeanForCancellation;
		}
		
		public List<String> getSiteCodes() {
			return siteCodes;
		}
		public void setSiteCodes(List<String> siteCodes) {
			this.siteCodes = siteCodes;
		}
		
		public boolean isQuoteCreated() {
			return isQuoteCreated;
		}
		public void setQuoteCreated(boolean isQuoteCreated) {
			this.isQuoteCreated = isQuoteCreated;
		}
		public boolean isO2c() {
			return isO2c;
		}
		public void setO2c(boolean isO2c) {
			this.isO2c = isO2c;
		}
		public int getQuoteId() {
			return quoteId;
		}
		public void setQuoteId(int quoteId) {
			this.quoteId = quoteId;
		}
		public int getQuoteToLeId() {
			return quoteToLeId;
		}
		public void setQuoteToLeId(int quoteToLeId) {
			this.quoteToLeId = quoteToLeId;
		}
		public String getParentOrderCode() {
			return parentOrderCode;
		}
		public void setParentOrderCode(String parentOrderCode) {
			this.parentOrderCode = parentOrderCode;
		}
		public Date getQuoteCreatedDate() {
			return quoteCreatedDate;
		}
		public void setQuoteCreatedDate(Date quoteCreatedDate) {
			this.quoteCreatedDate = quoteCreatedDate;
		}
		
		public String getQuoteCode() {
			return quoteCode;
		}
		public void setQuoteCode(String quoteCode) {
			this.quoteCode = quoteCode;
		}
		
		public boolean isOrderCreated() {
			return isOrderCreated;
		}
		public void setOrderCreated(boolean isOrderCreated) {
			this.isOrderCreated = isOrderCreated;
		}
		
		@Override
		public String toString() {
			return "CancellationBean [serviceDetailBeanForCancellation=" + serviceDetailBeanForCancellation
					+ ", siteCodes=" + siteCodes + ", isQuoteCreated=" + isQuoteCreated + ", isO2c=" + isO2c
					+ ", quoteId=" + quoteId + ", quoteToLeId=" + quoteToLeId + ", parentOrderCode=" + parentOrderCode
					+ ", quoteCreatedDate=" + quoteCreatedDate + ", quoteCode=" + quoteCode + ", isOrderCreated="
					+ isOrderCreated + "]";
		}

}
