package com.tcl.dias.ticketing.controller;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.response.beans.AttachmentInfoBean;
import com.tcl.dias.ticketing.controller.v1.AttachmentController;
import com.tcl.dias.ticketing.creator.TicketingMockBeanCreator;
import com.tcl.dias.ticketing.response.AttachmentResponse;
import com.tcl.dias.ticketing.service.category.controller.v1.ServiceCategoryController;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class AttachmentControllerTest {

	@Autowired
	ServiceCategoryController serviceCategoryController;

	@MockBean
	private RestClientService restClientService;

	@Autowired
	TicketingMockBeanCreator mockBeanCreator;

	@Autowired
	AttachmentController attachmentController;

	/**
	 * init
	 * 
	 * @throws IOException
	 */
	@Before
	public void init() throws IOException {

		
		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getRestResponse());

		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getRestResponse());

	}

	/**
	 * testGetServiceDetails
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetAttachment() throws TclCommonException {

		ResponseResource<AttachmentResponse> resource = attachmentController.getAttachment("1");

		assertTrue(resource != null);

	}

	/**
	 * testGetServiceDetails
	 * 
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@Test
	public void testGetAttachmentDetails() throws TclCommonException, IOException {

		ResponseResource<AttachmentInfoBean> resource = attachmentController.getAttachmentDetails("1", " 2",null);

		assertTrue(resource != null);

	}

	/**
	 * testGetServiceDetails
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Test
	public void testUpdateAttachmentDetails() throws TclCommonException, IOException {
		ResponseResource<AttachmentInfoBean> resource = attachmentController.updateAttachmentDetails(Mockito.any(), "1",
				" 2", "8");

		assertTrue(resource != null);

	}

	/**
	 * testGetServiceDetails
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeGetAttachment() throws TclCommonException {
		
		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		ResponseResource<AttachmentResponse> resource = attachmentController.getAttachment("1");

		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		assertTrue(resource != null);

	}

	/**
	 * testGetServiceDetails
	 * 
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@Test
	public void testNegativeGetAttachmentDetails() throws TclCommonException, IOException {
		
		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		ResponseResource<AttachmentInfoBean> resource = attachmentController.getAttachmentDetails("1", " 2",null);

		assertTrue(resource != null);

	}

	/**
	 * testGetServiceDetails
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Test
	public void testNegativeUpdateAttachmentDetails() throws TclCommonException, IOException {
		
		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		ResponseResource<AttachmentInfoBean> resource = attachmentController.updateAttachmentDetails(Mockito.any(), "1",
				" 2", "8");

		assertTrue(resource != null);

	}
}
