package com.tcl.dias.notification.beans;

import java.util.List;
/**
 * 
 * 
 * 
 *This bean is used for fetching and updating user notification subscriptions product wise
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UserNotificationSubscriptionDetailsBean {
	private List<ProductNotificationActionDetails> productNotificationActionDetails;

	public List<ProductNotificationActionDetails> getProductNotificationActionDetails() {
		return productNotificationActionDetails;
	}

	public void setProductNotificationActionDetails(
			List<ProductNotificationActionDetails> productNotificationActionDetails) {
		this.productNotificationActionDetails = productNotificationActionDetails;
	}
	
}
