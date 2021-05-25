package com.tcl.dias.customer.integration;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.customer.consumer.AttachmentListener;
import com.tcl.dias.customer.entity.repository.AttachmentRepository;
import com.tcl.dias.customer.service.v1.AttachmentService;
import com.tcl.dias.customer.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class is test cases class for customer consumer tests
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttachMentListnerTest {

	@MockBean
	AttachmentRepository attachmentRepository;

	@Value("${attatchment.queue}")
	private String queue;

	@Autowired
	AttachmentListener attachmentListner;

	@Autowired
	AttachmentService attachMentService;

	@Autowired
	ObjectCreator objectCreator;
	
	@Before
	public void init() throws AmqpException, TclCommonException {
		Mockito.when(attachmentRepository.save(Mockito.any())).thenReturn(objectCreator.createAttachMent());

	}

	/**
	 * This is exception test case for attachmentService
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testProcessAttachmentInformation() throws TclCommonException {

		String request="{\"request\":\"{\\\"path\\\":\\\"IAS\\\",\\\"fileName\\\":\\\"Service Schedule\\\"}\",\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		Integer id = attachmentListner.processAttachmentInformation(request);
		assertTrue(true);
	}

	/**
	 * This is exception test case for attachmentService
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected=Exception.class)
	public void testProcessAttachmentInformationNull() throws TclCommonException {
		Integer id = attachmentListner.processAttachmentInformation(null);
	}

	/**
	 * This is exception test case for attachmentService
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testprocessDownloadInformation() throws TclCommonException {
		String request = "{\"request\":\"114\",\"mdcFilterToken\":\"H66LGNCNN0MA5MGLGHAAQ9\"}";
		Mockito.when(attachmentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.createAttachMent()));
		String response = attachmentListner.processDownloadInformation(request);
		assertTrue(true);
	}

	/**
	 * Negative Test case	
	 * This is exception test case for attachmentService
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected=Exception.class)
	public void testprocessDownloadInformationForNull() throws TclCommonException {
		String response = attachmentListner.processDownloadInformation(null);
	}

}
