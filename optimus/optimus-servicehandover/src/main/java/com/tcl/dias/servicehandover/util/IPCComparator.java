package com.tcl.dias.servicehandover.util;

import java.util.Comparator;

import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;


/**
 * 
 * Implementing Comparator based on product type.
 * 
 * @author arjayapa
 *
 */
public class IPCComparator implements Comparator<ScProductDetail> {	
	
	@Override
	public int compare(ScProductDetail prd1, ScProductDetail prd2) {
		
		if(priority(prd1.getType()) > priority(prd2.getType()))
			return 1;
		else if (priority(prd1.getType()) < priority(prd2.getType()))
			return -1;
		else 
			return 0;
	}
	
	private int priority(String prd) {
		
		if(prd.equals("CLOUD"))
			return 0;
		else if(prd.equals("ACCESS"))
			return 1;
		else
			return 2;
	}

}
