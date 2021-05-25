package com.tcl.dias.billing.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.billing.controller.v1.BillingController;
import com.tcl.dias.billing.service.v1.BillingService;
import com.tcl.dias.billing.utils.BillingObjectCreator;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BillingControllerTest {

	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	@Qualifier("invoicesTemplate")
	JdbcTemplate invoicesTemplate;

	@Mock
	Client client;

	@Mock
	WebTarget target;

	@Mock
	Builder builder;

	@Mock
	InputStream is;

	@Mock
	CustomerDetail customer;

	@MockBean
	UserInformation userInfo;

	@Autowired
	BillingService billingService;

	@Autowired
	BillingController billingController;

	@Autowired
	@Qualifier("billingObjectCreator")
	BillingObjectCreator objectCreator;

	private List<String> invoiceIds;

	@Before
	public void init() throws Exception {
		invoiceIds = new ArrayList<>();
		invoiceIds.add("061804G10003335");
		invoiceIds.add("061804G10003336");
		invoiceIds.add("061804G10003337");

		Mockito.when(userInfoUtils.getUserInformation(Mockito.anyString()))
				.thenReturn(Mockito.mock(UserInformation.class));

		Mockito.when(invoicesTemplate.queryForMap(Mockito.anyString())).thenReturn(objectCreator.queryResultMap());

		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getBillingAccounts());

		Mockito.when(client.target(Mockito.anyString())).thenReturn(target);
		Mockito.when(target.request()).thenReturn(builder);
		Mockito.when(builder.get(InputStream.class)).thenReturn(is);
	}

	@Test
	public void getAllInvoices() throws TclCommonException {
		Mockito.when(invoicesTemplate.queryForList(Mockito.anyString())).thenReturn(objectCreator.queryResultList());

		ResponseResource<List<Map<String, Object>>> response = billingController.getAllInvoices(Mockito.anyString());
		assertEquals(Status.SUCCESS, response.getStatus());
	}

	@Test(expected = Exception.class)
	public void getAllInvoicesForException() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserInformation(Mockito.anyString())).thenReturn(null);
		billingController.getAllInvoices(Mockito.anyString());
	}

	@Test
	public void getAllInvoicesWithBAs() throws TclCommonException, FileNotFoundException {
		Mockito.when(userInfo.getCustomers()).thenReturn(objectCreator.getCustomerDetails());
		Mockito.when(customer.getCustomerLeId()).thenReturn(10097642);

		Mockito.when(invoicesTemplate.queryForList(Mockito.anyString())).thenReturn(objectCreator.queryResultList());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getBillingAccounts()));

		ResponseResource<List<Map<String, Object>>> response = billingController.getAllInvoices(Mockito.anyString());
		assertTrue(response.getData() != null);
	}

	@Test
	public void downloadInvoice() throws TclCommonException, FileNotFoundException {
		Mockito.when(invoicesTemplate.queryForMap(Mockito.anyString())).thenReturn(objectCreator.queryResultMap());
		ResponseEntity<byte[]> response = billingController.downloadInvoice("CBF", "ILL054359_061804G10003335_New.pdf", "/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20180419/NonUsage/ILL054359_061804G10003335_New.pdf","TCLI",null);
		assertTrue(response != null);
	}

	@Test(expected = Exception.class)
	public void downloadInvoiceForException() throws TclCommonException {
		ResponseEntity<byte[]> response = billingController.downloadInvoice(null, null, null,null,null);
		assertTrue(response == null);
	}

	@Test
	public void downloadInvoices() throws TclCommonException, IOException {
		Mockito.when(invoicesTemplate.queryForList(Mockito.anyString())).thenReturn(objectCreator.queryResultList());

		Map<String, String> invoiceDetails = new HashMap<>();
		Map<String, List<Map<String, String>>> invoiceDetailsFinal = new HashMap<>();

		invoiceDetails.put("ILL031241_CR271812G113606_New.pdf",
				"/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20181210/NonUsage/ILL031241_CR271812G113606_New.pdf");
		invoiceDetails.put("AWC001818_271812GC0008538_New.pdf",
				"/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20181214/NonUsage/AWC001818_271812GC0008538_New.pdf");
		invoiceDetails.put("ILL041733_291812G10060187_New.pdf",
				"/CBF_DATA/CBF_DATA/InvoiceSigned/Production/20181208/NonUsage/ILL041733_291812G10060187_New.pdf");

		List<Map<String, String>> input = new ArrayList<Map<String, String>>();

		input.add(invoiceDetails);

		invoiceDetailsFinal.put("ILL041733_291812G10060187_New.pdf", input);

		ResponseEntity<byte[]> response = billingController.downloadInvoices(invoiceDetailsFinal);
		assertTrue(response != null);
	}

	@Test(expected = Exception.class)
	public void downloadInvoicesForException() throws TclCommonException, IOException {

		ResponseEntity<byte[]> response = billingController.downloadInvoices(null);
		assertTrue(response == null);
	}

}
