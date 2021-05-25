package com.tcl.dias.servicehandover.listener;

import com.tcl.dias.common.beans.ServiceHandoverInputBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicehandover.service.ServiceInventoryHandoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ServiceHandoverDeligateListener for handling handover deligate
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component
public class ServiceHandoverDeligateListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHandoverDeligateListener.class);
	
	@Autowired
	ServiceInventoryHandoverService serviceInventoryHandoverService;
	
	@RabbitListener(queuesToDeclare = {@Queue("${service.handover.deligate.queue}") })
	public void pushToServiceInventory(String request) {
		try {
			LOGGER.error("pushToServiceInventory::{}", request);
			ServiceHandoverInputBean serviceHandoverInput=(ServiceHandoverInputBean) Utils.convertJsonToObject(request, ServiceHandoverInputBean.class);
			if(!serviceHandoverInput.getOrderCode().startsWith("IZOPC") && (serviceHandoverInput.getOrderCode().startsWith("IZO") || serviceHandoverInput.getOrderCode().startsWith("UC"))){
				LOGGER.info("pushToServiceInventory with SDWAN or UCW orderCode::{} and serviceId::{}", serviceHandoverInput.getOrderCode(),serviceHandoverInput.getServiceId());
				serviceInventoryHandoverService.handoverSdwanDetailsToServiceInventory(serviceHandoverInput.getServiceCode());
			}else {
				LOGGER.info("pushToServiceInventory with Other than SDWAN or UCW orderCode::{} and serviceId::{}", serviceHandoverInput.getOrderCode(),serviceHandoverInput.getServiceId());
				serviceInventoryHandoverService.handoverDetailsToServiceInventory(serviceHandoverInput.getOrderCode(), serviceHandoverInput.getServiceCode(), serviceHandoverInput.getServiceId());
			}
		} catch (Exception e) {
			LOGGER.error("Error in pushToServiceInventory", e);
		}
	}

	@RabbitListener(queuesToDeclare = {@Queue("${service.handover.delegate.renewal.noncommercial.queue}") })
	public void pushToServiceInventoryForRenewalWithNonCommercial(String request) {
		try {
			LOGGER.error("pushToServiceInventoryForRenewalWithNonCommercial::{}", request);
			ServiceHandoverInputBean serviceHandoverInput=(ServiceHandoverInputBean) Utils.convertJsonToObject(request, ServiceHandoverInputBean.class);
			if(serviceHandoverInput.getOrderCode().startsWith("IAS") || serviceHandoverInput.getOrderCode().startsWith("GVPN") || serviceHandoverInput.getOrderCode().startsWith("NPL")){
				LOGGER.info("pushToServiceInventoryForRenewalWithNonCommercial with IAS or GVPN or NPL orderCode::{} and serviceId::{}", serviceHandoverInput.getOrderCode(),serviceHandoverInput.getServiceId());
				serviceInventoryHandoverService.handoverDetailsToServiceInventoryForRenewalWithNonCommercial(serviceHandoverInput.getOrderCode(), serviceHandoverInput.getServiceCode(), serviceHandoverInput.getServiceId());
			}
		} catch (Exception e) {
			LOGGER.error("Error in pushToServiceInventoryForRenewalWithNonCommercial", e);
		}
	}
	
	@RabbitListener(queuesToDeclare = {@Queue("${service.handover.delegate.novation.queue}") })
	public void pushToServiceInventoryForNovation(String request) {
		try {
			LOGGER.error("pushToServiceInventoryForNovation::{}", request);
			ServiceHandoverInputBean serviceHandoverInput=(ServiceHandoverInputBean) Utils.convertJsonToObject(request, ServiceHandoverInputBean.class);
			serviceInventoryHandoverService.handoverDetailsToServiceInventoryForNovation(serviceHandoverInput.getOrderCode(), serviceHandoverInput.getServiceCode(), serviceHandoverInput.getServiceId());
		} catch (Exception e) {
			LOGGER.error("Error in pushToServiceInventoryForNovation", e);
		}
	}
}