package com.tcl.dias.tiger.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.gsc.beans.EnterpriseTigerNotificationBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.service.impl.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Listener for tiger notification on failure
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class TigerJobListener {

	@Autowired
	private NotificationService notificationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TigerJobListener.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.tiger.error.queue}") })
	public void getTigerJobDetails(String responseBody) {
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException("COMMON_ERROR", ResponseResource.R_CODE_BAD_REQUEST);
			}
			EnterpriseTigerNotificationBean enterpriseTigerNotificationBeanRequest = (EnterpriseTigerNotificationBean) Utils
					.convertJsonToObject(responseBody, EnterpriseTigerNotificationBean.class);
			notificationService.notifyTigerError(enterpriseTigerNotificationBeanRequest);
		} catch (Exception e) {
			LOGGER.warn("Error in CreateSiteLocation", e);
		}
	}
}
