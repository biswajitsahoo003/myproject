package com.tcl.dias.oms.izopc.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.GVPN;
import static com.tcl.dias.common.constants.CommonConstants.IZOPC;
import static com.tcl.dias.common.constants.CommonConstants.MACD;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.oms.constants.MACDConstants.MACD_QUOTE_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY;

import java.sql.Timestamp;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.CustomerLeAccountManagerDetails;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MSABean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceProviderLegalBean;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.OrderSummaryBeanResponse;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.utils.AuditMode;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MACDExistingComponentsBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.QuoteFamilySVBean;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteLeSVBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.beans.QuoteSummaryBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.SiteDetail;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.dto.QuoteDelegationDto;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderLinkStageAudit;
import com.tcl.dias.oms.entity.entities.OrderLinkStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderNplLinkSla;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteAddress;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteAccessPermission;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SiteFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteAddressRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteProvisioningRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteAccessPermissionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteDifferentialCommercialRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.SlaMasterRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.izopc.beans.ProductSolutionBean;
import com.tcl.dias.oms.izopc.beans.QuoteBean;
import com.tcl.dias.oms.izopc.beans.QuoteDetail;
import com.tcl.dias.oms.izopc.beans.QuoteToLeBean;
import com.tcl.dias.oms.izopc.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.izopc.beans.SolutionDetail;
import com.tcl.dias.oms.izopc.macd.service.v1.IzopcMACDService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.SLABean;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.validator.services.IzoPcCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains all IZOPC related services
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

/**
 * @author Dinahar Vivekanandan
 *
 */

@Service
@Transactional
public class IzoPcQuoteService {

	public static final Logger LOGGER = LoggerFactory.getLogger(IzoPcQuoteService.class);

	@Autowired
	OrderIllSitesRepository orderNplSitesRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	QuoteAccessPermissionRepository quoteAccessPermissionRepository;

	@Autowired
	protected MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	protected OrderRepository orderRepository;
	
	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;

	@Autowired
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	protected IllSiteRepository illSiteRepository;
	
	@Autowired
	MACDUtils macdUtils;

	@Autowired
	protected ProductSolutionRepository productSolutionRepository;

	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
	
	@Value("${rabbitmq.si.service.details.list.queue}")
	String siServiceDetailsListQueue;

	@Autowired
	OrderSiteProvisioningRepository ordersiteProvsioningRepository;

	@Autowired
	protected OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	OrderIllSiteSlaRepository orderNplSiteSlaRepository;

	@Autowired
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;

	@Autowired
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;
	
	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;

	@Autowired
	protected QuotePriceRepository quotePriceRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	protected DocusignAuditRepository docusignAuditRepository;

	@Autowired
	OrderNplLinkRepository orderNplLinkRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	NplLinkRepository nplLinkRepository;

	@Autowired
	SlaMasterRepository slaMasterRepository;

	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;

	@Autowired
	protected OmsSfdcService omsSfdcService;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	QuoteNplLinkSlaRepository nplLinkSlaRepository;

	@Autowired
	protected MQUtils mqUtils;

	@Autowired
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	protected OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderSiteStatusAuditRepository orderSiteStatusAuditRepository;

	@Autowired
	OrderSiteStageAuditRepository orderSiteStageAuditRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	IzoPcPricingFeasibilityService izopcPricingFeasibilityService;

	@Autowired
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@Autowired
	protected CofDetailsRepository cofDetailsRepository;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.izopc.sla.queue}")
	String productSlaQueue;

	@Value("${rabbitmq.location.details.feasibility}")
	protected String locationDetailQueue;

	@Autowired
	protected QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	OrderLinkStatusAuditRepository orderLinkStatusAuditRepository;

	@Autowired
	OrderLinkStageAuditRepository orderLinkStageAuditRepository;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${notification.mail.template}")
	String delegationTemplateId;

	@Value("${notification.mail.loginurl}")
	String loginUrl;

	@Value("${rabbitmq.service.provider.detail}")
	String spQueue;

	@Value("${rabbitmq.customerleattr.queue}")
	String customerleattrQueue;

	@Value("${rabbitmq.customerleattr.product.queue}")
	protected String customerLeAttrQueueProduct;

	@Value("${info.customer_le_location_queue}")
	String customerLeLocationQueue;

	@Value("${rabbitmq.customer.le.update.msa}")
	String updateMSAQueue;

	@Autowired
	EngagementRepository engagementRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	protected IzoPcQuotePdfService izoPcQuotePdfService;

	@Autowired
	IzoPcCofValidatorService izoPcCofValidatorService;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Value("${pilot.team.email}")
	String[] pilotTeamMail;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;
	
	@Value("${customer.support.email}")
	String customerSupportEmail;
	
	@Value("${rabbitmq.customer.le.account.mananger}")
	String customerLeAccountMangeQueue;
	
	@Value("${cust.get.segment.attribute}")
	String customerSegment;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;

	@Autowired
	IzopcMACDService izopcMACDService;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	@Value("${rabbitmq.service.provider.izosdwan}")
	String spQueueIzosdwan;
	/**
	 * 
	 * getUserId-This method get the user details if present or persist the user and
	 * get the entity
	 * 
	 * @param userData
	 * @return User
	 * @throws TclCommonException
	 */
	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * @param erfCustomerId
	 * @return
	 * @throws TclCommonException
	 *             Method to create quote
	 */
	public QuoteResponse createQuote(QuoteDetail quoteDetail, Integer erfCustomerId) throws TclCommonException {

		if (quoteDetail == null || Objects.isNull(erfCustomerId)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
		QuoteResponse response = new QuoteResponse();
		try {
			User user = getUserId(Utils.getSource());
			QuoteToLe quoteTole = processQuote(quoteDetail, erfCustomerId, user);
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());
				// Triggering Sfdc Creation
				// TODO: TO be enabled post SFDC integration

				if (quoteDetail.getQuoteId() == null) {
					omsSfdcService.processCreateOpty(quoteTole, quoteDetail.getProductName());
				}

			}
			//processQuoteAccessPermissions(user, quoteTole);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * Method to update quote to le status
	 * 
	 * @param quoteToLeId
	 * @param status
	 * @return
	 * @throws TclCommonException
	 */
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
	 * 
	 * processQuote- This method builds the quote workflow step by step it creates
	 * by providing the initial set of values
	 * 
	 * @param quoteDetail
	 * @param customerId
	 * @return Quote
	 * @throws TclCommonException
	 */
	protected QuoteToLe processQuote(QuoteDetail quoteDetail, Integer erfCustomerId, User user)
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
			quote = constructQuote(customer, user.getId(), quoteDetail.getProductName());
			quoteRepository.save(quote);
		} else {
			quote = quoteRepository.findByIdAndStatus(quoteDetail.getQuoteId(), CommonConstants.BACTIVE);
		}
		QuoteToLe quoteToLe = null;
		if (quoteDetail.getQuoteId() == null) {
			quoteToLe = constructQuoteToLe(quote);
			quoteToLeRepository.save(quoteToLe);
		} else {
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			quoteToLe = quoteToLeEntity.isPresent() ? quoteToLeEntity.get() : null;
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
		createSolutionDetails(quoteDetail, quoteToLe, productFamily, user, quoteToLeProductFamily);

		return quoteToLe;

	}

	/**
	 * createSolutionDetails - method to create solution details
	 * 
	 * @param quoteDetail
	 * @param quoteToLe
	 * @param quoteToLeProductFamily
	 * @param user
	 * @param productFamily
	 */
	private void createSolutionDetails(QuoteDetail quoteDetail, QuoteToLe quoteToLe, MstProductFamily productFamily,
			User user, QuoteToLeProductFamily quoteToLeProductFamily) {
		quoteDetail.getSolutions().forEach(solution -> {
			try {
				String productOffering = solution.getOfferingName();
				String productSolutionId = solution.getSolutionCode();
				final ProductSolution[] productSolution = new ProductSolution[1];
				ProductSolution solVariable = null;
				if (quoteToLe.getQuoteType() != null && quoteToLe.getQuoteType().equalsIgnoreCase("MACD")) {
					LOGGER.info("into macd flow");
					List<ProductSolution> exprodSolutions = productSolutionRepository
							.findByQuoteToLeProductFamily(quoteToLeProductFamily);
					LOGGER.info("size {}",exprodSolutions.size());
					for (ProductSolution exprodSolution : exprodSolutions) {
						solVariable = exprodSolution;
					}
				} else {

					if (Objects.nonNull(productSolutionId)) {
						Optional<ProductSolution> prodSolOptional = productSolutionRepository
								.findById(Integer.valueOf(productSolutionId));

						if (prodSolOptional.isPresent()) {
							solVariable = prodSolOptional.get();
						}
					}
				}
				productSolution[0] = solVariable;
				MstProductOffering productOfferng = getProductOffering(productFamily, productOffering, user);
				Set<QuoteIllSite> illSiteSet = new HashSet<>();
				if (Objects.isNull(productSolution[0])) {
					productSolution[0] = constructProductSolution(productOfferng, quoteToLeProductFamily,
							Utils.convertObjectToJson(solution));
					productSolutionRepository.save(productSolution[0]);
					
					// create sites under the solution
					List<SiteDetail> siteDetails = solution.getSiteDetail();
					siteDetails.forEach(siteDetail -> {
						try {
							illSiteSet.add(constructIllSites(productSolution[0], user, siteDetail, productFamily));
						} catch (Exception e) {
							throw new TclCommonRuntimeException(e.getMessage(), e);
						}
					});
					productSolution[0].setQuoteIllSites(illSiteSet);

					if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())
							&& !quoteToLe.getTpsSfdcOptyId().equals("-1")) {
						omsSfdcService.processProductServiceForSolution(quoteToLe, productSolution[0],
								quoteToLe.getTpsSfdcOptyId());// adding productService // to Sfdc
					}

					if (quoteToLe.getStage().equals(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode())) {
						quoteToLe.setStage(QuoteStageConstants.ADD_LOCATIONS.getConstantCode());
						quoteToLeRepository.save(quoteToLe);

					}
				} else {
					// overwrite the pre-existing solution with new data
					productSolution[0].setProductProfileData(Utils.convertObjectToJson(solution));
					productSolutionRepository.save(productSolution[0]);
					List<SiteDetail> siteDetails = solution.getSiteDetail();
					siteDetails.forEach(siteDetail -> {
						if (siteDetail.getSiteId() != null) {
							Optional<QuoteIllSite> quoteIllSiteOptional = illSiteRepository
									.findById(siteDetail.getSiteId());
							QuoteIllSite quoteIllSite = quoteIllSiteOptional.get();
							quoteIllSite.setProductSolution(productSolution[0]);
							illSiteRepository.save(quoteIllSite);
							removeComponentsAndAttr(quoteIllSite.getId());
							siteDetail.getComponents().forEach(componentDetail -> {
								try {
									processProductComponent(productFamily, quoteIllSite, componentDetail, user);
								} catch (Exception e) {
									throw new TclCommonRuntimeException(e.getMessage(), e);
								}

							});
						} else {
							try {
								QuoteIllSite site=constructIllSites(productSolution[0], user, siteDetail, productFamily);
								siteDetail.setSiteId(site.getId());
							} catch (TclCommonException e) {
								throw new TclCommonRuntimeException(e.getMessage(), e);
							}
						}
					});

				}

				// code Block to remove solutions in case of delete configuration usage
				List<ProductSolution> solutions = productSolutionRepository
						.findByQuoteToLeProductFamilyAndMstProductOffering_Id(quoteToLeProductFamily,
								productFamily.getId());
				List<SolutionDetail> solDetails = quoteDetail.getSolutions();

				solutions.forEach(sol -> {
					solDetails.forEach(detail -> {
						if (Objects.nonNull(detail.getSolutionCode())
								&& !sol.getId().equals(detail.getSolutionCode())) {
							removeSolution(sol);
						}
					});
				});
			} catch (Exception e) {
				throw new TclCommonRuntimeException(e.getMessage(), e);
			}

		});

	}

	/**
	 * Method to remove a product solution
	 * 
	 * @param productSolution
	 */
	private void removeSolution(ProductSolution productSolution) {
		List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
				CommonConstants.BACTIVE);
		illSites.forEach(quoteIllSite -> {
			removeComponentsAndAttr(quoteIllSite.getId());
			illSiteRepository.delete(quoteIllSite);
		});
		productSolutionRepository.delete(productSolution);
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
	public QuoteIllSite constructIllSites(ProductSolution productSolution, User user, SiteDetail siteDetail,
			MstProductFamily productFamily) throws TclCommonException {
		if (Objects.isNull(productSolution) || Objects.isNull(user) || Objects.isNull(siteDetail)
				|| Objects.isNull(productFamily)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		QuoteIllSite createdSite = null;
		SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);

		if (siteDetail.getSiteId() == null) {
			QuoteIllSite illSite = new QuoteIllSite();
			illSite.setErfLocSiteaLocationId(siteDetail.getSecondLocationId());
			illSite.setErfLocSiteaSiteCode(siteDetail.getSecondLocationCode());
			illSite.setErfLocSitebLocationId(siteDetail.getLocationId());
			illSite.setErfLocSitebSiteCode(siteDetail.getLocationCode());
			illSite.setProductSolution(productSolution);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 14); // Adding 14 days
			illSite.setEffectiveDate(cal.getTime());
			illSite.setCreatedBy(user.getId());
			illSite.setCreatedTime(new Date());
			illSite.setStatus((byte) 1);
			illSite.setImageUrl(soDetail.getImage());
			illSite.setSiteCode(Utils.generateUid());
			illSite.setFeasibility((byte) 0);
			illSite.setIsIzoPc((byte) 1);
			illSite.setQuoteIllSiteSlas(constructSiteSlas(illSite));
			createdSite = illSiteRepository.save(illSite);
			siteDetail.setSiteId(illSite.getId());
			siteDetail.getComponents().forEach(componentDetail -> {
				processProductComponent(productFamily, illSite, componentDetail, user);
			});
		} else {
			QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(siteDetail.getSiteId(), (byte) 1);
			if (illSiteEntity != null) {
				illSiteEntity.setProductSolution(productSolution);
				createdSite = illSiteRepository.save(illSiteEntity);
				removeComponentsAndAttr(illSiteEntity.getId());
				siteDetail.getComponents().forEach(componentDetail -> {
					processProductComponent(productFamily, illSiteEntity, componentDetail, user);
				});
			}
		}

		return createdSite;
	}

	private Set<QuoteIllSiteSla> constructSiteSlas(QuoteIllSite illSite) throws TclCommonException {
		Set<QuoteIllSiteSla> slas = new HashSet<>();
		try {
			String response = (String) mqUtils.sendAndReceive(productSlaQueue, "");
			LOGGER.info("Output Payload for IZOPC sla ", response);
			if (StringUtils.isNotBlank(response)) {
				TypeReference<Set<SLABean>> mapType = new TypeReference<Set<SLABean>>() {
				};
				ObjectMapper objectMapper = new ObjectMapper();
				Set<SLABean> slaValues = objectMapper.readValue(response, mapType);

				for (SLABean slaDetail : slaValues) {
					QuoteIllSiteSla sla = new QuoteIllSiteSla();
					sla.setQuoteIllSite(illSite);
					sla.setSlaMaster(slaMasterRepository.findBySlaName(slaDetail.getFactor()));
					sla.setSlaValue(slaDetail.getValue());
					quoteIllSiteSlaRepository.save(sla);
					slas.add(sla);
				}

			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(e.getMessage(), e);
		}
		return slas;

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
			ComponentDetail component, User user) {

		MstProductComponent productComponent = getProductComponent(component, user);
		QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily,
				illSite.getId());
		quoteComponent.setType(component.getType());
		quoteProductComponentRepository.save(quoteComponent);
		LOGGER.info("saved successfully");
		Set<QuoteProductComponentsAttributeValue> attributeValueSet = new HashSet<>();
		component.getAttributes().forEach(attribute -> {
			attributeValueSet.add(processProductAttribute(quoteComponent, attribute, user));
		});
		quoteComponent.setQuoteProductComponentsAttributeValues(attributeValueSet);

	}

	/**
	 * processProductAttribute- This method process the product attributes
	 * 
	 * @param quoteComponent
	 * @param attribute
	 * @param user
	 * @throws TclCommonException
	 */
	private QuoteProductComponentsAttributeValue processProductAttribute(QuoteProductComponent quoteComponent, AttributeDetail attribute, User user) {
		ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
		QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
				productAttribute, attribute);
		return quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
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
	private static QuoteProductComponentsAttributeValue constructProductAttribute(
			QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster,
			AttributeDetail attributeDetail) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;

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
	
	private ProductAttributeMaster getProductAttributes(String  attrName) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attrName, (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attrName);
			productAttributeMaster.setDescription(attrName);
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(Utils.getSource());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
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
	protected static QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer illSiteId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(QuoteConstants.IZO_PC_SITES.toString());
		return quoteProductComponent;

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
	 * removeComponentsAndAttr - method to remove components and attributes
	 * 
	 * @param siteId
	 */
	private void removeComponentsAndAttr(Integer siteId) {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,QuoteConstants.IZO_PC_SITES.toString());
		// List<QuoteProductComponent> quoteProductComponents =
		// quoteProductComponentRepository.findByReferenceIdAndType(siteId,CommonConstants.IZOPC);
		// component fix
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
			throw new TclCommonException(ExceptionConstants.CUSTOMER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return customer;

	}

	/**
	 * 
	 * constructQuote-This method constructs quote entity
	 * 
	 * @param customer
	 * @param userId
	 * @return Quote
	 */
	private static Quote constructQuote(Customer customer, Integer userId, String prodName) {
		Quote quote = new Quote();
		quote.setCustomer(customer);
		quote.setCreatedBy(userId);
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setQuoteCode(Utils.generateRefId(prodName));
		return quote;
	}

	/**
	 * constructQuoteToLe - constructs quoteToLe object
	 * 
	 * @param quote
	 * @return
	 */
	private static QuoteToLe constructQuoteToLe(Quote quote) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		quoteToLe.setCurrencyCode("INR");
		quoteToLe.setQuoteType("NEW");
		quoteToLe.setTermInMonths("12 months");
		quoteToLe.setIsAmended(CommonConstants.BDEACTIVATE);
		quoteToLe.setIsMultiCircuit(CommonConstants.BDEACTIVATE);
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

	private static QuoteToLeProductFamily constructQuoteToLeProductFamily(MstProductFamily mstProductFamily,
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

	private static ProductSolution constructProductSolution(MstProductOffering mstProductOffering,
			QuoteToLeProductFamily quoteToLeProductFamily, String productProfileData) {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setMstProductOffering(mstProductOffering);
		productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		productSolution.setProductProfileData(productProfileData);
		productSolution.setSolutionCode(Utils.generateUid());
		return productSolution;
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
	 * removeUnselectedSolution-remove unselected solution
	 * 
	 * @param quoteDetail
	 * @param quoteToLeProductFamily
	 */
	private void removeUnselectedSolution(QuoteDetail quoteDetail, QuoteToLeProductFamily quoteToLeProductFamily,
			QuoteToLe quoteToLe) {
		List<ProductSolution> exprodSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		exprodSolutions.forEach(exproductSolution -> {
			final boolean remove[] = { true };
			quoteDetail.getSolutions().forEach(solution -> {
				if (solution.getOfferingName().equals(exproductSolution.getMstProductOffering().getProductName())) {
					remove[0] = false;
					return;
				}
			});
			if (remove[0]) {
				exproductSolution.getQuoteIllSites().forEach(illSites -> {
					removeComponentsAndAttr(illSites.getId());
					deletedIllsiteAndRelation(illSites);
				});
				// Trigger delete productSolution

				if (StringUtils.isNotEmpty(exproductSolution.getTpsSfdcProductId()))
					omsSfdcService.processDeleteProduct(quoteToLe, exproductSolution);

				productSolutionRepository.delete(exproductSolution);
			}

		});
	}

	/**
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
			siteFeasibilities.forEach(site -> siteFeasibilityRepository.delete(site));
		}

		illSiteRepository.delete(quoteIllSite);

	}

	/**
	 * persistQuoteLeAttributes - saves the quoteToLe attributes in database
	 * 
	 * @param user
	 * @param quoteTole
	 * @throws TclCommonException
	 */
	protected void persistQuoteLeAttributes(User user, QuoteToLe quoteTole) {
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_NAME, user.getFirstName());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_EMAIL, user.getEmailId());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_ID, user.getUsername());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_NO, user.getContactNo());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.DESIGNATION, user.getDesignation());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.RECURRING_CHARGE_TYPE, "ARC");
		updateLeAttribute(quoteTole, user, LeAttributesConstants.LE_STATE_GST_NO, "");

	}

	/**
	 * updateConstactInfo - updates quoteToLe attribute
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
	 * updateLeAttrbute - updates Legal attributes
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ getQuoteDetails- This method is used
	 *       to get the quote details
	 * 
	 * @param quoteId
	 * @param version
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public QuoteBean getQuoteDetails(Integer quoteId, String feasibleSites,Boolean isSiteProp) throws TclCommonException {
		QuoteBean response = null;
		try {
			if (Objects.isNull(quoteId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString())) ? true : false;
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote, isFeasibleSites,isSiteProp,null);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 *             Method to get quote details
	 */
	protected Quote getQuote(Integer quoteId) throws TclCommonException {

		Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quote == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}

		return quote;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @throws TclCommonException
	 *             Method to construct quote details
	 */
	protected QuoteBean constructQuote(Quote quote, Boolean isFeasibleSites,Boolean isSiteProp, Boolean manualFeasibility) throws TclCommonException {
		QuoteBean quoteDto = new QuoteBean();
		quoteDto.setQuoteId(quote.getId());
		quoteDto.setQuoteCode(quote.getQuoteCode());
		quoteDto.setCreatedBy(quote.getCreatedBy());
		quoteDto.setCreatedTime(quote.getCreatedTime());
		quoteDto.setStatus(quote.getStatus());
		quoteDto.setTermInMonths(quote.getTermInMonths());
		if (quote.getCustomer() != null) {
			quoteDto.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		}
		quoteDto.setLegalEntities(constructQuoteLeEntitDtos(quote, isFeasibleSites,isSiteProp,manualFeasibility));

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
		return quoteDto;

	}

	/**
	 * @author Dinahar Vivekanandan
	 * @param publicIp
	 * @return Method to get public IP
	 */
	public static String getPublicIp(String publicIp) {
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 * @param quote
	 * @throws TclCommonException
	 *             Method to construct quoteToLe entity DTOs
	 */
	private Set<QuoteToLeBean> constructQuoteLeEntitDtos(Quote quote, Boolean isFeasibleSites,Boolean isSiteProp, Boolean manualFeasibility)
			throws TclCommonException {

		Set<QuoteToLeBean> quoteToLeDtos = new HashSet<>();
		if ((quote != null) && (getQuoteToLeBasenOnVersion(quote)) != null) {
			getQuoteToLeBasenOnVersion(quote).forEach(quTle -> {
				QuoteToLeBean quoteToLeDto = new QuoteToLeBean(quTle);
				quoteToLeDto.setLegalAttributes(constructLegalAttributes(quTle));
				try {
					quoteToLeDto.setProductFamilies(
							constructQuoteToLeFamilyDtos(getProductFamilyBasenOnVersion(quTle), isFeasibleSites,isSiteProp,manualFeasibility));
					quoteToLeDto.setCurrency(quTle.getCurrencyCode());
					quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
				} catch (Exception e) {
					throw new TclCommonRuntimeException(e.getMessage(), e);

				}
				quoteToLeDtos.add(quoteToLeDto);

			});
		}

		return quoteToLeDtos;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructQuoteToLeFamilyDtos
	 * @param quoteToLeProductFamilies
	 * @throws TclCommonException
	 *             constructs quote to family DTOs
	 */
	private Set<QuoteToLeProductFamilyBean> constructQuoteToLeFamilyDtos(
			List<QuoteToLeProductFamily> quoteToLeProductFamilies, Boolean isFeasibleSites,Boolean isSiteProp, Boolean manualFeasibility) throws TclCommonException {
		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilyBeans = new HashSet<>();
		if (quoteToLeProductFamilies != null) {
			quoteToLeProductFamilies.forEach(quFamily -> {
				try {
					QuoteToLeProductFamilyBean quoteToLeProductFamilyBean = new QuoteToLeProductFamilyBean();
					if (quFamily.getMstProductFamily() != null) {
						quoteToLeProductFamilyBean.setStatus(quFamily.getMstProductFamily().getStatus());
						quoteToLeProductFamilyBean.setProductName(quFamily.getMstProductFamily().getName());
					}
					List<ProductSolutionBean> solutionBeans = getSortedSolution(
							constructProductSolution(getProductSolutionBasenOnVersion(quFamily), isFeasibleSites,isSiteProp,manualFeasibility));
					quoteToLeProductFamilyBean.setSolutions(solutionBeans);
					quoteToLeProductFamilyBeans.add(quoteToLeProductFamilyBean);
				} catch (Exception e) {
					throw new TclCommonRuntimeException(e.getMessage(), e);

				}
			});

		}

		return quoteToLeProductFamilyBeans;
	}

	/**
	 * Method to sort the solution beans
	 * 
	 * @param solutionBeans
	 * @return
	 */
	private static List<ProductSolutionBean> getSortedSolution(List<ProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(ProductSolutionBean::getProductSolutionId));
		}

		return solutionBeans;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 * @param productSolutions
	 * @return Set<ProductSolutionBean>
	 * @throws TclCommonException
	 *             Method to construct product solutions
	 */
	private List<ProductSolutionBean> constructProductSolution(List<ProductSolution> productSolutions,
			Boolean isFeasibleSites,Boolean isSiteProp, Boolean manualFeasibility) throws TclCommonException {
		List<ProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			productSolutions.forEach(solution -> {
				ProductSolutionBean productSolutionBean = new ProductSolutionBean();
				productSolutionBean.setProductSolutionId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					productSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					productSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					productSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}
				if (solution.getProductProfileData() != null) {
					try {
						productSolutionBean.setSolution((SolutionDetail) Utils
								.convertJsonToObject(solution.getProductProfileData(), SolutionDetail.class));
					} catch (Exception e) {
						throw new TclCommonRuntimeException(e.getMessage(), e);
					}
				}
				List<QuoteIllSiteBean> illSiteBeans = getSortedIllSiteDtos(
						constructIllSiteDtos(getIllsitesBasenOnVersion(solution), isFeasibleSites,isSiteProp,manualFeasibility));
				productSolutionBean.setSites(illSiteBeans);
				productSolutionBeans.add(productSolutionBean);

			});
		}
		return productSolutionBeans;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean> Method to sort IllsiteDtos
	 */
	private static List<QuoteIllSiteBean> getSortedIllSiteDtos(List<QuoteIllSiteBean> illSiteBeans) {
		if (illSiteBeans != null) {
			illSiteBeans.sort(Comparator.comparingInt(QuoteIllSiteBean::getSiteId));

		}

		return illSiteBeans;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean> Method to construct IllsiteDtos
	 */
	private List<QuoteIllSiteBean> constructIllSiteDtos(List<QuoteIllSite> illSites, Boolean isFeasibleSites,Boolean isSiteProp,Boolean manualFeasibility) {
		if(isSiteProp==null) {
			isSiteProp=false;
		}
		final Boolean sitePropFlag = isSiteProp;
		List<QuoteIllSiteBean> sites = new ArrayList<>();
		if (illSites != null) {
			//illSites.forEach(illSite -> {
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
							constructQuoteProductComponent(illSite.getId(), false,sitePropFlag));
					illSiteBean.setComponents(quoteProductComponentBeans);

				   	if(quoteToLe.getQuoteType() != null && quoteToLe.getQuoteType().equalsIgnoreCase("MACD")) {
				   		illSiteBean
						.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe, illSiteBean,manualFeasibility)));
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
									/*
									 * //add rejection flow
									 * if(illSite.getCommercialRejectionStatus().equalsIgnoreCase("1")) {
									 * illSiteBean.setRejectionStatus(true); } else {
									 * illSiteBean.setRejectionStatus(false); }
									 */
								illSiteBeanSecondary.setMfStatus(illSite.getMfStatus());
								if(quoteToLe.getQuoteType() != null && quoteToLe.getQuoteType().equalsIgnoreCase("MACD")) {
									illSiteBeanSecondary
									.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe, illSiteBean, manualFeasibility)));
								}
								sites.add(illSiteBeanSecondary);

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
						
						


						sites.add(illSiteBean);
				}
			}
		}
		return sites;
	}

	private List<MACDExistingComponentsBean> generateExistingComponentsForMacd(QuoteToLe quoteToLe, QuoteIllSiteBean illSiteBean, Boolean manualFeasibility)
	{
		List<MACDExistingComponentsBean> existingComponentsBeanList=new ArrayList<>();
		List<String> serviceIdsList=new ArrayList<>();

		Optional<QuoteIllSite> quoteIllSite=illSiteRepository.findById(illSiteBean.getSiteId());
		Map<String,String> serviceIdsMap=macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite.get(),quoteToLe);
		if(Objects.nonNull(serviceIdsMap)) {
			String primaryServiceId = serviceIdsMap.get(PDFConstants.PRIMARY);
			String secondaryServiceId = serviceIdsMap.get(PDFConstants.SECONDARY);
			if (Objects.nonNull(primaryServiceId))
				serviceIdsList.add(primaryServiceId);
			if (Objects.nonNull(secondaryServiceId))
				serviceIdsList.add(secondaryServiceId);
		}
		LOGGER.info("ServiceIdsList"+serviceIdsList);
		if(!serviceIdsList.isEmpty())
		{
			serviceIdsList.stream().forEach(serviceId->{
				try {
					MACDExistingComponentsBean existingComponent = new MACDExistingComponentsBean();
					//order Id need to be removed
					List<Map> existingComponentMap = constructExistingComponentsforIsvPage(quoteToLe, illSiteBean, serviceId,manualFeasibility);
					existingComponent.setServiceId(serviceId);
					existingComponent.setExistingComponents(existingComponentMap);
					existingComponentsBeanList.add(existingComponent);
				}
				catch(Exception e)
				{
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}
			});

		}
		return existingComponentsBeanList;
	}
	
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

		if (Objects.nonNull(quoteToLe) && MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
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


			if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)){
				primaryServiceDataBean.getAttributes().stream().filter(attr-> "Demo Period in days".equalsIgnoreCase(attr.getName())).findFirst().ifPresent(
						attribute->{
							LOGGER.info("Setting Contract term for Demo");
							primaryComponentsMap.replace("contractTerm", primaryComponentsMap.get("contractTerm"), attribute.getValue()+"days");
						}
				);
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


				if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)){
					secondaryaryServiceDataBean.getAttributes().stream().filter(attr-> "Demo Period in days".equalsIgnoreCase(attr.getName())).findFirst().ifPresent(
							attribute->{
								LOGGER.info("Setting Contract term for Demo Secondary");
								secondaryComponentsMap.replace("contractTerm", secondaryComponentsMap.get("contractTerm"), attribute.getValue()+"days");
							}
					);
				}
				
				existingComponentsList.add(1, secondaryComponentsMap);

			}

		}
		return existingComponentsList;
	}
	
	
	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean> Method to sort components
	 */
	private List<QuoteProductComponentBean> getSortedComponents(List<QuoteProductComponentBean> quoteComponentBeans) {
		if (quoteComponentBeans != null) {
			quoteComponentBeans.sort(Comparator.comparingInt(QuoteProductComponentBean::getComponentId));

		}

		return quoteComponentBeans;
	}

	/**
	 * @author Dinahar V
	 * @param isSitePropertiesNeeded
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 *             Method to get components based on version
	 */

	private List<QuoteProductComponent> getComponentBasenOnVersion(Integer siteId, boolean isSitePropertiesNeeded,boolean isSiteProp) {
		List<QuoteProductComponent> components = null;
		if (isSitePropertiesNeeded) {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.IZO_PC_SITES.toString());
		}else if(isSiteProp) {
			components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,QuoteConstants.IZO_PC_SITES.toString());
		}
		else {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductFamily_Name(siteId,
					CommonConstants.IZOPC);
			if (components != null) {
				return components.stream()
						.filter(cmp -> (!cmp.getMstProductComponent().getName()
								.equals(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties())))
						.collect(Collectors.toList());
			}

		}

		return components;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent
	 * @param id,version
	 *            Method to construct components for the quote product
	 */
	private List<QuoteProductComponentBean> constructQuoteProductComponent(Integer id, boolean isSitePropertiesNeeded,Boolean isSiteProp) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenOnVersion(id, isSitePropertiesNeeded,isSiteProp);

		if (productComponents != null) {
			productComponents.forEach(quoteProductComponent -> {
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
						constructAttribute(
								getAttributeBasenOnVersion(quoteProductComponent.getId(), isSitePropertiesNeeded,isSiteProp)));
				quoteProductComponentBean.setAttributes(attributeValueBeans);
				quoteProductComponentDtos.add(quoteProductComponentBean);
			});

		}
		return quoteProductComponentDtos;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean> Method to sort attribute components
	 */
	private static List<QuoteProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<QuoteProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(QuoteProductComponentsAttributeValueBean::getAttributeId));

		}

		return attributeBeans;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com constructAttribute used to construct
	 *       attribute
	 * @param quoteProductComponentsAttributeValues
	 * @return
	 */
	private List<QuoteProductComponentsAttributeValueBean> constructAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues) {
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean = new ArrayList<>();
		if (quoteProductComponentsAttributeValues != null) {
			quoteProductComponentsAttributeValues.forEach(attributeValue -> {

				QuoteProductComponentsAttributeValueBean qtAttributeValue = null;
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
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
			});
		}

		return quoteProductComponentsAttributeValueBean;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @constructAttributePriceDto used to construct attribute price
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 *             Method to get attributes based on version
	 */
	private List<QuoteProductComponentsAttributeValue> getAttributeBasenOnVersion(Integer componentId,
			boolean isSitePropRequire,Boolean isSiteProp) {
		List<QuoteProductComponentsAttributeValue> attributes = null;

		attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);

		if (isSitePropRequire) {
			attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentId,
							IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
		}else if(isSiteProp) {
			attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);
		}
		else {
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
	 * @author Dinahar V constructComponentPriceDto used to get price of component
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
	 * constructSiteFeasibility - constructs site feasibility beans
	 * 
	 * @param illSite
	 * @return
	 */
	private List<SiteFeasibilityBean> constructSiteFeasibility(QuoteIllSite illSite) {
		List<SiteFeasibilityBean> siteFeasibilityBeans = new ArrayList<>();
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(illSite,
				(byte) 1);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibilities
					.forEach(siteFeasibility -> siteFeasibilityBeans.add(constructSiteFeasibility(siteFeasibility)));
		}
		return siteFeasibilityBeans;
	}

	/**
	 * constructSiteFeasibility - constructs site feasibility beans
	 * 
	 * @param siteFeasibility
	 * @return
	 */
	private static SiteFeasibilityBean constructSiteFeasibility(SiteFeasibility siteFeasibility) {
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
	 * constructSlaDetails - method to construct SLA details
	 * 
	 * @param illSite
	 * @return
	 */
	private static List<QuoteSlaBean> constructSlaDetails(QuoteIllSite illSite) {

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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 *             Method to get ill sites based on version
	 */
	private List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution) {
		List<QuoteIllSite> illsites = null;

		illsites = illSiteRepository.findByProductSolutionIdAndStatus(productSolution.getId(), (byte) 1);
		return illsites;

	}

	/**
	 * @author Dinahar Vivekanandan
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 *             Method to get solution based on version
	 */
	private List<ProductSolution> getProductSolutionBasenOnVersion(QuoteToLeProductFamily family) {
		List<ProductSolution> productSolutions = null;
		productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(family);
		return productSolutions;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 *             Method to get quote to le based on version
	 */
	protected List<QuoteToLe> getQuoteToLeBasenOnVersion(Quote quote) {
		List<QuoteToLe> quToLes = null;
		quToLes = quoteToLeRepository.findByQuote(quote);
		return quToLes;

	}

	/**
	 * @author Dinahar V
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 *             Retrieve product family based on version
	 */
	private List<QuoteToLeProductFamily> getProductFamilyBasenOnVersion(QuoteToLe quote) {
		List<QuoteToLeProductFamily> prodFamilys = null;
		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		return prodFamilys;

	}

	/**
	 * constructMstAttributBean
	 * 
	 * @param mstOmsAttribute
	 * @return Method to construct master attribute bean
	 */
	private static MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ editSiteComponent used to edit site
	 *       component values
	 * @param request
	 * @return
	 */
	public QuoteDetail editSiteComponent(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			quoteDetail = new QuoteDetail();
			validateRequest(request);
			request.getComponentDetails().forEach(cmpDetail -> {
				cmpDetail.getAttributes().forEach(attributeDetail -> {
					QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = null;
					if (attributeDetail.getAttributeId() == null) {
						quoteProductComponentsAttributeValue = updateProductAttribute(request, cmpDetail,
								attributeDetail, quoteProductComponentsAttributeValue);
					} else {
						quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValueRepository
								.findById(attributeDetail.getAttributeId()).get();
					}
					if (quoteProductComponentsAttributeValue != null) {
						quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
						quoteProductComponentsAttributeValueRepository
								.save(quoteProductComponentsAttributeValue);
					}
				});
			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * validateRequest validates the update request object for proper input
	 * 
	 * @param request
	 */
	protected void validateRequest(UpdateRequest request) throws TclCommonException {
		if (Objects.isNull(request) || request.getComponentDetails() == null
				|| request.getComponentDetails().isEmpty()) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

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
	private QuoteProductComponentsAttributeValue updateProductAttribute(UpdateRequest request,
			ComponentDetail cmpDetail, AttributeDetail attributeDetail,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
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
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteEntity.get().getId(), cmpDetail.getName(),QuoteConstants.IZO_PC_SITES.toString());
			QuoteProductComponent quoteProductComponent = null;
			if (prodComponent.isEmpty()) {
				List<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findByNameAndStatus(cmpDetail.getName(), CommonConstants.BACTIVE);
				if (!mstProductComponent.isEmpty()) {
					quoteProductComponent = new QuoteProductComponent();
					quoteProductComponent.setMstProductComponent(mstProductComponent.get(0));
					quoteProductComponent.setMstProductFamily(
							siteEntity.get().getProductSolution().getQuoteToLeProductFamily().getMstProductFamily());
					quoteProductComponent.setReferenceId(siteEntity.get().getId());
					quoteProductComponent.setType(cmpDetail.getType());
					quoteProductComponentRepository.save(quoteProductComponent);
					if (!attributeDetail.getValue().isEmpty()) {
						saveProductAttributevalue(productAttributeMaster, attributeDetail, quoteProductComponent,
								quoteProductComponentsAttributeValue);
					}
				}
			} else {
				quoteProductComponent = prodComponent.get(0);
			}
			if (!prodComponent.isEmpty()) {

				saveProductAttributevalue(productAttributeMaster, attributeDetail, quoteProductComponent,
						quoteProductComponentsAttributeValue);
			}
		}
		return quoteProductComponentsAttributeValue;
	}

	/**
	 * New attribute values get inserted/updated into the table while editing the
	 * configuration
	 * 
	 * @param productAttributeMaster
	 * @param attributeDetail
	 * @param quoteProductComponent
	 * @param quoteProductComponentsAttributeValue
	 */

	private void saveProductAttributevalue(ProductAttributeMaster productAttributeMaster,
			AttributeDetail attributeDetail, QuoteProductComponent quoteProductComponent,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties - Method to
	 *            map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	public QuoteDetail updateSiteProperties(UpdateRequest request) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			validateRequest(request);
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
			saveIllsiteProperties(quoteIllSite, request.getLocalITContactId(), user);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ saveIllsiteProperties - used to
	 *       saveillsite properties
	 * @param quoteIllSite
	 * @param localITContactId
	 * @param mstProductFamily
	 */
	private void saveIllsiteProperties(QuoteIllSite quoteIllSite, Integer localITContactId, User user) {
		MstProductComponent mstProductComponent = getMstProperties(user);
		constructIllSiteProperties(mstProductComponent, quoteIllSite, user.getUsername(), localITContactId);

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructMstProperties - used to
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructIllSitePropeties - used to
	 *       construct site properties
	 * @param mstProductComponent
	 * @param quoteIllSite
	 * @param username
	 * @param localITContactId
	 */
	private void constructIllSiteProperties(MstProductComponent mstProductComponent, QuoteIllSite quoteIllSite,
			String username, Integer localITContactId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(mstProductComponent);
		quoteProductComponent.setReferenceId(quoteIllSite.getId());
		quoteProductComponent.setReferenceName(QuoteConstants.IZO_PC_SITES.toString());
		quoteProductComponentRepository.save(quoteProductComponent);
		ProductAttributeMaster attributeMaster = getAttributePropertiesMaster(username);
		quoteProductComponent.setQuoteProductComponentsAttributeValues(
				constructIllSiteAtrribute(quoteProductComponent, localITContactId, attributeMaster));

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructAttributePropertiesMaster -
	 *       used to construct Attribute Master properties
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
	 * @author Dinahar V
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
	 * Method to get quote product components
	 * 
	 * @param siteId
	 * @return List<QuoteProductComponentBean>
	 * @throws TclCommonException
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
					constructQuoteProductComponent(quoteIllSite.getId(), true,false));

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteProductComponentBeans;
	}

	/**
	 * @param siteId
	 * @throws TclCommonException
	 */
	private void validateRequest(Integer siteId) throws TclCommonException {
		if (siteId == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		}
	}

	/**
	 * Method to process pdf as mail attachment
	 * 
	 * @param email
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public ServiceResponse processMailAttachment(String email, Integer quoteId) throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();
		if (Objects.isNull(email) || !Utils.isValidEmail(email) || Objects.isNull(quoteId)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String quoteHtml = izoPcQuotePdfService.processQuoteHtml(quoteId);
			String fileName = "Quote_" + quoteId + ".pdf";
			notificationService.processShareQuoteNotification(email,
					java.util.Base64.getEncoder().encodeToString(quoteHtml.getBytes()), userInfoUtils.getUserFullName(),
					fileName, CommonConstants.IZOPC);
			fileUploadResponse.setStatus(Status.SUCCESS);
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

	}

	/**
	 * @author Dinahar V Method to update the currency
	 * @param quoteId
	 * @param quoteToLeId
	 * @param inputCurrency
	 * @throws TclCommonException
	 */
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
				existingCurrency = findExistingCurrency(quoteToLe.get());
				if (Objects.isNull(existingCurrency)) {
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				} else if (!existingCurrency.equalsIgnoreCase(inputCurrency)) {
					Quote quote = quoteToLe.get().getQuote();
					updateQuoteToLeCurrencyValues(quote, inputCurrency, existingCurrency);
					updateQuoteIllSitesCurrencyValues(quoteToLe.get(), inputCurrency, existingCurrency);
					updateQuotePriceCurrencyValues(quote, inputCurrency, existingCurrency);
					updatePaymentCurrencyWithInputCurrency(quoteToLe.get(), inputCurrency);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	/**
	 * @author Dinahar V Method to update Payment Currency attribute as input
	 *         currency
	 * @param quoteToLeId
	 * @param inputCurrency
	 * @throws TclCommonException
	 */
	private void updatePaymentCurrencyWithInputCurrency(QuoteToLe quotele, String inputCurrency) {
		quotele.setCurrencyCode(inputCurrency);
		quoteToLeRepository.save(quotele);
	}

	/**
	 * @author Dinahar V Method to update quote price currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	private void updateQuotePriceCurrencyValues(Quote quote, String inputCurrency, String existingCurrency) {

		List<QuotePrice> quotePrices = quotePriceRepository.findByQuoteId(quote.getId());
		quotePrices.forEach(quotePrice -> {
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
		});
	}

	/**
	 * @author Dinahar V Method to update quoteIllSites currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	private void updateQuoteIllSitesCurrencyValues(QuoteToLe quoteToLe, String inputCurrency, String existingCurrency) {
		quoteToLe.getQuoteToLeProductFamilies().forEach(quoteLeProdFamily -> {
			quoteLeProdFamily.getProductSolutions().forEach(prodSolution -> {
				prodSolution.getQuoteIllSites().forEach(illSite -> {
					illSite.setArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getArc()));
					illSite.setMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getMrc()));
					illSite.setNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getNrc()));
					illSite.setTcv(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getTcv()));

					illSiteRepository.save(illSite);
				});

			});

		});
	}

	/**
	 * @author Dinahar V Method to update quoteToLe currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	private void updateQuoteToLeCurrencyValues(Quote quote, String inputCurrency, String existingCurrency) {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
		quoteToLes.forEach(quoteToLe -> {
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
		});
	}

	/**
	 * @author Dinahar V Method to find existing payment currency
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	private String findExistingCurrency(QuoteToLe quoteTole) throws TclCommonException {
		return quoteTole.getCurrencyCode();
	}

	/**
	 * @author Dinahar V
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
			MstOmsAttribute omsAttribute = getMstAttributeMaster(request, user);
			constructQuoteLeAttribute(request, omsAttribute, optionalQuoteToLe.get());

			if (Objects.isNull(optionalQuoteToLe.get().getQuoteCategory()) && optionalQuoteToLe.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
				optionalQuoteToLe.get().setStage(QuoteStageConstants.CHECKOUT.getConstantCode());
				quoteToLeRepository.save(optionalQuoteToLe.get());

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
	}

	/**
	 * @author Dinahar V
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
				if (Objects.isNull(optQuoteToLe.get().getQuoteCategory()) && optQuoteToLe.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
					optQuoteToLe.get().setStage(QuoteStageConstants.CHECKOUT.getConstantCode());
					quoteToLeRepository.save(optQuoteToLe.get());
				}
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return legalEntityAttributes;
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
	 * This method is to delete the product solution , sites and their corresponding
	 * relation.
	 * 
	 * @param solutionId
	 * @param quoteId
	 * @param action
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@Transactional
	public QuoteDetail deactivateSolutionAndSites(Integer solutionId, Integer quoteId, String action)
			throws TclCommonException {
		validateDeleteSites(solutionId, quoteId, action);
		QuoteDetail quoteDetail = null;
		try {
			quoteDetail = new QuoteDetail();
			Optional<ProductSolution> solutionOptional = productSolutionRepository.findById(solutionId);
			if (solutionOptional.isPresent()) {
				solutionOptional.get().getQuoteIllSites().stream().forEach(site -> {
					if (action.equals(QuoteConstants.DELETE.toString())) {
						removeComponentsAndAttr(site.getId());
						deletedIllsiteAndRelation(site);
					} else if (action.equals(QuoteConstants.DISABLE.toString())) {
						site.setStatus((byte) 0);
						illSiteRepository.save(site);
					}
				});

				QuoteToLe quoteToLe = solutionOptional.get().getQuoteToLeProductFamily().getQuoteToLe();

				if (action.equals(QuoteConstants.DELETE.toString())) {
					productSolutionRepository.delete(solutionOptional.get());
				}

				izopcPricingFeasibilityService.recalculateSites(quoteToLe.getId());
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;

	}

	/**
	 * processDeactivateSites -This method deletes or disables the illSites based on
	 * the action
	 * 
	 * @param siteId,
	 *            quoteId, action
	 * @return QuoteDetail
	 */
	@Transactional
	public QuoteDetail processDeactivateSites(Integer siteId, Integer quoteId, String action)
			throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			quoteDetail = new QuoteDetail();
			validateDeleteSites(siteId, quoteId, action);

			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			QuoteToLe quoteToLe = quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
			if (action.equals(QuoteConstants.DELETE.toString())) {
				removeComponentsAndAttr(siteId);
				deletedIllsiteAndRelation(quoteIllSite);
			} else if (action.equals(QuoteConstants.DISABLE.toString())) {
				quoteIllSite.setStatus((byte) 0);
				illSiteRepository.save(quoteIllSite);
			}

			// delete solution in case if no other site available under the solution post
			// deletion of site
			if (action.equals(QuoteConstants.DELETE.toString())
					&& illSiteRepository.countByProductSolution(quoteIllSite.getProductSolution()) == 0) {
				productSolutionRepository.delete(quoteIllSite.getProductSolution());
			}

			izopcPricingFeasibilityService.recalculateSites(quoteToLe.getId());

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;

	}

	private void validateDeleteSites(Integer solutionId, Integer quoteId, String action) throws TclCommonException {
		if (Objects.isNull(solutionId) || Objects.isNull(quoteId)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
		if (action == null || !(action.equals(QuoteConstants.DELETE.toString())
				|| action.equals(QuoteConstants.DISABLE.toString()))) {
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	/**
	 * checkQuoteLeFeasibility - this method checks the pricing and feasibility for
	 * the given quote legal entity id.
	 * 
	 * @author Paulraj Sundar checkQuoteLeFeasibility
	 * @param quoteLeId
	 * @return QuoteLeAttributeBean
	 */
	public QuoteLeAttributeBean checkQuoteLeFeasibility(Integer quoteLeId) {
		QuoteLeAttributeBean quoteLeAttributeBean = new QuoteLeAttributeBean();
		Optional<QuoteToLe> optQuoteToLe = quoteToLeRepository.findById(quoteLeId);
		if (optQuoteToLe.isPresent()) {
			quoteLeAttributeBean.setQuoteLegalEntityId(quoteLeId);
			// for (QuoteLeAttributeValue quoteLeAttribte :
			// optQuoteToLe.get().getQuoteLeAttributeValues()) {
			optQuoteToLe.get().getQuoteLeAttributeValues().stream().forEach(quoteLeAttribte -> {

				if (quoteLeAttribte.getMstOmsAttribute().getName()
						.equalsIgnoreCase(QuoteConstants.ISFEASIBLITYCHECKDONE.toString())) {
					quoteLeAttributeBean.setIsFeasibilityCheckDone(quoteLeAttribte.getAttributeValue());
				}

				else if (quoteLeAttribte.getMstOmsAttribute().getName()
						.equalsIgnoreCase(QuoteConstants.ISPRICINGCHECKDONE.toString())) {
					quoteLeAttributeBean.setIsPricingCheckDone(quoteLeAttribte.getAttributeValue());
				}
			});

		}
		return quoteLeAttributeBean;
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
	
	public CreateDocumentDto createDocumentWrapper(CreateDocumentDto documentDto) throws TclCommonException {
		return createDocument(documentDto,0);
	}


	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ createDocument this is used to
	 *       persist the customer and Supplier legal entity
	 * @param quoteId
	 * @return CreateDocumentDto
	 */
	@Transactional
	public CreateDocumentDto createDocument(CreateDocumentDto documentDto,int count) throws TclCommonException {
		try {
			validateDocumentRequest(documentDto);
			Quote quote = quoteRepository.findByIdAndStatus(documentDto.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			if (documentDto.getIllSitesIds() != null && !documentDto.getIllSitesIds().isEmpty()) {
				List<QuoteIllSite> illSites = illSiteRepository.findByIdInAndStatus(documentDto.getIllSitesIds(),
						(byte) 1);
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
			customerLeAttributeRequestBean.setProductName("IZOPC");
			LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
					Utils.convertObjectToJson(customerLeAttributeRequestBean));
			if (StringUtils.isNotEmpty(customerLeAttributes)) {
				updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
						CustomerLeDetailsBean.class), optionalQuoteLe.get());
			}
			ServiceProviderLegalBean spName = returnServiceProviderNameIzosdwan(documentDto.getSupplierLegalEntityId());
			if (spName != null) {
				processAccount(optionalQuoteLe.get(), spName.getEntityName(),
						LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
				processAccount(optionalQuoteLe.get(), spName.getContractingAddressId(),
						IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS);
			}
			QuoteToLe quoteToLe = optionalQuoteLe.get();
			quoteToLe.setErfCusCustomerLegalEntityId(documentDto.getCustomerLegalEntityId());
			quoteToLe.setErfCusSpLegalEntityId(documentDto.getSupplierLegalEntityId());
			if (Objects.isNull(quoteToLe.getQuoteCategory()) && quoteToLe.getStage().equals(QuoteStageConstants.CHECKOUT.getConstantCode()) ) {
				quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			}
			quoteToLeRepository.save(quoteToLe);
			CustomerDetail customerDetail = userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
			if (customerDetail != null && !customerDetail.getCustomerId().equals(quote.getCustomer().getId())) {
				Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
				if (customerEntity.isPresent()) {
					quote.setCustomer(customerEntity.get());
					quoteRepository.save(quote);
				}
			}
			processLocationDetailsAndSendToQueue(quoteToLe, quote.getCustomer().getErfCusCustomerId());
			if (Objects.isNull(quoteToLe.getQuoteCategory())) {
				omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
						SFDCConstants.VERBAL_AGREEMENT_STAGE.toString(), quoteToLe);
			}
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
				return createDocument(documentDto,count+1);	
			}else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		
			
		}

		return new CreateDocumentDto();
	}

	/**
	 * @author Dinahar V
	 * @param familyName
	 * @link http://www.tatacommunications.com/ processLocationDetailsAndSendToQueue
	 *       used to send location info
	 * @param quoteId
	 * @return CreateDocumentDto
	 */
	private void processLocationDetailsAndSendToQueue(QuoteToLe quoteToLe, Integer erfCustomerId) {
		try {
			CustomerLeLocationBean bean = constructCustomerLeAndLocation(quoteToLe, erfCustomerId);
			String request = Utils.convertObjectToJson(bean);
			LOGGER.info("MDC Filter token value in before Queue call processLocationDetailsAndSendToQueue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(customerLeLocationQueue, request);
		} catch (Exception e) {
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}

	}

	/**
	 * @author Dinahar V
	 * @param familyName
	 * @link http://www.tatacommunications.com/ method to construct legal entity and
	 *       location info
	 * @param quoteId
	 * @return CreateDocumentDto
	 */
	public CustomerLeLocationBean constructCustomerLeAndLocation(QuoteToLe quoteToLe, Integer erfCustomerId) {
		CustomerLeLocationBean customerLeLocationBean = new CustomerLeLocationBean();

		try {
			customerLeLocationBean.setErfCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
			customerLeLocationBean.setCustomerId(erfCustomerId);
			quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteProdFamilies -> {
				quoteProdFamilies.getProductSolutions().stream().forEach(prodSolutions -> {
					prodSolutions.getQuoteIllSites().stream().forEach(illSite -> {
						customerLeLocationBean.getLocationIds().add(illSite.getErfLocSitebLocationId());
					});
				});
			});
		} catch (Exception e) {
			// since it is and internal queue call so we are logging it only
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}

		return customerLeLocationBean;

	}

	/**
	 * Method to return service provider name
	 * 
	 * @param id
	 * @return
	 * @throws TclCommonException
	 */
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
	 * Method to get supplier details
	 * 
	 * @param supplierId
	 * @return
	 */
	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;
	}

	/**
	 * @author Dinahar V updateBillingInfoForSfdc Method to update billing info in
	 *         SFDC
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	protected void updateBillingInfoForSfdc(CustomerLeDetailsBean request, QuoteToLe quoteToLe)
			throws TclCommonException {
		try {
			construcBillingSfdcAttribute(quoteToLe, request);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	/**
	 * 
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited construcBillingSfdcAttribute used
	 *            to construct billing attributes
	 * @param quoteToLe
	 * @param request
	 * @param user
	 * @throws TclCommonException
	 */
	private void construcBillingSfdcAttribute(QuoteToLe quoteToLe, CustomerLeDetailsBean request)
			throws TclCommonException {
		if (request.getAttributes() != null) {
			request.getAttributes().forEach(billAttr -> {
				constructBillingAttribute(billAttr, quoteToLe);

			});
		}
		processAccount(quoteToLe, request.getAccounCuId(), LeAttributesConstants.ACCOUNT_CUID.toString());
		processAccount(quoteToLe, request.getAccountId(), LeAttributesConstants.ACCOUNT_NO18.toString());
		// processAccount(quoteToLe, String.valueOf(request.getBillingContactId()),
		// LeAttributesConstants.BILLING_CONTACT_ID.toString());
		processAccount(quoteToLe, request.getLegalEntityName(), LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
	}

	/**
	 * processAccountCuid used to process account details from customer mdm
	 * 
	 * @param quoteToLe
	 * @param request
	 * @param user
	 */
	private void processAccount(QuoteToLe quoteToLe, String attrValue, String attributeName) {
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
			attributeValue.setDisplayValue(attrValue);
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attributeName);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValueRepository.save(attributeValue);

		}
	}

	/**
	 * @author Dinahar V
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited constructBillingAttribute used to
	 *            construct billing attribute
	 * @param attribute
	 */
	private void constructBillingAttribute(Attributes attribute, QuoteToLe quoteToLe) {

		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getAttributeName());

		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			updateAttributes(attribute, quoteLeAttributeValues);

		} else {
			createAttribute(attribute, quoteToLe);

		}

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateAttributes used to update
	 *            billing attributes
	 * @param attribute
	 * @param quoteLeAttributeValues
	 */
	private void updateAttributes(Attributes attribute, List<QuoteLeAttributeValue> quoteLeAttributeValues) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository
				.findById(quoteLeAttributeValues.get(0).getQuoteToLe().getId());
		quoteLeAttributeValues.forEach(attr -> {
			if (!attr.getMstOmsAttribute().getName().equalsIgnoreCase("Payment Currency")
					&& !attr.getMstOmsAttribute().getName().equalsIgnoreCase("Billing Currency")
					&& !attr.getMstOmsAttribute().getName()
							.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY)) {

				if (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACD)
						&& attr.getDisplayValue().equalsIgnoreCase(BILLING_FREQUENCY)) {
					SIServiceDetailDataBean serviceDetail = null;
					// to save billing frequency value from inventory for MACD quotes
					if (Objects.nonNull(quoteToLe)) {
						if (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACD)) {
							List<String> serviceIds = macdUtils.getServiceIds(quoteToLe.get());
							String serviceId = "";
							if (Objects.nonNull(serviceIds) && !serviceIds.isEmpty()) {
								serviceId = serviceIds.stream().findFirst().get();
							}
							try {
								serviceDetail = macdUtils.getServiceDetail(serviceId,
										quoteToLe.get().getQuoteCategory());
							} catch (TclCommonException e) {
								LOGGER.info("Error in retrieving Service Info to update Billing Frequency");
							}
						}
					}
					if (Objects.nonNull(serviceDetail) && StringUtils.isNotBlank(serviceDetail.getBillingFrequency())) {
						LOGGER.info("Billing Frequency value {} to be in updated in quoteLeAttrValues ",
								serviceDetail.getBillingFrequency());
						attr.setAttributeValue(serviceDetail.getBillingFrequency());
						quoteLeAttributeValueRepository.save(attr);
					}
				} else {
					LOGGER.info("Attribute Name {} and Value {} to be in updated in quoteLeAttrValues ",
							attribute.getAttributeName(), attribute.getAttributeValue());
					attr.setAttributeValue(attribute.getAttributeValue());
					quoteLeAttributeValueRepository.save(attr);
				}
			}
		});
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited createAttribute used to create
	 *            billing attributes
	 * @param attribute
	 * @param quoteToLe
	 * @param user
	 */
	private void createAttribute(Attributes attribute, QuoteToLe quoteToLe) {
		QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
		attributeValue.setAttributeValue(attribute.getAttributeValue());
		attributeValue.setDisplayValue(attribute.getAttributeName());
		attributeValue.setQuoteToLe(quoteToLe);
		MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
		attributeValue.setMstOmsAttribute(mstOmsAttribute);
		quoteLeAttributeValueRepository.save(attributeValue);
	}

	/**
	 * Method to validate inputs
	 * 
	 * @param userId
	 * @param quoteId
	 * @throws TclCommonException
	 */
	private void validateTriggerInput(String userId, Integer quoteId) throws TclCommonException {
		if (userId == null || quoteId == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteId);
		if (!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
	}

	/**
	 * processForgotPassword- This method process the forgot password it gets the
	 * email as the input and generates an reset url The reset url will have an
	 * expiry and can be only accessed by token
	 * 
	 * @param TriggerEmailRequest,
	 *            ip address
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

				MailNotificationBean mailNotificationBeanCofDelegate = populateMailNotificationBeanCofDelegate(quoteToLe.get(),
						triggerEmailRequest, user, orderRefId, accManager);
				notificationService.cofCustomerDelegationNotification(mailNotificationBeanCofDelegate);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return triggerEmailResponse;
	}
	
	/**
	 * processQuoteAccessPermissions - this method is used to save the quote permission 
	 * - Quote Created by Customer not visible to Sales
	 * - Quote Created by Sales not visible to Customer
	 * @param user
	 * @param quoteTole
	 */
	public void processQuoteAccessPermissions(User user, QuoteToLe quoteTole) {
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
		quoteAccessPermission.setCreatedBy(Utils.getSource());
		quoteAccessPermission.setCreatedTime(new Date());
		quoteAccessPermission.setIsCustomerView(null);
		quoteAccessPermission.setIsSalesView(null);
		quoteAccessPermission.setProductFamilyId(prodFamilyId);
		quoteAccessPermission.setRefId(quoteTole.getQuote().getQuoteCode());
		quoteAccessPermission.setType("QUOTE");
		quoteAccessPermission.setUpdatedBy(Utils.getSource());
		quoteAccessPermission.setUpdatedTime(new Date());
		/*if (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
		} else if (user.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BDEACTIVATE);
		}*/
		quoteAccessPermissionRepository.save(quoteAccessPermission);
	}

	private MailNotificationBean populateMailNotificationBeanCofDelegate(QuoteToLe quoteToLe, TriggerEmailRequest triggerEmailRequest, User user, String orderRefId, String accManager) {
		MailNotificationBean mailNotificationBeanCofDelegate = new MailNotificationBean();
		mailNotificationBeanCofDelegate.setCustomerName(user.getFirstName());
		mailNotificationBeanCofDelegate.setUserEmail(triggerEmailRequest.getEmailId());
		mailNotificationBeanCofDelegate.setAccountManagerEmail(accManager);
		mailNotificationBeanCofDelegate.setOrderId(orderRefId);
		mailNotificationBeanCofDelegate.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBeanCofDelegate.setProductName(CommonConstants.IZOPC);
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBeanCofDelegate = populatePartnerClassification(quoteToLe, mailNotificationBeanCofDelegate);
		}
		return mailNotificationBeanCofDelegate;
	}


	private MailNotificationBean populateMailNotificationBean(String customerAccountName,String customerName, String userName,
															  String userContactNumber, String userEmail, String accountManagerEmail,
															  String orderRefId, String quoteLink, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setCustomerAccountName(customerAccountName);
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setUserContactNumber(userContactNumber);
		mailNotificationBean.setUserEmail(userEmail);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setQuoteLink(quoteLink);
		mailNotificationBean.setProductName(IZOPC);
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	private MailNotificationBean populatePartnerClassification(QuoteToLe quoteToLe, MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
					String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails()
					.stream().findAny().get().getLegalEntityName();

			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

			mailNotificationBean.setClassification(quoteToLe.getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		} catch (Exception e) {
			LOGGER.warn("Error while reading end customer name :: {}", e.getStackTrace());
		}
		return mailNotificationBean;
	}

	/**
	 * Method to get legal entity attributes
	 * 
	 * @param quoteTole
	 * @param attr
	 * @return
	 */
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
	 * updateContactInfo Method to update contact information
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

	/**
	 * Method to trigger email post getting email id
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
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
				mailNotificationBean.setProductName(CommonConstants.IZOPC);
				if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
					QuoteToLe quoteToLe = quoteEntity.get().getQuoteToLes().stream().
							findFirst().get();
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com getQuotesDetailsBasedOnCustomer used
	 *       to get Quote details based on customerId
	 * @return
	 */
	public List<QuoteBean> getQuotesDetailsBasedOnCustomer(Integer customerId) throws TclCommonException {
		final List<QuoteBean> quoteBeans = new ArrayList<>();
		;
		try {
			Customer customer = customerRepository.findByIdAndStatus(customerId, (byte) 1);
			if (customer == null) {
				throw new TclCommonException(ExceptionConstants.CUSTOMER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			customer.getQuotes().forEach(quote -> {
				QuoteBean bean;
				try {
					bean = constructQuote(quote, false,false,null);
					quoteBeans.add(bean);

				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(e.getMessage(), e);
				}

			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteBeans;
	}

	/**
	 * Method to persist list of quote le attributes
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public QuoteDetail persistListOfQuoteLeAttributes(UpdateRequest request) throws TclCommonException {
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

			List<AttributeDetail> attributeDetails = request.getAttributeDetails();
			for (AttributeDetail attribute : attributeDetails) {
				MstOmsAttribute mstOmsAttribute = null;
				List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
						.findByNameAndIsActive(attribute.getName(), (byte) 1);
				if (!mstOmsAttributeList.isEmpty()) {
					mstOmsAttribute = mstOmsAttributeList.get(0);
				}
				if (mstOmsAttribute == null) {
					mstOmsAttribute = new MstOmsAttribute();
					mstOmsAttribute.setCreatedBy(user.getUsername());
					mstOmsAttribute.setCreatedTime(new Date());
					mstOmsAttribute.setIsActive((byte) 1);
					mstOmsAttribute.setName(attribute.getName());
					mstOmsAttribute.setDescription("");
					mstOmsAttributeRepository.save(mstOmsAttribute);

				}

				saveLegalEntityAttributes(optionalQuoteToLe.get(), attribute, mstOmsAttribute);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}
	
	public QuoteDetail persistListOfQuoteLeAttribute(UpdateRequest request) throws TclCommonException {
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

				MstOmsAttribute mstOmsAttribute = null;
				List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
						.findByNameAndIsActive(request.getAttributeName(), (byte) 1);
				if (!mstOmsAttributeList.isEmpty()) {
					mstOmsAttribute = mstOmsAttributeList.get(0);
				}
				if (mstOmsAttribute == null) {
					mstOmsAttribute = new MstOmsAttribute();
					mstOmsAttribute.setCreatedBy(user.getUsername());
					mstOmsAttribute.setCreatedTime(new Date());
					mstOmsAttribute.setIsActive((byte) 1);
					mstOmsAttribute.setName(request.getAttributeName());
					mstOmsAttribute.setDescription("");
					mstOmsAttributeRepository.save(mstOmsAttribute);

				}
				AttributeDetail attribute =new AttributeDetail();
				attribute.setName(request.getAttributeName());
				attribute.setValue(request.getAttributeValue());;
				saveLegalEntityAttributes(optionalQuoteToLe.get(), attribute, mstOmsAttribute);


		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * saveLegalEntityAttributes Method to save le entity attributes
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
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setMstOmsAttribute(mstOmsAttribute);
				attrVal.setAttributeValue(attribute.getValue());
				attrVal.setDisplayValue(attribute.getName());
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeValueRepository.save(attrVal);
			});
		} else {
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setAttributeValue(attribute.getValue());
			quoteLeAttributeValue.setDisplayValue(attribute.getName());
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		}
	}

	/**
	 * @author Dinahar V approvedQuotes this method is used to map quote to order
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
				DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
				if (docusignAudit != null) {
					throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
				}
				order = constructOrder(quote, detail);
				detail.setOrderId(order.getId());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					omsSfdcService.processSiteDetails(quoteLe);
					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
					Boolean nat = (request.getCheckList() == null
							|| request.getCheckList().equalsIgnoreCase(CommonConstants.NO)) ? Boolean.FALSE
									: Boolean.TRUE;
					Map<String,String> cofObjectMapper=new HashMap<>();
					izoPcQuotePdfService.processCofPdf(quote.getId(), null, nat, true, quoteLe.getId(),cofObjectMapper);
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
					if (cofDetails != null) {
						cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
					}
					User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
					String userEmail=null;
					if(userRepo!=null) {
						userEmail=userRepo.getEmailId();
					}
					
					for (OrderToLe orderToLe : order.getOrderToLes()) {
						izoPcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),cofObjectMapper);
						break;
					}
					// Trigger orderMail
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
					List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
					for (QuoteDelegation quoteDelegation : quoteDelegate) {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					}
					uploadSSIfNotPresent(quoteLe);
					/**commented due to requirement change for 
					 * MSA mapping while optimus journey*/
					//uploadMSAIfNotPresent(quoteLe);
				}
			}

			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
					//Trigger ClosedBcr Process 
					/*try {
						String custId =quoteLe.getQuote().getCustomer().getErfCusCustomerId().toString();
			            String attribute = (String) mqUtils.sendAndReceive(customerSegment,
			                          custId,MDC.get(CommonConstants.MDC_TOKEN_KEY));
			            if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
			            	//need to add approverId instead of last null
					    omsSfdcService.processeClosedBcr(quote.getQuoteCode(), quoteLe.getTpsSfdcOptyId(), quoteLe.getCurrencyCode(), "India",attribute,"PB_SS",true,null,null);
					    LOGGER.info("Trigger closed bcr in IzopcQuoteService");
			            }
			            else {
			            	LOGGER.info("Failed Closed bcr request in izopcQuoteService customerAttribute/customerId is Empty");
			            }
					} catch (TclCommonException e) {
						
						LOGGER.warn("Problem in IzopcQuoteService Trigger Closed Bcr Request");
					
					}*/
				}
			}
			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileSite(quote);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
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
		nonFeasibleQuoteToLe.setQuote(nonFeasibleQuote);
		quoteToLeRepository.save(nonFeasibleQuoteToLe);
		return nonFeasibleQuoteToLe;
	}

	/**
	 * Method to clone quote for non feasible site
	 * 
	 * @param quote
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
	 * clonePricingDetails
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void clonePricingDetails(QuoteIllSite quoteIllSite, QuoteIllSite nonQuoteIllSite) {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(quoteIllSite.getSiteCode(),"Discount");
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
				.findByReferenceIdAndMstProductFamily_Name(quoteIllSite.getId(), CommonConstants.IZOPC);
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
			List<QuoteIllSite> sites = illSiteRepository.findByProductSolutionIdAndStatus(productSolution.getId(),
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

	/**
	 * getContactAttributeDetails Method to get contact details
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
					for (QuoteLeAttributeValue attrval : quToLe.getQuoteLeAttributeValues()) {

						if (attrval.getMstOmsAttribute().getName()
								.equals(LeAttributesConstants.CONTACT_ID.toString())) {
							attributeInfo.setUserId(attrval.getAttributeValue());

						} else if (attrval.getMstOmsAttribute().getName()
								.equals(LeAttributesConstants.CONTACT_NAME.toString())) {
							attributeInfo.setFirstName(attrval.getAttributeValue());

						} else if (attrval.getMstOmsAttribute().getName()
								.equals(LeAttributesConstants.CONTACT_EMAIL.toString())) {

							attributeInfo.setEmailId(attrval.getAttributeValue());

						} else if (attrval.getMstOmsAttribute().getName()
								.equals(LeAttributesConstants.DESIGNATION.toString())) {

							attributeInfo.setDesignation(attrval.getAttributeValue());

						} else if (attrval.getMstOmsAttribute().getName()
								.equals(LeAttributesConstants.CONTACT_NO.toString())) {
							attributeInfo.setContactNo(attrval.getAttributeValue());

						}
					}
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attributeInfo;
	}

	/**
	 * Method to construct MSA bean
	 * 
	 * @param customerLeId
	 * @return
	 */
	private MSABean constructMSABean(Integer customerLeId) {
		MSABean msaBean = new MSABean();
		msaBean.setCustomerLeId(customerLeId);
		msaBean.setDisplayName(LeAttributesConstants.MSA);
		msaBean.setIsMSAUploaded(true);
		msaBean.setName(LeAttributesConstants.MSA);
		msaBean.setProductName(SFDCConstants.IZOPC);
		return msaBean;
	}

	/**
	 * Uploads SSI document for LEs without SS document
	 * 
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
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

	/**
	 * @param customerLeId
	 * @return
	 */
	public ServiceScheduleBean constructServiceScheduleBean(Integer customerLeId) {
		ServiceScheduleBean serviceScheduleBean = new ServiceScheduleBean();
		serviceScheduleBean.setCustomerLeId(customerLeId);
		serviceScheduleBean.setDisplayName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setIsSSUploaded(true);
		serviceScheduleBean.setName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setProductName(SFDCConstants.IZOPC);
		return serviceScheduleBean;
	}

	/**
	 * Method to prcoess order e-mail
	 * 
	 * @param order
	 * @param quoteToLe
	 * @param attachmentPath
	 * @param userName
	 * @throws TclCommonException
	 */
	protected void processOrderMailNotification(Order order, QuoteToLe quoteToLe, Map<String,String> cofObjectMapper,String userEmail)
			throws TclCommonException {
		String emailId=userEmail!=null?userEmail:customerSupportEmail;
		String leMail = getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(), emailId,
				appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName, CommonConstants.IZOPC, quoteToLe);
		notificationService.newOrderSubmittedNotification(mailNotificationBean);
	}

	private MailNotificationBean populateMailNotifionSalesOrder(String accountManagerEmail, String orderRefId, String customerEmail,
																String provisioningLink, Map<String,String> cofObjectMapper, String fileName,String productName, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(provisioningLink);
		mailNotificationBean.setCofObjectMapper(cofObjectMapper);
		mailNotificationBean.setFileName(fileName);
		mailNotificationBean.setProductName(productName);
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
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
			OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
					.findByOrderRefUuid(orderRefUuid);
			if (orderConfirmationAudit == null) {
				orderConfirmationAudit = new OrderConfirmationAudit();
			}
			orderConfirmationAudit.setName(name);
			orderConfirmationAudit.setPublicIp(publicIp);
			orderConfirmationAudit.setOrderRefUuid(orderRefUuid);
			orderConfirmationAudit.setMode(AuditMode.CLICK_THROUGH.toString());
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * @author Dinahar V
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
	 * @author Dinahar V
	 * @param order
	 * @param detail
	 * @link http://www.tatacommunications.com/
	 */
	private Set<OrderToLe> getOrderToLeBasenOnVersion(Quote quote, Order order, QuoteDetail detail) {
		List<QuoteToLe> quToLes = null;
		Set<OrderToLe> orderToLes = null;

		quToLes = quoteToLeRepository.findByQuote(quote);
		if (quToLes != null) {
			orderToLes = new HashSet<>();
			for (QuoteToLe quoteToLe : quToLes) {
				OrderToLe orderToLe = new OrderToLe();
				orderToLe.setFinalMrc(quoteToLe.getFinalMrc());
				orderToLe.setFinalNrc(quoteToLe.getFinalNrc());
				orderToLe.setFinalArc(quoteToLe.getFinalArc());
				orderToLe.setOrder(order);
				orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
				orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
				orderToLe.setProposedArc(quoteToLe.getProposedArc());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
				orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
				orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
				orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
				orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
				orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
				orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
				orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
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
				orderToLeRepository.save(orderToLe);
				orderToLe.setOrdersLeAttributeValues(constructOrderToLeAttribute(orderToLe, quoteToLe));
				detail.getOrderLeIds().add(orderToLe.getId());
				orderToLe
						.setOrderToLeProductFamilies(getOrderProductFamilyBasenOnVersion(quoteToLe, orderToLe, detail));
				orderToLes.add(orderToLe);

			}

		}

		return orderToLes;

	}

	/**
	 * @author Dinahar V
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
					orderProductSolutionRepository.save(oSolution);
					oSolution.setOrderIllSites(constructOrderIllSite(quoteIllSites, oSolution, detail));
					orderProductSolution.add(oSolution);
				}

			}
		}

		return orderProductSolution;
	}

	/**
	 * @author Dinahar V
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
					orderSite.setIsIzoPc(illSite.getIsIzoPc());
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date()); // Now use today date.
					cal.add(Calendar.DATE, 14); // Adding 14 days
					orderSite.setEffectiveDate(cal.getTime());
					orderSite.setMrc(illSite.getMrc());
					orderSite.setFpStatus(illSite.getFpStatus());
					orderSite.setArc(illSite.getArc());
					orderSite.setTcv(illSite.getTcv());
					orderSite.setSiteCode(illSite.getSiteCode());
					orderSite.setStage(SiteStagingConstants.CONFIGURE_SITES.getStage());
					orderSite.setNrc(illSite.getNrc());
					orderSite.setSiteBulkUpdate(illSite.getSiteBulkUpdate());
					orderIllSitesRepository.save(orderSite);
					persistOrderSiteAddress(illSite.getErfLocSitebLocationId(), "b",String.valueOf(orderSite.getId()),QuoteConstants.IZO_PC_SITES.toString());//Site
					persistOrderSiteAddress(illSite.getErfLocSiteaLocationId(), "a",String.valueOf(orderSite.getId()),QuoteConstants.IZO_PC_SITES.toString());//Pop
					orderSite.setOrderSiteFeasibility(constructOrderSiteFeasibility(illSite, orderSite));
					orderSite.setOrderIllSiteSlas(constructOrderSiteSla(illSite, orderSite));
					QuoteToLe quoteToLe = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					String quoteType = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType();
					 LOGGER.info("quoteToLe quote type {}",quoteType );
					if(quoteType != null && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteType)){
						LOGGER.info("constructOrderIllSiteToService for Gvpn siteid  {} ",illSite.getId());
						List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository.findByQuoteIllSite(illSite);
						constructOrderIllSiteToService(illSite, orderSite,quoteIllSiteServices);
					}
					constructOrderProductComponent(illSite, orderSite);
					persistQuoteSiteCommercialsAtServiceIdLevel(illSite, quoteToLe, orderSite);
					sites.add(orderSite);
				} else {
					detail.setManualFeasible(true);
				}
			}
		}

		return sites;
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
			LOGGER.info("illSla is Null so fetching again");
			try {
				constructSiteSlas(illSite);
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
					findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.IZO_PC_SITES.toString(), siteToService.getType());
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
			
			List<QuoteProductComponent> quoteProductComponentListSecondary = quoteProductComponentRepository.findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.IZO_PC_SITES.toString(), PDFConstants.SECONDARY);
			
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
			
			
			List<QuoteProductComponent> quoteProductComponentListPrimary = quoteProductComponentRepository.findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.IZO_PC_SITES.toString(), PDFConstants.PRIMARY);
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
								IllSitePropertiesConstants.SITE_PROPERTIES.toString(), QuoteConstants.IZO_PC_SITES.toString());
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
	 * @author Dinahar V
	 * @param orderSite
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent
	 * @param id,version
	 */
	private List<OrderProductComponent> constructOrderProductComponent(QuoteIllSite qillSite, OrderIllSite illSite) {
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductFamily_Name(qillSite.getId(), CommonConstants.IZOPC);
		String orderType =qillSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType();
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
						constructOrderAttribute(attributes, orderProductComponent,orderType,qillSite));
				orderProductComponents.add(orderProductComponent);
			}

		}
		return orderProductComponents;

	}

	/**
	 * @author Dinahar V
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
				//orderPriceRepository.save(orderPrice);
			}

		}
		return orderPrice;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com constructOrderAttribute used to
	 *       construct order attribute
	 * @param quoteProductComponentsAttributeValues
	 * @param orderProductComponent
	 * @param orderProductComponent
	 * @param orderSite
	 * @return
	 * @throws TclCommonException 
	 */
	private Set<OrderProductComponentsAttributeValue> constructOrderAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues,
			OrderProductComponent orderProductComponent,String orderType,QuoteIllSite quoteIllSite) {
		boolean isMacd=false;
		if(orderType!=null && orderType.equalsIgnoreCase("MACD")) {
			isMacd=true;
		}
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
				if (isMacd && orderProductComponent.getType().equalsIgnoreCase("primary")) {
					if (attributeValue.getProductAttributeMaster().getName().equals("Bandwidth")) {
						try {
							List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository
									.findByQuoteIllSite_IdAndType(quoteIllSite.getId(), "primary");
							String oldBw = null;
							for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
								String iasQueueResponse = (String) mqUtils.sendAndReceive(siServiceDetailsListQueue,
										quoteIllSiteToService.getErfServiceInventoryTpsServiceId());

								if (StringUtils.isNotBlank(iasQueueResponse)) {
									OrderSummaryBeanResponse siDetailedInfoResponse = (OrderSummaryBeanResponse) Utils
											.convertJsonToObject(iasQueueResponse, OrderSummaryBeanResponse.class);
									for (Entry<String, List<SIServiceInfoBean>> siSet : siDetailedInfoResponse
											.getServiceMap().entrySet()) {
										for (SIServiceInfoBean detailedInfo : siSet.getValue()) {
											oldBw = detailedInfo.getBandwidthPortSpeed();
										}
									}
								}
							}
							LOGGER.info("Old Bandwidth {}", oldBw);
							persistOrderAttr(orderAttributeValue, "Bandwidth_Old", oldBw);
						} catch (Exception e) {
							LOGGER.warn("Error in fetching old bandwidth {}",ExceptionUtils.getStackTrace(e));
						}
					} else if (attributeValue.getProductAttributeMaster().getName().equals("Second Cloud Provider Ref ID")) {
						persistOrderAttr(orderAttributeValue, "Second Cloud Provider Ref ID_Old",
								orderAttributeValue.getAttributeValues());
					}else if (attributeValue.getProductAttributeMaster().getName().equals("Cloud Provider Ref ID")) {
						persistOrderAttr(orderAttributeValue, "Cloud Provider Ref ID_Old",
								orderAttributeValue.getAttributeValues());
					}
				}
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

	private void persistOrderAttr(OrderProductComponentsAttributeValue orderAttributeValue,String attrName,String attrVal) {
		OrderProductComponentsAttributeValue srcOrderAttributeValue = new OrderProductComponentsAttributeValue();
		srcOrderAttributeValue.setAttributeValues(attrVal);
		srcOrderAttributeValue.setDisplayValue(attrVal);
		srcOrderAttributeValue.setIsAdditionalParam(orderAttributeValue.getIsAdditionalParam());
		srcOrderAttributeValue.setOrderProductComponent(orderAttributeValue.getOrderProductComponent());
		srcOrderAttributeValue
				.setProductAttributeMaster(getProductAttributes(attrName));
		orderProductComponentsAttributeValueRepository.save(srcOrderAttributeValue);
	}

	/**
	 * @author Dinahar V constructOrderComponentPrice used to constrcut order
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
	 * @author Dinahar V
	 * @param orderToLe
	 * @link http://www.tatacommunications.com/
	 */
	private Set<OrderToLeProductFamily> getOrderProductFamilyBasenOnVersion(QuoteToLe quote, OrderToLe orderToLe,
			QuoteDetail detail) {
		List<QuoteToLeProductFamily> prodFamilys = null;
		Set<OrderToLeProductFamily> orderFamilys = null;

		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		if (prodFamilys != null) {
			orderFamilys = new HashSet<>();
			for (QuoteToLeProductFamily quoteToLeProductFamily : prodFamilys) {
				OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
				orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
				orderToLeProductFamily.setOrderToLe(orderToLe);
				orderToLeProductFamilyRepository.save(orderToLeProductFamily);
				orderToLeProductFamily.setOrderProductSolutions(constructOrderProductSolution(
						quoteToLeProductFamily.getProductSolutions(), orderToLeProductFamily, detail));
				orderFamilys.add(orderToLeProductFamily);
				processEngagement(quote, quoteToLeProductFamily);
			}

		}

		return orderFamilys;

	}

	private void processEngagement(QuoteToLe quote, QuoteToLeProductFamily quoteToLeProductFamily) {
		List<Engagement> engagements = engagementRepository.findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(
				quote.getQuote().getCustomer(), quote.getErfCusCustomerLegalEntityId(),
				quoteToLeProductFamily.getMstProductFamily(), CommonConstants.BACTIVE);
		if (engagements == null || engagements.isEmpty()) {
			Engagement engagement = new Engagement();
			engagement.setCustomer(quote.getQuote().getCustomer());
			engagement.setEngagementName(quoteToLeProductFamily.getMstProductFamily() + CommonConstants.HYPHEN
					+ quote.getErfCusCustomerLegalEntityId());
			engagement.setErfCusCustomerLeId(quote.getErfCusCustomerLegalEntityId());
			engagement.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
			engagement.setStatus(CommonConstants.BACTIVE);
			engagement.setCreatedTime(new Date());
			engagementRepository.save(engagement);
		}
	}

	/**
	 * @author Dinahar V
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @param detail
	 * @throws TclCommonException
	 */
	protected Order constructOrder(Quote quote, QuoteDetail detail) throws TclCommonException {
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
		if (checkO2CEligible(quote)) {
			order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
			order.setOrderToCashOrder(CommonConstants.BACTIVE);
		} else {
			order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
			order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
		}
		orderRepository.save(order);
		order.setOrderToLes(constructOrderToLe(quote, order, detail));
		return order;

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

	/**
	 * @param quoteId
	 * @param ipAddress
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public QuoteDetail approvedManualQuotes(Integer quoteId, String ipAddress) throws TclCommonException {
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

				LOGGER.info("Entering into IZOPC approve quote {}",quoteId);
				detail=izopcMACDService.approvedMacdManualQuotes(quoteId);

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
					order = constructOrder(quote, detail);
					detail.setOrderId(order.getId());
					updateManualOrderConfirmationAudit(ipAddress, order.getOrderCode());
					// Trigger SFDC
					for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
						omsSfdcService.processSiteDetails(quoteLe);
						omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
						List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
						Integer userId = order.getCreatedBy();
						String userEmail = null;
						if (userId != null) {
							Optional<User> userDetails = userRepository.findById(userId);
							if (userDetails.isPresent()) {
								userEmail = userDetails.get().getEmailId();
							}
						}
						for (OrderToLe orderToLe : order.getOrderToLes()) {
							izoPcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(), cofObjectMapper);
							break;
						}
						processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
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

	/**
	 * @param publicIp
	 * @param orderRefUuid
	 * @throws TclCommonException
	 */
	public void updateManualOrderConfirmationAudit(String publicIp, String orderRefUuid) throws TclCommonException {

		try {
			String name = Utils.getSource();
			OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
					.findByOrderRefUuid(orderRefUuid);
			if (orderConfirmationAudit == null) {
				orderConfirmationAudit = new OrderConfirmationAudit();
			}
			orderConfirmationAudit.setName(name);
			orderConfirmationAudit.setMode(AuditMode.MANUAL.toString());
			orderConfirmationAudit.setUploadedBy(name);
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
	 * 
	 * @author Dinahar V
	 * 
	 *         cofDownloadAccountManagerNotification - This method is used notify
	 *         the account manager when a customer downloads the cof
	 * @param orderId,
	 *            orderToLeId
	 * @return String
	 * @throws TclCommonException
	 */
	public String cofDownloadAccountManagerNotification(Integer quoteId, Integer quoteToLeId)
			throws TclCommonException {
		String str = null;
		QuoteToLe quoteToLe = null;
		String userName = Utils.getSource();
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeOpt.isPresent()) {
			quoteToLe = quoteToLeOpt.get();
			User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
			MailNotificationBean mailNotificationBean = populateMailNotificationBeanCof(
					getLeAttributes(quoteToLe, LeAttributesConstants.LEGAL_ENTITY_NAME.toString()),
					quoteToLe.getQuote().getCustomer().getCustomerName(), user.getFirstName(),
					getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL.toString()),
					quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl, quoteToLe);
			notificationService.cofDownloadNotification(mailNotificationBean);
		}
		return str;
	}

	private MailNotificationBean populateMailNotificationBeanCof(String customerAccountName, String customerName, String userName,
																 String accountManagerEmail, String orderId, String quoteLink, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setCustomerAccountName(customerAccountName);
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderId);
		mailNotificationBean.setQuoteLink(quoteLink);
		mailNotificationBean.setProductName(IZOPC);
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * deleteOrderRelatedDetails - This method is to delete order details in
	 * database for an order
	 * 
	 * @param order
	 */
	private void deleteNPLOrderRelatedDetails(Order order) {

		order.getOrderToLes().stream().forEach(orderToLe -> {
			orderToLe.getOrderToLeProductFamilies().stream().forEach(orderToLeProdFamily -> {
				orderToLeProdFamily.getOrderProductSolutions().stream().forEach(orderProdSolution -> {

					orderNplLinkRepository.findByProductSolutionId(orderProdSolution.getId()).forEach(link -> {
						deleteOrderFeasibilitySlaStatusStageAuditDetails(link);
						orderNplLinkRepository.delete(link);

						List<OrderProductComponent> orderProductComponentList = orderProductComponentRepository
								.findByReferenceIdAndType(link.getId(), CommonConstants.LINK);
						orderProductComponentList.addAll(orderProductComponentRepository
								.findByReferenceIdAndType(link.getSiteAId(), CommonConstants.SITEA));
						orderProductComponentList.addAll(orderProductComponentRepository
								.findByReferenceIdAndType(link.getSiteBId(), CommonConstants.SITEB));

						orderProductComponentList.stream().forEach(orderProdComponent -> {
							deleteOrderProductComponent(orderProdComponent);
						});
					});

					orderIllSitesRepository.deleteByOrderProductSolution(orderProdSolution);
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

	/**
	 * deleteOrderFeasibilitySlaStatusStageAuditDetails - deletes feasibility, SLA,
	 * status, stage and audit details of an order
	 * 
	 * @param link
	 */
	private void deleteOrderFeasibilitySlaStatusStageAuditDetails(OrderNplLink link) {

		List<OrderLinkFeasibility> orderLinkFeasiblityList = orderLinkFeasibilityRepository.findByOrderNplLink(link);
		if (!orderLinkFeasiblityList.isEmpty())
			orderLinkFeasibilityRepository.deleteAll(orderLinkFeasiblityList);

		List<OrderNplLinkSla> orderLinkSlaList = orderNplLinkSlaRepository.findByOrderNplLink(link);
		if (!orderLinkSlaList.isEmpty())
			orderNplLinkSlaRepository.deleteAll(orderLinkSlaList);

		List<OrderLinkStatusAudit> orderLinkStatusAuditList = orderLinkStatusAuditRepository.findByOrderNplLink(link);

		if (!orderLinkStatusAuditList.isEmpty()) {
			orderLinkStatusAuditList.forEach(statusAudit -> {
				List<OrderLinkStageAudit> orderLinkStageAuditList = orderLinkStageAuditRepository
						.findByOrderLinkStatusAudit(statusAudit);
				if (!orderLinkStageAuditList.isEmpty())
					orderLinkStageAuditRepository.deleteAll(orderLinkStageAuditList);
			});
		}

		orderLinkStatusAuditRepository.deleteAll(orderLinkStatusAuditList);

	}

	public void deleteQuote(Integer quoteId) throws TclCommonException {
		try {

			if (Objects.isNull(quoteId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			Optional<Quote> quoteToDelete = quoteRepository.findById(quoteId);

			if (quoteToDelete.isPresent()) {
				Quote quote = quoteToDelete.get();

				quote.getQuoteToLes().stream().forEach(quoteToLe -> {
					try
					{
					quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteToLeProdFamily -> {
						Optional<Order> order = orderRepository.findByQuote(quote);
						if (quoteToLeProdFamily.getMstProductFamily().getName()
								.equalsIgnoreCase(CommonConstants.NPL.toString())) {
							if (order.isPresent()) {
								throw new TclCommonRuntimeException(ExceptionConstants.TASKS_PENDING_FOR_QUOTE, ResponseResource.R_CODE_ERROR);
							}
								//deleteNPLOrderRelatedDetails(order.get());
							deleteNPLQuotes(quoteToLeProdFamily);

						} else {
							if (order.isPresent())
								deleteOrderRelatedDetails(order.get());
							quoteToLeProdFamily.getProductSolutions().stream().forEach(prodSolution -> {
								prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
									List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
											.findByReferenceIdAndMstProductFamily_Name(illSite.getId(),
													CommonConstants.IZOPC);

									quoteProductComponentList.stream().forEach(quoteProdComponent -> {
										deleteQuoteProductComponent(quoteProdComponent);
									});
									deleteFeasibilityDetails(illSite);
									illSiteRepository.delete(illSite);
								});
								productSolutionRepository.delete(prodSolution);
							});
						}
						quoteToLeProductFamilyRepository.delete(quoteToLeProdFamily);
					});
					deleteQuoteLeAttributeValues(quoteToLe);
					quoteToLeRepository.delete(quoteToLe);

						//SFDC Update Opportunity - CLOSED DROPPED
						omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(), SFDCConstants.CLOSED_DROPPED, quoteToLe);
					}
					catch( Exception e)
					{
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					}
				});
				deleteQuotePrice(quote);
				quoteRepository.delete(quote);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * deleteQuoteFeasibilitySlaPriceDetails - deletes feasibility, SLA and price
	 * details for a link
	 * 
	 * @param link
	 */
	private void deleteQuoteFeasibilitySlaPriceDetails(QuoteNplLink link) {
		List<LinkFeasibility> linkFeasibilityList = linkFeasibilityRepository.findByQuoteNplLink(link);
		if (!linkFeasibilityList.isEmpty())
			linkFeasibilityRepository.deleteAll(linkFeasibilityList);

		List<PricingEngineResponse> pricingDetailList = pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(link.getLinkCode(),"Discount");
		if (!pricingDetailList.isEmpty())
			pricingDetailsRepository.deleteAll(pricingDetailList);

		List<QuoteNplLinkSla> quoteNplLinkSlaList = quoteNplLinkSlaRepository.findByQuoteNplLink(link);
		if (!quoteNplLinkSlaList.isEmpty())
			quoteNplLinkSlaRepository.deleteAll(quoteNplLinkSlaList);

	}

	/**
	 * deleteQuote - This method is to delete the quote based on quote id
	 * 
	 * @param quoteId
	 * @throws TclCommonException
	 */
	private void deleteNPLQuotes(QuoteToLeProductFamily quoteToLeProductFamily) {

		quoteToLeProductFamily.getProductSolutions().stream().forEach(prodSolution -> {

			nplLinkRepository.findByProductSolutionId(prodSolution.getId()).stream().forEach(link -> {
				deleteQuoteFeasibilitySlaPriceDetails(link);

				List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
						.findByReferenceIdAndType(link.getId(), CommonConstants.LINK);
				quoteProductComponentList.addAll(quoteProductComponentRepository
						.findByReferenceIdAndType(link.getSiteAId(), CommonConstants.SITEA));
				quoteProductComponentList.addAll(quoteProductComponentRepository
						.findByReferenceIdAndType(link.getSiteBId(), CommonConstants.SITEB));

				quoteProductComponentList.stream().forEach(this::deleteQuoteProductComponent);

			});

			nplLinkRepository.deleteByProductSolutionId(prodSolution.getId());
			illSiteRepository.deleteByProductSolution(prodSolution);
			productSolutionRepository.delete(prodSolution);
		});

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
								.findByReferenceIdAndMstProductFamily_Name(orderIllSite.getId(), CommonConstants.IZOPC);
						orderProductComponentList.stream().forEach(orderProdComponent -> {
							deleteOrderProductComponent(orderProdComponent);
						});
						deleteOrderFeasibilityDetails(orderIllSite);
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

	/**
	 * deleteOrderLeAttributeValues - deletes orderToLe attributes for a particular
	 * orderToLe
	 * 
	 * @param orderToLe
	 */

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

	/**
	 * deleteOrderProductComponent - deletes an order product component
	 * 
	 * @param orderProdComponent
	 */

	private void deleteOrderProductComponent(OrderProductComponent orderProdComponent) {
		if (!orderProdComponent.getOrderProductComponentsAttributeValues().isEmpty()) {

			orderProdComponent.getOrderProductComponentsAttributeValues().stream().forEach(attri -> {
				OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(attri.getId()), QuoteConstants.ATTRIBUTES.toString());
				if (orderPrice != null)
					orderPriceRepository.delete(orderPrice);
				orderProductComponentsAttributeValueRepository.delete(attri);
			});

			OrderPrice orderPriceAtt = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderProdComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (orderPriceAtt != null)
				orderPriceRepository.delete(orderPriceAtt);

			/*
			 * orderProductComponentsAttributeValueRepository
			 * .deleteAll(orderProdComponent.getOrderProductComponentsAttributeValues());
			 */
			orderProductComponentRepository.delete(orderProdComponent);
		}

	}

	private void deleteFeasibilityDetails(QuoteIllSite illSite) {

		List<SiteFeasibility> siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite(illSite);
		if (!siteFeasibilityList.isEmpty()) {
			siteFeasibilityList.stream().forEach(siteFeasibility -> {
				List<SiteFeasibilityAudit> siteFeasibilityAuditList = siteFeasibilityAuditRepository
						.findBySiteFeasibility(siteFeasibility);
				if (!siteFeasibilityAuditList.isEmpty()) {
					siteFeasibilityAuditRepository.deleteAll(siteFeasibilityAuditList);
				}
			});

			siteFeasibilityRepository.deleteAll(siteFeasibilityList);
		}

		List<PricingEngineResponse> pricingDetailList = pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(illSite.getSiteCode(),"Discount");
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

	private void deleteQuoteProductComponent(QuoteProductComponent quoteProdComponent) {
		if (!quoteProdComponent.getQuoteProductComponentsAttributeValues().isEmpty()) {
			/*
			 * quoteProductComponentsAttributeValueRepository
			 * .deleteAll(quoteProdComponent.getQuoteProductComponentsAttributeValues());
			 */
			quoteProdComponent.getQuoteProductComponentsAttributeValues().stream().forEach(attri -> {
				QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(attri.getId()), QuoteConstants.ATTRIBUTES.toString());
				if (quotePrice != null) {
					List<QuotePriceAudit> quotePriceAuditList = quotePriceAuditRepository.findByQuotePrice(quotePrice);
					if (!quotePriceAuditList.isEmpty()) {
						quotePriceAuditRepository.deleteAll(quotePriceAuditList);
					}
					quotePriceRepository.delete(quotePrice);
				}
				quoteProductComponentsAttributeValueRepository.delete(attri);
			});

			QuotePrice quotePriceAttr = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(quoteProdComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (quotePriceAttr != null) {
				List<QuotePriceAudit> quotePriceAuditList = quotePriceAuditRepository.findByQuotePrice(quotePriceAttr);
				if (!quotePriceAuditList.isEmpty()) {
					quotePriceAuditRepository.deleteAll(quotePriceAuditList);
				}
				quotePriceRepository.delete(quotePriceAttr);
			}

			quoteProductComponentRepository.delete(quoteProdComponent);
		}
	}

	/**
	 * Method to get the currency for getquote page
	 * 
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public String getQuoteCurrency(Integer quoteLeId) throws TclCommonException {

		List<Integer> locationIds = new ArrayList<>();
		List<Integer> nonFeasibleLocationIds = new ArrayList<>();
		String currency = "INR";
		String locCommaSeparated = StringUtils.EMPTY;

		if (Objects.isNull(quoteLeId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		try {
			QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLe_IdAndMstProductFamily_Name(quoteLeId, "IZOPC");
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
				locCommaSeparated = locationIds.stream().map(i -> i.toString().trim()).collect(Collectors.joining(","));
				currency = getCurrencyBasedOnCountries(locCommaSeparated);
			} else {
				locCommaSeparated = nonFeasibleLocationIds.stream().map(i -> i.toString().trim())
						.collect(Collectors.joining(","));
				currency = getCurrencyBasedOnCountries(locCommaSeparated);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return currency;
	}

	private String getCurrencyBasedOnCountries(String locationIds) throws TclCommonException {
		
		List<LocationDetail> locDetails = null;
		Set<String> countrySet = new HashSet<>();
		locDetails = Arrays.asList(getAddress(locationIds));
		countrySet = locDetails.stream().map(detail -> detail.getApiAddress().getCountry())
				.collect(Collectors.toSet());
		return countrySet.contains("India") || countrySet.contains("INDIA") ? "INR" : "USD";
	
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
			LOGGER.info("Output Payload for location details", response);
			LocationDetail[] locDetails = (LocationDetail[]) Utils.convertJsonToObject(response,
					LocationDetail[].class);
			return locDetails;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
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
	 *  Updates the legal entity properties
	 *  
	 * @param request
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public void updateLegalEntityPropertiesIsvQuote(List<UpdateRequest> request, Integer quoteToLeId) throws TclCommonException {
		try{
			request.stream().forEach(req->{
				try {
					validateRequest2(req);
					User user = getUserId(Utils.getSource());
					if(user == null) {
						throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
					}
					Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(quoteToLeId);
					if(!optionalQuoteToLe.isPresent()) {
						throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
					}
					 
					MstOmsAttribute omsAttribute = getMstAttributeMaster(req, user);
					constructLegaAttribute(omsAttribute, optionalQuoteToLe.get(), req.getAttributeName(), req.getAttributeValue());
				}catch(Exception e) {
					throw new TclCommonRuntimeException(e);
				}
			});
		}catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void validateRequest2(UpdateRequest request) throws TclCommonException {
		if (request.getQuoteId() == null || request.getAttributeName()==null ) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}

	}
	/**
	 * 
	 * Update LCON properities against the site details
	 * @param quoteId
	 * @param quoteToLeId
	 * @param lconUpdateBeans
	 */
	public void updateLconProperities(Integer quoteId, Integer quoteToLeId, List<LconUpdateBean> lconUpdateBeans) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if(quoteToLe.isPresent()) {
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.get().getId());
			if(quoteToLeProductFamilies!=null && !quoteToLeProductFamilies.isEmpty()) {
				quoteToLeProductFamilies.forEach(productFamily->{
					MstProductComponent mstProductComponents = mstProductComponentRepository.findByName("SITE_PROPERTIES");
					List<ProductAttributeMaster> lconNameProductAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus("LCON_NAME", CommonConstants.BACTIVE);
					List<ProductAttributeMaster> lconContactProductAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus("LCON_CONTACT_NUMBER", CommonConstants.BACTIVE);
					List<ProductAttributeMaster> lconRemarksProductAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus("LCON_REMARKS", CommonConstants.BACTIVE);
					if (mstProductComponents != null && lconContactProductAttributeMasters != null
							&& !lconContactProductAttributeMasters.isEmpty() && lconNameProductAttributeMasters != null
							&& !lconNameProductAttributeMasters.isEmpty()) {
						if (lconUpdateBeans != null && !lconUpdateBeans.isEmpty()) {
							lconUpdateBeans.stream().forEach(lconUpdateBean -> {

								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponentAndMstProductFamily(lconUpdateBean.getSiteId(), mstProductComponents,productFamily.getMstProductFamily());
								if(quoteProductComponents!=null && quoteProductComponents.isEmpty()) {
									QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
									quoteProductComponent.setMstProductComponent(mstProductComponents);
									quoteProductComponent.setReferenceId(lconUpdateBean.getSiteId());
									quoteProductComponent.setMstProductFamily(productFamily.getMstProductFamily());
									quoteProductComponent.setType("primary");
									quoteProductComponentRepository.save(quoteProductComponent);
								}
								quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponentAndMstProductFamily(lconUpdateBean.getSiteId(), mstProductComponents,productFamily.getMstProductFamily());
								if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
									// LCON - Name
									List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
											.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponents.get(0),
													lconNameProductAttributeMasters.get(0));
									if (quoteProductComponentsAttributeValues != null
											&& quoteProductComponentsAttributeValues.isEmpty()) {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
										quoteProductComponentsAttributeValue.setAttributeValues(lconUpdateBean.getLconName());
										quoteProductComponentsAttributeValue
												.setProductAttributeMaster(lconNameProductAttributeMasters.get(0));
										quoteProductComponentsAttributeValue
												.setQuoteProductComponent(quoteProductComponents.get(0));
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
									} else {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
												.get(0);
										quoteProductComponentsAttributeValue.setAttributeValues(lconUpdateBean.getLconName());
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
									}
									// LCON - Number
									List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValuesContact = quoteProductComponentsAttributeValueRepository
											.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponents.get(0),
													lconContactProductAttributeMasters.get(0));
									if (quoteProductComponentsAttributeValuesContact != null
											&& quoteProductComponentsAttributeValuesContact.isEmpty()) {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
										quoteProductComponentsAttributeValue.setAttributeValues(lconUpdateBean.getLconNumber());
										quoteProductComponentsAttributeValue
												.setProductAttributeMaster(lconContactProductAttributeMasters.get(0));
										quoteProductComponentsAttributeValue
												.setQuoteProductComponent(quoteProductComponents.get(0));
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
									} else {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValuesContact
												.get(0);
										quoteProductComponentsAttributeValue.setAttributeValues(lconUpdateBean.getLconNumber());
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
									}
									
									//LCON_REMARKS
									List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValuesRemarks = quoteProductComponentsAttributeValueRepository
											.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponents.get(0),
													lconRemarksProductAttributeMasters.get(0));
									if (quoteProductComponentsAttributeValuesRemarks != null
											&& quoteProductComponentsAttributeValuesRemarks.isEmpty()) {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
										quoteProductComponentsAttributeValue.setAttributeValues(lconUpdateBean.getLconRemarks());
										quoteProductComponentsAttributeValue
												.setProductAttributeMaster(lconRemarksProductAttributeMasters.get(0));
										quoteProductComponentsAttributeValue
												.setQuoteProductComponent(quoteProductComponents.get(0));
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
									} else {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValuesRemarks
												.get(0);
										quoteProductComponentsAttributeValue.setAttributeValues(lconUpdateBean.getLconRemarks());
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
									}
								}
								try {
									omsSfdcService.updateFeasibility(quoteToLe.get(), lconUpdateBean.getSiteId());
								}catch(TclCommonException e) {
									LOGGER.error("Sfdc update feasibility request Failed " , e);
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
				detail = izopcMACDService.approvedMacdDocusignQuotes(quoteuuId);
			} else {
				Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

				if (order != null) {
					detail.setOrderId(order.getId());
				} else {
					order = constructOrder(quote, detail);
					detail.setOrderId(order.getId());
					// Trigger SFDC
					for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
						omsSfdcService.processSiteDetails(quoteLe);
						Date cofSignedDate = new Date();
						DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
						if (docusignAudit != null && docusignAudit.getCustomerSignedDate() != null
								&& (docusignAudit.getStatus()
										.equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString())
										|| docusignAudit.getStatus()
												.equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString()))) {
							cofSignedDate = docusignAudit.getCustomerSignedDate();
						}
						omsSfdcService.processUpdateOpportunity(cofSignedDate, quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
						List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
						Map<String, String> cofObjectMapper = new HashMap<>();
						CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
						if (cofDetails != null) {
							cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
						}
						Integer userId = order.getCreatedBy();
						String userEmail = null;
						if (userId != null) {
							Optional<User> userDetails = userRepository.findById(userId);
							if (userDetails.isPresent()) {
								userEmail = userDetails.get().getEmailId();
							}
						}
						for (OrderToLe orderToLe : order.getOrderToLes()) {
							List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
									.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES,
											quote.getId(), AttachmentTypeConstants.COF.toString());
							for (OmsAttachment omsAttachment : omsAttachmentList) {
								omsAttachment.setOrderToLe(orderToLe);
								omsAttachment.setReferenceName(CommonConstants.ORDERS);
								omsAttachment.setReferenceId(order.getId());
								omsAttachmentRepository.save(omsAttachment);
							}
							izoPcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
									orderToLe.getId(), cofObjectMapper);
							break;
						}
						processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
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
	
	
	/**
	 * Sets the site as not feasible for IzoPc
	 * 
	 * @author Kavya Singh
	 * @param quoteToLeId
	 * @param siteId
	 */
	@Transactional
	public void siteNotFeasibleIzoPc(Integer quoteToLeId,Integer siteId) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if(quoteToLe.isPresent()) {
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.get().getId());
			if(quoteToLeProductFamilies != null && !quoteToLeProductFamilies.isEmpty()) {
				quoteToLeProductFamilies.forEach(quoteLeProductFamily ->{
					MstProductFamily mstProdFamily = quoteLeProductFamily.getMstProductFamily();
					MstProductComponent mstProductComponent = mstProductComponentRepository
							.findByName("SITE_PROPERTIES");
					
					List<ProductAttributeMaster> isNotFeasProductAttributeMaster = productAttributeMasterRepository
							.findByNameAndStatus("IS_NOT_FEASIBLE", CommonConstants.BACTIVE);
					
					if(mstProductComponent != null && isNotFeasProductAttributeMaster != null 
							&& !isNotFeasProductAttributeMaster.isEmpty()) {
						List<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository.
								findByReferenceIdAndMstProductComponentAndMstProductFamily(siteId,mstProductComponent,mstProdFamily);
							
					List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository.
								findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent.get(0), isNotFeasProductAttributeMaster.get(0));	
						if(attributeValues != null && attributeValues.isEmpty()) {
							QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
							quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent.get(0));
							quoteProductComponentsAttributeValue.setProductAttributeMaster(isNotFeasProductAttributeMaster.get(0));
							quoteProductComponentsAttributeValue.setDisplayValue("true");
							quoteProductComponentsAttributeValue.setAttributeValues("true");
							quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
						}
						else {
							QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = attributeValues.get(0);
							quoteProductComponentsAttributeValue.setAttributeValues("true");
							quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
						}
					}
					
				});
			}
		}
		
		
	}
	
	/**
	 * 
	 * This function is used to trigger the tax exemption mail when us sites is selected for tax exemption
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public String processTriggerTaxExemptionMailForUSSites(Integer quoteId, Integer quoteLeId,Integer customerLeId)
			throws TclCommonException {
		try {
			if (quoteId != null && quoteLeId != null) {
				Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
				if (quote != null) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
					if (quoteToLe.isPresent()) {
						String queueResponse = (String) mqUtils.sendAndReceive(customerLeAccountMangeQueue, customerLeId.toString());
						if (StringUtils.isNotBlank( queueResponse)) {
							CustomerLeAccountManagerDetails customerLeAccountManagerDetails = (CustomerLeAccountManagerDetails) Utils.convertJsonToObject(queueResponse, CustomerLeAccountManagerDetails.class);
							if(customerLeAccountManagerDetails!=null) {
								String accountManagerEmail = customerLeAccountManagerDetails.getAccountManagerEmailId();
								String accountManagerName = customerLeAccountManagerDetails.getAccountManagerName();
								String customerEmail = null;
								User user = userRepository.findByIdAndStatus(quote.getCreatedBy(), CommonConstants.ACTIVE);
								if (user != null && user.getEmailId() != null) {
									customerEmail = user.getEmailId();
								}
								if (accountManagerEmail != null && accountManagerName != null && quote.getQuoteCode() != null) {
									notificationService.taxExemptionNotificationforUSSites(customerEmail, accountManagerEmail,
											accountManagerName, quote.getQuoteCode(), quoteDashBoardRelativeUrl,
											CommonConstants.IAS);
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

	public void updateCurrencyCodeInQuoteToLe(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
		if(quoteToLe.isPresent()) {
			quoteToLe.get().setCurrencyCode(getQuoteCurrency(quoteLeId));
			quoteToLeRepository.save(quoteToLe.get());
		}
				
	}
	
	 public List<QuoteSiteDifferentialCommercial> persistQuoteSiteCommercialsAtServiceIdLevel(QuoteIllSite illSite) {
			List<QuoteSiteDifferentialCommercial> quoteSiteCommercialList = new ArrayList<>();
			LOGGER.info("Entering persistQuoteSiteCommercialsAtServiceIdLevel");
			
			User userEntity = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
			if(userEntity==null) {
				userEntity = userRepository.findByUsernameAndStatus("root", CommonConstants.ACTIVE);//TODO
			}
			Integer createdId=userEntity.getId();
			Double[] subTotalMrc = { 0D };
			Double[] subTotalNrc = { 0D };
			
			QuoteToLe quoteToLe = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
			List<QuotePrice> quotePriceList = quotePriceRepository.findByQuoteId(quoteToLe.getQuote().getId());
				
			List<QuoteProductComponent> quoteProductComponentListPrimary = quoteProductComponentRepository.findByReferenceIdAndMstProductFamily_Name(illSite.getId(), "IZOPC");
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
		

			quoteSiteDifferentialCommercialRepository.saveAll(quoteSiteCommercialList);
			return quoteSiteCommercialList;
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

	public CommonValidationResponse processValidate(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		commonValidationResponse.setStatus(true);
		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = getQuoteDetails(quoteId, null, false);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			Map<String, Object> variable = izoPcQuotePdfService.getCofAttributes(true, quoteDetail, true, quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
					|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for IZOPC is {}", Utils.convertObjectToJson(variable));
				commonValidationResponse = izoPcCofValidatorService.processCofValidation(variable,
						"IZOPC", quoteToLe.get().getQuoteType());
			}
		} catch (Exception e) {
			LOGGER.error("Error in validating the mandatory Data", e);
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage("Data Error");
		}
		return commonValidationResponse;
	}
		
	private Boolean checkO2CEligible(Quote quote) {

		List<QuoteIllSite> sites = illSiteRepository.findIllSites(quote.getQuoteCode());
		if (sites != null && !sites.isEmpty()) {
			for (QuoteIllSite site : sites) {

				List<String> attrValues = quoteProductComponentsAttributeValueRepository
						.getAttributeValueByAttributeName(site.getId(), "Cloud Provider", "primary",
								QuoteConstants.IZO_PC_SITES.toString());
				if (attrValues != null && !attrValues.isEmpty()) {
					String value = attrValues.stream().filter(val -> (val.equals("SFDC Express Connect"))
							|| (val.equals("Alibaba Cloud Express Connect"))).findFirst().orElse(null);
					if (value != null) {
						return false;
					}
				}
				attrValues = quoteProductComponentsAttributeValueRepository.getAttributeValueByAttributeName(
						site.getId(), "Cloud Provider", "secondary", QuoteConstants.IZO_PC_SITES.toString());
				if (attrValues != null && !attrValues.isEmpty()) {
					String value = attrValues.stream().filter(val -> (val.equals("SFDC Express Connect"))
							|| (val.equals("Alibaba Cloud Express Connect"))).findFirst().orElse(null);
					if (value != null) {
						return false;
					}
				}
				List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository
						.findByQuoteIllSite_IdAndIsSelected(site.getId(), CommonConstants.BACTIVE);
				for (SiteFeasibility siteFeasibility : siteFeasibilities) {
					if ("INTL".equals(siteFeasibility.getFeasibilityMode())) {
						return false;
					} else {
						return true;
					}
				}
			}
		}
		/*
		 * if (quote.getQuoteToLes() != null && !quote.getQuoteToLes().isEmpty()) { for
		 * (QuoteToLe quoteToLe : quote.getQuoteToLes()) { List<QuoteLeAttributeValue>
		 * quoteLeAttributeValues = quoteLeAttributeValueRepository
		 * .findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe,
		 * IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS); if (quoteLeAttributeValues
		 * != null && !quoteLeAttributeValues.isEmpty()) { for (QuoteLeAttributeValue
		 * quoteLeAttributeValue : quoteLeAttributeValues) { try { String spLeResponse =
		 * (String) mqUtils.sendAndReceive(locationQueue,
		 * quoteLeAttributeValue.getAttributeValue()); if (spLeResponse != null) {
		 * AddressDetail spAddressDetail = (AddressDetail)
		 * Utils.convertJsonToObject(spLeResponse, AddressDetail.class); String address
		 * = StringUtils.trimToEmpty(spAddressDetail.getAddressLineOne()) +
		 * CommonConstants.SPACE +
		 * StringUtils.trimToEmpty(spAddressDetail.getAddressLineTwo()) +
		 * CommonConstants.SPACE +
		 * StringUtils.trimToEmpty(spAddressDetail.getLocality()) +
		 * CommonConstants.SPACE + StringUtils.trimToEmpty(spAddressDetail.getCity()) +
		 * CommonConstants.SPACE + StringUtils.trimToEmpty(spAddressDetail.getState()) +
		 * CommonConstants.SPACE + StringUtils.trimToEmpty(spAddressDetail.getCountry())
		 * + CommonConstants.SPACE +
		 * StringUtils.trimToEmpty(spAddressDetail.getPincode()); if
		 * (spAddressDetail.getCountry() != null &&
		 * !"India".equalsIgnoreCase(spAddressDetail.getCountry())) {
		 * LOGGER.info("International Service Provider mapped for this quote!!"); return
		 * false; } } } catch (Exception e) {
		 * LOGGER.error("Error while fetching supplier address", e); } }
		 * 
		 * } } }
		 */

		return true;

	}
	
	public ServiceProviderLegalBean returnServiceProviderNameIzosdwan(Integer id) throws TclCommonException {
		try {
			LOGGER.info("MDC Filter token value in before Queue call returnServiceProviderNameIzosdwan {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(spQueueIzosdwan,
					Utils.convertObjectToJson(constructSupplierDetailsRequestBean(id)));
			if (StringUtils.isNotBlank(response)) {
				return Utils.convertJsonToObject(response, ServiceProviderLegalBean.class);
			}
		} catch (Exception e) {
			throw new TclCommonException("No Service Provider Name");
		}
		return null;
	}
}
