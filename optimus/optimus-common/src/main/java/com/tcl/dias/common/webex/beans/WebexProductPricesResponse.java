package com.tcl.dias.common.webex.beans;

import java.util.List;

/**
 * This Class contains product prices related fields.
 *
 * @author S.Syed Ali
 * @link http://www.tatacommunications.com/ 
 * @copyright 2019 Tata Communications Limited
 */
public class WebexProductPricesResponse {
	private WebexProductPricesRequest pricesRequest;
	private List<WebexProductPricesBean> productPricesList;

	public WebexProductPricesResponse() {
	}

	public WebexProductPricesRequest getPricesRequest() {
		return pricesRequest;
	}

	public void setPricesRequest(WebexProductPricesRequest pricesRequest) {
		this.pricesRequest = pricesRequest;
	}

	public List<WebexProductPricesBean> getProductPricesList() {
		return productPricesList;
	}

	public void setProductPricesList(List<WebexProductPricesBean> productPricesList) {
		this.productPricesList = productPricesList;
	}
}
