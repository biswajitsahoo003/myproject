package com.tcl.dias.products.gsc.beans;

import java.util.Set;

/**
 * Product level country list Bean for GSC Products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscProductLocationBean {

	private Set<GscProductLocationDetailBean> sources;
	private Set<GscProductLocationDetailBean> destinations;

	public Set<GscProductLocationDetailBean> getSources() {
		return sources;
	}

	public void setSources(Set<GscProductLocationDetailBean> sources) {
		this.sources = sources;
	}

	public Set<GscProductLocationDetailBean> getDestinations() {
		return destinations;
	}

	public void setDestinations(Set<GscProductLocationDetailBean> destinations) {
		this.destinations = destinations;
	}

	@Override
	public String toString() {
		return "GscProductLocationBean{" +
				"sources=" + sources +
				", destinations=" + destinations +
				'}';
	}
}
