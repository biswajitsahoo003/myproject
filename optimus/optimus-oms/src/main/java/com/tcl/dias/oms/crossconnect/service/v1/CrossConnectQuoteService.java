package com.tcl.dias.oms.crossconnect.service.v1;

import static com.tcl.dias.oms.constants.MACDConstants.MACD_QUOTE_TYPE;
import static com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants.CROSS_CONNECT_LOCAL_DEMARCATION_ID;
import static java.lang.Integer.parseInt;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.tcl.dias.oms.beans.*;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.NplSite;
import com.tcl.dias.oms.npl.beans.NplSiteDetail;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteAddress;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SiteFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteAddressRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteDifferentialCommercialRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;


@Service
@Transactional
public class CrossConnectQuoteService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CrossConnectQuoteService.class);

    @Autowired
    public QuoteRepository quoteRepository;

    @Autowired
    public QuoteToLeRepository quoteToLeRepository;

    @Autowired
    public IllSiteRepository illSiteRepository;

    @Autowired
    public QuoteProductComponentRepository quoteProductComponentRepository;

    @Autowired
    public MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;

    @Autowired
    ProductSolutionRepository productSolutionRepository;

    @Autowired
    public QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

    @Autowired
    MstProductOfferingRepository mstProductOfferingRepository;

    @Autowired
    public QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    @Autowired
    public ProductAttributeMasterRepository productAttributeMasterRepository;

    @Autowired
    QuotePriceRepository quotePriceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    OrderConfirmationAuditRepository orderConfirmationAuditRepository;

    @Autowired
    CofDetailsRepository cofDetailsRepository;

    @Autowired
    DocusignAuditRepository docusignAuditRepository;

    @Autowired
    SiteFeasibilityRepository siteFeasibilityRepository;

    @Autowired
    QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

    @Autowired
    SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

    @Autowired
    IllPricingFeasibilityService illPricingFeasibilityService;

    @Autowired
    OrderIllSitesRepository orderIllSitesRepository;

    @Autowired
    OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

    @Autowired
    OrderIllSiteSlaRepository orderIllSiteSlaRepository;
    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;
    @Autowired
    OrderPriceRepository orderPriceRepository;
    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
    
    @Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;

	@Autowired
	protected MQUtils mqUtils;
	
	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	
	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

    @Autowired
    MACDUtils macdUtils;

    @Autowired
    NplQuoteService nplQuoteService;



    public QuoteBean updateCrossConnectSite(QuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId)
            throws TclCommonException {
        QuoteBean quoteBean = null;
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
                    Integer linkCount=site.getLinkCount();
                    while(linkCount>0) {
                        processSiteDetail(user, productFamily, quoteToLeProductFamily, site, productOfferingName,
                                quoteToLe.get().getQuote());
                        linkCount--;
                    }
                }
                quoteDetail.setQuoteId(quoteId);
            }
            if (quoteToLe.isPresent()) {
                if (quoteToLe.get().getStage().equals(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode())) {
                    quoteToLe.get().setStage(QuoteStageConstants.ADD_LOCATIONS.getConstantCode());
                    quoteToLeRepository.save(quoteToLe.get());
                }
            }
            quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), false, null);

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return quoteBean;
    }
    /**
     * Thi method is used for validating the site information
     * validateSiteInformation
     *
     * @param quoteDetail
     * @throws TclCommonException
     */
    public void validateSiteInformation(QuoteDetail quoteDetail) throws TclCommonException {
        if ((quoteDetail == null) || quoteDetail.getSite() == null) {
            throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

        }
    }

    /**
     * Thi method is used for validating the site information
     * validateSiteInformation
     *
     * @param quoteDetail
     * @throws TclCommonException
     */
    public void validateSiteInformationForMacd(NplQuoteDetail quoteDetail) throws TclCommonException {
        if ((quoteDetail == null) || quoteDetail.getSite() == null) {
            throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

        }
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
    public MstProductFamily getProductFamily(String productName) throws TclCommonException {
        MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
        if (mstProductFamily == null) {
            throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
        }
        return mstProductFamily;

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
    public void processSiteDetail(User user, MstProductFamily productFamily,
                                  QuoteToLeProductFamily quoteToLeProductFamily, Site site, String productOfferingName, Quote quote)
            throws TclCommonException {
        try {
            MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName, user);
            ProductSolution productSolution = productSolutionRepository
                    .findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
            constructIllSites(productSolution, user, site, productFamily, quote);

        } catch (TclCommonException e) {
            throw new TclCommonException(e);
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
    }
    /**
     * @link http://www.tatacommunications.com/ getQuoteDetails- This method is used
     *       to get the quote details
     *
     * @param quoteId
     * @param version
     * @return QuoteDetail
     * @throws TclCommonException
     */
    public QuoteBean getQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProperitiesRequired,
                                     Integer siteId) throws TclCommonException {
        QuoteBean response = null;
        try {
            LOGGER.info("Inside Get Quote for quoteId {}",quoteId);
            validateGetQuoteDetail(quoteId);
            Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
                    && feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString())) ? true : false;
            LOGGER.info("Get quote request is to fetch with non feasibile Items status {}",isFeasibleSites);
            Quote quote = getQuote(quoteId);
            LOGGER.info("Quote code for the fetched quote {} is {}",quoteId,quote.getQuoteCode());
            response = constructQuote(quote, isFeasibleSites, isSiteProperitiesRequired, siteId);

            Optional<QuoteToLe> quoteToLe1 = quote.getQuoteToLes().stream().findFirst()
                    .filter(quoteToLe -> MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()));

            if (quoteToLe1.isPresent()) {
                response.setQuoteType(quoteToLe1.get().getQuoteType());
                response.setQuoteCategory(quoteToLe1.get().getQuoteCategory());
            }

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_GET_QUOTE_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return response;
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
    public MstProductOffering getProductOffering(MstProductFamily mstProductFamily, String productOfferingName,
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
     *
     * constructIllSites- This methods is used to construct the IllSites entity
     *
     * @param productSolution
     * @param userId
     * @return void
     * @throws TclCommonException
     */
    public void constructIllSites(ProductSolution productSolution, User user, Site site,
                                  MstProductFamily productFamily, Quote quote) throws TclCommonException {
        SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
                SolutionDetail.class);

        List<SiteDetail> siteInp = site.getSite();
        for (SiteDetail siteDetail : siteInp) {
            //if (siteDetail.getSiteId() == null) {
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
                illSiteRepository.save(illSite);
                siteDetail.setSiteId(illSite.getId());
                for (ComponentDetail componentDetail : soDetail.getComponents()) {
                    processProductComponent(productFamily, illSite, componentDetail, user);
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
            /*} else {
                QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(siteDetail.getSiteId(), (byte) 1);
                if (illSiteEntity != null) {
                    illSiteEntity.setProductSolution(productSolution);
                    illSiteRepository.save(illSiteEntity);
                    removeComponentsAndAttr(illSiteEntity.getId());
                    for (ComponentDetail componentDetail : soDetail.getComponents()) {
                        processProductComponent(productFamily, illSiteEntity, componentDetail, user);
                    }
                }
            }*/
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
    public void validateGetQuoteDetail(Integer quoteId) throws TclCommonException {
        if (quoteId == null) {
            throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

        }
    }
    /**
     * @link http://www.tatacommunications.com/
     * @throws TclCommonException
     */
    public Quote getQuote(Integer quoteId) throws TclCommonException {

        Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
        if (quote == null) {
            throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
        }

        return quote;
    }
    /**
     * @link http://www.tatacommunications.com constructQuote
     * @param quote
     * @throws TclCommonException
     */

    public QuoteBean constructQuote(Quote quote, Boolean isFeasibleSites, Boolean isSiteProperitiesRequired,
                                    Integer siteId) throws TclCommonException {
        QuoteBean quoteDto = new QuoteBean();
        quoteDto.setQuoteId(quote.getId());
        quoteDto.setQuoteCode(quote.getQuoteCode());
        quoteDto.setCreatedBy(quote.getCreatedBy());
        quoteDto.setCreatedTime(quote.getCreatedTime());
        quoteDto.setStatus(quote.getStatus());
        quoteDto.setTermInMonths(quote.getTermInMonths());
        List<QuoteToLe> quoteToLe=new ArrayList<QuoteToLe>();
        quoteToLe=quoteToLeRepository.findByQuote_Id(quote.getId());
        LOGGER.info("quote to le is fetched for quote id {}",quote.getId());
        if (quoteToLe.size() != 0) {
            quoteDto.setQuoteStatus(quoteToLe.get(0).getCommercialStatus());
            LOGGER.info("Commercial Status is set as  {}",quoteToLe.get(0).getCommercialStatus());
        }
        if (quote.getCustomer() != null) {
            quoteDto.setCustomerId(quote.getCustomer().getErfCusCustomerId());
        }
        quoteDto.setLegalEntities(constructQuoteLeEntitDtos(quote, isFeasibleSites, isSiteProperitiesRequired, siteId, quoteToLe));

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
     * processProductComponent- This method process the product component details
     *
     * @param productFamily
     * @param illSite
     * @param component
     * @param user
     * @throws TclCommonException
     */
    public void processProductComponent(MstProductFamily productFamily, QuoteIllSite illSite,
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
        } catch (TclCommonException e) {
            throw new TclCommonException(e);
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
    }
    /**
     * @link http://www.tatacommunications.com constructQuoteLeEntitDto
     * @param quote
     * @throws TclCommonException
     */
    public Set<QuoteToLeBean> constructQuoteLeEntitDtos(Quote quote, Boolean isFeasibleSites,
                                                        Boolean isSiteProperitiesRequired, Integer siteId, List<QuoteToLe> quoteToLe) throws TclCommonException {

        Set<QuoteToLeBean> quoteToLeDtos = new HashSet<>();
        for (QuoteToLe quTle : quoteToLe) {
            LOGGER.info("Fetching quoteToLe {}", quTle.getId());
            QuoteToLeBean quoteToLeDto = new QuoteToLeBean(quTle);
            quoteToLeDto.setTermInMonths(quTle.getTermInMonths());
            quoteToLeDto.setCurrency(quTle.getCurrencyCode());
            quoteToLeDto.setLegalAttributes(constructLegalAttributes(quTle));
            quoteToLeDto.setProductFamilies(constructQuoteToLeFamilyDtos(getProductFamilyBasenOnVersion(quTle),
                    isFeasibleSites, isSiteProperitiesRequired, siteId));
            quoteToLeDto.setClassification(quTle.getClassification());
            quoteToLeDtos.add(quoteToLeDto);
        }

        return quoteToLeDtos;

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
     * @link http://www.tatacommunications.com/ constructMstProperties used to
     *       construct Mst Properties
     * @param id
     * @param localITContactId
     */
    public MstProductComponent getMstProperties(User user) {
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
     * removeComponentsAndAttr
     *
     * @param siteId
     */
    public void removeComponentsAndAttr(Integer siteId) {
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
     * getProductComponent- This method takes the component name and gives the
     * {@link MstProductComponent}
     *
     * @param user
     *
     * @param componentName
     * @return MstProductComponent
     * @throws TclCommonException
     */
    public MstProductComponent getProductComponent(ComponentDetail component, User user) throws TclCommonException {
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
     * constructProductComponent- This method constructs the
     * {@link QuoteProductComponent} Entity
     *
     * @param productComponent
     * @param mstProductFamily
     * @param illSiteId
     * @return QuoteProductComponent
     * @throws TclCommonException
     */
    public QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
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
    public void processProductAttribute(QuoteProductComponent quoteComponent, AttributeDetail attribute, User user)
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
     * @link http://www.tatacommunications.com constructLegalAttributes used to
     *       construct legal attributes
     * @param quTle
     * @return
     */
    public Set<LegalAttributeBean> constructLegalAttributes(QuoteToLe quTle) {

        Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
        List<QuoteLeAttributeValue> attributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quTle);
        if (attributeValues != null) {
            LOGGER.info("getting quote to le attributes for quoteLe id {}",quTle.getId());
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
     * @link http://www.tatacommunications.com/ constructQuoteToLeFamilyDtos
     * @param quoteToLeProductFamilies
     * @throws TclCommonException
     */
    public Set<QuoteToLeProductFamilyBean> constructQuoteToLeFamilyDtos(
            List<QuoteToLeProductFamily> quoteToLeProductFamilies, Boolean isFeasibleSites,
            Boolean isSiteProperitiesRequired, Integer siteId) throws TclCommonException {
        Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilyBeans = new HashSet<>();
        if (quoteToLeProductFamilies != null) {
            for (QuoteToLeProductFamily quFamily : quoteToLeProductFamilies) {
                QuoteToLeProductFamilyBean quoteToLeProductFamilyBean = new QuoteToLeProductFamilyBean();
                if (quFamily.getMstProductFamily() != null) {
                    quoteToLeProductFamilyBean.setStatus(quFamily.getMstProductFamily().getStatus());
                    quoteToLeProductFamilyBean.setProductName(quFamily.getMstProductFamily().getName());
                }
                List<ProductSolutionBean> solutionBeans = getSortedSolution(
                        constructProductSolution(getProductSolutionBasenOnVersion(quFamily), isFeasibleSites,
                                isSiteProperitiesRequired, siteId));
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
    public List<QuoteToLeProductFamily> getProductFamilyBasenOnVersion(QuoteToLe quoteToLe) {
        List<QuoteToLeProductFamily> prodFamilys = null;
        prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId());
        return prodFamilys;

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
    public ProductAttributeMaster getProductAttributes(AttributeDetail attributeDetail, User user)
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
     * constructProductAttribute- This method constructs the
     * {@link QuoteProductComponentsAttributeValue} Entity
     *
     * @param quoteProductComponent
     * @param productAttributeMaster
     * @param attributeDetail
     * @return QuoteProductComponentsAttributeValue
     */
    public QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
                                                                          ProductAttributeMaster productAttributeMaster, AttributeDetail attributeDetail) {
        QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
        quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
        quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
        quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
        return quoteProductComponentsAttributeValue;

    }
    /**
     * constructMstAttributBean
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
            mstOmsAttributeBean.setName(mstOmsAttribute.getName());
            mstOmsAttributeBean.setId(mstOmsAttribute.getId());
            mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
            mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
        }
        return mstOmsAttributeBean;
    }
    /**
     * @link http://www.tatacommunications.com/
     * @throws TclCommonException
     */
    public List<ProductSolution> getProductSolutionBasenOnVersion(QuoteToLeProductFamily family) {
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
    public List<ProductSolutionBean> constructProductSolution(List<ProductSolution> productSolutions,
                                                              Boolean isFeasibleSites, Boolean isSiteProperitiesRequired, Integer siteId) throws TclCommonException {
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
                LOGGER.info("Getting illSite bean");
                List<QuoteIllSiteBean> illSiteBeans = getSortedIllSiteDtos(constructIllSiteDtos(
                        getIllsitesBasenOnVersion(solution, siteId), isFeasibleSites, isSiteProperitiesRequired));
                LOGGER.info("Fetched illSite bean");
                productSolutionBean.setSites(illSiteBeans);
                productSolutionBeans.add(productSolutionBean);

            }
        }
        return productSolutionBeans;
    }
    public List<ProductSolutionBean> getSortedSolution(List<ProductSolutionBean> solutionBeans) {
        if (solutionBeans != null) {

            solutionBeans.sort(Comparator.comparingInt(ProductSolutionBean::getProductSolutionId));
        }

        return solutionBeans;

    }
    public List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution, Integer siteId) {

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
     * @link http://www.tatacommunications.com/ constructIllSiteDtos
     * @param illSites,version
     * @return List<QuoteIllSiteBean>
     */
    public List<QuoteIllSiteBean> constructIllSiteDtos(List<QuoteIllSite> illSites, Boolean isFeasibleSites,
                                                       Boolean isSiteProperitiesRequired) throws TclCommonException {
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
                    //illSiteBean.setFeasibility(constructSiteFeasibility(illSite));
                    List<QuoteProductComponentBean> quoteProductComponentBeans = getSortedComponents(
                            constructQuoteProductComponent(illSite.getId(), false, isSiteProperitiesRequired));
                    illSiteBean.setComponents(quoteProductComponentBeans);
                    illSiteBean.setChangeBandwidthFlag(illSite.getMacdChangeBandwidthFlag());
                    //illSiteBean.setIsTaskTriggered(illSite.getIsTaskTriggered());
                   // illSiteBean.setMfStatus(illSite.getMfStatus());
                    LOGGER.info("before calling generate existing components for site ---> {} ", illSite.getId());
                    illSiteBean.setExistingComponentsList((generateExistingComponentsForMacd(quoteToLe, illSiteBean)));
                    sites.add(illSiteBean);
                    
            		// Termination specific data
					if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
						LOGGER.info("Getting termination related details for quoteToLe Id {}", quoteToLe.getId());
						List<QuoteSiteServiceTerminationDetailsBean> terminationSiteServiceBeanList = new ArrayList<>();
						List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite(illSite);
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
     * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
     * @param illSites,version
     * @return List<QuoteIllSiteBean>
     */
    public List<QuoteIllSiteBean> getSortedIllSiteDtos(List<QuoteIllSiteBean> illSiteBeans) {
        if (illSiteBeans != null) {
            illSiteBeans.sort(Comparator.comparingInt(QuoteIllSiteBean::getSiteId));

        }

        return illSiteBeans;
    }
    /**
     * constructSlaDetails
     *
     * @param illSite
     */
    public List<QuoteSlaBean> constructSlaDetails(QuoteIllSite illSite) {

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
     * constructSiteFeasibility
     *
     * @param illSite
     * @return
     */
    public List<SiteFeasibilityBean> constructSiteFeasibility(QuoteIllSite illSite) {
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
    public SiteFeasibilityBean constructSiteFeasibility(SiteFeasibility siteFeasibility) {
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
     * @link http://www.tatacommunications.com/ constructQuoteProductComponent
     * @param id,version
     */
    public List<QuoteProductComponentBean> constructQuoteProductComponent(Integer id, boolean isSitePropertiesNeeded,
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
                                isSitePropertiesNeeded, isSitePropNeeded)));
                quoteProductComponentBean.setAttributes(attributeValueBeans);
                quoteProductComponentDtos.add(quoteProductComponentBean);
            }

        }
        return quoteProductComponentDtos;

    }
    /**
     * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
     * @param illSites,version
     * @return List<QuoteIllSiteBean>
     */
    public List<QuoteProductComponentBean> getSortedComponents(List<QuoteProductComponentBean> quoteComponentBeans) {
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

    public List<QuoteProductComponent> getComponentBasenOnVersion(Integer siteId, boolean isSitePropertiesNeeded,
                                                                  boolean isSitePropNeeded) {
        List<QuoteProductComponent> components = null;
        if (isSitePropertiesNeeded) {
            LOGGER.info("Getting quote Product Component for siteId {}",siteId);
            components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId,
                    IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.ILLSITES.toString());
            LOGGER.info("Fetched quote Product Component for siteId {}",siteId);
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
     *constructComponentPriceDto used to get price of
     *         componenet
     * @link http://www.tatacommunications.com/
     * @param QuoteProductComponent
     */
    public QuotePriceBean constructComponentPriceDto(QuoteProductComponent quoteProductComponent) {
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
    public List<QuoteProductComponentsAttributeValue> getAttributeBasenOnVersion(Integer componentId,
                                                                                 boolean isSitePropRequire, Boolean isSiteRequired) {
        List<QuoteProductComponentsAttributeValue> attributes = null;
        List<QuoteProductComponentsAttributeValue> attributes1 = null;

        attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);

        if (isSitePropRequire) {
            attributes = quoteProductComponentsAttributeValueRepository
                    .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentId,
                            "LOCAL_IT_CONTACT_A");
            attributes1 = quoteProductComponentsAttributeValueRepository
                    .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentId,
                            "LOCAL_IT_CONTACT_Z");
            attributes.addAll(attributes1);

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
     * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
     * @param illSites,version
     * @return List<QuoteIllSiteBean>
     */
    public List<QuoteProductComponentsAttributeValueBean> getSortedAttributeComponents(
            List<QuoteProductComponentsAttributeValueBean> attributeBeans) {
        if (attributeBeans != null) {
            attributeBeans.sort(Comparator.comparingInt(QuoteProductComponentsAttributeValueBean::getAttributeId));

        }

        return attributeBeans;
    }
    /**
     * @link http://www.tatacommunications.com constructAttribute used to constrcut
     *       attribute
     * @param quoteProductComponentsAttributeValues
     * @return
     */
    public List<QuoteProductComponentsAttributeValueBean> constructAttribute(
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
     * @constructAttributePriceDto used to constrcut attribute price
     */
    public QuotePriceBean constructAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue) {
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

    @Transactional
    public QuoteDetail processDeactivateCrossConnectSites(Integer siteId, Integer quoteId, String action)
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
            illPricingFeasibilityService.recalculateSites(quoteToLe.getId());

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return quoteDetail;

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
     * @param familyName
     * @link http://www.tatacommunications.com/ deletedIllsiteAndRelation used to
     *       delete ill site and its relation
     *
     * @param quoteIllSite
     */
    public void deletedIllsiteAndRelation(QuoteIllSite quoteIllSite) {
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

        illSiteRepository.delete(quoteIllSite);

    }

    /**
     * @link http://www.tatacommunications.com/ editSiteComponent used to edit site
     *       component values
     * @param request
     * @return
     */
    public QuoteDetail editCorssConnectSiteComponent(UpdateRequest request) throws TclCommonException {
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
     * validateRequest
     *
     * @param request
     */
    public void validateRequest(UpdateRequest request) throws TclCommonException {
        if (request.getComponentDetails() == null || request.getComponentDetails().isEmpty()) {
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
    public QuoteProductComponentsAttributeValue updateProductAttribute(UpdateRequest request,
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
                    .findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteEntity.get().getId(), cmpDetail.getName(),QuoteConstants.ILLSITES.toString());
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
                saveProductAttributevalue(productAttributeMaster, attributeDetail, quoteProductComponent,
                        quoteProductComponentsAttributeValue);
            }
        }
        return quoteProductComponentsAttributeValue;
    }
    /**
     * New attribute value insert/update into the table while edit the confoquration
     *
     * @param productAttributeMaster
     * @param attributeDetail
     * @param quoteProductComponent
     * @param quoteProductComponentsAttributeValue
     */

    public void saveProductAttributevalue(ProductAttributeMaster productAttributeMaster,
                                             AttributeDetail attributeDetail, QuoteProductComponent quoteProductComponent,
                                             QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
        quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
        quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
        quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
        quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
        quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);

    }
    /**
     * @author
     * @param oSolution
     * @link http://www.tatacommunications.com/ constructIllSiteDtos
     * @param illSites,version
     * @return List<QuoteIllSiteBean>
     */
    public Set<OrderIllSite> constructOrderIllSite(List<QuoteIllSite> illSites, OrderProductSolution oSolution, NplQuoteDetail detail) {
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
                orderIllSitesRepository.save(orderSite);
                persistOrderSiteAddress(illSite.getErfLocSitebLocationId(), "b",String.valueOf(orderSite.getId()),QuoteConstants.NPL_SITES.toString());//Site
				persistOrderSiteAddress(illSite.getErfLocSiteaLocationId(), "a",String.valueOf(orderSite.getId()),QuoteConstants.NPL_SITES.toString());//Pop
               // orderSite.setOrderSiteFeasibility(constructOrderSiteFeasibility(illSite, orderSite));
                orderSite.setOrderIllSiteSlas(constructOrderSiteSla(illSite, orderSite));
                constructOrderProductComponent(illSite.getId(), orderSite);
                persistQuoteSiteCommercialsAtServiceIdLevel(illSite);
                
                sites.add(orderSite);
                } else {
                    detail.setManualFeasible(true);
                }
            }
        }

        return sites;
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
     * constructOrderSiteSla
     *
     * @param illSite
     * @param orderSite
     */
    private Set<OrderIllSiteSla> constructOrderSiteSla(QuoteIllSite illSite, OrderIllSite orderSite) {
        Set<OrderIllSiteSla> orderIllSiteSlas = new HashSet<>();

        if (illSite.getQuoteIllSiteSlas() != null) {
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
        }

        return orderIllSiteSlas;
    }
    /**
     * @author
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
     * @author
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
			LOGGER.info("Querying component  Id {} , {}", quoteProductComponent.getId(),
					QuoteConstants.COMPONENTS);
			List<QuotePrice> prices = quotePriceRepository.findByReferenceNameAndReferenceId(
					QuoteConstants.COMPONENTS.toString(), String.valueOf(quoteProductComponent.getId()));
			for (QuotePrice price : prices) {
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
				break;
			}

		}
        return orderPrice;

    }
    /**
     * @author
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
     * @author
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
                orderPriceRepository.save(orderPrice);
            }

        }
        return orderPrice;

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
     * @author
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
            saveIllsiteProperties(quoteIllSite, request, user, mstProductFamily);

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }

        return detail;
    }

    /**
     * @author
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
    private void upateSitePropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
                                              AttributeDetail attributeDetail, QuoteProductComponent orderProductComponent) {

        List<QuoteProductComponentsAttributeValue> orderProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
                .findByQuoteProductComponentAndProductAttributeMaster(orderProductComponent,
                        productAttributeMasters.get(0));
        if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
            for (QuoteProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
                orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
                orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
                quoteProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
            }
        } else {

            orderProductComponent.setQuoteProductComponentsAttributeValues(
                    createAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

        }

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
    private Set<QuoteProductComponentsAttributeValue> createAttributes(ProductAttributeMaster attributeMaster,
                                                                       QuoteProductComponent orderProductComponent, AttributeDetail attributeDetail) {

        Set<QuoteProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();

        QuoteProductComponentsAttributeValue orderProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
        orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
        orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
        orderProductComponentsAttributeValue.setQuoteProductComponent(orderProductComponent);
        orderProductComponentsAttributeValue.setProductAttributeMaster(attributeMaster);
        quoteProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
        orderProductComponentsAttributeValues.add(orderProductComponentsAttributeValue);

        return orderProductComponentsAttributeValues;

    }
    /**
     * @param siteId
     * @throws TclCommonException getSiteProperties used to get only site specific
     *                            attributes
     */
    public List<QuoteProductComponentBean> getSiteProperties(Integer siteId) throws TclCommonException {
        List<QuoteProductComponentBean> quoteProductComponentBeans = null;
        try {
            if (siteId == null) {
                throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

            }
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
     * Get Local IT Contact And Demarcation ReferenceId
     * @param siteId
     * @return
     */
    public Integer getLocalITContactAndDemarcationReferenceId(Integer siteId) {
        List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId,
                IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), QuoteConstants.ILLSITES.toString());
        if (!CollectionUtils.isEmpty(components)) {
            QuoteProductComponent quoteProductComponentVal = components.stream()
                    .filter(quoteProductComponent ->
                    quoteProductComponent.getMstProductComponent().getName()
                            .equalsIgnoreCase(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties()))
                    .findFirst().get();
            if (Objects.nonNull(quoteProductComponentVal)) {
                if(Objects.nonNull(quoteProductComponentVal.getQuoteProductComponentsAttributeValues()) &&
                        !quoteProductComponentVal.getQuoteProductComponentsAttributeValues().isEmpty()){
                    QuoteProductComponentsAttributeValue crossConnectLocalDemarcationId = quoteProductComponentVal.getQuoteProductComponentsAttributeValues().
                            stream()
                            .filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()
                                    .equalsIgnoreCase(CROSS_CONNECT_LOCAL_DEMARCATION_ID)).findFirst().get();
                    if (Objects.nonNull(crossConnectLocalDemarcationId.getAttributeValues())) {
                        return Integer.valueOf(crossConnectLocalDemarcationId.getAttributeValues());
                    }
                }
            }
        }

        return 0;
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
			
		List<QuoteProductComponent> quoteProductComponentListPrimary = quoteProductComponentRepository.findByReferenceIdAndMstProductFamily_Name(illSite.getId(), "NPL");
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


    public QuoteBean updateMACDCrossConnectSite(NplQuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId)
            throws TclCommonException {
        QuoteBean quoteBean = null;
        try {
            LOGGER.info("Customer Id received is {}", erfCustomerId);
            validateSiteInformationForMacd(quoteDetail);
            User user = getUserId(Utils.getSource());
            List<NplSite> sites = quoteDetail.getSite();
            MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
            if (quoteToLe.isPresent()) {
                for (NplSite site : sites) {
                    QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
                            .findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
                    String productOfferingName = site.getOfferingName();
                    Integer linkCount=quoteDetail.getLinkCount();
                    while(linkCount>0) {
                        processMACDSiteDetail(user, productFamily, quoteToLeProductFamily, site, productOfferingName,
                                quoteToLe.get().getQuote());
                        linkCount--;
                    }
                }
                quoteDetail.setQuoteId(quoteId);
            }
            if (quoteToLe.isPresent()) {
                if (quoteToLe.get().getStage().equals(QuoteStageConstants.MODIFY.getConstantCode())) {
                    quoteToLe.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
                    quoteToLeRepository.save(quoteToLe.get());
                }
            }
            quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), false, null);

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return quoteBean;
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
    public void processMACDSiteDetail(User user, MstProductFamily productFamily,
                                  QuoteToLeProductFamily quoteToLeProductFamily, NplSite site, String productOfferingName, Quote quote)
            throws TclCommonException {
        try {
            MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName, user);
            ProductSolution productSolution = productSolutionRepository
                    .findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
            constructMACDIllSites(productSolution, user, site, productFamily, quote);

        } catch (TclCommonException e) {
            throw new TclCommonException(e);
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
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
    public void constructMACDIllSites(ProductSolution productSolution, User user, NplSite site,
                                  MstProductFamily productFamily, Quote quote) throws TclCommonException {
        SolutionDetail soDetail = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
                SolutionDetail.class);
        List<QuoteToLe> quoteToLe=quoteToLeRepository.findByQuote(quote);
        List<NplSiteDetail> siteInp = site.getSite();
        for (NplSiteDetail siteDetail : siteInp) {
            //if (siteDetail.getSiteId() == null) {
            QuoteIllSite illSite = new QuoteIllSite();
            if (Boolean.TRUE.equals(siteDetail.getSiteChangeflag())) {
                illSite.setNplShiftSiteFlag(1);
            }
            if (siteDetail.getSecondLocationId() != null) {
                illSite.setErfLocSiteaLocationId(siteDetail.getSecondLocationId());
            } else {
                illSite.setErfLocSiteaLocationId(siteDetail.getLocationId());
            }
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
            illSiteRepository.save(illSite);
            siteDetail.setSiteId(illSite.getId());
            for (ComponentDetail componentDetail : soDetail.getComponents()) {
                processProductComponent(productFamily, illSite, componentDetail, user);
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
                List<QuoteIllSiteToService> quoteSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(siteDetail.getErfServiceInventoryTpsServiceId(), quoteToLe.stream().findFirst().get());
                if (quoteSiteToService != null && !quoteSiteToService.isEmpty()) {
                    quoteSiteToService.stream().forEach(quoteSiteToServiceRecord -> {
                        LOGGER.info("Updating quoteIllSite data in QuoteIllSiteToService for GVPN site id {} ", illSite.getId());
                        quoteSiteToServiceRecord.setQuoteIllSite(illSite);
                        quoteIllSiteToServiceRepository.save(quoteSiteToServiceRecord);
                    });
                }
            }
            /*} else {
                QuoteIllSite illSiteEntity = illSiteRepository.findByIdAndStatus(siteDetail.getSiteId(), (byte) 1);
                if (illSiteEntity != null) {
                    illSiteEntity.setProductSolution(productSolution);
                    illSiteRepository.save(illSiteEntity);
                    removeComponentsAndAttr(illSiteEntity.getId());
                    for (ComponentDetail componentDetail : soDetail.getComponents()) {
                        processProductComponent(productFamily, illSiteEntity, componentDetail, user);
                    }
                }
            }*/
        }

    }

    private List<MACDExistingComponentsBean> generateExistingComponentsForMacd(QuoteToLe quoteToLe, QuoteIllSiteBean illSiteBean)
    {
        List<MACDExistingComponentsBean> existingComponentsBeanList=new ArrayList<>();
        List<String> serviceIdsList=new ArrayList<>();

        Optional<QuoteIllSite> quoteIllSite=illSiteRepository.findById(illSiteBean.getSiteId());
        Map<String,String> serviceIdsMap=macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite.get(),quoteToLe);
        LOGGER.info("Service id map value is ----> {} ", Optional.ofNullable(serviceIdsMap));
        if(Objects.nonNull(serviceIdsMap)) {
            String primaryServiceId = serviceIdsMap.get(PDFConstants.PRIMARY_CC);
            if(Objects.isNull(primaryServiceId)){
                primaryServiceId = serviceIdsMap.get(PDFConstants.PRIMARY);
            }
            String secondaryServiceId = serviceIdsMap.get(PDFConstants.SECONDARY);
            if (Objects.nonNull(primaryServiceId)){
                serviceIdsList.add(primaryServiceId);
                LOGGER.info("Primary service ID ---> {} ", primaryServiceId);
            }
            if (Objects.nonNull(secondaryServiceId))
                serviceIdsList.add(secondaryServiceId);
        }
        LOGGER.info("ServiceIdsList"+serviceIdsList);
        if(!serviceIdsList.isEmpty())
        {
            serviceIdsList.stream().forEach(serviceId->{
                try {
                    LOGGER.info("Inside generateExistingComponentsForMacd for MMR for service ID ---> {}", serviceId);
                    MACDExistingComponentsBean existingComponent = new MACDExistingComponentsBean();
                    //order Id need to be removed
                    List<Map> existingComponentMap = nplQuoteService.
                    constructExistingComponentsforIsvPage(quoteToLe, serviceId);
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

}
