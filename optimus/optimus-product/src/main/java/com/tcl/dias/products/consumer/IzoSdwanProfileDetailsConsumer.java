package com.tcl.dias.products.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.CpeRequestBean;
import com.tcl.dias.common.beans.IzoSdwanCpeDetails;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.izosdwan.service.v1.IzosdwanProductService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class IzoSdwanProfileDetailsConsumer {

	@Autowired
	IzosdwanProductService izoSdwanProductService;

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanProfileDetailsConsumer.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.get.profile.details}") })
	public String getProfileDetails(String request)  throws AmqpRejectAndDontRequeueException{
		String response = "";
		try {
			String vendorName = Utils.convertJsonToObject(request, String.class);
			LOGGER.info("Data sent to the queue call is {}", vendorName);
			List<VendorProfileDetailsBean> vendorDet = izoSdwanProductService
					.getProductOfferingsForSdwanBasedOnVendor(vendorName);
			LOGGER.info("response of the queue call is {}",vendorDet);
			response = Utils.convertObjectToJson(vendorDet);
			LOGGER.info("Vendor profile details after object to json: {}", response);
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			LOGGER.info("Error in the profile fetching queue call{}",e);
			e.printStackTrace();
		}

		return response;
	}

}
