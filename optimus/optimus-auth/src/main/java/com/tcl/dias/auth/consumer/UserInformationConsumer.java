package com.tcl.dias.auth.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.tcl.dias.auth.constants.ExceptionConstants;
import com.tcl.dias.auth.service.UserInformationAuthService;
import com.tcl.dias.auth.service.UserService;
import com.tcl.dias.common.beans.UserProductsBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the UserInformationConsumer.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class UserInformationConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserInformationConsumer.class);

	@Autowired
	private UserInformationAuthService userInformationAuthService;

	@Autowired
	UserService userService;

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.userinfo.name}") })
	public String processUserInfoService(Message<String> responseBody) {
		try {
			LOGGER.info("Request Received for refresh {} ", responseBody.getPayload());
			@SuppressWarnings("unchecked")
			Map<String, String> responseMapper = (Map<String, String>) Utils
					.convertJsonToObject(responseBody.getPayload(), Map.class);
			userInformationAuthService.persistToRedisV1(responseMapper.get("username"), responseMapper.get("token"),
					null);
			return "true";
		} catch (Exception e) {
			LOGGER.warn("Error in persisting {}", ExceptionUtils.getStackTrace(e));
		}
		return "false";
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.userinfo.get}") })
	public String processGetUserInfoService(Message<String> responseBody) {
		try {
			return Utils.convertObjectToJson(userInformationAuthService.getUserInformation(responseBody.getPayload()));
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_PROCESS_GET_USERINFO, responseBody, ExceptionUtils.getStackTrace(e));
		}
		return "";
	}
	
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.engagementInfo.get}")})
	public String getEngagementInfo(String request) throws TclCommonException {
		try {
			LOGGER.info("inside getEngagementInfo method");
			List<UserProductsBean> prodBean=userInformationAuthService.getUserEngagements();
			LOGGER.info("getEngagementInfo prodBean{}",prodBean.toString());
			return Utils.convertObjectToJson(prodBean);
		}catch (Exception e) {
			LOGGER.error("Unable to get interconnect details...", ExceptionUtils.getStackTrace(e));
		}
		return "";
		
	}
}
