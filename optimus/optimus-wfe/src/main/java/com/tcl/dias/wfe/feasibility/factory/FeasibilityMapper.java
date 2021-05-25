package com.tcl.dias.wfe.feasibility.factory;

import org.json.simple.JSONArray;

/**
 * Feasibilty Mapper interface is used to trigger the respective product service
 * @author PAULRAJ SUNDAR
 *
 */
public interface FeasibilityMapper {

	void processFeasibilityService(JSONArray request) throws Exception;
}
