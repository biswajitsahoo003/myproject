package com.tcl.dias.products.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.IzoSdwanCpeBomInterface;
import com.tcl.dias.common.beans.IzosdwanBandwidthInterface;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.izosdwan.service.v1.IzosdwanProductService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author mpalanis
 *
 */
@Service
@Transactional
public class IzoSdwanCpeInterface {
	@Autowired
	IzosdwanProductService izosdwanProductService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanCpeInterface.class);
	
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.product.izosdwan.cpeInterfacedetails}")})
	public String getCpeBomInterface(String request) throws TclCommonException {
		String response = "";
		try {

			String hi = Utils.convertJsonToObject(request, String.class);
			List<IzoSdwanCpeBomInterface> InterfaceDet = izosdwanProductService.getCpeBomInterface();
			response = Utils.convertObjectToJson(InterfaceDet);
			LOGGER.info("Izosdwan CPE bom interface details after object to json: {}", response);

		} catch (Exception e) {

			e.printStackTrace();

		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.product.byon.interface}")})
	public String getInterfaceDetailsForByon(String request) {
		String response="";
		try {
			List<IzosdwanBandwidthInterface> interfaceType=izosdwanProductService.getbwinterfaceTypes(request);
			response=Utils.convertObjectToJson(interfaceType);
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}

}
