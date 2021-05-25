package com.tcl.dias.oms.controller;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.oms.consumer.OmsAttachmentListener;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.service.OmsAttachmentService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This contains the test cases for consumer tests
 * 
 * @author SEKHAR ER
 *
 *
 * @link http://www.tatacommunications.com
 * @copyright 2018 Tata Communications Limited
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OmsAttachmentListenerTest {

	@Value("${oms.attatchment.queue}")
	private String queue;

	@Autowired
	OmsAttachmentService omsAttachmentService;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	OmsAttachmentRepository omsAttachmentRepository;

	@MockBean
	QuoteRepository quoterepository;
	
	@MockBean
	QuoteToLeRepository quoteToRepository;

	@Autowired
	OmsAttachmentListener omsAttatchmentListner;
	
	@MockBean
	OrderToLeRepository orderToLeRepository;

	/**
	 * This is positive test case for test attachment service
	 * 
	 * @throws TclCommonException
	 * 
	 */
	@Test
	public void testAttachmentService() throws TclCommonException {

		Mockito.when(quoteToRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		Mockito.when(omsAttachmentRepository.save(Mockito.any())).thenReturn(objectCreator.createOmsAttachMent());
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(Mockito.anyString(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getAttachmentsList());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
		omsAttachmentService.processOmsAttachment(objectCreator.createOmsAttachment());

	}

	/**
	 * 
	 * This is exception test case for attachmentService
	 * 
	 * 
	 * @throws TclCommonException
	 * 
	 * 
	 */
	@Test
	public void testAttachmentServiceWithExceptin() throws TclCommonException {

		Mockito.when(quoteToRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		Mockito.when(omsAttachmentRepository.save(Mockito.any())).thenReturn(objectCreator.createOmsAttachMent());
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(Mockito.anyString(),Mockito.anyInt(),Mockito.anyString())).thenReturn(new ArrayList<OmsAttachment>());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
		omsAttachmentService.processOmsAttachment(objectCreator.createOmsAttachment());

	}

	/**
	 * This is exception test case for attachmentService
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected = Exception.class)
	public void testAttachmentServiseWithException() throws TclCommonException {

		Mockito.when(quoterepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(omsAttachmentRepository.save(Mockito.any())).thenReturn(objectCreator.createOmsAttachMent());
		omsAttachmentService.processOmsAttachment(null);

	}

	/**
	 * This is exception test case for attachmentService
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAttachmentListner() throws TclCommonException {

		Mockito.when(quoterepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuote()));
		Mockito.when(omsAttachmentRepository.save(Mockito.any())).thenReturn(objectCreator.createOmsAttachMent());
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(Mockito.anyString(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getAttachmentsList());
	omsAttatchmentListner
				.processOmsAttachmentInformation("{\"quoteId\":\"1\",\"orderId\":\"2\",\"qouteLeId\":\"2\",\"operationName\":\"2\"}");

	}
	
	/**
	 * This is exception test case for attachmentService
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAttachmentListnerWithEmptyRequest() throws TclCommonException {

		Mockito.when(quoterepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuote()));
		Mockito.when(omsAttachmentRepository.save(Mockito.any())).thenReturn(objectCreator.createOmsAttachMent());
	 omsAttatchmentListner
				.processOmsAttachmentInformation("");

	}
	
	/**
	 * Test save of oms attachments for country documents.
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testprocessOmsGscAttachmentInformation() throws TclCommonException {
		String request="{\"omsAttachmentBean\":null,\"omsAttachBean\":[{\"qouteLeId\":null,\"orderLeId\":null,\"attachmentType\":\"Others\",\"attachmentId\":292,\"referenceName\":\"4DV43\",\"referenceId\":null}]}";
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndAttachmentType(Mockito.anyString(),Mockito.anyString()))
		.thenReturn(objectCreator.createOmsAttachMent());
		Mockito.when(omsAttachmentRepository.save(Mockito.any()))
		.thenReturn(objectCreator.createOmsAttachMent());
		String response=omsAttatchmentListner.processOmsGscAttachmentInformation(request);
		assertNotNull(response);
	}
	

}
