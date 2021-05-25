package com.tcl.dias.servicehandover.network.listener;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicehandover.service.BillingProductTerminationService;

@Component
public class ServiceTerminationGeneva {
private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTerminationGeneva.class);
	
	@Autowired
	BillingProductTerminationService billingProductTerminationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	/**
	 * Listener for ServiceTermination in Geneva
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.geneva.service.terminate}") }) 
	public String ServiceTerminationGenevaSync(String request) {
		LOGGER.info("inside ServiceTerminationGenevaSync");
		String status="";
		try {
			boolean isParallelUpgrade = false;
			Integer parallelDays=0;
			String req = request;
			String orderCode = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId= req.split("#")[3];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("ServiceTerminationGenevaSync orderId{} serviceCode{} serviceType{}",orderCode,serviceCode,serviceType);
			ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail.get(), scServiceDetail.get().getScOrder());

			if (scOrder != null && "MACD".equalsIgnoreCase(orderType)&& Objects.nonNull(scServiceDetail.get().getOrderSubCategory())&& scServiceDetail.get().getOrderSubCategory().toLowerCase().contains("parallel")) {
				LOGGER.info("Parallel Flow");
				if (Objects.nonNull(scServiceDetail.get().getParentUuid())) {
					serviceCode = scServiceDetail.get().getParentUuid();
				}
				ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.get().getId(),
								"downtime_duration");
				if (Objects.nonNull(scServiceDownTimeAttr)
						&& StringUtils.isNotEmpty(scServiceDownTimeAttr.getAttributeValue())) {
					LOGGER.info("ParallelDownTime Days");
					parallelDays= Integer.parseInt(scServiceDownTimeAttr.getAttributeValue());
					isParallelUpgrade = true;
				} 
			}
			status = billingProductTerminationService.serviceTerminationGeneva(orderCode,processInstanceId,serviceCode,serviceType,serviceId,parallelDays,isParallelUpgrade);
			if (status != null) {
				
				LOGGER.info("ServiceTerminationGenevaSync Status {} ", status);
				return status;
			}
		} catch (Exception e) {
			LOGGER.error("Error in LrServiceTerminationSync", e);
		}
		LOGGER.info("ServiceTerminationGenevaSync completed with status:  {} ",status);
		return status;
	}
}
