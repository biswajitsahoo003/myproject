package com.tcl.dias.l2oworkflowutils.abstractservice;

import java.util.Map;

import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited ITaskDataService .class is used
 *            for abstract data methods
 */
public interface ITaskDataService {

	public Map<String, Object> getTaskData(Task task)throws TclCommonException;

}
