package com.tcl.dias.oms.sfdc.service;

import static com.tcl.dias.common.constants.CommonConstants.*;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_PRODUCT_NAME;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GVPN;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.IAS;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_OPTY;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.Mf3DResponse;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteOpportunityLocation;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.ManualFeasibilitySiteBean;
import com.tcl.dias.oms.beans.O2CSubCategoryBean;
import com.tcl.dias.oms.beans.SFDCCommercialBifurcationBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.Opportunity;
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
import com.tcl.dias.oms.factory.impl.OmsSfdcGscMapper;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcPricingFeasibilityService;
import com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.service.v1.BundleOmsSfdcService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.renewals.bean.RenewalsSfdcObjectBean;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional
public class OmsSfdcService extends OmsSfdcUtilService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcService.class);

	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;

	@Autowired
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;

	@Autowired
	PartnerService partnerService;

	@Autowired
	OmsSfdcGscMapper omsSfdcGscMapper;

	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;
	
	@Autowired
	BundleOmsSfdcService bundleOmsSfdcService;

	@Autowired
	CancellationService cancellationService;
	
	@Autowired
	IzoPcPricingFeasibilityService izoPcPricingFeasibilityService;


	/**
	 * 
	 * 
	 * /** Update Product for GSC
	 *
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processUpdateProductForGSC(QuoteToLe quoteToLe) throws TclCommonException {
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilies) {
			if (GSC_PRODUCT_NAME.equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName())
					|| GSC_ORDER_PRODUCT_COMPONENT_TYPE
							.equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName())) {
				List<ProductSolution> productSolutions = productSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProductFamily);
				ProductSolution productSolution = productSolutions.stream().findFirst().get();
				ProductServiceBean productServiceBean = omsSfdcGscMapper.updateProductServiceInput(quoteToLe,
						productSolution);
				String request = Utils.convertObjectToJson(productServiceBean);
				LOGGER.info("Input for updating the product Details {}", request);
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
						StringUtils.isNotBlank(productSolution.getTpsSfdcProductId()) ? CommonConstants.BACTIVE
								: CommonConstants.BDEACTIVATE,
						SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT));
				break;
			}
		}

	}

	/**
	 * Method to set Partner SFDC AccountID
	 *
	 * @param opportunity
	 * @param opportunityBean
	 */
	public void setPartnerAccountId18(Opportunity opportunity, OpportunityBean opportunityBean) {
		Integer erfPartnerId = engagementOpportunityRepository.findByOpportunity(opportunity).getEngagement()
				.getPartner().getErfCusPartnerId();
		PartnerDetailsBean partnerDetails = partnerService.getPartnerDetailsMQ(erfPartnerId);
		opportunityBean.setAccountId(partnerDetails.getAccountId18());
	}

	/**
	 * To set Customer SFDC Account ID
	 *
	 * @param opportunityBean
	 * @param engagement
	 */
	public void setCustomerAccountID18(OpportunityBean opportunityBean, Engagement engagement) {
		CustomerBean customerBean = partnerService
				.getCustomerDetailsMQCall(String.valueOf(engagement.getCustomer().getErfCusCustomerId()));
		if (Objects.nonNull(customerBean)) {
			if (!CollectionUtils.isEmpty(customerBean.getCustomerDetailsSet())) {
				String SFDCAccountId = customerBean.getCustomerDetailsSet().stream().findAny().get().getSFDCAccountId();
				LOGGER.info("SFDC Account ID :: {}", SFDCAccountId);
				opportunityBean.setAccountId(SFDCAccountId);
			}
		}
	}

	/**
	 * To set customer contracting id
	 *
	 * @param opportunityBean
	 * @param engagementToOpportunity
	 * @return
	 */
	public Engagement setCustomerContractingId(OpportunityBean opportunityBean,
			EngagementToOpportunity engagementToOpportunity) {
		Engagement engagement = engagementToOpportunity.getEngagement();
		if (Objects.nonNull(engagement.getErfCusCustomerLeId())) {
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = partnerService
					.getCustomerLeDetailsMQCall(engagement.getErfCusCustomerLeId().toString());
			if (!CollectionUtils.isEmpty(customerLegalEntityDetailsBean.getCustomerLeDetails())) {
				LOGGER.info("SFDC ID :: {}",
						customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getSfdcId());
				opportunityBean.setCustomerContractingId(
						customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getSfdcId());
			}
		}
		return engagement;
	}

	/**
	 * To set Partner contracting Id
	 *
	 * @param opportunity
	 * @param opportunityBean
	 * @return
	 */
	public EngagementToOpportunity setPartnerContractingId(Opportunity opportunity, OpportunityBean opportunityBean) {
		EngagementToOpportunity engagementToOpportunity = engagementOpportunityRepository
				.findByOpportunityCode(opportunity.getUuid());
		if (Objects.nonNull(engagementToOpportunity)) {
			LOGGER.info("Engagement ID :: {}", engagementToOpportunity.getEngagement().getId());
			LOGGER.info("EngagementToOpportunity ID :: {}", engagementToOpportunity.getId());
			List<PartnerLegalEntityBean> partnerLegalEntiy = partnerService
					.getPartnerLegalEntiy(engagementToOpportunity.getId());
			opportunityBean.setPartnerContractingId(partnerLegalEntiy.stream().findFirst().get().getTpsSfdcCuid());
			LOGGER.info("Partner Legal Entity SFDC CUID :: {}",
					partnerLegalEntiy.stream().findFirst().get().getTpsSfdcCuid());
		}
		return engagementToOpportunity;
	}

	public void processMFTask(List<QuoteToLe> quoteLes) {
		try {
			// Code to create MF task for quote which got MF failed due to opty id empty
			List<ManualFeasibilitySiteBean> illManualFeasibilitySiteBeans = new ArrayList<>();
			List<ManualFeasibilitySiteBean> gvpnManualFeasibilitySiteBeans = new ArrayList<>();
			quoteLes.stream().forEach(quoteLe -> {
				quoteLe.getQuoteToLeProductFamilies().stream().forEach(prodFamily -> {
					if (CommonConstants.IAS.equalsIgnoreCase(prodFamily.getMstProductFamily().getName())
							|| CommonConstants.GVPN.equalsIgnoreCase(prodFamily.getMstProductFamily().getName())) {
						LOGGER.info("Inside processOptyResponse - prod family {} ",
								prodFamily.getMstProductFamily().getName());
						prodFamily.getProductSolutions().stream().forEach(prodSol -> {
							prodSol.getQuoteIllSites().stream().forEach(illSite -> {
								if (illSite.getMfTaskType() != null) {
									LOGGER.info("Inside processOptyResponse - siteID {} and mfTaskType {} ",
											illSite.getId(), illSite.getMfTaskType());
									ManualFeasibilitySiteBean manualFeasibilitySiteBeanPri = new ManualFeasibilitySiteBean();
									ManualFeasibilitySiteBean manualFeasibilitySiteBeanSec = new ManualFeasibilitySiteBean();
									String[] productAndType = illSite.getMfTaskType().split("_");
									String product = productAndType[0];
									String siteType = productAndType[1];

									manualFeasibilitySiteBeanPri.setSiteId(illSite.getId());
									manualFeasibilitySiteBeanPri.setSiteType("primary");
									if (siteType.equalsIgnoreCase("DUAL")) {
										manualFeasibilitySiteBeanSec.setSiteId(illSite.getId());
										manualFeasibilitySiteBeanSec.setSiteType("secondary");
									}
									if (product.equalsIgnoreCase(CommonConstants.IAS)) {
										illManualFeasibilitySiteBeans.add(manualFeasibilitySiteBeanPri);
										illManualFeasibilitySiteBeans.add(manualFeasibilitySiteBeanSec);
									}

									if (product.equalsIgnoreCase(CommonConstants.GVPN)) {
										gvpnManualFeasibilitySiteBeans.add(manualFeasibilitySiteBeanPri);
										gvpnManualFeasibilitySiteBeans.add(manualFeasibilitySiteBeanSec);
									}
								}
							});
							try {
								if (!illManualFeasibilitySiteBeans.isEmpty()) {
									LOGGER.info("Inside processOptyResponse - IAS Mf task for quoteLe {} and input {} ",
											quoteLe.getId(), illManualFeasibilitySiteBeans);
									illPricingFeasibilityService.processManualFeasibilityRequest(
											illManualFeasibilitySiteBeans, quoteLe.getId());
								}
								if (!gvpnManualFeasibilitySiteBeans.isEmpty()) {
									LOGGER.info("Inside processOptyResponse - GVPN Mf task for quoteLe {} and input {}",
											quoteLe.getId(), gvpnManualFeasibilitySiteBeans);
									gvpnPricingFeasibilityService.processManualFeasibilityRequest(
											gvpnManualFeasibilitySiteBeans, quoteLe.getId());
								}

							} catch (TclCommonException e) {
								LOGGER.error(
										" processSfdcOpportunityCreateResponse Exception while Re-triggering MF for site id {} failed due to opty id empty ",
										e);
							}
						});
					}

				});
			});
		} catch (Exception e) {
			LOGGER.error("Error in creating mf task", e);
		}
	}

	/**
	 * @return ResponseResource
	 * @throws TclCommonException
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to update the
	 *            order type and subtype in SFDC
	 */

	public OpportunityBean updateOrderTypeAndSubtype(UpdateRequest request) throws TclCommonException {
		O2CSubCategoryBean subCategory = new O2CSubCategoryBean();
		Optional<QuoteToLe> quoteToLeOptional = quoteToLeRepository.findById(request.getQuoteToLe());
		Optional<QuoteIllSite> illSiteOpt = illSiteRepository.findById(request.getSiteId());
		LOGGER.info("In SFDC subtype update method");
		Quote quote = quoteToLeOptional.get().getQuote();
		Optional<String> productName = quoteToLeOptional
				.get().getQuoteToLeProductFamilies().stream().map(QuoteToLeProductFamily::getMstProductFamily)
				.map(MstProductFamily::getName).filter(product -> product.equalsIgnoreCase(IAS)
						|| product.equalsIgnoreCase(GVPN) || product.equalsIgnoreCase(SFDCConstants.NPL)||
						product.equalsIgnoreCase(SFDCConstants.NDE)||
						product.equalsIgnoreCase(SFDCConstants.IZOPC))
				.findFirst();

		List<SIServiceInfoBean> siServiceInfoResponse = null;
		SIServiceInfoBean[] siDetailedInfoResponseIAS = null;
		OpportunityBean opportunityBean = new OpportunityBean();
		String llBwChange = null;
		String portBwChange = null;
		SIOrderDataBean sIOrderDataBean = null;
		List<SIServiceDetailDataBean> servicesList = null;
		// String interfaceChanged = null;
		SIServiceDetailDataBean sIServiceDetailDataBean = null;
		String cityType = null;
		String[] siteEnd = { null };

		if (MACDConstants.ADD_IP_SERVICE.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
			LOGGER.info("Entering ADD IP Sceanrio");
			opportunityBean.setType(MACDConstants.OTHERS_SFDC);
			opportunityBean.setSubType(MACDConstants.OTHERS_SFDC);
			subCategory.setO2cOrderType(MACDConstants.HOT_UPGRADE);
			subCategory.setO2cOrderSubType(MACDConstants.HOT_UPGRADE);
			LOGGER.info("Value of Order Type after Add ip{}", opportunityBean.getType());
		} else if (MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
			LOGGER.info("Entering ADD Site Sceanrio");
			opportunityBean.setType(MACDConstants.ADDITION_OF_SITE);
			opportunityBean.setSubType(MACDConstants.ADDITION_OF_SITE);
			subCategory.setO2cOrderType(MACDConstants.ADDITION_OF_SITE);
			subCategory.setO2cOrderSubType(MACDConstants.ADDITION_OF_SITE);
			LOGGER.info("Value of Order Type after Add site{}", opportunityBean.getType());

		} else if (MACDConstants.OTHERS.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
			//If considering ChangeOrder in "Others" quoteCategory
			LOGGER.info("Entering Others Scenario");
			opportunityBean.setType(SFDCConstants.OTHERS_SFDC);
			opportunityBean.setSubType("");			
			LOGGER.info("Value of Order Type after Others{}", opportunityBean.getType());
		} else if (MACDConstants.QUOTE_CATEGORY_CO.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory()) && "Y".equalsIgnoreCase(quote.getNsQuote())) {
			//Need to check in UAT SFDC
			opportunityBean.setType(MACDConstants.OTHERS_SFDC);
			opportunityBean.setSubType(MACDConstants.COS_CHANGE);
			subCategory.setO2cOrderType(MACDConstants.CHANGE_ORDER);
			subCategory.setO2cOrderSubType(MACDConstants.COS_CHANGE);
		} else {
			LOGGER.info("NSO or Standard Shiftsite and ChangeBandwidth{}",quoteToLeOptional.get().getQuoteCategory());
			List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository
					.findByQuoteToLe_Id(quoteToLeOptional.get().getId());
			QuoteIllSiteToService quoteIllSiteToService = quoteIllSiteToServices.stream().findFirst().get();
			Integer siParentOrderId = quoteIllSiteToService.getErfServiceInventoryParentOrderId();
			LOGGER.info("Si Parent order id is -----> {}  for quote -----> {} and quote site ----> {} ",
					Optional.ofNullable(siParentOrderId),
					quoteIllSiteToService.getQuoteToLe().getQuote().getQuoteCode(),
					quoteIllSiteToService.getQuoteIllSite());

			if (CommonConstants.NPL.equalsIgnoreCase(productName.get()) || CommonConstants.NDE.equalsIgnoreCase(productName.get())) {	
				servicesList = macdUtils
						.getServiceDetailNPL(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
			} else
				sIOrderDataBean = macdUtils.getSiOrderData(String.valueOf(siParentOrderId));

			if (Objects.nonNull(sIOrderDataBean)) {
				LOGGER.info("Si Order Data Bean is not null and COPF id is :::: {}", sIOrderDataBean.getTpsCrmCofId());
				opportunityBean.setCopfIdC(sIOrderDataBean.getTpsCrmCofId());
			}

			if (servicesList != null && !servicesList.isEmpty()) {
				LOGGER.info("NPL COPF id is :::: {}", servicesList.get(0).getTpsCrmCofId());
				opportunityBean.setCopfIdC(String.valueOf(servicesList.get(0).getTpsCrmCofId()));
			}
			// Need to be discussed with Akhil for the selection of serviceId
			String serviceId = quoteIllSiteToService.getErfServiceInventoryTpsServiceId();
			/*
			 * if(CommonConstants.NPL.equalsIgnoreCase(productName.get())) {
			 * interfaceChanged = getInterfaceChangeNPL(illSiteOpt, siServiceInfoResponse,
			 * productName, servicesList, request.getLinkId()); } else { interfaceChanged =
			 * getInterfaceChange(illSiteOpt, siServiceInfoResponse, productName,
			 * sIServiceDetailDataBean); }
			 */
			if (!NPL.equalsIgnoreCase(productName.get()) && !CommonConstants.NDE.equalsIgnoreCase(productName.get())) {
				sIServiceDetailDataBean = macdUtils.getServiceDetail(serviceId,
						quoteToLeOptional.get().getQuoteCategory());
			}
			/*
			 * Integer serviceDetailid =
			 * quoteToLeOptional.get().getErfServiceInventoryServiceDetailId();
			 */

			String crossConnectType = null;
			if(CommonConstants.NPL.equalsIgnoreCase(productName.get()) || CommonConstants.NDE.equalsIgnoreCase(productName.get()) && servicesList != null && !servicesList.isEmpty()) {
				crossConnectType=servicesList.get(0).getCrossConnectType();
			}
			if(!Objects.nonNull(crossConnectType)||!crossConnectType.equals("Passive")) {
				if ((CommonConstants.NPL.equalsIgnoreCase(productName.get()) || CommonConstants.NDE.equalsIgnoreCase(productName.get()))
						&& !illSiteOpt.get().getProductSolution().getMstProductOffering().getProductName().equals("MMR Cross Connect")) {
					llBwChange = getLlBwChangeNPL(illSiteOpt, serviceId, request.getLinkId());
				} else
					llBwChange = getLlBwChange(illSiteOpt, serviceId);
				if ((CommonConstants.NPL.equalsIgnoreCase(productName.get()) || CommonConstants.NDE.equalsIgnoreCase(productName.get()))
						&& !illSiteOpt.get().getProductSolution().getMstProductOffering().getProductName().equals("MMR Cross Connect")) {
					portBwChange = getPortBwChangeNPL(illSiteOpt, serviceId, quoteToLeOptional, request.getLinkId());
				} else
					portBwChange = getPortBwChange(illSiteOpt, serviceId, quoteToLeOptional);
				LOGGER.info("LL BW CHANGE IS  {}", llBwChange);
				LOGGER.info("PORT BW CHANGE IS  {}", portBwChange);
			}
			String parallelRunDays = getParallelRundays(request, quoteToLeOptional);

			LOGGER.info("After executing parallel rundays attribute value is {}", parallelRunDays);
			String parallelRundays = "";

			if (!"".equalsIgnoreCase(parallelRunDays) && !"0".equalsIgnoreCase(parallelRunDays)) {
				parallelRundays = Objects.nonNull(parallelRunDays) ? parallelRunDays : NONE;
			} else
				parallelRundays = NONE;
			String upgradeOrDowngradeBwChange = productName.get().equalsIgnoreCase(SFDCConstants.IZOPC)?isUpgradeOrDowngrade(llBwChange):isUpgradeOrDowngrade(llBwChange, portBwChange);
			LOGGER.info("Value of upgradeOrDowngradeBwChange is {}", upgradeOrDowngradeBwChange);
			if ((CommonConstants.NPL.equalsIgnoreCase(productName.get()) || CommonConstants.NDE.equalsIgnoreCase(productName.get()))
			&&!illSiteOpt.get().getProductSolution().getMstProductOffering().getProductName().equals("MMR Cross Connect")) {
				Optional<QuoteNplLink> quoteNplLinkOpt = quoteNplLinkRepository.findById(request.getLinkId());
				if (quoteNplLinkOpt.isPresent()) {
					if (illSiteOpt.get().getId().equals(quoteNplLinkOpt.get().getSiteAId())) {
						siteEnd[0] = MACDConstants.SI_SITEA;
					} else if (illSiteOpt.get().getId().equals(quoteNplLinkOpt.get().getSiteBId())) {
						siteEnd[0] = MACDConstants.SI_SITEB;
					}
				}
				sIServiceDetailDataBean = servicesList.stream()
						.filter(serviceDetail -> serviceDetail.getSiteType().equalsIgnoreCase(siteEnd[0])).findFirst()
						.get();
				LOGGER.info("site End {}", siteEnd[0]);
				cityType = getCityType(sIServiceDetailDataBean, illSiteOpt.get());
			} else {
				if(illSiteOpt.get().getProductSolution().getMstProductOffering().getProductName().equals("MMR Cross Connect")){
					sIServiceDetailDataBean = servicesList.stream().findFirst().get();
				}
				cityType = getCityType(sIServiceDetailDataBean, illSiteOpt.get());
			}
			LOGGER.info("Value of cityType is {}", cityType);

			if (CommonConstants.BDEACTIVATE.equals(quoteToLeOptional.get().getIsMultiCircuit())) {

				if (MACDConstants.CHANGE_BANDWIDTH_SERVICE
						.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
					String isBSOChanged = isBSOChanged(sIServiceDetailDataBean, illSiteOpt.get(), productName.get(),
							quoteIllSiteToService.getType());
					LOGGER.info("isBSOChanged :: {}", isBSOChanged);
					evaluateO2COrderSubCategoryForCB(isBSOChanged, upgradeOrDowngradeBwChange, parallelRundays,
							subCategory);
					getTypeBasedOnBWChangeAndParallelRundays(opportunityBean, upgradeOrDowngradeBwChange,
							parallelRundays);
				}

				if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
					// Parallel Upgrade and Downgrade for Shift Service - Intra(4cases)
					isParallelUpgradeOrDowngradeForIntra(opportunityBean, upgradeOrDowngradeBwChange, cityType);
					// Intercity shifting with/without parallelRundays(4cases)
					orderTypeForShiftWithoutBwChange(opportunityBean, parallelRundays, cityType,
							upgradeOrDowngradeBwChange);
					String isBSOChanged = isBSOChanged(sIServiceDetailDataBean, illSiteOpt.get(), productName.get(),
							quoteIllSiteToService.getType());
					LOGGER.info("isBSOChanged :: {}", isBSOChanged);
					evaluateO2CSubCategoryForSS(subCategory, upgradeOrDowngradeBwChange, cityType, parallelRundays,
							isBSOChanged);
					//nde intracity shifting with same ehs id or diff ehs id
					if (CommonConstants.NDE.equalsIgnoreCase(productName.get())) {
						SIServiceInfoBean bean = macdUtils
								.getServicAttributesNde(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
						LOGGER.info("service inventory data for nde" + bean.getEthernetFlavour());
						if(bean.getEthernetFlavour()!=null) {
						if (bean.getEthernetFlavour().equalsIgnoreCase("Hub National Dedicated Ethernet")) {
							LOGGER.info("inside if hub" + bean.getIsHub());
							if (bean.getIsHub()!=null) {
								if (bean.getIsHub().equalsIgnoreCase("Yes")) {
									isParallelShifting(opportunityBean, cityType, bean.getEhsId(),
											String.valueOf(request.getLinkId()), portBwChange, llBwChange,
											parallelRundays);
								}
							}
						}
						LOGGER.info("outside if hub" + bean.getEthernetFlavour()+"type and subtype"+opportunityBean.getType()+":"+opportunityBean.getSubType());
					}
	            }

				}

				LOGGER.info("OrderType is :    {}", opportunityBean.getType());
				LOGGER.info("SubType is :    {}   and copf id is ::::: {} ", opportunityBean.getSubType(),
						opportunityBean.getCopfIdC());
			} else {
			 if (!CommonConstants.NDE.equalsIgnoreCase(productName.get()) && !quote.getNsQuote().equalsIgnoreCase("Y")) {
				if (CommonConstants.UPGRADE.equalsIgnoreCase(upgradeOrDowngradeBwChange)) {
					opportunityBean.setType(MACDConstants.CHANGE_ORDER + " \u2013 " + CommonConstants.UPGRADE);
					opportunityBean.setSubType(MACDConstants.CHANGE_ORDER + " \u2013 " + CommonConstants.UPGRADE);
				} else if (CommonConstants.DOWNGRADE.equalsIgnoreCase(upgradeOrDowngradeBwChange)) {
					opportunityBean.setType(MACDConstants.CHANGE_ORDER + " \u2013 " + CommonConstants.DOWNGRADE);
					opportunityBean.setSubType(MACDConstants.CHANGE_ORDER + " \u2013 " + CommonConstants.DOWNGRADE);
				}
				LOGGER.info("OrderType is :    {}", opportunityBean.getType());
				LOGGER.info("SubType is :    {}", opportunityBean.getSubType());

				// O2C

				List<QuoteIllSiteToService> quoteIllSiteToServicesMulticircuitList = quoteIllSiteToServiceRepository
						.findByQuoteToLe_Id(quoteToLeOptional.get().getId());

				if (quoteIllSiteToServicesMulticircuitList != null
						&& !quoteIllSiteToServicesMulticircuitList.isEmpty()) {

					quoteIllSiteToServicesMulticircuitList.stream().forEach(siteToService -> {
						try {
							O2CSubCategoryBean subCategoryMC = new O2CSubCategoryBean();
							SIServiceDetailDataBean[] sIServiceDetailDataBeanMC = { null };
							String[] llBwChangeMC = { null };
							String[] portBwChangeMC = { null };
							sIServiceDetailDataBeanMC[0] = macdUtils.getServiceDetail(
									siteToService.getErfServiceInventoryTpsServiceId(),
									quoteToLeOptional.get().getQuoteCategory());
							String isBSOChanged = isBSOChanged(sIServiceDetailDataBeanMC[0],
									siteToService.getQuoteIllSite(), productName.get(), siteToService.getType());
							LOGGER.info("isBSOChanged  in mc :: {}", isBSOChanged);
							String parallelRunDaysMC = getParallelRundays(request, quoteToLeOptional);

							LOGGER.info("After executing parallel rundays in mc attribute value is {}",
									parallelRunDaysMC);
							String parallelRundaysMCFinal = "";

							if (!"".equalsIgnoreCase(parallelRunDaysMC) && !"0".equalsIgnoreCase(parallelRunDaysMC)) {
								parallelRundaysMCFinal = Objects.nonNull(parallelRunDaysMC) ? parallelRunDaysMC : NONE;
							} else
								parallelRundaysMCFinal = NONE;
							LOGGER.info("parallelRundaysMCFinal value for site id {} is {}",
									siteToService.getQuoteIllSite().getId(), parallelRundaysMCFinal);
							llBwChangeMC[0] = getLlBwChange(Optional.of(siteToService.getQuoteIllSite()),
									siteToService.getErfServiceInventoryTpsServiceId());
							portBwChangeMC[0] = getPortBwChange(Optional.of(siteToService.getQuoteIllSite()),
									siteToService.getErfServiceInventoryTpsServiceId(), quoteToLeOptional);
							String upgradeOrDowngradeBwChangeMC = isUpgradeOrDowngrade(llBwChangeMC[0],
									portBwChangeMC[0]);
							LOGGER.info("Value of upgradeOrDowngradeBwChange in mc loop is {}",
									upgradeOrDowngradeBwChangeMC);
							evaluateO2COrderSubCategoryForCB(isBSOChanged, upgradeOrDowngradeBwChangeMC,
									parallelRundaysMCFinal, subCategoryMC);
							LOGGER.info("subCategory saved o2cordertype {}, o2csubType {}", subCategoryMC.getO2cOrderType(), subCategoryMC.getO2cOrderSubType());
							siteToService.setErfSfdcOrderType(subCategoryMC.getO2cOrderType());
							siteToService.setErfSfdcSubType(subCategoryMC.getO2cOrderSubType());
							quoteIllSiteToServiceRepository.save(siteToService);
						} catch (Exception e) {
							LOGGER.error("Exception when processing updateOrderTypeSubType for multicircuit {}", e);
						}
					});
				}
              } else if (!CommonConstants.NDE.equalsIgnoreCase(productName.get()) && 
            		  !CommonConstants.NPL.equalsIgnoreCase(productName.get()) &&
            		  quote.getNsQuote().equalsIgnoreCase("Y")) {
            	  
            	  //NSO Multicircuit O2C subcategory changes            	  
            	  List<QuoteIllSiteToService> quoteIllSiteToServicesMulticircuitList = quoteIllSiteToServiceRepository
  						.findByQuoteToLe_Id(quoteToLeOptional.get().getId());

  				if (quoteIllSiteToServicesMulticircuitList != null
  						&& !quoteIllSiteToServicesMulticircuitList.isEmpty()) {

  					quoteIllSiteToServicesMulticircuitList.stream().forEach(siteToService -> {
  						try {
  							O2CSubCategoryBean subCategoryMC = new O2CSubCategoryBean();
							SIServiceDetailDataBean[] sIServiceDetailDataBeanMC = { null };
							String[] llBwChangeMC = { null };
							String[] portBwChangeMC = { null };
							String cityTypeMC = null;
							sIServiceDetailDataBeanMC[0] = macdUtils.getServiceDetail(
									siteToService.getErfServiceInventoryTpsServiceId(),
									quoteToLeOptional.get().getQuoteCategory());
							//siteIf from UI has to go
							String isBSOChanged = isBSOChanged(sIServiceDetailDataBeanMC[0],
									siteToService.getQuoteIllSite(), productName.get(), siteToService.getType());
							LOGGER.info("isBSOChanged  in mc :: {}", isBSOChanged);
							String parallelRunDaysMC = getParallelRundays(request, quoteToLeOptional);
							LOGGER.info("After executing parallel rundays in NSO mc attribute value is {}",
									parallelRunDaysMC);
							String parallelRundaysMCFinal = "";

							if (!"".equalsIgnoreCase(parallelRunDaysMC) && !"0".equalsIgnoreCase(parallelRunDaysMC)) {
								parallelRundaysMCFinal = Objects.nonNull(parallelRunDaysMC) ? parallelRunDaysMC : NONE;
							} else {
								parallelRundaysMCFinal = NONE;
							}
							LOGGER.info("parallelRundaysMCFinal value for site id {} is {}",
									siteToService.getQuoteIllSite().getId(), parallelRundaysMCFinal);
							llBwChangeMC[0] = getLlBwChange(Optional.of(siteToService.getQuoteIllSite()),
									siteToService.getErfServiceInventoryTpsServiceId());
							portBwChangeMC[0] = getPortBwChange(Optional.of(siteToService.getQuoteIllSite()),
									siteToService.getErfServiceInventoryTpsServiceId(), quoteToLeOptional);
							String upgradeOrDowngradeBwChangeMC = isUpgradeOrDowngrade(llBwChangeMC[0],
									portBwChangeMC[0]);
							LOGGER.info("Value of upgradeOrDowngradeBwChange in mc loop is {}",
									upgradeOrDowngradeBwChangeMC);
							
							//Need to get the diff new siteIds from UI i.e., SiteID from UI has to go
							cityTypeMC = getCityType(sIServiceDetailDataBeanMC[0], siteToService.getQuoteIllSite());
							
							//NSO(MC) IAS/GVPN ShiftSite and ChangeBW 
							
  						if (MACDConstants.CHANGE_BANDWIDTH_SERVICE
  								.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
  							evaluateO2COrderSubCategoryForCB(isBSOChanged, upgradeOrDowngradeBwChangeMC, parallelRundaysMCFinal,
  									subCategoryMC);
  							getTypeBasedOnBWChangeAndParallelRundays(opportunityBean, upgradeOrDowngradeBwChangeMC,
  									parallelRundaysMCFinal);
  						}

  						if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
  							// Parallel Upgrade and Downgrade for Shift Service - Intra(4cases)
  							isParallelUpgradeOrDowngradeForIntra(opportunityBean, upgradeOrDowngradeBwChangeMC, cityTypeMC);
  							// Intercity shifting with/without parallelRundays(4cases)
  							orderTypeForShiftWithoutBwChange(opportunityBean, parallelRundaysMCFinal, cityTypeMC,
  									upgradeOrDowngradeBwChangeMC);
  							evaluateO2CSubCategoryForSS(subCategoryMC, upgradeOrDowngradeBwChangeMC, cityTypeMC, parallelRundaysMCFinal,
  									isBSOChanged);
  						}
  						siteToService.setErfSfdcOrderType(subCategoryMC.getO2cOrderType());
						siteToService.setErfSfdcSubType(subCategoryMC.getO2cOrderSubType());
						quoteIllSiteToServiceRepository.save(siteToService);
						//Updating SFDC_orderType in quoteProdCompAttrVal
						QuoteProductComponent quoteprodComp = quoteProductComponentRepository
								.findByReferenceIdAndMstProductComponent_NameAndType(siteToService.getQuoteIllSite().getId(), MACDConstants.SITE_COMPONENT, MACDConstants.REFERENCE_TYPE_PRIMARY).stream().findFirst()
								.get();
						LOGGER.info("QuoteProductComponent Object {},and component id{}",quoteprodComp,quoteprodComp.getId());
						QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), MACDConstants.SFDC_ATTRIBUTE)
								.stream().findFirst().get();
						LOGGER.info("Attr Value {}",attributeValue.getAttributeValues());
						attributeValue.setAttributeValues(opportunityBean.getSubType());
						attributeValue.setDisplayValue(opportunityBean.getSubType());
						quoteProductComponentsAttributeValueRepository.save(attributeValue);
  					} catch (Exception e) {
						LOGGER.error("Exception when processing updateOrderTypeSubType for multicircuit NSO{}", e);
					}
  					});
  				}
             }
			 else {
				 LOGGER.info("enterd into mc nde");
					if (MACDConstants.CHANGE_BANDWIDTH_SERVICE
							.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
						String isBSOChanged = isBSOChanged(sIServiceDetailDataBean, illSiteOpt.get(), productName.get(),
								quoteIllSiteToService.getType());
						LOGGER.info("isBSOChanged :: {}", isBSOChanged);
						evaluateO2COrderSubCategoryForCB(isBSOChanged, upgradeOrDowngradeBwChange, parallelRundays,
								subCategory);
						getTypeBasedOnBWChangeAndParallelRundays(opportunityBean, upgradeOrDowngradeBwChange,
								parallelRundays);
					}

					if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
						// Parallel Upgrade and Downgrade for Shift Service - Intra(4cases)
						isParallelUpgradeOrDowngradeForIntra(opportunityBean, upgradeOrDowngradeBwChange, cityType);
						// Intercity shifting with/without parallelRundays(4cases)
						orderTypeForShiftWithoutBwChange(opportunityBean, parallelRundays, cityType,
								upgradeOrDowngradeBwChange);
						String isBSOChanged = isBSOChanged(sIServiceDetailDataBean, illSiteOpt.get(), productName.get(),
								quoteIllSiteToService.getType());
						LOGGER.info("isBSOChanged :: {}", isBSOChanged);
						evaluateO2CSubCategoryForSS(subCategory, upgradeOrDowngradeBwChange, cityType, parallelRundays,
								isBSOChanged);
						//nde intracity shifting with same ehs id or diff ehs id
						if (CommonConstants.NDE.equalsIgnoreCase(productName.get())) {
							SIServiceInfoBean bean = macdUtils
									.getServicAttributesNde(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
							LOGGER.info("service inventory data for nde" + bean.getEthernetFlavour());
							if(bean.getEthernetFlavour()!=null) {
							if (bean.getEthernetFlavour().equalsIgnoreCase("Hub National Dedicated Ethernet")) {
								LOGGER.info("inside if hub" + bean.getIsHub());
								if (bean.getIsHub()!=null) {
									if (bean.getIsHub().equalsIgnoreCase("Yes")) {
										isParallelShifting(opportunityBean, cityType, bean.getEhsId(),
												String.valueOf(request.getLinkId()), portBwChange, llBwChange,
												parallelRundays);
									}
								}
							}
							LOGGER.info("outside if hub" + bean.getEthernetFlavour()+"type and subtype"+opportunityBean.getType()+":"+opportunityBean.getSubType());
						}
		            }

					}

					LOGGER.info("OrderType nde mc is :    {}", opportunityBean.getType());
					LOGGER.info("SubType is nde mc:    {}   and copf id is ::::: {} ", opportunityBean.getSubType(),
							opportunityBean.getCopfIdC());
			    }
			}
			
			LOGGER.info("SFDC OrderType is Before going to save or update :    {}", opportunityBean.getType());
			// we needs to re write if it is multi circuit needs to update each site level
			if (CommonConstants.NDE.equalsIgnoreCase(productName.get())) {
				MstOmsAttribute mstOmsAttrubute = getMstAttributeMaster(SFDCConstants.SFDC_ORDER_NDE_TYPE,
						Utils.getSource());
				List<QuoteLeAttributeValue> quoteLeAttrvalues = quoteLeAttributeValueRepository
						.findByQuoteToLe_IdAndMstOmsAttribute_Id(quoteToLeOptional.get().getId(),
								mstOmsAttrubute.getId());
				if (quoteLeAttrvalues.isEmpty()) {
					QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
					quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttrubute);
					quoteLeAttributeValue.setAttributeValue(opportunityBean.getType());
					quoteLeAttributeValue.setDisplayValue(opportunityBean.getType());
					quoteLeAttributeValue.setQuoteToLe(quoteToLeOptional.get());
					quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
				} else {
					LOGGER.info("Updating Attribute values id" + quoteLeAttrvalues.get(0).getId() + "type value"
							+ opportunityBean.getType());
					quoteLeAttrvalues.get(0).setAttributeValue(opportunityBean.getType());
					quoteLeAttrvalues.get(0).setDisplayValue(opportunityBean.getType());
					quoteLeAttributeValueRepository.save(quoteLeAttrvalues.get(0));
				}
			}
		// end update order type
		}

		if (CommonConstants.BDEACTIVATE.equals(quoteToLeOptional.get().getIsMultiCircuit())) {
			List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository
					.findByTpsSfdcParentOptyIdAndQuoteToLe(quoteToLeOptional.get().getTpsSfdcParentOptyId(),
							quoteToLeOptional.get());
			if (quoteIllSiteToServices != null && !quoteIllSiteToServices.isEmpty()) {
				quoteIllSiteToServices.stream().forEach(siteToService -> {
					LOGGER.info("order sub type in subCategory {}",subCategory.getO2cOrderType());
					if (StringUtils.isNotBlank(subCategory.getO2cOrderType())) {
						siteToService.setErfSfdcOrderType(subCategory.getO2cOrderType());
						siteToService.setErfSfdcSubType(subCategory.getO2cOrderSubType());
					}
					else {
						siteToService.setErfSfdcOrderType(opportunityBean.getType());
						siteToService.setErfSfdcSubType(opportunityBean.getType());
					}
					quoteIllSiteToServiceRepository.save(siteToService);
				});
			}
		}

		if(Objects.nonNull(illSiteOpt.get()) && Objects.nonNull(illSiteOpt.get().getIsColo())
				&& illSiteOpt.get().getIsColo().equals(BACTIVE) && Objects.nonNull(productName.get()) && GVPN.equalsIgnoreCase(productName.get())){
			LOGGER.info("Entering is Colo block ----> {} for quote ----> {} ", quoteToLeOptional.get().getQuote().getQuoteCode());
			opportunityBean.setType(MACDConstants.ADDITION_OF_SITE);
			opportunityBean.setSubType(MACDConstants.ADDITION_OF_SITE);
			subCategory.setO2cOrderType(MACDConstants.ADDITION_OF_SITE);
			subCategory.setO2cOrderSubType(MACDConstants.ADDITION_OF_SITE);

		}
		if(Objects.nonNull(quoteToLeOptional.get()) && Objects.nonNull(quoteToLeOptional.get().getIsDemo()) && (GVPN.equalsIgnoreCase(productName.get()) ||
				IAS.equalsIgnoreCase(productName.get()))){
			int result = Byte.compare(quoteToLeOptional.get().getIsDemo(), BACTIVE);
			if(result==0) {
				LOGGER.info("Entering is DEMO block ----> {} for quote ----> {} ", quoteToLeOptional.get().getQuote().getQuoteCode());
				if (MACD.equalsIgnoreCase(quoteToLeOptional.get().getQuoteType()) && MACDConstants.DEMO_EXTENSION.equalsIgnoreCase(quoteToLeOptional.get().getQuoteCategory())) {
					LOGGER.info("Entering is DEMO MACD block ----> {} for quote ----> {} ", quoteToLeOptional.get().getQuote().getQuoteCode());
					opportunityBean.setType(MACDConstants.DEMO_EXTENSION_SFDC);
					opportunityBean.setSubType(MACDConstants.DEMO_EXTENSION_SFDC);
					subCategory.setO2cOrderType(MACDConstants.DEMO_EXTENSION_SFDC);
					subCategory.setO2cOrderSubType(MACDConstants.DEMO_EXTENSION_SFDC);
				}

				if (NEW.equalsIgnoreCase(quoteToLeOptional.get().getQuoteType())) {
					LOGGER.info("Entering is DEMO NEW block ----> {} for quote ----> {} ", quoteToLeOptional.get().getQuote().getQuoteCode());
					if("free".equalsIgnoreCase(quoteToLeOptional.get().getDemoType())){
						opportunityBean.setType(MACDConstants.DEMO_FREE);
						opportunityBean.setSubType(MACDConstants.DEMO_FREE);
						subCategory.setO2cOrderType(MACDConstants.DEMO_FREE);
						subCategory.setO2cOrderSubType(MACDConstants.DEMO_FREE);
					}
					else if("paid".equalsIgnoreCase(quoteToLeOptional.get().getDemoType())){
						opportunityBean.setType(MACDConstants.DEMO_PAID);
						opportunityBean.setSubType(MACDConstants.DEMO_PAID);
						subCategory.setO2cOrderType(MACDConstants.DEMO_PAID);
						subCategory.setO2cOrderSubType(MACDConstants.DEMO_PAID);
					}
				}
			}
	}
		

		if (Objects.nonNull(quote.getNsQuote()) && quote.getNsQuote().equalsIgnoreCase("Y")) {
			LOGGER.info("Entering NS quote block to fetch sfdc order type quoe code {} ", quote.getQuoteCode());
			// Updating SFDC_orderType in quoteProdCompAttrVal
			QuoteProductComponent quoteprodComp = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(request.getSiteId(),
							MACDConstants.SITE_COMPONENT, MACDConstants.REFERENCE_TYPE_PRIMARY)
					.stream().findFirst().orElse(null);
			if (quoteprodComp != null) {
				LOGGER.info("QuoteProductComponent Object {},and component id{}", quoteprodComp, quoteprodComp.getId());
				QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(),
								MACDConstants.SFDC_ATTRIBUTE)
						.stream().findFirst().orElse(null);
				if (attributeValue != null) {
					LOGGER.info("Attr Value {}", attributeValue.getAttributeValues());
					attributeValue.setAttributeValues(opportunityBean.getSubType());
					attributeValue.setDisplayValue(opportunityBean.getSubType());
					quoteProductComponentsAttributeValueRepository.save(attributeValue);
				}
			}
		}
		List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository
				.findByTpsSfdcParentOptyIdAndQuoteToLe(quoteToLeOptional.get().getTpsSfdcParentOptyId(),
						quoteToLeOptional.get());
		if (quoteIllSiteToServices != null && !quoteIllSiteToServices.isEmpty()) {
			LOGGER.info("Order sub type in quoteIllSiteToService is {}", quoteIllSiteToServices.stream().findAny().get().getErfSfdcOrderType());
		}

		return opportunityBean;
	}

	private O2CSubCategoryBean evaluateO2CSubCategoryForSS(O2CSubCategoryBean subCategory,
			String upgradeOrDowngradeBwChange, String cityType, String parallelRundays, String isBSOChanged) {

		LOGGER.info(
				"evaluateO2CSubCategoryForSS:: upgradeOrDowngradeBwChange{}, cityType {}, parallelRundays{}, isBSOChanged{}",
				upgradeOrDowngradeBwChange, cityType, parallelRundays, isBSOChanged);

		if (CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType) && "".equalsIgnoreCase(upgradeOrDowngradeBwChange)
				&& CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
			if (CommonConstants.NO.equalsIgnoreCase(isBSOChanged)) {
				subCategory.setO2cOrderSubType(MACDConstants.LM_SHIFTING);
				subCategory.setO2cOrderType(MACDConstants.LM_SHIFTING);
			} else {
				subCategory.setO2cOrderSubType(MACDConstants.LM_SHIFTING_BSO_CHANGE);
				subCategory.setO2cOrderType(MACDConstants.LM_SHIFTING_BSO_CHANGE);
			}
		} else if (CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType)
				&& !"".equalsIgnoreCase(upgradeOrDowngradeBwChange)
				&& CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
			if (CommonConstants.NO.equalsIgnoreCase(isBSOChanged)) {
				subCategory.setO2cOrderSubType(
						CommonConstants.HOT + upgradeOrDowngradeBwChange + MACDConstants.HYPHEN_LM_SHIFTING);
				subCategory.setO2cOrderType(
						CommonConstants.HOT + upgradeOrDowngradeBwChange + MACDConstants.HYPHEN_LM_SHIFTING);
			} else {
				subCategory.setO2cOrderSubType(
						CommonConstants.HOT + upgradeOrDowngradeBwChange + MACDConstants.BSO_CHANGE);
				subCategory
						.setO2cOrderType(CommonConstants.HOT + upgradeOrDowngradeBwChange + MACDConstants.BSO_CHANGE);
			}

		} else if (CommonConstants.INTER_CITY.equalsIgnoreCase(cityType)) {
			subCategory.setO2cOrderSubType(MACDConstants.PARALLEL_SHIFTING);
			subCategory.setO2cOrderType(MACDConstants.PARALLEL_SHIFTING);
		}

		LOGGER.info("evaluateO2CSubCategoryForSS :: subcategory {}", subCategory.toString());
		return subCategory;

	}

	private O2CSubCategoryBean evaluateO2COrderSubCategoryForCB(String isBSOChanged, String upgradeOrDowngradeBwChange,
			String parallelRundays, O2CSubCategoryBean subCategory) {
		LOGGER.info("evaluateO2COrderSubCategory :: isBSOChanged {}, upgradeOrDowngradeBwChange {}, parallelRundays{}",
				isBSOChanged, upgradeOrDowngradeBwChange, parallelRundays);
		if (!CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
			subCategory.setO2cOrderType(CommonConstants.PARALLEL + upgradeOrDowngradeBwChange);
			subCategory.setO2cOrderSubType(CommonConstants.PARALLEL + upgradeOrDowngradeBwChange);
		} else if (CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
			if (CommonConstants.NO.equalsIgnoreCase(isBSOChanged)) {
				subCategory.setO2cOrderType(CommonConstants.HOT + upgradeOrDowngradeBwChange);
				subCategory.setO2cOrderSubType(CommonConstants.HOT + upgradeOrDowngradeBwChange);
			} else {
				subCategory
						.setO2cOrderType(CommonConstants.HOT + upgradeOrDowngradeBwChange + MACDConstants.BSO_CHANGE);
				subCategory.setO2cOrderSubType(
						CommonConstants.HOT + upgradeOrDowngradeBwChange + MACDConstants.BSO_CHANGE);
			}
		}
		LOGGER.info("Sub category evaluateO2COrderSubCategory ordertype {}, order sub type {}", subCategory.getO2cOrderType(), subCategory.getO2cOrderSubType());
		return subCategory;

	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to update the
	 *            order type and subtype in SFDC - SHIFT_SITE Scenario
	 * @return OpportunityBean
	 */

	private OpportunityBean orderTypeForShiftWithoutBwChange(OpportunityBean opportunityBean, String parallelRundays,
			String cityType, String upgradeOrDowngradeBwChange) {
		LOGGER.info(
				"Entering orderTypeForShiftWithoutBwChange , value of parallelRundays here , city type,upgradeOrDowngradeBwChange {}"
						+ "",
				parallelRundays, cityType, upgradeOrDowngradeBwChange);
		if (CommonConstants.INTER_CITY.equalsIgnoreCase(cityType)) {
			opportunityBean.setSubType(MACDConstants.PARALLEL_SHIFTING);
			opportunityBean.setType(MACDConstants.PARALLEL_SHIFTING);
		} else if (CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType)) {
			if (CommonConstants.NONE.equalsIgnoreCase(parallelRundays)
					&& "".equalsIgnoreCase(upgradeOrDowngradeBwChange)) {
				opportunityBean.setSubType(MACDConstants.LM_BSO_SHIFTING);
				opportunityBean.setType(MACDConstants.LM_BSO_SHIFTING);
			} else if (!(CommonConstants.NONE.equalsIgnoreCase(parallelRundays))
					&& "".equalsIgnoreCase(upgradeOrDowngradeBwChange)) {
				opportunityBean.setSubType(MACDConstants.PARALLEL_SHIFTING);
				opportunityBean.setType(MACDConstants.PARALLEL_SHIFTING);
			}
		}
		LOGGER.info("Value of order type after orderTypeForShiftWithoutBwChange {}", opportunityBean.getType());

		return opportunityBean;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to update the
	 *            order type and subtype in SFDC - SHIFT_SITE + CHANGE_BANDWIDTH
	 *            Scenario
	 * @return OpportunityBean
	 */

	private OpportunityBean isParallelUpgradeOrDowngradeForIntra(OpportunityBean opportunityBean,
			String upgradeOrDowngradeBwChange, String cityType) {
		LOGGER.info("Entering isParallelUpgradeOrDowngradeForIntra");
		if (CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType)) {
			opportunityBean.setType(CommonConstants.PARALLEL + upgradeOrDowngradeBwChange);
			opportunityBean.setSubType(CommonConstants.PARALLEL + upgradeOrDowngradeBwChange);
		}
		LOGGER.info("Value of order type after isParallelUpgradeOrDowngradeForIntra {}", opportunityBean.getType());

		return opportunityBean;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to get the
	 *            city type - INTERCITY or INTRACITY
	 * @return cityType
	 */

	private String getCityType(SIServiceDetailDataBean sIServiceDetailDataBean, QuoteIllSite illSiteOpt)
			throws TclCommonException {
		com.tcl.dias.common.beans.AddressDetail existingAddressDetail = getExistingCity(sIServiceDetailDataBean);
		com.tcl.dias.common.beans.AddressDetail newAddressDetail = getNewCity(illSiteOpt);

		String existingCity = Objects.nonNull(existingAddressDetail.getCity()) ? existingAddressDetail.getCity() : "";
		String existingPincode = Objects.nonNull(existingAddressDetail.getPincode()) ? existingAddressDetail.getPincode() : "";
		LOGGER.info("Existing city {}, existing pincode {}", existingCity, existingPincode);

		String newCity =  Objects.nonNull(newAddressDetail.getCity()) ? newAddressDetail.getCity() : "";
		String newPincode = Objects.nonNull(newAddressDetail.getPincode()) ? newAddressDetail.getPincode() : "";
		LOGGER.info("new city {}, new pincode {}", newCity, newPincode);
		String cityType = "";

		if (Objects.nonNull(existingCity) && Objects.nonNull(newCity)) {

			if (existingCity.equalsIgnoreCase(newCity)) {
				cityType = CommonConstants.INTRA_CITY;
			} else if (!existingCity.equalsIgnoreCase(newCity)) {
				if(Objects.nonNull(newPincode) && Objects.nonNull(existingPincode) && !newPincode.equalsIgnoreCase(existingPincode)) {
				cityType = CommonConstants.INTER_CITY; 
				} else {
					cityType = CommonConstants.INTRA_CITY;
				}
			}
		}
		LOGGER.info("City type in getCityType method is {}", cityType);
		return cityType;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to update the
	 *            order type and subtype in SFDC - CHANGE_BANDWIDTH Scenario
	 * @return OpportunityBean
	 */

	private OpportunityBean getTypeBasedOnBWChangeAndParallelRundays(OpportunityBean opportunityBean,
			String upgradeOrDowngradeBwChange, String parallelRundays) {

		LOGGER.info("Entering getTypeBasedOnBWChangeAndParallelRundays");

		if (!CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
			opportunityBean.setSubType(CommonConstants.PARALLEL + upgradeOrDowngradeBwChange);
			opportunityBean.setType(CommonConstants.PARALLEL + upgradeOrDowngradeBwChange);
		} else if (CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
			opportunityBean.setSubType(CommonConstants.HOT + upgradeOrDowngradeBwChange);
			opportunityBean.setType(CommonConstants.HOT + upgradeOrDowngradeBwChange);
		}

		LOGGER.info("Value of order type after getTypeBasedOnBWChangeAndParallelRundays {}", opportunityBean.getType());

		return opportunityBean;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return the
	 *            bandwidths (port and LL) if upgraded or not
	 * @return attrValue
	 */
	private String isUpgradeOrDowngrade(String llBwChange, String portBwChange) {
		String attrValue = "";
		if (CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange)
				|| (CommonConstants.EQUAL.equalsIgnoreCase(portBwChange) && CommonConstants.DOWNGRADE.equalsIgnoreCase(llBwChange))
				|| (CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange)
						&& CommonConstants.UPGRADE.equalsIgnoreCase(llBwChange)))
			attrValue = CommonConstants.DOWNGRADE;

		else if (CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange)
				|| (CommonConstants.EQUAL.equalsIgnoreCase(portBwChange) && CommonConstants.UPGRADE.equalsIgnoreCase(llBwChange))
				|| (CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange)
						&& CommonConstants.DOWNGRADE.equalsIgnoreCase(llBwChange)))
			attrValue = CommonConstants.UPGRADE;
		LOGGER.info("Change in Bandwidth in isUpgradeOrDowngrade method is {}", attrValue);
		return attrValue;
	}
	
	private String isUpgradeOrDowngrade(String portBwChange) {
		String attrValue = "";
		if (CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange)
				|| (CommonConstants.EQUAL.equalsIgnoreCase(portBwChange) )
				|| (CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange)
						))
			attrValue = CommonConstants.DOWNGRADE;

		else if (CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange)
				|| (CommonConstants.EQUAL.equalsIgnoreCase(portBwChange) )
				|| (CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange)))
			attrValue = CommonConstants.UPGRADE;
		LOGGER.info("Change in Bandwidth in isUpgradeOrDowngrade method is {}", attrValue);
		return attrValue;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return the
	 *            bandwidth - LL if changed or not
	 * @return changeInLlBw
	 */
	private String getLlBwChange(Optional<QuoteIllSite> illSiteOpt, String serviceId) throws TclCommonException {

		LOGGER.info("ILL SITE OPT :::: {}  for quote id :::: {}", illSiteOpt.get().getId());

		Optional<QuoteToLe> quoteToLeOptional = Optional
				.ofNullable(illSiteOpt.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe());

		LOGGER.info("Quote to le is ::: {} ", quoteToLeOptional.get().getId());

		String bwUnitLl = illPricingFeasibilityService.getOldBandwidthUnit(serviceId,
				FPConstants.LOCAL_LOOP_BW_UNIT.toString());

		Double oldLlBw = Utils.parseDouble(
				illPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString()));

		LOGGER.info("OLd local bw for IAS{}", oldLlBw);

		Double newLLBw = 0D;

		newLLBw = newLlAccToProdName(illSiteOpt, quoteToLeOptional, newLLBw);
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

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return the
	 *            bandwidth - Port if changed or not
	 * @return changeInPortBw
	 */

	private String getPortBwChange(Optional<QuoteIllSite> illSiteOpt, String serviceId,
			Optional<QuoteToLe> quoteToLeOptional) throws TclCommonException {

		Double newPortBw = 0D;

		String bwUnitPort = illPricingFeasibilityService.getOldBandwidthUnit(serviceId,
				FPConstants.PORT_BANDWIDTH_UNIT.toString());
		String bw=
				illPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString());
		Double oldPortBw =null;
		if (bw != null) {
			oldPortBw = Double.parseDouble(
					illPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString()));
			LOGGER.info("OLd port Bandwidth in getPortBwChange method before BW Conversion {}", oldPortBw);
		}
		newPortBw = portBandwidthAccToCompName(illSiteOpt, quoteToLeOptional, newPortBw);

		if (oldPortBw != null) {
			oldPortBw = Double
					.parseDouble(illPricingFeasibilityService.setBandwidthConversion(oldPortBw.toString(), bwUnitPort));
			LOGGER.info("OLd port Bandwidth in getPortBwChange method after BW Conversion {}", oldPortBw);
		}

		String changeInPortBw = "";

		if (Objects.nonNull(oldPortBw) && Objects.nonNull(newPortBw) && 0D != newPortBw) {

			LOGGER.info("Before Comparison, for changeinLLBW old new bandwidth is {} ", oldPortBw + "new bandwidth  {}",
					newPortBw);

			int result = newPortBw.compareTo(oldPortBw);

			if (result > 0)
				changeInPortBw = CommonConstants.UPGRADE;
			else if (result < 0)
				changeInPortBw = CommonConstants.DOWNGRADE;
			else if (result == 0)
				changeInPortBw = CommonConstants.EQUAL;

			/*
			 * if(oldPortBw==newPortBw){ changeInPortBw = CommonConstants.EQUAL; } else
			 * if(newPortBw>oldPortBw){ changeInPortBw = CommonConstants.UPGRADE; } else
			 * changeInPortBw = CommonConstants.DOWNGRADE;
			 */

		}
		LOGGER.info("Port Bw inside getPortBwChange method{}", changeInPortBw);
		return changeInPortBw;

	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return the
	 *            port bandwidth according to VPN Port or Internet Port
	 * @return parallelRundaysAttrValue
	 */

	private Double portBandwidthAccToCompName(Optional<QuoteIllSite> illSiteOpt, Optional<QuoteToLe> quoteToLeOptional,
			Double newPortBw) throws TclCommonException {

		if (newPortBw == 0D) {
			if (getProductNameSfdc(quoteToLeOptional, GVPN).isPresent()
					&& CommonConstants.GVPN.equalsIgnoreCase(getProductNameSfdc(quoteToLeOptional, GVPN).get())) {
				newPortBw = Double.parseDouble(gvpnPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
						FPConstants.VPN_PORT.toString(), FPConstants.PORT_BANDWIDTH.toString()));
				LOGGER.info("New port bw for GVPN{}", newPortBw);

			} else if (getProductNameSfdc(quoteToLeOptional, IAS).isPresent()
					&& CommonConstants.IAS.equalsIgnoreCase(getProductNameSfdc(quoteToLeOptional, IAS).get())) {
				LOGGER.info("IAS started for new port cal{}");

				newPortBw = Double.parseDouble(illPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
						FPConstants.INTERNET_PORT.toString(), FPConstants.PORT_BANDWIDTH.toString()));
				LOGGER.info("New port bw for IAS{}", newPortBw);
			}else if (getProductNameSfdc(quoteToLeOptional, NPL).isPresent()&&CommonConstants.NPL.equalsIgnoreCase(getProductNameSfdc(quoteToLeOptional, NPL).get())) {
				LOGGER.info("IAS started for new port cal{}");
				newPortBw = Double.parseDouble(illPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
						"Cross Connect", FPConstants.BANDWIDTH.toString()));
				LOGGER.info("New port bw for NPL{}", newPortBw);
			} else {
				if (getProductNameSfdc(quoteToLeOptional, IZOPC).isPresent()
						&& CommonConstants.IZOPC.equalsIgnoreCase(getProductNameSfdc(quoteToLeOptional, IZOPC).get())) {
					newPortBw = Double.parseDouble(izoPcPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
							FPConstants.IZO_PORT.toString(), FPConstants.BANDWIDTH.toString()));
					LOGGER.info("New port bw for GVPN{}", newPortBw);

				} 
			}
		}
		return newPortBw;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return
	 *            Local Loop Bw for GVPN or ILL
	 * @return parallelRundaysAttrValue
	 */

	private Double newLlAccToProdName(Optional<QuoteIllSite> illSiteOpt, Optional<QuoteToLe> quoteToLeOptional,
			Double newLlBw) throws TclCommonException {

		LOGGER.info("Entering method newLlAccToProdName for Quote id{}", quoteToLeOptional.get().getQuote().getId());
		/** COLO Sites dont have Lastmile **/
		if (Objects.nonNull(illSiteOpt.get().getIsColo()) && illSiteOpt.get().getIsColo().equals((byte) 1)) {
		    LOGGER.info("Colo sites doesnt have lastmile component");
			return newLlBw;
		}
		if (newLlBw == 0D) {
			if (getProductNameSfdc(quoteToLeOptional, GVPN).isPresent()
					&& CommonConstants.GVPN.equalsIgnoreCase(getProductNameSfdc(quoteToLeOptional, GVPN).get())) {
				LOGGER.info("Entering Method newLlAccToProdName GVPN Case");
				newLlBw = Double.parseDouble(gvpnPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
						FPConstants.LAST_MILE.toString(), FPConstants.LOCAL_LOOP_BW.toString()));
				LOGGER.info("New LL bw for GVPN{}", newLlBw);

			} else if (getProductNameSfdc(quoteToLeOptional, IAS).isPresent()
					&& CommonConstants.IAS.equalsIgnoreCase(getProductNameSfdc(quoteToLeOptional, IAS).get())) {
				LOGGER.info("IAS started for new port cal{}");
				newLlBw = Double.parseDouble(illPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
						FPConstants.LAST_MILE.toString(), FPConstants.LOCAL_LOOP_BW.toString()));
				LOGGER.info("New LL bw for IAS{}", newLlBw);
			} else if (getProductNameSfdc(quoteToLeOptional, IZOPC).isPresent()
					&&CommonConstants.IZOPC.equalsIgnoreCase(getProductNameSfdc(quoteToLeOptional, IZOPC).get())) {
				LOGGER.info("IZOPC started for new port cal{}");
				LOGGER.info("New LL bw for IZOPC{}", newLlBw);
			}
			else if (getProductNameSfdc(quoteToLeOptional, NPL).isPresent()&&CommonConstants.NPL.equalsIgnoreCase(getProductNameSfdc(quoteToLeOptional, NPL).get())) {
				LOGGER.info("IAS started for new port cal{}");
				newLlBw = Double.parseDouble(illPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
						"Cross Connect", FPConstants.BANDWIDTH.toString()));
				LOGGER.info("New LL bw for NPL{}", newLlBw);
			}
		}
		return newLlBw;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return if
	 *            ParallelRundays is opted or not
	 * @return parallelRundaysAttrValue
	 */

	private String getParallelRundays(UpdateRequest request, Optional<QuoteToLe> quoteToLeOptional) {
		Map<String, String> parallelRundaysMap = new HashMap<>();
		List<QuoteProductComponent> quoteProductComponentList = new ArrayList<>();

		String parallelRundaysAttrValue = "";
		Optional<String> productNameIas = getProductNameSfdc(quoteToLeOptional, IAS);
		Optional<String> productNameGvpn = getProductNameSfdc(quoteToLeOptional, GVPN);
		Optional<String> productNameNpl = getProductNameSfdc(quoteToLeOptional, CommonConstants.NPL);
		Optional<String> productNameNde = getProductNameSfdc(quoteToLeOptional, CommonConstants.NDE);
		LOGGER.info("Product Name NPL :: {}", productNameNpl);
		if (productNameIas.isPresent() && Objects.nonNull(productNameIas.get())) {
			quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(request.getSiteId(), QuoteConstants.ILLSITES.toString());
			parallelRundaysMap = illPricingFeasibilityService
					.getParallelBuildAndParallelRunDays(quoteProductComponentList, parallelRundaysMap);
			parallelRundaysAttrValue = parallelRundaysMap.get(MACDConstants.PARALLEL_RUNDAYS);

		} else if (productNameGvpn.isPresent() && Objects.nonNull(productNameGvpn.get())) {
			quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(request.getSiteId(), QuoteConstants.GVPN_SITES.toString());
			parallelRundaysMap = gvpnPricingFeasibilityService
					.getParallelBuildAndParallelRunDays(quoteProductComponentList, parallelRundaysMap);
		} else if (productNameNpl.isPresent() && Objects.nonNull(productNameNpl.get())) {
			LOGGER.info("IN NPL Loop");
			quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(request.getLinkId(), QuoteConstants.NPL_LINK.toString());
			parallelRundaysMap = nplPricingFeasibilityService
					.getParallelBuildAndParallelRunDays(quoteProductComponentList, parallelRundaysMap);
		}
		else if(productNameNde.isPresent() && Objects.nonNull(productNameNde.get())) {
			LOGGER.info("IN NDE Loop");
			quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(request.getLinkId(), QuoteConstants.NPL_LINK.toString());
			parallelRundaysMap = nplPricingFeasibilityService.
					getParallelBuildAndParallelRunDays(quoteProductComponentList, parallelRundaysMap);
		}

		if (parallelRundaysMap.containsKey(MACDConstants.PARALLEL_RUNDAYS)) {
			parallelRundaysAttrValue = parallelRundaysMap.get(MACDConstants.PARALLEL_RUNDAYS);
			if (Objects.nonNull(parallelRundaysAttrValue))
				return parallelRundaysAttrValue;
			else
				return CommonConstants.NONE;
		}
		LOGGER.info("Parallel rundays attribute value is {}", parallelRundaysAttrValue);
		return StringUtils.isNotBlank(parallelRundaysAttrValue) ? parallelRundaysAttrValue : NONE;

	}

	private Optional<String> getProductNameSfdc(Optional<QuoteToLe> quoteToLeOptional, String productName) {
		return quoteToLeOptional.get().getQuoteToLeProductFamilies().stream()
				.map(QuoteToLeProductFamily::getMstProductFamily).map(MstProductFamily::getName)
				.filter(product -> productName.equalsIgnoreCase(product)).findFirst();
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to get the
	 *            existing city
	 * @return String
	 */

	private com.tcl.dias.common.beans.AddressDetail getExistingCity(SIServiceDetailDataBean sIServiceDetailDataBean) throws TclCommonException {
		com.tcl.dias.common.beans.AddressDetail addressDetail = new com.tcl.dias.common.beans.AddressDetail();
		String siSiteAddrId = Objects.nonNull(sIServiceDetailDataBean)
				? sIServiceDetailDataBean.getErfLocSiteAddressId()
				: "";
		LOGGER.info("ERF Location Site Address ID {}", siSiteAddrId);
		if (Objects.nonNull(siSiteAddrId)) {
			addressDetail = getLocQueueResponse(siSiteAddrId);
		}
		LOGGER.info("Address Details {}", addressDetail);
		LOGGER.info("Existing City in getExistingCity method is {}", addressDetail.getCity());
		return addressDetail;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to get the
	 *            new city
	 * @return String
	 */

	private com.tcl.dias.common.beans.AddressDetail getNewCity(QuoteIllSite illSiteOpt) throws TclCommonException {
		String quoteSiteAddrId = illSiteOpt.getErfLocSitebLocationId().toString();
		com.tcl.dias.common.beans.AddressDetail addressDetail = getLocQueueResponse(quoteSiteAddrId);
		LOGGER.info("New City in getNewCity method is {}", addressDetail.getCity());
		return addressDetail;
	}

	private com.tcl.dias.common.beans.AddressDetail getLocQueueResponse(String AddrId) throws TclCommonException {
		String locMstResponse = (String) mqUtils.sendAndReceive(addressDetailByLocationId, AddrId);
		return (com.tcl.dias.common.beans.AddressDetail) Utils.convertJsonToObject(locMstResponse,
				com.tcl.dias.common.beans.AddressDetail.class);
	}

	/**
	 * Method to validate addressdetail
	 * 
	 * @param addressDetail
	 * @return
	 */
	public AddressDetail validateAddressDetail(AddressDetail addressDetail) {
		if (Objects.isNull(addressDetail.getAddressLineOne()))
			addressDetail.setAddressLineOne("");
		if (Objects.isNull(addressDetail.getAddressLineTwo()))
			addressDetail.setAddressLineTwo("");
		if (Objects.isNull(addressDetail.getCity()))
			addressDetail.setCity("");
		if (Objects.isNull(addressDetail.getCountry()))
			addressDetail.setCountry("");
		if (Objects.isNull(addressDetail.getPincode()))
			addressDetail.setPincode("");
		if (Objects.isNull(addressDetail.getLocality()))
			addressDetail.setLocality("");
		if (Objects.isNull(addressDetail.getState()))
			addressDetail.setState("");
		return addressDetail;
	}

	private String setContinentValue(String country) {
		String continent;
		switch (country.toLowerCase()) {
		case unites_states_of_america: {
			continent = NORTH_AMERICA;
			break;
		}
		case india: {
			continent = ASIA;
			break;
		}
		case singapore: {
			continent = ASIA;
			break;
		}
		case france: {
			continent = EUROPE;
			break;
		}
		case united_kingdom: {
			continent = EUROPE;
			break;
		}
		case germany: {
			continent = EUROPE;
			break;
		}
		default:
			continent = ASIA;
		}
		LOGGER.info("Continent selected for country {}  is {}", country, continent);
		return continent;
	}

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return the
	 *            bandwidth - LL if changed or not
	 * @return changeInLlBw
	 */
	private String getLlBwChangeNPL(Optional<QuoteIllSite> illSiteOpt, String serviceId, Integer linkId)
			throws TclCommonException {

		LOGGER.info("In getLlBwChangeNPL ILL SITE OPT :::: {}  for service id :::: {}, link ID :: {}",
				illSiteOpt.get().getId(), serviceId, linkId);

		Optional<QuoteToLe> quoteToLeOptional = Optional
				.ofNullable(illSiteOpt.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe());

		String[] siteEnd = { null };
		LOGGER.info("In getLlBwChangeNPL Quote to le is ::: {} ", quoteToLeOptional.get().getId());

		String bwUnitLl = nplPricingFeasibilityService.getOldBandwidthUnit(serviceId,
				FPConstants.LOCAL_LOOP_BW_UNIT.toString());

		Double oldLlBw = Double.parseDouble(
				nplPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString()));

		LOGGER.info("OLd local bw for IAS{}", oldLlBw);

		Double newLLBw = 0D;
		String newLLBwWithUnit = null;

		LOGGER.info("IAS started for new port cal{}");

		Optional<QuoteNplLink> quoteNplLinkOpt = quoteNplLinkRepository.findById(linkId);
		if (quoteNplLinkOpt.isPresent()) {
			if (illSiteOpt.get().getId().equals(quoteNplLinkOpt.get().getSiteAId())) {
				siteEnd[0] = FPConstants.SITEA.toString();
				LOGGER.info(" site End {}", siteEnd[0]);
			} else if (illSiteOpt.get().getId().equals(quoteNplLinkOpt.get().getSiteBId())) {
				siteEnd[0] = FPConstants.SITEB.toString();
				LOGGER.info("site End {}", siteEnd[0]);
			}
		}

		newLLBwWithUnit = nplPricingFeasibilityService.getNewBandwidthtLM(illSiteOpt.get(),
				FPConstants.LAST_MILE.toString(), FPConstants.LOCAL_LOOP_BW.toString(), siteEnd[0]);
		LOGGER.info("Bandwidth from pricing service {}", newLLBwWithUnit);
		if (Objects.nonNull(newLLBwWithUnit) && StringUtils.isNotEmpty(newLLBwWithUnit)) {

			String[] bandWidthValue = newLLBwWithUnit.split(" ");
			newLLBw = Double.parseDouble(bandWidthValue[0]);
		}

		LOGGER.info("New LL bw for IAS{}", newLLBw);
		// Double.parseDouble(
		// illPricingFeasibilityService.getNewBandwidth(illSiteOpt.get(),
		// FPConstants.LAST_MILE.toString(),
		// FPConstants.LOCAL_LOOP_BW.toString()));

		LOGGER.info("New local bw for product{} -> bandwidthvalue -> {}", newLLBw);

		oldLlBw = Double.parseDouble(nplPricingFeasibilityService.setBandwidthConversion(oldLlBw.toString(), bwUnitLl));
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

	/**
	 * @author SURUCHIA
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used to return the
	 *            bandwidth - Port if changed or not
	 * @return changeInPortBw
	 */

	private String getPortBwChangeNPL(Optional<QuoteIllSite> illSiteOpt, String serviceId,
			Optional<QuoteToLe> quoteToLeOptional, Integer linkId) throws TclCommonException {

		Double newPortBw = 0D;
		String bwPortWithUnit = null;

		String bwUnitPort = nplPricingFeasibilityService.getOldBandwidthUnit(serviceId,
				FPConstants.PORT_BANDWIDTH_UNIT.toString());

		Double oldPortBw = Double.parseDouble(
				nplPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString()));
		LOGGER.info("OLd port Bandwidth in getPortBwChange method before BW Conversion NPL {}", oldPortBw);

		bwPortWithUnit = nplPricingFeasibilityService.getNewBandwidthPort(linkId,
				FPConstants.NATIONAL_CONNECTIVITY.toString(), FPConstants.PORT_BANDWIDTH.toString(),
				MACDConstants.LINK);
		LOGGER.info("Bandwidth from pricing service {}", bwPortWithUnit);
		String[] bandWidthValue = bwPortWithUnit.split(" ");
		newPortBw = Double.parseDouble(bandWidthValue[0]);

		LOGGER.info("New port bw for NPL{}", newPortBw);

		oldPortBw = Double
				.parseDouble(nplPricingFeasibilityService.setBandwidthConversion(oldPortBw.toString(), bwUnitPort));
		LOGGER.info("OLd port Bandwidth in getPortBwChange method after BW Conversion NPL {}", oldPortBw);

		String changeInPortBw = "";

		if (Objects.nonNull(oldPortBw) && Objects.nonNull(newPortBw) && 0D != newPortBw) {

			LOGGER.info("Before Comparison, for changeinLLBW old new bandwidth is {} ", oldPortBw + "new bandwidth  {}",
					newPortBw);

			int result = newPortBw.compareTo(oldPortBw);

			if (result > 0)
				changeInPortBw = CommonConstants.UPGRADE;
			else if (result < 0)
				changeInPortBw = CommonConstants.DOWNGRADE;
			else if (result == 0)
				changeInPortBw = CommonConstants.EQUAL;

		}
		LOGGER.info("Port Bw inside getPortBwChange method{}", changeInPortBw);
		return changeInPortBw;

	}

	private String isBSOChanged(SIServiceDetailDataBean sIServiceDetailDataBean, QuoteIllSite quoteIllSite,
			String productName, String type) throws TclCommonRuntimeException {
		String isBSOChanged = CommonConstants.NO;
		String[] newLastMileProvider = { null };
		String[] newAccessType = { null };
		String[] newVendorId = { null };
		String[] newVendorName = { null };
		String oldLastMileType = null;
		String newLastMileType = null;
		String[] oldVendorId = { null };
		String[] oldVendorName = { null };
		LOGGER.info("quoteIllSite Id {}, productName {}, type {}", quoteIllSite.getId(), productName, type);
		List<SiteFeasibility> siteFeasibilityList = siteFeasibilityRepository
				.findByQuoteIllSite_IdAndIsSelectedAndType(quoteIllSite.getId(), CommonConstants.BACTIVE, type);
		if (siteFeasibilityList != null && !siteFeasibilityList.isEmpty()) {
			siteFeasibilityList.stream().forEach(siteFeasibility -> {
				LOGGER.info("site feasibility -provider  :: {}", siteFeasibility.getProvider());
				newLastMileProvider[0] = siteFeasibility.getProvider();
				newAccessType[0] = siteFeasibility.getFeasibilityMode();
				if (Objects.nonNull(newAccessType[0])
						&& newAccessType[0].toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE)) {
					try {
						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObj = (JSONObject) jsonParser.parse(siteFeasibility.getResponseJson());
						newVendorId[0] = (String) jsonObj.get(MACDConstants.VENDOR_ID);
						newVendorName[0] = (String) jsonObj.get(MACDConstants.VENDOR_NAME);
					} catch (org.json.simple.parser.ParseException e) {
						LOGGER.info("Exception {}", e.getMessage());
						throw new TclCommonRuntimeException(e);
					}
				}
			});
		}
		if (Objects.nonNull(sIServiceDetailDataBean.getAttributes())
				&& !sIServiceDetailDataBean.getAttributes().isEmpty()) {
			sIServiceDetailDataBean.getAttributes().stream().forEach(attribute -> {
				if (attribute.getName().equalsIgnoreCase(MACDConstants.VENDOR_ID))
					oldVendorId[0] = attribute.getValue();
				else if (attribute.getName().equalsIgnoreCase(MACDConstants.VENDOR_NAME))
					oldVendorName[0] = attribute.getValue();
			});
		}
		LOGGER.info("New Values :: LAST MILE -provider {}, access Type {}, vendor id {}, vendor name {}",
				newLastMileProvider, newAccessType, newVendorId, newVendorName);
		LOGGER.info(
				"Service inventory last mile provider {}, service inventory access type {}, old vendor id {}, old vendor name {} ",
				sIServiceDetailDataBean.getAccessProvider(), sIServiceDetailDataBean.getAccessType(), oldVendorId,
				oldVendorName);

		/* if (sIServiceDetailDataBean.getAccessType() != null) */
		 if (sIServiceDetailDataBean.getLmType() != null && 
				 !MACDConstants.OFFNET_SMALL_CASE.equalsIgnoreCase(sIServiceDetailDataBean.getLmType())) {
			 LOGGER.info("passing lm type {}", sIServiceDetailDataBean.getLmType());
			oldLastMileType = normaliseLastMileType(sIServiceDetailDataBean.getLmType());
		 } else if(sIServiceDetailDataBean.getAccessType() != null) {
			 LOGGER.info("passing access type {}", sIServiceDetailDataBean.getAccessType());
			 oldLastMileType = normaliseLastMileType(sIServiceDetailDataBean.getAccessType());
		 }
		if (newAccessType[0] != null)
			newLastMileType = normaliseLastMileType(newAccessType[0]);
		LOGGER.info("oldLastMileType {}, newLastMileType {}", oldLastMileType, newLastMileType);

		if (newLastMileType != null && MACDConstants.MACD.equalsIgnoreCase(newLastMileType))
			isBSOChanged = CommonConstants.NO;
		else if (oldLastMileType != null && newLastMileType != null
				&& oldLastMileType.equalsIgnoreCase(newLastMileType)
				&& newLastMileProvider[0] != null
				&& sIServiceDetailDataBean.getAccessProvider() != null) {
			if (newLastMileType.equals(MACDConstants.ONNET_WIRELINE))
				isBSOChanged = CommonConstants.NO;
			else if (newLastMileType.equals(MACDConstants.ONNET_RF)) {
				LOGGER.info("entering onnetRF comparison");
				if ((sIServiceDetailDataBean.getAccessProvider().contains("PMP")
						&& !newLastMileProvider[0].contains("PMP"))
						|| (!sIServiceDetailDataBean.getAccessProvider().contains("PMP")
								&& newLastMileProvider[0].contains("PMP")))
					isBSOChanged = CommonConstants.YES;
			} else if (newLastMileType.equals(MACDConstants.OFFNET_RF)
					|| newLastMileType.equals(MACDConstants.OFFNET_WIRELINE)) {
				LOGGER.info("entering offnetRF comparison");
				if (oldVendorId[0] != null && newVendorId[0] != null && !oldVendorId[0].equals(newVendorId[0]))
					isBSOChanged = CommonConstants.YES;
			}
		} else if (newLastMileType != null && oldLastMileType != null
				&& !oldLastMileType.equalsIgnoreCase(newLastMileType)
				&& ((oldLastMileType.toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE)
						&& !newLastMileType.toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE))
						|| (oldLastMileType.toLowerCase().contains(MACDConstants.ONNET_SMALL_CASE)
								&& !newLastMileType.toLowerCase().contains(MACDConstants.ONNET_SMALL_CASE))
						|| (oldLastMileType.toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE)
								&& newLastMileType.toLowerCase().contains(MACDConstants.OFFNET_SMALL_CASE))
						|| (oldLastMileType.toLowerCase().contains(MACDConstants.ONNET_SMALL_CASE)
								&& newLastMileType.toLowerCase().contains(MACDConstants.ONNET_SMALL_CASE)))) {
			LOGGER.info("entering onnet offnet comparison");
			isBSOChanged = CommonConstants.YES;
		}

		LOGGER.info("BSO changed flag {}", isBSOChanged);

		return isBSOChanged;

	}

	private String normaliseLastMileType(String lastMileType) {
		String nLastMileType = null;
		if (lastMileType.toLowerCase().contains("onnet wireless"))
			nLastMileType = MACDConstants.ONNET_RF;
		else if (lastMileType.toLowerCase().contains("offnet wireless"))
			nLastMileType = MACDConstants.OFFNET_RF;
		else if (lastMileType.toLowerCase().contains("offnet wireline"))
			nLastMileType = MACDConstants.OFFNET_WIRELINE;
		else if (lastMileType.toLowerCase().contains("onnet wireline"))
			nLastMileType = MACDConstants.ONNET_WIRELINE;
		LOGGER.info("input last mile type {}, nLastMileType {}", lastMileType, nLastMileType);
		if (nLastMileType == null)
			nLastMileType = lastMileType;
		return nLastMileType;

	}
	
	public void processUpdateProductIzosdwan(QuoteToLe quoteToLe) {
		try {
			LOGGER.info("Inside update product IZOSDWAN!!!");
			bundleOmsSfdcService.processUpdateProduct(quoteToLe);
		} catch (TclCommonException e) {
			LOGGER.error("Error on product update IZOSDWAN",e);
		}
	}
	
	/**
     * retriggerCreateOpty3DMAPD
     *
     * @param quoteToLe
     * @param productName
     * @throws TclCommonException
     */
    @Transactional
    public void retriggerCreateOpty3D(Mf3DResponse mf3DResponse) throws TclCommonException {
        LOGGER.info("OmsSfdcService.retriggerCreateOpty3D method invoked");
        LOGGER.info("SfdcOptyId not exists");
        try {
            if(mf3DResponse.getQuoteCode().isEmpty() || mf3DResponse.getQuoteId()==0 || mf3DResponse.getCustomerId().isEmpty()
                    || mf3DResponse.getCurrencyIsoCode().isEmpty() || mf3DResponse.getProductName().isEmpty()) {
                LOGGER.info("SfdcOptyId not exists validation error");
                throw new TclCommonRuntimeException(ExceptionConstants.REQUEST_INVALID, ResponseResource.R_CODE_ERROR);
            }
            else {
                LOGGER.info("SfdcOptyId not exists validation sucess");
                OpportunityBean opportunityBean = new OpportunityBean();
                String orderCode = mf3DResponse.getQuoteCode();
                LOGGER.info("orderCode::" + orderCode);
                LOGGER.info("quote::" + mf3DResponse.getQuoteId());
                opportunityBean.setName("Optimus Opportunity -" + mf3DResponse.getQuoteId());
                opportunityBean.setDescription(
                        "Creating opportunity for the order " + mf3DResponse.getQuoteId() + " on " + new Date());
                opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
                opportunityBean.setOpportunityClassification(SFDCConstants.SELL_TO);
                opportunityBean.setReferralToPartner(SFDCConstants.NO);
                opportunityBean.setType(SFDCConstants.NEW_ORDER);
                opportunityBean.setSubType(SFDCConstants.NEW_ORDER);
                opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
                opportunityBean.setCurrencyIsoCode(mf3DResponse.getCurrencyIsoCode());
                opportunityBean.setCustomerChurned(SFDCConstants.NO);
                opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 30);
                opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
                opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
                opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
                opportunityBean.setIsPartnerOrder(SFDCConstants.OTHERS);
                opportunityBean.setPortalTransactionId(SFDCConstants.PRE_MF_FEASIBILITY +"_" +orderCode);
                opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
                CustomerDetailsBean customerDetails = processCustomerData(
                        Integer.parseInt(mf3DResponse.getCustomerId()));
                if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
                    setSfdcAccId(opportunityBean, customerDetails);
                } else {
                    opportunityBean.setAccountId(accountId);
                }
                LOGGER.info("Product name for create opportunity method {} ", mf3DResponse.getProductName());
                opportunityBean.setSelectProductType(mf3DResponse.getProductName());
                LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunityBean.getType());
                LOGGER.info("opportunitybean" + opportunityBean.getType() + " " + opportunityBean.getSubType());
                if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
                    if (appEnv.equals(SFDCConstants.PROD)) {
                        LOGGER.info("OPPORTUNITY USER EMAIL ID {}", Utils.getSource());
                        opportunityBean.setOwnerName(Utils.getSource());
                    } else {
                        opportunityBean.setOwnerName(null);
                    }
                } else {

 

                    String custId = mf3DResponse.getCustomerId();
                    String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
                    opportunityBean.setOwnerName(name);
                }

 

                persistSfdcServiceJob(mf3DResponse.getQuoteCode(), sfdcCreateOpty,
                        Utils.convertObjectToJson(opportunityBean), CommonConstants.BACTIVE, SFDCConstants.CREATE_OPTY,
                        getSequenceNumber(SFDCConstants.CREATE_OPTY));

 

                LOGGER.info("OmsSfdcService.retriggerCreateOpty3D method exited");
            }
            
        } catch (Exception e) {
            if (e instanceof TclCommonRuntimeException) {
                throw e;
            } else
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }

 

    }
    
    
/**
	
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited this method is used
	 * to update the order type and subtype in SFDC - SHIFT_SITE + CHANGE_BANDWIDTH Scenario
	 * @return OpportunityBean
	 */

	private OpportunityBean isParallelShifting(OpportunityBean opportunityBean, String cityType, String oldehsid, String linkid,
			String circuitBwChnage, String L1Bwchnage, String parralleldays) {
		LOGGER.info("Entering isParallelShifting");

		if (CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType)) {
			String ehsidNew = "";
			MstProductComponent productComponent = mstProductComponentRepository.findByName("National Connectivity");
			List<QuoteProductComponent> components = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent(Integer.parseInt(linkid), productComponent);
			if (components.size() != 0) {
				Set<QuoteProductComponentsAttributeValue> attributes = components.get(0)
						.getQuoteProductComponentsAttributeValues();
				if (attributes != null) {
					for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
						if (quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()
								.equals(NplPDFConstants.HUB_PARENTED_ID)) {
							if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
								ehsidNew = quoteProductComponentsAttributeValue.getAttributeValues();
						}
					}
				}

			}
			LOGGER.info("new and old ehs ids" + "new:" + ehsidNew + " old:" + oldehsid);
			if (!oldehsid.isEmpty() && !ehsidNew.isEmpty()) {
				if (!oldehsid.equalsIgnoreCase(ehsidNew) && !circuitBwChnage.equalsIgnoreCase(CommonConstants.EQUAL)) {
					LOGGER.info("EHS ID change and circuitBw change ");
					opportunityBean.setType("Parallel Shifitng");
					opportunityBean.setSubType("Parallel Shifitng");

				if (!oldehsid.equalsIgnoreCase(ehsidNew) && circuitBwChnage.equalsIgnoreCase(CommonConstants.EQUAL)) {
					LOGGER.info("EHS ID change and circuitBw not change ");
					opportunityBean.setType("Parallel Shifitng");
					opportunityBean.setSubType("Parallel Shifitng");

				}
			} 
			}
			
		}
		LOGGER.info("Value of order type after isParallelUpgradeOrDowngradeForIntra {}", opportunityBean.getType());

		return opportunityBean;
	}
	/**
	 * getMstAttributeMaster
	 *
	 * @param propName
	 * @param username
	 * @return
	 */

	private MstOmsAttribute getMstAttributeMaster(String propName, String username) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(propName,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}

		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(username);
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(propName);
			mstOmsAttribute.setDescription(propName);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}
    
	
	/**
	 * processSiteDetails- This method is used to process the site details
	 *
	 * @param orderToLe
	 * @param optyId
	 * @param productServiceId
	 * @throws TclCommonException
	 */
	@Transactional
	public void processSiteDetailsForCancellation(QuoteToLe quoteToLe) throws TclCommonException {
		Map<String, SFDCCommercialBifurcationBean> commercialsMap = new HashMap<>();
		List<QuoteToLeProductFamily> quoteLeProdFamilyRepo = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteLeProdFamilyRepo) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			if (quoteToLe.getIsMultiCircuit()!=null && quoteToLe.getIsMultiCircuit().equals(CommonConstants.BACTIVE) ) {
				LOGGER.info("multicircuit flow");
				processCreateSiteSolutionMulticircuitForCancellation(quoteToLe, commercialsMap, quoteToLeProductFamily);
			} else {
				LOGGER.info("Normal flow");
				for (ProductSolution productSolution : productSolutions) {
					processCreateSiteSolutionForCancellation(quoteToLe, commercialsMap, productSolution);
				}
			}
		}
	}
	
	private void processCreateSiteSolutionForCancellation(QuoteToLe quoteToLe,
			Map<String, SFDCCommercialBifurcationBean> commercialsMap, ProductSolution productSolution)
			throws TclCommonException {
		SiteSolutionOpportunityBean siteSolutionOpportunityBean = new SiteSolutionOpportunityBean();
		MDMServiceInventoryBean serviceDetail = null;
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();
		List<QuoteIllSite> illSites = getIllsitesBasenOnVersion(productSolution);
		String productname = getFamilyName(quoteToLe);
		for (QuoteIllSite quoteIllSite : illSites) {
			LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteIllSite.getErfLocSitebLocationId()));
			serviceDetail = getMDMServiceDetails(serviceDetail, quoteIllSite, locationResponse, productname, quoteToLe);
			
			if (!("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))) {
				SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
				if(StringUtils.isBlank(locationResponse)) { 
					siteOpportunityLocation.setLocation(serviceDetail.getServiceDetailBeans().get(0).getSiteAddress());
				} else {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				addressDetail = validateAddressDetail(addressDetail);
				/* String address=addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo()+" "+addressDetail.getLocality();*/
				
				siteOpportunityLocation.setCity(addressDetail.getCity());
				siteOpportunityLocation.setCountry(addressDetail.getCountry());
				siteOpportunityLocation.setLocation(addressDetail.getCity());
				siteOpportunityLocation.setState(addressDetail.getState());
				}
				siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIllSite.getSiteCode());
				siteOpportunityLocation.setSiteMRC(quoteIllSite.getMrc());
				siteOpportunityLocation.setSiteNRC(quoteIllSite.getNrc());
				siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				siteOpportunityLocations.add(siteOpportunityLocation);
			}
			else if ("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))
			{
				LOGGER.info("Site id id ::::::  {}", quoteIllSite.getId());
				String referenceName = productname.equalsIgnoreCase("IAS")
						?QuoteConstants.ILLSITES.toString():productname.equalsIgnoreCase("GVPN")?QuoteConstants.GVPN_SITES.toString():"";
				LOGGER.info("Reference Name is ::::::  {}", referenceName);
			List<QuoteProductComponent> productComponent =
					quoteProductComponentRepository.findByReferenceIdAndReferenceName(quoteIllSite.getId(), referenceName);
			LOGGER.info("Product Components size for quote ill site :::: {}  is ::::  {}  ", quoteIllSite.getId(), productComponent.size());
			productComponent.forEach(component -> {
				QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(component.getId()), MACDConstants.COMPONENTS);
				if (Objects.nonNull(quotePrice)) {
					LOGGER.info("Inside quoteprice if block with nrc ::: {}", quotePrice.getEffectiveNrc());
					populateSiteOpportunityLocationForPrimaryAndSecondary(component.getType(), commercialsMap, quotePrice,quoteToLe);
					LOGGER.info("Map Value :: {}", commercialsMap);
				}
			});
			//SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
			for (Map.Entry<String, SFDCCommercialBifurcationBean> bifurcationBeanEntry : commercialsMap.entrySet()) {
				String productComponentType = bifurcationBeanEntry.getKey();
				String circuitId = "";
				if (Objects.nonNull(quoteToLe)
						&& ((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(quoteToLe.getQuoteType()) || MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType()))) {
					Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite, quoteToLe);
					LOGGER.info("ServiceIds" + serviceIds);
					if (productComponentType.equalsIgnoreCase(PDFConstants.PRIMARY)) {
						/*circuitId = Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId()) ? quoteToLe.getErfServiceInventoryTpsServiceId() : "";*/
						circuitId = serviceIds.get(PDFConstants.PRIMARY);
					} else if (productComponentType.equalsIgnoreCase(PDFConstants.SECONDARY)) {
						/*circuitId = secondaryCircuitId;*/
						circuitId = serviceIds.get(PDFConstants.SECONDARY);
					}
				}
				SFDCCommercialBifurcationBean bifurcationBean = bifurcationBeanEntry.getValue();
				SiteOpportunityLocation siteOpportunityLocation = setSiteOpportunityLocationForSingleAndDualCircuitsForCancellation(quoteToLe, quoteIllSite, locationResponse, bifurcationBean.getNrc(),
						bifurcationBean.getMrc(), circuitId, serviceDetail);
				siteOpportunityLocations.add(siteOpportunityLocation);
			}
		}
		}
		siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
		siteSolutionOpportunityBean.setProductServiceId(productSolution.getTpsSfdcProductId());
		siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);
		siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
		siteSolutionOpportunityBean.setProductSolutionCode(productSolution.getSolutionCode());
		siteSolutionOpportunityBean
				.setSourceSytemTransactionId(SFDCConstants.OPTIMUS + productSolution.getSolutionCode());
		Byte isComplete = CommonConstants.BDEACTIVATE;
		if (StringUtils.isNotBlank(productSolution.getTpsSfdcProductId())
				&& StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())) {
			isComplete = CommonConstants.BACTIVE;
		}

		// included to handle site update issue for NPL
		String familyName = getFamilyName(quoteToLe);
		if (familyName != null && familyName.equalsIgnoreCase(CommonConstants.NPL) || familyName != null && familyName.equalsIgnoreCase(CommonConstants.NDE)) {
			IOmsSfdcInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.processSiteDetails(quoteToLe, siteSolutionOpportunityBean, productSolution);
			}
		}

		String request = Utils.convertObjectToJson(siteSolutionOpportunityBean);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateSite, request, isComplete,
				SFDCConstants.UPDATE_SITE, getSequenceNumber(SFDCConstants.UPDATE_SITE));
	}

	private MDMServiceInventoryBean getMDMServiceDetails(MDMServiceInventoryBean serviceDetail,
			QuoteIllSite quoteIllSite, String locationResponse, String productName, QuoteToLe quoteToLe) throws TclCommonException {
		if(StringUtils.isBlank(locationResponse)) { 
			if("IAS".equalsIgnoreCase(productName) || "GVPN".equalsIgnoreCase(productName) || "IZOPC".equalsIgnoreCase(productName)) {
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
					.findByQuoteIllSite(quoteIllSite);
			if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
			serviceDetail = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null,
					quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId(), null, null);
			}
		} else if("NPL".equalsIgnoreCase(productName) || "NDE".equalsIgnoreCase(productName)){
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
					.findByQuoteToLe_Id(quoteToLe.getId());
			if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
			serviceDetail = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null,
					quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId(), null, null);
			}
		}
		} 
		return serviceDetail;
	}
	
	private void processCreateSiteSolutionMulticircuitForCancellation(QuoteToLe quoteToLe,
			Map<String, SFDCCommercialBifurcationBean> commercialsMap, QuoteToLeProductFamily quoteToLeProductFamily)
			throws TclCommonException {
		List<ProductSolution> productSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		SiteSolutionOpportunityBean siteSolutionOpportunityBean = new SiteSolutionOpportunityBean();
		MDMServiceInventoryBean serviceDetail = null;
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();
		siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
		siteSolutionOpportunityBean.setProductSolutionCode("MLC-" + quoteToLe.getQuote().getQuoteCode());
		siteSolutionOpportunityBean
				.setSourceSytemTransactionId(SFDCConstants.OPTIMUS + "MLC-" + quoteToLe.getQuote().getQuoteCode());
		Byte isComplete = CommonConstants.BDEACTIVATE;
		String productname = getFamilyName(quoteToLe);
		for (ProductSolution productSolution : productSolutions) {
			List<QuoteIllSite> illSites = getIllsitesBasenOnVersion(productSolution);
			for (QuoteIllSite quoteIllSite : illSites) {
				LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(quoteIllSite.getErfLocSitebLocationId()));
				serviceDetail = getMDMServiceDetails(serviceDetail, quoteIllSite, locationResponse, productname, quoteToLe);
				
				if (!("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname))) {
					SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
					if(StringUtils.isBlank(locationResponse)) { 
						siteOpportunityLocation.setLocation(serviceDetail.getServiceDetailBeans().get(0).getSiteAddress());
					} else {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					addressDetail = validateAddressDetail(addressDetail);
					siteOpportunityLocation.setCity(addressDetail.getCity());
					siteOpportunityLocation.setCountry(addressDetail.getCountry());
					siteOpportunityLocation.setLocation(addressDetail.getCity());
					siteOpportunityLocation.setState(addressDetail.getState());
				}
					siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIllSite.getSiteCode());
					siteOpportunityLocation.setSiteMRC(quoteIllSite.getMrc());
					siteOpportunityLocation.setSiteNRC(quoteIllSite.getNrc());
					siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
					siteOpportunityLocations.add(siteOpportunityLocation);
				} else if ("IAS".equalsIgnoreCase(productname) || "GVPN".equalsIgnoreCase(productname)) {
					LOGGER.info("Site id id ::::::  {}", quoteIllSite.getId());
					String referenceName = productname.equalsIgnoreCase("IAS") ? QuoteConstants.ILLSITES.toString()
							: productname.equalsIgnoreCase("GVPN") ? QuoteConstants.GVPN_SITES.toString() : "";
					LOGGER.info("Reference Name is ::::::  {}", referenceName);
					List<QuoteProductComponent> productComponent = quoteProductComponentRepository
							.findByReferenceIdAndReferenceName(quoteIllSite.getId(), referenceName);
					LOGGER.info("Product Components size for quote ill site :::: {}  is ::::  {}  ",
							quoteIllSite.getId(), productComponent.size());
					productComponent.forEach(component -> {
						QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
								String.valueOf(component.getId()), MACDConstants.COMPONENTS);
						if (Objects.nonNull(quotePrice)) {
							LOGGER.info("Inside quoteprice if block with nrc ::: {}", quotePrice.getEffectiveNrc());
							populateSiteOpportunityLocationForPrimaryAndSecondary(component.getType(), commercialsMap,
									quotePrice, quoteToLe);
							LOGGER.info("Map Value :: {}", commercialsMap);
						}
					});
					// SiteOpportunityLocation siteOpportunityLocation = new
					// SiteOpportunityLocation();
					for (Map.Entry<String, SFDCCommercialBifurcationBean> bifurcationBeanEntry : commercialsMap
							.entrySet()) {
						String productComponentType = bifurcationBeanEntry.getKey();
						String circuitId = "";
						if (Objects.nonNull(quoteToLe)
								&& ((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(quoteToLe.getQuoteType()))) {
							Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite,
									quoteToLe);
							LOGGER.info("ServiceIds" + serviceIds);
							if (productComponentType.equalsIgnoreCase(PDFConstants.PRIMARY)) {
								/*
								 * circuitId = Objects.nonNull(quoteToLe.getErfServiceInventoryTpsServiceId()) ?
								 * quoteToLe.getErfServiceInventoryTpsServiceId() : "";
								 */
								circuitId = serviceIds.get(PDFConstants.PRIMARY);
							} else if (productComponentType.equalsIgnoreCase(PDFConstants.SECONDARY)) {
								/* circuitId = secondaryCircuitId; */
								circuitId = serviceIds.get(PDFConstants.SECONDARY);
							}
						}
						SFDCCommercialBifurcationBean bifurcationBean = bifurcationBeanEntry.getValue();
						SiteOpportunityLocation siteOpportunityLocation = setSiteOpportunityLocationForSingleAndDualCircuitsForCancellation(
								quoteToLe, quoteIllSite, locationResponse, bifurcationBean.getNrc(),
								bifurcationBean.getMrc(), circuitId, serviceDetail);
						siteOpportunityLocations.add(siteOpportunityLocation);
					}
				}
			}
			siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			siteSolutionOpportunityBean.setProductServiceId(productSolution.getTpsSfdcProductId());
			siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);
			if (StringUtils.isNotBlank(productSolution.getTpsSfdcProductId())
					&& StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())) {
				isComplete = CommonConstants.BACTIVE;
			}

			// included to handle site update issue for NPL
			String familyName = getFamilyName(quoteToLe);
			if (familyName != null && familyName.equalsIgnoreCase(CommonConstants.NPL)
					|| familyName != null && familyName.equalsIgnoreCase(CommonConstants.NDE)) {
				IOmsSfdcInputHandler handler = factory.getInstance(familyName);
				if (handler != null) {
					handler.processSiteDetails(quoteToLe, siteSolutionOpportunityBean, productSolution);
				}
			}
		}
		if (siteSolutionOpportunityBean.getSiteOpportunityLocations() != null
				&& !siteSolutionOpportunityBean.getSiteOpportunityLocations().isEmpty()) {
			String request = Utils.convertObjectToJson(siteSolutionOpportunityBean);
			persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateSite, request, isComplete,
					SFDCConstants.UPDATE_SITE, getSequenceNumber(SFDCConstants.UPDATE_SITE));
		}
	}

	
	private SiteOpportunityLocation setSiteOpportunityLocationForSingleAndDualCircuitsForCancellation(QuoteToLe quoteToLe, QuoteIllSite quoteIllSite, String locationResponse, Double nrc, Double mrc,String serviceId, MDMServiceInventoryBean serviceDetail) throws TclCommonException {
		SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
		if(StringUtils.isBlank(locationResponse)) {
			siteOpportunityLocation.setLocation(serviceDetail.getServiceDetailBeans().get(0).getSiteAddress());
		} else {
		AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
				AddressDetail.class);
		addressDetail = validateAddressDetail(addressDetail);		
		siteOpportunityLocation.setCity(addressDetail.getCity());
		siteOpportunityLocation.setCountry(addressDetail.getCountry());
		siteOpportunityLocation.setLocation(addressDetail.getCity());
		siteOpportunityLocation.setState(addressDetail.getState());
		}
		siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIllSite.getSiteCode());
		siteOpportunityLocation.setSiteMRC(mrc);
		siteOpportunityLocation.setSiteNRC(nrc);
		// MACD COPF implementation
		if (Objects.nonNull(quoteToLe)
				&& ((MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(quoteToLe.getQuoteType())) || MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			siteOpportunityLocation.setCurrentCircuitServiceId(serviceId);
		}
		
		return siteOpportunityLocation;
	}

	public void processCreateRenewalsOpty(QuoteToLe quoteToLe, String productName, Character isCommercial, String oppId, RenewalsSfdcObjectBean renewalsSfdcObjectBean)
			throws TclCommonException, ParseException {
		LOGGER.info("OmsSfdcService.processCreateOpty method invoked");
		if (StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
			// Get the OrderLeAttributes
			LOGGER.info("SfdcOptyId not exists");
			OpportunityBean opportunityBean = new OpportunityBean();

			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Quote exists");
				String orderCode = quote.getQuoteCode();
				LOGGER.info("orderCode::" + orderCode);
				LOGGER.info("quote::" + quote.getId());
				opportunityBean.setName("Optimus Opportunity -" + quote.getId());
				opportunityBean
						.setDescription("Creating opportunity for the order " + quote.getId() + " on " + new Date());
				opportunityBean.setOrderCategory(SFDCConstants.CAT_2);
				opportunityBean.setReferralToPartner(SFDCConstants.NO);
	
				opportunityBean.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
				String currency = SFDCConstants.INR;
				if (quoteToLe.getCurrencyCode() != null) {
					currency = quoteToLe.getCurrencyCode();
				}
				opportunityBean.setOpportunityClassification(SFDCConstants.SELL_TO);
				opportunityBean.setCurrencyIsoCode(currency);
				opportunityBean.setCustomerChurned(SFDCConstants.NO);
				opportunityBean.setiLLAutoCreation(CommonConstants.EMPTY);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
				opportunityBean.setWinLossRemarks(CommonConstants.EMPTY);
				opportunityBean.setCofType(SFDCConstants.OPTIMUS_COF);
				if ("Partner".equalsIgnoreCase(userInfoUtils.getUserType())) {
					opportunityBean.setIsPartnerOrder(SFDCConstants.PARTNER);
					if (quoteToLe.getClassification() != null) {
						opportunityBean.setOpportunityClassification(quoteToLe.getClassification());
					}
					opportunityBean.setOpportunityClassification(SFDCConstants.SELL_WITH_CLASSIFICATION);
				} else {
					opportunityBean.setIsPartnerOrder(SFDCConstants.OTHERS);
				}
				opportunityBean.setPortalTransactionId(SFDCConstants.OPTIMUS + orderCode);
				opportunityBean.setMigrationSourceSystem(SFDCConstants.OPTIMUS);
				opportunityBean.setCurrentCircuitServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
				opportunityBean.setParentOpportunityName(oppId);
				LOGGER.info("Get customer details::" + quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
					setSfdcAccId(opportunityBean, customerDetails);
				} else {
					opportunityBean.setAccountId(accountId);
				}

				/*
				 * if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.
				 * toString())) { if (appEnv.equals(SFDCConstants.PROD)) {
				 * LOGGER.info("OPPORTUNITY USER EMAIL ID {}", Utils.getSource());
				 * opportunityBean.setOwnerName(Utils.getSource()); } else {
				 * opportunityBean.setOwnerName(null); } } else { if
				 * (appEnv.equals(SFDCConstants.PROD)) { String custId =
				 * quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString(); String
				 * name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
				 * opportunityBean.setOwnerName(name); } else {
				 * opportunityBean.setOwnerName(null); } }
				 */
				LOGGER.info("Product name for create opportunity method {} ", productName);
				if (productName != null) {
					IOmsSfdcInputHandler handler = factory.getInstance(productName);
					if (handler != null) {
						handler.getOpportunityBean(quoteToLe, opportunityBean);
					}
				}
				
				if(renewalsSfdcObjectBean.getServiceIdList().size() > 1) {
					opportunityBean.setCurrentCircuitServiceId(renewalsSfdcObjectBean.getServiceIdList().get(0) + "- Multiple");
					if(renewalsSfdcObjectBean.getCopfId()!=null && renewalsSfdcObjectBean.getCopfId()!="") {
					//opportunityBean.setCopfIdC(renewalsSfdcObjectBean.getCopfId()+"- Multiple");
					opportunityBean.setCopfIdC(renewalsSfdcObjectBean.getCopfId());
					}
				} else {
					opportunityBean.setCurrentCircuitServiceId(renewalsSfdcObjectBean.getServiceIdList().get(0));
					opportunityBean.setCopfIdC(renewalsSfdcObjectBean.getCopfId());
				}
				Date formattedDate = DateUtils.parseDate(renewalsSfdcObjectBean.getEffectiveDate(), 
						  new String[] { "dd-MMM-yyyy", "yyyy-MM-dd" });
				DateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate1 = dateFormatter1.format(formattedDate);
				opportunityBean.setEffectiveDateOfChange(formattedDate1.toString());
				opportunityBean.setPreviousMRC(renewalsSfdcObjectBean.getPreviousMrc().toString());
				opportunityBean.setLeadTimeToRFSC("30");
				if (isCommercial.equals('Y')) {
					opportunityBean.setType(SFDCConstants.RENEWALS_WITH_REVESION);
					opportunityBean.setSubType(SFDCConstants.RENEWALS_WITH_REVESION);
				} else {
					opportunityBean.setType(SFDCConstants.RENEWALS_WITH_OUT_REVESION);
					opportunityBean.setSubType(SFDCConstants.RENEWALS_WITH_OUT_REVESION);
				}

				LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunityBean.getType());
				LOGGER.info("opportunitybean" + opportunityBean.getType() + " " + opportunityBean.getSubType());

				/*
				 * if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
				 * {
				 * 
				 * opportunityBean.setOwnerName(null); }
				 */
				quoteToLeRepository.save(quoteToLe);
				persistSfdcServiceJob(quote.getQuoteCode(), sfdcCreateOpty, Utils.convertObjectToJson(opportunityBean),
						CommonConstants.BACTIVE, CREATE_OPTY, getSequenceNumber(CREATE_OPTY));
			}
		}
		LOGGER.info("OmsSfdcService.processCreateOpty method exited");
	}

}
