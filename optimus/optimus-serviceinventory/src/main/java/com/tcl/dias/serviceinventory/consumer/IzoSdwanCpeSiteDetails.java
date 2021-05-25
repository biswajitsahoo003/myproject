package com.tcl.dias.serviceinventory.consumer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.IzoSdwanSiteDetails;
import com.tcl.dias.common.beans.SiSearchBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.service.v1.IzosdwanServiceInventoryService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the consumer calls for returning Inventory based details.
 * 
 * @author vpachava
 *
 */

@Service
@Transactional
public class IzoSdwanCpeSiteDetails {

	@Autowired
	IzosdwanServiceInventoryService izosdwanServiceInventoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanCpeSiteDetails.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.izosdwan.cpeportdetails}") })

	public String getCpePortDetails(String request) throws TclCommonException {
		String response = "";
		try {

			String hi = Utils.convertJsonToObject(request, String.class);
			List<IzoSdwanSiteDetails> portDet = izosdwanServiceInventoryService.getCpePortDetails();
			response = Utils.convertObjectToJson(portDet);

		} catch (Exception e) {

			e.printStackTrace();

		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.service.get.selected.site.details}") })
	public String getSelectedSiteDetails(String request) {
		String response = "";
		try {
			SiSearchBean siSearchBean = Utils.convertJsonToObject(request, SiSearchBean.class);
			Integer count = izosdwanServiceInventoryService.getSelectedProfileData(siSearchBean);
			response = Utils.convertObjectToJson(count);
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.port_mode.detail}") })
	public String getPortModeDetail(String id) throws TclCommonException {
		String response = "";
		try {
			response = izosdwanServiceInventoryService.getPortMode(id);

		} catch (Exception e) {

		}
		return response;
	}
	


}
