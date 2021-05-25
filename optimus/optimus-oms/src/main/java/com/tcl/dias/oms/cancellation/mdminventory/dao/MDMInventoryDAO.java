package com.tcl.dias.oms.cancellation.mdminventory.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

public interface MDMInventoryDAO {
	
	public MDMServiceInventoryBean getInventoryDetails(Integer page, Integer size, Integer customerId, Integer customerLeId, Integer opportunityId, String orderId, String serviceId, String status, String customerName) throws TclCommonException;
	
	public List<Map<String, Object>>  getPosServiceDetailByOrderCodeAndServiceId(String orderCode, List<String> serviceIds) throws TclCommonException;

}
