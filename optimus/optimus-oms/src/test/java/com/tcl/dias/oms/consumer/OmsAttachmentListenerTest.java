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

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.service.OmsAttachmentService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for OmsAttachmentListenerTest.java class.
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
public class OmsAttachmentListenerTest {
	
	@MockBean
	OmsAttachmentService omsAttachmentService;
	
	@Autowired
	OmsAttachmentListener omsAttachmentListener;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@Test
	public void testProcessOmsAttachmentInformation() throws TclCommonException {
		Mockito.doNothing().when(omsAttachmentService).processOmsAttachment(Mockito.any());
		omsAttachmentListener.processOmsAttachmentInformation(Utils.convertObjectToJson(objectCreator.createOmsAttachment()));
		assertTrue(true);
	}
	
	@Test(expected=Exception.class)
	public void testProcessOmsAttachmentInformationForException() throws TclCommonException {
		omsAttachmentListener.processOmsAttachmentInformation("");
		assertTrue(true);
	}
	

}
