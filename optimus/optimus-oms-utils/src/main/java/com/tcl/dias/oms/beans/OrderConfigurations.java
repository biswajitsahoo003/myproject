package com.tcl.dias.oms.beans;

import java.util.List;

/**
 * Bean file
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderConfigurations {

	private List<OrderConfiguration> orders;

	private Integer totalActiveOrders;

	private Integer totalActiveSites;

	public List<OrderConfiguration> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderConfiguration> orders) {
		this.orders = orders;
	}

	public Integer getTotalActiveOrders() {
		return totalActiveOrders;
	}

	public void setTotalActiveOrders(Integer totalActiveOrders) {
		this.totalActiveOrders = totalActiveOrders;
	}

	public Integer getTotalActiveSites() {
		return totalActiveSites;
	}

	public void setTotalActiveSites(Integer totalActiveSites) {
		this.totalActiveSites = totalActiveSites;
	}

	@Override
	public String toString() {
		return "OrderConfigurations [orders=" + orders + ", totalActiveOrders=" + totalActiveOrders
				+ ", totalActiveSites=" + totalActiveSites + "]";
	}

}
