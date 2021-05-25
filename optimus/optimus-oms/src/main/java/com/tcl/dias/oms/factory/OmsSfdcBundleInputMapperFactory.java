package com.tcl.dias.oms.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.oms.factory.impl.OmsSfdcSdwanMapper;
import com.tcl.dias.oms.sfdc.core.OmsSfdcBundleInputHandler;

/**
 * 
 * @author vpachava
 *
 */
@Component
public class OmsSfdcBundleInputMapperFactory {

	@Autowired
	private OmsSfdcSdwanMapper izosdwanMapper;

	public OmsSfdcBundleInputHandler getInstance(String type) {
		OmsSfdcBundleInputHandler instance = null;
		switch (type) {
		case "IZOSDWAN":
			return izosdwanMapper;

		default:
			return instance;
		}
	}
}
