package com.tcl.dias.billing.service.v1;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.billing.dispute.service.v1.BillingDisputeService;
import com.tcl.dias.billing.dispute.ticket.beans.AddComments;
import com.tcl.dias.billing.dispute.ticket.beans.ArrayOfString2;
import com.tcl.dias.billing.dispute.ticket.beans.NGPCESBulkTicketDetails;
import com.tcl.dias.billing.dispute.ticket.beans.NGPCESSingleTicketDetails;
import com.tcl.dias.billing.dispute.ticket.beans.UpdateCommentsAttachment;
import com.tcl.dias.billing.utils.BillingObjectCreator;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.webserviceclient.service.GenericWebserviceClient;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * 
 * Testing BillingDisputeServiceTest api positive and negative cases
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BillingDisputeServiceTest {
	@Autowired
	BillingDisputeService billingDisputeService;
	
	@Autowired
	BillingObjectCreator billingObjectCreator;
	
	@MockBean
	MQUtils mqUtils;

	@MockBean
	UserInfoUtils userInfoUtils;
	
	@MockBean
	@Qualifier("invoicesTemplate")
	JdbcTemplate invoicesTemplate;
	
	@MockBean
	@Qualifier("billingDisputeTicketsTemplate")
	JdbcTemplate billingDisputeTicketsTemplate;

	@MockBean
    private GenericWebserviceClient genericWebserviceClient;
	
	@MockBean
	NGPCESBulkTicketDetails nGPCESBulkTicketDetails;

	@Before
	public void init() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserInformation(Mockito.any())).thenReturn(billingObjectCreator.getUserInformation());
	}
	
	
	@Test
	public void testCreateBillingDisputeTicket() throws TclCommonException {
		NGPCESSingleTicketDetails nGPCESSingleTicketDetails=new NGPCESSingleTicketDetails();
		nGPCESSingleTicketDetails.setInvoiceNo("Invoc456");
		billingDisputeService.createBillingDisputeTicket(nGPCESSingleTicketDetails);
	}
	@Test
	public void testCreateBulkBillingDisputeTicket() throws TclCommonException {
		NGPCESBulkTicketDetails nGPCESBulkTicketDetails=new NGPCESBulkTicketDetails();
		ArrayOfString2 arrayOfString=new ArrayOfString2();
		nGPCESBulkTicketDetails.setInvoiceNo(arrayOfString);
		billingDisputeService.createBulkBillingDisputeTicket(nGPCESBulkTicketDetails);
	}

	@Test
	public void testAddComments() throws TclCommonException {
		Mockito.when(billingDisputeTicketsTemplate.queryForList(Mockito.anyString()))
				.thenReturn(billingObjectCreator.getBillingDisputeTicketsTemplateValue());
		AddComments addComments = new AddComments();
		addComments.setTicketNo("TK4567");
		billingDisputeService.addComments(addComments);
	}

	@Test
	public void testUpdateCommentsAttachment() throws TclCommonException {
		Mockito.when(billingDisputeTicketsTemplate.queryForList(Mockito.anyString()))
				.thenReturn(billingObjectCreator.getBillingDisputeTicketsTemplateValue());
		UpdateCommentsAttachment updateCommentsAttachment = new UpdateCommentsAttachment();
		updateCommentsAttachment.setTicketNo("TK4567");
		billingDisputeService.updateCommentsAttachment(updateCommentsAttachment);
	}
	
}
