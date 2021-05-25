package com.tcl.dias.oms.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.service.OmsCustomerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Listener Class for fetching Customer details using a queue call
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * @author SURUCHIA
 */

@Service
@Transactional
public class OmsCustomerListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsCustomerListener.class);

	
	
	@Autowired
	OmsCustomerService omsCustomerService;

	/**
	 * ProcessCustomer Information- This listener method is used for accessing customer details  
	 * 
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.customer.queue}") })
	public String processOmsCustomerInformation(String responseBody) throws TclCommonException {
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			List<Integer> cusErfIds = new ArrayList<>();
			
			String[] ids = responseBody.toString().split(",");
			for (String id : ids) {
				cusErfIds.add(Integer.parseInt(id));
			}
			
			
			return Utils.convertObjectToJson(omsCustomerService.getOmsCustomerDetails(cusErfIds));
		} catch (Exception e) {
			LOGGER.error("Error in processing oms customer information ", e);
		}

		return "";

	}
}
