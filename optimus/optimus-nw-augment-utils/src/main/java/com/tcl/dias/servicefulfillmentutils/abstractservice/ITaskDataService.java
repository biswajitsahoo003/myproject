package com.tcl.dias.servicefulfillmentutils.abstractservice;

import java.util.Map;

import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited ITaskDataService .class is used
 *            for abstract data methods
 */

public interface ITaskDataService {

	public Map<String, Object> getTaskData(Task task)throws TclCommonException;
	
	public Map<String, Object> getTaskData(Task task,String siteType)throws TclCommonException;


}
