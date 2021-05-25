package com.tcl.dias.oms.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.SlabBean;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.repository.LeProductSlaRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.SlaMasterRepository;
import com.tcl.dias.oms.ill.controller.v1.IllSlaController;
import com.tcl.dias.oms.ill.service.v1.IllSlaService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the SlaControllerTest.java class. This class contains all
 * the test cases for the SlaControllerTest
 * 
 *
 * @author Kusuma Kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class SlaControllerTest {

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	private MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	private LeProductSlaRepository leProductSlaRepository;

	@MockBean
	private QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@MockBean
	private SlaMasterRepository slaMasterRepository;

	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	RabbitTemplate rabbitTemplate;

	@Autowired
	private ObjectCreator quoteObjectCreator;

	@Autowired
	private IllSlaController IllSla;

	@Autowired
	private IllSlaService illSlaService;

	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Before
	public void init() {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteToLe()));
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		Mockito.when(leProductSlaRepository.findByErfCustomerLeIdAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.createLeProductSlaList());
		Mockito.when(leProductSlaRepository.findByErfCustomerIdAndMstProductFamily(Mockito.anyInt(), Mockito.any()))
				.thenReturn(quoteObjectCreator.createLeProductSlaList());
		Mockito.when(quoteIllSiteSlaRepository.findByQuoteIllSiteAndSlaMaster(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteIllSiteSlaList());
		Mockito.when(quoteIllSiteSlaRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteIllSiteSlaList());
		// Mockito.when(slaMasterRepository.findBySlaName(Mockito.anyString()))
		// .thenReturn(quoteObjectCreator.createSlaMaster());
		Mockito.when(slaMasterRepository.findBySlaName(Mockito.anyString())).thenReturn(quoteObjectCreator.createSlaMaster());
		Mockito.when(quoteIllSiteSlaRepository.save(Mockito.any())).thenReturn(new QuoteIllSiteSla());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
						Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdInAndProductAttributeMaster_Name(Mockito.any(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());

		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamilyListIAS());
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(1)))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrribute()));
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(2)))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrribute1()));
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelected(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent());

		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());

	}

	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testsaveSla() throws TclCommonException {

		ResponseResource<SlabBean> response = IllSla.saveSla(quoteObjectCreator.slaUpdateRequest());
		assertTrue(response.getStatus() == Status.SUCCESS);
	}

	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testsaveSla1() throws TclCommonException {

		List<String> list = Arrays.asList("1", "2", "3");
		list.stream().forEach(value -> {
			try {
				/*
				 * Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.eq("2")))
				 * .thenReturn(quoteObjectCreator.getAddressDetail());
				 */
				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai")))
						.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
				LocationDetail loc = quoteObjectCreator.getLocationDetail();
				loc.setTier(value);
				String json = Utils.convertObjectToJson(loc);
				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("CODE234243#$"))).thenReturn(json);

				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1,OnNet")))
						.thenReturn(quoteObjectCreator.getProductSlaBeanjson());
				illSlaService.saveSla(quoteObjectCreator.getQuoteToLeSaveSlaIAS());
			} catch (TclCommonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}
	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testsaveSlaforelse() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getLocationDetailjson());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("2")))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("tier1")))
				.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelected(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai,OnNet")))
				.thenReturn(quoteObjectCreator.getProductSlaBeanjson());
		illSlaService.saveSla(quoteObjectCreator.getQuoteToLeSaveSlaForElseIAS());
	}

	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testsaveSla2() throws TclCommonException {

		List<String> list = Arrays.asList("1", "2", "3");
		list.stream().forEach(value -> {
			try {
				/*
				 * Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.eq("2")))
				 * .thenReturn(quoteObjectCreator.getAddressDetail());
				 */
				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai")))
						.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
				LocationDetail loc = quoteObjectCreator.getLocationDetail();
				loc.setTier(value);
				String json = Utils.convertObjectToJson(loc);
				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("2"))).thenReturn(json);

				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("Chennai,OnNet")))
				.thenReturn(quoteObjectCreator.getProductSlaBeanjson());
				Mockito.when(quoteIllSiteSlaRepository.findByQuoteIllSiteAndSlaMaster(Mockito.any(), Mockito.any()))
						.thenReturn(null);
				illSlaService.saveSla(quoteObjectCreator.getQuoteToLeSaveSlaIAS());
			} catch (TclCommonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testsaveSlaForProductFamilyEmpty() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getLocationDetailjson());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("2")))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai")))
				.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
		Mockito.when(leProductSlaRepository.findByErfCustomerLeIdAndMstProductFamily(Mockito.anyInt(), Mockito.any()))
				.thenReturn(null);
		Mockito.when(leProductSlaRepository.findByErfCustomerIdAndMstProductFamily(Mockito.anyInt(), Mockito.any()))
				.thenReturn(null);
		illSlaService.saveSla(quoteObjectCreator.getQuoteToLe());

	}

	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testsaveSlaForMatserEmpty() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getLocationDetailjson());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("2")))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai")))
				.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
		Mockito.when(quoteIllSiteSlaRepository.findByQuoteIllSiteAndSlaMaster(Mockito.any(), Mockito.any()))
				.thenReturn(null);
		illSlaService.saveSla(quoteObjectCreator.getQuoteToLe());
	}

	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testsaveSlaForSiteAndMasterEmpty() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getLocationDetailjson());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("2")))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai")))
				.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
		Mockito.when(leProductSlaRepository.findByErfCustomerLeIdAndMstProductFamily(Mockito.anyInt(), Mockito.any()))
				.thenReturn(null);
		Mockito.when(leProductSlaRepository.findByErfCustomerIdAndMstProductFamily(Mockito.anyInt(), Mockito.any()))
				.thenReturn(null);
		Mockito.when(quoteIllSiteSlaRepository.findByQuoteIllSiteAndSlaMaster(Mockito.any(), Mockito.any()))
				.thenReturn(null);
		illSlaService.saveSla(quoteObjectCreator.getQuoteToLe());

	}

	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test(expected = TclCommonException.class)
	public void testsaveSlaforexceptions() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getLocationDetailjson());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("2")))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("tier1")))
				.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelected(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai,OnNet")))
				.thenReturn(quoteObjectCreator.getProductSlaBeanjson());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdInAndProductAttributeMaster_Name(Mockito.any(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);

		illSlaService.saveSla(quoteObjectCreator.getQuoteToLeSaveSlaForElseIAS());
	}

	/*
	 * 
	 * Negative test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	/*
	 * 
	 * Negative test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test(expected = RuntimeException.class)
	public void testsaveSlaforexceptions2() throws Exception {
		List<String> list = Arrays.asList("1", "2", "3");
		list.stream().forEach(value -> {
			try {
				/*
				 * Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.eq("2")))
				 * .thenReturn(quoteObjectCreator.getAddressDetail());
				 */
				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai")))
						.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
				LocationDetail loc = quoteObjectCreator.getLocationDetail();
				loc.setTier(value);
				String json = Utils.convertObjectToJson(loc);
				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("2"))).thenReturn(json);

				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("Unmanaged,1")))
						.thenReturn(quoteObjectCreator.getProductSlaBeanjson());
				Mockito.when(
						quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
								Mockito.anyInt(), Mockito.anyString()))
						.thenThrow(RuntimeException.class);
				illSlaService.saveSla(quoteObjectCreator.getQuoteToLeSaveSlaIAS());
			} catch (TclCommonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testsaveSla3() throws TclCommonException {

		List<String> list = Arrays.asList("1", "2", "3");
		list.stream().forEach(value -> {
			try {
				/*
				 * Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.eq("2")))
				 * .thenReturn(quoteObjectCreator.getAddressDetail());
				 */
				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai")))
						.thenReturn(quoteObjectCreator.createSLaDetailsBeanList());
				LocationDetail loc = quoteObjectCreator.getLocationDetail();
				loc.setTier(value);
				String json = Utils.convertObjectToJson(loc);
				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("2"))).thenReturn(json);

				Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("chennai,OnNet")))
						.thenReturn(quoteObjectCreator.getProductSlaBeanjson());
				Mockito.when(quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(Mockito.anyInt()))
						.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue1());
				Mockito.when(quoteIllSiteSlaRepository.findByQuoteIllSiteAndSlaMaster(Mockito.any(), Mockito.any()))
						.thenReturn(null);
				illSlaService.saveSla(quoteObjectCreator.getQuoteToLeSaveSlaIAS());
			} catch (TclCommonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

}
