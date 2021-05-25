package com.tcl.dias.products.webex.beans;

import java.util.Set;

/**
 * Product level country list Bean for Webex Products
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexProductLocationBean {

	private Set<WebexProductLocationDetailBean> sources;
	private Set<WebexProductLocationDetailBean> destinations;

	public Set<WebexProductLocationDetailBean> getSources() {
		return sources;
	}

	public void setSources(Set<WebexProductLocationDetailBean> sources) {
		this.sources = sources;
	}

	public Set<WebexProductLocationDetailBean> getDestinations() {
		return destinations;
	}

	public void setDestinations(Set<WebexProductLocationDetailBean> destinations) {
		this.destinations = destinations;
	}
}
