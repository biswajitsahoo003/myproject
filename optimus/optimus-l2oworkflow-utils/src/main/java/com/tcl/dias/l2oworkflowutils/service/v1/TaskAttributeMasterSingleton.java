package com.tcl.dias.l2oworkflowutils.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflow.entity.repository.MstTaskAttributeRepository;
import com.tcl.dias.l2oworkflowutils.beans.TaskAttributesBean;

/**
 * This file contains the TaskAttributeMasterSingleton.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class TaskAttributeMasterSingleton {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskAttributeMasterSingleton.class);

	private Map<String, List<TaskAttributesBean>> taskAttributes = new HashMap<>();

	/**
	 * 
	 * @param mstTaskAttributeRepository
	 */
	@Autowired
	public TaskAttributeMasterSingleton(MstTaskAttributeRepository mstTaskAttributeRepository) {
		LOGGER.info("Initializing the task Attribute Master for the first time !!!");
		List<Map<String, Object>> mstTaskAttributes = mstTaskAttributeRepository.findAllTaskAttributes();
		for (Map<String, Object> mstTaskAttribute : mstTaskAttributes) {
			String attributeName = (String) mstTaskAttribute.get("attribute_name");
			String category = (String) mstTaskAttribute.get("category");
			String nodeName = (String) mstTaskAttribute.get("node_name");
			String taskName = (String) mstTaskAttribute.get("mst_task_def_key");
			String subCategory = (String) mstTaskAttribute.get("sub_category");
			TaskAttributesBean taskAttributesBean = new TaskAttributesBean();
			taskAttributesBean.setAttributeName(attributeName);
			taskAttributesBean.setCategory(category);
			taskAttributesBean.setNodeName(nodeName);
			taskAttributesBean.setTaskName(taskName);
			taskAttributesBean.setSubCategory(subCategory);
			if (taskAttributes.get(taskName) == null) {
				List<TaskAttributesBean> taskAttrBeans = new ArrayList<>();
				taskAttrBeans.add(taskAttributesBean);
				taskAttributes.put(taskName, taskAttrBeans);
			} else {
				List<TaskAttributesBean> taskAttrBeans = taskAttributes.get(taskName);
				taskAttrBeans.add(taskAttributesBean);
			}
		}
		LOGGER.info("Initilized the task Attribute Master with total tasks as {} !!!", getTaskAttributes().size());
	}

	/**
	 * @return the taskAttributes
	 */
	public Map<String, List<TaskAttributesBean>> getTaskAttributes() {
		return taskAttributes;
	}

	/**
	 * @param taskAttributes the taskAttributes to set
	 */
	public void setTaskAttributes(Map<String, List<TaskAttributesBean>> taskAttributes) {
		this.taskAttributes = taskAttributes;
	}

	/**
	 * getTaskArrtribute
	 */
	public List<TaskAttributesBean> getTaskAttribute(String taskName) {
		return taskAttributes.get(taskName);

	}

}
