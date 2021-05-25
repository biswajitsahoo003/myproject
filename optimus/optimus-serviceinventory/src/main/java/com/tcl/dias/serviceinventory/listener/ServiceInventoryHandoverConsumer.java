package com.tcl.dias.serviceinventory.listener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.tcl.dias.common.serviceinventory.bean.ScSolutionComponentBean;
import com.tcl.dias.common.serviceinventory.bean.SdwanScOrderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.serviceinventory.bean.ScOrderBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.service.v1.ServiceInventoryHelperService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.util.CollectionUtils;


/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to assign the task details
 */

@Service
public class ServiceInventoryHandoverConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryHandoverConsumer.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${service.handover.complete.queue}")
	String serviceHandoverCompleteQueue;

	@Autowired
	ServiceInventoryHelperService serviceInventoryHelperService;

	@Transactional(readOnly = false)
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.service.handover.inventory}")})
	public void saveServiceInventoryData(String responseBody) throws TclCommonException {
		LOGGER.info("Enter getServiceInventory {} :",responseBody);

		try {
			ScOrderBean scOrderBean = Utils.convertJsonToObject(responseBody,ScOrderBean.class);
			serviceInventoryHelperService.processServiceInventoryData(scOrderBean);

			ScServiceDetailBean scServiceDetailBean = scOrderBean.getScServiceDetails().stream().findFirst().orElseGet(null);

			List<String> filters = Arrays.asList(scServiceDetailBean.getUuid(),String.valueOf(scServiceDetailBean.getId()));

			String result = filters.stream().collect(Collectors.joining(","));

			if(scServiceDetailBean!=null){
				String response = (String)mqUtils.sendAndReceive(serviceHandoverCompleteQueue, result);
				LOGGER.info("ServiceInventory Handover Consumer : {}",  response);
			}

		} catch (Exception e) {
			LOGGER.error("Error while pushing from Handover to Inventory for Service Details {} and error {} ", responseBody,e);
		}
	}

	@Transactional(readOnly = false)
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.service.handover.inventory.sdwan}")})
	public void processSdwanInventoryHandoverDetails(String responseBody) throws TclCommonException {
		try{
			LOGGER.info("processSdwanInventoryHandoverDetails::{}",responseBody);
			SdwanScOrderBean sdwanScOrderBean = Utils.convertJsonToObject(responseBody,SdwanScOrderBean.class);
			if(Objects.nonNull(sdwanScOrderBean)){
				serviceInventoryHelperService.processSdwanInventoryData(sdwanScOrderBean);
			}
		}catch(Exception ex){
			LOGGER.error("Error occured while processing Sdwan Inventory Data with exception {}", ex.getMessage());
		}
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED,readOnly=false)
	@RabbitListener(queuesToDeclare = { @Queue("${queue.rfinventory}") })
	public void processRftoInventory(String responseBody) throws TclCommonException{
	   try {
		   LOGGER.info("processRftoInventory",responseBody);
		   if((Objects.nonNull(responseBody)) && !org.springframework.util.StringUtils.isEmpty(responseBody)) {
			   serviceInventoryHelperService.saveRfData(
					   Utils.convertJsonToObject(responseBody, ScServiceDetailBean.class));
			   LOGGER.info("ServiceInventory Handover Consumer for RF Inventory", responseBody);
		   }
	   } catch (Exception e) {
	      LOGGER.error("Error in Save to RF inventory Queue ", e);
	   }
	}


}
