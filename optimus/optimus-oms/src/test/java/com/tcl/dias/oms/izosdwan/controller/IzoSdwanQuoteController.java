package com.tcl.dias.oms.izosdwan.controller;



import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.IzoSdwanCpeDetails;
import com.tcl.dias.common.beans.IzoSdwanSiteDetails;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ByonUploadResponse;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
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
import com.tcl.dias.oms.entity.repository.SfdcJobRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanQuotePdfBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanSolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.NetworkSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeDetails;
import com.tcl.dias.oms.izosdwan.beans.SolutionLevelPricingBreakupDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SolutionPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.izosdwan.controller.v1.IzosdwanQuoteController;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanGvpnPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanIllPricingAndFeasiblityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.utils.IzoSdwanObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IzoSdwanQuoteController {

	@Autowired
	IzosdwanQuoteController izosdwanQuoteController;

	@Autowired
	private ObjectCreator objectCreator;

	@MockBean
	IzosdwanQuoteService izosdwanservice;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	private QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	MQUtils mqUtils;
	

	@MockBean
	IllSiteRepository illSiteRepository;

	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@MockBean
	private QuotePriceRepository quotePriceRepository;

	@MockBean
	private QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private QuoteRepository quoteRepository;
	
	@MockBean
	QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;


	@Autowired
	private IzoSdwanObjectCreator sdwanObjectCreator;
	
	@MockBean
	private ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;

	@MockBean
	private ProductSolutionRepository productSolutionRepository;

	@MockBean
	private MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	private MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@MockBean
	SfdcJobRepository sfdcJobRepository;
	

	

	@MockBean
	private MstProductOfferingRepository mstProductOfferingRepository;

	@MockBean
	private QuoteDelegationRepository quoteDelegationRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	
	@MockBean
	private QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	IzosdwanIllPricingAndFeasiblityService izosdwanIllPricingAndFeasiblityService;
	
	@MockBean
	IzosdwanGvpnPricingAndFeasibilityService izosdwanGvpnPricingAndFeasibilityService;
	
	@MockBean
	CofDetailsRepository cofDetailsRepository;
	
	@MockBean
	DocusignAuditRepository docusignAuditRepository;
	
	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@MockBean
	QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository;
	
	@MockBean
	QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository;
	
	@MockBean 
	QuoteIzosdwanCgwDetailRepository quoteIzosdwanCgwDetailRepository;
	
	@Test
	public void testcreateQuote() throws Exception {
		Mockito.when(izosdwanservice.createQuote(objectCreator.getQuoteDetails(), 1)).thenReturn(new QuoteResponse());
		ResponseResource<QuoteResponse> response = izosdwanQuoteController.createQuote(objectCreator.getQuoteDetails(),
				1);
		assertTrue(response != null);
		System.out.println(response.getStatus());
	}
	

	/**
	 * test create quote Negative test case
	 **/
	@Test
	public void testcreateQuoteInvalidCustomer() throws Exception {
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(izosdwanservice.createQuote(objectCreator.getQuoteDetails(), 1)).thenReturn(new QuoteResponse());
		ResponseResource<QuoteResponse> response = izosdwanQuoteController.createQuote(objectCreator.getQuoteDetails(),
				1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case without passing QuoteleId and QuoteId
	 **/
	@Test
	public void testcreateQuote2() throws Exception {
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getCustomerDetailsBean());
		ResponseResource<QuoteResponse> response = izosdwanQuoteController.createQuote(objectCreator.getQuoteDet(),
				1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	public void testcreateQuoteWithValid() throws Exception {

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());
		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getCustomer());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuote()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteToLe());
		ResponseResource<QuoteResponse> response = izosdwanQuoteController.createQuote(objectCreator.getQuoteDetails(),
				1);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case
	 **/
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

		ResponseResource<QuoteResponse> response = izosdwanQuoteController.createQuote(objectCreator.getQuoteDetails(),
				null);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testgetQuoteConfiguration-get quote configuration postive
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetQuoteConfiguration() throws Exception {
		Mockito.when(quoteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.getQuote2()));

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(Mockito.any(), Mockito.any(Byte.class)))
				.thenReturn(objectCreator.getListOfQouteIllSites());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(objectCreator.getSiteFeasibilities1());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamilyList());

		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getSolutionList());

		ResponseResource<QuoteBean> response = izosdwanQuoteController.getQuoteConfiguration(1, "ALL", false,1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}
	
	@Test
	public void shareQuote() throws TclCommonException {
		Mockito.when(izosdwanservice.persistSolutionDetails(objectCreator.getQuoteDetails())).thenReturn(new QuoteResponse());
		ResponseResource<QuoteResponse> response = izosdwanQuoteController.shareQuote(25086,25034,objectCreator.getQuoteDetails());
		assertTrue(response != null);
	}

	@Test
	public void getCpeDetails() throws TclCommonException {
	Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString())).thenReturn(sdwanObjectCreator.getSdwanDetails());
	ResponseResource<List<IzoSdwanCpeDetails>> response=izosdwanQuoteController.getCpeDetails("IZO_SDWAN_SELECT", "premium", "Basic");
	assertTrue(response!=null);
	}
	
	
	@Test 
	public void getCpeDetailsNull() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString())).thenReturn(new IzoSdwanQuoteController());
		ResponseResource<List<IzoSdwanCpeDetails>> response=izosdwanQuoteController.getCpeDetails("IZO_SDWAN_SELECT", " ", "Enhanced");
		assertTrue(response!=null);
	}
	
	@Test
	public void getCpeConfigurationDeatils() throws TclCommonException {
		Mockito.when(izosdwanservice.getConfigurationCpeDetails(12222, "Single GVPN Single IAS")).thenReturn(new ConfigurationSummaryBean());
		ResponseResource<ConfigurationSummaryBean> responseResource=izosdwanQuoteController.getCpeConfigDetails(1222, "Single GVPN Single IAS");
		assertTrue(responseResource!=null);
	}
	
	@Test
	public void getSiteDetails() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(sdwanObjectCreator.getSiteDetails());
		Mockito.when(izosdwanservice.getBandwidthDetails(12762)).thenReturn(new NetworkSummaryDetails());
		ResponseResource<List<IzoSdwanSiteDetails>> response=izosdwanQuoteController.getCpeSiteDetails();
		assertTrue(response!=null);
		
	}
	
	@Test
	public void getCpeSiteDetails() throws TclCommonException {
		//Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(sdwanObjectCreator.getSiteDetails());
		Mockito.when(izosdwanservice.getBandwidthDetails(12762)).thenReturn(new NetworkSummaryDetails());
		ResponseResource<NetworkSummaryDetails> response=izosdwanQuoteController.getCpeSiteDetails(12222);
		assertTrue(response!=null);
	}
	
	/**
	 * postive test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void getSiteTypeDetails() throws TclCommonException {
		Mockito.when(izosdwanservice.getSiteTypeDetails(763666)).thenReturn(sdwanObjectCreator.getSiteTypeDetails());
		ResponseResource<List<SiteTypeDetails>> response = izosdwanQuoteController.getSitetypeDetails(76767);
		assertTrue(response != null);
	}
	
	/**
	 * Negative test case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void getSiteTypeDetailsNull() throws TclCommonException {
		Mockito.when(izosdwanservice.getSiteTypeDetails(763666)).thenReturn(sdwanObjectCreator.getSiteTypeDetails());
		ResponseResource<List<SiteTypeDetails>> response = izosdwanQuoteController.getSitetypeDetails(76767);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * Negative Test case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void getCpeConfigurationCpeDeatils() throws TclCommonException {
		Mockito.when(izosdwanservice.getConfigurationCpeDetails(12222, "Single GVPN Single IAS"))
				.thenReturn(new ConfigurationSummaryBean());
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(null);
		ResponseResource<ConfigurationSummaryBean> response = izosdwanQuoteController.getCpeConfigDetails(1222,
				"Single GVPN Single IAS");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
		
	/**
	 * Positive Test case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void getSitesBasedOnSiteType() throws TclCommonException {
		Mockito.when(izosdwanservice.getSitesBasedOnSiteType(6777, "Single IAS Single CPE",
				sdwanObjectCreator.getInputDetails())).thenReturn(sdwanObjectCreator.getViewSiteDetails());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(sdwanObjectCreator.getLocIds());
		ResponseResource<List<ViewSitesSummaryBean>> response = izosdwanQuoteController.getViewSitesSummaryDetails(7687,
				"Single IAS Single CPE", sdwanObjectCreator.getInputDetails());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * Negative Test case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void getSitesBasedOnSit() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(null);
		Mockito.when(izosdwanservice.getSitesBasedOnSiteType(6777, "Single IAS Single CPE",
				sdwanObjectCreator.getInputDetails())).thenReturn(sdwanObjectCreator.getViewSiteDetails());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(sdwanObjectCreator.getLocIds());
		ResponseResource<List<ViewSitesSummaryBean>> response = izosdwanQuoteController.getViewSitesSummaryDetails(7687,
				"Single IAS Single CPE", sdwanObjectCreator.getInputDetails());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * Positive Test Case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void updateSitePropertiesAttribute() throws TclCommonException {
		Mockito.when(izosdwanservice.updateSitePropertiesAttributes(objectCreator.getUpdateRequestList()))
				.thenReturn(objectCreator.getQuoteDetail());
		Mockito.when(quoteIzosdwanSiteRepository.findByIdAndStatus(77878, (byte) 1))
				.thenReturn(new QuoteIzosdwanSite());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus("dhdh", (byte) 1))
				.thenReturn(objectCreator.getMstProductFamily());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),
				Mockito.matches("CPE"))).thenReturn(objectCreator.getQuoteProductComponent());

		ResponseResource<QuoteDetail> response = izosdwanQuoteController.updateSitePropertiesAttributes(62632, 77322,
				objectCreator.getUpdateRequestList());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * Negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void updateSitePropertiesAttributes() throws TclCommonException {
		Mockito.when(izosdwanservice.updateSitePropertiesAttributes(objectCreator.getUpdateRequestList()))
				.thenReturn(objectCreator.getQuoteDetail());
		Mockito.when(quoteIzosdwanSiteRepository.findByIdAndStatus(null, (byte) 1)).thenReturn(new QuoteIzosdwanSite());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus("dhdh", (byte) 1))
				.thenReturn(objectCreator.getMstProductFamily());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),
				Mockito.matches("CPE"))).thenReturn(objectCreator.getQuoteProductComponent());

		ResponseResource<QuoteDetail> response = izosdwanQuoteController.updateSitePropertiesAttributes(62632, 77322,
				objectCreator.getUpdateRequestList());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void triggerForFeasibilityBean() throws TclCommonException {
		doNothing().when(izosdwanIllPricingAndFeasiblityService).processFeasibility(Mockito.anyInt());
		doNothing().when(izosdwanGvpnPricingAndFeasibilityService).processFeasibility(Mockito.anyInt(),
				Mockito.anyString());
		ResponseResource<String> response = izosdwanQuoteController
				.triggerForFeasibilityBean(objectCreator.getFeasibilityBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	
	@Test
	public void getQuoteConfigurationBySites() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(objectCreator.getQuote());
		Mockito.when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.anyString()))
				.thenReturn(new OrderConfirmationAudit());
		Mockito.when(cofDetailsRepository.findByOrderUuidAndSource(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(new CofDetails());
		Mockito.when(docusignAuditRepository.findByOrderRefUuid(Mockito.anyString())).thenReturn(new DocusignAudit());
		Mockito.when(izosdwanservice.getQuoteDetails(636762, "test", true, 76787, sdwanObjectCreator.getLocIds()))
				.thenReturn(objectCreator.getQuoteBean());
		ResponseResource<QuoteBean> response = izosdwanQuoteController.getQuoteConfigurationBySites(6636, "text", true,
				sdwanObjectCreator.getLocIds());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void feasibilityCheck() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(78787)).thenReturn(objectCreator.returnQuoteToLeForUpdateStatus());
		Mockito.when(izosdwanservice.checkQuoteLeFeasibility(76778)).thenReturn(sdwanObjectCreator.getQuoteLe());
		ResponseResource<QuoteLeAttributeBean> response = izosdwanQuoteController.feasibilityCheck(767676);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void getAllAttributesByQuoteToLeId() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(7765)).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(objectCreator.getQuoteToLe()))
				.thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(izosdwanservice.getAllAttributesByQuoteToLeId(76652))
				.thenReturn(objectCreator.getLegalAttributeBeanList());
		ResponseResource<Set<LegalAttributeBean>> response = izosdwanQuoteController
				.getAllAttributesByQuoteToLeId(12342, 45333);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}
	
	@Test
	public void persistListOfQuoteLeAttributes() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive("test", (byte) 1))
				.thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(izosdwanservice.persistListOfQuoteLeAttributes(objectCreator.getUpdateRequest()))
				.thenReturn(objectCreator.getQuoteDetail());
		ResponseResource<QuoteDetail> response = izosdwanQuoteController.persistListOfQuoteLeAttributes(6767, 87654,
				objectCreator.getUpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void createDocument () throws TclCommonException {
		ResponseResource<CreateDocumentDto> response=izosdwanQuoteController.createDocument(5622, 76765, objectCreator.getDocumentDto());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
		
	@Test
	public void getPriceSiteWise() throws TclCommonException{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.findByIdInAndStatus(Mockito.anyList(),Mockito.anyByte())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolution(objectCreator.getSolution())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(izosdwanservice.getPriceSiteWise(sdwanObjectCreator.getPriceRequestBean())).thenReturn(sdwanObjectCreator.getSolutionPricingDet());
		ResponseResource<SolutionPricingDetailsBean> response =izosdwanQuoteController.getPricingInfoSitewise(123, 333, sdwanObjectCreator.getPriceRequestBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	
	@Test
	public void getPriceSiteWiseNegative() throws TclCommonException{
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(null);
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(Mockito.anyInt())).thenReturn(null);
		Mockito.when(quoteIzosdwanSiteRepository.findByIdInAndStatus(Mockito.anyList(),Mockito.anyByte())).thenReturn(null);
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolution(objectCreator.getSolution())).thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(izosdwanservice.getPriceSiteWise(sdwanObjectCreator.getPriceRequestBean())).thenReturn(sdwanObjectCreator.getSolutionPricingDet());
		ResponseResource<SolutionPricingDetailsBean> response =izosdwanQuoteController.getPricingInfoSitewise(123, 333, sdwanObjectCreator.getPriceRequestBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void updateCurrency() throws TclCommonException{
		Mockito.when(quoteToLeRepository.findById(12133)).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteToLeRepository.findByQuote(objectCreator.getQuote())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByReferenceId("123")).thenReturn(objectCreator.getQuotePrice());
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteIllSite());
		Mockito.when(quotePriceRepository.findByQuoteId(123)).thenReturn(objectCreator.getQuotePriceList());
		Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(sdwanObjectCreator.getQuotePrice());
		Mockito.when(quoteToLeRepository.saveAndFlush(sdwanObjectCreator.getQuoteToLe())).thenReturn(objectCreator.getQuoteToLe());
		ResponseResource<String> response=izosdwanQuoteController.updateCurrency(122, 333, "abc");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	
	
	
	@Test
    public void updateTermAndMonths() throws TclCommonException {
        Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getquoteDetail());
        Mockito.when(quoteToLeRepository.findByQuoteAndId(objectCreator.getQuote(), 72733)).thenReturn(objectCreator.getQuoteToLe());
        doNothing().when(izosdwanservice).updateTermsInMonthsForQuoteToLe(63622, 77262, sdwanObjectCreator.getUpdateRequest(), 122);
        ResponseResource<String> response=izosdwanQuoteController.updateTermAndMonths(73672, 122871, sdwanObjectCreator.getUpdateRequest());
        assertTrue(response != null && response.getStatus() == Status.SUCCESS);
    }

	@Test
    public void updateTermAndMonthsNegative() throws TclCommonException {
        Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getquoteDetail());
        Mockito.when(quoteToLeRepository.findByQuoteAndId(objectCreator.getQuote(), 72733)).thenReturn(objectCreator.getQuoteToLe());
        doNothing().when(izosdwanservice).updateTermsInMonthsForQuoteToLe(63622, 77262, null, 122);
        ResponseResource<String> response=izosdwanQuoteController.updateTermAndMonths(73672, 122871, sdwanObjectCreator.getUpdateRequest());
        assertTrue(response != null && response.getStatus() == Status.SUCCESS);
    }
	
	
	@Test
	public void generateQuotePdf() throws TclCommonException {
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.when(quoteRepository.findByIdAndStatus(32468, (byte) 1)).thenReturn(objectCreator.getQuote());
		Mockito.when(izosdwanservice.processQuotePdf(32468, response, 32765)).thenReturn("htttp://gash d ");
		ResponseResource<String> response1 = izosdwanQuoteController.generateQuotePdf(32468, 32765, response);
		assertTrue(response1 != null && response1.getStatus() == Status.SUCCESS);
	}

	/**
	 * Negative Test case
	 * 
	 * @throws TclCommonException
	 */
	@SuppressWarnings("null")
	@Test
	public void generateQuotePdfNegative() throws TclCommonException {
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.when(quoteRepository.findByIdAndStatus(32468, (byte) 1)).thenReturn(null);
		Mockito.when(izosdwanservice.processQuotePdf(63632468, response, 777732765)).thenReturn(null);
		ResponseResource<String> response1 = izosdwanQuoteController.generateQuotePdf(63632468, 777732765, response);
		assertTrue(response1 != null && response1.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void getDistinctCpeDetails() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(32468, (byte) 1)).thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(6543456))
				.thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getDistinctCpeForSdwan(76665)).thenReturn(sdwanObjectCreator.getCpe());
		Mockito.when(izosdwanservice.getDistinctCpes(325468)).thenReturn(sdwanObjectCreator.getCpe());
		ResponseResource<List<String>> response = izosdwanQuoteController.getDistinctCpeDetails(32468);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void getDistinctCpeDetailsNegative() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(324686361, (byte) 0)).thenReturn(null);
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(6543456))
				.thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getDistinctCpeForSdwan(76665)).thenReturn(sdwanObjectCreator.getCpe());
		Mockito.when(izosdwanservice.getDistinctCpes(325468)).thenReturn(sdwanObjectCreator.getCpe());
		ResponseResource<List<String>> response = izosdwanQuoteController.getDistinctCpeDetails(32468);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void getVproxyProfileDetails() throws TclCommonException {
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
		.thenReturn(sdwanObjectCreator.getVproxySolutionDet());
		Mockito.when(izosdwanservice.getVproxyProfileDetails(32678)).thenReturn(sdwanObjectCreator.getVproxySolutionDet());
		ResponseResource<List<VproxySolutionsBean>> response=izosdwanQuoteController.getVproxyProfileDetails(32678);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	//Negative test case
	
	@Test
	public void getVproxyProfileDetailsNegative() throws TclCommonException {
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(null)).thenReturn(null);
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
		.thenReturn(sdwanObjectCreator.getVproxySolutionDet());
		Mockito.when(izosdwanservice.getVproxyProfileDetails(32678)).thenReturn(sdwanObjectCreator.getVproxySolutionDet());
		ResponseResource<List<VproxySolutionsBean>> response=izosdwanQuoteController.getVproxyProfileDetails(32678);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void addVproxySolutions() throws TclCommonException {
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus("Vproxy", (byte)1)).thenReturn(objectCreator.getMstProductFamily());
		Mockito.when(quoteToLeRepository.findById(32679)).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteToLeProductFamilyRepository.saveAndFlush(sdwanObjectCreator.getLeProductFamily())).thenReturn(sdwanObjectCreator.getLeProductFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(sdwanObjectCreator.getLeProductFamily())).thenReturn(objectCreator.getProductSolutionList());
		doNothing().when(productSolutionRepository).deleteAll();
		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(objectCreator.geProductFamily(), "Vproxy", CommonConstants.BACTIVE)).thenReturn(sdwanObjectCreator.getMstOffering());
		Mockito.when(izosdwanservice.addVproxySolutions(sdwanObjectCreator.getVproxySolutionDetails())).thenReturn(ResponseResource.RES_SUCCESS);
		ResponseResource<String > response=izosdwanQuoteController.addVproxySolutions(sdwanObjectCreator.getVproxySolutionDetails());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/*
	 * Negative test case
	 */
	
	@Test
	public void NegativeaddVproxySolutions() throws TclCommonException {
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus("Vproxy", (byte)1)).thenReturn(objectCreator.getMstProductFamily());
		Mockito.when(quoteToLeRepository.findById(null)).thenReturn(null);
		Mockito.when(quoteToLeProductFamilyRepository.saveAndFlush(sdwanObjectCreator.getLeProductFamily())).thenReturn(sdwanObjectCreator.getLeProductFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(sdwanObjectCreator.getLeProductFamily())).thenReturn(objectCreator.getProductSolutionList());
		doNothing().when(productSolutionRepository).deleteAll();
		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(objectCreator.geProductFamily(), "Vproxy", CommonConstants.BACTIVE)).thenReturn(sdwanObjectCreator.getMstOffering());
		Mockito.when(izosdwanservice.addVproxySolutions(sdwanObjectCreator.getVproxySolutionDetailsNegatiive())).thenThrow(new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST));
		ResponseResource<String > response=izosdwanQuoteController.addVproxySolutions(sdwanObjectCreator.getVproxySolutionDetailsNegatiive());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void updateLegalEntityProperties() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus("Trent", 1)).thenReturn(sdwanObjectCreator.getUser());
		Mockito.when(quoteToLeRepository.findById(654422)).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive("trent",(byte)1)).thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(objectCreator.getQuoteToLe(), "SPA")).thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteLeAttributeValueRepository.save(sdwanObjectCreator.getLeValues())).thenReturn(sdwanObjectCreator.getLeValues());
		Mockito.when(izosdwanservice.updateLegalEntityProperties(sdwanObjectCreator.getUpdateRequestDetails())).thenReturn(sdwanObjectCreator.getQuoteDetail());
		ResponseResource<QuoteDetail> response=izosdwanQuoteController.updateLegalEntityProperties(32679, 23444, sdwanObjectCreator.getUpdateRequestDetails());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		
	}
	
	
	@Test
	public void negativeUpdateLegalEntityProperties() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus("Trent", 1)).thenReturn(sdwanObjectCreator.getUser());
		Mockito.when(quoteToLeRepository.findById(654422)).thenReturn(objectCreator.getOptionalQuoteToLe());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive("trent",(byte)1)).thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(objectCreator.getQuoteToLe(), "SPA")).thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteLeAttributeValueRepository.save(sdwanObjectCreator.getLeValues())).thenReturn(sdwanObjectCreator.getLeValues());
		Mockito.when(izosdwanservice.updateLegalEntityProperties(null)).thenThrow(new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND));
		ResponseResource<QuoteDetail> response=izosdwanQuoteController.updateLegalEntityProperties(32679, 23444, sdwanObjectCreator.getUpdateRequestDetails());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		
	}
	
	@Test
	public void addOrModifyQuoteAttribute() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(23444, CommonConstants.BACTIVE)).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.saveAll(sdwanObjectCreator.getIzoSdwanVal())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(izosdwanservice.addOrModifyQuoteAttribute(sdwanObjectCreator.getSdwanUpdateBean())).thenReturn(ResponseResource.RES_SUCCESS);
		ResponseResource<String> response=izosdwanQuoteController.addOrModifyQuoteAttributes(sdwanObjectCreator.getSdwanUpdateBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/*
	 * Negative Test Case
	 */

	@Test
	public void addOrModifyQuoteAttributeNegative() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(null, (byte)0)).thenReturn(null);
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.saveAll(sdwanObjectCreator.getIzoSdwanVal())).thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(izosdwanservice.addOrModifyQuoteAttribute(sdwanObjectCreator.getSdwanUpdateBeanNegative())).thenThrow(new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST));
		ResponseResource<String> response=izosdwanQuoteController.addOrModifyQuoteAttributes(sdwanObjectCreator.getSdwanUpdateBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	

	@Test
	public void updateCurrencyNegativeCase() throws TclCommonException{
		Mockito.when(quoteToLeRepository.findById(12133)).thenReturn(null);
		Mockito.when(quoteToLeRepository.findByQuote(objectCreator.getQuote())).thenReturn(null);
		Mockito.when(quotePriceRepository.findByReferenceId("123")).thenReturn(null);
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteIllSite());
		Mockito.when(quotePriceRepository.findByQuoteId(123)).thenReturn(objectCreator.getQuotePriceList());
		Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(sdwanObjectCreator.getQuotePrice());
		Mockito.when(quoteToLeRepository.saveAndFlush(sdwanObjectCreator.getQuoteToLe())).thenReturn(null);
		ResponseResource<String> response=izosdwanQuoteController.updateCurrency(122, 333, "abc");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void profileSelectionDetails() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findByQuote_Id(77878)).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
		.thenReturn(2);
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
		.thenReturn(sdwanObjectCreator.getOfferingList());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.save(Mockito.any())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository.selectSiteTypeByQuote(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		Mockito.when(izosdwanservice.getProfileSelectionDetails(sdwanObjectCreator.getProfileSelectionInputDetails())).thenReturn(sdwanObjectCreator.getvendorProfileDetailsList());
		ResponseResource<List<VendorProfileDetailsBean>> response = izosdwanQuoteController.profileSelectionDetails(sdwanObjectCreator.getProfileSelectionInputDetails());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void profileSelectionDetailsNegative() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findByQuote_Id(77878)).thenReturn(null);
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
		.thenReturn(2);
		Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
		.thenReturn(sdwanObjectCreator.getOfferingList());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(null);
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.save(Mockito.any())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository.selectSiteTypeByQuote(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		Mockito.when(izosdwanservice.getProfileSelectionDetails(sdwanObjectCreator.getProfileSelectionInputDetails())).thenReturn(sdwanObjectCreator.getvendorProfileDetailsList());
		ResponseResource<List<VendorProfileDetailsBean>> response = izosdwanQuoteController.profileSelectionDetails(sdwanObjectCreator.getProfileSelectionInputDetails());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	
	@Test
	public void uploadByonSitesAgainstTheQuote() throws TclCommonException{
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(objectCreator.getQuote());
		 Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(6543456)).thenReturn(objectCreator.getSolution());
		 Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
			.thenReturn(objectCreator.getUser());
		 Mockito.when(productAttributeMasterRepository.findAll()).thenReturn(objectCreator.getProductAtrributeMaster());
		 Mockito.when(quoteIzosdwanByonUploadDetailRepository.selectSiteTypeByQuote(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		 Mockito.when(quoteIzosdwanSiteRepository.findByIdAndStatus(77878, (byte) 1))
			.thenReturn(new QuoteIzosdwanSite());
		 Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
			.thenReturn(sdwanObjectCreator).thenReturn(sdwanObjectCreator.getcpeBomInterfaces());
		 Mockito.when(izosdwanservice.uploadByonSitesAgainstTheQuote(Mockito.anyInt()));
			ResponseResource<String> response = izosdwanQuoteController.persistByonDetailsInSites(Mockito.anyInt(),Mockito.anyInt());
			assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		 
	}
	@Test
	public void uploadByonSitesAgainstTheQuoteNegative() throws TclCommonException{
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(null);
		 Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(6543456)).thenReturn(null);
		 Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
			.thenReturn(objectCreator.getUser());
		 Mockito.when(productAttributeMasterRepository.findAll()).thenReturn(null);
		 Mockito.when(quoteIzosdwanByonUploadDetailRepository.selectSiteTypeByQuote(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		 Mockito.when(quoteIzosdwanSiteRepository.findByIdAndStatus(77878, (byte) 1))
			.thenReturn(new QuoteIzosdwanSite());
		 Mockito.when(mqUtils.sendAndReceive( Mockito.anyString(), Mockito.anyString()))
			.thenReturn(sdwanObjectCreator).thenReturn(sdwanObjectCreator.getcpeBomInterfaces());
		 Mockito.when(izosdwanservice.uploadByonSitesAgainstTheQuote(645434));
			ResponseResource<String> response = izosdwanQuoteController.persistByonDetailsInSites(45556,76654);
			assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		 
	}
	
	
//	@Test 
//	public void getDefaultBandwidthForCgwNegative() throws TclCommonException{
//		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(null);
//		 Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(6543456)).thenReturn(null);
//		 Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
//		 Mockito.when(quoteIzoSdwanAttributeValuesRepository.save(Mockito.any())).thenReturn(null);
//		 Mockito.when(quoteIzosdwanCgwDetailRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getCgwDetail());
//		 Mockito.when(quoteIzosdwanSiteRepository.getDefaultPortBandwidth(Mockito.anyInt())).thenReturn("299");
//		 Mockito.when(quoteIzosdwanSiteRepository.getDistinctSiteTypesForSdwan(Mockito.anyInt())).thenReturn(null);
//		 Mockito.when(quoteIzoSdwanAttributeValuesRepository.saveAll(sdwanObjectCreator.getSdwanAttrValues())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
//		 Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstProductComponent());
//		 Mockito.when(quoteProductComponentRepository.saveAndFlush(sdwanObjectCreator.getProductComponent())).thenReturn(sdwanObjectCreator.getProductComponent());
//		 Mockito.when(quotePriceRepository.saveAndFlush(objectCreator.getQuotePrice())).thenReturn(null);
//		 Mockito.when(izosdwanservice.getDefaultBandwidthForCgw(1234, sdwanObjectCreator.getInputIzosdwanSdwanAttributeValues()));
//		 ResponseResource<String> response = izosdwanQuoteController.getDefaultBandwidthForCgw(Mockito.anyInt(),sdwanObjectCreator.getInputIzosdwanSdwanAttributeValues());
//		 assertTrue(response != null && response.getStatus() == Status.FAILURE);
//	}
	
	@Test
	public void getAllAttributesByQuoteId() throws TclCommonException{
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(objectCreator.getQuote());
		 Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		 Mockito.when(izosdwanservice.getAllAttributesByQuoteId(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getIzosdwanQuoteAttributesBean());
		 ResponseResource<List<IzosdwanQuoteAttributesBean>> response = izosdwanQuoteController.getQuoteAttributes(Mockito.anyInt());
		 assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void getAllAttributesByQuoteIdNegative() throws TclCommonException{
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(null);
		 Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(null);
		 Mockito.when(izosdwanservice.getAllAttributesByQuoteId(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getIzosdwanQuoteAttributesBean());
		 ResponseResource<List<IzosdwanQuoteAttributesBean>> response = izosdwanQuoteController.getQuoteAttributes(76665);
		 assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void getByonLocationValidationStatus() throws TclCommonException{
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(objectCreator.getQuote());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository.findByStatusInAndQuote_id(sdwanObjectCreator.getStatusList(),96545)).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository.findByQuote_idAndLocationErrorDetailsIsNotNull(Mockito.anyInt())).thenReturn(sdwanObjectCreator.getByonUploadDetails());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote())).thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		Mockito.when(izosdwanservice.getByonLocationValidationStatus(Mockito.anyInt())).thenReturn(true);
		ResponseResource<Boolean> response = izosdwanQuoteController.getLocationValidationStatusForQuote(Mockito.anyInt());
		 assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void test() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(32468, (byte) 1)).thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(6543456))
				.thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getUniqueLocationIds(6724425))
				.thenReturn(sdwanObjectCreator.getUniqueLocationIds());
		Mockito.when(quoteIzosdwanSiteRepository.findByErfLocSitebLocationIdAndProductSolution(
				sdwanObjectCreator.getUniqueLocationIds().get(0), objectCreator.getSolution()))
				.thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn("values");
		Mockito.when(izosdwanservice.test(32468)).thenReturn(sdwanObjectCreator.getIzosdwanQuotePdfBean());
		ResponseResource<IzosdwanQuotePdfBean> response = izosdwanQuoteController.test(32468, 324654);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}
	
	@Test
	public void testNegative() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(626327723, (byte) 1)).thenReturn(null);
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(6543456))
				.thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.getUniqueLocationIds(6724425))
				.thenReturn(sdwanObjectCreator.getUniqueLocationIds());
		Mockito.when(quoteIzosdwanSiteRepository.findByErfLocSitebLocationIdAndProductSolution(
				sdwanObjectCreator.getUniqueLocationIds().get(0), objectCreator.getSolution()))
				.thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn("values");
		Mockito.when(izosdwanservice.test(626327723)).thenReturn(sdwanObjectCreator.getIzosdwanQuotePdfBean());
		ResponseResource<IzosdwanQuotePdfBean> response = izosdwanQuoteController.test(32468, 324654);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}
	
	@Test
	public void getPricingInfo() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(23456, CommonConstants.BACTIVE))
				.thenReturn(objectCreator.getQuote());
		Mockito.when(quoteToLeRepository.findByQuote_Id(23456)).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(876562))
				.thenReturn(sdwanObjectCreator.getSolution());
		Mockito.when(quoteIzosdwanSiteRepository.findByProductSolution(objectCreator.getSolution()))
				.thenReturn(sdwanObjectCreator.getIzoSdwanSites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(12344,
				IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),
				QuoteConstants.IZOSDWAN_SITES.toString())).thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(12332,
				QuoteConstants.IZOSDWAN_SITES.toString())).thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(12332,
				QuoteConstants.IZOSDWAN_SITES.toString())).thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(izosdwanservice.getPriceInformationForTheQuote(23444))
				.thenReturn(sdwanObjectCreator.getPricingDetailBean());
		ResponseResource<QuotePricingDetailsBean> response = izosdwanQuoteController.getPricingInfo(23444, 45678);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	
	@Test
	public void processMailAttachment() throws TclCommonException {
		// doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		// Mockito.when(notificationService.processShareQuoteNotification("Chennai",
		// Mockito.anyString(), "trent", "Quote_\" + 12333 + \".pdf",
		// "IZOSDWAN")).thenReturn(Mockito.anyBoolean());
		Mockito.when(izosdwanservice.processMailAttachment("trent@legomail.com", 12345))
				.thenReturn(sdwanObjectCreator.getServiceResponse());
		ResponseResource<ServiceResponse> response = izosdwanQuoteController.shareQuote(324567, 45678,
				"trent@legomail.com");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void uploadIzoSdwanByonTemplate()
			throws EncryptedDocumentException, InvalidFormatException, TclCommonException, IOException {
		MockMultipartFile multipartFile = new MockMultipartFile("optimus", "optimusFile", "application/json",
				"optimus".getBytes());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote()))
				.thenReturn(sdwanObjectCreator.getIzoSdwanVal());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository.findByQuote(objectCreator.getQuote()))
				.thenReturn(sdwanObjectCreator.getByonUploadDetails());
		doNothing().when(quoteIzosdwanByonUploadDetailRepository)
				.deleteInBatch(sdwanObjectCreator.getByonUploadDetails());
		Mockito.when(izosdwanservice.uploadIzosdwanByonExcel(multipartFile, 1222))
				.thenReturn(sdwanObjectCreator.getByonUploadResponseDet());
		ResponseResource<ByonUploadResponse> response = izosdwanQuoteController
				.uploadIzoSdwanByonTemplate(multipartFile, 12222);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void downloadIzoSdwanByonTemplate() throws TclCommonException, IOException {
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

		Mockito.when(quoteIzosdwanByonUploadDetailRepository.findByQuote(objectCreator.getQuote()))
				.thenReturn(sdwanObjectCreator.getByonUploadDetails());
		doNothing().when(izosdwanservice).downloadIzoSdwanByonTemplate(sdwanObjectCreator.getLocationDetails(),
				response, true, 12222);
		ResponseResource<String> response1 = izosdwanQuoteController
				.downloadIzoSdwanByonTemplate(sdwanObjectCreator.getLocationDetails(), 12233, true, response);
		assertTrue(response1 != null && response1.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void checkUploadDoneOrNot() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(123344, CommonConstants.BACTIVE))
				.thenReturn(sdwanObjectCreator.getQuote());
		Mockito.when(quoteIzosdwanByonUploadDetailRepository.findByQuote_id(123456))
				.thenReturn(sdwanObjectCreator.getByonUploadDetails());
		Mockito.when(izosdwanservice.checkByonDetailsUploadedOrNot(12345)).thenReturn(true);
		ResponseResource<Boolean> response = izosdwanQuoteController.checkUploadDoneOrNot(12345);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void getSolutionLevelChargesForIzosdwan() throws TclCommonException {
		Mockito.when(quoteIzosdwanCgwDetailRepository.findByQuote_Id(123456))
				.thenReturn(sdwanObjectCreator.getCgwDetail());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(234,
				IzosdwanCommonConstants.IZOSDWAN_CGW)).thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quotePriceRepository
				.findByReferenceNameAndReferenceIdAndQuoteId(IzosdwanCommonConstants.COMPONENTS, "trent", 1222))
				.thenReturn(objectCreator.geQuotePrice());
		Mockito.when(izosdwanservice.getSolutionLevelChargesForIzosdwan(3452))
				.thenReturn(sdwanObjectCreator.getSolutionLevelCharges());
		ResponseResource<List<IzosdwanSolutionLevelCharges>> response = izosdwanQuoteController
				.getSolutionLevelChargesForIzosdwan(123456);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void getSolutionLevelBreakUpChargesForCGW() throws TclCommonException {
		Mockito.when(izosdwanservice.getSolutionLevelBreakUpPricingDetailForCGW(null)).thenThrow(
				new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST));
		ResponseResource<List<SolutionLevelPricingBreakupDetailsBean>> response = izosdwanQuoteController
				.getSolutionLevelBreakUpChargesForCGW(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void getDefaultBandwidthForCgwBand() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(77878, (byte) 1)).thenReturn(objectCreator.getQuote());
		Mockito.when(productSolutionRepository.findByReferenceIdForIzoSdwan(6543456))
				.thenReturn(objectCreator.getSolution());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.findByQuote(objectCreator.getQuote()))
				.thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.save(Mockito.any()))
				.thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		Mockito.when(quoteIzosdwanCgwDetailRepository.findByQuote(objectCreator.getQuote()))
				.thenReturn(sdwanObjectCreator.getCgwDetail());
		Mockito.when(quoteIzosdwanSiteRepository.getDefaultPortBandwidth(Mockito.anyInt())).thenReturn("299");
		Mockito.when(quoteIzosdwanSiteRepository.getDistinctSiteTypesForSdwan(Mockito.anyInt()))
				.thenReturn(sdwanObjectCreator.getSiteTypes());
		Mockito.when(quoteIzoSdwanAttributeValuesRepository.saveAll(sdwanObjectCreator.getSdwanAttrValues()))
				.thenReturn(sdwanObjectCreator.getSdwanAttrValues());
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstProductComponent());
		Mockito.when(quoteProductComponentRepository.saveAndFlush(sdwanObjectCreator.getProductComponent()))
				.thenReturn(sdwanObjectCreator.getProductComponent());
		Mockito.when(quotePriceRepository.saveAndFlush(objectCreator.getQuotePrice()))
				.thenReturn(objectCreator.getQuotePrice());
		Mockito.when(izosdwanservice.getDefaultBandwidthForCgw(1234,
				sdwanObjectCreator.getInputIzosdwanSdwanAttributeValues()));
		ResponseResource<String> response=izosdwanQuoteController.getDefaultBandwidthForCgw(12334,sdwanObjectCreator.getAttributeValuesSdwan());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		
	}
}





