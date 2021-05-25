package com.tcl.dias.common.webex.beans;

import java.util.List;

/**
 * This Class Contains Product related fields.
 *
 * @author S.Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexProductPricesBean {
	private String productName;
	private String paymentModel;
	private List<?> productPrices;

	public WebexProductPricesBean() {
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<?> getProductPrices() {
		return productPrices;
	}

	public void setProductPrices(List<?> productPrices) {
		this.productPrices = productPrices;
	}

	public String getPaymentModel() {
		return paymentModel;
	}

	public void setPaymentModel(String paymentModel) {
		this.paymentModel = paymentModel;
	}
}
