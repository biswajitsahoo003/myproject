package com.tcl.dias.billing.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.billing.dispute.controller.v1.BillingDisputesController;
import com.tcl.dias.billing.dispute.service.v1.BillingDisputeService;
import com.tcl.dias.billing.dispute.ticket.beans.AddCommentsResponse;
import com.tcl.dias.billing.dispute.ticket.beans.BulkTicketCreationResponse;
import com.tcl.dias.billing.dispute.ticket.beans.CreateSingleTicketResponse;
import com.tcl.dias.billing.dispute.ticket.beans.UpdateCommentsAttachmentResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Testing BillingDisputesControllerTest api positive and negative cases
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BillingDisputesControllerTest {
	
	@Autowired
	BillingDisputesController billingDisputesController;
	
	@MockBean
	BillingDisputeService billingDisputeService;
	
	
	@Test
	public void testGetBillingDisputes() throws TclCommonException {
		Mockito.when(billingDisputeService.queryBillingDisputeTickets(Mockito.any())).thenReturn(new ArrayList<>());
		ResponseResource<List<Map<String, Object>>> response= billingDisputesController
				.getBillingDisputes(Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testCreateBillingDisputeTicket() throws TclCommonException {
		Mockito.when(billingDisputeService.createBillingDisputeTicket(Mockito.any())).thenReturn(new CreateSingleTicketResponse());
		ResponseResource<CreateSingleTicketResponse> response= billingDisputesController
				.createBillingDisputeTicket(Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testCreateBulkBillingDisputeTicket() throws TclCommonException {
		Mockito.when(billingDisputeService.createBulkBillingDisputeTicket(Mockito.any())).thenReturn(new BulkTicketCreationResponse());
		ResponseResource<BulkTicketCreationResponse> response= billingDisputesController
				.createBulkBillingDisputeTicket(Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testAddComments() throws TclCommonException {
		Mockito.when(billingDisputeService.addComments(Mockito.any())).thenReturn(new AddCommentsResponse());
		ResponseResource<AddCommentsResponse> response= billingDisputesController
				.addComments(Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetCommentsApproch1() throws TclCommonException {
		Mockito.when(billingDisputeService.queryBillingDisputeTicketComments(Mockito.any())).thenReturn(new ArrayList<>());
		ResponseResource<List<Map<String, Object>>> response= billingDisputesController
				.getComments("TCLticketNo");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test(expected=Exception.class)
	public void testGetCommentsApproch2() throws TclCommonException {
		Mockito.when(billingDisputeService.queryBillingDisputeTicketComments(Mockito.any())).thenReturn(new ArrayList<>());
		ResponseResource<List<Map<String, Object>>> response= billingDisputesController
				.getComments(Mockito.any());	}
	@Test
	public void testUpdateCommentsAttachmentResponse() throws TclCommonException {
		Mockito.when(billingDisputeService.updateCommentsAttachment(Mockito.any())).thenReturn(new UpdateCommentsAttachmentResponse());
		ResponseResource<UpdateCommentsAttachmentResponse> response= billingDisputesController
				.updateCommentsAttachmentResponse(Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
}
