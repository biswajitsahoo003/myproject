package com.tcl.dias.oms.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.OmsLeAttributeBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * This file contains the OmsLeAttributeListener class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class OmsLeAttributeListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsLeAttributeListener.class);
	
	@Autowired
	GvpnQuoteService gvpnQuoteService;
	
	@RabbitListener(queuesToDeclare = { @Queue("${oms.billing.attribute.update}") })
	public String processOmsLeAttributeUpdate(String responseBody) throws TclCommonException {
		String response="";
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			
			OmsLeAttributeBean omsLeAttributeBean = (OmsLeAttributeBean) Utils.convertJsonToObject(responseBody,
					OmsLeAttributeBean.class);
			/**since billing attribute is updating by quoteToLeId ,therfore methos is
			 *  in gvpnQuoteService which will work for other product also.*/
            	omsLeAttributeBean=gvpnQuoteService.updateLegalEntityBillingProperties(omsLeAttributeBean);
            	if (omsLeAttributeBean!=null)
            		response=Utils.convertObjectToJson(omsLeAttributeBean);
			
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
		return response;
	}
}
