package com.tcl.dias.ticketing.creator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tcl.dias.beans.ContactBean;
import com.tcl.dias.beans.NotesBean;
import com.tcl.dias.beans.UpdateRequestBean;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.ticketing.request.CreateRequest;
import com.tcl.dias.ticketing.request.CreateTicketRequest;

/**
 * This file contains the TicketingMockBeanCreator.java class.
 * 
 * used for creation of mock data
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class TicketingMockBeanCreator {

	/**
	 * @author vivek
	 * 
	 *         getRestResponse mock
	 * @return
	 */
	public RestResponse getRestResponse() {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(Status.SUCCESS);
		restResponse.setData(" {\r\n" + "        \"status\": \"200\",\r\n" + "        \"message\": \"Success\",\r\n"
				+ "        \"impacts\": [\r\n" + "            \"Total Loss of service\",\r\n"
				+ "            \"Partial Loss\",\r\n" + "            \"No Impact\"\r\n" + "        ],\r\n"
				+ "        \"categories\": null,\r\n" + "        \"productType\": \"ILL\"\r\n" + "    }");
		return restResponse;
	}

	/**
	 * @author vivek getRestResponse
	 * @return
	 */
	public RestResponse getErrorRestResponse() {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(Status.FAILURE);
		restResponse.setData(" {\r\n" + "        \"status\": \"200\",\r\n" + "        \"message\": \"Success\",\r\n"
				+ "        \"impacts\": [\r\n" + "            \"Total Loss of service\",\r\n"
				+ "            \"Partial Loss\",\r\n" + "            \"No Impact\"\r\n" + "        ],\r\n"
				+ "        \"categories\": null,\r\n" + "        \"productType\": \"ILL\"\r\n" + "    }");
		return restResponse;
	}

	/**
	 * for updated request getUpdateRequest
	 * 
	 * @return
	 */
	public UpdateRequestBean getUpdateRequest() {
		UpdateRequestBean bean = new UpdateRequestBean();
		bean.setCorrelationId("tyty");
		bean.setCreationDateFrom("tyty");
		bean.setImpact("tyty");
		bean.setIssueCode("tyty");
		bean.setIssueType("tyty");
		bean.setLimit("tyty");
		bean.setOffset("tyty");
		bean.setOrgId("tyty");
		bean.setServiceAlias("tyty");
		bean.setServiceIdentifier("tyty");
		bean.setServiceType("tyty");
		bean.setSortBy("tyty");
		bean.setSortOrder("tyty");
		bean.setTicketCreatedFrom("tyty");
		bean.setTicketCreatedTo("tyty");
		bean.setTicketStatus("tyty");
		bean.setUpdateDateFrom("tyty");
		return bean;
	}

	/**
	 * for updated request getUpdateTicketRequest
	 * 
	 * @return
	 */
	public CreateRequest getUpdateTicketRequest() {
		CreateRequest createRequest = new CreateRequest();
		createRequest.setCategory("tyty");
		createRequest.setContact(getContact());
		createRequest.setCorrelationId("tyty");
		createRequest.setDescription("tyty");
		createRequest.setImpact("tyty");
		createRequest.setIssueOccurenceDate("tyty");
		createRequest.setNotes(getNotes());
		createRequest.setServiceIdentifier("tyty");
		createRequest.setTicketStatus("tyty");
		createRequest.setEscalated("tyty");
		return createRequest;
	}
	
	/**
	 * @author chetchau
	 * 
	 * @return escalate value for the ticket
	 */
	public CreateRequest getEscaleTicketRequest() {
		CreateRequest createRequest = new CreateRequest();
		createRequest.setEscalated("tyty");
		return createRequest;
	}
	
	/**
	 * for updated request getUpdateTicketRequest
	 * 
	 * @return
	 */
	public CreateTicketRequest createTicketRequest() {
		CreateTicketRequest createRequest = new CreateTicketRequest();
		List<String> serviceList = new ArrayList<>();
		serviceList.add("SER-001");
		serviceList.add("SER-002");
		createRequest.setCategory("tyty");
		createRequest.setContact(getContact());
		createRequest.setCorrelationId("tyty");
		createRequest.setDescription("tyty");
		createRequest.setImpact("tyty");
		createRequest.setIssueOccurenceDate("tyty");
		createRequest.setNotes(getNotes());
		createRequest.setServiceIdentifier(serviceList);
		createRequest.setTicketStatus("tyty");
		return createRequest;
	}

	private ContactBean getContact() {
		ContactBean bean = new ContactBean();
		bean.setEmail("vivek@gmail.com");
		bean.setName("Vivek");
		bean.setPrimaryPhone("9798979");
		bean.setSecondaryPhone("6766868");
		return bean;
	}

	private List<NotesBean> getNotes() {
		List<NotesBean> list = new ArrayList<>();
		NotesBean bean = new NotesBean();
		bean.setAuthor("tyty");
		bean.setDate("tyty");
		bean.setText("tyty");
		return list;
	}

	public Map<String, Object> getUSerInfp() {
		UserInformation value = new UserInformation();
		value.setUserId("1");
		value.setCustomers(getCustomerList());
		Map<String, Object> map = new HashMap<>();
		map.put("USER_INFORMATION", value);

		return map;
	}

	public List<CustomerDetail> getCustomerList() {
		List<CustomerDetail> list = new ArrayList<>();
		list.add(createCustomerDetails());
		return list;

	}

	public CustomerDetail createCustomerDetails() {
		CustomerDetail cd = new CustomerDetail();
		cd.setCustomerAcId("test");
		cd.setCustomerEmailId("test@gmail.com");

		cd.setCustomerId(1);
		cd.setCustomerLeId(1);
		cd.setErfCustomerId(1);
		cd.setStatus((byte) 1);
		return cd;

	}

	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("admin");
		userInformation.setCustomers(getCustomerList());
		userInformation.setRole(Arrays.asList("OPTIMUS_MU"));
		return userInformation;

	}

}
