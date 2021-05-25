package com.tcl.dias.oms.npl.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.FRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.pricing.bean.FeasibilityResponse;
import com.tcl.dias.oms.npl.pricing.bean.ManualFeasibilityRequest;
import com.tcl.dias.oms.npl.pricing.bean.PricingResponse;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the NplPricingFeasibilityServiceTest.java class.
 * 
 *
 * @author PRABHU A
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NplPricingFeasibilityServiceTest {

	@MockBean
	QuoteRepository quoteRepository;

	@Autowired
	private NplObjectCreator quoteObjectCreator;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	IllSiteRepository illSiteRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	NplLinkRepository nplLinkRepository;

	@MockBean
	OrderNplLinkRepository orderNplLinkRepository;

	@MockBean
	LinkFeasibilityRepository linkFeasibilityRepository;

	@MockBean
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;

	@MockBean
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;

	@MockBean
	OmsSfdcService omsSfdcService;

	@MockBean
	QuoteDelegationRepository quoteDelegationRepository;

	@MockBean
	ProductSolutionRepository productSolutionRepository;

	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;

	@MockBean
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;

	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	NplQuotePdfService nplQuotePdfService;

	@MockBean
	UserRepository userRepository;

	@MockBean
	NotificationService notificationService;

	@MockBean
	CofDetailsRepository cofDetailsRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	OrderPriceRepository orderPriceRepository;

	@MockBean
	QuotePriceRepository quotePriceRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	MQUtils mqUtilsMock;

	@Autowired
	NplPricingFeasibilityService feasibilityService;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	RestClientService restClientService;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	MailNotificationBean mailNotificationBean;

	@Before
	public void init() throws TclCommonException {
		when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
		when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());
		when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());
		when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuoteIllSite());
		when(nplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuoteNplLinkList());
		when(nplLinkRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuoteNplLink());
		when(nplLinkRepository.findByProductSolutionId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuoteNplLinkList());
		when(linkFeasibilityRepository.findByQuoteNplLink_Id(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getLinkFeasibilityList());
		when(quoteNplLinkSlaRepository.findByQuoteNplLink_Id(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuoteNplLinkSlaList());
		when(quoteNplLinkSlaRepository.save(Mockito.any(QuoteNplLinkSla.class)))
				.thenReturn(quoteObjectCreator.getQuoteNplLinkSla());
		when(quoteNplLinkSlaRepository.findByQuoteNplLink(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteNplLinkSlaList());
		when(quoteToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLe());
		when(quoteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuote());
		when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
		when(quoteToLeProductFamilyRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		when(productSolutionRepository.findByQuoteToLeProductFamily(
				Mockito.any(QuoteToLeProductFamily.class)))
						.thenReturn(quoteObjectCreator.getSolutionList());
		when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any(QuoteToLeProductFamily.class)))
				.thenReturn(quoteObjectCreator.getSolutionList());
		when(productSolutionRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		when(quoteProductComponentRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent(Mockito.any(QuoteProductComponent.class)))
						.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.anyInt()))
						.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteProductComponentsAttributeValue());
		when(linkFeasibilityRepository.save(Mockito.any(LinkFeasibility.class)))
				.thenReturn(quoteObjectCreator.getLinkFeasibility());
		when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null))
				.thenReturn(quoteObjectCreator.getPricingDetails());
		when(pricingDetailsRepository.save(Mockito.any())).thenReturn(new PricingEngineResponse());
		when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.craeteUser());
		when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.craeteUser()));

		when(notificationService.newOrderSubmittedNotification(mailNotificationBean)).thenReturn(true);
		when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), 
				Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(), Mockito.any(),
				Mockito.anyString())).thenReturn(Optional.of(quoteObjectCreator.getQuoteProductComponent().get(0)));
		when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()
				)).thenReturn(quoteObjectCreator.geQuotePrice());
		when(linkFeasibilityRepository.findByQuoteNplLink(Mockito.any(QuoteNplLink.class)))
				.thenReturn(quoteObjectCreator.getLinkFeasibilityList());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(new MstOmsAttribute());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		doNothing().when(linkFeasibilityRepository).deleteAll(Mockito.any());
		when(customerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getCustomer()));
		when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));
		when(nplLinkRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteNplLink()));
		when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getSolution()));
		when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getIllsites()));
		when(illSiteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getIllsites());
		when(nplLinkRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteNplLink());
		when(linkFeasibilityRepository.findByQuoteNplLink_IdAndIsSelected(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getLinkFeasibilityList());
		when(linkFeasibilityRepository.findByIdAndQuoteNplLink_Id(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getLinkFeasibility()));
	}

	@Test
	public void testProcessFeasibilityTest() throws Exception {

		when(mqUtilsMock.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) throws TclCommonException {
				if (count++ == 1)
					return quoteObjectCreator.getAddressDetail();

				return quoteObjectCreator.getCustomerDetailsBean();
			}
		});
		when(productAttributeMasterRepository.findById(Mockito.anyInt())).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) throws TclCommonException {
				count++;
				if (count == 1)
					return Optional.of(quoteObjectCreator.getProductAtrributeMas());
				else if (count == 2)
					return Optional.of(quoteObjectCreator.getProductAtrributeMas1());
				else if (count == 3)
					return Optional.of(quoteObjectCreator.getProductAtrributeMas2());
				return Optional.empty();
			}
		});
		feasibilityService.processFeasibility(20);

	}

	@Test
	public void testProcessFeasibilityNegativeScenarioTest() throws Exception {

		when(mqUtilsMock.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) throws TclCommonException {
				if (count++ == 1)
					return quoteObjectCreator.getAddressDetail();

				return quoteObjectCreator.getCustomerDetailsBean();
			}
		});
		when(nplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuoteNplLinkListMFMP());
		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);

		feasibilityService.processFeasibility(20);

	}

	@Test
	public void testProcessFeasibilityResponse() throws Exception{
		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);
		when(mqUtilsMock.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getLocationDetailjson());
		String fresponse =Utils.convertObjectToJson((FeasibilityResponse)readJson("NplFeasibleResponse.json","com.tcl.dias.oms.npl.pricing.bean.FeasibilityResponse"));
		feasibilityService.processFeasibilityResponse(fresponse);
	}

	@Test
	public void testProcessFeasibilityResponseWithNotFeasibleLink() throws Exception {
		when(mqUtilsMock.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getLocationDetailjson());
		String response = Utils.convertObjectToJson((FeasibilityResponse)readJson("NplNotFeasibleResponse.json","com.tcl.dias.oms.npl.pricing.bean.FeasibilityResponse"));
		feasibilityService.processFeasibilityResponse(response);
	}

	@Test
	public void testProcessManualFp() throws Exception{
		FPRequest fpRequest = new FPRequest();
		FRequest fRequest = new FRequest();
		fRequest.setSiteFeasibilityId(11);
		fRequest.setFeasibilityType("Manual");
		fpRequest.setFeasiblility(fRequest);
		PRequest prequest = new PRequest();
		prequest.setComponentName("Last mile");
		prequest.setEffectiveArc(10000D);
		prequest.setEffectiveMrc(1000D);
		prequest.setEffectiveNrc(5000D);
		prequest.setPricingType("Manual");
		prequest.setSiteQuotePriceId(4);
		prequest.setTcv(50000D);
		List<PRequest> pricing = new ArrayList<>();
		pricing.add(prequest);
		fpRequest.setPricings(pricing);
		fpRequest.setTcv(50000D);

		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);

		feasibilityService.processManualFP(fpRequest, 530, 20);
	}
	
	@Test
	public void testProcessManualFpWithNullPriceId() throws Exception {

		FPRequest fpRequest = new FPRequest();
		PRequest prequest = new PRequest();
		prequest.setComponentName("Last mile");
		prequest.setEffectiveArc(10000D);
		prequest.setEffectiveMrc(1000D);
		prequest.setEffectiveNrc(5000D);
		prequest.setPricingType("Manual");
		prequest.setTcv(50000D);
		List<PRequest> pricing = new ArrayList<>();
		pricing.add(prequest);
		fpRequest.setPricings(pricing);
		fpRequest.setTcv(50000D);

		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);
		Optional<QuoteProductComponent> component = Optional.of(quoteObjectCreator.getQuoteProductComponent().get(0));
		when(mstProductComponentRepository.findByName(Mockito.anyString())).thenReturn(quoteObjectCreator.getMstProductComponent());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(), Mockito.any(),Mockito.anyString())).thenReturn(component);
		feasibilityService.processManualFP(fpRequest, 530, 20);
	
	}
	
	

	@Test
	public void testProcessManualFpWithAllComponents() throws TclCommonException, JsonParseException, JsonMappingException, ClassNotFoundException, IOException {
		FPRequest fpRequest = new FPRequest();
		FRequest fRequest = new FRequest();
		fRequest.setSiteFeasibilityId(11);
		fRequest.setFeasibilityType("Manual");
		fpRequest.setFeasiblility(fRequest);
		PRequest prequest = new PRequest();
		prequest.setComponentName("Last mile");
		prequest.setEffectiveArc(10000D);
		prequest.setEffectiveMrc(1000D);
		prequest.setEffectiveNrc(5000D);
		prequest.setPricingType("Manual");
		prequest.setSiteQuotePriceId(4);
		prequest.setTcv(50000D);
		List<PRequest> pricing = new ArrayList<>();
		pricing.add(prequest);
		fpRequest.setPricings(pricing);
		fpRequest.setTcv(50000D);

		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);
		when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuotePrice());
		when(mstProductComponentRepository.findById(Mockito.anyInt())).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) throws TclCommonException {
				count++;
				if (count == 1)
					return Optional.of(quoteObjectCreator.getMstProductComponent1());
				else if (count == 2)
					return Optional.of(quoteObjectCreator.getMstProductComponent4());
				else if (count == 3)
					return Optional.of(quoteObjectCreator.getMstProductComponent5());
				return Optional.empty();
			}
		});

		when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenAnswer(new Answer() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) throws TclCommonException {
						if (count++ == 1)
							return new ArrayList<>();

						return quoteObjectCreator.getQuoteProductComponent1();
					}
				});
		// when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent1());

		feasibilityService.processManualFP(fpRequest, 530, 20);
	}

	@Test
	public void testProcessManualFpForNonFeasible() throws TclCommonException, JsonParseException, JsonMappingException, ClassNotFoundException, IOException {
		FPRequest fpRequest = new FPRequest();
		FRequest fRequest = new FRequest();
		fRequest.setSiteFeasibilityId(11);
		fRequest.setFeasibilityType("Manual");
		fpRequest.setFeasiblility(fRequest);
		PRequest prequest = new PRequest();
		prequest.setComponentName("Last mile");
		prequest.setEffectiveArc(10000D);
		prequest.setEffectiveMrc(1000D);
		prequest.setEffectiveNrc(5000D);
		prequest.setPricingType("Manual");
		prequest.setSiteQuotePriceId(4);
		prequest.setTcv(50000D);
		List<PRequest> pricing = new ArrayList<>();
		pricing.add(prequest);
		fpRequest.setPricings(pricing);
		fpRequest.setTcv(50000D);

		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);
		when(linkFeasibilityRepository.findByQuoteNplLink_IdAndIsSelected(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getLinkFeasibilityList1());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(new ArrayList<>());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(), Mockito.anyString()))
				.thenReturn(new ArrayList<>());
		when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(mstProductComponentRepository.findById(Mockito.anyInt())).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) throws TclCommonException {
				count++;
				if (count == 1)
					return Optional.of(quoteObjectCreator.getMstProductComponent1());
				else if (count == 2)
					return Optional.of(quoteObjectCreator.getMstProductComponent4());
				else if (count == 3)
					return Optional.of(quoteObjectCreator.getMstProductComponent5());
				return Optional.empty();
			}
		});

		when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenAnswer(new Answer() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) throws TclCommonException {
						if (count++ == 1)
							return new ArrayList<>();

						return quoteObjectCreator.getQuoteProductComponent1();
					}
				});

		feasibilityService.processManualFP(fpRequest, 530, 20);
	}

	@Test
	public void testProcessErrorFeasilityResponse() throws TclCommonException {

		Map<String, String> response = new HashMap<>();
		response.put("503", "String");
		feasibilityService.processErrorFeasibilityResponse(response);
	}

	@Test
	public void testRecalculateSites() {
		feasibilityService.recalculateSites(20);
	}

	@Test
	public void testProcessPricingRequest() throws TclCommonException, JsonParseException, JsonMappingException, ClassNotFoundException, IOException {
		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);

		feasibilityService.processPricingRequest(20, 20);
	}
	
	@Test
	public void testProcessManualFeasibility() throws TclCommonException, JsonParseException, JsonMappingException, IOException, ClassNotFoundException {

		ManualFeasibilityRequest fRequest = new ManualFeasibilityRequest();
		fRequest.setLinkFeasibilityId(11);
		fRequest.setaHhName("name");
		fRequest.setaLmArcBwOnwl("test");
		fRequest.setaLmNrcBwOnwl("test");
		fRequest.setaLmNrcInbldgOnwl("test");
		fRequest.setaLmNrcMuxOnwl("test");
		fRequest.setaLmNrcNerentalOnwl("test");
		fRequest.setaLmNrcOspcapexOnwl("test");
		fRequest.setaMinHhFatg("test");
		fRequest.setbLmArcBwOnwl("test");
		fRequest.setbLmNrcBwOnwl("test");
		fRequest.setbLmNrcInbldgOnwl("test");
		fRequest.setbLmNrcMuxOnwl("test");
		fRequest.setbLmNrcNerentalOnwl("test");
		fRequest.setbLmNrcOspcapexOnwl("test");
		fRequest.setbMinHhFatg("test");
		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);

		feasibilityService.processManualFeasibility(fRequest, 530, 20);
	
	}
	
	@Test
	public void testProcessManualFeasibilityForNonFeasible() throws TclCommonException, JsonParseException, JsonMappingException, IOException, ClassNotFoundException {

		ManualFeasibilityRequest fRequest = new ManualFeasibilityRequest();
		fRequest.setLinkFeasibilityId(11);
		fRequest.setaHhName("name");
		fRequest.setaLmArcBwOnwl("test");
		fRequest.setaLmNrcBwOnwl("test");
		fRequest.setaLmNrcInbldgOnwl("test");
		fRequest.setaLmNrcMuxOnwl("test");
		fRequest.setaLmNrcNerentalOnwl("test");
		fRequest.setaLmNrcOspcapexOnwl("test");
		fRequest.setaMinHhFatg("test");
		fRequest.setbLmArcBwOnwl("test");
		fRequest.setbLmNrcBwOnwl("test");
		fRequest.setbLmNrcInbldgOnwl("test");
		fRequest.setbLmNrcMuxOnwl("test");
		fRequest.setbLmNrcNerentalOnwl("test");
		fRequest.setbLmNrcOspcapexOnwl("test");
		fRequest.setbMinHhFatg("test");
		PricingResponse response = (PricingResponse) readJson("NplPricingResponse.json","com.tcl.dias.oms.npl.pricing.bean.PricingResponse");
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(Utils.convertObjectToJson(response));
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);
		when(linkFeasibilityRepository.findByIdAndQuoteNplLink_Id(Mockito.anyInt(), Mockito.anyInt()))
		.thenReturn(Optional.of(quoteObjectCreator.getLinkFeasibility1()));

		feasibilityService.processManualFeasibility(fRequest, 530, 20);
	
	}
	
	private Object readJson(String fileName, String className) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		Class classs = Class.forName(className);
		InputStream is = NplPricingFeasibilityServiceTest.class.getResourceAsStream(fileName);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(is, classs);
	    
	   
	}
}
