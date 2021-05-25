package com.tcl.dias.servicehandover.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class LoadCustomerDetails {

	@Value("${rabbitmq.customer.latest.crn.queue}")
	private String customerCrnQueue;

	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;
	
	@Value("${rabbitmq.customer.secs.queue}")
	String customerSecsQueue;
	
	@Value("${rabbitmq.customer.all.secs.queue}")
	String customerAllSecsQueue;
	
	@Value("${rabbitmq.site.gst.queue}")
	String siteGstQueue;
	
	@Autowired
	MQUtils mqUtils;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadCustomerDetails.class);

	public String getCrnNumber(int cusId) {
		LOGGER.info("getCrnNumber invoked for CUSTOMER ID {} ", cusId);
		String crnId = null;
		try {
			crnId = (String) mqUtils.sendAndReceive(customerCrnQueue, new Integer(cusId).toString());

		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		LOGGER.info("getCrnNumber completed CRN ID {} ", crnId);

		return crnId;

	}

	public AddressDetail getCustomerAddress(String billingContactId) {
		AddressDetail addressDetail = null;
		try {

			String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue, billingContactId);
			BillingContact billingContact = null;
			if (billingContactResponse != null && !StringUtils.isBlank(billingContactResponse)) {
				billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
						BillingContact.class);
				String locationId = billingContact.getErfLocationId();
				addressDetail = constructCustomerLocationDetails(locationId);

				// System.out.println("CONTRACTING ADDRESS:"+ addressDetail.toString());
				return addressDetail;
			}
		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return addressDetail;

	}

	public AddressDetail constructCustomerLocationDetails(String locationId)
			throws TclCommonException, IllegalArgumentException {

		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, locationId);
		if (locationResponse != null)
			return (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
		else
			return null;

	}
	
	public String getSecsCode(int cusId) {
		LOGGER.info("getSecsCode invoked for CUSTOMER ID {} ", cusId);
		String secsId = null;
		try {
			secsId = (String) mqUtils.sendAndReceive(customerSecsQueue, new Integer(cusId).toString());

		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		LOGGER.info("getSecsCode completed SECS ID {} ", secsId);

		return secsId;

	}
	
	
	@SuppressWarnings("unchecked")
	public List<String> getAllSecsCode(int cusId) {
		LOGGER.info("getCrnNumber invoked for CUSTOMER ID {} ", cusId);
		String crnIds = null;
		ArrayList<String> listSapCode=null;
		try {
			crnIds = (String) mqUtils.sendAndReceive(customerAllSecsQueue, new Integer(cusId).toString());
			
			listSapCode= Utils.convertJsonToObject(crnIds, ArrayList.class);
		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		LOGGER.info("getCrnNumber completed CRN ID {} ", listSapCode);

		return listSapCode;

	}
	
	public LeStateInfo getGstDetails(String gstNo, Integer customerLeID) {

		SiteGstDetail siteGstDetail = new SiteGstDetail();
		siteGstDetail.setGstNo(gstNo);
		siteGstDetail.setCustomerLeId(customerLeID);

		try {
			String leGst = (String) mqUtils.sendAndReceive(siteGstQueue, Utils.convertObjectToJson(siteGstDetail));

			if (StringUtils.isNotBlank(leGst)) {
				LeStateInfo leStateInfo = (LeStateInfo) Utils.convertJsonToObject(leGst, LeStateInfo.class);
				return leStateInfo;

			}
		} catch (TclCommonException | IllegalArgumentException e) {

			LOGGER.error("error in getting gst response");
		}
		return null;

	}
	
	
}
