package com.tcl.dias.billing.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;

@Service
public class BillingObjectCreator {

	private static String INVOICE_FILENAME = "invoice_file_name";
	private static String INVOICE_FILEURL = "invoice_link";
	
	public List<String> getBillingAccounts() {
		List<String> bAIds = new ArrayList<>();
		bAIds.add("ILL036668");
		bAIds.add("ETH044954");
		bAIds.add("ILL033986");
		return bAIds;
	}
	
	public List<CustomerDetail> getCustomerDetails() {
		List<CustomerDetail> details = new ArrayList<>();
		details.add(getCustomerDetail(001));
		details.add(getCustomerDetail(002));
		details.add(getCustomerDetail(003));
		return details;
	}
	
	private CustomerDetail getCustomerDetail(Integer customerId) {
		CustomerDetail detail = new CustomerDetail();
		detail.setCustomerId(customerId);
		detail.setCustomerAcId("CUST-001");
		detail.setCustomerEmailId("cust001@abc.com");
		return null;
	}

	public Map<String, Object> queryResultMap() {
		return getResult("ILL054359_061804G10003335_New.pdf", "/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20180419/NonUsage/ILL054359_061804G10003335_New.pdf");
	}
	
	public List<Map<String, Object>> queryResultList() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		result.add(getResult("ILL054359_061804G10003335_New.pdf", "/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20180419/NonUsage/ILL054359_061804G10003335_New.pdf"));
		result.add(getResult("ILL054359_061804G10003336_New.pdf", "/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20180419/NonUsage/ILL054359_061804G10003335_New.pdf"));
		result.add(getResult("ILL054359_061804G10003337_New.pdf", "/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20180419/NonUsage/ILL054359_061804G10003335_New.pdf"));
		return result;
	}

	public List<Map<String, Object>> queryResultList1() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		result.add(getInvoice("061804G10003335"));
		result.add(getInvoice("061804G10003336"));
		result.add(getInvoice("061804G10003337"));
		return result;
	}
	
	private Map<String, Object> getResult(String name, String url) {
		Map<String, Object> result = new HashMap<>();
		result.put(INVOICE_FILENAME, name);
		result.put(INVOICE_FILEURL, url);
		return result;
	}
	
	private Map<String, Object> getInvoice(String invoiceNo) {
		Map<String, Object> result = new HashMap<>();
		result.put("service_type", "GVPN");
		result.put("account_no", "VPN081041");
		result.put("invoice_no", invoiceNo);
		result.put("bill_due_date", "20180814");
		result.put("invtotal", "-976903.64");
		result.put("billing_currency", "INR");
		result.put("new_bill_date", "20180615");
		result.put("invoice_link", "/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20180615/NonUsage/VPN081041_CR271806G104640_New.pdf");
		result.put("invoice_file_name", "VPN081041_CR271806G104640_New.pdf");
		result.put("cdr_invoice_filename", null);
		result.put("cdr_invoice_link", null);
		return result;
	}
	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("optimus_regus");
		CustomerDetail customerDetail = new CustomerDetail();
		customerDetail.setCustomerId(9);
		customerDetail.setCustomerAcId("SFDC - 000430");
		customerDetail.setErfCustomerId(9);
		customerDetail.setStatus((byte) 1);
		List <CustomerDetail> custList = new ArrayList<>();
		custList.add(customerDetail);
		userInformation.setCustomers(custList);
		return userInformation;
	}

	public List<Map<String, Object>> getBillingDisputeTicketsTemplateValue() {
		List<Map<String, Object>> value = new ArrayList<>();
		Map<String, Object> mapVal = new HashMap<>();
		mapVal.put("sap_code", "CESTEST");
		value.add(mapVal);
		return value;
	}
	
	public List<Map<String, Object>> getDBValues() {
		List<Map<String, Object>> value = new ArrayList<>();
		Map<String, Object> mapVal = new HashMap<>();
		mapVal.put("sap_code", "CESTEST");
		mapVal.put("customer", "VI000176");
		mapVal.put("invoice_num_cheque_num_tds_cert_num", "CRILL011741866");
		mapVal.put("customer_name", "Inventurus Knowledge Solutions Pv");
		mapVal.put("invoice_receipt_date", "20170124");
		mapVal.put("orignal_invoice_amount", "20925.02-");
		mapVal.put("invoice_receipt_curreny", "INR");
		mapVal.put("outstanding_amount", "5412.87-");
		mapVal.put("due_date", "20170223");
		mapVal.put("bill_payment", "CREDIT NOTE");
		mapVal.put("due_date", "20170223");
		
		// map 2
		Map<String, Object> mapVal2= new HashMap<>();
		mapVal2.put("sap_code", "CESTEST");
		mapVal2.put("customer", "VI000176");
		mapVal2.put("invoice_num_cheque_num_tds_cert_num", "CRILL011741866");
		mapVal2.put("customer_name", "Inventurus Knowledge Solutions Pv");
		mapVal2.put("invoice_receipt_date", "20170124");
		mapVal2.put("orignal_invoice_amount", "20925.02");
		mapVal2.put("invoice_receipt_curreny", "USD");
		mapVal2.put("outstanding_amount", "5412.87");
		mapVal2.put("due_date", "20170223");
		mapVal2.put("bill_payment", "INVOICE");
		mapVal2.put("due_date", "20170223");
		
		value.add(mapVal);
		value.add(mapVal2);
		return value;
	}

}
