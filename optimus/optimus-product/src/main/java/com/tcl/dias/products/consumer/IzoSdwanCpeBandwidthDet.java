package com.tcl.dias.products.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.CpeRequestBean;
import com.tcl.dias.common.beans.IzoSdwanCpeDetails;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.izosdwan.service.v1.IzosdwanProductService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author vpachava
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */

@Service
@Transactional
public class IzoSdwanCpeBandwidthDet {

	@Autowired
	IzosdwanProductService izoSdwanProductService;

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanCpeBandwidthDet.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.izosdwan.cpedetails}") })
	public String getCpeBomDetails(String request) throws TclCommonException {
		String response = "";
		try {

			CpeRequestBean list =  Utils
					.convertJsonToObject((String) request, CpeRequestBean.class);
			LOGGER.info("Details in product micro Service {}", list.getProfileName());
			List<IzoSdwanCpeDetails> cpeDet = izoSdwanProductService.getCpeDetails(list);
			response = Utils.convertObjectToJson(cpeDet);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return response;
	}

}
