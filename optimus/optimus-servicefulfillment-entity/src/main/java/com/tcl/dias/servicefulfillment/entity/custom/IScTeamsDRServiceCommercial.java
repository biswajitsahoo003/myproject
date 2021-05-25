package com.tcl.dias.servicefulfillment.entity.custom;

/**
 * This file contains the CustomScTeamsDRServiceCommercial.java.
 * @author Syed Ali.
 * @createdAt 29/01/2021, Friday, 17:15
 */
public interface IScTeamsDRServiceCommercial {
	String getComponentName();

	String getComponentDesc();

	String getComponentType();

	String getHsnCode();

	String getVendorName();

	String getRentalMaterialCode();

	String getSaleMaterialCode();

	String getServiceNumber();

	String getShortText();

	Integer getQuantity();

	Double getNrc();

	Double getMrc();

	Double getArc();

	Double getTcv();

	Double getEffectiveUsage();

	Double getEffectiveOverage();
}
