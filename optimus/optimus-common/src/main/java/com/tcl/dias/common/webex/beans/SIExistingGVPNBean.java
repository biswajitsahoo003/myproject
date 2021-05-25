package com.tcl.dias.common.webex.beans;

import java.util.List;

import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoGVPNBean;

/**
 * Bean for existing gvpn details from service inventory
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SIExistingGVPNBean {
	List<String> cities;
	List<String> alias;
	String customerId;
	Integer page;
	Integer size;
	List<SIServiceInfoGVPNBean> serviceInfos;
	Integer totalItems;
	Integer totalPages;
	String status;
	String message;

	public SIExistingGVPNBean() {
	}

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}

	public List<String> getAlias() {
		return alias;
	}

	public void setAlias(List<String> alias) {
		this.alias = alias;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public List<SIServiceInfoGVPNBean> getServiceInfos() {
		return serviceInfos;
	}

	public void setServiceInfos(List<SIServiceInfoGVPNBean> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * toString() method for the bean
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "SIExistingGVPNBean{" + "cities=" + cities + "alias=" + alias + "customerId='" + customerId + '\''
				+ ", page=" + page + ", size=" + size + ", serviceInfos=" + serviceInfos + ", totalItems=" + totalItems
				+ ", totalPages=" + totalPages + ", status='" + status + '\'' + ", message='" + message + '\'' + '}';
	}
}
