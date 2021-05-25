package com.tcl.dias.oms.consumer;

import static org.junit.Assert.assertTrue;

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

import com.tcl.dias.common.beans.CommonDocusignResponse;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Test class for DocusignResponseListener.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocusignResponseListenerTest {
	
	@Autowired
	DocusignResponseListener docusignResponseListener;
	
	@MockBean
	DocusignService docuSignService;
	
	@Autowired
	ObjectCreator objectCreator;

	@Test
	public void testProcessDocusignResponse() throws TclCommonException{
		Mockito.doNothing().when(docuSignService).processDocuSignResponse(Mockito.any(CommonDocusignResponse.class));
		docusignResponseListener.processDocusignResponse(objectCreator.getDocuSignResponse());
		assertTrue(true);
	}
	
	@Test(expected = Exception.class)
	public void testProcessDocusignResponseForException() throws TclCommonException{
		Mockito.doThrow(RuntimeException.class).when(docuSignService).processDocuSignResponse(Mockito.any(CommonDocusignResponse.class));
		docusignResponseListener.processDocusignResponse(objectCreator.getDocuSignResponse());
		assertTrue(true);
	}
	
	@Test
	public void testProcessDocusignNotificationResponse() throws TclCommonException{
		Mockito.when(docuSignService.processDocuSignNotificationResponse(Mockito.any(CommonDocusignResponse.class))).thenReturn("");
		String response = docusignResponseListener.processDocusignNotificationResponse(objectCreator.getDocuSignResponse());
		assertTrue(response!=null);
	}
	
	@Test(expected = Exception.class)
	public void testProcessDocusignNotificationResponseForException() throws TclCommonException{
		Mockito.when(docuSignService.processDocuSignNotificationResponse(Mockito.any(CommonDocusignResponse.class))).thenThrow(RuntimeException.class);
		String response = docusignResponseListener.processDocusignNotificationResponse(objectCreator.getDocuSignResponse());
		assertTrue(response==null);
	}
}
