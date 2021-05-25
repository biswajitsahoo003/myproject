package com.tcl.dias.products.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.gsc.beans.GscSlaBean;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.gsc.service.v1.GscProductServiceMatrixService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Class is related to SLA Consumer for Product
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscProductSlaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscProductSlaConsumer.class);

	@Autowired
	GscProductServiceMatrixService gscProductService;

	/**
	 * Process ProductSLA
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gsi.sla.queue}") })
	public String processSlaDetails(String request) throws TclCommonException {
		String response = "";
		try {
			ProductSlaBean productSlaBean = null;
			productSlaBean = gscProductService.processProductSla(request);
			response = Utils.convertObjectToJson(productSlaBean);
		} catch (Exception e) {
			LOGGER.warn("error in getting sla detailsa details ", e);
		}
		return response;
	}
	
	/**
	 * get ProductSLA
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gsc.sla.queue}") })
	public String getSlaDetails(String request) throws TclCommonException {
		String response = "";
		try {
			GscSlaBeanListener listener = (GscSlaBeanListener)Utils.convertJsonToObject(request,
					GscSlaBeanListener.class);
			List<GscSlaBean> gscSlaBeans = gscProductService.processProductSla(listener);
			
			GscSlaBeanListener gscSlaBeanListener =new GscSlaBeanListener();
			gscSlaBeanListener.setAccessType(listener.getAccessType());
			gscSlaBeanListener.setGscSlaBeans(gscSlaBeans);
			response = Utils.convertObjectToJson(gscSlaBeanListener);
		} catch (Exception e) {
			LOGGER.warn("error in getting sla detailsa details ", e);
		}
		return response;
	}

}
