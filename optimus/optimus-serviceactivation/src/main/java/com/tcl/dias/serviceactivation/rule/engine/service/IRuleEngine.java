package com.tcl.dias.serviceactivation.rule.engine.service;

import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

public interface IRuleEngine {

	public boolean applyRule(ServiceDetail serviceDetails) throws TclCommonException;

}
