/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.serviceactivation.activation.utils;

import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;

/**
 * @author vivek
 *
 */
public class OrderCategoryMapping {

	public static String getOrderType(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		if (serviceDetail != null && serviceDetail.getOrderType() != null) {
			return serviceDetail.getOrderType();
		} else {
			return orderDetail.getOrderType();
		}
	}

	public static String getOrderCategory(ServiceDetail serviceDetail, OrderDetail orderDetail) {
		if (serviceDetail != null && serviceDetail.getOrderCategory() != null) {
			return serviceDetail.getOrderCategory();

		} else {
			return orderDetail.getOrderCategory();
		}
	}

}
