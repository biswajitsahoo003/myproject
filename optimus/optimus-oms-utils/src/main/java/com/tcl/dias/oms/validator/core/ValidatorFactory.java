package com.tcl.dias.oms.validator.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.oms.validator.beans.ValidatorAttributeBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the ValidatorFactory.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class ValidatorFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorFactory.class);

	@Autowired
	ValidatorAttributeMasterSingleton validatorAttributeMasterSingleton;

	public List<ValidatorAttributeBean> getValidatorAttributes(String productName, String type)
			throws TclCommonException {
		List<ValidatorAttributeBean> productValidatorAttributes=new ArrayList<>();
		LOGGER.info("Entering validator Attributes for productName {},", productName);
		List<ValidatorAttributeBean> validatorAttributes = validatorAttributeMasterSingleton
				.getValidatorAttribute(productName);
		if (validatorAttributes == null) {
			validatorAttributes = new ArrayList<>();
		} else {
			for (ValidatorAttributeBean validatorAttributeBean : validatorAttributes) {
				if (validatorAttributeBean.getType().equals(type)) {
					productValidatorAttributes.add(validatorAttributeBean);
				}
			}
		}
		return validatorAttributes;
	}

}
