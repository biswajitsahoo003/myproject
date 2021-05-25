package com.tcl.dias.oms.izpoc.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
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
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.izopc.service.v1.IzoPcPricingFeasibilityService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for izopc pricing and feasibility class
 * 
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IzoPcPricingFeasibilityServiceTest {

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
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

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
	IzoPcPricingFeasibilityService feasibilityService;

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
	SiteFeasibilityRepository siteFeasibilityRepository;
	
	@MockBean
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;
	
	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	MailNotificationBean mailNotificationBean;

	/**
	 * Initialize all the beans while loading
	 * @throws TclCommonException
	 */
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
		when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilityList());
		when(quoteIllSiteSlaRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteIllSiteSlaList());
		when(quoteIllSiteSlaRepository.save(Mockito.any()))
				.thenReturn(null);
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
						.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValuesGvpn());
		when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.anyInt()))
						.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValuesGvpn());
		when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteProductComponentsAttributeValue());
		when(siteFeasibilityRepository.save(Mockito.any()))
				.thenReturn(null);
		when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null))
				.thenReturn(quoteObjectCreator.getPricingDetails());
		when(pricingDetailsRepository.save(Mockito.any())).thenReturn(new PricingEngineResponse());
		when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.craeteUser());
		when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.craeteUser()));

		when(notificationService.newOrderSubmittedNotification(mailNotificationBean)).thenReturn(true);
		when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()
				)).thenReturn(quoteObjectCreator.geQuotePrice());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(new MstOmsAttribute());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		doNothing().when(siteFeasibilityRepository).deleteAll(Mockito.any());
		when(customerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getCustomer()));
		when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));
		when(illSiteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteIllSite()));
		when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getSolution()));
		when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getIllsites()));
		when(illSiteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getIllsites());
		when(siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelected(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilityList());
		when(siteFeasibilityRepository.findByIdAndQuoteIllSite_Id(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getSiteFeasibilityList().get(0)));
		when(userInfoUtils.getCustomerDetails()).thenReturn(quoteObjectCreator.getCustomerDetails());
	}
/**
 * Positive Test cases for processfeasibility 
 * @throws Exception
 */
	@Test
	public void testProcessFeasibilityTest() throws Exception {

		when(mqUtilsMock.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) throws TclCommonException {
				count++;
				if (count == 1)
					return quoteObjectCreator.getCustomerDetailsBean();
				else if (count == 2)
					return quoteObjectCreator.getCustomerLegalEntityDetailsBean();
				else if (count == 3)
					return quoteObjectCreator.getLocationDetailsjson();
				return quoteObjectCreator.getAddressDetailJSON();
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
/**
 * Negative test cases for process feasibility
 * @throws Exception
 */
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
		/*when(illSiteRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuoteIllSite());
		*/
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(
				"{\"results\": [ { \"Account.RTM_Cust\": \"Digital ENT\", \"BW_mbps_upd\": \"2488\", \"DistanceBetweenPOPs\": \"501\", \"Final_list_price\": \"47080000\", \"GVPN_ARC_per_BW\": \"0\", \"ILL_ARC_per_BW\": \"17672.1\", \"Identifier\": \"VALIDATION\", \"Industry_Cust\": \"Services\", \"Inv_GVPN_bw\": \"0\", \"Inv_ILL_bw\": \"4305\", \"Inv_NPL_bw\": \"0\", \"Inv_Other_bw\": \"1600\", \"Inv_Tot_BW\": \"5905\", \"Last_Mile_Cost_ARC\": \"0\", \"Last_Mile_Cost_NRC\": \"0\", \"Last_Modified_Date\": \"2018-09-27\", \"NPL_ARC_Mgmnt_charges\": \"0\", \"NPL_ARC_per_BW\": \"0\", \"NPL_List_Price\": \"47080000\", \"NPL_MRC_Mgmnt_charges\": \"0\", \"NPL_Port_ARC_Adjusted\": \"12412329\", \"NPL_Port_MRC_Adjusted\": \"1034360.75\", \"NPL_Port_NRC_Adjusted\": \"50000\", \"OpportunityID_Prod_Identifier\": \"0012000000BSomfAAD|1\", \"Other_ARC_per_BW\": \"5598.37\", \"Segment_Cust\": \"Service Provider\", \"Sum_CAT_1_2_MACD_FLAG\": \"209\", \"Sum_CAT_1_2_New_Opportunity_FLAG\": \"65\", \"Sum_CAT_3_MACD_FLAG\": \"0\", \"Sum_CAT_3_New_Opportunity_FLAG\": \"0\", \"Sum_CAT_4_MACD_FLAG\": \"0\", \"Sum_CAT_4_New_Opportunity_FLAG\": \"0\", \"Sum_Cat_1_2_opp\": \"274\", \"Sum_GVPN_Flag\": \"0\", \"Sum_IAS_FLAG\": \"95\", \"Sum_MACD_Opportunity\": \"209\", \"Sum_NPL_Flag\": \"0\", \"Sum_New_ARC_Converted\": \"85035768\", \"Sum_New_ARC_Converted_GVPN\": \"0\", \"Sum_New_ARC_Converted_ILL\": \"76078368\", \"Sum_New_ARC_Converted_NPL\": \"0\", \"Sum_New_ARC_Converted_Other\": \"8957396\", \"Sum_New_Opportunity\": \"65\", \"Sum_Other_Flag\": \"33\", \"Sum_tot_oppy_historic_opp\": \"274\", \"Sum_tot_oppy_historic_prod\": \"8\", \"TOT_ARC_per_BW\": \"14400.64\", \"a_latitude_final\": \"12.98063\", \"a_local_loop_interface\": \"GE\", \"a_longitude_final\": \"77.724888\", \"a_resp_city\": \"Bengaluru\", \"account_id_with_18_digit\": \"0012000000BSomfAAD\", \"b_latitude_final\": \"19.02086111\", \"b_local_loop_interface\": \"GE\", \"b_longitude_final\": \"72.83016667\", \"b_resp_city\": \"Mumbai\", \"bw_mbps\": \"2488\", \"chargeable_distance\": \"501\", \"createdDate_quote\": \"2018-09-27\", \"datediff\": \"726 days\", \"dist_betw_pops\": \"1066\", \"error_code\": \"NA\", \"error_flag\": \"0\", \"error_msg\": \"No Error\", \"f_a_lm_arc_bw_onwl\": \"0\", \"f_a_lm_nrc_bw_onwl\": \"0\", \"f_a_lm_nrc_inbldg_onwl\": \"0\", \"f_a_lm_nrc_mux_onwl\": \"0\", \"f_a_lm_nrc_nerental_onwl\": \"0\", \"f_a_lm_nrc_ospcapex_onwl\": \"0\", \"f_b_lm_arc_bw_onwl\": \"0\", \"f_b_lm_nrc_bw_onwl\": \"0\", \"f_b_lm_nrc_inbldg_onwl\": \"0\", \"f_b_lm_nrc_mux_onwl\": \"0\", \"f_b_lm_nrc_nerental_onwl\": \"0\", \"f_b_lm_nrc_ospcapex_onwl\": \"0\", \"feasibility_response_created_date\": \"2018-09-27\", \"hist_flag\": \"0\", \"intra_inter_flag\": \"Intercity\", \"link_id\": \"907\", \"list_price_mb\": \"18922.83\", \"model_name_transform\": \"NPL\", \"opportunityTerm\": \"12\", \"opportunity_day\": \"1\", \"opportunity_month\": \"9\", \"opportunity_term\": \"12\", \"p_a_lm_arc_bw_onwl\": \"0\", \"p_a_lm_nrc_bw_onwl\": \"0\", \"p_a_lm_nrc_inbldg_onwl\": \"0\", \"p_a_lm_nrc_mux_onwl\": \"0\", \"p_a_lm_nrc_nerental_onwl\": \"0\", \"p_a_lm_nrc_ospcapex_onwl\": \"0\", \"p_b_lm_arc_bw_onwl\": \"0\", \"p_b_lm_nrc_bw_onwl\": \"0\", \"p_b_lm_nrc_inbldg_onwl\": \"0\", \"p_b_lm_nrc_mux_onwl\": \"0\", \"p_b_lm_nrc_nerental_onwl\": \"0\", \"p_b_lm_nrc_ospcapex_onwl\": \"0\", \"port_lm_arc\": \"0\", \"port_pred_discount\": \"0.74\", \"predicted_NPL_Port_ARC\": \"12412328.98\", \"predicted_net_price\": \"12462328.98\", \"product_flavor_transform\": \"NPL\", \"product_name\": \"NPL\", \"prospect_name\": \"Regus\", \"quoteType_quote\": \"New Order\", \"quotetype_quote\": \"New Order\", \"site_id\": \"4252_4253\", \"sla_varient\": \"Standard\", \"sum_cat1_2_Opportunity\": \"274\", \"sum_cat_3_Opportunity\": \"0\", \"sum_cat_4_Opportunity\": \"0\", \"sum_model_name\": \"1\", \"time_taken\": \"1.34\", \"total_contract_value\": \"12462329\", \"total_osp_capex\": \"0\" } ] }");
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);

		feasibilityService.processFeasibility(20);

	}

	@Test
	public void testProcessFeasibilityResponse() throws TclCommonException {
		RestResponse pricingResponse = new RestResponse();
		pricingResponse.setStatus(Status.SUCCESS);
		pricingResponse.setData(
				"{\"results\": [ { \"Account.RTM_Cust\": \"Digital ENT\", \"BW_mbps_upd\": \"2488\", \"DistanceBetweenPOPs\": \"501\", \"Final_list_price\": \"47080000\", \"GVPN_ARC_per_BW\": \"0\", \"ILL_ARC_per_BW\": \"17672.1\", \"Identifier\": \"VALIDATION\", \"Industry_Cust\": \"Services\", \"Inv_GVPN_bw\": \"0\", \"Inv_ILL_bw\": \"4305\", \"Inv_NPL_bw\": \"0\", \"Inv_Other_bw\": \"1600\", \"Inv_Tot_BW\": \"5905\", \"Last_Mile_Cost_ARC\": \"0\", \"Last_Mile_Cost_NRC\": \"0\", \"Last_Modified_Date\": \"2018-09-27\", \"NPL_ARC_Mgmnt_charges\": \"0\", \"NPL_ARC_per_BW\": \"0\", \"NPL_List_Price\": \"47080000\", \"NPL_MRC_Mgmnt_charges\": \"0\", \"NPL_Port_ARC_Adjusted\": \"12412329\", \"NPL_Port_MRC_Adjusted\": \"1034360.75\", \"NPL_Port_NRC_Adjusted\": \"50000\", \"OpportunityID_Prod_Identifier\": \"0012000000BSomfAAD|1\", \"Other_ARC_per_BW\": \"5598.37\", \"Segment_Cust\": \"Service Provider\", \"Sum_CAT_1_2_MACD_FLAG\": \"209\", \"Sum_CAT_1_2_New_Opportunity_FLAG\": \"65\", \"Sum_CAT_3_MACD_FLAG\": \"0\", \"Sum_CAT_3_New_Opportunity_FLAG\": \"0\", \"Sum_CAT_4_MACD_FLAG\": \"0\", \"Sum_CAT_4_New_Opportunity_FLAG\": \"0\", \"Sum_Cat_1_2_opp\": \"274\", \"Sum_GVPN_Flag\": \"0\", \"Sum_IAS_FLAG\": \"95\", \"Sum_MACD_Opportunity\": \"209\", \"Sum_NPL_Flag\": \"0\", \"Sum_New_ARC_Converted\": \"85035768\", \"Sum_New_ARC_Converted_GVPN\": \"0\", \"Sum_New_ARC_Converted_ILL\": \"76078368\", \"Sum_New_ARC_Converted_NPL\": \"0\", \"Sum_New_ARC_Converted_Other\": \"8957396\", \"Sum_New_Opportunity\": \"65\", \"Sum_Other_Flag\": \"33\", \"Sum_tot_oppy_historic_opp\": \"274\", \"Sum_tot_oppy_historic_prod\": \"8\", \"TOT_ARC_per_BW\": \"14400.64\", \"a_latitude_final\": \"12.98063\", \"a_local_loop_interface\": \"GE\", \"a_longitude_final\": \"77.724888\", \"a_resp_city\": \"Bengaluru\", \"account_id_with_18_digit\": \"0012000000BSomfAAD\", \"b_latitude_final\": \"19.02086111\", \"b_local_loop_interface\": \"GE\", \"b_longitude_final\": \"72.83016667\", \"b_resp_city\": \"Mumbai\", \"bw_mbps\": \"2488\", \"chargeable_distance\": \"501\", \"createdDate_quote\": \"2018-09-27\", \"datediff\": \"726 days\", \"dist_betw_pops\": \"1066\", \"error_code\": \"NA\", \"error_flag\": \"0\", \"error_msg\": \"No Error\", \"f_a_lm_arc_bw_onwl\": \"0\", \"f_a_lm_nrc_bw_onwl\": \"0\", \"f_a_lm_nrc_inbldg_onwl\": \"0\", \"f_a_lm_nrc_mux_onwl\": \"0\", \"f_a_lm_nrc_nerental_onwl\": \"0\", \"f_a_lm_nrc_ospcapex_onwl\": \"0\", \"f_b_lm_arc_bw_onwl\": \"0\", \"f_b_lm_nrc_bw_onwl\": \"0\", \"f_b_lm_nrc_inbldg_onwl\": \"0\", \"f_b_lm_nrc_mux_onwl\": \"0\", \"f_b_lm_nrc_nerental_onwl\": \"0\", \"f_b_lm_nrc_ospcapex_onwl\": \"0\", \"feasibility_response_created_date\": \"2018-09-27\", \"hist_flag\": \"0\", \"intra_inter_flag\": \"Intercity\", \"link_id\": \"907\", \"list_price_mb\": \"18922.83\", \"model_name_transform\": \"NPL\", \"opportunityTerm\": \"12\", \"opportunity_day\": \"1\", \"opportunity_month\": \"9\", \"opportunity_term\": \"12\", \"p_a_lm_arc_bw_onwl\": \"0\", \"p_a_lm_nrc_bw_onwl\": \"0\", \"p_a_lm_nrc_inbldg_onwl\": \"0\", \"p_a_lm_nrc_mux_onwl\": \"0\", \"p_a_lm_nrc_nerental_onwl\": \"0\", \"p_a_lm_nrc_ospcapex_onwl\": \"0\", \"p_b_lm_arc_bw_onwl\": \"0\", \"p_b_lm_nrc_bw_onwl\": \"0\", \"p_b_lm_nrc_inbldg_onwl\": \"0\", \"p_b_lm_nrc_mux_onwl\": \"0\", \"p_b_lm_nrc_nerental_onwl\": \"0\", \"p_b_lm_nrc_ospcapex_onwl\": \"0\", \"port_lm_arc\": \"0\", \"port_pred_discount\": \"0.74\", \"predicted_NPL_Port_ARC\": \"12412328.98\", \"predicted_net_price\": \"12462328.98\", \"product_flavor_transform\": \"NPL\", \"product_name\": \"NPL\", \"prospect_name\": \"Regus\", \"quoteType_quote\": \"New Order\", \"quotetype_quote\": \"New Order\", \"site_id\": \"4252_4253\", \"sla_varient\": \"Standard\", \"sum_cat1_2_Opportunity\": \"274\", \"sum_cat_3_Opportunity\": \"0\", \"sum_cat_4_Opportunity\": \"0\", \"sum_model_name\": \"1\", \"time_taken\": \"1.34\", \"total_contract_value\": \"12462329\", \"total_osp_capex\": \"0\" } ] }");
		when(restClientService.post(Mockito.anyString(), Mockito.anyString())).thenReturn(pricingResponse);
		when(mqUtilsMock.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getLocationDetailjson());
		String response = "{\"Feasible\":[{\"a_X2km_prospect_min_bw\":\"0.064\",\"b_POP_DIST_KM_SERVICE_MOD\":\"5\",\"intra_inter_flag\":\"Intracity\",\"a_X2km_min_bw\":\"0.001\",\"b_lm_arc_bw_prov_ofrf\":\"0\",\"b_num_connected_cust\":\"0\",\"b_X2km_avg_dist\":\"1441.0390018\",\"b_lm_nrc_bw_onwl\":\"0\",\"b_X2km_max_bw\":\"  10240\",\"a_X2km_max_bw\":\"10240\",\"a_X2km_prospect_max_bw\":\"10240\",\"b_X2km_min_bw\":\"0.001\",\"a_longitude_final\":\"77.2150254\",\"b_access_check_CC\":\"No CC Found\",\"a_num_connected_building\":\"0\",\"b_Product.Name\":\"NPL\",\"a_city_tier\":\"Non_Tier1\",\"b_X5km_prospect_count\":\"3412\",\"a_sales_org\":\"Enterprise\",\"b_scenario_1\":\"0\",\"a_FATG_Ring_type\":\"SDH\",\"a_pop_lat\":\"28.63095826\",\"a_resp_city\":\"Central Delhi\",\"b_X2km_prospect_min_bw\":\"0.064\",\"b_FATG_Ring_type\":\"SDH\",\"a_X5km_prospect_min_bw\":\"0.064\",\"a_POP_Construction_Status\":\"In Service\",\"b_city_tier\":\"Non_Tier1\",\"b_time_taken\":\"11.589\",\"b_lm_nrc_mux_onwl\":\"58810\",\"b_sales_org\":\"Enterprise\",\"b_FATG_Category\":\"Metro Service Ready\",\"b_scenario_2\":\"0\",\"b_Network_F_NF_HH\":\"Network Feasible on HH\",\"Selected\":true,\"a_hh_name\":\"SC-DEL-BCP-0031-240340403\",\"b_lm_nrc_bw_prov_ofrf\":\"0\",\"b_latitude_final\":\"28.616262\",\"b_connected_cust_tag\":\"0\",\"a_POP_Building_Type\":\"Government\",\"a_X0.5km_prospect_avg_bw\":\"68.79545455\",\"a_lm_arc_bw_onwl\":\"108910\",\"b_X2km_prospect_max_bw\":\"10240\",\"b_X5km_prospect_min_bw\":\"0.064\",\"b_X0.5km_prospect_count\":\"  38\",\"b_OnnetCity_tag\":\"1\",\"pop_network_loc_id\":\"TINDDLNDELCNPL0030\",\"a_X0.5km_avg_bw\":\"      0.001\",\"a_X2km_prospect_avg_dist\":\"1447.093673\",\"a_POP_Category\":\"Metro Service Ready\",\"b_POP_DIST_KM_SERVICE\":\"2.16362202992\",\"a_X5km_prospect_avg_dist\":\"2570.671488\",\"b_pop_long\":\"77.20774270\",\"a_Predicted_Access_Feasibility\":\"Feasible with Capex\",\"a_POP_Network_Location_Type\":\"Mega POP\",\"a_scenario_1\":\"0\",\"a_scenario_2\":\"0\",\"b_POP_Building_Type\":\"Government\",\"b_X2km_prospect_count\":\"1641\",\"a_FATG_DIST_KM\":\" 367.2465429\",\"b_net_pre_feasible_flag\":\"0\",\"a_X5km_min_bw\":\"0.001\",\"a_X0.5km_prospect_max_bw\":\"500\",\"a_X2km_prospect_perc_feasible\":\"0.9946396665\",\"b_X5km_prospect_max_bw\":\"10240\",\"b_pop_name\":\"TCL  VSB, DELHI-1-11641110\",\"b_X0.5km_avg_bw\":\"  26.5000000\",\"b_X5km_max_bw\":\"  10240\",\"a_Network_F_NF_CC_Flag\":\"0\",\"b_Orch_BW\":\"34\",\"a_X0.5km_prospect_perc_feasible\":\"1\",\"a_site_id\":\"33\",\"b_Predicted_Access_Feasibility\":\"Feasible with Capex\",\"burstable_bw\":\"34\",\"a_Product.Name\":\"NPL\",\"b_X5km_prospect_num_feasible\":\"3400\",\"bw_mbps\":\"34\",\"b_X5km_prospect_avg_bw\":\" 76.02218757\",\"a_X2km_prospect_count\":\"1679\",\"b_X2km_prospect_min_dist\":\"352.601910124\",\"b_FATG_TCL_Access\":\"No\",\"b_last_mile_contract_term\":\"2 Year\",\"b_customer_segment\":\"Enterprise-Direct\",\"a_num_connected_cust\":\"0\",\"b_longitude_final\":\"77.213528\",\"a_POP_DIST_KM\":\"0.3672465429\",\"b_pop_address\":\"TCL,_Videsh_Sanchar_Bhavan,_Banglasaheb_Road,_New_Delhi_-_110_001\",\"b_hh_name\":\"HH-DEL-BCP-0032-240340453\",\"a_lm_nrc_nerental_onwl\":\"0\",\"b_lm_nrc_inbldg_onwl\":\"40000\",\"account_id_with_18_digit\":\"0012000001OiMX6\",\"a_lm_nrc_bw_onwl\":\"0\",\"a_X2km_cust_count\":\"300\",\"b_Network_Feasibility_Check\":\"Feasible\",\"b_FATG_DIST_KM\":\"333.96474119\",\"a_X5km_avg_bw\":\"164.6554943\",\"b_lm_nrc_ospcapex_onwl\":\"0\",\"b_core_check_hh\":\"Network Feasible on Core Ring of HH\",\"a_Network_F_NF_HH\":\"NA\",\"a_error_code\":\"NA\",\"b_X2km_min_dist\":\"481.84967993\",\"b_a_or_b_end\":\"B\",\"a_Network_Feasibility_Check\":\"Not Feasible\",\"b_X2km_prospect_num_feasible\":\"1632\",\"bw_mbps_upd\":\"34\",\"a_access_check_hh\":\"NA\",\"b_cost_permeter\":\"  0\",\"b_X0.5km_min_bw\":\"2.000\",\"a_X0.5km_prospect_count\":\"44\",\"a_X0.5km_avg_dist\":\"    471.9180868\",\"a_FATG_PROW\":\"No\",\"b_X2km_prospect_perc_feasible\":\"0.9945155393\",\"a_X2km_min_dist\":\" 471.9180868\",\"a_lm_nrc_mast_ofrf\":\"0\",\"a_X2km_prospect_num_feasible\":\"1670\",\"a_SERVICE_ID\":\"NA\",\"b_connected_building_tag\":\"0\",\"b_pop_lat\":\"28.63095826\",\"b_X5km_avg_dist\":\"2122.7313066\",\"a_lm_nrc_mast_onrf\":\"0\",\"a_HH_DIST_KM\":\"242.0701220\",\"a_pop_selected\":\"no\",\"a_min_hh_fatg\":\" 303\",\"a_X5km_avg_dist\":\"2116.888805\",\"b_X5km_prospect_min_dist\":\"352.601910124\",\"b_X2km_prospect_avg_bw\":\"105.8379427\",\"a_latitude_final\":\"28.6163909\",\"b_Network_F_NF_CC\":\"No CC Found\",\"b_POP_Category\":\"Metro Service Ready\",\"a_cost_permeter\":\"  0\",\"b_X0.5km_min_dist\":\"481.84967993\",\"b_lm_arc_bw_onwl\":\"108910\",\"b_Probabililty_Access_Feasibility\":\"0.93\",\"b_X0.5km_avg_dist\":\"490.60797661\",\"a_lm_arc_bw_onrf\":\"0\",\"site_id\":\"530_primary\",\"a_lm_nrc_bw_prov_ofrf\":\"0\",\"a_POP_TCL_Access\":\"Yes\",\"b_POP_Network_Location_Type\":\"Mega POP\",\"a_FATG_TCL_Access\":\"Yes\",\"b_error_flag\":\"0\",\"a_lm_nrc_mux_onwl\":\"58810\",\"sla_varient\":\"Standard\",\"a_lm_nrc_bw_onrf\":\"0\",\"a_pop_ui_id\":\"none\",\"b_total_cost\":\"207720\",\"b_X0.5km_prospect_avg_dist\":\"457.07767145\",\"a_core_check_hh\":\"NA\",\"b_Network_F_NF_HH_Flag\":\"1\",\"a_local_loop_interface\":\"GE\",\"a_Probabililty_Access_Feasibility\":\"0.92\",\"manual_flag\":\"1\",\"a_lm_arc_bw_prov_ofrf\":\"0\",\"b_X0.5km_prospect_min_bw\":\"2.000\",\"b_X5km_cust_count\":\" 438\",\"b_X0.5km_prospect_num_feasible\":\"  38\",\"b_FATG_PROW\":\"No\",\"opportunity_term\":\"24\",\"b_num_connected_building\":\"   0\",\"a_X0.5km_min_bw\":\"      0.001\",\"a_X5km_prospect_count\":\"3420\",\"a_lm_nrc_inbldg_onwl\":\"40000\",\"a_X2km_avg_bw\":\"196.7626033\",\"b_lm_arc_bw_onrf\":\"0\",\"b_X2km_avg_bw\":\" 199.3269628\",\"b_hh_flag\":\"1\",\"b_pop_ui_id\":\"none\",\"a_Network_F_NF_HH_Flag\":\"0\",\"a_error_flag\":\"0\",\"a_X0.5km_min_dist\":\"    471.9180868\",\"a_Orch_Connection\":\"Wireline\",\"b_X5km_min_dist\":\"481.84967993\",\"b_X2km_prospect_avg_dist\":\"1471.5637826\",\"b_error_msg\":\"No error\",\"a_last_mile_contract_term\":\"2 Year\",\"a_OnnetCity_tag\":\"1\",\"a_POP_DIST_KM_SERVICE\":\" 2.213629916\",\"dist_betw_pops\":\"   1\",\"a_connected_cust_tag\":\"0\",\"b_Orch_LM_Type\":\"Onnet\",\"a_X5km_prospect_num_feasible\":\"3409\",\"b_pop_network_loc_id\":\"TINDDLNDELCNPL0030\",\"Type\":\"OnnetWL_NPL\",\"b_pop_selected\":\"no\",\"a_access_check_CC\":\"No CC Found\",\"a_X0.5km_prospect_min_dist\":\"326.21594202\",\"a_FATG_Category\":\"Wimax Site\",\"a_X0.5km_cust_count\":\"1\",\"b_X0.5km_prospect_min_dist\":\"352.601910124\",\"b_HH_0_5km\":\"Red cross-13045391\",\"a_X5km_cust_count\":\"437\",\"a_X5km_prospect_avg_bw\":\" 76.1186269\",\"a_hh_flag\":\"1\",\"a_X2km_prospect_min_dist\":\"326.21594202\",\"b_X5km_min_bw\":\"0.001\",\"b_X0.5km_prospect_avg_bw\":\" 17.15789474\",\"b_HH_DIST_KM\":\"203.29668004\",\"b_X5km_prospect_perc_feasible\":\"0.9964830012\",\"b_POP_Construction_Status\":\"In Service\",\"a_net_pre_feasible_flag\":\"0\",\"prospect_name\":\"Dev Test Services & Sons\",\"Predicted_Access_Feasibility\":\"Not Feasible\",\"a_time_taken\":\"11.589\",\"a_total_cost\":\" 207720\",\"b_error_code\":\"NA\",\"a_X5km_min_dist\":\" 471.9180868\",\"b_X5km_prospect_avg_dist\":\"2587.024005\",\"a_pop_address\":\"TCL,_Videsh_Sanchar_Bhavan,_Banglasaheb_Road,_New_Delhi_-_110_001\",\"a_X0.5km_prospect_avg_dist\":\"401.5080933\",\"b_POP_TCL_Access\":\"Yes\",\"b_core_check_CC\":\"No CC Found\",\"a_X5km_prospect_max_bw\":\"10240\",\"b_POP_DIST_KM\":\"0.50830909040\",\"product_name\":\"NPL\",\"a_Network_F_NF_CC\":\"No CC Found\",\"a_X5km_max_bw\":\"10240\",\"b_lm_nrc_bw_onrf\":\"0\",\"a_lm_nrc_ospcapex_onwl\":\"     0\",\"a_X2km_avg_dist\":\"1454.066273\",\"b_Network_F_NF_CC_Flag\":\"0\",\"b_lm_nrc_mast_ofrf\":\"0\",\"b_access_check_hh\":\"Network Feasible on Access Ring of HH\",\"b_FATG_Network_Location_Type\":\"Access/Customer POP\",\"b_X2km_cust_count\":\" 296\",\"b_lm_nrc_mast_onrf\":\"0\",\"a_X2km_prospect_avg_bw\":\"104.06197975\",\"a_X5km_prospect_perc_feasible\":\"0.9967836257\",\"b_X5km_avg_bw\":\" 164.2864178\",\"a_connected_building_tag\":\"0\",\"a_pop_name\":\"TCL  VSB, DELHI-1-11641110\",\"a_core_check_CC\":\"No CC Found\",\"feasibility_response_created_date\":\"2018-09-27\",\"b_resp_city\":\"Central Delhi\",\"a_FATG_Network_Location_Type\":\"Mega POP\",\"a_pop_long\":\"77.20774270\",\"a_X0.5km_prospect_min_bw\":\"2\",\"a_POP_DIST_KM_SERVICE_MOD\":\" 5\",\"a_X5km_prospect_min_dist\":\"326.21594202\",\"a_Orch_BW\":\"34\",\"b_Orch_Category\":\"Capex greater than 175m\",\"b_X0.5km_max_bw\":\"     56\",\"b_site_id\":\"34\",\"a_HH_0_5km\":\"SC-DEL-BCP-0031-240340403\",\"a_X0.5km_max_bw\":\"      0.001\",\"a_X0.5km_prospect_num_feasible\":\"44\",\"a_a_or_b_end\":\"A\",\"b_X0.5km_cust_count\":\"   4\",\"a_Orch_LM_Type\":\"Onnet\",\"b_lm_nrc_nerental_onwl\":\"0\",\"b_local_loop_interface\":\"GE\",\"a_FATG_Building_Type\":\"Commercial\",\"a_Orch_Category\":\"Capex greater than 175m\",\"chargeable_distance\":\"  5\",\"b_X0.5km_prospect_perc_feasible\":\"1.0000000000\",\"b_FATG_Building_Type\":\"Commercial\",\"b_Orch_Connection\":\"Wireline\",\"b_min_hh_fatg\":\"254\",\"b_X0.5km_prospect_max_bw\":\"  100\",\"quotetype_quote\":\"New Order\",\"a_error_msg\":\"No error\",\"b_SERVICE_ID\":\"NA\",\"a_customer_segment\":\"Enterprise-Direct\"}],\"NotFeasible\":[]}";
		feasibilityService.processFeasibilityResponse(response);
	}

	
}
