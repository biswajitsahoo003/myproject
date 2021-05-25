package com.tcl.dias.oms.sfdc.service;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.common.constants.CommonConstants.COMMA;
import static com.tcl.dias.common.constants.CommonConstants.IAS;
import static com.tcl.dias.common.constants.CommonConstants.MACD;
import static com.tcl.dias.common.constants.CommonConstants.MMR_CROSS_CONNECT;
import static com.tcl.dias.common.constants.CommonConstants.NDE;
import static com.tcl.dias.common.constants.CommonConstants.NPL;
import static com.tcl.dias.common.constants.CommonConstants.PRIMARY;
import static com.tcl.dias.common.constants.CommonConstants.UCAAS;
import static com.tcl.dias.common.constants.CommonConstants.YES;

import static com.tcl.dias.common.constants.LeAttributesConstants.LEGAL_ENTITY_NAME;
import static com.tcl.dias.common.utils.Utils.convertJsonToObject;
import static com.tcl.dias.oms.constants.FPConstants.BANDWIDTH;
import static com.tcl.dias.oms.constants.FPConstants.CLOUD_PROVIDER;
import static com.tcl.dias.oms.constants.FPConstants.INTERNET_PORT;
import static com.tcl.dias.oms.constants.FPConstants.PORT_BANDWIDTH;
import static com.tcl.dias.oms.constants.FPConstants.VPN_PORT;
import static com.tcl.dias.oms.constants.FPConstants.VPN_PORT;
import static com.tcl.dias.oms.constants.GvpnConstants.GVPN;
import static com.tcl.dias.oms.constants.GvpnConstants.ORDER_ENRICHMENT;
import static com.tcl.dias.oms.constants.MACDConstants.ADD_IP_SERVICE;
import static com.tcl.dias.oms.constants.MACDConstants.ADD_SECONDARY;
import static com.tcl.dias.oms.constants.MACDConstants.DEMO_EXTENSION;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ADD_LOCATIONS;
import static com.tcl.dias.oms.constants.QuoteStageConstants.GET_QUOTE;
import static com.tcl.dias.oms.constants.QuoteStageConstants.SELECT_CONFIGURATION;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CLOSED_DROPPED;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CLOSED_WON_COF_RECI;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.COF_WON_PROCESS_STAGE;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_ENTITY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_FEASIBILITY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_OPTY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_PRODUCT;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.DELETE_PRODUCT;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GSC_PRODUCT_NAME;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GSIP;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.IPC_PRODUCT_NAME;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GSIP_PRODUCT;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.IPC_PRODUCT_NAME;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.OPEN_CLOSED_BCR;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.PROPOSAL_SENT;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.SELL_THROUGH_CLASSIFICATION;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.SELL_WITH_CLASSIFICATION;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.TIGER_SERVICE_TYPE_DOMESTIC_ORDER;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.UPDATE_FEASIBILITY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.UPDATE_INPROGRESS_BCR;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.UPDATE_PRODUCT;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.UPDATE_SITE;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.VERBAL_AGREEMENT_STAGE;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.tcl.dias.common.beans.ETCRecordBean;
import com.tcl.dias.common.beans.TerminationWaiverBean;
import com.tcl.dias.common.beans.ThirdPartyServiceJobBean;
import com.tcl.dias.common.beans.WaiverResponseBean;
import com.tcl.dias.oms.beans.SFDCCommercialBifurcationBean;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.oms.constants.*;
import com.tcl.dias.oms.pdf.constants.PDFConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.tcl.dias.oms.entity.entities.QuoteTeamsDR;
import com.tcl.dias.oms.entity.repository.QuoteTeamsDRRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.SessionFactoryServiceContributor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.COPFOmsRequest;
import com.tcl.dias.common.beans.COPFOmsResponse;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.LinkCOPFDetails;
import com.tcl.dias.common.beans.MDMOmsResponseBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.ordertocash.beans.SiteToService;
import com.tcl.dias.common.sfdc.bean.BCROmsRecord;
import com.tcl.dias.common.sfdc.bean.BCROmsRequest;
import com.tcl.dias.common.sfdc.bean.BCROmsResponse;
import com.tcl.dias.common.sfdc.bean.BCROptyProperitiesBean;
import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponse;
import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponseBean;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SfdcCreditCheckQueryRequest;
import com.tcl.dias.common.sfdc.bean.SiteOpportunityLocation;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.sfdc.response.bean.FeasibilityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.OpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductServicesResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductsserviceResponse;
import com.tcl.dias.common.sfdc.response.bean.SiteLocationResponse;
import com.tcl.dias.common.sfdc.response.bean.SiteResponseBean;
import com.tcl.dias.common.sfdc.response.bean.StagingResponseBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.SFDCCommercialBifurcationBean;
import com.tcl.dias.oms.beans.SfdcAttr;
import com.tcl.dias.oms.beans.SfdcAuditBean;
import com.tcl.dias.oms.beans.SfdcErrorAudit;
import com.tcl.dias.oms.beans.SfdcFullAudit;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PreMfRequest;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteLeCreditCheckAudit;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.EngagementOpportunityRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PartnerRepository;
import com.tcl.dias.oms.entity.repository.PreMfRequestRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeCreditCheckAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SfdcJobRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.dias.oms.sfdc.factory.OmsSfdcInputMapperFactory;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 *
 * This file contains the OmsSfdcService.java class.
 *
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class OmsSfdcUtilService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcUtilService.class);
	public static final String unites_states_of_america = "united states of america";
	public static final String united_kingdom = "united kingdom";
	public static final String india = "india";
	public static final String singapore = "singapore";
	public static final String germany = "germany";
	public static final String france = "france";
	public static final String NORTH_AMERICA = "North America";
	public static final String EUROPE = "Europe";
	public static final String ASIA = "Asia";

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderNplLinkRepository orderNplLinkRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${sfdc.create.bcr}")
	String BcrQueue;

	@Value("${rabbitmq.opportunity.create}")
	String sfdcCreateOpty;

	@Value("${rabbitmq.sfdc.create.feasibility}")
	String sfdcCreateFeasReq;

	@Value("${rabbitmq.opportunity.productservices}")
	String sfdcCreatePrdService;

	@Value("${rabbitmq.opportunity.updateproductservices}")
	String sfdcUpdatedService;

	@Value("${rabbitmq.opportunity.deleteproductservices}")
	String sfdcProductDeleteService;

	@Value("${rabbitmq.opportunity.site}")
	String sfdcUpdateSite;

	@Value("${rabbitmq.opportunity.update}")
	String sfdcUpdateOpty;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Value("${application.test.emails}")
	String[] applicationTestMails;

	@Value("${sfdc.test.accountid}")
	String accountId;

	@Value("${sfdc.test.accountcuid}")
	String accountCuid;

	@Value("${application.env}")
	String appEnv;

	@Value("${rabbitmq.customer.account.manager}")
	String customerAccountManagerName;

	@Value("${rabbitmq.customer.account.manager.email}")
	String customerAccountManagerEmail;

	@Value("${rabbitmq.customer.account.rtm}")
	String customerAccountRtmName;

	@Value("${rabbitmq.partner.opportunity.create}")
	String sfdcPartnerCreateOpty;
	
	@Value("${rabbitmq.mapper.service}")
	String sfdcMapperService;
	
	@Autowired
	ApplicationContext appctx;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	ProductSolutionRepository quoteProductSolutionRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	IllSiteRepository illSitesRepository;
	
	@Autowired
	QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	SfdcJobRepository sfdcJobRepository;

	@Autowired
	OmsSfdcInputMapperFactory factory;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Value("${rabbitmq.mstaddress.detail}")
	String addressDetail;

	@Value("${rabbitmq.mstaddrbylocationid.detail}")
	String addressDetailByLocationId;


	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	NplLinkRepository nplLinkRepository;

	@Value("${rabbitmq.partner.entity.create}")
	String sfdcCreatePartnerEntity;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Value("${mdm.address.contactid.add}")
	String addContactIdtoCustomerBillingInfo;

	@Value("${sfdc.process.cofid}")
	String processCopfId;


	@Value("${rabbitmq.get.partner.account.name.by.partner}")
	String partnerAccountNameMQ;

	@Value("${sfdc.process.creditcheck.query}")
	String creditCheckQueue;

	@Autowired
	QuoteLeCreditCheckAuditRepository quoteLeCreditCheckRepository;

	@Autowired
	CreditCheckService creditCheckService;


	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	EngagementOpportunityRepository engagementOpportunityRepository;

	@Autowired
	PartnerRepository partnerRepository;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	private static SimpleDateFormat yearlyFormatter = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat slashFormatter = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	NotificationService notificationService;

	@Autowired
	QuotePriceRepository quotePriceRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	NplLinkRepository quoteNplLinkRepository;
	
	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;
	
	@Autowired
    private PreMfRequestRepository preMfRequestRepository;

	@Autowired
	QuoteTeamsDRRepository quoteTeamsDRRepository;

	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;

	@Value("${rabbitmq.sfdc.create.waiver}")
	String sfdcCreateWaiver;

	@Value("${rabbitmq.sfdc.update.waiver}")
	String sfdcUpdateWaiver;
	
	@Autowired
	ThirdPartyServiceJobsAuditRepository thirdPartyServiceJobsAuditRepository;

	@Autowired
	MstSfdcAmSaRegionRepository mstSfdcAmSaRegionRepository;
	/**
	 * processCreateOpty
	 *
	 * @param quoteToLe
	 * @param productName
	 * @throws TclCommonException
	 */
	@Transactional
	public void processCreateOpty(QuoteToLe quoteToLe, String productName) throws TclCommonException {
		LOGGER.info("OmsSfdcService.processCreateOpty method invoked");
		if (StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
			// Get the OrderLeAttributes
			LOGGER.info("SfdcOptyId not exists");
			OpportunityBean opportunityBean = new OpportunityBean();

			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Quote exists");
				String orderCode = quote.getQuoteCode();
				LOGGER.info("orderCode::" + orderCode);
				LOGGER.info("quote::" + quote.getId());
				opportunityBean
						.setDescription("Creating opportunity for the order " + quote.getId() + " on " + new Date());
				opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
				opportunityBean.setReferralToPartner(SFDCConstants.NO);
				opportunityBean.setType(SFDCConstants.NEW_ORDER);
				opportunityBean.setSubType(SFDCConstants.NEW_ORDER);
				opportunityBean.setName(constructOpportunityName(quoteToLe,productName,opportunityBean.getType()));
				opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
				String currency = SFDCConstants.INR;
				if (quoteToLe.getCurrencyCode() != null) {
					currency = quoteToLe.getCurrencyCode();
				}
				opportunityBean.setOpportunityClassification(SFDCConstants.SELL_TO);
				opportunityBean.setCurrencyIsoCode(currency);
				opportunityBean.setCustomerChurned(SFDCConstants.NO);
				opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
				opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
				opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
				if ("Partner".equalsIgnoreCase(userInfoUtils.getUserType())) {
					opportunityBean.setIsPartnerOrder(SFDCConstants.PARTNER);
					if(quoteToLe.getClassification()!=null) {
						opportunityBean.setOpportunityClassification(quoteToLe.getClassification());
					}
					opportunityBean.setOpportunityClassification(SFDCConstants.SELL_WITH_CLASSIFICATION);
				} else {
					opportunityBean.setIsPartnerOrder(SFDCConstants.OTHERS);
				}
				opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS + orderCode);
				opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
				if(quoteToLe.getTpsSfdcParentOptyId()!=null  && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
									&& !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				}
				opportunityBean.setCurrentCircuitServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
				LOGGER.info("Get customer details::" + quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
					setSfdcAccId(opportunityBean, customerDetails);
				} else {
					opportunityBean.setAccountId(accountId);
				}
				List<MstSfdcAmSaRegion> mstSfdcAmSaRegion=null;
				Optional<User> userSource = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
				String userType="";
				if (userSource.isPresent()) {
					userType=userSource.get().getUserType();
				}
				if (userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
					if (appEnv.equals(SFDCConstants.PROD)) {
						LOGGER.info("OPPORTUNITY USER EMAIL ID {}", userSource.get().getEmailId());
						opportunityBean.setOwnerName(userSource.get().getEmailId());
						mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(userSource.get().getEmailId());
						if(mstSfdcAmSaRegion!=null && !mstSfdcAmSaRegion.isEmpty()) {
							LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
						}
						else{
							LOGGER.info("MST SFDC Region is empty for "+Utils.getSource());
							opportunityBean.setSalesAdministratorRegion(null);
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				} else {
					if (appEnv.equals(SFDCConstants.PROD)) {
						String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
						String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
						opportunityBean.setOwnerName(name);
						try {
							String email = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
							mstSfdcAmSaRegion = mstSfdcAmSaRegionRepository.findByAmEmail(email);
							if (mstSfdcAmSaRegion != null && !mstSfdcAmSaRegion.isEmpty()) {
								LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
								opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							} else {
								LOGGER.info("MST SFDC Region is empty for " + email);
								opportunityBean.setSalesAdministratorRegion(null);
							}
						}
						catch (Exception e){
							LOGGER.info("MST SFDC Sales Region :: Email is not available");
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				}
				
				LOGGER.info("Product name for create opportunity method {} ", productName);
				if (productName != null) {
					IOmsSfdcInputHandler handler = factory.getInstance(productName);
					if (handler != null) {
						handler.getOpportunityBean(quoteToLe, opportunityBean);
					}
				}
				Byte isComplete=CommonConstants.BACTIVE;
				LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunityBean.getType());
				LOGGER.info("opportunitybean"+opportunityBean.getType()+" "+opportunityBean.getSubType());
				if (SFDCConstants.TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
					//opportunityBean.setType(SFDCConstants.TERMINATION);
					//opportunityBean.setSubType(SFDCConstants.SERVICE_REPLACEMENT);
					opportunityBean.setAutoCreatedTerminationOpportunity(CommonConstants.TRUE);
					//opportunityBean.setCurrentCircuitServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
					if(StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
						 isComplete=CommonConstants.BDEACTIVATE;
					}else {
						opportunityBean.setParentTerminationOpportunityName(quoteToLe.getTpsSfdcOptyId());
					}
				}

				quoteToLeRepository.save(quoteToLe);
				persistSfdcServiceJob(quote.getQuoteCode(), sfdcCreateOpty, Utils.convertObjectToJson(opportunityBean),
						isComplete, CREATE_OPTY, getSequenceNumber(CREATE_OPTY),null,CommonConstants.BDEACTIVATE);
			}
		}
		LOGGER.info("OmsSfdcService.processCreateOpty method exited");
	}

	private String constructOpportunityName(QuoteToLe quoteToLe, String productName, String OrderType) throws TclCommonException {
		String name="";
		final String[] portBandwidth = {""};
		String accountName = "";
		Quote quote = quoteToLe.getQuote();
		final String[] city={""};
		QuoteToLeProductFamily quoteToLeProductFamily = null;
		String cityAend = "";
		String cityBend = "";
		String type = "primary";
		final Integer[] noOfSites = {0};
		String noOfLinks = "";
		final String[] cloudProvider = {""};
		final String[] orderType = {""};
		String nplProductName = "";
		Byte isCrossConnect = BDEACTIVATE;
		Byte isPassive = BDEACTIVATE;

		LOGGER.info("Inside constructOpportunityName() for quoteId {}",quoteToLe.getQuote().getId());
		if (MACD.equalsIgnoreCase(quoteToLe.getQuoteType()) || TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteType()) || CANCELLATION_ORDER.equalsIgnoreCase(quoteToLe.getQuoteType())){

			orderType[0] = StringUtils.capitalize(quoteToLe.getQuoteType().toLowerCase());

			Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
			if(attributeMapper != null && !(attributeMapper.isEmpty())) {
				if (Objects.nonNull(attributeMapper.get(LEGAL_ENTITY_NAME))
						&& StringUtils.isNotBlank(attributeMapper.get(LEGAL_ENTITY_NAME))) {
					accountName = attributeMapper.get(LEGAL_ENTITY_NAME);
				}
			}
		}
		else if(NEW.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			accountName = quote.getCustomer().getCustomerName();
			orderType[0] = NEW_ORDER;
		}

		if(StringUtils.isBlank(accountName)){
			accountName = quote.getCustomer().getCustomerName();
		}

		if(CommonConstants.NPL.equalsIgnoreCase(productName) && (quoteToLe.getQuoteToLeProductFamilies() != null) && !(quoteToLe.getQuoteToLeProductFamilies().isEmpty())){
			if (quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions() != null) {
				nplProductName = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering().getProductName();
				if (MMR_CROSS_CONNECT.equalsIgnoreCase(nplProductName)) {
					isCrossConnect = BACTIVE;
					LOGGER.info("isCrossConnect {}",isCrossConnect);
				}
			}
		}
		if (productName.equalsIgnoreCase(GVPN) || productName.equalsIgnoreCase(IAS) || (BACTIVE.equals(isCrossConnect))) {
			LOGGER.info("Inside GVPN, IAS, Crossconnect block");
			if (productName.equalsIgnoreCase(GVPN)) {
				quoteToLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLe.getId(), GVPN);
			} else if (productName.equalsIgnoreCase(IAS)) {
				quoteToLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLe.getId(), IAS);
			}
			else if (MMR_CROSS_CONNECT.equalsIgnoreCase(nplProductName)) {
				quoteToLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLe.getId(), NPL);
			}
			if (Objects.nonNull(quoteToLeProductFamily)) {
				List<ProductSolution> productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily);
				if (Objects.nonNull(productSolutions)) {
					productSolutions.stream().forEach(productSolution -> {
						List<QuoteIllSite> quoteIllSites = illSitesRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);
						if (Objects.nonNull(quoteIllSites)) {
							noOfSites[0] = quoteIllSites.size();
							LOGGER.info("No of sites: {}",noOfSites[0]);
							if(!(quoteIllSites.isEmpty())){
								if (MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
									List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite(quoteIllSites.get(0));
									orderType[0] = quoteIllSiteToService.get(0).getErfSfdcOrderType();
								}
								if (BACTIVE.equals(quoteToLe.getIsAmended()))
								{
									orderType[0] = AMENDMENT;
								}
								if (DEMO_EXTENSION.equalsIgnoreCase(quoteToLe.getQuoteCategory()))
								{
									orderType[0] = StringUtils.capitalize(DEMO_EXTENSION.toLowerCase());
								}
								if(ADD_IP_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory()))
								{
									orderType[0] = StringUtils.capitalize(OTHERS_SFDC);
								}
								if (ADD_SECONDARY.equalsIgnoreCase(quoteToLe.getQuoteCategory()))
								{
									orderType[0] = StringUtils.capitalize(ADD_SECONDARY);
								}
								if(quoteToLe.getQuoteToLeProductFamilies() != null && !(quoteToLe.getQuoteToLeProductFamilies().isEmpty())) {
									if (quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions() != null && !(quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().isEmpty()))
									{
										LOGGER.info("ProductSolutions  :: {}",quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions());

										if (quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering() != null &&
												MMR_CROSS_CONNECT.equalsIgnoreCase(quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering().getProductName())) {
											try {
												LOGGER.info("Inside Crossconnect 1st block");
												String crossConnectType = getCrossConnectType(quoteIllSites.get(0), CROSS_CONNECT, CROSS_CONNECT_TYPE, type);
												if (StringUtils.isNotBlank(crossConnectType) && PASSIVE.equalsIgnoreCase(crossConnectType)) {
													portBandwidth[0] = "NA"; //No bandwidth in cross connect passive - multisite
												}
											} catch (TclCommonException e) {
												e.printStackTrace();
											}
										}
								}
								}
									if (quoteIllSites.size()<2) {
										try {
											city[0] = getcityName(quoteIllSites.get(0).getId());
										} catch (TclCommonException e) {
											e.printStackTrace();
										}
										if (productName.equalsIgnoreCase(GVPN)) {
											try {
												portBandwidth[0] = getPortBw(quoteIllSites.get(0),
														FPConstants.VPN_PORT.toString(), PORT_BANDWIDTH.toString(), type);
											} catch (TclCommonException e) {
												e.printStackTrace();
											}
										} else if (productName.equalsIgnoreCase(IAS)) {
											try {
												portBandwidth[0] = getPortBw(quoteIllSites.get(0),
														INTERNET_PORT.toString(), PORT_BANDWIDTH.toString(), type);
											} catch (TclCommonException e) {
												e.printStackTrace();
											}
										}
										if(quoteToLe.getQuoteToLeProductFamilies() != null && !(quoteToLe.getQuoteToLeProductFamilies().isEmpty())) {
											if (quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions() != null && !(quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().isEmpty()))
											{
												LOGGER.info("ProductSolutions  :: {}",quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions());
												if (quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering() != null &&
														MMR_CROSS_CONNECT.equalsIgnoreCase(quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering().getProductName())) {
												try {
													LOGGER.info("Inside Crossconnect 2nd block");
													portBandwidth[0] = getPortBw(quoteIllSites.get(0),
															CROSS_CONNECT, BANDWIDTH.toString(), type);
													String crossConnectType = getCrossConnectType(quoteIllSites.get(0), CROSS_CONNECT, CROSS_CONNECT_TYPE, type);
													if (StringUtils.isNotBlank(crossConnectType) && PASSIVE.equalsIgnoreCase(crossConnectType)) {
														portBandwidth[0] = "NA"; //No bandwidth in cross connect passive
													}
												} catch (TclCommonException e) {
													e.printStackTrace();
												}
											}
										}

									}
							}
						}
					}
					});
					LOGGER.info("City, Portbandwidth inside GVPN, IAS, Crossconnect Block: {} {}", city[0],portBandwidth[0]);
					if (StringUtils.isNotBlank(portBandwidth[0]) && "NA".equalsIgnoreCase(portBandwidth[0])){
						isPassive = BACTIVE;
					}
				}

			}
			if (StringUtils.isNotBlank(orderType[0])){
				OrderType = orderType[0];
			}
			if(StringUtils.isEmpty(OrderType)){
				OrderType="";
			}

			if (noOfSites[0]!=0) {
				if (noOfSites[0] < 2) {
					name = "TCx_" + productName + "_" + city[0] + "_" + portBandwidth[0] + "M" + "_" + OrderType + "_" + accountName;

					if(StringUtils.isBlank(portBandwidth[0])){
						name = "TCx_" + productName + "_" + city[0] + "_" + OrderType + "_" + accountName;
					}

					if (BACTIVE.equals(isPassive)){
						name = "TCx_" + productName + "_" + city[0] + "_" + "NA" + "_" + OrderType + "_" + accountName; //No bandwidth in cross connect passive
					}
				} else {
					name = "TCx_" + productName + "_" + "multi-site" + "(" + noOfSites[0] + ")" + "_" + "multiBw" + "(" + noOfSites[0] + ")" + "_" + OrderType + "_" + accountName;
					if (BACTIVE.equals(isPassive)){
						name = "TCx_" + productName + "_" + "multi-site" + "(" + noOfSites[0] + ")" + "_" + "NA" + "_" + OrderType + "_" + accountName; //No bandwidth in cross connect passive
					}
				}
			}
			else{
				name = "TCx_" + productName + "_" + OrderType + "_" + accountName;
			}
		}
		else if (productName.equalsIgnoreCase(NPL )|| productName.equalsIgnoreCase(NDE)) {
			if (BDEACTIVATE.equals(isCrossConnect)){
				LOGGER.info("Inside NPL, NDE block");
			List<QuoteNplLink> quoteNplLinks = nplLinkRepository.findByQuoteIdAndStatus(quote.getId(), (byte) 1);
			LOGGER.info("quoteNpllinks::" + quoteNplLinks.toString() + "Size: " + quoteNplLinks.size());
			noOfLinks = String.valueOf(quoteNplLinks.size());
			if (!(quoteNplLinks.isEmpty())) {
				if (MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
					List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(quoteNplLinks.get(0).getId());
					orderType[0] = quoteIllSiteToService.get(0).getErfSfdcOrderType();
				}
				if (DEMO_EXTENSION.equalsIgnoreCase(quoteToLe.getQuoteCategory()))
				{
					orderType[0] = StringUtils.capitalize(DEMO_EXTENSION.toLowerCase());
				}
				if (BACTIVE.equals(quoteToLe.getIsAmended())) {
					orderType[0] = AMENDMENT;
				}
				if (quoteNplLinks.size() < 2) {

					cityAend = getcityName(quoteNplLinks.get(0).getSiteAId());
					cityBend = getcityName(quoteNplLinks.get(0).getSiteBId());
					portBandwidth[0] = getNewBandwidthPort(quoteNplLinks.get(0).getId(),
							FPConstants.NATIONAL_CONNECTIVITY.toString(), PORT_BANDWIDTH.toString(), type);
					LOGGER.info("Cities, Portbandwidth inside NPL block, {} {} {}", cityAend, cityBend, portBandwidth[0]);
				}

			}
			if (StringUtils.isNotBlank(orderType[0])) {
				OrderType = orderType[0];
			}

			if(StringUtils.isEmpty(OrderType)){
					OrderType="";
			}

				if (quoteNplLinks.size() != 0) {
				if (quoteNplLinks.size() < 2) {
					name = "TCx_" + productName + "_" + cityAend + "_" + cityBend + "_" + portBandwidth[0] + "M" + "_" + OrderType + "_" + accountName;

					if(StringUtils.isBlank(portBandwidth[0])){
						name = "TCx_" + productName + "_" + cityAend + "_" + cityBend  + "_" + OrderType + "_" + accountName;
					}

				} else {
					name = "TCx_" + productName + "_" + "multi-site" + "(" + noOfLinks + ")" + "_" + "multiBw" + "(" + noOfLinks + ")" + "_" + OrderType + "_" + accountName;

				}
			} else {
				name = "TCx_" + productName + "_" + OrderType + "_" + accountName;
			}
		}
		}

		else  if (productName.equalsIgnoreCase(IZOPC)) {
			LOGGER.info("Inside IZO-PC block");

			if (quoteToLe.getQuoteToLeProductFamilies() != null) {
				if (quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions() != null) {

					quoteToLe.getQuoteToLeProductFamilies().stream().forEach(prodFamily -> {
						prodFamily.getProductSolutions().stream().forEach(prodSolution -> {
							List<QuoteIllSite> quoteIllSites = illSitesRepository.findByProductSolutionAndStatus(prodSolution, (byte) 1);
							if (Objects.nonNull(quoteIllSites)) {
								noOfSites[0] = quoteIllSites.size();
								LOGGER.info("No of sites: {}", noOfSites[0]);
								if (!(quoteIllSites.isEmpty())) {
									if (MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
										List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite(quoteIllSites.get(0));
										orderType[0] = quoteIllSiteToService.get(0).getErfSfdcOrderType();
									}
									if (DEMO_EXTENSION.equalsIgnoreCase(quoteToLe.getQuoteCategory()))
									{
										orderType[0] = StringUtils.capitalize(DEMO_EXTENSION.toLowerCase());
									}
									if (BACTIVE.equals(quoteToLe.getIsAmended())) {
										orderType[0] = AMENDMENT;
									}

									if (quoteIllSites.size() < 2) {
										try {
											cloudProvider[0] = getCloudProviderName(quoteIllSites.get(0),
													FPConstants.IZO_PORT.toString(), CLOUD_PROVIDER.toString(), type);
										} catch (TclCommonException e) {
											e.printStackTrace();
										}

									}
									LOGGER.info("Cloud Provider, {} ", cloudProvider[0]);
								}
							}
						});
					});
				}
			}
			if (StringUtils.isNotBlank(orderType[0])){
				OrderType = orderType[0];
			}

			if(StringUtils.isEmpty(OrderType)){
				OrderType="";
			}

			if (noOfSites[0]!=0) {
				if (noOfSites[0] < 2) {
					name= "TCx_" + productName + "_" + OrderType +  "_" + cloudProvider[0] + "_" + accountName;
				}
			}
			else{
				name = "TCx_" + productName + "_" + OrderType + "_" + accountName;
			}
		}
		else if (IPC_PRODUCT_NAME.equalsIgnoreCase(productName)) {
			OrderType = quoteToLe.getQuoteType();
			name = "TCx_" + productName + "_" + OrderType + "_" + accountName;
		}
		if(GSIP.equalsIgnoreCase(productName)) {
			name = quote.getCustomer().getCustomerName();
		}else {
			name="Optimus Opportunity -" + quote.getId();
		}
		LOGGER.info("Opportunity name in constructOpportunityName(): {}",name);
		
			return  name;
	}

	private String getCrossConnectType(QuoteIllSite quoteIllSite, String componentName, String attributeName, String type) throws TclCommonException {
		try {
			LOGGER.info("Input quoteIllSiteId {} : Component Name {} : Attribute Name {} : type {}",quoteIllSite.getId(),componentName, attributeName, type);
			QuoteProductComponent quoteprodComp = null;
			List<QuoteProductComponent> quoteProdComponents = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName, type);
			for (QuoteProductComponent quoteProductComponent : quoteProdComponents) {
				quoteprodComp = quoteProductComponent;
				break;
			}
			if (quoteprodComp != null) {

				QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(),
								attributeName)
						.stream().findFirst().get();

				return attributeValue.getAttributeValues();
			} else {
				return "";
			}
		} catch (Exception e) {
			throw new TclCommonException(com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private String getCloudProviderName(QuoteIllSite quoteIllSite, String componentName, String attributeName, String type) throws TclCommonException {

		try {
			LOGGER.info("Input quoteIllSiteId {} : Component Name {} : Attribute Name {} : type {}",quoteIllSite.getId(),componentName, attributeName, type);
			QuoteProductComponent quoteprodComp = null;
			List<QuoteProductComponent> quoteProdComponents = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName, type);
			for (QuoteProductComponent quoteProductComponent : quoteProdComponents) {
				quoteprodComp = quoteProductComponent;
				break;
			}
			if (quoteprodComp != null) {

				QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(),
								attributeName)
						.stream().findFirst().get();

				return attributeValue.getAttributeValues();
			} else {
				return "";
			}
		} catch (Exception e) {
			throw new TclCommonException(com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private String getPortBw(QuoteIllSite quoteIllSite, String componentName, String attributeName, String type) throws TclCommonException {
		try {
			LOGGER.info("Input quoteIllSiteId {} : Component Name {} : Attribute Name {} : type {}",quoteIllSite.getId(),componentName, attributeName, type);
			QuoteProductComponent quoteprodComp = null;
			List<QuoteProductComponent> quoteProdComponents = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName, type);
			if(quoteProdComponents != null && !(quoteProdComponents.isEmpty())) {
				for (QuoteProductComponent quoteProductComponent : quoteProdComponents) {
					quoteprodComp = quoteProductComponent;
					break;
				}
			}
			if (quoteprodComp != null) {

				List<QuoteProductComponentsAttributeValue> attrValue = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(),
								attributeName);
				if (attrValue != null && !(attrValue.isEmpty())) {
					QuoteProductComponentsAttributeValue attributeValue = attrValue.stream().findFirst().get();
					if (attributeValue != null) {

						if (StringUtils.isNotBlank(attributeValue.getAttributeValues())) {
							String[] BW = attributeValue.getAttributeValues().split(" ");
							LOGGER.info("BW port::: " + BW);
							return BW[0];
						}else{
							return "";
						}
					}else{
						return "";
					}

				} else{
					return "";
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			throw new TclCommonException(com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	private String getNewBandwidthPort(Integer linkId, String componentName, String attributeName, String type) {
		type = MACDConstants.LINK;
		LOGGER.info("port Comp Name and Attribute Name type id{}", componentName + attributeName + type + linkId);
		QuoteProductComponent quoteprodComp = null;
		List<QuoteProductComponent> quoteProdComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(linkId, componentName, type);
		if(quoteProdComponents != null && !(quoteProdComponents.isEmpty())) {
			for (QuoteProductComponent quoteProductComponent : quoteProdComponents) {
				quoteprodComp = quoteProductComponent;
				break;
			}
		}

		if (quoteprodComp != null) {

			LOGGER.info("QuoteProductComponent Object {},and component id{}", quoteprodComp, quoteprodComp.getId());
			List<QuoteProductComponentsAttributeValue> attrValue = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName);

			if (attrValue != null && !(attrValue.isEmpty())) {
				QuoteProductComponentsAttributeValue attributeValue = attrValue.stream().findFirst().get();
				if (attributeValue != null) {
					LOGGER.info("Attr Value {}", attributeValue.getAttributeValues());
					if (StringUtils.isNotBlank(attributeValue.getAttributeValues())) {
						String[] BW = attributeValue.getAttributeValues().split(" ");
						LOGGER.info("BW port::: " + BW);
						return BW[0];
					} else {
						return "";
					}
				} else {
					return "";
				}
			} else {
				return "";
			}
		}else {
			return "";
		}
	}

	private String getcityName(Integer siteId) throws TclCommonException
	{
		String city = "";
		Optional<QuoteIllSite> quoteIllSite = illSitesRepository.findById(siteId);
		LOGGER.info("MDC Filter token value in before Queue call createFeasReq {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteIllSite.get().getErfLocSitebLocationId()));
		String country = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils
					.convertJsonToObject(locationResponse, AddressDetail.class);
			city = addressDetail.getCity();
		}

		return  city;
	}

	@Transactional
	public void processCreateOptyChildOpty(QuoteToLe quoteToLe, String productName,String serviceId) throws TclCommonException {
		LOGGER.info("OmsSfdcService.processCreateOpty method invoked");
		// if (StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
			// Get the OrderLeAttributes
			LOGGER.info("SfdcOptyId  exists");
			OpportunityBean opportunityBean = new OpportunityBean();

			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Quote exists");
				String orderCode = quote.getQuoteCode();
				LOGGER.info("orderCode::" + orderCode);
				LOGGER.info("quote::" + quote.getId());
				opportunityBean
						.setDescription("Creating opportunity for the order " + quote.getId() + " on " + new Date());
				opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
				opportunityBean.setReferralToPartner(SFDCConstants.NO);
				opportunityBean.setType(SFDCConstants.TERMINATION);
				opportunityBean.setName(constructOpportunityName(quoteToLe,productName,opportunityBean.getType()));
				LOGGER.info("Opportunity name in processCreateOptyChildOpty() for quoteToLeId {}: {}",quoteToLe.getId(),opportunityBean.getName());
				//opportunityBean.setSubType(SFDCConstants.NEW_ORDER);
				opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
				String currency = SFDCConstants.INR;
				if (quoteToLe.getCurrencyCode() != null) {
					currency = quoteToLe.getCurrencyCode();
				}
				opportunityBean.setOpportunityClassification(SFDCConstants.SELL_TO);
				opportunityBean.setCurrencyIsoCode(currency);
				opportunityBean.setCustomerChurned(SFDCConstants.NO);
				opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
				opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
				opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
				if ("Partner".equalsIgnoreCase(userInfoUtils.getUserType())) {
					opportunityBean.setIsPartnerOrder(SFDCConstants.PARTNER);
					if(quoteToLe.getClassification()!=null) {
						opportunityBean.setOpportunityClassification(quoteToLe.getClassification());
					}
					opportunityBean.setOpportunityClassification(SFDCConstants.SELL_WITH_CLASSIFICATION);
				} else {
					opportunityBean.setIsPartnerOrder(SFDCConstants.OTHERS);
				}
				opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS + orderCode);
				opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
				if(quoteToLe.getTpsSfdcParentOptyId()!=null  && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
									&& !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				}
				opportunityBean.setCurrentCircuitServiceId(serviceId);
				LOGGER.info("Get customer details::" + quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
					setSfdcAccId(opportunityBean, customerDetails);
				} else {
					opportunityBean.setAccountId(accountId);
				}
				List<MstSfdcAmSaRegion> mstSfdcAmSaRegion=null;
				Optional<User> userSource = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
				String userType="";
				if (userSource.isPresent()) {
					userType=userSource.get().getUserType();
				}
				if (userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
					if (appEnv.equals(SFDCConstants.PROD)) {
						LOGGER.info("OPPORTUNITY USER EMAIL ID {}", userSource.get().getEmailId());
						opportunityBean.setOwnerName(userSource.get().getEmailId());
						mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(userSource.get().getEmailId());
						if(mstSfdcAmSaRegion!=null && !mstSfdcAmSaRegion.isEmpty()) {
							LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
						}
						else{
							LOGGER.info("MST SFDC Region is empty for "+userSource.get().getEmailId());
							opportunityBean.setSalesAdministratorRegion(null);
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				} else {
					if (appEnv.equals(SFDCConstants.PROD)) {
						String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
						String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
						opportunityBean.setOwnerName(name);
						try {
							String email = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
							mstSfdcAmSaRegion = mstSfdcAmSaRegionRepository.findByAmEmail(email);
							if (mstSfdcAmSaRegion != null && !mstSfdcAmSaRegion.isEmpty()) {
								LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
								opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							} else {
								LOGGER.info("MST SFDC Region is empty for " + email);
								opportunityBean.setSalesAdministratorRegion(null);
							}
						}
						catch (Exception e){
							LOGGER.info("MST SFDC Sales Region :: Email is not available");
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				}

				LOGGER.info("Product name for create opportunity method {} ", productName);
				if (productName != null) {
					IOmsSfdcInputHandler handler = factory.getInstance(productName);
					if (handler != null) {
						handler.getOpportunityBean(quoteToLe, opportunityBean);
					}
				}
				Byte isComplete=CommonConstants.BACTIVE;
				// parent opty id for termination
				if(quoteToLe!=null && MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()) && quoteToLe.getTpsSfdcParentOptyId()!=null) {
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				}

				LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunityBean.getType());
				LOGGER.info("opportunitybean"+opportunityBean.getType()+" "+opportunityBean.getSubType());
				if (SFDCConstants.TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
					opportunityBean.setType(SFDCConstants.TERMINATION);
					//opportunityBean.setSubType(SFDCConstants.SERVICE_REPLACEMENT);
					opportunityBean.setAutoCreatedTerminationOpportunity(CommonConstants.TRUE);
					//opportunityBean.setCurrentCircuitServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
					if(StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
						 isComplete=CommonConstants.BDEACTIVATE;
					}else {
						opportunityBean.setParentTerminationOpportunityName(quoteToLe.getTpsSfdcOptyId());
					}
					opportunityBean.setDummyParentTerminationOpportunity("false");
				}

				quoteToLeRepository.save(quoteToLe);
				persistSfdcServiceJob(quote.getQuoteCode(), sfdcCreateOpty, Utils.convertObjectToJson(opportunityBean),
						isComplete, CREATE_OPTY, getSequenceNumber(CREATE_OPTY),serviceId,CommonConstants.BDEACTIVATE);
			}
		// }
		LOGGER.info("OmsSfdcService.processCreateOpty method exited");
	}

	@Transactional
	public void processCreateOptyDummy(QuoteToLe quoteToLe, String productName,String serviceId,String parentOptyId) throws TclCommonException {
		LOGGER.info("OmsSfdcService.processCreateOptyDummy method invoked");
		if (StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
			// Get the OrderLeAttributes
			LOGGER.info("SfdcOptyId not exists");
			OpportunityBean opportunityBean = new OpportunityBean();

			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Quote exists");
				String orderCode = quote.getQuoteCode();
				LOGGER.info("orderCode:: {}" ,orderCode);
				LOGGER.info("quote:: {}" , quote.getId());
				opportunityBean.setName("Optimus Parent Opportunity -" + quote.getId());
				opportunityBean
						.setDescription("Creating parent opportunity for the order " + quote.getId() + " on " + new Date());
				opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
				opportunityBean.setReferralToPartner(SFDCConstants.NO);
				opportunityBean.setType(SFDCConstants.TERMINATION);
				opportunityBean.setSubType(SFDCConstants.SERVICE_REPLACEMENT);
				opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
				String currency = SFDCConstants.INR;
				if (quoteToLe.getCurrencyCode() != null) {
					currency = quoteToLe.getCurrencyCode();
				}
				opportunityBean.setOpportunityClassification(SFDCConstants.SELL_TO);
				opportunityBean.setCurrencyIsoCode(currency);
				opportunityBean.setCustomerChurned(SFDCConstants.NO);
				opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
				opportunityBean.setDummyParentTerminationOpportunity("true");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
				opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
				opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
				if ("Partner".equalsIgnoreCase(userInfoUtils.getUserType())) {
					opportunityBean.setIsPartnerOrder(SFDCConstants.PARTNER);
					if(quoteToLe.getClassification()!=null) {
						opportunityBean.setOpportunityClassification(quoteToLe.getClassification());
					}
					opportunityBean.setOpportunityClassification(SFDCConstants.SELL_WITH_CLASSIFICATION);
				} else {
					opportunityBean.setIsPartnerOrder(SFDCConstants.OTHERS);
				}
				opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS + orderCode);
				opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
				if(quoteToLe.getTpsSfdcParentOptyId()!=null  && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
									&& !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				}
				opportunityBean.setCurrentCircuitServiceId(serviceId);
				LOGGER.info("Get customer details:: {}" ,quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
					setSfdcAccId(opportunityBean, customerDetails);
				} else {
					opportunityBean.setAccountId(accountId);
				}

				List<MstSfdcAmSaRegion> mstSfdcAmSaRegion=null;
				Optional<User> userSource = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
				String userType="";
				if (userSource.isPresent()) {
					userType=userSource.get().getUserType();
				}
				if (userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
					if (appEnv.equals(SFDCConstants.PROD)) {
						LOGGER.info("OPPORTUNITY USER EMAIL ID {}", userSource.get().getEmailId());
						opportunityBean.setOwnerName(userSource.get().getEmailId());
						mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(userSource.get().getEmailId());
						if(mstSfdcAmSaRegion !=null && !mstSfdcAmSaRegion.isEmpty()) {
							LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
						}
						else{
							LOGGER.info("MST SFDC Region is empty for "+Utils.getSource());
							opportunityBean.setSalesAdministratorRegion(null);
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				} else {
					if (appEnv.equals(SFDCConstants.PROD)) {
						String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
						String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
						opportunityBean.setOwnerName(name);
						try {
							String email = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
							mstSfdcAmSaRegion = mstSfdcAmSaRegionRepository.findByAmEmail(email);
							if (mstSfdcAmSaRegion != null && !mstSfdcAmSaRegion.isEmpty()) {
								LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
								opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							} else {
								LOGGER.info("MST SFDC Region is empty for " + email);
								opportunityBean.setSalesAdministratorRegion(null);
							}
						}
						catch (Exception e){
							LOGGER.info("MST SFDC Sales Region :: Email is not available");
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				}

				LOGGER.info("Product name for create opportunity method {} ", productName);
				if (productName != null) {
					IOmsSfdcInputHandler handler = factory.getInstance(productName);
					if (handler != null) {
						handler.getOpportunityBean(quoteToLe, opportunityBean);
					}
				}
				opportunityBean.setParentOpportunityName(parentOptyId);
				LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunityBean.getType());
				LOGGER.info("opportunitybean {} {}",opportunityBean.getType(),opportunityBean.getSubType());
				quoteToLeRepository.save(quoteToLe);
				persistSfdcServiceJob(quote.getQuoteCode(), sfdcCreateOpty, Utils.convertObjectToJson(opportunityBean),
						CommonConstants.BACTIVE, CREATE_OPTY, getSequenceNumber(CREATE_OPTY),serviceId,CommonConstants.BDEACTIVATE);
			}
		}
		LOGGER.info("OmsSfdcService.processCreateOptyDummy method exited");
	}

	@Transactional
	public void processUpdateOptyDummy(QuoteToLe quoteToLe,String serviceId,String parentOptyId,Date cofSignedDate, String optyId, String stage) throws TclCommonException {
		LOGGER.info("Entering Process opty for stage --> {}  and quote to le --->  {}  ",stage,quoteToLe.getQuote().getQuoteCode());
		UpdateOpportunityStage updateOpportunityStage = new UpdateOpportunityStage();
		Integer seqId = null;
		if (stage.equals(SFDCConstants.VERBAL_AGREEMENT_STAGE)) {
			Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
			if (Objects.nonNull(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
					&& StringUtils.isNotBlank(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)))
				updateOpportunityStage
						.setBillingEntity(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY));
			else
				updateOpportunityStage.setBillingEntity("Tata Communications Limited");

			updateOpportunityStage.setBillingFrequency(attributeMapper.get(LeAttributesConstants.BILLING_FREQUENCY));
			updateOpportunityStage.setBillingMethod(attributeMapper.get(LeAttributesConstants.BILLING_METHOD));
			if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
				setPartnerAttributesInUpdateOpportunityStage(optyId, quoteToLe, updateOpportunityStage);
				setSfdcAccountCuid(updateOpportunityStage, attributeMapper);
			} else {
				updateOpportunityStage.setCustomerContractingId(accountCuid);
				updateOpportunityStage.setAccountCuid(accountCuid);
			}
			updateOpportunityStage.setPaymentTerm(
					attributeMapper.get(LeAttributesConstants.PAYMENT_TERM) + " from generation of Invoice");
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}

				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , updateOpportunityStage.getTermOfMonths());

			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("60");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) && !MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				if (isIpcQuote(quoteToLe)) {
					processUpdateProductForIpc(quoteToLe);
				} else if (!isGscQuote(quoteToLe)) {
					processUpdateProduct(quoteToLe);

				}
			}

			seqId = getSequenceNumber(SFDCConstants.VERBAL_AGREEMENT_STAGE);

			//Commented based on archana confirmation
			/*if(isUcaasQuote(quoteToLe)){
				updateOpportunityStage.setOpportunitySpecification(WebexConstants.OPPPORTUNITY_SPECIFICATION);
			}*/

			LOGGER.info("In process Update opportunity, preapprovedflag {}, credit check triggered {}, variation approved flag {}",
					quoteToLe.getPreapprovedOpportunityFlag(), quoteToLe.getCreditCheckTriggered(), quoteToLe.getVariationApprovedFlag());
			if((Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getPreapprovedOpportunityFlag())) || Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag()) &&
					((Objects.nonNull(quoteToLe.getCreditCheckTriggered())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getCreditCheckTriggered())) ||
					(Objects.nonNull(quoteToLe.getVariationApprovedFlag()) && CommonConstants.BACTIVE.equals(quoteToLe.getVariationApprovedFlag())))) {
				LOGGER.info("Inside credit check evaluation - verbal agreement");
				if(quoteToLe.getQuoteToLeProductFamilies().stream().anyMatch(family ->
						family.getMstProductFamily().getName().equals(CommonConstants.IAS)
							|| family.getMstProductFamily().getName().equals(CommonConstants.NPL)
								|| family.getMstProductFamily().getName().equals(CommonConstants.GVPN)
								|| family.getMstProductFamily().getName().equals(CommonConstants.NDE)
								|| family.getMstProductFamily().getName().equals(UCAAS)
									|| family.getMstProductFamily().getName().equals(CommonConstants.IPC))){
					processCreditCheck(optyId, quoteToLe, updateOpportunityStage, seqId);
				}
			}

			if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				LOGGER.info("inside Cancellation verbal agreement for quoteCode {} quote id {} ", quoteToLe.getQuote().getQuoteCode(), quoteToLe.getQuote().getId(), updateOpportunityStage.getCurrentCircuitServiceId());
				String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getMstProductFamily().getName();
				String[] nplProductName = {null};
				if(CommonConstants.NPL.equalsIgnoreCase(productName))
					nplProductName[0] = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering().getProductName();
				Set<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteToLe.getQuoteIllSiteToServices();
				quoteIllSiteToServiceList.stream().forEach(quoteSiteToService -> {
					if(MACDConstants.ABSORBED.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
						updateOpportunityStage.setCancellationCharges("0");
					} else if(MACDConstants.PASSED_ON.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
					if(CommonConstants.IAS.equalsIgnoreCase(productName) || CommonConstants.GVPN.equalsIgnoreCase(productName) || CommonConstants.IZOPC.equalsIgnoreCase(productName)) {
					LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
					updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
					} else if(CommonConstants.NPL.equalsIgnoreCase(productName) || CommonConstants.NDE.equalsIgnoreCase(productName)) {
						if (CommonConstants.NPL.equalsIgnoreCase(productName)
								&& "MMR Cross Connect".equalsIgnoreCase(nplProductName[0])) {
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
						} else {
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteNplLink().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteNplLink().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteNplLink().getNrc()) : "0");
					}
					}
					} else {
						updateOpportunityStage.setCancellationCharges("0");
					}
					updateOpportunityStage.setLeadTimeToRFSC("5");
				});


			}

			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Updating parent dummy opportunity for termination, optyid {}", quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setDummyParentTerminationOpportunity("true");
				updateOpportunityStage.setParentTerminationOpportunityName(quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setCurrentCircuitServiceId(serviceId);
			}
		} else if (stage.equals(SFDCConstants.CLOSED_WON_COF_RECI)) {

			//product service level update code for sfdc order type
			/*String familyName =getFamilyName(quoteToLe);
			if(Objects.nonNull(quoteToLe.getQuoteType())){
				if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
				{
					if(IAS.equalsIgnoreCase(familyName) || GVPN.equalsIgnoreCase(familyName))
					processUpdateProduct(quoteToLe);
				}
			}*/
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					 months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}
				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in proposal sent block is ----> {} " , updateOpportunityStage.getTermOfMonths());
			}
			String familyName = getFamilyName(quoteToLe);
			updateOpportunityStage.setProductType(familyName);
			updateOpportunityStage.setCofSignedDate(DateUtil.convertDateToString(cofSignedDate));
			//updateOpportunityStage.setSalesAdministratorRegion(SFDCConstants.INDIA_WHOLESALE);
			updateOpportunityStage.setDsPreparedBy(SFDCConstants.CUSTOMER_URGENCY);
			if(!MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				updateOpportunityStage.setSalesAdministrator("None");
			}
			updateOpportunityStage.setWinReason(SFDCConstants.WIN_REASONS);
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(new Date()));
			if(isUcaasQuote(quoteToLe)){
				updateOpportunityStage.setE2eCommentsC(SFDCConstants.OPTIMUS_OPPORTUNITY);
			}
			seqId = getSequenceNumber(SFDCConstants.CLOSED_WON_COF_RECI);

			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Updating parent dummy opportunity for termination, optyid {}", quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setDummyParentTerminationOpportunity("true");
				updateOpportunityStage.setParentTerminationOpportunityName(quoteToLe.getTpsSfdcOptyId());
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_IdAndIsDeletedIsNullOrIsDeleted(quoteToLe.getId(),CommonConstants.INACTIVE);
				updateOpportunityStage.setCurrentCircuitServiceId(quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
			}
		} else if (stage.equals(SFDCConstants.COF_WON_PROCESS_STAGE)) {
			String programManager = null;
			List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
					.findByNameAndIsActive(LeAttributesConstants.PROGRAM_MANAGER, CommonConstants.BACTIVE);
			for (MstOmsAttribute mstOmsAttribute : mstAttributes) {
				List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
				for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
					programManager = quoteLeAttributeValue.getAttributeValue();
				}
			}
			updateOpportunityStage.setProgramManager(programManager);
			seqId = getSequenceNumber(SFDCConstants.COF_WON_PROCESS_STAGE);
		} else if (stage.equals(SFDCConstants.PROPOSAL_SENT)) {
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					 months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}
				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in proposal sent block is ----> {} " , updateOpportunityStage.getTermOfMonths());
			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("60");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if (isGscQuote(quoteToLe)) {
				processUpdateProductForGSC(quoteToLe);
			} else if (quoteToLe.getQuote().getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
				processUpdateProductIzosdwan(quoteToLe);
				LOGGER.info("Trigger update product sfdc");
			} else if (isUcaasQuote(quoteToLe)) {
				processUpdateProductForUcaasAndGsc(quoteToLe);
			} else if(isIpcQuote(quoteToLe)){
				processUpdateProductForIpc(quoteToLe);
			}else {
				LOGGER.info("QUOTE ID IS :::: {}", quoteToLe.getId());
				processUpdateProduct(quoteToLe);
			}
			seqId = getSequenceNumber(SFDCConstants.PROPOSAL_SENT);

		}
		// SFDC UPDATE OPPORTUNITY for CLOSED_DROPPED - reasons will be changed in
		// future
		else if (stage.equals(SFDCConstants.CLOSED_DROPPED)) {
			String firstName = "";
			String lastName = "";
			String userName = Utils.getSource();
			LOGGER.info("userName :::: {}", userName);
			if (Objects.nonNull(userName)) {
				User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
				if (Objects.nonNull(user)) {
					if (Objects.nonNull(user.getFirstName()))
						firstName = user.getFirstName();
					if (Objects.nonNull(user.getLastName()))
						lastName = user.getLastName();
				}
				LOGGER.info("user :::: {}", user);
			}
			updateOpportunityStage.setWinLossDropKeyReason(SFDCConstants.WIN_LOSS_DROP_KEY_REASON);
			updateOpportunityStage.setDropReasons(SFDCConstants.DROP_REASONS);
			String droppingReason = SFDCConstants.DROPPING_REASON + "," + firstName + " " + lastName;
			updateOpportunityStage.setDroppingReason(droppingReason.trim());
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Updating parent dummy opportunity for termination, optyid {}", quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setDummyParentTerminationOpportunity("true");
			}
			seqId = getSequenceNumber(SFDCConstants.CLOSED_DROPPED);
			LOGGER.info("seqId :::: {}", seqId);
		} else if(stage.equals(SFDCConstants.IDENTIFIED_OPTY_STAGE)) {
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
			if (Objects.nonNull(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
					&& StringUtils.isNotBlank(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)))
				updateOpportunityStage
						.setBillingEntity(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY));
			else
				updateOpportunityStage.setBillingEntity("Tata Communications Limited");

			updateOpportunityStage.setBillingFrequency(attributeMapper.get(LeAttributesConstants.BILLING_FREQUENCY));
			updateOpportunityStage.setBillingMethod(attributeMapper.get(LeAttributesConstants.BILLING_METHOD));
			if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
				setPartnerAttributesInUpdateOpportunityStage(optyId, quoteToLe, updateOpportunityStage);
				setSfdcAccountCuid(updateOpportunityStage, attributeMapper);
			} else {
				updateOpportunityStage.setCustomerContractingId(accountCuid);
				updateOpportunityStage.setAccountCuid(accountCuid);
			}
			updateOpportunityStage.setPaymentTerm(
					attributeMapper.get(LeAttributesConstants.PAYMENT_TERM) + " from generation of Invoice");
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}

				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , updateOpportunityStage.getTermOfMonths());

			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("30");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) && !MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				if (isIpcQuote(quoteToLe)) {
					processUpdateProductForIpc(quoteToLe);
				} else if (!isGscQuote(quoteToLe)) {
					processUpdateProduct(quoteToLe);

				}
			}

			seqId = getSequenceNumber(SFDCConstants.IDENTIFIED_OPTY_STAGE);

			//Commented based on archana confirmation
			/*if(isUcaasQuote(quoteToLe)){
				updateOpportunityStage.setOpportunitySpecification(WebexConstants.OPPPORTUNITY_SPECIFICATION);
			}*/

			LOGGER.info("In process Update opportunity, preapprovedflag {}, credit check triggered {}, variation approved flag {}",
					quoteToLe.getPreapprovedOpportunityFlag(), quoteToLe.getCreditCheckTriggered(), quoteToLe.getVariationApprovedFlag());
			if((Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag())  
					&& CommonConstants.BACTIVE.equals(quoteToLe.getPreapprovedOpportunityFlag())) || Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag()) && 
					((Objects.nonNull(quoteToLe.getCreditCheckTriggered()) 
					&& CommonConstants.BACTIVE.equals(quoteToLe.getCreditCheckTriggered())) || 
					(Objects.nonNull(quoteToLe.getVariationApprovedFlag()) && CommonConstants.BACTIVE.equals(quoteToLe.getVariationApprovedFlag())))) {
				LOGGER.info("Inside credit check evaluation - verbal agreement");
				if(quoteToLe.getQuoteToLeProductFamilies().stream().anyMatch(family ->
						family.getMstProductFamily().getName().equals(CommonConstants.IAS)  
							|| family.getMstProductFamily().getName().equals(CommonConstants.NPL)
								|| family.getMstProductFamily().getName().equals(CommonConstants.GVPN) 
								|| family.getMstProductFamily().getName().equals(CommonConstants.NDE)
								|| family.getMstProductFamily().getName().equals(UCAAS)
									|| family.getMstProductFamily().getName().equals(CommonConstants.IPC))){
					processCreditCheck(optyId, quoteToLe, updateOpportunityStage, seqId);
				}
			}
			
			if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				LOGGER.info("inside Cancellation verbal agreement for quoteCode {} quote id {} ", quoteToLe.getQuote().getQuoteCode(), quoteToLe.getQuote().getId(), updateOpportunityStage.getCurrentCircuitServiceId());
				String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getMstProductFamily().getName();
				String[] nplProductName = {null};
				if(CommonConstants.NPL.equalsIgnoreCase(productName))
					nplProductName[0] = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering().getProductName();
				Set<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteToLe.getQuoteIllSiteToServices();
				quoteIllSiteToServiceList.stream().forEach(quoteSiteToService -> {
					if(MACDConstants.ABSORBED.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
						updateOpportunityStage.setCancellationCharges("0");	
					} else if(MACDConstants.PASSED_ON.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
					if(CommonConstants.IAS.equalsIgnoreCase(productName) || CommonConstants.GVPN.equalsIgnoreCase(productName) || CommonConstants.IZOPC.equalsIgnoreCase(productName)) {
					LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
					updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
					} else if(CommonConstants.NPL.equalsIgnoreCase(productName) || CommonConstants.NDE.equalsIgnoreCase(productName)) {
						if (CommonConstants.NPL.equalsIgnoreCase(productName)
								&& "MMR Cross Connect".equalsIgnoreCase(nplProductName[0])) { 
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
						} else {
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteNplLink().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteNplLink().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteNplLink().getNrc()) : "0");
					}
					}
					} else {
						updateOpportunityStage.setCancellationCharges("0");
					}
					updateOpportunityStage.setLeadTimeToRFSC("5");	
				});
				
				
			}
			
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Updating parent dummy opportunity for termination, optyid {}", quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setDummyParentTerminationOpportunity("true");
				updateOpportunityStage.setParentTerminationOpportunityName(quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setCurrentCircuitServiceId(serviceId);
			}
		}
		
		}

		updateOpportunityStage.setOpportunityId(optyId);
		updateOpportunityStage.setStageName(stage);
		String familyName = getFamilyName(quoteToLe);

		// introduced for IZOPC related changes since the status PROPOSAL_SENT won't be
		// logical in IZOPC
		if (familyName.equals("IZOPC")) {
			processUpdateProduct(quoteToLe);
		}

		if (familyName != null) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.getOpportunityUpdate(quoteToLe, updateOpportunityStage);
			}
		}

		if(familyName.equals("IZO SDWAN")) {
			updateBundleProducts(quoteToLe,updateOpportunityStage);
		}

		if(quoteToLe.getIsAmended()!=null && quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE)) {
			updateOpportunityStage.setType(SFDCConstants.AMENDMENT);
		}
		String request = Utils.convertObjectToJson(updateOpportunityStage);
		LOGGER.info("Input for updating the opty Status {}", request);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateOpty, request,
				StringUtils.isNotBlank(optyId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.UPDATE_OPTY, seqId,serviceId,CommonConstants.BDEACTIVATE);
	}

	@Transactional
	public void processCreatePartnerOpty(QuoteToLe quoteToLe, String productName,String partnerCuid,String endCustomerName) throws TclCommonException {
		LOGGER.info("OmsSfdcService.processCreateOpty method invoked");
		if (StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
			// Get the OrderLeAttributes
			LOGGER.info("SfdcOptyId not exists");
			OpportunityBean opportunityBean = new OpportunityBean();

			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Quote exists");
				String orderCode = quote.getQuoteCode();
				LOGGER.info("orderCode::" + orderCode);
				LOGGER.info("quote::" + quote.getId());
				opportunityBean
						.setDescription("Creating opportunity for the order " + quote.getId() + " on " + new Date());
				opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
				opportunityBean.setReferralToPartner(SFDCConstants.NO);
				opportunityBean.setType(SFDCConstants.NEW_ORDER);
				opportunityBean.setSubType(SFDCConstants.NEW_ORDER);
				opportunityBean.setName(constructOpportunityName(quoteToLe,productName,opportunityBean.getType()));
				LOGGER.info("Opportunity name in processCreatePartnerOpty() for quoteToLeId {}: {}",quoteToLe.getId(),opportunityBean.getName());
				opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
				String currency = SFDCConstants.INR;
				if (quoteToLe.getCurrencyCode() != null) {
					currency = quoteToLe.getCurrencyCode();
				}
				opportunityBean.setOpportunityClassification(SFDCConstants.SELL_TO);
				opportunityBean.setCurrencyIsoCode(currency);
				opportunityBean.setCustomerChurned(SFDCConstants.NO);
				opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
				opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
				opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
				if ("Partner".equalsIgnoreCase(userInfoUtils.getUserType())) {
					opportunityBean.setIsPartnerOrder(SFDCConstants.PARTNER);
					if(quoteToLe.getClassification()!=null) {
						opportunityBean.setOpportunityClassification(quoteToLe.getClassification());
					}
					else
					{
						opportunityBean.setOpportunityClassification(SFDCConstants.SELL_WITH_CLASSIFICATION);
					}
				} else {
					opportunityBean.setIsPartnerOrder(SFDCConstants.OTHERS);
				}
				opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS + orderCode);
				opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
				if(quoteToLe.getTpsSfdcParentOptyId()!=null  && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
						&& !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				}
				if(partnerCuid!=null&&quoteToLe.getClassification().equalsIgnoreCase(SFDCConstants.SELL_WITH_CLASSIFICATION)){ 
					opportunityBean.setPartnerContractingId(partnerCuid); 
					}
				opportunityBean.setEndCustomerName(endCustomerName);
				opportunityBean.setCurrentCircuitServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
				LOGGER.info("Get customer details::" + quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
                    setSfdcAccIdForPartner(opportunityBean, customerDetails);
				} else {
					opportunityBean.setAccountId(accountId);
				}

				List<MstSfdcAmSaRegion> mstSfdcAmSaRegion=null;
				Optional<User> userSource = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
				String userType="";
				if (userSource.isPresent()) {
					userType=userSource.get().getUserType();
				}
				if (userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
					if (appEnv.equals(SFDCConstants.PROD)) {
						LOGGER.info("OPPORTUNITY USER EMAIL ID {}",userSource.get().getEmailId());
						opportunityBean.setOwnerName(userSource.get().getEmailId());
						mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(userSource.get().getEmailId());
						if(mstSfdcAmSaRegion !=null && !mstSfdcAmSaRegion.isEmpty()) {
							LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
						}
						else{
							LOGGER.info("MST SFDC Region is empty");
							opportunityBean.setSalesAdministratorRegion(null);
						}
					} else {
						opportunityBean.setOwnerName(null);

					}
				} else {
					if (appEnv.equals(SFDCConstants.PROD)) {
						String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
						String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
						opportunityBean.setOwnerName(name);
						try {
							String email = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
							mstSfdcAmSaRegion = mstSfdcAmSaRegionRepository.findByAmEmail(email);
							if (mstSfdcAmSaRegion != null && !mstSfdcAmSaRegion.isEmpty()) {
								LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
								opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							} else {
								LOGGER.info("MST SFDC Region is empty for " + email);
								opportunityBean.setSalesAdministratorRegion(null);
							}
						}
						catch (Exception e){
							LOGGER.info("MST SFDC Sales Region :: Email is not available");
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				}

				LOGGER.info("Product name for create opportunity method {} ", productName);
				if (productName != null) {
					IOmsSfdcInputHandler handler = factory.getInstance(productName);
					if (handler != null) {
						handler.getOpportunityBean(quoteToLe, opportunityBean);
					}
				}

				LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunityBean.getType());
				LOGGER.info("opportunitybean"+opportunityBean.getType()+" "+opportunityBean.getSubType());

				/*
				 * if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
				 * {
				 *
				 * opportunityBean.setOwnerName(null); }
				 */
				quoteToLeRepository.save(quoteToLe);
				persistSfdcServiceJob(quote.getQuoteCode(), sfdcCreateOpty, Utils.convertObjectToJson(opportunityBean),
						CommonConstants.BACTIVE, CREATE_OPTY, getSequenceNumber(CREATE_OPTY),null,CommonConstants.BDEACTIVATE);
			}
		}
		LOGGER.info("OmsSfdcService.processCreateOpty method exited");
	}

	@Transactional
	public void processCreateOptyForAmendment(QuoteToLe quoteToLe, String productName, String oldOrderCode,
			List<String> orderSiteCodeFromReq) throws TclCommonException {
		LOGGER.info("OmsSfdcService.processCreateOptyForAmendment method invoked");
		if (StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
			// Get the OrderLeAttributes
			LOGGER.info("SfdcOptyId not exists");
			OpportunityBean opportunityBean = new OpportunityBean();

			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Quote exists");
				String orderCode = quote.getQuoteCode();
				LOGGER.info("orderCode::" + orderCode);
				LOGGER.info("quote::" + quote.getId());
				opportunityBean
						.setDescription("Creating opportunity for the order " + quote.getId() + " on " + new Date());
				opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
				opportunityBean.setOpportunityClassification(SFDCConstants.SELL_TO);
				opportunityBean.setReferralToPartner(SFDCConstants.NO);
				opportunityBean.setType(SFDCConstants.AMENDMENT);
				opportunityBean.setSubType(SFDCConstants.NEW_ORDER);
				opportunityBean.setName(constructOpportunityName(quoteToLe,productName,opportunityBean.getType()));
				LOGGER.info("Opportunity name in processCreateOptyForAmendment() for quoteToLeId {}: {}",quoteToLe.getId(),opportunityBean.getName());
				opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
				String currency = SFDCConstants.INR;
				if (quoteToLe.getCurrencyCode() != null) {
					currency = quoteToLe.getCurrencyCode();
				}
				opportunityBean.setCurrencyIsoCode(currency);
				opportunityBean.setCustomerChurned(SFDCConstants.NO);
				opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
				opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
				opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
				if ("Partner".equalsIgnoreCase(userInfoUtils.getUserType())) {
					opportunityBean.setIsPartnerOrder(SFDCConstants.PARTNER);
				} else {
					opportunityBean.setIsPartnerOrder(SFDCConstants.OTHERS);
				}
				opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS + orderCode);
				opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
				if (quoteToLe.getTpsSfdcParentOptyId() != null) {
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				}
				opportunityBean.setCurrentCircuitServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
				LOGGER.info("Get customer details::" + quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
					setSfdcAccId(opportunityBean, customerDetails);
				} else {
					opportunityBean.setAccountId(accountId);
				}

				LOGGER.info("Product name for create opportunity method {} ", productName);
				if (productName != null) {
					IOmsSfdcInputHandler handler = factory.getInstance(productName);
					if (handler != null) {
						handler.getOpportunityBean(quoteToLe, opportunityBean);
					}
				}

				LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunityBean.getType());
				Order orders = orderRepository.findByOrderCode(oldOrderCode);
				String oldSfdcId = null;
				for (OrderToLe orderToLe : orders.getOrderToLes()) {
					oldSfdcId = orderToLe.getTpsSfdcCopfId();
				}
				Double mrc = 0.0;
				Double nrc = 0.0;
				for (String siteCode : orderSiteCodeFromReq) {
					List<OrderIllSite> orderillSite = orderIllSitesRepository.findBySiteCodeAndStatus(siteCode,
							CommonConstants.BACTIVE);
					for (OrderIllSite illSite : orderillSite) {
						mrc = mrc + illSite.getMrc();
						nrc = nrc + illSite.getNrc();
					}
				}
				LOGGER.info("opportunitybean" + opportunityBean.getType() + " " + opportunityBean.getSubType());
				if (!MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
					opportunityBean.setParentOpportunityName(oldSfdcId);
					LocalDateTime now = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					String formatDateTime = now.format(formatter);
					opportunityBean.setCustomerMailReceivedDate(formatDateTime);
					opportunityBean.setPreviousMRC(String.valueOf(mrc));
					opportunityBean.setPreviousNRC(String.valueOf(nrc));
				}else {
					opportunityBean.setType(SFDCConstants.AMENDMENT);
					opportunityBean.setParentOpportunityName(oldSfdcId);
					opportunityBean.setPreviousMRC(String.valueOf(mrc));
					opportunityBean.setPreviousNRC(String.valueOf(nrc));
				}
				List<MstSfdcAmSaRegion> mstSfdcAmSaRegion=null;
				Optional<User> userSource = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
				String userType="";
				if (userSource.isPresent()) {
					userType=userSource.get().getUserType();
				}
				if (userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
					if (appEnv.equals(SFDCConstants.PROD)) {
						LOGGER.info("OPPORTUNITY USER EMAIL ID {}", userSource.get().getEmailId());
						opportunityBean.setOwnerName(userSource.get().getEmailId());
						mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(userSource.get().getEmailId());
						if(mstSfdcAmSaRegion!=null && !mstSfdcAmSaRegion.isEmpty()) {
							LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
						}
						else{
							LOGGER.info("MST SFDC Region is empty");
							opportunityBean.setSalesAdministratorRegion(null);
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				} else {
					if (appEnv.equals(SFDCConstants.PROD)) {
						String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
						String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
						opportunityBean.setOwnerName(name);
						try {
							String email = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
							mstSfdcAmSaRegion = mstSfdcAmSaRegionRepository.findByAmEmail(email);
							if (mstSfdcAmSaRegion != null && !mstSfdcAmSaRegion.isEmpty()) {
								LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
								opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							} else {
								LOGGER.info("MST SFDC Region is empty for " + email);
								opportunityBean.setSalesAdministratorRegion(null);
							}
						}
						catch (Exception e){
							LOGGER.info("MST SFDC Sales Region :: Email is not available");
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				}

				quoteToLeRepository.save(quoteToLe);
				persistSfdcServiceJob(quote.getQuoteCode(), sfdcCreateOpty, Utils.convertObjectToJson(opportunityBean),
						CommonConstants.BACTIVE, CREATE_OPTY, getSequenceNumber(CREATE_OPTY),null,CommonConstants.BDEACTIVATE);
			}
		}
		LOGGER.info("OmsSfdcService.processCreateOpty method exited");
	}
	
	
	public void processSFDCCancellation(String orderCode,List<String> orderSiteCodeFromReq) throws TclCommonException {
		List<ThirdPartyServiceJob> cancelJobs=new ArrayList<>();
        //Create Opty
        List<ThirdPartyServiceJob> createOpty=thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdDesc(orderCode, SFDCConstants.CREATE_OPTY, SFDCConstants.SFDC);
        for (ThirdPartyServiceJob thirdPartyServiceJob : createOpty) {
        	ThirdPartyServiceJob newThirdPartyJob=new ThirdPartyServiceJob();
        	BeanUtils.copyProperties(thirdPartyServiceJob, newThirdPartyJob);
        	newThirdPartyJob.setId(null);
        	newThirdPartyJob.setRetryCount(null);
        	newThirdPartyJob.setResponsePayload(null);
        	newThirdPartyJob.setRefId(newThirdPartyJob.getRefId()+"-C");
        	newThirdPartyJob.setServiceStatus("NEW");
        	newThirdPartyJob.setTpsId(null);
        	OpportunityBean opportunityBean=Utils.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(), OpportunityBean.class);
        	opportunityBean.setType("Cancellation");
        	opportunityBean.setSubType("Cancellation");
        	opportunityBean.setPortalTransactionId(opportunityBean.getPortalTransactionId()+"-C");
			setCloseDateForAmendment(opportunityBean);
			newThirdPartyJob.setRequestPayload(Utils.convertObjectToJson(opportunityBean));
        	cancelJobs.add(newThirdPartyJob);
        	break;
		}
        
        Double mrc=0.0;
        Double nrc=0.0;
        for (String siteCode : orderSiteCodeFromReq) {
        	List<OrderIllSite> orderillSite=orderIllSitesRepository.findBySiteCodeAndStatus(siteCode, CommonConstants.BACTIVE);
        	for (OrderIllSite illSite : orderillSite) {
        		mrc=mrc+illSite.getMrc();
        		nrc=nrc+illSite.getNrc();
			}
		}
        
        //UpdateProduct
    	Set<String> productStageIdentifier=new HashSet<>();
        List<ThirdPartyServiceJob> updateProduct=thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdDesc(orderCode, SFDCConstants.UPDATE_PRODUCT, SFDCConstants.SFDC);
		for (ThirdPartyServiceJob thirdPartyServiceJob : updateProduct) {
			ThirdPartyServiceJob newThirdPartyJob = new ThirdPartyServiceJob();
			BeanUtils.copyProperties(thirdPartyServiceJob, newThirdPartyJob);
			newThirdPartyJob.setId(null);
			newThirdPartyJob.setRetryCount(null);
			newThirdPartyJob.setResponsePayload(null);
			newThirdPartyJob.setRefId(newThirdPartyJob.getRefId() + "-C");
			newThirdPartyJob.setServiceStatus("NEW");
			newThirdPartyJob.setTpsId(null);
			newThirdPartyJob.setIsComplete(CommonConstants.BDEACTIVATE);
			ProductServiceBean productServiceBean = Utils.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(),
					ProductServiceBean.class);
			if(productStageIdentifier.contains(productServiceBean.getProductSolutionCode())) {
				continue;
			}else {
				productStageIdentifier.add(productServiceBean.getProductSolutionCode());
			}
			productServiceBean.setProductMRC(0.0 - mrc);
			productServiceBean.setProductNRC(0.0 - nrc);
			productServiceBean.setProductId(null);
			productServiceBean.setProductName(null);
			productServiceBean.setIsCancel(true);
			LOGGER.info("For Cancellation MRC is -----> {} NRC is ----> {} ", productServiceBean.getProductMRC(),
					productServiceBean.getProductNRC());
			newThirdPartyJob.setRequestPayload(Utils.convertObjectToJson(productServiceBean));
			cancelJobs.add(newThirdPartyJob);
		}
		
		 //Update Stage
		Set<String> stageIdentifier=new HashSet<>();
        List<ThirdPartyServiceJob> updateOpty=thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdDesc(orderCode, SFDCConstants.UPDATE_OPTY, SFDCConstants.SFDC);
		for (ThirdPartyServiceJob thirdPartyServiceJob : updateOpty) {
			ThirdPartyServiceJob newThirdPartyJob = new ThirdPartyServiceJob();
			BeanUtils.copyProperties(thirdPartyServiceJob, newThirdPartyJob);
			newThirdPartyJob.setId(null);
			newThirdPartyJob.setRetryCount(null);
			newThirdPartyJob.setResponsePayload(null);
			newThirdPartyJob.setRefId(newThirdPartyJob.getRefId() + "-C");
			newThirdPartyJob.setServiceStatus("NEW");
			newThirdPartyJob.setTpsId(null);
			newThirdPartyJob.setIsComplete(CommonConstants.BDEACTIVATE);
			UpdateOpportunityStage updateOptyStage = Utils.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(),
					UpdateOpportunityStage.class);
			if(stageIdentifier.contains(updateOptyStage.getStageName())) {
				continue;
			}else {
				stageIdentifier.add(updateOptyStage.getStageName());
			}
			newThirdPartyJob.setRequestPayload(Utils.convertObjectToJson(updateOptyStage));
			cancelJobs.add(newThirdPartyJob);
		}
        
		 //create Site
        List<ThirdPartyServiceJob> createSite=thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdDesc(orderCode, SFDCConstants.UPDATE_SITE, SFDCConstants.SFDC);
		for (ThirdPartyServiceJob thirdPartyServiceJob : createSite) {
			ThirdPartyServiceJob newThirdPartyJob = new ThirdPartyServiceJob();
			BeanUtils.copyProperties(thirdPartyServiceJob, newThirdPartyJob);
			newThirdPartyJob.setId(null);
			newThirdPartyJob.setRetryCount(null);
			newThirdPartyJob.setResponsePayload(null);
			newThirdPartyJob.setRefId(newThirdPartyJob.getRefId() + "-C");
			newThirdPartyJob.setServiceStatus("NEW");
			newThirdPartyJob.setTpsId(null);
			newThirdPartyJob.setIsComplete(CommonConstants.BDEACTIVATE);
			SiteSolutionOpportunityBean siteSolution = Utils.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(),
					SiteSolutionOpportunityBean.class);
			List<SiteOpportunityLocation> newSiteOpportunityLocations= new ArrayList<>();
			for (SiteOpportunityLocation siteSolutionLocations : siteSolution.getSiteOpportunityLocations()) {
				String siteLocationCode=siteSolutionLocations.getSiteLocationID().replace(SFDCConstants.OPTIMUS, CommonConstants.EMPTY);
				if(orderSiteCodeFromReq.contains(siteLocationCode)) {
					siteSolutionLocations.setSiteLocationID(siteSolutionLocations.getSiteLocationID()+"-C");
					newSiteOpportunityLocations.add(siteSolutionLocations);
				}
			}
			siteSolution.setIsCancel(true);
			siteSolution.setSiteOpportunityLocations(newSiteOpportunityLocations);
			siteSolution.setSourceSytemTransactionId(siteSolution.getSourceSytemTransactionId()+"-C");
			newThirdPartyJob.setRequestPayload(Utils.convertObjectToJson(siteSolution));
			cancelJobs.add(newThirdPartyJob);
		}
       thirdPartyServiceJobsRepository.saveAll(cancelJobs) ;

	}

	private void setCloseDateForAmendment(OpportunityBean opportunityBean) {
		LocalDateTime todaysDate = LocalDateTime.now();
		LocalDateTime closeDate = todaysDate.plusDays(10);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String closeDateString = closeDate.format(formatter);
		opportunityBean.setCloseDate(closeDateString);
		LOGGER.info("Close Date String is ----> {}  for Quote code ----> {} ",
				opportunityBean.getCloseDate(),opportunityBean.getPortalTransactionId());
	}

	@Transactional
	public void processCreateOptyForCancellation(QuoteToLe quoteToLe, String productName) throws TclCommonException {
		LOGGER.info("OmsSfdcService.processCreateOpty method invoked");
		if (StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
			// Get the OrderLeAttributes
			LOGGER.info("SfdcOptyId not exists");
			OpportunityBean opportunityBean = new OpportunityBean();

			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Quote exists");
				String orderCode = quote.getQuoteCode();
				LOGGER.info("orderCode::" + orderCode);
				LOGGER.info("quote::" + quote.getId());
				opportunityBean
						.setDescription("Creating opportunity for the order " + quote.getId() + " on " + new Date());
				opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
				opportunityBean.setOpportunityClassification(SFDCConstants.SELL_TO);
				opportunityBean.setReferralToPartner(SFDCConstants.NO);
				opportunityBean.setType(SFDCConstants.NEW_ORDER);
				opportunityBean.setSubType(SFDCConstants.NEW_ORDER);
				opportunityBean.setName(constructOpportunityName(quoteToLe,productName,opportunityBean.getType()));
				LOGGER.info("Opportunity name in processCreateOptyForAmendment() for quoteToLeId {}: {}",quoteToLe.getId(),opportunityBean.getName());
				opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
				String currency = SFDCConstants.INR;
				if (quoteToLe.getCurrencyCode() != null) {
					currency = quoteToLe.getCurrencyCode();
				}
				opportunityBean.setCurrencyIsoCode(currency);
				opportunityBean.setCustomerChurned(SFDCConstants.NO);
				opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
				opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
				opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
				opportunityBean.setIsPartnerOrder(SFDCConstants.OTHERS);
				opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS + orderCode+"-C");
				opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
				if(quoteToLe.getTpsSfdcParentOptyId()!=null) {
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				}
				opportunityBean.setCurrentCircuitServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
				LOGGER.info("Get customer details::" + quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
					setSfdcAccId(opportunityBean, customerDetails);
				} else {
					opportunityBean.setAccountId(accountId);
				}

				LOGGER.info("Product name for create opportunity method {} ", productName);
				if (productName != null) {
					IOmsSfdcInputHandler handler = factory.getInstance(productName);
					if (handler != null) {
						handler.getOpportunityBean(quoteToLe, opportunityBean);
					}
				}

					LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunityBean.getType());

				LOGGER.info("opportunitybean"+opportunityBean.getType()+" "+opportunityBean.getSubType());
				List<MstSfdcAmSaRegion> mstSfdcAmSaRegion=null;
				Optional<User> userSource = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
				String userType="";
				if (userSource.isPresent()) {
					userType=userSource.get().getUserType();
				}
				if (userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
					if (appEnv.equals(SFDCConstants.PROD)) {
						LOGGER.info("OPPORTUNITY USER EMAIL ID {}", userSource.get().getEmailId());
						opportunityBean.setOwnerName(userSource.get().getEmailId());
						mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(userSource.get().getEmailId());
						if(mstSfdcAmSaRegion!=null && !mstSfdcAmSaRegion.isEmpty()) {
							LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
						}
						else{
							LOGGER.info("MST SFDC Region is empty");
							opportunityBean.setSalesAdministratorRegion(null);
						}
					} else {
						opportunityBean.setOwnerName(null);
					}
				} else {

					String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
					String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
					opportunityBean.setOwnerName(name);
					try {
						String email = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
						mstSfdcAmSaRegion = mstSfdcAmSaRegionRepository.findByAmEmail(email);
						if (mstSfdcAmSaRegion != null && !mstSfdcAmSaRegion.isEmpty()) {
							LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
							opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
						} else {
							LOGGER.info("MST SFDC Region is empty for " + email);
							opportunityBean.setSalesAdministratorRegion(null);
						}
					}
					catch (Exception e){
						LOGGER.info("MST SFDC Sales Region :: Email is not available");
					}
				}
				
				if (Objects.nonNull(quoteToLe.getIsAmended())) {
					int result = Byte.compare(quoteToLe.getIsAmended(), BACTIVE);
					if (result == 0) {
						opportunityBean.setType("Cancellation");
						opportunityBean.setSubType("Cancellation");
					}
				}



				 
				quoteToLeRepository.save(quoteToLe);
				persistSfdcServiceJob(quote.getQuoteCode()+"-C", sfdcCreateOpty, Utils.convertObjectToJson(opportunityBean),
						CommonConstants.BACTIVE, CREATE_OPTY, getSequenceNumber(CREATE_OPTY),null,CommonConstants.BDEACTIVATE);
			}
		}
		LOGGER.info("OmsSfdcService.processCreateOpty method exited");
	}

	/**
	 * processCreateFeasibility
	 *
	 * @param quoteToLe
	 * @param siteId
	 * @throws TclCommonException
	 */
	@Transactional
	public void createFeasibility(QuoteToLe quoteToLe, Integer siteId) throws TclCommonException {
		try {
			LOGGER.info("Create feasibility in OmsSfdcService");
			FeasibilityRequestBean feasibilityBean = new FeasibilityRequestBean();
			feasibilityBean.setCreateOrUpdate("create");
			feasibilityBean.setSiteId(siteId);
			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Inside create feasibilty sfdc: ", siteId);

				String productName = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId()).get(0)
						.getMstProductFamily().getName();
				feasibilityBean.setProductName(productName);
				if (productName.equalsIgnoreCase("NPL")|| productName.equalsIgnoreCase("NDE")) {
					Optional<QuoteNplLink> linkOpt = nplLinkRepository.findById(siteId);

					if (linkOpt.isPresent()) {
						Optional<ProductSolution> productSolutionOpt = productSolutionRepository
								.findById(linkOpt.get().getProductSolutionId());
						if (productSolutionOpt.isPresent()) {
							ProductSolution productSolution = productSolutionOpt.get();
							feasibilityBean.setProductsServices(productSolution.getTpsSfdcProductName());
							IOmsSfdcInputHandler handler = factory.getInstance(productName);
							if (handler != null) {

								handler.getFeasibilityBean(siteId, feasibilityBean);

							}
						}
					}

				} else {
					Optional<QuoteIllSite> illSiteOpt = illSiteRepository.findById(siteId);

					try {
						if (illSiteOpt.isPresent()) {

							QuoteIllSite quoteIllSite = illSiteOpt.get();

							ProductSolution productSolution = quoteIllSite.getProductSolution();
							IOmsSfdcInputHandler handler = factory.getInstance(productName);
							LOGGER.info("Product Name: "+ productName);
							if (handler != null) {

								handler.getFeasibilityBean(quoteIllSite.getId(), feasibilityBean);
								LOGGER.info("MDC Filter token value in before Queue call createFeasReq {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
										String.valueOf(quoteIllSite.getErfLocSitebLocationId()));
								String country = StringUtils.EMPTY;
								if (StringUtils.isNotBlank(locationResponse)) {
									AddressDetail addressDetail = (AddressDetail) Utils
											.convertJsonToObject(locationResponse, AddressDetail.class);
									feasibilityBean.setCityAEndC(addressDetail.getCity());
									feasibilityBean.setCountryAEndC(addressDetail.getCountry().equalsIgnoreCase(SFDCConstants.UNITED_STATES_OF_AMERICA) ? SFDCConstants.UNITED_STATES : addressDetail.getCountry());
									country = addressDetail.getCountry();
									feasibilityBean.setStateAEndC(addressDetail.getState());
									feasibilityBean.setAddressAEndC(addressDetail.getAddressLineOne());
									feasibilityBean.setPinZipAEndC(addressDetail.getPincode());
									feasibilityBean.setAddressLine1AEndC(addressDetail.getAddressLineOne());
									feasibilityBean.setAddressLine2AEndC(StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + " " + StringUtils.trimToEmpty(addressDetail.getLocality()));
								}

								String providersName = siteFeasibilityRepository
										.findByQuoteIllSite_IdAndType(quoteIllSite.getId(), "primary").stream()
										.findFirst()
										// .min(Comparator.comparing(SiteFeasibility::getRank))
										.map(SiteFeasibility::getProvider).orElse(SFDCConstants.PROVIDER_NAME);
								if(Objects.isNull(providersName) ||  providersName.isEmpty())
									providersName=SFDCConstants.PROVIDER_NAME;
								feasibilityBean.setAvailableTelecomPRIProviderAEndC(providersName);
								//feasibilityBean.setContinentAEndC("Asia");
								feasibilityBean.setContinentAEndC(setContinentValue(country));
								feasibilityBean.setProductsServices(productSolution.getTpsSfdcProductName());
								feasibilityBean.setStatus(SFDCConstants.STATUS_ASSIGNED);
								feasibilityBean.setRequestTypeC(SFDCConstants.LM_FIRM_QUOTE);
								feasibilityBean.setRecordTypeName(SFDCConstants.DEFAULT_FEASIBILITY_REQUEST);
								feasibilityBean.setSpecialRequirementsC(SFDCConstants.NOT_APPLICABLE);

							}
						}
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.ERROR_PROCESSING_OMS_SFDC_FEAS_REQ, e);
					}

				}

				String request = Utils.convertObjectToJson(feasibilityBean);
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcCreateFeasReq, request,
						CommonConstants.BDEACTIVATE, SFDCConstants.CREATE_FEASIBILITY,
						getSequenceNumber(SFDCConstants.CREATE_FEASIBILITY),null,CommonConstants.BDEACTIVATE);

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ERROR_PROCESSING_OMS_SFDC_FEAS_REQ, e);
		}

	}

	/**
	 * method to update feasibility request to sfdc
	 *
	 * @param quoteToLe
	 * @param siteId
	 * @throws TclCommonException
	 */
	@Transactional
	public void updateFeasibility(QuoteToLe quoteToLe, Integer siteId) throws TclCommonException {
		try {

			LOGGER.info("Update feasibility in OmsSfdcService ");
			FeasibilityRequestBean feasibilityBean = new FeasibilityRequestBean();
			feasibilityBean.setCreateOrUpdate("update");
			feasibilityBean.setSiteId(siteId);
			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Inside update feasibilty sfdc: ", siteId);

				String productName = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId()).get(0)
						.getMstProductFamily().getName();
				feasibilityBean.setProductName(productName);
				if (productName.equalsIgnoreCase("NPL")|| productName.equalsIgnoreCase("NDE")) {
					Optional<QuoteNplLink> linkOpt = nplLinkRepository.findById(siteId);

					if (linkOpt.isPresent()) {
						Optional<ProductSolution> productSolutionOpt = productSolutionRepository
								.findById(linkOpt.get().getProductSolutionId());
						if (productSolutionOpt.isPresent()) {
							ProductSolution productSolution = productSolutionOpt.get();
							feasibilityBean.setProductsServices(productSolution.getTpsSfdcProductName());
							IOmsSfdcInputHandler handler = factory.getInstance(productName);
							if (handler != null) {
								handler.getFeasibilityBean(siteId, feasibilityBean);
							}
						}
					}

				} else {
					Optional<QuoteIllSite> illSiteOpt = illSiteRepository.findById(siteId);

					try {
						if (illSiteOpt.isPresent()) {

							QuoteIllSite quoteIllSite = illSiteOpt.get();

							ProductSolution productSolution = quoteIllSite.getProductSolution();
							IOmsSfdcInputHandler handler = factory.getInstance(productName);
							if (handler != null) {

								handler.getFeasibilityBean(quoteIllSite.getId(), feasibilityBean);
								LOGGER.info("MDC Filter token value in before Queue call updateFeasReq {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
										String.valueOf(quoteIllSite.getErfLocSitebLocationId()));
								AddressDetail addressDetail = (AddressDetail) Utils
										.convertJsonToObject(locationResponse, AddressDetail.class);
								feasibilityBean.setCityAEndC(addressDetail.getCity());
								feasibilityBean.setCountryAEndC(addressDetail.getCountry().equalsIgnoreCase(SFDCConstants.UNITED_STATES_OF_AMERICA) ? SFDCConstants.UNITED_STATES : addressDetail.getCountry());
								feasibilityBean.setStateAEndC(addressDetail.getState());
								feasibilityBean.setAddressAEndC(addressDetail.getAddressLineOne());
								feasibilityBean.setPinZipAEndC(addressDetail.getPincode());
								feasibilityBean.setAddressLine1AEndC(addressDetail.getAddressLineOne());
								feasibilityBean.setAddressLine2AEndC(StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + " " + StringUtils.trimToEmpty(addressDetail.getLocality()));

								String providersName = siteFeasibilityRepository
										.findByQuoteIllSite_IdAndType(quoteIllSite.getId(), "primary").stream()
										.findFirst()
										// .min(Comparator.comparing(SiteFeasibility::getRank))
										.map(SiteFeasibility::getProvider).orElse(SFDCConstants.PROVIDER_NAME);
								if(Objects.isNull(providersName) ||  providersName.isEmpty())
									providersName=SFDCConstants.PROVIDER_NAME;
								feasibilityBean.setAvailableTelecomPRIProviderAEndC(providersName);
								//feasibilityBean.setContinentAEndC("Asia");
								feasibilityBean.setContinentAEndC(setContinentValue(addressDetail.getCountry()));
								feasibilityBean.setProductsServices(productSolution.getTpsSfdcProductName());
								feasibilityBean.setStatus(SFDCConstants.STATUS_ASSIGNED);
								feasibilityBean.setRequestTypeC(SFDCConstants.LM_FIRM_QUOTE);
								feasibilityBean.setRecordTypeName(SFDCConstants.DEFAULT_FEASIBILITY_REQUEST);
								feasibilityBean.setSpecialRequirementsC(SFDCConstants.NOT_APPLICABLE);

							}

							List<QuoteProductComponent> components = quoteProductComponentRepository
									.findByReferenceIdAndMstProductComponent_Name(siteId,
											IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
							List<QuoteProductComponentsAttributeValue> lconNameValue = null;
							List<QuoteProductComponentsAttributeValue> lconNumberValue = null;

							Optional<QuoteProductComponent> componentOptional = components.stream().findFirst();
							if (componentOptional.isPresent()) {
								lconNameValue = quoteProductComponentsAttributeValueRepository
										.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
												componentOptional.get().getId(),
												AttributeConstants.LCON_NAME.toString());
								lconNumberValue = quoteProductComponentsAttributeValueRepository
										.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
												componentOptional.get().getId(),
												AttributeConstants.LCON_NUMBER.toString());
							}

							if (lconNameValue != null && !lconNameValue.isEmpty())
								feasibilityBean.setSiteContactNameAEndC(lconNameValue.get(0).getAttributeValues());
							if (lconNumberValue != null && !lconNumberValue.isEmpty())
								feasibilityBean
										.setSiteLocalContactNumberAEndC(lconNumberValue.get(0).getAttributeValues());

						}
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.ERROR_PROCESSING_OMS_SFDC_FEAS_REQ, e);
					}

				}

				String request = Utils.convertObjectToJson(feasibilityBean);
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcCreateFeasReq, request,
						CommonConstants.BDEACTIVATE, SFDCConstants.UPDATE_FEASIBILITY,
						getSequenceNumber(SFDCConstants.UPDATE_FEASIBILITY),null,CommonConstants.BDEACTIVATE);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ERROR_PROCESSING_OMS_SFDC_FEAS_REQ, e);
		}

	}

	/**
	 * Create Partner Opportunity
	 *
	 * @param customerLeId
	 * @param opportunity
	 * @param endCustomerCuid
	 * @throws TclCommonException
	 */
	@Transactional
	public void processCreateOptyForPartner(Opportunity opportunity, String productName, String partnerId, Integer customerId, String endCustomerCuid) throws TclCommonException {
		LOGGER.info("Entering processCreateOptyForPartner method with " +
				"opportunity_id  ::: {}   productName  :::  {}  partnerId  :::  {}  ", opportunity.getId(), productName, partnerId);
		OpportunityBean opportunityBean = new OpportunityBean();
		opportunityBean
				.setDescription("Creating opportunity for the order " + opportunity.getId() + " on " + new Date());
		opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
		opportunityBean.setOpportunityClassification(opportunity.getOptyClassification());
		opportunityBean.setReferralToPartner(SFDCConstants.NO);
		opportunityBean.setSubType(SFDCConstants.NEW_ORDER);
		opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
		if(opportunity.getExpectedCurrency()!=null){
			opportunityBean.setCurrencyIsoCode(opportunity.getExpectedCurrency());
		}
		else {
			opportunityBean.setCurrencyIsoCode(opportunity.getCurrencyIsoCode());
		}
		opportunityBean.setCustomerChurned(SFDCConstants.NO);
		opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
		LOGGER.info("CurrencyIsoCode is :::  {}  and  OpportunityClassification is  ::: {} and  PortalTransactionId is ::: {} ",
				opportunity.getCurrencyIsoCode(), opportunity.getOptyClassification(), opportunity.getUuid());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 30);
		opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
		opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
		opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
		opportunityBean.setIsPartnerOrder(SFDCConstants.PARTNER);
		opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS + opportunity.getUuid());
		opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
		opportunityBean.setTermOfMonths("12");

		if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
			setPartnerAttributesInCreateOpportunityStage(opportunity, opportunityBean);

		} else {
			opportunityBean.setAccountId(accountId);
		}

		LOGGER.info("Product name for create opportunity method {} ", productName);
		if (productName != null) {
			opportunityBean = setProductName(opportunityBean, productName);
		}

		if (Objects.nonNull(opportunity.getTpsOptyId())) {
			opportunityBean.setTpsOptyId(opportunity.getTpsOptyId());
		}
		if (Objects.nonNull(opportunity.getCampaignName())) {
			opportunityBean.setCampaignName(opportunity.getCampaignName());
		}
		if (Objects.nonNull(opportunity.getOptySummary())) {
			opportunityBean.setDescription(opportunity.getOptySummary());
		}

		opportunityBean = setPsamOwnerName(opportunityBean, opportunity, customerId);
		String sfdcCreateOptyQueueName = null;
		sfdcCreateOptyQueueName = sfdcPartnerCreateOpty;
		if(opportunity.getDealRegistrationDate()!=null&&!opportunity.getDealRegistrationDate().equals("Invalid date")){
			opportunityBean.setDealRegistrationDate(getDealRegistrationDate(opportunity.getDealRegistrationDate()));
		}
		else {
			opportunityBean.setDealRegistrationDate(opportunityBean.getCloseDate());
		}
		opportunityBean.setPartnerOptyMRC(String.valueOf(opportunity.getExpectedMrc()));
		opportunityBean.setPartnerOptyNRC(String.valueOf(opportunity.getExpectedNrc()));
		opportunityBean.setCampaignId(opportunity.getCampaignId());
		opportunityBean.setEndCustomerCuid(endCustomerCuid);
		if (YES.equalsIgnoreCase(opportunity.getIsDealRegistration())) {
			opportunityBean.setDealRegistrationRequired(YES);;
			LOGGER.info("DealRegistrationDate is :::: {}  and  ExpectedMrc is :::: {} and PsamEmail is ::: {} ",
					opportunityBean.getDealRegistrationDate(), opportunityBean.getPartnerOptyMRC(), opportunityBean.getPsamEmail());
		} else {
			opportunityBean.setDealRegistrationRequired(NO);
		}
		if(Objects.nonNull(opportunity.getPsamEmailId())&&!opportunity.getPsamEmailId().isEmpty()){
			opportunityBean.setPsamEmail(opportunity.getPsamEmailId());
		}
		LOGGER.info("Owner Name :: {} and Email :: {} and Psam Email :: {}", opportunityBean.getOwnerName(), opportunityBean.getOwnerEmail(),
				opportunityBean.getPsamEmail());
		persistSfdcServiceJob(opportunity.getUuid(), sfdcCreateOptyQueueName, Utils.convertObjectToJson(opportunityBean),
				CommonConstants.BACTIVE, CREATE_OPTY, getSequenceNumber(CREATE_OPTY),null,CommonConstants.BDEACTIVATE);
	}

	@Transactional
	public void processCreateOrderNaLiteOptyForPartner(Opportunity opportunity, String productName, String partnerId, Integer customerId) throws TclCommonException {
		LOGGER.info("Entering processCreateOptyForPartner method with " +
				"opportunity_id  ::: {}   productName  :::  {}  partnerId  :::  {}  ", opportunity.getId(), productName, partnerId);
		OpportunityBean opportunityBean = new OpportunityBean();
		opportunityBean.setName("Optimus Opportunity - " + opportunity.getId());
		opportunityBean
				.setDescription("Creating opportunity for the order " + opportunity.getId() + " on " + new Date());
		opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
		opportunityBean.setOpportunityClassification(opportunity.getOptyClassification());
		opportunityBean.setReferralToPartner(SFDCConstants.NO);
		opportunityBean.setSubType(SFDCConstants.NEW_ORDER);
		opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
		if(opportunity.getExpectedCurrency()!=null){
			opportunityBean.setCurrencyIsoCode(opportunity.getExpectedCurrency());
		}
		else {
			opportunityBean.setCurrencyIsoCode(opportunity.getCurrencyIsoCode());
		}
		opportunityBean.setCustomerChurned(SFDCConstants.NO);
		opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
		LOGGER.info("CurrencyIsoCode is :::  {}  and  OpportunityClassification is  ::: {} and  PortalTransactionId is ::: {} ",
				opportunity.getCurrencyIsoCode(), opportunity.getOptyClassification(), opportunity.getUuid());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 30);
		opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
		opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
		opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
		opportunityBean.setIsPartnerOrder(SFDCConstants.PARTNER);
		opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS+SFDCConstants.ORDERLITE+ opportunity.getUuid());
		opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
		opportunityBean.setTermOfMonths("12");

		if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
			setPartnerAttributesInCreateOpportunityStage(opportunity, opportunityBean);

		} else {
			opportunityBean.setAccountId(accountId);
		}

		LOGGER.info("Product name for create opportunity method {} ", productName);
		if (productName != null) {
			opportunityBean = setOrderLiteProductName(opportunityBean, productName);
		}

		if (Objects.nonNull(opportunity.getTpsOptyId())) {
			opportunityBean.setTpsOptyId(opportunity.getTpsOptyId());
		}
		if (Objects.nonNull(opportunity.getCampaignName())) {
			opportunityBean.setCampaignName(opportunity.getCampaignName());
		}
		if (Objects.nonNull(opportunity.getOptySummary())) {
			opportunityBean.setDescription(opportunity.getOptySummary());
		}

		opportunityBean = setPsamOwnerName(opportunityBean, opportunity,customerId);
		String sfdcCreateOptyQueueName = null;
		sfdcCreateOptyQueueName = sfdcPartnerCreateOpty;
		if(opportunity.getDealRegistrationDate()!=null&&!opportunity.getDealRegistrationDate().equals("Invalid date")){
			opportunityBean.setDealRegistrationDate(getDealRegistrationDate(opportunity.getDealRegistrationDate()));
		}
		else {
			opportunityBean.setDealRegistrationDate(opportunityBean.getCloseDate());
		}
		opportunityBean.setPartnerOptyMRC(String.valueOf(opportunity.getExpectedMrc()));
		opportunityBean.setPartnerOptyNRC(String.valueOf(opportunity.getExpectedNrc()));
		opportunityBean.setCampaignId(opportunity.getCampaignId());
		if (YES.equalsIgnoreCase(opportunity.getIsDealRegistration())) {
			opportunityBean.setDealRegistrationRequired(YES);;
			LOGGER.info("DealRegistrationDate is :::: {}  and  ExpectedMrc is :::: {} and PsamEmail is ::: {} ",
					opportunityBean.getDealRegistrationDate(), opportunityBean.getPartnerOptyMRC(), opportunityBean.getPsamEmail());
		} else {
			opportunityBean.setDealRegistrationRequired(NO);
		}
		if(Objects.nonNull(opportunity.getPsamEmailId())&&!opportunity.getPsamEmailId().isEmpty()){
			opportunityBean.setPsamEmail(opportunity.getPsamEmailId());
		}
		LOGGER.info("Owner Name :: {} and Email :: {} and Psam Email :: {}", opportunityBean.getOwnerName(), opportunityBean.getOwnerEmail(),
				opportunityBean.getPsamEmail());
		persistSfdcServiceJob(opportunity.getUuid(), sfdcCreateOptyQueueName, Utils.convertObjectToJson(opportunityBean),
				CommonConstants.BACTIVE, CREATE_OPTY, getSequenceNumber(CREATE_OPTY),null,CommonConstants.BDEACTIVATE);
	}

	private String getPsamEmail(String partnerId) throws TclCommonException {
		String ownerNameAndEmail = (String) mqUtils.sendAndReceive(partnerAccountNameMQ, partnerId);
		return ownerNameAndEmail.split(COMMA)[1].trim();
	}

	private String getDealRegistrationDate(String date) {
		String dateValue = null;
		try {
			dateValue = yearlyFormatter.format(slashFormatter.parse(date));
		} catch (Exception ex) {
			LOGGER.error("Error in parsing date getDealRegistrationDate {}", ex.getMessage());
		}
		return dateValue;
	}

	private OpportunityBean setProductName(OpportunityBean opportunityBean, String productName) {
		switch (productName) {
			case "GVPN":
				opportunityBean.setSelectProductType(SFDCConstants.GVPN);
				break;
			case "GSIP":
				opportunityBean.setSelectProductType(SFDCConstants.GSC);
				break;
			case "IAS":
				opportunityBean.setSelectProductType(SFDCConstants.ILL);
				break;
			case "NPL":
				opportunityBean.setSelectProductType(SFDCConstants.NPL);
				break;
			case "NDE":
				opportunityBean.setSelectProductType(SFDCConstants.NDE);
				break;
			case "IPC":
				opportunityBean.setSelectProductType(SFDCConstants.IZOPRIVATECLOUD);
				break;
		}
		return opportunityBean;
	}

	private OpportunityBean setOrderLiteProductName(OpportunityBean opportunityBean, String productName) {
		opportunityBean.setSelectProductType(productName);
		return opportunityBean;
	}

	private OpportunityBean setOwnerName(OpportunityBean opportunityBean, Opportunity opportunity, Integer erfCustomerId)
			throws TclCommonException {
		List<MstSfdcAmSaRegion> mstSfdcAmSaRegion=null;
		LOGGER.info("Classification :: {} and erfCustomerId :: {}", opportunity.getOptyClassification(), erfCustomerId);
		if (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(opportunity.getOptyClassification())) {
			if(checkPartnerRTM(erfCustomerId)) {
				LOGGER.info("Inside Partner RTM");
				LOGGER.info("RTM Owner Name :: {}", userInfoUtils.getUserFullName());
				LOGGER.info("RTM Owner Email :: {}", Utils.getSource());
				opportunityBean.setOwnerName(userInfoUtils.getUserFullName());
				opportunityBean.setOwnerEmail(Utils.getSource());
				mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(Utils.getSource());
				if(mstSfdcAmSaRegion !=null && !mstSfdcAmSaRegion.isEmpty()) {
					LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
					opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
				}
				else{
					LOGGER.info("MST SFDC Region is empty");
					opportunityBean.setSalesAdministratorRegion(null);
				}
			} else {
				LOGGER.info("Outside Partner RTM");
				String partnerId = String
						.valueOf(userInfoUtils.getPartnerDetails().stream().findFirst().get().getErfPartnerId());
				String ownerNameAndEmail = (String) mqUtils.sendAndReceive(partnerAccountNameMQ, partnerId);
				LOGGER.info("Partner ID :: {}", partnerId);
				LOGGER.info("Partner Owner Name and Email:: {}", ownerNameAndEmail);
				opportunityBean.setOwnerName(ownerNameAndEmail.split(COMMA)[0].trim());
				opportunityBean.setOwnerEmail(ownerNameAndEmail.split(COMMA)[1].trim());
				mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(ownerNameAndEmail.split(COMMA)[1].trim());
				if(mstSfdcAmSaRegion !=null && !mstSfdcAmSaRegion.isEmpty()) {
					LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
					opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
				}
				else{
					LOGGER.info("MST SFDC Region is empty");
					opportunityBean.setSalesAdministratorRegion(null);
				}
			}
		} else {
			String customerId = String.valueOf(userInfoUtils.getCustomerDetails().stream().findFirst().get().getErfCustomerId());
			String ownerName = (String) mqUtils.sendAndReceive(customerAccountManagerName, customerId);
			String ownerEmail = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, customerId);
			LOGGER.info("Customer ID :: {}", customerId);
			LOGGER.info("Customer Owner Name :: {} and Email:: {}", ownerName, ownerEmail);
			opportunityBean.setOwnerName(ownerName.trim());
			opportunityBean.setOwnerEmail(ownerEmail.trim());
			mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(ownerEmail.trim());
			if(mstSfdcAmSaRegion !=null && !mstSfdcAmSaRegion.isEmpty()) {
				LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
				opportunityBean.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
			}
			else{
				LOGGER.info("MST SFDC Region is empty");
				opportunityBean.setSalesAdministratorRegion(null);
			}
		}
		return opportunityBean;
	}

	private OpportunityBean setPsamOwnerName(OpportunityBean opportunityBean, Opportunity opportunity,Integer erfCustomerId)
			throws TclCommonException {
//		String psamEmail=opportunity.getPsamEmailId();
//		User user=userRepository.findByEmailId(psamEmail);
		opportunityBean.setOwnerName(userInfoUtils.getUserFullName());
		opportunityBean.setOwnerEmail(Utils.getSource());
/*		if (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(opportunity.getOptyClassification())) {
//			if(checkPartnerRTM(erfCustomerId)) {
//				LOGGER.info("Inside Partner RTM");
//				LOGGER.info("RTM Owner Name :: {}", userInfoUtils.getUserFullName());
//				LOGGER.info("RTM Owner Email :: {}", Utils.getSource());
//				opportunityBean.setOwnerName(userInfoUtils.getUserFullName());
//				opportunityBean.setOwnerEmail(Utils.getSource());
//			} else {
//				LOGGER.info("Outside Partner RTM");
//				String partnerId = String
//						.valueOf(userInfoUtils.getPartnerDetails().stream().findFirst().get().getErfPartnerId());
//				String ownerNameAndEmail = (String) mqUtils.sendAndReceive(partnerAccountNameMQ, partnerId);
//				LOGGER.info("Partner ID :: {}", partnerId);
//				LOGGER.info("Partner Owner Name and Email:: {}", ownerNameAndEmail);
//				if(user!=null) {
//					String psamName = user.getFirstName() + " " + user.getLastName();
//					opportunityBean.setOwnerName(psamName);
//					opportunityBean.setOwnerEmail(psamEmail);
//				}
//				else{
//				opportunityBean.setOwnerName(ownerNameAndEmail.split(COMMA)[0].trim());
//				opportunityBean.setOwnerEmail(ownerNameAndEmail.split(COMMA)[1].trim());
//				}
//			}
//		} else {
//			String customerId = String.valueOf(userInfoUtils.getCustomerDetails().stream().findFirst().get().getErfCustomerId());
//			String ownerName = (String) mqUtils.sendAndReceive(customerAccountManagerName, customerId);
//			String ownerEmail = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, customerId);
//			LOGGER.info("Customer ID :: {}", customerId);
//			LOGGER.info("Customer Owner Name :: {} and Email:: {}", ownerName, ownerEmail);
//			opportunityBean.setOwnerName(ownerName.trim());
//			opportunityBean.setOwnerEmail(ownerEmail.trim());
//		}*/
		return opportunityBean;
	}

	private boolean checkPartnerRTM(Integer customerId) throws TclCommonException {
		String accountRtmName = (String) mqUtils.sendAndReceive(customerAccountRtmName, String.valueOf(customerId));
		LOGGER.info("Account RTM :: {}", accountRtmName);
		if(SFDCConstants.PARTNER_ENT.equalsIgnoreCase(accountRtmName)) {
			return true;
		}
		return false;
	}

	/**
	 * persistSfdcServiceJob
	 *
	 * @param opportunityBean
	 * @param quote
	 * @throws TclCommonException
	 */
	public void persistSfdcServiceJob(String refId, String queueName, String payload, Byte isComplete,
									  String serviceType, Integer seqNum,String serviceId,Byte isDropped) {
		ThirdPartyServiceJob thirdServiceJob = new ThirdPartyServiceJob();
		thirdServiceJob.setCreatedBy(Utils.getSource());
		thirdServiceJob.setCreatedTime(new Date());
		thirdServiceJob.setQueueName(queueName);
		thirdServiceJob.setRefId(refId);
		thirdServiceJob.setRequestPayload(payload);
		thirdServiceJob.setSeqNum(seqNum);
		thirdServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
		thirdServiceJob.setThirdPartySource(ThirdPartySource.SFDC.toString());
		thirdServiceJob.setServiceType(serviceType);
		thirdServiceJob.setIsComplete(isComplete);
		thirdServiceJob.setServiceRefId(serviceId);
		thirdServiceJob.setIsDropped(isDropped);
		thirdPartyServiceJobsRepository.save(thirdServiceJob);
	}

	public void persistSfdcServiceJob(String refId, String queueName, String payload, Byte isComplete,
			String serviceType, Integer seqNum) {
		ThirdPartyServiceJob thirdServiceJob = new ThirdPartyServiceJob();
		thirdServiceJob.setCreatedBy(Utils.getSource());
		thirdServiceJob.setCreatedTime(new Date());
		thirdServiceJob.setQueueName(queueName);
		thirdServiceJob.setRefId(refId);
		thirdServiceJob.setRequestPayload(payload);
		thirdServiceJob.setSeqNum(seqNum);
		thirdServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
		thirdServiceJob.setThirdPartySource(ThirdPartySource.SFDC.toString());
		thirdServiceJob.setServiceType(serviceType);
		thirdServiceJob.setIsComplete(isComplete);
		thirdServiceJob.setServiceRefId(null);
		thirdPartyServiceJobsRepository.save(thirdServiceJob);
	}

	/**
	 * processProductServiceForSolution
	 * <p>
	 * for product service call
	 *
	 * @param quoteToLe
	 * @param productSolution
	 * @throws TclCommonException
	 */
	public void processProductServiceForSolution(QuoteToLe quoteToLe, ProductSolution productSolution,
												 String sfdcOpportunityId) throws TclCommonException {
		if(quoteToLe.getIsMultiCircuit()!=null && quoteToLe.getIsMultiCircuit().equals(CommonConstants.BACTIVE) ) {
			LOGGER.info("Since this is a multicircuit flow so skiping");
			return;
		}
		ProductServiceBean productServiceBean = new ProductServiceBean();
		// Get the QuoteLeAttributes

			productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());


		// productServiceBean.setProductType(SFDCConstants.INTERNET_ACCESS_SERVICE.toString());//have
		// moved to product specific data
		String currency = SFDCConstants.INR;
		if (quoteToLe.getCurrencyCode() != null) {
			currency = quoteToLe.getCurrencyCode();
		}
		productServiceBean.setCurrencyIsoCode(currency);
		productServiceBean.setIsTrainingRequire(SFDCConstants.NO);
		productServiceBean.setIdcBandwidth(SFDCConstants.NO);
		productServiceBean.setMultiVRFSolution(SFDCConstants.NO);
		if(quoteToLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
			//Optional<QuoteToLe> quoteTole = quoteToLeRepository.findById(quoteToLe.getId());
			QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository.findByQuoteIDAndMstOmsAttributeName(quoteToLe.getQuote().getId(), "IS_COMMERCIAL");
			if(quoteLeAttributeValue!=null && quoteLeAttributeValue.getAttributeValue()!=null && quoteLeAttributeValue.getAttributeValue().equalsIgnoreCase("Y")) {
				productServiceBean.setOrderType(SFDCConstants.RENEWALS_WITH_REVESION);
			}else {
				productServiceBean.setOrderType(SFDCConstants.RENEWALS_WITH_OUT_REVESION);
			}		
		}else {
		productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
		}
		productServiceBean.setOpportunityId(sfdcOpportunityId);
		productServiceBean.setQuoteToLeId(quoteToLe.getId());
		productServiceBean.setIsCancel(false);
		String familyName = getFamilyName(quoteToLe);
		LOGGER.info("Family Name for Create Product {}", familyName);
		if (familyName != null) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.getProductServiceInput(quoteToLe, productServiceBean, productSolution);

			}
		}
		productSolutionRepository.save(productSolution);

		/*// setting productMRC and productNRC as 0 at Create Product for Termination specific quotes -- done inside mapper now
		if ((Objects.nonNull(quoteToLe.getQuoteType())
				&& quoteToLe.getQuoteType().equalsIgnoreCase(SFDCConstants.TERMINATION))) {
			productServiceBean.setProductMRC(0D);
			productServiceBean.setProductNRC(0D);
		}*/
		/*
		 * String serviceId=null; List<String>
		 * serviceIds=quoteRepository.findServiceIdByQuoteToLeAndOptyId(quoteToLe.getId(
		 * ), sfdcOpportunityId); for (String servicId : serviceIds) {
		 * serviceId=servicId; productServiceBean.setProductSolutionCode("TERM-" +
		 * quoteToLe.getQuote().getQuoteCode()+"--"+serviceId); }
		 */
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcCreatePrdService,
				Utils.convertObjectToJson(productServiceBean),
				StringUtils.isNotBlank(sfdcOpportunityId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.CREATE_PRODUCT, getSequenceNumber(SFDCConstants.CREATE_PRODUCT));
	}
	
	public void processProductServiceForSolutionMulticircuit(QuoteToLe quoteToLe,
			QuoteToLeProductFamily quoteToLeProductFamily, String sfdcOpportunityId) throws TclCommonException {
		LOGGER.info("sfdcOpportunityId {} ", sfdcOpportunityId);
		List<ProductSolution> productSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		ProductServiceBean productServiceBean = new ProductServiceBean();
		if (!(Objects.nonNull(quoteToLe.getQuoteType())
				&& quoteToLe.getQuoteType().equalsIgnoreCase(SFDCConstants.TERMINATION))) {
			productServiceBean.setProductSolutionCode("MLC-" + quoteToLe.getQuote().getQuoteCode());
		}else {
			// productServiceBean.setProductSolutionCode("TERM-" + quoteToLe.getQuote().getQuoteCode());
			LOGGER.info("Inside termination loop");
			productServiceBean.setProductMRC(0D);
			productServiceBean.setProductNRC(0D);
			if(Objects.nonNull(quoteToLe.getIsMultiCircuit()) && CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Mutlicircuit loop - opty id {}", sfdcOpportunityId);
			String serviceId=null;
			List<String> serviceIds=quoteRepository.findServiceIdByQuoteToLeAndOptyId(quoteToLe.getId(), sfdcOpportunityId);
			if(serviceIds != null && !serviceIds.isEmpty()) {
			for (String servicId : serviceIds) {
				serviceId=servicId;
				productServiceBean.setProductSolutionCode("TERM-" + quoteToLe.getQuote().getQuoteCode()+"--"+serviceId);
			}
			} else {
				productServiceBean.setProductSolutionCode(productSolutions.get(0).getSolutionCode());
			}
			} else {
				LOGGER.info("single circuit oty id {}", sfdcOpportunityId);
				List<ThirdPartyServiceJob> thirdPartyServiceJobList = thirdPartyServiceJobsRepository.
						findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, sfdcOpportunityId);
				if(thirdPartyServiceJobList != null && !thirdPartyServiceJobList.isEmpty()) {
					productServiceBean.setProductSolutionCode(quoteToLe.getQuote().getQuoteCode());
				}
			}
		}
		for (ProductSolution productSolution : productSolutions) {
			String currency = SFDCConstants.INR;
			if (quoteToLe.getCurrencyCode() != null) {
				currency = quoteToLe.getCurrencyCode();
			}
			productServiceBean.setCurrencyIsoCode(currency);
			productServiceBean.setIsTrainingRequire(SFDCConstants.NO);
			productServiceBean.setIdcBandwidth(SFDCConstants.NO);
			productServiceBean.setMultiVRFSolution(SFDCConstants.NO);
			productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
			productServiceBean.setOpportunityId(sfdcOpportunityId);
			productServiceBean.setQuoteToLeId(quoteToLe.getId());
			productServiceBean.setIsCancel(false);
			String familyName = getFamilyName(quoteToLe);
			LOGGER.info("Family Name for Create Product {}", familyName);
			if (familyName != null) {
				IOmsSfdcInputHandler handler = factory.getInstance(familyName);
				if (handler != null) {
					handler.getProductServiceInput(quoteToLe, productServiceBean, productSolution);

				}
			}
			productSolutionRepository.save(productSolution);
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			productServiceBean.setOpportunityId(sfdcOpportunityId);
			}
		}
		if (!productSolutions.isEmpty()) {
			String serviceId=null;
			List<String> serviceIds=quoteRepository.findServiceIdByQuoteToLeAndOptyId(quoteToLe.getId(), sfdcOpportunityId);
			for (String servicId : serviceIds) {
				serviceId=servicId;
				productServiceBean.setProductSolutionCode("TERM-" + quoteToLe.getQuote().getQuoteCode()+"--"+serviceId);
			}
			persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcCreatePrdService,
					Utils.convertObjectToJson(productServiceBean),
					StringUtils.isNotBlank(sfdcOpportunityId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
					SFDCConstants.CREATE_PRODUCT, getSequenceNumber(SFDCConstants.CREATE_PRODUCT),serviceId,CommonConstants.BDEACTIVATE);
		}
	}
	
	
	
	public void processProductServiceForSolutionForCancel(QuoteToLe quoteToLe, ProductSolution productSolution,
			String sfdcOpportunityId, Boolean isCancellation) throws TclCommonException {
		ProductServiceBean productServiceBean = new ProductServiceBean();
// Get the QuoteLeAttributes
		productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());
// productServiceBean.setProductType(SFDCConstants.INTERNET_ACCESS_SERVICE.toString());//have
// moved to product specific data

		String currency = SFDCConstants.INR;
		if (quoteToLe.getCurrencyCode() != null) {
			currency = quoteToLe.getCurrencyCode();
		}
		productServiceBean.setCurrencyIsoCode(currency);
		productServiceBean.setIsTrainingRequire(SFDCConstants.NO);
		productServiceBean.setIdcBandwidth(SFDCConstants.NO);
		productServiceBean.setMultiVRFSolution(SFDCConstants.NO);
		productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
		productServiceBean.setOpportunityId(sfdcOpportunityId);
		productServiceBean.setIsCancel(true);
		productServiceBean.setQuoteToLeId(quoteToLe.getId());
		String familyName = getFamilyName(quoteToLe);
		LOGGER.info("product service bean before handler{} for quote to le id {}", productServiceBean.toString(), quoteToLe.getId());
		LOGGER.info("Family Name for Create Product {}", familyName);
		if (familyName != null) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.getProductServiceInput(quoteToLe, productServiceBean, productSolution);

			}
		}

		if (isCancellation) {
			productServiceBean.setOpportunityId(sfdcOpportunityId);
		}
		LOGGER.info("product service bean after handler{} for quote to le id {}", productServiceBean.toString(), quoteToLe.getId());
		productSolutionRepository.save(productSolution);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode()+"-C", sfdcCreatePrdService,
				Utils.convertObjectToJson(productServiceBean),
				StringUtils.isNotBlank(sfdcOpportunityId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.CREATE_PRODUCT, getSequenceNumber(SFDCConstants.CREATE_PRODUCT),null,CommonConstants.BDEACTIVATE);
	}

	/**
	 * processProductServices- used to process the process service with sfdc end
	 * point
	 *
	 * @param quoteToLe
	 * @param optyId
	 * @throws TclCommonException
	 */
	public void processProductServices(QuoteToLe quoteToLe, String sfdcOpertunityId) throws TclCommonException {
		LOGGER.info("inside processSfdcOpportunityCreateResponse - after saving quoteToLe {}",
				quoteToLe.getTpsSfdcOptyId());
		//List<QuoteToLeProductFamily> quoteToLeProdFamily = quoteToLeProductFamilyRepository
				//.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLe.getQuoteToLeProductFamilies()) {
			if (quoteToLe.getIsMultiCircuit() != null
					&& quoteToLe.getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				LOGGER.info("Multicircuit flow");
				processProductServiceForSolutionMulticircuit(quoteToLe, quoteToLeProductFamily, sfdcOpertunityId);
			} else {
				LOGGER.info("Normal flow flow");
				//List<ProductSolution> productSolutions = productSolutionRepository
				//		.findByQuoteToLeProductFamily(quoteToLeProductFamily);
				for (ProductSolution productSolution : quoteToLeProductFamily.getProductSolutions()) {
					processProductServiceForSolution(quoteToLe, productSolution, sfdcOpertunityId);
				}
			}
		}
	}
	
	public void processProductServicesForIpc(QuoteToLe quoteToLe, String sfdcOpportunityId) throws TclCommonException {
		LOGGER.info("inside processProductServicesForIpc - after saving quoteToLe {}", quoteToLe.getTpsSfdcOptyId());
		List<QuoteToLeProductFamily> quoteToLeProdFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProdFamily.stream().findFirst().get();

		List<ProductSolution> productSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);

		processProductServiceForSolution(quoteToLe, productSolutions.stream().findFirst().get(), sfdcOpportunityId);
	}

	public void processProductServicesForCancellation(QuoteToLe quoteToLe, String sfdcOpertunityId, Boolean isCancellation) throws TclCommonException {
		LOGGER.info("inside processProductServicesForCancellation - after saving quoteToLe {}",
				sfdcOpertunityId);
		List<QuoteToLeProductFamily> quoteToLeProdFamily = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProdFamily) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			for (ProductSolution productSolution : productSolutions) {
				processProductServiceForSolutionForCancel(quoteToLe, productSolution, sfdcOpertunityId, isCancellation);
			}
		}
	}

	private void saveOpportunityIdToOrderForMacd(QuoteToLe quoteToLe, String sfdcOpportunityId, String quoteCode) {
		if (SFDCConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			List<OrderToLe> orderToLes = orderToLeRepository.findByOrder_OrderCode(quoteCode);
			orderToLes.forEach(orderToLe -> {
				orderToLe.setTpsSfdcCopfId(sfdcOpportunityId);
				orderToLeRepository.save(orderToLe);
			});
		}
	}

	/**
	 * Process Product Service for GSC - used to process the process service with
	 * sfdc endpoint
	 *
	 * @param quoteToLe
	 * @param sfdcOpportunityId
	 * @throws TclCommonException
	 */
	public void processProductServicesForGSC(QuoteToLe quoteToLe, String sfdcOpportunityId) throws TclCommonException {
		List<QuoteToLeProductFamily> quoteToLeProdFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProdFamilies.stream().findFirst().get();
		List<ProductSolution> productSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);

		processProductServiceForSolution(quoteToLe, productSolutions.stream().findFirst().get(), sfdcOpportunityId);
	}

	/**
	 * Process Product Service for UCAAS
	 * sfdc endpoint
	 * @param quoteToLe
	 * @param sfdcOpportunityId
	 */
	private void processProductServicesForUCAAS(QuoteToLe quoteToLe,String sfdcOpportunityId) throws TclCommonException {
		List<QuoteToLeProductFamily> quoteToLeProdFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProdFamilies.stream().findFirst().get();
		List<ProductSolution> productSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);

		//	predicate for Eliminating webex solutions
		Predicate<ProductSolution> webexPsUnAvailabilityCheck = ps -> !ps.getProductProfileData()
				.contains(CommonConstants.WEBEX);

		//	Predicate for getting only Webex solutions
		Predicate<ProductSolution> webexPsAvailabilityCheck = ps -> ps.getProductProfileData()
				.contains(CommonConstants.WEBEX);
		processProductServiceForSolution(quoteToLe, productSolutions.stream().filter(webexPsAvailabilityCheck).findFirst().get(), sfdcOpportunityId);
		processProductServiceForSolution(quoteToLe, productSolutions.stream().filter(webexPsUnAvailabilityCheck).findFirst().get(), sfdcOpportunityId);
	}


	/**
	 * processSiteDetails- This method is used to process the site details
	 *
	 * @param orderToLe
	 * @param optyId
	 * @param productServiceId
	 * @throws TclCommonException
	 */
	@Transactional
	public void processSiteDetails(QuoteToLe quoteToLe) throws TclCommonException {
		Map<String, SFDCCommercialBifurcationBean> commercialsMap = new HashMap<>();
		List<QuoteToLeProductFamily> quoteLeProdFamilyRepo = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteLeProdFamilyRepo) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			if (quoteToLe.getIsMultiCircuit()!=null && quoteToLe.getIsMultiCircuit().equals(CommonConstants.BACTIVE) ) {
				LOGGER.info("multicircuit flow");
				processCreateSiteSolutionMulticircuit(quoteToLe, commercialsMap, quoteToLeProductFamily);
			} else {
				LOGGER.info("Normal flow");
				for (ProductSolution productSolution : productSolutions) {
					processCreateSiteSolution(quoteToLe, commercialsMap, productSolution);
				}
			}
		}
	}

	private void processCreateSiteSolution(QuoteToLe quoteToLe,
			Map<String, SFDCCommercialBifurcationBean> commercialsMap, ProductSolution productSolution)
			throws TclCommonException {
		SiteSolutionOpportunityBean siteSolutionOpportunityBean = new SiteSolutionOpportunityBean();
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();
		List<QuoteIllSite> illSites = getIllsitesBasenOnVersion(productSolution);
		for (QuoteIllSite quoteIllSite : illSites) {
			LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteIllSite.getErfLocSitebLocationId()));

			String productname = getFamilyName(quoteToLe);
			if (!("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				addressDetail = validateAddressDetail(addressDetail);
				/* String address=addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo()+" "+addressDetail.getLocality();*/
				SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
				siteOpportunityLocation.setCity(addressDetail.getCity());
				siteOpportunityLocation.setCountry(addressDetail.getCountry());
				siteOpportunityLocation.setLocation(addressDetail.getCity());
				siteOpportunityLocation.setState(addressDetail.getState());
				siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIllSite.getSiteCode());
				siteOpportunityLocation.setSiteMRC(quoteIllSite.getMrc());
				siteOpportunityLocation.setSiteNRC(quoteIllSite.getNrc());
				siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				siteOpportunityLocations.add(siteOpportunityLocation);
			}
			else if ("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))
			{
				LOGGER.info("Site id id ::::::  {}", quoteIllSite.getId());
				String referenceName = productname.equalsIgnoreCase("IAS")
						?QuoteConstants.ILLSITES.toString():productname.equalsIgnoreCase("GVPN")?QuoteConstants.GVPN_SITES.toString():"";
				LOGGER.info("Reference Name is ::::::  {}", referenceName);
			List<QuoteProductComponent> productComponent =
					quoteProductComponentRepository.findByReferenceIdAndReferenceName(quoteIllSite.getId(), referenceName);
			LOGGER.info("Product Components size for quote ill site :::: {}  is ::::  {}  ", quoteIllSite.getId(), productComponent.size());
			productComponent.forEach(component -> {
				QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(component.getId()), MACDConstants.COMPONENTS);
				if (Objects.nonNull(quotePrice)) {
					LOGGER.info("Inside quoteprice if block with nrc ::: {}", quotePrice.getEffectiveNrc());
					populateSiteOpportunityLocationForPrimaryAndSecondary(component.getType(), commercialsMap, quotePrice,quoteToLe);
					LOGGER.info("Map Value :: {}", commercialsMap);
				}
			});
			//SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
			for (Map.Entry<String, SFDCCommercialBifurcationBean> bifurcationBeanEntry : commercialsMap.entrySet()) {
				String productComponentType = bifurcationBeanEntry.getKey();
				String circuitId = "";
				if (Objects.nonNull(quoteToLe)
						&& ((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(quoteToLe.getQuoteType()) || MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())
										|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()))) {
					Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite, quoteToLe);
					LOGGER.info("ServiceIds" + serviceIds);
					if (productComponentType.equalsIgnoreCase(PDFConstants.PRIMARY)) {
						/*circuitId = Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId()) ? quoteToLe.getErfServiceInventoryTpsServiceId() : "";*/
						circuitId = serviceIds.get(PDFConstants.PRIMARY);
					} else if (productComponentType.equalsIgnoreCase(PDFConstants.SECONDARY)) {
						/*circuitId = secondaryCircuitId;*/
						circuitId = serviceIds.get(PDFConstants.SECONDARY);
					}
				}
				SFDCCommercialBifurcationBean bifurcationBean = bifurcationBeanEntry.getValue();
				SiteOpportunityLocation siteOpportunityLocation = setSiteOpportunityLocationForSingleAndDualCircuits(quoteToLe, quoteIllSite, locationResponse, bifurcationBean.getNrc(),
						bifurcationBean.getMrc(), circuitId);
				siteOpportunityLocations.add(siteOpportunityLocation);
			}
		}
		}
		siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
		siteSolutionOpportunityBean.setProductServiceId(productSolution.getTpsSfdcProductId());
		siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);
		siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
		siteSolutionOpportunityBean.setProductSolutionCode(productSolution.getSolutionCode());
		siteSolutionOpportunityBean
				.setSourceSytemTransactionId(SFDCConstants.OPTIMUS + productSolution.getSolutionCode());
		Byte isComplete = CommonConstants.BDEACTIVATE;
		if (StringUtils.isNotBlank(productSolution.getTpsSfdcProductId())
				&& StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())) {
			isComplete = CommonConstants.BACTIVE;
		}

		// included to handle site update issue for NPL
		String familyName = getFamilyName(quoteToLe);
		if (familyName != null && familyName.equalsIgnoreCase(CommonConstants.NPL) || familyName != null && familyName.equalsIgnoreCase(CommonConstants.NDE)) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.processSiteDetails(quoteToLe, siteSolutionOpportunityBean, productSolution);
			}
		}

		String request = Utils.convertObjectToJson(siteSolutionOpportunityBean);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateSite, request, isComplete,
				SFDCConstants.UPDATE_SITE, getSequenceNumber(SFDCConstants.UPDATE_SITE),null,CommonConstants.BDEACTIVATE);
	}

	public void processCreateSiteSolutionTermination(QuoteToLe quoteToLe,
			Map<String, SFDCCommercialBifurcationBean> commercialsMap, String serviceId) throws TclCommonException {
		SiteSolutionOpportunityBean siteSolutionOpportunityBean = new SiteSolutionOpportunityBean();
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();
		Byte isComplete = CommonConstants.BDEACTIVATE;
		List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository
				.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);

		for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {

			QuoteIllSite quoteIllSite = quoteIllSiteToService.getQuoteIllSite();
			if(Objects.nonNull(quoteIllSite)) {
			LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteIllSite.getErfLocSitebLocationId()));


			String productname = getFamilyName(quoteToLe);
			if (!("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))) {
				AddressDetail addressDetail = (AddressDetail) convertJsonToObject(locationResponse,
						AddressDetail.class);
				addressDetail = validateAddressDetail(addressDetail);
				/*
				 * String
				 * address=addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo
				 * ()+" "+addressDetail.getLocality();
				 */
				SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
				siteOpportunityLocation.setCity(addressDetail.getCity());
				siteOpportunityLocation.setCountry(addressDetail.getCountry());
				siteOpportunityLocation.setLocation(addressDetail.getCity());
				siteOpportunityLocation.setState(addressDetail.getState());
				siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIllSite.getSiteCode());
				siteOpportunityLocation.setSiteMRC(quoteIllSite.getMrc());
				siteOpportunityLocation.setSiteNRC(quoteIllSite.getNrc());
				siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				siteOpportunityLocations.add(siteOpportunityLocation);

			} else if ("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname)) {
				LOGGER.info("Site id id ::::::  {}", quoteIllSite.getId());
				String referenceName = productname.equalsIgnoreCase("IAS") ? QuoteConstants.ILLSITES.toString()
						: productname.equalsIgnoreCase("GVPN") ? QuoteConstants.GVPN_SITES.toString() : "";
				LOGGER.info("Reference Name is ::::::  {}", referenceName);
				List<QuoteProductComponent> productComponent = quoteProductComponentRepository
						.findByReferenceIdAndReferenceName(quoteIllSite.getId(), referenceName);
				LOGGER.info("Product Components size for quote ill site :::: {}  is ::::  {}  ", quoteIllSite.getId(),
						productComponent.size());
				productComponent.forEach(component -> {
					QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
							String.valueOf(component.getId()), MACDConstants.COMPONENTS);
					if (Objects.nonNull(quotePrice)) {
						LOGGER.info("Inside quoteprice if block with nrc ::: {}", quotePrice.getEffectiveNrc());
						populateSiteOpportunityLocationForPrimaryAndSecondary(component.getType(), commercialsMap,
								quotePrice, quoteToLe);
						LOGGER.info("Map Value :: {}", commercialsMap);
					} else if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()) && Objects.isNull(quotePrice) && StringUtils.isNotBlank(component.getType())
							&& Objects.nonNull(component.getType())) {
						//LOGGER.info("Inside quoteprice if block with nrc ::: {}", quotePrice.getEffectiveNrc());
						populateSiteOpportunityLocationForPrimaryAndSecondaryTermination(component.getType(), commercialsMap,
								quotePrice, quoteToLe);
						LOGGER.info("Map Value :: {}", commercialsMap);
					}
				});
				// SiteOpportunityLocation siteOpportunityLocation = new
				// SiteOpportunityLocation();
				for (Map.Entry<String, SFDCCommercialBifurcationBean> bifurcationBeanEntry : commercialsMap
						.entrySet()) {
					String productComponentType = bifurcationBeanEntry.getKey();
					String circuitId = "";
					if (Objects.nonNull(quoteToLe)
							&& ((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(quoteToLe.getQuoteType())
									|| MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())
									|| (MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()) && StringUtils.isNotBlank(productComponentType)))) {
						Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite,
								quoteToLe);
						LOGGER.info("ServiceIds" + serviceIds);
						if (productComponentType.equalsIgnoreCase(PDFConstants.PRIMARY)) {
							/*
							 * circuitId = Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId()) ?
							 * quoteToLe.getErfServiceInventoryTpsServiceId() : "";
							 */
							circuitId = serviceIds.get(PDFConstants.PRIMARY);
						} else if (productComponentType.equalsIgnoreCase(PDFConstants.SECONDARY)) {
							/* circuitId = secondaryCircuitId; */
							circuitId = serviceIds.get(PDFConstants.SECONDARY);
						}
					}
					SFDCCommercialBifurcationBean bifurcationBean = bifurcationBeanEntry.getValue();
					SiteOpportunityLocation siteOpportunityLocation = setSiteOpportunityLocationForSingleAndDualCircuits(
							quoteToLe, quoteIllSite, locationResponse, bifurcationBean.getNrc(),
							bifurcationBean.getMrc(), circuitId);
					siteOpportunityLocations.add(siteOpportunityLocation);
				}
			}
			siteSolutionOpportunityBean.setOpportunityId(quoteIllSiteToService.getTpsSfdcOptyId());
			siteSolutionOpportunityBean.setProductServiceId(quoteIllSiteToService.getTpsSfdcProductId());
			siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);
			siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
			siteSolutionOpportunityBean
					.setProductSolutionCode("TERM-" + quoteToLe.getQuote().getQuoteCode() + "--" + serviceId);
			siteSolutionOpportunityBean.setSourceSytemTransactionId(
					SFDCConstants.OPTIMUS + quoteToLe.getQuote().getQuoteCode());

			if (StringUtils.isNotBlank(quoteIllSiteToService.getTpsSfdcProductId())
					&& StringUtils.isNotBlank(quoteIllSiteToService.getTpsSfdcOptyId())) {
				isComplete = CommonConstants.BACTIVE;
			}
		} else {

			siteSolutionOpportunityBean.setOpportunityId(quoteIllSiteToService.getTpsSfdcOptyId());
			siteSolutionOpportunityBean.setProductServiceId(quoteIllSiteToService.getTpsSfdcProductId());
			siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
			siteSolutionOpportunityBean.setProductSolutionCode("TERM-" + quoteToLe.getQuote().getQuoteCode() + "--" + serviceId);
			siteSolutionOpportunityBean.setSourceSytemTransactionId(
			SFDCConstants.OPTIMUS + quoteToLe.getQuote().getQuoteCode());
			// included to handle site update issue for NPL
			String familyName = getFamilyName(quoteToLe);
			Optional<ProductSolution> productSolutionOpt = productSolutionRepository.findById(quoteIllSiteToService.getQuoteNplLink().getProductSolutionId());

			if ((familyName != null && familyName.equalsIgnoreCase(CommonConstants.NPL)) || (familyName != null && familyName.equalsIgnoreCase(CommonConstants.NDE))) {
				processSiteDetailsForChildOpty(quoteToLe, siteSolutionOpportunityBean, productSolutionOpt.get(), quoteIllSiteToService);
		}
		}
		}

		String request = Utils.convertObjectToJson(siteSolutionOpportunityBean);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateSite, request, isComplete,
				SFDCConstants.UPDATE_SITE, getSequenceNumber(SFDCConstants.UPDATE_SITE), serviceId,CommonConstants.BDEACTIVATE);

	}

	private void processCreateSiteSolutionMulticircuit(QuoteToLe quoteToLe,
			Map<String, SFDCCommercialBifurcationBean> commercialsMap, QuoteToLeProductFamily quoteToLeProductFamily)
			throws TclCommonException {
		List<ProductSolution> productSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		SiteSolutionOpportunityBean siteSolutionOpportunityBean = new SiteSolutionOpportunityBean();
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();
		siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
		if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			siteSolutionOpportunityBean.setProductSolutionCode(quoteToLe.getQuote().getQuoteCode());
			siteSolutionOpportunityBean
					.setSourceSytemTransactionId(SFDCConstants.OPTIMUS + "TERM-" + quoteToLe.getQuote().getQuoteCode());
		} else {
		siteSolutionOpportunityBean.setProductSolutionCode("MLC-" + quoteToLe.getQuote().getQuoteCode());
		siteSolutionOpportunityBean
				.setSourceSytemTransactionId(SFDCConstants.OPTIMUS + "MLC-" + quoteToLe.getQuote().getQuoteCode());
		}
		Byte isComplete = CommonConstants.BDEACTIVATE;
		for (ProductSolution productSolution : productSolutions) {
			List<QuoteIllSite> illSites = getIllsitesBasenOnVersion(productSolution);
			for (QuoteIllSite quoteIllSite : illSites) {
				LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(quoteIllSite.getErfLocSitebLocationId()));

				String productname = getFamilyName(quoteToLe);
				if (!("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					addressDetail = validateAddressDetail(addressDetail);
					/*
					 * String
					 * address=addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo
					 * ()+" "+addressDetail.getLocality();
					 */
					SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
					siteOpportunityLocation.setCity(addressDetail.getCity());
					siteOpportunityLocation.setCountry(addressDetail.getCountry());
					siteOpportunityLocation.setLocation(addressDetail.getCity());
					siteOpportunityLocation.setState(addressDetail.getState());
					siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIllSite.getSiteCode());
					siteOpportunityLocation.setSiteMRC(quoteIllSite.getMrc());
					siteOpportunityLocation.setSiteNRC(quoteIllSite.getNrc());
					siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
					siteOpportunityLocations.add(siteOpportunityLocation);
				} else if ("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname)) {
					LOGGER.info("Site id id ::::::  {}", quoteIllSite.getId());
					String referenceName = productname.equalsIgnoreCase("IAS") ? QuoteConstants.ILLSITES.toString()
							: productname.equalsIgnoreCase("GVPN") ? QuoteConstants.GVPN_SITES.toString() : "";
					LOGGER.info("Reference Name is ::::::  {}", referenceName);
					List<QuoteProductComponent> productComponent = quoteProductComponentRepository
							.findByReferenceIdAndReferenceName(quoteIllSite.getId(), referenceName);
					LOGGER.info("Product Components size for quote ill site :::: {}  is ::::  {}  ",
							quoteIllSite.getId(), productComponent.size());
					productComponent.forEach(component -> {
						QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
								String.valueOf(component.getId()), MACDConstants.COMPONENTS);
						if (Objects.nonNull(quotePrice)) {
							LOGGER.info("Inside quoteprice if block with nrc ::: {}", quotePrice.getEffectiveNrc());
							populateSiteOpportunityLocationForPrimaryAndSecondary(component.getType(), commercialsMap,
									quotePrice, quoteToLe);
							LOGGER.info("Map Value :: {}", commercialsMap);
						} else if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()) && Objects.isNull(quotePrice) && Objects.nonNull(component.getType()) ) {
							// LOGGER.info("Inside quoteprice if block with nrc ::: {}", quotePrice.getEffectiveNrc());

							populateSiteOpportunityLocationForPrimaryAndSecondaryTermination(component.getType(), commercialsMap,
									quotePrice, quoteToLe);
							LOGGER.info("Map Value :: {}", commercialsMap);

						}
					});
					// SiteOpportunityLocation siteOpportunityLocation = new
					// SiteOpportunityLocation();
					for (Map.Entry<String, SFDCCommercialBifurcationBean> bifurcationBeanEntry : commercialsMap
							.entrySet()) {
						String productComponentType = bifurcationBeanEntry.getKey();
						String circuitId = "";
						if (Objects.nonNull(quoteToLe)
								&& ((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(quoteToLe.getQuoteType())
								||  ((MACDConstants.TERMINATION_SERVICE).equalsIgnoreCase(quoteToLe.getQuoteType())))) {
							Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite,
									quoteToLe);
							LOGGER.info("ServiceIds" + serviceIds);
							if (productComponentType.equalsIgnoreCase(PDFConstants.PRIMARY)) {
								/*
								 * circuitId = Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId()) ?
								 * quoteToLe.getErfServiceInventoryTpsServiceId() : "";
								 */
								circuitId = serviceIds.get(PDFConstants.PRIMARY);
							} else if (productComponentType.equalsIgnoreCase(PDFConstants.SECONDARY)) {
								/* circuitId = secondaryCircuitId; */
								circuitId = serviceIds.get(PDFConstants.SECONDARY);
							}
						}
						SFDCCommercialBifurcationBean bifurcationBean = bifurcationBeanEntry.getValue();
						SiteOpportunityLocation siteOpportunityLocation = setSiteOpportunityLocationForSingleAndDualCircuits(
								quoteToLe, quoteIllSite, locationResponse, bifurcationBean.getNrc(),
								bifurcationBean.getMrc(), circuitId);
						siteOpportunityLocations.add(siteOpportunityLocation);
					}
				}
			}
			siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			siteSolutionOpportunityBean.setProductServiceId(productSolution.getTpsSfdcProductId());
			siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);
			if (StringUtils.isNotBlank(productSolution.getTpsSfdcProductId())
					&& StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())) {
				isComplete = CommonConstants.BACTIVE;
			}

			// included to handle site update issue for NPL
			String familyName = getFamilyName(quoteToLe);
			if (familyName != null && familyName.equalsIgnoreCase(CommonConstants.NPL)
					|| familyName != null && familyName.equalsIgnoreCase(CommonConstants.NDE)) {
				IOmsSfdcInputHandler handler = factory.getInstance(familyName);
				if (handler != null) {
					handler.processSiteDetails(quoteToLe, siteSolutionOpportunityBean, productSolution);
				}
			}
		}
		if (siteSolutionOpportunityBean.getSiteOpportunityLocations() != null
				&& !siteSolutionOpportunityBean.getSiteOpportunityLocations().isEmpty()) {
			String request = Utils.convertObjectToJson(siteSolutionOpportunityBean);
			persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateSite, request, isComplete,
					SFDCConstants.UPDATE_SITE, getSequenceNumber(SFDCConstants.UPDATE_SITE),null,CommonConstants.BDEACTIVATE);
		}
	}

	protected Map<String, SFDCCommercialBifurcationBean> populateSiteOpportunityLocationForPrimaryAndSecondary(String type, Map<String, SFDCCommercialBifurcationBean> commercialsMap, QuotePrice quotePrice,QuoteToLe quoteToLe) {
		SFDCCommercialBifurcationBean bifurcationBean = null;
		if(!commercialsMap.containsKey(type)) {
			bifurcationBean = new SFDCCommercialBifurcationBean();
		} else {
			bifurcationBean = commercialsMap.get(type);
		}
		bifurcationBean.setNrc(bifurcationBean.getNrc() + quotePrice.getEffectiveNrc());
		bifurcationBean.setMrc(bifurcationBean.getMrc() + quotePrice.getEffectiveMrc());
		LOGGER.info("For normal orders MRC is -----> {} NRC is ----> {} ", bifurcationBean.getMrc(), bifurcationBean.getNrc());


		/*if(Objects.nonNull(quoteToLe.getIsAmended())){
			int result = Byte.compare(quoteToLe.getIsAmended(), BACTIVE);
			if(result==0){
				bifurcationBean.setMrc(0.0-bifurcationBean.getMrc());
				bifurcationBean.setNrc(0.0-bifurcationBean.getNrc());
				LOGGER.info("For Cancellation MRC is -----> {} NRC is ----> {} ", bifurcationBean.getMrc(), bifurcationBean.getNrc());

			}
		}*/

		commercialsMap.put(type, bifurcationBean);
		return commercialsMap;
	}

	protected Map<String, SFDCCommercialBifurcationBean> populateSiteOpportunityLocationForPrimaryAndSecondaryTermination(String type, Map<String, SFDCCommercialBifurcationBean> commercialsMap, QuotePrice quotePrice,QuoteToLe quoteToLe) {
		SFDCCommercialBifurcationBean bifurcationBean = null;
		if(!commercialsMap.containsKey(type)) {
			bifurcationBean = new SFDCCommercialBifurcationBean();
		} else {
			bifurcationBean = commercialsMap.get(type);
		}
		bifurcationBean.setNrc(bifurcationBean.getNrc() + 0D);
		bifurcationBean.setMrc(bifurcationBean.getMrc() + 0D);
		LOGGER.info("For normal orders MRC is -----> {} NRC is ----> {} ", bifurcationBean.getMrc(), bifurcationBean.getNrc());


		/*if(Objects.nonNull(quoteToLe.getIsAmended())){
			int result = Byte.compare(quoteToLe.getIsAmended(), BACTIVE);
			if(result==0){
				bifurcationBean.setMrc(0.0-bifurcationBean.getMrc());
				bifurcationBean.setNrc(0.0-bifurcationBean.getNrc());
				LOGGER.info("For Cancellation MRC is -----> {} NRC is ----> {} ", bifurcationBean.getMrc(), bifurcationBean.getNrc());

			}
		}*/

		commercialsMap.put(type, bifurcationBean);
		return commercialsMap;
	}

	private SiteOpportunityLocation setSiteOpportunityLocationForSingleAndDualCircuits(QuoteToLe quoteToLe, QuoteIllSite quoteIllSite, String locationResponse, Double nrc, Double mrc,String serviceId) throws TclCommonException {
		AddressDetail addressDetail = (AddressDetail) convertJsonToObject(locationResponse,
				AddressDetail.class);
		addressDetail = validateAddressDetail(addressDetail);
		/* String address=addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo()+" "+addressDetail.getLocality();*/
		SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
		siteOpportunityLocation.setCity(addressDetail.getCity());
		siteOpportunityLocation.setCountry(addressDetail.getCountry());
		siteOpportunityLocation.setLocation(addressDetail.getCity());
		siteOpportunityLocation.setState(addressDetail.getState());
		siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIllSite.getSiteCode());
		siteOpportunityLocation.setSiteMRC(mrc);
		siteOpportunityLocation.setSiteNRC(nrc);
		// MACD COPF implementation
		if (Objects.nonNull(quoteToLe)
				&& ((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(quoteToLe.getQuoteType())) || MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())
									|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			siteOpportunityLocation.setCurrentCircuitServiceId(serviceId);
		}
		return siteOpportunityLocation;
	}

	/**
	 * processUpdateProduct
	 *
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processUpdateProduct(QuoteToLe quoteToLe) throws TclCommonException {
		List<QuoteToLeProductFamily> quoteLeProdFamilyRepo = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteLeProdFamilyRepo) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			if (quoteToLe.getIsMultiCircuit()!=null && quoteToLe.getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				LOGGER.info("Since this is a multicircuit - we are accomodating and creating as a single product");
				processProductUpdateSolutionMulticircuit(quoteToLe, quoteToLeProductFamily);
			} else {
				LOGGER.info("Normal Scenario");
				for (ProductSolution productSolution : productSolutions) {
					processProductUpdateSolution(quoteToLe, productSolution);
				}
			}
		}

	}

	private void processProductUpdateSolution(QuoteToLe quoteToLe, ProductSolution productSolution)
			throws TclCommonException {
		ProductServiceBean productServiceBean = new ProductServiceBean();
		Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
		productServiceBean.setProductName(productSolution.getTpsSfdcProductName());
		productServiceBean.setProductId(productSolution.getTpsSfdcProductId());
		// String currencyIsoCode =
		// attributeMapper.get(LeAttributesConstants.CURRENCY_ISO_CODE);
		String currencyIsoCode = quoteToLe.getCurrencyCode(); // to update input currency in sfdc
		productServiceBean.setCurrencyIsoCode(currencyIsoCode != null ? currencyIsoCode : SFDCConstants.INR);
		String variant = CommonConstants.EMPTY;
		productServiceBean.setType(variant);
		productServiceBean.setIdcBandwidth(SFDCConstants.NO);
		if(quoteToLe.getQuoteType().equalsIgnoreCase("RENEWALS")){
		calculateSolutionRenewalsPrice(quoteToLe, productServiceBean);
		}else {
		calculateSolutionPrice(productSolution, productServiceBean);
		}
		productServiceBean.setBigMachinesArc(0D);
		productServiceBean.setBigMachinesMrc(0D);
		productServiceBean.setBigMachinesNrc(0D);
		productServiceBean.setBigMachinesTcv(0D);
		productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
		productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());
		productServiceBean.setL2feasibilityCommercialManagerName(
				attributeMapper.get(LeAttributesConstants.PROGRAM_MANAGER));
		String familyName = getFamilyName(quoteToLe);
		if (familyName != null) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.updateProductServiceInput(quoteToLe, productServiceBean, productSolution);
			}
		}
		String request = Utils.convertObjectToJson(productServiceBean);
		LOGGER.info("Input for updating the product Details {}", request);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
				StringUtils.isNotBlank(productSolution.getTpsSfdcProductId()) ? CommonConstants.BACTIVE
						: CommonConstants.BDEACTIVATE,
				SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT),null,CommonConstants.BDEACTIVATE);
	}
	
	private void processProductUpdateSolutionMulticircuit(QuoteToLe quoteToLe,
			QuoteToLeProductFamily quoteToLeProductFamily) throws TclCommonException {
		List<ProductSolution> productSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		ProductServiceBean productServiceBean = new ProductServiceBean();
		Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
		productServiceBean.setProductMRC(0D);
		productServiceBean.setProductNRC(0D);
		productServiceBean.setProductSolutionCode("MLC-" + quoteToLe.getQuote().getQuoteCode());

		for (ProductSolution productSolution : productSolutions) {
			if (StringUtils.isNotBlank(productSolution.getTpsSfdcProductName())) {
				productServiceBean.setProductName(productSolution.getTpsSfdcProductName());
				productServiceBean.setProductId(productSolution.getTpsSfdcProductId());
			}
			String currencyIsoCode = quoteToLe.getCurrencyCode(); // to update input currency in sfdc
			productServiceBean.setCurrencyIsoCode(currencyIsoCode != null ? currencyIsoCode : SFDCConstants.INR);
			String variant = CommonConstants.EMPTY;
			productServiceBean.setType(variant);
			productServiceBean.setIdcBandwidth(SFDCConstants.NO);
			List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
					CommonConstants.BACTIVE);
			for (QuoteIllSite quoteIllSite : illSites) {
				if (quoteIllSite.getFeasibility() == CommonConstants.BACTIVE) {
					productServiceBean.setProductNRC(productServiceBean.getProductNRC()
							+ (quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0D));
					productServiceBean.setProductMRC(productServiceBean.getProductMRC()
							+ (quoteIllSite.getArc() != null ? quoteIllSite.getArc() / 12 : 0D));
				}
			}
			productServiceBean.setBigMachinesArc(0D);
			productServiceBean.setBigMachinesMrc(0D);
			productServiceBean.setBigMachinesNrc(0D);
			productServiceBean.setBigMachinesTcv(0D);
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());

			productServiceBean
					.setL2feasibilityCommercialManagerName(attributeMapper.get(LeAttributesConstants.PROGRAM_MANAGER));
			String familyName = getFamilyName(quoteToLe);
			if (familyName != null) {
				IOmsSfdcInputHandler handler = factory.getInstance(familyName);
				if (handler != null) {
					handler.updateProductServiceInput(quoteToLe, productServiceBean, productSolution);
				}
			}
		}
		if (!productSolutions.isEmpty()) {
			String request = Utils.convertObjectToJson(productServiceBean);
			LOGGER.info("Input for updating the product Details {}", request);
			persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
					StringUtils.isNotBlank(productServiceBean.getProductId()) ? CommonConstants.BACTIVE
							: CommonConstants.BDEACTIVATE,
					SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT),null,CommonConstants.BDEACTIVATE);
		}
	}

	public void processProductUpdateSolutionTermination(QuoteToLe quoteToLe,
			QuoteIllSiteToService quoteIllSiteToService) throws TclCommonException {
		ProductServiceBean productServiceBean = new ProductServiceBean();
		Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
		ProductSolution productSolution = null;
		productServiceBean.setProductMRC(0D);
		productServiceBean.setProductNRC(0D);
		productServiceBean.setProductName(quoteIllSiteToService.getTpsSfdcProductName());
		productServiceBean.setProductId(quoteIllSiteToService.getTpsSfdcProductId());
		productServiceBean.setProductSolutionCode("TERM-" + quoteToLe.getQuote().getQuoteCode() + "--" + quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
		String currencyIsoCode = quoteToLe.getCurrencyCode(); // to update input currency in sfdc
		productServiceBean.setCurrencyIsoCode(currencyIsoCode != null ? currencyIsoCode : SFDCConstants.INR);
		String variant = CommonConstants.EMPTY;
		productServiceBean.setType(variant);
		productServiceBean.setIdcBandwidth(SFDCConstants.NO);
		QuoteIllSite quoteIllSite = quoteIllSiteToService.getQuoteIllSite();
		if (Objects.nonNull(quoteIllSite) && quoteIllSite.getFeasibility() == CommonConstants.BACTIVE) {
			productServiceBean.setProductNRC(
					productServiceBean.getProductNRC() + (quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0D));
			productServiceBean.setProductMRC(productServiceBean.getProductMRC()
					+ (quoteIllSite.getArc() != null ? quoteIllSite.getArc() / 12 : 0D));
			productSolution = quoteIllSite.getProductSolution();
		} else if(quoteIllSiteToService.getQuoteNplLink() != null && CommonConstants.BACTIVE.equals(quoteIllSiteToService.getQuoteNplLink().getFeasibility())) {
			productServiceBean.setProductNRC(
					productServiceBean.getProductNRC() + (quoteIllSiteToService.getQuoteNplLink().getNrc() != null ? quoteIllSiteToService.getQuoteNplLink().getNrc() : 0D));
			productServiceBean.setProductMRC(productServiceBean.getProductMRC()
					+ (quoteIllSiteToService.getQuoteNplLink().getArc() != null ? quoteIllSiteToService.getQuoteNplLink().getArc() / 12 : 0D));
			Optional<ProductSolution> productSolutionOpt = productSolutionRepository.findById(quoteIllSiteToService.getQuoteNplLink().getProductSolutionId());
			productSolution = productSolutionOpt.get();
		}
		productServiceBean.setBigMachinesArc(0D);
		productServiceBean.setBigMachinesMrc(0D);
		productServiceBean.setBigMachinesNrc(0D);
		productServiceBean.setBigMachinesTcv(0D);
		productServiceBean.setOpportunityId(quoteIllSiteToService.getTpsSfdcOptyId());

		productServiceBean
				.setL2feasibilityCommercialManagerName(attributeMapper.get(LeAttributesConstants.PROGRAM_MANAGER));
		String familyName = getFamilyName(quoteToLe);
		if (familyName != null) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.updateProductServiceInput(quoteToLe, productServiceBean,
						productSolution);
			}
		}
		String request = Utils.convertObjectToJson(productServiceBean);
		LOGGER.info("Input for updating the product Details {}", request);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
				StringUtils.isNotBlank(productServiceBean.getProductId()) ? CommonConstants.BACTIVE
						: CommonConstants.BDEACTIVATE,
				SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT), quoteIllSiteToService.getErfServiceInventoryTpsServiceId(),CommonConstants.BDEACTIVATE);
	}

	/**
	 * Update Product for GSC
	 *
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processUpdateProductForGSC(QuoteToLe quoteToLe) throws TclCommonException {
		// This have to be overriden
	}
	
	public void processUpdateProductIzosdwan(QuoteToLe quoteToLe) {
		//This have to be overriden
	}
	

	public void processUpdateProductForIpc(QuoteToLe quoteToLe) throws TclCommonException {
		List<QuoteToLeProductFamily> quoteLeProdFamilyRepo = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteLeProdFamilyRepo) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			ProductServiceBean productServiceBean = new ProductServiceBean();
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			productServiceBean.setType(CommonConstants.EMPTY);
			productServiceBean.setIdcBandwidth(SFDCConstants.NO);
			productServiceBean.setCurrencyIsoCode(quoteToLe.getCurrencyCode() != null ? quoteToLe.getCurrencyCode() : SFDCConstants.INR);
			productServiceBean.setBigMachinesArc(0D);
			productServiceBean.setBigMachinesMrc(0D);
			productServiceBean.setBigMachinesNrc(0D);
			productServiceBean.setBigMachinesTcv(0D);
			productServiceBean.setL2feasibilityCommercialManagerName(
					attributeMapper.get(LeAttributesConstants.PROGRAM_MANAGER));
			for (ProductSolution productSolution : productSolutions) {
				if(productSolution.getTpsSfdcProductName() != null && productSolution.getTpsSfdcProductId() != null) {
					productServiceBean.setProductName(productSolution.getTpsSfdcProductName());
					productServiceBean.setProductId(productSolution.getTpsSfdcProductId());
					productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());
					break;
				}
			}
			String familyName = getFamilyName(quoteToLe);
			if (familyName != null) {
				IOmsSfdcInputHandler handler = factory.getInstance(familyName);
				if (handler != null) {
					handler.updateProductServiceInput(quoteToLe, productServiceBean, null);
				}
			}
			String request = Utils.convertObjectToJson(productServiceBean);
			LOGGER.info("Input for updating the product Details {}", request);
			if(productServiceBean.getProductId() != null) {
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
						StringUtils.isNotBlank(productServiceBean.getProductId()) ? CommonConstants.BACTIVE
								: CommonConstants.BDEACTIVATE,
						SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT),null,CommonConstants.BDEACTIVATE);
			} else {
				LOGGER.info("Product ID is NULL. CREATE_PRODUCT call is not completed yet.");
			}
		}

	}

	/**
	 * Update Product for UCAAS
	 *
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processUpdateProductForUcaasAndGsc(QuoteToLe quoteToLe) throws TclCommonException {
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilies) {
			if (CommonConstants.UCAAS.equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName())) {
				List<ProductSolution> productSolutions = productSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProductFamily);

				//	predicate for Eliminating webex solutions
				Predicate<ProductSolution> webexPsUnAvailabilityCheck = ps -> !ps.getProductProfileData()
						.contains(CommonConstants.WEBEX);

				//	Predicate for getting only Webex solutions
				Predicate<ProductSolution> webexPsAvailabilityCheck = ps -> ps.getProductProfileData()
						.contains(CommonConstants.WEBEX);

				List<ProductSolution> solutions = new ArrayList<>();
				solutions.add(productSolutions.stream().filter(webexPsAvailabilityCheck).findFirst().get());
				solutions.add(productSolutions.stream().filter(webexPsUnAvailabilityCheck).findFirst().get());
				solutions.forEach(productSolution -> {
					ProductServiceBean productServiceBean;
					IOmsSfdcInputHandler handler;
					try{
						if (productSolution.getProductProfileData().contains(CommonConstants.WEBEX)){
							handler = factory.getInstance(UCAAS);
							productServiceBean = handler.updateProductServiceInput(quoteToLe, null,productSolution);
						}
						else{
							handler = factory.getInstance(GSIP);
							productServiceBean = handler.updateProductServiceInput(quoteToLe, null,productSolution);
						}
						String request = null;
						request = Utils.convertObjectToJson(productServiceBean);
						LOGGER.info("Input for updating the product Details {}", request);
						persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
								StringUtils.isNotBlank(productSolution.getTpsSfdcProductId()) ? CommonConstants.BACTIVE
										: CommonConstants.BDEACTIVATE,
								SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT),null,CommonConstants.BDEACTIVATE);

					}catch (TclCommonException e) {
						e.printStackTrace();
					}
				});
				break;
			}
		}

	}

	/**
	 * Update Product for UCAAS
	 *
	 */
	public void processUpdateProductForUCAAS(QuoteToLe quoteToLe, ProductSolution productSolution) throws TclCommonException {
		IOmsSfdcInputHandler handler = factory.getInstance(UCAAS);
		ProductServiceBean productServiceBean = handler.updateProductServiceInput(quoteToLe,null,
				productSolution);
		String request = null;
		try {
			request = Utils.convertObjectToJson(productServiceBean);
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
		LOGGER.info("Input for updating the ucaas product Details {}", request);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
				StringUtils.isNotBlank(productSolution.getTpsSfdcProductId()) ? CommonConstants.BACTIVE
						: CommonConstants.BDEACTIVATE,
				SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT),null,CommonConstants.BDEACTIVATE);
	}

	/**
	 * processDeleteProduct
	 *
	 * @param quoteToLe
	 * @param productSolution
	 * @throws TclCommonException
	 */
	public void processDeleteProduct(QuoteToLe quoteToLe, ProductSolution productSolution) {
		try {
			ProductServiceBean productServiceBean = new ProductServiceBean();
			List<String> productIds = new ArrayList<>();
			if (StringUtils.isNotBlank(productSolution.getTpsSfdcProductId()))
				productIds.add(productSolution.getTpsSfdcProductId());
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());
			productServiceBean.setProductIds(productIds);
			productServiceBean.setSourceSystem(SFDCConstants.OPTIMUS.toString());
			if(isTeamsDRQuote(quoteToLe.getId())){
				productServiceBean.setQuoteToLeId(quoteToLe.getId());
				List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quoteToLe.getQuote());
				productServiceBean.setParentQuoteToLeId(findParentQuoteToLe(quoteToLes).getId());
				if(Objects.nonNull(quoteToLe.getTpsSfdcParentOptyId())){
					productServiceBean.setParentTpsSfdcOptyId(String.valueOf(quoteToLe.getTpsSfdcParentOptyId()));
				}
			}
			String request = Utils.convertObjectToJson(productServiceBean);
			LOGGER.info("Input for updating the product Details {}", request);

			persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcProductDeleteService, request,
					StringUtils.isNotBlank(productSolution.getTpsSfdcProductId()) ? CommonConstants.BACTIVE
							: CommonConstants.BDEACTIVATE,
					SFDCConstants.DELETE_PRODUCT, getSequenceNumber(SFDCConstants.DELETE_PRODUCT),null,CommonConstants.BDEACTIVATE);
		} catch (Exception e) {
			LOGGER.info("Exception in deleting sfdc product", e);
		}
	}

	public void processUpdateOpportunityTermination(Date cofSignedDate, String stage, QuoteToLe quoteToLe,String serviceId)
			throws TclCommonException {
		LOGGER.info("Entering Process opty for stage --> {}  and quote to le --->  {}  ",stage,quoteToLe.getQuote().getQuoteCode());
		UpdateOpportunityStage updateOpportunityStage = new UpdateOpportunityStage();
		Integer seqId = null;
		String optyId=null;
		String prodName = "";
		prodName = getFamilyName(quoteToLe);
		LOGGER.info("Product Name: {}", prodName);
		List<QuoteIllSiteToService> quoteIllSiteToServices= quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
		for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
			optyId=quoteIllSiteToService.getTpsSfdcOptyId();
		}
		if (stage.equals(SFDCConstants.VERBAL_AGREEMENT_STAGE)) {
			Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
			if (Objects.nonNull(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
					&& StringUtils.isNotBlank(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)))
				updateOpportunityStage
						.setBillingEntity(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY));
			else
				updateOpportunityStage.setBillingEntity("Tata Communications Limited");

			updateOpportunityStage.setBillingFrequency(attributeMapper.get(LeAttributesConstants.BILLING_FREQUENCY));
			updateOpportunityStage.setBillingMethod(attributeMapper.get(LeAttributesConstants.BILLING_METHOD));
			if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
				setPartnerAttributesInUpdateOpportunityStage(optyId, quoteToLe, updateOpportunityStage);
				setSfdcAccountCuid(updateOpportunityStage, attributeMapper);
			} else {
				updateOpportunityStage.setCustomerContractingId(accountCuid);
				updateOpportunityStage.setAccountCuid(accountCuid);
			}
			updateOpportunityStage.setPaymentTerm(
					attributeMapper.get(LeAttributesConstants.PAYMENT_TERM) + " from generation of Invoice");
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}

				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , updateOpportunityStage.getTermOfMonths());

			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("60");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				if (isIpcQuote(quoteToLe)) {
					processUpdateProductForIpc(quoteToLe);
				} else if (!isGscQuote(quoteToLe)) {
					processUpdateProduct(quoteToLe);

				}
			}


			seqId = getSequenceNumber(SFDCConstants.VERBAL_AGREEMENT_STAGE);

			//Commented based on archana confirmation
			/*if(isUcaasQuote(quoteToLe)){
				updateOpportunityStage.setOpportunitySpecification(WebexConstants.OPPPORTUNITY_SPECIFICATION);
			}*/

			LOGGER.info("In process Update opportunity, preapprovedflag {}, credit check triggered {}, variation approved flag {}",
					quoteToLe.getPreapprovedOpportunityFlag(), quoteToLe.getCreditCheckTriggered(), quoteToLe.getVariationApprovedFlag());
			if((Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getPreapprovedOpportunityFlag())) || Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag()) &&
					((Objects.nonNull(quoteToLe.getCreditCheckTriggered())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getCreditCheckTriggered())) ||
					(Objects.nonNull(quoteToLe.getVariationApprovedFlag()) && CommonConstants.BACTIVE.equals(quoteToLe.getVariationApprovedFlag())))) {
				if(quoteToLe.getQuoteToLeProductFamilies().stream().anyMatch(family ->
						family.getMstProductFamily().getName().equals(CommonConstants.IAS)
							|| family.getMstProductFamily().getName().equals(CommonConstants.NPL)
								|| family.getMstProductFamily().getName().equals(CommonConstants.GVPN)
								|| family.getMstProductFamily().getName().equals(CommonConstants.NDE)
								|| family.getMstProductFamily().getName().equals(UCAAS)
									|| family.getMstProductFamily().getName().equals(CommonConstants.IPC))){
					processCreditCheck(optyId, quoteToLe, updateOpportunityStage, seqId);
				}
			}

			if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getMstProductFamily().getName();
				String[] nplProductName = {null};
				if(CommonConstants.NPL.equalsIgnoreCase(productName))
					nplProductName[0] = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering().getProductName();
				Set<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteToLe.getQuoteIllSiteToServices();
				quoteIllSiteToServiceList.stream().forEach(quoteSiteToService -> {
					if(MACDConstants.ABSORBED.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
						updateOpportunityStage.setCancellationCharges("0");
					} else if(MACDConstants.PASSED_ON.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
					if(CommonConstants.IAS.equalsIgnoreCase(productName) || CommonConstants.GVPN.equalsIgnoreCase(productName) || CommonConstants.IZOPC.equalsIgnoreCase(productName)) {
					LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
					updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
					} else if(CommonConstants.NPL.equalsIgnoreCase(productName) || CommonConstants.NDE.equalsIgnoreCase(productName)) {
						if (CommonConstants.NPL.equalsIgnoreCase(productName)
								&& "MMR Cross Connect".equalsIgnoreCase(nplProductName[0])) {
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
						} else {
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteNplLink().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteNplLink().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteNplLink().getNrc()) : "0");
					}
					}
					} else {
						updateOpportunityStage.setCancellationCharges("0");
					}
					updateOpportunityStage.setLeadTimeToRFSC("5");

				});


			}

			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Updating child opportunity for termination, optyid {}, service id {}", optyId, serviceId);
				updateOpportunityStage.setParentTerminationOpportunityName(quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setCurrentCircuitServiceId(serviceId);
				updateOpportunityStage.setOpportunityId(optyId);
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
				LOGGER.info("quoteIllSiteToServiceList size {}", quoteIllSiteToServiceList.size());
				processProductUpdateSolutionTermination(quoteToLe, quoteIllSiteToServiceList.get(0));
			}
		} else if (stage.equals(SFDCConstants.CLOSED_WON_COF_RECI)) {

			//product service level update code for sfdc order type
			/*String familyName =getFamilyName(quoteToLe);
			if(Objects.nonNull(quoteToLe.getQuoteType())){
				if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
				{
					if(IAS.equalsIgnoreCase(familyName) || GVPN.equalsIgnoreCase(familyName))
					processUpdateProduct(quoteToLe);
				}
			}*/
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					 months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}
				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in proposal sent block is ----> {} " , updateOpportunityStage.getTermOfMonths());
			}
			String familyName = getFamilyName(quoteToLe);
			updateOpportunityStage.setProductType(familyName);
			updateOpportunityStage.setCofSignedDate(DateUtil.convertDateToString(cofSignedDate));
			//updateOpportunityStage.setSalesAdministratorRegion(SFDCConstants.INDIA_WHOLESALE);
			updateOpportunityStage.setDsPreparedBy(SFDCConstants.CUSTOMER_URGENCY);
			if(!MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				updateOpportunityStage.setSalesAdministrator("None");
			}
			updateOpportunityStage.setWinReason(SFDCConstants.WIN_REASONS);
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(new Date()));
			if(isUcaasQuote(quoteToLe)){
				updateOpportunityStage.setE2eCommentsC(SFDCConstants.OPTIMUS_OPPORTUNITY);
			}
			seqId = getSequenceNumber(SFDCConstants.CLOSED_WON_COF_RECI);
			
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Updating child opportunity for termination, optyid {}, service id {}", optyId, serviceId);
				updateOpportunityStage.setParentTerminationOpportunityName(quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setCurrentCircuitServiceId(serviceId);
				updateOpportunityStage.setOpportunityId(optyId);
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
				LOGGER.info("quoteIllSiteToServiceList size {}", quoteIllSiteToServiceList.size());
				processProductUpdateSolutionTermination(quoteToLe, quoteIllSiteToServiceList.get(0));
			}
			
		} else if (stage.equals(SFDCConstants.COF_WON_PROCESS_STAGE)) {
			String programManager = null;
			List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
					.findByNameAndIsActive(LeAttributesConstants.PROGRAM_MANAGER, CommonConstants.BACTIVE);
			for (MstOmsAttribute mstOmsAttribute : mstAttributes) {
				List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
				for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
					programManager = quoteLeAttributeValue.getAttributeValue();
				}
			}
			updateOpportunityStage.setProgramManager(programManager);
			seqId = getSequenceNumber(SFDCConstants.COF_WON_PROCESS_STAGE);
		} else if (stage.equals(SFDCConstants.PROPOSAL_SENT)) {
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					 months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}
				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in proposal sent block is ----> {} " , updateOpportunityStage.getTermOfMonths());
			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("60");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if (isGscQuote(quoteToLe)) {
				processUpdateProductForGSC(quoteToLe);
			} else if (quoteToLe.getQuote().getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
				processUpdateProductIzosdwan(quoteToLe);
				LOGGER.info("Trigger update product sfdc");
			} else if (isUcaasQuote(quoteToLe)) {
				processUpdateProductForUcaasAndGsc(quoteToLe);
			} else if(isIpcQuote(quoteToLe)){
				processUpdateProductForIpc(quoteToLe);
			}else {
				LOGGER.info("QUOTE ID IS :::: {}", quoteToLe.getId());
				processUpdateProduct(quoteToLe);
			}
			seqId = getSequenceNumber(SFDCConstants.PROPOSAL_SENT);

		}
		// SFDC UPDATE OPPORTUNITY for CLOSED_DROPPED - reasons will be changed in
		// future
		else if (stage.equals(SFDCConstants.CLOSED_DROPPED)) {
			String firstName = "";
			String lastName = "";
			String userName = Utils.getSource();
			LOGGER.info("userName :::: {}", userName);
			if (Objects.nonNull(userName)) {
				User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
				if (Objects.nonNull(user)) {
					if (Objects.nonNull(user.getFirstName()))
						firstName = user.getFirstName();
					if (Objects.nonNull(user.getLastName()))
						lastName = user.getLastName();
				}
				LOGGER.info("user :::: {}", user);
			}
			updateOpportunityStage.setWinLossDropKeyReason(SFDCConstants.WIN_LOSS_DROP_KEY_REASON);
			updateOpportunityStage.setDropReasons(SFDCConstants.DROP_REASONS);
			String droppingReason = SFDCConstants.DROPPING_REASON + "," + firstName + " " + lastName;
			updateOpportunityStage.setDroppingReason(droppingReason.trim());
			seqId = getSequenceNumber(SFDCConstants.CLOSED_DROPPED);
			LOGGER.info("seqId :::: {}", seqId);

			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Updating child opportunity for termination, optyid {}, service id {}", optyId, serviceId);
				updateOpportunityStage.setCurrentCircuitServiceId(serviceId);
			}
		} else if(stage.equals(SFDCConstants.IDENTIFIED_OPTY_STAGE)) {

			Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
			if (Objects.nonNull(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
					&& StringUtils.isNotBlank(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)))
				updateOpportunityStage
						.setBillingEntity(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY));
			else
				updateOpportunityStage.setBillingEntity("Tata Communications Limited");

			updateOpportunityStage.setBillingFrequency(attributeMapper.get(LeAttributesConstants.BILLING_FREQUENCY));
			updateOpportunityStage.setBillingMethod(attributeMapper.get(LeAttributesConstants.BILLING_METHOD));
			if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
				setPartnerAttributesInUpdateOpportunityStage(optyId, quoteToLe, updateOpportunityStage);
				setSfdcAccountCuid(updateOpportunityStage, attributeMapper);
			} else {
				updateOpportunityStage.setCustomerContractingId(accountCuid);
				updateOpportunityStage.setAccountCuid(accountCuid);
			}
			updateOpportunityStage.setPaymentTerm(
					attributeMapper.get(LeAttributesConstants.PAYMENT_TERM) + " from generation of Invoice");
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}

				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , updateOpportunityStage.getTermOfMonths());

			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("60");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				if (isIpcQuote(quoteToLe)) {
					processUpdateProductForIpc(quoteToLe);
				} else if (!isGscQuote(quoteToLe)) {
					processUpdateProduct(quoteToLe);

				}
			}
			

			seqId = getSequenceNumber(SFDCConstants.IDENTIFIED_OPTY_STAGE);

			//Commented based on archana confirmation
			/*if(isUcaasQuote(quoteToLe)){
				updateOpportunityStage.setOpportunitySpecification(WebexConstants.OPPPORTUNITY_SPECIFICATION);
			}*/

			LOGGER.info("In process Update opportunity, preapprovedflag {}, credit check triggered {}, variation approved flag {}",
					quoteToLe.getPreapprovedOpportunityFlag(), quoteToLe.getCreditCheckTriggered(), quoteToLe.getVariationApprovedFlag());
			if((Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag())  
					&& CommonConstants.BACTIVE.equals(quoteToLe.getPreapprovedOpportunityFlag())) || Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag()) && 
					((Objects.nonNull(quoteToLe.getCreditCheckTriggered()) 
					&& CommonConstants.BACTIVE.equals(quoteToLe.getCreditCheckTriggered())) || 
					(Objects.nonNull(quoteToLe.getVariationApprovedFlag()) && CommonConstants.BACTIVE.equals(quoteToLe.getVariationApprovedFlag())))) {
				if(quoteToLe.getQuoteToLeProductFamilies().stream().anyMatch(family ->
						family.getMstProductFamily().getName().equals(CommonConstants.IAS)  
							|| family.getMstProductFamily().getName().equals(CommonConstants.NPL)
								|| family.getMstProductFamily().getName().equals(CommonConstants.GVPN) 
								|| family.getMstProductFamily().getName().equals(CommonConstants.NDE)
								|| family.getMstProductFamily().getName().equals(UCAAS)
									|| family.getMstProductFamily().getName().equals(CommonConstants.IPC))){
					processCreditCheck(optyId, quoteToLe, updateOpportunityStage, seqId);
				}
			}
			
			if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getMstProductFamily().getName();
				String[] nplProductName = {null};
				if(CommonConstants.NPL.equalsIgnoreCase(productName))
					nplProductName[0] = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering().getProductName();
				Set<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteToLe.getQuoteIllSiteToServices();
				quoteIllSiteToServiceList.stream().forEach(quoteSiteToService -> {
					if(MACDConstants.ABSORBED.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
						updateOpportunityStage.setCancellationCharges("0");	
					} else if(MACDConstants.PASSED_ON.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
					if(CommonConstants.IAS.equalsIgnoreCase(productName) || CommonConstants.GVPN.equalsIgnoreCase(productName) || CommonConstants.IZOPC.equalsIgnoreCase(productName)) {
					LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
					updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
					} else if(CommonConstants.NPL.equalsIgnoreCase(productName) || CommonConstants.NDE.equalsIgnoreCase(productName)) {
						if (CommonConstants.NPL.equalsIgnoreCase(productName)
								&& "MMR Cross Connect".equalsIgnoreCase(nplProductName[0])) { 
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
						} else {
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteNplLink().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteNplLink().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteNplLink().getNrc()) : "0");
					}
					}
					} else {
						updateOpportunityStage.setCancellationCharges("0");
					}
					updateOpportunityStage.setLeadTimeToRFSC("5");	
					
				});
				
				
			}
			
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				LOGGER.info("Updating child opportunity for termination, optyid {}, service id {}", optyId, serviceId);
				updateOpportunityStage.setParentTerminationOpportunityName(quoteToLe.getTpsSfdcOptyId());
				updateOpportunityStage.setCurrentCircuitServiceId(serviceId);
				updateOpportunityStage.setOpportunityId(optyId);
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
				LOGGER.info("quoteIllSiteToServiceList size {}", quoteIllSiteToServiceList.size());
				processProductUpdateSolutionTermination(quoteToLe, quoteIllSiteToServiceList.get(0));
			}
			
		
		}

		updateOpportunityStage.setOpportunityId(optyId);
		updateOpportunityStage.setStageName(stage);
		updateOpportunityStage.setName(constructOpportunityName(quoteToLe,prodName,updateOpportunityStage.getType()));
		LOGGER.info("Opportunity name in processUpdateOpportunity() for quoteToLeId {}: {}", quoteToLe.getId(), updateOpportunityStage.getName());
		String familyName = getFamilyName(quoteToLe);

		// introduced for IZOPC related changes since the status PROPOSAL_SENT won't be
		// logical in IZOPC
		if (familyName.equals("IZOPC")) {
			processUpdateProduct(quoteToLe);
		}

		if (familyName != null) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.getOpportunityUpdate(quoteToLe, updateOpportunityStage);
			}
		}

		if(familyName.equals("IZO SDWAN")) {
			updateBundleProducts(quoteToLe,updateOpportunityStage);
		}

		if(quoteToLe.getIsAmended()!=null && quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE)) {
			updateOpportunityStage.setType(SFDCConstants.AMENDMENT);
		}
		String request = Utils.convertObjectToJson(updateOpportunityStage);
		LOGGER.info("Input for updating the opty Status {}", request);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateOpty, request,
				StringUtils.isNotBlank(optyId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.UPDATE_OPTY, seqId,serviceId,CommonConstants.BDEACTIVATE);
	}

	/**
	 * processUpdateOpportunity -This method is used to update the opportunity
	 *
	 * @param cofSignedDate
	 * @param optyId
	 * @param stage
	 * @throws TclCommonException
	 */
	public void processUpdateOpportunity(Date cofSignedDate, String optyId, String stage, QuoteToLe quoteToLe)
			throws TclCommonException {
		LOGGER.info("Entering Process opty for stage --> {}  and quote to le --->  {}  ",stage,quoteToLe.getQuote().getQuoteCode());
		String prodName = "";
		prodName = getFamilyName(quoteToLe);
		LOGGER.info("Product Name: {}", prodName);
		UpdateOpportunityStage updateOpportunityStage = new UpdateOpportunityStage();
		Integer seqId = null;

		if (stage.equals(SFDCConstants.VERBAL_AGREEMENT_STAGE)) {
			Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
			if (Objects.nonNull(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
					&& StringUtils.isNotBlank(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)))
				updateOpportunityStage
						.setBillingEntity(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY));
			else
				updateOpportunityStage.setBillingEntity("Tata Communications Limited");

			updateOpportunityStage.setBillingFrequency(attributeMapper.get(LeAttributesConstants.BILLING_FREQUENCY));
			updateOpportunityStage.setBillingMethod(attributeMapper.get(LeAttributesConstants.BILLING_METHOD));
			if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
				setPartnerAttributesInUpdateOpportunityStage(optyId, quoteToLe, updateOpportunityStage);
				setSfdcAccountCuid(updateOpportunityStage, attributeMapper);
			} else {
				updateOpportunityStage.setCustomerContractingId(accountCuid);
				updateOpportunityStage.setAccountCuid(accountCuid);
			}
			updateOpportunityStage.setPaymentTerm(
					attributeMapper.get(LeAttributesConstants.PAYMENT_TERM) + " from generation of Invoice");
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}

				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , updateOpportunityStage.getTermOfMonths());

			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("60");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				if (isIpcQuote(quoteToLe)) {
					processUpdateProductForIpc(quoteToLe);
				} else if (!isGscQuote(quoteToLe)) {
					processUpdateProduct(quoteToLe);

				}
			}

			seqId = getSequenceNumber(SFDCConstants.VERBAL_AGREEMENT_STAGE);

			//Commented based on archana confirmation
			/*if(isUcaasQuote(quoteToLe)){
				updateOpportunityStage.setOpportunitySpecification(WebexConstants.OPPPORTUNITY_SPECIFICATION);
			}*/

			LOGGER.info("In process Update opportunity, preapprovedflag {}, credit check triggered {}, variation approved flag {}",
					quoteToLe.getPreapprovedOpportunityFlag(), quoteToLe.getCreditCheckTriggered(), quoteToLe.getVariationApprovedFlag());
			if((Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag())  
					&& CommonConstants.BACTIVE.equals(quoteToLe.getPreapprovedOpportunityFlag())) || Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag()) && 
					((Objects.nonNull(quoteToLe.getCreditCheckTriggered()) 
					&& CommonConstants.BACTIVE.equals(quoteToLe.getCreditCheckTriggered())) || 
					(Objects.nonNull(quoteToLe.getVariationApprovedFlag()) && CommonConstants.BACTIVE.equals(quoteToLe.getVariationApprovedFlag())))) {
				if(quoteToLe.getQuoteToLeProductFamilies().stream().anyMatch(family ->
						family.getMstProductFamily().getName().equals(CommonConstants.IAS)  
							|| family.getMstProductFamily().getName().equals(CommonConstants.NPL)
								|| family.getMstProductFamily().getName().equals(CommonConstants.GVPN) 
								|| family.getMstProductFamily().getName().equals(CommonConstants.NDE)
								|| family.getMstProductFamily().getName().equals(UCAAS)
									|| family.getMstProductFamily().getName().equals(CommonConstants.IPC)
									|| family.getMstProductFamily().getName().equals(CommonConstants.IZOSDWAN)
									|| family.getMstProductFamily().getName().equals(CommonConstants.IZO_SDWAN)
									|| family.getMstProductFamily().getName().equals(MICROSOFT_CLOUD_SOLUTIONS))){

					processCreditCheck(optyId, quoteToLe, updateOpportunityStage, seqId);
				}
			}
			
			LOGGER.info("VErbal aggreement quotecode {} quoteToLe type {} ", quoteToLe.getQuote().getQuoteCode(), quoteToLe.getQuoteType());
			if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getMstProductFamily().getName();
				String[] nplProductName = {null};
				if(CommonConstants.NPL.equalsIgnoreCase(productName))
					nplProductName[0] = quoteToLe.getQuoteToLeProductFamilies().stream().findAny().get().getProductSolutions().stream().findAny().get().getMstProductOffering().getProductName();
				Set<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteToLe.getQuoteIllSiteToServices();
				quoteIllSiteToServiceList.stream().forEach(quoteSiteToService -> {
					if(MACDConstants.ABSORBED.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
						updateOpportunityStage.setCancellationCharges("0");	
					} else if(MACDConstants.PASSED_ON.equalsIgnoreCase(quoteSiteToService.getAbsorbedOrPassedOn())) {
					if(CommonConstants.IAS.equalsIgnoreCase(productName) || CommonConstants.GVPN.equalsIgnoreCase(productName) || CommonConstants.IZOPC.equalsIgnoreCase(productName)) {
					LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
					updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
					} else if(CommonConstants.NPL.equalsIgnoreCase(productName) || CommonConstants.NDE.equalsIgnoreCase(productName)) {
						if (CommonConstants.NPL.equalsIgnoreCase(productName)
								&& "MMR Cross Connect".equalsIgnoreCase(nplProductName[0])) { 
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteIllSite().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteIllSite().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteIllSite().getNrc()) : "0");
						} else {
							LOGGER.info("service id {}, cancellation charge {}", quoteSiteToService.getErfServiceInventoryTpsServiceId(), quoteSiteToService.getQuoteNplLink().getNrc());
							updateOpportunityStage.setCancellationCharges(quoteSiteToService.getQuoteNplLink().getNrc() != null ? String.valueOf(quoteSiteToService.getQuoteNplLink().getNrc()) : "0");
					}
					}
					} else {
						updateOpportunityStage.setCancellationCharges("0");
					}
					updateOpportunityStage.setLeadTimeToRFSC("5");
					LOGGER.info("Constructing verbal agreement payload for quoteCode {} quote id {} and circuit id {}", quoteToLe.getQuote().getQuoteCode(), quoteToLe.getQuote().getId(), quoteSiteToService.getErfServiceInventoryTpsServiceId());
					updateOpportunityStage.setCurrentCircuitServiceId(quoteSiteToService.getErfServiceInventoryTpsServiceId());
					LOGGER.info("updating verbal agreement payload for quoteCode {} quote id {} with circuit id {}", quoteToLe.getQuote().getQuoteCode(), quoteToLe.getQuote().getId(), updateOpportunityStage.getCurrentCircuitServiceId());
					
				});
				
				
			}

		} else if (stage.equals(SFDCConstants.CLOSED_WON_COF_RECI)) {

			//product service level update code for sfdc order type
			/*String familyName =getFamilyName(quoteToLe);
			if(Objects.nonNull(quoteToLe.getQuoteType())){
				if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
				{
					if(IAS.equalsIgnoreCase(familyName) || GVPN.equalsIgnoreCase(familyName))
					processUpdateProduct(quoteToLe);
				}
			}*/
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					 months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}
				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in proposal sent block is ----> {} " , updateOpportunityStage.getTermOfMonths());
			}
			String familyName = getFamilyName(quoteToLe);
			updateOpportunityStage.setProductType(familyName);
			updateOpportunityStage.setCofSignedDate(DateUtil.convertDateToString(cofSignedDate));
			//updateOpportunityStage.setSalesAdministratorRegion(SFDCConstants.INDIA_WHOLESALE);
			updateOpportunityStage.setDsPreparedBy(SFDCConstants.CUSTOMER_URGENCY);
			if(!MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				updateOpportunityStage.setSalesAdministrator("None");
			}
			updateOpportunityStage.setWinReason(SFDCConstants.WIN_REASONS);
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(new Date()));
			if (!(CANCELLATION_ORDER.equalsIgnoreCase(quoteToLe.getQuoteType())) && ORDER_ENRICHMENT.equalsIgnoreCase(quoteToLe.getStage())) {
				String leadTime=getLeadTimefromOrder(quoteToLe);
				if (StringUtils.isNotBlank(leadTime)) {
					updateOpportunityStage.setLeadTimeToRFSC(leadTime);
				}else {
					updateOpportunityStage.setLeadTimeToRFSC("60");
				}
			}
			else
			{
				updateOpportunityStage.setLeadTimeToRFSC("60");
			}
			if(isUcaasQuote(quoteToLe) || isTeamsDRQuote(quoteToLe.getId())){
				updateOpportunityStage.setE2eCommentsC(SFDCConstants.OPTIMUS_OPPORTUNITY);
			}
			seqId = getSequenceNumber(SFDCConstants.CLOSED_WON_COF_RECI);
		} else if (stage.equals(SFDCConstants.COF_WON_PROCESS_STAGE)) {
			String programManager = null;
			List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
					.findByNameAndIsActive(LeAttributesConstants.PROGRAM_MANAGER, CommonConstants.BACTIVE);
			for (MstOmsAttribute mstOmsAttribute : mstAttributes) {
				List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
				for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
					programManager = quoteLeAttributeValue.getAttributeValue();
				}
			}
			updateOpportunityStage.setProgramManager(programManager);
			seqId = getSequenceNumber(SFDCConstants.COF_WON_PROCESS_STAGE);
		} else if (stage.equals(SFDCConstants.PROPOSAL_SENT)) {
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					 months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}
				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in proposal sent block is ----> {} " , updateOpportunityStage.getTermOfMonths());
			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("60");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if(isTeamsDRQuote(quoteToLe.getId())){
				// teamsdr quote.
				processUpdateProductForTeamsDR(quoteToLe,null);
			}
			else if (isGscQuote(quoteToLe)) {
				processUpdateProductForGSC(quoteToLe);
			} else if (quoteToLe.getQuote().getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
				processUpdateProductIzosdwan(quoteToLe);
				LOGGER.info("Trigger update product sfdc");
			} else if (isUcaasQuote(quoteToLe)) {
				processUpdateProductForUcaasAndGsc(quoteToLe);
			} else if(isIpcQuote(quoteToLe)){
				processUpdateProductForIpc(quoteToLe);
			}else if(isRenewals(quoteToLe)){ //todoo
				processUpdateProductRenewals(quoteToLe);
			}
			else {
				LOGGER.info("QUOTE ID IS :::: {}", quoteToLe.getId());
				processUpdateProduct(quoteToLe);
			}
			seqId = getSequenceNumber(SFDCConstants.PROPOSAL_SENT);

		}
		// SFDC UPDATE OPPORTUNITY for CLOSED_DROPPED - reasons will be changed in
		// future
		else if (stage.equals(SFDCConstants.CLOSED_DROPPED)) {
			String firstName = "";
			String lastName = "";
			String userName = Utils.getSource();
			LOGGER.info("userName :::: {}", userName);
			if (Objects.nonNull(userName)) {
				User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
				if (Objects.nonNull(user)) {
					if (Objects.nonNull(user.getFirstName()))
						firstName = user.getFirstName();
					if (Objects.nonNull(user.getLastName()))
						lastName = user.getLastName();
				}
				LOGGER.info("user :::: {}", user);
			}
			updateOpportunityStage.setWinLossDropKeyReason(SFDCConstants.WIN_LOSS_DROP_KEY_REASON);
			updateOpportunityStage.setDropReasons(SFDCConstants.DROP_REASONS);
			String droppingReason = SFDCConstants.DROPPING_REASON + "," + firstName + " " + lastName;
			updateOpportunityStage.setDroppingReason(droppingReason.trim());
			seqId = getSequenceNumber(SFDCConstants.CLOSED_DROPPED);
			LOGGER.info("seqId :::: {}", seqId);
		} else if(stage.equals(SFDCConstants.IDENTIFIED_OPTY_STAGE)) {
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
			if (Objects.nonNull(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
					&& StringUtils.isNotBlank(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)))
				updateOpportunityStage
						.setBillingEntity(attributeMapper.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY));
			else
				updateOpportunityStage.setBillingEntity("Tata Communications Limited");

			updateOpportunityStage.setBillingFrequency(attributeMapper.get(LeAttributesConstants.BILLING_FREQUENCY));
			updateOpportunityStage.setBillingMethod(attributeMapper.get(LeAttributesConstants.BILLING_METHOD));
			if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
				setPartnerAttributesInUpdateOpportunityStage(optyId, quoteToLe, updateOpportunityStage);
				setSfdcAccountCuid(updateOpportunityStage, attributeMapper);
			} else {
				updateOpportunityStage.setCustomerContractingId(accountCuid);
				updateOpportunityStage.setAccountCuid(accountCuid);
			}
			updateOpportunityStage.setPaymentTerm(
					attributeMapper.get(LeAttributesConstants.PAYMENT_TERM) + " from generation of Invoice");
			if (quoteToLe.getTermInMonths() != null) {
				Integer months = 12;
				Double months1 = 0.0;
				String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
				if (termsInMonth.contains("year")) {
					months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
				}
				else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
					months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
				}
				else if (termsInMonth.contains("months")) {
					months = Integer.valueOf(termsInMonth.replace("months", "").trim());
				} else if (termsInMonth.contains("month")) {
					months = Integer.valueOf(termsInMonth.replace("month", "").trim());
				}

				int compare = Double.compare(months1, 0.0);
				if(compare==0){
					LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months));
				}
				else {
					LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
					updateOpportunityStage.setTermOfMonths(String.valueOf(months1));
				}

				LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , updateOpportunityStage.getTermOfMonths());

			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 30);
			updateOpportunityStage.setLeadTimeToRFSC("30");
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				if (isIpcQuote(quoteToLe)) {
					processUpdateProductForIpc(quoteToLe);
				} else if (!isGscQuote(quoteToLe)) {
					processUpdateProduct(quoteToLe);
				}
			}

			seqId = getSequenceNumber(SFDCConstants.IDENTIFIED_OPTY_STAGE);
		}
		}

		updateOpportunityStage.setOpportunityId(optyId);
		updateOpportunityStage.setStageName(stage);
		String familyName = getFamilyName(quoteToLe);
		updateOpportunityStage.setName(constructOpportunityName(quoteToLe,prodName,updateOpportunityStage.getType()));
		LOGGER.info("Stage {}: Opportunity name in processUpdateOpportunity() for quoteToLeId {}: {}",stage, quoteToLe.getId(), updateOpportunityStage.getName());

		// introduced for IZOPC related changes since the status PROPOSAL_SENT won't be
		// logical in IZOPC
		//if (familyName.equals("IZOPC")) {
		//	processUpdateProduct(quoteToLe);
		//}

		if (familyName != null) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.getOpportunityUpdate(quoteToLe, updateOpportunityStage);
			}
		}
		
		if(familyName.equals("IZO SDWAN")) {
			updateBundleProducts(quoteToLe,updateOpportunityStage);
		}
		
		if(quoteToLe.getIsAmended()!=null && quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE)) {
			updateOpportunityStage.setType(SFDCConstants.AMENDMENT);
		}
		List<MstSfdcAmSaRegion> mstSfdcAmSaRegion=null;
		Optional<User> userSource = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
		String userType="";
		if (userSource.isPresent()) {
			userType=userSource.get().getUserType();
		}
		if (userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			if (appEnv.equals(SFDCConstants.PROD)) {
				LOGGER.info("OPPORTUNITY USER EMAIL ID {}", userSource.get().getEmailId());
				mstSfdcAmSaRegion= mstSfdcAmSaRegionRepository.findByAmEmail(userSource.get().getEmailId());
				if(mstSfdcAmSaRegion!=null && !mstSfdcAmSaRegion.isEmpty()) {
					LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
					updateOpportunityStage.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
				}
				else{
					LOGGER.info("MST SFDC Region is empty for "+userSource.get().getEmailId());
					updateOpportunityStage.setSalesAdministratorRegion(null);
				}
			}
		} else {
			if (appEnv.equals(SFDCConstants.PROD)) {
				String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
				try {
					String email = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
					mstSfdcAmSaRegion = mstSfdcAmSaRegionRepository.findByAmEmail(email);
					if (mstSfdcAmSaRegion != null && !mstSfdcAmSaRegion.isEmpty()) {
						LOGGER.info("MST SFDC Region " + mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
						updateOpportunityStage.setSalesAdministratorRegion(mstSfdcAmSaRegion.get(0).getSalesAdminRegion());
					} else {
						LOGGER.info("MST SFDC Region is empty for " + email);
						updateOpportunityStage.setSalesAdministratorRegion(null);
					}
				}
				catch (Exception e){
					LOGGER.info("MST SFDC Sales Region :: Email is not available");
				}
			}
		}

		String request = Utils.convertObjectToJson(updateOpportunityStage);
		LOGGER.info("Input for updating the opty Status {}", request);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateOpty, request,
				StringUtils.isNotBlank(optyId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.UPDATE_OPTY, seqId,null,CommonConstants.BDEACTIVATE);
	}

	private String getLeadTimefromOrder(QuoteToLe quoteToLe) throws TclCommonException {
		String leadTime = "";
		LocalDateTime today = LocalDateTime.now();
		String currentDate = Utils.convertTimeStampToString(Timestamp.valueOf(today));
		LOGGER.info("Today's Date: {}",currentDate);

		Order order = orderRepository.findByQuoteAndStatus(quoteToLe.getQuote(),BACTIVE);
		List<OrderNplLink> orderNplLink = orderNplLinkRepository.findByOrderId(order.getId());
		if (!orderNplLink.isEmpty()) {
			LOGGER.info("Calcualting lead time for order {} and link {}", order.getOrderCode(),
					orderNplLink.get(0).getId());

			if (orderNplLink.get(0).getRequestorDate() != null) {
				Date requestorDate = orderNplLink.get(0).getRequestorDate();
				String requestDate = Utils.convertTimeStampToString((Timestamp) requestorDate);
				LOGGER.info("Requestor Date: {}", requestDate);
				leadTime = String.valueOf(Utils.findDifferenceInDays(currentDate, requestDate));
				LOGGER.info("Lead time from requestor date: {}", leadTime);
			} else {
				Date effectiveDate = orderNplLink.get(0).getEffectiveDate();
				if (effectiveDate != null) {
					String effectDate = Utils.convertTimeStampToString((Timestamp) effectiveDate);
					LOGGER.info("Effective Date: {}", effectDate);
					leadTime = String.valueOf(Utils.findDifferenceInDays(currentDate, effectDate));
					LOGGER.info("Lead time from effective date: {}", leadTime);
				}
			}
		}
		return leadTime;
	}

	/**
	 * processUpdateOpportunity -This method is used to update the opportunity
	 *
	 * @param cofSignedDate
	 * @param optyId
	 * @param stage
	 * @throws TclCommonException
	 */
	public void processUpdateOpportunityCloseDropped(String optyId, String stage, QuoteToLe quoteToLe,String dropType,String keyReason,String subReason)
			throws TclCommonException {
		LOGGER.info("Entering Process opty for stage --> {}  and quote to le --->  {}  ",stage,quoteToLe.getQuote().getQuoteCode());
		UpdateOpportunityStage updateOpportunityStage = new UpdateOpportunityStage();
		Integer seqId = null;
		//updateOpportunityStage.setWinLossDropKeyReason(keyReason);
		updateOpportunityStage.setLossReasons(keyReason);
		updateOpportunityStage.setDropReasons(subReason);
		//updateOpportunityStage.setDroppingReason(subReason);
		seqId = getSequenceNumber(SFDCConstants.CLOSED_DROPPED);
		LOGGER.info("seqId :::: {}", seqId);
		updateOpportunityStage.setOpportunityId(optyId);
		updateOpportunityStage.setStageName(dropType);

		if (SFDCConstants.PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			updateOpportunityStage.setCompetitor("NOT APPLICABLE");
			updateOpportunityStage.setQuoteByCompetitor("0");
		}
		//String familyName = getFamilyName(quoteToLe);
		/*
		 * if (familyName != null) { IOmsSfdcInputHandler handler =
		 * factory.getInstance(familyName); if (handler != null) {
		 * handler.getOpportunityUpdate(quoteToLe, updateOpportunityStage); } }
		 */
		if(quoteToLe.getIsAmended()!=null && quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE)) {
			updateOpportunityStage.setType(SFDCConstants.AMENDMENT);
		}
		if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Termination quote being closed dropped {}", quoteToLe.getQuote().getQuoteCode());
			updateOpportunityStage.setRetentionReason(MACDConstants.RETENTION_REASON_CLOSED_DROPPED);
		}
		String request = Utils.convertObjectToJson(updateOpportunityStage);
		LOGGER.info("Input for updating the opty Status {}", request);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateOpty, request,
				StringUtils.isNotBlank(optyId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.UPDATE_OPTY, seqId,null,CommonConstants.BACTIVE);
	}

	private void processCreditCheck(String optyId, QuoteToLe quoteToLe, UpdateOpportunityStage updateOpportunityStage,
									Integer seqId) throws TclCommonException {
		LOGGER.info("In processCreditCheck , pre approved opportunity flag value - {}", quoteToLe.getPreapprovedOpportunityFlag());
		if ((Objects.nonNull(quoteToLe.getPreapprovedOpportunityFlag())
				&& CommonConstants.BDEACTIVATE.equals(quoteToLe.getPreapprovedOpportunityFlag())) && (Objects.isNull(quoteToLe.getVariationApprovedFlag()) ||
				(Objects.nonNull(quoteToLe.getVariationApprovedFlag()) && CommonConstants.BDEACTIVATE.equals(quoteToLe.getVariationApprovedFlag())))) {
			updateOpportunityStage.setIccEnterpriceVoiceProductFlag(SFDCConstants.TRUE);
	//		updateOpportunityStage.setStatusOfCreditControl(CommonConstants.PENDING_CREDIT_ACTION);
			updateOpportunityStage.setNotifyCreditControlTeam(SFDCConstants.TRUE);
			updateOpportunityStage.setIsPreapprovedOpportunity(SFDCConstants.FALSE);
			updateOpportunityStage.setLastApprovedMrcNrc(CommonConstants.EMPTY);
			if (Objects.nonNull(quoteToLe.getVariationApprovedFlag()) && CommonConstants.BDEACTIVATE.equals(quoteToLe.getVariationApprovedFlag()))
				updateOpportunityStage.setDifferentialMRC(quoteToLe.getTpsSfdcApprovedMrc() - quoteToLe.getFinalMrc());
			else
				updateOpportunityStage.setDifferentialMRC(0D);

			SfdcCreditCheckQueryRequest queryRequest = new SfdcCreditCheckQueryRequest();
			queryRequest.setFields(SFDCConstants.CREDIT_CHECK_QUERY_FIELDS);
			queryRequest.setObjectName(SFDCConstants.CREDIT_CHECK_OBJECT_NAME);
			queryRequest.setSourceSystem(SFDCConstants.OPTIMUS.toString());
			queryRequest.setWhereClause(SFDCConstants.CREDIT_CHECK_WHERE_CLAUSE + "'" + optyId + "'");
			queryRequest.setTransactionId(SFDCConstants.OPTIMUS.toString() + quoteToLe.getQuote().getQuoteCode());
			String qRequest = Utils.convertObjectToJson(queryRequest);

			// Query API
			persistSfdcServiceJobCreditCheck(quoteToLe.getQuote().getQuoteCode(), creditCheckQueue, qRequest,
					StringUtils.isNotBlank(optyId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE, SFDCConstants.CREDIT_CHECK_QUERY, seqId,quoteToLe);

		} else {
			updateOpportunityStage.setIccEnterpriceVoiceProductFlag(SFDCConstants.FALSE);
	//		updateOpportunityStage.setStatusOfCreditControl(SFDCConstants.POSITIVE);
			updateOpportunityStage.setCreditLimit(2 * quoteToLe.getFinalMrc());
			updateOpportunityStage.setNotifyCreditControlTeam(SFDCConstants.FALSE);
			updateOpportunityStage.setIsPreapprovedOpportunity(SFDCConstants.TRUE);
			if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) 
					&& !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				Map<String,String> attributes = getQuoteLeAttributes(quoteToLe);
				String differentialMrc = attributes.get(MACDConstants.DIFFERENTIAL_MRC);
				updateOpportunityStage.setLastApprovedMrcNrc(differentialMrc + '/' + String.valueOf(quoteToLe.getFinalNrc()));
			} 
			else {
				updateOpportunityStage.setLastApprovedMrcNrc(String.valueOf(quoteToLe.getFinalMrc()) + '/' + String.valueOf(quoteToLe.getFinalNrc()));
			}
			//updateOpportunityStage.setDifferentialMRC(0D);
			cancelManualCheckQueryIfAny(quoteToLe);
		}
	}

	/**
	 * Method to set partner Attributes in update Opportunity
	 *
	 * @param optyId
	 * @param quoteToLe
	 * @param updateOpportunityStage
	 */
	public void setPartnerAttributesInUpdateOpportunityStage(String optyId, QuoteToLe quoteToLe,
			UpdateOpportunityStage updateOpportunityStage) {
//This have to be overridden
	}

	/**
	 * Set Partner Attributes in Create Opportunity
	 *
	 * @param optyId
	 * @param quoteToLe
	 * @param opportunityBean
	 */
	private void setPartnerAttributesInCreateOpportunityStage(Opportunity opportunity, OpportunityBean opportunityBean) {
		LOGGER.info("Entering method setPartnerAttributesInCreateOpportunityStage with opportunity id ::: {} ", opportunityBean.getName());
		if (Objects.nonNull(opportunity.getOptyClassification()) && SELL_WITH_CLASSIFICATION.equalsIgnoreCase(opportunity.getOptyClassification())) {
			LOGGER.info("Partner's Classification :: {}", opportunity.getOptyClassification());
			EngagementToOpportunity engagementToOpportunity = setPartnerContractingId(opportunity, opportunityBean);
			Engagement engagement = setCustomerContractingId(opportunityBean, engagementToOpportunity);
			setCustomerAccountID18(opportunityBean, engagement);
		} else if (Objects.nonNull(opportunity.getOptyClassification()) && SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(opportunity.getOptyClassification())) {
			LOGGER.info("Partner's Classification :: {}", opportunity.getOptyClassification());
			EngagementToOpportunity engagementToOpportunity = setPartnerContractingId(opportunity, opportunityBean);
			if (Objects.nonNull(opportunity)) {
				opportunityBean.setEndCustomerName(opportunity.getCustomerLeName());
			}
			setPartnerAccountId18(opportunity, opportunityBean);

		}
	}

	/**
	 * Method to set Partner SFDC AccountID
	 *
	 * @param opportunity
	 * @param opportunityBean
	 */
	public void setPartnerAccountId18(Opportunity opportunity, OpportunityBean opportunityBean) {
		//Override
	}

	/**
	 * To set Customer SFDC Account ID
	 *
	 * @param opportunityBean
	 * @param engagement
	 */
	public void setCustomerAccountID18(OpportunityBean opportunityBean, Engagement engagement) {
	//Override
	}

	/**
	 * To set customer contracting id
	 *
	 * @param opportunityBean
	 * @param engagementToOpportunity
	 * @return
	 */
	public Engagement setCustomerContractingId(OpportunityBean opportunityBean, EngagementToOpportunity engagementToOpportunity) {
		Engagement engagement = engagementToOpportunity.getEngagement();
		//Override
		return engagement;
	}

	/**
	 * To set Partner contracting Id
	 *
	 * @param opportunity
	 * @param opportunityBean
	 * @return
	 */
	public EngagementToOpportunity setPartnerContractingId(Opportunity opportunity, OpportunityBean opportunityBean) {
		EngagementToOpportunity engagementToOpportunity = engagementOpportunityRepository.findByOpportunityCode(opportunity.getUuid());
		//override
		return engagementToOpportunity;
	}

	/**
	 * processUpdateOrderOpportunity
	 *
	 * @param cofSignedDate
	 * @param optyId
	 * @param stage
	 * @param orderToLe
	 * @throws TclCommonException
	 */

	public void processUpdateOrderOpportunity(Date cofSignedDate, String optyId, String stage, OrderToLe orderToLe)
			throws TclCommonException {
		UpdateOpportunityStage updateOpportunityStage = new UpdateOpportunityStage();
		if (stage.equals(SFDCConstants.CLOSED_WON_COF_RECI.toString())) {
			updateOpportunityStage.setCofSignedDate(DateUtil.convertDateToString(cofSignedDate));
			//updateOpportunityStage.setSalesAdministratorRegion(SFDCConstants.INDIA_WHOLESALE.toString());
		//	updateOpportunityStage.setStatusOfCreditControl(SFDCConstants.POSITIVE.toString());
			updateOpportunityStage.setDsPreparedBy(SFDCConstants.CUSTOMER_URGENCY.toString());
			updateOpportunityStage.setSalesAdministrator("None");
		} else if (stage.equals(SFDCConstants.COF_WON_PROCESS_STAGE.toString())) {
			String programManager = null;
			List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
					.findByNameAndIsActive(LeAttributesConstants.PROGRAM_MANAGER.toString(), CommonConstants.BACTIVE);
			for (MstOmsAttribute mstOmsAttribute : mstAttributes) {
				Set<OrdersLeAttributeValue> orderToleAttributes = ordersLeAttributeValueRepository
						.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe);
				for (OrdersLeAttributeValue orderLeAttributeValue : orderToleAttributes) {
					programManager = orderLeAttributeValue.getAttributeValue();
				}
			}
			updateOpportunityStage.setProgramManager(programManager);
		}
		updateOpportunityStage.setOpportunityId(optyId);
		updateOpportunityStage.setStageName(stage);
		String request = Utils.convertObjectToJson(updateOpportunityStage);
		LOGGER.info("Input for updating the opty Status {}", request);
		LOGGER.info("MDC Filter token value in before Queue call processUpdateOrderOpportunity {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		mqUtils.send(sfdcUpdateOpty, request);
	}

	/**
	 * used to process the opportunity response from sfdc end point
	 * processSfdcOpportunityCreateResponse
	 *
	 * @param opBean
	 * @throws TclCommonException
	 */
	public void processSfdcOpportunityCreateResponse(OpportunityResponseBean opBean) throws TclCommonException {

		LOGGER.info("inside processSfdcOpportunityCreateResponse:: {}", opBean.toString());

		String sfdcOpertunityId = opBean.getCustomOptyId();
		if (opBean.getOpportunity() != null && !opBean.isError()) {
			String orderCode = opBean.getOpportunity().getPortalTransactionId();
			LOGGER.info("inside processSfdcOpportunityCreateResponse - orderCode {}, opp id {}", orderCode,
					sfdcOpertunityId);
		//added for 3d maps
		if (!orderCode.contains(SFDCConstants.PRE_MF_FEASIBILITY)) {
			if (StringUtils.isNotBlank(orderCode)) {
				if(orderCode.contains(SFDCConstants.OPTIMUS+SFDCConstants.ORDERLITE)){
					orderCode = orderCode.replace(SFDCConstants.OPTIMUS+SFDCConstants.ORDERLITE, CommonConstants.EMPTY);
				}
				else {
					orderCode = orderCode.replace(SFDCConstants.OPTIMUS, CommonConstants.EMPTY);
				}
			}
			boolean isCancellation=false;
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(orderCode);
			if(orderCode.contains("-C")) {
				LOGGER.info("This opty is a response for the Cancellation");
				isCancellation=true;
				orderCode = orderCode.replace("-C", CommonConstants.EMPTY);
			}
			LOGGER.info("inside processSfdcOpportunityCreateResponse - replaced orderCode {}", orderCode);

			if (!isCancellation) {
				if (Objects.nonNull(opBean) && Objects.nonNull(opBean.getQuoteToLeId()) && isTeamsDRQuote(
						opBean.getQuoteToLeId())) {
					processCreateResponseForTeamsDR(opBean, sfdcOpertunityId, orderCode, quoteLes);
				}else{
					processCreateResponse(opBean, sfdcOpertunityId, orderCode, quoteLes);
				}
			} else {
				processCancellationCreateResponse(opBean, sfdcOpertunityId, orderCode, isCancellation, quoteLes);
			}
		}else {
			LOGGER.info("Entered into 3d maps process opty id respone"+orderCode+"optyid"+sfdcOpertunityId);
			String[] quotedetails = orderCode.split("_");
			String msg = quotedetails[0]; 
			String quoteCode = quotedetails[1]; 
			LOGGER.info("Quote Code***********"+quoteCode);
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
					.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
							SfdcServiceStatus.INPROGRESS.toString(), quoteCode, CREATE_OPTY,
							ThirdPartySource.SFDC.toString());
			LOGGER.info("Size of list"+sfdcServiceJobs.size());
			persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
					Utils.convertObjectToJson(opBean), sfdcOpertunityId);
			LOGGER.info("Persist the status of opty as success to table");
			List<PreMfRequest> preMfRequestList = preMfRequestRepository.findByMfQuoteCode(quoteCode);
				if (!preMfRequestList.isEmpty()) {
					preMfRequestList.stream().forEach(request -> {
						request.setOpportunityId(Integer.parseInt(sfdcOpertunityId));
						preMfRequestRepository.save(request);

					});
				}
		}
		
		} else {
			processErrorCreateResponse(opBean, sfdcOpertunityId);
		}
		quoteToLeRepository.flush();
	}


	private void processErrorCreateResponse(OpportunityResponseBean opBean, String sfdcOpertunityId) {
		String orderCode = opBean.getRefId();
		if (StringUtils.isNotBlank(orderCode)) {
			// added for 3d maps re trigger create opportunity response error
			if (!orderCode.contains(SFDCConstants.PRE_MF_FEASIBILITY)) {
			  orderCode = orderCode.replace(SFDCConstants.OPTIMUS, CommonConstants.EMPTY);
			}
			else {
				if (orderCode.contains(SFDCConstants.PRE_MF_FEASIBILITY)) {
					orderCode = opBean.getRefId();
					String[] quotedetails = orderCode.split("_");
					String quoteCode = quotedetails[1];
					orderCode = quoteCode;
					LOGGER.info("Error in opty id creation 3d maps" + orderCode);
				}
			}
		}
		updateStruckStatus(orderCode);
		List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
				.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
						SfdcServiceStatus.INPROGRESS.toString(), orderCode, CREATE_OPTY,
						ThirdPartySource.SFDC.toString());
		persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(), opBean.getErrorMessage(), sfdcOpertunityId);
	}


	private void processCancellationCreateResponse(OpportunityResponseBean opBean, String sfdcOpertunityId,
			String orderCode, boolean isCancellation, List<QuoteToLe> quoteLes) throws TclCommonException {
		MstOmsAttribute mstOmsAttrubute = getMstAttributeMaster(SFDCConstants.C_SFDC_STAGE, Utils.getSource());
		for (QuoteToLe quoteLe : quoteLes) {
			LOGGER.info("inside processSfdcOpportunityCreateResponse for cancellation - in for loop {}",
					quoteLe.getTpsSfdcOptyId());
			List<QuoteLeAttributeValue> quoteLeAttrvalues = quoteLeAttributeValueRepository
					.findByQuoteToLe_IdAndMstOmsAttribute_Id(quoteLe.getId(), mstOmsAttrubute.getId());
			if (quoteLeAttrvalues.isEmpty()) {
				QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
				quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttrubute);
				quoteLeAttributeValue.setAttributeValue(sfdcOpertunityId);
				quoteLeAttributeValue.setDisplayValue(sfdcOpertunityId);
				quoteLeAttributeValue.setQuoteToLe(quoteLe);
				quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
			}
			processProductServicesForCancellation(quoteLe, sfdcOpertunityId, isCancellation);
		}
		List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
				.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
						SfdcServiceStatus.INPROGRESS.toString(), orderCode+"-C", CREATE_OPTY,
						ThirdPartySource.SFDC.toString());
		persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
				Utils.convertObjectToJson(opBean), sfdcOpertunityId);
		LOGGER.info("Persisting the status of opty as success");
	}


	private void processCreateResponse(OpportunityResponseBean opBean, String sfdcOpertunityId, String orderCode,
			List<QuoteToLe> quoteLes) throws TclCommonException {
		MstOmsAttribute mstOmsAttrubute = getMstAttributeMaster(SFDCConstants.SFDC_STAGE, Utils.getSource());
		LOGGER.info("quoteLes size {}", quoteLes.size());
		for (QuoteToLe quoteLe : quoteLes) {
			LOGGER.info("inside processSfdcOpportunityCreateResponse - in for loop {}", quoteLe.getTpsSfdcOptyId());
			Quote quote=quoteLe.getQuote();
			String quoteCode=quote.getQuoteCode();
			LOGGER.info("Saved the Opty Id {} for orderCode {} : quoteCode {}", sfdcOpertunityId, orderCode,quoteCode);
			persistingQuoteLeAttributes(opBean, mstOmsAttrubute, quoteLe);
			LOGGER.info("SFDC Termination request update in macd detail");
			if (Objects.nonNull(opBean.getOpportunity().getType())
					&& opBean.getOpportunity().getType().equalsIgnoreCase(SFDCConstants.TERMINATION) &&
					Objects.nonNull(quoteLes.get(0).getIsMultiCircuit())&& quoteLes.get(0).getIsMultiCircuit() == 1 &&
					(opBean.getOpportunity().getDummyParentTerminationOpportunity()==null
							|| "false".equalsIgnoreCase(opBean.getOpportunity().getDummyParentTerminationOpportunity()))) {
				LOGGER.info("Termination - inside if loop");
				quoteLe.setStage(MACDConstants.TERMINATION_REQUEST_RECEIVED);
				MacdDetail macdDetail = macdDetailRepository.findByTpsServiceIdAndQuoteToLeId(opBean.getOpportunity().getCurrentCircuitServiceID(), quoteLe.getId());
				if (macdDetail != null) {
					macdDetail.setSfdcStage(MACDConstants.TERMINATION_REQUEST_RECEIVED);
					macdDetail.setTpsSfdcId(sfdcOpertunityId);
					macdDetailRepository.save(macdDetail);
					//quoteLe.setTpsSfdcOptyId(sfdcOpertunityId);
				}
				LOGGER.info(" service id {}", opBean.getOpportunity().getCurrentCircuitServiceID());
				String serviceId=opBean.getOpportunity().getCurrentCircuitServiceID();
				List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteLe);
				LOGGER.info("quoteIllSiteToServices size {}", quoteIllSiteToServices.size());
				for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
					quoteIllSiteToServiceRepository.updateTpsSfdcOptyId(sfdcOpertunityId,quoteIllSiteToService.getId());
					LOGGER.info("saving quote site id {}", quoteIllSiteToService.getId());
				}
				//quoteToLeRepository.save(quoteLe);
			}else {
				quoteLe.setTpsSfdcOptyId(sfdcOpertunityId);
			}
			if (Objects.nonNull(opBean.getOpportunity().getType())
					&& opBean.getOpportunity().getType().equalsIgnoreCase(SFDCConstants.TERMINATION) &&
					Objects.nonNull(quoteLes.get(0).getIsMultiCircuit())&& quoteLes.get(0).getIsMultiCircuit() == 1 &&
					(opBean.getOpportunity().getDummyParentTerminationOpportunity()!=null
							&& "true".equalsIgnoreCase(opBean.getOpportunity().getDummyParentTerminationOpportunity()))) {

				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteLe.getId());
				if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					quoteIllSiteToServiceList.stream().forEach(siteToService -> {
					// Creating child opportunities for each service
						try {
							LOGGER.info("Current service id   : {}",siteToService.getErfServiceInventoryTpsServiceId());
							LOGGER.info("Entering loop to create child opty for termination after parent opty id is created");
							processCreateOptyChildOpty(quoteLe, getFamilyName(quoteLe), siteToService.getErfServiceInventoryTpsServiceId());
						} catch (TclCommonException e) {
							LOGGER.info("Exception when trying to create child opportunity {}", e);
						}
					});
				}
			}
			if (quoteCode!=null && quoteCode.startsWith("GSC")) {
				LOGGER.info("Inside Gsc");
				processProductServicesForGSC(quoteLe, sfdcOpertunityId);
			} else if (quoteCode!=null && quoteCode.startsWith("UCWB")) {
				LOGGER.info("Inside Ucaas");
				// method name to be cross checked with satish and modified for ucaas
				processProductServicesForUCAAS(quoteLe, sfdcOpertunityId);
			} else if (quoteCode!=null && quoteCode.startsWith("IPC")) {
				LOGGER.info("Inside create response for IPC Product");
				processProductServicesForIpc(quoteLe, sfdcOpertunityId);
			} else if(isRenewals(quoteLe)) {
				processProductServicesRenewals(quoteLe, sfdcOpertunityId);
			}else {
				LOGGER.info("inside processSfdcOpportunityCreateResponse - after saving quoteToLe {}",
						quoteLe.getTpsSfdcOptyId());
				LOGGER.info("inside processSfdcOpportunityCreateResponse - before product service create call");
				// added for NDE DUE to EHS id passing issue
				/*
				 * if (!quoteLe.getQuote().getQuoteCode().startsWith("NDE") &&
				 * (opBean.getOpportunity().getDummyParentTerminationOpportunity() ==null ||
				 * (opBean.getOpportunity().getDummyParentTerminationOpportunity() != null &&
				 * opBean.getOpportunity().getDummyParentTerminationOpportunity().
				 * equalsIgnoreCase("false")))) {
				 */
				if (!quoteLe.getQuote().getQuoteCode().startsWith("NDE")) {

					processProductServices(quoteLe, sfdcOpertunityId);
				} else if (quoteLe.getQuote().getQuoteCode().startsWith("NDE") && MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteLe.getQuoteType())) {
					processProductServices(quoteLe, sfdcOpertunityId);
				}

			}
			if (!(Objects.nonNull(opBean.getOpportunity().getType())
					&& opBean.getOpportunity().getType().equalsIgnoreCase(SFDCConstants.TERMINATION))) {
				LOGGER.info("saving the opppty id");
				quoteLe.setTpsSfdcOptyId(sfdcOpertunityId);// Saving the optyId
			}
			quoteToLeRepository.save(quoteLe);

		}
		// Saving to Order if it exists
		List<OrderToLe> orderToLes = orderToLeRepository.findByOrder_OrderCode(orderCode);
		orderToLes.forEach(orderToLe -> {
			orderToLe.setTpsSfdcCopfId(sfdcOpertunityId);
			orderToLeRepository.save(orderToLe);
		});

		Opportunity opportunity = opportunityRepository.findByUuid(orderCode);
		if (Objects.nonNull(opportunity)) {
			opportunity.setTpsOptyId(sfdcOpertunityId);
			opportunityRepository.save(opportunity);
		}
		List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
				.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(SfdcServiceStatus.INPROGRESS.toString(),
						orderCode, CREATE_OPTY, ThirdPartySource.SFDC.toString());
		persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(), Utils.convertObjectToJson(opBean),
				sfdcOpertunityId);
		LOGGER.info("Persisting the status of opty as success");
		processMFTask(quoteLes);
		LOGGER.info("MF task triggered");
	}


	/**
	 * Method to find parent quote to le..
	 *
	 * @param quoteToLes
	 * @return
	 */
	public QuoteToLe findParentQuoteToLe(List<QuoteToLe> quoteToLes) {
		for (QuoteToLe quoteToLe : quoteToLes) {
			List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
			if (!quoteTeamsDRS.isEmpty()) {
				for (QuoteTeamsDR quoteTeamsDR : quoteTeamsDRS) {
					if(Objects.nonNull(quoteTeamsDR.getProfileName())){
						if (quoteTeamsDR.getProfileName().contains(PLAN)) {
							return quoteToLe;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Method to process response for teamsdr.
	 * @param opBean
	 * @param sfdcOpertunityId
	 * @param orderCode
	 * @param quoteLe
	 * @param mstOmsAttribute
	 * @throws TclCommonException
	 */
	private void processCreateResponseForTeamsDR(OpportunityResponseBean opBean, String sfdcOpertunityId, String orderCode,
												 QuoteToLe quoteLe,MstOmsAttribute mstOmsAttribute) {
		quoteLe.setTpsSfdcOptyId(sfdcOpertunityId);
		LOGGER.info("Saved the Opty Id {} for orderCode {} and quote to le :: {}", sfdcOpertunityId, orderCode,quoteLe.getId());
		persistingQuoteLeAttributes(opBean, mstOmsAttribute, quoteLe);

		LOGGER.info("SFDC Termination request update in macd detail");
		if (Objects.nonNull(opBean.getOpportunity().getType())
				&& opBean.getOpportunity().getType().equalsIgnoreCase(SFDCConstants.TERMINATION)) {
			quoteLe.setStage(MACDConstants.TERMINATION_REQUEST_RECEIVED);
			MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteLe.getId());
			if (macdDetail != null) {
				macdDetail.setSfdcStage(MACDConstants.TERMINATION_REQUEST_RECEIVED);
				macdDetail.setTpsSfdcId(sfdcOpertunityId);
				macdDetailRepository.save(macdDetail);
			}
			quoteToLeRepository.save(quoteLe);
		}
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quoteLe.getQuote());
		QuoteToLe parentQuoteToLe = findParentQuoteToLe(quoteToLes);
		if(parentQuoteToLe.getId().equals(quoteLe.getId())){
			quoteLe.setTpsSfdcOptyId(sfdcOpertunityId);
		}else{
			quoteLe.setTpsSfdcOptyId(sfdcOpertunityId);
			quoteLe.setTpsSfdcParentOptyId(Integer.valueOf(parentQuoteToLe.getTpsSfdcOptyId()));
		}
		// Creating prod services for opty here.
		processProductServicesForTeamsDR(quoteLe,sfdcOpertunityId);
		quoteToLeRepository.save(quoteLe);
	}

	/**
	 * Method to get product solutions for teamsdr.
	 * @param quoteToLe
	 * @return
	 */
	private List<ProductSolution> getTeamsDRSolutions(QuoteToLe quoteToLe){
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quoteToLe.getQuote());
		QuoteToLe parentQuoteToLe = findParentQuoteToLe(quoteToLes);
		List<ProductSolution> teamsAndGscSolutions = new ArrayList<>();
		List<QuoteToLeProductFamily> quoteToLeProdFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());

		// For teamsdr solutions
		Optional.ofNullable(quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId()))
				.orElse(Collections.emptyList())
				.stream()
				.filter(quoteTeamsDR -> Objects.isNull(quoteTeamsDR.getServiceName()) ||
						MS_LICENSE.equals(quoteTeamsDR.getServiceName()))
				.forEach(quoteTeamsDR -> {
					if(Objects.nonNull(quoteTeamsDR.getProductSolution()) &&
							!(MS_LICENSE.equals(quoteTeamsDR.getServiceName()) &&
									parentQuoteToLe.getId().equals(quoteToLe.getId()))){
						teamsAndGscSolutions.add(quoteTeamsDR.getProductSolution());
					}
				});


		List<ProductSolution> gscSolutions = new ArrayList<>();
		AtomicReference<QuoteToLeProductFamily> gscQtlePf = new AtomicReference<>();

		// For voice solutions.
		Optional.ofNullable(quoteToLeProdFamilies).orElse(Collections.emptyList())
				.stream()
				.filter(quoteToLeProductFamily -> GSIP.equals(quoteToLeProductFamily.getMstProductFamily().getName()))
				.map(quoteToLeProductFamily -> {
					gscQtlePf.set(quoteToLeProductFamily);
					return productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily);
				})
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.filter(productSolution -> Objects.nonNull(productSolution.getTpsSfdcProductName()))
				.findAny()
				.ifPresent(gscSolutions::add);

		if(gscSolutions.isEmpty() && Objects.nonNull(gscQtlePf.get())){
			productSolutionRepository.findByQuoteToLeProductFamily(gscQtlePf.get())
					.stream()
					.findAny()
					.ifPresent(gscSolutions::add);
		}
		teamsAndGscSolutions.addAll(gscSolutions);
		return teamsAndGscSolutions;
	}

	/**
	 * Method to create product services for teamsdr.
	 *
	 * @param quoteToLe
	 * @param sfdcOpportunityId
	 */
	public void processProductServicesForTeamsDR(QuoteToLe quoteToLe, String sfdcOpportunityId) {
		if(Objects.nonNull(quoteToLe.getTpsSfdcOptyId())){
			List<ProductSolution> productSolutions = getTeamsDRSolutions(quoteToLe);
			productSolutions.forEach(productSolution -> {
				try {
					productSolution.setTpsSfdcProductName("");
					productSolutionRepository.save(productSolution);
					LOGGER.info("TeamsDR processProductServicesForTeamsDR product solution id :: {}",productSolution.getId());
					processProductServiceForSolution(quoteToLe,productSolution,sfdcOpportunityId);
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			});
		}
	}

	/**
	 * Method to update teamsdr product services.
	 * @param quoteToLe
	 */
	public void processUpdateProductForTeamsDR(QuoteToLe quoteToLe,ProductSolution productSolution){
		if(Objects.nonNull(quoteToLe.getTpsSfdcOptyId())){
			List<ProductSolution> productSolutions = new ArrayList<>();
			if(Objects.nonNull(productSolution)){
				productSolutions.add(productSolution);
			}else{
				productSolutions.addAll(getTeamsDRSolutions(quoteToLe));
			}
			productSolutions.forEach(solution -> {
				if(Objects.nonNull(solution.getTpsSfdcProductName())){
					ProductServiceBean productServiceBean = null;
					String request = null;
					IOmsSfdcInputHandler handler = factory.getInstance(MICROSOFT_CLOUD_SOLUTIONS);
					if (handler != null) {
						try {
							productServiceBean = handler.updateProductServiceInput(quoteToLe,productServiceBean,solution);
							request = Utils.convertObjectToJson(productServiceBean);
						} catch (TclCommonException e) {
							e.printStackTrace();
						}
					}
					LOGGER.info("Input for updating the product Details {}", request);
					persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
							StringUtils.isNotBlank(solution.getTpsSfdcProductId()) ? CommonConstants.BACTIVE
									: CommonConstants.BDEACTIVATE,
							SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT));
				}
			});
		}
	}

	private void processMFTask(List<QuoteToLe> quoteLes) {
		
		//Override
	}
	
	private void persistingQuoteLeAttributes(OpportunityResponseBean opBean, MstOmsAttribute mstOmsAttrubute,
			QuoteToLe quoteLe) {
		try {
			LOGGER.info("Parsing towards the quote to le attributes table and getting the sfdc stage for quotele {}",
					quoteLe.getId());
			Set<QuoteLeAttributeValue> quoteLeAttrvalues = quoteLe.getQuoteLeAttributeValues();
			boolean flag = false;
			for (QuoteLeAttributeValue quoteLeAttrvalue : quoteLeAttrvalues) {
				if (quoteLeAttrvalue.getMstOmsAttribute().getName().equals(SFDCConstants.SFDC_STAGE)) {
					flag = true;
					LOGGER.info("Already the Sfdc Stage is present so making flag as true for {}", quoteLe.getId());
					break;
				}
			}
			if (!flag) {
				LOGGER.info("Since the SFDC stage is not present saving the stage in quote to le {}", quoteLe.getId());
				QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
				quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttrubute);
				quoteLeAttributeValue.setAttributeValue(opBean.getOpportunity().getStageName());
				quoteLeAttributeValue.setDisplayValue(opBean.getOpportunity().getStageName());
				quoteLeAttributeValue.setQuoteToLe(quoteLe);
				quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
			}
		} catch (Exception e) {
			LOGGER.error("Error in saving quote to le ", e);
		}
	}

	/**
	 * persistSfdcJobResponse
	 *
	 * @param opBean
	 * @param sfdcServiceJobs
	 */
	public void persistSfdcJobResponse(List<ThirdPartyServiceJob> sfdcServiceJobs, String serviceStatus,
									   String responsePayload, String optyId) {
		for (ThirdPartyServiceJob sfdcServiceJob : sfdcServiceJobs) {
			LOGGER.info("sfdc service job id {}", sfdcServiceJob.getId());
			sfdcServiceJob.setServiceStatus(serviceStatus);
			sfdcServiceJob.setResponsePayload(responsePayload);
			sfdcServiceJob.setUpdatedBy("admin");
			sfdcServiceJob.setUpdatedTime(new Date());
			//sfdcServiceJob.setIsComplete(CommonConstants.BACTIVE);
			sfdcServiceJob.setTpsId(optyId);
			thirdPartyServiceJobsRepository.save(sfdcServiceJob);
		}
	}

	/**
	 * used to process the product service from sfdc reponse
	 * processSfdcProductService
	 *
	 * @param productServicesResponseBean
	 * @throws TclCommonException
	 */
	@Transactional
	public void processSfdcProductService(ProductServicesResponseBean productServicesResponseBean)
			throws TclCommonException {
		if (productServicesResponseBean.getProductsservices() != null
				&& !productServicesResponseBean.getProductsservices().isEmpty()
				&& !productServicesResponseBean.isError()) {
			String prodServiceId = null;
			String productServiceName = productServicesResponseBean.getProductId();
			String productType = null;
			for (ProductsserviceResponse prodService : productServicesResponseBean.getProductsservices()) {
				prodServiceId = prodService.getId();
				productType = prodService.getProductType();
			}
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			if (quoteLes.isEmpty()) {
				if (productServicesResponseBean.getQuoteToLeId() != null) {
					Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository
							.findById(productServicesResponseBean.getQuoteToLeId());
					if (quoteToLeEntity.isPresent()) {
						LOGGER.warn("Not Saved the SfdcId , so using the quote to Le Id and processing the same");
						quoteLes = new ArrayList<>();
						quoteToLeEntity.get().setTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
						quoteToLeRepository.save(quoteToLeEntity.get());
						quoteLes.add(quoteToLeEntity.get());
					}

				}else {
					List<ThirdPartyServiceJob> tpsJobs=thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(ThirdPartySource.SFDC.toString(), CREATE_OPTY, productServicesResponseBean.getSfdcid());
					for (ThirdPartyServiceJob thirdPartyServiceJob : tpsJobs) {
						String quoteCode=thirdPartyServiceJob.getRefId();
						Quote quote=quoteRepository.findByQuoteCode(quoteCode);
						if(quote!=null) {
							for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
								LOGGER.warn("Not Saved the SfdcId , so using the quote to Le Id and processing the same");
								quoteLes = new ArrayList<>();
								quoteLe.setTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
								quoteToLeRepository.save(quoteLe);
								quoteLes.add(quoteLe);
								break;
							}
						}
						
					}
				}
			}else {
				List<ThirdPartyServiceJob> tpsJobs=thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(ThirdPartySource.SFDC.toString(), CREATE_OPTY, productServicesResponseBean.getSfdcid());
				for (ThirdPartyServiceJob thirdPartyServiceJob : tpsJobs) {
					String quoteCode=thirdPartyServiceJob.getRefId();
					Quote quote=quoteRepository.findByQuoteCode(quoteCode);
					if(quote!=null) {
						for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
							LOGGER.warn("Not Saved the SfdcId , so using the quote to Le Id and processing the same");
							quoteLes = new ArrayList<>();
							quoteLe.setTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
							quoteToLeRepository.save(quoteLe);
							quoteLes.add(quoteLe);
							break;
						}
					}
					
				}
			
			}
			List<ProductSolution> productSolutions = null;

			if (Objects.nonNull(quoteLes) && !quoteLes.isEmpty() 
					&& (isGscQuote(quoteLes.stream().findFirst().get()) || isUcaasQuote(quoteLes.stream().findFirst().get()) 
							|| isIpcQuote(quoteLes.stream().findFirst().get())) && !isTeamsDRQuote(quoteLes.get(0).getId())) {

				LOGGER.info("Finding product solutions find by quote code");
				productSolutions = productSolutionRepository
						.findByReferenceCode(quoteLes.stream().findFirst().get().getQuote().getQuoteCode());
			}else {
				LOGGER.info("Finding product solutions find by product solution code :: {}",
						productServicesResponseBean.getProductSolutionCode());
				productSolutions = productSolutionRepository
						.findBySolutionCode(productServicesResponseBean.getProductSolutionCode());
			}
			if (!productSolutions.isEmpty()) {
				LOGGER.info("Saving product Service id and service name in product solutions");

				//For ucaas
				if(isUcaasQuote(quoteLes.stream().findFirst().get())){
					for (ProductSolution productSolution : productSolutions) {
						if(productType.contains(SFDCConstants.WebEx)){
							if(productSolution.getProductProfileData().contains(CommonConstants.WEBEX)){
								productSolution.setTpsSfdcProductId(prodServiceId);
								productSolution.setTpsSfdcProductName(productServiceName);
								productSolutionRepository.save(productSolution);
							}
						}else{
							if(!productSolution.getProductProfileData().contains(CommonConstants.WEBEX)){
								productSolution.setTpsSfdcProductId(prodServiceId);
								productSolution.setTpsSfdcProductName(productServiceName);
								productSolutionRepository.save(productSolution);
							}
						}
					}
				}  else{
					LOGGER.info("Inside create product response - non ucaas for opty id {}",productServicesResponseBean.getSfdcid());
					for (ProductSolution productSolution : productSolutions) {
						productSolution.setTpsSfdcProductId(prodServiceId);
						productSolution.setTpsSfdcProductName(productServiceName);
						LOGGER.info("product solution id {}, sfdc product id {}, sfdc product name {}",
								productSolution.getId(), prodServiceId, productServiceName);
						productSolutionRepository.save(productSolution);
					}
				}
			} else {
				LOGGER.info("Finding quote to le by tps sfdc opty id {} to save in third party service job", productServicesResponseBean.getSfdcid());
				quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
				// for teamsdr
				if(Objects.nonNull(productServicesResponseBean) && Objects.nonNull(productServicesResponseBean.getParentQuoteToLeId())){
					quoteLes = checkForEmptyQuoteToLeTeamsDR(quoteLes,productServicesResponseBean.getParentQuoteToLeId());
				}
				for (QuoteToLe quoteToLe : quoteLes) {
					LOGGER.info("Finding sfdc jobs with service type Delete product");
					String quoteCode = quoteToLe.getQuote().getQuoteCode();
					if(quoteCode.startsWith("UCDR")){
						processDeleteProductForTeamsDR(quoteToLe,productServicesResponseBean,prodServiceId,quoteCode);
					}else{
						List<ThirdPartyServiceJob> sfdcJobs = thirdPartyServiceJobsRepository
								.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
										SfdcServiceStatus.NEW.toString(), quoteToLe.getQuote().getQuoteCode(),
										SFDCConstants.DELETE_PRODUCT, ThirdPartySource.SFDC.toString());
						for (ThirdPartyServiceJob sfdcServiceJob : sfdcJobs) {
							ProductServiceBean productServiceBean = (ProductServiceBean) Utils
									.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
							if (productServiceBean.getProductSolutionCode()
									.equals(productServicesResponseBean.getProductSolutionCode())) {
								LOGGER.info("Save Request payload in product service job ");
								List<String> prodIds = new ArrayList<>();
								prodIds.add(prodServiceId);
								productServiceBean.setProductIds(prodIds);
								sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productServiceBean));
								thirdPartyServiceJobsRepository.save(sfdcServiceJob);
								break;
							}
						}
					}
				}
			}
			quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			// for teamsdr
			if(Objects.nonNull(productServicesResponseBean) && Objects.nonNull(productServicesResponseBean.getParentQuoteToLeId())){
				quoteLes = checkForEmptyQuoteToLeTeamsDR(quoteLes,productServicesResponseBean.getParentQuoteToLeId());
			}
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			for (QuoteToLe quoteToLe : quoteLes) {
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteToLe.getQuote().getQuoteCode(), status,
								ThirdPartySource.SFDC.toString());
				if(isUcaasQuote(quoteToLe)){
					LOGGER.info("Inside Ucaas");
					persistSfdcJobResponseForUcaas(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
							Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid(),productType);
				}else{
					LOGGER.info("Inside non ucaas for opty id {}", productServicesResponseBean.getSfdcid());
					persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
							Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
				}
			}
		} else if (!productServicesResponseBean.isError()) {
			LOGGER.info("Entering - product service response when not in error ");
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			// For teamsdr
			if(Objects.nonNull(productServicesResponseBean.getParentTpsSfdcOptyId()) && quoteLes.isEmpty()){
				quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getParentTpsSfdcOptyId());
			}
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			for (QuoteToLe quoteToLe : quoteLes) {
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteToLe.getQuote().getQuoteCode(), status,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
			}

		} else {
			LOGGER.info("Entering if error");
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			// For teamsdr
			if(Objects.nonNull(productServicesResponseBean.getParentTpsSfdcOptyId()) && quoteLes.isEmpty()){
				quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getParentTpsSfdcOptyId());
			}
			for (QuoteToLe quoteToLe : quoteLes) {
				String orderCode = quoteToLe.getQuote().getQuoteCode();
				updateStruckStatus(orderCode);
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), orderCode, status,
								ThirdPartySource.SFDC.toString());
				if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
					persistSfdcJobResponseForCancellation(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
							productServicesResponseBean.getErrorMessage(), productServicesResponseBean.getSfdcid());
				} else {
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
						productServicesResponseBean.getErrorMessage(), productServicesResponseBean.getSfdcid());
				}
			}
		}
	}

	/**
	 * Method to check for empty quote to le
	 * and fetch data for quote to le ..
	 * For teamsdr
	 * @param quoteToLes
	 * @param parentQuoteToLeId
	 * @return
	 */
	private List<QuoteToLe> checkForEmptyQuoteToLeTeamsDR(List<QuoteToLe> quoteToLes,
														  Integer parentQuoteToLeId){
		if(Objects.isNull(quoteToLes) || quoteToLes.isEmpty()){
			// For teamsdr with multile..
			if(Objects.nonNull(parentQuoteToLeId)){
				quoteToLes = Collections.singletonList(quoteToLeRepository
						.findById(parentQuoteToLeId).get());
			}
		}
		return quoteToLes;
	}
	
	private void persistSfdcJobResponseForCancellation(List<ThirdPartyServiceJob> sfdcServiceJobs, String serviceStatus,
			String responsePayload, String optyId) throws TclCommonException {
		for (ThirdPartyServiceJob sfdcServiceJob : sfdcServiceJobs) {
			sfdcServiceJob.setServiceStatus(serviceStatus);
			sfdcServiceJob.setResponsePayload(responsePayload);
			sfdcServiceJob.setUpdatedBy("admin");
			sfdcServiceJob.setUpdatedTime(new Date());
			ProductServiceBean requestBean = (ProductServiceBean) Utils.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
			if(SFDCConstants.UPDATE_PRODUCT.equalsIgnoreCase(sfdcServiceJob.getServiceType()) && requestBean != null) {
				if(requestBean.getProductId() != null && requestBean.getProductName() != null) {
					sfdcServiceJob.setIsComplete(CommonConstants.BACTIVE);
			} else {
				sfdcServiceJob.setIsComplete(CommonConstants.BDEACTIVATE);
			}
			}
			sfdcServiceJob.setTpsId(optyId);
			thirdPartyServiceJobsRepository.save(sfdcServiceJob);
		}

	}

	@Transactional
	public void processSfdcProductServiceTermination(ProductServicesResponseBean productServicesResponseBean)
			throws TclCommonException {
		if (productServicesResponseBean.getProductsservices() != null
				&& !productServicesResponseBean.getProductsservices().isEmpty()
				&& !productServicesResponseBean.isError()) {
			String prodServiceId = null;
			String productServiceName = productServicesResponseBean.getProductId();
			for (ProductsserviceResponse prodService : productServicesResponseBean.getProductsservices()) {
				prodServiceId = prodService.getId();
			}
				LOGGER.info("Finding product solutions find by product solution code");
				String quoteCodeRaw=productServicesResponseBean.getProductSolutionCode().replaceAll("TERM-", "").trim();
				String splitter[]=quoteCodeRaw.split("--");
				String quoteCode=splitter[0];
				String serviceId=splitter[1];
				LOGGER.info("Finding quote to le by tps sfdc opty id {} to save in third party service job", productServicesResponseBean.getSfdcid());
				List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
				for (QuoteToLe quoteToLe : quoteLes) {
					List<QuoteIllSiteToService> quoteIllSiteToServices= quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
					for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
						quoteIllSiteToServiceRepository.updateTpsSfdcProdIdAndTpsSfdcProdName(prodServiceId,productServiceName,quoteIllSiteToService.getId());
					}
				}

			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			for (QuoteToLe quoteToLe : quoteLes) {
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteToLe.getQuote().getQuoteCode(), status,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
			}
		} else if (!productServicesResponseBean.isError()) {
			LOGGER.info("Entering - product service response when not in error ");
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			for (QuoteToLe quoteToLe : quoteLes) {
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteToLe.getQuote().getQuoteCode(), status,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
			}

		} else {
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			for (QuoteToLe quoteToLe : quoteLes) {
				String orderCode = quoteToLe.getQuote().getQuoteCode();
				updateStruckStatus(orderCode);
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), orderCode, status,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
						productServicesResponseBean.getErrorMessage(), productServicesResponseBean.getSfdcid());
			}
		}
	}

	@Transactional
	public void processSfdcProductServiceMulticircuit(ProductServicesResponseBean productServicesResponseBean)
			throws TclCommonException {
		if (productServicesResponseBean.getProductsservices() != null
				&& !productServicesResponseBean.getProductsservices().isEmpty()
				&& !productServicesResponseBean.isError()) {
			String prodServiceId = null;
			String productServiceName = productServicesResponseBean.getProductId();
			for (ProductsserviceResponse prodService : productServicesResponseBean.getProductsservices()) {
				prodServiceId = prodService.getId();
			}
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			if (quoteLes.isEmpty()) {
				Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository
						.findById(productServicesResponseBean.getQuoteToLeId());
				if (quoteToLeEntity.isPresent()) {
					LOGGER.warn("Not Saved the SfdcId , so using the quote to Le Id and processing the same");
					quoteLes = new ArrayList<>();
					quoteToLeEntity.get().setTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
					quoteToLeRepository.save(quoteToLeEntity.get());
					quoteLes.add(quoteToLeEntity.get());
				}
			}
			List<ProductSolution> productSolutions = null;
			if (Objects.nonNull(quoteLes) && !quoteLes.isEmpty() && isGscQuote(quoteLes.stream().findFirst().get())) {
				LOGGER.info("Finding product solutions find by quote code");
				productSolutions = productSolutionRepository
						.findByReferenceCode(quoteLes.stream().findFirst().get().getQuote().getQuoteCode());
			} else {
				LOGGER.info("Finding product solutions find by product solution code");
				String quoteCode=productServicesResponseBean.getProductSolutionCode().replaceAll("MLC-", "").trim();
				productSolutions = productSolutionRepository
						.findByReferenceCode(quoteCode);
			}
			if (!productSolutions.isEmpty()) {
				LOGGER.info("Saving product Service id and service name in product solutions");
				for (ProductSolution productSolution : productSolutions) {
					productSolution.setTpsSfdcProductId(prodServiceId);
					productSolution.setTpsSfdcProductName(productServiceName);
					productSolutionRepository.save(productSolution);
				}
			} else {
				LOGGER.info("Finding quote to le by tps sfdc opty id {} to save in third party service job", productServicesResponseBean.getSfdcid());
				quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
				for (QuoteToLe quoteToLe : quoteLes) {
					LOGGER.info("Finding sfdc jobs with service type Delete product");
					List<ThirdPartyServiceJob> sfdcJobs = thirdPartyServiceJobsRepository
							.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
									SfdcServiceStatus.NEW.toString(), quoteToLe.getQuote().getQuoteCode(),
									SFDCConstants.DELETE_PRODUCT, ThirdPartySource.SFDC.toString());
					for (ThirdPartyServiceJob sfdcServiceJob : sfdcJobs) {
						ProductServiceBean productServiceBean = (ProductServiceBean) Utils
								.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
						if (productServiceBean.getProductSolutionCode()
								.equals(productServicesResponseBean.getProductSolutionCode())) {
							LOGGER.info("Save Request payload in product service job ");
							List<String> prodIds = new ArrayList<>();
							prodIds.add(prodServiceId);
							productServiceBean.setProductIds(prodIds);
							sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productServiceBean));
							thirdPartyServiceJobsRepository.save(sfdcServiceJob);
							break;
						}
					}
				}

			}
			quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			for (QuoteToLe quoteToLe : quoteLes) {
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteToLe.getQuote().getQuoteCode(), status,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
			}
		} else if (!productServicesResponseBean.isError()) {
			LOGGER.info("Entering - product service response when not in error ");
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			for (QuoteToLe quoteToLe : quoteLes) {
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteToLe.getQuote().getQuoteCode(), status,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
			}

		} else {
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			for (QuoteToLe quoteToLe : quoteLes) {
				String orderCode = quoteToLe.getQuote().getQuoteCode();
				updateStruckStatus(orderCode);
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), orderCode, status,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
						productServicesResponseBean.getErrorMessage(), productServicesResponseBean.getSfdcid());
			}
		}
	}
	
	@Transactional
	public void processSfdcProductServiceForCancel(ProductServicesResponseBean productServicesResponseBean)
			throws TclCommonException {
		if (productServicesResponseBean.getProductsservices() != null
				&& !productServicesResponseBean.getProductsservices().isEmpty()
				&& !productServicesResponseBean.isError()) {
			String prodServiceId = null;
			String productServiceName = productServicesResponseBean.getProductId();
			for (ProductsserviceResponse prodService : productServicesResponseBean.getProductsservices()) {
				prodServiceId = prodService.getId();
			}
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			LOGGER.info("Response Received {}", productServicesResponseBean);
			List<ThirdPartyServiceJob> createOpty = thirdPartyServiceJobsRepository
					.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY,
							productServicesResponseBean.getSfdcid());
			String refId = null;
			for (ThirdPartyServiceJob thirdPartyServiceJob : createOpty) {
				refId = thirdPartyServiceJob.getRefId();
			}
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
					.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
							SfdcServiceStatus.INPROGRESS.toString(), refId, status, ThirdPartySource.SFDC.toString());
			persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
					Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
		} else {
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			List<ThirdPartyServiceJob> createOpty = thirdPartyServiceJobsRepository
					.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY,
							productServicesResponseBean.getSfdcid());
			String refId = null;
			for (ThirdPartyServiceJob thirdPartyServiceJob : createOpty) {
				refId = thirdPartyServiceJob.getRefId();
			}
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
					.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
							SfdcServiceStatus.INPROGRESS.toString(), refId, status, ThirdPartySource.SFDC.toString());
			persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
					Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
		}
	}
	
	/**
	 *
	 * @param sfdcServiceJobs
	 * @param serviceStatus
	 * @param responsePayload
	 * @param optyId
	 * @param productType
	 */
	public void persistSfdcJobResponseForUcaas(List<ThirdPartyServiceJob> sfdcServiceJobs, String serviceStatus,
											   String responsePayload, String optyId,String productType){

		sfdcServiceJobs.forEach(sfdcServiceJob->{
			if(productType.contains(SFDCConstants.WebEx)){
				if(sfdcServiceJob.getRequestPayload().contains(SFDCConstants.WebEx)){
					LOGGER.info("Inside Webex Product");
					sfdcServiceJob.setResponsePayload(responsePayload);
					sfdcServiceJob.setServiceStatus(serviceStatus);
					sfdcServiceJob.setUpdatedBy("admin");
					sfdcServiceJob.setUpdatedTime(new Date());
					//sfdcServiceJob.setIsComplete(CommonConstants.BACTIVE);
					sfdcServiceJob.setTpsId(optyId);
				}
			}else{
				if(!sfdcServiceJob.getRequestPayload().contains(SFDCConstants.WebEx)){
					LOGGER.info("Inside GSC Product");
					sfdcServiceJob.setResponsePayload(responsePayload);
					sfdcServiceJob.setServiceStatus(serviceStatus);
					sfdcServiceJob.setUpdatedBy("admin");
					sfdcServiceJob.setUpdatedTime(new Date());
					//sfdcServiceJob.setIsComplete(CommonConstants.BACTIVE);
					sfdcServiceJob.setTpsId(optyId);
				}
			}
			thirdPartyServiceJobsRepository.save(sfdcServiceJob);
		});
	}

	/**
	 * used to process createFeasibility response for sfdc
	 *
	 * @param responseBean
	 * @throws TclCommonException
	 */
	public void processSfdcFeasibilityResponse(FeasibilityResponseBean responseBean) throws TclCommonException {

		if (responseBean != null) {
			List<ThirdPartyServiceJob> jobList = new ArrayList<>();
			Optional<ThirdPartyServiceJob> serviceJobOpt = thirdPartyServiceJobsRepository
					.findById(responseBean.getSfdcServiceJobId());
			if (serviceJobOpt.isPresent()) {
				jobList.add(serviceJobOpt.get());
				if (responseBean.isError()) {
					persistSfdcJobResponse(jobList, SfdcServiceStatus.FAILURE.toString(),
							Utils.convertObjectToJson(responseBean), null);
				} else {
					persistSfdcJobResponse(jobList, SfdcServiceStatus.SUCCESS.toString(),
							Utils.convertObjectToJson(responseBean), null);
				}
			}
		}
	}

	/**
	 * used to process the opportunity response from SFDC processSfdcUpdateOpty
	 *
	 * @param opBean
	 * @throws TclCommonException
	 */
	public void processSfdcUpdateOpty(StagingResponseBean opBean) throws TclCommonException {
		String sfdcOpertunityId = opBean.getCustomOptyId();
		if (opBean.getOpportunity() != null && !opBean.isError()) {
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(sfdcOpertunityId);
			if(Objects.nonNull(opBean.getParentQuoteToLeId())){
				quoteLes = checkForEmptyQuoteToLeTeamsDR(quoteLes,opBean.getParentQuoteToLeId());
			}
			if (!quoteLes.isEmpty()) {
				MstOmsAttribute mstOmsAttrubute = processUpdateSfdcQuotes(opBean, quoteLes);
				List<OrderToLe> orderLes = orderToLeRepository.findByTpsSfdcCopfId(sfdcOpertunityId);
				processUpdateSfdcOrders(opBean, mstOmsAttrubute, orderLes);
				for (QuoteToLe quoteToLe : quoteLes) {
					LOGGER.info("Quote to le id for updating opportunity {}", quoteToLe);
					List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
							.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
									SfdcServiceStatus.INPROGRESS.toString(), quoteToLe.getQuote().getQuoteCode(),
									SFDCConstants.UPDATE_OPTY, ThirdPartySource.SFDC.toString());

					LOGGER.info("SFDC Termination request update based on stage movement");
					if (Objects.nonNull(opBean.getOpportunity().getStageName())
							&& opBean.getOpportunity().getStageName().equalsIgnoreCase(SFDCConstants.VERBAL_AGREEMENT_STAGE)
							&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE)) {
						LOGGER.info("Termination - Quote confirmed");
						quoteToLe.setStage(MACDConstants.TERMINATION_CONFIRMED);
						quoteToLeRepository.save(quoteToLe);
					} else if (Objects.nonNull(opBean.getOpportunity().getStageName())
							&& opBean.getOpportunity().getStageName().equalsIgnoreCase(SFDCConstants.COF_WON_STAGE)
							&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE)) {
						LOGGER.info("Termination - Quote Accepted");
						quoteToLe.setStage(MACDConstants.TERMINATION_ACCEPTED);
						quoteToLeRepository.save(quoteToLe);
					}

					LOGGER.info("Convert object to json {}", Utils.convertObjectToJson(opBean));
					LOGGER.info("sfdcOpportunity id {}", sfdcOpertunityId);
					persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
							Utils.convertObjectToJson(opBean), sfdcOpertunityId);
					if(SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.getClassification())
					&& quoteToLe.getQuoteToLeProductFamilies().stream().anyMatch(quoteToLeProductFamily->quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase(GSIP_PRODUCT))
					&& quoteToLe.getQuoteLeAttributeValues().stream().filter(quoteLeAttributeValue -> Objects.nonNull(quoteLeAttributeValue.getAttributeValue())).anyMatch(quoteLeAttributeValue -> quoteLeAttributeValue.getAttributeValue().equalsIgnoreCase(VERBAL_AGREEMENT_STAGE))){
						LOGGER.info("Trigger mail for GSC sell Through of Partner in Verbal agreement stage {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
						notificationService.gscSellThroughVerbalAgreementNotification(quoteToLe.getQuote().getQuoteCode(),quoteToLe.getTpsSfdcOptyId());

					}

					//Moving child termination optys to 80%
					if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())
							&& Objects.nonNull(opBean.getOpportunity()) && "true".equalsIgnoreCase(opBean.getOpportunity().getDummyParentTerminationOpportunity())) {
						List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_IdAndIsDeletedIsNullOrIsDeleted(quoteToLe.getId(), CommonConstants.INACTIVE);

						if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
							LOGGER.info("Entering loop to update child opty for termination to 80%/95% - {}", opBean.getOpportunity().getStageName());
						for(QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServiceList) {
							LOGGER.info("service ids passed {}", quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
							processUpdateOpportunityTermination(new Date(), opBean.getOpportunity().getStageName(), quoteToLe,
									quoteIllSiteToService.getErfServiceInventoryTpsServiceId());

							}
						}

					}

				}
			} else {
				List<ThirdPartyServiceJob> tpsJobs = thirdPartyServiceJobsRepository
						.findByThirdPartySourceAndServiceTypeAndTpsId(ThirdPartySource.SFDC.toString(),
								SFDCConstants.CREATE_OPTY, sfdcOpertunityId);
				for (ThirdPartyServiceJob thirdPartyServiceJob : tpsJobs) {
					String orderCode = thirdPartyServiceJob.getRefId();
					List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
							.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
									SfdcServiceStatus.INPROGRESS.toString(), orderCode, SFDCConstants.UPDATE_OPTY,
									ThirdPartySource.SFDC.toString());
					persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
							Utils.convertObjectToJson(opBean), sfdcOpertunityId);

				}
			}
		} else {
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(sfdcOpertunityId);
			for (QuoteToLe quoteToLe : quoteLes) {
				String orderCode = quoteToLe.getQuote().getQuoteCode();
				updateStruckStatus(orderCode);
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), orderCode, SFDCConstants.UPDATE_OPTY,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(), opBean.getErrorMessage(),
						sfdcOpertunityId);
			}
			if (quoteLes.isEmpty()) {
				List<ThirdPartyServiceJob> tpsJobs = thirdPartyServiceJobsRepository
						.findByThirdPartySourceAndServiceTypeAndTpsId(ThirdPartySource.SFDC.toString(),
								SFDCConstants.CREATE_OPTY, sfdcOpertunityId);
				for (ThirdPartyServiceJob thirdPartyServiceJob : tpsJobs) {
					String orderCode = thirdPartyServiceJob.getRefId();
					updateStruckStatus(orderCode);
					List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
							.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
									SfdcServiceStatus.INPROGRESS.toString(), orderCode, SFDCConstants.UPDATE_OPTY,
									ThirdPartySource.SFDC.toString());
					persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
							opBean.getErrorMessage(), sfdcOpertunityId);

				}
			}
		}

	}

	/**
	 * processUpdateSfdcOrders
	 *
	 * @param opBean
	 * @param mstOmsAttrubute
	 * @param orderLes
	 */
	private void processUpdateSfdcOrders(StagingResponseBean opBean, MstOmsAttribute mstOmsAttrubute,
										 List<OrderToLe> orderLes) {
		for (OrderToLe orderToLe : orderLes) {
			OrdersLeAttributeValue orderLeAttributeValue = null;
			Set<OrdersLeAttributeValue> orderLeAttrvalues = ordersLeAttributeValueRepository
					.findByMstOmsAttributeAndOrderToLe(mstOmsAttrubute, orderToLe);
			if (orderLeAttrvalues.isEmpty()) {
				orderLeAttributeValue = new OrdersLeAttributeValue();
				orderLeAttributeValue.setMstOmsAttribute(mstOmsAttrubute);
				orderLeAttributeValue.setAttributeValue(opBean.getOpportunity().getStageName());
				orderLeAttributeValue.setDisplayValue(opBean.getOpportunity().getStageName());
				orderLeAttributeValue.setOrderToLe(orderToLe);
				ordersLeAttributeValueRepository.save(orderLeAttributeValue);
			} else {
				for (OrdersLeAttributeValue ordersLeAttributeVal : orderLeAttrvalues) {
					ordersLeAttributeVal.setMstOmsAttribute(mstOmsAttrubute);
					ordersLeAttributeVal.setAttributeValue(opBean.getOpportunity().getStageName());
					ordersLeAttributeVal.setDisplayValue(opBean.getOpportunity().getStageName());
					ordersLeAttributeVal.setOrderToLe(orderToLe);
					ordersLeAttributeValueRepository.save(ordersLeAttributeVal);
				}

			}
		}
	}

	/**
	 * To check whether teams dr quote
	 *
	 * @param quoteToLeId
	 * @return
	 */
	public boolean isTeamsDRQuote(Integer quoteToLeId) {
		return quoteToLeRepository
				.findById(quoteToLeId)
				.filter(toLe -> Optional.ofNullable(quoteToLeRepository.findByQuote(toLe.getQuote()))
						.orElse(Collections.emptyList()).stream()
						.map(qtle -> quoteToLeProductFamilyRepository.findByQuoteToLe(qtle.getId()))
						.filter(Objects::nonNull).flatMap(Collection::stream)
						.anyMatch(quoteToLeProductFamily -> MICROSOFT_CLOUD_SOLUTIONS
								.equals(quoteToLeProductFamily.getMstProductFamily().getName())))
				.isPresent();
	}

	private boolean isGscQuote(QuoteToLe quoteToLe) {
		return quoteToLeProductFamilyRepository.findByQuoteToLeIn(Arrays.asList(quoteToLe)).stream()
				.anyMatch(quoteToLeProductFamily -> GSC_PRODUCT_NAME
						.equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName())
						|| GSC_ORDER_PRODUCT_COMPONENT_TYPE
						.equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName()));
	}
	
	private boolean isGscQuoteV1(QuoteToLe quoteToLe) {
		try {
			for (QuoteToLeProductFamily quoteLeProdFamily : quoteToLe.getQuoteToLeProductFamilies()) {
				if (GSC_PRODUCT_NAME.equalsIgnoreCase(quoteLeProdFamily.getMstProductFamily().getName())
						|| GSC_ORDER_PRODUCT_COMPONENT_TYPE
								.equalsIgnoreCase(quoteLeProdFamily.getMstProductFamily().getName())) {
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in verifying", e);
			return false;
		}
		return true;
	}

	private boolean isIpcQuote(QuoteToLe quoteToLe) {
		try {
			for (QuoteToLeProductFamily quoteToLeProdFamily : quoteToLe.getQuoteToLeProductFamilies()) {
				if (IPC_PRODUCT_NAME.equalsIgnoreCase(quoteToLeProdFamily.getMstProductFamily().getName())) {
					return true;
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in getting ipc Quote", e);
		}
		return false;

	}

	/**
	 * Method to check whether ucaas quote.
	 *
	 * @param quoteTole
	 * @return
	 */
	private boolean isUcaasQuote(QuoteToLe quoteToLe) {
		return quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId()).stream()
				.anyMatch(quoteToLeProductFamily -> CommonConstants.UCAAS
						.equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName()));
	}

	/**
	 * processUpdateSfdcQuotes
	 *
	 * @param opBean
	 * @param quoteLes
	 * @return
	 */
	private MstOmsAttribute processUpdateSfdcQuotes(StagingResponseBean opBean, List<QuoteToLe> quoteLes) {
		MstOmsAttribute mstOmsAttrubute = null;
		if (quoteLes != null && !quoteLes.isEmpty()) {
			Optional<User> userSource = userRepository.findById(quoteLes.get(0).getQuote().getCreatedBy());
			String sfdcStage = opBean.getOpportunity().getStageName();
			/*
			 * if (SFDCConstants.CLOSED_WON_COF_RECI.equalsIgnoreCase(sfdcStage) &&
			 * isGscQuote(quoteLes.get(0))) { try { String status =
			 * gscOrderService.retryDownstreamProcessing(quoteLes.get(0)); Integer quoteId =
			 * quoteLes.get(0).getQuote().getId(); LOGGER.info(String.
			 * format("Downstream retry for GSC product family quote id: %s, status: %s",
			 * quoteId, status)); } catch (Exception e) {
			 * LOGGER.warn("Error in retrying downstream for gsc product family {}",
			 * ExceptionUtils.getStackTrace(e)); } }
			 */
			if (userSource.isPresent()) {
				mstOmsAttrubute = getMstAttributeMaster(SFDCConstants.SFDC_STAGE, userSource.get().getUsername());
				for (QuoteToLe quoteToLe : quoteLes) {
					QuoteLeAttributeValue quoteLeAttributeValue = null;
					List<QuoteLeAttributeValue> quoteLeAttrvalues = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttrubute);
					if (quoteLeAttrvalues.isEmpty()) {
						quoteLeAttributeValue = new QuoteLeAttributeValue();
						quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttrubute);
						quoteLeAttributeValue.setAttributeValue(opBean.getOpportunity().getStageName());
						quoteLeAttributeValue.setDisplayValue(opBean.getOpportunity().getStageName());
						quoteLeAttributeValue.setQuoteToLe(quoteToLe);
						quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
					} else {
						for (QuoteLeAttributeValue quoteLeAttributeVal : quoteLeAttrvalues) {
							quoteLeAttributeVal.setMstOmsAttribute(mstOmsAttrubute);
							quoteLeAttributeVal.setAttributeValue(opBean.getOpportunity().getStageName());
							quoteLeAttributeVal.setDisplayValue(opBean.getOpportunity().getStageName());
							quoteLeAttributeVal.setQuoteToLe(quoteToLe);
							quoteLeAttributeValueRepository.save(quoteLeAttributeVal);
						}
					}
				}
			}
		}
		return mstOmsAttrubute;
	}

	/**
	 * used to process the site reponse from SFDC processSfdcSites
	 *
	 * @param siteResponse
	 * @throws TclCommonException
	 */

	public void processSfdcSites(SiteResponseBean siteResponse) throws TclCommonException {
		if (siteResponse != null && siteResponse.getOpportunityId() != null && !siteResponse.isError()) {
			List<MstProductComponent> mstProductComponent = mstProductComponentRepository.findByNameAndStatus(
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), CommonConstants.BACTIVE);
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("IAS",
					CommonConstants.BACTIVE);
			ProductAttributeMaster productAttributeMaster = null;
			List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
					.findByNameAndStatus(SFDCConstants.SFDC_SITE_ID, CommonConstants.BACTIVE);
			if (productAttributeMasters.isEmpty()) {
				productAttributeMaster = new ProductAttributeMaster();
				productAttributeMaster.setCreatedBy(Utils.getSource());
				productAttributeMaster.setCreatedTime(new Date());
				productAttributeMaster.setDescription(SFDCConstants.SFDC_SITE_ID);
				productAttributeMaster.setName(SFDCConstants.SFDC_SITE_ID);
				productAttributeMaster.setStatus(CommonConstants.BACTIVE);
				productAttributeMasterRepository.save(productAttributeMaster);
			} else {
				productAttributeMaster = productAttributeMasters.get(0);
			}
			for (SiteLocationResponse illSite : siteResponse.getSiteLocations()) {
				List<QuoteIllSite> illSiteEntity = illSitesRepository.findBySiteCodeAndStatus(
						illSite.getSiteName().replace(SFDCConstants.OPTIMUS, CommonConstants.EMPTY).trim(),
						CommonConstants.BACTIVE);

				for (QuoteIllSite quoteIllSite : illSiteEntity) {
					List<QuoteProductComponent> quoteComponents = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponent(quoteIllSite.getId(), mstProductComponent.get(0));
					if (quoteComponents.isEmpty()) {
						QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
						quoteProductComponent.setMstProductComponent(mstProductComponent.get(0));
						quoteProductComponent.setMstProductFamily(mstProductFamily);
						quoteProductComponent.setReferenceId(quoteIllSite.getId());
						quoteProductComponentRepository.save(quoteProductComponent);
						processQuoteAttributes(productAttributeMaster, illSite, quoteIllSite, quoteProductComponent);
					}
					for (QuoteProductComponent quoteProductComponent : quoteComponents) {
						processQuoteAttributes(productAttributeMaster, illSite, quoteIllSite, quoteProductComponent);
					}
				}
			}

			if(siteResponse.getProdSolutionCode() != null && siteResponse.getProdSolutionCode().contains("TERM-")) {
				String quoteCodeRaw=siteResponse.getProdSolutionCode().replaceAll("TERM-", "").trim();
				String[] splitter=quoteCodeRaw.split("--");
				String quoteCode=splitter[0];
				String serviceId=splitter[1];
				List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
				LOGGER.info("Inside termination loop processSfdcSites, quoteCode {}, service Id {}", quoteCode, serviceId);
				for (QuoteToLe quoteToLe : quoteLes) {
					List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
							.findByServiceRefIdAndServiceTypeAndRefIdAndThirdPartySourceAndServiceStatus(serviceId,SFDCConstants.UPDATE_SITE,
									quoteToLe.getQuote().getQuoteCode(),
									 ThirdPartySource.SFDC.toString(),SfdcServiceStatus.INPROGRESS.toString());
					persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
							Utils.convertObjectToJson(siteResponse), siteResponse.getOpportunityId());
				}
			} else {
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(siteResponse.getOpportunityId());
			for (QuoteToLe quoteToLe : quoteLes) {
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteToLe.getQuote().getQuoteCode(),
								SFDCConstants.UPDATE_SITE, ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(siteResponse), siteResponse.getOpportunityId());
			}
			}
		} else {
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(siteResponse.getOpportunityId());
			for (QuoteToLe quoteToLe : quoteLes) {
				String orderCode = quoteToLe.getQuote().getQuoteCode();
				updateStruckStatus(orderCode);
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), orderCode, SFDCConstants.UPDATE_SITE,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
						siteResponse.getErrorMessage(), siteResponse.getOpportunityId());
			}
		}
	}
	
	public void processSfdcSitesCancel(SiteResponseBean siteResponse) throws TclCommonException {
		if (siteResponse != null && siteResponse.getOpportunityId() != null && !siteResponse.isError()) {
			List<ThirdPartyServiceJob> createOpty = thirdPartyServiceJobsRepository
					.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY,
							siteResponse.getOpportunityId());
			String refId = null;
			for (ThirdPartyServiceJob thirdPartyServiceJob : createOpty) {
				refId = thirdPartyServiceJob.getRefId();
			}
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
					.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
							SfdcServiceStatus.INPROGRESS.toString(), refId, SFDCConstants.UPDATE_SITE,
							ThirdPartySource.SFDC.toString());
			persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
					Utils.convertObjectToJson(siteResponse), siteResponse.getOpportunityId());
		} else {
			List<ThirdPartyServiceJob> createOpty = thirdPartyServiceJobsRepository
					.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY,
							siteResponse.getOpportunityId());
			String refId = null;
			for (ThirdPartyServiceJob thirdPartyServiceJob : createOpty) {
				refId = thirdPartyServiceJob.getRefId();
			}
			String orderCode = refId;
			updateStruckStatus(orderCode);
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
					.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
							SfdcServiceStatus.INPROGRESS.toString(), orderCode, SFDCConstants.UPDATE_SITE,
							ThirdPartySource.SFDC.toString());
			persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
					siteResponse.getErrorMessage(), siteResponse.getOpportunityId());
		}
	}

	/**
	 * processOrderAttributes
	 *
	 * @param productAttributeMaster
	 * @param illSite
	 * @param orderIllSite
	 * @param orderProductComponent
	 */
	private void processQuoteAttributes(ProductAttributeMaster productAttributeMaster, SiteLocationResponse illSite,
										QuoteIllSite quoteIllSite, QuoteProductComponent quoteProductComponent) {
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, productAttributeMaster);
		for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponentsAttributeValues) {
			quoteProductComponentsAttributeValue.setAttributeValues(illSite.getSiteId());
			quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
		}
		if (quoteProductComponentsAttributeValues.isEmpty()) {
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
			quoteProductComponentsAttributeValue.setAttributeValues(illSite.getSiteId());
			quoteProductComponentsAttributeValue.setDisplayValue(illSite.getSiteId());
			quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
			quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
			quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
		}
	}

	/**
	 * getMstAttributeMaster
	 *
	 * @param propName
	 * @param username
	 * @return
	 */

	private MstOmsAttribute getMstAttributeMaster(String propName, String username) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(propName,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}

		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(username);
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(propName);
			mstOmsAttribute.setDescription(propName);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}

	/**
	 * setSfdcAccId
	 *
	 * @param opportunityBean
	 * @param customerDetails
	 */

    protected void setSfdcAccId(OpportunityBean opportunityBean, CustomerDetailsBean customerDetails) {
        for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
            if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
                opportunityBean.setAccountId(attribute.getValue());
                break;
            }
        }
    }



	protected void setSfdcAccIdForPartner(OpportunityBean opportunityBean, CustomerDetailsBean customerDetails) {
		if(Objects.nonNull(opportunityBean.getOpportunityClassification()) && SELL_WITH_CLASSIFICATION.equalsIgnoreCase(opportunityBean.getOpportunityClassification())) {
			for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
				if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
					opportunityBean.setAccountId(attribute.getValue());
					break;
				}
			}
		}
		else if (Objects.nonNull(opportunityBean.getOpportunityClassification()) && SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(opportunityBean.getOpportunityClassification())) {
			LOGGER.info("Partner's Classification :: {}", opportunityBean.getOpportunityClassification());
				String orderCode=opportunityBean.getPortalTransactionId().replace(SFDCConstants.OPTIMUS,"");
				Opportunity opportunity=opportunityRepository.findByUuid(orderCode);
				opportunityBean.setEndCustomerName(opportunityBean.getEndCustomerName());
				setPartnerAccountId18(opportunity, opportunityBean);

		}
	}




	/**
	 * processCustomerData
	 *
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */
	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		LOGGER.info("Queue call triggered for getting the customer details {}", customerId);
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

	}

	/**
	 * getOrderLeAttributes- This method is used for getting the orderLeAttributtes
	 *
	 * @param orderToLe
	 * @param version
	 * @return
	 */
	private Map<String, String> getQuoteLeAttributes(QuoteToLe quoteToLe) {
		Map<String, String> attributeMap = new HashMap<>();
		Set<QuoteLeAttributeValue> quoteToleAttributes = quoteToLe.getQuoteLeAttributeValues();
		if (quoteToleAttributes != null && !(quoteToleAttributes.isEmpty())) {
			for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
				MstOmsAttribute mstOmsAttributes = quoteLeAttributeValue.getMstOmsAttribute();
				if (mstOmsAttributes != null) {
					attributeMap.put(mstOmsAttributes.getName(), quoteLeAttributeValue.getAttributeValue());
				}
			}
		}
		return attributeMap;
	}

	/**
	 * calculateSolutionPrice
	 */
	private void calculateSolutionPrice(ProductSolution productSolution, ProductServiceBean productServiceBean) {
		productServiceBean.setProductMRC(0D);
		productServiceBean.setProductNRC(0D);

		List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
				CommonConstants.BACTIVE);
		for (QuoteIllSite quoteIllSite : illSites) {
			if (quoteIllSite.getFeasibility() == CommonConstants.BACTIVE) {
				productServiceBean.setProductNRC(productServiceBean.getProductNRC()
						+ (quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0D));
				productServiceBean.setProductMRC(productServiceBean.getProductMRC()
						+ (quoteIllSite.getArc() != null ? quoteIllSite.getArc() / 12 : 0D));
			}
			QuoteToLe quoteToLe = productSolution.getQuoteToLeProductFamily().getQuoteToLe();
			/*
			 * if(Objects.nonNull(quoteToLe.getIsAmended())) { int result =
			 * Byte.compare(quoteToLe.getIsAmended(), BACTIVE); if (result == 0) {
			 * productServiceBean.setProductMRC(0.0-productServiceBean.getProductMRC());
			 * productServiceBean.setProductNRC(0.0-productServiceBean.getProductNRC());
			 * LOGGER.info("For Cancellation MRC is -----> {} NRC is ----> {} ",
			 * productServiceBean.getProductMRC(), productServiceBean.getProductNRC());
			 * 
			 * } }
			 */


		}
		LOGGER.info("Product NRC {} and Product MRC {}", productServiceBean.getProductNRC(), productServiceBean.getProductMRC());
	}

	/**
	 * setSfdcAccountCuid
	 *
	 * @param updateOpportunityStage
	 * @param attributeMapper
	 */
	private void setSfdcAccountCuid(UpdateOpportunityStage updateOpportunityStage,
									Map<String, String> attributeMapper) {
		String accountCuid = attributeMapper.get(LeAttributesConstants.ACCOUNT_CUID.toString());
		updateOpportunityStage.setCustomerContractingId(accountCuid);
		updateOpportunityStage.setAccountCuid(accountCuid);
	}

	/**
	 * getIllsitesBasenOnVersion
	 *
	 * @param productSolution
	 * @param version
	 * @return List<OrderIllSite>
	 */
	protected List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution) {

		return illSitesRepository.findByProductSolutionAndStatus(productSolution, CommonConstants.BACTIVE);
	}

	protected String getFamilyName(QuoteToLe quoteToLe) {
		String quoteCode=quoteToLe.getQuote().getQuoteCode();
		if(quoteCode!=null && quoteCode.startsWith("UCDR")){
			return MICROSOFT_CLOUD_SOLUTIONS;
		}
		else if (quoteCode!=null && quoteCode.startsWith("GSC")) {
			return SFDCConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase();
		}else if(quoteToLe.getQuote().getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
			return IzosdwanCommonConstants.IZOSDWAN_NAME;
		}
		if(quoteCode!=null && quoteCode.startsWith("UCWB")){
			return UCAAS;
		}
		return quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
	}

	/**
	 * getSequenceNumber
	 */

	public Integer getSequenceNumber(String stage) {
		switch (stage) {
			case CREATE_OPTY:
				return 1;
			case CREATE_PRODUCT:
				return 2;
			case UPDATE_PRODUCT:
				return 3;
			case DELETE_PRODUCT:
				return 4;
			case PROPOSAL_SENT:
				return 5;
			case VERBAL_AGREEMENT_STAGE:
				return 6;
			case UPDATE_SITE:
				return 7;
			case CLOSED_WON_COF_RECI:
				return 8;
			case COF_WON_PROCESS_STAGE:
				return 9;
			case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES:
				return 10;
			case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND:
				return 11;
			case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT:
				return 12;
			case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS:
				return 13;
			case TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER:
				return 14;
			case TIGER_SERVICE_TYPE_DOMESTIC_ORDER:
				return 15;
			case CREATE_ENTITY:
				return 16;
			case CREATE_FEASIBILITY:
				return 4;
			case UPDATE_FEASIBILITY:
				return 6;
			case CLOSED_DROPPED:
				return 17;
			case OPEN_CLOSED_BCR:
				return 3;
			case UPDATE_INPROGRESS_BCR:
				return 4;
			case WHOLESALE_ORDER:
				return 1;
			case CHANGE_OUTPULSE_ORDER:
				return 1;
			case CREATE_WAIVER:
				return 5;
			case UPDATE_WAIVER:
				return 7;
			case IDENTIFIED_OPTY_STAGE:
				return 3;
			case CREATE_CONTACT:
				return 18;
			default:
				return 0;
		}
	}

	public SfdcAuditBean getSfdcAudit(Integer quoteId,Boolean fullLog) {
		SfdcAuditBean sfdcAuditBean = new SfdcAuditBean();
		List<SfdcErrorAudit> sfdcErrorAudits=new ArrayList<>();
		List<SfdcFullAudit> sfdcFullAudits=new ArrayList<>();
		sfdcAuditBean.setSfdcFullAudit(sfdcFullAudits);
		sfdcAuditBean.setSfdcErrorAudit(sfdcErrorAudits);
		Quote quotes = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		if (quotes != null) {
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
					.findByRefIdAndThirdPartySource(quotes.getQuoteCode(), ThirdPartySource.SFDC.toString());
			for (ThirdPartyServiceJob sfdcServiceJob : sfdcServiceJobs) {
				SfdcAttr sfdcAttr = new SfdcAttr();
				sfdcAttr.setStageName(sfdcServiceJob.getServiceType());
				sfdcAttr.setStatus(sfdcServiceJob.getServiceStatus());
				sfdcAuditBean.setActiveStatus(sfdcServiceJob.getIsActive()!=null?(sfdcServiceJob.getIsActive().equals(CommonConstants.BACTIVE)?true:false):false);
				Integer seq = sfdcServiceJob.getSeqNum();
				if (seq == 1) {
					sfdcAttr.setPercentage("10 %");
					sfdcAttr.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
					sfdcAuditBean.setCreateOpty(sfdcAttr);
				} else if (seq == 2) {
					sfdcAttr.setPercentage(null);
					sfdcAuditBean.setCreateProduct(sfdcAttr);
				} else if (seq == 5) {
					sfdcAttr.setPercentage("30 %");
					sfdcAttr.setStageName(SFDCConstants.PROPOSAL_SENT);
					sfdcAuditBean.setProposalSent(sfdcAttr);
				} else if (seq == 6) {
					sfdcAttr.setPercentage("80 %");
					sfdcAttr.setStageName(SFDCConstants.VERBAL_AGREEMENT_STAGE);
					sfdcAuditBean.setVerbalAggrement(sfdcAttr);
				} else if (seq == 8) {
					sfdcAttr.setPercentage("95 %");
					sfdcAttr.setStageName(SFDCConstants.CLOSED_WON_COF_RECI);
					sfdcAuditBean.setCofWonReceived(sfdcAttr);
				}

				if (sfdcServiceJob.getServiceStatus().equals(SfdcServiceStatus.FAILURE.toString())
						|| sfdcServiceJob.getServiceStatus().equals(SfdcServiceStatus.STRUCK.toString())) {
					if (sfdcServiceJob.getServiceStatus().equals(SfdcServiceStatus.FAILURE.toString())) {
						SfdcErrorAudit sfdcErrorAudit = new SfdcErrorAudit();
						sfdcErrorAudit.setUpdatedBy(sfdcServiceJob.getUpdatedBy());
						sfdcErrorAudit.setUpdatedTime(sfdcServiceJob.getUpdatedTime());
						sfdcErrorAudit.setErrorResponse(sfdcServiceJob.getResponsePayload());
						sfdcErrorAudit.setRefId(sfdcServiceJob.getRefId());
						sfdcErrorAudit.setStage(sfdcServiceJob.getServiceType());
						sfdcErrorAudit.setTpId(sfdcServiceJob.getId());
						sfdcErrorAudits.add(sfdcErrorAudit);
					}
					sfdcAuditBean.setShowRetrigger(true);
				}
				if(!sfdcAuditBean.getActiveStatus()) {
					LOGGER.info("Since the Job is inactive retrigger will not be there :: {}",sfdcAuditBean.getShowRetrigger());
					sfdcAuditBean.setShowRetrigger(null);
				}
				if (fullLog != null && fullLog) {
					constructFullAudit(sfdcFullAudits, sfdcServiceJob);
				}
			}
		}
		return sfdcAuditBean;
	}

	private void constructFullAudit(List<SfdcFullAudit> sfdcFullAudits, ThirdPartyServiceJob sfdcServiceJob) {
		try {
			String mapperType = getMapperType(sfdcServiceJob.getQueueName());
			String request = null;
			if (StringUtils.isNotBlank(mapperType)) {
				Map<String, String> mapperRequest = new HashMap<>();
				mapperRequest.put("request", sfdcServiceJob.getRequestPayload());
				mapperRequest.put("mapperType", mapperType);
				request = (String) mqUtils.sendAndReceive(sfdcMapperService,
						Utils.convertObjectToJson(mapperRequest));
			} else {
				request = sfdcServiceJob.getRequestPayload();
			}

			sfdcFullAudits.add(
					new SfdcFullAudit(sfdcServiceJob.getId(), request, sfdcServiceJob.getResponsePayload(),
							sfdcServiceJob.getUpdatedBy(), sfdcServiceJob.getUpdatedTime(),sfdcServiceJob.getServiceStatus()));
		} catch (Exception e) {
			LOGGER.error("Error in processing the full log", e);
		}
	}
	
	private String getMapperType(String queueName) {
		if (queueName.equalsIgnoreCase("sfdc_create_opportunity_request")) {
			return "OPPOERTUNITYMAPPER";
		} else if (queueName.equalsIgnoreCase("sfdc_product_service_request")) {
			return "PROCESSMAPPER";
		} else if (queueName.equalsIgnoreCase("sfdc_update_opportunity_request")) {
			return "STAGINGMAPPER";
		} else if (queueName.equalsIgnoreCase("sfdc_site_create_request")) {
			return "SITEMAPPER";
		} else if (queueName.equalsIgnoreCase("sfdc_delete_product_service_request")) {
			return "PRODUCTDELETE";
		}else if(queueName.equalsIgnoreCase("sfdc_update_product_service_request")) {
			return "PRODUCTUPDATE";
		} else if (queueName.equalsIgnoreCase("sfdc_process_cofid")) {
			return "";
		} else if (queueName.equalsIgnoreCase("sfdc_create_bcr_request")) {
			return "";
		} else if (queueName.equalsIgnoreCase("sfdc_create_feasibility_request")) {
			return "FEASIBILITYMAPPER";

		} else if (queueName.equalsIgnoreCase("rabbitmq_partner_opportunity_create")) {
			return "PARTNEROPPOERTUNITYMAPPER";
		} else {
			return "";
		}
	}

	public void triggerSfdc(Integer quoteId) throws TclCommonException {
		Quote quotes = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		if (quotes != null) {
			List<String> statuses = new ArrayList<>();
			statuses.add(SfdcServiceStatus.FAILURE.toString());
			statuses.add(SfdcServiceStatus.STRUCK.toString());
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
					.findByRefIdAndServiceStatusInAndThirdPartySource(quotes.getQuoteCode(), statuses,
							ThirdPartySource.SFDC.toString());
			for (ThirdPartyServiceJob sfdcServiceJob : sfdcServiceJobs) {
				if (sfdcServiceJob.getIsActive().equals(CommonConstants.BACTIVE)) {
					if (sfdcServiceJob.getServiceStatus().equals(SfdcServiceStatus.FAILURE.toString())) {
						sfdcServiceJob.setRetryCount(
								sfdcServiceJob.getRetryCount() != null ? sfdcServiceJob.getRetryCount() + 1 : 1);
					}
					sfdcServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
					sfdcServiceJob.setUpdatedBy(Utils.getSource());
					sfdcServiceJob.setUpdatedTime(new Date());
					thirdPartyServiceJobsRepository.save(sfdcServiceJob);
				}else {
					LOGGER.error("sfdc record for quoteId {} is inactive",quoteId);
					throw new TclCommonException(ExceptionConstants.INACTIVE_SFDC_RECORD, ResponseResource.R_CODE_ERROR);
				}
			}
		}
	}
	
	public void actionSfdc(Integer quoteId, String action ){
		Quote quotes = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		if (quotes != null) {
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
					.findByRefIdAndThirdPartySource(quotes.getQuoteCode(), ThirdPartySource.SFDC.toString());
			LOGGER.info("{} of refId {} by {}", action,quotes.getQuoteCode(), Utils.getSource());
			Byte baction = CommonConstants.BDEACTIVATE;
			if (action.equalsIgnoreCase("activate")) {
				baction = CommonConstants.BACTIVE;
			}
			for (ThirdPartyServiceJob sfdcServiceJob : sfdcServiceJobs) {
				sfdcServiceJob.setIsActive(baction);
				thirdPartyServiceJobsRepository.save(sfdcServiceJob);
			}
		}
	}
	
	public String updateTpsJob(Integer tpId, String refId, Map<String, String> request) {
		LOGGER.info("Thirdparty service job is edited by for tpId {} with refId {} by {}", tpId, refId,
				Utils.getSource());
		Optional<ThirdPartyServiceJob> tpsjs = thirdPartyServiceJobsRepository.findById(tpId);
		LOGGER.info("Input Request is {}", request);
		if (tpsjs.isPresent()) {
			if (!tpsjs.get().getRefId().equals(refId)) {
				return "Invalid RefId";
			}
			if (request.get("request") == null || request.get("complete") == null) {
				return "Invalid Request";
			}
			if (request.get("request") != null) {
				LOGGER.info("Previous Request :::: {}",tpsjs.get().getRequestPayload());
				tpsjs.get().setRequestPayload(request.get("request"));
			}
			if (request.get("complete") != null) {
				LOGGER.info("Previous isComplete :::: {}",tpsjs.get().getIsComplete());
				if (request.get("complete").equals("1")) {
					tpsjs.get().setIsComplete(CommonConstants.BACTIVE);
				} else {
					tpsjs.get().setIsComplete(CommonConstants.BDEACTIVATE);
				}
			}
			tpsjs.get().setUpdatedBy(Utils.getSource());
			tpsjs.get().setUpdatedTime(new Date());
			thirdPartyServiceJobsRepository.save(tpsjs.get());
		} else {
			return "Invalid tp Id";
		}
		return "Success";
	}

	/**
	 * updateStruckStatus
	 */
	public void updateStruckStatus(String refId) {
		thirdPartyServiceJobsRepository.updateServiceStatusByRefIdAndThirdPartySource(refId,
				SfdcServiceStatus.STRUCK.toString(), ThirdPartySource.SFDC.toString());
	}

	/**
	 * processenOpenBcr
	 *
	 * @param CreateRequestV1
	 * @param quote
	 * @throws TclCommonException
	 */
	public void processeOpenBcr(String quoteCode, String opportunityId, String quoteCurrency, String region,
								String custAttribute, String approverLevel, String approverEmail, String closeAttribute)
			throws TclCommonException {
		LOGGER.info("Inside process Open bcr.");
		List<ThirdPartyServiceJob> prevOpenBcr = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceTypeAndThirdPartySource(quoteCode, CommonConstants.CREATE_OPEN_BCR,
						ThirdPartySource.SFDC.toString());
		if (!prevOpenBcr.isEmpty()) {
			int length = prevOpenBcr.size() - 1;
			BCROmsRequest bcrRequest = new BCROmsRequest();
			BCROmsRecord record = new BCROmsRecord();
			BCROptyProperitiesBean optyBean = new BCROptyProperitiesBean();
			try {
				bcrRequest = (BCROmsRequest) Utils.convertJsonToObject(prevOpenBcr.get(length).getRequestPayload(),
						BCROmsRequest.class);
				record = bcrRequest.getBcrOmsRecords().get(0);
				optyBean = record.getBcrOptyProperitiesBeans();
				processeClosedBcr(prevOpenBcr.get(length).getRefId(), record.getOpportunityId(),
						optyBean.getQuoteCurrency(), optyBean.getRegion(), record.getCustAttribute(), approverLevel,
						false, approverEmail, closeAttribute);
			} catch (TclCommonException e) {

				LOGGER.warn("BCR Conversion" + e);
			}
		}

		String openBcr = Utils
				.convertObjectToJson(constructBcrOmsRequest(opportunityId, "India", "Raised", quoteCurrency, null, null,
						custAttribute, null, null, null, null, null, null, null, null, null, null, false));
		LOGGER.info("Persisting Create Open Bcr job.");
		persistSfdcServiceJob(quoteCode, BcrQueue, openBcr, (byte) 0, CommonConstants.CREATE_OPEN_BCR,
				getSequenceNumber(SFDCConstants.OPEN_CLOSED_BCR),null,CommonConstants.BDEACTIVATE);
	}

	private BCROmsRequest constructBcrOmsRequest(String opportunityId, String region, String status,
												 String quoteCurrency, String completionStatus, String pricingCategory, String custAttribute,
												 String approverId, String salesApproverDate, String level1, String level2, String level3,
												 String approvalDate1, String approvalDate2, String approvalDate3, String closeAttribute, String bidId,
												 Boolean isUpdated) {
		LOGGER.info("Inside create bcr request");
		BCROmsRequest bcrOmsRequest = new BCROmsRequest();
		List<BCROmsRecord> bcrOmsRecords = new ArrayList<BCROmsRecord>();
		BCROmsRecord bcrOmsRecord = new BCROmsRecord();
		BCROptyProperitiesBean bcrOptyProperitiesBean = new BCROptyProperitiesBean();
		bcrOptyProperitiesBean.setCompletionStatus(completionStatus);
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(date);
		bcrOptyProperitiesBean.setCustomerSubmissionDate(strDate);
		bcrOptyProperitiesBean.setDateResourceRequiredDate(strDate);
		bcrOptyProperitiesBean.setPricingCategory(pricingCategory);
		bcrOptyProperitiesBean.setQuoteCurrency(quoteCurrency);
		bcrOptyProperitiesBean.setRegion(region);
		bcrOptyProperitiesBean.setStatus(status);
		bcrOptyProperitiesBean.setId(bidId);
		bcrOptyProperitiesBean.setUpdate(isUpdated);
		bcrOptyProperitiesBean.setServiceRequired("Commercial Management Support");
		if (!status.equalsIgnoreCase("Raised")) {
			bcrOptyProperitiesBean.setApproverId(approverId);
			bcrOptyProperitiesBean.setApproverDate(salesApproverDate);
		}
		if (!status.equalsIgnoreCase("In Progress")) {
			bcrOptyProperitiesBean.setApproverId(approverId);
			bcrOptyProperitiesBean.setApproverDate(salesApproverDate);
		} else {
			bcrOptyProperitiesBean.setApproverId(null);
			bcrOptyProperitiesBean.setApproverDate(null);

		}

		bcrOptyProperitiesBean.setCdaCmLevel1ApproverName(level1);
		if (level1 != null) {
			bcrOptyProperitiesBean.setCdaCmLevel1ApprovalDate(approvalDate1);
		} else {
			bcrOptyProperitiesBean.setCdaCmLevel1ApprovalDate(null);
		}
		bcrOptyProperitiesBean.setCdaCmLevel2ApproverName(level2);
		if (level2 != null) {
			bcrOptyProperitiesBean.setCdaCmLevel2ApprovalDate(approvalDate2);
		} else {
			bcrOptyProperitiesBean.setCdaCmLevel2ApprovalDate(null);
		}
		bcrOptyProperitiesBean.setCdaCmLevel3ApproverName(level3);
		if (level3 != null) {
			bcrOptyProperitiesBean.setCdaCmLevel3ApprovalDate(approvalDate3);
		} else {
			bcrOptyProperitiesBean.setCdaCmLevel3ApprovalDate(null);
		}
		String assignToEmail = custAttribute;
		if (closeAttribute != null && custAttribute != null) {
			if (closeAttribute.equalsIgnoreCase(custAttribute)) {
				assignToEmail = custAttribute;
			} else {
				assignToEmail = closeAttribute;
			}

		}
		bcrOptyProperitiesBean.setAssignedToEmailId(assignToEmail);
		bcrOmsRecord.setBcrOptyProperitiesBeans(bcrOptyProperitiesBean);
		bcrOmsRecord.setOpportunityId(opportunityId);
		bcrOmsRecord.setRecordTypeName("Commercial Management");
		// bcrOmsRecord.setCustAttribute(custAttribute);
		bcrOmsRecords.add(bcrOmsRecord);
		bcrOmsRequest.setBcrOmsRecords(bcrOmsRecords);
		LOGGER.info("Bcr request created");
		return bcrOmsRequest;
	}

	/**
	 * processeClosedBcr
	 *
	 * @param CreateRequestV1
	 * @param quote
	 * @throws TclCommonException
	 */
	public void processeClosedBcr(String quoteCode, String opportunityId, String quoteCurrency, String region,
								  String Attribute, String approverLevel, Boolean placeOrder, String approverEmail, String closeAttribute)
			throws TclCommonException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String salesApproverDate = null;
		String salesApproverEmail = null;
		String approverDate1 = null;
		String approverDate2 = null;
		String approverDate3 = null;
		String pricingCategory = null;
		String approver1 = null;
		String approver2 = null;
		String approver3 = null;
		// check the level of approver and set the pricing categoery and approver email
		// and approver date for normal closed (submitted)
		if (approverLevel.equalsIgnoreCase("C0")) {
			pricingCategory = CommonConstants.LEVEL_0;
			salesApproverDate = formatter.format(date);
			salesApproverEmail = approverEmail;
		} else if (approverLevel.equalsIgnoreCase("C1")) {
			pricingCategory = CommonConstants.LEVEL_C1;
			approver1 = approverEmail;
			approverDate1 = formatter.format(date);
		} else if (approverLevel.equalsIgnoreCase("C2")) {
			approver2 = approverEmail;
			pricingCategory = CommonConstants.LEVEL_C2;
			approverDate2 = formatter.format(date);
		} else if (approverLevel.equalsIgnoreCase("C3")) {
			approver3 = approverEmail;
			pricingCategory = CommonConstants.LEVEL_C3;
			approverDate3 = formatter.format(date);
		}
		String completionStatus = null;
		// check the closed bcr whether it is place order or normal closed
		// if place order
		String bidId = null;
		Boolean isUpdate = false;
		if (placeOrder) {

			List<ThirdPartyServiceJob> prevOpenBcr = thirdPartyServiceJobsRepository
					.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatus(quoteCode,
							CommonConstants.CREATE_OPEN_BCR, ThirdPartySource.SFDC.toString(), "SUCCESS");
			List<ThirdPartyServiceJob> prevInProgressBcr = thirdPartyServiceJobsRepository
					.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(quoteCode,
							CommonConstants.CREATE_INPROGRESS_BCR, ThirdPartySource.SFDC.toString(), "SUCCESS");
			// check whether the quote is normal flow or workflow
			// if list empty normal flow
			if (prevOpenBcr.size() == 1 && prevInProgressBcr.isEmpty()) {
				completionStatus = "Won";
				pricingCategory = CommonConstants.LEVEL_0;
				salesApproverEmail = approverEmail;
				salesApproverDate = formatter.format(date);
				String closeBcr = Utils.convertObjectToJson(constructBcrOmsRequest(opportunityId, "India", "Closed",
						quoteCurrency, completionStatus, pricingCategory, Attribute, salesApproverEmail,
						salesApproverDate, approver1, approver2, approver3, approverDate1, approverDate2, approverDate3,
						closeAttribute, bidId, isUpdate));
				persistSfdcServiceJob(quoteCode, BcrQueue, closeBcr, (byte) 0, CommonConstants.CREATE_CLOSED_BCR,
						getSequenceNumber(SFDCConstants.OPEN_CLOSED_BCR),null,CommonConstants.BDEACTIVATE);

			}
			// workflow
			else {
				salesApproverDate = null;
				salesApproverEmail = null;
				completionStatus = "Won";
				approver1 = getQuoteApproveremail(quoteCode, CommonConstants.COMMERCIAL_APPROVER_1);
				approver2 = getQuoteApproveremail(quoteCode, CommonConstants.COMMERCIAL_APPROVER_2);
				approver3 = getQuoteApproveremail(quoteCode, CommonConstants.COMMERCIAL_APPROVER_3);

				if (approver1 != null && approver2 != null && approver3 != null) {
					pricingCategory = CommonConstants.LEVEL_C3;
					approverDate1 = formatter.format(date);
					approverDate2 = formatter.format(date);
					approverDate3 = formatter.format(date);
					closeAttribute = approver1;
				}
				if (approver1 != null && approver2 != null && approver3 == null) {
					pricingCategory = CommonConstants.LEVEL_C2;
					approverDate1 = formatter.format(date);
					approverDate2 = formatter.format(date);
					closeAttribute = approver1;
				}
				if (approver1 != null && approver3 == null && approver2 == null) {
					pricingCategory = CommonConstants.LEVEL_C1;
					approverDate1 = formatter.format(date);
					closeAttribute = approver1;
				}
				if (approver1 == null && approver2 == null && approver3 == null) {
					pricingCategory = CommonConstants.LEVEL_0;
					salesApproverDate = formatter.format(date);
					salesApproverEmail = approverEmail;
				}

				if (prevInProgressBcr != null && !prevInProgressBcr.isEmpty()) {
					BCROmsResponse bcrOmsResponse = (BCROmsResponse) Utils
							.convertJsonToObject(prevInProgressBcr.get(0).getResponsePayload(), BCROmsResponse.class);
					bidId = bcrOmsResponse.getCustomBCRId().get(0).toString();
					isUpdate = true;
				}

				String closeBcr = Utils.convertObjectToJson(constructBcrOmsRequest(opportunityId, "India", "Closed",
						quoteCurrency, completionStatus, pricingCategory, Attribute, salesApproverEmail,
						salesApproverDate, approver1, approver2, approver3, approverDate1, approverDate2, approverDate3,
						closeAttribute, bidId, isUpdate));
				persistSfdcServiceJob(quoteCode, BcrQueue, closeBcr, (byte) 0, CommonConstants.CREATE_UPDATE_BCR,
						getSequenceNumber(SFDCConstants.UPDATE_INPROGRESS_BCR),null,CommonConstants.BDEACTIVATE);

			}

		}
		// if normal closed bcr set the completion status
		else {
			completionStatus = "Submitted";
		}

	}

	/**
	 * processeInprogressBcr
	 *
	 * @param quoteCode
	 * @param quote
	 * @throws TclCommonException
	 */
	public void processeInprogressBcr(String quoteCode) throws TclCommonException {
		List<ThirdPartyServiceJob> prevOpenBcr = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceTypeAndThirdPartySource(quoteCode, CommonConstants.CREATE_OPEN_BCR,
						ThirdPartySource.SFDC.toString());
		if (!prevOpenBcr.isEmpty()) {
			int length = prevOpenBcr.size() - 1;
			BCROmsRequest bcrRequest = new BCROmsRequest();
			BCROmsRecord record = new BCROmsRecord();
			BCROptyProperitiesBean optyBean = new BCROptyProperitiesBean();
			BCROmsResponse response = new BCROmsResponse();
			String bcrResponse = "";
			String Id = "";

			try {
				bcrRequest = (BCROmsRequest) Utils.convertJsonToObject(prevOpenBcr.get(length).getRequestPayload(),
						BCROmsRequest.class);
				record = bcrRequest.getBcrOmsRecords().get(0);
				optyBean = record.getBcrOptyProperitiesBeans();
				bcrResponse = prevOpenBcr.get(length).getResponsePayload();
				if (!StringUtils.isEmpty(bcrResponse)) {
					response = (BCROmsResponse) Utils.convertJsonToObject(prevOpenBcr.get(length).getResponsePayload(),
							BCROmsResponse.class);
					if (response != null && response.getCustomBCRId() != null && !response.getCustomBCRId().isEmpty()
							&& response.getCustomBCRId().get(0) != null
							&& !StringUtils.isEmpty(response.getCustomBCRId().get(0))) {
						Id = response.getCustomBCRId().get(0);
					}
				}
				processeUpdateBcr(quoteCode, Id, optyBean.getRegion(), "In Progress", optyBean.getQuoteCurrency(), null,
						null, null, null, null, null, null, null, null, null, null, true);

			} catch (TclCommonException e) {

				LOGGER.warn("BCR inprogress Conversion" + e.getMessage());
			}
		}

	}

	/**
	 * getQuoteApproverEmail
	 *
	 * @param quotecode
	 * @param level
	 * @throws TclCommonException
	 */
	public String getQuoteApproveremail(String quotecode, String level) throws TclCommonException {
		String approverEmail = null;
		List<MstOmsAttribute> omsattribute = mstOmsAttributeRepository.findByNameAndIsActive(level, (byte) 1);
		Quote quote = quoteRepository.findByQuoteCode(quotecode);
		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quote.getId());
		if (!omsattribute.isEmpty() && !quoteToLe.isEmpty()) {
			List<QuoteLeAttributeValue> attributeValue = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe.get(0), omsattribute.get(0));
			if (!attributeValue.isEmpty() && !StringUtils.isEmpty(attributeValue.get(0).getAttributeValue())) {
				approverEmail = attributeValue.get(0).getAttributeValue();
			}
		}
		return approverEmail;
	}

	/**
	 * processenBcrResponse
	 *
	 * @param CreateRequestV1
	 * @param quote
	 * @throws TclCommonException
	 */
	public void processeBcrResponse(BCROmsResponse response) throws TclCommonException {
		Optional<ThirdPartyServiceJob> thirdPartyServiceJob = thirdPartyServiceJobsRepository
				.findById(response.getTpsId());
		String closeBcr = Utils.convertObjectToJson(response);
		if (thirdPartyServiceJob.isPresent()) {
			thirdPartyServiceJob.get().setResponsePayload(closeBcr);
			thirdPartyServiceJob.get().setIsComplete((byte) 1);
			if (response.getStatus().equalsIgnoreCase(CommonConstants.BCR_SUCCESS)) {
				thirdPartyServiceJob.get().setServiceStatus(CommonConstants.SUCCESS);
			} else {
				thirdPartyServiceJob.get().setServiceStatus(CommonConstants.FAILIURE);
			}
			thirdPartyServiceJob.get().setUpdatedTime(new Date());
			thirdPartyServiceJob.get().setUpdatedBy(Utils.getSource());
			thirdPartyServiceJobsRepository.save(thirdPartyServiceJob.get());
		}

	}

	/**
	 * processeMDMResponse
	 *
	 * @param CreateRequestV1
	 * @param quote
	 * @throws TclCommonException
	 */
	public void processeMDMResponse(MDMOmsResponseBean response) throws TclCommonException {
		Optional<ThirdPartyServiceJob> thirdPartyServiceJob = thirdPartyServiceJobsRepository
				.findById(response.getTpsId());
		String mdmResponse = Utils.convertObjectToJson(response);
		if (thirdPartyServiceJob.isPresent()) {
			thirdPartyServiceJob.get().setResponsePayload(mdmResponse);
			thirdPartyServiceJob.get().setIsComplete((byte) 1);
			if (response.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				thirdPartyServiceJob.get().setServiceStatus(CommonConstants.SUCCESS);
			} else {
				thirdPartyServiceJob.get().setServiceStatus(CommonConstants.FAILIURE);
			}
			thirdPartyServiceJob.get().setUpdatedTime(new Date());
			thirdPartyServiceJob.get().setUpdatedBy(Utils.getSource());
			thirdPartyServiceJobsRepository.save(thirdPartyServiceJob.get());
		}
		// Update contact Id to customer_le_billing_info
		if (response.isAddAddress()) {
			if (response.getContactId() != null) {
				LOGGER.info("BEFORE GOING TO CUSTOMER API MDM to add contact id");
				mqUtils.send(addContactIdtoCustomerBillingInfo, Utils.convertObjectToJson(response));
			}
		}

	}

	/**
	 * processeCopfResponse is used to process copfOmsResponse
	 *
	 * @param response
	 * @throws TclCommonException
	 */
	public void processeCopfResponse(COPFOmsResponse response) throws TclCommonException {
		Optional<ThirdPartyServiceJob> thirdPartyServiceJob = thirdPartyServiceJobsRepository
				.findById(response.getTpsId());
		String copfResponse = Utils.convertObjectToJson(response);
		if (thirdPartyServiceJob.isPresent()) {
			thirdPartyServiceJob.get().setResponsePayload(copfResponse);
			thirdPartyServiceJob.get().setIsComplete((byte) 1);
			if (response.getStatus().equalsIgnoreCase(CommonConstants.BCR_SUCCESS)) {
				thirdPartyServiceJob.get().setServiceStatus(CommonConstants.SUCCESS);
			} else {
				thirdPartyServiceJob.get().setServiceStatus(CommonConstants.FAILIURE);
			}
			thirdPartyServiceJob.get().setUpdatedTime(new Date());
			thirdPartyServiceJob.get().setUpdatedBy(Utils.getSource());
			thirdPartyServiceJobsRepository.save(thirdPartyServiceJob.get());
		}

	}

	private COPFOmsRequest constructCopfOmsRequest(String currencyCode, String optyId, String productServiceId,
			List<LinkCOPFDetails> linkCOPFDetails, String type) {
		COPFOmsRequest copfOmsRequest = new COPFOmsRequest();
		copfOmsRequest.setCurrencyIsoCode(currencyCode);
		copfOmsRequest.setLinkCOPFDetails(linkCOPFDetails);
		copfOmsRequest.setOpportunityId(optyId);
		copfOmsRequest.setProductServiceId(productServiceId);
		if (type.equals("NEW")) {
			copfOmsRequest.setRecordTypeName(CommonConstants.RECORD_TYPE_NAME_NEW_COPF_ID);
		} else {
			copfOmsRequest.setRecordTypeName(CommonConstants.RECORD_TYPE_NAME_MACD_COPF_ID);
		}
		copfOmsRequest.setSystemGeneratingCopfIdC(CommonConstants.M6);
		return copfOmsRequest;

	}

	/**
	 * Process create COPF
	 *
	 * @param CreateRequestV1
	 * @param quote
	 * @throws TclCommonException
	 */
	public void processCreateCopf(String quoteCode, String opportunityId, String quoteCurrency, String productServiceId,
								  List<LinkCOPFDetails> linkCOPFDetails,String type) throws TclCommonException {
		LOGGER.info("------------processCreateCopf---------------------");
		String createCopf = Utils.convertObjectToJson(
				constructCopfOmsRequest(quoteCurrency, opportunityId, productServiceId, linkCOPFDetails,type));
		persistSfdcServiceJob(quoteCode, processCopfId, createCopf, (byte) 0, SfdcServiceTypeConstants.CREATE_COPF_ID,
				0,null,CommonConstants.BDEACTIVATE);

	}

	/**
	 * processeOpenUpadteBcr
	 *
	 * @throws TclCommonException
	 */
	public void processeOpenUpdateBcr(String quoteCode, String opportunityId, String quoteCurrency, String region,
									  String custAttribute, String approverLevel, String approverEmail, String closeAttribute)
			throws TclCommonException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String salesApproverDate = null;
		String salesApproverEmail = null;
		String approverDate1 = null;
		String approverDate2 = null;
		String approverDate3 = null;
		String pricingCategory = null;
		String approver1 = null;
		String approver2 = null;
		String approver3 = null;
		LOGGER.info("Inside process Open bcr.");
		List<ThirdPartyServiceJob> prevInprogressBcr = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceTypeAndThirdPartySource(quoteCode, CommonConstants.CREATE_INPROGRESS_BCR,
						ThirdPartySource.SFDC.toString());
		int length = prevInprogressBcr.size() - 1;
		if (!prevInprogressBcr.isEmpty()) {
			approverEmail = null;
			BCROmsRequest bcrRequest = new BCROmsRequest();
			BCROmsRecord record = new BCROmsRecord();
			BCROmsResponse response = new BCROmsResponse();
			BCROptyProperitiesBean optyBean = new BCROptyProperitiesBean();
			String bcrResponse = "";
			String Id = "";
			try {
				bcrRequest = (BCROmsRequest) Utils
						.convertJsonToObject(prevInprogressBcr.get(length).getRequestPayload(), BCROmsRequest.class);
				record = bcrRequest.getBcrOmsRecords().get(0);
				optyBean = record.getBcrOptyProperitiesBeans();
				if (!StringUtils.isEmpty(bcrResponse)) {
					response = (BCROmsResponse) Utils.convertJsonToObject(
							prevInprogressBcr.get(length).getResponsePayload(), BCROmsResponse.class);
					if (response != null && response.getCustomBCRId() != null && !response.getCustomBCRId().isEmpty()
							&& response.getCustomBCRId().get(0) != null
							&& !StringUtils.isEmpty(response.getCustomBCRId().get(0))) {
						Id = response.getCustomBCRId().get(0);
					}
				}
				salesApproverEmail = null;
				String completionStatus = "Submitted";
				approver1 = getQuoteApproveremail(quoteCode, CommonConstants.COMMERCIAL_APPROVER_1);
				approver2 = getQuoteApproveremail(quoteCode, CommonConstants.COMMERCIAL_APPROVER_2);
				approver3 = getQuoteApproveremail(quoteCode, CommonConstants.COMMERCIAL_APPROVER_3);

				if (approver1 != null && approver2 != null && approver3 != null) {
					pricingCategory = CommonConstants.LEVEL_C3;
					approverDate1 = formatter.format(date);
					approverDate2 = formatter.format(date);
					approverDate3 = formatter.format(date);
					closeAttribute = approver1;
				}
				if (approver1 != null && approver2 != null && approver3 == null) {
					pricingCategory = CommonConstants.LEVEL_C2;
					approverDate1 = formatter.format(date);
					approverDate2 = formatter.format(date);
					closeAttribute = approver1;
				}
				if (approver1 != null && approver3 == null && approver2 == null) {
					pricingCategory = CommonConstants.LEVEL_C1;
					approverDate1 = formatter.format(date);
					closeAttribute = approver1;
				}
				if (approver1 == null && approver2 == null && approver3 == null) {
					pricingCategory = CommonConstants.LEVEL_0;
					salesApproverDate = formatter.format(date);
					salesApproverEmail = approverEmail;

				}

				processeUpdateBcr(prevInprogressBcr.get(length).getRefId(), Id,
						record.getBcrOptyProperitiesBeans().getRegion(), "Closed",
						record.getBcrOptyProperitiesBeans().getQuoteCurrency(), approver1, approver2, approver3,
						approverDate1, approverDate2, approverDate3, closeAttribute, completionStatus,
						salesApproverEmail, salesApproverDate, pricingCategory, false);
			} catch (TclCommonException e) {

				LOGGER.warn("BCR Conversion" + e);
			}

			String openBcr = Utils
					.convertObjectToJson(constructBcrOmsRequest(opportunityId, "India", "Raised", quoteCurrency, null,
							null, custAttribute, null, null, null, null, null, null, null, null, null, null, false));
			LOGGER.info("Persisting Create Open Bcr job.");
			persistSfdcServiceJob(quoteCode, BcrQueue, openBcr, (byte) 0, CommonConstants.CREATE_OPEN_BCR, 5,null,CommonConstants.BDEACTIVATE);
		}

	}

	/**
	 * processeUpadteBcr
	 *
	 * @throws TclCommonException
	 */
	public void processeUpdateBcr(String quotecode, String Id, String region, String status, String quoteCurrency,
								  String level1, String level2, String level3, String approvalDate1, String approvalDate2,
								  String approvalDate3, String closeAttribute, String completeionStatus, String salesApproverEmail,
								  String salesApproverDate, String pricingCategoery, Boolean Open) {
		String closeBcr = null;
		try {
			closeBcr = Utils.convertObjectToJson(constructBcrOmsUpdateRequest(Id, region, status, quoteCurrency, level1,
					level2, level3, approvalDate1, approvalDate2, approvalDate3, closeAttribute, completeionStatus,
					salesApproverEmail, salesApproverDate, pricingCategoery, Open));
		} catch (TclCommonException e) {

			LOGGER.info("ERROR IN conversion" + e);
		}
		if (status.equalsIgnoreCase("In Progress")) {
			persistSfdcServiceJob(quotecode, BcrQueue, closeBcr, (byte) 0, CommonConstants.CREATE_INPROGRESS_BCR,
					getSequenceNumber(SFDCConstants.UPDATE_INPROGRESS_BCR),null,CommonConstants.BDEACTIVATE);
		} else {
			persistSfdcServiceJob(quotecode, BcrQueue, closeBcr, (byte) 0, CommonConstants.CREATE_UPDATE_BCR,
					getSequenceNumber(SFDCConstants.UPDATE_INPROGRESS_BCR),null,CommonConstants.BDEACTIVATE);
		}

	}

	/**
	 * constructBcrOmsUpdateRequest
	 *
	 * @throws TclCommonException
	 */
	private BCROmsRequest constructBcrOmsUpdateRequest(String Id, String region, String status, String quoteCurrency,
													   String level1, String level2, String level3, String approvalDate1, String approvalDate2,
													   String approvalDate3, String closeAttribute, String completionStatus, String salesApproverEmail,
													   String salesApproverDate, String pricingCategoery, Boolean isOpen) {
		LOGGER.info("Inside create bcr request");
		BCROmsRequest bcrOmsRequest = new BCROmsRequest();
		List<BCROmsRecord> bcrOmsRecords = new ArrayList<BCROmsRecord>();
		BCROmsRecord bcrOmsRecord = new BCROmsRecord();
		BCROptyProperitiesBean bcrOptyProperitiesBean = new BCROptyProperitiesBean();
		bcrOptyProperitiesBean.setCompletionStatus(null);
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(date);
		bcrOptyProperitiesBean.setCustomerSubmissionDate(null);
		bcrOptyProperitiesBean.setDateResourceRequiredDate(null);
		bcrOptyProperitiesBean.setPricingCategory(pricingCategoery);
		bcrOptyProperitiesBean.setQuoteCurrency(quoteCurrency);
		bcrOptyProperitiesBean.setRegion(region);
		bcrOptyProperitiesBean.setCompletionStatus(completionStatus);
		bcrOptyProperitiesBean.setStatus(status);
		bcrOptyProperitiesBean.setServiceRequired("Commercial Management Support");
		bcrOptyProperitiesBean.setApproverId(salesApproverEmail);
		bcrOptyProperitiesBean.setApproverDate(salesApproverDate);
		bcrOptyProperitiesBean.setId(Id);
		bcrOptyProperitiesBean.setUpdate(true);
		bcrOptyProperitiesBean.setOpen(isOpen);
		bcrOptyProperitiesBean.setCdaCmLevel1ApproverName(level1);
		if (level1 != null) {
			bcrOptyProperitiesBean.setCdaCmLevel1ApprovalDate(approvalDate1);
		} else {
			bcrOptyProperitiesBean.setCdaCmLevel1ApprovalDate(null);
		}
		bcrOptyProperitiesBean.setCdaCmLevel2ApproverName(level2);
		if (level2 != null) {
			bcrOptyProperitiesBean.setCdaCmLevel2ApprovalDate(approvalDate2);
		} else {
			bcrOptyProperitiesBean.setCdaCmLevel2ApprovalDate(null);
		}
		bcrOptyProperitiesBean.setCdaCmLevel3ApproverName(level3);
		if (level3 != null) {
			bcrOptyProperitiesBean.setCdaCmLevel3ApprovalDate(approvalDate3);
		} else {
			bcrOptyProperitiesBean.setCdaCmLevel3ApprovalDate(null);
		}
		bcrOptyProperitiesBean.setAssignedToEmailId(closeAttribute);
		bcrOmsRecord.setBcrOptyProperitiesBeans(bcrOptyProperitiesBean);
		bcrOmsRecord.setOpportunityId(null);
		bcrOmsRecord.setRecordTypeName("Commercial Management");
		bcrOmsRecords.add(bcrOmsRecord);
		bcrOmsRequest.setBcrOmsRecords(bcrOmsRecords);
		LOGGER.info("Bcr request created");
		return bcrOmsRequest;
	}

	/**
	 * processeBeforeCommercialOpenUpdateBcr
	 *
	 * @throws TclCommonException
	 */


	private void cancelManualCheckQueryIfAny(QuoteToLe quoteToLe) {
		List<String> statuses = new ArrayList<>();
		statuses.add(SfdcServiceStatus.NEW.toString());
		statuses.add(SfdcServiceStatus.INPROGRESS.toString());
		List<ThirdPartyServiceJob> jobsList = thirdPartyServiceJobsRepository.findByRefIdAndServiceStatusInAndThirdPartySource(quoteToLe.getQuote().getQuoteCode(), statuses,
				ThirdPartySource.CREDITCHECK.toString());
		if (!jobsList.isEmpty()) {
			jobsList.stream()
					.forEach(inProgressJob -> {
						inProgressJob.setServiceStatus(SfdcServiceStatus.CANCELLED.toString());
						inProgressJob.setIsComplete(CommonConstants.BACTIVE);
						thirdPartyServiceJobsRepository.save(inProgressJob);
					});
		}

	}


	/**
	 * persistSfdcServiceJob
	 * This method persists the sfdc service job for credit check
	 *
	 * @param opportunityBean
	 * @param quote
	 * @throws TclCommonException
	 */
	public void persistSfdcServiceJobCreditCheck(String refId, String queueName, String payload, Byte isComplete,
												 String serviceType, Integer seqNum,QuoteToLe quoteToLe) {
		List<ThirdPartyServiceJob> jobsInProgress = new ArrayList<>();
		List<ThirdPartyServiceJob> jobsList = thirdPartyServiceJobsRepository.findByRefIdAndThirdPartySource(refId,
				ThirdPartySource.CREDITCHECK.toString());
		if (!jobsList.isEmpty()) {
			jobsInProgress = jobsList.stream()
					.filter(job -> (!SfdcServiceStatus.SUCCESS.toString().equalsIgnoreCase(job.getServiceStatus())
							&& !SfdcServiceStatus.CANCELLED.toString().equalsIgnoreCase(job.getServiceStatus())))
					.collect(Collectors.toList());

			// For teamsdr
			if (isTeamsDRQuote(quoteToLe.getId()) && !jobsInProgress.isEmpty()) {
				if (!jobsInProgress.stream().anyMatch(thirdPartyServiceJob -> {
					try {
						String tpsId;
						SfdcCreditCheckQueryRequest request =
								Utils.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(),
										SfdcCreditCheckQueryRequest.class);
						if (Objects.nonNull(request.getWhereClause())) {
							LOGGER.info("Where Clause data :: {}", request.getWhereClause());
							String[] val = request.getWhereClause().split("=");
							if (val.length > 0) {
								tpsId = val[1].replaceAll("^\'|\'$", "");
								LOGGER.info("OptyId :: {}",tpsId);
								if (Objects.nonNull(quoteToLe.getTpsSfdcOptyId()) && quoteToLe.getTpsSfdcOptyId().equals(tpsId)) {
									return true;
								}
							}
						}
					} catch (TclCommonException e) {
						LOGGER.error("Error",e);
					}
					return false;
				})) {
					jobsInProgress = Collections.emptyList();
				}
			}
		}
		if (jobsInProgress.isEmpty()) {
			ThirdPartyServiceJob thirdServiceJob = new ThirdPartyServiceJob();
			thirdServiceJob.setCreatedBy(Utils.getSource());
			thirdServiceJob.setCreatedTime(new Date());
			thirdServiceJob.setQueueName(queueName);
			thirdServiceJob.setRefId(refId);
			thirdServiceJob.setRequestPayload(payload);
			thirdServiceJob.setSeqNum(seqNum);
			thirdServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
			thirdServiceJob.setThirdPartySource(ThirdPartySource.CREDITCHECK.toString());
			thirdServiceJob.setServiceType(serviceType);
			thirdServiceJob.setIsComplete(isComplete);
			thirdPartyServiceJobsRepository.save(thirdServiceJob);
		}
	}


	/**
	 * processeCreditCheckResponse is used to process CreditCheckQueryResponseBean
	 *
	 * @param response
	 * @throws TclCommonException
	 */
	public void processCreditCheckResponse(CreditCheckQueryResponseBean response) throws TclCommonException {
		String responseContractingEntity = null;
		Optional<ThirdPartyServiceJob> thirdPartyServiceJob = thirdPartyServiceJobsRepository
				.findById(response.getTpsId());
		String copfResponse = Utils.convertObjectToJson(response);
		if (thirdPartyServiceJob.isPresent()) {
			thirdPartyServiceJob.get().setResponsePayload(copfResponse);
			thirdPartyServiceJob.get().setIsComplete((byte) 1);
			if (response.getStatus().equalsIgnoreCase(CommonConstants.BCR_SUCCESS)) {
				List<CreditCheckQueryResponse> creditCheckQueryResponsList = response.getSfdcCreditCheckQueryResponse();
				List<QuoteToLe> quoteToLeList = quoteToLeRepository.findByQuote_QuoteCode(thirdPartyServiceJob.get().getRefId());

				if(Objects.nonNull(creditCheckQueryResponsList) && !creditCheckQueryResponsList.isEmpty()){
					List<QuoteToLe> quoteToLes = quoteToLeRepository.
							findByTpsSfdcOptyId(creditCheckQueryResponsList.stream().findAny().get().getOpportunityId());
					if(Objects.nonNull(quoteToLes) && !quoteToLes.isEmpty()){
						QuoteToLe quoteToLe = quoteToLes.stream().findAny().get();
						if(isTeamsDRQuote(quoteToLe.getId())){
							quoteToLeList = quoteToLes;
						}
					}
				}

				if (Objects.nonNull(quoteToLeList) && !quoteToLeList.isEmpty()) {

					List<QuoteLeAttributeValue> quoteLeAttributeValueList = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLeList.get(0), LeAttributesConstants.ACCOUNT_NO18);

					if (Objects.nonNull(quoteLeAttributeValueList) && !quoteLeAttributeValueList.isEmpty() && !creditCheckQueryResponsList.isEmpty()) {
						LOGGER.info("Customer Contracting entity in quote Le {},customer c entity from response {}", quoteLeAttributeValueList.get(0).getAttributeValue(),
								creditCheckQueryResponsList.get(0).getAccountId());
						String customerContractingEntity = quoteLeAttributeValueList.get(0).getAttributeValue();
						if (creditCheckQueryResponsList.get(0).getAccountId() != null)
							responseContractingEntity = creditCheckQueryResponsList.get(0).getAccountId();
						Double responseProductMRC = creditCheckQueryResponsList.get(0).getProductServices().getRecord().stream().filter(rec -> Objects.nonNull(rec.getProductMRCc())).mapToDouble(rec -> new Double(rec.getProductMRCc())).sum();
						Double responseProductNRC = creditCheckQueryResponsList.get(0).getProductServices().getRecord().stream().filter(rec -> Objects.nonNull(rec.getProductNRCc())).mapToDouble(rec -> new Double(rec.getProductNRCc())).sum();
						LOGGER.info("Response Product MRC {}, response product NRC {}, quoteToLe final MRC {}, quoteToLe final NRC {}, status of credit control {}, opty id {} ", responseProductMRC,
								responseProductNRC, quoteToLeList.get(0).getFinalMrc(), quoteToLeList.get(0).getFinalNrc(), creditCheckQueryResponsList.get(0).getStatusOfCreditControl(), creditCheckQueryResponsList.get(0).getOpportunityId());
						// (Objects.nonNull(responseContractingEntity) && responseContractingEntity.equals(customerContractingEntity)) &&
						if (!creditCheckQueryResponsList.isEmpty()) {
							if ((Objects.nonNull(creditCheckQueryResponsList.get(0).getStatusOfCreditControl())
									&& !CommonConstants.PENDING_CREDIT_ACTION
									.equalsIgnoreCase(creditCheckQueryResponsList.get(0).getStatusOfCreditControl()) && !CommonConstants.RESERVED
									.equalsIgnoreCase(creditCheckQueryResponsList.get(0).getStatusOfCreditControl()) &&
									(responseProductMRC != null && responseProductNRC != null && (Math.abs(quoteToLeList.get(0).getFinalMrc() - responseProductMRC) <= 2) && (Math.abs(quoteToLeList.get(0).getFinalNrc() - responseProductNRC) <= 2))) 
									|| CommonConstants.POSITIVE.equalsIgnoreCase(quoteToLeList.get(0).getTpsSfdcStatusCreditControl())) {
								LOGGER.info("status of credit control in credit check query response {}, opty id {}, ref id {}, quoteToLe creditControlStatus {}", creditCheckQueryResponsList.get(0).getStatusOfCreditControl(), creditCheckQueryResponsList.get(0).getOpportunityId(), creditCheckQueryResponsList.get(0).getPortalTransactionId(), quoteToLeList.get(0).getTpsSfdcStatusCreditControl());
								thirdPartyServiceJob.get().setServiceStatus(CommonConstants.SUCCESS);
								processCreditCheckAudit(response);
								creditCheckService.triggerCreditCheckStatusChangeMail(response.getSfdcCreditCheckQueryResponse().get(0).getPortalTransactionId());

							} else if (Objects.nonNull(creditCheckQueryResponsList.get(0).getStatusOfCreditControl())
									&& CommonConstants.RESERVED
									.equalsIgnoreCase(creditCheckQueryResponsList.get(0).getStatusOfCreditControl())) {
								LOGGER.info("Credit check is reserved");
								Optional<User> userSource = userRepository.findById(quoteToLeList.get(0).getQuote().getCreatedBy());
								if (userSource.isPresent()) {
									LOGGER.info("user type in credit check response {}, opty id {}", userSource.get().getUserType(), creditCheckQueryResponsList.get(0).getOpportunityId());
									if (UserType.INTERNAL_USERS.toString().equalsIgnoreCase(userSource.get().getUserType()) && !CommonConstants.RESERVED
											.equalsIgnoreCase(quoteToLeList.get(0).getTpsSfdcStatusCreditControl())) {
										LOGGER.info("mail triggered for reserved status to sales user");
										creditCheckService.triggerCreditCheckStatusChangeMail(response.getSfdcCreditCheckQueryResponse().get(0).getPortalTransactionId());
									}
									if (!CommonConstants.RESERVED
											.equalsIgnoreCase(quoteToLeList.get(0).getTpsSfdcStatusCreditControl())) {
										LOGGER.info("Updating quote to le and audit as Reserved");
										processCreditCheckAudit(response);
									}
									//updateQuoteToForCreditCheck(creditCheckQueryResponsList.get(0), quoteToLeList.get(0));
								}
							}
						}
					}
				}

			} else {
				thirdPartyServiceJob.get().setServiceStatus(CommonConstants.FAILIURE);
			}
			thirdPartyServiceJob.get().setUpdatedTime(new Date());
			thirdPartyServiceJob.get().setUpdatedBy(Utils.getSource());
			thirdPartyServiceJobsRepository.save(thirdPartyServiceJob.get());
		}

	}

	private void processCreditCheckAudit(CreditCheckQueryResponseBean response) {
		response.getSfdcCreditCheckQueryResponse().stream().forEach(entry -> {

			String orderCode = entry.getPortalTransactionId();
			LOGGER.info("inside processCreditCheckAudit - orderCode {}", orderCode);
			if (StringUtils.isNotBlank(orderCode)) {
				orderCode = orderCode.replace(SFDCConstants.OPTIMUS, CommonConstants.EMPTY);
			}
			LOGGER.info("inside processSfdcOpportunityCreateResponse - replaced orderCode {}", orderCode);
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(orderCode);
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByTpsSfdcOptyId(entry.getOpportunityId());
			if(Objects.nonNull(quoteToLes) && !quoteToLes.isEmpty()){
				QuoteToLe quoteToLe = quoteToLes.stream().findAny().get();
				if(isTeamsDRQuote(quoteToLe.getId())){
					quoteLes = quoteToLes;
				}
			}
			quoteLes.stream().forEach(quoteLe -> {

				QuoteLeCreditCheckAudit creditCheckAudit = new QuoteLeCreditCheckAudit();
				creditCheckAudit.setCreatedTime(new Timestamp(new Date().getTime()));
				creditCheckAudit.setCreatedBy(Utils.getSource());
				creditCheckAudit.setQuoteToLe(quoteLe);
				creditCheckAudit.setTpsSfdcApprovedBy(entry.getApprovedBy());
				if (Objects.nonNull(entry.getMrcNrc()) && StringUtils.isNotBlank(entry.getMrcNrc())) {
					String[] mrcNrcValues = entry.getMrcNrc().split("/");
					creditCheckAudit.setTpsSfdcApprovedMrc(new Double(mrcNrcValues[0]));
					creditCheckAudit.setTpsSfdcApprovedNrc(new Double(mrcNrcValues[1]));
				} else {
					creditCheckAudit.setTpsSfdcApprovedMrc(entry.getProductServices().getRecord().stream().filter(rec -> Objects.nonNull(rec.getProductMRCc())).mapToDouble(rec -> new Double(rec.getProductMRCc())).sum());
					creditCheckAudit.setTpsSfdcApprovedNrc(entry.getProductServices().getRecord().stream().filter(rec -> Objects.nonNull(rec.getProductNRCc())).mapToDouble(rec -> new Double(rec.getProductNRCc())).sum());
				}
				creditCheckAudit.setTpsSfdcDifferentialMrc(entry.getDifferentialMRC());
				creditCheckAudit.setTpsSfdcCreditCheckStatus(entry.getStatusOfCreditControl());
				creditCheckAudit.setTpsSfdcCustomerName(entry.getCustomerName());
				creditCheckAudit.setTpsSfdcCuId(entry.getCustomerContractingEntityBean().getCustomerCode());
				quoteLeCreditCheckRepository.save(creditCheckAudit);

				updateQuoteToForCreditCheck(entry, quoteLe);

			});

		});

	}

	private void updateQuoteToForCreditCheck(CreditCheckQueryResponse entry, QuoteToLe quoteLe) {
		quoteLe.setTpsSfdcApprovedBy(entry.getApprovedBy());
		quoteLe.setCreditCheckTriggered(CommonConstants.BDEACTIVATE);
		if (Objects.nonNull(entry.getDateOfCreditApproval())) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = formatter.parse(entry.getDateOfCreditApproval());
			} catch (ParseException e) {
				LOGGER.info("Error in parsing date {}", e);
				throw new TclCommonRuntimeException(e);
			}

			quoteLe.setTpsSfdcCreditApprovalDate(date);
		}
		quoteLe.setTpsSfdcCreditRemarks(StringUtils.truncate(entry.getCreditRemarks(), 250));
		quoteLe.setTpsSfdcCreditLimit(entry.getCreditLimit());
		quoteLe.setTpsSfdcDifferentialMrc(entry.getDifferentialMRC());
		quoteLe.setTpsSfdcReservedBy(entry.getReservedBy());
		quoteLe.setTpsSfdcStatusCreditControl(entry.getStatusOfCreditControl());
		quoteLe.setTpsSfdcSecurityDepositAmount(entry.getSecurityDepositAmount());
		quoteToLeRepository.save(quoteLe);
	}


	public void processeBeforeCommercialOpenUpdateBcr(String quoteCode, String opportunityId, String quoteCurrency,
													  String region, String custAttribute, String approverLevel, String approverEmail, String closeAttribute)
			throws TclCommonException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String salesApproverDate = null;
		String salesApproverEmail = null;
		String approverDate1 = null;
		String approverDate2 = null;
		String approverDate3 = null;
		String pricingCategory = null;
		String approver1 = null;
		String approver2 = null;
		String approver3 = null;
		LOGGER.info("Inside process Open bcr.");
		List<ThirdPartyServiceJob> prevOpenBcr = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdAsc(quoteCode, CommonConstants.CREATE_OPEN_BCR,
						ThirdPartySource.SFDC.toString());
		// int length = prevOpenBcr.size() - 1;
		if (!prevOpenBcr.isEmpty()) {

			BCROmsRequest bcrRequest = new BCROmsRequest();
			BCROmsRecord record = new BCROmsRecord();
			BCROmsResponse response = new BCROmsResponse();
			BCROptyProperitiesBean optyBean = new BCROptyProperitiesBean();
			String bcrResponse = "";
			String Id = "";
			try {
				bcrRequest = (BCROmsRequest) Utils.convertJsonToObject(prevOpenBcr.get(0).getRequestPayload(),
						BCROmsRequest.class);
				record = bcrRequest.getBcrOmsRecords().get(0);
				optyBean = record.getBcrOptyProperitiesBeans();
				if (!StringUtils.isEmpty(bcrResponse)) {
					response = (BCROmsResponse) Utils.convertJsonToObject(prevOpenBcr.get(0).getResponsePayload(),
							BCROmsResponse.class);
					if (response != null && response.getCustomBCRId() != null && !response.getCustomBCRId().isEmpty()
							&& response.getCustomBCRId().get(0) != null
							&& !StringUtils.isEmpty(response.getCustomBCRId().get(0))) {
						Id = response.getCustomBCRId().get(0);
					}
				}
				salesApproverEmail = null;
				String completionStatus = "Submitted";
				pricingCategory = CommonConstants.LEVEL_0;
				salesApproverDate = formatter.format(date);
				salesApproverEmail = approverEmail;

				processeUpdateBcr(prevOpenBcr.get(0).getRefId(), Id, record.getBcrOptyProperitiesBeans().getRegion(),
						"Closed", record.getBcrOptyProperitiesBeans().getQuoteCurrency(), approver1, approver2,
						approver3, approverDate1, approverDate2, approverDate3, closeAttribute, completionStatus,
						salesApproverEmail, salesApproverDate, CommonConstants.LEVEL_0, true);
			} catch (TclCommonException e) {

				LOGGER.warn("BCR Conversion" + e);
			}

			String openBcr = Utils
					.convertObjectToJson(constructBcrOmsRequest(opportunityId, "India", "Raised", quoteCurrency, null,
							null, custAttribute, null, null, null, null, null, null, null, null, null, null, false));
			LOGGER.info("Persisting Create Open Bcr job.");
			persistSfdcServiceJob(quoteCode, BcrQueue, openBcr, (byte) 0, CommonConstants.CREATE_OPEN_BCR, 5,null,CommonConstants.BDEACTIVATE);
		}
	}


	/**
	    * @return ResponseResource
	    * @throws TclCommonException
	    * @author SURUCHIA
	    * @link http://www.tatacommunications.com
	    * @copyright 2018 Tata Communications Limited this method is used
	    * to update the order type and subtype in SFDC
	    */

	   public OpportunityBean updateOrderTypeAndSubtype(UpdateRequest request) throws TclCommonException {
		   //Override
		   return null;
	   }


	  
    /**
     * Method to validate addressdetail
     * @param addressDetail
     * @return
     */
    public AddressDetail validateAddressDetail(AddressDetail addressDetail)
    {
        if(Objects.isNull(addressDetail.getAddressLineOne()))
            addressDetail.setAddressLineOne("");
        if(Objects.isNull(addressDetail.getAddressLineTwo()))
            addressDetail.setAddressLineTwo("");
        if(Objects.isNull(addressDetail.getCity()))
            addressDetail.setCity("");
        if(Objects.isNull(addressDetail.getCountry()))
            addressDetail.setCountry("");
        if(Objects.isNull(addressDetail.getPincode()))
            addressDetail.setPincode("");
        if(Objects.isNull(addressDetail.getLocality()))
            addressDetail.setLocality("");
        if(Objects.isNull(addressDetail.getState()))
            addressDetail.setState("");
        return addressDetail;
    }

    private String setContinentValue(String country) {
    	String continent;
		switch (country.toLowerCase()) {
			case unites_states_of_america: {
				continent = NORTH_AMERICA;
				break;
			}
			case india: {
				continent = ASIA;
				break;
			}
			case singapore: {
				continent = ASIA;
				break;
			}
			case france: {
				continent = EUROPE;
				break;
			}
			case united_kingdom: {
				continent = EUROPE;
				break;
			}
			case germany: {
				continent = EUROPE;
				break;
			}
			default:
				continent = ASIA;
		}
		LOGGER.info("Continent selected for country {}  is {}", country, continent);
    	return continent;
    }


	public void createOptyRetrigger(Integer quoteId) throws TclCommonException {
		Quote quotes = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		if (quotes != null) {
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySource(quotes.getQuoteCode(),
					CREATE_OPTY, ThirdPartySource.SFDC.toString());
			LOGGER.info("create opty response {} :" ,sfdcServiceJobs);
			if (sfdcServiceJobs.isEmpty()) {
				QuoteToLeProductFamily quoteLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_Id(quotes.getQuoteToLes().stream().findFirst().get().getId());
				String productName = quoteLeProductFamily.getMstProductFamily().getName() != null
						? quoteLeProductFamily.getMstProductFamily().getName()
						: "";
				LOGGER.info("Retriggering Create Opportunity");
				processCreateOpty(quotes.getQuoteToLes().stream().findFirst().get(), productName);
			}
		}
	}

	@Transactional(readOnly = false)
	public void createProductRetrigger(Integer quoteId, String optyId) throws TclCommonException {
		Quote quotes = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		List<QuoteToLe> quoteLe = quoteToLeRepository.findByQuote_Id(quoteId);
		if (Objects.nonNull(quoteLe) && !quoteLe.isEmpty()) {
			QuoteToLe quoteToLe = quoteLe.get(0);
			if (StringUtils.isNotBlank(optyId)) {
				if (quoteToLe.getTpsSfdcOptyId() == null) {
					quoteToLe.setTpsSfdcOptyId(optyId);
					quoteToLeRepository.save(quoteToLe);
					List<ThirdPartyServiceJob> optyJobs = thirdPartyServiceJobsRepository
							.findByRefIdAndServiceTypeAndThirdPartySource(quotes.getQuoteCode(), CREATE_OPTY,
									ThirdPartySource.SFDC.toString());
					for (ThirdPartyServiceJob thirdPartyServiceJob : optyJobs) {
						if (thirdPartyServiceJob.getServiceStatus().equals(SfdcServiceStatus.INPROGRESS.toString())) {
							thirdPartyServiceJob.setTpsId(optyId);
							thirdPartyServiceJob.setServiceStatus(CommonConstants.SUCCESS);
							thirdPartyServiceJobsRepository.save(thirdPartyServiceJob);
						}
					}
				}
			}

			if (quoteToLe.getTpsSfdcOptyId() != null) { // checks if opty is created
				LOGGER.info("Opt id: {}", quoteToLe.getTpsSfdcOptyId());
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByRefIdAndServiceTypeAndThirdPartySource(quotes.getQuoteCode(), CREATE_PRODUCT,
								ThirdPartySource.SFDC.toString());
				LOGGER.info("create product response {} :", sfdcServiceJobs);
				if (sfdcServiceJobs.isEmpty()) {
					QuoteToLeProductFamily quoteLeProductFamily = quoteToLeProductFamilyRepository
							.findByQuoteToLe_Id(quoteToLe.getId());
					String productName = quoteLeProductFamily.getMstProductFamily().getName() != null
							? quoteLeProductFamily.getMstProductFamily().getName()
							: "";
					ProductSolution productSolution = quoteLeProductFamily.getProductSolutions().stream().findFirst()
							.get();
					LOGGER.info("Retriggering Create Product {}", productName);
					processProductServiceForSolution(quoteToLe, productSolution, quoteToLe.getTpsSfdcOptyId());
				}
			}
			LOGGER.error("Opportunity not created");

		}
	}


	/**
	 * Method created to convert local loop bw and portbw
	 * to kbps in sfdc
	 * @param type
	 * @param portCapacity
	 * @param localLoopCapacity
	 * @param feasibilityBean
	 */
	public void setLocalLoopAndPortCircuitCapacity(String type, String portCapacity, String localLoopCapacity, FeasibilityRequestBean feasibilityBean) {

			if(StringUtils.isNotBlank(portCapacity) && StringUtils.isNotBlank(localLoopCapacity)){
				String portBandwidth = getBw(portCapacity);
				feasibilityBean.setPortCircuitCapacityC(portBandwidth);
				String llBandwidth = getBw(localLoopCapacity);

				String llbwC = llBandwidth.split(" ")[0];
				String llUnit = llBandwidth.split(" ")[1];
				feasibilityBean.setIllLocalLoopCapacityC(llbwC);
				feasibilityBean.setIllLocalLoopCapacityUnitC(llUnit);
			}
	}

	public String getBw(String bandwidth) {
		String bwValue = "";
		if(bandwidth.split(" ").length == 2)
			bandwidth = bandwidth.split(" ")[0];

		switch(bandwidth){
			case "0.5" :
				bwValue="512 Kbps";
				break;
			case "0.125":
				bwValue="128 Kbps";
				break;
			case "0.25":
				bwValue="256 Kbps";
				break;
			default:
				bwValue= bandwidth + " Mbps";
		}
		return bwValue;
	}
	
	/**
	 * 
	 * @param quoteToLe
	 * @param updateOpportunityStage
	 * @return
	 * @throws TclCommonException
	 */
	public UpdateOpportunityStage updateBundleProducts(QuoteToLe quoteToLe,
			UpdateOpportunityStage updateOpportunityStage) throws TclCommonException {
		LOGGER.info("Inside getOpportunityUpdateBundleProducts izosdwan");
		ProductSolution solutions = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteToLe.getQuote().getId());
		if (solutions != null) {
			List<String> sdwanSiteProducts = quoteIzosdwanSiteRepository.getDistinctSiteProducts(solutions.getId());
			//static
//			if(sdwanSiteProducts.contains(IzosdwanCommonConstants.GVPN_CODE)) {
//				updateOpportunityStage.setBundledProductTwoC(IzosdwanCommonConstants.GVPN);
//				updateOpportunityStage.setBundledOrderTypeTwoC(IzosdwanCommonConstants.MACD_QUOTE_TYPE);
//			}
//			if(sdwanSiteProducts.contains(IzosdwanCommonConstants.IAS_CODE)) {
//				updateOpportunityStage.setBundledProductThreeC(IzosdwanCommonConstants.IAS);
//				updateOpportunityStage.setBundledOrderTypeThreeC(IzosdwanCommonConstants.MACD_QUOTE_TYPE);
//			}
			//dynamic
			if(sdwanSiteProducts.contains(IzosdwanCommonConstants.GVPN_CODE)) {
				updateOpportunityStage.setBundledProductTwoC(IzosdwanCommonConstants.GVPN);
				updateOpportunityStage.setBundledOrderTypeTwoC(IzosdwanCommonConstants.MACD_QUOTE_TYPE);
			}
			if(sdwanSiteProducts.contains(IzosdwanCommonConstants.IAS_CODE)) {
				if(updateOpportunityStage.getBundledProductTwoC()==null) {
					updateOpportunityStage.setBundledProductTwoC(IzosdwanCommonConstants.IAS);
					updateOpportunityStage.setBundledOrderTypeTwoC(IzosdwanCommonConstants.MACD_QUOTE_TYPE);
				}else {
				updateOpportunityStage.setBundledProductThreeC(IzosdwanCommonConstants.IAS);
				updateOpportunityStage.setBundledOrderTypeThreeC(IzosdwanCommonConstants.MACD_QUOTE_TYPE);
				}
			
		}
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = getProductFamilyBasenOnVersion(quoteToLe);
		for (QuoteToLeProductFamily quFamily : quoteToLeProductFamilies) {
			if (quFamily.getMstProductFamily() != null &&
				(quFamily.getMstProductFamily().getName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY) || quFamily.getMstProductFamily().getName().equalsIgnoreCase(IzosdwanCommonConstants.VUTM))) {
				updateOpportunityStage.setBundledProductFourC(IzosdwanCommonConstants.MSS);
				updateOpportunityStage.setBundledOrderTypeFourC(IzosdwanCommonConstants.NEW);
			}
		}
		}
		return updateOpportunityStage;
	}
	
	/**
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteToLeProductFamily> getProductFamilyBasenOnVersion(QuoteToLe quote) {
		List<QuoteToLeProductFamily> prodFamilys = null;
		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		return prodFamilys;
	}


	/**
	 * processUpdateOpportunity -This method is used to update the opportunity
	 *
	 * @param cofSignedDate
	 * @param optyId
	 * @param stage
	 * @throws TclCommonException
	 */
	public void processUpdateOpportunityCloseDroppedOrderlite(String optyId,String quoteCode,String stage,String dropType,String keyReason,String subReason)
			throws TclCommonException {
		LOGGER.info("Entering Process opty for OPTY id --->  {}  ", optyId);
		UpdateOpportunityStage updateOpportunityStage = new UpdateOpportunityStage();
		Integer seqId = null;
		//updateOpportunityStage.setWinLossDropKeyReason(keyReason);
		updateOpportunityStage.setLossReasons(keyReason);
		updateOpportunityStage.setDropReasons(subReason);
		//updateOpportunityStage.setDroppingReason(subReason);
		seqId = getSequenceNumber(SFDCConstants.CLOSED_DROPPED);
		LOGGER.info("seqId :::: {}", seqId);
		updateOpportunityStage.setOpportunityId(optyId);
		updateOpportunityStage.setStageName(dropType);

		if (SFDCConstants.PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			updateOpportunityStage.setCompetitor("NOT APPLICABLE");
			updateOpportunityStage.setQuoteByCompetitor("0");
		}
		//String familyName = getFamilyName(quoteToLe);
		/*
		 * if (familyName != null) { IOmsSfdcInputHandler handler =
		 * factory.getInstance(familyName); if (handler != null) {
		 * handler.getOpportunityUpdate(quoteToLe, updateOpportunityStage); } }
		 */
//		if(quoteToLe.getIsAmended()!=null && quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE)) {
//			updateOpportunityStage.setType(SFDCConstants.AMENDMENT);
//		}
		String request = Utils.convertObjectToJson(updateOpportunityStage);
		LOGGER.info("Input for updating the opty Status {}", request);
		persistSfdcServiceJob(quoteCode, sfdcUpdateOpty, request,
				StringUtils.isNotBlank(optyId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.UPDATE_OPTY, seqId,null,CommonConstants.BDEACTIVATE);
	}


	public void processCreateWaiverForTermination(QuoteToLe quoteToLe , QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetail)
			throws TclCommonException {
		LOGGER.info("inside processCreateWaiverForTermination for quote code {} ", quoteToLe.getQuote().getQuoteCode());

		if (Objects.nonNull(quoteSiteServiceTerminationDetail)) {
			// setting only for etc applicable sites
			if (CommonConstants.BACTIVE.equals(quoteSiteServiceTerminationDetail.getEtcApplicable())) {
				TerminationWaiverBean terminationWaiverBean = new TerminationWaiverBean();
			//	if (appEnv.equals(SFDCConstants.PROD)) {
			//		terminationWaiverBean.setRecordTypeName("New ETC Waiver");
			//	} else {
					terminationWaiverBean.setRecordTypeName("Pre Invoice ETC Waiver");   // for both dev and Prod env
			//	}
			//	terminationWaiverBean.setApproverEmail(quoteSiteServiceTerminationDetail.getCsmNonCsmEmail());
				if(Objects.nonNull(quoteToLe.getIsMultiCircuit())&&CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
					terminationWaiverBean.setOpportunityId(quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getTpsSfdcOptyId());
				} else {
					terminationWaiverBean.setOpportunityId(quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getQuoteToLe().getTpsSfdcOptyId());
				}
				ETCRecordBean etcRecordBean = new ETCRecordBean();
				etcRecordBean.setEtcWaiverTypeProposedBySales(quoteSiteServiceTerminationDetail.getWaiverType());
				if(quoteSiteServiceTerminationDetail.getProposedEtc() != null)
					etcRecordBean.setEtcValueProposedBySales(quoteSiteServiceTerminationDetail.getProposedEtc().toString());
				etcRecordBean.setEtcWaiverBasedOnEtcPolicy(quoteSiteServiceTerminationDetail.getWaiverPolicy());
				if (quoteSiteServiceTerminationDetail.getProposedBySales() != null)
				etcRecordBean.setAgreeWithWaiverProposedBySales(quoteSiteServiceTerminationDetail.getProposedBySales());
				if (quoteSiteServiceTerminationDetail.getFinalEtc() != null)
				etcRecordBean.setFinalEtcAmountToBeInvoiced(quoteSiteServiceTerminationDetail.getFinalEtc().toString());
			//	etcRecordBean.setLmProviderPayout("100");
				etcRecordBean.setComments(quoteSiteServiceTerminationDetail.getWaiverRemarks()); // setting for now
				if(quoteSiteServiceTerminationDetail.getWaiverPolicy() != null && quoteSiteServiceTerminationDetail.getWaiverPolicy().contains("6.1.4")) {
					etcRecordBean.setCompensatoryDetails(quoteSiteServiceTerminationDetail.getCompensatoryDetails());
				} else {
					etcRecordBean.setCompensatoryDetails(StringUtils.EMPTY);
				}
				terminationWaiverBean.setEtcRecord(etcRecordBean);

				String request = Utils.convertObjectToJson(terminationWaiverBean);
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcCreateWaiver, request, CommonConstants.BDEACTIVATE,
						CREATE_WAIVER, getSequenceNumber(SFDCConstants.CREATE_WAIVER),
						quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId(),CommonConstants.BDEACTIVATE);

			}
		}

	}

	public void processCreateCopfForChildOpty(String quoteCode, String opportunityId, String quoteCurrency,
			String productServiceId, List<LinkCOPFDetails> linkCOPFDetails, String type, String serviceId)
			throws TclCommonException {
		LOGGER.info("------------processCreateCopf---------------------");
		String createCopf = Utils.convertObjectToJson(
				constructCopfOmsRequest(quoteCurrency, opportunityId, productServiceId, linkCOPFDetails, type));
		persistSfdcServiceJob(quoteCode, processCopfId, createCopf, (byte) 0, SfdcServiceTypeConstants.CREATE_COPF_ID,
				0, serviceId, CommonConstants.BDEACTIVATE);

	}

	public void processSiteDetailsForChildOpty(QuoteToLe quoteToLe, SiteSolutionOpportunityBean siteSolutionOpportunityBean,ProductSolution productSolution, QuoteIllSiteToService quoteIllSiteToService) throws TclCommonException
	{
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();

		siteSolutionOpportunityBean.setSiteOpportunityLocations(null);// Empty the generalised data
		List<QuoteNplLink> links = nplLinkRepository.findByProductSolutionIdAndStatus(quoteIllSiteToService.getQuoteNplLink().getProductSolutionId(),CommonConstants.BACTIVE);

		if (!links.isEmpty()) {
			for (QuoteNplLink link : links) {

				QuoteIllSite siteA = illSiteRepository.findByIdAndStatus(link.getSiteAId(), CommonConstants.BACTIVE);
				QuoteIllSite siteB = illSiteRepository.findByIdAndStatus(link.getSiteBId(), CommonConstants.BACTIVE);
				if (siteA!=null)
					siteOpportunityLocations.add(constructSiteLocation(link,siteA));
				if (siteB!=null)
					siteOpportunityLocations.add(constructSiteLocation(link,siteB));
			}
		}
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType()) || MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
		if(productSolution != null && productSolution.getMstProductOffering() != null
				&& "MMR Cross Connect".equalsIgnoreCase(productSolution.getMstProductOffering().getProductName())){
			List<QuoteIllSite> quoteIllSitesList = illSiteRepository.findByProductSolutionAndStatus(productSolution, CommonConstants.BACTIVE);
			if(quoteIllSitesList != null && !quoteIllSitesList.isEmpty()) {
				for(QuoteIllSite quoteIllSite : quoteIllSitesList) {
					siteOpportunityLocations.add(constructSiteLocationForCrossConnect(quoteIllSite));
				}
			}

		}
		}
		siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);

	}

	public SiteOpportunityLocation constructSiteLocation(QuoteNplLink link, QuoteIllSite site ) throws TclCommonException{

		Double siteMrc=0D,siteArc=0D,siteNrc=0D;
		SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
		LOGGER.info("MDC Filter token value in before Queue call constructSiteLocation {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(site.getErfLocSitebLocationId()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);

			siteOpportunityLocation.setCity(addressDetail.getCity());
			siteOpportunityLocation.setCountry(addressDetail.getCountry());
			siteOpportunityLocation.setLocation(addressDetail.getCity());
			siteOpportunityLocation.setState(addressDetail.getState());
		}
		siteOpportunityLocation
				.setSiteLocationID(SFDCConstants.OPTIMUS.toString() + site.getSiteCode());

		if (link.getMrc()!=null && link.getMrc()!=0)
			siteMrc = link.getMrc()/2;

		if (link.getArc()!=null && link.getArc()!=0)
			siteArc = link.getArc()/2;

		if (link.getNrc()!=null && link.getNrc()!=0)
			siteNrc = link.getNrc()/2;

		siteOpportunityLocation.setSiteMRC(siteMrc);
		siteOpportunityLocation.setSiteARC(siteArc);
		siteOpportunityLocation.setSiteNRC(siteNrc);
		siteOpportunityLocation.setLinkCode(link.getLinkCode());

		if(MACDConstants.CANCELLATION.equalsIgnoreCase(site.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType())
					|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(site.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType())) {
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(link.getId());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {

			siteOpportunityLocation.setCurrentCircuitServiceId(quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
			LOGGER.info("Setting current circuit service id for NPL MACD {}", quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
			}
		}

		return siteOpportunityLocation;

	}

	public SiteOpportunityLocation constructSiteLocationForCrossConnect(QuoteIllSite site) throws TclCommonException{

		Double siteMrc=0D,siteArc=0D,siteNrc=0D;
		SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
		LOGGER.info("MDC Filter token value in before Queue call constructSiteLocation {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(site.getErfLocSitebLocationId()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);

			siteOpportunityLocation.setCity(addressDetail.getCity());
			siteOpportunityLocation.setCountry(addressDetail.getCountry());
			siteOpportunityLocation.setLocation(addressDetail.getCity());
			siteOpportunityLocation.setState(addressDetail.getState());
		}
		siteOpportunityLocation
				.setSiteLocationID(SFDCConstants.OPTIMUS.toString() + site.getSiteCode());


		siteOpportunityLocation.setSiteMRC(site.getMrc());
		siteOpportunityLocation.setSiteARC(site.getArc());
		siteOpportunityLocation.setSiteNRC(site.getNrc());
		siteOpportunityLocation.setLinkCode(site.getSiteCode());

			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite_Id(site.getId());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
			siteOpportunityLocation.setCurrentCircuitServiceId(quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
			LOGGER.info("Setting current circuit service id for NPL MACD {}", quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
			}

		return siteOpportunityLocation;

	}

	public void processSfdcPersistCreateWaiver(WaiverResponseBean responseBean) throws TclCommonException {
		LOGGER.info("Inside processSfdcPersistCreateWaiver with response bean {}", responseBean.toString());

		if (responseBean != null) {
			Optional<ThirdPartyServiceJob> thirdPartyServiceJob = thirdPartyServiceJobsRepository
					.findById(responseBean.getTpsId());
			Quote quote = quoteRepository.findByQuoteCode(thirdPartyServiceJob.get().getRefId());
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quote.getId());

			if (Objects.nonNull(quoteToLes.get(0).getIsMultiCircuit())&&quoteToLes.get(0).getIsMultiCircuit() == 1) {
				// checking for MULTI ckt - searching opty id from quote ill sites to service for each service id
				if (thirdPartyServiceJob.isPresent()) {
					thirdPartyServiceJob.get().setResponsePayload(Utils.convertObjectToJson(responseBean));
					thirdPartyServiceJob.get().setIsComplete((byte) 1);
					if (responseBean.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
						thirdPartyServiceJob.get().setServiceStatus(CommonConstants.SUCCESS);
					} else {
						thirdPartyServiceJob.get().setServiceStatus(CommonConstants.FAILIURE);
					}
					thirdPartyServiceJob.get().setUpdatedTime(new Date());
					thirdPartyServiceJob.get().setUpdatedBy(Utils.getSource());
					thirdPartyServiceJobsRepository.save(thirdPartyServiceJob.get());
				}

				if ((CommonConstants.SUCCESS).equalsIgnoreCase(thirdPartyServiceJob.get().getServiceStatus())) {
					responseBean.getEtcRecordList().forEach(etcRecord -> {
						QuoteIllSiteToService quoteIllSiteToServices = quoteIllSiteToServiceRepository.findByTpsSfdcOptyId(etcRecord.getChildOptyId());
						quoteIllSiteToServices.setTpsSfdcWaiverId(etcRecord.getId());
						quoteIllSiteToServices.setTpsSfdcWaiverName(etcRecord.getName());
						quoteIllSiteToServiceRepository.save(quoteIllSiteToServices);
					});
				}
			}
			 else {
				// checking for single ckt - searching opty id from quote
				List<ThirdPartyServiceJob> sfdcServiceJobs = new ArrayList<>();
				if (thirdPartyServiceJob.isPresent()) {
					thirdPartyServiceJob.get().setResponsePayload(Utils.convertObjectToJson(responseBean));
					thirdPartyServiceJob.get().setIsComplete((byte) 1);
					if (responseBean.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
						thirdPartyServiceJob.get().setServiceStatus(CommonConstants.SUCCESS);
					} else {
						thirdPartyServiceJob.get().setServiceStatus(CommonConstants.FAILIURE);
					}
					thirdPartyServiceJob.get().setUpdatedTime(new Date());
					thirdPartyServiceJob.get().setUpdatedBy(Utils.getSource());
					thirdPartyServiceJobsRepository.save(thirdPartyServiceJob.get());
				}

				if ((CommonConstants.SUCCESS).equalsIgnoreCase(thirdPartyServiceJob.get().getServiceStatus())) {
					Set<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteToLes.get(0).getQuoteIllSiteToServices();
					if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
						for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServiceList) {
							quoteIllSiteToService.setTpsSfdcWaiverId(responseBean.getEtcRecordList().get(0).getId());
							quoteIllSiteToService
									.setTpsSfdcWaiverName(responseBean.getEtcRecordList().get(0).getName());
							quoteIllSiteToServiceRepository.save(quoteIllSiteToService);
							break;
						}

					}
				}

			}
		}
	}


	public void processUpdateWaiverForTermination(QuoteToLe quoteToLe, QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetail)
			throws TclCommonException {
		LOGGER.info("inside processUpdateWaiverForTermination for quote code {} ", quoteToLe.getQuote().getQuoteCode());
		List<QuoteIllSite> illSites = new ArrayList<>();

		if (Objects.nonNull(quoteSiteServiceTerminationDetail)) {
			// setting only for etc applicable sites -- updating for all
			if (CommonConstants.BACTIVE.equals(quoteSiteServiceTerminationDetail.getEtcApplicable())) {
				TerminationWaiverBean terminationWaiverBean = new TerminationWaiverBean();
				ETCRecordBean etcRecordBean = new ETCRecordBean();
				etcRecordBean.setId(quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getTpsSfdcWaiverId());
				etcRecordBean.setEtcWaiverTypeProposedBySales(quoteSiteServiceTerminationDetail.getWaiverType());
				etcRecordBean.setEtcValueProposedBySales(quoteSiteServiceTerminationDetail.getProposedEtc().toString());
				etcRecordBean.setEtcWaiverBasedOnEtcPolicy(quoteSiteServiceTerminationDetail.getWaiverPolicy());
				etcRecordBean.setAgreeWithWaiverProposedBySales(quoteSiteServiceTerminationDetail.getProposedBySales());
				etcRecordBean.setFinalEtcAmountToBeInvoiced(quoteSiteServiceTerminationDetail.getFinalEtc().toString());
				if(quoteToLe.getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.getIsMultiCircuit())) {
					etcRecordBean.setOpportunityName(quoteToLe.getTpsSfdcOptyId());
				} else {
					etcRecordBean.setOpportunityName(quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getTpsSfdcOptyId());
				}
			//	etcRecordBean.setLmProviderPayout("100");
				etcRecordBean.setComments(quoteSiteServiceTerminationDetail.getWaiverRemarks()); // setting for now
				terminationWaiverBean.setEtcRecord(etcRecordBean);

				String request = Utils.convertObjectToJson(terminationWaiverBean);
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateWaiver, request, CommonConstants.BDEACTIVATE,
						UPDATE_WAIVER, getSequenceNumber(SFDCConstants.UPDATE_WAIVER),
						quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId(),CommonConstants.BDEACTIVATE);
			}
		}
	}

	public void processSfdcPersistUpdateWaiver(WaiverResponseBean responseBean) throws TclCommonException {
		LOGGER.info("Inside processSfdcPersistUpdateWaiver with response bean {}", responseBean.toString());

		if (responseBean != null) {
			Optional<ThirdPartyServiceJob> thirdPartyServiceJob = thirdPartyServiceJobsRepository
					.findById(responseBean.getTpsId());
			Quote quote = quoteRepository.findByQuoteCode(thirdPartyServiceJob.get().getRefId());
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quote.getId());

			if (Objects.nonNull(quoteToLes.get(0).getIsMultiCircuit())&&quoteToLes.get(0).getIsMultiCircuit() == 1) {
				// checking for MULTI ckt - searching opty id from quote ill sites to service for each service id
				if (thirdPartyServiceJob.isPresent()) {
					thirdPartyServiceJob.get().setResponsePayload(Utils.convertObjectToJson(responseBean));
					thirdPartyServiceJob.get().setIsComplete((byte) 1);
					if (responseBean.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
						thirdPartyServiceJob.get().setServiceStatus(CommonConstants.SUCCESS);
					} else {
						thirdPartyServiceJob.get().setServiceStatus(CommonConstants.FAILIURE);
					}
					thirdPartyServiceJob.get().setUpdatedTime(new Date());
					thirdPartyServiceJob.get().setUpdatedBy(Utils.getSource());
					thirdPartyServiceJobsRepository.save(thirdPartyServiceJob.get());
				}

				/*if ((CommonConstants.SUCCESS).equalsIgnoreCase(thirdPartyServiceJob.get().getServiceStatus())) {
					responseBean.getEtcRecordList().forEach(etcRecord -> {
						QuoteIllSiteToService quoteIllSiteToServices = quoteIllSiteToServiceRepository.findByTpsSfdcOptyId(etcRecord.getChildOptyId());
						quoteIllSiteToServices.setTpsSfdcWaiverId(etcRecord.getId());
						quoteIllSiteToServices.setTpsSfdcWaiverName(etcRecord.getName());
						quoteIllSiteToServiceRepository.save(quoteIllSiteToServices);
					});
				}*/
			}
			else {
				// checking for single ckt - searching opty id from quote
				List<ThirdPartyServiceJob> sfdcServiceJobs = new ArrayList<>();
				if (thirdPartyServiceJob.isPresent()) {
					thirdPartyServiceJob.get().setResponsePayload(Utils.convertObjectToJson(responseBean));
					thirdPartyServiceJob.get().setIsComplete((byte) 1);
					if (responseBean.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
						thirdPartyServiceJob.get().setServiceStatus(CommonConstants.SUCCESS);
					} else {
						thirdPartyServiceJob.get().setServiceStatus(CommonConstants.FAILIURE);
					}
					thirdPartyServiceJob.get().setUpdatedTime(new Date());
					thirdPartyServiceJob.get().setUpdatedBy(Utils.getSource());
					thirdPartyServiceJobsRepository.save(thirdPartyServiceJob.get());
				}

/*				if ((CommonConstants.SUCCESS).equalsIgnoreCase(thirdPartyServiceJob.get().getServiceStatus())) {
					List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLes.get(0).getId());
					if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {

						quoteIllSiteToServiceList.get(0).setTpsSfdcWaiverId(responseBean.getEtcRecordList().get(0).getId());
						quoteIllSiteToServiceList.get(0).setTpsSfdcWaiverName(responseBean.getEtcRecordList().get(0).getName());
						quoteIllSiteToServiceRepository.save(quoteIllSiteToServiceList.get(0));
					}
				}*/

			}
		}
	}

	/**
	 * Process create response for teamsdr.
	 * @param opBean
	 * @param sfdcOpertunityId
	 * @param orderCode
	 * @param quoteLes
	 * @throws TclCommonException
	 */
	private void processCreateResponseForTeamsDR(OpportunityResponseBean opBean, String sfdcOpertunityId, String orderCode,
			List<QuoteToLe> quoteLes) throws TclCommonException {
		MstOmsAttribute mstOmsAttrubute = getMstAttributeMaster(SFDCConstants.SFDC_STAGE, Utils.getSource());
		// For teamsDR with multiLE.
		quoteToLeRepository.findById(opBean.getQuoteToLeId()).ifPresent(quoteToLe ->
				processCreateResponseForTeamsDR(opBean,sfdcOpertunityId,orderCode,quoteToLe,mstOmsAttrubute));

		//			// Saving to Order if it exists
		//			List<OrderToLe> orderToLes = orderToLeRepository.findByOrder_OrderCode(orderCode);
		//			orderToLes.forEach(orderToLe -> {
		//						orderToLe.setTpsSfdcCopfId(sfdcOpertunityId);
		//						orderToLeRepository.save(orderToLe);
		Opportunity opportunity = opportunityRepository.findByUuid(orderCode);
		if (Objects.nonNull(opportunity)) {
			opportunity.setTpsOptyId(sfdcOpertunityId);
			opportunityRepository.save(opportunity);
		}
		List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
				.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(SfdcServiceStatus.INPROGRESS.toString(),
						orderCode, CREATE_OPTY, ThirdPartySource.SFDC.toString());
		persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(), Utils.convertObjectToJson(opBean),
				sfdcOpertunityId);
		LOGGER.info("Persisting the status of opty as success");
		processMFTask(quoteLes);
		LOGGER.info("MF task triggered");
	}

	/**
	 * processDeleteProductForTeamsDR
	 * @param quoteToLe
	 * @param productServicesResponseBean
	 * @param prodServiceId
	 * @throws TclCommonException
	 */
	private void processDeleteProductForTeamsDR(QuoteToLe quoteToLe,ProductServicesResponseBean productServicesResponseBean,
			String prodServiceId,String quoteCode) throws TclCommonException {
		if("delete".equals(productServicesResponseBean.getType())){
			List<ThirdPartyServiceJob> sfdcJobs = thirdPartyServiceJobsRepository
					.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
							SfdcServiceStatus.NEW.toString(), quoteToLe.getQuote().getQuoteCode(),
							SFDCConstants.DELETE_PRODUCT, ThirdPartySource.SFDC.toString());
			for (ThirdPartyServiceJob sfdcServiceJob : sfdcJobs) {
				ProductServiceBean productServiceBean = (ProductServiceBean) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
				if (productServiceBean.getProductSolutionCode()
						.equals(productServicesResponseBean.getProductSolutionCode())) {
					LOGGER.info("Save Request payload in product service job ");
					List<String> prodIds = new ArrayList<>();
					prodIds.add(prodServiceId);
					productServiceBean.setProductIds(prodIds);
					sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productServiceBean));
					thirdPartyServiceJobsRepository.save(sfdcServiceJob);
					break;
				}
			}
		}
	}
	
	/**
	 * 
	 * getThirdPartyServiceJobDetails - get the thirdparty jobs with failire cases
	 * 
	 * @param quoteCode
	 * @return List<ThirdPartyServiceJobBean>
	 */
	public List<ThirdPartyServiceJobBean> getThirdPartyServiceJobDetails(String quoteCode) {
		LOGGER.info("getThirdPartyServiceJobDetails method invoked {}", quoteCode);
		List<ThirdPartyServiceJobBean> thirdPartyServiceJobBeanList = new ArrayList<>();
		if (StringUtils.isNotBlank(quoteCode)) {
			Map<String, ThirdPartyServiceJobBean> updateOppMap = new HashMap<>();
			Map<String, ThirdPartyServiceJobBean> updateProductMap = new HashMap<>();
			Map<String, ThirdPartyServiceJobBean> deleteProductMap = new HashMap<>();

			List<ThirdPartyServiceJob> thirdPartyServiceJobList = thirdPartyServiceJobsRepository
					.findByRefIdAndServiceStatus(quoteCode);
			for (ThirdPartyServiceJob thirdPartyServiceJob : thirdPartyServiceJobList) {
				constructFailureCase(thirdPartyServiceJobBeanList, updateOppMap, updateProductMap,deleteProductMap,
						thirdPartyServiceJob);

			}
			for (Map.Entry<String, ThirdPartyServiceJobBean> map : updateOppMap.entrySet()) {
				thirdPartyServiceJobBeanList.add(map.getValue());
			}
			for (Map.Entry<String, ThirdPartyServiceJobBean> map : updateProductMap.entrySet()) {
				thirdPartyServiceJobBeanList.add(map.getValue());
			}
			for (Map.Entry<String, ThirdPartyServiceJobBean> map : deleteProductMap.entrySet()) {
				thirdPartyServiceJobBeanList.add(map.getValue());
			}
		} else {
			LOGGER.info("Quote Code is empty {}", quoteCode);
		}
		return thirdPartyServiceJobBeanList;
	}

	/**
	 * 
	 * constructFailureCase
	 * @param thirdPartyServiceJobBeanList
	 * @param updateOppMap
	 * @param updateProductMap
	 * @param thirdPartyServiceJob
	 */
	private void constructFailureCase(List<ThirdPartyServiceJobBean> thirdPartyServiceJobBeanList,
			Map<String, ThirdPartyServiceJobBean> updateOppMap, Map<String, ThirdPartyServiceJobBean> updateProductMap, Map<String, ThirdPartyServiceJobBean> deleteProductMap,
			ThirdPartyServiceJob thirdPartyServiceJob) {
		ThirdPartyServiceJobBean bean = new ThirdPartyServiceJobBean();
		if (StringUtils.isNotBlank(thirdPartyServiceJob.getRequestPayload())) {
			JSONParser jsonParser = new JSONParser();
			try {
				JSONObject dataEnvelopeObj = (JSONObject) jsonParser
						.parse(thirdPartyServiceJob.getRequestPayload());
				bean.setId(thirdPartyServiceJob.getId());
				bean.setSeqNum(thirdPartyServiceJob.getSeqNum());
				bean.setServiceStatus(thirdPartyServiceJob.getServiceStatus());
				bean.setServiceType(thirdPartyServiceJob.getServiceType());
				bean.setRequestPayload(thirdPartyServiceJob.getRequestPayload());
				LOGGER.info("Proccessing tps Id {}", thirdPartyServiceJob.getId());
				switch (thirdPartyServiceJob.getServiceType()) {
				case CREATE_OPTY:
					thirdPartyServiceJobBeanList.add(bean);
					break;
				case UPDATE_OPTY:
					dataEnvelopeObj = (JSONObject) jsonParser.parse(thirdPartyServiceJob.getRequestPayload());
					String stageName = (String) dataEnvelopeObj.get(SFDCConstants.STAGE_NAME);
					updateOppMap.put(stageName, bean);
					break;
				case CREATE_PRODUCT:
					thirdPartyServiceJobBeanList.add(bean);
					break;
				case UPDATE_WAIVER:
					thirdPartyServiceJobBeanList.add(bean);
					break;
				case UPDATE_PRODUCT:
					String productSolCode = (String) dataEnvelopeObj.get(SFDCConstants.PROD_SOL_CODE);
					updateProductMap.put(productSolCode, bean);
					break;
				case DELETE_PRODUCT:
					String delProductSolCode = (String) dataEnvelopeObj.get(SFDCConstants.PROD_SOL_CODE);
					deleteProductMap.put(delProductSolCode, bean);
					break;
				case UPDATE_SITE:
					thirdPartyServiceJobBeanList.add(bean);
					break;
				default:
					LOGGER.info("Case not handled yet {}", thirdPartyServiceJob.getServiceType());
					break;
				}
			} catch (Exception e) {
				LOGGER.error("Error in extracting thirdparty failure cases", e);
			}
		}
	}
	
	/**
	 * 
	 * updateThirdPartyServiceJobDetails
	 * @param thirdPartyServiceJobBean
	 */
	@Transactional
	public void updateThirdPartyServiceJobDetails(ThirdPartyServiceJobBean thirdPartyServiceJobBean) {
		LOGGER.info("updateThirdPartyServiceJobDetails method invoked {}", thirdPartyServiceJobBean.getId());
		LOGGER.info("makesuccess {} ,reason {}",thirdPartyServiceJobBean.getMakeSuccess(),thirdPartyServiceJobBean.getReasonForMakingSuccess());
		Optional<ThirdPartyServiceJob> entityOptional = thirdPartyServiceJobsRepository
				.findById(thirdPartyServiceJobBean.getId());
		if (entityOptional.isPresent() && !entityOptional.get().getServiceStatus().equalsIgnoreCase(SfdcServiceStatus.SUCCESS.toString())) {
			ThirdPartyServiceJob entity = entityOptional.get();
			try {
				JSONParser inputJsonParser = new JSONParser();
				JSONObject inputJsonObject = (JSONObject) inputJsonParser.parse(entity.getRequestPayload());
				List<ThirdPartyServiceJob> thirdPartyList = thirdPartyServiceJobsRepository
						.findAllByRefIdAndServiceType(entity.getRefId(), entity.getServiceType());
				LOGGER.info("Total thirdPartyServiceJob based on service type except first records{}",
						thirdPartyList.size());
				List<ThirdPartyServiceJob> successthirdPartyServiceJobsList = new ArrayList<>();
				for (ThirdPartyServiceJob thirdPartyServiceJob : thirdPartyList) {
					if (!thirdPartyServiceJob.getId().equals(entity.getId())) {
						extractPrevCases(inputJsonObject, successthirdPartyServiceJobsList, thirdPartyServiceJob);
					}
				}
				persistAllStatusExpectFirst(successthirdPartyServiceJobsList);
				persistThirdPartyServiceJobsAudit(entity, thirdPartyServiceJobBean);
				if (thirdPartyServiceJobBean.getMakeSuccess() != null && thirdPartyServiceJobBean.getMakeSuccess()) {
					entity.setServiceStatus(SfdcServiceStatus.SUCCESS.toString());
				} else {
					entity.setServiceStatus(SFDCConstants.NEW);
				}
				entity.setRequestPayload(thirdPartyServiceJobBean.getRequestPayload());
				thirdPartyServiceJobsRepository.save(entity);
				LOGGER.info("saved and updated the status: {}",entity.getId());
				List<String> statuses = new ArrayList<>();
				statuses.add(SfdcServiceStatus.FAILURE.toString());
				statuses.add(SfdcServiceStatus.STRUCK.toString());
				enableStruck(entity, statuses);
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}else {
			LOGGER.info("ALready Success");
		}
	}

	private void enableStruck(ThirdPartyServiceJob entity, List<String> statuses) {
		List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceStatusInAndThirdPartySource(entity.getRefId(), statuses,
						ThirdPartySource.SFDC.toString());
		for (ThirdPartyServiceJob sfdcServiceJob : sfdcServiceJobs) {
			if (sfdcServiceJob.getIsActive().equals(CommonConstants.BACTIVE)) {
				if (sfdcServiceJob.getServiceStatus().equals(SfdcServiceStatus.FAILURE.toString())) {
					sfdcServiceJob.setRetryCount(
							sfdcServiceJob.getRetryCount() != null ? sfdcServiceJob.getRetryCount() + 1 : 1);
				}
				sfdcServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
				sfdcServiceJob.setUpdatedBy(Utils.getSource());
				sfdcServiceJob.setUpdatedTime(new Date());
				thirdPartyServiceJobsRepository.save(sfdcServiceJob);
			}
		}
	}

	private void extractPrevCases(JSONObject inputJsonObject,
			List<ThirdPartyServiceJob> successthirdPartyServiceJobsList, ThirdPartyServiceJob thirdPartyServiceJob) {
		JSONParser jsonParser = new JSONParser();
		try {
			if (thirdPartyServiceJob.getServiceType().equals(UPDATE_OPTY)) {
				String inputStageName = (String) inputJsonObject.get(SFDCConstants.STAGE_NAME);
				JSONObject exitingJsonObj = (JSONObject) jsonParser
						.parse(thirdPartyServiceJob.getRequestPayload());
				String existingStageName = (String) exitingJsonObj.get(SFDCConstants.STAGE_NAME);
				if (existingStageName.equals(inputStageName)) {
					successthirdPartyServiceJobsList.add(thirdPartyServiceJob);
				}
			} else if (thirdPartyServiceJob.getServiceType().equals(UPDATE_PRODUCT) || thirdPartyServiceJob.getServiceType().equals(DELETE_PRODUCT)) {
				String inputSoleCode = (String) inputJsonObject.get(SFDCConstants.PROD_SOL_CODE);
				JSONObject exitingProdSolJsonObj = (JSONObject) jsonParser
						.parse(thirdPartyServiceJob.getRequestPayload());
				String existingProdSol = (String) exitingProdSolJsonObj
						.get(SFDCConstants.PROD_SOL_CODE);
				if (existingProdSol.equals(inputSoleCode)) {
					successthirdPartyServiceJobsList.add(thirdPartyServiceJob);
				}
			} else {
				LOGGER.info("Case not handled yet {}", thirdPartyServiceJob.getServiceType());
			}
		} catch (Exception e) {
			LOGGER.error("Error in extracting thirdparty failure cases", e);
		}
	}

	@Transactional
	public void persistAllStatusExpectFirst(List<ThirdPartyServiceJob> list) {
		LOGGER.info("persistAllThirdPartyVerbalAgreement method invoked {} ",list.size());
		list.stream().forEach(element -> {
			element.setServiceStatus("SUCCESS");
			element.setIsComplete(CommonConstants.BACTIVE); // set 1
			thirdPartyServiceJobsRepository.save(element);
		});
	}
    
    public void persistThirdPartyServiceJobsAudit(ThirdPartyServiceJob entity,ThirdPartyServiceJobBean bean) {
    	ThirdPartyServiceJobAudit audit = new ThirdPartyServiceJobAudit();
    	audit.setRefId(entity.getRefId());
    	audit.setFromJSON(entity.getRequestPayload());
    	audit.setToJSON(bean.getRequestPayload());
    	audit.setThirdPartyServiceJobsId(entity.getId());
    	audit.setCreatedBy(Utils.getSource());
    	audit.setCreatedTime(new Date());
    	audit.setMakeSuccess(bean.getMakeSuccess()!=null?(bean.getMakeSuccess()?CommonConstants.BACTIVE:CommonConstants.BDEACTIVATE):CommonConstants.BDEACTIVATE);
		if (bean.getReasonForMakingSuccess() != null) {
			if (bean.getReasonForMakingSuccess().toCharArray().length <= 500) {
				audit.setSuccessReason(bean.getReasonForMakingSuccess());
			}else {
				audit.setSuccessReason(bean.getReasonForMakingSuccess().substring(0, 499));
			}
		}
        thirdPartyServiceJobsAuditRepository.save(audit);
    }
    
	private boolean isRenewals(QuoteToLe quoteToLe) {
	    if(quoteToLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
	    	return true;
	    }
		return false;

	}
	
	public void processProductServicesRenewals(QuoteToLe quoteToLe, String sfdcOpertunityId) throws TclCommonException {
		LOGGER.info("inside processSfdcOpportunityCreateResponse - after saving quoteToLe {}",
				quoteToLe.getTpsSfdcOptyId());
		Set<QuoteToLeProductFamily> quoteToLeProdFamily =quoteToLe.getQuoteToLeProductFamilies();
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProdFamily) {
			if (quoteToLe.getIsMultiCircuit() != null
					&& quoteToLe.getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				LOGGER.info("Multicircuit flow");
				processProductServiceForSolutionMulticircuit(quoteToLe, quoteToLeProductFamily, sfdcOpertunityId);
			} else {
				LOGGER.info("Normal flow flow");
				List<ProductSolution> productSolutions = productSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProductFamily);
				     if(!productSolutions.isEmpty()) {
					processProductServiceForSolution(quoteToLe, productSolutions.get(0), sfdcOpertunityId);
				     }
				
			}
		}
	}
	
	public void processUpdateProductRenewals(QuoteToLe quoteToLe) throws TclCommonException {
		List<QuoteToLeProductFamily> quoteLeProdFamilyRepo = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteLeProdFamilyRepo) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			if (quoteToLe.getIsMultiCircuit()!=null && quoteToLe.getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				LOGGER.info("Since this is a multicircuit - we are accomodating and creating as a single product");
				processProductUpdateSolutionMulticircuit(quoteToLe, quoteToLeProductFamily);
			} else {
				LOGGER.info("Normal Scenario");
				if(!productSolutions.isEmpty()) {
					processProductUpdateSolution(quoteToLe, productSolutions.get(0));
					break;
					}
				
			}
		}

	}
	
	@Transactional
	public void processSiteDetailsRenewals(QuoteToLe quoteToLe) throws TclCommonException {
		Map<String, SFDCCommercialBifurcationBean> commercialsMap = new HashMap<>();
		List<QuoteToLeProductFamily> quoteLeProdFamilyRepo = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteLeProdFamilyRepo) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			if (quoteToLe.getIsMultiCircuit()!=null && quoteToLe.getIsMultiCircuit().equals(CommonConstants.BACTIVE) ) {
				LOGGER.info("multicircuit flow");
				processCreateSiteSolutionMulticircuit(quoteToLe, commercialsMap, quoteToLeProductFamily);
			} else {
				LOGGER.info("Normal flow");
				ProductSolution productSolutionWithId = null;
				for (ProductSolution productSolution : productSolutions) {
					if(productSolution.getTpsSfdcProductId()!=null) {
						productSolutionWithId = productSolution;
					}
				}
				for (ProductSolution productSolution : productSolutions) {
					if(productSolutionWithId!=null) {
					processCreateSiteSolutionRenewals(quoteToLe, commercialsMap, productSolution, productSolutionWithId);
					}
				}
			}
		}
	}
	
	private void processCreateSiteSolutionRenewals(QuoteToLe quoteToLe,
			Map<String, SFDCCommercialBifurcationBean> commercialsMap, ProductSolution productSolution, ProductSolution productSolutionWithId)
			throws TclCommonException {
		SiteSolutionOpportunityBean siteSolutionOpportunityBean = new SiteSolutionOpportunityBean();
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();
		List<QuoteIllSite> illSites = getIllsitesBasenOnVersion(productSolution);
		for (QuoteIllSite quoteIllSite : illSites) {
			LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteIllSite.getErfLocSitebLocationId()));

			String productname = getFamilyName(quoteToLe);
			if (!("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				addressDetail = validateAddressDetail(addressDetail);
				/* String address=addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo()+" "+addressDetail.getLocality();*/
				SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
				siteOpportunityLocation.setCity(addressDetail.getCity());
				siteOpportunityLocation.setCountry(addressDetail.getCountry());
				siteOpportunityLocation.setLocation(addressDetail.getCity());
				siteOpportunityLocation.setState(addressDetail.getState());
				siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIllSite.getSiteCode());
				siteOpportunityLocation.setSiteMRC(quoteIllSite.getMrc());
				siteOpportunityLocation.setSiteNRC(quoteIllSite.getNrc());
				siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				siteOpportunityLocations.add(siteOpportunityLocation);
			}
			else if ("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))
			{
				LOGGER.info("Site id id ::::::  {}", quoteIllSite.getId());
				String referenceName = productname.equalsIgnoreCase("IAS")
						?QuoteConstants.ILLSITES.toString():productname.equalsIgnoreCase("GVPN")?QuoteConstants.GVPN_SITES.toString():"";
				LOGGER.info("Reference Name is ::::::  {}", referenceName);
			List<QuoteProductComponent> productComponent =
					quoteProductComponentRepository.findByReferenceIdAndReferenceName(quoteIllSite.getId(), referenceName);
			LOGGER.info("Product Components size for quote ill site :::: {}  is ::::  {}  ", quoteIllSite.getId(), productComponent.size());
			productComponent.forEach(component -> {
				QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(component.getId()), MACDConstants.COMPONENTS);
				if (Objects.nonNull(quotePrice)) {
					LOGGER.info("Inside quoteprice if block with nrc ::: {}", quotePrice.getEffectiveNrc());
					populateSiteOpportunityLocationForPrimaryAndSecondary(component.getType(), commercialsMap, quotePrice,quoteToLe);
					LOGGER.info("Map Value :: {}", commercialsMap);
				}
			});
			//SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
			for (Map.Entry<String, SFDCCommercialBifurcationBean> bifurcationBeanEntry : commercialsMap.entrySet()) {
				String productComponentType = bifurcationBeanEntry.getKey();
				String circuitId = "";
				if (Objects.nonNull(quoteToLe)
						&& ((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(quoteToLe.getQuoteType()) || MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())
										|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()))) {
					Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite, quoteToLe);
					LOGGER.info("ServiceIds" + serviceIds);
					if (productComponentType.equalsIgnoreCase(PDFConstants.PRIMARY)) {
						/*circuitId = Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId()) ? quoteToLe.getErfServiceInventoryTpsServiceId() : "";*/
						circuitId = serviceIds.get(PDFConstants.PRIMARY);
					} else if (productComponentType.equalsIgnoreCase(PDFConstants.SECONDARY)) {
						/*circuitId = secondaryCircuitId;*/
						circuitId = serviceIds.get(PDFConstants.SECONDARY);
					}
				}
				SFDCCommercialBifurcationBean bifurcationBean = bifurcationBeanEntry.getValue();
				SiteOpportunityLocation siteOpportunityLocation = setSiteOpportunityLocationForSingleAndDualCircuits(quoteToLe, quoteIllSite, locationResponse, bifurcationBean.getNrc(),
						bifurcationBean.getMrc(), circuitId);
				siteOpportunityLocations.add(siteOpportunityLocation);
			}
		}
		}
		siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
		siteSolutionOpportunityBean.setProductServiceId(productSolutionWithId.getTpsSfdcProductId());
		siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);
		siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
		siteSolutionOpportunityBean.setProductSolutionCode(productSolution.getSolutionCode());
		siteSolutionOpportunityBean
				.setSourceSytemTransactionId(SFDCConstants.OPTIMUS + productSolution.getSolutionCode());
		Byte isComplete = CommonConstants.BDEACTIVATE;
		if (StringUtils.isNotBlank(productSolutionWithId.getTpsSfdcProductId())
				&& StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())) {
			isComplete = CommonConstants.BACTIVE;
		}

		// included to handle site update issue for NPL
		String familyName = getFamilyName(quoteToLe);
		if (familyName != null && familyName.equalsIgnoreCase(CommonConstants.NPL) || familyName != null && familyName.equalsIgnoreCase(CommonConstants.NDE)) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.processSiteDetails(quoteToLe, siteSolutionOpportunityBean, productSolution);
			}
		}

		String request = Utils.convertObjectToJson(siteSolutionOpportunityBean);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateSite, request, isComplete,
				SFDCConstants.UPDATE_SITE, getSequenceNumber(SFDCConstants.UPDATE_SITE),null,CommonConstants.BDEACTIVATE);
	}

	public void triggerCreateProductInSfdcForGSC(Integer quoteToLeId){
		try {
			LOGGER.info("Calling Create Product in SFDC");
			QuoteToLe quoteToLe = quoteToLeRepository.findById(quoteToLeId).orElseThrow(NullPointerException::new);
			if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())) {
				processProductServicesForGSC(quoteToLe, quoteToLe.getTpsSfdcOptyId());
			}
		} catch (Exception e) {
			LOGGER.error("create Product in sfdc failed",e.getMessage());
		}


	}
	private void calculateSolutionRenewalsPrice(QuoteToLe quoteTole, ProductServiceBean productServiceBean) {
		productServiceBean.setProductMRC(0D);
		productServiceBean.setProductNRC(0D);
		
		productServiceBean.setProductNRC(productServiceBean.getProductNRC()
				+ (quoteTole.getFinalNrc() != null ? quoteTole.getFinalNrc() : 0D));
		productServiceBean.setProductMRC(productServiceBean.getProductMRC()
				+ (quoteTole.getFinalArc() != null ? quoteTole.getFinalArc() / 12 : 0D));
//		List<QuoteToLeProductFamily> quoteLeProdFamilyRepo = quoteToLeProductFamilyRepository
//				.findByQuoteToLe(quoteTole.getId());

//		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteLeProdFamilyRepo) {
//			List<ProductSolution> productSolutions = productSolutionRepository
//					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
//
//			for (ProductSolution productSolution : productSolutions) {
//
//				List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
//						CommonConstants.BACTIVE);
//				for (QuoteIllSite quoteIllSite : illSites) {
//					if (quoteIllSite.getFeasibility() == CommonConstants.BACTIVE) {
//						productServiceBean.setProductNRC(productServiceBean.getProductNRC()
//								+ (quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0D));
//						productServiceBean.setProductMRC(productServiceBean.getProductMRC()
//								+ (quoteIllSite.getArc() != null ? quoteIllSite.getArc() / 12 : 0D));
//					}
//					QuoteToLe quoteToLe = productSolution.getQuoteToLeProductFamily().getQuoteToLe();
//
//				}
//			}
//
//		}
	}
}
