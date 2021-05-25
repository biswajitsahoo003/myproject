package com.tcl.dias.oms.npl.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.ACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.NONE;
import static com.tcl.dias.common.constants.CommonConstants.NPL;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.oms.constants.MACDConstants.CHANGE_BANDWIDTH;
import static com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants.CROSS_CONNECT_LOCAL_DEMARCATION_ID;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_THROUGH_CLASSIFICATION;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_WITH_CLASSIFICATION;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CrossConnectLocalITDemarcationBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.DashBoardFamilyBean;
import com.tcl.dias.oms.beans.DashboardCustomerbean;
import com.tcl.dias.oms.beans.DashboardLegalEntityBean;
import com.tcl.dias.oms.beans.DashboardQuoteBean;
import com.tcl.dias.oms.beans.ExcelBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.OrderFamilySVBean;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrderLeSVBean;
import com.tcl.dias.oms.beans.OrderLinkRequest;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.OrderSummaryBean;
import com.tcl.dias.oms.beans.PricingDetailBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.LinkStagingConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossConnectPricingResponse;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossconnectConstants;
import com.tcl.dias.oms.crossconnect.service.v1.CrossConnectOrderService;
import com.tcl.dias.oms.dto.OrderToLeDto;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStage;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
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
import com.tcl.dias.oms.entity.entities.OrderSiteBillingDetails;
import com.tcl.dias.oms.entity.entities.OrderSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteSiteBillingDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStatusRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
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
import com.tcl.dias.oms.entity.repository.OrderSiteBillingDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteProvisioningRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteBillingDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.lr.service.OrderLrService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.DashboardOrdersBean;
import com.tcl.dias.oms.npl.beans.NplFeasiblityBean;
import com.tcl.dias.oms.npl.beans.NplLinkBean;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplPricingFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.OrderNplSiteBean;
import com.tcl.dias.oms.npl.beans.OrderProductSolutionBean;
import com.tcl.dias.oms.npl.beans.OrderToLeBean;
import com.tcl.dias.oms.npl.beans.OrderToLeProductFamilyBean;
import com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.pricing.bean.Feasible;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pricing.bean.NotFeasible;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the NplOrderService.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class NplOrderService {
	
	@Autowired
	OrderIllSitesRepository orderNplSitesRepository;

	@Autowired
	OrderNplLinkRepository orderNplLinkRepository;

	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	NplLinkRepository nplLinkRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	OrderSiteProvisioningRepository ordersiteProvsioningRepository;

	@Autowired
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	OrderIllSiteSlaRepository orderNplSiteSlaRepository;

	@Autowired
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;

	@Autowired
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;

	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;

	@Autowired
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;

	@Autowired
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	MstOrderLinkStageRepository mstOrderLinkStageRepository;

	@Autowired
	MstOrderLinkStatusRepository mstOrderLinkStatusRepository;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	NplQuotePdfService nplQuotePdfService;

	@Autowired
	OrderLrService orderLrService;

	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;

	@Autowired
	NplQuoteService nplQuoteService;

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NplOrderService.class);

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${rabbitmq.si.npl.details.queue}")
	String siNplDetailsQueue;

	@Value("${notification.mail.template}")
	String delegationTemplateId;

	@Value("${notification.mail.loginurl}")
	String loginUrl;

	@Value("${rabbitmq.location.request}")
	String locationQueue;

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

	@Value("${rabbitmq.location.address.request}")
	String apiAddressQueue;

	@Value("${rabbitmq.poplocation.detail}")
	String popQueue;

	@Value("${rabbitmq.location.itcontact.request}")
	String locationItContactQueue;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeQueue;

	@Value("${rabbitmq.site.gst.queue}")
	String siteGstQueue;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OrdersLeAttributeValueRepository orderToLeAttributeValueRespository;

	@Autowired
	OrderSiteProvisioningRepository orderSiteProvisioningRepository;

	@Autowired
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@Autowired
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@Autowired
	OrderSiteStatusAuditRepository orderSiteStatusAuditRepository;

	@Autowired
	OrderSiteStageAuditRepository orderSiteStageAuditRepository;

	@Autowired
	OrderLinkStatusAuditRepository orderLinkStatusAuditRepository;

	@Autowired
	OrderLinkStageAuditRepository orderLinkStageAuditRepository;

	@Autowired
	NotificationService notificationService;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Value("${rabbitmq.customer.billing.contact.billingId.queue}")
	String customerBillingContactInfo;

	@Value("${rabbitmq.customer.crn.queue}")
	String customerCrnQueue;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Autowired
	CrossConnectOrderService crossConnectOrderService;

	@Value("${rabbitmq.odr.oe.process.queue}")
	String odrOrderEnrichmentProcessQueue;
	
	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Value("${rabbitmq.mstaddrbylocationid.detail}")
	String addressDetailByLocationId;
	
	@Autowired
	GstInService gstInService;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

    @Value("${rabbitmq.location.localitcontact}")
    String localItQueue;

    @Autowired
    PartnerService partnerService;

	@Value("${rabbitmq.pop.location.queue}")
	String popLocationQueue;
	
	@Autowired
	CancellationService cancellationService;

	@Autowired
	OrderSiteServiceTerminationDetailsRepository orderSiteServiceTerminationDetailsRepository;

	
	@Autowired
	QuoteSiteBillingDetailsRepository quoteSiteBillingInfoRepository;
	
	@Autowired
	OrderSiteBillingDetailsRepository orderSiteBillingInfoRepository;
	



	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;


	/**
	 * @link http://www.tatacommunications.com/ getOrderDetails- This method is used
	 *       to get the order details
	 * 
	 * @param orderId
	 * @return OrdersBean
	 * @throws TclCommonException
	 */

	public NplOrdersBean getOrderDetails(Integer orderId) throws TclCommonException {
		NplOrdersBean ordersBean = null;
		try {
			if (Objects.isNull(orderId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			}
			Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);

			if (order == null) {
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			ordersBean = constructOrder(order);
			ordersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBean;
	}

	/**
	 * 
	 * getOmsAttachments - This method is used to fetch the attachments for a order
	 * 
	 * @param orderId
	 * @return List<OmsAttachBean>
	 * @throws TclCommonException
	 */
	public List<OmsAttachBean> getOmsAttachments(Integer orderToLeId) throws TclCommonException {
		List<OmsAttachBean> omsAttachBeans = null;
		try {
			if (Objects.isNull(orderToLeId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
			if (orderToLe.isPresent()) {
				List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByOrderToLe(orderToLe.get());
				omsAttachBeans = omsAttachments.stream().map(omsEntity -> {
					OmsAttachBean omsBean = new OmsAttachBean();
					omsBean.setAttachmentId(omsEntity.getErfCusAttachmentId());
					omsBean.setAttachmentType(omsEntity.getAttachmentType());
					omsBean.setQouteLeId(omsEntity.getQuoteToLe().getId());
					omsBean.setOrderLeId(omsEntity.getOrderToLe().getId());
					omsBean.setReferenceId(omsEntity.getReferenceId());
					omsBean.setReferenceName(omsEntity.getReferenceName());
					return omsBean;
				}).collect(Collectors.toList());
			}

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return omsAttachBeans;
	}

	/**
	 * constructOrder - Method to construct order bean
	 * 
	 * @param orders
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public NplOrdersBean constructOrder(Order orders) throws TclCommonException {
		NplOrdersBean orderBean = new NplOrdersBean();
		try {
			if (Objects.isNull(orders)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			orderBean.setId(orders.getId());
			orderBean.setCreatedBy(orders.getCreatedBy());
			orderBean.setOrderCode(orders.getOrderCode());
			orderBean.setCreatedTime(orders.getCreatedTime());
			orderBean.setStatus(orders.getStatus());
			orderBean.setTermInMonths(orders.getTermInMonths());
			orderBean.setOrderToLeBeans(constructOrderLeEntityDtos(orders));
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderBean;

	}

	/**
	 * constructOrderLeEntityDtos - Method to construct legal entity dtos
	 * 
	 * @param order
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public Set<OrderToLeBean> constructOrderLeEntityDtos(Order order) throws TclCommonException {
		Set<OrderToLeBean> orderToLeDtos = new HashSet<>();
		try {
			if (Objects.isNull(order)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			if (getOrderToLeBasenOnVersion(order) != null) {
				for (OrderToLe orTle : getOrderToLeBasenOnVersion(order)) {
					OrderToLeBean orderToLe = new OrderToLeBean(orTle);
					orderToLe.setTermInMonths(orTle.getTermInMonths());
					orderToLe.setCurrency(orTle.getCurrencyCode());
					orderToLe.setLegalAttributes(constructLegalAttributes(orTle));
					orderToLe.setOrderType(orTle.getOrderType());
					orderToLe.setOrderCategory(orTle.getOrderCategory());
					orderToLe.setOrderToLeProductFamilyBeans(
							constructOrderToLeFamilyDtos(getProductFamilyBasenOnVersion(orTle)));
					
					//added for site wise billing
					if (orTle.getSiteLevelBilling() != null) {
						if (orTle.getSiteLevelBilling().equalsIgnoreCase("1")) {
							orderToLe.setSiteWiseBilling(true);
						}
					}
					
					orderToLeDtos.add(orderToLe);

				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderToLeDtos;

	}

	/**
	 * constructLegalAttributes - Method to construct legal attributes
	 * 
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */
	public Set<LegalAttributeBean> constructLegalAttributes(OrderToLe orderToLe) throws TclCommonException {
		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		try {

			if (Objects.isNull(orderToLe)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			List<OrdersLeAttributeValue> attributeValues = ordersLeAttributeValueRepository.findByOrderToLe(orderToLe);
			if (attributeValues != null) {
				attributeValues.stream().forEach(attrVal -> {
					LegalAttributeBean attributeBean = new LegalAttributeBean();
					attributeBean.setAttributeValue(attrVal.getAttributeValue());
					attributeBean.setDisplayValue(attrVal.getDisplayValue());
					attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
					leAttributeBeans.add(attributeBean);

				});

			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return leAttributeBeans;
	}

	/**
	 * constructMstAttributBean - Method to constuct MstOmsAttributeBean
	 * 
	 * @param mstOmsAttribute
	 * @return
	 */
	public MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
		MstOmsAttributeBean mstOmsAttributeBean = null;
		if (mstOmsAttribute != null) {
			mstOmsAttributeBean = new MstOmsAttributeBean();
			mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
			mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
		}
		return mstOmsAttributeBean;
	}

	/**
	 * getOrderToLeBasenOnVersion - Method to get orderToLe based on version
	 * 
	 * @param orders
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderToLe> getOrderToLeBasenOnVersion(Order orders) throws TclCommonException {
		List<OrderToLe> orToLes = null;
		try {
			if (Objects.isNull(orders)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			orToLes = orderToLeRepository.findByOrder(orders);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orToLes;
	}

	/**
	 * getProductSolutionBasenOnVersion - Method to get product solution based on
	 * version
	 * 
	 * @param family
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductSolution> getProductSolutionBasenOnVersion(OrderToLeProductFamily family)
			throws TclCommonException {
		List<OrderProductSolution> productSolutions = null;
		try {
			if (Objects.isNull(family)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			productSolutions = orderProductSolutionRepository.findByOrderToLeProductFamily(family);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return productSolutions;
	}

	/**
	 * getComponentBasedOnIdVersionType - Method to get component based on site id ,
	 * version and type
	 * 
	 * @param siteId
	 * @param version
	 * @param type
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductComponent> getComponentBasedOnIdVersionType(Integer siteId, String type,
			String productFamilyName) throws TclCommonException {
		List<OrderProductComponent> components = null;
		try {
			if (Objects.isNull(siteId) || Objects.isNull(type) || StringUtils.isEmpty(type)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			components = orderProductComponentRepository.findByReferenceIdAndTypeAndMstProductFamily_Name(siteId, type,
					productFamilyName);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return components;
	}

	/**
	 * getProductFamilyBasenOnVersion - Method to get product family based on
	 * version and orderToLe
	 * 
	 * @param orderToLe
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderToLeProductFamily> getProductFamilyBasenOnVersion(OrderToLe orderToLe) throws TclCommonException {
		List<OrderToLeProductFamily> prodFamilys = null;

		try {
			if (Objects.isNull(orderToLe))
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			prodFamilys = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return prodFamilys;

	}

	/**
	 * constructOrderToLeFamilyDtos - Method to construct orderToLeProductFamilyBean
	 * 
	 * @param orderToLeProductFamilies
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public Set<OrderToLeProductFamilyBean> constructOrderToLeFamilyDtos(
			List<OrderToLeProductFamily> orderToLeProductFamilies) throws TclCommonException {
		Set<OrderToLeProductFamilyBean> orderToLeProductFamilyBeans = new HashSet<>();
		try {
			if (Objects.isNull(orderToLeProductFamilies) || orderToLeProductFamilies.isEmpty())
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			for (OrderToLeProductFamily orFamily : orderToLeProductFamilies) {
				OrderToLeProductFamilyBean orderToLeProductFamilyBean = new OrderToLeProductFamilyBean();
				if (orFamily.getMstProductFamily() != null) {
					orderToLeProductFamilyBean.setStatus(orFamily.getMstProductFamily().getStatus());
					orderToLeProductFamilyBean.setProductName(orFamily.getMstProductFamily().getName());
				}
				List<OrderProductSolutionBean> orderProductSolutionBeans = getSortedSolution(
						constructProductSolution(getProductSolutionBasenOnVersion(orFamily)));
				orderToLeProductFamilyBean.setOrderProductSolutions(orderProductSolutionBeans);
				orderToLeProductFamilyBeans.add(orderToLeProductFamilyBean);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderToLeProductFamilyBeans;
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution - Method to construct OrderProductSolutionBean
	 * @param productSolutions
	 * @return Set<OrderProductSolutionBean>
	 * @throws TclCommonException
	 */
	public List<OrderProductSolutionBean> constructProductSolution(List<OrderProductSolution> productSolutions)
			throws TclCommonException {
		List<OrderProductSolutionBean> productSolutionBeans = new ArrayList<>();
		try {
			if (Objects.isNull(productSolutions) || productSolutions.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			for (OrderProductSolution solution : productSolutions) {
				OrderProductSolutionBean orderProductSolutionBean = new OrderProductSolutionBean();
				orderProductSolutionBean.setId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					orderProductSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					orderProductSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					orderProductSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}
				if(Objects.nonNull(solution.getMstProductOffering()) && Objects.nonNull(solution.getMstProductOffering().getProductName()) &&
						CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())){
					List<OrderIllSiteBean> orderIllSiteBeans = crossConnectOrderService.getSortedIllSiteDtos(
							crossConnectOrderService.constructIllSiteDtos(crossConnectOrderService.getIllsitesBasenOnVersion(solution)));
					orderProductSolutionBean.setOrderCrossConnectSite(orderIllSiteBeans);
					productSolutionBeans.add(orderProductSolutionBean);
				}else {
					List<OrderIllSite> orderNplSiteList = null;
					List<OrderNplLink> orderNplLinkList = orderNplLinkRepository.findByProductSolutionId(solution.getId());
					List<NplLinkBean> nplLinkBeanList = new ArrayList<>();
					if (orderNplLinkList != null && !orderNplLinkList.isEmpty()) {
						for (OrderNplLink orderNplLinkBean : orderNplLinkList) {
							orderNplSiteList = new ArrayList<>();
							OrderIllSite orderNplSiteA = getSitesBasedOnNplLink(orderNplLinkBean.getSiteAId());
							OrderIllSite orderNplSiteB = getSitesBasedOnNplLink(orderNplLinkBean.getSiteBId());
							orderNplSiteList.add(orderNplSiteA);
							orderNplSiteList.add(orderNplSiteB);
							List<OrderNplSiteBean> OrdeNplSiteBeans = getSortedNplSiteDtos(
									constructNplSiteDtos(orderNplSiteList));
							nplLinkBeanList.add(constructNplLinkBean(orderNplLinkBean, OrdeNplSiteBeans,
									solution.getOrderToLeProductFamily().getMstProductFamily().getName()));

						}
					}
					orderProductSolutionBean.setNplLinkBean(nplLinkBeanList);
					productSolutionBeans.add(orderProductSolutionBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return productSolutionBeans;
	}

	/**
	 * constructNplLinkBean
	 * 
	 * @param orderNplLink      - Method to construct NplLinkBean
	 * @param orderNplSiteBeans
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public NplLinkBean constructNplLinkBean(OrderNplLink orderNplLink, List<OrderNplSiteBean> orderNplSiteBeans,
			String productFamilyName) throws TclCommonException {
		NplLinkBean nplLinkBean = null;
		try {
			if (Objects.isNull(orderNplLink) || Objects.isNull(orderNplSiteBeans) || orderNplSiteBeans.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			nplLinkBean = new NplLinkBean(orderNplLink);
			nplLinkBean.setQuoteSla(constructOrderLinkSla(orderNplLink.getOrderNplLinkSlas()));
			nplLinkBean.setOrderSites(orderNplSiteBeans);

			if (orderNplLink.getStatus() == 1) {
				List<OrderProductComponentBean> linkComp = getSortedComponents(
						constructOrderProductComponent(orderNplLink.getId(), CommonConstants.LINK, productFamilyName));
				nplLinkBean.setOrderProductComponentBeans(linkComp);

				List<OrderProductComponentBean> siteAComp = getSortedComponents(constructOrderProductComponent(
						orderNplLink.getSiteAId(), CommonConstants.SITEA, productFamilyName));
				nplLinkBean.getOrderProductComponentBeans().addAll(siteAComp);

				List<OrderProductComponentBean> siteBComp = getSortedComponents(constructOrderProductComponent(
						orderNplLink.getSiteBId(), CommonConstants.SITEB, productFamilyName));
				nplLinkBean.getOrderProductComponentBeans().addAll(siteBComp);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return nplLinkBean;
	}

	/**
	 * constructOrderLinkSla - method to construct quote sla beans
	 * 
	 * @param orderLinkSlas
	 * @return
	 * @throws TclCommonException
	 */
	public List<QuoteSlaBean> constructOrderLinkSla(Set<OrderNplLinkSla> orderLinkSlas) throws TclCommonException {
		List<QuoteSlaBean> slaSet = new ArrayList<>();

		try {
			/*if (Objects.isNull(orderLinkSlas) || orderLinkSlas.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}*/

			if (orderLinkSlas != null) {
				for (OrderNplLinkSla linkSla : orderLinkSlas) {
					QuoteSlaBean slaBean = new QuoteSlaBean();
					slaBean.setId(linkSla.getId());
					slaBean.setSlaEndDate(linkSla.getSlaEndDate());
					slaBean.setSlaMaster(constructSlaMasterBean(linkSla.getSlaMaster()));
					slaBean.setSlaStartDate(linkSla.getSlaStartDate());
					slaBean.setSlaValue(Utils.convertEval(linkSla.getSlaValue()));
					slaSet.add(slaBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return slaSet;
	}

	/**
	 * constructSlaMasterBean - method to construct sla master bean
	 * 
	 * @param slaMaster
	 * @return
	 */
	private SlaMasterBean constructSlaMasterBean(SlaMaster slaMaster) {
		SlaMasterBean masterBean = new SlaMasterBean();
		masterBean.setId(slaMaster.getId());
		masterBean.setSlaDurationInDays(slaMaster.getSlaDurationInDays());
		masterBean.setSlaName(slaMaster.getSlaName());
		return masterBean;
	}

	/**
	 * getSitesBasedOnNplLink
	 * 
	 * @param siteId
	 * @return
	 */
	private OrderIllSite getSitesBasedOnNplLink(Integer siteId) {
		OrderIllSite illsites = null;

		if (siteId != null) {
			illsites = orderNplSitesRepository.findByIdAndStatus(siteId, (byte) 1);
		}

		return illsites;

	}

	/**
	 * constructNplSiteDtos
	 * 
	 * @param nplSites
	 * @param version
	 * @return
	 */
	private List<OrderNplSiteBean> constructNplSiteDtos(List<OrderIllSite> nplSites) {
		List<OrderNplSiteBean> sites = new ArrayList<>();
		if (nplSites != null) {
			for (OrderIllSite nplSite : nplSites) {
				if (nplSite.getStatus() == 1) {
					OrderNplSiteBean nplSiteBean = new OrderNplSiteBean(nplSite);
					if (nplSite.getMstOrderSiteStage() != null) {
						nplSiteBean.setCurrentStage(nplSite.getMstOrderSiteStage().getName());
					}
					if (nplSite.getMstOrderSiteStatus() != null) {
						nplSiteBean.setCurrentStatus(nplSite.getMstOrderSiteStatus().getName());
					}
					sites.add(nplSiteBean);
				}
			}
		}
		return sites;
	}

	/**
	 * constructOrderProductComponent - Method to construct
	 * OrderProductComponentBean list
	 * 
	 * @param id
	 * @param version
	 * @param type
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductComponentBean> constructOrderProductComponent(Integer id, String type,
			String productFamilyName) throws TclCommonException {
		List<OrderProductComponentBean> orderProductComponentDtos = new ArrayList<>();

		try {
			if (Objects.isNull(id) || Objects.isNull(type) || StringUtils.isEmpty(type)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			List<OrderProductComponent> productComponents = getComponentBasedOnIdVersionType(id, type,
					productFamilyName);

			if (productComponents != null) {

				for (OrderProductComponent quoteProductComponent : productComponents) {
					OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
					orderProductComponentBean.setId(id);
					orderProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
					if (quoteProductComponent.getMstProductComponent() != null) {
						orderProductComponentBean
								.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
						orderProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
					}
					orderProductComponentBean.setType(quoteProductComponent.getType());
					orderProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
					List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
							constructAttribute(quoteProductComponent.getOrderProductComponentsAttributeValues()));
					orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
					orderProductComponentDtos.add(orderProductComponentBean);
				}

			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderProductComponentDtos;

	}

	/**
	 * constructComponentPriceDto
	 * 
	 * @param orderProductComponent
	 * @param version
	 * @return
	 */
	private QuotePriceBean constructComponentPriceDto(OrderProductComponent orderProductComponent) {
		QuotePriceBean priceDto = null;
		if (orderProductComponent != null && orderProductComponent.getMstProductComponent() != null) {
			OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
			priceDto = new QuotePriceBean(price);
		}
		return priceDto;

	}

	/**
	 * constructAttributePriceDto
	 * 
	 * @param attributeValue
	 * @param version
	 * @return
	 */
	private QuotePriceBean constructAttributePriceDto(OrderProductComponentsAttributeValue attributeValue) {
		QuotePriceBean priceDto = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			OrderPrice attrPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.COMPONENTS.toString());
			priceDto = new QuotePriceBean(attrPrice);
		}
		return priceDto;

	}

	/**
	 * constructAttribute
	 * 
	 * @param orderProductComponentsAttributeValues
	 * @param version
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (orderProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValueBean qtAttributeValue = null;
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> additionalServiceParamEntity = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeValue.getAttributeValues()));
					if (additionalServiceParamEntity.isPresent()) {
						qtAttributeValue = new OrderProductComponentsAttributeValueBean(attributeValue,
								additionalServiceParamEntity.get().getValue());
					}
				} else {
					qtAttributeValue = new OrderProductComponentsAttributeValueBean(
							attributeValue);
				}
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}

				qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
				orderProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return orderProductComponentsAttributeValueBean;
	}

	/**
	 * editLinkComponent - Method to edit attribute values with repsect to a
	 * component
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public NplQuoteDetail editLinkComponent(UpdateRequest request) throws TclCommonException {
		NplQuoteDetail quoteDetail = null;
		try {
			quoteDetail = new NplQuoteDetail();
			validateRequest(request);
			for (ComponentDetail cmpDetail : request.getComponentDetails()) {
				for (AttributeDetail attributeDetail : cmpDetail.getAttributes()) {
					Optional<OrderProductComponentsAttributeValue> attributeValue = orderProductComponentsAttributeValueRepository
							.findById(attributeDetail.getAttributeId());
					if (attributeValue.isPresent()) {
						attributeValue.get().setAttributeValues(attributeDetail.getValue());
						orderProductComponentsAttributeValueRepository.save(attributeValue.get());
					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * updateOrderSites - method to udpate site details
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public NplQuoteDetail updateOrderSites(UpdateRequest request) throws TclCommonException {
		NplQuoteDetail quoteDetail = null;
		try {
			quoteDetail = new NplQuoteDetail();
			LOGGER.info("Request received --- > {}",request);

			if (request.getSiteId() > 0) {
				OrderIllSite orderNplSiteEntity = orderNplSitesRepository.findByIdAndStatus(request.getSiteId(),
						(byte) 1);
				LOGGER.info("orderNplSiteEntity --> {}",orderNplSiteEntity);
				if (orderNplSiteEntity != null) {
					if (request.getRequestorDate() != null) {
						orderNplSiteEntity.setRequestorDate(request.getRequestorDate());
					}
					if (request.getServiceId() != null && !request.getServiceId().isEmpty()) {
						orderNplSiteEntity.setErfServiceInventoryTpsServiceId(request.getServiceId());
						LOGGER.info("service id is updated");
					}
					orderNplSitesRepository.save(orderNplSiteEntity);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * getDashboardDetails - used to get dashboard details of order
	 * 
	 * @param legalEntityId
	 * @return
	 * @throws TclCommonException
	 */
	public DashBoardBean getDashboardDetails(Integer legalEntityId) throws TclCommonException {
		DashBoardBean dashBoardBean = null;
		try {
			dashBoardBean = new DashBoardBean();
			List<DashboardCustomerbean> dashboardCustomerbeans = new ArrayList<>();

			if (legalEntityId != null) {
				getDashBoardDetailsForEntity(dashboardCustomerbeans, legalEntityId);

			} else {
				getDashboardDetailsForAllCustomers(dashboardCustomerbeans, dashBoardBean);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return dashBoardBean;
	}

	/**
	 * getDashBoardDetailsForEntity - to get dashboard details for the provided
	 * customer legal entity
	 * 
	 * @param dashBoardBean
	 * @param dashboardCustomerbeans
	 * @throws TclCommonException
	 */
	public void getDashBoardDetailsForEntity(List<DashboardCustomerbean> dashboardCustomerbeans, Integer legalEntityId)
			throws TclCommonException {

		try {
			if (Objects.isNull(legalEntityId) || Objects.isNull(dashboardCustomerbeans)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			List<OrderToLe> orderToLes = orderToLeRepository.findByErfCusCustomerLegalEntityId(legalEntityId);
			DashboardCustomerbean customerbean = new DashboardCustomerbean();
			int count = 0;
			Map<String, Integer> countMap = constructDashBoardBean(orderToLes, customerbean, count);
			customerbean.setTotalOrderCount(countMap.get("totalOrderCount"));
			customerbean.setActiveOrderCount(countMap.get("activeOrderCount"));
			dashboardCustomerbeans.add(customerbean);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * getDashboardDetailsForAllCustomers - To get dashboard details for all the
	 * customers
	 * 
	 * @param dashboardCustomerbeans
	 * @param dashBoardBean
	 * @throws TclCommonException
	 */
	public void getDashboardDetailsForAllCustomers(List<DashboardCustomerbean> dashboardCustomerbeans,
			DashBoardBean dashBoardBean) throws TclCommonException {
		if (Objects.isNull(dashboardCustomerbeans) || Objects.isNull(dashBoardBean)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
		if (customerDetails == null) {
			throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		}

		Map<Integer, List<CustomerDetail>> customerMap = new HashMap<>();

		groupBasedOnCustomer(customerMap, customerDetails);

		for (Entry<Integer, List<CustomerDetail>> cusId : customerMap.entrySet()) {
			DashboardCustomerbean customerbean = new DashboardCustomerbean();
			customerbean.setCustomerId(cusId.getKey());
			List<CustomerDetail> custDetails = customerMap.get(cusId.getKey());
			for (CustomerDetail custDetail : custDetails) {
				if (StringUtils.isBlank(customerbean.getCustomerName())) {
					customerbean.setCustomerName(custDetail.getCustomerName());
				}
				List<OrderToLe> orderToLes = orderToLeRepository
						.findByErfCusCustomerLegalEntityId(custDetail.getCustomerLeId());

				int count = 0;
				Map<String, Integer> countMap = constructDashBoardBean(orderToLes, customerbean, count);
				customerbean.setTotalOrderCount(countMap.get("totalOrderCount"));
				customerbean.setActiveOrderCount(countMap.get("activeOrderCount"));
				dashboardCustomerbeans.add(customerbean);
			}
		}
		dashBoardBean.setDashboardCustomerbeans(dashboardCustomerbeans);

	}

	/**
	 * constructDashBoardBean - to get dashboard details as map for the particular
	 * orderToLe and customer
	 * 
	 * @param orderToLes
	 * @param customerbean
	 * @param count
	 * @throws TclCommonException
	 */
	public Map<String, Integer> constructDashBoardBean(List<OrderToLe> orderToLes, DashboardCustomerbean customerbean,
			int count) throws TclCommonException {
		Map<String, Integer> countMap = new HashMap<>();
		int activeOrderCount = 0;
		try {
			if (Objects.isNull(orderToLes) || Objects.isNull(customerbean) || Objects.isNull(count)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			for (OrderToLe orderToLe : orderToLes) {
				if (orderToLe.getOrderToLeProductFamilies() != null
						&& !orderToLe.getOrderToLeProductFamilies().isEmpty()) {
					for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
						if (family.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.NPL)) {
							count++;
							if (!orderToLe.getOrder().getStage()
									.equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())) {
								activeOrderCount++;
							}
							DashboardLegalEntityBean daEntityBean = new DashboardLegalEntityBean();
							daEntityBean.setLegEntityId(orderToLe.getId());
							DashboardQuoteBean quoteBean = new DashboardQuoteBean();
							quoteBean.setCreatedDate(orderToLe.getOrder().getCreatedTime());
							quoteBean.setOrderId(orderToLe.getOrder().getId());
							quoteBean.setQuoteStage(orderToLe.getStage());
							daEntityBean.setQuoteBean(quoteBean);
							customerbean.getLegalEntityBeans().add(daEntityBean);
							DashBoardFamilyBean boardFamilyBean = new DashBoardFamilyBean();
							boardFamilyBean.setFamilyName(family.getMstProductFamily().getName());
							daEntityBean.getFamilyBeans().add(boardFamilyBean);
							for (OrderProductSolution solution : family.getOrderProductSolutions()) {

								List<OrderNplLink> links = orderNplLinkRepository
										.findByProductSolutionId(solution.getId());

								if (links != null && !links.isEmpty()) {
									Long siteCount = links.stream().count();
									Long provistionCount = links.stream().filter(link -> (link.getStage() != null
											&& link.getStage().equals(LinkStagingConstants.PROVISION_LINK.getStage())))
											.count();
									boardFamilyBean.setTotalCount(siteCount);
									boardFamilyBean.setProvisionedSiteCount(provistionCount);
								}

							}

						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		countMap.put("totalOrderCount", count);
		countMap.put("activeOrderCount", activeOrderCount);
		return countMap;

	}

	/**
	 * groupBasedOnCustomer - to group customers based on details
	 * 
	 * @param customerMap
	 * @param customerDetails
	 * @throws TclCommonException
	 */
	public void groupBasedOnCustomer(Map<Integer, List<CustomerDetail>> customerMap,
			List<CustomerDetail> customerDetails) throws TclCommonException {

		if (Objects.isNull(customerMap) || Objects.isNull(customerDetails)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

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
	 * getLinkDetails - to get details of a particular NPL link
	 * 
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public NplLinkBean getLinkDetails(Integer linkId) throws TclCommonException {
		List<OrderNplSiteBean> nplSiteBeans = new ArrayList<>();
		NplLinkBean nplLinkBean = null;
		try {
			if (Objects.isNull(linkId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			OrderNplLink orderNplLinkEntity = orderNplLinkRepository.findByIdAndStatus(linkId, (byte) 1);
			if (orderNplLinkEntity == null) {
				throw new TclCommonException(ExceptionConstants.NPL_LINK_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			nplLinkBean = new NplLinkBean(orderNplLinkEntity);
			if (nplLinkBean.getSiteA() != null && nplLinkBean.getSiteB() != null) {
				nplSiteBeans.add(getSiteDetails(nplLinkBean.getSiteA()));
				nplSiteBeans.add(getSiteDetails(nplLinkBean.getSiteB()));
			}
			nplLinkBean.setOrderSites(nplSiteBeans);
			nplLinkBean.setQuoteSla(constructOrderLinkSla(orderNplLinkEntity.getOrderNplLinkSlas()));
			Optional<OrderProductSolution> orderProductSolution = orderProductSolutionRepository
					.findById(orderNplLinkEntity.getProductSolutionId());
			if (orderProductSolution.isPresent()) {
				List<OrderProductComponentBean> orderProductComponentBeans = constructOrderProductComponent(linkId,
						CommonConstants.LINK,
						orderProductSolution.get().getOrderToLeProductFamily().getMstProductFamily().getName());
				orderProductComponentBeans.addAll(constructOrderProductComponent(orderNplLinkEntity.getSiteAId(),
						CommonConstants.SITEA,
						orderProductSolution.get().getOrderToLeProductFamily().getMstProductFamily().getName()));
				orderProductComponentBeans.addAll(constructOrderProductComponent(orderNplLinkEntity.getSiteBId(),
						CommonConstants.SITEB,
						orderProductSolution.get().getOrderToLeProductFamily().getMstProductFamily().getName()));
				nplLinkBean.setOrderProductComponentBeans(orderProductComponentBeans);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return nplLinkBean;
	}

	/**
	 * getSiteDetails - get details of a site
	 * 
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	public OrderNplSiteBean getSiteDetails(Integer siteId) throws TclCommonException {
		OrderNplSiteBean nplSiteBean = null;

		try {
			if (Objects.isNull(siteId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			OrderIllSite orderNplSiteEntity = orderNplSitesRepository.findByIdAndStatus(siteId, (byte) 1);
			if (orderNplSiteEntity == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			nplSiteBean = new OrderNplSiteBean(orderNplSiteEntity);
			if (orderNplSiteEntity.getMstOrderSiteStage() != null) {
				nplSiteBean.setCurrentStage(orderNplSiteEntity.getMstOrderSiteStage().getName());
			}
			if (orderNplSiteEntity.getMstOrderSiteStatus() != null) {
				nplSiteBean.setCurrentStatus(orderNplSiteEntity.getMstOrderSiteStatus().getName());
			}

			Optional<OrderProductSolution> orderProductSolution = orderProductSolutionRepository
					.findById(orderNplSiteEntity.getOrderProductSolution().getId());
			if (orderProductSolution.isPresent()) {
				Optional<OrderToLeProductFamily> orderToLeProductFamily = orderToLeProductFamilyRepository
						.findById(orderProductSolution.get().getOrderToLeProductFamily().getId());
				if (orderToLeProductFamily.isPresent()) {
					Optional<OrderToLe> orderToLe = orderToLeRepository
							.findById(orderToLeProductFamily.get().getOrderToLe().getId());
					if (orderToLe.isPresent()) {
						nplSiteBean.setErfCusCustomerLeId(orderToLe.get().getErfCusCustomerLegalEntityId());
					}
				}
			}
			
			//added for sitewise billing gst no
			List<QuoteIllSite> site=illSiteRepository.findBySiteCodeAndStatus(orderNplSiteEntity.getSiteCode(), (byte)1);
			if (!site.isEmpty()) {
				QuoteSiteBillingDetails billinginfo = quoteSiteBillingInfoRepository.findByQuoteIllSite(site.get(0));
				if (billinginfo != null) {
					LOGGER.info("GST no:::"+billinginfo.getGstNo()+"site wise billingid:::"+billinginfo.getId()+"siteid::"+site.get(0).getId());
					nplSiteBean.setSiteBillingGstNo(billinginfo.getGstNo());
				}
				
			}
			
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return nplSiteBean;

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<OrderProductComponentBean> getSortedComponents(List<OrderProductComponentBean> orderComponentBeans) {
		if (orderComponentBeans != null) {
			orderComponentBeans.sort(Comparator.comparingInt(OrderProductComponentBean::getId));

		}

		return orderComponentBeans;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<OrderNplSiteBean> getSortedNplSiteDtos(List<OrderNplSiteBean> nplSiteBeans) {
		if (nplSiteBeans != null) {
			nplSiteBeans.sort(Comparator.comparingInt(OrderNplSiteBean::getId));

		}

		return nplSiteBeans;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<OrderProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<OrderProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(OrderProductComponentsAttributeValueBean::getId));

		}

		return attributeBeans;
	}

	private List<OrderProductSolutionBean> getSortedSolution(List<OrderProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(OrderProductSolutionBean::getId));
		}

		return solutionBeans;

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
			if (Objects.isNull(orderRefUuid)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			String name = Utils.getSource();
			OrderConfirmationAudit orderConfirmationAudit = new OrderConfirmationAudit();
			orderConfirmationAudit.setName(name);
			orderConfirmationAudit.setPublicIp(publicIp);
			orderConfirmationAudit.setOrderRefUuid(orderRefUuid);
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * getLeAttributes - to get legal attributes
	 * 
	 * @param quoteTole
	 * @param attr
	 * @return
	 * @throws TclCommonException
	 */
	public String getLeAttributes(QuoteToLe quoteTole, String attr) throws TclCommonException {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;

		if (Objects.isNull(quoteTole) || Objects.isNull(attr)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

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
	 * constructOrderSiteSla - construct SLA for order link
	 * 
	 * @param illSite
	 * @param orderSite
	 * @throws TclCommonException
	 */
	public Set<OrderNplLinkSla> constructOrderLinkSla(Integer quoteLinkId, OrderNplLink orderLink)
			throws TclCommonException {
		Set<OrderNplLinkSla> orderNplLinkSlas = new HashSet<>();

		if (Objects.isNull(quoteLinkId) || Objects.isNull(orderLink)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

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
	 * @param orderSite - construct component for order npl link
	 * @link http://www.tatacommunications.com/
	 * @param id,version
	 * @throws TclCommonException
	 */
	public List<OrderProductComponent> constructOrderProductComponent(Integer quoteNplLinkId, OrderNplLink orderLink)
			throws TclCommonException {

		if (Objects.isNull(quoteNplLinkId) || Objects.isNull(orderLink)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

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
	 * constructOrderComponentPrice used to constrcut order Componenet price
	 * 
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
	 * 
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
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValue orderAttributeValue = new OrderProductComponentsAttributeValue();
				orderAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
				orderAttributeValue.setDisplayValue(attributeValue.getDisplayValue());
				orderAttributeValue.setProductAttributeMaster(attributeValue.getProductAttributeMaster());
				orderAttributeValue.setOrderProductComponent(orderProductComponent);
				orderProductComponentsAttributeValueRepository.save(orderAttributeValue);
				constructOrderAttributePriceDto(attributeValue, orderAttributeValue);
				orderProductComponentsAttributeValues.add(orderAttributeValue);
			}
		}

		return orderProductComponentsAttributeValues;
	}

	/**
	 * 
	 * @param orderAttributeValue - constructs attribute prices
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
				orderPriceRepository.save(orderPrice);
			}

		}
		return orderPrice;

	}

	/**
	 * 
	 * getUserId-This method get the user details if present or persist the user and
	 * get the entity
	 * 
	 * @param userData
	 * @return User
	 */
	public User getUserId(String username) {
		User user = null;
		if (username != null && !username.isEmpty()) {
			user = userRepository.findByUsernameAndStatus(username, 1);
		}
		return user;
	}

	/**
	 * validateRequest - validates the UpdateRequest object
	 * 
	 * @param request
	 */
	public void validateRequest(UpdateRequest request) throws TclCommonException {
		if (request.getComponentDetails() == null || request.getComponentDetails().isEmpty()) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}

	}

	/**
	 * updateLegalEntityProperties - updates the legal entity properties
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
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
			Optional<OrderToLe> optionalOrderToLe = orderToLeRepository.findById(request.getOrderToLeId());
			if (!optionalOrderToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			MstOmsAttribute omsAttribute = getMstAttributeMaster(request, user);
			constructOrderToLeAttribute(request, omsAttribute, optionalOrderToLe.get());

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
	}

	/**
	 * Updates the legal entity properties
	 * 
	 * @param request
	 * @param orderToLeId
	 * @throws TclCommonException
	 */
	public void updateLegalEntityPropertiesIsv(List<UpdateRequest> request, Integer orderToLeId)
			throws TclCommonException {

		try {
			request.stream().forEach(req -> {
				try {
					validateUpdateRequest(req);
					User user = getUserId(Utils.getSource());
					if (user == null) {
						throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR,
								ResponseResource.R_CODE_ERROR);

					}
					Optional<OrderToLe> optionalOrderToLe = orderToLeRepository.findById(orderToLeId);
					if (!optionalOrderToLe.isPresent()) {
						throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);

					}
					MstOmsAttribute omsAttribute = getMstAttributeMaster(req, user);
					constructOrderToLeAttribute(req, omsAttribute, optionalOrderToLe.get());
				} catch (Exception e) {
					throw new TclCommonRuntimeException(e);
				}
			});
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * validateUpdateRequest - validates the UpdateRequest object
	 * 
	 * @param request
	 */
	public void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		}

	}

	/**
	 * constructQuoteLeAttribute - constructs legal attributes for order
	 * 
	 * @param request
	 * @param omsAttribute
	 * @param orderToLe
	 * @throws TclCommonException
	 */
	private OrdersLeAttributeValue constructOrderToLeAttribute(UpdateRequest request, MstOmsAttribute omsAttribute,
			OrderToLe orderToLe) throws TclCommonException {

		OrdersLeAttributeValue orderLeAttributeValue = null;
		Set<OrdersLeAttributeValue> leAttrVals = ordersLeAttributeValueRepository
				.findByMstOmsAttributeAndOrderToLe(omsAttribute, orderToLe);

		if (leAttrVals.isEmpty()) {
			orderLeAttributeValue = new OrdersLeAttributeValue();
			orderLeAttributeValue.setMstOmsAttribute(omsAttribute);
			orderLeAttributeValue.setAttributeValue(request.getAttributeValue());
			orderLeAttributeValue.setDisplayValue(request.getAttributeName());
			orderLeAttributeValue.setOrderToLe(orderToLe);
		}

		else if (leAttrVals.size() == 1) {
			List<OrdersLeAttributeValue> leAttrList = new ArrayList<>();
			leAttrList.addAll(leAttrVals);
			orderLeAttributeValue = leAttrList.get(0);
			orderLeAttributeValue.setAttributeValue(request.getAttributeValue());
		}

		else {
			throw new TclCommonException(ExceptionConstants.MULTIPLE_ORDER_LE_ATTR_VALUES,
					ResponseResource.R_CODE_ERROR);
		}

		ordersLeAttributeValueRepository.save(orderLeAttributeValue);

		return orderLeAttributeValue;
	}

	/**
	 * getMstAttributeMaster - get attribute from attribute master
	 * 
	 * @param request
	 * @param user
	 * @return
	 * @throws TclCommonException
	 */
	public MstOmsAttribute getMstAttributeMaster(UpdateRequest request, User user) throws TclCommonException {
		validateUpdateRequest(request);
		if (Objects.isNull(user)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(request.getAttributeName(), (byte) 1);
		if (mstOmsAttributes != null && !mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}

		if (mstOmsAttribute == null || mstOmsAttributes.isEmpty()) {
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
	 * getAttributes - to get the attribute value for orderToLe
	 * 
	 * @param orderToLeId
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	public OrderToLeDto getAttributes(Integer orderToLeId, String attributeName) throws TclCommonException {
		OrderToLeDto orderToLeDto = null;
		try {
			if (Objects.isNull(orderToLeId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			Optional<OrderToLe> optOrderToLe = orderToLeRepository.findById(orderToLeId);
			if (optOrderToLe.isPresent()) {
				orderToLeDto = new OrderToLeDto(optOrderToLe.get());
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			if (!Objects.isNull(attributeName)) {
				MstOmsAttribute mstAttributes = null;
				List<MstOmsAttribute> mstAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(attributeName,
						(byte) 1);
				if (!mstAttributesList.isEmpty()) {
					mstAttributes = mstAttributesList.get(0);

				}
				if (mstAttributes != null) {
					Set<OrdersLeAttributeValue> ordersLeAttributeValues = ordersLeAttributeValueRepository
							.findByMstOmsAttributeAndOrderToLe(mstAttributes, optOrderToLe.get());
					optOrderToLe.get().setOrdersLeAttributeValues(ordersLeAttributeValues);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return orderToLeDto;

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ updateOrderToLeStatus to update the
	 *       order to le status
	 * @param orderToLeId, status
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */

	public NplQuoteDetail updateOrderToLeStatus(Integer orderToLeId, String status) throws TclCommonException {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		try {
			if (Objects.isNull(orderToLeId) || (StringUtils.isEmpty(status))) {
				throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
			if (!orderToLe.isPresent())
				throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			orderToLe.get().setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
			orderToLeRepository.save(orderToLe.get());

			Order orders = orderToLe.get().getOrder();
			orders.setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
			orderRepository.save(orders);

			if (status.equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())) {
				updateSiteStatusToOrderEnrichment(orders.getId());
				String accManagerEmail = getLeAttributes(orderToLe.get(), LeAttributesConstants.LE_EMAIL.toString());
				String custAccountName = getLeAttributes(orderToLe.get(),
						LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
				String orderRefId = orders.getOrderCode();
				processOrderMailNotification(orders, orderToLe.get());
				notificationService.provisioningOrderNewOrderNotification(accManagerEmail, orderRefId, custAccountName,
						appHost + adminRelativeUrl);
				LOGGER.info("MDC Filter token value in before Queue call processOrderEnrichment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				if (orders.getIsOrderToCashEnabled() != null
						&& orders.getIsOrderToCashEnabled().equals(CommonConstants.BACTIVE)) {
					//if (orderToLe.get().getOrderType() == null || orderToLe.get().getOrderType().equals("NEW")) {
						Map<String, Object> requestparam = new HashMap<>();
						requestparam.put("orderId", orders.getId());
						requestparam.put("productName", "NPL");
						requestparam.put("userName", Utils.getSource());
						mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
					//}
				}
				String lrDownloadUrl = appHost + CommonConstants.RIGHT_SLASH + "optimus-oms/api/lr/orders/npl/"
						+ orders.getId();
				orderLrService.initiateLrJob(orders.getOrderCode(), "NPL", lrDownloadUrl);
				
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;

	}

	/**
	 * Method to update the order ill sites as order enrichment on launch delivery
	 * ui call.
	 * 
	 * @param orderId
	 */
	private void updateSiteStatusToOrderEnrichment(Integer orderId) {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isPresent()) {
			optionalOrder.get().getOrderToLes().stream().forEach(orderToLe -> {
				orderToLe.getOrderToLeProductFamilies().stream().forEach(family -> {
					if (family.getMstProductFamily().getName()
							.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.NPL)) {
						family.getOrderProductSolutions().stream().forEach(orderProdSol -> {

							List<OrderNplLink> orderNplLink = orderNplLinkRepository
									.findByProductSolutionId(orderProdSol.getId());

							orderNplLink.stream().forEach(link -> {
								MstOrderLinkStatus mstOrderSiteStatus = mstOrderLinkStatusRepository
										.findByName(OrderDetailsExcelDownloadConstants.ORDER_ENRICHMENT);
								// site.setMstOrderSiteStatus(mstOrderSiteStatus);
								link.setMstOrderLinkStatus(mstOrderSiteStatus);
								orderNplLinkRepository.save(link);
							});
						});
					}
				});
			});
		}
	}

	/**
	 * processOrderMailNotification - initiates e-mail to intimate that order has
	 * been placed
	 * 
	 * @param order
	 * @param orderToLe
	 * @throws TclCommonException
	 */
	public void processOrderMailNotification(Order order, OrderToLe orderToLe) throws TclCommonException {
		if (Objects.isNull(order) || Objects.isNull(orderToLe)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
		String leName = getLeAttributes(orderToLe, LeAttributesConstants.LE_NAME);
		String leContact = getLeAttributes(orderToLe, LeAttributesConstants.LE_CONTACT);
		String cusEntityName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME);
		String spName = getLeAttributes(orderToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(userRepo.getFirstName(), cusEntityName,
				spName, leName, leContact, leMail, order.getOrderCode(), userRepo.getEmailId(),
				appHost + quoteDashBoardRelativeUrl, CommonConstants.NPL, orderToLe);
		notificationService.welcomeLetterNotification(mailNotificationBean);
	}

	private MailNotificationBean populateMailNotificationBean(String userName, String contactEntityName,
			String supplierEntityName, String accName, String accContact, String accountManagerEmail, String orderRefId,
			String customerEmail, String orderTrackingUrl, String productName, OrderToLe orderToLe) {
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
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	private MailNotificationBean populatePartnerClassification(OrderToLe orderToLe,
			MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
					String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny()
					.get().getLegalEntityName();

			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

			mailNotificationBean.setClassification(orderToLe.getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		} catch (Exception e) {
			LOGGER.warn("Error while reading end customer name :: {}", e.getMessage());
		}
		return mailNotificationBean;
	}

	/**
	 * getLeAttributes - method to get attibute value for a legal attribute
	 * 
	 * @param orderToLe
	 * @param attr
	 * @return
	 * @throws TclCommonException
	 */
	public String getLeAttributes(OrderToLe orderToLe, String attr) throws TclCommonException {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		if (Objects.isNull(attr) || Objects.isNull(orderToLe)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
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
	 * updateOrderLinkStatus - updates order link status
	 * 
	 * @param orderLinkId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public boolean updateOrderLinkStatus(Integer orderLinkId, OrderLinkRequest request) throws TclCommonException {
		boolean updateFlag = false;
		OrderLinkStatusAudit parentStatusAuditRecord = null;
		OrderLinkStageAudit parentStageAuditRecord = null;

		try {
			if (Objects.isNull(orderLinkId) || StringUtils.isEmpty(request.getMstOrderLinkStatusName())) {
				throw new TclCommonException(ExceptionConstants.ORDER_LINK_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}

			Optional<OrderNplLink> orderNplLink = orderNplLinkRepository.findById(orderLinkId);
			if (!orderNplLink.isPresent())
				throw new TclCommonException(ExceptionConstants.ORDER_LINK_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);

			MstOrderLinkStatus mstOrderLinkStatus = mstOrderLinkStatusRepository
					.findByName(request.getMstOrderLinkStatusName());
			if (Objects.isNull(mstOrderLinkStatus)) {
				throw new TclCommonException(ExceptionConstants.ORDER_LINK_STATUS_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}

			MstOrderLinkStage mstOrderLinkStage = null;
			if (!StringUtils.isEmpty(request.getMstOrderLinkStageName())) {
				mstOrderLinkStage = mstOrderLinkStageRepository.findByName(request.getMstOrderLinkStageName());
				if (Objects.isNull(mstOrderLinkStage)) {
					throw new TclCommonException(ExceptionConstants.ORDER_LINK_STAGE_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
				orderNplLink.get().setMstOrderLinkStage(mstOrderLinkStage);
			}

			orderNplLink.get().setMstOrderLinkStatus(mstOrderLinkStatus);
			orderNplLinkRepository.save(orderNplLink.get());

			List<OrderLinkStatusAudit> orderLinkStatusAuditList = orderLinkStatusAuditRepository
					.findByOrderNplLinkAndMstOrderLinkStatusAndIsActive(orderNplLink.get(), mstOrderLinkStatus,
							CommonConstants.BACTIVE);
			OrderLinkStatusAudit orderLinkStatusAudit = new OrderLinkStatusAudit();
			if (!orderLinkStatusAuditList.isEmpty()) {
				for (OrderLinkStatusAudit ordLinkStatusAudit : orderLinkStatusAuditList) {
					if (ordLinkStatusAudit.getEndTime() == null) {
						LocalDateTime localDateTime = LocalDateTime.now();
						ordLinkStatusAudit.setEndTime(Timestamp.valueOf(localDateTime));
						parentStatusAuditRecord = ordLinkStatusAudit;
						orderLinkStatusAuditRepository.save(ordLinkStatusAudit);
						break;
					}
				}

				if (parentStatusAuditRecord != null)
					orderLinkStatusAudit.setParentId(parentStatusAuditRecord.getId());
				orderLinkStatusAudit.setIsActive(CommonConstants.BACTIVE);
				orderLinkStatusAudit.setCreatedBy(Utils.getSource());
				LocalDateTime localDateTime = LocalDateTime.now();
				orderLinkStatusAudit.setCreatedTime(Timestamp.valueOf(localDateTime));
				orderLinkStatusAudit.setStartTime(Timestamp.valueOf(localDateTime));
				orderLinkStatusAudit.setEndTime(null);
				orderLinkStatusAudit.setMstOrderLinkStatus(mstOrderLinkStatus);
				orderLinkStatusAudit.setOrderNplLink(orderNplLink.get());
				orderLinkStatusAuditRepository.save(orderLinkStatusAudit);

			} else {

				orderLinkStatusAudit.setIsActive(CommonConstants.BACTIVE);
				orderLinkStatusAudit.setCreatedBy(Utils.getSource());
				LocalDateTime localDateTime = LocalDateTime.now();
				orderLinkStatusAudit.setCreatedTime(Timestamp.valueOf(localDateTime));
				orderLinkStatusAudit.setParentId(0);
				orderLinkStatusAudit.setStartTime(Timestamp.valueOf(localDateTime));
				orderLinkStatusAudit.setEndTime(null);
				orderLinkStatusAudit.setMstOrderLinkStatus(mstOrderLinkStatus);
				orderLinkStatusAudit.setOrderNplLink(orderNplLink.get());
				orderLinkStatusAuditRepository.save(orderLinkStatusAudit);

			}

			if (mstOrderLinkStage != null) {
				List<OrderLinkStageAudit> orderLinkStageAuditList = orderLinkStageAuditRepository
						.findByMstOrderLinkStageAndOrderLinkStatusAuditAndIsActive(mstOrderLinkStage,
								parentStatusAuditRecord, CommonConstants.BACTIVE);
				OrderLinkStageAudit orderLinkStageAudit = new OrderLinkStageAudit();
				if (!orderLinkStageAuditList.isEmpty()) {
					for (OrderLinkStageAudit ordLinkStageAudit : orderLinkStageAuditList) {
						if (ordLinkStageAudit.getEndTime() == null) {
							LocalDateTime localDateTime = LocalDateTime.now();
							ordLinkStageAudit.setEndTime(Timestamp.valueOf(localDateTime));
							parentStageAuditRecord = ordLinkStageAudit;
							orderLinkStageAuditRepository.save(ordLinkStageAudit);
							break;
						}
					}
					if (parentStageAuditRecord != null) {
						orderLinkStageAudit.setParentId(parentStageAuditRecord.getId());
					}
					orderLinkStageAudit.setCreatedBy(Utils.getSource());
					LocalDateTime localDateTime = LocalDateTime.now();
					orderLinkStageAudit.setCreatedTime(Timestamp.valueOf(localDateTime));
					orderLinkStageAudit.setEndTime(null);
					orderLinkStageAudit.setIsActive(CommonConstants.BACTIVE);
					orderLinkStageAudit.setMstOrderLinkStage(mstOrderLinkStage);
					orderLinkStageAudit.setOrderLinkStatusAudit(orderLinkStatusAudit);
					orderLinkStageAudit.setStartTime(Timestamp.valueOf(localDateTime));
					orderLinkStageAuditRepository.save(orderLinkStageAudit);
				} else {

					orderLinkStageAudit.setIsActive(CommonConstants.BACTIVE);
					orderLinkStageAudit.setCreatedBy(Utils.getSource());
					LocalDateTime localDateTime = LocalDateTime.now();
					orderLinkStageAudit.setCreatedTime(Timestamp.valueOf(localDateTime));
					orderLinkStageAudit.setParentId(0);
					orderLinkStageAudit.setStartTime(Timestamp.valueOf(localDateTime));
					orderLinkStageAudit.setEndTime(null);
					orderLinkStageAudit.setMstOrderLinkStage(mstOrderLinkStage);
					orderLinkStageAudit.setOrderLinkStatusAudit(orderLinkStatusAudit);
					orderLinkStageAuditRepository.save(orderLinkStageAudit);

				}
			}

			Optional<OrderProductSolution> prodSol = orderProductSolutionRepository
					.findById(orderNplLink.get().getProductSolutionId());

			if (!prodSol.isPresent())
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			if (request.getMstOrderLinkStatusName() != null
					&& request.getMstOrderLinkStatusName().equals(OrderConstants.START_OF_SERVICE.toString())
					&& checkWhetherAllLinkStatusAsStartOfService(prodSol.get())) {

				OrderToLe orderToLe = prodSol.get().getOrderToLeProductFamily().getOrderToLe();
				Integer userId = orderToLe.getOrder().getCreatedBy();
				Order orders = orderToLe.getOrder();

				orderToLe.setStage(OrderStagingConstants.ORDER_DELIVERED.toString());
				orderToLeRepository.save(orderToLe);
				orders.setStage(OrderStagingConstants.ORDER_DELIVERED.toString());
				orderRepository.save(orders);
				Optional<User> userRepo = userRepository.findById(userId);
				if (userRepo.isPresent()) {
					LOGGER.info("Emailing manual notification to customer {} for user Id {}",
							userRepo.get().getEmailId(), userId);
					String accManagerEmail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL.toString());
					String projectManagerEmail = null;
					String customerEmail = userRepo.get().getEmailId();
					String orderRefId = orders.getOrderCode();
					String effectiveDeliveryDate = new Date().toString();
					MailNotificationBean mailNotificationBean = populateMailNotificationBean(accManagerEmail,
							customerEmail, projectManagerEmail, orderRefId, orderRefId, effectiveDeliveryDate,
							CommonConstants.NPL, orderToLe);
					notificationService.orderDeliveryCompleteNotification(mailNotificationBean);
				}
			}

			updateFlag = true;

		} catch (Exception e) {
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return updateFlag;

	}

	private MailNotificationBean populateMailNotificationBean(String accountManagerEmail, String customerEmail,
			String projectManagerEmail, String orderId, String subOrderId, String effectiveDeliveryDate,
			String productName, OrderToLe orderToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setProjectManagerEmail(projectManagerEmail);
		mailNotificationBean.setOrderId(orderId);
		mailNotificationBean.setSubOrderId(subOrderId);
		mailNotificationBean.setEffectiveDeliveryDate(effectiveDeliveryDate);
		mailNotificationBean.setProductName(productName);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * checkWhetherAllLinkStatusAsStartOfService - validates all the links are in
	 * 'Start of service' status
	 * 
	 * @param sol
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean checkWhetherAllLinkStatusAsStartOfService(OrderProductSolution sol) throws TclCommonException {
		Boolean response = true;
		try {
			if (Objects.isNull(sol)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			List<OrderProductSolution> orderProductSolutions = orderProductSolutionRepository
					.findByOrderToLeProductFamily(sol.getOrderToLeProductFamily());
			for (OrderProductSolution orderProductSolution : orderProductSolutions) {
				List<OrderNplLink> links = orderNplLinkRepository.findByProductSolutionId(orderProductSolution.getId());
				if (links != null && !links.isEmpty()) {
					for (OrderNplLink link : links) {
						if (!link.getMstOrderLinkStatus().getName()
								.equals(OrderConstants.START_OF_SERVICE.toString())) {
							return false;
						}
					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ getAllOrdersByProductName get all
	 *       orders by product name
	 * @param productName
	 * @return List<DashboardOrdersBean>
	 * @throws TclCommonException
	 */

	public List<DashboardOrdersBean> getAllOrdersByProductName(String productName) throws TclCommonException {
		List<DashboardOrdersBean> dashBoardOrdersBeanList = new ArrayList<>();
		try {
			if (Objects.isNull(productName))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			List<OrderToLeProductFamily> orderToLeProductFamilyList = orderToLeProductFamilyRepository
					.findByMstProductFamilyOrderByIdAsc(mstProductFamily);
			if (!orderToLeProductFamilyList.isEmpty() && orderToLeProductFamilyList.size() > 0) {
				orderToLeProductFamilyList.stream().forEach(orderToLeProductFamily -> {
					DashboardOrdersBean dashBoardOrderBean = new DashboardOrdersBean();
					Optional<OrderToLe> orderTole = orderToLeRepository
							.findById(orderToLeProductFamily.getOrderToLe().getId());
					if (orderTole.isPresent()) {
						NplOrdersBean orderBean = new NplOrdersBean();
						Order order = orderRepository.findByIdAndStatus(orderTole.get().getOrder().getId(), (byte) 1);
						orderBean.setId(order.getId());
						orderBean.setOrderCode(order.getOrderCode());
						dashBoardOrderBean.setOrders(orderBean);

						List<OrderProductSolution> orderProductSolution = orderProductSolutionRepository
								.findByOrderToLeProductFamily(orderToLeProductFamily);
						List<OrderNplSiteBean> OrdeNplSiteBeanList = new ArrayList<>();
						if (!orderProductSolution.isEmpty() && orderProductSolution.size() > 0) {
							orderProductSolution.stream().forEach(orderPrdtSol -> {

								List<OrderIllSite> orderNplSite = orderNplSitesRepository
										.findByOrderProductSolutionAndStatus(orderPrdtSol, (byte) 1);
								if (!orderNplSite.isEmpty() && orderNplSite.size() > 0) {
									orderNplSite.stream().forEach(ordIllSite -> {

										OrderNplSiteBean OrdeNplSiteBean = new OrderNplSiteBean();
										OrdeNplSiteBean.setId(ordIllSite.getId());
										OrdeNplSiteBean.setErfLocSiteBLocationId(ordIllSite.getErfLocSitebLocationId());
										OrdeNplSiteBean.setRequestorDate(ordIllSite.getRequestorDate());
										OrdeNplSiteBeanList.add(OrdeNplSiteBean);
									});
								}

							});
						}
						dashBoardOrderBean.setOrderIllSite(OrdeNplSiteBeanList);

						List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository
								.findByNameAndIsActive(AttributeConstants.EXPECTED_DELIVERY_DATE.toString(), (byte) 1);
						Set<OrdersLeAttributeValue> ordersLeAttributeValue = orderToLeAttributeValueRespository
								.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute.get(0), orderTole.get());
						List<LegalAttributeBean> legalAttributeBeanList = new ArrayList<>();
						ordersLeAttributeValue.stream().forEach(legalAttValue -> {
							LegalAttributeBean legalAttributeBean = new LegalAttributeBean();
							legalAttributeBean.setAttributeValue(legalAttValue.getAttributeValue());
							legalAttributeBean.setDisplayValue(legalAttValue.getDisplayValue());
							legalAttributeBean.setId(legalAttValue.getId());
							legalAttributeBeanList.add(legalAttributeBean);
						});

						dashBoardOrderBean.setLegalAttributes(legalAttributeBeanList);

					}
					dashBoardOrdersBeanList.add(dashBoardOrderBean);
				});
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return dashBoardOrdersBeanList;

	}

	/**
	 * updateSiteProperties - updates site properties
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public OrderNplSiteBean updateOrderSiteProperties(UpdateRequest request, String type) throws TclCommonException {
		OrderNplSiteBean OrdeNplSiteBean = new OrderNplSiteBean();
		try {
			validateUpdateRequest(request);
			if (Objects.isNull(type) || type.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderIllSite> orderNplSite = orderNplSitesRepository.findById(request.getSiteId());
			if (!orderNplSite.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			User user = getUserId(Utils.getSource());
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
					(byte) 1);

			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			saveNplsiteProperties(orderNplSite.get(), request, user, mstProductFamily, type);
			
			//added for multisitebilling info gst updation
			LOGGER.info("ORDER TO LE ID ::::"+request.getOrderToLeId());
			if (request.getAttributeDetails() != null && request.getOrderToLeId() != null) {
				Optional<OrderToLe> ordertoLe = orderToLeRepository.findById(request.getOrderToLeId());
				if (ordertoLe.isPresent()) {
					if (ordertoLe.get().getSiteLevelBilling() != null) {
						if (ordertoLe.get().getSiteLevelBilling().equalsIgnoreCase("1")) {
							for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
								if (attributeDetail.getName().equalsIgnoreCase(AttributeConstants.GST_NUMBER.toString())) {
									updateGstNumber(attributeDetail.getValue(), orderNplSite.get().getSiteCode(),orderNplSite.get());
								}
							}
						}
					}
				}
			}
		}

		catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return OrdeNplSiteBean;

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ saveNplsiteProperties - updates NPL
	 *       site properties
	 * @param quoteIllSite
	 * @param localITContactId
	 * @param mstProductFamily
	 * @throws TclCommonException
	 */
	private void saveNplsiteProperties(OrderIllSite orderNplSite, UpdateRequest request, User user,
			MstProductFamily mstProductFamily, String type) throws TclCommonException {
		MstProductComponent mstProductComponent = getMstProperties(user);
		constructNplSitePropeties(mstProductComponent, orderNplSite, user.getUsername(), request, mstProductFamily,
				type);

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ getMstProperties used to get Mst
	 *       Properties
	 * @param id
	 * @param localITContactId
	 * @throws TclCommonException
	 */
	public MstProductComponent getMstProperties(User user) throws TclCommonException {
		if (Objects.isNull(user)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
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
	 * 
	 * @link http://www.tatacommunications.com/ constructIllSitePropeties - creates/
	 *       updates site properties
	 * 
	 * @param mstProductComponent
	 * @param quoteIllSite
	 * @param username
	 * @param localITContactId
	 * @param mstProductFamily
	 * @throws TclCommonException
	 */
	private void constructNplSitePropeties(MstProductComponent mstProductComponent, OrderIllSite orderNplSite,
			String username, UpdateRequest request, MstProductFamily mstProductFamily, String type)
			throws TclCommonException {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(orderNplSite.getId(), mstProductComponent, type);
		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
			updateIllSiteProperties(orderProductComponents, request, username);
		} else {
			createNplSiteAttribute(mstProductComponent, mstProductFamily, orderNplSite, request, username, type);
		}

	}

	/**
	 * createIllSiteAttribute - creates site attributes
	 * 
	 * @param mstProductComponent
	 * @param mstProductFamily
	 * @param orderNplSite
	 * @param request
	 * @param username
	 */
	private void createNplSiteAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
			OrderIllSite orderNplSite, UpdateRequest request, String username, String type) {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setMstProductComponent(mstProductComponent);
		orderProductComponent.setReferenceId(orderNplSite.getId());
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponent.setType(type);
		orderProductComponent.setReferenceName(QuoteConstants.NPL_SITES.toString());
		orderProductComponentRepository.save(orderProductComponent);

		if (request.getAttributeDetails() != null) {
			for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
				createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

			}

		}
	}

	/**
	 * updateIllSiteProperties - udpates site properties
	 * 
	 * @param orderProductComponents
	 * @param request
	 * @param username
	 * @throws TclCommonException
	 */
	public void updateIllSiteProperties(List<OrderProductComponent> orderProductComponents, UpdateRequest request,
			String username) throws TclCommonException {
		validateUpdateRequest(request);
		if (Objects.isNull(orderProductComponents) || Objects.isNull(username) || StringUtils.isEmpty(username)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		for (OrderProductComponent orderProductComponent : orderProductComponents) {

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

		}

	}

	/**
	 * createSitePropertiesAttribute - creates site properties attribute
	 * 
	 * @param orderProductComponent
	 * @param attributeDetail
	 * @param username
	 */
	private void createSitePropertiesAttribute(OrderProductComponent orderProductComponent,
			AttributeDetail attributeDetail, String username) {

		ProductAttributeMaster attributeMaster = getPropertiesMaster(username, attributeDetail);
		orderProductComponent.setOrderProductComponentsAttributeValues(
				createAttributes(attributeMaster, orderProductComponent, attributeDetail));

	}

	/**
	 * createAttributes - creates attributes for a component
	 * 
	 * @param attributeMaster
	 * @param orderProductComponent
	 * @param attributeDetail
	 */
	private Set<OrderProductComponentsAttributeValue> createAttributes(ProductAttributeMaster attributeMaster,
			OrderProductComponent orderProductComponent, AttributeDetail attributeDetail) {

		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();

		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
		orderProductComponentsAttributeValue.setProductAttributeMaster(attributeMaster);
		orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValues.add(orderProductComponentsAttributeValue);
		if (attributeMaster.getName().toLowerCase().contains("GSTNO".toLowerCase())) {
			orderProductComponentsAttributeValues.add(createGstAddress(attributeDetail, orderProductComponent));
		}
		return orderProductComponentsAttributeValues;

	}
	
	/**
	 * createGstAddress
	 * @param attributeDetail
	 * @param orderProductComponent
	 */
	private OrderProductComponentsAttributeValue createGstAddress(AttributeDetail attributeDetail,
			OrderProductComponent orderProductComponent) {
		String address = getGstAddress(attributeDetail.getValue());
		AdditionalServiceParams additionalServiceParam = new AdditionalServiceParams();
		additionalServiceParam.setValue(address);
		additionalServiceParam.setCreatedBy(Utils.getSource());
		additionalServiceParam.setCreatedTime(new Date());
		additionalServiceParam.setIsActive(CommonConstants.Y);
		additionalServiceParam.setAttribute("GST_ADDRESS");
		additionalServiceParamRepository.save(additionalServiceParam);
		List<ProductAttributeMaster> gstAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus("GST_ADDRESS", (byte) 1);
		if (gstAttributeMasters != null && !gstAttributeMasters.isEmpty()) {
			OrderProductComponentsAttributeValue gstComponentsAttributeValue = new OrderProductComponentsAttributeValue();
			gstComponentsAttributeValue.setAttributeValues(additionalServiceParam.getId()+"");
			gstComponentsAttributeValue.setDisplayValue(additionalServiceParam.getId()+"");
			gstComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
			gstComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
			gstComponentsAttributeValue.setProductAttributeMaster(gstAttributeMasters.get(0));
			orderProductComponentsAttributeValueRepository.save(gstComponentsAttributeValue);
			return gstComponentsAttributeValue;
		}
		return null;
	}
	
	/**
	 * saveGstAddress
	 */
	private String getGstAddress(String gstIn) {
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

	/**
	 * upateSitePropertiesAttribute - updates attributes under site properties
	 * 
	 * @param productAttributeMasters
	 * @param attributeDetail
	 * @param orderProductComponent
	 * @param username
	 * @throws TclCommonException
	 */
	public void upateSitePropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
			AttributeDetail attributeDetail, OrderProductComponent orderProductComponent) throws TclCommonException {

		if (Objects.isNull(orderProductComponent) || Objects.isNull(productAttributeMasters)
				|| Objects.isNull(attributeDetail)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
						productAttributeMasters.get(0));
		if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
			for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
				orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
				orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);

			}
		} else {

			orderProductComponent.setOrderProductComponentsAttributeValues(
					createAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

		}

	}

	/**
	 * getPropertiesMaster - retrieves an attribute from attribute master based on
	 * name
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
	 * getAllOrders - retrieves all orders
	 * 
	 * @return
	 */
	public List<NplOrdersBean> getAllOrders() throws TclCommonException {
		List<NplOrdersBean> ordersBeans = null;
		try {
			ordersBeans = new ArrayList<>();
			List<Order> orders = orderRepository.findAllByOrderByCreatedTimeDesc();
			for (Order order : orders) {
				NplOrdersBean ordersBean = constructOrder(order);
				ordersBeans.add(ordersBean);
			}

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBeans;
	}

	/**
	 * getOrderSummary - retrieves order summary for all orders
	 * 
	 * @return
	 */
	public List<OrderSummaryBean> getOrderSummary() throws TclCommonException {
		List<OrderSummaryBean> ordersBeans = null;
		try {
			ordersBeans = new ArrayList<>();
			List<Order> orders = orderRepository.findAllByOrderByCreatedTimeDesc();
			if (orders != null) {
				for (Order order : orders) {
					constructSummaryForSV(order, ordersBeans);

				}

			}
		}

		catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBeans;
	}

	/**
	 * constructSummaryForSV - constructs order summary for orders
	 * 
	 * @param order
	 * @param ordersBeans
	 */
	private void constructSummaryForSV(Order order, List<OrderSummaryBean> ordersBeans) {
		OrderSummaryBean bean = constructOrderSummaryBean(order);
		if (order.getOrderToLes() != null) {
			for (OrderToLe orderTLe : order.getOrderToLes()) {
				constructLeForSV(orderTLe, bean);
				for (OrderToLeProductFamily family : orderTLe.getOrderToLeProductFamilies()) {
					construcyFamilyForSV(family, bean);
				}

			}
		}
		ordersBeans.add(bean);
	}

	/**
	 * construcyFamilyForSV - constructs family for Summary view
	 * 
	 * @param family
	 * @param bean
	 */
	private void construcyFamilyForSV(OrderToLeProductFamily family, OrderSummaryBean bean) {
		OrderFamilySVBean orderFamilySVBean = new OrderFamilySVBean();
		orderFamilySVBean.setName(family.getMstProductFamily().getName());
		orderFamilySVBean.setFamilyId(family.getId());
		bean.getOrderFamily().add(orderFamilySVBean);
	}

	/**
	 * constructLeForSV - constructs legal entity for Summary view
	 * 
	 * @param orderTLe
	 * @param bean
	 * @return
	 */
	private OrderLeSVBean constructLeForSV(OrderToLe orderTLe, OrderSummaryBean bean) {
		OrderLeSVBean orderLeSVBean = new OrderLeSVBean();
		orderLeSVBean.setLegalEntityId(orderTLe.getId());
		orderLeSVBean.setStage(orderTLe.getStage());
		orderLeSVBean.setOpportunityId(orderTLe.getTpsSfdcCopfId());
		orderLeSVBean.setAccountCuid(
				extractLeAttributes(orderTLe, orderLeSVBean, LeAttributesConstants.ACCOUNT_CUID.toString()));
		orderLeSVBean
				.setPoNumber(extractLeAttributes(orderTLe, orderLeSVBean, LeAttributesConstants.PO_NUMBER.toString()));
		orderLeSVBean.setPoDate(extractLeAttributes(orderTLe, orderLeSVBean, LeAttributesConstants.PO_DATE.toString()));
		orderLeSVBean.setLegalEntityName(
				extractLeAttributes(orderTLe, orderLeSVBean, LeAttributesConstants.LEGAL_ENTITY_NAME.toString()));
		bean.getLegalEntity().add(orderLeSVBean);
		return orderLeSVBean;
	}

	/**
	 * constructOrderSummaryBean - constructs order summary bean for an order
	 * 
	 * @param order
	 * @return
	 */
	private OrderSummaryBean constructOrderSummaryBean(Order order) {
		OrderSummaryBean bean = new OrderSummaryBean();

		bean.setOrderId(order.getId());
		if (order.getCreatedBy() != null) {
			Optional<User> optionaluser = userRepository.findById(order.getCreatedBy());
			if (optionaluser.isPresent()) {
				bean.setCreatedBy(optionaluser.get().getUsername());
			}
		}
		if (order.getQuoteCreatedBy() != null) {
			Optional<User> optionaluser = userRepository.findById(order.getQuoteCreatedBy());
			if (optionaluser.isPresent()) {
				bean.setQuoteCreatedBy(optionaluser.get().getUserType());
			}
		}
		bean.setOrderStage(order.getStage());
		bean.setCreatedTime(order.getCreatedTime());
		bean.setOrderCode(order.getOrderCode());
		return bean;
	}

	/**
	 * updateSFDCStage - updates stage in SFDC
	 * 
	 * @throws TclCommonException
	 */
	public void updateSfdcStage(Integer orderToLeId, String stage) throws TclCommonException {

		if (Objects.isNull(orderToLeId) || Objects.isNull(stage)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		if (StringUtils.isNotBlank(stage) && (SFDCConstants.CLOSED_WON_COF_RECI.equals(stage)
				|| SFDCConstants.COF_WON_PROCESS_STAGE.equals(stage) || SFDCConstants.COF_WON_STAGE.equals(stage)
				|| SFDCConstants.VERBAL_AGREEMENT_STAGE.equals(stage)
				|| SFDCConstants.IDENTIFIED_OPTY_STAGE.equals(stage))) {
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
			if (orderToLe.isPresent()) {
				String sfdcId = orderToLe.get().getTpsSfdcCopfId();
				omsSfdcService.processUpdateOrderOpportunity(new Date(), sfdcId, stage, orderToLe.get());
			}

		}
	}

	/**
	 * getSiteProperties - gets site properties for a site
	 * 
	 * @param attributeName
	 * 
	 * @param request
	 * @return
	 */
	public List<OrderProductComponentBean> getSiteProperties(Integer siteId, String attributeName, String type)
			throws TclCommonException {

		// attributeName is optional parameter
		if (Objects.isNull(siteId) || Objects.isNull(type)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		MstProductComponent mstProductComponent = null;
		Optional<OrderIllSite> optionalIllSite = orderNplSitesRepository.findById(siteId);
		if (!optionalIllSite.isPresent()) {
			throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);
		}

		OrderIllSite orderNplSite = optionalIllSite.get();

		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		if (mstProductComponents != null) {
			mstProductComponent = mstProductComponents.get(0);
		}

		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(orderNplSite.getId(), mstProductComponent, type);

		return constructOrderProductComponent(orderProductComponents, attributeName);

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ constructOrderProductComponent
	 * @param id,version
	 */
	private List<OrderProductComponentBean> constructOrderProductComponent(
			List<OrderProductComponent> orderProductComponents, String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> orderProductComponentDtos = new ArrayList<>();
		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = null;

		if (orderProductComponents != null) {

			for (OrderProductComponent quoteProductComponent : orderProductComponents) {
				OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
				orderProductComponentBean.setId(quoteProductComponent.getId());
				orderProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					orderProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					orderProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				orderProductComponentBean.setType(quoteProductComponent.getType());
				if (attributeName == null) {
					orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponent(quoteProductComponent);
				} else {
					orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponentAndProductAttributeMaster_Name(quoteProductComponent,
									attributeName);
				}
				List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(orderProductComponentsAttributeValues));
				orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
				orderProductComponentDtos.add(orderProductComponentBean);
			}

		}
		return orderProductComponentDtos;

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com
	 * @param orderProductComponentsAttributeValues
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (orderProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValueBean qtAttributeValue = null;
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> additionalServiceParamEntity = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeValue.getAttributeValues()));
					if (additionalServiceParamEntity.isPresent()) {
						qtAttributeValue = new OrderProductComponentsAttributeValueBean(attributeValue,
								additionalServiceParamEntity.get().getValue());
					}
				} else {
					qtAttributeValue = new OrderProductComponentsAttributeValueBean(
							attributeValue);
				}
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}

				orderProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return orderProductComponentsAttributeValueBean;
	}

	/**
	 * getOrderLinkAuditTrail
	 * 
	 * @param orderId
	 * @param orderToLeId
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	/*
	 * public List<AuditBean> getOrderLinkAuditTrail(Integer orderId, Integer
	 * orderToLeId, Integer linkId) throws TclCommonException { List<AuditBean>
	 * auditBeanList = new ArrayList<>();
	 * 
	 * try {
	 * 
	 * Optional<OrderNplLink> orderNplLink =
	 * orderNplLinkRepository.findById(linkId); if (Objects.isNull(orderNplLink)) {
	 * throw new
	 * TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
	 * ResponseResource.R_CODE_ERROR); }
	 * auditBeanList.add(getOrderSiteAuditTrail(orderId, orderToLeId,
	 * orderNplLink.get().getSiteAId()));
	 * auditBeanList.add(getOrderSiteAuditTrail(orderId, orderToLeId,
	 * orderNplLink.get().getSiteBId()));
	 * 
	 * } catch (Exception e) { throw new
	 * TclCommonException(ExceptionConstants.COMMON_ERROR, e,
	 * ResponseResource.R_CODE_ERROR); } return auditBeanList; }
	 * 
	 */

	/**
	 * updateLinkDetails - updates link details
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public String updateLinkDetails(UpdateRequest request) throws TclCommonException {
		String result = "FAILURE";
		try {
			// getSiteId() is used to get the link id
			validateUpdateRequest(request);
			if (request.getSiteId() != null && request.getSiteId() > 0) {
				OrderNplLink orderNplLinkEntity = orderNplLinkRepository.findByIdAndStatus(request.getSiteId(),
						(byte) 1);

				if (orderNplLinkEntity != null) {

					if (request.getRequestorDate() != null) {
						orderNplLinkEntity.setRequestorDate(request.getRequestorDate());
					}
					if (request.getServiceId() != null && !request.getServiceId().isEmpty()) {
						orderNplLinkEntity.setServiceId(request.getServiceId());
					}
					orderNplLinkRepository.save(orderNplLinkEntity);
					result = "SUCCESS";
				}
			}

		} catch (Exception e) {
			result = "FAILURE";
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return result;
	}

	/**
	 * @author Dinahar Vivekanandan getFeasiblityAndPricingDetails - retrieves
	 *         feasibility and pricing details
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public NplPricingFeasibilityBean getFeasiblityAndPricingDetails(Integer linkId) throws TclCommonException {
		NplPricingFeasibilityBean link = new NplPricingFeasibilityBean();
		try {
			if (Objects.isNull(linkId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderNplLink> orderNplLink = orderNplLinkRepository.findById(linkId);
			if (orderNplLink.isPresent()) {
				List<OrderLinkFeasibility> feasiblityDetails = orderLinkFeasibilityRepository
						.findByOrderNplLink(orderNplLink.get());
				List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingType(orderNplLink.get().getLinkCode(), CommonConstants.LINK);
				link = constructOrderLinkPricingFeasibilityDetails(orderNplLink.get(), feasiblityDetails,
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
	 * @author Dinahar Vivekanandan constructPricingFeasibilityDetails - constructs
	 *         feasbility and pricing details as a dto object
	 * @param orderLink
	 * @param feasiblityDetails
	 * @param pricingDetails
	 * @return
	 * @throws TclCommonException
	 */
	public NplPricingFeasibilityBean constructOrderLinkPricingFeasibilityDetails(OrderNplLink orderLink,
			List<OrderLinkFeasibility> feasiblityDetails, List<PricingEngineResponse> pricingDetails)
			throws TclCommonException {
		if (Objects.isNull(orderLink) || Objects.isNull(feasiblityDetails) || Objects.isNull(pricingDetails)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		NplPricingFeasibilityBean link = new NplPricingFeasibilityBean();
		Optional<OrderIllSite> quoteIllOrderSiteA = orderIllSitesRepository.findById(orderLink.getSiteAId());
		Optional<OrderIllSite> quoteIllOrderSiteB = orderIllSitesRepository.findById(orderLink.getSiteBId());
		link.setLinkId(orderLink.getId());
		link.setLinkCode(orderLink.getLinkCode());
		link.setIsFeasible(orderLink.getFeasibility());
		link.setChargeableDistance(orderLink.getChargeableDistance());
		if (quoteIllOrderSiteA.isPresent()) {
			link.setIsTaxExemptedSiteA(quoteIllOrderSiteA.get().getIsTaxExempted());
		}
		if (quoteIllOrderSiteB.isPresent()) {
			link.setIsTaxExemptedSiteB(quoteIllOrderSiteB.get().getIsTaxExempted());
		}
		link.setFeasiblityDetails(constructOrderFeasiblityResponse(feasiblityDetails));
		link.setPricingDetails(constructPricingDetails(pricingDetails));
		return link;
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
	 * constructOrderFeasiblityResponse
	 * 
	 * @param feasiblityDetails
	 * @return
	 */
	private List<NplFeasiblityBean> constructOrderFeasiblityResponse(List<OrderLinkFeasibility> feasiblityDetails) {
		List<NplFeasiblityBean> feasiblityResponse = new ArrayList<>();
		feasiblityDetails.stream().forEach(feasiblity -> {
			NplFeasiblityBean feasiblityBean = new NplFeasiblityBean();
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
			feasiblityBean.setLinkId(feasiblity.getOrderNplLink().getId());
			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}

	/**
	 * returnExcel - method holding functionality to export order details as LR
	 * Excel
	 * 
	 * @param orderId
	 * @param response
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public void returnExcel(Integer orderId, HttpServletResponse response)
			throws FileNotFoundException, IOException, TclCommonException {

		if (Objects.isNull(orderId) || Objects.isNull(response)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);
		LOGGER.info("Order received is {}", order);

		QuoteToLe quoteToLe = order.getQuote().getQuoteToLes().stream().findFirst().get();
		String type = quoteToLe.getQuoteType();
		List<ExcelBean> listBook = getExcelList(orderId, quoteToLe, type);
//		List<ExcelBean> listBook = getExcelList(orderId);
		LOGGER.info("Order data created successfully for {}", orderId);
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		sheet.setColumnWidth(50000, 50000);
		int rowCount = 0;

		for (ExcelBean aBook : listBook) {
			Row row = sheet.createRow(rowCount);
			writeBook(aBook, row);
			rowCount++;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "OrderDetails-" + orderId + ".xlsx";
		response.reset();
		response.setContentType("application/ms-excel");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {

		}

		outByteStream.flush();
		outByteStream.close();
	}

	/**
	 * getExcelList - converts order into list of excel bean
	 * 
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	public List<ExcelBean> getExcelList(Integer orderId, QuoteToLe quoteToLe, String type) throws TclCommonException {

		if (Objects.isNull(orderId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Entering NplOrderService::getExcelList for excel download {}", orderId);
		List<ExcelBean> listBook = new ArrayList<>();
		MDMServiceInventoryBean[] serviceInventoryMDM = {null};
		Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);
		LOGGER.info("Order details are fetched {}", orderId);
		Iterator<?> iter = order.getOrderToLes().iterator();
		OrderToLe orderToLe = (OrderToLe) iter.next();
		List<OrderNplLink> orderNplLinkList = getLinks(orderToLe);
		Map<String, String> attributeValues = getAttributeValues(orderToLe);

		Map<String, String> siDetsPrimarySet = new HashMap<>();
		Map<String, String> siDetsSecondarySet = new HashMap<>();
		//CROSS connect attributes Added in LR Export
		Boolean Crossconnect=false;
		OrderToLeProductFamily orderToLeProductFamily=orderToLeProductFamilyRepository.findByOrderToLe_Id(orderToLe.getId());
		List<OrderProductSolution> orderProductSolution=orderProductSolutionRepository.findByOrderToLeProductFamily(orderToLeProductFamily);
		for (OrderProductSolution solution : orderProductSolution) {
			if(Objects.nonNull(solution.getMstProductOffering()) && Objects.nonNull(solution.getMstProductOffering().getProductName()) &&
					CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())){
				Crossconnect=true;

			}

		}
		if (type != null && type.equalsIgnoreCase("MACD")&&!Crossconnect){
			/*siDets.putAll(SiDets(order.getQuote().getId(), quoteToLe.getId(),
					quoteToLe.getErfServiceInventoryTpsServiceId(), quoteToLe, orderToLe));*/
			List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderNplLink_IdAndOrderToLe(orderNplLinkList.get(0).getId(),orderToLe);
			OrderIllSiteToService orderIllSiteToService=orderIllSiteToServices.stream().findFirst().get();
			getSiDets(siDetsPrimarySet,siDetsSecondarySet,orderIllSiteToService.getErfServiceInventoryTpsServiceId(),type,orderToLe);

		}

		if (type != null && type.equalsIgnoreCase("MACD"))
		{
			ExcelBean info = new ExcelBean(OrderDetailsExcelDownloadConstants.LR_SECTION,
					OrderDetailsExcelDownloadConstants.ATTRIBUTE_REQUIRED, OrderDetailsExcelDownloadConstants.REMARKS,
					"ACTION TYPE");
			info.setOrder(0);
			info.setSiteId(0);
			listBook.add(info);
		}
		else
		{
			ExcelBean info = new ExcelBean(OrderDetailsExcelDownloadConstants.LR_SECTION,
					OrderDetailsExcelDownloadConstants.ATTRIBUTE_REQUIRED, OrderDetailsExcelDownloadConstants.REMARKS);
			info.setOrder(0);
			info.setLinkId(0);
			listBook.add(info);
		}

		LOGGER.info("constructing order details {}", orderToLe.getId());

		createOrderDetails(listBook, attributeValues, orderToLe);


		if(Crossconnect) {
			LOGGER.info("constructing order attributes details crossconnect");
			List<OrderIllSite> orderIllSites=orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution.get(0), (byte) 1);

			ExcelBean serviveType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.SERVICE_TYPE, "NPL - BSO MMR Cross Connect");
			serviveType.setOrder(1);
			serviveType.setLinkId(0);
			listBook.add(serviveType);

			ExcelBean nplFlavour = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.NPLI_FLAVOUR, "BSO MMR Cross Connect");
			nplFlavour.setOrder(1);
			nplFlavour.setLinkId(0);
			listBook.add(nplFlavour);

			ExcelBean nplType= new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.NPLI_TYPE, "Normal");
			nplType.setOrder(1);
			nplType.setLinkId(0);
			listBook.add(nplType);

			ExcelBean componentType= new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.COMPONENT_TYPE, "Cross Connect");
			componentType.setOrder(1);
			componentType.setLinkId(0);
			listBook.add(componentType);

			String customerSegmentValue=CommonConstants.EMPTY;
			CustomerDetailsBean customerSegmentBean=processCustomerData(quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
			if(Objects.nonNull(customerSegmentBean) && !customerSegmentBean.getCustomerAttributes().isEmpty() &&
					customerSegmentBean.getCustomerAttributes().stream().
							filter(attributeValue->attributeValue.getName().
									equalsIgnoreCase("CUSTOMER TYPE")).findFirst().isPresent()) {
				customerSegmentValue=customerSegmentBean.getCustomerAttributes().stream().
						filter(attributeValue->attributeValue.getName().
								equalsIgnoreCase("CUSTOMER TYPE")).findFirst().get().getValue();
			}
				ExcelBean customerSegment = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT, customerSegmentValue);
				customerSegment.setOrder(1);
				customerSegment.setLinkId(0);
				listBook.add(customerSegment);


			if(Objects.nonNull(orderIllSites) && !orderIllSites.isEmpty()) {
				for(OrderIllSite orderIllSite:orderIllSites){
					if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
						List<OrderIllSiteToService> orderIllSiteToServiceList =  orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
						if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty())
							try {
								serviceInventoryMDM[0] = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null, orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId(), null, null);
							} catch (Exception e) {
								LOGGER.info("Exception when fetching MDM invntry details {}", e);
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
							}
							LOGGER.info("Service Inventory MDM Bean {}", serviceInventoryMDM[0].getServiceDetailBeans().get(0).toString());
					}
					Map<String, String> crossConnectAttributeList = getCrossconnectAttributeValues(orderIllSite);
					createCrossConnectOrderDetails(listBook, crossConnectAttributeList,orderToLe.getErfCusCustomerLegalEntityId());
					createCrossConnectBillingComponentPrice(listBook,orderIllSite,crossConnectAttributeList.get(CrossconnectConstants.CROSS_CONNECT_TYPE));
			/*=new ArrayList<OrderIllSite>();
			if(orderProductSolution.get(0)!=null){
			   orderIllSite=orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution.get(0), (byte) 1);
			}*/		if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				ExcelBean siteLocationAddressLine1A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
						OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, serviceInventoryMDM[0].getServiceDetailBeans().get(0).getSiteAddress());
				siteLocationAddressLine1A.setOrder(2);
				siteLocationAddressLine1A.setSiteAId(orderIllSite.getId());
				listBook.add(siteLocationAddressLine1A);
				
				ExcelBean siteLocationAddressLineZ = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
						OrderDetailsExcelDownloadConstants.SITE_Z_ADDRESS, serviceInventoryMDM[0].getServiceDetailBeans().get(0).getSiteAddress());
				siteLocationAddressLineZ.setOrder(2);
				siteLocationAddressLineZ.setSiteAId(orderIllSite.getId());
				listBook.add(siteLocationAddressLineZ);
				
					List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
					Double cancellationCharge = 0D;
					if(!orderIllSiteToServiceList.isEmpty()) {
					LOGGER.info("Extract cancellation charges");
					if(MACDConstants.ABSORBED.equalsIgnoreCase(orderIllSiteToServiceList.get(0).getAbsorbedOrPassedOn())) {
						cancellationCharge = 0D;
					} else {
						cancellationCharge = orderIllSite.getTcv();
					}
					ExcelBean cancellationCharges = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
							OrderDetailsExcelDownloadConstants.CANCELLATION_CHARGES, String.valueOf(cancellationCharge));
					cancellationCharges.setOrder(2);
					cancellationCharges.setSiteId(0);
					if (!listBook.contains(cancellationCharges)) {
						listBook.add(cancellationCharges);
					}
					
					
					LOGGER.info("Extract cancellation leadToRFS date");
					ExcelBean leadToRFSDate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
							OrderDetailsExcelDownloadConstants.LEAD_TO_RFS_DATE, String.valueOf(orderIllSiteToServiceList.get(0).getLeadToRFSDate()));
					leadToRFSDate.setOrder(2);
					leadToRFSDate.setSiteId(0);
					if (!listBook.contains(leadToRFSDate)) {
						listBook.add(leadToRFSDate);
					}
					
					

					
					LOGGER.info("Extract effective date of change");
					ExcelBean effectiveDateOfChange = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
							OrderDetailsExcelDownloadConstants.EFFECTIVE_DATE_OF_CHANGE, String.valueOf(orderIllSiteToServiceList.get(0).getEffectiveDateOfChange()));
					effectiveDateOfChange.setOrder(2);
					effectiveDateOfChange.setSiteId(0);
					if (!listBook.contains(effectiveDateOfChange)) {
						listBook.add(effectiveDateOfChange);
					}
					}
					
			} else {
					createSiteAEndLocationForExcel(orderIllSite, listBook);
					createSiteZEndLocationForExcel(orderIllSite, listBook);
			}
				}
			}
		}
		else {
		LOGGER.info("constructing order details Npl order");
		List<OrderLinkFeasibility> orderLinkFeasibilityList = null;
		OrderLinkFeasibility orderLinkFeasibility = null;
		Feasible feasible = null;
		NotFeasible notFeasible = null;
		String feasibilityModeA = "";
		String feasibilityModeB = "";
		for (OrderNplLink link : orderNplLinkList) {
			
			LOGGER.info("INSIDE FOR LOOP");
			Map<String, Map<String, String>> excelMap = new HashMap<>();
			
			if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				List<OrderIllSiteToService> orderIllSiteToServiceList =  orderIllSiteToServiceRepository.findByOrderNplLink_Id(link.getId());
				if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty())
					try {
						serviceInventoryMDM[0] = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null, orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId(), null, null);
					} catch (Exception e) {
						LOGGER.info("Exception when fetching MDM invntry details {}", e);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
					}
					LOGGER.info("Service Inventory MDM Bean {}", serviceInventoryMDM[0].getServiceDetailBeans().get(0).toString());
			}
			
			orderLinkFeasibilityList = orderLinkFeasibilityRepository.findByOrderNplLinkAndIsSelected(link, (byte) 1);

			if (orderLinkFeasibilityList != null && !orderLinkFeasibilityList.isEmpty()) {
				orderLinkFeasibility = orderLinkFeasibilityList.get(0);
				if (orderLinkFeasibility.getIsSelected() == 1) {
					if (orderLinkFeasibility.getRank() != null)
						feasible = (Feasible) Utils.convertJsonToObject(orderLinkFeasibility.getResponseJson(),
								Feasible.class);

					createLinkDetailsBasedOnFeasibility(orderLinkFeasibility, listBook, link, feasible, orderToLe);
				} else {
					notFeasible = (NotFeasible) Utils.convertJsonToObject(orderLinkFeasibility.getResponseJson(),
							NotFeasible.class);
					createLinkDetailsBasedOnFeasibility(orderLinkFeasibility, listBook, link, notFeasible, orderToLe);
				}

			}

			if (feasible != null || notFeasible != null) {
				feasibilityModeA = orderLinkFeasibility.getFeasibilityMode();
				feasibilityModeA = getAccessType(feasibilityModeA);
				feasibilityModeB = orderLinkFeasibility.getFeasibilityModeB();
				feasibilityModeB = getAccessType(feasibilityModeB);
			}

			OrderIllSite orderIllSiteA = orderIllSitesRepository.findByIdAndStatus(link.getSiteAId(), (byte) 1);
			OrderIllSite orderIllSiteB = orderIllSitesRepository.findByIdAndStatus(link.getSiteBId(), (byte) 1);
			createDefaultLinkDetails(listBook, link, orderToLe,orderIllSiteA,orderIllSiteB);

			List<OrderProductComponent> orderProductComponents = new ArrayList<>();
			orderProductComponents.addAll(
					orderProductComponentRepository.findByReferenceIdAndType(link.getId(), CommonConstants.LINK));
			orderProductComponents.addAll(
					orderProductComponentRepository.findByReferenceIdAndType(link.getSiteAId(), CommonConstants.SITEA));
			orderProductComponents.addAll(
					orderProductComponentRepository.findByReferenceIdAndType(link.getSiteBId(), CommonConstants.SITEB));

			Map<String, OrderProductComponent> componentPriceMap = new HashMap<>();
			for (OrderProductComponent orderProductComponent : orderProductComponents) {
				Map<String, String> attributesMap = new HashMap<>();

				String compName = orderProductComponent.getMstProductComponent().getName();
				if (compName != null && (compName.contains(OrderDetailsExcelDownloadConstants.LAST_MILE)
						|| compName.contains("SITE_PROPERTIES")))
					compName = compName + orderProductComponent.getType();
				componentPriceMap.put(compName, orderProductComponent);

				for (OrderProductComponentsAttributeValue attributeValue : orderProductComponent
						.getOrderProductComponentsAttributeValues()) {
					attributesMap.put(attributeValue.getProductAttributeMaster().getName(),
							attributeValue.getAttributeValues());
				}
				excelMap.put(compName, attributesMap);

			}
			
			Map<String, String> interfaceList = new HashMap<>();
			LOGGER.info("BEFOR createLinkNetworkComponentForExcel METHOD ");
			createLinkNetworkComponentForExcel(excelMap, listBook, link, interfaceList);
			String siteAInterface = null, siteBInterface = null;
			if (interfaceList.size() != 0) {
				siteAInterface = interfaceList.get(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_A_END);
				siteBInterface = interfaceList.get(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_B_END);
			}
			createSiteAPropAndNetworDetailsForExcel(excelMap, listBook, orderIllSiteA, componentPriceMap, feasible,
					feasibilityModeA, link, (orderLinkFeasibility!=null?orderLinkFeasibility.getProvider():null), siteAInterface, notFeasible, orderToLe);

			createSiteBPropAndNetworDetailsForExcel(excelMap, listBook, orderIllSiteB, componentPriceMap, feasible,
					feasibilityModeB, link, (orderLinkFeasibility!=null?orderLinkFeasibility.getProviderB():null), siteBInterface, notFeasible, orderToLe);
		

			createSlaDetails(link, listBook);
			
			if(quoteToLe.getQuote().getQuoteCode().startsWith("NDE")) {
				createBillingComponentPriceNde(listBook, link);
			}
			else {
				createBillingComponentPrice(listBook, link);
			}

			// Termination
			if (orderToLe.getOrder().getOrderCode().startsWith("NDE") && MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())) {
				createNDETerminationDetails(listBook, attributeValues, orderToLe , link);
			}
		  }

		}
		createBillingDetails(listBook, attributeValues, orderToLe);

		return listBook;
	}

	private void getSiDets(Map<String, String> siDetsSingle, Map<String, String> siDetsSecondary, String serviceId, String type, OrderToLe orderToLe)
	throws TclCommonException
	{
		Map<String, Map<String, String>> siDets = new HashMap<>();
		LOGGER.info("Service Id "+serviceId+" Type "+type);
		QuoteToLe quoteToLe=orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get();
		if (type != null && type.equalsIgnoreCase("MACD")) {
			siDets.putAll(SiDets(orderToLe.getOrder().getQuote().getId(), quoteToLe.getId(),
					serviceId, quoteToLe, orderToLe));
			if (siDets.containsKey("Single")) {
				siDetsSingle.putAll(siDets.get("Single"));
			}
		}

	}

	private Map<String, Map<String, String>> SiDets(Integer quoteId, Integer quoteLeId, String serviceId, QuoteToLe quoteToLe, OrderToLe orderToLe)
	throws TclCommonException {

			Map<String, String> oldAttributesMapSingle = new HashedMap<>();
			Map<String, Map<String, String>> actionMap = new HashMap<>();
			SIServiceInfoBean[] siDetailedInfoResponseNPL = null;
			List<SIServiceInfoBean> siServiceInfoResponse = null;
			Optional<QuoteToLe> quoteToLeOptional=quoteToLeRepository.findById(quoteLeId);
			List<QuoteIllSiteToService> quoteIllSiteToServices=quoteIllSiteToServiceRepository.findByTpsSfdcParentOptyIdAndQuoteToLe(quoteToLeOptional.get().getTpsSfdcParentOptyId(),quoteToLeOptional.get());
			QuoteIllSiteToService quoteIllSiteToService=quoteIllSiteToServices.stream().findFirst().get();
			Integer siParentOrderId=quoteIllSiteToService.getErfServiceInventoryParentOrderId();


		if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(serviceId))
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeOpt.isPresent()) {
				// Queue call to get old attribute values from service details
				String nplQueueResponse = (String) mqUtils.sendAndReceive(siNplDetailsQueue, serviceId);

				if (org.apache.commons.lang.StringUtils.isNotBlank(nplQueueResponse)) {
					siDetailedInfoResponseNPL = (SIServiceInfoBean[]) Utils.convertJsonToObject(nplQueueResponse,
							SIServiceInfoBean[].class);
					siServiceInfoResponse = Arrays.asList(siDetailedInfoResponseNPL);
					// Logic to get new attribute values from inventory
					siServiceInfoResponse.stream().forEach(detailedInfo -> {
						if (StringUtils.isNotBlank(detailedInfo.getTpsServiceId()) && StringUtils.isNotEmpty(detailedInfo.getTpsServiceId()))
						{
						if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)) {
							oldAttributesMapSingle.put("Service ID", detailedInfo.getTpsServiceId());
						}
						}

						getLocationDetails(oldAttributesMapSingle, detailedInfo);
						getPortBandwidth(oldAttributesMapSingle, detailedInfo);
						getInterfaceDetails(oldAttributesMapSingle,  detailedInfo);
						getLocalLoopBandwidthDetails(oldAttributesMapSingle, detailedInfo);
						getAdditionalIPDetails(oldAttributesMapSingle, detailedInfo);
						getProviderDetail(oldAttributesMapSingle, detailedInfo);
						getLastMileType(oldAttributesMapSingle, detailedInfo);
						getServiceType(oldAttributesMapSingle, detailedInfo);
						getServiceOption(oldAttributesMapSingle, detailedInfo);
						getAdditionalIpsReq(oldAttributesMapSingle, detailedInfo);
						getOwnerDetails(oldAttributesMapSingle, detailedInfo);
					});
				}
				if (quoteToLeOpt.get().getTpsSfdcParentOptyId() != null) {
					oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.SFDC_ID,
							(quoteToLeOpt.get().getTpsSfdcParentOptyId()).toString());
				}
			}
			actionMap.put("Single", oldAttributesMapSingle);
			return actionMap;
		}

	private void getOwnerDetails(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.ACCOUNT_MANAGER.toString(),
					(detailedInfo.getAccountManager()));
	}

	private void getAdditionalIpsReq(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.ADDITIONALIP.toString(),
					(detailedInfo.getAdditionalIpsReq()));
	}

	private void getServiceOption(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.SERVICE_OPTION.toString(),
					(detailedInfo.getServiceOption()));
	}

	//FROM INVENTORY 
	private void getServiceType(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.SERVICE_TYPE.toString(),
					(detailedInfo.getServiceType()));
	}

	private void getLastMileType(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.LM_TYPE, detailedInfo.getLastMileType());
	}

	private void getProviderDetail(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.PROVIDER.toString(),
					(detailedInfo.getLastMileProvider()));
	}

	private void getAdditionalIPDetails(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS,
					(detailedInfo.getIpAddressArrangement()));
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.IPV4_ADDRESS_POOL_SIZE,
					(detailedInfo.getIpv4AddressPoolSize()));
			oldAttributesMapSingle.put(OrderDetailsExcelDownloadConstants.IPV6_ADDRESS_POOL_SIZE,
					(detailedInfo.getIpv6AddressPoolSize()));
	}

	private void getLocalLoopBandwidthDetails(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(FPConstants.LOCAL_LOOP_BW.toString(), (detailedInfo.getLastMileBandwidth()
					+ CommonConstants.SPACE + detailedInfo.getLastMileBandwidthUnit()));
	}

	private void getInterfaceDetails(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(FPConstants.INTERFACE.toString(), detailedInfo.getSiteEndInterface());
	}


	private void getPortBandwidth(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(FPConstants.PORT_BANDWIDTH.toString(),
					(detailedInfo.getBandwidthPortSpeed() + CommonConstants.SPACE + detailedInfo.getBandwidthUnit()));
	}

	private void getLocationDetails(Map<String, String> oldAttributesMapSingle, SIServiceInfoBean detailedInfo)
	{
			oldAttributesMapSingle.put(MACDConstants.LAT_LONG, detailedInfo.getLocationId().toString());
			oldAttributesMapSingle.put(MACDConstants.LOCATION_ID, detailedInfo.getSiteAddress());

	}


	/**
	 * writeBook - writes data into excel
	 * 
	 * @param aBook
	 * @param row
	 * @throws TclCommonException
	 */
	public void writeBook(ExcelBean aBook, Row row) throws TclCommonException {
		if (Objects.isNull(aBook) || Objects.isNull(row)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		Cell cell = row.createCell(1);
		if (aBook.getLinkId() != null && aBook.getLinkId() == 0) {
			cell.setCellValue(aBook.getLrSection());
		} else {
			if ((aBook.getSiteAId() != null && aBook.getSiteAId() == 0)
					|| (aBook.getSiteBId() != null && aBook.getSiteBId() == 0)) {
				cell.setCellValue(aBook.getLrSection());
			} else if (aBook.getSiteAId() != null) {
				cell.setCellValue(aBook.getLrSection() + ":" + aBook.getSiteAId());
			} else if (aBook.getSiteBId() != null) {
				cell.setCellValue(aBook.getLrSection() + ":" + aBook.getSiteBId());
			} 
			else if (aBook.getSiteId() != null) {
				
				if(aBook.getSiteId() == 0) {
					cell.setCellValue(aBook.getLrSection());
				}
				else {
				cell.setCellValue(aBook.getLrSection() + ":" + aBook.getSiteId());
				}
			}
			else {
				cell.setCellValue(aBook.getLrSection() + ":" + aBook.getLinkId());
			}
		}

		cell = row.createCell(2);
		cell.setCellValue(aBook.getAttributeName());

		cell = row.createCell(3);
		cell.setCellValue(aBook.getAttributeValue());
	}

	/**
	 * getLinks - retrieves links under an orderToLe
	 * 
	 * @param orderToLe
	 * @param version
	 * @return
	 */
	private List<OrderNplLink> getLinks(OrderToLe orderToLe) {
		LOGGER.info("Getting get links {}", orderToLe.getId());
		List<OrderNplLink> orderNplLinkList = new ArrayList<>();
		MstProductFamily mstProductFamily=new MstProductFamily();
		if(orderToLe.getOrder().getOrderCode().startsWith("NDE")) {
		  mstProductFamily = mstProductFamilyRepository
				.findByNameAndStatus("NDE", (byte) 1);
		}
		else{
			 mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(OrderDetailsExcelDownloadConstants.NPL, (byte) 1);
		}
		if (mstProductFamily != null) {
			OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
					.findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
			List<OrderProductSolution> orderProductSolutionList = orderProductSolutionRepository
					.findByOrderToLeProductFamily(orderToLeProductFamily);

			orderProductSolutionList.stream().forEach(orderProductSolution -> {
				orderNplLinkList.addAll(orderNplLinkRepository
						.findByProductSolutionIdAndStatus(orderProductSolution.getId(), (byte) 1));
			});
		}
		LOGGER.info("Get links completed {}", orderToLe.getId());
		return orderNplLinkList;
	}

	/**
	 * createSiteDetailsBasedOnFeasibility - method extracting feasibility info from
	 * order and converts it into excel bean
	 * 
	 * @param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param feasible2
	 */
	private void createLinkDetailsBasedOnFeasibility(OrderLinkFeasibility orderLinkFeasibility,
			List<ExcelBean> listBook, OrderNplLink link, Feasible feasible, OrderToLe orderToLe)
			throws TclCommonException {
		ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID, orderLinkFeasibility.getFeasibilityCode());
		book19.setOrder(2);
		book19.setLinkId(link.getId());
		listBook.add(book19);
		ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID, orderLinkFeasibility.getFeasibilityCode());
		book20.setOrder(2);
		book20.setLinkId(link.getId());
		listBook.add(book20);

		ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
				feasible == null ? "" : feasible.getACustomerSegment());
		book3.setOrder(2);
		book3.setLinkId(link.getId());
		listBook.add(book3);

		if (!orderToLe.getOrder().getOrderCode().startsWith("NDE")) {
			ExcelBean serviveType = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
					OrderDetailsExcelDownloadConstants.SERVICE_TYPE,
					getProductFamily(OrderDetailsExcelDownloadConstants.NPL, orderToLe) + "-" + link.getLinkType());
			serviveType.setOrder(1);
			serviveType.setLinkId(link.getId());
			listBook.add(serviveType);
		}
		else {
			List<OrderProductComponentsAttributeValue> attributeList=new ArrayList<OrderProductComponentsAttributeValue>();
			String hub="No";
			MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
					.findByName(OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY);
			Set<OrderProductComponentsAttributeValue> attributeSet=new HashSet<OrderProductComponentsAttributeValue>();
			List<OrderProductComponent> ordercompolist=orderProductComponentRepository.findByReferenceIdAndMstProductComponent(link.getId(), internetPortmstProductComponent);
			if(ordercompolist.size()!=0) {
				attributeSet=ordercompolist.get(0).getOrderProductComponentsAttributeValues();
				attributeList=attributeSet.stream().collect(Collectors.toList());
			}
			if(attributeList.size()!=0) {
				for(OrderProductComponentsAttributeValue attributeVal:attributeList) {
					 if (attributeVal.getProductAttributeMaster().getName()
							.equals(NplPDFConstants.HUB_PARENTED)) {
						if (StringUtils.isNotBlank(attributeVal.getAttributeValues())) {
							hub=attributeVal.getAttributeValues();
						}
							
					}
				}
			}
			LOGGER.info("hub data"+hub);
			if(hub.equalsIgnoreCase("Yes")) {
			ExcelBean serviveType = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
					OrderDetailsExcelDownloadConstants.SERVICE_TYPE,
					"Hub NDE");
			serviveType.setOrder(1);
			serviveType.setLinkId(link.getId());
			listBook.add(serviveType);
			}
			else {
				ExcelBean serviveType = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
						OrderDetailsExcelDownloadConstants.SERVICE_TYPE,
						"NDE");
				serviveType.setOrder(1);
				serviveType.setLinkId(link.getId());
				listBook.add(serviveType);
			}
		}

		ExcelBean linkCode = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.LINK_CODE,
				orderLinkFeasibility.getOrderNplLink().getLinkCode() == null ? ""
						: orderLinkFeasibility.getOrderNplLink().getLinkCode());
		linkCode.setOrder(2);
		linkCode.setLinkId(link.getId());
		listBook.add(linkCode);
		if (orderLinkFeasibility != null && orderLinkFeasibility.getOrderNplLink() != null
				&& orderLinkFeasibility.getOrderNplLink().getCreatedBy() != null
				&& orderLinkFeasibility.getOrderNplLink().getEffectiveDate() != null) {

			String lead = "";
			ExcelBean leadTime;
			LocalDateTime today = LocalDateTime.now();
			String currentDate = Utils.convertTimeStampToString(Timestamp.valueOf(today));

			LOGGER.info("Today's Date: {}",currentDate);
			if (orderLinkFeasibility.getOrderNplLink().getRequestorDate()!= null)
			{
				Date requestorDate = orderLinkFeasibility.getOrderNplLink().getRequestorDate();
				String requestDate = Utils.convertTimeStampToString((Timestamp) requestorDate);
				LOGGER.info("Requestor Date: {}",requestDate);
				lead = String.valueOf(Utils.findDifferenceInDays(currentDate,requestDate));
				leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.LEAD_TIME, lead);
				LOGGER.info("Lead time from requestor date: {}", lead);
			}
			else {
				Date effectiveDate = orderLinkFeasibility.getOrderNplLink().getEffectiveDate();
				String effectDate = Utils.convertTimeStampToString((Timestamp) effectiveDate);
				LOGGER.info("Effective Date: {}", effectDate);
				lead = String.valueOf(Utils.findDifferenceInDays(currentDate,effectDate));
				leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.LEAD_TIME, lead);
				LOGGER.info("Lead time from effective date: {}", lead);
			}

			leadTime.setOrder(2);
			leadTime.setSiteId(link.getId());
			listBook.add(leadTime);
		}
	}

	/**
	 * createLinkDetailsBasedOnFeasibility - method retrieves link details in excel
	 * bean format
	 * 
	 * @param orderLinkFeasibility
	 * @param listBook
	 * @param link
	 * @param notFeasible
	 * @throws TclCommonException
	 */
	public void createLinkDetailsBasedOnFeasibility(OrderLinkFeasibility orderLinkFeasibility, List<ExcelBean> listBook,
			OrderNplLink link, NotFeasible notFeasible, OrderToLe orderToLe) throws TclCommonException {

		if (Objects.isNull(orderLinkFeasibility) || Objects.isNull(listBook) || Objects.isNull(link)
				|| Objects.isNull(orderToLe)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID, orderLinkFeasibility.getFeasibilityCode());
		book19.setOrder(2);
		book19.setLinkId(link.getId());
		listBook.add(book19);
		ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID, orderLinkFeasibility.getFeasibilityCode());
		book20.setOrder(2);
		book20.setLinkId(link.getId());
		listBook.add(book20);

		ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
				notFeasible == null ? "" : notFeasible.getCustomerSegmentA());
		book3.setOrder(2);
		book3.setLinkId(link.getId());
		listBook.add(book3);

		ExcelBean serviveType = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.SERVICE_TYPE,
				getProductFamily(OrderDetailsExcelDownloadConstants.NPL, orderToLe) + "-" + link.getLinkType());
		serviveType.setOrder(1);
		serviveType.setLinkId(link.getId());
		listBook.add(serviveType);

		ExcelBean linkCode = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.LINK_CODE,
				orderLinkFeasibility.getOrderNplLink().getLinkCode() == null ? ""
						: orderLinkFeasibility.getOrderNplLink().getLinkCode());
		linkCode.setOrder(2);
		linkCode.setLinkId(link.getId());
		listBook.add(linkCode);

		String lead = "";
		ExcelBean leadTime;
		LocalDateTime today = LocalDateTime.now();
		String currentDate = Utils.convertTimeStampToString(Timestamp.valueOf(today));

		LOGGER.info("Today's Date: {}",currentDate);
		if (orderLinkFeasibility.getOrderNplLink().getRequestorDate()!= null)
		{
			Date requestorDate = orderLinkFeasibility.getOrderNplLink().getRequestorDate();
			String requestDate = Utils.convertTimeStampToString((Timestamp) requestorDate);
			LOGGER.info("Requestor Date: {}",requestDate);
			lead = String.valueOf(Utils.findDifferenceInDays(currentDate,requestDate));
			leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TIME, lead);
			LOGGER.info("Lead time from requestor date: {}", lead);
		}
		else
		{
			Date effectiveDate = orderLinkFeasibility.getOrderNplLink().getEffectiveDate();
			String effectDate = Utils.convertTimeStampToString((Timestamp) effectiveDate);
			LOGGER.info("Effective Date: {}", effectDate);
			lead = String.valueOf(Utils.findDifferenceInDays(currentDate,effectDate));
			leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TIME, lead);
			LOGGER.info("Lead time from effective date: {}", lead);
		}
		leadTime.setOrder(2);
		leadTime.setSiteId(link.getId());
		listBook.add(leadTime);
	}

	/**
	 * getAttributeValues - retrieves attribute values for an OrderToLe
	 * 
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, String> getAttributeValues(OrderToLe orderToLe) throws TclCommonException {
		Map<String, String> attributeMap = new HashMap<>();
		LOGGER.info("Get attributes started {}", orderToLe.getId());
		try {

			if (Objects.isNull(orderToLe)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			orderToLe.getOrdersLeAttributeValues().forEach(attr -> {
				attributeMap.put(attr.getMstOmsAttribute().getName(), attr.getAttributeValue());

			});

			constructCreditCheckVariables(attributeMap, orderToLe);
		} catch (Exception e) {
			LOGGER.error("getAttributeValues error", e);
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Get attributes completed {}", orderToLe.getId());
		return attributeMap;
	}

	private void constructCreditCheckVariables(Map<String, String> attributeMap, OrderToLe orderToLe) {
		if(orderToLe.getTpsSfdcCreditLimit() != null)
			attributeMap.put(OrderDetailsExcelDownloadConstants.CREDIT_LIMIT,getFormattedCurrency(orderToLe.getTpsSfdcCreditLimit(),attributeMap.get(OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY)));
	}
	/**
	 * Method to format currency based on locale USD - 100,000. INR 1,00,000
	 *
	 * @param num
	 * @param currency
	 * @return formatted currency
	 */
	private String getFormattedCurrency(Double num, String currency) {

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		if (currency.equals("INR")) {
			formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		}

		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setCurrencySymbol("");
		formatter.setDecimalFormatSymbols(symbols);
		if (num != null) {
			return formatter.format(num);
		} else {
			return num + "";
		}
	}

	/**
	 * createOrderDetails - extracts order related details as excel bean
	 * 
	 * @param listBook
	 * @param attributeValues
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private void createOrderDetails(List<ExcelBean> listBook, Map<String, String> attributeValues, OrderToLe orderToLe)
			throws TclCommonException, IllegalArgumentException {
		ExcelBean orderDetails = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ORDER_REF_ID, orderToLe.getOrder().getOrderCode());
		orderDetails.setOrder(1);
		orderDetails.setLinkId(0);
		listBook.add(orderDetails);
		ExcelBean supplierInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.NPL_SUPPLIER_NAME,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.NPL_SUPPLIER_NAME)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.NPL_SUPPLIER_NAME)
						: OrderDetailsExcelDownloadConstants.TCL);
		supplierInfo.setOrder(1);
		supplierInfo.setLinkId(0);
		listBook.add(supplierInfo);

		ExcelBean ownerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.Le_Owner,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.Le_Owner)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.Le_Owner)
						: OrderDetailsExcelDownloadConstants.NOT_APPLICABLE);
		ownerInfo.setOrder(1);
		ownerInfo.setLinkId(0);
		listBook.add(ownerInfo);


        if (Objects.nonNull(orderToLe.getOrder().getEngagementOptyId())
                && (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())
                || (SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())))) {

            String leOwnerName = "";
            leOwnerName = attributeValues.containsKey(OrderDetailsExcelDownloadConstants.Le_Owner)
                    ? attributeValues.get(OrderDetailsExcelDownloadConstants.Le_Owner)
                    : "";

            List<PartnerLegalEntityBean> partnerLegalEntites = partnerService
                    .getPartnerLegalEntiy(Integer.valueOf(orderToLe.getOrder().getEngagementOptyId()));
            ExcelBean partnerLeName = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.PARTNER_LE_NAME, partnerLegalEntites.isEmpty() ? ""
                    : partnerLegalEntites.stream().findFirst().get().getEntityName());
            partnerLeName.setOrder(1);
            partnerLeName.setSiteId(0);
            listBook.add(partnerLeName);

            ExcelBean partnerCuid = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.PARTNER_CUID, partnerLegalEntites.isEmpty() ? ""
                    : partnerLegalEntites.stream().findFirst().get().getTpsSfdcCuid());
            partnerCuid.setOrder(1);
            partnerCuid.setSiteId(0);
            listBook.add(partnerCuid);

            if (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
                CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = partnerService
                        .getCustomerLeDetailsMQCall(String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));

                ExcelBean customerLeName = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                        OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
                        customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get()
                                .getLegalEntityName() + " ," + leOwnerName);
                customerLeName.setOrder(1);
                customerLeName.setSiteId(0);
                listBook.add(customerLeName);

                ExcelBean customerCuid = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                        OrderDetailsExcelDownloadConstants.CUID,
                        customerLegalEntityDetailsBean.getCustomerLeDetails().isEmpty() ? ""
                                : customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get()
                                .getSfdcId());
                customerCuid.setOrder(1);
                customerCuid.setSiteId(0);
                listBook.add(customerCuid);

            } else {
                EngagementToOpportunity engagementToOpportunity = partnerService
                        .getEngagementToOpportunity(Integer.valueOf(orderToLe.getOrder().getEngagementOptyId()));

                ExcelBean customerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                        OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
                        engagementToOpportunity.getOpportunity().getCustomerLeName() + " ," + leOwnerName);
                customerInfo.setOrder(1);
                customerInfo.setSiteId(0);
                listBook.add(customerInfo);
            }
        }
        else {
            ExcelBean customerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
                    attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_NAME)
                            ? attributeValues.get(OrderDetailsExcelDownloadConstants.LE_NAME)
                            : OrderDetailsExcelDownloadConstants.NOT_APPLICABLE);
            customerInfo.setOrder(1);
            customerInfo.setLinkId(0);
            listBook.add(customerInfo);

            ExcelBean cuId = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.CUID,
                    attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUID_VALUE)
                            ? attributeValues.get(OrderDetailsExcelDownloadConstants.CUID_VALUE)
                            : "");
            cuId.setOrder(1);
            cuId.setSiteId(0);
            listBook.add(cuId);
        }
		
		if("NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName())){
		ExcelBean izo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.IZO_PRIVATE_CONNECT, "No");
		izo.setOrder(1);
		izo.setSiteId(0);
		listBook.add(izo);
		
		ExcelBean porttype = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PORT_TYPE, "Fixed");
		porttype.setOrder(1);
		porttype.setSiteId(0);
		listBook.add(porttype);
		
		ExcelBean cpereq = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CPE_REQUIRED, "No");
		cpereq.setOrder(1);
		cpereq.setSiteId(0);
		listBook.add(cpereq);
		
		}
		
		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType()) || MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())
							|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType()) ) {
			if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType())) {
			ExcelBean type = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.ORDER_TYPE, "MACD");
			type.setOrder(1);
			type.setLinkId(0);
			listBook.add(type);
			} else if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				ExcelBean type = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.ORDER_TYPE, String.valueOf(orderToLe.getOrderType()));
				type.setOrder(1);
				type.setLinkId(0);
				listBook.add(type);
			} else if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())) {
				ExcelBean type = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.ORDER_TYPE, String.valueOf(orderToLe.getOrderType()));
				type.setOrder(1);
				type.setLinkId(0);
				listBook.add(type);
			}

			String orderType = getChangeRequestSummary(orderToLe.getOrder().getQuote().getQuoteToLes().stream()
					.findFirst().get().getChangeRequestSummary());
			if (Objects.isNull(orderType)) {
				orderType = orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get()
						.getQuoteCategory();
			}
			/*
			 * if(!(NplPDFConstants.NATIONAL_DEDICATED_ETHERNET).equalsIgnoreCase(orderToLe.
			 * getOrderToLeProductFamilies().stream().findFirst().get()
			 * .getOrderProductSolutions().stream().findFirst().get().getMstProductOffering(
			 * ).getProductName() )) {
			 */
			if(!("NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName()))) {
				ExcelBean macdOrderCategory = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.OPTIMUS_ORDER_TYPE, orderType);
				macdOrderCategory.setOrder(1);
				macdOrderCategory.setSiteId(0);
				listBook.add(macdOrderCategory);
			}

				String serviceId[] = {null};
				final String[] alias = {null};
				String[] newPortBandwidth = {"0"};
				String customerRequestDate = null;
				String terminationRequestDate = null;
				MDMServiceInventoryBean[] serviceInventoryMDM = {null};


			if("NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName())
						&& !MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())){
				String orderCategoery="";
				if (orderToLe.getOrderType().equalsIgnoreCase("MACD") && orderToLe.getOrderCategory().equalsIgnoreCase("SHIFT_SITE")
						&& Objects.nonNull(orderToLe.getChangeRequestSummary()) && orderToLe.getChangeRequestSummary().contains(CHANGE_BANDWIDTH)) {

					orderCategoery = "Change Bandwidth" + MACDConstants.COMMA + " " + "Shift Site";

					LOGGER.info("inside NDE macd shift site and change bandwidth" + orderCategoery);
				}
				else {
					orderCategoery=orderToLe.getOrderCategory();
				}
				LOGGER.info("orderCategoery" + orderCategoery);
				ExcelBean macdOrderSubCategory = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.ORDER_CATEGORY, orderCategoery);
				macdOrderSubCategory.setOrder(1);
				macdOrderSubCategory.setSiteId(0);
				listBook.add(macdOrderSubCategory);

				if(!MACDConstants.CANCELLATION.equals(orderToLe.getOrderType())) {
				QuoteToLe quoteToLe = orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get();
				String attributeValue="";
				List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository.findByNameAndIsActive(
						OrderDetailsExcelDownloadConstants.SFDC_NDE_ORDER_TYPE,(byte)1);
				List<QuoteLeAttributeValue> quoteLeAttributeValue = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe,mstOmsAttribute.get(0).getName());
				if(quoteLeAttributeValue.size()!=0) {
					attributeValue=quoteLeAttributeValue.get(0).getAttributeValue();
				}
				ExcelBean macdOrderType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.OPTIMUS_ORDER_TYPE, attributeValue);
				macdOrderType.setOrder(1);
				macdOrderType.setSiteId(0);
				listBook.add(macdOrderType);
				}
				
				

			}

			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
			List<OrderNplLink> orderNplLinkList = getLinks(orderToLe);
			OrderNplLink link = new OrderNplLink();
			Map<String, String> serviceIdMap =new HashMap<>();
			if(!orderNplLinkList.isEmpty()){
				link=orderNplLinkList.stream().findFirst().get();
				serviceIdMap = macdUtils.getServiceIdBasedOnOrderLink(link, orderToLe);
			}
			else{
				serviceIdMap = macdUtils.getServiceIdBasedOnOrderLe(orderToLe);
			}

			serviceId[0] = serviceIdMap.get(PDFConstants.LINK);
			if(Objects.isNull(serviceId[0])){
				serviceId[0]=serviceIdMap.get(PDFConstants.PRIMARY);
			}
			List<SIServiceDetailDataBean> sIServiceDetailDataBean = null;
			if(!MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType()))
				sIServiceDetailDataBean = macdUtils.getServiceDetailNPL(serviceId[0]);

			Optional<String> productName = quoteToLe.stream().findFirst().get().getQuoteToLeProductFamilies().stream()
					.map(QuoteToLeProductFamily::getMstProductFamily).map(MstProductFamily::getName)
					.filter(product -> product.equalsIgnoreCase(NPL)).findFirst();
			String linkTypeLr = "";

			//duplicate it is visible in link details only for nde
			
			if(!"NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName())) {
				ExcelBean existingCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.EXISTING_CIRCUIT_ID, serviceId[0]);
				existingCircuitId.setOrder(1);
				existingCircuitId.setSiteId(0);
				listBook.add(existingCircuitId);
			}
			
			if (Objects.nonNull(serviceId)) {
				if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
					serviceInventoryMDM[0] = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null, serviceId[0], null, null);
					alias[0] = serviceInventoryMDM[0].getServiceDetailBeans().get(0).getAlias();
				} else {
				List<SIServiceDetailDataBean> serviceDetail = macdUtils.getServiceDetailNPL(serviceId[0]);
				if (Objects.nonNull(serviceDetail)) {
						alias[0] = serviceDetail.get(0).getAlias();

						String[] serviceType = {null};
						serviceType[0] = serviceDetail.get(0).getLinkType();

						if ("Single".equalsIgnoreCase(serviceType[0]) || Objects.isNull(serviceType[0]))
							serviceType[0] = "single";
						MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
								.findByName(OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY);
						List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
								.findByReferenceIdAndMstProductComponent(link.getId(), internetPortmstProductComponent);
						orderProductComponents.stream().forEach(orderProductComponent -> {
							if (orderProductComponent.getType().equalsIgnoreCase(serviceType[0])
									&& (OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY)
									.equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())) {
								orderProductComponent.getOrderProductComponentsAttributeValues().stream()
										.forEach(orderProductComponentsAttributeValue -> {
											if ("Port Bandwidth".equalsIgnoreCase(orderProductComponentsAttributeValue
													.getProductAttributeMaster().getName())) {
												newPortBandwidth[0] = orderProductComponentsAttributeValue
														.getAttributeValues();
											}
										});
							}
						});

						if (Objects.nonNull(alias[0])) {
							ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
									OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, alias[0]);
							serviceAlias.setOrder(1);
							serviceAlias.setSiteId(0);
							listBook.add(serviceAlias);
						} else {
							ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
									OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, "NA");
							serviceAlias.setOrder(1);
							serviceAlias.setSiteId(0);
							listBook.add(serviceAlias);
						}
						
						if(!MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType()) && !MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())) {

							String parallelRunDays = "";
							String parallelRundays = "";
							String lrLinkType = "";
							Map<String, String> attributes = getAttributesMap(orderToLe);
							parallelRunDays = attributes.get("Parallel Rundays");

							String llBwChangeA = null;
							String llBwChangeB = null;
							Boolean Crossconnect = false;
							String crossConnectType=null;
							OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository.findByOrderToLe_Id(orderToLe.getId());
							List<OrderProductSolution> orderProductSolution = orderProductSolutionRepository.findByOrderToLeProductFamily(orderToLeProductFamily);
							for (OrderProductSolution solution : orderProductSolution) {
								if (Objects.nonNull(solution.getMstProductOffering()) && Objects.nonNull(solution.getMstProductOffering().getProductName()) &&
										CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())) {
									Crossconnect = true;
									crossConnectType=sIServiceDetailDataBean.get(0).getCrossConnectType();

								}

							}

							Optional<OrderIllSite> orderIllSite = orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getOrderProductSolutions().stream().findFirst().get().getOrderIllSites().stream().findFirst();
							try {
								if (!Crossconnect) {
									llBwChangeA = Objects
											.nonNull(getLlBwChangeForSiteA(link, serviceId[0]))
											? getLlBwChangeForSiteA(link, serviceId[0])
											: "";
								} else if(crossConnectType.equalsIgnoreCase("Active")) {
									llBwChangeA = getLlBwChange(orderIllSite, orderToLe, serviceId[0]);
								}
							} catch (TclCommonException e) {
								LOGGER.info("EXCEPTION IN ORDER" + e);
							}
							try {
								if (!Crossconnect) {
									llBwChangeB = Objects
											.nonNull(getLlBwChangeForSiteB(link, serviceId[0]))
											? getLlBwChangeForSiteB(link, serviceId[0])
											: "";
								} else if(crossConnectType.equalsIgnoreCase("Active")) {
									llBwChangeA = getLlBwChange(orderIllSite, orderToLe, serviceId[0]);
								}
							} catch (TclCommonException e) {
								LOGGER.info("EXCEPTION IN ORDER" + e);
							}

							String portBwChange = null;
							try {
								if (!Crossconnect) {
									portBwChange = Objects
											.nonNull(getPortBwChange(link, serviceId[0]))
											? getPortBwChange(link, serviceId[0])
											: "";
								} else {
									portBwChange = "";
								}
							} catch (TclCommonException e) {
								LOGGER.info("EXCEPTION IN ORDER" + e);
							}

							if (!"".equalsIgnoreCase(parallelRunDays) && !"0".equalsIgnoreCase(parallelRunDays)) {
								parallelRundays = Objects.nonNull(parallelRunDays) ? parallelRunDays : NONE;
							} else
								parallelRundays = NONE;

							String upgradeOrDowngradeBwChange = isUpgradeOrDowngrade(llBwChangeA, portBwChange);
							String cityType = null;
							if(!Crossconnect) {
							try {
								cityType = getCityType(sIServiceDetailDataBean, link);
							} catch (TclCommonException e) {
								LOGGER.info("EXCEPTION IN ORDER" + e);
							}
						}

						lrLinkType = getLinkTypeLR(quoteToLe, linkTypeLr, parallelRundays, upgradeOrDowngradeBwChange,
								cityType);

				
						parallelRunDays = StringUtils.isNotBlank(parallelRunDays) ? parallelRunDays : "0";
						}
						
						// only visibe for npl not for nde
					/*
					 * if(!(NplPDFConstants.NATIONAL_DEDICATED_ETHERNET).equalsIgnoreCase(orderToLe.
					 * getOrderToLeProductFamilies().stream().findFirst().get()
					 * .getOrderProductSolutions().stream().findFirst().get().getMstProductOffering(
					 * ).getProductName() )) {
					 */

						/*
						 * ExcelBean linkType = new
						 * ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						 * OrderDetailsExcelDownloadConstants.LINK_TYPE_SFDC_ORDER_TYPE, lrLinkType);
						 * linkType.setOrder(1); linkType.setSiteId(0); listBook.add(linkType);
						 * 
						 * ExcelBean parallelLink = new
						 * ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						 * OrderDetailsExcelDownloadConstants.PARALLEL_LINK, parallelRunDays);
						 * parallelLink.setOrder(1); parallelLink.setSiteId(0);
						 * listBook.add(parallelLink);
						 */
						
						//}

				}
			}
			}
		}
		else {
			ExcelBean orderType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.ORDER_TYPE, "NEW");
			orderType.setOrder(1);
			orderType.setLinkId(0);
			listBook.add(orderType);
		}
		
		
		
		if(!"NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName())) {

		ExcelBean billingAddress = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_CONTRACTING_BILLING_ADDRESS,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_ADDRESS)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_ADDRESS)
						: "NA");
		billingAddress.setOrder(1);
		billingAddress.setLinkId(0);
		listBook.add(billingAddress);
		}
		else {
			BillingContact billingContact = getBillingDetails(
					attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_CONTACT_ID));

			String billingFName = Objects.nonNull(billingContact.getFname()) ? billingContact.getFname() : "";
			String billingLName = Objects.nonNull(billingContact.getLname()) ? billingContact.getLname() : "";
			String billingEmail = Objects.nonNull(billingContact.getEmailId()) ? billingContact.getEmailId() : "";
			String billingAddressDATA = Objects.nonNull(billingContact.getBillAddr()) ? billingContact.getBillAddr() : "";
			LOGGER.info("INSIDE NDE CONTRACTING ADDRESS+"+billingAddressDATA);
			ExcelBean billingAddress = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.CUSTOMER_CONTRACTING_BILLING_ADDRESS,
					billingAddressDATA);
			billingAddress.setOrder(1);
			billingAddress.setLinkId(0);
			listBook.add(billingAddress);
		}

		ExcelBean paymentType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.BILLING_TYPE, attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_TYPE)
				? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_TYPE)
				: "NA");
		paymentType.setOrder(1);
		paymentType.setLinkId(0);
		listBook.add(paymentType);

		ExcelBean paymentCurrency = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY,
				StringUtils.isAllBlank(orderToLe.getCurrencyCode()) ? "" : orderToLe.getCurrencyCode());
		paymentCurrency.setOrder(1);
		paymentCurrency.setLinkId(0);
		listBook.add(paymentCurrency);

		ExcelBean paymentMethod = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_METHOD,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.INVOICE_METHOD)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.INVOICE_METHOD)
						: "Paper/Electronic");
		paymentMethod.setOrder(1);
		paymentMethod.setLinkId(0);
		listBook.add(paymentMethod);

		ExcelBean paymentTerm = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_TERM,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PAYMENT_TERM_VALUE)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.PAYMENT_TERM_VALUE)
						: " ");
		paymentTerm.setOrder(1);
		paymentTerm.setSiteId(0);
		listBook.add(paymentTerm);

		ExcelBean billingCurrency = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.BILLING_CURRENCY,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_CURRENCY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_CURRENCY)
						: "");
		billingCurrency.setOrder(1);
		billingCurrency.setSiteId(0);
		listBook.add(billingCurrency);

		ExcelBean autoCofNumber = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.AUTO_COF_NUMBER, orderToLe.getOrder().getOrderCode());
		autoCofNumber.setOrder(1);
		autoCofNumber.setLinkId(0);
		listBook.add(autoCofNumber);

		ExcelBean sfdc = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.SFDC_ID, orderToLe.getTpsSfdcCopfId());
		sfdc.setOrder(1);
		sfdc.setLinkId(0);
		listBook.add(sfdc);
		
		/*
		 * ExcelBean book5 = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
		 * OrderDetailsExcelDownloadConstants.PROGRAM_MANAGER,
		 * LeAttributesConstants.PROGRAM_MANAGER); book5.setOrder(1);
		 * book5.setLinkId(0); listBook.add(book5);
		 */
		LOGGER.info("PROGRAM MANAGER NAME"+attributeValues.get(LeAttributesConstants.PROGRAM_MANAGER));
		ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PROGRAM_MANAGER,
				attributeValues.containsKey(LeAttributesConstants.PROGRAM_MANAGER)
						? attributeValues.get(LeAttributesConstants.PROGRAM_MANAGER)
						: "NA");
		book5.setOrder(1);
		book5.setSiteId(0);
		listBook.add(book5);

		/*
		 * ExcelBean book6 = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
		 * OrderDetailsExcelDownloadConstants.CONTRACT_TERMS,
		 * attributeValues.containsKey(OrderDetailsExcelDownloadConstants.
		 * TERMS_IN_MONTHS) ?
		 * attributeValues.get(OrderDetailsExcelDownloadConstants.TERMS_IN_MONTHS) :
		 * null);
		 */
		ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CONTRACT_TERMS, orderToLe.getTermInMonths());
		book6.setOrder(1);
		book6.setLinkId(0);
		listBook.add(book6);

		ExcelBean book8 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.OPPORTUNITY_CLASSIFICATION,
				orderToLe.getClassification());
		book8.setOrder(1);
		book8.setLinkId(0);
		listBook.add(book8);
		StringBuilder gstDetail = new StringBuilder();

		String gstAddress = "";
		String gstNo = "";


		if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_STATE_GST_NUMBER)) {
			gstNo= attributeValues.get(OrderDetailsExcelDownloadConstants.LE_STATE_GST_NUMBER);

		}else if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.GST_NUM)) {
			gstNo= attributeValues.get(OrderDetailsExcelDownloadConstants.GST_NUM);

		}
		if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_STATE_GST_ADDRESS)) {
			gstAddress= attributeValues.get(OrderDetailsExcelDownloadConstants.LE_STATE_GST_ADDRESS);
		}else if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.GST_ADDR)) {
			gstAddress= attributeValues.get(OrderDetailsExcelDownloadConstants.GST_ADDR);
		}

		if (gstDetail == null || gstDetail.toString().equalsIgnoreCase(CommonConstants.EMPTY)) {
			gstDetail.append(Objects.nonNull(gstNo) ? gstNo : CommonConstants.EMPTY);
			gstDetail.append(",")
					.append(Objects.nonNull(gstAddress) ? gstAddress : "NA");
		} else {
			if (gstDetail.toString().endsWith(CommonConstants.RIGHT_SLASH)) {
				gstDetail.setLength(gstDetail.length() - 1);
			}
		}

		ExcelBean book9 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_LE_GST, gstDetail.toString());
		book9.setOrder(1);
		book9.setLinkId(0);
		listBook.add(book9);

		ExcelBean book10 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ACCOUNT_MANAGER,
				attributeValues.containsKey(LeAttributesConstants.LE_NAME)
						? attributeValues.get(LeAttributesConstants.LE_NAME)
						: "");
		book10.setOrder(1);
		book10.setLinkId(0);
		listBook.add(book10);

		ExcelBean book12 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CREDIT_LIMIT,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CREDIT_LIMIT)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.CREDIT_LIMIT)
						: "NA");
		book12.setOrder(1);
		book12.setLinkId(0);
		listBook.add(book12);

		ExcelBean book13 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT)
						: "NA");
		book13.setOrder(1);
		book13.setLinkId(0);
		listBook.add(book13);

		ExcelBean book14 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.BILLING_RATIO,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.MST_BILLING_RATIO)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.MST_BILLING_RATIO)
						: "NA");
		book14.setOrder(1);
		book14.setLinkId(0);
		listBook.add(book14);

		ExcelBean billingCategory = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.BILLING_CATEGORY, OrderDetailsExcelDownloadConstants.BILLABLE);
		billingCategory.setOrder(1);
		billingCategory.setLinkId(0);
		listBook.add(billingCategory);

		ExcelBean book15 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PO_NUMBER,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PO_NUMBER_KEY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.PO_NUMBER_KEY)
						: "NA");
		book15.setOrder(1);
		book15.setLinkId(0);
		listBook.add(book15);

		ExcelBean book16 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PO_DATE,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PO_DATE_KEY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.PO_DATE_KEY)
						: "NA");
		book16.setOrder(1);
		book16.setLinkId(0);
		listBook.add(book16);

		if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUS_CRN_ID)) {
			LOGGER.info("Setting CRN id from le Attr");
			ExcelBean book17 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.CRN_ID,
					attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUS_CRN_ID)
							? attributeValues.get(OrderDetailsExcelDownloadConstants.CUS_CRN_ID)
							: "NA");
			book17.setOrder(1);
			book17.setSiteId(0);
			listBook.add(book17);
		} else {
			Integer customerLeId = orderToLe.getErfCusCustomerLegalEntityId();
			LOGGER.info("Getting customer microservice for getting the crnDetails for customerLeId {}", customerLeId);
			String crnId = (String) mqUtils.sendAndReceive(customerCrnQueue, customerLeId.toString());
			LOGGER.info("Response for customer microservice for getting the crnDetails for customerLeId {} is {}",
					customerLeId, crnId);
			ExcelBean book17 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.CRN_ID, StringUtils.isAllEmpty(crnId) ? "NA" : crnId);
			book17.setOrder(1);
			book17.setSiteId(0);
			listBook.add(book17);
		}

		LOGGER.info("Order details created");

	}

	/**
	 * Method to get link type LR
	 *
	 * @param quoteToLe
	 * @param linkTypeLr
	 * @param parallelRundays
	 * @param upgradeOrDowngradeBwChange
	 * @param cityType
	 * @return
	 */
	private String getLinkTypeLR(List<QuoteToLe> quoteToLe, String linkTypeLr, String parallelRundays,
								 String upgradeOrDowngradeBwChange, String cityType) {
		if (MACDConstants.CHANGE_BANDWIDTH_SERVICE
				.equalsIgnoreCase(quoteToLe.stream().findFirst().get().getQuoteCategory())) {
			linkTypeLr = getTypeBasedOnBWChangeAndParallelRundays(linkTypeLr, upgradeOrDowngradeBwChange,
					parallelRundays);

		}

		else if (MACDConstants.SHIFT_SITE_SERVICE
				.equalsIgnoreCase(quoteToLe.stream().findFirst().get().getQuoteCategory())) {
			// Parallel Upgrade and Downgrade for Shift Service - Intra(4cases)
			linkTypeLr = isParallelUpgradeOrDowngradeForIntra(linkTypeLr, upgradeOrDowngradeBwChange, cityType);
			// Intercity shifting with/without parallelRundays(4cases)
			linkTypeLr = orderTypeForShiftWithoutBwChange(linkTypeLr, parallelRundays, cityType,
					upgradeOrDowngradeBwChange);
		}

		return linkTypeLr;
	}

	private String orderTypeForShiftWithoutBwChange(String linkTypeLr, String parallelRundays, String cityType, String upgradeOrDowngradeBwChange) {

		if (CommonConstants.INTER_CITY.equalsIgnoreCase(cityType)) {
			if (CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
				linkTypeLr = MACDConstants.SHIFTING;
			} else if (!(CommonConstants.NONE.equalsIgnoreCase(parallelRundays))) {
				linkTypeLr = MACDConstants.PARALLEL_SHIFTING;
			}
		} else if (CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType)) {
			if (CommonConstants.NONE.equalsIgnoreCase(parallelRundays)
					&& "".equalsIgnoreCase(upgradeOrDowngradeBwChange)) {
				linkTypeLr = MACDConstants.LM_BSO_SHIFTING;
			} else if (!(CommonConstants.NONE.equalsIgnoreCase(parallelRundays))
					&& "".equalsIgnoreCase(upgradeOrDowngradeBwChange)) {
				linkTypeLr = MACDConstants.PARALLEL_SHIFTING;
			}
		}
		return linkTypeLr;
	}

	private String isParallelUpgradeOrDowngradeForIntra(String linkTypeLr, String upgradeOrDowngradeBwChange, String cityType) {
		if (CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType)) {
			linkTypeLr = CommonConstants.PARALLEL + upgradeOrDowngradeBwChange;
		}
		return linkTypeLr;
	}

	private String getTypeBasedOnBWChangeAndParallelRundays(String linkTypeLr, String upgradeOrDowngradeBwChange, String parallelRundays) {

		if (!CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
			linkTypeLr = CommonConstants.PARALLEL + upgradeOrDowngradeBwChange;
		} else if (CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
			linkTypeLr = CommonConstants.HOT + upgradeOrDowngradeBwChange;
		}
		return linkTypeLr;
	}

	private String getCityType(List<SIServiceDetailDataBean> sIServiceDetailDataBean, OrderNplLink link) throws TclCommonException
	{
		String existingCity = Objects.nonNull(getExistingCity(sIServiceDetailDataBean))
				? getExistingCity(sIServiceDetailDataBean)
				: "";
		String newCity = Objects.nonNull(getNewCity(link)) ? getNewCity(link) : "";
		String cityType = "";

		if (existingCity.equalsIgnoreCase(newCity)) {
			cityType = CommonConstants.INTRA_CITY;
		} else if (!existingCity.equalsIgnoreCase(newCity)) {
			cityType = CommonConstants.INTER_CITY;
		}
		return cityType;
	}

	private String getNewCity(OrderNplLink link) throws TclCommonException {

		OrderIllSite siteA = orderIllSitesRepository.findByIdAndStatus(link.getSiteAId(),BACTIVE);
		OrderIllSite siteB = orderIllSitesRepository.findByIdAndStatus(link.getSiteBId(),BACTIVE);
		com.tcl.dias.common.beans.AddressDetail  addressDetail;
		if (siteA.getNplShiftSiteFlag().equals(ACTIVE)) {
			String quoteSiteAddrId = siteA.getErfLocSitebLocationId().toString();
			addressDetail = getLocQueueResponse(quoteSiteAddrId);
		}
		else
		{
			String quoteSiteAddrId = siteB.getErfLocSitebLocationId().toString();
			addressDetail = getLocQueueResponse(quoteSiteAddrId);
		}
		return Objects.nonNull(addressDetail) ? addressDetail.getCity() : "";
	}

	private String getExistingCity(List<SIServiceDetailDataBean> sIServiceDetailDataBean) throws TclCommonException
	{
		com.tcl.dias.common.beans.AddressDetail addressDetail = new com.tcl.dias.common.beans.AddressDetail();

		String siSiteAddrId = Objects.nonNull(sIServiceDetailDataBean)
				? sIServiceDetailDataBean.get(0).getErfLocSiteAddressId()
				: "";
		if (Objects.nonNull(siSiteAddrId))
			addressDetail = getLocQueueResponse(siSiteAddrId);
		return Objects.nonNull(addressDetail) ? addressDetail.getCity() : "";
	}

	private com.tcl.dias.common.beans.AddressDetail getLocQueueResponse(String AddrId) throws TclCommonException {
		String locMstResponse = (String) mqUtils.sendAndReceive(addressDetailByLocationId, AddrId);
		return (com.tcl.dias.common.beans.AddressDetail) Utils.convertJsonToObject(locMstResponse,
				com.tcl.dias.common.beans.AddressDetail.class);
	}

	private String isUpgradeOrDowngrade(String llBwChange, String portBwChange)
	{
		String attrValue = "";
		if (CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange)
				|| CommonConstants.DOWNGRADE.equalsIgnoreCase(llBwChange)
				|| (CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange)
				&& CommonConstants.UPGRADE.equalsIgnoreCase(llBwChange)))
			attrValue = CommonConstants.DOWNGRADE;

		else if (CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange)
				|| CommonConstants.UPGRADE.equalsIgnoreCase(llBwChange)
				|| (CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange)
				&& CommonConstants.DOWNGRADE.equalsIgnoreCase(llBwChange)))
			attrValue = CommonConstants.UPGRADE;

		return attrValue;
	}

	private String getPortBwChange(OrderNplLink link, String serviceId) throws TclCommonException {

		Map<String, String> newAttributesMapPrimary = new HashedMap<>();

		String bwUnitPort = nplPricingFeasibilityService.getOldBandwidthUnit(serviceId,
				FPConstants.PORT_BANDWIDTH_UNIT.toString());

		Double oldPortBw = Double.parseDouble(nplPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString()));

		oldPortBw = Double
				.parseDouble(nplPricingFeasibilityService.setBandwidthConversion(oldPortBw.toString(), bwUnitPort));

		String newPBw = getInternetPortDetails(link,FPConstants.PORT_BANDWIDTH.toString(), FPConstants.NATIONAL_CONNECTIVITY.toString());
		String[] newBandWidthValue = newPBw.split(" ");
		Double newPortBw = Double.parseDouble(newBandWidthValue[0]);

		String changeInPortBw = "";

		if (Objects.nonNull(oldPortBw) && Objects.nonNull(newPortBw)) {

			int result = newPortBw.compareTo(oldPortBw);

			if (result > 0)
				changeInPortBw = CommonConstants.UPGRADE;
			else if (result < 0)
				changeInPortBw = CommonConstants.DOWNGRADE;
			else if (result == 0)
				changeInPortBw = CommonConstants.EQUAL;

		}
		return changeInPortBw;
	}

	private String getInternetPortDetails(OrderNplLink link, String attributeName, String componentName)
	{
		String[] newBw = new String[1];
		{
			LOGGER.info("Comp Name and Attribute Name{} ",componentName+attributeName);
			OrderProductComponent orderProductComponent = orderProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(link.getId(), componentName,QuoteConstants.NPL_LINK.toString()).stream().findFirst()
					.get();
			LOGGER.info("OrderProductComponent Object {},and component id{}",orderProductComponent,orderProductComponent.getId());

			OrderProductComponentsAttributeValue attributeValue;
			orderProductComponentsAttributeValueRepository
					.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent, attributeName)
					.stream().forEach(attribute -> {
						if (attribute.getProductAttributeMaster().getName().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))
						newBw[0] = attribute.getAttributeValues();
							});
		}
		return newBw[0];
	}

	private String getLlBwChangeForSiteA(OrderNplLink link, String serviceId) throws TclCommonException
	{
		String bwUnitLl = nplPricingFeasibilityService.getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString());

		String  oldBwValue = nplPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString());

		String[] oldBandWidthValue = oldBwValue.split(" ");
		Double oldLlBw = Double.parseDouble(oldBandWidthValue[0]);

		oldLlBw = Double.parseDouble(nplPricingFeasibilityService.setBandwidthConversion(oldLlBw.toString(), bwUnitLl));

		String newBwValue = getNewBandwidth(link, link.getSiteAId(), FPConstants.LAST_MILE.toString(), FPConstants.LOCAL_LOOP_BANDWIDTH.toString());

		String[] newBandWidthValue = newBwValue.split(" ");
		Double newLLBw = Double.parseDouble(newBandWidthValue[0]);

		String changeInLlBw = "";

		if (Objects.nonNull(oldBwValue) && Objects.nonNull(newLLBw)) {

			int result = newLLBw.compareTo(oldLlBw);

			if (result > 0)
				changeInLlBw = CommonConstants.UPGRADE;
			else if (result < 0)
				changeInLlBw = CommonConstants.DOWNGRADE;
			else if (result == 0)
				changeInLlBw = CommonConstants.EQUAL;

		}
		return changeInLlBw;
	}

	private String getLlBwChangeForSiteB(OrderNplLink link, String serviceId) throws TclCommonException
	{
		String bwUnitLl = nplPricingFeasibilityService.getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString());

		String  oldBwValue = nplPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString());

		String[] oldBandWidthValue = oldBwValue.split(" ");
		Double oldLlBw = Double.parseDouble(oldBandWidthValue[0]);

		oldLlBw = Double.parseDouble(nplPricingFeasibilityService.setBandwidthConversion(oldLlBw.toString(), bwUnitLl));

		String newBwValue = getNewBandwidth(link, link.getSiteBId(), FPConstants.LAST_MILE.toString(), FPConstants.LOCAL_LOOP_BANDWIDTH.toString());

		String[] newBandWidthValue = newBwValue.split(" ");
		Double newLLBw = Double.parseDouble(newBandWidthValue[0]);

		String changeInLlBw = "";

		if (Objects.nonNull(oldBwValue) && Objects.nonNull(newLLBw)) {

			int result = newLLBw.compareTo(oldLlBw);

			if (result > 0)
				changeInLlBw = CommonConstants.UPGRADE;
			else if (result < 0)
				changeInLlBw = CommonConstants.DOWNGRADE;
			else if (result == 0)
				changeInLlBw = CommonConstants.EQUAL;

		}
		return changeInLlBw;
	}

	private String getNewBandwidth(OrderNplLink link, Integer siteId, String componentName, String attributeName) {
		LOGGER.info("Comp Name and Attribute Name{}",componentName+attributeName);
		OrderProductComponent orderProductComponent = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId, componentName,QuoteConstants.NPL_SITES.toString()).stream().findFirst()
				.get();
		LOGGER.info("OrderProductComponent Object {},and component id{}",orderProductComponent,orderProductComponent.getId());
		OrderProductComponentsAttributeValue attributeValue = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent, attributeName)
				.stream().findFirst().get();
		LOGGER.info("Attr Value {}",attributeValue.getAttributeValues());
		return attributeValue.getAttributeValues();
	}


	public Map<String, String> getAttributesMap(OrderToLe orderToLe) {
		Map<String, String> attributesMap = new HashMap<>();

		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
		if (quoteToLe.stream().findFirst().isPresent()) {
			QuoteToLe quoteToLeOpt = quoteToLe.stream().findFirst().get();
			quoteToLeOpt.getQuoteToLeProductFamilies().stream().forEach(prodFamily -> {
				prodFamily.getProductSolutions().stream().forEach(prodSolution -> {
					prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
						try {
							List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
									.findByReferenceIdAndReferenceName(illSite.getId(),
											QuoteConstants.ILLSITES.toString());


							getParallelBuildAndParallelRunDays(quoteProductComponentList, attributesMap);
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}

					});
				});
			});

		}
		return attributesMap;
	}


	/**
	 * Method to get parallel build and paralled run days
	 *
	 * @param quoteProductComponentList
	 * @param response
	 * @return
	 */
	private Map<String, String> getParallelBuildAndParallelRunDays(
			List<QuoteProductComponent> quoteProductComponentList, Map<String, String> response) {
		quoteProductComponentList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(OrderDetailsExcelDownloadConstants.NPL_COMMON.toString())
						&& quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
				.forEach(attribute -> {
					if (attribute.getProductAttributeMaster().getName()
							.equals(MACDConstants.PARALLEL_BUILD.toString()))
						response.put("Parallel Build", attribute.getAttributeValues());
					if (attribute.getProductAttributeMaster().getName()
							.equals(MACDConstants.PARALLEL_RUN_DAYS.toString()))
						response.put("Parallel Rundays", attribute.getAttributeValues());
				}));
		return response;
	}

	public List<OrderIllSite> getSites(OrderToLe orderToLe) {

		List<OrderIllSite> orderIllSitesList = new ArrayList<>();
		MstProductFamily mstProductFamily = mstProductFamilyRepository
				.findByNameAndStatus(OrderDetailsExcelDownloadConstants.NPL, (byte) 1);
		if (mstProductFamily != null) {
			OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
					.findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
			List<OrderProductSolution> orderProductSolutionList = orderProductSolutionRepository
					.findByOrderToLeProductFamily(orderToLeProductFamily);

			orderProductSolutionList.stream().forEach(orderProductSolution -> {
				orderIllSitesList.addAll(
						orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution, (byte) 1));
			});
		}
		LOGGER.info("Sites received is {}", orderIllSitesList);
		return orderIllSitesList;
	}

	/**
	 * Method to get change request summary
	 *
	 * @param changeRequest
	 * @return
	 */
	private String getChangeRequestSummary(String changeRequest)
	{
		if (Objects.nonNull(changeRequest)) {
			changeRequest = changeRequest.replace("+", ",");
		}
		return changeRequest;
	}


	/**
	 * createSiteLocationForExcel
	 * 
	 * @param site
	 * @param listBook
	 */
	private void createSiteALocationForExcel(OrderIllSite orderIllSiteA, List<ExcelBean> listBook,
			String feasibilityMode, OrderNplLink orderNplLink) throws TclCommonException {

		AddressDetail adDetailA = null;
		String crossConnect = CommonConstants.NO;

		if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderIllSiteA.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrderType())) {
			List<OrderIllSiteToService> oSiteToServiceList = orderIllSiteToServiceRepository.findByOrderNplLink_Id(orderNplLink.getId());
			if(oSiteToServiceList != null && !oSiteToServiceList.isEmpty()) {
			MDMServiceInventoryBean serviceInventoryMDM = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null, oSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId(), null, null);
			ExcelBean siteLocationAddressLine1A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, serviceInventoryMDM.getServiceDetailBeans().get(0).getSiteAddress());
			siteLocationAddressLine1A.setOrder(2);
			siteLocationAddressLine1A.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationAddressLine1A);
		}
		} 
		else {

		String siteALocation = returnApiAddressForSites(orderIllSiteA.getErfLocSitebLocationId());

		if (siteALocation != null) {
			adDetailA = (AddressDetail) Utils.convertJsonToObject(siteALocation, AddressDetail.class);
		}
//		String addressSiteA = "";
//
//		if (adDetailA != null) {
//			addressSiteA = adDetailA.getAddressLineOne() + " " + adDetailA.getCity() + " " + adDetailA.getState() + ""
//					+ adDetailA.getPincode();
//		}

		if (adDetailA != null) {
			ExcelBean siteLocationAddressLine1A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, adDetailA.getAddressLineOne());
			siteLocationAddressLine1A.setOrder(2);
			siteLocationAddressLine1A.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationAddressLine1A);

			ExcelBean siteLocationAddressLine2A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, adDetailA.getAddressLineTwo());
			siteLocationAddressLine2A.setOrder(2);
			siteLocationAddressLine2A.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationAddressLine2A);

			ExcelBean siteLocationLocalityA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS_LOCALITY, adDetailA.getLocality());
			siteLocationLocalityA.setOrder(2);
			siteLocationLocalityA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationLocalityA);

			ExcelBean siteLocationCityA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS_CITY, adDetailA.getCity());
			siteLocationCityA.setOrder(2);
			siteLocationCityA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationCityA);

			ExcelBean siteLocationStateA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS_STATE, adDetailA.getState());
			siteLocationStateA.setOrder(2);
			siteLocationStateA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationStateA);

			ExcelBean siteLocationPincodeA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS_PIN_ZIPCODE, adDetailA.getPincode());
			siteLocationPincodeA.setOrder(2);
			siteLocationPincodeA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationPincodeA);

			ExcelBean siteLocationCountryA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS_COUNTRY, adDetailA.getCountry());
			siteLocationCountryA.setOrder(2);
			siteLocationCountryA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationCountryA);

		}
		}
	

		ExcelBean siteType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
				OrderDetailsExcelDownloadConstants.NPL_SITE_TYPE, orderNplLink.getSiteAType());

		if (Objects.nonNull(orderNplLink.getSiteAType()) && orderNplLink.getSiteAType().equalsIgnoreCase(OrderDetailsExcelDownloadConstants.DC)) {
			crossConnect = CommonConstants.YES;
		}
		ExcelBean siteCrossConnect = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
				OrderDetailsExcelDownloadConstants.CROSS_CONNECT, crossConnect);

		ExcelBean siteALastMileType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
				OrderDetailsExcelDownloadConstants.LM_TYPE, feasibilityMode == null? "" : feasibilityMode);

		siteALastMileType.setOrder(2);
		siteALastMileType.setSiteAId(orderIllSiteA.getId());
		siteType.setOrder(2);
		siteType.setSiteAId(orderIllSiteA.getId());
		siteCrossConnect.setOrder(2);
		siteCrossConnect.setSiteAId(orderIllSiteA.getId());
		listBook.add(siteType);
		listBook.add(siteCrossConnect);
		listBook.add(siteALastMileType);

	}

	/**
	 * createSiteLocationForExcel
	 * 
	 * @param site
	 * @param listBook
	 */
	private void createSiteBLocationForExcel(OrderIllSite orderIllSiteB, List<ExcelBean> listBook,
			String feasibilityMode, OrderNplLink orderNplLink) throws TclCommonException {
		AddressDetail adDetailB = null;
		String crossConnect = CommonConstants.NO;
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderIllSiteB.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrderType())) {
			List<OrderIllSiteToService> oSiteToServiceList = orderIllSiteToServiceRepository.findByOrderNplLink_Id(orderNplLink.getId());
			if(oSiteToServiceList != null && !oSiteToServiceList.isEmpty()) {
			MDMServiceInventoryBean serviceInventoryMDM = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null, oSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId(), null, null);
			ExcelBean siteLocationAddressLine1A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_B_ADDRESS, serviceInventoryMDM.getServiceDetailBeans().get(0).getSiteAddressBEnd());
			siteLocationAddressLine1A.setOrder(2);
			siteLocationAddressLine1A.setSiteAId(orderIllSiteB.getId());
			listBook.add(siteLocationAddressLine1A);
		}
		} 
		else {
			String siteBLocation=null;
			if(orderIllSiteB.getErfLocSiteaLocationId()!=null){
				siteBLocation = returnApiAddressForSites(orderIllSiteB.getErfLocSiteaLocationId());
			}
			else {
				siteBLocation = returnApiAddressForSites(orderIllSiteB.getErfLocSitebLocationId());
			}


		
		if (siteBLocation != null) {
			adDetailB = (AddressDetail) Utils.convertJsonToObject(siteBLocation, AddressDetail.class);
		}

		if (adDetailB != null) {

			ExcelBean siteLocationAddressLine1B = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_B_ADDRESS, adDetailB.getAddressLineOne());
			siteLocationAddressLine1B.setOrder(2);
			siteLocationAddressLine1B.setSiteBId(orderIllSiteB.getId());
			listBook.add(siteLocationAddressLine1B);

			ExcelBean siteLocationAddressLine2B = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_B_ADDRESS, adDetailB.getAddressLineTwo());
			siteLocationAddressLine2B.setOrder(2);
			siteLocationAddressLine2B.setSiteAId(orderIllSiteB.getId());
			listBook.add(siteLocationAddressLine2B);

			ExcelBean siteLocationLocalityB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_B_ADDRESS_LOCALITY, adDetailB.getLocality());
			siteLocationLocalityB.setOrder(2);
			siteLocationLocalityB.setSiteAId(orderIllSiteB.getId());
			listBook.add(siteLocationLocalityB);

			ExcelBean siteLocationCityB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_B_ADDRESS_CITY, adDetailB.getCity());
			siteLocationCityB.setOrder(2);
			siteLocationCityB.setSiteAId(orderIllSiteB.getId());
			listBook.add(siteLocationCityB);

			ExcelBean siteLocationStateB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_B_ADDRESS_STATE, adDetailB.getState());
			siteLocationStateB.setOrder(2);
			siteLocationStateB.setSiteAId(orderIllSiteB.getId());
			listBook.add(siteLocationStateB);

			ExcelBean siteLocationPincodeB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_B_ADDRESS_PIN_ZIPCODE, adDetailB.getPincode());
			siteLocationPincodeB.setOrder(2);
			siteLocationPincodeB.setSiteAId(orderIllSiteB.getId());
			listBook.add(siteLocationPincodeB);

			ExcelBean siteLocationCountryB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_B_ADDRESS_COUNTRY, adDetailB.getCountry());
			siteLocationCountryB.setOrder(2);
			siteLocationCountryB.setSiteAId(orderIllSiteB.getId());
			listBook.add(siteLocationCountryB);

		}
		}

		ExcelBean siteType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
				OrderDetailsExcelDownloadConstants.NPL_SITE_TYPE, orderNplLink.getSiteBType());

		if (Objects.nonNull(orderNplLink.getSiteBType()) && orderNplLink.getSiteBType().equalsIgnoreCase(OrderDetailsExcelDownloadConstants.DC)) {
			crossConnect = CommonConstants.YES;
		}
		ExcelBean siteCrossConnect = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
				OrderDetailsExcelDownloadConstants.CROSS_CONNECT, crossConnect);

		ExcelBean siteBLastMileType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
				OrderDetailsExcelDownloadConstants.LM_TYPE, feasibilityMode);

		siteBLastMileType.setOrder(2);
		siteBLastMileType.setSiteBId(orderIllSiteB.getId());
		siteType.setOrder(2);
		siteType.setSiteBId(orderIllSiteB.getId());
		siteCrossConnect.setOrder(2);
		siteCrossConnect.setSiteBId(orderIllSiteB.getId());
		listBook.add(siteType);
		listBook.add(siteCrossConnect);
		listBook.add(siteBLastMileType);
	}

	/**
	 * createSitePropAndNetworDetailsForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 * @param notFeasible
	 * @throws TclCommonException
	 */
	private void createSiteAPropAndNetworDetailsForExcel(Map<String, Map<String, String>> excelMap,
			List<ExcelBean> listBook, OrderIllSite siteA, Map<String, OrderProductComponent> componentPriceMap,
			Feasible feasible, String feasibilityMode, OrderNplLink orderNplLink, String provider, String interfaceA,
			NotFeasible notFeasible, OrderToLe orderToLe) throws TclCommonException {

		createSiteALocationForExcel(siteA, listBook, feasibilityMode, orderNplLink);
		createSiteAPropertiesForExcel(excelMap, listBook, siteA, orderToLe);
		OrderProductComponent component = componentPriceMap
				.get(OrderDetailsExcelDownloadConstants.LAST_MILE + "Site-A");
		if (Objects.nonNull(component) && component.getType().equalsIgnoreCase("Site-A")) {
			createSiteANetworkComponentForExcel(excelMap, listBook, siteA, orderNplLink.getChargeableDistance(),
					provider, interfaceA);
			createLmPopValueSiteA(listBook, siteA, feasible, notFeasible);
		}
	}

	/**
	 * createSiteBPropAndNetworDetailsForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 * @param notFeasible
	 * @throws TclCommonException
	 */
	private void createSiteBPropAndNetworDetailsForExcel(Map<String, Map<String, String>> excelMap,
			List<ExcelBean> listBook, OrderIllSite siteB, Map<String, OrderProductComponent> componentPriceMap,
			Feasible feasible, String feasibilityMode, OrderNplLink orderNplLink, String provider, String interfaceB,
			NotFeasible notFeasible, OrderToLe orderToLe) throws TclCommonException {

		createSiteBLocationForExcel(siteB, listBook, feasibilityMode, orderNplLink);
		createSiteBPropertiesForExcel(excelMap, listBook, siteB, orderToLe);
		OrderProductComponent component = componentPriceMap
				.get(OrderDetailsExcelDownloadConstants.LAST_MILE + "Site-B");
		if (Objects.nonNull(component) && component.getType().equalsIgnoreCase("Site-B")) {
			createSiteBNetworkComponentForExcel(excelMap, listBook, siteB, orderNplLink.getChargeableDistance(),
					provider, interfaceB);
			createLmPopValueSiteB(listBook, siteB, feasible, notFeasible);
		}

	}

	/**
	 * createDefaultSiteDetails
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createDefaultLinkDetails(List<ExcelBean> listBook, OrderNplLink link,OrderToLe orderToLe
			,OrderIllSite orderIllSiteA,OrderIllSite orderIllSiteB)
			throws TclCommonException, IllegalArgumentException {
		MDMServiceInventoryBean[] serviceInventoryMDM = {null};
		ExcelBean book17 = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.BILLING_TYPE, OrderDetailsExcelDownloadConstants.STANDARD);
		book17.setOrder(2);
		book17.setLinkId(link.getId());
		listBook.add(book17);

		if (!("NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName()))) {
			ExcelBean category = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
					OrderDetailsExcelDownloadConstants.NPLC_FLAVOUR, OrderDetailsExcelDownloadConstants.NORMAL);
			category.setOrder(2);
			category.setLinkId(link.getId());
			listBook.add(category);
		}

		ExcelBean dependentService = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.NPL_COMPONENT, OrderDetailsExcelDownloadConstants.NLD);
		dependentService.setOrder(2);
		dependentService.setLinkId(link.getId());
		listBook.add(dependentService);

		ExcelBean rfsDate = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
				OrderDetailsExcelDownloadConstants.REQUESTED_DATE,
				link.getRequestorDate() != null ? DateUtil.convertDateToSlashString(link.getRequestorDate()) : "NA");
		rfsDate.setOrder(2);
		rfsDate.setLinkId(link.getId());
		listBook.add(rfsDate);

		// createPrimaySecMap(listBook, link,
		// OrderDetailsExcelDownloadConstants.LINK_DETAILS);

		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType()) || MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())
							|| MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())) {
			String orderType = getChangeRequestSummary(orderToLe.getOrder().getQuote().getQuoteToLes().stream()
					.findFirst().get().getChangeRequestSummary());
			if (Objects.isNull(orderType)) {
				orderType = orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get()
						.getQuoteCategory();
			} 
			
			if (MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType()) || MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())){
				orderType = orderToLe.getOrderType();
				
			}
			if (!("NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName()))) {
				ExcelBean macdOrderCategory = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
						OrderDetailsExcelDownloadConstants.OPTIMUS_ORDER_TYPE, orderType);
				macdOrderCategory.setOrder(1);
				macdOrderCategory.setSiteId(0);
				listBook.add(macdOrderCategory);
			}

			String serviceId[] = { null };
			final String[] alias = { null };
			String[] newPortBandwidth = { "0" };

			if ("NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName()) && !MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				QuoteToLe quoteToLe = orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get();
				String attributeValue = "";
				List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository
						.findByNameAndIsActive(OrderDetailsExcelDownloadConstants.SFDC_NDE_ORDER_TYPE, (byte) 1);
				List<QuoteLeAttributeValue> quoteLeAttributeValue = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, mstOmsAttribute.get(0).getName());
				if (quoteLeAttributeValue.size() != 0) {
					attributeValue = quoteLeAttributeValue.get(0).getAttributeValue();
				}
				LOGGER.info("OPTIMUS_ORDER_TYPE FOR LINK value" + attributeValue);
				ExcelBean macdOrderType = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
						OrderDetailsExcelDownloadConstants.OPTIMUS_ORDER_TYPE, attributeValue);
				macdOrderType.setOrder(1);
				macdOrderType.setSiteId(0);
				listBook.add(macdOrderType);

			}

			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
			List<OrderNplLink> orderNplLinkList = getLinks(orderToLe);
			OrderNplLink linkOne = orderNplLinkList.stream().findFirst().get();
			Optional<OrderNplLink> linkOptional = orderNplLinkList.stream().findFirst();

			Map<String, String> serviceIdMap = macdUtils.getServiceIdBasedOnOrderLink(linkOne, orderToLe);
			serviceId[0] = serviceIdMap.get(PDFConstants.LINK);
			
			List<OrderIllSiteToService> orderIllservice=orderIllSiteToServiceRepository.findByOrderNplLink_IdAndOrderToLe(link.getId(), orderToLe);
			LOGGER.info("SERVICE ID against linkid"+orderIllservice.get(0).getErfServiceInventoryTpsServiceId()+"LINKID"+link.getId());
			List<SIServiceDetailDataBean> sIServiceDetailDataBean = null;
			if(!MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType()))
				sIServiceDetailDataBean = macdUtils.getServiceDetailNPL(orderIllservice.get(0).getErfServiceInventoryTpsServiceId());

			Optional<String> productName = quoteToLe.stream().findFirst().get().getQuoteToLeProductFamilies().stream()
					.map(QuoteToLeProductFamily::getMstProductFamily).map(MstProductFamily::getName)
					.filter(product -> product.equalsIgnoreCase(NPL)).findFirst();
			String linkTypeLr = "";

			ExcelBean existingCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
					OrderDetailsExcelDownloadConstants.EXISTING_CIRCUIT_ID, orderIllservice.get(0).getErfServiceInventoryTpsServiceId());
			existingCircuitId.setOrder(1);
			existingCircuitId.setSiteId(0);
			listBook.add(existingCircuitId);

			if (Objects.nonNull(serviceId)) {
				if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
					serviceInventoryMDM[0] = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null, serviceId[0], null, null);
					alias[0] = serviceInventoryMDM[0].getServiceDetailBeans().get(0).getAlias();
				} 
				else {
				List<SIServiceDetailDataBean> serviceDetail = macdUtils.getServiceDetailNPL(orderIllservice.get(0).getErfServiceInventoryTpsServiceId());
				if (Objects.nonNull(serviceDetail)) {
					alias[0] = serviceDetail.get(0).getAlias();

					String[] serviceType = { null };
					serviceType[0] = serviceDetail.get(0).getLinkType();

					if ("Single".equalsIgnoreCase(serviceType[0]) || Objects.isNull(serviceType[0]))
						serviceType[0] = "single";
					MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
							.findByName(OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY);
					List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
							.findByReferenceIdAndMstProductComponent(link.getId(), internetPortmstProductComponent);
					orderProductComponents.stream().forEach(orderProductComponent -> {
						if (orderProductComponent.getType().equalsIgnoreCase(serviceType[0])
								&& (OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY)
										.equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())) {
							orderProductComponent.getOrderProductComponentsAttributeValues().stream()
									.forEach(orderProductComponentsAttributeValue -> {
										if ("Port Bandwidth".equalsIgnoreCase(orderProductComponentsAttributeValue
												.getProductAttributeMaster().getName())) {
											newPortBandwidth[0] = orderProductComponentsAttributeValue
													.getAttributeValues();
										}
									});
						}
					});

					if (Objects.nonNull(alias[0])) {
						ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
								OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, alias[0]);
						serviceAlias.setOrder(1);
						serviceAlias.setSiteId(0);
						listBook.add(serviceAlias);
					} else {
						ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
								OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, "NA");
						serviceAlias.setOrder(1);
						serviceAlias.setSiteId(0);
						listBook.add(serviceAlias);
					}

					if(!MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType()) && !MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())) {
					String parallelRunDays = "";
					String parallelRundays = "";
					String lrLinkType = "";
					Map<String, String> attributes = getAttributesMap(orderToLe);
					parallelRunDays = attributes.get("Parallel Rundays");

					String llBwChangeA = null;
					String llBwChangeB = null;

					try {
						llBwChangeA = Objects.nonNull(getLlBwChangeForSiteA(link, orderIllservice.get(0).getErfServiceInventoryTpsServiceId()))
								? getLlBwChangeForSiteA(link, orderIllservice.get(0).getErfServiceInventoryTpsServiceId())
								: "";
					} catch (TclCommonException e) {
						LOGGER.info("EXCEPTION IN ORDER"+e);
					}
					try {
						llBwChangeB = Objects.nonNull(getLlBwChangeForSiteB(link, orderIllservice.get(0).getErfServiceInventoryTpsServiceId()))
								? getLlBwChangeForSiteB(link, orderIllservice.get(0).getErfServiceInventoryTpsServiceId())
								: "";
					} catch (TclCommonException e) {
						LOGGER.info("EXCEPTION IN ORDER"+e);
					}

					String portBwChange = null;
					try {
						portBwChange = Objects.nonNull(getPortBwChange(link, orderIllservice.get(0).getErfServiceInventoryTpsServiceId()))
								? getPortBwChange(link, orderIllservice.get(0).getErfServiceInventoryTpsServiceId())
								: "";
					} catch (TclCommonException e) {
						LOGGER.info("EXCEPTION IN ORDER"+e);
					}

					if (!"".equalsIgnoreCase(parallelRunDays) && !"0".equalsIgnoreCase(parallelRunDays)) {
						parallelRundays = Objects.nonNull(parallelRunDays) ? parallelRunDays : NONE;
					} else
						parallelRundays = NONE;

					String upgradeOrDowngradeBwChange = isUpgradeOrDowngrade(llBwChangeA, portBwChange);
					String cityType = null;
					try {
						cityType = getCityType(sIServiceDetailDataBean, link);
					} catch (TclCommonException e) {
						LOGGER.info("EXCEPTION IN ORDER"+e);
					}

					lrLinkType = getLinkTypeLR(quoteToLe, linkTypeLr, parallelRundays, upgradeOrDowngradeBwChange,
							cityType);

					parallelRunDays = StringUtils.isBlank(parallelRunDays)
							|| lrLinkType.equalsIgnoreCase("LM Shifting / BSO Change") ? parallelRunDays : "NA";

					if (!"NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName())) {
						LOGGER.info("inside npl link detail");
						ExcelBean linkType = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
								OrderDetailsExcelDownloadConstants.LINK_TYPE_SFDC_ORDER_TYPE, lrLinkType);
						linkType.setOrder(1);
						linkType.setSiteId(0);
						listBook.add(linkType);

						ExcelBean parallelLink = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
								OrderDetailsExcelDownloadConstants.PARALLEL_LINK, parallelRunDays);
						parallelLink.setOrder(1);
						parallelLink.setSiteId(0);
						listBook.add(parallelLink);

					}

					// added for nde macd and mc
					if ("NDE".equalsIgnoreCase(orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName())) {
						LOGGER.info("inside nde link detail");
						String attributeValue = "";
						List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository.findByNameAndIsActive(
								OrderDetailsExcelDownloadConstants.SFDC_NDE_ORDER_TYPE, (byte) 1);
						List<QuoteLeAttributeValue> quoteLeAttributeValue = quoteLeAttributeValueRepository
								.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe.get(0),
										mstOmsAttribute.get(0).getName());
						if (quoteLeAttributeValue.size() != 0) {
							attributeValue = quoteLeAttributeValue.get(0).getAttributeValue();
						}
						LOGGER.info("LINK_TYPE_SFDC_ORDER_TYPE FOR LINK value" + attributeValue);
						ExcelBean linkType = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
								OrderDetailsExcelDownloadConstants.LINK_TYPE_SFDC_ORDER_TYPE, attributeValue);
						linkType.setOrder(1);
						linkType.setSiteId(0);
						listBook.add(linkType);

						// added for nde mc
						List<OrderProductComponent> components = orderProductComponentRepository
								.findByReferenceIdAndType(link.getId(), CommonConstants.LINK);
						final String[] parraleldays = { "" };
						for (OrderProductComponent opComponenet : components) {
							if (opComponenet.getMstProductComponent().getName().equals(MACDConstants.NPL_COMMON)) {
								LOGGER.info("setting Parallel build and Parallel Run days for NDE - MACD");
								opComponenet.getOrderProductComponentsAttributeValues()
										.forEach(quoteProductComponentsAttributeValue -> {
											if (quoteProductComponentsAttributeValue.getProductAttributeMaster()
													.getName().equals(MACDConstants.PARALLEL_RUN_DAYS.toString())) {
												LOGGER.info("PARALLEL RUN DAYS"
														+ quoteProductComponentsAttributeValue.getAttributeValues());
												parraleldays[0] = quoteProductComponentsAttributeValue
														.getAttributeValues();

											}
										});
							}
						}

						LOGGER.info("setting Parallel Run days for NDE - MACD" + parraleldays[0]);
						
							ExcelBean parallelLink = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
									OrderDetailsExcelDownloadConstants.PARALLEL_LINK, parraleldays[0]);
							parallelLink.setOrder(1);
							parallelLink.setSiteId(0);
							listBook.add(parallelLink);
						

					}

					if (orderIllSiteA.getNplShiftSiteFlag().equals(1)) {
						String siteShifted = "Site A Shifted";
						ExcelBean siteShift = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
								OrderDetailsExcelDownloadConstants.SITE_SHIFTED, siteShifted);
						siteShift.setOrder(1);
						siteShift.setSiteId(0);
						listBook.add(siteShift);
					} else if (orderIllSiteB.getNplShiftSiteFlag().equals(1)) {
						String siteShifted = "Site B Shifted";
						ExcelBean siteShift = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
								OrderDetailsExcelDownloadConstants.SITE_SHIFTED, siteShifted);
						siteShift.setOrder(1);
						siteShift.setSiteId(0);
						listBook.add(siteShift);
					} else {
						String siteShifted = "NA";
						ExcelBean siteShift = new ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
								OrderDetailsExcelDownloadConstants.SITE_SHIFTED, siteShifted);
						siteShift.setOrder(1);
						siteShift.setSiteId(0);
						listBook.add(siteShift);
					}
				}
				}
				
				}
				
				if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
					List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderNplLink_Id(link.getId());
					Double cancellationCharge = 0D;
					if(!orderIllSiteToServiceList.isEmpty()) {
					LOGGER.info("Extract cancellation charges");
					if(MACDConstants.ABSORBED.equalsIgnoreCase(orderIllSiteToServiceList.get(0).getAbsorbedOrPassedOn())) {
						cancellationCharge = 0D;
					} else {
						cancellationCharge = link.getNrc();
					}
					ExcelBean cancellationCharges = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
							OrderDetailsExcelDownloadConstants.CANCELLATION_CHARGES, String.valueOf(cancellationCharge));
					cancellationCharges.setOrder(2);
					cancellationCharges.setSiteId(0);
					if (!listBook.contains(cancellationCharges)) {
						listBook.add(cancellationCharges);
					}
					
					
					LOGGER.info("Extract cancellation leadToRFS date");
					ExcelBean leadToRFSDate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
							OrderDetailsExcelDownloadConstants.LEAD_TO_RFS_DATE, String.valueOf(orderIllSiteToServiceList.get(0).getLeadToRFSDate()));
					leadToRFSDate.setOrder(2);
					leadToRFSDate.setSiteId(0);
					if (!listBook.contains(leadToRFSDate)) {
						listBook.add(leadToRFSDate);
					}
					
					

					
					LOGGER.info("Extract effective date of change");
					ExcelBean effectiveDateOfChange = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
							OrderDetailsExcelDownloadConstants.EFFECTIVE_DATE_OF_CHANGE, String.valueOf(orderIllSiteToServiceList.get(0).getEffectiveDateOfChange()));
					effectiveDateOfChange.setOrder(2);
					effectiveDateOfChange.setSiteId(0);
					if (!listBook.contains(effectiveDateOfChange)) {
						listBook.add(effectiveDateOfChange);
					}
					}
					
				}
			}
		} else {
			//as per duplicate removed visible in order details
			/*
			 * ExcelBean orderType = new
			 * ExcelBean(OrderDetailsExcelDownloadConstants.LINK_DETAILS,
			 * OrderDetailsExcelDownloadConstants.ORDER_TYPE, "NEW"); orderType.setOrder(1);
			 * orderType.setLinkId(0); listBook.add(orderType);
			 */
		}
	}
	/**
	 * getAccessType - method to get access type based on input
	 */
	public String getAccessType(String mode) {
		if (mode != null && mode.equals(NplPDFConstants.ONNETWL_NPL)) {
			return NplPDFConstants.ONNET_WIRED;
		}
		
		return mode;

	}

	/**
	 * createSlaDetails - Method to create SLA details for a link in LR Excel
	 * 
	 * @param link
	 * @param listBook
	 * @throws TclCommonException
	 */
	public void createSlaDetails(OrderNplLink link, List<ExcelBean> listBook) throws TclCommonException {
		if (Objects.isNull(link) || Objects.isNull(listBook)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		List<OrderNplLinkSla> linkSla = orderNplLinkSlaRepository.findByOrderNplLink(link);

		for (OrderNplLinkSla orderNplLinkSla : linkSla) {
			if (orderNplLinkSla.getSlaMaster().getSlaName()
					.contains(OrderDetailsExcelDownloadConstants.SERVICE_AVAILABILITY)) {
				ExcelBean ntwUpTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
						OrderDetailsExcelDownloadConstants.SERVICE_AVAILABILITY, orderNplLinkSla.getSlaValue());
				ntwUpTime.setOrder(3);
				ntwUpTime.setLinkId(link.getId());
				listBook.add(ntwUpTime);

			}
		}

	}

	/**
	 * createNetworkComponentDefaultValue
	 * 
	 * @param listBook
	 * @param notFeasible
	 * @param site
	 */
	private void createLmPopValueSiteA(List<ExcelBean> listBook, OrderIllSite siteA, Feasible feasible,
			NotFeasible notFeasible) {

		String popLmDetails = null;
		try {
			if (siteA.getErfLocSiteaSiteCode() != null) {
				String locationResponse = (String) mqUtils.sendAndReceive(popQueue, siteA.getErfLocSiteaSiteCode());
				if (StringUtils.isNotBlank(locationResponse)) {
					LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
							LocationDetail.class);
					if (locationDetails.getApiAddress() != null) {
						popLmDetails = locationDetails.getAddress() + "" + locationDetails.getPopId() + ""
								+ locationDetails.getTier();

					} else if (locationDetails.getUserAddress() != null) {
						popLmDetails = locationDetails.getUserAddress() + "" + locationDetails.getPopId() + ""
								+ locationDetails.getTier();

					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("pop lm details errot", e);
		}
		if (popLmDetails != null && feasible != null) {

			popLmDetails = siteA.getErfLocSiteaSiteCode() + "," + feasible != null ? feasible.getAPopAddress() : "NA";
		}
		if (popLmDetails == null && feasible != null) {
			popLmDetails = feasible.getAPopNetworkLocId();
		}

		if (notFeasible != null) {
			popLmDetails = notFeasible.getPopAddress() != null ? notFeasible.getPopAddress() : "";

		}

		ExcelBean book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_A,
				OrderDetailsExcelDownloadConstants.LM_POP_DETAILS, popLmDetails);
		book28.setOrder(3);
		book28.setSiteAId(siteA.getId());
		listBook.add(book28);

	}

	private void createLmPopValueSiteB(List<ExcelBean> listBook, OrderIllSite siteB, Feasible feasible,
			NotFeasible notFeasible) {

		String popLmDetails = null;
		try {
			if (siteB.getErfLocSiteaSiteCode() != null) {
				String locationResponse = (String) mqUtils.sendAndReceive(popQueue, siteB.getErfLocSiteaSiteCode());
				if (StringUtils.isNotBlank(locationResponse)) {
					LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
							LocationDetail.class);
					if (locationDetails.getApiAddress() != null) {
						popLmDetails = locationDetails.getAddress() + "" + locationDetails.getPopId() + ""
								+ locationDetails.getTier();

					} else if (locationDetails.getUserAddress() != null) {
						popLmDetails = locationDetails.getUserAddress() + "" + locationDetails.getPopId() + ""
								+ locationDetails.getTier();

					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("pop lm details errot", e);
		}
		if (popLmDetails != null && feasible != null) {
			popLmDetails = siteB.getErfLocSiteaSiteCode() + "," + feasible != null ? feasible.getBPopAddress() : "NA";
		}
		if (popLmDetails == null && feasible != null) {
			popLmDetails = feasible.getBPopNetworkLocId();
		}
		if (notFeasible != null) {
			popLmDetails = notFeasible.getPopAddress();
		}

		ExcelBean book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_B,
				OrderDetailsExcelDownloadConstants.LM_POP_DETAILS, popLmDetails);
		book28.setOrder(3);
		book28.setSiteBId(siteB.getId());
		listBook.add(book28);

	}

	/**
	 * createBillingComponentPrice
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createBillingComponentPrice(List<ExcelBean> listBook, OrderNplLink link) {

		MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
				.findByName(OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY);
		extractComponentPrices(link, internetPortmstProductComponent, listBook,
				OrderDetailsExcelDownloadConstants.CONNECTIVITY_ARC_NRC);

		MstProductComponent lastMilePortmstProductComponent = mstProductComponentRepository
				.findByName(FPConstants.LAST_MILE.toString());
		extractComponentPrices(link, lastMilePortmstProductComponent, listBook,
				OrderDetailsExcelDownloadConstants.LASTMILE_CHARGES_ARC_NRC);

		MstProductComponent linkMgmtmstProductComponent = mstProductComponentRepository
				.findByName(FPConstants.LINK_MANAGEMENT_CHARGES.toString());
		extractComponentPrices(link, linkMgmtmstProductComponent, listBook,
				OrderDetailsExcelDownloadConstants.LINK_MGMT_CHARGES_ARC_NRC);

	}

	/**
	 * createBillingDetails
	 * 
	 * @param listBook
	 * @param attributeValues
	 * @param orderToLe
	 */
	private void createBillingDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
			OrderToLe orderToLe) {
		ExcelBean billingMethod = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.BILLING_METHOD,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_METHOD)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_METHOD)
						: "");
		billingMethod.setOrder(4);
		billingMethod.setLinkId(0);
		listBook.add(billingMethod);

		ExcelBean billingFrequency = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY)
						: "");
		billingFrequency.setOrder(4);
		billingFrequency.setSiteId(0);
		listBook.add(billingFrequency);

		BillingContact billingContact = getBillingDetails(
				attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_CONTACT_ID));

		String billingFName = Objects.nonNull(billingContact.getFname()) ? billingContact.getFname() : "";
		String billingLName = Objects.nonNull(billingContact.getLname()) ? billingContact.getLname() : "";
		String billingEmail = Objects.nonNull(billingContact.getEmailId()) ? billingContact.getEmailId() : "";
		String billingAddress = Objects.nonNull(billingContact.getBillAddr()) ? billingContact.getBillAddr() : "";

		ExcelBean contactName = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.SEND_INVOICE_TO_NAME, billingFName.concat(" ").concat(billingLName));
		contactName.setOrder(4);
		contactName.setSiteId(0);
		listBook.add(contactName);

		ExcelBean contactNumber = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.SEND_INVOICE_TO_CONTACT, billingEmail);
		contactNumber.setOrder(4);
		contactNumber.setSiteId(0);
		listBook.add(contactNumber);

		ExcelBean billingAddr = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.SEND_INVOICE_TO_ADDRESS, billingAddress);

		billingAddr.setOrder(4);
		billingAddr.setSiteId(0);
		listBook.add(billingAddr);

	}

	// TODO: queue call to customer MDM

	private BillingContact getBillingDetails(String billingId) {
		BillingContact billingContacts = new BillingContact();
		try {
			String response = (String) mqUtils.sendAndReceive(customerBillingContactInfo, billingId);
			billingContacts = (BillingContact) Utils.convertJsonToObject(response, BillingContact.class);
		} catch (TclCommonException | IllegalArgumentException e) {
			LOGGER.error("Error in getting billing ",e);
		}

		return billingContacts;

	}

	/**
	 * extractComponentPrices
	 * 
	 * @param site
	 * @param mstProductComponent
	 */
	private void extractComponentPrices(OrderNplLink link, MstProductComponent mstProductComponent,
			List<ExcelBean> listBook, String attrName) {
		List<Integer> refIds = new ArrayList();

		if (mstProductComponent.getName().equals(FPConstants.LAST_MILE.toString())) {
			refIds.add(link.getSiteAId());
			refIds.add(link.getSiteBId());
		} else
			refIds.add(link.getId());

		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdInAndMstProductComponent(refIds, mstProductComponent);

		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {

			orderProductComponents.forEach(comp -> {

				OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(String.valueOf(comp.getId()),
						OrderDetailsExcelDownloadConstants.COMPONENTS);

				if (price != null) {
					ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, attrName,
							BigDecimal.valueOf(price.getEffectiveArc()).toPlainString() + ","
									+ BigDecimal.valueOf(price.getEffectiveNrc()).toPlainString());
					book36.setOrder(4);
					book36.setLinkId(link.getId());
					listBook.add(book36);
				}
			});

		}
	}

	/**
	 * getProductFamily
	 * 
	 * @param name
	 * @param orderToLe
	 * @return
	 */
	private String getProductFamily(String name, OrderToLe orderToLe) {
		for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
			if (family.getMstProductFamily().getName().equals(name)) {
				return family.getMstProductFamily().getName();
			}
		}

		return null;
	}

	/**
	 * createSitePropertiesForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 */
	private void createSiteAPropertiesForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
			OrderIllSite siteA, OrderToLe orderToLe) {

		Map<String, String> siteMap = excelMap.get("SITE_PROPERTIESSite-A");
		if (siteMap == null) {
			siteMap = new HashMap<>();
		}

		if (siteA.getIsTaxExempted() == CommonConstants.BACTIVE) {
			ExcelBean taxExcemption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.TAX_EXEMPTION,
					siteMap.containsKey("TAX_EXCEMPTED_REASON") ? siteMap.get("TAX_EXCEMPTED_REASON") : "");
			taxExcemption.setSiteAId(siteA.getId());
			taxExcemption.setOrder(2);
			listBook.add(taxExcemption);
		}

		String contactDetails = "";
		LocationItContact locationItContact = new LocationItContact();
		try {
			if (orderToLe.getOrder().getOrderCode().startsWith("NDE") && MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())) {
				// setting local it attributes for Termination - NDE specific order
				List<OrderIllSiteToService> orderIllSiteToServices= orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
				if (!orderIllSiteToServices.isEmpty()) {
					OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetails = orderSiteServiceTerminationDetailsRepository.findByOrderIllSiteToService(orderIllSiteToServices.stream().findFirst().get());
					locationItContact.setName(orderSiteServiceTerminationDetails.getLocalItContactName());
					locationItContact.setEmail(orderSiteServiceTerminationDetails.getLocalItContactEmailId());
					locationItContact.setContactNo(orderSiteServiceTerminationDetails.getLocalItContactNumber());
				}


			} else {
				if (siteMap.containsKey(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)) {
					String respone = returnLocationItContactName(Integer
							.valueOf(siteMap.get(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)));
					if (respone != null) {
						locationItContact = (LocationItContact) Utils.convertJsonToObject(respone, LocationItContact.class);
						contactDetails = locationItContact.getName() + "," + locationItContact.getEmail() + ","
								+ locationItContact.getContactNo();
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting local It Contact info", e);
		}

		if (locationItContact != null) {
			ExcelBean localItContactName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_NAME, locationItContact.getName());
			localItContactName.setSiteId(siteA.getId());
			localItContactName.setOrder(2);
			listBook.add(localItContactName);

			ExcelBean localItContactEmail = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_EMAIL, locationItContact.getEmail());
			localItContactEmail.setSiteId(siteA.getId());
			localItContactEmail.setOrder(2);
			listBook.add(localItContactEmail);

			ExcelBean localItContactPhone = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_NUMBER, locationItContact.getContactNo());
			localItContactPhone.setSiteId(siteA.getId());
			localItContactPhone.setOrder(2);
			listBook.add(localItContactPhone);
		}

		LeStateInfo gstDetails = new LeStateInfo();
		if (siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
				&& !StringUtils.isBlank(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO))) {

			gstDetails = getGstDetails(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO),
					orderToLe.getErfCusCustomerLegalEntityId());

		}

		ExcelBean siteGst = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
				OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO));
		siteGst.setSiteAId(siteA.getId());
		siteGst.setOrder(2);
		listBook.add(siteGst);

		if (gstDetails != null) {
			ExcelBean siteGstAddr = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getAddress() : "NA");
			siteGstAddr.setSiteId(siteA.getId());
			siteGstAddr.setOrder(2);
			listBook.add(siteGstAddr);

			ExcelBean siteGstAddrLine1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getAddresslineOne()
							: "NA");
			siteGstAddrLine1.setSiteId(siteA.getId());
			siteGstAddrLine1.setOrder(2);
			listBook.add(siteGstAddrLine1);

			ExcelBean siteGstAddrLine2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getAddresslineTwo()
							: "NA");
			siteGstAddrLine2.setSiteId(siteA.getId());
			siteGstAddrLine2.setOrder(2);
			listBook.add(siteGstAddrLine2);

			ExcelBean siteGstCity = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getCity() : "NA");
			siteGstCity.setSiteId(siteA.getId());
			siteGstCity.setOrder(2);
			listBook.add(siteGstCity);

			ExcelBean siteGstState = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getState() : "NA");
			siteGstState.setSiteId(siteA.getId());
			siteGstState.setOrder(2);
			listBook.add(siteGstState);

			ExcelBean siteGstCountry = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getCountry() : "NA");
			siteGstCountry.setSiteId(siteA.getId());
			siteGstCountry.setOrder(2);
			listBook.add(siteGstCountry);

			ExcelBean siteGstPincode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getPincode() : "NA");
			siteGstPincode.setSiteId(siteA.getId());
			siteGstPincode.setOrder(2);
			listBook.add(siteGstPincode);
		}

		if (locationItContact != null) {
			ExcelBean localItContactNameA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getName());
			localItContactNameA.setSiteId(siteA.getId());
			localItContactNameA.setOrder(2);
			listBook.add(localItContactNameA);

			ExcelBean localItContactEmailA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getEmail());
			localItContactEmailA.setSiteId(siteA.getId());
			localItContactEmailA.setOrder(2);
			listBook.add(localItContactEmailA);

			ExcelBean localItContactPhoneA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getContactNo());
			localItContactPhoneA.setSiteId(siteA.getId());
			localItContactPhoneA.setOrder(2);
			listBook.add(localItContactPhoneA);
		}

		ExcelBean isNocID = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
				OrderDetailsExcelDownloadConstants.IS_NOC_ID, "No");
		isNocID.setSiteId(siteA.getId());
		isNocID.setOrder(2);
		listBook.add(isNocID);

		if (!orderToLe.getOrder().getOrderCode().startsWith("NDE")) {
			ExcelBean nplType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.NPL_Type, OrderDetailsExcelDownloadConstants.STANDARD);
			nplType.setOrder(2);
			nplType.setSiteAId(siteA.getId());
			if (!listBook.contains(nplType)) {
				listBook.add(nplType);

			}
		}
		
		
		
		

	}

	/**
	 * createSitePropertiesForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 */
	private void createSiteBPropertiesForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
			OrderIllSite siteB, OrderToLe orderToLe) {

		Map<String, String> siteMap = excelMap.get("SITE_PROPERTIESSite-B");
		if (siteMap == null) {
			siteMap = new HashMap<>();
		}

		if (siteB.getIsTaxExempted() == CommonConstants.BACTIVE) {
			ExcelBean taxExcemption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.TAX_EXEMPTION,
					siteMap.containsKey("TAX_EXCEMPTED_REASON") ? siteMap.get("TAX_EXCEMPTED_REASON") : "");
			taxExcemption.setSiteBId(siteB.getId());
			taxExcemption.setOrder(2);
			listBook.add(taxExcemption);
		}

		String contactDetails = "";
		LocationItContact locationItContact = new LocationItContact();
		try {

			if (orderToLe.getOrder().getOrderCode().startsWith("NDE") && MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderType())) {
				// setting local it attributes for Termination - NDE specific order
				List<OrderIllSiteToService> orderIllSiteToServices= orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
				if (!orderIllSiteToServices.isEmpty()) {
					OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetails = orderSiteServiceTerminationDetailsRepository.findByOrderIllSiteToService(orderIllSiteToServices.stream().findFirst().get());
					locationItContact.setName(orderSiteServiceTerminationDetails.getLocalItContactName());
					locationItContact.setEmail(orderSiteServiceTerminationDetails.getLocalItContactEmailId());
					locationItContact.setContactNo(orderSiteServiceTerminationDetails.getLocalItContactNumber());
				}


			} else {
				if (siteMap.containsKey(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)) {
					String respone = returnLocationItContactName(Integer
							.valueOf(siteMap.get(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)));
					if (respone != null) {
						locationItContact = (LocationItContact) Utils.convertJsonToObject(respone, LocationItContact.class);
//					contactDetails = locationItContact.getName() + "," + locationItContact.getEmail() + ","
//							+ locationItContact.getContactNo();
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting local It Contact info", e);
		}

		if (locationItContact != null) {
			ExcelBean localItContactNameB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_NAME, locationItContact.getName());
			localItContactNameB.setSiteId(siteB.getId());
			localItContactNameB.setOrder(2);
			listBook.add(localItContactNameB);

			ExcelBean localItContactEmailB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_EMAIL, locationItContact.getEmail());
			localItContactEmailB.setSiteId(siteB.getId());
			localItContactEmailB.setOrder(2);
			listBook.add(localItContactEmailB);

			ExcelBean localItContactPhoneB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_NUMBER, locationItContact.getContactNo());
			localItContactPhoneB.setSiteId(siteB.getId());
			localItContactPhoneB.setOrder(2);
			listBook.add(localItContactPhoneB);
		}

		LeStateInfo gstDetails = new LeStateInfo();
		if (siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
				&& !StringUtils.isBlank(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO))) {

			gstDetails = getGstDetails(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO),
					orderToLe.getErfCusCustomerLegalEntityId());

		}

		ExcelBean siteGst = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
				OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO));
		siteGst.setSiteAId(siteB.getId());
		siteGst.setOrder(2);
		listBook.add(siteGst);

		if (gstDetails != null) {
			ExcelBean siteGstAddr = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getAddress() : "NA");
			siteGstAddr.setSiteId(siteB.getId());
			siteGstAddr.setOrder(2);
			listBook.add(siteGstAddr);

			ExcelBean siteGstAddrLine1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getAddresslineOne()
							: "NA");
			siteGstAddrLine1.setSiteId(siteB.getId());
			siteGstAddrLine1.setOrder(2);
			listBook.add(siteGstAddrLine1);

			ExcelBean siteGstAddrLine2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getAddresslineTwo()
							: "NA");
			siteGstAddrLine2.setSiteId(siteB.getId());
			siteGstAddrLine2.setOrder(2);
			listBook.add(siteGstAddrLine2);

			ExcelBean siteGstCity = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getCity() : "NA");
			siteGstCity.setSiteId(siteB.getId());
			siteGstCity.setOrder(2);
			listBook.add(siteGstCity);

			ExcelBean siteGstState = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getState() : "NA");
			siteGstState.setSiteId(siteB.getId());
			siteGstState.setOrder(2);
			listBook.add(siteGstState);

			ExcelBean siteGstCountry = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getCountry() : "NA");
			siteGstCountry.setSiteId(siteB.getId());
			siteGstCountry.setOrder(2);
			listBook.add(siteGstCountry);

			ExcelBean siteGstPincode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.SITE_GST,
					siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) ? gstDetails.getPincode() : "NA");
			siteGstPincode.setSiteId(siteB.getId());
			siteGstPincode.setOrder(2);
			listBook.add(siteGstPincode);
		}

		if (locationItContact != null) {
			ExcelBean localItContactName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getName());
			localItContactName.setSiteId(siteB.getId());
			localItContactName.setOrder(2);
			listBook.add(localItContactName);

			ExcelBean localItContactEmail = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getEmail());
			localItContactEmail.setSiteId(siteB.getId());
			localItContactEmail.setOrder(2);
			listBook.add(localItContactEmail);

			ExcelBean localItContactPhone = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getContactNo());
			localItContactPhone.setSiteId(siteB.getId());
			localItContactPhone.setOrder(2);
			listBook.add(localItContactPhone);
		}

		ExcelBean isNocID = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
				OrderDetailsExcelDownloadConstants.IS_NOC_ID, "No");
		isNocID.setSiteId(siteB.getId());
		isNocID.setOrder(2);
		listBook.add(isNocID);

		if (!orderToLe.getOrder().getOrderCode().startsWith("NDE")) {
			ExcelBean nplType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_B,
					OrderDetailsExcelDownloadConstants.NPL_Type, OrderDetailsExcelDownloadConstants.STANDARD);

			nplType.setSiteBId(siteB.getId());
			nplType.setOrder(2);
			listBook.add(nplType);
		}
		
	

	}

	/**
	 * returnApiAddressForSites
	 * 
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */
	private String returnApiAddressForSites(Integer sites) throws TclCommonException {
		try {
			return (String) mqUtils.sendAndReceive(apiAddressQueue, String.valueOf(sites));
		} catch (Exception e) {
			LOGGER.error("error in process location", e);
		}
		return null;
	}

	/**
	 * createSiteANetworkComponentForExcel - creates excel bean for site A network
	 * component
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 */
	private void createSiteANetworkComponentForExcel(Map<String, Map<String, String>> excelMap,
			List<ExcelBean> listBook, OrderIllSite siteA, String chargeableDistance, String provider,
			String interfaceA) {

		Map<String, String> lastMileMap = excelMap
				.get(OrderDetailsExcelDownloadConstants.LAST_MILE + CommonConstants.SITEA);
		createSiteALastMileComponentForExcel(lastMileMap, listBook, siteA, chargeableDistance, provider, interfaceA);
	}

	/**
	 * createSiteBNetworkComponentForExcel - creates excel bean for site B network
	 * component
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 */
	private void createSiteBNetworkComponentForExcel(Map<String, Map<String, String>> excelMap,
			List<ExcelBean> listBook, OrderIllSite site, String chargeableDistance, String provider,
			String interfaceB) {

		Map<String, String> latsMileMap = excelMap
				.get(OrderDetailsExcelDownloadConstants.LAST_MILE + CommonConstants.SITEB);
		createSiteBLastMileComponentForExcel(latsMileMap, listBook, site, chargeableDistance, provider, interfaceB);
	}

	/**
	 * createLinkNetworkComponentForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param link
	 * @throws TclCommonException
	 */
	private void createLinkNetworkComponentForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
			OrderNplLink link, Map<String, String> interfaceList) throws TclCommonException {
		Map<String, String> nationalConnectivityMap = excelMap
				.get(OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY);
		Map<String, String> privateLinesMap = excelMap.get(OrderDetailsExcelDownloadConstants.PRIVATE_LINES);
		createNationalConnectivityforExcel(nationalConnectivityMap, listBook, link);
		createPrivateLinesforExcel(privateLinesMap, listBook, link);
		if (nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_A_END))
			interfaceList.put(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_A_END,
					nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_A_END));
		if (nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_B_END))
			interfaceList.put(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_B_END,
					nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_B_END));

	}

	/**
	 * returnLocationItContactName - retrieves local IT contact details
	 * 
	 * @param id
	 * @return
	 * @throws TclCommonException
	 */
	public String returnLocationItContactName(Integer id) throws TclCommonException {
		try {
			if (Objects.isNull(id)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			return (String) mqUtils.sendAndReceive(locationItContactQueue, String.valueOf(id));
		} catch (Exception e) {
			LOGGER.error("error in process location", e);
		}
		return null;
	}

	/**
	 * createNationalConnectivityforExcel - creates excel bean for National
	 * connectivity component
	 * 
	 * @param nationalConnectivityMap
	 * @param listBook
	 * @param link
	 * @throws TclCommonException
	 */
	public void createNationalConnectivityforExcel(Map<String, String> nationalConnectivityMap,
			List<ExcelBean> listBook, OrderNplLink link) throws TclCommonException {
		LOGGER.info("INSIDE createNationalConnectivityforExcel "+link.getId()+"order id"+link.getOrderId());
		Optional<Order> order=orderRepository.findById(link.getOrderId());
		
		if (Objects.isNull(listBook) || Objects.isNull(link)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		if (nationalConnectivityMap == null) {
			nationalConnectivityMap = new HashMap<>();
		}

		ExcelBean portBandwidth = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
				OrderDetailsExcelDownloadConstants.NLD_BANDWIDTH,
				nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.PORT_BANDWIDTH)
						? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.PORT_BANDWIDTH)
						: "No");
		portBandwidth.setOrder(3);
		portBandwidth.setLinkId(link.getId());
		if (!listBook.contains(portBandwidth)) {
			listBook.add(portBandwidth);
		}

		if (!order.get().getOrderCode().startsWith("NDE")) {
			ExcelBean npliType = new ExcelBean(
					OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
					OrderDetailsExcelDownloadConstants.NPLI_TYPE,
					nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.SUB_PRODUCT)
							? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.SUB_PRODUCT)
							: "NA");
			npliType.setOrder(3);
			npliType.setLinkId(link.getId());
			if (!listBook.contains(npliType)) {
				listBook.add(npliType);
			}
		}

		ExcelBean connectorTypeA = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
				OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE_A_END,
				nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE_A_END)
						? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE_A_END)
						: "No");
		connectorTypeA.setOrder(3);
		connectorTypeA.setLinkId(link.getId());
		if (!listBook.contains(connectorTypeA)) {
			listBook.add(connectorTypeA);
		}

		ExcelBean connectorTypeB = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
				OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE_B_END,
				nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE_B_END)
						? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE_B_END)
						: "No");
		connectorTypeB.setOrder(3);
		connectorTypeB.setLinkId(link.getId());
		if (!listBook.contains(connectorTypeB)) {
			listBook.add(connectorTypeB);
		}

		ExcelBean interfaceTypeA = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
				OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_A_END,
				nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_A_END)
						? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_A_END)
						: "No");
		interfaceTypeA.setOrder(3);
		interfaceTypeA.setLinkId(link.getId());
		listBook.add(interfaceTypeA);

		ExcelBean interfaceTypeB = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
				OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_B_END,
				nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_B_END)
						? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_B_END)
						: "No");
		interfaceTypeB.setOrder(3);
		interfaceTypeB.setLinkId(link.getId());
		listBook.add(interfaceTypeB);

		ExcelBean networkProtection = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
				OrderDetailsExcelDownloadConstants.NETWORK_PROTECTION,
				nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.NETWORK_PROTECTION)
						? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.NETWORK_PROTECTION)
						: "No");
		networkProtection.setOrder(3);
		networkProtection.setLinkId(link.getId());
		if (!listBook.contains(networkProtection)) {
			listBook.add(networkProtection);
		}
		
		//needs to add nde attributes
		
		LOGGER.info("orderCode**************"+order.get().getOrderCode());
		if(order.isPresent()) {
			if (order.get().getOrderCode().startsWith("NDE") && !MACDConstants.CANCELLATION.equalsIgnoreCase(order.get().getOrderToLes().stream().findFirst().get().getOrderType())) {
				LOGGER.info("inside nde**************" + link.getLinkType() + " " + nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.HUP_PARANTED)
						+ " " + nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.HUP_PARANTED_ID));

				ExcelBean circuitType = new ExcelBean(
						OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
						OrderDetailsExcelDownloadConstants.CIRCUIT_TYPE, link.getLinkType());
				interfaceTypeB.setOrder(3);
				interfaceTypeB.setLinkId(link.getId());
				listBook.add(circuitType);

				if (nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.HUP_PARANTED) != null && nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.HUP_PARANTED_ID) !=null) {
					if (!nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.HUP_PARANTED).isEmpty()) {
						if (nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.HUP_PARANTED)
								.equalsIgnoreCase("Yes")) {

							ExcelBean ethernetFlavour = new ExcelBean(
									OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
									OrderDetailsExcelDownloadConstants.ETHERNET_FLAVOUR,
									OrderDetailsExcelDownloadConstants.HUB_NDE);
							interfaceTypeB.setOrder(3);
							interfaceTypeB.setLinkId(link.getId());
							listBook.add(ethernetFlavour);
						} else {
							ExcelBean ethernetFlavour = new ExcelBean(
									OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
									OrderDetailsExcelDownloadConstants.ETHERNET_FLAVOUR,
									OrderDetailsExcelDownloadConstants.NORMAL);
							interfaceTypeB.setOrder(3);
							interfaceTypeB.setLinkId(link.getId());
							listBook.add(ethernetFlavour);
						}

					}
				}


				ExcelBean hubId = new ExcelBean(
						OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
						OrderDetailsExcelDownloadConstants.HUB_ID,
						nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.HUP_PARANTED_ID)
								? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.HUP_PARANTED_ID)
								: "NA");
				interfaceTypeB.setOrder(3);
				interfaceTypeB.setLinkId(link.getId());
				listBook.add(hubId);

				ExcelBean vlanId = new ExcelBean(
						OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_NATIONAL_CONNECTIVITY_LINK,
						OrderDetailsExcelDownloadConstants.VLAN_ID,
						nationalConnectivityMap.containsKey(OrderDetailsExcelDownloadConstants.VLAN_ID)
								? nationalConnectivityMap.get(OrderDetailsExcelDownloadConstants.VLAN_ID)
								: "NA");
				interfaceTypeB.setOrder(3);
				interfaceTypeB.setLinkId(link.getId());
				listBook.add(vlanId);

			}
		}
	}

	/**
	 * createPrivateLinesforExcel
	 * 
	 * @param privateLinesMap
	 * @param listBook
	 * @param link
	 */
	private void createPrivateLinesforExcel(Map<String, String> privateLinesMap, List<ExcelBean> listBook,
			OrderNplLink link) {
		if (privateLinesMap == null) {
			privateLinesMap = new HashMap<>();
		}
		ExcelBean chargeType = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PRIVATE_LINES_LINK,
				OrderDetailsExcelDownloadConstants.CHARGE_TYPE,
				privateLinesMap.containsKey(OrderDetailsExcelDownloadConstants.CHARGE_TYPE)
						? privateLinesMap.get(OrderDetailsExcelDownloadConstants.CHARGE_TYPE)
						: "No");
		chargeType.setOrder(3);
		chargeType.setLinkId(link.getId());
		if (!listBook.contains(chargeType)) {
			listBook.add(chargeType);
		}

	}

	/**
	 * /** createLastMileComponentForExcel
	 * 
	 * @param latsMileMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 */
	private void createSiteBLastMileComponentForExcel(Map<String, String> latsMileMap, List<ExcelBean> listBook,
			OrderIllSite siteB, String chargeableDistance, String provider, String interfaceB) {
		if (latsMileMap == null) {
			latsMileMap = new HashMap<>();
		}
		String localLoopBandWidthValue;
		if(latsMileMap.containsKey("Local Loop Bandwidth")) {
			if(latsMileMap.get("Local Loop Bandwidth").contains("Mbps")) {
				  localLoopBandWidthValue = latsMileMap.get("Local Loop Bandwidth");
			}else {
				 localLoopBandWidthValue = latsMileMap.get("Local Loop Bandwidth").concat("Mbps");
			}
		}else {
			 localLoopBandWidthValue = "NA";
		}
		ExcelBean localLoopBandWidth = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_B,
				OrderDetailsExcelDownloadConstants.LOCAL_LOOP_BANDWITHD,
				localLoopBandWidthValue);
		localLoopBandWidth.setOrder(3);
		localLoopBandWidth.setSiteBId(siteB.getId());
		listBook.add(localLoopBandWidth);

		ExcelBean interfaceTypeB = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_B,
				OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_B_END, interfaceB == null ? "NA" : interfaceB);
		interfaceTypeB.setOrder(3);
		interfaceTypeB.setSiteBId(siteB.getId());
		listBook.add(interfaceTypeB);

		ExcelBean book29 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_B,
				OrderDetailsExcelDownloadConstants.CHARGABLE_DISTANCE, chargeableDistance == null ? "NA"
						: chargeableDistance + OrderDetailsExcelDownloadConstants.KILO_METERS);
		book29.setOrder(3);
		book29.setSiteBId(siteB.getId());
		listBook.add(book29);

		ExcelBean book291 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_B,
				OrderDetailsExcelDownloadConstants.LM_COMPONENT, "LAST MILE");
		book291.setOrder(3);
		book291.setSiteBId(siteB.getId());
		listBook.add(book291);


		if (provider != null && provider.toLowerCase().contains("Tata Communications".toLowerCase())) {
			provider = "MAN";
		}
		//PIPF-208 - LM provider change for NDE
		OrderNplLink orderNplLinkList = orderNplLinkRepository.findBySiteBId(siteB.getId());
		Optional<OrderProductSolution> orderProductSolutions= orderProductSolutionRepository.findById(orderNplLinkList.getProductSolutionId());
		if (orderProductSolutions.get().getOrderToLeProductFamily().getOrderToLe().getOrder().getOrderCode().startsWith("NDE") && !MACDConstants.CANCELLATION.equalsIgnoreCase(orderProductSolutions.get().getOrderToLeProductFamily().getOrderToLe().getOrderType())) {
			LOGGER.info("Checking provider value for NDE");
			// checking provider for dc
			if(orderNplLinkList.getSiteBType().equalsIgnoreCase(NplPDFConstants.DC)){
				provider = "Tata Comm Colocated LM";
			}
			// checking provider for pop
			Optional<OrderIllSite> siteb = orderIllSitesRepository.findById(siteB.getId());
			String popLocationID = getPopLocationbyLocId(siteb.get().getErfLocSitebLocationId());
			LOGGER.info("site b Pop location id is {}" ,popLocationID);
			if (popLocationID != null && !popLocationID.isEmpty()){
				LOGGER.info("Setting provider for POP location site b");
				provider = "Tata Comm Colocated LM";
			} else {
				JSONObject dataEnvelopeObj = null;
				JSONParser jsonParser = new JSONParser();
				List<OrderLinkFeasibility> feasResponseList =orderLinkFeasibilityRepository.findByOrderNplLink(orderNplLinkList);
				Boolean isPopTrueforSiteB = false;
				if (feasResponseList != null && !feasResponseList.isEmpty()){
					for (OrderLinkFeasibility feasResponseListIn : feasResponseList) {
						String responseJson = feasResponseListIn.getResponseJson();
						try {
							if (responseJson != null) {
								dataEnvelopeObj = (JSONObject) jsonParser.parse(responseJson);
								isPopTrueforSiteB = findifPopTrueforSite(dataEnvelopeObj, "b_tcl_pop_flag");
								if (isPopTrueforSiteB){
									LOGGER.info("Setting provider for POP location site b with feas attribute");
									provider = "Tata Comm Colocated LM";
								}
							}
						} catch (ParseException e) {
							LOGGER.error("Exception in parsing Response Json ", ExceptionUtils.getStackTrace(e));
						}
					}
				}

			}


		}
		LOGGER.info("provider value set {}", provider);
		ExcelBean book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_B,
				OrderDetailsExcelDownloadConstants.LM_PROVIDER, provider == null ? " " : provider);
		book26.setOrder(3);
		book26.setSiteBId(siteB.getId());
		listBook.add(book26);
		ExcelBean book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_B,
				OrderDetailsExcelDownloadConstants.MANAGED, OrderDetailsExcelDownloadConstants.TCL);
		book27.setOrder(3);
		book27.setSiteBId(siteB.getId());
		listBook.add(book27);

		ExcelBean book25 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_B,
				OrderDetailsExcelDownloadConstants.ARRANGED_BY, OrderDetailsExcelDownloadConstants.TCL);
		book25.setOrder(3);
		book25.setSiteBId(siteB.getId());
		listBook.add(book25);

	}

	/**
	 * createLastMileComponentForExcel
	 * 
	 * @param latsMileMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 */
	private void createSiteALastMileComponentForExcel(Map<String, String> latsMileMap, List<ExcelBean> listBook,
			OrderIllSite siteA, String chargeableDistance, String provider, String interfaceA) {
		if (latsMileMap == null) {
			latsMileMap = new HashMap<>();
		}
		String localLoopBandWidthValue;
		if(latsMileMap.containsKey("Local Loop Bandwidth")) {
			if(latsMileMap.get("Local Loop Bandwidth").contains("Mbps")) {
				 localLoopBandWidthValue = latsMileMap.get("Local Loop Bandwidth");
			}else {
				localLoopBandWidthValue = latsMileMap.get("Local Loop Bandwidth").concat("Mbps");
			}
		}else {
			localLoopBandWidthValue = "NA";
		}
		ExcelBean localLoopBandWidth = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_A,
				OrderDetailsExcelDownloadConstants.LOCAL_LOOP_BANDWITHD,
				localLoopBandWidthValue);
		localLoopBandWidth.setOrder(3);
		localLoopBandWidth.setSiteAId(siteA.getId());
		listBook.add(localLoopBandWidth);

		ExcelBean interfaceTypeA = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_A,
				OrderDetailsExcelDownloadConstants.INTERFACE_TYPE_A_END, interfaceA == null ? "NA" : interfaceA);
		interfaceTypeA.setOrder(3);
		interfaceTypeA.setSiteAId(siteA.getId());
		listBook.add(interfaceTypeA);

		ExcelBean book29 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_A,
				OrderDetailsExcelDownloadConstants.CHARGABLE_DISTANCE, chargeableDistance == null ? "NA"
						: chargeableDistance + OrderDetailsExcelDownloadConstants.KILO_METERS);
		book29.setOrder(3);
		book29.setSiteAId(siteA.getId());
		listBook.add(book29);

		ExcelBean book291 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_A,
				OrderDetailsExcelDownloadConstants.LM_COMPONENT, "LAST MILE");
		book291.setOrder(3);
		book291.setSiteAId(siteA.getId());
		listBook.add(book291);

		if (provider != null && provider.toLowerCase().contains("Tata Communications".toLowerCase())) {
			provider = "MAN";
		}


		//PIPF-208 - LM provider change for NDE
		OrderNplLink orderNplLinkList = orderNplLinkRepository.findBySiteAId(siteA.getId());
		Optional<OrderProductSolution> orderProductSolutions= orderProductSolutionRepository.findById(orderNplLinkList.getProductSolutionId());
		if (orderProductSolutions.get().getOrderToLeProductFamily().getOrderToLe().getOrder().getOrderCode().startsWith("NDE") && !MACDConstants.CANCELLATION.equalsIgnoreCase(orderProductSolutions.get().getOrderToLeProductFamily().getOrderToLe().getOrderType())) {
			LOGGER.info("Checking provider value for NDE");
			// checking provider for dc
			if(orderNplLinkList.getSiteAType().equalsIgnoreCase(NplPDFConstants.DC)){

				provider = "Tata Comm Colocated LM";
			}
			// checking provider for pop
			Optional<OrderIllSite> sitea = orderIllSitesRepository.findById(siteA.getId());
			String popLocationID = getPopLocationbyLocId(sitea.get().getErfLocSitebLocationId());
			LOGGER.info("site a Pop location id is {}" ,popLocationID);
			if (popLocationID != null && !popLocationID.isEmpty()){
				LOGGER.info("Setting provider for POP location site a");
				provider = "Tata Comm Colocated LM";
			} else {
				JSONObject dataEnvelopeObj = null;
				JSONParser jsonParser = new JSONParser();
				List<OrderLinkFeasibility> feasResponseList =orderLinkFeasibilityRepository.findByOrderNplLink(orderNplLinkList);
				Boolean isPopTrueforSiteA = false;
				if (feasResponseList != null && !feasResponseList.isEmpty()){
					for (OrderLinkFeasibility feasResponseListIn : feasResponseList) {
						String responseJson = feasResponseListIn.getResponseJson();
						try {
							if (responseJson != null) {
								dataEnvelopeObj = (JSONObject) jsonParser.parse(responseJson);
								isPopTrueforSiteA = findifPopTrueforSite(dataEnvelopeObj, "a_tcl_pop_flag");
								if (isPopTrueforSiteA){
									LOGGER.info("Setting provider for POP location site a with feas attribute");
									provider = "Tata Comm Colocated LM";
								}
							}
						} catch (ParseException e) {
							LOGGER.error("Exception in parsing Response Json ", ExceptionUtils.getStackTrace(e));
						}
					}
				}

			}


		}
		LOGGER.info("provider value set {}", provider);
		ExcelBean book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_A,
				OrderDetailsExcelDownloadConstants.LM_PROVIDER, provider == null ? " " : provider);
		book26.setOrder(3);
		book26.setSiteAId(siteA.getId());
		listBook.add(book26);
		ExcelBean book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_A,
				OrderDetailsExcelDownloadConstants.MANAGED, OrderDetailsExcelDownloadConstants.TCL);
		book27.setOrder(3);
		book27.setSiteAId(siteA.getId());
		listBook.add(book27);

		ExcelBean book25 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE_SITE_A,
				OrderDetailsExcelDownloadConstants.ARRANGED_BY, OrderDetailsExcelDownloadConstants.TCL);
		book25.setOrder(3);
		book25.setSiteAId(siteA.getId());
		listBook.add(book25);
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
			
			feasiblityBean.setaEnd_feasibilityMode(feasiblity.getFeasibilityMode());
			feasiblityBean.setbEnd_feasibilityMode(feasiblity.getFeasibilityModeB());
			
			feasiblityBean.setaEnd_provider(feasiblity.getProvider());
			feasiblityBean.setbEnd_provider(feasiblity.getProviderB());
			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}

	/**
	 * 
	 * getOrdersBasedOnLegalEntities - This method is used to get orders based on
	 * legal entities
	 * 
	 * @return IsvFilterResponse
	 * @throws TclCommonException
	 */
	/*
	 * public IsvFilterResponse getOrdersForFilter() throws TclCommonException {
	 * IsvFilterResponse isvFilterResponse = new IsvFilterResponse(); try {
	 * 
	 * List<String> stagesList = new ArrayList<>(); List<MstProductFamilyDto>
	 * productFamiliesList = new ArrayList<>(); List<String> customerLeIdsList = new
	 * ArrayList<>(); List<Map<String, Object>> mapDistinctStages =
	 * orderRepository.findDistinctStages(); if (!mapDistinctStages.isEmpty()) {
	 * mapDistinctStages.stream().forEach(map -> { stagesList.add((String)
	 * map.get("orderStage")); }); }
	 * 
	 * isvFilterResponse.setStatusList(stagesList); List<Map<String, Object>>
	 * mapDistinctProdFamilies = orderRepository.findDistinctProductFamily(); if
	 * (!mapDistinctProdFamilies.isEmpty()) {
	 * mapDistinctProdFamilies.stream().forEach(map -> { MstProductFamilyDto
	 * mstProductFamilyDto = new MstProductFamilyDto();
	 * mstProductFamilyDto.setId((Integer) map.get("productFamilyId"));
	 * mstProductFamilyDto.setName((String) map.get("productFamilyName"));
	 * productFamiliesList.add(mstProductFamilyDto); }); }
	 * isvFilterResponse.setProductsList(productFamiliesList); List<Map<String,
	 * Object>> mapDistinctCustomerLeIds =
	 * orderRepository.findDistinctCustomerLeIds(); if
	 * (!mapDistinctCustomerLeIds.isEmpty()) {
	 * mapDistinctCustomerLeIds.stream().forEach(map -> { if
	 * ((map.containsKey("customerLegalEntities"))) {
	 * 
	 * customerLeIdsList.add(map.get("customerLegalEntities").toString());
	 * 
	 * } }); }
	 * 
	 * String customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
	 * String response = (String) mqUtils.sendAndReceive(customerLeQueue,
	 * customerLeIdsCommaSeparated); CustomerLegalEntityDetailsBean cLeBean =
	 * (CustomerLegalEntityDetailsBean) Utils .convertJsonToObject(response,
	 * CustomerLegalEntityDetailsBean.class); if (!Objects.isNull(cLeBean)) {
	 * isvFilterResponse.setCustomerLegalEntityList(cLeBean.getCustomerLeDetails());
	 * } } catch (Exception e) { throw new
	 * TclCommonException(ExceptionConstants.COMMON_ERROR, e,
	 * ResponseResource.R_CODE_ERROR); } return isvFilterResponse;
	 * 
	 * }
	 */
	/**
	 * Get All Orders based on Search Criteria
	 *
	 * @param stage
	 * @param productFamilyId
	 * @param legalEntity
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param size
	 * @return
	 * @throws TclCommonException
	 */

	/*
	 * public PagedResult<NplOrdersBean> getOrdersBySearch(final String stage, final
	 * Integer productFamilyId, final Integer legalEntity, final String startDate,
	 * final String endDate, final Integer page, final Integer size) throws
	 * TclCommonException { final List<NplOrdersBean> ordersBeans = new
	 * ArrayList<>(); Page<Order> orders = null; try {
	 * 
	 * orders = orderRepository.findAll( OrderSpecification.getOrders(stage,
	 * productFamilyId, legalEntity, startDate, endDate), PageRequest.of(page,
	 * size));
	 * 
	 * for (final Order order : orders) { ordersBeans.add(constructOrder(order,
	 * order.getOrderVersion())); }
	 * 
	 * } catch (Exception e) { throw new
	 * TclCommonException(ExceptionConstants.COMMON_ERROR, e,
	 * ResponseResource.R_CODE_ERROR); }
	 * 
	 * return new PagedResult<>(ordersBeans, orders.getTotalElements(),
	 * orders.getTotalPages()); }
	 */
	/**
	 * extractLeAttributes
	 * 
	 * @param orderTLe
	 */
	private String extractLeAttributes(OrderToLe orderTLe, OrderLeSVBean orderLeSVBean, String attributeName) {
		MstOmsAttribute mstAttributes = null;
		String attributeValue = null;
		List<MstOmsAttribute> mstAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(attributeName,
				(byte) 1);
		if (!mstAttributesList.isEmpty()) {
			mstAttributes = mstAttributesList.get(0);

		}
		if (mstAttributes != null) {
			Set<OrdersLeAttributeValue> ordersLeAttributeValues = ordersLeAttributeValueRepository
					.findByMstOmsAttributeAndOrderToLe(mstAttributes, orderTLe);
			for (OrdersLeAttributeValue ordersLeAttributeValue : ordersLeAttributeValues) {
				attributeValue = ordersLeAttributeValue.getAttributeValue();
			}
		}
		return attributeValue;
	}

	private LeStateInfo getGstDetails(String gstNo, Integer customerLeID) {

		SiteGstDetail siteGstDetail = new SiteGstDetail();
		siteGstDetail.setGstNo(gstNo);
		siteGstDetail.setCustomerLeId(customerLeID);

		try {
			String leGst = (String) mqUtils.sendAndReceive(siteGstQueue, Utils.convertObjectToJson(siteGstDetail));

			if (StringUtils.isNotBlank(leGst)) {
				LeStateInfo leStateInfo = (LeStateInfo) Utils.convertJsonToObject(leGst, LeStateInfo.class);
				return leStateInfo;

			}
		} catch (TclCommonException | IllegalArgumentException e) {

			LOGGER.error("error in getting gst response");
		}

		return null;
	}
	
	/**
	 * createOrderDetails - extracts order related details as excel bean Cross connect
	 * 
	 * @param listBook
	 * @param attributeValues
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private void createCrossConnectOrderDetails(List<ExcelBean> listBook, Map<String, String> crossConnectAttributeValues, Integer customerLeId)
			throws TclCommonException, IllegalArgumentException {
		ExcelBean Cattribute1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.CROSS_CONNECT_TYPE,
				crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.CROSS_CONNECT_TYPE)
						? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.CROSS_CONNECT_TYPE)
						: "NA");
		Cattribute1.setOrder(2);
			Cattribute1.setSiteId(0);
		listBook.add(Cattribute1);

		if(crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.CROSS_CONNECT_TYPE).equalsIgnoreCase(CrossconnectConstants.ACTIVE)) {
			ExcelBean Cattribute5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.BANDWIDTH,
					crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.BANDWIDTH)
							? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.BANDWIDTH)+" Mbps"
							: "NA");
			Cattribute5.setOrder(2);
			Cattribute5.setSiteId(0);
			listBook.add(Cattribute5);
			ExcelBean interfaceA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.INTERFACE_A_END,
					crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.INTERFACE)
							? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.INTERFACE)
							: "NA");
			interfaceA.setOrder(2);
			interfaceA.setSiteId(0);
			listBook.add(interfaceA);

			ExcelBean interfaceB = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.INTERFACE_B_END,
					crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.INTERFACE_B)
							? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.INTERFACE_B)
							: "NA");
			interfaceB.setOrder(2);
			interfaceB.setSiteId(0);
			listBook.add(interfaceB);
			
		}
		else {
		ExcelBean fattribute1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.FIBER_PAIRS_COUNT,
				crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.FIBER_PAIRS_COUNT)
						? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.FIBER_PAIRS_COUNT)
						: "NA");
		fattribute1.setOrder(2);
		fattribute1.setSiteId(0);
		listBook.add(fattribute1);
		ExcelBean Cattribute7 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.MEDIA_TYPE,
				crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.MEDIA_TYPE)
						? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.MEDIA_TYPE)
						: "NA");
		Cattribute7.setOrder(2);
		Cattribute7.setSiteId(0);
		listBook.add(Cattribute7);
		if(crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.MEDIA_TYPE).equalsIgnoreCase(CrossconnectConstants.FIBER_PAIR)){
		ExcelBean fattribute2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.FIBER_ENTRY,
				crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.FIBER_ENTRY)
						? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.FIBER_ENTRY)
						: "NA");
		fattribute2.setOrder(2);
		fattribute2.setSiteId(0);
		listBook.add(fattribute2);
		if(crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.FIBER_ENTRY).equalsIgnoreCase("Yes")) {
		ExcelBean fattribute3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.TYPE_OF_FIBER_ENTRY,
				crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.TYPE_OF_FIBER_ENTRY)
						? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.TYPE_OF_FIBER_ENTRY)
						: "NA");
		fattribute3.setOrder(2);
		fattribute3.setSiteId(0);
		listBook.add(fattribute3);
		  }
		}
		else {
				/*
				 * ExcelBean fattribute2 = new
				 * ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				 * OrderDetailsExcelDownloadConstants.FIBER_ENTRY,
				 * crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.
				 * FIBER_ENTRY) ? "NO": "NA"); fattribute2.setOrder(1);
				 * fattribute2.setLinkId(0); listBook.add(fattribute2);
				 */
		}
		
		}
		
		
		/*Commented as per requirement
		ExcelBean Cattribute2 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.SERVICE_CREDITS_UPTIME,
				crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_CREDITS_UPTIME)
						? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.SERVICE_CREDITS_UPTIME)
						: "NA");
		Cattribute2.setOrder(1);
		Cattribute2.setLinkId(0);
		listBook.add(Cattribute2);*/
		ExcelBean Cattribute3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.A_END_ADDRESS_TYPE,
				crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.A_END_ADDRESS_TYPE)
						? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.A_END_ADDRESS_TYPE)
						: "NA");
		Cattribute3.setOrder(2);
		Cattribute3.setSiteId(0);
		listBook.add(Cattribute3);
		ExcelBean Cattribute4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.Z_END_ADDRESS_TYPE,
				crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.Z_END_ADDRESS_TYPE)
						? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.Z_END_ADDRESS_TYPE)
						: "NA");
		Cattribute4.setOrder(2);
		Cattribute4.setSiteId(0);
		listBook.add(Cattribute4);
		
		
		//SLA added default as 99.5
		ExcelBean ntwUpTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
				OrderDetailsExcelDownloadConstants.SERVICE_AVAILABILITY, ">=99.5%");
		ntwUpTime.setOrder(2);
		ntwUpTime.setSiteId(0);
		listBook.add(ntwUpTime);

        if(crossConnectAttributeValues.containsKey("GSTNO_A") && crossConnectAttributeValues.containsKey("GSTNO_Z")){
            LOGGER.info("GST no for cross connect site"+crossConnectAttributeValues.get("GSTNO_A"));

            ExcelBean siteGstNo = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SITE_GST, crossConnectAttributeValues.get("GSTNO_A"));
            siteGstNo.setOrder(2);
            siteGstNo.setSiteId(0);
            listBook.add(siteGstNo);

            LeStateInfo gstDetails = getGstDetails(crossConnectAttributeValues.get("GSTNO_A"), customerLeId);

            if(gstDetails!=null) {

                ExcelBean siteGstAddr = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                        OrderDetailsExcelDownloadConstants.SITE_GST, (Objects.nonNull(gstDetails.getAddress()) && !gstDetails.getAddress().isEmpty())
                        ?  gstDetails.getAddress() : "NA");
                siteGstAddr.setSiteId(0);
                siteGstAddr.setOrder(2);
                listBook.add(siteGstAddr);

                ExcelBean siteGstAddrLine1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                        OrderDetailsExcelDownloadConstants.SITE_GST, (Objects.nonNull(gstDetails.getAddresslineOne()) && !gstDetails.getAddresslineOne().isEmpty())
                        ?  gstDetails.getAddresslineOne() : "NA");
                siteGstAddrLine1.setSiteId(0);
                siteGstAddrLine1.setOrder(2);
                listBook.add(siteGstAddrLine1);

                ExcelBean siteGstAddrLine2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                        OrderDetailsExcelDownloadConstants.SITE_GST, (Objects.nonNull(gstDetails.getAddresslineTwo()) && !gstDetails.getAddresslineTwo().isEmpty())
                        ?  gstDetails.getAddresslineTwo() : "NA");
                siteGstAddrLine2.setSiteId(0);
                siteGstAddrLine2.setOrder(2);
                listBook.add(siteGstAddrLine2);

                ExcelBean siteGstCity = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                        OrderDetailsExcelDownloadConstants.SITE_GST, (Objects.nonNull(gstDetails.getCity()) && !gstDetails.getCity().isEmpty())
                        ? gstDetails.getCity() : "NA");
                siteGstCity.setSiteId(0);
                siteGstCity.setOrder(2);
                listBook.add(siteGstCity);

                ExcelBean siteGstState = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                        OrderDetailsExcelDownloadConstants.SITE_GST, (Objects.nonNull(gstDetails.getState()) && !gstDetails.getState().isEmpty())
                        ?  gstDetails.getState() : "NA");
                siteGstState.setSiteId(0);
                siteGstState.setOrder(2);
                listBook.add(siteGstState);

                ExcelBean siteGstCountry = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                        OrderDetailsExcelDownloadConstants.SITE_GST, (Objects.nonNull(gstDetails.getCountry()) && !gstDetails.getCountry().isEmpty())
                        ? gstDetails.getCountry(): "NA");
                siteGstCountry.setSiteId(0);
                siteGstCountry.setOrder(2);
                listBook.add(siteGstCountry);

                ExcelBean siteGstPincode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                        OrderDetailsExcelDownloadConstants.SITE_GST, (Objects.nonNull(gstDetails.getPincode()) && !gstDetails.getPincode().isEmpty())
                        ? gstDetails.getPincode(): "NA");
                siteGstPincode.setSiteId(0);
                siteGstPincode.setOrder(2);
                listBook.add(siteGstPincode);
            }

        }
		
//        if(Objects.nonNull(crossConnectAttributeValues.get("LOCAL_IT_CONTACT_A")) && Objects.nonNull(crossConnectAttributeValues.get("LOCAL_IT_CONTACT_Z"))){
//		Map<String ,Object> locITcontactA=getLocalItContacts(crossConnectAttributeValues.get("LOCAL_IT_CONTACT_A"));
//        Map<String ,Object> locITcontactZ=getLocalItContacts(crossConnectAttributeValues.get("LOCAL_IT_CONTACT_Z"));
//		//for(LocationItContact itcontact:locItcontactList) {
//			if (!locITcontactA.isEmpty()) {
//				ExcelBean localItContactName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
//						OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locITcontactA.get("NAME").toString());
//				localItContactName.setSiteId(0);
//				localItContactName.setOrder(2);
//				listBook.add(localItContactName);
//
//				ExcelBean localItContactEmail = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
//						OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locITcontactA.get("EMAIL").toString());
//				localItContactEmail.setSiteId(0);
//				localItContactEmail.setOrder(2);
//				listBook.add(localItContactEmail);
//
//				ExcelBean localItContactPhone = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
//						OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locITcontactA.get("CONTACT_NO").toString());
//				localItContactPhone.setSiteId(0);
//				localItContactPhone.setOrder(2);
//				listBook.add(localItContactPhone);
//			}
//			else {
//				ExcelBean localItContactName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
//						OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locITcontactZ.get("NAME").toString());
//				localItContactName.setSiteId(0);
//				localItContactName.setOrder(2);
//				listBook.add(localItContactName);
//
//				ExcelBean localItContactEmail = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
//						OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locITcontactZ.get("EMAIL").toString());
//				localItContactEmail.setSiteId(0);
//				localItContactEmail.setOrder(2);
//				listBook.add(localItContactEmail);
//
//				ExcelBean localItContactPhone = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
//						OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locITcontactZ.get("CONTACT_NO").toString());
//				localItContactPhone.setSiteId(0);
//				localItContactPhone.setOrder(2);
//				listBook.add(localItContactPhone);
//			}
//
//		}


//		 List<DemarcationBean> demolist = getCrossConnectDemarcation(crossConnectAttributeValues.get("erfLocationId"));
//		 LOGGER.info("SIZE OF demolist"+demolist.size());
//		 for(DemarcationBean demarcationBean:demolist) {
//			 if(demarcationBean.getAendDemarc()) {
//			 ExcelBean demarcationFloor = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
//						OrderDetailsExcelDownloadConstants.A_END_DEMARCATION_FLOOR, demarcationBean.getFloor());
//			 demarcationFloor.setSiteId(0);
//			 demarcationFloor.setOrder(2);
//			 listBook.add(demarcationFloor);
//			 ExcelBean demarcationRoom = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
//						OrderDetailsExcelDownloadConstants.A_END_DEMARCATION_ROOM, demarcationBean.getRoom());
//			 demarcationRoom.setSiteId(0);
//			 demarcationRoom.setOrder(2);
//			 listBook.add(demarcationRoom);
//			 ExcelBean demarcationWing = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
//						OrderDetailsExcelDownloadConstants.A_END_DEMARCATION_WING, demarcationBean.getWing());
//			 demarcationWing.setSiteId(0);
//			 demarcationWing.setOrder(2);
//			 listBook.add(demarcationWing);
//			 }
//			 else {
//				 ExcelBean demarcationFloor = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
//							OrderDetailsExcelDownloadConstants.Z_END_DEMARCATION_FLOOR, demarcationBean.getFloor());
//				 demarcationFloor.setSiteId(0);
//				 demarcationFloor.setOrder(2);
//				 listBook.add(demarcationFloor);
//				 ExcelBean demarcationRoom = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
//							OrderDetailsExcelDownloadConstants.Z_END_DEMARCATION_ROOM, demarcationBean.getRoom());
//				 demarcationRoom.setSiteId(0);
//				 demarcationRoom.setOrder(2);
//				 listBook.add(demarcationRoom);
//				 ExcelBean demarcationWing = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
//							OrderDetailsExcelDownloadConstants.Z_END_DEMARCATION_WING, demarcationBean.getWing());
//				 demarcationWing.setSiteId(0);
//				 demarcationWing.setOrder(2);
//				 listBook.add(demarcationWing);
//			 }
//
//		 }
//		 LOGGER.info("SIZE OF demolist"+demolist.size());
//
//		String siteId = crossConnectAttributeValues.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_ID) ? crossConnectAttributeValues.get(OrderDetailsExcelDownloadConstants.CROSS_CONNECT_TYPE) : null;
//		List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(Integer.parseInt(siteId),
//				IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), QuoteConstants.ILLSITES.toString());
//		QuoteProductComponent quoteProductComponentVal = components.stream().filter(quoteProductComponent ->
//				quoteProductComponent.getMstProductComponent().getName().equalsIgnoreCase(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties())).findFirst().get();
//		Optional<QuoteProductComponentsAttributeValue> crossConnectLocalDemarcationId = quoteProductComponentVal.getQuoteProductComponentsAttributeValues().
//				stream().filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(CROSS_CONNECT_LOCAL_DEMARCATION_ID)).findFirst();

		if(Objects.nonNull(crossConnectAttributeValues.get(CROSS_CONNECT_LOCAL_DEMARCATION_ID))) {
			LOGGER.info("crossConnectLocalDemarcationId :: {}", crossConnectAttributeValues.get(CROSS_CONNECT_LOCAL_DEMARCATION_ID));

			CrossConnectLocalITDemarcationBean crossConnectLocalITDemarcationBean = nplQuoteService.getCrossConnectDemarcationV2(crossConnectAttributeValues.get(CROSS_CONNECT_LOCAL_DEMARCATION_ID));
			if (!Objects.isNull(crossConnectLocalITDemarcationBean)) {

				crossConnectLocalITDemarcationBean.getContacts().forEach(contact ->
						contact.getSiteA().forEach(site -> {
							ExcelBean localItContactName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
									OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_NAME, site.getName());
							localItContactName.setSiteId(0);
							localItContactName.setOrder(2);
							listBook.add(localItContactName);

							ExcelBean localItContactEmail = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
									OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_EMAIL, site.getEmail());
							localItContactEmail.setSiteId(0);
							localItContactEmail.setOrder(2);
							listBook.add(localItContactEmail);

							ExcelBean localItContactPhone = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
									OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_NUMBER, site.getContactNo());
							localItContactPhone.setSiteId(0);
							localItContactPhone.setOrder(2);
							listBook.add(localItContactPhone);
						})
				);


				crossConnectLocalITDemarcationBean.getContacts().forEach(contact ->
						contact.getSiteZ().forEach(site -> {
							ExcelBean localItContactName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
									OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_NAME,  site.getName());
							localItContactName.setSiteId(0);
							localItContactName.setOrder(2);
							listBook.add(localItContactName);

							ExcelBean localItContactEmail = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
									OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_EMAIL, site.getEmail());
							localItContactEmail.setSiteId(0);
							localItContactEmail.setOrder(2);
							listBook.add(localItContactEmail);

							ExcelBean localItContactPhone = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
									OrderDetailsExcelDownloadConstants.CUSTOMER_SITE_LOCAL_IT_CONTACT_NUMBER, site.getContactNo());
							localItContactPhone.setSiteId(0);
							localItContactPhone.setOrder(2);
							listBook.add(localItContactPhone);
						})
				);


				crossConnectLocalITDemarcationBean.getDemarcations().forEach(demark ->
						demark.getSiteA().forEach(site -> {
							ExcelBean demarcationFloor = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
									OrderDetailsExcelDownloadConstants.A_END_DEMARCATION_FLOOR, site.getFloor());
							demarcationFloor.setSiteId(0);
							demarcationFloor.setOrder(2);
							listBook.add(demarcationFloor);
							ExcelBean demarcationRoom = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
									OrderDetailsExcelDownloadConstants.A_END_DEMARCATION_ROOM, site.getRoom());
							demarcationRoom.setSiteId(0);
							demarcationRoom.setOrder(2);
							listBook.add(demarcationRoom);
							ExcelBean demarcationWing = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
									OrderDetailsExcelDownloadConstants.A_END_DEMARCATION_WING, site.getWing());
							demarcationWing.setSiteId(0);
							demarcationWing.setOrder(2);
							listBook.add(demarcationWing);
						})
				);


				crossConnectLocalITDemarcationBean.getDemarcations().forEach(demark ->
						demark.getSiteZ().forEach(site -> {
							ExcelBean demarcationFloor = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
									OrderDetailsExcelDownloadConstants.Z_END_DEMARCATION_FLOOR, site.getFloor());

							demarcationFloor.setSiteId(0);
							demarcationFloor.setOrder(2);
							listBook.add(demarcationFloor);
							ExcelBean demarcationRoom = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
									OrderDetailsExcelDownloadConstants.Z_END_DEMARCATION_ROOM, site.getRoom());
							demarcationRoom.setSiteId(0);
							demarcationRoom.setOrder(2);
							listBook.add(demarcationRoom);
							ExcelBean demarcationWing = new ExcelBean(OrderDetailsExcelDownloadConstants.DEMARCATION_DETAILS,
									OrderDetailsExcelDownloadConstants.Z_END_DEMARCATION_WING, site.getWing());
							demarcationWing.setSiteId(0);
							demarcationWing.setOrder(2);
							listBook.add(demarcationWing);

						})
				);

			}
		}


	}

	/*private List<LocationItContact> getCrossConnectLocalItContact(String locationId,String customerId)
			throws TclCommonException, IllegalArgumentException {
		LOGGER.info("Info for cross connect customer LE co {} :"+locationId+"--"+customerId);
		List<String> list = Arrays.asList(locationId,customerId);
		String request = String.join(",", list);
		String response = (String) mqUtils.sendAndReceive(getCrossConnectLocalItContact,
				request);
		LocationItContact[] locationItContactsResult = (LocationItContact[]) Utils.convertJsonToObject(response,LocationItContact[].class);
		List<LocationItContact> locationItContacts=Arrays.asList(Arrays.stream(locationItContactsResult).toArray(LocationItContact[]::new));
		return  locationItContacts;
	}*/
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


//	private CrossConnectLocalITDemarcationBean getCrossConnectDemarcation(String locationId)
//		throws TclCommonException, IllegalArgumentException {
//		LOGGER.info("Info for cross connect location {} :"+locationId);
//		CrossConnectLocalITDemarcationBean crossConnectDemarcationDetails = nplQuoteService.getCrossConnectDemarcationV2(locationId);
//		return  crossConnectDemarcationDetails;
//
//	}

//	private List<DemarcationBean> getCrossConnectDemarcation(String locationId)
//			throws TclCommonException, IllegalArgumentException {
//		LOGGER.info("Info for cross connect location {} :"+locationId);
//		List<String> list = Arrays.asList(locationId);
//		String request = String.join(",", list);
//		String response = (String) mqUtils.sendAndReceive(geCrossConnectDemarcation,
//				request);
//		DemarcationBean[] locationItContactsResult = (DemarcationBean[]) Utils.convertJsonToObject(response,DemarcationBean[].class);
//		List<DemarcationBean> demarcationBeans =Arrays.asList(Arrays.stream(locationItContactsResult).toArray(DemarcationBean[]::new));
//		return  demarcationBeans;
//	}
	
	/**
	 * getAttributeValues - retrieves attribute values for an OrderToLe for crossconnect
	 * 
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, String> getCrossconnectAttributeValues(OrderIllSite orderIllSite) throws TclCommonException {
		Map<String, String> attributeMap = new HashMap<>();
		LOGGER.info("Get Cross connect attributes started {}", orderIllSite.getId());
		try {

			/*if (Objects.isNull(orderToLe)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			List<OrderProductSolution> orderProductSolution=new ArrayList<OrderProductSolution>();
			List<OrderIllSite> orderIllSite=new ArrayList<OrderIllSite>();
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues=new ArrayList<OrderProductComponentsAttributeValue>();
			
			OrderToLeProductFamily orderToLeProductFamily=orderToLeProductFamilyRepository.findByOrderToLe_Id(orderToLe.getId());
			if(orderToLeProductFamily!=null) {
			   orderProductSolution=orderProductSolutionRepository.findByOrderToLeProductFamily(orderToLeProductFamily);
			}
			if(orderProductSolution.get(0)!=null){
			   orderIllSite=orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution.get(0), (byte) 1);
			}
			if(orderIllSite.get(0)!=null) {*/
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues=new ArrayList<OrderProductComponentsAttributeValue>();
			attributeMap.put("erfLocationId",orderIllSite.getErfLocSitebLocationId().toString());
			List<OrderProductComponent> orderProductComponent=orderProductComponentRepository.findByReferenceId(orderIllSite.getId());
			for(OrderProductComponent productcomponent :orderProductComponent) {
				LOGGER.info("Enter componnet"+productcomponent.getMstProductComponent().getName());
				if(productcomponent.getMstProductComponent().getName().equalsIgnoreCase(CrossconnectConstants.FIBER_ENTRY_COMPONENT) || 
						productcomponent.getMstProductComponent().getName().equalsIgnoreCase(CrossconnectConstants.CROSS_CONNECT) ||
						productcomponent.getMstProductComponent().getName().equalsIgnoreCase("SITE_PROPERTIES")) {
					orderProductComponentsAttributeValues=orderProductComponentsAttributeValueRepository.findByOrderProductComponent(productcomponent);
					orderProductComponentsAttributeValues.forEach(attr -> {
						attributeMap.put(attr.getProductAttributeMaster().getName(), attr.getAttributeValues());

					});
				}
				
			   }
			//}
			
		} catch (Exception e) {
			LOGGER.error("getAttributeValues crossconnect error", e);
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Get attributes crossconnct completed {}", orderIllSite.getId());
		return attributeMap;
	}
	
	
	/**
	 * createCrossConnectBillingComponentPrice
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createCrossConnectBillingComponentPrice(List<ExcelBean> listBook,OrderIllSite orderIllSite,String crossConnectType)throws TclCommonException {
		
		LOGGER.info("Entered into createCrossConnectBillingComponentPrice {}", orderIllSite.getId());
		try {
			/*if (Objects.isNull(orderToLe)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			List<OrderProductSolution> orderProductSolution=new ArrayList<OrderProductSolution>();
			List<OrderIllSite> orderIllSites=new ArrayList<OrderIllSite>();
			
			OrderToLeProductFamily orderToLeProductFamily=orderToLeProductFamilyRepository.findByOrderToLe_Id(orderToLe.getId());
			if(orderToLeProductFamily!=null) {
			   orderProductSolution=orderProductSolutionRepository.findByOrderToLeProductFamily(orderToLeProductFamily);
			}
			if(orderProductSolution.get(0)!=null){
			   orderIllSites=orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution.get(0), (byte) 1);
			}
			if(orderIllSites!=null) {
			orderIllSites.stream().forEach(orderIllSite -> {*/
			List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
					.findBySiteCodeAndPricingType(orderIllSite.getSiteCode(), "primary");
			CrossConnectPricingResponse crossConnectPricingResponse=
					Utils.convertJsonToObject(pricingDetails.stream().findFirst().get().getResponseData(),CrossConnectPricingResponse.class);
			List<OrderProductComponent> orderProductComponent=orderProductComponentRepository.findByReferenceId(orderIllSite.getId());
			for(OrderProductComponent productcomponent :orderProductComponent) {
				LOGGER.info("Enter componnet price id"+productcomponent.getMstProductComponent().getName());
				if(productcomponent.getMstProductComponent().getName().equalsIgnoreCase(CrossconnectConstants.FIBER_ENTRY_COMPONENT) || 
						productcomponent.getMstProductComponent().getName().equalsIgnoreCase(CrossconnectConstants.CROSS_CONNECT)) {
					OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(String.valueOf(productcomponent.getId()),
							OrderDetailsExcelDownloadConstants.COMPONENTS);
					if(CrossconnectConstants.ACTIVE.equalsIgnoreCase(crossConnectType)){
						if (Objects.nonNull(price) && productcomponent.getMstProductComponent().getName().equalsIgnoreCase(CrossconnectConstants.FIBER_ENTRY_COMPONENT)) {
							ExcelBean unitFiberCharges = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.FIBER_ENTRY_UNIT_ARC_NRC,
									BigDecimal.valueOf(price.getEffectiveArc()).toPlainString() + ","
											+ BigDecimal.valueOf(price.getEffectiveNrc()).toPlainString());
							unitFiberCharges.setOrder(4);
							unitFiberCharges.setLinkId(0);
							listBook.add(unitFiberCharges);

							ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.FIBER_ENTRY_ARC_NRC,
									BigDecimal.valueOf(price.getEffectiveArc()).toPlainString() + ","
											+ BigDecimal.valueOf(price.getEffectiveNrc()).toPlainString());
							book36.setOrder(4);
							book36.setLinkId(0);
							listBook.add(book36);

						}
						if (Objects.nonNull(price) && productcomponent.getMstProductComponent().getName().equalsIgnoreCase(CrossconnectConstants.CROSS_CONNECT)) {
							ExcelBean unitCrossConnectCharges = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.CROSS_CONNECT_UNIT_ARC_NRC,
									BigDecimal.valueOf(price.getEffectiveArc()).toPlainString() + ","
											+ BigDecimal.valueOf(price.getEffectiveNrc()).toPlainString());
							unitCrossConnectCharges.setOrder(4);
							unitCrossConnectCharges.setLinkId(0);
							listBook.add(unitCrossConnectCharges);

							ExcelBean book37 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.CROSS_CONNECT_ARC_NRC,
									BigDecimal.valueOf(price.getEffectiveArc()).toPlainString() + ","
											+ BigDecimal.valueOf(price.getEffectiveNrc()).toPlainString());
							book37.setOrder(4);
							book37.setLinkId(0);
							listBook.add(book37);

						}
					}else {

						if (Objects.nonNull(price) && productcomponent.getMstProductComponent().getName().equalsIgnoreCase(CrossconnectConstants.FIBER_ENTRY_COMPONENT)) {
							ExcelBean unitFiberCharges = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.FIBER_ENTRY_UNIT_ARC_NRC,
									BigDecimal.valueOf(crossConnectPricingResponse.getUnitFiberEntryArc()).toPlainString() + ","
											+ BigDecimal.valueOf(crossConnectPricingResponse.getUnitFiberEntryNrc()).toPlainString());
							unitFiberCharges.setOrder(4);
							unitFiberCharges.setLinkId(0);
							listBook.add(unitFiberCharges);

							ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.FIBER_ENTRY_ARC_NRC,
									BigDecimal.valueOf(price.getEffectiveArc()).toPlainString() + ","
											+ BigDecimal.valueOf(price.getEffectiveNrc()).toPlainString());
							book36.setOrder(4);
							book36.setLinkId(0);
							listBook.add(book36);

						}
						if (Objects.nonNull(price) && productcomponent.getMstProductComponent().getName().equalsIgnoreCase(CrossconnectConstants.CROSS_CONNECT)) {
							ExcelBean unitCrossConnectCharges = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.CROSS_CONNECT_UNIT_ARC_NRC,
									BigDecimal.valueOf(crossConnectPricingResponse.getUnitCrossConnectArc()).toPlainString() + ","
											+ BigDecimal.valueOf(crossConnectPricingResponse.getUnitCrossConnectNrc()).toPlainString());
							unitCrossConnectCharges.setOrder(4);
							unitCrossConnectCharges.setLinkId(0);
							listBook.add(unitCrossConnectCharges);

							ExcelBean book37 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.CROSS_CONNECT_ARC_NRC,
									BigDecimal.valueOf(price.getEffectiveArc()).toPlainString() + ","
											+ BigDecimal.valueOf(price.getEffectiveNrc()).toPlainString());
							book37.setOrder(4);
							book37.setLinkId(0);
							listBook.add(book37);

						}
					}
					
				
			    }
			  }
			// });
			
			//}
			
		} catch (Exception e) {
			LOGGER.error("getAttributeValues crossconnect component price error", e);
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
		}

		
	}
	
	/**
	 * createSite A End Address DetailsForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 * @param notFeasible
	 * @throws TclCommonException
	 */
	private void createSiteAEndAddressDetailForExcel(Map<String, Map<String, String>> excelMap,List<ExcelBean> listBook, OrderIllSite siteA,  OrderToLe orderToLe) {

		try {
			createSiteAEndLocationForExcel(siteA, listBook);
		} catch (TclCommonException e) {
			LOGGER.error("Error in crossconnct site a address details");
			
		}
		createSiteAPropertiesForExcel(excelMap, listBook, siteA, orderToLe);
		
	}
	
	/**
	 * createSite A end LocationForExcel
	 * 
	 * @param site
	 * @param listBook
	 */
	private void createSiteAEndLocationForExcel(OrderIllSite orderIllSiteA, List<ExcelBean> listBook) throws TclCommonException {

		String siteALocation = returnApiAddressForSites(orderIllSiteA.getErfLocSitebLocationId());

		AddressDetail adDetailA = null;
		String crossConnect = CommonConstants.NO;

		if (siteALocation != null) {
			adDetailA = (AddressDetail) Utils.convertJsonToObject(siteALocation, AddressDetail.class);
		}

		if (adDetailA != null) {
			ExcelBean siteLocationAddressLine1A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, adDetailA.getAddressLineOne());
			siteLocationAddressLine1A.setOrder(2);
			siteLocationAddressLine1A.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationAddressLine1A);

			ExcelBean siteLocationAddressLine2A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, adDetailA.getAddressLineTwo());
			siteLocationAddressLine2A.setOrder(2);
			siteLocationAddressLine2A.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationAddressLine2A);

			ExcelBean siteLocationCityA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, adDetailA.getCity());
			siteLocationCityA.setOrder(2);
			siteLocationCityA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationCityA);

			ExcelBean siteLocationStateA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, adDetailA.getState());
			siteLocationStateA.setOrder(2);
			siteLocationStateA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationStateA);

			ExcelBean siteLocationPincodeA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, adDetailA.getPincode());
			siteLocationPincodeA.setOrder(2);
			siteLocationPincodeA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationPincodeA);

			ExcelBean siteLocationCountryA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_A,
					OrderDetailsExcelDownloadConstants.SITE_A_ADDRESS, adDetailA.getCountry());
			siteLocationCountryA.setOrder(2);
			siteLocationCountryA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationCountryA);

		}

		

	}
	
	
	/**
	 * createSite A end LocationForExcel
	 * 
	 * @param site
	 * @param listBook
	 */
	private void createSiteZEndLocationForExcel(OrderIllSite orderIllSiteA, List<ExcelBean> listBook) throws TclCommonException {

		String siteALocation = returnApiAddressForSites(orderIllSiteA.getErfLocSitebLocationId());

		AddressDetail adDetailA = null;
		String crossConnect = CommonConstants.NO;

		if (siteALocation != null) {
			adDetailA = (AddressDetail) Utils.convertJsonToObject(siteALocation, AddressDetail.class);
		}

		if (adDetailA != null) {
			ExcelBean siteLocationAddressLine1A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
					OrderDetailsExcelDownloadConstants.SITE_Z_ADDRESS, adDetailA.getAddressLineOne());
			siteLocationAddressLine1A.setOrder(2);
			siteLocationAddressLine1A.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationAddressLine1A);

			ExcelBean siteLocationAddressLine2A = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
					OrderDetailsExcelDownloadConstants.SITE_Z_ADDRESS, adDetailA.getAddressLineTwo());
			siteLocationAddressLine2A.setOrder(2);
			siteLocationAddressLine2A.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationAddressLine2A);

			ExcelBean siteLocationCityA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
					OrderDetailsExcelDownloadConstants.SITE_Z_ADDRESS, adDetailA.getCity());
			siteLocationCityA.setOrder(2);
			siteLocationCityA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationCityA);

			ExcelBean siteLocationStateA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
					OrderDetailsExcelDownloadConstants.SITE_Z_ADDRESS, adDetailA.getState());
			siteLocationStateA.setOrder(2);
			siteLocationStateA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationStateA);

			ExcelBean siteLocationPincodeA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
					OrderDetailsExcelDownloadConstants.SITE_Z_ADDRESS, adDetailA.getPincode());
			siteLocationPincodeA.setOrder(2);
			siteLocationPincodeA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationPincodeA);

			ExcelBean siteLocationCountryA = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS_Z,
					OrderDetailsExcelDownloadConstants.SITE_Z_ADDRESS, adDetailA.getCountry());
			siteLocationCountryA.setOrder(2);
			siteLocationCountryA.setSiteAId(orderIllSiteA.getId());
			listBook.add(siteLocationCountryA);

		}

		ExcelBean siteCode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_CODE,orderIllSiteA.getSiteCode());
		siteCode.setOrder(1);
		siteCode.setLinkId(0);
		listBook.add(siteCode);
		
		ExcelBean rfsDate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.REQUESTED_DATE,
				orderIllSiteA.getRequestorDate() != null ? DateUtil.convertDateToSlashString(orderIllSiteA.getRequestorDate()) : "NA");
		rfsDate.setOrder(2);
		rfsDate.setLinkId(0);
		listBook.add(rfsDate);

	}
	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

	}
	
	
	/**
	 * createBillingComponentPrice
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createBillingComponentPriceNde(List<ExcelBean> listBook, OrderNplLink link) {
		List<MstProductComponent> componnetList=new ArrayList<MstProductComponent>();
		MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
				.findByName(OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY);
		componnetList.add(internetPortmstProductComponent);
		
		  MstProductComponent lastMilePortmstProductComponent =
		  mstProductComponentRepository .findByName(FPConstants.LAST_MILE.toString());
		  componnetList.add(lastMilePortmstProductComponent);
		 
		extractBandwidthComponentPrices(link, componnetList, listBook,
				OrderDetailsExcelDownloadConstants.BANDWITH_CHARGE_ARC_NRC);

		MstProductComponent linkMgmtmstProductComponent = mstProductComponentRepository
				.findByName(FPConstants.LINK_MANAGEMENT_CHARGES.toString());
		extractComponentNdePrices(link, linkMgmtmstProductComponent, listBook,
				OrderDetailsExcelDownloadConstants.MANAGEMENT_ARC_NRC);
		
		MstProductComponent shiftComponenet = mstProductComponentRepository
				.findByName(FPConstants.SHIFTING_CHARGES.toString());
		extractComponentNdePrices(link, shiftComponenet, listBook,
				OrderDetailsExcelDownloadConstants.SHIFTING_CHARGE_ARC_NRC);


	}
	
	
	/**
	 * extractComponentPrices
	 * 
	 * @param site
	 * @param mstProductComponent
	 */
	private void extractBandwidthComponentPrices(OrderNplLink link, List<MstProductComponent> ProductComponents,
			List<ExcelBean> listBook, String attrName) {
		LOGGER.info("INSIDE extractBandwidthComponentPrices"+link.getId());
		
		 Double totalArc= 0.0;
		 Double totalNrc= 0.0;
		for (MstProductComponent mstProductComponent : ProductComponents) {
			List<Integer> refIds = new ArrayList<Integer>();
			LOGGER.info("productCoponenet name"+mstProductComponent.getName());
			if (mstProductComponent.getName().equals(FPConstants.LAST_MILE.toString())) {
				refIds.add(link.getSiteAId());
				refIds.add(link.getSiteBId());
			} else {
				refIds.add(link.getId());
			}
			List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
					.findByReferenceIdInAndMstProductComponent(refIds, mstProductComponent);
			if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
				for(OrderProductComponent comp:orderProductComponents) {
					OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
							String.valueOf(comp.getId()), OrderDetailsExcelDownloadConstants.COMPONENTS);
					if (price != null) {
						LOGGER.info("compo arc:"+price.getEffectiveArc()+"compo nrc"+price.getEffectiveNrc()+totalArc+totalNrc);
						LOGGER.info("entered if");
						if(price.getEffectiveArc()!=null && price.getEffectiveNrc()!=null ) {
							LOGGER.info("entered second if arc"+price.getEffectiveArc()+"nrc"+price.getEffectiveNrc());
							totalArc = totalArc + price.getEffectiveArc();
							totalNrc = totalNrc + price.getEffectiveNrc();
							LOGGER.info("entered after addition Arc"+totalArc+"nrc"+totalNrc);
							}
					}
				}

			}
			LOGGER.info("loop ARC value:"+totalArc+"NDE value"+totalNrc);
		}
		
		LOGGER.info("NDE ARC:"+totalArc+"NDE NRC IN LR"+totalNrc);
		if((totalArc == 0.0 && totalNrc!=0.0) || (totalArc!=0.0 && totalNrc== 0.0) || (totalArc!=0.0 && totalNrc!=0.0) ) {
			LOGGER.info(" inside NDE ARC:"+totalArc+"NDE NRC IN LR"+totalNrc);
		  ExcelBean book36 = new
		  ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, attrName,
		  BigDecimal.valueOf(totalArc).toPlainString() + "," +
		  BigDecimal.valueOf(totalNrc).toPlainString());
		  book36.setOrder(4); book36.setLinkId(link.getId()); listBook.add(book36);
		}
		 
	}
	
	/**
	 * extractComponentPrices
	 * 
	 * @param site
	 * @param mstProductComponent
	 */
	private void extractComponentNdePrices(OrderNplLink link, MstProductComponent mstProductComponent,
			List<ExcelBean> listBook, String attrName) {
		List<Integer> refIds = new ArrayList();

		if (mstProductComponent.getName().equals(FPConstants.LAST_MILE.toString())) {
			refIds.add(link.getSiteAId());
			refIds.add(link.getSiteBId());
		} else
			refIds.add(link.getId());

		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdInAndMstProductComponent(refIds, mstProductComponent);

		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {

			orderProductComponents.forEach(comp -> {

				OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(String.valueOf(comp.getId()),
						OrderDetailsExcelDownloadConstants.COMPONENTS);

				if (price != null) {
					if((price.getEffectiveArc()!=0 && price.getEffectiveNrc()!=0) || (price.getEffectiveArc()!=0 && price.getEffectiveNrc()==0) || 
							(price.getEffectiveArc() == 0 && price.getEffectiveNrc() != 0)) {
						LOGGER.info("arc and nrc" + price.getEffectiveArc() + "nrc" + price.getEffectiveNrc());
						ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, attrName,
								BigDecimal.valueOf(price.getEffectiveArc()).toPlainString() + ","
										+ BigDecimal.valueOf(price.getEffectiveNrc()).toPlainString());
						book36.setOrder(4);
						book36.setLinkId(link.getId());
						listBook.add(book36);
					}
				}
			});

		}
	}

    private String getPopLocationbyLocId(Integer locationId) {

        try {
            LOGGER.info("Inside method getPopLocationbyLocId");
            String popLocationId = (String) mqUtils.sendAndReceive(popLocationQueue, String.valueOf(locationId));
            return popLocationId;

        }  catch (Exception e) {
            LOGGER.error("error in processing pop location queue{}", e);
        }
        return  null;
    }

    private Boolean findifPopTrueforSite(JSONObject dataEnvelopeObj,String attr) {
        Boolean isPopTrueforSite = false;
        if(dataEnvelopeObj.get(attr)!=null) {

            String popAttr = String.valueOf(dataEnvelopeObj.get(attr));
            if(popAttr.equalsIgnoreCase("1.0")) {
                isPopTrueforSite= true;
            }
        }
        return isPopTrueforSite;
    }


	/**
	 * createNDETerminationDetails
	 *
	 * @param listBook
	 * @param attributeValues
	 * @param orderToLe
	 */
	private void createNDETerminationDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
									  OrderToLe orderToLe, OrderNplLink link) {

		List<OrderIllSiteToService> orderIllSiteToServices= orderIllSiteToServiceRepository.findByOrderNplLink_Id(link.getId());
		if (!orderIllSiteToServices.isEmpty()) {
			OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetails = orderSiteServiceTerminationDetailsRepository.findByOrderIllSiteToService(orderIllSiteToServices.stream().findFirst().get());

			ExcelBean effectiveDoc = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.EFFECTIVE_DATE_OF_CHANGE, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getEffectiveDateOfChange().toString())
					? "" : orderSiteServiceTerminationDetails.getEffectiveDateOfChange().toString());
			effectiveDoc.setOrder(4);
			effectiveDoc.setSiteId(0);
			listBook.add(effectiveDoc);

			ExcelBean customerMailReceiveDate = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.CUSTOMER_MAIL_RECEIVED_DATE, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getCustomerMailReceivedDate().toString())
					? "" : orderSiteServiceTerminationDetails.getCustomerMailReceivedDate().toString());
			customerMailReceiveDate.setOrder(4);
			customerMailReceiveDate.setSiteId(0);
			listBook.add(customerMailReceiveDate);

			ExcelBean requestedDateOfTerm = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.REQUESTED_DATE_FOR_TERMINATION, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getRequestedDateForTermination().toString())
					? "" : orderSiteServiceTerminationDetails.getRequestedDateForTermination().toString());
			requestedDateOfTerm.setOrder(4);
			requestedDateOfTerm.setSiteId(0);
			listBook.add(requestedDateOfTerm);

			ExcelBean termSendToTDdate = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.TERMINATION_SENT_TO_TERMINATION_DESK_DATE, Objects.nonNull(orderSiteServiceTerminationDetails.getTerminationSendToTdDate())
					?  orderSiteServiceTerminationDetails.getTerminationSendToTdDate().toString() : "");
			termSendToTDdate.setOrder(4);
			termSendToTDdate.setSiteId(0);
			listBook.add(termSendToTDdate);

			ExcelBean communicationRecepient = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.COMMUNICATION_RECEPIENT, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getCommunicationReceipient())
					? "" : orderSiteServiceTerminationDetails.getCommunicationReceipient());
			communicationRecepient.setOrder(4);
			communicationRecepient.setSiteId(0);
			listBook.add(communicationRecepient);

			ExcelBean csmWorkedOnRetention = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.IS_CSM_RETENTION, "NO");  // TODO
			csmWorkedOnRetention.setOrder(4);
			csmWorkedOnRetention.setSiteId(0);
			listBook.add(csmWorkedOnRetention);

			ExcelBean csmName = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.CSM_NAME, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getCsmNonCsmName())
					? "" : orderSiteServiceTerminationDetails.getCsmNonCsmName());
			csmName.setSiteId(0);
			csmName.setOrder(4);
			listBook.add(csmName);

			ExcelBean internalCustomer = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.INTERNAL_CUSTOMER, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getInternalCustomer())
					? "" : orderSiteServiceTerminationDetails.getInternalCustomer());
			internalCustomer.setSiteId(0);
			internalCustomer.setOrder(4);
			listBook.add(internalCustomer);

			ExcelBean reasonForTerm = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.REASON_FOR_TERMINATION, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getReasonForTermination())
					? "" : orderSiteServiceTerminationDetails.getReasonForTermination());
			reasonForTerm.setSiteId(0);
			reasonForTerm.setOrder(4);
			listBook.add(reasonForTerm);

			ExcelBean subReason = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.SUB_REASON, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getSubReason())
					? "" : orderSiteServiceTerminationDetails.getSubReason());
			subReason.setSiteId(0);
			subReason.setOrder(4);
			listBook.add(subReason);

			ExcelBean subType = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.SUB_TYPE, StringUtils.isAllBlank(orderSiteServiceTerminationDetails.getTerminationSubtype())
					? "" : orderSiteServiceTerminationDetails.getTerminationSubtype());
			subType.setSiteId(0);
			subType.setOrder(4);
			listBook.add(subType);

			ExcelBean isEtcApplicable = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.ETC_APPLICABLE, orderSiteServiceTerminationDetails.getEtcApplicable().byteValue()==1 ? "Yes" : "No");
			isEtcApplicable.setSiteId(0);
			isEtcApplicable.setOrder(4);
			listBook.add(isEtcApplicable);

			ExcelBean etcCharges = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.ACTUAL_ETC_CHARGES, "0");  // setting default as 0 till we get etc charges from pricing for termination
			etcCharges.setSiteId(0);
			etcCharges.setOrder(4);
			listBook.add(etcCharges);

			ExcelBean isWaiverdone = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.WAIVER, "NO");  // setting default as No for now
			isWaiverdone.setSiteId(0);
			isWaiverdone.setOrder(4);
			listBook.add(isWaiverdone);

			ExcelBean etcAfterWaiver = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.ETC_TO_BE_CHARGED, "0");  // setting default as No for now till waiver implemented
			isWaiverdone.setSiteId(0);
			isWaiverdone.setOrder(4);
			listBook.add(isWaiverdone);

			ExcelBean waiverReason = new ExcelBean(OrderDetailsExcelDownloadConstants.TERMINATION_DETAILS,
					OrderDetailsExcelDownloadConstants.WAIVER_REASON, "");  // setting default as empty for now
			waiverReason.setSiteId(0);
			waiverReason.setOrder(4);
			listBook.add(waiverReason);


		}

	}

	
	/**
	 *updateGstNumber used to update
	 * GST number in multisitebillinginfo table
	 * @param gstno
	 * @param sitecode
	 */
	private void updateGstNumber(String gstNo,String siteCode,OrderIllSite orderIllSite) {
		LOGGER.info("ENTER into updateGstNumber::"+gstNo+"siteCode"+siteCode);
		try {
			if(gstNo!=null && siteCode!=null) {
				List<QuoteIllSite> site=illSiteRepository.findBySiteCodeAndStatus(siteCode, (byte)1);
				if(!site.isEmpty()) {
					QuoteSiteBillingDetails billinginfo=quoteSiteBillingInfoRepository.findByQuoteIllSite(site.get(0));
					if(billinginfo!=null) {
						LOGGER.info("inside if multisitebillinginfo id site level:::"+billinginfo.getId());
						billinginfo.setGstNo(gstNo);
						quoteSiteBillingInfoRepository.save(billinginfo);
					}
				}
				//order level gst updation
				if(orderIllSite!=null) {
					OrderSiteBillingDetails orderBillinginfo=orderSiteBillingInfoRepository.findByOrderIllSite(orderIllSite);
					if(orderBillinginfo!=null) {
						LOGGER.info("inside if multisitebillinginfo id order level:::"+orderBillinginfo.getId());
						orderBillinginfo.setGstNo(gstNo);
						orderSiteBillingInfoRepository.save(orderBillinginfo);
					}
				}
			}
			
		}catch (Exception ex){
			LOGGER.info("Error in updateGstNumber in illoredrservice : {}",ex);
		}
		
}

	private String getLlBwChange(Optional<OrderIllSite> illSiteOpt, OrderToLe orderToLe,String serviceId) throws TclCommonException {

		LOGGER.info("ILL SITE OPT :::: {}  for quote id :::: {}", illSiteOpt.get().getId());



		String bwUnitLl = illPricingFeasibilityService.getOldBandwidthUnit(serviceId,
				FPConstants.LOCAL_LOOP_BW_UNIT.toString());

		Double oldLlBw = Double.parseDouble(
				illPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString()));

		LOGGER.info("OLd local bw for IAS{}", oldLlBw);

		Double newLLBw = 0D;

		newLLBw = newLlAccToProdName(illSiteOpt, orderToLe, newLLBw);
		// Double.parseDouble(
		// illPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
		// FPConstants.LAST_MILE.toString(),
		// FPConstants.LOCAL_LOOP_BW.toString()));

		LOGGER.info("New local bw for product{} -> bandwidthvalue -> {}", newLLBw);

		oldLlBw = Double.parseDouble(illPricingFeasibilityService.setBandwidthConversion(oldLlBw.toString(), bwUnitLl));
		LOGGER.info("After Parsing in Double and converting unit, oldLLBw is {}", oldLlBw);

		String changeInLlBw = "";

		if (Objects.nonNull(oldLlBw) && Objects.nonNull(newLLBw)) {

			LOGGER.info(
					"Before Comparison, for changeinLLBW old bandwidth is " + oldLlBw + "new bandwidth is " + newLLBw);

			int result = newLLBw.compareTo(oldLlBw);

			if (result > 0)
				changeInLlBw = CommonConstants.UPGRADE;
			else if (result < 0)
				changeInLlBw = CommonConstants.DOWNGRADE;
			else if (result == 0)
				changeInLlBw = CommonConstants.EQUAL;

			/*
			 * if(oldLlBw.equals(newLLBw)){ changeInLlBw = CommonConstants.EQUAL; } else
			 * if(newLLBw>oldLlBw){ changeInLlBw = CommonConstants.UPGRADE; } else
			 * if(newLLBw<oldLlBw) changeInLlBw = CommonConstants.DOWNGRADE
			 */;
		}
		LOGGER.info("LL Bw inside getPortBwChange method{}", changeInLlBw);

		return changeInLlBw;
	}
	private Double newLlAccToProdName(Optional<OrderIllSite> illSiteOpt, OrderToLe orderToLe,
									  Double newLlBw) throws TclCommonException {

		LOGGER.info("Entering method newLlAccToProdName for Quote id{}", orderToLe.getOrder().getId());
		/** COLO Sites dont have Lastmile **/
		if (Objects.nonNull(illSiteOpt.get().getIsColo()) && illSiteOpt.get().getIsColo().equals((byte) 1)) {
			LOGGER.info("Colo sites doesnt have lastmile component");
			return newLlBw;
		}
		if (newLlBw == 0D) {
						LOGGER.info("IAS started for new port cal{}");
				newLlBw = Double.parseDouble(getNewBandwidthForOrder(illSiteOpt.get(),"Cross Connect", FPConstants.BANDWIDTH.toString(),PDFConstants.PRIMARY));
				LOGGER.info("New LL bw for NPL{}", newLlBw);

		}
		return newLlBw;
	}

	private String getNewBandwidthForOrder(OrderIllSite illSiteOpt, String componentName, String attributeName,String type) {
		LOGGER.info("Comp Name and Attribute Name{}",componentName+attributeName);
		OrderProductComponent quoteprodComp = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(illSiteOpt.getId(), componentName,type).stream().findFirst()
				.get();
		LOGGER.info("QuoteProductComponent Object {},and component id{}",quoteprodComp,quoteprodComp.getId());
		OrderProductComponentsAttributeValue attributeValue = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
				.stream().findFirst().get();
		LOGGER.info("Attr Value {}",attributeValue.getAttributeValues());
		return attributeValue.getAttributeValues();

	}
}
