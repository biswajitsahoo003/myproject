package com.tcl.dias.oms.npl.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

import java.util.Optional;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.npl.pdf.beans.NplQuotePdfBean;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the NplQuotePdfServiceTest.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NplQuotePdfServiceTest {
	
	@Autowired
	NplQuotePdfService nplQuotePdfService;
	
	@MockBean
	NplQuoteService nplQuoteService;
	
	@Autowired
	private NplObjectCreator nplObjectCreator;
	
	@MockBean
	MQUtils mqUtils;
	
	@MockBean
	CofDetailsRepository cofDetailsRepository;
	
	@MockBean
	QuoteRepository quoteRepository;
	
	@MockBean
	OrderRepository orderRepository;
	
	@Before
	public void init() throws TclCommonException {
		Mockito.when(nplQuoteService.getQuoteDetails(Mockito.anyInt(),Mockito.anyString(),false)).thenReturn(nplObjectCreator.getNplQuoteBean());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString())).thenReturn(nplObjectCreator.getCofDetails());
		Mockito.when(cofDetailsRepository.save(Mockito.any(CofDetails.class))).thenReturn(nplObjectCreator.getCofDetails());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(nplObjectCreator.getQuote()));
		doNothing().when(mqUtils).send(Mockito.anyString(),Mockito.any());
		Mockito.when(orderRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(nplObjectCreator.getOrder()));
	}

	@Test
	public void testProcessCofPdfApproch1() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(nplObjectCreator.getAddressDetailJSON());
		String htmlData=nplQuotePdfService.processCofPdf(1,new MockHttpServletResponse(), null, false, 1,null);
		assertTrue(htmlData != null && !htmlData.isEmpty());

	}
	@Test
	public void testProcessCofPdfApproch2() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(nplObjectCreator.getAddressDetailJSON());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString())).thenReturn(null);
		String htmlData=nplQuotePdfService.processCofPdf(1,new MockHttpServletResponse(), null, true,1,null);
		assertTrue(htmlData != null && !htmlData.isEmpty());

	}
	@Test
	public void testProcessQuotePdf() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(nplObjectCreator.getAddressDetailJSON());
		nplQuotePdfService.processQuotePdf(1,new MockHttpServletResponse(), 1);
		assertTrue(true);

	}
	@Test
	public void testProcessDocusign() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(nplObjectCreator.getAddressDetailJSON());
		nplQuotePdfService.processDocusign(1,1,true,"optimus@tatacommunications.com","optimus",null);
		assertTrue(true);

	}
	@Test
	public void testUploadCofPdfApproch1() throws Exception {
		Quote quote= nplObjectCreator.getQuote();
		quote.setQuoteCode(Utils.generateUid());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(quote));
        MockMultipartFile multipartFile = new MockMultipartFile("optimus", "optimusFile", "application/json", "optimus".getBytes());
		nplQuotePdfService.uploadCofPdf(1,multipartFile,1);
		assertTrue(true);

	}
	@Test
	public void testUploadCofPdfApproch2() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("optimus", "optimusFile", "application/json", "optimus".getBytes());
        Quote quote= nplObjectCreator.getQuote();
		quote.setQuoteCode(Utils.generateUid());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(quote));
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString())).thenReturn(null);
		nplQuotePdfService.uploadCofPdf(1,multipartFile,1);
		assertTrue(true);

	}
	@Test
	public void testDownloadCofPdf() throws Exception {
		nplQuotePdfService.downloadCofPdf(1,new MockHttpServletResponse());
		assertTrue(true);

	}
	@Test
	public void testProcessApprovedCof() throws Exception {
		nplQuotePdfService.processApprovedCof(1,1,new MockHttpServletResponse());
		assertTrue(true);

	}
	@Test
	public void testProcessQuoteHtml() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(nplObjectCreator.getAddressDetailJSON());
		nplQuotePdfService.processQuoteHtml(1);
		assertTrue(true);
	}
	@Test
	public void testConstructSupplierInformations() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.anyString())).thenReturn(nplObjectCreator.getConstructSupplierInformationsJSON());
		nplQuotePdfService.constructSupplierInformations(new NplQuotePdfBean(), nplObjectCreator.getQuoteToLeBean());
		assertTrue(true);

	}
	@Test
	public void testConstructCustomerLocationDetails() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.anyString())).thenReturn(nplObjectCreator.getCustomerLocationDetailsJSON());
		nplQuotePdfService.constructCustomerLocationDetails(new NplQuotePdfBean(), nplObjectCreator.getLegalAttributeBean());
		assertTrue(true);

	}
	@Test
	public void testConstructBillingInformations() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.anyString())).thenReturn(nplObjectCreator.getBillingContactInfoJSON());
		nplQuotePdfService.constructBillingInformations(new NplQuotePdfBean(), nplObjectCreator.getLegalAttributeBean());
		assertTrue(true);

	}
	
}
