package com.tcl.dias.products.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.VproxyAttributeDetails;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.izosdwan.service.v1.IzosdwanProductService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author vpachava
 *
 */
@Service
@Transactional
public class IzoSdwanVproxyProfileConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanVproxyProfileConsumer.class);
	
	@Autowired
	IzosdwanProductService izoSdwanProductService;
	
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.get.vproxy.profile.details}") })
	public String getVproxyProductDetails(String request) throws TclCommonException, JsonParseException, JsonMappingException, IOException {
		String response="";
		try {
			
			LOGGER.info("Data sent to the queue call is {}");
			ObjectMapper mapper=new ObjectMapper();
			List<VproxyAttributeDetails> attributeDetails=mapper.readValue(request, new TypeReference<List<VproxyAttributeDetails>>() {
			});
		//	List<VproxyAttributeDetails> attributeDetails= Utils.convertJsonToObject(request, List.class);
			LOGGER.info("THE MESSAGE IS QUEEUE IS {}",attributeDetails);
			List<VproxySolutionsBean> vproxyDetails=izoSdwanProductService.getVproxyProductDetails(attributeDetails);
			LOGGER.info("THE Response of service IS {}",vproxyDetails);
			response=Utils.convertObjectToJson(vproxyDetails);
			LOGGER.info("THE Response sending in queue call {}",response);
		} catch ( TclCommonException e) {
			LOGGER.info("Error in getting vproxy details");
			e.printStackTrace();
		}
		return response;
	}

}
