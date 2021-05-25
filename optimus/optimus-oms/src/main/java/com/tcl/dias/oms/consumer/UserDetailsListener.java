package com.tcl.dias.oms.consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class UserDetailsListener {
	


    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierLeListener.class);

    @Autowired
    UserService userService;
    


    @Transactional
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.user.details.queue}")})
	public String getUserDetails(String responseBody) throws TclCommonException {
		LOGGER.info("Enter getUserDetails method with groupname{} :",responseBody);

		try {
			String groupName=responseBody;
			UserGroupBeans response=	userService.getUserDetailsByGroupName(groupName);
			LOGGER.info("get the response from user service{} :",responseBody);

			return Utils.convertObjectToJson(response);
		} catch (Exception e) {
			LOGGER.error("Error in getUserDetails Repsonse ", e);
		}
		return "";
	}



}
