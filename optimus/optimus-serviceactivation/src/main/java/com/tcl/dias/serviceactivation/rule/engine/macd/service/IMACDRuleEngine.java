package com.tcl.dias.serviceactivation.rule.engine.macd.service;

import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

public interface IMACDRuleEngine {

	public boolean applyRule(ServiceDetail serviceDetails) throws TclCommonException;

}
