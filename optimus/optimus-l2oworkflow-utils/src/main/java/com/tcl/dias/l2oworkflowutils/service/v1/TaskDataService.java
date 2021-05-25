package com.tcl.dias.l2oworkflowutils.service.v1;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflowutils.abstractservice.ITaskDataService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited TaskDataService is used to get
 * the task details
 */

@Service
@Transactional(readOnly = true)
public class TaskDataService implements ITaskDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDataService.class);


    @Autowired
    AttributeService attributeService;


	@Override
	public Map<String, Object> getTaskData(Task task) throws TclCommonException{
		LOGGER.info("get Task Data method started with Key {} ", task.getMstTaskDef().getKey());
		Integer serviceId = task.getServiceId();
		Map<String, Object> varMap = null;
		Map<String, Object> taskDataMap = attributeService.getTaskAttributes(task.getMstTaskDef().getKey(), serviceId);
		LOGGER.info("Variable map has values ; {} ", varMap);
		taskDataMap.put("systemDate", new Date());
		return taskDataMap;
	}
    

}
