package com.tcl.dias.oms.gde.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MACDExistingComponentsBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.GdeScheduleDetails;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.LinkFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OdrScheduleDetails;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.GdeScheduleDetailsRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OdrScheduleDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gde.beans.GdeLinkBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteDetail;
import com.tcl.dias.oms.gde.beans.GdeScheduleDetailBean;
import com.tcl.dias.oms.gde.beans.GdeSite;
import com.tcl.dias.oms.gde.beans.GdeSiteDetail;
import com.tcl.dias.oms.gde.beans.LinkFeasibilityBean;
import com.tcl.dias.oms.gde.beans.ProductSolutionBean;
import com.tcl.dias.oms.gde.beans.QuoteGdeSiteBean;
import com.tcl.dias.oms.gde.beans.QuoteToLeBean;
import com.tcl.dias.oms.gde.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional
public class GdeQuoteService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(GdeQuoteService.class);
	
	@Autowired
	protected MQUtils mqUtils;
	
	@Autowired
	protected MACDUtils macdUtils;
	
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
	CustomerRepository customerRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	protected NplLinkRepository nplLinkRepository;
	
	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;
	
	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;
	
	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;
	
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@Autowired
	MstProductComponentRepository mstProductComponentRepository;
		
	@Autowired
	protected QuoteNplLinkSlaRepository nplLinkSlaRepository;
	
	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;
	
	@Autowired
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;
	
	@Autowired
	LinkFeasibilityAuditRepository linkFeasibilityAuditRepository;
	
	@Value("${rabbitmq.location.detail}")
	String locationDetailQueue;
	
	@Autowired
	protected QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	QuotePriceRepository quotePriceRepository;
	
	@Autowired
	GdeScheduleDetailsRepository gdeScheduleDetailsRepository;
	
	@Autowired
	OdrScheduleDetailsRepository odrScheduleDetailsRepository;
	
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
	protected QuoteToLe processQuote(GdeQuoteDetail quoteDetail, Integer erfCustomerId, User user)
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
			quote = new Quote();
			quote.setCustomer(customer);
			quote.setCreatedBy(user.getId());
			quote.setCreatedTime(new Date());
			quote.setStatus((byte) 1);
			quote.setQuoteCode(Utils.generateRefId(quoteDetail.getProductName()));
			quote.setNsQuote("N");
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
			quoteToLeProductFamily = new QuoteToLeProductFamily();
			quoteToLeProductFamily.setMstProductFamily(productFamily);
			quoteToLeProductFamily.setQuoteToLe(quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		} else {
			removeUnselectedSolution(quoteDetail, quoteToLeProductFamily, quoteToLe);
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
	 * constructQuoteToLe
	 * 
	 * @param quote
	 * @return
	 */
	private QuoteToLe constructQuoteToLe(Quote quote) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(QuoteStageConstants.MODIFY.getConstantCode());
		quoteToLe.setTermInMonths("12 months");
		quoteToLe.setCurrencyCode("INR");
		quoteToLe.setQuoteType("NEW");
		quoteToLe.setIsMultiCircuit(CommonConstants.BDEACTIVATE);
		return quoteToLe;
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
	
	private void removeUnselectedSolution(GdeQuoteDetail quoteDetail, QuoteToLeProductFamily quoteToLeProductFamily,
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
			}

		}
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
	 * deActivateSite - Method to deactivate a site
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
	 * deActivateLink - Method to deactivate a link
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

	private void removeQuoteIllSitetoServiceValues(Integer linkId, Integer quoteId)
	{
		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId);
		QuoteNplLink link = nplLinkRepository.findByIdAndStatus(linkId, BACTIVE);
		LOGGER.info("Link Id Before deleting value in QuoteIllSiteService: "+link.getId());
		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get(0).getQuoteType())) {
			List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(linkId, quoteToLe.get(0));
			if(Objects.nonNull(quoteIllSiteToService) && !(quoteIllSiteToService.isEmpty()))
			{
				quoteIllSiteToService.get(0).setQuoteNplLink(null);
				quoteIllSiteToServiceRepository.save(quoteIllSiteToService.get(0));
				LOGGER.info("Removed link from QuoteIllSiteToServiceRepository");
			}
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
			String refType = QuoteConstants.GDE_SITES.toString();
			if (component.getType() != null && component.getType().equalsIgnoreCase(CommonConstants.LINK)) {
				id = link.getId();
				refType = QuoteConstants.GDE_LINK.toString();
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
			Map<String,QuoteProductComponentsAttributeValue> attributeUpdate = new HashMap<>();
			for (AttributeDetail attribute : component.getAttributes()) {
				processProductAttribute(quoteComponent, attribute, user, locA, locB, attributeUpdate);
			}
			if(Arrays.asList(CommonConstants.BOD_SCHEDULE_START_DATE,CommonConstants.BOD_SCHEDULE_END_DATE,CommonConstants.SLOTS,
					CommonConstants.SCHEDULE_DURATION).containsAll(attributeUpdate.keySet()) && attributeUpdate.size() >= 4) {
				QuoteProductComponentsAttributeValue startDate = attributeUpdate.get(CommonConstants.BOD_SCHEDULE_START_DATE);
				QuoteProductComponentsAttributeValue endDate = attributeUpdate.get(CommonConstants.BOD_SCHEDULE_END_DATE);
				QuoteProductComponentsAttributeValue slots = attributeUpdate.get(CommonConstants.SLOTS);
				QuoteProductComponentsAttributeValue scheduleDuration = attributeUpdate.get(CommonConstants.SCHEDULE_DURATION);
				int slotsCount = Utils.findDifferenceInDays(startDate.getAttributeValues(), endDate.getAttributeValues());
				slots.setAttributeValues(String.valueOf(slotsCount));
				scheduleDuration.setAttributeValues(String.valueOf(slotsCount*24));
				quoteProductComponentsAttributeValueRepository.saveAll(Arrays.asList(slots,scheduleDuration));
				Optional<ProductSolution> productSolutionDetails = productSolutionRepository.findById(link.getProductSolutionId());
				if(productSolutionDetails.isPresent()) {
					QuoteToLe quoteToLe = productSolutionDetails.get().getQuoteToLeProductFamily().getQuoteToLe();
					quoteToLe.setTermInMonths(slotsCount+"days");
					quoteToLeRepository.save(quoteToLe);
					LOGGER.info("Updated contract terms for the quoteleid {} for the linkid {} ",quoteToLe.getId(), link.getId());
				}
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
			Integer locA, Integer locB, Map<String,QuoteProductComponentsAttributeValue> attributeUpdate) throws TclCommonException {
		try {
			ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
			QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
					productAttribute, attribute, locA, locB);
			quoteProductAttribute = quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);
			
			if (productAttribute.getName().equals(CommonConstants.BOD_SCHEDULE_START_DATE) || productAttribute.getName().equals(CommonConstants.BOD_SCHEDULE_END_DATE) 
					|| productAttribute.getName().equals(CommonConstants.SLOTS) || productAttribute.getName().equals(CommonConstants.SCHEDULE_DURATION)) {
				attributeUpdate.put(productAttribute.getName(), quoteProductAttribute);
			}
				
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
	 * @param quoteIllSite
	 * @link http://www.tatacommunications.com/ deletedIllsiteAndRelation used to
	 *       delete ill site and its relation
	 * 
	 * @param quoteIllSite
	 */
	private void deleteNplsiteAndRelation(QuoteIllSite quoteIllSite) {
//		List<QuoteIllSiteSla> slas = quoteIllSiteSlaRepository.findByQuoteIllSite(quoteIllSite);
//		if (slas != null && !slas.isEmpty()) {
//			slas.forEach(sl -> {
//				quoteIllSiteSlaRepository.delete(sl);
//				quoteIllSiteSlaRepository.flush();
//
//			});
//		}
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibilities.forEach(site -> {
				siteFeasibilityRepository.delete(site);

			});
		}

		illSiteRepository.delete(quoteIllSite);

	}
	
	/**
	 * getUserAddress
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
	 * This method is used for validating the site information
	 * validateSiteInformation
	 * 
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	protected void validateSiteInformation(GdeQuoteDetail quoteDetail, Integer quoteId) throws TclCommonException {
		if ((quoteDetail == null) || quoteDetail.getSite() == null || quoteDetail.getLinkCount() == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
		if (Objects.isNull(quoteId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
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
	 * @param productSolution
	 * @param productOfferingName
	 * @throws TclCommonException
	 */
	protected Integer processSiteDetail(User user, MstProductFamily productFamily,
			QuoteToLeProductFamily quoteToLeProductFamily, GdeSite site, String productOfferingName,
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
	 * constructIllSites- This methods is used to construct the IllSites entity
	 * 
	 * @param productSolution
	 * @param userId
	 * @return void
	 * @throws TclCommonException
	 */
	private Integer constructIllSites(ProductSolution productSolution, User user, GdeSite site,
			MstProductFamily productFamily, String productOfferingName, boolean siteChanged) throws TclCommonException {

		Integer siteId = null;
		SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);

		List<GdeSiteDetail> siteInp = site.getSite();
		for (GdeSiteDetail siteDetail : siteInp) {
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
				siteId = illSite.getId();
				LOGGER.info("Create Site {} for GDE Macd and product solution id {}",siteId, productSolution.getId());

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

//			QuoteToLe quoteToLe = optProdSol.get().getQuoteToLeProductFamily().getQuoteToLe();

			deActivateLinkAndSites(optNplLink.get(), action);
//			nplPricingFeasibilityService.recalculateSites(quoteToLe.getId());
			result = "SUCCESS";

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return result;

	}
	/**
	 * @author archchan Method to construct GDE links
	 * @param siteAId
	 * @param siteBId
	 * @param productSolution
	 * @throws TclCommonException
	 */
	protected void constructNplLinks(User user, MstProductFamily productFamily, Integer quoteId, Integer siteAId,
			Integer siteBId, ProductSolution productSolution, final String siteAType, final String siteBType,
			String productOfferingName, Integer locA, Integer locB, String serviceId) throws TclCommonException {
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
		//	link.setIsTaskTriggered(0);
			// Implemented as in ILL
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 60); // Adding 60 days
			link.setEffectiveDate(cal.getTime());
			link = nplLinkRepository.save(link);
			LOGGER.info("Link Id: "+ link.getId());
//			link.setQuoteNplLinkSlas(constuctNplLinkSla(link, productOfferingName));
		}

		LOGGER.info("Service Id before storing link id in QuoteIllSiteService: "+serviceId);
		if (StringUtils.isNotEmpty(serviceId) && StringUtils.isNotBlank(serviceId))
		{
			LOGGER.info("Service Id after storing link id in QuoteIllSiteService: "+serviceId);
			QuoteToLe quoteToLe = productSolution.getQuoteToLeProductFamily().getQuoteToLe();
			if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {

				List<QuoteIllSiteToService> quoteSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);

				if (quoteSiteToService != null && !quoteSiteToService.isEmpty() && quoteSiteToService.get(0).getQuoteNplLink() == null) {
					quoteSiteToService.get(0).setQuoteNplLink(link);
					quoteIllSiteToServiceRepository.save(quoteSiteToService.get(0));
					LOGGER.info("Saved GDE link to QuoteIllSiteToService for the serviceid {}",quoteSiteToService.get(0).getErfServiceInventoryTpsServiceId());

				}
			}
		}
		Map<String,QuoteProductComponentsAttributeValue> attributeUpdate = new HashMap<>();
		for (ComponentDetail componentDetail : soDetail.getComponents()) {
			processProductComponent(productFamily, link, componentDetail, user, locA, locB);
		}
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
	public GdeQuoteBean getQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProp)
			throws TclCommonException {
		GdeQuoteBean response = null;
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

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_GET_QUOTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
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
	protected GdeQuoteBean constructQuote(Quote quote, Boolean isFeasibleSites, Boolean isSiteProp)
			throws TclCommonException {
		GdeQuoteBean quoteDto = new GdeQuoteBean();
		quoteDto.setQuoteId(quote.getId());
		quoteDto.setQuoteCode(quote.getQuoteCode());
		quoteDto.setCreatedBy(quote.getCreatedBy());
		quoteDto.setCreatedTime(quote.getCreatedTime());
		quoteDto.setStatus(quote.getStatus());
		quoteDto.setTermInMonths(quote.getTermInMonths());
		if (quote.getCustomer() != null) {
			quoteDto.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		}
		quoteDto.setLegalEntities(constructQuoteLeEntitDtos(quote, isFeasibleSites, isSiteProp));
		return quoteDto;

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
				quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
				quoteToLeDto.setCurrency(quTle.getCurrencyCode());
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
				productSolutionBean.setLinks(getSortedNplLinks(constructNplLinkBeans(solution, isFeasibleSites, isSiteProp)));
				productSolutionBeans.add(productSolutionBean);

			}
		}
		return productSolutionBeans;
	}

	
	/**
	 * @param solution
	 * @param isFeasibleSites
	 * @param isSiteProp
	 * @return
	 */
	private List<GdeLinkBean> constructNplLinkBeans(ProductSolution solution, Boolean isFeasibleSites,
			Boolean isSiteProp) {
		List<QuoteNplLink> listOfLinks = null;
		if (isFeasibleSites)
			listOfLinks = nplLinkRepository.findByProductSolutionIdAndStatus(solution.getId(), (byte) 1);
		else
			listOfLinks = nplLinkRepository.findByProductSolutionIdAndStatusAndFeasibility(solution.getId(), (byte) 1,
					(byte) 1);

		return (listOfLinks == null || listOfLinks.isEmpty()) ? new ArrayList<>()
				: listOfLinks.stream().map(link -> constructNplLinkBean(link, isFeasibleSites, isSiteProp))
						.collect(Collectors.toList());
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
	private GdeLinkBean constructNplLinkBean(QuoteNplLink link, Boolean isFeasibleSites, Boolean isSiteProp) {
		if (isSiteProp == null) {
			isSiteProp = false;
		}
		GdeLinkBean linkDto = new GdeLinkBean(link);
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
		linkDto.setSites(getSortedNplSiteBeans(constructGdeSiteBeans(siteMap, isFeasibleSites)));
		
		GdeScheduleDetails gdeSchedules = gdeScheduleDetailsRepository.findByLinkId(link.getId());	
		Order order = null;
		if(gdeSchedules != null) {
			order = orderRepository.findByOrderCode(gdeSchedules.getQuoteCode());
		}
		if(gdeSchedules != null && System.currentTimeMillis() > gdeSchedules.getFeasibilityValidity().getTime() && order == null) {
			List<LinkFeasibility> linkFeas = linkFeasibilityRepository.findByQuoteNplLink(link);
			LOGGER.info("Updating Link feasibility {} for link id {} as feasibility expired",linkFeas.get(0).getId(), linkFeas.get(0).getQuoteNplLink().getId());
			linkFeas.get(0).setIsSelected((byte) 0);
			linkFeasibilityRepository.save(linkFeas.get(0));
			LOGGER.info("Updating gde schedule details {} for ordercode {} as feasibility expired",gdeSchedules.getLinkId(), gdeSchedules.getQuoteCode());
			gdeSchedules.setIsActive((byte) 0);
			gdeScheduleDetailsRepository.save(gdeSchedules);
		} else {
			linkDto.setLinkFeasibility(constructLinkFeasibility(link));
		}
//		linkDto.setQuoteSla(constructNplSlaDetails(link));
	//	linkDto.setIsTaskTriggered(link.getIsTaskTriggered());
		Optional<QuoteIllSite> siteAOpt = illSiteRepository.findById(link.getSiteAId());
		if(siteAOpt.isPresent()) {
			QuoteToLe quoteToLe = siteAOpt.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
		{
			
			linkDto
					.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe, link)));
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
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param links,version
	 * @return List<NplLinkBean>
	 */
	private List<GdeLinkBean> getSortedNplLinks(List<GdeLinkBean> links) {
		if (links != null) {
			links.sort(Comparator.comparingInt(GdeLinkBean::getId));

		}

		return links;
	}
	
	/**
	 * @link http://www.tatacommunications.com/ constructNplSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<QuoteGdeSiteBean> constructGdeSiteBeans(Map<String, QuoteIllSite> gdeSites, Boolean isFeasibleSites) {
		List<QuoteGdeSiteBean> sites = new ArrayList<>();
		if (gdeSites != null) {
			for (Map.Entry<String, QuoteIllSite> entry : gdeSites.entrySet()) {
				QuoteIllSite gdeSite = entry.getValue();
				String type = entry.getKey().split("_")[1];
				if (gdeSite.getStatus() == 1) {
					QuoteGdeSiteBean gdeSiteBean = new QuoteGdeSiteBean(gdeSite);
					// nplSiteBean.setQuoteSla(constructSlaDetails(nplSite));
					// nplSiteBean.setFeasibility(constructSiteFeasibility(nplSite));
					gdeSiteBean.setType(type);
					sites.add(gdeSiteBean);
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
	 * @link http://www.tatacommunications.com
	 * @param quoteProductComponentsAttributeValues
	 * @return
	 */
	private List<QuoteProductComponentsAttributeValueBean> constructAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues) {
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean = new ArrayList<>();
		if (quoteProductComponentsAttributeValues != null) {
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {

				QuoteProductComponentsAttributeValueBean qtAttributeValue = new QuoteProductComponentsAttributeValueBean(
						attributeValue);
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
		MstProductFamily prodFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.GDE,
				CommonConstants.BACTIVE);
		if (isSitePropertiesNeeded) {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(id,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.GDE_LINK.toString());
		} else if (isSiteProp) {
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductFamilyAndType(id, prodFamily,
					type);
			components.addAll(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(id,
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.GDE_LINK.toString()));
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
	private List<QuoteGdeSiteBean> getSortedNplSiteBeans(List<QuoteGdeSiteBean> gdeSiteBeans) {
		if (gdeSiteBeans != null) {
			gdeSiteBeans.sort(Comparator.comparingInt(QuoteGdeSiteBean::getSiteId));

		}

		return gdeSiteBeans;
	}
	
	/**
	 * constructSiteFeasibility
	 * 
	 * @param illSite
	 * @return
	 */
	private List<LinkFeasibilityBean> constructLinkFeasibility(QuoteNplLink nplLink) {
		//gde for link id, and chk feasibilit validity-> if currenttimestamp > feasibilityvalidity then update linkfeasibility-> Isselected 0, updatedtime & delete gde schedule & update quote_npl_link-FPstatus 'N'
		
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

	private GdeScheduleDetailBean constructGdeScheduleBeans(QuoteNplLink nplLink) {
		GdeScheduleDetailBean gdeScheduleDetailBean = new GdeScheduleDetailBean();
		GdeScheduleDetails schedules = gdeScheduleDetailsRepository.findByLinkId(nplLink.getId());
		if(schedules != null) {
			gdeScheduleDetailBean.setActivationStatus(schedules.getActivationStatus());
			gdeScheduleDetailBean.setCreatedTime(Utils.convertTimeStampToString(schedules.getCreatedTime()));
			gdeScheduleDetailBean.setFeasibilityStatus(schedules.getFeasibilityStatus());
			gdeScheduleDetailBean.setFeasibilityValidity(Utils.convertTimeStampToString(schedules.getFeasibilityValidity()));
			gdeScheduleDetailBean.setMdsoFeasibilityUuid(schedules.getMdsoFeasibilityUuid());
			gdeScheduleDetailBean.setMdsoResourceId(schedules.getMdsoResourceId());
			gdeScheduleDetailBean.setUpdatedTime(Utils.convertTimeStampToString(schedules.getUpdatedTime()));
			gdeScheduleDetailBean.setServiceId(schedules.getServiceId());
			gdeScheduleDetailBean.setChargeableNrc(schedules.getChargeableNrc());
			gdeScheduleDetailBean.setScheduleStartDate(schedules.getScheduleStartDate().toString());
			gdeScheduleDetailBean.setScheduleEndDate(schedules.getScheduleEndDate().toString());
			
			OdrScheduleDetails orderScheduleDetails = odrScheduleDetailsRepository.findByMdsoResourceIdAndMdsoFeasibilityUuid(schedules.getMdsoResourceId(), schedules.getMdsoFeasibilityUuid());
			if(orderScheduleDetails != null) {
				gdeScheduleDetailBean.setOperationId(orderScheduleDetails.getScheduleOperationId());
				gdeScheduleDetailBean.setTicketId(orderScheduleDetails.getTicketId());
				gdeScheduleDetailBean.setScheduleId(orderScheduleDetails.getScheduleId());
			}
		}
		return gdeScheduleDetailBean;
		
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
		linkFeasibilityBean.setOrderScheduleDetail(constructGdeScheduleBeans(linkFeasibility.getQuoteNplLink()));
		return linkFeasibilityBean;
	}
	
	private List<MACDExistingComponentsBean> generateExistingComponentsForMacd(QuoteToLe quoteToLe, QuoteNplLink link)
	{
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
						.filter(attr -> (!attr.getProductAttributeMaster().getName()
								.equals(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties())))
						.collect(Collectors.toList());
			}

		}

		return attributes;

	}
	
	private List<Map> constructExistingComponentsforIsvPage(QuoteToLe quoteToLe, String serviceId) throws TclCommonException {

		List<Map> existingComponentsList = new ArrayList<>();
		String secondaryServiceId = null;
		Integer primaryOrderId = null;
		Integer secondaryOrderId = null;

		if (Objects.nonNull(quoteToLe) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())
				&& Objects.nonNull(serviceId) ) {
			Map<String, Object> primaryComponentsMap = new HashMap<>();
			SIServiceDetailDataBean primaryServiceDataBean = new SIServiceDetailDataBean();
			SIServiceDetailDataBean sIServiceDetailDataBean = macdUtils.getServiceDetail(serviceId, quoteToLe.getQuoteCategory());
			String linkType = sIServiceDetailDataBean.getLinkType();

			if (linkType.equalsIgnoreCase("PRIMARY") || linkType.equalsIgnoreCase("SINGLE")) {
			
				primaryServiceDataBean = sIServiceDetailDataBean;
			}

			primaryComponentsMap.put("type", MACDConstants.PRIMARY_STRING);
			primaryComponentsMap.put("contractTerm",
					Objects.nonNull(primaryServiceDataBean.getContractTerm()) ? primaryServiceDataBean.getContractTerm()
							: 0);
			primaryComponentsMap.put("portBw",
					Objects.nonNull(primaryServiceDataBean.getPortBw()) ? primaryServiceDataBean.getPortBw()
							: CommonConstants.NULL);
			primaryComponentsMap.put("oldMrc",
					Objects.nonNull(primaryServiceDataBean.getMrc()) ? primaryServiceDataBean.getMrc()
							: CommonConstants.NULL);
			primaryComponentsMap.put("serviceId", Objects.nonNull(serviceId) ? serviceId : CommonConstants.NULL);
			existingComponentsList.add(0, primaryComponentsMap);

		}
		return existingComponentsList;
	
	}
	
	/**
	 *
	 * editSiteComponent used to edit site component values
	 * 
	 * @param request
	 * @return
	 */

	public String editLinkComponent(UpdateRequest request, Integer quoteLeId, Integer linkId)
			throws TclCommonException {
		String response = "Failure";
		try { 
			if (request.getComponentDetails() == null || request.getComponentDetails().isEmpty()
					|| Objects.isNull(quoteLeId) || Objects.isNull(linkId)) {
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
			}
			Map<String, QuoteProductComponentsAttributeValue> attributeUpdate = new HashMap<>();
			for (ComponentDetail cmpDetail : request.getComponentDetails()) {
				for (AttributeDetail attributeDetail : cmpDetail.getAttributes()) {
					LOGGER.info("attribute name and value {} in editLinkComponent Method {}", attributeDetail.getName(), attributeDetail.getValue());
					Optional<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository
							.findById(attributeDetail.getAttributeId());
					if (attributeValue.isPresent()) {
						//Getting start date,end date, slots & schedule duration to calculate slots and duration
						if(attributeValue.get().getProductAttributeMaster().getName().equalsIgnoreCase(CommonConstants.BOD_SCHEDULE_START_DATE)
								|| attributeValue.get().getProductAttributeMaster().getName().equalsIgnoreCase(CommonConstants.BOD_SCHEDULE_END_DATE)
								|| attributeValue.get().getProductAttributeMaster().getName().equalsIgnoreCase(CommonConstants.SLOTS)
								|| attributeValue.get().getProductAttributeMaster().getName().equalsIgnoreCase(CommonConstants.SCHEDULE_DURATION)) {
							attributeUpdate.put(attributeValue.get().getProductAttributeMaster().getName(), attributeValue.get());
						}
						if (!StringUtils.isEmpty(attributeValue.get().getAttributeValues()) && !attributeValue.get().getAttributeValues().equalsIgnoreCase(attributeDetail.getValue())) {
							LOGGER.info("GdeQuoteService. editLinkComponent updating attributeId {} and value ", attributeDetail.getAttributeId(), attributeDetail.getValue());
							attributeValue.get().setAttributeValues(attributeDetail.getValue());								
							quoteProductComponentsAttributeValueRepository.save(attributeValue.get());
						}
					}
					
				}
			}
			//
			if(attributeUpdate.size() >= 4) {
				QuoteProductComponentsAttributeValue startDate = attributeUpdate.get(CommonConstants.BOD_SCHEDULE_START_DATE);
				QuoteProductComponentsAttributeValue endDate = attributeUpdate.get(CommonConstants.BOD_SCHEDULE_END_DATE);
				QuoteProductComponentsAttributeValue slots = attributeUpdate.get(CommonConstants.SLOTS);
				QuoteProductComponentsAttributeValue scheduleDuration = attributeUpdate.get(CommonConstants.SCHEDULE_DURATION);
				if(attributeUpdate != null && endDate != null) {
					LOGGER.info("editLinkComponents - Calculating slots for the QuoteProductComponentsAttributeValue id {} ", slots.getId());
					int slotsCount = Utils.findDifferenceInDays(startDate.getAttributeValues(), endDate.getAttributeValues());
					slots.setAttributeValues(String.valueOf(slotsCount));
					scheduleDuration.setAttributeValues(String.valueOf(slotsCount*24));
					quoteProductComponentsAttributeValueRepository.saveAll(Arrays.asList(slots,scheduleDuration));
					Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);
					if(quoteToLes.isPresent()) {
						QuoteToLe quoteToLe = quoteToLes.get();
						quoteToLe.setTermInMonths(slotsCount+CommonConstants.SPACE+"days");
						quoteToLeRepository.save(quoteToLe);
						LOGGER.info("Inside editLinkComponent Updated contract terms for the quoteleid {} for the linkid {} ",quoteToLe.getId(), linkId);
					}
				}
				
			}
			LOGGER.info("Linked updated successfully");
			response = "Sucess";

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return response;
	}
	
//	/**
//	 * constuctNplLinkSla
//	 * 
//	 * @param link
//	 * @param productOfferingName
//	 * @return
//	 * @throws TclCommonException
//	 */
//	public Set<QuoteNplLinkSla> constuctNplLinkSla(QuoteNplLink link, String productOfferingName)
//			throws TclCommonException {
//		List<SLABean> slaDetails = null;
//		Set<QuoteNplLinkSla> quoteNplLinkSlaSet = new HashSet<>();
//		// 2 profiles of NPL has been merged to a single profile
//		// if
//		// (productOfferingName.toLowerCase().contains(SLAConstants.STANDARD.toString().toLowerCase()))
//		// {
//		slaDetails = getProductSlaDetails(SLAConstants.STANDARD.toString(),
//				SLAConstants.TWO_PATH_PROTECTION.toString());
//		/*
//		 * } else if
//		 * (productOfferingName.toLowerCase().contains(SLAConstants.PREMIUM.toString().
//		 * toLowerCase())) { slaDetails =
//		 * getProductSlaDetails(SLAConstants.PREMIUM.toString(),
//		 * SLAConstants.THREE_PATH_PROTECTION.toString()); }
//		 */
//
//		if (!Objects.isNull(slaDetails)) {
//			for (SLABean slaDetail : slaDetails) {
//				QuoteNplLinkSla quoteNplSla = new QuoteNplLinkSla();
//				quoteNplSla.setQuoteNplLink(link);
//				quoteNplSla.setSlaMaster(slaMasterRepository.findBySlaName(slaDetail.getFactor()));
//				quoteNplSla.setSlaValue(slaDetail.getValue());
//				quoteNplLinkSlaRepository.save(quoteNplSla);
//				quoteNplLinkSlaSet.add(quoteNplSla);
//			}
//		}
//		return quoteNplLinkSlaSet;
//	}
	
}
