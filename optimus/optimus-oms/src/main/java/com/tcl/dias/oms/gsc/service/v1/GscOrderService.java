package com.tcl.dias.oms.gsc.service.v1;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.COF_SUBMITTED;
import static com.tcl.dias.common.constants.CommonConstants.COF_SUBMITTED_COMPLETE_PERCENTAGE;
import static com.tcl.dias.common.constants.CommonConstants.COLON;
import static com.tcl.dias.common.constants.CommonConstants.LAUNCH_DELIVERY;
import static com.tcl.dias.common.constants.CommonConstants.LAUNCH_DELIVERY_COMPLETE_PERCENTAGE;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.LeAttributesConstants.MULTI_MACD_TRUNK_DATA_TYPE;
import static com.tcl.dias.common.utils.AuditMode.MANUAL;
import static com.tcl.dias.common.utils.Source.MANUAL_COF;
import static com.tcl.dias.common.utils.ThirdPartySource.CHANGE_OUTPULSE;
import static com.tcl.dias.common.utils.ThirdPartySource.ENTERPRISE_TIGER_ORDER;
import static com.tcl.dias.common.utils.ThirdPartySource.WHOLESALE_TIGER_ORDER;
import static com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_ENRICHMENT;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_FORM;
import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundError;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.APPROVE_QUOTE_TYPE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CUSTOMER_SECS_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_DOMESTIC_DOWNSTREAM_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_MACD_PRODUCT_REFERENCE_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_SERVICE_ABBREVIATION;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.SUPPLIER_SECS_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACCESS_SERVICES;
import static com.tcl.dias.oms.gsc.util.GscConstants.ATTRIBUTES_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANGE_OUTPULSE_ORDER;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANGING_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.COMPLETED;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONFIGURATION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CUSTOMER_LE_ORG_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOWNSTREAM_REQUEST_STATUS_ERROR;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOWNSTREAM_REQUEST_STATUS_IN_PROGRESS;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOWNSTREAM_REQUEST_STATUS_PENDING;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOWNSTREAM_REQUEST_STATUS_SUBMITTED;
import static com.tcl.dias.oms.gsc.util.GscConstants.GLOBAL_OUTBOUND;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CFG_TYPE_REFERENCE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_PRODUCT_NAME;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_OUTBOUND_EXCEL;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_OUTBOUND_PDF;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_SURCHARGE_PDF;
import static com.tcl.dias.oms.gsc.util.GscConstants.MPLS;
import static com.tcl.dias.oms.gsc.util.GscConstants.NEW;
import static com.tcl.dias.oms.gsc.util.GscConstants.NO;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_GSC_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_PRODUCT_COMPONENT_NUll_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;
import static com.tcl.dias.oms.gsc.util.GscConstants.OTHERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.PRODUCT_COMPONENT_TYPE_ORDER_GSC;
import static com.tcl.dias.oms.gsc.util.GscConstants.PRODUCT_NAME_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.SOLUTION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_DOMESTIC_ORDER;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER;
import static com.tcl.dias.oms.gsc.util.GscConstants.TRUE_FLAG;
import static com.tcl.dias.oms.gsc.util.GscConstants.WHOLESALE_ORDER;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.SELL_THROUGH_CLASSIFICATION;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ExpectedDeliveryDateBean;
import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.MSABean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.beans.SfdcOpportunityInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.GscAttachmentTypeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.gsc.beans.GscOutboundAttachmentBean;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.SalesSupportMailNotificationBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscSla;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteGscSla;
import com.tcl.dias.oms.entity.entities.QuoteGscTfn;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscTfnRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscOrderBean;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscOrderLeAttributeBean;
import com.tcl.dias.oms.gsc.beans.GscOrderLeBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeArrayValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.common.GscPdfHelper;
import com.tcl.dias.oms.gsc.exception.Exceptions;
import com.tcl.dias.oms.gsc.exception.ObjectNotFoundException;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;
import com.tcl.dias.oms.gsc.tiger.TigerServiceConstants;
import com.tcl.dias.oms.gsc.tiger.TigerServiceHelper;
import com.tcl.dias.oms.gsc.tiger.beans.BaseOrderManagementBean;
import com.tcl.dias.oms.gsc.tiger.beans.BaseOrderManagementResponse;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.GetOrderDetail;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.SubOrder;
import com.tcl.dias.oms.gsc.tiger.beans.SubOrderDetails;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscComponentAttributeValuesHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscOrderManagementRequestBuilder;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gsc.validation.OrderValidator;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.lr.service.OrderLrService;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.vavr.control.Try;
import io.vavr.control.Validation;

/**
 * Services to handle all order related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscOrderService.class);

	@Value("${app.host}")
	String appHost;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${rabbitmq.gsc.order.process.queue}")
	String orderJobProcessingQueue;

	@Value("${rabbitmq.opportunity.get}")
	String sfdcOpportunityGetQueue;

	@Value("${process.macd.orders.with.downstream}")
	String processMACDOrdersWithDownstreamSystem;

	@Value("${process.gsc.orders.with.downstream}")
	String processOrdersWithDownstreamSystem;

	@Autowired
	UserService userService;
	@Autowired
	QuoteGscRepository quoteGscRepository;
	@Autowired
	QuoteRepository quoteRepository;
	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderToLeRepository orderToLeRepository;
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;
	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;
	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;
	@Autowired
	OrderGscRepository orderGscRepository;
	@Autowired
	QuoteGscSlaRepository quoteGscSlaRepository;
	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;
	@Autowired
	OrderGscSlaRepository orderGscSlaRepository;
	@Autowired
	OrderGscDetailRepository orderGscDetailRepository;
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
	@Autowired
	ProductSolutionRepository productSolutionRepository;
	@Autowired
	GscOrderDetailService gscOrderDetailService;
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	@Autowired
	OrderGscTfnRepository orderGscTfnRepository;
	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	@Autowired
	GscComponentAttributeValuesHelper attributeValuesPopulator;
	@Autowired
	MstProductComponentRepository mstProductComponentRepository;
	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;
	@Autowired
	MstOrderSiteStageRepository mstOrderSiteStageRepository;
	@Autowired
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;
    @Autowired
    AdditionalServiceParamRepository additionalServiceParamRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	QuoteGscTfnRepository quoteGscTfnRepository;
	@Autowired
	OmsSfdcService omsSfdcService;
	@Autowired
	GscPdfHelper gscPdfHelper;
	@Autowired
	MQUtils mqUtils;
	@Autowired
	OrderLrService orderLrService;
	@Autowired
    FileStorageService fileStorageService;
    @Autowired
    GscAttachmentHelper gscAttachmentHelper;
    
    @Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Autowired
	OrderValidator orderValidator;

	@Value("${expected.delivery.date.queue}")
	String expectedDeliveryDateQueue;

	@Value("${rabbitmq.product.outbound.pricing}")
	private String outboundQueue;

	@Value("${rabbitmq.product.city.location.queue}")
	private String cityDetailsQueue;

	@Autowired
	GscOrderManagementRequestBuilder gscOrderManagementRequestBuilder;

	@Autowired
	TigerServiceHelper tigerServiceHelper;

	@Autowired
	NotificationService notificationService;

	@Autowired
	GscQuotePdfService gscQuotePdfService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Value("${rabbitmq.customer.le.update.msa}")
	String updateMSAQueue;

	@Autowired
	EngagementRepository engagementRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	GscExportLRService gscExportLRService;

	@Autowired
	DocusignAuditRepository docusignAuditRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	private static final String OUTBOUND_ERROR = "Error in getting outbound prices";

	@Autowired
	UserInfoUtils userInfoUtils;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	PartnerService partnerService;

	@Value("${rabbitmq.gsc.tiger.order.change.outpulse.process.queue}")
	String tigerOrderChangeOutpulseJobProcessingQueue;

	@Value("${rabbitmq.inventory.outpulse.queue}")
	private String updateOutpulseInventory;

	@Value("${rabbitmq.customer.le.secs.queue}")
	String customerLeSecsIdQueue;

	@Value("${rabbitmq.inventory.gsc.abbreviation.queue}")
	String gscAbbreviation;
	
	private static final Map<String, String> GSC_SERVICE_ABBREVIATION_TABLE = ImmutableMap.<String, String>builder()
			.put("LNS", "ENTLOCA")
			.put("ITFS", "ENT800")
			.put("UIFN", "ENTUIFN")
			.put("Domestic Voice", "GSCDNVT,GSCDVTS,GSCDDID")
			.put("Global Outbound", "ENTVTS")
			.put("ACANS", "ENTLNSI")
			.put("ACDTFS", "ENTDTFS")
			.build();
	@Value("${rabbitmq.partner.customer.details}")
	private String partnerCustomerDetailsMQ;
	
	@Value("${swift.api.enabled}")
    String swiftApiEnabled;
    
    @Value(("${swift.api.container}"))
    String swiftApiContainer;
    
    @Value("${attachment.queue}")
    String attachmentQueue;

	/**
	 * Create order after quote approved by customer
	 *
	 * @param quoteId
	 * @param HttpServletResponse
	 * @return {@link GscOrderDataBean}
	 */
	@Transactional
	public GscOrderDataBean approveQuotes(Integer quoteId, HttpServletResponse response) {
		return Try.success(createApproveQuoteContext(quoteId, response))
				.flatMap(this::getQuote)
				.map(this::getOrder)
				.flatMap(this::saveOrder)
				.map(this::getQuoteToLe)
				.map(this::getQuoteToLeAttribute)
				.map(this::getProductFamily)
				.map(this::getQuoteProductSolution)
				.map(this::getQuoteGsc)
				.map(this::getQuoteGscDetails)
				.map(this::getQuoteProductComponent)
				.map(this::getQuoteProductComponentAttributeValues)
				.map(this::getQuoteGscSla)
				.map(this::createAndSaveOrderToLe)
				.mapTry(this::updateOpportunityInSfdc)
				.map(this::processCofPdf)
				.map(this::checkMSAandSSDocuments)
				.map(this::sendPlaceOrderNotification)
				.mapTry(this::updateApproveQuoteType)
//				.map(this::updateCustomerSecsId)
//				.mapTry(this::updateSupplierSecsId)
//				.mapTry(this::updateServiceAbbrevation)
				.map(context -> context.gscOrderDataBean).get();
	}

	/**
	 * Approve Quotes for Docu Sign
	 *
	 * @param quoteCode
	 * @return
	 * @throws TclCommonException
	 */
	public GscOrderDataBean approvedDocuSignQuotes(String quoteCode) throws TclCommonException {
		return Try.success(createDocuSignApproveQuoteContext(quoteCode))
				.flatMap(this::getQuoteByQuoteCode)
				.map(this::getQuoteToLe)
				.map(this::getQuoteToLeAttribute)
				.map(this::getProductFamily)
				.map(this::getQuoteProductSolution)
				.map(this::getQuoteGsc)
				.mapTry(this::approveDocuSignForGVPN)
				.map(this::updateQuoteLeStage)
				.map(this::getOrder)
				.flatMap(this::saveOrder)
				.map(this::getQuoteGscDetails)
				.map(this::getQuoteProductComponent)
				.map(this::getQuoteProductComponentAttributeValues)
				.map(this::getQuoteGscSla)
				.map(this::createAndSaveOrderToLe)
				.map(this::getDocuSignAudit)
				.mapTry(this::updateOpportunityInSfdc)
				.map(this::getCofObjectMapperForDocuSign)
				.map(this::setQuoteStatus)
				.map(this::updateOrderToLe)
				.map(this::updateAttachmentAuditInfo)
				.mapTry(this::getQuoteDelegate)
				.mapTry(this::updateApproveQuoteType)
//				.map(this::updateCustomerSecsId)
//				.mapTry(this::updateSupplierSecsId)
//				.mapTry(this::updateServiceAbbrevation)
				.map(context -> context.gscOrderDataBean).get();
	}

	/**
	 * Create order after quote signed by customer and upload by admin
	 *
	 * @param HttpServletResponse
	 * @param quoteId
	 * @param forwardedIp
	 * @return {@link GscOrderDataBean}
	 */
	@Transactional
	public GscOrderDataBean approveManualQuotes(Integer quoteId, String ipAddress) {
		return Try.success(createManualApproveQuoteContext(quoteId, ipAddress))
				.flatMap(this::getQuote)
				.map(this::getOrder)
				.flatMap(this::saveOrder)
				.map(this::getQuoteToLe)
				.map(this::getQuoteToLeAttribute)
				.map(this::getProductFamily)
				.map(this::getQuoteProductSolution)
				.map(this::getQuoteGsc)
				.map(this::getQuoteGscDetails)
				.map(this::getQuoteProductComponent)
				.map(this::getQuoteProductComponentAttributeValues)
				.map(this::updateQuoteLeStage)
				.map(this::getQuoteGscSla)
				.map(this::createAndSaveOrderToLe)
				.mapTry(this::updateOpportunityInSfdc)
				.map(this::getCofObjectMapperForManualApprove)
				.map(this::setQuoteStatus)
				.map(this::updateOrderToLe)
				.map(this::updateAttachmentAuditInfo)
				.mapTry(this::getQuoteDelegate)
				.mapTry(this::updateManualOrderConfirmationAudit)
				.mapTry(this::updateApproveQuoteType)
//				.map(this::updateCustomerSecsId)
//				.mapTry(this::updateSupplierSecsId)
//				.mapTry(this::updateServiceAbbrevation)
				.map(context -> context.gscOrderDataBean).get();
	}

	/**
	 * createApproveQuoteContext
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 */
	private GscApproveQuoteContext createApproveQuoteContext(Integer quoteId, HttpServletResponse response) {
		GscApproveQuoteContext context = new GscApproveQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		Quote quote = new Quote();
		quote.setId(quoteId);
		context.quote = quote;
		context.gscNewOrder = false;
		context.response = response;
		context.cofSignedDate = new Timestamp(System.currentTimeMillis());
		context.approveQuoteType = "Auto Approval";
		return context;
	}

	/**
	 * Get Quote
	 * <p>
	 * getQuote
	 *
	 * @param context
	 * @return
	 */
	private Try<GscApproveQuoteContext> getQuote(GscApproveQuoteContext context) {
		context.quote = getQuoteById(context.quote.getId()).get();
		return Try.success(context);
	}

	private GscApproveQuoteContext updateApproveQuoteType(GscApproveQuoteContext context) throws TclCommonException {
		ProductAttributeMaster approvequoteTypeMaster = getMasterAttribute(APPROVE_QUOTE_TYPE);
		context.orderGscDetail.stream().forEach(orderGscDetail -> {
			List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
					.findByReferenceIdAndType(orderGscDetail.getId(), GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
			orderProductComponents.stream().forEach(orderProductComponent -> {
				List<OrderProductComponentsAttributeValue> attributeValues = orderProductComponentsAttributeValueRepository
						.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent, approvequoteTypeMaster);
				attributeValues.stream().forEach(attributeValue -> {
					attributeValue.setAttributeValues(context.approveQuoteType);
					attributeValue.setDisplayValue(context.approveQuoteType);
					orderProductComponentsAttributeValueRepository.save(attributeValue);
				});
			});
		});

		return context;
	}

	private GscOrderAttributeValuesContext updateCustomerSecsId(GscOrderAttributeValuesContext context) {
		Set<OrdersLeAttributeValue> orgNoAttribute = ordersLeAttributeValueRepository
				.findByMstOmsAttribute_NameAndOrderToLe(CUSTOMER_LE_ORG_ID, context.orderToLe);
		Set<OrdersLeAttributeValue> secsIdAttribute = ordersLeAttributeValueRepository
				.findByMstOmsAttribute_NameAndOrderToLe(ATTR_CUSTOMER_SECS_ID, context.orderToLe);

		LOGGER.info("Org No Attribute :: {}", orgNoAttribute);
		LOGGER.info("Secs Id Attribute :: {}", secsIdAttribute);

		String orgNo = getErfSecsId(context.orderToLe.getErfCusCustomerLegalEntityId());

		// setting secs id to org no attribute
		orgNoAttribute.stream().forEach(ordersLeAttributeValue -> {
			ordersLeAttributeValue.setAttributeValue(orgNo);
		});
		ordersLeAttributeValueRepository.saveAll(orgNoAttribute);
		return context;
	}

	private String getErfSecsId(Integer erfCusCustomerLegalEntityId) {
		String orgId = "";
		//Integer erfCusCustomerLegalEntityId = context.quoteToLe.getErfCusCustomerLegalEntityId();
		LOGGER.info("MDC Filter token value in before Queue call getCustomerLeCuId {} {} :", erfCusCustomerLegalEntityId,
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		try {
			if (Objects.nonNull(erfCusCustomerLegalEntityId)) {
				orgId = (String) mqUtils.sendAndReceive(customerLeSecsIdQueue, String.valueOf(erfCusCustomerLegalEntityId));
			}
		} catch (TclCommonException e) {
			LOGGER.warn("Error Occured while getting CustomerLe SECS ID :: {}", e.getStackTrace());
		}
		return orgId;
	}

	private GscOrderAttributeValuesContext updateSupplierSecsId(GscOrderAttributeValuesContext context) throws TclCommonException {
		LeSapCodeResponse leSapCodeResponseSupplier = gscOrderDetailService.getSupplierLeSapCodeDetails(context.orderToLe.getErfCusSpLegalEntityId());
		LeSapCodeBean leSapCodeBeanSupplier = leSapCodeResponseSupplier.getLeSapCodes().stream().findFirst().get();
		LOGGER.info("leSapCodeBean :: {}", leSapCodeBeanSupplier.toString());
		// SECS Code and Org No both are same
		String supplierSECSCode = leSapCodeBeanSupplier.getCodeValue();
		LOGGER.info("supplierSECSCode :: {}", supplierSECSCode);

		GscOrderProductComponentsAttributeSimpleValueBean simpleValueBean = new GscOrderProductComponentsAttributeSimpleValueBean();
		simpleValueBean.setAttributeValue(supplierSECSCode);
		simpleValueBean.setDisplayValue(supplierSECSCode);
		simpleValueBean.setAttributeName(SUPPLIER_SECS_ID);
		saveOrderToLeAttribute(context.orderToLe, simpleValueBean);
		return context;
	}

	private GscOrderAttributeValuesContext updateServiceAbbreviation(GscOrderAttributeValuesContext context) throws TclCommonException {

		orderGscRepository.findByOrderToLe(context.orderToLe).stream()
				.filter(orderGsc -> !GscConstants.DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName())
						&& !GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(orderGsc.getProductName())).forEach(orderGsc -> {
			orderGscDetailRepository.findByorderGsc(orderGsc).stream().forEach(orderGscDetail -> {

				List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = mstProductComponentRepository.findByNameAndStatus(GSC_CONFIG_PRODUCT_COMPONENT_TYPE.toUpperCase(),
						STATUS_ACTIVE).stream().findFirst()
						.flatMap(mstProductComponent -> orderProductComponentRepository
								.findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
										GSC_CONFIG_PRODUCT_COMPONENT_TYPE)
								.stream().findFirst())
						.map(orderProductComponent -> orderProductComponentsAttributeValueRepository
								.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
										productAttributeMasterRepository
												.findByNameAndStatus(GSC_SERVICE_ABBREVIATION, STATUS_ACTIVE).stream().findFirst().get())).get();

				orderProductComponentsAttributeValues.stream().forEach(orderProductComponentsAttributeValue -> {
					// queue call to service inventory
					// if value present, then replace or skip
					String requestPayload = context.orderToLe.getErfCusCustomerLegalEntityId() + COLON +
							context.orderToLe.getErfCusSpLegalEntityId() + COLON +
							orderGsc.getProductName();
					try {
						String response = (String) mqUtils.sendAndReceive(gscAbbreviation, requestPayload);
						String gscServiceAbbreviations = Utils.convertJsonToObject(response, String.class);
						//if(!CollectionUtils.isEmpty(gscServiceAbbreviations)) {
							//String value = gscServiceAbbreviations.stream().collect(Collectors.joining(","));
							orderProductComponentsAttributeValue.setAttributeValues(gscServiceAbbreviations);
							orderProductComponentsAttributeValue.setDisplayValue(gscServiceAbbreviations);
							orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
						//}
					} catch (TclCommonException e) {
						LOGGER.error("Error while getting service Abbreviation");
					}
				});
			});
		});

		return context;
	}

	/**
	 * Get Quote By ID
	 *
	 * @param quoteId
	 * @return
	 */
	private Try<Quote> getQuoteById(Integer quoteId) {
		return quoteRepository.findById(quoteId).map(Try::success).orElse(notFoundError(ExceptionConstants.QUOTE_EMPTY,
				String.format("Quote not found for given quote_id %s", quoteId)));
	}

	/**
	 * Create Order Bean
	 * <p>
	 * getOrder
	 *
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext getOrder(GscApproveQuoteContext context) {
		Quote quote = context.quote;
		context.order = Optional.ofNullable(orderRepository.findByQuoteAndStatus(quote, GscConstants.STATUS_ACTIVE))
				.orElseGet(() -> {
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
					order.setStartDate(new Date());
					order.setStatus(quote.getStatus());
					order.setOrderCode(context.quote.getQuoteCode());
					order.setQuoteCreatedBy(quote.getCreatedBy());
					order.setEngagementOptyId(quote.getEngagementOptyId());
					context.gscNewOrder = true;
					return order;
				});
		return context;
	}

	/**
	 * saveOrder
	 *
	 * @param context
	 * @return
	 */
	private Try<GscApproveQuoteContext> saveOrder(GscApproveQuoteContext context) {
		if ((context.gscNewOrder) && Objects.isNull(context.order.getId())) {
			context.order = orderRepository.save(context.order);
		}
		context.gscOrderDataBean = new GscOrderDataBean();
		context.gscOrderDataBean.setOrderId(context.order.getId());
		context.gscOrderDataBean.setCustomerId(context.order.getCustomer().getErfCusCustomerId());
		context.gscOrderDataBean.setQuoteId(context.order.getQuote().getId());
		context.gscOrderDataBean.setOrderCode(context.quote.getQuoteCode());
		context.gscOrderDataBean.setCreatedTime(context.order.getCreatedTime());
		context.gscOrderDataBean.setCreatedBy(context.order.getCreatedBy());
		return Try.success(context);
	}

	/**
	 * getQuoteToLe
	 *
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext getQuoteToLe(GscApproveQuoteContext context) {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(context.quote);
		context.quoteToLe = quoteToLes;
		return context;
	}

	/**
	 * createAndSaveOrderToLe
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext createAndSaveOrderToLe(GscApproveQuoteContext gscApproveQuoteContext) {
		Set<OrderToLe> orderToLes = new HashSet<>();
		Optional<OrderToLeProductFamily> gsipLeProductFamily = orderToLeRepository
				.findByOrder(gscApproveQuoteContext.order).stream().findFirst().map(orderToLe -> {
				    LOGGER.info("Order to le id {}", orderToLe.getId());
					MstProductFamily gsipProductFamily = mstProductFamilyRepository.findByNameAndStatus(
							GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
					return orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(orderToLe,
							gsipProductFamily);
				});
		if (!gsipLeProductFamily.isPresent()) {
			gscApproveQuoteContext.quoteToLe.forEach(quoteToLe -> {
                LOGGER.info("Quote To Le id {}", quoteToLe.getId());
				OrderToLe orderToLe = new OrderToLe();
				orderToLe.setFinalMrc(quoteToLe.getFinalMrc());
				orderToLe.setFinalNrc(quoteToLe.getFinalNrc());
				orderToLe.setFinalArc(quoteToLe.getFinalArc());
				orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
				orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
				orderToLe.setProposedArc(quoteToLe.getProposedArc());
				orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
				orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
				orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
				orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
				orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
				orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
				orderToLe.setOrder(gscApproveQuoteContext.order);
				orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
				orderToLe.setSourceSystem(quoteToLe.getSourceSystem());
				orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
				orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
				orderToLe.setClassification(quoteToLe.getClassification());
				orderToLe.setIsWholesale(quoteToLe.getIsWholesale());
				orderToLe = orderToLeRepository.save(orderToLe);
				orderToLe.setOrdersLeAttributeValues(
						createAndSaveOrderToLeAttribute(gscApproveQuoteContext, orderToLe, quoteToLe));
				orderToLe.setOrderToLeProductFamilies(
						createAndSaveOrderProductFamily(gscApproveQuoteContext, orderToLe, quoteToLe));
				orderToLes.add(orderToLe);
				gscApproveQuoteContext.gscOrderDataBean.setOrderLeId(orderToLe.getId());
			});
			gscApproveQuoteContext.orderToLe = orderToLes;
			gscApproveQuoteContext.gscOrderDataBean.setLegalEntities(getLegalEntities(gscApproveQuoteContext.order));
		}
		return gscApproveQuoteContext;
	}

	/**
	 * getQuoteToLeAttribute
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteToLeAttribute(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteLeAttributeValue = new ArrayList<>();
		gscApproveQuoteContext.quoteToLe.forEach(quoteToLe -> {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLe(quoteToLe);
			quoteToLe.setQuoteLeAttributeValues(new HashSet<>(quoteLeAttributeValues));
			if (!quoteLeAttributeValues.isEmpty()) {
				gscApproveQuoteContext.quoteLeAttributeValue.addAll(quoteLeAttributeValues);
			}
		});
		return gscApproveQuoteContext;
	}

	/**
	 * createAndSaveOrderToLeAttribute
	 *
	 * @param gscApproveQuoteContext
	 * @param orderToLe
	 * @param quoteToLe
	 * @return
	 */
	private Set<OrdersLeAttributeValue> createAndSaveOrderToLeAttribute(GscApproveQuoteContext gscApproveQuoteContext,
			OrderToLe orderToLe, QuoteToLe quoteToLe) {
		gscApproveQuoteContext.orderToLeAttributeValue = new HashSet<>();
		if (Objects.nonNull(gscApproveQuoteContext.quoteLeAttributeValue)
				&& !gscApproveQuoteContext.quoteLeAttributeValue.isEmpty()) {
			gscApproveQuoteContext.quoteLeAttributeValue.stream().filter(
					quoteLeAttributeValue -> quoteLeAttributeValue.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.forEach(quoteLeAttributeValue -> {
						OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
						ordersLeAttributeValue.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
						ordersLeAttributeValue.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
						ordersLeAttributeValue.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute());
						ordersLeAttributeValue.setOrderToLe(orderToLe);
						ordersLeAttributeValue = ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
						gscApproveQuoteContext.orderToLeAttributeValue.add(ordersLeAttributeValue);
					});
		}

		return gscApproveQuoteContext.orderToLeAttributeValue;
	}

	/**
	 * getProductFamily
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getProductFamily(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteToLeProductFamily = new ArrayList<>();
		gscApproveQuoteContext.quoteToLe.forEach(quoteToLe -> {
			MstProductFamily gsipProductFamily = mstProductFamilyRepository
					.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
			QuoteToLeProductFamily quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe, gsipProductFamily);
			LOGGER.info("Quote to le id {} and quote le product family {}", quoteToLe.getId(), quoteToLeProductFamilies.getId());
			gscApproveQuoteContext.quoteToLeProductFamily.add(quoteToLeProductFamilies);
		});
		return gscApproveQuoteContext;
	}

	/**
	 * createAndSaveOrderProductFamily
	 *
	 * @param gscApproveQuoteContext
	 * @param orderToLe
	 * @param quoteToLe
	 * @return
	 */
	private Set<OrderToLeProductFamily> createAndSaveOrderProductFamily(GscApproveQuoteContext gscApproveQuoteContext,
			OrderToLe orderToLe, QuoteToLe quoteToLe) {
		gscApproveQuoteContext.orderToLeProductFamily = new HashSet<>();
		if (!(gscApproveQuoteContext.quoteToLeProductFamily.isEmpty())) {
			gscApproveQuoteContext.quoteToLeProductFamily.stream().filter(
					quoteToLeProductFamily -> quoteToLeProductFamily.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.forEach(quoteToLeProductFamily -> {
						LOGGER.info("Order to le product family created w.r.t to quote le product family id {}", quoteToLeProductFamily.getId());
						OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
						orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
						orderToLeProductFamily.setOrderToLe(orderToLe);
						orderToLeProductFamily = orderToLeProductFamilyRepository.save(orderToLeProductFamily);
						LOGGER.info("Order to le product family id {}", orderToLeProductFamily.getId());
						orderToLeProductFamily.setOrderProductSolutions(createAndSaveOrderProductSolution(
								gscApproveQuoteContext, orderToLeProductFamily, orderToLe, quoteToLeProductFamily));
						gscApproveQuoteContext.gscOrderDataBean
								.setProductFamilyName(orderToLeProductFamily.getMstProductFamily().getName());
						gscApproveQuoteContext.orderToLeProductFamily.add(orderToLeProductFamily);
//						if (!userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
						if(!partnerService.quoteCreatedByPartner(quoteToLe.getQuote().getId())) {
							processEngagement(quoteToLe, quoteToLeProductFamily);
						}
//						}
					});

		}
		return gscApproveQuoteContext.orderToLeProductFamily;

	}

	/**
	 * Method to have an entry in Engagement table based on customer and
	 * productFamily
	 * 
	 * @param quoteToLe
	 * @param quoteToLeProductFamily
	 */
	private void processEngagement(QuoteToLe quoteToLe, QuoteToLeProductFamily quoteToLeProductFamily) {
		List<Engagement> engagements = engagementRepository.findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(
				quoteToLe.getQuote().getCustomer(), quoteToLe.getErfCusCustomerLegalEntityId(),
				quoteToLeProductFamily.getMstProductFamily(), CommonConstants.BACTIVE);
		if (engagements == null || engagements.isEmpty()) {
			Engagement engagement = new Engagement();
			engagement.setCustomer(quoteToLe.getQuote().getCustomer());
			engagement.setEngagementName(quoteToLeProductFamily.getMstProductFamily().getName() + CommonConstants.HYPHEN
					+ quoteToLe.getErfCusCustomerLegalEntityId());
			engagement.setErfCusCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
			engagement.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
			engagement.setStatus(CommonConstants.BACTIVE);
			engagement.setCreatedTime(new Date());
			engagementRepository.save(engagement);
		}
	}

	/**
	 * getQuoteProductSolution
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteProductSolution(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteProductSolutions = new ArrayList<>();
		gscApproveQuoteContext.quoteToLeProductFamily
				.forEach(productFamily -> {
                    gscApproveQuoteContext.quoteProductSolutions.addAll(productSolutionRepository.findByQuoteToLeProductFamily(productFamily));
                    LOGGER.info("product family id {}", productFamily.getId());
                    if (!CollectionUtils.isEmpty(gscApproveQuoteContext.quoteProductSolutions)) {
                        gscApproveQuoteContext.quoteProductSolutions.stream().forEach(productSolution -> {
                            LOGGER.info("Product solution id {}", productSolution.getId());
                        });
                    }
                });

        return gscApproveQuoteContext;
	}

	/**
	 * createAndSaveOrderProductSolution
	 *
	 * @param gscApproveQuoteContext
	 * @param orderToLeProductFamily
	 * @param orderToLe
	 * @param quoteToLeProductFamily
	 * @return
	 */
	private Set<OrderProductSolution> createAndSaveOrderProductSolution(GscApproveQuoteContext gscApproveQuoteContext,
			OrderToLeProductFamily orderToLeProductFamily, OrderToLe orderToLe,
			QuoteToLeProductFamily quoteToLeProductFamily) {
		List<GscOrderSolutionBean> gscOrderSolutionBeanList = new ArrayList<>();
		gscApproveQuoteContext.orderProductSolution = new HashSet<>();
		if (!(gscApproveQuoteContext.quoteProductSolutions.isEmpty())) {
			gscApproveQuoteContext.quoteProductSolutions.stream().filter(quoteProductSolution -> quoteProductSolution
					.getQuoteToLeProductFamily().getId().equals(quoteToLeProductFamily.getId()))
					.forEach(quoteProductSolution -> {
						LOGGER.info("Order to le product solution creation w.r.t to quote le product solution id {}", quoteProductSolution.getId());
						OrderProductSolution orderProductSolution = new OrderProductSolution();
						if (quoteProductSolution.getMstProductOffering() != null) {
							orderProductSolution.setMstProductOffering(quoteProductSolution.getMstProductOffering());
						}
						orderProductSolution.setOrderToLeProductFamily(orderToLeProductFamily);
						orderProductSolution.setSolutionCode(quoteProductSolution.getSolutionCode());
						orderProductSolution.setProductProfileData(quoteProductSolution.getProductProfileData());

						orderProductSolution = orderProductSolutionRepository.save(orderProductSolution);

						LOGGER.info("Order to le product solution id {}", orderProductSolution.getId());
						Set<OrderGsc> orderGscSet = createAndSaveOrderGsc(gscApproveQuoteContext, orderToLe,
								orderProductSolution, quoteToLeProductFamily.getQuoteToLe(), quoteProductSolution);

						Map<String, Object> orderProductProfileData = GscUtils.fromJson(
								orderProductSolution.getProductProfileData(), new TypeReference<Map<String, Object>>() {
								});

						GscOrderSolutionBean gscOrderSolutionBean = createGscOrderSolutionBean(orderProductSolution,
								orderProductProfileData, gscApproveQuoteContext, orderGscSet);
						gscOrderSolutionBeanList.add(gscOrderSolutionBean);
						gscApproveQuoteContext.orderProductSolution.add(orderProductSolution);
					});

		}
		gscApproveQuoteContext.gscOrderDataBean.setSolutions(gscOrderSolutionBeanList);
		String accessType = gscApproveQuoteContext.gscOrderDataBean.getSolutions().stream().findFirst()
				.map(GscOrderSolutionBean::getAccessType).orElse(null);
		gscApproveQuoteContext.gscOrderDataBean.setAccessType(accessType);
		return gscApproveQuoteContext.orderProductSolution;

	}

	/**
	 * createGscOrderSolutionBean
	 *
	 * @param orderProductSolution
	 * @param orderProductProfileData
	 * @param gscApproveQuoteContext
	 * @param orderGscSet
	 * @return
	 */
	private GscOrderSolutionBean createGscOrderSolutionBean(OrderProductSolution orderProductSolution,
			Map<String, Object> orderProductProfileData, GscApproveQuoteContext gscApproveQuoteContext,
			Set<OrderGsc> orderGscSet) {
		GscOrderSolutionBean gscOrderSolutionBean = new GscOrderSolutionBean();
		gscOrderSolutionBean.setSolutionId(orderProductSolution.getId());
		gscOrderSolutionBean.setSolutionCode(orderProductSolution.getSolutionCode());
		gscOrderSolutionBean.setOfferingName(orderProductSolution.getMstProductOffering().getProductName());
		gscOrderSolutionBean.setGscOrders(orderGscSet.stream().map(orderGsc -> {
			GscOrderBean gscOrderBean = GscOrderBean.fromOrderGsc(orderGsc);
			attributeValuesPopulator.populateComponentAttributeValues(gscOrderBean,
					() -> getOrderGscAttributes(orderGsc));
			return gscOrderBean;
		}).collect(Collectors.toList()));
		gscOrderSolutionBean.getGscOrders().stream().findFirst().ifPresent(ordergsc -> {
			gscOrderSolutionBean.setAccessType(ordergsc.getAccessType());
			gscOrderSolutionBean.setProductName(ordergsc.getProductName());
		});
		setOrderProductionBean(gscOrderSolutionBean.getGscOrders(),
				gscApproveQuoteContext.gscOrderProductComponentBean);
		return gscOrderSolutionBean;
	}

	/**
	 * Setting OrderProduction To Configuration Bean
	 *
	 * @param gscOrders
	 * @param gscOrderProductComponentBean
	 */
	private void setOrderProductionBean(List<GscOrderBean> gscOrders,
			List<GscOrderProductComponentBean> gscOrderProductComponentBean) {
		gscOrders.stream().forEach(gscOrder -> gscOrder.getConfigurations().stream()
				.forEach(gscConfiguration -> gscConfiguration.setProductComponents(gscOrderProductComponentBean.stream()
						.filter(gscProductBean -> gscProductBean.getReferenceId().equals(gscConfiguration.getId()))
						.collect(Collectors.toList()))));

	}

	/**
	 * getQuoteGsc
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteGsc(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteGsc = new ArrayList<>();
		gscApproveQuoteContext.quoteProductSolutions.forEach(productSolution -> {
			List<QuoteGsc> quoteGscList = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
					GscConstants.STATUS_ACTIVE);
			gscApproveQuoteContext.quoteGsc.addAll(quoteGscList);
		});
		return gscApproveQuoteContext;
	}

	/**
	 * createAndSaveOrderGsc
	 *
	 * @param gscApproveQuoteContext
	 * @param orderToLe
	 * @param orderProductSolution
	 * @param quoteToLe
	 * @param quoteProductSolution
	 * @return
	 */
	private Set<OrderGsc> createAndSaveOrderGsc(GscApproveQuoteContext gscApproveQuoteContext, OrderToLe orderToLe,
			OrderProductSolution orderProductSolution, QuoteToLe quoteToLe, ProductSolution quoteProductSolution) {
		gscApproveQuoteContext.orderGsc = new HashSet<>();
		if (!gscApproveQuoteContext.quoteGsc.isEmpty()) {
			gscApproveQuoteContext.quoteGsc.stream()
					.filter(quoteGsc -> quoteGsc.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.filter(quoteGsc -> quoteGsc.getProductSolution().getId().equals(quoteProductSolution.getId()))
					.forEach(quoteGsc -> {
						LOGGER.info("Order GSC creation w.r.t to quote gsc id {}", quoteGsc.getId());
						OrderGsc orderGsc = new OrderGsc();
						orderGsc.setAccessType(quoteGsc.getAccessType());
						orderGsc.setArc(quoteGsc.getArc());
						orderGsc.setCreatedBy(quoteGsc.getCreatedBy());
						orderGsc.setCreatedTime(quoteGsc.getCreatedTime());
						orderGsc.setImageUrl(quoteGsc.getImageUrl());
						orderGsc.setMrc(quoteGsc.getMrc());
						orderGsc.setNrc(quoteGsc.getNrc());
						orderGsc.setName(quoteGsc.getName());
						orderGsc.setStatus(quoteGsc.getStatus());
						orderGsc.setTcv(quoteGsc.getTcv());
						orderGsc.setProductName(quoteGsc.getProductName());
						orderGsc.setOrderToLe(orderToLe);
						orderGsc.setOrderProductSolution(orderProductSolution);
						orderGsc = orderGscRepository.save(orderGsc);
						LOGGER.info("Order GSC id {}", orderGsc.getId());
						orderGsc.setOrderGscSlas(createAndSaveOrderGscSla(gscApproveQuoteContext, orderGsc, quoteGsc));
						orderGsc.setOrderGscDetails(
								createAndSaveOrderGscDetails(gscApproveQuoteContext, orderGsc, quoteGsc));
						gscApproveQuoteContext.orderGsc.add(orderGsc);

					});
		}
		return gscApproveQuoteContext.orderGsc;
	}

	/**
	 * getQuoteGscDetails
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteGscDetails(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteGscDetail = gscApproveQuoteContext.quoteGsc.stream()
				.map(quoteGscDetailsRepository::findByQuoteGsc).flatMap(List::stream)
				.filter(quoteGscDetail -> !GSC_CFG_TYPE_REFERENCE.equalsIgnoreCase(quoteGscDetail.getType()))
				.collect(Collectors.toList());
		return gscApproveQuoteContext;
	}

	/**
	 * getQuoteProductComponent
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteProductComponent(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteProductComponents = new ArrayList<>();
		gscApproveQuoteContext.quoteGscDetail.forEach(quoteGscDetail -> {
			List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(quoteGscDetail.getId(),QuoteConstants.GSC.toString());
			gscApproveQuoteContext.quoteProductComponents.addAll(quoteProductComponents);
		});
		return gscApproveQuoteContext;
	}

	/**
	 * getQuoteProductComponentAttributeValues
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteProductComponentAttributeValues(
			GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteProductComponentsAttributeValues = new ArrayList<>();
		gscApproveQuoteContext.quoteProductComponents.forEach(quoteProductComponent -> {
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());
			gscApproveQuoteContext.quoteProductComponentsAttributeValues.addAll(quoteProductComponentsAttributeValues);
		});
		return gscApproveQuoteContext;
	}

	private ProductAttributeMaster getMasterAttribute(String attributeName) {
		return productAttributeMasterRepository.findByNameAndStatus(attributeName, GscConstants.STATUS_ACTIVE).stream()
				.findFirst().orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY,
						String.format("Attribute with name: %s not found", attributeName)));
	}

	private void createDefaultConfigurationAttributes(OrderProductComponent productComponent,
			OrderGscDetail orderGscDetail) {
		ProductAttributeMaster requestorDateAttributeMaster = getMasterAttribute(
				GscAttributeConstants.ATTR_REQUESTOR_DATE_FOR_SERVICE);
		OrderProductComponentsAttributeValue requestorDateAttribute = new OrderProductComponentsAttributeValue();
		requestorDateAttribute.setOrderProductComponent(productComponent);
		requestorDateAttribute.setProductAttributeMaster(requestorDateAttributeMaster);
		String requestorDateForServiceValue = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		requestorDateAttribute.setAttributeValues(requestorDateForServiceValue);
		requestorDateAttribute.setDisplayValue(requestorDateForServiceValue);
		orderProductComponentsAttributeValueRepository.save(requestorDateAttribute);

		ProductAttributeMaster expectedDeliveryDateAttributeMaster = getMasterAttribute(
				GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE);
		OrderProductComponentsAttributeValue expectedDeliveryDateAttribute = new OrderProductComponentsAttributeValue();
		expectedDeliveryDateAttribute.setOrderProductComponent(productComponent);
		expectedDeliveryDateAttribute.setProductAttributeMaster(expectedDeliveryDateAttributeMaster);
		String expectedDeliveryDateForValue = getGscExpectedDateForDelivery(orderGscDetail);
		if (!expectedDeliveryDateForValue.equalsIgnoreCase(GscConstants.BEST_EFFORT)) {
			expectedDeliveryDateForValue = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()
					.plusDays(Integer.valueOf(Optional.ofNullable(expectedDeliveryDateForValue).orElse("0"))));
		}
		expectedDeliveryDateAttribute.setAttributeValues(expectedDeliveryDateForValue);
		expectedDeliveryDateAttribute.setDisplayValue(expectedDeliveryDateForValue);
		orderProductComponentsAttributeValueRepository.save(expectedDeliveryDateAttribute);

		// O2C dependent basic attributes
		ProductAttributeMaster approveQuoteType = getMasterAttribute(APPROVE_QUOTE_TYPE);
		OrderProductComponentsAttributeValue approveQuoteTypeAttribute = new OrderProductComponentsAttributeValue();
		approveQuoteTypeAttribute.setOrderProductComponent(productComponent);
		approveQuoteTypeAttribute.setProductAttributeMaster(approveQuoteType);
		approveQuoteTypeAttribute.setAttributeValues("");
		approveQuoteTypeAttribute.setDisplayValue("");
		orderProductComponentsAttributeValueRepository.save(approveQuoteTypeAttribute);

		ProductAttributeMaster gscServiceAbbreviation = getMasterAttribute(GSC_SERVICE_ABBREVIATION);
		OrderProductComponentsAttributeValue gscServiceAbbreviationAttribute = new OrderProductComponentsAttributeValue();
		gscServiceAbbreviationAttribute.setOrderProductComponent(productComponent);
		gscServiceAbbreviationAttribute.setProductAttributeMaster(gscServiceAbbreviation);
		gscServiceAbbreviationAttribute.setAttributeValues(GSC_SERVICE_ABBREVIATION_TABLE.get(orderGscDetail.getOrderGsc().getProductName()));
		gscServiceAbbreviationAttribute.setDisplayValue(GSC_SERVICE_ABBREVIATION_TABLE.get(orderGscDetail.getOrderGsc().getProductName()));
		orderProductComponentsAttributeValueRepository.save(gscServiceAbbreviationAttribute);
	}

	private void createConfigurationProductComponent(OrderGscDetail orderGscDetail, Order order) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(orderGscDetail.getId(), GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
		if (orderProductComponents.isEmpty()) {
			OrderProductComponent component = new OrderProductComponent();
			component.setReferenceName(GSC_PRODUCT_NAME.toUpperCase());
			component.setType(GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
			component.setReferenceId(orderGscDetail.getId());
			List<MstProductComponent> mstProductComponents = mstProductComponentRepository
					.findByNameAndStatus(GSC_CONFIG_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
			component.setMstProductComponent(mstProductComponents.get(0));
			MstProductFamily productFamily = mstProductFamilyRepository
					.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
			component.setMstProductFamily(productFamily);
			component = orderProductComponentRepository.save(component);
			createDefaultConfigurationAttributes(component, orderGscDetail);
		}
	}

	/**
	 * createAndSaveOrderGscDetails
	 *
	 * @param gscApproveQuoteContext
	 * @param orderGsc
	 * @param quoteGsc
	 * @return
	 */
	private Set<OrderGscDetail> createAndSaveOrderGscDetails(GscApproveQuoteContext gscApproveQuoteContext,
			OrderGsc orderGsc, QuoteGsc quoteGsc) {
		gscApproveQuoteContext.orderGscDetail = new HashSet<>();
		Try<MstOrderSiteStage> mstOrderSiteStage = getMstOrderSiteStage(GscConstants.INTIAL_ORDER_CONFIGURATION_STAGE);
		Try<MstOrderSiteStatus> mstOrderSiteStatus = getMstOrderSiteStatus(
				GscConstants.INTIAL_ORDER_CONFIGURATION_STATUS);
		if (!gscApproveQuoteContext.quoteGscDetail.isEmpty()) {
			gscApproveQuoteContext.quoteGscDetail.stream()
					.filter(quoteGscDetail -> quoteGscDetail.getQuoteGsc().getId().equals(quoteGsc.getId()))
					.forEach(quoteGscDetail -> {
						OrderGscDetail orderGscDetail = new OrderGscDetail();
						orderGscDetail.setArc(quoteGscDetail.getArc());
						orderGscDetail.setCreatedBy(quoteGscDetail.getCreatedBy());
						orderGscDetail.setCreatedTime(quoteGscDetail.getCreatedTime());
						orderGscDetail.setDestType(quoteGscDetail.getDestType());
						orderGscDetail.setSrcType(quoteGscDetail.getSrcType());
						orderGscDetail.setDest(quoteGscDetail.getDest());
						orderGscDetail.setSrc(quoteGscDetail.getSrc());
						orderGscDetail.setMrc(quoteGscDetail.getMrc());
						orderGscDetail.setNrc(quoteGscDetail.getNrc());
						orderGscDetail.setOrderGsc(orderGsc);
						if (Objects.nonNull(mstOrderSiteStatus)) {
							orderGscDetail.setMstOrderSiteStatus(mstOrderSiteStatus.get());
						}
						if (Objects.nonNull(mstOrderSiteStage)) {
							orderGscDetail.setMstOrderSiteStage(mstOrderSiteStage.get());
						}
						orderGscDetail = orderGscDetailRepository.save(orderGscDetail);
						createOrderGscTfns(quoteGscDetail, orderGscDetail);
						createConfigurationProductComponent(orderGscDetail, gscApproveQuoteContext.order);
						gscApproveQuoteContext.orderGscDetail.add(orderGscDetail);
						gscApproveQuoteContext.gscOrderProductComponentBean
								.addAll(createAndSaveOrderProductComponent(gscApproveQuoteContext, orderGscDetail,
										quoteGscDetail).stream()
												.map(GscOrderProductComponentBean::fromOrderProductComponent)
												.collect(Collectors.toList()));
					});
		}
		return gscApproveQuoteContext.orderGscDetail;
	}

	private void createOrderGscTfns(QuoteGscDetail quoteGscDetail, OrderGscDetail orderGscDetail) {
		List<QuoteGscTfn> quoteGscTfns = quoteGscTfnRepository.findByQuoteGscDetail(quoteGscDetail);
		if (!CollectionUtils.isEmpty(quoteGscTfns)) {
			List<OrderGscTfn> orderGscTfns = quoteGscTfns.stream().map(quoteGscTfn -> {
				OrderGscTfn orderGscTfn = new OrderGscTfn();
				orderGscTfn.setOrderGscDetail(orderGscDetail);
				orderGscTfn.setPortedFrom(quoteGscTfn.getPortedFrom());
				orderGscTfn.setStatus(quoteGscTfn.getStatus());
				orderGscTfn.setIsPorted(quoteGscTfn.getIsPorted());
				orderGscTfn.setTfnNumber(quoteGscTfn.getTfnNumber());
				orderGscTfn.setCountryCode(quoteGscTfn.getCountryCode());
				orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
				orderGscTfn.setCreatedBy(Utils.getSource());
				return orderGscTfn;
			}).collect(Collectors.toList());
			orderGscTfnRepository.saveAll(orderGscTfns);
		}
	}

	/**
	 * createAndSaveOrderProductComponent
	 *
	 * @param gscApproveQuoteContext
	 * @param orderGscDetail
	 * @param quoteGscDetail
	 * @return
	 */
	private Set<OrderProductComponent> createAndSaveOrderProductComponent(GscApproveQuoteContext gscApproveQuoteContext,
			OrderGscDetail orderGscDetail, QuoteGscDetail quoteGscDetail) {
		gscApproveQuoteContext.orderProductComponents = new HashSet<>();
		if (!gscApproveQuoteContext.quoteProductComponents.isEmpty()) {
			gscApproveQuoteContext.quoteProductComponents.stream().filter(
					quoteProductComponents -> quoteProductComponents.getReferenceId().equals(quoteGscDetail.getId()))
					.forEach(quoteProductComponent -> {
						OrderProductComponent orderProductComponent = new OrderProductComponent();
						orderProductComponent.setReferenceId(orderGscDetail.getId());
						orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
						orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
						orderProductComponent.setType(quoteProductComponent.getType());
						orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
						orderProductComponent = orderProductComponentRepository.save(orderProductComponent);
						orderProductComponent.setOrderProductComponentsAttributeValues(
								createAndSaveOrderProductAttributeValues(gscApproveQuoteContext, orderProductComponent,
										quoteProductComponent));
						gscApproveQuoteContext.orderProductComponents.add(orderProductComponent);
					});
		}

		return gscApproveQuoteContext.orderProductComponents;
	}

	/**
	 * createAndSaveOrderProductAttributeValues
	 *
	 * @param gscApproveQuoteContext
	 * @param orderProductComponent
	 * @param quoteProductComponent
	 * @param quoteProductComponent
	 * @return
	 */
	private Set<OrderProductComponentsAttributeValue> createAndSaveOrderProductAttributeValues(
			GscApproveQuoteContext gscApproveQuoteContext, OrderProductComponent orderProductComponent,
			QuoteProductComponent quoteProductComponent) {
		gscApproveQuoteContext.orderProductComponentsAttributeValues = new HashSet<>();
		if (!gscApproveQuoteContext.quoteProductComponentsAttributeValues.isEmpty()) {
			gscApproveQuoteContext.quoteProductComponentsAttributeValues.stream()
					.filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue
							.getQuoteProductComponent().getId().equals(quoteProductComponent.getId()))
					.forEach(quoteProductComponentsAttributeValue -> {
						OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
						orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
						orderProductComponentsAttributeValue
								.setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
						orderProductComponentsAttributeValue
								.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
						orderProductComponentsAttributeValue.setProductAttributeMaster(
								quoteProductComponentsAttributeValue.getProductAttributeMaster());
						orderProductComponentsAttributeValue = orderProductComponentsAttributeValueRepository
								.save(orderProductComponentsAttributeValue);
						gscApproveQuoteContext.orderProductComponentsAttributeValues
								.add(orderProductComponentsAttributeValue);
					});
		}

		return gscApproveQuoteContext.orderProductComponentsAttributeValues;
	}

	/**
	 * getQuoteGscSla
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteGscSla(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteGscSla = new ArrayList<>();
		gscApproveQuoteContext.quoteGsc.forEach(quoteGsc -> {
			List<QuoteGscSla> quoteGscSla = quoteGscSlaRepository.findByQuoteGsc(quoteGsc);
			gscApproveQuoteContext.quoteGscSla.addAll(quoteGscSla);
		});
		return gscApproveQuoteContext;
	}

	/**
	 * createAndSaveOrderGscSla
	 *
	 * @param gscApproveQuoteContext
	 * @param orderGsc
	 * @param quoteGsc
	 * @return
	 */
	private Set<OrderGscSla> createAndSaveOrderGscSla(GscApproveQuoteContext gscApproveQuoteContext, OrderGsc orderGsc,
			QuoteGsc quoteGsc) {
		gscApproveQuoteContext.orderGscSla = new HashSet<>();
		if (!gscApproveQuoteContext.quoteGscSla.isEmpty()) {
			gscApproveQuoteContext.quoteGscSla.stream()
					.filter(quoteGscSla -> quoteGscSla.getQuoteGsc().getId().equals(quoteGsc.getId()))
					.forEach(quoteGscSla -> {
						OrderGscSla orderGscSla = new OrderGscSla();
						orderGscSla.setAttributeName(quoteGscSla.getAttributeName());
						orderGscSla.setAttributeValue(quoteGscSla.getAttributeValue());
						orderGscSla.setSlaMaster(quoteGscSla.getSlaMaster());
						orderGscSla.setOrderGsc(orderGsc);
						orderGscSla = orderGscSlaRepository.save(orderGscSla);
						gscApproveQuoteContext.orderGscSla.add(orderGscSla);

					});
		}
		return gscApproveQuoteContext.orderGscSla;
	}

	public List<GscOrderSolutionBean> updateProductComponentAttributesForSolutions(
			List<GscOrderSolutionBean> solutionBeans, Order order) {
		solutionBeans.stream()
				.map(gscOrderSolutionBean -> Optional
						.ofNullable(gscOrderSolutionBean.getGscOrders()).orElse(ImmutableList.of()))
				.flatMap(List::stream)
				.map(gscOrderSolutionBean -> Optional
						.ofNullable(gscOrderSolutionBean.getConfigurations()).orElse(ImmutableList.of()))
				.flatMap(List::stream)
				.map(gscOrderSolutionBean -> Optional.ofNullable(gscOrderSolutionBean.getProductComponents())
						.orElse(ImmutableList.of()))
				.flatMap(List::stream)
				.forEach(gscOrderProductComponentBean -> processProductComponent(gscOrderProductComponentBean, order));
		return solutionBeans;
	}

	/**
	 * Bulk update configuration attributes for multiple solutions and product
	 * components
	 * 
	 * @param orderId
	 * @param solutions
	 * @return
	 */
	@Transactional
	public List<GscOrderSolutionBean> updateProductComponentAttributesForSolutions(Integer orderId,
			List<GscOrderSolutionBean> solutions) {
		return Try.of(() -> {
			Objects.requireNonNull(orderId, "Order ID cannot be null");
			Preconditions.checkArgument(!CollectionUtils.isEmpty(solutions), "Solutions cannot be empty");
			return orderId;
		}).map(this::fetchOrderById).map(order -> updateProductComponentAttributesForSolutions(solutions, order)).get();
	}

	/**
	 * Update product component attributes
	 *
	 * @param orderId
	 * @param solutionId
	 * @param orderGscId
	 * @param configurationId
	 * @param orderProductComponentBean
	 * @return {@link GscOrderProductComponentBean}
	 */
	@Transactional
	public List<GscOrderProductComponentBean> updateOrderProductComponentAttributes(Integer orderId, Integer solutionId,
			Integer orderGscId, Integer configurationId, List<GscOrderProductComponentBean> orderProductComponentBean) {

		return Try.of(() -> {
			Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
			Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
			Objects.requireNonNull(orderGscId, ORDER_GSC_ID_NULL_MESSAGE);
			Objects.requireNonNull(configurationId, CONFIGURATION_ID_NULL_MESSAGE);
			Objects.requireNonNull(orderProductComponentBean, ORDER_PRODUCT_COMPONENT_NUll_MESSAGE);
			return orderId;
		}).flatMap(ordrId -> gscOrderDetailService.getOrderProductSolution(solutionId))
				.flatMap(orderProductSolution -> gscOrderDetailService.getOrderGsc(orderGscId))
				.flatMap(orderGsc -> gscOrderDetailService.getOrderGscDetail(configurationId))
				.map(orderGscDetail -> toContext(orderId, orderProductComponentBean, orderGscId))
				.flatMap(this::populateOrder).map(this::updateOrderProductComponentAttributes)
				.map(context -> context.componentBeans).get();
	}

	/**
	 * Create Context for Update Order Attributes
	 *
	 * @param orderId
	 * @param orderProductComponentBean
	 * @param orderGscId
	 * @return GscOrderProductComponentAttributeValueContext
	 */
	private GscOrderProductComponentAttributeValueContext toContext(Integer orderId,
			List<GscOrderProductComponentBean> orderProductComponentBean, Integer orderGscId) {
		GscOrderProductComponentAttributeValueContext context = new GscOrderProductComponentAttributeValueContext();
		context.componentBeans = orderProductComponentBean;
		context.orderId = orderId;
		context.orderGscId = orderGscId;
		return context;
	}

	/**
	 * Get Order based on order Id
	 *
	 * @param context
	 * @return
	 */
	private Try<GscOrderProductComponentAttributeValueContext> populateOrder(
			GscOrderProductComponentAttributeValueContext context) {
		return getOrder(context.orderId).map(order -> {
			context.order = order;
			return context;
		});
	}

	/**
	 * getOrder
	 *
	 * @param orderId
	 * @return
	 */
	private Try<Order> getOrder(Integer orderId) {
		Order order = orderRepository.findByIdAndStatus(orderId, GscConstants.STATUS_ACTIVE);
		return Optional.ofNullable(order).map(Try::success).orElse(
				notFoundError(ExceptionConstants.ORDER_EMPTY, String.format("Order with id: %s not found", orderId)));
	}

	private Try<OrderToLe> getOrderToLe(Integer orderToLeId) {
		return orderToLeRepository.findById(orderToLeId).map(Try::success).orElse(notFoundError(
				ExceptionConstants.ORDER_EMPTY, String.format("OrderToLe with id:%s not found", orderToLeId)));
	}

	/**
	 * update Order Product Component Attributes based on Attributes in Request
	 *
	 * @param context
	 * @return GscOrderProductComponentAttributeValueContext
	 */
	private GscOrderProductComponentAttributeValueContext updateOrderProductComponentAttributes(
			GscOrderProductComponentAttributeValueContext context) {
		context.componentBeans = context.componentBeans.stream()
				.map(componentBean -> processProductComponent(componentBean, context.order))
				.collect(Collectors.toList());
		return context;
	}

	/**
	 * Get Product Component for the product Bean Id
	 *
	 * @param gscOrderProductComponentBean
	 * @param order
	 * @return GscOrderProductComponentBean
	 */
	private GscOrderProductComponentBean processProductComponent(
			GscOrderProductComponentBean gscOrderProductComponentBean, Order order) {
		return orderProductComponentRepository.findById(gscOrderProductComponentBean.getId())
				.map(orderProductComponent -> processProductComponent(orderProductComponent,
						gscOrderProductComponentBean, order))
				.orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ORDER_PRODUCT_EMPTY, String.format(
						"Order product component with id: %s not found", gscOrderProductComponentBean.getId())));
	}

	public OrderProductComponentsAttributeValue saveOrderComponentAttributeValue(
			OrderProductComponent orderProductComponent, GscOrderProductComponentsAttributeSimpleValueBean bean) {
		Objects.requireNonNull(orderProductComponent);
		Objects.requireNonNull(bean);
		return productAttributeMasterRepository.findByNameAndStatus(bean.getAttributeName(), GscConstants.STATUS_ACTIVE)
				.stream().findFirst().map(productAttributeMaster -> {
					OrderProductComponentsAttributeValue attributeValue = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
									productAttributeMaster)
							.stream().findFirst().orElse(new OrderProductComponentsAttributeValue());
					attributeValue.setAttributeValues(bean.getAttributeValue());
					attributeValue.setDisplayValue(bean.getAttributeValue());
					attributeValue.setOrderProductComponent(orderProductComponent);
					attributeValue.setProductAttributeMaster(productAttributeMaster);
					return orderProductComponentsAttributeValueRepository.save(attributeValue);
				}).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY, String
						.format("Order Product component Attribute with name: %s not found", bean.getAttributeName())));
	}

	private List<OrderProductComponentsAttributeValue> handleArrayAttribute(OrderProductComponent productComponent,
			GscOrderProductComponentsAttributeArrayValueBean arrayAttribute, Order quote) {
		return productAttributeMasterRepository
				.findByNameAndStatus(arrayAttribute.getAttributeName(), GscConstants.STATUS_ACTIVE).stream().findFirst()
				.map(productAttributeMaster -> {
					List<OrderProductComponentsAttributeValue> values = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponentAndProductAttributeMaster(productComponent,
									productAttributeMaster);
					// delete all existing attributes for that name
					orderProductComponentsAttributeValueRepository.deleteAll(values);
					List<OrderProductComponentsAttributeValue> attributeValues = arrayAttribute.getAttributeValue()
							.stream()
							.map(attributeValue -> createOrderProductComponentsAttributeValues(arrayAttribute, quote,
									productComponent, productAttributeMaster, attributeValue))
							.collect(Collectors.toList());
					// save all new attributes
					return orderProductComponentsAttributeValueRepository.saveAll(attributeValues);
				})
				.orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY,
						String.format("Order product component attribute with name: %s not found",
								arrayAttribute.getAttributeName())));
	}

	private static OrderGscTfn createOrderGscTfn(String tfnNumber, String customerName, Timestamp timestamp,
			OrderGscDetail gscOrderDetail) {
		OrderGscTfn orderGscTfn = new OrderGscTfn();
		orderGscTfn.setOrderGscDetail(gscOrderDetail);
		orderGscTfn.setIsPorted(GscConstants.STATUS_INACTIVE);
		orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
		orderGscTfn.setTfnNumber(tfnNumber);
		orderGscTfn.setCreatedBy(customerName);
		orderGscTfn.setUpdatedBy(customerName);
		orderGscTfn.setCreatedTime(timestamp);
		orderGscTfn.setUpdatedTime(timestamp);
		return orderGscTfn;
	}

	private GscOrderProductComponentsAttributeValueBean handleTfnAttribute(Integer gscOrderDetailId,
			GscOrderProductComponentsAttributeValueBean tfnValueBean) {
		return orderGscDetailRepository.findById(gscOrderDetailId).map(orderGscDetail -> {
			List<OrderGscTfn> savedTfns = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail);
			orderGscTfnRepository.deleteAll(savedTfns);
			String customerName = Utils.getSource();
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			List<String> tfnNumbers = null;
			if (tfnValueBean instanceof GscOrderProductComponentsAttributeSimpleValueBean) {
				String tfnNumber = ((GscOrderProductComponentsAttributeSimpleValueBean) tfnValueBean)
						.getAttributeValue();
				tfnNumbers = Objects.isNull(tfnNumber) ? ImmutableList.of() : ImmutableList.of(tfnNumber);
			} else {
				tfnNumbers = Optional
						.ofNullable(
								((GscOrderProductComponentsAttributeArrayValueBean) tfnValueBean).getAttributeValue())
						.orElse(ImmutableList.of());
			}
			List<String> phoneNumberValidationErrors = tfnNumbers.stream().map(GscUtils::validatePhoneNumber)
					.filter(Validation::isInvalid).map(Validation::getError).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(phoneNumberValidationErrors)) {
				throw Exceptions.validationError(COMMON_ERROR, phoneNumberValidationErrors);
			}
			List<OrderGscTfn> newTfns = tfnNumbers.stream()
					.map(tfnNumber -> createOrderGscTfn(tfnNumber, customerName, now, orderGscDetail))
					.collect(Collectors.toList());
			orderGscTfnRepository.saveAll(newTfns);
			return tfnValueBean;
		}).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.GSC_ORDER_EMPTY,
				String.format("GSC Order detail with id: %s not found", gscOrderDetailId)));
	}

	/**
	 * Create Product Attribute values for each attributes
	 *
	 * @param orderProductComponent
	 * @param gscOrderProductComponentBean
	 * @param order
	 * @return GscOrderProductComponentBean
	 */
	public GscOrderProductComponentBean processProductComponent(OrderProductComponent orderProductComponent,
			GscOrderProductComponentBean gscOrderProductComponentBean, Order order) {
		List<GscOrderProductComponentsAttributeValueBean> normalAttributes = gscOrderProductComponentBean
				.getAttributes();
		Stream<OrderProductComponentsAttributeValue> simpleAttributes = normalAttributes.stream()
				.filter(attributeValue -> attributeValue instanceof GscOrderProductComponentsAttributeSimpleValueBean)
				.map(attribute -> (GscOrderProductComponentsAttributeSimpleValueBean) attribute)
				.map(attribute -> saveOrderComponentAttributeValue(orderProductComponent, attribute));
		Stream<OrderProductComponentsAttributeValue> arrayAttributes = normalAttributes.stream()
				.filter(attributeValue -> attributeValue instanceof GscOrderProductComponentsAttributeArrayValueBean)
				.map(attribute -> (GscOrderProductComponentsAttributeArrayValueBean) attribute)
				.map(attribute -> handleArrayAttribute(orderProductComponent, attribute, order)).flatMap(List::stream);
		List<GscOrderProductComponentsAttributeSimpleValueBean> savedAttributes = Stream
				.concat(simpleAttributes, arrayAttributes)
				.map(GscOrderProductComponentsAttributeSimpleValueBean::fromAttribute).collect(Collectors.toList());
		List<GscOrderProductComponentsAttributeValueBean> resultAttributes = groupAndConvertToValueBeans(
				savedAttributes);
		// handle tfn attribute values
//        gscOrderProductComponentBean
//                .getAttributes()
//                .stream()
//                .filter(bean -> GscConstants.TFN_ATTRIBUTE_NAME.equalsIgnoreCase(bean.getAttributeName()))
//                .findFirst()
//                .ifPresent(valueBean -> resultAttributes.add(
//                        handleTfnAttribute(orderProductComponent.getReferenceId(), valueBean)));
		gscOrderProductComponentBean.setAttributes(resultAttributes);
		return gscOrderProductComponentBean;
	}

	/**
	 * group And Convert To Value Beans
	 *
	 * @param simpleValueBeans
	 * @return List of GscOrderProductComponentsAttributeValueBean>
	 */
	private List<GscOrderProductComponentsAttributeValueBean> groupAndConvertToValueBeans(
			List<GscOrderProductComponentsAttributeSimpleValueBean> simpleValueBeans) {
		Map<String, List<GscOrderProductComponentsAttributeSimpleValueBean>> groupedAttributes = simpleValueBeans
				.stream()
				.collect(Collectors.groupingBy(GscOrderProductComponentsAttributeSimpleValueBean::getAttributeName));
		return groupedAttributes.values().stream().map(values -> {
			if (values.size() == 1) {
				return values.get(0);
			} else {
				GscOrderProductComponentsAttributeSimpleValueBean first = values.get(0);
				List<String> attributeValues = values.stream()
						.map(GscOrderProductComponentsAttributeSimpleValueBean::getAttributeValue)
						.collect(Collectors.toList());
				GscOrderProductComponentsAttributeArrayValueBean bean = new GscOrderProductComponentsAttributeArrayValueBean();
				bean.setAttributeId(first.getAttributeId());
				bean.setAttributeName(first.getAttributeName());
				bean.setDescription(first.getDescription());
				bean.setDisplayValue(first.getDisplayValue());
				bean.setAttributeValue(attributeValues);
				return bean;
			}
		}).collect(Collectors.toList());
	}

	/**
	 * create Order Product Components Attribute Values
	 *
	 * @param attributeValueBean
	 * @param order
	 * @param productComponent
	 * @param attributeMaster
	 * @return OrderProductComponentsAttributeValue
	 */
	private OrderProductComponentsAttributeValue createOrderProductComponentsAttributeValues(
			GscOrderProductComponentsAttributeValueBean attributeValueBean, Order order,
			OrderProductComponent productComponent, ProductAttributeMaster attributeMaster, String attributeValue) {
		OrderProductComponentsAttributeValue value = new OrderProductComponentsAttributeValue();
		value.setAttributeValues(attributeValue);
		value.setId(attributeValueBean.getAttributeId());
		value.setDisplayValue(attributeValueBean.getDisplayValue());
		value.setOrderProductComponent(productComponent);
		value.setProductAttributeMaster(attributeMaster);
		return value;
	}

	/**
	 * This file contains the GscApproveQuoteContext.java class.
	 *
	 * @author AVALLAPI
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	private static class GscApproveQuoteContext {
		User user;
		Quote quote;
		Order order;
		boolean gscNewOrder;
		List<QuoteToLe> quoteToLe;
		Set<OrderToLe> orderToLe;
		List<QuoteLeAttributeValue> quoteLeAttributeValue;
		Set<OrdersLeAttributeValue> orderToLeAttributeValue;
		List<QuoteToLeProductFamily> quoteToLeProductFamily;
		Set<OrderToLeProductFamily> orderToLeProductFamily;
		List<ProductSolution> quoteProductSolutions;
		Set<OrderProductSolution> orderProductSolution;
		List<QuoteGsc> quoteGsc;
		Set<OrderGsc> orderGsc;
		List<QuoteGscDetail> quoteGscDetail;
		Set<OrderGscDetail> orderGscDetail;
		List<QuoteGscSla> quoteGscSla;
		Set<OrderGscSla> orderGscSla;
		List<QuoteProductComponent> quoteProductComponents;
		Set<OrderProductComponent> orderProductComponents;
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues;
		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues;
		GscOrderDataBean gscOrderDataBean;
		List<GscOrderProductComponentBean> gscOrderProductComponentBean = new ArrayList<>();
		HttpServletResponse response;
        Date cofSignedDate;
		List<QuoteDelegation> quoteDelegate;
		String ipAddress;
		Map<String, String> cofObjectMapper;
		boolean isDocuSign;
		String approveQuoteType;
	}

	private static class GscOrderProductComponentAttributeValueContext {
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
		Integer orderId;
		Order order;
		Integer orderGscId;
		List<GscOrderProductComponentBean> componentBeans;
	}

	/**
	 * Get Order by orderId
	 *
	 * @param orderId
	 * @return {@link GscOrderDataBean}
	 */
	public Try<GscOrderDataBean> getGscOrderById(Integer orderId) {
		Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
		return getOrderData(orderId).flatMap(this::getOrderProductFamily);
	}

	private GscOrderDataBean populateDownstreamSystemStatus(GscOrderDataBean gscOrderDataBean) {
		List<ThirdPartyServiceJob> thirdPartyServiceJobs = thirdPartyServiceJobsRepository
				.findAllByRefIdAndServiceTypeInAndThirdPartySourceIn(gscOrderDataBean.getOrderCode(),
						ImmutableList.of(TIGER_SERVICE_TYPE_DOMESTIC_ORDER, TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES, TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND, WHOLESALE_ORDER),
						ImmutableList.of(ENTERPRISE_TIGER_ORDER.toString(), WHOLESALE_TIGER_ORDER.toString()));
		Optional<ThirdPartyServiceJob> orderJobOpt = thirdPartyServiceJobs.stream().findFirst();
		if (orderJobOpt.isPresent()) {
			ThirdPartyServiceJob orderJob = orderJobOpt.get();
			SfdcServiceStatus orderJobStatus = Objects.isNull(orderJob.getServiceStatus()) ? SfdcServiceStatus.NEW
					: SfdcServiceStatus.valueOf(orderJob.getServiceStatus());
			switch (orderJobStatus) {
			case STRUCK:
				gscOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_ERROR);
				break;
			case INPROGRESS:
				gscOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_IN_PROGRESS);
				break;
			case FAILURE:
			case NEW:
				gscOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_PENDING);
				break;
			default:
				gscOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_SUBMITTED);
			}
		} else {
			gscOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_PENDING);
		}
		return gscOrderDataBean;
	}

	/**
	 * Get Order and OrderToLe Data by orderId
	 * 
	 * @param orderId
	 * @return {@link GscOrderDataBean}
	 */
	private Try<GscOrderDataBean> getOrderData(Integer orderId) {
		GscOrderDataBean gscOrderDataBean = new GscOrderDataBean();
		Order order = orderRepository.findByIdAndStatus(orderId, GscConstants.STATUS_ACTIVE);
		if (Objects.nonNull(order)) {
			gscOrderDataBean.setOrderId(orderId);
			gscOrderDataBean.setQuoteId(Optional.ofNullable(order.getQuote()).orElse(new Quote()).getId());
			gscOrderDataBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
		} else {
			return notFoundError(ExceptionConstants.ORDER_EMPTY, String.format("Order with id: %s not found", orderId));
		}

		List<OrderToLe> orderToLe = orderToLeRepository.findByOrder(order);
		if (!orderToLe.isEmpty()) {
			if (orderToLe.size() > 1) {
				gscOrderDataBean.setOrderLeId(orderToLe.get(1).getId());
				gscOrderDataBean.setClassification(orderToLe.get(1).getClassification());
			} else {
				gscOrderDataBean.setOrderLeId(orderToLe.get(0).getId());
				gscOrderDataBean.setClassification(orderToLe.get(0).getClassification());
				gscOrderDataBean.setWholesale(orderToLe.get(0).getIsWholesale() == BACTIVE);
			}
		} else {
			return notFoundError(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
					String.format("OrderLeId not found for given quote id: %s", orderId));
		}

		/*if (getIsMultiMacdAttribute(gscOrderDataBean)) {
			return notFoundError(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					String.format("QuoteLeId does not have attribute to check multi macd or not for order id: %s", gscOrderDataBean.getOrderId()));
		}*/

		getIsMultiMacdAttribute(gscOrderDataBean);

		if(Objects.nonNull(gscOrderDataBean.getClassification()) && (PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(gscOrderDataBean.getClassification())
				|| (PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(gscOrderDataBean.getClassification())))){
			Opportunity opportunity=opportunityRepository.findByUuid(order.getOrderCode());
			if(Objects.nonNull(opportunity)){
				gscOrderDataBean.setPartnerOptyExpectedArc((opportunity.getExpectedMrc() * 12));
				gscOrderDataBean.setPartnerOptyExpectedNrc(opportunity.getExpectedNrc());
				gscOrderDataBean.setPartnerOptyExpectedCurrency(opportunity.getExpectedCurrency());
			}
		}
		gscOrderDataBean.setOrderId(orderId);
		gscOrderDataBean.setOrderCode(order.getOrderCode());
		gscOrderDataBean.setCreatedTime(order.getCreatedTime());
		gscOrderDataBean.setCreatedBy(order.getCreatedBy());
		gscOrderDataBean.setLegalEntities(getLegalEntities(order));
		gscOrderDataBean.setAttributes(getOrderToLeAttributes(orderId, gscOrderDataBean.getOrderLeId()));
		populateDownstreamSystemStatus(gscOrderDataBean);
		return Try.success(gscOrderDataBean);
	}

	/**
	 * Get Is Multi macd attribute against order
	 * @param orderDataBean
	 * @return
	 */
	private void getIsMultiMacdAttribute(GscOrderDataBean orderDataBean) {
		OrdersLeAttributeValue gscMultiMacdAttr = null;
		List<OrdersLeAttributeValue> gscMultiMacdAttributesList = ordersLeAttributeValueRepository.findByOrderIDAndMstOmsAttributeName(orderDataBean.getOrderId(), LeAttributesConstants.IS_GSC_MULTI_MACD);
		if (Objects.nonNull(gscMultiMacdAttributesList) && !gscMultiMacdAttributesList.isEmpty()){

				gscMultiMacdAttr = gscMultiMacdAttributesList.stream().findFirst().get();
		}
		if (Objects.nonNull(gscMultiMacdAttr)) {
			orderDataBean.setIsGscMultiMacd(gscMultiMacdAttr.getAttributeValue());
			LOGGER.info("Value of is multi macd attribute is {}", orderDataBean.getIsGscMultiMacd());
		} else {
			orderDataBean.setIsGscMultiMacd(NO);
		}
	}

	private GscOrderLeBean populateProductFamily(GscOrderLeBean orderLeBean, OrderToLe orderToLe) {
		List<OrderToLeProductFamily> productFamilies = Optional.ofNullable(orderToLe.getOrderToLeProductFamilies())
				.map(orderToLeProductFamilies -> (List)new ArrayList<>(orderToLeProductFamilies))
				.orElseGet(() -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe));
		if(!CollectionUtils.isEmpty(productFamilies)) {
			orderLeBean.setProductFamily(productFamilies.get(0)
					.getMstProductFamily()
					.getName());
		}
		return orderLeBean;
	}

	/**
	 * get Legal Entities by order
	 * 
	 * @param order
	 * @return {@link List<GscOrderLeBean>}
	 */
	private List<GscOrderLeBean> getLegalEntities(Order order) {
		return Optional.ofNullable(orderToLeRepository.findByOrder(order)).orElse(ImmutableList.of()).stream()
				.map(orderToLe -> populateProductFamily(GscOrderLeBean.fromOrderToLe(orderToLe), orderToLe))
				.collect(Collectors.toList());
	}

	/**
	 * get Order Product Family by orderId
	 * 
	 * @param gscOrderDataBean
	 * @return {@link GscOrderDataBean}
	 */
	private Try<GscOrderDataBean> getOrderProductFamily(GscOrderDataBean gscOrderDataBean) {
		MstProductFamily gsipProductFamily = mstProductFamilyRepository
				.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
		OrderToLe orderToLe = orderToLeRepository.findById(gscOrderDataBean.getOrderLeId()).orElse(null);
		OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
				.findByOrderToLeAndMstProductFamily(orderToLe, gsipProductFamily);
		if (Objects.nonNull(orderToLeProductFamily)) {
			gscOrderDataBean.setProductFamilyName(orderToLeProductFamily.getMstProductFamily().getName());
			List<OrderProductSolution> orderProductSolutions = orderProductSolutionRepository
					.findByOrderToLeProductFamily(orderToLeProductFamily);
			gscOrderDataBean.setSolutions(
					orderProductSolutions.stream().map(this::createOrderProductSolution).collect(Collectors.toList()));
			String accessType = gscOrderDataBean.getSolutions().stream().findFirst()
					.map(GscOrderSolutionBean::getAccessType).orElse(null);
			gscOrderDataBean.setAccessType(accessType);
			return Try.success(gscOrderDataBean);
		} else {
			return notFoundError(ExceptionConstants.ORDER_PRODUCT_EMPTY,
					String.format("Product with OrderLeId: %s not found", gscOrderDataBean.getOrderLeId()));
		}
	}

	/**
	 * Get GscOrderSolutionBean from OrderProductSolution
	 * 
	 * @param orderProductSolution
	 * @return {@link GscOrderSolutionBean}
	 */
	private GscOrderSolutionBean createOrderProductSolution(OrderProductSolution orderProductSolution) {
		GscOrderSolutionBean gscOrderSolutionBean = new GscOrderSolutionBean();
		gscOrderSolutionBean.setSolutionId(orderProductSolution.getId());
		gscOrderSolutionBean.setSolutionCode(orderProductSolution.getSolutionCode());
		gscOrderSolutionBean.setOfferingName(orderProductSolution.getMstProductOffering().getProductName());
		List<OrderGsc> orderGsc = orderGscRepository.findByorderProductSolutionAndStatus(orderProductSolution,
				GscConstants.STATUS_ACTIVE);
		orderGsc.stream().findFirst().ifPresent(ordergsc -> {
			gscOrderSolutionBean.setAccessType(ordergsc.getAccessType());
			gscOrderSolutionBean.setProductName(ordergsc.getProductName());
		});
		gscOrderSolutionBean.setGscOrders(orderGsc.stream().map(this::fromOrderGsc).collect(Collectors.toList()));
		return gscOrderSolutionBean;
	}

	/**
	 * Get GscOrderBean from OrderGsc
	 * 
	 * @param orderGsc
	 * @return {@link GscOrderBean}
	 */
	public GscOrderBean fromOrderGsc(OrderGsc orderGsc) {
		Objects.requireNonNull(orderGsc, "OrderGsc cannot be null");
		GscOrderBean gscOrderBean = GscOrderBean.fromOrderGsc(orderGsc);
		attributeValuesPopulator.populateComponentAttributeValues(gscOrderBean, () -> getOrderGscAttributes(orderGsc));
		gscOrderBean.setConfigurations(getGscOrderConfigurationBean(orderGsc));
		return gscOrderBean;
	}

	/**
	 * Get the attribute values of a configuration id as a map
	 * 
	 * @param configurationId
	 * @return
	 */
	public Map<String, String> getOrderProductComponentAttributeMap(Integer configurationId) {
		Objects.requireNonNull(configurationId, "Configuration Id cannot be null");
		return orderProductComponentRepository
				.findByReferenceIdAndType(configurationId, GSC_CONFIG_PRODUCT_COMPONENT_TYPE).stream()
				.findFirst()
				.map(orderProductComponent -> orderProductComponentsAttributeValueRepository
						.findByOrderProductComponent(orderProductComponent).stream()
						.collect(Collectors.toMap(attribute -> attribute.getProductAttributeMaster().getName(),
								OrderProductComponentsAttributeValue::getAttributeValues, (first, second) -> second,
								HashMap::new)))
				.orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.GSC_ORDER_EMPTY,
						String.format("Configuration with id: %s not found", configurationId)));
	}

	private OrderProductComponentsAttributeValue saveOrderConfigurationAttributeValue(
			OrderProductComponent orderProductComponent, ProductAttributeMaster productAttributeMaster,
			String attributeValue) {
		OrderProductComponentsAttributeValue savedAttributeValue = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent, productAttributeMaster)
				.stream().findFirst().map(value -> {
					value.setAttributeValues(attributeValue);
					value.setDisplayValue(attributeValue);
					return value;
				}).orElseGet(() -> {
					OrderProductComponentsAttributeValue value = new OrderProductComponentsAttributeValue();
					value.setAttributeValues(attributeValue);
					value.setDisplayValue(attributeValue);
					value.setProductAttributeMaster(productAttributeMaster);
					value.setOrderProductComponent(orderProductComponent);
					return value;
				});
		return orderProductComponentsAttributeValueRepository.save(savedAttributeValue);
	}

	/**
	 * Persist given map of key-value pairs which are attributes of a given
	 * configuration id
	 * 
	 * @param attributeMap
	 * @param configurationId
	 * @return
	 */
	public Map<String, String> saveOrderProductComponentAttributeMap(Map<String, String> attributeMap,
			Integer configurationId) {
		Objects.requireNonNull(attributeMap, "Configuration attribute map cannot be null");
		Objects.requireNonNull(configurationId, "Configuration Id cannot be null");
		return orderProductComponentRepository
				.findByReferenceIdAndType(configurationId, GSC_CONFIG_PRODUCT_COMPONENT_TYPE).stream()
				.findFirst().map(orderProductComponent -> {
					attributeMap.forEach((attributeName, attributeValue) -> productAttributeMasterRepository
							.findByNameAndStatus(attributeName, GscConstants.STATUS_ACTIVE).stream().findFirst()
							.map(productAttributeMaster -> saveOrderConfigurationAttributeValue(orderProductComponent,
									productAttributeMaster, attributeValue))
							.orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY,
									String.format("Configuration attribute with name: %s not found", attributeName))));
					return attributeMap;
				}).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.GSC_ORDER_EMPTY,
						String.format("Order product configuration component with id: %s not found", configurationId)));
	}

	/**
	 * Get List of GscOrderConfigurationBean from OrderGsc
	 * 
	 * @param orderGsc
	 * @return {@link List<GscOrderConfigurationBean>}
	 */
	private List<GscOrderConfigurationBean> getGscOrderConfigurationBean(OrderGsc orderGsc) {
		return orderGscDetailRepository.findByorderGsc(orderGsc).stream()
				.map(GscOrderConfigurationBean::fromOrderGscDetail)
				.map(gscOrderConfigurationBean -> attributeValuesPopulator.populateComponentAttributeValues(
						gscOrderConfigurationBean,
						() -> getOrderProductComponentAttributeMap(gscOrderConfigurationBean.getId())))
				.map(this::populateProductComponentBean).collect(Collectors.toList());
	}

	/**
	 * Get GscOrderConfigurationBean from OrderGscDetail
	 * 
	 * @param gscOrderConfigurationBean
	 * @return {@link GscOrderConfigurationBean}
	 */
	private GscOrderConfigurationBean populateProductComponentBean(
			GscOrderConfigurationBean gscOrderConfigurationBean) {

		List<OrderProductComponent> orderProductComponents = getOrderProductComponent(gscOrderConfigurationBean.getId())
				.get();
		List<GscOrderProductComponentBean> gscOrderProductComponentBeans = orderProductComponents.stream()
				.map(orderProductComponent -> {
					GscOrderProductComponentBean gscOrderProductComponentBean = GscOrderProductComponentBean
							.fromOrderProductComponent(orderProductComponent);
					List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponent(orderProductComponent);
					List<GscOrderProductComponentsAttributeSimpleValueBean> attributes = orderProductComponentsAttributeValues
							.stream().map(GscOrderProductComponentsAttributeSimpleValueBean::fromAttribute)
							.collect(Collectors.toList());
					List<GscOrderProductComponentsAttributeValueBean> resultAttributes = groupAndConvertToValueBeans(
							attributes);
					gscOrderProductComponentBean.setAttributes(resultAttributes);
					return gscOrderProductComponentBean;
				}).collect(Collectors.toList());
		gscOrderConfigurationBean.setProductComponents(gscOrderProductComponentBeans);
		return gscOrderConfigurationBean;
	}

	/**
	 * get List of OrderProductComponent by OrderGscDetailId
	 * 
	 * @param orderGscDetailId
	 * @return {@link List<OrderProductComponent>}
	 */
	private Try<List<OrderProductComponent>> getOrderProductComponent(Integer orderGscDetailId) {

		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(orderGscDetailId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		if (!orderProductComponents.isEmpty()) {
			return Try.success(orderProductComponents);
		} else {
			return notFoundError(ExceptionConstants.ORDER_PRODUCT_EMPTY,
					String.format("Order Product components with reference id: %s not found", orderGscDetailId));
		}
	}

	/**
	 * GscOrderAttributeValuesContext
	 *
	 * @author VISHESH AWASTHI
	 *
	 */
	private static class GscOrderAttributeValuesContext {
		Integer orderId;
		Order order;
		Integer orderToLeId;
		OrderToLe orderToLe;
		OrderProductComponent orderProductComponent;
		List<GscOrderProductComponentsAttributeValueBean> attributes;
	}

	/**
	 * List the order le attribute for OrderLeId
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @return {@link GscOrderAttributesBean}
	 */
	public GscOrderAttributesBean getOrderToLeAttributes(Integer orderId, Integer orderToLeId) {
		Objects.requireNonNull(orderId);
		Objects.requireNonNull(orderToLeId);
		return createOrderAttributeContext(orderId, orderToLeId, null).flatMap(this::populateOrderToLe)
				.map(this::fetchOrderToLeAttributeValues).map(context -> {
					GscOrderAttributesBean bean = new GscOrderAttributesBean();
					bean.setOrderToLeId(orderToLeId);
					bean.setAttributes(context.attributes);
					return bean;
				}).get();

	}

	/**
	 * Create context for GscOrderAttributeValuesContext
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param attributes
	 * @return GscOrderAttributeValuesContext
	 */

	private Try<GscOrderAttributeValuesContext> createOrderAttributeContext(Integer orderId, Integer orderToLeId,
			List<GscOrderProductComponentsAttributeValueBean> attributes) {
		return getOrder(orderId).map(order -> {
			GscOrderAttributeValuesContext context = new GscOrderAttributeValuesContext();
			context.orderId = orderId;
			context.order = order;
			context.orderToLeId = orderToLeId;
			context.attributes = attributes;
			return context;
		});

	}

	/**
	 * Populates orderToLe based on OrderleId
	 *
	 * @param context
	 * @return
	 */
	private Try<GscOrderAttributeValuesContext> populateOrderToLe(GscOrderAttributeValuesContext context) {
		return getOrderToLe(context.orderToLeId).map(orderToLe -> {
			context.orderToLe = orderToLe;
			return context;
		});
	}

	/**
	 * fetches OrderLeAttrValus and maps it to
	 * GscOrderProductComponentsAttributeSimpleValueBean
	 *
	 * @param context
	 * @return
	 */
	private GscOrderAttributeValuesContext fetchOrderToLeAttributeValues(GscOrderAttributeValuesContext context) {
		List<GscOrderProductComponentsAttributeSimpleValueBean> attributeValues = ordersLeAttributeValueRepository
				.findByOrderToLe(context.orderToLe).stream()
				.map(GscOrderProductComponentsAttributeSimpleValueBean::formOrderLeAttributeValue)
				.collect(Collectors.toList());
		context.attributes = groupAndConvertToValueBeans(attributeValues);
		return context;
	}

	/**
	 * Fetch the order object by ID or fail by throwing exception is order not found
	 * 
	 * @param orderId
	 * @return
	 */
	public Order fetchOrderById(Integer orderId) {
		Objects.requireNonNull(orderId, "Order ID cannot be null");
		return Optional.ofNullable(orderRepository.findByIdAndStatus(orderId, GscConstants.STATUS_ACTIVE))
				.orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ORDER_EMPTY,
						String.format("Order with id : %s not found", orderId)));
	}

	/**
	 * Fetch Order GSC by ID or fail by throwing exception if order gsc not found
	 * 
	 * @param orderGscId
	 * @return
	 */
	public OrderGsc fetchOrderGscById(Integer orderGscId) {
		Objects.requireNonNull(orderGscId, "Order GSC ID cannot be null");
		return orderGscRepository.findById(orderGscId)
				.orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ORDER_EMPTY,
						String.format("Order GSC with id : %s not found", orderGscId)));
	}

	/**
	 * save and update OrderToLe attr values
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param attributes
	 * @return
	 */
	@Transactional
	public GscOrderAttributesBean saveOrderToLeAttributes(Integer orderId, Integer orderToLeId,
			List<GscOrderProductComponentsAttributeValueBean> attributes) {
		Objects.requireNonNull(orderId);
		Objects.requireNonNull(orderToLeId);
		Objects.requireNonNull(attributes);
		return createOrderAttributeContext(orderId, orderToLeId, attributes)
				.flatMap(this::populateOrderToLe)
				.map(this::saveOrderToLeAttributes)
				//.map(this::updateCustomerSecsId)
				//.mapTry(this::updateSupplierSecsId)
				.map(context -> {
					GscOrderAttributesBean bean = new GscOrderAttributesBean();
					bean.setOrderToLeId(orderToLeId);
					bean.setAttributes(attributes);
					return bean;
				}).get();
	}

	/**
	 * save and update OrderToLe attr values
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param attributes
	 * @return
	 */
	@Transactional
	public GscOrderAttributesBean saveOrderToLeAttributesForO2C(Integer orderId, Integer orderToLeId) {
		Objects.requireNonNull(orderId);
		Objects.requireNonNull(orderToLeId);
		//Objects.requireNonNull(attributes);
		return createOrderAttributeContext(orderId, orderToLeId, null)
				.flatMap(this::populateOrderToLe)
				//.map(this::saveOrderToLeAttributes)
				.map(this::updateCustomerSecsId)
				.mapTry(this::updateSupplierSecsId)
				.mapTry(this::updateServiceAbbreviation)
				.map(context -> {
					GscOrderAttributesBean bean = new GscOrderAttributesBean();
					bean.setOrderToLeId(orderToLeId);
					bean.setAttributes(null);
					return bean;
				}).get();
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	private GscOrderAttributeValuesContext saveOrderToLeAttributes(GscOrderAttributeValuesContext context) {
		context.attributes = saveOrderToLeAttributes(context.order, context.orderToLe, context.attributes);
		return context;
	}

	/**
	 *
	 * @param order
	 * @param orderToLe
	 * @param attributes
	 * @return
	 */
	private List<GscOrderProductComponentsAttributeValueBean> saveOrderToLeAttributes(Order order, OrderToLe orderToLe,
			List<GscOrderProductComponentsAttributeValueBean> attributes) {
		List<GscOrderProductComponentsAttributeSimpleValueBean> simpleAttributes = attributes.stream()
				.filter(attribute -> attribute instanceof GscOrderProductComponentsAttributeSimpleValueBean)
				.map(attribute -> (GscOrderProductComponentsAttributeSimpleValueBean) attribute)
				.map(attribute -> saveOrderToLeAttribute(orderToLe, attribute))
				.map(GscOrderProductComponentsAttributeSimpleValueBean::formOrderLeAttributeValue)
				.collect(Collectors.toList());
		List<GscOrderProductComponentsAttributeSimpleValueBean> arrayAttributes = attributes.stream()
				.filter(attribute -> attribute instanceof GscOrderProductComponentsAttributeArrayValueBean)
				.map(attribute -> (GscOrderProductComponentsAttributeArrayValueBean) attribute)
				.map(attribute -> handleArrayAttribute(attribute, order, orderToLe)).flatMap(List::stream)
				.map(GscOrderProductComponentsAttributeSimpleValueBean::formOrderLeAttributeValue)
				.collect(Collectors.toList());
		simpleAttributes.addAll(arrayAttributes);
		return groupAndConvertToValueBeans(simpleAttributes);
	}

	/**
	 * save the attributes
	 *
	 * @param orderToLe
	 * @param attribute
	 * @return
	 */
	private OrdersLeAttributeValue saveOrderToLeAttribute(OrderToLe orderToLe,
			GscOrderProductComponentsAttributeSimpleValueBean attribute) {
		return mstOmsAttributeRepository.findByNameAndIsActive(attribute.getAttributeName(), GscConstants.STATUS_ACTIVE)
				.stream().findFirst().map(mstOmsAttribute -> {
					OrdersLeAttributeValue ordersLeAttributeValue = ordersLeAttributeValueRepository
							.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe).stream().findFirst()
							.orElse(new OrdersLeAttributeValue());
					ordersLeAttributeValue.setAttributeValue(attribute.getAttributeValue());
					ordersLeAttributeValue.setDisplayValue(attribute.getDisplayValue());
					ordersLeAttributeValue.setOrderToLe(orderToLe);
					ordersLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
					return ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
				}).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY,
						String.format("Order LE Attribute with name: %s not found", attribute.getAttributeName())));
	}

	/**
	 * this method handles the multiple values for attribute
	 *
	 * @param arrayAttribute
	 * @param order
	 * @param orderToLe
	 * @return
	 */
	private List<OrdersLeAttributeValue> handleArrayAttribute(
			GscOrderProductComponentsAttributeArrayValueBean arrayAttribute, Order order, OrderToLe orderToLe) {
		return mstOmsAttributeRepository
				.findByNameAndIsActive(arrayAttribute.getAttributeName(), GscConstants.STATUS_ACTIVE).stream()
				.findFirst().map(mstOmsAttribute -> {
					Set<OrdersLeAttributeValue> values = ordersLeAttributeValueRepository
							.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe);
					// delete all existing attributes for that name
					ordersLeAttributeValueRepository.deleteAll(values);
					List<OrdersLeAttributeValue> attributeValues = arrayAttribute.getAttributeValue().stream()
							.map(attributeValue -> createOrderLeAttributeValue(arrayAttribute, order, orderToLe,
									mstOmsAttribute, attributeValue))
							.collect(Collectors.toList());
					// save all new attributes
					return ordersLeAttributeValueRepository.saveAll(attributeValues);
				}).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY, String
						.format("Order LE Attribute with name: %s not found", arrayAttribute.getAttributeName())));
	}

	/**
	 *
	 * @param arrayAttribute
	 * @param order
	 * @param orderToLe
	 * @param mstOmsAttribute
	 * @param attributeValue
	 * @return
	 */
	private OrdersLeAttributeValue createOrderLeAttributeValue(
			GscOrderProductComponentsAttributeArrayValueBean arrayAttribute, Order order, OrderToLe orderToLe,
			MstOmsAttribute mstOmsAttribute, String attributeValue) {
		OrdersLeAttributeValue value = new OrdersLeAttributeValue();
		value.setAttributeValue(attributeValue);
		value.setDisplayValue(arrayAttribute.getDisplayValue());
		value.setMstOmsAttribute(mstOmsAttribute);
		value.setOrderToLe(orderToLe);
		value.setId(arrayAttribute.getAttributeId());
		return value;
	}

	/**
	 * return order attributes
	 * 
	 * @param orderId
	 * @return {@link GscOrderAttributesBean}
	 */
	@Transactional
	public GscOrderAttributesBean getOrderAttributes(Integer orderId) {
		Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
		return createOrderAttributeContext(orderId, 0, ImmutableList.of()).map(this::populateOrderProductComponent)
				.map(this::populateOrderProductComponentAttributes).map(context -> {
					GscOrderAttributesBean bean = new GscOrderAttributesBean();
					bean.setAttributes(context.attributes);
					bean.setOrderToLeId(context.orderId);
					return bean;
				}).get();
	}

	/**
	 * fetched orderProductComponents based on order id
	 * 
	 * @param context
	 * @return
	 */
	private GscOrderAttributeValuesContext populateOrderProductComponent(GscOrderAttributeValuesContext context) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(context.orderId, GSC_COMMON_PRODUCT_COMPONENT_TYPE);
		String productName = GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase();
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(
				GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), GscConstants.STATUS_ACTIVE);
		context.orderProductComponent = orderProductComponents.stream().findFirst()
				.orElseGet(() -> this.saveOrderProductComponent(context.orderId, productName, context.order,
						mstProductFamily, GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase()));
		return context;
	}

	/**
	 * saves orderProductComponent
	 * 
	 * @param referenceId
	 * @param productName
	 * @param order
	 * @param mstProductFamily
	 * @param componentType
	 * @return
	 */
	private OrderProductComponent saveOrderProductComponent(Integer referenceId, String productName, Order order,
			MstProductFamily mstProductFamily, String componentType) {
		Objects.requireNonNull(referenceId, "Reference ID cannot be null");
		Objects.requireNonNull(productName, PRODUCT_NAME_NULL_MESSAGE);
		Objects.requireNonNull(order, ORDER_NULL_MESSAGE);
		Objects.requireNonNull(mstProductFamily, "Master product family cannot be null");
		Objects.requireNonNull(componentType, "Component Type cannot be null");
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(productName);
		OrderProductComponent orderProductComponent = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(referenceId, mstProductComponent, componentType)
				.stream().findFirst().orElse(new OrderProductComponent());
		orderProductComponent.setType(componentType);
		orderProductComponent.setMstProductComponent(mstProductComponent);
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponent.setReferenceId(referenceId);
		boolean isCreate = Objects.isNull(orderProductComponent.getId());
		OrderProductComponent savedOrderProductComponent = orderProductComponentRepository.save(orderProductComponent);
		if (isCreate) {
			List<GscOrderProductComponentsAttributeValueBean> defaultAttributes = getDefaultAttributes(
					mstProductComponent.getName());
			if (!defaultAttributes.isEmpty()) {
				GscOrderProductComponentBean componentBean = GscOrderProductComponentBean
						.fromOrderProductComponentWithoutAttributes(savedOrderProductComponent);
				componentBean.setAttributes(defaultAttributes);
				processProductComponent(componentBean, order);
				return savedOrderProductComponent;
			}
		}
		return savedOrderProductComponent;
	}

	/**
	 * group attributes and set them to context
	 * 
	 * @param context
	 * @return
	 */
	private GscOrderAttributeValuesContext populateOrderProductComponentAttributes(
			GscOrderAttributeValuesContext context) {
		List<GscOrderProductComponentsAttributeSimpleValueBean> attributes = Optional
				.ofNullable(context.orderProductComponent.getOrderProductComponentsAttributeValues())
				.orElse(ImmutableSet.of()).stream()
				.map(GscOrderProductComponentsAttributeSimpleValueBean::fromAttribute).collect(Collectors.toList());
		context.attributes = groupAndConvertToValueBeans(attributes);
		return context;
	}

	/**
	 * saves order Attributes
	 * 
	 * @param orderId
	 * @param attributes
	 * @return
	 */
	@Transactional
	public List<GscOrderProductComponentsAttributeValueBean> saveOrderAttributes(Integer orderId,
			List<GscOrderProductComponentsAttributeValueBean> attributes) {
		Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
		Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);
		return createOrderAttributeContext(orderId, 0, attributes).map(this::populateOrderProductComponent)
				.map(this::saveOrderAttributes).map(context -> context.attributes).get();
	}

	/**
	 * save the attributes and set them to context
	 * 
	 * @param context
	 * @return
	 */
	private GscOrderAttributeValuesContext saveOrderAttributes(GscOrderAttributeValuesContext context) {
		GscOrderProductComponentBean componentBean = GscOrderProductComponentBean
				.fromOrderProductComponentWithoutAttributes(context.orderProductComponent);
		componentBean.setAttributes(context.attributes);
		context.attributes = processProductComponent(context.orderProductComponent, componentBean, context.order)
				.getAttributes();
		return context;
	}

	/**
	 * gets the list of default attributes based on component name
	 * 
	 * @param productComponentName
	 * @return
	 */
	private List<GscOrderProductComponentsAttributeValueBean> getDefaultAttributes(String productComponentName) {
		URL url = Resources.getResource("attributes/product_component_default_attributes.json");
		CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
		try (Reader jsonReader = charSource.openStream()) {
			Map<String, List<GscOrderProductComponentsAttributeValueBean>> defaultAttributesMap = new ObjectMapper()
					.readValue(jsonReader,
							new TypeReference<Map<String, List<GscOrderProductComponentsAttributeValueBean>>>() {
							});
			return defaultAttributesMap.getOrDefault(productComponentName, ImmutableList.of());
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return ImmutableList.of();
	}

	/**
	 * Context class for OrderStatusStageUpdate
	 *
	 * @author VISHESH AWASTHI
	 *
	 */
	public static class OrderStatusStageUpdateContext {
		Integer configurationId;
		MstOrderSiteStage mstOrderSiteStage;
		MstOrderSiteStatus mstOrderSiteStatus;
		GscOrderStatusStageUpdate gscOrderStatusStageUpdate;
	}

	/**
	 * fetch site stage
	 *
	 * @param stage
	 * @return
	 */
	private Try<MstOrderSiteStage> getMstOrderSiteStage(String stage) {
		MstOrderSiteStage mstOrderSiteStage = mstOrderSiteStageRepository.findByName(stage);
		return Optional.ofNullable(mstOrderSiteStage).map(Try::success)
				.orElse(notFoundError(ExceptionConstants.MST_ORDER_SITE_STAGE_EMPTY,
						String.format("Order site stage with name:%s not found", stage)));
	}

	/**
	 * fetch site status
	 *
	 * @param status
	 * @return
	 */
	public Try<MstOrderSiteStatus> getMstOrderSiteStatus(String status) {
		MstOrderSiteStatus mstOrderSiteStatus = mstOrderSiteStatusRepository.findByName(status);
		return Optional.ofNullable(mstOrderSiteStatus).map(Try::success)
				.orElse(notFoundError(ExceptionConstants.MST_ORDER_SITE_STATUS_EMPTY,
						String.format("Order site status with name:%s not found", status)));
	}

	/**
	 * populate OrderStatusStageUpdateContext with MstOrderSiteStage
	 * 
	 * @param context
	 * @return
	 */
	private Try<OrderStatusStageUpdateContext> populateMstOrderSiteStage(OrderStatusStageUpdateContext context) {
		if (Objects.isNull(context.gscOrderStatusStageUpdate.getConfigurationStageName()))
			return Try.success(context);
		return getMstOrderSiteStage(context.gscOrderStatusStageUpdate.getConfigurationStageName()).map(stage -> {
			context.mstOrderSiteStage = stage;
			return context;
		});
	}

	/**
	 * populate OrderStatusStageUpdateContext with MstOrderSiteStatus
	 * 
	 * @param context
	 * @return
	 */
	private Try<OrderStatusStageUpdateContext> populateMstOrderSiteStatus(OrderStatusStageUpdateContext context) {
		String configurationStatus = context.gscOrderStatusStageUpdate.getConfigurationStatusName();
		return getMstOrderSiteStatus(configurationStatus).map(status -> {
			context.mstOrderSiteStatus = status;
			return context;
		});
	}

	/**
	 * update order status and sub stages
	 *
	 * @author VISHESH AWASTHI
	 * @param configurationId
	 * @param request
	 * @return
	 */
	@Transactional
	public GscOrderStatusStageUpdate updateOrderConfigurationStageStatus(Integer configurationId,
			GscOrderStatusStageUpdate request) {
		Objects.requireNonNull(configurationId);
		Objects.requireNonNull(request);
		return Try.success(createOrderStatusStageUpdateContext(configurationId, request))
				.flatMap(this::populateMstOrderSiteStatus)
				.flatMap(this::populateMstOrderSiteStage)
				.map(this::updateConfigurationStatusStage)
				.map(context -> {
					return context.gscOrderStatusStageUpdate;
				}).get();
	}

	/**
	 * creates context for OrderStatusStageUpdateContext
	 * 
	 * @param configurationId
	 * @param request
	 * @return
	 */
	private OrderStatusStageUpdateContext createOrderStatusStageUpdateContext(Integer configurationId,
			GscOrderStatusStageUpdate request) {
		OrderStatusStageUpdateContext context = new OrderStatusStageUpdateContext();
		context.configurationId = configurationId;
		context.gscOrderStatusStageUpdate = request;
		return context;
	}

	/**
	 * save the order status and stage
	 * 
	 * @param context
	 * @return
	 */
	private OrderStatusStageUpdateContext updateConfigurationStatusStage(OrderStatusStageUpdateContext context) {
		Optional<OrderGscDetail> gscDetail = orderGscDetailRepository.findById(context.configurationId);
		gscDetail.ifPresent(gscOrderdetail -> {
			// TODO:set the stage and site status
			gscOrderdetail.setMstOrderSiteStatus(context.mstOrderSiteStatus);
			gscOrderdetail.setMstOrderSiteStage(context.mstOrderSiteStage);
			orderGscDetailRepository.save(gscOrderdetail);
		});
		return context;
	}

	private GscApproveQuoteContext updateOrderToLe(GscApproveQuoteContext context) {
		List<OrderToLe> orderToLes = orderToLeRepository.findByOrder(context.order);
		context.orderToLe = orderToLes.stream().collect(Collectors.toSet());
		return context;
	}

	/**
	 * Update opportunity in SFDC
	 * 
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext updateOpportunityInSfdc(GscApproveQuoteContext context) {
		quoteToLeRepository.findByQuote(context.quote).forEach(quoteToLe ->{
			callUpdateOpportunityInSfdc(quoteToLe,context);
		});
		return context;

	}

	private GscApproveQuoteContext processCofPdf(GscApproveQuoteContext context) {
		gscQuotePdfService.processCofPdf(context.quote.getId(), context.response, false, true);
		return context;
	}

	/**
	 * Method to call UpdateOpportunity In Sfdc
	 * 
	 * @param context
	 * @return
	 */
	private void callUpdateOpportunityInSfdc(QuoteToLe quoteLe, GscApproveQuoteContext context) {
		try {
			omsSfdcService.processUpdateOpportunity(context.cofSignedDate, quoteLe.getTpsSfdcOptyId(),
					SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

		} catch (Exception e) {
			LOGGER.error("error in callUpdateOpportunityInSfdc method", e);
			//throw new TCLException("", e.getMessage());
		}

	}

	/**
	 * return cof pdf
	 * 
	 * @author Mayank Sharma
	 * @param orderId
	 * @param response
	 * @throws TclCommonException
	 */

	public void getCofPdfByOrderId(Integer orderId, HttpServletResponse response) throws TclCommonException {
		Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
		Object order = getOrder(orderId).get();
		gscPdfHelper.generateGscCof(((Order) order).getQuote().getId(), response);

	}

	/**
	 * fetch rfs via MQ call
	 * 
	 * @author VISHESH AWASTHI
	 * @param orderGscDetail
	 * @return
	 */
	private String getGscExpectedDateForDelivery(OrderGscDetail orderGscDetail) {
		String expectedDeliveryDate = null;
		String country = null;
		if (orderGscDetail.getOrderGsc().getProductName().equals(GscConstants.GLOBAL_OUTBOUND)) {
			country = orderGscDetail.getDest();
		} else {
			country = orderGscDetail.getSrc();
		}
		ExpectedDeliveryDateBean expectedDeliveryDateBean = new ExpectedDeliveryDateBean(
				orderGscDetail.getOrderGsc().getProductName(), orderGscDetail.getOrderGsc().getAccessType(), country);
		try {
			LOGGER.info("MDC Filter token value in before Queue call getGscRequestorDateForServiceDays {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			expectedDeliveryDate = mqUtils.sendAndReceive(expectedDeliveryDateQueue,
					Utils.convertObjectToJson(expectedDeliveryDateBean))
					.toString();
		} catch (Exception e) {
			LOGGER.warn("Error in estimaiting the Delivery Date ", e);
		}
		return expectedDeliveryDate;
	}

	public Try<BaseOrderManagementBean> getGscOrderManagementData(Integer orderId, String type) {
		return Try.of(() -> fetchOrderById(orderId))
				.map(order -> {
					switch (type) {
					case "INTERNATIONAL":
						return gscOrderManagementRequestBuilder.buildInternationalOrderRequest(order);
					case "DOMESTIC":
						return gscOrderManagementRequestBuilder.buildDomesticOrderManagementRequest(order);
					default:
						return gscOrderManagementRequestBuilder.buildInternationalOrderRequest(order);
					}
				});
	}

	/**
	 * Update Tiger Request
	 *
	 * @param orderId
	 * @return
	 */
	@Transactional
	public String updateTigerRequest(Integer orderId) {
		if (!userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
			Order order = fetchOrderById(orderId);
			order.getOrderToLes().stream().findAny().filter(this::isEligibleForDownstreamProcessing)
					.ifPresent(orderToLe1 -> processOrderManagementWithDownstreamSystems(order));
		}
		return "Success";
	}

	private void createTigerServiceThirdPartyJobEntry(Order order, String serviceType,
													  BaseOrderManagementBean request, Integer sequenceNumber) {
		// TODO : To create multiple interconnect orders We have commented below code
		/*List<ThirdPartyServiceJob> thirdPartyServiceJobs = thirdPartyServiceJobsRepository
				.findAllByRefIdAndServiceTypeInAndThirdPartySource(order.getOrderCode(), ImmutableList.of(serviceType),
						ThirdPartySource.TIGER.toString());
		if (CollectionUtils.isEmpty(thirdPartyServiceJobs)) {*/
			ThirdPartyServiceJob job = new ThirdPartyServiceJob();
			job.setRequestPayload(GscUtils.toJson(request));
			job.setServiceStatus(SfdcServiceStatus.NEW.toString());
			job.setIsComplete(GscConstants.STATUS_INACTIVE);
			job.setRefId(order.getOrderCode());
			job.setRetryCount(0);
			job.setQueueName(orderJobProcessingQueue);
			job.setSeqNum(sequenceNumber);
			job.setCreatedTime(new Date());
			job.setCreatedBy(Utils.getSource());
			job.setServiceType(serviceType);
			job.setThirdPartySource(ENTERPRISE_TIGER_ORDER.toString());
			thirdPartyServiceJobsRepository.save(job);
		/*} else {
			// if job already exists and is not in progress reset retry count and mark as
			// new
			ThirdPartyServiceJob job = thirdPartyServiceJobs.get(0);
			if (!SfdcServiceStatus.INPROGRESS.toString().equalsIgnoreCase(job.getServiceStatus())) {
				job.setServiceStatus(SfdcServiceStatus.NEW.toString());
				job.setRetryCount(0);
				job.setUpdatedBy(Utils.getSource());
				job.setUpdatedTime(new Date());
				thirdPartyServiceJobsRepository.save(job);
			}
		}*/
	}

	/**
	 * Processing all Orders to Tiger Downstream System
	 *
	 * @param order
	 */
	@Transactional
	public void processOrderManagementWithDownstreamSystems(Order order) {

		Objects.requireNonNull(order, "Order cannot be null");

		if(GscConstants.NEW.equalsIgnoreCase(orderToLeRepository.findByOrder(order).stream().findAny().get().getOrderType())) {
			processInterConnectOrdersManagement(order);
		}

		InternationalOrderManagementRequest orderManagementRequest = gscOrderManagementRequestBuilder
				.buildInternationalOrderRequest(order);
		if (!CollectionUtils.isEmpty(orderManagementRequest.getAccessServiceOrderItems())) {
			createTigerServiceThirdPartyJobEntry(order, TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER, orderManagementRequest,
					omsSfdcService.getSequenceNumber(TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER));
		}

		DomesticOrderManagementRequest domesticOrderManagementRequest = gscOrderManagementRequestBuilder
				.buildDomesticOrderManagementRequest(order);
		if (!CollectionUtils.isEmpty(domesticOrderManagementRequest.getDomesticCallingServiceOrderItem())) {
			createTigerServiceThirdPartyJobEntry(order, TIGER_SERVICE_TYPE_DOMESTIC_ORDER, domesticOrderManagementRequest,
					omsSfdcService.getSequenceNumber(TIGER_SERVICE_TYPE_DOMESTIC_ORDER));
		}
	}

	private void processInterConnectOrdersManagement(Order order) {

		/**
		 Business Logic Reference Notes:
		 1. Global Outbound + MPLS / PUblic IP --> Only one call will go always
		 2. Global Outbound + Domestic Voice + MPLS / Public IP --> One 3 InterConnect call have to trigger
		 	2.1. First, Create Global Outbound VTS
		 	2.2. Secound, Create Domestic VTS interconnect
		 	2.3. Third, Create Domestic NTS interconnect
		 3. Domestic Voice always will come with Global Outbound, So Read Point no. 2
		 4. ITFS + LNS + UIFN + ACDTFS + ACANS --> Only one InterConnect call will go
		 5. If customer select ITFS, LNS, UIFN, Global Outbound, Domestic Voice + MPLS / Public IP
		 	5.1. First, Create Global Outbound VTS
		 	5.2. Secound, Create Domestic VTS interconnect
		 	5.3. Third, Create Domestic NTS interconnect
		 	5.4. Fourth, Create International AccessService interconnect
		 6. ACANS, ACDTSF + MPLS / Public IP --> Only one InterConnect call will go
		 */

		OrderToLe orderToLe = getOrderToLeByOrder(order);
		List<OrderGsc> orderGscs = Optional
				.<Collection<OrderToLeProductFamily>>ofNullable(orderToLe.getOrderToLeProductFamilies())
				.orElseGet(() -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe)).stream()
				.map(orderProductSolutionRepository::findByOrderToLeProductFamily).flatMap(List::stream)
				.map(orderProductSolution -> orderGscRepository
						.findByorderProductSolutionAndStatus(orderProductSolution, GscConstants.STATUS_ACTIVE))
				.flatMap(List::stream)
				.filter(orderGsc -> GscConstants.PUBLIC_IP.equalsIgnoreCase(orderGsc.getAccessType()) ||
						GscConstants.MPLS.equalsIgnoreCase(orderGsc.getAccessType()))
				.collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(orderGscs)) {
			Map<String, Boolean> productNames = populateProductNames(orderGscs);
			if (Objects.nonNull(productNames.get(DOMESTIC_VOICE)) && productNames.get(DOMESTIC_VOICE).booleanValue()) {
				buildInterConnectOrderManagementRequest(order, orderToLe, orderGscs, TigerServiceConstants.SERVICE_NAME_DOMESTIC_VTS, TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS);
				buildInterConnectOrderManagementRequest(order, orderToLe, orderGscs, TigerServiceConstants.SERVICE_NAME_DOMESTIC_NVT, TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT);
			}
			if (Objects.nonNull(productNames.get(GLOBAL_OUTBOUND)) && productNames.get(GLOBAL_OUTBOUND).booleanValue()) {
				buildInterConnectOrderManagementRequest(order, orderToLe, orderGscs, TigerServiceConstants.SERVICE_NAME_OUTBOUND_VTS, TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND);
			}
			if (Objects.nonNull(productNames.get(ACCESS_SERVICES)) && productNames.get(ACCESS_SERVICES).booleanValue()) {
				buildInterConnectOrderManagementRequest(order, orderToLe, orderGscs, TigerServiceConstants.SERVICE_NAME_ACCESS_SERVICE, TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES);
			}
		}

	}

	private Map<String, Boolean> populateProductNames(List<OrderGsc> orderGscs) {
		Map<String, Boolean> productNameFlags = new HashMap<>();
		orderGscs.forEach(orderGsc -> {
			switch (orderGsc.getProductName()) {
				case DOMESTIC_VOICE: {
					productNameFlags.put(DOMESTIC_VOICE, true);
					break;
				}
				case GLOBAL_OUTBOUND: {
					productNameFlags.put(GLOBAL_OUTBOUND, true);
					break;
				}
				default: {
					productNameFlags.put(ACCESS_SERVICES, true);
					break;
				}
			}
		});

		if(Objects.nonNull(productNameFlags.get(DOMESTIC_VOICE))) {
			productNameFlags.put(ACCESS_SERVICES, false);
			productNameFlags.put(GLOBAL_OUTBOUND, false);
		} else if(Objects.nonNull(productNameFlags.get(GLOBAL_OUTBOUND))) {
			productNameFlags.put(ACCESS_SERVICES, false);
		}

		return productNameFlags;
	}

	private void buildInterConnectOrderManagementRequest(Order order, OrderToLe orderToLe, List<OrderGsc> orderGscs, String serviceName, String serviceType) {
		InterConnectOrderManagementRequest interConnectOrderManagementRequest = gscOrderManagementRequestBuilder
				.buildInterConnectOrderRequest(order, orderToLe, orderGscs, serviceName);
		if (!CollectionUtils.isEmpty(interConnectOrderManagementRequest.getInterconnectOrderItems())) {
			createTigerServiceThirdPartyJobEntry(order, serviceType,
					interConnectOrderManagementRequest, omsSfdcService.getSequenceNumber(serviceType));
		}
	}

	/**
	 * Get OrderToLe By Order
	 *
	 * @param order
	 * @return {@link OrderToLe}
	 */
	public OrderToLe getOrderToLeByOrder(Order order) {
		return orderToLeRepository.findByOrder(order).stream().filter(this::containsGsipProductFamily)
				.findFirst().orElseThrow(() -> new ObjectNotFoundException(
						String.format("No order to LE for GSIP found for order id: %s", order.getId())));
	}

	private boolean containsGsipProductFamily(OrderToLe orderToLe) {
		return Optional.<Collection<OrderToLeProductFamily>>ofNullable(orderToLe.getOrderToLeProductFamilies())
				.orElseGet(() -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe)).stream()
				.anyMatch(orderToLeProductFamily -> GSC_ORDER_PRODUCT_COMPONENT_TYPE
						.equalsIgnoreCase(orderToLeProductFamily.getMstProductFamily().getName()));
	}

	/**
	 * Get LeAttributes by OrderToLe and Attributes
	 *
	 * @param orderToLe
	 * @param attr
	 * @return
	 */
	public String getLeAttributes(OrderToLe orderToLe, String attr) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attr,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		Set<OrdersLeAttributeValue> orderToLeAttribute = ordersLeAttributeValueRepository
				.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe);
		for (OrdersLeAttributeValue quoteLeAttributeValue : orderToLeAttribute) {
			attrValue = quoteLeAttributeValue.getAttributeValue();
		}
		return attrValue;
	}

	/**
	 * Send Notification Mail to customer after manual approve and docu sign is done
	 *
	 * @param order
	 * @param orderToLe
	 * @param cofObjectMapper
	 * @param user
	 * @throws TclCommonException
	 */
	private void processOrderMailNotificationForManualAndDocuSign(Order order, OrderToLe orderToLe, Map<String, String> cofObjectMapper,
												User user) throws TclCommonException {
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(), user.getEmailId(),
				appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName, CommonConstants.GSC, orderToLe);
		notificationService.newOrderSubmittedNotification(mailNotificationBean);
	}

	/**
	 * Populate Mail Notification Bean for manual approve and docu sign
	 *
	 * @param accountManagerEmail
	 * @param orderRefId
	 * @param customerEmail
	 * @param provisioningLink
	 * @param cofObjectMapper
	 * @param fileName
	 * @param productName
	 * @param orderToLe
	 * @return {@link MailNotificationBean}
	 */
	private MailNotificationBean populateMailNotifionSalesOrder(String accountManagerEmail, String orderRefId, String customerEmail,
																String provisioningLink, Map<String,String> cofObjectMapper, String fileName,String productName, OrderToLe orderToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(provisioningLink);
		mailNotificationBean.setCofObjectMapper(cofObjectMapper);
		mailNotificationBean.setFileName(fileName);
		mailNotificationBean.setProductName(productName);
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * Send Notification Mail to customer and place order is done
	 *
	 * @param order
	 * @param orderToLe
	 * @param user
	 * @throws TclCommonException
	 */
	private void processOrderMailNotification(Order order, OrderToLe orderToLe, User user) throws TclCommonException {
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL.toString());
		String leName = getLeAttributes(orderToLe, LeAttributesConstants.LE_NAME.toString());
		String leContact = getLeAttributes(orderToLe, LeAttributesConstants.LE_CONTACT.toString());
		String cusEntityName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
		String spName = getLeAttributes(orderToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
		LOGGER.info("Emailing welcome letter notification to customer {} for order code {}", user.getFirstName(),
				order.getOrderCode());
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(user.getFirstName(), cusEntityName, spName, leName, leContact,
				leMail, order.getOrderCode(), user.getEmailId(), appHost + quoteDashBoardRelativeUrl,
				CommonConstants.GSC, orderToLe);
		notificationService.welcomeLetterNotification(mailNotificationBean);
	}

	/**
	 * Send Notification Mail to Sales Support team
	 *
	 * @param context
	 *
	 */
	public GscApproveQuoteContext sendPlaceOrderNotification(GscApproveQuoteContext context) {
		Order order = context.order;
		OrderToLe orderToLe = context.orderToLe.stream().findFirst().get();
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType()) &&
				SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
			LOGGER.info(">> Partner GSC Sell through - Place order email notification");
			processSalesSupportMailNotification(order, orderToLe, COF_SUBMITTED, COF_SUBMITTED_COMPLETE_PERCENTAGE);
		}
		return context;
	}

	/**
	 * Send Notification Mail to Sales Support team
	 *
	 * @param order
	 * @param orderToLe
	 * @param status
	 * @throws TclCommonException
	 */
	private void processSalesSupportMailNotification(Order order, OrderToLe orderToLe, String status, String completePercentage) {
		try {
			LOGGER.info(">> Process Sales support mail notification. Order status {}",  status);
			SalesSupportMailNotificationBean salesSupportMailNotificationBean = new SalesSupportMailNotificationBean();
			String orderCode = order.getOrderCode();
			Opportunity opportunity = opportunityRepository.findByUuid(orderCode);
			String customerSecsId = getLeAttributes(orderToLe, LeAttributesConstants.CUSTOMER_SECS_ID);
			Integer customerLeId = orderToLe.getErfCusCustomerLegalEntityId();
			String opportunityID = orderToLe.getTpsSfdcCopfId();
			String endCustomerName = opportunity.getCustomerLeName();
			String optyClassification = opportunity.getOptyClassification();

			//MQ call to get partner LE and Partner Account Name
			String partnerCustomerDetails = (String) mqUtils.sendAndReceive(partnerCustomerDetailsMQ, String.valueOf(customerLeId));
			String partnerLe = partnerCustomerDetails.split(":")[0].trim();
			String partnerAccountName = partnerCustomerDetails.split(":")[1].trim();

			salesSupportMailNotificationBean.setOpportunityId(Objects.nonNull(opportunityID) ? opportunityID : "");
			salesSupportMailNotificationBean.setEndCustomerName(Objects.nonNull(endCustomerName) ? endCustomerName : "");
			salesSupportMailNotificationBean.setOptyClassification(Objects.nonNull(optyClassification) ? optyClassification : "");
			salesSupportMailNotificationBean.setStatus(Objects.nonNull(status) ? status : "");
			salesSupportMailNotificationBean.setCompletePercentage(Objects.nonNull(completePercentage) ? completePercentage : "");
			salesSupportMailNotificationBean.setSecsId(Objects.nonNull(customerSecsId) ? customerSecsId : "");
			salesSupportMailNotificationBean.setQuoteRefId(Objects.nonNull(orderCode) ? orderCode : "");
			salesSupportMailNotificationBean.setPartnerLe(Objects.nonNull(partnerLe) ? partnerLe : "");
			salesSupportMailNotificationBean.setAccountName(Objects.nonNull(partnerAccountName) ? partnerAccountName : "");
			LOGGER.info(">> Sales support notification bean values {}",  salesSupportMailNotificationBean.toString());
			notificationService.salesSupportMailNotification(salesSupportMailNotificationBean);
		} catch (TclCommonException e) {
			LOGGER.error("Sales support notification failed", ExceptionUtils.getStackTrace(e));
			throw new TclCommonRuntimeException("Sales support notification failed");
		}
	}

	/**
	 * Populate Mail Notification Bean for place order
	 *
	 * @param userName
	 * @param contactEntityName
	 * @param supplierEntityName
	 * @param accName
	 * @param accContact
	 * @param accountManagerEmail
	 * @param orderRefId
	 * @param customerEmail
	 * @param orderTrackingUrl
	 * @param productName
	 * @param orderToLe
	 * @return {@link MailNotificationBean}
	 */
	private MailNotificationBean populateMailNotificationBean(String userName, String contactEntityName, String supplierEntityName,
				String accName, String accContact, String accountManagerEmail, String orderRefId, String customerEmail,
				String orderTrackingUrl,String productName, OrderToLe orderToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setContactEntityName(contactEntityName);
		mailNotificationBean.setSupplierEntityName(supplierEntityName);
		mailNotificationBean.setCustomerAccountName(accName);
		mailNotificationBean.setAccountManagerContact(accContact);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(orderTrackingUrl);
		mailNotificationBean.setProductName(productName);
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * Populating partner specific data into order
	 *
	 * @param orderToLe
	 * @param mailNotificationBean
	 * @return {@link MailNotificationBean}
	 */
	private MailNotificationBean populatePartnerClassification(OrderToLe orderToLe, MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
					String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails()
					.stream().findAny().get().getLegalEntityName();

			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

			mailNotificationBean.setClassification(orderToLe.getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		} catch (Exception e) {
			LOGGER.warn("Error while reading end customer name :: {}", e.getMessage());
		}
		return mailNotificationBean;
	}

	private boolean isEligibleForDownstreamProcessing(OrderToLe orderToLe) {
		return (ORDER_TYPE_MACD.equalsIgnoreCase(orderToLe.getOrderType()) &&
				(TRUE_FLAG.equalsIgnoreCase(processMACDOrdersWithDownstreamSystem))
				|| (TRUE_FLAG.equalsIgnoreCase(processOrdersWithDownstreamSystem) &&
				NEW.equalsIgnoreCase(orderToLe.getOrderType())));
	}

	/**
	 * Update OrderToLe Stage
	 *
	 * @param orderToLeId
	 * @param status
	 * @return {@link QuoteDetail}
	 * @throws TclCommonException
	 */
	@Transactional
	public QuoteDetail updateOrderToLeStatus(Integer orderToLeId, String status) throws TclCommonException {
		QuoteDetail quoteDetail = new QuoteDetail();
		if (Objects.isNull(orderToLeId) || (StringUtils.isEmpty(status))) {
			throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
					R_CODE_ERROR);
		}
		Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
		if (!orderToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
					R_CODE_ERROR);
		orderToLe.get().setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
		updateOptyId(orderToLe.get());
		orderToLeRepository.save(orderToLe.get());
		Order order = orderToLe.get().getOrder();
		order.setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
		orderRepository.save(order);
		if (status.equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())
				|| (GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(orderToLe.get().getOrderType())
						&& status.equalsIgnoreCase(OrderStagingConstants.ORDER_IN_PROGRESS.toString()))) {
			String accManagerEmail = getLeAttributes(orderToLe.get(), LeAttributesConstants.LE_EMAIL);
			String custAccountName = getLeAttributes(orderToLe.get(), LeAttributesConstants.LEGAL_ENTITY_NAME);
			String orderRefId = order.getOrderCode();
			User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
			processOrderMailNotification(order, orderToLe.get(), user);
			notificationService.provisioningOrderNewOrderNotification(accManagerEmail, orderRefId, custAccountName,
					appHost + adminRelativeUrl);
			
			if (orderToLe.get().getOrderType() == null || orderToLe.get().getOrderType().equals("NEW") || orderToLe.get().getOrderType().equals("MACD")) {
				List<OrderGsc> orderGscEntity=orderGscRepository.findByOrderToLe(orderToLe.get());
				for (OrderGsc orderGsc : orderGscEntity) {
					if(orderGsc.getAccessType().equals(GscConstants.PSTN)) {
						order.setIsOrderToCashEnabled(BACTIVE);
						order.setOrderToCashOrder(BACTIVE);
					}else if(orderGsc.getAccessType().equals(GscConstants.PUBLIC_IP)){
						order.setOrderToCashOrder(BACTIVE);
					}
				}
				orderRepository.save(order);
			}
			if (order.getIsOrderToCashEnabled() != null
					&& order.getIsOrderToCashEnabled().equals(CommonConstants.BACTIVE)) {
				LOGGER.info("Inside the order to flat table freeze");
				Map<String, Object> requestparam = new HashMap<>();
				requestparam.put("orderId", order.getId());
				if (orderToLe.get().getOrderType() == null || orderToLe.get().getOrderType().equals("NEW")) {
					requestparam.put("productName", CommonConstants.GSIP);
				} else {
					//requestparam.put("productName", CommonConstants.GSIP_MACD);
				}
				requestparam.put("userName", Utils.getSource());
				mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
			}
			String lrDownloadUrl = appHost + CommonConstants.RIGHT_SLASH + "optimus-oms/api/lr/orders/gsc/"
					+ order.getId();
			orderLrService.initiateLrJob(order.getOrderCode(), "GSIP", lrDownloadUrl);
		}
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType()) &&
				SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.get().getClassification())) {
			LOGGER.info(">> Partner GSC Sell through - Launch delivery email notification");
			processSalesSupportMailNotification(order, orderToLe.get(), LAUNCH_DELIVERY, LAUNCH_DELIVERY_COMPLETE_PERCENTAGE);
		}
		return quoteDetail;
	}

	private void updateOptyId(OrderToLe orderToLe) {
		if(Objects.nonNull(orderToLe.getOrder()) && Objects.nonNull(orderToLe.getOrder().getQuote()) && Objects.nonNull(orderToLe.getOrder().getQuote().getQuoteToLes())) {
			orderToLe.setTpsSfdcCopfId(orderToLe.getOrder().getQuote().getQuoteToLes().stream().findAny().get().getTpsSfdcOptyId());
		}
	}

	/**
	 * Delete Order Product Solutions by Id
	 *
	 * @param productSolutionIds
	 */
	public void deleteOrderProductSolutions(Set<Integer> productSolutionIds) {
		List<OrderProductSolution> productSolutions = orderProductSolutionRepository.findAllById(productSolutionIds);
		productSolutions.forEach(productSolution -> {
			List<OrderGsc> orderGscs = orderGscRepository.findByorderProductSolutionAndStatus(productSolution,
					GscConstants.STATUS_ACTIVE);
			orderGscs.forEach(gscOrderDetailService::deleteOrderGscDetailsByOrderGsc);
			orderGscRepository.deleteByOrderProductSolution(productSolution);
			// Trigger delete productSolution
			/*
			 * if (StringUtils.isNotEmpty(quoteToLe.getTpsSfdcOptyId())) gscOmsSfdcComponent
			 * .getOmsSfdcService() .processDeleteProduct(quoteToLe, productSolution);
			 */
			orderProductSolutionRepository.delete(productSolution);
		});
	}

	private OrderProductComponent getGscOrderProductComponent(OrderGsc orderGsc) {
		MstProductComponent gscOrderMasterComponent = mstProductComponentRepository
				.findByName(PRODUCT_COMPONENT_TYPE_ORDER_GSC);
		if (Objects.isNull(gscOrderMasterComponent)) {
			throw new RuntimeException(
					String.format("Master product component of type: %s not found", PRODUCT_COMPONENT_TYPE_ORDER_GSC));
		}
		return orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(orderGsc.getId(), gscOrderMasterComponent).stream().findFirst()
				.orElseGet(() -> {
					OrderProductComponent orderProductComponent = new OrderProductComponent();
					orderProductComponent.setMstProductComponent(gscOrderMasterComponent);
					orderProductComponent.setReferenceId(orderGsc.getId());
					orderProductComponent.setType(PRODUCT_COMPONENT_TYPE_ORDER_GSC);
					MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(
							GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), GscConstants.STATUS_ACTIVE);
					orderProductComponent.setMstProductFamily(mstProductFamily);
					return orderProductComponentRepository.save(orderProductComponent);
				});
	}

	/**
	 * Save Order Gsc Attributes Using OrderGsc and Attributes
	 *
	 * @param orderGsc
	 * @param attributes
	 */
	public void saveOrderGscAttributes(OrderGsc orderGsc, Map<String, String> attributes) {
		OrderProductComponent gscOrderProductComponent = getGscOrderProductComponent(orderGsc);
		attributes.forEach((key, value) -> {
			ProductAttributeMaster productAttributeMaster = productAttributeMasterRepository
					.findByNameAndStatus(key, GscConstants.STATUS_ACTIVE).stream().findFirst()
					.orElseThrow(() -> new RuntimeException(
							String.format("Product attribute with name: %s not found in master", key)));
			saveOrderConfigurationAttributeValue(gscOrderProductComponent, productAttributeMaster, value);
		});
	}

	private Map<String, String> getOrderGscAttributes(OrderGsc orderGsc) {
		OrderProductComponent gscOrderProductComponent = getGscOrderProductComponent(orderGsc);
		return Optional.ofNullable(gscOrderProductComponent.getOrderProductComponentsAttributeValues())
				.orElse(ImmutableSet.of()).stream()
				.collect(Collectors.toMap(value -> value.getProductAttributeMaster().getName(),
						OrderProductComponentsAttributeValue::getAttributeValues));
	}

	/**
	 * Save/Update Tiger API response params like order id etc in order gsc level
	 *  @param response
	 * @param orderId
	 * @param isInterConnectOrder
	 */
	@Transactional
	public void updateResponseDataOrderGscAttributes(BaseOrderManagementResponse response, Integer orderId, String attributeName) {
		Objects.requireNonNull(response, "Response cannot be null");
		Objects.requireNonNull(orderId, "Order Id cannot be null");
		Objects.requireNonNull(attributeName, "Attribute Name cannot be null");
		try {
			Order order = orderRepository.findById(orderId).get();
			// Load GSIP specific OrderToLe from Orders when there is a MPLS
			List<OrderGsc> orderGscList = orderGscRepository.findByOrderToLe(getOrderToLeByOrder(order));
//			List<OrderGsc> orderGscList = order.getOrderToLes().stream().findFirst()
//					.map(orderGscRepository::findByOrderToLe).orElseThrow(() -> new RuntimeException(
//							String.format("No order to le found for order id: %s", orderId)));

            switch (attributeName) {
                case TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER:
                    orderGscList = orderGscList.stream().filter(orderGsc -> !DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName())
                        && !GLOBAL_OUTBOUND.equalsIgnoreCase(orderGsc.getProductName())).collect(Collectors.toList());
                    Map<String, String> internationOrderAttribute = ImmutableMap.of(GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID, response.getOrderId());
                    orderGscList.forEach(orderGsc -> saveOrderGscAttributes(orderGsc, internationOrderAttribute));
                    saveDownStreamSubOrderId(orderGscList, response.getSubOrderDetails(), GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID);
					checkChangeOutpulseAndReverseUpdate(order);
                    break;
                case TIGER_SERVICE_TYPE_DOMESTIC_ORDER:
                    orderGscList = orderGscList.stream().filter(orderGsc -> DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName())).collect(Collectors.toList());
                    Map<String, String> domesticOrderAttribute = ImmutableMap.of(GSC_DOMESTIC_DOWNSTREAM_ORDER_ID, response.getOrderId());
                    orderGscList.forEach(orderGsc -> saveOrderGscAttributes(orderGsc, domesticOrderAttribute));
                    saveDownStreamSubOrderId(orderGscList, response.getSubOrderDetails(), GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID);
                    break;
                case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES:
                    orderGscList = orderGscList.stream().filter(orderGsc -> !DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName())
                        && !GLOBAL_OUTBOUND.equalsIgnoreCase(orderGsc.getProductName())).collect(Collectors.toList());
                    Map<String, String> interConnectOrderAttribute = ImmutableMap.of(GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID, response.getOrderId());
                    orderGscList.forEach(orderGsc -> saveOrderGscAttributes(orderGsc, interConnectOrderAttribute));
                    saveDownStreamSubOrderId(orderGscList, response.getSubOrderDetails(), GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID);
                    break;
                case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND:
                    orderGscList = orderGscList.stream().filter(orderGsc -> GLOBAL_OUTBOUND.equalsIgnoreCase(orderGsc.getProductName())).collect(Collectors.toList());
                    Map<String, String> interConnectGlobalOrderAttribute = ImmutableMap.of(GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID, response.getOrderId());
                    orderGscList.forEach(orderGsc -> saveOrderGscAttributes(orderGsc, interConnectGlobalOrderAttribute));
                    saveDownStreamSubOrderId(orderGscList, response.getSubOrderDetails(), GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID);
                    break;
                case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT:
					orderGscList = orderGscList.stream().filter(orderGsc -> DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName())).collect(Collectors.toList());
					Map<String, String> interConnectDomesticNVTOrderAttribute = ImmutableMap.of(GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID, response.getOrderId());
					orderGscList.forEach(orderGsc -> saveOrderGscAttributes(orderGsc, interConnectDomesticNVTOrderAttribute));
					saveDownStreamSubOrderId(orderGscList, response.getSubOrderDetails(), GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT);
					break;
                case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS:
                    orderGscList = orderGscList.stream().filter(orderGsc -> DOMESTIC_VOICE.equalsIgnoreCase(orderGsc.getProductName())).collect(Collectors.toList());
                    Map<String, String> interConnectDomesticVTSOrderAttribute = ImmutableMap.of(GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID, response.getOrderId());
                    orderGscList.forEach(orderGsc -> saveOrderGscAttributes(orderGsc, interConnectDomesticVTSOrderAttribute));
                    saveDownStreamSubOrderId(orderGscList, response.getSubOrderDetails(), GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS);
                    break;
                default:
                    break;
            }

		} catch (Exception e) {
			LOGGER.warn(
					String.format("Error occurred while updating response data attributes for order id: %s", orderId),
					e);
		}
	}

	private void checkChangeOutpulseAndReverseUpdate(Order order) {
		if(CHANGING_OUTPULSE.equalsIgnoreCase(order.getOrderToLes().stream().findFirst().get().getOrderCategory())) {
			createTigerServiceThirdPartyJobEntryForChangeOutpulse(order, CHANGE_OUTPULSE_ORDER, new InternationalOrderManagementRequest(),
					omsSfdcService.getSequenceNumber(CHANGE_OUTPULSE_ORDER),
					tigerOrderChangeOutpulseJobProcessingQueue, CHANGE_OUTPULSE.toString());
		}
	}

	private void createTigerServiceThirdPartyJobEntryForChangeOutpulse(Order order, String serviceType,
													  BaseOrderManagementBean request, Integer sequenceNumber, String queueName, String source) {
		// TODO : To create multiple interconnect orders We have commented below code
		ThirdPartyServiceJob job = new ThirdPartyServiceJob();
		job.setRequestPayload(GscUtils.toJson(request));
		job.setServiceStatus(SfdcServiceStatus.NEW.toString());
		job.setIsComplete(GscConstants.STATUS_INACTIVE);
		job.setRefId(order.getOrderCode());
		job.setRetryCount(0);
		job.setQueueName(queueName);
		job.setSeqNum(sequenceNumber);
		job.setCreatedTime(new Date());
		job.setCreatedBy(Utils.getSource());
		job.setServiceType(serviceType);
		job.setThirdPartySource(source);
		thirdPartyServiceJobsRepository.save(job);
	}

	private void saveDownStreamSubOrderId(List<OrderGsc> orderGscList, SubOrderDetails subOrderDetails, String attributeName) {
	    // Based on response save sub order id
        Iterator<OrderGsc> orderGscIterator = orderGscList.iterator();
        Iterator<SubOrder> subOrderIterator = subOrderDetails.getSuborders().iterator();

        while (orderGscIterator.hasNext() && subOrderIterator.hasNext()) {
            Map<String, String> orderAttribute = ImmutableMap.of(attributeName, subOrderIterator.next().getSubOrderId());
            saveOrderGscAttributes(orderGscIterator.next(), orderAttribute);
        }
    }

	/**
	 * download Outbound Prices as PDF
	 * 
	 * @param response
	 * @param orderId
	 * @return {@link Try<HttpServletResponse>}
	 * @throws TclCommonException
	 */
	@Transactional
	public Integer downloadOutboundPrices(GscOutboundAttachmentBean attachmentBean) {
		Objects.requireNonNull(attachmentBean, "Attachment Request is null");
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(attachmentBean.getQuoteCode()).stream().findFirst().get();
		List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByQuoteToLeAndAttachmentType(quoteToLe, OTHERS);

		return omsAttachments.stream().filter(omsAttachment -> {
			return omsAttachment.getReferenceName().equalsIgnoreCase(getReferenceName(attachmentBean.getFileName(), attachmentBean.getFileType()));
		}).findFirst().get().getErfCusAttachmentId();
	}

	private String getReferenceName(final String fileName, final String fileType) {
		if(GSIP_SURCHARGE_PDF.toLowerCase().contains(fileName.toLowerCase())) {
			return GSIP_SURCHARGE_PDF;
		} else if(GSIP_OUTBOUND_PDF.toLowerCase().contains(fileName.toLowerCase())) {
			if(GscAttachmentTypeConstants.PDF.toLowerCase().contains(fileType.toLowerCase())) {
				return GSIP_OUTBOUND_PDF;
			} else {
				return GSIP_OUTBOUND_EXCEL;
			}
		}
		return "";
	}

	private void updateSfdcStageForOrder(Order order, String sfdcStage) {
		QuoteToLe quoteToLe = order.getQuote().getQuoteToLes().stream().findFirst().get();
		QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, SFDCConstants.SFDC_STAGE).stream().findFirst().get();
		quoteLeAttributeValue.setAttributeValue(sfdcStage);
		quoteLeAttributeValue.setDisplayValue(sfdcStage);
		quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
	}

	private boolean checkAndUpdateSfdcStageForOrder(Order order) {
		String sfdcStage = getSfdcStageForOrder(order.getOrderCode());
		if (!SFDCConstants.CLOSED_WON_COF_RECI.equalsIgnoreCase(sfdcStage)) {
			// TODO get status from optimus-service MQ call
			String opportunityId = order.getQuote().getQuoteToLes().stream().findFirst()
					.map(QuoteToLe::getTpsSfdcOptyId)
					.orElseThrow(() -> new RuntimeException("SFDC opportunity not found for order"));
			try {
				String response = (String) mqUtils.sendAndReceive(sfdcOpportunityGetQueue, opportunityId);
				if (StringUtils.isBlank(response)) {
					throw new RuntimeException("Unable to get SFDC stage data!");
				}
				SfdcOpportunityInfo opportunityInfo = GscUtils.fromJson(response, SfdcOpportunityInfo.class);
				boolean isSfdcStageSuitable = SFDCConstants.CLOSED_WON_COF_RECI
						.equalsIgnoreCase(opportunityInfo.getStageName());
				if (isSfdcStageSuitable) {
					LOGGER.info("Updating SFDC stage to '{}' for order code '{}'", opportunityInfo.getStageName(),
							order.getOrderCode());
					updateSfdcStageForOrder(order, opportunityInfo.getStageName());
				}
				return isSfdcStageSuitable;
				// check if 95% and update DB else return false
			} catch (Exception e) {
				Throwables.propagate(e);
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * Retry failed or struck order records for downstream processing
	 * 
	 * @param orderId
	 * @return
	 */
	@Transactional
	public String retryDownstreamProcessing(Integer orderId) {
		Objects.requireNonNull(orderId, "Order Id cannot be null");
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException(String.format("Order with id: %s not found", orderId)));
		List<ThirdPartyServiceJob> thirdPartyServiceJobs = thirdPartyServiceJobsRepository
				.findAllByRefIdAndServiceTypeInAndThirdPartySourceIn(order.getOrderCode(),
						ImmutableList.of(TIGER_SERVICE_TYPE_DOMESTIC_ORDER, TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES, TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND, WHOLESALE_ORDER),
						ImmutableList.of(ENTERPRISE_TIGER_ORDER.toString(), WHOLESALE_TIGER_ORDER.toString()));
		if (CollectionUtils.isEmpty(thirdPartyServiceJobs)) {
			throw new RuntimeException(String.format(
					"Order id: %s in stage: %s is not eligible for downstream processing", orderId, order.getStage()));
		} else {
			ThirdPartyServiceJob orderJob = thirdPartyServiceJobs.get(0);

			if(ENTERPRISE_TIGER_ORDER.toString().equalsIgnoreCase(orderJob.getThirdPartySource())) {
				updateSFDCOppurtunityID(orderJob, order);
			}

			switch (SfdcServiceStatus.valueOf(orderJob.getServiceStatus())) {
			case STRUCK:
				LOGGER.info(String.format("Downstream job for order code: %s is Struck, retrying via API now",
						order.getOrderCode()));
				if (ENTERPRISE_TIGER_ORDER.toString().equalsIgnoreCase(orderJob.getThirdPartySource()) &&
						checkAndUpdateSfdcStageForOrder(order)) {
					return updateThirdPartyServiceJobStatus(orderJob);
				} else if(WHOLESALE_TIGER_ORDER.toString().equalsIgnoreCase(orderJob.getThirdPartySource())) {
					return updateThirdPartyServiceJobStatus(orderJob);
				} else {
					throw new RuntimeException(
							String.format("Order id: %s is not in SFDC stage of 95 percent", orderId));
				}
			default:
				LOGGER.info(String.format(
						"Downstream job for order code: %s is already in %s status, no need to mark retry now",
						order.getOrderCode(), orderJob.getServiceStatus()));
				return GscConstants.SUCCESS;
			}
		}
	}

	private String updateThirdPartyServiceJobStatus(ThirdPartyServiceJob orderJob) {
		orderJob.setRetryCount(0);
		orderJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
		thirdPartyServiceJobsRepository.save(orderJob);
		return GscConstants.SUCCESS;
	}

	private ThirdPartyServiceJob updateSFDCOppurtunityID(ThirdPartyServiceJob orderJob, Order order) {
//		if(DELETE_NUMBER.equalsIgnoreCase(orderToLeRepository.findByOrder(order).stream().findAny().get().getOrderCategory())) {
			InternationalOrderManagementRequest requestBean = GscUtils.fromJson(orderJob.getRequestPayload(), InternationalOrderManagementRequest.class);
			String sfdcOptyId = order.getOrderToLes().stream().findAny().get().getTpsSfdcCopfId();
			requestBean.setSFDCOPTYID(sfdcOptyId);
			requestBean.setSolutionId(sfdcOptyId);
			orderJob.setRequestPayload(GscUtils.toJson(requestBean));
			orderJob = thirdPartyServiceJobsRepository.save(orderJob);
//		}
		return orderJob;
	}

	/**
	 * Retry downstream processing for a given quote to le based order
	 * 
	 * @param quoteToLe
	 * @return
	 */
	@Transactional
	public String retryDownstreamProcessing(QuoteToLe quoteToLe) {
		Objects.requireNonNull(quoteToLe, "QuoteToLe cannot be null");
		return orderRepository.findByQuote(quoteToLe.getQuote()).map(Order::getId).map(this::retryDownstreamProcessing)
				.orElse(CommonConstants.ERROR);
	}

	/**
	 * Fetch Sfdc stage attribute value based on provided orderId
	 * 
	 * @param orderId
	 * @return
	 */
	@Transactional
	public String getSfdcStageForOrder(String orderCode) {
		Objects.requireNonNull(orderCode, "Order code cannot be null");
		Order order = orderRepository.findByOrderCode(orderCode);

		// No SFDC for MACD - Change Outpulse - So Mocking SFDC Stage
		List<OrderToLe> orderToLes = order.getOrderToLes().stream()
				.filter(orderToLe -> GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(orderToLe.getOrderType()) &&
						GscConstants.CHANGING_OUTPULSE.equalsIgnoreCase(orderToLe.getOrderCategory()))
				.collect(Collectors.toList());
		if(Objects.nonNull(orderToLes) && !orderToLes.isEmpty()) {
			return SFDCConstants.CLOSED_WON_COF_RECI;
		}

		QuoteToLe quoteToLe = order.getQuote().getQuoteToLes().stream().findFirst().get();
		return quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, SFDCConstants.SFDC_STAGE).stream().findFirst()
				.map(QuoteLeAttributeValue::getAttributeValue).orElse("");
	}

	/**
	 * Method to check MSA and SS documents
	 * 
	 * @param context
	 * @return {@link GscApproveQuoteContext}
	 */
	public GscApproveQuoteContext checkMSAandSSDocuments(GscApproveQuoteContext context) {
		context.quoteToLe.stream().forEach(this::uploadSSIfNotPresent);
		/**commented due to requirement change for
		* MSA mapping while optimus journey*/
		//forEach(this::uploadMSAIfNotPresent);
		return context;
	}

	/**
	 * Method to upload Service schedule document If Not Present
	 * 
	 * @param quoteToLe
	 * @return {@link QuoteToLe}
	 */
	private QuoteToLe uploadSSIfNotPresent(QuoteToLe quoteToLe) {
		Optional<List<MstOmsAttribute>> mstOmsAttributes = Optional.ofNullable(mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.SERVICE_SCHEDULE, CommonConstants.BACTIVE));
		mstOmsAttributes.ifPresent(attribute -> {
			attribute.forEach(mstOmsAttribute -> {
				List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
				if (quoteLeAttributeValues.isEmpty()) {
					ServiceScheduleBean serviceScheduleBean = constructServiceScheduleBean(
							quoteToLe.getErfCusCustomerLegalEntityId());
					LOGGER.info("MDC Filter token value in before Queue call uploadSSIfNotPresent {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					try {
						mqUtils.sendAndReceive(updateSSQueue, Utils.convertObjectToJson(serviceScheduleBean));
					} catch (TclCommonException | IllegalArgumentException e) {
						LOGGER.info("Exception in uploading SS document: {}", e.getMessage());
					}
				}
			});
		});
		return quoteToLe;
	}

	/**
	 * construct Service Schedule Bean
	 * 
	 * @param customerLeId
	 * @return {@link ServiceScheduleBean}
	 */
	public ServiceScheduleBean constructServiceScheduleBean(Integer customerLeId) {
		ServiceScheduleBean serviceScheduleBean = new ServiceScheduleBean();
		serviceScheduleBean.setCustomerLeId(customerLeId);
		serviceScheduleBean.setDisplayName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setIsSSUploaded(true);
		serviceScheduleBean.setName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setProductName(SFDCConstants.GSIP);
		return serviceScheduleBean;
	}

	/**
	 * upload MSA document if not present
	 * 
	 * @param quoteToLe
	 * @return {@link QuoteToLe}
	 */
	private QuoteToLe uploadMSAIfNotPresent(QuoteToLe quoteToLe) {
		Optional<List<MstOmsAttribute>> mstOmsAttributes = Optional.ofNullable(
				mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.MSA, CommonConstants.BACTIVE));
		mstOmsAttributes.ifPresent(attribute -> {
			attribute.forEach(mstOmsAttribute -> {
				List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
				if (quoteLeAttributeValues.isEmpty()) {
					MSABean msaBean = constructMSABean(quoteToLe.getErfCusCustomerLegalEntityId());
					try {
						mqUtils.sendAndReceive(updateMSAQueue, Utils.convertObjectToJson(msaBean));
					} catch (TclCommonException e) {
						LOGGER.error("error in process update oms billing attribute", e);
					}
				}

			});
		});
		return quoteToLe;
	}

	/**
	 * construct MSA Bean
	 * 
	 * @param customerLeId
	 * @return {@link MSABean}
	 */
	private MSABean constructMSABean(Integer customerLeId) {
		MSABean msaBean = new MSABean();
		msaBean.setCustomerLeId(customerLeId);
		msaBean.setDisplayName(LeAttributesConstants.MSA);
		msaBean.setIsMSAUploaded(true);
		msaBean.setName(LeAttributesConstants.MSA);
		msaBean.setProductName(SFDCConstants.GSIP);
		return msaBean;
	}

	/**
	 * Get Emergency Address
	 *
	 * @param configuarationId
	 * @return {@link List<String>}
	 */
	public List<String> getEmergencyAddress(Integer configuarationId) {
		List<String> emergencyAddressList = new ArrayList<>();
		List<String> emergencyAddressMap = getOrderProductComponentAttributeForGSIP(configuarationId);
		emergencyAddressMap.stream().forEach(emergencyAddress -> {
			emergencyAddressList.add(gscExportLRService.getEmergencyAddress(emergencyAddress.split(":")[0]));

		});
		return emergencyAddressList;
	}

	private List<String> getOrderProductComponentAttributeForGSIP(Integer configurationId) {
		Objects.requireNonNull(configurationId, "Configuration Id cannot be null");
		final List<String> attributeList = new ArrayList<>();
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(configurationId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		orderProductComponents.stream().forEach(orderProductComponent -> {
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponent(orderProductComponent);

			orderProductComponentsAttributeValues.stream()
					.filter(orderProductComponentsAttributeValue -> orderProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase("Emergency Address"))
					.forEach(orderProductComponentsAttributeValue -> fetchEmergencyAddressAttrValues(orderProductComponentsAttributeValue, attributeList));
		});
		return attributeList;
	}

	private void fetchEmergencyAddressAttrValues(OrderProductComponentsAttributeValue orderProductComponentsAttributeValue, List<String> attributeList) {
		attributeList.add(orderProductComponentsAttributeValue.getAttributeValues());
	}

	/**
	 * create Context for Docu Sign Approve Quotes
	 *
	 * @param quoteCode
	 * @return {@link GscApproveQuoteContext}
	 */
	private GscApproveQuoteContext createDocuSignApproveQuoteContext(String quoteCode) {
        GscApproveQuoteContext context = new GscApproveQuoteContext();
        context.user = userService.getUserId(Utils.getSource());
        Quote quote = new Quote();
        quote.setQuoteCode(quoteCode);
        context.quote = quote;
        context.gscNewOrder = false;
        context.cofSignedDate = new Timestamp(System.currentTimeMillis());
		context.isDocuSign = true;
		context.approveQuoteType = "DocuSign Approval";
        return context;
    }

	private GscApproveQuoteContext createManualApproveQuoteContext(Integer quoteId, String ipAddress) {
		GscApproveQuoteContext context = new GscApproveQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		Quote quote = new Quote();
		quote.setId(quoteId);
		context.quote = quote;
		context.gscNewOrder = false;
		context.cofSignedDate = new Timestamp(System.currentTimeMillis());
		context.ipAddress = ipAddress;
		context.approveQuoteType = "Manual Approval";
		return context;
	}

	/**
	 * Get docu sign audit details
	 *
	 * @param context
	 * @return {@link GscApproveQuoteContext}
	 */
	private GscApproveQuoteContext getDocuSignAudit(GscApproveQuoteContext context) {
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(context.quote.getQuoteCode());
		if (Objects.nonNull(docusignAudit) && Objects.nonNull(docusignAudit.getCustomerSignedDate())
				&& (docusignAudit.getStatus().equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString())
				|| docusignAudit.getStatus().equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString()))) {
			context.cofSignedDate = docusignAudit.getCustomerSignedDate();
		}
		return context;
	}

	/**
	 * Get COF Object Mapper - File Location Path
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext getCofObjectMapperForDocuSign(GscApproveQuoteContext context) {
		Map<String, String> cofObjectMapper = new HashMap<>();
		CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(context.order.getOrderCode());
		if (Objects.nonNull(cofDetails)) {
			cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
		}
		context.cofObjectMapper = cofObjectMapper;
		return context;
	}

	/**
	 * Get COF Object Mapper - File Location Path
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext getCofObjectMapperForManualApprove(GscApproveQuoteContext context) {
		Map<String, String> cofObjectMapper = new HashMap<>();
		CofDetails cofDetails = cofDetailsRepository.findByOrderUuidAndSource(context.order.getOrderCode(),
				MANUAL_COF.getSourceType());
		if (Objects.nonNull(cofDetails)) {
			cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
		}
		context.cofObjectMapper = cofObjectMapper;
		return context;
	}

	/**
	 * Get Quote Delegate details
	 *
	 * @param context
	 * @return {@link GscApproveQuoteContext}
	 * @throws TclCommonException
	 */
	private GscApproveQuoteContext getQuoteDelegate(GscApproveQuoteContext context) throws TclCommonException {
		context.quoteDelegate = quoteDelegationRepository.findByQuoteToLe(context.quoteToLe.get(0));
		User user = userRepository.findById(context.order.getCreatedBy()).get();
		processOrderMailNotificationForManualAndDocuSign(context.order, context.orderToLe.stream().findFirst().get(), context.cofObjectMapper, user);
		context.quoteDelegate.forEach(quoteDelegation -> {
			quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
			quoteDelegationRepository.save(quoteDelegation);
		});
		return context;
	}

	/**
	 * Set Quote status for docu sign aprrove Quotes
	 * @param context
	 * @return {@link GscApproveQuoteContext}
	 */
	private GscApproveQuoteContext setQuoteStatus(GscApproveQuoteContext context){
		context.quoteToLe.forEach(quoteToLe -> {
			if (ORDER_FORM.getConstantCode().equalsIgnoreCase(quoteToLe.getStage())) {
				quoteToLe.setStage(ORDER_ENRICHMENT.getConstantCode());
				quoteToLeRepository.save(quoteToLe);
			}
		});
		return context;
	}

	/**
	 * Set quote in approve quote
	 * @param context
	 * @return {@link Try<GscApproveQuoteContext>}
	 */
    private Try<GscApproveQuoteContext> getQuoteByQuoteCode(GscApproveQuoteContext context) {
        context.quote = getQuoteByCode(context.quote.getQuoteCode()).get();
        return Try.success(context);
    }

	/**
	 * Get Quote by quote code
	 * @param quoteCode
	 * @return {@link Try<Quote>}
	 */
	private Try<Quote> getQuoteByCode(String quoteCode) {
        return Optional.of(quoteRepository.findByQuoteCode(quoteCode)).map(Try::success).orElse(notFoundError(ExceptionConstants.QUOTE_EMPTY,
                String.format("Quote not found for given quote code %s", quoteCode)));
    }

	/**
	 * Approve docu Sign of GVPN
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
    private GscApproveQuoteContext approveDocuSignForGVPN(GscApproveQuoteContext context) throws TclCommonException {
		QuoteGsc quoteGsc = context.quoteGsc.stream().findFirst().get();
		boolean isNew = NEW.equalsIgnoreCase(context.quoteToLe.stream().findFirst().get().getQuoteType());
		if (Objects.nonNull(quoteGsc) && MPLS.equalsIgnoreCase(quoteGsc.getAccessType()) && isNew) {
			gvpnQuoteService.approvedDocusignQuotes(context.quote.getQuoteCode());
		}
		return context;
	}


	private GscApproveQuoteContext updateQuoteLeStage(GscApproveQuoteContext context) {
			context.quote.getQuoteToLes().forEach(quoteLe -> {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}
			});
    	return context;
	}

	/**
	 * Update audit information after docu sign and manual cof upload
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private GscApproveQuoteContext updateManualOrderConfirmationAudit(GscApproveQuoteContext context) throws TclCommonException {
		try {
			OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository.findByOrderRefUuid(context.order.getOrderCode());
			if (Objects.isNull(orderConfirmationAudit)) {
				orderConfirmationAudit = new OrderConfirmationAudit();
			}
			orderConfirmationAudit.setName(Utils.getSource());
			orderConfirmationAudit.setMode(MANUAL.toString());
			orderConfirmationAudit.setUploadedBy(Utils.getSource());
			orderConfirmationAudit.setPublicIp(context.ipAddress);
			orderConfirmationAudit.setOrderRefUuid(context.order.getOrderCode());
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			throw new TclCommonRuntimeException(COMMON_ERROR, e, R_CODE_ERROR);
		}
		return context;
	}

	private GscApproveQuoteContext updateAttachmentAuditInfo(GscApproveQuoteContext context) {
		LOGGER.info("Order To Le ID :: {}", context.orderToLe);
		for (OrderToLe orderToLe : context.orderToLe) {
			if(context.isDocuSign) {
				List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
						.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES,
								context.quote.getId(), AttachmentTypeConstants.COF.toString());
				for (OmsAttachment omsAttachment : omsAttachmentList) {
					omsAttachment.setOrderToLe(orderToLe);
					omsAttachment.setReferenceName(CommonConstants.ORDERS);
					omsAttachment.setReferenceId(context.order.getId());
					omsAttachmentRepository.save(omsAttachment);
				}
			}
			try {
				gscQuotePdfService.downloadCofFromStorageContainer(null, null, context.order.getId(),
						orderToLe.getId(), context.cofObjectMapper);
			} catch (Exception e) {
				throw new TclCommonRuntimeException(COMMON_ERROR, e, R_CODE_ERROR);
			}
			break;
		}
		return context;
	}

	/**
	 * Update Outpulse value to inventory
	 *
	 * @param orderCode
	 */
	public void checkChangeOutpulseStatusAndUpdate(String orderCode) throws TclCommonException {
		// create get api
		// check the status
		// we have si_order_id in order_to_le table for change outpulse
		// update the service inventory if success
		// update the thirdparty with 'SUCCESS' or 'STRUCK'
		LOGGER.info("Received order :: {}", orderCode);
		try {
			Order order = orderRepository.findByOrderCode(orderCode);
			List<GetOrderDetail> statusDetails = tigerServiceHelper.getStatusDetails(order.getId(), order.getOrderCode());
			statusDetails.stream().findFirst().ifPresent(getOrderDetail -> {
				if (COMPLETED.equalsIgnoreCase(getOrderDetail.getState())) {
					OrderToLe orderToLe = orderToLeRepository.findByOrder_OrderCode(orderCode).get(0);
					Integer siOrderId = orderToLe.getErfServiceInventoryParentOrderId();
					String outpulse = getOutpulseAttributeValue(orderToLe, ATTR_TERMINATION_NUMBER_OUTPULSE);
					String gscreferenceOrderId = getOutpulseAttributeValue(orderToLe, GSC_MACD_PRODUCT_REFERENCE_ORDER_ID);
					updateOutpulseToInventory(siOrderId, outpulse, gscreferenceOrderId);
					updateThirdPartyServiceStatus(orderCode, SfdcServiceStatus.SUCCESS.toString());
				} else {
					updateThirdPartyServiceStatus(orderCode, SfdcServiceStatus.STRUCK.toString());
				}
			});
		} catch (Exception e) {
			updateThirdPartyServiceStatus(orderCode, SfdcServiceStatus.STRUCK.toString());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private String getOutpulseAttributeValue(OrderToLe orderToLe, String attributeName) {
		AtomicReference<String> value = new AtomicReference<>("");
		List<OrderGsc> orderGscs = orderGscRepository.findByOrderToLe(orderToLe);
		orderGscs.stream().findFirst().ifPresent(orderGsc -> {
			List<OrderGscDetail> orderGscDetails = orderGscDetailRepository.findByorderGsc(orderGsc);
			orderGscDetails.stream().findFirst().ifPresent(orderGscDetail -> {
				value.set(gscOrderManagementRequestBuilder.getProductMasterAttribute(orderGsc, orderGscDetail, attributeName));
			});
		});

		return value.get();
	}

	private void updateOutpulseToInventory(Integer siOrderId, String outpulse, String gscreferenceOrderId) {
		LOGGER.info("SI Order ID :: {} and Outpulse :: {}", siOrderId, outpulse);
		// queue call to inventory with si order id and outpulse value
		try {
			StringBuilder request = new StringBuilder();
			request.append(siOrderId);
			request.append(COLON);
			request.append(outpulse);
			request.append(COLON);
			request.append(gscreferenceOrderId);
			mqUtils.send(updateOutpulseInventory, request.toString());
		} catch (TclCommonException e) {
			LOGGER.warn("Error in updating outpulse :: {} ", e);
		}
	}

	private void updateThirdPartyServiceStatus(String orderCode, String status) {
		List<ThirdPartyServiceJob> thirdPartyServiceJobs = thirdPartyServiceJobsRepository.findByRefIdAndThirdPartySource(
				orderCode, CHANGE_OUTPULSE.toString());
		thirdPartyServiceJobs.stream().findFirst().ifPresent(thirdPartyServiceJob -> {
			thirdPartyServiceJob.setServiceStatus(status);
			thirdPartyServiceJobsRepository.save(thirdPartyServiceJob);
		});
	}
    
    /**
     * Method to update order le attribute values
     *
     * @param orderId
     * @param orderLeId
     * @param orderLeAttributeBeans
     * @return {@link List<GscOrderLeAttributeBean>}}
     */
    public List<GscOrderLeAttributeBean> updateOrderToLeAttributes(Integer orderId, Integer orderLeId, List<GscOrderLeAttributeBean> orderLeAttributeBeans) {
        if (!CollectionUtils.isEmpty(orderLeAttributeBeans)) {
            orderLeAttributeBeans.stream().forEach(orderLeAttributeBean -> {
                OrdersLeAttributeValue orderLeAttribute = null;
                List<OrdersLeAttributeValue> ordersLeAttributeValues = ordersLeAttributeValueRepository.findByOrderIDAndMstOmsAttributeName(orderId, orderLeAttributeBean.getAttributeName());
            	if (Objects.nonNull(ordersLeAttributeValues) && !ordersLeAttributeValues.isEmpty()){
                     orderLeAttribute = ordersLeAttributeValues.stream().findFirst().get();
                     if (Objects.nonNull(orderLeAttribute) && Objects.nonNull(orderLeAttribute.getAttributeValue()) )
                    	 LOGGER.info("Updating order le attribute of id {} ", orderLeAttribute.getId());
                 } else {
                    LOGGER.info("Creating order le attribute with OrderToLeId {} and mst oms attribute name {} ", orderLeId, orderLeAttributeBean.getAttributeName());
                    orderLeAttribute = new OrdersLeAttributeValue();
                    orderLeAttribute.setOrderToLe(orderToLeRepository.findById(orderLeId).get());
                    orderLeAttribute.setMstOmsAttribute(mstOmsAttributeRepository.findByNameAndIsActive(orderLeAttributeBean.getAttributeName(), (byte) 1).stream().findFirst().get());
                }
//                if (orderLeAttributeBean.getAttributeName().contains("Multi Macd") && !"Multi Macd Trunk Data Type".equalsIgnoreCase(orderLeAttributeBean.getAttributeName())){
//                    AdditionalServiceParams additionalServiceParams = saveAdditionalServiceParamsForMultiMacd(orderId, orderLeAttributeBean);
//                    LOGGER.info("AdditionalServiceParams ID :: {}", additionalServiceParams.getId());
//                    orderLeAttribute.setAttributeValue(String.valueOf(additionalServiceParams.getId()));
//                }
//                else{
//                    orderLeAttribute.setAttributeValue(orderLeAttributeBean.getAttributeValue());
//                }

				if (MULTI_MACD_TRUNK_DATA_TYPE.equalsIgnoreCase(orderLeAttributeBean.getAttributeName())){
					orderLeAttribute.setAttributeValue(orderLeAttributeBean.getAttributeValue());
				} else{
					AdditionalServiceParams additionalServiceParams = saveAdditionalServiceParamsForMultiMacd(orderId, orderLeAttributeBean);
					LOGGER.info("AdditionalServiceParams ID :: {}", additionalServiceParams.getId());
					orderLeAttribute.setAttributeValue(String.valueOf(additionalServiceParams.getId()));
				}
                ordersLeAttributeValueRepository.save(orderLeAttribute);
                orderLeAttributeBean.setId(orderLeAttribute.getId());
            });
        }
        return orderLeAttributeBeans;
    }
    
    
    private AdditionalServiceParams saveAdditionalServiceParamsForMultiMacd(Integer orderId, GscOrderLeAttributeBean orderLeAttributeBean) {
        AdditionalServiceParams additionalServiceParams = new AdditionalServiceParams();
        additionalServiceParams.setReferenceId(String.valueOf(orderId));
        additionalServiceParams.setCategory("OrderId");
        additionalServiceParams.setValue(orderLeAttributeBean.getAttributeValue());
        additionalServiceParams.setCreatedBy(Utils.getSource());
        additionalServiceParams.setCreatedTime(new Date());
        additionalServiceParams.setIsActive(CommonConstants.Y);
        additionalServiceParams.setAttribute(mstOmsAttributeRepository.findByNameAndIsActive(orderLeAttributeBean.getAttributeName(), (byte) 1)
                .stream().findFirst().get().getName());
        additionalServiceParams = additionalServiceParamRepository.save(additionalServiceParams);
        return additionalServiceParams;
    }

    
	/**
	 * Upload confirmation excel and save the response as json in table
	 *
	 * @param orderCode
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public String uploadApacEmailConfirmationInExcel(String orderCode, MultipartFile file) throws TclCommonException {
		Objects.requireNonNull(orderCode, GscConstants.ORDER_CODE_NULL_MESSAGE);
		OrderToLe orderToLe = orderToLeRepository.findByOrder_OrderCode(orderCode).stream().findAny().get();
		LOGGER.info("Order to le id for email confirmation of APAC country is {} ", orderToLe.getId());
		Integer erfAttachmentId = uploadFilesByObjectOrFileStorage(file, orderToLe);

		return erfAttachmentId.toString();
	}

	/**
	 * Upload files by object or file storage
	 *
	 * @param file
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */
	private Integer uploadFilesByObjectOrFileStorage(MultipartFile file, OrderToLe orderToLe)
			throws TclCommonException {
		LOGGER.info("Saving the uploaded file by object or file storage");
		Integer erfAttachmentId = null;
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				StoredObject storedObject = fileStorageService.uploadFiles(
						GscAttachmentTypeConstants.GSC_APAC_EMAIL_CONFIRMATION_PDF, "GSC_APAC_EMAIL_CONFIRMATION_FILES",
						file.getInputStream());
				if (Objects.nonNull(storedObject) && !StringUtils.isEmpty(storedObject.getURL())) {
					String objectStorageUrl = storedObject.getURL().substring(
							storedObject.getURL().indexOf(swiftApiContainer), storedObject.getURL().lastIndexOf("/"));
					objectStorageUrl = objectStorageUrl.replaceAll("%2F", "/").replaceAll("%20", " ");
					String updatedFileName = storedObject.getName();
					if (Objects.nonNull(objectStorageUrl)) {
						erfAttachmentId = setAttachmentDetails(orderToLe, objectStorageUrl, updatedFileName,
								GscConstants.APAC_EMAIL_CONFIRMATION_PDF);
					}
				}
			} else {
				erfAttachmentId = gscAttachmentHelper.saveFileAttachment(file.getInputStream(),
						GscConstants.APAC_EMAIL_CONFIRMATION_PDF);
				if (Objects.nonNull(erfAttachmentId)) {
					createOrUpdateOmsAttachment(orderToLe, erfAttachmentId, GscConstants.APAC_EMAIL_CONFIRMATION_PDF);
				}
			}
		} catch (IOException e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		return erfAttachmentId;
	}

	/**
	 * Save object storage uploaded file in customer attachment table
	 *
	 * @param orderToLe
	 * @param objectStorageUrl
	 * @param updatedFileName
	 * @return
	 * @throws TclCommonException
	 */
	private Integer setAttachmentDetails(OrderToLe orderToLe, String objectStorageUrl, String updatedFileName,
			String referenceName) throws TclCommonException {
		AttachmentBean attachmentBean = new AttachmentBean();
		attachmentBean.setPath(objectStorageUrl);
		attachmentBean.setFileName(updatedFileName);
		String attachmentRequest = GscUtils.toJson(attachmentBean);
		LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		Integer erfAttachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
		LOGGER.info("Attachment id is {}", erfAttachmentId);
		createOrUpdateOmsAttachment(orderToLe, erfAttachmentId, referenceName);
		return erfAttachmentId;
	}

	/**
	 * create or update oms attachment based on erf attachment id
	 *
	 * @param orderToLe
	 * @param erfAttachmentId
	 */
	private void createOrUpdateOmsAttachment(OrderToLe orderToLe, Integer erfAttachmentId, String referenceName) {
		LOGGER.info("Create or update oms attachment");
		if (Objects.nonNull(erfAttachmentId)) {
			List<OmsAttachment> omsAttachments = omsAttachmentRepository
					.findByOrderToLeAndAttachmentTypeAndReferenceName(orderToLe, GscConstants.OTHERS, referenceName);
			if (!CollectionUtils.isEmpty(omsAttachments)) {
				OmsAttachment omsAttachment = omsAttachments.stream().findFirst().get();
				omsAttachment.setErfCusAttachmentId(erfAttachmentId);
				omsAttachmentRepository.save(omsAttachment);
			} else {
				createOmsAttachment(erfAttachmentId, orderToLe, referenceName);
			}
			;
		}
	}

	/**
	 * create oms Attachment
	 *
	 * @param erfAttachmentId
	 * @param orderToLe
	 * @param referenceName
	 * @return
	 */
	private OmsAttachment createOmsAttachment(Integer erfAttachmentId, OrderToLe orderToLe, String referenceName) {
		LOGGER.info("Creating oms attachment");
		OmsAttachment omsAttachment = new OmsAttachment();
		omsAttachment.setErfCusAttachmentId(erfAttachmentId);
		omsAttachment.setOrderToLe(orderToLe);
		omsAttachment.setAttachmentType("Others");
		omsAttachment.setReferenceName(referenceName);
		omsAttachmentRepository.save(omsAttachment);
		return omsAttachment;
	}
}
