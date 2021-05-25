package com.tcl.dias.servicehandover.network.listener;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicehandover.bean.lr.termination.RequestResponse;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.service.BillingProductTerminationService;

@Component
public class ServiceTerminationLR {
private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTerminationLR.class);
	
	@Autowired
	BillingProductTerminationService billingProductTerminationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	/**
	 * Listener for ServiceTermination in Lr
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.lr.service.terminate}") }) 
	public String LrServiceTerminationSync(String request) {
		LOGGER.info("inside LrServiceTerminationSync");
		String status=NetworkConstants.FAILURE;
		try {
			String req = request;
			String orderCode = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId= req.split("#")[3];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("LrServiceTerminationSync orderId{} serviceCode{} serviceType{}",orderCode,serviceCode,serviceType);
			ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail.get(), scServiceDetail.get().getScOrder());

			if (scOrder != null && "MACD".equalsIgnoreCase(orderType)&& Objects.nonNull(scServiceDetail.get().getOrderSubCategory())&& scServiceDetail.get().getOrderSubCategory().toLowerCase().contains("parallel")) {
				LOGGER.info("Parallel Flow");
				if (Objects.nonNull(scServiceDetail.get().getParentUuid())) {
					serviceCode = scServiceDetail.get().getParentUuid();
				}
			}
			
			RequestResponse requestResponse = billingProductTerminationService.serviceTerminationLR(orderCode,processInstanceId,serviceCode,serviceType,serviceId);
			if (requestResponse != null) {
				status = requestResponse.getOptimusTerminationResponse().getStatus();
				return status;
			}
		} catch (Exception e) {
			LOGGER.error("Error in LrServiceTerminationSync", e);
		}
		LOGGER.info("LrServiceTermination completed with status:  {} ",status);
		return status;
	}
}
