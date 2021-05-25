package com.tcl.dias.oms.service.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.Mf3DSiteStatus;
import com.tcl.dias.common.beans.MfNplResponseDetailBean;
import com.tcl.dias.common.beans.MfResponseDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.PreMfRequest;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasiblityRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.PreMfRequestRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.iwan.service.v1.IwanPricingFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanGvpnPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanIllPricingAndFeasiblityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants;
import com.tcl.dias.oms.npl.pricing.bean.Feasible;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service related to all manual site feasibilities
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class ManualSiteFeasibilityService {

    public static final String MF = "MF";

    private static final Logger LOGGER = LoggerFactory.getLogger(ManualSiteFeasibilityService.class);
    public static final String MANUAL_FEASIBLE = "Manual Feasible";
    public static final String RETURNED = "Return";
    public static final String NOT_FEASIBLE = "Not Feasible";
    public static final String FEASIBLE = "Feasible";



    @Autowired
    SiteFeasibilityRepository siteFeasibilityRepository;

    @Autowired
    IllSiteRepository illSiteRepository;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    IllPricingFeasibilityService illPricingFeasibilityService;
    
    @Autowired
    IwanPricingFeasibilityService iwanPricingFeasibilityService;
    
    @Autowired
    NplPricingFeasibilityService nplPricingFeasibilityService;
    
    @Autowired
    GvpnPricingFeasibilityService gvpnPricingFeasibilityService;

    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;
    
    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
    
    @Autowired
    IllQuoteService illQuoteService;
    
    @Autowired
    PreMfRequestRepository preMfRequestRepository;
    
    @Autowired
    LinkFeasibilityRepository linkFeasibilityRepository;
    
    @Autowired
    NplLinkRepository nplLinkRepository;
    
    @Autowired
    QuoteRepository quoteRepository;
    
    @Autowired
    IzosdwanQuoteService izosdwanQuoteService;
    
    @Autowired
    QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;
    
    @Autowired
    IzosdwanSiteFeasiblityRepository izosdwanSiteFeasiblityRepository;
    
    @Autowired
    IzosdwanIllPricingAndFeasiblityService izosdwanIllPricingAndFeasiblityService;
    
    @Autowired
    IzosdwanGvpnPricingAndFeasibilityService izosdwanGvpnPricingAndFeasibilityService;
    
    @Autowired
    IzosdwanPricingAndFeasibilityService izosdwanPricingAndFeasibilityService;
    
    /**
     * Method to save MF response In Site Feasible (both return & feasible)
     *
     * @param mfResponseDetailBeans
     */
	public void saveMfResponseInSiteFeasibile(List<MfResponseDetailBean> mfResponseDetailBeans) {

		LOGGER.info("Total number of responses in saveMfResponseInSiteFeasibile : {}", mfResponseDetailBeans.size());
		/**
		 * @author AnandhiV Filtering Izosdwan quotes saperately here!!!!!
		 */
		List<Integer> izosdwanQuoteIds = saperateIzosdwanQuotesFromTheList(mfResponseDetailBeans);
		LOGGER.info("Total number of responses in saveMfResponseInSiteFeasibile : {}", mfResponseDetailBeans.size());
		LOGGER.info("Total number of SDWAN quotes : {}", izosdwanQuoteIds != null ? izosdwanQuoteIds.size() : 0);
		Map<Integer, List<MfResponseDetailBean>> mapMfResponseDetailsBySite = new HashMap<>();
		Map<Integer, List<MfResponseDetailBean>> mapMfResponseDetailsBySiteIzosdwan = new HashMap<>();
		if (izosdwanQuoteIds != null && !izosdwanQuoteIds.isEmpty()) {
			LOGGER.info("IZOSDWAN quotes present !!");
			List<MfResponseDetailBean> izosdwanEntries = new ArrayList<>();
			List<MfResponseDetailBean> nonIzosdwanEntries = new ArrayList<>();
			mfResponseDetailBeans.stream().forEach(bean -> {
				Integer quoteId = izosdwanQuoteIds.stream().filter(id -> id.equals(bean.getQuoteId())).findFirst()
						.orElse(null);
				if (quoteId != null) {
					izosdwanEntries.add(bean);
				} else {
					nonIzosdwanEntries.add(bean);
				}
			});
			if (izosdwanEntries != null && !izosdwanEntries.isEmpty()) {
				LOGGER.info("Processing IZOSDWAN sites!!");
				mapMfResponseDetailsBySiteIzosdwan = izosdwanEntries.stream()
						.collect(Collectors.groupingBy(mfResponseDetailBean -> mfResponseDetailBean.getSiteId()));
				processManualFeasibilityResponseForIzosdwan(mapMfResponseDetailsBySiteIzosdwan, izosdwanQuoteIds);
				LOGGER.info("Trigger pricing IZOSDWAN for manual feasible sites with {} responses ",
						mfResponseDetailBeans.size());

				List<MfResponseDetailBean> responseForPricing = izosdwanEntries.stream().filter(x -> x.isHitPrice())
						.collect(Collectors.toList());
				if (responseForPricing != null && !responseForPricing.isEmpty()) {
					processPricingRequestForManualFeasibleSitesIzosdwan(responseForPricing);
				}

			}
			if (nonIzosdwanEntries != null && !nonIzosdwanEntries.isEmpty()) {
				LOGGER.info("Got Non IZOSDWAN quotes!!!");
				mapMfResponseDetailsBySite = nonIzosdwanEntries.stream()
						.collect(Collectors.groupingBy(mfResponseDetailBean -> mfResponseDetailBean.getSiteId()));
			}
		} else {
			LOGGER.info("Only Non-IZOSDWAN sites present!!");
			mapMfResponseDetailsBySite = mfResponseDetailBeans.stream()
					.collect(Collectors.groupingBy(mfResponseDetailBean -> mfResponseDetailBean.getSiteId()));
		}
		mapMfResponseDetailsBySite.entrySet().forEach(siteResponseDetail -> {

			List<MfResponseDetailBean> siteResponses = siteResponseDetail.getValue();
			Integer siteId = siteResponseDetail.getKey();

			if (siteResponses.get(0).getFeasibilityStatus().equalsIgnoreCase(RETURNED)) {
				setStatusForQuoteIllSite(siteResponseDetail.getKey(), RETURNED);
			} else if (!siteResponses.get(0).isHitPrice()) {
				// Hit price enabled only for feasible sites.
				setStatusForQuoteIllSite(siteResponseDetail.getKey(), NOT_FEASIBLE);

				try {
					LOGGER.info("Updating Is not feasible  site property for site {} ", siteId);
					UpdateRequest request = new UpdateRequest();
					List<AttributeDetail> attributeDetails = new ArrayList<>();
					AttributeDetail attributeDetail = new AttributeDetail();
					attributeDetail.setName(QuoteConstants.IS_NOT_FEASIBLE.toString());
					attributeDetail.setValue("true");
					attributeDetails.add(attributeDetail);
					request.setAttributeDetails(attributeDetails);
					request.setFamilyName(siteResponses.get(0).getProduct());
					// request.setAttributeName(QuoteConstants.IS_NOT_FEASIBLE.toString());
					// request.setAttributeValue("true");
					request.setSiteId(siteId);
					illQuoteService.updateSitePropertiesAttributes(request);
				} catch (TclCommonException e) {
					LOGGER.info("Error while updating site property for site : {}", siteId);
				}
			} else {
				
				// feasible 
                QuoteIllSite quoteIllSite = getQuoteIllSiteAndUpdateItsStatus(siteResponses, siteId);
				LOGGER.info("quoteIllSite = {}", quoteIllSite);
                siteResponses.forEach(mfResponseDetailBean -> constructAndSaveSiteFeasibilityFromMfResponse(mfResponseDetailBean, quoteIllSite));
            }
		});

		LOGGER.info("Trigger pricing for manual feasible sites with {} responses ", mfResponseDetailBeans.size());

		List<MfResponseDetailBean> responseForPricing = mfResponseDetailBeans.stream().filter(x -> x.isHitPrice())
				.collect(Collectors.toList());
		if (responseForPricing != null && !responseForPricing.isEmpty()) {
			processPricingRequestForManualFeasibleSites(responseForPricing);
		}

	}
    
    /**
     * Method to get quote ill site by site id and update its status to manual feasible
     *
     * @param siteResponses
     * @param siteId
     * @return {@link QuoteIllSite}
     */
    private QuoteIllSite getQuoteIllSiteAndUpdateItsStatus(List<MfResponseDetailBean> siteResponses, Integer siteId) {
		LOGGER.info("Inside getQuoteIllSiteAndUpdateItsStatus with siteId {}", siteId);
        QuoteIllSite quoteIllSite = illSiteRepository.findById(siteId).get();
        quoteIllSite.setMfStatus(MANUAL_FEASIBLE);
        if (siteResponses.stream().filter(mfResponseDetailBean -> mfResponseDetailBean.getIsSelected().equals(1)).count() > 0) {
			LOGGER.info("Inside if condition with siteId {}", siteId);
            quoteIllSite.setFpStatus(MF);
            quoteIllSite.setFeasibility((byte) 1);
            quoteIllSite.setMfTaskTriggered(0);
        }
        return illSiteRepository.save(quoteIllSite);
    }

    /**
     * check if response is of return site.
     * Return site will have only one response with id null and site id having value
     *
     * @param siteResponses
     * @return {@link boolean}
     */
    private boolean checkIfResponseStatusIsReturnOrNf(List<MfResponseDetailBean> siteResponses) {
        MfResponseDetailBean randomResponse = siteResponses.stream().findFirst().get();
        if(Objects.isNull(randomResponse.getId()) && Objects.nonNull(randomResponse.getSiteId())){
            return true;
        }
        return false;
    }

    /**
     * Process Pricing Request for manual feasible sites
     *
     * @param mfResponseDetailBeans
     */
    @Transactional
    public void processPricingRequestForManualFeasibleSites(List<MfResponseDetailBean> mfResponseDetailBeans) {
    	LOGGER.info("Inside ManualSiteFeasibleService.processPricingRequestForManualFeasibleSites to process selected response");
    	String product = mfResponseDetailBeans.stream().findFirst().get().getProduct();
    	LOGGER.info("Product name ========================== {} ", product);
        Integer quoteId = mfResponseDetailBeans.stream().findFirst().get().getQuoteId();
        List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
        Integer quoteToLeId = quoteToLes.stream().findFirst().get().getId();
        try {
        	mfResponseDetailBeans.stream().forEach(mf->{
        		if(mf.getIsSelected() != null && mf.getIsSelected() == 1 ) {
        			JSONObject dataEnvelopeObj = null;
        			JSONParser jsonParser = new JSONParser();
        			LOGGER.info("Inside processPricingRequestForManualFeasibleSites to process selected response {} ", mf.getFraId());
    					try {
    						dataEnvelopeObj =  (JSONObject) jsonParser.parse(mf.getCreateResponseJson());
    						String updateFlag = null;
    						if(dataEnvelopeObj.get("lm_interface_change") != null) {
    							LOGGER.info("Inside processPricingRequestForManualFeasibleSites to check lm_interface_change {} ", dataEnvelopeObj.get("lm_interface_change"));
    							updateFlag = (String) dataEnvelopeObj.get("lm_interface_change");
    						}
    						if(updateFlag != null && updateFlag.equalsIgnoreCase("Yes")) { 
    							if (dataEnvelopeObj.get("local_loop_interface") != null) {
    								LOGGER.info("Inside processPricingRequestForManualFeasibleSites to check local_loop_interface {} ", dataEnvelopeObj.get("local_loop_interface"));
    								LOGGER.info("Checking the product solution in the response. quotetype_quote:: {} and product_solution:: {}",dataEnvelopeObj.get("quotetype_quote"),
    										dataEnvelopeObj.get("product_solution"));
    	        					String localLoopInterface =  (String) dataEnvelopeObj.get("local_loop_interface");
    	        					String quoteTypeQuote =  (String) dataEnvelopeObj.get("quotetype_quote");
    	        					String productSolution =  (String) dataEnvelopeObj.get("product_solution");
    	        					List<QuoteProductComponent> quoteComponents  =null;
								if (productSolution != null && quoteTypeQuote != null
										&& productSolution.equalsIgnoreCase("GVPN")
										&& (quoteTypeQuote.equalsIgnoreCase("New Order") ||quoteTypeQuote.equalsIgnoreCase("MACD"))) {
									// for GVPN macd interface attr component type is VPN PORT 
									LOGGER.info("Inside GVPN - VPN Port is picked up for LocalLoopInterface {} quoteType {} ProductSolution {}",localLoopInterface,quoteTypeQuote,productSolution );
									quoteComponents = quoteProductComponentRepository
											.findByReferenceIdAndMstProductComponent_NameAndType(mf.getSiteId(),
													"VPN Port", mf.getType());
								} else {
									LOGGER.info("Inside ILL - Internet Port is picked up for LocalLoopInterface {} quoteType {} ProductSolution {}",localLoopInterface,quoteTypeQuote,productSolution );
									quoteComponents = quoteProductComponentRepository
											.findByReferenceIdAndMstProductComponent_NameAndType(mf.getSiteId(),
													"Internet Port", mf.getType());
								}

								if (quoteComponents != null && !quoteComponents.isEmpty()) {
									quoteComponents.stream().forEach(components -> {
										List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository
												.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
														components.getId(), FPConstants.INTERFACE.toString());
										QuoteProductComponentsAttributeValue quoteAttrValues = attributeValue.get(0);
										quoteAttrValues.setAttributeValues(localLoopInterface);
										quoteProductComponentsAttributeValueRepository.save(quoteAttrValues);
									});
								}
							}
    	        				if(dataEnvelopeObj.get("local_loop_bw") != null) {
    	        					LOGGER.info("Inside processPricingRequestForManualFeasibleSites to check local_loop_bw {} ", dataEnvelopeObj.get("local_loop_bw"));
    	        					//Double localLoopBw =  getCharges(dataEnvelopeObj.get(ManualFeasibilityConstants.LOCAL_LOOP_BW));
    	        					
    	        					Object localLoopBw = String.valueOf(dataEnvelopeObj.get(ManualFeasibilityConstants.LOCAL_LOOP_BW));
    	        					List<QuoteProductComponent> quoteComponents = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(mf.getSiteId(), "Last mile",mf.getType());
    	        					quoteComponents.stream().forEach(components->{
    	        						List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(components.getId(), FPConstants.LOCAL_LOOP_BW.toString());
    	        						QuoteProductComponentsAttributeValue quoteAttrValues = attributeValue.get(0);
    	        						quoteAttrValues.setAttributeValues(String.valueOf(localLoopBw));
    	        						quoteProductComponentsAttributeValueRepository.save(quoteAttrValues);
    	        					}); 
    	        				} 
    						}     				
    					} catch (ParseException e) {
    						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
    					}		
            	}
        		});
            LOGGER.info("Trigger pricing for quoteId {} and quoteLeId {} product {}", quoteId, quoteToLeId, product);
			if (StringUtils.isNotEmpty(product)) {
				switch (product.toUpperCase()) {
				case "IAS": 
					LOGGER.info("Trigger pricing for IAS");
					illPricingFeasibilityService.processPricingRequest(quoteId, quoteToLeId);
					break;
					
				case "IZO INTERNET WAN": 
					LOGGER.info("Trigger pricing for IWAN");
					iwanPricingFeasibilityService.processPricingRequest(quoteId, quoteToLeId);
					break;
				
				case "GVPN": 
					LOGGER.info("Trigger pricing for GVPN");
					
					gvpnPricingFeasibilityService.processPricingRequest(quoteId, quoteToLeId);
					break;
				}
			}
            
        } catch (TclCommonException e) {
            throw new TclCommonRuntimeException(ExceptionConstants.PRICING_FAILURE, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
    }

    
    private Double getCharges(Object charge) {
		Double mfCharge = 0.0D;
		if (charge != null) {
			if (charge instanceof Double) {
				mfCharge = (Double) charge;
			} else if (charge instanceof String && StringUtils.isNotEmpty((String)charge)) {
				mfCharge = new Double((String) charge);
			} else if (charge instanceof Long) {
				mfCharge = new Double((Long) charge);
			} else if (charge instanceof Integer) {
				mfCharge = new Double((Integer) charge);
			}
		}
		return mfCharge;
	}
    /**
     * Construct Site Feasible From MF Response
     *
     * @param mfResponseDetailBean
     * @return {@link SiteFeasibility}
     */
    private SiteFeasibility constructAndSaveSiteFeasibilityFromMfResponse(MfResponseDetailBean mfResponseDetailBean, QuoteIllSite quoteIllSite){
        LOGGER.info("Construct SiteFeasibility Response for MfResponseDetailId {}", mfResponseDetailBean.getId());
        SiteFeasibility siteFeasibility = new SiteFeasibility();
        siteFeasibility.setFeasibilityMode(mfResponseDetailBean.getFeasibilityMode());
        siteFeasibility.setType(mfResponseDetailBean.getType());
        siteFeasibility.setProvider(mfResponseDetailBean.getProvider());
        siteFeasibility.setRank(mfResponseDetailBean.getMfRank());
        siteFeasibility.setIsSelected(mfResponseDetailBean.getIsSelected().byteValue());
        siteFeasibility.setResponseJson(mfResponseDetailBean.getCreateResponseJson());
        siteFeasibility.setCreatedTime(mfResponseDetailBean.getCreatedTime());
        siteFeasibility.setFeasibilityType(mfResponseDetailBean.getFeasibilityType());
        siteFeasibility.setFeasibilityCheck(mfResponseDetailBean.getFeasibilityCheck());
        siteFeasibility.setFeasibilityCode(Utils.generateUid());
        siteFeasibility.setQuoteIllSite(quoteIllSite);
        
        JSONObject dataEnvelopeObj = null;
		JSONParser jsonParser = new JSONParser();
		try {
			dataEnvelopeObj = (JSONObject) jsonParser.parse(mfResponseDetailBean.getCreateResponseJson());
		} catch (ParseException e) {
			LOGGER.info("Exception in parsing mfResponse while fetching sfdc_feasibility_id..");
		}

		LOGGER.info("dataEnvelopeObj {}",dataEnvelopeObj.toString());
		if (dataEnvelopeObj.get("sfdc_feasibility_id") != null && !dataEnvelopeObj.get("sfdc_feasibility_id").equals("")) {
			siteFeasibility.setSfdcFeasibilityId((String)dataEnvelopeObj.get("sfdc_feasibility_id"));
		}

        siteFeasibilityRepository.save(siteFeasibility);
        return siteFeasibility;
    }

    /**
     * Method to set return status
     *
     * @param siteId
     * @return
     */
    private QuoteIllSite setStatusForQuoteIllSite(Integer siteId,String status) {
        LOGGER.info("Setting status for site id {}", siteId);
        QuoteIllSite quoteIllSite = illSiteRepository.findById(siteId).get();
        quoteIllSite.setMfStatus(status);
        return illSiteRepository.save(quoteIllSite);
    }
    
    /**
     * Method to set return status in quotenpl link
     *
     * @param siteId
     * @return
     */
    private QuoteNplLink setStatusForQuoteNPLLink(Integer linkId,String status) {
        LOGGER.info("Setting status not feasible or return for link id {}", linkId);
        QuoteNplLink quoteNplLink = nplLinkRepository.findById(linkId).get();
		if (quoteNplLink != null) {
			quoteNplLink.setMfStatus(status);
				quoteNplLink.setFeasibility((byte) 0);
				quoteNplLink.setMfTaskTriggered(0);
			
		}
    	
        return nplLinkRepository.save(quoteNplLink);
    }
    
    
    /**
     * Method to save site status in preMf Request table 
     * @param listOfSitesAndItsStatus
     */
	public void saveMfResponseInPreMfRequest(List<Mf3DSiteStatus> listOfSitesAndItsStatus) {

		if (CollectionUtils.isNotEmpty(listOfSitesAndItsStatus)) {

			listOfSitesAndItsStatus.stream().forEach(x -> {
				Optional<PreMfRequest> dbEntry = preMfRequestRepository.findById(x.getSiteId());

				if (dbEntry.isPresent()) {
					LOGGER.info("Setting status for site id {}", x.getSiteId());
					dbEntry.get().setFeasibility(x.getFeasibility().byteValue());
					dbEntry.get().setStatus(x.getSiteStatus());
					preMfRequestRepository.save(dbEntry.get());
				} else {
					LOGGER.info("No such siteID : {} available in preMFRequestTable", x.getSiteId());
				}

			});
		}

	}

	/**
	 * saveMfNplResponseInLinkFeasible
	 * 
	 * @param mfLinkResponseDetailBeans
	 * @throws TclCommonException 
	 */
	@Transactional
	public void saveMfNplResponseInLinkFeasible(List<MfNplResponseDetailBean> mfLinkResponseDetailBeans) throws TclCommonException {

		LOGGER.info("Total number of responses in saveMfResponseInLinkFeasibile : {}",
				mfLinkResponseDetailBeans.size());
		if (!CollectionUtils.isEmpty(mfLinkResponseDetailBeans)) {

			 Integer quoteId = mfLinkResponseDetailBeans.stream().findFirst().get().getQuoteId();
		        List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
		        Integer quoteToLeId = quoteToLes.stream().findFirst().get().getId();
			mfLinkResponseDetailBeans.forEach(linkResponse -> {

				if (linkResponse.getFeasibilityStatus().equalsIgnoreCase(RETURNED)) {
					setStatusForQuoteNPLLink(linkResponse.getLinkId(), RETURNED);

					if (linkResponse.getReturnedSiteType() != null) {
						QuoteIllSite siteDetail = null;
						Optional<QuoteNplLink> quoteNplLinkObj = nplLinkRepository.findById(linkResponse.getLinkId());
						if (quoteNplLinkObj.isPresent() && linkResponse.getReturnedSiteType().equalsIgnoreCase("SiteA")) {
							siteDetail = illSiteRepository.findById(quoteNplLinkObj.get().getSiteAId()).get();
							siteDetail.setMfStatus(linkResponse.getFeasibilityStatus());
						}
						
						else if (quoteNplLinkObj.isPresent() && linkResponse.getReturnedSiteType().equalsIgnoreCase("SiteB")) {
							siteDetail = illSiteRepository.findById(quoteNplLinkObj.get().getSiteBId()).get();
							siteDetail.setMfStatus(linkResponse.getFeasibilityStatus());
					}
						illSiteRepository.save(siteDetail);
						
					}

				} else if (linkResponse.getFeasibilityStatus().equalsIgnoreCase(NOT_FEASIBLE)) {
					setStatusForQuoteNPLLink(linkResponse.getLinkId(), NOT_FEASIBLE);
					
					LOGGER.info("Updating Is not feasible  site property for site {} ", linkResponse.getLinkId());
					UpdateRequest request = new UpdateRequest();
					List<AttributeDetail> attributeDetails = new ArrayList<>();
					AttributeDetail attributeDetail = new AttributeDetail();
					attributeDetail.setName(QuoteConstants.IS_NOT_FEASIBLE.toString());
					attributeDetail.setValue("true");
					attributeDetails.add(attributeDetail);
					request.setAttributeDetails(attributeDetails);
					request.setFamilyName("NPL");
					request.setLinkId(linkResponse.getLinkId());
				} else {
					// feasible scenario
					QuoteNplLink quoteNplLink = getQuoteNplLinkAndUpdateItsStatus(linkResponse);
					constructAndSaveLinkFeasibilityFromMfNplResponse(linkResponse, quoteNplLink);

				}

			});
			
			// update Quote components which has been changed while creating MF response.
			
			if (mfLinkResponseDetailBeans != null && !mfLinkResponseDetailBeans.isEmpty()) {
				Optional<MfNplResponseDetailBean> selectedResp = mfLinkResponseDetailBeans.stream()
						.filter(x ->( x.getIsSelected()!=null &&  x.getIsSelected() == 1)).findFirst();
				if ( selectedResp.isPresent() ) {
					LOGGER.info("Inside pricing Hit for quoteId ####{}  ", quoteId);
					updateComponents(mfLinkResponseDetailBeans);
					nplPricingFeasibilityService.processPricingRequest(quoteId, quoteToLeId);

				}
			}
		}
	}

	 @Transactional
	private void updateComponents(List<MfNplResponseDetailBean> mfLinkResponseDetailBeans) {
		mfLinkResponseDetailBeans.stream().forEach(mf -> {
			if (mf.getIsSelected() != null && mf.getIsSelected() == 1) {
				JSONParser jsonParser = new JSONParser();
				LOGGER.info("Inside update component values which is changed in MF {} ", mf.getFraId());
				try {

					List<QuoteProductComponent> quoteComponents = new ArrayList<QuoteProductComponent>();

					QuoteNplLink quoteNplLink = nplLinkRepository.findById(mf.getLinkId()).get();

					Arrays.asList(quoteNplLink.getSiteAId(), quoteNplLink.getSiteBId()).stream().forEach(siteId -> {

						if (siteId != null) {
							List<QuoteProductComponent> productComponentsForNplSites = quoteProductComponentRepository
									.findByReferenceIdAndReferenceName(siteId, QuoteConstants.NPL_SITES.toString());
							quoteComponents.addAll(productComponentsForNplSites);
						}
					});
					List<QuoteProductComponent> productComponentsForNplLinks = null;
					productComponentsForNplLinks = quoteProductComponentRepository
							.findByReferenceIdAndReferenceName(mf.getLinkId(), QuoteConstants.NPL_LINK.toString());

					quoteComponents.addAll(productComponentsForNplLinks);
					List<String> listOfAttrs = Arrays.asList("Interface Type - A end", "Interface Type - B end",
							"Local Loop Bandwidth");

					if (quoteComponents != null && !quoteComponents.isEmpty()) {
						JSONObject dataEnvelopeObj = (JSONObject) jsonParser.parse(mf.getCreateResponseJson());

						LOGGER.info(
								"Inside processPricingRequestForManualFeasibleSites to check local_loop_bandwidth AEnd {} and BEnd{} ",
								dataEnvelopeObj.get("a_local_loop_bw"), dataEnvelopeObj.get("b_local_loop_bw"));

						LOGGER.info(
								"Inside processPricingRequestForManualFeasibleSites to check local_loop_interface AEnd {} and BEnd{} ",
								dataEnvelopeObj.get("a_local_loop_interface"),
								dataEnvelopeObj.get("b_local_loop_interface"));

						quoteComponents.forEach(x -> {
							listOfAttrs.forEach(z -> {
								List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
										.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(x.getId(), z);

								attributes.forEach(y -> {

									if (y.getProductAttributeMaster() != null
											&& y.getProductAttributeMaster().getName() != null) {
										if (y.getProductAttributeMaster().getName().equals("Interface Type - A end")
												&& dataEnvelopeObj.get("a_local_loop_interface") != null) {
											LOGGER.info("Inside a_local_loop_interface");

											String aEndLocalLoopInterface = (String) dataEnvelopeObj
													.get("a_local_loop_interface");

											if (aEndLocalLoopInterface != null && !aEndLocalLoopInterface.equals("null")
													&& !y.getAttributeValues().equals(aEndLocalLoopInterface)) {
												y.setAttributeValues(aEndLocalLoopInterface);
											}

										}

										if (y.getProductAttributeMaster().getName().equals("Interface Type - B end")
												&& dataEnvelopeObj.get("b_local_loop_interface") != null) {
											LOGGER.info("Inside b_local_loop_interface");
											String bEndLocalLoopInterface = (String) dataEnvelopeObj
													.get("b_local_loop_interface");

											if (bEndLocalLoopInterface != null && !bEndLocalLoopInterface.equals("null")
													&& !y.getAttributeValues().equals(bEndLocalLoopInterface)) {

												y.setAttributeValues(bEndLocalLoopInterface);
											}
										}

										if (y.getProductAttributeMaster() != null
												&& y.getProductAttributeMaster().getName() != null
												&& y.getProductAttributeMaster().getName()
														.equalsIgnoreCase("Local Loop Bandwidth")) {

											if (x.getType().equals(NplPDFConstants.SITE_A)
													&& dataEnvelopeObj.get("a_local_loop_bw") != null) {

												String aEndLocalLoopBandwidth = String
														.valueOf(dataEnvelopeObj.get("a_local_loop_bw"));

												if (aEndLocalLoopBandwidth != null
														&& !aEndLocalLoopBandwidth.equals("null")
														&& !y.getAttributeValues().equals(aEndLocalLoopBandwidth)) {
													LOGGER.info(
															"Inside AEnd LLB condition  aEndLocalLoopBandwidth is {}",
															aEndLocalLoopBandwidth);

													y.setAttributeValues(aEndLocalLoopBandwidth);
												}
											}
											if (x.getType().equals(NplPDFConstants.SITE_B)
													&& dataEnvelopeObj.get("b_local_loop_bw") != null) {
												String bEndLocalLoopBandwidth = String
														.valueOf(dataEnvelopeObj.get("b_local_loop_bw"));

												if (bEndLocalLoopBandwidth != null
														&& !bEndLocalLoopBandwidth.equals("null")
														&& !y.getAttributeValues().equals(bEndLocalLoopBandwidth)) {
													LOGGER.info(
															"Inside BEnd LLB condition  bEndLocalLoopBandwidth is {}",
															bEndLocalLoopBandwidth);

													y.setAttributeValues(bEndLocalLoopBandwidth);
												}
											}
										}
										quoteProductComponentsAttributeValueRepository.save(y);
									}
								});

							});
						});
					}

				} catch (ParseException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}
			}
		});
	}
	 
	    /**
	     * Method to get quote NPL site by link id and update its status to manual feasible
	     *
	     * @param LinkResponses
	     * @param LinkId
	     * @return {@link QuoteNplLink}
	     */
	    private QuoteNplLink getQuoteNplLinkAndUpdateItsStatus(MfNplResponseDetailBean linkResponse) {
	    	LOGGER.info("linkId {}",linkResponse.getLinkId());
	    	Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(linkResponse.getLinkId());
	    	if(linkResponse.getFeasibilityStatus().startsWith(FEASIBLE) && quoteNplLink.isPresent()) {
	    	quoteNplLink.get().setMfStatus(MANUAL_FEASIBLE);
	        	quoteNplLink.get().setFpStatus(MF);
	        	
	        	quoteNplLink.get().setFeasibility((byte) 1);
	        	quoteNplLink.get().setMfTaskTriggered(0);
	    	}
	        return nplLinkRepository.save(quoteNplLink.get());
	    }

	/**
	 * Metbod to construct and save link Feasibility for NPL
	 * @param mfnplResponseDetailBean
	 * @param quoteNplLink
	 * @return
	 */
	private LinkFeasibility constructAndSaveLinkFeasibilityFromMfNplResponse(
			MfNplResponseDetailBean mfnplResponseDetailBean, QuoteNplLink quoteNplLink) {
		LOGGER.info("Construct LinkFeasibility Response for MfNplResponseDetailId {}", mfnplResponseDetailBean.getId());
		Optional<QuoteNplLink> linkId = nplLinkRepository.findById(mfnplResponseDetailBean.getLinkId());
		if (!linkId.isPresent()) {
			LOGGER.error("Link ID {} not found in link Feasibility table", mfnplResponseDetailBean.getLinkId());
		}
		Optional<QuoteNplLink> quoteNplLinkObj = nplLinkRepository.findById(mfnplResponseDetailBean.getLinkId());
		LinkFeasibility linkFeasibility = new LinkFeasibility();
		linkFeasibility.setFeasibilityMode(mfnplResponseDetailBean.getFeasibilityMode());
		linkFeasibility.setFeasibilityModeB(mfnplResponseDetailBean.getFeasibilityModeB());

		linkFeasibility.setQuoteNplLink(quoteNplLinkObj.get());
		linkFeasibility.setType(mfnplResponseDetailBean.getType());
		linkFeasibility.setProvider(mfnplResponseDetailBean.getProvider());
		linkFeasibility.setProviderB(mfnplResponseDetailBean.getProviderB());

		linkFeasibility.setRank(mfnplResponseDetailBean.getMfRank());
		linkFeasibility.setIsSelected(mfnplResponseDetailBean.getIsSelected().byteValue());
		linkFeasibility.setResponseJson(mfnplResponseDetailBean.getCreateResponseJson());
		linkFeasibility.setCreatedTime(mfnplResponseDetailBean.getCreatedTime());
		linkFeasibility.setFeasibilityType(mfnplResponseDetailBean.getFeasibilityType());
		linkFeasibility.setFeasibilityTypeB(mfnplResponseDetailBean.getFeasibilityTypeB());

		linkFeasibility.setFeasibilityCheck(mfnplResponseDetailBean.getFeasibilityCheck());
		linkFeasibility.setFeasibilityCode(Utils.generateUid());
		linkFeasibility.setResponseJson(mfnplResponseDetailBean.getCreateResponseJson());
		linkFeasibility.setQuoteNplLink(quoteNplLink);
		//ADDED for discount delegation
		LOGGER.info("MF SELECTED STATUS"+mfnplResponseDetailBean.getIsSelected().byteValue());
		if (mfnplResponseDetailBean.getIsSelected().byteValue() == 1) {
			String feasibleLinkResponse = mfnplResponseDetailBean.getCreateResponseJson();
			Feasible sitef = new Feasible();
			try {
				sitef = (Feasible) Utils.convertJsonToObject(feasibleLinkResponse, Feasible.class);
			} catch (TclCommonException e) {
				LOGGER.info("EXCEPTION IN BEAN conversion Feasibile" + e);
			}

			// added for discount cal
			int cd = 0;
			/*
			 * if(Objects.nonNull(sitef.getAPOPDISTKMSERVICEMOD())) {
			 * LOGGER.info("update cd distance for discount cal for A END feasible"+sitef.
			 * getAPOPDISTKMSERVICEMOD()); if(sitef.getAPOPDISTKMSERVICEMOD()!=null) {
			 * cd=Integer.parseInt(sitef.getAPOPDISTKMSERVICEMOD()); }
			 * linkFeasibility.setCdDistance(cd); }
			 * if(Objects.nonNull(sitef.getBPOPDISTKMSERVICEMOD())) {
			 * LOGGER.info("update cd distance for discount cal for B end feasible"+sitef.
			 * getBPOPDISTKMSERVICEMOD()); if(sitef.getBPOPDISTKMSERVICEMOD()!=null) {
			 * cd=Integer.parseInt(sitef.getBPOPDISTKMSERVICEMOD()); }
			 * linkFeasibility.setCdDistance(cd); }
			 */
			if (Objects.nonNull(sitef.getChargeableDistance())) {
				LOGGER.info("update cd distance for discount cal for A END feasible" + sitef.getChargeableDistance());
				if (sitef.getChargeableDistance() != null) {
					cd = Integer.parseInt(sitef.getChargeableDistance());
				}
				linkFeasibility.setCdDistance(cd);

			}
		}
			else {
				LOGGER.info("update cd distance for discount cal for A END not feasible");
				//needs to set not feasible later
				linkFeasibility.setCdDistance(0);
			}
			
		
		linkFeasibilityRepository.save(linkFeasibility);

		return linkFeasibility;
	}
	
	private List<Integer> saperateIzosdwanQuotesFromTheList(List<MfResponseDetailBean> mfResponseDetailBeans) {
		List<Integer> izosdwanQuoteIds = new ArrayList<>();
		try {
			if (mfResponseDetailBeans != null && !mfResponseDetailBeans.isEmpty()) {
				List<Integer> quoteIds = mfResponseDetailBeans.stream().filter(bean -> bean.getQuoteId() != null)
						.map(bean -> bean.getQuoteId()).collect(Collectors.toList());
				quoteIds.stream().forEach(id -> {
					Quote quote = quoteRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
					if (quote != null && quote.getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
						izosdwanQuoteIds.add(id);
					}
				});
			}
		} catch (Exception e) {
			LOGGER.error("Error in finding IZOSDWAN quote Ids", e);
		}
		return izosdwanQuoteIds;
	}
	
	private void processManualFeasibilityResponseForIzosdwan(
			Map<Integer, List<MfResponseDetailBean>> mapMfResponseDetailsBySite,List<Integer> quoteIds) {
		LOGGER.info("Processing Manual feasibility response for IZOSDWAN ");
		mapMfResponseDetailsBySite.entrySet().forEach(siteResponseDetail -> {

			List<MfResponseDetailBean> siteResponses = siteResponseDetail.getValue();
			Integer siteId = siteResponseDetail.getKey();

			if (siteResponses.get(0).getFeasibilityStatus().equalsIgnoreCase(RETURNED)) {
				setStatusForQuoteIllSite(siteResponseDetail.getKey(), RETURNED);
			} /*
				 * else if (!siteResponses.get(0).isHitPrice()) { // Hit price enabled only for
				 * feasible sites. setStatusForQuoteIzosdwanSite(siteResponseDetail.getKey(),
				 * NOT_FEASIBLE);
				 * 
				 * try { LOGGER.info("Updating Is not feasible  site property for site {} ",
				 * siteId); UpdateRequest request = new UpdateRequest(); List<AttributeDetail>
				 * attributeDetails = new ArrayList<>(); AttributeDetail attributeDetail = new
				 * AttributeDetail();
				 * attributeDetail.setName(QuoteConstants.IS_NOT_FEASIBLE.toString());
				 * attributeDetail.setValue("true"); attributeDetails.add(attributeDetail);
				 * request.setAttributeDetails(attributeDetails);
				 * request.setFamilyName(IzosdwanCommonConstants.IZOSDWAN_NAME); //
				 * request.setAttributeName(QuoteConstants.IS_NOT_FEASIBLE.toString()); //
				 * request.setAttributeValue("true"); request.setSiteId(siteId);
				 * izosdwanQuoteService.updateSitePropertiesAttributes(request); } catch
				 * (TclCommonException e) {
				 * LOGGER.info("Error while updating site property for site : {}", siteId); } }
				 */ else {
				QuoteIzosdwanSite quoteIzosdwanSite = getQuoteIzosdwanSiteAndUpdateItsStatus(siteResponses, siteId);
				siteResponses.forEach(mfResponseDetailBean -> constructAndSaveIzosdwanSiteFeasibilityFromMfResponse(
						mfResponseDetailBean, quoteIzosdwanSite));
			}

		});
	}

	private QuoteIzosdwanSite setStatusForQuoteIzosdwanSite(Integer siteId, String status) {
		LOGGER.info("Setting status for site id {}", siteId);
		QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSiteRepository.findById(siteId).get();
		quoteIzosdwanSite.setIsFeasiblityCheckRequired(CommonConstants.INACTIVE);
		quoteIzosdwanSite.setMfStatus(status);
		return quoteIzosdwanSiteRepository.save(quoteIzosdwanSite);
	}

	private QuoteIzosdwanSite getQuoteIzosdwanSiteAndUpdateItsStatus(List<MfResponseDetailBean> siteResponses,
			Integer siteId) {
		QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSiteRepository.findById(siteId).get();
		quoteIzosdwanSite.setMfStatus(MANUAL_FEASIBLE);
		if (siteResponses.stream().filter(mfResponseDetailBean -> mfResponseDetailBean.getIsSelected().equals(1))
				.count() > 0) {
			quoteIzosdwanSite.setFpStatus(MF);
			quoteIzosdwanSite.setFeasibility((byte) 1);
			quoteIzosdwanSite.setMfTaskTriggered(0);
		}
		return quoteIzosdwanSiteRepository.save(quoteIzosdwanSite);
	}

	private IzosdwanSiteFeasibility constructAndSaveIzosdwanSiteFeasibilityFromMfResponse(
			MfResponseDetailBean mfResponseDetailBean, QuoteIzosdwanSite quoteIzosdwanSite) {
		LOGGER.info("Construct SiteFeasibility Response for MfResponseDetailId {}", mfResponseDetailBean.getId());
		IzosdwanSiteFeasibility siteFeasibility = new IzosdwanSiteFeasibility();
		siteFeasibility.setFeasibilityMode(mfResponseDetailBean.getFeasibilityMode());
		siteFeasibility.setType(mfResponseDetailBean.getType());
		siteFeasibility.setProvider(mfResponseDetailBean.getProvider());
		siteFeasibility.setRank(mfResponseDetailBean.getMfRank());
		siteFeasibility.setIsSelected(mfResponseDetailBean.getIsSelected().byteValue());
		siteFeasibility.setResponseJson(mfResponseDetailBean.getCreateResponseJson());
		siteFeasibility.setCreatedTime(mfResponseDetailBean.getCreatedTime());
		siteFeasibility.setFeasibilityType(mfResponseDetailBean.getFeasibilityType());
		siteFeasibility.setFeasibilityCheck(mfResponseDetailBean.getFeasibilityCheck());
		siteFeasibility.setFeasibilityCode(Utils.generateUid());
		siteFeasibility.setQuoteIzosdwanSite(quoteIzosdwanSite);

		JSONObject dataEnvelopeObj = null;
		JSONParser jsonParser = new JSONParser();
		try {
			dataEnvelopeObj = (JSONObject) jsonParser.parse(mfResponseDetailBean.getCreateResponseJson());
		} catch (ParseException e) {
			LOGGER.info("Exception in parsing mfResponse while fetching sfdc_feasibility_id..");
		}

		if (dataEnvelopeObj.get("sfdc_feasibility_id") != null
				&& !dataEnvelopeObj.get("sfdc_feasibility_id").equals("")) {
			siteFeasibility.setSfdcFeasibilityId((String) dataEnvelopeObj.get("sfdc_feasibility_id"));
		}

		izosdwanSiteFeasiblityRepository.save(siteFeasibility);
		return siteFeasibility;
	}
	
	@Transactional
	public void processPricingRequestForManualFeasibleSitesIzosdwan(List<MfResponseDetailBean> mfResponseDetailBeans) {
		LOGGER.info(
				"Inside ManualSiteFeasibleService.processPricingRequestForManualFeasibleSites to process selected response");
		String product = mfResponseDetailBeans.stream().findFirst().get().getProduct();
		LOGGER.info("Product name ========================== {} ", product);
		Integer quoteId = mfResponseDetailBeans.stream().findFirst().get().getQuoteId();
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
		Integer quoteToLeId = quoteToLes.stream().findFirst().get().getId();
		try {
			mfResponseDetailBeans.stream().forEach(mf -> {
				if (mf.getIsSelected() != null && mf.getIsSelected() == 1) {
					JSONObject dataEnvelopeObj = null;
					JSONParser jsonParser = new JSONParser();
					LOGGER.info("Inside processPricingRequestForManualFeasibleSites to process selected response {} ",
							mf.getFraId());
					try {
						dataEnvelopeObj = (JSONObject) jsonParser.parse(mf.getCreateResponseJson());
						String updateFlag = null;
						if (dataEnvelopeObj.get("lm_interface_change") != null) {
							LOGGER.info(
									"Inside processPricingRequestForManualFeasibleSites to check lm_interface_change {} ",
									dataEnvelopeObj.get("lm_interface_change"));
							updateFlag = (String) dataEnvelopeObj.get("lm_interface_change");
						}
						if (updateFlag != null && updateFlag.equalsIgnoreCase("Yes")) {
							if (dataEnvelopeObj.get("local_loop_interface") != null) {
								LOGGER.info(
										"Inside processPricingRequestForManualFeasibleSites to check local_loop_interface {} ",
										dataEnvelopeObj.get("local_loop_interface"));
								LOGGER.info(
										"Checking the product solution in the response. quotetype_quote:: {} and product_solution:: {}",
										dataEnvelopeObj.get("quotetype_quote"),
										dataEnvelopeObj.get("product_solution"));
								String localLoopInterface = (String) dataEnvelopeObj.get("local_loop_interface");
								String quoteTypeQuote = (String) dataEnvelopeObj.get("quotetype_quote");
								String productSolution = (String) dataEnvelopeObj.get("product_solution");
								List<QuoteProductComponent> quoteComponents = null;
								if (productSolution != null) {
									LOGGER.info(
											"Inside IZOSDWAN - SITE_PROP is picked up for LocalLoopInterface {} quoteType {} ProductSolution {}",
											localLoopInterface, quoteTypeQuote, productSolution);
									quoteComponents = quoteProductComponentRepository
											.findByReferenceIdAndMstProductComponent_NameAndType(mf.getSiteId(),
													IzosdwanCommonConstants.SITE_PROPERTIES, mf.getType());
								}

								if (quoteComponents != null && !quoteComponents.isEmpty()) {
									quoteComponents.stream().forEach(components -> {
										List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository
												.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
														components.getId(), FPConstants.INTERFACE.toString());
										QuoteProductComponentsAttributeValue quoteAttrValues = attributeValue.get(0);
										quoteAttrValues.setAttributeValues(localLoopInterface);
										quoteProductComponentsAttributeValueRepository.save(quoteAttrValues);
									});
								}
							}
							if (dataEnvelopeObj.get("local_loop_bw") != null) {
								LOGGER.info(
										"Inside processPricingRequestForManualFeasibleSites to check local_loop_bw {} ",
										dataEnvelopeObj.get("local_loop_bw"));
								Double localLoopBw = getCharges(
										dataEnvelopeObj.get(ManualFeasibilityConstants.LOCAL_LOOP_BW));
								List<QuoteProductComponent> quoteComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponent_NameAndType(mf.getSiteId(),
												IzosdwanCommonConstants.SITE_PROPERTIES, mf.getType());
								quoteComponents.stream().forEach(components -> {
									List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository
											.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
													components.getId(), FPConstants.LOCAL_LOOP_BW.toString());
									QuoteProductComponentsAttributeValue quoteAttrValues = attributeValue.get(0);
									quoteAttrValues.setAttributeValues(String.valueOf(localLoopBw));
									quoteProductComponentsAttributeValueRepository.save(quoteAttrValues);
								});
							}
						}
					} catch (ParseException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
					}
				}
			});
			LOGGER.info("Trigger pricing for quoteId {} and quoteLeId {}", quoteId, quoteToLeId);
			if (StringUtils.isNotEmpty(product)) {
				switch (product.toUpperCase()) {
				case "IAS":
					LOGGER.info("Trigger pricing for IAS from IZOSDWAN");
					izosdwanIllPricingAndFeasiblityService.processPricingRequestFromMf(quoteId, quoteToLeId);
					break;

				case "GVPN":
					LOGGER.info("Trigger pricing for GVPN from IZOSDWAN" );

					izosdwanGvpnPricingAndFeasibilityService.processPricingRequestFromMF(quoteId, quoteToLeId);
					break;

				}
			}
			izosdwanPricingAndFeasibilityService.putEntryInPricingBatch(quoteId, quoteToLeId, false, true,false,CommonConstants.STANDARD);

		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.PRICING_FAILURE, e,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
	}
}
