package com.tcl.dias.sfdc.mapper;

import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the ISfdcMapper.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface ISfdcMapper {
	
	BaseBean transfortToSfdcRequest(String omsInput)throws TclCommonException;

}
