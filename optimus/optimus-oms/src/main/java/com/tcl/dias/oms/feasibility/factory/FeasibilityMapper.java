package com.tcl.dias.oms.feasibility.factory;

import java.util.Map;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * Feasibilty Mapper interface is used to trigger the respective product service
 * @author PAULRAJ SUNDAR
 *
 */
public interface FeasibilityMapper {

	void processFeasibilityResponse(String data) throws TclCommonException;
	
	void processErrorFeasibilityResponse(Map<String, String> errorResponse) throws TclCommonException;
	
	
}
