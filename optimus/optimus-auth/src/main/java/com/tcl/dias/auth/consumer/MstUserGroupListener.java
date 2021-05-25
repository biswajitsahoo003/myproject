package com.tcl.dias.auth.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.auth.beans.UserBean;
import com.tcl.dias.auth.service.UserService;
import com.tcl.dias.common.utils.Utils;

/**
 * Contains all listeners related to workflow
 *
 * @author KRUTHIKA S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class MstUserGroupListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(MstUserGroupListener.class);

	@Autowired
	UserService userService;

	@RabbitListener(queuesToDeclare = { @Queue("${queue.get.mstUserGroups}") })
	public String getMstUserGroups(String request) {
		try {
			String groupNameToSearch = Utils.fromJson(request, new TypeReference<String>() {
			});

			List<UserBean> responseBean = userService.getUserGroupList(groupNameToSearch);

			List<String> response = new ArrayList<String>();
			if (!responseBean.isEmpty())
				response.addAll(responseBean.stream().map(bean -> bean.getEmailId()).collect(Collectors.toList()));
			return Utils.convertObjectToJson(response);

		} catch (Exception e) {
			LOGGER.warn("Error in getting mst user group details", e);
		}
		return "";
	}
}
