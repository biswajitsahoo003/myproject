package com.tcl.dias.oms.termination.service.v1;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.DiscountComponent;
import com.tcl.dias.oms.beans.PDRequest;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.beans.TerminationWaiverRequest;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.macd.beans.TerminationResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * TerminationWaiverService file
 * 
 *
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class TerminationWaiverService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(TerminationWaiverService.class);
	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	OmsSfdcUtilService omsSfdcService;
	
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	MstProductComponentRepository mstProductComponentRepository;
	
	@Autowired
	QuotePriceRepository quotePriceRepository;
	
	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;
	
	@Autowired
	TerminationService terminationService;
	
	public TerminationResponse updateTerminationWaiverDetails(TerminationWaiverRequest request) throws TclCommonException {
		
		LOGGER.info("updateTerminationWaiverDetails request {}", request);
		TerminationResponse response = new TerminationResponse();
		validateTerminationRequest(request);
		
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(request.getQuoteToLeId());
		if(!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		
		request.getTerminationWaiverDetails().stream().forEach(service -> {
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(service.getServiceId(), quoteToLe.get());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToServiceList.get(0));
				if(quoteSiteServiceTerminationDetail != null) {
					LOGGER.info("service id {} - sitetoserviceid {}", service.getServiceId(), quoteIllSiteToServiceList.get(0).getId());
					quoteSiteServiceTerminationDetail.setProposedEtc(service.getProposedEtc());
					quoteSiteServiceTerminationDetail.setWaiverType(service.getEtcWaiverType());
					quoteSiteServiceTerminationDetail.setWaiverPolicy(service.getEtcWaiverPolicy());
					quoteSiteServiceTerminationDetail.setWaiverRemarks(service.getWaiverRemarks());
					quoteSiteServiceTerminationDetail.setCompensatoryDetails(service.getCompensatoryDetails());
					User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
					quoteSiteServiceTerminationDetail.setUpdatedBy(user.getId());
					quoteSiteServiceTerminationDetail.setUpdatedTime(new Date());
					quoteSiteServiceTerminationDetailsRepository.save(quoteSiteServiceTerminationDetail);

					try {
						List<ThirdPartyServiceJob> createWaiverList = thirdPartyServiceJobsRepository.findByServiceRefIdAndServiceTypeAndRefIdAndThirdPartySource(quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId(), 
								SFDCConstants.CREATE_WAIVER, quoteToLe.get().getQuote().getQuoteCode(), ThirdPartySource.SFDC.toString());
						if(createWaiverList == null || createWaiverList.isEmpty()) {
						omsSfdcService.processCreateWaiverForTermination(quoteToLe.get() , quoteSiteServiceTerminationDetail);
						}
						
						String productName = getProductFamilyNameForQuoteToLe(quoteToLe.get());
						if (quoteToLe.isPresent() && productName != null && !productName.isEmpty() && (quoteToLe.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit()))) {
							omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.get().getTpsSfdcOptyId(), SFDCConstants.IDENTIFIED_OPTY_STAGE, quoteToLe.get());
						} else {
							
							List<ThirdPartyServiceJob> optyList = thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, quoteToLe.get().getTpsSfdcOptyId());
							if(optyList != null && !optyList.isEmpty()) {
								// Create parent dummy opportunity
								omsSfdcService.processUpdateOptyDummy(quoteToLe.get(), optyList.get(0).getServiceRefId(),  String.valueOf(quoteToLe.get().getTpsSfdcParentOptyId()), new Date(), quoteToLe.get().getTpsSfdcOptyId(), SFDCConstants.IDENTIFIED_OPTY_STAGE); 
					
							}
						}
					} catch (TclCommonException e) {
						LOGGER.error("error in processing sfdc create waiver for termination {}", e);
						e.printStackTrace();
					}
				} else {
					LOGGER.error("quoteSiteServiceTerminationDetail entry not present!!!");
				}
				
			}
		});
		response.setQuoteId(request.getQuoteId());
		response.setQuoteToLeId(quoteToLe.get().getId());
		return response;
	}
	
	public TerminationResponse updateTerminationWaiverApproval(TerminationWaiverRequest request) throws TclCommonException {
		
		LOGGER.info("updateTerminationWaiverDetails request {}", request);
		TerminationResponse response = new TerminationResponse();
		validateTerminationRequest(request);
		
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(request.getQuoteToLeId());
		if(!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		
		request.getTerminationWaiverDetails().stream().forEach(service -> {
			Double previousEtc = 0D;
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(service.getServiceId(), quoteToLe.get());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToServiceList.get(0));
				if(quoteSiteServiceTerminationDetail != null) {
					LOGGER.info("service id {} - sitetoserviceid {}", service.getServiceId(), quoteIllSiteToServiceList.get(0).getId());
					previousEtc = (quoteSiteServiceTerminationDetail.getFinalEtc() != null) ? quoteSiteServiceTerminationDetail.getFinalEtc() : 0D;
					quoteSiteServiceTerminationDetail.setFinalEtc(service.getFinalEtc());
					quoteSiteServiceTerminationDetail.setProposedBySales(service.getProposedBySales());
					if(service.getEtcWaiverType() != null)
						quoteSiteServiceTerminationDetail.setWaiverType(service.getEtcWaiverType());
					quoteSiteServiceTerminationDetail.setWaiverApprovalRemarks(service.getWaiverApprovalRemarks());
					if(service.getEtcWaiverPolicy() != null){
						quoteSiteServiceTerminationDetail.setWaiverPolicy(service.getEtcWaiverPolicy());
					}
					User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
					quoteSiteServiceTerminationDetail.setUpdatedBy(user.getId());
					quoteSiteServiceTerminationDetail.setUpdatedTime(new Date());
					quoteSiteServiceTerminationDetailsRepository.save(quoteSiteServiceTerminationDetail);


					try {
						String[] productSolution =  { null };
						quoteToLe.get().getQuoteToLeProductFamilies().stream().forEach(quoteToProductFamily -> {
							quoteToProductFamily.getProductSolutions().stream().forEach(prodSol -> {
								productSolution[0] = prodSol.getMstProductOffering().getProductName();
							});
						});

						if(productSolution[0] != null && !CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(productSolution[0])) {
							omsSfdcService.processUpdateWaiverForTermination(quoteToLe.get() , quoteSiteServiceTerminationDetail);
						}
						if(productSolution[0] != null && CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(productSolution[0])) {
							processETCComponentNewAttributePrice(quoteToLe.get());
							 terminationService.regenerateAndUploadTRFToStorage(quoteToLe.get().getQuote().getId(), quoteToLe.get().getId());
						}
						String sfdcStage = null;
						if(quoteToLe.get().getStage() != null && MACDConstants.TERMINATION_REQUEST_RECEIVED.equalsIgnoreCase(quoteToLe.get().getStage())) {
							sfdcStage = SFDCConstants.IDENTIFIED_OPTY_STAGE;
						} else {
							sfdcStage = SFDCConstants.VERBAL_AGREEMENT_STAGE;
						}
						String productName = getProductFamilyNameForQuoteToLe(quoteToLe.get());
						if (quoteToLe.isPresent() && productName != null && !productName.isEmpty() && (quoteToLe.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit()))) {
							omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.get().getTpsSfdcOptyId(), sfdcStage, quoteToLe.get());
						} else {
							
							List<ThirdPartyServiceJob> optyList = thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, quoteToLe.get().getTpsSfdcOptyId());
							if(optyList != null && !optyList.isEmpty()) {
								// Create parent dummy opportunity
								omsSfdcService.processUpdateOptyDummy(quoteToLe.get(), optyList.get(0).getServiceRefId(),  String.valueOf(quoteToLe.get().getTpsSfdcParentOptyId()), new Date(), quoteToLe.get().getTpsSfdcOptyId(), sfdcStage); 
					
							}
						}

						LOGGER.info("Current ETC {} Previous ETC {}", service.getFinalEtc(), previousEtc);
						if(service.getFinalEtc()!=null){
							if(!service.getFinalEtc().equals(previousEtc)){
								LOGGER.info("Before method sendEtcRevisionMailForTermination");
								terminationService.sendEtcRevisionMailForTermination(request.getQuoteId(),request.getQuoteToLeId(),service.getServiceId());
							}
						}
						
					} catch (TclCommonException e) {
						LOGGER.error("error in processing sfdc update waiver for termination {}", e);
						e.printStackTrace();
					}
				} else {
					LOGGER.error("quoteSiteServiceTerminationDetail entry not present!!!");
				}
				
			}
		});
		response.setQuoteId(request.getQuoteId());
		response.setQuoteToLeId(quoteToLe.get().getId());
		return response;
	}	
	
	private void validateTerminationRequest(TerminationWaiverRequest request) throws TclCommonException {
		if(request.getQuoteId() == null || request.getQuoteToLeId() == null || request.getTerminationWaiverDetails() == null) {
			LOGGER.info("Validation error occured for the termination waiver request");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,  ResponseResource.R_CODE_ERROR);
		}
	}
	
	private String getProductFamilyNameForQuoteToLe(QuoteToLe quoteToLe) {
		return quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
	}
	
	/**
	 * process processETCComponentNewAttributePrice
	 * @param PDRequest
	 * @throws TclCommonException 
	 */
	private void processETCComponentNewAttributePrice(QuoteToLe quoteToLe) throws TclCommonException {
		LOGGER.info("Saving ETC price for the components and attributes in quote price.");
		try {
			MstProductComponent etcChargesComponent = mstProductComponentRepository.findByName(ChargeableItemConstants.ETC_CHARGES);
			quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteLeProdFam -> {
				quoteLeProdFam.getProductSolutions().stream().forEach(quoteProdSol -> {
					quoteProdSol.getQuoteIllSites().stream().forEach(qSites ->{
						List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite_Id(qSites.getId());
						QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetails = quoteSiteServiceTerminationDetailsRepository.
								findByQuoteIllSiteToService(quoteIllSiteToServiceList.get(0));
						Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository
								.findByReferenceIdAndMstProductComponentAndType(qSites.getId(), etcChargesComponent,
										"primary");
					
						LOGGER.info("Saving component values : ");
						if (etcChargesComponent.getName().equalsIgnoreCase(ChargeableItemConstants.ETC_CHARGES)) {
							processQuoteETCPrice(quoteSiteServiceTerminationDetails.getFinalEtc(), QuoteConstants.COMPONENTS.toString(), quoteToLe.getQuote().getId(),
									quoteProductComponent.get().getId().toString());
						}
					});
				}); 
				});
				
			

		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	private void processQuoteETCPrice(Double Nrc, String refName, Integer quoteId, String refId) {
		
		QuoteToLe quoteToLe=quoteToLeRepository.findByQuote_Id(quoteId).get(0);
		QuotePrice price=quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(refName, refId, quoteId);
		
		PRequest prequest = new PRequest();
		prequest.setEffectiveNrc(Nrc);
		
		
		if(price!=null) {
			processQuotePriceAudit(price, prequest, quoteToLe.getQuote().getQuoteCode());
			if(Nrc!=null)
				price.setEffectiveNrc(Nrc);
			quotePriceRepository.save(price);
		}
		else {
			QuotePrice attrPrice = new QuotePrice();
			attrPrice.setQuoteId(quoteId);
			attrPrice.setReferenceId(refId);
			attrPrice.setReferenceName(refName);
			if(Nrc!=null)
				attrPrice.setEffectiveNrc(Nrc);
			
			attrPrice.setMstProductFamily(quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);
		}
	}
	

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

}
