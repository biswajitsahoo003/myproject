/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;

/**
 * @author vivek
 *
 */
public class OrderCategoryMapping {

	public static String getOrderCategory(String orderCategory, String orderSubcategory) {
		if (orderSubcategory != null && orderCategory != null && orderCategory.equalsIgnoreCase("CHANGE_ORDER")
				&& (orderSubcategory.toLowerCase().contains("cos") || orderSubcategory.equalsIgnoreCase("IP REMOVAL"))) {
			return "CHANGE_BANDWIDTH";
		} else {
			return orderCategory;
		}
	}

	public static String getOrderSubCategory(String orderCategory, String orderSubcategory) {
		if (orderSubcategory != null && orderCategory != null && ((orderCategory.equalsIgnoreCase("CHANGE_ORDER") || orderCategory.equalsIgnoreCase("CHANGE_BANDWIDTH")))
				&& (orderSubcategory.toLowerCase().contains("cos") || orderSubcategory.equalsIgnoreCase("IP REMOVAL") )) {
			return "HOT UPGRADE";
		} else {
			return orderSubcategory;
		}
	}

	public static String getOrderSubCategory(String orderSubcategory) {
		if (orderSubcategory != null && (orderSubcategory.toLowerCase().contains("cos") || orderSubcategory.equalsIgnoreCase("IP REMOVAL"))) {
			return "HOT UPGRADE";
		} else {
			return orderSubcategory;
		}
	}
	
	public static String getOrderType(ScServiceDetail scServiceDetail,ScOrder scOrder) {
		if (scServiceDetail != null && scServiceDetail.getOrderType()!=null) {
			return scServiceDetail.getOrderType();
		} else {
			return scOrder.getOrderType();
		}
	}
	
	public static String getOrderCategory(ScServiceDetail scServiceDetail,ScOrder scOrder) {
		if (scServiceDetail != null && scServiceDetail.getOrderCategory()!=null) {
			return scServiceDetail.getOrderCategory();
			
		} else {
			return scOrder.getOrderCategory();
		}
	}
	
	public static String getTerminationType(ScServiceDetail scServiceDetail,ScOrder scOrder) {
		
			return scOrder.getParentOrderType();
	}
	
	

}
