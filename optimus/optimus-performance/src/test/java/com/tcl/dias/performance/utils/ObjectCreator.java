package com.tcl.dias.performance.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.performance.request.ReportGeneratorRequest;
/**
 * 
 * This file contains the Object Creator class
 * used to get the object value need in the performance test class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class ObjectCreator {
	public ObjectCreator() {

	}

	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("admin");
		userInformation.setCustomers(getCustomerList());
		userInformation.setRole(Arrays.asList("OPTIMUS_MU"));
		return userInformation;
	}

	public List<CustomerDetail> getCustomerList() {
		List<CustomerDetail> list = new ArrayList<>();
		list.add(createCustomerDetails());
		return list;
	}

	public CustomerDetail createCustomerDetails() {
		CustomerDetail cd = new CustomerDetail();
		cd.setCustomerAcId("test");
		cd.setCustomerId(1);
		cd.setCustomerLeId(1);
		cd.setErfCustomerId(1);
		cd.setStatus((byte) 1);
		return cd;
	}

	public ReportGeneratorRequest getReportGeneratorRequest(String granularity) {
		ReportGeneratorRequest reportGeneratorRequest = new ReportGeneratorRequest();
		reportGeneratorRequest.setDestinationCountry("India");
		reportGeneratorRequest.setDestinationLocation("India");
		reportGeneratorRequest.setGranularity(granularity);
		reportGeneratorRequest.setSourceCountry("India");
		reportGeneratorRequest.setSourceLocation("India");
		reportGeneratorRequest.setEndDate(new Timestamp(System.currentTimeMillis()));
		reportGeneratorRequest.setStartDate(new Timestamp(System.currentTimeMillis()));
		return reportGeneratorRequest;
	}

	public String getSfdcLeIdsJSON() {
		return "{\"customerLeDetails\":[{\"legalEntityName\":\"system\",\"agreementId\":\"2\",\"legalEntityId\":\"3456\",\"sfdcId\":2}]}";
	}
}
