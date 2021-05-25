package com.tcl.dias.oms.teamsdr.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.teamsdr.beans.TeamsDRLicenseAgreementType;
import com.tcl.dias.common.teamsdr.beans.TeamsDRServiceQuoteBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.OptimusOmsApplication;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDirectRoutingCityRepository;
import com.tcl.dias.oms.entity.repository.QuoteDirectRoutingMgRepository;
import com.tcl.dias.oms.entity.repository.QuoteDirectRoutingRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTeamsDRRepository;
import com.tcl.dias.oms.entity.repository.QuoteTeamsLicenseRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEOrderService;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRLicenseComponentsBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiQuoteLeBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRQuoteDataBean;
import com.tcl.dias.oms.teamsdr.controller.v1.TeamsDRQuoteController;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.dias.oms.teamsdr.util.TeamsDRUtils;
import com.tcl.dias.oms.utils.TeamsDRObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.junit.Assert;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * This file contains the testing scenarios of TeamsDR Product.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OptimusOmsApplication.class})
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TeamsDRQuoteControllerTest {

	@Autowired
	TeamsDRQuoteController teamsDRQuoteController;

	@Autowired
	TeamsDRObjectCreator teamsDRObjectCreator = new TeamsDRObjectCreator();

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	ProductSolutionRepository productSolutionRepository;

	@MockBean
	QuoteDirectRoutingRepository quoteDirectRoutingRepository;

	@MockBean
	UserRepository userRepository;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	MstProductOfferingRepository mstProductOfferingRepository;

	@MockBean
	QuoteTeamsDRRepository quoteTeamsDRRepository;

	@MockBean
	QuoteDirectRoutingCityRepository quoteDirectRoutingCityRepository;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	RestClientService restClientService;

	@MockBean
	QuoteTeamsLicenseRepository quoteTeamsLicenseRepository;

	@MockBean
	QuoteDirectRoutingMgRepository quoteDirectRoutingMgRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	OrderRepository orderRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	CofDetailsRepository cofDetailsRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	GscMultiLEOrderService gscMultiLEOrderService;

	/**
	 * Initialize mock repositories
	 */
	@Before
	public void init() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(teamsDRObjectCreator.createQuote());
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt())).thenReturn(teamsDRObjectCreator.getQuoteToLeList());
		Mockito.when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(teamsDRObjectCreator.getProductSolution().stream().findFirst());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(teamsDRObjectCreator.getUser());
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(teamsDRObjectCreator.getCustomer());
		Mockito.when(quoteRepository.save(Mockito.any())).thenReturn(teamsDRObjectCreator.createQuote());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(teamsDRObjectCreator.getQuoteToLe(true));
//		Mockito.when(quoteToLeProductFamilyRepository.save(Mockito.any())).thenReturn(objectCreator
//		.getQuoteToLeProductFamilies());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(teamsDRObjectCreator.getMstProductFamily());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getQuoteToLeProductFamilies(true).stream().findAny().get());
		Mockito.when(quoteToLeRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(teamsDRObjectCreator.getQuoteToLe(true)));
		Mockito.when(quoteDirectRoutingRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.getQuoteDirectRouting()));
		Mockito.when(quoteDirectRoutingCityRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.getCity()));
		Mockito.when(quoteDirectRoutingRepository.save(Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getQuoteDirectRouting());
		Mockito.when(quoteTeamsDRRepository.findByPlan(Mockito.anyInt())).thenReturn(teamsDRObjectCreator.getQuoteTeamsDRs());
		Mockito.when(quoteDirectRoutingCityRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.getCity()));
		Mockito.when(quoteDirectRoutingMgRepository.findByQuoteDirectRoutingCityId(Mockito.anyInt()))
				.thenReturn(teamsDRObjectCreator.getMediaGateways());
		Mockito.when(quoteDirectRoutingRepository.save(Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getQuoteDirectRouting());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(null);
		Mockito.when(quoteTeamsLicenseRepository.findByQuoteTeamsDR(Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getQuoteTeamsLicense());
		Mockito.when(productSolutionRepository.findAllById(Mockito.anyIterable())).thenReturn(teamsDRObjectCreator.getProductSolutions());
		Mockito.when(quoteTeamsDRRepository.save(Mockito.any())).thenReturn(teamsDRObjectCreator.getQuoteTeamsDRs().get(0));
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getQuoteToLeAttributeValues());
//		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(Mockito.any()))
//				.thenReturn(teamsDRObjectCreator.getQuoteProductComponentAttrValues());
	}

	/**
	 * Test to create quote
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testCreateQuote() throws TclCommonException {

		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),
				Mockito.anyString(), Mockito.anyByte())).thenReturn(teamsDRObjectCreator.getCustomPlanOffering());
		TeamsDRQuoteDataBean teamsDRQuoteDataBean = TeamsDRUtils.fromJsonFile(
				"com/tcl/dias/oms/teamsdr/request/createQuote.json", new TypeReference<TeamsDRQuoteDataBean>() {
				});
		ResponseResource response = teamsDRQuoteController.saveQuote(CommonConstants.CREATE, teamsDRQuoteDataBean);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * Test to create quote
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testCreateQuoteManagedPlan() throws TclCommonException {

		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),
				Mockito.anyString(), Mockito.anyByte())).thenReturn(teamsDRObjectCreator.getCustomPlanOffering());
		TeamsDRQuoteDataBean teamsDRQuoteDataBean = TeamsDRUtils.fromJsonFile(
				"com/tcl/dias/oms/teamsdr/request/createQuote_Plan_Only_Managed.json",
				new TypeReference<TeamsDRQuoteDataBean>() {
				});
		ResponseResource response = teamsDRQuoteController.saveQuote(CommonConstants.CREATE, teamsDRQuoteDataBean);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * Test for update quote
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuote() throws TclCommonException {
		Mockito.when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.createProductSolution()));
		Mockito.when(quoteTeamsDRRepository.findByProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(teamsDRObjectCreator.getQuoteTeamsDRs());
		TeamsDRQuoteDataBean teamsDRQuoteDataBean = TeamsDRUtils.fromJsonFile(
				"com/tcl/dias/oms/teamsdr/request/updateQuote.json", new TypeReference<TeamsDRQuoteDataBean>() {
				});
		ResponseResource response = teamsDRQuoteController.updateQuote(teamsDRQuoteDataBean);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);

		// Updating license attributes
		teamsDRQuoteDataBean = TeamsDRUtils.fromJsonFile(
				"com/tcl/dias/oms/teamsdr/request/updateQuote001.json", new TypeReference<TeamsDRQuoteDataBean>() {
				});
		response = teamsDRQuoteController.updateQuote(teamsDRQuoteDataBean);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * Test for get prices for offerings
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricesForOfferings() throws TclCommonException {
		TeamsDRServiceQuoteBean teamsDRServiceQuoteBean = TeamsDRUtils.fromJsonFile(
				"com/tcl/dias/oms/teamsdr/request/getPricesForManagedPlanAndConfigurations",
				new TypeReference<TeamsDRServiceQuoteBean>() {
				});
		ResponseResource response = teamsDRQuoteController.getPricesForOffering(teamsDRServiceQuoteBean);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * Test for getting license countries
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetLicenseByCountries() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(
				"{\"agreementType\": \"Corporate\", \"teamsDRLicenseBasedOnProviders\" : [{\"provider\" : " +
						"\"Microsoft\"}] }");
		ResponseResource<TeamsDRLicenseAgreementType> response = teamsDRQuoteController.getLicenseByCountries(1234,
				4321, "Corporate");
		Assert.assertTrue(response.getData().getTeamsDRLicenseBasedOnProviders().size() > 0);
	}

	/**
	 * Test for saving license details.
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testSaveLicenseDetails() throws TclCommonException {
		TeamsDRLicenseComponentsBean licenseComponentsBean = TeamsDRUtils.fromJsonFile(
				"com/tcl/dias/oms/teamsdr/request/addConfigForLicense.json",
				new TypeReference<TeamsDRLicenseComponentsBean>() {
				});

		Mockito.when(quoteTeamsLicenseRepository.saveAll(Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getQuoteTeamsLicense());

		Mockito.when(quoteTeamsLicenseRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.createQuoteTeamLicense()));
		ResponseResource<TeamsDRLicenseComponentsBean> response = teamsDRQuoteController.saveLicenseDetails(1, 1, 1,
				licenseComponentsBean);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * Test method getQuote
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetQuote() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(teamsDRObjectCreator.getQuoteToLeList());
		Mockito.when(quoteTeamsDRRepository.findByProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any())).thenReturn(teamsDRObjectCreator.getProductSolutions());

		ResponseResource<TeamsDRQuoteDataBean> response = teamsDRQuoteController.getQuote(1234, null, null);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * Test method for delete configurations.
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testDeleteAllConfigurations() throws TclCommonException {
		Mockito.when(quoteTeamsLicenseRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.createQuoteTeamLicense()));
		ResponseResource<String> response = teamsDRQuoteController.deleteConfigurations(1, 1);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);

		response = teamsDRQuoteController.deleteConfigurations(1, 1);
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * To test COF PDF API
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testCofPdf() throws TclCommonException {
		Mockito.when(quoteTeamsLicenseRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.createQuoteTeamLicense()));
		ResponseResource<String> response = teamsDRQuoteController.downloadCOFPDF(1, 1, new MockHttpServletResponse());
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * To test quote PDF API
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testQuotePdf() throws TclCommonException {
		Mockito.when(quoteTeamsLicenseRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.createQuoteTeamLicense()));
		ResponseResource<String> response = teamsDRQuoteController.downloadQuotePDF(1, 1,
				new MockHttpServletResponse());
		Assert.assertEquals(response.getStatus(), Status.SUCCESS);
	}

	/**
	 * To test approve quote API
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testApproveQuote() throws TclCommonException {
		Mockito.when(quoteTeamsLicenseRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.createQuoteTeamLicense()));
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(teamsDRObjectCreator.getOrder(true));
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(teamsDRObjectCreator.getOrderToLe(true));
		Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getOrderToLeProductFamily(true));
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getOrderLeAttributeValues(true));
		Mockito.when(orderProductComponentRepository.save(Mockito.any()))
				.thenReturn(teamsDRObjectCreator.getOrderToLe(true));
		Mockito.when(cofDetailsRepository.findByReferenceIdAndReferenceType(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);

		ResponseResource<TeamsDROrderDataBean> response = teamsDRQuoteController.approveQuotes(1,
				TeamsDRConstants.APPROVE, new MockHttpServletResponse());
		Assert.assertEquals(Status.SUCCESS, response.getStatus());
	}

	/**
	 * To test update stage and sub stages at quote_to_le level
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteToLeStatus() throws TclCommonException {
		Mockito.when(quoteTeamsLicenseRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(teamsDRObjectCreator.createQuoteTeamLicense()));
		System.out.println(QuoteStageConstants.GET_QUOTE.toString());
		ResponseResource<List<TeamsDRMultiQuoteLeBean>> response = teamsDRQuoteController.updateQuoteToLeStatus(1,
				"GET_QUOTE", "CONFIGURE_LICENSE");
		Assert.assertEquals("Configure License", response.getData().get(0).getSubStage());
	}
}
