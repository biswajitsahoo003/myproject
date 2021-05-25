package com.tcl.dias.oms.gde.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.CreateTicketRequestBean;
import com.tcl.dias.common.beans.CreateTicketResponseBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.TicketContactBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.serviceinventory.beans.SiServiceSiContractInfoBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.BodBookingDetails;
import com.tcl.dias.oms.entity.entities.GdeScheduleDetails;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.OdrScheduleDetails;
import com.tcl.dias.oms.entity.entities.OdrScheduleDetailsAudit;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.BodBookingDetailsRepository;
import com.tcl.dias.oms.entity.repository.GdeScheduleDetailsRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OdrScheduleDetailsAuditRepository;
import com.tcl.dias.oms.entity.repository.OdrScheduleDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gde.beans.FeasibilityCheckResponse;
import com.tcl.dias.oms.gde.beans.GdeAuthRequestBean;
import com.tcl.dias.oms.gde.beans.GdeFeasibilityBwProfile;
import com.tcl.dias.oms.gde.beans.GdeFeasibilityInputBean;
import com.tcl.dias.oms.gde.beans.GdeFeasibilityRequest;
import com.tcl.dias.oms.gde.beans.GdeOdrScheduleAuditBean;
import com.tcl.dias.oms.gde.constants.GdeOrderConstants;
import com.tcl.dias.oms.gde.pdf.service.GdeQuotePdfService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class GdePricingFeasibilityService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GdePricingFeasibilityService.class);
	
	@Value("${gde.mdso.auth.token.url}")
	String authTokenEndPoint;
	
	@Value("${gde.mdso.auth.token.username}")
	String authTokenUserName;
	
	@Value("${gde.mdso.auth.token.password}")
	String authTokenPswd;
	
	@Value("${gde.mdso.auth.token.tenant}")
	String authTenant;
	
	@Value("${gde.mdso.resource.url}")
	String circuitLookUpEndPoint;
	
	@Value("${gde.mdso.feasibility.check.endpoint}")
	String feasibilityCheckEndPoint;
	
	@Autowired
	RestClientService restClientService;
	
	@Autowired
	protected QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	NplLinkRepository nplLinkRepository;
	
	@Autowired
	GdeScheduleDetailsRepository gdeScheduleDetailsRepository;
	
	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;
	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${rabbitmq.si.service.get.contract.info.queue}")
	String siServiceContractInfoQueue;
	
	@Autowired
	MstProductComponentRepository mstProductComponentRepository;
	
	@Autowired
	QuotePriceRepository quotePriceRepository;
	
	@Autowired
	OmsUtilService omsUtilService;
	
	@Autowired
	OdrScheduleDetailsRepository odrScheduleDetailsRepository;
	
	@Autowired
	OdrScheduleDetailsAuditRepository odrScheduleDetailsAuditRepository;
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	ProductSolutionRepository productSolutionRepository;
	
	@Value("${rabbitmq.ticket.create}")
	String ticketCreationQueue;
	
	@Autowired
	OrderToLeRepository orderToLeRepository;
	
	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	GdeQuotePdfService gdeQuotePdfService;
	
	@Autowired
	BodBookingDetailsRepository bodBookingDetailsRepository;
	
	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;
	
	/**
	 * MEthod 
	 * @param quoteToLeId
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public String processFeasibility(Integer quoteToLeId, Integer linkId) throws TclCommonException {
		/*check whether feasibility need to be triggered.
		 * If needed, get resource id from MDSO & for the resource check feasibility
		 * 
		 */
		String mdsoAck = CommonConstants.FAILIURE;
		try {
			LOGGER.info("Entering processFeasibility for quoteToLeId {} and link {} ",quoteToLeId, linkId);
			QuoteNplLink nplLink = nplLinkRepository.findByIdAndStatus(linkId, (byte) 1);
			if(nplLink != null) {
				LOGGER.info("Link data available for the link {} ", linkId);
				Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteToLeId);
				String resourceId = null;
				String circuitId = null;
				GdeScheduleDetails gdeSchedules = gdeScheduleDetailsRepository.findByQuoteCodeAndLinkId(quoteToLeEntity.get().getQuote().getQuoteCode(), linkId);				
				if(gdeSchedules != null) {
					LOGGER.info("gdeSchedules for linkId {} and serviceId {} ",gdeSchedules.getLinkId(), gdeSchedules.getServiceId());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					long diff = Math.abs(timestamp.getTime() - gdeSchedules.getFeasibilityValidity().getTime());
					long diffSeconds = diff / 1000;
					Map<String, Boolean> quoteUpdates = new HashMap<>();
					List<QuoteProductComponent> quoteComponents = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(nplLink.getId(), "GDE_BOD");
					List<QuoteProductComponentsAttributeValue> quoteAttributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(quoteComponents.get(0).getId()); 
					getUpdatedAttributes(gdeSchedules, quoteUpdates, quoteAttributes); 
					if(diffSeconds > 86400 || quoteUpdates.get(CommonConstants.UPGRADED_BANDWIDTH) 
							|| quoteUpdates.get(CommonConstants.BOD_SCHEDULE_END_DATE) || quoteUpdates.get(CommonConstants.BOD_SCHEDULE_END_DATE)) {
						LOGGER.info("checking feasibility - Quote components changed/ feasibility expired for quoteLe {} and linkId {}",quoteToLeId, linkId);
						saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
								FPConstants.FEASIBILITY.toString(), FPConstants.FALSE.toString());// disable the feasible flag
						saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
								FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
						
						List<LinkFeasibility> linkFeasibiityList = linkFeasibilityRepository.findByQuoteNplLink_Id(nplLink.getId());
						LinkFeasibility linkFeasibility = linkFeasibiityList.get(0);	
						GdeFeasibilityRequest gdeFeasibilityRequest = new GdeFeasibilityRequest();
						resourceId = getResourceIdByCircuitId(gdeSchedules);
						constructFeasibilityPayload(nplLink, quoteToLeEntity, gdeSchedules, gdeFeasibilityRequest); 
						  mdsoAck = reTriggerFeasibilityCheck(mdsoAck, nplLink, gdeSchedules, linkFeasibility,gdeFeasibilityRequest);							
					} else {
						LOGGER.info("checking feasibility - Quote not got changed for quoteLe {} and linkId {}",quoteToLeId, linkId);
						saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
								FPConstants.FEASIBILITY.toString(), FPConstants.TRUE.toString());// disable the feasible flag
						saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
								FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
						mdsoAck = CommonConstants.SUCCESS;
						
					}
				} else {
					LOGGER.info("checking feasibility - Triggering feasibility for quoteLe {} and linkId {}",quoteToLeId, linkId);
					saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
							FPConstants.FEASIBILITY.toString(), FPConstants.FALSE.toString());// disable the feasible flag
					saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
							FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
					
					GdeScheduleDetails gdeScheduleDetails = new GdeScheduleDetails();
					List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLeId);
					if(quoteIllSiteToService != null && !quoteIllSiteToService.isEmpty()) {
						circuitId = quoteIllSiteToService.get(0).getErfServiceInventoryTpsServiceId();
						gdeScheduleDetails.setServiceId(circuitId);
					}
					LinkFeasibility linkFeasibiity = new LinkFeasibility();
					resourceId = getResourceIdByCircuitId(gdeScheduleDetails);
					mdsoAck = triggerFeasibilityCheck(linkId, mdsoAck, nplLink, quoteToLeEntity, resourceId,gdeScheduleDetails, linkFeasibiity);
				} 
				
			} else {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			
		}catch(TclCommonException | ParseException e) {
			throw new TclCommonException("Exception occured while checking feasibility for GDE quoteToLeId {}",quoteToLeId) ;
		}
		return mdsoAck;
		
	}

	/**
	 * Method to trigger feasibility to MDSO
	 * @param linkId
	 * @param mdsoAck
	 * @param nplLink
	 * @param quoteToLeEntity
	 * @param resourceId
	 * @param gdeScheduleDetails
	 * @param linkFeasibiity
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	private String triggerFeasibilityCheck(Integer linkId, String mdsoAck, QuoteNplLink nplLink,
			Optional<QuoteToLe> quoteToLeEntity, String resourceId, GdeScheduleDetails gdeScheduleDetails,
			LinkFeasibility linkFeasibiity) throws TclCommonException, ParseException {
		GdeFeasibilityInputBean inputs = new GdeFeasibilityInputBean();
		GdeFeasibilityRequest gdeFeasibilityRequest = new GdeFeasibilityRequest();
		GdeFeasibilityBwProfile bwProfile = new GdeFeasibilityBwProfile();
		gdeFeasibilityRequest.setInterfaces("feasibility_check");
		gdeFeasibilityRequest.setDescription("feasibilityCheck1");
		
		  inputs.setExclusive_time(1440); 
//		  inputs.setCallback_url("http://calback");  		  
		  extractComponentAttributes(nplLink, gdeScheduleDetails, inputs, bwProfile); 
//		  bwProfile.setBwEbs(1);
//		  bwProfile.setBwEir(1);
		  inputs.setBwProfile(bwProfile);
		  inputs.setTitle(Utils.generateFeasibilityTitle("feasibilityCheck", quoteToLeEntity.get().getQuote().getQuoteCode()));
		  gdeFeasibilityRequest.setInputs(inputs); 
		  HttpHeaders headers = peristHeaders(); 
		  if(StringUtils.isEmpty(resourceId))
			 throw new TclCommonException(ExceptionConstants.MSDO_RESOURCE_ID_NULL, ResponseResource.R_CODE_ERROR);
		  RestResponse feasibilitCheckResponse = restClientService.postSkipSslVerification(feasibilityCheckEndPoint.replace("<resourceId>",resourceId), Utils.convertObjectToJson(gdeFeasibilityRequest), headers);
		  if(feasibilitCheckResponse != null && feasibilitCheckResponse.getData() != null) {
			  JSONParser jsonParserf = new JSONParser(); 
			  JSONObject feasibilitObj = (JSONObject) jsonParserf.parse(feasibilitCheckResponse.getData());
			  linkFeasibiity.setResponseJson(feasibilitCheckResponse.getData());
				linkFeasibiity.setFeasibilityCheck("system");
				linkFeasibiity.setIsSelected((byte) 0);
				linkFeasibiity.setFeasibilityMode("ONNET_GDE");
				linkFeasibiity.setQuoteNplLink(nplLink);
				linkFeasibiity.setCreatedTime(new Timestamp(new Date().getTime()));
				linkFeasibiity.setFeasibilityCode(Utils.generateUid());
				linkFeasibilityRepository.save(linkFeasibiity);
				
				nplLink.setFpStatus("N");
				nplLink.setFeasibility((byte) 0);
				nplLinkRepository.save(nplLink);
				
				gdeScheduleDetails.setMdsoFeasibilityUuid((String) feasibilitObj.get("id"));
				gdeScheduleDetails.setActivationStatus((String) feasibilitObj.get("state"));
				gdeScheduleDetails.setLinkId(linkId);
				gdeScheduleDetails.setQuoteCode(quoteToLeEntity.get().getQuote().getQuoteCode());
				gdeScheduleDetails.setCreatedTime(new Timestamp(new Date().getTime()));
				gdeScheduleDetails.setFeasibilityStatus("N");
				gdeScheduleDetails.setUpdatedTime(new Timestamp(new Date().getTime()));
				gdeScheduleDetails.setMdsoResourceId(resourceId);
				gdeScheduleDetails.setIsActive((byte) 0);
				gdeScheduleDetailsRepository.save(gdeScheduleDetails); 
				mdsoAck = CommonConstants.SUCCESS;
		  } else {
			  LOGGER.error("Error occured while checking MDSO feasibility response in null for linkId {}", linkId);
			  throw new TclCommonException("MDSO Feasibility check is null");
		  }
		return mdsoAck;
	}

	private HttpHeaders peristHeaders() throws TclCommonException, ParseException {
		HttpHeaders headers = new  HttpHeaders(); 
		  headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		  headers.add("Authorization", generateMdsoAuthToken());
		return headers;
	}

	/**
	 * MEthod to extract componets and attributes to persist 
	 * in feasibility request
	 * @param nplLink
	 * @param gdeScheduleDetails
	 * @param inputs
	 * @param bwProfile
	 */
	private void extractComponentAttributes(QuoteNplLink nplLink, GdeScheduleDetails gdeScheduleDetails,
			GdeFeasibilityInputBean inputs, GdeFeasibilityBwProfile bwProfile) {
		List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(nplLink.getId(), "GDE_BOD"); 
		  List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(components.get(0).getId());
		  attributes.stream().forEach(attr->{
		  LOGGER.info("Processing attribute {} and attribute id {} ",attr.getProductAttributeMaster().getName()); 
		  switch(attr.getProductAttributeMaster().getName()) {
		  case CommonConstants.BANDWIDTH_ON_DEMAND :{
			  int bwOnDemand = Integer.parseInt(attr.getAttributeValues());
			  //temporarily sending cir in Kbps
			  gdeScheduleDetails.setBwOnDemand(bwOnDemand);
			  bwProfile.setBwCir(bwOnDemand*1000);
		  }  
		  break; 
		  case CommonConstants.BOD_SCHEDULE_END_DATE: { 
			  inputs.setEnd_time(attr.getAttributeValues());
			  try {
				gdeScheduleDetails.setScheduleEndDate(Utils.convertStringToOffSetTime(attr.getAttributeValues()));
			} catch (java.text.ParseException e) {
				LOGGER.error("Exception while parsing Schedule End Date & Time while creating feasibility",e);
			}
		  }   
		  break; 
		  case CommonConstants.BOD_SCHEDULE_START_DATE: {
			  inputs.setStart_time(attr.getAttributeValues()); 
			  try {
				gdeScheduleDetails.setScheduleStartDate(Utils.convertStringToOffSetTime(attr.getAttributeValues()));
			} catch (java.text.ParseException e) {
				LOGGER.error("Exception while parsing Schedule start Date & Time while creating feasibility",e);
			}						   
			  } 
		  break;
		  case CommonConstants.UPGRADED_BANDWIDTH: { 
			  gdeScheduleDetails.setUpgradedBw(Integer.parseInt(attr.getAttributeValues()));
		  }   
		  break; 
		  case CommonConstants.BASE_CIRCUIT_BANDWIDTH: {
			  gdeScheduleDetails.setBaseCircuitBw(Integer.parseInt(attr.getAttributeValues()));
		  }   
		  break;
		  case CommonConstants.SLOTS :
			  gdeScheduleDetails.setSlots(Integer.parseInt(attr.getAttributeValues()));
			  break;
		  
		  }

		  });
	}

	/**
	 * Method to trigger feasibility for expired or updated attributes
	 * @param mdsoAck
	 * @param nplLink
	 * @param gdeSchedules
	 * @param linkFeasibility
	 * @param gdeFeasibilityRequest
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	private String reTriggerFeasibilityCheck(String mdsoAck, QuoteNplLink nplLink, GdeScheduleDetails gdeSchedules,
			LinkFeasibility linkFeasibility, GdeFeasibilityRequest gdeFeasibilityRequest)
			throws TclCommonException, ParseException {
		  HttpHeaders headers = peristHeaders();
		  if(StringUtils.isEmpty(gdeSchedules.getMdsoResourceId()))
			  throw new TclCommonException(ExceptionConstants.MDSO_RESOURCE_ID_IS_NULL, ResponseResource.R_CODE_ERROR);
		  RestResponse feasibilitCheckResponse = restClientService.post(feasibilityCheckEndPoint.replace("<resourceId>",gdeSchedules.getMdsoResourceId()), Utils.convertObjectToJson(gdeFeasibilityRequest), headers);  
		  if(feasibilitCheckResponse != null & feasibilitCheckResponse.getStatus().equals(Status.SUCCESS)) {
			  JSONParser jsonParserf = new JSONParser();
			  JSONObject feasibilitObj = (JSONObject) jsonParserf.parse(feasibilitCheckResponse.getData());
			  gdeSchedules.setMdsoFeasibilityUuid((String) feasibilitObj.get("id"));
			  linkFeasibility.setResponseJson(feasibilitCheckResponse.getData()); 							  
			  nplLink.setFpStatus("N");
				nplLinkRepository.save(nplLink);
				linkFeasibility.setFeasibilityCheck("system");
				linkFeasibility.setIsSelected((byte) 0);
				linkFeasibilityRepository.save(linkFeasibility);		
				gdeSchedules.setFeasibilityStatus("N");
				gdeSchedules.setIsActive((byte) 0);
				gdeSchedules.setUpdatedTime(new Timestamp(new Date().getTime()));
				gdeScheduleDetailsRepository.save(gdeSchedules);
				mdsoAck = CommonConstants.SUCCESS;
		  } else {
			  throw new TclCommonException(ExceptionConstants.MDSO_RESOURCE_ID_IS_NULL, ResponseResource.R_CODE_ERROR);
		  }
		return mdsoAck;
	}

	/**
	 * Method to construct feasibility request payload MDSO
	 * @param nplLink
	 * @param quoteToLeEntity
	 * @param gdeSchedules
	 * @param gdeFeasibilityRequest
	 */
	private void constructFeasibilityPayload(QuoteNplLink nplLink, Optional<QuoteToLe> quoteToLeEntity,
			GdeScheduleDetails gdeSchedules, GdeFeasibilityRequest gdeFeasibilityRequest) {
		GdeFeasibilityInputBean inputs = new GdeFeasibilityInputBean();
		GdeFeasibilityBwProfile bwProfile = new GdeFeasibilityBwProfile();
		gdeFeasibilityRequest.setInterfaces("feasibility_check");
		inputs.setTitle(Utils.generateFeasibilityTitle("feasibilityCheck",quoteToLeEntity.get().getQuote().getQuoteCode()));
		gdeFeasibilityRequest.setDescription("feasibilityCheck"+quoteToLeEntity.get().getQuote().getQuoteCode());
		//exclusive time is 1440 mins for BOD
		inputs.setExclusive_time(1440); 
//		bwProfile.setBwEbs(1);
//		bwProfile.setBwEir(1);
		List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(nplLink.getId(), "GDE_BOD");
		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(components.get(0).getId()); 
		attributes.stream().forEach(attr->{
			LOGGER.info("Processing attribute {} and attribute id {} ",attr.getProductAttributeMaster().getName());
			switch (attr.getProductAttributeMaster().getName()) { 
			case CommonConstants.BANDWIDTH_ON_DEMAND : {
				int bwOnDemand = Integer.parseInt(attr.getAttributeValues());
				//temporarily sending cir in Kbps
				gdeSchedules.setBwOnDemand(bwOnDemand);
				bwProfile.setBwCir(bwOnDemand*1000);
			}
				break; 
		  case CommonConstants.BOD_SCHEDULE_END_DATE : { 
			  try {
				gdeSchedules.setScheduleEndDate(Utils.convertStringToOffSetTime(attr.getAttributeValues()));
			} catch (java.text.ParseException e) {
				LOGGER.error("Exception while parsing BOD_SCHEDULE_END_DATE ", e);
			}
			  inputs.setEnd_time(attr.getAttributeValues());
		  } 
		  break; 
		  case CommonConstants.BOD_SCHEDULE_START_DATE:{
			  try {
				  gdeSchedules.setScheduleStartDate(Utils.convertStringToOffSetTime(attr.getAttributeValues()));
				  inputs.setStart_time(attr.getAttributeValues());
			  } catch(java.text.ParseException e) {
				  LOGGER.error("Exception while parsing BOD_SCHEDULE_START_DATE ", e);
			  }
			   
		  }
		  break; 
		  
		  case CommonConstants.UPGRADED_BANDWIDTH : {
			  gdeSchedules.setUpgradedBw(Integer.parseInt(attr.getAttributeValues()));
//			  bwProfile.setBwCbs(Integer.parseInt(attr.getAttributeValues()));
		  }
			  break;
			  
		  case CommonConstants.BASE_CIRCUIT_BANDWIDTH :
			  gdeSchedules.setBaseCircuitBw(Integer.parseInt(attr.getAttributeValues()));
			  break;
		  case CommonConstants.SLOTS :
			  gdeSchedules.setSlots(Integer.parseInt(attr.getAttributeValues()));
			  break;
		  }
		  }); 
		  inputs.setBwProfile(bwProfile);
		  gdeFeasibilityRequest.setInputs(inputs);
	}

	/**
	 * Method to get updated attributes for computation
	 * @param gdeSchedules
	 * @param quoteUpdates
	 * @param quoteAttributes
	 */
	private void getUpdatedAttributes(GdeScheduleDetails gdeSchedules, Map<String, Boolean> quoteUpdates,
			List<QuoteProductComponentsAttributeValue> quoteAttributes) {
		quoteAttributes.stream().forEach(qattr->{
			  LOGGER.info("Processing attribute {} and attribute id {} ",qattr.getProductAttributeMaster().getName()); 
			  switch (qattr.getProductAttributeMaster().getName()) { 
			  case CommonConstants.UPGRADED_BANDWIDTH :{
				  if(!gdeSchedules.getUpgradedBw().toString().equals(qattr.getAttributeValues())) {
					  quoteUpdates.put(qattr.getProductAttributeMaster().getName(), true);
				  } else {
					  quoteUpdates.put(qattr.getProductAttributeMaster().getName(), false);
				  }
			  }
			  break; 
			  case CommonConstants.BOD_SCHEDULE_END_DATE: { 
				  if(!gdeSchedules.getScheduleEndDate().toString().equals(qattr.getAttributeValues().substring(0,16).concat(qattr.getAttributeValues().substring(19)))) {
					  quoteUpdates.put(qattr.getProductAttributeMaster().getName(), true);
				  } else {
					  quoteUpdates.put(qattr.getProductAttributeMaster().getName(), false);
				  }
			  } 
			  break; 
			  case CommonConstants.BOD_SCHEDULE_START_DATE :{
				  if(!gdeSchedules.getScheduleStartDate().toString().equals(qattr.getAttributeValues().substring(0,16).concat(qattr.getAttributeValues().substring(19)))) {
					  quoteUpdates.put(qattr.getProductAttributeMaster().getName(), true);
				  } else {
					  quoteUpdates.put(qattr.getProductAttributeMaster().getName(), false);
				  }
			  }
			  
			  break; 
			  }
			  
			  });
	}

	/**
	 * Method to get resource id for the TCL service id configured
	 * @param gdeScheduleDetails
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	private String getResourceIdByCircuitId(GdeScheduleDetails gdeScheduleDetails)
			throws TclCommonException, ParseException {
		LOGGER.info("Entering getResourceIdByCircuitId to fetch resource id for link {} and service id{} ",gdeScheduleDetails.getLinkId(), gdeScheduleDetails.getServiceId());
		String resourceId = null;
		Map<String,String> header = new HashMap<>(); 
		header.put("Authorization",generateMdsoAuthToken()); 
		header.put("Content-Type", "application/json");
		String resourceEndPoint = circuitLookUpEndPoint;
		if(gdeScheduleDetails.getServiceId()!=null) {
			if(gdeScheduleDetails.getServiceId().equalsIgnoreCase("22160327152"))
				resourceEndPoint = resourceEndPoint+"dev-tcl-svc-0003";
			else
				resourceEndPoint = resourceEndPoint+gdeScheduleDetails.getServiceId();
		} else 
			throw new TclCommonException(ExceptionConstants.GDE_SERVICE_ID_IS_NULL, ResponseResource.R_CODE_ERROR);
		LOGGER.info("getResourceIdByCircuitId circuitLookUp url {} ", resourceEndPoint);
		  RestResponse circuitLookUpResponse = restClientService.get(resourceEndPoint, header, true);
		  LOGGER.info("Entering getResourceIdByCircuitId and  circuitLookUpResponse {} ",circuitLookUpResponse);
		  if(!StringUtils.isBlank(circuitLookUpResponse.getData())) {
			  JSONParser parseCircuitLookUp = new JSONParser(); 
			  JSONObject jsonObj = (JSONObject) parseCircuitLookUp.parse(circuitLookUpResponse.getData());
			  JSONArray jsonArray = (JSONArray) jsonObj.get("items");
			  if(!jsonArray.isEmpty()) {
				  JSONObject circuitData = (JSONObject)jsonArray.get(0);
				  resourceId = (String) circuitData.get("id");
				  LOGGER.info("Inside getResourceIdByCircuitId and resourceId {}", resourceId);
				  if(resourceId != null) {
					  gdeScheduleDetails.setMdsoResourceId(resourceId); 
				  } else {
					  throw new TclCommonException(ExceptionConstants.MSDO_RESOURCE_ID_NULL, ResponseResource.R_CODE_ERROR);
					  
				  }
				  
			  } 
		  }
		return resourceId;
	}

	public String generateMdsoAuthToken() throws TclCommonException, ParseException {
		String bearerToken = null;
		try{
			GdeAuthRequestBean gdeAuthRequestPayLoad = new GdeAuthRequestBean();
			gdeAuthRequestPayLoad.setUsername(authTokenUserName);
			gdeAuthRequestPayLoad.setPassword(authTokenPswd);
			gdeAuthRequestPayLoad.setTenant(authTenant);
			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			RestResponse authResponse = restClientService.postSkipSslVerification(authTokenEndPoint, Utils.convertObjectToJson(gdeAuthRequestPayLoad), httpHeader);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(authResponse.getData());
			String authToken = (String) jsonObject.get("token");
			bearerToken = "Bearer"+CommonConstants.SPACE+authToken;
		}catch(Exception e) {
			throw new TclCommonException("Exception while getting MDSO auth token", e);		  
		}
		
		return bearerToken;
	}
	
	/**
	 * Method to calclulate price and map to components
	 * @param linkId
	 * @param quoteToLeId
	 * @param gdeSchedules
	 * @return
	 * @throws TclCommonException
	 */
	public String gdePricingCalculation(Integer linkId, Integer quoteToLeId, GdeScheduleDetails gdeSchedules) throws TclCommonException {
		/*
		 * On-Demand charges= No of slots x Rate x Final Multiplier x Additional on-demand Bandwidth 
		 * Rate = [ MRC/ (Base Bandwidth*30 days*24hrs)] * Slot
		 * Final Multiplier = Upgrade Multiplier x Reservation Duration Multiplier X On-Demand Premium multiplier is 2.4
		 */
		String response = CommonConstants.FAILIURE;
		Double onDemandCharge = 0D;
		try {
			Double mrc = 0D;
			int slotsCounts = 1;
			int baseBw;
			Double OnDemandPremiumMultiplier = 2.4;
			Double finalMultiplier = 0D;
			Double reservationMultiplier = 0D;
			List<Integer> bwOnDemand = new ArrayList<>();
			List<Integer> slotsCountList = new ArrayList<>();
			List<Integer> upgradedBw = new ArrayList<>();
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			SiServiceSiContractInfoBean serviceDetails = extractServiceIdDetails(gdeSchedules);
			Double onnetArc = serviceDetails.getMrc();
			LOGGER.info("Pricing calc  onnetArc {} for service id {} ",onnetArc , gdeSchedules.getServiceId());
			if(onnetArc != null && onnetArc != 0)
				mrc = onnetArc/12;
			else
				throw new TclCommonException(ExceptionConstants.GDE_ONNET_ARC_NOT_AVAILABLE, ResponseResource.R_CODE_ERROR);
			baseBw = Integer.parseInt(serviceDetails.getBaseBandwidth());
			gdeSchedules.setBandwidthUnit(serviceDetails.getBandwidthUnit());
			gdeSchedules.setPaymentCurrency(serviceDetails.getBillingCurrency());
			//Attribute values
			processPricingAttr(linkId, bwOnDemand, slotsCountList, upgradedBw);
			//Rate
			long bwPerDay = baseBw * 30 * 24;
			Double mrcBw = mrc / bwPerDay;
			LOGGER.info("Pricing calc baseBw {} / bwPerDay {} / mrc {} / mrcBw {} ",baseBw , bwPerDay,mrc, mrcBw);
			Double Rate = mrcBw *24;
			LOGGER.info("Pricing calculation Rate for link id {} is {} for ", linkId, Rate);
			//Final Multiplier
			Double upgradeMultiplier = 0D;
			int upgradeBw = upgradedBw.get(0);
			if(baseBw <= upgradeBw && upgradeBw <= 2*baseBw)
				upgradeMultiplier = 0.81;
			else if (baseBw*2 <= upgradeBw && upgradeBw <= 3*baseBw)
				upgradeMultiplier = 0.663;
			else if(3*baseBw <= upgradeBw && upgradeBw <= 4*baseBw)
				upgradeMultiplier = 0.6;
			LOGGER.info("Pricing calculation upgradeMultiplier {} for link id {} / upgradeBw {}",upgradeMultiplier, linkId, upgradeBw);
			//ReservationMultiplier
			slotsCounts = slotsCountList.get(0);
			if(1 <= slotsCounts && slotsCounts <= 10) {
				reservationMultiplier = 1D;
			} else if (11 <= slotsCounts && slotsCounts <= 20 ) {
				reservationMultiplier = 0.98D;
			} else if (21 <= slotsCounts && slotsCounts <= 30 ) {
				reservationMultiplier = 0.96D;
			} else if (31 <= slotsCounts && slotsCounts <= 40 ) {
				reservationMultiplier = 0.94D;
			} else if (41 <= slotsCounts && slotsCounts <= 50 ) {
				reservationMultiplier = 0.92D;
			} else if (51 <= slotsCounts && slotsCounts <= 60 ) {
				reservationMultiplier = 0.9D;
			}
			finalMultiplier = upgradeMultiplier*reservationMultiplier*OnDemandPremiumMultiplier;
			LOGGER.info("Pricing calculation reservationMultiplier {} finalMultiplier{} ",reservationMultiplier, finalMultiplier);
			onDemandCharge = slotsCounts*Rate*finalMultiplier*bwOnDemand.get(0);
			LOGGER.info("Pricing calculation onDemandCharge{}  ",onDemandCharge);
			onDemandCharge = Math.round(onDemandCharge * 100.0) / 100.0;
			LOGGER.info("Pricing calculation rounded onDemandCharge {} ",onDemandCharge);
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(linkId, "Link");
			Map<Integer,Map<String, Double>> sitePriceMapper = new HashMap<>();
			Map<String, Double> priceMapper = mapPriceToComponents(productComponents,quoteToLe.get(), quoteToLe.get().getCurrencyCode(), onDemandCharge);
			sitePriceMapper.put(linkId, priceMapper);
			mapLinkPrices(linkId ,sitePriceMapper, quoteToLe.get(), quoteToLe.get().getCurrencyCode(), gdeSchedules);
			
			if(onDemandCharge != 0.0) {
				response = "Sucess";
			}
			
		} catch(Exception e) {
			throw new TclCommonException("Errorin pricing calculation link id {}",linkId) ;
		}
		return response;		
	}

	private SiServiceSiContractInfoBean extractServiceIdDetails(GdeScheduleDetails gdeSchedules)
			throws TclCommonException {
		SIServiceInfoBean sIServiceInfoBean= new SIServiceInfoBean();
		sIServiceInfoBean.setTpsServiceId(gdeSchedules.getServiceId());
		sIServiceInfoBean.setProductName("GDE");
		String requestPayLoad = Utils.convertObjectToJson(sIServiceInfoBean);
		LOGGER.info("Request payload for siServiceContractInfoQueue {} ",requestPayLoad);
		String siContractInfoResponse = (String) mqUtils.sendAndReceive(siServiceContractInfoQueue, requestPayLoad);
		LOGGER.info("Response received from siServiceContractInfoQueue {} ",siContractInfoResponse);
		SiServiceSiContractInfoBean serviceDetails = Utils.convertJsonToObject(siContractInfoResponse, SiServiceSiContractInfoBean.class);
		return serviceDetails;
	}

	private void processPricingAttr(Integer linkId, List<Integer> bwOnDemand, List<Integer> slotsCountList,
			List<Integer> upgradedBw) {
		List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(linkId, "GDE_BOD");
		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(components.get(0).getId());
		attributes.stream().forEach(attr ->{
			if(attr.getProductAttributeMaster().getName().equalsIgnoreCase("Bandwidth On Demand")) {
				bwOnDemand.add(0,Integer.parseInt(attr.getAttributeValues()));
			}
			if(attr.getProductAttributeMaster().getName().equalsIgnoreCase("Slots")) {
				slotsCountList.add(Integer.parseInt(attr.getAttributeValues()));
				
			}
			if(attr.getProductAttributeMaster().getName().equalsIgnoreCase(CommonConstants.UPGRADED_BANDWIDTH)) {
				upgradedBw.add(Integer.parseInt(attr.getAttributeValues()));
			}
			
			
		});
	}
	
	public String callbackService(String resourceId, Integer linkId) {
		String resp = "Failure";
		try {
			GdeScheduleDetails gdeSchedule = gdeScheduleDetailsRepository.findByMdsoResourceIdAndLinkId(resourceId, linkId);
			//Feasibility poll invoke
//			String feasibilityPollEndPoint = "https://10.133.100.207/bpocore/market/api/v1/resources/"+resourceId+"/operations/"+operationId;
			
//			Map<String,String> header = new HashMap<>(); header.put("Authorization","Bearer "+generateAuthToken()); 
//			  header.put("Content-Type", "application/json"); 
//			  RestResponse feasibilityPoll = restClientService.get(feasibilityPollEndPoint, header, true);
			  
			  LOGGER.info("Inside getResourceIdByCircuitId");
//			  JSONParser jsonParser = new JSONParser();
//				JSONObject jsonObject = (JSONObject) jsonParser.parse(feasibilityPoll.getData());
//				String feasibilityState = (String) jsonObject.get("state");
//				String outputs = (String) jsonObject.get("outputs");
//				if(feasibilityState.equalsIgnoreCase("Sucess")) {
					gdeSchedule.setFeasibilityStatus("F");
					gdeSchedule.setUpdatedTime(new Timestamp(new Date().getTime()));
//				}
			Optional<QuoteNplLink> nplLinks = nplLinkRepository.findById(gdeSchedule.getLinkId());
			QuoteNplLink nplLink = nplLinks.get();
			nplLink.setFpStatus("F");
			nplLinkRepository.save(nplLink);
			List<LinkFeasibility> linkFeasibility = linkFeasibilityRepository.findByQuoteNplLink(nplLink);
			LinkFeasibility linkF= linkFeasibility.get(0);
			linkF.setIsSelected((byte) 1);
			linkF.setResponseJson("Manual coded response : Feasible");
			linkFeasibilityRepository.save(linkF);
			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(gdeSchedule.getQuoteCode());
			gdePricingCalculation(nplLink.getId(), quoteToLe.get(0).getId(), gdeSchedule);
			gdeScheduleDetailsRepository.save(gdeSchedule);
			saveProcessState(quoteToLe.get(0), FPConstants.IS_FP_DONE.toString(),
					FPConstants.FEASIBILITY.toString(), FPConstants.TRUE.toString());// disable the feasible flag
			saveProcessState(quoteToLe.get(0), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
			resp = "Sucess";
		} catch(Exception e) {
			LOGGER.error("Error in call back");
			
		}
		return resp;
		
		
	}
	
	private Map<String, Double> mapPriceToComponents(List<QuoteProductComponent> productComponents,
			QuoteToLe quoteToLe, String existingCurrency, Double onDemandCharge) {
		Map<String, Double> priceMapper = new HashMap<>();
		try {
			productComponents.stream().forEach(component -> {
				Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findById(component.getMstProductComponent().getId());
				if (mstProductComponent.get().getName().equals(FPConstants.GDE_BOD.toString())) {
					Double totalMRC = 0.0;
					Double totalNRC = 0.0;
					Double totalARC = 0.0;
					QuotePrice attrPrice = getComponentQuotePrice(component);
					Double chargableMRC = 0D;
					Double chargableNRC = onDemandCharge;
					Double chargableARC = 0D;
					
//					chargableMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
//							chargableMRC);
//					chargableNRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
//							chargableNRC);
//					chargableARC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
//							chargableARC);
//					chargableNRC = Math.round(chargableNRC * 100.0) / 100.0;
					if (attrPrice != null) {
						totalMRC = totalMRC + chargableMRC;
						totalNRC = totalNRC + chargableNRC;
						totalARC = totalARC + chargableARC;
						attrPrice.setEffectiveMrc(chargableMRC);
						attrPrice.setEffectiveNrc(chargableNRC);
						attrPrice.setEffectiveArc(chargableARC);
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, chargableMRC ,chargableNRC, chargableARC);
						totalMRC = totalMRC + chargableMRC;
						totalNRC = totalNRC + chargableNRC;
						totalARC = totalARC + chargableARC;
					}
					priceMapper.put(FPConstants.TOTAL_MRC.toString(), totalMRC);
					priceMapper.put(FPConstants.TOTAL_NRC.toString(), totalNRC);
					priceMapper.put(FPConstants.TOTAL_ARC.toString(), totalARC);
					priceMapper.put(FPConstants.TOTAL_TCV.toString(), totalNRC);
				}
			});
			
		} catch(Exception e) {
			
		}
				return priceMapper;
		
	}
	
	public void mapLinkPrices(Integer linkId, Map<Integer,Map<String,Double>> sitePriceMapper, QuoteToLe quoteToLe, String currencyCode, GdeScheduleDetails gdeSchedules) {
		try {			
			sitePriceMapper.entrySet().forEach(linkPrice ->{
				QuoteNplLink quoteNplLink = nplLinkRepository.findByIdAndStatus(linkPrice.getKey(), (byte) 1);
				quoteNplLink.setMrc(linkPrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
				quoteNplLink.setNrc(linkPrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
				quoteNplLink.setArc(linkPrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
				quoteNplLink.setTcv(linkPrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
				quoteNplLink.setFeasibility((byte) 1);
				quoteNplLink.setFpStatus(FPStatus.FP.toString());
				quoteNplLink = nplLinkRepository.save(quoteNplLink);
				LOGGER.info("updating price to link {}", quoteNplLink.getId());
				//map price to quote to le 
				quoteToLe.setProposedMrc(quoteNplLink.getMrc() != null ? quoteNplLink.getMrc() : 0D);
				quoteToLe.setProposedNrc(quoteNplLink.getNrc() != null ? quoteNplLink.getNrc() : 0D);
				quoteToLe.setProposedArc(quoteNplLink.getArc() != null ? quoteNplLink.getArc() : 0D);
				quoteToLe.setTotalTcv(quoteNplLink.getTcv() != null ? quoteNplLink.getTcv() : 0D);
				quoteToLe.setFinalMrc(quoteNplLink.getMrc() != null ? quoteNplLink.getMrc() : 0D);
				quoteToLe.setFinalNrc(quoteNplLink.getNrc() != null ? quoteNplLink.getNrc() : 0D);
				quoteToLe.setFinalArc(quoteNplLink.getArc() != null ? quoteNplLink.getArc() : 0D);
				quoteToLeRepository.save(quoteToLe);
				LOGGER.info("updated quoteToLe price to for link id {} and quoteToLeId {} ", quoteNplLink.getId(), quoteToLe.getId());
				gdeSchedules.setChargeableNrc(linkPrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
			});
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * processNewPrice
	 * 
	 * @param quoteToLe
	 * @param component
	 * @param illARC
	 * @param illNrc
	 */
	private void processNewPrice(QuoteToLe quoteToLe, QuoteProductComponent component, Double illMRC, Double illNrc,
			Double illArc) {
		QuotePrice attrPrice;
		attrPrice = new QuotePrice();
		attrPrice.setQuoteId(quoteToLe.getQuote().getId());
		attrPrice.setReferenceId(String.valueOf(component.getId()));
		attrPrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
		attrPrice.setEffectiveMrc(illMRC);
		attrPrice.setEffectiveNrc(illNrc);
		attrPrice.setEffectiveArc(illArc);
		attrPrice.setMstProductFamily(component.getMstProductFamily());
		quotePriceRepository.save(attrPrice);
	}
	/**
	 * 
	 * saveProcessState
	 * 
	 * @param quoteToLe
	 * @param attrName
	 * @param category
	 * @param state
	 */
	private void saveProcessState(QuoteToLe quoteToLe, String attrName, String category, String state) {
		LOGGER.info("Save process State");
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attrName,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setName(attrName);
			mstOmsAttribute.setCategory(category);
			mstOmsAttribute.setDescription(attrName);
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttributeRepository.save(mstOmsAttribute);

		}
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attrName);
		if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();

			attributeValue.setAttributeValue(state);
			attributeValue.setDisplayValue(attrName);
			attributeValue.setQuoteToLe(quoteToLe);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValueRepository.save(attributeValue);
		} else {
			quoteLeAttributeValues.forEach(attr -> {
				attr.setAttributeValue(state);
				quoteLeAttributeValueRepository.save(attr);

			});

		}

	}
	
	/**
	 * Method to get quote price for the given component
	 * @param component
	 * @return
	 */
	private QuotePrice getComponentQuotePrice(QuoteProductComponent component) {

		return quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getId()),
				QuoteConstants.COMPONENTS.toString());
	}
	
	/**
	 * Method provide call back functionality for MDSO on operations
	 * @param feasibilityResponse
	 * @return
	 * @throws TclCommonException
	 */
	public String notifyFeasibilityCheck(FeasibilityCheckResponse feasibilityResponse) throws TclCommonException {
		String response = CommonConstants.FAILIURE;
		/*This method used by MDSO as call back to notify operation status. 
		 * On failure cases, ticket got created in SNOW*/
		try {
			if(feasibilityResponse != null && StringUtils.isNotBlank(feasibilityResponse.getStatus())) {
				LOGGER.info("notifyFeasibilityCheck request payload {} ", feasibilityResponse.toString());
				boolean bookingResponse = Arrays.asList("BOOKING_SUCCESS","BOOKING_FAILED").contains(feasibilityResponse.getStatus());
				OdrScheduleDetails odrSchedules = new OdrScheduleDetails();
				if(bookingResponse) {
					LOGGER.info("bookingResponse and status is {} for operationid {}  ", feasibilityResponse.getStatus(), feasibilityResponse.getOperationId());
					odrSchedules = odrScheduleDetailsRepository.findByScheduleOperationIdAndMdsoResourceId(
							feasibilityResponse.getOperationId(), feasibilityResponse.getMdsoId());
					if(feasibilityResponse.getStatus().equalsIgnoreCase("BOOKING_SUCCESS")) {
						odrSchedules.setScheduleId(feasibilityResponse.getScheduleId());
					}	
				} else {
					LOGGER.info("bookingResponse and status is {} for scheduleid {}  ", feasibilityResponse.getStatus(), feasibilityResponse.getScheduleId());
					odrSchedules = odrScheduleDetailsRepository.findByScheduleIdAndMdsoResourceId(feasibilityResponse.getScheduleId(), feasibilityResponse.getMdsoId());
				}
				boolean feasibilityFailure = Arrays.asList(GdeOrderConstants.BOOKING_FAILED, GdeOrderConstants.CANCELLATION_FAILED, 
						GdeOrderConstants.ACTIVATION_FAILED, GdeOrderConstants.DE_ACTIVATION_FAILED).contains(feasibilityResponse.getStatus());
				boolean orderStageStatus = Arrays.asList(GdeOrderConstants.CANCELLATION_SUCCESS, GdeOrderConstants.DE_ACTIVATION_SUCCESS).contains(feasibilityResponse.getStatus());
				LOGGER.info("notifyFeasibilityCheck :feasibilty staus is {} for shedule id {}",feasibilityResponse.getStatus(), feasibilityResponse.getScheduleId());
				if(odrSchedules != null) {
					List<OrderToLe> orderLes = orderToLeRepository.findByOrder_OrderCode(odrSchedules.getOrderCode());
					OrderToLe orderToLe = orderLes.get(0);
					List<String> ticketIds = new ArrayList<>();
					User customerUser = userRepository.findByIdAndStatus(orderToLe.getOrder().getCreatedBy(), 1);
					LOGGER.info("notifyFeasibilityCheck Feasibility status is {} for orderCode {} ",feasibilityResponse.getStatus(), odrSchedules.getOrderCode());
					if(feasibilityFailure) {
						//Snow integration
						LOGGER.info("Creating ticket is SNOW system for failure operation for linkid {}",feasibilityResponse.getStatus(), odrSchedules.getOrderLinkId());
						CreateTicketRequestBean createTicketRequestBean = new CreateTicketRequestBean();
						String category = null;
						if(feasibilityResponse.getStatus().equalsIgnoreCase(GdeOrderConstants.ACTIVATION_FAILED)) {
							category = "BOD Activation Failure";
						} else if(feasibilityResponse.getStatus().equalsIgnoreCase(GdeOrderConstants.BOOKING_FAILED)) {
							category = "Booking Failed";
						} else if(feasibilityResponse.getStatus().equalsIgnoreCase(GdeOrderConstants.CANCELLATION_FAILED)) {
							category = "BOD Cancellation Failure";
						} else if(feasibilityResponse.getStatus().equalsIgnoreCase(GdeOrderConstants.DE_ACTIVATION_FAILED)) {
							category = "BOD Deactivation Failure";
						}
						createTicketRequestBean.setImpact("Partial Loss");
						createTicketRequestBean.setCategory(category);
						createTicketRequestBean.setDescription("Order id - "+odrSchedules.getOrderCode()+","+"Schedule id - "+odrSchedules.getScheduleId());
						createTicketRequestBean.setServiceIdentifier(Arrays.asList(odrSchedules.getServiceId()));
						TicketContactBean ticketContactBean = new TicketContactBean();
						if(customerUser != null) {
							createTicketRequestBean.setUserId(customerUser.getUsername());
							ticketContactBean.setName(customerUser.getUsername());
							ticketContactBean.setEmail(customerUser.getEmailId());
							//contact No is mandatory, setting default in case of null
							if(StringUtils.isNotBlank(customerUser.getContactNo()))
								ticketContactBean.setPrimaryPhone(customerUser.getContactNo());
							ticketContactBean.setPrimaryPhone("9090909090");
//							ticketContactBean.setSecondaryPhone(user.getContactNo());
							createTicketRequestBean.setContact(ticketContactBean);
						} else {
							LOGGER.error(ExceptionConstants.CUSTOMER_EMPTY, ResponseResource.R_CODE_ERROR); 
						}
						LOGGER.info("MDC Filter token value in before Queue call ticket create {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
						 String ticketResponse = (String) mqUtils.sendAndReceive(ticketCreationQueue, Utils.convertObjectToJson(createTicketRequestBean));
						 LOGGER.info("MDC Filter token value in After Queue call ticket create {} and response{} :",MDC.get(CommonConstants.MDC_TOKEN_KEY), ticketResponse);	 
						 if(StringUtils.isNotBlank(ticketResponse)) {
							 CreateTicketResponseBean[] createTicketResponseBeans = Utils.convertJsonToObject(ticketResponse, CreateTicketResponseBean[].class);
							 List<CreateTicketResponseBean> createTicketResp = Arrays.asList(createTicketResponseBeans);
							 createTicketResp.stream().forEach(resp->{
								 if(resp.getStatus().equals("201")) {
									 ticketIds.add(resp.getTicketId());
								 } else {
									 ticketIds.add(CommonConstants.FAILIURE);
								 }
							 });
							 odrSchedules.setTicketId(ticketIds.get(0));
						 }						
					}
					orderToLe.setStage(feasibilityResponse.getStatus());
					orderToLeRepository.save(orderToLe);
					
					Order orders = orderToLe.getOrder();
					if(orderStageStatus)
						orders.setStage(feasibilityResponse.getStatus());
						else
							orders.setStage(OrderStagingConstants.ORDER_COMPLETED.toString());						
					orderRepository.save(orders);
					
					odrSchedules.setActivationStatus(feasibilityResponse.getStatus());
					odrSchedules.setUpdatedTime(new Timestamp(new Date().getTime()));
					odrScheduleDetailsRepository.save(odrSchedules);
					constructOdrScheduleAudit(odrSchedules);
					
					if(feasibilityResponse.getStatus().equalsIgnoreCase(GdeOrderConstants.BOOKING_SUCCESS)) {
						persistBodBookingDetails(odrSchedules);
						List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_QuoteCode(orders.getOrderCode());
						LOGGER.info("notifyFeasibilityCheck process cof to update quoteid {} and quotele  {} ",quoteToLes.get(0).getQuote().getId(), quoteToLes.get(0).getId());
						Map<String, Object> requestparam = new HashMap<>();
						requestparam.put("orderId", orders.getId());
						requestparam.put("productName", "GDE");
						requestparam.put("userName", Utils.getSource());
						mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
						Map<String, String> cofObjectMapper = new HashMap<>();
						gdeQuotePdfService.processCofPdf(quoteToLes.get(0).getQuote().getId(), null, null, true, quoteToLes.get(0).getId(), cofObjectMapper);
						LOGGER.info("notifyFeasibilityCheck updated cof for ordercode {} with  scheduleid  {} ",odrSchedules.getOrderCode(), odrSchedules.getScheduleId());
					}
					if(feasibilityResponse.getStatus().equalsIgnoreCase(GdeOrderConstants.CANCELLATION_SUCCESS)) {
						BodBookingDetails bodBookingDetail = bodBookingDetailsRepository.findByBodScheduleIdAndMdsoResourceId(odrSchedules.getScheduleId(), odrSchedules.getMdsoResourceId());
						LOGGER.info("Inside notifyFeasibilityCheck deleting Booking details for cancelled order {} and link {} ",bodBookingDetail.getOrderCode(), bodBookingDetail.getOrderLinkId());
						bodBookingDetailsRepository.delete(bodBookingDetail);
					}
					
					response = CommonConstants.SUCCESS;
				} else {
				    LOGGER.info("notifyFeasibilityCheck Invalid input on notifying status for scheduleid {} / operationId", feasibilityResponse.getScheduleId(), feasibilityResponse.getOperationId());
					throw new TclCommonException(ExceptionConstants.MDSO_OPTIMUS_NOTIFICATION, ResponseResource.R_CODE_ERROR);
				}
			} else {
				LOGGER.info("notifyFeasibilityCheck Input is null / status is null on notifying status is {}",feasibilityResponse );
				throw new TclCommonException(ExceptionConstants.MDSO_OPTIMUS_NOTIFICATION, ResponseResource.R_CODE_ERROR);
			}
			
		} catch(Exception e) {
		    LOGGER.info("notifyFeasibilityCheck Exception on notifying status for scheduleid {} / operation id {} and error {} ", feasibilityResponse.getScheduleId() , feasibilityResponse.getOperationId(), e.getMessage());
			throw new TclCommonException(ExceptionConstants.MDSO_OPTIMUS_NOTIFICATION, e, ResponseResource.R_CODE_ERROR);
		}		
		return response;
	}

	/**
	 * Method to set BodBookingDetails
	 * @param odrSchedules
	 */
	private void persistBodBookingDetails(OdrScheduleDetails odrSchedules) {
		LOGGER.info("Inside persistBodBookingDetails and orderlinkid {} ", odrSchedules.getOrderLinkId());
		BodBookingDetails orderBookingDetails = bodBookingDetailsRepository.findFirstByBodScheduleIdAndMdsoResourceIdOrderByCreatedTimeAsc(
				odrSchedules.getScheduleId(), odrSchedules.getMdsoResourceId());
		if(orderBookingDetails == null) {
			BodBookingDetails bodBookingDetails = new BodBookingDetails();
			bodBookingDetails.setOrderLinkId(odrSchedules.getOrderLinkId());
			bodBookingDetails.setActivationStatus(odrSchedules.getActivationStatus());
			bodBookingDetails.setBandwidthUnit(odrSchedules.getBandwidthUnit());
			bodBookingDetails.setBaseCircuitBw(odrSchedules.getBaseCircuitBw());
			bodBookingDetails.setBwOnDemand(odrSchedules.getBwOnDemand());
			bodBookingDetails.setChargeableNrc(odrSchedules.getChargeableNrc());
			bodBookingDetails.setCreatedTime(new Timestamp(new Date().getTime()));
			bodBookingDetails.setFeasibilityStatus(odrSchedules.getFeasibilityStatus());
			bodBookingDetails.setFeasibilityValidity(odrSchedules.getFeasibilityValidity());
			bodBookingDetails.setIsPaymentInitiated((byte) 0);
			bodBookingDetails.setMdsoFeasibilityUuid(odrSchedules.getMdsoFeasibilityUuid());
			bodBookingDetails.setMdsoResourceId(odrSchedules.getMdsoResourceId());
			bodBookingDetails.setOrderCode(odrSchedules.getOrderCode());
			bodBookingDetails.setPaymentCurrency(odrSchedules.getPaymentCurrency());
			bodBookingDetails.setScheduleEndDate(odrSchedules.getScheduleEndDate());
			bodBookingDetails.setBodScheduleId(odrSchedules.getScheduleId());
			bodBookingDetails.setScheduleOperationId(odrSchedules.getScheduleOperationId());
			bodBookingDetails.setScheduleStartDate(odrSchedules.getScheduleStartDate());
			bodBookingDetails.setServiceId(odrSchedules.getServiceId());
			bodBookingDetails.setSlots(odrSchedules.getSlots());
			bodBookingDetails.setTicketId(odrSchedules.getTicketId());
			bodBookingDetails.setUpdatedTime(new Timestamp(new Date().getTime()));
			bodBookingDetails.setUpgradedBw(odrSchedules.getUpgradedBw());
			bodBookingDetailsRepository.save(bodBookingDetails);
			LOGGER.info("Inside persistBodBookingDetails Inserted booking details for ordercode {} ", odrSchedules.getOrderCode());
	}
	}
	
	
	/**
	 * checkQuoteLeFeasibility - this method checks the pricing and feasibility for
	 * the given quote legal entity id.
	 * 
	 * @author archchan
	 * @param quoteLeId
	 * @return QuoteLeAttributeBean
	 * @throws TclCommonException 
	 */
	public QuoteLeAttributeBean checkQuoteLeFeasibility(Integer quoteLeId) throws TclCommonException {
		try {
			Map<String, Boolean> fpStatus = new HashMap<>();
			QuoteLeAttributeBean quoteLeAttributeBean = new QuoteLeAttributeBean();
			Optional<QuoteToLe> optQuoteToLe = quoteToLeRepository.findById(quoteLeId);
			if (optQuoteToLe.isPresent()) {
				List<QuoteLeAttributeValue> leAttributes = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_NameIn(optQuoteToLe.get(), Arrays.asList(FPConstants.IS_FP_DONE.toString(),FPConstants.IS_PRICING_DONE.toString()));
					leAttributes.stream().forEach(le->{
						LOGGER.info("checkQuoteLeFeasibility - Getting quoteLe Attributes {} ", le.getMstOmsAttribute().getName());
						if((le.getMstOmsAttribute().getName().equals(FPConstants.IS_FP_DONE.toString()) || le.getMstOmsAttribute().getName().equals(FPConstants.IS_PRICING_DONE.toString()))
								&& le.getAttributeValue().equals("true")){
							fpStatus.put(le.getMstOmsAttribute().getName(), true);
						}
						else {
							fpStatus.put(le.getMstOmsAttribute().getName(), false);
						}
					});
				if(!fpStatus.isEmpty() && fpStatus.get(FPConstants.IS_FP_DONE.toString()).equals(false) && fpStatus.get(FPConstants.IS_FP_DONE.toString()).equals(false)) {
					if(optQuoteToLe.get().getQuoteCategory().equalsIgnoreCase(CommonConstants.QUOTE_CATEGORY_BANDWIDTH_ON_DEMAND)) {
						//Poll api until successful or failed
						LOGGER.info("checkQuoteLeFeasibility - Polling MDSO to fetch feasibility status for quoteToLe {} ", quoteLeId);
						List<QuoteNplLink> quoteNpl = nplLinkRepository.findLinks(optQuoteToLe.get().getQuote().getQuoteCode());
						
						quoteNpl.stream().forEach(link->{
							List<LinkFeasibility> linkFeasilities = linkFeasibilityRepository.findByQuoteNplLink(link);
							LinkFeasibility linkF = linkFeasilities.get(0);
							GdeScheduleDetails quoteSchedules = gdeScheduleDetailsRepository.findByLinkId(link.getId());
							try {
								
//							String feasibilityPollEndPoint = "https://10.133.100.207/bpocore/market/api/v1/resources/"+quoteSchedules.getMdsoResourceId()+"/operations/"+quoteSchedules.getMdsoFeasibilityUuid();
							String feasibilityPollEndPoint = feasibilityCheckEndPoint.replace("<resourceId>",quoteSchedules.getMdsoResourceId())+"/"+quoteSchedules.getMdsoFeasibilityUuid();
							Map<String,String> header = new HashMap<>();
							header.put("Authorization",generateMdsoAuthToken());
							header.put("Content-Type", "application/json"); 
							RestResponse feasibilityPoll = restClientService.get(feasibilityPollEndPoint, header, true);
							LOGGER.info("GDE feasibility polling response {}",feasibilityPoll.getData());
							JSONParser jsonParser = new JSONParser();
							JSONObject jsonObject = (JSONObject) jsonParser.parse(feasibilityPoll.getData());
							String feasibilityState = (String) jsonObject.get("state");
							String createdTime = (String) jsonObject.get("createdAt");
							Timestamp feasiValidity = Utils.convertMillisStringToTimeStamp(createdTime,24);
							if(feasibilityState.equalsIgnoreCase("successful")) {
								LOGGER.info("checkQuoteLeFeasibility - Polling MDSO got sucess for leid{} ", quoteLeId);
								quoteSchedules.setActivationStatus(feasibilityState);
								quoteSchedules.setFeasibilityStatus("F");
								quoteSchedules.setUpdatedTime(new Timestamp(new Date().getTime()));
								quoteSchedules.setFeasibilityValidity(feasiValidity);
								quoteSchedules.setIsActive((byte) 1);
								linkF.setIsSelected((byte) 1);
								linkF.setResponseJson(feasibilityPoll.getData());
								linkF.setRank(1);
								linkFeasibilityRepository.save(linkF);
								link.setFpStatus("F");
								nplLinkRepository.save(link);
								gdePricingCalculation(link.getId(), optQuoteToLe.get().getId(), quoteSchedules);
								gdeScheduleDetailsRepository.save(quoteSchedules);
								saveProcessState(optQuoteToLe.get(), FPConstants.IS_FP_DONE.toString(),
								FPConstants.FEASIBILITY.toString(), FPConstants.TRUE.toString());// disable the feasible flag
								saveProcessState(optQuoteToLe.get(), FPConstants.IS_PRICING_DONE.toString(),
								FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
								quoteLeAttributeBean.setQuoteLegalEntityId(quoteLeId);
								quoteLeAttributeBean.setIsFeasibilityCheckDone(FPConstants.TRUE.toString());
								quoteLeAttributeBean.setIsPricingCheckDone(FPConstants.TRUE.toString());
							}
							else if(feasibilityState.equalsIgnoreCase("failed")) {
								LOGGER.info("checkQuoteLeFeasibility - Polling MDSO  for leid {} and statue{}", quoteLeId, feasibilityState);
								quoteSchedules.setActivationStatus(feasibilityState);
								quoteSchedules.setFeasibilityStatus("N");
								quoteSchedules.setUpdatedTime(new Timestamp(new Date().getTime()));
								quoteSchedules.setFeasibilityValidity(feasiValidity);
								quoteSchedules.setIsActive((byte) 1);
								linkF.setIsSelected((byte) 1);
								linkF.setResponseJson(feasibilityPoll.getData());
								linkF.setRank(1);
								linkFeasibilityRepository.save(linkF);
								link.setFpStatus("N");
								nplLinkRepository.save(link);
								gdeScheduleDetailsRepository.save(quoteSchedules);
								saveProcessState(optQuoteToLe.get(), FPConstants.IS_FP_DONE.toString(),
										FPConstants.FEASIBILITY.toString(), FPConstants.TRUE.toString());// disable the feasible flag
								saveProcessState(optQuoteToLe.get(), FPConstants.IS_PRICING_DONE.toString(),
											FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
								quoteLeAttributeBean.setQuoteLegalEntityId(quoteLeId);
								quoteLeAttributeBean.setIsFeasibilityCheckDone(FPConstants.TRUE.toString());
								quoteLeAttributeBean.setIsPricingCheckDone(FPConstants.TRUE.toString());
								}
							else {
								LOGGER.info("checkQuoteLeFeasibility - Polling MDSO for quotetoLe {} and status {} ", quoteLeId, feasibilityState);
								saveProcessState(optQuoteToLe.get(), FPConstants.IS_FP_DONE.toString(),
								FPConstants.FEASIBILITY.toString(), FPConstants.FALSE.toString());// disable the feasible flag
								saveProcessState(optQuoteToLe.get(), FPConstants.IS_PRICING_DONE.toString(),
								FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
								quoteLeAttributeBean.setQuoteLegalEntityId(quoteLeId);
								quoteLeAttributeBean.setIsFeasibilityCheckDone(FPConstants.FALSE.toString());
								quoteLeAttributeBean.setIsPricingCheckDone(FPConstants.FALSE.toString());
								}
							} catch (TclCommonException | ParseException e) {
								LOGGER.info("Error while parsing feasibility poll api response",e);
							} 
							
						});
						
					}
				} else {
					quoteLeAttributeBean.setQuoteLegalEntityId(quoteLeId);
					quoteLeAttributeBean.setIsFeasibilityCheckDone(FPConstants.TRUE.toString());
					quoteLeAttributeBean.setIsPricingCheckDone(FPConstants.TRUE.toString());
				}
			
			}
			return quoteLeAttributeBean;
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.MDSO_FEASIBILITY_CHECK_POLL_FAILED,ResponseResource.R_CODE_ERROR);
		}
		
	}
	
	public GdeOdrScheduleAuditBean constructOdrScheduleAudit(OdrScheduleDetails orderSchedules) {
		LOGGER.info("Inside constructOdrScheduleAudit to persit audit for ordercode {} & order linkid {}",
				orderSchedules.getOrderCode(), orderSchedules.getOrderLinkId());
		OdrScheduleDetailsAudit odrAudit = new OdrScheduleDetailsAudit();
		  odrAudit.setOdrScheduleDetailId(orderSchedules.getId());
		  odrAudit.setActivationStatus(orderSchedules.getActivationStatus());
		  odrAudit.setBandwidthUnit(orderSchedules.getBandwidthUnit());
		  odrAudit.setBaseCircuitBw(orderSchedules.getBaseCircuitBw());
		  odrAudit.setScheduleOperationId(orderSchedules.getScheduleOperationId());
		  odrAudit.setBwOnDemand(orderSchedules.getBwOnDemand());
		  odrAudit.setChargeableNrc(orderSchedules.getChargeableNrc());
		  odrAudit.setCreatedTime(new Timestamp(new Date().getTime()));
		  odrAudit.setFeasibilityStatus(orderSchedules.getFeasibilityStatus());
		  odrAudit.setFeasibilityValidity(orderSchedules.getFeasibilityValidity());
		  odrAudit.setMdsoFeasibilityUuid(orderSchedules.getMdsoFeasibilityUuid());
		  odrAudit.setMdsoResourceId(orderSchedules.getMdsoResourceId());
		  odrAudit.setOrderCode(orderSchedules.getOrderCode());
		  odrAudit.setOrderLinkId(orderSchedules.getOrderLinkId());
		  odrAudit.setPaymentCurrency(orderSchedules.getPaymentCurrency());
		  odrAudit.setScheduleEndDate(orderSchedules.getScheduleEndDate());
		  odrAudit.setScheduleStartDate(orderSchedules.getScheduleStartDate());
		  odrAudit.setServiceId(orderSchedules.getServiceId());
		  odrAudit.setSlots(orderSchedules.getSlots());
		  odrAudit.setUpdatedTime(orderSchedules.getUpdatedTime());
		  odrAudit.setUpgradedBw(orderSchedules.getUpgradedBw());
		  odrAudit.setTicketId(orderSchedules.getTicketId());
		  odrAudit.setScheduleId(orderSchedules.getScheduleId());
		  odrScheduleDetailsAuditRepository.save(odrAudit);
		  return new GdeOdrScheduleAuditBean(odrAudit);
	}
	
	/**
	 * this method is to process manual fp request and update the db
	 * 
	 * @param fpRequest
	 * @param linkId
	 * @param quoteLeId
	 * @throws TclCommonException
	 */
	@Transactional
	public void processManualPricing(FPRequest fpRequest, Integer linkId, Integer quoteLeId) throws TclCommonException {
		try {
			Quote quote = null;
			Optional<QuoteNplLink> nplLink = nplLinkRepository.findById(linkId);
			Optional<ProductSolution> productSolution = productSolutionRepository
					.findById(nplLink.get().getProductSolutionId());
			if (productSolution.isPresent())
				quote = productSolution.get().getQuoteToLeProductFamily().getQuoteToLe().getQuote();
			if (fpRequest.getPricings() != null && !fpRequest.getPricings().isEmpty()) {
				Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
				if (quoteToLeEntity.isPresent()) {
					if (nplLink.isPresent()) {
						final Quote quoteFinal = quote;
						fpRequest.getPricings().stream()
								.forEach(prRequest -> saveQuotePrice(prRequest, quoteToLeEntity, linkId, quoteFinal));
						if (fpRequest.getTcv() != null) 
							nplLink.get().setTcv(fpRequest.getTcv());
						nplLink.get().setFpStatus(FPStatus.FMP.toString());
						nplLink.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 60);
						nplLink.get().setEffectiveDate(cal.getTime());
						List<QuotePrice> quotePrices = getQuotePrices(quoteToLeEntity.get().getId(),
								nplLink.get().getId());
						reCalculateLinkPrice(nplLink.get(), quotePrices);
						nplLinkRepository.save(nplLink.get());
						quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						recalculate(quoteToLeEntity.get());

					}
				}

			}
		} catch (Exception e) {
			LOGGER.warn(ExceptionUtils.getMessage(e));
			throw new TclCommonException(CommonConstants.ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void saveQuotePrice(PRequest prRequest, Optional<QuoteToLe> quoteToLeEntity, Integer linkId, Quote quote) {

		if (prRequest.getSiteQuotePriceId() != null && prRequest.getSiteQuotePriceId() != 0) {
			Optional<QuotePrice> quotePrice = quotePriceRepository.findById(prRequest.getSiteQuotePriceId());
			if (quotePrice.isPresent()) {
				LOGGER.info("Updating GDE NRC quote price {} as {} ", prRequest.getSiteQuotePriceId(), prRequest.getEffectiveNrc());
				processQuotePriceAudit(quotePrice.get(), prRequest, quote.getQuoteCode());
//				quotePrice.get().setEffectiveArc(prRequest.getEffectiveArc());
//				if (prRequest.getEffectiveArc() != null && prRequest.getEffectiveArc() != 0)
//					quotePrice.get().setEffectiveMrc(prRequest.getEffectiveArc() / 12);
//				else
//					quotePrice.get().setEffectiveMrc(0D);
				quotePrice.get().setEffectiveNrc(prRequest.getEffectiveNrc());
				quotePriceRepository.save(quotePrice.get());
			}
		} else
			updateNewPrice(quoteToLeEntity.get(), linkId, prRequest);
		GdeScheduleDetails quoteScheduleDetails = gdeScheduleDetailsRepository.findByQuoteCodeAndLinkId(quote.getQuoteCode(), linkId);
		if(quoteScheduleDetails != null) {
			LOGGER.info("Updating GdeScheduleDetails {} as {} ", quoteScheduleDetails.getLinkId(), prRequest.getEffectiveNrc());
			quoteScheduleDetails.setChargeableNrc(prRequest.getEffectiveNrc());
			gdeScheduleDetailsRepository.save(quoteScheduleDetails);
		}

	}

	/**
	 * Method to persist quote price audit
	 * @param quotePrice
	 * @param prRequest
	 * @param quoteRefId
	 */
	private void processQuotePriceAudit(QuotePrice quotePrice, PRequest prRequest, String quoteRefId) {
		if (quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveMrc() != null
				&& quotePrice.getEffectiveNrc() != null
				&& !(quotePrice.getEffectiveArc().equals(prRequest.getEffectiveArc())
						&& quotePrice.getEffectiveMrc().equals(prRequest.getEffectiveMrc())
						&& quotePrice.getEffectiveNrc().equals(prRequest.getEffectiveNrc()))) {
			QuotePriceAudit priceAudit = new QuotePriceAudit();
			priceAudit.setCreatedBy(Utils.getSource());
			priceAudit.setCreatedTime(new Timestamp(new Date().getTime()));
			priceAudit.setFromArcPrice(quotePrice.getEffectiveArc());
			priceAudit.setToArcPrice(prRequest.getEffectiveArc());
			priceAudit.setFromMrcPrice(quotePrice.getEffectiveMrc());
			priceAudit.setToMrcPrice(prRequest.getEffectiveMrc());
			priceAudit.setFromNrcPrice(quotePrice.getEffectiveNrc());
			priceAudit.setToNrcPrice(prRequest.getEffectiveNrc());
			priceAudit.setQuotePrice(quotePrice);
			priceAudit.setQuoteRefId(quoteRefId);
			quotePriceAuditRepository.save(priceAudit);
		}
	}
	
	private void updateNewPrice(QuoteToLe quoteToLe, Integer linkId, PRequest request) {
		String type = request.getType();
		Integer referenceId = linkId;
		Optional<QuoteNplLink> nplLinkOptional = nplLinkRepository.findById(linkId);
		QuoteNplLink nplLink = nplLinkOptional.orElse(new QuoteNplLink());
		if (type.equalsIgnoreCase("Site-A"))
			referenceId = nplLink.getSiteAId();
		else if (type.equalsIgnoreCase("Site-B"))
			referenceId = nplLink.getSiteBId();
		MstProductComponent mstComponent = mstProductComponentRepository.findByName(request.getComponentName());
		Optional<QuoteProductComponent> componentOptional = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(referenceId, mstComponent, request.getType());
		if (componentOptional.isPresent()) {
			QuotePrice attrPrice;
			attrPrice = new QuotePrice();
			attrPrice.setQuoteId(quoteToLe.getQuote().getId());
			attrPrice.setReferenceId(String.valueOf(componentOptional.get().getId()));
			attrPrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
			attrPrice.setEffectiveMrc(request.getEffectiveMrc());
			attrPrice.setEffectiveNrc(request.getEffectiveNrc());
			attrPrice.setEffectiveArc(request.getEffectiveArc());
			attrPrice.setMstProductFamily(componentOptional.get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);
		}
	}
	
	private List<QuotePrice> getQuotePrices(Integer quoteLeEntityId, Integer linkId) {
		Optional<QuoteNplLink> nplLinkOptional = nplLinkRepository.findById(linkId);
		QuoteNplLink nplLink = nplLinkOptional.orElse(new QuoteNplLink());
		List<QuoteProductComponent> componentList = quoteProductComponentRepository.findByReferenceIdAndType(linkId,
				"Link");
		componentList.addAll(quoteProductComponentRepository.findByReferenceIdAndType(nplLink.getSiteAId(), "Site-A"));
		componentList.addAll(quoteProductComponentRepository.findByReferenceIdAndType(nplLink.getSiteBId(), "Site-B"));
		List<QuotePrice> quotePrices = new ArrayList<>();
		if (!componentList.isEmpty()) {
			quotePrices.addAll(componentList.stream().map((component) -> {
				LOGGER.info("Getting the quotePrice for referenceId {}", component.getId());
				QuotePrice quotePriceEntity = quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString());
				return quotePriceEntity;
			}).collect(Collectors.toList()));
		}
		return quotePrices;
	}
	
	/**
	 * 
	 * recalculate
	 * 
	 * @param quoteToLe
	 */
	private void recalculate(QuoteToLe quoteToLe) {
		Double totalMrc = 0.0D;
		Double totalNrc = 0.0D;
		Double totalArc = 0.0D;
		Double totalTcv = 0.0D;
		Set<QuoteToLeProductFamily> quoteProductFamily = quoteToLe.getQuoteToLeProductFamilies();
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteProductFamily) {
			Set<ProductSolution> productSolutions = quoteToLeProductFamily.getProductSolutions();
			for (ProductSolution productSolution : productSolutions) {
				Set<QuoteNplLink> quoteNplLinks = new HashSet<QuoteNplLink>(nplLinkRepository.findByProductSolutionId(productSolution.getId()));
				for (QuoteNplLink quoteNplLink : quoteNplLinks) {
					totalMrc = totalMrc + (quoteNplLink.getMrc() != null ? quoteNplLink.getMrc() : 0D);
					totalNrc = totalNrc + (quoteNplLink.getNrc() != null ? quoteNplLink.getNrc() : 0D);
					totalArc = totalArc + (quoteNplLink.getArc() != null ? quoteNplLink.getArc() : 0D);
					totalTcv = totalTcv + (quoteNplLink.getTcv() != null ? quoteNplLink.getTcv() : 0D);
				}
			}

		}
		quoteToLe.setProposedMrc(totalMrc);
		quoteToLe.setProposedNrc(totalNrc);
		quoteToLe.setProposedArc(totalArc);
		quoteToLe.setTotalTcv(totalTcv);
		quoteToLe.setFinalMrc(totalMrc);
		quoteToLe.setFinalNrc(totalNrc);
		quoteToLe.setFinalArc(totalArc);
		quoteToLeRepository.save(quoteToLe);
	}
	
	/**
	 * ReCalculateSitePrice
	 * 
	 * @param illSite
	 * @param quotePrices
	 */
	private void reCalculateLinkPrice(QuoteNplLink quoteNplLink, List<QuotePrice> quotePrices) {
		Double effecArc = 0D;
		Double effecMrc = 0D;
		Double effecNrc = 0D;
		for (QuotePrice quotePrice : quotePrices) {
			if (quotePrice != null) {
				effecArc = effecArc + (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
				effecMrc = effecMrc + (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
				effecNrc = effecNrc + (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
			}
		}
		quoteNplLink.setMrc(effecMrc);
		quoteNplLink.setArc(effecArc);
		quoteNplLink.setNrc(effecNrc);
	}
	
	
}

