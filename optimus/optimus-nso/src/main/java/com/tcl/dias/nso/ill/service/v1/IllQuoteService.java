/**
 * 
 */
package com.tcl.dias.nso.ill.service.v1;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.nso.beans.Attribute;
import com.tcl.dias.nso.beans.Component;
import com.tcl.dias.nso.beans.QuoteDetail;
import com.tcl.dias.nso.beans.QuoteToLeDetail;
import com.tcl.dias.nso.beans.Site;
import com.tcl.dias.nso.beans.Solution;
import com.tcl.dias.nso.constants.ExceptionConstants;
import com.tcl.dias.nso.constants.IllSitePropertiesConstants;
import com.tcl.dias.nso.constants.QuoteConstants;
import com.tcl.dias.nso.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SiteFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
//import com.tcl.dias.oms.service.v1.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author KarMani
 *
 */

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class IllQuoteService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IllQuoteService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	protected IllSiteRepository illSiteRepository;

	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customerleattr.product.queue}")
	protected String customerLeAttrQueueProduct;

	@Value("${rabbitmq.service.provider.detail}")
	String spQueue;

//	@Autowired
//	protected OmsSfdcService omsSfdcService;

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
	@Transactional(readOnly = false)
	public Map<String, Object> createQuote(QuoteDetail quoteDetail, Integer quoteId) throws TclCommonException {
		LOGGER.info("Create Quote request received for the product ILL {}", quoteDetail);
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			processQuote(quoteDetail, quoteId, response);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * 
	 * updateQuote - update the quote
	 * 
	 * @param quoteDetail
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateQuote(QuoteDetail quoteDetail, Integer quoteId) throws TclCommonException {
		LOGGER.info("update Quote request received for the product ILL {}", quoteDetail);
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			processQuote(quoteDetail, quoteId, response);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
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
	 * persistQuoteLeAttributes
	 * 
	 * @param user
	 * @param quoteTole
	 * @throws TclCommonException
	 */
	protected void persistQuoteLeAttributes(User user, QuoteToLe quoteTole) throws TclCommonException {
		CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
		customerLeAttributeRequestBean.setCustomerLeId(quoteTole.getErfCusCustomerLegalEntityId());
		customerLeAttributeRequestBean.setProductName("IAS");
		LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
				Utils.convertObjectToJson(customerLeAttributeRequestBean));

		if (StringUtils.isNotEmpty(customerLeAttributes)) {
			updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
					CustomerLeDetailsBean.class), quoteTole);
		}
		String spName = returnServiceProviderName(quoteTole.getErfCusSpLegalEntityId());
		if (StringUtils.isNotEmpty(spName)) {
			processAccount(quoteTole, spName, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
		}
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

	}

	/**
	 * updateConstactInfo
	 * 
	 * @param quoteTole
	 * @param user
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
				quoteIllSiteSlaRepository.flush();
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

		illSiteRepository.delete(quoteIllSite);
		quoteIllSiteSlaRepository.flush();

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
	protected Quote processQuote(QuoteDetail quoteDetail, Integer quoteId, Map<String, Object> response)
			throws TclCommonException {
		LOGGER.info("Entering processQuote with the quoteDetail Object {}", quoteDetail);
		MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
		LOGGER.info("Getting the quote for product name {}", quoteDetail.getProductName());
		User user = getUserId(Utils.getSource());
		Quote quote = getQuoteObj(quoteId);
		LOGGER.info("Before Constructing quote for product {}", quoteDetail.getProductName());
		constructQuote(productFamily, user, quote);
		response.put("quoteId", quote.getId());
		LOGGER.info("Quote construction completed with quote code as {}", quote.getQuoteCode());
		Set<QuoteToLe> quoteToLes = getQuoteToLesObj(quote);
		if (quoteToLes.isEmpty()) {
			for (QuoteToLeDetail quoteToLeDetail : quoteDetail.getQuoteToLes()) {
				QuoteToLe quoteToLe = new QuoteToLe();
				processQuoteToLe(quote, quoteToLes, quoteToLeDetail, quoteToLe, productFamily, user);
				response.put("quoteLeId", quoteToLe.getId());
			}
		} else {
			for (QuoteToLe quoteToLe : quoteToLes) {
				for (QuoteToLeDetail quoteToLeDetail : quoteDetail.getQuoteToLes()) {// assuming that only one le is
																						// selected
					processQuoteToLe(quote, quoteToLes, quoteToLeDetail, quoteToLe, productFamily, user);
				}
			}
		}
		return quote;

	}

	/**
	 * constructQuoteToLe
	 * 
	 * @param quote
	 * @param quoteToLes
	 * @param quoteToLeDetail
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	private void processQuoteToLe(Quote quote, Set<QuoteToLe> quoteToLes, QuoteToLeDetail quoteToLeDetail,
			QuoteToLe quoteToLe, MstProductFamily productFamily, User user) throws TclCommonException {
		constructQuoteToLe(quote, quoteToLes, quoteToLeDetail, quoteToLe, user);
		QuoteToLeProductFamily quoteToLeProductFamily = constructQuoteToLeProductFamily(quoteToLe, productFamily);
		for (Solution solution : quoteToLeDetail.getSolutions()) {
			constructProductSolution(quoteToLe, productFamily, quoteToLeProductFamily, solution);
		}
	}

	/**
	 * constructProductSolution
	 * 
	 * @param quoteToLe
	 * @param productFamily
	 * @param quoteToLeProductFamily
	 * @param solution
	 * @throws TclCommonException
	 */
	private void constructProductSolution(QuoteToLe quoteToLe, MstProductFamily productFamily,
			QuoteToLeProductFamily quoteToLeProductFamily, Solution solution) throws TclCommonException {
		String productOffering = solution.getProfilename();
		MstProductOffering productOfferng = getProductOffering(productFamily, productOffering);
		ProductSolution productSolution = productSolutionRepository
				.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
		if (productSolution == null) {
			productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
					Utils.convertObjectToJson(solution.getSolution()));
			productSolution.setSolutionCode(Utils.generateUid());
			productSolutionRepository.save(productSolution);
		}
		for (Site site : solution.getSites()) {
			constructIllSites(productSolution, site, productFamily, quoteToLe.getQuote(), solution);
		}
	}

	/**
	 * constructQuoteToLeProductFamily
	 * 
	 * @param quoteToLe
	 * @param productFamily
	 * @return
	 */
	private QuoteToLeProductFamily constructQuoteToLeProductFamily(QuoteToLe quoteToLe,
			MstProductFamily productFamily) {
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
		if (quoteToLeProductFamily == null) {
			quoteToLeProductFamily = constructQuoteToLeProductFamily(productFamily, quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		}
		return quoteToLeProductFamily;
	}

	/**
	 * constructQuoteToLe
	 * 
	 * @param quote
	 * @param quoteToLes
	 * @param quoteToLeDetail
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	private void constructQuoteToLe(Quote quote, Set<QuoteToLe> quoteToLes, QuoteToLeDetail quoteToLeDetail,
			QuoteToLe quoteToLe, User user) throws TclCommonException {
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
		quoteToLe.setTermInMonths(quoteToLeDetail.getTermInMonths());
		quoteToLe.setCurrencyCode(quoteToLeDetail.getCurrency());
		quoteToLe.setErfCusCustomerLegalEntityId(quoteToLeDetail.getErfCustomerLeId());
		quoteToLe.setErfCusSpLegalEntityId(quoteToLeDetail.getErfSupplierLeId());
		quoteToLe.setQuoteType("NEW");
		quoteToLeRepository.save(quoteToLe);
		quoteToLes.add(quoteToLe);
		persistQuoteLeAttributes(user, quoteToLe);
	}

	/**
	 * getQuoteToLesObj - get the quoteToLe Obj
	 * 
	 * @param quote
	 */
	private Set<QuoteToLe> getQuoteToLesObj(Quote quote) {
		Set<QuoteToLe> quoteToLes = null;
		if (quote.getQuoteToLes() != null) {
			LOGGER.info("This is an update request so fetching the existing quoteToLe for quoteId {}", quote.getId());
			quoteToLes = quote.getQuoteToLes();
		} else {
			LOGGER.info("Initializing quoteToLes for quote code {}", quote.getQuoteCode());
			quoteToLes = new HashSet<>();
		}
		return quoteToLes;
	}

	/**
	 * constructQuote
	 * 
	 * @param productFamily
	 * @param user
	 * @param quote
	 */
	private void constructQuote(MstProductFamily productFamily, User user, Quote quote) {
		quote.setCustomer(user.getCustomer());
		quote.setCreatedBy(user.getId());
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setQuoteCode(Utils.generateRefId(productFamily.getName()));
		quoteRepository.save(quote);
	}

	/**
	 * getQuoteObj - initializing the quote
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	private Quote getQuoteObj(Integer quoteId) throws TclCommonException {
		Quote quote = null;
		if (quoteId != null) {
			LOGGER.info("Entering quote with the quote Id {}", quoteId);
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				quote = quoteEntity.get();
			} else {
				LOGGER.warn("The Quote given for update {} is not found!!!", quoteId);
				throw new TclCommonException(ExceptionConstants.QUOTE_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);
			}
		} else {
			LOGGER.info("Initializing a new create Quote");
			quote = new Quote();
		}
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
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		quoteToLe.setTermInMonths("12 months");
		quoteToLe.setCurrencyCode("INR");
		quoteToLe.setQuoteType("NEW");
//		quoteToLe.setClassification(quoteDetail.getClassification());

		LOGGER.info("constructing quote to le " + quoteToLe);
		return quoteToLe;
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
	 * getProductOffering - This method takes in the
	 * {@link MstProductFamily},productOfferingName and gets back
	 * {@link MstProductOffering}
	 * 
	 * @param mstProductFamily
	 * @param productOfferingName
	 * @return MstProductOffering
	 * @throws TclCommonException
	 */
	protected MstProductOffering getProductOffering(MstProductFamily mstProductFamily, String productOfferingName)
			throws TclCommonException {
		MstProductOffering productOffering = null;

		productOffering = mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(mstProductFamily,
				productOfferingName, (byte) 1);
		if (productOffering == null) {
			productOffering = new MstProductOffering();
			productOffering.setCreatedBy(Utils.getSource());
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
	 * @param siteId
	 */
	private void removeComponentsAndAttr(Integer siteId) {
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
	 * construct Mst Properties
	 * 
	 * @param id
	 * @param localITContactId
	 */
	private MstProductComponent getMstProperties() {
		LOGGER.info("Getting master properties");
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(Utils.getSource());
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
	 * 
	 * constructIllSites- This methods is used to construct the IllSites entity
	 * 
	 * @param productSolution
	 * @param userId
	 * @return void
	 * @throws TclCommonException
	 */
	private void constructIllSites(ProductSolution productSolution, Site site, MstProductFamily productFamily,
			Quote quote, Solution solution) throws TclCommonException {
		if (site.getSiteId() == null) {
			QuoteIllSite illSite = new QuoteIllSite();
			illSite.setErfLocSitebLocationId(site.getLocationId());
			LOGGER.info("product solution before site call" + productSolution.getId());

			illSite.setProductSolution(productSolution);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 60); // Adding 60 days
			illSite.setEffectiveDate(cal.getTime());
			illSite.setCreatedBy(quote.getCreatedBy());
			illSite.setCreatedTime(new Date());
			illSite.setStatus((byte) 1);
			illSite.setImageUrl(site.getImage());
			illSite.setSiteCode(Utils.generateUid());
			illSite.setFeasibility((byte) 1);//DUMMY
			illSite.setFpStatus("FP");//DUMMY
			illSite.setIsTaskTriggered(0);
			illSiteRepository.save(illSite);
			LOGGER.info("Ill site created");
			dummyFeasibility(illSite);
			
			//DUMMY ENDS
			for (Component componentDetail : site.getComponents()) {
				processProductComponent(productFamily, illSite, componentDetail);
			}
			// Initializing siteProperty
			MstProductComponent sitePropComp = getMstProperties();
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
		} else {
			QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(site.getSiteId(), (byte) 1);
			if (illSiteEntity != null) {
				illSiteEntity.setProductSolution(productSolution);
				illSiteRepository.save(illSiteEntity);
				removeComponentsAndAttr(illSiteEntity.getId());
				for (Component componentDetail : site.getComponents()) {
					processProductComponent(productFamily, illSiteEntity, componentDetail);
				}
			}
		}

	}

	/**
	 * dummyFeasibility
	 * @param illSite
	 */
	private void dummyFeasibility(QuoteIllSite illSite) {
		//STORING DUMMY FEASIBILITY
		SiteFeasibility siteFeasibility=new SiteFeasibility();
		LocalDateTime localDateTime = LocalDateTime.now();
		siteFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
		siteFeasibility.setFeasibilityCheck("system");
		siteFeasibility.setFeasibilityCode(Utils.generateUid());
		siteFeasibility.setFeasibilityMode("OnnetWL");
		siteFeasibility.setIsSelected(CommonConstants.BACTIVE);
		siteFeasibility.setProvider("TATA COMMUNICATIONS");
		siteFeasibility.setQuoteIllSite(illSite);
		siteFeasibility.setRank(siteFeasibility.getRank());
		siteFeasibility.setResponseJson("{\"lm_nrc_ospcapex_onwl\":0,\"hh_name\":\"SC 01-893225158\",\"total_cost\":\"258870.0\",\"X0.5km_avg_dist\":292,\"POP_DIST_KM\":0.37785122,\"X0.5km_prospect_avg_bw\":199.6993,\"OnnetCity_tag\":1,\"lm_arc_bw_prov_ofrf\":0,\"Network_F_NF_CC\":\"No CC Found\",\"lm_nrc_nerental_onwl\":0,\"lm_nrc_inbldg_onwl\":40000,\"Network_Feasibility_Check\":\"Feasible\",\"core_check_CC\":\"NA\",\"X5km_min_bw\":0,\"access_check_CC\":\"NA\",\"rank\":0,\"POP_DIST_KM_SERVICE_MOD\":15,\"X5km_max_bw\":10240,\"X5km_prospect_perc_feasible\":0.94897425,\"lm_arc_bw_onrf\":0,\"POP_Category\":\"Metro Service Ready\",\"lm_nrc_bw_prov_ofrf\":0,\"FATG_Building_Type\":\"Commercial\",\"hh_flag\":\"1.0\",\"local_loop_interface\":\"FE\",\"X5km_prospect_avg_dist\":2751.825,\"latitude_final\":13.012676,\"FATG_Network_Location_Type\":\"Access/Customer POP\",\"topology\":\"primary_active\",\"city_tier\":\"Tier1\",\"lm_nrc_bw_onwl\":0,\"X2km_prospect_min_dist\":28.196367,\"X2km_prospect_perc_feasible\":0.9572108,\"X0.5km_prospect_num_feasible\":142,\"cpe_supply_type\":\"rental\",\"additional_ip_flag\":\"No\",\"Orch_Connection\":\"Wireline\",\"site_id\":\"47123_primary\",\"cpe_management_type\":\"unmanaged\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"error_flag\":0,\"resp_city\":\"Chennai\",\"connected_cust_tag\":0,\"net_pre_feasible_flag\":1,\"Network_F_NF_CC_Flag\":\"0.0\",\"pop_long\":80.27853,\"Orch_LM_Type\":\"Onnet\",\"ipv4_address_pool_size\":\"29\",\"X5km_prospect_avg_bw\":153.98943,\"FATG_Category\":\"Metro Service Ready\",\"lm_nrc_mast_ofrf\":0.0,\"X2km_prospect_min_bw\":1,\"X5km_prospect_num_feasible\":1804,\"Selected\":true,\"pop_name\":\"VSB CHENNAI-6638637\",\"lm_nrc_mast_onrf\":0,\"X5km_prospect_count\":1901,\"error_msg\":\"No error\",\"connection_type\":\"Standard\",\"ip_address_arrangement\":\"None\",\"X0.5km_avg_bw\":8,\"X2km_cust_count\":410,\"customer_segment\":\"Enterprise-Direct\",\"FATG_PROW\":\"No\",\"connected_building_tag\":1,\"FATG_DIST_KM\":49.3265,\"X0.5km_prospect_max_bw\":10240,\"X5km_prospect_min_dist\":28.196367,\"Type\":\"OnnetWL\",\"FATG_Ring_type\":\"SDH\",\"longitude_final\":80.20131,\"Orch_BW\":20,\"scenario_1\":1,\"scenario_2\":0,\"access_check_hh\":\"NA\",\"X2km_prospect_count\":631,\"ipv6_address_pool_size\":\"0\",\"min_hh_fatg\":0.0,\"POP_Construction_Status\":\"In Service\",\"X5km_avg_dist\":3096.4746,\"X2km_avg_dist\":911.2313,\"X2km_prospect_avg_bw\":169.38197,\"X0.5km_prospect_min_dist\":28.196367,\"cost_permeter\":800,\"prospect_name\":\"Regus\",\"Predicted_Access_Feasibility\":\"Feasible\",\"X2km_max_bw\":10240,\"X2km_prospect_max_bw\":10240,\"last_mile_contract_term\":\"12 months\",\"X5km_prospect_min_bw\":0.064,\"X2km_min_bw\":1,\"burstable_bw\":20.0,\"pop_network_loc_id\":\"TINDTNCHENANAR0004\",\"bw_mbps\":20.0,\"X0.5km_cust_count\":98,\"X0.5km_min_bw\":1,\"product_name\":\"Internet Access Service\",\"Network_F_NF_HH\":\"No HH/core ring on POP\",\"X0.5km_min_dist\":91,\"POP_Building_Type\":\"Commercial\",\"core_check_hh\":\"NA\",\"HH_DIST_KM\":93.0,\"X0.5km_prospect_min_bw\":2,\"error_code\":\"NA\",\"POP_DIST_KM_SERVICE\":10437.939,\"lm_arc_bw_onwl\":160060,\"num_connected_building\":1,\"account_id_with_18_digit\":\"0012000000BSomfAAD\",\"Orch_Category\":\"Connected Building\",\"X0.5km_prospect_avg_dist\":272.03018,\"POP_Network_Location_Type\":\"Mega POP\",\"lm_nrc_mux_onwl\":58810,\"num_connected_cust\":0,\"HH_0_5km\":\"SC 01-893225158\",\"pop_lat\":13.068643,\"X5km_min_dist\":91.97047,\"feasibility_response_created_date\":\"2020-01-11\",\"X2km_prospect_num_feasible\":604,\"X2km_avg_bw\":56,\"X0.5km_max_bw\":100,\"X5km_avg_bw\":21,\"X5km_prospect_max_bw\":10240,\"X2km_prospect_avg_dist\":933.32623,\"cpe_variant\":\"None\",\"X5km_cust_count\":1542,\"sum_no_of_sites_uni_len\":1,\"X0.5km_prospect_perc_feasible\":0,\"POP_TCL_Access\":\"Yes\",\"X0.5km_prospect_count\":143,\"Probabililty_Access_Feasibility\":0.99,\"lm_nrc_bw_onrf\":0,\"quotetype_quote\":\"New Order\",\"pop_address\":\"Tata_Commuications,_5thFloor,_ITMC_5,_Swami_Sivananda_Salai,_Chennai-_600002,_India\",\"X2km_min_dist\":91.97047,\"FATG_TCL_Access\":\"No\",\"Network_F_NF_HH_Flag\":\"0.0\",\"solution_type\":\"MAN\",\"cu_le_id\":\"282707\",\"ll_change\":\"None\",\"macd_option\":\"Yes\",\"trigger_feasibility\":\"Yes\",\"old_contract_term\":\"None\",\"service_commissioned_date\":\"None\",\"lat_long\":\"None\",\"old_Port_Bw\":\"None\",\"old_Ll_Bw\":\"None\",\"backup_port_requested\":\"No\",\"service_id\":\"None\",\"parallel_run_days\":\"None\",\"cpe_chassis_changed\":\"None\",\"local_loop_bw\":20.0,\"Compressed_Internet_Ratio\":\"0:0\",\"partner_account_id_with_18_digit\":\"None\",\"partner_profile\":\"None\",\"quotetype_partner\":\"None\",\"version\":\"2.01\",\"user_name\":\"optimus_regus\",\"user_type\":\"customer\",\"error_msg_display\":\"Feasible\",\"Product.Name\":\"Internet Access Service\",\"mux\":\"NA\",\"access_rings_hh\":\"NA\"}");
		siteFeasibility.setType("primary");
		siteFeasibilityRepository.save(siteFeasibility);
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
	private void processProductComponent(MstProductFamily productFamily, QuoteIllSite illSite, Component component)
			throws TclCommonException {
		try {
			MstProductComponent productComponent = getProductComponent(component);
			QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily,
					illSite.getId());
			quoteComponent.setType(component.getType());
			quoteProductComponentRepository.save(quoteComponent);
			LOGGER.info("saved successfully");
			for (Attribute attribute : component.getAttributes()) {
				processProductAttribute(quoteComponent, attribute);
			}
		} catch (Exception e) {
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
	private MstProductComponent getProductComponent(Component component) throws TclCommonException {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(component.getName(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setName(component.getName());
			mstProductComponent.setCreatedBy(Utils.getSource());
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
			MstProductFamily mstProductFamily, Integer illSiteId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
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
	private void processProductAttribute(QuoteProductComponent quoteComponent, Attribute attribute)
			throws TclCommonException {
		try {
			ProductAttributeMaster productAttribute = getProductAttributes(attribute);
			QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
					productAttribute, attribute);
			quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
		} catch (TclCommonException e) {
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
	private ProductAttributeMaster getProductAttributes(Attribute attributeDetail) throws TclCommonException {
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
			productAttributeMaster.setCreatedBy(Utils.getSource());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
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
			ProductAttributeMaster productAttributeMaster, Attribute attributeDetail) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;

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
		attributeValue.setAttributeValue(attribute.getAttributeValue());
		attributeValue.setDisplayValue(attribute.getAttributeName());
		attributeValue.setQuoteToLe(quoteToLe);
		MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
		attributeValue.setMstOmsAttribute(mstOmsAttribute);
		quoteLeAttributeValueRepository.save(attributeValue);
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
		quoteLeAttributeValues.forEach(attr -> {
			if (!attr.getMstOmsAttribute().getName().equalsIgnoreCase("Payment Currency")
					&& !attr.getMstOmsAttribute().getName().equalsIgnoreCase("Billing Currency")) {
				attr.setAttributeValue(attribute.getAttributeValue());
				quoteLeAttributeValueRepository.save(attr);
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

}
