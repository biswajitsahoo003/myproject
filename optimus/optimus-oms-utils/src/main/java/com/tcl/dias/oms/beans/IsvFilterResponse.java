package com.tcl.dias.oms.beans;

import java.util.List;

import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.oms.dto.MstProductFamilyDto;

/**
 * This file contains the IsvFilterRequest.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class IsvFilterResponse {
	
	List<CustomerLeBean> customerLegalEntityList;
	List<String> statusList;
	List<MstProductFamilyDto> productsList;
	
	
	/**
	 * @return the customerLegalEntityList
	 */
	public List<CustomerLeBean> getCustomerLegalEntityList() {
		return customerLegalEntityList;
	}
	/**
	 * @param customerLegalEntityList the customerLegalEntityList to set
	 */
	public void setCustomerLegalEntityList(List<CustomerLeBean> customerLegalEntityList) {
		this.customerLegalEntityList = customerLegalEntityList;
	}
	/**
	 * @return the statusList
	 */
	public List<String> getStatusList() {
		return statusList;
	}
	/**
	 * @param statusList the statusList to set
	 */
	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}
	/**
	 * @return the productsList
	 */
	public List<MstProductFamilyDto> getProductsList() {
		return productsList;
	}
	/**
	 * @param productsList the productsList to set
	 */
	public void setProductsList(List<MstProductFamilyDto> productsList) {
		this.productsList = productsList;
	}
	
	
	

}
