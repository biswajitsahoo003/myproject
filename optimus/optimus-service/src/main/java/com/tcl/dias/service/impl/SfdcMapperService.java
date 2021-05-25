package com.tcl.dias.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.SfdcOpportunityRequest;
import com.tcl.dias.sfdc.factory.SfdcMapperFactory;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;

/**
 * 
 * @author MRajakum
 *
 */
@Service
public class SfdcMapperService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SfdcMapperService.class);

	@Autowired
	private SfdcMapperFactory factory;

	/**
	 * getMappedReqest - get Mapped Request
	 * 
	 * @param input
	 * @param mapperType
	 * @return
	 */
	public String getMappedReqest(String input, String mapperType) {
		String request = "";
		try {
			ISfdcMapper mapper = factory.getInstanceMapper(mapperType);
			if (mapper != null) {
				Object sfdcRequest = mapper.transfortToSfdcRequest(input);
				request = Utils.convertObjectToJson(sfdcRequest);
			} else {
				request = "Invalid Mapper Type";
			}
		} catch (Exception e) {
			LOGGER.error("Error in get Mapped Request", e);
			request = "Error in Parsing Request";
		}
		return request;
	}

}
