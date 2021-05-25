package com.tcl.dias.servicefulfillmentutils.factory;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface IProjectWorkFlowHandler {

	public Boolean processStagePlanStart(DelegateExecution execution, Expression preceders) throws TclCommonException;

	public Boolean processStagePlanCompletion(DelegateExecution execution, Expression preceders)throws TclCommonException;

	public Boolean processPlan(DelegateExecution execution, Expression preceders)throws TclCommonException;

	public Boolean processPlanCompletion(DelegateExecution execution, Expression preceders)throws TclCommonException;

	public Boolean processActivityPlan(DelegateExecution execution, Expression preceders)throws TclCommonException;

	public Boolean processActivityPlanCompletion(DelegateExecution execution, Expression preceders)throws TclCommonException;

	public Boolean processTaskPlan(DelegateExecution execution, Expression preceders)throws TclCommonException;

	

}
