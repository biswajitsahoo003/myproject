package com.tcl.dias.serviceactivation.rule.engine.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.rule.engine.macd.service.GVPNMACDRuleEngineService;
import com.tcl.dias.serviceactivation.rule.engine.macd.service.IASMACDRuleEngineService;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class RuleEngineService {

	@Autowired
	IASRuleEngineService aASRuleEngineService;
	
	@Autowired
	IASMACDRuleEngineService iasMACDRuleEngineService;
	
	@Autowired
	GVPNMACDRuleEngineService gvpnMACDRuleEngineService;

	@Autowired
	GVPNRuleEngineService gvPnRuleEngineService;

	@Autowired
	ServiceDetailRepository serviceDetailRepository;

	@Transactional(readOnly=false, isolation = Isolation.READ_UNCOMMITTED)
	public boolean applyRule(String serviceCode) throws TclCommonException {
		ServiceDetail serviceDetails = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode,TaskStatusConstants.ISSUED);		
		String type = serviceDetails.getServiceOrderType();
		OrderDetail orderDetail=serviceDetails.getOrderDetail();
		String orderCategory=com.tcl.dias.serviceactivation.activation.utils.OrderCategoryMapping.getOrderCategory(serviceDetails, orderDetail);
		String orderSubCategory=serviceDetails.getOrderSubCategory();
		orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
		orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
		if (type.equalsIgnoreCase("ILL") || type.equalsIgnoreCase("IAS") 
				|| ((type.equalsIgnoreCase("ILL_MACD") || type.equalsIgnoreCase("IAS_MACD")) && Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel"))) {
			return aASRuleEngineService.applyRule(serviceDetails);
		} else if ((type.equalsIgnoreCase("GVPN")) || (Objects.nonNull(orderCategory) && "ADD_SITE".equals(orderCategory))
				|| (type.equalsIgnoreCase("GVPN_MACD") && Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel"))) {
			return gvPnRuleEngineService.applyRule(serviceDetails);
		}else if (type.equalsIgnoreCase("ILL_MACD") || type.equalsIgnoreCase("IAS_MACD")) {
			return iasMACDRuleEngineService.applyRule(serviceDetails);
		}else if (type.equalsIgnoreCase("GVPN_MACD")) {
			return gvpnMACDRuleEngineService.applyRule(serviceDetails);
		}
		return false;

	}

}
