package com.tcl.dias.oms.izopc.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.izopc.beans.QuoteBean;
import com.tcl.dias.oms.izopc.beans.QuoteDetail;
import com.tcl.dias.oms.izopc.controller.v1.IzoPcQuoteController;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuoteService;
import com.tcl.dias.oms.utils.IzoPcObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains test cases for IzoPcQuoteController.java class.
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IzoPcQuoteControllerTest {
	
	@MockBean
	IzoPcQuoteService izoPcQuoteService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@Autowired
	IzoPcObjectCreator izoPcObjectCreator;
	
	@Autowired
	IzoPcQuoteController izoPcQuoteController;
	
	
	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	IllSiteRepository illSiteRepository;
	
	@MockBean
	private QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@MockBean
	private ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@MockBean
	private QuoteToLeRepository quoteToLeRepository;
	
	@MockBean
	MQUtils mqUtils;


	/**
	 * 
	 * init- predefined mocks
	 * 
	 * @throws TclCommonException
	 */
	@Before
	public void init() throws TclCommonException {
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(izoPcObjectCreator.getUserInformation());
	}
	

	
	/**
	 * testCreateQuote - test case for create quote
	 * @throws TclCommonException
	 */
	@Test
	public void testCreateQuote() throws TclCommonException {
		Mockito.when(izoPcQuoteService.createQuote(Mockito.any(),Mockito.anyInt())).thenReturn(objectCreator.getQuoteResponse());
		ResponseResource<QuoteResponse> response = izoPcQuoteController.createQuote(izoPcObjectCreator.getQuoteDetail_IZO(), Integer.valueOf(1));
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	/**
	 * Test case for trigger feasibility 
	 * @throws Exception
	 */
	@Test
	public void testTriggerForFeasibilityBean() throws Exception {

		List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();

		QuoteProductComponentsAttributeValue obj1 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj1.getProductAttributeMaster().setId(1);
		list.add(obj1);

		QuoteProductComponentsAttributeValue obj2 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj2.getProductAttributeMaster().setId(2);
		list.add(obj2);

		QuoteProductComponentsAttributeValue obj3 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj3.getProductAttributeMaster().setId(3);
		obj3.setAttributeValues("1=2");
		list.add(obj3);

		QuoteProductComponentsAttributeValue obj4 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj3.setAttributeValues("1=2");
		obj3.getProductAttributeMaster().setId(4);
		list.add(obj4);

		QuoteProductComponentsAttributeValue obj5 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj5.getProductAttributeMaster().setId(5);
		obj5.setAttributeValues("Fully Managed");
		list.add(obj5);

		
		QuoteProductComponentsAttributeValue obj6 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj6.getProductAttributeMaster().setId(6);
		list.add(obj6);

		QuoteProductComponentsAttributeValue obj7 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj7.getProductAttributeMaster().setId(7);
		list.add(obj7);

		QuoteProductComponentsAttributeValue obj8 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj8.getProductAttributeMaster().setId(8);
		list.add(obj8);

		QuoteProductComponentsAttributeValue obj9 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj9.getProductAttributeMaster().setId(9);
		list.add(obj9);

		QuoteProductComponentsAttributeValue obj10 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj10.getProductAttributeMaster().setId(10);
		list.add(obj10);

		QuoteProductComponentsAttributeValue obj11 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj11.getProductAttributeMaster().setId(11);
		list.add(obj11);

		QuoteProductComponentsAttributeValue obj12 = objectCreator.createQuoteProductComponentsAttributeValue();
		obj12.getProductAttributeMaster().setId(12);
		list.add(obj12);

		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.anyInt())).thenReturn(list);

		ProductAttributeMaster pm2 = objectCreator.getProductAtrributeMas();
		pm2.setName("Port Bandwidth");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(2))).thenReturn(Optional.of(pm2));

		ProductAttributeMaster pm3 = objectCreator.getProductAtrributeMas();
		pm3.setName("IPv6 Address Pool Size");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(3))).thenReturn(Optional.of(pm3));

		ProductAttributeMaster pm4 = objectCreator.getProductAtrributeMas();
		pm4.setName("IPv4 Address Pool Size");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(4))).thenReturn(Optional.of(pm4));

		ProductAttributeMaster pm5 = objectCreator.getProductAtrributeMas();
		pm5.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(5))).thenReturn(Optional.of(pm5));

		ProductAttributeMaster pm13 = objectCreator.getProductAtrributeMas();
		pm13.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(13))).thenReturn(Optional.of(pm13));

		ProductAttributeMaster pm14 = objectCreator.getProductAtrributeMas();
		pm14.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(14))).thenReturn(Optional.of(pm14));

		ProductAttributeMaster pm15 = objectCreator.getProductAtrributeMas();
		pm15.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(15))).thenReturn(Optional.of(pm15));

		ProductAttributeMaster pm16 = objectCreator.getProductAtrributeMas();
		pm16.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(16))).thenReturn(Optional.of(pm16));

		ProductAttributeMaster pm6 = objectCreator.getProductAtrributeMas();
		pm6.setName("Interface");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(6))).thenReturn(Optional.of(pm6));

		ProductAttributeMaster pm7 = objectCreator.getProductAtrributeMas();
		pm7.setName("CPE");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(7))).thenReturn(Optional.of(pm7));

		ProductAttributeMaster pm8 = objectCreator.getProductAtrributeMas();
		pm8.setName("CPE Basic Chassis");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(8))).thenReturn(Optional.of(pm8));

		ProductAttributeMaster pm9 = objectCreator.getProductAtrributeMas();
		pm9.setName("Service Variant");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(9))).thenReturn(Optional.of(pm9));

		ProductAttributeMaster pm10 = objectCreator.getProductAtrributeMas();
		pm10.setName("Local Loop Bandwidth");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(10))).thenReturn(Optional.of(pm10));

		ProductAttributeMaster pm11 = objectCreator.getProductAtrributeMas();
		pm11.setName("CPE Basic Chassis");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(11))).thenReturn(Optional.of(pm11));

		ProductAttributeMaster pm12 = objectCreator.getProductAtrributeMas();
		pm12.setName("CPE Basic Chassis");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(12))).thenReturn(Optional.of(pm12));

		ProductAttributeMaster pm1 = objectCreator.getProductAtrributeMas();
		pm1.setName("Burstable Bandwidth");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(1))).thenReturn(Optional.of(pm1));

		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(objectCreator.getCustomerDetailsBean());
		ResponseResource<String> response = izoPcQuoteController
				.triggerForFeasibilityBean(objectCreator.getFeasibilityBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * testCreateQuote - test case for create quote
	 * @throws TclCommonException
	 */
	@Test
	public void testGetQuote() throws TclCommonException {
		Mockito.when(izoPcQuoteService.getQuoteDetails(Mockito.anyInt(),Mockito.anyString(),false)).thenReturn(izoPcObjectCreator.getQuoteBean_IZO());
		ResponseResource<QuoteBean> response = izoPcQuoteController.getQuoteConfiguration(1,"ALL",false);
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	
	/**
	 * testEditSites - test case for edit sites API
	 * @throws TclCommonException
	 */
	
	@Test
	public void testEditSites() throws TclCommonException {
		Integer i = Integer.valueOf(1);
		Mockito.when(izoPcQuoteService.editSiteComponent(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteDetail_IZO());
		ResponseResource<QuoteDetail> response= izoPcQuoteController.editSites(i,i,i,objectCreator.getUpdateRequest());
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * testUpdateSiteProperties - test case for API to update site properties
	 * @throws TclCommonException
	 */
	
	@Test
	public void testUpdateSiteProperties() throws TclCommonException {
		Integer i = Integer.valueOf(1);
		Mockito.when(izoPcQuoteService.updateSiteProperties(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteDetail_IZO());
		ResponseResource<QuoteDetail> response= izoPcQuoteController.updateSiteProperties(i,i,i,objectCreator.getUpdateRequest());
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testShareQuote()  throws TclCommonException {
		Mockito.when(izoPcQuoteService.processMailAttachment(Mockito.anyString(), Mockito.anyInt())).thenReturn(new ServiceResponse());
		ResponseResource<ServiceResponse> response = izoPcQuoteController.shareQuote(1,1,"izopc@gmail.com");
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	

	@Test
	public void testUpdateCurrency() throws TclCommonException {
		Integer i = Integer.valueOf(1);
		doNothing().when(izoPcQuoteService).updateCurrency(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString());
		ResponseResource<String> response= izoPcQuoteController.updateCurrency(i,i,"USD");
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testUpdateLegalEntityProperties() throws TclCommonException {
		Integer i = Integer.valueOf(1);
		Mockito.when(izoPcQuoteService.updateLegalEntityProperties(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteDetail_IZO());
		ResponseResource<QuoteDetail> response= izoPcQuoteController.updateLegalEntityProperties(i,i,izoPcObjectCreator.getUpdateRequest());
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	


}
