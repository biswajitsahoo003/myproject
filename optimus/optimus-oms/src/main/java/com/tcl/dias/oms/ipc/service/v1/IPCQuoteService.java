package com.tcl.dias.oms.ipc.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.HYPHEN;
import static com.tcl.dias.common.constants.CommonConstants.IPC;
import static com.tcl.dias.common.constants.CommonConstants.IPC_DESC;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_SFDC;
import static com.tcl.dias.common.constants.LeAttributesConstants.OWNER_EMAIL_SFDC;
import static com.tcl.dias.common.constants.LeAttributesConstants.OWNER_NAME_SFDC;
import static com.tcl.dias.common.constants.LeAttributesConstants.REGION_SFDC;
import static com.tcl.dias.common.constants.LeAttributesConstants.TEAM_ROLE_SFDC;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_FORM;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LeOwnerDetailsSfdc;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
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
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteTncBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OdrAttachment;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderCloud;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteTnc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OdrAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderCloudRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteCloudRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTncRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ipc.beans.ProductSolutionBean;
import com.tcl.dias.oms.ipc.beans.QuoteBean;
import com.tcl.dias.oms.ipc.beans.QuoteToLeBean;
import com.tcl.dias.oms.ipc.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.ipc.beans.pricebean.CrossBorderBean;
import com.tcl.dias.oms.ipc.beans.pricebean.IpcDiscountBean;
import com.tcl.dias.oms.ipc.constants.IPCQuoteConstants;
import com.tcl.dias.oms.ipc.constants.OrderStagingConstants;
import com.tcl.dias.oms.ipc.macd.service.v1.IPCQuoteMACDService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the IPCQuoteService.java class. All the Quote related
 * Services for IPC will be implemented in this class
 *
 * get
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Primary
@Transactional
public class IPCQuoteService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IPCQuoteService.class);

	@Value("${notification.mail.admin}")
	private String adminRelativeUrl;

	@Value("${app.host}")
	private String appHost;

	@Value("${info.customer_le_location_queue}")
	private String customerLeLocationQueue;

	@Value("${rabbitmq.customerleattr.product.queue}")
	protected String customerLeAttrQueueProduct;

	@Value("${pricing.request.ipc.queue}")
	protected String pricingIPCRequestQueue;

	@Value("${pilot.team.email}")
	private String[] pilotTeamMail;

	@Value("${notification.mail.quotedashboard}")
	private String quoteDashBoardRelativeUrl;

	@Value("${rabbitmq.service.provider.detail}")
	private String spQueue;

	@Value("${rabbitmq.customerlename.queue}")
	private String customerLeName;
	
	@Value("${rabbitmq.pricing.ipc.location}")
	private String ipcLocation;

	private static final List<String> COMPUTES = Arrays.asList("L", "C", "G", "X", "M", "B", "H");

	private static final List<String> VARIANTS = Arrays.asList("Nickel", "Bronze", "Silver", "Cobalt", "Gold",
			"Platinum", "Titanium");

	public static final List<String> FLAVORS = new ArrayList<>();

	static {
		COMPUTES.forEach(compute -> VARIANTS.forEach(variant -> FLAVORS.add(compute + "." + variant)));
		FLAVORS.add(IPCQuoteConstants.CARBON_VM);
		FLAVORS.add(IPCQuoteConstants.SOLUTION_IPC_ACCESS);
		FLAVORS.add(IPCQuoteConstants.SOLUTION_IPC_ADDON);
		FLAVORS.add(IPCQuoteConstants.SOLUTION_IPC_COMMON);
		FLAVORS.add(IPCQuoteConstants.SOLUTION_IPC_DISCOUNT);
	}

	@Autowired
	private MQUtils mqUtils;

	@Autowired
	UserRepository userRepository;

	@Autowired
	EngagementRepository engagementRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;
	
	@Autowired
	QuoteTncRepository quoteTncRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	protected OmsSfdcService omsSfdcService;
	
	@Autowired
	OmsSfdcUtilService omsSfdcUtilService;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	QuoteCloudRepository quoteCloudRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	protected CofDetailsRepository cofDetailsRepository;

	@Autowired
	protected DocusignAuditRepository docusignAuditRepository;

	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	private IPCQuotePdfService ipcQuotePdfService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserInfoUtils userInfoUtils;

	@Autowired
	protected OrderRepository orderRepository;

	@Autowired
	protected OrderToLeRepository orderToLeRepository;

	@Autowired
	protected OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderCloudRepository orderCloudRepository;

	@Autowired
	protected QuoteDelegationRepository quoteDelegationRepository;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;
	
	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	IPCOrderService ipcOrderService;

	@Autowired
	private OmsUtilService omsUtilService;

	@Autowired
	private IPCPricingService ipcPricingService;

	@Value("${rabbitmq.customerle.queue}")
	private String customerLeQueue;

	@Value("${rabbitmq.pricing.ipc.crossborder.tax}")
	private String crossBorderTaxQueue;
	
	@Value("${rabbitmq.getOwnerDetailsSfdc.queue}")
	private String ownerDetailsQueue;
	
	@Autowired
	IPCQuoteMACDService ipcQuoteMACDService;
	
	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	CreditCheckService creditCheckService;

	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	OdrAttachmentRepository odrAttachmentRepository;
	
	/**
	 * createQuote - This method is used to create a quote The input validation
	 * is done and the corresponding tables are populated with initial set of
	 * values
	 *
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteResponse createQuote(QuoteDetail quoteDetail, Integer erfCustomerId) throws TclCommonException {
		QuoteResponse response = new QuoteResponse();
		try {
			LOGGER.info("inside ipc create quote service");
			validateQuoteDetail(quoteDetail);// validating the input for create
			// Quote
			User user = getUserId(Utils.getSource());
			QuoteToLe quoteTole = processQuote("CREATE", quoteDetail, erfCustomerId, user);
			persistQuoteLeAttributes(user, quoteTole, quoteDetail);
			if (quoteTole != null) {
				LOGGER.info("QuoteToLe not null");
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());
				if (quoteDetail.getQuoteId() == null && Objects.isNull(quoteDetail.getEngagementOptyId())) {
					// Triggering Sfdc Creation
					omsSfdcService.processCreateOpty(quoteTole, quoteDetail.getProductName());
				}
				ipcPricingService.processPricingRequest(quoteTole.getQuote().getId(), quoteTole.getId(), null);
			}
		} catch (Exception e) {
			LOGGER.error("error while creating a quote :: {}", e);
		}
		return response;
	}

	/**
	 * persistQuoteLeAttributes
	 *
	 * @param user
	 * @param quoteTole
	 */
	protected void persistQuoteLeAttributes(User user, QuoteToLe quoteTole, QuoteDetail quoteDetail) {
		LOGGER.info("IPCquoteService.persistQuoteLeAttributes method invoked");
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NAME, user.getFirstName());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_EMAIL, user.getEmailId());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_ID, user.getUsername());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NO, user.getContactNo());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.DESIGNATION, user.getDesignation());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.RECURRING_CHARGE_TYPE, "ARC");
		if(quoteDetail.getDcOrderId() != null) {
			updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.IPC_DC_ORDER_ID, quoteDetail.getDcOrderId());
		}
		LOGGER.info("IPCquoteService.persistQuoteLeAttributes method exited");
	}

	/**
	 * updateConstactInfo
	 *
	 * @param quoteTole
	 * @param userName
	 */
	public void updateLeAttribute(QuoteToLe quoteTole, String userName, String name, String value) {
		MstOmsAttribute mstOmsAttribute = null;

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
			mstOmsAttribute.setDescription(name);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		constructLegaAttribute(mstOmsAttribute, quoteTole, name, value);
	}

	/**
	 * @param mstOmsAttribute
	 * @param quoteTole
	 * @link http://www.tatacommunications.com/ constructLegaAttribute used to
	 *       construct legal attributes
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
	 * This method validates the Quote Details Request validateQuoteDetail
	 *
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	public void validateQuoteDetail(QuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null)) {
			// TODO validate the inputs for quote //NOSONAR
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * getCustomerId- This method persists the customer if not present or get
	 * the customer details if already present
	 *
	 * @param erfCustomerId
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
	 * getUserId-This method get the user details if present or persist the user
	 * and get the entity
	 *
	 * @param username
	 * @return User
	 */
	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * processQuote- This method builds the quote workflow step by step it
	 * creates by providing the initial set of values
	 *
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @return Quote
	 * @throws TclCommonException
	 */
	protected QuoteToLe processQuote(String requestType, QuoteDetail quoteDetail, Integer erfCustomerId, User user)
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
			quote = constructQuote(customer, user.getId(), quoteDetail.getProductName(), quoteDetail.getEngagementOptyId(), quoteDetail.getQuoteCode());
			quoteRepository.save(quote);
		} else {
			quote = quoteRepository.findByIdAndStatus(quoteDetail.getQuoteId(), BACTIVE);
		}
		QuoteToLe quoteToLe = null;
		if (quoteDetail.getQuoteId() == null) {
			quoteToLe = constructQuoteToLe(quote, quoteDetail.isMigrationOrder());
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				quoteToLe.setClassification(quoteDetail.getClassification());
			} else {
				quoteToLe.setClassification(IPCQuoteConstants.SELL_TO_CLASSIFICATION);
			}
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
		
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			quoteToLe.setQuoteToLeProductFamilies(
					Arrays.asList(quoteToLeProductFamily).stream().collect(Collectors.toSet()));
		}
		
		for (SolutionDetail solution : quoteDetail.getSolutions()) {
			String productOffering = solution.getOfferingName();
			MstProductOffering productOfferng = getProductOffering(productFamily, productOffering, user);
			LOGGER.info("Product Offering::" + productOfferng.getProductName());
			
			if(quoteToLe.getCurrencyCode() == null) {
				if (("EP_V2_MUM").equals(solution.getDcLocationId()) || ("EP_V2_DEL").equals(solution.getDcLocationId()) 
						|| ("EP_V2_BL").equals(solution.getDcLocationId())) {
					quoteToLe.setCurrencyCode(CommonConstants.INR);
				} else {
					quoteToLe.setCurrencyCode(CommonConstants.USD);
				}
				quoteToLeRepository.save(quoteToLe);
			}
			ProductSolution productSolution = productSolutionRepository
					.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
			if (productSolution == null) {
				LOGGER.info("Product solution doesn't exists");
				constructComponentDetail(productSolution, productOfferng, quoteToLeProductFamily, solution,
						productFamily, quoteToLe, user,quoteDetail);
			}
			LOGGER.info("Request Type: {}", requestType);
			if ((MACDConstants.UPGRADE_VM_SERVICE).equals(requestType) && Objects.nonNull(productSolution)) {
				QuoteCloud quoteCloud = constructQuoteCloud(productSolution, solution, quoteToLe, user);
				quoteCloudRepository.save(quoteCloud);
				LOGGER.info("Quote cloud saved successfully::" + quoteCloud.getCloudCode());
				removeComponentsAndAttr(quoteCloud.getId(), productFamily);
				if (Objects.nonNull(solution.getComponents())) {
					LOGGER.info("Solution components doeesn't exists");
					for (ComponentDetail componentDetail : solution.getComponents()) {
						processProductComponent(productFamily, quoteCloud.getId(), componentDetail, user);
					}
				}
			}
		}
		return quoteToLe;
	}

	private ProductSolution constructComponentDetail(ProductSolution productSolution, MstProductOffering productOfferng,
			QuoteToLeProductFamily quoteToLeProductFamily, SolutionDetail solution, MstProductFamily productFamily,
			QuoteToLe quoteToLe, User user, QuoteDetail quoteDetail) throws TclCommonException {
		productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
				Utils.convertObjectToJson(solution));
		productSolution.setSolutionCode(Utils.generateUid());
		productSolution.setProductProfileData(Utils.convertObjectToJson(solution));
		productSolutionRepository.save(productSolution);
		LOGGER.info("Product solution::" + productSolution.getId());
		QuoteCloud quoteCloud = constructQuoteCloud(productSolution, solution, quoteToLe, user);
		quoteCloudRepository.save(quoteCloud);
		LOGGER.info("Quote cloud saved successfully::" + quoteCloud.getCloudCode());
		removeComponentsAndAttr(quoteCloud.getId(), productFamily);
		if (Objects.nonNull(solution.getComponents())) {
			LOGGER.info("Solution components doeesn't exists");
			for (ComponentDetail componentDetail : solution.getComponents()) {
				processProductComponent(productFamily, quoteCloud.getId(), componentDetail, user);
			}
		}

		return productSolution;
	}

	/**
	 * constructQuote-This method constructs quote entity
	 *
	 * @param customer
	 * @param userId
	 * @return Quote
	 */
	private Quote constructQuote(Customer customer, Integer userId, String prodName, 
			String engagementOptyId, String quoteCode) {
		Quote quoteExisting = quoteCode != null ? quoteRepository.findByQuoteCode(quoteCode) : null;
		Quote quote = new Quote();
		if(quoteExisting != null){
			quote = quoteExisting;
		}
		quote.setCustomer(customer);
		quote.setCreatedBy(userId);
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setQuoteCode(null != engagementOptyId ? quoteCode : Utils.generateRefId(prodName));
		quote.setEngagementOptyId(engagementOptyId);
		return quote;
	}

	/**
	 * constructQuoteToLe -This method is used to construct QuoteToLe
	 *
	 * @param quote
	 * @return QuoteToLe
	 */
	private QuoteToLe constructQuoteToLe(Quote quote, boolean isMigrationOrder) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		quoteToLe.setTermInMonths("12 months");
		if(isMigrationOrder) {
			quoteToLe.setQuoteType("MIGRATION");
		} else {
			quoteToLe.setQuoteType("NEW");
		}
		return quoteToLe;
	}

	/**
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
	 * @param productSolutions
	 * @return Set<ProductSolutionBean>
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 */
	private List<ProductSolutionBean> constructProductSolution(List<ProductSolution> productSolutions)
			throws TclCommonException {
		List<ProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				boolean flag = true;
				ProductSolutionBean productSolutionBean = new ProductSolutionBean();
				productSolutionBean.setProductSolutionId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					flag = !(IPCQuoteConstants.SOLUTION_IPC_DISCOUNT
							.equalsIgnoreCase(solution.getMstProductOffering().getProductDescription()));
					productSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					productSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					productSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}
				if (solution.getProductProfileData() != null) {
					productSolutionBean.setCloudSolutions(constructProductSolutionDetails(solution));
				}
				if (flag) {
					productSolutionBeans.add(productSolutionBean);
				}
			}
		}
		return productSolutionBeans;
	}

	private List<com.tcl.dias.oms.ipc.beans.SolutionDetail> constructProductSolutionDetails(ProductSolution solution)
			throws TclCommonException {
		List<com.tcl.dias.oms.ipc.beans.SolutionDetail> ipcSolutions = new ArrayList<>();
		List<QuoteCloud> quoteClouds = quoteCloudRepository.findByProductSolutionAndStatus(solution, BACTIVE);
		com.tcl.dias.oms.ipc.beans.SolutionDetail solutionDetail;
		for (QuoteCloud quoteCloud : quoteClouds) {
			solutionDetail = new com.tcl.dias.oms.ipc.beans.SolutionDetail();
			solutionDetail.setCloudSolutionId(quoteCloud.getId());
			solutionDetail.setOfferingName(quoteCloud.getResourceDisplayName());
			solutionDetail.setDcCloudType(quoteCloud.getDcCloudType() != null ? quoteCloud.getDcCloudType() : "DC");
			solutionDetail.setDcLocationId(quoteCloud.getDcLocationId());
			solutionDetail.setComponents(constructProductComponentDetail(quoteCloud, solution));
			solutionDetail.setIsTaskTriggered(quoteCloud.getIsTaskTriggered());
			solutionDetail.setNrc(quoteCloud.getNrc());
			solutionDetail.setMrc(quoteCloud.getMrc());
			solutionDetail.setArc(quoteCloud.getArc());
			solutionDetail.setTcv(quoteCloud.getTcv());
			solutionDetail.setPpuRate(quoteCloud.getPpuRate());
			solutionDetail.setCloudCode(quoteCloud.getCloudCode());
			solutionDetail.setParentCloudCode(quoteCloud.getParentCloudCode());
			ipcSolutions.add(solutionDetail);
		}
		return ipcSolutions;
	}

	private List<ComponentDetail> constructProductComponentDetail(QuoteCloud quoteCloud, ProductSolution solution)
			throws TclCommonException {
		List<ComponentDetail> componentDetails = new ArrayList<>();
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductFamily(quoteCloud.getId(),
						solution.getMstProductOffering().getMstProductFamily());
		ComponentDetail componentDetail;
		for (QuoteProductComponent productComponent : productComponents) {
			componentDetail = new ComponentDetail();
			componentDetail.setComponentId(productComponent.getId());
			componentDetail.setComponentMasterId(productComponent.getMstProductComponent().getId());
			componentDetail.setName(productComponent.getMstProductComponent().getName());
			componentDetail.setType(productComponent.getType());
			QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(productComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (Objects.nonNull(price)) {
				componentDetail.setQuotePriceId(price.getId());
				componentDetail.setArc(price.getEffectiveArc());
				componentDetail.setMrc(price.getEffectiveMrc());
				componentDetail.setNrc(price.getEffectiveNrc());
			}
			componentDetail.setAttributes(constructProductComponentAttributeDetail(productComponent));
			componentDetails.add(componentDetail);
		}
		return componentDetails;
	}

	private List<AttributeDetail> constructProductComponentAttributeDetail(QuoteProductComponent productComponent) {
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent(productComponent);
		AttributeDetail attributeDetail;
		for (QuoteProductComponentsAttributeValue attributeValue : attributeValues) {
			attributeDetail = new AttributeDetail();
			attributeDetail.setAttributeId(attributeValue.getId());
			attributeDetail.setAttributeMasterId(attributeValue.getProductAttributeMaster().getId());
			attributeDetail.setName(attributeValue.getProductAttributeMaster().getName());
			attributeDetail.setValue(attributeValue.getAttributeValues());
			attributeDetails.add(attributeDetail);
		}
		return attributeDetails;
	}

	/**
	 * constructQuote-This method constructs quote cloud
	 *
	 * @param productSolution
	 * @param solution
	 * @param quoteToLe
	 * @param user
	 * @return QuoteCloud
	 * @throws TclCommonException 
	 */
	private QuoteCloud constructQuoteCloud(ProductSolution productSolution, SolutionDetail solution,
			QuoteToLe quoteToLe, User user) throws TclCommonException {
		LOGGER.info("IPCQuoteService.constructQuoteCloud invoked");
		MstProductFamily mstProductFamily = getProductFamily(IPC);
		QuoteCloud quoteCloud = new QuoteCloud();
		quoteCloud.setCloudCode(Utils.generateUid());
		if (Objects.nonNull(solution.getParentCloudCode())) {
			quoteCloud.setParentCloudCode(solution.getParentCloudCode());
		}
		quoteCloud.setProductSolution(productSolution);
		quoteCloud.setQuoteId(quoteToLe.getQuote().getId());
		quoteCloud.setQuoteToLeId(quoteToLe.getId());
		quoteCloud.setDcCloudType(solution.getDcCloudType() != null ? solution.getDcCloudType() : "DC");
		quoteCloud.setMstProductFamily(mstProductFamily);
		quoteCloud.setDcLocationId(solution.getDcLocationId());
		quoteCloud.setResourceDisplayName(solution.getOfferingName());
		quoteCloud.setArc(0d);
		quoteCloud.setMrc(0d);
		quoteCloud.setNrc(0d);
		quoteCloud.setTcv(0d);
		quoteCloud.setFpStatus(FPStatus.P.toString());
		quoteCloud.setIsTaskTriggered(0);
		quoteCloud.setCreatedBy(user.getId());
		quoteCloud.setCreatedTime(new Date());
		quoteCloud.setUpdatedBy(user.getId());
		quoteCloud.setUpdatedTime(new Date());
		quoteCloud.setStatus((byte) 1);
		LOGGER.info("IPCQuoteService.constructQuoteCloud ends");
		return quoteCloud;
	}

	/**
	 * getProductFamily - This methods gets the {@link MstProductFamily} from
	 * the given product name
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
		for (ProductSolution exproductSolution : exprodSolutions) {
			boolean remove = true;
			for (SolutionDetail solution : quoteDetail.getSolutions()) {
				if (solution.getOfferingName().equals(exproductSolution.getMstProductOffering().getProductName())) {
					remove = false;
					break;
				}
			}
		}
	}

	/**
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
	 * removeComponentsAndAttr
	 *
	 * @param quoteCloudId
	 */
	private void removeComponentsAndAttr(Integer quoteCloudId, MstProductFamily mstProductFamily) {
		LOGGER.info("IPCQuoteService.removeComponentsAndAttr invoked::QuoteCloud::" + quoteCloudId + "Pfamily::"
				+ mstProductFamily.getId());
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductFamily(quoteCloudId, mstProductFamily);
		if (Objects.nonNull(quoteProductComponents) && !quoteProductComponents.isEmpty()) {
			LOGGER.info("QPC exists");
			quoteProductComponents.forEach(quoteProd -> {
				quoteProd.getQuoteProductComponentsAttributeValues()
						.forEach(attr -> quoteProductComponentsAttributeValueRepository.delete(attr));
				quoteProductComponentRepository.delete(quoteProd);
			});
		}
		LOGGER.info("IPCQuoteService.removeComponentsAndAttr exited");
	}

	/**
	 * processProductComponent- This method process the product component
	 * details
	 *
	 * @param productFamily
	 * @param quoteCloud
	 * @param component
	 * @param user
	 * @throws TclCommonException
	 */
	private void processProductComponent(MstProductFamily productFamily, Integer quoteCloudId,
			ComponentDetail component, User user) throws TclCommonException {
		LOGGER.info("IPCQuoteService.processProductComponent invoked");
		try {
			MstProductComponent productComponent = getProductComponent(component, user);

			QuoteProductComponent quoteComponent = null;
			if (component.getComponentId() != null) {
				Optional<QuoteProductComponent> quoteComponentOpt = quoteProductComponentRepository
						.findById(component.getComponentId());
				quoteComponent = quoteComponentOpt.get();
			}

			if (quoteComponent == null) {
				quoteComponent = constructProductComponent(productComponent, productFamily, quoteCloudId);
				quoteProductComponentRepository.save(quoteComponent);
			}

			LOGGER.info("saved successfully");
			for (AttributeDetail attribute : component.getAttributes()) {
				processProductAttribute(quoteComponent, attribute, user);
			}
			LOGGER.info("IPCQuoteService.processProductComponent exited");
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * getProductComponent- This method takes the component name and gives the
	 * {@link MstProductComponent}
	 *
	 * @param user
	 * @param component
	 * @return MstProductComponent
	 * @throws TclCommonException
	 */
	public MstProductComponent getProductComponent(ComponentDetail component, User user) {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(component.getName(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setName(component.getName());
			mstProductComponent.setDescription(component.getName());
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		return mstProductComponent;
	}

	/**
	 * constructProductComponent- This method constructs the
	 * {@link QuoteProductComponent} Entity
	 *
	 * @param productComponent
	 * @param mstProductFamily
	 * @param quoteCloudId
	 * @return QuoteProductComponent
	 * @throws TclCommonException
	 */
	private QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer quoteCloudId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(quoteCloudId);
		LOGGER.debug("In constructProductComponent - setting the reference name.");
		quoteProductComponent.setReferenceName(QuoteConstants.IPCCLOUD.toString());
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
	protected void processProductAttribute(QuoteProductComponent quoteComponent, AttributeDetail attribute, User user)
			throws TclCommonException {
		try {
			ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, productAttribute);
			if (quoteProductComponentsAttributeValues != null && !quoteProductComponentsAttributeValues.isEmpty()) {
				QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
						.get(0);
				quoteProductComponentsAttributeValue.setAttributeValues(attribute.getValue());
				quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
			} else {
				QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
						productAttribute, attribute);
				quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
			}
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.INVALID_ATTRIBUTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * getProductAttributes-This methods takes the attributeName and gets back
	 * {@link ProductAttributeMaster}
	 *
	 * @param user
	 * @param attributeDetail
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
	 * @param quoteId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/ getQuoteDetails- This method is
	 *       used to get the quote details
	 */
	public QuoteBean getQuoteDetails(Integer quoteId) throws TclCommonException {
		QuoteBean response = null;
		try {
			validateGetQuoteDetail(quoteId);
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote);
			// PIPF-212
			getLeOwnerDetailsSFDC(response, quote);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * validateGetQuoteDetail
	 *
	 * @param quoteId
	 * @throws TclCommonException
	 */
	protected void validateGetQuoteDetail(Integer quoteId) throws TclCommonException {
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	protected Quote getQuote(Integer quoteId) throws TclCommonException {
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quote == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quote;
	}

	/**
	 * @param quote
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com constructQuote
	 */
	protected QuoteBean constructQuote(Quote quote) throws TclCommonException {
		QuoteBean quoteDto = new QuoteBean();
		quoteDto.setQuoteId(quote.getId());
		quoteDto.setQuoteCode(quote.getQuoteCode());
		quoteDto.setCreatedBy(quote.getCreatedBy());
		quoteDto.setCreatedTime(quote.getCreatedTime());
		quoteDto.setStatus(quote.getStatus());
		if (Objects.nonNull(quote.getQuoteToLes())) {
			Set<QuoteToLe> quoteToLeSet = quote.getQuoteToLes();
			String quoteType = quoteToLeSet.stream().findFirst().get().getQuoteType();
			quoteDto.setTermInMonths(quoteToLeSet.stream().findFirst().get().getTermInMonths());
			if (quoteType.equals("NEW")) {
				quoteDto.setQuoteType(quoteToLeSet.stream().findFirst().get().getQuoteType());
			} else {
				quoteDto.setQuoteType(quoteToLeSet.stream().findFirst().get().getQuoteType());
				quoteDto.setQuoteCategory(quoteToLeSet.stream().findFirst().get().getQuoteCategory());
				quoteDto.setServiceId(quoteToLeSet.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
			}
		}

		if (quote.getCustomer() != null) {
			quoteDto.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		}
		quoteDto.setLegalEntities(constructQuoteLeEntitDtos(quote));

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
		return quoteDto;
	}

	/**
	 * @param quote
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 */
	private Set<QuoteToLeBean> constructQuoteLeEntitDtos(Quote quote) throws TclCommonException {
		Set<QuoteToLeBean> quoteToLeDtos = new HashSet<>();
		if ((quote != null) && (getQuoteToLeBasenOnVersion(quote)) != null) {
			for (QuoteToLe quTle : getQuoteToLeBasenOnVersion(quote)) {
				QuoteToLeBean quoteToLeDto = new QuoteToLeBean(quTle);
				quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
				quoteToLeDto.setCurrency(quTle.getCurrencyCode());
				quoteToLeDto.setLegalAttributes(constructLegalAttributes(quTle));
				quoteToLeDto
						.setProductFamilies(constructQuoteToLeFamilyDtos(getProductFamilyBasenOnVersion(quTle), quTle));
				quoteToLeDto.setFinalArc(quTle.getFinalArc());
				quoteToLeDto.setFinalMrc(quTle.getFinalMrc());
				quoteToLeDto.setFinalNrc(quTle.getFinalNrc());
				quoteToLeDto.setTotalTcv(quTle.getTotalTcv());
				quoteToLeDtos.add(quoteToLeDto);
			}
		}
		return quoteToLeDtos;
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	protected List<QuoteToLe> getQuoteToLeBasenOnVersion(Quote quote) {
		List<QuoteToLe> quToLes = null;
		quToLes = quoteToLeRepository.findByQuote(quote);
		return quToLes;
	}

	/**
	 * @param quTle
	 * @return
	 * @link http://www.tatacommunications.com constructLegalAttributes used to
	 *       construct legal attributes
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
	 * @param quoteToLeProductFamilies
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/ constructQuoteToLeFamilyDtos
	 */
	private Set<QuoteToLeProductFamilyBean> constructQuoteToLeFamilyDtos(
			List<QuoteToLeProductFamily> quoteToLeProductFamilies, QuoteToLe quTle) throws TclCommonException {
		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilyBeans = new HashSet<>();
		if (quoteToLeProductFamilies != null) {
			for (QuoteToLeProductFamily quFamily : quoteToLeProductFamilies) {
				QuoteToLeProductFamilyBean quoteToLeProductFamilyBean = new QuoteToLeProductFamilyBean();
				if (quFamily.getMstProductFamily() != null) {
					quoteToLeProductFamilyBean.setStatus(quFamily.getMstProductFamily().getStatus());
					quoteToLeProductFamilyBean.setProductName(quFamily.getMstProductFamily().getName());
				}
				List<ProductSolutionBean> solutionBeans = getSortedSolution(
						constructProductSolution(getProductSolutionBasenOnVersion(quFamily)));
				quoteToLeProductFamilyBean.setSolutions(solutionBeans);
				quoteToLeProductFamilyBeans.add(quoteToLeProductFamilyBean);
			}
		}
		return quoteToLeProductFamilyBeans;
	}

	private List<ProductSolutionBean> getSortedSolution(List<ProductSolutionBean> solutionBeans) {
		if (Objects.nonNull(solutionBeans)) {
			Collections.sort(solutionBeans, (final ProductSolutionBean o1, final ProductSolutionBean o2) -> {
				Integer firstIndex = FLAVORS.indexOf(o1.getOfferingName());
				Integer secondIndex = FLAVORS.indexOf(o2.getOfferingName());
				return firstIndex.compareTo(secondIndex);
			});
		}
		return solutionBeans;
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	private List<ProductSolution> getProductSolutionBasenOnVersion(QuoteToLeProductFamily family) {
		List<ProductSolution> productSolutions = null;
		productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(family);
		return productSolutions;
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	private List<QuoteToLeProductFamily> getProductFamilyBasenOnVersion(QuoteToLe quote) {
		List<QuoteToLeProductFamily> prodFamilys = null;
		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		return prodFamilys;
	}

	/**
	 * getPublicIp
	 */
	public String getPublicIp(String publicIp) {
		if (publicIp != null) {
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
	 * @param cloudId
	 * @return
	 * @link http://www.tatacommunications.com
	 */
	public boolean processDeactivateCloudProducts(Integer quoteLeId, Integer cloudId) throws TclCommonException {
		boolean response = false;
		try {
			QuoteCloud quoteCloud = quoteCloudRepository.findByIdAndQuoteToLeIdAndStatus(cloudId, quoteLeId, (byte) 1);
			if (quoteCloud != null) {
				quoteCloudRepository.delete(quoteCloud);
				response = true;
				ipcPricingService.processPricingRequest(quoteCloud.getQuoteId(), quoteCloud.getQuoteToLeId(), null);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	public QuoteToLe addQuoteCloudProduct(com.tcl.dias.oms.ipc.beans.QuoteCloud quoteDetail, Integer quoteId,
			Integer quoteLeId, Integer erfCustomerId) throws TclCommonException {
		User user = getUserId(Utils.getSource());
		Customer customer = null;
		if (erfCustomerId != null) {
			customer = getCustomerId(erfCustomerId);// get the customer Id
		} else {
			customer = user.getCustomer();
		}
		Quote quote = null;
		// Checking whether the input is for creating or updating
		if (quoteLeId == null && quoteId == null) {
			quote = constructQuote(customer, user.getId(), quoteDetail.getProductName(), null, null);
			quoteRepository.save(quote);
		} else {
			quote = quoteRepository.findByIdAndStatus(quoteId, BACTIVE);
		}
		QuoteToLe quoteToLe = null;
		if (quoteId == null) {
			quoteToLe = constructQuoteToLe(quote, false);
			quoteToLeRepository.save(quoteToLe);
		} else {
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
			quoteToLe = quoteToLeEntity.isPresent() ? quoteToLeEntity.get() : null;
		}

		MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
		if (quoteToLeProductFamily == null) {
			quoteToLeProductFamily = constructQuoteToLeProductFamily(productFamily, quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		}
		/*
		 * } else { removeUnselectedSolution(quoteDetail,
		 * quoteToLeProductFamily, quoteToLe); }
		 */
		for (SolutionDetail solution : quoteDetail.getSolutions()) {
			String productOffering = solution.getOfferingName();
			MstProductOffering productOfferng = getProductOffering(productFamily, productOffering, user);
			ProductSolution productSolution = productSolutionRepository
					.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
			if (productSolution == null) {
				productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
						Utils.convertObjectToJson(solution));
				productSolution.setSolutionCode(Utils.generateUid());
				productSolution.setProductProfileData(Utils.convertObjectToJson(solution));
				productSolutionRepository.save(productSolution);
			}
			QuoteCloud quoteCloud = constructQuoteCloud(productSolution, solution, quoteToLe, user);
			quoteCloudRepository.save(quoteCloud);

			removeComponentsAndAttr(quoteCloud.getId(), productFamily);
			for (ComponentDetail componentDetail : solution.getComponents()) {
				processProductComponent(productFamily, quoteCloud.getId(), componentDetail, user);
			}

			if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId()) && 
					!productSolution.getProductProfileData().contains(IPCQuoteConstants.SOLUTION_IPC_DISCOUNT)) {
				productSolutionRepository.save(productSolution);
			}

		}
		ipcPricingService.processPricingRequest(quote.getId(), quoteToLe.getId(), null);
		return quoteToLe;
	}

	/**
	 * @param quoteLeId
	 * @param solutions
	 * @return
	 * @link http://www.tatacommunications.com
	 */
	public boolean processDeactivateCloudSolution(Integer quoteId, Integer quoteLeId, List<Integer> solutions)
			throws TclCommonException {
		boolean response = false;
		try {
			for (Integer solution : solutions) {
				Optional<ProductSolution> productSolutionEntity = productSolutionRepository.findById(solution);
				ProductSolution productSolution = productSolutionEntity.isPresent() ? productSolutionEntity.get()
						: null;
				List<QuoteCloud> quoteClouds = quoteCloudRepository.findByProductSolution(productSolution);
				/*
				 * for (QuoteCloud quoteCloud : quoteClouds) {
				 * quoteCloud.setStatus((byte) 0); }
				 */
				quoteCloudRepository.deleteAll(quoteClouds);
				productSolutionRepository.delete(productSolution);
			}
			response = true;
			ipcPricingService.processPricingRequest(quoteId, quoteLeId, null);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * This method validates the Quote Details Request validateQuoteDetail
	 *
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	public void validateSolutionDetail(com.tcl.dias.oms.ipc.beans.SolutionDetail solutionDetail)
			throws TclCommonException {
		if (solutionDetail == null || solutionDetail.getCloudSolutionId() == null
				|| solutionDetail.getComponents() == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_SOLUTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * addUpdateComponentsAndAttr used to add component and its attribute to the
	 * quote
	 *
	 * @param quoteId
	 * @param componentDetail
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteBean addUpdateComponentsAndAttr(Integer quoteId,
			List<com.tcl.dias.oms.ipc.beans.SolutionDetail> solutionDetails) throws TclCommonException {
		User user = getUserId(Utils.getSource());
		MstProductFamily productFamily = getProductFamily(IPC);
		if (!solutionDetails.isEmpty()) {
			for (com.tcl.dias.oms.ipc.beans.SolutionDetail solutionDetail : solutionDetails) {
				validateSolutionDetail(solutionDetail);
				for (ComponentDetail componentDetail : solutionDetail.getComponents()) {
					processProductComponent(productFamily, solutionDetail.getCloudSolutionId(), componentDetail, user);
				}
			}
		}
		return getQuoteDetails(quoteId);
	}

	/**
	 * addUpdateComponentsAndAttr used to add component and its attribute to the
	 * quote
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param componentDetail
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteBean addUpdateComponentsAndAttr(Integer quoteId, Integer solutionId,
			String serviceId, List<ComponentDetail> componentDetails) throws TclCommonException {
		User user = getUserId(Utils.getSource());
		MstProductFamily productFamily = getProductFamily(IPC);
		for (ComponentDetail componentDetail : componentDetails) {
			processProductComponent(productFamily, solutionId, componentDetail, user);
		}
		Quote quote = getQuote(quoteId);
		List<QuoteToLe> quoteToLe = getQuoteToLeBasenOnVersion(quote);
		if (quoteToLe != null && !quoteToLe.isEmpty()) {
			ipcPricingService.processPricingRequest(quoteId, quoteToLe.get(0).getId(), serviceId);
		}
		return getQuoteDetails(quoteId);
	}

	/**
	 * removeComponentsAndAttr used to remove component and its attribute to the
	 * quote
	 *
	 * @param quoteId
	 * @param List<componentDetail>
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteBean removeComponentsAndAttr(Integer quoteId, List<Integer> componentId) throws TclCommonException {
		if (null != componentId && !componentId.isEmpty()) {
			//remove QuotePrice entry incase of IPC-Addon
			List<QuotePrice> quotePriceLst = quotePriceRepository.findByReferenceIdIn(componentId.stream().map(s -> String.valueOf(s)).collect(Collectors.toList()));
			if(!quotePriceLst.isEmpty()) {
				quotePriceLst.forEach(quotePrice -> quotePriceRepository.delete(quotePrice));
			}
			List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
					.findAllById(componentId);
			if (!quoteProductComponents.isEmpty()) {
				quoteProductComponents.forEach(quoteProd -> {
					quoteProd.getQuoteProductComponentsAttributeValues()
							.forEach(attr -> quoteProductComponentsAttributeValueRepository.delete(attr));
					quoteProductComponentRepository.delete(quoteProd);
				});
				Quote quote = getQuote(quoteId);
				List<QuoteToLe> quoteToLe = getQuoteToLeBasenOnVersion(quote);
				ipcPricingService.processPricingRequest(quoteId, quoteToLe.get(0).getId(), null);
			}
		}
		return getQuoteDetails(quoteId);
	}

	/**
	 * addUpdateAttributes used to add component and its attribute to the quote
	 *
	 * @param quoteId
	 * @param componentId
	 * @param attributeDetail
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteBean addUpdateAttributes(Integer quoteId, Integer componentId, AttributeDetail attribute)
			throws TclCommonException {
		User user = getUserId(Utils.getSource());
		Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository.findById(componentId);
		if (quoteProductComponent.isPresent()) {
			QuoteProductComponent quoteComponent = quoteProductComponent.get();
			processProductAttribute(quoteComponent, attribute, user);
		}
		return getQuoteDetails(quoteId);
	}

	/**
	 * addUpdateAttributes used to add component and its attribute to the quote
	 *
	 * @param quoteId
	 * @param List<attributeId>
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteBean removeAttributes(Integer quoteId, List<Integer> attributeId) throws TclCommonException {
		if (null != attributeId && !attributeId.isEmpty()) {
			quoteProductComponentsAttributeValueRepository.deleteAllByIdIn(attributeId);
		}
		return getQuoteDetails(quoteId);
	}

	/**
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
				ipcPricingService.processPricingRequest(quoteId, quoteToLeId, null);
			}
		}
	}

	/**
	 * processMailAttachment method is used to prepare the quote PDF for mail
	 * attachment.
	 *
	 * @param email
	 * @param quoteId
	 * @return ServiceResponse
	 * @throws TclCommonException
	 */
	public ServiceResponse processMailAttachment(String email, Integer quoteId) throws TclCommonException {
		ServiceResponse fileUploadResponse = new ServiceResponse();
		if (Objects.isNull(email) || !Utils.isValidEmail(email)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String quoteHtml = ipcQuotePdfService.processQuoteHtml(quoteId);
			String fileName = "Quote_" + quoteId + ".pdf";
			notificationService.processShareQuoteNotification(email,
					Base64.getEncoder().encodeToString(quoteHtml.getBytes()), userInfoUtils.getUserFullName(), fileName,
					IPC);
			fileUploadResponse.setStatus(Status.SUCCESS);
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * @return ResponseResource
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 */
	@Transactional
	public QuoteDetail approvedQuotes(UpdateRequest request, String ipAddress) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Map<String, String> cofObjectMapper = new HashMap<>();
			CofDetails cofDetail = cofDetailsRepository.findByOrderUuid(quote.getQuoteCode());
			if (cofDetail != null) {
				cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetail.getUriPath());
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
					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
					Boolean nat = (request.getCheckList() == null
							|| request.getCheckList().equalsIgnoreCase(CommonConstants.NO)) ? Boolean.FALSE
									: Boolean.TRUE;
					ipcQuotePdfService.processCofPdf(quote.getId(), null, nat, true, quoteLe.getId(), cofObjectMapper);
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
					// uploadSSIfNotPresent(quoteLe);
					/**
					 * commented due to requirement change for MSA mapping while optimus journey
					 */
					// uploadMSAIfNotPresent(quoteLe);
				}

				for (OrderToLe orderToLe : order.getOrderToLes()) {
					List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
							.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quote.getId(),
									AttachmentTypeConstants.COF.toString());
					for (OmsAttachment omsAttachment : omsAttachmentList) {
						omsAttachment.setOrderToLe(orderToLe);
						omsAttachment.setReferenceName(CommonConstants.ORDERS);
						omsAttachment.setReferenceId(order.getId());
						omsAttachmentRepository.save(omsAttachment);
					}
					ipcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),
							cofObjectMapper);
					break;
				}
			}

			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}
			}

			for (OrderToLe orderToLe : order.getOrderToLes()) {
				LOGGER.info("Order Type: {}, Order Category: {}", orderToLe.getOrderType(), orderToLe.getOrderCategory());
				if (MACDConstants.MACD_QUOTE_TYPE.equals(orderToLe.getOrderType())
						&& MACDConstants.DELETE_VM.equals(orderToLe.getOrderCategory())) {
					processAttachmentsFromQuote(request.getQuoteId(), orderToLe);
					ipcOrderService.launchCloud(order.getId(), orderToLe.getId(), false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	protected void processAttachmentsFromQuote(Integer quoteId, OrderToLe orderToLe) {
		List<OmsAttachment> omsAttach = omsAttachmentRepository.findByReferenceNameAndReferenceId(CommonConstants.QUOTES, quoteId);
		for(OmsAttachment omsAttachment : omsAttach) {
			omsAttachment.setOrderToLe(orderToLe);
			omsAttachment.setReferenceName(CommonConstants.ORDERS);
			omsAttachment.setReferenceId(orderToLe.getOrder().getId());
			omsAttachmentRepository.save(omsAttachment);
		}
		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId);
		if (!quoteToLe.isEmpty() && MACDConstants.MACD_QUOTE_TYPE.equals(orderToLe.getOrderType())) {
			MacdDetail macdDtl = macdDetailRepository.findByQuoteToLeId(quoteToLe.get(0).getId());
			if(null != macdDtl) {
				OdrAttachment odrAttachment = odrAttachmentRepository
						.findByServiceCodeAndProductNameOrderByIdDesc(macdDtl.getTpsServiceId(), CommonConstants.IPC);
				if(odrAttachment != null) {
					odrAttachment.setOrderId(orderToLe.getOrder().getId());
					odrAttachmentRepository.save(odrAttachment);
				}
			}
		}
	}

	protected void processOrderMailNotification(Order order, QuoteToLe quoteToLe, Map<String, String> cofObjectMapper,
			String userEmail) throws TclCommonException {
		String emailId = userEmail != null ? userEmail : customerSupportEmail;
		String leMail = getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),
				emailId, appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName, IPC_DESC, quoteToLe);
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
			populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	private MailNotificationBean populatePartnerClassification(QuoteToLe quoteToLe,
			MailNotificationBean mailNotificationBean) {
		try {
			mailNotificationBean.setClassification(quoteToLe.getClassification());
			String mqResponse = (String) mqUtils.sendAndReceive(customerLeName,
					String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = Utils.convertJsonToObject(mqResponse,
					CustomerLegalEntityDetailsBean.class);
			customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny().ifPresent(customerLeBean -> {
				String endCustomerLegalEntityName = customerLeBean.getLegalEntityName();
				LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);
				mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
			});
		} catch (Exception e) {
			LOGGER.warn("Error while reading end customer name :: {}", e.getMessage());
		}
		return mailNotificationBean;
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
	 * @param quote
	 * @param detail
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com constructQuote
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
		order.setStage(OrderStagingConstants.ORDER_CREATED.name());
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
	 * @param quote
	 * @param order
	 * @param detail
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 */
	private Set<OrderToLe> constructOrderToLe(Quote quote, Order order, QuoteDetail detail) throws TclCommonException {
		return getOrderToLeBasenOnVersion(quote, order, detail);
	}

	/**
	 * @param order
	 * @param detail
	 * @link http://www.tatacommunications.com/
	 */
	private Set<OrderToLe> getOrderToLeBasenOnVersion(Quote quote, Order order, QuoteDetail detail)
			throws TclCommonException {
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
				orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
				orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
				orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
				orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
				orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
				orderToLe.setStage(OrderStagingConstants.ORDER_CREATED.getSubStage());
				orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
				orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setTpsSfdcParentOptyId(quoteToLe.getTpsSfdcParentOptyId());
				orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
				orderToLe.setSourceSystem(quoteToLe.getSourceSystem());
				orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
				orderToLe.setClassification(quoteToLe.getClassification());
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
	 * @param order
	 * @param detail
	 * @link http://www.tatacommunications.com/ constructOrderToLeAttribute use
	 *       for constructing attribute value
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
	 * @param orderToLe
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 */
	private Set<OrderToLeProductFamily> getOrderProductFamilyBasenOnVersion(QuoteToLe quote, OrderToLe orderToLe,
			QuoteDetail detail) throws TclCommonException {
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
				quoteToLeProductFamily.getMstProductFamily(), BACTIVE);
		if (engagements == null || engagements.isEmpty()) {
			Engagement engagement = new Engagement();
			engagement.setCustomer(quote.getQuote().getCustomer());
			engagement.setEngagementName(quoteToLeProductFamily.getMstProductFamily().getName() + HYPHEN
					+ quote.getErfCusCustomerLegalEntityId());
			engagement.setErfCusCustomerLeId(quote.getErfCusCustomerLegalEntityId());
			engagement.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
			engagement.setStatus(BACTIVE);
			engagement.setCreatedTime(new Date());
			engagementRepository.save(engagement);
		}
	}

	/**
	 * @param productSolutions
	 * @param orderToLeProductFamily
	 * @return Set<ProductSolutionBean>
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 */
	private Set<OrderProductSolution> constructOrderProductSolution(Set<ProductSolution> productSolutions,
			OrderToLeProductFamily orderToLeProductFamily, QuoteDetail detail) throws TclCommonException {

		Set<OrderProductSolution> orderProductSolution = new HashSet<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				List<QuoteCloud> quoteClouds = getQuoteCloudBasedOnVersion(solution);
				if (quoteClouds != null && !quoteClouds.isEmpty()) {
					OrderProductSolution oSolution = new OrderProductSolution();
					if (solution.getMstProductOffering() != null) {
						oSolution.setMstProductOffering(solution.getMstProductOffering());
					}
					oSolution.setSolutionCode(solution.getSolutionCode());
					oSolution.setTpsSfdcProductId(solution.getTpsSfdcProductId());
					oSolution.setTpsSfdcProductName(solution.getTpsSfdcProductName());
					oSolution.setOrderToLeProductFamily(orderToLeProductFamily);
					orderProductSolutionRepository.save(oSolution);
					constructOrderCloud(quoteClouds, oSolution, detail);
					orderProductSolution.add(oSolution);
				}
			}
		}
		return orderProductSolution;
	}

	private List<QuoteCloud> getQuoteCloudBasedOnVersion(ProductSolution productSolution) {
		List<QuoteCloud> quoteClouds = null;
		quoteClouds = quoteCloudRepository.findByProductSolution(productSolution);
		return quoteClouds;
	}
	
	private List<OrderCloud> getOrderCloudBasedOnVersion(OrderProductSolution productSolution) {
		List<OrderCloud> orderClouds = null;
		orderClouds = orderCloudRepository.findByOrderProductSolutionAndStatus(productSolution, (byte) 1);
		return orderClouds;
	}

	private Set<OrderCloud> constructOrderCloud(List<QuoteCloud> quoteClouds, OrderProductSolution oSolution,
			QuoteDetail detail) throws TclCommonException {
		Set<OrderCloud> orderClouds = new HashSet<>();
		if (quoteClouds != null) {
			OrderCloud orderCloud;
			for (QuoteCloud quoteCloud : quoteClouds) {
				orderCloud = new OrderCloud();
				orderCloud.setCloudCode(quoteCloud.getCloudCode());
				if (Objects.nonNull(quoteCloud.getParentCloudCode())) {
					orderCloud.setParentCloudCode(quoteCloud.getParentCloudCode());
				}
				orderCloud.setOrderProductSolution(oSolution);
				orderCloud.setOrderId(oSolution.getOrderToLeProductFamily().getOrderToLe().getOrder().getId());
				orderCloud.setOrderToLeId(oSolution.getOrderToLeProductFamily().getOrderToLe().getId());
				orderCloud.setDcCloudType(quoteCloud.getDcCloudType() != null ? quoteCloud.getDcCloudType() : "DC");
				orderCloud.setMstProductFamily(quoteCloud.getMstProductFamily());
				orderCloud.setDcLocationId(quoteCloud.getDcLocationId());
				orderCloud.setResourceDisplayName(quoteCloud.getResourceDisplayName());
				orderCloud.setArc(quoteCloud.getArc());
				orderCloud.setMrc(quoteCloud.getMrc());
				orderCloud.setNrc(quoteCloud.getNrc());
				orderCloud.setTcv(quoteCloud.getTcv());
				orderCloud.setPpuRate(quoteCloud.getPpuRate());
				quoteCloud.setFpStatus(quoteCloud.getFpStatus());
				orderCloud.setIsTaskTriggered(quoteCloud.getIsTaskTriggered());
				orderCloud.setCreatedBy(quoteCloud.getCreatedBy());
				orderCloud.setCreatedTime(new Date());
				orderCloud.setUpdatedBy(quoteCloud.getCreatedBy());
				orderCloud.setUpdatedTime(new Date());
				orderCloud.setStatus((byte) 1);
				orderCloudRepository.save(orderCloud);
				constructOrderProductComponent(quoteCloud.getId(), orderCloud);
				orderClouds.add(orderCloud);
			}
		}
		return orderClouds;
	}

	/**
	 * @param orderCloud
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent
	 * @param id,version
	 */
	private List<OrderProductComponent> constructOrderProductComponent(Integer id, OrderCloud orderCloud)
			throws TclCommonException {
		LOGGER.debug("In constructOrderProductComponent - start");
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
		MstProductFamily productFamily = getProductFamily(IPC);
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductFamily(id, productFamily);
		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				OrderProductComponent orderProductComponent = new OrderProductComponent();
				orderProductComponent.setReferenceId(orderCloud.getId());
				orderProductComponent.setReferenceName(QuoteConstants.IPCCLOUD.toString());
				if (quoteProductComponent.getMstProductComponent() != null) {
					orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
				}
				orderProductComponent.setType(quoteProductComponent.getType());
				orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
				LOGGER.debug("In constructOrderProductComponent - saving the orderProductComponent.");
				orderProductComponentRepository.save(orderProductComponent);
				LOGGER.debug("In constructOrderProductComponent - saved the orderProductComponent.");
				constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
				List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());
				orderProductComponent.setOrderProductComponentsAttributeValues(
						constructOrderAttribute(attributes, orderProductComponent));
				orderProductComponents.add(orderProductComponent);
			}
		}
		LOGGER.debug("In constructOrderProductComponent - end");
		return orderProductComponents;
	}

	/**
	 * @link http://www.tatacommunications.com constructOrderAttribute used to
	 *       construct order attribute
	 * @param quoteProductComponentsAttributeValues
	 * @param orderProductComponent
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
				orderPriceRepository.save(orderPrice);
			}
		}
		return orderPrice;
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
		mailNotificationBeanCofDelegate.setProductName(IPC_DESC);
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
		mailNotificationBean.setProductName(IPC);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	public String getLeAttributes(QuoteToLe quoteTole, String attr) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attr, BACTIVE);
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

	private void validateTriggerInput(String userId, Integer quoteId) throws TclCommonException {
		if (userId == null || quoteId == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteId);
		if (!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
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

	private void constructAttributeInfo(QuoteToLe quToLe, ContactAttributeInfo attributeInfo) throws TclCommonException, IllegalArgumentException {
		QuoteToLeBean quoteToLeBean = new QuoteToLeBean(quToLe);
		CustomerLeContactDetailBean customerLeContact = ipcQuotePdfService.getCustomerLeContact(quoteToLeBean);
		quToLe.getQuoteLeAttributeValues().forEach(attrval -> {
			if (attrval.getMstOmsAttribute() != null && attrval.getMstOmsAttribute().getName() != null) {
				switch (attrval.getMstOmsAttribute().getName()) {
				case LeAttributesConstants.LEGAL_ENTITY_NAME:
					attributeInfo.setCustomerContractingEntity(attrval.getAttributeValue());
					break;
				case LeAttributesConstants.GST_NUMBER:
					if (StringUtils.isEmpty(attrval.getAttributeValue())
							|| attrval.getAttributeValue().trim().equals("-")) {
						attributeInfo.setGstNumber(PDFConstants.NO_REGISTERED_GST);
					} else {
						attributeInfo.setGstNumber(attrval.getAttributeValue());
					}
					break;
				case LeAttributesConstants.CONTACT_NAME:
					if (customerLeContact != null && customerLeContact.getName() != null) {
						attributeInfo.setFirstName(customerLeContact.getName());
					}
					break;
				case LeAttributesConstants.CONTACT_NO:
					if (customerLeContact != null && customerLeContact.getName() != null) {
						attributeInfo.setContactNo(customerLeContact.getMobilePhone());
					}
					break;
				case LeAttributesConstants.CONTACT_EMAIL:
					if (customerLeContact != null && customerLeContact.getName() != null) {
						attributeInfo.setEmailId(customerLeContact.getEmailId());
					}
					break;
				case LeAttributesConstants.CONTACT_ID:
					attributeInfo.setUserId(attrval.getAttributeValue());
					break;
				case LeAttributesConstants.DESIGNATION:
					attributeInfo.setDesignation(customerLeContact.getTitle());
					break;
				default:
					break;
				}
			}
		});
	}

	private void validateDocumentRequest(CreateDocumentDto documentDto) throws TclCommonException {

		if ((documentDto == null) || (documentDto.getQuoteId() == 0) || (documentDto.getCustomerLegalEntityId() == 0)
				|| (documentDto.getSupplierLegalEntityId() == 0)) {
			throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	public CreateDocumentDto createDocument(CreateDocumentDto documentDto) throws TclCommonException {
		CreateDocumentDto createDocumentDto = new CreateDocumentDto();
		Integer oldCustomerLegalEntityId = null;
		try {
			if (documentDto.getQuoteId() == null || documentDto.getQuoteLeId() == null) {
				throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			}
			Quote quote = quoteRepository.findByIdAndStatus(documentDto.getQuoteId(), (byte) 1);

			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(documentDto.getQuoteLeId());
			if (!optionalQuoteLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			}
			if (documentDto.getCrossBorderTaxIpc() != null && documentDto.getCrossBorderTaxIpc()) {
				ipcPricingService.processPricingRequest(optionalQuoteLe.get().getQuote().getId(),
						optionalQuoteLe.get().getId(), Boolean.TRUE, null);
			} else {
				validateDocumentRequest(documentDto);

				CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
				customerLeAttributeRequestBean.setCustomerLeId(documentDto.getCustomerLegalEntityId());
				customerLeAttributeRequestBean.setProductName(IPC);

				LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));

				String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
						Utils.convertObjectToJson(customerLeAttributeRequestBean));

				if (StringUtils.isNotEmpty(customerLeAttributes)) {
					LOGGER.info("updateBillingInfoForSfdc: {}", customerLeAttributes);
					updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
							CustomerLeDetailsBean.class), optionalQuoteLe.get());
				}
				String spName = returnServiceProviderName(documentDto.getSupplierLegalEntityId());
				if (StringUtils.isNotEmpty(spName)) {
					LOGGER.info("processAccount for supplier");
					processAccount(optionalQuoteLe.get(), spName, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
				}
				QuoteToLe quoteToLe = optionalQuoteLe.get();
				oldCustomerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();

				quoteToLe.setErfCusCustomerLegalEntityId(documentDto.getCustomerLegalEntityId());
				quoteToLe.setErfCusSpLegalEntityId(documentDto.getSupplierLegalEntityId());

				if (Objects.nonNull(quoteToLe)
						&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
					quoteToLe.setStage(QuoteStageConstants.CHANGE_ORDER.getConstantCode());
				} else if (Objects.nonNull(quoteToLe)
						&& (quoteToLe.getQuoteType().equalsIgnoreCase(QuoteStageConstants.NEW.getConstantCode())
								|| quoteToLe.getQuoteType()
										.equalsIgnoreCase(QuoteStageConstants.MIGRATION.getConstantCode()))) {
					quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
				}
				quoteToLeRepository.save(quoteToLe);

				CustomerDetail customerDetail = null;
				if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
					Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(documentDto.getCustomerId(),
							(byte) 1);
					if (customer != null) {
						customerDetail = new CustomerDetail();
						customerDetail.setCustomerId(customer.getId());
					}
				} else {
					customerDetail = userInfoUtils.getCustomerByLeId(documentDto.getCustomerLegalEntityId());
				}
				if (customerDetail != null && !customerDetail.getCustomerId().equals(quote.getCustomer().getId())) {
					Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
					if (customerEntity.isPresent()) {
						quote.setCustomer(customerEntity.get());
						quoteRepository.save(quote);
					}
				}

				// Credit Check - Start
				LOGGER.info("Before triggering credit check");
				if (Objects.isNull(optionalQuoteLe.get().getQuoteType())
						|| (Objects.nonNull(optionalQuoteLe.get().getQuoteType())
								&& optionalQuoteLe.get().getQuoteType().equals(CommonConstants.NEW))) {
					CustomerLeDetailsBean lePreapprovedValuesBean = (CustomerLeDetailsBean) Utils
							.convertJsonToObject(customerLeAttributes, CustomerLeDetailsBean.class);
					processAccount(optionalQuoteLe.get(), lePreapprovedValuesBean.getCreditCheckAccountType(),
							LeAttributesConstants.CREDIT_CHECK_ACCOUNT_TYPE.toString());
					creditCheckService.triggerCreditCheck(documentDto.getCustomerLegalEntityId(), optionalQuoteLe,
							lePreapprovedValuesBean, oldCustomerLegalEntityId);
					createDocumentDto.setCreditCheckStatus(optionalQuoteLe.get().getTpsSfdcStatusCreditControl());
					createDocumentDto.setPreapprovedFlag(
							CommonConstants.BACTIVE.equals(optionalQuoteLe.get().getPreapprovedOpportunityFlag()) ? true
									: false);
				}
				LOGGER.info("After triggering credit check");

				AttributeDetail attribute = new AttributeDetail();
				attribute.setName("isTaxExemption");
				attribute.setValue(documentDto.getIsTaxExemption());
				User user = getUserId(Utils.getSource());
				if (attribute.getName() != null) {
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
					saveLegalEntityAttributes(optionalQuoteLe.get(), attribute, mstOmsAttribute);
					ipcPricingService.processPricingRequest(optionalQuoteLe.get().getQuote().getId(),
							optionalQuoteLe.get().getId(), Boolean.TRUE, null);
					omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.getTpsSfdcOptyId(),
							SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe);
				}
			}
			
			// pipf-212
			persistLeOwnerDetailsSfdc(documentDto);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return createDocumentDto;
	}
	
	private void persistLeOwnerDetailsSfdc(CreateDocumentDto documentDto) {
		if (Objects.nonNull(documentDto) && Objects.nonNull(documentDto.getQuoteLeId())) {
			QuoteToLe quoteToLe = quoteToLeRepository.findById(documentDto.getQuoteLeId()).get();

			List<QuoteLeAttributeValue> quoteLeAttrVal = quoteLeAttributeValueRepository
					.findByQuoteToLe_Id(quoteToLe.getId());
			Map<String, Integer> attrMap = new HashMap<>();
			quoteLeAttrVal.forEach(value -> {
				attrMap.put(value.getDisplayValue(), value.getId());
			});

			if (attrMap.containsKey(OWNER_EMAIL_SFDC)) {
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository
						.findById(attrMap.get(OWNER_EMAIL_SFDC)).get();
				attribute.setAttributeValue(documentDto.getOwnerEmailSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			} else {
				QuoteLeAttributeValue leAttributeValue = new QuoteLeAttributeValue();
				leAttributeValue.setQuoteToLe(quoteToLe);
				leAttributeValue.setAttributeValue(
						Objects.nonNull(documentDto.getOwnerEmailSfdc()) ? documentDto.getOwnerEmailSfdc() : "");
				leAttributeValue.setDisplayValue(LeAttributesConstants.OWNER_EMAIL_SFDC);
				leAttributeValue.setMstOmsAttribute(mstOmsAttributeRepository
						.findByNameAndIsActive(LeAttributesConstants.OWNER_EMAIL_SFDC, BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue);
			}

			if (attrMap.containsKey(OWNER_NAME_SFDC)) {
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(OWNER_NAME_SFDC))
						.get();
				attribute.setAttributeValue(documentDto.getOwnerNameSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			} else {
				QuoteLeAttributeValue leAttributeValue1 = new QuoteLeAttributeValue();
				leAttributeValue1.setQuoteToLe(quoteToLe);
				leAttributeValue1.setAttributeValue(
						Objects.nonNull(documentDto.getOwnerNameSfdc()) ? documentDto.getOwnerNameSfdc() : "");
				leAttributeValue1.setDisplayValue(LeAttributesConstants.OWNER_NAME_SFDC);
				leAttributeValue1.setMstOmsAttribute(mstOmsAttributeRepository
						.findByNameAndIsActive(LeAttributesConstants.OWNER_NAME_SFDC, BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue1);
			}

			if (attrMap.containsKey(REGION_SFDC)) {
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(REGION_SFDC))
						.get();
				attribute.setAttributeValue(documentDto.getRegionSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			} else {
				QuoteLeAttributeValue leAttributeValue2 = new QuoteLeAttributeValue();
				leAttributeValue2.setQuoteToLe(quoteToLe);
				leAttributeValue2.setAttributeValue(
						Objects.nonNull(documentDto.getRegionSfdc()) ? documentDto.getRegionSfdc() : "");
				leAttributeValue2.setDisplayValue(LeAttributesConstants.REGION_SFDC);
				leAttributeValue2.setMstOmsAttribute(mstOmsAttributeRepository
						.findByNameAndIsActive(LeAttributesConstants.REGION_SFDC, BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue2);
			}

			if (attrMap.containsKey(TEAM_ROLE_SFDC)) {
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(TEAM_ROLE_SFDC))
						.get();
				attribute.setAttributeValue(documentDto.getTeamRoleSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			} else {
				QuoteLeAttributeValue leAttributeValue3 = new QuoteLeAttributeValue();
				leAttributeValue3.setQuoteToLe(quoteToLe);
				leAttributeValue3.setAttributeValue(
						Objects.nonNull(documentDto.getTeamRoleSfdc()) ? documentDto.getTeamRoleSfdc() : "");
				leAttributeValue3.setDisplayValue(LeAttributesConstants.TEAM_ROLE_SFDC);
				leAttributeValue3.setMstOmsAttribute(mstOmsAttributeRepository
						.findByNameAndIsActive(LeAttributesConstants.TEAM_ROLE_SFDC, BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue3);
			}

			if (attrMap.containsKey(CONTACT_SFDC)) {
				QuoteLeAttributeValue attribute = quoteLeAttributeValueRepository.findById(attrMap.get(CONTACT_SFDC))
						.get();
				attribute.setAttributeValue(documentDto.getContactMobileSfdc());
				quoteLeAttributeValueRepository.save(attribute);
			} else {

				QuoteLeAttributeValue leAttributeValue3 = new QuoteLeAttributeValue();
				leAttributeValue3.setQuoteToLe(quoteToLe);
				leAttributeValue3.setAttributeValue(
						Objects.nonNull(documentDto.getContactMobileSfdc()) ? documentDto.getContactMobileSfdc() : "");
				leAttributeValue3.setDisplayValue(LeAttributesConstants.CONTACT_SFDC);
				leAttributeValue3.setMstOmsAttribute(mstOmsAttributeRepository
						.findByNameAndIsActive(LeAttributesConstants.CONTACT_SFDC, BACTIVE).get(0));
				quoteLeAttributeValueRepository.save(leAttributeValue3);
			}
		}
	}

	public void updateBillingInfoForSfdc(CustomerLeDetailsBean request, QuoteToLe quoteToLe) throws TclCommonException {
		try {
			construcBillingSfdcAttribute(quoteToLe, request);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void construcBillingSfdcAttribute(QuoteToLe quoteToLe, CustomerLeDetailsBean request) {
		if (request.getAttributes() != null) {
			request.getAttributes().forEach(billAttr -> constructBillingAttribute(billAttr, quoteToLe)

			);
		}
		processAccount(quoteToLe, request.getAccounCuId(), LeAttributesConstants.ACCOUNT_CUID);
		processAccount(quoteToLe, request.getAccountId(), LeAttributesConstants.ACCOUNT_NO18);
		// processAccount(quoteToLe,
		// String.valueOf(request.getBillingContactId()),
		// LeAttributesConstants.BILLING_CONTACT_ID);
		processAccount(quoteToLe, request.getLegalEntityName(), LeAttributesConstants.LEGAL_ENTITY_NAME);
	}

	private void constructBillingAttribute(Attributes attribute, QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getAttributeName());
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			updateAttributes(attribute, quoteLeAttributeValues, quoteToLe);
		} else {
			createAttribute(attribute, quoteToLe);
		}
	}

	private void createAttribute(Attributes attribute, QuoteToLe quoteToLe) {
		if (attribute.getAttributeName() != null) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			if (LeAttributesConstants.BILLING_CURRENCY.equalsIgnoreCase(attribute.getAttributeName())) {
				attributeValue.setAttributeValue(quoteToLe.getCurrencyCode());
			} else {
				attributeValue.setAttributeValue(attribute.getAttributeValue());
			}
			attributeValue.setDisplayValue(attribute.getAttributeName());
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValueRepository.save(attributeValue);
		}
	}

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

	private void updateAttributes(Attributes attribute, List<QuoteLeAttributeValue> quoteLeAttributeValues,
			QuoteToLe quoteToLe) {
		quoteLeAttributeValues.forEach(attr -> {
			if (!LeAttributesConstants.PAYMENT_CURRENCY.equalsIgnoreCase(attr.getMstOmsAttribute().getName())
					&& !LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY
							.equalsIgnoreCase(attr.getMstOmsAttribute().getName())) {
				if (LeAttributesConstants.BILLING_CURRENCY.equalsIgnoreCase(attr.getMstOmsAttribute().getName())) {
					attr.setAttributeValue(quoteToLe.getCurrencyCode());
				} else {
					attr.setAttributeValue(attribute.getAttributeValue());
				}
				quoteLeAttributeValueRepository.save(attr);
			}
		});
	}

	private void processAccount(QuoteToLe quoteToLe, String attrValue, String attributeName) {
		LOGGER.info("Account Name {} :",attributeName);
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attributeName);
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			LOGGER.info("Update");
			quoteLeAttributeValues.forEach(attr -> {
				attr.setAttributeValue(attrValue);
				quoteLeAttributeValueRepository.save(attr);
			});
		} else {
			LOGGER.info("Create");
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(attrValue);
			attributeValue.setDisplayValue(attrValue);
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attributeName);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValueRepository.save(attributeValue);
		}
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

	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}");
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;
	}

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

			if (Objects.isNull(optionalQuoteToLe.get().getQuoteCategory()) || (MACDConstants.ADD_CLOUDVM_SERVICE
					.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteCategory()))) {
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

	protected void saveLegalEntityAttributes(QuoteToLe quoteToLe, AttributeDetail attribute,
			MstOmsAttribute mstOmsAttribute) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getName());
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				LOGGER.info("Inside quote to le update: {}",attribute.getName());
				attrVal.setMstOmsAttribute(mstOmsAttribute);
				attrVal.setAttributeValue(attribute.getValue());
				attrVal.setDisplayValue(attribute.getName());
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeValueRepository.save(attrVal);
			});
		} else {
			LOGGER.info("Inside quote to create: {}",attribute.getName());
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setAttributeValue(attribute.getValue());
			quoteLeAttributeValue.setDisplayValue(attribute.getName());
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		}
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

	public QuoteDetail approvedManualQuotes(Integer quoteId) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
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
				// updateManualOrderConfirmationAudit(ipAddress,
				// order.getOrderCode());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
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
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
					for (QuoteDelegation quoteDelegation : quoteDelegate) {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					}
				}
				for (OrderToLe orderToLe : order.getOrderToLes()) {
					List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
							.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quote.getId(),
									AttachmentTypeConstants.COF.toString());
					for (OmsAttachment omsAttachment : omsAttachmentList) {
						omsAttachment.setOrderToLe(orderToLe);
						omsAttachment.setReferenceName(CommonConstants.ORDERS);
						omsAttachment.setReferenceId(order.getId());
						omsAttachmentRepository.save(omsAttachment);
					}
					ipcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),
							cofObjectMapper);
					break;
				}
			}

			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())
						|| quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}
			}

			for (OrderToLe orderToLe : order.getOrderToLes()) {
				LOGGER.info("Order Type: {}, Order Category: {}", orderToLe.getOrderType(), orderToLe.getOrderCategory());
				if (MACDConstants.MACD_QUOTE_TYPE.equals(orderToLe.getOrderType())
						&& MACDConstants.DELETE_VM.equals(orderToLe.getOrderCategory())) {
					processAttachmentsFromQuote(quoteId, orderToLe);
					ipcOrderService.launchCloud(order.getId(), orderToLe.getId(), false);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	/**
	 * 
	 * cofDownloadAccountManagerNotification - This method is used notify the
	 * account manager when a customer downloads the cof
	 * 
	 * @param orderId,
	 *            orderToLeId
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
			String userName, String accountManagerEmail, String orderId, String quoteLink, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setCustomerAccountName(customerAccountName);
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderId);
		mailNotificationBean.setQuoteLink(quoteLink);
		mailNotificationBean.setProductName(IPC_DESC);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			populatePartnerClassification(quoteToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	public void updateQuoteStage(Integer quoteToLeId, QuoteStageConstants quoteStageConstants) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeOpt.isPresent()) {
			quoteToLeOpt.get().setStage(quoteStageConstants.toString());
			quoteToLeRepository.save(quoteToLeOpt.get());
			updateSfdcStage(quoteToLeOpt.get().getId(), SFDCConstants.PROPOSAL_SENT.toString());
		}
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
					updateQuoteCloudCurrencyValues(quoteToLe.get(), inputCurrency, existingCurrency);
					updateQuotePriceCurrencyValues(quote, inputCurrency, existingCurrency);
					updatePaymentCurrencyWithInputCurrency(quoteToLe.get(), inputCurrency);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

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

	public void updateQuoteCloudCurrencyValues(QuoteToLe quoteToLe, String inputCurrency, String existingCurrency) {
		for (QuoteToLeProductFamily quoteLeProdFamily : quoteToLe.getQuoteToLeProductFamilies()) {
			for (ProductSolution prodSolution : quoteLeProdFamily.getProductSolutions()) {
				List<QuoteCloud> quoteClouds = getQuoteCloudBasedOnVersion(prodSolution);
				for (QuoteCloud quoteCloud : quoteClouds) {
					quoteCloud.setArc(
							omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteCloud.getArc()));
					quoteCloud.setMrc(
							omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteCloud.getMrc()));
					quoteCloud.setNrc(
							omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteCloud.getNrc()));
					quoteCloud.setPpuRate(
							omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteCloud.getPpuRate()));
					quoteCloudRepository.save(quoteCloud);
				}
			}

		}
	}

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

	public void updatePaymentCurrencyWithInputCurrency(QuoteToLe quotele, String inputCurrency) {
		quotele.setCurrencyCode(StringUtils.upperCase(inputCurrency));
		quoteToLeRepository.save(quotele);
	}

	public List<AttributeDetail> getIPCDiscountComments(Integer quoteToLeId) {
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
				quoteToLeId, IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
		if (optQuoteCloud.isPresent()) {
			QuoteCloud quoteCloud = optQuoteCloud.get();
			QuoteProductComponent quoteComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(quoteCloud.getId(),
							IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES, CommonConstants.IPC);
			if (null != quoteComponent) {
				attributeDetails = constructProductComponentAttributeDetail(quoteComponent);
			}
		}
		return attributeDetails.stream().filter(
				attributeDetail -> !IPCQuoteConstants.ATTRIBUTE_IPC_APPROVAL_LEVEL.equals(attributeDetail.getName()))
				.collect(Collectors.toList());
	}

	public QuoteToLeProductFamily getQuoteToLeProductDetails(QuoteToLe quoteToLe, MstProductFamily productFamily) {
		return quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
	}

	public static AttributeDetail constructAttributeDetail(String name) {
		return constructAttributeDetail(name, null);
	}

	public static AttributeDetail constructAttributeDetail(String name, String value) {
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName(name);
		attributeDetail.setValue(value);
		return attributeDetail;
	}

	/**
	 * 
	 * updateIPCDiscountProperties - Updates ipc price discount details
	 * 
	 * @param updateRequest
	 * @throws TclCommonException
	 */
	public void updateIPCDiscountProperties (UpdateRequest updateRequest) throws TclCommonException{
		List<AttributeDetail> attributeDetails =  new  ArrayList<>();
		attributeDetails.add(constructAttributeDetail(IPCQuoteConstants.ADDITIONAL_DISCOUNT_PERCENTAGE, null)); 
		for (AttributeDetail attributeDetail : updateRequest.getAttributeDetails()){
			attributeDetails.add(constructAttributeDetail(attributeDetail.getName(), attributeDetail.getValue()));
		}
		if (!attributeDetails.isEmpty()) {
			updateIPCDiscountProperties(updateRequest.getQuoteToLe(), attributeDetails);
		} else {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
		}
	}
	
	public void updateIPCDiscountProperties(QuoteToLe quoteToLe, String approvalLevel, IpcDiscountBean ipcDiscountBean)
			throws TclCommonException {
		if (!MACDConstants.DELETE_VM.equals(quoteToLe.getQuoteCategory())) {
			List<AttributeDetail> attributes = new ArrayList<>();
			attributes.add(constructAttributeDetail(IPCQuoteConstants.ATTRIBUTE_IPC_APPROVAL_LEVEL, approvalLevel));
			attributes.add(constructAttributeDetail(IPCQuoteConstants.ATTRIBUTE_IPC_DISCOUNT_REQUEST,
					Utils.convertObjectToJson(ipcDiscountBean)));
			attributes.add(constructAttributeDetail(IPCQuoteConstants.ADDITIONAL_DISCOUNT_PERCENTAGE,
					String.valueOf(ipcDiscountBean.getAdditionalDiscountPercentage())));
			attributes.add(constructAttributeDetail(IPCQuoteConstants.INPUT_DISCOUNT_PERCENTAGE,
					String.valueOf(ipcDiscountBean.getInputDiscountPercentage())));
			attributes.add(constructAttributeDetail(IPCQuoteConstants.IPC_FINAL_PRICE,
					String.valueOf(ipcDiscountBean.getIpcFinalPrice())));
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteToLe.getId());
			if (quoteToLeEntity.isPresent()) {
				if (ipcDiscountBean.getAskAccessPrice() != null) {
					attributes.add(constructAttributeDetail(IPCQuoteConstants.ATTRIBUTE_IPC_ASK_ACCESS_MRC,
							String.valueOf(round(omsUtilService.convertCurrency(quoteToLeEntity.get().getCurrencyCode(),
									"INR", ipcDiscountBean.getAskAccessPrice())))));
				}
				if (ipcDiscountBean.getAskAdditionalIpPrice() != null) {
					attributes.add(constructAttributeDetail(IPCQuoteConstants.ATTRIBUTE_IPC_ASK_IP_MRC,
							String.valueOf(round(omsUtilService.convertCurrency(quoteToLeEntity.get().getCurrencyCode(),
									"USD", ipcDiscountBean.getAskAdditionalIpPrice())))));
				}
			}
			updateIPCDiscountProperties(quoteToLe.getId(), attributes);
		}
	}
	
	public static double round(double value) {
        return (double) Math.round(value * 100) / 100;
    }

	public void updateIPCDiscountProperties(Integer quoteToLeId, List<AttributeDetail> attributeDetails)
			throws TclCommonException {
		User user = getUserId(Utils.getSource());
		Optional<QuoteToLe> quoteToLeO = quoteToLeRepository.findById(quoteToLeId);
		if(quoteToLeO.isPresent() && MACDConstants.DELETE_VM.equals(quoteToLeO.get().getQuoteCategory())) {
			for(AttributeDetail attribute : attributeDetails) {
				MstOmsAttribute mstOmsAttribute = ipcPricingService.fetchMstOmsAttribute(attribute);
				saveLegalEntityAttributes(quoteToLeO.get(), attribute, mstOmsAttribute);
			}
		} else {
			Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
					quoteToLeId, IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
			if (optQuoteCloud.isPresent()) {
				QuoteCloud quoteCloud = optQuoteCloud.get();
				QuoteProductComponent quoteComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(quoteCloud.getId(),
								IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES, CommonConstants.IPC);
				if (Objects.nonNull(quoteComponent)) {
					for (AttributeDetail attribute : attributeDetails) {
						processProductAttribute(quoteComponent, attribute, user);
					}
				}
			} else {
				if (quoteToLeO.isPresent()) {
					QuoteToLe quoteToLe = quoteToLeO.get();
					List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLeId,
							CommonConstants.BACTIVE);
	
					ComponentDetail component = new ComponentDetail();
					component.setName(IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES);
					component.setAttributes(attributeDetails);
	
					SolutionDetail solution = new SolutionDetail();
					solution.setOfferingName(IPCQuoteConstants.SOLUTION_IPC_DISCOUNT);
					solution.setComponents(Arrays.asList(component));
					solution.setDcCloudType(quoteClouds.get(0).getDcCloudType() != null ? quoteClouds.get(0).getDcCloudType() : "DC");
					solution.setDcLocationId(quoteClouds.get(0).getDcLocationId());
	
					com.tcl.dias.oms.ipc.beans.QuoteCloud cloud = new com.tcl.dias.oms.ipc.beans.QuoteCloud();
					cloud.setProductName(CommonConstants.IPC);
					cloud.setSolutions(Arrays.asList(solution));
					addQuoteCloudProduct(cloud, quoteToLe.getQuote().getId(), quoteToLeId,
							quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				}
			}
		}
	}
	
	public String processIPCQuoteCrossBorderTax(Integer quoteLeId, Integer customerLeId) throws TclCommonException {
		CrossBorderBean crossBorderBean = null;
		String crossBorderBeanResponse = "";
		List<QuoteCloud> quoteCloud = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteLeId,
				CommonConstants.BACTIVE);
		Optional<QuoteCloud> optionalCloud = quoteCloud.stream().findFirst();
		if (optionalCloud.isPresent()) {
			crossBorderBean = new CrossBorderBean();
			crossBorderBean.setDcLocationId(optionalCloud.get().getDcLocationId());
			String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeId.toString());
			LOGGER.info("Response from customerLeQueue queue {} :", response);
			if (StringUtils.isNotBlank(response)) {
				CustomerLegalEntityDetailsBean cLeBean = Utils.convertJsonToObject(response,
						CustomerLegalEntityDetailsBean.class);
				if (!Objects.isNull(cLeBean) && !CollectionUtils.isEmpty(cLeBean.getCustomerLeDetails())) {
					CustomerLeBean customerLeBean = cLeBean.getCustomerLeDetails().get(0);
					if (!CollectionUtils.isEmpty(customerLeBean.getCountry())) {
						crossBorderBean.setCustomerLeCountry(customerLeBean.getCountry().get(0).toUpperCase());
					}
				}
				LOGGER.info("Cross Border Bean {} :", crossBorderBean);
				String crossBorderTaxResponse = (String) mqUtils.sendAndReceive(crossBorderTaxQueue,
						Utils.convertObjectToJson(crossBorderBean));
				LOGGER.info("Response from ipc crossborder tax queue: {}", crossBorderTaxResponse);
				if (StringUtils.isNotBlank(crossBorderTaxResponse)) {
					crossBorderBean = Utils.convertJsonToObject(crossBorderTaxResponse, CrossBorderBean.class);
					LOGGER.info("Ipc crossborder tax response flag: {}", crossBorderTaxResponse);
				}
			}
		}
		if(Objects.nonNull(crossBorderBean)) {
			crossBorderBeanResponse = Utils.convertObjectToJson(crossBorderBean);
		} 
		
		return crossBorderBeanResponse;
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
		if (StringUtils.isNotBlank(stage) && (SFDCConstants.PROPOSAL_SENT.equals(stage))) {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent()) {
				String sfdcId = quoteToLe.get().getTpsSfdcOptyId();
				omsSfdcService.processUpdateOpportunity(new Date(), sfdcId, stage, quoteToLe.get());
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
			LOGGER.info("Get QuoteToLe");
			Optional<QuoteToLe> optionalQuoteToLe = quote.getQuoteToLes().stream().findFirst();
			if (optionalQuoteToLe.isPresent() && Objects.nonNull(optionalQuoteToLe.get().getQuoteType())
					&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteToLe.get().getQuoteType())
					&& (optionalQuoteToLe.get().getIsAmended() == null
							|| optionalQuoteToLe.get().getIsAmended() != 1)) {
				LOGGER.info("Call approvedMacdDocusignQuotes");
				ipcQuoteMACDService.approvedMacdDocusignQuotes(quoteuuId);
			} else {
				Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);
				if (order != null) {
					detail.setOrderId(order.getId());
				} else {
					LOGGER.info("Order is not available. Creating new Order entity");
					order = constructOrder(quote, detail);
					detail.setOrderId(order.getId());

					LOGGER.info("Get Details from QuoteToLe");
					for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
						Date cofSignedDate = new Date();
						DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
						if (docusignAudit != null && docusignAudit.getCustomerSignedDate() != null
								&& (docusignAudit.getStatus()
										.equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString())
										|| docusignAudit.getStatus()
												.equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString()))) {
							cofSignedDate = docusignAudit.getCustomerSignedDate();
						}
						LOGGER.info("Cof signed date for quote code ---> {} before cof won recieved stage is ----> {} ",
								quote.getQuoteCode(), cofSignedDate);
						omsSfdcService.processUpdateOpportunity(cofSignedDate, quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
						LOGGER.info("Cof signed date for quote code ---> {} after cof won recieved stage is ----> {} ",
								quote.getQuoteCode(), cofSignedDate);
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
							ipcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
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
						LOGGER.info(
								"After updating the quote stage from order form to enrichment for refId {} stage is ---> {}",
								quoteuuId, quoteLe.getStage());
					}
				}

				for (OrderToLe orderToLe : order.getOrderToLes()) {
					LOGGER.info("Order Type: {}, Order Category: {}", orderToLe.getOrderType(), orderToLe.getOrderCategory());
					if (MACDConstants.MACD_QUOTE_TYPE.equals(orderToLe.getOrderType())
							&& MACDConstants.DELETE_VM.equals(orderToLe.getOrderCategory())) {
						processAttachmentsFromQuote(quote.getId(), orderToLe);
						ipcOrderService.launchCloud(order.getId(), orderToLe.getId(), false);
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
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
				LOGGER.info("Quote {}", quote.toString());
				Optional<Order> order = orderRepository.findByQuote(quote);
				if (!order.isPresent()) {
					quote.getQuoteToLes().stream().forEach(quoteToLe -> {
						try {
							LOGGER.info("SFDC Update Opportunity - CLOSED DROPPED");
							// SFDC Update Opportunity - CLOSED DROPPED
							omsSfdcService.processUpdateOpportunity(null, quoteToLe.getTpsSfdcOptyId(),
									SFDCConstants.CLOSED_DROPPED, quoteToLe);
							quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteToLeProdFamily -> {
								quoteToLeProdFamily.getProductSolutions().stream().forEach(prodSolution -> {
									List<QuoteCloud> quoteClouds = getQuoteCloudBasedOnVersion(prodSolution);
									quoteClouds.stream().forEach(quoteCloud -> {
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
												.findByReferenceIdAndReferenceName(quoteCloud.getId(),
														QuoteConstants.IPCCLOUD.toString());
										quoteCloudRepository.delete(quoteCloud);
										quoteProductComponentList.stream().forEach(quoteProdComponent -> {
											deleteQuoteProductComponent(quoteProdComponent);
										});
									});
									productSolutionRepository.delete(prodSolution);
								});
								quoteToLeProductFamilyRepository.delete(quoteToLeProdFamily);
							});
							deleteQuoteLeAttributeValues(quoteToLe);
							deleteMacdDetails(quoteToLe.getId());
							quoteToLeRepository.delete(quoteToLe);
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}
					});
					deleteQuotePrice(quote);
					quoteRepository.delete(quote);
				}
				else {
					throw new TclCommonException(ExceptionConstants.ORDER_ALREADY_EXISTS, ResponseResource.R_CODE_ERROR);
				}
			 }
		} catch (Exception e) {
			LOGGER.error("Error",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void deleteMacdDetails(Integer quoteToLeId) {
		List<MacdDetail> macdDetails = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteToLeId);
		if(macdDetails != null) {
			macdDetails.forEach(macdDetail -> {
				if(!MACDConstants.MACD_ORDER_COMMISSIONED.equalsIgnoreCase(macdDetail.getStage())) {
					macdDetailRepository.delete(macdDetail);
				}
			});
		}
	}
	
	private boolean tasksPending(Integer quoteId) {
		boolean[] tasksPending = {false};
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId).get(0);
		Set<QuoteToLeProductFamily> quoteProductFamilySet = quoteToLe.getQuoteToLeProductFamilies();
		quoteProductFamilySet.stream().forEach(quoteToLeProductFamily ->{
			Set<ProductSolution> productSolutions = quoteToLeProductFamily.getProductSolutions();
			productSolutions.stream().forEach(productSolution -> {
				List<QuoteCloud> quoteClouds = getQuoteCloudBasedOnVersion(productSolution);
				quoteClouds.stream().forEach(quoteCloud -> {
					if(quoteCloud.getIsTaskTriggered() == 1) {
						tasksPending[0] = true;
					}
				});
			});
		});
		return tasksPending[0];
	}
	
	private void deleteOrderRelatedDetails(Order order) {
		order.getOrderToLes().stream().forEach(orderToLe -> {
			orderToLe.getOrderToLeProductFamilies().stream().forEach(orderToLeProdFamily -> {
				orderToLeProdFamily.getOrderProductSolutions().stream().forEach(orderProdSolution -> {
					List<OrderCloud> orderClouds = getOrderCloudBasedOnVersion(orderProdSolution);
					orderClouds.stream().forEach(orderCloud -> {
						List<OrderProductComponent> orderProductComponentList = orderProductComponentRepository
								.findByReferenceId(orderCloud.getId());
						orderCloudRepository.delete(orderCloud);
						orderProductComponentList.stream().forEach(orderProdComponent -> {
							deleteOrderProductComponent(orderProdComponent);
						});
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
	
	private void deleteOrderProductComponent(OrderProductComponent orderProdComponent) {
		if (!orderProdComponent.getOrderProductComponentsAttributeValues().isEmpty()) {
			orderProdComponent.getOrderProductComponentsAttributeValues().stream().forEach(attri -> {
				OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(attri.getId()), QuoteConstants.ATTRIBUTES.toString());
				if (orderPrice != null) {
					orderPriceRepository.delete(orderPrice);
				}
				orderProductComponentsAttributeValueRepository.delete(attri);
			});

			OrderPrice orderPriceAtt = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderProdComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (orderPriceAtt != null) {
				orderPriceRepository.delete(orderPriceAtt);
			}
			orderProductComponentRepository.delete(orderProdComponent);
		}
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
	
	private void deleteOrderConfirmationAudits(Order order) {
		OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
				.findByOrderRefUuid(order.getOrderCode());
		if (orderConfirmationAudit != null) {
			orderConfirmationAuditRepository.delete(orderConfirmationAudit);
		}

		CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
		if (cofDetails != null) {
			cofDetailsRepository.delete(cofDetails);
		}

		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(order.getOrderCode());
		if (docusignAudit != null) {
			docusignAuditRepository.delete(docusignAudit);
		}
	}
	
	private void deleteQuoteLeAttributeValues(QuoteToLe quoteToLe) {
		LOGGER.info("Inside Method - deleteQuoteLeAttributeValues");
		List<QuoteLeAttributeValue> quoteLeAttributeValueList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe);
		if (!quoteLeAttributeValueList.isEmpty()) {
			quoteLeAttributeValueRepository.deleteAll(quoteLeAttributeValueList);
		}
		List<QuoteDelegation> quoteDelegationList = quoteDelegationRepository.findByQuoteToLe(quoteToLe);
		if (!quoteDelegationList.isEmpty()) {
			quoteDelegationRepository.deleteAll(quoteDelegationList);
		}
		List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository.findByQuoteToLe(quoteToLe);
		if (!omsAttachmentsList.isEmpty()) {
			omsAttachmentRepository.deleteAll(omsAttachmentsList);
		}
	}
	
	private void deleteQuotePrice(Quote quote) {
		List<QuotePrice> quotePriceList = quotePriceRepository.findByQuoteId(quote.getId());
		if (!quotePriceList.isEmpty()) {
			quotePriceRepository.deleteAll(quotePriceList);
		}

		List<OrderPrice> orderPriceList = orderPriceRepository.findByQuoteId(quote.getId());
		if (!orderPriceList.isEmpty()) {
			orderPriceRepository.deleteAll(orderPriceList);
		}
	}
	
	private void deleteQuoteProductComponent(QuoteProductComponent quoteProdComponent) {
		LOGGER.info("Inside Method - deleteQuoteProductComponent");
		if (!quoteProdComponent.getQuoteProductComponentsAttributeValues().isEmpty()) {
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
	
	public void updateDataTransferOverageChargesInLeAttributes(Map<String, String> dataTransferPriceMap, QuoteToLe quoteTole) {
		StringBuilder ipcDataTransferOverageCharges = new StringBuilder("null");
		dataTransferPriceMap.forEach((dtRange, price) -> {
			if(!dtRange.contains(">")) {
				String keyArray[] = dtRange.split("to");
				if(keyArray.length == 2) {
					ipcDataTransferOverageCharges.append("#");
					ipcDataTransferOverageCharges.append(keyArray[0].trim());
					ipcDataTransferOverageCharges.append(";");
					ipcDataTransferOverageCharges.append(keyArray[1].replace("GB", "").trim());
					ipcDataTransferOverageCharges.append(";");
					ipcDataTransferOverageCharges.append(price);
					ipcDataTransferOverageCharges.append(";");
				}
			} else {
				String keyArray[] = dtRange.split(">");
				if(keyArray.length == 2) {
					ipcDataTransferOverageCharges.append("#");
					ipcDataTransferOverageCharges.append(keyArray[1].replace("GB", "").trim());
					ipcDataTransferOverageCharges.append(";");
					ipcDataTransferOverageCharges.append("999999999");
					ipcDataTransferOverageCharges.append(";");
					ipcDataTransferOverageCharges.append(price);
					ipcDataTransferOverageCharges.append(";");
				}
			}
		});
		ipcDataTransferOverageCharges.append("#");
		
		User user = getUserId(Utils.getSource());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.IPC_DATA_TRANSFER_OVERAGE_CHARGES, ipcDataTransferOverageCharges.toString());
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
	
	@Transactional
	public List<LeOwnerDetailsSfdc> getOwnerDetailsForSfdc(Integer customerId, Integer customerLeId)
			throws TclCommonException {
		try {
			return getDetails(customerId, customerLeId);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private List<LeOwnerDetailsSfdc> getDetails(Integer customerId, Integer customerLeId) throws TclCommonException {
		Objects.requireNonNull(customerId, "Customer Id cannot be null;required field");
		Objects.requireNonNull(customerLeId, "customerLe Id cannot be null;required field");
		String cusLeId = "," + customerLeId.toString();
		LOGGER.info("Customer id and cus Le Id are  -> {} ----------- {} ", customerId, cusLeId);

		String queueResponse = (String) mqUtils.sendAndReceive(ownerDetailsQueue, customerId.toString() + cusLeId);
		LOGGER.info("Response from owner details queue for quote ---> {}  is ----> {} ", customerLeId, queueResponse);

		LeOwnerDetailsSfdc[] ownerDetails = (LeOwnerDetailsSfdc[]) Utils.convertJsonToObject(queueResponse,
				LeOwnerDetailsSfdc[].class);
		List<LeOwnerDetailsSfdc> ownerDetailsSfdcList = new ArrayList<>();
		ownerDetailsSfdcList.addAll(Arrays.asList(ownerDetails));
		if (!ownerDetailsSfdcList.isEmpty() && Objects.nonNull(ownerDetailsSfdcList)) {
			LOGGER.info("List converted into object after Owner Details queue call is----> {} with size ----> {} ",
					ownerDetailsSfdcList, ownerDetailsSfdcList.size());
		}
		if (!ownerDetailsSfdcList.isEmpty()) {
			LOGGER.info("Final List sent for quote id ---> {} is ----> {} ", customerLeId, ownerDetailsSfdcList);
		}
		return ownerDetailsSfdcList;
	}
	
	private void getLeOwnerDetailsSFDC(QuoteBean response, Quote quote) {
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote(quote).stream().findFirst().get();
		LOGGER.info("Entered setLeOwnerDetailsSFDC for quote --> {} and stage ---> {} ", quote.getQuoteCode(),
				quoteToLe.getStage());
		if (Objects.nonNull(quoteToLe) && ORDER_FORM.getConstantCode().equalsIgnoreCase(quoteToLe.getStage())) {

			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLe_Id(quoteToLe.getId());
			LeOwnerDetailsSfdc leOwnerDetailsSfdc = new LeOwnerDetailsSfdc();

			Map<String, String> attrMap = new HashMap<>();
			quoteLeAttributeValues.forEach(value -> {
				attrMap.put(value.getDisplayValue(), value.getAttributeValue());
			});

			if (attrMap.containsKey(OWNER_NAME_SFDC)) {
				leOwnerDetailsSfdc.setOwnerName(attrMap.get(OWNER_NAME_SFDC));
			}

			if (attrMap.containsKey(OWNER_EMAIL_SFDC)) {
				leOwnerDetailsSfdc.setEmail(attrMap.get(OWNER_EMAIL_SFDC));
			}

			if (attrMap.containsKey(REGION_SFDC)) {
				leOwnerDetailsSfdc.setRegion(attrMap.get(REGION_SFDC));
			}

			if (attrMap.containsKey(TEAM_ROLE_SFDC)) {
				leOwnerDetailsSfdc.setTeamRole(attrMap.get(TEAM_ROLE_SFDC));
			}

			if (attrMap.containsKey(CONTACT_SFDC)) {
				leOwnerDetailsSfdc.setMobile(attrMap.get(CONTACT_SFDC));
			}

			LOGGER.info("Le Owner Bean for quote ---> {} is ---> {} ", quote.getQuoteCode(), leOwnerDetailsSfdc);
			response.setLeOwnerDetailsSfdc(leOwnerDetailsSfdc);
		}
	}

	public void triggerSfdcUpdateProduct(String quoteCode) throws TclCommonException {
		Quote quote = quoteRepository.findByQuoteCode(quoteCode);
		if (Objects.nonNull(quote)) {
			omsSfdcUtilService.processUpdateProductForIpc(quote.getQuoteToLes().stream().findFirst().get());
		}
	}
}
