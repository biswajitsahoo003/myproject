package com.tcl.dias.servicefulfillment.entity.custom;

/**
 * 
 * This file contains the IScWebexServiceCommercial.java.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public interface IScWebexServiceCommercial {
	
	String getComponentName();

	String getComponentDesc();

	String getComponentType();

	String getHsnCode();

	String getRentalMaterialCode();

	String getSaleMaterialCode();

	String getServiceNumber();

	String getShortText();

	Integer getQuantity();

	Double getCiscoUnitListPrice();

	Double getCiscoDiscntPrct();

	Double getCiscoUnitNetPrice();
	
	String getContractType();
	
	String getSupportType();
}
