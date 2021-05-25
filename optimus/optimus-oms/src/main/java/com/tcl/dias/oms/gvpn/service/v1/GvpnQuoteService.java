
package com.tcl.dias.oms.gvpn.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.common.constants.CommonConstants.COMMA;
import static com.tcl.dias.common.constants.CommonConstants.GVPN;
import static com.tcl.dias.common.constants.CommonConstants.MACD;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.LeAttributesConstants.*;
import static com.tcl.dias.common.constants.LeAttributesConstants.TEAM_ROLE_SFDC;
import static com.tcl.dias.oms.constants.MACDConstants.MACD_QUOTE_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_FORM;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_THROUGH_CLASSIFICATION;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_WITH_CLASSIFICATION;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.dias.common.beans.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.CustomerLeAccountManagerDetails;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.CustomerLeVO;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LMDetailBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MSABean;
import com.tcl.dias.common.beans.OmsLeAttributeBean;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.beans.ThirdPartyResponseBean;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIPriceRevisionDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponseBean;
import com.tcl.dias.common.sfdc.bean.SfdcCreditCheckQueryRequest;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.QuoteAccess;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.DashBoardFamilyBean;
import com.tcl.dias.oms.beans.DashboardCustomerbean;
import com.tcl.dias.oms.beans.DashboardLegalEntityBean;
import com.tcl.dias.oms.beans.DashboardQuoteBean;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MACDExistingComponentsBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.PricingDetailBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteFamilySVBean;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuoteIllSitesFeasiblityBean;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteLeSVBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteSiteServiceTerminationDetailsBean;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.beans.QuoteSummaryBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.Site;
import com.tcl.dias.oms.beans.SiteDetail;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.dto.QuoteDelegationDto;
import com.tcl.dias.oms.dto.QuoteIllSiteDto;
import com.tcl.dias.oms.dto.QuotePriceDto;
import com.tcl.dias.oms.dto.QuoteProductComponentDto;
import com.tcl.dias.oms.dto.UserDto;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gvpn.macd.service.v1.GvpnMACDService;
import com.tcl.dias.oms.gvpn.pricing.bean.Feasible;
import com.tcl.dias.oms.gvpn.pricing.bean.Result;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.renewals.bean.RenewalsConstant;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.validator.services.GvpnCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the GvpnQuoteService.java class. All the Quote related
 * Services for Gvpn will be implemented in this class
 * 
 *
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class GvpnQuoteService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GvpnQuoteService.class);
	public static final String INTL_MANUAL_FEASIBILITY_REQUESTED = "INTL_MANUAL_FEASIBILITY_REQUESTED";
	public static final String PRIMARY = "primary";
	public static final String CUSTOMER_SECS_ID = "CUSTOMER_SECS_ID";
	@Autowired
	GvpnMACDService gvpnMacdService;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;

	@Autowired
	protected IllSiteRepository illSiteRepository;

	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	protected MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	protected ProductSolutionRepository productSolutionRepository;

	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	protected QuotePriceRepository quotePriceRepository;

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;
	
	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	protected QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	protected OrderRepository orderRepository;

	@Autowired
	protected OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	OrderSiteProvisioningRepository ordersiteProvsioningRepository;

	@Autowired
	protected OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@Autowired
	protected OmsSfdcService omsSfdcService;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	NotificationService notificationService;

	@Autowired
	CurrencyConversionRepository currencyConversionRepository;

	@Autowired
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Value("${info.customer_le_location_queue}")
	String customerLeLocationQueue;

	@Autowired
	protected MQUtils mqUtils;

	@Autowired
	PartnerService partnerService;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${notification.mail.template}")
	String delegationTemplateId;

	@Value("${notification.mail.loginurl}")
	String loginUrl;

	@Value("${rabbitmq.location.detail}")
	protected String locationQueue;

	@Value("${rabbitmq.orderIdInRespecToServiceId.queue}")
	String orderIdCorrespondingToServId;

	@Value("${pilot.team.email}")
	String[] pilotTeamMail;

	@Value("${notification.order.mail.subject}")
	String objectConfirmationSubject;

	@Value("${notification.order.mail.bcc}")
	String[] orderConfirmationBccMail;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Value("${rabbitmq.feasibility.request}")
	String feasibilityEngineQueue;

	@Value("${notification.new.order.customer.template}")
	String customerOrderTemplateId;

	@Value("${notification.new.order.pilot.template}")
	String orderConfirmationTemplateId;

	@Value("${order.enrichment.url}")
	String orderEntrichmentUrl;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Value("${rabbitmq.service.provider.detail}")
	String spQueue;

	@Value("${rabbitmq.customerleattr.queue}")
	String customerleattrQueue;

	@Value("${rabbitmq.customerleattr.product.queue}")
	protected String customerLeAttrQueueProduct;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;

	@Value("${app.host}")
	String appHost;

	@Value("${rabbitmq.customer.le.account.mananger}")
	String customerLeAccountMangeQueue;

	@Autowired
	protected GvpnQuotePdfService gvpnQuotePdfService;

	@Autowired
	private GscPricingFeasibilityService gscPricingFeasibilityService;

	@Autowired
	protected CofDetailsRepository cofDetailsRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	protected DocusignAuditRepository docusignAuditRepository;

	@Autowired
	OrderSiteStatusAuditRepository orderSiteStatusAuditRepository;

	@Autowired
	OrderSiteStageAuditRepository orderSiteStageAuditRepository;

	@Value("${rabbitmq.customer.le.update.msa}")
	String updateMSAQueue;

	@Autowired
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

	@Autowired
	EngagementRepository engagementRepository;

	@Value("${rabbitmq.location.details.feasibility}")

	protected String locationDetailQueue;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Value("${cust.get.segment.attribute}")
	String customerSegment;

	@Autowired
	GscQuoteService gscQuoteService;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Value("${rabbitmq.sites.type.detail}")
	String siteTypeDetailsQueue;

	@Autowired
	CreditCheckService creditCheckService;

	@Value("${sfdc.process.creditcheck.retrigger.queue}")
	String creditCheckRetriggerQueue;

	@Autowired
	QuoteLeCreditCheckAuditRepository quoteLeCreditCheckRepository;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobRepository;

	@Autowired
	MACDUtils macdUtils;

	@Value("${rabbitmq.customerle.credit.queue}")
	String customerLeCreditCheckQueue;

	@Value("${rabbitmq.customer.secs.queue}")
	String customerLeSecsQueue;

	@Value("${rabbitmq.related.task.deletion}")
	String taskCloseQueue;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Value("${rabbitmq.customer.secs.queue.by.flag}")
	String customerLeSecsQueueByFlag;

	@Value("${rabbitmq.get.partner.account.name.by.partner}")
	String partnerAccountNameMQ;
	
	@Autowired
	protected OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	private CommercialQuoteAuditRepository commercialQuoteAuditRepository;
	
	@Autowired
	GvpnCofValidatorService gvpnCofValidatorService;
	
	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Autowired
	QuoteAccessPermissionRepository quoteAccessPermissionRepository;
	
	@Autowired
	GvpnSlaService gvpnSlaService;
	
	@Value("${rabbitmq.o2c.amendment.task}")
	String amendmentTaskApi;
	
	@Autowired
	IllSiteRepository illSitesRepository;

	@Autowired
	OrderVrfSiteRepository orderVrfSiteRepository;

	@Autowired
	QuoteVrfSitesRepository quoteVrfSitesRepository;

	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	
	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;


	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;


	@Value("${application.env}")
	String appEnv;

	@Value("${rabbitmq.getOwnerDetailsSfdc.queue}")
	String ownerDetailsQueue;
	
	@Autowired
	CommercialBulkProcessSiteRepository commercialBulkProcessSiteRepository;
	
	@Autowired
	ProductSolutionRepository quoteProductSolutionRepository;

	@Autowired
	OrderSiteServiceTerminationDetailsRepository orderSiteServiceTerminationDetailsRepository;
	
	@Value("${bulkupload.max.count}")
	String minSiteLength;
	
	@Autowired
	OrderSiteBillingDetailsRepository orderSiteBillingInfoRepository;
	
	@Autowired
	QuoteSiteBillingDetailsRepository quoteSiteBillingInfoRepository;
	
	@Value("${rabbitmq.si.related.details.inactive.queue}")
	String siRelatedDetailsInactiveQueue;
	@Autowired
	private ProductSolutionSiLinkRepository productSolutionSiLinkRepository;
	
	@Autowired
	private OrderProductSolutionSiLinkRepository orderProductSolutionSiLinkRepository;
	
	@Autowired
	UtilityAuditRepository utilityAuditRepository;

	@Autowired
	FeasibilityPricingPayloadAuditRepository feasibilityPricingPayloadAuditRepository;

	/**
	 *
	 * createQuote - This method is used to create a quote The input validation is
	 * done and the corresponding tables are populated with initial set of values
	 * 
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteResponse createQuote(QuoteDetail quoteDetail, Integer erfCustomerId, Boolean ns) throws TclCommonException {
		QuoteResponse response = new QuoteResponse();
		try {
			if (ns == null) {
				ns = false;
			}
			validateQuoteDetail(quoteDetail);// validating the input for create Quote
			User user = getUserId(Utils.getSource());
			QuoteToLe quoteTole = processQuote(quoteDetail, erfCustomerId, user,ns);
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());
				response.setClassification(quoteTole.getClassification());
				if (quoteDetail.getQuoteId() == null && Objects.isNull(quoteDetail.getEngagementOptyId())) {
					// Triggering Sfdc Creation
					omsSfdcService.processCreateOpty(quoteTole, quoteDetail.getProductName());
				}
			}
			processQuoteAccessPermissions(user, quoteTole);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	
	/**
	 * processQuoteAccessPermissions - this method is used to save the quote permission 
	 * - Quote Created by Customer not visible to Sales
	 * - Quote Created by Sales not visible to Customer
	 * @param user
	 * @param quoteTole
	 */
	private void processQuoteAccessPermissions(User user, QuoteToLe quoteTole) {
		Integer prodFamilyId=null;
		List<QuoteToLeProductFamily> quoteToLeProductFamilys =quoteToLeProductFamilyRepository.findByQuoteToLe(quoteTole.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilys) {
			prodFamilyId=quoteToLeProductFamily.getMstProductFamily().getId();
		}
		QuoteAccessPermission quoteAccessPermission=null;
		List<QuoteAccessPermission> quoteAccessPermissions=quoteAccessPermissionRepository.findByProductFamilyIdAndTypeAndRefId(prodFamilyId, "QUOTE", quoteTole.getQuote().getQuoteCode());
		if(!quoteAccessPermissions.isEmpty()) {
			 quoteAccessPermission=quoteAccessPermissions.get(0);
		}else {
			quoteAccessPermission=new QuoteAccessPermission();
		}
		Quote quote=quoteTole.getQuote();
		quoteAccessPermission.setCreatedBy(Utils.getSource());
		quoteAccessPermission.setCreatedTime(new Date());
		quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
		quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
		quote.setIsCustomerView(CommonConstants.BACTIVE);
		quote.setIsSalesView(CommonConstants.BACTIVE);
		quoteAccessPermission.setProductFamilyId(prodFamilyId);
		quoteAccessPermission.setRefId(quoteTole.getQuote().getQuoteCode());
		quoteAccessPermission.setType("QUOTE");
		quoteAccessPermission.setUpdatedBy(Utils.getSource());
		quoteAccessPermission.setUpdatedTime(new Date());
		if (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
			quote.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quote.setIsSalesView(CommonConstants.BACTIVE);
		} else if (user.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BDEACTIVATE);
			quote.setIsCustomerView(CommonConstants.BACTIVE);
			quote.setIsSalesView(CommonConstants.BDEACTIVATE);
		}
		quoteRepository.save(quote);
		quoteAccessPermissionRepository.save(quoteAccessPermission);
	}

	/**
	 * persistQuoteLeAttributes
	 * 
	 * @param user
	 * @param quoteTole
	 */
	protected void persistQuoteLeAttributes(User user, QuoteToLe quoteTole) {
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_NAME, user.getFirstName());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_EMAIL, user.getEmailId());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_ID, user.getUsername());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_NO, user.getContactNo());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.DESIGNATION, user.getDesignation());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.RECURRING_CHARGE_TYPE, "ARC");

	}

	/**
	 * updateConstactInfo
	 * 
	 * @param quoteTole
	 * @param user
	 */
	private void updateLeAttribute(QuoteToLe quoteTole, User user, String name, String value) {
		MstOmsAttribute mstOmsAttribute = null;

		List<MstOmsAttribute> mstOmsAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(name, (byte) 1);

		if (!mstOmsAttributesList.isEmpty()) {
			mstOmsAttribute = mstOmsAttributesList.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(user.getUsername());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(name);
			mstOmsAttribute.setDescription(value);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}

		constructLegaAttribute(mstOmsAttribute, quoteTole, name, value);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructLegaAttribute used to
	 *       construct legal attributes
	 * @param mstOmsAttribute
	 * @param quoteTole
	 */
	private void constructLegaAttribute(MstOmsAttribute mstOmsAttribute, QuoteToLe quoteTole, String name,
			String value) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteTole, name);
		if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(value);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			attributeValue.setQuoteToLe(quoteTole);
			attributeValue.setDisplayValue(name);
			quoteLeAttributeValueRepository.save(attributeValue);
		} else {
			updateLeAttrbute(quoteLeAttributeValues, name, value);
		}

	}

	/**
	 * updateLeAttrbute
	 * 
	 * @param quoteLeAttributeValues
	 * @param name
	 * @param value
	 */
	private void updateLeAttrbute(List<QuoteLeAttributeValue> quoteLeAttributeValues, String name, String value) {
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setAttributeValue(value);
				attrVal.setDisplayValue(name);
				quoteLeAttributeValueRepository.save(attrVal);

			});
		}

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSiteInfo : this is used to fetch
	 *       the site details
	 * @param siteId
	 * @return
	 */
	public QuoteResponse getSiteInfo(Integer siteId) throws TclCommonException {
		QuoteResponse quoteResponse = null;
		try {
			quoteResponse = new QuoteResponse();
			List<QuoteIllSiteDto> illSiteDtos = new ArrayList<>();
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			illSiteDtos.add(new QuoteIllSiteDto(quoteIllSite));
			constructProductComponents(illSiteDtos);

			quoteResponse.setIllSiteDtos(illSiteDtos);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteResponse;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getTaxExcemptedSite
	 *       getTaxExcemptedSite this is used to fetch the Tax excempted site
	 *       details only
	 * 
	 * @param siteId
	 * @return QuoteResponse
	 */
	public QuoteResponse getTaxExemptedSite(Integer quoteId) throws TclCommonException {
		QuoteResponse quoteResponse = null;
		try {
			quoteResponse = new QuoteResponse();
			List<QuoteIllSiteDto> illSiteDtos = new ArrayList<>();
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			quote.getQuoteToLes().stream().forEach(quoteLE -> processQuoteLe(illSiteDtos, quoteLE));
			constructProductComponents(illSiteDtos);
			quoteResponse.setIllSiteDtos(illSiteDtos);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteResponse;
	}

	/**
	 * processQuoteLe
	 * 
	 * @param illSiteDtos
	 * @param quoteLE
	 */
	private void processQuoteLe(List<QuoteIllSiteDto> illSiteDtos, QuoteToLe quoteLE) {
		quoteLE.getQuoteToLeProductFamilies().stream()
				.forEach(quoProd -> processProductSolutions(illSiteDtos, quoProd));
	}

	/**
	 * processProductSolutions
	 * 
	 * @param illSiteDtos
	 * @param quoProd
	 */
	private void processProductSolutions(List<QuoteIllSiteDto> illSiteDtos, QuoteToLeProductFamily quoProd) {
		quoProd.getProductSolutions().stream().forEach(prodSol -> processIllSites(illSiteDtos, prodSol));
	}

	/**
	 * processIllSites
	 * 
	 * @param illSiteDtos
	 * @param prodSol
	 */
	private void processIllSites(List<QuoteIllSiteDto> illSiteDtos, ProductSolution prodSol) {
		prodSol.getQuoteIllSites().stream().forEach(ill -> processSiteTaxExempted(illSiteDtos, ill));
	}

	/**
	 * processSiteTaxExempted
	 * 
	 * @param illSiteDtos
	 * @param ill
	 */
	private void processSiteTaxExempted(List<QuoteIllSiteDto> illSiteDtos, QuoteIllSite ill) {
		if (ill.getIsTaxExempted() == 1) {
			illSiteDtos.add(new QuoteIllSiteDto(ill));

		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructProductComponents- This
	 *       method constructs the product components
	 * 
	 * @param illSiteDtos
	 */
	private void constructProductComponents(List<QuoteIllSiteDto> illSiteDtos) {
		illSiteDtos.forEach(illSites -> {
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(illSites.getId(), QuoteConstants.GVPN_SITES.toString());
			if (!productComponents.isEmpty()) {
				List<QuoteProductComponentDto> quoteProdDto = productComponents.stream()
						.map(QuoteProductComponentDto::new).collect(Collectors.toList());
				if (!quoteProdDto.isEmpty()) {
					quoteProdDto.forEach(this::processQuotePrice);
				}
				illSites.setQuoteProductComponentDtos(quoteProdDto);
			}
		});
	}

	/**
	 * processQuotePrice
	 * 
	 * @param quotProd
	 */
	private void processQuotePrice(QuoteProductComponentDto quotProd) {
		QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(quotProd.getId()),
				QuoteConstants.COMPONENTS.toString());
		if (quotProd.getMstProductComponent() != null) {
			quotProd.getMstProductComponent().setQuotePriceDto(new QuotePriceDto(price));
		}
		quotProd.getQuoteProductComponentsAttributeValues().stream().forEach(attr -> {
			QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(attr.getId()),
					QuoteConstants.ATTRIBUTES.toString());
			if (attrPrice != null) {
				attr.setQuotePriceDto(new QuotePriceDto(attrPrice));
			}

		});
	}
	
	public CreateDocumentDto createDocumentWrapper(CreateDocumentDto documentDto) throws TclCommonException {
		return createDocument(documentDto,0);
	}
	

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ createDocument this is used to
	 *       persist the customer and Supplier legal entity
	 * @param quoteId
	 * @return CreateDocumentDto
	 */
	@Transactional
	public CreateDocumentDto createDocument(CreateDocumentDto documentDto,int count) throws TclCommonException {
		CreateDocumentDto response = new CreateDocumentDto();
		Integer oldCustomerLegalEntityId = null;
		try {
			validateDocumentRequest(documentDto);

			//pipf-212
			persistLeOwnerDetailsSfdc(documentDto);

			Quote quote = quoteRepository.findByIdAndStatus(documentDto.getQuoteId(), (byte) 1);
			LOGGER.info("Quote received  is {}", quote);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			if (documentDto.getIllSitesIds() != null && !documentDto.getIllSitesIds().isEmpty()) {
				List<QuoteIllSite> illSites = illSiteRepository.findByIdInAndStatus(documentDto.getIllSitesIds(),
						(byte) 1);
				LOGGER.info("Ill sites received :  {}", illSites);
				illSites.forEach(illsite -> {
					illsite.setIsTaxExempted((byte) 1);
					illSiteRepository.save(illsite);
				});
			}

			Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(documentDto.getQuoteLeId());
			if (!optionalQuoteLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);

			}
			CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
			customerLeAttributeRequestBean.setCustomerLeId(documentDto.getCustomerLegalEntityId());
			if (quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
				customerLeAttributeRequestBean
						.setProductName(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
			} else {
				customerLeAttributeRequestBean.setProductName("GVPN");
			}
			LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
					Utils.convertObjectToJson(customerLeAttributeRequestBean));

			if (StringUtils.isNotEmpty(customerLeAttributes)) {
				updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
						CustomerLeDetailsBean.class), optionalQuoteLe.get());

			}

			/*if (StringUtils.isNotBlank(customerLeAttributes)) {
				CustomerLeDetailsBean customerLeDetailsBean = Utils.convertJsonToObject(customerLeAttributes, CustomerLeDetailsBean.class);
				String erfServiceInventoryParentOrderId = optionalQuoteLe.get().getErfServiceInventoryParentOrderId().toString();

				if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteLe.get().getQuoteType())) {
					if(StringUtils.isNotBlank(erfServiceInventoryParentOrderId)) {
						SIOrderDataBean orderData = macdUtils.getSiOrderData(erfServiceInventoryParentOrderId);

						*//*Setting billing parameters from Service Inventory*//*
						customerLeDetailsBean.setPreapprovedBillingMethod(orderData.getBillingMethod());
						customerLeDetailsBean.setPreapprovedPaymentTerm(orderData.getPaymentTerm());
						customerLeDetailsBean.setBillingFrequency(orderData.getBillingFrequency());
					}
				}
				if (Objects.nonNull(customerLeDetailsBean)) {
					updateBillingInfoForSfdc(customerLeDetailsBean, optionalQuoteLe.get());
				}
			}
*/


			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(optionalQuoteLe.get(), CommonConstants.QUOTE_SITE_TYPE);
			if (!quoteLeAttributeValues.isEmpty()) {
				long intlSites = quoteLeAttributeValues.stream().filter(quoteLeAttributeValue ->
						(quoteLeAttributeValue.getAttributeValue().equalsIgnoreCase(CommonConstants.INDIA_INTERNATIONAL_SITES)
								|| quoteLeAttributeValue.getAttributeValue().equalsIgnoreCase(CommonConstants.INTERNATIONAL_SITES))).count();
				if (intlSites > 0) {
					//update secs code only intl quote
					updateLegalSecsCodeInQuoteLeAttributes(documentDto, optionalQuoteLe.get());
				}
			}


			

			String spName = returnServiceProviderName(documentDto.getSupplierLegalEntityId());
			if (StringUtils.isNotEmpty(spName)) {
				processAccount(optionalQuoteLe.get(), spName, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
			}
			QuoteToLe quoteToLe = optionalQuoteLe.get();
			oldCustomerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
			quoteToLe.setErfCusCustomerLegalEntityId(documentDto.getCustomerLegalEntityId());
			quoteToLe.setErfCusSpLegalEntityId(documentDto.getSupplierLegalEntityId());
			if (Objects.isNull(quoteToLe.getQuoteCategory())
					|| (MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory()))) {
				if (quoteToLe.getStage().equals(QuoteStageConstants.CHECKOUT.getConstantCode())) {
					quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
				}

			}
			//Ns Orders
			if (("Y").equalsIgnoreCase(quoteToLe.getQuote().getNsQuote())&&quoteToLe.getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
				quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			}
			quoteToLeRepository.save(quoteToLe);
			LOGGER.info("quoteToLe saved successfully");

			CustomerDetail customerDetail = null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				customerDetail = new CustomerDetail();
				Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(documentDto.getCustomerId(),
						(byte) 1);
				customerDetail.setCustomerId(customer.getId());
			} else {
				customerDetail = userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
			}
			LOGGER.info("Customer details received {}", customerDetail);
//			CustomerDetail customerDetail = userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
			if (customerDetail != null && !customerDetail.getCustomerId().equals(quote.getCustomer().getId())) {
				Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
				if (customerEntity.isPresent()) {
					quote.setCustomer(customerEntity.get());
					quoteRepository.save(quote);
				}
			}

			processLocationDetailsAndSendToQueue(quoteToLe, quote.getCustomer().getErfCusCustomerId());

			if(!quote.getQuoteCode().startsWith(WebexConstants.UCAAS_WEBEX)){
				// Credit Check - Start
				LOGGER.info("Before triggering credit check");
				if(Objects.isNull(optionalQuoteLe.get().getQuoteType()) ||
						(Objects.nonNull(optionalQuoteLe.get().getQuoteType()) && optionalQuoteLe.get().getQuoteType().equals(CommonConstants.NEW))
						|| (Objects.nonNull(optionalQuoteLe.get()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteLe.get().getQuoteType()))
						|| (Objects.nonNull(optionalQuoteLe.get().getQuoteType()) && optionalQuoteLe.get().getQuoteType().equalsIgnoreCase(RenewalsConstant.RENEWALS))) {
					CustomerLeDetailsBean lePreapprovedValuesBean =     (CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
							CustomerLeDetailsBean.class);
					processAccount(optionalQuoteLe.get(), lePreapprovedValuesBean.getCreditCheckAccountType(), LeAttributesConstants.CREDIT_CHECK_ACCOUNT_TYPE.toString());
					creditCheckService.triggerCreditCheck(documentDto.getCustomerLegalEntityId(),  optionalQuoteLe, lePreapprovedValuesBean, oldCustomerLegalEntityId);
					response.setCreditCheckStatus(optionalQuoteLe.get().getTpsSfdcStatusCreditControl());
					response.setPreapprovedFlag(CommonConstants.BACTIVE.equals(optionalQuoteLe.get().getPreapprovedOpportunityFlag()) ? true: false );
					if(optionalQuoteLe.get().getQuoteType().equalsIgnoreCase(RenewalsConstant.RENEWALS)){
						quoteToLe.setTpsSfdcStatusCreditControl(optionalQuoteLe.get().getTpsSfdcStatusCreditControl());
						quoteToLe.setPreapprovedOpportunityFlag(optionalQuoteLe.get().getPreapprovedOpportunityFlag());
						quoteToLeRepository.save(quoteToLe);
					}
				}
				LOGGER.info("After triggering credit check");
				// Credit Check - End
			}


			if (!quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase()) &&
					!quote.getQuoteCode().startsWith(WebexConstants.UCAAS_WEBEX)) {
				omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
						SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe);
			}
//			if ((RenewalsConstant.RENEWALS.equalsIgnoreCase(quoteToLe.getQuoteType()))) {
//			omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
//					SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe);
//		}
			updateDemoQuoteLeAttributes(quoteToLe);
			quoteToLeRepository.flush();
		} catch (Exception e) {
			LOGGER.error("Error in creating document", e);
			if(count<3) {
				LOGGER.info(
						"Waiting for 10 seconds");
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e1) {
					LOGGER.warn("Sleep Intrepted");
				}
				LOGGER.info("Since it failed we will retry for {}",count);
				response=createDocument(documentDto,count+1);	
			}else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			
		}

		return response;
	}

	/**
	 * update secs code of legal entity in quote le attributes
	 *
	 * @param documentDto
	 * @param quoteToLe
	 */
	private void updateLegalSecsCodeInQuoteLeAttributes(CreateDocumentDto documentDto, QuoteToLe quoteToLe) {
		LOGGER.info("Update legal secs code in quote le attributes");
		String secsCode = getSecsCodeOfCustomerLegalEntity(documentDto.getCustomerLegalEntityId());
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(CUSTOMER_SECS_ID, (byte) 1);
		MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.stream().findFirst().get();
		}
		QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, CUSTOMER_SECS_ID);
		if (!quoteLeAttributeValues.isEmpty()) {
			LOGGER.info("Update the secs code in quote le attribute value ");
			quoteLeAttributeValue = quoteLeAttributeValues.stream().findFirst().get();
			quoteLeAttributeValue.setAttributeValue(secsCode);
		} else {
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValue.setAttributeValue(secsCode);
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setDisplayValue(CUSTOMER_SECS_ID);
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
			LOGGER.info("create the secs code in quote le attribute value id {} ", quoteLeAttributeValue.getId());
		}
	}

	/**
	 * Get secs code of customer legal entity
	 *
	 * @param customerLegalEntityId
	 */
	private String getSecsCodeOfCustomerLegalEntity(Integer customerLegalEntityId) {
		LOGGER.info("getSecsCode for CUSTOMER le ID {} ", customerLegalEntityId);
		String secsCode = null;
		try {
			secsCode = (String) mqUtils.sendAndReceive(customerLeSecsQueueByFlag, customerLegalEntityId.toString());

		} catch (TclCommonException e) {
			LOGGER.info("Error in getting secs code  ", e);
		}
		LOGGER.info("getSecsCode completed. SECS code {} ", secsCode);
		return secsCode;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @param familyName
	 * @link http://www.tatacommunications.com/ processLocationDetailsAndSendToQueue
	 *       used to send location info
	 * @param quoteId
	 * @return CreateDocumentDto
	 */
	private void processLocationDetailsAndSendToQueue(QuoteToLe quoteToLe, Integer erfCustomerId) {
		LOGGER.info("Inside processLocationDetailsAndSendToQueue method");
		try {
			CustomerLeLocationBean bean = constructCustomerLeAndLocation(quoteToLe, erfCustomerId);
			String request = Utils.convertObjectToJson(bean);
			LOGGER.info("Customer le location bean is {}", request);
			LOGGER.info("MDC Filter token value in before Queue call processLocationDetailsAndSendToQueue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
//			String response = (String) mqUtils.sendAndReceive(customerLeLocationQueue, request,
//					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(customerLeLocationQueue, request);

		} catch (Exception e) {
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param familyName
	 * @link http://www.tatacommunications.com/ processLocationDetailsAndSendToQueue
	 * @param quoteId
	 * @return CreateDocumentDto
	 */
	public CustomerLeLocationBean constructCustomerLeAndLocation(QuoteToLe quoteToLe, Integer erfCustomerId) {
		CustomerLeLocationBean customerLeLocationBean = new CustomerLeLocationBean();

		try {
			customerLeLocationBean.setErfCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
			customerLeLocationBean.setCustomerId(erfCustomerId);
			for (QuoteToLeProductFamily quoteProdFamilies : quoteToLe.getQuoteToLeProductFamilies()) {
				for (ProductSolution prodSolutions : quoteProdFamilies.getProductSolutions()) {
					for (QuoteIllSite illSite : prodSolutions.getQuoteIllSites()) {
						customerLeLocationBean.getLocationIds().add(illSite.getErfLocSitebLocationId());
					}

				}

			}
		} catch (Exception e) {
			// since it is and internal queue call so we are logging it only
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}

		return customerLeLocationBean;

	}

	/**
	 * processDeactivateSites -This method deletes or disables the illSites based on
	 * the action
	 * 
	 * @param siteId, quoteId, action
	 * @return QuoteDetail
	 */
	@Transactional
	public QuoteDetail processDeactivateSites(Integer siteId, Integer quoteId, String action)
			throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			quoteDetail = new QuoteDetail();
			validateDeleteSites(siteId, quoteId, action);

			if(tasksPending(quoteId)) {
				throw new TclCommonException(ExceptionConstants.TASKS_PENDING_FOR_QUOTE, ResponseResource.R_CODE_ERROR);
			}
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			QuoteToLe quoteToLe = quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
			if (action.equals(QuoteConstants.DELETE.toString())) {
				removeRelatedTasks(siteId);
				removeComponentsAndAttr(siteId);
				deletedIllsiteAndRelation(quoteIllSite);
			} else if (action.equals(QuoteConstants.DISABLE.toString())) {
				quoteIllSite.setStatus((byte) 0);
				illSiteRepository.save(quoteIllSite);
			}
			gvpnPricingFeasibilityService.recalculateSites(quoteToLe.getId());
			try {
				LOGGER.info("Calling set site type method");
				setQuoteTypesInQuoteToLeByLocationIds(quoteId, quoteToLe, getUserId(Utils.getSource()));
			} catch (Exception e) {
				LOGGER.error("Error on updating the Site Type ", e);
			}
		} catch (Exception e) {
			if(e instanceof TclCommonException)
				throw e;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;

	}

	private void removeRelatedTasks(Integer siteId) throws TclCommonException
	{

		LOGGER.info("MDC Filter token value in before Queue call removeRelatedTasks {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));

		try {
			mqUtils.sendAndReceive(taskCloseQueue, siteId.toString());
		} catch (TclCommonException e) {
			LOGGER.error("error in processing to queue call for closing task{}", e);
		}

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param familyName
	 * @link http://www.tatacommunications.com/ deletedIllsiteAndRelation used to
	 *       delete ill site and its relation
	 * 
	 * @param quoteIllSite
	 */
	private void deletedIllsiteAndRelation(QuoteIllSite quoteIllSite) {
		List<QuoteIllSiteSla> slas = quoteIllSiteSlaRepository.findByQuoteIllSite(quoteIllSite);
		if (slas != null && !slas.isEmpty()) {
			slas.forEach(sl -> {
				quoteIllSiteSlaRepository.delete(sl);
			});
		}
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibilities.forEach(site -> {
				List<SiteFeasibilityAudit> siteFeasibilityAuditList = siteFeasibilityAuditRepository
						.findBySiteFeasibility(site);
				if (!siteFeasibilityAuditList.isEmpty())
					siteFeasibilityAuditRepository.deleteAll(siteFeasibilityAuditList);
				siteFeasibilityRepository.delete(site);
			});
		}
		
		List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite(quoteIllSite);
		if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
			quoteIllSiteToServiceList.stream().forEach(quoteIllSiteToService -> {
				quoteIllSiteToServiceRepository.delete(quoteIllSiteToService);
			});
		}

		illSiteRepository.delete(quoteIllSite);
		
		// replace opportunity id in quote to le if needed - to handle multicircuit case - PIPF-22
		QuoteToLe quoteLe = quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		List<QuoteIllSiteToService> quoteSiteToServiceList = quoteIllSiteToServiceRepository.findByTpsSfdcParentOptyIdAndQuoteToLe(quoteLe.getTpsSfdcParentOptyId(), quoteLe);
		if(quoteSiteToServiceList != null && quoteSiteToServiceList.isEmpty()) {
			LOGGER.info("Replacing parent opty id in quote to le quoteToLeId {}", quoteLe.getId());
			List<QuoteIllSiteToService> siteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteLe.getId());
			if(siteToServiceList != null && !siteToServiceList.isEmpty()) {
				Optional<QuoteIllSiteToService> siteToService =  siteToServiceList.stream().findFirst();
				if(siteToService.isPresent()) {
					quoteLe.setTpsSfdcParentOptyId(siteToService.get().getTpsSfdcParentOptyId());
					quoteToLeRepository.save(quoteLe);
					LOGGER.info("Replacing parent opty id {} in quote to le quoteToLeId {}", siteToService.get().getTpsSfdcParentOptyId(), quoteLe.getId());
				}
			}
		}

	}

	/**
	 * removeComponentsAndAttr
	 * 
	 * @param siteId
	 */
	private void removeComponentsAndAttr(Integer siteId) {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(siteId, QuoteConstants.GVPN_SITES.toString());
		if (!quoteProductComponents.isEmpty()) {
			quoteProductComponents.forEach(quoteProd -> {

				quoteProd.getQuoteProductComponentsAttributeValues()
						.forEach(attr -> quoteProductComponentsAttributeValueRepository.delete(attr));
				quoteProductComponentRepository.delete(quoteProd);
			});
		}
	}

	/**
	 * 
	 * validateDeleteSites- validate the deleteSites
	 * 
	 * @param siteId
	 * @param quoteId
	 * @throws TclCommonException
	 */
	public void validateDeleteSites(Integer siteId, Integer quoteId, String action) throws TclCommonException {
		if (siteId == null || quoteId == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
		if (action == null || !(action.equals(QuoteConstants.DELETE.toString())
				|| action.equals(QuoteConstants.DISABLE.toString()))) {
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	/**
	 * saveQuote
	 * 
	 * updateSite - This method is used to update the site Informations
	 * 
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @param quoteId
	 * @return QuoteResponse
	 * @throws TclCommonException
	 */
	@Transactional
	public QuoteBean updateSite(QuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId)
			throws TclCommonException {
		QuoteBean quoteBean = new QuoteBean();
		try {
			LOGGER.info("Customer Id received is {}", erfCustomerId);
			validateSiteInformation(quoteDetail);
			User user = getUserId(Utils.getSource());
			List<Site> sites = quoteDetail.getSite();
			MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			if (quoteToLe.isPresent()) {
				for (Site site : sites) {
					QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
							.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
					String productOfferingName = site.getOfferingName();
					processSiteDetail(user, productFamily, quoteToLeProductFamily, site, productOfferingName);
				}
				quoteDetail.setQuoteId(quoteId);
				try {
					LOGGER.info("Calling set site type method");
					setQuoteTypesInQuoteToLeByLocationIds(quoteId, quoteToLe.get(), user);
				} catch (Exception e) {
					LOGGER.error("Error on updating the Site Type ", e);
				}
			}
			if (quoteToLe.isPresent()
					&& quoteToLe.get().getStage().equals(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode())) {
				quoteToLe.get().setStage(QuoteStageConstants.ADD_LOCATIONS.getConstantCode());
				quoteToLeRepository.save(quoteToLe.get());
			}

		} catch (Exception e) {
			LOGGER.warn("Error in Update Site {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteBean;
	}

	/**
	 * 
	 * getCustomerId- This method persists the customer if not present or get the
	 * customer details if already present
	 * 
	 * @param customerAcid
	 * @return Customer
	 * @throws TclCommonException
	 */
	private Customer getCustomerId(Integer erfCustomerId) throws TclCommonException {
		Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(erfCustomerId, (byte) 1);
		if (customer == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
		return customer;

	}

	/**
	 * 
	 * getUserId-This method get the user details if present or persist the user and
	 * get the entity
	 * 
	 * @param userData
	 * @return User
	 */
	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * 
	 * processQuote- This method builds the quote workflow step by step it creates
	 * by providing the initial set of values
	 * 
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @return Quote
	 * @throws TclCommonException
	 */
	protected QuoteToLe processQuote(QuoteDetail quoteDetail, Integer erfCustomerId, User user,Boolean ns)
			throws TclCommonException {
		Customer customer = null;
		if (erfCustomerId != null) {
			customer = getCustomerId(erfCustomerId);// get the customer Id
		} else {
			customer = user.getCustomer();
		}
		Quote quote = null;
		// Checking whether the input is for creating or updating
		if (quoteDetail.getQuoteleId() == null && quoteDetail.getQuoteId() == null) {
			quote = constructQuote(customer, user.getId(), quoteDetail.getProductName(),
					quoteDetail.getEngagementOptyId(), quoteDetail.getQuoteCode(),ns);
			quoteRepository.save(quote);
		} else {
			quote = quoteRepository.findByIdAndStatus(quoteDetail.getQuoteId(), CommonConstants.BACTIVE);
		}
		QuoteToLe quoteToLe = null;
		if (quoteDetail.getQuoteId() == null) {
			quoteToLe = constructQuoteToLe(quote, quoteDetail);
			//added for multiVrf
			quoteToLe.setIsMultiVrf("No");
			if(!StringUtil.isBlank(quoteDetail.getIsmultiVrf()) && quoteDetail.getIsmultiVrf()!=null ) {
				LOGGER.info("vrf isenabled flag"+quoteDetail.getIsmultiVrf());
				quoteToLe.setIsMultiVrf(quoteDetail.getIsmultiVrf());
			}
			quoteToLeRepository.save(quoteToLe);
		} else {
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			quoteToLe = quoteToLeEntity.isPresent() ? quoteToLeEntity.get() : null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				Opportunity opportunity = opportunityRepository.findByUuid(quote.getQuoteCode());
				quoteToLe.setClassification(opportunity.getOptyClassification());
				quoteToLe = quoteToLeRepository.save(quoteToLe);
			}
		}

		MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
		if (quoteToLeProductFamily == null) {
			quoteToLeProductFamily = constructQuoteToLeProductFamily(productFamily, quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		} else {
			removeUnselectedSolution(quoteDetail, quoteToLeProductFamily, quoteToLe);
		}

		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			quoteToLe.setQuoteToLeProductFamilies(
					Arrays.asList(quoteToLeProductFamily).stream().collect(Collectors.toSet()));
		}

		createSolutionDetails(quoteDetail, quoteToLe, productFamily, user, quoteToLeProductFamily);

		return quoteToLe;

	}

	/**
	 * createSolutionDetails
	 * 
	 * @param quoteDetail
	 * @param quoteToLe
	 * @param quoteToLeProductFamily
	 * @param user
	 * @param productFamily
	 */
	private void createSolutionDetails(QuoteDetail quoteDetail, QuoteToLe quoteToLe, MstProductFamily productFamily,
			User user, QuoteToLeProductFamily quoteToLeProductFamily) throws TclCommonException {
		List<ProductSolution> prodSolutionList = new ArrayList<>();
		quoteDetail.getSolutions().forEach(solution -> {
			try {
				String productOffering = solution.getOfferingName();
				MstProductOffering productOfferng = getProductOffering(productFamily, productOffering, user);
				ProductSolution productSolution = productSolutionRepository
						.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);

				if (productSolution == null) {
					productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
							Utils.convertObjectToJson(solution));
					productSolution.setSolutionCode(Utils.generateUid());
					productSolutionRepository.save(productSolution);

					if (quoteToLe.getTpsSfdcOptyId() != null && !quoteToLe.getQuote().getQuoteCode()
							.startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
						// adding productService to Sfdc
						omsSfdcService.processProductServiceForSolution(quoteToLe, productSolution,
								quoteToLe.getTpsSfdcOptyId());
					}
				} else {
					prodSolutionList.add(productSolution);
					productSolution.setProductProfileData(Utils.convertObjectToJson(solution));
					productSolutionRepository.save(productSolution);
					List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
							CommonConstants.BACTIVE);
					illSites.forEach(quoteIllSite -> {
						quoteIllSite.setProductSolution(prodSolutionList.get(0));
						illSiteRepository.save(quoteIllSite);
						removeComponentsAndAttr(quoteIllSite.getId());
						solution.getComponents().forEach(componentDetail -> {
							try {
								processProductComponent(productFamily, quoteIllSite, componentDetail, user);
							} catch (Exception e) {
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
										ResponseResource.R_CODE_ERROR);

							}

						});
						// Initializing siteProperty
						MstProductComponent sitePropComp = getMstProperties(user);
						List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
								.findByReferenceIdAndReferenceNameAndMstProductComponent(quoteIllSite.getId(),
										QuoteConstants.GVPN_SITES.toString(), sitePropComp);
						if (quoteProductComponents.isEmpty()) {
							QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
							quoteProductComponent.setMstProductComponent(sitePropComp);
							quoteProductComponent.setReferenceId(quoteIllSite.getId());
							quoteProductComponent.setMstProductFamily(productFamily);
							quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
							quoteProductComponentRepository.save(quoteProductComponent);
						}
					});

				}
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

			}

		});

	}

	/**
	 * removeUnselectedSolution-remove unselected solution
	 * 
	 * @param quoteDetail
	 * @param quoteToLeProductFamily
	 */
	private void removeUnselectedSolution(QuoteDetail quoteDetail, QuoteToLeProductFamily quoteToLeProductFamily,
			QuoteToLe quoteToLe) {
		List<ProductSolution> exprodSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		for (ProductSolution exproductSolution : exprodSolutions) {
			boolean remove = true;
			for (SolutionDetail solution : quoteDetail.getSolutions()) {
				if (solution.getOfferingName().equals(exproductSolution.getMstProductOffering().getProductName())) {
					remove = false;
					break;
				}
			}
			if (remove) {
				for (QuoteIllSite illSites : exproductSolution.getQuoteIllSites()) {
					removeComponentsAndAttr(illSites.getId());
					deletedIllsiteAndRelation(illSites);
				}
				// Trigger delete productSolution
				if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId()))
					omsSfdcService.processDeleteProduct(quoteToLe, exproductSolution);
				productSolutionRepository.delete(exproductSolution);
			}

		}
	}

	/**
	 * createQuoteLe processSolutionDetail- This method process the solution details
	 * ======= createQuoteLe ======= processSolutionDetail- This method process the
	 * solution details
	 * 
	 * @param user
	 * @param productFamily
	 * @param quoteToLeProductFamily
	 * @param solution
	 * @param productOfferingName
	 * @throws TclCommonException
	 */
	protected void processSiteDetail(User user, MstProductFamily productFamily,
			QuoteToLeProductFamily quoteToLeProductFamily, Site site, String productOfferingName)
			throws TclCommonException {
		try {
			MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName, user);
			ProductSolution productSolution = productSolutionRepository
					.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
			constructIllSites(productSolution, user, site, productFamily);

		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * processProductComponent- This method process the product component details
	 * 
	 * @param productFamily
	 * @param illSite
	 * @param component
	 * @param user
	 * @throws TclCommonException
	 */
	private void processProductComponent(MstProductFamily productFamily, QuoteIllSite illSite,
			ComponentDetail component, User user) throws TclCommonException {
		try {
			MstProductComponent productComponent = getProductComponent(component, user);
			QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily,
					illSite.getId());
			quoteComponent.setType(component.getType());
			quoteProductComponentRepository.save(quoteComponent);
			LOGGER.info("saved successfully");
			for (AttributeDetail attribute : component.getAttributes()) {

				processProductAttribute(quoteComponent, attribute, user);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * processProductAttribute- This method process the product attributes
	 * 
	 * @param quoteComponent
	 * @param attribute
	 * @param user
	 * @throws TclCommonException
	 */
	private void processProductAttribute(QuoteProductComponent quoteComponent, AttributeDetail attribute, User user) {
		ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
		QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
				productAttribute, attribute);
		quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
		/*if(attribute.getName().equalsIgnoreCase(CommonConstants.Burstable_Bandwidth))
		{
			Optional<QuoteIllSite> illSite=illSiteRepository.findById(quoteComponent.getReferenceId());
			QuoteToLe quoteToLe=illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
			processAttributePrice(quoteToLe,quoteProductAttribute,0D,0D,quoteComponent.getMstProductFamily(),0D);
		}*/
	}

	/**
	 * 
	 * constructQuote-This method constructs quote entity
	 * 
	 * @param customer
	 * @param userId
	 * @return Quote
	 */
	private Quote constructQuote(Customer customer, Integer userId, String prodName, String engagementOptyId,
			String quoteCode,Boolean ns) {
		Quote quoteExisting=quoteRepository.findByQuoteCode(quoteCode);
		Quote quote = new Quote();
		if(quoteExisting!=null){
			quote=quoteExisting;
		}
		quote.setCustomer(customer);
		quote.setCreatedBy(userId);
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setNsQuote(ns ? CommonConstants.Y : CommonConstants.N);
		quote.setQuoteCode(null != engagementOptyId ? quoteCode : Utils.generateRefId(prodName.toUpperCase()));
		quote.setEngagementOptyId(engagementOptyId);
		return quote;
	}

	/**
	 * 
	 * constructQuoteToLe -This method is used to construct QuoteToLe
	 * 
	 * @param quote
	 * @param quoteDetail
	 * @return QuoteToLe
	 */
	private QuoteToLe constructQuoteToLe(Quote quote, QuoteDetail quoteDetail) {
		List<QuoteToLe> existingQuoteToLe=quoteToLeRepository.findByQuote(quote);
		QuoteToLe quoteToLe = new QuoteToLe();
		if(existingQuoteToLe!=null&&!existingQuoteToLe.isEmpty()){
			quoteToLe=existingQuoteToLe.stream().findFirst().get();
		}
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		quoteToLe.setTermInMonths("12 months");
		quoteToLe.setCurrencyCode("INR");
		quoteToLe.setQuoteType("NEW");
		quoteToLe.setIsAmended(BDEACTIVATE);
		quoteToLe.setClassification(quoteDetail.getClassification());
		quoteToLe.setIsMultiCircuit(CommonConstants.BDEACTIVATE);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			Opportunity opportunity = opportunityRepository.findByUuid(quote.getQuoteCode());
			quoteToLe.setTpsSfdcOptyId(opportunity.getTpsOptyId());
			quoteToLe.setClassification(opportunity.getOptyClassification());
			quoteToLe.setErfCusCustomerLegalEntityId(partnerService
					.getCustomerLeIdFromEngagementOpportunityId(Integer.valueOf(quoteDetail.getEngagementOptyId())));
		}
		return quoteToLe;
	}

	/**
	 * 
	 * constructQuoteToLeProductFamily-This method construct the
	 * quoteToLeProductFamily entity
	 * 
	 * @param mstProductFamily
	 * @param quoteToLe
	 * @return QuoteToLeProductFamily
	 */

	private QuoteToLeProductFamily constructQuoteToLeProductFamily(MstProductFamily mstProductFamily,
			QuoteToLe quoteToLe) {
		QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
		quoteToLeProductFamily.setMstProductFamily(mstProductFamily);
		quoteToLeProductFamily.setQuoteToLe(quoteToLe);
		return quoteToLeProductFamily;

	}

	/**
	 * 
	 * constructProductSolution - This method is used to construct the product
	 * Solution entity
	 * 
	 * @param mstProductOffering
	 * @param quoteToLeProductFamily
	 * @param productProfileData
	 * @return ProductSolution
	 */

	private ProductSolution constructProductSolution(MstProductOffering mstProductOffering,
			QuoteToLeProductFamily quoteToLeProductFamily, String productProfileData) {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setMstProductOffering(mstProductOffering);
		productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		productSolution.setProductProfileData(productProfileData);
		return productSolution;
	}

	/**
	 * 
	 * constructIllSites- This methods is used to construct the IllSites entity
	 * 
	 * @param productSolution
	 * @param userId
	 * @return void
	 * @throws TclCommonException
	 */
	private void constructIllSites(ProductSolution productSolution, User user, Site site,
			MstProductFamily productFamily) throws TclCommonException {
		SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);

		List<SiteDetail> siteInp = site.getSite();
		for (SiteDetail siteDetail : siteInp) {
			if (siteDetail.getSiteId() == null) {
				QuoteIllSite illSite = new QuoteIllSite();
				illSite.setErfLocSiteaLocationId(siteDetail.getSecondLocationId());
				illSite.setErfLocSiteaSiteCode(siteDetail.getSecondLocationCode());
				illSite.setErfLocSitebLocationId(siteDetail.getLocationId());
				illSite.setErfLocSitebSiteCode(siteDetail.getLocationCode());
				illSite.setProductSolution(productSolution);
				Calendar cal = Calendar.getInstance();
				// cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 60); // Adding 60 days
				illSite.setEffectiveDate(cal.getTime());
				LOGGER.info("Setting Effective Date as : {}",cal.getTime());
				illSite.setCreatedBy(user.getId());
				illSite.setCreatedTime(new Date());
				illSite.setStatus((byte) 1);
				illSite.setImageUrl(soDetail.getImage());
				illSite.setSiteCode(Utils.generateUid());
				illSite.setFeasibility((byte) 0);
				illSite.setIsTaskTriggered(0);
				illSite.setMfTaskTriggered(0);
				illSite.setCommercialRejectionStatus("0");
				illSiteRepository.save(illSite);
				siteDetail.setSiteId(illSite.getId());
				for (ComponentDetail componentDetail : soDetail.getComponents()) {
					processProductComponent(productFamily, illSite, componentDetail, user);
				}
				// Initializing siteProperty
				MstProductComponent sitePropComp = getMstProperties(user);
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndReferenceNameAndMstProductComponent(illSite.getId(),
								QuoteConstants.GVPN_SITES.toString(), sitePropComp);
				if (quoteProductComponents.isEmpty()) {
					QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
					quoteProductComponent.setMstProductComponent(sitePropComp);
					quoteProductComponent.setReferenceId(illSite.getId());
					quoteProductComponent.setMstProductFamily(productFamily);
					quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
					quoteProductComponentRepository.save(quoteProductComponent);
				}
				QuoteToLe quoteToLe = productSolution.getQuoteToLeProductFamily().getQuoteToLe();
				if(quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE) || quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE)) {
					
					List<QuoteIllSiteToService> quoteSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdInAndQuoteToLe(siteDetail.getErfServiceInventoryTpsServiceId(), quoteToLe);
					if(!MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					if(quoteSiteToService != null && !quoteSiteToService.isEmpty()) {
						quoteSiteToService.stream().forEach(quoteSiteToServiceRecord -> {
						LOGGER.info("Updating quoteIllSite data in QuoteIllSiteToService for GVPN site id {} ", illSite.getId());
						quoteSiteToServiceRecord.setQuoteIllSite(illSite);
						quoteIllSiteToServiceRepository.save(quoteSiteToServiceRecord);
						});
					}
					} else {
						//ADD site - update existing record
						if(quoteSiteToService != null && !quoteSiteToService.isEmpty() && quoteSiteToService.get(0).getQuoteIllSite() == null) {
							quoteSiteToService.get(0).setQuoteIllSite(illSite);
							quoteIllSiteToServiceRepository.save(quoteSiteToService.get(0));
						} else {
							QuoteIllSiteToService siteToService = new QuoteIllSiteToService();
							siteToService.setErfServiceInventoryParentOrderId(quoteSiteToService.get(0).getErfServiceInventoryParentOrderId());
							siteToService.setErfServiceInventoryServiceDetailId(quoteSiteToService.get(0).getErfServiceInventoryServiceDetailId());
							siteToService.setErfServiceInventoryTpsServiceId(quoteSiteToService.get(0).getErfServiceInventoryTpsServiceId());
							siteToService.setQuoteToLe(quoteSiteToService.get(0).getQuoteToLe());
							siteToService.setTpsSfdcParentOptyId(quoteSiteToService.get(0).getTpsSfdcParentOptyId());
							siteToService.setType(quoteSiteToService.get(0).getType());
							siteToService.setAllowAmendment("NA");
							siteToService.setQuoteIllSite(illSite);							
							quoteIllSiteToServiceRepository.save(siteToService);
						}
					}
				}
			} else {
				QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(siteDetail.getSiteId(), (byte) 1);
				if (illSiteEntity != null) {
					illSiteEntity.setProductSolution(productSolution);
					illSiteRepository.save(illSiteEntity);
					removeComponentsAndAttr(illSiteEntity.getId());
					for (ComponentDetail componentDetail : soDetail.getComponents()) {
						processProductComponent(productFamily, illSiteEntity, componentDetail, user);
					}
				}
			}
		}

	}

	/**
	 * This method validates the Quote Details Request validateQuoteDetail
	 * 
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	protected void validateQuoteDetail(QuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	/**
	 * 
	 * validateGetQuoteDetail
	 * 
	 * @param quoteId
	 * @param erfCustomerId
	 * @throws TclCommonException
	 */
	protected void validateGetQuoteDetail(Integer quoteId) throws TclCommonException {
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	/**
	 * Thi method is used for validating the site information
	 * validateSiteInformation
	 * 
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	protected void validateSiteInformation(QuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null) || quoteDetail.getSite() == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

		}
	}

	/**
	 * 
	 * constructProductComponent- This method constructs the
	 * {@link QuoteProductComponent} Entity
	 * 
	 * @param productComponent
	 * @param mstProductFamily
	 * @param illSiteId
	 * @return QuoteProductComponent
	 * @throws TclCommonException
	 */
	protected QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer illSiteId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
		return quoteProductComponent;

	}

	/**
	 * 
	 * constructProductAttribute- This method constructs the
	 * {@link QuoteProductComponentsAttributeValue} Entity
	 * 
	 * @param quoteProductComponent
	 * @param productAttributeMaster
	 * @param attributeDetail
	 * @return QuoteProductComponentsAttributeValue
	 */
	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, AttributeDetail attributeDetail) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;

	}

	/**
	 * 
	 * getProductFamily - This methods gets the {@link MstProductFamily} from the
	 * given product name
	 * 
	 * @param productName
	 * @return MstProductFamily
	 * @throws TclCommonException
	 */
	protected MstProductFamily getProductFamily(String productName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;

	}

	/**
	 * 
	 * getProductComponent- This method takes the component name and gives the
	 * {@link MstProductComponent}
	 * 
	 * @param user
	 * 
	 * @param componentName
	 * @return MstProductComponent
	 * @throws TclCommonException
	 */
	private MstProductComponent getProductComponent(ComponentDetail component, User user) {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(component.getName(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setName(component.getName());
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}

		return mstProductComponent;
	}

	/**
	 * 
	 * getProductAttributes-This methods takes the attributeName and gets back
	 * {@link ProductAttributeMaster}
	 * 
	 * @param user
	 * 
	 * @param attributeName
	 * @return ProductAttributeMaster
	 * @throws TclCommonException
	 */
	private ProductAttributeMaster getProductAttributes(AttributeDetail attributeDetail, User user) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attributeDetail.getName());
			productAttributeMaster.setDescription(attributeDetail.getName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
	}

	/**
	 * 
	 * getProductOffering - This method takes in the
	 * {@link MstProductFamily},productOfferingName and gets back
	 * {@link MstProductOffering}
	 * 
	 * @param mstProductFamily
	 * @param productOfferingName
	 * @return MstProductOffering
	 * @throws TclCommonException
	 */
	protected MstProductOffering getProductOffering(MstProductFamily mstProductFamily, String productOfferingName,
			User user) {
		MstProductOffering productOffering = null;

		productOffering = mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(mstProductFamily,
				productOfferingName, (byte) 1);
		if (productOffering == null) {
			productOffering = new MstProductOffering();
			productOffering.setCreatedBy(user.getUsername());
			productOffering.setCreatedTime(new Date());
			productOffering.setMstProductFamily(mstProductFamily);
			productOffering.setProductName(productOfferingName);
			productOffering.setStatus((byte) 1);
			productOffering.setProductDescription(productOfferingName);
			mstProductOfferingRepository.save(productOffering);

		}
		return productOffering;
	}

	/**
	 * validateDocumentRequest- This method is used to validate the Document request
	 * 
	 * @param documentDto
	 */
	private void validateDocumentRequest(CreateDocumentDto documentDto) throws TclCommonException {

		if ((documentDto == null) || (documentDto.getQuoteId() == 0) || (documentDto.getCustomerLegalEntityId() == 0)
				|| (documentDto.getSupplierLegalEntityId() == 0)) {
			throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * getAllUsersByCustomer-This method is used to fetch all the user details based
	 * on customer getAllUsersByCustomer
	 * 
	 * @return UserDto List
	 * @throws TclCommonException
	 */
	public List<UserDto> getAllUsersByCustomer() throws TclCommonException {
		List<UserDto> userDtos = null;
		try {
			User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
			userDtos = user.getCustomer().getUsers().stream().map(UserDto::new).collect(Collectors.toList());

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return userDtos;
	}

	public TriggerEmailResponse getEmailIdAndTriggerEmail(Integer quoteId) throws TclCommonException {
		TriggerEmailResponse response = new TriggerEmailResponse(Status.SUCCESS.toString());
		try {
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				User users = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
				MailNotificationBean mailNotificationBean = new MailNotificationBean();
				mailNotificationBean.setCustomerEmail(users.getEmailId());
				mailNotificationBean.setOrderId(String.valueOf(quoteEntity.get().getQuoteCode()));
				mailNotificationBean.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
				mailNotificationBean.setProductName(CommonConstants.GVPN);
				if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
					QuoteToLe quoteToLe = quoteEntity.get().getQuoteToLes().stream().findFirst().get();
					mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
				}
				notificationService.salesOrderMultipleleNotification(mailNotificationBean);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return response;
	}

	/**
	 * processForgotPassword- This method process the forgot password it gets the
	 * email as the input and generates an reset url The reset url will have an
	 * expiry and can be only accessed by token
	 * 
	 * @param TriggerEmailRequest, ip address
	 * @return TriggerEmailResponse
	 * @throws TclCommonException
	 */
	public TriggerEmailResponse processTriggerMail(TriggerEmailRequest triggerEmailRequest, String initiatorIp)
			throws TclCommonException {
		TriggerEmailResponse triggerEmailResponse = new TriggerEmailResponse(Status.SUCCESS.toString());
		try {

			String userId = triggerEmailRequest.getEmailId();
			int quoteToLeId = triggerEmailRequest.getQuoteToLeId();
			validateTriggerInput(userId, quoteToLeId);
			User user = userRepository.findByEmailIdAndStatus(userId, 1);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				updateContactInfo(user, quoteToLe);

				Optional<QuoteDelegation> quoteDelExists = quoteDelegationRepository
						.findByQuoteToLeAndAssignToAndStatus(quoteToLe.get(), user.getId(),
								UserStatusConstants.OPEN.toString());
				if (!quoteDelExists.isPresent()) {
					QuoteDelegation quoteDelegation = new QuoteDelegation();
					quoteDelegation.setAssignTo(user.getId());
					quoteDelegation.setInitiatedBy(user.getCustomer().getId());
					quoteDelegation.setParentId(0);
					quoteDelegation.setStatus(UserStatusConstants.OPEN.toString());
					quoteDelegation.setType(UserStatusConstants.OTHERS.toString());
					quoteDelegation.setRemarks("");
					quoteDelegation.setIpAddress(initiatorIp);
					quoteDelegation.setIsActive((byte) 1);
					quoteDelegation.setQuoteToLe(quoteToLe.get());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					quoteDelegation.setCreatedTime(timestamp);
					quoteDelegationRepository.save(quoteDelegation);
				}
				String orderRefId = quoteToLe.get().getQuote().getQuoteCode();
				User customerUser = userRepository.findByEmailIdAndStatus(triggerEmailRequest.getEmailId(), 1);
				String leMail = getLeAttributes(quoteToLe.get(), LeAttributesConstants.LE_EMAIL);
				String pilotMail = pilotTeamMail != null ? pilotTeamMail[0] : null;
				String accManager = (leMail == null || leMail.equals(CommonConstants.HYPHEN)) ? pilotMail : leMail;

				MailNotificationBean mailNotificationBean = populateMailNotificationBean(
						getLeAttributes(quoteToLe.get(), LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY),
						customerUser.getCustomer().getCustomerName(), customerUser.getUsername(),
						customerUser.getContactNo(), triggerEmailRequest.getEmailId(), accManager, orderRefId,
						appHost + adminRelativeUrl, quoteToLe.get());
				notificationService.cofDelegationNotification(mailNotificationBean);

				MailNotificationBean mailNotificationBeanCofDelegate = populateMailNotificationBeanCofDelegate(
						quoteToLe.get(), triggerEmailRequest, user, orderRefId, accManager);
				notificationService.cofCustomerDelegationNotification(mailNotificationBeanCofDelegate);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return triggerEmailResponse;
	}

	private MailNotificationBean populateMailNotificationBeanCofDelegate(QuoteToLe quoteToLe,
			TriggerEmailRequest triggerEmailRequest, User user, String orderRefId, String leMail) {
		MailNotificationBean mailNotificationBeanCofDelegate = new MailNotificationBean();
		mailNotificationBeanCofDelegate.setCustomerName(user.getFirstName());
		mailNotificationBeanCofDelegate.setUserEmail(triggerEmailRequest.getEmailId());
		mailNotificationBeanCofDelegate.setAccountManagerEmail(leMail);
		mailNotificationBeanCofDelegate.setOrderId(orderRefId);
		mailNotificationBeanCofDelegate.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBeanCofDelegate.setProductName(GVPN);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBeanCofDelegate = populatePartnerClassification(quoteToLe, mailNotificationBeanCofDelegate);
		}
		return mailNotificationBeanCofDelegate;
	}

	private MailNotificationBean populateMailNotificationBean(String customerAccountName, String customerName,
			String userName, String userContactNumber, String userEmail, String accountManagerEmail, String orderRefId,
			String quoteLink, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setCustomerAccountName(customerAccountName);
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setUserContactNumber(userContactNumber);
		mailNotificationBean.setUserEmail(userEmail);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setQuoteLink(quoteLink);
		mailNotificationBean.setProductName(GVPN);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	private MailNotificationBean populatePartnerClassification(QuoteToLe quoteToLe,
			MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
					String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny()
					.get().getLegalEntityName();

			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

			mailNotificationBean.setClassification(quoteToLe.getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		} catch (Exception e) {
			LOGGER.warn("Error while reading end customer name :: {}", e.getStackTrace());
		}
		return mailNotificationBean;
	}

	/**
	 * updateContactInfo
	 * 
	 * @param user
	 * @param quoteToLe
	 */
	private void updateContactInfo(User user, Optional<QuoteToLe> opTQuoteLe) {
		if (user != null && opTQuoteLe.isPresent()) {
			QuoteToLe quToLe = opTQuoteLe.get();
			if (quToLe.getQuoteLeAttributeValues() != null && !quToLe.getQuoteLeAttributeValues().isEmpty()) {
				quToLe.getQuoteLeAttributeValues().forEach(attrval -> {
					if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_ID)) {
						attrval.setAttributeValue(String.valueOf(user.getId()));
						quoteLeAttributeValueRepository.save(attrval);

					} else if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_NAME)) {
						attrval.setAttributeValue(user.getFirstName());
						quoteLeAttributeValueRepository.save(attrval);

					} else if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_EMAIL)) {
						attrval.setAttributeValue(user.getEmailId());
						quoteLeAttributeValueRepository.save(attrval);

					} else if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_NO)) {
						attrval.setAttributeValue(user.getContactNo());
						quoteLeAttributeValueRepository.save(attrval);
					} else if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.DESIGNATION)) {
						attrval.setAttributeValue(user.getDesignation());
						quoteLeAttributeValueRepository.save(attrval);

					}
				});
			}

		}

	}

	private void validateTriggerInput(String userId, Integer quoteId) throws TclCommonException {
		if (userId == null || quoteId == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteId);
		if (!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
	}

	/**
	 * This method is used to send the email to the intended recipient
	 * 
	 * @param none
	 * @return QuoteDelegationDto
	 * @throws TclCommonException
	 */
	public QuoteDelegationDto getQuoteDelegation() throws TclCommonException {
		Optional<QuoteDelegation> quoteDelegation = null;
		QuoteDelegationDto quoteDelegationDto = null;
		try {

			User user = getUserId(Utils.getSource());
			quoteDelegation = quoteDelegationRepository.findByAssignToAndStatus(user.getId(),
					UserStatusConstants.OPEN.toString());
			if (quoteDelegation.isPresent()) {
				quoteDelegationDto = new QuoteDelegationDto(quoteDelegation.get());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDelegationDto;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getQuoteDetails- This method is used
	 *       to get the quote details
	 * 
	 * @param quoteId
	 * @param version
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public QuoteBean getQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProp, Boolean manualFeasibility)
			throws TclCommonException {
		QuoteBean response = null;
		try {
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString()));
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote, isFeasibleSites, isSiteProp, manualFeasibility);

			Optional<QuoteToLe> quoteToLe1 = quote.getQuoteToLes().stream().findFirst()
					.filter(quoteToLe -> MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()));

			if (quoteToLe1.isPresent()) {
				response.setIsAmended(quoteToLe1.get().getIsAmended());
				response.setQuoteType(quoteToLe1.get().getQuoteType());
				response.setQuoteCategory(quoteToLe1.get().getQuoteCategory());
				if (Objects.nonNull(quoteToLe1.get().getIsMultiCircuit())&&quoteToLe1.get().getIsMultiCircuit() == 1)
					response.setIsMultiCircuit(true);

			}

			// for termination ==================
			Optional<QuoteToLe> quoteToLeTerm = quote.getQuoteToLes().stream()
					.filter(quoteToLe -> MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())).findFirst();
			if (quoteToLeTerm.isPresent()) {
				response.setQuoteType(quoteToLeTerm.get().getQuoteType());
			}
			if (!quoteToLeTerm.isPresent()) {
				extractQuoteAccessPermission(response, quote);
			}
			Optional<User> user=userRepository.findById(quote.getCreatedBy());
			if(user.isPresent()) {
				response.setQuoteCreatedUserType(user.get().getUserType());
			}

			List<QuoteToLe> quoteLeList = quoteToLeRepository.findByQuote_Id(quoteId);
			Optional<QuoteToLe> quoteToLer = quoteLeList.stream().findFirst()
					.filter(quoteToLe -> RenewalsConstant.RENEWALS.equalsIgnoreCase(quoteToLe.getQuoteType()));

			if (quoteToLer.isPresent()) {
				response.setQuoteType(quoteToLer.get().getQuoteType());
				response.setIsCommercialChanges(findIsCommercial(quoteToLer).charAt(0));
				response.setIsMultiCircuit(quoteToLer.get().getIsMultiCircuit() == 1 ? true : false);
			}
			//PIPF-212
			getLeOwnerDetailsSFDC(response, quote);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_GET_QUOTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	private void extractQuoteAccessPermission(QuoteBean response, Quote quote) {
		for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
			for (QuoteToLeProductFamily quoteLeFamily : quoteLe.getQuoteToLeProductFamilies()) {
				List<QuoteAccessPermission> quoteAccessPermisions = quoteAccessPermissionRepository
						.findByProductFamilyIdAndTypeAndRefId(quoteLeFamily.getMstProductFamily().getId(), "QUOTE",
								quote.getQuoteCode());
				response.setQuoteAccess(QuoteAccess.FULL.toString());
				for (QuoteAccessPermission quoteAccessPermission : quoteAccessPermisions) {
					User user=userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
					if(user.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
						if(quoteAccessPermission.getIsCustomerView()!=null && quoteAccessPermission.getIsCustomerView()==CommonConstants.BACTIVE) {
							response.setQuoteAccess(QuoteAccess.FULL.toString());
						}else if(quoteAccessPermission.getIsCustomerView()!=null && quoteAccessPermission.getIsCustomerView()==CommonConstants.BDEACTIVATE) {
							response.setQuoteAccess(QuoteAccess.NO_ACCESS.toString());
						}else if(quoteAccessPermission.getIsCustomerView()!=null && quoteAccessPermission.getIsCustomerView()==CommonConstants.BTEN) {
							response.setQuoteAccess(QuoteAccess.RESTRICTED.toString());
						}
						
					}else if(user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())){
						if(quoteAccessPermission.getIsSalesView()!=null && quoteAccessPermission.getIsSalesView()==CommonConstants.BACTIVE) {
							response.setQuoteAccess(QuoteAccess.FULL.toString());
						}else if(quoteAccessPermission.getIsSalesView()!=null && quoteAccessPermission.getIsSalesView()==CommonConstants.BDEACTIVATE) {
							response.setQuoteAccess(QuoteAccess.NO_ACCESS.toString());
						}else if(quoteAccessPermission.getIsSalesView()!=null && quoteAccessPermission.getIsSalesView()==CommonConstants.BTEN) {
							response.setQuoteAccess(QuoteAccess.RESTRICTED.toString());
						}
					}else {
						response.setQuoteAccess(QuoteAccess.FULL.toString());
					}
				}
			}
		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	protected Quote getQuote(Integer quoteId) throws TclCommonException {

		Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quote == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}

		return quote;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @throws TclCommonException
	 */

	protected QuoteBean constructQuote(Quote quote, Boolean isFeasibleSites, Boolean isSiteProp, Boolean manualFeasibility)
			throws TclCommonException {
		QuoteBean quoteDto = new QuoteBean();
		quoteDto.setQuoteId(quote.getId());
		quoteDto.setQuoteCode(quote.getQuoteCode());
		quoteDto.setCreatedBy(quote.getCreatedBy());
		quoteDto.setCreatedTime(quote.getCreatedTime());
		quoteDto.setStatus(quote.getStatus());
		quoteDto.setNsQuote(quote.getNsQuote()!=null?(quote.getNsQuote().equals(CommonConstants.Y)?CommonConstants.Y:CommonConstants.N):CommonConstants.N);
		quoteDto.setTermInMonths(quote.getTermInMonths());
		List<QuoteToLe> quoteToLe=new ArrayList<QuoteToLe>();
		Opportunity optyentity=opportunityRepository.findByUuid(quote.getQuoteCode());
		if(optyentity!=null) {
			quoteDto.setOpportunityId(optyentity.getId()+"");
		}
		quoteToLe=quoteToLeRepository.findByQuote_Id(quote.getId());
		//added for multi vrf
		if(quoteToLe.get(0).getIsMultiVrf()!=null) {
		   quoteDto.setIsMultiVrf(quoteToLe.get(0).getIsMultiVrf());
		}
		else {
			quoteDto.setIsMultiVrf("No");
		}
		List<CommercialQuoteAudit> audit=commercialQuoteAuditRepository.findByQuoteId(quote.getId());
		if(audit.isEmpty() || audit.size()==0) {
			quoteDto.setIsInitialCommercialTrigger(true);
		}
		if (quoteToLe.size() != 0) {
			quoteDto.setQuoteStatus(quoteToLe.get(0).getCommercialStatus());
			if (quoteToLe.get(0).getCommercialQuoteRejectionStatus() != null) {
				quoteDto.setQuoteRejectionComment(quoteToLe.get(0).getQuoteRejectionComment());
				if (quoteToLe.get(0).getCommercialQuoteRejectionStatus().equalsIgnoreCase("1")) {
					quoteDto.setQuoteRejectionStatus(true);
				} else {
					quoteDto.setQuoteRejectionStatus(false);
				}
				LOGGER.info(" quote Commercial Status and rejection status and comments  is set as  {}",
						quoteToLe.get(0).getCommercialStatus() + ":"
								+ quoteToLe.get(0).getCommercialQuoteRejectionStatus() + ":"
								+ quoteToLe.get(0).getCommercialQuoteRejectionStatus());
			}
			
			LOGGER.info(" getIsCommercialTriggered"+quoteToLe.get(0).getIsCommercialTriggered());
			if(quoteToLe.get(0).getIsCommercialTriggered()==null ) {
				quoteDto.setIsCommercialTriggered(false);
			}
			else {
				quoteDto.setIsCommercialTriggered(true);
			}
			
			//added for bulk site commercial upload
			if (quoteToLe.get(0) != null) {
				List<OmsAttachment> omsAttachments = omsAttachmentRepository
						.findByQuoteToLeAndAttachmentTypeOrderByIdDesc(quoteToLe.get(0), "CommercialExcel");
				if (!omsAttachments.isEmpty()) {
					if (omsAttachments.get(0) != null && omsAttachments.get(0).getErfCusAttachmentId() != null) {
						LOGGER.info("AttachmentId:::" + omsAttachments.get(0).getErfCusAttachmentId());
						quoteDto.setBulkUploadId(omsAttachments.get(0).getErfCusAttachmentId());
					}
				}
				if (quoteToLe.get(0).getQuoteBulkUpdate() != null) {
					if (quoteToLe.get(0).getQuoteBulkUpdate().equalsIgnoreCase("1")) {
						quoteDto.setIsBulkUpload(true);
					}
				}
				List<CommercialBulkProcessSites> commercialBulkProcessSites=commercialBulkProcessSiteRepository.findByQuoteIdOrderByIdDesc(quote.getId());
				if(!commercialBulkProcessSites.isEmpty()) {
					quoteDto.setBulkUploadStatus(commercialBulkProcessSites.get(0).getStatus());
				}
			}

		}
		if (quote.getCustomer() != null) {
			quoteDto.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		}
		quoteDto.setLegalEntities(constructQuoteLeEntitDtos(quote, isFeasibleSites, isSiteProp, manualFeasibility));

		OrderConfirmationAudit auditEntity = orderConfirmationAuditRepository
				.findByOrderRefUuid(quoteDto.getQuoteCode());
		if (auditEntity != null && auditEntity.getPublicIp() != null) {
			quoteDto.setPublicIp(getPublicIp(auditEntity.getPublicIp()));
		} else {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			String forwardedIp = request.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
			if (forwardedIp != null) {
				LOGGER.info("Audit Public IP is {} ", forwardedIp);
				quoteDto.setPublicIp(getPublicIp(forwardedIp));
			}
		}
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quoteDto.getQuoteCode());
		quoteDto.setIsDocusign(docusignAudit != null);
		quoteDto.setCustomerName(quote.getCustomer().getCustomerName());

		//added for multisite
		Integer totalSiteCount = getTotalSiteCount(quote.getId());
		LOGGER.info("total Site Count {} ", totalSiteCount);
		quoteDto.setTotalSiteCount(totalSiteCount);
		quoteDto.setQuoteMaxCount(minSiteLength);
		//added for sitewise billing
		if (quoteToLe.get(0).getSiteLevelBilling() != null) {
			if (quoteToLe.get(0).getSiteLevelBilling().equalsIgnoreCase("1")) {
				quoteDto.setIsSiteBilling(true);
			}
		}
		

		return quoteDto;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @param detail
	 * @throws TclCommonException
	 */
	protected Order constructOrder(Quote quote, QuoteDetail detail) {
		LOGGER.info("Entered construct order for quote ---> {} ", quote.getQuoteCode());
		Order order = new Order();
		order.setCreatedBy(quote.getCreatedBy());
		order.setCreatedTime(new Date());
		order.setStatus(quote.getStatus());
		order.setTermInMonths(quote.getTermInMonths());
		order.setCustomer(quote.getCustomer());
		order.setEffectiveDate(quote.getEffectiveDate());
		order.setStatus(quote.getStatus());
		order.setQuote(quote);
		order.setStage(OrderStagingConstants.ORDER_CREATED.getStage());
		order.setEndDate(quote.getEffectiveDate());
		order.setStartDate(quote.getEffectiveDate());
		order.setStatus(quote.getStatus());
		order.setOrderCode(quote.getQuoteCode());
		order.setQuoteCreatedBy(quote.getCreatedBy());
		order.setEngagementOptyId(quote.getEngagementOptyId());
		orderRepository.save(order);
		LOGGER.info("Order created for quote -----> {} is -----> {} ", quote.getQuoteCode(), order.getId());
		order.setOrderToLes(constructOrderToLe(quote, order, detail));
		return order;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 * @param quote
	 * @throws TclCommonException
	 */
	private Set<QuoteToLeBean> constructQuoteLeEntitDtos(Quote quote, Boolean isFeasibleSites, Boolean isSiteProp, Boolean manualFeasibility)
			throws TclCommonException {

		Set<QuoteToLeBean> quoteToLeDtos = new HashSet<>();
		if ((quote != null) && (getQuoteToLeBasenOnVersion(quote)) != null) {
			for (QuoteToLe quTle : getQuoteToLeBasenOnVersion(quote)) {
				QuoteToLeBean quoteToLeDto = new QuoteToLeBean(quTle);
				quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
				quoteToLeDto.setCurrency(quTle.getCurrencyCode());
				quoteToLeDto.setLegalAttributes(constructLegalAttributes(quTle));
				quoteToLeDto.setProductFamilies(constructQuoteToLeFamilyDtos(getProductFamilyBasenOnVersion(quTle),
						isFeasibleSites, isSiteProp, manualFeasibility));
				quoteToLeDto.setClassification(quTle.getClassification());
				quoteToLeDto.setIsMultiCircuit(quTle.getIsMultiCircuit());
				if(Objects.nonNull(quTle.getIsDemo())){
					LOGGER.info("Demo flag is not null for quote ---> {} " , quTle.getQuote().getQuoteCode());
					int result = Byte.compare(quTle.getIsDemo(), BACTIVE);
					if(result==0){
						LOGGER.info("Entered into the block to set demo info in get quote details for quote -----> {} " , quTle.getQuote().getQuoteCode());
						quoteToLeDto.setIsDemo(true);
						quoteToLeDto.setDemoType(Objects.nonNull(quTle.getDemoType())?quTle.getDemoType():"");
					}
				}
				quoteToLeDtos.add(quoteToLeDto);
				partnerService.setExpectedArcAndNrcForPartnerQuote(quote.getQuoteCode(), quoteToLeDto);

			}
		}

		return quoteToLeDtos;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructLegalAttributes used to
	 *       construct legal attributes
	 * @param quTle
	 * @return
	 */
	private Set<LegalAttributeBean> constructLegalAttributes(QuoteToLe quTle) {

		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		List<QuoteLeAttributeValue> attributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quTle);
		if (attributeValues != null) {

			attributeValues.stream().forEach(attrVal -> {
				LegalAttributeBean attributeBean = new LegalAttributeBean();

				attributeBean.setAttributeValue(attrVal.getAttributeValue());
				attributeBean.setDisplayValue(attrVal.getDisplayValue());
				attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
				leAttributeBeans.add(attributeBean);

			});

		}
		return leAttributeBeans;
	}

	/**
	 * constructMstAttributBean
	 * 
	 * @param mstOmsAttribute
	 * @return
	 */
	private MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
		MstOmsAttributeBean mstOmsAttributeBean = null;
		if (mstOmsAttribute != null) {
			mstOmsAttributeBean = new MstOmsAttributeBean();
			mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
			mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
			mstOmsAttributeBean.setName(mstOmsAttribute.getName());
			mstOmsAttributeBean.setId(mstOmsAttribute.getId());
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
		}
		return mstOmsAttributeBean;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 * @param quote
	 * @param order
	 * @param detail
	 * @throws TclCommonException
	 */
	private Set<OrderToLe> constructOrderToLe(Quote quote, Order order, QuoteDetail detail) {

		return getOrderToLeBasenOnVersion(quote, order, detail);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	protected List<QuoteToLe> getQuoteToLeBasenOnVersion(Quote quote) {
		List<QuoteToLe> quToLes = null;
		quToLes = quoteToLeRepository.findByQuote(quote);
		return quToLes;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param order
	 * @param detail
	 * @link http://www.tatacommunications.com/
	 */
	private Set<OrderToLe> getOrderToLeBasenOnVersion(Quote quote, Order order, QuoteDetail detail) {
		List<QuoteToLe> quToLes = null;
		Set<OrderToLe> orderToLes = new HashSet<>();
		quToLes = quoteToLeRepository.findByQuote(quote);
		LOGGER.info("Entered contruct order to le for quote ---> {} ", quote.getQuoteCode());
		if (quToLes != null) {
			quToLes.forEach(quoteToLe -> {
				OrderToLe orderToLe = new OrderToLe();
				orderToLe.setFinalMrc(quoteToLe.getFinalMrc());
				orderToLe.setFinalNrc(quoteToLe.getFinalNrc());
				orderToLe.setFinalArc(quoteToLe.getFinalArc());
				orderToLe.setIsAmended(Objects.nonNull(quoteToLe.getIsAmended())?quoteToLe.getIsAmended():BDEACTIVATE);
				orderToLe.setOrder(order);
				orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
				orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
				orderToLe.setProposedArc(quoteToLe.getProposedArc());
				orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
				orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
				orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
				orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
				orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
				orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
				orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
				orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setClassification(quoteToLe.getClassification());
				orderToLe.setPreapprovedOpportunityFlag(quoteToLe.getPreapprovedOpportunityFlag());
				orderToLe.setTpsSfdcApprovedMrc(quoteToLe.getTpsSfdcApprovedMrc());
				orderToLe.setTpsSfdcApprovedNrc(quoteToLe.getTpsSfdcApprovedNrc());
				orderToLe.setTpsSfdcApprovedBy(quoteToLe.getTpsSfdcApprovedBy());
				orderToLe.setTpsSfdcReservedBy(quoteToLe.getTpsSfdcReservedBy());
				orderToLe.setTpsSfdcCreditApprovalDate(quoteToLe.getTpsSfdcCreditApprovalDate());
				orderToLe.setTpsSfdcCreditRemarks(quoteToLe.getTpsSfdcCreditRemarks());
				orderToLe.setTpsSfdcDifferentialMrc(quoteToLe.getTpsSfdcDifferentialMrc());
				orderToLe.setTpsSfdcStatusCreditControl(quoteToLe.getTpsSfdcStatusCreditControl());
				orderToLe.setVariationApprovedFlag(quoteToLe.getVariationApprovedFlag());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
				orderToLe.setTpsSfdcSecurityDepositAmount(quoteToLe.getTpsSfdcSecurityDepositAmount());
				orderToLe.setCreditCheckTrigerred(quoteToLe.getCreditCheckTriggered());
				orderToLe.setTpsSfdcCreditLimit(quoteToLe.getTpsSfdcCreditLimit());
				orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
				orderToLe.setIsMultiCircuit(quoteToLe.getIsMultiCircuit());
				orderToLe.setIsDemo(quoteToLe.getIsDemo());
				orderToLe.setDemoType(quoteToLe.getDemoType());
				//added for multivrf
				orderToLe.setIsMultiVrf(quoteToLe.getIsMultiVrf());
				orderToLe.setQuoteBulkUpdate(quoteToLe.getQuoteBulkUpdate());
				orderToLe.setSiteLevelBilling(quoteToLe.getSiteLevelBilling());
				orderToLeRepository.save(orderToLe);
				LOGGER.info("Order to le created for quote ---> is ----> " , quote.getQuoteCode(), orderToLe.getId());
				orderToLe.setOrdersLeAttributeValues(constructOrderToLeAttribute(orderToLe, quoteToLe));
				detail.getOrderLeIds().add(orderToLe.getId());
				orderToLe.setOrderToLeProductFamilies(getOrderProductFamilyBasenOnVersion(quote,
						quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId()), orderToLe, detail));
				orderToLes.add(orderToLe);
				if(quoteToLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
					List<OrderProductSolutionSiLink> orderProductSolutionSiLinks = new ArrayList<OrderProductSolutionSiLink>();
					List<ProductSolutionSiLink> productSolutionSiLinks =  productSolutionSiLinkRepository.findByQuoteToLeId(quoteToLe.getId());
					
					for(ProductSolutionSiLink productSolutionSiLink: productSolutionSiLinks) {
						OrderProductSolutionSiLink orderProductSolutionSiLink = new OrderProductSolutionSiLink();
						BeanUtils.copyProperties(productSolutionSiLink, orderProductSolutionSiLink);
						orderProductSolutionSiLink.setQuoteToLeId(orderToLe.getId());
						orderProductSolutionSiLinks.add(orderProductSolutionSiLink);
					}
					if(!orderProductSolutionSiLinks.isEmpty()) {
					orderProductSolutionSiLinkRepository.saveAll(orderProductSolutionSiLinks);
					}
				}
				//added for save order sitewise billing info
				if (quote != null && orderToLe.getSiteLevelBilling() != null) {
					LOGGER.info("get order SiteLevelBilling flag " + orderToLe.getSiteLevelBilling());
					if (orderToLe.getSiteLevelBilling().equalsIgnoreCase("1")) {
						List<QuoteSiteBillingDetails> quoteSiteBillingDetails = quoteSiteBillingInfoRepository
								.findByQuoteId(quote.getId());
						LOGGER.info("site billing list size" + quoteSiteBillingDetails.size());
						if (!quoteSiteBillingDetails.isEmpty()) {
							saveOrderSiteBillingDetails(order, quoteSiteBillingDetails);
						}
					}
				}
			});

		}

		return orderToLes;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param order
	 * @param detail
	 * @link http://www.tatacommunications.com/ constructOrderToLeAttribute use for
	 *       constructing attribute value
	 */
	private Set<OrdersLeAttributeValue> constructOrderToLeAttribute(OrderToLe orderToLe, QuoteToLe quoteToLe) {
		Set<OrdersLeAttributeValue> attributeValues = new HashSet<>();

		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
		if (quoteLeAttributeValues != null) {
			quoteLeAttributeValues.stream().forEach(attrVal -> {
				OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
				ordersLeAttributeValue.setAttributeValue(attrVal.getAttributeValue());
				ordersLeAttributeValue.setDisplayValue(attrVal.getDisplayValue());
				ordersLeAttributeValue.setMstOmsAttribute(attrVal.getMstOmsAttribute());
				ordersLeAttributeValue.setOrderToLe(orderToLe);
				ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
				attributeValues.add(ordersLeAttributeValue);

			});
		}

		return attributeValues;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<ProductSolution> getProductSolutionBasenOnVersion(QuoteToLeProductFamily family) {
		List<ProductSolution> productSolutions = null;
		productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(family);
		return productSolutions;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution) {
		List<QuoteIllSite> illsites = null;
		illsites = illSiteRepository.findByProductSolutionIdAndStatus(productSolution.getId(), (byte) 1);
		return illsites;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param isSitePropertiesNeeded
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */

	private List<QuoteProductComponent> getComponentBasenOnVersion(Integer siteId, boolean isSitePropertiesNeeded,
			Boolean isSiteProp) {
		List<QuoteProductComponent> components = null;
		if (isSitePropertiesNeeded) {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.GVPN_SITES.toString());
		} else if (isSiteProp) {
			components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
					QuoteConstants.GVPN_SITES.toString());
		} else {
			components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
					QuoteConstants.GVPN_SITES.toString());
			if (components != null) {
				return components.stream()
						.filter(cmp -> (!cmp.getMstProductComponent().getName()
								.equals(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties())))
						.collect(Collectors.toList());
			}

		}
		// Added for multi vrf
					Optional<QuoteIllSite> site=illSiteRepository.findById(siteId);
					if (site.isPresent()) {
						LOGGER.info("Inside siteid" + site.get().getId());
						List<QuoteVrfSites> vrfsites = quoteVrfSitesRepository.findByQuoteIllSite(site.get());
						if (!vrfsites.isEmpty() && vrfsites.size() != 0 && vrfsites!=null ) {
							for (QuoteVrfSites quoteVrfSites : vrfsites) {
								LOGGER.info("inside for vrf id and componenet name id:" + quoteVrfSites.getId() + "name"
										+ quoteVrfSites.getVrfName() + "type" + quoteVrfSites.getVrfType());
								QuoteProductComponent multiVrfcomponents = new QuoteProductComponent();
								List<QuoteProductComponent> prodComponent = null;
								List<MstProductComponent> mstProductComponent = mstProductComponentRepository
										.findByNameAndStatus(quoteVrfSites.getVrfName(), (byte) 1);
								MstProductFamily productFamily = new MstProductFamily();
								try {
									productFamily = getProductFamily("GVPN");
								} catch (TclCommonException e) {
									LOGGER.error("exception in getting family" + e);
								}
								if (!mstProductComponent.isEmpty()) {
									LOGGER.info("mst product name " + mstProductComponent.get(0).getName());
									prodComponent = quoteProductComponentRepository
											.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndTypeAndReferenceName(
													quoteVrfSites.getId(), mstProductComponent.get(0), productFamily,
													quoteVrfSites.getSiteType(),QuoteConstants.VRF_SITES.toString());
								}
								if (prodComponent != null && !prodComponent.isEmpty()) {
									multiVrfcomponents = prodComponent.get(0);
									components.add(multiVrfcomponents);
								}

							}
							LOGGER.info("componenet size" + components.size());
						}
					}

		return components;

	}
	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteProductComponentsAttributeValue> getAttributeBasenOnVersion(Integer componentId,
			boolean isSitePropRequire, boolean isSiteProp) {
		List<QuoteProductComponentsAttributeValue> attributes = null;

		attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);

		if (isSitePropRequire) {
			attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentId,
							IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
		} else if (isSiteProp) {
			attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);
		} else {
			if (attributes != null) {
				return attributes.stream()
						.filter(attr -> (!attr.getProductAttributeMaster().getName()
								.equals(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties())))
						.collect(Collectors.toList());
			}

		}

		return attributes;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteToLeProductFamily> getProductFamilyBasenOnVersion(QuoteToLe quote) {
		List<QuoteToLeProductFamily> prodFamilys = null;
		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		return prodFamilys;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param orderToLe
	 * @link http://www.tatacommunications.com/
	 */
	private Set<OrderToLeProductFamily> getOrderProductFamilyBasenOnVersion(Quote quote,
			List<QuoteToLeProductFamily> productFamilies, OrderToLe orderToLe, QuoteDetail detail) {
		Set<OrderToLeProductFamily> orderFamilys = new HashSet<>();
		if (productFamilies != null && !productFamilies.isEmpty()) {
			productFamilies.forEach(quoteToLeProductFamily -> {
				// For gsc gvpn combination.
				if (quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase(PDFConstants.GVPN)) {
					OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
					orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
					orderToLeProductFamily.setOrderToLe(orderToLe);
					orderToLeProductFamilyRepository.save(orderToLeProductFamily);
					orderToLeProductFamily.setOrderProductSolutions(constructOrderProductSolution(
							quoteToLeProductFamily.getProductSolutions(), orderToLeProductFamily, detail));

//					if (userInfoUtils.getUserType() != null
//							&& !userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
					if(!partnerService.quoteCreatedByPartner(quote.getId())) {
						processEngagement(quoteToLeProductFamily.getQuoteToLe(), quoteToLeProductFamily);
					}
//					}
					orderFamilys.add(orderToLeProductFamily);
				}
			}

			);

		}

		return orderFamilys;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructQuoteToLeFamilyDtos
	 * @param quoteToLeProductFamilies
	 * @throws TclCommonException
	 */
	private Set<QuoteToLeProductFamilyBean> constructQuoteToLeFamilyDtos(
			List<QuoteToLeProductFamily> quoteToLeProductFamilies, Boolean isFeasibleSites, Boolean isSiteProp, Boolean manualFeasibility)
			throws TclCommonException {
		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilyBeans = new HashSet<>();
		if (quoteToLeProductFamilies != null) {
			quoteToLeProductFamilies.forEach(quFamily -> {
				LOGGER.info("Quote to le family is ----> for quote ----> {} ", quFamily.getId(), quFamily.getQuoteToLe().getQuote().getQuoteCode() );
				try {
					if (quFamily.getMstProductFamily().getName().equalsIgnoreCase(GvpnConstants.GVPN)) {
						QuoteToLeProductFamilyBean quoteToLeProductFamilyBean = new QuoteToLeProductFamilyBean();
						if (quFamily.getMstProductFamily() != null) {
							quoteToLeProductFamilyBean.setStatus(quFamily.getMstProductFamily().getStatus());
							quoteToLeProductFamilyBean.setProductName(quFamily.getMstProductFamily().getName());
						}
						List<ProductSolutionBean> solutionBeans = getSortedSolution(constructProductSolution(
								getProductSolutionBasenOnVersion(quFamily), isFeasibleSites, isSiteProp, manualFeasibility));
						quoteToLeProductFamilyBean.setSolutions(solutionBeans);
						quoteToLeProductFamilyBeans.add(quoteToLeProductFamilyBean);
					}
				} catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);

				}
			});

		}

		return quoteToLeProductFamilyBeans;
	}

	private List<ProductSolutionBean> getSortedSolution(List<ProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(ProductSolutionBean::getProductSolutionId));
		}

		return solutionBeans;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 * @param productSolutions
	 * @return Set<ProductSolutionBean>
	 * @throws TclCommonException
	 */
	private List<ProductSolutionBean> constructProductSolution(List<ProductSolution> productSolutions,
			Boolean isFeasibleSites, Boolean isSiteProp, Boolean manualFeasibility) throws TclCommonException {
		List<ProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				ProductSolutionBean productSolutionBean = new ProductSolutionBean();
				productSolutionBean.setProductSolutionId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					productSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					productSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					productSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}
				if (solution.getProductProfileData() != null) {
					productSolutionBean.setSolution((SolutionDetail) Utils
							.convertJsonToObject(solution.getProductProfileData(), SolutionDetail.class));
				}
				List<QuoteIllSiteBean> illSiteBeans = getSortedIllSiteDtos(
						constructIllSiteDtos(getIllsitesBasenOnVersion(solution), isFeasibleSites, isSiteProp, manualFeasibility));
				productSolutionBean.setSites(illSiteBeans);
				productSolutionBeans.add(productSolutionBean);

			}
		}
		return productSolutionBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 * @param productSolutions
	 * @param orderToLeProductFamily
	 * @return Set<ProductSolutionBean>
	 */
	private Set<OrderProductSolution> constructOrderProductSolution(Set<ProductSolution> productSolutions,
			OrderToLeProductFamily orderToLeProductFamily, QuoteDetail detail) {

		Set<OrderProductSolution> orderProductSolution = new HashSet<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				List<QuoteIllSite> quoteIllSites = getIllsitesBasenOnVersion(solution);
				if (quoteIllSites != null && !quoteIllSites.isEmpty()) {
					OrderProductSolution oSolution = new OrderProductSolution();
					if (solution.getMstProductOffering() != null) {
						oSolution.setMstProductOffering(solution.getMstProductOffering());
					}
					oSolution.setSolutionCode(solution.getSolutionCode());
					oSolution.setTpsSfdcProductId(solution.getTpsSfdcProductId());
					oSolution.setTpsSfdcProductName(solution.getTpsSfdcProductName());
					oSolution.setOrderToLeProductFamily(orderToLeProductFamily);
					oSolution.setProductProfileData(solution.getProductProfileData());
					orderProductSolutionRepository.save(oSolution);
					LOGGER.info("ProductSolutionID:{}",oSolution.getId());
					oSolution.setOrderIllSites(constructOrderIllSite(quoteIllSites, oSolution, detail));
					orderProductSolution.add(oSolution);
				}

			}
		}

		return orderProductSolution;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<QuoteIllSiteBean> constructIllSiteDtos(List<QuoteIllSite> illSites, Boolean isFeasibleSites,
			Boolean isSiteProp, Boolean manualFeasibility) throws TclCommonException {
		if (isSiteProp == null) {
			isSiteProp = false;
		}
		List<QuoteIllSiteBean> sites = new ArrayList<>();
		if (illSites != null) {
			for (QuoteIllSite illSite : illSites) {
				if (illSite.getStatus() == 1) {
					if (!isFeasibleSites && !illSite.getFeasibility().equals(CommonConstants.BACTIVE)) {
						continue;
					}
					QuoteToLe quoteToLe = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					QuoteIllSiteBean illSiteBean = new QuoteIllSiteBean(illSite);
					illSiteBean.setQuoteSla(constructSlaDetails(illSite));
					illSiteBean.setFeasibility(constructSiteFeasibility(illSite));
					List<QuoteProductComponentBean> quoteProductComponentBeans = getSortedComponents(
							constructQuoteProductComponent(illSite.getId(), false, isSiteProp));
					illSiteBean.setComponents(quoteProductComponentBeans);
					illSiteBean.setChangeBandwidthFlag(illSite.getMacdChangeBandwidthFlag());
					illSiteBean.setIsTaskTriggered(illSite.getIsTaskTriggered());
					illSiteBean.setMfStatus(illSite.getMfStatus());

					
					//add rejection flow
					if (illSite.getCommercialRejectionStatus() != null) {
					  if(illSite.getCommercialRejectionStatus().equalsIgnoreCase("1")) {
						illSiteBean.setRejectionStatus(true);
						}
						else {
							illSiteBean.setRejectionStatus(false);
						}
					}
					
					if (illSite.getCommercialApproveStatus() != null) {
						if (illSite.getCommercialApproveStatus().equalsIgnoreCase("1")) {
							illSiteBean.setApproveStatus(true);
						} else {
							illSiteBean.setApproveStatus(false);
						}
					}
					if(illSiteBean.getRejectionStatus() || illSiteBean.getApproveStatus()) {
						illSiteBean.setIsActionTaken(true);
					}
					
					//added for bulk site commercial upload
					if (illSite.getSiteBulkUpdate() != null) {
						if (illSite.getSiteBulkUpdate().equalsIgnoreCase("1")) {
							illSiteBean.setBulkuploadStatus(true);
						} else {
							illSiteBean.setBulkuploadStatus(false);
						}
					}
					
					// added for site level billing gst
					QuoteSiteBillingDetails billinginfo = quoteSiteBillingInfoRepository.findByQuoteIllSite(illSite);
					if (billinginfo != null) {
						LOGGER.info("billing site gst no gvpn"+billinginfo.getGstNo()+"siteid:::"+illSite.getId());
						illSiteBean.setSiteBillingGstNo(billinginfo.getGstNo());
					}


						Map<String,String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSite(illSite,quoteToLe);
						LOGGER.info("Service Ids"+serviceIds);
						if(serviceIds.size()==2)
						{
							illSiteBean.setServiceId(serviceIds.get(PDFConstants.PRIMARY));
							illSiteBean.setLinkType(PDFConstants.PRIMARY);
							
							if(CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
								LOGGER.info("constructing duplicate site for secondary");
								QuoteIllSiteBean illSiteBeanSecondary = new QuoteIllSiteBean(illSite);
								illSiteBeanSecondary.setServiceId(serviceIds.get(PDFConstants.SECONDARY));
								illSiteBeanSecondary.setLinkType(PDFConstants.SECONDARY);	
								illSiteBeanSecondary.setQuoteSla(constructSlaDetails(illSite));
								illSiteBeanSecondary.setFeasibility(constructSiteFeasibility(illSite));
								List<QuoteProductComponentBean> quoteProductComponentBeansSec = getSortedComponents(
										constructQuoteProductComponent(illSite.getId(), false, isSiteProp));
								illSiteBeanSecondary.setComponents(quoteProductComponentBeansSec);
								illSiteBeanSecondary.setChangeBandwidthFlag(illSite.getMacdChangeBandwidthFlag());
								illSiteBeanSecondary.setIsTaskTriggered(illSite.getIsTaskTriggered());
								illSiteBeanSecondary.setMfStatus(illSite.getMfStatus());
								if(quoteToLe.getQuoteType() != null && (quoteToLe.getQuoteType().equalsIgnoreCase("MACD")
										|| quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE))) {
									illSiteBeanSecondary
									.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe,
											quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE) ? illSiteBeanSecondary : illSiteBean,manualFeasibility)));
								}
								sites.add(illSiteBeanSecondary);
								
								if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
									LOGGER.info("Getting termination related details for quoteToLe Id {}", quoteToLe.getId());
									List<QuoteSiteServiceTerminationDetailsBean> terminationSiteServiceBeanList = new ArrayList<>();
									List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndType(illSite.getId(),PDFConstants.SECONDARY);
									if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
										quoteIllSiteToServiceList.stream().forEach(siteToService ->{
											LOGGER.info("siteToService serviceId secondary {}", siteToService.getErfServiceInventoryTpsServiceId());
											QuoteSiteServiceTerminationDetails terminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);
											QuoteSiteServiceTerminationDetailsBean terminationSiteServiceBean = new QuoteSiteServiceTerminationDetailsBean();
											BeanUtils.copyProperties(terminationDetail, terminationSiteServiceBean);
											terminationSiteServiceBean.setQuoteIllSiteToServiceId(siteToService.getId());
											terminationSiteServiceBean.setQuoteSiteId(siteToService.getQuoteIllSite().getId());
											terminationSiteServiceBean.setServiceId(siteToService.getErfServiceInventoryTpsServiceId());
											terminationSiteServiceBean.setServiceLinkType(siteToService.getType());
											LOGGER.info("terminationSiteServiceBean for secondary {}", terminationSiteServiceBean.toString());
											terminationSiteServiceBeanList.add(terminationSiteServiceBean);
										});
									}
									illSiteBeanSecondary.setQuoteSiteServiceTerminationsBean(terminationSiteServiceBeanList);
								}
								
							}
						}
						else if(serviceIds.size()==1)
						{
							if (Objects.nonNull(serviceIds.get(PDFConstants.PRIMARY))) {
								illSiteBean.setServiceId(serviceIds.get(PDFConstants.PRIMARY));
								illSiteBean.setLinkType(PDFConstants.PRIMARY);
							} else {
								illSiteBean.setServiceId(serviceIds.get(PDFConstants.SECONDARY));
								illSiteBean.setLinkType(PDFConstants.SECONDARY);
							}

					}
					if(quoteToLe.getQuoteType() != null && (quoteToLe.getQuoteType().equalsIgnoreCase("MACD")
							|| quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE))) {
						illSiteBean.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe, illSiteBean, manualFeasibility)));
					}
					sites.add(illSiteBean);
					
					// Termination specific data
					if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
						LOGGER.info("Getting termination related details for quoteToLe Id {}", quoteToLe.getId());
						List<QuoteSiteServiceTerminationDetailsBean> terminationSiteServiceBeanList = new ArrayList<>();
						LOGGER.info("LinkType of site  {} : {}",illSite.getId(),illSiteBean.getLinkType());
						List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndType(illSite.getId(),illSiteBean.getLinkType());
						if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
							quoteIllSiteToServiceList.stream().forEach(siteToService ->{
								LOGGER.info("siteToService serviceId {}", siteToService.getErfServiceInventoryTpsServiceId());
								QuoteSiteServiceTerminationDetails terminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);
								QuoteSiteServiceTerminationDetailsBean terminationSiteServiceBean = new QuoteSiteServiceTerminationDetailsBean();
								BeanUtils.copyProperties(terminationDetail, terminationSiteServiceBean);
								terminationSiteServiceBean.setQuoteIllSiteToServiceId(siteToService.getId());
								terminationSiteServiceBean.setQuoteSiteId(siteToService.getQuoteIllSite().getId());
								terminationSiteServiceBean.setServiceId(siteToService.getErfServiceInventoryTpsServiceId());
								terminationSiteServiceBean.setServiceLinkType(siteToService.getType());
								LOGGER.info("terminationSiteServiceBean {}", terminationSiteServiceBean.toString());
								terminationSiteServiceBeanList.add(terminationSiteServiceBean);
							});
						}
						
						illSiteBean.setQuoteSiteServiceTerminationsBean(terminationSiteServiceBeanList);
						
					}
				}
			}
		}
		return sites;
	}


	/**
	 * Method to generate existing components for multif
	 * @param quoteToLe
	 * @return
	 */
	private List<MACDExistingComponentsBean> generateExistingComponentsForMacd(QuoteToLe quoteToLe, QuoteIllSiteBean illSiteBean, Boolean manualFeasibility)
	{
		List<MACDExistingComponentsBean> existingComponentsBeanList=new ArrayList<>();
		List<String> serviceIdsList=new ArrayList<>();
		Optional<QuoteIllSite> quoteIllSite=illSiteRepository.findById(illSiteBean.getSiteId());
		Map<String,String> serviceIdsMap=macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite.get(),quoteToLe);
		if(Objects.nonNull(serviceIdsMap)) {
			if(quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE)){
				String serviceId = serviceIdsMap.get(illSiteBean.getLinkType());
				if (Objects.nonNull(serviceId))
					serviceIdsList.add(serviceId);
			}
			else
			{
				String primaryServiceId = serviceIdsMap.get(PDFConstants.PRIMARY);
				String secondaryServiceId = serviceIdsMap.get(PDFConstants.SECONDARY);
				if (Objects.nonNull(primaryServiceId))
					serviceIdsList.add(primaryServiceId);
				if (Objects.nonNull(secondaryServiceId))
					serviceIdsList.add(secondaryServiceId);
			}
		}
		if(!serviceIdsList.isEmpty())
		{
			serviceIdsList.stream().forEach(serviceId->{
				try {
					MACDExistingComponentsBean existingComponent = new MACDExistingComponentsBean();
					//order Id need to be removed
					List<Map> existingComponentMap = constructExistingComponentsforIsvPage(quoteToLe, illSiteBean, serviceId, manualFeasibility);
					existingComponent.setServiceId(serviceId);
					existingComponent.setExistingComponents(existingComponentMap);
					existingComponentsBeanList.add(existingComponent);
				}
				catch(Exception e)
				{
					LOGGER.error("Error ",e);
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}
			});

		}
		return existingComponentsBeanList;
	}

	/**
	 * Method to display the existing components in ISV page for primary and
	 * secondary
	 * 
	 * @param quoteToLe
	 * @param illSiteBean
	 * @return Map<String ,Object>
	 * @throws TclCommonException
	 */

	private List<Map> constructExistingComponentsforIsvPage(QuoteToLe quoteToLe, QuoteIllSiteBean illSiteBean,String serviceId, Boolean manualFeasibility)
			throws TclCommonException {
		List<Map> existingComponentsList = new ArrayList<>();

	/*	String serviceId = quoteToLe.getErfServiceInventoryTpsServiceId();
		Integer orderId = quoteToLe.getErfServiceInventoryParentOrderId();*/
		String amended = "";
		if (quoteToLe.getIsAmended() != null
				&& quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE) && MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			amended="yes";
		}
		String secondaryServiceId = null;

		if (Objects.nonNull(quoteToLe) && (MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
				|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()))
				&& Objects.nonNull(serviceId)) {
			Map<String, Object> primaryComponentsMap = new HashMap<>();
			Map<String, Object> secondaryComponentsMap = new HashMap<>();
			Boolean isSecondary = false;
			// PIPF-167 - show service details in MF workbench even if service id is inactive after order is placed.
			SIServiceDetailDataBean sIServiceDetailDataBean = null;
			if (Boolean.TRUE.equals(manualFeasibility)) {
				LOGGER.info("constructExistingComponentsforIsvPage manualFeasibility {}, serviceId {}",
						manualFeasibility, serviceId);
				sIServiceDetailDataBean = macdUtils.getServiceDetailForInactiveServicesOnceOrderIsPlaced(serviceId,
						quoteToLe.getQuoteCategory(), quoteToLe);

			} else if (MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(serviceId);
			} else {
				sIServiceDetailDataBean = macdUtils.getServiceDetail(serviceId, quoteToLe.getQuoteCategory());
			}

			SIServiceDetailDataBean secondaryaryServiceDataBean = new SIServiceDetailDataBean();
			SIServiceDetailDataBean primaryServiceDataBean = new SIServiceDetailDataBean();
			isSecondary = Objects.nonNull(sIServiceDetailDataBean.getPriSecServLink());
			String linkType = sIServiceDetailDataBean.getLinkType();

			LOGGER.info("Link Type: " + sIServiceDetailDataBean.getLinkType());
			if (linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SINGLE")) {
				if (isSecondary) {
					secondaryServiceId = sIServiceDetailDataBean.getPriSecServLink();
					// PIPF-167 - show service details in MF workbench even if service id is inactive after order is placed.
					//PIPF-418 to add existing components for amendment in cwb
					if (Boolean.TRUE.equals(manualFeasibility) || "yes".equalsIgnoreCase(amended)) {
						LOGGER.info("constructExistingComponentsforIsvPage manualFeasibility {}, serviceId {}",
								manualFeasibility, secondaryServiceId);
						secondaryaryServiceDataBean = macdUtils.getServiceDetailForInactiveServicesOnceOrderIsPlaced(
								secondaryServiceId, quoteToLe.getQuoteCategory(), quoteToLe);

					} else if (MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
						secondaryaryServiceDataBean = macdUtils.getServiceDetailIASTermination(secondaryServiceId);
					} else {
						secondaryaryServiceDataBean = macdUtils.getServiceDetail(secondaryServiceId,
								quoteToLe.getQuoteCategory());
					}

				}
				primaryServiceDataBean = sIServiceDetailDataBean;
			}

			else if (isSecondary && linkType.equalsIgnoreCase("SECONDARY")) {
				serviceId = sIServiceDetailDataBean.getPriSecServLink();
				// PIPF-167 - show service details in MF workbench even if service id is inactive after order is placed.
				//PIPF-418 to add existing components for amendment in cwb
				if (Boolean.TRUE.equals(manualFeasibility) || "yes".equalsIgnoreCase(amended)) {
					LOGGER.info("constructExistingComponentsforIsvPage manualFeasibility {}, serviceId {}",
							manualFeasibility, serviceId);
					primaryServiceDataBean = macdUtils.getServiceDetailForInactiveServicesOnceOrderIsPlaced(serviceId,
							quoteToLe.getQuoteCategory(), quoteToLe);

				} else if (MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
					primaryServiceDataBean = macdUtils.getServiceDetailIASTermination(serviceId);
				} else {
					primaryServiceDataBean = macdUtils.getServiceDetail(serviceId, quoteToLe.getQuoteCategory());
				}

				secondaryaryServiceDataBean = sIServiceDetailDataBean;
			}

			primaryComponentsMap.put("type", MACDConstants.PRIMARY_STRING);
			primaryComponentsMap.put("contractTerm",
					Objects.nonNull(primaryServiceDataBean.getContractTerm()) ? primaryServiceDataBean.getContractTerm()
							: 0);
			primaryComponentsMap.put("portBw",
					Objects.nonNull(primaryServiceDataBean.getPortBw()) ? primaryServiceDataBean.getPortBw()
							: CommonConstants.NULL);
			primaryComponentsMap.put("oldArc",
					Objects.nonNull(primaryServiceDataBean.getArc()) ? primaryServiceDataBean.getArc() : 0);
			primaryComponentsMap.put("oldNrc",
					Objects.nonNull(primaryServiceDataBean.getNrc()) ? primaryServiceDataBean.getNrc() : 0);
			primaryComponentsMap.put("oldMrc",
					Objects.nonNull(primaryServiceDataBean.getMrc()) ? primaryServiceDataBean.getMrc() : 0);
			primaryComponentsMap.put("serviceId", Objects.nonNull(serviceId) ? serviceId : CommonConstants.NULL);

			//PIPF-425
			primaryComponentsMap.put("lmProvider", Objects.nonNull(primaryServiceDataBean.getLastMileProvider())? primaryServiceDataBean.getLastMileProvider() : "NA");
			if(Objects.nonNull(primaryServiceDataBean) && Objects.nonNull(primaryServiceDataBean.getAttributes())){
				primaryServiceDataBean.getAttributes().forEach(attribute->{
					LOGGER.info("Attribute in construct exiting components is ---> {} ", attribute);
					if("CPE Basic Chassis".equalsIgnoreCase(attribute.getName())){
						primaryComponentsMap.put("existingCpe", Objects.nonNull(attribute.getValue())? attribute.getValue() : "NA");
					}
				});
			}
			if(!primaryComponentsMap.containsKey("existingCpe")){
				primaryComponentsMap.put("existingCpe","NA");
			}
			primaryComponentsMap.put("contractExpiryDate", Objects.nonNull(primaryServiceDataBean.getContractEndDate())? primaryServiceDataBean.getContractEndDate() : "0000-00-00000:00:00.000+0000");


			if(quoteToLe.getQuoteCategory() != null && quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)){
				primaryServiceDataBean.getAttributes().stream().filter(attr-> "Demo Period in days".equalsIgnoreCase(attr.getName())).findFirst().ifPresent(
						attribute->{
							LOGGER.info("Setting Contract term for Demo");
							primaryComponentsMap.replace("contractTerm", primaryComponentsMap.get("contractTerm"), attribute.getValue()+"days");
						}
				);
			}
			
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())){
				primaryComponentsMap.put("contractStartDate",primaryServiceDataBean.getsCommisionDate());
				
				List<SIPriceRevisionDetailDataBean> priceRevisionDetailsList = macdUtils.getPriceRevisionDetailForTermination(serviceId);
				if(priceRevisionDetailsList != null && !priceRevisionDetailsList.isEmpty()) {
					if(MACDConstants.RENEWAL_WO_PRC_RVS.equalsIgnoreCase(primaryServiceDataBean.getCurrentOpportunityType())) {
						primaryComponentsMap.put("prDate", priceRevisionDetailsList.get(0).getEffDateOfPriceRevision());
					} else {
						primaryComponentsMap.put("prDate",priceRevisionDetailsList.get(0).getMaxPriceRevDate());
					}
				}
				List<PricingEngineResponse> pricingEngineResponseList = pricingDetailsRepository.findBySiteCode(illSiteBean.getSiteCode());
				if(pricingEngineResponseList != null && !pricingEngineResponseList.isEmpty()) {
					
					String lmMRC = null;
					try {
						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObj = (JSONObject) jsonParser.parse(pricingEngineResponseList.get(0).getResponseData());
						lmMRC = (String) jsonObj.get("prev_ll_mrc");
					} catch (org.json.simple.parser.ParseException e) {
						LOGGER.info("Exception {}", e.getMessage());
						throw new TclCommonRuntimeException(e);
					}
					
					primaryComponentsMap.put("lmMRC", lmMRC);
				}
			}
			
			existingComponentsList.add(0, primaryComponentsMap);

			if (isSecondary) {
				secondaryComponentsMap.put("type", MACDConstants.SECONDARY_STRING);
				secondaryComponentsMap.put("contractTerm",
						Objects.nonNull(secondaryaryServiceDataBean.getContractTerm())
								? secondaryaryServiceDataBean.getContractTerm()
								: 0);
				secondaryComponentsMap.put("portBw",
						Objects.nonNull(secondaryaryServiceDataBean.getPortBw())
								? secondaryaryServiceDataBean.getPortBw()
								: CommonConstants.NULL);
				secondaryComponentsMap.put("oldArc",
						Objects.nonNull(secondaryaryServiceDataBean.getArc()) ? secondaryaryServiceDataBean.getArc()
								: 0);
				secondaryComponentsMap.put("oldNrc",
						Objects.nonNull(secondaryaryServiceDataBean.getNrc()) ? secondaryaryServiceDataBean.getNrc()
								: 0);
				secondaryComponentsMap.put("oldMrc",
						Objects.nonNull(secondaryaryServiceDataBean.getMrc()) ? secondaryaryServiceDataBean.getMrc()
								: 0);
				secondaryComponentsMap.put("serviceId", secondaryaryServiceDataBean.getTpsServiceId());

				//PIPF-425
				secondaryComponentsMap.put("lmProvider", Objects.nonNull(secondaryaryServiceDataBean.getLastMileProvider())? secondaryaryServiceDataBean.getLastMileProvider() : "NA");
				if(Objects.nonNull(secondaryaryServiceDataBean) && Objects.nonNull(secondaryaryServiceDataBean.getAttributes())){
					secondaryaryServiceDataBean.getAttributes().forEach(attribute->{
						LOGGER.info("Attribute in construct exiting components is ---> {} ", attribute);
						if("CPE Basic Chassis".equalsIgnoreCase(attribute.getName())){
							secondaryComponentsMap.put("existingCpe", Objects.nonNull(attribute.getValue())? attribute.getValue() : "NA");
						}
					});
				}
				if(!secondaryComponentsMap.containsKey("existingCpe")){
					secondaryComponentsMap.put("existingCpe","NA");
				}
				secondaryComponentsMap.put("contractExpiryDate", Objects.nonNull(secondaryaryServiceDataBean.getContractEndDate())? secondaryaryServiceDataBean.getContractEndDate() : "0000-00-00000:00:00.000+0000");


				if( quoteToLe.getQuoteCategory() != null && quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)){
					secondaryaryServiceDataBean.getAttributes().stream().filter(attr-> "Demo Period in days".equalsIgnoreCase(attr.getName())).findFirst().ifPresent(
							attribute->{
								LOGGER.info("Setting Contract term for Demo Secondary");
								secondaryComponentsMap.replace("contractTerm", secondaryComponentsMap.get("contractTerm"), attribute.getValue()+"days");
							}
					);
				}
				
				if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())){
					secondaryComponentsMap.put("contractStartDate",primaryServiceDataBean.getsCommisionDate());
					
					List<SIPriceRevisionDetailDataBean> priceRevisionDetailsList = macdUtils.getPriceRevisionDetailForTermination(serviceId);
					if(priceRevisionDetailsList != null && !priceRevisionDetailsList.isEmpty()) {
						if(MACDConstants.RENEWAL_WO_PRC_RVS.equalsIgnoreCase(primaryServiceDataBean.getCurrentOpportunityType())) {
							secondaryComponentsMap.put("prDate", priceRevisionDetailsList.get(0).getEffDateOfPriceRevision());
						} else {
							secondaryComponentsMap.put("prDate",priceRevisionDetailsList.get(0).getMaxPriceRevDate());
						}
					}
					
					List<PricingEngineResponse> pricingEngineResponseList = pricingDetailsRepository.findBySiteCode(illSiteBean.getSiteCode());
					if(pricingEngineResponseList != null && !pricingEngineResponseList.isEmpty()) {
						
						String lmMRC = null;
						try {
							JSONParser jsonParser = new JSONParser();
							JSONObject jsonObj = (JSONObject) jsonParser.parse(pricingEngineResponseList.get(0).getResponseData());
							lmMRC = (String) jsonObj.get("prev_ll_mrc");
						} catch (org.json.simple.parser.ParseException e) {
							LOGGER.info("Exception {}", e.getMessage());
							throw new TclCommonRuntimeException(e);
						}
						
						secondaryComponentsMap.put("lmMRC", lmMRC);
					}
				}
				
				existingComponentsList.add(1, secondaryComponentsMap);

			}

		}
		return existingComponentsList;
	}

	public Integer getOrderIdFromServiceId(String tpsId) throws TclCommonException {
		String responseOrderId = (String) mqUtils.sendAndReceive(orderIdCorrespondingToServId, tpsId);
		return (Integer) Utils.convertJsonToObject(responseOrderId, Integer.class);
	}

	/**
	 * constructSiteFeasibility
	 * 
	 * @param illSite
	 * @return
	 */
	private List<SiteFeasibilityBean> constructSiteFeasibility(QuoteIllSite illSite) {
		List<SiteFeasibilityBean> siteFeasibilityBeans = new ArrayList<>();
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(illSite,
				(byte) 1);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			for (SiteFeasibility siteFeasibility : siteFeasibilities) {
				siteFeasibilityBeans.add(constructSiteFeasibility(siteFeasibility));
			}
		}
		return siteFeasibilityBeans;
	}

	/**
	 * constructSiteFeasibility
	 * 
	 * @param siteFeasibility
	 * @return
	 */
	private SiteFeasibilityBean constructSiteFeasibility(SiteFeasibility siteFeasibility) {
		SiteFeasibilityBean siteFeasibilityBean = new SiteFeasibilityBean();
		siteFeasibilityBean.setFeasibilityCheck(siteFeasibility.getFeasibilityCheck());
		siteFeasibilityBean.setFeasibilityCode(siteFeasibility.getFeasibilityCode());
		siteFeasibilityBean.setFeasibilityMode(siteFeasibility.getFeasibilityMode());
		siteFeasibilityBean.setType(siteFeasibility.getType());
		siteFeasibilityBean.setCreatedTime(siteFeasibility.getCreatedTime());
		siteFeasibilityBean.setProvider(siteFeasibility.getProvider());
		siteFeasibilityBean.setRank(siteFeasibility.getRank());
		siteFeasibilityBean.setResponseJson(siteFeasibility.getResponseJson());
		siteFeasibilityBean.setIsSelected(siteFeasibility.getIsSelected());
		siteFeasibilityBean.setFeasibilityType(siteFeasibility.getFeasibilityType());
		siteFeasibilityBean.setSfdcFeasibilityId(siteFeasibility.getSfdcFeasibilityId());
		return siteFeasibilityBean;
	}

	/**
	 * constructSlaDetails
	 * 
	 * @param illSite
	 */
	private List<QuoteSlaBean> constructSlaDetails(QuoteIllSite illSite) {

		List<QuoteSlaBean> quoteSlas = new ArrayList<>();
		if (illSite.getQuoteIllSiteSlas() != null) {

			illSite.getQuoteIllSiteSlas().forEach(siteSla -> {
				QuoteSlaBean sla = new QuoteSlaBean();
				sla.setId(siteSla.getId());
				sla.setSlaEndDate(siteSla.getSlaEndDate());
				sla.setSlaStartDate(siteSla.getSlaStartDate());
				sla.setSlaValue(siteSla.getSlaValue());
				if (siteSla.getSlaMaster() != null) {
					SlaMaster slaMaster = siteSla.getSlaMaster();
					SlaMasterBean master = new SlaMasterBean();
					master.setId(siteSla.getId());
					master.setSlaDurationInDays(slaMaster.getSlaDurationInDays());
					master.setSlaName(slaMaster.getSlaName());
					sla.setSlaMaster(master);
				}

				quoteSlas.add(sla);
			});
		}

		// ordering sla's
		quoteSlas.sort(Comparator.comparingInt(QuoteSlaBean::getId));
		return quoteSlas;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<QuoteProductComponentBean> getSortedComponents(List<QuoteProductComponentBean> quoteComponentBeans) {
		if (quoteComponentBeans != null) {
			quoteComponentBeans.sort(Comparator.comparingInt(QuoteProductComponentBean::getComponentId));

		}

		return quoteComponentBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<QuoteProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<QuoteProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(QuoteProductComponentsAttributeValueBean::getAttributeId));

		}

		return attributeBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @param oSolution
	 * @link http://www.tatacommunications.com/ constructIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private Set<OrderIllSite> constructOrderIllSite(List<QuoteIllSite> illSites, OrderProductSolution oSolution,
			QuoteDetail detail) {
		Set<OrderIllSite> sites = new HashSet<>();

		if (illSites != null) {
			for (QuoteIllSite illSite : illSites) {
				if (illSite.getStatus() == 1 && illSite.getFeasibility() == 1) {
					OrderIllSite orderSite = new OrderIllSite();
					orderSite.setIsTaxExempted(illSite.getIsTaxExempted());
					orderSite.setStatus((byte) 1);
					orderSite.setErfLocSiteaLocationId(illSite.getErfLocSiteaLocationId());
					orderSite.setErfLocSitebLocationId(illSite.getErfLocSitebLocationId());
					orderSite.setErfLocSiteaSiteCode(illSite.getErfLocSiteaSiteCode());
					orderSite.setErfLocSitebSiteCode(illSite.getErfLocSitebSiteCode());
					orderSite.setErfLrSolutionId(illSite.getErfLrSolutionId());
					orderSite.setImageUrl(illSite.getImageUrl());
					orderSite.setCreatedBy(illSite.getCreatedBy());
					orderSite.setCreatedTime(new Date());
					orderSite.setFeasibility(illSite.getFeasibility());
					orderSite.setOrderProductSolution(oSolution);
					if (illSite.getEffectiveDate() != null) {
						orderSite.setEffectiveDate(illSite.getEffectiveDate());

					} else {
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date()); // Now use today date.
						cal.add(Calendar.DATE, 60); // Adding 60 days
						orderSite.setEffectiveDate(cal.getTime());
						LOGGER.info("Setting Effective Date as : {}",cal.getTime());
					}
					// orderSite.setRequestorDate(new Date());
					orderSite.setMrc(illSite.getMrc());
					orderSite.setFpStatus(illSite.getFpStatus());
					orderSite.setArc(illSite.getArc());
					orderSite.setTcv(illSite.getTcv());
					orderSite.setSiteCode(illSite.getSiteCode());
					orderSite.setStage(SiteStagingConstants.CONFIGURE_SITES.getStage());
					orderSite.setNrc(illSite.getNrc());
					orderSite.setSiteBulkUpdate(illSite.getSiteBulkUpdate());
					orderSite.setErfServiceInventoryTpsServiceId(illSite.getErfServiceInventoryTpsServiceId());
					orderIllSitesRepository.save(orderSite);
					persistOrderSiteAddress(illSite.getErfLocSitebLocationId(), "b",String.valueOf(orderSite.getId()),QuoteConstants.GVPN_SITES.toString());//Site
					persistOrderSiteAddress(illSite.getErfLocSiteaLocationId(), "a",String.valueOf(orderSite.getId()),QuoteConstants.GVPN_SITES.toString());//Pop
					orderSite.setOrderSiteFeasibility(constructOrderSiteFeasibility(illSite, orderSite));
					orderSite.setOrderIllSiteSlas(constructOrderSiteSla(illSite, orderSite));
					String quoteType = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType();
					 LOGGER.info("quoteToLe quote type {}",quoteType );
					QuoteToLe quoteToLe = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					if(quoteType != null && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteType)){
						LOGGER.info("constructOrderIllSiteToService for Gvpn siteid  {} ",illSite.getId());
						List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository.findByQuoteIllSite(illSite);
						constructOrderIllSiteToService(illSite, orderSite,quoteIllSiteServices);
						if (quoteToLe.getIsAmended() != null
								&& quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE)) {
							updateAmendmentStatus(quoteIllSiteServices, quoteToLe);
						}
					}else  if(quoteType != null && MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteType)){
						List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository.findByQuoteIllSite(illSite);
						constructOrderIllSiteToServiceTermination(illSite, orderSite,quoteIllSiteServices);
					} else {
						if (quoteToLe.getIsAmended() != null
								&& quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE)) {
							List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository
									.findByQuoteIllSite(illSite);
							updateAmendmentStatus(quoteIllSiteServices, quoteToLe);
							for (QuoteIllSiteToService quoteIllSiteService : quoteIllSiteServices) {
								OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
								orderIllSiteToService.setO2cServiceId(quoteIllSiteService.getO2cServiceId());
								orderIllSiteToService.setOrderIllSite(orderSite);
								orderIllSiteToService.setOrderToLe(orderSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe());
								orderIllSiteToService.setParentOrderId(quoteIllSiteService.getParentOrderId());
								orderIllSiteToService.setParentSiteId(quoteIllSiteService.getParentSiteId());
								orderIllSiteToServiceRepository.save(orderIllSiteToService);
							}
						}
					}
					constructOrderProductComponent(illSite.getId(), orderSite);
					persistQuoteSiteCommercialsAtServiceIdLevel(illSite, quoteToLe, orderSite);
					sites.add(orderSite);
				} else {
					detail.setManualFeasible(true);
				}
			}
		}

		return sites;
	}
	
	private void updateAmendmentStatus(List<QuoteIllSiteToService> quoteIllSiteServices, QuoteToLe quoteToLe) {
		try {
			for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteServices) {
				String o2cServiceId = quoteIllSiteToService.getO2cServiceId();
				if (StringUtils.isNotBlank(o2cServiceId)) {
					Map<String, String> amendmentRequest = new HashMap<>();
					amendmentRequest.put("serviceCode", o2cServiceId);
					amendmentRequest.put("status", "AMENDED");
					amendmentRequest.put("amendedOrderCode", quoteToLe.getQuote().getQuoteCode());
					String request = Utils.convertObjectToJson(amendmentRequest);
					LOGGER.info("Request being send to O2C to update the amendment api with request {}", request);
					String amendResponse = (String) mqUtils.sendAndReceive(amendmentTaskApi, request);
					LOGGER.info("response  being received from O2C to update the amendment api with response {}",
							amendResponse);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in updating o2c Status", e);
		}
	}
	
	private void persistOrderSiteAddress(Integer erfLocationLocId, String siteType, String referenceId,
			String referenceName) {
		try {
			LOGGER.info("Saving the Order Site Address for order id {} : erfLocationId {}",referenceId,erfLocationLocId);
			if (erfLocationLocId != null) {
				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(erfLocationLocId));
				LOGGER.info("Location Response {}",locationResponse);
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						OrderSiteAddress orderSiteAddress = orderSiteAddressRepository
								.findByReferenceIdAndReferenceNameAndSiteType(referenceId, referenceName, siteType);
						if (orderSiteAddress == null) {
							orderSiteAddress = new OrderSiteAddress();
						}
						String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getPincode());

						orderSiteAddress.setAddressLineOne(addressDetail.getAddressLineOne());
						orderSiteAddress.setAddressLineTwo(addressDetail.getAddressLineTwo());
						orderSiteAddress.setFullAddress(addr);
						orderSiteAddress.setCity(addressDetail.getCity());
						orderSiteAddress.setCountry(addressDetail.getCountry());
						orderSiteAddress.setCreatedBy(Utils.getSource());
						orderSiteAddress.setCreatedTime(new Date());
						orderSiteAddress.setErfLocationLocId(erfLocationLocId);
						orderSiteAddress.setSiteType(siteType);
						orderSiteAddress.setLatLong(addressDetail.getLatLong());
						orderSiteAddress.setLocality(addressDetail.getLocality());
						orderSiteAddress.setPincode(addressDetail.getPincode());
						orderSiteAddress.setPlotBuilding(addressDetail.getPlotBuilding());
						orderSiteAddress.setReferenceId(referenceId);
						orderSiteAddress.setReferenceName(referenceName);
						orderSiteAddress.setSource(addressDetail.getSource());
						orderSiteAddress.setState(addressDetail.getState());
						orderSiteAddressRepository.save(orderSiteAddress);
						LOGGER.info("Order Site Address Save with id {}",orderSiteAddress.getId());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in saving the Order IllSite Address", e);
		}
	}

	/**
	 * constructOrderSiteSla
	 * 
	 * @param illSite
	 * @param orderSite
	 */
	private Set<OrderIllSiteSla> constructOrderSiteSla(QuoteIllSite illSite, OrderIllSite orderSite) {
		Set<OrderIllSiteSla> orderIllSiteSlas = new HashSet<>();

		if (illSite.getQuoteIllSiteSlas() != null && !illSite.getQuoteIllSiteSlas().isEmpty()) {
			illSite.getQuoteIllSiteSlas().forEach(illsiteSla -> {
				OrderIllSiteSla orderIllSiteSla = new OrderIllSiteSla();
				orderIllSiteSla.setOrderIllSite(orderSite);
				orderIllSiteSla.setSlaEndDate(illsiteSla.getSlaEndDate());
				orderIllSiteSla.setSlaStartDate(illsiteSla.getSlaStartDate());
				orderIllSiteSla.setSlaValue(illsiteSla.getSlaValue());
				orderIllSiteSla.setSlaMaster(illsiteSla.getSlaMaster());
				orderIllSiteSlaRepository.save(orderIllSiteSla);
				orderIllSiteSlas.add(orderIllSiteSla);

			});
		} else {
			LOGGER.info("illSla for gvpn is Null so fetching again");
			try {
				gvpnSlaService.constructIllSiteSla(illSite);
				LOGGER.info("Fetching slas");
				List<QuoteIllSiteSla> illSiteSlas = quoteIllSiteSlaRepository.findByQuoteIllSite(illSite);
				for (QuoteIllSiteSla illsiteSla : illSiteSlas) {
					OrderIllSiteSla orderIllSiteSla = new OrderIllSiteSla();
					orderIllSiteSla.setOrderIllSite(orderSite);
					orderIllSiteSla.setSlaEndDate(illsiteSla.getSlaEndDate());
					orderIllSiteSla.setSlaStartDate(illsiteSla.getSlaStartDate());
					orderIllSiteSla.setSlaValue(illsiteSla.getSlaValue());
					orderIllSiteSla.setSlaMaster(illsiteSla.getSlaMaster());
					orderIllSiteSlaRepository.save(orderIllSiteSla);
					orderIllSiteSlas.add(orderIllSiteSla);
				}
			} catch (Exception e) {
				LOGGER.error("Error in saving SLA", e);
			}

		}

		return orderIllSiteSlas;
	}

	/**
	 * constructOrderSiteFeasibility
	 * 
	 * @param illSite
	 * @param orderSite
	 */
	private Set<OrderSiteFeasibility> constructOrderSiteFeasibility(QuoteIllSite illSite, OrderIllSite orderSite) {
		Set<OrderSiteFeasibility> orderSiteFeasibilities = new HashSet<>();

		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite(illSite);
		if (siteFeasibilities != null) {
			siteFeasibilities.forEach(sitefeas -> {
				OrderSiteFeasibility orderSiteFeasibility = new OrderSiteFeasibility();
				orderSiteFeasibility.setFeasibilityCode(sitefeas.getFeasibilityCode());
				orderSiteFeasibility.setFeasibilityCheck(sitefeas.getFeasibilityCheck());
				orderSiteFeasibility.setFeasibilityMode(sitefeas.getFeasibilityMode());
				orderSiteFeasibility.setIsSelected(sitefeas.getIsSelected());
				orderSiteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
				orderSiteFeasibility.setOrderIllSite(orderSite);
				orderSiteFeasibility.setType(sitefeas.getType());
				orderSiteFeasibility.setProvider(sitefeas.getProvider());
				orderSiteFeasibility.setRank(sitefeas.getRank());
				orderSiteFeasibility.setResponseJson(sitefeas.getResponseJson());
				orderSiteFeasibility.setSfdcFeasibilityId(sitefeas.getSfdcFeasibilityId());
				orderSiteFeasibility.setFeasibilityType(sitefeas.getFeasibilityType());
				orderSiteFeasibilityRepository.save(orderSiteFeasibility);
				orderSiteFeasibilities.add(orderSiteFeasibility);

			});
		}

		return orderSiteFeasibilities;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<QuoteIllSiteBean> getSortedIllSiteDtos(List<QuoteIllSiteBean> illSiteBeans) {
		if (illSiteBeans != null) {
			illSiteBeans.sort(Comparator.comparingInt(QuoteIllSiteBean::getSiteId));

		}

		return illSiteBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent
	 * @param id,version
	 */
	private List<QuoteProductComponentBean> constructQuoteProductComponent(Integer id, boolean isSitePropertiesNeeded,
			Boolean isSiteProp) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenOnVersion(id, isSitePropertiesNeeded,
				isSiteProp);

		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				QuoteProductComponentBean quoteProductComponentBean = new QuoteProductComponentBean();
				quoteProductComponentBean.setComponentId(quoteProductComponent.getId());
				quoteProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					quoteProductComponentBean
							.setComponentMasterId(quoteProductComponent.getMstProductComponent().getId());
					quoteProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					quoteProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				quoteProductComponentBean.setType(quoteProductComponent.getType());
				quoteProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributeBasenOnVersion(quoteProductComponent.getId(),
								isSitePropertiesNeeded, isSiteProp)));
				quoteProductComponentBean.setAttributes(attributeValueBeans);
				quoteProductComponentDtos.add(quoteProductComponentBean);
			}

		}
		return quoteProductComponentDtos;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param orderSite
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent
	 * @param id,version
	 */
	private List<OrderProductComponent> constructOrderProductComponent(Integer id, OrderIllSite illSite) {
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(id, QuoteConstants.GVPN_SITES.toString());
		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				OrderProductComponent orderProductComponent = new OrderProductComponent();
				orderProductComponent.setReferenceId(illSite.getId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
				}
				orderProductComponent.setType(quoteProductComponent.getType());
				orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
				orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
				orderProductComponentRepository.save(orderProductComponent);
				constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
				List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());
				orderProductComponent.setOrderProductComponentsAttributeValues(
						constructOrderAttribute(attributes, orderProductComponent));
				orderProductComponents.add(orderProductComponent);
			}

		}
		// added for processs multi vrf order sites and componenets and attribute and price
		Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(id);
		if (quoteIllSite.isPresent()) {
			List<QuoteVrfSites> vrfSites = quoteVrfSitesRepository.findByQuoteIllSite(quoteIllSite.get());
			if (!vrfSites.isEmpty() && vrfSites.size() != 0) {
				LOGGER.info("quote vrf site size" + vrfSites.size());
				for (QuoteVrfSites vrfSite : vrfSites) {
					OrderVrfSites orderVrfSite = new OrderVrfSites();
					orderVrfSite.setArc(vrfSite.getArc());
					orderVrfSite.setCreatedTime(new Date());
					orderVrfSite.setUpdatedTime(new Date());
					orderVrfSite.setMrc(vrfSite.getMrc());
					orderVrfSite.setNrc(vrfSite.getNrc());
					orderVrfSite.setOrderIllSite(illSite);
					orderVrfSite.setSiteType(vrfSite.getSiteType());
					orderVrfSite.setTcv(vrfSite.getTcv());
					orderVrfSite.setVrfName(vrfSite.getVrfName());
					orderVrfSite.setVrfType(vrfSite.getVrfType());
					orderVrfSite.setTpsServiceId(vrfSite.getTpsServiceId());
					orderVrfSiteRepository.save(orderVrfSite);
					LOGGER.info("quote vrf site id" + vrfSite.getId());
					MstProductFamily prodFamily=mstProductFamilyRepository.findByNameAndStatus("GVPN",(byte) 1);
					LOGGER.info("prodFamily"+prodFamily.getName());
					List<QuoteProductComponent> vrfProductComponents = quoteProductComponentRepository
							.findByReferenceIdAndMstProductFamilyAndReferenceName(vrfSite.getId(),prodFamily, QuoteConstants.VRF_SITES.toString());
					if (vrfProductComponents != null) {
						LOGGER.info("inside if vrfProductComponents size"+vrfProductComponents.size());
						for (QuoteProductComponent quoteProductComponent : vrfProductComponents) {
							OrderProductComponent orderProductComponent = new OrderProductComponent();
							orderProductComponent.setReferenceId(orderVrfSite.getId());
							if (quoteProductComponent.getMstProductComponent() != null) {
								orderProductComponent
										.setMstProductComponent(quoteProductComponent.getMstProductComponent());
							}
							orderProductComponent.setType(quoteProductComponent.getType());
							orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
							orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
							orderProductComponentRepository.save(orderProductComponent);
							constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
							List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
									.findByQuoteProductComponent_Id(quoteProductComponent.getId());
							orderProductComponent.setOrderProductComponentsAttributeValues(
									constructOrderAttribute(attributes, orderProductComponent));
							orderProductComponents.add(orderProductComponent);
						}
					}
				}
			}
		}
		return orderProductComponents;

	}

	/**
	 * @author VIVEK KUMAR K constructComponentPriceDto used to get price of
	 *         componenet
	 * @link http://www.tatacommunications.com/
	 * @param QuoteProductComponent
	 */
	private QuotePriceBean constructComponentPriceDto(QuoteProductComponent quoteProductComponent) {
		QuotePriceBean priceDto = null;
		if (quoteProductComponent != null && quoteProductComponent.getMstProductComponent() != null) {
			List<QuotePrice> prices = quotePriceRepository.findByReferenceNameAndReferenceId(
					QuoteConstants.COMPONENTS.toString(), String.valueOf(quoteProductComponent.getId()));
			if (prices != null && !prices.isEmpty())
				priceDto = new QuotePriceBean(prices.get(0));
		}
		return priceDto;

	}

	/**
	 * @author VIVEK KUMAR K constructOrderComponentPrice used to constrcut order
	 *         Componenet price
	 * @param orderProductComponent
	 * @link http://www.tatacommunications.com/
	 * @param QuoteProductComponent
	 * 
	 */
	private OrderPrice constructOrderComponentPrice(QuoteProductComponent quoteProductComponent,
			OrderProductComponent orderProductComponent) {
		OrderPrice orderPrice = null;
		if (quoteProductComponent != null && quoteProductComponent.getMstProductComponent() != null) {
			QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(quoteProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (price != null) {
				orderPrice = new OrderPrice();
				orderPrice.setCatalogMrc(price.getCatalogMrc());
				orderPrice.setCatalogNrc(price.getCatalogNrc());
				orderPrice.setCatalogArc(price.getCatalogArc());
				orderPrice.setReferenceName(price.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderProductComponent.getId()));
				orderPrice.setComputedMrc(price.getComputedMrc());
				orderPrice.setComputedNrc(price.getComputedNrc());
				orderPrice.setComputedArc(price.getComputedArc());
				orderPrice.setDiscountInPercent(price.getDiscountInPercent());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPrice.setVersion(VersionConstants.ONE.getVersionNumber());
				orderPrice.setMinimumMrc(price.getMinimumMrc());
				orderPrice.setMinimumNrc(price.getMinimumNrc());
				orderPrice.setMinimumArc(price.getMinimumArc());
				orderPrice.setEffectiveMrc(price.getEffectiveMrc());
				orderPrice.setEffectiveNrc(price.getEffectiveNrc());
				orderPrice.setEffectiveArc(price.getEffectiveArc());
				orderPrice.setEffectiveUsagePrice(price.getEffectiveUsagePrice());
				orderPrice.setMstProductFamily(price.getMstProductFamily());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPriceRepository.save(orderPrice);

			}
		}
		return orderPrice;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @constructAttributePriceDto used to constrcut attribute price
	 */
	private QuotePriceBean constructAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue) {
		QuotePriceBean priceDto = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			if (attrPrice != null) {
				priceDto = new QuotePriceBean(attrPrice);
			}
		}
		return priceDto;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param orderAttributeValue
	 * @link http://www.tatacommunications.com/
	 * @constructAttributePriceDto used to get Attribute price
	 */
	private OrderPrice constructOrderAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue,
			OrderProductComponentsAttributeValue orderAttributeValue) {
		OrderPrice orderPrice = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			orderPrice = new OrderPrice();
			if (attrPrice != null) {
				orderPrice = new OrderPrice();
				orderPrice.setCatalogMrc(attrPrice.getCatalogMrc());
				orderPrice.setCatalogNrc(attrPrice.getCatalogNrc());
				orderPrice.setReferenceName(attrPrice.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderAttributeValue.getId()));
				orderPrice.setComputedMrc(attrPrice.getComputedMrc());
				orderPrice.setComputedNrc(attrPrice.getComputedNrc());
				orderPrice.setDiscountInPercent(attrPrice.getDiscountInPercent());
				orderPrice.setQuoteId(attrPrice.getQuoteId());
				orderPrice.setVersion(1);
				orderPrice.setEffectiveUsagePrice(attrPrice.getEffectiveUsagePrice());
				orderPrice.setMinimumMrc(attrPrice.getMinimumMrc());
				orderPrice.setMinimumNrc(attrPrice.getMinimumNrc());
				orderPrice.setEffectiveMrc(attrPrice.getEffectiveMrc());
				orderPrice.setEffectiveNrc(attrPrice.getEffectiveNrc());
				orderPrice.setEffectiveArc(attrPrice.getEffectiveArc());
			//	orderPriceRepository.save(orderPrice);
			}

		}
		return orderPrice;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructAttribute used to constrcut
	 *       attribute
	 * @param quoteProductComponentsAttributeValues
	 * @return
	 */
	private List<QuoteProductComponentsAttributeValueBean> constructAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues) {
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean = new ArrayList<>();
		if (quoteProductComponentsAttributeValues != null) {
			//LOGGER.info("constructAttribute attribute values {}  ", quoteProductComponentsAttributeValues);
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {
				//LOGGER.info("constructAttribute processing attribute name {} value {} and id {}  ", attributeValue.getProductAttributeMaster().getName(), attributeValue.getAttributeValues(), attributeValue.getId() );
				QuoteProductComponentsAttributeValueBean qtAttributeValue = null;
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y) && attributeValue.getAttributeValues()!=null) {
					Optional<AdditionalServiceParams> additionalServiceParamEntity = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeValue.getAttributeValues()));
					if (additionalServiceParamEntity.isPresent()) {
						qtAttributeValue = new QuoteProductComponentsAttributeValueBean(attributeValue,
								additionalServiceParamEntity.get().getValue());
					}
				} else {
					qtAttributeValue = new QuoteProductComponentsAttributeValueBean(attributeValue);
				}
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setAttributeMasterId(productAttributeMaster.getId());
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}
				qtAttributeValue.setAttributeId(attributeValue.getId());
				qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
				quoteProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return quoteProductComponentsAttributeValueBean;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructOrderAttribute used to
	 *       construct order attribute
	 * @param quoteProductComponentsAttributeValues
	 * @param orderProductComponent
	 * @param orderProductComponent
	 * @param orderSite
	 * @return
	 */
	private Set<OrderProductComponentsAttributeValue> constructOrderAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues,
			OrderProductComponent orderProductComponent) {
		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();
		if (quoteProductComponentsAttributeValues != null) {
			Set<OrderPrice> orderPrices=new HashSet<>();
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValue orderAttributeValue = new OrderProductComponentsAttributeValue();
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> additionalServiceParamEntity = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeValue.getAttributeValues()));
					if (additionalServiceParamEntity.isPresent()) {
						AdditionalServiceParams orderAdditionalServiceParams =new AdditionalServiceParams();
						orderAdditionalServiceParams.setAttribute(additionalServiceParamEntity.get().getAttribute());
						orderAdditionalServiceParams.setCategory("ORDERS");
						orderAdditionalServiceParams.setCreatedBy(additionalServiceParamEntity.get().getCreatedBy());
						orderAdditionalServiceParams.setCreatedTime(new Date());
						orderAdditionalServiceParams.setIsActive(CommonConstants.Y);
						orderAdditionalServiceParams.setReferenceId(additionalServiceParamEntity.get().getReferenceId());
						orderAdditionalServiceParams.setReferenceType(additionalServiceParamEntity.get().getReferenceType());
						orderAdditionalServiceParams.setValue(additionalServiceParamEntity.get().getValue());
						additionalServiceParamRepository.save(orderAdditionalServiceParams);
						orderAttributeValue.setAttributeValues(orderAdditionalServiceParams.getId()+"");
						orderAttributeValue.setIsAdditionalParam(CommonConstants.Y);
					}
				} else {
					orderAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
				}
				orderAttributeValue.setDisplayValue(attributeValue.getDisplayValue());
				orderAttributeValue.setProductAttributeMaster(attributeValue.getProductAttributeMaster());
				orderAttributeValue.setOrderProductComponent(orderProductComponent);
				orderProductComponentsAttributeValueRepository.save(orderAttributeValue);
				OrderPrice orderPrice = constructOrderAttributePriceDto(attributeValue, orderAttributeValue);
				if (orderPrice != null) {
					orderPrices.add(orderPrice);
				}
				orderProductComponentsAttributeValues.add(orderAttributeValue);
			}
			if(!orderPrices.isEmpty()) {
				orderPriceRepository.saveAll(orderPrices);
			}
		}

		return orderProductComponentsAttributeValues;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com getQuotesDetailsBasedOnCustomer used
	 *       to get Quote details based on customerId
	 * @return
	 */
	public List<QuoteBean> getQuotesDetailsBasedOnCustomer(Integer customerId) throws TclCommonException {
		List<QuoteBean> quoteBeans = null;
		try {
			quoteBeans = new ArrayList<>();
			Customer customer = customerRepository.findByIdAndStatus(customerId, (byte) 1);
			if (customer == null) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			for (Quote quote : customer.getQuotes()) {
				QuoteBean bean = constructQuote(quote, false, false,null);
				quoteBeans.add(bean);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public QuoteDetail approvedQuotes(UpdateRequest request, String ipAddress) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				order = constructOrder(quote, detail);
				if(checkO2cEnabled(order)) {
					order.setOrderToCashOrder(CommonConstants.BACTIVE);
					order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
					LOGGER.info("leaving approvedDocusignQuotes checkO2cEnabled o2corder {}, enabled {}", order.getOrderToCashOrder(), order.getIsOrderToCashEnabled());
					orderRepository.save(order);
				}
				/*
				 * if(checkO2cEnabledForOffnetWireless(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE); LOGGER.
				 * info("leaving approvedDocusignQuotes checkO2cEnabledForOffnetWireless o2corder {}, enabled {}"
				 * , order.getOrderToCashOrder(), order.getIsOrderToCashEnabled());
				 * orderRepository.save(order); }
				 */
				detail.setOrderId(order.getId());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
				// Trigger SFDC

				processQuoteToLeAndSfdc(quote, order, request);
			}
			quote.getQuoteToLes().forEach(quoteLe -> {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
					// Trigger ClosedBcr Process
					String custId = quoteLe.getQuote().getCustomer().getErfCusCustomerId().toString();
					/*
					 * String attribute=null; try { attribute = (String)
					 * mqUtils.sendAndReceive(customerSegment,
					 * custId,MDC.get(CommonConstants.MDC_TOKEN_KEY)); } catch (TclCommonException
					 * e) {
					 * LOGGER.error("Error in gvpn sending closed bcr {}",ExceptionUtils.getMessage(
					 * e));
					 * 
					 * }
					 */

					/*
					 * try { if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
					 * //need to add approverId instead of last null
					 * omsSfdcService.processeClosedBcr(quote.getQuoteCode(),
					 * quoteLe.getTpsSfdcOptyId(), quoteLe.getCurrencyCode(),
					 * "India",attribute,"PB_SS",true,null,null);
					 * LOGGER.info("Trigger Closed Bcr Request in GvpnQuoteService"); } else {
					 * LOGGER.
					 * info("Failed Closed bcr request in GvpnQuoteService customerAttribute/customerId is Empty"
					 * ); } } catch (TclCommonException e) {
					 * 
					 * LOGGER.warn("issue in GvpnQuoteService Trigger Closed Bcr Request");
					 * 
					 * }
					 */

				}

			});

			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileSite(quote);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}
	
	private Boolean checkO2cEnabled(Order order) {
		LOGGER.info("checkO2cEnabled for order code {}", order.getOrderCode());
		Boolean status = false;
		//Enabling O2C auto-trigger for Partner orders
		if(Objects.nonNull(order.getEngagementOptyId())) {
			return true;
		}
		for (OrderToLe orderToLe : order.getOrderToLes()) {
			
			/*
			 * if(orderToLe.getIsDemo() != null &&
			 * CommonConstants.BACTIVE.equals(orderToLe.getIsDemo())) { return false; }
			 */
			
			if(orderToLe.getOrderCategory() != null && MACDConstants.ADD_SECONDARY.equalsIgnoreCase(orderToLe.getOrderCategory())) {
				return false;
			}

			//Enabling O2C auto-trigger for Partner orders
			if(StringUtils.isNotBlank(orderToLe.getClassification()) && SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
				return true;
			}
			else if (StringUtils.isNotBlank(orderToLe.getClassification()) && SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification()))
			{
				return true;
			}
			else {

				for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
					for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {
						for (OrderIllSite orderIllSite : oProductSolution.getOrderIllSites()) {
							List<OrderSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
									.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
											"primary");
							for (OrderSiteFeasibility orderSiteFeasibility : orderSiteFeasibilities) {
								String feasibilityMode = orderSiteFeasibility.getFeasibilityMode();
								// Enabled O2C for all feasibility modes except INTL
								/*if (!(feasibilityMode.equals("OnnetWL") || feasibilityMode.equals("Onnet Wireline") || feasibilityMode.equals("OnnetRF") || feasibilityMode.equals("Onnet Wireless"))) {
									LOGGER.info("The feasibility Mode is {} for site {}", feasibilityMode,
											orderIllSite.getId());
									return false;
								}*/

								//Disable O2C auto-trigger for INTL orders
								if (feasibilityMode.equals("INTL")) {
									return false;
								} else {
									return true;
								}
							}
							List<OrderSiteFeasibility> secOrderSiteFeasibilities = orderSiteFeasibilityRepository
									.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
											"secondary");
							if (!secOrderSiteFeasibilities.isEmpty()) {
								LOGGER.info("The prisec have secondary for site {}", orderIllSite.getId());
								return false;
							} else {
								status = true;
							}
							
//							if(isNotBurstableBandwidth(orderIllSite)) {	
//								status = true;
//								LOGGER.info("Inside checkO2cEnabled isNotBurstableBandwidth loop, status returned {}", status);
//							} else {
//								status = false;
//								LOGGER.info("Inside checkO2cEnabled isNotBurstableBandwidth loop, status returned {}", status);
//							}
						
						}
					}
				}
			}
		}
		return status;
	}
	
	
	protected boolean isNotBurstableBandwidth(OrderIllSite orderIllSite) {
		// GVPN - Block Burstable Bandwidth orders in O2C
		boolean status = true;
		LOGGER.info("Inside isNotBurstableBandwidth method order site id {}", orderIllSite.getId());
		List<OrderProductComponent> vpnPortComponentList = orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(orderIllSite.getId(), ComponentConstants.VPN_PORT.getComponentsValue(), PDFConstants.PRIMARY);
		if(vpnPortComponentList != null && !vpnPortComponentList.isEmpty()) {
			for(OrderProductComponentsAttributeValue attribute : vpnPortComponentList.get(0).getOrderProductComponentsAttributeValues()) {
				LOGGER.info("primary attribute name {}, value {}", attribute.getProductAttributeMaster().getName(), attribute.getAttributeValues());
				if(PDFConstants.BUSTABLE_BW.equalsIgnoreCase(attribute.getProductAttributeMaster().getName()) && StringUtils.isNotBlank(attribute.getAttributeValues())) {
					status = false;
					break;
				}
				
			}
			
		}
		
		List<OrderProductComponent> vpnPortComponentListSecondary = orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(orderIllSite.getId(), ComponentConstants.VPN_PORT.getComponentsValue(), PDFConstants.SECONDARY);
		if(vpnPortComponentListSecondary != null && !vpnPortComponentListSecondary.isEmpty()) {
			for(OrderProductComponentsAttributeValue attribute : vpnPortComponentListSecondary.get(0).getOrderProductComponentsAttributeValues()) {
				LOGGER.info("secondary attribute name {}, value {}", attribute.getProductAttributeMaster().getName(), attribute.getAttributeValues());
				if(PDFConstants.BUSTABLE_BW.equalsIgnoreCase(attribute.getProductAttributeMaster().getName()) && StringUtils.isNotBlank(attribute.getAttributeValues())) {
					status =  false;
					break;
				}
				
			}
			
		}
		LOGGER.info("isNotBurstableBandwidth status returned {}", status);
		return status;
	}
	
	private Boolean checkO2cEnabledForOffnetWireless(Order order) {
		Boolean status = false;
		//Disable O2C auto-trigger for Partner orders
		if(Objects.nonNull(order.getEngagementOptyId())) {
			return status;
		}
		for (OrderToLe orderToLe : order.getOrderToLes()) {
			
			/*
			 * if(orderToLe.getIsDemo() != null &&
			 * CommonConstants.BACTIVE.equals(orderToLe.getIsDemo())) { return false; }
			 */
			
			if(orderToLe.getOrderCategory() != null && MACDConstants.ADD_SECONDARY.equalsIgnoreCase(orderToLe.getOrderCategory())) {
				return false;
			}


			//Disable O2C auto-trigger for Partner orders
			if(StringUtils.isNotBlank(orderToLe.getClassification()) && SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {

				LOGGER.info("Classification type: {}",orderToLe.getClassification());
				return false;
			}
			else if (StringUtils.isNotBlank(orderToLe.getClassification()) && SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification()))
			{
				LOGGER.info("Classification type: {}",orderToLe.getClassification());
				return false;
			}
			else {
				for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
					for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {
						for (OrderIllSite orderIllSite : oProductSolution.getOrderIllSites()) {
							List<OrderSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
									.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
											"primary");
							for (OrderSiteFeasibility orderSiteFeasibility : orderSiteFeasibilities) {
								String feasibilityMode = orderSiteFeasibility.getFeasibilityMode();
								if (!(feasibilityMode.equals("OffnetRF") || feasibilityMode.equals("Offnet Wireless"))) {
									LOGGER.info("The feasibility Mode is {} for site {}", feasibilityMode,
											orderIllSite.getId());
									return false;
								}
								//Disable O2C auto-trigger for INTL orders
								else if (feasibilityMode.equals("INTL")) {
									return false;
								} else {
									status = true;
								}
							}
							List<OrderSiteFeasibility> secOrderSiteFeasibilities = orderSiteFeasibilityRepository
									.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
											"secondary");
							if (!secOrderSiteFeasibilities.isEmpty()) {
								LOGGER.info("The prisec have secondary for site {}", orderIllSite.getId());
								return false;
							} else {
								status = true;
							}
							
							if(isNotBurstableBandwidth(orderIllSite)) {
								status = true;
								LOGGER.info("Inside checkO2cEnabledForOffnetWireless isNotBurstableBandwidth loop, status returned {}", status);
							}  else {
								status = false;
								LOGGER.info("Inside checkO2cEnabled isNotBurstableBandwidth loop, status returned {}", status);
							}
						}
					}
				}
			}
		}
		return status;
	}


	/**
	 * processOrderFlatTable
	 * 
	 * @param order
	 * @throws TclCommonException
	 */
	/*
	 * public void processOrderFlatTable(Integer orderId) throws TclCommonException
	 * { Map<String, Object> requestparam = new HashMap<>();
	 * requestparam.put("orderId", orderId); requestparam.put("productName",
	 * "GVPN"); requestparam.put("userName", Utils.getSource());
	 * mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam),
	 * MDC.get(CommonConstants.MDC_TOKEN_KEY)); }
	 */

	private void processEngagement(QuoteToLe quote, QuoteToLeProductFamily quoteToLeProductFamily) {
		List<Engagement> engagements = engagementRepository.findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(
				quote.getQuote().getCustomer(), quote.getErfCusCustomerLegalEntityId(),
				quoteToLeProductFamily.getMstProductFamily(), CommonConstants.BACTIVE);
		if (engagements == null || engagements.isEmpty()) {
			Engagement engagement = new Engagement();
			engagement.setCustomer(quote.getQuote().getCustomer());
			engagement.setEngagementName(quoteToLeProductFamily.getMstProductFamily().getName() + CommonConstants.HYPHEN
					+ quote.getErfCusCustomerLegalEntityId());
			engagement.setErfCusCustomerLeId(quote.getErfCusCustomerLegalEntityId());
			engagement.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
			engagement.setStatus(CommonConstants.BACTIVE);
			engagement.setCreatedTime(new Date());
			engagementRepository.save(engagement);
		}
	}

	/**
	 * processQuoteToLeAndSfdc
	 * 
	 * @param quote
	 * @param order
	 * @param request
	 */
	protected void processQuoteToLeAndSfdc(Quote quote, Order order, UpdateRequest request) throws TclCommonException {
		quote.getQuoteToLes().forEach(quoteLe -> {

			try {

				if (Objects.nonNull(quote.getQuoteCode())
						&& !quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {

					if (quoteLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
						omsSfdcService.processSiteDetailsRenewals(quoteLe); // renewals-sfdc
					} else {
						omsSfdcService.processSiteDetails(quoteLe);
					}

					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

					Boolean nat = (request.getCheckList() == null
							|| request.getCheckList().equalsIgnoreCase(CommonConstants.NO)) ? Boolean.FALSE
									: Boolean.TRUE;
					Map<String, String> cofObjectMapper = new HashMap<>();
					gvpnQuotePdfService.processCofPdf(quote.getId(), null, nat, true, quoteLe.getId(), cofObjectMapper);
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
					if (cofDetails != null) {
						cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
					}
					User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
					String userEmail = null;
					if (userRepo != null) {
						userEmail = userRepo.getEmailId();
					}

					for (OrderToLe orderToLe : order.getOrderToLes()) {
						gvpnQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
								orderToLe.getId(), cofObjectMapper);
						break;
					}
					// Trigger orderMail
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
				}

				List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
				quoteDelegate.forEach(quoteDelegation -> {
					quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
					quoteDelegationRepository.save(quoteDelegation);
				});
				uploadSSIfNotPresent(quoteLe);
				/**
				 * commented due to requirement change for MSA mapping while optimus journey
				 */
				// uploadMSAIfNotPresent(quoteLe);
			} catch (Exception e) {
				LOGGER.warn("Error in gvpn {}", ExceptionUtils.getStackTrace(e));
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

			}

		});

	}

	public void uploadSSIfNotPresent(QuoteToLe quoteToLe) throws TclCommonException {
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.SERVICE_SCHEDULE, CommonConstants.BACTIVE);
		for (MstOmsAttribute mstOmsAttribute : mstOmsAttributes) {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
				ServiceScheduleBean serviceScheduleBean = constructServiceScheduleBean(
						quoteToLe.getErfCusCustomerLegalEntityId());
				LOGGER.info("MDC Filter token value in before Queue call uploadSSIfNotPresent {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.sendAndReceive(updateSSQueue, Utils.convertObjectToJson(serviceScheduleBean));
				break;
			}
		}
	}

	public ServiceScheduleBean constructServiceScheduleBean(Integer customerLeId) {
		ServiceScheduleBean serviceScheduleBean = new ServiceScheduleBean();
		serviceScheduleBean.setCustomerLeId(customerLeId);
		serviceScheduleBean.setDisplayName("Service Schedule");
		serviceScheduleBean.setIsSSUploaded(true);
		serviceScheduleBean.setName("Service Schedule");
		serviceScheduleBean.setProductName("GVPN");
		return serviceScheduleBean;
	}

	protected void processOrderMailNotification(Order order, QuoteToLe quoteToLe, Map<String, String> cofObjectMapper,
			String userEmail) throws TclCommonException {
		String emailId = userEmail != null ? userEmail : customerSupportEmail;
		String leMail = getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),
				emailId, appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName, CommonConstants.GVPN,
				quoteToLe);
		notificationService.newOrderSubmittedNotification(mailNotificationBean);
	}

	private MailNotificationBean populateMailNotifionSalesOrder(String accountManagerEmail, String orderRefId,
			String customerEmail, String provisioningLink, Map<String, String> cofObjectMapper, String fileName,
			String productName, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(provisioningLink);
		mailNotificationBean.setCofObjectMapper(cofObjectMapper);
		mailNotificationBean.setFileName(fileName);
		mailNotificationBean.setProductName(productName);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * cloneQuoteForNonFeasibileSite
	 */
	protected void cloneQuoteForNonFeasibileSite(Quote quote) {
		Quote nonFeasibleQuote = cloneQuote(quote);
		String productName = null;
		Set<QuoteToLe> quoteToLes = quote.getQuoteToLes();
		for (QuoteToLe quoteToLe : quoteToLes) {
			QuoteToLe nonFeasibleQuoteToLe = cloneQuoteToLe(nonFeasibleQuote, quoteToLe);
			cloneQuoteLeAttributes(quoteToLe, nonFeasibleQuoteToLe);
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.getId());
			for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilies) {
				productName = quoteToLeProductFamily.getMstProductFamily().getName();
				QuoteToLeProductFamily nonFeasibleProdFamily = cloneQuoteLeToProductFamily(nonFeasibleQuoteToLe,
						quoteToLeProductFamily);
				extractProductSolutions(quoteToLeProductFamily, nonFeasibleProdFamily);
			}
		}
		nonFeasibleQuote.setQuoteCode(Utils.generateRefId(productName));
		quoteRepository.save(nonFeasibleQuote);
	}

	/**
	 * cloneQuoteLeAttributes
	 * 
	 * @param quoteToLe
	 */
	private void cloneQuoteLeAttributes(QuoteToLe quoteToLe, QuoteToLe nonFeasibleQuoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
		for (QuoteLeAttributeValue quoteLeAttributeValue : quoteLeAttributeValues) {
			QuoteLeAttributeValue nonFeasibleQuoteLeAttributeValue = new QuoteLeAttributeValue();
			nonFeasibleQuoteLeAttributeValue.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
			nonFeasibleQuoteLeAttributeValue.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
			nonFeasibleQuoteLeAttributeValue.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute());
			nonFeasibleQuoteLeAttributeValue.setQuoteToLe(nonFeasibleQuoteToLe);
			quoteLeAttributeValueRepository.save(nonFeasibleQuoteLeAttributeValue);
		}
	}

	/**
	 * extractProductSolutions
	 * 
	 * @param quoteToLeProductFamily
	 * @param nonFeasibleProdFamily
	 */
	private void extractProductSolutions(QuoteToLeProductFamily quoteToLeProductFamily,
			QuoteToLeProductFamily nonFeasibleProdFamily) {
		List<ProductSolution> prodSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		for (ProductSolution productSolution : prodSolutions) {
			ProductSolution nonFeasibleProductSolution = cloneProductSolution(nonFeasibleProdFamily, productSolution);
			List<QuoteIllSite> sites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
					CommonConstants.BACTIVE);
			for (QuoteIllSite quoteIllSite : sites) {
				if (quoteIllSite.getFeasibility().equals(new Byte("0"))) {
					QuoteIllSite nonQuoteIllSite = cloneIllSite(nonFeasibleProductSolution, quoteIllSite);
					extractNonFeasibleComponents(quoteIllSite, nonQuoteIllSite);
					cloneSlaDetails(quoteIllSite, nonQuoteIllSite);
					cloneFeasilibility(quoteIllSite, nonQuoteIllSite);
					clonePricingDetails(quoteIllSite, nonQuoteIllSite);
				}
			}
		}
	}

	/**
	 * clonePricingDetails
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void clonePricingDetails(QuoteIllSite quoteIllSite, QuoteIllSite nonQuoteIllSite) {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(quoteIllSite.getSiteCode(), "Discount");
		for (PricingEngineResponse pricingDetail : pricingDetails) {
			PricingEngineResponse nonFeasiblepricingDetail = new PricingEngineResponse();
			nonFeasiblepricingDetail.setDateTime(pricingDetail.getDateTime());
			nonFeasiblepricingDetail.setPriceMode(pricingDetail.getPriceMode());
			nonFeasiblepricingDetail.setPricingType(pricingDetail.getPricingType());
			nonFeasiblepricingDetail.setRequestData(pricingDetail.getRequestData());
			nonFeasiblepricingDetail.setResponseData(pricingDetail.getResponseData());
			nonFeasiblepricingDetail.setSiteCode(nonQuoteIllSite.getSiteCode());
			pricingDetailsRepository.save(nonFeasiblepricingDetail);
		}
	}

	/**
	 * cloneFeasilibility
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void cloneFeasilibility(QuoteIllSite quoteIllSite, QuoteIllSite nonQuoteIllSite) {
		List<SiteFeasibility> siteFeasiblities = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite);
		for (SiteFeasibility siteFeasibility : siteFeasiblities) {
			SiteFeasibility nonFeasibleSiteFeasibility = new SiteFeasibility();
			nonFeasibleSiteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
			nonFeasibleSiteFeasibility.setFeasibilityCheck(siteFeasibility.getFeasibilityCheck());
			nonFeasibleSiteFeasibility.setFeasibilityCode(siteFeasibility.getFeasibilityCode());
			nonFeasibleSiteFeasibility.setFeasibilityMode(siteFeasibility.getFeasibilityMode());
			nonFeasibleSiteFeasibility.setIsSelected(siteFeasibility.getIsSelected());
			nonFeasibleSiteFeasibility.setProvider(siteFeasibility.getProvider());
			nonFeasibleSiteFeasibility.setQuoteIllSite(nonQuoteIllSite);
			nonFeasibleSiteFeasibility.setRank(siteFeasibility.getRank());
			nonFeasibleSiteFeasibility.setResponseJson(siteFeasibility.getResponseJson());
			nonFeasibleSiteFeasibility.setType(siteFeasibility.getType());
			siteFeasibilityRepository.save(nonFeasibleSiteFeasibility);
		}
	}

	/**
	 * cloneSlaDetails
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void cloneSlaDetails(QuoteIllSite quoteIllSite, QuoteIllSite nonQuoteIllSite) {
		List<QuoteIllSiteSla> slaDetails = quoteIllSiteSlaRepository.findByQuoteIllSite(quoteIllSite);
		for (QuoteIllSiteSla quoteIllSiteSla : slaDetails) {
			QuoteIllSiteSla nonFeasibileQuoteIllSiteSla = new QuoteIllSiteSla();
			nonFeasibileQuoteIllSiteSla.setQuoteIllSite(nonQuoteIllSite);
			nonFeasibileQuoteIllSiteSla.setSlaEndDate(quoteIllSiteSla.getSlaEndDate());
			nonFeasibileQuoteIllSiteSla.setSlaMaster(quoteIllSiteSla.getSlaMaster());
			nonFeasibileQuoteIllSiteSla.setSlaStartDate(quoteIllSiteSla.getSlaStartDate());
			nonFeasibileQuoteIllSiteSla.setSlaValue(quoteIllSiteSla.getSlaValue());
			quoteIllSiteSlaRepository.save(nonFeasibileQuoteIllSiteSla);
		}
	}

	/**
	 * extractNonFeasibleComponents
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void extractNonFeasibleComponents(QuoteIllSite quoteIllSite, QuoteIllSite nonQuoteIllSite) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quoteIllSite.getId(), QuoteConstants.GVPN_SITES.toString());
		for (QuoteProductComponent quoteProductComponent : productComponents) {
			QuoteProductComponent nonFeasibleProductComponent = cloneProductComponent(nonQuoteIllSite,
					quoteProductComponent);
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());
			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
				cloneComponentAttributes(nonFeasibleProductComponent, quoteProductComponentsAttributeValue);
			}
		}
	}

	/**
	 * cloneComponentAttributes
	 * 
	 * @param nonFeasibleProductComponent
	 * @param quoteProductComponentsAttributeValue
	 */
	private void cloneComponentAttributes(QuoteProductComponent nonFeasibleProductComponent,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		QuoteProductComponentsAttributeValue nonFeasiblequoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		nonFeasiblequoteProductComponentsAttributeValue
				.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
		nonFeasiblequoteProductComponentsAttributeValue
				.setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
		nonFeasiblequoteProductComponentsAttributeValue
				.setProductAttributeMaster(quoteProductComponentsAttributeValue.getProductAttributeMaster());
		nonFeasiblequoteProductComponentsAttributeValue.setQuoteProductComponent(nonFeasibleProductComponent);
		quoteProductComponentsAttributeValueRepository.save(nonFeasiblequoteProductComponentsAttributeValue);
	}

	/**
	 * cloneProductComponent
	 * 
	 * @param nonQuoteIllSite
	 * @param quoteProductComponent
	 * @return
	 */
	private QuoteProductComponent cloneProductComponent(QuoteIllSite nonQuoteIllSite,
			QuoteProductComponent quoteProductComponent) {
		QuoteProductComponent nonFeasibleProductComponent = new QuoteProductComponent();
		nonFeasibleProductComponent.setReferenceId(nonQuoteIllSite.getId());
		if (quoteProductComponent.getMstProductComponent() != null) {
			nonFeasibleProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
		}
		nonFeasibleProductComponent.setType(quoteProductComponent.getType());
		nonFeasibleProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
		nonFeasibleProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
		quoteProductComponentRepository.save(nonFeasibleProductComponent);
		return nonFeasibleProductComponent;
	}

	/**
	 * cloneIllSite
	 * 
	 * @param nonFeasibleProductSolution
	 * @param quoteIllSite
	 */
	private QuoteIllSite cloneIllSite(ProductSolution nonFeasibleProductSolution, QuoteIllSite quoteIllSite) {
		QuoteIllSite nonQuoteIllSite = new QuoteIllSite();
		nonQuoteIllSite.setCreatedBy(quoteIllSite.getCreatedBy());
		nonQuoteIllSite.setCreatedTime(new Date());
		nonQuoteIllSite.setErfLocSiteaLocationId(quoteIllSite.getErfLocSiteaLocationId());
		nonQuoteIllSite.setErfLocSiteaSiteCode(quoteIllSite.getErfLocSiteaSiteCode());
		nonQuoteIllSite.setErfLocSitebLocationId(quoteIllSite.getErfLocSitebLocationId());
		nonQuoteIllSite.setErfLocSitebSiteCode(quoteIllSite.getErfLocSitebSiteCode());
		nonQuoteIllSite.setErfLrSolutionId(quoteIllSite.getErfLrSolutionId());
		nonQuoteIllSite.setFeasibility(quoteIllSite.getFeasibility());
		nonQuoteIllSite.setFpStatus(quoteIllSite.getFpStatus());
		nonQuoteIllSite.setImageUrl(quoteIllSite.getImageUrl());
		nonQuoteIllSite.setProductSolution(nonFeasibleProductSolution);
		nonQuoteIllSite.setStatus(quoteIllSite.getStatus());
		nonQuoteIllSite.setSiteCode(Utils.generateUid());
		illSiteRepository.save(nonQuoteIllSite);
		return nonQuoteIllSite;
	}

	/**
	 * cloneProductSolution
	 * 
	 * @param nonFeasibleProdFamily
	 * @param productSolution
	 */
	private ProductSolution cloneProductSolution(QuoteToLeProductFamily nonFeasibleProdFamily,
			ProductSolution productSolution) {
		ProductSolution nonFeasibleProductSolution = new ProductSolution();
		nonFeasibleProductSolution.setMstProductOffering(productSolution.getMstProductOffering());
		nonFeasibleProductSolution.setProductProfileData(productSolution.getProductProfileData());
		nonFeasibleProductSolution.setQuoteToLeProductFamily(nonFeasibleProdFamily);
		nonFeasibleProductSolution.setSolutionCode(Utils.generateUid());
		productSolutionRepository.save(nonFeasibleProductSolution);
		return nonFeasibleProductSolution;
	}

	/**
	 * cloneQuoteLeToProductFamily
	 * 
	 * @param nonFeasibleQuoteToLe
	 * @param quoteToLeProductFamily
	 */
	private QuoteToLeProductFamily cloneQuoteLeToProductFamily(QuoteToLe nonFeasibleQuoteToLe,
			QuoteToLeProductFamily quoteToLeProductFamily) {
		QuoteToLeProductFamily nonFeasibleProdFamily = new QuoteToLeProductFamily();
		nonFeasibleProdFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
		nonFeasibleProdFamily.setQuoteToLe(nonFeasibleQuoteToLe);
		quoteToLeProductFamilyRepository.save(nonFeasibleProdFamily);
		return nonFeasibleProdFamily;
	}

	/**
	 * cloneQuoteToLe
	 * 
	 * @param nonFeasibleQuote
	 * @param quoteToLe
	 */
	private QuoteToLe cloneQuoteToLe(Quote nonFeasibleQuote, QuoteToLe quoteToLe) {
		QuoteToLe nonFeasibleQuoteToLe = new QuoteToLe();
		nonFeasibleQuoteToLe.setCurrencyId(quoteToLe.getCurrencyId());
		nonFeasibleQuoteToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
		nonFeasibleQuoteToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
		nonFeasibleQuoteToLe.setStage(QuoteStageConstants.GET_QUOTE.toString());
		nonFeasibleQuoteToLe.setIsAmended(quoteToLe.getIsAmended());
		nonFeasibleQuoteToLe.setQuote(nonFeasibleQuote);
		quoteToLeRepository.save(nonFeasibleQuoteToLe);
		return nonFeasibleQuoteToLe;
	}

	/**
	 * cloneQuote
	 * 
	 * @param quote
	 * @param nonFeasibleQuote
	 */
	private Quote cloneQuote(Quote quote) {
		Quote nonFeasibleQuote = new Quote();
		nonFeasibleQuote.setCreatedBy(quote.getCreatedBy());
		nonFeasibleQuote.setCreatedTime(new Date());
		nonFeasibleQuote.setCustomer(quote.getCustomer());
		nonFeasibleQuote.setEffectiveDate(quote.getEffectiveDate());
		nonFeasibleQuote.setStatus((byte) 1);
		quoteRepository.save(nonFeasibleQuote);
		return nonFeasibleQuote;
	}

	/**
	 * getAccountManagersEmail
	 * 
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	public String getAccountManagersEmail(QuoteToLe quoteToLe) {
		String leMail = getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
		String pilotMail = pilotTeamMail != null ? pilotTeamMail[0] : null;
		return (leMail == null || leMail.equals(CommonConstants.HYPHEN)) ? pilotMail : leMail;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @param legalEntityId
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getDashboardDetails used to get
	 *            dashboard details for legal entity
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	public DashBoardBean getDashboardDetails(Integer legalEntityId) throws TclCommonException {
		DashBoardBean dashBoard = null;
		try {
			dashBoard = new DashBoardBean();

			List<DashboardCustomerbean> dashboardCustomerbeans = new ArrayList<>();

			if (legalEntityId != null) {
				getDashBoardDetailsForLegalEntity(dashboardCustomerbeans, legalEntityId);
			} else {
				getDashBoardDetailsForAllCustomer(dashboardCustomerbeans, dashBoard);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return dashBoard;
	}

	/**
	 * getDashBoardDetailsForLegalEntity used to get dashboarddetails for entity
	 * 
	 * @param dashboardCustomerbeans
	 */
	private void getDashBoardDetailsForLegalEntity(List<DashboardCustomerbean> dashboardCustomerbeans,
			Integer legalEntityId) {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByErfCusCustomerLegalEntityId(legalEntityId);
		if (quoteToLes != null) {
			int count = 0;
			DashboardCustomerbean customerbean = new DashboardCustomerbean();
			count = constructDashBoardBeans(quoteToLes, count, customerbean);
			customerbean.setActiveQuotesCount(count);
			count = 0;
			dashboardCustomerbeans.add(customerbean);
		}

	}

	/**
	 * getDashBoardDetailsForAllCustomer used to get details for all customer
	 * 
	 * @param dashboardCustomerbeans
	 * @param dashBoard
	 */
	private void getDashBoardDetailsForAllCustomer(List<DashboardCustomerbean> dashboardCustomerbeans,
			DashBoardBean dashBoard) throws TclCommonException {
		Map<Integer, List<CustomerDetail>> customerMap = new HashMap<>();
		List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
		if (customerDetails == null) {
			throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

		}
		groupBasedOnCustomer(customerMap, customerDetails);
		for (Entry<Integer, List<CustomerDetail>> cusId : customerMap.entrySet()) {
			DashboardCustomerbean customerbean = new DashboardCustomerbean();
			customerbean.setCustomerId(cusId.getKey());
			List<CustomerDetail> custDetails = customerMap.get(cusId.getKey());
			for (CustomerDetail custDetail : custDetails) {
				if (StringUtils.isBlank(customerbean.getCustomerName())) {
					customerbean.setCustomerName(custDetail.getCustomerName());
				}
				List<QuoteToLe> quoteToLes = quoteToLeRepository
						.findByErfCusCustomerLegalEntityId(custDetail.getCustomerLeId());
				if (quoteToLes != null) {
					int count = 0;
					count = constructDashBoardBeans(quoteToLes, count, customerbean);
					customerbean.setActiveQuotesCount(count);
					count = 0;
					dashboardCustomerbeans.add(customerbean);
				}
			}
		}
		dashBoard.setDashboardCustomerbeans(dashboardCustomerbeans);
	}

	/**
	 * constructDashBoardBeans used to construct dashboard beans
	 * 
	 * @param quoteToLes
	 * @param count
	 * @param customerbean
	 */
	private int constructDashBoardBeans(List<QuoteToLe> quoteToLes, int count, DashboardCustomerbean customerbean) {
		for (QuoteToLe quoteToLe : quoteToLes) {
			count++;
			DashboardLegalEntityBean daEntityBean = new DashboardLegalEntityBean();
			DashboardQuoteBean quoteBean = new DashboardQuoteBean();
			daEntityBean.setLegEntityId(quoteToLe.getId());
			quoteBean.setQuoteId(quoteToLe.getQuote().getId());
			quoteBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			quoteBean.setCreatedDate(quoteToLe.getQuote().getCreatedTime());
			quoteBean.setQuoteStage(quoteToLe.getStage());
			daEntityBean.setQuoteBean(quoteBean);
			customerbean.getLegalEntityBeans().add(daEntityBean);
			for (QuoteToLeProductFamily family : quoteToLe.getQuoteToLeProductFamilies()) {
				DashBoardFamilyBean boardFamilyBean = new DashBoardFamilyBean();
				boardFamilyBean.setFamilyName(family.getMstProductFamily().getName());
				daEntityBean.getFamilyBeans().add(boardFamilyBean);
				for (ProductSolution solution : family.getProductSolutions()) {
					if (solution.getQuoteIllSites() != null) {
						Long siteCount = solution.getQuoteIllSites().stream().count();
						boardFamilyBean.setTotalCount(siteCount);
					}

				}
			}

		}

		return count;
	}

	/**
	 * groupBasedOnCustomer used to group based on customer
	 * 
	 * @param customerMap
	 * @param customerDetails
	 */
	private void groupBasedOnCustomer(Map<Integer, List<CustomerDetail>> customerMap,
			List<CustomerDetail> customerDetails) {
		for (CustomerDetail customerDetail : customerDetails) {
			if (customerMap.get(customerDetail.getCustomerId()) == null) {
				List<CustomerDetail> custDetails = new ArrayList<>();
				custDetails.add(customerDetail);
				customerMap.put(customerDetail.getCustomerId(), custDetails);

			} else {
				customerMap.get(customerDetail.getCustomerId()).add(customerDetail);
			}
		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 *       getQuotesDetailsBasedOnCustomerLegalEntity used to get quoteDetails
	 *       based on customerLegal entityId
	 * @param customerLegalEntityId
	 * @return
	 */
	public List<QuoteBean> getQuotesDetailsBasedOnCustomerLegalEntity(Integer customerLegalEntityId)
			throws TclCommonException {
		List<QuoteBean> quoteBeans = null;
		try {
			quoteBeans = new ArrayList<>();

			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByErfCusCustomerLegalEntityId(customerLegalEntityId);
			if (quoteToLes.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			for (QuoteToLe quoteToLe : quoteToLes) {

				QuoteBean quotBean = constructQuote(quoteToLe.getQuote(), false, false, null);
				quoteBeans.add(quotBean);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ editSiteComponent used to edit site
	 *       component values
	 * @param request
	 * @return
	 */
	@Transactional
	public QuoteDetail editSiteComponent(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			quoteDetail = new QuoteDetail();
			validateRequest(request);
			for (ComponentDetail cmpDetail : request.getComponentDetails()) {

				for (AttributeDetail attributeDetail : cmpDetail.getAttributes()) {
					QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = null;
					if (attributeDetail.getAttributeId() == null) {
						quoteProductComponentsAttributeValue = updateProductAttribute(request, cmpDetail,
								attributeDetail, quoteProductComponentsAttributeValue);
						updateCpeProvided(attributeDetail);

					}

					/*  PT-1132  CPE component should be available when Proactive

					else if (attributeDetail.getName().equalsIgnoreCase("CPE")
							&& attributeDetail.getValue().equalsIgnoreCase("Customer provided")) {

						removeCPEComponenet(request.getSiteId());
						updateCpeProvided(attributeDetail);

					}*/

					else {
						updateCpeProvided(attributeDetail);

					}

				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * updateCpeProvided
	 * 
	 * @param attributeDetail
	 */
	protected void updateCpeProvided(AttributeDetail attributeDetail) {
		if (attributeDetail.getAttributeId() != null) {
			Optional<QuoteProductComponentsAttributeValue> optionalquoteProductComponentsAttributeValue = quoteProductComponentsAttributeValueRepository
					.findById(attributeDetail.getAttributeId());

			if (optionalquoteProductComponentsAttributeValue.isPresent()) {
				QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = optionalquoteProductComponentsAttributeValue
						.get();
				quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
			}
		}

	}

	/**
	 * removeCPEComponenet
	 * 
	 * @param name
	 * @param integer
	 */
	protected void removeCPEComponenet(Integer siteID) {
		List<QuoteProductComponent> prodComponent = null;
		Optional<QuoteIllSite> siteEntity = illSiteRepository.findById(siteID);
		if (siteEntity.isPresent()) {
			prodComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteEntity.get().getId(), "CPE",QuoteConstants.GVPN_SITES.toString());
		}
		if (prodComponent != null && !prodComponent.isEmpty()) {

			for (QuoteProductComponent quoteProductComponents : prodComponent) {

				for (QuoteProductComponentsAttributeValue quoteProductComponent : quoteProductComponents
						.getQuoteProductComponentsAttributeValues()) {
					quoteProductComponentsAttributeValueRepository.delete(quoteProductComponent);

				}
				quoteProductComponentRepository.delete(quoteProductComponents);

			}
		}
	}

	/**
	 * updateProductAttribute
	 * 
	 * @param request
	 * @param cmpDetail
	 * @param attributeDetail
	 * @param quoteProductComponentsAttributeValue
	 * @return
	 */
	protected QuoteProductComponentsAttributeValue updateProductAttribute(UpdateRequest request,
			ComponentDetail cmpDetail, AttributeDetail attributeDetail,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		QuoteProductComponent quoteProductComponent = null;
		Optional<QuoteIllSite> siteEntity = illSiteRepository.findById(request.getSiteId());
		if (siteEntity.isPresent()) {
			List<ProductAttributeMaster> mstAttributeMaster = productAttributeMasterRepository
					.findByNameAndStatus(attributeDetail.getName(), CommonConstants.BACTIVE);
			ProductAttributeMaster productAttributeMaster = null;
			if (mstAttributeMaster.isEmpty()) {
				productAttributeMaster = new ProductAttributeMaster();
				productAttributeMaster.setName(attributeDetail.getName());
				productAttributeMaster.setStatus(CommonConstants.BACTIVE);
				productAttributeMaster.setDescription(attributeDetail.getName());
				productAttributeMaster.setCreatedBy(Utils.getSource());
				productAttributeMaster.setCreatedTime(new Date());
				productAttributeMasterRepository.save(productAttributeMaster);
			} else {
				productAttributeMaster = mstAttributeMaster.get(0);
			}
			List<QuoteProductComponent> prodComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(siteEntity.get().getId(), cmpDetail.getName(),
							cmpDetail.getType());

			if (prodComponent.isEmpty()) {
				MstProductComponent productComponent = mstProductComponentRepository.findByName(cmpDetail.getName());
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(request.getFamilyName(), (byte) 1);

				quoteProductComponent = constructProductComponent(productComponent, mstProductFamily,
						request.getSiteId());
				quoteProductComponent.setType(cmpDetail.getType());
				quoteProductComponentRepository.save(quoteProductComponent);

			} else {
				quoteProductComponent = prodComponent.get(0);
			}
			if (quoteProductComponent != null) {
				quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
				quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
				quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
				quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
			}
		}
		return quoteProductComponentsAttributeValue;
	}

	/**
	 * validateRequest
	 * 
	 * @param request
	 */
	protected void validateRequest(UpdateRequest request) throws TclCommonException {
		if (request.getComponentDetails() == null || request.getComponentDetails().isEmpty()) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}

	}

	/**
	 * validateUpdateRequest
	 * 
	 * @param request
	 */
	protected void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ saveIllsiteProperties used to
	 *       saveillsite properties
	 * @param localITContactId
	 * @param quoteIllSite
	 * @param mstProductFamily
	 */
	private void saveIllsiteProperties(QuoteIllSite quoteIllSite, UpdateRequest request, User user, MstProductFamily mstProductFamily) {
		MstProductComponent mstProductComponent = getMstProperties(user);
		constructIllSitePropeties(mstProductComponent, quoteIllSite, user.getUsername(), request, mstProductFamily);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructIllSitePropeties used to
	 *       construct site properties
	 * @param mstProductComponent
	 * @param quoteIllSite
	 * @param username
	 * @param localITContactId
	 */
	private void constructIllSitePropeties(MstProductComponent mstProductComponent, QuoteIllSite quoteIllSite,
			String username, Integer localITContactId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(mstProductComponent);
		quoteProductComponent.setReferenceId(quoteIllSite.getId());
		quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
		quoteProductComponentRepository.save(quoteProductComponent);
		ProductAttributeMaster attributeMaster = getAttributePropertiesMaster(username);
		quoteProductComponent.setQuoteProductComponentsAttributeValues(
				constructIllSiteAtrribute(quoteProductComponent, localITContactId, attributeMaster));

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructIllSiteAtrribute used to
	 *       constrcu illsite attributes
	 * @param quoteProductComponent
	 * @param localITContactId
	 * @param attributeMaster
	 * @return
	 */
	private Set<QuoteProductComponentsAttributeValue> constructIllSiteAtrribute(
			QuoteProductComponent quoteProductComponent, Integer localITContactId,
			ProductAttributeMaster attributeMaster) {
		Set<QuoteProductComponentsAttributeValue> quAttributeValues = new HashSet<>();
		QuoteProductComponentsAttributeValue attributeValue = new QuoteProductComponentsAttributeValue();
		attributeValue.setAttributeValues(String.valueOf(localITContactId));
		attributeValue.setDisplayValue(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
		attributeValue.setProductAttributeMaster(attributeMaster);
		attributeValue.setQuoteProductComponent(quoteProductComponent);
		quoteProductComponentsAttributeValueRepository.save(attributeValue);
		quAttributeValues.add(attributeValue);
		return quAttributeValues;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructAttributePropertiesMaster
	 *       used to constrcu Attribute Master properties
	 * 
	 * @param quoteProductComponent
	 */
	private ProductAttributeMaster getAttributePropertiesMaster(String name) {
		ProductAttributeMaster productAttributeMaster = null;
		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setCreatedBy(name);
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
			productAttributeMaster.setName(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructMstProperties used to
	 *       construct Mst Properties
	 * @param id
	 * @param localITContactId
	 */
	private MstProductComponent getMstProperties(User user) {

		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
			mstProductComponent.setDescription(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		return mstProductComponent;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException getSiteProperties used to get only site specific
	 *                            attributes
	 */
	public List<QuoteProductComponentBean> getSiteProperties(Integer siteId) throws TclCommonException {
		List<QuoteProductComponentBean> quoteProductComponentBeans = null;
		try {
			validateRequest(siteId);
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			quoteProductComponentBeans = getSortedComponents(
					constructQuoteProductComponent(quoteIllSite.getId(), true, false));

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteProductComponentBeans;
	}

	/**
	 * validateRequest
	 * 
	 * @param siteId
	 */
	private void validateRequest(Integer siteId) throws TclCommonException {
		if (siteId == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateLegalEntityProperties used to
	 *       update legal enitity properties
	 * 
	 * @param request
	 * @return
	 */
	public QuoteDetail updateLegalEntityProperties(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(request);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
			if (!optionalQuoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			if (request.getAttributeDetails() != null) {
				for (AttributeDetail attribute : request.getAttributeDetails()) {
					if (attribute.getName() != null) {
						LOGGER.info("Attribute Name {} ", attribute.getName());
						MstOmsAttribute mstOmsAttribute = persistMstOmsAttribute(user, attribute);
						saveLegalEntityAttributes(optionalQuoteToLe.get(), attribute, mstOmsAttribute);
					}
				}

			}
			if (request.getAttributeName() != null) {
				MstOmsAttribute omsAttribute = getMstAttributeMaster(request, user);
				constructQuoteLeAttribute(request, omsAttribute, optionalQuoteToLe.get());
			}

			if (Objects.isNull(optionalQuoteToLe.get().getQuoteCategory())
					|| (MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteCategory()))) {
				if (optionalQuoteToLe.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
					optionalQuoteToLe.get().setStage(QuoteStageConstants.CHECKOUT.getConstantCode());
					quoteToLeRepository.save(optionalQuoteToLe.get());
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
	}

	/**
	 * persistMstOmsAttribute
	 * 
	 * @param user
	 * @param attribute
	 * @return
	 */
	private MstOmsAttribute persistMstOmsAttribute(User user, AttributeDetail attribute) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository.findByNameAndIsActive(attribute.getName(),
				(byte) 1);
		if (!mstOmsAttributeList.isEmpty()) {
			mstOmsAttribute = mstOmsAttributeList.get(0);
			LOGGER.info("Mst already there with id  {} ", mstOmsAttribute.getId());
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(user.getUsername());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(attribute.getName());
			mstOmsAttribute.setDescription("");
			mstOmsAttributeRepository.save(mstOmsAttribute);
			LOGGER.info("Mst OMS Saved with id  {} ", mstOmsAttribute.getId());
		}
		return mstOmsAttribute;
	}

	/**
	 * constructQuoteLeAttribute used to construct Quote Le attribute
	 * 
	 * @param request
	 * @param omsAttribute
	 * @param quoteToLe
	 */
	private void constructQuoteLeAttribute(UpdateRequest request, MstOmsAttribute omsAttribute, QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, request.getAttributeName());
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setMstOmsAttribute(omsAttribute);
				attrVal.setAttributeValue(request.getAttributeValue());
				attrVal.setDisplayValue(request.getAttributeName());
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeValueRepository.save(attrVal);
			});
		} else {
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(omsAttribute);
			quoteLeAttributeValue.setAttributeValue(request.getAttributeValue());
			quoteLeAttributeValue.setDisplayValue(request.getAttributeName());
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		}
	}

	/**
	 * getMstAttributeMaster used to get the Attribute Master
	 * 
	 * @param request
	 * @return
	 */
	private MstOmsAttribute getMstAttributeMaster(UpdateRequest request, User user) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(request.getAttributeName(), (byte) 1);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(user.getUsername());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(request.getAttributeName());
			mstOmsAttribute.setDescription(request.getAttributeValue());
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}

	public String returnServiceProviderName(Integer id) throws TclCommonException {
		try {
			LOGGER.info("MDC Filter token value in before Queue call returnServiceProviderName {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			return (String) mqUtils.sendAndReceive(spQueue,
					Utils.convertObjectToJson(constructSupplierDetailsRequestBean(id)));

		} catch (Exception e) {
			throw new TclCommonException("No Service Provider Name");
		}
	}

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/ getAllAttributesByQuoteToLeId get
	 *       all attribute details by quote to le id
	 * 
	 * @param quoteToLeId
	 * @return QuoteToLeDto
	 * @throws TclCommonException
	 */

	public Set<LegalAttributeBean> getAllAttributesByQuoteToLeId(Integer quoteToLeId) throws TclCommonException {
		Set<LegalAttributeBean> legalEntityAttributes = null;
		try {
			if (Objects.isNull(quoteToLeId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			Optional<QuoteToLe> optQuoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (optQuoteToLe.isPresent()) {
				legalEntityAttributes = constructLegalAttributes(optQuoteToLe.get());
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return legalEntityAttributes;
	}

	public QuoteDetail persistListOfQuoteLeAttributes(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			LOGGER.info("Input Received {}", request);
			validateUpdateRequest(request);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
			if (!optionalQuoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			List<AttributeDetail> attributeDetails = request.getAttributeDetails();
			for (AttributeDetail attribute : attributeDetails) {
				if (attribute.getName() != null) {
					LOGGER.info("Attribute Name {} ", attribute.getName());
					MstOmsAttribute mstOmsAttribute = null;
					List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
							.findByNameAndIsActive(attribute.getName(), (byte) 1);
					if (!mstOmsAttributeList.isEmpty()) {
						mstOmsAttribute = mstOmsAttributeList.get(0);
						LOGGER.info("Mst already there with id  {} ", mstOmsAttribute.getId());
					}
					if (mstOmsAttribute == null) {
						mstOmsAttribute = new MstOmsAttribute();
						mstOmsAttribute.setCreatedBy(user.getUsername());
						mstOmsAttribute.setCreatedTime(new Date());
						mstOmsAttribute.setIsActive((byte) 1);
						mstOmsAttribute.setName(attribute.getName());
						mstOmsAttribute.setDescription("");
						mstOmsAttributeRepository.save(mstOmsAttribute);
						LOGGER.info("Mst OMS Saved with id  {} ", mstOmsAttribute.getId());
					}

					saveLegalEntityAttributes(optionalQuoteToLe.get(), attribute, mstOmsAttribute);
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * saveLegalEntityAttributes
	 * 
	 * @param quoteToLe
	 * @param attribute
	 * @param mstOmsAttribute
	 */
	private void saveLegalEntityAttributes(QuoteToLe quoteToLe, AttributeDetail attribute,
			MstOmsAttribute mstOmsAttribute) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getName());
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			List<Map<String, String>> fromValues = new ArrayList<>();
			List<Map<String, String>> toValues = new ArrayList<>();
			quoteLeAttributeValues.forEach(attrVal -> {
				LOGGER.info("Inside quote to le update quoteLe {} and attribute name {} ", attrVal.getQuoteToLe().getId(), attribute.getName());
				Map<String, String> fromValue = new HashMap<>();
				if(attribute.getName().equalsIgnoreCase(LeAttributesConstants.LE_NAME) || attribute.getName().equalsIgnoreCase(LeAttributesConstants.LE_EMAIL)
						||attribute.getName().equalsIgnoreCase(LeAttributesConstants.LE_CONTACT) || attribute.getName().equalsIgnoreCase("Supplier Mobile")) {
					fromValue.put("Oms_attribute_name",attribute.getName());
					fromValue.put("attribute_value",attrVal.getAttributeValue());
					fromValue.put("quoteLeAttributeId",attrVal.getId().toString());
					fromValue.put("quoteToLeId",quoteToLe.getId().toString());
					fromValues.add(fromValue);
				}
				attrVal.setMstOmsAttribute(mstOmsAttribute);
				attrVal.setAttributeValue(attribute.getValue());
				attrVal.setDisplayValue(attribute.getName());
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeValueRepository.save(attrVal);
				if(!fromValue.isEmpty()) {
					Map<String, String> toValue = new HashMap<>();
					toValue.put("Oms_attribute_name",mstOmsAttribute.getName());
					toValue.put("attribute_value",attrVal.getAttributeValue());
					toValue.put("quoteToLeId",quoteToLe.getId().toString());
					toValues.add(toValue);
				}

			});
			if(!fromValues.isEmpty() && !toValues.isEmpty()) {
				saveUtilityAudit(quoteToLe.getQuote().getQuoteCode(), toValues.toString(), fromValues.toString(), "UPDATE ACCOUNT DETAILS");
			}
		} else {
			LOGGER.info("Inside quote to create");
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setAttributeValue(attribute.getValue());
			quoteLeAttributeValue.setDisplayValue(attribute.getName());
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		}
	}

	public QuoteDetail updateQuoteToLeStatus(Integer quoteToLeId, String status) throws TclCommonException {
		QuoteDetail quoteDetail = new QuoteDetail();
		try {
			if (Objects.isNull(quoteToLeId) || (StringUtils.isEmpty(status))) {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (!quoteToLe.isPresent())
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			quoteToLe.get().setStage(QuoteStageConstants.valueOf(status.toUpperCase()).toString());
			quoteToLeRepository.save(quoteToLe.get());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;

	}

	/**
	 * checkQuoteLeFeasibility - this method checks the pricing and feasibility for
	 * the given quote legal entity id.
	 * 
	 * @author NAVEEN GUNASEKARAN checkQuoteLeFeasibility
	 * @param quoteLeId
	 * @return QuoteLeAttributeBean
	 */
	public QuoteLeAttributeBean checkQuoteLeFeasibility(Integer quoteLeId) {
		QuoteLeAttributeBean quoteLeAttributeBean = new QuoteLeAttributeBean();
		Optional<QuoteToLe> optQuoteToLe = quoteToLeRepository.findById(quoteLeId);
		if (optQuoteToLe.isPresent()) {
			quoteLeAttributeBean.setQuoteLegalEntityId(quoteLeId);
			for (QuoteLeAttributeValue quoteLeAttribte : optQuoteToLe.get().getQuoteLeAttributeValues()) {

				if (quoteLeAttribte.getMstOmsAttribute().getName()
						.equalsIgnoreCase(QuoteConstants.ISFEASIBLITYCHECKDONE.toString())) {
					quoteLeAttributeBean.setIsFeasibilityCheckDone(quoteLeAttribte.getAttributeValue());
				}

				else if (quoteLeAttribte.getMstOmsAttribute().getName()
						.equalsIgnoreCase(QuoteConstants.ISPRICINGCHECKDONE.toString())) {
					quoteLeAttributeBean.setIsPricingCheckDone(quoteLeAttribte.getAttributeValue());
				}
			}

		}
		return quoteLeAttributeBean;
	}

	/**
	 * This method is to update audit information
	 * 
	 * @param name
	 * @param publicIp
	 * @param craetedTime
	 * @param orderRefId
	 * @throws TclCommonException
	 */
	public void updateOrderConfirmationAudit(String publicIp, String orderRefUuid, String checkList)
			throws TclCommonException {

		try {
			String name = Utils.getSource();
			OrderConfirmationAudit orderConfirmationAudit = new OrderConfirmationAudit();
			orderConfirmationAudit.setName(name);
			orderConfirmationAudit.setPublicIp(publicIp);
			orderConfirmationAudit.setOrderRefUuid(orderRefUuid);
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * getPublicIp
	 */
	public String getPublicIp(String publicIp) {
		String[] publicIps = publicIp.split(",");
		Pattern ipPattern = Pattern.compile(CommonConstants.PUBLIC_IP_PATTERN);
		for (String ip : publicIps) {
			if (ip.contains("%3")) {
				ip = ip.replace("%3", "");
			}
			if (ipPattern.matcher(ip).matches()) {
				return ip;
			}
		}
		return null;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateBillingInfoForSfdc this
	 *            method
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	public void updateBillingInfoForSfdc(CustomerLeDetailsBean request, QuoteToLe quoteToLe) throws TclCommonException {
		try {
			LOGGER.info("Inside updateBillingInfoForSfdc method");
			construcBillingSfdcAttribute(quoteToLe, request);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	/**
	 * 
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited construcBillingSfdcAttribute used
	 *            to construct billing attributes
	 * @param quoteToLe
	 * @param request
	 * @param user
	 * @throws TclCommonException
	 */
	private void construcBillingSfdcAttribute(QuoteToLe quoteToLe, CustomerLeDetailsBean request) {
		LOGGER.info("Inside construcBillingSfdcAttribute method");
		if (request.getAttributes() != null) {

			request.getAttributes().forEach(billAttr -> constructBillingAttribute(billAttr, quoteToLe)

			);
		}
		processAccount(quoteToLe, request.getAccounCuId(), LeAttributesConstants.ACCOUNT_CUID);
		processAccount(quoteToLe, request.getAccountId(), LeAttributesConstants.ACCOUNT_NO18);
		// processAccount(quoteToLe, String.valueOf(request.getBillingContactId()),
		// LeAttributesConstants.BILLING_CONTACT_ID);
		processAccount(quoteToLe, request.getLegalEntityName(), LeAttributesConstants.LEGAL_ENTITY_NAME);
	}

	/**
	 * processAccountCuid used to process account details from customer mdm
	 * 
	 * @param quoteToLe
	 * @param request
	 * @param user
	 */
	protected void processAccount(QuoteToLe quoteToLe, String attrValue, String attributeName) {
		LOGGER.info("Inside processAccount method for attribute {}", attributeName );
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attributeName);

		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attr -> {
				attr.setAttributeValue(attrValue);
				quoteLeAttributeValueRepository.save(attr);

			});

		} else {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(attrValue);
			attributeValue.setDisplayValue(attributeName);
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attributeName);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValueRepository.save(attributeValue);

		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited constructBillingAttribute used to
	 *            construct billing attribute
	 * @param attribute
	 */
	private void constructBillingAttribute(Attributes attribute, QuoteToLe quoteToLe) {

		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getAttributeName());

		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			updateAttributes(attribute, quoteLeAttributeValues, quoteToLe.getQuote().getQuoteCode());

		} else {
			createAttribute(attribute, quoteToLe);

		}

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited createAttribute used to create
	 *            billing attributes
	 * @param attribute
	 * @param quoteToLe
	 * @param user
	 */
	private void createAttribute(Attributes attribute, QuoteToLe quoteToLe) {
		if (attribute.getAttributeName() != null) {
			Optional<Quote> quote = quoteRepository.findById(quoteToLe.getQuote().getId());
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			if (quote.get().getQuoteCode().toUpperCase().startsWith(GVPN)
					&& quoteToLe.getQuoteType().equalsIgnoreCase(MACD)
					&& attribute.getAttributeName().equalsIgnoreCase(BILLING_FREQUENCY)) {
				//to save billing frequency value from inventory for GVPN MACD quotes
				SIServiceDetailDataBean serviceDetail = null;
				if (Objects.nonNull(quoteToLe)) {
					if (quoteToLe.getQuoteType().equalsIgnoreCase(MACD)) {
						List<String> serviceIds = macdUtils.getServiceIds(quoteToLe);
						String serviceId = "";
						if (Objects.nonNull(serviceIds) && !serviceIds.isEmpty()) {
							serviceId = serviceIds.stream().findFirst().get();
						}
						try {
							serviceDetail = macdUtils.getServiceDetail(serviceId, quoteToLe.getQuoteCategory());
						} catch (TclCommonException e) {
							LOGGER.info("Error in retrieving Service Info to create Billing Frequency");
						}
					}
				}
				if (Objects.nonNull(serviceDetail) && StringUtils.isNotBlank(serviceDetail.getBillingFrequency())) {
					LOGGER.info("Billing Frequency value {} to be in inserted in quoteLeAttrValues ", serviceDetail.getBillingFrequency());
					attributeValue.setAttributeValue(serviceDetail.getBillingFrequency());
					attributeValue.setDisplayValue(attribute.getAttributeName());
					attributeValue.setQuoteToLe(quoteToLe);
					MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
					attributeValue.setMstOmsAttribute(mstOmsAttribute);
					quoteLeAttributeValueRepository.save(attributeValue);
				}

			} else {
					attributeValue.setAttributeValue(attribute.getAttributeValue());
					attributeValue.setDisplayValue(attribute.getAttributeName());
					attributeValue.setQuoteToLe(quoteToLe);
					MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
					attributeValue.setMstOmsAttribute(mstOmsAttribute);
					LOGGER.info("Attribute Name {} and Value {} to be in inserted in quoteLeAttrValues ", attribute.getAttributeName(), attribute.getAttributeValue());
					quoteLeAttributeValueRepository.save(attributeValue);
				}
			}
		}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateAttributes used to update
	 *            billing attributes
	 * @param attribute
	 * @param quoteLeAttributeValues
	 */
	private void updateAttributes(Attributes attribute, List<QuoteLeAttributeValue> quoteLeAttributeValues,
			String quoteCode) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeAttributeValues.get(0).getQuoteToLe().getId());

		quoteLeAttributeValues.forEach(attr -> {
			if (quoteCode.toUpperCase().startsWith(GVPN)) {
				if (!attr.getMstOmsAttribute().getName().equalsIgnoreCase("Payment Currency")
						&& !attr.getMstOmsAttribute().getName().equalsIgnoreCase("Billing Currency")&&!attr.getMstOmsAttribute().getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY)) {
					if (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACD) && attr.getDisplayValue().equalsIgnoreCase(BILLING_FREQUENCY))
					{
						SIServiceDetailDataBean serviceDetail = null;
						//to save billing frequency value from inventory for MACD quotes
						if (Objects.nonNull(quoteToLe))
						{
							if (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACD)) {
								List<String> serviceIds = macdUtils.getServiceIds(quoteToLe.get());
								String serviceId = "";
								if(Objects.nonNull(serviceIds) && !serviceIds.isEmpty()) {
									serviceId = serviceIds.stream().findFirst().get();
								}
								try {
									serviceDetail = macdUtils.getServiceDetail(serviceId,quoteToLe.get().getQuoteCategory());
								} catch (TclCommonException e) {
									LOGGER.info("Error in retrieving Service Info to update Billing Frequency");
								}
							}
						}
						if (Objects.nonNull(serviceDetail) && StringUtils.isNotBlank(serviceDetail.getBillingFrequency())) {
							LOGGER.info("Billing Frequency value {} to be in updated in quoteLeAttrValues ", serviceDetail.getBillingFrequency());
							attr.setAttributeValue(serviceDetail.getBillingFrequency());
							quoteLeAttributeValueRepository.save(attr);
						}
					}
					else
					{
						LOGGER.info("Attribute Name {} and Value {} to be in updated in quoteLeAttrValues ", attribute.getAttributeName(), attribute.getAttributeValue());
						attr.setAttributeValue(attribute.getAttributeValue());
						quoteLeAttributeValueRepository.save(attr);
					}
				}
			} else {
				if (!attr.getMstOmsAttribute().getName().equalsIgnoreCase("Payment Currency")&&!attr.getMstOmsAttribute().getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY)) {
					attr.setAttributeValue(attribute.getAttributeValue());
					quoteLeAttributeValueRepository.save(attr);
				}
			}

		});
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getMstAttributeMasterForBilling
	 *            used to get the attribute master
	 * @param request
	 * @return
	 */
	private MstOmsAttribute getMstAttributeMasterForBilling(String attrName) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attrName, (byte) 1);
		if (mstOmsAttributes != null && !mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(Utils.getSource());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(attrName);
			mstOmsAttribute.setDescription(attrName);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}

	/**
	 * 
	 * updateSfdcStage
	 * 
	 * @param quoteToLeId
	 * @param stage
	 * @throws TclCommonException
	 */
	public void updateSfdcStage(Integer quoteToLeId, String stage) throws TclCommonException {
		LOGGER.info("update sfdc stage for quoteToLeId {}  with stage {} ", quoteToLeId , stage);
		if (StringUtils.isNotBlank(stage) && (SFDCConstants.PROPOSAL_SENT.equals(stage))) {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				String sfdcId = quoteToLe.get().getTpsSfdcOptyId();
				omsSfdcService.processUpdateOpportunity(new Date(), sfdcId, stage, quoteToLe.get());
				creditCheckService.resetCreditCheckFields( quoteToLe.get());
			}

		}
	}

	/**
	 * getContactAttributeDetails
	 * 
	 * @param quoteLeId
	 * @return
	 */
	public ContactAttributeInfo getContactAttributeDetails(Integer quoteLeId) throws TclCommonException {
		ContactAttributeInfo attributeInfo = null;
		try {
			attributeInfo = new ContactAttributeInfo();

			Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(quoteLeId);
			if (optionalQuoteLe.isPresent()) {
				QuoteToLe quToLe = optionalQuoteLe.get();

				if (quToLe.getQuoteLeAttributeValues() != null && !quToLe.getQuoteLeAttributeValues().isEmpty()) {
					constructAttributeInfo(quToLe, attributeInfo);

				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attributeInfo;
	}

	/**
	 * constructAttributeInfo
	 * 
	 * @param quToLe
	 * @param attributeInfo
	 */
	private void constructAttributeInfo(QuoteToLe quToLe, ContactAttributeInfo attributeInfo) {

		quToLe.getQuoteLeAttributeValues().forEach(attrval -> {
			if (attrval.getMstOmsAttribute() != null && attrval.getMstOmsAttribute().getName() != null) {
				switch (attrval.getMstOmsAttribute().getName()) {
				case LeAttributesConstants.CONTACT_ID:

					attributeInfo.setUserId(attrval.getAttributeValue());

					break;
				case LeAttributesConstants.CONTACT_NAME:

					attributeInfo.setFirstName(attrval.getAttributeValue());

					break;
				case LeAttributesConstants.CONTACT_EMAIL:

					attributeInfo.setEmailId(attrval.getAttributeValue());

					break;
				case LeAttributesConstants.DESIGNATION:

					attributeInfo.setDesignation(attrval.getAttributeValue());

					break;
				case LeAttributesConstants.CONTACT_NO:

					attributeInfo.setContactNo(attrval.getAttributeValue());

					break;

				default:
					break;
				}
			}
		});

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	public QuoteDetail updateSiteProperties(UpdateRequest request) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			validateUpdateRequest(request);
			User user;
			detail = new QuoteDetail();
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(request.getSiteId(), (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
					(byte) 1);

			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

			}

            if(request.getUserName()==null) {
           	 user = getUserId(Utils.getSource());
            }else {
           	 user = getUserId(request.getUserName());
            }
			saveIllsiteProperties(quoteIllSite, request, user, mstProductFamily);
			
			//added for multisitebilling info gst updation
			LOGGER.info("quote to le id ::::"+request.getQuoteToLe());
			if (request.getAttributeDetails() != null && request.getQuoteToLe() != null) {
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
				if (quoteToLe.isPresent()) {
					if (quoteToLe.get().getSiteLevelBilling() != null) {
						if (quoteToLe.get().getSiteLevelBilling().equalsIgnoreCase("1")) {
							for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
								if (attributeDetail.getName().equalsIgnoreCase(AttributeConstants.GST_NUMBER.toString())) {
									updateGstNumber(attributeDetail.getValue(), quoteIllSite);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	public QuoteDetail updateSitePropertiesAttributes(UpdateRequest request) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			validateUpdateRequest(request);
			detail = new QuoteDetail();
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(request.getSiteId(), (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
					(byte) 1);

			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			User user = getUserId(Utils.getSource());
			MstProductComponent mstProductComponent = getMstProperties(user);
			constructIllSitePropeties(mstProductComponent, quoteIllSite, user.getUsername(), request, mstProductFamily);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	private void constructIllSitePropeties(MstProductComponent mstProductComponent, QuoteIllSite orderIllSite,
			String username, UpdateRequest request, MstProductFamily mstProductFamily) {
		List<QuoteProductComponent> orderProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent(orderIllSite.getId(), mstProductComponent);
		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
			updateIllSiteProperties(orderProductComponents, request, username);
		} else {
			createIllSiteAttribute(mstProductComponent, mstProductFamily, orderIllSite, request, username);
		}

	}

	private void createIllSiteAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
			QuoteIllSite orderIllSite, UpdateRequest request, String username) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(mstProductComponent);
		quoteProductComponent.setReferenceId(orderIllSite.getId());
		quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponentRepository.save(quoteProductComponent);

		if (request.getAttributeDetails() != null) {
			for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
				createSitePropertiesAttribute(quoteProductComponent, attributeDetail, username);

			}

		}
	}

	private void updateIllSiteProperties(List<QuoteProductComponent> orderProductComponents, UpdateRequest request,
			String username) {

		orderProductComponents.forEach(orderProductComponent -> {

			if (request.getAttributeDetails() != null) {
				for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
					List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
					if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
						upateSitePropertiesAttribute(productAttributeMasters, attributeDetail, orderProductComponent);
					} else {
						createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);
					}
				}
			}

		});
	}

	private void upateSitePropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
			AttributeDetail attributeDetail, QuoteProductComponent orderProductComponent) {

		List<QuoteProductComponentsAttributeValue> orderProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(orderProductComponent,
						productAttributeMasters.get(0));
		if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
			for (QuoteProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
				if (orderProductComponentsAttributeValue.getIsAdditionalParam() != null
						&& orderProductComponentsAttributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> additionalServiceParam = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeDetail.getValue()));
					if (additionalServiceParam.isPresent()) {
						additionalServiceParam.get().setValue(attributeDetail.getValue());
						additionalServiceParam.get().setUpdatedBy(Utils.getSource());
						additionalServiceParam.get().setUpdatedTime(new Date());
						additionalServiceParamRepository.save(additionalServiceParam.get());
					}
				} else {
					if (attributeDetail.getValue()!=null && attributeDetail.getValue().length() > 255) {
						AdditionalServiceParams additionalServiceParams = new AdditionalServiceParams();
						additionalServiceParams.setAttribute(attributeDetail.getName());
						additionalServiceParams.setCategory("QUOTE");
						additionalServiceParams.setCreatedBy(Utils.getSource());
						additionalServiceParams.setCreatedTime(new Date());
						additionalServiceParams.setIsActive(CommonConstants.Y);
						additionalServiceParams.setValue(attributeDetail.getValue());
						additionalServiceParamRepository.save(additionalServiceParams);
						orderProductComponentsAttributeValue.setAttributeValues(additionalServiceParams.getId() + "");
						orderProductComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
					} else {
						orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
					}
				}
				orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
				quoteProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);

			}
		} else {

			orderProductComponent.setQuoteProductComponentsAttributeValues(
					createAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

		}

	}

	private Set<QuoteProductComponentsAttributeValue> createAttributes(ProductAttributeMaster attributeMaster,
			QuoteProductComponent orderProductComponent, AttributeDetail attributeDetail) {

		Set<QuoteProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();

		QuoteProductComponentsAttributeValue orderProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		if(attributeDetail.getValue()!=null && attributeDetail.getValue().length() >255) {
			AdditionalServiceParams additionalServiceParams=new AdditionalServiceParams();
			additionalServiceParams.setAttribute(attributeDetail.getName());
			additionalServiceParams.setCategory("QUOTE");
			additionalServiceParams.setCreatedBy(Utils.getSource());
			additionalServiceParams.setCreatedTime(new Date());
			additionalServiceParams.setIsActive(CommonConstants.Y);
			additionalServiceParams.setValue(attributeDetail.getValue());
			additionalServiceParamRepository.save(additionalServiceParams);
			orderProductComponentsAttributeValue.setAttributeValues(additionalServiceParams.getId()+"");
			orderProductComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
		}else {
			orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		}
		orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setQuoteProductComponent(orderProductComponent);
		orderProductComponentsAttributeValue.setProductAttributeMaster(attributeMaster);
		quoteProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValues.add(orderProductComponentsAttributeValue);

		return orderProductComponentsAttributeValues;

	}

	private void createSitePropertiesAttribute(QuoteProductComponent orderProductComponent,
			AttributeDetail attributeDetail, String username) {

		ProductAttributeMaster attributeMaster = getPropertiesMaster(username, attributeDetail);
		orderProductComponent.setQuoteProductComponentsAttributeValues(
				createAttributes(attributeMaster, orderProductComponent, attributeDetail));

	}

	private ProductAttributeMaster getPropertiesMaster(String name, AttributeDetail attributeDetail) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}

		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setCreatedBy(name);
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(attributeDetail.getName());
			productAttributeMaster.setName(attributeDetail.getName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;

	}

	/**
	 * getOrderSummary
	 * 
	 * @return
	 */
	public List<QuoteSummaryBean> getQuoteSummary() throws TclCommonException {
		List<QuoteSummaryBean> ordersBeans = null;
		try {
			ordersBeans = new ArrayList<>();
			List<Quote> quotes = quoteRepository.findTop100ByOrderByCreatedTimeDesc();
			if (quotes != null) {
				for (Quote order : quotes) {
					constructSummaryForSV(order, ordersBeans);

				}

			}
		}

		catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBeans;
	}

	/**
	 * 
	 * getQuoteSummary
	 * 
	 * @param page
	 * @param size
	 * @return
	 * @throws TclCommonException
	 */
	public PagedResult<QuoteSummaryBean> getQuoteSummary(int page, int size, String searchTerm)
			throws TclCommonException {
		List<QuoteSummaryBean> ordersBeans = null;
		Page<Quote> quotes = null;
		try {
			ordersBeans = new ArrayList<>();
			if (searchTerm == null) {
				quotes = quoteRepository.findAllByOrderByCreatedTimeDesc(PageRequest.of(page, size));
			} else {

				quotes = quoteRepository.findByQuoteCodeContainsAllIgnoreCase(searchTerm, PageRequest.of(page, size));

			}
			if (quotes != null) {
				for (Quote order : quotes.getContent()) {
					constructSummaryForSV(order, ordersBeans);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new PagedResult<>(ordersBeans, quotes.getTotalElements(), quotes.getTotalPages());
	}

	/**
	 * constructSummaryForSV
	 * 
	 * @param order
	 * @param ordersBeans
	 */
	private void constructSummaryForSV(Quote quote, List<QuoteSummaryBean> quoteBeans) {
		QuoteSummaryBean bean = constructQuoteSummaryBean(quote);
		if (quote.getQuoteToLes() != null && !quote.getQuoteToLes().isEmpty()) {
			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				constructLeForSV(quoteLe, bean);
				for (QuoteToLeProductFamily family : quoteLe.getQuoteToLeProductFamilies()) {
					construcyFamilyForSV(family, bean);
				}

			}
		}
		quoteBeans.add(bean);
	}

	/**
	 * construcyFamilyForSV
	 * 
	 * @param family
	 * @param bean
	 */
	private void construcyFamilyForSV(QuoteToLeProductFamily family, QuoteSummaryBean bean) {
		QuoteFamilySVBean orderFamilySVBean = new QuoteFamilySVBean();
		orderFamilySVBean.setName(family.getMstProductFamily().getName());
		orderFamilySVBean.setFamilyId(family.getId());
		bean.getQuoteFamily().add(orderFamilySVBean);
	}

	/**
	 * constructLeForSV
	 * 
	 * @param quoteTLe
	 * @param bean
	 * @return
	 */
	private QuoteLeSVBean constructLeForSV(QuoteToLe quoteTLe, QuoteSummaryBean bean) {
		QuoteLeSVBean quoteLeSVBean = new QuoteLeSVBean();
		quoteLeSVBean.setLegalEntityId(quoteTLe.getId());
		quoteLeSVBean.setStage(quoteTLe.getStage());
		quoteLeSVBean.setOpportunityId(quoteTLe.getTpsSfdcOptyId());
		quoteLeSVBean.setLegalEntityName(extractLeAttributes(quoteTLe, LeAttributesConstants.LEGAL_ENTITY_NAME));
		quoteLeSVBean.setAccountCuid(extractLeAttributes(quoteTLe, LeAttributesConstants.ACCOUNT_CUID));
		bean.getLegalEntity().add(quoteLeSVBean);
		return quoteLeSVBean;
	}

	/**
	 * constructOrderSummaryBean
	 * 
	 * @param order
	 * @return
	 */
	private QuoteSummaryBean constructQuoteSummaryBean(Quote quote) {
		QuoteSummaryBean bean = new QuoteSummaryBean();

		bean.setQuoteId(quote.getId());
		if (quote.getCreatedBy() != null) {
			Optional<User> optionaluser = userRepository.findById(quote.getCreatedBy());
			if (optionaluser.isPresent()) {
				bean.setCreatedBy(optionaluser.get().getUsername());
			}
		}
		bean.setCreatedTime(quote.getCreatedTime());
		bean.setQuoteCode(quote.getQuoteCode());
		return bean;
	}

	/**
	 * extractLeAttributes
	 * 
	 * @param orderTLe
	 */
	private String extractLeAttributes(QuoteToLe quoteTLe, String attributeName) {
		MstOmsAttribute mstAttributes = null;
		String attributeValue = null;
		List<MstOmsAttribute> mstAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(attributeName,
				(byte) 1);
		if (!mstAttributesList.isEmpty()) {
			mstAttributes = mstAttributesList.get(0);

		}
		if (mstAttributes != null) {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteTLe, mstAttributes);
			for (QuoteLeAttributeValue quoteLeAttributeValue : quoteLeAttributeValues) {
				attributeValue = quoteLeAttributeValue.getAttributeValue();
			}
		}
		return attributeValue;
	}

	@Transactional
	public void updateCurrency(Integer quoteId, Integer quoteToLeId, String inputCurrency) throws TclCommonException {
		try {
			String existingCurrency = null;
			if (Objects.isNull(quoteId) || Objects.isNull(quoteToLeId) || StringUtils.isEmpty(inputCurrency)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			inputCurrency = inputCurrency.toUpperCase();
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				existingCurrency = quoteToLe.get().getCurrencyCode();
				if (!existingCurrency.equalsIgnoreCase(inputCurrency)) {
					Quote quote = quoteToLe.get().getQuote();
					updateQuoteToLeCurrencyValues(quote, inputCurrency, existingCurrency);
					updateQuoteIllSitesCurrencyValues(quoteToLe.get(), inputCurrency, existingCurrency);
					updateQuotePriceCurrencyValues(quote, inputCurrency, existingCurrency);
					updatePaymentCurrencyWithInputCurrency(quoteToLe.get(), inputCurrency);
				}
				try {
					if (quoteToLe.get().getQuote().getQuoteCode()
							.startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
						omsSfdcService.processUpdateProductForGSC(quoteToLe.get());
					} else {
						omsSfdcService.processUpdateProduct(quoteToLe.get());
					}

					LOGGER.info("Trigger update product sfdc");
				} catch (TclCommonException e) {
					LOGGER.info("Error in updating sfdc with pricing");
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	public String getLeAttributes(QuoteToLe quoteTole, String attr) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attr,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute(quoteTole, mstOmsAttribute);
		for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute) {
			attrValue = quoteLeAttributeValue.getAttributeValue();
		}
		return attrValue;
	}

	/**
	 * @author Thamizhselvi Perumal Method to find existing payment currency
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	public String findExistingCurrency(QuoteToLe quoteTole) {
		return quoteTole.getCurrencyCode();

	}

	/**
	 * @author Thamizhselvi Perumal Method to update Payment Currency attribute as
	 *         input currency
	 * @param quoteToLeId
	 * @param inputCurrency
	 * @throws TclCommonException
	 */
	public void updatePaymentCurrencyWithInputCurrency(QuoteToLe quotele, String inputCurrency) {
		quotele.setCurrencyCode(inputCurrency);
		quoteToLeRepository.save(quotele);
	}

	/**
	 * @author Thamizhselvi perumal Method to update quoteToLe currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	public void updateQuoteToLeCurrencyValues(Quote quote, String inputCurrency, String existingCurrency) {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
		for (QuoteToLe quoteToLe : quoteToLes) {
			quoteToLe.setFinalArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getFinalArc()));
			quoteToLe.setFinalMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getFinalMrc()));
			quoteToLe.setFinalNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getFinalNrc()));
			quoteToLe.setProposedArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getProposedArc()));
			quoteToLe.setProposedMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getProposedMrc()));
			quoteToLe.setProposedNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getProposedNrc()));
			quoteToLe.setTotalTcv(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteToLe.getTotalTcv()));
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Method to set termInMonths
	 *
	 * @param context
	 * @param quoteToLe
	 */
	private String setTermInMonths(QuoteToLe quoteToLe) {
		if (quoteToLe.getTermInMonths() != null) {
			return String.valueOf(getMonthsforOpportunityTerms(quoteToLe.getTermInMonths()));
		}
		return "0";
	}

	/**
	 * Method to convert year into months
	 *
	 * @param termPeriod
	 * @return
	 */
	private Integer getMonthsforOpportunityTerms(String termPeriod) {
		String reg[] = termPeriod.split(CommonConstants.MULTI_SPACE);
		Integer month = Integer.valueOf(reg[0]);
		if (reg.length > 0) {
			if (termPeriod.contains("year")) {
				return month * 12;
			}
		}
		return month;
	}

	/**
	 * @author Thamizhselvi Perumal Method to update quoteIllSites currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	public void updateQuoteIllSitesCurrencyValues(QuoteToLe quoteToLe, String inputCurrency, String existingCurrency) {
		Double totalMrc = 0.0;
		Double totalNrc = 0.0;
		Double totalArc = 0.0;

		for (QuoteToLeProductFamily quoteLeProdFamily : quoteToLe.getQuoteToLeProductFamilies()) {
			for (ProductSolution prodSolution : quoteLeProdFamily.getProductSolutions()) {
				for (QuoteIllSite illSite : prodSolution.getQuoteIllSites()) {
					List<QuoteProductComponent> components = getSiteComponents(illSite);

					Double siteTotalMrc = 0.0;
					Double siteTotalNrc = 0.0;
					Double siteTotalArc = 0.0;

					for (QuoteProductComponent component : components) {
						QuotePrice attrPrice = quotePriceRepository.findByReferenceId(component.getId().toString());
						if (Objects.nonNull(attrPrice)) {
							if (Objects.nonNull(attrPrice.getEffectiveMrc())) {
								siteTotalMrc = siteTotalMrc + Utils.setPrecision(attrPrice.getEffectiveMrc(), 2);
							}
							if (Objects.nonNull(attrPrice.getEffectiveNrc())) {
								siteTotalNrc = siteTotalNrc + Utils.setPrecision(attrPrice.getEffectiveNrc(), 2);
							}
							if (Objects.nonNull(attrPrice.getEffectiveArc())) {
								siteTotalArc = siteTotalArc + Utils.setPrecision(attrPrice.getEffectiveArc(), 2);
							}
						}
					}

					illSite.setArc(siteTotalArc);
					illSite.setMrc(siteTotalMrc);
					illSite.setNrc(siteTotalNrc);
					illSite.setArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, siteTotalArc));
					illSite.setMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, siteTotalMrc));
					illSite.setNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, siteTotalNrc));

					Double contractTerm = Double.parseDouble(this.setTermInMonths(quoteToLe));
					if (Objects.nonNull(illSite.getMrc()) && Objects.nonNull(illSite.getNrc())) {
						// Double tcv=(contractTerm*Utils.setPrecision(illSite.getMrc(),
						// 2))+Utils.setPrecision(illSite.getNrc(), 2);
						illSite.setTcv(
								omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getTcv()));

					}
					illSiteRepository.save(illSite);
					totalMrc = totalMrc + illSite.getMrc();
					totalNrc = totalNrc + illSite.getNrc();
					totalArc = totalArc + illSite.getArc();
				}

			}

		}

		if (Objects.nonNull(quoteToLe.getQuote()) && Objects.nonNull(quoteToLe.getQuote().getQuoteCode())) {
			if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
				quoteToLe.setFinalMrc(totalMrc);
				quoteToLe.setFinalNrc(totalNrc);
				quoteToLe.setFinalArc(totalArc);
				quoteToLe.setProposedArc(totalArc);
				quoteToLe.setProposedMrc(totalMrc);
				quoteToLe.setProposedNrc(totalNrc);
				Double contractTerm = Double.parseDouble(this.setTermInMonths(quoteToLe));
				Double totalTcv = (contractTerm * Utils.setPrecision(quoteToLe.getFinalMrc(), 2))
						+ Utils.setPrecision(quoteToLe.getFinalNrc(), 2);
				quoteToLe.setTotalTcv(totalTcv);
				quoteToLeRepository.save(quoteToLe);
			}
		}
	}

	/**
	 * Method to get site components
	 * 
	 * @param illSite
	 * @return
	 */
	public List<QuoteProductComponent> getSiteComponents(QuoteIllSite illSite) {
		List<QuoteProductComponent> components = new ArrayList<>();
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(illSite.getId(), QuoteConstants.GVPN_SITES.toString());

		quoteProductComponents.stream().forEach(quoteProductComponent -> {
			if (quoteProductComponent.getMstProductComponent().getName()
					.equalsIgnoreCase(FPConstants.LAST_MILE.toString())
					|| quoteProductComponent.getMstProductComponent().getName()
							.equalsIgnoreCase(FPConstants.CPE.toString())
					|| quoteProductComponent.getMstProductComponent().getName()
							.equalsIgnoreCase(FPConstants.VPN_PORT.toString())) {
				components.add(quoteProductComponent);
			}
		});
		return components;
	}

	/**
	 * Method to set precision
	 *
	 * @param value
	 * @param precision
	 * @return
	 */
	/**
	 * Method to set precision
	 *
	 * @param value
	 * @param precision
	 * @return
	 */
	private Double setPrecision(Double value, Integer precision) {
		Double result = 0.0;
		if (Objects.nonNull(value)) {
			if (precision == 2) {
				result = Math.round(value * 100.0) / 100.0;
				DecimalFormat df1 = new DecimalFormat(".##");
				result = Double.parseDouble(df1.format(result));
			} else if (precision == 4) {
				result = Math.round(value * 10000.0) / 10000.0;
				DecimalFormat df2 = new DecimalFormat(".####");
				result = Double.parseDouble(df2.format(result));
			}
		}
		return result;
	}

	/**
	 * @author Thamizhselvi Perumal Method to update quote price currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	public void updateQuotePriceCurrencyValues(Quote quote, String inputCurrency, String existingCurrency) {

		List<QuotePrice> quotePrices = quotePriceRepository.findByQuoteId(quote.getId());
		for (QuotePrice quotePrice : quotePrices) {
			quotePrice.setCatalogArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getCatalogArc()));
			quotePrice.setCatalogMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getCatalogMrc()));
			quotePrice.setCatalogNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getCatalogNrc()));
			quotePrice.setComputedArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getComputedArc()));
			quotePrice.setComputedMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getComputedMrc()));
			quotePrice.setComputedNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getComputedNrc()));
			quotePrice.setEffectiveArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getEffectiveArc()));
			quotePrice.setEffectiveMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getEffectiveMrc()));
			quotePrice.setEffectiveNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getEffectiveNrc()));
			quotePrice.setEffectiveUsagePrice(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
					quotePrice.getEffectiveUsagePrice()));
			quotePrice.setMinimumArc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getMinimumArc()));
			quotePrice.setMinimumMrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getMinimumMrc()));
			quotePrice.setMinimumNrc(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, quotePrice.getMinimumNrc()));
			quotePriceRepository.save(quotePrice);
		}
	}

	/**
	 * @author VIVEK KUMAR K getEmailIdAndTriggerEmail
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */

	public TriggerEmailResponse getSupplierUnavailableTriggerEmail(Integer quoteId) throws TclCommonException {
		TriggerEmailResponse response = new TriggerEmailResponse(Status.SUCCESS.toString());
		try {
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				User users = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
				notificationService.salesOrdeSupplierUnavailableNotification(users.getEmailId(),
						String.valueOf(quoteId), quoteDashBoardRelativeUrl, CommonConstants.GVPN);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return response;
	}

	public ServiceResponse processMailAttachment(String email, Integer quoteId) throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();
		if (Objects.isNull(email) || !Utils.isValidEmail(email)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String quoteHtml = gvpnQuotePdfService.processQuoteHtml(quoteId);
			String fileName = "Quote_" + quoteId + ".pdf";
			notificationService.processShareQuoteNotification(email,
					java.util.Base64.getEncoder().encodeToString(quoteHtml.getBytes()), userInfoUtils.getUserFullName(),
					fileName, CommonConstants.GVPN);
			fileUploadResponse.setStatus(Status.SUCCESS);
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

	}

	/**
	 * 
	 * @author ANNE NISHA
	 * 
	 *         cofDownloadAccountManagerNotification - This method is used notify
	 *         the account manager when a customer downloads the cof
	 * @param orderId, orderToLeId
	 * @return String
	 * @throws TclCommonException
	 */
	public String cofDownloadAccountManagerNotification(Integer quoteToLeId) throws TclCommonException {
		String str = null;
		QuoteToLe quoteToLe = null;
		String userName = Utils.getSource();
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeOpt.isPresent()) {
			quoteToLe = quoteToLeOpt.get();
			User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
			MailNotificationBean mailNotificationBean = populateMailNotificationBeanCof(
					getLeAttributes(quoteToLe, LeAttributesConstants.LEGAL_ENTITY_NAME),
					quoteToLe.getQuote().getCustomer().getCustomerName(), user.getFirstName(),
					getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL), quoteToLe.getQuote().getQuoteCode(),
					appHost + quoteDashBoardRelativeUrl, quoteToLe);
			notificationService.cofDownloadNotification(mailNotificationBean);
		}
		return str;
	}

	private MailNotificationBean populateMailNotificationBeanCof(String customerAccountName, String customerName,
			String userName, String accountManagerEmail, String orderId, String quoteLink, QuoteToLe quoteToLe) throws TclCommonException {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setCustomerAccountName(customerAccountName);
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderId);
		mailNotificationBean.setQuoteLink(quoteLink);
		mailNotificationBean.setProductName(GVPN);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
			Integer partnerId = partnerService.getOpportunityByQuoteId(quoteToLe.getQuote().getId()).getPartnerId();
			mailNotificationBean.setCustomerName(setAccountOwnerName(String.valueOf(partnerId)));
		}
		return mailNotificationBean;
	}

	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public QuoteDetail approvedManualQuotes(Integer quoteId) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			if (optionalQuoteToLe.isPresent() && Objects.nonNull(optionalQuoteToLe.get().getQuoteType())
					&& MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType()) && Objects.nonNull(optionalQuoteToLe.get().getIsAmended()) && optionalQuoteToLe.get().getIsAmended()==0) {

					LOGGER.info("Entering into GVPN approve quote {}",quoteId);
					detail=gvpnMacdService.approvedMacdManualQuotes(quoteId);

			} else {
				Map<String, String> cofObjectMapper = new HashMap<>();
				CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quote.getQuoteCode(),
						Source.MANUAL_COF.getSourceType());
				if (cofDetail != null) {
					cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetail.getUriPath());
				}
				Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

				if (order != null) {
					detail.setOrderId(order.getId());
				} else {
					LOGGER.info("Entering contruct order for normal flow for quote -----> {} ", quote.getQuoteCode());
					order = constructOrder(quote, detail);
					LOGGER.info("Order is constructed successfully for quote ----> {} and the order id is -----> {} ", quote.getQuoteCode(), order.getId());
					if(checkO2cEnabled(order)) {
						order.setOrderToCashOrder(CommonConstants.BACTIVE);
						order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
						LOGGER.info("leaving approvedDocusignQuotes checkO2cEnabled o2corder {}, enabled {}", order.getOrderToCashOrder(), order.getIsOrderToCashEnabled());
						orderRepository.save(order);
					}
					/*
					 * if(checkO2cEnabledForOffnetWireless(order)) {
					 * order.setOrderToCashOrder(CommonConstants.BACTIVE); LOGGER.
					 * info("leaving approvedDocusignQuotes checkO2cEnabledForOffnetWireless o2corder {}, enabled {}"
					 * , order.getOrderToCashOrder(), order.getIsOrderToCashEnabled());
					 * orderRepository.save(order); }
					 */
					detail.setOrderId(order.getId());
					// Trigger SFDC
					for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
						if (quoteLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
							omsSfdcService.processSiteDetailsRenewals(quoteLe); // renewals-sfdc
						} else {
							omsSfdcService.processSiteDetails(quoteLe);
						}
						omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
						List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
						for (QuoteDelegation quoteDelegation : quoteDelegate) {
							quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
							quoteDelegationRepository.save(quoteDelegation);
						}
					}
				}

				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				}
				if (detail.isManualFeasible()) {
					cloneQuoteForNonFeasibileSite(quote);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	public void deleteQuote(Integer quoteId) throws TclCommonException {
		try {
			Quote quote = null;
			Order order = null;
			Optional<Quote> quoteToDelete = quoteRepository.findById(quoteId);
			Optional<Order> orderToDelete = orderRepository.findByQuote(quoteToDelete.get());

			if (orderToDelete.isPresent()) {
				order = orderToDelete.get();
				throw new TclCommonException(ExceptionConstants.TASKS_PENDING_FOR_QUOTE, ResponseResource.R_CODE_ERROR);
				//deleteOrderRelatedDetails(order);
			}
			if (quoteToDelete.isPresent()) {
				quote = quoteToDelete.get();

				if(tasksPending(quote.getId())) {
					throw new TclCommonException(ExceptionConstants.TASKS_PENDING_FOR_QUOTE, ResponseResource.R_CODE_ERROR);
				}
				
				quote.getQuoteToLes().stream().forEach(quoteToLe -> {
					try {
						// SFDC Update Opportunity - CLOSED DROPPED
						omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_DROPPED, quoteToLe);
						quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteToLeProdFamily -> {
							quoteToLeProdFamily.getProductSolutions().stream().forEach(prodSolution -> {
								prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
									List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
											.findByReferenceIdAndReferenceName(illSite.getId(),
													QuoteConstants.GVPN_SITES.toString());
									quoteProductComponentList.stream().forEach(quoteProdComponent -> {
										deleteQuoteProductComponent(quoteProdComponent);
									});
									deleteFeasibilityDetails(illSite);
									deleteQuoteSiteToServiceDetails(illSite);
									illSiteRepository.delete(illSite);
								});
								productSolutionRepository.delete(prodSolution);
							});
							quoteToLeProductFamilyRepository.delete(quoteToLeProdFamily);
						});
						deleteQuoteLeAttributeValues(quoteToLe);
						quoteToLeRepository.delete(quoteToLe);
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
				deleteQuotePrice(quote);
				quoteRepository.delete(quote);

			}

		} catch (Exception e) {
			if(e instanceof TclCommonException)
				throw e;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private boolean tasksPending(Integer quoteId) {
		boolean[] tasksPending = {false};
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId).get(0);
		Set<QuoteToLeProductFamily> quoteProductFamilySet = quoteToLe.getQuoteToLeProductFamilies();
		quoteProductFamilySet.stream().forEach(quoteToLeProductFamily ->{
			Set<ProductSolution> productSolutions = quoteToLeProductFamily.getProductSolutions();
			productSolutions.stream().forEach(productSolution ->{
				Set<QuoteIllSite> quoteIllSites = productSolution.getQuoteIllSites();
				tasksPending[0] = quoteIllSites.stream().filter(site -> (site.getMfTaskTriggered() == 1 && StringUtils.isEmpty(site.getMfStatus())) || site.getIsTaskTriggered() == 1).findAny().isPresent();
			} ); 

		} );
		
		return tasksPending[0];
	}
	private void deleteQuoteSiteToServiceDetails(QuoteIllSite illSite) {
		List<QuoteIllSiteToService> siteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite(illSite);
		if(siteToServiceList != null && !siteToServiceList.isEmpty())
			quoteIllSiteToServiceRepository.deleteAll(siteToServiceList);
		
	}
private void deleteQuotePrice(Quote quote) {
		List<QuotePrice> quotePriceList = quotePriceRepository.findByQuoteId(quote.getId());
		if (!quotePriceList.isEmpty())
			quotePriceRepository.deleteAll(quotePriceList);

		List<OrderPrice> orderPriceList = orderPriceRepository.findByQuoteId(quote.getId());
		if (!orderPriceList.isEmpty())
			orderPriceRepository.deleteAll(orderPriceList);

	}

	private void deleteOrderRelatedDetails(Order order) {

		order.getOrderToLes().stream().forEach(orderToLe -> {
			orderToLe.getOrderToLeProductFamilies().stream().forEach(orderToLeProdFamily -> {
				orderToLeProdFamily.getOrderProductSolutions().stream().forEach(orderProdSolution -> {
					orderProdSolution.getOrderIllSites().stream().forEach(orderIllSite -> {
						List<OrderProductComponent> orderProductComponentList = orderProductComponentRepository
								.findByReferenceId(orderIllSite.getId());
						orderProductComponentList.stream().forEach(orderProdComponent -> {
							deleteOrderProductComponent(orderProdComponent);
						});
						deleteOrderFeasibilityDetails(orderIllSite);
						deleteOrderSiteToServiceDetails(orderIllSite);
						orderIllSitesRepository.delete(orderIllSite);
					});
					orderProductSolutionRepository.delete(orderProdSolution);
				});
				orderToLeProductFamilyRepository.delete(orderToLeProdFamily);
			});
			deleteOrderLeAttributeValues(orderToLe);
			orderToLeRepository.delete(orderToLe);
		});
		deleteOrderConfirmationAudits(order);
		orderRepository.delete(order);

	}

	
	private void deleteOrderSiteToServiceDetails(OrderIllSite orderIllSite) {
		List<OrderIllSiteToService> orderSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
		if(orderSiteToServiceList != null && !orderSiteToServiceList.isEmpty())
			orderIllSiteToServiceRepository.deleteAll(orderSiteToServiceList);
		
	}
	
	private void deleteOrderConfirmationAudits(Order order) {
		OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
				.findByOrderRefUuid(order.getOrderCode());
		if (orderConfirmationAudit != null) {
			orderConfirmationAuditRepository.delete(orderConfirmationAudit);
		}

		CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
		if (cofDetails != null)
			cofDetailsRepository.delete(cofDetails);

		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(order.getOrderCode());
		if (docusignAudit != null)
			docusignAuditRepository.delete(docusignAudit);
	}

	private void deleteOrderLeAttributeValues(OrderToLe orderToLe) {

		Set<OrdersLeAttributeValue> orderLeAttributeValueList = new HashSet<>(
				ordersLeAttributeValueRepository.findByOrderToLe(orderToLe));
		if (!orderLeAttributeValueList.isEmpty())
			ordersLeAttributeValueRepository.deleteAll(orderLeAttributeValueList);

		List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository.findByOrderToLe(orderToLe);
		if (!omsAttachmentsList.isEmpty()) {
			omsAttachmentRepository.deleteAll(omsAttachmentsList);
		}

	}

	private void deleteOrderFeasibilityDetails(OrderIllSite orderIllSite) {
		List<OrderSiteFeasibility> orderSiteFeasiblityList = orderSiteFeasibilityRepository
				.findByOrderIllSite(orderIllSite);
		if (!orderSiteFeasiblityList.isEmpty())
			orderSiteFeasibilityRepository.deleteAll(orderSiteFeasiblityList);

		List<OrderIllSiteSla> orderIllSiteSlaList = orderIllSiteSlaRepository.findByOrderIllSite(orderIllSite);
		if (!orderIllSiteSlaList.isEmpty())
			orderIllSiteSlaRepository.deleteAll(orderIllSiteSlaList);

		List<OrderSiteStatusAudit> orderSiteStatusAuditList = orderSiteStatusAuditRepository
				.findByOrderIllSite(orderIllSite);
		if (!orderSiteStatusAuditList.isEmpty()) {
			orderSiteStatusAuditList.stream().forEach(orderSiteStatusAudit -> {
				List<OrderSiteStageAudit> orderSiteStageAuditList = orderSiteStageAuditRepository
						.findByOrderSiteStatusAudit(orderSiteStatusAudit);
				if (!orderSiteStageAuditList.isEmpty())
					orderSiteStageAuditRepository.deleteAll(orderSiteStageAuditList);
			});
		}
		orderSiteStatusAuditRepository.deleteAll(orderSiteStatusAuditList);

	}

	private void deleteOrderProductComponent(OrderProductComponent orderProdComponent) {
		if (!orderProdComponent.getOrderProductComponentsAttributeValues().isEmpty()) {
			orderProdComponent.getOrderProductComponentsAttributeValues().stream().forEach(attri -> {
				OrderPrice quotePrice = orderPriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(attri.getId()), QuoteConstants.ATTRIBUTES.toString());
				orderPriceRepository.delete(quotePrice);
				orderProductComponentsAttributeValueRepository.delete(attri);
			});

			OrderPrice quotePrice = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderProdComponent.getId()), QuoteConstants.COMPONENTS.toString());
			orderPriceRepository.delete(quotePrice);

			orderProductComponentRepository.delete(orderProdComponent);
		}

	}

	public void deleteFeasibilityDetails(QuoteIllSite illSite) {
		List<SiteFeasibility> siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite(illSite);
		if (!siteFeasibilityList.isEmpty())
			siteFeasibilityRepository.deleteAll(siteFeasibilityList);

		List<PricingEngineResponse> pricingDetailList = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(illSite.getSiteCode(), "Discount");
		if (!pricingDetailList.isEmpty())
			pricingDetailsRepository.deleteAll(pricingDetailList);

		List<QuoteIllSiteSla> quoteIllSiteSlaList = quoteIllSiteSlaRepository.findByQuoteIllSite(illSite);
		if (!quoteIllSiteSlaList.isEmpty())
			quoteIllSiteSlaRepository.deleteAll(quoteIllSiteSlaList);

	}

	private void deleteQuoteLeAttributeValues(QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValueList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe);
		if (!quoteLeAttributeValueList.isEmpty())
			quoteLeAttributeValueRepository.deleteAll(quoteLeAttributeValueList);
		List<QuoteDelegation> quoteDelegationList = quoteDelegationRepository.findByQuoteToLe(quoteToLe);
		if (!quoteDelegationList.isEmpty())
			quoteDelegationRepository.deleteAll(quoteDelegationList);
	}

	public void deleteQuoteProductComponent(QuoteProductComponent quoteProdComponent) {
		if (!quoteProdComponent.getQuoteProductComponentsAttributeValues().isEmpty())
			quoteProdComponent.getQuoteProductComponentsAttributeValues().stream().forEach(attri -> {
				Optional.ofNullable(quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(attri.getId()), QuoteConstants.ATTRIBUTES.toString())).ifPresent(quoteprice -> {
							quotePriceRepository.delete(quoteprice);
						});
				quoteProductComponentsAttributeValueRepository.delete(attri);
			});
		Optional.ofNullable(quotePriceRepository.findByReferenceIdAndReferenceName(
				String.valueOf(quoteProdComponent.getId()), QuoteConstants.COMPONENTS.toString()))
				.ifPresent(quotePrice -> quotePriceRepository.delete(quotePrice));
		quoteProductComponentRepository.delete(quoteProdComponent);
	}

	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}");
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;
	}

	public void deleteProductFamily(Integer prodFamilyId) throws TclCommonException {
		try {

			Optional<QuoteToLeProductFamily> quoteToLeProductFamily = quoteToLeProductFamilyRepository
					.findById(prodFamilyId);
			if (quoteToLeProductFamily.isPresent()) {
				quoteToLeProductFamily.get().getProductSolutions().stream().forEach(prodSolution -> {
					prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
						List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
								.findByReferenceIdAndReferenceName(illSite.getId(),
										QuoteConstants.GVPN_SITES.toString());
						quoteProductComponentList.stream().forEach(quoteProdComponent -> {
							deleteQuoteProductComponent(quoteProdComponent);
						});
						deleteFeasibilityDetails(illSite);
						illSiteRepository.delete(illSite);
					});
					productSolutionRepository.delete(prodSolution);
				});
				quoteToLeProductFamilyRepository.delete(quoteToLeProductFamily.get());
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to map MSA documents with legal entity if MSA is not available for
	 * that legal entity
	 * 
	 * @param quoteToLe
	 *
	 * @throws TclCommonException
	 */
	private void uploadMSAIfNotPresent(QuoteToLe quoteToLe) throws TclCommonException {
		if (quoteToLe == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.MSA, CommonConstants.BACTIVE);
		mstOmsAttributes.forEach(mstOmsAttribute -> {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
				MSABean msaBean = constructMSABean(quoteToLe.getErfCusCustomerLegalEntityId());
				try {
					mqUtils.sendAndReceive(updateMSAQueue, Utils.convertObjectToJson(msaBean));
				} catch (TclCommonException e) {
					LOGGER.error("error in process update oms billing attribute", e);
				}
			}

		});
	}

	private MSABean constructMSABean(Integer customerLeId) {
		MSABean msaBean = new MSABean();
		msaBean.setCustomerLeId(customerLeId);
		msaBean.setDisplayName(LeAttributesConstants.MSA);
		msaBean.setIsMSAUploaded(true);
		msaBean.setName(LeAttributesConstants.MSA);
		msaBean.setProductName("GVPN");
		return msaBean;
	}

	/**
	 * Method to update billing attribute of customer in OMS
	 * 
	 * @param OmsLeAttributeBean
	 * @return OmsLeAttributeBean
	 * @throws TclCommonException
	 */
	public OmsLeAttributeBean updateLegalEntityBillingProperties(OmsLeAttributeBean omsLeAttributeBean)
			throws TclCommonException {

		try {
			if (omsLeAttributeBean != null) {
				User user = getUserId(omsLeAttributeBean.getUserName());
				if (user == null) {
					throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
				Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository
						.findById(omsLeAttributeBean.getQuoteToLeId());
				if (!optionalQuoteToLe.isPresent()) {
					throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
				}
				UpdateRequest updateRequest = new UpdateRequest();
				updateRequest.setAttributeName(omsLeAttributeBean.getAttrName());
				updateRequest.setAttributeValue(omsLeAttributeBean.getAttrValue());
				MstOmsAttribute omsAttribute = getMstAttributeMaster(updateRequest, user);
				constructQuoteLeAttribute(updateRequest, omsAttribute, optionalQuoteToLe.get());
			} else {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return omsLeAttributeBean;
	}

	/**
	 * 
	 * update Terms In Months For QuoteToLe
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param updateRequest
	 * @throws TclCommonException
	 */
	public void updateTermsInMonthsForQuoteToLe(Integer quoteId, Integer quoteToLeId, UpdateRequest updateRequest)
			throws TclCommonException {
		if (quoteId == null || quoteToLeId == null || updateRequest == null
				|| updateRequest.getTermInMonths() == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Optional<Quote> quote = quoteRepository.findById(quoteId);
		if (quote.isPresent()) {
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuoteAndId(quote.get(), quoteToLeId);
			if (quoteToLe != null && quoteToLe.getId() != null) {
				quoteToLe.setTermInMonths(updateRequest.getTermInMonths());
				quoteToLeRepository.save(quoteToLe);
			}
		}
	}

	/**
	 * Method to convert gvpn prices based on payment currency
	 * 
	 * @param quoteToLe
	 */
	public void convertGvpnPricesBasedOnPaymentCurrency(QuoteToLe quoteToLe) {
		try {
			String existingCurrency = GscConstants.GVPN_CURRENCY;
			existingCurrency = gscQuoteService.updateExistingCurrencyBasedOnService(quoteToLe, existingCurrency);
			String inputCurrency = findExistingCurrency(quoteToLe);
			Quote quote = quoteToLe.getQuote();
			/* updateQuoteToLeCurrencyValues(quote, inputCurrency, existingCurrency); */
			updateQuotePriceCurrencyValues(quote, inputCurrency, existingCurrency);
			updateQuoteIllSitesCurrencyValues(quoteToLe, inputCurrency, existingCurrency);

		} catch (Exception e) {
			LOGGER.warn("Error in converting the GVPN prices for GSC" + ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 
	 * getFeasiblityAndPricingDetailsForQuoteIllSites - get feasibility and pricing
	 * details based on site id
	 * 
	 * @param quoteIllSiteId
	 * @return
	 * @throws TclCommonException
	 */

	public QuoteIllSitesWithFeasiblityAndPricingBean getFeasiblityAndPricingDetailsForQuoteIllSites(
			Integer quoteIllSiteId) throws TclCommonException {
		QuoteIllSitesWithFeasiblityAndPricingBean quoteIllSiteBeans = new QuoteIllSitesWithFeasiblityAndPricingBean();
		try {
			Optional<QuoteIllSite> quoteIllSiteDetail = illSiteRepository.findById(quoteIllSiteId);
			if (quoteIllSiteDetail.isPresent()) {
				List<SiteFeasibility> feasiblityDetails = siteFeasibilityRepository
						.findByQuoteIllSite(quoteIllSiteDetail.get());
				List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingTypeNotIn(quoteIllSiteDetail.get().getSiteCode(), "Discount");
				quoteIllSiteBeans = constructQuoteIllSitesWithFeasiblityAndPricingDetails(quoteIllSiteDetail.get(),
						feasiblityDetails, pricingDetails);
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteIllSiteBeans;
	}

	/**
	 * 
	 * constructQuoteIllSitesWithFeasiblityAndPricingDetails -construct the
	 * feasibility and pricing beans based on input
	 * 
	 * @param quoteIllSite
	 * @param feasiblityDetails
	 * @param pricingDetails
	 * @return
	 */
	public QuoteIllSitesWithFeasiblityAndPricingBean constructQuoteIllSitesWithFeasiblityAndPricingDetails(
			QuoteIllSite quoteIllSite, List<SiteFeasibility> feasiblityDetails,
			List<PricingEngineResponse> pricingDetails) {
		QuoteIllSitesWithFeasiblityAndPricingBean quoteIllSiteBeans = new QuoteIllSitesWithFeasiblityAndPricingBean();
		quoteIllSiteBeans.setSiteId(quoteIllSite.getId());
		quoteIllSiteBeans.setSiteCode(quoteIllSite.getSiteCode());
		quoteIllSiteBeans.setIsFeasible(quoteIllSite.getFeasibility());
		quoteIllSiteBeans.setIsTaxExempted(quoteIllSite.getIsTaxExempted());
		quoteIllSiteBeans.setFeasiblityDetails(constructQuoteFeasiblityResponse(feasiblityDetails));
		quoteIllSiteBeans.setPricingDetails(constructPricingDetails(pricingDetails));
		return quoteIllSiteBeans;
	}

	/**
	 * 
	 * constructQuoteFeasiblityResponse -construct feasibility response based on
	 * inputs
	 * 
	 * @param feasiblityDetails
	 * @return
	 */

	public List<QuoteIllSitesFeasiblityBean> constructQuoteFeasiblityResponse(List<SiteFeasibility> feasiblityDetails) {
		List<QuoteIllSitesFeasiblityBean> feasiblityResponse = new ArrayList<>();
		feasiblityDetails.stream().forEach(feasiblity -> {
			QuoteIllSitesFeasiblityBean feasiblityBean = new QuoteIllSitesFeasiblityBean();
			feasiblityBean.setId(feasiblity.getId());
			feasiblityBean.setCreatedTime(feasiblity.getCreatedTime());
			feasiblityBean.setFeasibilityCheck(feasiblity.getFeasibilityCheck());
			feasiblityBean.setFeasibilityMode(feasiblity.getFeasibilityMode());
			feasiblityBean.setFeasibilityCode(feasiblity.getFeasibilityCode());
			feasiblityBean.setIsSelected(feasiblity.getIsSelected());
			feasiblityBean.setType(feasiblity.getType());
			feasiblityBean.setProvider(feasiblity.getProvider());
			feasiblityBean.setRank(feasiblity.getRank());
			feasiblityBean.setResponse(feasiblity.getResponseJson());
			feasiblityBean.setSiteId(feasiblity.getQuoteIllSite().getId());
			feasiblityBean.setFeasibilityType(feasiblity.getFeasibilityType());
			feasiblityBean.setSfdcFeasibilityId(feasiblity.getSfdcFeasibilityId());
			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}

	/*
	 * Contruct pricing response from pricingDetail entity
	 * 
	 * @author Anandhi Vijayaraghavan
	 * 
	 * @param List<PricingDetail>
	 * 
	 * @return List<Map<String,String>>
	 */
	public List<PricingDetailBean> constructPricingDetails(List<PricingEngineResponse> pricingDetails) {
		List<PricingDetailBean> pricingResponse = new ArrayList<>();
		pricingDetails.stream().forEach(pricing -> {
			PricingDetailBean pricingBean = new PricingDetailBean();
			pricingBean.setId(pricing.getId());
			pricingBean.setDateTime(pricing.getDateTime());
			pricingBean.setPriceMode(pricingBean.getPriceMode());
			pricingBean.setPricingType(pricing.getPricingType());
			pricingBean.setRequest(pricing.getRequestData());
			pricingBean.setResponse(pricing.getResponseData());
			pricingBean.setSiteCode(pricing.getSiteCode());
			pricingResponse.add(pricingBean);
		});
		return pricingResponse;
	}

	/**
	 * Method to get the currency for getquote page
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public String getQuoteCurrency(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		List<Integer> locationIds = new ArrayList<>();
		List<Integer> nonFeasibleLocationIds = new ArrayList<>();
		String currency = "INR";
		String locCommaSeparated = StringUtils.EMPTY;

		if (Objects.isNull(quoteLeId) && Objects.isNull(quoteId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
				return quoteToLeRepository.findById(quoteLeId).get().getCurrencyCode();
			} else {
				QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_IdAndMstProductFamily_Name(quoteLeId, "GVPN");
				List<ProductSolution> solutions = productSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProductFamily);

				solutions.forEach(sol -> {
					sol.getQuoteIllSites().forEach(quoteIllsite -> {
						if (quoteIllsite.getFeasibility() == CommonConstants.BACTIVE)
							locationIds.add(quoteIllsite.getErfLocSitebLocationId());
						else
							nonFeasibleLocationIds.add(quoteIllsite.getErfLocSitebLocationId());
					});
				});

				if (!locationIds.isEmpty()) {
					locCommaSeparated = locationIds.stream().map(i -> i.toString().trim())
							.collect(Collectors.joining(","));
					currency = getCurrencyBasedOnCountries(locCommaSeparated);
				} else {
					locCommaSeparated = nonFeasibleLocationIds.stream().map(i -> i.toString().trim())
							.collect(Collectors.joining(","));
					currency = getCurrencyBasedOnCountries(locCommaSeparated);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return currency;
	}

	public void updateCurrencyCodeInQuoteToLe(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLe.isPresent()) {
			quoteToLe.get().setCurrencyCode(getQuoteCurrency(quoteId, quoteLeId));
			quoteToLeRepository.save(quoteToLe.get());
		}

	}

	/*private String getCurrencyBasedOnCountries(String locationIds) throws TclCommonException {

		List<LocationDetail> locDetails = null;
		Set<String> countrySet = new HashSet<>();
		locDetails = Arrays.asList(getAddress(locationIds));
		countrySet = locDetails.stream().map(detail -> detail.getApiAddress().getCountry()).collect(Collectors.toSet());
		int count = 0;
		for (String country : countrySet) {
			if (!country.equals("India") && !country.equals("INDIA"))
				count++;
		}
		if (count > 0 && count!=0 && countrySet.contains("India")) {
			return "USD";
		} 
		if(count > 0 && count!=0 && countrySet.contains("INDIA")) {
			return "USD";
		}
		else {
			if (count > 0 && count!=0) {
				return "USD";
			} else {
				return "INR";
			}
		}
//		 return countrySet.contains("India") || countrySet.contains("INDIA") ? "INR" :
//		 "USD";

	}*/

	/**
	 * Get Currency Based on Countries
	 * @param locationIds
	 * @return
	 * @throws TclCommonException
	 */
	private String getCurrencyBasedOnCountries(String locationIds) throws TclCommonException {
		/*
		Business Logic:
		1. If the list contains only india, then currency will be in 'INR'
		2. Or else 'USD'
		 */
		List<LocationDetail> locDetails = Arrays.asList(getAddress(locationIds));
		Set<String> countries = locDetails.stream().map(detail -> detail.getApiAddress().getCountry()).collect(Collectors.toSet());
		Map<String, Integer> countriesCount = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		countries.stream().forEach(country -> {
			if (Objects.nonNull(countriesCount.get(country))) {
				countriesCount.put(country, countriesCount.get(country) + 1);
			} else {
				countriesCount.put(country, 1);
			}
		});

		if (!countriesCount.containsKey("India")) {
			return "USD";
		} else if (countriesCount.containsKey("India") && countriesCount.size() > 1) {
			return "USD";
		}

		return "INR";
	}

	/**
	 * @author Dinahar Vivekanandan getAddress
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	private LocationDetail[] getAddress(String locCommaSeparated) throws TclCommonException {
		try {
			String response = (String) mqUtils.sendAndReceive(locationDetailQueue, locCommaSeparated);
			LOGGER.info("Output Payload for location details {}", response);
			LocationDetail[] locDetails = (LocationDetail[]) Utils.convertJsonToObject(response,
					LocationDetail[].class);
			return locDetails;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Updates the legal entity properties
	 * 
	 * @param request
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public void updateLegalEntityPropertiesIsvQuote(List<UpdateRequest> request, Integer quoteToLeId)
			throws TclCommonException {
		try {
			request.stream().forEach(req -> {
				try {
					validateRequest2(req);
					User user = getUserId(Utils.getSource());
					if (user == null) {
						throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR,
								ResponseResource.R_CODE_ERROR);
					}
					Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(quoteToLeId);
					if (!optionalQuoteToLe.isPresent()) {
						throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
					}

					MstOmsAttribute omsAttribute = getMstAttributeMaster(req, user);
					constructLegaAttribute(omsAttribute, optionalQuoteToLe.get(), req.getAttributeName(),
							req.getAttributeValue());
				} catch (Exception e) {
					throw new TclCommonRuntimeException(e);
				}
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void validateRequest2(UpdateRequest request) throws TclCommonException {
		if (request.getQuoteId() == null || request.getAttributeName() == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}

	}

	/**
	 * 
	 * Update LCON properities against the site details
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param lconUpdateBeans
	 */
	public void updateLconProperities(Integer quoteId, Integer quoteToLeId, List<LconUpdateBean> lconUpdateBeans) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.get().getId());
			if (quoteToLeProductFamilies != null && !quoteToLeProductFamilies.isEmpty()) {
				quoteToLeProductFamilies.forEach(productFamily -> {
					MstProductComponent mstProductComponents = mstProductComponentRepository
							.findByName("SITE_PROPERTIES");
					List<ProductAttributeMaster> lconNameProductAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus("LCON_NAME", CommonConstants.BACTIVE);
					List<ProductAttributeMaster> lconContactProductAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus("LCON_CONTACT_NUMBER", CommonConstants.BACTIVE);
					// Added for MF -GVPN
					List<ProductAttributeMaster> lconRemarksProductAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus("LCON_REMARKS", CommonConstants.BACTIVE);
					
					if (mstProductComponents != null && lconContactProductAttributeMasters != null
							&& !lconContactProductAttributeMasters.isEmpty() && lconNameProductAttributeMasters != null
							&& !lconNameProductAttributeMasters.isEmpty() && 
							lconRemarksProductAttributeMasters != null && !lconRemarksProductAttributeMasters.isEmpty()) {
						if (lconUpdateBeans != null && !lconUpdateBeans.isEmpty()) {
							lconUpdateBeans.stream().forEach(lconUpdateBean -> {

								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponentAndMstProductFamily(
												lconUpdateBean.getSiteId(), mstProductComponents,
												productFamily.getMstProductFamily());
								if (quoteProductComponents != null && quoteProductComponents.isEmpty()) {
									QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
									quoteProductComponent.setMstProductComponent(mstProductComponents);
									quoteProductComponent.setReferenceId(lconUpdateBean.getSiteId());
									quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
									quoteProductComponent.setMstProductFamily(productFamily.getMstProductFamily());
									quoteProductComponent.setType("primary");
									quoteProductComponentRepository.save(quoteProductComponent);
								}
								quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponentAndMstProductFamily(
												lconUpdateBean.getSiteId(), mstProductComponents,
												productFamily.getMstProductFamily());
								if(Objects.nonNull(lconUpdateBean.getLconNumber()) && Objects.nonNull(lconUpdateBean.getLconName())) {
									LOGGER.info("Request manual feasibility when LconNumber - {}and LconName - {} is provided ", lconUpdateBean.getLconNumber(), lconUpdateBean.getLconName());
									if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
										LOGGER.info("Quote product component for manually updating request manual feasibility is {} ", quoteProductComponents.get(0));
										// LCON - Name
										List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
												.findByQuoteProductComponentAndProductAttributeMaster(
														quoteProductComponents.get(0),
														lconNameProductAttributeMasters.get(0));
										if (quoteProductComponentsAttributeValues != null
												&& quoteProductComponentsAttributeValues.isEmpty()) {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconName());
											quoteProductComponentsAttributeValue
													.setProductAttributeMaster(lconNameProductAttributeMasters.get(0));
											quoteProductComponentsAttributeValue
													.setQuoteProductComponent(quoteProductComponents.get(0));
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										} else {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
													.get(0);
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconName());
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										}
										// LCON - Number
										List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValuesContact = quoteProductComponentsAttributeValueRepository
												.findByQuoteProductComponentAndProductAttributeMaster(
														quoteProductComponents.get(0),
														lconContactProductAttributeMasters.get(0));
										if (quoteProductComponentsAttributeValuesContact != null
												&& quoteProductComponentsAttributeValuesContact.isEmpty()) {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconNumber());
											quoteProductComponentsAttributeValue
													.setProductAttributeMaster(lconContactProductAttributeMasters.get(0));
											quoteProductComponentsAttributeValue
													.setQuoteProductComponent(quoteProductComponents.get(0));
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										} else {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValuesContact
													.get(0);
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconNumber());
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										}
									
									
										//LCON Remarks - Added for MF -GVPN
										List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValuesRemarks = quoteProductComponentsAttributeValueRepository
												.findByQuoteProductComponentAndProductAttributeMaster(
														quoteProductComponents.get(0),
														lconRemarksProductAttributeMasters.get(0));
										if (quoteProductComponentsAttributeValuesRemarks != null
												&& quoteProductComponentsAttributeValuesRemarks.isEmpty()) {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconRemarks());
											quoteProductComponentsAttributeValue
													.setProductAttributeMaster(lconRemarksProductAttributeMasters.get(0));
											quoteProductComponentsAttributeValue
													.setQuoteProductComponent(quoteProductComponents.get(0));
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										} else {
											QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValuesRemarks
													.get(0);
											quoteProductComponentsAttributeValue
													.setAttributeValues(lconUpdateBean.getLconRemarks());
											quoteProductComponentsAttributeValueRepository
													.save(quoteProductComponentsAttributeValue);
										}
									}
								}
								else if (Objects.isNull(lconUpdateBean.getLconName()) && Objects.isNull(lconUpdateBean.getLconNumber())) {
									LOGGER.info("Requested manual feasibility for intl");
									List<ProductAttributeMaster> manualFeasibilityForIntlRequested = productAttributeMasterRepository
											.findByNameAndStatus(INTL_MANUAL_FEASIBILITY_REQUESTED, CommonConstants.BACTIVE);
									List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
											.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponents.get(0), manualFeasibilityForIntlRequested.get(0));
									if (quoteProductComponentsAttributeValues.isEmpty()) {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
										quoteProductComponentsAttributeValue.setAttributeValues("1");
										quoteProductComponentsAttributeValue.setProductAttributeMaster(manualFeasibilityForIntlRequested.get(0));
										quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponents.get(0));
										quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
										LOGGER.info("Manual feasible requested for siteid {} ", lconUpdateBean.getSiteId());
									} else {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues.get(0);
										quoteProductComponentsAttributeValue.setAttributeValues("1");
										quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
										LOGGER.info("Update value of Manual feasible requested for siteid {} to {} ", lconUpdateBean.getSiteId(), lconUpdateBean.getIsRequestManualFeasibilityTriggered());
									}
								}
								
								QuoteIllSite illSite = illSiteRepository.findByIdAndMfStatus(lconUpdateBean.getSiteId(), "Return");
								if(illSite != null) {
									illSite.setMfStatus(null);
									illSiteRepository.save(illSite);
								}

								try {
									LOGGER.info("Entering Update feasibility for request manual feasibility");
									omsSfdcService.updateFeasibility(quoteToLe.get(), lconUpdateBean.getSiteId());
									LOGGER.info("After Update feasibility for request manual feasibility");
								} catch (TclCommonException e) {
									LOGGER.error("Sfdc update feasibility request Failed ", e);
								}
							});
						}
					}
				});
			}
		}

	}
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public QuoteDetail approvedDocusignQuotes(String quoteuuId) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			Quote quote = quoteRepository.findByQuoteCode(quoteuuId);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			if (optionalQuoteToLe.isPresent() && Objects.nonNull(optionalQuoteToLe.get().getQuoteType())
					&& MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType())) {
				detail=gvpnMacdService.approvedMacdDocusignQuotes(quoteuuId);
			} else {
				Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

				if (order != null) {
					detail.setOrderId(order.getId());
				} else {
					order = constructOrder(quote, detail);
					if(checkO2cEnabled(order)) {
						order.setOrderToCashOrder(CommonConstants.BACTIVE);
						order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
						LOGGER.info("leaving approvedDocusignQuotes checkO2cEnabled o2corder {}, enabled {}", order.getOrderToCashOrder(), order.getIsOrderToCashEnabled());
						orderRepository.save(order);
					}
					/*
					 * if(checkO2cEnabledForOffnetWireless(order)) {
					 * order.setOrderToCashOrder(CommonConstants.BACTIVE); LOGGER.
					 * info("leaving approvedDocusignQuotes checkO2cEnabledForOffnetWireless o2corder {}, enabled {}"
					 * , order.getOrderToCashOrder(), order.getIsOrderToCashEnabled());
					 * orderRepository.save(order); }
					 */
					detail.setOrderId(order.getId());

					if (Objects.nonNull(quote.getQuoteCode())
							&& !quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
						// Trigger SFDC
						Order finalOrder = order;
						quote.getQuoteToLes().forEach(quoteLe -> {
							try {
								if (quoteLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
									omsSfdcService.processSiteDetailsRenewals(quoteLe); // renewals-sfdc
								} else {
									omsSfdcService.processSiteDetails(quoteLe);
								}
								Date cofSignedDate = new Date();
								DocusignAudit docusignAudit = docusignAuditRepository
										.findByOrderRefUuid(quote.getQuoteCode());
								if (docusignAudit != null && docusignAudit.getCustomerSignedDate() != null
										&& (docusignAudit.getStatus()
												.equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString())
												|| docusignAudit.getStatus()
														.equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString()))) {
									cofSignedDate = docusignAudit.getCustomerSignedDate();
								}
								omsSfdcService.processUpdateOpportunity(cofSignedDate, quoteLe.getTpsSfdcOptyId(),
										SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
								List<QuoteDelegation> quoteDelegate = quoteDelegationRepository
										.findByQuoteToLe(quoteLe);
								Map<String, String> cofObjectMapper = new HashMap<>();
								CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(finalOrder.getOrderCode());
								if (cofDetails != null) {
									cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
								}

								Integer userId = finalOrder.getCreatedBy();
								String userEmail = null;
								if (userId != null) {
									Optional<User> userDetails = userRepository.findById(userId);
									if (userDetails.isPresent()) {
										userEmail = userDetails.get().getEmailId();
									}
								}

								for (OrderToLe orderToLe : finalOrder.getOrderToLes()) {
									List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
											.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES,
													quote.getId(), AttachmentTypeConstants.COF.toString());
									for (OmsAttachment omsAttachment : omsAttachmentList) {
										omsAttachment.setOrderToLe(orderToLe);
										omsAttachment.setReferenceName(CommonConstants.ORDERS);
										omsAttachment.setReferenceId(finalOrder.getId());
										omsAttachmentRepository.save(omsAttachment);
									}
									gvpnQuotePdfService.downloadCofFromStorageContainer(null, null, finalOrder.getId(),
											orderToLe.getId(), cofObjectMapper);
									break;
								}
								processOrderMailNotification(finalOrder, quoteLe, cofObjectMapper, userEmail);
								quoteDelegate.forEach(quoteDelegation -> {
									quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
									quoteDelegationRepository.save(quoteDelegation);
								});
							} catch (TclCommonException e) {
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
										ResponseResource.R_CODE_ERROR);
							}
						});
					}
				}
				if (Objects.nonNull(quote.getQuoteCode())
						&& !quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
					quote.getQuoteToLes().forEach(quoteLe -> {
						if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
							quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
							quoteToLeRepository.save(quoteLe);
						}
					});
				}

				if (detail.isManualFeasible()) {
					cloneQuoteForNonFeasibileSite(quote);
				}
				if (o2cEnableFlag.equalsIgnoreCase("true")) {
					LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
					//processOrderFlatTable(order.getId());
				} else {
					LOGGER.info("Order flat table is disabled");
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public QuoteDetail approvedQuotesForHardFix(UpdateRequest request, String ipAddress) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				order = constructOrder(quote, detail);
				detail.setOrderId(order.getId());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
			}
			quote.getQuoteToLes().forEach(quoteLe -> {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}
			});

			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileSite(quote);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	/**
	 * Sets the site as not feasible for Gvpn
	 * 
	 * @author Kavya Singh
	 * @param quoteToLeId
	 * @param siteId
	 */
	@Transactional
	public void siteNotFeasibleGvpn(Integer quoteToLeId, Integer siteId) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.get().getId());
			if (quoteToLeProductFamilies != null && !quoteToLeProductFamilies.isEmpty()) {
				quoteToLeProductFamilies.forEach(quoteLeProductFamily -> {
					MstProductFamily mstProdFamily = quoteLeProductFamily.getMstProductFamily();
					MstProductComponent mstProductComponent = mstProductComponentRepository
							.findByName("SITE_PROPERTIES");

					if (mstProductComponent != null) {
						List<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository
								.findByReferenceIdAndMstProductComponentAndMstProductFamily(siteId, mstProductComponent,
										mstProdFamily);

						quoteProductComponent.stream().forEach(quoteCmp -> {

							List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
									.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteCmp.getId(),
											"IS_NOT_FEASIBLE");

							if (attributeValues != null && attributeValues.isEmpty()) {
								QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
								quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteCmp);
								quoteProductComponentsAttributeValue
										.setProductAttributeMaster(getAttributePropertiesMaster("IS_NOT_FEASIBLE"));
								quoteProductComponentsAttributeValue.setDisplayValue("true");
								quoteProductComponentsAttributeValue.setAttributeValues("true");
								quoteProductComponentsAttributeValueRepository
										.save(quoteProductComponentsAttributeValue);
							} else {

								attributeValues.stream().forEach(attr -> {
									attr.setAttributeValues("true");
									quoteProductComponentsAttributeValueRepository.save(attr);

								});
							}
						});
					}
				});
			}
		}

	}

	/**
	 * 
	 * This function is used to trigger the tax exemption mail when us sites is
	 * selected for tax exemption
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public String processTriggerTaxExemptionMailForUSSites(Integer quoteId, Integer quoteLeId, Integer customerLeId)
			throws TclCommonException {
		try {
			if (quoteId != null && quoteLeId != null) {
				Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
				if (quote != null) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
					if (quoteToLe.isPresent()) {
						String queueResponse = (String) mqUtils.sendAndReceive(customerLeAccountMangeQueue,
								customerLeId.toString());
						if (StringUtils.isNotBlank(queueResponse)) {
							CustomerLeAccountManagerDetails customerLeAccountManagerDetails = (CustomerLeAccountManagerDetails) Utils
									.convertJsonToObject(queueResponse, CustomerLeAccountManagerDetails.class);
							if (customerLeAccountManagerDetails != null) {
								String accountManagerEmail = customerLeAccountManagerDetails.getAccountManagerEmailId();
								String accountManagerName = customerLeAccountManagerDetails.getAccountManagerName();
								String customerEmail = null;
								User user = userRepository.findByIdAndStatus(quote.getCreatedBy(),
										CommonConstants.ACTIVE);
								if (user != null && user.getEmailId() != null) {
									customerEmail = user.getEmailId();
								}
								if (accountManagerEmail != null && accountManagerName != null
										&& quote.getQuoteCode() != null) {
									notificationService.taxExemptionNotificationforUSSites(customerEmail,
											accountManagerEmail, accountManagerName, quote.getQuoteCode(),
											quoteDashBoardRelativeUrl, CommonConstants.IAS);
								}
							}
						}

					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * Update partner location details using customer Le Id
	 *
	 * @param quoteLeId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean updatePartnerLeLocation(Integer quoteLeId, Integer customerLeId) throws TclCommonException {
		boolean flag = false;

		try {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLe.isPresent()) {
				QuoteToLe quoteToLeValue = quoteToLe.get();
				quoteToLeValue.setErfCusCustomerLegalEntityId(customerLeId);
				quoteToLeRepository.save(quoteToLeValue);
				processLocationDetailsAndSendToQueue(quoteToLeValue,
						quoteToLeValue.getQuote().getCustomer().getErfCusCustomerId());
				flag = true;
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return flag;
	}

	private void setQuoteTypesInQuoteToLeByLocationIds(Integer quoteId, QuoteToLe quoteToLe, User user)
			throws TclCommonException, IllegalArgumentException {
		List<Integer> locationIds = illSiteRepository.getLocationIdsByQuoteId(quoteId);
		List<String> locationIdsInString = new ArrayList<>();
		if (locationIds != null && !locationIds.isEmpty()) {
			LOGGER.info("Successfully got the location IDS {}", locationIds);
			locationIds.stream().forEach(locId -> {
				if (locId != null)
					locationIdsInString.add(locId.toString());
			});
		}
		if (locationIdsInString != null && !locationIdsInString.isEmpty()) {
			String request = String.join(",", locationIdsInString);
			LOGGER.info("Locations IDS {}", request);
			UpdateRequest req = new UpdateRequest();
			req.setAttributeName(CommonConstants.QUOTE_SITE_TYPE);
			req.setAttributeValue((String) mqUtils.sendAndReceive(siteTypeDetailsQueue, request));
			LOGGER.info("Update request {}", req);
			MstOmsAttribute omsAttribute = getMstAttributeMaster(req, user);
			constructLegaAttribute(omsAttribute, quoteToLe, req.getAttributeName(), req.getAttributeValue());
		}
	}

	/**
	 * 
	 * processValidateMandatoryAttr - validation method
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 */
	public CommonValidationResponse processValidateMandatoryAttr(Integer quoteId, Integer quoteLeId) {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		commonValidationResponse.setStatus(true);
		try {
			List<String> validationMessages = new ArrayList<>();
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLe.isPresent()) {
				boolean isMacd=false;
				if(MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
					isMacd=true;
				}
				processBillingValidation(quoteLeId, validationMessages);
				processContractingInfoValidation(quoteLeId, validationMessages,isMacd);
				String validationMessage = validationMessages.stream().collect(Collectors.joining(","));
				if (StringUtils.isNotBlank(validationMessage)) {
					commonValidationResponse.setStatus(false);
					commonValidationResponse.setValidationMessage(validationMessage);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in validating the mandatory Data", e);
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage("Data Error");
		}
		return commonValidationResponse;
	}

	/**
	 * 
	 * processBillingValidation - billing validation
	 * 
	 * @param quoteToLeId
	 * @param validationMessages
	 */
	private void processBillingValidation(Integer quoteToLeId, List<String> validationMessages) {

		List<Map<String, String>> billingCurrencyAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.BILLING_CURRENCY.toString());
		if (billingCurrencyAttr.isEmpty()) {
			validationMessages.add("Billing Currency is Mandatory");
		}
		List<Map<String, String>> paymentCurrencyAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.PAYMENT_CURRENCY.toString());
		if (paymentCurrencyAttr.isEmpty()) {
			validationMessages.add("Payment Currency is Mandatory");
		}
		List<Map<String, String>> billingInvMethodAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.INVOICE_METHOD.toString());
		if (billingInvMethodAttr.isEmpty()) {
			validationMessages.add("Invoice Method is Mandatory");
		}
		List<Map<String, String>> billingFreqAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.BILLING_FREQUENCY.toString());
		if (billingFreqAttr.isEmpty()) {
			validationMessages.add("Billing Frequency is Mandatory");
		}
		List<Map<String, String>> billingMethodAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.BILLING_METHOD.toString());
		if (billingMethodAttr.isEmpty()) {
			validationMessages.add("Billing Method is Mandatory");
		}
		List<Map<String, String>> billingTypeAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.BILLING_TYPE.toString());
		if (billingTypeAttr.isEmpty()) {
			validationMessages.add("Billing Type is Mandatory");
		}
		List<Map<String, String>> billingPayTermAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.PAYMENT_TERM.toString());
		if (billingPayTermAttr.isEmpty()) {
			validationMessages.add("Payment Term is Mandatory");
		}

		List<Map<String, String>> billingContactAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.BILLING_CONTACT_ID.toString());
		if (billingContactAttr.isEmpty()) {
			validationMessages.add("Billing Address is Mandatory");
		}

	}

	/**
	 * 
	 * processContractingInfoValidation- Contracting info validation
	 * 
	 * @param quoteToLeId
	 * @param validationMessages
	 */
	private void processContractingInfoValidation(Integer quoteToLeId, List<String> validationMessages,
			boolean isMacd) {

		List<Map<String, String>> contactNameAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.CONTACT_NAME.toString());
		if (contactNameAttr.isEmpty()) {
			validationMessages.add("Customer Contact is Mandatory");
		}
		List<Map<String, String>> contactNoAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.CONTACT_NO.toString());
		if (contactNoAttr.isEmpty()) {
			validationMessages.add("Customer Contact Number is Mandatory");
		}
		/*
		 * if (!isMacd) { List<Map<String, String>> gstleAttr =
		 * quoteLeAttributeValueRepository
		 * .findByQuoteToLeIdAndAttributeName(quoteToLeId,
		 * LeAttributesConstants.LE_STATE_GST_NO.toString()); if (gstleAttr.isEmpty()) {
		 * List<Map<String, String>> gstAttr = quoteLeAttributeValueRepository
		 * .findByQuoteToLeIdAndAttributeName(quoteToLeId,
		 * LeAttributesConstants.GST_NUMBER.toString()); if (gstAttr.isEmpty()) {
		 * validationMessages.add("GST Number is Mandatory"); } } }
		 */
		List<Map<String, String>> contactEmailAttr = quoteLeAttributeValueRepository
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, LeAttributesConstants.CONTACT_EMAIL.toString());
		if (contactEmailAttr.isEmpty()) {
			validationMessages.add("Customer Contact Email is Mandatory");
		}
		/*
		 * List<Map<String, String>> billingAddressAttr =
		 * quoteLeAttributeValueRepository
		 * .findByQuoteToLeIdAndAttributeName(quoteToLeId,
		 * LeAttributesConstants.BILLING_ADDRESS.toString()); if
		 * (billingAddressAttr.isEmpty()) {
		 * validationMessages.add("Customer Contact Address is Mandatory"); }
		 */
	}




	public String retriggerCreditCheck(Integer quoteId) throws TclCommonException{
		String[] creditCheckStatus = {null};
		String[] portalTransactionId = {null};
		String[] oldCreditControlStatus = {null};
		String productName = null;
		if(quoteId == null)
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION,
					ResponseResource.R_CODE_ERROR);

		List<QuoteToLe> quoteToLeList = quoteToLeRepository.findByQuote_Id(quoteId);
		if(Objects.nonNull(quoteToLeList) && !quoteToLeList.isEmpty()) {

			QuoteToLe quoteToLe = quoteToLeList.get(0);

			// quoteToLeList.stream().forEach(quoteToLe -> {

			if(Objects.nonNull(quoteToLe.getTpsSfdcStatusCreditControl()) && !quoteToLe.getTpsSfdcStatusCreditControl().equals(CommonConstants.POSITIVE)) {
				LOGGER.info("Retrigger credit check query , opty Id {}", quoteToLe.getTpsSfdcOptyId());
				oldCreditControlStatus[0] = quoteToLe.getTpsSfdcStatusCreditControl();
				if(Objects.nonNull(quoteToLe.getTpsSfdcOptyId())) {
					SfdcCreditCheckQueryRequest queryRequest = new SfdcCreditCheckQueryRequest();
					queryRequest.setFields(SFDCConstants.CREDIT_CHECK_QUERY_FIELDS);
					queryRequest.setObjectName(SFDCConstants.CREDIT_CHECK_OBJECT_NAME);
					queryRequest.setSourceSystem(SFDCConstants.OPTIMUS.toString());
					queryRequest.setWhereClause(SFDCConstants.CREDIT_CHECK_WHERE_CLAUSE + "'" + quoteToLe.getTpsSfdcOptyId() + "'");
					queryRequest.setTransactionId(SFDCConstants.OPTIMUS.toString() + quoteToLe.getQuote().getQuoteCode());


					try {
						String mqResponse = (String) mqUtils.sendAndReceive(creditCheckRetriggerQueue,
								Utils.convertObjectToJson(queryRequest));
						LOGGER.info("Response from service queue - {}", mqResponse);
						CreditCheckQueryResponseBean creditCheckResponse = Utils
								.convertJsonToObject(mqResponse, CreditCheckQueryResponseBean.class);
						creditCheckResponse.getSfdcCreditCheckQueryResponse().stream().forEach(entry -> {
							QuoteLeCreditCheckAudit creditCheckAudit = new QuoteLeCreditCheckAudit();
							creditCheckAudit.setCreatedTime(new Timestamp(new Date().getTime()));
							creditCheckAudit.setCreatedBy(Utils.getSource());
							creditCheckAudit.setQuoteToLe(quoteToLe);
							if(Objects.nonNull(entry.getMrcNrc()) && StringUtils.isNotBlank(entry.getMrcNrc())) {
								String[] mrcNrcValues = entry.getMrcNrc().split("/");
								creditCheckAudit.setTpsSfdcApprovedMrc(new Double(mrcNrcValues[0]));
								creditCheckAudit.setTpsSfdcApprovedNrc(new Double(mrcNrcValues[1]));
							} else {
								creditCheckAudit.setTpsSfdcApprovedMrc(entry.getProductServices().getRecord().stream().filter(rec -> Objects.nonNull(rec.getProductMRCc())).mapToDouble(rec-> new Double(rec.getProductMRCc())).sum());
								creditCheckAudit.setTpsSfdcApprovedNrc(entry.getProductServices().getRecord().stream().filter(rec -> Objects.nonNull(rec.getProductNRCc())).mapToDouble(rec-> new Double(rec.getProductNRCc())).sum());
							}
							creditCheckAudit.setTpsSfdcDifferentialMrc(entry.getDifferentialMRC());
							creditCheckAudit.setTpsSfdcApprovedBy(entry.getApprovedBy());
							creditCheckAudit.setTpsSfdcCreditCheckStatus(entry.getStatusOfCreditControl());
							creditCheckAudit.setTpsSfdcCustomerName(entry.getCustomerName());
							creditCheckAudit.setTpsSfdcCuId(entry.getCustomerContractingEntityBean().getCustomerCode());
							quoteLeCreditCheckRepository.save(creditCheckAudit);
							quoteToLe.setCreditCheckTriggered(CommonConstants.BDEACTIVATE);
							if(Objects.nonNull(entry.getDateOfCreditApproval())) {
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
								Date date;
								try {
									date = formatter.parse(entry.getDateOfCreditApproval());
								} catch (ParseException e) {
									LOGGER.info("Error in parsing date {}", e);
									throw new TclCommonRuntimeException(e);
								}

								quoteToLe.setTpsSfdcCreditApprovalDate(date);
							}
							//quoteToLe.setTpsSfdcCreditRemarks(entry.getCreditRemarks());
							quoteToLe.setTpsSfdcCreditRemarks(StringUtils.truncate(entry.getCreditRemarks(), 250));
							quoteToLe.setTpsSfdcDifferentialMrc(entry.getDifferentialMRC());
							quoteToLe.setTpsSfdcReservedBy(entry.getReservedBy());
							quoteToLe.setTpsSfdcStatusCreditControl(entry.getStatusOfCreditControl());
							quoteToLe.setTpsSfdcSecurityDepositAmount(entry.getSecurityDepositAmount());
							quoteToLeRepository.save(quoteToLe);
							portalTransactionId[0] = entry.getPortalTransactionId();
						});

					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.CREDITCHECK_RETRIGGER_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					}

				}

				if(!oldCreditControlStatus[0].equals(quoteToLe.getTpsSfdcStatusCreditControl())) {
					creditCheckStatus[0] = quoteToLe.getTpsSfdcStatusCreditControl();
					if(Objects.nonNull(portalTransactionId) && Objects.nonNull(portalTransactionId[0]))
						try {
							Optional<User> userSource = userRepository.findById(quoteToLeList.get(0).getQuote().getCreatedBy());
							if(userSource.isPresent()) {
								LOGGER.info("user type {}", userSource.get().getUserType() );
								if(quoteToLe.getTpsSfdcStatusCreditControl().equalsIgnoreCase(CommonConstants.POSITIVE) ||
										quoteToLe.getTpsSfdcStatusCreditControl().equalsIgnoreCase(CommonConstants.NEGATIVE) ||
										(quoteToLe.getTpsSfdcStatusCreditControl().equalsIgnoreCase(CommonConstants.RESERVED) && UserType.INTERNAL_USERS.toString().equalsIgnoreCase(userSource.get().getUserType())))
									creditCheckService.triggerCreditCheckStatusChangeMail(portalTransactionId[0]);
								if(quoteToLe.getTpsSfdcStatusCreditControl().equalsIgnoreCase(CommonConstants.POSITIVE) ||
										quoteToLe.getTpsSfdcStatusCreditControl().equalsIgnoreCase(CommonConstants.NEGATIVE)) {
/*								List<String> serviceStatusList = new ArrayList<>();
								serviceStatusList.add(SfdcServiceStatus.NEW.toString());
								serviceStatusList.add(SfdcServiceStatus.INPROGRESS.toString());
								List<ThirdPartyServiceJob> serviceJob = thirdPartyServiceJobRepository.findByRefIdAndServiceStatusInAndThirdPartySource(quoteToLe.getQuote().getQuoteCode(), serviceStatusList, ThirdPartySource.CREDITCHECK.toString());
								if(serviceJob != null && !serviceJob.isEmpty()) {
									serviceJob.get(0).setServiceStatus(SfdcServiceStatus.SUCCESS.toString());
									serviceJob.get(0).setResponsePayload(mqResponse[0]);
									thirdPartyServiceJobRepository.save(serviceJob.get(0));
								} */
								}
							}

						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.CREDITCHECK_RETRIGGER_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
						}
				}
				else
					creditCheckStatus[0] = quoteToLe.getTpsSfdcStatusCreditControl();
			}
			else if(Objects.nonNull(quoteToLe.getTpsSfdcStatusCreditControl()) && quoteToLe.getTpsSfdcStatusCreditControl().equals(CommonConstants.POSITIVE))
				creditCheckStatus[0] = CommonConstants.POSITIVE;
			else {
				QuoteToLeProductFamily quoteLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteToLe.getId());
				productName = quoteLeProductFamily.getMstProductFamily().getName()!=null?
						quoteLeProductFamily.getMstProductFamily().getName():"";

				LOGGER.info("Sending the customerLeIds as {}", quoteToLe.getErfCusCustomerLegalEntityId());
				LOGGER.info("MDC Filter token value in before Queue call creditCheckBasedOnPreapprovedValue {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String response = (String) mqUtils.sendAndReceive(customerLeCreditCheckQueue, String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()).concat(",").concat(productName));
				LOGGER.info("Response from customerLeCreditCheckQueue: " +response);
				if(response != null ) {
					CustomerLeVO customerLeDetails = (CustomerLeVO) Utils.convertJsonToObject(response,
							CustomerLeVO.class);
					if(customerLeDetails.getCreditPreapprovedFlag() == null)
						throw new TclCommonException(ExceptionConstants.CREDITCHECK_LE_PREAPPROVED_FLAG_ERROR,
								ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					if(CommonConstants.Y.equalsIgnoreCase(customerLeDetails.getBlacklistStatus())) {
						throw new TclCommonException(ExceptionConstants.BLACKLISTED_ACCOUNT_ERROR,
								ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					}

					if((Objects.nonNull(customerLeDetails.getPreapprovedPaymentTerm()))
							&& (Objects.nonNull(customerLeDetails.getPreapprovedBillingMethod()))) {
						if (Objects.isNull(customerLeDetails.getPreapprovedMrc())
								|| (Objects.isNull(customerLeDetails.getPreapprovedNrc()))
								|| (Objects.isNull(customerLeDetails.getPreapprovedPaymentTerm()))
								|| (Objects.isNull(customerLeDetails.getPreapprovedBillingMethod()))) {
							LOGGER.info("Some data is null for cuLe sfdc id - {}, sending for manual check by default", customerLeDetails.getAccountId());
							throw  new TclCommonException(ExceptionConstants.CREDITCHECK_DATA_INVALID, ResponseResource.R_CODE_ERROR);
						}
					} else if(Objects.isNull(customerLeDetails.getPreapprovedMrc())
							|| (Objects.isNull(customerLeDetails.getPreapprovedNrc()))) {
						LOGGER.info("Preapproved mrc/nrc is null for cuLe sfdc id - {}, sending for manual check by default", customerLeDetails.getAccountId());
						throw  new TclCommonException(ExceptionConstants.CREDITCHECK_DATA_INVALID, ResponseResource.R_CODE_ERROR);

					}

				}
			}

		}
		return creditCheckStatus[0];

	}
	
	
	/**
	 * 
	 * Method to get oppurtunity Details for GVPN
	 * @param quoteId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	public OpportunityBean getOpportunityDetails(Integer quoteId,Integer siteId) throws TclCommonException {
		LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch opportunity details for the quoteId {} ",quoteId);
		OpportunityBean opporBean = new OpportunityBean();
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			QuoteToLe quoteToLe = optionalQuoteToLe.get();
			opporBean.setProductName(quoteToLe.getQuoteToLeProductFamilies().stream().map(fam->fam.getMstProductFamily().getName()).findFirst().get());
			/*opporBean.setServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());*/
			if( Objects.nonNull(quoteToLe.getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
					&& Objects.nonNull(quoteToLe.getIsAmended()) && quoteToLe.getIsAmended()!=1)
			{
	//	    List<String> serviceIds=macdUtils.getServiceIds(quoteToLe);
		    List<String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSiteId(siteId,quoteToLe.getId());
				LOGGER.info("service ids for quote- {}", serviceIds);
			String serviceIdList=serviceIds.stream().findFirst().get();
			if(Objects.nonNull(quoteToLe.getIsMultiCircuit())&&CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())){
				serviceIds.remove(serviceIdList);
				serviceIds.forEach(serviceId -> {
					LOGGER.info("service id in loop {}", serviceIdList.toString());
					serviceIdList.concat("," + serviceId);
				});
			}
			LOGGER.info("service id list final {}", serviceIdList.toString());
			opporBean.setServiceId(serviceIdList);
			}
			opporBean.setOpportunityStage(SFDCConstants.PROPOSAL_SENT);
			opporBean.setOpportunityAccountName(quote.getCustomer().getCustomerName());
			Optional<User> user = userRepository.findById(quote.getCreatedBy());
			opporBean.setOpportunityOwnerEmail(user.get().getEmailId());
			opporBean.setOpportunityOwnerName(user.get().getUsername());
			List<SiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository.findByQuoteIllSite_IdAndType(siteId, "primary");
			Optional<SiteFeasibility> siteFeasibility = selectedSiteFeasibility.stream().findFirst();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(siteFeasibility.get().getResponseJson());
			opporBean.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.CUSTOMER_SEGMENT));
				
			QuoteProductComponent quoteProductComponent= quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId, "GVPN_SITES")
					.stream().filter(comp -> comp.getMstProductComponent().getName().equalsIgnoreCase("SITE_PROPERTIES")).findFirst().orElse(null);

			if(quoteProductComponent != null){
				
				LOGGER.info("Inside GVPNQuoteService.getOpportunityDetails to fetch site properties for the siteId {} ",siteId);
				Map<String, String> map = quoteProductComponent.getQuoteProductComponentsAttributeValues()
						.stream().filter(k->k.getAttributeValues() != null)
						.collect(Collectors.toMap(k -> k.getProductAttributeMaster().getName(),  QuoteProductComponentsAttributeValue::getAttributeValues));

				opporBean.setSiteContactName(map.getOrDefault("LCON_NAME", null));
				opporBean.setSiteLocalContactNumber(map.getOrDefault("LCON_CONTACT_NUMBER", null));
				if(map.containsKey("LCON_REMARKS"))
				opporBean.setSalesRemarks(map.getOrDefault("LCON_REMARKS", null));
			}
			String response = thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(
					quote.getQuoteCode(), SfdcServiceTypeConstants.UPDATE_OPPORTUNITY, ThirdPartySource.SFDC.toString(), "SUCCESS")
					.stream().findFirst().map(ThirdPartyServiceJob::getResponsePayload).orElse(StringUtils.EMPTY);

			if(response != null && !response.isEmpty()) {
				LOGGER.info("Inside GVPNQuoteService.getOpportunityDetails to fetch opportunity stage");
				ThirdPartyResponseBean thirdPartyResponse = (ThirdPartyResponseBean) Utils.convertJsonToObject(response, ThirdPartyResponseBean.class);
				opporBean.setOpportunityStage(thirdPartyResponse.getOpportunity().getStageName());
			}

			opporBean.setOppotunityId(quoteToLe.getTpsSfdcOptyId());
			
			List<QuoteLeAttributeValue> quoteLeAttributeValueLegalEntity = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe,
							LeAttributesConstants.LE_NAME.toString());
			if (quoteLeAttributeValueLegalEntity != null && !quoteLeAttributeValueLegalEntity.isEmpty())
				opporBean.setOpportunityOwnerName(quoteLeAttributeValueLegalEntity.get(0).getAttributeValue());

			List<QuoteLeAttributeValue> quoteLeAttributeValueLeOwnerEmail = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe,
							LeAttributesConstants.LE_EMAIL.toString());
			if (quoteLeAttributeValueLeOwnerEmail != null && !quoteLeAttributeValueLeOwnerEmail.isEmpty())
				opporBean.setOpportunityOwnerEmail(quoteLeAttributeValueLeOwnerEmail.get(0).getAttributeValue());
		} catch(Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		
		return opporBean;
	}
	
	private String setAccountOwnerName(String partnerId) throws TclCommonException {
		String accountOwnerName = "";
		try {
			String accountOwnerNameAndEmail = (String) mqUtils.sendAndReceive(partnerAccountNameMQ, partnerId);
			accountOwnerName = accountOwnerNameAndEmail.split(COMMA)[0].trim();
		}catch(TclCommonException e){
			LOGGER.warn("Error Occoured while fetching account owner name for partner id :: {} and error is :: {}", partnerId, e.getStackTrace());
		}
		return accountOwnerName;
	}
	
	/**
	 * This method is to save OrderIllSiteToService
	 * @param illSite
	 * @param orderSite
	 * @throws TclCommonException
	 */
	private void constructOrderIllSiteToService(QuoteIllSite illSite, OrderIllSite orderSite,List<QuoteIllSiteToService> quoteIllSiteServices) {
		try {
			String[] nsQuote = {null};
			LOGGER.info("Inside GvpnQuoteService.constructOrderIllSiteToService to save orderIllSiteService for orderSiteId {} ", orderSite.getId());
			List<OrderIllSiteToService> orderIllSiteToServices = new ArrayList<>();
			Quote quote = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote();
			if(quote != null) {
				nsQuote[0] = quote.getNsQuote();
			}
			LOGGER.info("NS Quote {}", nsQuote[0]);
			if(!quoteIllSiteServices.isEmpty()) {
				quoteIllSiteServices.stream().forEach(quoteSiteService->{
					LOGGER.info("Setting orderIllSiteTOService for siteId  {} and QuoteLe  {}", quoteSiteService.getQuoteIllSite().getId(), quoteSiteService.getQuoteToLe().getId());
					OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
					orderIllSiteToService.setErfServiceInventoryParentOrderId(quoteSiteService.getErfServiceInventoryParentOrderId());
					orderIllSiteToService.setErfServiceInventoryServiceDetailId(quoteSiteService.getErfServiceInventoryServiceDetailId());
					orderIllSiteToService.setErfServiceInventoryTpsServiceId(quoteSiteService.getErfServiceInventoryTpsServiceId());
					orderIllSiteToService.setOrderIllSite(orderSite);
					orderIllSiteToService.setTpsSfdcParentOptyId(quoteSiteService.getTpsSfdcParentOptyId());
					orderIllSiteToService.setType(quoteSiteService.getType());
					orderIllSiteToService.setAllowAmendment(quoteSiteService.getAllowAmendment());
					if(Objects.nonNull(nsQuote[0]) && CommonConstants.Y.equalsIgnoreCase(nsQuote[0])) {
						List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(illSite.getId(), 
								IllSitePropertiesConstants.SITE_PROPERTIES.toString(), QuoteConstants.GVPN_SITES.toString());
						quoteProductComponentList.stream().filter(prodCom -> (quoteSiteService.getType().equalsIgnoreCase(prodCom.getType()))).forEach(prodComponent -> {
							LOGGER.info("Entering prod component list loop");
							Optional<QuoteProductComponentsAttributeValue> attValue = prodComponent.getQuoteProductComponentsAttributeValues().stream().
							filter(prodCompAttValue -> IllSitePropertiesConstants.SFDC_ORDER_TYPE.toString().equalsIgnoreCase(
									prodCompAttValue.getProductAttributeMaster().getName())).findFirst();
							
							if(attValue.isPresent()) {
								LOGGER.info("attValue for site properties {} ", attValue.get().getAttributeValues());
								orderIllSiteToService.setErfSfdcOrderType(attValue.get().getAttributeValues());
								orderIllSiteToService.setErfSfdcSubType(attValue.get().getAttributeValues());
							}
								
						});
					} else {
					orderIllSiteToService.setErfSfdcOrderType(quoteSiteService.getErfSfdcOrderType());
					orderIllSiteToService.setErfSfdcSubType(quoteSiteService.getErfSfdcSubType());
					}
					if(Objects.nonNull(quoteSiteService.getQuoteIllSite().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getIsAmended())) {
						int result = Byte.compare(BACTIVE, quoteSiteService.getQuoteIllSite().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getIsAmended());
						if (result==0) {
							orderIllSiteToService.setAllowAmendment(quoteSiteService.getAllowAmendment());
							orderIllSiteToService.setParentSiteId(Objects.nonNull(quoteSiteService.getParentSiteId()) ? quoteSiteService.getParentSiteId() : -1);
							orderIllSiteToService.setParentOrderId(Objects.nonNull(quoteSiteService.getParentOrderId()) ? quoteSiteService.getParentOrderId() : -1);
							orderIllSiteToService.setServiceType(Objects.nonNull(quoteSiteService.getServiceType()) ? quoteSiteService.getServiceType() : "NA");
							orderIllSiteToService.setO2cServiceId(Objects.nonNull(quoteSiteService.getO2cServiceId()) ? quoteSiteService.getO2cServiceId() : "NA");
						}
					}

					orderIllSiteToService.setOrderToLe(orderSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe());
					
						String iasQueueResponse = null;
						try {
							iasQueueResponse = (String) mqUtils.sendAndReceive(siRelatedDetailsQueue, quoteSiteService.getErfServiceInventoryTpsServiceId());
						
						if (StringUtils.isNotBlank(iasQueueResponse)) {
							LOGGER.info("queue response from si in constructOrderIllSiteToService {}", iasQueueResponse);
							SIServiceInfoBean[] siDetailedInfoResponseIAS = (SIServiceInfoBean[]) Utils.convertJsonToObject(iasQueueResponse,
									SIServiceInfoBean[].class);
							List<SIServiceInfoBean> siServiceInfoResponse = Arrays.asList(siDetailedInfoResponseIAS);
							Optional<SIServiceInfoBean> siServiceInfoBeanOne = siServiceInfoResponse.stream().filter(service -> service.getTpsServiceId().equals(quoteSiteService.getErfServiceInventoryTpsServiceId())).findFirst();
							orderIllSiteToService.setErfServiceInventoryO2cLinkType(siServiceInfoBeanOne.get().getPrimaryOrSecondary());
							String quoteCategory = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteCategory();
							if(Objects.nonNull(siServiceInfoResponse) && siServiceInfoResponse.size() > 1) {
								LOGGER.info("more than one record in response from queue");
								Optional<SIServiceInfoBean> siServiceInfoBean = siServiceInfoResponse.stream().filter(service -> !service.getTpsServiceId().equals(quoteSiteService.getErfServiceInventoryTpsServiceId())).findFirst();
								if(siServiceInfoBean.isPresent()) {
									orderIllSiteToService.setErfServiceInventoryPriSecLinkServiceId(siServiceInfoBean.get().getTpsServiceId());
									Optional<QuoteIllSiteToService> qSiteToService = quoteIllSiteServices.stream().filter(siteToService -> siteToService.getErfServiceInventoryTpsServiceId().equalsIgnoreCase(siServiceInfoBean.get().getTpsServiceId())).findAny();
									String macdChangeBandwidhFlag = illSite.getMacdChangeBandwidthFlag();
									LOGGER.info("quote Category {}, macdChangeBandwidthFlag {}", quoteCategory, macdChangeBandwidhFlag);
										if (!qSiteToService.isPresent() &&  ((MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteCategory) 
												&& MACDConstants.BOTH_STRING.equalsIgnoreCase(macdChangeBandwidhFlag)) 
												|| MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteCategory))
												&& (Objects.nonNull(nsQuote[0]) && CommonConstants.N.equalsIgnoreCase(nsQuote[0]))) {
											LOGGER.info("Service Id {}, primary or secondary {}",
													siServiceInfoBean.get().getTpsServiceId(),
													siServiceInfoBean.get().getPrimaryOrSecondary());
											OrderIllSiteToService relatedServiceDetail = new OrderIllSiteToService();
											relatedServiceDetail.setErfServiceInventoryParentOrderId(
													siServiceInfoBean.get().getSiOrderId());
											relatedServiceDetail.setErfServiceInventoryPriSecLinkServiceId(
													orderIllSiteToService.getErfServiceInventoryTpsServiceId());
											relatedServiceDetail.setErfServiceInventoryServiceDetailId(
													siServiceInfoBean.get().getId());
											relatedServiceDetail.setErfServiceInventoryTpsServiceId(
													siServiceInfoBean.get().getTpsServiceId());
											relatedServiceDetail.setOrderIllSite(orderSite);
											relatedServiceDetail.setTpsSfdcParentOptyId(
													siServiceInfoBean.get().getTpsSfdcParentOptyId());
											relatedServiceDetail.setErfServiceInventoryO2cLinkType(
													siServiceInfoBean.get().getPrimaryOrSecondary());
											if (MACDConstants.DUAL_PRIMARY
													.equalsIgnoreCase(siServiceInfoBean.get().getPrimaryOrSecondary())
													|| MACDConstants.SINGLE_ALL_CAPS.equalsIgnoreCase(
															siServiceInfoBean.get().getPrimaryOrSecondary())) {

												relatedServiceDetail.setType(PDFConstants.PRIMARY);
											}
											if (MACDConstants.DUAL_SECONDARY
													.equalsIgnoreCase(siServiceInfoBean.get().getPrimaryOrSecondary()))
												relatedServiceDetail.setType(PDFConstants.SECONDARY);

											relatedServiceDetail
													.setAllowAmendment(quoteSiteService.getAllowAmendment());
											relatedServiceDetail
													.setErfSfdcOrderType(orderIllSiteToService.getErfSfdcOrderType());
											relatedServiceDetail
													.setErfSfdcSubType(orderIllSiteToService.getErfSfdcSubType());

											relatedServiceDetail.setOrderToLe(orderSite.getOrderProductSolution()
													.getOrderToLeProductFamily().getOrderToLe());
											if (Objects.nonNull(quoteSiteService.getQuoteIllSite().getProductSolution()
													.getQuoteToLeProductFamily().getQuoteToLe().getIsAmended())) {
												int result = Byte.compare(BACTIVE,
														quoteSiteService.getQuoteIllSite().getProductSolution()
																.getQuoteToLeProductFamily().getQuoteToLe()
																.getIsAmended());
												if (result == 0) {
													relatedServiceDetail
															.setAllowAmendment(quoteSiteService.getAllowAmendment());
													relatedServiceDetail.setParentSiteId(
															Objects.nonNull(quoteSiteService.getParentSiteId())
																	? quoteSiteService.getParentSiteId()
																	: -1);
													relatedServiceDetail.setParentOrderId(
															Objects.nonNull(quoteSiteService.getParentOrderId())
																	? quoteSiteService.getParentOrderId()
																	: -1);
													relatedServiceDetail.setServiceType(
															Objects.nonNull(quoteSiteService.getServiceType())
																	? quoteSiteService.getServiceType()
																	: "NA");
													relatedServiceDetail.setO2cServiceId(
															Objects.nonNull(quoteSiteService.getO2cServiceId())
																	? quoteSiteService.getO2cServiceId()
																	: "NA");
												}
											}
											orderIllSiteToServices.add(relatedServiceDetail);
										}
								}
								
							}
							
							if(Objects.nonNull(siServiceInfoResponse) && MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteCategory)) {
								String[] offeringName = {null};
								try {
									JSONParser jsonParser = new JSONParser();
									JSONObject jsonObj = (JSONObject) jsonParser
											.parse(illSite.getProductSolution().getProductProfileData());
									offeringName[0] = (String) jsonObj.get(MACDConstants.OFFERING_NAME);
								} catch (org.json.simple.parser.ParseException e) {
									LOGGER.info("Exception {}", e.getMessage());
									throw new TclCommonRuntimeException(e);
								}
								if(offeringName[0] != null && offeringName[0].toLowerCase().contains(MACDConstants.DUAL_SMALL_CASE)) {
									LOGGER.info("Add Site Scenario :: Service Id {}, primary or secondary {}",
											orderIllSiteToService.getErfServiceInventoryTpsServiceId(),
											orderIllSiteToService.getType());
									OrderIllSiteToService relatedServiceDetail = new OrderIllSiteToService();
									relatedServiceDetail.setErfServiceInventoryParentOrderId(
											orderIllSiteToService.getErfServiceInventoryParentOrderId());
									relatedServiceDetail.setErfServiceInventoryPriSecLinkServiceId(orderIllSiteToService.getErfServiceInventoryPriSecLinkServiceId());
									relatedServiceDetail.setErfServiceInventoryServiceDetailId(
											orderIllSiteToService.getErfServiceInventoryServiceDetailId());
									relatedServiceDetail.setErfServiceInventoryTpsServiceId(
											orderIllSiteToService.getErfServiceInventoryTpsServiceId());
									relatedServiceDetail.setOrderIllSite(orderSite);
									relatedServiceDetail.setTpsSfdcParentOptyId(
											orderIllSiteToService.getTpsSfdcParentOptyId());
									
										relatedServiceDetail.setErfServiceInventoryO2cLinkType(
											orderIllSiteToService.getErfServiceInventoryO2cLinkType());
										if(PDFConstants.PRIMARY.equalsIgnoreCase(orderIllSiteToService.getType())) {
											relatedServiceDetail.setType(PDFConstants.SECONDARY);
										}
										if (PDFConstants.SECONDARY
											.equalsIgnoreCase(orderIllSiteToService.getType()))
										relatedServiceDetail.setType(PDFConstants.PRIMARY);

									relatedServiceDetail
											.setAllowAmendment(quoteSiteService.getAllowAmendment());
									relatedServiceDetail
											.setErfSfdcOrderType(orderIllSiteToService.getErfSfdcOrderType());
									relatedServiceDetail
											.setErfSfdcSubType(orderIllSiteToService.getErfSfdcSubType());

									relatedServiceDetail.setOrderToLe(orderSite.getOrderProductSolution()
											.getOrderToLeProductFamily().getOrderToLe());
									if (Objects.nonNull(quoteSiteService.getQuoteIllSite().getProductSolution()
											.getQuoteToLeProductFamily().getQuoteToLe().getIsAmended())) {
										int result = Byte.compare(BACTIVE,
												quoteSiteService.getQuoteIllSite().getProductSolution()
														.getQuoteToLeProductFamily().getQuoteToLe()
														.getIsAmended());
										if (result == 0) {
											relatedServiceDetail
													.setAllowAmendment(quoteSiteService.getAllowAmendment());
											relatedServiceDetail.setParentSiteId(
													Objects.nonNull(quoteSiteService.getParentSiteId())
															? quoteSiteService.getParentSiteId()
															: -1);
											relatedServiceDetail.setParentOrderId(
													Objects.nonNull(quoteSiteService.getParentOrderId())
															? quoteSiteService.getParentOrderId()
															: -1);
											relatedServiceDetail.setServiceType(
													Objects.nonNull(quoteSiteService.getServiceType())
															? quoteSiteService.getServiceType()
															: "NA");
											relatedServiceDetail.setO2cServiceId(
													Objects.nonNull(quoteSiteService.getO2cServiceId())
															? quoteSiteService.getO2cServiceId()
															: "NA");
										}
									}
									orderIllSiteToServices.add(relatedServiceDetail);
								}
								}
							
					}
						
					} catch (Exception e) {
						LOGGER.error("error in queue call siRelatedDetailsQueue in constructOrderIllSiteToService {}", e);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
						
					
					orderIllSiteToServices.add(orderIllSiteToService);
				});
				orderIllSiteToServiceRepository.saveAll(orderIllSiteToServices);
				LOGGER.info("Inside GvpnQuoteService.constructOrderIllSiteToService Saved orderillSiteToService ");
			}
		} catch(Exception e) {
			LOGGER.error("Exception occured while saving orderIllSiteToServices {} ", e);
		}
		
		
	}

	/**
	 * Updated the price in  table against attributes
	 *
	 * @param quoteToLe
	 * @param component
	 * @param burMBPrice
	 */
	private void processAttributePrice(QuoteToLe quoteToLe,
									   QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue, Double burMBPNrcPrice,Double ArcPrice,
									   MstProductFamily mstProductFamily,Double Mrc) {
		QuotePrice attrPrice;
		attrPrice = new QuotePrice();
		attrPrice.setQuoteId(quoteToLe.getQuote().getId());
		attrPrice.setReferenceId(String.valueOf(quoteProductComponentsAttributeValue.getId()));
		attrPrice.setReferenceName(QuoteConstants.ATTRIBUTES.toString());
		attrPrice.setEffectiveNrc(burMBPNrcPrice);
		attrPrice.setEffectiveArc(ArcPrice);
		attrPrice.setEffectiveMrc(Mrc);
		attrPrice.setMstProductFamily(mstProductFamily);
		quotePriceRepository.save(attrPrice);
	}
	
	
	public CommonValidationResponse processValidate(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		commonValidationResponse.setStatus(true);
		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = getQuoteDetails(quoteId, null, false, null);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			Map<String, Object> variable = gvpnQuotePdfService.getCofAttributes(true, quoteDetail,true,quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
					|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for GVPN is {}", Utils.convertObjectToJson(variable));
				commonValidationResponse = gvpnCofValidatorService.processCofValidation(variable,
						"GVPN", quoteToLe.get().getQuoteType());
				checkFeasibilityValidityPeriod(quoteToLe, commonValidationResponse);
			}
		} catch (Exception e) {
			LOGGER.error("Error in validating the mandatory Data", e);
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage("Data Error");
		}
		return commonValidationResponse;
	}
	
	public OpportunityBean retrievePriSecSIDsForMFOppurtunity(OpportunityBean opBean, Integer quoteId, Integer siteId)
			throws TclCommonException {

		Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(siteId);
		QuoteToLe quoteToLe = null;
		if (quoteIllSite.isPresent()) {
			quoteToLe = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		}

		if (Objects.nonNull(quoteToLe) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
				&& Objects.nonNull(opBean.getServiceId())) {
			String secondaryServiceId = null;
			Boolean isSecondary = false;
			SIServiceDetailDataBean sIServiceDetailDataBean = macdUtils.getServiceDetail(opBean.getServiceId(),
					quoteToLe.getQuoteCategory());
			isSecondary = Objects.nonNull(sIServiceDetailDataBean.getPriSecServLink());
			String linkType = sIServiceDetailDataBean.getLinkType();

			if (linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SINGLE")) {
				if (isSecondary) {
					secondaryServiceId = sIServiceDetailDataBean.getPriSecServLink();
					LOGGER.info("Primary service Id is ----> {} and Secondary service ID is -----> {} ",
							opBean.getServiceId(), secondaryServiceId);
					opBean.setSecondaryServiceId(secondaryServiceId);
					opBean.setPrimaryServiceId(sIServiceDetailDataBean.getTpsServiceId());
				}else    // PT-2138 : set primaryServiceId for Single/Primary linkType
					opBean.setPrimaryServiceId(sIServiceDetailDataBean.getTpsServiceId());
			}

			else if (isSecondary && linkType.equalsIgnoreCase("SECONDARY")) {
				secondaryServiceId = sIServiceDetailDataBean.getTpsServiceId();
				LOGGER.info("Secondary service ID is -----> {}  and primary service Id is ----> {} ",
						secondaryServiceId, opBean.getServiceId());
				opBean.setSecondaryServiceId(secondaryServiceId);
				opBean.setPrimaryServiceId(sIServiceDetailDataBean.getPriSecServLink());

			}
		}
		return opBean;
	}
	

	//added for multi vrf
	
	/**
	 * 
	 * 
	 * updateVrfSiteInfo - This method is used to update the vrf  site Informations
	 * 
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @param quoteId
	 * @return QuoteResponse
	 * @throws TclCommonException
	 */
	@Transactional
	public void updateVrfSiteInfo(UpdateRequest request, Integer erfCustomerId, Integer quoteId,Integer siteId)
			throws TclCommonException {

		try {
			LOGGER.info("Entered into updateVrfSiteInfo" + erfCustomerId + "quoteId" + quoteId + "siteId" + siteId + "isNoOfMultiVrfChanged" + request.getIsNoOfMultiVrfChanged());
			validateVrfSiteInformation(request, siteId, quoteId);
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
			MstProductFamily productFamily = getProductFamily(request.getFamilyName());
			User user = getUserId(Utils.getSource());
			if(request.getIsNoOfMultiVrfChanged()) {
				LOGGER.info("Number of VRFs changed by the user so removing previous VRF sites for site ID " + siteId);
				removeExistingMultiVrfProductComponent(quoteIllSite,productFamily);
				LOGGER.info("Removed VRFs and updating the new VRFs given by user");
			}
			for (ComponentDetail componentDetail : request.getComponentDetails()) {
				LOGGER.info("component name" + componentDetail.getName());
				if (componentDetail.getName().equalsIgnoreCase(CommonConstants.VRF_COMMON)) {
					for (AttributeDetail attributeDetail : componentDetail.getAttributes()) {
						updateVrfCommonProductAttribute(request, componentDetail, attributeDetail, productFamily);
					}
				} else {
					processMultiVrfProductComponent(productFamily, quoteIllSite, componentDetail, user);
				}
			}

		} catch (Exception e) {
			LOGGER.warn("Error in Update vrf Site info {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
	
	/**
	 * This method is used for validating the  vrf site information
	 * validateSiteInformation
	 * 
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	protected void validateVrfSiteInformation(UpdateRequest request, Integer siteId, Integer quoteId)
			throws TclCommonException {
		if ((request == null) || (siteId == null) || (quoteId == null) || (request.getComponentDetails() == null)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}
	
	/**
	 * This method is used updateVrfCommonProductAttribute
	 * @param UpdateRequest
	 * @throws TclCommonException
	 */
	public void updateVrfCommonProductAttribute(UpdateRequest request, ComponentDetail cmpDetail,
			AttributeDetail attributeDetail, MstProductFamily prodFamily) throws TclCommonException{
		LOGGER.info("entered into updateVrfCommonProductAttribute" + cmpDetail.getName()+"type"+cmpDetail.getType());
	 try {
		QuoteProductComponent quoteProductComponent = null;
		List<QuoteProductComponent> prodComponent=null;
		Optional<QuoteIllSite> siteEntity = illSiteRepository.findById(request.getSiteId());
		if (siteEntity.isPresent()) {
			List<ProductAttributeMaster> mstAttributeMaster = productAttributeMasterRepository
					.findByNameAndStatus(attributeDetail.getName(), CommonConstants.BACTIVE);
			ProductAttributeMaster productAttributeMaster = null;
			if (mstAttributeMaster.isEmpty()) {
				productAttributeMaster = new ProductAttributeMaster();
				productAttributeMaster.setName(attributeDetail.getName());
				productAttributeMaster.setStatus(CommonConstants.BACTIVE);
				productAttributeMaster.setDescription(attributeDetail.getName());
				productAttributeMaster.setCreatedBy(Utils.getSource());
				productAttributeMaster.setCreatedTime(new Date());
				productAttributeMasterRepository.save(productAttributeMaster);
			} else {
				productAttributeMaster = mstAttributeMaster.get(0);
			}
			List<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findByNameAndStatus(cmpDetail.getName(), (byte) 1);
			if (!mstProductComponent.isEmpty()) {
				 LOGGER.info("mst product component" + mstProductComponent.get(0).getName());
				 prodComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(siteEntity.get().getId(),
								mstProductComponent.get(0), prodFamily, cmpDetail.getType());
			}

			if (prodComponent != null) {
				quoteProductComponent = prodComponent.get(0);
			}
			if (quoteProductComponent != null) {
				List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent,
								productAttributeMaster);
				if (quoteProductComponentsAttributeValues.isEmpty()) {
					QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
					quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
					quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
					quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
					quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
					LOGGER.info("-------created the attributes---new-------");
				} else {
					QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
							.get(0);
					quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
					quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
					LOGGER.info("-------Updated the attribute---old-----");
				}

			}
		  }
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
	
	/**
	 * processProductComponent Vrf- This method process the product component details
	 * 
	 * @param productFamily
	 * @param illSite
	 * @param component
	 * @param user
	 * @throws TclCommonException
	 */
	private void processMultiVrfProductComponent(MstProductFamily productFamily, QuoteIllSite illSite,
			ComponentDetail component, User user) throws TclCommonException {
		try {
			LOGGER.info("Entered into processMultiVrfProductComponent" + component.getName() + "TYPE"
					+ component.getType());
			QuoteVrfSites vrfSite = quoteVrfSitesRepository.findByQuoteIllSiteAndVrfNameAndSiteType(illSite,
					component.getName(), component.getType());
			if (vrfSite == null) {
				QuoteVrfSites quoteVrfSite = new QuoteVrfSites();
				quoteVrfSite.setQuoteIllSite(illSite);
				quoteVrfSite.setVrfName(component.getName());
				quoteVrfSite.setVrfType(component.getVrfPortType());
				quoteVrfSite.setSiteType(component.getType());
				quoteVrfSite.setCreatedTime(new Date());
				quoteVrfSite.setTpsServiceId(component.getServiceId());
				quoteVrfSite = quoteVrfSitesRepository.save(quoteVrfSite);

				MstProductComponent productComponent = getProductComponent(component, user);
				QuoteProductComponent quoteComponent = constructVrfProductComponent(productComponent, productFamily,
						quoteVrfSite.getId());
				quoteComponent.setType(component.getType());
				quoteProductComponentRepository.save(quoteComponent);
				LOGGER.info("componenet saved successfully" + quoteComponent.getMstProductComponent().getName());

				for (AttributeDetail attribute : component.getAttributes()) {
					processProductAttribute(quoteComponent, attribute, user);
				}
			} 
			else {
				LOGGER.info("updated vrf attributes" + vrfSite.getId());
				List<QuoteProductComponent> prodComponent=null;
				QuoteProductComponent quoteComponent = null;
				List<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findByNameAndStatus(component.getName(), (byte) 1);
				if (!mstProductComponent.isEmpty()) {
					LOGGER.info("mst product componennet " + mstProductComponent.get(0).getName());
					prodComponent = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndTypeAndReferenceName(vrfSite.getId(),
									mstProductComponent.get(0), productFamily, component.getType(),QuoteConstants.VRF_SITES.toString());
				}
				if (prodComponent != null && !prodComponent.isEmpty()) {
					quoteComponent = prodComponent.get(0);
						if (quoteComponent != null) {
							for (AttributeDetail attribute : component.getAttributes()) {
								LOGGER.info("-------Attribute Name--------{} ", attribute.getName());
								ProductAttributeMaster productAttribute=null;
								List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
										.findByNameAndStatus(attribute.getName(), (byte) 1);
								// if attribute is empty need to add new attribute
								if (productAttributeMasters == null) {
									productAttribute = new ProductAttributeMaster();
									productAttribute.setCreatedBy(user.getUsername());
									productAttribute.setCreatedTime(new Date());
									productAttribute.setDescription(attribute.getName());
									productAttribute.setName(attribute.getName());
									productAttribute.setStatus((byte) 1);
									productAttribute = productAttributeMasterRepository
											.save(productAttribute);
								}
								else {
									productAttribute=productAttributeMasters.get(0);
								}
								List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
										.findByQuoteProductComponentAndProductAttributeMaster(quoteComponent,
												productAttribute);
								if (quoteProductComponentsAttributeValues.isEmpty()) {
									QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
									quoteProductComponentsAttributeValue.setAttributeValues(attribute.getValue());
									quoteProductComponentsAttributeValue
											.setProductAttributeMaster(productAttribute);
									quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteComponent);
									quoteProductComponentsAttributeValueRepository
											.save(quoteProductComponentsAttributeValue);
									LOGGER.info("-------Updated the properities---new-------");
								} else {
									QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
											.get(0);
									quoteProductComponentsAttributeValue.setAttributeValues(attribute.getValue());
									quoteProductComponentsAttributeValueRepository
											.save(quoteProductComponentsAttributeValue);
									LOGGER.info("-------Updated the properities---old-----");
								}
							}
						}
					}

				}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * removeExistingMultiVrfProductComponent - This method is to remove the existing VRFs
	 * 
	 * @param productFamily
	 * @param illSite
	 * @param component
	 * @param user
	 * @throws TclCommonException
	 */
	private void removeExistingMultiVrfProductComponent(QuoteIllSite illSite,MstProductFamily productFamily) throws TclCommonException {
		try {
			LOGGER.info("Entered in to removeExistingMultiVrfProductComponent for site ID "+ illSite.getId());
			List<QuoteProductComponent> quoteProductComponent = new ArrayList<QuoteProductComponent>();
			List<QuoteProductComponent> quoteProductComponentList = new ArrayList<QuoteProductComponent>();
			List<QuoteVrfSites> vrfSiteList = quoteVrfSitesRepository.findByQuoteIllSite(illSite);
			List<MstProductComponent> mstProductComponents = new ArrayList<MstProductComponent>();
			if(!vrfSiteList.isEmpty()) {
				for(QuoteVrfSites vrfSiteListIds : vrfSiteList) {
					mstProductComponents = mstProductComponentRepository
							.findByNameAndStatus(vrfSiteListIds.getVrfName(), (byte) 1);
					if(!mstProductComponents.isEmpty()) {
							quoteProductComponent = quoteProductComponentRepository
									.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndReferenceName(vrfSiteListIds.getId(),
											mstProductComponents.get(0), productFamily,QuoteConstants.VRF_SITES.toString());
							quoteProductComponentList.addAll(quoteProductComponent);
					}	
				}
			}
			Set<Integer> setOfProductComponentIds = new HashSet<Integer>();
			if(!quoteProductComponentList.isEmpty()) {
				for(QuoteProductComponent quoteProductComponentIds : quoteProductComponentList) {
					setOfProductComponentIds.add(quoteProductComponentIds.getId());
					quoteProductComponentsAttributeValueRepository.deleteAllByQuoteProductComponentIdIn(setOfProductComponentIds);
					quoteProductComponentRepository.deleteById(quoteProductComponentIds.getId());
					quoteVrfSitesRepository.deleteById(quoteProductComponentIds.getReferenceId());
				}	
			}
			LOGGER.info("Exiting removeExistingMultiVrfProductComponent method after removing entries in quote_vrf_site, quote_product_component"
					+ "quote_product_component_attribute_values for site ID "+ illSite.getId());
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	
	/**
	 * 
	 * constructProductComponent- This method constructs the
	 * {Vrf QuoteProductComponent} Entity
	 * 
	 * @param productComponent
	 * @param mstProductFamily
	 * @param illSiteId
	 * @return QuoteProductComponent
	 * @throws TclCommonException
	 */
	protected QuoteProductComponent constructVrfProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer vrfId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(vrfId);
		quoteProductComponent.setReferenceName(QuoteConstants.VRF_SITES.toString());
		return quoteProductComponent;

	}

	

	
	public CommonValidationResponse validateSubCategoryForNSQuotes(Integer quoteId) throws TclCommonException {
		
		CommonValidationResponse response = new CommonValidationResponse();
		response.setStatus(true);
		response.setValidationMessage(null);
		if(quoteId == null)
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		Optional<Quote> quote = quoteRepository.findById(quoteId);
		if(quote.isPresent() && quote.get().getNsQuote() != null && CommonConstants.Y.equalsIgnoreCase(quote.get().getNsQuote())) {
			LOGGER.info("Entering validateSubCategoryForNSQuotes with quote code{}", quote.get().getQuoteCode());
			quote.get().getQuoteToLes().stream().forEach(quoteToLe -> {
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
				if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					quoteIllSiteToServiceList.stream().forEach(siteToServiceEntry -> {
						List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteToServiceEntry.getQuoteIllSite().getId(), 
								IllSitePropertiesConstants.SITE_PROPERTIES.toString(), QuoteConstants.GVPN_SITES.toString());
						quoteProductComponentList.stream().forEach(prodComponent -> {
							LOGGER.info("Entering prod component list loop");
							Optional<QuoteProductComponentsAttributeValue> attValue = prodComponent.getQuoteProductComponentsAttributeValues().stream().
							filter(prodCompAttValue -> IllSitePropertiesConstants.SFDC_ORDER_TYPE.toString().equalsIgnoreCase(
									prodCompAttValue.getProductAttributeMaster().getName())).findFirst();
							
							if(attValue.isPresent()) {
								if(Objects.isNull(attValue.get().getAttributeValues()) || StringUtils.isAllBlank(attValue.get().getAttributeValues())) {
									LOGGER.info("Sub category not selected for site id {}", siteToServiceEntry.getQuoteIllSite().getId());
									response.setStatus(false);
									response.setValidationMessage("Sub Category has not been selected for a site");
								}
							} else {
								LOGGER.info("In else condition Sub category not selected for site id {}", siteToServiceEntry.getQuoteIllSite().getId());
								response.setStatus(false);
								response.setValidationMessage("Sub Category has not been selected for a site");
							}
								
						});
					});
					
				}
			});
			
		}
		
		return response;
	}
	

	public List<LMDetailBean> getLMProvider(Integer quoteLeId, Integer siteId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);

		List<LMDetailBean> listoflms = new ArrayList<LMDetailBean>();
		List<QuoteIllSiteToService> serviceIdsList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteLeId);
		Integer siteIDFromDB = null;
		Optional<SiteFeasibility> selectedSiteFeasibilityPrimary =null;
		Optional<SiteFeasibility>  selectedSiteFeasibilitySecondary = null;
		Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
				
		if(illSite.isPresent()) {
			siteIDFromDB = illSite.get().getId();
			selectedSiteFeasibilityPrimary = getManualSelectedResponseSiteTypeBased(siteId, illSite,"primary");			
		    selectedSiteFeasibilitySecondary = getManualSelectedResponseSiteTypeBased(siteId, illSite,"secondary");			
		}else {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
		}


		if (quoteToLes.get().getIsMultiCircuit() == 1 && !CollectionUtils.isEmpty(serviceIdsList)) {
			Optional<QuoteIllSiteToService> optServiceDetail = serviceIdsList.stream().filter(x -> x.getType() != null)
					.filter(x -> x.getType().equalsIgnoreCase("primary")).findFirst();

			if (optServiceDetail.isPresent()) {
				LMDetailBean primaryLM = new LMDetailBean();
				primaryLM.setServiceId(optServiceDetail.get().getErfServiceInventoryTpsServiceId());
				primaryLM.setSiteType(optServiceDetail.get().getType());
				primaryLM.setServiceInventoryLMProvider(gvpnPricingFeasibilityService.getLmProviderForSIds(primaryLM.getServiceId()));
				primaryLM.setSiteId(siteIDFromDB);
				if (selectedSiteFeasibilityPrimary != null && selectedSiteFeasibilityPrimary.isPresent()) {
					if (selectedSiteFeasibilityPrimary.get().getProvider() == null
							&& selectedSiteFeasibilityPrimary.get().getFeasibilityCheck().equalsIgnoreCase("system")
							&& selectedSiteFeasibilityPrimary.get().getWfeType().equalsIgnoreCase("MACD")
							&& primaryLM.getServiceInventoryLMProvider() != null) {
						LOGGER.info("Inside  primary provider null system selected macd wfe type scenario");
						primaryLM.setMfLMProvider(primaryLM.getServiceInventoryLMProvider());
						primaryLM.setPreFeasible(true);
						
					}
					if (selectedSiteFeasibilityPrimary.get().getProvider() != null) {
						primaryLM.setMfLMProvider(selectedSiteFeasibilityPrimary.get().getProvider());
					}
				}
				listoflms.add(primaryLM);
				LOGGER.info("The Primary serviceID for multicirucit Quote is {}", primaryLM.getServiceId());
			}

			Optional<QuoteIllSiteToService> optServiceDetailSecondary = serviceIdsList.stream()
					.filter(x -> x.getType() != null).filter(x -> x.getType().equalsIgnoreCase("secondary"))
					.findFirst();

			if (optServiceDetailSecondary.isPresent()) {
				LMDetailBean secondaryLm = new LMDetailBean();
				secondaryLm.setServiceId(optServiceDetailSecondary.get().getErfServiceInventoryTpsServiceId());
				secondaryLm.setSiteType(optServiceDetailSecondary.get().getType());
				secondaryLm
						.setServiceInventoryLMProvider(gvpnPricingFeasibilityService.getLmProviderForSIds(secondaryLm.getServiceId()));
				secondaryLm.setSiteId(siteIDFromDB);
				 if (selectedSiteFeasibilitySecondary != null && selectedSiteFeasibilitySecondary.isPresent()) {
						// for port upgrade system wont give provider .existing lm as new LM
						if (selectedSiteFeasibilitySecondary.get().getProvider() == null
								&& selectedSiteFeasibilitySecondary.get().getFeasibilityCheck().equalsIgnoreCase("system")
								&& selectedSiteFeasibilitySecondary.get().getWfeType().equalsIgnoreCase("MACD")
								&& secondaryLm.getServiceInventoryLMProvider() != null) {
							LOGGER.info("Inside  secondary provider null system selected macd wfe type scenario");
							secondaryLm.setMfLMProvider(secondaryLm.getServiceInventoryLMProvider());
							secondaryLm.setPreFeasible(true);

						}
						if (selectedSiteFeasibilitySecondary.get().getProvider() != null) {
							secondaryLm.setMfLMProvider(selectedSiteFeasibilitySecondary.get().getProvider());
						}
					}
				listoflms.add(secondaryLm);
				LOGGER.info("The secondary serviceID for multicirucit Quote is {}", secondaryLm.getServiceId());
			}

		}
		if (!CollectionUtils.isEmpty(serviceIdsList) && quoteToLes.get().getIsMultiCircuit()!=1) {
			OpportunityBean oppBean =new OpportunityBean();
			oppBean.setServiceId(serviceIdsList.stream().findFirst().get()
					.getErfServiceInventoryTpsServiceId());
			oppBean = retrievePriSecSIDsForMFOppurtunity(oppBean,
					quoteToLes.get().getQuote().getId(), siteId);
			if(oppBean.getPrimaryServiceId()!=null) {
				LMDetailBean primaryNonMulticircuit = new LMDetailBean();
				primaryNonMulticircuit.setServiceId(oppBean.getPrimaryServiceId());
				primaryNonMulticircuit.setSiteType("primary");
				primaryNonMulticircuit.setServiceInventoryLMProvider(gvpnPricingFeasibilityService.getLmProviderForSIds(oppBean.getPrimaryServiceId()));
				primaryNonMulticircuit.setSiteId(siteIDFromDB);
				
				if (selectedSiteFeasibilityPrimary != null && selectedSiteFeasibilityPrimary.isPresent()) {
					if (selectedSiteFeasibilityPrimary.get().getProvider() == null
							&& selectedSiteFeasibilityPrimary.get().getFeasibilityCheck().equalsIgnoreCase("system")
							&& selectedSiteFeasibilityPrimary.get().getWfeType().equalsIgnoreCase("MACD")
							&& primaryNonMulticircuit.getServiceInventoryLMProvider() != null) {
						LOGGER.info(
								"Inside non multicircuit primary provider null system selected macd wfe type scenario");
						primaryNonMulticircuit.setMfLMProvider(primaryNonMulticircuit.getServiceInventoryLMProvider());
						primaryNonMulticircuit.setPreFeasible(true);

					}
					if (selectedSiteFeasibilityPrimary.get().getProvider() != null) {
						primaryNonMulticircuit.setMfLMProvider(selectedSiteFeasibilityPrimary.get().getProvider());
					}
				}

				listoflms.add(primaryNonMulticircuit);

			}
			if (oppBean.getSecondaryServiceId() != null) {
				LMDetailBean secNonMulticircuit = new LMDetailBean();
				secNonMulticircuit.setServiceId(oppBean.getSecondaryServiceId());
				secNonMulticircuit.setSiteType("secondary");
				secNonMulticircuit.setServiceInventoryLMProvider(
						gvpnPricingFeasibilityService.getLmProviderForSIds(oppBean.getSecondaryServiceId()));
				secNonMulticircuit.setSiteId(siteIDFromDB);
				if (selectedSiteFeasibilitySecondary != null && selectedSiteFeasibilitySecondary.isPresent()) {
					if (selectedSiteFeasibilitySecondary.get().getProvider() == null
							&& selectedSiteFeasibilitySecondary.get().getFeasibilityCheck().equalsIgnoreCase("system")
							&& selectedSiteFeasibilitySecondary.get().getWfeType().equalsIgnoreCase("MACD")
							&& secNonMulticircuit.getServiceInventoryLMProvider() != null) {
						LOGGER.info(
								"Inside non multicircuit secondary provider null system selected macd wfe type scenario");
						secNonMulticircuit.setMfLMProvider(secNonMulticircuit.getServiceInventoryLMProvider());
						secNonMulticircuit.setPreFeasible(true);

					}
					if (selectedSiteFeasibilitySecondary.get().getProvider() != null) {
						secNonMulticircuit.setMfLMProvider(selectedSiteFeasibilitySecondary.get().getProvider());
					}
				}
				listoflms.add(secNonMulticircuit);
			}
		}
            return listoflms;
	}
	private Optional<SiteFeasibility> getManualSelectedResponseSiteTypeBased(Integer siteId,
			Optional<QuoteIllSite> illSite, String siteType) {
		if (illSite.isPresent()) {
			List<SiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository
					.findByQuoteIllSite_IdAndIsSelectedAndType(siteId, CommonConstants.BACTIVE, siteType);
			
			if(!CollectionUtils.isEmpty(selectedSiteFeasibility)) {
			     return selectedSiteFeasibility.stream().findFirst();
			}

		}
		return null;
	}
	
	public void checkFeasibilityValidityPeriod(Optional<QuoteToLe> quoteToLe, CommonValidationResponse commonValidationResponse) {
		LOGGER.info("Inside checkFeasibilityValidityPeriod");
		List<String> validationMessages = new ArrayList<>();

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLe.get().getId(), GvpnConstants.GVPN);
		if(Objects.nonNull(quoteToLeProductFamily)) {
			List<ProductSolution> productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			if(Objects.nonNull(productSolutions)) {
				productSolutions.stream().forEach(productSolution -> {
					List<QuoteIllSite> quoteIllSites= illSitesRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);
					if(Objects.nonNull(quoteIllSites)) {
						quoteIllSites.stream().forEach(quoteIllSite -> {
							List<SiteFeasibility> siteFeasibilitiesList = siteFeasibilityRepository.findByQuoteIllSite_Id(quoteIllSite.getId());
							if(Objects.nonNull(siteFeasibilitiesList)) {
								siteFeasibilitiesList.stream().forEach(siteFeasibility -> {
									try {
										String responseJson = siteFeasibility.getResponseJson();
										if (siteFeasibility.getIsSelected() == 1) {
											LOGGER.info("Inside feasibilty rank non null");
											Feasible site = (Feasible) Utils.convertJsonToObject(responseJson, Feasible.class);
											processFeasibilityCheck(siteFeasibility,validationMessages,site);
										}
									} catch (TclCommonException e) {
										e.printStackTrace();
									}
								});
							}
						});
					}
				});
			}
		}
		String feasibilityValidationMessage = validationMessages.stream().collect(Collectors.joining(","));
		String validationResponseMessage = commonValidationResponse.getValidationMessage();
		String validationResponse = null;
		if(validationResponseMessage!=null) {
			StringJoiner response = new StringJoiner(",").add(validationResponseMessage).add(feasibilityValidationMessage) ;
			validationResponse = response.toString();
			LOGGER.info("checkFeasibilityValidityPeriod>> not null validationResponse{}"+ validationResponse);
		} else {
			validationResponse = feasibilityValidationMessage;
			LOGGER.info("checkFeasibilityValidityPeriod>> validationResponse{}"+ validationResponse);
		}
		if (StringUtils.isNotBlank(validationResponse)) {
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage(validationResponse.toString());
		}
	}

	private void processFeasibilityCheck(SiteFeasibility siteFeasibility, List<String> validationMessages, Feasible site) throws TclCommonException {
		LOGGER.info("Inside processFeasibilityCheck");
		if("system".equalsIgnoreCase(siteFeasibility.getFeasibilityCheck())) {
			LOGGER.info("processFeasibilityCheck inside system");
			int validityPeriod = 60;
			constructValidationMessage(siteFeasibility.getCreatedTime(), validationMessages, validityPeriod);
		} else if("manual".equalsIgnoreCase(siteFeasibility.getFeasibilityCheck())) {
			LOGGER.info("processFeasibilityCheck inside manual");
			if(("Onnet Wireline").equalsIgnoreCase(siteFeasibility.getFeasibilityMode()) || (PDFConstants.ONNET_RF).equalsIgnoreCase(siteFeasibility.getFeasibilityMode())
					|| (PDFConstants.ONNET_WIRED).equalsIgnoreCase(siteFeasibility.getFeasibilityMode()) || (PDFConstants.ONNET_WIRELESS).equalsIgnoreCase(siteFeasibility.getFeasibilityMode())) {
				LOGGER.info("processFeasibilityCheck inside manual>>>onnet");
				constructValidationMessage(siteFeasibility.getCreatedTime(), validationMessages, Integer.valueOf(site.getValidityPeriod()));
			} else if((PDFConstants.OFFNET_WIRELESS).equalsIgnoreCase(siteFeasibility.getFeasibilityMode())||(PDFConstants.OFFNET_RF).equalsIgnoreCase(siteFeasibility.getFeasibilityMode())
					|| ("Offnet Wireline").equalsIgnoreCase(siteFeasibility.getFeasibilityMode())) {
				LOGGER.info("processFeasibilityCheck inside manual>>>offnet");
				String providerResponseDate = site.getProviderResponseDate();
				LocalDate calDate = LocalDate.parse(providerResponseDate);
				LocalDate totalDays = calDate.plusDays(Integer.valueOf(site.getValidityPeriod()));
				LOGGER.info("processFeasibilityCheck>> totalDays{} " + totalDays + "site validity period>> "  +site.getValidityPeriod());
				LocalDate today = LocalDate.now();
				if (totalDays.compareTo(today) < 0) {
					validationMessages.add("Validity of Feasibility has expired");
				}
			}
		}		
	}

	private void constructValidationMessage(Timestamp createdTime, List<String> validationMessages, int validityPeriod) throws TclCommonException {
		String createdDate = Utils.convertTimeStampToString(createdTime);
		LocalDateTime today = LocalDateTime.now();
		String currentDate = Utils.convertTimeStampToString(Timestamp.valueOf(today));
		int differenceInDays = Utils.findDifferenceInDays(createdDate,currentDate);
		LOGGER.info("constructValidationMessage>> differenceInDays{} " + differenceInDays);
		if(differenceInDays > validityPeriod) {
			validationMessages.add("Validity of Feasibility has expired");
		}
	}
	
	public List<QuoteSiteDifferentialCommercial> persistQuoteSiteCommercialsAtServiceIdLevel(QuoteIllSite illSite, QuoteToLe quoteToLe, OrderIllSite orderSite) {
		List<QuoteSiteDifferentialCommercial> quoteSiteCommercialList = new ArrayList<>();
		LOGGER.info("Entering persistQuoteSiteCommercialsAtServiceIdLevel");
		
		User userEntity = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
		if(userEntity==null) {
			userEntity = userRepository.findByUsernameAndStatus("root", CommonConstants.ACTIVE);//TODO
		}
		Integer createdId=userEntity.getId();
		
		Double[] subTotalMrc = { 0D };
		Double[] subTotalNrc = { 0D };
		
		Double[] subTotalMrcSecondary = { 0D };
		Double[] subTotalNrcSecondary = { 0D };
		List<QuotePrice> quotePriceList = quotePriceRepository.findByQuoteId(quoteToLe.getQuote().getId());
	
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) && !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
			List<OrderIllSiteToService> orderSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(orderSite);
			orderSiteToServiceList.stream().forEach(siteToService -> {
			QuoteSiteDifferentialCommercial serviceDifferentialCommercial = null;
			List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository.
					findByQuoteSiteCodeAndServiceType(siteToService.getOrderIllSite().getSiteCode(), siteToService.getType());
			if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) { 
				serviceDifferentialCommercial = quoteSiteDifferentialCommercialList.get(0);
			} else {
				serviceDifferentialCommercial = new QuoteSiteDifferentialCommercial();
			}
			SIServiceDetailDataBean serviceDetail;
			Double calculatedMrc = 0D;
			try {
				LOGGER.info("Gvpn Fetching  service details for {} ",siteToService.getErfServiceInventoryTpsServiceId());
				serviceDetail = macdUtils.getServiceDetail(siteToService.getErfServiceInventoryTpsServiceId(), quoteToLe.getQuoteCategory());
			} catch (Exception e) {
				LOGGER.info("Error in persistQuoteSiteCommercialsAtServiceIdLevel {}", e);
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
				
			}
			LOGGER.info("Fetched service details for {} ",serviceDetail);
			LOGGER.info("service inventory MRC {}, nrc {}, arc {}", serviceDetail.getMrc(), serviceDetail.getNrc(), serviceDetail.getArc());
			calculatedMrc = serviceDetail.getArc()/12;
			LOGGER.info("calculated mrc {}", calculatedMrc);
			serviceDifferentialCommercial.setExistingMrc(calculatedMrc);
			serviceDifferentialCommercial.setExistingNrc(serviceDetail.getNrc());
			serviceDifferentialCommercial.setCreatedBy(createdId);
			serviceDifferentialCommercial.setCreatedTime(new Date());
			serviceDifferentialCommercial.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			serviceDifferentialCommercial.setQuoteToLe(quoteToLe);
			serviceDifferentialCommercial.setQuoteSiteCode(illSite.getSiteCode());
			serviceDifferentialCommercial.setQuoteSiteId(illSite.getId());
			serviceDifferentialCommercial.setServiceType(siteToService.getType());
			serviceDifferentialCommercial.setTpsServiceId(siteToService.getErfServiceInventoryTpsServiceId());
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.
					findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.GVPN_SITES.toString(), siteToService.getType());
			if(quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
				quoteProductComponentList.stream().forEach(quoteProductComponent -> {
					quotePriceList.stream().filter(quotePrice -> (quoteProductComponent.getId().equals(Integer.valueOf(quotePrice.getReferenceId())) 
							&& QuoteConstants.COMPONENTS.toString().equalsIgnoreCase(quotePrice.getReferenceName()))).forEach(quotePriceEntry -> {
								quotePriceEntry.setEffectiveMrc(quotePriceEntry.getEffectiveMrc() == null ? 0D : quotePriceEntry.getEffectiveMrc());
								quotePriceEntry.setEffectiveNrc(quotePriceEntry.getEffectiveNrc() == null ? 0D : quotePriceEntry.getEffectiveNrc());
								subTotalMrc[0] += quotePriceEntry.getEffectiveMrc();
								subTotalNrc[0] += quotePriceEntry.getEffectiveNrc();
					});
					
					LOGGER.info("sub total value after quote prd component loop secondary mrc {}, nrc {}", subTotalMrc[0], subTotalNrc[0]);
				});
				
				
				serviceDifferentialCommercial.setDifferentialMrc(subTotalMrc[0] - calculatedMrc);
				serviceDifferentialCommercial.setDifferentialNrc(subTotalNrc[0] - serviceDetail.getNrc());
				LOGGER.info("final diff mrc {}, final diff nrc {}", serviceDifferentialCommercial.getDifferentialMrc(), serviceDifferentialCommercial.getDifferentialNrc());
				quoteSiteCommercialList.add(serviceDifferentialCommercial);
				
				
			}
		});
		
		
		} else {
			
			List<QuoteProductComponent> quoteProductComponentListSecondary = quoteProductComponentRepository.findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.GVPN_SITES.toString(), PDFConstants.SECONDARY);
			
			if(quoteProductComponentListSecondary != null && !quoteProductComponentListSecondary.isEmpty()) {
				QuoteSiteDifferentialCommercial serviceDifferentialCommercialSecondary = null;
				List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository.findByQuoteSiteIdAndServiceType(illSite.getId(), PDFConstants.SECONDARY);
				if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) { 
					serviceDifferentialCommercialSecondary = quoteSiteDifferentialCommercialList.get(0);
				} else {
					 serviceDifferentialCommercialSecondary = new QuoteSiteDifferentialCommercial();
				}
				serviceDifferentialCommercialSecondary.setExistingMrc(0D);
				serviceDifferentialCommercialSecondary.setExistingNrc(0D);
				serviceDifferentialCommercialSecondary.setCreatedBy(createdId);
				serviceDifferentialCommercialSecondary.setCreatedTime(new Date());
				serviceDifferentialCommercialSecondary.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
				serviceDifferentialCommercialSecondary.setQuoteToLe(quoteToLe);
				serviceDifferentialCommercialSecondary.setQuoteSiteCode(illSite.getSiteCode());
				serviceDifferentialCommercialSecondary.setQuoteSiteId(illSite.getId());
				serviceDifferentialCommercialSecondary.setServiceType(PDFConstants.SECONDARY);
				quoteProductComponentListSecondary.stream().forEach(quoteProductComponent -> {
					quotePriceList.stream().filter(quotePrice -> (quoteProductComponent.getId().equals(Integer.valueOf(quotePrice.getReferenceId())) 
							&& QuoteConstants.COMPONENTS.toString().equalsIgnoreCase(quotePrice.getReferenceName()))).forEach(quotePriceEntry -> {
								quotePriceEntry.setEffectiveMrc(quotePriceEntry.getEffectiveMrc() == null ? 0D : quotePriceEntry.getEffectiveMrc());
								quotePriceEntry.setEffectiveNrc(quotePriceEntry.getEffectiveNrc() == null ? 0D : quotePriceEntry.getEffectiveNrc());
								subTotalMrcSecondary[0] += quotePriceEntry.getEffectiveMrc();
								subTotalNrcSecondary[0] += quotePriceEntry.getEffectiveNrc();
								
							});
					LOGGER.info("sub total value after quote prd component loop secondary mrc {}, nrc {}", subTotalMrcSecondary[0], subTotalNrcSecondary[0]);
					

			});
				
				serviceDifferentialCommercialSecondary.setDifferentialMrc(subTotalMrcSecondary[0]);
				serviceDifferentialCommercialSecondary.setDifferentialNrc(subTotalNrcSecondary[0]);
				quoteSiteCommercialList.add(serviceDifferentialCommercialSecondary);
			}
			
			
			List<QuoteProductComponent> quoteProductComponentListPrimary = quoteProductComponentRepository.findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.GVPN_SITES.toString(), PDFConstants.PRIMARY);
			if(quoteProductComponentListPrimary != null && !quoteProductComponentListPrimary.isEmpty()) {
				QuoteSiteDifferentialCommercial serviceDifferentialCommercial = null;
				List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository.findByQuoteSiteIdAndServiceType(illSite.getId(), PDFConstants.PRIMARY);
				if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) { 
					serviceDifferentialCommercial = quoteSiteDifferentialCommercialList.get(0);
				} else {
					serviceDifferentialCommercial = new QuoteSiteDifferentialCommercial();
				}	
				serviceDifferentialCommercial.setExistingMrc(0D);
				serviceDifferentialCommercial.setExistingNrc(0D);
				serviceDifferentialCommercial.setCreatedBy(createdId);
				serviceDifferentialCommercial.setCreatedTime(new Date());
				serviceDifferentialCommercial.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
				serviceDifferentialCommercial.setQuoteToLe(quoteToLe);
				serviceDifferentialCommercial.setQuoteSiteCode(illSite.getSiteCode());
				serviceDifferentialCommercial.setQuoteSiteId(illSite.getId());
				serviceDifferentialCommercial.setServiceType(PDFConstants.PRIMARY);
			quoteProductComponentListPrimary.stream().forEach(quoteProductComponent -> {
				quotePriceList.stream().filter(quotePrice -> (quoteProductComponent.getId().equals(Integer.valueOf(quotePrice.getReferenceId())) 
						&& QuoteConstants.COMPONENTS.toString().equalsIgnoreCase(quotePrice.getReferenceName()))).forEach(quotePriceEntry -> {
							quotePriceEntry.setEffectiveMrc(quotePriceEntry.getEffectiveMrc() == null ? 0D : quotePriceEntry.getEffectiveMrc());
							quotePriceEntry.setEffectiveNrc(quotePriceEntry.getEffectiveNrc() == null ? 0D : quotePriceEntry.getEffectiveNrc());
							subTotalMrc[0] += quotePriceEntry.getEffectiveMrc();
							subTotalNrc[0] += quotePriceEntry.getEffectiveNrc();
							
						});
				LOGGER.info("sub total value after quote prd component loop primary mrc {}, nrc {}", subTotalMrc[0], subTotalNrc[0]);
				
			});
			serviceDifferentialCommercial.setDifferentialMrc(subTotalMrc[0]);
			serviceDifferentialCommercial.setDifferentialNrc(subTotalNrc[0]);
			quoteSiteCommercialList.add(serviceDifferentialCommercial);
			
			}
			
			
			}
	

		quoteSiteDifferentialCommercialRepository.saveAll(quoteSiteCommercialList);
		return quoteSiteCommercialList;
	}
		

	@Transactional
	public List<LeOwnerDetailsSfdc> getOwnerDetailsForSfdc(Integer customerId, Integer quoteId) throws TclCommonException {

		try {
			return getDetails(customerId, quoteId);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private List<LeOwnerDetailsSfdc> getDetails(Integer customerId, Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(customerId,"Customer Id cannot be null;required field");
		Objects.requireNonNull(quoteId,"Quote Id cannot be null;required field");
		Integer erfCusCustomerLegalEntityId = quoteToLeRepository.findByQuote_Id(quoteId).get(0).getErfCusCustomerLegalEntityId();
		String cusLeId=","+erfCusCustomerLegalEntityId.toString();
		/*String fetchOwner="";
		LeOwnerDetailsSfdc leOwnerDetailsSfdc= new LeOwnerDetailsSfdc();
		fetchOwner = settingJourneyOwnerDetails(fetchOwner, leOwnerDetailsSfdc);*/

		String queueResponse = (String) mqUtils.sendAndReceive(ownerDetailsQueue, customerId.toString()+cusLeId);
		LOGGER.info("Response from owner details queue for quote ---> {}  is ----> {} ", quoteId, queueResponse);

		LeOwnerDetailsSfdc[] ownerDetails = (LeOwnerDetailsSfdc[]) Utils.convertJsonToObject(queueResponse,
				LeOwnerDetailsSfdc[].class);
		List<LeOwnerDetailsSfdc> ownerDetailsSfdcList = new ArrayList<>();
		ownerDetailsSfdcList.addAll(Arrays.asList(ownerDetails));
		if(!ownerDetailsSfdcList.isEmpty() && Objects.nonNull(ownerDetailsSfdcList)){
			LOGGER.info("List converted into object after Owner Details queue call is----> {} with size ----> {} " , ownerDetailsSfdcList,ownerDetailsSfdcList.size());
		}
		/*if(fetchOwner.equalsIgnoreCase("No")){
			ownerDetailsSfdcList.add(leOwnerDetailsSfdc);
		}*/
		if(!ownerDetailsSfdcList.isEmpty()){
			LOGGER.info("Final List sent for quote id ---> {} is ----> {} ", quoteId,ownerDetailsSfdcList );
		}
		return ownerDetailsSfdcList;
	}

	/*private String settingJourneyOwnerDetails(String fetchOwner, LeOwnerDetailsSfdc leOwnerDetailsSfdc) {
		fetchOwner="No";
		if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			if (appEnv.equals(SFDCConstants.PROD)) {
				LOGGER.info("OPPORTUNITY USER EMAIL ID {}", Utils.getSource());
				leOwnerDetailsSfdc.setOwnerName(Utils.getSource());
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
			} else {
				leOwnerDetailsSfdc.setOwnerName(null);
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
			}
		} else {
			if (appEnv.equals(SFDCConstants.PROD)) {
				fetchOwner="Yes";
			} else {
				leOwnerDetailsSfdc.setOwnerName(null);
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
			}
		}
		return fetchOwner;
	}
*/
	private void persistLeOwnerDetailsSfdc(CreateDocumentDto documentDto) {
		if(Objects.nonNull(documentDto) && Objects.nonNull(documentDto.getQuoteLeId())){
			QuoteToLe quoteToLe = quoteToLeRepository.findById(documentDto.getQuoteLeId()).get();

			List<QuoteLeAttributeValue> quoteLeAttrVal = quoteLeAttributeValueRepository.findByQuoteToLe_Id(quoteToLe.getId());
			Map<String,Integer> attrMap = new HashMap<>();
			quoteLeAttrVal.forEach(value->{
				attrMap.put(value.getDisplayValue(),value.getId());
			});

			if(attrMap.containsKey(OWNER_EMAIL_SFDC)){
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(OWNER_EMAIL_SFDC)).get();
				attribute.setAttributeValue(documentDto.getOwnerEmailSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			}
			else{
				QuoteLeAttributeValue leAttributeValue = new QuoteLeAttributeValue();
				leAttributeValue.setQuoteToLe(quoteToLe);
				leAttributeValue.setAttributeValue(Objects.nonNull(documentDto.getOwnerEmailSfdc())?documentDto.getOwnerEmailSfdc():"");
				leAttributeValue.setDisplayValue(LeAttributesConstants.OWNER_EMAIL_SFDC);
				leAttributeValue.setMstOmsAttribute(mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.OWNER_EMAIL_SFDC,BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue);
			}

			if(attrMap.containsKey(OWNER_NAME_SFDC)){
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(OWNER_NAME_SFDC)).get();
				attribute.setAttributeValue(documentDto.getOwnerNameSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			}
			else{
				QuoteLeAttributeValue leAttributeValue1 = new QuoteLeAttributeValue();
				leAttributeValue1.setQuoteToLe(quoteToLe);
				leAttributeValue1.setAttributeValue(Objects.nonNull(documentDto.getOwnerNameSfdc())?documentDto.getOwnerNameSfdc():"");
				leAttributeValue1.setDisplayValue(LeAttributesConstants.OWNER_NAME_SFDC);
				leAttributeValue1.setMstOmsAttribute(mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.OWNER_NAME_SFDC,BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue1);
			}

			if(attrMap.containsKey(REGION_SFDC)){
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(REGION_SFDC)).get();
				attribute.setAttributeValue(documentDto.getRegionSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			}
			else {
				QuoteLeAttributeValue leAttributeValue2 = new QuoteLeAttributeValue();
				leAttributeValue2.setQuoteToLe(quoteToLe);
				leAttributeValue2.setAttributeValue(Objects.nonNull(documentDto.getRegionSfdc()) ? documentDto.getRegionSfdc() : "");
				leAttributeValue2.setDisplayValue(LeAttributesConstants.REGION_SFDC);
				leAttributeValue2.setMstOmsAttribute(mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.REGION_SFDC, BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue2);
			}

			if(attrMap.containsKey(TEAM_ROLE_SFDC)){
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(TEAM_ROLE_SFDC)).get();
				attribute.setAttributeValue(documentDto.getTeamRoleSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			}
			else {

				QuoteLeAttributeValue leAttributeValue3 = new QuoteLeAttributeValue();
				leAttributeValue3.setQuoteToLe(quoteToLe);
				leAttributeValue3.setAttributeValue(Objects.nonNull(documentDto.getTeamRoleSfdc()) ? documentDto.getTeamRoleSfdc() : "");
				leAttributeValue3.setDisplayValue(LeAttributesConstants.TEAM_ROLE_SFDC);
				leAttributeValue3.setMstOmsAttribute(mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.TEAM_ROLE_SFDC, BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue3);
			}

			if(attrMap.containsKey(CONTACT_SFDC)){
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(CONTACT_SFDC)).get();
				attribute.setAttributeValue(documentDto.getContactMobileSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			}
			else {

				QuoteLeAttributeValue leAttributeValue3 = new QuoteLeAttributeValue();
				leAttributeValue3.setQuoteToLe(quoteToLe);
				leAttributeValue3.setAttributeValue(Objects.nonNull(documentDto.getContactMobileSfdc()) ? documentDto.getContactMobileSfdc() : "");
				leAttributeValue3.setDisplayValue(LeAttributesConstants.CONTACT_SFDC);
				leAttributeValue3.setMstOmsAttribute(mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.CONTACT_SFDC, BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue3);
			}



		}
	}


    private void getLeOwnerDetailsSFDC(QuoteBean response, Quote quote) {
        QuoteToLe quoteToLe = quoteToLeRepository.findByQuote(quote).stream().findFirst().get();
        LOGGER.info("Entered setLeOwnerDetailsSFDC for quote --> {} and stage ---> {} ",quote.getQuoteCode(),quoteToLe.getStage());
        if(Objects.nonNull(quoteToLe) && ORDER_FORM.getConstantCode().equalsIgnoreCase(quoteToLe.getStage())){

            List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe_Id(quoteToLe.getId());
            LeOwnerDetailsSfdc leOwnerDetailsSfdc= new LeOwnerDetailsSfdc();

            Map<String,String> attrMap = new HashMap<>();
            quoteLeAttributeValues.forEach(value->{
                attrMap.put(value.getDisplayValue(),value.getAttributeValue());
            });

            if(attrMap.containsKey(OWNER_NAME_SFDC)){
                leOwnerDetailsSfdc.setOwnerName(attrMap.get(OWNER_NAME_SFDC));
            }

            if(attrMap.containsKey(OWNER_EMAIL_SFDC)){
                leOwnerDetailsSfdc.setEmail(attrMap.get(OWNER_EMAIL_SFDC));
            }

            if(attrMap.containsKey(REGION_SFDC)){
                leOwnerDetailsSfdc.setRegion(attrMap.get(REGION_SFDC));
            }

            if(attrMap.containsKey(TEAM_ROLE_SFDC)){
                leOwnerDetailsSfdc.setTeamRole(attrMap.get(TEAM_ROLE_SFDC));
            }

			if(attrMap.containsKey(CONTACT_SFDC)){
				leOwnerDetailsSfdc.setMobile(attrMap.get(CONTACT_SFDC));
			}



			LOGGER.info("Le Owner Bean for quote ---> {} is ---> {} ", quote.getQuoteCode(),leOwnerDetailsSfdc);
            response.setLeOwnerDetailsSfdc(leOwnerDetailsSfdc);
        }
    }

    
    public Integer getTotalSiteCount(Integer quoteId) {
		LOGGER.info("Inside getTotalSiteCount quoteId{} ", quoteId);
		Integer totalSiteCount = 0;
		List<QuoteToLe> quoteToLeList = quoteToLeRepository.findByQuote_Id(quoteId);
		for(QuoteToLe quoteToLe:quoteToLeList) {
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("GVPN",
					CommonConstants.BACTIVE);
			QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
			if (quoteToLeProdFamily != null) {
				List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProdFamily);
				for (ProductSolution productSolution : quoteProdSoln) {
					List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
							CommonConstants.BACTIVE);
					totalSiteCount = totalSiteCount + illSites.size();
				}
			}
		}
		return totalSiteCount;
	}

    /**
     * 
     * @author 4014530 Phaniteja
     * This method is for getting the gvpn list price attributes
     * @param multiSitePricingAttributes
     * @param primaryFlag
     * @param secondaryFlag
     * @param sites
     * @return MultiSitePricingAttributes
     */
	public MultiSitePricingAttributes getListPriceDetails(MultiSitePricingAttributes multiSitePricingAttributes, boolean primaryFlag,
			boolean secondaryFlag, QuoteIllSite sites) {
		LOGGER.info("Entering getListPriceDetails in GvpnQuoteService");
		 if(primaryFlag) {
			 LOGGER.info("Entering in to primary part");
		 Result response = new Result();
			PricingEngineResponse pricingDetails = pricingDetailsRepository
					.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(sites.getSiteCode(), "primary");
			if(Objects.nonNull(pricingDetails)) {
			String pricingResponse = pricingDetails.getResponseData();
			try {
				response = (Result) Utils.convertJsonToObject(pricingResponse, Result.class);
				multiSitePricingAttributes.setPortNrcListPrice(Objects.nonNull(response.getGVPNPortNRCAdjusted())?Utils.parseDouble(response.getGVPNPortNRCAdjusted()):0.0);
				multiSitePricingAttributes.setPortArcListPrice(Objects.nonNull(response.getGVPNARC())?Utils.parseDouble(response.getGVPNARC()):0.0);
				
				Double fLmNrcBwOnrf = Objects.nonNull(response.getFLmNrcBwOnrf())?Utils.parseDouble(response.getFLmNrcBwOnrf()):0.0;
				Double fLmNrcBwOnwl = Objects.nonNull(response.getFLmNrcBwOnwl())?Utils.parseDouble(response.getFLmNrcBwOnwl()):0.0;
				Double fLmNrcBwProvOfrf = Objects.nonNull(response.getFLmNrcBwProvOfrf())?Utils.parseDouble(response.getFLmNrcBwProvOfrf()):0.0;
				Double lastMileNrcListPrice = fLmNrcBwOnrf + fLmNrcBwOnwl + fLmNrcBwProvOfrf;
				multiSitePricingAttributes.setLastMileNrcListPrice(lastMileNrcListPrice);
	
				Double fLmArcBwOnwl = Objects.nonNull(response.getFLmArcBwOnwl())?Utils.parseDouble(response.getFLmArcBwOnwl()):0.0;
				Double fLmArcBwOnrf  = Objects.nonNull(response.getFLmArcBwOnrf())?Utils.parseDouble(response.getFLmArcBwOnrf()):0.0;
				Double fLmArcBwProvOfrf = Objects.nonNull(response.getFLmArcBwProvOfrf())?Utils.parseDouble(response.getFLmArcBwProvOfrf()):0.0;
				Double lastMileArcListPrice = fLmArcBwOnwl + fLmArcBwOnrf + fLmArcBwProvOfrf;
				multiSitePricingAttributes.setLastMileArcListPrice(lastMileArcListPrice);
				
				Double fLmNrcMastOnrf = Objects.nonNull(response.getFLmNrcMastOnrf())?Utils.parseDouble(response.getFLmNrcMastOnrf()):0.0;
				Double fLmNrcMastOfrf = Objects.nonNull(response.getFLmNrcMastOfrf())?Utils.parseDouble(response.getFLmNrcMastOfrf()):0.0;
				Double mastNrcListPrice = fLmNrcMastOnrf + fLmNrcMastOfrf;
				multiSitePricingAttributes.setMastNrcListPrice(mastNrcListPrice);	
				multiSitePricingAttributes.setCpeInstallNrcListPrice(Objects.nonNull(response.getCPEInstallationMP())?Utils.parseDouble(response.getCPEInstallationMP()):0.0);//CPE Install NRC List Price
				multiSitePricingAttributes.setCpeOutrightSaleNRCListPrice(Objects.nonNull(response.getCPEHWMP())?Utils.parseDouble(response.getCPEHWMP()):0.0);//CPE Outright Sale NRC List Price
				double cpeRentalMrc = (Objects.nonNull(response.getCPERentalMRC()) ?Utils.parseDouble(response.getCPERentalMRC()):0.0);
				multiSitePricingAttributes.setCpeRentalArcListPrice(cpeRentalMrc * 12);//CPE Rental ARC List Price
				multiSitePricingAttributes.setCpeManagementArcListPrice(Objects.nonNull(response.getCPEManagementINR())?Utils.parseDouble(response.getCPEManagementINR()):0.0);
				
				//macd
				if((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(response.getQuotetypeQuote())) {
					multiSitePricingAttributes.setPortArcListPrice(Objects.nonNull(response.getPortArcLP())?Utils.parseDouble(response.getPortArcLP()):0.0);
					Double lmNrcBwOnrf = Objects.nonNull(response.getLmNrcBwOnrf())?Utils.parseDouble(response.getLmNrcBwOnrf()):0.0;
					Double lmNrcBwOnwl = Objects.nonNull(response.getLmNrcBwOnwl())?Utils.parseDouble(response.getLmNrcBwOnwl()):0.0;
					Double lmNrcBwProvOfrf = Objects.nonNull(response.getLmNrcBwProvOfrf())?Utils.parseDouble(response.getLmNrcBwProvOfrf()):0.0;
					Double lmNrcListPrice = lmNrcBwOnrf + lmNrcBwOnwl + lmNrcBwProvOfrf;
					multiSitePricingAttributes.setLastMileNrcListPrice(lmNrcListPrice);
					
					Double lmArcBwOnwl = Objects.nonNull(response.getLmArcBwOnwl())?Utils.parseDouble(response.getLmArcBwOnwl()):0.0;
					Double lmArcBwOnrf  = Objects.nonNull(response.getLmArcBwOnrf())?Utils.parseDouble(response.getLmArcBwOnrf()):0.0;
					Double lmArcBwProvOfrf = Objects.nonNull(response.getLmArcBwProvOfrf())?Utils.parseDouble(response.getLmArcBwProvOfrf()):0.0;
					Double lmArcListPrice = lmArcBwOnwl + lmArcBwOnrf + lmArcBwProvOfrf;
					multiSitePricingAttributes.setLastMileArcListPrice(lmArcListPrice);
					
					Double lmNrcMastOnrf = Objects.nonNull(response.getLmNrcMastOnrf())?Utils.parseDouble(response.getLmNrcMastOnrf()):0.0;
					Double lmNrcMastOfrf = Objects.nonNull(response.getLmNrcMastOfrf())?Utils.parseDouble(response.getLmNrcMastOfrf()):0.0;
					Double mastNrcListPriceMacd = lmNrcMastOnrf + lmNrcMastOfrf;
					multiSitePricingAttributes.setMastNrcListPrice(mastNrcListPriceMacd);
				}
		}catch (TclCommonException e) {
			LOGGER.error("Error in GVPN getListPriceDetails",e);				
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,
					ResponseResource.R_CODE_ERROR);
			}
	 }
}
		if(secondaryFlag) {
			LOGGER.info("Entering in to secondary part");
			Result response = new Result();
		PricingEngineResponse pricingDetails = pricingDetailsRepository
				.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(sites.getSiteCode(), "secondary");
		if(Objects.nonNull(pricingDetails)) {
		String pricingResponse = pricingDetails.getResponseData();
		try {
			response = (Result) Utils.convertJsonToObject(pricingResponse, Result.class);
			multiSitePricingAttributes.setPortNrcListPrice(Objects.nonNull(response.getGVPNPortNRCAdjusted())?Utils.parseDouble(response.getGVPNPortNRCAdjusted()):0.0);
			multiSitePricingAttributes.setPortArcListPrice(Objects.nonNull(response.getGVPNARC())?Utils.parseDouble(response.getGVPNARC()):0.0);
			
			Double fLmNrcBwOnrf = Objects.nonNull(response.getFLmNrcBwOnrf())?Utils.parseDouble(response.getFLmNrcBwOnrf()):0.0;
			Double fLmNrcBwOnwl = Objects.nonNull(response.getFLmNrcBwOnwl())?Utils.parseDouble(response.getFLmNrcBwOnwl()):0.0;
			Double fLmNrcBwProvOfrf = Objects.nonNull(response.getFLmNrcBwProvOfrf())?Utils.parseDouble(response.getFLmNrcBwProvOfrf()):0.0;
			Double lastMileNrcListPrice = fLmNrcBwOnrf + fLmNrcBwOnwl + fLmNrcBwProvOfrf;
			multiSitePricingAttributes.setLastMileNrcListPrice(lastMileNrcListPrice);

			Double fLmArcBwOnwl = Objects.nonNull(response.getFLmArcBwOnwl())?Utils.parseDouble(response.getFLmArcBwOnwl()):0.0;
			Double fLmArcBwOnrf  = Objects.nonNull(response.getFLmArcBwOnrf())?Utils.parseDouble(response.getFLmArcBwOnrf()):0.0;
			Double fLmArcBwProvOfrf = Objects.nonNull(response.getFLmArcBwProvOfrf())?Utils.parseDouble(response.getFLmArcBwProvOfrf()):0.0;
			Double lastMileArcListPrice = fLmArcBwOnwl + fLmArcBwOnrf + fLmArcBwProvOfrf;
			multiSitePricingAttributes.setLastMileArcListPrice(lastMileArcListPrice);
			
			Double fLmNrcMastOnrf = Objects.nonNull(response.getFLmNrcMastOnrf())?Utils.parseDouble(response.getFLmNrcMastOnrf()):0.0;
			Double fLmNrcMastOfrf = Objects.nonNull(response.getFLmNrcMastOfrf())?Utils.parseDouble(response.getFLmNrcMastOfrf()):0.0;
			Double mastNrcListPrice = fLmNrcMastOnrf + fLmNrcMastOfrf;
			multiSitePricingAttributes.setMastNrcListPrice(mastNrcListPrice);	
			
			multiSitePricingAttributes.setCpeInstallNrcListPrice(Objects.nonNull(response.getCPEInstallationMP())?Utils.parseDouble(response.getCPEInstallationMP()):0.0);//CPE Install NRC List Price
			multiSitePricingAttributes.setCpeOutrightSaleNRCListPrice(Objects.nonNull(response.getCPEHWMP())?Utils.parseDouble(response.getCPEHWMP()):0.0);//CPE Outright Sale NRC List Price
			double cpeRentalMrc = (Objects.nonNull(response.getCPERentalMRC()) ?Utils.parseDouble(response.getCPERentalMRC()):0.0);
			multiSitePricingAttributes.setCpeRentalArcListPrice(cpeRentalMrc * 12);//CPE Rental ARC List Price
			multiSitePricingAttributes.setCpeManagementArcListPrice(Objects.nonNull(response.getCPEManagementINR())?Utils.parseDouble(response.getCPEManagementINR()):0.0);
			
			//macd
			if((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(response.getQuotetypeQuote())) {
				multiSitePricingAttributes.setPortArcListPrice(Objects.nonNull(response.getPortArcLP())?Utils.parseDouble(response.getPortArcLP()):0.0);
				Double lmNrcBwOnrf = Objects.nonNull(response.getLmNrcBwOnrf())?Utils.parseDouble(response.getLmNrcBwOnrf()):0.0;
				Double lmNrcBwOnwl = Objects.nonNull(response.getLmNrcBwOnwl())?Utils.parseDouble(response.getLmNrcBwOnwl()):0.0;
				Double lmNrcBwProvOfrf = Objects.nonNull(response.getLmNrcBwProvOfrf())?Utils.parseDouble(response.getLmNrcBwProvOfrf()):0.0;
				Double lmNrcListPrice = lmNrcBwOnrf + lmNrcBwOnwl + lmNrcBwProvOfrf;
				multiSitePricingAttributes.setLastMileNrcListPrice(lmNrcListPrice);
				
				Double lmArcBwOnwl = Objects.nonNull(response.getLmArcBwOnwl())?Utils.parseDouble(response.getLmArcBwOnwl()):0.0;
				Double lmArcBwOnrf  = Objects.nonNull(response.getLmArcBwOnrf())?Utils.parseDouble(response.getLmArcBwOnrf()):0.0;
				Double lmArcBwProvOfrf = Objects.nonNull(response.getLmArcBwProvOfrf())?Utils.parseDouble(response.getLmArcBwProvOfrf()):0.0;
				Double lmArcListPrice = lmArcBwOnwl + lmArcBwOnrf + lmArcBwProvOfrf;
				multiSitePricingAttributes.setLastMileArcListPrice(lmArcListPrice);
				
				Double lmNrcMastOnrf = Objects.nonNull(response.getLmNrcMastOnrf())?Utils.parseDouble(response.getLmNrcMastOnrf()):0.0;
				Double lmNrcMastOfrf = Objects.nonNull(response.getLmNrcMastOfrf())?Utils.parseDouble(response.getLmNrcMastOfrf()):0.0;
				Double mastNrcListPriceMacd = lmNrcMastOnrf + lmNrcMastOfrf;
				multiSitePricingAttributes.setMastNrcListPrice(mastNrcListPriceMacd);
			}
	}catch (TclCommonException e) {
		LOGGER.error("Error in GVPN getListPriceDetails",e);				
		throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,
				ResponseResource.R_CODE_ERROR);
		}
	}
}
		LOGGER.info("Exiting getListPriceDetails in GvpnQuoteService");
	return multiSitePricingAttributes;
	}


	/**
	 *
	 * @param illSite
	 * @param orderSite
	 * @param quoteIllSiteServices
	 */
	private void constructOrderIllSiteToServiceTermination(QuoteIllSite illSite, OrderIllSite orderSite,List<QuoteIllSiteToService> quoteIllSiteServices) {
		try {
			LOGGER.info("Inside IllQuoteService.constructOrderIllSiteToServiceTermination to save orderIllSiteService for termination for orderSiteId {} ", orderSite.getId());
			List<OrderIllSiteToService> orderIllSiteToServices = new ArrayList<>();
			Quote quote = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote();

			if(!quoteIllSiteServices.isEmpty()) {
				quoteIllSiteServices.stream().forEach(quoteSiteService->{
					LOGGER.info("Setting orderIllSiteTOService for siteId  {} and QuoteLe  {}", quoteSiteService.getQuoteIllSite().getId(), quoteSiteService.getQuoteToLe().getId());
					OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
					orderIllSiteToService.setErfServiceInventoryParentOrderId(quoteSiteService.getErfServiceInventoryParentOrderId());
					orderIllSiteToService.setErfServiceInventoryServiceDetailId(quoteSiteService.getErfServiceInventoryServiceDetailId());
					orderIllSiteToService.setErfServiceInventoryTpsServiceId(quoteSiteService.getErfServiceInventoryTpsServiceId());

					orderIllSiteToService.setOrderIllSite(orderSite);
					orderIllSiteToService.setTpsSfdcParentOptyId(quoteSiteService.getTpsSfdcParentOptyId());
					orderIllSiteToService.setType(quoteSiteService.getType());
					orderIllSiteToService.setAllowAmendment(quoteSiteService.getAllowAmendment());
					orderIllSiteToService.setErfSfdcOrderType(quoteSiteService.getErfSfdcOrderType());
					orderIllSiteToService.setErfSfdcSubType(quoteSiteService.getErfSfdcSubType());
					orderIllSiteToService.setTpsSfdcOptyId(quoteSiteService.getTpsSfdcOptyId());
					orderIllSiteToService.setTpsSfdcProductId(quoteSiteService.getTpsSfdcProductId());
					orderIllSiteToService.setTpsSfdcProductName(quoteSiteService.getTpsSfdcProductName());

					orderIllSiteToService.setOrderToLe(orderSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe());
					if(Objects.nonNull(quoteSiteService.getQuoteIllSite().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getIsAmended())) {
						int result = Byte.compare(BACTIVE, quoteSiteService.getQuoteIllSite().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getIsAmended());
						if (result==0) {
							orderIllSiteToService.setAllowAmendment(quoteSiteService.getAllowAmendment());
							orderIllSiteToService.setParentSiteId(Objects.nonNull(quoteSiteService.getParentSiteId()) ? quoteSiteService.getParentSiteId() : -1);
							orderIllSiteToService.setParentOrderId(Objects.nonNull(quoteSiteService.getParentOrderId()) ? quoteSiteService.getParentOrderId() : -1);
							orderIllSiteToService.setServiceType(Objects.nonNull(quoteSiteService.getServiceType()) ? quoteSiteService.getServiceType() : "NA");
							orderIllSiteToService.setO2cServiceId(Objects.nonNull(quoteSiteService.getO2cServiceId()) ? quoteSiteService.getO2cServiceId() : "NA");
						}
					}

					String iasQueueResponse = null;
					try {
						iasQueueResponse = (String) mqUtils.sendAndReceive(siRelatedDetailsInactiveQueue, quoteSiteService.getErfServiceInventoryTpsServiceId());

						if (StringUtils.isNotBlank(iasQueueResponse)) {
							LOGGER.info("queue response from si in constructOrderIllSiteToServiceTermination {}", iasQueueResponse);
							SIServiceInfoBean[] siDetailedInfoResponseIAS = (SIServiceInfoBean[]) Utils.convertJsonToObject(iasQueueResponse,
									SIServiceInfoBean[].class);
							List<SIServiceInfoBean> siServiceInfoResponse = Arrays.asList(siDetailedInfoResponseIAS);
							Optional<SIServiceInfoBean> siServiceInfoBeanOne = siServiceInfoResponse.stream().filter(service -> service.getTpsServiceId().equals(quoteSiteService.getErfServiceInventoryTpsServiceId())).findFirst();
							orderIllSiteToService.setErfServiceInventoryO2cLinkType(siServiceInfoBeanOne.get().getPrimaryOrSecondary());
							if(Objects.nonNull(siServiceInfoResponse) && siServiceInfoResponse.size() > 1) {
								LOGGER.info("more than one record in response from queue");
								Optional<SIServiceInfoBean> siServiceInfoBean = siServiceInfoResponse.stream().filter(service -> !service.getTpsServiceId().equals(quoteSiteService.getErfServiceInventoryTpsServiceId())).findFirst();
								if(siServiceInfoBean.isPresent()) {
									orderIllSiteToService.setErfServiceInventoryPriSecLinkServiceId(siServiceInfoBean.get().getTpsServiceId());
								}
							}
						}

					} catch (Exception e) {
						LOGGER.error("error in queue call siRelatedDetailsQueue in constructOrderIllSiteToServiceTermination {}", e);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}

					orderIllSiteToServices.add(orderIllSiteToService);
					orderIllSiteToServiceRepository.save(orderIllSiteToService);
					LOGGER.info("After Saving OrderIllSiteToService in IllQuoteService.constructOrderIllSiteToServiceTermination");
					QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(quoteSiteService);
					constructOrderSiteServiceTerminationDetails (quoteSiteServiceTerminationDetail, orderIllSiteToService);

				});

				LOGGER.info("Inside IllQuoteService.constructOrderIllSiteToServiceTermination Saved orderillSiteToServicesss");
			}
		} catch(Exception e) {
			LOGGER.error("Exception occured while saving orderIllSiteToServices {} ", e);
		}
	}

	private void constructOrderSiteServiceTerminationDetails(QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetails, OrderIllSiteToService orderIllSiteToService){
		LOGGER.info("Inside method constructOrderSiteServiceTerminationDetails for quoteSiteServiceTerminationDetails_id {} and order site to sevice id {}",
				quoteSiteServiceTerminationDetails.getId(), orderIllSiteToService.getId());

		if(Objects.nonNull(quoteSiteServiceTerminationDetails) && Objects.nonNull(orderIllSiteToService)){
			LOGGER.info("OrderIllSiteToService is not NULL");
			OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetail = new OrderSiteServiceTerminationDetails();
			orderSiteServiceTerminationDetail.setOrderIllSiteToService(orderIllSiteToService);
			orderSiteServiceTerminationDetail.setOrderToLeId(orderIllSiteToService.getOrderToLe().getId());
			orderSiteServiceTerminationDetail.setEffectiveDateOfChange(quoteSiteServiceTerminationDetails.getEffectiveDateOfChange());
			orderSiteServiceTerminationDetail.setCustomerMailReceivedDate(quoteSiteServiceTerminationDetails.getCustomerMailReceivedDate());
			orderSiteServiceTerminationDetail.setRequestedDateForTermination(quoteSiteServiceTerminationDetails.getRequestedDateForTermination());
			orderSiteServiceTerminationDetail.setTermInMonths(quoteSiteServiceTerminationDetails.getTermInMonths());
			orderSiteServiceTerminationDetail.setSubReason(quoteSiteServiceTerminationDetails.getSubReason());
			orderSiteServiceTerminationDetail.setReasonForTermination(quoteSiteServiceTerminationDetails.getReasonForTermination());
			orderSiteServiceTerminationDetail.setCommunicationReceipient(quoteSiteServiceTerminationDetails.getCommunicationReceipient());
			orderSiteServiceTerminationDetail.setLocalItContactName(quoteSiteServiceTerminationDetails.getLocalItContactName());
			orderSiteServiceTerminationDetail.setLocalItContactNumber(quoteSiteServiceTerminationDetails.getLocalItContactNumber());
			orderSiteServiceTerminationDetail.setLocalItContactEmailId(quoteSiteServiceTerminationDetails.getLocalItContactEmailId());
			orderSiteServiceTerminationDetail.setInternalCustomer(quoteSiteServiceTerminationDetails.getInternalCustomer());
			orderSiteServiceTerminationDetail.setCustomerEmailConfirmationErfCusAttachmentId(quoteSiteServiceTerminationDetails.getCustomerEmailConfirmationErfCusAttachmentId());
			orderSiteServiceTerminationDetail.setEtcApplicable(quoteSiteServiceTerminationDetails.getEtcApplicable());
			orderSiteServiceTerminationDetail.setHandoverTo(quoteSiteServiceTerminationDetails.getHandoverTo());
			orderSiteServiceTerminationDetail.setCsmNonCsmName(quoteSiteServiceTerminationDetails.getCsmNonCsmName());
			orderSiteServiceTerminationDetail.setCsmNonCsmEmail(quoteSiteServiceTerminationDetails.getCsmNonCsmEmail());
			orderSiteServiceTerminationDetail.setCsmNonCsmContactNumber(quoteSiteServiceTerminationDetails.getCsmNonCsmContactNumber());
			orderSiteServiceTerminationDetail.setTerminationSubtype(quoteSiteServiceTerminationDetails.getTerminationSubtype());
			orderSiteServiceTerminationDetail.setTerminatedParentOrderCode(quoteSiteServiceTerminationDetails.getTerminatedParentOrderCode());
			orderSiteServiceTerminationDetail.setTerminationSendToTdDate(quoteSiteServiceTerminationDetails.getTerminationSendToTdDate());
			orderSiteServiceTerminationDetail.setSalesTaskResponse(quoteSiteServiceTerminationDetails.getSalesTaskResponse());
			User user = getUserId(Utils.getSource());
			orderSiteServiceTerminationDetail.setCreatedBy(user.getId());
			orderSiteServiceTerminationDetail.setCreatedTime(new Date());

			orderSiteServiceTerminationDetailsRepository.save(orderSiteServiceTerminationDetail);


		}
	}



	public String findIsCommercial(Optional<QuoteToLe> quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe.get());
		Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
				.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
						.equalsIgnoreCase(LeAttributesConstants.IS_COOMERCIAL))
				.findFirst();
		return customerCodeLeVal.get().getAttributeValue();
	}
	
	public void updateDemoQuoteLeAttributes(QuoteToLe quoteToLe) {
		if (quoteToLe.getIsDemo() != null && quoteToLe.getIsDemo().equals(CommonConstants.BACTIVE)
				&& "free".equalsIgnoreCase(quoteToLe.getDemoType())) {
			updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.BILLING_TYPE, "Non-billable demo");
			updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.BILLING_FREQUENCY, "NA");
			updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.BILLING_METHOD, "NA");
			updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.PAYMENT_TERM, "NA");

		} else if (quoteToLe.getIsDemo() != null && quoteToLe.getIsDemo().equals(CommonConstants.BACTIVE)
				&& "paid".equalsIgnoreCase(quoteToLe.getDemoType())) {
			updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.BILLING_TYPE, "Billable demo");
			updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.BILLING_FREQUENCY, "One Time");
		}
	}

	public void updateLeAttribute(QuoteToLe quoteTole, String userName, String name, String value) {
		MstOmsAttribute mstOmsAttribute = null;
		if (name != null) {
			List<MstOmsAttribute> mstOmsAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(name,
					(byte) 1);

			if (!mstOmsAttributesList.isEmpty()) {
				mstOmsAttribute = mstOmsAttributesList.get(0);
			}
			if (mstOmsAttribute == null) {
				mstOmsAttribute = new MstOmsAttribute();
				mstOmsAttribute.setCreatedBy(userName);
				mstOmsAttribute.setCreatedTime(new Date());
				mstOmsAttribute.setIsActive((byte) 1);
				mstOmsAttribute.setName(name);
				mstOmsAttribute.setDescription(value);
				mstOmsAttributeRepository.save(mstOmsAttribute);
			}

			constructLegaAttribute(mstOmsAttribute, quoteTole, name, value);
		}

	}
	
	/**
	 * This method used to saveOrderSiteBillingDetails
	 * 
	 * @param order, quoteSiteBillingDetails
	 * @return MultiSiteBillingInfoBean
	 */
	public void saveOrderSiteBillingDetails(Order order,List<QuoteSiteBillingDetails> quoteSiteBillingDetails) {
		LOGGER.info("enter into saveOrderSiteBillingDetails"+order.getOrderCode());
		try {
			if(order!=null && !quoteSiteBillingDetails.isEmpty()) {
				for(QuoteSiteBillingDetails quoteSite:quoteSiteBillingDetails) {
					List<OrderIllSite> orderSite=new ArrayList<OrderIllSite>();
					if(quoteSite.getQuoteIllSite()!=null) {
						 orderSite=orderIllSitesRepository.findBySiteCodeAndStatus(quoteSite.getQuoteIllSite().getSiteCode(), (byte)1);
					}
					LOGGER.info("ORDER SITE LIST SIZE"+orderSite.size());
					if (!orderSite.isEmpty()) {
						OrderSiteBillingDetails orderSiteBillingDetails = new OrderSiteBillingDetails();
						orderSiteBillingDetails.setOrderId(order.getId());
						orderSiteBillingDetails.setOrderCode(order.getOrderCode());
						orderSiteBillingDetails.setCity(quoteSite.getCity());
						orderSiteBillingDetails.setContactId(quoteSite.getContactId());
						orderSiteBillingDetails.setCusLeId(quoteSite.getCusLeId());
						orderSiteBillingDetails.setEtcCusBillingInfoId(quoteSite.getEtcCusBillingInfoId());
						orderSiteBillingDetails.setGstNo(quoteSite.getGstNo());
						orderSiteBillingDetails.setCreatedBy(Utils.getSource());
						orderSiteBillingDetails.setCreatedDate(new Date());
						orderSiteBillingDetails.setState(quoteSite.getState());
						orderSiteBillingDetails.setProductName(quoteSite.getProductName());
						orderSiteBillingDetails.setOrderIllSite(orderSite.get(0));
						orderSiteBillingInfoRepository.save(orderSiteBillingDetails);
					}
					
				}
			}
			
		} catch (Exception e) {
			LOGGER.warn("error in saveOrderSiteBillingDetails", e.toString());
		}
		
	}
	
	/**
	 *updateGstNumber used to update
	 * GST number in multisitebillinginfo table
	 * @param gstno
	 * @param sitecode
	 */
	private void updateGstNumber(String gstNo, QuoteIllSite quoteIllSite) {
		LOGGER.info("ENTER into updateGstNumber::" + gstNo);
		try {
			if (gstNo != null && quoteIllSite != null) {
				QuoteSiteBillingDetails billinginfo = quoteSiteBillingInfoRepository.findByQuoteIllSite(quoteIllSite);
				if (billinginfo != null) {
					LOGGER.info("inside if multisitebillinginfo id:::" + billinginfo.getId());
					billinginfo.setGstNo(gstNo);
					quoteSiteBillingInfoRepository.save(billinginfo);
				}
			}

		} catch (Exception ex) {
			LOGGER.info("Error in updateGstNumber in illoredrservice : {}", ex);
		}

	}
	
	/**
	 * Method to save utility audit
	 * @param quoteCode
	 * @param toValue
	 * @param fromValue
	 * @param utilityType
	 * @author archchan
	 */
	public void saveUtilityAudit(String quoteCode, String toValue, String fromValue, String utilityType) {
		try {
			LOGGER.info("Entering saveUtilityAudit for quote code {} utility type {}", quoteCode, utilityType);
			UtilityAudit utilityAudit = new UtilityAudit();
			utilityAudit.setMdcToken(MDC.get(CommonConstants.MDC_TOKEN_KEY));
			utilityAudit.setQuoteCode(quoteCode);
			utilityAudit.setToValue(toValue);
			utilityAudit.setUtilityType(utilityType);
			utilityAudit.setFromValue(fromValue);
			utilityAudit.setUpdatedBy(Utils.getSource());
			utilityAudit.setUpdatedTime(new Date());
			LOGGER.info("utilityAudit {} ",utilityAudit.toString());
			utilityAuditRepository.save(utilityAudit);
		} catch(Exception e) {
			LOGGER.error("Exception while saving utility audit for quote code {}   utilityType {}  fromValue {} and error {} ", quoteCode,utilityType,  fromValue, e);
		}
	}

	/**
	 * Method to save feasibility and pricing payload audit
	 * @param quoteCode
	 * @param requestPayload
	 * @param responsePayload
	 * @param auditType
	 * @author manisham
	 */
	public void saveFeasibilityPricingPayloadAudit(String quoteCode, String requestPayload, String responsePayload, String auditType) {
		try {
			LOGGER.info("Entering save feasibility pricing payload for quote code {} utility type {}", quoteCode, auditType);
			FeasibilityPricingPayloadAudit feasibilityPricingPayloadAudit = new FeasibilityPricingPayloadAudit();
			feasibilityPricingPayloadAudit.setMdcToken(MDC.get(CommonConstants.MDC_TOKEN_KEY));
			feasibilityPricingPayloadAudit.setQuoteCode(quoteCode);
			feasibilityPricingPayloadAudit.setRequestPayload(requestPayload);
			feasibilityPricingPayloadAudit.setAuditType(auditType);
			feasibilityPricingPayloadAudit.setResponsePayload(responsePayload);
			feasibilityPricingPayloadAudit.setCreatedBy(Utils.getSource());
			feasibilityPricingPayloadAudit.setCreatedTime(new Date());
			LOGGER.info("feasibilityPricingPayloadAudit {} ",feasibilityPricingPayloadAudit.toString());
			feasibilityPricingPayloadAuditRepository.save(feasibilityPricingPayloadAudit);
		} catch(Exception e) {
			LOGGER.error("Exception while saving feasibilityPricingPayload audit for quote code {}   auditType {}  requestPayload {} and error {} ", quoteCode,auditType,  requestPayload, e);
		}
	}
}
