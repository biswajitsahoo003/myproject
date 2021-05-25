package com.tcl.dias.oms.ill.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.common.constants.CommonConstants.COMMA;
import static com.tcl.dias.common.constants.CommonConstants.GVPN;
import static com.tcl.dias.common.constants.CommonConstants.IAS;
import static com.tcl.dias.common.constants.CommonConstants.MACD;
import static com.tcl.dias.common.constants.CommonConstants.NPL;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.LeAttributesConstants.BILLING_CONTACT_ID;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_SFDC;
import static com.tcl.dias.common.constants.LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY;
import static com.tcl.dias.common.constants.LeAttributesConstants.OWNER_EMAIL_SFDC;
import static com.tcl.dias.common.constants.LeAttributesConstants.OWNER_NAME_SFDC;
import static com.tcl.dias.common.constants.LeAttributesConstants.REGION_SFDC;
import static com.tcl.dias.common.constants.LeAttributesConstants.TEAM_ROLE_SFDC;
import static com.tcl.dias.oms.constants.MACDConstants.MACD_QUOTE_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_FORM;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_THROUGH_CLASSIFICATION;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_WITH_CLASSIFICATION;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.LeCcaRequestBean;
import com.tcl.dias.common.beans.TaskBean;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javaswift.joss.model.StoredObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.lowagie.text.DocumentException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.AddressDetailsMultiSite;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.BillingAddressInfo;
import com.tcl.dias.common.beans.BulkSiteBean;
import com.tcl.dias.common.beans.CommercialQuoteDetailBean;
import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.CustomerLeVO;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.DemoOrderInfo;
import com.tcl.dias.common.beans.ExistingMacdComponents;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.GstAddressInfo;
import com.tcl.dias.common.beans.IllGvpnMultiSiteInputs;
import com.tcl.dias.common.beans.LMDetailBean;
import com.tcl.dias.common.beans.LeGstDetailsBean;
import com.tcl.dias.common.beans.LeOwnerDetailsSfdc;
import com.tcl.dias.common.beans.LocationAddressInfo;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MSABean;
import com.tcl.dias.common.beans.MfAttachmentBean;
import com.tcl.dias.common.beans.MfL2OReportBean;
import com.tcl.dias.common.beans.MultiSiteBasicAttributes;
import com.tcl.dias.common.beans.MultiSiteBillingInfoBean;
import com.tcl.dias.common.beans.MultiSiteCpeAttributes;
import com.tcl.dias.common.beans.MultiSiteFeasibility;
import com.tcl.dias.common.beans.MultiSitePricingAttributes;
import com.tcl.dias.common.beans.MultiSiteResponseJsonAttributes;
import com.tcl.dias.common.beans.ObjectStorageListenerBean;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.beans.SiteDetailServiceFulfilmentUpdateBean;
import com.tcl.dias.common.beans.SiteLevelAddressBean;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.beans.ThirdPartyResponseBean;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIPriceRevisionDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponseBean;
import com.tcl.dias.common.sfdc.bean.ServiceRequestBean;
import com.tcl.dias.common.sfdc.bean.SfdcCreditCheckQueryRequest;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.AuditMode;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.QuoteAccess;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.CommercialRejectionBean;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.DashBoardFamilyBean;
import com.tcl.dias.oms.beans.DashboardCustomerbean;
import com.tcl.dias.oms.beans.DashboardLegalEntityBean;
import com.tcl.dias.oms.beans.DashboardQuoteBean;
import com.tcl.dias.oms.beans.EnrichmentDetailsBean;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.LinkJsonManualUpdateBean;
import com.tcl.dias.oms.beans.MACDExistingComponentsBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.MultiSiteStatusBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.PricingDetailBean;
import com.tcl.dias.oms.beans.ProductAttributeMasterBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.ProfileRequest;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteFamilySVBean;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuoteIllSitesFeasiblityBean;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteLeSVBean;
import com.tcl.dias.oms.beans.QuotePriceAuditBean;
import com.tcl.dias.oms.beans.QuotePriceAuditResponse;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteSiteNotFeasibleBean;
import com.tcl.dias.oms.beans.QuoteSiteServiceTerminationDetailsBean;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.beans.QuoteSummaryBean;
import com.tcl.dias.oms.beans.QuoteTncBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.Site;
import com.tcl.dias.oms.beans.SiteAttributeUpdateBean;
import com.tcl.dias.oms.beans.SiteDetail;
import com.tcl.dias.oms.beans.SiteDocumentBean;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SiteFeasibilityManualBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.PricingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.dashboard.service.v1.DashboardService;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.dto.MstProductComponentDto;
import com.tcl.dias.oms.dto.QuoteDelegationDto;
import com.tcl.dias.oms.dto.QuoteDto;
import com.tcl.dias.oms.dto.QuoteIllSiteDto;
import com.tcl.dias.oms.dto.QuotePriceDto;
import com.tcl.dias.oms.dto.QuoteProductComponentDto;
import com.tcl.dias.oms.dto.QuoteToLeDto;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.gvpn.pricing.bean.Feasible;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.ill.macd.service.v1.IllMACDService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.service.IllQuotePdfService;
import com.tcl.dias.oms.pricing.bean.Result;
import com.tcl.dias.oms.renewals.bean.RenewalsConstant;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.service.QuoteIsvSpecification;
import com.tcl.dias.oms.service.QuoteSpecification;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.validator.services.IllCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
/**
 * This file contains the IllQuoteService.java class. All the Quote related
 * Services for ILL will be implemented in this classcre
 * 
 * get
 * 
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class IllQuoteService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IllQuoteService.class);
	public static final String SITE_PROPERTIES = "SITE_PROPERTIES";

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	IllMACDService illMACDService;

	@Autowired
	MACDUtils macdutils;

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
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	protected MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;
	
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
	NotificationService notificationService;

	@Autowired
	CurrencyConversionRepository currencyConversionRepository;

	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;
	
	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	protected MQUtils mqUtils;
	
	@Autowired
	CommercialBulkProcessSiteRepository commercialBulkProcessSiteRepository;

	@Value("${rabbitmq.orderIdInRespecToServiceId.queue}")
	String orderIdCorrespondingToServId;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${notification.mail.template}")
	String delegationTemplateId;

	@Value("${notification.mail.loginurl}")
	String loginUrl;

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

	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Value("${rabbitmq.suplierle.queue}")
	String suplierLeQueue;

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
	
	@Value("${app.host}")
	String appHost;

	@Value("${rabbitmq.location.detail}")
	protected String locationQueue;

	@Value("${rabbitmq.related.task.deletion}")
	String taskCloseQueue;

	@Value("${info.customer_le_location_queue}")
	String customerLeLocationQueue;

	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Autowired
	protected IllQuotePdfService illQuotePdfService;
	
	@Autowired
	IllCofValidatorService illCofValidatorService;

	@Autowired
	protected CofDetailsRepository cofDetailsRepository;

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Value("${application.test.customers}")
	String[] testCustomers;

	@Value("${application.env}")
	String appEnv;

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
	OrderNplLinkRepository orderNplLinkRepository;

	@Autowired
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;

	@Autowired
	NplLinkRepository nplLinkRepository;

	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;

	@Autowired
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;

	@Autowired
	OrderLinkStatusAuditRepository orderLinkStatusAuditRepository;

	@Autowired
	OrderLinkStageAuditRepository orderLinkStageAuditRepository;

	@Autowired
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;

	@Autowired
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Value("${rabbitmq.location.details.feasibility}")
	protected String locationDetailsQueue;

	@Autowired
	EngagementRepository engagementRepository;

	@Value("${object.storage.file.move.queue}")
	String objectStorageFileMoveQueue;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${rabbitmq.customercode.customerlecode.queue}")
	String customerCodeCustomerLeCodeQueue;

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${cust.get.segment.attribute}")
	String customerSegment;
	
	@Value("${attachment.requestId.queue}")
	String attachReqIdQueue;

	@Autowired
	PartnerCustomerDetailsService partnerCustomerDetailsService;

	@Autowired
	PartnerService partnerService;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Autowired
	IllMACDService illMacdService;

	@Autowired
	CreditCheckService creditCheckService;

	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Value("${rabbitmq.customer.account.manager.email}")
	String customerAccountManagerEmail;

	@Value("${rabbitmq.o2c.sitedetail}")
	String updateTermsInMonthsSiteDetailQueue;

	@Value("${sfdc.process.creditcheck.retrigger.queue}")
	String creditCheckRetriggerQueue;

	@Autowired
	QuoteLeCreditCheckAuditRepository quoteLeCreditCheckRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobRepository;

	@Value("${rabbitmq.customerle.credit.queue}")
	String customerLeCreditCheckQueue;

	@Value("${rabbitmq.get.service.request.queue}")
	String getServiceRequestQueue;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Autowired
	MACDUtils macdUtils;

	@Value("${rabbitmq.get.partner.account.name.by.partner}")
	String partnerAccountNameMQ;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	protected OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	@Autowired
	QuoteTncRepository quoteTncRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	GstInService gstInService;

	@Autowired
	NplQuoteService nplQuoteService;

	@Value("${rabbitmq.location.localitcontact}")
	String localItQueue;
	
	@Autowired
	private CommercialQuoteAuditRepository commercialQuoteAuditRepository;


	@Value("${rabbitmq.location.demarcation}")
	String demarcationQueue;
	
	@Autowired
	QuoteAccessPermissionRepository quoteAccessPermissionRepository;
	
	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;
	
	@Autowired
	IllSlaService illSlaService;
	
	@Value("${rabbitmq.o2c.amendment.task}")
	String amendmentTaskApi;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	
	@Autowired
	IllSiteRepository illSitesRepository;
	
	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	
	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;


	@Value("${rabbitmq.getOwnerDetailsSfdc.queue}")
	String ownerDetailsQueue;
	
	@Value("${oms.bulk.site.process.queue}")
	String omsBulkSiteProcessQueue;
	
	@Autowired
	ProductSolutionRepository quoteProductSolutionRepository;

	@Autowired
	OrderSiteServiceTerminationDetailsRepository orderSiteServiceTerminationDetailsRepository;
	
	@Value("${bulkupload.max.count}")
	String minSiteLength;

	@Autowired
	QuoteSiteBillingDetailsRepository quoteSiteBillingInfoRepository;
	
	@Value("${rabbitmq.customer.billing.gst.queue}")
	String customerBillingGstAddressQueue;
	
	@Value("${rabbitmq.sitelevel.location.queue}")
	protected String siteLevelLocationQueue;

	@Value("${rabbitmq.cca.customer.le.queue}")
	protected String getLeCCAfromCustomerQueue;

	@Autowired
	OrderSiteBillingDetailsRepository orderSiteBillingInfoRepository;
	
	@Value("${rabbitmq.si.related.details.inactive.queue}")
	String siRelatedDetailsInactiveQueue;
	
	@Value("${rabbitmq.le.gst.queue}")
	protected String LeGstQueue;
	
	@Value("${rabbitmq.mf.task.triggered.queue}")
	private String mfTaskTriggeredQueue;
	
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
	public QuoteResponse createQuote(QuoteDetail quoteDetail, Integer erfCustomerId, Boolean ns)
			throws TclCommonException {
		QuoteResponse response = new QuoteResponse();
		try {
			if (ns == null) {
				ns = false;
			}
			validateQuoteDetail(quoteDetail);// validating the input for create Quote
			User user = getUserId(Utils.getSource());
			QuoteToLe quoteTole = processQuote(quoteDetail, erfCustomerId, user, ns);
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());
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
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NAME.toString(),
				user.getFirstName());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_EMAIL.toString(),
				user.getEmailId());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_ID.toString(),
				user.getUsername());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NO.toString(),
				user.getContactNo());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.DESIGNATION.toString(),
				user.getDesignation());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.RECURRING_CHARGE_TYPE.toString(), "ARC");
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.IS_ORDER_ENRICHMENT_ATTRIBUTES_PROVIDED, "No");
	}

	/**
	 * updateConstactInfo
	 * 
	 * @param quoteTole
	 * @param user
	 */
	public void updateLeAttribute(QuoteToLe quoteTole, String userName, String name, String value) {
		MstOmsAttribute mstOmsAttribute = null;
		if(name != null) {
		List<MstOmsAttribute> mstOmsAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(name, (byte) 1);

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
					.findByReferenceIdAndReferenceName(illSites.getId(), QuoteConstants.ILLSITES.toString());
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
			customerLeAttributeRequestBean.setProductName("IAS");
			LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
					Utils.convertObjectToJson(customerLeAttributeRequestBean));

			if (StringUtils.isNotEmpty(customerLeAttributes)) {

				updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
						CustomerLeDetailsBean.class), optionalQuoteLe.get());
			}

			/*
			 * if (StringUtils.isNotBlank(customerLeAttributes)) { CustomerLeDetailsBean
			 * customerLeDetailsBean = Utils.convertJsonToObject(customerLeAttributes,
			 * CustomerLeDetailsBean.class);
			 * 
			 * if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteLe.get().
			 * getQuoteType())) { { String erfServiceInventoryParentOrderId =
			 * optionalQuoteLe.get().getErfServiceInventoryParentOrderId().toString(); if
			 * (StringUtils.isNotBlank(erfServiceInventoryParentOrderId)) { SIOrderDataBean
			 * orderData = macdUtils.getSiOrderData(erfServiceInventoryParentOrderId);
			 * 
			 *//* Setting billing parameters from Service Inventory *//*
																		 * customerLeDetailsBean.
																		 * setPreapprovedBillingMethod(orderData.
																		 * getBillingMethod()); customerLeDetailsBean.
																		 * setPreapprovedPaymentTerm(orderData.
																		 * getPaymentTerm());
																		 * customerLeDetailsBean.setBillingFrequency(
																		 * orderData.getBillingFrequency()); } } } if
																		 * (Objects.nonNull(customerLeDetailsBean)) {
																		 * updateBillingInfoForSfdc(
																		 * customerLeDetailsBean,
																		 * optionalQuoteLe.get()); } }
																		 */

			String spName = returnServiceProviderName(documentDto.getSupplierLegalEntityId());
			if (StringUtils.isNotEmpty(spName)) {
				processAccount(optionalQuoteLe.get(), spName,
						LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
			}

			QuoteToLe quoteToLe = optionalQuoteLe.get();
			oldCustomerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
			quoteToLe.setErfCusCustomerLegalEntityId(documentDto.getCustomerLegalEntityId());
			quoteToLe.setErfCusSpLegalEntityId(documentDto.getSupplierLegalEntityId());
			if (quoteToLe.getStage().equals(QuoteStageConstants.CHECKOUT.getConstantCode())) {
				quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			}
			//Ns Orders
			if (("Y").equalsIgnoreCase(quoteToLe.getQuote().getNsQuote())&&quoteToLe.getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
				quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			}
			List<String> productFamily = new ArrayList<>();
			quoteToLe.getQuoteToLeProductFamilies().stream().forEach(pf->productFamily.add(pf.getMstProductFamily().getName()));
			if (("GDE").equalsIgnoreCase(productFamily.get(0)) && quoteToLe.getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
				quoteToLe.setStage(QuoteStageConstants.CHANGE_ORDER.getConstantCode());
			}
			quoteToLeRepository.save(quoteToLe);

			CustomerDetail customerDetail = null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				customerDetail = new CustomerDetail();
				Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(documentDto.getCustomerId(),
						(byte) 1);
				customerDetail.setCustomerId((customer!=null ?customer.getId():null));
			} else {
				customerDetail = userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
			}
			// CustomerDetail customerDetail =
			// userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
			if (customerDetail != null && !customerDetail.getCustomerId().equals(quote.getCustomer().getId())) {
				Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
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
					|| (Objects.nonNull(optionalQuoteLe.get()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteLe.get().getQuoteType())) ||
					(Objects.nonNull(optionalQuoteLe.get().getQuoteType()) && optionalQuoteLe.get().getQuoteType().equalsIgnoreCase(RenewalsConstant.RENEWALS))) {
				CustomerLeDetailsBean lePreapprovedValuesBean = (CustomerLeDetailsBean) Utils
						.convertJsonToObject(customerLeAttributes, CustomerLeDetailsBean.class);
//				if(Objects.nonNull(lePreapprovedValuesBean.getCreditCheckAccountType()) && 
//						(lePreapprovedValuesBean.getCreditCheckAccountType().equalsIgnoreCase(CreditCheckConstants.CC_ACCOUNT_TYPE_3A) || lePreapprovedValuesBean.getCreditCheckAccountType().equalsIgnoreCase(CreditCheckConstants.CC_ACCOUNT_TYPE_3B))) {
				processAccount(optionalQuoteLe.get(), lePreapprovedValuesBean.getCreditCheckAccountType(),
						LeAttributesConstants.CREDIT_CHECK_ACCOUNT_TYPE.toString());
				creditCheckService.triggerCreditCheck(documentDto.getCustomerLegalEntityId(), optionalQuoteLe,
						lePreapprovedValuesBean, oldCustomerLegalEntityId);
				response.setCreditCheckStatus(optionalQuoteLe.get().getTpsSfdcStatusCreditControl());
				response.setPreapprovedFlag(
						CommonConstants.BACTIVE.equals(optionalQuoteLe.get().getPreapprovedOpportunityFlag()) ? true
								: false);
//				} else {
//					response.setCreditCheckStatus("NA");
//				}
				if(optionalQuoteLe.get().getQuoteType().equalsIgnoreCase(RenewalsConstant.RENEWALS)){
					quoteToLe.setTpsSfdcStatusCreditControl(optionalQuoteLe.get().getTpsSfdcStatusCreditControl());
					quoteToLe.setPreapprovedOpportunityFlag(optionalQuoteLe.get().getPreapprovedOpportunityFlag());
					quoteToLeRepository.save(quoteToLe);
				}
			}
			LOGGER.info("After triggering credit check");
			// Credit Check - End
			if  ((CommonConstants.NEW.equalsIgnoreCase(quoteToLe.getQuoteType())) ||
					( MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) && !CommonConstants.QUOTE_CATEGORY_BANDWIDTH_ON_DEMAND.equalsIgnoreCase(quoteToLe.getQuoteCategory())) ||
							(RenewalsConstant.RENEWALS.equalsIgnoreCase(quoteToLe.getQuoteType()))) {

				omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
						SFDCConstants.VERBAL_AGREEMENT_STAGE.toString(), quoteToLe);
				LOGGER.info("Verbal Agreement request sent to SFDC");
			}

			//pipf-212
			persistLeOwnerDetailsSfdc(documentDto);
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

	/**
	 * @author VIVEK KUMAR K
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
			LOGGER.info("Customer id to be send {} , request {}", erfCustomerId, request);
			LOGGER.info("MDC Filter token value in before Queue call processLocationDetailsAndSendToQueue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
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
			illPricingFeasibilityService.recalculateSites(quoteToLe.getId());

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

		LOGGER.info("MDC Filter token value in before Queue call removeReturnedTasks {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));

		try {
			mqUtils.sendAndReceive(taskCloseQueue, siteId.toString());
		} catch (TclCommonException e) {
			LOGGER.error("error in processing to queue call for closing tasks{}", e);
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
	public void deletedIllsiteAndRelation(QuoteIllSite quoteIllSite) {
		LOGGER.info("Inside deletedIllsiteAndRelation for siteid {}", quoteIllSite.getId());
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
	public void removeComponentsAndAttr(Integer siteId) {
		LOGGER.info("Inside removeComponentsAndAttr for siteid {}", siteId);
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(siteId, QuoteConstants.ILLSITES.toString());
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
	 * @param isColo
	 * @return QuoteResponse
	 * @throws TclCommonException
	 */
	public QuoteBean updateSite(QuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId, String isColo)
			throws TclCommonException {
		QuoteBean quoteBean = null;
		try {
			LOGGER.info("Customer Id received is {}", erfCustomerId);
			LOGGER.info("quote details received {} , for quote id {}", quoteDetail.toString(), quoteId);
			validateSiteInformation(quoteDetail);
			User user = getUserId(Utils.getSource());
			List<Site> sites = quoteDetail.getSite();
			MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			if (quoteToLe.isPresent()) {
				for (Site site : sites) {
					QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
							.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
					LOGGER.info("product family is {}", quoteToLeProductFamily.toString());
					String productOfferingName = site.getOfferingName();
					LOGGER.info("Offering name is {}", productOfferingName);
					processSiteDetail(user, productFamily, quoteToLeProductFamily, site, productOfferingName,
							quoteToLe.get().getQuote(), isColo);
				}
				quoteDetail.setQuoteId(quoteId);
			}
			if (quoteToLe.isPresent()) {
				if (quoteToLe.get().getStage().equals(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode())) {
					quoteToLe.get().setStage(QuoteStageConstants.ADD_LOCATIONS.getConstantCode());
					quoteToLeRepository.save(quoteToLe.get());
				}
			}
			quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), false, null, null);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteBean;
	}

	public ServiceResponse processMailAttachment(String email, Integer quoteId) throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();
		if (Objects.isNull(email) || !Utils.isValidEmail(email)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String quoteHtml = illQuotePdfService.processQuoteHtml(quoteId);
			String fileName = "Quote_" + quoteId + ".pdf";
			notificationService.processShareQuoteNotification(email,
					java.util.Base64.getEncoder().encodeToString(quoteHtml.getBytes()), userInfoUtils.getUserFullName(),
					fileName, CommonConstants.IAS);
			fileUploadResponse.setStatus(Status.SUCCESS);
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
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
	 * @param erfcustomerId
	 * @return Quote
	 * @throws TclCommonException
	 */
	protected QuoteToLe processQuote(QuoteDetail quoteDetail, Integer erfCustomerId, User user, Boolean ns)
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
					quoteDetail.getEngagementOptyId(), quoteDetail.getQuoteCode(), ns);
			quoteRepository.save(quote);
		} else {
			quote = quoteRepository.findByIdAndStatus(quoteDetail.getQuoteId(), CommonConstants.BACTIVE);
		}
		QuoteToLe quoteToLe = null;
		if (quoteDetail.getQuoteId() == null) {
			quoteToLe = constructQuoteToLe(quote, quoteDetail);
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
				if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId()))
					omsSfdcService.processProductServiceForSolution(quoteToLe, productSolution,
							quoteToLe.getTpsSfdcOptyId());// adding productService
				// to Sfdc
			} else {
				productSolution.setProductProfileData(Utils.convertObjectToJson(solution));
				productSolutionRepository.save(productSolution);
				List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
						CommonConstants.BACTIVE);
				for (QuoteIllSite quoteIllSite : illSites) {
					quoteIllSite.setProductSolution(productSolution);
					illSiteRepository.save(quoteIllSite);
					removeComponentsAndAttr(quoteIllSite.getId());
					for (ComponentDetail componentDetail : solution.getComponents()) {
						processProductComponent(productFamily, quoteIllSite, componentDetail, user, null);
					}
					// Initializing siteProperty
					MstProductComponent sitePropComp = getMstProperties(user);
					List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
							.findByReferenceIdAndReferenceNameAndMstProductComponent(quoteIllSite.getId(),
									QuoteConstants.ILLSITES.toString(), sitePropComp);
					if (quoteProductComponents.isEmpty()) {
						QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
						quoteProductComponent.setMstProductComponent(sitePropComp);
						quoteProductComponent.setMstProductFamily(productFamily);
						quoteProductComponent.setReferenceId(quoteIllSite.getId());
						quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
						quoteProductComponentRepository.save(quoteProductComponent);
					}
				}
			}
		}
		return quoteToLe;

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
			QuoteToLeProductFamily quoteToLeProductFamily, Site site, String productOfferingName, Quote quote, String isColo)
			throws TclCommonException {
		try {
			MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName, user);
			LOGGER.info("Product offering {}", productOfferng);
			ProductSolution productSolution = productSolutionRepository
					.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
			LOGGER.info("Product solution {}", productSolution.toString());
			constructIllSites(productSolution, user, site, productFamily, quote, isColo);

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
			ComponentDetail component, User user, String isColo) throws TclCommonException {
		try {
			if (Objects.isNull((isColo)) || "False".equalsIgnoreCase(isColo) ||
					(Objects.nonNull(isColo) && "True".equalsIgnoreCase(isColo) && !component.getName().equalsIgnoreCase("Last Mile"))) {
				MstProductComponent productComponent = getProductComponent(component, user);
				QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily,
						illSite.getId());
				quoteComponent.setType(component.getType());
				quoteProductComponentRepository.save(quoteComponent);
				LOGGER.info("saved successfully");
				for (AttributeDetail attribute : component.getAttributes()) {
					processProductAttribute(quoteComponent, attribute, user);
				}
			}
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
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
	private void processProductAttribute(QuoteProductComponent quoteComponent, AttributeDetail attribute, User user)
			throws TclCommonException {
		try {
			ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
			QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
					productAttribute, attribute);
			    quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.INVALID_ATTRIBUTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * constructQuote-This method constructs quote entity
	 * 
	 * @param customer
	 * @param userId
	 * @return Quote
	 * @param productName
	 * @param engagementOptyId
	 */
	private Quote constructQuote(Customer customer, Integer userId, String productName, String engagementOptyId,
			String quoteCode, Boolean ns) {
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
		quote.setQuoteCode(null != engagementOptyId ? quoteCode : Utils.generateRefId(productName.toUpperCase()));
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
	 * @param userId
	 * @param productSolution
	 * @param isColo
	 * @return void
	 * @throws TclCommonException
	 */
	private void constructIllSites(ProductSolution productSolution, User user, Site site,
								   MstProductFamily productFamily, Quote quote, String isColo) throws TclCommonException {
		LOGGER.info("inside constructIllSites method , profile data is {}", productSolution.getProductProfileData().toString());
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
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 60); // Adding 60 days
				illSite.setEffectiveDate(cal.getTime());
				illSite.setCreatedBy(user.getId());
				illSite.setCreatedTime(new Date());
				illSite.setStatus((byte) 1);
				illSite.setImageUrl(soDetail.getImage());
				illSite.setSiteCode(Utils.generateUid());
				illSite.setFeasibility((byte) 0);
				illSite.setIsTaskTriggered(0);
				illSite.setMfTaskTriggered(0);
				illSite.setCommercialRejectionStatus("0");
				if (Objects.nonNull(isColo) && "True".equalsIgnoreCase(isColo)) {
					illSite.setIsColo((byte) 1);
				} else {
					illSite.setIsColo((byte) 0);
				}
				illSiteRepository.save(illSite);
				siteDetail.setSiteId(illSite.getId());
				for (ComponentDetail componentDetail : soDetail.getComponents()) {
					processProductComponent(productFamily, illSite, componentDetail, user, isColo);
				}
				// Initializing siteProperty
				MstProductComponent sitePropComp = getMstProperties(user);
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndReferenceNameAndMstProductComponent(illSite.getId(),
								QuoteConstants.ILLSITES.toString(), sitePropComp);
				if (quoteProductComponents.isEmpty()) {
					LOGGER.info("Entering saving quote product component");
					QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
					quoteProductComponent.setMstProductComponent(sitePropComp);
					quoteProductComponent.setMstProductFamily(productFamily);
					quoteProductComponent.setReferenceId(illSite.getId());
					quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
					quoteProductComponentRepository.save(quoteProductComponent);
					LOGGER.info("Saved Quote Product Component");
				}
				QuoteToLe quoteToLe = quote.getQuoteToLes().stream().findFirst().get();
				if(quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE) || quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE)) {
					List<QuoteIllSiteToService> quoteSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdInAndQuoteToLe(siteDetail.getErfServiceInventoryTpsServiceId(), quoteToLe);
					if(quoteSiteToService != null && !quoteSiteToService.isEmpty()) {
						quoteSiteToService.stream().forEach(quoteSiteToServiceRecord -> {
						LOGGER.info("Updateing quoteIllSite data in QuoteIllSiteToService for site id {} ", illSite.getId());
						quoteSiteToServiceRecord.setQuoteIllSite(illSite);
						quoteIllSiteToServiceRepository.save(quoteSiteToServiceRecord);
						});
					}
				}
			} else {
				QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(siteDetail.getSiteId(), (byte) 1);
				if (illSiteEntity != null) {
					illSiteEntity.setProductSolution(productSolution);
					illSiteRepository.save(illSiteEntity);
					removeComponentsAndAttr(illSiteEntity.getId());
					for (ComponentDetail componentDetail : soDetail.getComponents()) {
						processProductComponent(productFamily, illSiteEntity, componentDetail, user, isColo);
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
	public void validateQuoteDetail(QuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null)) {// TODO validate the inputs for quote
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
			throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);

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
	private QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer illSiteId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
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
	private MstProductComponent getProductComponent(ComponentDetail component, User user) throws TclCommonException {
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
	private ProductAttributeMaster getProductAttributes(AttributeDetail attributeDetail, User user)
			throws TclCommonException {
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
			User user) throws TclCommonException {
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

	public TriggerEmailResponse getEmailIdAndTriggerEmail(Integer quoteId, Integer quoteLeId)
			throws TclCommonException {
		TriggerEmailResponse response = new TriggerEmailResponse(Status.SUCCESS.toString());
		try {
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				User users = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
				MailNotificationBean mailNotificationBean = new MailNotificationBean();
				mailNotificationBean.setCustomerEmail(users.getEmailId());
				mailNotificationBean.setOrderId(String.valueOf(quoteEntity.get().getQuoteCode()));
				mailNotificationBean.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
				mailNotificationBean.setProductName(CommonConstants.IAS);
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
				String leMail = getLeAttributes(quoteToLe.get(), LeAttributesConstants.LE_EMAIL.toString());

				MailNotificationBean mailNotificationBean = populateMailNotificationBean(
						getLeAttributes(quoteToLe.get(), CUSTOMER_CONTRACTING_ENTITY),
						customerUser.getCustomer().getCustomerName(), customerUser.getUsername(),
						customerUser.getContactNo(), triggerEmailRequest.getEmailId(), leMail, orderRefId,
						appHost + adminRelativeUrl, quoteToLe.get());
				notificationService.cofDelegationNotification(mailNotificationBean);

				MailNotificationBean mailNotificationBeanCofDelegate = populateMailNotificationBeanCofDelegate(
						quoteToLe.get(), triggerEmailRequest, user, orderRefId, leMail);
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
		mailNotificationBeanCofDelegate.setProductName(CommonConstants.IAS);
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
		mailNotificationBean.setProductName(IAS);
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
			LOGGER.warn("Error while reading end customer name ::  " + ExceptionUtils.getStackFrames(e));
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
					if (attrval.getMstOmsAttribute().getName().equals(LeAttributesConstants.CONTACT_ID.toString())) {
						attrval.setAttributeValue(String.valueOf(user.getId()));
						quoteLeAttributeValueRepository.save(attrval);

					} else if (attrval.getMstOmsAttribute().getName()
							.equals(LeAttributesConstants.CONTACT_NAME.toString())) {
						attrval.setAttributeValue(user.getFirstName());
						quoteLeAttributeValueRepository.save(attrval);

					} else if (attrval.getMstOmsAttribute().getName()
							.equals(LeAttributesConstants.CONTACT_EMAIL.toString())) {
						attrval.setAttributeValue(user.getEmailId());
						quoteLeAttributeValueRepository.save(attrval);

					} else if (attrval.getMstOmsAttribute().getName()
							.equals(LeAttributesConstants.CONTACT_NO.toString())) {
						attrval.setAttributeValue(user.getContactNo());
						quoteLeAttributeValueRepository.save(attrval);
					} else if (attrval.getMstOmsAttribute().getName()
							.equals(LeAttributesConstants.DESIGNATION.toString())) {
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
	public QuoteBean getQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProperitiesRequired,
			Integer siteId, Boolean manualFeasibility) throws TclCommonException {
		QuoteBean response = null;
		try {
			LOGGER.info("Inside Get Quote for quoteId {}", quoteId);
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString())) ? true : false;
			LOGGER.info("Get quote request is to fetch with non feasibile Items status {}", isFeasibleSites);
			Quote quote = getQuote(quoteId);
			LOGGER.info("Quote code for the fetched quote {} is {}", quoteId, quote.getQuoteCode());
			response = constructQuote(quote, isFeasibleSites, isSiteProperitiesRequired, siteId,manualFeasibility);

			Optional<QuoteToLe> quoteToLe1 = quote.getQuoteToLes().stream().findFirst()
					.filter(quoteToLe -> MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()));

			if (quoteToLe1.isPresent()) {
				response.setIsAmended(quoteToLe1.get().getIsAmended());
				response.setQuoteType(quoteToLe1.get().getQuoteType());
				response.setQuoteCategory(quoteToLe1.get().getQuoteCategory());
				if (Objects.nonNull(quoteToLe1.get().getIsMultiCircuit())&&quoteToLe1.get().getIsMultiCircuit() == 1)
					response.setIsMultiCircuit(true);				
					List<String> multiCircuitChangeBandwidthFlag = new ArrayList<>();
					List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe1.get().getId());
					quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
						if (Objects.nonNull(quoteIllSiteToService) && CommonConstants.BACTIVE.equals(quoteIllSiteToService.getBandwidthChanged()))
							multiCircuitChangeBandwidthFlag.add("true");
						else
							multiCircuitChangeBandwidthFlag.add("false");
					});
					if (multiCircuitChangeBandwidthFlag.contains("false")) {
						response.setIsMulticircuitBandwidthChangeFlag(false);
					} else
						response.setIsMulticircuitBandwidthChangeFlag(true);
			}

			// for termination ==================
			Optional<QuoteToLe> quoteToLeTerm = quote.getQuoteToLes().stream()
					.filter(quoteToLe -> MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())).findFirst();
			if (quoteToLeTerm.isPresent()) {
				response.setQuoteType(quoteToLeTerm.get().getQuoteType());
				if (Objects.nonNull(quoteToLeTerm.get().getIsMultiCircuit())&&quoteToLeTerm.get().getIsMultiCircuit() == 1)
					response.setIsMultiCircuit(true);
				List<String> multiCircuitChangeBandwidthFlag = new ArrayList<>();
				List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository.findByQuoteToLe_IdAndIsDeletedIsNullOrIsDeleted(quoteToLeTerm.get().getId(),CommonConstants.INACTIVE);
				quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
					if (Objects.nonNull(quoteIllSiteToService) && CommonConstants.BACTIVE.equals(quoteIllSiteToService.getBandwidthChanged()))
						multiCircuitChangeBandwidthFlag.add("true");
					else
						multiCircuitChangeBandwidthFlag.add("false");
				});
				if (multiCircuitChangeBandwidthFlag.contains("false")) {
					response.setIsMulticircuitBandwidthChangeFlag(false);
				} else
					response.setIsMulticircuitBandwidthChangeFlag(true);
			}

			List<QuoteToLe> quoteLeList = quoteToLeRepository.findByQuote_Id(quoteId);
			Optional<QuoteToLe> quoteToLer = quoteLeList.stream().findFirst()
					.filter(quoteToLe -> RenewalsConstant.RENEWALS.equalsIgnoreCase(quoteToLe.getQuoteType()));

			if (quoteToLer.isPresent()) {
				response.setQuoteType(quoteToLer.get().getQuoteType());
				response.setIsCommercialChanges(findIsCommercial(quoteToLer).charAt(0));
				response.setIsMultiCircuit(quoteToLer.get().getIsMultiCircuit() == 1 ? true : false);
			}
			
			if (!quoteToLeTerm.isPresent()) {
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
		LOGGER.info("Response for get quote API for quote ---> {} is ---> {} ", quoteId,response);
		return response;
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
			throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}

		return quote;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @throws TclCommonException
	 */

	protected QuoteBean constructQuote(Quote quote, Boolean isFeasibleSites, Boolean isSiteProperitiesRequired,
			Integer siteId, Boolean manualFeasibility) throws TclCommonException {
		QuoteBean quoteDto = new QuoteBean();
		quoteDto.setQuoteId(quote.getId());
		quoteDto.setQuoteCode(quote.getQuoteCode());
		quoteDto.setCreatedBy(quote.getCreatedBy());
		quoteDto.setCreatedTime(quote.getCreatedTime());
		quoteDto.setStatus(quote.getStatus());
		quoteDto.setTermInMonths(quote.getTermInMonths());
		quoteDto.setNsQuote(quote.getNsQuote()!=null?(quote.getNsQuote().equals(CommonConstants.Y)?CommonConstants.Y:CommonConstants.N):CommonConstants.N);
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
		LOGGER.info("quote to le is fetched for quote id {}", quote.getId());
		if (quoteToLe.size() != 0) {
			quoteDto.setQuoteStatus(quoteToLe.get(0).getCommercialStatus());
			quoteDto.setQuoteRejectionComment(quoteToLe.get(0).getQuoteRejectionComment());
			if (quoteToLe.get(0).getCommercialQuoteRejectionStatus() != null) {
				if (quoteToLe.get(0).getCommercialQuoteRejectionStatus().equalsIgnoreCase("1")) {
					quoteDto.setQuoteRejectionStatus(true);
				} else {
					quoteDto.setQuoteRejectionStatus(false);
				}
				LOGGER.info(" quote Commercial Status and rejection status and comments  is set as  {}", quoteToLe.get(0).getCommercialStatus()+":"+quoteToLe.get(0).getCommercialQuoteRejectionStatus()
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
		quoteDto.setLegalEntities(
				constructQuoteLeEntitDtos(quote, isFeasibleSites, isSiteProperitiesRequired, siteId, quoteToLe, manualFeasibility));

		OrderConfirmationAudit auditEntity = orderConfirmationAuditRepository
				.findByOrderRefUuid(quoteDto.getQuoteCode());
		if (auditEntity != null) {
			quoteDto.setPublicIp(getPublicIp(auditEntity.getPublicIp()));
		} else {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			String forwardedIp = request.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
			LOGGER.info("Audit Public IP is {} ", forwardedIp);
			if (forwardedIp != null) {
				quoteDto.setPublicIp(getPublicIp(forwardedIp));
			}
		}
		CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quoteDto.getQuoteCode(),
				Source.MANUAL_COF.getSourceType());
		if (cofDetail != null) {
			quoteDto.setIsManualCofSigned(true);
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
		order.setEngagementOptyId(quote.getEngagementOptyId());
		orderRepository.save(order);
		order.setOrderToLes(constructOrderToLe(quote, order, detail));
		return order;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 * @param quote
	 * @throws TclCommonException
	 */
	private Set<QuoteToLeBean> constructQuoteLeEntitDtos(Quote quote, Boolean isFeasibleSites,
			Boolean isSiteProperitiesRequired, Integer siteId, List<QuoteToLe> quoteToLe, Boolean manualFeasibility) throws TclCommonException {

		Set<QuoteToLeBean> quoteToLeDtos = new HashSet<>();
		for (QuoteToLe quTle : quoteToLe) {
			LOGGER.info("Fetching quoteToLe {}", quTle.getId());
			QuoteToLeBean quoteToLeDto = new QuoteToLeBean(quTle);
			quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
			quoteToLeDto.setCurrency(quTle.getCurrencyCode());
			quoteToLeDto.setLegalAttributes(constructLegalAttributes(quTle));
			LOGGER.info("To set product families in quote to le dto");
			quoteToLeDto.setProductFamilies(constructQuoteToLeFamilyDtos(getProductFamilyBasenOnVersion(quTle),
					isFeasibleSites, isSiteProperitiesRequired, siteId, manualFeasibility));
			quoteToLeDto.setClassification(quTle.getClassification());
			quoteToLeDto.setIsMultiCircuit(quTle.getIsMultiCircuit());
			if(Objects.nonNull(quTle.getIsDemo())){
				LOGGER.info("Demo flag is not null for quote ---> {} " , quTle.getQuote().getQuoteCode());
				int result = Byte.compare(quTle.getIsDemo(), BACTIVE);
				if(result==0){
					LOGGER.info("Entered into the block to set demo info in get quote details for quote -----> {} " , quTle.getQuote().getQuoteCode());
					quoteToLeDto.setIsDemo(true);
					quoteToLeDto.setDemoType(quTle.getDemoType());
				}
			}

			quoteToLeDtos.add(quoteToLeDto);
			partnerService.setExpectedArcAndNrcForPartnerQuote(quote.getQuoteCode(), quoteToLeDto);
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
			LOGGER.info("getting quote to le attributes for quoteLe id {}", quTle.getId());
			attributeValues.stream().forEach(attrVal -> {
				LegalAttributeBean attributeBean = new LegalAttributeBean();

				attributeBean.setAttributeValue(attrVal.getAttributeValue());
				attributeBean.setDisplayValue(attrVal.getDisplayValue());
				attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
				leAttributeBeans.add(attributeBean);

			});

		}
		LOGGER.info("Legal attribute beans size {}", leAttributeBeans.size() );
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
				orderToLe.setIsAmended(Objects.nonNull(quoteToLe.getIsAmended())?quoteToLe.getIsAmended():BDEACTIVATE);
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
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setCreditCheckTrigerred(quoteToLe.getCreditCheckTriggered());
				orderToLe.setTpsSfdcCreditLimit(quoteToLe.getTpsSfdcCreditLimit());
				orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
				orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
				orderToLe.setIsMultiCircuit(quoteToLe.getIsMultiCircuit());
				orderToLe.setIsDemo(quoteToLe.getIsDemo());
				orderToLe.setDemoType(quoteToLe.getDemoType());
				orderToLe.setQuoteBulkUpdate(quoteToLe.getQuoteBulkUpdate());
				orderToLe.setSiteLevelBilling(quoteToLe.getSiteLevelBilling());
				orderToLeRepository.save(orderToLe);
				orderToLe.setOrdersLeAttributeValues(constructOrderToLeAttribute(orderToLe, quoteToLe));
				detail.getOrderLeIds().add(orderToLe.getId());
				orderToLe
						.setOrderToLeProductFamilies(getOrderProductFamilyBasenOnVersion(quoteToLe, orderToLe, detail));
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

			}
			
			
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
		LOGGER.info("Getting product solution of familyid {} ", family.getId());
		productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(family);
		return productSolutions;

	}

	/**
	 * @author VIVEK KUMAR K
	 * 
	 * @link http://www.tatacommunications.com/
	 * 
	 * @throws TclCommonException
	 * 
	 */

	private List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution, Integer siteId) {

		List<QuoteIllSite> illsites = new ArrayList<>();

		if (siteId != null) {

			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(siteId);

			if (quoteIllSite.isPresent()) {

				illsites.add(quoteIllSite.get());

			}

		} else {

			illsites = illSiteRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);

		}

		return illsites;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @param isSitePropertiesNeeded
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */

	private List<QuoteProductComponent> getComponentBasenOnVersion(Integer siteId, boolean isSitePropertiesNeeded,
			boolean isSitePropNeeded) {
		List<QuoteProductComponent> components = null;
		if (isSitePropertiesNeeded) {
			LOGGER.info("Getting quote Product Component for siteId {}", siteId);
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
					siteId, IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),
					QuoteConstants.ILLSITES.toString());
			LOGGER.info("Fetched quote Product Component for siteId {}", siteId);
		} else if (isSitePropNeeded) {
			components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
					QuoteConstants.ILLSITES.toString());
		} else {
			components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
					QuoteConstants.ILLSITES.toString());
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
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteProductComponentsAttributeValue> getAttributeBasenOnVersion(Integer componentId,
			boolean isSitePropRequire, Boolean isSiteRequired) {
		List<QuoteProductComponentsAttributeValue> attributes = null;

		attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);

		if (isSitePropRequire) {
			attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentId,
							IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
		} else if (isSiteRequired) {
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
		LOGGER.info("Get quote to le product family by quote id");
		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		LOGGER.info("size of prod family fetched is {} ", prodFamilys.size());
		return prodFamilys;

	}

	/**
	 * @author VIVEK KUMAR K
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
//				if (!PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				if (!partnerService.quoteCreatedByPartner(quote.getQuote().getId())) {
					processEngagement(quote, quoteToLeProductFamily);
				}
//				}
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
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructQuoteToLeFamilyDtos
	 * @param quoteToLeProductFamilies
	 * @throws TclCommonException
	 */
	private Set<QuoteToLeProductFamilyBean> constructQuoteToLeFamilyDtos(
			List<QuoteToLeProductFamily> quoteToLeProductFamilies, Boolean isFeasibleSites,
			Boolean isSiteProperitiesRequired, Integer siteId, Boolean manualFeasibility) throws TclCommonException {
		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilyBeans = new HashSet<>();
		if (quoteToLeProductFamilies != null) {
			LOGGER.info("Iterating through quoteToLeProductFamilies");
			for (QuoteToLeProductFamily quFamily : quoteToLeProductFamilies) {
				QuoteToLeProductFamilyBean quoteToLeProductFamilyBean = new QuoteToLeProductFamilyBean();
				if (quFamily.getMstProductFamily() != null) {
					quoteToLeProductFamilyBean.setStatus(quFamily.getMstProductFamily().getStatus());
					quoteToLeProductFamilyBean.setProductName(quFamily.getMstProductFamily().getName());
				}
				LOGGER.info("Status and product name are {} , {} ", quoteToLeProductFamilyBean.getStatus(), quoteToLeProductFamilyBean.getProductName());
				List<ProductSolutionBean> solutionBeans = getSortedSolution(
						constructProductSolution(getProductSolutionBasenOnVersion(quFamily), isFeasibleSites,
								isSiteProperitiesRequired, siteId, manualFeasibility));
				quoteToLeProductFamilyBean.setSolutions(solutionBeans);
				quoteToLeProductFamilyBeans.add(quoteToLeProductFamilyBean);

			}
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
			Boolean isFeasibleSites, Boolean isSiteProperitiesRequired, Integer siteId, Boolean manualFeasibility) throws TclCommonException {
		List<ProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				LOGGER.info("IProduct solution data is {} ", solution.toString());
				ProductSolutionBean productSolutionBean = new ProductSolutionBean();
				productSolutionBean.setProductSolutionId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					productSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					productSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					productSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}
				if (solution.getProductProfileData() != null) {
					LOGGER.info("Product solution product profile data is {} ", solution.getProductProfileData());
					productSolutionBean.setSolution((SolutionDetail) Utils
							.convertJsonToObject(solution.getProductProfileData(), SolutionDetail.class));
				}
				LOGGER.info("Getting illSite bean");
				List<QuoteIllSiteBean> illSiteBeans = getSortedIllSiteDtos(constructIllSiteDtos(
						getIllsitesBasenOnVersion(solution, siteId), isFeasibleSites, isSiteProperitiesRequired, manualFeasibility));
				LOGGER.info("Fetched illSite bean");
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
				List<QuoteIllSite> quoteIllSites = getIllsitesBasenOnVersion(solution, null);
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
			Boolean isSiteProperitiesRequired, Boolean manualFeasibility) throws TclCommonException {

		if (isSiteProperitiesRequired == null) {
			isSiteProperitiesRequired = false;
		}

		List<QuoteIllSiteBean> sites = new ArrayList<>();
		if (illSites != null) {
			for (QuoteIllSite illSite : illSites) {
				if (illSite.getStatus() == 1) {
					if (!isFeasibleSites && !illSite.getFeasibility().equals(CommonConstants.BACTIVE)) {
						continue;
					}
					// Quote quote =
					// illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote();
					QuoteToLe quoteToLe = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					QuoteIllSiteBean illSiteBean = new QuoteIllSiteBean(illSite);
					illSiteBean.setQuoteSla(constructSlaDetails(illSite));
					illSiteBean.setFeasibility(constructSiteFeasibility(illSite));
					List<QuoteProductComponentBean> quoteProductComponentBeans = getSortedComponents(
							constructQuoteProductComponent(illSite.getId(), false, isSiteProperitiesRequired));
					illSiteBean.setComponents(quoteProductComponentBeans);
					illSiteBean.setChangeBandwidthFlag(illSite.getMacdChangeBandwidthFlag());
					illSiteBean.setIsTaskTriggered(illSite.getIsTaskTriggered());
					//add rejection flag
					if (illSite.getCommercialRejectionStatus() != null) {
						if (illSite.getCommercialRejectionStatus().equalsIgnoreCase("1")) {
							illSiteBean.setRejectionStatus(true);
						} else {
							illSiteBean.setRejectionStatus(false);
						}
					}
					//approve flag
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
			   	illSiteBean.setMfStatus(illSite.getMfStatus());

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
						LOGGER.info("billing site gst no ill"+billinginfo.getGstNo()+"siteid:::"+illSite.getId());
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
									constructQuoteProductComponent(illSite.getId(), false, isSiteProperitiesRequired));
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
							if(quoteToLe.getQuoteType() != null && (quoteToLe.getQuoteType().equalsIgnoreCase("MACD")
									|| quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE))) {
								illSiteBeanSecondary
								.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe,
										quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE) ? illSiteBeanSecondary : illSiteBean, manualFeasibility)));
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
		String secondaryServiceId = null;

		String amended = "";
		if (quoteToLe.getIsAmended() != null
				&& quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE) && MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			amended="yes";
		}

		if (Objects.nonNull(quoteToLe) && (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
				  || MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType()))
				&& Objects.nonNull(serviceId) ) {
			Map<String, Object> primaryComponentsMap = new HashMap<>();
			Map<String, Object> secondaryComponentsMap = new HashMap<>();
			Boolean isSecondary = false;
			// PIPF-167 - show service details in MF workbench even if service id is inactive after order is placed.
			//PIPF-418 to add existing components for amendment in cwb
			SIServiceDetailDataBean sIServiceDetailDataBean = null;
			if (Boolean.TRUE.equals(manualFeasibility) || "yes".equalsIgnoreCase(amended)) {
				LOGGER.info("constructExistingComponentsforIsvPage manualFeasibility {}, serviceId {}",
						manualFeasibility, serviceId);
				sIServiceDetailDataBean = macdUtils.getServiceDetailForInactiveServicesOnceOrderIsPlaced(serviceId,
						quoteToLe.getQuoteCategory(), quoteToLe);

			} else if (MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(serviceId);
			} else {
				sIServiceDetailDataBean = macdUtils.getServiceDetail(serviceId, quoteToLe.getQuoteCategory());
			}

			LOGGER.info("Si service detail bean in constructExistingComponentsforIsvPage is ----> {} ", sIServiceDetailDataBean);

			SIServiceDetailDataBean secondaryaryServiceDataBean = new SIServiceDetailDataBean();
			SIServiceDetailDataBean primaryServiceDataBean = new SIServiceDetailDataBean();
			isSecondary = Objects.nonNull(sIServiceDetailDataBean.getPriSecServLink());
			String linkType = sIServiceDetailDataBean.getLinkType();

			if (linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SINGLE")) {
				if (isSecondary) {
					secondaryServiceId = sIServiceDetailDataBean.getPriSecServLink();
					LOGGER.info("Primary service Id is ----> {} and Secondary service ID is -----> {} ",serviceId, secondaryServiceId);

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
				secondaryServiceId=sIServiceDetailDataBean.getTpsServiceId();
				serviceId = sIServiceDetailDataBean.getPriSecServLink();
				LOGGER.info("Secondary service ID is -----> {}  and primary service Id is ----> {} ", secondaryServiceId,serviceId);
				// PIPF-167 - show service details in MF workbench even if service id is inactive after order is placed. 
				if(Boolean.TRUE.equals(manualFeasibility)) {
					LOGGER.info("constructExistingComponentsforIsvPage manualFeasibility {}, serviceId {}", manualFeasibility, serviceId);
					primaryServiceDataBean = macdUtils.getServiceDetailForInactiveServicesOnceOrderIsPlaced(serviceId, quoteToLe.getQuoteCategory(), quoteToLe);
					
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
				secondaryComponentsMap.put("serviceId", secondaryServiceId);

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


				if(quoteToLe.getQuoteCategory() != null && quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)){
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
		siteFeasibilityBean.setFeasibilityType(siteFeasibility.getFeasibilityType());
		siteFeasibility.setSfdcFeasibilityId(siteFeasibility.getSfdcFeasibilityId());
		siteFeasibilityBean.setIsSelected(siteFeasibility.getIsSelected());
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
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date()); // Now use today date.
					cal.add(Calendar.DATE, 60); // Adding 60 days
					orderSite.setEffectiveDate(cal.getTime());
					orderSite.setMrc(illSite.getMrc());
					orderSite.setFpStatus(illSite.getFpStatus());
					orderSite.setArc(illSite.getArc());
					orderSite.setTcv(illSite.getTcv());
					orderSite.setSiteCode(illSite.getSiteCode());
					orderSite.setStage(SiteStagingConstants.CONFIGURE_SITES.getStage());
					orderSite.setNrc(illSite.getNrc());
					orderSite.setIsColo(illSite.getIsColo());
					orderSite.setSiteBulkUpdate(illSite.getSiteBulkUpdate());
					orderSite.setErfServiceInventoryTpsServiceId(illSite.getErfServiceInventoryTpsServiceId());
					orderIllSitesRepository.save(orderSite);
					orderSite.setOrderSiteFeasibility(constructOrderSiteFeasibility(illSite, orderSite));
					orderSite.setOrderIllSiteSlas(constructOrderSiteSla(illSite, orderSite));
					persistOrderSiteAddress(illSite.getErfLocSitebLocationId(), "b",String.valueOf(orderSite.getId()),"ILL_SITES");//Site
					persistOrderSiteAddress(illSite.getErfLocSiteaLocationId(), "a",String.valueOf(orderSite.getId()),"ILL_SITES");//Pop
					
				/*	
					Optional<OrderToLe> orderToLe = orderToLeRepository.findById(detail.getOrderLeIds().get(0));
					if(orderToLe.isPresent() && orderToLe.get().getOrderType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)){*/
                    String quoteType = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType();
					QuoteToLe quoteToLe = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					LOGGER.info("quoteToLe quote type {}",quoteType );
                    if(quoteType != null && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteType)){
                    	List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository.findByQuoteIllSite(illSite);
						constructOrderIllSiteToService(illSite, orderSite,quoteIllSiteServices);
						if (quoteToLe.getIsAmended() != null
								&& quoteToLe.getIsAmended().equals(CommonConstants.BACTIVE)) {
							updateAmendmentStatus(quoteIllSiteServices, quoteToLe);
						}
					} else  if(quoteType != null && MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteType)){
						List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository.findByQuoteIllSite(illSite);
						constructOrderIllSiteToServiceTermination(illSite, orderSite,quoteIllSiteServices);
					}

                    else {
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
	
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) || MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {

			List<OrderIllSiteToService> orderSiteToServiceList= orderIllSiteToServiceRepository.findByOrderIllSite(orderSite);
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
				LOGGER.info("ILL Fetching  service details for {} ",siteToService.getErfServiceInventoryTpsServiceId());
				if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
					serviceDetail = macdUtils.getServiceDetailIASTermination(siteToService.getErfServiceInventoryTpsServiceId());
				} else {
					serviceDetail = macdUtils.getServiceDetailIAS(siteToService.getErfServiceInventoryTpsServiceId());
				}
			} catch (TclCommonException e) {
				LOGGER.info("Error in persistQuoteSiteCommercialsAtServiceIdLevel {}", e);
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
				
			}
			LOGGER.info("ill Fetched  service details for {} and details {}",siteToService.getErfServiceInventoryTpsServiceId(), serviceDetail);
			LOGGER.info("service inventory MRC {}, nrc {} arc {}", serviceDetail.getMrc(), serviceDetail.getNrc(), serviceDetail.getArc());
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
					findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.ILLSITES.toString(), siteToService.getType());
			if(quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
				quoteProductComponentList.stream().forEach(quoteProductComponent -> {
					quotePriceList.stream().filter(quotePrice -> (quoteProductComponent.getId().equals(Integer.valueOf(quotePrice.getReferenceId())) 
							&& QuoteConstants.COMPONENTS.toString().equalsIgnoreCase(quotePrice.getReferenceName()))).forEach(quotePriceEntry -> {
								quotePriceEntry.setEffectiveMrc(quotePriceEntry.getEffectiveMrc() == null ? 0D : quotePriceEntry.getEffectiveMrc());
								quotePriceEntry.setEffectiveNrc(quotePriceEntry.getEffectiveNrc() == null ? 0D : quotePriceEntry.getEffectiveNrc());
								subTotalMrc[0] += quotePriceEntry.getEffectiveMrc();
								subTotalNrc[0] += quotePriceEntry.getEffectiveNrc();
					});
					
					LOGGER.info("sub total value after quote prd component loop  mrc {}, nrc {}", subTotalMrc[0], subTotalNrc[0]);
				});
				
				
				serviceDifferentialCommercial.setDifferentialMrc(subTotalMrc[0] - calculatedMrc);
				serviceDifferentialCommercial.setDifferentialNrc(subTotalNrc[0] - serviceDetail.getNrc());
				LOGGER.info("final diff mrc {}, final diff nrc {}", serviceDifferentialCommercial.getDifferentialMrc(), serviceDifferentialCommercial.getDifferentialNrc());
				quoteSiteCommercialList.add(serviceDifferentialCommercial);
				
				
			}
		});
		
		
		} else {
			
			List<QuoteProductComponent> quoteProductComponentListSecondary = quoteProductComponentRepository.findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.ILLSITES.toString(), PDFConstants.SECONDARY);
			
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
			
			
			List<QuoteProductComponent> quoteProductComponentListPrimary = quoteProductComponentRepository.findByReferenceIdAndReferenceNameAndType(illSite.getId(), QuoteConstants.ILLSITES.toString(), PDFConstants.PRIMARY);
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
				illSlaService.constructIllSiteSla(illSite, null);
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
				LocalDateTime localDateTime = LocalDateTime.now();
				orderSiteFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
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
			boolean isSitePropNeeded) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getComponentBasenOnVersion(id, isSitePropertiesNeeded,
				isSitePropNeeded);

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
								isSitePropertiesNeeded, isSitePropNeeded),id));
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
				.findByReferenceIdAndReferenceName(id, QuoteConstants.ILLSITES.toString());
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
		    LOGGER.info("Reference Id is ----> {} ", attributeValue.getId());
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
				//orderPriceRepository.save(orderPrice);
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
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues, Integer siteId) {
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean = new ArrayList<>();
		Optional<QuoteIllSite> illSite =  illSiteRepository.findById(siteId);
		//UnComment if need total ip count along with pool size
//		SolutionDetail soDetail = new SolutionDetail();
//		if(illSite.isPresent()) {
//			try {
//				soDetail = (SolutionDetail) Utils.convertJsonToObject(illSite.get().getProductSolution().getProductProfileData(),
//						SolutionDetail.class);
//			} catch (TclCommonException e) {
//				LOGGER.error("Inside illQuoteService.constructAttribute error while parsing product solution profile data for site id {}",siteId);
//			}
//			
//		}
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
				//UnComment if need total ip count along with pool size
				//Concat Total ip count with CIDR notation
//				if(qtAttributeValue.getName().equalsIgnoreCase(PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS) && !StringUtils.isAllBlank(qtAttributeValue.getAttributeValues())) {
//					if(illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType().equalsIgnoreCase("MACD")) {
//						qtAttributeValue.setAttributeValues(illQuotePdfService.setIpAttributes(qtAttributeValue.getAttributeValues(), PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail));
//					} else {
//						qtAttributeValue.setAttributeValues(attributeValue.getAttributeValues()+illQuotePdfService.setIpAttributes(qtAttributeValue.getAttributeValues(), PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail));
//					}
//				}
//				if(qtAttributeValue.getName().equalsIgnoreCase(PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS) && !StringUtils.isAllBlank(qtAttributeValue.getAttributeValues())) {
//					if(illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType().equalsIgnoreCase("MACD")) {
//						qtAttributeValue.setAttributeValues(illQuotePdfService.setIpAttributes(attributeValue.getAttributeValues(), PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail));
//					} else {
//						qtAttributeValue.setAttributeValues(qtAttributeValue.getAttributeValues()+illQuotePdfService.setIpAttributes(qtAttributeValue.getAttributeValues(), PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail));
//					}
//				}
//				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
//				if (productAttributeMaster != null) {
//					qtAttributeValue.setAttributeMasterId(productAttributeMaster.getId());
//					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
//					qtAttributeValue.setName(productAttributeMaster.getName());
//				}
				//Without concatenating total ip count.
				if(qtAttributeValue.getName().equalsIgnoreCase(PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS) && !StringUtils.isAllBlank(qtAttributeValue.getAttributeValues())) {
					if(illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType().equalsIgnoreCase("MACD")) {
						qtAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
					} else {
						qtAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
					}
				}
				if(qtAttributeValue.getName().equalsIgnoreCase(PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS) && !StringUtils.isAllBlank(qtAttributeValue.getAttributeValues())) {
					if(illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType().equalsIgnoreCase("MACD")) {
						qtAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
					} else {
						qtAttributeValue.setAttributeValues(qtAttributeValue.getAttributeValues());
					}
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
				QuoteBean bean = constructQuote(quote, false, false, null, null);
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
				DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
				if (docusignAudit != null) {
					throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
				}
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
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
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
					illQuotePdfService.processCofPdf(quote.getId(), null, nat, true, quoteLe.getId(), cofObjectMapper);
					User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
					String userEmail = null;
					if (userRepo != null) {
						userEmail = userRepo.getEmailId();
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
				}
			}
			// triggerClosedBCROnPlacingOrder(quote);
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

				LOGGER.info("Classification type: {}",orderToLe.getClassification());
				return true;
			}
			else if (StringUtils.isNotBlank(orderToLe.getClassification()) && SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification()))
			{
				LOGGER.info("Classification type: {}",orderToLe.getClassification());
				return true;
			}			
			else {
				for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
					for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {
						for (OrderIllSite orderIllSite : oProductSolution.getOrderIllSites()) {
							if(orderIllSite.getIsColo() != null && CommonConstants.BACTIVE.equals(orderIllSite.getIsColo())) {
								return false;
							} else {
							List<OrderSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
									.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
											"primary");
							for (OrderSiteFeasibility orderSiteFeasibility : orderSiteFeasibilities) {
								String feasibilityMode = orderSiteFeasibility.getFeasibilityMode();
								return true;

								// Enabled O2C for all feasibility modes except INTL
								/*if (!(feasibilityMode.equals("OnnetWL") || feasibilityMode.equals("Onnet Wireline") || feasibilityMode.equals("OnnetRF") || feasibilityMode.equals("Onnet Wireless"))) {
									LOGGER.info("The feasibility Mode is {} for site {}", feasibilityMode,
											orderIllSite.getId());
									return false;
								}*/
								/*else {
									status = true;
								}*/
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
		}
		return status;
	}
	
	protected boolean isNotBurstableBandwidth(OrderIllSite orderIllSite) {
		// IAS - Block Burstable Bandwidth orders in O2C
		boolean status = true;
		LOGGER.info("Inside isNotBurstableBandwidth method order site id {}", orderIllSite.getId());
		List<OrderProductComponent> ipPortComponentList = orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(orderIllSite.getId(), ComponentConstants.PORT_CMP.getComponentsValue(), PDFConstants.PRIMARY);
		if(ipPortComponentList != null && !ipPortComponentList.isEmpty()) {
			for(OrderProductComponentsAttributeValue attribute : ipPortComponentList.get(0).getOrderProductComponentsAttributeValues()) {
				LOGGER.info("primary attribute name {}, value {}", attribute.getProductAttributeMaster().getName(), attribute.getAttributeValues());
				if(PDFConstants.BUSTABLE_BW.equalsIgnoreCase(attribute.getProductAttributeMaster().getName()) && StringUtils.isNotBlank(attribute.getAttributeValues())) {
					status = false;
					break;
				}
				
			}
			
		}
		
		List<OrderProductComponent> ipPortComponentListSecondary = orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(orderIllSite.getId(), ComponentConstants.PORT_CMP.getComponentsValue(), PDFConstants.SECONDARY);
		if(ipPortComponentListSecondary != null && !ipPortComponentListSecondary.isEmpty()) {
			for(OrderProductComponentsAttributeValue attribute : ipPortComponentListSecondary.get(0).getOrderProductComponentsAttributeValues()) {
				LOGGER.info("secondary attribute name {}, value {}", attribute.getProductAttributeMaster().getName(), attribute.getAttributeValues());
				if(PDFConstants.BUSTABLE_BW.equalsIgnoreCase(attribute.getProductAttributeMaster().getName()) && StringUtils.isNotBlank(attribute.getAttributeValues())) {
					status = false;
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
			 * if (orderToLe.getIsDemo() != null &&
			 * CommonConstants.BACTIVE.equals(orderToLe.getIsDemo())) { return false; }
			 */

			if (orderToLe.getOrderCategory() != null && MACDConstants.ADD_SECONDARY.equalsIgnoreCase(orderToLe.getOrderCategory())) {
				return false;
			}

			//Disable O2C auto-trigger for Partner orders
			if (StringUtils.isNotBlank(orderToLe.getClassification()) && SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {

				LOGGER.info("Classification type: {}", orderToLe.getClassification());
				return false;
			} else if (StringUtils.isNotBlank(orderToLe.getClassification()) && SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
				LOGGER.info("Classification type: {}", orderToLe.getClassification());
				return false;
			} else {
				for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
					for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {
						for (OrderIllSite orderIllSite : oProductSolution.getOrderIllSites()) {
							if (orderIllSite.getIsColo() != null && CommonConstants.BACTIVE.equals(orderIllSite.getIsColo())) {
								return false;
							} else {
								List<OrderSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
										.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
												"primary");
								for (OrderSiteFeasibility orderSiteFeasibility : orderSiteFeasibilities) {
									String feasibilityMode = orderSiteFeasibility.getFeasibilityMode();
									if (!(feasibilityMode.equals("OffnetRF") || feasibilityMode.equals("Offnet Wireless"))) {
										LOGGER.info("The feasibility Mode is {} for site {}", feasibilityMode,
												orderIllSite.getId());
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
									LOGGER.info("Inside checkO2cEnabled isNotBurstableBandwidth loop, status returned {}", status);
								} else {
									status = false;
									LOGGER.info("Inside checkO2cEnabled isNotBurstableBandwidth loop, status returned {}", status);
								}
							}
						}
					}
				}
			}
		}
		return status;
	}

	/*
	 * private void triggerClosedBCROnPlacingOrder(Quote quote) {
	 * LOGGER.info("Inside the triggerClosedBCROnPlacingOrder {}",quote.getQuoteCode
	 * ()); for (QuoteToLe quoteLe : quote.getQuoteToLes()) { //Trigger ClosedBcr
	 * Process try { String custId
	 * =quoteLe.getQuote().getCustomer().getErfCusCustomerId().toString(); String
	 * attribute=null; String approverEmail=null; try { attribute = (String)
	 * mqUtils.sendAndReceive(customerSegment, custId,
	 * MDC.get(CommonConstants.MDC_TOKEN_KEY)); } catch (TclCommonException e) {
	 * LOGGER.error("Error in Ill sending closed bcr"+ExceptionUtils.getMessage(e));
	 * } if (Utils.getSource() != null) { LOGGER.info("userinfoUtills details" +
	 * Utils.getSource()); User user =
	 * userRepository.findByUsernameAndStatus(Utils.getSource(), 1); if(user!=null)
	 * { if
	 * (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
	 * approverEmail = user.getEmailId(); } else { String emailId = (String)
	 * mqUtils.sendAndReceive(customerAccountManagerEmail, custId,
	 * MDC.get(CommonConstants.MDC_TOKEN_KEY)); approverEmail = emailId; } } }
	 * LOGGER.info("userinfoUtills details validate"+Utils.getSource());
	 * if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId)) {
	 * omsSfdcService.processeClosedBcr(quote.getQuoteCode(),
	 * quoteLe.getTpsSfdcOptyId(), quoteLe.getCurrencyCode(),
	 * "India",attribute,"C0",true,approverEmail,null);
	 * LOGGER.info("Trigger closed bcr request in illQuoteService"); } else {
	 * LOGGER.
	 * info("Failed closed bcr request in illQuoteService customerAttribute/customerId is Empty"
	 * ); } } catch (TclCommonException e) {
	 * 
	 * LOGGER.warn("Problem in illQuoteService Trigger Closed Bcr Request");
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 * LOGGER.info("TriggerClosedBCROnPlacingOrder completed {}",quote.getQuoteCode(
	 * )); }
	 */
	/**
	 * processOrderFlatTable
	 * 
	 * @param order
	 * @throws TclCommonException
	 */
	/*
	 * public void processOrderFlatTable(Integer orderId) throws TclCommonException
	 * { LOGGER.info("Inside the order to flat table freeze"); Map<String, Object>
	 * requestparam = new HashMap<>(); requestparam.put("orderId", orderId);
	 * requestparam.put("productName", "IAS"); requestparam.put("userName",
	 * Utils.getSource()); mqUtils.send(odrProcessQueue,
	 * Utils.convertObjectToJson(requestparam),
	 * MDC.get(CommonConstants.MDC_TOKEN_KEY)); }
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
					&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType()) && Objects.nonNull(optionalQuoteToLe.get().getIsAmended()) && optionalQuoteToLe.get().getIsAmended()==0) {

					LOGGER.info("Entering into ILL approve quote {}",quoteId);
					illMacdService.approvedMacdManualQuotes(quoteId, ipAddress);

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
					updateManualOrderConfirmationAudit(ipAddress, order.getOrderCode());
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
						Integer userId = order.getCreatedBy();
						String userEmail = null;
						if (userId != null) {
							Optional<User> userDetails = userRepository.findById(userId);
							if (userDetails.isPresent()) {
								userEmail = userDetails.get().getEmailId();
							}
						}

						for (OrderToLe orderToLe : order.getOrderToLes()) {
							LOGGER.info("Order to le is ----> {} ", Optional.ofNullable(orderToLe));
							illQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
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
				// triggerClosedBCROnPlacingOrder(quote);
				if (detail.isManualFeasible()) {
					cloneQuoteForNonFeasibileSite(quote);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
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
					&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType()) && (optionalQuoteToLe.get().getIsAmended()==null ||optionalQuoteToLe.get().getIsAmended()!=1)) {
				illMacdService.approvedMacdDocusignQuotes(quoteuuId);
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
					// Trigger SFDC
					for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
						if (quoteLe.getQuoteType().equalsIgnoreCase("RENEWALS")) {
							omsSfdcService.processSiteDetailsRenewals(quoteLe);
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
						LOGGER.info("Cof signed date for quote code ---> {} before cof won recieved stage is ----> {} ",quote.getQuoteCode(),cofSignedDate);
						omsSfdcService.processUpdateOpportunity(cofSignedDate, quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
						LOGGER.info("Cof signed date for quote code ---> {} after cof won recieved stage is ----> {} ",quote.getQuoteCode(),cofSignedDate);
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
							illQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
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

				LOGGER.info("set to update the quote stage from order form to enrichment for refId {}", quoteuuId);
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					LOGGER.info(
							"Before updating the quote stage from order form to enrichment for refId {} with existing stage {}",
							quoteuuId, quoteLe.getStage());
					if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
						LOGGER.info("After updating the quote stage from order form to enrichment for refId {} stage is ---> {}",
								quoteuuId,quoteLe.getStage());
					}
				}
				// triggerClosedBCROnPlacingOrder(quote);
				if (detail.isManualFeasible()) {
					cloneQuoteForNonFeasibileSite(quote);
				}
				if (o2cEnableFlag.equalsIgnoreCase("true")) {
					LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
					// processOrderFlatTable(order.getId());
				} else {
					LOGGER.info("Order flat table is disabled");
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	protected void processOrderMailNotification(Order order, QuoteToLe quoteToLe, Map<String, String> cofObjectMapper,
			String userEmail) throws TclCommonException {
		String emailId = userEmail != null ? userEmail : customerSupportEmail;
		String leMail = getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),
				emailId, appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName, CommonConstants.IAS,
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
			LocalDateTime localDateTime = LocalDateTime.now();
			nonFeasibleSiteFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
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
				.findByReferenceIdAndReferenceName(quoteIllSite.getId(), QuoteConstants.ILLSITES.toString());
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
	public String getAccountManagersEmail(QuoteToLe quoteToLe) throws TclCommonException {
		return getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL.toString());
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

				QuoteBean quotBean = constructQuote(quoteToLe.getQuote(), false, false, null, null);
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
					} else {
						quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValueRepository
								.findById(attributeDetail.getAttributeId()).get();
					}
					if (quoteProductComponentsAttributeValue != null) {
						quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
						quoteProductComponentsAttributeValueRepository
								.save(quoteProductComponentsAttributeValue);
					}
				}
			}


		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
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
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteEntity.get().getId(),
							cmpDetail.getName(), QuoteConstants.ILLSITES.toString());
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
					quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
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
				/*
				 * quoteProductComponentsAttributeValue = new
				 * QuoteProductComponentsAttributeValue();
				 * quoteProductComponentsAttributeValue.setProductAttributeMaster(
				 * productAttributeMaster);
				 * quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.
				 * getValue()); quoteProductComponentsAttributeValue.setQuoteProductComponent(
				 * quoteProductComponent);
				 * quoteProductComponentsAttributeValue.setQuoteVersion(VersionConstants.ONE.
				 * getVersionNumber());
				 * quoteProductComponentsAttributeValueRepository.save(
				 * quoteProductComponentsAttributeValue);
				 */
				saveProductAttributevalue(productAttributeMaster, attributeDetail, quoteProductComponent,
						quoteProductComponentsAttributeValue);
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
	 * @param quoteIllSite
	 * @param localITContactId
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
		quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
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
		LOGGER.info("Getting master properties");
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
		LOGGER.info(" exing get master properties");
		return mstProductComponent;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException getSiteProperties used to get only site specific
	 *                            attributes
	 */
	public List<QuoteProductComponentBean> getSiteProperties(Integer quoteLeId, Integer siteId) throws TclCommonException {
		List<QuoteProductComponentBean> quoteProductComponentBeans = null;
		long is_order_enrichment_attributes_provided = 0;
		try {
			validateRequest(siteId);
			QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
			if (quoteIllSite == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe_Id(quoteLeId);
			if (!quoteLeAttributeValues.isEmpty()) {
				is_order_enrichment_attributes_provided = quoteLeAttributeValues.stream()
						.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName().equalsIgnoreCase("Is Order Enrichment Attributes Provided"))
						.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getAttributeValue().equalsIgnoreCase("Yes")).count();
			}

			if (is_order_enrichment_attributes_provided == 1) {
				quoteProductComponentBeans = getSortedComponents(constructQuoteProductComponent(quoteIllSite.getId(), false, true));
			} else {
				quoteProductComponentBeans = getSortedComponents(constructQuoteProductComponent(quoteIllSite.getId(), true, false));
			}


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
			Boolean variationMatrixCheck = false;
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
			if (!optionalQuoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			if(request.getAttributeName() != null) {
			MstOmsAttribute omsAttribute = getMstAttributeMaster(request, user);
			constructQuoteLeAttribute(request, omsAttribute, optionalQuoteToLe.get());
			}

			if (optionalQuoteToLe.isPresent()) {
                if (Objects.isNull(optionalQuoteToLe.get().getQuoteType())
                        || (!MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType())) && (Objects.nonNull(optionalQuoteToLe.get().getIsAmended()) && optionalQuoteToLe.get().getIsAmended()!=1)) {
                    if (optionalQuoteToLe.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
						optionalQuoteToLe.get().setStage(QuoteStageConstants.CHECKOUT.getConstantCode());
						quoteToLeRepository.save(optionalQuoteToLe.get());
					}
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
	}

	private void constructAttributes(Attributes attribute, QuoteToLe quoteToLe) {

		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getAttributeName());

		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			updateAttributes(attribute, quoteLeAttributeValues);

		} else {
			createAttribute(attribute, quoteToLe);

		}

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
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return legalEntityAttributes;
	}

	public QuoteDetail persistListOfQuoteLeAttributes(UpdateRequest request,Integer quoteToLeId) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(request);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			if(request.getQuoteToLe()==null) {
				request.setQuoteToLe(quoteToLeId);
			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
			if (!optionalQuoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			List<AttributeDetail> attributeDetails = request.getAttributeDetails();
			for (AttributeDetail attribute : attributeDetails) {
				if(attribute.getName() != null) {
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
	 * This method is to update site provisioning audit information
	 * 
	 * @param name
	 * @param publicIp
	 * @param craetedTime
	 * @param orderRefId
	 * @throws TclCommonException
	 */
	public void updateOrderSiteProvisioningAudit(String stage, String siteUuid) throws TclCommonException {

		try {
			if (!StringUtils.isEmpty(stage) || !StringUtils.isEmpty(siteUuid) || StringUtils.isBlank(stage)
					|| StringUtils.isBlank(siteUuid)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_NOT_FOUND);
			}

			String name = Utils.getSource();
			OrderSiteProvisionAudit orderSiteProvisioning = new OrderSiteProvisionAudit();
			orderSiteProvisioning.setStage(stage);
			orderSiteProvisioning.setUpdatedTime(new Date());
			orderSiteProvisioning.setSiteUuid(siteUuid);
			orderSiteProvisioning.setUpdatedBy(name);
			ordersiteProvsioningRepository.save(orderSiteProvisioning);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

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
				if(billAttr.getAttributeName() != null) {
					constructBillingAttribute(billAttr, quoteToLe);
				}

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
		if (quoteToLe.getQuoteType().equalsIgnoreCase(MACD) && attribute.getAttributeName().equalsIgnoreCase(BILLING_FREQUENCY)) {
			//to save billing frequency value from inventory for MACD quotes
			SIServiceDetailDataBean serviceDetail = null;
			if (Objects.nonNull(quoteToLe)) {
				if (quoteToLe.getQuoteType().equalsIgnoreCase(MACD)) {
					List<String> serviceIds = macdUtils.getServiceIds(quoteToLe);
					String serviceId = "";
					if (Objects.nonNull(serviceIds) && !serviceIds.isEmpty()) {
						serviceId = serviceIds.stream().findFirst().get();
					}
					try {
						serviceDetail = macdUtils.getServiceDetail(serviceId,quoteToLe.getQuoteCategory());
					} catch (TclCommonException e) {
						LOGGER.info("Error in retrieving Service Info to update Billing Frequency");
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
		}
		else
		{
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
					&& !attr.getMstOmsAttribute().getName().equalsIgnoreCase("Billing Currency")&&!attr.getMstOmsAttribute().getName().equalsIgnoreCase(CUSTOMER_CONTRACTING_ENTITY)) {
				if (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACD) && attr.getDisplayValue().equalsIgnoreCase(BILLING_FREQUENCY)) {
					//to save billing frequency value from inventory for MACD quotes
					SIServiceDetailDataBean serviceDetail = null;
					if (Objects.nonNull(quoteToLe)) {
						if (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACD)) {
							List<String> serviceIds = macdUtils.getServiceIds(quoteToLe.get());
							String serviceId = "";
							if (Objects.nonNull(serviceIds) && !serviceIds.isEmpty()) {
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
		if (StringUtils.isNotBlank(stage) && (SFDCConstants.PROPOSAL_SENT.toString().equals(stage))) {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				String sfdcId = quoteToLe.get().getTpsSfdcOptyId();
				omsSfdcService.processUpdateOpportunity(new Date(), sfdcId, stage, quoteToLe.get());
				creditCheckService.resetCreditCheckFields(quoteToLe.get());
			}
		}else if(StringUtils.isNotBlank(stage) && (SFDCConstants.VERBAL_AGREEMENT_STAGE.toString().equals(stage))) {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				String sfdcId = quoteToLe.get().getTpsSfdcOptyId();
				omsSfdcService.processUpdateOpportunity(new Date(), sfdcId, stage, quoteToLe.get());
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
					for (QuoteLeAttributeValue attrval : quToLe.getQuoteLeAttributeValues()) {
					//if(attrval.getDisplayValue()!= null && attrval.getAttributeValue()!= null) {
						if (attrval.getMstOmsAttribute() != null && attrval.getMstOmsAttribute().getName() != null){
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

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attributeInfo;
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
			User user;
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
             if(request.getUserName()==null) {
            	 user = getUserId(Utils.getSource());
             }else {
            	 user = getUserId(request.getUserName());
             }
             LOGGER.info("user "+user.toString());
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
				.findByReferenceIdAndMstProductComponentAndMstProductFamily(orderIllSite.getId(), mstProductComponent,
						mstProductFamily);
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
		quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
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
		if (orderProductComponents != null) {
			for (QuoteProductComponent orderProductComponent : orderProductComponents) {

				if (request.getAttributeDetails() != null) {
					for (AttributeDetail attributeDetail : request.getAttributeDetails()) {

						List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
								.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
						if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
							upateSitePropertiesAttribute(productAttributeMasters, attributeDetail,
									orderProductComponent);
						} else {
							createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

						}

					}
				}

			}
		}

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
		orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
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
	 * 
	 * Get Quote summary details from this API
	 * 
	 * @param page
	 * @param size
	 * @param searchTerm
	 * @param customerId
	 * @param legaalEntityId
	 * @return
	 * @throws TclCommonException
	 */
	public PagedResult<QuoteSummaryBean> getQuoteSummary(int page, int size, String searchTerm, Integer customerId,
			Integer legaalEntityId) throws TclCommonException {
		List<QuoteSummaryBean> ordersBeans = null;
		Page<Quote> quotes = null;
		try {
			ordersBeans = new ArrayList<>();
			if (StringUtils.isBlank(searchTerm) && customerId == null && legaalEntityId == null) {

				Specification<Quote> specs = QuoteSpecification
						.getQuotes(OrderStagingConstants.ORDER_CONFIRMED.toString());
				quotes = quoteRepository.findAll(specs, PageRequest.of(page - 1, size));
			} else {
				Specification<Quote> specs = QuoteIsvSpecification.getQuotes(legaalEntityId, customerId, searchTerm,
						searchTerm);
				quotes = quoteRepository.findAll(specs, PageRequest.of(page - 1, size));

			}
			if (quotes != null) {
				for (Quote quote : quotes.getContent()) {
					if (appEnv.equalsIgnoreCase(CommonConstants.PROD)
							&& isTestCustomer(quote.getCustomer().getCustomerName())) {
						continue;
					}
					constructSummaryForSV(quote, ordersBeans);
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
				Set<QuoteToLeProductFamily> prodFamily = quoteLe.getQuoteToLeProductFamilies();
				for (QuoteToLeProductFamily family : prodFamily) {
					if (quote.getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
						construcyFamilyForSVIzosdwan(family, bean);
					}else {
						construcyFamilyForSV(family, bean);
					}
				}
				if (prodFamily.isEmpty()) {
					bean = null;
				}
			}
		}
		if (bean != null)
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
	
	private void construcyFamilyForSVIzosdwan(QuoteToLeProductFamily family, QuoteSummaryBean bean) {
		if (family.getMstProductFamily().getName().equals(IzosdwanCommonConstants.IZOSDWAN_NAME)
				|| family.getMstProductFamily().getName().equals(IzosdwanCommonConstants.IZOSDWAN)) {
			QuoteFamilySVBean orderFamilySVBean = new QuoteFamilySVBean();
			orderFamilySVBean.setName(family.getMstProductFamily().getName());
			orderFamilySVBean.setFamilyId(family.getId());
			bean.getQuoteFamily().add(orderFamilySVBean);
		}
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
		quoteLeSVBean
				.setLegalEntityName(extractLeAttributes(quoteTLe, LeAttributesConstants.LEGAL_ENTITY_NAME.toString()));
		quoteLeSVBean.setAccountCuid(extractLeAttributes(quoteTLe, LeAttributesConstants.ACCOUNT_CUID.toString()));
		quoteLeSVBean.setQuoteType(quoteTLe.getQuoteType());
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
		bean.setNsQuote(quote.getNsQuote()!=null?(quote.getNsQuote().equals(CommonConstants.Y)?CommonConstants.Y:CommonConstants.N):CommonConstants.N);
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
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

	}

	public String getLeAttributes(QuoteToLe quoteTole, String attr) throws TclCommonException {
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
	public String findExistingCurrency(QuoteToLe quoteTole) throws TclCommonException {
		return quoteTole.getCurrencyCode();
	}

	/**
	 * @author Thamizhselvi Perumal Method to update Payment Currency attribute as
	 *         input currency
	 * @param quoteToLeId
	 * @param inputCurrency
	 * @throws TclCommonException
	 */
	public void updatePaymentCurrencyWithInputCurrency(QuoteToLe quotele, String inputCurrency)
			throws TclCommonException {
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
	public void updateQuoteToLeCurrencyValues(Quote quote, String inputCurrency, String existingCurrency)
			throws TclCommonException {
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
	 * @author Thamizhselvi Perumal Method to update quoteIllSites currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
	 */
	public void updateQuoteIllSitesCurrencyValues(QuoteToLe quoteToLe, String inputCurrency, String existingCurrency)
			throws TclCommonException {
		for (QuoteToLeProductFamily quoteLeProdFamily : quoteToLe.getQuoteToLeProductFamilies()) {
			for (ProductSolution prodSolution : quoteLeProdFamily.getProductSolutions()) {
				for (QuoteIllSite illSite : prodSolution.getQuoteIllSites()) {
					illSite.setArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getArc()));
					illSite.setMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getMrc()));
					illSite.setNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getNrc()));
					illSite.setTcv(omsUtilService.convertCurrency(existingCurrency, inputCurrency, illSite.getTcv()));
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
	public void updateQuotePriceCurrencyValues(Quote quote, String inputCurrency, String existingCurrency)
			throws TclCommonException {

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
	 * 
	 * @author ANNE NISHA
	 * 
	 *         cofDownloadAccountManagerNotification - This method is used notify
	 *         the account manager when a customer downloads the cof
	 * @param orderId, orderToLeId
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

	private MailNotificationBean populateMailNotificationBeanCof(String customerAccountName, String customerName,
			String userName, String accountManagerEmail, String orderId, String quoteLink, QuoteToLe quoteToLe)
			throws TclCommonException {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setCustomerAccountName(customerAccountName);
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderId);
		mailNotificationBean.setQuoteLink(quoteLink);
		mailNotificationBean.setProductName(IAS);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
			Integer partnerId = partnerService.getOpportunityByQuoteId(quoteToLe.getQuote().getId()).getPartnerId();
			mailNotificationBean.setCustomerName(setAccountOwnerName(String.valueOf(partnerId)));
		}
		return mailNotificationBean;
	}

	public TriggerEmailResponse delegateUserForSalesLogin(Integer quoteToLeId, String forwardedIp)
			throws TclCommonException {
		if (quoteToLeId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		try {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				String email = null;
				List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(
						AttributeConstants.CUSTOMER_CONTACT_EMAIL.toString(), CommonConstants.BACTIVE);
				if (!mstOmsAttributes.isEmpty()) {
					MstOmsAttribute mstOmsAttribute = mstOmsAttributes.get(0);
					List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute(quoteToLe.get(), mstOmsAttribute);
					if (quoteToLeAttribute != null && !quoteToLeAttribute.isEmpty()) {
						email = quoteToLeAttribute.get(0).getAttributeValue();
					}
				}
				if (email != null) {
					return processTriggerMail(constructTriggerEmailRequest(email, quoteToLeId), forwardedIp);
				}
			} else {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	public TriggerEmailRequest constructTriggerEmailRequest(String email, Integer quoteToLeId) {
		TriggerEmailRequest triggerEmailRequest = new TriggerEmailRequest();
		triggerEmailRequest.setEmailId(email);
		triggerEmailRequest.setQuoteToLeId(quoteToLeId);
		return triggerEmailRequest;
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
		serviceScheduleBean.setDisplayName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setIsSSUploaded(true);
		serviceScheduleBean.setName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setProductName(SFDCConstants.IAS);
		return serviceScheduleBean;
	}

	private boolean isTestCustomer(String customerName) {
		if (testCustomers != null)
			for (String testCustomer : testCustomers) {
				if (testCustomer.contains(customerName)) {
					return true;

				}
			}
		return false;
	}

	public void deleteQuote(Integer quoteId) throws TclCommonException {
		LOGGER.info("Quote id {} ", quoteId);
		try {
			LOGGER.info("Inside Delete Quote");
			if (Objects.isNull(quoteId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			
			if(tasksPending(quoteId)) {
				throw new TclCommonException(ExceptionConstants.TASKS_PENDING_FOR_QUOTE, ResponseResource.R_CODE_ERROR);
			}
			LOGGER.info("Task Not Pending");
			Optional<Quote> quoteToDelete = quoteRepository.findById(quoteId);

			if (quoteToDelete.isPresent()) {
				Quote quote = quoteToDelete.get();
				Optional<Order> order = orderRepository.findByQuote(quote);
				if(order.isPresent()) {
					throw new TclCommonException(ExceptionConstants.TASKS_PENDING_FOR_QUOTE, ResponseResource.R_CODE_ERROR);
				}
				LOGGER.info("Quote {}", quote.toString());
				quote.getQuoteToLes().stream().forEach(quoteToLe -> {
					try {
						LOGGER.info("SFDC Update Opportunity - CLOSED DROPPED");
						// SFDC Update Opportunity - CLOSED DROPPED
						omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_DROPPED, quoteToLe);
						quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteToLeProdFamily -> {
							if (quoteToLeProdFamily.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.NPL.toString()) ||
									quoteToLeProdFamily.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.NDE.toString())) {
								if (order.isPresent())
									deleteNPLOrderRelatedDetails(order.get());
								deleteNPLQuotes(quoteToLeProdFamily);

							} else {
								LOGGER.info("DELETE REQUEST FOR ORDER EXCEPT NPL");
								if (order.isPresent())
									//deleteOrderRelatedDetails(order.get());
								quoteToLeProdFamily.getProductSolutions().stream().forEach(prodSolution -> {
									prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
												.findByReferenceIdAndReferenceName(illSite.getId(),
														QuoteConstants.ILLSITES.toString());
										LOGGER.info("QuoteProductComponentList is {}", quoteProductComponentList);
										quoteProductComponentList.stream().forEach(quoteProdComponent -> {
											deleteQuoteProductComponent(quoteProdComponent);
										});
										deleteFeasibilityDetails(illSite);
										deleteQuoteSiteToServiceDetails(illSite);
										illSiteRepository.delete(illSite);
									});
									productSolutionRepository.delete(prodSolution);
								});
							}
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
			LOGGER.error("Error",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}
	
	@SuppressWarnings("unused")
	private boolean tasksPending(Integer quoteId) {
		boolean[] tasksPending = {false};
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId).get(0);
		Set<QuoteToLeProductFamily> quoteProductFamilySet = quoteToLe.getQuoteToLeProductFamilies();
		quoteProductFamilySet.stream().forEach(quoteToLeProductFamily ->{
			Set<ProductSolution> productSolutions = quoteToLeProductFamily.getProductSolutions();
			productSolutions.stream().forEach(productSolution ->{
				Set<QuoteIllSite> quoteIllSites = productSolution.getQuoteIllSites();
				for (QuoteIllSite quoteIllSite : quoteIllSites) {
					if(((quoteIllSite.getMfTaskTriggered()!=null && quoteIllSite.getMfTaskTriggered() == 1) && StringUtils.isEmpty(quoteIllSite.getMfStatus())) || (quoteIllSite.getIsTaskTriggered()!=null && quoteIllSite.getIsTaskTriggered() == 1)) {
						tasksPending[0]=true;
					}
				}
				//tasksPending[0] = quoteIllSites.stream().filter(site -> (site.getMfTaskTriggered() == 1 && StringUtils.isEmpty(site.getMfStatus())) || site.getIsTaskTriggered() == 1).findAny().isPresent();
			} ); 

		} );
		
		return tasksPending[0];
	}
	
	private void deleteQuoteSiteToServiceDetails(QuoteIllSite illSite) {
		LOGGER.info("Inside in deleteQuoteSiteToServiceDetails");
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
		LOGGER.info("Inside Method - deleteFeasibilityDetails");
		List<SiteFeasibility> siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite(illSite);
		LOGGER.info("siteFeasibilityList is {}", siteFeasibilityList);
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

		List<PricingEngineResponse> pricingDetailList = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(illSite.getSiteCode(), "Discount");
		if (!pricingDetailList.isEmpty())
			pricingDetailsRepository.deleteAll(pricingDetailList);

		List<QuoteIllSiteSla> quoteIllSiteSlaList = quoteIllSiteSlaRepository.findByQuoteIllSite(illSite);
		if (!quoteIllSiteSlaList.isEmpty())
			quoteIllSiteSlaRepository.deleteAll(quoteIllSiteSlaList);

	}

	private void deleteQuoteLeAttributeValues(QuoteToLe quoteToLe) {
		LOGGER.info("Inside Method - deleteQuoteLeAttributeValues");
		List<QuoteLeAttributeValue> quoteLeAttributeValueList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe);
		if (!quoteLeAttributeValueList.isEmpty())
			quoteLeAttributeValueRepository.deleteAll(quoteLeAttributeValueList);
		List<QuoteDelegation> quoteDelegationList = quoteDelegationRepository.findByQuoteToLe(quoteToLe);
		if (!quoteDelegationList.isEmpty())
			quoteDelegationRepository.deleteAll(quoteDelegationList);
	}

	private void deleteQuoteProductComponent(QuoteProductComponent quoteProdComponent) {
		LOGGER.info("Inside Method - deleteQuoteProductComponent");
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
	 * New attribute value insert/update into the table while edit the confoquration
	 * 
	 * @param productAttributeMaster
	 * @param attributeDetail
	 * @param quoteProductComponent
	 * @param quoteProductComponentsAttributeValue
	 */

	protected void saveProductAttributevalue(ProductAttributeMaster productAttributeMaster,
			AttributeDetail attributeDetail, QuoteProductComponent quoteProductComponent,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);

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

				LOGGER.info("Deleting siteToService: For Link {}",link);
				List<QuoteIllSiteToService> siteToServiceList = quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(link.getId());
				if(siteToServiceList != null && !siteToServiceList.isEmpty())
					quoteIllSiteToServiceRepository.deleteAll(siteToServiceList);
			});

			nplLinkRepository.deleteByProductSolutionId(prodSolution.getId());
			illSiteRepository.deleteByProductSolution(prodSolution);
			productSolutionRepository.delete(prodSolution);
		});

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

	/**
	 * deleteQuoteFeasibilitySlaPriceDetails - deletes feasibility, SLA and price
	 * details for a link
	 * 
	 * @param link
	 */
	private void deleteQuoteFeasibilitySlaPriceDetails(QuoteNplLink link) {
		List<LinkFeasibility> linkFeasibilityList = linkFeasibilityRepository.findByQuoteNplLink(link);
		LOGGER.info("Deleting npl link with id {}",link.getId());
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

	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}");
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;
	}

	/**
	 * 
	 * getQuotePriceAudit - get the quote price audit trail component wise and
	 * attribute wise with the given quote id input
	 * 
	 * @param quoteId
	 */

	public QuotePriceAuditResponse getQuotePriceAudit(Integer quoteId) throws TclCommonException {
		if (Objects.isNull(quoteId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		QuotePriceAuditResponse response = new QuotePriceAuditResponse();
		List<QuotePriceAuditBean> quotePriceAuditList = new ArrayList<>();
		Set<Integer> locationIdsList = new HashSet<>();
		try {
			List<QuotePrice> quotePriceList = null;

			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if (quote.isPresent()) {
				quotePriceList = quotePriceRepository.findByQuoteId(quote.get().getId());
				quotePriceList.stream().forEach(quotePrice -> {

					List<QuotePriceAudit> priceAuditList = quotePriceAuditRepository.findByQuotePrice(quotePrice);
					priceAuditList.stream().forEach(priceAudit -> {
						QuotePriceAuditBean quotePriceAuditBean = new QuotePriceAuditBean(priceAudit);

						if (quotePrice.getReferenceName().equalsIgnoreCase(QuoteConstants.COMPONENTS.toString())) {
							Optional<QuoteProductComponent> quoteProductComponentOpt = quoteProductComponentRepository
									.findById(Integer.parseInt(quotePrice.getReferenceId()));
							if (quoteProductComponentOpt.isPresent()) {
								quotePriceAuditBean.setMstProductComponent(new MstProductComponentDto(
										quoteProductComponentOpt.get().getMstProductComponent()));
								Optional<QuoteIllSite> quoteIllSite = illSiteRepository
										.findById(quoteProductComponentOpt.get().getReferenceId());
								if (quoteIllSite.isPresent()) {
									locationIdsList.add(quoteIllSite.get().getErfLocSitebLocationId());
									quotePriceAuditBean.setLocationId(quoteIllSite.get().getErfLocSitebLocationId());
								}
							}

						} else if (quotePrice.getReferenceName()
								.equalsIgnoreCase(QuoteConstants.ATTRIBUTES.toString())) {
							Optional<QuoteProductComponentsAttributeValue> quoteProductComponentAttributeValueOpt = quoteProductComponentsAttributeValueRepository
									.findById(Integer.parseInt(quotePrice.getReferenceId()));
							if (quoteProductComponentAttributeValueOpt.isPresent()) {
								quotePriceAuditBean.setProductAttributeMaster(new ProductAttributeMasterBean(
										quoteProductComponentAttributeValueOpt.get().getProductAttributeMaster()));
								Optional<QuoteIllSite> quoteIllSite = illSiteRepository
										.findById(quoteProductComponentAttributeValueOpt.get()
												.getQuoteProductComponent().getReferenceId());
								if (quoteIllSite.isPresent()) {
									locationIdsList.add(quoteIllSite.get().getErfLocSitebLocationId());
									quotePriceAuditBean.setLocationId(quoteIllSite.get().getErfLocSitebLocationId());
								}
							}

						}
						quotePriceAuditList.add(quotePriceAuditBean);
					});
				});
				response.setQuotePriceAuditDetails(quotePriceAuditList);
				if (!locationIdsList.isEmpty())
					response.setLocationDetailsList(processLocationDetailsAndSendToLocationQueue(locationIdsList));
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;

	}

	private LocationDetail[] processLocationDetailsAndSendToLocationQueue(Set<Integer> locationIdsList) {

		try {
			String locCommaSeparated = locationIdsList.stream().map(i -> i.toString().trim())
					.collect(Collectors.joining(","));
			String response = (String) mqUtils.sendAndReceive(locationDetailsQueue, locCommaSeparated);
			LocationDetail[] addressDetail = (LocationDetail[]) Utils.convertJsonToObject(response,
					LocationDetail[].class);
			return addressDetail;
		} catch (Exception e) {
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}
		return null;

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
		msaBean.setProductName(SFDCConstants.IAS);
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
	public void updateTermsInMonthsForQuoteToLe(Integer quoteId, Integer quoteToLeId, UpdateRequest updateRequest,
			Integer taskId) throws TclCommonException {
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
			if (taskId != null) {
				// Updating in service fulfilment schema
				SiteDetailServiceFulfilmentUpdateBean siteDetailServiceFulfilmentUpdateBean = new SiteDetailServiceFulfilmentUpdateBean();
				siteDetailServiceFulfilmentUpdateBean.setTaskId(taskId);
				siteDetailServiceFulfilmentUpdateBean.setTermsInMonths(updateRequest.getTermInMonths());
				mqUtils.send(updateTermsInMonthsSiteDetailQueue,
						Utils.convertObjectToJson(siteDetailServiceFulfilmentUpdateBean));
			}
		}
	}

	/**
	 * getting order count based on customer.
	 * 
	 * @param customerId
	 * @throws TclCommonException
	 */
	public Long getCustomerOrderCount(List<Integer> customerIds) {
		long count = 0;
		List<String> stages = new ArrayList<>();
		stages.add(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		stages.add(QuoteStageConstants.GET_QUOTE.getConstantCode());
		stages.add(QuoteStageConstants.ADD_LOCATIONS.getConstantCode());
		stages.add(QuoteStageConstants.ORDER_FORM.getConstantCode());

		try {
			count = quoteToLeRepository.countByErfCusCustomerLegalEntityIdInAndStageIn(customerIds, stages);

		} catch (Exception ex) {
			LOGGER.error("error in get customer order count from oms", ex);
		}
		return count;
	}

	/**
	 * getting order count based on customer.
	 * 
	 * @param customerId
	 * @throws TclCommonException
	 */
	public Long getCustomerOrderCount(Integer customerId) {
		long count = 0;
		List<String> stages = new ArrayList<>();
		stages.add(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		stages.add(QuoteStageConstants.GET_QUOTE.getConstantCode());
		stages.add(QuoteStageConstants.ADD_LOCATIONS.getConstantCode());
		stages.add(QuoteStageConstants.ORDER_FORM.getConstantCode());

		try {
			List<Quote> quotes = quoteRepository.findByCustomerIdAndStatus(customerId, CommonConstants.BACTIVE);
			if (quotes != null && !quotes.isEmpty())
				count = quoteToLeRepository.countByQuoteInAndStageIn(quotes, stages);

		} catch (Exception ex) {
			LOGGER.error("error in get customer order count from oms", ex);
		}
		return count;
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

	@Transactional
	public List<OmsAttachmentBean> moveFilesToObjectStorage() throws TclCommonException {
		List<ObjectStorageListenerBean> listenerBeanList = new ArrayList<>();
		List<Integer> attachmentIdsList = new ArrayList<>();
		Set<String> customerLeIdList = new HashSet<>();
		List<OmsAttachmentBean> omsAttachmentBeanList = new ArrayList<>();

		// List<QuoteToLe> quoteToLeList = quoteToLeRepository.findAll();
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findAll();
		LOGGER.info("Attachment {}", omsAttachmentList.size());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachmentList.stream()
					.filter(omsAttach -> omsAttach.getQuoteToLe() != null
							&& omsAttach.getQuoteToLe().getErfCusCustomerLegalEntityId() != null)
					.forEach(omsAttachment -> {
						ObjectStorageListenerBean objectListenerBean = new ObjectStorageListenerBean();
						objectListenerBean.setAttachmentId(omsAttachment.getErfCusAttachmentId());
						objectListenerBean
								.setCustomerLeId(omsAttachment.getQuoteToLe().getErfCusCustomerLegalEntityId());
						objectListenerBean.setCustomerId(
								omsAttachment.getQuoteToLe().getQuote().getCustomer().getErfCusCustomerId());
						objectListenerBean.setOmsAttachmentId(omsAttachment.getId());
						customerLeIdList.add(omsAttachment.getQuoteToLe().getErfCusCustomerLegalEntityId().toString());
						listenerBeanList.add(objectListenerBean);
					});
			LOGGER.info("customer le id list {}", customerLeIdList.toString());

			String request = Utils.convertObjectToJson(listenerBeanList);
			mqUtils.sendAndReceive(objectStorageFileMoveQueue, request);
			List<CofDetails> cofDetailsListTemp = cofDetailsRepository.findAll();
			if (cofDetailsListTemp != null && !cofDetailsListTemp.isEmpty()) {
				cofDetailsListTemp.stream().forEach(cofDetials -> {
					Quote quote = quoteRepository.findByQuoteCode(cofDetials.getOrderUuid());
					if (Objects.nonNull(quote)) {
						Optional<QuoteToLe> quoteToLe = quote.getQuoteToLes().stream().findFirst();
						if (quoteToLe.isPresent() && Objects.nonNull(quoteToLe.get().getErfCusCustomerLegalEntityId()))
							customerLeIdList.add(quoteToLe.get().getErfCusCustomerLegalEntityId().toString());
					}
				});
			}

			String customerLeIdsStringList = customerLeIdList.stream().collect(Collectors.joining(","));
			LOGGER.info("customer le id string list{}", customerLeIdsStringList);
			String response = (String) mqUtils.sendAndReceive(customerCodeCustomerLeCodeQueue, customerLeIdsStringList);
			LOGGER.info("Response from Customer {}", response);
			ObjectStorageListenerBean[] objStorageListenerBeanArray = (ObjectStorageListenerBean[]) Utils
					.convertJsonToObject(response, ObjectStorageListenerBean[].class);
			List<ObjectStorageListenerBean> objStorageListenerBeanList = Arrays.asList(objStorageListenerBeanArray);
			if (objStorageListenerBeanList != null)
				LOGGER.info("customer code :: {}, customer Le Code :: {}",
						objStorageListenerBeanList.get(0).getCustomerCode(),
						objStorageListenerBeanList.get(0).getCustomerLeCode());

			if (cofDetailsListTemp != null && !cofDetailsListTemp.isEmpty()) {
				cofDetailsListTemp.stream().forEach(cofDetials -> {
					if (cofDetials != null) {

						Quote quote = quoteRepository.findByQuoteCode(cofDetials.getOrderUuid());
						if (quote != null) {
							File files = new File(cofDetials.getUriPath());
							if (files != null) {
								LOGGER.info("MOVE: file is present for cof detail id {}", cofDetials.getId());
								Optional<ObjectStorageListenerBean> objStorageListenerBean = objStorageListenerBeanList
										.stream()
										.filter(objStorageListener -> objStorageListener.getCustomerLeId()
												.equals(quote.getQuoteToLes().stream().findFirst().get()
														.getErfCusCustomerLegalEntityId()))
										.findFirst();
								if (objStorageListenerBean.isPresent()) {
									LOGGER.info(
											"MOVE: objStorageListenerBean present {}, customer code {}, customer le code {}, file exists {}",
											objStorageListenerBean.get().getCustomerLeId(),
											objStorageListenerBean.get().getCustomerCode(),
											objStorageListenerBean.get().getCustomerLeCode(), files.exists());

									if (files.exists() && objStorageListenerBean.get().getCustomerCode() != null
											&& objStorageListenerBean.get().getCustomerLeCode() != null) {
										try {
											LOGGER.info("before filestorageservice call");
											InputStream inputStream = new FileInputStream(files);
											// getClass().getClassLoader().getResourceAsStream(arg0)
											StoredObject storedObject = fileStorageService.uploadObject(files.getName(),
													inputStream, objStorageListenerBean.get().getCustomerCode(),
													objStorageListenerBean.get().getCustomerLeCode());

											String[] pathArray = storedObject.getPath().split("/");
											LOGGER.info("CofDEtials {}", cofDetials.getId());
											LOGGER.info("path :: {}, name :: {}", storedObject.getPath(),
													storedObject.getName());
											// OmsAttachmentBean omsReturnedBean =
											// illQuotePdfService.updateCofUploadedDetails(
											// quote.getId(),
											// quote.getQuoteToLes().stream().findFirst().get().getId(),
											// storedObject.getName(), pathArray[1]);
											AttachmentBean attachmentBean = new AttachmentBean();
											attachmentBean.setFileName(storedObject.getName());
											attachmentBean.setPath(pathArray[1]);
											String attachmentrequest = Utils.convertObjectToJson(attachmentBean);
											LOGGER.info(
													"MDC Filter token value in before Queue call processUploadFiles {} :",
													MDC.get(CommonConstants.MDC_TOKEN_KEY));
											Integer attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue,
													attachmentrequest);
											LOGGER.info("attachment id {} :", attachmentId);
											List<OmsAttachment> omsAttachment = omsAttachmentRepository
													.findByQuoteToLeAndAttachmentType(
															quote.getQuoteToLes().stream().findFirst().get(),
															AttachmentTypeConstants.COF.toString());
											if (omsAttachment.isEmpty()) {
												LOGGER.info("creating omsattachment e");
												OmsAttachment omsAttachmentE = new OmsAttachment();
												omsAttachment.add(omsAttachmentE);
											}
											if (!omsAttachment.isEmpty()) {
												if (omsAttachment.get(0).getId() != null)
													LOGGER.info("omsAttachment {} :", omsAttachment.get(0).getId());
												if (attachmentId != null && attachmentId != -1) {
													omsAttachment.get(0).setErfCusAttachmentId(attachmentId);
												}
												omsAttachment.get(0)
														.setAttachmentType(AttachmentTypeConstants.COF.toString());
												omsAttachment.get(0)
														.setQuoteToLe(quote.getQuoteToLes().stream().findFirst().get());
												Optional<Order> order = orderRepository.findByQuote(quote);
												if (order.isPresent()) {
													omsAttachment.get(0).setReferenceName(CommonConstants.ORDERS);
													omsAttachment.get(0).setReferenceId(order.get().getId());
													omsAttachment.get(0).setOrderToLe(
															order.get().getOrderToLes().iterator().next());
												} else {
													omsAttachment.get(0).setReferenceName(CommonConstants.QUOTES);
													omsAttachment.get(0).setReferenceId(quote.getId());
												}
												OmsAttachment omsAttach = omsAttachmentRepository
														.save(omsAttachment.get(0));
												omsAttachmentBeanList.add(new OmsAttachmentBean(omsAttach));
											}
											// omsAttachmentBeanList.add(objStorageListenerBean);
										} catch (Exception e) {
											LOGGER.warn("Exception" + ExceptionUtils.getStackTrace(e));
											throw new TclCommonRuntimeException(e.getMessage(), e);
										}
									}
								}
							}
						}
					}
				});
			}
		} else {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return omsAttachmentBeanList;
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
							&& !lconNameProductAttributeMasters.isEmpty() && lconRemarksProductAttributeMasters != null
							&& !lconRemarksProductAttributeMasters.isEmpty()) {
						if (lconUpdateBeans != null && !lconUpdateBeans.isEmpty()) {
							lconUpdateBeans.stream().forEach(lconUpdateBean -> {

								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponentAndMstProductFamily(
												lconUpdateBean.getSiteId(), mstProductComponents,
												productFamily.getMstProductFamily());
								if (quoteProductComponents != null && quoteProductComponents.isEmpty()) {
									QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
									quoteProductComponent.setMstProductComponent(mstProductComponents);
									quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
									quoteProductComponent.setReferenceId(lconUpdateBean.getSiteId());
									quoteProductComponent.setMstProductFamily(productFamily.getMstProductFamily());
									quoteProductComponent.setType("primary");
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
									// LCON Remarks
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

								QuoteIllSite illSite = illSiteRepository.findByIdAndMfStatus(lconUpdateBean.getSiteId(),
										"Return");
								if (illSite != null) {
									illSite.setMfStatus(null);
									illSiteRepository.save(illSite);
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
					if(req.getAttributeName() != null) {
					MstOmsAttribute omsAttribute = getMstAttributeMaster(req, user);
					constructLegaAttribute(omsAttribute, optionalQuoteToLe.get(), req.getAttributeName(),
							req.getAttributeValue());
					}
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
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
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

			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
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
	 * Sets the site as not feasible
	 * 
	 * @author Kavya Singh
	 * @param quoteToLeId
	 * @param siteId
	 */
	@Transactional
	public void siteNotFeasible(QuoteSiteNotFeasibleBean request) {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(request.getQuoteToLeId());
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
								.findByReferenceIdAndMstProductComponentAndMstProductFamily(request.getSiteId(),
										mstProductComponent, mstProdFamily);

						if (quoteProductComponent.isEmpty()) {
							QuoteProductComponent comp = constructProductComponent(mstProductComponent, mstProdFamily,
									request.getSiteId());

							quoteProductComponentRepository.save(comp);
							createFeasibleAttributes(comp);
							createFeasibleAttributesDescription(comp, request.getDescription());

						} else {

							quoteProductComponent.stream().forEach(quoteCmp -> {

								List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
										.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteCmp.getId(),
												QuoteConstants.IS_NOT_FEASIBLE.getConstantCode());

								if (attributeValues != null && attributeValues.isEmpty()) {
									createFeasibleAttributes(quoteCmp);
								} else {

									attributeValues.stream().forEach(attr -> {
										attr.setAttributeValues("true");
										quoteProductComponentsAttributeValueRepository.save(attr);

									});
								}

								List<QuoteProductComponentsAttributeValue> attributeValues2 = quoteProductComponentsAttributeValueRepository
										.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteCmp.getId(),
												QuoteConstants.NOT_FEASIBLE_DESC.getConstantCode());

								if (attributeValues2 != null && attributeValues2.isEmpty()) {
									createFeasibleAttributesDescription(quoteCmp, request.getDescription());
								} else {

									attributeValues2.stream().forEach(attr -> {
										attr.setAttributeValues(request.getDescription());
										quoteProductComponentsAttributeValueRepository.save(attr);

									});
								}

							});

						}
					}
				});
			}
		}

	}

	private QuoteProductComponentsAttributeValue createFeasibleAttributes(QuoteProductComponent quoteCmp) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteCmp);
		quoteProductComponentsAttributeValue
				.setProductAttributeMaster(getProductAttributeMaster(QuoteConstants.IS_NOT_FEASIBLE.getConstantCode()));
		quoteProductComponentsAttributeValue.setDisplayValue("true");
		quoteProductComponentsAttributeValue.setAttributeValues("true");
		quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);

		return quoteProductComponentsAttributeValue;

	}

	private QuoteProductComponentsAttributeValue createFeasibleAttributesDescription(QuoteProductComponent quoteCmp,
			String desc) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteCmp);
		quoteProductComponentsAttributeValue.setProductAttributeMaster(
				getProductAttributeMaster(QuoteConstants.NOT_FEASIBLE_DESC.getConstantCode()));
		quoteProductComponentsAttributeValue.setDisplayValue("NOT_FEASIBLE_DESC");
		quoteProductComponentsAttributeValue.setAttributeValues(desc);
		quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);

		return quoteProductComponentsAttributeValue;

	}

	private ProductAttributeMaster getProductAttributeMaster(String name) {
		ProductAttributeMaster productAttributeMaster = null;
		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(name, (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setCreatedBy(name);
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(name);
			productAttributeMaster.setName(name);
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;

	}

	/**
	 * getQuoteDataById method to get quote by Id
	 * 
	 * @param quoteId
	 * @return
	 */
	public QuoteDto getQuoteDataById(String quoteId) {
		LOGGER.info("Entering method for fetching Quote data for quoteId getQuoteDataById {}: ", quoteId);
		Quote quote = quoteRepository.findByIdAndStatus(Integer.parseInt(quoteId), (byte) 1);
		LOGGER.info(" findByIdAndStatus : getQuoteDataById {}: ", quote);
		if (quote != null) {
			return new QuoteDto(quote);
		}
		return null;
	}

	/**
	 * getQuoteLeById method to get QuoteToLe by Id
	 * 
	 * @param quoteLeId
	 * @return
	 */
	public QuoteToLeDto getQuoteLeById(String quoteLeId) {
		LOGGER.info("Entering method for fetching QuoteToLe date for quoteLeId getQuoteDataById {}: ", quoteLeId);
		Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(Integer.parseInt(quoteLeId));
		if (quoteToLes.isPresent()) {
			LOGGER.info(" findByIdAndStatus : getQuoteLeById {}: ", quoteToLes.get());
			return new QuoteToLeDto(quoteToLes.get());
		} else {
			return null;
		}
	}

	/**
	 * This method is used to delete the unselected solutions from quote
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param profilesToRemove
	 * @throws TclCommonException
	 */
	public void removeUnselectedSolutionsFromQuote(Integer quoteId, Integer quoteLeId, ProfileRequest profilesToRemove)
			throws TclCommonException {
		if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(profilesToRemove))
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
		if (Objects.isNull(quoteToLe))
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		if (quoteToLe.isPresent()) {
			quoteToLe.get().getQuoteToLeProductFamilies().forEach(quoteProductFamily -> {
				quoteProductFamily.getProductSolutions().forEach(productSolution -> {
					profilesToRemove.getProfilesToRemove().forEach(removeProfile -> {
						if (productSolution.getMstProductOffering().getProductName().equalsIgnoreCase(removeProfile)) {
							productSolution.getQuoteIllSites().forEach(illSite -> {
								removeComponentsAndAttr(illSite.getId());
								deletedIllsiteAndRelation(illSite);

							});
							// Trigger delete productSolution
							if (StringUtils.isNotBlank(quoteToLe.get().getTpsSfdcOptyId()))
								omsSfdcService.processDeleteProduct(quoteToLe.get(), productSolution);
							productSolutionRepository.delete(productSolution);

						}

					});
				});

			});
		}
	}

	/**
	 * @author ANANDHI VIJAY updateSiteAttributes for Commercial and Pilot team
	 * @param siteAttributeUpdateBean
	 * @param quoteToLeId
	 */
	public void updateSiteAttributesIsv(SiteAttributeUpdateBean siteAttributeUpdateBean, Integer quoteToLeId) {
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
									siteAttributeUpdateBean.getSiteId(), mstProductComponents,
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
								if (quoteProductComponentsAttributeValues != null
										&& quoteProductComponentsAttributeValues.isEmpty()) {
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
									if (quoteProductComponentsAttributeValue.getIsAdditionalParam() != null
											&& quoteProductComponentsAttributeValue.getIsAdditionalParam()
													.equals(CommonConstants.Y)) {
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
										LOGGER.info("-------quoteProductComponentsAttributeValue.getIsAdditionalParam() is not Y-----");
										quoteProductComponentsAttributeValue
												.setAttributeValues(attributeBean.getValue());
									}
									quoteProductComponentsAttributeValueRepository
											.save(quoteProductComponentsAttributeValue);
									LOGGER.info("-------Updated the properities---old-----");
								}
							}

						});
					}
				});
			}

		}

	}
	
	
	/**
	 * 
	 * Get Quote Details for commercial purpose
	 * 
	 * @param quoteCode
	 * @return
	 */
	public CommercialQuoteDetailBean getQuoteDetailsForCommercialPurpose(String quoteCode) {
		try {
			Quote quote = quoteRepository.findByQuoteCode(quoteCode);
			if (quote != null) {
				CommercialQuoteDetailBean commercialQuoteDetailBean = new CommercialQuoteDetailBean();
				List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(quote);
				if (quoteToLe != null && !quoteToLe.isEmpty()) {
					commercialQuoteDetailBean.setOptyId(quoteToLe.get(0).getTpsSfdcOptyId());
				}
				if (quote.getCustomer() != null) {
					commercialQuoteDetailBean.setAccountName(quote.getCustomer().getCustomerName());
				}
				User user = userRepository.findByIdAndStatus(quote.getCreatedBy(), CommonConstants.ACTIVE);
				if (user != null) {
					commercialQuoteDetailBean.setEmail(user.getEmailId());
				}

				return commercialQuoteDetailBean;
			}
		} catch (Exception e) {
			LOGGER.warn("Error in fetchin quote detail {}", e.getMessage());
		}
		return null;
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
	public String uploadDocument(Integer siteId, MultipartFile siteDocument) throws TclCommonException {
		String uploadStatus = CommonConstants.FAILIURE;
		try {
			LOGGER.info("Inside IllQuoteService.uploadDocument method to upload site document for siteId {} ", siteId);
			List<SiteFeasibility> siteFeasibilityResponse = siteFeasibilityRepository
					.findByQuoteIllSite_IdAndType(siteId, "Primary");
			if (siteFeasibilityResponse != null && !siteFeasibilityResponse.isEmpty()) {
				SiteFeasibility sitef = siteFeasibilityResponse.get(0);
				sitef.setSiteDocument(siteDocument.getBytes());
				sitef.setSiteDocumentName(siteDocument.getOriginalFilename());
				siteFeasibilityRepository.save(sitef);
				uploadStatus = CommonConstants.SUCCESS;
				LOGGER.info("Uploaded site document for siteId {} ", siteId);
			} else {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return uploadStatus;

	}

	/**
	 * This method download file
	 * 
	 * @param siteId
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public String downloadDocument(Integer siteId, HttpServletResponse response) throws TclCommonException {
		String fileName = null;
		try {
			LOGGER.info("Inside IllQuoteService.downloadDocument method to download site document for siteId {} ",
					siteId);
			List<SiteFeasibility> siteFeasibilityResponse = siteFeasibilityRepository
					.findByQuoteIllSite_IdAndType(siteId, FPConstants.PRIMARY.toString());
			if (siteFeasibilityResponse != null && !siteFeasibilityResponse.isEmpty()) {
				SiteFeasibility siteFeas = siteFeasibilityResponse.get(0);
				byte[] bytes = siteFeas.getSiteDocument();
				fileName = siteFeas.getSiteDocumentName();
				response.setContentType("application/octet-stream");
				response.setContentLength(bytes.length);
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
				response.setContentLength(bytes.length);
				FileCopyUtils.copy(bytes, response.getOutputStream());
				response.flushBuffer();
				LOGGER.info("Downloaded site document for siteId {} ", siteId);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return fileName;

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
						List<QuoteIllSite> quoteIllSites = illSiteRepository
								.findByProductSolutionAndStatus(prodSolution, (byte) 1);
						quoteIllSites.stream().forEach(ilSite -> {
							List<SiteFeasibility> sitefeas = siteFeasibilityRepository
									.findByQuoteIllSite_IdAndType(ilSite.getId(), "Primary");
							sitefeas.stream().forEach(sitef -> {
								if (sitef.getSiteDocumentName() != null) {
									SiteDocumentBean siteDocumentBean = new SiteDocumentBean();
									siteDocumentBean.setSiteId(sitef.getQuoteIllSite().getId());
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

	public OpportunityBean getOpportunityDetails(Integer quoteId, Integer siteId) throws TclCommonException {
		LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch opportunity details for the quoteId {} ",
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

				//	List<String> serviceIds=macdutils.getServiceIds(quoteToLe);
					List<String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSiteId(siteId,quoteToLe.getId());
					LOGGER.info("service ids for quote- {}", serviceIds);
					String serviceIdList=serviceIds.stream().findFirst().get();
					if(Objects.nonNull(quoteToLe.getIsMultiCircuit())&&CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
						serviceIds.remove(serviceIdList);
						serviceIds.forEach(serviceId -> {
							LOGGER.info("service id in loop {}", serviceIdList.toString());
							serviceIdList.concat("," + serviceId);
						});
					}
					LOGGER.info("service id list final {}", serviceIdList.toString());
					opporBean.setServiceId(serviceIdList);
				}
				}
			/*opporBean.setServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());*/

			opporBean.setOpportunityStage(SFDCConstants.PROPOSAL_SENT);
			opporBean.setOpportunityAccountName(quote.getCustomer().getCustomerName());
			Optional<User> user = userRepository.findById(quote.getCreatedBy());
			opporBean.setOpportunityOwnerEmail(user.get().getEmailId());
			opporBean.setOpportunityOwnerName(user.get().getUsername());
			List<SiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository
					.findByQuoteIllSite_IdAndType(siteId, "primary");
			Optional<SiteFeasibility> siteFeasibility = selectedSiteFeasibility.stream().findFirst();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(siteFeasibility.get().getResponseJson());
			opporBean.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.CUSTOMER_SEGMENT));
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(siteId, "ILL_SITES").stream()
					.filter(comp -> comp.getMstProductComponent().getName().equalsIgnoreCase("SITE_PROPERTIES"))
					.findFirst().orElse(null);

			if (quoteProductComponent != null) {
				LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch site properties for the siteId {} ",
						siteId);
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
				LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch opportunity stage");
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

	public String retriggerCreditCheck(Integer quoteId) throws TclCommonException {
		String[] creditCheckStatus = { null };
		String[] portalTransactionId = { null };
		String[] oldCreditControlStatus = { null };
		String productName = null;
		if (quoteId == null)
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		List<QuoteToLe> quoteToLeList = quoteToLeRepository.findByQuote_Id(quoteId);
		if (Objects.nonNull(quoteToLeList) && !quoteToLeList.isEmpty()) {

			QuoteToLe quoteToLe = quoteToLeList.get(0);

			// quoteToLeList.stream().forEach(quoteToLe -> {

			if (Objects.nonNull(quoteToLe.getTpsSfdcStatusCreditControl())
					&& !quoteToLe.getTpsSfdcStatusCreditControl().equals(CommonConstants.POSITIVE)) {
				LOGGER.info("Retrigger credit check query , opty Id {}", quoteToLe.getTpsSfdcOptyId());
				oldCreditControlStatus[0] = quoteToLe.getTpsSfdcStatusCreditControl();
				if (Objects.nonNull(quoteToLe.getTpsSfdcOptyId())) {
					SfdcCreditCheckQueryRequest queryRequest = new SfdcCreditCheckQueryRequest();
					queryRequest.setFields(SFDCConstants.CREDIT_CHECK_QUERY_FIELDS);
					queryRequest.setObjectName(SFDCConstants.CREDIT_CHECK_OBJECT_NAME);
					queryRequest.setSourceSystem(SFDCConstants.OPTIMUS.toString());
					queryRequest.setWhereClause(
							SFDCConstants.CREDIT_CHECK_WHERE_CLAUSE + "'" + quoteToLe.getTpsSfdcOptyId() + "'");
				
					queryRequest
							.setTransactionId(SFDCConstants.OPTIMUS.toString() + quoteToLe.getQuote().getQuoteCode());

					try {
						String mqResponse = (String) mqUtils.sendAndReceive(creditCheckRetriggerQueue,
								Utils.convertObjectToJson(queryRequest));
						LOGGER.info("Response from service queue - {}", mqResponse);
						CreditCheckQueryResponseBean creditCheckResponse = Utils.convertJsonToObject(mqResponse,
								CreditCheckQueryResponseBean.class);
						creditCheckResponse.getSfdcCreditCheckQueryResponse().stream().forEach(entry -> {
							QuoteLeCreditCheckAudit creditCheckAudit = new QuoteLeCreditCheckAudit();
							creditCheckAudit.setCreatedTime(new Timestamp(new Date().getTime()));
							creditCheckAudit.setCreatedBy(Utils.getSource());
							creditCheckAudit.setQuoteToLe(quoteToLe);
							if (Objects.nonNull(entry.getMrcNrc()) && StringUtils.isNotBlank(entry.getMrcNrc())) {
								String[] mrcNrcValues = entry.getMrcNrc().split("/");
								creditCheckAudit.setTpsSfdcApprovedMrc(new Double(mrcNrcValues[0]));
								creditCheckAudit.setTpsSfdcApprovedNrc(new Double(mrcNrcValues[1]));
							} else {
								creditCheckAudit.setTpsSfdcApprovedMrc(entry.getProductServices().getRecord().stream()
										.filter(rec -> Objects.nonNull(rec.getProductMRCc()))
										.mapToDouble(rec -> new Double(rec.getProductMRCc())).sum());
								creditCheckAudit.setTpsSfdcApprovedNrc(entry.getProductServices().getRecord().stream()
										.filter(rec -> Objects.nonNull(rec.getProductNRCc()))
										.mapToDouble(rec -> new Double(rec.getProductNRCc())).sum());
							}
							creditCheckAudit.setTpsSfdcDifferentialMrc(entry.getDifferentialMRC());
							creditCheckAudit.setTpsSfdcApprovedBy(entry.getApprovedBy());
							creditCheckAudit.setTpsSfdcCreditCheckStatus(entry.getStatusOfCreditControl());
							creditCheckAudit.setTpsSfdcCustomerName(entry.getCustomerName());
							creditCheckAudit.setTpsSfdcCuId(entry.getCustomerContractingEntityBean().getCustomerCode());
							quoteLeCreditCheckRepository.save(creditCheckAudit);
							quoteToLe.setCreditCheckTriggered(CommonConstants.BDEACTIVATE);
							if (Objects.nonNull(entry.getDateOfCreditApproval())) {
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
							quoteToLe.setTpsSfdcCreditRemarks(StringUtils.truncate(entry.getCreditRemarks(), 250));
							quoteToLe.setTpsSfdcDifferentialMrc(entry.getDifferentialMRC());
							quoteToLe.setTpsSfdcReservedBy(entry.getReservedBy());
							quoteToLe.setTpsSfdcStatusCreditControl(entry.getStatusOfCreditControl());
							if (entry.getCreditLimit() != null)
								quoteToLe.setTpsSfdcCreditLimit(entry.getCreditLimit());
							quoteToLe.setTpsSfdcSecurityDepositAmount(entry.getSecurityDepositAmount());
							quoteToLeRepository.save(quoteToLe);
							portalTransactionId[0] = entry.getPortalTransactionId();
						});

					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.CREDITCHECK_RETRIGGER_ERROR, e,
								ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					}

				}

				if (!oldCreditControlStatus[0].equals(quoteToLe.getTpsSfdcStatusCreditControl())) {
					creditCheckStatus[0] = quoteToLe.getTpsSfdcStatusCreditControl();
					if (Objects.nonNull(portalTransactionId) && Objects.nonNull(portalTransactionId[0]))
						try {
							Optional<User> userSource = userRepository
									.findById(quoteToLeList.get(0).getQuote().getCreatedBy());
							if (userSource.isPresent()) {
								LOGGER.info("user type {}", userSource.get().getUserType());
								if (quoteToLe.getTpsSfdcStatusCreditControl().equalsIgnoreCase(CommonConstants.POSITIVE)
										|| quoteToLe.getTpsSfdcStatusCreditControl()
												.equalsIgnoreCase(CommonConstants.NEGATIVE)
										|| (quoteToLe.getTpsSfdcStatusCreditControl()
												.equalsIgnoreCase(CommonConstants.RESERVED)
												&& UserType.INTERNAL_USERS.toString()
														.equalsIgnoreCase(userSource.get().getUserType())))
									creditCheckService.triggerCreditCheckStatusChangeMail(portalTransactionId[0]);
								if (quoteToLe.getTpsSfdcStatusCreditControl().equalsIgnoreCase(CommonConstants.POSITIVE)
										|| quoteToLe.getTpsSfdcStatusCreditControl()
												.equalsIgnoreCase(CommonConstants.NEGATIVE)) {
									/*
									 * List<String> serviceStatusList = new ArrayList<>();
									 * serviceStatusList.add(SfdcServiceStatus.NEW.toString());
									 * serviceStatusList.add(SfdcServiceStatus.INPROGRESS.toString());
									 * List<ThirdPartyServiceJob> serviceJob = thirdPartyServiceJobRepository.
									 * findByRefIdAndServiceStatusInAndThirdPartySource(quoteToLe.getQuote().
									 * getQuoteCode(), serviceStatusList, ThirdPartySource.CREDITCHECK.toString());
									 * if(serviceJob != null && !serviceJob.isEmpty()) {
									 * serviceJob.get(0).setServiceStatus(SfdcServiceStatus.SUCCESS.toString());
									 * serviceJob.get(0).setResponsePayload(mqResponse[0]);
									 * thirdPartyServiceJobRepository.save(serviceJob.get(0)); }
									 */
								}
							}

						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.CREDITCHECK_RETRIGGER_ERROR, e,
									ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
						}
				} else
					creditCheckStatus[0] = quoteToLe.getTpsSfdcStatusCreditControl();
			} else if (Objects.nonNull(quoteToLe.getTpsSfdcStatusCreditControl())
					&& quoteToLe.getTpsSfdcStatusCreditControl().equals(CommonConstants.POSITIVE))
				creditCheckStatus[0] = CommonConstants.POSITIVE;
			else {
				QuoteToLeProductFamily quoteLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_Id(quoteToLe.getId());
				productName = quoteLeProductFamily.getMstProductFamily().getName() != null
						? quoteLeProductFamily.getMstProductFamily().getName()
						: "";

				LOGGER.info("Sending the customerLeIds as {}", quoteToLe.getErfCusCustomerLegalEntityId());
				LOGGER.info("MDC Filter token value in before Queue call creditCheckBasedOnPreapprovedValue {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String response = (String) mqUtils.sendAndReceive(customerLeCreditCheckQueue,
						String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()).concat(",").concat(productName));
				LOGGER.info("Response from customerLeCreditCheckQueue: " + response);
				if (response != null) {
					CustomerLeVO customerLeDetails = (CustomerLeVO) Utils.convertJsonToObject(response,
							CustomerLeVO.class);
					if (customerLeDetails.getCreditPreapprovedFlag() == null)
						throw new TclCommonException(ExceptionConstants.CREDITCHECK_LE_PREAPPROVED_FLAG_ERROR,
								ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					if (CommonConstants.Y.equalsIgnoreCase(customerLeDetails.getBlacklistStatus())) {
						throw new TclCommonException(ExceptionConstants.BLACKLISTED_ACCOUNT_ERROR,
								ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					}

					if ((Objects.nonNull(customerLeDetails.getPreapprovedPaymentTerm()))
							&& (Objects.nonNull(customerLeDetails.getPreapprovedBillingMethod()))) {
						if (Objects.isNull(customerLeDetails.getPreapprovedMrc())
								|| (Objects.isNull(customerLeDetails.getPreapprovedNrc()))
								|| (Objects.isNull(customerLeDetails.getPreapprovedPaymentTerm()))
								|| (Objects.isNull(customerLeDetails.getPreapprovedBillingMethod()))) {
							LOGGER.info("Some data is null for cuLe sfdc id - {}, sending for manual check by default",
									customerLeDetails.getAccountId());
							throw new TclCommonException(ExceptionConstants.CREDITCHECK_DATA_INVALID,
									ResponseResource.R_CODE_ERROR);
						}
					} else if (Objects.isNull(customerLeDetails.getPreapprovedMrc())
							|| (Objects.isNull(customerLeDetails.getPreapprovedNrc()))) {
						LOGGER.info(
								"Preapproved mrc/nrc is null for cuLe sfdc id - {}, sending for manual check by default",
								customerLeDetails.getAccountId());
						throw new TclCommonException(ExceptionConstants.CREDITCHECK_DATA_INVALID,
								ResponseResource.R_CODE_ERROR);

					}

				}
			}

			// });

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
				boolean isMacd = false;
				if (MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
					isMacd = true;
				}
				processBillingValidation(quoteLeId, validationMessages);
				processContractingInfoValidation(quoteLeId, validationMessages, isMacd);
				validateSites(quoteToLe.get(), validationMessages);
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
	
	public CommonValidationResponse processValidate(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		commonValidationResponse.setStatus(true);
		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = getQuoteDetails(quoteId, null, false,null,null);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			Map<String, Object> variable = illQuotePdfService.getCofAttributes(true, true, quoteDetail, quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
					|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for ILL is {}", Utils.convertObjectToJson(variable));
				commonValidationResponse = illCofValidatorService.processCofValidation(variable,
						"IAS", quoteToLe.get().getQuoteType());
				checkFeasibilityValidityPeriod(quoteToLe, commonValidationResponse);
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
				.findByQuoteToLeIdAndAttributeName(quoteToLeId, BILLING_CONTACT_ID.toString());
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

	private void validateSites(QuoteToLe quoteToLe, List<String> validationMessages) {
		List<QuoteIllSite> taskTriggeredSites = illSiteRepository.getTaskTriggeredSites(quoteToLe.getQuote().getId());
		if (taskTriggeredSites != null && !taskTriggeredSites.isEmpty()) {
			validationMessages.add("Quote is still pending for commercial approval");
		}
	}

	public List<String> getServiceRequest(Integer quoteId, String serviceType) throws TclCommonException {
		List<String> response = new ArrayList<>();

		if (quoteId == null)
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

		Optional<Quote> quoteOpt = quoteRepository.findById(quoteId);
		if (!quoteOpt.isPresent())
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

		List<ThirdPartyServiceJob> thirdPartyJobsList = thirdPartyServiceJobRepository
				.findByRefIdAndServiceTypeAndThirdPartySource(quoteOpt.get().getQuoteCode(), serviceType,
						ThirdPartySource.SFDC.toString());
		if (!thirdPartyJobsList.isEmpty()) {
			thirdPartyJobsList.stream().forEach(job -> {
				try {
					LOGGER.info("MDC Filter token value in before Queue call getServiceRequest {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));

					ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
					serviceRequestBean.setRequest(job.getRequestPayload());
					serviceRequestBean.setServiceType(serviceType);
					String res = (String) mqUtils.sendAndReceive(getServiceRequestQueue,
							Utils.convertObjectToJson(serviceRequestBean));
					LOGGER.info("Response received in oms {}", res);
					LOGGER.info("json {}", Utils.convertJsonStingToJson(res));
					response.add(res.replaceAll("\\\\", ""));

				} catch (Exception e) {
					LOGGER.error("error in queue call getServiceRequest {}", e);
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}

			});

		} else
			throw new TclCommonException(ExceptionConstants.JOBS_EMPTY, ResponseResource.R_CODE_ERROR);

		return response;
	}

	private String setAccountOwnerName(String partnerId) throws TclCommonException {
		String accountOwnerName = "";
		try {
			String accountOwnerNameAndEmail = (String) mqUtils.sendAndReceive(partnerAccountNameMQ, partnerId);
			accountOwnerName = accountOwnerNameAndEmail.split(COMMA)[0].trim();
		} catch (TclCommonException e) {
			LOGGER.warn("Error Occoured while fetching account owner name for partner id :: {} and error is :: {}",
					partnerId, e.getStackTrace());
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
			LOGGER.info("Inside IllQuoteService.constructOrderIllSiteToService to save orderIllSiteService for orderSiteId {} ", orderSite.getId());
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
								IllSitePropertiesConstants.SITE_PROPERTIES.toString(), QuoteConstants.ILLSITES.toString());
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
							iasQueueResponse = (String) mqUtils.sendAndReceive(siRelatedDetailsQueue, quoteSiteService.getErfServiceInventoryTpsServiceId());
						
						if (StringUtils.isNotBlank(iasQueueResponse)) {
							LOGGER.info("queue response from si in constructOrderIllSiteToService {}", iasQueueResponse);
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
									Optional<QuoteIllSiteToService> qSiteToService = quoteIllSiteServices.stream().filter(siteToService -> siteToService.getErfServiceInventoryTpsServiceId().equalsIgnoreCase(siServiceInfoBean.get().getTpsServiceId())).findAny();
									String quoteCategory = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteCategory();
									String macdChangeBandwidhFlag = illSite.getMacdChangeBandwidthFlag();
									LOGGER.info("quote Category {}, macdChangeBandwidthFlag {}", quoteCategory, macdChangeBandwidhFlag);
										if (!qSiteToService.isPresent() &&  ((MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteCategory) 
												&& MACDConstants.BOTH_STRING.equalsIgnoreCase(macdChangeBandwidhFlag)) || MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteCategory))
												&& (Objects.nonNull(nsQuote[0]) && CommonConstants.N.equalsIgnoreCase(nsQuote[0])) ) {
											LOGGER.info("Service Id {}, primary or secondary {}",
													siServiceInfoBean.get().getTpsServiceId(),
													siServiceInfoBean.get().getPrimaryOrSecondary());
											OrderIllSiteToService relatedServiceDetail = new OrderIllSiteToService();
											relatedServiceDetail.setErfServiceInventoryParentOrderId(
													siServiceInfoBean.get().getSiOrderId());
											relatedServiceDetail.setErfServiceInventoryPriSecLinkServiceId(orderIllSiteToService.getErfServiceInventoryTpsServiceId());
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
							
					}
						
					} catch (Exception e) {
						LOGGER.error("error in queue call siRelatedDetailsQueue in constructOrderIllSiteToService {}", e);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
						
					
					

					orderIllSiteToServices.add(orderIllSiteToService);
				});
				orderIllSiteToServiceRepository.saveAll(orderIllSiteToServices);
				LOGGER.info("Inside IllQuoteService.constructOrderIllSiteToService Saved orderillSiteToServicesss");
			}
		} catch(Exception e) {
			LOGGER.error("Exception occured while saving orderIllSiteToServices {} ", e);
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

	/**
	 * Method to download order enrichment details by quote code
	 *
	 * @param response
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public  String downloadEnrichmentDetails(HttpServletResponse response, String quoteCode) throws DocumentException, IOException, TclCommonException {
		String htmlContent="";
		if (!StringUtils.isEmpty(quoteCode)) {
            EnrichmentDetailsBean enrichmentSiteProperties = new EnrichmentDetailsBean();
            String productName = getProductName(quoteCode);
            if (productName.isEmpty()) {
                LOGGER.warn("Product Name Not Applicable for given quote code :: {}", quoteCode);
            } else {
                List<String> enrichmentAttributeNames;
                switch (productName) {
                    case IAS: {
                        enrichmentAttributeNames = Arrays.asList("Routing Protocol", "BFD Required", "GSTNO", "Extended LAN Required?",
								"isAuthenticationRequired for protocol", "Routes Exchanged", "AS Number", "BGP AS Number","Customer prefixes",
								"BGP Peering on");
                        enrichmentSiteProperties = getEnrichmentSiteProperties(quoteCode, enrichmentAttributeNames, IAS);
						htmlContent=downloadEnrichmentDetailsPDF(enrichmentSiteProperties, response);
                        break;
                    }
                    case GVPN: {
                        enrichmentAttributeNames = Arrays.asList("Routing Protocol", "BFD Required", "BGP Peering on", "AS Number", "WAN IP Provided By",
								"isAuthenticationRequired for protocol", "VPN Topology", "Site Type", "Routes Exchanged", "GSTNO", "isMultiCastExists",
								"multiCastType", "rpAddress");
                        enrichmentSiteProperties = getEnrichmentSiteProperties(quoteCode, enrichmentAttributeNames, GVPN);
						htmlContent=downloadEnrichmentDetailsPDF(enrichmentSiteProperties, response);
                        break;
                    }
                    /* Attributes to be verified and query also to be checked*/
                    case NPL: {
                        enrichmentSiteProperties = nplQuoteService.getEnrichmentLinkProperties(quoteCode, NPL);
						htmlContent=downloadEnrichmentDetailsPDF(enrichmentSiteProperties, response);
                        break;
                    }
                    case CommonConstants.NDE: {
                    	LOGGER.info("inside Nde case"+CommonConstants.NDE);
                        enrichmentSiteProperties = nplQuoteService.getEnrichmentLinkProperties(quoteCode, CommonConstants.NDE);
						htmlContent=downloadEnrichmentDetailsPDF(enrichmentSiteProperties, response);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        } else {
			LOGGER.info("Quote code is empty");
		}
		return htmlContent;
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
	 * Method to get enrichmentdetails by quote code
	 *
	 * @param quoteCode
	 * @return {@link EnrichmentDetailsBean}
	 */
	private EnrichmentDetailsBean getEnrichmentSiteProperties(String quoteCode, List<String> enrichmentAttributeNames, String productName) {
		List<QuoteIllSite> quoteIllSites = illSiteRepository.findSites(quoteCode);
		EnrichmentDetailsBean enrichmentDetailsBean = new EnrichmentDetailsBean();
		if (!CollectionUtils.isEmpty(quoteIllSites)) {
			enrichmentDetailsBean.setQuoteCode(quoteCode);
			Map<String, List<QuoteProductComponentsAttributeValueBean>> siteEnrichmentDetails = new HashMap<>();
			quoteIllSites.stream().forEach(quoteIllSite -> {
				List<QuoteProductComponentsAttributeValue> enrichmentAttributes = quoteProductComponentsAttributeValueRepository.findOrderEnrichmentAttributesByAttributesAndSiteId(enrichmentAttributeNames, quoteIllSite.getId(), productName);
				List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean = enrichmentAttributes.stream().map(quoteProductComponentsAttributeValue -> new QuoteProductComponentsAttributeValueBean(quoteProductComponentsAttributeValue)).collect(Collectors.toList());
				getSiteAddressDetail(quoteIllSite.getErfLocSitebLocationId(), enrichmentAttributesBean);
				getLocalItContactDetails(productName, quoteIllSite, enrichmentAttributesBean);
				getDemarcationDetails(quoteIllSite, enrichmentAttributesBean);

				if (!CollectionUtils.isEmpty(enrichmentAttributesBean)) {
					siteEnrichmentDetails.put(quoteIllSite.getSiteCode(), enrichmentAttributesBean);
				}
			});
			enrichmentDetailsBean.setSiteEnrichmentDetails(siteEnrichmentDetails);
		}
		LOGGER.info("EnrichmentDetailsBean :: {}", enrichmentDetailsBean);
		return enrichmentDetailsBean;
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
	 * Download enrichment details in PDF
	 *
	 * @param enrichmentDetailsBean
	 * @param response
	 * @return {@link HttpServletResponse}
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	private String downloadEnrichmentDetailsPDF(EnrichmentDetailsBean enrichmentDetailsBean, HttpServletResponse response)
			throws DocumentException, IOException, TclCommonException {
		String html="";
		if (!CollectionUtils.isEmpty(enrichmentDetailsBean.getSiteEnrichmentDetails())) {
			byte[] outArray = null;
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			Map<String, Object> variable = objectMapper.convertValue(ImmutableMap.of("enrichmentDetailsBean", enrichmentDetailsBean), Map.class);
			Context context = new Context();
			context.setVariables(variable);
			html = templateEngine.process("order_enrichment_details_template", context);
			PDFGenerator.createPdf(html, outByteStream);
			outArray = outByteStream.toByteArray();
			response.reset();
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			response.setContentLength(outArray.length);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "ENRICHMENT_DETAILS_PDF.pdf" + "\"");
			try {
				FileCopyUtils.copy(outArray, response.getOutputStream());
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			outByteStream.flush();
			outByteStream.close();
		}
		return html;
	}

	/**
	 * to get the product name by parsing the quoteCode
	 * @param quoteCode
	 * @return
	 */
	private String getProductName(String quoteCode) {
		if (quoteCode.startsWith(IAS)) {
			return IAS;
		} else if (quoteCode.startsWith(GVPN)) {
			return GVPN;
		} else if (quoteCode.startsWith(NPL)) {
			return NPL;
		}else if (quoteCode.startsWith(CommonConstants.NDE)) {
			LOGGER.info("INSIDE NDE COPF ORDER ENRICHEMENT"+CommonConstants.NDE);
			return CommonConstants.NDE;
		}
		
		return "";
	}

	
	/**
	 * updateRejectionSiteSatus - site level
	 * 
	 * @throws TclCommonException
	 */
	public void updateSiteRejectionSatus(CommercialRejectionBean commercialRejectionBean) throws TclCommonException {
		try {
			LOGGER.info("updateSiteRejectionSatus siteid:siterejectiostatus"+commercialRejectionBean.getSiteId()+":"+commercialRejectionBean.getSiteRejectionStatus());
			if (commercialRejectionBean.getSiteId()!=null && commercialRejectionBean.getSiteRejectionStatus()!=null) {
				Optional<QuoteIllSite> site = illSiteRepository.findById(Integer.parseInt(commercialRejectionBean.getSiteId()));
				if (site.isPresent()) {
					if(commercialRejectionBean.getSiteRejectionStatus()) {
					site.get().setCommercialRejectionStatus("1");
					site.get().setCommercialApproveStatus("0");
					}
					else {
						site.get().setCommercialRejectionStatus("0");
					}
					illSiteRepository.save(site.get());
					
					//audit commercial 
					CommercialQuoteAudit audit=new CommercialQuoteAudit();
					User user = getUserId(Utils.getSource());
					audit.setCommercialAction("Site_Reject");
					audit.setQuoteId(Integer.parseInt(commercialRejectionBean.getQuoteId()));
					audit.setSiteId(commercialRejectionBean.getSiteId().toString());
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
							List<Integer> sites = commercialRejectionBean.getSites();
							sites.forEach(siteId -> {
								QuoteIllSite illSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
								illSite.setCommercialRejectionStatus("1");
								illSite.setCommercialApproveStatus("0");
								illSiteRepository.save(illSite);

							});

							CommercialQuoteAudit audit = new CommercialQuoteAudit();
							User user = getUserId(Utils.getSource());
							audit.setCommercialAction("Quote_Reject");
							audit.setQuoteId(Integer.parseInt(commercialRejectionBean.getQuoteId()));
							audit.setSiteId(commercialRejectionBean.getSites().toString());
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
	 * @param orderToLeId
	 * @return
	 * @throws TclCommonException
	 */
	public List<MfL2OReportBean> getOmsAttachments(List<String>  fIds) throws TclCommonException {
		ArrayList<MfL2OReportBean> reportList = new ArrayList<MfL2OReportBean>();
		try {
			fIds.stream().forEach(fTId -> {
				List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByAttachmentTypeAndReferenceName("MF",fTId);
				MfL2OReportBean mfBean = new MfL2OReportBean();
				mfBean.setFeasibilityId(fTId);
				List<OmsAttachBean> omsAttachmentDetails = new ArrayList<OmsAttachBean>();
				List<MfAttachmentBean> mfAttachmentBeanDetails = new ArrayList<MfAttachmentBean>();

				mfBean.setOmsAttachmentDetails(omsAttachmentDetails);
				mfBean.setMfAttachments(mfAttachmentBeanDetails);
				mfBean.setFeasibilityId(fTId);
				
				  omsAttachments.stream().filter( x -> x.getErfCusAttachmentId()!=null)
				   .forEach(omsEntity -> {
					OmsAttachBean omsAttachmentBean = new OmsAttachBean();
						// Giving all OMs_attachment table info
						omsAttachmentBean.setAttachmentId(omsEntity.getErfCusAttachmentId());
						omsAttachmentBean.setAttachmentType(omsEntity.getAttachmentType());
						
						// Adding Null check as quoteToLe will be null for l2o uploaded via 3D feasibility
						// Defect Fix - OPPORTAL-1515
						if(omsEntity.getQuoteToLe()!=null) {
						omsAttachmentBean.setQouteLeId(omsEntity.getQuoteToLe().getId());
				         }
						omsAttachmentBean.setAttachmentId(omsEntity.getErfCusAttachmentId());
						omsAttachmentBean.setReferenceId(omsEntity.getReferenceId());
						omsAttachmentBean.setReferenceName(omsEntity.getReferenceName());
						omsAttachmentDetails.add(omsAttachmentBean);
						
						try {
							   String attachmentResponse = ((String) mqUtils.sendAndReceive(attachReqIdQueue, String.valueOf(omsEntity.getErfCusAttachmentId())));
					            AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(attachmentResponse, AttachmentBean.class);
							
							if (attachmentResponse != null) {
								MfAttachmentBean mfAttachmentDetail = new MfAttachmentBean();
								@SuppressWarnings("unchecked")
								Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);
								if (attachmentMapper != null) {
									mfAttachmentDetail.setName(attachmentBean.getFileName());
									mfAttachmentDetail.setUrlPath(attachmentBean.getPath());
									mfAttachmentBeanDetails.add(mfAttachmentDetail);
								}
							}
						} catch (TclCommonException e) {
							LOGGER.error(" Error Accessign attachmentQueue", e);
						}
						
					
				});
				   reportList.add(mfBean);
			});
				
			

		} catch (Exception e) {
			LOGGER.warn("Cannot get oms Attachments");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return reportList;
	}
	
	public void patchInterface(Integer quoteIllSiteId) {
		try {
			MstProductComponent productComponent = mstProductComponentRepository
					.findByName(PricingConstants.INTERNET_PORT);
			List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceNameAndMstProductComponent(quoteIllSiteId, "ILL_SITES",
							productComponent);
			for (QuoteProductComponent quoteProductComponent : primaryComponents) {
				LOGGER.info("Patching for component Name {} with component Id {} and type {}",
						productComponent.getName(), quoteProductComponent.getId(), quoteProductComponent.getType());
				ProductAttributeMaster attributeMaster = productAttributeMasterRepository
						.findByName(FPConstants.INTERFACE.toString());
				List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, attributeMaster);
				for (QuoteProductComponentsAttributeValue quoteProdCompAttributeValue : quoteProdCompAttributeValues) {
					LOGGER.info("Patching for Attribute Name {} with attribute Id {} and value {}",
							attributeMaster.getName(), quoteProdCompAttributeValue.getId(),
							quoteProdCompAttributeValue.getAttributeValues());
					if (quoteProdCompAttributeValue.getAttributeValues().equals("FE")) {
						LOGGER.info(
								"The value is FE for QuoteProductComponentsAttributeValue id {} , so updating as Fast Ethernet",
								quoteProdCompAttributeValue.getId());
						quoteProdCompAttributeValue.setAttributeValues("Fast Ethernet");
						quoteProductComponentsAttributeValueRepository.save(quoteProdCompAttributeValue);

					} else {
						LOGGER.info("The value is {} for QuoteProductComponentsAttributeValue id {} , so ignoring",
								quoteProdCompAttributeValue.getAttributeValues(), quoteProdCompAttributeValue.getId());
					}

				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in patching interface ", e);
		}

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
	 * @author  getUserAddress
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	public AddressDetail getUserAddress(Integer locationId) throws TclCommonException {
		AddressDetail userAddress = null;
		try {
			String response = (String) mqUtils.sendAndReceive(locationQueue, locationId.toString());
			LOGGER.info("Output Payload for location details", response);
			if (StringUtils.isNotBlank(response)) {
				userAddress = (AddressDetail) Utils.convertJsonToObject(response, AddressDetail.class);
			}
		} catch (Exception e) {
			throw new TclCommonException(e);
		}
		return userAddress;
	}
	
	

	public OpportunityBean retrievePriSecSIDsForMFOppurtunity(OpportunityBean opBean, Integer quoteId, Integer siteId) throws TclCommonException{
		
		Optional<QuoteIllSite> quoteIllSite=illSiteRepository.findById(siteId);
		QuoteToLe quoteToLe = null;
		if(quoteIllSite.isPresent()) {
			 quoteToLe = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		}
		
		if (Objects.nonNull(quoteToLe) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
				&& Objects.nonNull(opBean.getServiceId()) ) {
			String secondaryServiceId = null;
			Boolean isSecondary = false;
			SIServiceDetailDataBean sIServiceDetailDataBean = macdUtils.getServiceDetail(opBean.getServiceId(), quoteToLe.getQuoteCategory());
			isSecondary = Objects.nonNull(sIServiceDetailDataBean.getPriSecServLink());
			String linkType = sIServiceDetailDataBean.getLinkType();
			
			if (linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SINGLE")) {
				if (isSecondary) {
					secondaryServiceId = sIServiceDetailDataBean.getPriSecServLink();
					LOGGER.info("Primary service Id is ----> {} and Secondary service ID is -----> {} ",opBean.getServiceId(), secondaryServiceId);
					opBean.setSecondaryServiceId(secondaryServiceId);
					opBean.setPrimaryServiceId(sIServiceDetailDataBean.getTpsServiceId());
				}  else  // PT-3242 : set primaryServiceId for Single/Primary linkType
					opBean.setPrimaryServiceId(sIServiceDetailDataBean.getTpsServiceId());
			}

				else if (isSecondary && linkType.equalsIgnoreCase("SECONDARY")) {
					secondaryServiceId=sIServiceDetailDataBean.getTpsServiceId();
					LOGGER.info("Secondary service ID is -----> {}  and primary service Id is ----> {} ", secondaryServiceId,opBean.getServiceId());
					opBean.setSecondaryServiceId(secondaryServiceId);
					opBean.setPrimaryServiceId(sIServiceDetailDataBean.getPriSecServLink());
				
				}
			}
		return opBean;
	}

	/**
	 * getoptyid method by quotecode
	 * 
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException 
	 */
	public String getOpporunityId(String quotecode) throws TclCommonException {
			try {
				LOGGER.info("Entering method for fetching opportunity id quotecode: " + quotecode);
					if(quotecode!=null) {
						String optyId=null;
						Quote quote = quoteRepository.findByQuoteCode(quotecode);
						List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quote.getId());
						if (quoteToLes.size() != 0 && quoteToLes.get(0) != null
								&& quoteToLes.get(0).getTpsSfdcOptyId() != null && !quoteToLes.get(0).getTpsSfdcOptyId().isEmpty()) {
							LOGGER.info(" findByQuote_Id : get opty id {}: " + quoteToLes.get(0).getTpsSfdcOptyId());
							optyId=quoteToLes.get(0).getTpsSfdcOptyId();
							LOGGER.info(" optyId response " +optyId);
							return optyId;
						} else {
							LOGGER.info("Entering method for fetching opportunity id quotetoLe itself null");
							return null;
						}
					}
					else {
						LOGGER.info("Entering method for fetching opportunity id quotecode null: " + quotecode);
						return null;
					}
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);

			}
		}

	@Transactional
	public String persistDemoOrderInfo(Integer quoteToLe, DemoOrderInfo info) throws TclCommonException{

		validateReqInfo(quoteToLe, info);

		QuoteToLe quoteLe = quoteToLeRepository.findById(quoteToLe).get();

		if(Objects.nonNull(info.getIsDemo()) && info.getIsDemo()){
			LOGGER.info("Demo order flag for quote ----> {} is ----> {} " , quoteLe.getQuote().getQuoteCode(),info.getIsDemo());
			quoteLe.setIsDemo(BACTIVE);
			quoteLe.setDemoType(info.getDemoType());
			quoteToLeRepository.save(quoteLe);
			return "Saved";
		}

		return "";
	}

	private void validateReqInfo(Integer quoteToLe, DemoOrderInfo info) throws TclCommonException {
		if(Objects.isNull(quoteToLe) || Objects.isNull(info) ){
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
		}
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
								IllSitePropertiesConstants.SITE_PROPERTIES.toString(), QuoteConstants.ILLSITES.toString());
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
				primaryLM.setServiceInventoryLMProvider(illPricingFeasibilityService.getLmProviderForSIds(primaryLM.getServiceId()));
				primaryLM.setSiteId(siteIDFromDB);
				if (selectedSiteFeasibilityPrimary != null && selectedSiteFeasibilityPrimary.isPresent()) {
					if (selectedSiteFeasibilityPrimary.get().getProvider() == null
							&& selectedSiteFeasibilityPrimary.get().getFeasibilityCheck().equalsIgnoreCase("system")
							&& selectedSiteFeasibilityPrimary.get().getWfeType().equalsIgnoreCase("MACD")
							&& primaryLM.getServiceInventoryLMProvider() != null) {
						LOGGER.info("Inside primary provider null system selected macd wfe type scenario");
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
						.setServiceInventoryLMProvider(illPricingFeasibilityService.getLmProviderForSIds(secondaryLm.getServiceId()));
				secondaryLm.setSiteId(siteIDFromDB);
				if (selectedSiteFeasibilitySecondary != null && selectedSiteFeasibilitySecondary.isPresent()) {
					// for port upgrade system wont give provider .existing lm as new LM
					if (selectedSiteFeasibilitySecondary.get().getProvider() == null
							&& selectedSiteFeasibilitySecondary.get().getFeasibilityCheck().equalsIgnoreCase("system")
							&& selectedSiteFeasibilitySecondary.get().getWfeType().equalsIgnoreCase("MACD")
							&& secondaryLm.getServiceInventoryLMProvider() != null) {
						LOGGER.info("Inside secondary provider null system selected macd wfe type scenario");
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
				primaryNonMulticircuit.setSiteId(siteIDFromDB);
				primaryNonMulticircuit.setServiceInventoryLMProvider(illPricingFeasibilityService.getLmProviderForSIds(oppBean.getPrimaryServiceId()));
				
				if (selectedSiteFeasibilityPrimary != null && selectedSiteFeasibilityPrimary.isPresent()) {
					if (selectedSiteFeasibilityPrimary.get().getProvider() == null
							&& selectedSiteFeasibilityPrimary.get().getFeasibilityCheck().equalsIgnoreCase("system")
							&& selectedSiteFeasibilityPrimary.get().getWfeType().equalsIgnoreCase("MACD")
							&& primaryNonMulticircuit.getServiceInventoryLMProvider() != null) {
						LOGGER.info("Inside non multicircuit primary provider null system selected macd wfe type scenario");
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
				secNonMulticircuit.setSiteId(siteIDFromDB);
				secNonMulticircuit.setServiceInventoryLMProvider(
						illPricingFeasibilityService.getLmProviderForSIds(oppBean.getSecondaryServiceId()));
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

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteToLe.get().getId());
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

	@Transactional
	public List<LeOwnerDetailsSfdc> getOwnerDetailsForSfdc(Integer customerId, Integer quoteId,Integer leId) throws TclCommonException {

		try {
			return getDetails(customerId, quoteId,leId);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private List<LeOwnerDetailsSfdc> getDetails(Integer customerId, Integer quoteId,Integer leId) throws TclCommonException {
		Objects.requireNonNull(customerId, "Customer Id cannot be null;required field");
		Objects.requireNonNull(quoteId, "Quote Id cannot be null;required field");
		List<LeOwnerDetailsSfdc> ownerDetailsSfdcList = new ArrayList<>();
		String cusLeId = "";
		if (leId != null) {
			cusLeId += leId;
		} else {
			List<QuoteToLe> quoteLe = quoteToLeRepository.findByQuote_Id(quoteId);
			Integer erfCusCustomerLegalEntityId = quoteLe.get(0).getErfCusCustomerLegalEntityId();

			if (erfCusCustomerLegalEntityId != null) {
				cusLeId += erfCusCustomerLegalEntityId.toString();
			} else {
				List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute_Name(quoteLe.get(0),
								LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY);
				for (QuoteLeAttributeValue quoteLeAttributeValue : quoteLeAttributeValues) {
					cusLeId += quoteLeAttributeValue.getAttributeValue();
					break;
				}
			}
		}
		if (StringUtils.isNotBlank(cusLeId)) {
			LOGGER.info("Customer id and cus Le Id are  -> {} ----------- {} ", customerId, cusLeId);
			// LeOwnerDetailsSfdc leOwnerDetailsSfdc= new LeOwnerDetailsSfdc();
			// cusLeId = settingJourneyOwnerDetails(cusLeId, leOwnerDetailsSfdc);

			String queueResponse = (String) mqUtils.sendAndReceive(ownerDetailsQueue,
					customerId.toString() + "," + cusLeId);
			LOGGER.info("Response from owner details queue for quote ---> {}  is ----> {} ", quoteId, queueResponse);

			LeOwnerDetailsSfdc[] ownerDetails = (LeOwnerDetailsSfdc[]) Utils.convertJsonToObject(queueResponse,
					LeOwnerDetailsSfdc[].class);

			ownerDetailsSfdcList.addAll(Arrays.asList(ownerDetails));
			if (!ownerDetailsSfdcList.isEmpty() && Objects.nonNull(ownerDetailsSfdcList)) {
				LOGGER.info("List converted into object after Owner Details queue call is----> {} with size ----> {} ",
						ownerDetailsSfdcList, ownerDetailsSfdcList.size());
			}
			/*
			 * if(cusLeId.equalsIgnoreCase("No")){
			 * ownerDetailsSfdcList.add(leOwnerDetailsSfdc); }
			 */
			if (!ownerDetailsSfdcList.isEmpty()) {
				LOGGER.info("Final List sent for quote id ---> {} is ----> {} ", quoteId, ownerDetailsSfdcList);
			}
		}
		return ownerDetailsSfdcList;
	}
	
	
	public void patchCopyComponents(Integer quoteReferenceId, String quoteReferenceName, Integer orderReferenceId,
			String componentName) throws TclCommonException {

		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndReferenceName(orderReferenceId, quoteReferenceName);
		if (orderProductComponents.isEmpty() || StringUtils.isNotBlank(componentName)) {
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(quoteReferenceId, quoteReferenceName);
			if (productComponents != null) {
				for (QuoteProductComponent quoteProductComponent : productComponents) {
					OrderProductComponent orderProductComponent = null;
					if (StringUtils.isNotBlank(componentName)) {
						String compName = quoteProductComponent.getMstProductComponent().getName();
						if (!compName.equals(componentName)) {
							LOGGER.info("Skipping as component is not matching {}", compName);
							continue;
						} else {
							List<OrderProductComponent> orderProductComponentList = orderProductComponentRepository
									.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndTypeAndReferenceName(
											orderReferenceId, quoteProductComponent.getMstProductComponent(),
											quoteProductComponent.getMstProductFamily(),
											quoteProductComponent.getType(), quoteReferenceName);
							if(!orderProductComponentList.isEmpty()) {
								continue;
							}
						}
					}

					if (orderProductComponent == null) {
						LOGGER.info("component not found - creating");
						orderProductComponent = new OrderProductComponent();
					}
					orderProductComponent.setReferenceId(orderReferenceId);
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
				}

			}
		}
	}

	/*private String settingJourneyOwnerDetails(String fetchOwner, LeOwnerDetailsSfdc leOwnerDetailsSfdc) {
		fetchOwner="No";
		if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			if (appEnv.equals(SFDCConstants.PROD)) {
				LOGGER.info("OPPORTUNITY USER EMAIL ID {}", Utils.getSource());
				String[] ownerName = Utils.getSource().split("@");
				leOwnerDetailsSfdc.setOwnerName(ownerName[0]);
				leOwnerDetailsSfdc.setEmail(Utils.getSource());
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
				leOwnerDetailsSfdc.setMobile("");
			} else {
				leOwnerDetailsSfdc.setOwnerName(null);
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
				leOwnerDetailsSfdc.setMobile("");
			}
		} else {
			if (appEnv.equals(SFDCConstants.PROD)) {
				fetchOwner="Yes";
			} else {
				leOwnerDetailsSfdc.setOwnerName(null);
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
				leOwnerDetailsSfdc.setMobile("");
			}
		}
		return fetchOwner;
	}*/

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


	/*private String settingJourneyOwnerDetails(String fetchOwner, LeOwnerDetailsSfdc leOwnerDetailsSfdc) {
		fetchOwner="No";
		if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			if (appEnv.equals(SFDCConstants.PROD)) {
				LOGGER.info("OPPORTUNITY USER EMAIL ID {}", Utils.getSource());
				String[] ownerName = Utils.getSource().split("@");
				leOwnerDetailsSfdc.setOwnerName(ownerName[0]);
				leOwnerDetailsSfdc.setEmail(Utils.getSource());
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
				leOwnerDetailsSfdc.setMobile("");
			} else {
				leOwnerDetailsSfdc.setOwnerName(null);
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
				leOwnerDetailsSfdc.setMobile("");
			}
		} else {
			if (appEnv.equals(SFDCConstants.PROD)) {
				fetchOwner="Yes";
			} else {
				leOwnerDetailsSfdc.setOwnerName(null);
				leOwnerDetailsSfdc.setTeamRole("Account Owner");
				leOwnerDetailsSfdc.setMobile("");
			}
		}
		return fetchOwner;
	}*/
	
	
	@SuppressWarnings("deprecation")
	public void returnIllGvpnMultisiteReportExcel(HttpServletResponse response,String quoteCode) throws IOException, TclCommonException {
		
		try {
			LOGGER.info("Inside returnIllGvpnMultisiteReportExcel method:start{}:");
		List<IllGvpnMultiSiteInputs> illGvpnMultiSiteInputs = new ArrayList<>();
		List<IllGvpnMultiSiteInputs> illGvpnSiteInfoList = new ArrayList<>();
		illGvpnMultiSiteInputs = getSiteRelatedInformation(quoteCode,illGvpnSiteInfoList);
		/*
		 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
		 * workbook.createSheet(); sheet.setColumnWidth(50000, 50000);
		 */
		
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
			

			/* Header color for non editable-set to Grey */
			XSSFCellStyle headerCellNonEditable = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
			headerCellNonEditable.setLocked(true);
		
			/* Header color for editable as sky blue */
			XSSFCellStyle headerEditableColor = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
			headerEditableColor.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
			headerEditableColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerEditableColor.setLocked(true);
			  
			XSSFFont font = (XSSFFont) workbook.createFont();
			font.setFontHeightInPoints((short) 10); font.setFontName("Arial");
			font.setBold(true);
			font.setItalic(false);
			
			headerCellNonEditable.setFont(font);
			headerEditableColor.setFont(font);

			headerRow = sheet.createRow(0);
			Cell headerCell = headerRow.createCell(2);
			headerCell.setCellValue("Address Details");
			CellStyle cellStyle = headerRow.getSheet().getWorkbook().createCellStyle();  
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headerCell.setCellStyle(cellStyle);
			cellStyle.setFont(font);
			sheet.addMergedRegion(CellRangeAddress.valueOf("D1:E1"));
			headerCell = headerRow.createCell(autosizecolumn);
			sheet.autoSizeColumn(1, true);

			Row subHeaderRow = sheet.createRow(1);
			cell = subHeaderRow.createCell(0);
			cell.setCellValue("Site Id");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(1);
			cell.setCellValue("Site Code");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(2);
			cell.setCellValue("Site Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(3);
			cell.setCellValue("Address");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(4);
			cell.setCellValue("Country");
			cell.setCellStyle(headerCellNonEditable);
			
			Cell headerCellTwo = headerRow.createCell(5);
			headerCellTwo.setCellValue("Existing Component (Only for MAC/PR)");
			CellStyle cellStyleTwo = headerRow.getSheet().getWorkbook().createCellStyle();
			cellStyleTwo.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyleTwo.setFont(font);
			headerCellTwo.setCellStyle(cellStyleTwo);
			sheet.addMergedRegion(CellRangeAddress.valueOf("F1:T1"));
			headerCellTwo = headerRow.createCell(autosizecolumn);
			sheet.autoSizeColumn(5, true);
			
			cell = subHeaderRow.createCell(5);
			cell.setCellValue("Service Id");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(6);
			cell.setCellValue("Port Bandwidth (Mbps)");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(7);
			cell.setCellValue("Port Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(8);
			cell.setCellValue("Local Loop Bandwidth");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(9);
			cell.setCellValue("LM Provider");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(10);
			cell.setCellValue("CPE Management Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(11);
			cell.setCellValue("CPE Support Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(12);
			cell.setCellValue("CPE Model");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(13);
			cell.setCellValue("IPv4 Address Pool Size");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(14);
			cell.setCellValue("Currency");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(15);
			cell.setCellValue("Existing ARC");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(16);
			cell.setCellValue("Existing NRC");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(17);
			cell.setCellValue("Contract Term");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(18);
			cell.setCellValue("Commission Date");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(19);
			cell.setCellValue("Expiry Date");
			cell.setCellStyle(headerCellNonEditable);
			
			Cell headerCellThree = headerRow.createCell(20);
			headerCellThree.setCellValue("ATTRIBUTES - BASIC");
			CellStyle cellStyleThree = headerRow.getSheet().getWorkbook().createCellStyle();
			cellStyleThree.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyleThree.setFont(font);
			headerCellThree.setCellStyle(cellStyleThree);
			sheet.addMergedRegion(CellRangeAddress.valueOf("U1:AQ1"));
			headerCellThree = headerRow.createCell(autosizecolumn);
			sheet.autoSizeColumn(20, true);
			
			cell = subHeaderRow.createCell(20);
			cell.setCellValue("Product");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(21);
			cell.setCellValue("Order Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(22);
			cell.setCellValue("Interface");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(23);
			cell.setCellValue("Port Bandwidth (Mbps)");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(24);
			cell.setCellValue("Port Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(25);
			cell.setCellValue("Local Loop Bandwidth (Mbps)");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(26);
			cell.setCellValue("CPE Management Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(27);
			cell.setCellValue("Service Variant");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(28);
			cell.setCellValue("GVPN Port Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(29);
			cell.setCellValue("COS1");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(30);
			cell.setCellValue("COS2");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(31);
			cell.setCellValue("COS3");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(32);
			cell.setCellValue("COS4");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(33);
			cell.setCellValue("COS5");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(34);
			cell.setCellValue("COS6");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(35);
			cell.setCellValue("VPN Topology");
			cell.setCellStyle(headerCellNonEditable);
//			cell = subHeaderRow.createCell(35);
//			cell.setCellValue("Chargeable Distance for NDE/NPL (Km)");
//			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(36);
			cell.setCellValue("Site Type (Attributes)");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(37);
			cell.setCellValue("Resiliency");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(38);
			cell.setCellValue("Access Required");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(39);
			cell.setCellValue("CPE Support Type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(40);
			cell.setCellValue("Backup Configuration");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(41);
			cell.setCellValue("IP Address Provided By");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(42);
			cell.setCellValue("Dual Circuit");
			cell.setCellStyle(headerCellNonEditable);
			
			Cell headerCellFour = headerRow.createCell(43);
			headerCellFour.setCellValue("ATTRIBUTES - ADVANCED");
			CellStyle cellStyleFour = headerRow.getSheet().getWorkbook().createCellStyle();
			cellStyleFour.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyleFour.setFont(font);
			headerCellFour.setCellStyle(cellStyleFour);
			sheet.addMergedRegion(CellRangeAddress.valueOf("AR1:BA1"));
			headerCellFour = headerRow.createCell(autosizecolumn);
			sheet.autoSizeColumn(43, true);
			
			cell = subHeaderRow.createCell(43);
			cell.setCellValue("Service type");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(44);
			cell.setCellValue("Burstable Bandwidth");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(45);
			cell.setCellValue("IP Address Arrangement");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(46);
			cell.setCellValue("IPv4 Address Pool Size");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(47);
			cell.setCellValue("IPv6 Address Pool Size");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(48);
			cell.setCellValue("Shared Last Mile");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(49);
			cell.setCellValue("Shared Last Mile Service ID");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(50);
			cell.setCellValue("Shared CPE");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(51);
			cell.setCellValue("Shared CPE Service ID");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(52);
			cell.setCellValue("Additional IPs");
			cell.setCellStyle(headerCellNonEditable);
			
			Cell headerCellFive = headerRow.createCell(53);
			headerCellFive.setCellValue("Feasibility");
			CellStyle cellStyleFive = headerRow.getSheet().getWorkbook().createCellStyle();
			cellStyleFive.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyleFive.setFont(font);
			headerCellFive.setCellStyle(cellStyleFive);
			sheet.addMergedRegion(CellRangeAddress.valueOf("BB1:BZ1"));
			headerCellFive = headerRow.createCell(autosizecolumn);
			sheet.autoSizeColumn(53, true);
			cell = subHeaderRow.createCell(53);
			cell.setCellValue("FEASIBILITY STATUS");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(54);
			cell.setCellValue("FEASIBILITY TYPE");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(55);
			cell.setCellValue("FEASIBILITY Validity");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(56);
			cell.setCellValue("FEASIBILITY CODE");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(57);
			cell.setCellValue("FEASIBILITY MODE");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(58);
			cell.setCellValue("FEASIBILITY TYPE");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(59);
			cell.setCellValue("TYPE");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(60);
			cell.setCellValue("PROVIDER");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(61);
			cell.setCellValue("FEASIBILITY STATUS");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(62);
			cell.setCellValue("CD");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(63);
			cell.setCellValue("TCL POP");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(64);
			cell.setCellValue("Feasibility Check");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(65);
			cell.setCellValue("CONNECTED BUILDING");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(66);
			cell.setCellValue("CONNECTED CUSTOMER");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(67);
			cell.setCellValue("OSP DIST");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(68);
			cell.setCellValue("OSP CAPEX");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(69);
			cell.setCellValue("IN BUILDING CAPEX ");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(70);
			cell.setCellValue("MUX COST");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(71);
			cell.setCellValue("PROW VALUE (OTC)");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(72);
			cell.setCellValue("PROW VALUE (ARC)");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(73);
			cell.setCellValue("Mast Height (Meter)");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(74);
			cell.setCellValue("Mast NRC");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(75);
			cell.setCellValue("Offnet NRC");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(76);
			cell.setCellValue("Offnet ARC");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(77);
			cell.setCellValue("Feasibility Remarks");
			cell.setCellStyle(headerCellNonEditable);
			
			Cell headerCellSix = headerRow.createCell(78);
			headerCellSix.setCellValue("CPE Attributes");
			CellStyle cellStyleSix = headerRow.getSheet().getWorkbook().createCellStyle();
			cellStyleSix.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyleSix.setFont(font);
			headerCellSix.setCellStyle(cellStyleSix);
			sheet.addMergedRegion(CellRangeAddress.valueOf("CA1:CC1"));
			
			cell = subHeaderRow.createCell(78);
			cell.setCellValue("CPE Basic Chassis");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(79);
			cell.setCellValue("CPE Intl Chassis flag");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(80);
			cell.setCellValue("Cube Licenses");
			cell.setCellStyle(headerCellNonEditable);
			
			Cell headerCellSeven = headerRow.createCell(81);
			headerCellSeven.setCellValue("Pricing");
			CellStyle cellStyleSeven = headerRow.getSheet().getWorkbook().createCellStyle();
			cellStyleSeven.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyleSeven.setFont(font);
			headerCellSeven.setCellStyle(cellStyleSeven);
			sheet.addMergedRegion(CellRangeAddress.valueOf("CD1:DN1"));
			
			cell = subHeaderRow.createCell(81);
			cell.setCellValue("Contract Term");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(82);
			cell.setCellValue("Currency");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(83);
			cell.setCellValue("Port NRC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(84);
			cell.setCellValue("Port NRC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(85);
			cell.setCellValue("Port NRC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(86);
			cell.setCellValue("Port ARC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(87);
			cell.setCellValue("Port ARC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(88);
			cell.setCellValue("Port ARC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(89);
			cell.setCellValue("Last Mile NRC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(90);
			cell.setCellValue("Last Mile NRC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(91);
			cell.setCellValue("Last Mile NRC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(92);
			cell.setCellValue("Last Mile ARC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(93);
			cell.setCellValue("Last Mile ARC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(94);
			cell.setCellValue("Last Mile ARC Sale Price");
			cell.setCellStyle(headerEditableColor);
//			cell = subHeaderRow.createCell(95);
//			cell.setCellValue("Link NRC (Port+LM)");
//			cell.setCellStyle(headerCellNonEditable);
//			cell = subHeaderRow.createCell(96);
//			cell.setCellValue("Link ARC (Port+LM)");
//			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(95);
			cell.setCellValue("Mast NRC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(96);
			cell.setCellValue("Mast NRC Discount Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(97);
			cell.setCellValue("Mast NRC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(98);
			cell.setCellValue("Additional IP ARC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(99);
			cell.setCellValue("Additional IP ARC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(100);
			cell.setCellValue("Additional IP ARC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(101);
			cell.setCellValue("Burstable ARC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(102);
			cell.setCellValue("Burstable ARC Discount Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(103);
			cell.setCellValue("Burstable ARC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(104);
			cell.setCellValue("CPE Install NRC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(105);
			cell.setCellValue("CPE Install NRC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(106);
			cell.setCellValue("CPE Install NRC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(107);
			cell.setCellValue("CPE Outright Sale NRC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(108);
			cell.setCellValue("CPE Outright Sale NRC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(109);
			cell.setCellValue("CPE Outright Sale NRC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(110);
			cell.setCellValue("CPE Rental ARC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(111);
			cell.setCellValue("CPE Rental ARC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(112);
			cell.setCellValue("CPE Rental ARC Sale Price");
			cell.setCellStyle(headerEditableColor);
			cell = subHeaderRow.createCell(113);
			cell.setCellValue("CPE Management ARC List Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(114);
			cell.setCellValue("CPE Management ARC Discount");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(115);
			cell.setCellValue("CPE Management ARC Sale Price");
			cell.setCellStyle(headerEditableColor);
			/*
			 * cell = subHeaderRow.createCell(117); 
			 * cell.setCellValue("Link Management Charges ARC List Price");
			 *  cell = subHeaderRow.createCell(118);
			 * cell.setCellValue("Link Management Charges ARC Discount"); 
			 * cell = subHeaderRow.createCell(119);
			 * cell.setCellValue("Link Management Charges ARC Sale Price");
			 */
			cell = subHeaderRow.createCell(116);
			cell.setCellValue("TOTAL NRC Sale Price");
			cell.setCellStyle(headerCellNonEditable);
			cell = subHeaderRow.createCell(117);
			cell.setCellValue("TOTAL ARC Sale Price");
			cell.setCellStyle(headerCellNonEditable);
			
			Cell headerCellEight = headerRow.createCell(118);
			headerCellEight.setCellValue("Approval Comments");
			CellStyle cellStyleEight = headerRow.getSheet().getWorkbook().createCellStyle();
			cellStyleEight.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyleEight.setFont(font);
			headerCellEight.setCellStyle(cellStyleEight);
			sheet.addMergedRegion(CellRangeAddress.valueOf("DO1:DQ1"));
			/*
			 * cell = subHeaderRow.createCell(122); 
			 * cell.setCellValue("Existing ARC Check (MAC cases)"); 
			 * cell = subHeaderRow.createCell(123); 
			 * cell.setCellValue("Existing CPE Check (MAC Cases)"); 
			 * cell = subHeaderRow.createCell(124);
			 * cell.setCellValue("Offline Approval Available");
			 */
			cell = subHeaderRow.createCell(118);
			cell.setCellValue("Site Level Action");
			cell.setCellStyle(headerEditableColor);
			
			cell = subHeaderRow.createCell(119);
			cell.setCellValue("Commercial Manager Comments");
			cell.setCellStyle(headerEditableColor);

			cell = subHeaderRow.createCell(120);
			cell.setCellValue("Existing Commercial Manager Comments");
			cell.setCellStyle(headerCellNonEditable);
			
			List<String> siteLevelList = new ArrayList<>();
			siteLevelList.add("Approve");
			siteLevelList.add("Reject");
			//cell.getColumnIndex();
			/* profile validation begins */
			DataValidation profileDataValidation = null;
			DataValidationConstraint profileConstraint = null;
			DataValidationHelper profileValidationHelper = null;
			profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			
			Integer firstRow = 2;
			Integer lastRow = illGvpnMultiSiteInputs.size()+1;
			//Integer lastColumn = 0;
			//String siteLevelActionCell = "Site Level Action";
			//if(cell.getStringCellValue().equalsIgnoreCase(siteLevelActionCell)) {
			Integer siteLevelActionColumn = 118;
			Integer lastColumn = 121;
			//}
			// As the column index of Site level action is 125
			CellRangeAddressList profileAddressList = new CellRangeAddressList(firstRow,lastRow , siteLevelActionColumn, siteLevelActionColumn);
			// add the selected profiles
			profileConstraint = profileValidationHelper
					.createExplicitListConstraint(siteLevelList.stream().toArray(String[]::new));
			profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
			profileDataValidation.setSuppressDropDownArrow(true);
			profileDataValidation.setShowErrorBox(true);
			profileDataValidation.setShowPromptBox(true);
			sheet.addValidationData(profileDataValidation);
			
			int rowCount[] = {2};
			int index[]= {2};
			if(!illGvpnMultiSiteInputs.isEmpty()) {
				illGvpnMultiSiteInputs.stream().forEach(aBook->{
					Row row = sheet.createRow(rowCount[0]);
					writeSiteInformation(aBook, row,index[0]);
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
		String fileName = "ILL GVPN Report.xlsx";
		response.reset();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {

		}
		outByteStream.close();
	}
	catch (Exception e) {
			LOGGER.error("Exception occured returnIllGvpnMultisiteReportExcel: ");
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
			}
	}
	
	private List<IllGvpnMultiSiteInputs> getSiteRelatedInformation(String quoteCode,List<IllGvpnMultiSiteInputs> illGvpnSiteInfoList) throws TclCommonException {
		LOGGER.info("Entering in to getSiteRelatedInformation Method");
		if(quoteCode.isEmpty() || !Objects.nonNull(quoteCode)) {
			throw new TclCommonRuntimeException(ExceptionConstants.QUOTE_EMPTY,
					ResponseResource.R_CODE_ERROR);
		}else {
			try {
				List<QuoteIllSite> quoteIllSites = new ArrayList<QuoteIllSite>();
				Quote quoteEntity = quoteRepository.findByQuoteCode(quoteCode);
				List<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findByQuote_Id(quoteEntity.getId());
				QuoteToLeProductFamily quoteToLeProductFamily = 
						quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteToLeEntity.get(0).getId());
				List<ProductSolution> productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily);
				productSolutions.forEach(productSolution->{
					String quoteBulkUpdate = null;
					quoteBulkUpdate = quoteToLeEntity.get(0).getQuoteBulkUpdate();
					if(Objects.isNull(quoteBulkUpdate)) {
						 List<QuoteIllSite> quoteIllSitesResponse = illSiteRepository.findByProductSolutionIdAndStatus(productSolution.getId(), (byte) 1);
						 quoteIllSites.addAll(quoteIllSitesResponse);
						 LOGGER.info("quoteIllSites size "+quoteIllSites.size());
						 List<QuoteIllSite> quoteIllSitesForApproval = new ArrayList<QuoteIllSite>();
							List<QuoteIllSite> quoteIllSitesForRejection = new ArrayList<QuoteIllSite>();
							if(!quoteIllSitesResponse.isEmpty() && Objects.nonNull(quoteIllSitesResponse.get(0).getCommercialApproveStatus()) && Objects.nonNull(quoteIllSitesResponse.get(0).getCommercialRejectionStatus())) {
								quoteIllSitesForApproval = quoteIllSitesResponse.stream().filter(quoteIllSite -> quoteIllSite.getCommercialApproveStatus().equals("1")).collect(Collectors.toList());
								quoteIllSitesForRejection = quoteIllSitesResponse.stream().filter(quoteIllSite -> quoteIllSite.getCommercialRejectionStatus().equals("1")).collect(Collectors.toList());
							}
							quoteIllSites.addAll(quoteIllSitesForApproval);
							quoteIllSites.addAll(quoteIllSitesForRejection);
							quoteIllSitesForApproval.clear();
							quoteIllSitesForRejection.clear();
							if(quoteIllSites.isEmpty()) {
								quoteIllSites.addAll(quoteIllSitesResponse);
							}
					}
					
					if(Objects.nonNull(quoteBulkUpdate) && quoteBulkUpdate.equals("1")) {
						List<QuoteIllSite> quoteIllSitesResponse = illSiteRepository.findByProductSolutionIdAndStatus(productSolution.getId(), (byte) 1);
						List<QuoteIllSite> quoteIllSitesForApproval = new ArrayList<QuoteIllSite>();
						List<QuoteIllSite> quoteIllSitesForRejection = new ArrayList<QuoteIllSite>();
						
						quoteIllSitesForApproval = quoteIllSitesResponse.stream().filter(quoteIllSite -> quoteIllSite.getCommercialApproveStatus().equals("1")).collect(Collectors.toList());
						quoteIllSitesForRejection = quoteIllSitesResponse.stream().filter(quoteIllSite -> quoteIllSite.getCommercialRejectionStatus().equals("1")).collect(Collectors.toList());
						quoteIllSites.addAll(quoteIllSitesForApproval);
						quoteIllSites.addAll(quoteIllSitesForRejection);
						quoteIllSitesForApproval.clear();
						quoteIllSitesForRejection.clear();
						if(quoteIllSites.isEmpty()) {
							quoteIllSites.addAll(quoteIllSitesResponse);
						}
					}
				String productFamily = null;
				if(quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("GVPN")) {
					 productFamily = "GVPN";
				}else if(quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("IAS")) {
					 productFamily = "IAS";
				}
				List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
				List<Integer> quoteProductComponentIds = new ArrayList<>();
				List<String> basicAttributesList = new ArrayList<String>();
				List<String> componentsList = new ArrayList<String>();
				Map<String,Integer> attributesMap = new HashMap<String,Integer>();
				Map<String,Integer> componentsMap = new HashMap<String,Integer>();
				basicAttributesList = getBasicAttributesList();
				componentsList = getComponentsList();
				attributesMap = getAttributesMap(basicAttributesList);
				componentsMap = getComponentsMap(componentsList);
				SolutionDetail solutionDetail = null;
				try {
					solutionDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
							SolutionDetail.class);
				} catch (TclCommonException e) {
					// TODO Auto-generated catch block
					LOGGER.error("Error occured while capturing Solution Details "+e);
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,
							ResponseResource.R_CODE_ERROR);
				}
				boolean dualFlag = false;
				List<ComponentDetail> componentsDetails = new ArrayList<>();
				List<AttributeDetail> attributeDetails = new ArrayList<>();
				componentsDetails = solutionDetail.getComponents();
				for(ComponentDetail componentDetail : componentsDetails) {
					if(componentDetail.getName().equalsIgnoreCase("GVPN Common") && componentDetail.getType().equalsIgnoreCase("primary")) {
						attributeDetails = componentDetail.getAttributes();
						
						for(AttributeDetail attributeDetail : attributeDetails) {
							if(attributeDetail.getName().equalsIgnoreCase("Resiliency")&&attributeDetail.getValue().equalsIgnoreCase("Yes")) {
								dualFlag = true;
							}
						}
					}
					if(componentDetail.getName().equalsIgnoreCase("IAS Common") && componentDetail.getType().equalsIgnoreCase("primary")) {
						attributeDetails = componentDetail.getAttributes();
						
						for(AttributeDetail attributeDetail : attributeDetails) {
							if(attributeDetail.getName().equalsIgnoreCase("Resiliency")&&attributeDetail.getValue().equalsIgnoreCase("Yes")) {
								dualFlag = true;
							}
						}
						
					}
				}
				if (Objects.nonNull(quoteIllSites) && !quoteIllSites.isEmpty()) {
					for (QuoteIllSite sites : quoteIllSites) {
						IllGvpnMultiSiteInputs illGvpnMultiSiteInputs = new IllGvpnMultiSiteInputs();
						AddressDetailsMultiSite addressDetailsMultiSite = new AddressDetailsMultiSite();
						ExistingMacdComponents existingMacdComponents = new ExistingMacdComponents();
						MultiSiteBasicAttributes multiSiteBasicAttributes = new MultiSiteBasicAttributes();
						MultiSiteFeasibility multiSiteFeasibility = new MultiSiteFeasibility();
						MultiSiteCpeAttributes multiSiteCpeAttributes = new MultiSiteCpeAttributes();
						MultiSitePricingAttributes multiSitePricingAttributes = new MultiSitePricingAttributes();
						boolean primaryFlag = true;
						boolean secondaryFlag = false;
						addressDetailsMultiSite = getAddressDetailsMultiSite(addressDetailsMultiSite,sites,primaryFlag,secondaryFlag);
						illGvpnMultiSiteInputs.setAddressDetailsMultiSite(addressDetailsMultiSite);
						if(productFamily.equals("IAS")) {
							quoteProductComponents = quoteProductComponentRepository.
									findByReferenceIdAndReferenceNameAndType(sites.getId(), "ILL_SITES", "primary");
									
							for(QuoteProductComponent quoteProductComponentsOne : quoteProductComponents) {
								quoteProductComponentIds.add(quoteProductComponentsOne.getId());
							}
						}else{
							quoteProductComponents = quoteProductComponentRepository
									.findByReferenceIdAndReferenceNameAndType(sites.getId(), "GVPN_SITES", "primary");
							for(QuoteProductComponent quoteProductComponentsOne : quoteProductComponents) {
								quoteProductComponentIds.add(quoteProductComponentsOne.getId());
							}
						}

						LOGGER.info("quoteProductComponentIds size{}, siteId{}", quoteProductComponentIds.size(), sites.getId() );
						List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues = new ArrayList<QuoteProductComponentsAttributeValue>();
						for(Integer quoteProductComponentId : quoteProductComponentIds) {
							List<QuoteProductComponentsAttributeValue>  quoteProdCompAttribute = quoteProductComponentsAttributeValueRepository
									.findByQuoteProductComponent_Id(quoteProductComponentId);
							if(!quoteProdCompAttribute.isEmpty())
								quoteProdCompAttributeValues.addAll(quoteProdCompAttribute);
						}
						//	List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentId(quoteProductComponentIds);
										// MACD attributes if its a macd order 
										if (Objects.nonNull(quoteToLeEntity.get(0).getQuoteType())
							                    && quoteToLeEntity.get(0).getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
											
											existingMacdComponents = getExistingMacdComponents(quoteEntity,quoteToLeEntity,sites,quoteProdCompAttributeValues,quoteProductComponents,attributesMap,componentsMap);
										illGvpnMultiSiteInputs.setMacdFlag(true);
										illGvpnMultiSiteInputs.setExistingMacdComponents(existingMacdComponents);
										}
										// Basic attribute
										multiSiteBasicAttributes = getMultiSiteBasicAttributesInfo(quoteToLeEntity,sites,quoteProdCompAttributeValues,attributesMap);
										multiSiteBasicAttributes.setProduct(productFamily);
										illGvpnMultiSiteInputs.setMultiSiteBasicAttributes(multiSiteBasicAttributes);
										
										//feasibility
										multiSiteFeasibility = getFeasibilityAttributes(sites,primaryFlag,secondaryFlag);
										illGvpnMultiSiteInputs.setMultiSiteFeasibility(multiSiteFeasibility);
											
										//cpe attributes
										multiSiteCpeAttributes = getCpeAttributes(sites,quoteProdCompAttributeValues,attributesMap);	
										illGvpnMultiSiteInputs.setMultiSiteCpeAttributes(multiSiteCpeAttributes);
														
										multiSitePricingAttributes = getPricingAttributes(quoteEntity,quoteToLeEntity, quoteProductComponents, quoteProdCompAttributeValues,sites,attributesMap,componentsMap,productFamily,multiSiteFeasibility,primaryFlag,secondaryFlag);
										
										illGvpnMultiSiteInputs.setMultiSitePricingAttributes(multiSitePricingAttributes);	
										illGvpnSiteInfoList.add(illGvpnMultiSiteInputs);
										if(dualFlag) {
											IllGvpnMultiSiteInputs illGvpnMultiSiteInputsSecondary = new IllGvpnMultiSiteInputs();
											illGvpnMultiSiteInputsSecondary = getSecondarySiteInformation(illGvpnMultiSiteInputsSecondary,sites,productFamily,attributesMap,componentsMap,quoteEntity,quoteToLeEntity);
											illGvpnSiteInfoList.add(illGvpnMultiSiteInputsSecondary);
										}
										quoteProductComponentIds.clear();
							}
					}
				quoteIllSites.clear();
				});
			}catch(Exception e) {
				LOGGER.error("Error in getSiteRelatedInformation",e);				
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,
						ResponseResource.R_CODE_ERROR);
			}
			LOGGER.info("Exiting getSiteRelatedInformation Method");
		return illGvpnSiteInfoList;
			
		}
	}
		
	
	private IllGvpnMultiSiteInputs getSecondarySiteInformation(IllGvpnMultiSiteInputs illGvpnMultiSiteInputsSecondary, QuoteIllSite sites, String productFamily, Map<String, Integer> attributesMap, Map<String, Integer> componentsMap, Quote quoteEntity, List<QuoteToLe> quoteToLeEntity) {
		LOGGER.info("Entering getSecondarySiteInformation Method");
		AddressDetailsMultiSite addressDetailsMultiSiteSecondary = new AddressDetailsMultiSite();
		ExistingMacdComponents existingMacdComponentsSecondary = new ExistingMacdComponents();
		MultiSiteBasicAttributes multiSiteBasicAttributesSecondary = new MultiSiteBasicAttributes();
		MultiSiteFeasibility multiSiteFeasibilitySecondary = new MultiSiteFeasibility();
		MultiSiteCpeAttributes multiSiteCpeAttributesSecondary = new MultiSiteCpeAttributes();
		MultiSitePricingAttributes multiSitePricingAttributesSecondary = new MultiSitePricingAttributes();
		List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
		List<Integer> quoteProductComponentIdsSecondary = new ArrayList<>();
		boolean primaryFlag = false;
		boolean secondaryFlag = true;
		addressDetailsMultiSiteSecondary = getAddressDetailsMultiSite(addressDetailsMultiSiteSecondary,sites,primaryFlag,secondaryFlag);
		illGvpnMultiSiteInputsSecondary.setAddressDetailsMultiSite(addressDetailsMultiSiteSecondary);
		if(productFamily.equals("IAS")) {
			quoteProductComponents = quoteProductComponentRepository.
					findByReferenceIdAndReferenceNameAndType(sites.getId(), "ILL_SITES", "secondary");
					
			for(QuoteProductComponent quoteProductComponentsOne : quoteProductComponents) {
				quoteProductComponentIdsSecondary.add(quoteProductComponentsOne.getId());
			}
		}else{
			quoteProductComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceNameAndType(sites.getId(), "GVPN_SITES", "secondary");
			for(QuoteProductComponent quoteProductComponentsOne : quoteProductComponents) {
				quoteProductComponentIdsSecondary.add(quoteProductComponentsOne.getId());
			}
		}
		LOGGER.info("quoteProductComponentIds size{}, siteId{}", quoteProductComponentIdsSecondary.size(),sites.getId() );
		List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues = new ArrayList<QuoteProductComponentsAttributeValue>();
		for(Integer quoteProductComponentId : quoteProductComponentIdsSecondary) {
			List<QuoteProductComponentsAttributeValue>  quoteProdCompAttribute = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponentId);
			if(!quoteProdCompAttribute.isEmpty())
				quoteProdCompAttributeValues.addAll(quoteProdCompAttribute);
		}
		//List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentId(quoteProductComponentIdsSecondary);
						// MACD attributes if its a macd order 
						if (Objects.nonNull(quoteToLeEntity.get(0).getQuoteType())
			                    && quoteToLeEntity.get(0).getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
							
							existingMacdComponentsSecondary = getExistingMacdComponents(quoteEntity,quoteToLeEntity,sites,quoteProdCompAttributeValues,quoteProductComponents,attributesMap,componentsMap);
							illGvpnMultiSiteInputsSecondary.setMacdFlag(true);
							illGvpnMultiSiteInputsSecondary.setExistingMacdComponents(existingMacdComponentsSecondary);
						}
						// Basic attribute
						multiSiteBasicAttributesSecondary = getMultiSiteBasicAttributesInfo(quoteToLeEntity,sites,quoteProdCompAttributeValues,attributesMap);
						multiSiteBasicAttributesSecondary.setProduct(productFamily);
						illGvpnMultiSiteInputsSecondary.setMultiSiteBasicAttributes(multiSiteBasicAttributesSecondary);
						
						//feasibility
						multiSiteFeasibilitySecondary = getFeasibilityAttributes(sites,primaryFlag,secondaryFlag);
						illGvpnMultiSiteInputsSecondary.setMultiSiteFeasibility(multiSiteFeasibilitySecondary);
							
						//cpe attributes
						multiSiteCpeAttributesSecondary = getCpeAttributes(sites,quoteProdCompAttributeValues,attributesMap);	
						illGvpnMultiSiteInputsSecondary.setMultiSiteCpeAttributes(multiSiteCpeAttributesSecondary);
										
						multiSitePricingAttributesSecondary = getPricingAttributes(quoteEntity,quoteToLeEntity, quoteProductComponents, quoteProdCompAttributeValues,sites,attributesMap,componentsMap,productFamily, multiSiteFeasibilitySecondary,primaryFlag,secondaryFlag);
						illGvpnMultiSiteInputsSecondary.setMultiSitePricingAttributes(multiSitePricingAttributesSecondary);	
						LOGGER.info("Exiting getSecondarySiteInformation Method");			
		return illGvpnMultiSiteInputsSecondary;
	}

	private Map<String, Integer> getComponentsMap(List<String> componentsList) {
		Map<String,Integer> componentsMap = new HashMap<String,Integer>();
		List<MstProductComponent> mstProductComponent = mstProductComponentRepository.findByNameIn(componentsList);
		for(MstProductComponent component : mstProductComponent) {
			componentsMap.put(component.getName(), component.getId());
		}
		return componentsMap;
	}

	private List<String> getComponentsList() {
		List<String> componentsList = new ArrayList<>();
		componentsList.add("Internet Port");
		componentsList.add("CPE Management");
		componentsList.add("VPN Port");
		componentsList.add("CPE");
		componentsList.add("Last mile");
		componentsList.add("Additional IPs");
		return componentsList;
	}

	private ExistingMacdComponents getExistingMacdComponents(Quote quoteEntity, List<QuoteToLe> quoteToLeEntity,
			QuoteIllSite sites, List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues, List<QuoteProductComponent> quoteProductComponents, Map<String, Integer> attributesMap, Map<String, Integer> componentsMap) {
		LOGGER.info("Entering getExistingMacdComponents Method");
		ExistingMacdComponents existingMacdComponents = new ExistingMacdComponents();
		try {
			Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(sites,quoteToLeEntity.get(0));
			LOGGER.info("serviceIds"+serviceIds);
			String currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
			if(currentServiceId == null) {
				currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
				}
			LOGGER.info("Current Service Id" + currentServiceId );
			String[] site = {null};
			SIServiceDetailDataBean serviceDetailbean = null;
			if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLeEntity.stream().findFirst().get().getQuoteType())) {
				 serviceDetailbean=macdUtils.getServiceDetailIASTermination(currentServiceId);
			} else {
			 serviceDetailbean=macdUtils.getServiceDetailIAS(currentServiceId);
			}
			if (serviceDetailbean!=null) {
				
					if (Objects.nonNull(serviceDetailbean)) {
						String serviceCommissionedDate = null;
						String oldContractTerm = null;
						String latLong = null;
						String serviceId = null;
						Integer serviceDetailId = null;
						String nrc=null;
						String arc=null;
						double mrc=0D;
						LOGGER.info("serviceDetailbean.getSiteType()" + serviceDetailbean.getSiteType() + "cdate:" + serviceDetailbean.getsCommisionDate());
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
							
							LOGGER.info("oldPortBw conversion"+oldPortBw+"bwUnitPort"+bwUnitPort+"serviceDetailbean.getCircuitExpiryDate()"+serviceDetailbean.getCircuitExpiryDate());
							oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);
							LOGGER.info("oldPortBw after conversion"+oldPortBw);
							
							
							existingMacdComponents
							.setPortBandWidth(Objects.nonNull(oldPortBw)
									? oldPortBw
									: "");
							
							
							existingMacdComponents.setServiceId(Objects.nonNull(serviceDetailbean.getTpsServiceId())?serviceDetailbean.getTpsServiceId():"");
							existingMacdComponents.setExistingArc(Objects.nonNull(arc)?arc:"");
							existingMacdComponents.setExistingNrc(Objects.nonNull(nrc)?nrc:"");
							existingMacdComponents.setContractTerm(Objects.nonNull(oldContractTerm)?oldContractTerm:"");
							existingMacdComponents.setLmProvider(Objects.nonNull(serviceDetailbean.getLastMileProvider())?serviceDetailbean.getLastMileProvider():"");
							existingMacdComponents.setCommisionDate(Objects.nonNull(serviceDetailbean.getsCommisionDate())?serviceDetailbean.getsCommisionDate().toString():"");
							LOGGER.info("oldLlBw local loop bw"+oldLlBw+"serviceCommissionedDate"+serviceDetailbean.getsCommisionDate()+"serviceDetailbean.getPortMode()"+serviceDetailbean.getPortMode()+"serviceDetailbean.getBillingCurrency()"
									+serviceDetailbean.getBillingCurrency());
							existingMacdComponents.setLocalLoopBandwidth(Objects.nonNull(oldLlBw)?oldLlBw:"");
						//	existingMacdComponents.setProduct(Objects.nonNull(serviceDetailbean.getErfPrdCatalogProductName())?serviceDetailbean.getErfPrdCatalogProductName():"");
							existingMacdComponents.setPortType(Objects.nonNull(serviceDetailbean.getPortMode())?serviceDetailbean.getPortMode():"");
							existingMacdComponents.setCurrency(Objects.nonNull(serviceDetailbean.getBillingCurrency())?serviceDetailbean.getBillingCurrency():"");
							existingMacdComponents.setExpiryDate(Objects.nonNull(serviceDetailbean.getCircuitExpiryDate())?serviceDetailbean.getCircuitExpiryDate().toString():"");
							
							List<SIAttributeBean> attributes=serviceDetailbean.getAttributes();
							if(!attributes.isEmpty()) {
								for(SIAttributeBean attribute:attributes) {
									LOGGER.info("attribute name "+attribute.getName()+"attribute.getValue()"+attribute.getValue());
									if(attribute.getName().equalsIgnoreCase("CPE Basic Chassis")) {
										existingMacdComponents.setCpeModel(Objects.nonNull(attribute.getValue())?attribute.getValue():"");
										
									}
									if(attribute.getName().equalsIgnoreCase("CPE Support Type")) {
										existingMacdComponents.setCpeSupportType(Objects.nonNull(attribute.getValue())?attribute.getValue():"");
										
									}
									if(attribute.getName().equalsIgnoreCase("CPE Management Type")) {
										existingMacdComponents.setCpeManagmentType(Objects.nonNull(attribute.getValue())?attribute.getValue():"");
									}
									
								}
							}
							LOGGER.info("ipv4 adddress pool size"+serviceDetailbean.getIpv4AddressPoolsize());
							existingMacdComponents.setIpV4AddressPoolSize(Objects.nonNull(serviceDetailbean.getIpv4AddressPoolsize())?serviceDetailbean.getIpv4AddressPoolsize().toString():"");
						

					}
				
			}
			
			
			
		}catch(Exception e) {
			LOGGER.error("Error in getExistingMacdComponents",e);		
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Exiting getExistingMacdComponents Method");
		return existingMacdComponents;
	}

	private MultiSitePricingAttributes getPricingAttributes(Quote quoteEntity,
			List<QuoteToLe> quoteToLeEntity, List<QuoteProductComponent> quoteProductComponents,List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues
			,QuoteIllSite sites, Map<String, Integer> attributesMap, Map<String, Integer> componentsMap, String productFamily, MultiSiteFeasibility multiSiteFeasibility, boolean primaryFlag, boolean secondaryFlag) {
		LOGGER.info("Entering getPricingAttributes Method");
		MultiSitePricingAttributes multiSitePricingAttributes = new MultiSitePricingAttributes();
		Integer internetPortId = null;
		Integer additionalIps = null;
		multiSitePricingAttributes.setContractTerm(Objects.nonNull(quoteToLeEntity.get(0).getTermInMonths())?quoteToLeEntity.get(0).getTermInMonths():"");
		multiSitePricingAttributes.setCurrency(Objects.nonNull(quoteToLeEntity.get(0).getCurrencyCode()) ? quoteToLeEntity.get(0).getCurrencyCode() : "");
		if(productFamily.equalsIgnoreCase("GVPN")) {
			 internetPortId =  componentsMap.get("VPN Port");
			 multiSitePricingAttributes = gvpnQuoteService.getListPriceDetails(multiSitePricingAttributes,primaryFlag,secondaryFlag,sites);
		}
		if(productFamily.equalsIgnoreCase("IAS")) {
			 internetPortId =  componentsMap.get("Internet Port");
			 additionalIps = componentsMap.get("Additional IPs");
			 
			 if(primaryFlag) {
				 Result response = new Result();
					PricingEngineResponse pricingDetails = pricingDetailsRepository
							.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(sites.getSiteCode(), "primary");
					if(Objects.nonNull(pricingDetails)) {
					String pricingResponse = pricingDetails.getResponseData();
					try {
						response = (Result) Utils.convertJsonToObject(pricingResponse, Result.class);
						multiSitePricingAttributes.setPortNrcListPrice(Objects.nonNull(response.getILLPortNRCAdjusted())?Utils.parseDouble(response.getILLPortNRCAdjusted()):0.0);
						multiSitePricingAttributes.setPortArcListPrice(Objects.nonNull(response.getILLARC())?Utils.parseDouble(response.getILLARC()):0.0);
						
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
						Double cpeRentalMrc = (Objects.nonNull(response.getCPERentalMRC()) ?Utils.parseDouble(response.getCPERentalMRC()):0.0);
						multiSitePricingAttributes.setCpeRentalArcListPrice(cpeRentalMrc * 12);//CPE Rental ARC List Price
						multiSitePricingAttributes.setCpeManagementArcListPrice(Objects.nonNull(response.getCPEManagementINR())?Utils.parseDouble(response.getCPEManagementINR()):0.0);
				
						//macd
						if((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(response.getQuotetypeQuote())) {
							LOGGER.info("MACD_QUOTE_TYPE>>>> inside macd " + response.getQuotetypeQuote());
							LOGGER.info("MACD_QUOTE_TYPE>>>> inside response " + response.toString());

							multiSitePricingAttributes.setPortArcListPrice(Objects.nonNull(response.getPortArcLP())?Utils.parseDouble(response.getPortArcLP()):0.0);
							Double lmNrcBwOnrf = Objects.nonNull(response.getLmNrcBwOnrf())?Utils.parseDouble(response.getLmNrcBwOnrf()):0.0;
							Double lmNrcBwOnwl = Objects.nonNull(response.getLmNrcBwOnwl())?Utils.parseDouble(response.getLmNrcBwOnwl()):0.0;
							Double lmNrcBwProvOfrf = Objects.nonNull(response.getLmNrcBwProvOfrf())?Utils.parseDouble(response.getLmNrcBwProvOfrf()):0.0;
							Double lmNrcListPrice = lmNrcBwOnrf + lmNrcBwOnwl + lmNrcBwProvOfrf;
							LOGGER.info("MACD_QUOTE_TYPE>>>>lmNrcListPrice{} " + lmNrcListPrice);
							//multiSitePricingAttributes.setLastMileNrcListPrice(35000.0);
							multiSitePricingAttributes.setLastMileNrcListPrice(lmNrcListPrice);

							Double lmArcBwOnwl = Objects.nonNull(response.getLmArcBwOnwl())?Utils.parseDouble(response.getLmArcBwOnwl()):0.0;
							Double lmArcBwOnrf  = Objects.nonNull(response.getLmArcBwOnrf())?Utils.parseDouble(response.getLmArcBwOnrf()):0.0;
							Double lmArcBwProvOfrf = Objects.nonNull(response.getLmArcBwProvOfrf())?Utils.parseDouble(response.getLmArcBwProvOfrf()):0.0;
							Double lmArcListPrice = lmArcBwOnwl + lmArcBwOnrf + lmArcBwProvOfrf;
							LOGGER.info("MACD_QUOTE_TYPE>>>>lmArcListPrice{} " + lmArcListPrice);

							//multiSitePricingAttributes.setLastMileArcListPrice(119220.0);
							
							multiSitePricingAttributes.setLastMileArcListPrice(lmArcListPrice);
							Double lmNrcMastOnrf = Objects.nonNull(response.getLmNrcMastOnrf())?Utils.parseDouble(response.getLmNrcMastOnrf()):0.0;
							Double lmNrcMastOfrf = Objects.nonNull(response.getLmNrcMastOfrf())?Utils.parseDouble(response.getLmNrcMastOfrf()):0.0;
							Double mastNrcListPriceMacd = lmNrcMastOnrf + lmNrcMastOfrf;
							LOGGER.info("MACD_QUOTE_TYPE>>>>mastNrcListPriceMacd{} " + mastNrcListPriceMacd);

							multiSitePricingAttributes.setMastNrcListPrice(mastNrcListPriceMacd);
						}
						
					}catch (TclCommonException e) {
						LOGGER.error("Error in getPricingAttributes",e);				
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,
								ResponseResource.R_CODE_ERROR);
					}
			 }
		}
				if(secondaryFlag) {
					Result response = new Result();
				PricingEngineResponse pricingDetails = pricingDetailsRepository
						.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(sites.getSiteCode(), "secondary");
				if(Objects.nonNull(pricingDetails)) {
				String pricingResponse = pricingDetails.getResponseData();
				try {
					response = (Result) Utils.convertJsonToObject(pricingResponse, Result.class);
					multiSitePricingAttributes.setPortNrcListPrice(Objects.nonNull(response.getILLPortNRCAdjusted())?Utils.parseDouble(response.getILLPortNRCAdjusted()):0.0);
					multiSitePricingAttributes.setPortArcListPrice(Objects.nonNull(response.getILLARC())?Utils.parseDouble(response.getILLARC()):0.0);
					
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
				LOGGER.error("Error in getPricingAttributes",e);				
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,
						ResponseResource.R_CODE_ERROR);
				}
			}
		}
				
		}
			
		Integer lastMileId =  componentsMap.get("Last mile");
		try{
			for(QuoteProductComponent quoteProductComponent : quoteProductComponents) {
				
				if(quoteProductComponent.getMstProductComponent().getId() == internetPortId) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("COMPONENTS", quoteProductComponent.getId().toString(), quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setPortNrcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentNrc())? quotePrice.get().getDiscountPercentNrc() : 0.0);
						multiSitePricingAttributes.setPortNrcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveNrc())? quotePrice.get().getEffectiveNrc() : 0.0);
						multiSitePricingAttributes.setPortArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc())? quotePrice.get().getDiscountPercentArc() : 0.0);
						multiSitePricingAttributes.setPortArcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveArc())? quotePrice.get().getEffectiveArc() : 0.0);
					}
				}
				
				if(quoteProductComponent.getMstProductComponent().getId() == lastMileId) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("COMPONENTS", quoteProductComponent.getId().toString(), quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setLastMileNrcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentNrc())? quotePrice.get().getDiscountPercentNrc() : 0.0);
						multiSitePricingAttributes.setLastMileNrcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveNrc())? quotePrice.get().getEffectiveNrc() : 0.0);
						multiSitePricingAttributes.setLastMileArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc())? quotePrice.get().getDiscountPercentArc() : 0.0);
						multiSitePricingAttributes.setLastMileArcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveArc())? quotePrice.get().getEffectiveArc() : 0.0);
					}
				}
				
				if(quoteProductComponent.getMstProductComponent().getId() == additionalIps) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("COMPONENTS", quoteProductComponent.getId().toString(), quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setAdditionalIpArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc())? quotePrice.get().getDiscountPercentArc() : 0.0);
						multiSitePricingAttributes.setAdditionalIpArcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveArc())? quotePrice.get().getEffectiveArc() : 0.0);
					}
				}
			}
		}catch(Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
		
		Integer mastCostId = attributesMap.get("Mast Cost");
		Integer burstableBandWidthId = attributesMap.get("Burstable Bandwidth");
		Integer cpeInstallId = attributesMap.get("CPE Discount Install");
		Integer cpeDiscountId = attributesMap.get("CPE Discount Outright Sale");
		Integer cpeRentalId = attributesMap.get("CPE Discount Rental");
		Integer cpeManagementId = attributesMap.get("CPE Discount Management");
		try {
			for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProdCompAttributeValues) {
				Integer attributeId = quoteProductComponentsAttributeValue.getProductAttributeMaster().getId();
				if(attributeId == mastCostId) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",quoteProductComponentsAttributeValue.getId().toString(),quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setMastNrcDiscountPrice(Objects.nonNull(quotePrice.get().getDiscountPercentNrc()) ? quotePrice.get().getDiscountPercentNrc() : 0.0);
						multiSitePricingAttributes.setMastNrcSalesPrice(Objects.nonNull(quotePrice.get().getEffectiveUsagePrice()) ? quotePrice.get().getEffectiveUsagePrice() : 0.0);
					}
				}
				
				if(attributeId == burstableBandWidthId) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",quoteProductComponentsAttributeValue.getId().toString(),quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setBurstableArcDiscountPrice(Objects.nonNull(quotePrice.get().getDiscountPercentArc()) ? quotePrice.get().getDiscountPercentArc() : 0.0);
						multiSitePricingAttributes.setBurstableArcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveUsagePrice()) ? quotePrice.get().getEffectiveUsagePrice() : 0.0);
					}
				}
				
				if(attributeId == cpeInstallId) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",quoteProductComponentsAttributeValue.getId().toString(),quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setCpeInstallNrcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentNrc()) ? quotePrice.get().getDiscountPercentNrc() : 0.0);
						multiSitePricingAttributes.setCpeInstallNRCSalePrice(Objects.nonNull(quotePrice.get().getEffectiveNrc()) ? quotePrice.get().getEffectiveNrc() : 0.0);
					}
				}
				
				if(attributeId == cpeDiscountId) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",quoteProductComponentsAttributeValue.getId().toString(),quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setCpeOutrightSaleNrcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentNrc()) ? quotePrice.get().getDiscountPercentNrc() : 0.0);
						multiSitePricingAttributes.setCpeOutrightSaleNrcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveNrc()) ? quotePrice.get().getEffectiveNrc() : 0.0);
					}
				}
				
				if(attributeId == cpeRentalId) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",quoteProductComponentsAttributeValue.getId().toString(),quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setCpeRentalArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc()) ? quotePrice.get().getDiscountPercentArc() : 0.0);
						multiSitePricingAttributes.setCpeRentalArcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveArc()) ? quotePrice.get().getEffectiveArc() : 0.0);
					}
				}
				
				if(attributeId == cpeManagementId) {
					Optional<QuotePrice> quotePrice = Optional.ofNullable(quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId("ATTRIBUTES",quoteProductComponentsAttributeValue.getId().toString(),quoteEntity.getId()));
					if(quotePrice.isPresent()) {
						multiSitePricingAttributes.setCpeManagementArcDiscount(Objects.nonNull(quotePrice.get().getDiscountPercentArc()) ? quotePrice.get().getDiscountPercentArc() : 0.0);
						multiSitePricingAttributes.setCpeManagementArcSalePrice(Objects.nonNull(quotePrice.get().getEffectiveArc()) ? quotePrice.get().getEffectiveArc() : 0.0);
					}
				}
			}
			Double portArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getPortArcSalePrice())?multiSitePricingAttributes.getPortArcSalePrice():0.0;
			Double lastMileArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getLastMileArcSalePrice())?multiSitePricingAttributes.getLastMileArcSalePrice():0.0;
			Double additionalIpArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getAdditionalIpArcSalePrice())?multiSitePricingAttributes.getAdditionalIpArcSalePrice():0.0;
			Double burstableArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getBurstableArcSalePrice())?multiSitePricingAttributes.getBurstableArcSalePrice():0.0;
			Double cpeRentalArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getCpeRentalArcSalePrice())?multiSitePricingAttributes.getCpeRentalArcSalePrice():0.0;
			Double cpeManagementArcSalePrice = Objects.nonNull(multiSitePricingAttributes.getCpeManagementArcSalePrice())?multiSitePricingAttributes.getCpeManagementArcSalePrice():0.0;
			
			Double totalArcSalePrice = portArcSalePrice + lastMileArcSalePrice + additionalIpArcSalePrice +
					burstableArcSalePrice + cpeRentalArcSalePrice + cpeManagementArcSalePrice;
	
			Double portNrcSalePrice = Objects.nonNull(multiSitePricingAttributes.getPortNrcSalePrice())?multiSitePricingAttributes.getPortNrcSalePrice():0.0;
			Double lastMileNrcSalePrice = Objects.nonNull(multiSitePricingAttributes.getLastMileNrcSalePrice())?multiSitePricingAttributes.getLastMileNrcSalePrice():0.0;
			Double mastNrcSalePrice = Objects.nonNull(multiSitePricingAttributes.getMastNrcSalesPrice())?multiSitePricingAttributes.getMastNrcSalesPrice():0.0;
			Double cpeInstallNrcSalePrice = Objects.nonNull(multiSitePricingAttributes.getCpeInstallNRCSalePrice())?multiSitePricingAttributes.getCpeInstallNRCSalePrice():0.0;
			Double cpeOutrightSaleNrcSalePrice = Objects.nonNull(multiSitePricingAttributes.getCpeOutrightSaleNrcSalePrice())?multiSitePricingAttributes.getCpeOutrightSaleNrcSalePrice():0.0;
			
			Double totalNrcSalePrice = portNrcSalePrice + lastMileNrcSalePrice + mastNrcSalePrice
			+ cpeInstallNrcSalePrice + cpeOutrightSaleNrcSalePrice;
			multiSitePricingAttributes.setTotalArcSalePrice(totalArcSalePrice);
			multiSitePricingAttributes.setTotalNrcSalePrice(totalNrcSalePrice);
			
			   LOGGER.info("BEFORE binding existing manager comments siteid::"+sites.getId()+"productFamily:"+productFamily);
			   MstProductComponent mstProductComponents = mstProductComponentRepository.findByName("SITE_PROPERTIES");
			   MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(productFamily, (byte) 1);
			if (mstProductComponents != null && mstProductFamily != null) {
				List<QuoteProductComponent> siteQuoteProductCommentComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndMstProductFamily(sites.getId(), mstProductComponents,
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
			
		}catch(Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Exiting getPricingAttributes Method");
		return multiSitePricingAttributes;
	}


	private MultiSiteCpeAttributes getCpeAttributes(QuoteIllSite sites, List<QuoteProductComponentsAttributeValue> quoteProdCompAttributeValues, Map<String, Integer> attributesMap) {
		LOGGER.info("Entering getCpeAttributes Method");
		MultiSiteCpeAttributes multiSiteCpeAttributes = new MultiSiteCpeAttributes();
		try {
			for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProdCompAttributeValues) {
				Integer attributeId = quoteProductComponentsAttributeValue.getProductAttributeMaster().getId();
				if(Objects.nonNull(attributeId)) {
					if(attributeId == attributesMap.get("CPE Basic Chassis")) {
						multiSiteCpeAttributes.setCpeBasicChassis(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())?quoteProductComponentsAttributeValue.getAttributeValues():"");
					}
					if(attributeId == attributesMap.get("CPE Intl Chassis flag")) {
						multiSiteCpeAttributes.setCpeIntlChassisFlag(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())?quoteProductComponentsAttributeValue.getAttributeValues():"");
					}
					if(attributeId == attributesMap.get("Cube Licenses")) {
						multiSiteCpeAttributes.setCubeLicenses(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())?quoteProductComponentsAttributeValue.getAttributeValues():"");
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("Error occured in getCpeAttributes "+e);
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Exiting getCpeAttributes Method");
		return multiSiteCpeAttributes;
	}

	private MultiSiteFeasibility getFeasibilityAttributes(QuoteIllSite sites,boolean primaryFlag, boolean secondaryFlag) {
		LOGGER.info("Entering getFeasibilityAttributes Method");
		MultiSiteFeasibility multiSiteFeasibility = new MultiSiteFeasibility();
		try {
			List<SiteFeasibility> siteFeasibilityList = new ArrayList<SiteFeasibility>();
			if(primaryFlag) {
				siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelectedAndType(sites.getId(),(byte) 1, "primary");			
			}
			if(secondaryFlag) {
				siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelectedAndType(sites.getId(),(byte) 1, "secondary");
			}
			Feasible site = new Feasible();
			if(!CollectionUtils.isEmpty(siteFeasibilityList) && Objects.nonNull(siteFeasibilityList)) {
					try {
						String responseJson = siteFeasibilityList.get(0).getResponseJson();
							LOGGER.info("Getting feasibility Json response");
							 site = (Feasible) Utils.convertJsonToObject(responseJson, Feasible.class);
					} catch (TclCommonException e) {
						LOGGER.error("Error in getFeasibilityAttributes",e);				
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,
								ResponseResource.R_CODE_ERROR);
					}
			}
			
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(sites.getId());
			multiSiteFeasibility.setFeasibilityStatus(Objects.nonNull(quoteIllSite.get().getFpStatus())?quoteIllSite.get().getFpStatus():"");
				multiSiteFeasibility.setFeasibilityType(!CollectionUtils.isEmpty(siteFeasibilityList) && Objects.nonNull(siteFeasibilityList.get(0).getFeasibilityType())?siteFeasibilityList.get(0).getFeasibilityType():"");
				multiSiteFeasibility.setFeasibilityValidity(Objects.nonNull(site.getValidityPeriod())?site.getValidityPeriod():"");
				multiSiteFeasibility.setFeasibilityCode(!CollectionUtils.isEmpty(siteFeasibilityList)?siteFeasibilityList.get(0).getFeasibilityCode():"");
				multiSiteFeasibility.setFeasibilityMode(!CollectionUtils.isEmpty(siteFeasibilityList)?siteFeasibilityList.get(0).getFeasibilityMode():"");
				multiSiteFeasibility.setFeasibilityType(!CollectionUtils.isEmpty(siteFeasibilityList)?siteFeasibilityList.get(0).getFeasibilityType():"");
				multiSiteFeasibility.setFeasibilityStatus(Objects.nonNull(quoteIllSite.get().getFpStatus())?quoteIllSite.get().getFpStatus():"");
				multiSiteFeasibility.setTclPop(Objects.nonNull(site.getPopName())?site.getPopName():"");
				multiSiteFeasibility.setCd(Objects.nonNull(site.getPOPDISTKMSERVICEMOD())?Integer.toString(site.getPOPDISTKMSERVICEMOD()):"");
				multiSiteFeasibility.setFeasibilityCheck(!CollectionUtils.isEmpty(siteFeasibilityList)?siteFeasibilityList.get(0).getFeasibilityCheck():"");
				multiSiteFeasibility.setConnectedBuilding(Objects.nonNull(site.getConnectedBuildingTag())?Integer.toString(site.getConnectedBuildingTag()):"");//string or integer
				multiSiteFeasibility.setConnectedCustomer(Objects.nonNull(site.getConnectedCustTag())?Integer.toString(site.getConnectedCustTag()):"");// string or integer
				multiSiteFeasibility.setOspDistance(Objects.nonNull(site.getMinHhFatg())?Float.toString(site.getMinHhFatg()):"");// confirm from feasible response 
				multiSiteFeasibility.setOspCapex(Objects.nonNull(site.getLmNrcOspcapexOnwl())?site.getLmNrcOspcapexOnwl():"");//lm_nrc_ospcapex_onwl
				multiSiteFeasibility.setInBuildingCapex(Objects.nonNull(site.getLmNrcInbldgOnwl())?Integer.toString(site.getLmNrcInbldgOnwl()):"");
				multiSiteFeasibility.setMuxCost(Objects.nonNull(site.getLmNrcMuxOnwl())?Integer.toString(site.getLmNrcMuxOnwl()):"");// get confirmation lm_nrc_mux_onwl
				multiSiteFeasibility.setProwValueArc(Objects.nonNull(site.getLmArcProwOnwl())?site.getLmArcProwOnwl():"");//lm_arc_prow_onwl
				multiSiteFeasibility.setProwValueOtc(Objects.nonNull(site.getLmNrcProwOnwl())?site.getLmNrcProwOnwl():"");//lm_nrc_prow_onwl
				multiSiteFeasibility.setFeasibilityRemarks(Objects.nonNull(site.getFeasibilityRemarks())?site.getFeasibilityRemarks():"");
				if(multiSiteFeasibility.getFeasibilityMode().equalsIgnoreCase("OffnetRF") || multiSiteFeasibility.getFeasibilityMode().equalsIgnoreCase("Offnet Wireless")) {
					multiSiteFeasibility.setMastHeight(Objects.nonNull(Float.toString(site.getAvgMastHt()))?Float.toString(site.getAvgMastHt()):"");
				}else {
					// check for Wireless - RADWIN', 'Wireless - UBR PMP / WiMax
					multiSiteFeasibility.setMastHeight("");
				}
				if(multiSiteFeasibility.getFeasibilityMode().equalsIgnoreCase("OnnetRF") || multiSiteFeasibility.getFeasibilityMode().equalsIgnoreCase("Onnet Wireless")) {
					multiSiteFeasibility.setMastNrc(Objects.nonNull(site.getLmNrcMastOnrf())?Integer.toString(site.getLmNrcMastOnrf()):"");//lm_nrc_mast_onrf 
				}else if(multiSiteFeasibility.getFeasibilityMode().equalsIgnoreCase("OffnetRF") || multiSiteFeasibility.getFeasibilityMode().equalsIgnoreCase("Offnet Wireless")){
					multiSiteFeasibility.setMastNrc(Objects.nonNull(site.getLmNrcMastOfrf())?Float.toString(site.getLmNrcMastOfrf()):"");
				}else {
					multiSiteFeasibility.setMastNrc("");
				}
				multiSiteFeasibility.setOffnetArc("");
				multiSiteFeasibility.setOffnetNrc("");
				multiSiteFeasibility.setType(!CollectionUtils.isEmpty(siteFeasibilityList)?siteFeasibilityList.get(0).getType():"");
				multiSiteFeasibility.setProvider(!CollectionUtils.isEmpty(siteFeasibilityList)?siteFeasibilityList.get(0).getProvider():"");
				
				String approveStatus = quoteIllSite.get().getCommercialApproveStatus();
				String rejectionStatus = quoteIllSite.get().getCommercialRejectionStatus();
				
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
			
		}catch(Exception e) {
			LOGGER.error("Error in getFeasibilityAttributes",e);		
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
		
		LOGGER.info("Exiting getFeasibilityAttributes Method");
		return multiSiteFeasibility;
	}

	private AddressDetailsMultiSite getAddressDetailsMultiSite(AddressDetailsMultiSite addressDetailsMultiSite,
			QuoteIllSite sites, boolean primaryFlag,boolean secondaryFlag) {
		LOGGER.info("Entering getAddressDetailsMultiSite Method");
		if(primaryFlag) {
			addressDetailsMultiSite.setSiteType("Primary");
		}
		if(secondaryFlag) {
			addressDetailsMultiSite.setSiteType("Secondary");
		}
		try {
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(sites.getErfLocSitebLocationId()));
			if(locationResponse != null && !locationResponse.isEmpty()) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				// Adding address details to illGvpnMultiSiteInputs.
				if(addressDetail != null ) {
					addressDetailsMultiSite.setSiteId(sites.getId());
					addressDetailsMultiSite.setAddress((Objects.nonNull(addressDetail.getAddressLineOne())?addressDetail.getAddressLineOne():"") + (Objects.nonNull(addressDetail.getAddressLineTwo())?addressDetail.getAddressLineTwo():""));
					addressDetailsMultiSite.setCountry(addressDetail.getCountry());
					
				}
				addressDetailsMultiSite.setSiteCode(sites.getSiteCode());
				LOGGER.info("Region for the locationId {} : {} ", sites.getErfLocSitebLocationId(),addressDetail.getRegion());
			} else {
				LOGGER.warn("Location data not found for the locationId {} ", sites.getErfLocSitebLocationId());
			}
		} catch (Exception e) {
			LOGGER.error("Error occured at getAddressDetailsMultiSite "+e);
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Exiting getAddressDetailsMultiSite Method");
		return addressDetailsMultiSite;
	}
	
	private MultiSiteBasicAttributes getMultiSiteBasicAttributesInfo(
			List<QuoteToLe> quoteToLeEntity,QuoteIllSite sites,List<QuoteProductComponentsAttributeValue>quoteProdCompAttributeValues, Map<String, Integer> attributesMap) {
		LOGGER.info("Entering getMultiSiteBasicAttributesInfo Method");
		MultiSiteBasicAttributes multiSiteBasicAttributes = new MultiSiteBasicAttributes();
		try {
			multiSiteBasicAttributes.setOrderType(quoteToLeEntity.get(0).getQuoteType());
			Integer interfaceId = attributesMap.get("Interface");
			Integer portBandWidthId = attributesMap.get("Port Bandwidth");
			Integer portTypeId = attributesMap.get("Port Type");
			Integer localLoopBandWidthId = attributesMap.get("Local Loop Bandwidth");
			Integer cpeManagementTypeId = attributesMap.get("CPE Management Type");
			Integer serviceVariantId = attributesMap.get("Service Variant");
			Integer cosOneId = attributesMap.get("cos 1");
			Integer cosTwoId = attributesMap.get("cos 2");
			Integer cosThreeId = attributesMap.get("cos 3");
			Integer cosFourId = attributesMap.get("cos 4");
			Integer cosFiveId = attributesMap.get("cos 5");
			Integer cosSixId = attributesMap.get("cos 6");
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
			for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProdCompAttributeValues) {
				Integer attributeId = quoteProductComponentsAttributeValue.getProductAttributeMaster().getId();
				if(Objects.nonNull(attributeId)) {
		
						if(attributeId==interfaceId) {
							multiSiteBasicAttributes.setInterfaceType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==portBandWidthId) {
							multiSiteBasicAttributes.setPortBandwidth(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==portTypeId) {
							multiSiteBasicAttributes.setPortType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==localLoopBandWidthId) {
							multiSiteBasicAttributes.setLocalLoopBandwidth(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==cpeManagementTypeId) {
							multiSiteBasicAttributes.setCpeManagementType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==serviceVariantId) {
							multiSiteBasicAttributes.setServiceVariant(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==cosOneId) {
							multiSiteBasicAttributes.setCos1(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==cosTwoId) {
							multiSiteBasicAttributes.setCos2(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==cosThreeId) {
							multiSiteBasicAttributes.setCos3(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==cosFourId) {
							multiSiteBasicAttributes.setCos4(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==cosFiveId) {
							multiSiteBasicAttributes.setCos5(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==cosSixId) {
							multiSiteBasicAttributes.setCos6(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==vpnTopologyId) {
							multiSiteBasicAttributes.setVpnTopology(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==siteTypeId) {
							multiSiteBasicAttributes.setSiteType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==resiliencyId) {
							multiSiteBasicAttributes.setResiliency(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==accessRequiredId) {
							multiSiteBasicAttributes.setAccessRequired(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
//						if(attributeId==cpeSupportTypeId) {
//							multiSiteBasicAttributes.setCpeSupportType(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
//						}
						if(attributeId==backUpConfigurationId) {
							multiSiteBasicAttributes.setBackUpConfiguration(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==ipAddressProvidedId) {
							multiSiteBasicAttributes.setIpAddressProvidedBy(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
						}
						if(attributeId==dualCircuitId) {
							multiSiteBasicAttributes.setDualCircuit(Objects.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())? quoteProductComponentsAttributeValue.getAttributeValues() : "");
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
		}catch(Exception e) {
			LOGGER.error("Error occured in getMultiSiteBasicAttributesInfo "+e);
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}	
		LOGGER.info("Exiting getMultiSiteBasicAttributesInfo Method");
		return multiSiteBasicAttributes;
	}
	private List<String> getBasicAttributesList() {
		List<String> basicAttributesList = new ArrayList<String>();
		// basic attributes 
		basicAttributesList.add("Interface" );
		basicAttributesList.add("Port Bandwidth");
		basicAttributesList.add("Port Type");
		basicAttributesList.add("Local Loop Bandwidth");
		basicAttributesList.add("CPE Management Type");
		basicAttributesList.add( "Service Variant");
		basicAttributesList.add("cos 1");
		basicAttributesList.add("cos 2");
		basicAttributesList.add("cos 3");
		basicAttributesList.add("cos 4");
		basicAttributesList.add("cos 5");
		basicAttributesList.add("cos 6");
		basicAttributesList.add("VPN Topology");
		basicAttributesList.add("Site Type");
		basicAttributesList.add("Resiliency");
		basicAttributesList.add("Access Required");
		//basicAttributesMap.put("CPE Support Type", 1); not there in product attribute master
		basicAttributesList.add("Backup Configuration");
		basicAttributesList.add("IP Address Provided By");
		basicAttributesList.add("Dual Circuit");
		// cpe attributes 
		basicAttributesList.add("CPE Basic Chassis");
		basicAttributesList.add("CPE Intl Chassis flag");
		basicAttributesList.add("Cube Licenses");
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
		
		return basicAttributesList;
	}


	private Map<String, Integer> getAttributesMap(List<String> basicAttributesList) {
		Map<String,Integer> attributesMap = new HashMap<String,Integer>();
		List<ProductAttributeMaster> productAttributeMasterList = productAttributeMasterRepository.findByNameIn(basicAttributesList);
		for(ProductAttributeMaster productAttributes: productAttributeMasterList) {	
			attributesMap.put(productAttributes.getName(), productAttributes.getId());	
		}
		return attributesMap;
	}
	
	private void writeSiteInformation(IllGvpnMultiSiteInputs aBook, Row row,int index) {
		
		Cell cell = row.createCell(0);
		CellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
		cell.setCellValue(aBook.getAddressDetailsMultiSite().getSiteId());
		
		cell = row.createCell(1);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getSiteCode())?aBook.getAddressDetailsMultiSite().getSiteCode():"");
		
		cell = row.createCell(2);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getSiteType())?aBook.getAddressDetailsMultiSite().getSiteType():"");

		cell = row.createCell(3);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getAddress())?aBook.getAddressDetailsMultiSite().getAddress():"");
		cellStyle.setLocked(true);
		cell.setCellStyle(cellStyle);
		
		cell = row.createCell(4);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getCountry())?aBook.getAddressDetailsMultiSite().getCountry():"");

		if(aBook.getMacdFlag()) {	
			cell = row.createCell(5);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getServiceId())?aBook.getExistingMacdComponents().getServiceId():"");

			
			cell = row.createCell(6);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getPortBandWidth())?aBook.getExistingMacdComponents().getPortBandWidth():"");
			
			cell = row.createCell(7);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getPortType())?aBook.getExistingMacdComponents().getPortType():"");
			
			cell = row.createCell(8);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getLocalLoopBandwidth())?aBook.getExistingMacdComponents().getLocalLoopBandwidth():"");
			
			cell = row.createCell(9);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getLmProvider())?aBook.getExistingMacdComponents().getLmProvider():""); //LM Provider needs to get

			
			cell = row.createCell(10);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getCpeManagmentType())?aBook.getExistingMacdComponents().getCpeManagmentType():"");
			
			cell = row.createCell(11);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getCpeSupportType())?aBook.getExistingMacdComponents().getCpeSupportType():"");// cpe support type
			
			cell = row.createCell(12);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getCpeModel())?aBook.getExistingMacdComponents().getCpeModel():"");// cpe model
			
			cell = row.createCell(13);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getIpV4AddressPoolSize())?aBook.getExistingMacdComponents().getIpV4AddressPoolSize():"");// ipv4 address pool size
			
			cell = row.createCell(14);
			
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getCurrency())?aBook.getExistingMacdComponents().getCurrency():"");
			
			cell = row.createCell(15);
			
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getExistingArc())?aBook.getExistingMacdComponents().getExistingArc():"");// existing arc
			
			
			cell = row.createCell(16);// existing nrc
			
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getExistingNrc())?aBook.getExistingMacdComponents().getExistingNrc():"");
			
			
			cell = row.createCell(17);
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getContractTerm())?aBook.getExistingMacdComponents().getContractTerm():"");
			
			cell = row.createCell(18);// commission date 
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getCommisionDate())?aBook.getExistingMacdComponents().getCommisionDate():"");
			
			cell = row.createCell(19);// expiry date
			cell.setCellValue(Objects.nonNull(aBook.getExistingMacdComponents().getExpiryDate())?aBook.getExistingMacdComponents().getExpiryDate():"");
		}
		
		cell = row.createCell(20);// product
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getProduct())?aBook.getMultiSiteBasicAttributes().getProduct():"");

		
		cell = row.createCell(21);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getOrderType())?aBook.getMultiSiteBasicAttributes().getOrderType():"");
		
		cell = row.createCell(22);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getInterfaceType())?aBook.getMultiSiteBasicAttributes().getInterfaceType():"");
		
		cell = row.createCell(23);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getPortBandwidth())?aBook.getMultiSiteBasicAttributes().getPortBandwidth():"");
		
		//cell = row.createCell(24);
		//cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getServiceType())?aBook.getMultiSiteBasicAttributes().getServiceType():"");
		
		//defaultly primary - active / Secondary- passive
		LOGGER.info("SITE TYPE::::::"+aBook.getAddressDetailsMultiSite().getSiteType());
		if(Objects.nonNull(aBook.getAddressDetailsMultiSite().getSiteType()) && aBook.getAddressDetailsMultiSite().getSiteType().equalsIgnoreCase("Primary")) {
				cell = row.createCell(24);
				cell.setCellValue("Active");
		}
		if(Objects.nonNull(aBook.getAddressDetailsMultiSite().getSiteType()) && aBook.getAddressDetailsMultiSite().getSiteType().equalsIgnoreCase("Secondary")) {
					cell = row.createCell(24);
					cell.setCellValue("Passive");
		}
		
		cell = row.createCell(25);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getLocalLoopBandwidth())?aBook.getMultiSiteBasicAttributes().getLocalLoopBandwidth():"");
		
		cell = row.createCell(26);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCpeManagementType())?aBook.getMultiSiteBasicAttributes().getCpeManagementType():"");
		
		cell = row.createCell(27);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getServiceVariant())?aBook.getMultiSiteBasicAttributes().getServiceVariant():"");

		cell = row.createCell(28);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getServiceType())?aBook.getMultiSiteBasicAttributes().getServiceType():"");

		cell = row.createCell(29);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCos1())?aBook.getMultiSiteBasicAttributes().getCos1():"");

		cell = row.createCell(30);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCos2())?aBook.getMultiSiteBasicAttributes().getCos2():"");

		cell = row.createCell(31);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCos3())?aBook.getMultiSiteBasicAttributes().getCos3():"");

		cell = row.createCell(32);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCos4())?aBook.getMultiSiteBasicAttributes().getCos4():"");

		cell = row.createCell(33);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCos5())?aBook.getMultiSiteBasicAttributes().getCos5():"");

		cell = row.createCell(34);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCos6())?aBook.getMultiSiteBasicAttributes().getCos6():"");

		cell = row.createCell(35);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getVpnTopology())?aBook.getMultiSiteBasicAttributes().getVpnTopology():"");

//		cell = row.createCell(35);// chargeable distance for npl nde
//		cell.setCellValue("");

		cell = row.createCell(36);
		cell.setCellValue(Objects.nonNull(aBook.getAddressDetailsMultiSite().getSiteType())?aBook.getAddressDetailsMultiSite().getSiteType():"");
		
		cell = row.createCell(37);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getResiliency())?aBook.getMultiSiteBasicAttributes().getResiliency():"");
		
		cell = row.createCell(38);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getAccessRequired())?aBook.getMultiSiteBasicAttributes().getAccessRequired():"");
		
		cell = row.createCell(39);
		
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getCpeSupportType())?aBook.getMultiSiteBasicAttributes().getCpeSupportType():"");
		
		cell = row.createCell(40);
		
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getBackUpConfiguration())?aBook.getMultiSiteBasicAttributes().getBackUpConfiguration():"");
		
		cell = row.createCell(41);
		
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getIpAddressProvidedBy())?aBook.getMultiSiteBasicAttributes().getIpAddressProvidedBy():"");
		
		
		cell = row.createCell(42);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getDualCircuit())?aBook.getMultiSiteBasicAttributes().getDualCircuit():"");
		
		
		cell = row.createCell(43);// advanced attributes up to 51
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getServiceType())?aBook.getMultiSiteBasicAttributes().getServiceType():"");
		
		cell = row.createCell(44);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getBurstableBandwidth())?aBook.getMultiSiteBasicAttributes().getBurstableBandwidth():"");
		
		cell = row.createCell(45);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getIpAddressArrangement())?aBook.getMultiSiteBasicAttributes().getIpAddressArrangement():"");
		
		cell = row.createCell(46);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getIpV4AddressPoolSize())?aBook.getMultiSiteBasicAttributes().getIpV4AddressPoolSize():"");
		
		cell = row.createCell(47);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getIpV6AddressPoolSize())?aBook.getMultiSiteBasicAttributes().getIpV6AddressPoolSize():"");
		
		cell = row.createCell(48);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getSharedLastMile())?aBook.getMultiSiteBasicAttributes().getSharedLastMile():"");
		
		cell = row.createCell(49);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getSharedLastMileServiceId())?aBook.getMultiSiteBasicAttributes().getSharedLastMileServiceId():"");
		
		cell = row.createCell(50);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getSharedCpe())?aBook.getMultiSiteBasicAttributes().getSharedCpe():"");
		
		cell = row.createCell(51);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getSharedCpeServiceId())?aBook.getMultiSiteBasicAttributes().getSharedCpeServiceId():"");
		
		cell = row.createCell(52);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteBasicAttributes().getAddtionalIps())?aBook.getMultiSiteBasicAttributes().getAddtionalIps():"");
		
		cell = row.createCell(53);// f status
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityStatus())?aBook.getMultiSiteFeasibility().getFeasibilityStatus():"");
		
		cell = row.createCell(54);//f type
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getType()) && !aBook.getMultiSiteFeasibility().getType().isEmpty() ?aBook.getMultiSiteFeasibility().getType()+" "+"Feasibility":"");
		
		cell = row.createCell(55);//f validity
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityValidity())?aBook.getMultiSiteFeasibility().getFeasibilityValidity():"");
		
		cell = row.createCell(56);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityCode())?aBook.getMultiSiteFeasibility().getFeasibilityCode():"");

		cell = row.createCell(57);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityMode())?aBook.getMultiSiteFeasibility().getFeasibilityMode():"");
		
		cell = row.createCell(58);// f type
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getType()) && !aBook.getMultiSiteFeasibility().getType().isEmpty()?aBook.getMultiSiteFeasibility().getType()+" "+"Feasibility":"");
		
		cell = row.createCell(59);// type
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getType())?aBook.getMultiSiteFeasibility().getType():"");
		
		cell = row.createCell(60);// provider
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProvider())?aBook.getMultiSiteFeasibility().getProvider():"");
		
		cell = row.createCell(61);// fstatus
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityStatus())?aBook.getMultiSiteFeasibility().getFeasibilityStatus():"");
		
		cell = row.createCell(62);//cd
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getCd())?aBook.getMultiSiteFeasibility().getCd():"");
		
		cell = row.createCell(63);//tcl pop
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getTclPop())?aBook.getMultiSiteFeasibility().getTclPop():"");

		cell = row.createCell(64);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityCheck())?aBook.getMultiSiteFeasibility().getFeasibilityCheck():"");
		
		cell = row.createCell(65);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getConnectedBuilding())?aBook.getMultiSiteFeasibility().getConnectedBuilding():"");//CONNECTED BUILDING
		
		cell = row.createCell(66);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getConnectedCustomer())?aBook.getMultiSiteFeasibility().getConnectedCustomer():"");//CONNECTED CUSTOMER

		cell = row.createCell(67);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOspDistance())?aBook.getMultiSiteFeasibility().getOspDistance():"");//OSP DIST

		cell = row.createCell(68);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOspCapex())?aBook.getMultiSiteFeasibility().getOspCapex():"");//OSP CAPEX
		
		cell = row.createCell(69);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getInBuildingCapex())?aBook.getMultiSiteFeasibility().getInBuildingCapex():"");//IN BUILDING CAPEX 
		
		cell = row.createCell(70);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMuxCost())?aBook.getMultiSiteFeasibility().getMuxCost():"");//MUX COST
		
		cell = row.createCell(71);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProwValueOtc())?aBook.getMultiSiteFeasibility().getProwValueOtc():"");//PROW VALUE (OTC)
		
		cell = row.createCell(72);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getProwValueArc())?aBook.getMultiSiteFeasibility().getProwValueArc():"");//PROW VALUE (ARC)

		cell = row.createCell(73);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMastHeight())?aBook.getMultiSiteFeasibility().getMastHeight():"");//Mast Height (Meter)
		
		cell = row.createCell(74);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getMastNrc())?aBook.getMultiSiteFeasibility().getMastNrc():"");//Mast NRC

		cell = row.createCell(75);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOffnetNrc())?aBook.getMultiSiteFeasibility().getOffnetNrc():"");//Offnet NRC
		
		cell = row.createCell(76);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getOffnetArc())?aBook.getMultiSiteFeasibility().getOffnetArc():"");//Offnet ARC

		cell = row.createCell(77);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getFeasibilityRemarks())?aBook.getMultiSiteFeasibility().getFeasibilityRemarks():"");//F remarks

		cell = row.createCell(78);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteCpeAttributes().getCpeBasicChassis())?aBook.getMultiSiteCpeAttributes().getCpeBasicChassis():"");

		cell = row.createCell(79);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteCpeAttributes().getCpeIntlChassisFlag())?aBook.getMultiSiteCpeAttributes().getCpeIntlChassisFlag():"");

		cell = row.createCell(80);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteCpeAttributes().getCubeLicenses())?aBook.getMultiSiteCpeAttributes().getCubeLicenses():"");
		
		cell = row.createCell(81);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getContractTerm())?aBook.getMultiSitePricingAttributes().getContractTerm():"");
		
		cell = row.createCell(82);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCurrency())?aBook.getMultiSitePricingAttributes().getCurrency():"");

		cell = row.createCell(83);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortNrcListPrice())?aBook.getMultiSitePricingAttributes().getPortNrcListPrice():0.0);// port nrc list price
		
		cell = row.createCell(85);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortNrcSalePrice())?aBook.getMultiSitePricingAttributes().getPortNrcSalePrice():0.0);
		
		cell = row.createCell(84);
		String nrcDiscountFormula = "IFERROR(1-"+ row.getCell(85).getAddress().formatAsString() + "/" + row.getCell(83).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(nrcDiscountFormula);
		//df.format(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortNrcDiscount())?aBook.getMultiSitePricingAttributes().getPortNrcDiscount():0.0)
		Double portNrcDiscountValue = Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortNrcDiscount())?aBook.getMultiSitePricingAttributes().getPortNrcDiscount():0.0;
		cell.setCellValue(portNrcDiscountValue);
		
		cell = row.createCell(86);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortArcListPrice())?aBook.getMultiSitePricingAttributes().getPortArcListPrice():0.0);
		
		cell = row.createCell(88);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortArcSalePrice())?aBook.getMultiSitePricingAttributes().getPortArcSalePrice():0.0);
		
		cell = row.createCell(87);
		String arcDiscountFormula = "IFERROR(1-"+ row.getCell(88).getAddress().formatAsString() + "/" + row.getCell(86).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(arcDiscountFormula);
		Double portArcDiscountValue = Objects.nonNull(aBook.getMultiSitePricingAttributes().getPortArcDiscount())?aBook.getMultiSitePricingAttributes().getPortArcDiscount():0.0;
		cell.setCellValue(portArcDiscountValue);
		
		
		cell = row.createCell(89);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileNrcListPrice())?aBook.getMultiSitePricingAttributes().getLastMileNrcListPrice():0.0);//last mile nrc list price
		
		cell = row.createCell(91);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileNrcSalePrice())?aBook.getMultiSitePricingAttributes().getLastMileNrcSalePrice():0.0);
		
		cell = row.createCell(90);
		String lastMileNrcDiscountFormula = "IFERROR(1-"+ row.getCell(91).getAddress().formatAsString() + "/" + row.getCell(89).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(lastMileNrcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileNrcDiscount())?aBook.getMultiSitePricingAttributes().getLastMileNrcDiscount():0.0);
		
		cell = row.createCell(92);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileArcListPrice())?aBook.getMultiSitePricingAttributes().getLastMileArcListPrice():0.0);// last mile arc list price

		cell = row.createCell(94);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileArcSalePrice())?aBook.getMultiSitePricingAttributes().getLastMileArcSalePrice():0.0);
		
		cell = row.createCell(93);
		String lastMileArcDiscountFormula = "IFERROR(1-"+ row.getCell(94).getAddress().formatAsString() + "/" + row.getCell(92).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(lastMileArcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getLastMileArcDiscount())?aBook.getMultiSitePricingAttributes().getLastMileArcDiscount():0.0);
		
//		cell = row.createCell(95);
//		cell.setCellValue("");//link nrc
//		
//		cell = row.createCell(96);
//		cell.setCellValue("");//link arc
		
		cell = row.createCell(95);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getMastNrcListPrice())?aBook.getMultiSitePricingAttributes().getMastNrcListPrice():0.0);//mast nrc list price
		
		cell = row.createCell(97);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getMastNrcSalesPrice())?aBook.getMultiSitePricingAttributes().getMastNrcSalesPrice():0.0);
		
		cell = row.createCell(96);
		String mastNrcDiscountFormula = "IFERROR(1-"+ row.getCell(97).getAddress().formatAsString() + "/" + row.getCell(95).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(mastNrcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getMastNrcDiscountPrice())?aBook.getMultiSitePricingAttributes().getMastNrcDiscountPrice():0.0);
		
		cell = row.createCell(98);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getAdditionalIpArcListPrice())?aBook.getMultiSitePricingAttributes().getAdditionalIpArcListPrice() : 0.0);// additional Ip Arc list price
		
		cell = row.createCell(100);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getAdditionalIpArcSalePrice())?aBook.getMultiSitePricingAttributes().getAdditionalIpArcSalePrice():0.0);
		
		cell = row.createCell(99);
		String additionalIpArcDiscountFormula = "IFERROR(1-"+ row.getCell(100).getAddress().formatAsString() + "/" + row.getCell(98).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(additionalIpArcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getAdditionalIpArcDiscount())?aBook.getMultiSitePricingAttributes().getAdditionalIpArcDiscount() : 0.0);
		
		cell = row.createCell(101);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getBurstableArcListPrice())?aBook.getMultiSitePricingAttributes().getBurstableArcListPrice():0.0);//burstable arc list price
		
		cell = row.createCell(103);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getBurstableArcSalePrice())?aBook.getMultiSitePricingAttributes().getBurstableArcSalePrice():0.0);
		
		cell = row.createCell(102);
		String burstableArcDiscountFormula = "IFERROR(1-"+ row.getCell(103).getAddress().formatAsString() + "/" + row.getCell(101).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(burstableArcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getBurstableArcDiscountPrice())?aBook.getMultiSitePricingAttributes().getBurstableArcDiscountPrice():0.0);
		
		cell = row.createCell(104);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeInstallNrcListPrice())?aBook.getMultiSitePricingAttributes().getCpeInstallNrcListPrice():0.0);// cpe install nrc list price
		
		cell = row.createCell(106);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeInstallNRCSalePrice())?aBook.getMultiSitePricingAttributes().getCpeInstallNRCSalePrice():0.0);
		
		cell = row.createCell(105);
		String cpeInstallNrcDiscountFormula = "IFERROR(1-"+ row.getCell(106).getAddress().formatAsString() + "/" + row.getCell(104).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(cpeInstallNrcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeInstallNrcDiscount())?aBook.getMultiSitePricingAttributes().getCpeInstallNrcDiscount():0.0);
		
		cell = row.createCell(107);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeOutrightSaleNRCListPrice())?aBook.getMultiSitePricingAttributes().getCpeOutrightSaleNRCListPrice():0.0); // cpe outright sale nrc list price
		
		cell = row.createCell(109);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeOutrightSaleNrcSalePrice())?aBook.getMultiSitePricingAttributes().getCpeOutrightSaleNrcSalePrice():0.0);
		
		cell = row.createCell(108);
		String cpeOutrightSaleNrcDiscountFormula = "IFERROR(1-"+ row.getCell(109).getAddress().formatAsString() + "/" + row.getCell(107).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(cpeOutrightSaleNrcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeOutrightSaleNrcDiscount())?aBook.getMultiSitePricingAttributes().getCpeOutrightSaleNrcDiscount():0.0);
		
		cell = row.createCell(110);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeRentalArcListPrice())?aBook.getMultiSitePricingAttributes().getCpeRentalArcListPrice():0.0);// cpe rental list price
		
		cell = row.createCell(112);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeRentalArcSalePrice())?aBook.getMultiSitePricingAttributes().getCpeRentalArcSalePrice():0.0);
		
		cell = row.createCell(111);
		String cpeRentalArcDiscountFormula = "IFERROR(1-"+ row.getCell(112).getAddress().formatAsString() + "/" + row.getCell(110).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(cpeRentalArcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeRentalArcDiscount())?aBook.getMultiSitePricingAttributes().getCpeRentalArcDiscount():0.0);
		
		cell = row.createCell(113);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeManagementArcListPrice())?aBook.getMultiSitePricingAttributes().getCpeManagementArcListPrice():0.0);// cpe mngmt arc list price
		
		cell = row.createCell(115);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeManagementArcSalePrice())?aBook.getMultiSitePricingAttributes().getCpeManagementArcSalePrice():0.0);
		
		cell = row.createCell(114);
		String cpeManagementArcDiscountFormula = "IFERROR(1-"+ row.getCell(115).getAddress().formatAsString() + "/" + row.getCell(113).getAddress().formatAsString() + ",\"\")";
		cell.setCellFormula(cpeManagementArcDiscountFormula);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCpeManagementArcDiscount())?aBook.getMultiSitePricingAttributes().getCpeManagementArcDiscount():0.0);

		cell = row.createCell(116);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getTotalNrcSalePrice())?aBook.getMultiSitePricingAttributes().getTotalNrcSalePrice():0.0);// total nrc sale price 
		
		cell = row.createCell(117);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getTotalArcSalePrice())?aBook.getMultiSitePricingAttributes().getTotalArcSalePrice():0.0);// total arc sale price
		

		
		cell = row.createCell(118);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSiteFeasibility().getSiteLevelActions())?aBook.getMultiSiteFeasibility().getSiteLevelActions():"Approve");// site level action 
		
		cell = row.createCell(119);
		cell.setCellValue("");// commercial manager comments

		cell = row.createCell(120);
		cell.setCellValue(Objects.nonNull(aBook.getMultiSitePricingAttributes().getCommercialManagerComments())?aBook.getMultiSitePricingAttributes().getCommercialManagerComments():" ");// commercial manager comments 
		 
	}


  @SuppressWarnings("resource")
  public CommercialBulkProcessSites uploadIllGvpnMultiSiteExcel(MultipartFile file,String approvalLevel,Integer attachmentIdValue,String quoteCode,Integer taskId) throws IOException, TclCommonException {
	  Quote quoteEntity = quoteRepository.findByQuoteCode(quoteCode);
	  List<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findByQuote(quoteEntity);
	  Integer quoteId = quoteEntity.getId();
	  QuoteToLe quoteTole = quoteToLeEntity.get(0);
	  Integer attachmentId = attachmentIdValue;
      byte[] fileContent = file.getBytes();
      CommercialBulkProcessSites commercialBulkProcessSites = new CommercialBulkProcessSites();
      LOGGER.info("Inside uploadexcelfile::IIIQuoteService class");
      ByteArrayInputStream inputStream = null;
      List<MultiSiteResponseJsonAttributes> multiSiteResponseJsonAttributesList = new ArrayList<>();
      try {
          if (Objects.nonNull(fileContent) && fileContent.length > 0) {
              LOGGER.info("File read starts...");
              inputStream = new ByteArrayInputStream(fileContent);
              Workbook workbook = null;
              workbook = new XSSFWorkbook(inputStream);
              Map<String,Integer> columnMap = new HashMap<String,Integer>();
              List<Integer> illSitesList = new ArrayList();
              if (workbook != null) {
                  int sheetCount = workbook.getNumberOfSheets();
                  for (int sheetNo = 0; sheetNo < sheetCount; sheetNo++) {
                      Sheet sheet = workbook.getSheetAt(sheetNo);
                      
                      int rowSize = sheet.getLastRowNum()+1;
                      // as the excel sheet row starts from 0
                      LOGGER.info("row size "+rowSize);
                      
                      columnMap = getIllGvpnColumnMappingDetails(sheet);
                     
                      for(int index=2;index<rowSize;index++) {
                    	  MultiSiteResponseJsonAttributes multiSiteResponseJsonAttribute = new MultiSiteResponseJsonAttributes();
                    	  
                    	  Row dataRow = sheet.getRow(index);
                    	  if(Objects.nonNull(dataRow)) {
                    	  Cell siteIdCell = dataRow.getCell(columnMap.get("Site Id"));
                    	  if(Objects.nonNull(siteIdCell)) {
                    		  if(siteIdCell.getCellType()==0) {
                        		  Integer siteId = (int) (siteIdCell.getNumericCellValue());
                        		  multiSiteResponseJsonAttribute.setSiteId(siteId);
                        	  }
                        	  
                        	  if(siteIdCell.getCellType()==1 || siteIdCell.getCellType()==3) {
                        		  Integer siteId = Integer.parseInt("");
                        		  multiSiteResponseJsonAttribute.setSiteId(siteId);
                        	  }
                    	  }else {
                    		  Integer siteId = Integer.parseInt("");
                    		  multiSiteResponseJsonAttribute.setSiteId(siteId);
                    	  }
                    	 
                          Cell siteTypeCell = dataRow.getCell(columnMap.get("Site Type"));
                          if(Objects.nonNull(siteTypeCell)) {
                        	  String siteType = (Objects.nonNull(siteTypeCell.getStringCellValue())?siteTypeCell.getStringCellValue():"");
                        	  multiSiteResponseJsonAttribute.setPriSecInfo(siteType);
                          }else {
                        	  String siteType = (Objects.nonNull(siteTypeCell.getStringCellValue())?siteTypeCell.getStringCellValue():"");
                        	  multiSiteResponseJsonAttribute.setPriSecInfo("");
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
                          
                          Cell mastNRCDiscountPriceCell = dataRow.getCell(columnMap.get("Mast NRC Discount Price"));
                          if(Objects.nonNull(mastNRCDiscountPriceCell)) {
                        	  if(mastNRCDiscountPriceCell.getCellType()==0) {
                            	  Double mastNRCDiscountPrice = mastNRCDiscountPriceCell.getNumericCellValue();
                                  multiSiteResponseJsonAttribute.setMastNrcDiscountInPercentage(mastNRCDiscountPrice);
                              }
                              
                              if(mastNRCDiscountPriceCell.getCellType()==1 || mastNRCDiscountPriceCell.getCellType()==3) {
                            	  Double mastNRCDiscountPrice = 0.0;
                                  multiSiteResponseJsonAttribute.setMastNrcDiscountInPercentage(mastNRCDiscountPrice);
                              }
                              if(mastNRCDiscountPriceCell.getCellType()==2) {
                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            	  CellReference cellReference = new CellReference(mastNRCDiscountPriceCell.getAddress().formatAsString());
                            	  Row row = sheet.getRow(cellReference.getRow());
                                  Cell cell = row.getCell(cellReference.getCol());
                                  CellValue cellValue = evaluator.evaluate(cell);
                                  multiSiteResponseJsonAttribute.setMastNrcDiscountInPercentage(cellValue.getNumberValue());
                              }
                          }else {
                        	  Double mastNRCDiscountPrice = 0.0;
                              multiSiteResponseJsonAttribute.setMastNrcDiscountInPercentage(mastNRCDiscountPrice);
                          }
                          
                                            
                          Cell mastNRCSalePriceCell = dataRow.getCell(columnMap.get("Mast NRC Sale Price"));
                          if(Objects.nonNull(mastNRCSalePriceCell)) {
                        	  if(mastNRCSalePriceCell.getCellType()==0) {
                            	  Double mastNRCSalePrice = mastNRCSalePriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setMastNrcSalePrice(mastNRCSalePrice);
                              }
                              
                              if(mastNRCSalePriceCell.getCellType()==1 || mastNRCSalePriceCell.getCellType()==3) {
                            	  Double mastNRCSalePrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setMastNrcSalePrice(mastNRCSalePrice);
                              }
                          }else {
                        	  Double mastNRCSalePrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setMastNrcSalePrice(mastNRCSalePrice);
                          }
                          
                          Cell mastNRCListPriceCell = dataRow.getCell(columnMap.get("Mast NRC List Price"));
                          if(Objects.nonNull(mastNRCListPriceCell)) {
                        	  if(mastNRCListPriceCell.getCellType()==0) {
                            	  Double mastNRCListPrice = mastNRCListPriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setMastNrcListPrice(mastNRCListPrice);
                              }
                              
                              if(mastNRCListPriceCell.getCellType()==1 || mastNRCListPriceCell.getCellType()==3) {
                            	  Double mastNRCListPrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setMastNrcListPrice(mastNRCListPrice);
                              }
                          }else {
                        	  Double mastNRCListPrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setMastNrcListPrice(mastNRCListPrice);
                          }
                          
                          
                          Cell additionalIPARCDiscountCell = dataRow.getCell(columnMap.get("Additional IP ARC Discount"));
                          if(Objects.nonNull(additionalIPARCDiscountCell)) {
                        	  if(additionalIPARCDiscountCell.getCellType()==0) {
                        		  Double additionalIPARCDiscount = additionalIPARCDiscountCell.getNumericCellValue();
                        		  multiSiteResponseJsonAttribute.setAdditionalIpArcDiscountInPercentage(additionalIPARCDiscount);
                        	  }
                        	  
                        	  if(additionalIPARCDiscountCell.getCellType()==1 || additionalIPARCDiscountCell.getCellType()==3) {
                        		  Double additionalIPARCDiscount = 0.0;
                        		  multiSiteResponseJsonAttribute.setAdditionalIpArcDiscountInPercentage(additionalIPARCDiscount);
                        	  }
                        	  if(additionalIPARCDiscountCell.getCellType()==2) {
                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            	  CellReference cellReference = new CellReference(additionalIPARCDiscountCell.getAddress().formatAsString());
                            	  Row row = sheet.getRow(cellReference.getRow());
                                  Cell cell = row.getCell(cellReference.getCol());
                                  CellValue cellValue = evaluator.evaluate(cell);
                                  multiSiteResponseJsonAttribute.setAdditionalIpArcDiscountInPercentage(cellValue.getNumberValue());
                              }
                          }else {
                        	  Double additionalIPARCDiscount = 0.0;
                    		  multiSiteResponseJsonAttribute.setAdditionalIpArcDiscountInPercentage(additionalIPARCDiscount);
                          }
                    	  
                          Cell additionalIPARCSalePriceCell = dataRow.getCell(columnMap.get("Additional IP ARC Sale Price"));
                          if(Objects.nonNull(additionalIPARCSalePriceCell)) {
                        	  if(additionalIPARCSalePriceCell.getCellType()==0) {
                            	  Double additionalIPARCSalePrice = additionalIPARCSalePriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setAdditionalIpArcSalePrice(additionalIPARCSalePrice);
                              }
                              
                              if(additionalIPARCSalePriceCell.getCellType()==1 || additionalIPARCSalePriceCell.getCellType()==3) {
                            	  Double additionalIPARCSalePrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setAdditionalIpArcSalePrice(additionalIPARCSalePrice);
                              }
                          }else {
                        	  Double additionalIPARCSalePrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setAdditionalIpArcSalePrice(additionalIPARCSalePrice);
                          }
                          
                          Cell additionalIPARCListPriceCell = dataRow.getCell(columnMap.get("Additional IP ARC List Price"));
                          if(Objects.nonNull(additionalIPARCListPriceCell)) {
                        	  if(additionalIPARCListPriceCell.getCellType()==0) {
                            	  Double additionalIPARCListPrice = additionalIPARCListPriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setAdditionalIpArcListPrice(additionalIPARCListPrice);
                              }
                              
                              if(additionalIPARCListPriceCell.getCellType()==1 || additionalIPARCListPriceCell.getCellType()==3) {
                            	  Double additionalIPARCListPrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setAdditionalIpArcListPrice(additionalIPARCListPrice);
                              }
                          }else {
                        	  Double additionalIPARCListPrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setAdditionalIpArcListPrice(additionalIPARCListPrice);
                          }
                          
                         
                          Cell burstableARCDiscountPriceCell = dataRow.getCell(columnMap.get("Burstable ARC Discount Price"));
                          if(Objects.nonNull(burstableARCDiscountPriceCell)) {
                        	  if(burstableARCDiscountPriceCell.getCellType()==0) {
                            	  Double burstableARCDiscountPrice = burstableARCDiscountPriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setBurstableArcDiscountInPercentage(burstableARCDiscountPrice);
                              }
                             
                              if(burstableARCDiscountPriceCell.getCellType()==3 || burstableARCDiscountPriceCell.getCellType()==1) {
                            	  Double burstableARCDiscountPrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setBurstableArcDiscountInPercentage(burstableARCDiscountPrice);
                              }
                              if(burstableARCDiscountPriceCell.getCellType()==2) {
                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            	  CellReference cellReference = new CellReference(burstableARCDiscountPriceCell.getAddress().formatAsString());
                            	  Row row = sheet.getRow(cellReference.getRow());
                                  Cell cell = row.getCell(cellReference.getCol());
                                  CellValue cellValue = evaluator.evaluate(cell);
                                  multiSiteResponseJsonAttribute.setBurstableArcDiscountInPercentage(cellValue.getNumberValue());
                              }
                          }else {
                        	  Double burstableARCDiscountPrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setBurstableArcDiscountInPercentage(burstableARCDiscountPrice);
                          }
                          
                         
                          Cell burstableARCSalePriceCell = dataRow.getCell(columnMap.get("Burstable ARC Sale Price"));
                          if(Objects.nonNull(burstableARCSalePriceCell)) {
                        	  if(burstableARCSalePriceCell.getCellType()==0) {
                            	  Double burstableARCSalePrice = burstableARCSalePriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setBurstableArcSalePrice(burstableARCSalePrice);
                              }
                              
                              if(burstableARCSalePriceCell.getCellType()==1 || burstableARCSalePriceCell.getCellType()==3) {
                            	  Double burstableARCSalePrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setBurstableArcSalePrice(burstableARCSalePrice);
                              }
                          }else {
                        	  Double burstableARCSalePrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setBurstableArcSalePrice(burstableARCSalePrice);
                          }
                          
                          Cell burstableARCListPriceCell = dataRow.getCell(columnMap.get("Burstable ARC List Price"));
                          if(Objects.nonNull(burstableARCListPriceCell)) {
                        	  if(burstableARCListPriceCell.getCellType()==0) {
                            	  Double burstableARCListPrice = burstableARCListPriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setBurstableArcListPrice(burstableARCListPrice);
                              }
                              
                              if(burstableARCListPriceCell.getCellType()==1 || burstableARCListPriceCell.getCellType()==3) {
                            	  Double burstableARCListPrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setBurstableArcListPrice(burstableARCListPrice);
                              }
                          }else {
                        	  Double burstableARCListPrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setBurstableArcListPrice(burstableARCListPrice);
                          }
                          
                         
                          Cell cPEInstallNRCDisocuntCell = dataRow.getCell(columnMap.get("CPE Install NRC Discount"));
                          if(Objects.nonNull(cPEInstallNRCDisocuntCell)) {
                        	  if(cPEInstallNRCDisocuntCell.getCellType()==0) {
                            	  Double cPEInstallNRCDisocunt = cPEInstallNRCDisocuntCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeInstallNrcDisocuntInPercentage(cPEInstallNRCDisocunt);
                              }
                              
                              if(cPEInstallNRCDisocuntCell.getCellType()==1 || cPEInstallNRCDisocuntCell.getCellType()==3) {
                            	  Double cPEInstallNRCDisocunt = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeInstallNrcDisocuntInPercentage(cPEInstallNRCDisocunt);
                              }
                              if(cPEInstallNRCDisocuntCell.getCellType()==2) {
                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            	  CellReference cellReference = new CellReference(cPEInstallNRCDisocuntCell.getAddress().formatAsString());
                            	  Row row = sheet.getRow(cellReference.getRow());
                                  Cell cell = row.getCell(cellReference.getCol());
                                  CellValue cellValue = evaluator.evaluate(cell);
                                  multiSiteResponseJsonAttribute.setCpeInstallNrcDisocuntInPercentage(cellValue.getNumberValue());
                              }
                          }else {
                        	  Double cPEInstallNRCDisocunt = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeInstallNrcDisocuntInPercentage(cPEInstallNRCDisocunt);
                          }
                          
                         
                          Cell cPEInstallNRCSalePriceCell = dataRow.getCell(columnMap.get("CPE Install NRC Sale Price"));
                          if(Objects.nonNull(cPEInstallNRCSalePriceCell)) {
                        	  if(cPEInstallNRCSalePriceCell.getCellType()==0) {
                            	  Double cPEInstallNRCSalePrice = cPEInstallNRCSalePriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeInstallNrcSalePrice(cPEInstallNRCSalePrice);
                              }
                              if(cPEInstallNRCSalePriceCell.getCellType()==1 || cPEInstallNRCSalePriceCell.getCellType()==3) {
                            	  Double cPEInstallNRCSalePrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeInstallNrcSalePrice(cPEInstallNRCSalePrice);
                              }
                          }else {
                        	  Double cPEInstallNRCSalePrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeInstallNrcSalePrice(cPEInstallNRCSalePrice);
                          }
                          
                          Cell cPEInstallNRCListPriceCell = dataRow.getCell(columnMap.get("CPE Install NRC List Price"));
                          if(Objects.nonNull(cPEInstallNRCListPriceCell)) {
                        	  if(cPEInstallNRCListPriceCell.getCellType()==0) {
                            	  Double cPEInstallNRCListPrice = cPEInstallNRCListPriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeInstallNrcListPrice(cPEInstallNRCListPrice);
                              }
                              if(cPEInstallNRCListPriceCell.getCellType()==1 || cPEInstallNRCListPriceCell.getCellType()==3) {
                            	  Double cPEInstallNRCListPrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeInstallNrcListPrice(cPEInstallNRCListPrice);
                              }
                          }else {
                        	  Double cPEInstallNRCListPrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeInstallNrcListPrice(cPEInstallNRCListPrice);
                          }
                          
                         
                          Cell cPEOutrightSaleNRCDiscountCell = dataRow.getCell(columnMap.get("CPE Outright Sale NRC Discount"));
                          if(Objects.nonNull(cPEOutrightSaleNRCDiscountCell)) {
                        	  if(cPEOutrightSaleNRCDiscountCell.getCellType()==0) {
                                  Double cPEOutrightSaleNRCDiscount = cPEOutrightSaleNRCDiscountCell.getNumericCellValue();
                                  multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcDiscount(cPEOutrightSaleNRCDiscount);
                                  }
                                 
                                  if(cPEOutrightSaleNRCDiscountCell.getCellType()==1 || cPEOutrightSaleNRCDiscountCell.getCellType()==3) {
                                      Double cPEOutrightSaleNRCDiscount = 0.0;
                                      multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcDiscount(cPEOutrightSaleNRCDiscount);
                                   }
                                  if(cPEOutrightSaleNRCDiscountCell.getCellType()==2) {
                                	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                                	  CellReference cellReference = new CellReference(cPEOutrightSaleNRCDiscountCell.getAddress().formatAsString());
                                	  Row row = sheet.getRow(cellReference.getRow());
                                      Cell cell = row.getCell(cellReference.getCol());
                                      CellValue cellValue = evaluator.evaluate(cell);
                                      multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcDiscount(cellValue.getNumberValue());
                                  }
                          }else {
                        	  Double cPEOutrightSaleNRCDiscount = 0.0;
                              multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcDiscount(cPEOutrightSaleNRCDiscount);
                          }
                          
                          
                          Cell cPEOutrightSaleNRCSalePriceCell = dataRow.getCell(columnMap.get("CPE Outright Sale NRC Sale Price"));
                          if(Objects.nonNull(cPEOutrightSaleNRCSalePriceCell)) {
                        	  if(cPEOutrightSaleNRCSalePriceCell.getCellType()==0) {
                            	  Double cPEOutrightSaleNRCSalePrice = cPEOutrightSaleNRCSalePriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcSalePrice(cPEOutrightSaleNRCSalePrice);
                              }
                    
                              if(cPEOutrightSaleNRCSalePriceCell.getCellType()==1 || cPEOutrightSaleNRCSalePriceCell.getCellType()==3) {
                            	  Double cPEOutrightSaleNRCSalePrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcSalePrice(cPEOutrightSaleNRCSalePrice);
                              }
                          }else {
                        	  Double cPEOutrightSaleNRCSalePrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcSalePrice(cPEOutrightSaleNRCSalePrice);
                          }
                          
                          Cell cPEOutrightSaleNRCListPriceCell = dataRow.getCell(columnMap.get("CPE Outright Sale NRC List Price"));
                          if(Objects.nonNull(cPEOutrightSaleNRCListPriceCell)) {
                        	  if(cPEOutrightSaleNRCListPriceCell.getCellType()==0) {
                            	  Double cPEOutrightSaleNRCListPrice = cPEOutrightSaleNRCListPriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcListPrice(cPEOutrightSaleNRCListPrice);
                              }
                    
                              if(cPEOutrightSaleNRCListPriceCell.getCellType()==1 || cPEOutrightSaleNRCListPriceCell.getCellType()==3) {
                            	  Double cPEOutrightSaleNRCListPrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcListPrice(cPEOutrightSaleNRCListPrice);
                              }
                          }else {
                        	  Double cPEOutrightSaleNRCListPrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeOutrightSaleNrcListPrice(cPEOutrightSaleNRCListPrice);
                          }
                          
                          Cell cPERentalARCDiscountCell = dataRow.getCell(columnMap.get("CPE Rental ARC Discount"));
                          if(Objects.nonNull(cPERentalARCDiscountCell)) {
                        	  if(cPERentalARCDiscountCell.getCellType()==0) {
                            	  Double cPERentalARCDiscount = cPERentalARCDiscountCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeRentalArcDiscountInPercentage(cPERentalARCDiscount);
                              }
                              
                              if(cPERentalARCDiscountCell.getCellType()==1 || cPERentalARCDiscountCell.getCellType()==3) {
                            	  Double cPERentalARCDiscount = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeRentalArcDiscountInPercentage(cPERentalARCDiscount);
                              }
                              if(cPERentalARCDiscountCell.getCellType()==2) {
                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            	  CellReference cellReference = new CellReference(cPERentalARCDiscountCell.getAddress().formatAsString());
                            	  Row row = sheet.getRow(cellReference.getRow());
                                  Cell cell = row.getCell(cellReference.getCol());
                                  CellValue cellValue = evaluator.evaluate(cell);
                                  multiSiteResponseJsonAttribute.setCpeRentalArcDiscountInPercentage(cellValue.getNumberValue());
                              }
                          }
                          
                          
                          Cell cPERentalARCSalePriceCell = dataRow.getCell(columnMap.get("CPE Rental ARC Sale Price"));
                          if(Objects.nonNull(cPERentalARCSalePriceCell)) {
                        	  if(cPERentalARCSalePriceCell.getCellType()==0) {
                            	  Double cPERentalARCSalePrice = cPERentalARCSalePriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeRentalArcSalePrice(cPERentalARCSalePrice);
                              }
                            
                              if(cPERentalARCSalePriceCell.getCellType()==1 || cPERentalARCSalePriceCell.getCellType()==3) {
                            	  Double cPERentalARCSalePrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeRentalArcSalePrice(cPERentalARCSalePrice);
                              }
                          }else {
                        	  Double cPERentalARCSalePrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeRentalArcSalePrice(cPERentalARCSalePrice);
                          }
                          
                          Cell cPERentalARCListPriceCell = dataRow.getCell(columnMap.get("CPE Rental ARC List Price"));
                          if(Objects.nonNull(cPERentalARCListPriceCell)) {
                        	  if(cPERentalARCListPriceCell.getCellType()==0) {
                            	  Double cPERentalARCListPrice = cPERentalARCListPriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeRentalArcListPrice(cPERentalARCListPrice);
                              }
                            
                              if(cPERentalARCListPriceCell.getCellType()==1 || cPERentalARCListPriceCell.getCellType()==3) {
                            	  Double cPERentalARCListPrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeRentalArcListPrice(cPERentalARCListPrice);
                              }
                          }else {
                        	  Double cPERentalARCListPrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeRentalArcListPrice(cPERentalARCListPrice);
                          }
                          
                          Cell cPEManagementARCDiscountCell = dataRow.getCell(columnMap.get("CPE Management ARC Discount"));
                          if(Objects.nonNull(cPEManagementARCDiscountCell)) {
                        	  if(cPEManagementARCDiscountCell.getCellType()==0) {
                            	  Double cPEManagementARCDiscount = cPEManagementARCDiscountCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeManagementArcDiscountInPercentage(cPEManagementARCDiscount);
                              }
                           
                              if(cPEManagementARCDiscountCell.getCellType()==1 || cPEManagementARCDiscountCell.getCellType()==3) {
                            	  Double cPEManagementARCDiscount = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeManagementArcDiscountInPercentage(cPEManagementARCDiscount);
                              }
                              if(cPEManagementARCDiscountCell.getCellType()==2) {
                            	  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            	  CellReference cellReference = new CellReference(cPEManagementARCDiscountCell.getAddress().formatAsString());
                            	  Row row = sheet.getRow(cellReference.getRow());
                                  Cell cell = row.getCell(cellReference.getCol());
                                  CellValue cellValue = evaluator.evaluate(cell);
                                  multiSiteResponseJsonAttribute.setCpeManagementArcDiscountInPercentage(cellValue.getNumberValue());
                              }
                          }else {
                        	  Double cPEManagementARCDiscount = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeManagementArcDiscountInPercentage(cPEManagementARCDiscount);
                          }
                          

                          Cell cPEManagementARCSalePriceCell = dataRow.getCell(columnMap.get("CPE Management ARC Sale Price"));
                          if(Objects.nonNull(cPEManagementARCSalePriceCell)) {
                        	  if(cPEManagementARCSalePriceCell.getCellType()==0) {
                            	  Double cPEManagementARCSalePrice = cPEManagementARCSalePriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeManagementArcSalePrice(cPEManagementARCSalePrice);
                              }
                             
                              if(cPEManagementARCSalePriceCell.getCellType()==1 || cPEManagementARCSalePriceCell.getCellType()==3) {
                            	  Double cPEManagementARCSalePrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeManagementArcSalePrice(cPEManagementARCSalePrice);
                              }
                          }else {
                        	  Double cPEManagementARCSalePrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeManagementArcSalePrice(cPEManagementARCSalePrice);
                          }
                          
                          Cell cPEManagementARCListPriceCell = dataRow.getCell(columnMap.get("CPE Management ARC List Price"));
                          if(Objects.nonNull(cPEManagementARCListPriceCell)) {
                        	  if(cPEManagementARCListPriceCell.getCellType()==0) {
                            	  Double cPEManagementARCListPrice = cPEManagementARCListPriceCell.getNumericCellValue();
                            	  multiSiteResponseJsonAttribute.setCpeManagementArcListPrice(cPEManagementARCListPrice);
                              }
                             
                              if(cPEManagementARCListPriceCell.getCellType()==1 || cPEManagementARCListPriceCell.getCellType()==3) {
                            	  Double cPEManagementARCListPrice = 0.0;
                            	  multiSiteResponseJsonAttribute.setCpeManagementArcListPrice(cPEManagementARCListPrice);
                              }
                          }else {
                        	  Double cPEManagementARCListPrice = 0.0;
                        	  multiSiteResponseJsonAttribute.setCpeManagementArcListPrice(cPEManagementARCListPrice);
                          }

                          multiSiteResponseJsonAttribute.setApprovalLevel(Objects.nonNull(approvalLevel)?approvalLevel:"");// from UI

                          multiSiteResponseJsonAttribute.setQuoteId(quoteId);// from UI

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
                          }
                          


                          Cell commercialManagerCommentsCell = dataRow.getCell(columnMap.get("Commercial Manager Comments"));
							if(Objects.nonNull(commercialManagerCommentsCell)) {
								if(commercialManagerCommentsCell.getCellType()==1) {
									String commercialManagerComments = commercialManagerCommentsCell.getStringCellValue();
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

                          multiSiteResponseJsonAttributesList.add(multiSiteResponseJsonAttribute);


                          illSitesList.add(multiSiteResponseJsonAttribute.getSiteId());

                          //reset bulkupload status to zero for illsite each upload
                          Optional<QuoteIllSite> site = illSiteRepository.findById(multiSiteResponseJsonAttribute.getSiteId());
                          if (site.isPresent()) {
                        	  site.get().setSiteBulkUpdate("0");
                        	  site.get().setCommercialApproveStatus("0");
                        	  site.get().setCommercialRejectionStatus("0");
                        	  illSiteRepository.save(site.get());
                          }
                      }
                      }
                  } 
              }
              
              // we have to write a method for updating quote to le table column 
              processCommercialBulkSites(quoteId,quoteCode,illSitesList,multiSiteResponseJsonAttributesList,attachmentId, taskId);
              
              //bulk status updation in quotetole
              quoteTole.setQuoteBulkUpdate("1");
              quoteToLeRepository.save(quoteTole);
              
          } else {
              LOGGER.info("Exception occured in uploadIllGvpnMultiSiteExcel :: fileContent length is zero"
                      + fileContent.length);
              throw new TclCommonException(ExceptionConstants.FILE_EMPTY, ResponseResource.R_CODE_ERROR);
          }
      }
//      catch(NullPointerException ex) {
//    	  LOGGER.error("Exception Occured as mandatory fields are missing : {}" + ex.getMessage());
//    	  throw new TclCommonException(ExceptionConstants.EXCEL_MANDATORY_SITE_LEVEL_ACTION_OR_COMMERCIAL_MANAGER_COMMENTS_EMPTY, ResponseResource.R_CODE_ERROR);
//      }
      catch (Exception e) {
          LOGGER.error("Exception Occured while IIIQuoteService : {}" + e.getMessage(),e);
          throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
      }
      return commercialBulkProcessSites;
  }

  private Map<String, Integer> getIllGvpnColumnMappingDetails(Sheet sheet) {
	  Map<String,Integer> columnMap = new HashMap<String,Integer>();
	  Iterator iterator = sheet.iterator();
	// TODO Auto-generated method stub
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

private void processCommercialBulkSites(Integer quoteId, String quoteCode, List<Integer> illSitesList,
			List<MultiSiteResponseJsonAttributes> multiSiteResponseJsonAttributesList, Integer attachmentId,
			Integer taskId) throws TclCommonException {
		CommercialBulkProcessSites commercialBulkProcessSites = commercialBulkProcessSiteRepository
				.findBytaskId(taskId);
		Optional<QuoteIllSite> quoteIllSites = illSiteRepository.findById(illSitesList.get(0));
		if (commercialBulkProcessSites == null) {
			commercialBulkProcessSites = new CommercialBulkProcessSites();
			commercialBulkProcessSites.setTaskId(taskId);
			commercialBulkProcessSites.setQuoteId(quoteId);
			commercialBulkProcessSites.setQuoteCode(quoteCode);
			//commercialBulkProcessSites.setQuoteIllSite(quoteIllSites.get());
			commercialBulkProcessSites.setStatus("OPEN");
			commercialBulkProcessSites.setResponseJson(Utils.convertObjectToJson(multiSiteResponseJsonAttributesList));
			commercialBulkProcessSites.setSiteStatus("SUCCESS");
			commercialBulkProcessSites.setAttachementId(attachmentId);
			commercialBulkProcessSites.setCreatedBy(Utils.getSource());
			commercialBulkProcessSites.setCreatedTime(new Date());
			commercialBulkProcessSites.setTaskId(taskId);
			commercialBulkProcessSiteRepository.save(commercialBulkProcessSites);
		} else {
			LOGGER.info("ENTER else part" + commercialBulkProcessSites.getId());
			commercialBulkProcessSites.setQuoteId(quoteId);
			commercialBulkProcessSites.setQuoteCode(quoteCode);
			//commercialBulkProcessSites.setQuoteIllSite(quoteIllSites.get());
			commercialBulkProcessSites.setStatus("OPEN");
			commercialBulkProcessSites.setResponseJson(Utils.convertObjectToJson(multiSiteResponseJsonAttributesList));
			commercialBulkProcessSites.setSiteStatus("SUCCESS");
			commercialBulkProcessSites.setAttachementId(attachmentId);
			commercialBulkProcessSites.setUpdatedTime(new Date());
			commercialBulkProcessSites.setTaskId(taskId);
			commercialBulkProcessSiteRepository.save(commercialBulkProcessSites);
		}
		LOGGER.info(
				"Site details for quote code " + quoteCode + " is successfully saved in commercial bulk process sites");
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
	public MultiSiteStatusBean getMultiSiteStatusForIllGvpn(String quoteCode) {
		LOGGER.info("Inside getMultiSiteStatusForIllGvpn method quoteCode{} ", quoteCode);
		MultiSiteStatusBean multiSiteStatusBean = new MultiSiteStatusBean();
		long siteCompleteCount = 0L;
		long siteInProgressCount = 0L;
		Integer overAllSiteCount = 0;
		List<QuoteIllSite> quoteillSiteList = illSiteRepository.findIllSites(quoteCode);
		if(Objects.nonNull(quoteillSiteList) && !quoteillSiteList.isEmpty()) {
			LOGGER.info("Inside quoteillSiteList size{} ", quoteillSiteList.size());
			overAllSiteCount = quoteillSiteList.size();
			siteCompleteCount = quoteillSiteList.stream().filter(quoteIllSite -> Objects.nonNull(quoteIllSite.getSiteBulkUpdate()) && quoteIllSite.getSiteBulkUpdate().equals("1")).count();
			siteInProgressCount = quoteillSiteList.stream().filter(quoteIllSite -> Objects.isNull(quoteIllSite.getSiteBulkUpdate()) || quoteIllSite.getSiteBulkUpdate().equals("0")).count();
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
	
	public Integer getTotalSiteCount(Integer quoteId) {
		LOGGER.info("Inside getTotalSiteCount quoteId{} ", quoteId);
		Integer totalSiteCount = 0;
		List<QuoteToLe> quoteToLeList = quoteToLeRepository.findByQuote_Id(quoteId);
		for(QuoteToLe quoteToLe:quoteToLeList) {
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("IAS",
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


	/**
	 * This method used to update the site wise billing info
	 * 
	 * @param quoteId, productName, multiSiteBillingInfoBean
	 */
	public void persistMultiSiteBillingInfo(Integer quoteId, String productName,
			List<MultiSiteBillingInfoBean> requestInfoList) throws TclCommonException {
		LOGGER.info("Inside persistMultiSiteBillingInfo method productName {}, quoteId {} MultiSiteBillingInfoBean {} ",
				productName, quoteId, requestInfoList.toString());
		validateReqInfo(productName, quoteId, requestInfoList);
		try {
			for (MultiSiteBillingInfoBean requestInfo : requestInfoList) {
				if (productName.equalsIgnoreCase(CommonConstants.IAS)
						|| productName.equalsIgnoreCase(CommonConstants.GVPN)) {
					LOGGER.info("Inside persistMultiSiteBillingInfo product{}", productName);
					Optional<QuoteIllSite> quoteIllSite = illSitesRepository.findById(requestInfo.getSiteId());
					if (quoteIllSite.isPresent()) {
						QuoteSiteBillingDetails multiSiteBillingInfo = quoteSiteBillingInfoRepository
								.findByQuoteIdAndQuoteIllSiteAndProductName(quoteId, quoteIllSite.get(), productName);
						if (Objects.isNull(multiSiteBillingInfo)) {
							LOGGER.info("Inside persistMultiSiteBillingInfo if");
							multiSiteBillingInfo = new QuoteSiteBillingDetails();
							multiSiteBillingInfo.setCreatedBy(Utils.getSource());
							multiSiteBillingInfo.setCreatedDate(new Date());
							multiSiteBillingInfo.setProductName(productName);
							multiSiteBillingInfo.setQuoteIllSite(quoteIllSite.get());
							multiSiteBillingInfo.setQuoteId(quoteId);
							constructMultiSiteBillingInfo(quoteId, multiSiteBillingInfo, requestInfo);
						} else {
							LOGGER.info("Inside update multiSiteBillingInfo ");
							multiSiteBillingInfo.setUpdatedBy(Utils.getSource());
							multiSiteBillingInfo.setUpdatedDate(new Date());
							constructMultiSiteBillingInfo(quoteId, multiSiteBillingInfo, requestInfo);
						}
					}
				} else if (productName.equalsIgnoreCase(CommonConstants.NPL)
						|| productName.equalsIgnoreCase(CommonConstants.NDE)) {
					LOGGER.info("Inside persistMultiSiteBillingInfo product{}", productName);
					Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(requestInfo.getLinkId());
					Optional<QuoteIllSite> quoteIllSite = illSitesRepository.findById(requestInfo.getSiteId());
					if (quoteNplLink.isPresent()) {
						QuoteSiteBillingDetails multiSiteBillingInfo = quoteSiteBillingInfoRepository
								.findByQuoteIdAndQuoteIllSiteAndProductName(quoteId, quoteIllSite.get(), productName);
						if (Objects.isNull(multiSiteBillingInfo)) {
							LOGGER.info("Inside persistMultiSiteBillingInfo if");
							multiSiteBillingInfo = new QuoteSiteBillingDetails();
							multiSiteBillingInfo.setCreatedBy(Utils.getSource());
							multiSiteBillingInfo.setCreatedDate(new Date());
							multiSiteBillingInfo.setProductName(productName);
							multiSiteBillingInfo.setQuoteNplLink(quoteNplLink.get());
							multiSiteBillingInfo.setQuoteIllSite(quoteIllSite.get());
							multiSiteBillingInfo.setQuoteId(quoteId);
							multiSiteBillingInfo.setSiteType(requestInfo.getSiteType());
							constructMultiSiteBillingInfo(quoteId, multiSiteBillingInfo, requestInfo);
						} else {
							LOGGER.info("Inside update multiSiteBillingInfo");
							multiSiteBillingInfo.setUpdatedBy(Utils.getSource());
							multiSiteBillingInfo.setUpdatedDate(new Date());
							constructMultiSiteBillingInfo(quoteId, multiSiteBillingInfo, requestInfo);
						}
					}
				}
			}
			List<QuoteToLe> quoteTole = quoteToLeRepository.findByQuote_Id(quoteId);
			LOGGER.info(" update sitelevel billing in quotetole");
			quoteTole.get(0).setSiteLevelBilling("1");
			quoteToLeRepository.save(quoteTole.get(0));
		} catch (Exception e) {
			LOGGER.error(
					"Exception Occured while IIIQuoteService inside persistMultiSiteBillingInfo : {}" + e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	private void constructMultiSiteBillingInfo(Integer quoteId, QuoteSiteBillingDetails multiSiteBillingInfo,
			MultiSiteBillingInfoBean requestInfo) {
		LOGGER.info("Inside constructMultiSiteBillingInfo method");
	    multiSiteBillingInfo.setContactId(requestInfo.getContactId());
		multiSiteBillingInfo.setCusLeId(requestInfo.getCusLeId());
		multiSiteBillingInfo.setEtcCusBillingInfoId(requestInfo.getBillingInfoId());
		multiSiteBillingInfo.setGstNo(requestInfo.getGstNo());
		multiSiteBillingInfo.setQuoteCode(requestInfo.getQuoteCode());
		multiSiteBillingInfo.setState(requestInfo.getState());
		multiSiteBillingInfo.setCity(requestInfo.getCity());
		quoteSiteBillingInfoRepository.save(multiSiteBillingInfo);
		LOGGER.info("Saved site wise billing info");
	}

	private void validateReqInfo(String productName, Integer quoteId, List<MultiSiteBillingInfoBean> requestInfoList)
			throws TclCommonException {
		if (Objects.isNull(productName) || Objects.isNull(quoteId) || Objects.isNull(requestInfoList)) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
		}
		if (!requestInfoList.isEmpty()) {
			 List<MultiSiteBillingInfoBean> requestBiilingInfo=requestInfoList.stream().filter(st -> Objects.isNull(st.getState())).collect(Collectors.toList());
			if (!requestBiilingInfo.isEmpty()) {
				throw new TclCommonRuntimeException(ExceptionConstants.STATE_EMPTY, ResponseResource.R_CODE_ERROR);
			}
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
	
	/**
	 * This method used to get the sitewise billing details
	 * 
	 * @param quoteId, legalId
	 * @return MultiSiteBillingInfoBean
	 */
	public List<MultiSiteBillingInfoBean> getMultiSiteBillingInfo(Integer quoteId) throws TclCommonException {
		LOGGER.info("Inside getMultiSiteBillingInfo quoteId {} ", quoteId);
		List<MultiSiteBillingInfoBean> multiSiteBillingInfoBean = new ArrayList<>();
		try {
			List<QuoteSiteBillingDetails> multiSiteBillingInfoList = quoteSiteBillingInfoRepository.findByQuoteId(quoteId);
			List<LocationAddressInfo> locationAddressInfoList = new ArrayList<LocationAddressInfo>(); 
			List<BillingAddressInfo> billingAddressInfoList = new ArrayList<BillingAddressInfo>(); 
			List<GstAddressInfo> gstAddressInfoList = new ArrayList<GstAddressInfo>(); 
			if(!multiSiteBillingInfoList.isEmpty()) {
				for(QuoteSiteBillingDetails multiSiteBillingInfo : multiSiteBillingInfoList) {
					LocationAddressInfo locationAddressInfo = new LocationAddressInfo();
					BillingAddressInfo billingAddressInfo= new BillingAddressInfo();
					GstAddressInfo gstAddressInfo = new GstAddressInfo();

					locationAddressInfo.setMultisiteInfoId(multiSiteBillingInfo.getId());
					locationAddressInfo.setLocationId(multiSiteBillingInfo.getQuoteIllSite().getErfLocSitebLocationId());
					locationAddressInfoList.add(locationAddressInfo);

					billingAddressInfo.setMultisiteInfoId(multiSiteBillingInfo.getId());
					billingAddressInfo.setBillingInfoId(multiSiteBillingInfo.getEtcCusBillingInfoId());
					billingAddressInfoList.add(billingAddressInfo);

					gstAddressInfo.setMultisiteInfoId(multiSiteBillingInfo.getId());
					gstAddressInfo.setGstNo(multiSiteBillingInfo.getGstNo());
					gstAddressInfo.setState(multiSiteBillingInfo.getState());
					gstAddressInfo.setCity(multiSiteBillingInfo.getCity());
					gstAddressInfo.setSiteId(multiSiteBillingInfo.getQuoteIllSite().getId());
					gstAddressInfo.setSiteType(multiSiteBillingInfo.getSiteType());
					if(FPConstants.NPL.toString().equalsIgnoreCase(multiSiteBillingInfo.getProductName()) || ("NDE").equalsIgnoreCase(multiSiteBillingInfo.getProductName())) {			
						LOGGER.info("Inside getMultiSiteBillingInfo if NPL or NDE linkId {} ", multiSiteBillingInfo.getQuoteNplLink().getId());
						gstAddressInfo.setLinkId(multiSiteBillingInfo.getQuoteNplLink().getId());
					}
					gstAddressInfoList.add(gstAddressInfo);
				}
				SiteLevelAddressBean siteLevelAddressBean = new SiteLevelAddressBean();

				//billing and gst address
				siteLevelAddressBean.setLegalId(multiSiteBillingInfoList.get(0).getCusLeId());
				siteLevelAddressBean.setBillingAddressInfo(billingAddressInfoList);
				siteLevelAddressBean.setGstAddressInfo(gstAddressInfoList);
				siteLevelAddressBean = getBillingAndGstAddress(siteLevelAddressBean);

				siteLevelAddressBean.getGstAddressInfo().forEach(gstAddressInfo -> {
					LOGGER.info("GstAddressInfo GstNo {} ", gstAddressInfo.getGstNo());
					if(!"No Registered GST".equalsIgnoreCase(gstAddressInfo.getGstNo())) {
						String gstAddress = getGstAddress(gstAddressInfo.getGstNo());
						gstAddressInfo.setGstAddress(Objects.nonNull(gstAddress) ? gstAddress : "");
						LOGGER.info("GstAddressInfo GstAddress {} ", gstAddressInfo.getGstAddress());
					} else {
						gstAddressInfo.setGstAddress("");
					}
				});

				//locationAddress
				siteLevelAddressBean.setLocationAddressInfo(locationAddressInfoList);
				siteLevelAddressBean = getLocationAddressDetails(siteLevelAddressBean);

				multiSiteBillingInfoBean = setMultiSiteBillingInfoBeanValues(siteLevelAddressBean, multiSiteBillingInfoBean);
				LOGGER.info("Inside getMultiSiteBillingInfo multiSiteBillingInfoBean {} ", multiSiteBillingInfoBean);

			}

		} catch (Exception e) {
			LOGGER.error(
					"Exception Occured Inside getMultiSiteBillingInfo method: {}" + e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return multiSiteBillingInfoBean;
	}

	public List<MultiSiteBillingInfoBean> setMultiSiteBillingInfoBeanValues(SiteLevelAddressBean siteLevelAddressBean,
			List<MultiSiteBillingInfoBean> multiSiteBillingInfoBeanList) {
		try {
			List<LocationAddressInfo> locationAddressInfo = siteLevelAddressBean.getLocationAddressInfo();
			List<BillingAddressInfo> billingAddressInfo = siteLevelAddressBean.getBillingAddressInfo();
			List<GstAddressInfo> gstAddressInfo = siteLevelAddressBean.getGstAddressInfo();
			LOGGER.info("gstAddressInfo{}", gstAddressInfo.toString());	

			locationAddressInfo.forEach(location ->
			{
				MultiSiteBillingInfoBean multiSiteBillingInfoBean = new MultiSiteBillingInfoBean();
				multiSiteBillingInfoBean.setMultisiteInfoId(location.getMultisiteInfoId());
				multiSiteBillingInfoBean.setLocationId(location.getLocationId());
				multiSiteBillingInfoBean.setSiteAddress(location.getAddressDetail());

				List<BillingAddressInfo> siteBillingAddressInfo=billingAddressInfo.stream().filter(b -> location.getMultisiteInfoId().equals(b.getMultisiteInfoId())).collect(Collectors.toList());
				LOGGER.info("gstBillingAddressInfo SIZE"+siteBillingAddressInfo.size());	
				if(!siteBillingAddressInfo.isEmpty()) {
					multiSiteBillingInfoBean.setBillingAddress(siteBillingAddressInfo.get(0).getBillingAddress());
					multiSiteBillingInfoBean.setBillingInfoId(siteBillingAddressInfo.get(0).getBillingInfoId());
				}
				gstAddressInfo.stream().filter(g -> location.getMultisiteInfoId().equals(g.getMultisiteInfoId())).forEach(g -> {
					multiSiteBillingInfoBean.setGstAddress(g.getGstAddress());
					multiSiteBillingInfoBean.setGstNo(g.getGstNo());
					multiSiteBillingInfoBean.setState(g.getState());
					multiSiteBillingInfoBean.setCity(g.getCity());
					multiSiteBillingInfoBean.setSiteId(g.getSiteId());
					multiSiteBillingInfoBean.setSiteType(g.getSiteType());
					LOGGER.info("multiSiteBillingInfoBean g.getLinkId{} ", g.getLinkId());	
					multiSiteBillingInfoBean.setLinkId(g.getLinkId());
					LOGGER.info("multiSiteBillingInfoBean linkId{} ", multiSiteBillingInfoBean.getLinkId());	
				});

				multiSiteBillingInfoBean.setCusLeId(siteLevelAddressBean.getLegalId());
				multiSiteBillingInfoBeanList.add(multiSiteBillingInfoBean);
			});

		}catch (Exception e) {
			LOGGER.error(
					"Exception Occured Inside getMultiSiteBillingInfo method: {}" + e.getMessage());
		}
		return multiSiteBillingInfoBeanList;
	}

	private SiteLevelAddressBean getBillingAndGstAddress(SiteLevelAddressBean siteLevelAddressBean) {
		try {
			String requestData = Utils.convertObjectToJson(siteLevelAddressBean);
			LOGGER.info("Inside getBillingAndGstAddress siteLevelAddressBean request{} ", requestData);
			String response = (String) mqUtils.sendAndReceive(customerBillingGstAddressQueue, requestData);
			LOGGER.info(" getAddressDetails locationDetails response{}", response);
			if (response !=null) {
				siteLevelAddressBean = (SiteLevelAddressBean) Utils.convertJsonToObject(response, SiteLevelAddressBean.class);
				LOGGER.info(" Inside getBillingAndGstAddress response{} ", siteLevelAddressBean.toString());
			} 
		} catch (TclCommonException e) { 
			LOGGER.warn("getGstAddressDetials: Error in invoking GstAddressQueue {}", ExceptionUtils.getStackTrace(e));
		}
		return siteLevelAddressBean;
	}

	private SiteLevelAddressBean getLocationAddressDetails(SiteLevelAddressBean siteLevelAddressBean) {
		LOGGER.info("Inside getLocationAddressDetails siteLevelAddressBean request{} ", siteLevelAddressBean.toString());
		try {
			String requestData = Utils.convertObjectToJson(siteLevelAddressBean);
			LOGGER.info("Inside getLocationAddressDetails siteLevelAddressBean request{} ", requestData);
			String response = (String) mqUtils.sendAndReceive(siteLevelLocationQueue, requestData);
			LOGGER.info("getLocationAddressDetails locationDetails response{} ", response);
			if(response != null) {
				siteLevelAddressBean = (SiteLevelAddressBean) Utils.convertJsonToObject(response,
						SiteLevelAddressBean.class);
				LOGGER.info(" Inside getLocationAddressDetails locationDetails response{} ", siteLevelAddressBean.toString());
			}
		} catch (Exception e) {
			LOGGER.warn("getLocationAddressDetails: Error in invoking locationQueue {}", ExceptionUtils.getStackTrace(e));
		}
		return siteLevelAddressBean;
	}

	/**
	 * This method used deleteSiteBilliginfo
	 * 
	 * @param quoteId, quotole
	 * @return MultiSiteBillingInfoBean
	 */
	public void deleteSiteBilliginfo(Integer quoteId, Integer quotetoLeId) throws TclCommonException {
		LOGGER.info("enter into deleteSiteBilliginfo"+quoteId+"quotetoLeId:"+quotetoLeId);
		try {
			if(Objects.isNull(quoteId) || Objects.isNull(quotetoLeId))
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

			List<QuoteSiteBillingDetails> multiSiteBillingInfoList = quoteSiteBillingInfoRepository.findByQuoteId(quoteId);
			if(!multiSiteBillingInfoList.isEmpty() && multiSiteBillingInfoList.size()!=0) {
				quoteSiteBillingInfoRepository.deleteAll(multiSiteBillingInfoList);
				LOGGER.info("SITE WISE BILLING DELETED SUCCESSFULLY");
			}
			Optional<QuoteToLe> quoteToLe=quoteToLeRepository.findById(quotetoLeId);
			if(quoteToLe.isPresent()) {
				quoteToLe.get().setSiteLevelBilling("0");
				LOGGER.info("QUOTOTOLE UPDATED SUCCESSFULLY");
			}

		} catch (Exception e) {
			LOGGER.warn("error in deleteSiteBilliginfo", e.toString());
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
	 * getList of gst based on le id
	 * @param gstno
	 * @param sitecode
	 */
	public List<LeGstDetailsBean> getAllGstInfo(Integer custLeId) {
		LOGGER.info("ENTER into getAllGstInfo::" + custLeId);
		List<LeGstDetailsBean> gstList = new ArrayList<LeGstDetailsBean>();
		try {
			if (custLeId != null) {
				String gstResponse = (String) mqUtils.sendAndReceive(LeGstQueue, String.valueOf(custLeId));
				LOGGER.info("Queue response:::::" + gstResponse);
				if (StringUtils.isNotBlank(gstResponse)) {
					List<LeGstDetailsBean> leGstDetailsBeanList = Utils.fromJson(gstResponse,
							new TypeReference<List<LeGstDetailsBean>>() {
							});
					if (!leGstDetailsBeanList.isEmpty() && leGstDetailsBeanList != null) {
						for(LeGstDetailsBean bean:leGstDetailsBeanList) {
							LOGGER.info("GST NO::::"+bean.getGstNo());
							LeGstDetailsBean leGstn=new LeGstDetailsBean();
							String gstAddress="";
							if(bean.getGstNo()!=null) {
							 gstAddress=getGstAddress(bean.getGstNo());
							}
							LOGGER.info("GST ADDRESS:::"+gstAddress);
							if (gstAddress != null) {
								leGstn.setAddress(gstAddress);
							} else {
								leGstn.setAddress("");
							}
							leGstn.setId(bean.getId());
							leGstn.setGstNo(bean.getGstNo());
							leGstn.setState(bean.getState());
							gstList.add(leGstn);
						}
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.info("Error in updateGstNumber in illoredrservice : {}", ex);
		}
		return gstList;
	}
	
	/**
	 * getGstAddress
	 */
	private String getGstAddress(String gstIn) {
		LOGGER.info("inside getGstAddress gst no :::"+gstIn);
		String gstAddress = null;
		try {
			GstAddressBean gstAddressBean = new GstAddressBean();
			gstInService.getGstAddress(gstIn, gstAddressBean);
			if(gstAddressBean!=null && Objects.nonNull(gstAddressBean)) {
			    gstAddress = gstAddressBean.getFullAddress();
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting gst address", e);
		}
		return gstAddress;
	}
	private String getGstAddressRenewals(String gstIn) {
		String gstAddress = null;
		try {
			GstAddressBean gstAddressBean = new GstAddressBean();
			gstInService.getGstAddress(gstIn, gstAddressBean);
			gstAddress = Utils.convertObjectToJson(gstAddressBean);
		} catch (Exception e) {
			LOGGER.error("Error in getting gst address", e);
		}
		return gstAddress;
	}
	
	public void updateGstAddress(AttributeDetail attributeDetail, QuoteProductComponent orderProductComponent) {
		List<QuoteProductComponentsAttributeValue> gstAddrComps = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster_Name(orderProductComponent,
						"GST_ADDRESS");
		for (QuoteProductComponentsAttributeValue gstAddrComp : gstAddrComps) {
			if(gstAddrComp.getIsAdditionalParam().equals(CommonConstants.Y)) {
			String attrV=gstAddrComp.getAttributeValues();
				Optional<AdditionalServiceParams> additionalServiceParams=additionalServiceParamRepository.findById(Integer.valueOf(attrV));
				if(additionalServiceParams.isPresent()) {
					additionalServiceParams.get().setValue(getGstAddressRenewals(attributeDetail.getValue()));
					additionalServiceParams.get().setUpdatedBy(Utils.getSource());
					additionalServiceParams.get().setUpdatedTime(new Date());
					additionalServiceParamRepository.save(additionalServiceParams.get());
				}
			}
			
		}
		if(gstAddrComps.isEmpty()) {
			createGstAddress(attributeDetail, orderProductComponent);
		
		}
	}

	/**
	 * createGstAddress
	 * @param attributeDetail
	 * @param orderProductComponent
	 */
	private QuoteProductComponentsAttributeValue createGstAddress(AttributeDetail attributeDetail,
			QuoteProductComponent orderProductComponent) {
		String address = getGstAddressRenewals(attributeDetail.getValue());
		AdditionalServiceParams additionalServiceParam = new AdditionalServiceParams();
		additionalServiceParam.setValue(address);
		additionalServiceParam.setCreatedBy(Utils.getSource());
		additionalServiceParam.setCreatedTime(new Date());
		additionalServiceParam.setIsActive(CommonConstants.Y);
		additionalServiceParam.setAttribute("GST_ADDRESS");
		additionalServiceParam.setCategory("QUOTE");
		additionalServiceParamRepository.save(additionalServiceParam);
		List<ProductAttributeMaster> gstAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus("GST_ADDRESS", (byte) 1);
		if (gstAttributeMasters != null && !gstAttributeMasters.isEmpty()) {
			QuoteProductComponentsAttributeValue gstComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
			gstComponentsAttributeValue.setAttributeValues(additionalServiceParam.getId()+"");
			gstComponentsAttributeValue.setDisplayValue(additionalServiceParam.getId()+"");
			gstComponentsAttributeValue.setQuoteProductComponent(orderProductComponent);
			gstComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
			gstComponentsAttributeValue.setProductAttributeMaster(gstAttributeMasters.get(0));
			quoteProductComponentsAttributeValueRepository.save(gstComponentsAttributeValue);
			return gstComponentsAttributeValue;
		}
		return null;
	}
	
	public void upateSitePropertiesAttributeRenewals(ProductAttributeMaster productAttributeMasters,
			AttributeDetail attributeDetail, QuoteProductComponent orderProductComponent) {

		List<QuoteProductComponentsAttributeValue> orderProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(orderProductComponent,
						productAttributeMasters);
		if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
			for (QuoteProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
			
					
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
					
				
				orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
				quoteProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
			}
		} 

	}
	
	/**
     * Method to get quote ill site by site id and update its mf task triggered
     *
     * @param siteId
     */
    public String updateMfTaskTriggered(Integer siteId) {
		LOGGER.info("Inside updateMfTaskTriggered with siteId {}", siteId);
		String response = "";
		try {
		List<TaskBean> tasks = getTaskList(siteId);
		List<TaskBean> successList = new ArrayList<TaskBean>();
		if(tasks.size() > 0) {
		tasks = tasks.stream()
					.filter(task -> task.getMstTaskDef().startsWith("manual_feasibility"))
					.collect(Collectors.toList());
		if (!tasks.isEmpty()) {
		tasks.stream().forEach(item ->{
			LOGGER.info("Task id in oms :{}",item.getId());
			if(item.getStatus().equalsIgnoreCase("OPEN") || item.getStatus().equalsIgnoreCase("RETURNED") || 
				item.getStatus().equalsIgnoreCase("INPROGRESS") || item.getStatus().equalsIgnoreCase("REOPEN")) {
				successList.add(item);
			}
		});
		if(successList.size() == 0) {
				QuoteIllSite quoteIllSite = illSiteRepository.findById(siteId).get();
				   if(quoteIllSite != null) {
				         quoteIllSite.setMfTaskTriggered(0);
				         illSiteRepository.save(quoteIllSite);
				         response =  "Successfully Triggered";
			       }else {
				         LOGGER.info("Invalid siteId {}", siteId);
				  }  
		}else {
			response =  "MF tasks are still in-progress";
		}
	 }else {
			response = "No MF tasks available for this siteId" ;
		}
	}else {
		response = "No tasks available for this siteId" ;
	}
	} catch (Exception ex) {
			LOGGER.info("Error in updateMfTaskTriggered in illOrderService : {}", ex);
		} 
	return response;
    }
    
    /**
     * Get Task List by siteId
     *
     * @param siteId
     */
    private List<TaskBean> getTaskList(Integer siteId) {
    	LOGGER.info("Inside getTaskList with siteId {}", siteId);
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

	public String updateBillingDetails(Integer quoteLeId, Integer billingContactId) throws TclCommonException {
		String status = Status.SUCCESS.toString();
		MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
		LOGGER.info("Inside updateBillingDetailsforIllGvpn() with quoteLeId {} and billingContactId {}", quoteLeId, billingContactId);
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {

			List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(BILLING_CONTACT_ID,
					CommonConstants.BACTIVE);

			if (!mstOmsAttributes.isEmpty()) {
				mstOmsAttribute = mstOmsAttributes.get(0);
				LOGGER.info("mstOmsAttribute name {}", mstOmsAttribute.getName());
			}
			List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLeEntity.get(), mstOmsAttribute);
			try {
				Map<String, Object> utilityFromValue = new HashMap<>();
				if (quoteToLeAttribute != null && !(quoteToLeAttribute.isEmpty())) {
					for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute) {
						LOGGER.info("Setting billing contact id {}", billingContactId);
						utilityFromValue.put("QuoteLeAttribute_ID", quoteLeAttributeValue.getId());
						utilityFromValue.put("QuoteLEMstId", quoteLeAttributeValue.getMstOmsAttribute().getId());
						utilityFromValue.put("QuoteLEAttributeValue", quoteLeAttributeValue.getAttributeValue());
						quoteLeAttributeValue.setAttributeValue(billingContactId.toString());
						quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
					}
				}
				saveUtilityAudit(quoteToLeEntity.get().getQuote().getQuoteCode(), billingContactId.toString(), utilityFromValue.toString(), "Update Billing Contact Id");
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
			}
		}

		return status;
	}

	public String updateCCADetails(Integer quoteLeId, String ccaId) throws TclCommonException {

		String status = Status.SUCCESS.toString();
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
		LeCcaRequestBean leCcaRequestBean = new LeCcaRequestBean();
		MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();

		if (quoteToLeOpt.isPresent()) {
			leCcaRequestBean.setCcaId(ccaId);
			leCcaRequestBean.setCustomerLeId(quoteToLeOpt.get().getErfCusCustomerLegalEntityId().toString());
			LOGGER.info("Inside updateCCADetails leCcaRequestBean request{} ", leCcaRequestBean.toString());
			try {
				String requestData = Utils.convertObjectToJson(leCcaRequestBean);
				LOGGER.info("Inside updateCCADetails leCcaRequestBean request{} ", requestData);
				String response = (String) mqUtils.sendAndReceive(getLeCCAfromCustomerQueue, requestData);
				LOGGER.info("updateCCADetails leCcaRequestBean response{} ", response);
				if (response != null) {
					leCcaRequestBean = (LeCcaRequestBean) Utils.convertJsonToObject(response,
							LeCcaRequestBean.class);
					LOGGER.info(" Inside getLocationAddressDetails locationDetails response{} ", leCcaRequestBean.toString());
				}
			} catch (Exception e) {
				LOGGER.warn("updateCCADetails: Error in invoking getLeCCAfromCustomerQueue {}", ExceptionUtils.getStackTrace(e));
			}
			if (StringUtils.isNotBlank(leCcaRequestBean.getCcaLocationId())) {

				List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(CUSTOMER_CONTRACTING_ENTITY,
						CommonConstants.BACTIVE);

				if (!mstOmsAttributes.isEmpty()) {
					mstOmsAttribute = mstOmsAttributes.get(0);
					LOGGER.info("mstOmsAttribute name {}", mstOmsAttribute.getName());
				}
				List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute(quoteToLeOpt.get(), mstOmsAttribute);
				Map<String, Object> utilityFromValue = new HashMap<>();
				try {
					if (quoteToLeAttribute != null && !(quoteToLeAttribute.isEmpty())) {

						for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute) {
							LOGGER.info("Setting le cca in quote le attributes {}", leCcaRequestBean.getCcaLocationId());
							utilityFromValue.put("QuoteLeAttribute_ID", quoteLeAttributeValue.getId());
							utilityFromValue.put("QuoteLEMstId", quoteLeAttributeValue.getMstOmsAttribute().getId());
							utilityFromValue.put("QuoteLEAttributeValue", quoteLeAttributeValue.getAttributeValue());
							quoteLeAttributeValue.setAttributeValue(leCcaRequestBean.getCcaLocationId());
							quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
						}
					}
				} catch (Exception e) {
					throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
				}
				saveUtilityAudit(quoteToLeOpt.get().getQuote().getQuoteCode(), ccaId.toString(), utilityFromValue.toString(), "CCA Address Update");
			}
		}
		return status;

	}

	public String updateSelectedResponse(Integer siteId, Integer feasibilityRowId,Integer flag){
		String response=null;
		try{
			LOGGER.info("Inside Response selection method", siteId, feasibilityRowId,flag);
			Optional<SiteFeasibility> siteFeasibilityResult= null;
			siteFeasibilityResult=siteFeasibilityRepository.findById(feasibilityRowId);
			if(siteFeasibilityResult!=null){
				LOGGER.info("Site Feasibility record available {}",siteFeasibilityResult.get());
				if(flag==0) {
					siteFeasibilityResult.get().setIsSelected(BDEACTIVATE);
				}
				else{
					siteFeasibilityResult.get().setIsSelected(BACTIVE);
				}
				siteFeasibilityRepository.save(siteFeasibilityResult.get());
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
	
	public void updateSiteFeasibilityResponse(SiteFeasibilityManualBean bean) {
		if (bean.getSiteId() != null) {
			List<SiteFeasibility> quoteIllSitesEntryList = siteFeasibilityRepository.findByQuoteIllSite_Id(bean.getSiteId());
			if (CollectionUtils.isEmpty(quoteIllSitesEntryList)) {
				LOGGER.error("Site ID {} not found in site Feasibility table", bean.getSiteId());
				throw new TclCommonRuntimeException(ExceptionConstants.SITE_FEASIBILITY_UNAVAILABLE,
						ResponseResource.R_CODE_ERROR);
			}
			Optional<SiteFeasibility> requestedToBeUpdated = quoteIllSitesEntryList.stream()
					.filter(x -> x.getId().equals(bean.getSiteFeasibilityRowId())).findFirst();
			if (!requestedToBeUpdated.isPresent()) {
				LOGGER.error("No such row in site feasibility", bean.getSiteFeasibilityRowId());
				throw new TclCommonRuntimeException(ExceptionConstants.SITE_FEASIBILITY_ROW_UNAVAILABLE,
						ResponseResource.R_CODE_ERROR);
			}
			SiteFeasibility siteFeasibilityEntry = requestedToBeUpdated.get();
			// response_json
			if (bean.getColumnName().equals("response_json")) {
				String responseJson = siteFeasibilityEntry.getResponseJson();
				Map<String, String> utilityFromValue = new HashMap<>();
				if (responseJson != null) {
					siteFeasibilityEntry.setResponseJson(parseAndUpdateJson(bean, responseJson, siteFeasibilityEntry, utilityFromValue));
					siteFeasibilityRepository.save(siteFeasibilityEntry);
					LOGGER.info("Upated site feasibility response for id {} ", siteFeasibilityEntry.getId());
				}
				saveUtilityAudit(requestedToBeUpdated.get().getQuoteIllSite().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode(), 
						bean.toString(), utilityFromValue.toString(), "Site Feasibility Response Update");
			}
		}
	}

	private String parseAndUpdateJson(SiteFeasibilityManualBean bean, String responseJson,
			SiteFeasibility linkFeasibilityEntry, Map<String, String> utilityFromValue ) {
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
