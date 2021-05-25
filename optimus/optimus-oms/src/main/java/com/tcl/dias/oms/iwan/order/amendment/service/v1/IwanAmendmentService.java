package com.tcl.dias.oms.iwan.order.amendment.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.oms.constants.MACDConstants.CHANGE_BANDWIDTH_SERVICE;
import static com.tcl.dias.oms.constants.MACDConstants.MACD_QUOTE_TYPE;
import static com.tcl.dias.oms.constants.MACDConstants.NEW;
import static com.tcl.dias.oms.constants.MACDConstants.SHIFT_SITE;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.ordertocash.beans.ScServiceDetailForOrderAmend;
import com.tcl.dias.common.ordertocash.beans.SiteToService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.CheckAmendmentQuoteBean;
import com.tcl.dias.oms.beans.LastMileProviderDetails;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OrderAmendmentStatusBean;
import com.tcl.dias.oms.beans.QuoteBeanForOrderAmendment;
import com.tcl.dias.oms.beans.QuoteOrderAmendmentBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.ServiceDetailBeanForAmendment;
import com.tcl.dias.oms.beans.ServiceDetailsForASite;
import com.tcl.dias.oms.beans.SiteToServiceMapping;
import com.tcl.dias.oms.beans.SiteUpdateForAmendmentBean;
import com.tcl.dias.oms.beans.SolutionToSiteMapping;
import com.tcl.dias.oms.beans.VendorDetails;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderAmendmentStatus;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderRepository;
import com.tcl.dias.oms.entity.repository.OrderAmendmentStatusRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.iwan.service.v1.IwanPricingFeasibilityService;
import com.tcl.dias.oms.iwan.service.v1.IwanQuoteService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.service.IllQuotePdfService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IllAmendmentService.java class. All the Quote related
 * Services for ILL Order Amendment will be implemented in this class
 *
 * @author Suruchi A
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */




@Service
@Transactional
public class IwanAmendmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IwanAmendmentService.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OdrOrderRepository odrOrderRepository;

    @Autowired
    protected OrderIllSitesRepository orderIllSiteRepository;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    protected DocusignAuditRepository docusignAuditRepository;

    @Autowired
    protected OrderToLeRepository orderToLeRepository;

    @Autowired
    OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;

    @Autowired
    ProductSolutionRepository productSolutionRepository;

    @Autowired
    protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

    @Autowired
    MstProductOfferingRepository mstProductOfferingRepository;

    @Autowired
    protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    @Autowired
    protected QuoteProductComponentRepository quoteProductComponentRepository;

    @Autowired
    OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

    @Autowired
    QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

    @Autowired
    OrderIllSiteSlaRepository orderIllSiteSlaRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    protected QuoteToLeRepository quoteToLeRepository;

    @Autowired
    OrderConfirmationAuditRepository orderConfirmationAuditRepository;

    @Autowired
    protected IllQuotePdfService illQuotePdfService;

    @Autowired
    protected OmsSfdcService omsSfdcService;

    @Autowired
    PricingDetailsRepository pricingDetailsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    protected QuoteDelegationRepository quoteDelegationRepository;

    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

    @Autowired
    NotificationService notificationService;

    @Value("${notification.mail.quotedashboard}")
    String quoteDashBoardRelativeUrl;

    @Value("${notification.mail.admin}")
    String adminRelativeUrl;

    @Value("${app.host}")
    String appHost;

    @Value("${customer.support.email}")
    String customerSupportEmail;

    @Value("${rabbitmq.customerlename.queue}")
    private String getCustomerLeNameById;

    @Autowired
    protected MQUtils mqUtils;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Autowired
    PartnerService partnerService;

    @Autowired
    OrderProductSolutionRepository orderProductSolutionRepository;

    @Autowired
    OrderIllSitesRepository orderIllSitesRepository;

    @Autowired
    SiteFeasibilityRepository siteFeasibilityRepository;

    @Autowired
    OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

    @Autowired
    protected IllSiteRepository illSiteRepository;

    @Autowired
    EngagementRepository engagementRepository;

    @Autowired
    OrderPriceRepository orderPriceRepository;

    @Autowired
    QuotePriceRepository quotePriceRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

    @Autowired
    QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

    @Autowired
    IwanQuoteService iwanQuoteService;

    @Autowired
    IwanPricingFeasibilityService illPricingFeasibilityService;

    @Autowired
    OrderAmendmentStatusRepository orderAmendmentStatusRepository;
    
    @Autowired
    ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

    @Value("${rabbitmq.getServiceDetailsO2c.queue}")
    private String serviceDetailsQueue;

    /**
     * @param orderCode
     * @return
     * @throws TclCommonException
     */

    @Transactional
    public List<ServiceDetailBeanForAmendment> getServiceDetailsForAmendment(String orderCode,boolean isO2c) throws TclCommonException {
        List<ServiceDetailBeanForAmendment> serviceDetailBeanForAmendmentList = new ArrayList<>();
        try {
            return getServiceAndSiteDetails(orderCode, serviceDetailBeanForAmendmentList,isO2c);
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
    }

    /**
     * @param orderCode
     * @param serviceDetailBeanForAmendments
     * @return
     */
    public List<ServiceDetailBeanForAmendment> getServiceAndSiteDetails(String orderCode, List<ServiceDetailBeanForAmendment> serviceDetailBeanForAmendments, boolean isO2c) throws TclCommonException {

        if (Objects.nonNull(orderCode)) {
            LOGGER.info("entered method getServiceAndSiteDetails for order code ::: {}  ", orderCode);
            ScServiceDetailForOrderAmend scServiceDetailForOrderAmendment = null;
            if (isO2c) {
                String queueResponse = (String) mqUtils.sendAndReceive(serviceDetailsQueue, orderCode);
                if(Objects.nonNull(queueResponse)){
                    LOGGER.info("Queue Response for order code --> {} is ---> {} ", orderCode, queueResponse);
                    scServiceDetailForOrderAmendment = (ScServiceDetailForOrderAmend) Utils.convertJsonToObject(queueResponse,
                            ScServiceDetailForOrderAmend.class);
                    LOGGER.info("SC Service Detail for Order code ----> {} after conversion to object is ----> {} ", orderCode , scServiceDetailForOrderAmendment);
                }
            }

            ScServiceDetailForOrderAmend amendment = Objects.nonNull(scServiceDetailForOrderAmendment) ? scServiceDetailForOrderAmendment : new ScServiceDetailForOrderAmend();
            if( Objects.nonNull(amendment.getSiteToServices()) && !amendment.getSiteToServices().isEmpty() && amendment.getSiteToServices().stream().filter(service -> service.getPrimarySecondary().equalsIgnoreCase("Secondary")).findFirst().isPresent()){
                amendment.setDualCase("yes");
            }
            LOGGER.info("Amendment Bean for Order code ----> {} after conversion to object is ----> {} ", orderCode , amendment);
            Order order = orderRepository.findByOrderCode(orderCode);
            if (Objects.nonNull(order)) {
                LOGGER.info("Order is not null and value is :::: {} ", order.getOrderCode());
                order.getOrderToLes()
                        .forEach(orderToLe -> orderToLe.getOrderToLeProductFamilies()
                                .forEach(orderToLeProductFamily -> orderToLeProductFamily.getOrderProductSolutions()
                                        .forEach(orderProductSolution -> orderProductSolution.getOrderIllSites()
                                                .forEach(orderIllSite -> {
                                                    LOGGER.info("Order ill site is ::: {}", orderIllSite.getSiteCode());
                                                    setServiceAmendmentBeanForEachSite(orderToLe, orderIllSite, amendment, isO2c,serviceDetailBeanForAmendments);
                                                }))));
            }
            LOGGER.info("ServiceDetailBeanForAmendments size is :::: {} ", serviceDetailBeanForAmendments.size());

        }
        return serviceDetailBeanForAmendments;
    }

    /**
     * @param orderToLe
     * @param orderIllSite
     * @return
     */
    private List<ServiceDetailBeanForAmendment> setServiceAmendmentBeanForEachSite(OrderToLe orderToLe, OrderIllSite orderIllSite
            ,ScServiceDetailForOrderAmend scServiceDetailForOrderAmendment,boolean isO2c,List<ServiceDetailBeanForAmendment> serviceDetailBeanForAmendments) {
        LOGGER.info("Entered setServiceAmendmentBeanForEachSite for site ---> {} ", orderIllSite.getSiteCode());

        ServiceDetailBeanForAmendment serviceDetailBeanForAmendment = new ServiceDetailBeanForAmendment();
        setCommonAttrForAmendmentServiceDetails(orderToLe, orderIllSite, serviceDetailBeanForAmendment);
        if(Objects.nonNull(scServiceDetailForOrderAmendment) && isO2c){

            if( Objects.nonNull(scServiceDetailForOrderAmendment.getSiteToServices()) && !scServiceDetailForOrderAmendment.getSiteToServices().isEmpty()) {

                LOGGER.info("Setting service Ids If O2C for site ---> {} ", orderIllSite.getSiteCode());
                scServiceDetailForOrderAmendment.getSiteToServices().forEach(servDet->{
                    if(orderIllSite.getSiteCode().equalsIgnoreCase(servDet.getSiteCode())){
                        if("Primary".equalsIgnoreCase(servDet.getPrimarySecondary()) || "Single".equalsIgnoreCase(servDet.getPrimarySecondary())){
                            setO2cAttrForAmendmentServiceDetails(serviceDetailBeanForAmendment, servDet);
                        }
                        if("yes".equalsIgnoreCase(scServiceDetailForOrderAmendment.getDualCase()) && "Secondary".equalsIgnoreCase(servDet.getPrimarySecondary())){
                            LOGGER.info("Entered Secondary");
                            ServiceDetailBeanForAmendment serviceDetailBeanForAmendmentSec = new ServiceDetailBeanForAmendment();
                            setCommonAttrForAmendmentServiceDetails(orderToLe, orderIllSite, serviceDetailBeanForAmendmentSec);
                            setO2cAttrForAmendmentServiceDetails(serviceDetailBeanForAmendmentSec, servDet);
                            serviceDetailBeanForAmendments.add(serviceDetailBeanForAmendmentSec);
                        }
                    }
                });
            }
        }

        serviceDetailBeanForAmendments.add(serviceDetailBeanForAmendment);
        LOGGER.info("Final service detail bean list is ---> {} ", serviceDetailBeanForAmendments);

        return serviceDetailBeanForAmendments;
    }

    public void setCommonAttrForAmendmentServiceDetails(OrderToLe orderToLe, OrderIllSite orderIllSite, ServiceDetailBeanForAmendment serviceDetailBeanForAmendment) {
        LOGGER.info("Entered set common attributes for service details method");
        serviceDetailBeanForAmendment.setSiteCode(orderIllSite.getSiteCode());
        serviceDetailBeanForAmendment.setType(orderToLe.getOrderType());
        serviceDetailBeanForAmendment.setProduct(getFamilyName(orderToLe));
        serviceDetailBeanForAmendment.setStatus("In progress");
        serviceDetailBeanForAmendment.setDate(orderToLe.getOrder().getCreatedTime());
    }

    private void setO2cAttrForAmendmentServiceDetails(ServiceDetailBeanForAmendment serviceDetailBeanForAmendment, SiteToService scdetails) {
        serviceDetailBeanForAmendment.setServiceId(scdetails.getServiceId());
        serviceDetailBeanForAmendment.setPrimarySecondary(scdetails.getPrimarySecondary());
        serviceDetailBeanForAmendment.setAllowAmendment(scdetails.getClrTask());
        LOGGER.info("Service id for site code ----> {} in O2C case is ---> {} ", serviceDetailBeanForAmendment.getSiteCode(),serviceDetailBeanForAmendment.getServiceId());
    }

    /**
     * @param orderToLe
     * @return
     */
    protected String getFamilyName(OrderToLe orderToLe) {
        return orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
    }

    protected String getFamilyNameForQuoteToLe(QuoteToLe quoteT) {
        return quoteT.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
    }

    /**
     * This method creates quote for amendment orders
     * @param quoteBeanForOrderAmendment
     * @param orderCode
     * @return
     * @throws TclCommonException
     */
    @Transactional
	public QuoteBeanForOrderAmendment createQuoteForOrderAmendment(
			QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, String orderCode) throws TclCommonException {

		QuoteBeanForOrderAmendment quoteBeanResponse = new QuoteBeanForOrderAmendment();
		try {
			validateRequest(quoteBeanForOrderAmendment);
			Order order = orderRepository.findByOrderCode(orderCode);
			List<String> orderSiteCodeFromReq = quoteBeanForOrderAmendment.getSiteCodes();

			String familyName = getFamilyName(order.getOrderToLes().stream().findAny().get());
			QuoteToLeProductFamily productFamily = new QuoteToLeProductFamily();
			if (!quoteBeanForOrderAmendment.isQuoteCreated()) {
				Quote newQuote = constructQuote(order, quoteBeanForOrderAmendment, quoteBeanResponse);
				quoteBeanResponse.setParentOrderCode(orderCode);
				quoteBeanResponse.setQuoteId(newQuote.getId());
				quoteBeanResponse.setIsQuoteCreated(true);
				quoteBeanResponse.setQuoteToLeId(newQuote.getQuoteToLes().stream().findAny().get().getId());
				// Triggering Sfdc Creation
				omsSfdcService.processCreateOptyForAmendment(newQuote.getQuoteToLes().stream().findAny().get(), familyName,orderCode, orderSiteCodeFromReq);
			}

			else {

				List<QuoteIllSite> quoteIllSites = new ArrayList<>();
				List<String> parentSiteCodes = new ArrayList<>();
				List<ProductSolution> existingProductSolutionList = new ArrayList<>();
				List<OrderProductSolution> newProductSolutionList = new ArrayList<>();
				List<OrderIllSite> orderIllSites = new ArrayList<>();
				List<String> orderIllSitesForNewSolution = new ArrayList<>();
				Set<OrderProductSolution> orderProductSolutions = new HashSet<>();
				if (Objects.nonNull(quoteBeanForOrderAmendment.getQuoteId())) {
					LOGGER.info("Quote Id Recieved from request is ---> {} ", quoteBeanForOrderAmendment.getQuoteId());
					Integer quote = quoteBeanForOrderAmendment.getQuoteId();
					LOGGER.info("Quote Id Recieved from request for repo call is ---> {} ", quote);
					Optional<Quote> quoteOptional = quoteRepository.findById(quote);
					productFamily = quoteOptional.get().getQuoteToLes().stream().findAny().get()
							.getQuoteToLeProductFamilies().stream().findAny().get();
					updateQuoteDetailsForAlreadyCreatedQuote(quoteBeanForOrderAmendment, orderSiteCodeFromReq,
							productFamily, parentSiteCodes, existingProductSolutionList, newProductSolutionList,
							orderIllSites, orderProductSolutions, quoteOptional);
					quoteBeanResponse.setParentOrderCode(orderCode);
					quoteBeanResponse.setQuoteId(quoteBeanForOrderAmendment.getQuoteId());
					quoteBeanResponse.setIsQuoteCreated(true);
					quoteBeanResponse.setQuoteToLeId(quoteToLeRepository.findByQuote_Id(quoteBeanResponse.getQuoteId())
							.stream().findAny().get().getId());

				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteBeanResponse;
	}

    /**
     * This method will be called if quote is already created , but the user updates the list of previously selected services/sites
     * by adding new selected services/sites or removing them
     *Logic followed ->
     * 1.First the list of sites already persisted in the db are retrieved using method getListOfParentSiteCodesPresentInTheCreatedQuote
     *
     * 2. First a list of sites is consolidated in two lists , 1 having the sites to be removed and the other has the sites
     * to be added. This is done in method : getSelectedSiteListToAddAndUnselectedSiteListToRemove
     *
     * 3. The list of sites to be removed are deleted from db and its corresponding components and attributes are also deleted
     * from the DB. This is done in method deleteRemovedSiteAndRelatedComponents
     *
     * 4. If the new sites selected by the user have a different product solution than the sites previously selected while quote creation,
     * a new solution has to be created corresponding to this site newly selected and is persisted in the DB for the same quote. The list of new sites corresponding
     * to this new solution is also mapped and persisted in DB for the quote. Also, if all the sites previously selected mapped to 1 solution is removed, this whole
     * solution needs to removed from the db. A consolidated list of solutions to be removed as well as added are retrieved in 2 lists. The solutions to be removed are deleted
     * from DB. This is done in method : getSelectedNewSolutionsAndRemoveUnselectedSolutions
     *
     * 5. The new solution and its sites is saved in DB in method : saveNewSolutionsAndItsSitesForExistingQuote
     *
     * 6. If sites newly selected are a part of a solution which already exists in the quote, then these sites are mapped to the solution and persisted
     * this is done in method : saveIllSitesForExistingSolutions
     * @param quoteBeanForOrderAmendment
     * @param orderSiteCodeFromReq
     * @param productFamily
     * @param parentSiteCodes
     * @param existingProductSolutionList
     * @param newProductSolutionList
     * @param orderIllSites
     * @param orderProductSolutions
     * @param quoteOptional
     */
    private void updateQuoteDetailsForAlreadyCreatedQuote(QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, List<String> orderSiteCodeFromReq, QuoteToLeProductFamily productFamily,
                                                          List<String> parentSiteCodes, List<ProductSolution> existingProductSolutionList,
                                                          List<OrderProductSolution> newProductSolutionList, List<OrderIllSite> orderIllSites,
                                                          Set<OrderProductSolution> orderProductSolutions, Optional<Quote> quoteOptional) throws TclCommonException {

        getListOfParentSiteCodesPresentInTheCreatedQuote(parentSiteCodes, quoteOptional,existingProductSolutionList);

        getSelectedSiteListToAddAndUnselectedSiteListToRemove(orderSiteCodeFromReq, parentSiteCodes);

        deleteRemovedSiteAndRelatedComponents(parentSiteCodes, existingProductSolutionList);

        getSelectedNewSolutionsAndRemoveUnselectedSolutions(orderSiteCodeFromReq, existingProductSolutionList, newProductSolutionList, orderIllSites,productFamily);

        deleteRemovedProductSolutions(existingProductSolutionList);

        saveNewSolutionsAndItsSitesForExistingQuote(quoteBeanForOrderAmendment, productFamily, newProductSolutionList,orderIllSites,orderProductSolutions);

        saveIllSitesForExistingSolutions(quoteBeanForOrderAmendment, productFamily, orderIllSites);
    }

    private void saveIllSitesForExistingSolutions(QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, QuoteToLeProductFamily productFamily, List<OrderIllSite> orderIllSites) {
        List<SolutionToSiteMapping> sitesMappingsToExistingSolutions = getSitesMappingsToExistingSolutions(orderIllSites,productFamily);

        sitesMappingsToExistingSolutions.stream().forEach(siteSolutionMapping->{

            productFamily.getProductSolutions()
                    .stream()
                    .filter(req-> req.getMstProductOffering()
                            .getProductName()
                            .equalsIgnoreCase(siteSolutionMapping
                                    .getSolution()
                                    .getMstProductOffering()
                                    .getProductName()))
                    .findFirst()
                    .ifPresent(solution-> {
                        solution.setQuoteIllSites(
                                constructQuoteIllSite(siteSolutionMapping.getSites(),siteSolutionMapping.getSolution(),quoteBeanForOrderAmendment));
                        productSolutionRepository.save(solution);
                    });
        });
    }

    /**
     * If a list of sites are added to te existing solution, this method maps the list of sites to the existing solution
     * @param orderIllSites
     * @param productFamily
     * @return
     */
    private List<SolutionToSiteMapping> getSitesMappingsToExistingSolutions(List<OrderIllSite> orderIllSites,QuoteToLeProductFamily productFamily) {
        List<SolutionToSiteMapping> solutionToSiteMappings = new ArrayList<>();
        Set<OrderProductSolution> orderProductSolutionsCheck = new HashSet<>();
        orderIllSites.stream().forEach(site->{
            if(!orderProductSolutionsCheck.contains(site.getOrderProductSolution())){
                orderProductSolutionsCheck.add(site.getOrderProductSolution());
            }
        });

        orderProductSolutionsCheck.stream().forEach(solution->{
            SolutionToSiteMapping solutionToSiteMapping = new SolutionToSiteMapping();
            List<OrderIllSite> orderIllSitesMapToSolution = new ArrayList<>();
            ProductSolution productSolutionForMapping = productSolutionRepository
                    .findByQuoteToLeProductFamilyAndMstProductOffering(productFamily, solution.getMstProductOffering());
            //solutionToSiteMapping.setSolution(productSolutionRepository.findBySolutionCode(solution.getSolutionCode()).stream().findAny().get());
            solutionToSiteMapping.setSolution(productSolutionForMapping);
            solution.getOrderIllSites().forEach(site->{
                orderIllSitesMapToSolution.add(site);
            });
            solutionToSiteMapping.setSites(orderIllSitesMapToSolution);
            solutionToSiteMappings.add(solutionToSiteMapping);
        });
        return solutionToSiteMappings;
    }

    /**
     * This method Saves the Newly selected solution , its corresponding sites and components and attributes in DB
     * orderillsites list ,has the list of order ill sites to be added for existing solutions in existing quote
     * @param quoteBeanForOrderAmendment
     * @param productFamily
     * @param newProductSolutionList
     * @param orderIllSitesCodesForNewSolution
     */
    private void saveNewSolutionsAndItsSitesForExistingQuote(QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, QuoteToLeProductFamily productFamily,
                                                             List<OrderProductSolution> newProductSolutionList, List<OrderIllSite> orderIllSites,Set<OrderProductSolution> orderProductSolutions ) throws TclCommonException {
        orderProductSolutions= newProductSolutionList.stream().collect(Collectors.toSet());
        if(!orderProductSolutions.isEmpty()) {
            Set<ProductSolution> productSolutions = constructQuoteProductSolution(orderProductSolutions, productFamily, quoteBeanForOrderAmendment);
            productFamily.setProductSolutions(productSolutions);
            quoteToLeProductFamilyRepository.save(productFamily);
        }

        getSitesSelectedForExistingSolution(newProductSolutionList, orderIllSites);


    }

    private void getSitesSelectedForExistingSolution(List<OrderProductSolution> newProductSolutionList, List<OrderIllSite> orderIllSites) {
        newProductSolutionList.stream().forEach(solution->{
            solution.getOrderIllSites().stream().forEach(site->{
                //If the sites are not a part of the new solution and are to be added to existing solution.
                // List orderIllSites contains all those sites
                if(orderIllSites.contains(site)){
                    orderIllSites.remove(site);
                }
            });
        });
    }

    /**
     * Logic -> A quote is created with n no of sites. The user goes back , and removes some selected sites and adds some new sites.
     * The previously selected sites which are now to be removed are already persisted in the DB and should be deleted.
     * This method gets a list of sites to be deleted in parentSiteCodes List and the sites to be added to the quote are present in orderSiteCodeFromReq
     * @param orderSiteCodeFromReq
     * @param parentSiteCodes
     */
    private void getSelectedSiteListToAddAndUnselectedSiteListToRemove(List<String> orderSiteCodeFromReq, List<String> parentSiteCodes) {

        CopyOnWriteArrayList tempList = new CopyOnWriteArrayList(orderSiteCodeFromReq);
        tempList.forEach(siteCode->{
            if(parentSiteCodes.contains(siteCode)){
                parentSiteCodes.remove(siteCode);
                orderSiteCodeFromReq.remove(siteCode);
            }
        });
        LOGGER.info("After Removal and additions parent site code size is --->" +
                " {}  and order site code from req is ---> {} ", parentSiteCodes,orderSiteCodeFromReq);
    }

    /**
     * This method has a list of existing prod solutions from DB. There is also a list of site codes,
     * orderSiteCodeFromReq, that are flowing from the UI to be added to the existing quote.
     * The solutions to be removed(if present) are there in existingProductSolutionList
     * The solutions to be added to the quote(if present) are there in newProductSolutionList
     * orderIllSites is a list of order ill site object , that are opted newly for amendment
     * and are supposed to be added to the quote
     * @param orderSiteCodeFromReq
     * @param existingProductSolutionList
     * @param newProductSolutionList
     * @param orderIllSites
     */
    private void getSelectedNewSolutionsAndRemoveUnselectedSolutions(List<String> orderSiteCodeFromReq, List<ProductSolution> existingProductSolutionList, List<OrderProductSolution>
            newProductSolutionList, List<OrderIllSite> orderIllSites, QuoteToLeProductFamily productFamily) {
        CopyOnWriteArrayList<ProductSolution> tempList = new CopyOnWriteArrayList(existingProductSolutionList);
        getUpdatedExistingSolutionList(existingProductSolutionList, productFamily, tempList);

        if(!orderSiteCodeFromReq.isEmpty()){
            orderSiteCodeFromReq.forEach(siteCode-> {

                if (orderIllSiteRepository.findBySiteCodeAndStatus(siteCode, BACTIVE).stream().findAny().isPresent()){
                    OrderIllSite orderIllSite = orderIllSiteRepository.findBySiteCodeAndStatus(siteCode, BACTIVE).stream().findAny().get();
                    if (!newProductSolutionList.contains(orderIllSite.getOrderProductSolution())) {
                        newProductSolutionList.add(orderIllSite.getOrderProductSolution());
                        tempList.forEach(exSolution -> {
                            if (exSolution.getMstProductOffering()
                                    .getProductName()
                                    .equalsIgnoreCase(orderIllSite
                                            .getOrderProductSolution()
                                            .getMstProductOffering()
                                            .getProductName())) {
                                existingProductSolutionList.remove(exSolution);
                                newProductSolutionList.remove(orderIllSite.getOrderProductSolution());
                            }

                        });
                    }

                    orderIllSites.add(orderIllSite);
                }
            });

        }
    }

    private void getUpdatedExistingSolutionList(List<ProductSolution> existingProductSolutionList, QuoteToLeProductFamily productFamily, CopyOnWriteArrayList<ProductSolution> tempList) {
        productFamily.getProductSolutions().forEach(productSolution -> {
            tempList.forEach(exSol->{
                if(productSolution.getMstProductOffering().getProductName().equalsIgnoreCase(exSol.getMstProductOffering().getProductName())){
                    existingProductSolutionList.remove(exSol);
                }
            });

        });
    }

    /**
     * parentSiteCodes is a list that contains the list of sites that are unselected by the user (which were previously selected)
     * This method deletes the site and its related components and sla details for the unselected sites. The product solutions mapped to this site are
     * added in existingProductSolutionList to be used further
     * @param parentSiteCodes
     * @param existingProductSolutionList
     */
    private void deleteRemovedSiteAndRelatedComponents(List<String> parentSiteCodes, List<ProductSolution> existingProductSolutionList) {

        if(!parentSiteCodes.isEmpty()){
            parentSiteCodes.forEach(siteCode->{
                Optional<OrderIllSite> orderSites = orderIllSiteRepository.findBySiteCodeAndStatus(siteCode, BACTIVE).stream().findFirst();
                if(orderSites.isPresent()){
                    List<QuoteIllSiteToService> illSiteToService = quoteIllSiteToServiceRepository.findByParentSiteId(orderSites.get().getId());
                    QuoteIllSite quoteIllSite = illSiteRepository.findById(illSiteToService.stream().findAny().get().getQuoteIllSite().getId()).get();
                    if(!existingProductSolutionList.contains(quoteIllSite.getProductSolution())) {
                        existingProductSolutionList.add(quoteIllSite.getProductSolution());
                    }
                    iwanQuoteService.removeComponentsAndAttr(quoteIllSite.getId());
                    iwanQuoteService.deletedIllsiteAndRelation(quoteIllSite);
                    illSiteToService.forEach(siteToService->{
                        quoteIllSiteToServiceRepository.delete(siteToService);
                    });
                }

            });

        }
    }

    /**
     * This method removes the list of solutions that are not to be amended(unselected by the user, if any)
     * in the existig quote. It also deletes the product from SFDC.
     *
     * @param existingProductSolutionList
     */
    private void deleteRemovedProductSolutions(List<ProductSolution> existingProductSolutionList) {
        if(!existingProductSolutionList.isEmpty()){
            existingProductSolutionList.forEach(solution->{
                QuoteToLe quoteToLe = solution.getQuoteToLeProductFamily().getQuoteToLe();
                if(Objects.nonNull(quoteToLe.getTpsSfdcOptyId())) {
                    omsSfdcService.processDeleteProduct(quoteToLe, solution);
                }
                productSolutionRepository.delete(solution);
            });
        }
    }

    /**
     * This method returns the list of sites present in Db for the respective quote, if quote is already created
     * @param parentSiteCodes
     * @param quoteOptional
     * @return
     */
    private List<String> getListOfParentSiteCodesPresentInTheCreatedQuote(List<String> parentSiteCodes, Optional<Quote> quoteOptional ,List<ProductSolution> existingProductSolutionList) {

        quoteOptional.get().getQuoteToLes().forEach(quoteToLe -> {
            quoteToLe.getQuoteToLeProductFamilies().forEach(quoteToLeProductFamily -> {
                quoteToLeProductFamily.getProductSolutions().forEach(productSolution -> {
                    productSolution.getQuoteIllSites().forEach(quoteIllSite -> {
                        List<QuoteIllSiteToService> siteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite(quoteIllSite);
                        if(siteToService.stream().findFirst().isPresent()) {
                            OrderIllSite orderIllSite = orderIllSiteRepository.findById(siteToService.stream().findFirst().get().getParentSiteId()).get();
                            parentSiteCodes.add(orderIllSite.getSiteCode());
                            LOGGER.info("Each Parent site code is --> {} ", orderIllSite.getSiteCode());
                        }
                        if(!existingProductSolutionList.contains(quoteIllSite.getProductSolution())){
                            existingProductSolutionList.add(quoteIllSite.getProductSolution());
                        }
                    });
                });
            });
        });
        return parentSiteCodes;
    }

    /**
     * validateUpdateRequest
     *
     * @param request
     */
    protected void validateRequest(QuoteBeanForOrderAmendment quoteBeanForOrderAmendment) throws TclCommonException {

        if (quoteBeanForOrderAmendment == null || quoteBeanForOrderAmendment.getSiteCodes()==null) {
            throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
        }
    }

    protected Quote constructQuote(Order order,QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, QuoteBeanForOrderAmendment quoteBeanResponse) throws TclCommonException {

        Quote newQuote = null;
        newQuote=new Quote();
        newQuote.setQuoteCode(Utils.generateRefId(getFamilyName(order.getOrderToLes().stream().findAny().get())));
        newQuote.setEngagementOptyId(order.getEngagementOptyId());
        newQuote.setCustomer(order.getCustomer());
        newQuote.setCreatedBy(order.getCreatedBy());
        newQuote.setCreatedTime(new Date());
        newQuote.setStatus(order.getStatus());
        newQuote.setTermInMonths(order.getTermInMonths());
        newQuote.setEffectiveDate(order.getEffectiveDate());
        newQuote.setStatus(BACTIVE);
        newQuote.setNsQuote("N");
        quoteRepository.save(newQuote);
        newQuote.setQuoteToLes(constructQuoteToLe(newQuote, order,quoteBeanForOrderAmendment));
        LOGGER.info("New Amendment Quote created is ----> {} ", newQuote.getQuoteCode());
        return newQuote;
    }


    private Set<QuoteToLe> constructQuoteToLe(Quote quote, Order order,QuoteBeanForOrderAmendment quoteBeanForOrderAmendment) throws TclCommonException {

        return getQuoteToLeBasenOnVersion(quote, order,quoteBeanForOrderAmendment);

    }

    private Set<QuoteToLe> getQuoteToLeBasenOnVersion(Quote quote, Order order,QuoteBeanForOrderAmendment quoteBeanForOrderAmendment) throws TclCommonException {
        Set<QuoteToLe> quoteToLes = new HashSet<>();
        List<OrderToLe> orderToLes = null;
        Set<QuoteToLeBean> quoteToLeBeans = new HashSet<>();
        orderToLes = orderToLeRepository.findByOrder(order);
        if (orderToLes != null) {
            for (OrderToLe orderToLe : orderToLes) {
                QuoteToLe quoteToLe = new QuoteToLe();
                quoteToLe.setQuote(quote);
                quoteToLe.setFinalMrc(orderToLe.getFinalMrc());
                quoteToLe.setFinalNrc(orderToLe.getFinalNrc());
                quoteToLe.setFinalArc(orderToLe.getFinalArc());
                quoteToLe.setProposedMrc(orderToLe.getProposedMrc());
                quoteToLe.setProposedNrc(orderToLe.getProposedNrc());
                quoteToLe.setProposedArc(orderToLe.getProposedArc());
                quoteToLe.setTotalTcv(orderToLe.getTotalTcv());
                quoteToLe.setCurrencyId(orderToLe.getCurrencyId());
                quoteToLe.setErfCusCustomerLegalEntityId(orderToLe.getErfCusCustomerLegalEntityId());
                quoteToLe.setErfCusSpLegalEntityId(orderToLe.getErfCusSpLegalEntityId());
                quoteToLe.setTpsSfdcParentOptyId(orderToLe.getTpsSfdcParentOptyId());
                quoteToLe.setStage(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode());
                quoteToLe.setTermInMonths(orderToLe.getTermInMonths());
                quoteToLe.setCurrencyCode(orderToLe.getCurrencyCode());
                quoteToLe.setClassification(orderToLe.getClassification());
                quoteToLe.setQuoteType(orderToLe.getOrderType());
                quoteToLe.setQuoteCategory(orderToLe.getOrderCategory());
                quoteToLe.setIsAmended(BACTIVE);
                quoteToLe.setIsMultiCircuit(orderToLe.getIsMultiCircuit());
                quoteToLe.setAmendmentParentOrderCode(order.getOrderCode());
                quoteToLe.setErfServiceInventoryParentOrderId(orderToLe.getErfServiceInventoryParentOrderId());
                quoteToLe.setIsMultiCircuit(orderToLe.getIsMultiCircuit());
                quoteToLe.setAmendmentParentOrderCode(order.getOrderCode());
                quoteToLeRepository.save(quoteToLe);
                quoteToLe.setQuoteLeAttributeValues(constructQuoteToLeAttribute(orderToLe, quoteToLe,quoteBeanForOrderAmendment));
                //detail.getOrderLeIds().add(quoteToLe.getId());
                quoteToLe
                        .setQuoteToLeProductFamilies(getQuoteProductFamilyBasenOnVersion(quoteToLe, orderToLe,quoteBeanForOrderAmendment));
                quoteToLes.add(quoteToLe);
            }

        }

        return quoteToLes;

    }

    private Set<QuoteLeAttributeValue> constructQuoteToLeAttribute(OrderToLe orderToLe, QuoteToLe quoteToLe, QuoteBeanForOrderAmendment quoteBeanForOrderAmendment) {
        Set<QuoteLeAttributeValue> attributeValues = new HashSet<>();
        Set<LegalAttributeBean> legalAttributeBeans = new HashSet<>();
        List<OrdersLeAttributeValue> orderLeAttributeValue = ordersLeAttributeValueRepository.findByOrderToLe(orderToLe);
        if (orderLeAttributeValue != null) {
            orderLeAttributeValue.stream().forEach(attrVal -> {
                LegalAttributeBean legalAttributeBean = new LegalAttributeBean();
                QuoteLeAttributeValue quoteLeAttributeValues = new QuoteLeAttributeValue();
                quoteLeAttributeValues.setAttributeValue(attrVal.getAttributeValue());
                quoteLeAttributeValues.setDisplayValue(attrVal.getDisplayValue());
                quoteLeAttributeValues.setMstOmsAttribute(attrVal.getMstOmsAttribute());
                quoteLeAttributeValues.setQuoteToLe(quoteToLe);
                quoteLeAttributeValueRepository.save(quoteLeAttributeValues);


                legalAttributeBeans.add(contructLegalAttributeBeanForOrderAmendment(legalAttributeBean, quoteLeAttributeValues));
                attributeValues.add(quoteLeAttributeValues);

            });

        }

        return attributeValues;
    }

    private LegalAttributeBean contructLegalAttributeBeanForOrderAmendment(LegalAttributeBean legalAttributeBean, QuoteLeAttributeValue quoteLeAttributeValues) {
        legalAttributeBean.setAttributeValue(quoteLeAttributeValues.getAttributeValue());
        legalAttributeBean.setDisplayValue(quoteLeAttributeValues.getAttributeValue());
        legalAttributeBean.setId(quoteLeAttributeValues.getId());
        legalAttributeBean.setMstOmsAttribute(constructMstOmsAttributeBeanForOrderAmendment(quoteLeAttributeValues));
        return legalAttributeBean;
    }

    private MstOmsAttributeBean constructMstOmsAttributeBeanForOrderAmendment(QuoteLeAttributeValue quoteLeAttributeValues) {
        MstOmsAttribute mstOmsAttribute = quoteLeAttributeValues.getMstOmsAttribute();
        MstOmsAttributeBean mstOmsAttributeBean= new MstOmsAttributeBean();
        mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
        mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
        mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
        mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
        mstOmsAttributeBean.setId(mstOmsAttribute.getId());
        mstOmsAttributeBean.setName(mstOmsAttribute.getName());
        mstOmsAttributeBean.setIsActive(mstOmsAttribute.getIsActive());
        return mstOmsAttributeBean;
    }

    private Set<QuoteToLeProductFamily> getQuoteProductFamilyBasenOnVersion(QuoteToLe quoteToLe, OrderToLe orderToLe,
                                                                            QuoteBeanForOrderAmendment quoteBeanForOrderAmendment) throws TclCommonException {
        List<OrderToLeProductFamily> orderToLeProductFamilies = null;
        Set<QuoteToLeProductFamily> quoteToLeProductFamilies = null;
        orderToLeProductFamilies = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
        if (orderToLeProductFamilies != null) {
            quoteToLeProductFamilies = new HashSet<>();
            for (OrderToLeProductFamily orderToLeProductFamily : orderToLeProductFamilies) {
                QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
                quoteToLeProductFamily.setMstProductFamily(orderToLeProductFamily.getMstProductFamily());
                quoteToLeProductFamily.setQuoteToLe(quoteToLe);
                quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
                quoteToLeProductFamily.setProductSolutions(constructQuoteProductSolution(
                        orderToLeProductFamily.getOrderProductSolutions(), quoteToLeProductFamily,quoteBeanForOrderAmendment));

                quoteToLeProductFamilies.add(quoteToLeProductFamily);



//				if (!PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
                if (!partnerService.quoteCreatedByPartner(quoteToLe.getQuote().getId())) {
                    processEngagement(quoteToLe, quoteToLeProductFamily);
                }
//				}
            }
        }

        return quoteToLeProductFamilies;

    }

    private Set<ProductSolution> constructQuoteProductSolution(Set<OrderProductSolution> orderProductSolutions,
                                                               QuoteToLeProductFamily quoteToLeProductFamily,QuoteBeanForOrderAmendment quoteBeanForOrderAmendment) throws TclCommonException {

        Set<ProductSolution> quoteProductSolutions = new HashSet<>();
        if (orderProductSolutions != null) {
            for (OrderProductSolution orderProductSolution : orderProductSolutions) {
                List<OrderIllSite> orderIllSites = getIllsitesBasenOnVersion(orderProductSolution, null);
                if (orderIllSites != null && !orderIllSites.isEmpty()) {
                    ProductSolution quoteProductSolution = new ProductSolution();
                    if (orderProductSolution.getMstProductOffering() != null) {
                        quoteProductSolution.setMstProductOffering(orderProductSolution.getMstProductOffering());
                    }
                    productSolutionRepository.findBySolutionCode(orderProductSolution.getSolutionCode()).stream().findFirst().ifPresent(solution->{
                        String productProfileData = solution.getProductProfileData();
                        quoteProductSolution.setProductProfileData(productProfileData);
                    });
                    quoteProductSolution.setSolutionCode(Utils.generateUid());
                    quoteProductSolution.setTpsSfdcProductName(orderProductSolution.getTpsSfdcProductName());
                    quoteProductSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
                    productSolutionRepository.save(quoteProductSolution);

                    QuoteToLe quoteToLe = quoteToLeProductFamily.getQuoteToLe();
                    if(Objects.nonNull(quoteToLe) && Objects.nonNull(quoteToLe.getTpsSfdcOptyId()) && StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())){
                        omsSfdcService.processProductServiceForSolution(quoteToLe, quoteProductSolution,
                                quoteToLe.getTpsSfdcOptyId());
                    }

                    quoteProductSolution.setQuoteIllSites(constructQuoteIllSite(orderIllSites, quoteProductSolution,quoteBeanForOrderAmendment));
                    quoteProductSolutions.add(quoteProductSolution);
                }
            }
        }

        return quoteProductSolutions;
    }


    private Set<QuoteIllSite> constructQuoteIllSite(List<OrderIllSite> orderIllSites, ProductSolution productSolution,
                                                    QuoteBeanForOrderAmendment quoteBeanForOrderAmendment) {
        Set<QuoteIllSite> illSiteSet = new HashSet<>();


//        quoteBeanForOrderAmendment.getServiceIds().stream().filter(sitecode -> sitecode.equalsIgnoreCase(orderIllSite.getSiteCode()))

        for (OrderIllSite orderIllSite : orderIllSites) {
            for(String stringList : quoteBeanForOrderAmendment.getSiteCodes()) {
                if(orderIllSite.getSiteCode().equalsIgnoreCase(stringList)) {

                    if (orderIllSite.getStatus() == 1 && orderIllSite.getFeasibility() == 1) {
                        QuoteIllSite quoteIllSite = new QuoteIllSite();
                        quoteIllSite.setIsTaxExempted(orderIllSite.getIsTaxExempted());
                        quoteIllSite.setStatus((byte) 1);
                        quoteIllSite.setErfLocSiteaLocationId(orderIllSite.getErfLocSiteaLocationId());
                        quoteIllSite.setErfLocSitebLocationId(orderIllSite.getErfLocSitebLocationId());
                        quoteIllSite.setErfLocSiteaSiteCode(orderIllSite.getErfLocSiteaSiteCode());
                        quoteIllSite.setErfLocSitebSiteCode(orderIllSite.getErfLocSitebSiteCode());
                        quoteIllSite.setErfLrSolutionId(orderIllSite.getErfLrSolutionId());
                        quoteIllSite.setImageUrl(orderIllSite.getImageUrl());
                        quoteIllSite.setCreatedBy(orderIllSite.getCreatedBy());
                        quoteIllSite.setCreatedTime(new Date());
                        quoteIllSite.setFeasibility(orderIllSite.getFeasibility());
                        quoteIllSite.setProductSolution(productSolution);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date()); // Now use today date.
                        cal.add(Calendar.DATE, 60); // Adding 60 days
                        quoteIllSite.setEffectiveDate(cal.getTime());
                        quoteIllSite.setMrc(orderIllSite.getMrc());
                        quoteIllSite.setArc(orderIllSite.getArc());
                        quoteIllSite.setTcv(orderIllSite.getTcv());
                        quoteIllSite.setSiteCode(Utils.generateUid());
                        quoteIllSite.setNrc(orderIllSite.getNrc());
                        illSiteRepository.save(quoteIllSite);
                        constructQuoteSiteFeasibility(orderIllSite, quoteIllSite);
                        quoteIllSite.setQuoteIllSiteSlas(constructQuoteSiteSla(orderIllSite, quoteIllSite));
                        String quoteType = quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType();
                        LOGGER.info("quoteToLe quote type {}", quoteType);
                        if (quoteType != null ) {

                            constructQuoteIllSiteToService(orderIllSite, quoteIllSite,quoteBeanForOrderAmendment,quoteType);
                        }
                        LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ", quoteIllSite.getId());
                        constructQuoteProductComponent(orderIllSite.getId(), quoteIllSite, quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId());
                        illSiteSet.add(quoteIllSite);
                    }
                }
            }
        }

        LOGGER.info("Ill site set size is ----> {} ", illSiteSet.size());
        return illSiteSet;
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

    private List<OrderIllSite> getIllsitesBasenOnVersion(OrderProductSolution productSolution, Integer siteId) {

        List<OrderIllSite> orderIllSites = new ArrayList<>();

        if (siteId != null) {

            Optional<OrderIllSite> orderIllSite = orderIllSiteRepository.findById(siteId);

            if (orderIllSite.isPresent()) {

                orderIllSites.add(orderIllSite.get());

            }

        } else {

            orderIllSites = orderIllSiteRepository.findByOrderProductSolutionAndStatus(productSolution, (byte) 1);

        }

        return orderIllSites;

    }

    private Set<SiteFeasibility> constructQuoteSiteFeasibility( OrderIllSite orderIllSite,QuoteIllSite  quoteIllSite) {
        Set<SiteFeasibility> siteFeasibilities = new HashSet<>();

        List<OrderSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository.findByOrderIllSite(orderIllSite);
        if (orderSiteFeasibilities != null) {
            orderSiteFeasibilities.forEach(sitefeas -> {
                SiteFeasibility siteFeasibility = new SiteFeasibility();
                siteFeasibility.setFeasibilityCode(Utils.generateUid());
                siteFeasibility.setFeasibilityCheck(sitefeas.getFeasibilityCheck());
                siteFeasibility.setFeasibilityMode(sitefeas.getFeasibilityMode());
                siteFeasibility.setIsSelected(sitefeas.getIsSelected());
                LocalDateTime localDateTime = LocalDateTime.now();
                siteFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
                siteFeasibility.setQuoteIllSite(quoteIllSite);
                siteFeasibility.setType(sitefeas.getType());
                siteFeasibility.setProvider(sitefeas.getProvider());
                siteFeasibility.setRank(sitefeas.getRank());
                siteFeasibility.setResponseJson(sitefeas.getResponseJson());
                //siteFeasibility.setSfdcFeasibilityId(sitefeas.getSfdcFeasibilityId());
                siteFeasibility.setFeasibilityType(sitefeas.getFeasibilityType());
                siteFeasibilityRepository.save(siteFeasibility);
                siteFeasibilities.add(siteFeasibility);

            });
        }

        return siteFeasibilities;
    }

    private Set<QuoteIllSiteSla> constructQuoteSiteSla(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite) {
        Set<QuoteIllSiteSla> quoteIllSitesSlas = new HashSet<>();

        if (orderIllSite.getOrderIllSiteSlas() != null) {
            orderIllSite.getOrderIllSiteSlas().forEach(orderIllSiteSla -> {
                QuoteIllSiteSla quoteIllSiteSla = new QuoteIllSiteSla();
                quoteIllSiteSla.setQuoteIllSite(quoteIllSite);
                quoteIllSiteSla.setSlaEndDate(orderIllSiteSla.getSlaEndDate());
                quoteIllSiteSla.setSlaStartDate(orderIllSiteSla.getSlaStartDate());
                quoteIllSiteSla.setSlaValue(orderIllSiteSla.getSlaValue());
                quoteIllSiteSla.setSlaMaster(orderIllSiteSla.getSlaMaster());
                quoteIllSiteSlaRepository.save(quoteIllSiteSla);
                quoteIllSitesSlas.add(quoteIllSiteSla);

            });
        }

        return quoteIllSitesSlas;
    }

    private void constructQuoteIllSiteToService( OrderIllSite orderIllSite, QuoteIllSite quoteIllSite,QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, String quoteType) {
        try {
            List<QuoteIllSiteToService> illSiteToServices = new ArrayList<>();
            String parentSiteCode = orderIllSite.getSiteCode();
            String scenario = getScenarioForConstructQuoteIllSite(quoteBeanForOrderAmendment,quoteType);

            switch(scenario) {
                case "MACD_O2C":
                    LOGGER.info("Entered case macd o2c");
                    constructIllSiteToServiceMacdO2c(orderIllSite, quoteIllSite, quoteBeanForOrderAmendment, quoteType, illSiteToServices, parentSiteCode);
                    break;

                case "MACD":
                    LOGGER.info("Entered case macd");
                    constructIllSiteToServiceMacd(orderIllSite, quoteIllSite, quoteType, illSiteToServices);
                    break;

                case "NEW":
                    LOGGER.info("Entered case new");
                    constructIllQuoteToServiceNew(orderIllSite, quoteIllSite, illSiteToServices);
                    break;

                case "NEW_O2C":
                    LOGGER.info("Entered case new o2c");
                    constructIllSiteToServiceNewO2c(orderIllSite, quoteIllSite, quoteBeanForOrderAmendment, illSiteToServices, parentSiteCode);
                    break;
            }

            quoteIllSiteToServiceRepository.saveAll(illSiteToServices);
            LOGGER.info("Inside IwanQuoteService.constructQuoteIllSiteToService Saved quote ill site to services for scenario ----> {}  and ill site ---> {} and" +
                    "se quote is ----> {} ", scenario, quoteIllSite.getId(), quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode());

        } catch (Exception e) {
            LOGGER.error("Exception occured while saving orderIllSiteToServices {} ", e);
        }
    }

    private void constructIllSiteToServiceNewO2c(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite, QuoteBeanForOrderAmendment quoteBeanForOrderAmendment,
                                                 List<QuoteIllSiteToService> illSiteToServices, String parentSiteCode) {
        LOGGER.info("Entered method constructIllSiteToServiceNewO2c with quote ill site ------> {}  and parent site ----> {} ", quoteIllSite.getId() , parentSiteCode);
        if (Objects.nonNull(quoteBeanForOrderAmendment.getServiceDetailBeanForAmendments()) && !quoteBeanForOrderAmendment.getServiceDetailBeanForAmendments().isEmpty()) {
            List<ServiceDetailBeanForAmendment> serviceDetailBeanForAmendments = getServiceDetailMappedToParentSiteCode(quoteBeanForOrderAmendment, parentSiteCode);
            serviceDetailBeanForAmendments.forEach(servDetail->{
                QuoteIllSiteToService quoteIllSiteToService = new QuoteIllSiteToService();
                setIllSiteToServiceCommon(orderIllSite, quoteIllSite, quoteIllSiteToService);
                quoteIllSiteToService.setO2cServiceId(servDetail.getServiceId());
                quoteIllSiteToService.setServiceType(servDetail.getType());
                quoteIllSiteToService.setAllowAmendment(servDetail.getAllowAmendment());
                illSiteToServices.add(quoteIllSiteToService);
            });
        }
    }

    private void constructIllQuoteToServiceNew(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite, List<QuoteIllSiteToService> illSiteToServices) {
        LOGGER.info("entered mothod constructIllQuoteToServiceNew for order site -----> {} ", orderIllSite.getSiteCode());
        QuoteIllSiteToService quoteIllSiteToService = new QuoteIllSiteToService();
        setIllSiteToServiceCommon(orderIllSite, quoteIllSite, quoteIllSiteToService);
        quoteIllSiteToService.setAllowAmendment("Yes");
        illSiteToServices.add(quoteIllSiteToService);
    }

    private void constructIllSiteToServiceMacdO2c(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite, QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, String quoteType, List<QuoteIllSiteToService> illSiteToServices, String parentSiteCode) {
        LOGGER.info("Entered method constructIllSiteToServiceMacdO2c with quote ill site ------> {}  and parent site ----> {} ", quoteIllSite.getId() , parentSiteCode);
        List<OrderIllSiteToService> orderIllSiteServices = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
        if(!orderIllSiteServices.isEmpty()) {
            orderIllSiteServices.stream().forEach(siteService->{
                LOGGER.info("Site service id for Amendment quote -----> {}  is ----> {}  ", quoteBeanForOrderAmendment.getQuoteId(),
                        siteService.getErfServiceInventoryTpsServiceId());
                QuoteIllSiteToService illSiteToService = new QuoteIllSiteToService();
                constructIllSiteToServiceForMacd(quoteType, siteService,illSiteToService);
                setIllSiteToServiceCommon(orderIllSite, quoteIllSite, illSiteToService);
                setAttributesIfO2cTriggeredAndMacd(quoteBeanForOrderAmendment, siteService, parentSiteCode, illSiteToService);
                illSiteToServices.add(illSiteToService);
            });
        }
    }

    private void constructIllSiteToServiceMacd(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite, String quoteType, List<QuoteIllSiteToService> illSiteToServices) {
        LOGGER.info("Entered method constructIllSiteToServiceMACD with quote ill site ------> {}  and quote Type ----> {} ", quoteIllSite.getId() , quoteType);
        List<OrderIllSiteToService> orderIllSiteServices = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
        if(!orderIllSiteServices.isEmpty()) {
            LOGGER.info("Order ill site to service is not empty for site code ----> {} and its quote is-----> {} ", orderIllSite.getSiteCode(),quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode());
            orderIllSiteServices.stream().forEach(siteService->{
                LOGGER.info("Site service id for site code -----> {}  is -------> {} ", orderIllSite.getSiteCode(),siteService.getId());
                QuoteIllSiteToService illSiteToService = new QuoteIllSiteToService();
                constructIllSiteToServiceForMacd(quoteType, siteService,illSiteToService);
                setIllSiteToServiceCommon(orderIllSite, quoteIllSite, illSiteToService);
                illSiteToService.setAllowAmendment("Yes");
                //setAttributesIfO2cTriggeredAndMacd(quoteBeanForOrderAmendment, siteService, parentSiteCode, illSiteToService);
                illSiteToServices.add(illSiteToService);
            });
            quoteIllSiteToServiceRepository.saveAll(illSiteToServices);
            LOGGER.info("Inside IwanQuoteService.constructQuoteIllSiteToService Saved orderillSiteToService ");
        }
    }

    private void constructIllSiteToServiceForMacd(String quoteType, OrderIllSiteToService siteService, QuoteIllSiteToService illSiteToService) {
//        LOGGER.info("Setting quote Ill site to service for siteId  {} and QuoteLe  {}", illSiteToService.getQuoteIllSite().getId(), illSiteToService.getQuoteToLe().getId());
        if(MACD_QUOTE_TYPE.equalsIgnoreCase(quoteType)){
            LOGGER.info("Entered inside MACD for Construct Quote Ill Site To Service");
            illSiteToService.setErfServiceInventoryParentOrderId(siteService.getErfServiceInventoryParentOrderId());
            illSiteToService.setErfServiceInventoryServiceDetailId(siteService.getErfServiceInventoryServiceDetailId());
            illSiteToService.setErfServiceInventoryTpsServiceId(siteService.getErfServiceInventoryTpsServiceId());
            illSiteToService.setTpsSfdcParentOptyId(siteService.getTpsSfdcParentOptyId());
            illSiteToService.setType(siteService.getType());
        }
    }

    private void setIllSiteToServiceCommon(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite, QuoteIllSiteToService illSiteToService) {
        illSiteToService.setQuoteIllSite(quoteIllSite);
        illSiteToService.setQuoteToLe(quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe());
        illSiteToService.setParentSiteId(orderIllSite.getId());
        illSiteToService.setParentOrderId(orderIllSite
                .getOrderProductSolution()
                .getOrderToLeProductFamily()
                .getOrderToLe()
                .getOrder()
                .getId());
    }

    private String getScenarioForConstructQuoteIllSite(QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, String quoteType) {
        String scenario = "";
        LOGGER.info("Is O2C flag from Payload is -----> {} and Quote Bean for Order Amendment is ----> {} ", quoteBeanForOrderAmendment.isO2c() , quoteBeanForOrderAmendment);
        if(MACD_QUOTE_TYPE.equalsIgnoreCase(quoteType) && quoteBeanForOrderAmendment.isO2c()) {
            scenario = "MACD_O2C";
        }
        else if(MACD_QUOTE_TYPE.equalsIgnoreCase(quoteType) && !quoteBeanForOrderAmendment.isO2c()){
            scenario="MACD";
        }
        else if(NEW.equalsIgnoreCase(quoteType) && quoteBeanForOrderAmendment.isO2c()){
            scenario="NEW_O2C";
        }
        else if(NEW.equalsIgnoreCase(quoteType) && !quoteBeanForOrderAmendment.isO2c()){
            scenario = "NEW";
        }
        LOGGER.info("Scenario is ----> {} ", scenario);
        return scenario;
    }


    private void setAttributesIfO2cTriggeredAndMacd(QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, OrderIllSiteToService siteService, String parentSiteCode, QuoteIllSiteToService illSiteToService) {
        if(quoteBeanForOrderAmendment.isO2c()) {
            LOGGER.info("Entered inside setAttributesIfO2cTriggered method");
            if (Objects.nonNull(quoteBeanForOrderAmendment.getServiceDetailBeanForAmendments()) && !quoteBeanForOrderAmendment.getServiceDetailBeanForAmendments().isEmpty()) {
                List<ServiceDetailBeanForAmendment> serviceDetailBeanForAmendments = getServiceDetailMappedToParentSiteCode(quoteBeanForOrderAmendment, parentSiteCode);
                LOGGER.info("Service Detail Bean for Amendment size is ----> {} and service detaiils for amendment is -----> {} ", serviceDetailBeanForAmendments.size() , serviceDetailBeanForAmendments);
                serviceDetailBeanForAmendments.stream()
                        .filter(servDetails ->
                                servDetails.getPrimarySecondary()
                                        .equalsIgnoreCase(siteService.getType()))
                        .findFirst()
                        .ifPresent(service -> {
                            LOGGER.info("Service Detail Beans type ----> {} and Parent site codes type-----> {} ",service.getPrimarySecondary() ,siteService.getType() );
                            illSiteToService.setO2cServiceId(service.getServiceId());
                            illSiteToService.setServiceType(service.getPrimarySecondary());
                            illSiteToService.setAllowAmendment(service.getAllowAmendment());
                        });

            }
        }
    }

    private List<ServiceDetailBeanForAmendment> getServiceDetailMappedToParentSiteCode(QuoteBeanForOrderAmendment quoteBeanForOrderAmendment, String parentSiteCode) {
        return quoteBeanForOrderAmendment.getServiceDetailBeanForAmendments()
                .stream()
                .filter(detail ->
                        detail.getSiteCode()
                                .equalsIgnoreCase(parentSiteCode))
                .collect(Collectors.toList());
    }



    private List<QuoteProductComponent> constructQuoteProductComponent(Integer id, QuoteIllSite illSite,Integer quoteId) {
        OrderIllSite orderIllSite = orderIllSiteRepository.findById(id).get();
        LOGGER.info("Inside construct quote product component method for site -----> {} ", illSite.getId());
        List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
        String productFamilyName = illSite.getProductSolution().getQuoteToLeProductFamily().getMstProductFamily().getName();
        List<OrderProductComponent> orderProductComponents = orderProductComponentRepository.findByReferenceIdInAndReferenceName(id,QuoteConstants.IWANSITES.toString());
        if (orderProductComponents != null) {
            for (OrderProductComponent orderProductComponent : orderProductComponents) {
                if(orderProductComponent.getMstProductComponent().getName().equalsIgnoreCase("SITE_PROPERTIES") && "MFMP".equalsIgnoreCase(orderIllSite.getFpStatus())){
                    continue;
                }
                QuoteProductComponent productComponent = new QuoteProductComponent();
                productComponent.setReferenceId(illSite.getId());
                if (orderProductComponent.getMstProductComponent() != null) {
                    productComponent.setMstProductComponent(orderProductComponent.getMstProductComponent());
                }
                productComponent.setType(orderProductComponent.getType());
                productComponent.setMstProductFamily(orderProductComponent.getMstProductFamily());
                productComponent.setReferenceName(orderProductComponent.getReferenceName());


                quoteProductComponentRepository.save(productComponent);

                LOGGER.info("Calling constructQuoteComponentPrice method for prod component id ---> {} ", productComponent.getId());
                constructQuoteComponentPrice(orderProductComponent, productComponent);
                List<OrderProductComponentsAttributeValue> attributes = orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponent_Id(orderProductComponent.getId());
                productComponent.setQuoteProductComponentsAttributeValues(
                        constructQuoteAttributes(attributes, productComponent,quoteId));
                quoteProductComponents.add(productComponent);
                LOGGER.info("Product Component Added is ----> {}  and its primary key is -----> {} ", productComponent.getMstProductComponent().getName(),productComponent.getId());


            }


               /* if()) {
                    List<QuoteProductComponent> productComponents = quoteProductComponents.stream().filter(quoteProductComponent ->
                            "SITE_PROPERTIES".equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())).collect(Collectors.toList());
                            if(Objects.nonNull(productComponents) && !productComponents.isEmpty())
                    productComponents.stream().forEach(prodComp->{
                        quoteProductComponents.remove(prodComp);
                        LOGGER.info("Product Component Removed is ----> {} and its primary key is ----> {} ", prodComp.getMstProductComponent().getName(), prodComp.getId());
                    });
                }*/

        }
        return quoteProductComponents;

    }

    private Set<QuoteProductComponentsAttributeValue> constructQuoteAttributes(
            List<OrderProductComponentsAttributeValue> ordrProductComponentsAttributeValues,
            QuoteProductComponent quoteProductComponent,Integer quoteId) {
        Set<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = new HashSet<>();
        if (ordrProductComponentsAttributeValues != null) {
            for (OrderProductComponentsAttributeValue orderAttributeValue : ordrProductComponentsAttributeValues) {
                QuoteProductComponentsAttributeValue quoteAttributeValue = new QuoteProductComponentsAttributeValue();
                quoteAttributeValue.setAttributeValues(orderAttributeValue.getAttributeValues());
                quoteAttributeValue.setDisplayValue(orderAttributeValue.getDisplayValue());
                quoteAttributeValue.setProductAttributeMaster(orderAttributeValue.getProductAttributeMaster());
                quoteAttributeValue.setQuoteProductComponent(quoteProductComponent);
                quoteProductComponentsAttributeValueRepository.save(quoteAttributeValue);
                LOGGER.info("Calling the construct quote atributes method for attribute id---> ", quoteAttributeValue.getId());
                constructQuoteAttributePriceDto(orderAttributeValue, quoteAttributeValue,quoteId);
                quoteProductComponentsAttributeValues.add(quoteAttributeValue);
            }
        }

        return quoteProductComponentsAttributeValues;
    }

    private QuotePrice constructQuoteAttributePriceDto( OrderProductComponentsAttributeValue orderAttributeValue,
                                                        QuoteProductComponentsAttributeValue quoteAttributeValue, Integer quoteId) {

        LOGGER.info("Inside the constructQuoteAttributePriceDto method for quoteAttributeValue id ---> {} " , quoteAttributeValue.getId());
        QuotePrice quotePrice = null;
        if (orderAttributeValue != null && orderAttributeValue.getProductAttributeMaster() != null) {
            OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
                    String.valueOf(orderAttributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
            quotePrice = new QuotePrice();
            if (orderPrice != null) {
                quotePrice = new QuotePrice();
                quotePrice.setCatalogMrc(orderPrice.getCatalogMrc());
                quotePrice.setCatalogNrc(orderPrice.getCatalogNrc());
                quotePrice.setReferenceName(orderPrice.getReferenceName());
                quotePrice.setReferenceId(String.valueOf(quoteAttributeValue.getId()));
                quotePrice.setComputedMrc(orderPrice.getComputedMrc());
                quotePrice.setComputedNrc(orderPrice.getComputedNrc());
                quotePrice.setDiscountInPercent(orderPrice.getDiscountInPercent());
                quotePrice.setQuoteId(quoteId);
                //quotePrice.setVersion(1);
                quotePrice.setEffectiveUsagePrice(orderPrice.getEffectiveUsagePrice());
                quotePrice.setMinimumMrc(orderPrice.getMinimumMrc());
                quotePrice.setMinimumNrc(orderPrice.getMinimumNrc());
                quotePrice.setEffectiveMrc(orderPrice.getEffectiveMrc());
                quotePrice.setEffectiveNrc(orderPrice.getEffectiveNrc());
                quotePrice.setEffectiveArc(orderPrice.getEffectiveArc());
                quotePriceRepository.save(quotePrice);
            }

        }
        return quotePrice;

    }

    private QuotePrice constructQuoteComponentPrice(OrderProductComponent orderProductComponent,
                                                    QuoteProductComponent productComponent) {
        Optional<QuoteIllSite> siteId = illSiteRepository.findById(productComponent.getReferenceId());
        Integer quoteId = siteId.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId();
        QuotePrice quotePrice = null;
        if (orderProductComponent != null && orderProductComponent.getMstProductComponent() != null) {
            OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
                    String.valueOf(orderProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
            if (orderPrice != null) {
                quotePrice = new QuotePrice();
                quotePrice.setCatalogMrc(orderPrice.getCatalogMrc());
                quotePrice.setCatalogNrc(orderPrice.getCatalogNrc());
                quotePrice.setCatalogArc(orderPrice.getCatalogArc());
                quotePrice.setReferenceName(orderPrice.getReferenceName());
                quotePrice.setReferenceId(String.valueOf(productComponent.getId()));
                quotePrice.setComputedMrc(orderPrice.getComputedMrc());
                quotePrice.setComputedNrc(orderPrice.getComputedNrc());
                quotePrice.setComputedArc(orderPrice.getComputedArc());
                quotePrice.setDiscountInPercent(orderPrice.getDiscountInPercent());
                //quotePrice.setQuoteId(orderPrice.getQuoteId());
                //quotePrice.setVersion(VersionConstants.ONE.getVersionNumber());
                quotePrice.setMinimumMrc(orderPrice.getMinimumMrc());
                quotePrice.setMinimumNrc(orderPrice.getMinimumNrc());
                quotePrice.setMinimumArc(orderPrice.getMinimumArc());
                quotePrice.setEffectiveMrc(orderPrice.getEffectiveMrc());
                quotePrice.setEffectiveNrc(orderPrice.getEffectiveNrc());
                quotePrice.setEffectiveArc(orderPrice.getEffectiveArc());
                quotePrice.setEffectiveUsagePrice(orderPrice.getEffectiveUsagePrice());
                quotePrice.setMstProductFamily(orderPrice.getMstProductFamily());
                quotePrice.setQuoteId(quoteId);
                quotePriceRepository.save(quotePrice);

            }
        }
        return quotePrice;

    }


    public QuoteOrderAmendmentBean getIsQuoteCreatedForParentOrderInOrderAmendment(String orderCode) throws  TclCommonException{
        QuoteOrderAmendmentBean quoteBeanForOrderAmendment = new QuoteOrderAmendmentBean();
        List<CheckAmendmentQuoteBean> quoteDetails = new ArrayList<>();
        try {
            if(Objects.nonNull(orderCode)) {
                List<QuoteToLe> quoteToLes = quoteToLeRepository.findByAmendmentParentOrderCode(orderCode);

                if (!quoteToLes.isEmpty()) {
                    quoteBeanForOrderAmendment.setQuoteCreated(true);
                    quoteToLes.forEach(quoteToLe -> {
                        List<QuoteIllSiteToService> siteToService = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
                        siteToService.forEach(service->{
                            CheckAmendmentQuoteBean beanForAmendment = new CheckAmendmentQuoteBean();
                            beanForAmendment.setStage(quoteToLe.getStage());
                            beanForAmendment.setQuoteId(quoteToLe.getQuote().getId());
                            beanForAmendment.setQuoteType(quoteToLe.getQuoteType());
                            beanForAmendment.setQuoteToLeId(quoteToLe.getId());
                            OrderIllSite orderIllSite = orderIllSiteRepository.findById(service.getParentSiteId()).get();
                            beanForAmendment.setSiteCode(orderIllSite.getSiteCode());
                            beanForAmendment.setServiceId(service.getO2cServiceId());
                            beanForAmendment.setAllowAmendment(service.getAllowAmendment());
                            quoteDetails.add(beanForAmendment);
                        });
                    });
                    quoteBeanForOrderAmendment.setCheckQuoteBeanForAmendmentList(quoteDetails);

                } else {
                    quoteBeanForOrderAmendment.setQuoteCreated(false);
                }
            }
        }
        catch(Exception e){
            LOGGER.error("Exception occured while checking if quote is created for Order Amendment {} ", e);
        }
        return  quoteBeanForOrderAmendment;
    }






    public Integer updateLocationsToCorrespondingSites(SiteUpdateForAmendmentBean siteUpdateForAmendmentBean) throws TclCommonException{
        try {
            validateSiteUpdateRequest(siteUpdateForAmendmentBean);
            siteUpdateForAmendmentBean.getSites().forEach(siteDetail -> {
                QuoteIllSite illSite = illSiteRepository.findById(siteDetail.getSiteId()).get();
                illSite.setErfLocSitebLocationId(siteDetail.getLocationId());
                illSiteRepository.save(illSite);
            });
        }
        catch (Exception e){
            LOGGER.error("Exception occured while updating location for a site {} ", e);
        }
        return siteUpdateForAmendmentBean.getQuoteId();
    }

    private void validateSiteUpdateRequest(SiteUpdateForAmendmentBean siteUpdateForAmendmentBean) throws TclCommonException {
        if(Objects.isNull(siteUpdateForAmendmentBean) || siteUpdateForAmendmentBean.getSites().isEmpty() || Objects.isNull(siteUpdateForAmendmentBean.getSites())){
            throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
        }
    }

    public List<LastMileProviderDetails> getVendorDetailsForSite(List<Integer> sites) throws TclCommonException {
        List<LastMileProviderDetails> details = new ArrayList<>();
        try {
            if (Objects.isNull(sites) || sites.isEmpty()) {
                throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
            }

            sites.stream().forEach(site -> {
                LastMileProviderDetails lastMileProvider = new LastMileProviderDetails();
                List<VendorDetails> vendorDetails = new ArrayList<>();
                List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite_Id(site);
                lastMileProvider.setSiteId(site);
                siteFeasibilities.forEach(siteFeasibility -> {
                    VendorDetails vendorDetail = new VendorDetails();
                    vendorDetail.setSiteFeasibilityId(siteFeasibility.getId());
                    vendorDetail.setIsSelected(siteFeasibility.getIsSelected());
                    vendorDetail.setName(siteFeasibility.getProvider());
                    vendorDetail.setType(siteFeasibility.getType());
                    vendorDetails.add(vendorDetail);
                });
                lastMileProvider.setVendorDetails(vendorDetails);
                details.add(lastMileProvider);
            });
        }
        catch (Exception e){
            LOGGER.error("Exception occured while getting the vendor list {} ", e);

        }
        return details;
    }

    public String updateFeasibleSitesAsMF(List<Integer> sites, Integer quoteLeId) throws TclCommonException {
        if (sites == null || sites.isEmpty()) {
            throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_BAD_REQUEST);
        }
        try {
            Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
            LOGGER.info("Making the sites as non feasible.");
            sites.stream().forEach(siteId -> {
                Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(siteId);
                if (siteOpt.isPresent()) {
                    LOGGER.info("Changing feasibility flag and fp status for the site for OA ---> {}", siteOpt.get().getId());
                    QuoteIllSite site = siteOpt.get();
                    site.setFeasibility((byte) 0);
                    site.setFpStatus("N");
                    LOGGER.info("Resetting the price values for the site for OA ---> {} ", site.getId());
                    illPricingFeasibilityService.removeSitePrices(site, quoteToLeOpt.get());

                    List<SiteFeasibility> siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite(site);
                    siteFeasibilityList.stream().forEach(siteFeasibility -> {
                        LOGGER.info("Changing isSelected flag for site feasiblity : {}", siteFeasibility.getId());
                        siteFeasibility.setIsSelected((byte) 0);
                        siteFeasibilityRepository.save(siteFeasibility);
                    });
                }

            });

            LOGGER.info("Recalculating QTL price for OA");
            illPricingFeasibilityService.recalculate(quoteToLeOpt.get());

        } catch(Exception e) {
            LOGGER.error("Exception occured while updating Feasible Site as Non Feasible {} ", e);
        }
        return "Updated";
    }

    public void updateVariousStages(String value, Integer quoteToLe){
        Optional<QuoteToLe> quoteLeopt = quoteToLeRepository.findById(quoteToLe);
        quoteLeopt.ifPresent(quToLe->{
            quToLe.setStage(value);
            quoteToLeRepository.save(quToLe);
        });
    }


    public List<SiteToServiceMapping> getO2cServiceIdsForSites(List<Integer> sites, Integer quoteToLe) throws  TclCommonException{
        List<SiteToServiceMapping> siteToServiceMappings = new ArrayList<>();
        try {
            sites.forEach(site -> {
                List<ServiceDetailsForASite> serviceMappings = new ArrayList<>();
                SiteToServiceMapping siteToServiceMapping = new SiteToServiceMapping();
                siteToServiceMapping.setSiteId(site);
                List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite_Id(site);
                quoteIllSiteToService.forEach(service -> {
                    ServiceDetailsForASite serviceDetailsMapping = new ServiceDetailsForASite();
                    serviceDetailsMapping.setServiceId(service.getO2cServiceId());
                    serviceDetailsMapping.setType(service.getServiceType());
                    serviceMappings.add(serviceDetailsMapping);
                });

                siteToServiceMapping.setDetails(serviceMappings);
                siteToServiceMappings.add(siteToServiceMapping);
            });
        }
        catch (Exception e){
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return  siteToServiceMappings;
    }


    public String saveAmendmentInfo(Integer quoteId, Integer quoteToLe, OrderAmendmentStatusBean orderAmendmentStatusBean) throws TclCommonException{
        try {
            QuoteToLe quoteLe = quoteToLeRepository.findById(quoteToLe).get();
            Date orderCreatedTime = orderRepository.findByOrderCode
                    (quoteLe.getAmendmentParentOrderCode()).getCreatedTime();

            List<SiteToServiceMapping> siteToServiceMappings = orderAmendmentStatusBean.getSiteToServiceMappings();
            if (orderAmendmentStatusBean.isO2c()) {
                siteToServiceMappings.forEach(siteToServiceMapping -> {
                    List<ServiceDetailsForASite> details = siteToServiceMapping.getDetails();
                    details.forEach(detail -> {
                        OrderIllSite orderIllSite = getOrderIllSite(detail);
                        OrderAmendmentStatus orderAmendmentStatus = new OrderAmendmentStatus();
                        setOrderAmendmentCommonDetails(orderAmendmentStatusBean, quoteLe, orderCreatedTime, orderIllSite, orderAmendmentStatus);
                        orderAmendmentStatus.setParentServiceId(detail.getServiceId());
                        orderAmendmentStatus.setO2cCancelTriggerStatus(BACTIVE);
                        orderAmendmentStatusRepository.save(orderAmendmentStatus);
                    });
                });
            } else if (!orderAmendmentStatusBean.isO2c()) {
                List<Integer> sites = orderAmendmentStatusBean.getSites();
                sites.forEach(site -> {
                    OrderAmendmentStatus orderAmendmentStatus = new OrderAmendmentStatus();
                    OrderIllSite orderIllSite = getOrderIllSiteIfNotO2c(site);
                    orderAmendmentStatus.setO2cCancelTriggerStatus(BDEACTIVATE);
                    setOrderAmendmentCommonDetails(orderAmendmentStatusBean, quoteLe, orderCreatedTime, orderIllSite, orderAmendmentStatus);
                    orderAmendmentStatusRepository.save(orderAmendmentStatus);
                });

            }
        }
        catch(Exception e){
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return "Success";
    }

    private OrderIllSite getOrderIllSiteIfNotO2c(Integer site) {
        List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository.findByQuoteIllSite_Id(site);
        Integer parentSiteId = quoteIllSiteToServices.stream().findAny().get().getParentSiteId();
        return orderIllSiteRepository.findById(parentSiteId).get();
    }

    private void setOrderAmendmentCommonDetails(OrderAmendmentStatusBean orderAmendmentStatusBean, QuoteToLe quoteLe, Date orderCreatedTime, OrderIllSite orderIllSite, OrderAmendmentStatus orderAmendmentStatus) {
        orderAmendmentStatus.setCreatedTime(new Date());
        orderAmendmentStatus.setModifiedBy(Utils.getSource());
        orderAmendmentStatus.setAmendmentStatus(BACTIVE);
        orderAmendmentStatus.setOrderAmendmentDate(new Date());
        orderAmendmentStatus.setOrderCode(orderAmendmentStatusBean.getOrderCode());
        orderAmendmentStatus.setParentOrderCode(quoteLe.getAmendmentParentOrderCode());
        orderAmendmentStatus.setParentSiteCode(orderIllSite.getSiteCode());
        orderAmendmentStatus.setOrderCreatedDate(orderCreatedTime);
        orderAmendmentStatus.setProductFamily(getFamilyNameForQuoteToLe(quoteLe));
    }

    private OrderIllSite getOrderIllSite(ServiceDetailsForASite detail) {
        QuoteIllSiteToService quoteIllSiteToService = quoteIllSiteToServiceRepository
                .findByO2cServiceId(detail.getServiceId());
        Integer parentSiteId = quoteIllSiteToService.getParentSiteId();
        Optional<OrderIllSite> orderIllSite = orderIllSiteRepository.findById(parentSiteId);
        return orderIllSite.get();
    }


    public String updateCategoryIfSiteShifted(Integer quoteLe) throws TclCommonException {
        QuoteToLe quoteToLe = quoteToLeRepository.findById(quoteLe).get();
        try {
            LOGGER.info("Entering method updateCategoryIfSiteShifted for quote to le ----> {} ", quoteLe);
            if ( Objects.nonNull(quoteToLe.getIsAmended()) &&
            Objects.nonNull(quoteToLe.getQuoteCategory()) && MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) && CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
                int result = Byte.compare(quoteToLe.getIsAmended(), BACTIVE);
                LOGGER.info("Result for comparison for quote -----> {}  is -----> {} ",quoteToLe.getQuote().getQuoteCode(),result );
                if (result == 0) {
                    LOGGER.info("Is amended case true for quote ------> {} ", quoteToLe.getQuote().getQuoteCode());
                  /*  QuoteIllSite quoteIllSite = quoteToLe.getQuoteToLeProductFamilies().stream()
                            .findFirst()
                            .get()
                            .getProductSolutions()
                            .stream()
                            .findFirst()
                            .get()
                            .getQuoteIllSites()
                            .stream()
                            .findFirst()
                            .get();*/
                    List<QuoteIllSite> sites = new ArrayList<>();
                    quoteToLe.getQuoteToLeProductFamilies().stream()
                            .findAny()
                            .get()
                            .getProductSolutions()
                            .forEach(soln->{
                                if(Objects.nonNull(soln.getQuoteIllSites())){
                                    soln.getQuoteIllSites()
                                            .stream()
                                            .filter(site->Objects.nonNull(site.getId())).findAny()
                                            .ifPresent(
                                                    site1 -> {
                                                        LOGGER.info("Site id is -----> {} for solution ----> {} and quote ----? {} ", site1.getId(), site1.getProductSolution()
                                                                ,site1.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode());
                                                        sites.add(site1);
                                                    }
                                            );
                                }
                            });
                    QuoteIllSite quoteIllSite= sites.get(0);
                    LOGGER.info("Quote Ill Site fr isShiftSite method for quote ------> {}  is -----> {} " , quoteIllSite.getId(),quoteToLe.getQuote().getQuoteCode());
                    List<QuoteIllSiteToService> siteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite(quoteIllSite);
                    Integer parentSiteId = siteToService.stream().findFirst().get().getParentSiteId();
                    LOGGER.info("Parent site id is ------> {} ", parentSiteId);
                    Integer oldLocationId = orderIllSitesRepository.findById(parentSiteId).get().getErfLocSitebLocationId();
                    LOGGER.info("Old location id for quote -----> {} is ----> {} ", quoteToLe.getQuote().getQuoteCode(),oldLocationId);
                    Integer newLocationId = quoteIllSite.getErfLocSitebLocationId();
                    LOGGER.info("New location id for quote -----> {} is ----> {} ", quoteToLe.getQuote().getQuoteCode(),newLocationId);
                    int compare = Integer.compare(oldLocationId, newLocationId);
                    if (compare != 0) {
                        quoteToLe.setQuoteCategory(SHIFT_SITE);
                        quoteToLeRepository.save(quoteToLe);
                    }

                }
            }
        }
        catch(Exception e){
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return quoteToLe.getQuoteCategory();
    }



    public String updateLastMileSelectedVendor(LastMileProviderDetails lastMileProviderDetails) throws TclCommonException{
        try{

            if(Objects.isNull(lastMileProviderDetails) || (Objects.nonNull(lastMileProviderDetails) && lastMileProviderDetails.getVendorDetails().isEmpty())){
                throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
            }

                List<VendorDetails> vendorDetails = lastMileProviderDetails.getVendorDetails();
                LOGGER.info("Size of vendor details List is ----> {} ", vendorDetails.size());
                vendorDetails.forEach(vendorDetail->{
                    byte selected = (byte) vendorDetail.getIsSelected();
                    int siteFeasibilityId = vendorDetail.getSiteFeasibilityId();
                    if(Objects.nonNull(siteFeasibilityId)){
                        LOGGER.info("Site feasibility id is ----> {} ", siteFeasibilityId);
                        Optional<SiteFeasibility> siteFeasibility = siteFeasibilityRepository.findById(siteFeasibilityId);
                        if(siteFeasibility.isPresent()){
                            LOGGER.info("Previously set is selected is ----> {} ", siteFeasibility.get().getIsSelected());
                            siteFeasibility.get().setIsSelected(selected);
                            LOGGER.info("The newly set is selected is---> {} ", siteFeasibility.get().getIsSelected());
                            siteFeasibilityRepository.save(siteFeasibility.get());
                        }
                    }
                });

        }
        catch (Exception e){
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

        }
        return "Success";
    }


   /* public String updateSalesAdminRegion(List<SalesAdminRegion> salesAdminRegions){
        salesAdminRegions.forEach(region->{
            SalesAdminRegionSfdc salesAdminRegionSfdc= new SalesAdminRegionSfdc();
            salesAdminRegionSfdc.setAccountManager(region.getName());
            salesAdminRegionSfdc.setRegion(region.getRegion());
            salesAdminRegionSfdcRepository.save(salesAdminRegionSfdc);
        });
        return "Success";
    }*/

}
