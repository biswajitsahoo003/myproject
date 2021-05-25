package com.tcl.dias.oms.izosdwan.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.IzoSdwanCpeBomInterface;
import com.tcl.dias.common.beans.IzoSdwanSiteDetails;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesBean;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CurrencyConversionRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanPricingServiceRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasiblityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
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
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanMssPricingRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanByonUploadDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanCgwDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationCpeInfo;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanQuotePdfBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanSolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.NetworkSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeDetails;
import com.tcl.dias.oms.izosdwan.beans.SolutionLevelPricingBreakupDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SolutionPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanGvpnPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanIllPricingAndFeasiblityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.IzoSdwanObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class IzoSdwanQuoteTestService {

	@Autowired
	IzosdwanQuoteService izosdwanservice;
	
	@MockBean
	private ProductSolutionRepository productSolutionRepository;
	
	@Autowired
	private ObjectCreator objectCreator;
	
	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;
	
	
	@MockBean
	QuoteToLeRepository quoteToLeRepository;
	
	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@MockBean
	 QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	
	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@MockBean
	private CustomerRepository customerRepository;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	MQUtils mqUtils;

	@MockBean
	IllSiteRepository illSiteRepository;
	
	@MockBean
	QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;
	
	@Autowired
	IzosdwanGvpnPricingAndFeasibilityService izosdwanGvpnPricingAndFeasibilityService;


	@MockBean
	IzosdwanSiteFeasiblityRepository siteFeasibilityRepository;
	
	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	private QuoteRepository quoteRepository;
	
	@MockBean
	IzosdwanPricingServiceRepository izosdwanPricingServiceRepository;
	
	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;
	
	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;


	@Autowired
	private IzoSdwanObjectCreator sdwanObjectCreator;
	
	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@MockBean
	MstProductOfferingRepository mstProductOfferingRepository;
	
	@Autowired
	IzosdwanIllPricingAndFeasiblityService izosdwanIllPricingAndFeasiblityService;
	
	@MockBean
	QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository;
	
	@MockBean
	QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository;
	
	@MockBean
	QuoteIzosdwanCgwDetailRepository quoteIzosdwanCgwDetailRepository;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OmsSfdcService omsSfdcService;

	@MockBean
	QuoteDelegationRepository quoteDelegationRepository;

	@MockBean
	PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;

	@Autowired
	GvpnOrderService gvpnOrderService;

	@MockBean
	GvpnQuotePdfService gvpnQuotePdfService;

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
	AuthTokenDetailRepository authTokenDetailRepository;

	@MockBean
	MailNotificationBean mailNotificationBean;
	
	@MockBean
	DocusignAuditRepository docusignAuditRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;
	
	@MockBean
	CurrencyConversionRepository currencyConversionRepository;

	OmsUtilService omsUtilService;

	@MockBean
	QuoteIzoSdwanMssPricingRepository quoteIzoSdwanMssPricingRepository;

	
	@MockBean
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	
	@Test
	public void testcreateQuote() throws Exception {
		
		Mockito.when(quoteToLeRepository.findById(12)).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(objectCreator.getQuoteToLe(), objectCreator.geProductFamily())).thenReturn(objectCreator.getQuoteToLeFamily());
		
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive("trent", (byte) 1)).thenReturn(objectCreator.getMstAttributeList());
		
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(objectCreator.getQuoteToLe(), "trent")).thenReturn(objectCreator.getQuoteLeAttributeValueList());
		
		Mockito.when(quoteRepository.findByIdAndStatus(78771, (byte) 1)).thenReturn(objectCreator.getQuote());
		
		QuoteResponse response=izosdwanservice.createQuote(objectCreator.getQuoteDetails(),
				1);
		//IzosdwanQuoteService mock=createP
		//expect
		assertTrue(response.getQuoteId() !=null);
		
	}
	
	@Test
	public void testCreateInvalidCustomer() throws TclCommonException {
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(null);
		QuoteResponse response=izosdwanservice.createQuote(objectCreator.getQuoteDetails(), 0);
		assertTrue(response!=null);
	}
	
	@Test
	public void testcreateQuoteWithValidAndCustomerNull() throws Exception {
		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getCustomer());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuote()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteToLe());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.createProductSolutions());
		QuoteResponse response=izosdwanservice.createQuote(objectCreator.getQuoteDetails(), null);
		
		assertTrue(response!=null);
	}

	
	
	@Test
	public void testgetQuoteConfiguration() throws Exception {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when( quoteToLeRepository.findByQuote(objectCreator.getQuote())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(objectCreator.getQuoteToLe())).thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.anyString())).thenReturn(sdwanObjectCreator.getOrderConfirmationAudit());
		Mockito.when(cofDetailsRepository.findByOrderUuidAndSource(Mockito.anyString(),Mockito.anyString())).thenReturn(sdwanObjectCreator.getCofDetails());
      //  Mockito.when(docusignAuditRepository.findByOrderRefUuid(Mockito.anyString())).thenReturn(objectCreat)
		Mockito.when(quoteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.getQuote2()));
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());
		Mockito.when(siteFeasibilityRepository.findByQuoteIzosdwanSite(Mockito.any()))
				.thenReturn(sdwanObjectCreator.feasibilitySitesList());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamilyList());
	
	
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getSolutionList());
		
		QuoteBean response=izosdwanservice.getQuoteDetails(1, Mockito.anyString(), true, 1, sdwanObjectCreator.getLocIds());
		assertTrue(response!=null);
	}
	

//	@Test
//	public void getCpeDetails() throws TclCommonException {
//	Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString())).thenReturn(sdwanObjectCreator.getSdwanDetails());
//	List<IzoSdwanCpeDetails> response=izosdwanservice.getCpeDetails("IZO_SDWAN_SELECT", "premium", "Basic");
//	assertTrue(response!=null);
//	}
//	
//
//	@Test
//	public void getSiteDet() throws TclCommonException {
//		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(sdwanObjectCreator.getSiteDetails());
//		List<IzoSdwanSiteDetails> response=izosdwanservice.getSiteDetails();
//		assertTrue(response!=null);
//	}
	
	@Test
	public void getCpeSiteDetails() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolution(objectCreator.getSolution())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(quoteIzosdwanSiteRepository.getDistinctSiteTypesForSdwan(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getSiteTypes());
		Mockito.when(quoteIzosdwanSiteRepository.getUniqueBwLessThan2Mb(Mockito.anyInt(), Mockito.anyString())).thenReturn(sdwanObjectCreator.getIds());
		Mockito.when(quoteIzosdwanSiteRepository.getCountBasedOnOldBw(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(7677);
		Mockito.when(quoteIzosdwanSiteRepository.getCountOfRetainedBandwidth(Mockito.anyInt(),Mockito.anyString())).thenReturn(12);
		Mockito.when(quoteIzosdwanSiteRepository.getCountOfReplacedBandwidth(Mockito.anyInt(), Mockito.anyString())).thenReturn(23);
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteIzosdwanSiteRepository.getCountOfAutoUpgradedBwWhichWasUserUpgrade(Mockito.anyInt(), Mockito.anyString())).thenReturn(3);
		NetworkSummaryDetails response=izosdwanservice.getBandwidthDetails(656776);
		assertTrue(response!=null);
	}
	
	@Test
	public void getSiteTypeDetails() {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolutionAndIzosdwanSiteType(objectCreator.getSolution(), "Single IAS Single CPE")).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		List<SiteTypeDetails> response=izosdwanservice.getSiteTypeDetails(166612);
		assertTrue(response!=null);
	}

	@Test
	public void getConfigurationCpeDetails() throws TclCommonException {

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolutionAndIzosdwanSiteType(objectCreator.getSolution(), "Single IAS Single CPE"))
				.thenReturn(sdwanObjectCreator.getIzoSdwanSites());

		Mockito.when(quoteIzosdwanSiteRepository.getBandwidth(1, "Primary",
				"Single IAS Single CPE", "GVPN")).thenReturn("200-300");
		Mockito.when(quoteIzosdwanSiteRepository.getBandwidth(1, "Primary",
				"Single IAS Single CPE", "IAS")).thenReturn("200-300");
		Mockito.when(quoteIzosdwanSiteRepository.getBandwidth(1, "Primary",
				"Single IAS Single CPE", IzosdwanCommonConstants.BYON_INTERNET_PRODUCT)).thenReturn("200-300");

		Mockito.when(quoteIzosdwanSiteRepository.getBandwidth(1, "SECONDARY",
				"Single IAS Single CPE", "GVPN")).thenReturn("200-300");
		Mockito.when(quoteIzosdwanSiteRepository.getBandwidth(1, "SECONDARY",
				"Single IAS Single CPE", "IAS")).thenReturn("200-300");
		Mockito.when(quoteIzosdwanSiteRepository.getBandwidth(1, "SECONDARY",
				"Single IAS Single CPE", IzosdwanCommonConstants.BYON_INTERNET_PRODUCT)).thenReturn("200-300");


		ConfigurationSummaryBean response=izosdwanservice.getConfigurationCpeDetails(24260, "Single IAS Single CPE");
		assertTrue(response!=null);
	}
	
	@Test
	public void getSitesBasedOnSiteType() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(objectCreator.getQuote());
		String resp = Utils.convertObjectToJson(sdwanObjectCreator.locs());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(sdwanObjectCreator.getLocIds());
		List<ViewSitesSummaryBean> response=izosdwanservice.getSitesBasedOnSiteType(24260, "Single IAS Single CPE", sdwanObjectCreator.getInputDetails());
		assertTrue(response!=null);
	}
	
	
	@Test
	public void updateSitePropertiesAttributes() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(objectCreator.getQuote());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.geProductFamily());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(12213, objectCreator.getMstProductComponent())).thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.getProductAtrributeMaster());
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentAndProductAttributeMaster(sdwanObjectCreator.getComponent(), objectCreator.getProductAtrribute())).thenReturn(objectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(quoteIzosdwanSiteRepository.saveAndFlush(sdwanObjectCreator.getIzoSdwanSite())).thenReturn(sdwanObjectCreator.getIzoSdwanSite());
		QuoteDetail response=izosdwanservice.updateSitePropertiesAttributes(objectCreator.getUpdateRequestList());
		assertTrue(response!=null);
	}
	@Test
	public void testProcessFeasibilityTest() throws Exception {

		when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) throws TclCommonException {
				count++;
				if (count == 1)
					return objectCreator.getCustomerDetailsBean();
				else if (count == 2)
					return objectCreator.getCustomerDetailsBean();
				else if (count == 3)
					return objectCreator.getLocationDetail();
				return objectCreator.getAddressDetailJSON();
			}
		});
		when(productAttributeMasterRepository.findById(Mockito.anyInt())).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) throws TclCommonException {
				count++;
				if (count == 1)
					return Optional.of(objectCreator.getProductAtrributeMas());
				else if (count == 2)
					return Optional.of(objectCreator.getProductAtrribute());
				else if (count == 3)
					return Optional.of(objectCreator.getProductAtrributeMas2());
				return Optional.empty();
			}
		});
		izosdwanGvpnPricingAndFeasibilityService.processFeasibility(20,"GVPN");
		
	}
	
	@Test
	public void testPersistSolutionDetailsTest() throws Exception {
		Mockito.when(quoteToLeRepository.findById(39202)).thenReturn(objectCreator.getOptionalQuoteToLe());
		 Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
	        .thenReturn(objectCreator.getUser());
		Mockito.when(quoteRepository.findById(39216)).thenReturn(sdwanObjectCreator.getquoteDetail());
		Mockito.when(quoteRepository.findByIdAndStatus(39216, (byte)1)).thenReturn(sdwanObjectCreator.getQuote());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus("IZOSDWAN", (byte)1)).thenReturn(objectCreator.geProductFamily());
	    Mockito.when(quoteRepository.save(sdwanObjectCreator.getQuote())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(sdwanObjectCreator.getQuoteToLe(),objectCreator.geProductFamily())).thenReturn(sdwanObjectCreator.getQuoteToLeFamily());
	    Mockito.when(quoteToLeProductFamilyRepository.save(sdwanObjectCreator.getQuoteToLeFamily())).thenReturn(sdwanObjectCreator.getQuoteToLeFamily());
	    Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(sdwanObjectCreator.getLeProductFamily())).thenReturn(objectCreator.getProductSolutionList());
	    Mockito.when(quoteIzosdwanByonUploadDetailRepository.findByStatusAndQuote_id("IZOSDWAN",39202)).thenReturn(sdwanObjectCreator.getByonUploadDetails());
	    Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatusAndVendorCd(sdwanObjectCreator.geProductFamily(),"IZOSDWAN",(byte)1,"IZO_SDWAN_SELECT")).thenReturn(objectCreator.getMstProductOffering());
	    Mockito.when(mstProductOfferingRepository.save(objectCreator.getMstOffering())).thenReturn(objectCreator.getMstOffering());
	    Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(sdwanObjectCreator.getLeProductFamily(),sdwanObjectCreator.getMstOffering())).thenReturn(sdwanObjectCreator.getSolution());
	    Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
	    Mockito.when(quoteIzoSdwanAttributeValuesRepository.saveAll(sdwanObjectCreator.getSdwanAttrValues())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
	    Mockito.when(productSolutionRepository.save(sdwanObjectCreator.getSolution())).thenReturn(sdwanObjectCreator.getSolution());
	    Mockito.when(quoteIzosdwanSiteRepository.findByProductSolutionAndStatus(sdwanObjectCreator.getSolution(),(byte)1)).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
	    Mockito.when(quoteIzosdwanSiteRepository.saveAll(sdwanObjectCreator.getIzoSdwanSites())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
	    QuoteResponse response=izosdwanservice.persistSolutionDetails(sdwanObjectCreator.getQuoteDetailSdwan());
	    assertTrue(response != null);
	}
	
	@Test
	public void getPriceInformationForTheQuote() throws Exception {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(objectCreator.getSolution());
		Mockito.when(quoteProductComponentRepository.
				findByReferenceIdAndMstProductComponent_NameAndReferenceName(Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).
		thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString())).thenReturn(sdwanObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolution(Mockito.any())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString())).
		thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getSdwanAttrValues1());
		QuotePricingDetailsBean response=izosdwanservice.getPriceInformationForTheQuote(1);
		assertTrue(response!=null);
	}
	
	@Test
	public void getQuoteDetailsTest() throws Exception{
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(sdwanObjectCreator.getQuote())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(quoteRepository.findByIdAndStatus(39269, (byte)1)).thenReturn(sdwanObjectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(39269)).thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getDistinctSiteTypesForSdwan(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getSiteTypes());
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolutionAndIzosdwanSiteType(objectCreator.getSolution(),"dsffs")).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		QuoteBean response=izosdwanservice.getQuoteDetails(39269, "all",true, null,objectCreator.getIllSitesId() );
		assertTrue(response!=null);

	}

	@Test(expected = Exception.class)
	public void persistIzosdwanSiteDetailsTest() throws  Exception{
		Mockito.when(quoteRepository.findById(1)).thenReturn(Optional.of(objectCreator.getQuote()));

//		Mockito.when(quoteRepository.findById(39216)).thenReturn(objectCreator.getQuote());
//		Mockito.when(quoteToLeRepository.findById(39269)).thenReturn(objectCreator.getOptionalQuoteToLeWithSitesśś());
//		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(1)).thenReturn(objectCreator.getSolution());
//		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolution(objectCreator.getSolution())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
//		Mockito.when(productAttributeMasterRepository.findAll()).thenReturn(objectCreator.getProductAtrributeMaster());
//		Mockito.when(mstProductComponentRepository.saveAndFlush(sdwanObjectCreator.getMstProductComponent4())).thenReturn(sdwanObjectCreator.getMstProductComponent4());
//		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.MINCONTRACTTERM,Mockito.anyInt())).
//				thenReturn(sdwanObjectCreator.getSdwanAttrValues());
//		Mockito.when(quoteIzoSdwanAttributeValuesRepository.saveAndFlush(sdwanObjectCreator.getIzoSdwanAttribute())).thenReturn(sdwanObjectCreator.getIzoSdwanAttribute());
//		Mockito.when(quoteIzoSdwanAttributeValuesRepository.saveAll(sdwanObjectCreator.getSdwanAttrValues())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		
		String mqResponse = Utils.convertObjectToJson(Stream.of(objectCreator.constructSiBean()).collect(Collectors.toList()));
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(mqResponse);

		String resp = Utils.convertObjectToJson(sdwanObjectCreator.getSiteDetails());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(resp);


		String response = izosdwanservice.persistIzoSdwanSiteDetails(1, sdwanObjectCreator.getQuoteToLe());
		assertTrue(response!=null);
	}

	@Test
	public void testFeasibleSite() throws Exception {

		Mockito.when(quoteIzosdwanSiteRepository.findById(1))
				.thenReturn(sdwanObjectCreator.getOptionalsiteDetails());
		Mockito.when(siteFeasibilityRepository.findByQuoteIzosdwanSite(Mockito.any()))
		.thenReturn(sdwanObjectCreator.feasibilitySitesList());
//		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.any(),null)).thenReturn(sdwanObjectCreator.getPricingDetails());
		QuoteIllSitesWithFeasiblityAndPricingBean response = izosdwanservice.getFeasiblityAndPricingDetailsForQuoteIllSites(1);
		assertTrue(response!=null);

	}

	@Test
	public void testPersistListOfQuoteLeAttributes() throws Exception{

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());
		Mockito.when(quoteToLeRepository.findById(1))
				.thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		QuoteDetail response = izosdwanservice.persistListOfQuoteLeAttributes(objectCreator.getUpdateRequest());
		assertTrue(response!=null);
	}


	@Test
	public void getCpeSiteDetailsNegative() throws TclCommonException {

		NetworkSummaryDetails response=izosdwanservice.getBandwidthDetails(null);
		assertTrue(response!=null);
	}

	@Test
	public void getgetSiteDetails() throws TclCommonException {
		String resp = Utils.convertObjectToJson(sdwanObjectCreator.getSiteDetails());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(resp);
		List<IzoSdwanSiteDetails> response=izosdwanservice.getSiteDetails();
		assertTrue(response!=null);
	}

	@Test
	public void getPublicIpTest() throws Exception{
		String response = izosdwanservice.getPublicIp("%3,%2");
		assertTrue(response!=null);
	}

	@Test
	public void getProfileSelectionDetailsTest() throws Exception{

		Mockito.when(quoteToLeRepository.findByQuote_Id(34567)).thenReturn(objectCreator.getQuoteToLeList());
		Integer num = 1;
		String resp = Utils.convertObjectToJson(num);
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(resp);
//		String resp2 = Utils.convertObjectToJson(sdwanObjectCreator.getvendorProfileDetailsList());
//		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(resp2);

		List<VendorProfileDetailsBean> response =  izosdwanservice.getProfileSelectionDetails(sdwanObjectCreator.getProfileSelectionInputDetails());
		assertTrue(response!=null);
	}

	@Test
	public void getInterfaceDetailsTest() throws Exception {
		
		String resp = Utils.convertObjectToJson(sdwanObjectCreator.getcpeBomInterfaces());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(resp);
		List<IzoSdwanCpeBomInterface> response = izosdwanservice.getInterfaceDetails();
		assertTrue(response!=null);
	}

	@Test
	public void getSiteDetailsTest() throws Exception {

		String resp = Utils.convertObjectToJson(sdwanObjectCreator.getSdwanDetails());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(resp);
		List<IzoSdwanSiteDetails> response = izosdwanservice.getSiteDetails();
		assertTrue(response!=null);
	}

	@Test
	public void getcpeInfoTest() throws Exception{
		ConfigurationCpeInfo response = izosdwanservice.getCpeInfo(sdwanObjectCreator.getCpeInfo(), "cpe");
		assertTrue(response!=null);
	}

	@Test
	public void getAccountManagersEmailTest() throws Exception{
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive("SUPPLIER_LE_EMAIL", (byte) 1)).thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(objectCreator.getQuoteToLe(), "Payment Currency"))
				.thenReturn(objectCreator.getQuoteLeAttributeValueList());
		String reponse = izosdwanservice.getAccountManagersEmail(objectCreator.getQuoteToLe());
		assertTrue(reponse!=null);
	}

	@Test
	public void checkQuoteLeFeasibilityTest() throws Exception{

		Mockito.when(quoteToLeRepository.findById(1)).thenReturn(objectCreator.returnQuoteToLeForUpdateStatusForContactAttributeInfo2());
		QuoteLeAttributeBean response = izosdwanservice.checkQuoteLeFeasibility(1);
		assertTrue(response!=null);

	}

	@Test
	public void checkQuoteLeFeasibilityTestCase2() throws Exception{

		Mockito.when(quoteToLeRepository.findById(1)).thenReturn(objectCreator.returnQuoteToLeForUpdateStatusForContactAttributeInfo3());
		QuoteLeAttributeBean response = izosdwanservice.checkQuoteLeFeasibility(1);
		assertTrue(response!=null);

	}

	@Test
	public void getAllAttributesByQuoteToLeIdTest() throws Exception{

		Mockito.when(quoteToLeRepository.findById(1)).thenReturn(objectCreator.getOptionalQuoteToLe());
		Set<LegalAttributeBean> response = izosdwanservice.getAllAttributesByQuoteToLeId(1);
		assertTrue(response!=null);
 	}

//	@Test
//	public void getLeAttributesTest() throws Exception{
//		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive("Payment Currency",(byte)1)).thenReturn(sdwanObjectCreator.getMstAttributeList());	
//	    Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(sdwanObjectCreator.getQuoteToLe(),sdwanObjectCreator.getMstAttribute())).thenReturn(sdwanObjectCreator.getQuoteLeAttributeValueList());
//	    String htmlData=izosdwanservice.getLeAttributes(sdwanObjectCreator.getQuoteToLe(),"Payment Currency");
//	    assertTrue(htmlData != null && !htmlData.isEmpty());
//	}
//	
	@Test
	public void updateLegalEntityPropertiesTest() throws Exception{
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(objectCreator.getQuoteToLe()));
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(sdwanObjectCreator.getMstAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteLeAttributeValue());
		//Mockito.when(quoteToLeRepository.save(new QuoteToLe())).thenReturn(objectCreator.getQuoteToLe());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(objectCreator.craeteUser());
		QuoteDetail response = izosdwanservice.updateLegalEntityProperties(objectCreator.getUpdateRequest());
		assertTrue(response != null);
	}
	
	@Test
	public void testupdateLegalEntityPropertiesWithMstNull() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.ofNullable(objectCreator.getQuoteToLe()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(new ArrayList<>());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValue());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(objectCreator.craeteUser());
		QuoteDetail response = izosdwanservice.updateLegalEntityProperties(objectCreator.getUpdateRequest());
		assertTrue(response!=null);
	}
	
	@Test(expected=Exception.class)
	public void testUpdateLegalEntityPropertiesForQuote() throws TclCommonException {

		/*
		 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
		 * anyString(), Mockito.anyByte()))
		 * .thenReturn(izoPcObjectCreator.getMstAttributeList());
		 */

		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValue());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

		UpdateRequest request = objectCreator.getUpdateRequest();
		izosdwanservice.updateLegalEntityProperties(request);
	}
	
	@Test
	public void testUpdateCurrency() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(omsUtilService.convertCurrency(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(1D);
		Mockito.when(quoteToLeRepository.save(new QuoteToLe())).thenReturn(objectCreator.getQuoteToLe());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName
				(Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalQuoteToLeWithSites());
		Mockito.when(quotePriceRepository.findByReferenceId(Mockito.anyString())).thenReturn(objectCreator.geQuotePrice());
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(objectCreator.getIllsites());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt())).thenReturn(objectCreator.getQuotePriceList());
		Mockito.when(omsUtilService.convertCurrency(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(1D);
	    Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(objectCreator.getQuotePrice());
	    Mockito.when(quoteToLeRepository.saveAndFlush(new QuoteToLe())).thenReturn(objectCreator.getQuoteToLe());
	    //Mockito.when(omsSfdcService.)
		izosdwanservice.updateCurrency(1, 1, "USD");
		assertTrue(true);
	}
	
	@Test
	public void testProcessMailAttachment() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt()))
		.thenReturn(sdwanObjectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getUniqueLocationIds(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getIllSitesId());
		Mockito.when( quoteIzosdwanSiteRepository.findByErfLocSitebLocationIdAndProductSolution(Mockito.anyInt(),Mockito.any(ProductSolution.class))).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString())).thenReturn(sdwanObjectCreator.getAddressDetailJSON());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt()))
		.thenReturn(sdwanObjectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.findByIdInAndStatus(Mockito.any(),Mockito.anyByte())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		
//		ServiceResponse responseResource = izosdwanservice.processMailAttachment( "izopc@gmail.com",1);
//		assertTrue(responseResource != null);

	}

    @Test
    public void getPriceSitewiseTest() throws Exception{
        Mockito.when(quoteRepository.findByIdAndStatus(1234, (byte) 1)).thenReturn(objectCreator.getQuote());
        Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(objectCreator.getSolution());
        Mockito.when(quoteIzosdwanSiteRepository.findByIdInAndStatus(Mockito.any(),Mockito.anyByte())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());

        SolutionPricingDetailsBean response = izosdwanservice.getPriceSiteWise(sdwanObjectCreator.getPriceRequestBean());
        assertTrue(response != null);
    }

    @Test
    public void testProcessQuotePdf() throws Exception {
        Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
                .thenReturn(objectCreator.getQuote());
        Mockito.when(quoteIzosdwanSiteRepository.findByProductSolution(objectCreator.getSolution())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
        izosdwanservice.processQuotePdf(1, new MockHttpServletResponse(), 1);
        assertTrue(true);

    }

	@Test
	public void updateLeAttribute() throws Exception{
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(sdwanObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),Mockito.anyString())).thenReturn(sdwanObjectCreator.getQuoteLeAttributeValueList());
	    izosdwanservice.updateLeAttribute(sdwanObjectCreator.getQuoteToLe(), null, "Test", "Description");
	    assertTrue(true);
	}
	 
	
	@Test
	public void updateLeAttributeWithMstNull() throws Exception{
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(sdwanObjectCreator.getMstAttributeList());
		Mockito.when(mstOmsAttributeRepository.save(sdwanObjectCreator.getMstAttribute())).thenReturn(sdwanObjectCreator.getMstAttribute());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),Mockito.anyString())).thenReturn(sdwanObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteLeAttributeValueRepository.saveAll(objectCreator.getQuoteLeAttributeValueList())).thenReturn(objectCreator.getQuoteLeAttributeValueList());
		izosdwanservice.updateLeAttribute(null, null, null, null);
	    assertTrue(true);
	}
	
	@Test 
	public void createQuoteProductComponentIfNotPresentTest()
	{
		Mockito.when(quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(sdwanObjectCreator.getComponent());
	  Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(sdwanObjectCreator.getMstProductComponentList());
	  Mockito.when(quoteProductComponentRepository.save(sdwanObjectCreator.getComponent())).thenReturn(sdwanObjectCreator.getComponent());
	  izosdwanservice.createQuoteProductComponentIfNotPresent(null, "CPE",null,null,IzosdwanCommonConstants.IZOSDWAN_SITES);
	    assertTrue(true);
	
	}
	
	@Test
	public void updateTermsInMonthsForQuoteToLeTest() throws Exception{
	Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(objectCreator.getQuote()));
	Mockito.when(quoteToLeRepository.findByQuoteAndId(Mockito.any(),Mockito.anyInt())).thenReturn(objectCreator.getQuoteToLe());
	Mockito.when(quoteToLeRepository.saveAndFlush(sdwanObjectCreator.getQuoteToLe())).thenReturn(sdwanObjectCreator.getQuoteToLe());
	izosdwanservice.updateTermsInMonthsForQuoteToLe(1,1,sdwanObjectCreator.getUpdateRequest(),null);
    assertTrue(true);

	}
	
	@Test
	public void getDistinctCpesTest() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(sdwanObjectCreator.getQuote());
		Mockito.when(productSolutionRepository
						.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getDistinctCpeForSdwan(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getCpe());
		List<String> response=izosdwanservice.getDistinctCpes(1);
		assertTrue(response != null && !response.isEmpty());
	}
	
	@Test
	public void test() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when( productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getUniqueLocationIds(Mockito.anyInt())).thenReturn(objectCreator.getIllSitesId());
		IzosdwanQuotePdfBean response=izosdwanservice.test(1);
		assertTrue(response!=null);
		
		
		
	}
	

	@Test
    public void validateAddressDetailTest() throws Exception{
        AddressDetail response = izosdwanservice.validateAddressDetail(sdwanObjectCreator.getaddressdetailNull());
        assertTrue(response !=null);
    }

    @Test
    public void updateQuoteToLeCurrencyValuesTest() throws Exception{
        Mockito.when(quoteToLeRepository.findByQuote(objectCreator.getQuote())).thenReturn(objectCreator.getQuoteToLeList());
//        Mockito.when(omsUtilService.convertCurrency(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(1D);
	    izosdwanservice.updateQuoteToLeCurrencyValues(objectCreator.getQuote(), "1D", "2D");
	    assertTrue(true);
    }

    @Test
    public void getSiteComponentsTest2() throws Exception{
        Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName
                (Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getQuoteProductComponentVpn());
        List<QuoteProductComponent> response =  izosdwanservice.getSiteComponents(objectCreator.getIllsites());
        assertTrue(response != null);
    }

    @Test
    public void updateQuoteIllSitesCurrencyValuesTest() throws Exception{
        Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName
                (Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getQuoteProductComponent());
        Mockito.when(quotePriceRepository.findByReferenceId("1")).thenReturn(objectCreator.geQuotePrice());
//      Mockito.when(omsUtilService.convertCurrency("2D","1D",10.24)).thenReturn(1D);
//		Mockito.when(currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
//				.thenReturn(Optional.ofNullable(objectCreator.getCurrencyConversionRate()));
        izosdwanservice.updateQuoteIllSitesCurrencyValues(objectCreator.getQuoteToLe(), "1D","2D");
        assertTrue(true);
    }

    @Test
	public void triggerFeasibilityTest()throws  Exception{
		Mockito.when(quoteToLeRepository.findById(1))
				.thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByDisplayValueAndQuote_id("BYON100P",1))
				.thenReturn(sdwanObjectCreator.getIzoSdwanVal2());
		String resp = Utils.convertObjectToJson(sdwanObjectCreator.getcpeBomInterfaces());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(resp);
		izosdwanservice.triggerFeasibility(sdwanObjectCreator.getFeasibilityBean());
	}

	@Test
	public void triggerFeasibilityTest2()throws  Exception{
		Mockito.when(quoteToLeRepository.findById(1))
				.thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByDisplayValueAndQuote_id("BYON100P",1))
				.thenReturn(sdwanObjectCreator.getIzoSdwanVal3());
		String resp = Utils.convertObjectToJson(sdwanObjectCreator.getcpeBomInterfaces());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(resp);

		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getCustomerDetailsBean());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(sdwanObjectCreator.CustomerLegalEntityDetailsBeanDets());

//		String resp2 = Utils.convertObjectToJson()

		izosdwanservice.triggerFeasibility(sdwanObjectCreator.getFeasibilityBean());
	}

	@Test
	public void getDefaultBandwidthForCgwTest() throws Exception{

//		Mockito.when(quoteRepository.findByIdAndStatus(1, (byte) 1)).thenReturn(objectCreator.getQuote());
//		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(1)).thenReturn(objectCreator.getSolution());
//		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(sdwanObjectCreator.getQuote())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuote());


		String response = izosdwanservice.getDefaultBandwidthForCgw(1, sdwanObjectCreator.getAttributeValuesSdwancgw());
		assertTrue(response != null);
	}

	@Test
	public void getDefaultBandwidthForCgwTest2() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(1)).thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getDefaultPortBandwidth(1)).thenReturn("120.24");
		Mockito.when(quoteIzosdwanCgwDetailRepository.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getCgwDetail());
		Mockito.when(quoteIzosdwanSiteRepository.getDistinctSiteTypesForSdwan(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getSiteTypes());
		String response = izosdwanservice.getDefaultBandwidthForCgw(1, null);
		assertTrue(response != null);
	}
	
	@Test
	public void getByonLocationValidationStatusTest() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository
					.findByStatusInAndQuote_id(Mockito.anyList(),Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository
						.findByQuote_idAndLocationErrorDetailsIsNotNull(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository
							.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.save(sdwanObjectCreator.getIzoSdwanAttribute())).thenReturn(sdwanObjectCreator.getIzoSdwanAttribute());
		Boolean response = izosdwanservice.getByonLocationValidationStatus(1);
		assertTrue(response != true);
	}
	
	@Test
	public void uploadByonSitesAgainstTheQuoteTest() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
        Mockito.when( productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).
        thenReturn(sdwanObjectCreator.getSolution());
        Mockito.when( userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());
        String resp = Utils.convertObjectToJson(sdwanObjectCreator.getSiteDetails());
		Mockito.when(mqUtils.sendAndReceive(Mockito.any(), Mockito.any())).thenReturn(resp);
    	//String resp1 = Utils.convertObjectToJson(sdwanObjectCreator.getCpeDetailsBean());
		//Mockito.when(mqUtils.sendAndReceive(Mockito.any(), Mockito.any())).thenReturn(resp1);
		Mockito.when(productAttributeMasterRepository.findAll()).thenReturn(sdwanObjectCreator.getProductAtrributeMaster());
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString())).thenReturn(sdwanObjectCreator.getMstProductComponent());
        Mockito.when(mstProductComponentRepository.saveAndFlush(sdwanObjectCreator.getMstProductComponent1())).
        thenReturn(sdwanObjectCreator.getMstProductComponent1());
		String response = izosdwanservice.uploadByonSitesAgainstTheQuote(1);
		assertTrue(response!=null && !response.isEmpty());
	}
	
	@Test
	public void checkByonDetailsUploadedOrNot() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository
						.findByQuote_id(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		Boolean response = izosdwanservice.checkByonDetailsUploadedOrNot(1);
		assertTrue(response == false);
		}
	
	@Test
	public void updateQuoteStage() throws Exception{
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteToLeRepository.saveAndFlush(objectCreator.getQuoteToLe())).thenReturn(objectCreator.getQuoteToLe());
	    String response=izosdwanservice.updateQuoteStage(1, 1,"Get Quote");
	    assertTrue(response !=null && !response.isEmpty());
	}
	
	@Test
	public void deleteQuote() throws Exception{
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn
		(Optional.of(objectCreator.getQuote()));
		Mockito.when(orderRepository.findByQuote(Mockito.any())).thenReturn(Optional.of(objectCreator.getOrder()));
		Mockito.when(orderProductComponentRepository
        .findByReferenceId(Mockito.anyInt())).thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(
                Mockito.anyString(),Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());
		Mockito.when(orderIllSiteToServiceRepository.findByOrderIllSite(objectCreator.getOrderIllSite())).thenReturn(objectCreator.getOrderIllSiteToServiceList());
		//Mockito.when(orderPriceRepository.delete(Mockito.any())).thenReturn();
		//ckito.when( quoteRepository.delete(objectCreator.getQuote());
		izosdwanservice.deleteQuote(1);
		assertTrue(true);
	}
	
	@Test
	public void getPricingServiceCompletionStatusByQuote() throws Exception{
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn
		(Optional.of(objectCreator.getQuote()));
		Mockito.when( izosdwanPricingServiceRepository
						.findByRefIdAndStatusIn
						(Mockito.any(),Mockito.any())).thenReturn(sdwanObjectCreator.getPricingListsss());
	Boolean response=izosdwanservice.getPricingServiceCompletionStatusByQuote(1,1);
	assertTrue(response != true);
	}
	
	

	@Test
	public void recalculateTest() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		List<IzosdwanQuoteAttributesBean> response = izosdwanservice.getAllAttributesByQuoteId(1);
		assertTrue(response != null);
	}

	@Test
	public void recalculateTestNegative() throws Exception{
		List<IzosdwanQuoteAttributesBean> response = izosdwanservice.getAllAttributesByQuoteId(null);
		assertTrue(response != null);
	}
	@Test
	public void recalculateTestNegative2() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(null);
		List<IzosdwanQuoteAttributesBean> response = izosdwanservice.getAllAttributesByQuoteId(1);
		assertTrue(response != null);
	}

	@Test
	public void checkByonDetailsUploadedOrNotTest() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository.findByQuote_id(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetailsStatus());
		Boolean response = izosdwanservice.checkByonDetailsUploadedOrNot(1);
		assertTrue(response != null);
	}

	@Test
	public void checkByonDetailsUploadedOrNotTest2() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository.findByQuote_id(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetailsStatus2());
		Boolean response = izosdwanservice.checkByonDetailsUploadedOrNot(1);
		assertTrue(response != null);
	}

	@Test
	public void getUniqueCountriesForTheQuoteTest() throws Exception{
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolution(Mockito.any())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		List<String> response = izosdwanservice.getUniqueCountriesForTheQuote(1);
		assertTrue(response != null);
	}

	@Test
	public void getSolutionLevelChargesForIzosdwanTest() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzosdwanCgwDetailRepository.findByQuote_Id(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getCgwDetail());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName
				(1,"IZOSDWAN_CGW")).thenReturn(objectCreator.getQuoteProductComponentVpn());
		Mockito.when(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("COMPONENTS","1",1)).thenReturn(objectCreator.geQuotePrice());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getSdwanAttrValues1());
		List<IzosdwanSolutionLevelCharges>response = izosdwanservice.getSolutionLevelChargesForIzosdwan(1);
		assertTrue(response!=null);
	}

	@Test
	public void getSolutionLevelBreakUpPricingDetailForCGWTest() throws Exception {

		Mockito.when(quoteIzosdwanCgwDetailRepository.findByQuote_Id(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getCgwDetail());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName
				(1, "IZOSDWAN_CGW")).thenReturn(objectCreator.getQuoteProductComponentCgp());
		Mockito.when(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("COMPONENTS", "1", 1)).thenReturn(objectCreator.geQuotePrice());
		List<SolutionLevelPricingBreakupDetailsBean> response = izosdwanservice.getSolutionLevelBreakUpPricingDetailForCGW(1);
		assertTrue(response != null);
	}

	@Test
	public void addOrModifyQuoteAttributeTest() throws Exception {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		String response = izosdwanservice.addOrModifyQuoteAttribute(sdwanObjectCreator.getSdwanUpdateBean2());
		assertTrue(response != null);
	}

	@Test
	public void getVproxySolutionDetailsTesyt() throws Exception {
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.geProductFamily());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.createProductSolutions());
		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatusAndVendorCd
				(sdwanObjectCreator.geProductFamily(),"IZOSDWAN",(byte)1,"IZO_SDWAN_SELECT")).
				thenReturn(objectCreator.getMstProductOffering());
		String response = izosdwanservice.addVproxySolutions(sdwanObjectCreator.getVproxySolutionDetails2());
		assertTrue(response != null);
	}

    @Test
    public void getVproxySolutionDetailsTest2() throws Exception {
        Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.geProductFamily());
        Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalQuoteToLe());
        Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
                .thenReturn(null);
//        Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
//                .thenReturn(objectCreator.createProductSolutions());
//        Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatusAndVendorCd
//                (sdwanObjectCreator.geProductFamily(),"IZOSDWAN",(byte)1,"IZO_SDWAN_SELECT")).
//                thenReturn(objectCreator.getMstProductOffering());
        String response = izosdwanservice.addVproxySolutions(sdwanObjectCreator.getVproxySolutionDetails2());
        assertTrue(response != null);
    }

    @Test
    public void getVproxyProfileDetailsTest() throws Exception{

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());

		        Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
        String resp = Utils.convertObjectToJson(sdwanObjectCreator.getVproxySolutionDet());
        Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(resp);

        List<VproxySolutionsBean> response = izosdwanservice.getVproxyProfileDetails(1);
        assertTrue(response!=null);
    }

	@Test
	public void persistVproxyPricingDetailsTest() throws Exception {

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getIzoSdwanVal4());
		Mockito.when(productSolutionRepository.findByReferenceIdForVproxy(1)).thenReturn(objectCreator.createProductSolutions());
//		Mockito.when(quoteIzoSdwanMssPricingRepository.findBySolutionId(1)).thenReturn(sdwanObjectCreator.getQuoteIzoSdwanMssPricingDetails());
		String response = izosdwanservice.persistVproxyPricingDetails(1);
		assertTrue(response!= null);
	}

	@Test
	public void getVproxySolutionLevelChargesTest() throws Exception{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(Mockito.any())).thenReturn(sdwanObjectCreator.getSdwanAttrValues1());
		Mockito.when(productSolutionRepository.findByReferenceIdForVproxy(1)).thenReturn(objectCreator.createProductSolutions());
		Mockito.when(quoteIzoSdwanMssPricingRepository.findBySolutionId(1)).thenReturn(sdwanObjectCreator.getQuoteIzoSdwanMssPricingDetailslist());
		List<VproxySolutionLevelCharges>response = izosdwanservice.getVproxySolutionLevelCharges(1);
		assertTrue(response!=null);
	}

}