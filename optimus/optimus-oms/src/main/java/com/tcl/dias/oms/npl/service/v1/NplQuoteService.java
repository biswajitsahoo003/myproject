
package com.tcl.dias.oms.npl.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.*;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SICustomerInfoBean;
import com.tcl.dias.common.serviceinventory.beans.SIPriceRevisionDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponseBean;
import com.tcl.dias.common.sfdc.bean.SfdcCreditCheckQueryRequest;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.QuoteAccess;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.*;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.CommercialRejectionBean;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.CrossConnectEnrichmentBean;
import com.tcl.dias.oms.beans.EnrichmentDetailsBean;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.LinkFeasibilityManualBean;
import com.tcl.dias.oms.beans.LinkJsonManualUpdateBean;
import com.tcl.dias.oms.beans.MACDExistingComponentsBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.PricingDetailBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteSiteServiceTerminationDetailsBean;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.beans.QuoteTncBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.SiteAttributeUpdateBean;
import com.tcl.dias.oms.beans.SiteDocumentBean;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.LinkStagingConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteDelegationConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossconnectConstants;
import com.tcl.dias.oms.crossconnect.service.v1.CrossConnectQuoteService;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.dto.QuotePriceDto;
import com.tcl.dias.oms.dto.QuoteProductComponentDto;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.LinkFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplFeasiblityBean;
import com.tcl.dias.oms.npl.beans.NplLinkBean;
import com.tcl.dias.oms.npl.beans.NplLinksUpdateBean;
import com.tcl.dias.oms.npl.beans.NplPricingFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.NplSite;
import com.tcl.dias.oms.npl.beans.NplSiteDetail;
import com.tcl.dias.oms.npl.beans.NplUpdateRequest;
import com.tcl.dias.oms.npl.beans.ProductSolutionBean;
import com.tcl.dias.oms.npl.beans.QuoteNplSiteBean;
import com.tcl.dias.oms.npl.beans.QuoteNplSiteDto;
import com.tcl.dias.oms.npl.beans.QuoteResponse;
import com.tcl.dias.oms.npl.beans.QuoteToLeBean;
import com.tcl.dias.oms.npl.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.npl.beans.SICustomerDetailDataBean;
import com.tcl.dias.oms.npl.beans.SLABean;
import com.tcl.dias.oms.npl.constants.NplOrderConstants;
import com.tcl.dias.oms.npl.constants.SLAConstants;
import com.tcl.dias.oms.npl.macd.service.v1.NplMACDService;
import com.tcl.dias.oms.npl.pdf.beans.NplMcQuoteDetailBean;
import com.tcl.dias.oms.npl.pdf.beans.NplQuotePdfBean;
import com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.pricing.bean.Feasible;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.renewals.bean.RenewalsConstant;
import com.tcl.dias.oms.npl.pricing.bean.Result;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.validator.services.NplCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.micrometer.core.lang.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tcl.dias.common.constants.CommonConstants.*;
import static com.tcl.dias.common.constants.LeAttributesConstants.*;
import static com.tcl.dias.common.constants.LeAttributesConstants.TEAM_ROLE_SFDC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_FORM;
import static com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants.CROSS_CONNECT_LOCAL_DEMARCATION_ID;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_THROUGH_CLASSIFICATION;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_WITH_CLASSIFICATION;

/**
 * This file contains the NplQuoteService.java class. All the Quote related
 * Services for NPL will be implemented in this class
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class NplQuoteService {

	public static final Logger LOGGER = LoggerFactory.getLogger(NplQuoteService.class);

	@Autowired
	OrderIllSitesRepository orderNplSitesRepository;

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	protected OrderRepository orderRepository;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;

	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	protected IllSiteRepository illSiteRepository;

	@Autowired
	protected ProductSolutionRepository productSolutionRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OrderSiteProvisioningRepository ordersiteProvsioningRepository;

	@Autowired
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	OrderIllSiteSlaRepository orderNplSiteSlaRepository;

	@Autowired
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;

	@Autowired
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;

	@Autowired
	protected OrderIllSitesRepository orderIllSitesRepository;

	@Autowired
	protected QuotePriceRepository quotePriceRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	protected OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	protected DocusignAuditRepository docusignAuditRepository;

	@Autowired
	OrderNplLinkRepository orderNplLinkRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	protected NplLinkRepository nplLinkRepository;

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
	protected QuoteNplLinkSlaRepository nplLinkSlaRepository;

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
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	protected CofDetailsRepository cofDetailsRepository;

	@Value("${rabbitmq.location.detail}")
	protected String locationQueue;

	@Value("${rabbitmq.npl.sla.queue}")
	String productSlaQueue;

	@Value("${rabbitmq.location.detail}")
	String locationDetailQueue;

	@Autowired
	protected QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	OrderLinkStatusAuditRepository orderLinkStatusAuditRepository;

	@Autowired
	OrderLinkStageAuditRepository orderLinkStageAuditRepository;

	@Autowired
	LinkFeasibilityAuditRepository linkFeasibilityAuditRepository;

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

	@Autowired
	protected NplQuotePdfService nplQuotePdfService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Value("${rabbitmq.customer.le.update.msa}")
	String updateMSAQueue;

	@Autowired
	EngagementRepository engagementRepository;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${cust.get.segment.attribute}")
	String customerSegment;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Value("${rabbitmq.city.details}")
	protected String citeDetailByLocationIdQueue;

	@Value("${rabbitmq.customerle.credit.queue}")
	String customerLeCreditCheckQueue;

	@Value("${sfdc.process.creditcheck.retrigger.queue}")
	String creditCheckRetriggerQueue;

	@Autowired
	CreditCheckService creditCheckService;

	@Autowired
	QuoteLeCreditCheckAuditRepository quoteLeCreditCheckRepository;

	@Autowired
	CrossConnectQuoteService crossConnectQuoteService;

	@Autowired
	protected QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	protected OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	@Autowired
	MACDUtils macdUtils;

	@Value("${rabbitmq.location.localitcontact}")
	String localItQueue;

	@Value("${rabbitmq.location.demarcation}")
	String demarcationQueue;
	
	@Autowired
	NplCofValidatorService nplCofValidatorService;	

	@Autowired
	NplMACDService nplMACDService;

	@Value("${rabbitmq.crossconnect.localit.demarcation.detail}")
	private String getCrossConnectLocalITandDemarcation;
	
	@Autowired
	QuoteAccessPermissionRepository quoteAccessPermissionRepository;
	

	@Value("${rabbitmq.si.nde.details.queue}")
	String siNdeCustomerDetailsQueue;
	
	
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;
	
	
	 @Value("${rabbitmq.nde.customer.le.by.custid}")
	 String customerLeByCustomerIdMQ;

	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;
	
	@Autowired
	private CommercialQuoteAuditRepository commercialQuoteAuditRepository;
	
	@Autowired
	NplLinkRepository linkRepository;


	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	PartnerService partnerService;
	
	@Autowired
	QuoteTncRepository quoteTncRepository;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	
	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;

	@Value("${application.env}")
	String appEnv;

	@Value("${rabbitmq.getOwnerDetailsSfdc.queue}")
	String ownerDetailsQueue;
	
	@Autowired
	CommercialBulkProcessSiteRepository commercialBulkProcessSiteRepository;
	
	@Value("${oms.bulk.site.process.queue}")
	String omsBulkSiteProcessQueue;

	@Autowired
	ProductSolutionRepository quoteProductSolutionRepository;

	@Autowired
	protected OrderSiteServiceTerminationDetailsRepository orderSiteServiceTerminationDetailsRepository;
	
	@Value("${bulkupload.max.count}")
	String minSiteLength;
	
	@Autowired
	OrderSiteBillingDetailsRepository orderSiteBillingInfoRepository;
	
	@Autowired
	QuoteSiteBillingDetailsRepository quoteSiteBillingInfoRepository;

  @Autowired
    ProductSolutionSiLinkRepository productSolutionSiLinkRepository;

	@Autowired
	private OrderProductSolutionSiLinkRepository orderProductSolutionSiLinkRepository;
	
	@Autowired
	QuoteNplLinkRepository quoteNplLinkRepository;
	
	@Value("${rabbitmq.mf.task.triggered.queue}")
	private String mfTaskTriggeredQueue;
	
	@Autowired
	UtilityAuditRepository utilityAuditRepository;

	@Autowired
	FeasibilityPricingPayloadAuditRepository feasibilityPricingPayloadAuditRepository;
	/**
	 * cloneQuote
	 * 
	 * @param quote
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
	 * 
	 * @author Dinahar V
	 * 
	 *         cofDownloadAccountManagerNotification - This method is used notify
	 *         the account manager when a customer downloads the cof
	 * @param quoteId
	 * @param quoteToLeId
	 * @return String
	 * @throws TclCommonException
	 */
	public String cofDownloadAccountManagerNotification(Integer quoteId, Integer quoteToLeId)
			throws TclCommonException {
		String str = null;
		QuoteToLe quoteToLe = null;
		if (Objects.isNull(quoteId) || Objects.isNull(quoteToLeId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
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
			String userName, String accountManagerEmail, String orderId, String quoteLink, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setCustomerAccountName(customerAccountName);
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderId);
		mailNotificationBean.setQuoteLink(quoteLink);
		mailNotificationBean.setProductName(NPL);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructProductComponents- This
	 *       method constructs the product components
	 * 
	 * @param nplSiteDtos
	 */
	private void constructProductComponents(List<QuoteNplSiteDto> nplSiteDtos) {
		nplSiteDtos.forEach(nplSite -> {
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(nplSite.getId(), QuoteConstants.NPL_SITES.toString());
			if (!productComponents.isEmpty()) {
				List<QuoteProductComponentDto> quoteProdDto = productComponents.stream()
						.map(QuoteProductComponentDto::new).collect(Collectors.toList());
				if (!quoteProdDto.isEmpty()) {
					quoteProdDto.forEach(this::processQuotePrice);
				}
				nplSite.setQuoteProductComponentDtos(quoteProdDto);
			}
		});
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public NplQuoteDetail approvedManualQuotes(Integer quoteId) throws TclCommonException {

		LOGGER.info("Calling method Approve Manual Quotes for quote ID ----> {} ", quoteId);
		NplQuoteDetail detail = null;
		try {
			detail = new NplQuoteDetail();
			String sfdcId = null;
			if (Objects.isNull(quoteId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			
			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			if (optionalQuoteToLe.isPresent() && Objects.nonNull(optionalQuoteToLe.get().getQuoteType())
					&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType()) && (Objects.isNull(optionalQuoteToLe.get().getIsAmended()) || (Objects.nonNull(optionalQuoteToLe.get().getIsAmended()) && optionalQuoteToLe.get().getIsAmended()==0))) {

					LOGGER.info("Entering into ILL approve quote {}",quoteId);
					nplMACDService.approvedMacdManualQuotes(quoteId);

			} else {

				LOGGER.info("Entering Else block since it is not MACD for quote ---> {} ", quote.getQuoteCode());
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
				Map<String, Object> responseCollection = constructOrder(quote, detail);
				order = (Order) responseCollection.get(NplOrderConstants.METHOD_RESPONSE);
					if (!order.getOrderCode().startsWith("NDE")) {
						if (checkO2cEnabled(quote)) {
							LOGGER.info("OnnetWL");
							order.setOrderToCashOrder(CommonConstants.BACTIVE);
							order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
							orderRepository.save(order);
						}
					}
					if(optionalQuoteToLe.isPresent() && Objects.nonNull(optionalQuoteToLe.get().getQuoteType())
							&& "RENEWALS".equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType())) {
						order.setOrderToCashOrder(CommonConstants.BACTIVE);
						order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);	
						orderRepository.save(order);
					}

				//persist method should not be called for Cross connect as no link present in cross connect
				Set<ProductSolution> productSolutions = quote.getQuoteToLes().stream().findFirst().get().getQuoteToLeProductFamilies().stream().findFirst().get().getProductSolutions();
				//LOGGER.info("Product solutuon name is ----> " , productSolutions.stream().findFirst().get().getMstProductOffering().getProductName());
				for(ProductSolution sol : productSolutions){
					if(Objects.nonNull(sol.getMstProductOffering().getProductName())){
						LOGGER.info("Product Name is ----> {} ", sol.getMstProductOffering().getProductName());
					}
					if(Objects.nonNull(sol.getMstProductOffering().getProductName()) &&
							!CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(sol.getMstProductOffering().getProductName())){
						LOGGER.info("Entering persistOrderNplLinkWithOrder method for quote ----> {}  ", quote.getQuoteCode());
						Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
								.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);
						if(!optionalQuoteToLe.get().getQuoteType().equalsIgnoreCase("RENEWALS")) {
						persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
						}
					}
				}
				if(optionalQuoteToLe.get().getQuoteType().equalsIgnoreCase("RENEWALS")) {
				Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
							.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);
				persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
				}

				detail.setOrderId(order.getId());

				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					sfdcId = quoteLe.getTpsSfdcOptyId();
					if (quoteLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
						omsSfdcService.processSiteDetailsRenewals(quoteLe); // renewals-sfdc
					} else {
						omsSfdcService.processSiteDetails(quoteLe);
					}
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
						nplQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),
								cofObjectMapper);
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
				cloneQuoteForNonFeasibileLink(quote);
			}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public NplQuoteDetail approvedQuotes(UpdateRequest request, String ipAddress) throws TclCommonException {
		LOGGER.info("Entering approvedQuotes for quote id {}", request.getQuoteId());
		NplQuoteDetail detail = null;
		try {
			detail = new NplQuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);
			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				Map<String, Object> responseCollection = constructOrder(quote, detail);
				order = (Order) responseCollection.get(NplOrderConstants.METHOD_RESPONSE);
				if (!order.getOrderCode().startsWith("NDE")) {
					if (checkO2cEnabled(quote)) {
						LOGGER.info("OnnetWL");
						order.setOrderToCashOrder(CommonConstants.BACTIVE);
						order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
						orderRepository.save(order);
					}
				}
				if(Objects.nonNull(responseCollection.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP))
						&& responseCollection.containsKey(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP)) {
					Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
							.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);

					if (!optionalQuoteToLe.get().getQuoteType().equalsIgnoreCase("RENEWALS")) {
						persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
					}
				}
				
				if (optionalQuoteToLe.get().getQuoteType().equalsIgnoreCase("RENEWALS")) {
					Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
							.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);
					persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
				}
				detail.setOrderId(order.getId());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					if (quoteLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
						omsSfdcService.processSiteDetailsRenewals(quoteLe); // renewals-sfdc
					} else {
						omsSfdcService.processSiteDetails(quoteLe);
					}
					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

					/*
					 * TODO: May be required later Boolean nat = (request.getCheckList() == null ||
					 * request.getCheckList().equalsIgnoreCase(CommonConstants.NO)) ? Boolean.FALSE
					 * : Boolean.TRUE;
					 */
					Map<String, String> cofObjectMapper = new HashMap<>();
					//Commnt for test Cross connect just remove after testing
					nplQuotePdfService.processCofPdf(quote.getId(), null, null, true, quoteLe.getId(), cofObjectMapper);
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
						nplQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),
								cofObjectMapper);
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
					/**
					 * commented due to requirement change for MSA mapping while optimus journey
					 */
					// uploadMSAIfNotPresent(quoteLe);

				}
			}

			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
					// Trigger ClosedBcr Process
					/*
					 * try { String custId
					 * =quoteLe.getQuote().getCustomer().getErfCusCustomerId().toString(); String
					 * attribute = (String) mqUtils.sendAndReceive(customerSegment,
					 * custId,MDC.get(CommonConstants.MDC_TOKEN_KEY));
					 * if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) { //need
					 * to add approverId instead of last null
					 * omsSfdcService.processeClosedBcr(quote.getQuoteCode(),
					 * quoteLe.getTpsSfdcOptyId(), quoteLe.getCurrencyCode(),
					 * "India",attribute,"PB_SS",true,null,null);
					 * LOGGER.info("Trigger closed Bcr in NplQuoteService"); } else { LOGGER.
					 * info("Failed closed bcr request in NplQuoteService customerAttribute/customerId is Empty"
					 * ); } } catch (TclCommonException e) {
					 * 
					 * LOGGER.warn("Problem in NplQuoteService Trigger Closed Bcr Request");
					 * 
					 * }
					 */
				}

			}
			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileLink(quote);
			}

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}
	
	private Boolean checkO2cEnabled(Quote quote) {
		LOGGER.info("Entering checkO2cEnabled NPLQuoteSerivce for quote {} ", quote.getQuoteCode());
		Boolean status = false;
		// Disable for Partner orders
		if(Objects.nonNull(quote.getEngagementOptyId())) {
			return true;
		}
		List<ProductSolution> productSolutions = productSolutionRepository.findByReferenceCode(quote.getQuoteCode());
		if(productSolutions != null)
			LOGGER.info("product Solutions list size returned {}, is not empty {}", productSolutions.size(), !productSolutions.isEmpty());
		List<QuoteNplLink> nplLinks = nplLinkRepository.findByQuoteIdAndStatus(quote.getId(), (byte) 1);
		if (nplLinks != null && !nplLinks.isEmpty()) {
			LOGGER.info("CheckO2C QuoteNpl Link exists");
			QuoteNplLink link = nplLinks.get(0);
			List<LinkFeasibility> linkFeasibilityList=linkFeasibilityRepository.findByQuoteNplLink_IdAndIsSelected(link.getId(), (byte) 1);
			if(linkFeasibilityList!=null && !linkFeasibilityList.isEmpty()){
				LOGGER.info("CheckO2C QuoteNpl LinkFeasibility exists");
				LinkFeasibility linkFeasibility=linkFeasibilityList.get(0);
				if(("OnnetWL_NPL".equals(linkFeasibility.getFeasibilityMode()) || "OnnetWL".equals(linkFeasibility.getFeasibilityMode()) 
						|| "Onnet Wireline".equals(linkFeasibility.getFeasibilityMode())) 
						 && !isNDEExists(productSolutions)){
					LOGGER.info("CheckO2C QuoteNpl LinkFeasibility Mode OnnetWL");
					status=true;
				}
			}
		} else if(nplLinks != null && nplLinks.isEmpty() && productSolutions != null && !productSolutions.isEmpty() && isCrossConnectExists(productSolutions)) {
			LOGGER.info("Triggering O2C for BSO MMR Cross Connect quote code {}", productSolutions.get(0).getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode());
			status = true;
		}
		return status;
	}

	private boolean isCrossConnectExists(List<ProductSolution> productSolutions) {
		LOGGER.info("Entering isCrossConnectExists for product solution id {}", productSolutions.get(0).getId());
		if(productSolutions!=null && !productSolutions.isEmpty() && 
				productSolutions.stream().filter(solution->solution.getProductProfileData().contains("MMR Cross Connect")).findAny().isPresent()){
				return true;
		}
		return false;
	}

	
	private boolean isNDEExists(List<ProductSolution> productSolutions) {
		if(productSolutions!=null && !productSolutions.isEmpty() && 
				productSolutions.stream().filter(solution->solution.getProductProfileData().contains("National Dedicated Ethernet")).findAny().isPresent()){
				return true;
		}
		return false;
	}

	/**
	 * uploadSSIfNotPresent - upload Service schedule document if its not present
	 * for the legal entity
	 * 
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void uploadSSIfNotPresent(QuoteToLe quoteToLe) throws TclCommonException {
		if (Objects.isNull(quoteToLe)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.SERVICE_SCHEDULE, CommonConstants.BACTIVE);
		for (MstOmsAttribute mstOmsAttribute : mstOmsAttributes) {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
				ServiceScheduleBean serviceScheduleBean = constructServiceScheduleBean(
						quoteToLe.getErfCusCustomerLegalEntityId());
				mqUtils.sendAndReceive(updateSSQueue, Utils.convertObjectToJson(serviceScheduleBean));
				break;
			}
		}
	}

	/**
	 * constructServiceScheduleBean - constructs service schedule bean
	 * 
	 * @param customerLeId
	 * @return
	 */
	public ServiceScheduleBean constructServiceScheduleBean(Integer customerLeId) {
		ServiceScheduleBean serviceScheduleBean = new ServiceScheduleBean();
		serviceScheduleBean.setCustomerLeId(customerLeId);
		serviceScheduleBean.setDisplayName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setIsSSUploaded(true);
		serviceScheduleBean.setName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setProductName(CommonConstants.NPL);
		return serviceScheduleBean;
	}

	/**
	 * processOrderMailNotification
	 * 
	 * @param order
	 * @param quoteToLe
	 * @param cofObjectMapper
	 * @throws TclCommonException
	 */
	protected void processOrderMailNotification(Order order, QuoteToLe quoteToLe, Map<String, String> cofObjectMapper,
			String userEmail) throws TclCommonException {
		String emailId = userEmail != null ? userEmail : customerSupportEmail;
		String leMail = getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),
				emailId, appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName, CommonConstants.NPL,
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
	 * This method is to update audit information
	 * 
	 * @param orderRefUuid
	 * @param publicIp
	 * @param checkList
	 * @throws TclCommonException
	 */
	public void updateOrderConfirmationAudit(String publicIp, String orderRefUuid, String checkList)
			throws TclCommonException {

		try {
			if (Objects.isNull(publicIp) || Objects.isNull(orderRefUuid) || Objects.isNull(checkList)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			String name = Utils.getSource();
			OrderConfirmationAudit orderConfirmationAudit = new OrderConfirmationAudit();
			orderConfirmationAudit.setName(name);
			orderConfirmationAudit.setPublicIp(publicIp);
			orderConfirmationAudit.setOrderRefUuid(orderRefUuid);
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * persistOrderNplLinkWithOrder - perists the link with order
	 * 
	 * @param quoteLinkIdOrderLinkMap
	 * @param orderId
	 * @throws TclCommonException
	 */
	public void persistOrderNplLinkWithOrder(Map<String, OrderNplLink> quoteLinkIdOrderLinkMap, Integer orderId, Order order)
			throws TclCommonException {

		if (Objects.isNull(quoteLinkIdOrderLinkMap) || Objects.isNull(orderId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		if (!quoteLinkIdOrderLinkMap.isEmpty()) {
			for (String strLinkId : quoteLinkIdOrderLinkMap.keySet()) {
				Integer quotelinkId = Integer.parseInt(strLinkId);
				OrderNplLink orderLink = quoteLinkIdOrderLinkMap.get(strLinkId);

				orderLink.setOrderId(orderId);
				orderNplLinkRepository.save(orderLink);
				
				constructOrderSiteToService(orderLink, order,quotelinkId);
				orderLink.setLinkFeasibilities(constructOrderLinkFeasibilities(quotelinkId, orderLink));
				orderLink.setOrderNplLinkSlas(constructOrderLinkSla(quotelinkId, orderLink));
				constructOrderProductComponent(quotelinkId, orderLink);
				persistQuoteSiteCommercialsAtServiceIdLevel(quotelinkId, order);

			}
			//added for save order sitewise billing info
			if (order != null) {
				List<OrderToLe> orderToLe = orderToLeRepository.findByOrder(order);
				if (!orderToLe.isEmpty()) {
					LOGGER.info("get order SiteLevelBilling flag " + orderToLe.get(0).getSiteLevelBilling());
					if (orderToLe.get(0).getSiteLevelBilling() != null) {
						if (orderToLe.get(0).getSiteLevelBilling().equalsIgnoreCase("1")) {
							List<QuoteSiteBillingDetails> quoteSiteBillingDetails = quoteSiteBillingInfoRepository
									.findByQuoteCode(order.getOrderCode());
							LOGGER.info("site billing list size" + quoteSiteBillingDetails.size());
							if (!quoteSiteBillingDetails.isEmpty()) {
								saveOrderSiteBillingDetails(order, quoteSiteBillingDetails);
							}
						}
					}
				}
			}
		}

	}

	private void constructOrderSiteToService(OrderNplLink orderLink, Order order,Integer quoteLinkid) {
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(order.getOrderToLes().stream().findFirst().get().getOrderType()) ||
				MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(order.getOrderToLes().stream().findFirst().get().getOrderType())) {
			LOGGER.info("linkid quote"+quoteLinkid+"orderlinkid"+orderLink.getId());
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(quoteLinkid, order.getQuote().getQuoteToLes().stream().findFirst().get());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				LOGGER.info("quoteIllSiteToServiceList size"+quoteIllSiteToServiceList.size());
					quoteIllSiteToServiceList.stream().forEach(siteToService -> {
						OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
						orderIllSiteToService.setAllowAmendment(siteToService.getAllowAmendment());
						orderIllSiteToService.setErfServiceInventoryParentOrderId(
								siteToService.getErfServiceInventoryParentOrderId());
						orderIllSiteToService.setErfServiceInventoryServiceDetailId(
								siteToService.getErfServiceInventoryServiceDetailId());
						orderIllSiteToService
								.setErfServiceInventoryTpsServiceId(siteToService.getErfServiceInventoryTpsServiceId());
						orderIllSiteToService.setOrderNplLink(orderLink);
						orderIllSiteToService.setOrderToLe(order.getOrderToLes().stream().findFirst().get());
						orderIllSiteToService.setType(siteToService.getType());
						orderIllSiteToService.setErfSfdcOrderType(siteToService.getErfSfdcOrderType());
						orderIllSiteToService.setErfSfdcSubType(siteToService.getErfSfdcSubType());
						orderIllSiteToService.setTpsSfdcOptyId(siteToService.getTpsSfdcOptyId());
						orderIllSiteToService.setTpsSfdcProductId(siteToService.getTpsSfdcProductId());
						orderIllSiteToService.setTpsSfdcProductName(siteToService.getTpsSfdcProductName());
						if (MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(order.getOrderToLes().stream().findFirst().get().getOrderType())) {
							QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);
							constructOrderSiteServiceTerminationDetails (quoteSiteServiceTerminationDetail, orderIllSiteToService);
						}
						orderIllSiteToServiceRepository.save(orderIllSiteToService);

					});
					
				
			}

		}
	}

	/**
	 * constructOrderLinkSla
	 * 
	 * @param quoteLinkId
	 * @param orderLink
	 */
	private Set<OrderNplLinkSla> constructOrderLinkSla(Integer quoteLinkId, OrderNplLink orderLink) {
		Set<OrderNplLinkSla> orderNplLinkSlas = new HashSet<>();

		List<QuoteNplLinkSla> slaList = quoteNplLinkSlaRepository.findByQuoteNplLink_Id(quoteLinkId);

		if (slaList != null && !slaList.isEmpty()) {
			slaList.forEach(nplLinkSla -> {
				OrderNplLinkSla orderNplLinkSla = new OrderNplLinkSla();
				orderNplLinkSla.setOrderNplLink(orderLink);
				orderNplLinkSla.setSlaEndDate(nplLinkSla.getSlaEndDate());
				orderNplLinkSla.setSlaStartDate(nplLinkSla.getSlaStartDate());
				orderNplLinkSla.setSlaValue(nplLinkSla.getSlaValue());
				orderNplLinkSla.setSlaMaster(nplLinkSla.getSlaMaster());
				orderNplLinkSlaRepository.save(orderNplLinkSla);
				orderNplLinkSlas.add(orderNplLinkSla);

			});
		}

		return orderNplLinkSlas;
	}

	/**
	 * 
	 * @param quoteNplLinkId
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent for
	 *       illSite
	 * @param orderLink
	 */
	private List<OrderProductComponent> constructOrderProductComponent(Integer quoteNplLinkId, OrderNplLink orderLink) {

		Optional<QuoteNplLink> optQuoteLink = nplLinkRepository.findById(quoteNplLinkId);
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();

		if (optQuoteLink.isPresent()) {
			QuoteNplLink quoteLink = optQuoteLink.get();
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(quoteLink.getId(), CommonConstants.LINK);
			productComponents.addAll(quoteProductComponentRepository.findByReferenceIdAndType(quoteLink.getSiteAId(),
					CommonConstants.SITEA));
			productComponents.addAll(quoteProductComponentRepository.findByReferenceIdAndType(quoteLink.getSiteBId(),
					CommonConstants.SITEB));

			if (productComponents != null && !productComponents.isEmpty()) {
				for (QuoteProductComponent quoteProductComponent : productComponents) {

					Integer referenceId = null;
					if (quoteProductComponent.getType().equals(CommonConstants.LINK))
						referenceId = orderLink.getId();
					else if (quoteProductComponent.getType().equals(CommonConstants.SITEA))
						referenceId = orderLink.getSiteAId();
					else if (quoteProductComponent.getType().equals(CommonConstants.SITEB))
						referenceId = orderLink.getSiteBId();

					OrderProductComponent orderProductComponent = new OrderProductComponent();
					if (quoteProductComponent.getMstProductComponent() != null) {
						orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
					}
					orderProductComponent.setType(quoteProductComponent.getType());
					orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
					orderProductComponent.setReferenceId(referenceId);

					orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
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
		return orderProductComponents;

	}

	/**
	 * 
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
				orderPrice.setCatalogArc(attrPrice.getCatalogArc());
				orderPrice.setReferenceName(attrPrice.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderAttributeValue.getId()));
				orderPrice.setComputedMrc(attrPrice.getComputedMrc());
				orderPrice.setComputedNrc(attrPrice.getComputedNrc());
				orderPrice.setComputedArc(attrPrice.getComputedArc());
				orderPrice.setDiscountInPercent(attrPrice.getDiscountInPercent());
				orderPrice.setQuoteId(attrPrice.getQuoteId());
				orderPrice.setVersion(1);
				orderPrice.setMinimumMrc(attrPrice.getMinimumMrc());
				orderPrice.setMinimumNrc(attrPrice.getMinimumNrc());
				orderPrice.setMinimumArc(attrPrice.getMinimumArc());
				//orderPriceRepository.save(orderPrice);
			}

		}
		return orderPrice;

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com constructOrderAttribute used to
	 *       construct order attribute
	 * @param quoteProductComponentsAttributeValues
	 * @param orderProductComponent
	 * @param orderProductComponent
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
	 * 
	 * constructOrderComponentPrice used to constrcut order Componenet price
	 * 
	 * @param orderProductComponent
	 * @link http://www.tatacommunications.com/
	 * @param quoteProductComponent
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
				orderPrice.setReferenceName(price.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderProductComponent.getId()));
				orderPrice.setComputedMrc(price.getComputedMrc());
				orderPrice.setComputedNrc(price.getComputedNrc());
				orderPrice.setDiscountInPercent(price.getDiscountInPercent());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPrice.setVersion(VersionConstants.ONE.getVersionNumber());
				orderPrice.setMinimumMrc(price.getMinimumMrc());
				orderPrice.setMinimumNrc(price.getMinimumNrc());
				orderPrice.setEffectiveMrc(price.getEffectiveMrc());
				orderPrice.setEffectiveNrc(price.getEffectiveNrc());
				orderPrice.setEffectiveArc(price.getEffectiveArc());
				orderPrice.setMstProductFamily(price.getMstProductFamily());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPriceRepository.save(orderPrice);

			}
		}
		return orderPrice;

	}

	/**
	 * constructOrderLinkFeasibilities
	 * 
	 * @param quotelinkId
	 * @param orderLink
	 * @return
	 */
	private List<OrderLinkFeasibility> constructOrderLinkFeasibilities(Integer quotelinkId, OrderNplLink orderLink) {
		List<OrderLinkFeasibility> feasibilities = new ArrayList<>();
		List<LinkFeasibility> quoteLinkFeasibilites = linkFeasibilityRepository.findByQuoteNplLink_Id(quotelinkId);
		if (quoteLinkFeasibilites != null && !quoteLinkFeasibilites.isEmpty()) {
			quoteLinkFeasibilites.forEach(quoteFeas -> {
				feasibilities.add(orderLinkFeasibilityRepository.save(new OrderLinkFeasibility(quoteFeas, orderLink)));
			});
		}

		return feasibilities;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @param detail
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> constructOrder(Quote quote, NplQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<String, Object>();
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
		order = orderRepository.save(order);
		responseCollection = constructOrderToLe(quote, order, detail);
		order.setOrderToLes((Set<OrderToLe>) responseCollection.get(NplOrderConstants.METHOD_RESPONSE));
		responseCollection.put(NplOrderConstants.METHOD_RESPONSE, order);
		try {
			if(Objects.nonNull(responseCollection.get(NplOrderConstants.ORDER_NPL_LINK)) && responseCollection.containsKey(NplOrderConstants.ORDER_NPL_LINK)){
				Set<OrderNplLink> orderNplLinks = (Set<OrderNplLink>) responseCollection
						.get(NplOrderConstants.ORDER_NPL_LINK);
				if (order.getOrderToLes() != null && orderNplLinks != null) {
					List<QuoteToLe> quToLes = quoteToLeRepository.findByQuote(quote);
					if(quToLes!=null && !quToLes.isEmpty() && quToLes.get(0).getQuoteType().equalsIgnoreCase("RENEWALS")) {
		
						List<ProductSolutionSiLink> listlinkData=productSolutionSiLinkRepository.findByQuoteToLeId(quToLes.get(0).getId());
						Map<String, String> productSolutionSiLinkMap = listlinkData.stream().collect(
								Collectors.toMap(ProductSolutionSiLink::getServiceId, ProductSolutionSiLink::getCopfId));
					createCopfIdForLinksRenewals(order, orderNplLinks, productSolutionSiLinkMap);
//					  List<OrderNplLink> aList = new ArrayList<OrderNplLink>();
//					  Map<String, OrderNplLink> quoteLinkIdOrderLinkMapr = new HashMap<>();
//						for (OrderNplLink x : orderNplLinks) {
//							x.setOrderId(order.getId());
//							aList.add(x);
//						}
//						aList = orderNplLinkRepository.saveAll(aList);
//						for (OrderNplLink x : aList) {
//							quoteLinkIdOrderLinkMapr.put(x.getId().toString(), x);
//						}
//						Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
//								.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);
//						quoteLinkIdOrderLinkMap.putAll(quoteLinkIdOrderLinkMapr);
//						responseCollection.put(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP, quoteLinkIdOrderLinkMap);
						}else {
					createCopfIdForLinks(order, orderNplLinks);
				
					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error on creating COPF request " + ExceptionUtils.getStackTrace(e));
		}
		return responseCollection;

	}

	protected void createCopfIdForLinks(Order order, Set<OrderNplLink> orderNplLinks) {
		LOGGER.info("------++++++Insider createCopfIdForLinks++++++------");
		List<LinkCOPFDetails> linkCOPFDetailsList = new ArrayList<>();
		try {
			if (order != null) {

				if (order.getOrderToLes() != null) {
					order.getOrderToLes().stream().forEach(orToLe -> {

						if (orToLe.getOrderToLeProductFamilies() != null) {
							orToLe.getOrderToLeProductFamilies().stream().forEach(orToLeProductFamil -> {
								if (orToLeProductFamil.getOrderProductSolutions() != null) {
									orToLeProductFamil.getOrderProductSolutions().stream().forEach(orProductSol -> {
										String quoteCode = order.getOrderCode();
										String currencyCode = orToLe.getCurrencyCode();
										String optyId = orToLe.getTpsSfdcCopfId();
										String productServiceId = null;
										List<ProductSolution> prodSol = productSolutionRepository
												.findByReferenceCode(quoteCode);
										if (prodSol != null && !prodSol.isEmpty()) {
											productServiceId = prodSol.get(0).getTpsSfdcProductName();
										}

										if (orderNplLinks != null) {
											orderNplLinks.stream().forEach(nplLink -> {
												LinkCOPFDetails linkCOPFDetails = new LinkCOPFDetails();
												linkCOPFDetails.setMrcC(nplLink.getMrc().toString());
												linkCOPFDetails.setNrc(nplLink.getNrc().toString());
												linkCOPFDetails.setCopfIdC(CommonConstants.EMPTY);
												Integer siteAId = nplLink.getSiteAId();
												if (siteAId != null) {
													Optional<OrderIllSite> orderIllSite = orderIllSitesRepository
															.findById(siteAId);
													if (orderIllSite.isPresent()
															&& orderIllSite.get().getErfLocSitebLocationId() != null) {
														try {
															LOGGER.info(
																	"Location ID inside constructing link copf details {}",
																	orderIllSite.get().getErfLocSitebLocationId());
															linkCOPFDetails.setCity((String) mqUtils.sendAndReceive(
																	citeDetailByLocationIdQueue,
																	Integer.toString(orderIllSite.get()
																			.getErfLocSitebLocationId())));
														} catch (TclCommonException | IllegalArgumentException e) {
															LOGGER.info("Error while fetching the city from OMS");
														}
													}
												}
												
												if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orToLe.getOrderType())) {
													LOGGER.info("Termination loop to create copf id for links");
													List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.
															findByOrderNplLink_Id(nplLink.getId());
													if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty()) {
														LOGGER.info("fetched order site to service primary key {}", orderIllSiteToServiceList.get(0).getId());
													OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetails = orderSiteServiceTerminationDetailsRepository.
															findByOrderIllSiteToService(orderIllSiteToServiceList.get(0));
													linkCOPFDetails.setEffectiveDateOfTermination(DateUtil.convertDateToString(orderSiteServiceTerminationDetails.getEffectiveDateOfChange()));
													}
												}

												linkCOPFDetailsList.add(linkCOPFDetails);
											});
											if (quoteCode != null && currencyCode != null /*&& optyId != null
													&& productServiceId != null */ && !linkCOPFDetailsList.isEmpty()) {
												try {
													String type="NEW";
													if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orToLe.getOrderType())) {
														type="MACD";
													}
													if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orToLe.getOrderType())) {
														type = "TERMINATION";
													}
													omsSfdcService.processCreateCopf(quoteCode, optyId, currencyCode,
															productServiceId, linkCOPFDetailsList,type);
												} catch (TclCommonException e) {
													LOGGER.warn(ExceptionUtils.getStackTrace(e));
												}
											}
										}
									});
								}
							});
						}
					});
				}

			}
		} catch (Exception e) {
			LOGGER.warn("Error in creating COPF" + ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 * @param quote
	 * @param order
	 * @param detail
	 * @throws TclCommonException
	 */
	private Map<String, Object> constructOrderToLe(Quote quote, Order order, NplQuoteDetail detail) {

		return getOrderToLeBasenOnVersion(quote, order, detail);
	}

	/**
	 * 
	 * @param order
	 * @param detail
	 * @link http://www.tatacommunications.com/
	 */
	private Map<String, Object> getOrderToLeBasenOnVersion(Quote quote, Order order, NplQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<>();
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
				orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
				orderToLe.setOrder(order);
				orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
				orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
				orderToLe.setProposedArc(quoteToLe.getProposedArc());
				orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
				orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
				orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
				orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
				orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
				orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
				orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
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
				orderToLe.setTpsSfdcSecurityDepositAmount(quoteToLe.getTpsSfdcSecurityDepositAmount());
				orderToLe.setCreditCheckTrigerred(quoteToLe.getCreditCheckTriggered());
				orderToLe.setTpsSfdcCreditLimit(quoteToLe.getTpsSfdcCreditLimit());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
				orderToLe.setChangeRequestSummary(quoteToLe.getChangeRequestSummary());

				orderToLe.setQuoteBulkUpdate(quoteToLe.getQuoteBulkUpdate());

				orderToLe.setIsMultiCircuit(quoteToLe.getIsMultiCircuit());
				
				orderToLe.setSiteLevelBilling(quoteToLe.getSiteLevelBilling());

				orderToLeRepository.save(orderToLe);
				orderToLe.setOrdersLeAttributeValues(constructOrderToLeAttribute(orderToLe, quoteToLe));
				detail.getOrderLeIds().add(orderToLe.getId());
				responseCollection = getOrderProductFamilyBasenOnVersion(quoteToLe, orderToLe, detail);
				orderToLe.setOrderToLeProductFamilies(
						(Set<OrderToLeProductFamily>) responseCollection.get("methodResponse"));

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
				responseCollection.put(NplOrderConstants.METHOD_RESPONSE, orderToLes);

			}

		}

		return responseCollection;

	}

	/**
	 * 
	 * @param orderToLe
	 * @param quoteToLe
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
	 * 
	 * @param orderToLe
	 * @link http://www.tatacommunications.com/
	 */
	private Map<String, Object> getOrderProductFamilyBasenOnVersion(QuoteToLe quote, OrderToLe orderToLe,
			NplQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<>();
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
				processEngagement(quoteToLeProductFamily.getQuoteToLe(), quoteToLeProductFamily);
				if(quote.getQuoteType().equalsIgnoreCase("RENEWALS")){
					responseCollection = constructOrderProductSolutionRenewals(quoteToLeProductFamily.getProductSolutions(),
							orderToLeProductFamily, detail);		
				}else {
					responseCollection = constructOrderProductSolution(quoteToLeProductFamily.getProductSolutions(),
							orderToLeProductFamily, detail);
				}				
				orderToLeProductFamily
						.setOrderProductSolutions((Set<OrderProductSolution>) responseCollection.get("methodResponse"));
				orderFamilys.add(orderToLeProductFamily);
				responseCollection.put(NplOrderConstants.METHOD_RESPONSE, orderFamilys);

			}

		}
	return responseCollection;

	}

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
	 * 
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 * @param productSolutions
	 * @param orderToLeProductFamily
	 * @return Set<ProductSolutionBean>
	 */
	private Map<String, Object> constructOrderProductSolution(Set<ProductSolution> productSolutions,
			OrderToLeProductFamily orderToLeProductFamily, NplQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<>();
		Set<OrderProductSolution> orderProductSolution = new HashSet<>();
		Set<OrderNplLink> orderNplLinks = new HashSet<>();
		Map<String, OrderNplLink> quoteLinkIdOrderLinkMapAll = new HashMap<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				List<QuoteIllSite> quoteIllSites = getIllsitesBasenOnVersion(solution);
				if (quoteIllSites != null && !quoteIllSites.isEmpty()) {
					OrderProductSolution oSolution = new OrderProductSolution();
					if (solution.getMstProductOffering() != null) {
						oSolution.setMstProductOffering(solution.getMstProductOffering());
					}
					oSolution.setSolutionCode(solution.getSolutionCode());
					oSolution.setOrderToLeProductFamily(orderToLeProductFamily);
					orderProductSolutionRepository.save(oSolution);
					if (Objects.nonNull(solution.getMstProductOffering().getProductName()) &&
							CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())) {
						Set<OrderIllSite> orderIllsites = crossConnectQuoteService.constructOrderIllSite(quoteIllSites, oSolution,detail);
						oSolution.setOrderIllSites(orderIllsites);
						orderProductSolution.add(oSolution);
						responseCollection.put(NplOrderConstants.METHOD_RESPONSE, orderProductSolution);
					} else {
						responseCollection = constructOrderIllSite(quoteIllSites, oSolution);
						Set<OrderIllSite> orderIllsites = (Set<OrderIllSite>) responseCollection
								.get(NplOrderConstants.METHOD_RESPONSE);
						Map<String, Integer> orderNplSiteIds = (Map<String, Integer>) responseCollection
								.get(NplOrderConstants.ORDER_ILLSITE_ID);
						oSolution.setOrderIllSites(orderIllsites);

						orderProductSolution.add(oSolution);

						responseCollection = processOrderNplLinks(solution, oSolution.getId(), orderNplSiteIds, detail, orderToLeProductFamily);
						orderNplLinks.addAll((Set<OrderNplLink>) responseCollection.get(NplOrderConstants.METHOD_RESPONSE));
						responseCollection.put(NplOrderConstants.METHOD_RESPONSE, orderProductSolution);
						responseCollection.put(NplOrderConstants.ORDER_NPL_LINK, orderNplLinks);		
					}
				}
			}
		}
		return responseCollection;
	}

	/**
	 * processOrderNplLinks
	 * 
	 * @param solution
	 * @param orderSolutionId
	 * @param orderNplSiteIds
	 * @param version
	 * @param detail
	 * @return
	 */
	private Map<String, Object> processOrderNplLinks(ProductSolution solution, Integer orderSolutionId,
			Map<String, Integer> orderNplSiteIds, NplQuoteDetail detail, OrderToLeProductFamily orderToLeProductFamily) {
		Map<String, Object> responseCollection = new HashMap<>();
		Set<OrderNplLink> orderNplLinks = new HashSet<>();
		Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = new HashMap<>();
		List<QuoteNplLink> nplLinks = nplLinkRepository.findByProductSolutionIdAndStatus(solution.getId(), (byte) 1);
		if (nplLinks != null && !nplLinks.isEmpty()) {
			for (QuoteNplLink link : nplLinks) {

				if (link.getFeasibility().equals(CommonConstants.BACTIVE)) {
					OrderNplLink orderNplLink = new OrderNplLink();
					orderNplLink.setCreatedBy(link.getCreatedBy());
					orderNplLink.setCreatedDate(link.getCreatedDate());
					orderNplLink.setLinkCode(link.getLinkCode());
					orderNplLink.setProductSolutionId(orderSolutionId);
					orderNplLink.setSiteAId(orderNplSiteIds.get(Integer.toString(link.getSiteAId())));
					orderNplLink.setSiteBId(orderNplSiteIds.get(Integer.toString(link.getSiteBId())));
					orderNplLink.setStatus(link.getStatus());
					orderNplLink.setChargeableDistance(link.getChargeableDistance());
					orderNplLink.setArc(link.getArc());
					orderNplLink.setMrc(link.getMrc());
					orderNplLink.setNrc(link.getNrc());
					orderNplLink.setLinkType(link.getLinkType());
					orderNplLink.setSiteAType(link.getSiteAType());
					orderNplLink.setSiteBType(link.getSiteBType());
					orderNplLink.setRequestorDate(link.getRequestorDate());
					orderNplLink.setEffectiveDate(link.getEffectiveDate());
					orderNplLink.setFeasibility(link.getFeasibility());
					orderNplLink.setStage(LinkStagingConstants.CONFIGURE_LINK.getStage());
					orderNplLink.setLinkBulkUpdate(link.getLinkBulkUpdate());
					orderNplLinks.add(orderNplLink);
					quoteLinkIdOrderLinkMap.put(link.getId().toString(), orderNplLink);
	
				} else {
					detail.setManualFeasible(true);
				}
				
			

			}
		}
		
		
		responseCollection.put(NplOrderConstants.METHOD_RESPONSE, orderNplLinks);
		responseCollection.put(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP, quoteLinkIdOrderLinkMap);		
		return responseCollection;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution) {
		List<QuoteIllSite> illsites = null;
		illsites = illSiteRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);
		return illsites;

	}

	/**
	 * 
	 * @param oSolution
	 * @link http://www.tatacommunications.com/ constructIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private Map<String, Object> constructOrderIllSite(List<QuoteIllSite> illSites, OrderProductSolution oSolution) {
		Map<String, Object> responseCollection = new HashMap<>();
		Set<OrderIllSite> sites = new HashSet<>();
		Map<String, Integer> orderNplSitesIds = new HashMap<>();
		if (illSites != null && !illSites.isEmpty()) {
			for (QuoteIllSite illSite : illSites) {
				if (illSite.getStatus() == 1) {
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
					orderSite.setMrc(illSite.getMrc());
					orderSite.setSiteCode(illSite.getSiteCode());
					orderSite.setStage(SiteStagingConstants.CONFIGURE_SITES.getStage());
					orderSite.setNrc(illSite.getNrc());
					orderSite.setNplShiftSiteFlag(illSite.getNplShiftSiteFlag());
					orderSite.setErfServiceInventoryTpsServiceId(illSite.getErfServiceInventoryTpsServiceId());
					persistOrderSiteAddress(illSite.getErfLocSitebLocationId(), "b",String.valueOf(orderSite.getId()),QuoteConstants.NPL_SITES.toString());//Site
					persistOrderSiteAddress(illSite.getErfLocSiteaLocationId(), "a",String.valueOf(orderSite.getId()),QuoteConstants.NPL_SITES.toString());//Pop
					orderSite.setErfServiceInventoryTpsServiceId(illSite.getErfServiceInventoryTpsServiceId());
					orderNplSitesRepository.save(orderSite);
					orderNplSitesIds.put(Integer.toString(illSite.getId()), orderSite.getId());
					sites.add(orderSite);
				}
			}
		}
		responseCollection.put(NplOrderConstants.METHOD_RESPONSE, sites);
		responseCollection.put(NplOrderConstants.ORDER_ILLSITE_ID, orderNplSitesIds);
		return responseCollection;
	}
	
	private void persistOrderSiteAddress(Integer erfLocationLocId, String siteType, String referenceId,
			String referenceName) {
		try {
			if (erfLocationLocId != null) {
				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(erfLocationLocId));
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
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in saving the Order IllSite Address", e);
		}
	}

	/**
	 * cloneQuoteForNonFeasibileSite
	 */
	protected void cloneQuoteForNonFeasibileLink(Quote quote) {
		try {
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
		} catch (Exception e) {
			LOGGER.error("Error in creating new Quote for not feasible sites", e);
		}
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
			List<QuoteNplLink> links = nplLinkRepository.findByProductSolutionIdAndStatus(productSolution.getId(),
					CommonConstants.BACTIVE);
			for (QuoteNplLink quoteNplLink : links) {
				if (quoteNplLink.getFeasibility().equals(new Byte("0"))) {
					QuoteNplLink nonQuoteNplLink = cloneNplLink(nonFeasibleProductSolution, quoteNplLink);
					extractNonFeasibleLinkComponents(quoteNplLink, nonQuoteNplLink);
					cloneLinkSlaDetails(quoteNplLink, nonQuoteNplLink);
					cloneLinkFeasilibility(quoteNplLink, nonQuoteNplLink);
					cloneLinkPricingDetails(quoteNplLink, nonQuoteNplLink);
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
	private void cloneLinkPricingDetails(QuoteNplLink quoteNplLink, QuoteNplLink nonQuoteNplLink) {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(quoteNplLink.getLinkCode(), "Discount");
		for (PricingEngineResponse pricingDetail : pricingDetails) {
			PricingEngineResponse nonFeasiblepricingDetail = new PricingEngineResponse();
			nonFeasiblepricingDetail.setDateTime(pricingDetail.getDateTime());
			nonFeasiblepricingDetail.setPriceMode(pricingDetail.getPriceMode());
			nonFeasiblepricingDetail.setPricingType(pricingDetail.getPricingType());
			nonFeasiblepricingDetail.setRequestData(pricingDetail.getRequestData());
			nonFeasiblepricingDetail.setResponseData(pricingDetail.getResponseData());
			nonFeasiblepricingDetail.setSiteCode(nonQuoteNplLink.getLinkCode());
			pricingDetailsRepository.save(nonFeasiblepricingDetail);
		}
	}

	/**
	 * cloneFeasilibility
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void cloneLinkFeasilibility(QuoteNplLink quoteNplLink, QuoteNplLink nonQuoteNplLink) {
		List<LinkFeasibility> linkFeasiblities = linkFeasibilityRepository.findByQuoteNplLink(quoteNplLink);
		for (LinkFeasibility linkFeasibility : linkFeasiblities) {
			LinkFeasibility nonFeasibleLinkFeasibility = new LinkFeasibility();
			nonFeasibleLinkFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
			nonFeasibleLinkFeasibility.setFeasibilityCheck(linkFeasibility.getFeasibilityCheck());
			nonFeasibleLinkFeasibility.setFeasibilityCode(linkFeasibility.getFeasibilityCode());
			nonFeasibleLinkFeasibility.setFeasibilityMode(linkFeasibility.getFeasibilityMode());
			nonFeasibleLinkFeasibility.setIsSelected(linkFeasibility.getIsSelected());
			nonFeasibleLinkFeasibility.setProvider(linkFeasibility.getProvider());
			nonFeasibleLinkFeasibility.setQuoteNplLink(nonQuoteNplLink);
			nonFeasibleLinkFeasibility.setRank(linkFeasibility.getRank());
			nonFeasibleLinkFeasibility.setResponseJson(linkFeasibility.getResponseJson());
			nonFeasibleLinkFeasibility.setType(linkFeasibility.getType());
			linkFeasibilityRepository.save(nonFeasibleLinkFeasibility);
		}
	}

	/**
	 * cloneSlaDetails
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void cloneLinkSlaDetails(QuoteNplLink quoteNplLink, QuoteNplLink nonQuoteNplLink) {
		List<QuoteNplLinkSla> slaDetails = quoteNplLinkSlaRepository.findByQuoteNplLink(quoteNplLink);
		for (QuoteNplLinkSla quoteNplLinkSla : slaDetails) {
			QuoteNplLinkSla nonFeasibileQuoteNplLinkSla = new QuoteNplLinkSla();
			nonFeasibileQuoteNplLinkSla.setQuoteNplLink(nonQuoteNplLink);
			nonFeasibileQuoteNplLinkSla.setSlaEndDate(quoteNplLinkSla.getSlaEndDate());
			nonFeasibileQuoteNplLinkSla.setSlaMaster(quoteNplLinkSla.getSlaMaster());
			nonFeasibileQuoteNplLinkSla.setSlaStartDate(quoteNplLinkSla.getSlaStartDate());
			nonFeasibileQuoteNplLinkSla.setSlaValue(quoteNplLinkSla.getSlaValue());
			quoteNplLinkSlaRepository.save(nonFeasibileQuoteNplLinkSla);
		}
	}

	/**
	 * cloneIllSite
	 * 
	 * @param nonFeasibleProductSolution
	 * @param quoteIllSite
	 */
	private QuoteNplLink cloneNplLink(ProductSolution nonFeasibleProductSolution, QuoteNplLink quoteNplLink) {
		QuoteNplLink nonQuoteNplLink = new QuoteNplLink();
		nonQuoteNplLink.setId(quoteNplLink.getId());
		nonQuoteNplLink.setLinkCode(quoteNplLink.getLinkCode());
		nonQuoteNplLink.setProductSolutionId(nonFeasibleProductSolution.getId());
		nonQuoteNplLink.setSiteAId(quoteNplLink.getSiteAId());
		nonQuoteNplLink.setSiteBId(quoteNplLink.getSiteBId());
		nonQuoteNplLink.setStatus(quoteNplLink.getStatus());
		nonQuoteNplLink.setQuoteId(quoteNplLink.getQuoteId());
		nonQuoteNplLink.setRequestorDate(quoteNplLink.getRequestorDate());
		nonQuoteNplLink.setArc(quoteNplLink.getArc());
		nonQuoteNplLink.setMrc(quoteNplLink.getMrc());
		nonQuoteNplLink.setNrc(quoteNplLink.getNrc());
		nonQuoteNplLink.setLinkType(quoteNplLink.getLinkType());
		nonQuoteNplLink.setStatus(quoteNplLink.getStatus());
		nonQuoteNplLink.setChargeableDistance(quoteNplLink.getChargeableDistance());
		nonQuoteNplLink.setCreatedBy(quoteNplLink.getCreatedBy());
		nonQuoteNplLink.setCreatedDate(quoteNplLink.getCreatedDate());
		nonQuoteNplLink.setWorkflowStatus(quoteNplLink.getWorkflowStatus());
		nonQuoteNplLink.setSiteAType(quoteNplLink.getSiteAType());
		nonQuoteNplLink.setSiteBType(quoteNplLink.getSiteBType());
		nonQuoteNplLink.setEffectiveDate(quoteNplLink.getEffectiveDate());
		nonQuoteNplLink.setFeasibility(quoteNplLink.getFeasibility());
		nplLinkRepository.save(nonQuoteNplLink);
		return nonQuoteNplLink;
	}

	/**
	 * extractNonFeasibleComponents
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void extractNonFeasibleLinkComponents(QuoteNplLink quoteNplLink, QuoteNplLink nonQuoteNplLink) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(quoteNplLink.getId(), CommonConstants.LINK);
		for (QuoteProductComponent quoteProductComponent : productComponents) {
			QuoteProductComponent nonFeasibleProductComponent = cloneProductLinkComponent(nonQuoteNplLink,
					quoteProductComponent);
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent(quoteProductComponent);
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
	private QuoteProductComponent cloneProductLinkComponent(QuoteNplLink nonQuoteNplLink,
			QuoteProductComponent quoteProductComponent) {
		QuoteProductComponent nonFeasibleProductComponent = new QuoteProductComponent();
		nonFeasibleProductComponent.setReferenceId(nonQuoteNplLink.getId());
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

	/**
	 * @author Dinahar Vivekanandan deactivateLink -This method deletes or disables
	 *         the NPL link based on the action
	 * @param linkId
	 * @param action
	 * @return String
	 */
	@Transactional
	public String procesDeActivateLink(Integer linkId, String action) throws TclCommonException {
		String result = "FAILURE";
		try {

			if (linkId == null)
				throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);

			if (action == null || !(action.equals(QuoteConstants.DELETE.toString())
					|| action.equals(QuoteConstants.DISABLE.toString())))
				throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);

			Optional<QuoteNplLink> optNplLink = nplLinkRepository.findById(linkId);
			if (!optNplLink.isPresent())
				throw new TclCommonException(ExceptionConstants.NPL_LINK_UNAVAILABLE,
						ResponseResource.R_CODE_NOT_FOUND);
			Optional<ProductSolution> optProdSol = productSolutionRepository
					.findById(optNplLink.get().getProductSolutionId());
			if (!optProdSol.isPresent())
				throw new TclCommonException(ExceptionConstants.NPL_LINK_UNAVAILABLE,
						ResponseResource.R_CODE_NOT_FOUND);

			QuoteToLe quoteToLe = optProdSol.get().getQuoteToLeProductFamily().getQuoteToLe();

			deActivateLinkAndSites(optNplLink.get(), action);
			nplPricingFeasibilityService.recalculateSites(quoteToLe.getId());
			result = "SUCCESS";

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return result;

	}

	/**
	 * deActivateLinkAndSites
	 * 
	 * @param link
	 * @param action
	 */
	private void deActivateLinkAndSites(QuoteNplLink link, String action) {
		deActivateLink(link, action);
		deActivateSite(link.getSiteAId(), action, CommonConstants.SITEA);
		deActivateSite(link.getSiteBId(), action, CommonConstants.SITEB);
	}

	/**
	 * @author Dinahar Vivekanandan deActivateSite - Method to deactivate a site
	 *         based on site id
	 * @param siteId
	 * @param action - delete or Deactivate
	 * @throws TclCommonException
	 */
	private void deActivateSite(Integer siteId, String action, String compType) {

		QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
		if (quoteIllSite != null) {
			if (action.equals(QuoteConstants.DELETE.toString())) {
				removeComponentsAndAttr(siteId, compType);
				deleteNplsiteAndRelation(quoteIllSite);

			} else if (action.equals(QuoteConstants.DISABLE.toString())) {
				quoteIllSite.setStatus((byte) 0);
				illSiteRepository.save(quoteIllSite);
			}
		}
	}

	/**
	 * @author Dinahar Vivekanandan deActivateLink - Method to deactivate a link
	 * @param linkId
	 * @param action - delete or Deactivate
	 * @throws TclCommonException
	 */
	private void deActivateLink(QuoteNplLink nplLink, String action) {

		if (nplLink != null) {
			if (action.equals(QuoteConstants.DELETE.toString())) {
				removeComponentsAndAttr(nplLink.getId(), CommonConstants.LINK);
				removeQuoteIllSitetoServiceValues(nplLink.getId(),nplLink.getQuoteId());
				quoteNplLinkSlaRepository.deleteAll(quoteNplLinkSlaRepository.findByQuoteNplLink(nplLink));
				List<LinkFeasibility> linkFeasibilityList = linkFeasibilityRepository.findByQuoteNplLink(nplLink);
				if (linkFeasibilityList != null && !linkFeasibilityList.isEmpty()) {
					linkFeasibilityList.forEach(linkFeasibility -> {
						List<LinkFeasibilityAudit> linkFeasibilityAuditList = linkFeasibilityAuditRepository
								.findByLinkFeasibility(linkFeasibility);
						if (!linkFeasibilityAuditList.isEmpty())
							linkFeasibilityAuditRepository.deleteAll(linkFeasibilityAuditList);
						linkFeasibilityRepository.delete(linkFeasibility);
					});
				}
				nplLinkRepository.delete(nplLink);
			} else if (action.equals(QuoteConstants.DISABLE.toString())) {
				nplLink.setStatus((byte) 0);
				nplLinkRepository.save(nplLink);
			}
		}
	}

	private void removeQuoteIllSitetoServiceValues(Integer linkId, Integer quoteId)
	{
		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId);
		QuoteNplLink link = nplLinkRepository.findByIdAndStatus(linkId, BACTIVE);
		LOGGER.info("Link Id Before deleting value in QuoteIllSiteService: "+link.getId());
		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get(0).getQuoteType())) {
			List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(linkId, quoteToLe.get(0));
			if(Objects.nonNull(quoteIllSiteToService) && !(quoteIllSiteToService.isEmpty()))
			{
				//added for nde mc
				for(QuoteIllSiteToService quoteIllSiteToServ:quoteIllSiteToService) {
					quoteIllSiteToServ.setQuoteNplLink(null);
				    quoteIllSiteToServiceRepository.save(quoteIllSiteToServ);
				}
				
				LOGGER.info("Removed link from QuoteIllSiteToServiceRepository");
			}
		}

	}

	/**
	 * @author Dinahar V
	 * @param quoteIllSite
	 * @link http://www.tatacommunications.com/ deletedIllsiteAndRelation used to
	 *       delete ill site and its relation
	 * 
	 * @param quoteIllSite
	 */
	private void deleteNplsiteAndRelation(QuoteIllSite quoteIllSite) {
		List<QuoteIllSiteSla> slas = quoteIllSiteSlaRepository.findByQuoteIllSite(quoteIllSite);
		if (slas != null && !slas.isEmpty()) {
			slas.forEach(sl -> {
				quoteIllSiteSlaRepository.delete(sl);

			});
		}
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibilities.forEach(site -> {
				siteFeasibilityRepository.delete(site);

			});
		}

		illSiteRepository.delete(quoteIllSite);

	}

	/**
	 * saveQuote
	 * 
	 * @author Thamizhselvi Perumal updateLink - This method is used to update the
	 *         link Informations
	 * 
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @param quoteId
	 * @return QuoteResponse
	 * @throws TclCommonException
	 */
	public NplQuoteBean updateLink(NplQuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId)
			throws TclCommonException {
		validateSiteInformation(quoteDetail, quoteId);
		Integer linkCount = quoteDetail.getLinkCount();
		NplQuoteBean quoteBean = null;
		LOGGER.info("link count{}", linkCount);
		while (linkCount > 0) {

			Integer siteA = null;
			Integer siteB = null;
			Integer siteId = null;
			Integer locA = null;
			Integer locB = null;
			ProductSolution productSolution = null;
			String siteAType = "";
			String siteBType = "";
			String productOfferingName = null;
			try {
				LOGGER.info("Customer Id received is {}", erfCustomerId);

				User user = getUserId(Utils.getSource());

				List<NplSite> sites = quoteDetail.getSite();
				MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());

				if (quoteToLe.isPresent() && sites.size() == 2 && user != null) {
					for (NplSite site : sites) {
						QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
								.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
						productOfferingName = site.getOfferingName();
						MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName,
								user);
						productSolution = productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(
								quoteToLeProductFamily, productOfferng);
						siteId = processSiteDetail(user, productFamily, quoteToLeProductFamily, site,
								productOfferingName, productSolution, quoteToLe.get().getQuote(), site.getSite().get(0).getSiteChangeflag());
						if (siteA == null) {
							siteA = siteId;
							siteAType = site.getType().getType();
							locA = site.getSite().get(0).getLocationId();
						} else {
							siteB = siteId;
							siteBType = site.getType().getType();
							locB = site.getSite().get(0).getLocationId();

							// Multiple profile usecase
							/*
							 * if (siteA != null && siteB != null) { constructNplLinks(user.getId(),
							 * quoteDetail.getQuoteId(), siteA, siteB, productSolution); siteA=null;
							 * siteB=null; } else throw new
							 * TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
							 * ResponseResource.R_CODE_NOT_FOUND);
							 * 
							 */
						}

					}
					quoteDetail.setQuoteId(quoteId);

					if (siteA != null && siteB != null)
						constructNplLinks(user, productFamily, quoteDetail.getQuoteId(), siteA, siteB, productSolution,
								siteAType, siteBType, productOfferingName, locA, locB, null,null,null);

				} else {
					throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
							ResponseResource.R_CODE_NOT_FOUND);
				}

				if (quoteToLe.get().getStage().equals(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode())) {
					quoteToLe.get().setStage(QuoteStageConstants.ADD_LOCATIONS.getConstantCode());
					quoteToLeRepository.save(quoteToLe.get());
				}
			} catch (Exception e) {
				LOGGER.error(String.format("Message:  %s", e.getMessage()));
				LOGGER.error("Cause: ", e.getCause());
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			linkCount--;
		}
		/*//NDE SFDC CREATE PRODUCT CALL add same method in edit link componenet due to sfdc ehs id not getting updated issue fix 
		try {
			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if (quote.get().getQuoteCode().startsWith("NDE")) {
				LOGGER.info("Before trigger sfdc product call for NDE");
				List<QuoteToLe> quotele = quoteToLeRepository.findByQuote(quote.get());
				List<ThirdPartyServiceJob> thirdPartyService = thirdPartyServiceJobsRepository
						.findByRefIdAndServiceTypeAndThirdPartySource(quote.get().getQuoteCode(), "CREATE_OPPORTUNITY",
								"SFDC");
				if (thirdPartyService.size() != 0) {
					List<ThirdPartyServiceJob> thirdPartyServiceProduct = thirdPartyServiceJobsRepository
							.findByRefIdAndServiceTypeAndThirdPartySource(quote.get().getQuoteCode(), "CREATE_PRODUCT",
									"SFDC");
					LOGGER.info("sfdc product call size"+thirdPartyServiceProduct.size());
					if (thirdPartyServiceProduct.size() == 0) {
						LOGGER.info("Before TRIGGER sfdc product create call");
						omsSfdcService.processProductServices(quotele.get(0), thirdPartyService.get(0).getTpsId());
					}

				}

			}
		}
		catch(Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause NDE product call: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			
		}*/
		quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), false);
		return quoteBean;
	}

	/**
	 * updateUploadLink
	 *
	 * @author Thamizhselvi Perumal updateUploadLink - This method is used to update
	 *         the link Informations
	 *
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @param quoteId
	 * @return QuoteResponse
	 * @throws TclCommonException
	 */
	public String updateUploadLink(NplLinksUpdateBean nplQuoteDetail) throws TclCommonException {
		String message = null;
		if (Objects.isNull(nplQuoteDetail)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		nplQuoteDetail.getLinks().stream().forEach(link -> {
			try {
				NplQuoteDetail quoteDetail = new NplQuoteDetail();
				quoteDetail.setCustomerId(nplQuoteDetail.getCustomerId());
				quoteDetail.setOrderId(nplQuoteDetail.getOrderId());
				quoteDetail.setOrderLeIds(nplQuoteDetail.getOrderLeIds());
				quoteDetail.setProductName(nplQuoteDetail.getProductName());
				quoteDetail.setQuoteId(nplQuoteDetail.getQuoteId());
				quoteDetail.setQuoteleId(nplQuoteDetail.getQuoteleId());
				quoteDetail.setManualFeasible(nplQuoteDetail.isManualFeasible());
				quoteDetail.setSolutions(nplQuoteDetail.getSolutions());
				quoteDetail.setSite(link.getLink());
				quoteDetail.setLinkCount(1);
				NplQuoteBean nplQuoteBean = updateLink(quoteDetail, quoteDetail.getCustomerId(),
						quoteDetail.getQuoteId());
				if (Objects.isNull(nplQuoteBean))
					nplQuoteDetail.setSuccess(false);
			} catch (Exception e) {
				throw new TclCommonRuntimeException(e);
			}
		});

		if (nplQuoteDetail.getSuccess())
			message = "Links are updated successfully";
		else
			message = "Links are not updated successfully";

		return message;
	}

	/**
	 * This method is used for validating the site information
	 * validateSiteInformation
	 * 
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	protected void validateSiteInformation(NplQuoteDetail quoteDetail, Integer quoteId) throws TclCommonException {
		if ((quoteDetail == null) || quoteDetail.getSite() == null || quoteDetail.getLinkCount() == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
		if (Objects.isNull(quoteId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * getUserId-This method get the user details if present or persist the user and
	 * get the entity
	 * 
	 * @param username
	 * @return User
	 */
	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
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
	 * @link http://www.tatacommunications.com/ getQuoteDetails- This method is used
	 *       to get the quote details
	 * 
	 * @param quoteId
	 * @param feasibleSites
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */
	public NplQuoteBean getQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProp)
			throws TclCommonException {
		NplQuoteBean response = null;
		try {
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString())) ? true : false;
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote, isFeasibleSites, isSiteProp);
			
			Optional<QuoteToLe> quoteToLeOpt = quote.getQuoteToLes().stream().filter(quoteToLe -> MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())).findFirst();
			if(quoteToLeOpt.isPresent()) {
				response.setQuoteType(quoteToLeOpt.get().getQuoteType());
				response.setQuoteCategory(quoteToLeOpt.get().getQuoteCategory());
			}

			Optional<QuoteToLe> quoteToLer = quote.getQuoteToLes().stream().findFirst()
					.filter(quoteToLe -> "RENEWALS".equalsIgnoreCase(quoteToLe.getQuoteType()));

			if (quoteToLer.isPresent()) {
				response.setQuoteType(quoteToLer.get().getQuoteType());
				response.setIsCommercialChanges(findIsCommercial(quoteToLer).charAt(0));
				response.setIsMultiCircuit(quoteToLer.get().getIsMultiCircuit() == 1 ? true : false);
			}
			//Return quote type for termination
			Optional<QuoteToLe> quoteToLeOptTerm = quote.getQuoteToLes().stream().filter(quoteToLe -> MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())).findFirst();
			if(quoteToLeOptTerm.isPresent()) {
				response.setQuoteType(quoteToLeOptTerm.get().getQuoteType());
			}

			if(!quoteToLeOptTerm.isPresent()) {  // excluding quote access check for termination
				extractQuoteAccessPermission(response, quote);
			}
			Optional<User> user=userRepository.findById(quote.getCreatedBy());
			if(user.isPresent()) {
				response.setQuoteCreatedUserType(user.get().getUserType());
			}

			//PIPF-212
			getLeOwnerDetailsSFDC(response, quote);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_GET_QUOTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	private void extractQuoteAccessPermission(NplQuoteBean response, Quote quote) {
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
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @throws TclCommonException
	 */
	protected NplQuoteBean constructQuote(Quote quote, Boolean isFeasibleSites, Boolean isSiteProp)
			throws TclCommonException {
		NplQuoteBean quoteDto = new NplQuoteBean();
		quoteDto.setQuoteId(quote.getId());
		quoteDto.setQuoteCode(quote.getQuoteCode());
		quoteDto.setCreatedBy(quote.getCreatedBy());
		quoteDto.setCreatedTime(quote.getCreatedTime());
		quoteDto.setStatus(quote.getStatus());
		quoteDto.setTermInMonths(quote.getTermInMonths());
		Opportunity optyentity=opportunityRepository.findByUuid(quote.getQuoteCode());
		if(optyentity!=null) {
			quoteDto.setOpportunityId(optyentity.getId()+"");
		}
		List<QuoteToLe> quoteToLe = new ArrayList<QuoteToLe>();
		quoteToLe = quoteToLeRepository.findByQuote_Id(quote.getId());
		List<CommercialQuoteAudit> audit=commercialQuoteAuditRepository.findByQuoteId(quote.getId());
		if(audit.isEmpty() || audit.size()==0) {
			quoteDto.setIsInitialCommercialTrigger(true);
		}
		if (quoteToLe.size() != 0) {
			quoteDto.setQuoteStatus(quoteToLe.get(0).getCommercialStatus());
			quoteDto.setQuoteRejectionComment(quoteToLe.get(0).getQuoteRejectionComment());
			if (quoteToLe.get(0).getCommercialQuoteRejectionStatus() != null) {
				if (quoteToLe.get(0).getCommercialQuoteRejectionStatus().equalsIgnoreCase("1")) {
					quoteDto.setQuoteRejectionStatus(true);
				} else {
					quoteDto.setQuoteRejectionStatus(false);
				}
				LOGGER.info(" quote Commercial Status and rejection status and comments npl is set as  {}", quoteToLe.get(0).getCommercialStatus()+":"+quoteToLe.get(0).getCommercialQuoteRejectionStatus()
						+":"+quoteToLe.get(0).getCommercialQuoteRejectionStatus());
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
		quoteDto.setLegalEntities(constructQuoteLeEntitDtos(quote, isFeasibleSites, isSiteProp));

		OrderConfirmationAudit auditEntity = orderConfirmationAuditRepository
				.findByOrderRefUuid(quoteDto.getQuoteCode());
		if (auditEntity != null && auditEntity.getPublicIp() != null) {
			quoteDto.setPublicIp(getPublicIp(auditEntity.getPublicIp()));
		} else {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			String forwardedIp = request.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
			LOGGER.info("Audit Public IP is {} ", forwardedIp);
			quoteDto.setPublicIp(getPublicIp(forwardedIp));
		}
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quoteDto.getQuoteCode());
		quoteDto.setIsDocusign(docusignAudit != null);
		quoteDto.setCustomerName(quote.getCustomer().getCustomerName());

		//added for multisite
		Integer totalLinkCount = getTotalLinkCount(quote.getId());
		LOGGER.info("total link Count {} ", totalLinkCount);
		quoteDto.setTotalLinkCount(totalLinkCount);
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
	 * getPublicIp
	 */
	public String getPublicIp(String publicIp) {
		if(publicIp!=null) {
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
	}
		return null;
	}

	/**
	 * checkQuoteLeFeasibility - this method checks the pricing and feasibility for
	 * the given quote legal entity id.
	 * 
	 * checkQuoteLeFeasibility
	 * 
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
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 * @param quote
	 * @throws TclCommonException
	 */

	private Set<QuoteToLeBean> constructQuoteLeEntitDtos(Quote quote, Boolean isFeasibleSites, Boolean isSiteProp)
			throws TclCommonException {

		Set<QuoteToLeBean> quoteToLeDtos = new HashSet<>();
		if ((quote != null) && (getQuoteToLeBasenOnVersion(quote)) != null) {
			for (QuoteToLe quTle : getQuoteToLeBasenOnVersion(quote)) {
				QuoteToLeBean quoteToLeDto = new QuoteToLeBean(quTle);
				quoteToLeDto.setClassification(Objects.nonNull(quTle.getClassification()) ? quTle.getClassification() : null);
				quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
				quoteToLeDto.setCurrency(quTle.getCurrencyCode());
				quoteToLeDto.setClassification(quTle.getClassification());
				quoteToLeDto.setLegalAttributes(constructLegalAttributes(quTle));
				quoteToLeDto.setProductFamilies(constructQuoteToLeFamilyDtos(getProductFamilyBasenOnVersion(quTle),
						isFeasibleSites, isSiteProp));
				quoteToLeDtos.add(quoteToLeDto);

			}
		}

		return quoteToLeDtos;

	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	protected List<QuoteToLe> getQuoteToLeBasenOnVersion(Quote quote) {
		List<QuoteToLe> quToLes = null;
		quToLes = quoteToLeRepository.findByQuote(quote);
		return quToLes;

	}

	/**
	 * constructLegalAttributes
	 * 
	 * @param quTle
	 * @return
	 */
	private Set<LegalAttributeBean> constructLegalAttributes(QuoteToLe quTle) {

		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		List<QuoteLeAttributeValue> attributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quTle);
		if (attributeValues != null && !attributeValues.isEmpty()) {

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
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
			mstOmsAttributeBean.setName(mstOmsAttribute.getName());
			mstOmsAttributeBean.setId(mstOmsAttribute.getId());
		}
		return mstOmsAttributeBean;
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
	 * @link http://www.tatacommunications.com/ constructQuoteToLeFamilyDtos
	 * @param quoteToLeProductFamilies
	 * @throws TclCommonException
	 */
	private Set<QuoteToLeProductFamilyBean> constructQuoteToLeFamilyDtos(
			List<QuoteToLeProductFamily> quoteToLeProductFamilies, Boolean isFeasibleSites, Boolean isSiteProp)
			throws TclCommonException {

		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilyBeans = new HashSet<>();
		if (quoteToLeProductFamilies != null && !quoteToLeProductFamilies.isEmpty()) {
			for (QuoteToLeProductFamily quFamily : quoteToLeProductFamilies) {
				QuoteToLeProductFamilyBean quoteToLeProductFamilyBean = new QuoteToLeProductFamilyBean();
				if (quFamily.getMstProductFamily() != null) {
					quoteToLeProductFamilyBean.setStatus(quFamily.getMstProductFamily().getStatus());
					quoteToLeProductFamilyBean.setProductName(quFamily.getMstProductFamily().getName());
				}
				List<ProductSolutionBean> solutionBeans = getSortedSolution(constructProductSolution(
						getProductSolutionBasenOnVersion(quFamily), isFeasibleSites, isSiteProp));
				quoteToLeProductFamilyBean.setSolutions(solutionBeans);
				quoteToLeProductFamilyBeans.add(quoteToLeProductFamilyBean);

			}
		}

		return quoteToLeProductFamilyBeans;
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<ProductSolution> getProductSolutionBasenOnVersion(QuoteToLeProductFamily family) {
		List<ProductSolution> productSolutions = null;
		productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(family);
		return productSolutions;

	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 * @param productSolutions
	 * @return Set<ProductSolutionBean>
	 * @throws TclCommonException
	 */

	private List<ProductSolutionBean> constructProductSolution(List<ProductSolution> productSolutions,
			Boolean isFeasibleSites, Boolean isSiteProp) throws TclCommonException {

		List<ProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null && !productSolutions.isEmpty()) {
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
				if(Objects.nonNull(solution.getMstProductOffering().getProductName()) &&
						CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())){
					List<QuoteIllSiteBean> illSiteBeans = crossConnectQuoteService.getSortedIllSiteDtos(crossConnectQuoteService.constructIllSiteDtos(
							crossConnectQuoteService.getIllsitesBasenOnVersion(solution, null), isFeasibleSites, isSiteProp));
					productSolutionBean.setCrossConnectSite(illSiteBeans);

				}else {
					productSolutionBean
							.setLinks(getSortedNplLinks(constructNplLinkBeans(solution, isFeasibleSites, isSiteProp)));
				}

				productSolutionBeans.add(productSolutionBean);

			}
		}
		return productSolutionBeans;
	}

	/**
	 * constructNplLinkBean
	 * 
	 * @param solution
	 * @return Set<NplLinkBean>
	 * @throws TclCommonException
	 */
	private List<NplLinkBean> constructNplLinkBeans(ProductSolution solution, Boolean isFeasibleSites,
			Boolean isSiteProp) {
		List<QuoteNplLink> listOfLinks = null;
		if (isFeasibleSites)
			listOfLinks = nplLinkRepository.findByProductSolutionIdAndStatus(solution.getId(), (byte) 1);
		else
			listOfLinks = nplLinkRepository.findByProductSolutionIdAndStatusAndFeasibility(solution.getId(), (byte) 1,
					(byte) 1);

		if (Objects.nonNull(solution.getMstProductOffering().getProductName())
				&& FPConstants.NATIONAL_DEDICATED_ETHERNET.toString()
						.equalsIgnoreCase(solution.getMstProductOffering().getProductName())) {
			LOGGER.info("Entered into NDE Component block");
			return (listOfLinks == null || listOfLinks.isEmpty()) ? new ArrayList<>()
					: listOfLinks.stream().map(link -> constructNdeLinkBean(link, isFeasibleSites, isSiteProp))
							.collect(Collectors.toList());
		} else {
			LOGGER.info("Entered into NPL Component block");
			return (listOfLinks == null || listOfLinks.isEmpty()) ? new ArrayList<>()
					: listOfLinks.stream().map(link -> constructNplLinkBean(link, isFeasibleSites, isSiteProp))
							.collect(Collectors.toList());
		}
	}

	/*
	 * constructNplLinkBean - method to construct NplLinkBean
	 * 
	 * @param link
	 * 
	 * @param version
	 * 
	 * @return NplLinkBean
	 */
	private NplLinkBean constructNplLinkBean(QuoteNplLink link, Boolean isFeasibleSites, Boolean isSiteProp) {
		if (isSiteProp == null) {
			isSiteProp = false;
		}
		NplLinkBean linkDto = new NplLinkBean(link);
		Map<String, QuoteIllSite> siteMap = new HashMap<>();

		if (link.getStatus() == 1) {
			List<QuoteProductComponentBean> linkComp = getSortedComponents(
					constructQuoteProductComponent(link.getId(), false, "Link", isSiteProp));
			linkDto.setComponents(linkComp);

			List<QuoteProductComponentBean> siteAComp = getSortedComponents(
					constructQuoteProductComponent(link.getSiteAId(), false, "Site-A", isSiteProp));
			linkDto.getComponents().addAll(siteAComp);

			List<QuoteProductComponentBean> siteBComp = getSortedComponents(
					constructQuoteProductComponent(link.getSiteBId(), false, "Site-B", isSiteProp));
			linkDto.getComponents().addAll(siteBComp);
		}

		siteMap.put(link.getSiteAId() + "_" + link.getSiteAType(), illSiteRepository.findById(link.getSiteAId()).get());
		siteMap.put(link.getSiteBId() + "_" + link.getSiteBType(), illSiteRepository.findById(link.getSiteBId()).get());
		linkDto.setSites(getSortedNplSiteBeans(constructNplSiteBeans(siteMap, isFeasibleSites)));

		linkDto.setLinkFeasibility(constructLinkFeasibility(link));
		linkDto.setQuoteSla(constructNplSlaDetails(link));

		// Added for NPL MF
        if(link.getMfTaskTriggered()!=null) {
        linkDto.setMfTaskTriggered(link.getMfTaskTriggered());
        }
        else {
            linkDto.setMfTaskTriggered(0);

        }
	
        linkDto.setIsTaskTriggered(link.getIsTaskTriggered());
        //add rejection flag
      		if (link.getCommercialRejectionStatus() != null) {
      			if (link.getCommercialRejectionStatus().equalsIgnoreCase("1")) {
      				linkDto.setRejectionStatus(true);
      			} else {
      				linkDto.setRejectionStatus(false);
      			}
      		}
      		//approve flag
      		if (link.getCommercialApproveStatus() != null) {
      			if (link.getCommercialApproveStatus().equalsIgnoreCase("1")) {
      				linkDto.setApproveStatus(true);
      			} else {
      				linkDto.setApproveStatus(false);
      			}
      		}
      		if(linkDto.getRejectionStatus() || linkDto.getApproveStatus()) {
      			linkDto.setIsActionTaken(true);
      		}
		linkDto.setMfStatus(link.getMfStatus());
		
		//added for bulk link commercial upload
		if (link.getLinkBulkUpdate() != null) {
			if (link.getLinkBulkUpdate().equalsIgnoreCase("1")) {
				linkDto.setBulkuploadStatus(true);
			} else {
				linkDto.setBulkuploadStatus(false);
			}
		}
		
		Optional<QuoteIllSite> siteAOpt = illSiteRepository.findById(link.getSiteAId());
		if(siteAOpt.isPresent()) {
			QuoteToLe quoteToLe = siteAOpt.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) 
				|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()))
		{
			
			linkDto
					.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe, link)));
		}
		
		
		// Termination specific data
		if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Getting termination related details for quoteToLe Id {}", quoteToLe.getId());
			List<QuoteSiteServiceTerminationDetailsBean> terminationSiteServiceBeanList = new ArrayList<>();
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(link.getId());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				quoteIllSiteToServiceList.stream().forEach(siteToService ->{
					LOGGER.info("siteToService serviceId {}", siteToService.getErfServiceInventoryTpsServiceId());
					QuoteSiteServiceTerminationDetails terminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);
					QuoteSiteServiceTerminationDetailsBean terminationSiteServiceBean = new QuoteSiteServiceTerminationDetailsBean();
					BeanUtils.copyProperties(terminationDetail, terminationSiteServiceBean);
					terminationSiteServiceBean.setQuoteIllSiteToServiceId(siteToService.getId());
					//terminationSiteServiceBean.setQuoteSiteId(siteToService.getQuoteIllSite().getId());
					terminationSiteServiceBean.setQuoteLinkId(siteToService.getQuoteNplLink().getId());
					terminationSiteServiceBean.setServiceId(siteToService.getErfServiceInventoryTpsServiceId());
					terminationSiteServiceBean.setServiceLinkType(siteToService.getType());
					LOGGER.info("terminationSiteServiceBean {}", terminationSiteServiceBean.toString());
					terminationSiteServiceBeanList.add(terminationSiteServiceBean);
				});
			}
			
			linkDto.setQuoteSiteServiceTerminationsBean(terminationSiteServiceBeanList);
			
		}
		}
		return linkDto;
	}

	/**
	 * getSortedSolution
	 * 
	 * @param solutionBeans
	 * @return
	 */
	private List<ProductSolutionBean> getSortedSolution(List<ProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(ProductSolutionBean::getProductSolutionId));
		}

		return solutionBeans;

	}

	/**
	 * getAccountManagersEmail
	 * 
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	public String getAccountManagersEmail(QuoteToLe quoteToLe) {
		return getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
	}

	/**
	 * getLeAttributes
	 * 
	 * @param quoteTole
	 * @param attr
	 * @return
	 * @throws TclCommonException
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
	 * @link http://www.tatacommunications.com/ constructNplSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<QuoteNplSiteBean> constructNplSiteBeans(Map<String, QuoteIllSite> nplSites, Boolean isFeasibleSites) {
		List<QuoteNplSiteBean> sites = new ArrayList<>();
		if (nplSites != null) {
			for (Map.Entry<String, QuoteIllSite> entry : nplSites.entrySet()) {
				QuoteIllSite nplSite = entry.getValue();
				String type = entry.getKey().split("_")[1];
				if (nplSite.getStatus() == 1) {
					QuoteNplSiteBean nplSiteBean = new QuoteNplSiteBean(nplSite);
					// nplSiteBean.setQuoteSla(constructSlaDetails(nplSite));
					// nplSiteBean.setFeasibility(constructSiteFeasibility(nplSite));
					nplSiteBean.setMfStatus(nplSite.getMfStatus());
					nplSiteBean.setType(type);
					// added for site level billing gst
					QuoteSiteBillingDetails billinginfo = quoteSiteBillingInfoRepository.findByQuoteIllSite(nplSite);
					if (billinginfo != null) {
						LOGGER.info("billing site gst no npl"+billinginfo.getGstNo()+"siteid:::"+nplSite.getId());
						nplSiteBean.setSiteBillingGstNo(billinginfo.getGstNo());
					}
					sites.add(nplSiteBean);
				}
			}
		}
		return sites;
	}

	/**
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent
	 * @param id,version
	 */
	private List<QuoteProductComponentBean> constructQuoteProductComponent(Integer id, boolean isSitePropertiesNeeded,
			String type, Boolean isSiteProp) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenOnVersion(id, isSitePropertiesNeeded, type,
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
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<QuoteProductComponentBean> getSortedComponents(List<QuoteProductComponentBean> quoteComponentBeans) {
		if (quoteComponentBeans != null) {
			quoteComponentBeans.sort(Comparator.comparingInt(QuoteProductComponentBean::getComponentId));

		}

		return quoteComponentBeans;
	}

	/**
	 * @param isSitePropertiesNeeded
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */

	private List<QuoteProductComponent> getComponentBasenOnVersion(Integer id, boolean isSitePropertiesNeeded,
			String type, Boolean isSiteProp) {
		List<QuoteProductComponent> components = null;
		MstProductFamily prodFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.NPL,
				CommonConstants.BACTIVE);
		if (isSitePropertiesNeeded) {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(id,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.NPL_LINK.toString());
		} else if (isSiteProp) {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductFamilyAndType(id, prodFamily,
					type);
			components.addAll(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(id,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.NPL_LINK.toString()));
		} else {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductFamilyAndType(id, prodFamily,
					type);
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
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<QuoteNplSiteBean> getSortedNplSiteBeans(List<QuoteNplSiteBean> nplSiteBeans) {
		if (nplSiteBeans != null) {
			nplSiteBeans.sort(Comparator.comparingInt(QuoteNplSiteBean::getSiteId));

		}

		return nplSiteBeans;
	}

	/**
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param links,version
	 * @return List<NplLinkBean>
	 */
	private List<NplLinkBean> getSortedNplLinks(List<NplLinkBean> links) {
		if (links != null) {
			links.sort(Comparator.comparingInt(NplLinkBean::getId));

		}

		return links;
	}

	/**
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
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteProductComponentsAttributeValue> getAttributeBasenOnVersion(Integer componentId,
			boolean isSitePropRequire, Boolean isSiteProp) {
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
						.filter(attr -> (!attr.getProductAttributeMaster().getName().equals(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties()))||(!attr.getProductAttributeMaster().getName().equals(CROSS_CONNECT_LOCAL_DEMARCATION_ID)))
						.collect(Collectors.toList());
			}

		}

		return attributes;

	}

	/**
	 * @link http://www.tatacommunications.com
	 * @param quoteProductComponentsAttributeValues
	 * @return
	 */
	private List<QuoteProductComponentsAttributeValueBean> constructAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues) {
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean = new ArrayList<>();
		if (quoteProductComponentsAttributeValues != null) {
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {

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
			}
		}

		return quoteProductComponentsAttributeValueBean;
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @constructAttributePriceDto
	 */
	private QuotePriceBean constructAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue) {
		QuotePriceBean priceDto = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			priceDto = new QuotePriceBean(attrPrice);
		}
		return priceDto;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<QuoteProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<QuoteProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(QuoteProductComponentsAttributeValueBean::getAttributeId));

		}

		return attributeBeans;
	}

	/**
	 * createQuoteLe processSolutionDetail- This method process the solution details
	 * ======= createQuoteLe ======= processSolutionDetail- This method process the
	 * solution details
	 * 
	 * @param user
	 * @param productFamily
	 * @param quoteToLeProductFamily
	 * @param productSolution
	 * @param productOfferingName
	 * @throws TclCommonException
	 */
	protected Integer processSiteDetail(User user, MstProductFamily productFamily,
			QuoteToLeProductFamily quoteToLeProductFamily, NplSite site, String productOfferingName,
			ProductSolution productSolution, Quote quote, boolean siteChanged) throws TclCommonException {
		Integer siteId = null;

		try {
			siteId = constructIllSites(productSolution, user, site, productFamily, productOfferingName, siteChanged);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siteId;
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
	 * 
	 * constructIllSites- This methods is used to construct the IllSites entity
	 * 
	 * @param productSolution
	 * @param userId
	 * @return void
	 * @throws TclCommonException
	 */
	private Integer constructIllSites(ProductSolution productSolution, User user, NplSite site,
			MstProductFamily productFamily, String productOfferingName, boolean siteChanged) throws TclCommonException {

		Integer siteId = null;
		SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);

		List<NplSiteDetail> siteInp = site.getSite();
		for (NplSiteDetail siteDetail : siteInp) {
			if (siteDetail.getSiteId() == null) {
				QuoteIllSite illSite = new QuoteIllSite();
				illSite.setErfLocSiteaLocationId(siteDetail.getSecondLocationId());
				illSite.setErfLocSiteaSiteCode(siteDetail.getSecondLocationCode());
				illSite.setErfLocSitebLocationId(siteDetail.getLocationId());
				illSite.setErfLocSitebSiteCode(siteDetail.getLocationCode());
				illSite.setProductSolution(productSolution);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 60); // Adding 60 days
				illSite.setEffectiveDate(cal.getTime());
				illSite.setCreatedBy(user.getId());
				illSite.setCreatedTime(new Date());
				illSite.setStatus((byte) 1);
				illSite.setImageUrl(soDetail.getImage());
				illSite.setSiteCode(Utils.generateUid());
				illSite.setFeasibility((byte) 0);
				int shiftSite = (siteChanged) ? 1 : 0;
				illSite.setNplShiftSiteFlag(shiftSite);
				illSite = illSiteRepository.save(illSite);
				// siteDetail.setSiteId(illSite.getId());
				siteId = illSite.getId();
				LOGGER.info("Npl shift site: " +illSite.getNplShiftSiteFlag());

//				}

			} else {
				QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(siteDetail.getSiteId(), (byte) 1);
				if (illSiteEntity != null) {
					illSiteEntity.setProductSolution(productSolution);
					illSiteRepository.save(illSiteEntity);
					siteId = illSiteEntity.getId();

				}
			}
		}

		return siteId;
	}

	/**
	 * processProductComponent- This method process the product component details
	 * 
	 * @param productFamily
	 * @param link
	 * @param component
	 * @param user
	 * @throws TclCommonException
	 */
	private void processProductComponent(MstProductFamily productFamily, QuoteNplLink link, ComponentDetail component,
			User user, Integer locA, Integer locB) throws TclCommonException {
		Integer id = null;
		try {
			MstProductComponent productComponent = getProductComponent(component, user);
			String refType = QuoteConstants.NPL_SITES.toString();
			if (component.getType() != null && component.getType().equalsIgnoreCase(CommonConstants.LINK)) {
				id = link.getId();
				refType = QuoteConstants.NPL_LINK.toString();
			}
			if (component.getType() != null && component.getType().equalsIgnoreCase(CommonConstants.SITEA))
				id = link.getSiteAId();
			if (component.getType() != null && component.getType().equalsIgnoreCase(CommonConstants.SITEB))
				id = link.getSiteBId();
			QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily, id,
					refType);
			quoteComponent.setType(component.getType());
			quoteProductComponentRepository.save(quoteComponent);
			LOGGER.info("saved successfully");
			for (AttributeDetail attribute : component.getAttributes()) {
				processProductAttribute(quoteComponent, attribute, user, locA, locB);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
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
	private MstProductComponent getProductComponent(ComponentDetail component, User user) throws TclCommonException {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(component.getName(), (byte) 1);
		if (mstProductComponents != null && mstProductComponents.size() > 0) {
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
	 * constructProductComponent- This method constructs the
	 * {@link QuoteProductComponent} Entity
	 * 
	 * @param productComponent
	 * @param mstProductFamily
	 * @param illSiteId
	 * @return QuoteProductComponent
	 * @throws TclCommonException
	 */
	private QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer illSiteId, String refType) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(refType);
		return quoteProductComponent;

	}

	/**
	 * processProductAttribute- This method process the product attributes
	 * 
	 * @param quoteComponent
	 * @param attribute
	 * @param user
	 * @throws TclCommonException
	 */
	private void processProductAttribute(QuoteProductComponent quoteComponent, AttributeDetail attribute, User user,
			Integer locA, Integer locB) throws TclCommonException {
		try {
			ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
			QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
					productAttribute, attribute, locA, locB);
			quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.INVALID_ATTRIBUTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
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
	private ProductAttributeMaster getProductAttributes(AttributeDetail attributeDetail, User user)
			throws TclCommonException {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && productAttributeMasters.size() > 0) {
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

	/*
	 * constructProductAttribute- This method constructs the {@link
	 * QuoteProductComponentsAttributeValue} Entity
	 * 
	 * @param quoteProductComponent
	 * 
	 * @param productAttributeMaster
	 * 
	 * @param attributeDetail
	 * 
	 * @return QuoteProductComponentsAttributeValue
	 */
	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, AttributeDetail attributeDetail, Integer locA, Integer locB)
			throws TclCommonException {
		String attrValue = attributeDetail.getValue();

		if (locA != null && productAttributeMaster.getName().equals(CommonConstants.SITE_A_ADDRESS)) {
			attrValue = constructAddressAsString(getUserAddress(locA));

		}
		if (locB != null && productAttributeMaster.getName().equals(CommonConstants.SITE_B_ADDRESS)) {
			attrValue = constructAddressAsString(getUserAddress(locB));
		}

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attrValue);
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;

	}

	/**
	 * constructAddressAsString
	 * 
	 * @param addressDetail
	 * @return
	 */
	private String constructAddressAsString(AddressDetail addressDetail) {
		StringBuilder address = new StringBuilder();

		if (addressDetail.getPlotBuilding() != null)
			address.append(addressDetail.getPlotBuilding()).append(CommonConstants.COMMA);
		if (addressDetail.getAddressLineOne() != null)
			address.append(addressDetail.getAddressLineOne()).append(CommonConstants.COMMA);
		if (addressDetail.getAddressLineTwo() != null)
			address.append(addressDetail.getAddressLineTwo()).append(CommonConstants.COMMA);
		if (addressDetail.getLocality() != null)
			address.append(addressDetail.getLocality()).append(CommonConstants.COMMA);
		if (addressDetail.getCity() != null)
			address.append(addressDetail.getCity()).append(CommonConstants.HYPHEN);
		if (addressDetail.getPincode() != null)
			address.append(addressDetail.getPincode());

		return address.toString();
	}

	/**
	 * @author Dinahar Vivekanandan getUserAddress
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	public AddressDetail getUserAddress(Integer locationId) throws TclCommonException {
		AddressDetail userAddress = null;
		try {
			String response = (String) mqUtils.sendAndReceive(locationDetailQueue, locationId.toString());
			LOGGER.info("Output Payload for location details", response);
			if (StringUtils.isNotBlank(response)) {
				userAddress = (AddressDetail) Utils.convertJsonToObject(response, AddressDetail.class);
			}
		} catch (Exception e) {
			throw new TclCommonException(e);
		}
		return userAddress;
	}

	/**
	 * 
	 * createQuote-This methods create the quotes
	 *
	 * 
	 * @param quoteDetail
	 * 
	 * @param erfCustomerId
	 * @return QuoteResponse
	 * @throws TclCommonException
	 */
	public QuoteResponse createQuote(NplQuoteDetail quoteDetail, Integer erfCustomerId) throws TclCommonException {
		QuoteResponse response = new QuoteResponse();
		try {
			if ((quoteDetail == null)) {
				throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			}
			User user = getUserId(Utils.getSource());
			QuoteToLe quoteTole = processQuote(quoteDetail, erfCustomerId, user);
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());

				//For partner
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
	 * constructLegaAttribute
	 * 
	 * @param mstOmsAttribute
	 * @param quoteTole
	 * @param name
	 * @param value
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
	 * 
	 * processQuote-This methods create/upadte the
	 * quotes,productFamily,productSolutions and sites
	 *
	 * 
	 * @param quoteDetail
	 * 
	 * @param erfCustomerId
	 * 
	 * @param user
	 * @return QuoteToLe
	 * @throws TclCommonException
	 */
	protected QuoteToLe processQuote(NplQuoteDetail quoteDetail, Integer erfCustomerId, User user)
			throws TclCommonException {
		Customer customer = null;
		if (erfCustomerId != null) {
			customer = customerRepository.findByErfCusCustomerIdAndStatus(erfCustomerId, (byte) 1);// get the customer
																									// Id
		} else {
			customer = user.getCustomer();
		}
		Quote quote = null;
		// Checking whether the input is for creating or updating
		if (quoteDetail.getQuoteleId() == null && quoteDetail.getQuoteId() == null) {
			Quote quoteExisting=quoteRepository.findByQuoteCode(quoteDetail.getQuoteCode());
			quote = new Quote();
			if(quoteExisting!=null){
				quote=quoteExisting;
			}
			quote.setCustomer(customer);
			quote.setCreatedBy(user.getId());
			quote.setCreatedTime(new Date());
			quote.setStatus((byte) 1);
			//For Partner
			quote.setQuoteCode(quoteDetail.getEngagementOptyId()!=null?
					quoteDetail.getQuoteCode() : Utils.generateRefId(quoteDetail.getProductName().toUpperCase()));
			quote.setEngagementOptyId(quoteDetail.getEngagementOptyId());
			quote.setNsQuote("N");
			quoteRepository.save(quote);
		} else {
			quote = quoteRepository.findByIdAndStatus(quoteDetail.getQuoteId(), CommonConstants.BACTIVE);
		}
		QuoteToLe quoteToLe = null;
		if (quoteDetail.getQuoteId() == null) {
			quoteToLe = constructQuoteToLe(quote,quoteDetail);
			quoteToLeRepository.save(quoteToLe);
		} else {
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			quoteToLe = quoteToLeEntity.isPresent() ? quoteToLeEntity.get() : null;
			// For Partner
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
			quoteToLeProductFamily = new QuoteToLeProductFamily();
			quoteToLeProductFamily.setMstProductFamily(productFamily);
			quoteToLeProductFamily.setQuoteToLe(quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		} else {
			removeUnselectedSolution(quoteDetail, quoteToLeProductFamily, quoteToLe);
		}

		//For Partner
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			quoteToLe.setQuoteToLeProductFamilies(
					Arrays.asList(quoteToLeProductFamily).stream().collect(Collectors.toSet()));
		}

		for (SolutionDetail solution : quoteDetail.getSolutions()) {
			String productOffering = solution.getOfferingName();
			MstProductOffering productOfferng = getProductOffering(productFamily, productOffering, user);
			ProductSolution productSolution = productSolutionRepository
					.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
			if (productSolution == null) {
				productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
						Utils.convertObjectToJson(solution));
				productSolution.setSolutionCode(Utils.generateUid());
				productSolutionRepository.save(productSolution);
				// ADDED for nde ehs id passing issue
				if (quoteToLe.getTpsSfdcOptyId() != null && !quoteToLe.getQuote().getQuoteCode().startsWith("NDE"))
					omsSfdcService.processProductServiceForSolution(quoteToLe, productSolution,
							quoteToLe.getTpsSfdcOptyId());// adding productService
				// to Sfdc
			} else {
				productSolution.setProductProfileData(Utils.convertObjectToJson(solution));

				productSolutionRepository.save(productSolution);
				List<QuoteNplLink> links = nplLinkRepository.findByProductSolutionIdAndStatus(productSolution.getId(),
						CommonConstants.BACTIVE);

				if (links != null && !links.isEmpty()) {
					for (QuoteNplLink link : links) {
						saveProductSolutionForNplSites(productSolution, link.getSiteAId());
						saveProductSolutionForNplSites(productSolution, link.getSiteBId());

						removeCompAndAttrForLink(link);

						for (ComponentDetail componentDetail : solution.getComponents()) {
							processProductComponent(productFamily, link, componentDetail, user, null, null);
						}

					}
				}

			}
		}
		return quoteToLe;

	}

	/**
	 * removeCompAndAttrForLink
	 * 
	 * @param link
	 */
	private void removeCompAndAttrForLink(QuoteNplLink link) {
		removeComponentsAndAttr(link.getId(), CommonConstants.LINK);
		removeComponentsAndAttr(link.getSiteAId(), CommonConstants.SITEA);
		removeComponentsAndAttr(link.getSiteBId(), CommonConstants.SITEB);

	}

	/**
	 * saveProductSolutionForNplSites
	 * 
	 * @param productSolution
	 * @param siteId
	 */
	private void saveProductSolutionForNplSites(ProductSolution productSolution, Integer siteId) {
		Optional<QuoteIllSite> optSite = illSiteRepository.findById(siteId);
		if (optSite.isPresent()) {
			QuoteIllSite site = optSite.get();
			site.setProductSolution(productSolution);
			illSiteRepository.save(site);
		}

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
	 * removeComponentsAndAttr
	 * 
	 * @param id
	 * @param compType
	 */
	private void removeComponentsAndAttr(Integer id, String compType) {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(id, compType);
		if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
			quoteProductComponents.forEach(quoteProd -> {
				if (quoteProd.getQuoteProductComponentsAttributeValues() != null) {
					quoteProd.getQuoteProductComponentsAttributeValues()
							.forEach(attr -> quoteProductComponentsAttributeValueRepository.delete(attr));
					quoteProductComponentRepository.delete(quoteProd);
				}
			});
		}
	}
	
	

	/**
	 * @author  construct Npl links mc
	 * @param siteAId
	 * @param siteBId
	 * @param productSolution
	 * @throws TclCommonException
	 */
	protected void constructNplLinks(User user, MstProductFamily productFamily, Integer quoteId, Integer siteAId,
			Integer siteBId, ProductSolution productSolution, final String siteAType, final String siteBType,
			String productOfferingName, Integer locA, Integer locB, String serviceId,String isbandwith,String isshiftsite) throws TclCommonException {
		QuoteNplLink link = null;
		Optional<QuoteNplLink> optLink = nplLinkRepository.findByProductSolutionIdAndSiteAIdAndSiteBIdAndStatus(
				productSolution.getId(), siteAId, siteBId, (byte) 1);
		SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);

		if (optLink.isPresent()) {
			link = optLink.get();
			removeCompAndAttrForLink(link);
		}

		else {
			link = new QuoteNplLink();
			link.setSiteAId(siteAId);
			link.setSiteBId(siteBId);
			link.setProductSolutionId(productSolution.getId());
			link.setCreatedDate(new Date());
			link.setStatus((byte) 1);
			link.setQuoteId(quoteId);
			link.setCreatedBy(user.getId());
			link.setWorkflowStatus(null);
			link.setLinkCode(Utils.generateUid());
			link.setSiteAType(siteAType);
			link.setSiteBType(siteBType);
			link.setIsTaskTriggered(0);
			// Implemented as in ILL
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 60); // Adding 60 days
			link.setEffectiveDate(cal.getTime());
			link = nplLinkRepository.save(link);
			LOGGER.info("Link Id: "+ link.getId());
			link.setQuoteNplLinkSlas(constuctNplLinkSla(link, productOfferingName));
		}

		LOGGER.info("Service Id before storing link id in QuoteIllSiteService: "+serviceId);
		if (StringUtils.isNotEmpty(serviceId) && StringUtils.isNotBlank(serviceId))
		{
			LOGGER.info("Service Id after storing link id in QuoteIllSiteService: "+serviceId);
			LOGGER.info("Shift site and change bandwidth flag"+"CB:"+isbandwith+"SS:"+isshiftsite);
			QuoteToLe quoteToLe = productSolution.getQuoteToLeProductFamily().getQuoteToLe();
			if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) || MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {

				List<QuoteIllSiteToService> quoteSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);

				for(QuoteIllSiteToService quoteIllSiteToService:quoteSiteToService) {
				
				if (quoteSiteToService != null && !quoteSiteToService.isEmpty() && quoteIllSiteToService.getQuoteNplLink() == null) {
					quoteIllSiteToService.setQuoteNplLink(link);
					//ADDED for nde mc
					if(isbandwith!=null) {
						quoteIllSiteToService.setBandwidthChanged((isbandwith != null&& isbandwith.equalsIgnoreCase("true"))? CommonConstants.BACTIVE: CommonConstants.BDEACTIVATE);
					}
					if(isshiftsite!=null) {
						quoteIllSiteToService.setSiteShifted((isshiftsite != null&& isshiftsite.equalsIgnoreCase("true"))? CommonConstants.BACTIVE: CommonConstants.BDEACTIVATE);
					}
					quoteIllSiteToServiceRepository.save(quoteIllSiteToService);
					LOGGER.info("Saved NPL link to QuoteIllSiteToService"+quoteIllSiteToService.getId());

				}
			}
			}
		}

		for (ComponentDetail componentDetail : soDetail.getComponents()) {
			processProductComponent(productFamily, link, componentDetail, user, locA, locB);
		}
	}

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to update components and attributes of sites
	 * @throws TclCommonException
	 */

	public NplQuoteDetail updateSiteProperties(NplUpdateRequest nplRequest, String type) throws TclCommonException {
		NplQuoteDetail quote = new NplQuoteDetail();
		try {
			validateNplRequest(nplRequest);
			for (UpdateRequest request : nplRequest.getUpdateRequest()) {
				updateSiteComponentsAndAttributes(request, type);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quote;
	}

	/**
	 * Method to update component and attributes for site
	 * 
	 * @param data UpdateRequest
	 * @param type
	 * @return Returns the UpdatedData
	 * @throws TclCommonException
	 */
	public UpdateRequest updateSiteComponentsAndAttributes(UpdateRequest data, String type) throws TclCommonException {

		try {
			validateUpdateRequest(data);
			User user;
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(data.getSiteId(), (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);
			}
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(data.getFamilyName(),
					(byte) 1);

			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

			}
            if(data.getUserName()==null) {
           	 user = getUserId(Utils.getSource());
            }else {
           	 user = getUserId(data.getUserName());
            }
			saveIllsiteProperties(quoteIllSite, data, user, mstProductFamily, type);
			
			//added for multisitebilling info gst updation
			LOGGER.info("quote to le id ::::"+data.getQuoteToLe());
			if (data.getAttributeDetails() != null && data.getQuoteToLe() != null) {
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(data.getQuoteToLe());
				if (quoteToLe.isPresent()) {
					if (quoteToLe.get().getSiteLevelBilling() != null) {
						if (quoteToLe.get().getSiteLevelBilling().equalsIgnoreCase("1")) {
							for (AttributeDetail attributeDetail : data.getAttributeDetails()) {
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
		return data;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ saveIllsiteProperties used to
	 *       saveillsite properties
	 * @param localITContactId
	 * @param quoteIllSite
	 * @param mstProductFamily
	 * @param type
	 */
	private void saveIllsiteProperties(QuoteIllSite quoteIllSite, UpdateRequest request, User user, MstProductFamily mstProductFamily, String type) {
		MstProductComponent mstProductComponent = getMstProperties(user);
		constructIllSitePropeties(mstProductComponent, quoteIllSite, user.getUsername(), request, mstProductFamily, type);

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
		quoteProductComponent.setReferenceName(QuoteConstants.NPL_SITES.toString());
		quoteProductComponent.setMstProductFamily(
				quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getMstProductFamily());
		quoteProductComponentRepository.save(quoteProductComponent);
		ProductAttributeMaster attributeMaster = getAttributePropertiesMaster(username);
		quoteProductComponent.setQuoteProductComponentsAttributeValues(
				constructIllSiteAtrribute(quoteProductComponent, localITContactId, attributeMaster));
	}

    /**
     * construct sites
     *
     * @param mstProductComponent
     * @param orderIllSite
     * @param username
     * @param request
     * @param mstProductFamily
     */
    private void constructIllSitePropeties(MstProductComponent mstProductComponent, QuoteIllSite orderIllSite,
                                           String username, UpdateRequest request, MstProductFamily mstProductFamily, String type) {
        List<QuoteProductComponent> orderProductComponents = quoteProductComponentRepository
                .findByReferenceIdAndMstProductComponent(orderIllSite.getId(), mstProductComponent);
        if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
            updateIllSiteProperties(orderProductComponents, request, username);
        } else {
            createIllSiteAttribute(mstProductComponent, mstProductFamily, orderIllSite, request, username, type);
        }

    }

    /**
     * create site attributes
     *
     * @param mstProductComponent
     * @param mstProductFamily
     * @param orderIllSite
     * @param request
     * @param username
     */
    private void createIllSiteAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
                                        QuoteIllSite orderIllSite, UpdateRequest request, String username, String type) {
        QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
        quoteProductComponent.setMstProductComponent(mstProductComponent);
        quoteProductComponent.setReferenceId(orderIllSite.getId());
        quoteProductComponent.setReferenceName(QuoteConstants.NPL_LINK.toString());
        quoteProductComponent.setMstProductFamily(mstProductFamily);
        if(Objects.nonNull(type)){
			quoteProductComponent.setType(type);
		}
        quoteProductComponentRepository.save(quoteProductComponent);

        if (request.getAttributeDetails() != null) {
            for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
                createSitePropertiesAttribute(quoteProductComponent, attributeDetail, username);

            }

        }
    }

    /**
     * update site properties
     *
     * @param orderProductComponents
     * @param request
     * @param username
     */
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

    /**
     * update site properties attributes
     *
     * @param productAttributeMasters
     * @param attributeDetail
     * @param orderProductComponent
     */
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

    /**
     * create attributes
     *
     * @param attributeMaster
     * @param orderProductComponent
     * @param attributeDetail
     * @return
     */
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

    /**
     * create site properties attribute
     *
     * @param orderProductComponent
     * @param attributeDetail
     * @param username
     */
    private void createSitePropertiesAttribute(QuoteProductComponent orderProductComponent,
                                               AttributeDetail attributeDetail, String username) {

        ProductAttributeMaster attributeMaster = getPropertiesMaster(username, attributeDetail);
        orderProductComponent.setQuoteProductComponentsAttributeValues(
                createAttributes(attributeMaster, orderProductComponent, attributeDetail));

    }

    /**
     * get properties of master
     *
     * @param name
     * @param attributeDetail
     * @return
     */
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
	protected MstProductComponent getMstProperties(User user) {

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
	 * validateRequest
	 * 
	 * @param siteId
	 */
	private void validateNplRequest(NplUpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		}
	}

	private void removeUnselectedSolution(NplQuoteDetail quoteDetail, QuoteToLeProductFamily quoteToLeProductFamily,
			QuoteToLe quoteToLe) {
		List<QuoteNplLink> links = null;
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
				links = nplLinkRepository.findByProductSolutionId(exproductSolution.getId());
				if (links != null && !links.isEmpty())
					links.forEach(link -> deActivateLinkAndSites(link, QuoteConstants.DELETE.toString()));

				// Trigger delete productSolution
				if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId()))
					omsSfdcService.processDeleteProduct(quoteToLe, exproductSolution);
				productSolutionRepository.delete(exproductSolution);
			}

		}
	}

	/**
	 * getLinkInfo
	 * 
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public QuoteResponse getLinkInfo(Integer linkId) throws TclCommonException {
		QuoteResponse quoteResponse = null;
		Integer siteAId = null;
		Integer siteBId = null;
		try {
			quoteResponse = new QuoteResponse();
			List<NplLinkBean> nplLinks = new ArrayList<>();
			List<QuoteNplSiteDto> nplSiteDtos = new ArrayList<>();
			QuoteNplLink quoteNplLink = nplLinkRepository.findByIdAndStatus(linkId, (byte) 1);

			if (quoteNplLink == null) {
				throw new TclCommonException(ExceptionConstants.NPL_LINK_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			siteAId = quoteNplLink.getSiteAId();
			siteBId = quoteNplLink.getSiteBId();

			QuoteIllSite nplSiteA = illSiteRepository.findByIdAndStatus(siteAId, (byte) 1);
			QuoteIllSite nplSiteB = illSiteRepository.findByIdAndStatus(siteBId, (byte) 1);

			if (nplSiteA == null || nplSiteB == null) {
				throw new TclCommonException(ExceptionConstants.NPL_SITE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			nplSiteDtos.add(new QuoteNplSiteDto(nplSiteA));
			nplSiteDtos.add(new QuoteNplSiteDto(nplSiteB));

			constructProductComponents(nplSiteDtos);
			NplLinkBean nplLinkBean = constructNplLinkBean(quoteNplLink, null, false);
			nplLinkBean.setSitesDtos(nplSiteDtos);
			nplLinks.add(nplLinkBean);
			quoteResponse.setNplLinks(nplLinks);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteResponse;
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
		LOGGER.info("inside createDocument method");
		CreateDocumentDto response = new CreateDocumentDto();
		Integer oldCustomerLegalEntityId = null;
		try {
			validateDocumentRequest(documentDto);
			//pipf-212
			persistLeOwnerDetailsSfdc(documentDto);

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
			customerLeAttributeRequestBean.setProductName("NPL");
			String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
					Utils.convertObjectToJson(customerLeAttributeRequestBean));
			LOGGER.info("customer le attr {}", customerLeAttributes );
			if (StringUtils.isNotEmpty(customerLeAttributes)) {
				updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
						CustomerLeDetailsBean.class), optionalQuoteLe.get());
			}
			String spName = returnServiceProviderName(documentDto.getSupplierLegalEntityId());
			if (StringUtils.isNotEmpty(spName)) {
				processAccount(optionalQuoteLe.get(), spName, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
			}
			QuoteToLe quoteToLe = optionalQuoteLe.get();
			oldCustomerLegalEntityId=quoteToLe.getErfCusCustomerLegalEntityId();
			quoteToLe.setErfCusCustomerLegalEntityId(documentDto.getCustomerLegalEntityId());
			quoteToLe.setErfCusSpLegalEntityId(documentDto.getSupplierLegalEntityId());
			if (quoteToLe.getStage().equals(QuoteStageConstants.CHECKOUT.getConstantCode())) {
				quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			}
			quoteToLeRepository.save(quoteToLe);

			CustomerDetail customerDetail =null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				LOGGER.info("Inside partner loop for customer details");
				customerDetail = new CustomerDetail();
				Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(documentDto.getCustomerId(),
						(byte) 1);
				customerDetail.setCustomerId((customer!=null ?customer.getId():null));
			} else {
				LOGGER.info("Inside loop for customer details");
				customerDetail = userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
			}
			LOGGER.info("customer details {}" , customerDetail.toString());
			if (customerDetail != null && !customerDetail.getCustomerId().equals(quote.getCustomer().getId())) {
				Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
				LOGGER.info("customer entity {}", customerEntity);
				if (customerEntity.isPresent()) {
					quote.setCustomer(customerEntity.get());
					quoteRepository.save(quote);
				}
			}
			processLocationDetailsAndSendToQueue(quoteToLe, quote.getCustomer().getErfCusCustomerId());
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
			omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
					SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe);
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
				LOGGER.info("Since it failed we will retry for {}",count+1);
				response=createDocument(documentDto,count+1);	
			}else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}

		return response;
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
		LOGGER.info("Inside method processLocationDetailsAndSendToQueue");
		try {
			CustomerLeLocationBean bean = constructCustomerLeAndLocation(quoteToLe, erfCustomerId);
			LOGGER.info("MDC Filter token value in before Queue call processLocationDetailsAndSendToQueue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(customerLeLocationQueue, Utils.convertObjectToJson(bean));
		} catch (Exception e) {
			LOGGER.info("error in processing to queue call for persist location{}", e);
		}

	}

	/**
	 * returnServiceProviderName
	 * 
	 * @param id
	 * @return
	 * @throws TclCommonException
	 */
	public String returnServiceProviderName(Integer id) throws TclCommonException {
		try {
			return (String) mqUtils.sendAndReceive(spQueue,
					Utils.convertObjectToJson(constructSupplierDetailsRequestBean(id)));

		} catch (Exception e) {
			throw new TclCommonException("No Service Provider Name");
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
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateBillingInfoForSfdc this
	 *            method
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	public void updateBillingInfoForSfdc(CustomerLeDetailsBean request, QuoteToLe quoteToLe) throws TclCommonException {
		try {
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
	private void construcBillingSfdcAttribute(QuoteToLe quoteToLe, CustomerLeDetailsBean request)
			throws TclCommonException {
		if (request.getAttributes() != null) {
			request.getAttributes().forEach(billAttr -> {
				constructBillingAttribute(billAttr, quoteToLe);

			});
		}
		processAccount(quoteToLe, request.getAccounCuId(), LeAttributesConstants.ACCOUNT_CUID);
		processAccount(quoteToLe, request.getAccountId(), LeAttributesConstants.ACCOUNT_NO18);
		// processAccount(quoteToLe, String.valueOf(request.getBillingContactId()),
		// LeAttributesConstants.BILLING_CONTACT_ID);
		processAccount(quoteToLe, request.getLegalEntityName(), LeAttributesConstants.LEGAL_ENTITY_NAME);
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
			updateAttributes(attribute, quoteLeAttributeValues);

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
		QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
		if (quoteToLe.getQuoteType().equalsIgnoreCase(MACD) && attribute.getAttributeName().equalsIgnoreCase(BILLING_FREQUENCY))
		{
			List<SIServiceDetailDataBean> serviceDetail = null;

			//to save billing frequency value from inventory for MACD quotes
			if (Objects.nonNull(quoteToLe)) {
				if (quoteToLe.getQuoteType().equalsIgnoreCase(MACD)) {
					List<String> serviceIds = macdUtils.getServiceIds(quoteToLe);
					String serviceId = "";
					if (Objects.nonNull(serviceIds) && !serviceIds.isEmpty()) {
						serviceId = serviceIds.stream().findFirst().get();
					}
					try {
						serviceDetail = macdUtils.getServiceDetailNPL(serviceId);
					} catch (TclCommonException e) {
						LOGGER.info("Error in retrieving Service Info to save Billing Frequency");
					}
				}
			}
			if (Objects.nonNull(serviceDetail) && StringUtils.isNotBlank(serviceDetail.get(0).getBillingFrequency())) {
				LOGGER.info("Billing Frequency value {} to be in inserted in quoteLeAttrValues ", serviceDetail.get(0).getBillingFrequency());
				attributeValue.setAttributeValue(serviceDetail.get(0).getBillingFrequency());
				attributeValue.setDisplayValue(attribute.getAttributeName());
				attributeValue.setQuoteToLe(quoteToLe);
				MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
				attributeValue.setMstOmsAttribute(mstOmsAttribute);
				quoteLeAttributeValueRepository.save(attributeValue);
			}
		}
		else {
			attributeValue.setAttributeValue(attribute.getAttributeValue());
			attributeValue.setDisplayValue(attribute.getAttributeName());
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			LOGGER.info("Attribute Name {} and Value {} to be in inserted in quoteLeAttrValues ", attribute.getAttributeName(), attribute.getAttributeValue());
			quoteLeAttributeValueRepository.save(attributeValue);
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
	private void updateAttributes(Attributes attribute, List<QuoteLeAttributeValue> quoteLeAttributeValues) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeAttributeValues.get(0).getQuoteToLe().getId());

		quoteLeAttributeValues.forEach(attr -> {
			if (!attr.getMstOmsAttribute().getName().equalsIgnoreCase("Payment Currency")
					&& !attr.getMstOmsAttribute().getName().equalsIgnoreCase("Billing Currency")&&!attr.getMstOmsAttribute().getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY)) {
				if (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACD) && attr.getDisplayValue().equalsIgnoreCase(BILLING_FREQUENCY))
				{
					List<SIServiceDetailDataBean> serviceDetail = null;

					//to save billing frequency value from inventory for MACD quotes
					if (Objects.nonNull(quoteToLe)) {
						if (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACD)) {
							List<String> serviceIds = macdUtils.getServiceIds(quoteToLe.get());
							String serviceId = "";
							if (Objects.nonNull(serviceIds) && !serviceIds.isEmpty()) {
								serviceId = serviceIds.stream().findFirst().get();
							}
							try {
								serviceDetail = macdUtils.getServiceDetailNPL(serviceId);
							} catch (TclCommonException e) {
								LOGGER.info("Error in retrieving Service Info to update Billing Frequency");
							}
						}
					}
					if (Objects.nonNull(serviceDetail) && StringUtils.isNotBlank(serviceDetail.get(0).getBillingFrequency())) {
						LOGGER.info("Billing Frequency value {} to be in updated in quoteLeAttrValues ", serviceDetail.get(0).getBillingFrequency());
						attr.setAttributeValue(serviceDetail.get(0).getBillingFrequency());
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
		});
	}

	/**
	 * processAccountCuid used to process account details from customer mdm
	 * 
	 * @param quoteToLe
	 * @param request
	 * @param user
	 */
	protected void processAccount(QuoteToLe quoteToLe, String attrValue, String attributeName) {
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
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateLegalEntityProperties used to
	 *       update legal enitity properties
	 * @param request
	 * @return
	 */
	public NplQuoteDetail updateLegalEntityProperties(UpdateRequest request) throws TclCommonException {
		NplQuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(request);
			quoteDetail = new NplQuoteDetail();
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());

			if (optionalQuoteToLe.isPresent()) {
				if (Objects.isNull(optionalQuoteToLe.get().getQuoteType())
						|| (!MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType()))) {
				if (optionalQuoteToLe.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
					optionalQuoteToLe.get().setStage(QuoteStageConstants.CHECKOUT.getConstantCode());
					quoteToLeRepository.save(optionalQuoteToLe.get());
				}
				}
			} else {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			MstOmsAttribute omsAttribute = getMstAttributeMaster(request, user);
			constructQuoteLeAttribute(request, omsAttribute, optionalQuoteToLe.get());

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
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
	 * processTriggerMail
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
			Optional<QuoteDelegation> quoteDelExists = quoteDelegationRepository.findByAssignToAndStatus(user.getId(),
					UserStatusConstants.OPEN.toString());
			if (!quoteDelExists.isPresent()) {
				QuoteDelegation quoteDelegation = new QuoteDelegation();
				quoteDelegation.setAssignTo(user.getId());
				quoteDelegation.setInitiatedBy(user.getCustomer().getId());
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
				if (quoteToLe.isPresent()) {
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
			}
			String notificationBody = constructMailNotificationObject(triggerEmailRequest.getEmailId(),
					QuoteDelegationConstants.DELEGATION_MAIL_SUBJECT, user.getFirstName(), loginUrl,
					delegationTemplateId);
			LOGGER.info("MDC Filter token value in before Queue call processTriggerMail {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, notificationBody);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return triggerEmailResponse;
	}

	/**
	 * validateTriggerInput
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
	 * 
	 * @param toAddress
	 * @param subject
	 * @param message
	 * @param name
	 * @param reseturl
	 * @param templateId
	 * @return String
	 * @throws TclCommonException
	 */
	private String constructMailNotificationObject(String toAddress, String subject, String name, String resetUrl,
			String templateId) throws TclCommonException {
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("reseturl", resetUrl);
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setSubject(subject);
		mailNotificationRequest.setTemplateId(templateId);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(toAddress);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setVariable(map);
		return Utils.convertObjectToJson(mailNotificationRequest);
	}

	/**
	 *
	 * editSiteComponent used to edit site component values
	 * 
	 * @param request
	 * @return
	 */

	public NplQuoteDetail editLinkComponent(UpdateRequest request, Integer quoteLeId, Integer linkId)
			throws TclCommonException {
		NplQuoteDetail quoteDetail = null;
		try {
			quoteDetail = new NplQuoteDetail();
			if (request.getComponentDetails() == null || request.getComponentDetails().isEmpty()
					|| Objects.isNull(quoteLeId) || Objects.isNull(linkId)) {
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

			}

			for (ComponentDetail cmpDetail : request.getComponentDetails()) {
				for (AttributeDetail attributeDetail : cmpDetail.getAttributes()) {
					LOGGER.info("attribute name in editLinkComponent Method {}", attributeDetail.getName());
					
					Optional<QuoteProductComponentsAttributeValue> attributeValue =null;
					if (attributeDetail.getAttributeId() != null) {
						attributeValue = quoteProductComponentsAttributeValueRepository
								.findById(attributeDetail.getAttributeId());
					}
					if (attributeValue!=null && attributeValue.isPresent()) {
						if (!attributeValue.get().getAttributeValues().equalsIgnoreCase(attributeDetail.getValue())) {
							if (attributeDetail.getName()
									.equalsIgnoreCase(SLAConstants.SERVICE_AVAILABILITY.toString())) {

								List<QuoteNplLinkSla> nplLinksSlaList = nplLinkSlaRepository
										.findByQuoteNplLink_Id(linkId);
								if (nplLinksSlaList != null) {
									for (QuoteNplLinkSla nplLinkSla : nplLinksSlaList) {
										nplLinkSla.setSlaValue(attributeDetail.getValue());
										nplLinkSlaRepository.save(nplLinkSla);
									}
								}

								/*
								 * updateProductSolutionWithOffering(quoteLeId,CommonConstants.NPL,
								 * attributeValue.get().getAttributeValues(),attributeDetail.getValue());
								 */
							}
							attributeValue.get().setAttributeValues(attributeDetail.getValue());
							quoteProductComponentsAttributeValueRepository.save(attributeValue.get());

						}
					}
					
					
					if(attributeDetail.getName().contains("Local Loop Bandwidth")) {
						List<QuoteNplSiteDto> nplSiteDtos = new ArrayList<>();

						Optional<QuoteNplLink> quoteNplLinkOpt = nplLinkRepository.findById(linkId);
						if(quoteNplLinkOpt.isPresent()) {
							QuoteNplLink quoteNplLink = quoteNplLinkOpt.get();
							
							Integer siteAId = quoteNplLink.getSiteAId();
							Integer siteBId = quoteNplLink.getSiteBId();

							QuoteIllSite nplSiteA = illSiteRepository.findByIdAndStatus(siteAId, (byte) 1);
							QuoteIllSite nplSiteB = illSiteRepository.findByIdAndStatus(siteBId, (byte) 1);

							if (nplSiteA == null || nplSiteB == null) {
								throw new TclCommonException(ExceptionConstants.NPL_SITE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
							}

							nplSiteDtos.add(new QuoteNplSiteDto(nplSiteA));
							nplSiteDtos.add(new QuoteNplSiteDto(nplSiteB));
							
							nplSiteDtos.forEach( nplSite -> {
								
								List<QuoteProductComponent> productComponentsForNplSites = quoteProductComponentRepository
										.findByReferenceIdAndReferenceName(nplSite.getId(), QuoteConstants.NPL_SITES.toString());
								
								productComponentsForNplSites.forEach(quoteProductComponentBean -> {

									List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
											.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
													quoteProductComponentBean.getId(), "Local Loop Bandwidth");

									attributes.forEach(y -> {

										if (y.getProductAttributeMaster().getName().equals("Local Loop Bandwidth")
												&& cmpDetail.getType().equals(NplPDFConstants.SITE_A)
												&& !attributeDetail.getValue().equals(y.getAttributeValues())
												&& quoteProductComponentBean.getType().equals(NplPDFConstants.SITE_A)) {
											y.setAttributeValues(attributeDetail.getValue());

										}
										if (y.getProductAttributeMaster().getName().equals("Local Loop Bandwidth")
												&& cmpDetail.getType().equals(NplPDFConstants.SITE_B)
												&& !attributeDetail.getValue().equals(y.getAttributeValues())
												&& quoteProductComponentBean.getType().equals(NplPDFConstants.SITE_B)) {
											y.setAttributeValues(attributeDetail.getValue());

										}
										quoteProductComponentsAttributeValueRepository.save(y);
									});

								});
								
							});
							
							
						}
						
					}
				}
				
				
				
				
			}
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLe.get().getQuote().getQuoteCode().startsWith("NDE")) {
				LOGGER.info("Entered into NDE");
				QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_Id(quoteLeId);
				List<ProductSolution> productSolution = productSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProductFamily);
				if (!productSolution.isEmpty()) {
					ProductSolution solution = productSolution.get(0);
					LOGGER.info("solution data" + solution.getProductProfileData());
					SolutionDetail solutionBean = (SolutionDetail) Utils
							.convertJsonToObject(solution.getProductProfileData(), SolutionDetail.class);
					for (ComponentDetail cmpDetail : solutionBean.getComponents()) {
						for (ComponentDetail reqCompdetail : request.getComponentDetails()) {
							if (cmpDetail.getName().equalsIgnoreCase(reqCompdetail.getName())) {
								for (AttributeDetail attributeDetail : cmpDetail.getAttributes()) {
									for (AttributeDetail requestAttributeDetail : reqCompdetail.getAttributes()) {
										if (attributeDetail.getName()
												.equalsIgnoreCase(requestAttributeDetail.getName())) {
											LOGGER.info("ATTRIBUTE NAME" + attributeDetail.getName()+"REQ ATTRIBUTENAME"+requestAttributeDetail.getName());

											if (attributeDetail.getName().equalsIgnoreCase("Hub Parented")) {
												attributeDetail.setValue(requestAttributeDetail.getValue());

											}
											/*
											 * if (attributeDetail.getName().equalsIgnoreCase("Hub Parent ID")) {
											 * attributeDetail.setValue(requestAttributeDetail.getValue());
											 * 
											 * } if (attributeDetail.getName().equalsIgnoreCase("Frame Sizes")) {
											 * attributeDetail.setValue(requestAttributeDetail.getValue());
											 * 
											 * } if (attributeDetail.getName().equalsIgnoreCase("VLAN ID")) {
											 * attributeDetail.setValue(requestAttributeDetail.getValue());
											 * 
											 * }
											 */
										}
									}
								}
							}
						}
					}
					solution.setProductProfileData(Utils.convertObjectToJson(solutionBean));
					LOGGER.info("Before going to update json to product solution" + solutionBean);
					productSolutionRepository.save(solution);
				}
				
				//added for nde mc
				if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
					if (Objects.nonNull(linkId) && Objects.nonNull(request.getIsMulticircuit())) {
						LOGGER.info("multi circuit nde mc flag" + request.getIsMulticircuit());
						if (request.getIsMulticircuit() == 1) {
							LOGGER.info(" link id in QuoteIllSiteService: " + linkId);
							List<QuoteIllSiteToService> quoteSiteToServiceLink = quoteIllSiteToServiceRepository
									.findByQuoteNplLink_IdAndQuoteToLe(linkId, quoteToLe.get());
							for (QuoteIllSiteToService quoteIllSiteToService : quoteSiteToServiceLink) {
								LOGGER.info(
										"Shift site and change bandwidth flag" + "CB:" + request.getIsBandwidthChanged()
												+ "SS:" + request.getIsSiteShift() + "serviceid:"
												+ quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
								if (request.getIsBandwidthChanged() != null) {
									quoteIllSiteToService
											.setBandwidthChanged((request.getIsBandwidthChanged() != null
													&& request.getIsBandwidthChanged().equalsIgnoreCase("true"))
															? CommonConstants.BACTIVE
															: CommonConstants.BDEACTIVATE);
								}
								if (request.getIsSiteShift() != null) {
									quoteIllSiteToService
											.setSiteShifted((request.getIsSiteShift() != null
													&& request.getIsSiteShift().equalsIgnoreCase("true"))
															? CommonConstants.BACTIVE
															: CommonConstants.BDEACTIVATE);
								}
								quoteIllSiteToServiceRepository.save(quoteIllSiteToService);
								LOGGER.info("updated NPL link to QuoteIllSiteToService id"
										+ quoteIllSiteToService.getId());
							}
						}
					}
				}
				
			}
			
			//NDE MC SFDC CREATE PRODUCT CALL FOR chnage bandwidth
			if (quoteToLe.isPresent()) {
				try {
					LOGGER.info("sfdc product call for NDE mc chnage bandwidth case");
					if (quoteToLe.get().getQuoteType().equalsIgnoreCase(CommonConstants.MACD)) {
						if (quoteToLe.get().getQuoteCategory().equalsIgnoreCase("CHANGE_BANDWIDTH")
								&& quoteToLe.get().getIsMultiCircuit() == 1) {
							if (quoteToLe.get().getQuote().getQuoteCode().startsWith("NDE")) {
								LOGGER.info("Before trigger sfdc product call for NDE mc chnage bandwidth");
								List<ThirdPartyServiceJob> thirdPartyService = thirdPartyServiceJobsRepository
										.findByRefIdAndServiceTypeAndThirdPartySource(
												quoteToLe.get().getQuote().getQuoteCode(), "CREATE_OPPORTUNITY",
												"SFDC");
								if (thirdPartyService.size() != 0) {
									List<ThirdPartyServiceJob> thirdPartyServiceProduct = thirdPartyServiceJobsRepository
											.findByRefIdAndServiceTypeAndThirdPartySource(
													quoteToLe.get().getQuote().getQuoteCode(), "CREATE_PRODUCT",
													"SFDC");
									LOGGER.info("sfdc product call size" + thirdPartyServiceProduct.size());
									if (thirdPartyServiceProduct.size() == 0) {
										LOGGER.info("Before TRIGGER sfdc product create call in mc CB");
										omsSfdcService.processProductServices(quoteToLe.get(),
												thirdPartyService.get(0).getTpsId());
									}
								}
							}
						}
					}
					//added for nde new sfdc
					else if(quoteToLe.get().getQuoteType().equalsIgnoreCase("NEW")) {
						if (quoteToLe.get().getQuote().getQuoteCode().startsWith("NDE")) {
							LOGGER.info("Before trigger sfdc product call for new NDE");
							List<ThirdPartyServiceJob> thirdPartyService = thirdPartyServiceJobsRepository
									.findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.get().getQuote().getQuoteCode(), "CREATE_OPPORTUNITY",
											"SFDC");
							if (thirdPartyService.size() != 0) {
								List<ThirdPartyServiceJob> thirdPartyServiceProduct = thirdPartyServiceJobsRepository
										.findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.get().getQuote().getQuoteCode(), "CREATE_PRODUCT",
												"SFDC");
								LOGGER.info("sfdc product call size"+thirdPartyServiceProduct.size());
								if (thirdPartyServiceProduct.size() == 0) {
									LOGGER.info("Before TRIGGER sfdc product create call");
									omsSfdcService.processProductServices(quoteToLe.get(), thirdPartyService.get(0).getTpsId());
								}

							}

						}
						
					}
				} catch (Exception e) {
					LOGGER.error(String.format("Message:  %s", e.getMessage()));
					LOGGER.error("Cause NDE product call: ", e.getCause());
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		return quoteDetail;
	}

	/**
	 * @param siteId
	 * @throws TclCommonException getSiteProperties used to get only site specific
	 *                            attributes
	 */
	public List<QuoteProductComponentBean> getSiteProperties(Integer siteId,Integer quoteId) throws TclCommonException {
		List<QuoteProductComponentBean> quoteProductComponentBeans = null;
		try {
			if (siteId == null) {
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

			}
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);
			}
			Optional<Quote> quote=quoteRepository.findById(quoteId);
			if (quote.isPresent()) {
				if (quote.get().getQuoteCode().startsWith("NDE")) {
					LOGGER.info("nde product component" + quote.get().getQuoteCode());
					quoteProductComponentBeans = getSortedComponents(
							constructQuoteProductComponentNde(quoteIllSite.getId(), true, null, false));
				} else {
					LOGGER.info("NPL product component" + quote.get().getQuoteCode());
					quoteProductComponentBeans = getSortedComponents(
							constructQuoteProductComponent(quoteIllSite.getId(), true, null, false));
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteProductComponentBeans;
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
	 * persistListOfQuoteLeAttributes
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public NplQuoteDetail persistListOfQuoteLeAttributes(UpdateRequest request) throws TclCommonException {
		NplQuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(request);
			quoteDetail = new NplQuoteDetail();
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

				LOGGER.info("Attribute name: " +attribute.getName());
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
			quoteLeAttributeValues.forEach(attrVal -> {
				LOGGER.info("Legal Entity Attribte Name: "+attribute.getName());
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
	 * constructQuoteToLe
	 * 
	 * @param quote
	 * @return
	 */
	private QuoteToLe constructQuoteToLe(Quote quote,NplQuoteDetail quoteDetail) {
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
		quoteToLe.setIsMultiCircuit(CommonConstants.BDEACTIVATE);
		//For partner
		quoteToLe.setClassification(quoteDetail.getClassification());
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
	 * updateQuoteToLeStatus - method to udpate quote to legal entity status
	 * 
	 * @param quoteToLeId
	 * @param status
	 * @return
	 * @throws TclCommonException
	 */
	public NplQuoteDetail updateQuoteToLeStatus(Integer quoteToLeId, String status) throws TclCommonException {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
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
			LOGGER.info("Exception occurred:{}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;

	}

	/**
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
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return legalEntityAttributes;
	}

	/**
	 * @author Dinahar
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
			List<QuoteNplSiteDto> nplSiteDtos = new ArrayList<>();
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			quote.getQuoteToLes().stream().forEach(quoteLE -> processQuoteLe(nplSiteDtos, quoteLE));
			constructProductComponents(nplSiteDtos);
			quoteResponse.setNplSiteDtos(nplSiteDtos);
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
	private void processQuoteLe(List<QuoteNplSiteDto> nplSiteDtos, QuoteToLe quoteLE) {
		quoteLE.getQuoteToLeProductFamilies().stream()
				.forEach(quoProd -> processProductSolutions(nplSiteDtos, quoProd));
	}

	/**
	 * processProductSolutions
	 * 
	 * @param illSiteDtos
	 * @param quoProd
	 */
	private void processProductSolutions(List<QuoteNplSiteDto> nplSiteDtos, QuoteToLeProductFamily quoProd) {
		quoProd.getProductSolutions().stream().forEach(prodSol -> processIllSites(nplSiteDtos, prodSol));
	}

	/**
	 * processIllSites
	 * 
	 * @param illSiteDtos
	 * @param prodSol
	 */
	private void processIllSites(List<QuoteNplSiteDto> illSiteDtos, ProductSolution prodSol) {
		prodSol.getQuoteIllSites().stream().forEach(ill -> processSiteTaxExempted(illSiteDtos, ill));
	}

	/**
	 * processSiteTaxExempted
	 * 
	 * @param illSiteDtos
	 * @param ill
	 */
	private void processSiteTaxExempted(List<QuoteNplSiteDto> illSiteDtos, QuoteIllSite ill) {
		if (ill.getIsTaxExempted() != null && ill.getIsTaxExempted() == 1) {
			illSiteDtos.add(new QuoteNplSiteDto(ill));

		}
	}

	/**
	 * updateCurrency
	 * 
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
			if (quoteToLe.isPresent() && quoteToLe.get().getCurrencyCode() != null) {
				existingCurrency = quoteToLe.get().getCurrencyCode();
				if (!Objects.isNull(existingCurrency)) {
					if (!existingCurrency.equalsIgnoreCase(inputCurrency)) {
						Quote quote = quoteToLe.get().getQuote();
						updateQuoteToLeCurrencyValues(quote, inputCurrency, existingCurrency);
						updateQuoteIllSitesCurrencyValues(quoteToLe.get(), inputCurrency, existingCurrency);
						updateQuotePriceCurrencyValues(quote, inputCurrency, existingCurrency);
						updatePaymentCurrencyWithInputCurrency(quoteToLe.get(), inputCurrency);
					}
				} else {
					throw new TclCommonException(ExceptionConstants.ILL_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
			} else {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			LOGGER.info("Exception occurs:{}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
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
		quotele.setCurrencyCode(StringUtils.upperCase(inputCurrency));
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
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * @author Thamizhselvi Perumal Method to update quoteIllSites currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	public void updateQuoteIllSitesCurrencyValues(QuoteToLe quoteToLe, String inputCurrency, String existingCurrency) {
		for (QuoteToLeProductFamily quoteLeProdFamily : quoteToLe.getQuoteToLeProductFamilies()) {
			for (ProductSolution prodSolution : quoteLeProdFamily.getProductSolutions()) {
				for (QuoteIllSite illSite : prodSolution.getQuoteIllSites()) {
					illSite.setArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getArc()));
					illSite.setMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getMrc()));
					illSite.setNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getNrc()));
					illSiteRepository.save(illSite);
				}
			}
		}
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
					for (QuoteLeAttributeValue attrval : quToLe.getQuoteLeAttributeValues()) {

						if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_ID)) {
							attributeInfo.setUserId(attrval.getAttributeValue());

						} else if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_NAME)) {
							attributeInfo.setFirstName(attrval.getAttributeValue());

						} else if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_EMAIL)) {

							attributeInfo.setEmailId(attrval.getAttributeValue());

						} else if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.DESIGNATION)) {

							attributeInfo.setDesignation(attrval.getAttributeValue());

						} else if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_NO)) {
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
	 * constructSiteFeasibility
	 * 
	 * @param illSite
	 * @return
	 */
	/*
	 * private List<SiteFeasibilityBean> constructSiteFeasibility(QuoteIllSite
	 * illSite) { List<SiteFeasibilityBean> siteFeasibilityBeans = new
	 * ArrayList<>(); List<SiteFeasibility> siteFeasibilities =
	 * siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(illSite, (byte) 1);
	 * if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) { for
	 * (SiteFeasibility siteFeasibility : siteFeasibilities) {
	 * siteFeasibilityBeans.add(constructSiteFeasibility(siteFeasibility)); } }
	 * return siteFeasibilityBeans; }
	 */

	private List<LinkFeasibilityBean> constructLinkFeasibility(QuoteNplLink nplLink) {
		List<LinkFeasibilityBean> linkFeasibilityBeans = new ArrayList<>();
		List<LinkFeasibility> linkFeasibilities = linkFeasibilityRepository.findByQuoteNplLinkAndIsSelected(nplLink,
				(byte) 1);
		if (linkFeasibilities != null && !linkFeasibilities.isEmpty()) {
			for (LinkFeasibility linkFeasibility : linkFeasibilities) {
				linkFeasibilityBeans.add(constructLinkFeasibility(linkFeasibility));
			}
		}
		return linkFeasibilityBeans;
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
		siteFeasibilityBean.setIsSelected(siteFeasibility.getIsSelected());
		return siteFeasibilityBean;
	}

	/**
	 * constructLinkFeasibility
	 * 
	 * @param linkFeasibility
	 * @return
	 */
	private LinkFeasibilityBean constructLinkFeasibility(LinkFeasibility linkFeasibility) {
		LinkFeasibilityBean linkFeasibilityBean = new LinkFeasibilityBean();
		linkFeasibilityBean.setFeasibilityCheck(linkFeasibility.getFeasibilityCheck());
		linkFeasibilityBean.setFeasibilityCode(linkFeasibility.getFeasibilityCode());
		linkFeasibilityBean.setFeasibilityMode(linkFeasibility.getFeasibilityMode());
		linkFeasibilityBean.setType(linkFeasibility.getType());
		linkFeasibilityBean.setCreatedTime(linkFeasibility.getCreatedTime());
		linkFeasibilityBean.setProvider(linkFeasibility.getProvider());
		linkFeasibilityBean.setRank(linkFeasibility.getRank());
		linkFeasibilityBean.setIsSelected(linkFeasibility.getIsSelected());
		linkFeasibilityBean.setFeasibilityModeB(linkFeasibility.getFeasibilityModeB());
		linkFeasibilityBean.setProviderB(linkFeasibility.getProviderB());
		return linkFeasibilityBean;
	}

	/**
	 * constructSlaDetails
	 * 
	 * @param illSite
	 */
	/*
	 * private List<QuoteSlaBean> constructSlaDetails(QuoteIllSite illSite) {
	 * 
	 * List<QuoteSlaBean> quoteSlas = new ArrayList<>(); if
	 * (illSite.getQuoteIllSiteSlas() != null) {
	 * 
	 * illSite.getQuoteIllSiteSlas().forEach(siteSla -> { QuoteSlaBean sla = new
	 * QuoteSlaBean(); sla.setId(siteSla.getId());
	 * sla.setSlaEndDate(siteSla.getSlaEndDate());
	 * sla.setSlaStartDate(siteSla.getSlaStartDate());
	 * sla.setSlaValue(Utils.convertEval(siteSla.getSlaValue())); if
	 * (siteSla.getSlaMaster() != null) { SlaMaster slaMaster =
	 * siteSla.getSlaMaster(); SlaMasterBean master = new SlaMasterBean();
	 * master.setId(siteSla.getId());
	 * master.setSlaDurationInDays(slaMaster.getSlaDurationInDays());
	 * master.setSlaName(slaMaster.getSlaName()); sla.setSlaMaster(master); }
	 * 
	 * quoteSlas.add(sla); }); }
	 * 
	 * return quoteSlas;
	 * 
	 * }
	 */

	/**
	 * constructNplSlaDetails
	 * 
	 * @param quoteNplLink
	 * @return
	 */
	private List<QuoteSlaBean> constructNplSlaDetails(QuoteNplLink quoteNplLink) {

		List<QuoteSlaBean> quoteSlas = new ArrayList<>();
		if (quoteNplLink.getQuoteNplLinkSlas() != null) {

			quoteNplLink.getQuoteNplLinkSlas().forEach(siteSla -> {
				QuoteSlaBean sla = new QuoteSlaBean();
				sla.setId(siteSla.getId());
				sla.setSlaEndDate(siteSla.getSlaEndDate());
				sla.setSlaStartDate(siteSla.getSlaStartDate());
				sla.setSlaValue(Utils.convertEval(siteSla.getSlaValue()));
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

		return quoteSlas;

	}

	/**
	 * getProductSlaDetails
	 * 
	 * @param serviceVarient
	 * @param accessTopology
	 * @return
	 * @throws TclCommonException
	 */
	public List<SLABean> getProductSlaDetails(String serviceVarient, String accessTopology) throws TclCommonException {
		List<SLABean> slaList = null;
		try {
			String response = (String) mqUtils.sendAndReceive(productSlaQueue,
					serviceVarient + CommonConstants.COMMA + accessTopology);
			LOGGER.info("Output Payload for for sla with serviceVarient and accessTopology {}", response);
			if (StringUtils.isNotBlank(response)) {
				TypeReference<List<SLABean>> mapType = new TypeReference<List<SLABean>>() {
				};
				ObjectMapper objectMapper = new ObjectMapper();
				slaList = objectMapper.readValue(response, mapType);
			}
		} catch (Exception e) {
			throw new TclCommonException(e);
		}
		return slaList;
	}

	/**
	 * constuctNplLinkSla
	 * 
	 * @param link
	 * @param productOfferingName
	 * @return
	 * @throws TclCommonException
	 */
	public Set<QuoteNplLinkSla> constuctNplLinkSla(QuoteNplLink link, String productOfferingName)
			throws TclCommonException {
		LOGGER.info("constuctNplLinkSla PRODUCT OFFERING NAME"+productOfferingName);
		List<SLABean> slaDetails = null;
		Set<QuoteNplLinkSla> quoteNplLinkSlaSet = new HashSet<>();
		// 2 profiles of NPL has been merged to a single profile
		// if
		// (productOfferingName.toLowerCase().contains(SLAConstants.STANDARD.toString().toLowerCase()))
		// {
		//added for nde
		if(productOfferingName.equalsIgnoreCase("National Dedicated Ethernet")) {
		slaDetails = getProductSlaDetails(SLAConstants.PREMIUM.toString(),
				SLAConstants.THREE_PATH_PROTECTION.toString());
		}
		else {
			slaDetails = getProductSlaDetails(SLAConstants.STANDARD.toString(),
					SLAConstants.TWO_PATH_PROTECTION.toString());
		}
		/*
		 * } else if
		 * (productOfferingName.toLowerCase().contains(SLAConstants.PREMIUM.toString().
		 * toLowerCase())) { slaDetails =
		 * getProductSlaDetails(SLAConstants.PREMIUM.toString(),
		 * SLAConstants.THREE_PATH_PROTECTION.toString()); }
		 */

		if (!Objects.isNull(slaDetails)) {
			for (SLABean slaDetail : slaDetails) {
				QuoteNplLinkSla quoteNplSla = new QuoteNplLinkSla();
				quoteNplSla.setQuoteNplLink(link);
				quoteNplSla.setSlaMaster(slaMasterRepository.findBySlaName(slaDetail.getFactor()));
				quoteNplSla.setSlaValue(slaDetail.getValue());
				quoteNplLinkSlaRepository.save(quoteNplSla);
				quoteNplLinkSlaSet.add(quoteNplSla);
			}
		}
		return quoteNplLinkSlaSet;
	}

	/*
	 * Method to change profiles in case of 2 profiles public void
	 * updateProductSolutionWithOffering(Integer quoteToLeId, String productName,
	 * String oldAttributeValue, String newAttributeValue) throws TclCommonException
	 * { User user = getUserId(Utils.getSource()); if (Objects.isNull(quoteToLeId)
	 * || Objects.isNull(productName) || Objects.isNull(oldAttributeValue) ||
	 * Objects.isNull(newAttributeValue)) throw new
	 * TclCommonException(ExceptionConstants.REQUEST_VALIDATION,
	 * ResponseResource.R_CODE_NOT_FOUND);
	 * 
	 * MstProductFamily productFamily = getProductFamily(productName);
	 * Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId); if
	 * (quoteToLe.isPresent()) { QuoteToLeProductFamily quoteToLeProductFamily =
	 * quoteToLeProductFamilyRepository
	 * .findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
	 * 
	 * String newProductOfferingName = getProductOfferingName(newAttributeValue);
	 * String oldProductOfferingName = getProductOfferingName(oldAttributeValue);
	 * MstProductOffering oldProductOfferng = getProductOffering(productFamily,
	 * oldProductOfferingName, user); MstProductOffering newProductOfferng =
	 * getProductOffering(productFamily, newProductOfferingName, user);
	 * ProductSolution productSolution = productSolutionRepository
	 * .findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily,
	 * oldProductOfferng); productSolution.setMstProductOffering(newProductOfferng);
	 * SolutionDetail solution = (SolutionDetail) Utils
	 * .convertJsonToObject(productSolution.getProductProfileData(),
	 * SolutionDetail.class); solution.setOfferingName(newProductOfferingName);
	 * productSolution.setProductProfileData(Utils.convertObjectToJson(solution));
	 * productSolutionRepository.save(productSolution);
	 * 
	 * } else throw new
	 * TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
	 * ResponseResource.R_CODE_NOT_FOUND);
	 * 
	 * }
	 */
	/**
	 * getProductOfferingName
	 * 
	 * @param attributeValue
	 * @return
	 */
	/*
	 * public String getProductOfferingName(String attributeValue) { String
	 * productOfferingName = null; if
	 * (attributeValue.equalsIgnoreCase(SLAConstants.STANDARD.toString()))
	 * productOfferingName =
	 * SLAConstants.STANDARD_POINT_TO_POINT_CONNECTIVITY.toString(); if
	 * (attributeValue.equalsIgnoreCase(SLAConstants.PREMIUM.toString()))
	 * productOfferingName =
	 * SLAConstants.PREMIUM_POINT_TO_POINT_CONNECTIVITY.toString(); return
	 * productOfferingName;
	 * 
	 * }
	 */

	/**
	 * 
	 * updateSfdcStage
	 * 
	 * @param quoteToLeId
	 * @param stage
	 * @throws TclCommonException
	 */
	public void updateSfdcStage(Integer quoteToLeId, String stage) throws TclCommonException {
		if (StringUtils.isNotBlank(stage) && SFDCConstants.PROPOSAL_SENT.equals(stage)){
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				String sfdcId = quoteToLe.get().getTpsSfdcOptyId();
				omsSfdcService.processUpdateOpportunity(new Date(), sfdcId, stage, quoteToLe.get());
				creditCheckService.resetCreditCheckFields( quoteToLe.get());
			}

		}
	}

	/**
	 *
	 * updateSfdcStageCofWon
	 *
	 * @param quoteToLeId
	 * @param stage
	 * @throws TclCommonException
	 */
	public void updateSfdcStageCofWon(String orderCode, String stage) throws TclCommonException {
		if (StringUtils.isNotBlank(stage) && SFDCConstants.CLOSED_WON_COF_RECI.equalsIgnoreCase(stage)){
			Quote quote = quoteRepository.findByQuoteCode(orderCode);
			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quote.getId());
				String sfdcId = quoteToLe.get(0).getTpsSfdcOptyId();
				omsSfdcService.processUpdateOpportunity(new Date(), sfdcId, stage, quoteToLe.get(0));
				creditCheckService.resetCreditCheckFields( quoteToLe.get(0));
		}
	}

	/**
	 * processMailAttachment
	 * 
	 * @param email
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public ServiceResponse processMailAttachment(String email, Integer quoteId) throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();
		if (Objects.isNull(email) || !Utils.isValidEmail(email)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String quoteHtml = nplQuotePdfService.processQuoteHtml(quoteId);
			String fileName = "Quote_" + quoteId + ".pdf";
			notificationService.processShareQuoteNotification(email,
					java.util.Base64.getEncoder().encodeToString(quoteHtml.getBytes()), userInfoUtils.getUserFullName(),
					fileName, CommonConstants.NPL);
			fileUploadResponse.setStatus(Status.SUCCESS);
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

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
				mailNotificationBean.setProductName(CommonConstants.NPL);
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
	 * deleteQuote - This method is to delete the quote based on quote id
	 * 
	 * @param quoteId
	 * @throws TclCommonException
	 */
	public void deleteQuote(Integer quoteId, String actionType) throws TclCommonException {
		try {
			if (Objects.isNull(quoteId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			if (Objects.isNull(actionType) || !actionType.equalsIgnoreCase("delete"))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			Quote quote = null;
			Order order = null;
			Optional<Quote> quoteToDelete = quoteRepository.findById(quoteId);
			Optional<Order> orderToDelete = Optional.empty();
			if (quoteToDelete.isPresent())
				orderToDelete = orderRepository.findByQuote(quoteToDelete.get());

			if (orderToDelete.isPresent()) {
				order = orderToDelete.get();
				throw new TclCommonException(ExceptionConstants.TASKS_PENDING_FOR_QUOTE, ResponseResource.R_CODE_ERROR);
				//deleteOrderRelatedDetails(order);
			}
			quote = quoteToDelete.get();

			quote.getQuoteToLes().stream().forEach(quoteToLe -> {
				quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteToLeProdFamily -> {
					quoteToLeProdFamily.getProductSolutions().stream().forEach(prodSolution -> {

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
					quoteToLeProductFamilyRepository.delete(quoteToLeProdFamily);
				});
				deleteQuoteLeAttributeValues(quoteToLe);
				quoteToLeRepository.delete(quoteToLe);
			});
			deleteQuotePrice(quote);
			quoteRepository.delete(quote);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * deleteOrderRelatedDetails - This method is to delete order details in
	 * database for an order
	 * 
	 * @param order
	 */
	private void deleteOrderRelatedDetails(Order order) {

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
	 * deleteOrderProductComponent - deletes an order product component
	 * 
	 * @param orderProdComponent
	 */
	private void deleteOrderProductComponent(OrderProductComponent orderProdComponent) {
		if (!orderProdComponent.getOrderProductComponentsAttributeValues().isEmpty())
			orderProductComponentsAttributeValueRepository
					.deleteAll(orderProdComponent.getOrderProductComponentsAttributeValues());

		orderProductComponentRepository.delete(orderProdComponent);

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

	/**
	 * deleteOrderConfirmationAudits - deletes order confirmation audits
	 * 
	 * @param order
	 */
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
	 * deleteQuoteLeAttributeValues - deletes quoteLe attribute for a quoteToLe
	 * 
	 * @param quoteToLe
	 */
	private void deleteQuoteLeAttributeValues(QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValueList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe);
		if (!quoteLeAttributeValueList.isEmpty())
			quoteLeAttributeValueRepository.deleteAll(quoteLeAttributeValueList);
		List<QuoteDelegation> quoteDelegationList = quoteDelegationRepository.findByQuoteToLe(quoteToLe);
		if (!quoteDelegationList.isEmpty())
			quoteDelegationRepository.deleteAll(quoteDelegationList);
	}

	/**
	 * deleteQuoteProductComponent - deletes a quote product component
	 * 
	 * @param quoteProdComponent
	 */
	private void deleteQuoteProductComponent(QuoteProductComponent quoteProdComponent) {
		if (!quoteProdComponent.getQuoteProductComponentsAttributeValues().isEmpty())
			quoteProductComponentsAttributeValueRepository
					.deleteAll(quoteProdComponent.getQuoteProductComponentsAttributeValues());

		quoteProductComponentRepository.delete(quoteProdComponent);
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

		List<PricingEngineResponse> pricingDetailList = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(link.getLinkCode(), "Discount");
		if (!pricingDetailList.isEmpty())
			pricingDetailsRepository.deleteAll(pricingDetailList);

		List<QuoteNplLinkSla> quoteNplLinkSlaList = quoteNplLinkSlaRepository.findByQuoteNplLink(link);
		if (!quoteNplLinkSlaList.isEmpty())
			quoteNplLinkSlaRepository.deleteAll(quoteNplLinkSlaList);

	}

	/**
	 * deleteQuotePrice - deletes price entries related to a quote
	 * 
	 * @param quote
	 */
	private void deleteQuotePrice(Quote quote) {
		List<QuotePrice> quotePriceList = quotePriceRepository.findByQuoteId(quote.getId());
		if (!quotePriceList.isEmpty())
			quotePriceRepository.deleteAll(quotePriceList);

		List<OrderPrice> orderPriceList = orderPriceRepository.findByQuoteId(quote.getId());
		if (!orderPriceList.isEmpty())
			orderPriceRepository.deleteAll(orderPriceList);

	}

	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;
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
		msaBean.setProductName(CommonConstants.NPL);
		return msaBean;
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
	 * getFeasiblityAndPricingDetailsForQuoteNplBean - retrieves feasibility and
	 * pricing details for a link
	 * 
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public NplPricingFeasibilityBean getFeasiblityAndPricingDetailsForQuoteNplBean(Integer linkId)
			throws TclCommonException {
		NplPricingFeasibilityBean link = new NplPricingFeasibilityBean();
		try {
			if (Objects.isNull(linkId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(linkId);
			if (quoteNplLink.isPresent()) {
				List<LinkFeasibility> feasiblityDetails = linkFeasibilityRepository
						.findByQuoteNplLink(quoteNplLink.get());
				List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingType(quoteNplLink.get().getLinkCode(), CommonConstants.LINK);
				link = constructQuoteLinkPricingFeasibilityDetails(quoteNplLink.get(), feasiblityDetails,
						pricingDetails);
			} else {
				throw new TclCommonException(ExceptionConstants.ORDER_LINK_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return link;
	}

	/**
	 * constructQuoteLinkPricingFeasibilityDetails
	 * 
	 * @param quoteNplLink
	 * @param feasiblityDetails
	 * @param pricingDetails
	 * @return
	 */
	private NplPricingFeasibilityBean constructQuoteLinkPricingFeasibilityDetails(QuoteNplLink quoteNplLink,
			List<LinkFeasibility> feasiblityDetails, List<PricingEngineResponse> pricingDetails) {
		Optional<QuoteIllSite> quoteIllSiteA = illSiteRepository.findById(quoteNplLink.getSiteAId());
		Optional<QuoteIllSite> quoteIllSiteB = illSiteRepository.findById(quoteNplLink.getSiteBId());
		NplPricingFeasibilityBean link = new NplPricingFeasibilityBean();
		link.setLinkId(quoteNplLink.getId());
		link.setLinkCode(quoteNplLink.getLinkCode());
		link.setIsFeasible(quoteNplLink.getFeasibility());
		link.setChargeableDistance(quoteNplLink.getChargeableDistance());
		if (quoteIllSiteA.isPresent())
			link.setIsTaxExemptedSiteA(quoteIllSiteA.get().getIsTaxExempted());
		if (quoteIllSiteB.isPresent())
			link.setIsTaxExemptedSiteB(quoteIllSiteB.get().getIsTaxExempted());
		link.setFeasiblityDetails(constructQuoteFeasiblityResponse(feasiblityDetails));
		link.setPricingDetails(constructPricingDetails(pricingDetails));
		return link;
	}

	/**
	 * constructQuoteFeasiblityResponse
	 * 
	 * @param feasiblityDetails
	 * @return
	 */
	private List<NplFeasiblityBean> constructQuoteFeasiblityResponse(List<LinkFeasibility> feasiblityDetails) {
		List<NplFeasiblityBean> feasiblityResponse = new ArrayList<>();
		feasiblityDetails.stream().forEach(feasiblity -> {
			NplFeasiblityBean feasiblityBean = new NplFeasiblityBean();
			feasiblityBean.setId(feasiblity.getId());
			feasiblityBean.setCreatedTime(feasiblity.getCreatedTime());
			feasiblityBean.setFeasibilityCheck(feasiblity.getFeasibilityCheck());
			feasiblityBean.setFeasibilityMode(feasiblity.getFeasibilityMode());
			
			feasiblityBean.setaEnd_feasibilityMode(feasiblity.getFeasibilityMode());
			feasiblityBean.setbEnd_feasibilityMode(feasiblity.getFeasibilityModeB());

			feasiblityBean.setFeasibilityCode(feasiblity.getFeasibilityCode());
			feasiblityBean.setIsSelected(feasiblity.getIsSelected());
			feasiblityBean.setType(feasiblity.getType());
			feasiblityBean.setProvider(feasiblity.getProvider());
			feasiblityBean.setRank(feasiblity.getRank());
			feasiblityBean.setResponse(feasiblity.getResponseJson());
			feasiblityBean.setLinkId(feasiblity.getQuoteNplLink().getId());
			
			// newly added attrs
			feasiblityBean.setFeasibilityType(feasiblity.getFeasibilityType());

			feasiblityBean.setaEnd_feasibilityType(feasiblity.getFeasibilityType());
			feasiblityBean.setbEnd_feasibilityType(feasiblity.getFeasibilityTypeB());

			feasiblityBean.setaEnd_provider(feasiblity.getProvider());
			feasiblityBean.setbEnd_provider(feasiblity.getProviderB());

			
			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}

	/**
	 * @author Dinahar Vivekanandan constructPricingDetails
	 * @param pricingDetails
	 * @return
	 */
	private List<PricingDetailBean> constructPricingDetails(List<PricingEngineResponse> pricingDetails) {
		List<PricingDetailBean> pricingResponse = new ArrayList<>();
		pricingDetails.stream().forEach(pricing -> {
			PricingDetailBean pricingBean = new PricingDetailBean();
			pricingBean.setId(pricing.getId());
			pricingBean.setDateTime(pricing.getDateTime());
			pricingBean.setPriceMode(pricingBean.getPriceMode());
			pricingBean.setPricingType(pricing.getPricingType());
			pricingBean.setRequest(pricing.getRequestData());
			pricingBean.setResponse(pricing.getResponseData());
			pricingBean.setLinkCode(pricing.getSiteCode());
			pricingResponse.add(pricingBean);
		});
		return pricingResponse;
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
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
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
					List<ProductAttributeMaster> lconRemarksProductAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus("LCON_REMARKS", CommonConstants.BACTIVE);
					
					if (mstProductComponents != null && lconContactProductAttributeMasters != null
							&& !lconContactProductAttributeMasters.isEmpty() && lconNameProductAttributeMasters != null
							&& !lconNameProductAttributeMasters.isEmpty() && lconRemarksProductAttributeMasters != null && !lconRemarksProductAttributeMasters.isEmpty()) {
						if (lconUpdateBeans != null && !lconUpdateBeans.isEmpty()) {
							lconUpdateBeans.stream().forEach(lconUpdateBean -> {

								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndReferenceName(
												lconUpdateBean.getSiteId(), mstProductComponents,
												productFamily.getMstProductFamily(),QuoteConstants.NPL_LINK.toString());
								if (quoteProductComponents != null && quoteProductComponents.isEmpty()) {
									QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
									quoteProductComponent.setMstProductComponent(mstProductComponents);
									quoteProductComponent.setReferenceId(lconUpdateBean.getSiteId());
									quoteProductComponent.setMstProductFamily(productFamily.getMstProductFamily());
									quoteProductComponent.setType("primary");
									quoteProductComponent.setReferenceName(QuoteConstants.NPL_LINK.toString());
									quoteProductComponentRepository.save(quoteProductComponent);
								}
								quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponentAndMstProductFamily(
												lconUpdateBean.getSiteId(), mstProductComponents,
												productFamily.getMstProductFamily());
								if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
									
									
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
									//LCON REMARKS
									List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeRemarks = quoteProductComponentsAttributeValueRepository
											.findByQuoteProductComponentAndProductAttributeMaster(
													quoteProductComponents.get(0),lconRemarksProductAttributeMasters.get(0));
									if(quoteProductComponentsAttributeRemarks != null && quoteProductComponentsAttributeRemarks.isEmpty()) {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
										quoteProductComponentsAttributeValue
										.setAttributeValues(lconUpdateBean.getLconRemarks());
										quoteProductComponentsAttributeValue
										.setProductAttributeMaster(lconRemarksProductAttributeMasters.get(0));
										quoteProductComponentsAttributeValue
										.setQuoteProductComponent(quoteProductComponents.get(0));
										quoteProductComponentsAttributeValueRepository
										.save(quoteProductComponentsAttributeValue);
									}else {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeRemarks
												.get(0);
										quoteProductComponentsAttributeValue
										.setAttributeValues(lconUpdateBean.getLconRemarks());
								quoteProductComponentsAttributeValueRepository
										.save(quoteProductComponentsAttributeValue);										
									}
								}
								try {
									omsSfdcService.updateFeasibility(quoteToLe.get(), lconUpdateBean.getSiteId());
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

	/**
	 * 
	 * Approve Quotes for Docusign
	 * 
	 * @param quoteuuId
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public NplQuoteDetail approvedDocusignQuotes(String quoteuuId) throws TclCommonException {
		NplQuoteDetail detail = null;
		try {
			detail = new NplQuoteDetail();
			Quote quote = quoteRepository.findByQuoteCode(quoteuuId);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			
			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			if (optionalQuoteToLe.isPresent() && Objects.nonNull(optionalQuoteToLe.get().getQuoteType())
					&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType()) && (optionalQuoteToLe.get().getIsAmended()==null ||optionalQuoteToLe.get().getIsAmended()!=1)) {
				nplMACDService.approvedMacdDocusignQuotes(quoteuuId);
				
			} else {

				Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

				if (order != null) {
					detail.setOrderId(order.getId());
				} else {
					// order = constructOrder(quote, detail);
					Map<String, Object> responseCollection = constructOrder(quote, detail);
					order = (Order) responseCollection.get(NplOrderConstants.METHOD_RESPONSE);
					if (!order.getOrderCode().startsWith("NDE")) {
						if (checkO2cEnabled(quote)) {
							LOGGER.info("OnnetWL");
							order.setOrderToCashOrder(CommonConstants.BACTIVE);
							order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
							orderRepository.save(order);
						}
					}
					//@SuppressWarnings("unchecked")

					Set<ProductSolution> productSolutions = quote.getQuoteToLes().stream().findFirst().get().getQuoteToLeProductFamilies().stream().findFirst().get().getProductSolutions();
					//LOGGER.info("Product solutuon name is ----> " , productSolutions.stream().findFirst().get().getMstProductOffering().getProductName());
					for(ProductSolution sol : productSolutions){
						if(Objects.nonNull(sol.getMstProductOffering().getProductName())){
							LOGGER.info("Product Name is ----> {} ", sol.getMstProductOffering().getProductName());
						}
						if(Objects.nonNull(sol.getMstProductOffering().getProductName()) &&
								!CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(sol.getMstProductOffering().getProductName())){
							LOGGER.info("Entering persistOrderNplLinkWithOrder method for quote ----> {}  ", quote.getQuoteCode());
							Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
									.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);

							if (!optionalQuoteToLe.get().getQuoteType().equalsIgnoreCase("RENEWALS")) {
								persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
							}
						}
					}
					if(optionalQuoteToLe.get().getQuoteType().equalsIgnoreCase("RENEWALS")) {
						Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
									.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);
						persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
						}

					//persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
					detail.setOrderId(order.getId());
					// Trigger SFDC
					for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
						if (quoteLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
							omsSfdcService.processSiteDetailsRenewals(quoteLe); // renewals-sfdc
						} else {
							omsSfdcService.processSiteDetails(quoteLe);
						}
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
						LOGGER.info("SFDC update is completed for quote code {}",quoteuuId);
						List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
						Map<String, String> cofObjectMapper = new HashMap<>();
						CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
						if (cofDetails != null) {
							cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
						}
						LOGGER.info("Fetching cof details is completed for quote code {}",quoteuuId);
						Integer userId = order.getCreatedBy();
						String userEmail = null;
						if (userId != null) {
							Optional<User> userDetails = userRepository.findById(userId);
							if (userDetails.isPresent()) {
								userEmail = userDetails.get().getEmailId();
							}
						}
						LOGGER.info("Getting oms attachment is starting for quote code {}",quoteuuId);
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
							LOGGER.info("Oms Attachment saved for quote code {}",quoteuuId);
							nplQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
									orderToLe.getId(), cofObjectMapper);
							LOGGER.info("download cof from storage for quote code {}",quoteuuId);
							break;
						}
						LOGGER.info("processing mail notification for quote code {}",quoteuuId);
						processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
						LOGGER.info("Starting quote delegation for quote code {}",quoteuuId);
						for (QuoteDelegation quoteDelegation : quoteDelegate) {
							quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
							quoteDelegationRepository.save(quoteDelegation);
						}
						LOGGER.info("Quote delegation completed for quote code {}",quoteuuId);
					}
				}
				LOGGER.info("Starting to update the stage for quote code {}",quoteuuId);
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				}
				LOGGER.info("Stage updation is completed for quote code {}",quoteuuId);
				if (detail.isManualFeasible()) {
					LOGGER.info("Cloning the not feasible sites for new quote code {}",quoteuuId);
					cloneQuoteForNonFeasibileLink(quote);
					LOGGER.info("Cloning the not feasible sites completed for quote code {}",quoteuuId);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in approve quotes",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public NplQuoteDetail approvedQuotesHardFix(UpdateRequest request, String ipAddress) throws TclCommonException {

		NplQuoteDetail detail = null;
		try {
			detail = new NplQuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);
			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				Map<String, Object> responseCollection = constructOrder(quote, detail);
				order = (Order) responseCollection.get(NplOrderConstants.METHOD_RESPONSE);

				Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
						.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);

				persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
				detail.setOrderId(order.getId());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
				// Trigger SFDC
			}

			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}
			}
			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileLink(quote);
			}

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	/**
	 * Sets the site as not feasible for npl
	 * 
	 * @author Kavya Singh
	 * @param quoteToLeId
	 * @param siteId
	 */
	@Transactional
	public void siteNotFeasibleNpl(Integer quoteToLeId, Integer siteId) {
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
	public String retriggerCreditCheckNpl(Integer quoteId) throws TclCommonException {
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
							quoteToLe.setTpsSfdcCreditRemarks(entry.getCreditRemarks());
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
/*                                List<String> serviceStatusList = new ArrayList<>();
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


			//    });




		}
		return creditCheckStatus[0];

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
				processBillingValidation(quoteLeId, validationMessages);
				processContractingInfoValidation(quoteLeId, validationMessages);
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
	private void processContractingInfoValidation(Integer quoteToLeId, List<String> validationMessages) {

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
		 * List<Map<String, String>> gstleAttr = quoteLeAttributeValueRepository
		 * .findByQuoteToLeIdAndAttributeName(quoteToLeId,
		 * LeAttributesConstants.LE_STATE_GST_NO.toString()); if (gstleAttr.isEmpty()) {
		 * List<Map<String, String>> gstAttr = quoteLeAttributeValueRepository
		 * .findByQuoteToLeIdAndAttributeName(quoteToLeId,
		 * LeAttributesConstants.GST_NUMBER.toString()); if (gstAttr.isEmpty()) {
		 * validationMessages.add("GST Number is Mandatory"); } }
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

	/**
	 * processProductComponent- This method process the product component details
	 *
	 * @param productFamily
	 * @param illSite
	 * @param component
	 * @param user
	 * @throws TclCommonException
	 */
	protected void processProductComponent(MstProductFamily productFamily, QuoteIllSite illSite,
										 ComponentDetail component, User user) throws TclCommonException {
		try {
			LOGGER.info("Offering name{}" ,illSite.getProductSolution().getMstProductOffering().getProductName());

			String refType = (CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(illSite.getProductSolution().getMstProductOffering().getProductName())) ? QuoteConstants.ILLSITES.toString(): QuoteConstants.NPL_SITES.toString();
			MstProductComponent productComponent = getProductComponent(component, user);
			QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily,
					illSite.getId(), refType);
			quoteComponent.setType(component.getType());
			quoteProductComponentRepository.save(quoteComponent);
			LOGGER.info("saved successfully");
			for (AttributeDetail attribute : component.getAttributes()) {
				processProductAttribute(quoteComponent, attribute, user,null,null);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private List<MACDExistingComponentsBean> generateExistingComponentsForMacd(QuoteToLe quoteToLe, QuoteNplLink link)
	{
		LOGGER.info("generateExistingComponentsForMacd LINKID"+link.getId());
		List<MACDExistingComponentsBean> existingComponentsBeanList=new ArrayList<>();
		List<String> serviceIdsList=new ArrayList<>();
		Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(link.getId());
		List<String> serviceIds = macdUtils.getServiceIdBasedOnLink(link, quoteToLe);
		
		if(!serviceIds.isEmpty())
		{
			serviceIds.stream().forEach(serviceId->{
				try {
					MACDExistingComponentsBean existingComponent = new MACDExistingComponentsBean();
					//order Id need to be removed
					List<Map> existingComponentMap = constructExistingComponentsforIsvPage(quoteToLe, serviceId);
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

	public List<Map> constructExistingComponentsforIsvPage(QuoteToLe quoteToLe, String serviceId) throws TclCommonException {

		List<Map> existingComponentsList = new ArrayList<>();
		String secondaryServiceId = null;
		Integer primaryOrderId = null;
		Integer secondaryOrderId = null;

		if (Objects.nonNull(quoteToLe) && (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
				|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()))	
				&& Objects.nonNull(serviceId) ) {
			Map<String, Object> primaryComponentsMap = new HashMap<>();
			SIServiceDetailDataBean primaryServiceDataBean = new SIServiceDetailDataBean();
			SIServiceDetailDataBean sIServiceDetailDataBean = new SIServiceDetailDataBean();
			if (MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				List <SIServiceDetailDataBean> sIServiceDetailDataBeanList = macdUtils.getServiceDetailNPLTermination(serviceId);
				if(Objects.nonNull(sIServiceDetailDataBeanList) && !sIServiceDetailDataBeanList.isEmpty()) {
					sIServiceDetailDataBean = sIServiceDetailDataBeanList.get(0);
				}
			} else {
				sIServiceDetailDataBean = macdUtils.getServiceDetail(serviceId, quoteToLe.getQuoteCategory());
			}
			if(Objects.nonNull(sIServiceDetailDataBean)) {
				String linkType = sIServiceDetailDataBean.getLinkType();
				LOGGER.info("Link type is ---> {} for s id ---> {} ", sIServiceDetailDataBean.getLinkType(), sIServiceDetailDataBean.getTpsServiceId());
	
				if (linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SINGLE") || ("LINK").equalsIgnoreCase(linkType)) {
				
					primaryServiceDataBean = sIServiceDetailDataBean;
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
					List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
					if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
						
					List<PricingEngineResponse> pricingEngineResponseList = pricingDetailsRepository.findBySiteCode(quoteIllSiteToServiceList.get(0).getQuoteNplLink().getLinkCode());
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
				}
				
				
				existingComponentsList.add(0, primaryComponentsMap);
				LOGGER.info("Primary components is  ----> {}  ", primaryComponentsMap);
			}
		}
		return existingComponentsList;
	
	}

	/**
	 * Get enrichment link properties
	 *
	 * @param quoteCode
	 * @return {@link EnrichmentDetailsBean}
	 */
	public EnrichmentDetailsBean getEnrichmentLinkProperties(String quoteCode, String productName) {
		List<String> linkEnrichmentAttributeNames = Arrays.asList("Connector Type - A end","Connector Type - B end");
		List<String> siteEnrichmentAttributeNames = Arrays.asList("Local Loop Bandwidth","GSTNO");
		List<Integer> siteIds = new ArrayList<>();
		List<QuoteNplLink> quoteNplLinks = nplLinkRepository.findLinks(quoteCode);
		EnrichmentDetailsBean enrichmentDetailsBean = new EnrichmentDetailsBean();
		enrichmentDetailsBean.setQuoteCode(quoteCode);
		if (!CollectionUtils.isEmpty(quoteNplLinks)) {
			Map<String, List<QuoteProductComponentsAttributeValueBean>> linkEnrichmentDetails = new HashMap<>();
			getEnrichmentAttributesOfLink(productName, linkEnrichmentAttributeNames, quoteNplLinks, linkEnrichmentDetails);
			enrichmentDetailsBean.setLinkEnrichmentDetails(linkEnrichmentDetails);

			Map<String, List<QuoteProductComponentsAttributeValueBean>> siteEnrichmentDetails = new HashMap<>();
			getEnrichmentAttributesOfSite(productName, siteEnrichmentAttributeNames, quoteNplLinks, siteEnrichmentDetails);
			enrichmentDetailsBean.setSiteEnrichmentDetails(siteEnrichmentDetails);

		}
		List<QuoteIllSite> quoteIllSites = illSiteRepository.findSites(quoteCode);
		if(Objects.nonNull(quoteIllSites) && !quoteIllSites.isEmpty() &&
				CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(quoteIllSites.stream().
						findFirst().get().getProductSolution().getMstProductOffering().getProductName())) {
			List<String> enrichmentAttributeNames = Arrays.asList("Interface", "Bandwidth", "Cross Connect Type","GSTNO_A","GSTNO_Z");
			Map<String, List<QuoteProductComponentsAttributeValueBean>> siteEnrichmentDetails = new HashMap<>();
			getCrossConnectEnrichmentSiteProperties(quoteCode, enrichmentAttributeNames, productName,siteEnrichmentDetails);
			enrichmentDetailsBean.setSiteEnrichmentDetails(siteEnrichmentDetails);

		}
		LOGGER.info("EnrichmentDetailsBean :: {}", enrichmentDetailsBean);
		return enrichmentDetailsBean;
	}

	/**
	 * Get enrichment details of site
	 *
	 * @param productName
	 * @param siteEnrichmentAttributeNames
	 * @param quoteNplLinks
	 * @param siteEnrichmentDetails
	 */
	private void getEnrichmentAttributesOfSite(String productName, List<String> siteEnrichmentAttributeNames, List<QuoteNplLink> quoteNplLinks, Map<String, List<QuoteProductComponentsAttributeValueBean>> siteEnrichmentDetails) {
		List<Integer> siteIds;
		siteIds = quoteNplLinks.stream().map(QuoteNplLink::getSiteAId).distinct().collect(Collectors.toList());
		siteIds.addAll(quoteNplLinks.stream().map(QuoteNplLink::getSiteBId).distinct().collect(Collectors.toList()));

		siteIds.stream().forEach(siteId -> {
			QuoteIllSite quoteIllSite = illSiteRepository.findById(siteId).get();
			List<QuoteProductComponentsAttributeValue> enrichmentAttributes = quoteProductComponentsAttributeValueRepository.findOrderEnrichmentAttributesByAttributesAndSiteId(siteEnrichmentAttributeNames, quoteIllSite.getId(), productName);
			List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean = enrichmentAttributes.stream().map(quoteProductComponentsAttributeValue -> new QuoteProductComponentsAttributeValueBean(quoteProductComponentsAttributeValue)).collect(Collectors.toList());
			getSiteAddressDetail(quoteIllSite.getErfLocSitebLocationId(), enrichmentAttributesBean);
			getLocalItContactDetails(productName, quoteIllSite, enrichmentAttributesBean);
			getDemarcationDetails(quoteIllSite, enrichmentAttributesBean);

			if (!CollectionUtils.isEmpty(enrichmentAttributesBean)) {
				siteEnrichmentDetails.put(quoteIllSite.getSiteCode(), enrichmentAttributesBean);
			}
		});
	}

	/**
	 * Get local it contact details
	 *
	 * @param productName
	 * @param quoteIllSite
	 * @param enrichmentAttributesBean
	 */
	private void getLocalItContactDetails(String productName, QuoteIllSite quoteIllSite, List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean) {
		List<QuoteProductComponentsAttributeValue> localItContactAttributes = quoteProductComponentsAttributeValueRepository.findOrderEnrichmentAttributesByAttributesAndSiteId(Arrays.asList("LOCAL_IT_CONTACT"), quoteIllSite.getId(), productName);
		localItContactAttributes.stream().forEach(quoteProductComponentsAttributeValue -> {
			Map<String, Object> localItDetails = getLocalItContacts(quoteProductComponentsAttributeValue.getAttributeValues());
			if (!localItDetails.isEmpty()) {
				if (localItDetails.get("EMAIL") != null) {
					QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
					quoteProductComponentsAttributeValueBean.setName("Local IT Contact Email");
					quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetails.get("EMAIL").toString());
					enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
				}
				if (localItDetails.get("CONTACT_NO") != null) {
					QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
					quoteProductComponentsAttributeValueBean.setName("Local IT Contact Number");
					quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetails.get("CONTACT_NO").toString());
					enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
				}
				if (localItDetails.get("NAME") != null) {
					QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
					quoteProductComponentsAttributeValueBean.setName("Local IT Contact Name");
					quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetails.get("NAME").toString());
					enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
				}
			}
		});
	}

	/**
	 * Get local it contact details by localit contact id
	 *
	 * @param localItContactId
	 * @return {@link Map}
	 */
	private Map<String, Object> getLocalItContacts(String localItContactId) {
		LOGGER.info("MDC Filter token value in before Queue call getLocalItContacts {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String localItResponse = null;
		Map<String, Object> localItDetail = new HashMap<>();
		try {
			localItResponse = (String) mqUtils.sendAndReceive(localItQueue, localItContactId);
			if (localItResponse != null) {
				localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse,
						Map.class);
				return localItDetail;
			}
		} catch (TclCommonException e) {
			LOGGER.info("Error in finding local it contact {} ", e);
		}
		return localItDetail;
	}

	/**
	 * Get enrichment details of link
	 *
	 * @param productName
	 * @param linkEnrichmentAttributeNames
	 * @param quoteNplLinks
	 * @param siteEnrichmentDetails
	 */
	private void getEnrichmentAttributesOfLink(String productName, List<String> linkEnrichmentAttributeNames, List<QuoteNplLink> quoteNplLinks, Map<String, List<QuoteProductComponentsAttributeValueBean>> linkEnrichmentDetails) {
		quoteNplLinks.stream().forEach(quoteNplLink -> {
			List<QuoteProductComponentsAttributeValue> enrichmentAttributes = quoteProductComponentsAttributeValueRepository.findOrderEnrichmentAttributesByAttributesAndSiteId(linkEnrichmentAttributeNames, quoteNplLink.getId(), productName);
			List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean = enrichmentAttributes.stream().map(quoteProductComponentsAttributeValue -> new QuoteProductComponentsAttributeValueBean(quoteProductComponentsAttributeValue)).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(enrichmentAttributesBean)) {
				linkEnrichmentDetails.put(quoteNplLink.getLinkCode(), enrichmentAttributesBean);
			}
		});
	}


	/**
	 *
	 * @param quoteIllSite
	 * @param enrichmentAttributesBean
	 */
	private void getDemarcationDetails(QuoteIllSite quoteIllSite, List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean) {
		if (Objects.nonNull(quoteIllSite.getErfLocSitebLocationId())) {
			Map<String, Object> demarcationDetails = getDemarcationDetailsQueue(quoteIllSite.getErfLocSitebLocationId().toString());
			if (!demarcationDetails.isEmpty()) {
				if (Objects.nonNull(demarcationDetails.get("BUILDING_NAME"))) {
					QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
					quoteProductComponentsAttributeValueBean.setName("Demarcation Building Name");
					quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationDetails.get("BUILDING_NAME").toString());
					enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
				}
				if (Objects.nonNull(demarcationDetails.get("FLOOR"))) {
					QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
					quoteProductComponentsAttributeValueBean.setName("Demarcation Floor");
					quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationDetails.get("FLOOR").toString());
					enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
				}
				if (Objects.nonNull(demarcationDetails.get("ROOM"))) {
					QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
					quoteProductComponentsAttributeValueBean.setName("Demarcation Room");
					quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationDetails.get("ROOM").toString());
					enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
				}
				if (Objects.nonNull(demarcationDetails.get("WING"))) {
					QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
					quoteProductComponentsAttributeValueBean.setName("Demarcation Rack");
					quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationDetails.get("WING").toString());
					enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
				}
			}
		}
	}

	/**
	 * Get demarcation details by location id
	 *
	 * @param localItContactId
	 * @return {@link Map}
	 */
	private Map<String, Object> getDemarcationDetailsQueue(String locationId) {
		LOGGER.info("MDC Filter token value in before Queue call getDemarcationDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String demarcationResponse = null;
		Map<String, Object> demarcationDetails = new HashMap<>();
		try {
			demarcationResponse = (String) mqUtils.sendAndReceive(demarcationQueue, locationId);
			if (demarcationResponse != null) {
				demarcationDetails = (Map<String, Object>) Utils.convertJsonToObject(demarcationResponse,
						Map.class);
				return demarcationDetails;
			}
		} catch (TclCommonException e) {
			LOGGER.info("Error in finding demarcation details {} ", e);
		}
		return demarcationDetails;
	}
	/**
	 * Method to get enrichmentdetails by quote code
	 *
	 * @param quoteCode
	 * @return {@link EnrichmentDetailsBean}
	 */
	private EnrichmentDetailsBean getCrossConnectEnrichmentSiteProperties(String quoteCode, List<String> enrichmentAttributeNames, String productName,Map<String, List<QuoteProductComponentsAttributeValueBean>> siteEnrichmentDetails) {
		List<QuoteIllSite> quoteIllSites = illSiteRepository.findSites(quoteCode);
		EnrichmentDetailsBean enrichmentDetailsBean = new EnrichmentDetailsBean();
		List<String> enrichmentAttributeNamesPassive = Arrays.asList("Cross Connect Type",
				"Media Type", "No. of Fiber pairs", "Do you want fiber entry", "Type of Fibre Entry", "No. of Cable pairs", "Fiber Type","GSTNO_A","GSTNO_Z");
		List<String> enrichmentAttributeNamesActive = Arrays.asList("Cross Connect Type","GSTNO_A","GSTNO_Z");

		if (!CollectionUtils.isEmpty(quoteIllSites)) {
			enrichmentDetailsBean.setQuoteCode(quoteCode);
			quoteIllSites.stream().forEach(quoteIllSite -> {
				List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean=new ArrayList<>();
				CrossConnectEnrichmentBean crossConnectEnrichmentBean = new CrossConnectEnrichmentBean();

				List<QuoteProductComponentsAttributeValue> enrichmentAttributes = quoteProductComponentsAttributeValueRepository.
						findOrderEnrichmentAttributesByAttributesAndSiteId(enrichmentAttributeNames, quoteIllSite.getId(), productName);
				QuoteProductComponentsAttributeValue quoteProductComponentsAttribute=enrichmentAttributes.stream().
						filter(attribute->attribute.getProductAttributeMaster().getName().equalsIgnoreCase("Cross Connect Type")).findFirst().get();

				getSiteAddressDetail(quoteIllSite.getErfLocSitebLocationId(),enrichmentAttributesBean);

				if(CrossconnectConstants.PASSIVE.equalsIgnoreCase(quoteProductComponentsAttribute.getAttributeValues())) {
					enrichmentAttributes = quoteProductComponentsAttributeValueRepository.
							findOrderEnrichmentAttributesByAttributesAndSiteId(enrichmentAttributeNamesPassive, quoteIllSite.getId(), productName);
					enrichmentAttributesBean.addAll(enrichmentAttributes.stream().filter(quoteProductComponentsAttributeValue->
							Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())).map(quoteProductComponentsAttributeValue ->
							new QuoteProductComponentsAttributeValueBean(quoteProductComponentsAttributeValue)).collect(Collectors.toList()));
					if(enrichmentAttributes.stream().
							filter(attribute->attribute.getProductAttributeMaster().getName().equalsIgnoreCase(CrossconnectConstants.FIBER_ENTRY)).
							findFirst().get().getAttributeValues().equalsIgnoreCase("No")){
						enrichmentAttributesBean.removeIf(enrichmtAttr->enrichmtAttr.getName().equalsIgnoreCase(CrossconnectConstants.TYPE_OF_FIBER_ENTRY));
						//enrichmentAttributesBean.removeIf(enrichmtAttr->enrichmtAttr.getName().equalsIgnoreCase(CrossconnectConstants.FIBER_PAIRS_COUNT));
						//enrichmentAttributesBean.removeIf(enrichmtAttr->enrichmtAttr.getName().equalsIgnoreCase(CrossconnectConstants.FIBER_ENTRY));
						enrichmentAttributesBean.removeIf(enrichmtAttr->enrichmtAttr.getName().equalsIgnoreCase("Fiber Type"));
					}
					if(enrichmentAttributes.stream().
							filter(attribute->attribute.getProductAttributeMaster().getName().equalsIgnoreCase(CrossconnectConstants.MEDIA_TYPE)).
							findFirst().get().getAttributeValues().toUpperCase().contains("CABLE")){
						enrichmentAttributesBean.removeIf(enrichmtAttr->enrichmtAttr.getName().equalsIgnoreCase(CrossconnectConstants.FIBER_PAIRS_COUNT));
						enrichmentAttributesBean.removeIf(enrichmtAttr->enrichmtAttr.getName().equalsIgnoreCase(CrossconnectConstants.TYPE_OF_FIBER_ENTRY));
					}
					else{
						enrichmentAttributesBean.removeIf(enrichmtAttr->enrichmtAttr.getName().equalsIgnoreCase(CrossconnectConstants.CABLE_PAIR_COUNT));
					}

				}
				else{
					enrichmentAttributes = quoteProductComponentsAttributeValueRepository.
							findOrderEnrichmentAttributesByAttributesAndSiteId(enrichmentAttributeNamesActive, quoteIllSite.getId(), productName);
					enrichmentAttributesBean.addAll(enrichmentAttributes.stream().filter(quoteProductComponentsAttributeValue->
							Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())).map(quoteProductComponentsAttributeValue ->
							new QuoteProductComponentsAttributeValueBean(quoteProductComponentsAttributeValue)).collect(Collectors.toList()));

				}

//				getCrossConnectLocalItContactDetails(quoteIllSite.getId(), enrichmentAttributesBean, crossConnectEnrichmentBean);
//				getCrossConnectDemarcationDetails(quoteIllSite, enrichmentAttributesBean, crossConnectEnrichmentBean);
				getCrossConnectDemarcationDetailsV2(quoteIllSite.getId(), enrichmentAttributesBean, crossConnectEnrichmentBean);

				if (!CollectionUtils.isEmpty(enrichmentAttributesBean)) {
					siteEnrichmentDetails.put(quoteIllSite.getSiteCode(), enrichmentAttributesBean);
				}
			});
			enrichmentDetailsBean.setSiteEnrichmentDetails(siteEnrichmentDetails);
		}
		LOGGER.info("EnrichmentDetailsBean :: {}", enrichmentDetailsBean);
		return enrichmentDetailsBean;
	}

	private void getSiteAddressDetail(Integer locationId,List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean){
		try {
			AddressDetail addressDetail=getUserAddress(locationId);
			if(Objects.nonNull(addressDetail) && Objects.nonNull(addressDetail.getAddressLineOne()) && !addressDetail.getAddressLineOne().isEmpty()) {
				QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
				quoteProductComponentsAttributeValueBean.setName("Site Address Details");
				quoteProductComponentsAttributeValueBean.setAttributeValues(addressDetail.getAddressLineOne());
				enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
			}
		}catch (Exception ex){
			LOGGER.error("Error in address detail for order Enrichment attribute PDF",ex.getMessage());
		}
	}

	/**
	 * Get local it contact details
	 *
	 * @param productName
	 * @param quoteIllSite
	 * @param enrichmentAttributesBean
	 */
	public void getCrossConnectLocalItContactDetails(Integer siteId, List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean,
													 CrossConnectEnrichmentBean crossConnectEnrichmentBean) {
		List<QuoteProductComponent> components  = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId,
				IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.ILLSITES.toString());
		QuoteProductComponent quoteProductComponentVal=components.stream().filter(quoteProductComponent ->
				quoteProductComponent.getMstProductComponent().getName().equalsIgnoreCase(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties())).findFirst().get();
		Optional<QuoteProductComponentsAttributeValue> localItContactA=quoteProductComponentVal.getQuoteProductComponentsAttributeValues().
				stream().filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase("LOCAL_IT_CONTACT_A")).findFirst();
		Optional<QuoteProductComponentsAttributeValue> localItContactZ=quoteProductComponentVal.getQuoteProductComponentsAttributeValues().
				stream().filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase("LOCAL_IT_CONTACT_Z")).findFirst();
		LOGGER.info("Local IT details : {}"+ components.size());
			if(localItContactZ.isPresent() && localItContactZ.isPresent()) {
				LOGGER.info("Local IT details master : {}"+ localItContactA.get().getAttributeValues()+"--"+localItContactZ.get().getAttributeValues());
				Map<String, Object> localItDetailsA = getLocalItContacts(localItContactA.get().getAttributeValues());
				LOGGER.info("Local IT queue call details : {}"+ localItDetailsA.toString());
				if (!localItDetailsA.isEmpty()) {
					if (localItDetailsA.get("EMAIL") != null) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Local IT Contact Email");
						quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetailsA.get("EMAIL").toString());
						crossConnectEnrichmentBean.setSiteAEmail(localItDetailsA.get("EMAIL").toString());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (localItDetailsA.get("CONTACT_NO") != null) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Local IT Contact Number");
						quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetailsA.get("CONTACT_NO").toString());
						crossConnectEnrichmentBean.setSiteANumber(localItDetailsA.get("CONTACT_NO").toString());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (localItDetailsA.get("NAME") != null) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Local IT Contact Name");
						quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetailsA.get("NAME").toString());
						crossConnectEnrichmentBean.setSiteAName(localItDetailsA.get("NAME").toString());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
				}

				Map<String, Object> localItDetailsZ = getLocalItContacts(localItContactZ.get().getAttributeValues());
				if (!localItDetailsZ.isEmpty()) {
					if (localItDetailsZ.get("EMAIL") != null) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Local IT Contact Email");
						quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetailsZ.get("EMAIL").toString());
						crossConnectEnrichmentBean.setSiteZEmail(localItDetailsZ.get("EMAIL").toString());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (localItDetailsZ.get("CONTACT_NO") != null) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Local IT Contact Number");
						quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetailsZ.get("CONTACT_NO").toString());
						crossConnectEnrichmentBean.setSiteZNumber(localItDetailsZ.get("CONTACT_NO").toString());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (localItDetailsZ.get("NAME") != null) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Local IT Contact Name");
						quoteProductComponentsAttributeValueBean.setAttributeValues(localItDetailsZ.get("NAME").toString());
						crossConnectEnrichmentBean.setSiteZName(localItDetailsZ.get("NAME").toString());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
				}
			}

	}

	private List<DemarcationBean> getCrossConnectDemarcation(String locationId) {
		List<DemarcationBean> demarcationBeans=null;
		LOGGER.info("Info for cross connect location {} :"+locationId);
		List<String> list = Arrays.asList(locationId);
		try {
			String request = String.join(",", list);
			String response = (String) mqUtils.sendAndReceive(getCrossConnectLocalITandDemarcation,
					request);
			DemarcationBean[] locationItContactsResult = (DemarcationBean[]) Utils.convertJsonToObject(response, DemarcationBean[].class);
			demarcationBeans = Arrays.asList(Arrays.stream(locationItContactsResult).toArray(DemarcationBean[]::new));
		}catch (Exception ex){
			LOGGER.error("Error in getCrossConnectDemarcation queue call method",ex.getMessage());
		}
		return  demarcationBeans;
	}

	public CrossConnectLocalITDemarcationBean getCrossConnectDemarcationV2(String locationId) {
		CrossConnectLocalITDemarcationBean crossConnectLocalITDemarcationBean = null;
		LOGGER.info("Info for cross connect location {} :" + locationId);
		List<String> list = Arrays.asList(locationId);
		try {
			String request = String.join(",", list);
			String response = (String) mqUtils.sendAndReceive(getCrossConnectLocalITandDemarcation,
					request);
			crossConnectLocalITDemarcationBean = (CrossConnectLocalITDemarcationBean)
					Utils.convertJsonToObject(response, CrossConnectLocalITDemarcationBean.class);

		} catch (Exception ex) {
			LOGGER.error("Error in getCrossConnectDemarcation queue call method", ex.getMessage());
		}
		return crossConnectLocalITDemarcationBean;
	}

	public void getCrossConnectDemarcationDetailsV2(Integer siteId, List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean,
													CrossConnectEnrichmentBean crossConnectEnrichmentBean) {
		CrossConnectLocalITDemarcationBean crossConnectLocalITDemarcationBean = new CrossConnectLocalITDemarcationBean();
		try {
			List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), QuoteConstants.ILLSITES.toString());
			QuoteProductComponent quoteProductComponentVal = components.stream().filter(quoteProductComponent ->
					quoteProductComponent.getMstProductComponent().getName().equalsIgnoreCase(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties())).findFirst().get();

			LOGGER.info("Quote prod comp is ---> {} ", Objects.nonNull(quoteProductComponentVal) ? quoteProductComponentVal.getId() : "null value");

			if (Objects.nonNull(quoteProductComponentVal) && Objects.nonNull(quoteProductComponentVal.getQuoteProductComponentsAttributeValues()) && !quoteProductComponentVal.getQuoteProductComponentsAttributeValues().isEmpty()) {
				LOGGER.info("Inside demrcation id block and value is ----> {} ", Objects.nonNull(quoteProductComponentVal.getQuoteProductComponentsAttributeValues()) ? quoteProductComponentVal.getQuoteProductComponentsAttributeValues() : "null value");
				Optional<QuoteProductComponentsAttributeValue> crossConnectLocalDemarcationId = quoteProductComponentVal.getQuoteProductComponentsAttributeValues().
						stream().filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(CROSS_CONNECT_LOCAL_DEMARCATION_ID)).findFirst();

				LOGGER.info("crossConnectLocalDemarcationId :: {}", crossConnectLocalDemarcationId.get().getAttributeValues());

				crossConnectLocalITDemarcationBean = getCrossConnectDemarcationV2(crossConnectLocalDemarcationId.get().getAttributeValues());

			}
			// Get Site A and Site Z Local IT Contacts
			if (Objects.nonNull(crossConnectLocalITDemarcationBean) && Objects.nonNull(crossConnectLocalITDemarcationBean.getContacts())) {
				CrossConnectLocalITContacts crossConnectLocalITContacts = crossConnectLocalITDemarcationBean.getContacts().stream().findFirst().get();
				if (Objects.nonNull(crossConnectLocalITContacts)) {
					// Set Site A Local IT Contacts
					LocationItContact siteAlocationItContact = crossConnectLocalITContacts.getSiteA().stream().findFirst().get();
					if (Objects.nonNull(siteAlocationItContact.getEmail())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Local IT Contact Email");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteAlocationItContact.getEmail());
						crossConnectEnrichmentBean.setSiteAEmail(siteAlocationItContact.getEmail());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteAlocationItContact.getContactNo())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Local IT Contact Number");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteAlocationItContact.getContactNo());
						crossConnectEnrichmentBean.setSiteANumber(siteAlocationItContact.getContactNo());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteAlocationItContact.getName())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Local IT Contact Name");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteAlocationItContact.getName());
						crossConnectEnrichmentBean.setSiteAName(siteAlocationItContact.getName());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}

					// Set Site Z Local IT Contacts
					LocationItContact siteZlocationItContact = crossConnectLocalITContacts.getSiteZ().stream().findFirst().get();
					if (Objects.nonNull(siteZlocationItContact.getEmail())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Local IT Contact Email");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteZlocationItContact.getEmail());
						crossConnectEnrichmentBean.setSiteZEmail(siteZlocationItContact.getEmail());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteZlocationItContact.getContactNo())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Local IT Contact Number");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteZlocationItContact.getContactNo());
						crossConnectEnrichmentBean.setSiteZNumber(siteZlocationItContact.getContactNo());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteZlocationItContact.getName())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Local IT Contact Name");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteZlocationItContact.getName());
						crossConnectEnrichmentBean.setSiteZName(siteZlocationItContact.getName());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
				}
			}
			if (Objects.nonNull(crossConnectLocalITDemarcationBean) && Objects.nonNull(crossConnectLocalITDemarcationBean.getContacts())) {
				CrossConnectDemarcations crossConnectDemarcations = crossConnectLocalITDemarcationBean.getDemarcations().stream().findFirst().get();
				if (Objects.nonNull(crossConnectDemarcations)) {
					// Set Site A Demarcations
					DemarcationBean siteAdemarcationBean = crossConnectDemarcations.getSiteA().stream().findFirst().get();
					if (Objects.nonNull(siteAdemarcationBean.getBuildingName())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Demarcation Building Name");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteAdemarcationBean.getBuildingName());
						crossConnectEnrichmentBean.setSiteABuilding(siteAdemarcationBean.getBuildingName());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteAdemarcationBean.getFloor())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Demarcation Floor");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteAdemarcationBean.getFloor());
						crossConnectEnrichmentBean.setSiteAFloor(siteAdemarcationBean.getFloor());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteAdemarcationBean.getRoom())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Demarcation Room");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteAdemarcationBean.getRoom());
						crossConnectEnrichmentBean.setSiteARoom(siteAdemarcationBean.getRoom());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteAdemarcationBean.getWing())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-A Demarcation Rack");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteAdemarcationBean.getWing());
						crossConnectEnrichmentBean.setSiteARack(siteAdemarcationBean.getWing());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}

					// Set Site Z Demarcations
					DemarcationBean siteZdemarcationBean = crossConnectDemarcations.getSiteZ().stream().findFirst().get();
					if (Objects.nonNull(siteZdemarcationBean.getBuildingName())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Demarcation Building Name");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteZdemarcationBean.getBuildingName());
						crossConnectEnrichmentBean.setSiteZBuilding(siteZdemarcationBean.getBuildingName());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteZdemarcationBean.getFloor())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Demarcation Floor");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteZdemarcationBean.getFloor());
						crossConnectEnrichmentBean.setSiteZFloor(siteZdemarcationBean.getFloor());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteZdemarcationBean.getRoom())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Demarcation Room");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteZdemarcationBean.getRoom());
						crossConnectEnrichmentBean.setSiteZRoom(siteZdemarcationBean.getRoom());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
					if (Objects.nonNull(siteZdemarcationBean.getWing())) {
						QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
						quoteProductComponentsAttributeValueBean.setName("Site-Z Demarcation Rack");
						quoteProductComponentsAttributeValueBean.setAttributeValues(siteZdemarcationBean.getWing());
						crossConnectEnrichmentBean.setSiteZRack(siteZdemarcationBean.getWing());
						enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
					}
				}
			}

		}
		catch (Exception e){
			LOGGER.error("Error in getting demarc details for MMR cross connect");
		}
		// Get Site A and Site Z Demarcation Details

	}

	/**
	 *
	 * @param quoteIllSite
	 * @param enrichmentAttributesBean
	 */
	public void getCrossConnectDemarcationDetails(QuoteIllSite quoteIllSite, List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean,
												  CrossConnectEnrichmentBean crossConnectEnrichmentBean) {
		if (Objects.nonNull(quoteIllSite.getErfLocSitebLocationId())) {
			List<DemarcationBean> demolist = getCrossConnectDemarcation(quoteIllSite.getErfLocSitebLocationId().toString());
			if (Objects.nonNull(demolist) && !demolist.isEmpty()) {
				demolist.stream().forEach(demarcationBean -> {
					if (demarcationBean.getAendDemarc()) {
						if (Objects.nonNull(demarcationBean.getBuildingName())) {
							QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
							quoteProductComponentsAttributeValueBean.setName("Site-A Demarcation Building Name");
							quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationBean.getBuildingName());
							crossConnectEnrichmentBean.setSiteABuilding(demarcationBean.getBuildingName());
							enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
						}
						if (Objects.nonNull(demarcationBean.getFloor())) {
							QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
							quoteProductComponentsAttributeValueBean.setName("Site-A Demarcation Floor");
							quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationBean.getFloor());
							crossConnectEnrichmentBean.setSiteAFloor(demarcationBean.getFloor());
							enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
						}
						if (Objects.nonNull(demarcationBean.getRoom())) {
							QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
							quoteProductComponentsAttributeValueBean.setName("Site-A Demarcation Room");
							quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationBean.getRoom());
							crossConnectEnrichmentBean.setSiteARoom(demarcationBean.getRoom());
							enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
						}
						if (Objects.nonNull(demarcationBean.getWing())) {
							QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
							quoteProductComponentsAttributeValueBean.setName("Site-A Demarcation Rack");
							quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationBean.getWing());
							crossConnectEnrichmentBean.setSiteARack(demarcationBean.getWing());
							enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
						}
					} else {

						if (Objects.nonNull(demarcationBean.getBuildingName())) {
							QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
							quoteProductComponentsAttributeValueBean.setName("Site-Z Demarcation Building Name");
							quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationBean.getBuildingName());
							crossConnectEnrichmentBean.setSiteZBuilding(demarcationBean.getBuildingName());
							enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
						}
						if (Objects.nonNull(demarcationBean.getFloor())) {
							QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
							quoteProductComponentsAttributeValueBean.setName("Site-Z Demarcation Floor");
							quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationBean.getFloor());
							crossConnectEnrichmentBean.setSiteZFloor(demarcationBean.getFloor());
							enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
						}
						if (Objects.nonNull(demarcationBean.getRoom())) {
							QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
							quoteProductComponentsAttributeValueBean.setName("Site-Z Demarcation Room");
							quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationBean.getRoom());
							crossConnectEnrichmentBean.setSiteZRoom(demarcationBean.getRoom());
							enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
						}
						if (Objects.nonNull(demarcationBean.getWing())) {
							QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean = new QuoteProductComponentsAttributeValueBean();
							quoteProductComponentsAttributeValueBean.setName("Site-Z Demarcation Rack");
							quoteProductComponentsAttributeValueBean.setAttributeValues(demarcationBean.getWing());
							crossConnectEnrichmentBean.setSiteZRack(demarcationBean.getWing());
							enrichmentAttributesBean.add(quoteProductComponentsAttributeValueBean);
						}
					}

				});

			}
		}
	}

	public CommonValidationResponse processValidate(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		commonValidationResponse.setStatus(true);
		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			NplQuoteBean quoteDetail = getQuoteDetails(quoteId, null, false);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			Map<String, Object> variable = nplQuotePdfService.getCofAttributes(true, true, quoteDetail,quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
					|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for NPL is {}", Utils.convertObjectToJson(variable));
				commonValidationResponse = nplCofValidatorService.processCofValidation(variable,
						"NPL", quoteToLe.get().getQuoteType());
				String crossConnect=(String)variable.get("crossConnect");
				if(Objects.nonNull(crossConnect) && crossConnect.equals("mmr")) {
					commonValidationResponse = nplCofValidatorService.processMMRCofValidation(variable);
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
	 * Method to get the details from service inventory
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SICustomerDetailDataBean> getCustomerDetailNde(String customerId)throws TclCommonException
	{
		LOGGER.info("entered into getCustomerDetailNde :: {}", customerId);
		List<SICustomerDetailDataBean> serviceDetailDataList = new ArrayList<>();
		if (StringUtils.isBlank(customerId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<CustomerLeBean> cuLeIds=new ArrayList<CustomerLeBean>();
		Set<Integer> customerLeIds;
		String response;
		try {
			LOGGER.debug("MDC Filter token value in before Queue call get Customer le ids by customer id {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			response = (String) mqUtils.sendAndReceive(customerLeByCustomerIdMQ, customerId);
			LOGGER.info("RESPONSE FROM QUEUE :: {}", response);
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_MQ_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
		if (Objects.nonNull(response)) {
			CustomerLeBean[] lgalEntityList = (CustomerLeBean[]) Utils.convertJsonToObject(response,
					CustomerLeBean[].class);
			 cuLeIds= Arrays.asList(lgalEntityList);
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_MQ_EMPTY,
					ResponseResource.R_CODE_ERROR);
		}
		if (!cuLeIds.isEmpty()) {
			cuLeIds.stream().forEach(CustomerLeBean -> {

				List<SICustomerInfoBean> customerDetailsList = new ArrayList<>();
				LOGGER.info("Inside getServiceDetail to fetch NdeCustomerDetailsBean for customer le id {} ", CustomerLeBean.getSfdcId());

				String orderDetailsQueueResponse;
				try {
					orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siNdeCustomerDetailsQueue,
							CustomerLeBean.getSfdcId());

					LOGGER.info("Response received in OMS getServiceDetailNDE: {} ", orderDetailsQueueResponse);

					SICustomerInfoBean[] serviceDetailBeanArray = (SICustomerInfoBean[]) Utils
							.convertJsonToObject(orderDetailsQueueResponse, SICustomerInfoBean[].class);
					customerDetailsList = Arrays.asList(serviceDetailBeanArray);

				} catch (TclCommonException e) {
					LOGGER.info("exception in view inventory fetching ehs data" + e);
				}

				if (!customerDetailsList.isEmpty()) {
					LOGGER.info("customerDetailsList :: {}", customerDetailsList.toString());
					LOGGER.info("serviceDetailDataList :: {}", serviceDetailDataList.toString());
					mapServiceDetailBeanNDE(customerDetailsList, serviceDetailDataList);
				}

			});
		}
		return serviceDetailDataList;
	}
	
	/**
	 * method to map service details bean
	 * @param siCustomerDetailList
	 * @param customerDetailDataList
	 */
	private void mapServiceDetailBeanNDE(List<SICustomerInfoBean> siCustomerDetailList, List<SICustomerDetailDataBean> customerDetailDataList)
	{
		LOGGER.info("Inside mapServiceDetailBean to construct SIServiceDetailDataBean for serviceid {} ");
		siCustomerDetailList.stream().forEach(siServiceDetail -> {
			SICustomerDetailDataBean serviceDetail = new SICustomerDetailDataBean();
			serviceDetail.setId(siServiceDetail.getId());
			serviceDetail.setCuId(siServiceDetail.getCuId());
			serviceDetail.setEhsLocId(siServiceDetail.getEhsLocId());
			serviceDetail.setEhsAddress(siServiceDetail.getEhsAddress());
			serviceDetail.setEhsId(siServiceDetail.getEhsId());
			customerDetailDataList.add(serviceDetail);
		});
		
	}
	
	/*
	 * constructNplLinkBean - method to construct NplLinkBean for NDE
	 * 
	 * @param link
	 * 
	 * @param version
	 * 
	 * @return NplLinkBean
	 */
	private NplLinkBean constructNdeLinkBean(QuoteNplLink link, Boolean isFeasibleSites, Boolean isSiteProp) {
		if (isSiteProp == null) {
			isSiteProp = false;
		}
		NplLinkBean linkDto = new NplLinkBean(link);
		Map<String, QuoteIllSite> siteMap = new HashMap<>();

		if (link.getStatus() == 1) {
			List<QuoteProductComponentBean> linkComp = getSortedComponents(
					constructQuoteProductComponentNde(link.getId(), false, "Link", isSiteProp));
			linkDto.setComponents(linkComp);

			List<QuoteProductComponentBean> siteAComp = getSortedComponents(
					constructQuoteProductComponentNde(link.getSiteAId(), false, "Site-A", isSiteProp));
			linkDto.getComponents().addAll(siteAComp);

			List<QuoteProductComponentBean> siteBComp = getSortedComponents(
					constructQuoteProductComponentNde(link.getSiteBId(), false, "Site-B", isSiteProp));
			linkDto.getComponents().addAll(siteBComp);
		}

		siteMap.put(link.getSiteAId() + "_" + link.getSiteAType(), illSiteRepository.findById(link.getSiteAId()).get());
		siteMap.put(link.getSiteBId() + "_" + link.getSiteBType(), illSiteRepository.findById(link.getSiteBId()).get());
		linkDto.setSites(getSortedNplSiteBeans(constructNplSiteBeans(siteMap, isFeasibleSites)));

		linkDto.setLinkFeasibility(constructLinkFeasibility(link));
		linkDto.setQuoteSla(constructNplSlaDetails(link));
		// Added for NPL MF
        if(link.getMfTaskTriggered()!=null) {
        linkDto.setMfTaskTriggered(link.getMfTaskTriggered());
        }
        else {
            linkDto.setMfTaskTriggered(0);

        }
		linkDto.setMfStatus(link.getMfStatus());

		linkDto.setIsTaskTriggered(link.getIsTaskTriggered());
		
		//add rejection flag
		if (link.getCommercialRejectionStatus() != null) {
			if (link.getCommercialRejectionStatus().equalsIgnoreCase("1")) {
				linkDto.setRejectionStatus(true);
			} else {
				linkDto.setRejectionStatus(false);
			}
		}
		//approve flag
		if (link.getCommercialApproveStatus() != null) {
			if (link.getCommercialApproveStatus().equalsIgnoreCase("1")) {
				linkDto.setApproveStatus(true);
			} else {
				linkDto.setApproveStatus(false);
			}
		}
		if(linkDto.getRejectionStatus() || linkDto.getApproveStatus()) {
			linkDto.setIsActionTaken(true);
		}
		
		//added for bulk link commercial upload
				if (link.getLinkBulkUpdate() != null) {
					if (link.getLinkBulkUpdate().equalsIgnoreCase("1")) {
						linkDto.setBulkuploadStatus(true);
					} else {
						linkDto.setBulkuploadStatus(false);
					}
				}
				
		
		Optional<QuoteIllSite> siteAOpt = illSiteRepository.findById(link.getSiteAId());
		if(siteAOpt.isPresent()) {
			QuoteToLe quoteToLe = siteAOpt.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
				|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()))
		{
			
			linkDto
					.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe, link)));
		}
		
		// Termination specific data
		if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Getting termination related details for quoteToLe Id {}", quoteToLe.getId());
			List<QuoteSiteServiceTerminationDetailsBean> terminationSiteServiceBeanList = new ArrayList<>();
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(link.getId());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				quoteIllSiteToServiceList.stream().forEach(siteToService ->{
					LOGGER.info("siteToService serviceId {}", siteToService.getErfServiceInventoryTpsServiceId());
					QuoteSiteServiceTerminationDetails terminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);
					QuoteSiteServiceTerminationDetailsBean terminationSiteServiceBean = new QuoteSiteServiceTerminationDetailsBean();
					BeanUtils.copyProperties(terminationDetail, terminationSiteServiceBean);
					terminationSiteServiceBean.setQuoteIllSiteToServiceId(siteToService.getId());
					//terminationSiteServiceBean.setQuoteSiteId(siteToService.getQuoteIllSite().getId());
					terminationSiteServiceBean.setQuoteLinkId(siteToService.getQuoteNplLink().getId());
					terminationSiteServiceBean.setServiceId(siteToService.getErfServiceInventoryTpsServiceId());
					terminationSiteServiceBean.setServiceLinkType(siteToService.getType());
					LOGGER.info("terminationSiteServiceBean {}", terminationSiteServiceBean.toString());
					terminationSiteServiceBeanList.add(terminationSiteServiceBean);
				});
			}
			
			linkDto.setQuoteSiteServiceTerminationsBean(terminationSiteServiceBeanList);
			
		}
		
		}
		//added for nde mc
		LOGGER.info("LINK ID FOR NDE MC"+link.getId());
		Optional<QuoteNplLink> nplLink=nplLinkRepository.findById(link.getId());
		if (nplLink.isPresent()) {
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(nplLink.get().getSiteAId());
			if (quoteIllSite.isPresent()
					&& quoteIllSite.get().getNplShiftSiteFlag().equals(CommonConstants.ACTIVE)) {
				linkDto.setSiteShiftFlag(MACDConstants.SITEA);

			} else {
				Optional<QuoteIllSite> quoteIllSiteSiteB = illSiteRepository
						.findById(nplLink.get().getSiteBId());
				if (quoteIllSiteSiteB.isPresent()
						&& quoteIllSiteSiteB.get().getNplShiftSiteFlag().equals(CommonConstants.ACTIVE)) {
					linkDto.setSiteShiftFlag(MACDConstants.SITEB);
				}
			}
			LOGGER.info("QUOTE ID FOR NDE MC"+nplLink.get().getQuoteId());
			/*
			 * List<QuoteToLe>
			 * quoteToLe=quoteToLeRepository.findByQuote_Id(nplLink.get().getQuoteId());
			 * List<QuoteIllSiteToService> quoteSiteToServiceLink =
			 * quoteIllSiteToServiceRepository
			 * .findByQuoteNplLink_IdAndQuoteToLe(nplLink.get().getId(), quoteToLe.get(0));
			 * linkDto.setServiceId(quoteSiteToServiceLink.get(0).
			 * getErfServiceInventoryTpsServiceId());
			 */

			LOGGER.info("Site Shifted {}", linkDto.getSiteShiftFlag()+"linkid"+link.getId());
			LOGGER.info("link service id", linkDto.getServiceId());

		}
		return linkDto;
	}
	
	/**
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent for NDE
	 * @param id,version
	 */
	private List<QuoteProductComponentBean> constructQuoteProductComponentNde(Integer id, boolean isSitePropertiesNeeded,
			String type, Boolean isSiteProp) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenOnVersionNDE(id, isSitePropertiesNeeded, type,
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
	 * @param isSitePropertiesNeeded NDE
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */

	private List<QuoteProductComponent> getComponentBasenOnVersionNDE(Integer id, boolean isSitePropertiesNeeded,
			String type, Boolean isSiteProp) {
		List<QuoteProductComponent> components = null;
		MstProductFamily prodFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.NDE,
				CommonConstants.BACTIVE);
		if (isSitePropertiesNeeded) {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(id,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.NPL_LINK.toString());
		} else if (isSiteProp) {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductFamilyAndType(id, prodFamily,
					type);
			components.addAll(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(id,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.NPL_LINK.toString()));
		} else {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductFamilyAndType(id, prodFamily,
					type);
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
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String,String> getSiteDetailForNplBasedOnLinkId(String linkId) throws TclCommonException {
		if(linkId.isEmpty()) {
			
			throw new TclCommonException(ExceptionConstants.INVALID_LINKID, ResponseResource.R_CODE_NOT_FOUND);
		}
		Map<String,String> map = new HashMap<>();
		Integer lid=Integer.parseInt(linkId);
		QuoteNplLink siteDetail = Optional.ofNullable(lid).flatMap(nplLinkRepository::findById).orElseThrow(
				() -> new TclCommonRuntimeException(ExceptionConstants.LINKID_NOT_FOUND, ResponseResource.R_CODE_ERROR));
		map.put("siteAId", siteDetail.getSiteAId().toString());
		map.put("siteBId", siteDetail.getSiteBId().toString());
		LOGGER.info("map::{}"+map);
		return map;

	}

	public NplQuoteBean updateLinkMc(NplMcQuoteDetailBean quoteDetail, Integer erfCustomerId, Integer quoteId)
			throws TclCommonException {
		NplQuoteBean bean=new NplQuoteBean();
		LOGGER.info("INSIDE Nplquoteservice updateLinkMc");
		return bean;
	}
	
	
	//added for mc nde
	/**
	 * This method is used for validating the site information for mc
	 * validateSiteInformation
	 * 
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	protected void validateSiteInformationMc(NplMcQuoteDetailBean quoteDetail, Integer quoteId) throws TclCommonException {
		if ((quoteDetail == null) || quoteDetail.getLinks() == null || quoteDetail.getLinkCount() == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
		if (Objects.isNull(quoteId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	protected Integer processSiteDetailMc(User user, MstProductFamily productFamily,
			QuoteToLeProductFamily quoteToLeProductFamily, NplSiteDetail site, String productOfferingName,
			ProductSolution productSolution, Quote quote, boolean siteChanged) throws TclCommonException {
		Integer siteId = null;

		try {
			siteId = constructIllSitesMc(productSolution, user, site, productFamily, productOfferingName, siteChanged);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siteId;
	}
	
	private Integer constructIllSitesMc(ProductSolution productSolution, User user, NplSiteDetail siteDetail,
			MstProductFamily productFamily, String productOfferingName, boolean siteChanged) throws TclCommonException {

		Integer siteId = null;
		SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);

		
		if(siteDetail!=null) {
			if (siteDetail.getSiteId() == null) {
				QuoteIllSite illSite = new QuoteIllSite();
				illSite.setErfLocSiteaLocationId(siteDetail.getSecondLocationId());
				illSite.setErfLocSiteaSiteCode(siteDetail.getSecondLocationCode());
				illSite.setErfLocSitebLocationId(siteDetail.getLocationId());
				illSite.setErfLocSitebSiteCode(siteDetail.getLocationCode());
				illSite.setProductSolution(productSolution);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 60); // Adding 60 days
				illSite.setEffectiveDate(cal.getTime());
				illSite.setCreatedBy(user.getId());
				illSite.setCreatedTime(new Date());
				illSite.setStatus((byte) 1);
				illSite.setImageUrl(soDetail.getImage());
				illSite.setSiteCode(Utils.generateUid());
				illSite.setFeasibility((byte) 0);
				int shiftSite = (siteChanged) ? 1 : 0;
				illSite.setNplShiftSiteFlag(shiftSite);
				illSite = illSiteRepository.save(illSite);
				// siteDetail.setSiteId(illSite.getId());
				siteId = illSite.getId();
				LOGGER.info("Npl shift site: " +illSite.getNplShiftSiteFlag());

//				}

			} else {
				QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(siteDetail.getSiteId(), (byte) 1);
				if (illSiteEntity != null) {
					illSiteEntity.setProductSolution(productSolution);
					illSiteRepository.save(illSiteEntity);
					siteId = illSiteEntity.getId();

				}
			}
		}

		return siteId;
	}
	
	/**
	 * @author  construct Npl links mc
	 * @param siteAId
	 * @param siteBId
	 * @param productSolution
	 * @throws TclCommonException
	 */
	protected void constructNplLinksMacdMc(User user, MstProductFamily productFamily, Integer quoteId, Integer siteAId,
			Integer siteBId, ProductSolution productSolution, final String siteAType, final String siteBType,
			String productOfferingName, Integer locA, Integer locB, String serviceId,String isbandwith,String isshiftsite, List<ComponentDetail> components) throws TclCommonException {
		QuoteNplLink link = null;
		Optional<QuoteNplLink> optLink = nplLinkRepository.findByProductSolutionIdAndSiteAIdAndSiteBIdAndStatus(
				productSolution.getId(), siteAId, siteBId, (byte) 1);

		if (optLink.isPresent()) {
			link = optLink.get();
			removeCompAndAttrForLink(link);
		}

		else {
			link = new QuoteNplLink();
			link.setSiteAId(siteAId);
			link.setSiteBId(siteBId);
			link.setProductSolutionId(productSolution.getId());
			link.setCreatedDate(new Date());
			link.setStatus((byte) 1);
			link.setQuoteId(quoteId);
			link.setCreatedBy(user.getId());
			link.setWorkflowStatus(null);
			link.setLinkCode(Utils.generateUid());
			link.setSiteAType(siteAType);
			link.setSiteBType(siteBType);
			link.setIsTaskTriggered(0);
			// Implemented as in ILL
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 60); // Adding 60 days
			link.setEffectiveDate(cal.getTime());
			link = nplLinkRepository.save(link);
			LOGGER.info("Link Id: "+ link.getId());
			link.setQuoteNplLinkSlas(constuctNplLinkSla(link, productOfferingName));
		}

		LOGGER.info("Service Id before storing link id in QuoteIllSiteService: "+serviceId);
		if (StringUtils.isNotEmpty(serviceId) && StringUtils.isNotBlank(serviceId))
		{
			LOGGER.info("Service Id after storing link id in QuoteIllSiteService: "+serviceId);
			LOGGER.info("Shift site and change bandwidth flag"+"CB:"+isbandwith+"SS:"+isshiftsite);
			QuoteToLe quoteToLe = productSolution.getQuoteToLeProductFamily().getQuoteToLe();
			if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) || MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {

				List<QuoteIllSiteToService> quoteSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);

				for(QuoteIllSiteToService quoteIllSiteToService:quoteSiteToService) {
				
				if (quoteSiteToService != null && !quoteSiteToService.isEmpty() && quoteIllSiteToService.getQuoteNplLink() == null) {
					quoteIllSiteToService.setQuoteNplLink(link);
					//ADDED for nde mc
					if(isbandwith!=null) {
						quoteIllSiteToService.setBandwidthChanged((isbandwith != null&& isbandwith.equalsIgnoreCase("true"))? CommonConstants.BACTIVE: CommonConstants.BDEACTIVATE);
					}
					if(isshiftsite!=null) {
						quoteIllSiteToService.setSiteShifted((isshiftsite != null&& isshiftsite.equalsIgnoreCase("true"))? CommonConstants.BACTIVE: CommonConstants.BDEACTIVATE);
					}
					quoteIllSiteToServiceRepository.save(quoteIllSiteToService);
					LOGGER.info("Saved NPL link to QuoteIllSiteToService"+quoteIllSiteToService.getId());

				}
			}
			}
		}

		if (components.size() != 0) {
			for (ComponentDetail componentDetail : components) {
				processProductComponent(productFamily, link, componentDetail, user, locA, locB);
			}
		}
	}

	/**
	 * Method upload the Multipart file into table
	 * 
	 * @param siteId
	 * @param siteDocument
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public String uploadDocument(Integer linkId, MultipartFile siteDocument) throws TclCommonException {
		String uploadStatus = CommonConstants.FAILIURE;
		try {
			LOGGER.info("Inside NPLQuoteService.uploadDocument method to upload site document for linkId {} ", linkId);
			
			List<LinkFeasibility> linkFeasibility = linkFeasibilityRepository
					.findByQuoteNplLink_Id(linkId);
			
			if (linkFeasibility != null && !linkFeasibility.isEmpty()) {
				LinkFeasibility sitef = linkFeasibility.get(0);
				sitef.setSiteDocument(siteDocument.getBytes());
				sitef.setSiteDocumentName(siteDocument.getOriginalFilename());
				linkFeasibilityRepository.save(sitef);
				uploadStatus = CommonConstants.SUCCESS;
				LOGGER.info("Uploaded solution diagram document for linkId {} ", linkId);
			} else {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return uploadStatus;

	}
	
	/**
	 * method to get site documents names for given quoteToLe
	 * 
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	public List<SiteDocumentBean> getSiteDocumentDetails(Integer quoteId) throws TclCommonException {
		List<SiteDocumentBean> siteDocNames = new ArrayList<>();
		try {
			LOGGER.info(
					"Inside IllQuoteService.getSiteDocumentDetails method to get site document names for quoteId {} ",
					quoteId);
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
			if (quoteToLes.stream().findFirst().isPresent()) {
				QuoteToLe quoteToLe = quoteToLes.stream().findFirst().get();
				List<QuoteToLeProductFamily> quoteLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe(quoteToLe.getId());
				if (!(quoteLeProductFamily.isEmpty())){
					quoteLeProductFamily.stream().forEach(quotLeProdFamName->
							{
					List<ProductSolution> productSolutions = productSolutionRepository
							.findByQuoteToLeProductFamily(quotLeProdFamName);
					productSolutions.stream().forEach(prodSolution -> {
					
					
						List<QuoteNplLink> quoteNplLinks = nplLinkRepository.findByProductSolutionIdAndStatus(prodSolution.getId(),
								CommonConstants.BACTIVE);
						quoteNplLinks.stream().forEach(nplLink -> {

							List<LinkFeasibility> linkFeasibility = linkFeasibilityRepository
									.findByQuoteNplLink_Id(nplLink.getId());
							
							linkFeasibility.stream().forEach(sitef -> {
								if (sitef.getSiteDocumentName() != null) {
									SiteDocumentBean siteDocumentBean = new SiteDocumentBean();
									siteDocumentBean.setSiteId(sitef.getQuoteNplLink().getId());
									siteDocumentBean.setSiteDocumentName(sitef.getSiteDocumentName());
									siteDocNames.add(siteDocumentBean);
								}
							});

						});
					});
							});
				}
				LOGGER.info("Fetched site document names for the quoteToLe {} ", quoteToLe);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return siteDocNames;

	}
	
	public OpportunityBean getOpportunityDetails(Integer quoteId, Integer linkId) throws TclCommonException {
		LOGGER.info("Inside NPLQuoteService.getOpportunityDetails to fetch opportunity details for the quoteId {} ",
				quoteId);
		OpportunityBean opporBean = new OpportunityBean();
		try {
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			QuoteToLe quoteToLe = optionalQuoteToLe.get();
			opporBean.setProductName(quoteToLe.getQuoteToLeProductFamilies().stream().map(fam->fam.getMstProductFamily().getName()).findFirst().get());
			if( Objects.nonNull(quoteToLe.getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				if( Objects.nonNull(quoteToLe.getIsAmended()) && quoteToLe.getIsAmended()!=1){
					List<String> serviceIds=macdUtils.getServiceIds(quoteToLe);
					String serviceIdList=serviceIds.stream().findFirst().get();
					if(Objects.nonNull(quoteToLe.getIsMultiCircuit())&&CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
						serviceIds.remove(serviceIdList);
						serviceIds.forEach(serviceId -> {
							serviceIdList.concat("," + serviceId);
						});
					}
					opporBean.setServiceId(serviceIdList);
				}
				}
		//	opporBean.setServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());

			List<QuoteIllSiteToService> quoteIllSiteToService=quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(linkId, quoteToLe);
            if(!CollectionUtils.isEmpty(quoteIllSiteToService)){
			opporBean.setServiceId(quoteIllSiteToService.get(0).getErfServiceInventoryTpsServiceId());
            }
			opporBean.setOpportunityStage(SFDCConstants.PROPOSAL_SENT);
			opporBean.setOpportunityAccountName(quote.getCustomer().getCustomerName());
			Optional<User> user = userRepository.findById(quote.getCreatedBy());
			opporBean.setOpportunityOwnerEmail(user.get().getEmailId());
			opporBean.setOpportunityOwnerName(user.get().getUsername());
			
			/*List<SiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository
					.findByQuoteIllSite_IdAndType(siteId, "primary"); */
			
			List<LinkFeasibility> linkFeasibility = linkFeasibilityRepository
					.findByQuoteNplLink_Id(linkId);
			
			Optional<LinkFeasibility> linkFeasibilityOpt = linkFeasibility.stream().findFirst();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(linkFeasibilityOpt.get().getResponseJson());
			//opporBean.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.CUSTOMER_SEGMENT));
			
			if(jsonObj.get(ManualFeasibilityConstants.A_CUSTOMER_SEGMENT)!=null) {
				opporBean.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.A_CUSTOMER_SEGMENT));
				}else if(jsonObj.get(ManualFeasibilityConstants.B_CUSTOMER_SEGMENT)!=null){
					opporBean.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.B_CUSTOMER_SEGMENT));
				}
			
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(linkId, QuoteConstants.NPL_LINK.toString()).stream()
					.filter(comp -> comp.getMstProductComponent().getName().equalsIgnoreCase("SITE_PROPERTIES"))
					.findFirst().orElse(null);

			
			if (quoteProductComponent != null) {
				LOGGER.info("Inside NPLQuoteService.getOpportunityDetails to fetch site properties for the linkId {} ",
						linkId);
				Map<String, String> map = quoteProductComponent.getQuoteProductComponentsAttributeValues().stream()
						.filter(k -> k.getAttributeValues() != null)
						.collect(Collectors.toMap(k -> k.getProductAttributeMaster().getName(),
								QuoteProductComponentsAttributeValue::getAttributeValues));

				opporBean.setSiteContactName(map.getOrDefault("LCON_NAME", null));
				opporBean.setSiteLocalContactNumber(map.getOrDefault("LCON_CONTACT_NUMBER", null));
				if (map.containsKey("LCON_REMARKS"))
					opporBean.setSalesRemarks(map.getOrDefault("LCON_REMARKS", null));
			}
			String response = thirdPartyServiceJobsRepository
					.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(
							quote.getQuoteCode(), SfdcServiceTypeConstants.UPDATE_OPPORTUNITY,
							ThirdPartySource.SFDC.toString(), "SUCCESS")
					.stream().findFirst().map(ThirdPartyServiceJob::getResponsePayload).orElse(StringUtils.EMPTY);

			if (response != null && !response.isEmpty()) {
				LOGGER.info("Inside NPLQuoteService.getOpportunityDetails to fetch opportunity stage");
				ThirdPartyResponseBean thirdPartyResponse = (ThirdPartyResponseBean) Utils.convertJsonToObject(response,
						ThirdPartyResponseBean.class);
				opporBean.setOpportunityStage(thirdPartyResponse.getOpportunity().getStageName());
			}

			opporBean.setOppotunityId(quoteToLe.getTpsSfdcOptyId());

			List<QuoteLeAttributeValue> quoteLeAttributeValueLegalEntity = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.LE_NAME.toString());
			if (quoteLeAttributeValueLegalEntity != null && !quoteLeAttributeValueLegalEntity.isEmpty())
				opporBean.setOpportunityOwnerName(quoteLeAttributeValueLegalEntity.get(0).getAttributeValue());

			List<QuoteLeAttributeValue> quoteLeAttributeValueLeOwnerEmail = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.LE_EMAIL.toString());
			if (quoteLeAttributeValueLeOwnerEmail != null && !quoteLeAttributeValueLeOwnerEmail.isEmpty())
				opporBean.setOpportunityOwnerEmail(quoteLeAttributeValueLeOwnerEmail.get(0).getAttributeValue());
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}

		return opporBean;
	}
	
	
	//added for npl cb
	
		public QuoteDetail updateSitePropertiesAttributes(UpdateRequest request) throws TclCommonException {
			LOGGER.info("inside updateSitePropertiesAttributes"+request.getLinkId());
			QuoteDetail detail = null;
			try {
				validateUpdateRequest(request);
				detail = new QuoteDetail();
				QuoteNplLink link = nplLinkRepository.findByIdAndStatus(request.getLinkId(), (byte) 1);
				if (link == null) {
					throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);

				}
				MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
						(byte) 1);

				if (mstProductFamily == null) {
					throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

				}

				User user = getUserId(Utils.getSource());
				MstProductComponent mstProductComponent = getMstProperties(user);
				constructNplSitePropeties(mstProductComponent, link, user.getUsername(), request, mstProductFamily,"primary");

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}

			return detail;
		}
		
		
		/**
	     * construct sites
	     *
	     * @param mstProductComponent
	     * @param orderIllSite
	     * @param username
	     * @param request
	     * @param mstProductFamily
	     */
	    private void constructNplSitePropeties(MstProductComponent mstProductComponent, QuoteNplLink link,
	                                           String username, UpdateRequest request, MstProductFamily mstProductFamily, String type) {
	        List<QuoteProductComponent> orderProductComponents = quoteProductComponentRepository
	                .findByReferenceIdAndMstProductComponentAndMstProductFamily(link.getId(), mstProductComponent,mstProductFamily);
	        if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
	            updateIllSiteProperties(orderProductComponents, request, username);
	        } else {
	        	createIllLinkAttribute(mstProductComponent, mstProductFamily, link, request, username, type);
	        }

	    }
	    
	    
	    /**
	     * create site attributes
	     *
	     * @param mstProductComponent
	     * @param mstProductFamily
	     * @param orderIllSite
	     * @param request
	     * @param username
	     */
	    private void createIllLinkAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
	    		QuoteNplLink link, UpdateRequest request, String username, String type) {
	        QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
	        quoteProductComponent.setMstProductComponent(mstProductComponent);
	        quoteProductComponent.setReferenceId(link.getId());
	        quoteProductComponent.setReferenceName(QuoteConstants.NPL_LINK.toString());
	        quoteProductComponent.setMstProductFamily(mstProductFamily);
	        if(Objects.nonNull(type)){
				quoteProductComponent.setType(type);
			}
	        quoteProductComponentRepository.save(quoteProductComponent);

	        if (request.getAttributeDetails() != null) {
	            for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
	                createSitePropertiesAttribute(quoteProductComponent, attributeDetail, username);

	            }

	        }
	    }
	    
	    /**
		 * updateRejectionSiteSatus - site level
		 * 
		 * @throws TclCommonException
		 */
		public void updateSiteRejectionSatus(CommercialRejectionBean commercialRejectionBean) throws TclCommonException {
			try {
				LOGGER.info("updateSiteRejectionSatus LINKid:siterejectiostatus"+commercialRejectionBean.getLinkId()+":"+commercialRejectionBean.getSiteRejectionStatus());
				if (commercialRejectionBean.getLinkId()!=null && commercialRejectionBean.getSiteRejectionStatus()!=null) {
					Optional<QuoteNplLink> link = linkRepository.findById(Integer.parseInt(commercialRejectionBean.getLinkId()));
					if (link.isPresent()) {
						if(commercialRejectionBean.getSiteRejectionStatus()) {
							link.get().setCommercialRejectionStatus("1");
							link.get().setCommercialApproveStatus("0");
						}
						else {
							link.get().setCommercialRejectionStatus("0");
						}
						linkRepository.save(link.get());
						
						//audit commercial 
						CommercialQuoteAudit audit=new CommercialQuoteAudit();
						User user = getUserId(Utils.getSource());
						audit.setCommercialAction("Link_Reject");
						audit.setQuoteId(Integer.parseInt(commercialRejectionBean.getQuoteId()));
						audit.setSiteId(commercialRejectionBean.getLinkId().toString());
						audit.setCreatedTime(new Date());
						audit.setCreatedBy(user.getUsername());
						commercialQuoteAuditRepository.save(audit);
						
				     } 
				     else {
				    	 throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
				     	}
				}
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			
		}
		
		/**
		 * updateRejectionSiteSatus - quote level
		 * 
		 * @throws TclCommonException
		 */
		public void updateQuoteRejectionSatus(CommercialRejectionBean commercialRejectionBean) throws TclCommonException {
			try {
				LOGGER.info("updatequoteRejectionSatus quoteeid:quoterejectioncomment and quoterejectionstatus"
						+ commercialRejectionBean.getQuoteId() + ":" + commercialRejectionBean.getQuoteRejectionComments()
						+ ":" + commercialRejectionBean.getQuoteRejectionComments());

				if (commercialRejectionBean.getQuoteId() != null
						&& commercialRejectionBean.getQuoteRejectionComments() != null
						&& commercialRejectionBean.getQuoteRejectionStatus()) {
					Optional<QuoteToLe> quotele = quoteToLeRepository
							.findByQuote_Id(Integer.parseInt(commercialRejectionBean.getQuoteId())).stream().findFirst();
					
					if (quotele.isPresent()) {
						quotele.get().setQuoteRejectionComment(commercialRejectionBean.getQuoteRejectionComments());
						if (commercialRejectionBean.getQuoteRejectionStatus()) {
							quotele.get().setCommercialQuoteRejectionStatus("1");
						} else {
							quotele.get().setCommercialQuoteRejectionStatus("0");
						}

						quoteToLeRepository.save(quotele.get());
						try {

							Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository
									.findByQuote_Id(Integer.parseInt(commercialRejectionBean.getQuoteId())).stream()
									.findFirst();
							if (quoteToLeOpt.isPresent()) {
								List<Integer> links = commercialRejectionBean.getLinks();
								links.forEach(linkId -> {
									QuoteNplLink link = linkRepository.findByIdAndStatus(linkId, (byte) 1);
									link.setCommercialRejectionStatus("1");
									link.setCommercialApproveStatus("0");
									linkRepository.save(link);

								});

								CommercialQuoteAudit audit = new CommercialQuoteAudit();
								User user = getUserId(Utils.getSource());
								audit.setCommercialAction("Quote_Reject");
								audit.setQuoteId(Integer.parseInt(commercialRejectionBean.getQuoteId()));
								audit.setSiteId(commercialRejectionBean.getLinks().toString());
								audit.setCreatedTime(new Date());
								audit.setCreatedBy(user.getUsername());
								commercialQuoteAuditRepository.save(audit);

							}

						} catch (Exception e) {
							throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
						}
					} else {
						throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
					}
				}
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}

		}
		
		/**
		 * 
		 * @param siteAttributeUpdateBean
		 * @param quoteToLeId
		 */
		public void updateSiteAttributesIsv(SiteAttributeUpdateBean siteAttributeUpdateBean, Integer quoteToLeId,Integer linkid) throws TclCommonException {
			LOGGER.info("Entered into updateSiteAttributesIsv"+linkid);
		try {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
						.findByQuoteToLe(quoteToLe.get().getId());
				if (quoteToLeProductFamilies != null && !quoteToLeProductFamilies.isEmpty()) {
					quoteToLeProductFamilies.forEach(productFamily -> {
						MstProductComponent mstProductComponents = mstProductComponentRepository
								.findByName("SITE_PROPERTIES");
						List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
								.findByReferenceIdAndMstProductComponentAndMstProductFamily(
										linkid, mstProductComponents,
										productFamily.getMstProductFamily());
						if (mstProductComponents != null) {
							LOGGER.info("-------Got Site properities product component--------");
							siteAttributeUpdateBean.getAttributeDetails().stream().forEach(attributeBean -> {
								LOGGER.info("-------Attribute Name--------{} ", attributeBean.getName());
								if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
									LOGGER.info("-------Got quoteProductComponents component--------");
									ProductAttributeMaster productAttributeMaster = productAttributeMasterRepository
											.findByName(attributeBean.getName());
									//if attribute is empty need to add new attribute
									if(productAttributeMaster==null) {
										User user = getUserId(Utils.getSource());
										productAttributeMaster = new ProductAttributeMaster();
										productAttributeMaster.setCreatedBy(user.getUsername());
										productAttributeMaster.setCreatedTime(new Date());
										productAttributeMaster.setDescription(attributeBean.getName());
										productAttributeMaster.setName(attributeBean.getName());
										productAttributeMaster.setStatus((byte) 1);
										productAttributeMaster=productAttributeMasterRepository.save(productAttributeMaster);
									}
									List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
											.findByQuoteProductComponentAndProductAttributeMaster(
													quoteProductComponents.get(0), productAttributeMaster);
									if (quoteProductComponentsAttributeValues.isEmpty()) {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
										if(attributeBean.getValue()!=null && attributeBean.getValue().length() >255) {
											AdditionalServiceParams additionalServiceParams=new AdditionalServiceParams();
											additionalServiceParams.setAttribute(attributeBean.getName());
											additionalServiceParams.setCategory("QUOTE");
											additionalServiceParams.setCreatedBy(Utils.getSource());
											additionalServiceParams.setCreatedTime(new Date());
											additionalServiceParams.setIsActive(CommonConstants.Y);
											additionalServiceParams.setValue(attributeBean.getValue());
											additionalServiceParamRepository.save(additionalServiceParams);
											quoteProductComponentsAttributeValue.setAttributeValues(additionalServiceParams.getId()+"");
											quoteProductComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
										}else {
											quoteProductComponentsAttributeValue.setAttributeValues(attributeBean.getValue());
										}
										quoteProductComponentsAttributeValue
												.setProductAttributeMaster(productAttributeMaster);
										quoteProductComponentsAttributeValue
												.setQuoteProductComponent(quoteProductComponents.get(0));
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
										LOGGER.info("-------Updated the properities---new-------");
									} else {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
												.get(0);
										LOGGER.info("quoteProductComponentsAttributeValue Attribute Value  {} ", quoteProductComponentsAttributeValue.getAttributeValues());
										if (quoteProductComponentsAttributeValue.getIsAdditionalParam() != null
												&& quoteProductComponentsAttributeValue.getIsAdditionalParam()
														.equals(CommonConstants.Y)) {
											LOGGER.info("quoteProductComponentsAttributeValue.getIsAdditionalParam() is Y");
											Optional<AdditionalServiceParams> additionalServiceParam = additionalServiceParamRepository
													.findById(Integer.valueOf(
															quoteProductComponentsAttributeValue.getAttributeValues()));
											if (additionalServiceParam.isPresent()) {
												additionalServiceParam.get().setValue(attributeBean.getValue());
												additionalServiceParam.get().setUpdatedBy(Utils.getSource());
												additionalServiceParam.get().setUpdatedTime(new Date());
												additionalServiceParamRepository.save(additionalServiceParam.get());
												quoteProductComponentsAttributeValue
														.setAttributeValues(additionalServiceParam.get().getId() + "");
												quoteProductComponentsAttributeValue
														.setIsAdditionalParam(CommonConstants.Y);
											} else {
												if (attributeBean.getValue()!=null && attributeBean.getValue().length() > 255) {
													AdditionalServiceParams additionalServiceParams = new AdditionalServiceParams();
													additionalServiceParams.setAttribute(attributeBean.getName());
													additionalServiceParams.setCategory("QUOTE");
													additionalServiceParams.setCreatedBy(Utils.getSource());
													additionalServiceParams.setCreatedTime(new Date());
													additionalServiceParams.setIsActive(CommonConstants.Y);
													additionalServiceParams.setValue(attributeBean.getValue());
													additionalServiceParamRepository.save(additionalServiceParams);
													quoteProductComponentsAttributeValue
															.setAttributeValues(additionalServiceParams.getId() + "");
													quoteProductComponentsAttributeValue
															.setIsAdditionalParam(CommonConstants.Y);
												} else {
													quoteProductComponentsAttributeValue
															.setAttributeValues(attributeBean.getValue());
												}
											}
										} else {
											LOGGER.info("-------Does not have quoteProductComponentsAttributeValue.getIsAdditionalParam() -----");
											quoteProductComponentsAttributeValue.setAttributeValues(attributeBean.getValue());
										}
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
										LOGGER.info("-------Updated the properities---old-----");
									}
								}
							else {

								LOGGER.info("-------new  properities created for " + linkid+"name"+mstProductComponents.getName());
								User user = getUserId(Utils.getSource());
								QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
								quoteProductComponent.setMstProductComponent(mstProductComponents);
								quoteProductComponent.setReferenceId(linkid);
								quoteProductComponent.setReferenceName(QuoteConstants.NPL_LINK.toString());
								quoteProductComponent.setMstProductFamily(productFamily.getMstProductFamily());
								quoteProductComponent.setType("primary");
								quoteProductComponentRepository.save(quoteProductComponent);

								if (siteAttributeUpdateBean.getAttributeDetails() != null) {
									for (AttributeDetail attributeDetail : siteAttributeUpdateBean
											.getAttributeDetails()) {
										createSitePropertiesAttribute(quoteProductComponent, attributeDetail,
												user.getUsername());

									}

								}

							}

							});
						}
					});
				}

			}
			
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		}
		
		/**
		 * processTnc - update special tnc
		 * 
		 * @throws TclCommonException
		 */
		public String processTnc(Integer quoteId, QuoteTncBean quoteTncBean) throws TclCommonException {
			String status = Status.SUCCESS.toString();
			try {
				Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
				if (quoteEntity.isPresent()) {
					QuoteTnc quoteTnc = quoteTncRepository.findByQuote(quoteEntity.get());
					if (quoteTnc == null) {
						quoteTnc = new QuoteTnc();
						quoteTnc.setCreatedBy(Utils.getSource());
						quoteTnc.setCreatedTime(new Date());
						quoteTnc.setQuote(quoteEntity.get());
					} else {
						quoteTnc.setUpdatedBy(Utils.getSource());
						quoteTnc.setUpdatedTime(new Date());
					}
					quoteTnc.setTnc(quoteTncBean.getTnc());
					quoteTncRepository.save(quoteTnc);
				} else {
					throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
				}
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			return status;
		}

		/**
		 * 
		 * getTnc
		 * 
		 * @param quoteId
		 * @return
		 * @throws TclCommonException
		 */
		public QuoteTncBean getTnc(Integer quoteId) throws TclCommonException {
			QuoteTncBean quoteTncBean = new QuoteTncBean();
			try {
				Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
				if (quoteEntity.isPresent()) {
					QuoteTnc quoteTnc = quoteTncRepository.findByQuote(quoteEntity.get());
					if (quoteTnc != null) {
						quoteTncBean.setTnc(quoteTnc.getTnc());
					}
				} else {
					throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
				}
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			return quoteTncBean;
		}
	// Emergency API for prod support temp fixes
		// Inserting new entry in link_feasibility table
	public Integer insertLinkFeasibility(LinkFeasibilityManualBean bean) throws TclCommonRuntimeException {
		LOGGER.info("Construct LinkFeasibility Response for linkID", bean.getLinkId());
		Optional<QuoteNplLink> linkId = nplLinkRepository.findById(bean.getLinkId());
		if (!linkId.isPresent()) {
			LOGGER.error("Link ID {} not found in link Feasibility table", bean.getLinkId());
			throw new TclCommonRuntimeException(ExceptionConstants.NPL_LINK_UNAVAILABLE,ResponseResource.R_CODE_ERROR);
		}
		Optional<QuoteNplLink> quoteNplLinkObj = nplLinkRepository.findById(bean.getLinkId());
		LinkFeasibility linkFeasibility = new LinkFeasibility();
		linkFeasibility.setFeasibilityMode(bean.getFeasibilityModeA());
		linkFeasibility.setFeasibilityModeB(bean.getFeasibilityModeB());
		linkFeasibility.setQuoteNplLink(quoteNplLinkObj.get());
		linkFeasibility.setType(bean.getType());
		linkFeasibility.setProvider(bean.getProviderA());
		linkFeasibility.setProviderB(bean.getProviderB());
		linkFeasibility.setRank(bean.getRank());
		linkFeasibility.setIsSelected(bean.getIsSelected().byteValue());
		linkFeasibility.setResponseJson(bean.getResponseJson());
		linkFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		linkFeasibility.setFeasibilityType(bean.getFeasibilityTypeA());
		linkFeasibility.setFeasibilityTypeB(bean.getFeasibilityTypeB());
		linkFeasibility.setFeasibilityCheck(bean.getFeasibilityCheck());
		linkFeasibility.setFeasibilityCode(Utils.generateUid());
		Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(bean.getLinkId());
		if (quoteNplLink.isPresent()) {
			linkFeasibility.setQuoteNplLink(quoteNplLink.get());
		}
		// ADDED for discount delegation
		LOGGER.info("MF SELECTED STATUS" + bean.getIsSelected().byteValue());
		String feasibleLinkResponse = bean.getResponseJson();
		Feasible sitef = new Feasible();
		try {
			sitef = (Feasible) Utils.convertJsonToObject(feasibleLinkResponse, Feasible.class);
		} catch (TclCommonException e) {
			LOGGER.info("EXCEPTION IN BEAN conversion Feasible" + e);
		}
		int cd = 0;
		if (Objects.nonNull(sitef.getChargeableDistance())) {
			LOGGER.info("update cd distance for discount cal for A END feasible" + sitef.getChargeableDistance());
			if (sitef.getChargeableDistance() != null) {
				cd = Integer.parseInt(sitef.getChargeableDistance());
			}
			linkFeasibility.setCdDistance(cd);
		} else {
			LOGGER.info("update cd distance for discount cal for A END not feasible");
			linkFeasibility.setCdDistance(0);
		}
		LinkFeasibility linkEntry = linkFeasibilityRepository.save(linkFeasibility);
		return linkEntry.getId();
	}

	public void updateLinkFeasibility(LinkFeasibilityManualBean bean) {
		if (bean.getLinkId() != null) {
			List<LinkFeasibility> linkEntryList = linkFeasibilityRepository.findByQuoteNplLink_Id(bean.getLinkId());
			if (CollectionUtils.isEmpty(linkEntryList)) {
				LOGGER.error("Link ID {} not found in link Feasibility table", bean.getLinkId());
				throw new TclCommonRuntimeException(ExceptionConstants.NPL_LINK_UNAVAILABLE,
						ResponseResource.R_CODE_ERROR);
			}
			Optional<LinkFeasibility> requestedToBeUpdated = linkEntryList.stream()
					.filter(x -> x.getId().equals(bean.getLinkFeasibilityRowId())).findFirst();
			if (!requestedToBeUpdated.isPresent()) {
				LOGGER.error("No such row ", bean.getLinkFeasibilityRowId());
				throw new TclCommonRuntimeException(ExceptionConstants.NPL_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			LinkFeasibility linkFeasibilityEntry = requestedToBeUpdated.get();
			Map<String, String> utilityFromValue = new HashMap<>();
			// response_json
			if (bean.getColumnName().equals("response_json")) {
				String responseJson = linkFeasibilityEntry.getResponseJson();
				if (responseJson != null) {
					linkFeasibilityEntry.setResponseJson(parseAndUpdateJson(bean, responseJson, linkFeasibilityEntry, utilityFromValue));
					linkFeasibilityRepository.save(linkFeasibilityEntry);
				}
				Optional<Quote> quote = quoteRepository.findById(requestedToBeUpdated.get().getQuoteNplLink().getQuoteId());
				saveUtilityAudit(quote.get().getQuoteCode(), bean.toString(), utilityFromValue.toString(), "Link Feasibility Response Update");
			}
		}
	}

	private String parseAndUpdateJson(LinkFeasibilityManualBean bean, String responseJson,
			LinkFeasibility linkFeasibilityEntry, Map<String, String> utilityFromValue) {
		JSONObject dataEnvelopeObj = null;
		JSONParser jsonParser = new JSONParser();
		try {
			dataEnvelopeObj = (JSONObject) jsonParser.parse(responseJson);
		} catch (org.json.simple.parser.ParseException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		if (!CollectionUtils.isEmpty(bean.getListofFields())) {
			for (LinkJsonManualUpdateBean action : bean.getListofFields()) {
				LOGGER.info("json field {}, json value {}", action.getJsonFieldName(), action.getValueToBeUpdated());
				if (dataEnvelopeObj.containsKey(action.getJsonFieldName())) {
					LOGGER.info("Inside json field {}, json value {}", action.getJsonFieldName(),
							action.getValueToBeUpdated());
					utilityFromValue.put(action.getJsonFieldName(), 
							dataEnvelopeObj.get(action.getJsonFieldName())!=null? (String) dataEnvelopeObj.get(action.getJsonFieldName()): null);
					dataEnvelopeObj.put(action.getJsonFieldName(), action.getValueToBeUpdated());
				}
			}
		}
		return dataEnvelopeObj.toJSONString();
	}


	
	public List<QuoteSiteDifferentialCommercial> persistQuoteSiteCommercialsAtServiceIdLevel(Integer quoteNplLinkId, Order order) {
		List<QuoteSiteDifferentialCommercial> quoteSiteCommercialList = new ArrayList<>();
		LOGGER.info("Entering persistQuoteSiteCommercialsAtServiceIdLevel");
		
		User userEntity = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
		if(userEntity==null) {
			userEntity = userRepository.findByUsernameAndStatus("root", CommonConstants.ACTIVE);//TODO
		}
		Integer createdId=userEntity.getId();
		Double[] subTotalMrc = { 0D };
		Double[] subTotalNrc = { 0D };
		Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(quoteNplLinkId);
		List<QuotePrice> quotePriceList = quotePriceRepository.findByQuoteId(order.getQuote().getId());
		Optional<ProductSolution> productSolution = productSolutionRepository.findById(quoteNplLink.get().getProductSolutionId());
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(productSolution.get().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType())) {
		List<QuoteIllSiteToService> quoteSiteToServiceList= quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(quoteNplLinkId);
		quoteSiteToServiceList.stream().forEach(siteToService -> {
			QuoteSiteDifferentialCommercial serviceDifferentialCommercial = null;
			List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository.
					findByQuoteLinkId(quoteNplLinkId);
			if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) { 
				serviceDifferentialCommercial = quoteSiteDifferentialCommercialList.get(0);
			} else {
				serviceDifferentialCommercial = new QuoteSiteDifferentialCommercial();
			}
			List<SIServiceDetailDataBean> serviceDetailList;
			Double calculatedMrc = 0D;
			try {
				LOGGER.info("Fetching service details for {}",siteToService.getErfServiceInventoryTpsServiceId());
				serviceDetailList = macdUtils.getServiceDetailNPL(siteToService.getErfServiceInventoryTpsServiceId());
			} catch (Exception e) {
				LOGGER.error("Error in persistQuoteSiteCommercialsAtServiceIdLevel", e);
				LOGGER.info("retriggering one timec {}", e.getMessage());
				try {
					LOGGER.info("Fetching service details for {}",siteToService.getErfServiceInventoryTpsServiceId());
					serviceDetailList = macdUtils.getServiceDetailNPL(siteToService.getErfServiceInventoryTpsServiceId());
				} catch (Exception ex) {
					LOGGER.error("Error in persistQuoteSiteCommercialsAtServiceIdLevel", ex);
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
					
				}
			}
			LOGGER.info("Fetched service details for {} and list size {} ",siteToService.getErfServiceInventoryTpsServiceId(),serviceDetailList.size());
			SIServiceDetailDataBean serviceDetail = serviceDetailList.get(0);
			LOGGER.info("service inventory MRC {}, nrc {}, arc {}", serviceDetail.getMrc(), serviceDetail.getNrc(), serviceDetail.getArc());
			calculatedMrc = serviceDetail.getArc()/12;
			LOGGER.info("calculated mrc {}", calculatedMrc);
			serviceDifferentialCommercial.setExistingMrc(calculatedMrc);
			serviceDifferentialCommercial.setExistingNrc(serviceDetail.getNrc());
			serviceDifferentialCommercial.setCreatedBy(createdId);
			serviceDifferentialCommercial.setCreatedTime(new Date());
			serviceDifferentialCommercial.setQuoteCode(productSolution.get().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode());
			serviceDifferentialCommercial.setQuoteToLe(productSolution.get().getQuoteToLeProductFamily().getQuoteToLe());
			serviceDifferentialCommercial.setQuoteLinkId(quoteNplLinkId);
			serviceDifferentialCommercial.setQuoteLinkCode(quoteNplLink.get().getLinkCode());
			serviceDifferentialCommercial.setServiceType(siteToService.getType());
			serviceDifferentialCommercial.setTpsServiceId(siteToService.getErfServiceInventoryTpsServiceId());
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.
					findByReferenceIdAndReferenceName(quoteNplLinkId, QuoteConstants.NPL_LINK.toString());
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
			
			
			List<QuoteProductComponent> quoteProductComponentListPrimary = quoteProductComponentRepository.findByReferenceIdAndReferenceName(quoteNplLinkId, QuoteConstants.NPL_LINK.toString());
			if(quoteProductComponentListPrimary != null && !quoteProductComponentListPrimary.isEmpty()) {
				QuoteSiteDifferentialCommercial serviceDifferentialCommercial = null;
				List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository.findByQuoteLinkId(quoteNplLinkId);
				if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) { 
					serviceDifferentialCommercial = quoteSiteDifferentialCommercialList.get(0);
				} else {
					serviceDifferentialCommercial = new QuoteSiteDifferentialCommercial();
				}	
				serviceDifferentialCommercial.setExistingMrc(0D);
				serviceDifferentialCommercial.setExistingNrc(0D);
				serviceDifferentialCommercial.setCreatedBy(createdId);
				serviceDifferentialCommercial.setCreatedTime(new Date());
				serviceDifferentialCommercial.setQuoteCode(productSolution.get().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode());
				serviceDifferentialCommercial.setQuoteToLe(productSolution.get().getQuoteToLeProductFamily().getQuoteToLe());
				serviceDifferentialCommercial.setQuoteLinkId(quoteNplLinkId);
				serviceDifferentialCommercial.setQuoteLinkCode(quoteNplLink.get().getLinkCode());
				serviceDifferentialCommercial.setServiceType(PDFConstants.LINK);
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
		//LeOwnerDetailsSfdc leOwnerDetailsSfdc= new LeOwnerDetailsSfdc();
		//cusLeId = settingJourneyOwnerDetails(cusLeId, leOwnerDetailsSfdc);

		LOGGER.info("Customer id and cus Le Id are  -> {} ----------- {} ", customerId, cusLeId);
		String queueResponse = (String) mqUtils.sendAndReceive(ownerDetailsQueue, customerId.toString()+cusLeId);
		LOGGER.info("Response from owner details queue for quote ---> {}  is ----> {} ", quoteId, queueResponse);

		LeOwnerDetailsSfdc[] ownerDetails = (LeOwnerDetailsSfdc[]) Utils.convertJsonToObject(queueResponse,
				LeOwnerDetailsSfdc[].class);
		List<LeOwnerDetailsSfdc> ownerDetailsSfdcList = new ArrayList<>();
		ownerDetailsSfdcList.addAll(Arrays.asList(ownerDetails));
		if(!ownerDetailsSfdcList.isEmpty() && Objects.nonNull(ownerDetailsSfdcList)){
			LOGGER.info("List converted into object after Owner Details queue call is----> {} with size ----> {} " , ownerDetailsSfdcList,ownerDetailsSfdcList.size());
		}
		/*if(cusLeId.equalsIgnoreCase("No")){
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
	}*/

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

	private void getLeOwnerDetailsSFDC(NplQuoteBean response, Quote quote) {
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

	
	@SuppressWarnings("resource")
	public CommercialBulkProcessSites uploadNplMultiSiteExcel(MultipartFile file,String approvalLevel,Integer attachmentIdValue,String quoteCode,Integer taskId) throws IOException {
		Quote quoteEntity = quoteRepository.findByQuoteCode(quoteCode);
		List<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findByQuote(quoteEntity);
		Integer quoteId = quoteEntity.getId();
		QuoteToLe quoteTole = quoteToLeEntity.get(0);
		Integer attachmentId = attachmentIdValue;
		byte[] fileContent = file.getBytes();
		CommercialBulkProcessSites commercialBulkProcessSites = new CommercialBulkProcessSites();
		LOGGER.info("Inside uploadexcelfile::NplQuoteService class");
		ByteArrayInputStream inputStream = null;
		List<MultiSiteResponseJsonAttributes> multiSiteResponseJsonAttributesList = new ArrayList<>();
		try {
			if (Objects.nonNull(fileContent) && fileContent.length > 0) {
				LOGGER.info("File read starts...");
				inputStream = new ByteArrayInputStream(fileContent);
				Workbook workbook = null;
				workbook = new XSSFWorkbook(inputStream);
				Map<String,Integer> columnMap = new HashMap<String,Integer>();
				List<Integer> nplLinkList = new ArrayList<>();
				if (workbook != null) {
					int sheetCount = workbook.getNumberOfSheets();
					for (int sheetNo = 0; sheetNo < sheetCount; sheetNo++) {
						Sheet sheet = workbook.getSheetAt(sheetNo);

						int rowSize = sheet.getLastRowNum()+1;
						LOGGER.info("row size "+rowSize);
						
						columnMap = getNplNdeColumnMappingDetails(sheet);
						
						
						for(int index=2;index<rowSize;index++) {
							MultiSiteResponseJsonAttributes multiSiteResponseJsonAttribute = new MultiSiteResponseJsonAttributes();

							Row dataRow = sheet.getRow(index);
							if(Objects.nonNull(dataRow)) {
							Cell linkIdCell = dataRow.getCell(columnMap.get("Link ID"));
							if(Objects.nonNull(linkIdCell)) {
								if(linkIdCell.getCellType()==0) {
									Integer linkId = (int) (linkIdCell.getNumericCellValue());
									multiSiteResponseJsonAttribute.setLinkId(linkId);
								}
								if(linkIdCell.getCellType()==1 || linkIdCell.getCellType()==3) {
									Integer linkId = Integer.parseInt("");
									multiSiteResponseJsonAttribute.setLinkId(linkId);
								}
							}else {
								Integer linkId = Integer.parseInt("");
								multiSiteResponseJsonAttribute.setLinkId(linkId);
							}
							
							Cell portNrcDiscountCell = dataRow.getCell(columnMap.get("Port NRC Discount"));
							if(Objects.nonNull(portNrcDiscountCell)) {
								if(portNrcDiscountCell.getCellType()==0) {
									Double portNrcDiscount = portNrcDiscountCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setPortNrcDiscountInPercentage(portNrcDiscount);
								}
								if(portNrcDiscountCell.getCellType()==1 || portNrcDiscountCell.getCellType()==3) {
									Double portNrcDiscount = 0.0;
									multiSiteResponseJsonAttribute.setPortNrcDiscountInPercentage(portNrcDiscount);
								}
								if(portNrcDiscountCell.getCellType()==2) {
	                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	                            	  CellReference cellReference = new CellReference(portNrcDiscountCell.getAddress().formatAsString());
	                            	  Row row = sheet.getRow(cellReference.getRow());
	                                  Cell cell = row.getCell(cellReference.getCol());
	                                  CellValue cellValue = evaluator.evaluate(cell);
	                                  multiSiteResponseJsonAttribute.setPortNrcDiscountInPercentage(cellValue.getNumberValue());
	                              }
							}else {
								Double portNrcDiscount = 0.0;
								multiSiteResponseJsonAttribute.setPortNrcDiscountInPercentage(portNrcDiscount);
							}

							Cell portNrcSalePriceCell = dataRow.getCell(columnMap.get("Port NRC Sale Price"));
							if(Objects.nonNull(portNrcSalePriceCell)) {
								if(portNrcSalePriceCell.getCellType()==0) {
									Double portNrcSalePrice = portNrcSalePriceCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setPortNrcSalePrice(portNrcSalePrice);
								}
								if(portNrcSalePriceCell.getCellType()==1 || portNrcSalePriceCell.getCellType()==3) {
									Double portNrcSalePrice = 0.0;
									multiSiteResponseJsonAttribute.setPortNrcSalePrice(portNrcSalePrice);
								}
							}else {
								Double portNrcSalePrice = 0.0;
								multiSiteResponseJsonAttribute.setPortNrcSalePrice(portNrcSalePrice);
							}

							Cell portNrcListPriceCell = dataRow.getCell(columnMap.get("Port NRC List Price"));
	                          if(Objects.nonNull(portNrcListPriceCell)) {
	                        	  if(portNrcListPriceCell.getCellType()==0) {
	                            	  Double portNrcListPrice = portNrcListPriceCell.getNumericCellValue();
	                            	  multiSiteResponseJsonAttribute.setPortNrcListPrice(portNrcListPrice);
	                              }
	                              
	                              if(portNrcListPriceCell.getCellType()==1 || portNrcListPriceCell.getCellType()==3) {
	                            	  Double portNrcListPrice = 0.0;
	                            	  multiSiteResponseJsonAttribute.setPortNrcListPrice(portNrcListPrice);
	                              }
	                          }else {
	                        	  Double portNrcListPrice = 0.0;
	                        	  multiSiteResponseJsonAttribute.setPortNrcListPrice(portNrcListPrice);
	                          }
							
							Cell portArcDiscountCell = dataRow.getCell(columnMap.get("Port ARC Discount"));
							if(Objects.nonNull(portArcDiscountCell)) {
								if(portArcDiscountCell.getCellType()==0) {
									Double portArcDiscount = portArcDiscountCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setPortArcDiscountInPercentage(portArcDiscount);
								}
								if(portArcDiscountCell.getCellType()==1 || portArcDiscountCell.getCellType()==3) {
									Double portArcDiscount = 0.0;
									multiSiteResponseJsonAttribute.setPortArcDiscountInPercentage(portArcDiscount);
								}
								if(portArcDiscountCell.getCellType()==2) {
	                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	                            	  CellReference cellReference = new CellReference(portArcDiscountCell.getAddress().formatAsString());
	                            	  Row row = sheet.getRow(cellReference.getRow());
	                                  Cell cell = row.getCell(cellReference.getCol());
	                                  CellValue cellValue = evaluator.evaluate(cell);
	                                  multiSiteResponseJsonAttribute.setPortArcDiscountInPercentage(cellValue.getNumberValue());
	                              }
							}else {
								Double portArcDiscount = 0.0;
								multiSiteResponseJsonAttribute.setPortArcDiscountInPercentage(portArcDiscount);
							}

							Cell portArcSalePriceCell = dataRow.getCell(columnMap.get("Port ARC Sale Price"));
							if(Objects.nonNull(portArcSalePriceCell)) {
								if(portArcSalePriceCell.getCellType()==0) {
									Double portArcSalePrice = portArcSalePriceCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setPortArcSalePrice(portArcSalePrice);
								}
								if(portArcSalePriceCell.getCellType()==1 || portArcSalePriceCell.getCellType()==3) {
									Double portArcSalePrice = 0.0;
									multiSiteResponseJsonAttribute.setPortArcSalePrice(portArcSalePrice);
								}
							}else {
								Double portArcSalePrice = 0.0;
								multiSiteResponseJsonAttribute.setPortArcSalePrice(portArcSalePrice);
							}
							
							Cell portArcListPriceCell = dataRow.getCell(columnMap.get("Port ARC List Price"));
	                          if(Objects.nonNull(portArcListPriceCell)) {
	                        	  if(portArcListPriceCell.getCellType()==0) {
	                            	  Double portArcListPrice = portArcListPriceCell.getNumericCellValue();
	                            	  multiSiteResponseJsonAttribute.setPortArcListPrice(portArcListPrice);
	                              }
	                              
	                              if(portArcListPriceCell.getCellType()==1 || portArcListPriceCell.getCellType()==3) {
	                            	  Double portArcListPrice = 0.0;
	                            	  multiSiteResponseJsonAttribute.setPortArcListPrice(portArcListPrice);
	                              }
	                          }else {
	                        	  Double portArcListPrice = 0.0;
	                        	  multiSiteResponseJsonAttribute.setPortArcListPrice(portArcListPrice);
	                          }

							Cell lastMileNrcDiscountCell = dataRow.getCell(columnMap.get("Last Mile NRC Discount"));
							if(Objects.nonNull(lastMileNrcDiscountCell)) {
								if(lastMileNrcDiscountCell.getCellType()==0) {
									Double lastMileNrcDiscount = lastMileNrcDiscountCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setLastMileNrcDiscountInPercentage(lastMileNrcDiscount);
								}
								if(lastMileNrcDiscountCell.getCellType()==1 || lastMileNrcDiscountCell.getCellType()==3) {
									Double lastMileNrcDiscount = 0.0;
									multiSiteResponseJsonAttribute.setLastMileNrcDiscountInPercentage(lastMileNrcDiscount);
								}
								if(lastMileNrcDiscountCell.getCellType()==2) {
	                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	                            	  CellReference cellReference = new CellReference(lastMileNrcDiscountCell.getAddress().formatAsString());
	                            	  Row row = sheet.getRow(cellReference.getRow());
	                                  Cell cell = row.getCell(cellReference.getCol());
	                                  CellValue cellValue = evaluator.evaluate(cell);
	                                  multiSiteResponseJsonAttribute.setLastMileNrcDiscountInPercentage(cellValue.getNumberValue());
	                              }
							}else {
								Double lastMileNrcDiscount = 0.0;
								multiSiteResponseJsonAttribute.setLastMileNrcDiscountInPercentage(lastMileNrcDiscount);
							}
							
							Cell lastMileNRCSalePriceCell = dataRow.getCell(columnMap.get("Last Mile NRC Sale Price"));
							if(Objects.nonNull(lastMileNRCSalePriceCell)) {
								if(lastMileNRCSalePriceCell.getCellType()==0) {
									Double lastMileNRCSalePrice = lastMileNRCSalePriceCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setLastMileNrcSalePrice(lastMileNRCSalePrice);
								}
								if(lastMileNRCSalePriceCell.getCellType()==1 || lastMileNRCSalePriceCell.getCellType()==3) {
									Double lastMileNRCSalePrice = 0.0;
									multiSiteResponseJsonAttribute.setLastMileNrcSalePrice(lastMileNRCSalePrice);
								}
							}else {
								Double lastMileNRCSalePrice = 0.0;
								multiSiteResponseJsonAttribute.setLastMileNrcSalePrice(lastMileNRCSalePrice);
							}

							Cell lastMileNRCListPriceCell = dataRow.getCell(columnMap.get("Last Mile NRC List Price"));
	                          if(Objects.nonNull(lastMileNRCListPriceCell)) {
	                        	  if(lastMileNRCListPriceCell.getCellType()==0) {
	                            	  Double lastMileNRCListPrice = lastMileNRCListPriceCell.getNumericCellValue();
	                                  multiSiteResponseJsonAttribute.setLastMileNrcListPrice(lastMileNRCListPrice);
	                              }
	                             
	                              if(lastMileNRCListPriceCell.getCellType()==1 || lastMileNRCListPriceCell.getCellType()==3) {
	                            	  Double lastMileNRCListPrice = 0.0;
	                            	  multiSiteResponseJsonAttribute.setLastMileNrcListPrice(lastMileNRCListPrice);
	                              }
	                          }else {
	                        	  Double lastMileNRCListPrice = 0.0;
	                        	  multiSiteResponseJsonAttribute.setLastMileNrcListPrice(lastMileNRCListPrice);
	                          }
							
							Cell lastMileARCDiscountCell = dataRow.getCell(columnMap.get("Last Mile ARC Discount"));
							if(Objects.nonNull(lastMileARCDiscountCell)) {
								if(lastMileARCDiscountCell.getCellType()==0) {
									Double lastMileARCDiscount = lastMileARCDiscountCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setLastMileArcDiscountInPercentage(lastMileARCDiscount);
								}
								if(lastMileARCDiscountCell.getCellType()==1 || lastMileARCDiscountCell.getCellType()==3) {
									Double lastMileARCDiscount = 0.0;
									multiSiteResponseJsonAttribute.setLastMileArcDiscountInPercentage(lastMileARCDiscount);
								}
								 if(lastMileARCDiscountCell.getCellType()==2) {
	                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	                            	  CellReference cellReference = new CellReference(lastMileARCDiscountCell.getAddress().formatAsString());
	                            	  Row row = sheet.getRow(cellReference.getRow());
	                                  Cell cell = row.getCell(cellReference.getCol());
	                                  CellValue cellValue = evaluator.evaluate(cell);
	                                  multiSiteResponseJsonAttribute.setLastMileArcDiscountInPercentage(cellValue.getNumberValue());
	                              }
							}else {
								Double lastMileARCDiscount = 0.0;
								multiSiteResponseJsonAttribute.setLastMileArcDiscountInPercentage(lastMileARCDiscount);
							}
							

							Cell lastMileARCSalePriceCell = dataRow.getCell(columnMap.get("Last Mile ARC Sale Price"));
							if(Objects.nonNull(lastMileARCSalePriceCell)) {
								if(lastMileARCSalePriceCell.getCellType()==0) {
									Double lastMileARCSalePrice = lastMileARCSalePriceCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setLastMileArcSalePrice(lastMileARCSalePrice);
								}
								if(lastMileARCSalePriceCell.getCellType()==1 || lastMileARCSalePriceCell.getCellType()==3) {
									Double lastMileARCSalePrice = 0.0;
									multiSiteResponseJsonAttribute.setLastMileArcSalePrice(lastMileARCSalePrice);
								}
							}else {
								Double lastMileARCSalePrice = 0.0;
								multiSiteResponseJsonAttribute.setLastMileArcSalePrice(lastMileARCSalePrice);
							}
							
							Cell lastMileARCListPriceCell = dataRow.getCell(columnMap.get("Last Mile ARC List Price"));
	                          if(Objects.nonNull(lastMileARCListPriceCell)) {
	                        	  if(lastMileARCListPriceCell.getCellType()==0) {
	                            	  Double lastMileARCListPrice = lastMileARCListPriceCell.getNumericCellValue();
	                                  multiSiteResponseJsonAttribute.setLastMileArcListPrice(lastMileARCListPrice);
	                              }
	                             
	                              if(lastMileARCListPriceCell.getCellType()==1 || lastMileARCListPriceCell.getCellType()==3) {
	                            	  Double lastMileARCListPrice = 0.0;
	                            	  multiSiteResponseJsonAttribute.setLastMileArcListPrice(lastMileARCListPrice);
	                              }
	                          }else {
	                        	  Double lastMileARCListPrice = 0.0;
	                        	  multiSiteResponseJsonAttribute.setLastMileArcListPrice(lastMileARCListPrice);
	                          }
//							Cell burstableARCDiscountPriceCell = dataRow.getCell(columnMap.get("Burstable ARC Discount Price"));
//							if(Objects.nonNull(burstableARCDiscountPriceCell)) {
//								if(burstableARCDiscountPriceCell.getCellType()==0) {
//									Double burstableARCDiscountPrice = burstableARCDiscountPriceCell.getNumericCellValue();
//									multiSiteResponseJsonAttribute.setBurstableArcDiscountInPercentage(burstableARCDiscountPrice);
//								}
//								if(burstableARCDiscountPriceCell.getCellType()==1 || burstableARCDiscountPriceCell.getCellType()==3) {
//									Double burstableARCDiscountPrice = 0.0;
//									multiSiteResponseJsonAttribute.setBurstableArcDiscountInPercentage(burstableARCDiscountPrice);
//								}
//							}else {
//								Double burstableARCDiscountPrice = 0.0;
//								multiSiteResponseJsonAttribute.setBurstableArcDiscountInPercentage(burstableARCDiscountPrice);
//							}
//
//							Cell burstableARCSalePriceCell = dataRow.getCell(columnMap.get("Burstable ARC Sale Price"));
//							if(Objects.nonNull(burstableARCSalePriceCell)) {
//								if(burstableARCSalePriceCell.getCellType()==0) {
//									Double burstableARCSalePrice = burstableARCSalePriceCell.getNumericCellValue();
//									multiSiteResponseJsonAttribute.setBurstableArcSalePrice(burstableARCSalePrice);
//								}
//								if(burstableARCSalePriceCell.getCellType()==1 || burstableARCSalePriceCell.getCellType()==3) {
//									Double burstableARCSalePrice = 0.0;
//									multiSiteResponseJsonAttribute.setBurstableArcSalePrice(burstableARCSalePrice);
//								}
//							}else {
//								Double burstableARCSalePrice = 0.0;
//								multiSiteResponseJsonAttribute.setBurstableArcSalePrice(burstableARCSalePrice);
//							}
							Cell linkManagementChargesARCDiscountCell = dataRow.getCell(columnMap.get("Link Management Charges ARC Discount"));
							if(Objects.nonNull(linkManagementChargesARCDiscountCell)) {
								if(linkManagementChargesARCDiscountCell.getCellType()==0) {
									Double linkManagementChargesARCDiscount = linkManagementChargesARCDiscountCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setLinkManagementChargesArcDiscountInPercentage(linkManagementChargesARCDiscount);
								}
								if(linkManagementChargesARCDiscountCell.getCellType()==1 || linkManagementChargesARCDiscountCell.getCellType()==3) {
									Double linkManagementChargesARCDiscount = 0.0;
									multiSiteResponseJsonAttribute.setLinkManagementChargesArcDiscountInPercentage(linkManagementChargesARCDiscount);
								}
								if(linkManagementChargesARCDiscountCell.getCellType()==2) {
	                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	                            	  CellReference cellReference = new CellReference(linkManagementChargesARCDiscountCell.getAddress().formatAsString());
	                            	  Row row = sheet.getRow(cellReference.getRow());
	                                  Cell cell = row.getCell(cellReference.getCol());
	                                  CellValue cellValue = evaluator.evaluate(cell);
	                                  multiSiteResponseJsonAttribute.setLinkManagementChargesArcDiscountInPercentage(cellValue.getNumberValue());
	                              }
							}else {
								Double linkManagementChargesARCDiscount = 0.0;
								multiSiteResponseJsonAttribute.setLinkManagementChargesArcDiscountInPercentage(linkManagementChargesARCDiscount);
							}
							Cell linkManagementChargesARCSalePriceCell = dataRow.getCell(columnMap.get("Link Management Charges ARC Sale Price"));
							if(Objects.nonNull(linkManagementChargesARCSalePriceCell)) {
								if(linkManagementChargesARCSalePriceCell.getCellType()==0) {
									Double linkManagementChargesARCSalePrice = linkManagementChargesARCSalePriceCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setLinkManagementChargesArcSalePrice(linkManagementChargesARCSalePrice);
								}
								if(linkManagementChargesARCSalePriceCell.getCellType()==1 || linkManagementChargesARCSalePriceCell.getCellType()==3) {
									Double linkManagementChargesARCSalePrice = 0.0;
									multiSiteResponseJsonAttribute.setLinkManagementChargesArcSalePrice(linkManagementChargesARCSalePrice);
								}
							}else {
								Double linkManagementChargesARCSalePrice = 0.0;
								multiSiteResponseJsonAttribute.setLinkManagementChargesArcSalePrice(linkManagementChargesARCSalePrice);
							}
							Cell linkManagementChargesARCListPriceCell = dataRow.getCell(columnMap.get("Link Management Charges ARC List Price"));
							if(Objects.nonNull(linkManagementChargesARCListPriceCell)) {
								if(linkManagementChargesARCListPriceCell.getCellType()==0) {
									Double linkManagementChargesARCListPrice = linkManagementChargesARCListPriceCell.getNumericCellValue();
									multiSiteResponseJsonAttribute.setLinkManagementChargesArcListPrice(linkManagementChargesARCListPrice);
								}
								if(linkManagementChargesARCListPriceCell.getCellType()==1 || linkManagementChargesARCListPriceCell.getCellType()==3) {
									Double linkManagementChargesARCListPrice = 0.0;
									multiSiteResponseJsonAttribute.setLinkManagementChargesArcListPrice(linkManagementChargesARCListPrice);
								}
							}else {
								Double linkManagementChargesARCListPrice = 0.0;
								multiSiteResponseJsonAttribute.setLinkManagementChargesArcListPrice(linkManagementChargesARCListPrice);
							}

							Cell siteLevelActionCell = dataRow.getCell(columnMap.get("Site Level Action"));
							if(Objects.nonNull(siteLevelActionCell)) {
								if(siteLevelActionCell.getCellType()==1) {
									String siteLevelAction = siteLevelActionCell.getStringCellValue();                       
									multiSiteResponseJsonAttribute.setSiteLevelActions(Objects.nonNull(siteLevelAction)?siteLevelAction:"");
								}
								if(siteLevelActionCell.getCellType()==0) {
									String siteLevelAction = "";                       
									multiSiteResponseJsonAttribute.setSiteLevelActions(siteLevelAction);
								}
							}else {
								String siteLevelAction = "";                       
								multiSiteResponseJsonAttribute.setSiteLevelActions(siteLevelAction);
							}
	                        	  
							Cell commercialManagerCommentsCell = dataRow.getCell(columnMap.get("Commercial Manager Comments"));
							if(Objects.nonNull(commercialManagerCommentsCell)) {
								if(commercialManagerCommentsCell.getCellType()==1) {
									String commercialManagerComments = Objects.nonNull(commercialManagerCommentsCell.getStringCellValue())?commercialManagerCommentsCell.getStringCellValue():"";
									multiSiteResponseJsonAttribute.setCommercialManagerComments(commercialManagerComments);
								}
								if(commercialManagerCommentsCell.getCellType()==0) {
									String commercialManagerComments = "";
									multiSiteResponseJsonAttribute.setCommercialManagerComments(commercialManagerComments);
								}
							}else {
								String commercialManagerComments = "";
								multiSiteResponseJsonAttribute.setCommercialManagerComments(commercialManagerComments);
							}

							multiSiteResponseJsonAttribute.setApprovalLevel(Objects.nonNull(approvalLevel)?approvalLevel:"");
							multiSiteResponseJsonAttributesList.add(multiSiteResponseJsonAttribute);
							nplLinkList.add(multiSiteResponseJsonAttribute.getLinkId());

							LOGGER.info("multiSiteResponseJsonAttributesList== " + multiSiteResponseJsonAttributesList.toString());
							LOGGER.info("multiSiteResponseJsonAttributesList size "+multiSiteResponseJsonAttributesList.size());
							//reset bulkupload status to zero for NplLink each upload
							Optional<QuoteNplLink> link = nplLinkRepository.findById(multiSiteResponseJsonAttribute.getLinkId());
							if (link.isPresent()) {
								link.get().setLinkBulkUpdate("0");
								link.get().setCommercialApproveStatus("0");
								link.get().setCommercialRejectionStatus("0");
								nplLinkRepository.save(link.get());
							}
						}
						}
					}
				} 
				processCommercialBulkSites(quoteId,quoteCode,multiSiteResponseJsonAttributesList,attachmentId,taskId);
				//bulk status updation in quotetole
				quoteTole.setQuoteBulkUpdate("1");
				quoteToLeRepository.save(quoteTole);
			}
			else {
				LOGGER.info("Exception occured in uploadIllGvpnMultiSiteExcel :: fileContent length is zero"
						+ fileContent.length);
			}
		}catch (Exception e) {
			LOGGER.error("Exception Occured while nplQuoteService : {}" + e.getMessage());
		}
		return commercialBulkProcessSites;
	}

	private Map<String, Integer> getNplNdeColumnMappingDetails(Sheet sheet) {
		Map<String,Integer> columnMap = new HashMap<String,Integer>();
		Iterator iterator = sheet.iterator();
		boolean exitFlag = false;
		while(iterator.hasNext()) {
			Row attributesRow = sheet.getRow(1);
			if(attributesRow.getRowNum()==1) {
				exitFlag = true;
				attributesRow.forEach(attributeRow->{
					columnMap.put(attributeRow.getStringCellValue(), attributeRow.getColumnIndex());
				});
			}
			if(exitFlag) {
				break;
			}else {
				continue;
			}
		}
		return columnMap;
	}

	private void processCommercialBulkSites(Integer quoteId, String quoteCode,
			List<MultiSiteResponseJsonAttributes> multiSiteResponseJsonAttributesList, Integer attachmentId,
			Integer taskId) throws TclCommonException {
		CommercialBulkProcessSites commercialBulkProcessSites = commercialBulkProcessSiteRepository
				.findBytaskId(taskId);
		if (commercialBulkProcessSites == null) {
			commercialBulkProcessSites = new CommercialBulkProcessSites();
			commercialBulkProcessSites.setTaskId(taskId);
			commercialBulkProcessSites.setQuoteId(quoteId);
			commercialBulkProcessSites.setQuoteCode(quoteCode);
			commercialBulkProcessSites.setStatus("OPEN");
			commercialBulkProcessSites.setResponseJson(Utils.convertObjectToJson(multiSiteResponseJsonAttributesList));
			commercialBulkProcessSites.setSiteStatus("SUCCESS");
			commercialBulkProcessSites.setAttachementId(attachmentId);
			commercialBulkProcessSites.setCreatedBy(Utils.getSource());
			commercialBulkProcessSites.setCreatedTime(new Date());
			commercialBulkProcessSiteRepository.save(commercialBulkProcessSites);
		} else {
			LOGGER.info("ENTER else part" + commercialBulkProcessSites.getId());
			commercialBulkProcessSites.setTaskId(taskId);
			commercialBulkProcessSites.setQuoteId(quoteId);
			commercialBulkProcessSites.setQuoteCode(quoteCode);
			commercialBulkProcessSites.setStatus("OPEN");
			commercialBulkProcessSites.setResponseJson(Utils.convertObjectToJson(multiSiteResponseJsonAttributesList));
			commercialBulkProcessSites.setSiteStatus("SUCCESS");
			commercialBulkProcessSites.setAttachementId(attachmentId);
			commercialBulkProcessSites.setUpdatedTime(new Date());
			commercialBulkProcessSiteRepository.save(commercialBulkProcessSites);
		}
		LOGGER.info(
				"Link details for quote code " + quoteCode + " is successfully saved in commercial bulk process sites");
		processOpenCommercialBulkSites(commercialBulkProcessSites);
	}

	private void processOpenCommercialBulkSites(CommercialBulkProcessSites commercialBulkProcessSites) {
		LOGGER.info("Inside processOpenCommercialBulkSites method");
				try {
					BulkSiteBean bulkSiteBean = new BulkSiteBean();
					bulkSiteBean.setQuoteId(String.valueOf(commercialBulkProcessSites.getQuoteId()));
					bulkSiteBean.setBulkId(String.valueOf(commercialBulkProcessSites.getId()));
					bulkSiteBean.setTaskId(String.valueOf(commercialBulkProcessSites.getTaskId()));
					bulkSiteBean.setQuoteCode(commercialBulkProcessSites.getQuoteCode());
					String requestData = Utils.convertObjectToJson(bulkSiteBean);
					LOGGER.info("requestData{} queueName{} ", requestData, omsBulkSiteProcessQueue);
					updateStatus("INPROGRESS", commercialBulkProcessSiteRepository,
							commercialBulkProcessSites);
					mqUtils.send(omsBulkSiteProcessQueue,requestData);
				} catch (TclCommonException e) {
					LOGGER.error("Error occured while triggering the Commercial Bulk Process Job ", e);
				}
			}

	private void updateStatus(String status,
			CommercialBulkProcessSiteRepository commercialBulkProcessSiteRepository,
			CommercialBulkProcessSites commercialBulkProcessSites) {
		LOGGER.info("Updating status for {} with status {}", commercialBulkProcessSites, status);
		if (commercialBulkProcessSites != null) {
			commercialBulkProcessSites.setStatus(status);
			commercialBulkProcessSiteRepository.save(commercialBulkProcessSites);
		}
	}
	
	/**
	 * This method to get the status of multisites 
	 * @param quoteCode
	 * @return MultiSiteStatusBean
	 */
	public MultiSiteStatusBean getMultiSiteStatusForNpl(String quoteCode) {
		LOGGER.info("Inside getMultiSiteStatusForIllGvpn method quoteCode{} ", quoteCode);
		MultiSiteStatusBean multiSiteStatusBean = new MultiSiteStatusBean();
		long siteCompleteCount = 0L;
		long siteInProgressCount = 0L;
		Integer overAllSiteCount = 0;
		List<QuoteNplLink> nplLinkList = nplLinkRepository.findLinks(quoteCode);
		if(Objects.nonNull(nplLinkList) && !nplLinkList.isEmpty()) {
			LOGGER.info("Inside nplLinkList size{} ", nplLinkList.size());
			overAllSiteCount = nplLinkList.size();
			siteCompleteCount = nplLinkList.stream().filter(nplLink -> Objects.nonNull(nplLink.getLinkBulkUpdate()) && nplLink.getLinkBulkUpdate().equals("1")).count();
			siteInProgressCount = nplLinkList.stream().filter(nplLink -> Objects.isNull(nplLink.getLinkBulkUpdate()) || nplLink.getLinkBulkUpdate().equals("0")).count();
			LOGGER.info("siteCompleteCount {} ", siteCompleteCount);
			LOGGER.info("siteInProgressCount {} ", siteInProgressCount);
			LOGGER.info("overAllSiteCount {} ", overAllSiteCount);
		}
		String overAllStatus = (overAllSiteCount <= siteCompleteCount)? "COMPLETED" : "INPROGRESS" ;
		LOGGER.info("overAllStatus {} ", overAllStatus);
		multiSiteStatusBean.setOverAllStatus(overAllStatus);
		multiSiteStatusBean.setSiteComplete(String.valueOf(siteCompleteCount));
		multiSiteStatusBean.setSiteInProgress(String.valueOf(siteInProgressCount));
		return multiSiteStatusBean;
	}
	

	/**
	 * This method is to download the NPL-NDE feasibility Report
	 * 
	 * @author 4016226-Sobhan
	 * @param response
	 * @param quoteCode
	 * @throws IOException and TclCommonException
	 */

	public void returnNplNdeMultisiteReportExcel(HttpServletResponse response, String linkCode)
			throws TclCommonException, IOException {
			try {
				LOGGER.info("Inside returnNplNdeMultisiteReportExcel method:start{}:");
		/** 
		 * linkCode check
		 * */
		if(linkCode.isEmpty() || !Objects.nonNull(linkCode)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("linkCode: "+linkCode);
		List<NplNdeMultiSiteInputs> nplNdeMultiSiteInputs = new ArrayList<>();
		List<NplNdeMultiSiteInputs> NplNdeSiteInfoList = new ArrayList<NplNdeMultiSiteInputs>();
		/*
		 * get the npl nde site related information :illNplNdeMultiSiteInputs
		 */
		nplNdeMultiSiteInputs = getNplNdeSiteRelatedInformation(linkCode, NplNdeSiteInfoList);
		LOGGER.info("NplNdeMultiSiteInputs Size:  "+nplNdeMultiSiteInputs.size());
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		//sheet.protectSheet("password");
		((XSSFSheet) sheet).lockFormatColumns(false);
		int rowNum = 0;
		Row headerRow = sheet.createRow(rowNum++);
		int headingCol = 0;
		int dataCol = 0;
		int autosizecolumn = headingCol++;
		Cell cell = headerRow.createCell(dataCol++);
		
		/* Header color for non editable-set to yellow */
		XSSFCellStyle headerCellNonEditable = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		headerCellNonEditable.setLocked(true);
		
		/* Header color for editable as sky blue */
		XSSFCellStyle headerEditableColor = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		headerEditableColor.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
		headerEditableColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerEditableColor.setLocked(true);
		  
		XSSFFont font = (XSSFFont) workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBold(true);
		font.setItalic(false);
		
		headerCellNonEditable.setFont(font);
		headerEditableColor.setFont(font);
		
		/*
		 * Address Details (A-End)
		 */
		headerRow = sheet.createRow(0);
		Cell headerCell = headerRow.createCell(1);
		headerCell.setCellValue("Address Details (A-End)");
		CellStyle cellStyle = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);
		headerCell.setCellStyle(cellStyle);
		sheet.addMergedRegion(CellRangeAddress.valueOf("C1:D1"));
		headerCell = headerRow.createCell(autosizecolumn);
		sheet.autoSizeColumn(1, true);
		
		Row subHeaderRow = sheet.createRow(1);
		cell = subHeaderRow.createCell(0);
		cell = subHeaderRow.createCell(0);
		cell.setCellValue("Link ID");
		cell.setCellStyle(headerCellNonEditable);
		
		cell = subHeaderRow.createCell(1);
		cell.setCellValue("Link Code");
		cell.setCellStyle(headerCellNonEditable);
		
		cell = subHeaderRow.createCell(2);
		cell.setCellValue("Address");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(3);
		cell.setCellValue("Country");
		cell.setCellStyle(headerCellNonEditable);
		/*
		 * Address Details (B-End)
		 */
		Cell headerCellTwo = headerRow.createCell(4);
		headerCellTwo.setCellValue("Address Details (B-End)");
		CellStyle cellStyleTwo = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleTwo.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTwo.setFont(font);
		headerCellTwo.setCellStyle(cellStyleTwo);
		sheet.addMergedRegion(CellRangeAddress.valueOf("E1:F1"));
		headerCellTwo = headerRow.createCell(autosizecolumn);
		sheet.autoSizeColumn(4, true);

		cell = subHeaderRow.createCell(4);
		cell.setCellValue("Address");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(5);
		cell.setCellValue("Country");
		cell.setCellStyle(headerCellNonEditable);
		/*
		 * Existing Component (Only for MAC/PR)
		 */
		Cell headerCellThree = headerRow.createCell(6);
		headerCellThree.setCellValue("Existing Component (Only for MAC/PR)");
		CellStyle cellStyleThree = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleThree.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleThree.setFont(font);
		headerCellThree.setCellStyle(cellStyleThree);
		sheet.addMergedRegion(CellRangeAddress.valueOf("G1:V1"));
		headerCellThree = headerRow.createCell(autosizecolumn);
		sheet.autoSizeColumn(6, true);
		
		cell = subHeaderRow.createCell(6);
		cell.setCellValue("Service Id");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(7);
		cell.setCellValue("Port Bandwidth (Mbps)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(8);
		cell.setCellValue("Local Loop Bandwidth");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(9);
		cell.setCellValue("End to End Chargeable Distance (Km)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(10);
		cell.setCellValue("LM Provider A-End");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(11);
		cell.setCellValue("LM Provider B-End");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(12);
		cell.setCellValue("CPE Management Type");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(13);
		cell.setCellValue("CPE Support Type");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(14);
		cell.setCellValue("CPE Model A-End");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(15);
		cell.setCellValue("CPE Model B-End");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(16);
		cell.setCellValue("Currency");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(17);
		cell.setCellValue("Existing ARC");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(18);
		cell.setCellValue("Existing NRC");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(19);
		cell.setCellValue("Contract Term");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(20);
		cell.setCellValue("Commission Date");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(21);
		cell.setCellValue("Expiry Date");
		cell.setCellStyle(headerCellNonEditable);

		/*
		 * ATTRIBUTES - BASIC -8
		 */
		Cell headerCellFour = headerRow.createCell(22);
		headerCellFour.setCellValue("ATTRIBUTES - BASIC");

		CellStyle cellStyleFour = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleFour.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleFour.setFont(font);
		headerCellFour.setCellStyle(cellStyleFour);
		sheet.addMergedRegion(CellRangeAddress.valueOf("W1:AC1"));
		headerCellFour = headerRow.createCell(autosizecolumn);
		sheet.autoSizeColumn(22, true);

		cell = subHeaderRow.createCell(22);
		cell.setCellValue("Product");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(23);
		cell.setCellValue("Order Type");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(24);
		cell.setCellValue("Interface");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(25);
		cell.setCellValue("Port Bandwidth (Mbps)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(26);
		cell.setCellValue("Local Loop Bandwidth (Mbps)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(27);
		cell.setCellValue("CPE Management Type");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(28);
		cell.setCellValue("Service Variant");
		cell.setCellStyle(headerCellNonEditable);
//		cell = subHeaderRow.createCell(28);
//		cell.setCellValue("Chargeable Distance for NDE/NPL (Km)");
//		cell.setCellStyle(headerCellNonEditable);

		/*
		 * ATTRIBUTES - ADVANCED -6
		 */
		Cell headerCellFive = headerRow.createCell(29);
		headerCellFive.setCellValue("ATTRIBUTES - ADVANCED");

		CellStyle cellStyleFive = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleFive.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleFive.setFont(font);
		headerCellFive.setCellStyle(cellStyleFive);
		sheet.addMergedRegion(CellRangeAddress.valueOf("AD1:AI1"));
		headerCellFive = headerRow.createCell(autosizecolumn);
		sheet.autoSizeColumn(29, true);

		cell = subHeaderRow.createCell(29);
		cell.setCellValue("Service type");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(30);
		cell.setCellValue("Burstable Bandwidth");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(31);
		cell.setCellValue("Shared Last Mile");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(32);
		cell.setCellValue("Shared Last Mile Service ID");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(33);
		cell.setCellValue("Shared CPE");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(34);
		cell.setCellValue("Shared CPE Service ID");
		cell.setCellStyle(headerCellNonEditable);
		/*
		 * Feasibility -1
		 */
		Cell headerCellSix = headerRow.createCell(35);
		headerCellSix.setCellValue("Feasibility");
		CellStyle cellStyleSix = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleSix.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleSix.setFont(font);
		headerCellSix.setCellStyle(cellStyleSix);

		cell = subHeaderRow.createCell(35);
		cell.setCellValue("Response Type");
		cell.setCellStyle(headerCellNonEditable);

		/*
		 * Feasibility A-End -24
		 */
		Cell headerCellSeven = headerRow.createCell(36);
		headerCellSeven.setCellValue("Feasibility A-End");
		CellStyle cellStyleSeven = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleSeven.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleSeven.setFont(font);
		headerCellSeven.setCellStyle(cellStyleSeven);
		sheet.addMergedRegion(CellRangeAddress.valueOf("AK1:BG1"));

		cell = subHeaderRow.createCell(36);
		cell.setCellValue("FEASIBILITY STATUS");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(37);
		cell.setCellValue("FEASIBILITY TYPE");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(38);
		cell.setCellValue("FEASIBILITY Validity");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(39);
		cell.setCellValue("FEASIBILITY CODE");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(40);
		cell.setCellValue("FEASIBILITY MODE");
		cell.setCellStyle(headerCellNonEditable);
//		cell = subHeaderRow.createCell(40);
//		cell.setCellValue("FEASIBILITY TYPE");
//		cell.setCellStyle(headerCellNonEditable);
//		cell = subHeaderRow.createCell(41);
//		cell.setCellValue("TYPE");
//		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(41);
		cell.setCellValue("PROVIDER");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(42);
		cell.setCellValue("FEASIBILITY STATUS");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(43);
		cell.setCellValue("CD");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(44);
		cell.setCellValue("TCL POP");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(45);
		cell.setCellValue("Feasibility Check");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(46);
		cell.setCellValue("CONNECTED BUILDING");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(47);
		cell.setCellValue("CONNECTED CUSTOMER");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(48);
		cell.setCellValue("OSP DIST");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(49);
		cell.setCellValue("OSP CAPEX");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(50);
		cell.setCellValue("IN BUILDING CAPEX ");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(51);
		cell.setCellValue("MUX COST");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(52);
		cell.setCellValue("PROW VALUE (OTC)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(53);
		cell.setCellValue("PROW VALUE (ARC)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(54);
		cell.setCellValue("Mast Height (Meter)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(55);
		cell.setCellValue("Mast NRC");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(56);
		cell.setCellValue("Offnet NRC");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(57);
		cell.setCellValue("Offnet ARC");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(58);
		cell.setCellValue("Feasibility A End Remarks");
		cell.setCellStyle(headerCellNonEditable);

		/*
		 * Feasibility B-End -24
		 */
		Cell headerCellEight = headerRow.createCell(59);
		headerCellEight.setCellValue("Feasibility B-End");
		CellStyle cellStyleEight = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleEight.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleEight.setFont(font);
		headerCellEight.setCellStyle(cellStyleEight);
		sheet.addMergedRegion(CellRangeAddress.valueOf("BH1:CD1"));

		cell = subHeaderRow.createCell(59);
		cell.setCellValue("FEASIBILITY STATUS");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(60);
		cell.setCellValue("FEASIBILITY TYPE");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(61);
		cell.setCellValue("FEASIBILITY Validity");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(62);
		cell.setCellValue("FEASIBILITY CODE");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(63);
		cell.setCellValue("FEASIBILITY MODE");
		cell.setCellStyle(headerCellNonEditable);
//		cell = subHeaderRow.createCell(65);
//		cell.setCellValue("FEASIBILITY TYPE");
//		cell.setCellStyle(headerCellNonEditable);
//		cell = subHeaderRow.createCell(66);
//		cell.setCellValue("TYPE");
//		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(64);
		cell.setCellValue("PROVIDER");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(65);
		cell.setCellValue("FEASIBILITY STATUS");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(66);
		cell.setCellValue("CD");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(67);
		cell.setCellValue("TCL POP");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(68);
		cell.setCellValue("Feasibility Check");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(69);
		cell.setCellValue("CONNECTED BUILDING");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(70);
		cell.setCellValue("CONNECTED CUSTOMER");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(71);
		cell.setCellValue("OSP DIST");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(72);
		cell.setCellValue("OSP CAPEX");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(73);
		cell.setCellValue("IN BUILDING CAPEX ");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(74);
		cell.setCellValue("MUX COST");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(75);
		cell.setCellValue("PROW VALUE (OTC)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(76);
		cell.setCellValue("PROW VALUE (ARC)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(77);
		cell.setCellValue("Mast Height (Meter)");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(78);
		cell.setCellValue("Mast NRC");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(79);
		cell.setCellValue("Offnet NRC");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(80);
		cell.setCellValue("Offnet ARC");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(81);
		cell.setCellValue("Feasibility Remarks B End");
		cell.setCellStyle(headerCellNonEditable);

		/*
		 * Pricing
		 */
		Cell headerCellNine = headerRow.createCell(82);
		headerCellNine.setCellValue("Pricing");
		CellStyle cellStyleNine = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleNine.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleNine.setFont(font);
		headerCellNine.setCellStyle(cellStyleNine);
		sheet.addMergedRegion(CellRangeAddress.valueOf("CE1:CL1"));

		cell = subHeaderRow.createCell(82);
		cell.setCellValue("Contract Term");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(83);
		cell.setCellValue("Currency");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(84);
		cell.setCellValue("Port NRC List Price");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(85);
		cell.setCellValue("Port NRC Discount");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(86);
		cell.setCellValue("Port NRC Sale Price");
		cell.setCellStyle(headerEditableColor);
		cell = subHeaderRow.createCell(87);
		cell.setCellValue("Port ARC List Price");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(88);
		cell.setCellValue("Port ARC Discount");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(89);
		cell.setCellValue("Port ARC Sale Price");
		cell.setCellStyle(headerEditableColor);

		/*
		 * A-End
		 */
		Cell headerCellTen = headerRow.createCell(90);
		headerCellTen.setCellValue("A-End");
		CellStyle cellStyleTen = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleTen.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTen.setFont(font);
		headerCellTen.setCellStyle(cellStyleNine);
		sheet.addMergedRegion(CellRangeAddress.valueOf("CM1:CR1"));

		cell = subHeaderRow.createCell(90);
		cell.setCellValue("Last Mile NRC List Price");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(91);
		cell.setCellValue("Last Mile NRC Discount");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(92);
		cell.setCellValue("Last Mile NRC Sale Price");
		cell.setCellStyle(headerEditableColor);
		cell = subHeaderRow.createCell(93);
		cell.setCellValue("Last Mile ARC List Price");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(94);
		cell.setCellValue("Last Mile ARC Discount");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(95);
		cell.setCellValue("Last Mile ARC Sale Price");
		cell.setCellStyle(headerEditableColor);

		/*cell = subHeaderRow.createCell(98);
		cell.setCellValue("Burstable ARC List Price");
		cell = subHeaderRow.createCell(99);
		cell.setCellValue("Burstable ARC Discount Price");
		cell = subHeaderRow.createCell(100);
		cell.setCellValue("Burstable ARC Sale Price");*/
		
		cell = subHeaderRow.createCell(96);
		cell.setCellValue("Link Management Charges ARC List Price");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(97);
		cell.setCellValue("Link Management Charges ARC Discount");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(98);
		cell.setCellValue("Link Management Charges ARC Sale Price");
		cell.setCellStyle(headerEditableColor);
		
//		cell = subHeaderRow.createCell(100);
//		cell.setCellValue("Mast NRC List Price");
//		cell.setCellStyle(headerCellNonEditable);
//		cell = subHeaderRow.createCell(101);
//		cell.setCellValue("Mast NRC Discount Price");
//		cell.setCellStyle(headerEditableColor);
//		cell = subHeaderRow.createCell(102);
//		cell.setCellValue("Mast NRC Sale Price");
//		cell.setCellStyle(headerEditableColor);
		cell = subHeaderRow.createCell(99);
		cell.setCellValue("TOTAL NRC Sale Price");
		cell.setCellStyle(headerCellNonEditable);
		cell = subHeaderRow.createCell(100);
		cell.setCellValue("TOTAL ARC Sale Price");
		cell.setCellStyle(headerCellNonEditable);

		/*
		 * Approval Comments
		 */
		Cell headerCellEleven = headerRow.createCell(101);
		headerCellEleven.setCellValue("Approval Comments");
		CellStyle cellStyleEleven = headerRow.getSheet().getWorkbook().createCellStyle();
		cellStyleEleven.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleEleven.setFont(font);
		headerCellEleven.setCellStyle(cellStyleNine);
		sheet.addMergedRegion(CellRangeAddress.valueOf("CX1:CZ1"));
		/*
		 * cell = subHeaderRow.createCell(103); 
		 * cell.setCellValue("Existing ARC Check (MAC cases)");
		 * cell = subHeaderRow.createCell(104); 
		 * cell.setCellValue("Existing CPE Check (MAC Cases)");
		 * cell =subHeaderRow.createCell(105); //
		 * cell.setCellValue("Offline Approval Available");
		 * 
		 */
		
		/* site level action */
		cell = subHeaderRow.createCell(101);
		cell.setCellValue("Site Level Action");
		cell.setCellStyle(headerEditableColor);

		cell = subHeaderRow.createCell(102);
		cell.setCellValue("Commercial Manager Comments");
		cell.setCellStyle(headerEditableColor);
		
		cell = subHeaderRow.createCell(103);
		cell.setCellValue("Existing Commercial Manager Comments");
		cell.setCellStyle(headerCellNonEditable);
		
		List<String> siteLevelList = new ArrayList<>();
		siteLevelList.add("Approve");
		siteLevelList.add("Reject");
		
		 /* profile validation begins */
        DataValidation profileDataValidation = null;
        DataValidationConstraint profileConstraint = null;
        DataValidationHelper profileValidationHelper = null;
        profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        Integer firstRow = 2;
        Integer lastRow = nplNdeMultiSiteInputs.size()+1;
        Integer siteLevelActionsColumn = 101; // As the column index of Site Level Action is 103
        Integer lastColumn = 104;
        CellRangeAddressList profileAddressList = new CellRangeAddressList(firstRow,lastRow , siteLevelActionsColumn, siteLevelActionsColumn);
        // add the selected profiles
        profileConstraint = profileValidationHelper
                .createExplicitListConstraint(siteLevelList.stream().toArray(String[]::new));
        profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
        profileDataValidation.setSuppressDropDownArrow(true);
        profileDataValidation.setShowErrorBox(true);
        profileDataValidation.setShowPromptBox(true);
        sheet.addValidationData(profileDataValidation);

		int rowCount[] = {2};
		int index[] = {2};
		LOGGER.info("NplNdeMultiSiteInputs size: "+nplNdeMultiSiteInputs.size());
		
		if (!nplNdeMultiSiteInputs.isEmpty()) {
			nplNdeMultiSiteInputs.stream().forEach(aBook -> {
				Row row = sheet.createRow(rowCount[0]);
				writeSiteInformation(aBook, row, index[0]);
				index[0]++;
				rowCount[0]++;
			});
		}
		
		for(int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
		     sheet.autoSizeColumn(columnIndex);
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "NPL_NDE Report.xlsx";
		response.reset();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Exception occured NPL NDE download report");
		}
		outByteStream.close();
		
	}catch (Exception e) {
		LOGGER.error("Exception occured returnNplNdeMultisiteReportExcel: ");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * This method is to write the excel for npl-nde report
	 * @author 4016226
	 */

	private void writeSiteInformation(NplNdeMultiSiteInputs aBook, Row row, int index) {
		LOGGER.info("Calling writeSiteInformation method :");

		
		CellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
		Cell cell = row.createCell(0);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getLinkId())?aBook.getAddressDetailsMultiSite().getLinkId(): Integer.parseInt(""));
		cellStyle.setLocked(true);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(1);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getLinkCode())?aBook.getAddressDetailsMultiSite().getLinkCode(): "");
		cellStyle.setLocked(true);
		cell.setCellStyle(cellStyle);
		/* Address Details (A-End) */
		cell = row.createCell(2);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getAddressAEnd())?aBook.getAddressDetailsMultiSite().getAddressAEnd(): "");
		cellStyle.setLocked(true);
		cell.setCellStyle(cellStyle);

		cell = row.createCell(3);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getCountryAEnd())?aBook.getAddressDetailsMultiSite().getCountryAEnd(): "");

		/* Address Details (B-End) */

		cell = row.createCell(4);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getAddressBEnd())?aBook.getAddressDetailsMultiSite().getAddressBEnd(): "");
		cellStyle.setLocked(true);
		cell.setCellStyle(cellStyle);

		cell = row.createCell(5);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getCountryBEnd())?aBook.getAddressDetailsMultiSite().getCountryBEnd(): "");
		

		/*
		 * Existing Component (Only for MAC/PR)
		 *  */
		if (aBook.getMacdFlag()) {
			cell = row.createCell(6);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getServiceId())?aBook.getExistingMacdComponents().getServiceId():"");//Service Id

			cell = row.createCell(7);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getPortBandWidth())?aBook.getExistingMacdComponents().getPortBandWidth(): "");

			cell = row.createCell(8);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getLocalLoopBandwidth())
					?aBook.getExistingMacdComponents().getLocalLoopBandwidth()
					: "");

			cell = row.createCell(9);
			cell.setCellValue(""); //// End to End Chargeable Distance (Km)

			cell = row.createCell(10);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getLmProvider())?aBook.getExistingMacdComponents().getLmProvider():""); // LM Provider A-End needs to get

			cell = row.createCell(11);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getLmProvider())?aBook.getExistingMacdComponents().getLmProvider():""); // LM Provider A-End needs to get

			cell = row.createCell(12);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getCpeManagmentType())?aBook.getExistingMacdComponents().getCpeManagmentType(): "");

			cell = row.createCell(13);
			cell.setCellValue("");// cpe support type

			cell = row.createCell(14);
			cell.setCellValue("");// cpe model A-End

			cell = row.createCell(15);
			cell.setCellValue("");// cpe model B-End

			cell = row.createCell(16);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getCurrency())?aBook.getExistingMacdComponents().getCurrency(): "");

			cell = row.createCell(17);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getExistingArc())?aBook.getExistingMacdComponents().getExistingArc():"");// existing arc

			cell = row.createCell(18);// existing nrc
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getExistingNrc())?aBook.getExistingMacdComponents().getExistingNrc():"");

			cell = row.createCell(19);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getContractTerm())?aBook.getExistingMacdComponents().getContractTerm(): "");

			cell = row.createCell(20);// commission date
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getCommisionDate())?aBook.getExistingMacdComponents().getCommisionDate():"");

			cell = row.createCell(21);// expiry date
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getExpiryDate())?aBook.getExistingMacdComponents().getExpiryDate(): "");
		}
		

		/* 
		 * ATTRIBUTES - BASIC
		 * */
		cell = row.createCell(22);// product
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getProduct())?aBook.getMultiSiteBasicAttributes().getProduct():"");
		
		cell = row.createCell(23);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getOrderType())?aBook.getMultiSiteBasicAttributes().getOrderType():"");
		
		cell = row.createCell(24);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getInterfaceType())?aBook.getMultiSiteBasicAttributes().getInterfaceType():"");
		
		cell = row.createCell(25);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getPortBandwidth())?aBook.getMultiSiteBasicAttributes().getPortBandwidth():"");
		
		cell = row.createCell(26);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getLocalLoopBandwidth())?aBook.getMultiSiteBasicAttributes().getLocalLoopBandwidth():"");
		
		cell = row.createCell(27);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCpeManagementType())?aBook.getMultiSiteBasicAttributes().getCpeManagementType():"");
		
		cell = row.createCell(28);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getServiceVariant())?aBook.getMultiSiteBasicAttributes().getServiceVariant():"");
		
//		cell = row.createCell(28);
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getChargeableDistance())?aBook.getMultiSiteBasicAttributes().getChargeableDistance():"");

		
		
		/* 
		 * ATTRIBUTES - Advanced
		 * */
		cell = row.createCell(29);//Service type
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getServiceType())?aBook.getMultiSiteBasicAttributes().getServiceType():"");
		cell = row.createCell(30);//Burstable Bandwidth
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getBurstableBandwidth())?aBook.getMultiSiteBasicAttributes().getBurstableBandwidth():"");
		cell = row.createCell(31);//Shared Last Mile
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getSharedLastMile())?aBook.getMultiSiteBasicAttributes().getSharedLastMile():"");
		cell = row.createCell(32);//Shared Last Mile Service ID
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getSharedLastMileServiceId())?aBook.getMultiSiteBasicAttributes().getSharedLastMileServiceId():"");
		cell = row.createCell(33);//Shared CPE
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getSharedCpe())?aBook.getMultiSiteBasicAttributes().getSharedCpe():"");
		cell = row.createCell(34);//Shared CPE Service ID
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getSharedCpeServiceId())?aBook.getMultiSiteBasicAttributes().getSharedCpeServiceId():"");
		
		/* 
		 * Feasibility A-End {Table:link_feasibility}
		 * */
		cell = row.createCell(35);//Response Type
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityType())?aBook.getMultiSiteFeasibility().getFeasibilityType():"");
		cell = row.createCell(36);//FEASIBILITY STATUS  
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityStatus())?aBook.getMultiSiteFeasibility().getFeasibilityStatus():"");
		cell = row.createCell(37);//FEASIBILITY TYPE
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityType())?aBook.getMultiSiteFeasibility().getFeasibilityType():"");
		cell = row.createCell(38);//FEASIBILITY Validity
		cell.setCellValue("");
		cell = row.createCell(39);////FEASIBILITY CODE
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityCode())?aBook.getMultiSiteFeasibility().getFeasibilityCode():"");
		cell = row.createCell(40);//FEASIBILITY MODE
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityMode())?aBook.getMultiSiteFeasibility().getFeasibilityMode():"");
//		cell = row.createCell(40);
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityType())?aBook.getMultiSiteFeasibility().getFeasibilityType():"");
//		cell = row.createCell(42);
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getType())?aBook.getMultiSiteFeasibility().getType():"");
		cell = row.createCell(41);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProvider())?aBook.getMultiSiteFeasibility().getProvider():"");
		cell = row.createCell(42);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityStatus())?aBook.getMultiSiteFeasibility().getFeasibilityStatus():"");
		cell = row.createCell(43);//CD
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getCd())?aBook.getMultiSiteFeasibility().getCd():"");
		cell = row.createCell(44);//TCL POP
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getTclPop())?aBook.getMultiSiteFeasibility().getTclPop():"");
		
		cell = row.createCell(45);//f check
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityCheck())?aBook.getMultiSiteFeasibility().getFeasibilityCheck():"");
		
		cell = row.createCell(46);//CONNECTED BUILDING
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getConnectedBuilding())?aBook.getMultiSiteFeasibility().getConnectedBuilding():"");
		cell = row.createCell(47);//CONNECTED CUSTOMER
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getConnectedCustomer())?aBook.getMultiSiteFeasibility().getConnectedCustomer():"");
		cell = row.createCell(48);//OSP DIST
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOspDistance())?aBook.getMultiSiteFeasibility().getOspDistance():"");
		cell = row.createCell(49);//OSP CAPEX
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOspCapex())?aBook.getMultiSiteFeasibility().getOspCapex():"");
		cell = row.createCell(50);//IN BUILDING CAPEX 
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getInBuildingCapex())?aBook.getMultiSiteFeasibility().getInBuildingCapex():"");
		cell = row.createCell(51);//MUX COST
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMuxCost())?aBook.getMultiSiteFeasibility().getMuxCost():"");
		cell = row.createCell(52); //PROW VALUE (OTC)
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProwValueOtc())?aBook.getMultiSiteFeasibility().getProwValueOtc():"");
		cell = row.createCell(53);//PROW VALUE (ARC)
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProwValueArc())?aBook.getMultiSiteFeasibility().getProwValueArc():"");
		cell = row.createCell(54);//Mast Height (Meter)
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMastHeight())?aBook.getMultiSiteFeasibility().getMastHeight():"");
		cell = row.createCell(55);//Mast NRC
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMastNrc())?aBook.getMultiSiteFeasibility().getMastNrc():"");
		cell = row.createCell(56);//Offnet NRC
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOffnetNrc())?aBook.getMultiSiteFeasibility().getOffnetNrc():"");
		cell = row.createCell(57);//Offnet ARC
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOffnetArc())?aBook.getMultiSiteFeasibility().getOffnetArc():"");
		cell = row.createCell(58);//F Remarks
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityRemarks())?aBook.getMultiSiteFeasibility().getFeasibilityRemarks():"");
		
		/* 
		 * Feasibility B-End {Table:link_feasibility}
		 * */
		cell = row.createCell(59);//FEASIBILITY STATUS
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityStatus())?aBook.getMultiSiteFeasibility().getFeasibilityStatus():"");
		cell = row.createCell(60);//FEASIBILITY TYPE-B
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityTypeb())?aBook.getMultiSiteFeasibility().getFeasibilityTypeb():"");
		cell = row.createCell(61);//FEASIBILITY Validity
		cell.setCellValue("");
		cell = row.createCell(62);//FEASIBILITY CODE
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityCode())?aBook.getMultiSiteFeasibility().getFeasibilityCode():"");
		cell = row.createCell(63);//FEASIBILITY MODE-B
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityModeb())?aBook.getMultiSiteFeasibility().getFeasibilityModeb():"");
//		cell = row.createCell(66);
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityTypeb())?aBook.getMultiSiteFeasibility().getFeasibilityTypeb():"");
//		cell = row.createCell(67);
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityType())?aBook.getMultiSiteFeasibility().getFeasibilityType():"");
		cell = row.createCell(64);//PROVIDER-B
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProviderb())?aBook.getMultiSiteFeasibility().getProviderb():"");
		cell = row.createCell(65);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityStatus())?aBook.getMultiSiteFeasibility().getFeasibilityStatus():"");
		cell = row.createCell(66);//CD
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getCdB())?aBook.getMultiSiteFeasibility().getCdB():"");
		cell = row.createCell(67);//TCL POP
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getTclBPop())?aBook.getMultiSiteFeasibility().getTclBPop():"");
		
		cell = row.createCell(68);//Feasibility Check
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityCheck())?aBook.getMultiSiteFeasibility().getFeasibilityCheck():"");
		
		//cell.setCellValue("");
		cell = row.createCell(69);//CONNECTED BUILDING
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getConnectedBBuilding())?aBook.getMultiSiteFeasibility().getConnectedBBuilding():"");
		cell = row.createCell(70);//CONNECTED CUSTOMER
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getConnectedBCustomer())?aBook.getMultiSiteFeasibility().getConnectedBCustomer():"");
		cell = row.createCell(71);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOspBDistance())?aBook.getMultiSiteFeasibility().getOspBDistance():"");//OSP DIST
		cell = row.createCell(72);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOspBCapex())?aBook.getMultiSiteFeasibility().getOspBCapex():"");//OSP CAPEX
		cell = row.createCell(73);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getInBuildingBCapex())?aBook.getMultiSiteFeasibility().getInBuildingBCapex():"");//IN BUILDING CAPEX 
		cell = row.createCell(74);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMuxCostB())?aBook.getMultiSiteFeasibility().getMuxCostB():"");//MUX COST
		cell = row.createCell(75);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProwValueBOtc())?aBook.getMultiSiteFeasibility().getProwValueBOtc():"");//PROW VALUE (OTC)
		cell = row.createCell(76);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProwValueBArc())?aBook.getMultiSiteFeasibility().getProwValueBArc():"");//PROW VALUE (ARC)
		cell = row.createCell(77);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMastBHeight())?aBook.getMultiSiteFeasibility().getMastBHeight():"");//Mast Height (Meter)
		cell = row.createCell(78);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMastBNrc())?aBook.getMultiSiteFeasibility().getMastBNrc():"");//Mast NRC
		cell = row.createCell(79);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOffnetBNrc())?aBook.getMultiSiteFeasibility().getOffnetBNrc():"");//Offnet NRC
		cell = row.createCell(80);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOffnetBArc())?aBook.getMultiSiteFeasibility().getOffnetBArc():"");//Offnet ARC
		cell = row.createCell(81);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityRemarks())?aBook.getMultiSiteFeasibility().getFeasibilityRemarks():"");//F B Remarks
		
		/** Pricing */
		
		cell = row.createCell(82);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getContractTerm())?aBook.getMultiSitePricingAttributes().getContractTerm():"");
		
		cell = row.createCell(83);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCurrency())?aBook.getMultiSitePricingAttributes().getCurrency():"");

		cell = row.createCell(84);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortNrcListPrice())?aBook.getMultiSitePricingAttributes().getPortNrcListPrice():0.0);// port nrc list price
		
		cell = row.createCell(86);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortNrcSalePrice())?aBook.getMultiSitePricingAttributes().getPortNrcSalePrice():0.0);
		
		cell = row.createCell(85);
		String nrcDiscountFormula = "IFERROR(1-"+ row.getCell(86).getAddress().formatAsString() + "/" + row.getCell(84).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(nrcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortNrcDiscount())?aBook.getMultiSitePricingAttributes().getPortNrcDiscount():0.0);
		
		cell = row.createCell(87);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortArcListPrice())?aBook.getMultiSitePricingAttributes().getPortArcListPrice():0.0);
		
		cell = row.createCell(89);//Port ARC Sale Price
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortArcSalePrice())?aBook.getMultiSitePricingAttributes().getPortArcSalePrice():0.0);
		
		cell = row.createCell(88);
		String arcDiscountFormula = "IFERROR(1-"+ row.getCell(89).getAddress().formatAsString() + "/" + row.getCell(87).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(arcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortArcDiscount())?aBook.getMultiSitePricingAttributes().getPortArcDiscount():0.0);	
		
		/** pricing A-End */
		cell = row.createCell(90);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileNrcListPrice())?aBook.getMultiSitePricingAttributes().getLastMileNrcListPrice():0.0);//Last Mile NRC List Price
		
		cell = row.createCell(92); //Last Mile NRC Sale Price
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileNrcSalePrice())?aBook.getMultiSitePricingAttributes().getLastMileNrcSalePrice():0.0);
		
		cell = row.createCell(91);//Last Mile NRC Discount
		String lastMileNrcDiscountFormula = "IFERROR(1-"+ row.getCell(92).getAddress().formatAsString() + "/" + row.getCell(90).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(lastMileNrcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileNrcDiscount())?aBook.getMultiSitePricingAttributes().getLastMileNrcDiscount():0.0);
		
		cell = row.createCell(93); //Last Mile ARC List Price
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileArcListPrice())?aBook.getMultiSitePricingAttributes().getLastMileArcListPrice():0.0);

		cell = row.createCell(95);//Last Mile ARC Sale Price
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileArcSalePrice())?aBook.getMultiSitePricingAttributes().getLastMileArcSalePrice():0.0);
		
		cell = row.createCell(94); //Last Mile ARC Discount
		String lastMileArcDiscountFormula = "IFERROR(1-"+ row.getCell(95).getAddress().formatAsString() + "/" + row.getCell(93).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(lastMileArcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileArcDiscount())?aBook.getMultiSitePricingAttributes().getLastMileArcDiscount():0.0);
		/** Burstable ARC List Price*/
		
//		cell = row.createCell(98);
//		cell.setCellValue("");//Burstable ARC List Price
//		
//		cell = row.createCell(99); //Burstable ARC Discount Price
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getBurstableArcDiscountPrice())?aBook.getMultiSitePricingAttributes().getBurstableArcDiscountPrice():0.0);
//		
//		cell = row.createCell(100); //Burstable ARC Sale Price
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getBurstableArcSalePrice())?aBook.getMultiSitePricingAttributes().getBurstableArcSalePrice():0.0);
		
		cell = row.createCell(96); //Link Management Charges ARC List Price
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcListPrice())?aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcListPrice():0.0); 
		
		cell = row.createCell(98); //Link Management Charges ARC Sale Price
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcSalePrice())?aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcSalePrice():0.0);
		
		cell = row.createCell(97); //Link Management Charges ARC Discount
		String linkManagementChargesArcDiscountFormula = "IFERROR(1-"+ row.getCell(98).getAddress().formatAsString() + "/" + row.getCell(96).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(linkManagementChargesArcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcDiscount())?aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcDiscount():0.0);
//		cell = row.createCell(96); //Link Management Charges ARC List Price
//		cell.setCellValue(""); 
//		
//		cell = row.createCell(97); //Link Management Charges ARC Discount
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcDiscount())?aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcDiscount():0.0);
//		
//		cell = row.createCell(98); //Link Management Charges ARC Sale Price
//		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcSalePrice())?aBook.getMultiSitePricingAttributes().getLinkManagementChargesArcSalePrice():0.0);
		
		cell = row.createCell(99); //TOTAL NRC Sale Price
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getTotalNrcSalePrice())?aBook.getMultiSitePricingAttributes().getTotalNrcSalePrice():0.0);
		
		cell = row.createCell(100); //TOTAL ARC Sale Price
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getTotalArcSalePrice())?aBook.getMultiSitePricingAttributes().getTotalArcSalePrice():0.0);
		
		/** Approval Comments */
//		cell = row.createCell(103);
//		cell.setCellValue("");//Existing ARC Check (MAC cases)
//		
//		cell = row.createCell(104);
//		cell.setCellValue("");// Existing CPE Check (MAC Cases) 
//		
//		cell = row.createCell(105);
//		cell.setCellValue("");// Offline Approval Available
		
		cell = row.createCell(101);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getSiteLevelActions())?aBook.getMultiSiteFeasibility().getSiteLevelActions():"Approve");// Site Level Action 
		
		cell = row.createCell(102);
		cell.setCellValue("");// Commercial Manager Comments
		

		
		cell = row.createCell(103);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCommercialManagerComments())?aBook.getMultiSitePricingAttributes().getCommercialManagerComments():" ");// Commercial Manager Comments
		
	}

	/**
	 * This method is NplNde site related information based on quoteCode
	 * 
	 * @author 4016226-Sobhan
	 * @param response
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	private List<NplNdeMultiSiteInputs> getNplNdeSiteRelatedInformation(String quoteCode,
			List<NplNdeMultiSiteInputs> nplNdeSiteInfoList) throws TclCommonException {
		LOGGER.info("Calling getNplNdeSiteRelatedInformation method:{} ");
		try {
			List<QuoteNplLink> quoteNplLinks = new ArrayList<QuoteNplLink>();
		LOGGER.info("Quote code : " + quoteCode);
		List<Integer> quoteProductComponentIds = new ArrayList<Integer>();
		Quote quoteEntity = quoteRepository.findByQuoteCode(quoteCode);
		Integer quoteID = quoteEntity.getId();
		LOGGER.info("QuoteID:" + quoteID);  
		List<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findByQuote_Id(quoteEntity.getId());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteToLeEntity.get(0).getId());
		LOGGER.info("QuoteToLeProductFamily:" + quoteToLeProductFamily.getId());
		List<ProductSolution> productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		LOGGER.info("ProductSolutions" + productSolutions.get(0).getId());

		
		
		String quoteBulkUpdate = null;
		quoteBulkUpdate = quoteToLeEntity.get(0).getQuoteBulkUpdate();
		if(Objects.isNull(quoteBulkUpdate)) {
			List<QuoteNplLink> quoteNpllinksResponse = nplLinkRepository.findByQuoteIdAndStatus(quoteID, (byte) 1);
			LOGGER.info("quoteNpllinks::" + quoteNpllinksResponse.toString() + "Size: " + quoteNpllinksResponse.size());
			quoteNplLinks.addAll(quoteNpllinksResponse);
		}
		
		if(Objects.nonNull(quoteBulkUpdate) && quoteBulkUpdate.equals("1")) {
			List<QuoteNplLink> quoteNpllinksResponse = nplLinkRepository.findByQuoteIdAndStatus(quoteID, (byte) 1);
			LOGGER.info("quoteNpllinks::" + quoteNpllinksResponse.toString() + "Size: " + quoteNpllinksResponse.size());
			List<QuoteNplLink> quoteNplLinkForApproval = new ArrayList<QuoteNplLink>();
			List<QuoteNplLink> quoteNplLinkForRejection = new ArrayList<QuoteNplLink>();
			
			quoteNplLinkForApproval = quoteNpllinksResponse.stream().filter(quoteNplLink -> quoteNplLink.getCommercialApproveStatus().equals("1")).collect(Collectors.toList());
			quoteNplLinkForRejection = quoteNpllinksResponse.stream().filter(quoteNplLink -> quoteNplLink.getCommercialRejectionStatus().equals("1")).collect(Collectors.toList());
			quoteNplLinks.addAll(quoteNplLinkForApproval);
			quoteNplLinks.addAll(quoteNplLinkForRejection);
			quoteNplLinkForApproval.clear();
			quoteNplLinkForRejection.clear();
			if(quoteNplLinks.isEmpty()) {
				quoteNplLinks.addAll(quoteNpllinksResponse);
			}
		}
		String productFamily = null;
		/** 
		 * NPL-NDE check
		 * */
		if (quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("NPL")) {
			productFamily = "NPL";
		} else if (quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("NDE")) {
			productFamily = "NDE";
		}
		List<String> basicAttributesList = new ArrayList<String>();
		List<String> componentsList = new ArrayList<String>();
		Map<String, Integer> attributesMap = new HashMap<String, Integer>();
		Map<String, Integer> componentsMap = new HashMap<String, Integer>();
		basicAttributesList = getBasicAttributesList();
		componentsList = getNplComponentsList(); 
		attributesMap = getAttributesMap(basicAttributesList);
		componentsMap = getComponentsMap(componentsList);

		/** 
		 * quote npl links check 
		 * */
		if (Objects.nonNull(quoteNplLinks) && !quoteNplLinks.isEmpty()) {
			for (QuoteNplLink links : quoteNplLinks) {
				NplNdeMultiSiteInputs nplNdeMultiSiteInputs = new NplNdeMultiSiteInputs();
				AddressDetailsMultiSite addressDetailsMultiSite = new AddressDetailsMultiSite();
				ExistingMacdComponents existingMacdComponents = new ExistingMacdComponents();
				MultiSiteBasicAttributes multiSiteBasicAttributes = new MultiSiteBasicAttributes();
				MultiSiteFeasibility multiSiteFeasibility = new MultiSiteFeasibility();
				MultiSitePricingAttributes multiSitePricingAttributes = new MultiSitePricingAttributes();
				 
				 Integer siteA = links.getSiteAId();
				 Integer siteB = links.getSiteBId();
				 LOGGER.info("SiteA ID:{} :"+siteA);
				 LOGGER.info("SiteB ID:{} :"+siteB);
				 
				boolean siteAFlag = true;
				boolean siteBFlag = false;
				
				/** 
				 * Address Details(A-End) 
				 * */
				addressDetailsMultiSite = getAddressDetailsMultiSite(addressDetailsMultiSite, siteA,siteAFlag,siteBFlag,links); // List of and Site A and siteB  here.
				LOGGER.info("AddressDetailsMultiSiteList"+addressDetailsMultiSite.toString());

				List<Integer> referenceId = Arrays.asList(links.getId(),links.getSiteAId());
				List<String> referenceName = Arrays.asList("NPL_LINK", "NPL_SITES");
				
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository.findByReferenceIdInAndReferenceNameInAndMstProductFamily(referenceId, referenceName, quoteToLeProductFamily.getMstProductFamily() );
				
				for (QuoteProductComponent quoteProductComponentsOne : quoteProductComponents) {
					quoteProductComponentIds.add(quoteProductComponentsOne.getId());
				}
				LOGGER.info("QuoteProductComponentIds::" + quoteProductComponentIds.size());
				List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponentId(quoteProductComponentIds);
				/**
				 *  MACD attributes if its a macd order 
				 * */
				if (Objects.nonNull(quoteToLeEntity.get(0).getQuoteType())
						&& quoteToLeEntity.get(0).getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {

					/* 
					 * Existing Component (Only for MAC/PR)
					 * */
					existingMacdComponents = getExistingMacdComponents(quoteEntity, quoteToLeEntity, links,quoteProdCompAttributeValues, quoteProductComponents, attributesMap, componentsMap);
					nplNdeMultiSiteInputs.setMacdFlag(true);
					nplNdeMultiSiteInputs.setExistingMacdComponents(existingMacdComponents);
					LOGGER.info("ExistingMacdComponents: "+existingMacdComponents.toString());

				}
				/** 
				 * Basic attribute--need to add product and chargeableDistance [product_attribute_master]
				 *  */

				multiSiteBasicAttributes = getMultiSiteBasicAttributesInfo(quoteToLeEntity,links, quoteProdCompAttributeValues, attributesMap);
				multiSiteBasicAttributes.setProduct(productFamily);
				nplNdeMultiSiteInputs.setMultiSiteBasicAttributes(multiSiteBasicAttributes);
				  
				/** feasibility-(A-End and B-End) -- need to add type,CD and Response Type [no DB column-site_feasibility;].*/
				  multiSiteFeasibility = getFeasibilityAttributes(links,siteAFlag,siteBFlag);
				  nplNdeMultiSiteInputs.setMultiSiteFeasibility(multiSiteFeasibility);

				  /** pricing */
				  multiSitePricingAttributes = getPricingAttributes(quoteEntity,quoteToLeEntity, quoteProductComponents, quoteProdCompAttributeValues,links,attributesMap,componentsMap,productFamily,multiSiteFeasibility);
				  nplNdeMultiSiteInputs.setMultiSitePricingAttributes(multiSitePricingAttributes);	
				  
				  /**
				   * This method for site B-End information related...
				   *  */
				  nplNdeMultiSiteInputs= getSiteBRelatedInformation(quoteEntity,quoteToLeEntity,nplNdeMultiSiteInputs,siteB,addressDetailsMultiSite,multiSiteFeasibility,links);
					
				  LOGGER.info("NplNdeMultiSiteInputs List : "+nplNdeMultiSiteInputs.toString());
				  nplNdeSiteInfoList.add(nplNdeMultiSiteInputs);
				LOGGER.info("nplNdeSiteInfoList:{} "+nplNdeSiteInfoList.size());
				/*clear the quoteProductComponentsIds list */
				quoteProductComponentIds.clear();
			}
		}
	}catch (Exception e) {
			LOGGER.error("Filed getNplNdeSiteRelatedInformation method :: {} ", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return nplNdeSiteInfoList;
	}

	/* 
	 * site B-End
	 * */
	private NplNdeMultiSiteInputs getSiteBRelatedInformation(Quote quoteEntity, List<QuoteToLe> quoteToLeEntity,NplNdeMultiSiteInputs nplNdeMultiSiteInputs, Integer siteB, AddressDetailsMultiSite addressDetailsMultiSite,
			MultiSiteFeasibility multiSiteFeasibility, QuoteNplLink links) throws TclCommonException {
			LOGGER.info("Calling getSiteBRelatedInformation method:start{}");
		try {
			boolean siteAFlag = false;
			boolean siteBFlag = true;
		/** Address Details(B-End) */
		addressDetailsMultiSite = getAddressDetailsMultiSite(addressDetailsMultiSite, siteB,siteAFlag,siteBFlag,links); // List of and Site A and siteB  here.
		LOGGER.info("AddressDetailsMultiSiteList"+addressDetailsMultiSite.toString());
		nplNdeMultiSiteInputs.setAddressDetailsMultiSite(addressDetailsMultiSite);
		LOGGER.info("NplNdeMultiSiteInputs List : "+nplNdeMultiSiteInputs.toString());
		LOGGER.info("Ending getSiteBRelatedInformation method:return {nplNdeMultiSiteInputs}::");
	} catch (Exception e) {
			LOGGER.info("failed getSiteBRelatedInformation method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return nplNdeMultiSiteInputs;
	}


	private MultiSitePricingAttributes getPricingAttributes(Quote quoteEntity, List<QuoteToLe> quoteToLeEntity,
			List<QuoteProductComponent> quoteProductComponents,
			List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues, QuoteNplLink links,
			Map<String, Integer> attributesMap, Map<String, Integer> componentsMap, String productFamily, MultiSiteFeasibility multiSiteFeasibility) throws TclCommonException {
		MultiSitePricingAttributes multiSitePricingAttributes = new MultiSitePricingAttributes();
		try {
			LOGGER.info("Callling getPricingAttributes:start{}");
			multiSitePricingAttributes.setContractTerm(
					Objects.nonNull(quoteToLeEntity.get(0).getTermInMonths()) ? quoteToLeEntity.get(0).getTermInMonths()
							: "");
			multiSitePricingAttributes.setCurrency(
					Objects.nonNull(quoteToLeEntity.get(0).getCurrencyCode()) ? quoteToLeEntity.get(0).getCurrencyCode()
							: "");


			Result response = new Result();
			PricingEngineResponse pricingDetails = pricingDetailsRepository
					.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(links.getLinkCode(), "Link");
			if(Objects.nonNull(pricingDetails)) {
				String pricingResponse = pricingDetails.getResponseData();
				try {
					response = (Result) Utils.convertJsonToObject(pricingResponse, Result.class);
					LOGGER.info(" linkCode {} " , links.getLinkCode());
					LOGGER.info("inside getPricingAttributes response {} " ,response.toString());

					multiSitePricingAttributes.setPortNrcListPrice(Objects.nonNull(response.getNPLPortNRCAdjusted())?Utils.parseDouble(response.getNPLPortNRCAdjusted()):0.0);
					multiSitePricingAttributes.setPortArcListPrice(Objects.nonNull(response.getPortARCLP())?Utils.parseDouble(response.getPortARCLP()):0.0);
					LOGGER.info(" getPricingAttributes PortNrcListPrice {} " , multiSitePricingAttributes.getPortNrcListPrice());
					LOGGER.info(" getPricingAttributes PortArcListPrice {} " , multiSitePricingAttributes.getPortArcListPrice());

					Double aLmNrcBwOnwl = Objects.nonNull(response.getaLmNrcBwOnwl())?Utils.parseDouble(response.getaLmNrcBwOnwl()):0.0;
					Double bLmNrcBwOnwl = Objects.nonNull(response.getbLmNrcBwOnwl())?Utils.parseDouble(response.getbLmNrcBwOnwl()):0.0;
					Double aLmNrcBwOnrf = Objects.nonNull(response.getaLmNrcBwOnrf())?Utils.parseDouble(response.getaLmNrcBwOnrf()):0.0;
					Double bLmNrcBwOnrf = Objects.nonNull(response.getbLmNrcBwOnrf())?Utils.parseDouble(response.getbLmNrcBwOnrf()):0.0;
					Double aLmNrcBwProvOfrf = Objects.nonNull(response.getaLmNrcBwProvOfrf())?Utils.parseDouble(response.getaLmNrcBwProvOfrf()):0.0;
					Double bLmNrcBwProvOfrf = Objects.nonNull(response.getbLmNrcBwProvOfrf())?Utils.parseDouble(response.getbLmNrcBwProvOfrf()):0.0;
					Double lastMileNrcListPrice = aLmNrcBwOnwl + bLmNrcBwOnwl + aLmNrcBwOnrf + bLmNrcBwOnrf + aLmNrcBwProvOfrf + bLmNrcBwProvOfrf;
					LOGGER.info(" getPricingAttributes lastMileNrcListPrice {} " , lastMileNrcListPrice);

					multiSitePricingAttributes.setLastMileNrcListPrice(lastMileNrcListPrice);

					Double aLmArcBwOnwl = Objects.nonNull(response.getaLmArcBwOnwl())?Utils.parseDouble(response.getaLmArcBwOnwl()):0.0;
					Double bLmArcBwOnwl = Objects.nonNull(response.getbLmArcBwOnwl())?Utils.parseDouble(response.getbLmArcBwOnwl()):0.0;
					Double aLmArcBwProvOfrf = Objects.nonNull(response.getaLmArcBwProvOfrf())?Utils.parseDouble(response.getaLmArcBwProvOfrf()):0.0;
					Double bLmArcBwProvOfrf = Objects.nonNull(response.getbLmArcBwProvOfrf())?Utils.parseDouble(response.getbLmArcBwProvOfrf()):0.0;
					Double aLmArcBwOnrf = Objects.nonNull(response.getaLmArcBwOnrf())?Utils.parseDouble(response.getaLmArcBwOnrf()):0.0;
					Double bLmArcBwOnrf = Objects.nonNull(response.getbLmArcBwOnrf())?Utils.parseDouble(response.getbLmArcBwOnrf()):0.0;
					Double lastMileArcListPrice = aLmArcBwOnwl + bLmArcBwOnwl + aLmArcBwProvOfrf + bLmArcBwProvOfrf + aLmArcBwOnrf + bLmArcBwOnrf;
					LOGGER.info(" getPricingAttributes lastMileArcListPrice " + lastMileArcListPrice);

					multiSitePricingAttributes.setLastMileArcListPrice(lastMileArcListPrice);

					Double aLmNrcMastOnrf = Objects.nonNull(response.getaLmNrcMastOnrf())?Utils.parseDouble(response.getaLmNrcMastOnrf()):0.0;
					Double aLmNrcMastOfrf = Objects.nonNull(response.getaLmNrcMastOfrf())?Utils.parseDouble(response.getaLmNrcMastOfrf()):0.0;
					Double mastNrcListPrice = aLmNrcMastOnrf + aLmNrcMastOfrf;
					LOGGER.info(" getPricingAttributes mastNrcListPrice " + mastNrcListPrice);

					multiSitePricingAttributes.setMastNrcListPrice(mastNrcListPrice);

					multiSitePricingAttributes.setCpeInstallNrcListPrice(0.0);//CPE Install NRC List Price
					multiSitePricingAttributes.setCpeOutrightSaleNRCListPrice(0.0);//CPE Outright Sale NRC List Price
					multiSitePricingAttributes.setCpeRentalArcListPrice(0.0);//CPE Rental ARC List Price
					multiSitePricingAttributes.setCpeManagementArcListPrice(0.0);//

					if(("NPL").equalsIgnoreCase(response.getProductName())) {
						LOGGER.info("inside getPricingAttributes if NPL " + response.getProductName());
						
						Double pALmNrcBwOnwl = Objects.nonNull(response.getpALmNrcBwOnwl())?Utils.parseDouble(response.getpALmNrcBwOnwl()):0.0;
						Double pBLmNrcBwOnwl = Objects.nonNull(response.getPBLmNrcBwOnwl())?Utils.parseDouble(response.getPBLmNrcBwOnwl()):0.0;
						Double pALmNrcBwOnrf = Objects.nonNull(response.getpALmNrcBwOnrf())?Utils.parseDouble(response.getpALmNrcBwOnrf()):0.0;
						Double pBLmNrcBwOnrf = Objects.nonNull(response.getpBLmNrcBwOnrf())?Utils.parseDouble(response.getpBLmNrcBwOnrf()):0.0;
						Double pALmNrcBwProvOfrf = Objects.nonNull(response.getpALmNrcBwProvOfrf())?Utils.parseDouble(response.getpALmNrcBwProvOfrf()):0.0;
						Double pBLmNrcBwProvOfrf = Objects.nonNull(response.getpBLmNrcBwProvOfrf())?Utils.parseDouble(response.getpBLmNrcBwProvOfrf()):0.0;
						Double lmNrcListPrice = pALmNrcBwOnwl + pBLmNrcBwOnwl + pALmNrcBwOnrf + pBLmNrcBwOnrf + pALmNrcBwProvOfrf + pBLmNrcBwProvOfrf;
						LOGGER.info(" getPricingAttributes NPL lastMileNrcListPrice {} " , lmNrcListPrice);

						multiSitePricingAttributes.setLastMileNrcListPrice(lmNrcListPrice);

						Double pALmArcBwOnwl = Objects.nonNull(response.getpALmArcBwOnwl())?Utils.parseDouble(response.getpALmArcBwOnwl()):0.0;
						Double pBLmArcBwOnwl = Objects.nonNull(response.getPBLmArcBwOnwl())?Utils.parseDouble(response.getPBLmArcBwOnwl()):0.0;
						Double pALmArcBwProvOfrf = Objects.nonNull(response.getpALmArcBwProvOfrf())?Utils.parseDouble(response.getpALmArcBwProvOfrf()):0.0;
						Double pBLmArcBwProvOfrf = Objects.nonNull(response.getpBLmArcBwProvOfrf())?Utils.parseDouble(response.getpBLmArcBwProvOfrf()):0.0;
						Double pALmArcBwOnrf = Objects.nonNull(response.getpALmArcBwOnrf())?Utils.parseDouble(response.getpALmArcBwOnrf()):0.0;
						Double pBLmArcBwOnrf = Objects.nonNull(response.getpBLmArcBwOnrf())?Utils.parseDouble(response.getpBLmArcBwOnrf()):0.0;
						Double lmArcListPrice = pALmArcBwOnwl + pBLmArcBwOnwl + pALmArcBwProvOfrf + pBLmArcBwProvOfrf + pALmArcBwOnrf + pBLmArcBwOnrf;
						LOGGER.info(" inside npl lmArcListPrice {} " , lmArcListPrice);
						multiSitePricingAttributes.setLastMileArcListPrice(lmArcListPrice);

						Double pALmNrcMastOnrf = Objects.nonNull(response.getpALmNrcMastOnrf())?Utils.parseDouble(response.getpALmNrcMastOnrf()):0.0;
						Double pALmNrcMastOfrf = Objects.nonNull(response.getpALmNrcMastOfrf())?Utils.parseDouble(response.getpALmNrcMastOfrf()):0.0;
						Double mastNrcListPriceNpl = pALmNrcMastOnrf + pALmNrcMastOfrf;
						LOGGER.info(" inside npl mastNrcListPriceNpl {} " , mastNrcListPriceNpl);
						multiSitePricingAttributes.setMastNrcListPrice(mastNrcListPriceNpl);

					}

					if((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(response.getQuotetypeQuote())) {
						LOGGER.info("inside MACD_QUOTE_TYPE " + response.getQuotetypeQuote());
		                multiSitePricingAttributes.setPortArcListPrice(0.0);
						Double fALmNrcBwOnwl = Objects.nonNull(response.getFALmNrcBwOnwl())?Utils.parseDouble(response.getFALmNrcBwOnwl()):0.0;
						Double fBLmNrcBwOnwl = Objects.nonNull(response.getFBLmNrcBwOnwl())?Utils.parseDouble(response.getFBLmNrcBwOnwl()):0.0;
						Double fAaLmNrcBwOnrf = Objects.nonNull(response.getfALmNrcBwOnrf())?Utils.parseDouble(response.getfALmNrcBwOnrf()):0.0;
						Double fBLmNrcBwOnrf = Objects.nonNull(response.getfBLmNrcBwOnrf())?Utils.parseDouble(response.getfBLmNrcBwOnrf()):0.0;
						Double fALmNrcBwProvOfrf = Objects.nonNull(response.getfALmNrcBwProvOfrf())?Utils.parseDouble(response.getfALmNrcBwProvOfrf()):0.0;
						Double fBLmNrcBwProvOfrf = Objects.nonNull(response.getfBLmNrcBwProvOfrf())?Utils.parseDouble(response.getfBLmNrcBwProvOfrf()):0.0;
						Double lmNrcListPrice = fALmNrcBwOnwl + fBLmNrcBwOnwl + fAaLmNrcBwOnrf + fBLmNrcBwOnrf + fALmNrcBwProvOfrf + fBLmNrcBwProvOfrf;
						LOGGER.info(" inside MACD_QUOTE_TYPE lmNrcListPrice {} " , lmNrcListPrice);

						multiSitePricingAttributes.setLastMileNrcListPrice(lmNrcListPrice);

						Double fALmArcBwOnwl = Objects.nonNull(response.getFALmArcBwOnwl())?Utils.parseDouble(response.getFALmArcBwOnwl()):0.0;
						Double fBLmArcBwOnwl = Objects.nonNull(response.getFBLmArcBwOnwl())?Utils.parseDouble(response.getFBLmArcBwOnwl()):0.0;
						Double fAaLmArcBwOnrf = Objects.nonNull(response.getfALmArcBwOnrf())?Utils.parseDouble(response.getfALmNrcBwOnrf()):0.0;
						Double fBLmArcBwOnrf = Objects.nonNull(response.getfBLmArcBwOnrf())?Utils.parseDouble(response.getfBLmArcBwOnrf()):0.0;
						Double fALmArcBwProvOfrf = Objects.nonNull(response.getfALmArcBwProvOfrf())?Utils.parseDouble(response.getfALmArcBwProvOfrf()):0.0;
						Double fBLmArcBwProvOfrf = Objects.nonNull(response.getfBLmArcBwProvOfrf())?Utils.parseDouble(response.getfBLmArcBwProvOfrf()):0.0;
						Double lmArcListPrice = fALmArcBwOnwl + fBLmArcBwOnwl + fAaLmArcBwOnrf + fBLmArcBwOnrf + fALmArcBwProvOfrf + fBLmArcBwProvOfrf;
						LOGGER.info(" inside MACD_QUOTE_TYPE lmArcListPrice {} " , lmArcListPrice);

						multiSitePricingAttributes.setLastMileArcListPrice(lmArcListPrice);

						Double fALmNrcMastOnrf = Objects.nonNull(response.getfALmNrcMastOnrf())?Utils.parseDouble(response.getfALmNrcMastOnrf()):0.0;
						Double fALmNrcMastOfrf = Objects.nonNull(response.getfALmNrcMastOfrf())?Utils.parseDouble(response.getfALmNrcMastOfrf()):0.0;
						Double mastNrcListPriceNpl = fALmNrcMastOnrf + fALmNrcMastOfrf;
						LOGGER.info(" inside MACD_QUOTE_TYPE mastNrcListPriceNpl {} " , mastNrcListPriceNpl);

						multiSitePricingAttributes.setMastNrcListPrice(mastNrcListPriceNpl);
					}


				}catch (TclCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


		Integer internetPortId = componentsMap.get("National Connectivity");
		Integer lastMileId = componentsMap.get("Last mile");
		Integer linkManagementChargesId = componentsMap.get("Link Management Charges");

		for (QuoteProductComponent quoteProductComponent : quoteProductComponents) {
			if (quoteProductComponent.getMstProductComponent().getId() == internetPortId) {
				Optional<QuotePrice> quotePrice = Optional
						.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("COMPONENTS",
								quoteProductComponent.getId().toString(), quoteEntity.getId()));
				if (quotePrice.isPresent()) {
					multiSitePricingAttributes
							.setPortNrcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentNrc())
									? quotePrice.get().getDiscountPercentNrc()
									: 0.0);
					multiSitePricingAttributes.setPortNrcSalePrice(
							Objects.nonNull(quotePrice.get().getEffectiveNrc()) ? quotePrice.get().getEffectiveNrc()
									: 0.0);
					multiSitePricingAttributes
							.setPortArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc())
									? quotePrice.get().getDiscountPercentArc()
									: 0.0);
					multiSitePricingAttributes.setPortArcSalePrice(
							Objects.nonNull(quotePrice.get().getEffectiveArc()) ? quotePrice.get().getEffectiveArc()
									: 0.0);
				}
			}

			if (quoteProductComponent.getMstProductComponent().getId() == lastMileId) {
					Optional<QuotePrice> quotePrice = Optional
							.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("COMPONENTS",
									quoteProductComponent.getId().toString(), quoteEntity.getId()));
					if (quotePrice.isPresent()) {
						multiSitePricingAttributes
						.setLastMileNrcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentNrc())
								? quotePrice.get().getDiscountPercentNrc()
										: 0.0);
						multiSitePricingAttributes.setLastMileNrcSalePrice(
								Objects.nonNull(quotePrice.get().getEffectiveNrc()) ? quotePrice.get().getEffectiveNrc()
										: 0.0);
						multiSitePricingAttributes
						.setLastMileArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc())
								? quotePrice.get().getDiscountPercentArc()
										: 0.0);
						multiSitePricingAttributes.setLastMileArcSalePrice(
								Objects.nonNull(quotePrice.get().getEffectiveArc()) ? quotePrice.get().getEffectiveArc()
										: 0.0);
					}
			}

			if (quoteProductComponent.getMstProductComponent().getId() == linkManagementChargesId) {
				Optional<QuotePrice> quotePrice = Optional
						.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("COMPONENTS",
								quoteProductComponent.getId().toString(), quoteEntity.getId()));
				if (quotePrice.isPresent()) {
					multiSitePricingAttributes.setLinkManagementChargesArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc()) ? quotePrice.get().getDiscountPercentArc(): 0.0);
					multiSitePricingAttributes.setLinkManagementChargesArcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveArc()) ? quotePrice.get().getEffectiveArc(): 0.0);
				}
			}
		}
		Integer mastCostId = attributesMap.get("Mast Cost");
		Integer burstableBandWidthId = attributesMap.get("Burstable Bandwidth");
		Integer cpeInstallId = attributesMap.get("CPE Discount Install");
		Integer cpeDiscountId = attributesMap.get("CPE Discount Outright Sale");
		Integer cpeRentalId = attributesMap.get("CPE Discount Rental");
		Integer cpeManagementId = attributesMap.get("CPE Discount Management");

		for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProdCompAttributeValues) {
			Integer attributeId = quoteProductComponentsAttributeValue.getProductAttributeMaster().getId();
			if (attributeId == mastCostId) {
				Optional<QuotePrice> quotePrice = Optional
						.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",
								quoteProductComponentsAttributeValue.getId().toString(), quoteEntity.getId()));
				if (quotePrice.isPresent()) {
					multiSitePricingAttributes
							.setMastNrcDiscountPrice(Objects.nonNull(quotePrice.get().getEffectiveUsagePrice())
									? quotePrice.get().getEffectiveUsagePrice()
									: 0.0);
					multiSitePricingAttributes
							.setMastNrcSalesPrice(Objects.nonNull(quotePrice.get().getEffectiveUsagePrice())
									? quotePrice.get().getEffectiveUsagePrice()
									: 0.0);
				}
			}

			if (attributeId == burstableBandWidthId) {
				Optional<QuotePrice> quotePrice = Optional
						.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",
								quoteProductComponentsAttributeValue.getId().toString(), quoteEntity.getId()));
				if (quotePrice.isPresent()) {
					multiSitePricingAttributes
							.setBurstableArcDiscountPrice(Objects.nonNull(quotePrice.get().getDiscountInPercent())
									? quotePrice.get().getDiscountInPercent()
									: 0.0);
					multiSitePricingAttributes
							.setBurstableArcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveUsagePrice())
									? quotePrice.get().getEffectiveUsagePrice()
									: 0.0);
				}
			}

			if (attributeId == cpeInstallId) {
				Optional<QuotePrice> quotePrice = Optional
						.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",
								quoteProductComponentsAttributeValue.getId().toString(), quoteEntity.getId()));
				if (quotePrice.isPresent()) {
					multiSitePricingAttributes
							.setCpeInstallNrcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentNrc())
									? quotePrice.get().getDiscountPercentNrc()
									: 0.0);
					multiSitePricingAttributes.setCpeInstallNRCSalePrice(
							Objects.nonNull(quotePrice.get().getEffectiveNrc()) ? quotePrice.get().getEffectiveNrc()
									: 0.0);
				}
			}

			if (attributeId == cpeDiscountId) {
				Optional<QuotePrice> quotePrice = Optional
						.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",
								quoteProductComponentsAttributeValue.getId().toString(), quoteEntity.getId()));
				if (quotePrice.isPresent()) {
					multiSitePricingAttributes
							.setCpeOutrightSaleNrcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentNrc())
									? quotePrice.get().getDiscountPercentNrc()
									: 0.0);
					multiSitePricingAttributes.setCpeOutrightSaleNrcSalePrice(
							Objects.nonNull(quotePrice.get().getEffectiveNrc()) ? quotePrice.get().getEffectiveNrc()
									: 0.0);
				}
			}

			if (attributeId == cpeRentalId) {
				Optional<QuotePrice> quotePrice = Optional
						.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",
								quoteProductComponentsAttributeValue.getId().toString(), quoteEntity.getId()));
				if (quotePrice.isPresent()) {
					multiSitePricingAttributes
							.setCpeRentalArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc())
									? quotePrice.get().getDiscountPercentArc()
									: 0.0);
					multiSitePricingAttributes.setCpeRentalArcSalePrice(
							Objects.nonNull(quotePrice.get().getEffectiveArc()) ? quotePrice.get().getEffectiveArc()
									: 0.0);
				}
			}

			if (attributeId == cpeManagementId) {
				Optional<QuotePrice> quotePrice = Optional
						.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",
								quoteProductComponentsAttributeValue.getId().toString(), quoteEntity.getId()));
				if (quotePrice.isPresent()) {
					multiSitePricingAttributes
							.setCpeManagementArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc())
									? quotePrice.get().getDiscountPercentArc()
									: 0.0);
					multiSitePricingAttributes.setCpeManagementArcSalePrice(
							Objects.nonNull(quotePrice.get().getEffectiveArc()) ? quotePrice.get().getEffectiveArc()
									: 0.0);
				}
			}

			
		}
		
		Double portNrcSalePrice = Objects.nonNull(multiSitePricingAttributes.getPortNrcSalePrice())?multiSitePricingAttributes.getPortNrcSalePrice():0.0;
		Double lastMileNrcSalePrice = Objects.nonNull(multiSitePricingAttributes.getLastMileNrcSalePrice())?multiSitePricingAttributes.getLastMileNrcSalePrice():0.0;
		
		Double totalNrcSalePrice = portNrcSalePrice + lastMileNrcSalePrice;
		
		Double portArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getPortArcSalePrice())?multiSitePricingAttributes.getPortArcSalePrice():0.0;
		Double lastMileArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getLastMileArcSalePrice())?multiSitePricingAttributes.getLastMileArcSalePrice():0.0;
		Double linkManagementChargesArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getLinkManagementChargesArcSalePrice())?multiSitePricingAttributes.getLinkManagementChargesArcSalePrice():0.0;
		
		Double totalArcSalePrice = portArcSalePrice + lastMileArcSalePrice + 
				linkManagementChargesArcSalePrice;
		
		multiSitePricingAttributes.setTotalNrcSalePrice(totalNrcSalePrice);
		multiSitePricingAttributes.setTotalArcSalePrice(totalArcSalePrice);
		
		LOGGER.info("BEFORE binding existing manager comments linkid::"+links.getId()+"productFamily:"+productFamily);
		   MstProductComponent mstProductComponents = mstProductComponentRepository.findByName("SITE_PROPERTIES");
		   MstProductFamily mstProductFamily = mstProductFamilyRepository
					.findByNameAndStatus(productFamily, (byte) 1);
		if (mstProductComponents != null && mstProductFamily != null) {
			List<QuoteProductComponent> siteQuoteProductCommentComponents = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndMstProductFamily(links.getId(), mstProductComponents,
							mstProductFamily);

			if (siteQuoteProductCommentComponents != null && !siteQuoteProductCommentComponents.isEmpty()) {
				LOGGER.info("-------inside product compo site prop--------");
				List<QuoteProductComponentsAttributeValue> siteQuoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent(siteQuoteProductCommentComponents.get(0));
				if (!siteQuoteProductComponentsAttributeValues.isEmpty()) {
					String approverCommentsOne = null;
					String approverCommentsTwo = null;
					String approverCommentsThree = null;
					String rejectorCommentsOne = null;
					String rejectorCommentsTwo = null;
					String rejectorCommentsThree = null;
					for (QuoteProductComponentsAttributeValue productComponentsAttributeValue : siteQuoteProductComponentsAttributeValues) {

						if (productComponentsAttributeValue.getProductAttributeMaster().getName()
								.equalsIgnoreCase("COMMERCIAL_APPROVER1_COMMENTS")) {
							approverCommentsOne = productComponentsAttributeValue.getAttributeValues();
						}
						if (productComponentsAttributeValue.getProductAttributeMaster().getName()
								.equalsIgnoreCase("COMMERCIAL_APPROVER2_COMMENTS")) {
							approverCommentsTwo = productComponentsAttributeValue.getAttributeValues();
						}
						if (productComponentsAttributeValue.getProductAttributeMaster().getName()
								.equalsIgnoreCase("COMMERCIAL_APPROVER3_COMMENTS")) {
							approverCommentsThree = productComponentsAttributeValue.getAttributeValues();
						}

						if (productComponentsAttributeValue.getProductAttributeMaster().getName()
								.equalsIgnoreCase("COMMERCIAL_REJECTOR1_COMMENTS")) {
							rejectorCommentsOne = productComponentsAttributeValue.getAttributeValues();
						}
						if (productComponentsAttributeValue.getProductAttributeMaster().getName()
								.equalsIgnoreCase("COMMERCIAL_REJECTOR3_COMMENTS")) {
							rejectorCommentsThree = productComponentsAttributeValue.getAttributeValues();
						}
						if (productComponentsAttributeValue.getProductAttributeMaster().getName()
								.equalsIgnoreCase("COMMERCIAL_REJECTOR2_COMMENTS")) {
							rejectorCommentsTwo = productComponentsAttributeValue.getAttributeValues();
						}

					}
					
					multiSitePricingAttributes.setCommercialManagerComments(((Objects.nonNull(approverCommentsOne)&& !approverCommentsOne.isEmpty())
							? "C1 ApproveComments :" + approverCommentsOne+" "
							: "")
							+ ((Objects.nonNull(approverCommentsTwo) && !approverCommentsTwo.isEmpty()) ? " C2 ApproveComments :" + approverCommentsTwo+" "
									: "")
							+ ((Objects.nonNull(approverCommentsThree) && !approverCommentsThree.isEmpty() )
									? " C3 ApproveComments :" + approverCommentsThree+" "
									: "")
							+ ((Objects.nonNull(rejectorCommentsOne) && !rejectorCommentsOne.isEmpty()) ? " C1 RejectComments : " + rejectorCommentsOne+" "
									: "")
							+ ((Objects.nonNull(rejectorCommentsTwo) && !rejectorCommentsTwo.isEmpty()) ? " C2 RejectComments : " + rejectorCommentsTwo+" "
									: "")
							+ ((Objects.nonNull(rejectorCommentsThree) && !rejectorCommentsThree.isEmpty() )
									? " C3 RejectComments : " + rejectorCommentsThree+" "
									: ""));
				}
			  }
			}
			
			LOGGER.info("EXISITING MANAGER COMMENTS::::"+multiSitePricingAttributes.getCommercialManagerComments());
		
	}	catch (Exception e) {
			LOGGER.info("failed getPricingAttributes method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("Ending getPricingAttributes:returns {multiSitePricingAttributes}");
		return multiSitePricingAttributes;
	}


	private MultiSiteFeasibility getFeasibilityAttributes(QuoteNplLink links, boolean siteAFlag, boolean siteBFlag) throws TclCommonException {
		MultiSiteFeasibility multiSiteFeasibility = new MultiSiteFeasibility();
		List<LinkFeasibility> linkFeasibilityList = null;
		try {
			LOGGER.info("Calling getFeasibilityAttributes method:start{} ");
		//List<LinkFeasibility> linkFeasibilityList = linkFeasibilityRepository.findByQuoteNplLinkAndIsSelected(links,(byte)1);
			linkFeasibilityList = linkFeasibilityRepository.findByQuoteNplLinkAndIsSelected(links,(byte)1);
			Feasible linkFeasiblity = new Feasible();
		LOGGER.info("LinkFeasibilityList:{}:: "+linkFeasibilityList.toString());
		Optional<QuoteNplLink> nplLink = nplLinkRepository.findById(links.getId());
		LOGGER.info("QuoteNplLink in feasibility attributes:: "+nplLink.toString());
		
		/** Feasibility A-End- Type A*/
		LOGGER.info("Calling Feasibility A-End- Type A :{}");
		if(!CollectionUtils.isEmpty(linkFeasibilityList) && Objects.nonNull(linkFeasibilityList)) {
			try {
				String feasibleLinkResponse = linkFeasibilityList.get(0).getResponseJson();
				LOGGER.info("Getting feasibility Json response");
				linkFeasiblity = (Feasible) Utils.convertJsonToObject(feasibleLinkResponse, Feasible.class);
			}catch (TclCommonException e) {
				e.printStackTrace();
			}
			
		}
		
		multiSiteFeasibility.setFeasibilityStatus(nplLink.get().getFpStatus());//FEASIBILITY STATUS
		multiSiteFeasibility.setFeasibilityType(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityType():"");
		multiSiteFeasibility.setFeasibilityValidity("");
		multiSiteFeasibility.setFeasibilityCode(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityCode():"");//FEASIBILITY CODE
		multiSiteFeasibility.setFeasibilityMode(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityMode():"");//FEASIBILITY MODE
		multiSiteFeasibility.setFeasibilityType(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityType():"");//FEASIBILITY TYPE
		multiSiteFeasibility.setType(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityType():"");//Type --primary/secondary
		multiSiteFeasibility.setProvider(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getProvider():"");//PROVIDER --TCL MAN
		multiSiteFeasibility.setFeasibilityStatus(nplLink.get().getFpStatus());
		multiSiteFeasibility.setCd(Objects.nonNull(linkFeasiblity.getAPOPDISTKMSERVICEMOD())?linkFeasiblity.getAPOPDISTKMSERVICEMOD():"");//CD
		multiSiteFeasibility.setTclPop(Objects.nonNull(linkFeasiblity.getAPopName())?linkFeasiblity.getAPopName():"");
		//Feasibility check
		multiSiteFeasibility.setFeasibilityCheck(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityCheck():"");
		multiSiteFeasibility.setConnectedBuilding(Objects.nonNull(linkFeasiblity.getAConnectedBuildingTag())?linkFeasiblity.getAConnectedBuildingTag():"");// string or integer
		multiSiteFeasibility.setConnectedCustomer(Objects.nonNull(linkFeasiblity.getAConnectedCustTag())?linkFeasiblity.getAConnectedCustTag():"");// string or integer
		multiSiteFeasibility.setOspDistance(Objects.nonNull(linkFeasiblity.getAMinHhFatg())?linkFeasiblity.getAMinHhFatg():"");// confirm from feasible response
		multiSiteFeasibility.setOspCapex(Objects.nonNull(linkFeasiblity.getALmNrcOspcapexOnwl())?linkFeasiblity.getALmNrcOspcapexOnwl():""); //lm_nrc_ospcapex_onwl
		multiSiteFeasibility.setInBuildingCapex(Objects.nonNull(linkFeasiblity.getALmNrcInbldgOnwl())?linkFeasiblity.getALmNrcInbldgOnwl():"");//lm_nrc_inbldg_onwl
		multiSiteFeasibility.setMuxCost(Objects.nonNull(linkFeasiblity.getALmNrcMuxOnwl())?linkFeasiblity.getALmNrcMuxOnwl():"");// lm_nrc_mux_onwl
		multiSiteFeasibility.setProwValueOtc(linkFeasiblity.getaLmNrcProwOnwl());//lm_nrc_prow_onwl
		multiSiteFeasibility.setProwValueArc(linkFeasiblity.getaLmArcProwOnwl());//lm_arc_prow_onwl
		if(multiSiteFeasibility.getFeasibilityMode().contains("OnnetRF") || multiSiteFeasibility.getFeasibilityMode().contains("Onnet Wireless")) {
			multiSiteFeasibility.setMastNrc(Objects.nonNull(linkFeasiblity.getALmNrcMastOnrf())?linkFeasiblity.getALmNrcMastOnrf():"");//lm_nrc_mast_onrf 
		}else if(multiSiteFeasibility.getFeasibilityMode().contains("OffnetRF") || multiSiteFeasibility.getFeasibilityMode().contains("Offnet Wireless")){
			multiSiteFeasibility.setMastNrc(Objects.nonNull(linkFeasiblity.getALmNrcMastOfrf())?linkFeasiblity.getALmNrcMastOfrf():"");
		}else {
			multiSiteFeasibility.setMastNrc("");
		}
//		if(multiSiteFeasibility.getFeasibilityMode().contains("OffnetRF") || multiSiteFeasibility.getFeasibilityMode().contains("Offnet Wireless")) {
//			multiSiteFeasibility.setMastHeight(Objects.nonNull(linkFeasiblity.getAv)?site.getAvgMastHt():0);
//		}else {
			// check for Wireless - RADWIN', 'Wireless - UBR PMP / WiMax
			multiSiteFeasibility.setMastHeight("");
	//	}
		multiSiteFeasibility.setOffnetNrc("");
		multiSiteFeasibility.setOffnetArc("");
		multiSiteFeasibility.setFeasibilityRemarks(Objects.nonNull(linkFeasiblity.getFeasibilityRemarks())?linkFeasiblity.getFeasibilityRemarks():"");
		LOGGER.info("Ending Feasibility A-End- Type A :{}");
		
		/** Feasibility B-End- Type B*/
		
		multiSiteFeasibility.setFeasibilityBStatus(nplLink.get().getFpStatus());//FEASIBILITY STATUS
		multiSiteFeasibility.setFeasibilityTypeb(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityTypeB():"");//feasibility_type_b
		multiSiteFeasibility.setFeasibilityBValidity("");
		multiSiteFeasibility.setFeasibilityBCode(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityCode():"");
		multiSiteFeasibility.setFeasibilityModeb(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityModeB():"");//feasibility_mode_b
		multiSiteFeasibility.setFeasibilityTypeb(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityTypeB():"");////feasibility_type_b
		multiSiteFeasibility.setType(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityType():"");//Type
		multiSiteFeasibility.setProviderb(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getProviderB():"");//PROVIDER --provider_b
		multiSiteFeasibility.setFeasibilityBStatus(nplLink.get().getFpStatus());
		multiSiteFeasibility.setCdB(Objects.nonNull(linkFeasiblity.getBPOPDISTKMSERVICEMOD())?linkFeasiblity.getBPOPDISTKMSERVICEMOD():"");//CD
		multiSiteFeasibility.setTclBPop(Objects.nonNull(linkFeasiblity.getBPopName())?linkFeasiblity.getBPopName():"");
		//Feasibility check
		multiSiteFeasibility.setFeasibilityBCheck(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getFeasibilityCheck():"");
		multiSiteFeasibility.setConnectedBBuilding(Objects.nonNull(linkFeasiblity.getBConnectedBuildingTag())?linkFeasiblity.getBConnectedBuildingTag():"");// string or integer
		multiSiteFeasibility.setConnectedBCustomer(Objects.nonNull(linkFeasiblity.getBConnectedCustTag())?linkFeasiblity.getBConnectedCustTag():"");// string or integer
		multiSiteFeasibility.setOspBDistance(Objects.nonNull(linkFeasiblity.getBMinHhFatg())?linkFeasiblity.getBMinHhFatg():"");// confirm from feasible response
		multiSiteFeasibility.setOspBCapex(Objects.nonNull(linkFeasiblity.getBLmNrcOspcapexOnwl())?linkFeasiblity.getBLmNrcOspcapexOnwl():""); //lm_nrc_ospcapex_onwl
		multiSiteFeasibility.setInBuildingBCapex(Objects.nonNull(linkFeasiblity.getBLmNrcInbldgOnwl())?linkFeasiblity.getBLmNrcInbldgOnwl():"");//lm_nrc_inbldg_onwl
		multiSiteFeasibility.setMuxCostB(Objects.nonNull(linkFeasiblity.getBLmNrcMuxOnwl())?linkFeasiblity.getBLmNrcMuxOnwl():"");// lm_nrc_mux_onwl
		multiSiteFeasibility.setProwValueBOtc(linkFeasiblity.getbLmNrcProwOnwl());//lm_nrc_prow_onwl
		multiSiteFeasibility.setProwValueBArc(linkFeasiblity.getbLmArcProwOnwl());//lm_arc_prow_onwl
		if(multiSiteFeasibility.getFeasibilityModeb().contains("OnnetRF") || multiSiteFeasibility.getFeasibilityModeb().contains("Onnet Wireless")) {
			multiSiteFeasibility.setMastNrc(Objects.nonNull(linkFeasiblity.getBLmNrcMastOnrf())?linkFeasiblity.getBLmNrcMastOnrf():"");//lm_nrc_mast_onrf 
		}else if(multiSiteFeasibility.getFeasibilityModeb().contains("OffnetRF") || multiSiteFeasibility.getFeasibilityModeb().contains("Offnet Wireless")){
			multiSiteFeasibility.setMastNrc(Objects.nonNull(linkFeasiblity.getBLmNrcMastOfrf())?linkFeasiblity.getBLmNrcMastOfrf():"");
		}else {
			multiSiteFeasibility.setMastNrc("");
		}
//		if(multiSiteFeasibility.getFeasibilityMode().contains("OffnetRF") || multiSiteFeasibility.getFeasibilityMode().contains("Offnet Wireless")) {
//			multiSiteFeasibility.setMastHeight(Objects.nonNull(linkFeasiblity.getAv)?site.getAvgMastHt():0);
//		}else {
			// check for Wireless - RADWIN', 'Wireless - UBR PMP / WiMax
			multiSiteFeasibility.setMastBHeight("");
	//	}
		multiSiteFeasibility.setOffnetBNrc("");
		multiSiteFeasibility.setOffnetBArc("");
		multiSiteFeasibility.setFeasibilityBRemarks("");
		String approveStatus = links.getCommercialApproveStatus();
		String rejectionStatus = links.getCommercialRejectionStatus();
		if(!(Objects.nonNull(approveStatus) && Objects.nonNull(rejectionStatus))) {
			multiSiteFeasibility.setSiteLevelActions("Approve");
		}
		
		if(Objects.nonNull(approveStatus)) {
			if(approveStatus.equals("1")) {
				multiSiteFeasibility.setSiteLevelActions("Approve");
			}
		}
		if(Objects.nonNull(approveStatus) && Objects.nonNull(rejectionStatus)) {
			if(approveStatus.equals("0") && rejectionStatus.equals("0")) {
				multiSiteFeasibility.setSiteLevelActions("Approve");
			}
		}
		if(Objects.nonNull(rejectionStatus)) {
			if(rejectionStatus.equals("1")) {
				multiSiteFeasibility.setSiteLevelActions("Reject");
			}
		}
		LOGGER.info("Site Level Action is "+ multiSiteFeasibility.getSiteLevelActions());
	}catch (Exception e) {
			LOGGER.info("failed getFeasibilityAttributes method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("Ending getFeasibilityAttributes method:returns{multiSiteFeasibility}::");
		return multiSiteFeasibility;
	}

	
	private List<String> getNplComponentsList() throws TclCommonException {
		LOGGER.info("Calling getNplComponentsList method");
		List<String> componentsList = new ArrayList<>();
		try {
			LOGGER.info("Callling getNplComponentsList Method:start{}");
		
		componentsList.add("National Connectivity");// It is a port
		componentsList.add("Link Management"); // Link Management Charges.This is management
		componentsList.add("Last mile"); // Last mile reference id is quote npl link site_A columan id.
		componentsList.add("Link Management Charges");
		}catch (Exception e) {
			LOGGER.error("failed getNplComponentsList method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("Ending getNplComponentsList method:{}");
		return componentsList;
	}

	private AddressDetailsMultiSite getAddressDetailsMultiSite(AddressDetailsMultiSite addressDetailsMultiSite,Integer site, boolean siteAFlag, boolean siteBFlag, QuoteNplLink links) {
		LOGGER.info("Calling getAddressDetailsMultiSite method:Start:{}");
		QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(site, (byte) 1);
			try {
				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,String.valueOf(quoteIllSite.getErfLocSitebLocationId()));
				LOGGER.info("LocationResponse:{} "+locationResponse);
				addressDetailsMultiSite.setLinkId(links.getId());
				addressDetailsMultiSite.setLinkCode(links.getLinkCode());
				if (locationResponse != null && !locationResponse.isEmpty()) {
					if(siteAFlag) {
					
					// Adding address details to nplNdeMultiSiteInputs.
						AddressDetail addressDetailAEnd = (AddressDetail) Utils.convertJsonToObject(locationResponse,AddressDetail.class);
						if(addressDetailAEnd != null) {
						addressDetailsMultiSite.setSiteId(quoteIllSite.getId());
						LOGGER.info("SiteA ID :" + quoteIllSite.getId());

						addressDetailsMultiSite.setAddressAEnd((Objects.nonNull(addressDetailAEnd.getAddressLineOne())?addressDetailAEnd.getAddressLineOne():"") + (Objects.nonNull(addressDetailAEnd.getAddressLineTwo())?addressDetailAEnd.getAddressLineTwo():""));
						addressDetailsMultiSite.setCountryAEnd(Objects.nonNull(addressDetailAEnd.getCountry())?addressDetailAEnd.getCountry():"");
						
						LOGGER.info("AddressDetailsMultiSiteList:{} "+addressDetailsMultiSite.toString());
						}
						LOGGER.info("Region for the locationId {} : {} ", quoteIllSite.getErfLocSitebLocationId(),addressDetailAEnd.getRegion());
					}
					
					if(siteBFlag) {
						// Adding address details to nplNdeMultiSiteInputs.
						AddressDetail addressDetailBEnd = (AddressDetail) Utils.convertJsonToObject(locationResponse,AddressDetail.class);
						if(addressDetailBEnd != null) {
						addressDetailsMultiSite.setSiteId(quoteIllSite.getId());
						LOGGER.info("SiteB ID :" + quoteIllSite.getId());

						//addressDetailsMultiSite.setAddressBEnd(addressDetailBEnd.getAddressLineOne() + addressDetailBEnd.getAddressLineTwo());
						//addressDetailsMultiSite.setCountryBEnd(addressDetailBEnd.getCountry());
						
						addressDetailsMultiSite.setAddressBEnd((Objects.nonNull(addressDetailBEnd.getAddressLineOne())?addressDetailBEnd.getAddressLineOne():"") + (Objects.nonNull(addressDetailBEnd.getAddressLineTwo())?addressDetailBEnd.getAddressLineTwo():""));
						addressDetailsMultiSite.setCountryBEnd(Objects.nonNull(addressDetailBEnd.getCountry())?addressDetailBEnd.getCountry():"");
						
						LOGGER.info("AddressDetailsMultiSiteList:{} "+addressDetailsMultiSite.toString());
						}
						LOGGER.info("Region for the locationId {} : {} ", quoteIllSite.getErfLocSitebLocationId(),addressDetailBEnd.getRegion());
					}
					
				} else {
					LOGGER.warn("Location data not found for the locationId {} ", quoteIllSite.getErfLocSitebLocationId());
				}
			} catch (Exception e) {
				LOGGER.error("Error occured at getAddressDetailsMultiSite "+e);
				throw new TclCommonRuntimeException(ExceptionConstants.GET_ATTRIBUTES_ERROR, e,ResponseResource.R_CODE_ERROR);
			}
		return addressDetailsMultiSite;
	}
	
	/** 
	 * This method is getBasicAttributesList
	 * @author 4016226
	 * @throws TclCommonException 
	 * */
	private List<String> getBasicAttributesList() throws TclCommonException {
		LOGGER.info("Calling getBasicAttributesList method:{} ");
		List<String> basicAttributesList = new ArrayList<String>();
		try {
			LOGGER.info("Callling getPricingAttributes:start{}");
		// basic attributes
		basicAttributesList.add("Interface Type - A end");
		basicAttributesList.add("Port Bandwidth");
		basicAttributesList.add("Port Type");
		basicAttributesList.add("Local Loop Bandwidth");
		basicAttributesList.add("CPE Management Type");
		basicAttributesList.add("Service Variant");
		//basicAttributesList.add("POP_DIST_KM_SERVICE_MOD");
		
		basicAttributesList.add("VPN Topology");
		basicAttributesList.add("Site Type");
		basicAttributesList.add("Resiliency");
		basicAttributesList.add("Access Required");
		// basicAttributesMap.put("CPE Support Type", 1); not there in product attribute
		// master
		basicAttributesList.add("Backup Configuration");
		basicAttributesList.add("IP Address Provided By");
		basicAttributesList.add("Dual Circuit");
		
		// pricing attributes
		basicAttributesList.add("Mast Cost");
		basicAttributesList.add("Additional Ips");
		basicAttributesList.add("Burstable Bandwidth");
		basicAttributesList.add("CPE Discount Install");
		basicAttributesList.add("CPE Discount Outright Sale");
		basicAttributesList.add("CPE Discount Rental");
		basicAttributesList.add("CPE Discount Management");

		// commercial manager comments 
		basicAttributesList.add("COMMERCIAL_APPROVER1_COMMENTS");
		basicAttributesList.add("COMMERCIAL_APPROVER2_COMMENTS");
		basicAttributesList.add("COMMERCIAL_APPROVER3_COMMENTS");
		basicAttributesList.add("COMMERCIAL_REJECTOR1_COMMENTS");
		basicAttributesList.add("COMMERCIAL_REJECTOR2_COMMENTS");
		basicAttributesList.add("COMMERCIAL_REJECTOR3_COMMENTS");
				
		// advanced attributes 
		basicAttributesList.add("Service type");
		basicAttributesList.add("Burstable Bandwidth");
		basicAttributesList.add("IP Address Arrangement");
		basicAttributesList.add("IPv4 Address Pool Size");
		basicAttributesList.add("IPv6 Address Pool Size");
		basicAttributesList.add("Shared Last Mile");
		basicAttributesList.add("Shared Last Mile Service ID");
		basicAttributesList.add("Shared CPE");
		basicAttributesList.add("Shared CPE Service ID");
		basicAttributesList.add("Additional IPs");
		
				
		}	catch (Exception e) {
			LOGGER.error("failed getBasicAttributesList method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("Ending getBasicAttributesList Method:{}");
		return basicAttributesList;
	}

	private Map<String, Integer> getAttributesMap(List<String> basicAttributesList) throws TclCommonException {
		LOGGER.info("Inside getAttributesMap method:{}");
		Map<String, Integer> attributesMap = new HashMap<String, Integer>();
		try {
		List<ProductAttributeMaster> productAttributeMasterList = productAttributeMasterRepository
				.findByNameIn(basicAttributesList);
		for (ProductAttributeMaster productAttributes : productAttributeMasterList) {
			attributesMap.put(productAttributes.getName(), productAttributes.getId());
		}
	}
		catch (Exception e) {
			LOGGER.error("failed getAttributesMap method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("Ending getAttributesMap method:{}");
		return attributesMap;
	}

	private Map<String, Integer> getComponentsMap(List<String> componentsList) throws TclCommonException {
		LOGGER.info("Calling getComponentsMap method :start :{}");
		Map<String, Integer> componentsMap = new HashMap<String, Integer>();
		try {
		List<MstProductComponent> mstProductComponent = mstProductComponentRepository.findByNameIn(componentsList);
		for (MstProductComponent component : mstProductComponent) {
			componentsMap.put(component.getName(), component.getId());
		}
	}	catch (Exception e) {
			LOGGER.error("failed getComponentsMap method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("Ending getComponentsMap method :{}");
		return componentsMap;
	}

	private ExistingMacdComponents getExistingMacdComponents(Quote quoteEntity, List<QuoteToLe> quoteToLeEntity,
			QuoteNplLink links, List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues,
			List<QuoteProductComponent> quoteProductComponents, Map<String, Integer> attributesMap,
			Map<String, Integer> componentsMap) throws TclCommonException {
		LOGGER.info("Calling the getExistingMacdComponents method:start{}");
		ExistingMacdComponents existingMacdComponents = new ExistingMacdComponents();

		try {
			LOGGER.info("Callling getExistingMacdComponents{}");
		/*
		 * List<Integer> sites = new ArrayList<Integer>(); Integer siteA =
		 * links.getSiteAId(); Integer siteB = links.getSiteBId(); sites.add(siteA);
		 * sites.add(siteB);
		 */

		//Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteSite((QuoteIllSite) sites,quoteToLeEntity.get(0));
		//need to change Macd repo for npl-nde
			Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteNplLink(links,quoteToLeEntity.get(0));
			LOGGER.info("serviceIds" + serviceIds);
		    String currentServiceId = serviceIds.get(MACDConstants.LINK);
		    LOGGER.info("currentServiceId" + currentServiceId);
		    
		
		
		LOGGER.info("Current Service Id" + currentServiceId );
		String[] site = {null};
		List<SIServiceDetailDataBean> serviceDetailList = null;
		if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLeEntity.stream().findFirst().get().getQuoteType())) {
			serviceDetailList = macdUtils.getServiceDetailNPLTermination(currentServiceId);
		} else {
		serviceDetailList = macdUtils.getServiceDetailNPL(currentServiceId);
		}
		if (serviceDetailList.size() != 0) {
			serviceDetailList.stream().forEach(serviceDetailbean -> {
				if (Objects.nonNull(serviceDetailbean)) {
					String serviceCommissionedDate = null;
					String oldContractTerm = null;
					String latLong = null;
					String serviceId = null;
					Integer serviceDetailId = null;
					String nrc=null;
					String arc=null;
					double mrc=0D;
					LOGGER.info("serviceDetailbean.getSiteType()" + serviceDetailbean.getSiteType() + "cdate:" + serviceDetailbean.getServiceCommissionedDate());
						serviceDetailId = serviceDetailbean.getId();
						oldContractTerm = serviceDetailbean.getContractTerm().toString();
						latLong = serviceDetailbean.getLatLong();
						serviceId = serviceDetailbean.getTpsServiceId();
						String bwUnitLl = serviceDetailbean.getLastmileBwUnit();
						String oldLlBw = serviceDetailbean.getLastmileBw();
						String oldPortBw = serviceDetailbean.getPortBw();
						String bwUnitPort = serviceDetailbean.getPortBwUnit();
						if(Objects.nonNull(serviceDetailbean.getArc())){
						    arc=String.valueOf(serviceDetailbean.getArc());
						}
						if(Objects.nonNull(serviceDetailbean.getNrc())){
						    nrc=String.valueOf(serviceDetailbean.getNrc());
						}
						Timestamp timestampServiceCommissionedDate = serviceDetailbean.getServiceCommissionedDate();
						LOGGER.info("oldPortBw conversion"+oldPortBw+"bwUnitPort"+bwUnitPort+"serviceDetailbean.getCircuitExpiryDate()"+serviceDetailbean.getCircuitExpiryDate());
						oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);
						LOGGER.info("oldPortBw after conversion"+oldPortBw+"timestampServiceCommissionedDate"+timestampServiceCommissionedDate);
						if (Objects.nonNull(timestampServiceCommissionedDate)) {
							serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd")
									.format(timestampServiceCommissionedDate.getTime());
						}
						
						existingMacdComponents
						.setPortBandWidth(Objects.nonNull(oldPortBw)
								? oldPortBw
								: "");
						
						
						
						existingMacdComponents.setServiceId(Objects.nonNull(serviceDetailbean.getTpsServiceId())?serviceDetailbean.getTpsServiceId():"");
						existingMacdComponents.setExistingArc(Objects.nonNull(arc)?arc:"");
						existingMacdComponents.setExistingNrc(Objects.nonNull(nrc)?nrc:"");
						existingMacdComponents.setContractTerm(Objects.nonNull(oldContractTerm)?oldContractTerm:"");
						existingMacdComponents.setLmProvider(Objects.nonNull(serviceDetailbean.getLastMileProvider())?serviceDetailbean.getLastMileProvider():"");
						existingMacdComponents.setCommisionDate(Objects.nonNull(serviceCommissionedDate)?serviceCommissionedDate.toString():"");
						LOGGER.info("oldLlBw local loop bw"+oldLlBw+"serviceCommissionedDate"+serviceCommissionedDate+"serviceDetailbean.getPortMode()"+serviceDetailbean.getPortMode()+"serviceDetailbean.getBillingCurrency()"
								+serviceDetailbean.getBillingCurrency());
						existingMacdComponents.setLocalLoopBandwidth(Objects.nonNull(oldLlBw)?oldLlBw:"");

					//	existingMacdComponents.setProduct(Objects.nonNull(serviceDetailbean.getErfPrdCatalogProductName())?serviceDetailbean.getErfPrdCatalogProductName():"");

						//existingMacdComponents.setProduct(Objects.nonNull(serviceDetailbean.getErfPrdCatalogProductName())?serviceDetailbean.getErfPrdCatalogProductName():"");
						existingMacdComponents.setPortType(Objects.nonNull(serviceDetailbean.getPortMode())?serviceDetailbean.getPortMode():"");
						existingMacdComponents.setCurrency(Objects.nonNull(serviceDetailbean.getBillingCurrency())?serviceDetailbean.getBillingCurrency():"");
						existingMacdComponents.setExpiryDate(Objects.nonNull(serviceDetailbean.getCircuitExpiryDate())?serviceDetailbean.getCircuitExpiryDate().toString():"");

						
						
				}
			});
		}
		
		
		
		}catch (Exception e) {
			LOGGER.error("failed getExistingMacdComponents method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("Ending the getExistingMacdComponents method:{}");
		return existingMacdComponents;
	}

	private MultiSiteBasicAttributes getMultiSiteBasicAttributesInfo(List<QuoteToLe> quoteToLeEntity,
			QuoteNplLink links, List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues,
			Map<String, Integer> attributesMap) throws TclCommonException {
		LOGGER.info("Calling getMultiSiteBasicAttributesInfo method: ");
		MultiSiteBasicAttributes multiSiteBasicAttributes = new MultiSiteBasicAttributes();
		try {
			LOGGER.info("Callling getMultiSiteBasicAttributesInfo{}");
		//Integer productId = attributesMap.get("Product"); //product
		multiSiteBasicAttributes.setOrderType(quoteToLeEntity.get(0).getQuoteType());
		Integer interfaceId = attributesMap.get("Interface Type - A end");
		Integer portBandWidthId = attributesMap.get("Port Bandwidth");
		Integer localLoopBandWidthId = attributesMap.get("Local Loop Bandwidth");
		Integer cpeManagementTypeId = attributesMap.get("CPE Management Type");
		Integer serviceVariantId = attributesMap.get("Service Variant");
		//Integer chargeableDistance= attributesMap.get("Chargeable Dist");//POP_DIST_KM_SERVICE_MOD
		
		Integer portTypeId = attributesMap.get("Port Type");
		/*
		 * Integer cosOneId = attributesMap.get("cos 1"); Integer cosTwoId =
		 * attributesMap.get("cos 2"); Integer cosThreeId = attributesMap.get("cos 3");
		 * Integer cosFourId = attributesMap.get("cos 4"); Integer cosFiveId =
		 * attributesMap.get("cos 5"); Integer cosSixId = attributesMap.get("cos 6");
		 */
		Integer vpnTopologyId = attributesMap.get("VPN Topology");
		Integer siteTypeId = attributesMap.get("Site Type");
		Integer resiliencyId = attributesMap.get("Resiliency");
		Integer accessRequiredId = attributesMap.get("Access Required");
		Integer backUpConfigurationId = attributesMap.get("Backup Configuration");
		Integer ipAddressProvidedId = attributesMap.get("IP Address Provided By");
		Integer dualCircuitId = attributesMap.get("Dual Circuit");
		Integer serviceTypeId = attributesMap.get("Service type");
		Integer burstableBandWidthId = attributesMap.get("Burstable Bandwidth");
		Integer ipAddressArrangementId = attributesMap.get("IP Address Arrangement");
		Integer iPv4AddressPoolSizeId = attributesMap.get("IPv4 Address Pool Size");
		Integer iPv6AddressPoolSizeId = attributesMap.get("IPv6 Address Pool Size");
		Integer sharedLastMileId = attributesMap.get("Shared Last Mile");
		Integer sharedLastMileServiceId = attributesMap.get("Shared Last Mile Service ID");
		Integer sharedCpeId = attributesMap.get("Shared CPE");
		Integer sharedCpeServiceId = attributesMap.get("Shared CPE Service ID");
		Integer additionalIpsId = attributesMap.get("Additional IPs");
		for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProdCompAttributeValues) {
			Integer attributeId = quoteProductComponentsAttributeValue.getProductAttributeMaster().getId();
			if (Objects.nonNull(attributeId)) {
				
				
				if (attributeId == interfaceId) {
					multiSiteBasicAttributes
							.setInterfaceType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == portBandWidthId) {
					multiSiteBasicAttributes
							.setPortBandwidth(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == portTypeId) {
					multiSiteBasicAttributes
							.setPortType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == localLoopBandWidthId) {
					multiSiteBasicAttributes.setLocalLoopBandwidth(
							Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == cpeManagementTypeId) {
					multiSiteBasicAttributes.setCpeManagementType(
							Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == serviceVariantId) {
					multiSiteBasicAttributes.setServiceVariant(
							Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == vpnTopologyId) {
					multiSiteBasicAttributes
							.setVpnTopology(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == siteTypeId) {
					multiSiteBasicAttributes
							.setSiteType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == resiliencyId) {
					multiSiteBasicAttributes
							.setResiliency(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == accessRequiredId) {
					multiSiteBasicAttributes.setAccessRequired(
							Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
//					if(attributeId==cpeSupportTypeId) {
//						multiSiteBasicAttributes.setCpeSupportType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
//					}
				if (attributeId == backUpConfigurationId) {
					multiSiteBasicAttributes.setBackUpConfiguration(
							Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == ipAddressProvidedId) {
					multiSiteBasicAttributes.setIpAddressProvidedBy(
							Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if (attributeId == dualCircuitId) {
					multiSiteBasicAttributes
							.setDualCircuit(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())
									? quoteProductComponentsAttributeValue.getAttributeValues()
									: "");
				}
				if(attributeId==serviceTypeId) {
					multiSiteBasicAttributes.setServiceType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==burstableBandWidthId) {
					multiSiteBasicAttributes.setBurstableBandwidth(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==ipAddressArrangementId) {
					multiSiteBasicAttributes.setIpAddressArrangement(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==iPv4AddressPoolSizeId) {
					multiSiteBasicAttributes.setIpV4AddressPoolSize(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==iPv6AddressPoolSizeId) {
					multiSiteBasicAttributes.setIpV6AddressPoolSize(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==sharedLastMileId) {
					multiSiteBasicAttributes.setSharedLastMile(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==sharedLastMileServiceId) {
					multiSiteBasicAttributes.setSharedLastMileServiceId(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==sharedCpeId) {
					multiSiteBasicAttributes.setSharedCpe(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==sharedCpeServiceId) {
					multiSiteBasicAttributes.setSharedCpeServiceId(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
				if(attributeId==additionalIpsId) {
					multiSiteBasicAttributes.setAddtionalIps(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
				}
			}
		}
	} catch (Exception e) {
			LOGGER.error("Failed getMultiSiteBasicAttributesInfo method:{}"+e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		LOGGER.info("Ending getMultiSiteBasicAttributesInfo method::returns{} "+multiSiteBasicAttributes.toString());
		return multiSiteBasicAttributes;
	}
	
	public Integer getTotalLinkCount(Integer quoteId) {
		LOGGER.info("Inside getTotalLinkCount quoteId{} ", quoteId);
		Integer totalLinkCount = 0;
		List<QuoteNplLink> nplLink = nplLinkRepository.findByQuoteIdAndStatus(quoteId, CommonConstants.BACTIVE);
		totalLinkCount = totalLinkCount + nplLink.size();
		return totalLinkCount;
	}




	private void constructOrderSiteServiceTerminationDetails(QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetails, OrderIllSiteToService orderIllSiteToService){
		LOGGER.info("Inside method constructOrderSiteServiceTerminationDetails for quoteSiteServiceTerminationDetails_id {} and order site to sevice id {}",
				quoteSiteServiceTerminationDetails.getId(), orderIllSiteToService.getId());

		if(Objects.nonNull(quoteSiteServiceTerminationDetails) && Objects.nonNull(orderIllSiteToService)){
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
	public String setBandwidthConversion(String bandwidth, String bandwidthUnit)
	{
		Double bandwidthValue=0D;
		Double bwidth = 0D;
		LOGGER.info("Bandwidth Value in setBandwidthConversion {}",bandwidth);
		LOGGER.info("Bandwidth Unit in setBandwidthConversion {}",bandwidthUnit);

		if(Objects.nonNull(bandwidth)&&Objects.nonNull(bandwidthUnit))
		{
			switch (bandwidthUnit.trim().toLowerCase())
			{
				case "kbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue / 1024;
					bandwidth = bandwidthValue.toString();
					break;
				}
				case "gbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue * 1000;
					bandwidth = bandwidthValue.toString();
					break;
				}
				case "Mbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue * 1;
					bandwidth = bandwidthValue.toString();
					break;
				}
				default:
					break;
			}

			int index=bandwidth.indexOf(".");
			if(index>0) {
				LOGGER.info("bandwidth value" + bandwidth);
				String precisions = bandwidth.substring(index + 1);
				LOGGER.info("precision value" + precisions);
				if (precisions.length() > 3) {
					DecimalFormat df = new DecimalFormat("#.###");
					df.setRoundingMode(RoundingMode.CEILING);
					String value = df.format(bandwidthValue);
					LOGGER.info("Formatted value" + value);
					bandwidth = value;
				}
			}
			if (Character.toString(bandwidth.charAt(index+1)).equalsIgnoreCase(CommonConstants.ZERO))
			{
				bwidth = Double.parseDouble(bandwidth.trim());
				Integer bw = bwidth.intValue();
				bandwidth = bw.toString();
			}
		}
		LOGGER.info("Resultant Bandwidth in setBandwidthConversion",bandwidth);
		return bandwidth;
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
					List<OrderNplLink> orderLink=new ArrayList<OrderNplLink>();
					if(quoteSite.getQuoteNplLink()!=null) {
						orderLink=orderNplLinkRepository.findByLinkCodeAndStatus(quoteSite.getQuoteNplLink().getLinkCode(), (byte)1);
					}
					if(quoteSite.getQuoteIllSite()!=null) {
						 orderSite=orderIllSitesRepository.findBySiteCodeAndStatus(quoteSite.getQuoteIllSite().getSiteCode(), (byte)1);
					}
					LOGGER.info("LINK SIZE"+orderLink.size()+"SITE LIST SIZE"+orderSite.size());
					if (!orderSite.isEmpty() && !orderLink.isEmpty()) {
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
						orderSiteBillingDetails.setSiteType(quoteSite.getSiteType());
						orderSiteBillingDetails.setOrderNplLink(orderLink.get(0));
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



	public void constructOrderSiteToServiceForCrossConnect(Order order) {
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(order.getOrderToLes().stream().findFirst().get().getOrderType()) ||
				MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(order.getOrderToLes().stream().findFirst().get().getOrderType())) {
			LOGGER.info("Order to service for cross connect");
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_IdAndIsDeletedIsNullOrIsDeleted(order.getQuote().getQuoteToLes().stream().findFirst().get().getId(),CommonConstants.INACTIVE);
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				LOGGER.info("quoteIllSiteToServiceList size"+quoteIllSiteToServiceList.size());
				quoteIllSiteToServiceList.stream().forEach(siteToService -> {
					OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
					orderIllSiteToService.setAllowAmendment(siteToService.getAllowAmendment());
					orderIllSiteToService.setErfServiceInventoryParentOrderId(
							siteToService.getErfServiceInventoryParentOrderId());
					orderIllSiteToService.setErfServiceInventoryServiceDetailId(
							siteToService.getErfServiceInventoryServiceDetailId());
					orderIllSiteToService
							.setErfServiceInventoryTpsServiceId(siteToService.getErfServiceInventoryTpsServiceId());
					List<OrderIllSite> orderIllSite=orderIllSitesRepository.findBySiteCodeAndStatus(siteToService.getQuoteIllSite().getSiteCode(),CommonConstants.BACTIVE);
					orderIllSiteToService.setOrderIllSite(orderIllSite.stream().findFirst().get());
					orderIllSiteToService.setOrderToLe(order.getOrderToLes().stream().findFirst().get());
					orderIllSiteToService.setType(siteToService.getType());
					orderIllSiteToService.setErfSfdcOrderType(siteToService.getErfSfdcOrderType());
					orderIllSiteToService.setErfSfdcSubType(siteToService.getErfSfdcSubType());
					orderIllSiteToService.setTpsSfdcOptyId(siteToService.getTpsSfdcOptyId());
					orderIllSiteToService.setTpsSfdcProductId(siteToService.getTpsSfdcProductId());
					orderIllSiteToService.setTpsSfdcProductName(siteToService.getTpsSfdcProductName());
					if (MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(order.getOrderToLes().stream().findFirst().get().getOrderType())) {
						QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);
						constructOrderSiteServiceTerminationDetails (quoteSiteServiceTerminationDetail, orderIllSiteToService);
					}
					orderIllSiteToServiceRepository.save(orderIllSiteToService);

				});


			}

		}
	}



    public List<OrderProductComponent> constructOrderProductComponentForCrossConnect(Order order,Set<ProductSolution> productSolutions) {


        List<OrderProductComponent> orderProductComponents = new ArrayList<>();
        List<QuoteProductComponent> productComponents=new ArrayList<>();

        if (!productSolutions.isEmpty()) {
            for(ProductSolution productSolution: productSolutions) {
                productComponents = quoteProductComponentRepository
                        .findByReferenceIdAndType(productSolution.getQuoteIllSites().stream().findFirst().get().getId(), PDFConstants.PRIMARY);
            }

            if (productComponents != null && !productComponents.isEmpty()) {
                for (QuoteProductComponent quoteProductComponent : productComponents) {

                    Integer referenceId = null;
                    if (quoteProductComponent.getType().equals(PDFConstants.PRIMARY))
                        referenceId = order.getOrderToLes().stream().findFirst().get().getOrderToLeProductFamilies().stream().findFirst().get().getOrderProductSolutions().stream().findFirst().get().getOrderIllSites().stream().findFirst().get().getId();


                    OrderProductComponent orderProductComponent = new OrderProductComponent();
                    if (quoteProductComponent.getMstProductComponent() != null) {
                        orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
                    }
                    orderProductComponent.setType(quoteProductComponent.getType());
                    orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
                    orderProductComponent.setReferenceId(referenceId);

                    orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
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
        return orderProductComponents;

    }



    protected void createCopfIdForLinksRenewals(Order order, Set<OrderNplLink> orderNplLinks, Map<String, String> productSolutionSiLinkMap) {
		LOGGER.info("------++++++Insider createCopfIdForLinks++++++------");

		try {
			if (order != null) {

				if (order.getOrderToLes() != null) {
					order.getOrderToLes().stream().forEach(orToLe -> {

						if (orToLe.getOrderToLeProductFamilies() != null) {
							orToLe.getOrderToLeProductFamilies().stream().forEach(orToLeProductFamil -> {
								if (orToLeProductFamil.getOrderProductSolutions() != null) {
									orToLeProductFamil.getOrderProductSolutions().stream().forEach(orProductSol -> {
										List<LinkCOPFDetails> linkCOPFDetailsList = new ArrayList<>();
										String quoteCode = order.getOrderCode();
										String currencyCode = orToLe.getCurrencyCode();
										String optyId = orToLe.getTpsSfdcCopfId();
										String productServiceId = null;
										List<ProductSolution> prodSol = productSolutionRepository
												.findByReferenceCode(quoteCode);
//										if (prodSol != null && !prodSol.isEmpty()) {
//											productServiceId = prodSol.get(0).getTpsSfdcProductName();
//										}
										for (ProductSolution productSolution : prodSol) {
											if(productSolution.getTpsSfdcProductId()!=null) {
												productServiceId = productSolution.getTpsSfdcProductName();
												break;
												}
										}
										if (orderNplLinks != null) {
											orderNplLinks.stream().forEach(nplLink -> {
												if(nplLink.getProductSolutionId()==orProductSol.getId()) {
												LinkCOPFDetails linkCOPFDetails = new LinkCOPFDetails();
												linkCOPFDetails.setMrcC(nplLink.getMrc().toString());
												linkCOPFDetails.setNrc(nplLink.getNrc().toString());
												linkCOPFDetails.setCopfIdC(CommonConstants.EMPTY);
												linkCOPFDetails.setEffectiveDateOfTermination(null);
												Integer siteAId = nplLink.getSiteAId();
												if (siteAId != null) {
													Optional<OrderIllSite> orderIllSite = orderIllSitesRepository
															.findById(siteAId);
													if (orderIllSite.isPresent()
															&& orderIllSite.get().getErfLocSitebLocationId() != null) {
														try {
															LOGGER.info(
																	"Location ID inside constructing link copf details {}",
																	orderIllSite.get().getErfLocSitebLocationId());
															linkCOPFDetails.setCity((String) mqUtils.sendAndReceive(
																	citeDetailByLocationIdQueue,
																	Integer.toString(orderIllSite.get()
																			.getErfLocSitebLocationId())));
														} catch (TclCommonException | IllegalArgumentException e) {
															LOGGER.info("Error while fetching the city from OMS");
														}
														if(orderIllSite.get().getErfServiceInventoryTpsServiceId()!=null) {
														 String copfId = productSolutionSiLinkMap.get(orderIllSite.get().getErfServiceInventoryTpsServiceId());
														 if(copfId!=null && copfId != "") {
														 linkCOPFDetails.setPreCopfId(orderIllSite.get().getErfServiceInventoryTpsServiceId()+"_"+copfId);
														 }
														}
													}
												}											

												linkCOPFDetailsList.add(linkCOPFDetails);
											}
											});
											if (quoteCode != null && currencyCode != null /*&& optyId != null
													&& productServiceId != null */ && !linkCOPFDetailsList.isEmpty()) {
												try {
													String type="NEW";
													if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orToLe.getOrderType())) {
														type="MACD";
													}
													if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orToLe.getOrderType())) {
														type = "TERMINATION";
													}
													omsSfdcService.processCreateCopf(quoteCode, optyId, currencyCode,
															productServiceId, linkCOPFDetailsList,type);
												} catch (TclCommonException e) {
													LOGGER.warn(ExceptionUtils.getStackTrace(e));
												}
											}
										}
									});
								}
							});
						}
					});
				}

			}
		} catch (Exception e) {
			LOGGER.warn("Error in creating COPF" + ExceptionUtils.getStackTrace(e));
		}

	}
    
	private Map<String, Object> constructOrderProductSolutionRenewals(Set<ProductSolution> productSolutions,
			OrderToLeProductFamily orderToLeProductFamily, NplQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<>();
		Set<OrderProductSolution> orderProductSolution = new HashSet<>();
		Set<OrderNplLink> orderNplLinks = new HashSet<>();
		Map<String, OrderNplLink> quoteLinkIdOrderLinkMapAll = new HashMap<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				List<QuoteIllSite> quoteIllSites = getIllsitesBasenOnVersion(solution);
				if (quoteIllSites != null && !quoteIllSites.isEmpty()) {
					OrderProductSolution oSolution = new OrderProductSolution();
					if (solution.getMstProductOffering() != null) {
						oSolution.setMstProductOffering(solution.getMstProductOffering());
					}
					oSolution.setSolutionCode(solution.getSolutionCode());
					oSolution.setOrderToLeProductFamily(orderToLeProductFamily);
					orderProductSolutionRepository.save(oSolution);
					if (Objects.nonNull(solution.getMstProductOffering().getProductName()) &&
							CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())) {
						Set<OrderIllSite> orderIllsites = crossConnectQuoteService.constructOrderIllSite(quoteIllSites, oSolution,detail);
						oSolution.setOrderIllSites(orderIllsites);
						orderProductSolution.add(oSolution);
						responseCollection.put(NplOrderConstants.METHOD_RESPONSE, orderProductSolution);
					} else {
						responseCollection = constructOrderIllSite(quoteIllSites, oSolution);
						Set<OrderIllSite> orderIllsites = (Set<OrderIllSite>) responseCollection
								.get(NplOrderConstants.METHOD_RESPONSE);
						Map<String, Integer> orderNplSiteIds = (Map<String, Integer>) responseCollection
								.get(NplOrderConstants.ORDER_ILLSITE_ID);
						oSolution.setOrderIllSites(orderIllsites);

						orderProductSolution.add(oSolution);

						responseCollection = processOrderNplLinks(solution, oSolution.getId(), orderNplSiteIds, detail, orderToLeProductFamily);
						orderNplLinks.addAll((Set<OrderNplLink>) responseCollection.get(NplOrderConstants.METHOD_RESPONSE));
						responseCollection.put(NplOrderConstants.METHOD_RESPONSE, orderProductSolution);
						responseCollection.put(NplOrderConstants.ORDER_NPL_LINK, orderNplLinks);
						Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
								.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);
						quoteLinkIdOrderLinkMapAll.putAll(quoteLinkIdOrderLinkMap);				
						responseCollection.put(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP, quoteLinkIdOrderLinkMapAll);		
						
					}
				}
			}
		}
		return responseCollection;
	}
	
	/**
     * Method to get quote npl link by link code and update its mf task triggered
     *
     * @param siteId
     */
    public String updateNplMfTaskTriggered(Integer linkId) {
		LOGGER.info("Inside updateNplMfTaskTriggered with linkId {}", linkId);
		List<TaskBean> taskBeanAEnd = new ArrayList<TaskBean>();
		List<TaskBean> taskBeanBEnd = new ArrayList<TaskBean>();
		List<TaskBean> totalTaskList = new ArrayList<TaskBean>();
		List<TaskBean> successList = new ArrayList<TaskBean>();
		String response = "";
		try {
			QuoteNplLink quoteNplLink = quoteNplLinkRepository.findById(linkId).get();
			if(quoteNplLink != null) {
				taskBeanAEnd = getTaskList(quoteNplLink.getSiteAId());
				taskBeanBEnd = getTaskList(quoteNplLink.getSiteBId());
				totalTaskList.addAll(taskBeanAEnd);
				totalTaskList.addAll(taskBeanBEnd);
				if(totalTaskList.size() > 0) {
					totalTaskList = totalTaskList.stream()
							.filter(task -> task.getMstTaskDef().startsWith("manual_feasibility"))
							.collect(Collectors.toList());
				if (!totalTaskList.isEmpty()) {
					totalTaskList.stream().forEach(item ->{
						LOGGER.info("Task id in oms :{}",item.getId());
						if(item.getStatus().equalsIgnoreCase("OPEN") || item.getStatus().equalsIgnoreCase("RETURNED") || 
							item.getStatus().equalsIgnoreCase("INPROGRESS") || item.getStatus().equalsIgnoreCase("REOPEN")) {
							successList.add(item);
						}
					});
				if(successList.size() == 0) {
					        quoteNplLink.setMfTaskTriggered(0);
					        quoteNplLinkRepository.save(quoteNplLink);
					        response =  "Successfully Triggered";
			      } else {
				            response =  "MF tasks are still in-progress";
			}
		  }else {
				response = "No MF tasks available for this linkId";
			}
		}else {
			response = "No tasks available for this linkId" ;
		}
	}else {
			LOGGER.info("Invalid linkId {}",linkId);
		}
		}catch (Exception ex) {
			LOGGER.info("Error in updateNplMfTaskTriggered in nplQuoteService : {}", ex);
		}
      return response;  
    }
    
    /**
     * Get Task List by siteId
     *
     * @param siteId
     */
    private List<TaskBean> getTaskList(Integer siteId) {
        List<TaskBean> tasks = new ArrayList<>();
        String response = null;
        try {
            response = (String) mqUtils.sendAndReceive(mfTaskTriggeredQueue, String.valueOf(siteId));
            LOGGER.info("response from l20workflow {}", response);
        } catch (TclCommonException e) {
            throw new TclCommonRuntimeException(ExceptionConstants.MF_TASK_TRIGGERED_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        if (Objects.nonNull(response)) {
        	tasks = GscUtils.fromJson(response, new TypeReference<List<TaskBean>>() {
            });
        }
        return tasks;
    }

	/**
	 * Get Task List by siteId
	 *
	 * @param linkId
	 * @param fesaibilityRowId
	 * @param flag
	 */
	public String updateSelectedResponseLink(Integer linkId, Integer feasibilityRowId,Integer flag){
		String response=null;
		try{
			LOGGER.info("Inside Response selection method", linkId, feasibilityRowId,flag);
			Optional<LinkFeasibility> linkFeasibilityResult= null;
			linkFeasibilityResult=linkFeasibilityRepository.findById(feasibilityRowId);
			if(linkFeasibilityResult!=null){
				LOGGER.info("Link Feasibility record available {}",linkFeasibilityResult.get());
				if(flag==0) {
					linkFeasibilityResult.get().setIsSelected(BDEACTIVATE);
				}
				else{
					linkFeasibilityResult.get().setIsSelected(BACTIVE);
				}
				linkFeasibilityRepository.save(linkFeasibilityResult.get());
				LOGGER.info("Response Updated");
				response="Response Updated";
			}
			else{
				LOGGER.info("No Feasibility Response");
				response="No Feasibility Response available";
			}
		}
		catch (Exception e){
			LOGGER.info("Exception in updateSelectedResponse {}", e);
		}
		return response;
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
