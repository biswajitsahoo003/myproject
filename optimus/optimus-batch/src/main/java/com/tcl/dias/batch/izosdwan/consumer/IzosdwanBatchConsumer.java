package com.tcl.dias.batch.izosdwan.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.batch.izosdwan.service.v1.IzosdwanBatchService;
import com.tcl.dias.batch.odr.listener.OmsOdrListener;
import com.tcl.dias.common.beans.ByonBulkUploadDetail;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * 
 * This is the consumer class for IZOSDWAN in batch
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class IzosdwanBatchConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanBatchConsumer.class);
	
	@Autowired
	IzosdwanBatchService izosdwanBatchService;
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.izosdwan.update.byon}") })
	public void processOrderFlat(Message<String> responseBody) throws TclCommonException {
		try {
			String request= responseBody.getPayload();
			LOGGER.info("Request from location {}",request);
			if(request!=null) {
				Map<Integer,List<ByonBulkUploadDetail>> byonBulkUploadDetails = IzosdwanUtils.fromJson(request,
						new TypeReference<Map<Integer,List<ByonBulkUploadDetail>>>() {
						});
				byonBulkUploadDetails.forEach((k,v)->{
					izosdwanBatchService.updateLocationDetailsForByonInOms(v);
				});
				
			}
		} catch (Exception e) {
			LOGGER.error("error in getting odr details ", e);
		}
	}

}
