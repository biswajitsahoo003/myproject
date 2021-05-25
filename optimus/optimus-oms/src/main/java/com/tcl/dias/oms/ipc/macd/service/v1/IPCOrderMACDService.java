package com.tcl.dias.oms.ipc.macd.service.v1;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.ipc.beans.IPCOrdersBean;
import com.tcl.dias.oms.ipc.service.v1.IPCOrderService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * This file contains the IPCOrderMACDService.java class. This class contains IPCMACD
 * related functionalities
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 *
 */
@Service
@Transactional
public class IPCOrderMACDService extends IPCOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPCOrderMACDService.class);
    
    @Autowired
    IPCOrderService ipcOrderService;
    
    @Autowired
    IPCQuoteMACDService ipcQuoteMACDService;
    
    @Autowired
    OrderRepository orderRepository;
    
    /**
   	 * getMACDOrderDetails - This method is used to get macd order details 
   	 *
   	 * @param orderId
   	 * @return IPCOrdersBean
   	 * @throws TclCommonException
   	 */
    public IPCOrdersBean getMACDOrderDetails(Integer orderId) throws TclCommonException {
		IPCOrdersBean ordersBean = null;
		try {
			 Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);
	            if (order == null) {
	                throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
	            }
	        ordersBean = ipcOrderService.constructOrder(order, null);
	        ordersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
			ordersBean.setOrderType(order.getOrderToLes().stream().findFirst().get().getOrderType());
			ordersBean.setOrderCategory(order.getOrderToLes().stream().findFirst().get().getOrderCategory());
			String serviceIdVal=order.getQuote().getQuoteToLes().stream().findFirst().get().getErfServiceInventoryTpsServiceId();
			if(Objects.nonNull(serviceIdVal)){
				ordersBean.setIsMacdInitiated(ipcQuoteMACDService.checkMacdInitiatedBasedOnQuoteToLe(order.getQuote().getQuoteToLes().stream().findFirst().get()));
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBean;
	}

}
