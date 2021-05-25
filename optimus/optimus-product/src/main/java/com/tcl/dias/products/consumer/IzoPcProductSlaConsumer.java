package com.tcl.dias.products.consumer;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.IzopcDcRequestBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.izopc.service.v1.IZOPCProductService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Consumer class to provide sla details for IZOPC
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class IzoPcProductSlaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoPcProductSlaConsumer.class);
	

	@Autowired
	IZOPCProductService izoPcProductService;
	

	/**
	 * processSlaDetails- Method to find sla for IZOPC
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.izopc.sla.queue}") })
	public String processSlaDetails(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for sla with tier {}", request);
			response = Utils.convertObjectToJson(izoPcProductService.getSlaDetails().stream().collect(Collectors.toSet()));
		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.provider.name}") })
	public String getProviderName(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for provider  {}", request);
			response = (String) izoPcProductService.getCloudProviderAlias(request);
		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.izopc.dc}") })
	public String getDcInterconnectPoint(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for provider  {}", request);
			IzopcDcRequestBean izopcDcRequestBean = Utils.convertJsonToObject(request, IzopcDcRequestBean.class);
			response = izoPcProductService.getDcInterconnectCodeFromPopIdAndCloudProvider(izopcDcRequestBean);
		} catch (Exception e) {

			LOGGER.error("error in getting sla detailsa details ", e);
		}
		return response;
	}
}
