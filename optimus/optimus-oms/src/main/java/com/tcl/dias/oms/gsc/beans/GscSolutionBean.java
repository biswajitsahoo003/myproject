package com.tcl.dias.oms.gsc.beans;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Product solution information related to Order
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscSolutionBean {

	private Integer solutionId;
	private String offeringName;
	private String solutionCode;
	private String accessType;
	private String productName;
	private List<GscQuoteBean> gscQuotes;
	private String rateColumn;
	private String paymentCurrency;
	private String billingCurrency;

	public static GscOrderSolutionBean fromGscSolutionBean(GscSolutionBean gscSolutionBean) {
		GscOrderSolutionBean gscOrderSolutionBean = new GscOrderSolutionBean();
		gscOrderSolutionBean.setOfferingName(gscSolutionBean.getOfferingName());
		gscOrderSolutionBean.setGscOrders(gscSolutionBean.getGscQuotes().stream().map(GscQuoteBean::fromGscQuoteBean)
				.collect(Collectors.toList()));
		return gscOrderSolutionBean;
	}

	public Integer getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getSolutionCode() {
		return solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<GscQuoteBean> getGscQuotes() {
		return gscQuotes;
	}

	public void setGscQuotes(List<GscQuoteBean> gscQuotes) {
		this.gscQuotes = gscQuotes;
	}

	public String getRateColumn() {
		return rateColumn;
	}

	public void setRateColumn(String rateColumn) {
		this.rateColumn = rateColumn;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public String getBillingCurrency() {
		return billingCurrency;
	}

	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}

	@Override
	public String toString() {
		return "GscSolutionBean{" +
				"solutionId=" + solutionId +
				", offeringName='" + offeringName + '\'' +
				", solutionCode='" + solutionCode + '\'' +
				", accessType='" + accessType + '\'' +
				", productName='" + productName + '\'' +
				", gscQuotes=" + gscQuotes +
				", rateColumn='" + rateColumn + '\'' +
				'}';
	}
}
