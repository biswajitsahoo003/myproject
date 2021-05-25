package com.tcl.dias.controller.integration.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.service.beans.ServiceResponse;
import com.tcl.dias.service.controller.v1.FileUploadController;
import com.tcl.dias.service.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 *This class contains the response information about the file up
 * 
 * @author SEKHAR ER
 *
 *@link http://www.tatacommunications.com/
 *@copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileUploadControllerTest {

	
	@Autowired
	private FileUploadController fileUploadController;
	

	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	MQUtils mqUtils;

	
	/**
	 * Positive test case for upload file controller
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadFile() throws TclCommonException {
	    MockMultipartFile mpf =
	    new MockMultipartFile("foo", "foooo.txt", MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes());
	    List<Integer> intList= new ArrayList<>();
	    intList.add(1);
		ResponseResource<ServiceResponse> response = fileUploadController.upLoadFile(mpf,1,1,intList,"Site","TAX");
		assertTrue(response.getData().getStatus().equals(Status.SUCCESS));
	}
	
	
	/**
	 * Positive test case for upload file controller
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadFileGetterTest() throws TclCommonException {
		MockMultipartFile mpf =
			    new MockMultipartFile("foo", "sekhar.pdf", MediaType.TEXT_PLAIN_VALUE,
		                "Hello World".getBytes());
		 List<Integer> intList= new ArrayList<>();
		    intList.add(1);
		 Mockito.when(mqUtils.sendAndReceive("attachment","")).thenReturn(1);
		ResponseResource<ServiceResponse> response = fileUploadController.upLoadFile(mpf,1,1,intList,"Site","TAX");
		assertTrue(response.getData().getStatus().equals(Status.SUCCESS));
	}
	

	/**
	 * Positive test case for upload file controller
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadFileGetterTestWithFile() throws TclCommonException {
		MockMultipartFile mpf =
			    new MockMultipartFile("foo", "ram.txt", MediaType.TEXT_PLAIN_VALUE,
		                "Hello World".getBytes());
		 List<Integer> intList= new ArrayList<>();
		    intList.add(1);
		 Mockito.when(mqUtils.sendAndReceive("attachment","")).thenReturn(1);
		ResponseResource<ServiceResponse> response = fileUploadController.upLoadFile(mpf,1,1,intList,"Site","TAX");
		assertTrue(response.getData().getFileName()!=null);
	}
	
	
	
	
	/**
	 * Negative test case for upload file controller with Exception
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadFileWithException() throws TclCommonException {
		MockMultipartFile mpf = new MockMultipartFile("file","file::::","test", "txt".getBytes());
		List<Integer> intList= new ArrayList<>();
	    intList.add(1);
		ResponseResource<ServiceResponse> response=fileUploadController.upLoadFile(mpf,1,1,intList,"","TAX");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	
	
	
	/**
	 * Negative test case for upload file controller with Exception
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadFileWithIvalidFormat() throws TclCommonException {
		MockMultipartFile mpf = new MockMultipartFile("file","file::::","test", "txt".getBytes());
		List<Integer> intList= new ArrayList<>();
	    intList.add(1);
		ResponseResource<ServiceResponse> response=fileUploadController.upLoadFile(mpf,1,1,intList,"Site","TAX");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	
	
	/**
	 * Negative test case for upload file controller with Exception
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadFileWithEmptyFile() throws TclCommonException {
		MockMultipartFile mpf = new MockMultipartFile("file","file::::","test", "".getBytes());
		List<Integer> intList= new ArrayList<>();
	    intList.add(1);
		ResponseResource<ServiceResponse> response=fileUploadController.upLoadFile(mpf,1,1,intList,"","TAX");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	
	/**
	 * Negative test case for upload file controller with Null
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadFileWithNull() throws TclCommonException {

		ResponseResource<ServiceResponse> response=fileUploadController.upLoadFile(null,null,null,null,null, null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	
	
	/**
	 * Negative test case for upload file controller with Null And Valid
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadFileWithNullAndValid() throws TclCommonException {

		ResponseResource<ServiceResponse> response=fileUploadController.upLoadFile(null,null,1,null, null,"TAX");
		assertTrue(response .getResponseCode()!=200);

	
	}
	
	
	
	/**
	 * testDownloadAttachment - testing file download by passing attachment id
	 * 
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@Test
	public void testDownloadAttachment() throws TclCommonException, IOException {
		ResponseEntity<Resource> response = fileUploadController.getAttachments(38);
		assertNotNull(response.getBody());
	}

	/**
	 * testDownloadAttachment - testing file download by passing attachment id
	 * 
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@Test(expected = ClassCastException.class)
	public void testDownloadAttachmentNegative() throws TclCommonException, IOException {
		ResponseEntity<Resource> response = fileUploadController.getAttachments(38000);
		assertNotNull(response.getBody());
	}

	/**
	 * testDownloadAttachment - testing file download by passing attachment id
	 * 
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@Test(expected = ClassCastException.class)
	public void testDownloadAttachmentNull() throws TclCommonException, IOException {
		assertNull(fileUploadController.getAttachments(null));
	}

	/**
	 * testDownloadAttachment - testing file download by passing attachment id
	 * 
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@Test(expected = ClassCastException.class)
	public void testDownloadAttachmentZero() throws TclCommonException, IOException {
		assertNull(fileUploadController.getAttachments(0));
	}
	/*
	 * Test upload file - Negative Scenario - Passing refference ID null
	 * @throws TclCommonException
	 * 
	 */
	@Test
	public void testUploadFileWithRefferenceIdNull() throws TclCommonException {
		 MockMultipartFile mpf =
				    new MockMultipartFile("foo", "foooo.txt", MediaType.TEXT_PLAIN_VALUE,
			                "Hello World".getBytes());
				    List<Integer> intList= new ArrayList<>();
				    intList.add(1);
		ResponseResource<ServiceResponse> response=fileUploadController.upLoadFile(mpf, 1, 1, null, "Site", "TAX");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	/*
	 * Test upload file - Negative Scenario - Passing refference name null
	 * @throws TclCommonException
	 * 
	 */
	@Test
	public void testUploadFileWithRefferenceNameNull() throws TclCommonException {
		 MockMultipartFile mpf =
				    new MockMultipartFile("foo", "foooo.txt", MediaType.TEXT_PLAIN_VALUE,
			                "Hello World".getBytes());
				    List<Integer> intList= new ArrayList<>();
				    intList.add(1);
		ResponseResource<ServiceResponse> response=fileUploadController.upLoadFile(mpf, 1, 1, intList, null, "TAX");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	/*
	 * Test upload file - Negative Scenario - Passing attachement type null
	 * @throws TclCommonException
	 * 
	 */
	
	@Test
	public void testUploadFileWithAttachmentTypeNull() throws TclCommonException {
		 MockMultipartFile mpf =
				    new MockMultipartFile("foo", "foooo.txt", MediaType.TEXT_PLAIN_VALUE,
			                "Hello World".getBytes());
				    List<Integer> intList= new ArrayList<>();
				    intList.add(1);
		ResponseResource<ServiceResponse> response=fileUploadController.upLoadFile(mpf, 1, 1, intList, "Site", null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	}
	
