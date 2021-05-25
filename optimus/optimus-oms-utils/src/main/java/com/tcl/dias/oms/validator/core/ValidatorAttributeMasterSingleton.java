package com.tcl.dias.oms.validator.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.oms.entity.repository.MstValidatorAttributeRepository;
import com.tcl.dias.oms.validator.beans.ValidatorAttributeBean;

/**
 * ValidatorAttributeMasterSingleton
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class ValidatorAttributeMasterSingleton {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorAttributeMasterSingleton.class);

	private Map<String, List<ValidatorAttributeBean>> validatorAttributes = new HashMap<>();

	/**
	 * 
	 * @param mstValidatorAttributeRepository
	 */
	@Autowired
	public ValidatorAttributeMasterSingleton(MstValidatorAttributeRepository mstValidatorAttributeRepository) {
		LOGGER.info("Initializing the validator Attribute Master for the first time !!!");
		List<Map<String, Object>> mstValidatorAttributes = mstValidatorAttributeRepository.findAllValidatorAttributes();
		for (Map<String, Object> mstValidatorAttribute : mstValidatorAttributes) {
			String nodeName = (String) mstValidatorAttribute.get("node_name");
			String type = (String) mstValidatorAttribute.get("type");
			String xPath = (String) mstValidatorAttribute.get("x_path");
			String productName = (String) mstValidatorAttribute.get("name");
			ValidatorAttributeBean validatorAttributesBean = new ValidatorAttributeBean();
			validatorAttributesBean.setNodeName(nodeName);
			validatorAttributesBean.setProductName(productName);
			validatorAttributesBean.setType(type);
			validatorAttributesBean.setxPath(xPath);
			if (validatorAttributes.get(productName) == null) {
				List<ValidatorAttributeBean> validatorAttrBeans = new ArrayList<>();
				validatorAttrBeans.add(validatorAttributesBean);
				validatorAttributes.put(productName, validatorAttrBeans);
			} else {
				List<ValidatorAttributeBean> validatorAttrBeans = validatorAttributes.get(productName);
				validatorAttrBeans.add(validatorAttributesBean);
			}
		}
		LOGGER.info("Initilized the validator Attribute Master with total tasks as {} !!!",
				getValidatorAttributes().size());
	}

	/**
	 * 
	 * getValidatorAttributes
	 * 
	 * @return
	 */
	public Map<String, List<ValidatorAttributeBean>> getValidatorAttributes() {
		return validatorAttributes;
	}

	/**
	 * 
	 * setValidatorAttributes
	 * 
	 * @param taskAttributes
	 */
	public void setValidatorAttributes(Map<String, List<ValidatorAttributeBean>> validatorAttr) {
		this.validatorAttributes = validatorAttr;
	}

	/**
	 * 
	 * getValidatorAttribute
	 * 
	 * @param taskName
	 * @return
	 */
	public List<ValidatorAttributeBean> getValidatorAttribute(String productName) {
		return validatorAttributes.get(productName);

	}

}
