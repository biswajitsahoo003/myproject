package com.tcl.dias.l2oworkflowutils.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.l2oworkflowutils.beans.TaskAttributesBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the AttributeServiceImpl.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class AttributeServiceImpl implements AttributeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttributeServiceImpl.class);

	@Autowired
	TaskAttributeMasterSingleton taskAttributeMasterSingleton;

	/**
	 * getTaskAttributes - This Method takes the taskId and give the attributes
	 * 
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public Map<String, Object> getTaskAttributes(String taskName, Integer serviceId) throws TclCommonException {
		LOGGER.info("Entering task Attributes for taskName {}, serviceId{}",taskName, serviceId);
		Map<String, Object> categoryMapper = new HashMap<String, Object>();
		List<TaskAttributesBean> taskAttributes = taskAttributeMasterSingleton.getTaskAttribute(taskName);
		taskAttributes = (taskAttributes == null) ? new ArrayList<>() : taskAttributes;
		taskAttributes.addAll(taskAttributeMasterSingleton.getTaskAttribute(null));
		return categoryMapper;
	}

	@Override
	public Map<String, Object> getTaskAttributes(String taskName, Integer serviceId,
			AttributeManipulator attributeManipulator) throws TclCommonException {
		LOGGER.info("Entering task Attributes with manipulator");
		return attributeManipulator.manipulate(getTaskAttributes(taskName, serviceId));
	}

	

	

}
