package com.tcl.dias.notification.beans;
/**
 * 
 * This bean is used for fetching and updating user notification subscriptions and appending with product
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

import java.util.List;

public class ProductNotificationActionDetails {
	private String productName;
	private List<NotificationActionDetailsBean> notificationActionDetails;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<NotificationActionDetailsBean> getNotificationActionDetails() {
		return notificationActionDetails;
	}
	public void setNotificationActionDetails(List<NotificationActionDetailsBean> notificationActionDetails) {
		this.notificationActionDetails = notificationActionDetails;
	}
	
	
}
