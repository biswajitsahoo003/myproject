package com.tcl.dias.oms.npl.macd.service.v1;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.tcl.dias.common.sfdc.response.bean.FResponse;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.partner.beans.PartnerOpportunityBean;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.CompareQuotePrices;
import com.tcl.dias.oms.beans.CompareQuotes;
import com.tcl.dias.oms.beans.ComponentQuotePrices;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.TotalSolutionQuote;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteAccessPermission;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteAccessPermissionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.macd.beans.MACDAttributesBean;
import com.tcl.dias.oms.macd.beans.MACDAttributesComparisonBean;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponseBean;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.beans.NplMACDRequest;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.NplSite;
import com.tcl.dias.oms.npl.beans.NplSiteDetail;
import com.tcl.dias.oms.npl.constants.NplOrderConstants;
import com.tcl.dias.oms.npl.constants.SLAConstants;
import com.tcl.dias.oms.npl.pdf.beans.NplLinkDetailBean;
import com.tcl.dias.oms.npl.pdf.beans.NplMcQuoteDetailBean;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import static com.tcl.dias.common.constants.CommonConstants.IAS;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.PARTNER;

@Service
public class NplMACDService extends NplQuoteService {

	public static final Logger LOGGER = LoggerFactory.getLogger(NplMACDService.class);

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;

	@Value("${rabbitmq.si.npl.details.queue}")
	String siNplDetailsQueue;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Value("${rabbitmq.location.details.feasibility}")
	protected String locationDetailsQueue;

	@Autowired
	NplOrderService nplOrderService;

	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;
	
	@Autowired
	QuoteAccessPermissionRepository quoteAccessPermissionRepository;
	
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;
	
	@Value("${rabbitmq.si.npl.details.mc.queue}")
	String siServiceDetailsListMcQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	PartnerService partnerService;

	/**
	 * @param macdRequest
	 * @return
	 * @throws TclCommonException
	 * @author Harini Sri Reka J Method for handling macd request to create quote
	 *         with macd type
	 */

	public MacdQuoteResponse handleMacdRequestToCreateQuote(NplMACDRequest macdRequest) throws TclCommonException {
		try {
			validateMacdQuoteRequest(macdRequest);
			return createMacdQuote(macdRequest);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void validateMacdQuoteRequest(NplMACDRequest macdRequest) throws TclCommonException {
		String[] quoteTypes = { MACDConstants.SHIFT_SITE_SERVICE, MACDConstants.CHANGE_BANDWIDTH_SERVICE };

		List<String> quoteTypeList = Arrays.asList(quoteTypes);
		if (Objects.nonNull(macdRequest)) {
			if (Objects.nonNull(macdRequest.getRequestType()) && !quoteTypeList.contains(macdRequest.getRequestType())
					|| StringUtils.isBlank(macdRequest.getRequestType())) {
				String errorMessage = String.format("Business validation failed:Invalid MACD request type %s ",
						macdRequest.getRequestType());
				LOGGER.warn(errorMessage);
				throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			}

			if (macdRequest.getServiceDetails() == null || macdRequest.getServiceDetails().isEmpty()) {

				LOGGER.info("Service Details Empty");
				throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			}
			validateQuoteDetail(macdRequest.getQuoteRequest());// validating the input for create Quote
		} else {
			throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		}

	}

	/**
	 * This method validates the Quote Details Request validateQuoteDetail
	 *
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	protected void validateQuoteDetail(NplQuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * @param request
	 * @return QuoteDetail
	 * @throws TclCommonException
	 * @throws ParseException
	 * @author Harini Sri Reka J createQuote - This method is used to create a quote
	 *         The input validation is done and the corresponding tables are
	 *         populated with initial set of values
	 */

	public MacdQuoteResponse createMacdQuote(NplMACDRequest request) throws TclCommonException, ParseException {
		MacdQuoteResponse macdResponse = new MacdQuoteResponse();
		QuoteResponse response = null;
		Integer erfCustomerIdInt = null;
		Integer erfCustomerLeIdInt = null;
		String partnerCuid=null;
		String endCustomerName=null;
		User user = getUserId(Utils.getSource());
		if (Objects.nonNull(user)) {
			LOGGER.info("MDC Filter token value in before Queue call createMacdQuote {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String serviceIds = request.getServiceDetails().stream().map(i -> i.getServiceId().toString().trim())
					.distinct().collect(Collectors.joining(","));
			String OrderDetailsQueue = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceIds);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(OrderDetailsQueue, SIServiceDetailsBean[].class);
			List<SIServiceDetailsBean> serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
			response = new QuoteResponse();
			if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
				if (serviceDetailsList.stream().findFirst().isPresent()) {
					erfCustomerIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerId();
					erfCustomerLeIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerLeId();
					partnerCuid=serviceDetailsList.stream().findFirst().get().getPartnerCuid();
					endCustomerName=serviceDetailsList.stream().findFirst().get().getErfCustomerName();

				}
			}
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				// To reuse createPartnerOpportunityBeanForMACD the request is sent as null for NPL
				PartnerOpportunityBean partnerOpportunityBean = partnerService.createPartnerOpportunityBeanForMACD(null, serviceDetailsList, request.getQuoteRequest().getProductName());
				request.getQuoteRequest().setClassification(partnerOpportunityBean.getClassification());
				request.getQuoteRequest().setQuoteCode(partnerOpportunityBean.getOpportunityCode());
				request.getQuoteRequest().setEngagementOptyId(String.valueOf(partnerOpportunityBean.getEngagementOptyId()));
				erfCustomerIdInt = partnerOpportunityBean.getCustomerId();
				erfCustomerLeIdInt = partnerOpportunityBean.getCustomerLeId();
				LOGGER.info("MacdQuoteRequest NPL :: {}", request);
				LOGGER.info("erfCustomerIdInt :: {} , erfCustomerLeIdInt :: {}", erfCustomerIdInt, erfCustomerLeIdInt);
			}
			QuoteToLe quoteTole = processQuote(request.getQuoteRequest(), erfCustomerIdInt, user);
			if(Objects.nonNull(request.getQuoteRequest().getIsMulticircuit())&&CommonConstants.BACTIVE.equals(request.getQuoteRequest().getIsMulticircuit())) {
				quoteTole.setIsMultiCircuit(request.getQuoteRequest().getIsMulticircuit());
				quoteToLeRepository.save(quoteTole);
				LOGGER.info("QuoteToLe term " + quoteTole.getIsMultiCircuit());
		}
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				createMACDSpecificQuoteToLe(quoteTole, serviceDetailsList, erfCustomerLeIdInt,
						request.getRequestType());
				QuoteToLe quoteLe = quoteToLeRepository.save(quoteTole);
				LOGGER.info("Term in months" + quoteTole.getTermInMonths());
				LOGGER.info("Opportunity Id: " + quoteLe.getTpsSfdcParentOptyId());
				if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
					LOGGER.info("service id list size"+serviceDetailsList.size());
					//ADDED for O2C duplicate serviceinventory record fix
					List<SIServiceDetailsBean> serviceInventoryDetailsList=new ArrayList<SIServiceDetailsBean>();
					for(SIServiceDetailsBean siBean:serviceDetailsList) {
						Boolean idExisist=false;
						if(serviceInventoryDetailsList.size()==0) {
							serviceInventoryDetailsList.add(siBean);
						}
						else {
							LOGGER.info("before call isServiceIdExit"+siBean.getTpsServiceId());
							idExisist=isServiceIdExit(siBean,serviceInventoryDetailsList);
							LOGGER.info("idExisist flag value"+idExisist);
							if(!idExisist) {
								serviceInventoryDetailsList.add(siBean);
							}
						}
						
					}
					LOGGER.info("service id list size after filter "+serviceInventoryDetailsList.size());
					serviceInventoryDetailsList.stream().forEach(serviceDetail -> {
						QuoteIllSiteToService siteToService = new QuoteIllSiteToService();
						siteToService.setAllowAmendment("NA");
						siteToService
								.setErfServiceInventoryParentOrderId(serviceDetail.getReferenceOrderId());
						siteToService.setErfServiceInventoryServiceDetailId(serviceDetail.getId());
						siteToService.setErfServiceInventoryTpsServiceId(serviceDetail.getTpsServiceId());
						siteToService.setQuoteToLe(quoteTole);
						siteToService.setTpsSfdcParentOptyId(serviceDetail.getParentOpportunityId());
						if (Objects.nonNull(serviceInventoryDetailsList.get(0).getLinkType())&&!serviceInventoryDetailsList.get(0).getErfPrdCatalogOfferingName().equals(CommonConstants.MMR_CROSS_CONNECT)
								&& (MACDConstants.SINGLE.equalsIgnoreCase(serviceDetail.getLinkType()) || MACDConstants.LINK.equalsIgnoreCase(serviceDetail.getLinkType()))  ) {
							siteToService.setType(MACDConstants.LINK);
						}
						else{
							siteToService.setType(MACDConstants.REFERENCE_TYPE_PRIMARY);
						}
						quoteIllSiteToServiceRepository.save(siteToService);
						LOGGER.info("service id inside loop"+serviceDetail.getTpsServiceId());
					});
				}
				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteTole.getId());
				if (Objects.isNull(macdDetail)) {
					createMacdOrderDetail(quoteTole, request.getCancellationDate(), request.getCancellationReason());
				} else {
					macdDetail.setCreatedTime(new Date());
					macdDetailRepository.save(macdDetail);
				}
				macdResponse.setQuoteCategory(quoteTole.getQuoteCategory());
				macdResponse.setQuoteType(quoteTole.getQuoteType());
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());

				if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
					omsSfdcService.processCreatePartnerOpty(quoteTole, request.getQuoteRequest().getProductName(),partnerCuid,endCustomerName);
				} else if (request.getQuoteRequest().getQuoteId() == null && Objects.isNull(request.getQuoteRequest().getEngagementOptyId())) {
					// Triggering Sfdc Creation
					omsSfdcService.processCreateOpty(quoteTole, request.getQuoteRequest().getProductName());
				}

				processQuoteAccessPermissions(user, quoteTole);
			}
		}
		macdResponse.setQuoteResponse(response);
		return macdResponse;
	}
	
	/**
	 * processQuoteAccessPermissions - this method is used to save the quote permission 
	 * - Quote Created by Customer not visible to Sales
	 * - Quote Created by Sales not visible to Customer
	 * @param user
	 * @param quoteTole
	 */
	private void processQuoteAccessPermissions(User user, QuoteToLe quoteTole) {
		Integer prodFamilyId=null;
		List<QuoteToLeProductFamily> quoteToLeProductFamilys =quoteToLeProductFamilyRepository.findByQuoteToLe(quoteTole.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilys) {
			prodFamilyId=quoteToLeProductFamily.getMstProductFamily().getId();
		}
		QuoteAccessPermission quoteAccessPermission=null;
		List<QuoteAccessPermission> quoteAccessPermissions=quoteAccessPermissionRepository.findByProductFamilyIdAndTypeAndRefId(prodFamilyId, "QUOTE", quoteTole.getQuote().getQuoteCode());
		if(!quoteAccessPermissions.isEmpty()) {
			 quoteAccessPermission=quoteAccessPermissions.get(0);
		}else {
			quoteAccessPermission=new QuoteAccessPermission();
		}
		Quote quote=quoteTole.getQuote();
		quoteAccessPermission.setCreatedBy(Utils.getSource());
		quoteAccessPermission.setCreatedTime(new Date());
		quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
		quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
		quote.setIsCustomerView(CommonConstants.BACTIVE);
		quote.setIsSalesView(CommonConstants.BACTIVE);
		quoteAccessPermission.setProductFamilyId(prodFamilyId);
		quoteAccessPermission.setRefId(quoteTole.getQuote().getQuoteCode());
		quoteAccessPermission.setType("QUOTE");
		quoteAccessPermission.setUpdatedBy(Utils.getSource());
		quoteAccessPermission.setUpdatedTime(new Date());
		if (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
			quote.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quote.setIsSalesView(CommonConstants.BACTIVE);
		} else if (user.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BDEACTIVATE);
			quote.setIsCustomerView(CommonConstants.BACTIVE);
			quote.setIsSalesView(CommonConstants.BDEACTIVATE);
		}
		quoteRepository.save(quote);
		quoteAccessPermissionRepository.save(quoteAccessPermission);
	}

	/**
	 * @param quoteTole
	 * @param serviceDetailsBeanList
	 * @param erfCustomerLeIdInt
	 * @param quoteCategory
	 * @return
	 * @author Harini Sri Reka J Method to create MACD specific quoteToLe
	 */
	private QuoteToLe createMACDSpecificQuoteToLe(QuoteToLe quoteTole,
			List<SIServiceDetailsBean> serviceDetailsBeanList, Integer erfCustomerLeIdInt, String quoteCategory) {
		Integer erfSpLeId = null;
		Integer erfCustCurrencyId = null;
		Double maxContractTerm = null;
		String productOfferingType=null;
		Integer parentSfdcOptyId = null;
		if (serviceDetailsBeanList != null && !serviceDetailsBeanList.isEmpty()) {
			if (serviceDetailsBeanList.stream().findFirst().isPresent()) {

				erfSpLeId = serviceDetailsBeanList.stream().findFirst().get().getErfSpLeId();
				erfCustCurrencyId = serviceDetailsBeanList.stream().findFirst().get().getCustomerCurrencyId();
				parentSfdcOptyId = serviceDetailsBeanList.stream().findFirst().get().getParentOpportunityId();
			}

			Optional<SIServiceDetailsBean> siServiceDetail = serviceDetailsBeanList.stream()
					.min(Comparator.comparing(SIServiceDetailsBean::getContractTerm));
			if (siServiceDetail.isPresent()) {
                maxContractTerm = siServiceDetail.get().getContractTerm();
                productOfferingType=siServiceDetail.get().getErfPrdCatalogOfferingName();
            }

		}
		//LEAD-1111 Added for Contract term decimal value check 
		Integer contractTerm = null;
		if(maxContractTerm % 1 == 0) {
			contractTerm = maxContractTerm.intValue();
			quoteTole.setTermInMonths(contractTerm + " months");
		} else {
			quoteTole.setTermInMonths(maxContractTerm + " months");
		}
		LOGGER.info("quoteTole termInMonths {} ", quoteTole.getTermInMonths());
		LOGGER.info("sp le id {}, currencyId {}, maxContract term {} from service inventory: ", erfSpLeId,
				erfCustCurrencyId, maxContractTerm);

		quoteTole.setQuoteCategory(quoteCategory);
		quoteTole.setQuoteType(MACDConstants.MACD_QUOTE_TYPE);
		quoteTole.setSourceSystem(MACDConstants.SOURCE_SYSTEM);
		quoteTole.setErfCusCustomerLegalEntityId(erfCustomerLeIdInt);
		quoteTole.setErfCusSpLegalEntityId(erfSpLeId);
		if (Objects.nonNull(quoteTole.getQuoteCategory())
				&& !quoteTole.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE))
			quoteTole.setStage(QuoteStageConstants.MODIFY.getConstantCode());
		quoteTole.setCurrencyId(erfCustCurrencyId);
		if(productOfferingType!=null&&productOfferingType.equalsIgnoreCase("MMR Cross Connect")){
            quoteTole.setTermInMonths(Math.round(maxContractTerm) + " months");
        }
		quoteTole.setTpsSfdcParentOptyId(parentSfdcOptyId);
		return quoteTole;
	}

	/**
	 * Method to create Macd Order Detail
	 *
	 * @param quoteToLe
	 * @param cancellationDate
	 * @param cancellationReason
	 */
	public void createMacdOrderDetail(QuoteToLe quoteToLe, String cancellationDate, String cancellationReason)
			throws ParseException {
		MacdDetail macdDetail = new MacdDetail();
		if (Objects.nonNull(quoteToLe)) {
			List<String> serviceDetailsList = macdUtils.getAllServiceIdListBasedOnQuoteToLe(quoteToLe);
			serviceDetailsList.stream().forEach(serviceId -> {
				macdDetail.setQuoteToLeId(quoteToLe.getId());
				macdDetail.setTpsServiceId(serviceId);
				macdDetail.setCreatedBy(quoteToLe.getQuote().getCreatedBy().toString());
				macdDetail.setCreatedTime(new Timestamp(quoteToLe.getQuote().getCreatedTime().getTime()));
				macdDetail.setOrderCategory(quoteToLe.getQuoteCategory());
				macdDetail.setOrderType(quoteToLe.getQuoteType());
				macdDetail.setIsActive(quoteToLe.getQuote().getStatus());
				macdDetail.setStage(MACDConstants.MACD_ORDER_IN_PROGRESS);
				if (Objects.nonNull(quoteToLe.getTpsSfdcParentOptyId()))
					macdDetail.setTpsSfdcParentOptyId(quoteToLe.getTpsSfdcParentOptyId().toString());
				macdDetail.setUpdatedBy(quoteToLe.getQuote().getCreatedBy().toString());
				macdDetail.setUpdatedTime(new Timestamp(quoteToLe.getQuote().getCreatedTime().getTime()));
				macdDetailRepository.save(macdDetail);
			});
		}
	}

	@Override
	public NplQuoteBean updateLink(NplQuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId)
			throws TclCommonException {

		validateSiteInformation(quoteDetail, quoteId);
		Integer linkCount = quoteDetail.getLinkCount();
		NplQuoteBean quoteBean = null;
		LOGGER.info("link count{}", linkCount);
		while (linkCount > 0) {

			Integer siteA = null;
			Integer siteB = null;
			Integer siteId = null;
			Integer locA = null;
			Integer locB = null;
			ProductSolution productSolution = null;
			String siteAType = "";
			String siteBType = "";
			String productOfferingName = null;
			try {
				LOGGER.info("Customer Id received is {} ", erfCustomerId);

				User user = getUserId(Utils.getSource());
				Boolean siteChanged = false;
				String serviceId = null;
				final String[] siteCtId = {""};

				List<NplSite> sites = quoteDetail.getSite();
				MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());

				if (quoteToLe.isPresent() && sites.size() == 2 && user != null) {
					if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
						LOGGER.info("is multi circuit flag {} ", quoteDetail.getIsMulticircuit());
						if(Objects.nonNull(quoteDetail.getIsMulticircuit())) {
							LOGGER.info("Entered into multi circuit flag {} "+ quoteDetail.getIsMulticircuit()+"link id:"+quoteDetail.getLinkid());
							if(quoteDetail.getIsMulticircuit() == 1) {
							   deactivateOtherLinksMc(quoteToLe,quoteDetail.getLinkid());
							}
						}
						else {
							LOGGER.info("Entered into normal nde macd {} "+quoteDetail.getIsMulticircuit());
						    deactivateOtherLinks(quoteToLe);
						}
					}
					for (NplSite site : sites) {
						QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
								.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
						productOfferingName = site.getOfferingName();
						MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName,
								user);
						productSolution = productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(
								quoteToLeProductFamily, productOfferng);

						for (NplSiteDetail nplSiteDetail : site.getSite()) {
							if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
								if (MACDConstants.SHIFT_SITE_SERVICE
										.equalsIgnoreCase(quoteToLe.get().getQuoteCategory())) {
									if (nplSiteDetail.getSiteChangeflag() == true) {
										siteChanged = nplSiteDetail.getSiteChangeflag();
										serviceId = nplSiteDetail.getErfServiceInventoryTpsServiceId();
										siteCtId[0] = serviceId;
									}
								} else if (MACDConstants.CHANGE_BANDWIDTH_SERVICE
										.equalsIgnoreCase(quoteToLe.get().getQuoteCategory())) {
									siteChanged = nplSiteDetail.getSiteChangeflag();
									serviceId = nplSiteDetail.getErfServiceInventoryTpsServiceId();
									siteCtId[0] = serviceId;
								}
							}
						}
						siteId = processSiteDetail(user, productFamily, quoteToLeProductFamily, site,
								productOfferingName, productSolution, quoteToLe.get().getQuote(), siteChanged);
						if (siteA == null) {
							siteA = siteId;
							siteAType = site.getType().getType();
							locA = site.getSite().get(0).getLocationId();
						} else {
							siteB = siteId;
							siteBType = site.getType().getType();
							locB = site.getSite().get(0).getLocationId();

						}
						siteChanged = false;
					}
					quoteDetail.setQuoteId(quoteId);
					LOGGER.info("serviceId before constructNplLinks {}", serviceId+"siteid"+siteId+"siteCtId[0]:"+siteCtId[0]);
					if (siteA != null && siteB != null)
						if (quoteToLe.get().getIsMultiCircuit() == 0) {
							LOGGER.info("inside normal links creation");
							constructNplLinks(user, productFamily, quoteDetail.getQuoteId(), siteA, siteB, productSolution,
								siteAType, siteBType, productOfferingName, locA, locB, serviceId,quoteDetail.getIsBandwidthChanged(),quoteDetail.getIsSiteShift());
						}
					//added for nde mc multiple ehs id 
					if (quoteToLe.get().getIsMultiCircuit() == 1) {
						LOGGER.info("inside mc shift site link creation");
						constructNplLinksMacdMc(user, productFamily, quoteDetail.getQuoteId(), siteA, siteB,
								productSolution, siteAType, siteBType, productOfferingName, locA, locB, serviceId, null,
								null, quoteDetail.getSolutions().get(0).getComponents());
					}
				} else {
					throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
							ResponseResource.R_CODE_NOT_FOUND);
				}

				if (quoteToLe.isPresent()) {
					switch (quoteToLe.get().getQuoteCategory()) {
					case MACDConstants.SHIFT_SITE_SERVICE:
						if (quoteToLe.get().getStage().equals(QuoteStageConstants.MODIFY.getConstantCode())) {
							quoteToLe.get().setStage(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode());
							quoteToLeRepository.save(quoteToLe.get());
						}
						break;
					case MACDConstants.CHANGE_BANDWIDTH_SERVICE:
						if (quoteToLe.get().getStage().equals(QuoteStageConstants.MODIFY.getConstantCode())) {
							quoteToLe.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
							quoteToLeRepository.save(quoteToLe.get());
						}
						break;
					default:
						break;

					}
				}

				
			} catch (Exception e) {
				LOGGER.error(String.format("Message:  %s", e.getMessage()));
				LOGGER.error("Cause: ", e.getCause());
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			linkCount--;
		}
		
		//NDE SFDC CREATE PRODUCT CALL
				try {
					Optional<Quote> quote = quoteRepository.findById(quoteId);
					if (quote.get().getQuoteCode().startsWith("NDE")) {
						LOGGER.info("Before trigger sfdc product call for NDE macd");
						List<QuoteToLe> quotele = quoteToLeRepository.findByQuote(quote.get());
						List<ThirdPartyServiceJob> thirdPartyService = thirdPartyServiceJobsRepository
								.findByRefIdAndServiceTypeAndThirdPartySource(quote.get().getQuoteCode(), "CREATE_OPPORTUNITY",
										"SFDC");
						if (thirdPartyService.size() != 0) {
							List<ThirdPartyServiceJob> thirdPartyServiceProduct = thirdPartyServiceJobsRepository
									.findByRefIdAndServiceTypeAndThirdPartySource(quote.get().getQuoteCode(), "CREATE_PRODUCT",
											"SFDC");
							LOGGER.info("sfdc product call size"+thirdPartyServiceProduct.size());
							if (thirdPartyServiceProduct.size() == 0) {
								LOGGER.info("Before TRIGGER sfdc product create call");
								omsSfdcService.processProductServices(quotele.get(0), thirdPartyService.get(0).getTpsId());
							}

						}

					}
				}
				catch(Exception e) {
					LOGGER.error(String.format("Message:  %s", e.getMessage()));
					LOGGER.error("Cause NDE product call: ", e.getCause());
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					
				}
		quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), false);
		return quoteBean;

	}

	private void deactivateOtherLinks(Optional<QuoteToLe> quoteToLe) {
		quoteToLe.get().getQuoteToLeProductFamilies().stream().forEach(quoteProdFamily -> {
			quoteProdFamily.getProductSolutions().stream().forEach(quoteProdSolution -> {
				List<QuoteNplLink> links = nplLinkRepository.findByProductSolutionIdAndStatus(quoteProdSolution.getId(),
						CommonConstants.BACTIVE);
				if (links.size() != 0) {
					for (QuoteNplLink link : links) {
						try {
							procesDeActivateLink(link.getId(), "delete");
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}
					}
				}
			});
		});

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
	public NplQuoteBean getMacdQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProp)
			throws TclCommonException {
		NplQuoteBean response = null;
		try {
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString())) ? true : false;
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote, isFeasibleSites, isSiteProp);

			List<QuoteToLe> quoteToLeList = getQuoteToLeBasenOnVersion(quote);
			if (!quoteToLeList.isEmpty()) {
				QuoteToLe quoteToLe = quoteToLeList.get(0);
				if (quoteToLe != null) {
					response.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				}

				response.setQuoteType(quoteToLe.getQuoteType());
				response.setQuoteCategory(quoteToLe.getQuoteCategory());
				List<String> serviceIdsList = new ArrayList<>();
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
						.findByQuoteToLe_Id(quoteToLe.getId());

				if (Objects.nonNull(quoteIllSiteToServiceList) && !quoteIllSiteToServiceList.isEmpty()) {
					LOGGER.info("Service Id: " + quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
					response.setServiceId(quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
					response.setServiceOrderId(
							quoteIllSiteToServiceList.stream().findFirst().get().getErfServiceInventoryParentOrderId());
				}

				List<Boolean> flagList = new ArrayList<>();
				Boolean[] macdFlagValue = { false };
				if (Objects.nonNull(quoteIllSiteToServiceList) && !quoteIllSiteToServiceList.isEmpty()) {
					quoteIllSiteToServiceList.stream().forEach(siteToService -> {

						try {

							LOGGER.info("Service ID" + siteToService.getErfServiceInventoryTpsServiceId());

							Map<String, Object> macdFlag = macdUtils
									.getMacdInitiatedStatusForNPL(siteToService.getErfServiceInventoryTpsServiceId());
							LOGGER.info("macdFlag" + macdFlag);
							if (Objects.nonNull(macdFlag) && !macdFlag.isEmpty()) {
								if (macdFlag.size() == 1)
									macdFlagValue[0] = (Boolean) macdFlag
											.get(siteToService.getErfServiceInventoryTpsServiceId());
								else
									macdFlagValue[0] = setMacdFlag(macdFlag, macdFlagValue);
							}
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}
						flagList.add(macdFlagValue[0]);
					});

					//added for mc nde
					if (flagList.contains(true))
						response.setIsMacdInitiated(true);
					LOGGER.info("ismc flag"+quoteToLe.getIsMultiCircuit());
					if (Objects.nonNull(quoteToLe.getIsMultiCircuit())&&quoteToLe.getIsMultiCircuit() == 1) {
						LOGGER.info("INSIDE if"+quoteToLe.getIsMultiCircuit());
						response.setIsMultiCircuit(true);
					}
					if (Objects.nonNull(quoteToLe.getIsMultiCircuit())&&quoteToLe.getIsMultiCircuit() == 1) {
						List<String> multiCircuitChangeBandwidthFlag = new ArrayList<>();
						List<String> multiCircuitShiftSiteFlag = new ArrayList<>();
						List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
						
						//ADDED FOR nde mc
						quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
							if (quoteIllSiteToService.getBandwidthChanged() != null) {
								if (Objects.nonNull(quoteIllSiteToService)
										&& CommonConstants.BACTIVE.equals(quoteIllSiteToService.getBandwidthChanged()))
									multiCircuitChangeBandwidthFlag.add("true");
								else
									multiCircuitChangeBandwidthFlag.add("false");
							}
							else {
								multiCircuitChangeBandwidthFlag.add("false");
							}
						});
						
						if (multiCircuitChangeBandwidthFlag.size() != 0) {
							if (multiCircuitChangeBandwidthFlag.contains("false")) {
								response.setIsMulticircuitBandwidthChangeFlag(false);
							} else {
								response.setIsMulticircuitBandwidthChangeFlag(true);
							}
						}
						
						quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
							if (quoteIllSiteToService.getSiteShifted() != null) {
								if (Objects.nonNull(quoteIllSiteToService)
										&& CommonConstants.BACTIVE.equals(quoteIllSiteToService.getSiteShifted()))
									multiCircuitShiftSiteFlag.add("true");
								else
									multiCircuitShiftSiteFlag.add("false");
							}
							else {
								multiCircuitShiftSiteFlag.add("false");
							}
						});
						
						if (multiCircuitShiftSiteFlag.size() != 0) {
							if (multiCircuitShiftSiteFlag.contains("false")) {
								response.setIsMulticircuitShiftSiteFlag(false);
							} else {
								response.setIsMulticircuitShiftSiteFlag(true);
							}
						}
					}
				}
				LOGGER.info("bandwidth flag mc:"+response.getIsMulticircuitBandwidthChangeFlag());
				LOGGER.info("shiftsite flag mc:"+response.getIsMulticircuitShiftSiteFlag());
				
				List<QuoteNplLink> nplLinksList = nplLinkRepository.findByQuoteIdAndStatus(quoteToLe.getQuote().getId(),
						CommonConstants.BACTIVE);
				if (nplLinksList != null && !nplLinksList.isEmpty()) {
					Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(nplLinksList.get(0).getSiteAId());
					if (quoteIllSite.isPresent()
							&& quoteIllSite.get().getNplShiftSiteFlag().equals(CommonConstants.ACTIVE)) {
						response.setSiteShifted(MACDConstants.SITEA);

					} else {
						Optional<QuoteIllSite> quoteIllSiteSiteB = illSiteRepository
								.findById(nplLinksList.get(0).getSiteBId());
						if (quoteIllSiteSiteB.isPresent()
								&& quoteIllSiteSiteB.get().getNplShiftSiteFlag().equals(CommonConstants.ACTIVE)) {
							response.setSiteShifted(MACDConstants.SITEB);
						}
					}

					LOGGER.info("Site Shifted {}", response.getSiteShifted());

				}
				LOGGER.info("muticircuit is flag"+response.getIsMultiCircuit());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_GET_QUOTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * Method to setMacdFlag for quote
	 *
	 * @param macdFlag
	 * @param macdFlagValue
	 * @return
	 */

	public Boolean setMacdFlag(Map<String, Object> macdFlag, Boolean[] macdFlagValue) {
		macdFlag.entrySet().stream().forEach(entry -> {
			Boolean flag = (Boolean) entry.getValue();
			if (flag)
				macdFlagValue[0] = flag;

		});
		return macdFlagValue[0];
	}

	public MACDOrderSummaryResponseBean getOrderSummary(Integer quoteId, Integer quoteLeId, String serviceId)
			throws TclCommonException {

		MACDOrderSummaryResponseBean macdOrderResponse = new MACDOrderSummaryResponseBean();
		List<MACDOrderSummaryResponse> macdOrderSummaryResponseList = new ArrayList<>();
		List<String> solName = new ArrayList<>();

		// 1. Location Id
		// 2. Circuit Speed
		// 3. Local Loop Interface
		// 4. Interface
		// 5. Service Availability

		SIServiceInfoBean[] siDetailedInfoResponseIAS = null;
		List<SIServiceInfoBean> siServiceInfoResponse = null;
		String changeRequests = null;

		if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(serviceId))
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);

		List<String> serviceIdsList = macdUtils.getServiceIds(quoteToLeOpt.get());
		String serviceIds = serviceIdsList.stream().map(i -> i.trim()).distinct().collect(Collectors.joining(","));

		if (quoteToLeOpt.isPresent()) {

			// Queue call to get old attribute values from service details
			String iasQueueResponse = (String) mqUtils.sendAndReceive(siNplDetailsQueue, serviceId);

			if (StringUtils.isNotBlank(iasQueueResponse)) {
				siDetailedInfoResponseIAS = (SIServiceInfoBean[]) Utils.convertJsonToObject(iasQueueResponse,
						SIServiceInfoBean[].class);
				siServiceInfoResponse = Arrays.asList(siDetailedInfoResponseIAS);
				List<String> oldInterface = new ArrayList<>();

				siServiceInfoResponse.stream().forEach(serviceIdEntry-> {
					if(Objects.nonNull(serviceIdEntry.getSiteEndInterface())){
						oldInterface.add(serviceIdEntry.getSiteEndInterface());
					}
				});
				LOGGER.info("For quote ---> {} old interface is ----> {} ", quoteToLeOpt.get().getQuote().getQuoteCode(), oldInterface);

				// Logic to get new attribute values from oms
				siServiceInfoResponse.stream().forEach(serviceIdEntry -> {
					LOGGER.info("Service Id Entry is for the circuit ----> {} and the bean value is ----> {} ", serviceIdEntry.getTpsServiceId() , serviceIdEntry);
					MACDOrderSummaryResponse response = new MACDOrderSummaryResponse();
					Map<String, String> newAttributesMapPrimary = new HashedMap<>();
					List<MACDAttributesComparisonBean> primaryAttributesList = new ArrayList<>();
					MACDAttributesBean attributesBean = new MACDAttributesBean();
					Map<String, String> oldAttributesMapPrimary = new HashedMap<>();

					getLocationDetails(oldAttributesMapPrimary, serviceIdEntry);
					getCircuitSpeed(oldAttributesMapPrimary, serviceIdEntry);
					getInterfaceDetails(oldAttributesMapPrimary, serviceIdEntry);
					getLocalLoopBandwidthDetails(oldAttributesMapPrimary, serviceIdEntry);
					getServiceAvailabilityDetails(oldAttributesMapPrimary, serviceIdEntry);
					response.setSiteType(serviceIdEntry.getSiteType());
					LOGGER.info("Site type for quote ---> {} is ----> {} and interface is ----> {} ", quoteToLeOpt.get().getQuote().getQuoteCode(), response.getSiteType(), oldInterface);
					if (quoteToLeOpt.isPresent()) {

						try {

							List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository
									.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceIdEntry.getTpsServiceId(),
											quoteToLeOpt.get());
							Optional<QuoteIllSiteToService> quoteIllSiteToServiceOpt = quoteIllSiteToService.stream()
									.findFirst();
							if (quoteIllSiteToServiceOpt.isPresent()) {
								List<QuoteProductComponent> quoteProductComponentList=new ArrayList<>();
								if(quoteIllSiteToServiceOpt.get().getQuoteNplLink()!=null) {
									quoteProductComponentList = quoteProductComponentRepository
											.findByReferenceIdAndReferenceName(
													quoteIllSiteToServiceOpt.get().getQuoteNplLink().getId(),
													QuoteConstants.NPL_LINK.toString());
									LOGGER.info("site type looped {}", response.getSiteType());
									if (response.getSiteType().equalsIgnoreCase(MACDConstants.SI_SITEA)) {
										Optional<QuoteIllSite> illSiteAOpt = illSiteRepository
												.findById(quoteIllSiteToServiceOpt.get().getQuoteNplLink().getSiteAId());
										getLatLongDetails(newAttributesMapPrimary,
												illSiteAOpt.get().getErfLocSitebLocationId());
									} else {
										Optional<QuoteIllSite> illSiteBOpt = illSiteRepository
												.findById(quoteIllSiteToServiceOpt.get().getQuoteNplLink().getSiteBId());
										getLatLongDetails(newAttributesMapPrimary,
												illSiteBOpt.get().getErfLocSitebLocationId());
									}

									if(Objects.nonNull(quoteIllSiteToServiceOpt.get().getQuoteNplLink()) && Objects.isNull(quoteIllSiteToServiceOpt.get().getQuoteIllSite())){
										LOGGER.info("Inside NPL NDE MACD Block for service id ---> {} ", quoteIllSiteToServiceOpt.get().getErfServiceInventoryTpsServiceId());
										solName.add("");
									}
								}




								else if(Objects.nonNull(quoteIllSiteToServiceOpt.get().getQuoteIllSite().getId())){
									LOGGER.info("Quote ill site id is not null and its value is ---> {} ", quoteIllSiteToServiceOpt.get().getQuoteIllSite().getId() );

									quoteProductComponentList=quoteProductComponentRepository
											.findByReferenceIdAndReferenceName(
													quoteIllSiteToServiceOpt.get().getQuoteIllSite().getId(),
													QuoteConstants.ILLSITES.toString());

									Integer solId = quoteIllSiteToServiceOpt.get().getQuoteIllSite().getProductSolution().getId();
									Optional<ProductSolution> prodSol = productSolutionRepository.findById(solId);
									if(Objects.nonNull(prodSol)){
										LOGGER.info("Product solution id is ---> {} ",solId);
										if("MMR Cross Connect".equalsIgnoreCase(prodSol.get().getMstProductOffering().getProductName())
												&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLeOpt.get().getQuoteType())){
											LOGGER.info("Inside MMR Block");
											solName.add("MMR");
											LOGGER.info("Solution is ---> {} ", solName.get(0));
											String siteType = serviceIdEntry.getSiteType();
											Integer locationId;
											if(siteType.equalsIgnoreCase("siteA")) {
												locationId= quoteIllSiteToServiceOpt.get().getQuoteIllSite().getErfLocSitebLocationId();
											}
											else
											{
												locationId= quoteIllSiteToServiceOpt.get().getQuoteIllSite().getErfLocSiteaLocationId();
											}
											getLatLongDetails(newAttributesMapPrimary, locationId);
										}

									}

								}
								else
								{

									LOGGER.info("Site ID is ---> {} ", Optional.ofNullable(quoteIllSiteToServiceOpt.get().getQuoteIllSite().getId()));
									quoteProductComponentList=quoteProductComponentRepository
											.findByReferenceIdAndReferenceName(
													quoteIllSiteToServiceOpt.get().getQuoteIllSite().getId(),
													QuoteConstants.ILLSITES.toString());
									Optional<QuoteIllSite> illSiteAOpt = illSiteRepository
											.findById(quoteIllSiteToServiceOpt.get().getQuoteIllSite().getId());
									getLatLongDetails(newAttributesMapPrimary,
											illSiteAOpt.get().getErfLocSitebLocationId());
								}


								getPrimaryInternetPortDetails(newAttributesMapPrimary, quoteProductComponentList,
										response.getSiteType(), oldAttributesMapPrimary, quoteToLeOpt, oldInterface.get(0));

								getPrimaryLastMileDetails(newAttributesMapPrimary, quoteProductComponentList,
										response.getSiteType());

								getParallelBuildAndParallelRunDays(quoteProductComponentList, response);




								quoteProductComponentList.forEach(comp->{
								    comp.getQuoteProductComponentsAttributeValues().forEach(attr->{
                                        if("CROSS_CONNECT_LOCAL_DEMARCATION_ID".equalsIgnoreCase(attr.getProductAttributeMaster().getName())){
                                            LOGGER.info("inside demarc flag set for quote ---> {} ", quoteToLeOpt.get().getQuote().getQuoteCode());
                                            macdOrderResponse.setDemarcDone("yes");
                                        }
                                    });
                                });


							}
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}

					}
					if (!newAttributesMapPrimary.equals(oldAttributesMapPrimary)) {
						if (quoteToLeOpt.isPresent() && Objects.nonNull(quoteToLeOpt.get().getQuoteCategory()) && (MACDConstants.SHIFT_SITE_SERVICE
								.equals(quoteToLeOpt.get().getQuoteCategory()) && !solName.isEmpty() && "MMR".equals(solName.get(0)))) {

							LOGGER.info("change order summary , change request is ----> {} ", Optional.ofNullable(response.getChangeRequests()));
							if (response.getChangeRequests() == null || MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLeOpt.get().getQuoteCategory()))
								response.setChangeRequests(MACDConstants.SHIFT_SITE);
							LOGGER.info("Shift site block fr change req summary ---> {} ", response.getChangeRequests());

						}

						List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository
								.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceIdEntry.getTpsServiceId(),
										quoteToLeOpt.get());
						Optional<QuoteIllSiteToService> quoteIllSiteToServiceOpt = quoteIllSiteToService.stream()
								.findFirst();
						if (!solName.isEmpty() &&"MMR".equalsIgnoreCase(solName.get(0)))
						{
							if (quoteIllSiteToServiceOpt.isPresent() && Objects.nonNull(quoteIllSiteToServiceOpt.get())) {
								List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceId(quoteIllSiteToServiceOpt.get().getQuoteIllSite().getId());
								components.forEach(comp -> {
									comp.getQuoteProductComponentsAttributeValues().forEach(attr -> {
										if ("Cross Connect Type".equalsIgnoreCase(attr.getProductAttributeMaster().getName()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLeOpt.get().getQuoteType())) {
											if ("Passive".equalsIgnoreCase(attr.getAttributeValues())) {
												solName.add("Passive");
											} else solName.add("Active");
										}
									});
								});

							}
					}

						if(!solName.isEmpty()){
							LOGGER.info("Solution name size and data are ---> {} ----> {} ", solName.size(), solName);
						}

						if (!solName.isEmpty() && (solName.size() >= 2 && !"Passive".equalsIgnoreCase(solName.get(1)) || (!solName.isEmpty() && solName.get(0).equalsIgnoreCase(""))))
						{
							newAttributesMapPrimary.entrySet().stream()
									.filter(att -> !att.getKey().equalsIgnoreCase(MACDConstants.LOCATION_ID)
											&& !((att.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
											|| att.getKey().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))
											&& Objects.nonNull(att.getValue()) && compareNewAndOldValues(
											oldAttributesMapPrimary.get(att.getKey()), att.getValue())))
									.forEach(attribute -> {
										/*if ( "MMR".equalsIgnoreCase(solName.get(0))){
											if(MACDConstants.LAT_LONG.equalsIgnoreCase(attribute.getKey())){
												oldAttributesMapPrimary.remove(MACDConstants.LAT_LONG);
												newAttributesMapPrimary.remove(MACDConstants.LAT_LONG);
												LOGGER.info("removed lat long for mmr quote id --> {} ", quoteId);
											}
										}*/
										LOGGER.info("Old and new attributes are ----> name : {} ------ value: {} ///////// new name -----> : {} ------- value -----> : {} ",
												attribute.getKey(), oldAttributesMapPrimary.get(attribute.getKey()), attribute.getKey(), newAttributesMapPrimary.get(attribute.getKey()));
										if (Objects.nonNull(attribute.getValue()) && !attribute.getValue()
												.equalsIgnoreCase(oldAttributesMapPrimary.get(attribute.getKey()))) {
											LOGGER.info("Attribute name {}, attribute value {}, old attribute value {}",
													attribute.getKey(), attribute.getValue(),
													oldAttributesMapPrimary.get(attribute.getKey()));
											MACDAttributesComparisonBean bean = new MACDAttributesComparisonBean();
											bean.setAttributeName(attribute.getKey());
											if ((attribute.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
													|| attribute.getKey()
													.equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))) {
												bean.setNewAttributes(convertNewBandwidthValue(attribute.getValue()));
											} else
												bean.setNewAttributes(attribute.getValue());
											bean.setOldAttributes(oldAttributesMapPrimary.get(attribute.getKey()));



											if (Objects.nonNull(attribute.getKey())
													&& attribute.getKey().equalsIgnoreCase(MACDConstants.LAT_LONG)) {
												if (!"MMR".equals(solName.get(0))) {
													LOGGER.info(
															"Location id in quote - {} is different from service inventory - {} for category {}",
															newAttributesMapPrimary.get(MACDConstants.LOCATION_ID),
															oldAttributesMapPrimary.get(MACDConstants.LOCATION_ID),
															quoteToLeOpt.get().getQuoteCategory());
													LOGGER.info("Quote category ---> //// ----> {} ", quoteToLeOpt.get().getQuoteCategory());
													if (MACDConstants.SHIFT_SITE_SERVICE
															.equals(quoteToLeOpt.get().getQuoteCategory())) {
														LOGGER.info("Shift site block Quote Category for quote ---> {} is ---> {} ", quoteToLeOpt.get().getQuote().getQuoteCode(), quoteToLeOpt.get().getQuoteCategory());
														bean.setAttributeName(MACDConstants.LOCATION_ID);
														bean.setNewAttributes(
																newAttributesMapPrimary.get(MACDConstants.LOCATION_ID));
														bean.setOldAttributes(
																oldAttributesMapPrimary.get(MACDConstants.LOCATION_ID));
														LOGGER.info("change order summary , change request is ----> {} ", Optional.ofNullable(response.getChangeRequests()));
														response.setChangeRequests(response.getChangeRequests() == null
																? MACDConstants.SHIFT_SITE
																: response.getChangeRequests() + " + "
																+ MACDConstants.SHIFT_SITE);
														LOGGER.info("Shift site block fr change req summary ---> {} ", response.getChangeRequests());
													} else {
														throw new TclCommonRuntimeException(
																ExceptionConstants.LOCATION_ID_MISMATCH,
																ResponseResource.R_CODE_ERROR);
													}
												}
											} else if (Objects.nonNull(attribute.getKey())
													&& attribute.getKey()
													.equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
													|| attribute.getKey()
													.equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString())) {
												LOGGER.info("Quote Category for quote ---> {} is ---> {} ", quoteToLeOpt.get().getQuote().getQuoteCode(), quoteToLeOpt.get().getQuoteCategory());
												LOGGER.info("Inside Change Bandwidth loop : {} ", changeRequests);
												response.setChangeRequests(response.getChangeRequests() == null
														? MACDConstants.CHANGE_BANDWIDTH
														: response.getChangeRequests()
														.contains(MACDConstants.CHANGE_BANDWIDTH)
														? response.getChangeRequests()
														: response.getChangeRequests() + " + "
														+ MACDConstants.CHANGE_BANDWIDTH);
											}

											primaryAttributesList.add(bean);
										}

									});
					}
					}

					attributesBean.setPrimaryAttributesList(primaryAttributesList);
					response.setQuotesAttributes(attributesBean);
					response.setServiceId(serviceIdEntry.getTpsServiceId());
					if (quoteToLeOpt.isPresent()) {
						quoteToLeOpt.get().setChangeRequestSummary(response.getChangeRequests());
						LOGGER.info("Saving change request summmary for quote to le ----> {} ", quoteToLeOpt.get().getChangeRequestSummary());
						quoteToLeRepository.save(quoteToLeOpt.get());
					}

					try {
						response.setPricingsList(quoteCompare(quoteId, serviceIdEntry.getSiOrderId(),
								serviceIdEntry.getTpsServiceId(), serviceIdEntry));
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);

					}

					macdOrderSummaryResponseList.add(response);

				});
			}
		}

		if (quoteToLeOpt.isPresent()) {
			if (quoteToLeOpt.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
				quoteToLeOpt.get().setStage(QuoteStageConstants.CHANGE_ORDER.getConstantCode());
				quoteToLeRepository.save(quoteToLeOpt.get());
			}
		}

		Optional<String> longest = macdOrderSummaryResponseList.stream()
				.map(MACDOrderSummaryResponse::getChangeRequests).filter(Objects::nonNull)
				.sorted((e1, e2) -> e1.length() > e2.length() ? -1 : 1).findFirst();
		LOGGER.info("Change request string {}", longest);
		if (longest.isPresent())
			macdOrderResponse.setChangeRequests(longest.get());
		macdOrderResponse.setMacdOrderSummaryResponseList(macdOrderSummaryResponseList);

        LOGGER.info("Demarc done value is ----> {} ", macdOrderResponse.getDemarcDone() );
        return macdOrderResponse;

	}

	private void getParallelBuildAndParallelRunDays(List<QuoteProductComponent> quoteProductComponentList,
			MACDOrderSummaryResponse response) {
		quoteProductComponentList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(MACDConstants.NPL_COMMON.toString())
						&& quoteProdComponent.getType().equals(FPConstants.LINK.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(MACDConstants.PARALLEL_BUILD.toString()))
								response.setParallelBuild(attribute.getAttributeValues());
							if (attribute.getProductAttributeMaster().getName()
									.equals(MACDConstants.PARALLEL_RUN_DAYS.toString()))
								response.setParallelRunDays(attribute.getAttributeValues());
						}));

	}

	private void getLocalLoopBandwidthDetails(Map<String, String> oldAttributesMapPrimary,
			SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.CROSSCONNECT)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.LINK)) {
			LOGGER.info("Inside getLocalLoopBwDetails for s id ---> {} ", detailedInfo.getTpsServiceId());
			oldAttributesMapPrimary.put(FPConstants.LOCAL_LOOP_BW.toString(), (detailedInfo.getLastMileBandwidth()
					+ CommonConstants.SPACE + detailedInfo.getLastMileBandwidthUnit()));
		}
	}

	private void getInterfaceDetails(Map<String, String> oldAttributesMapPrimary, SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.CROSSCONNECT)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.LINK)) {
			LOGGER.info("Inside getInterfaceDetails for s id ---> {} ", detailedInfo.getTpsServiceId());
			oldAttributesMapPrimary.put(FPConstants.INTERFACE.toString(), detailedInfo.getSiteEndInterface());
		}
	}

	private void getServiceAvailabilityDetails(Map<String, String> oldAttributesMapPrimary,
			SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.CROSSCONNECT)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.LINK)) {
			LOGGER.info("Inside getServiceAvailabilityDetails for s id ---> {} ", detailedInfo.getTpsServiceId());
			oldAttributesMapPrimary.put(FPConstants.SERVICE_AVAILABILITY.toString(), detailedInfo.getCommittedSla());
		}
	}

	private void getCircuitSpeed(Map<String, String> oldAttributesMapPrimary, SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.CROSSCONNECT)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.LINK)) {
			LOGGER.info("Inside getCircuitSpeed for s id ---> {} ", detailedInfo.getTpsServiceId());
			oldAttributesMapPrimary.put(FPConstants.PORT_BANDWIDTH.toString(),
					(detailedInfo.getBandwidthPortSpeed() + CommonConstants.SPACE + detailedInfo.getBandwidthUnit()));
		}
	}

	private void getLocationDetails(Map<String, String> oldAttributesMapPrimary, SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.CROSSCONNECT)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.LINK)) {
			LOGGER.info("Inside getLocationDetails for s id ---> {} ", detailedInfo.getTpsServiceId());
			oldAttributesMapPrimary.put(MACDConstants.LAT_LONG, detailedInfo.getLocationId().toString());
			oldAttributesMapPrimary.put(MACDConstants.LOCATION_ID, detailedInfo.getSiteAddress());
		}
	}

	private void getPrimaryLastMileDetails(Map<String, String> newAttributesMapPrimary,
			List<QuoteProductComponent> componentsList, String siteType) {
		if (MACDConstants.SI_SITEA.equalsIgnoreCase(siteType)) {
			componentsList.stream()
					.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
							.equals(FPConstants.LAST_MILE.toString())
							&& quoteProdComponent.getType().equals(FPConstants.SITEA.toString()))
					.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
							.forEach(attribute -> {
								if (attribute.getProductAttributeMaster().getName()
										.equals(FPConstants.LOCAL_LOOP_BW.toString()))
									newAttributesMapPrimary.put(FPConstants.LOCAL_LOOP_BW.toString(),
											attribute.getAttributeValues());
							}));
		} else {
			componentsList.stream()
					.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
							.equals(FPConstants.LAST_MILE.toString())
							&& quoteProdComponent.getType().equals(FPConstants.SITEB.toString()))
					.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
							.forEach(attribute -> {
								if (attribute.getProductAttributeMaster().getName()
										.equals(FPConstants.LOCAL_LOOP_BW.toString()))
									newAttributesMapPrimary.put(FPConstants.LOCAL_LOOP_BW.toString(),
											attribute.getAttributeValues());
							}));
		}

	}

	private void getPrimaryInternetPortDetails(Map<String, String> newAttributesMapPrimary,
			List<QuoteProductComponent> componentsList, String siteType, Map<String, String> oldAttributesMapPrimary, Optional<QuoteToLe> quoteToLe, String oldInterface) {
		LOGGER.info("Inside getPrimaryInternetPortDetails");
		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equalsIgnoreCase(ComponentConstants.NATIONAL_CONNECTIVITY.getComponentsValue())
						&& quoteProdComponent.getType().equalsIgnoreCase(FPConstants.LINK.toString()))
				.findFirst().ifPresent(quoteProd ->

						quoteProd.getQuoteProductComponentsAttributeValues().stream().forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName().equals(FPConstants.PORT_BANDWIDTH.toString())){
							newAttributesMapPrimary.put(FPConstants.PORT_BANDWIDTH.toString(),
										attribute.getAttributeValues());
							}

							else if (MACDConstants.SI_SITEA.equalsIgnoreCase(siteType) && MACDConstants.INTERFACE_TYPE_A_END
									.equals(attribute.getProductAttributeMaster().getName())) {
								newAttributesMapPrimary.put(FPConstants.INTERFACE.toString(), attribute.getAttributeValues());
							} else if (MACDConstants.SI_SITEB.equalsIgnoreCase(siteType) && MACDConstants.INTERFACE_TYPE_B_END
									.equals(attribute.getProductAttributeMaster().getName())) {
								newAttributesMapPrimary.put(FPConstants.INTERFACE.toString(), attribute.getAttributeValues());
							} else if (FPConstants.SERVICE_AVAILABILITY.toString()
									.equalsIgnoreCase(attribute.getProductAttributeMaster().getName())) {
								newAttributesMapPrimary.put(FPConstants.SERVICE_AVAILABILITY.toString(),
										attribute.getAttributeValues());
							}
						}));

		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equalsIgnoreCase("Cross Connect")
						&& quoteProdComponent.getType().equalsIgnoreCase(FPConstants.PRIMARY.toString()))
				.findFirst().ifPresent(quoteProd ->

				quoteProd.getQuoteProductComponentsAttributeValues().stream().forEach(attribute -> {
					if (attribute.getProductAttributeMaster().getName().equals(FPConstants.BANDWIDTH.toString())){
						LOGGER.info(" Inside Bandwidth scenario for site type ---> {}  and id ---> {}  ", siteType,
								quoteProductComponentRepository.findById(quoteProd.getId()).get().getReferenceId());
						newAttributesMapPrimary.put(FPConstants.PORT_BANDWIDTH.toString(),
								attribute.getAttributeValues()+ CommonConstants.SPACE + MACDConstants.MBPS);
					}

					else if ( siteType.equalsIgnoreCase("SiteA") && MACDConstants.INTERFACE.equals(attribute.getProductAttributeMaster().getName())) {
						LOGGER.info("Site A");
						newAttributesMapPrimary.put(FPConstants.INTERFACE.toString(), attribute.getAttributeValues());
					}
					else if(siteType.equalsIgnoreCase("SiteB") && MACDConstants.INTERFACEB.equals(attribute.getProductAttributeMaster().getName())) {
						LOGGER.info("Site B block");
						newAttributesMapPrimary.put(MACDConstants.INTERFACEB.toString(), attribute.getAttributeValues());
						if(quoteToLe.get().getIsMultiCircuit()==0 && siteType.equalsIgnoreCase("SiteB")){
							LOGGER.info("Inside interface block for site B");
							//LOGGER.info("Old interface is ---> {} ", oldAttributesMapPrimary.get(FPConstants.INTERFACE).toString());
							oldAttributesMapPrimary.put(MACDConstants.INTERFACEB.toString(), oldInterface);
							LOGGER.info("Old site B interface is ----> {} ", oldAttributesMapPrimary.get(MACDConstants.INTERFACEB));
						}
					}

					else if (FPConstants.SERVICE_AVAILABILITY.toString()
							.equalsIgnoreCase(attribute.getProductAttributeMaster().getName())) {
						newAttributesMapPrimary.put(FPConstants.SERVICE_AVAILABILITY.toString(),
								attribute.getAttributeValues());
					}
				}));


	}

	private void getLatLongDetails(Map<String, String> newAttributesMapPrimary, Integer siteId)
			throws TclCommonException, IllegalArgumentException {
		String locationResponse = (String) mqUtils.sendAndReceive(locationDetailsQueue, siteId.toString());
		LocationDetail[] locationDetailResponse = (LocationDetail[]) Utils.convertJsonToObject(locationResponse,
				LocationDetail[].class);
		if (locationDetailResponse.length > 0) {
			newAttributesMapPrimary.put(MACDConstants.LAT_LONG, locationDetailResponse[0].getLocationId().toString());
			String address = constructUserAddress(locationDetailResponse);
			newAttributesMapPrimary.put(MACDConstants.LOCATION_ID, address);
		}
	}

	private String constructUserAddress(LocationDetail[] locationDetailResponse) {
		String address = StringUtils.EMPTY;
		if (locationDetailResponse[0].getUserAddress().getAddressLineOne() != null)
			address += locationDetailResponse[0].getUserAddress().getAddressLineOne();
		// if (locationDetailResponse[0].getUserAddress().getAddressLineTwo() != null)
		// address += CommonConstants.SPACE +
		// locationDetailResponse[0].getUserAddress().getAddressLineTwo();
		if (locationDetailResponse[0].getUserAddress().getLocality() != null)
			address += CommonConstants.SPACE + locationDetailResponse[0].getUserAddress().getLocality();
		if (locationDetailResponse[0].getUserAddress().getCity() != null)
			address += CommonConstants.SPACE + locationDetailResponse[0].getUserAddress().getCity();
		if (locationDetailResponse[0].getUserAddress().getState() != null)
			address += CommonConstants.SPACE + locationDetailResponse[0].getUserAddress().getState();
		if (locationDetailResponse[0].getUserAddress().getPincode() != null)
			address += CommonConstants.SPACE + locationDetailResponse[0].getUserAddress().getPincode();
		return address;
	}

	private Boolean compareNewAndOldValues(String oldBandwidth, String newBandwidth) {
		if (oldBandwidth.toLowerCase().contains(MACDConstants.KBPS_lOWER_CASE)) {
			String newBandwidthInKbps = convertNewValuetoKbps(newBandwidth);
			String oldbandwidthValInKbps = oldBandwidth.substring(0, (oldBandwidth.indexOf(MACDConstants.KBPS)) - 1);
			LOGGER.info("newBandwidthInKbps-" + newBandwidthInKbps + "oldbandwidthInKbps-" + oldbandwidthValInKbps);
			return (oldbandwidthValInKbps.equals(newBandwidthInKbps));

		} else if (oldBandwidth.toLowerCase().contains(MACDConstants.GBPS_lOWER_CASE)) {
			String newBandwidthInGbps = convertNewValuetoGbps(newBandwidth);
			String oldbandwidthValInGbps = oldBandwidth.substring(0, (oldBandwidth.indexOf(MACDConstants.GBPS)) - 1);
			LOGGER.info("newBandwidthInGbps-" + newBandwidthInGbps + "oldbandwidthInGbps-" + oldbandwidthValInGbps);
			return (oldbandwidthValInGbps.equals(newBandwidthInGbps));
		} else if (oldBandwidth.toLowerCase().contains(MACDConstants.MBPS_LOWER_CASE)) {
			return oldBandwidth.equals(newBandwidth);
		}

		return null;
	}

	private String convertNewBandwidthValue(String value) {
		LOGGER.info("Srtring length is ----> {} && value is ---> {} ", value.indexOf(MACDConstants.MBPS) - 1 , value);
		Double newbandwidthVal = Double.valueOf(value.substring(0, (value.indexOf(MACDConstants.MBPS)) - 1));
		if (newbandwidthVal < 1)
			return String.valueOf((int) (newbandwidthVal * 1024) + CommonConstants.SPACE + MACDConstants.KBPS);
		else if (newbandwidthVal >= 1 && newbandwidthVal <= 999)
			return value;
		else if (newbandwidthVal > 999)
			return (String.valueOf((int) (newbandwidthVal / 1000)) + CommonConstants.SPACE + MACDConstants.GBPS);
		return null;
	}

	/**
	 * Method for compare quotes API
	 *
	 * @param quoteId
	 * @param orderid
	 * @param servicedetailid
	 * @return
	 * @throws TclCommonException
	 */

	public CompareQuotes quoteCompare(Integer quoteId, Integer orderid, String tpsId, SIServiceInfoBean serviceInfoBean)
			throws TclCommonException {
		Objects.requireNonNull(quoteId, ExceptionConstants.QUOTE_ID_ERROR);
		Objects.requireNonNull(orderid, ExceptionConstants.SERVICE_ID_ERROR);
		Objects.requireNonNull(tpsId, ExceptionConstants.TPS_ID_ERROR);

		double finalArc = 0D;
		double finalNrc = 0D;
		CompareQuotes compareQuotes = new CompareQuotes();
		Map<String, CompareQuotePrices> arcPrice = new HashMap<>();
		Map<String, CompareQuotePrices> nrcPrice = new HashMap<>();

		Optional<Quote> quote = quoteRepository.findById(quoteId);
		newQuotesComponentRepo(arcPrice, nrcPrice, quote);

		if (quote.get().getQuoteToLes().stream().findFirst().isPresent()
				&& Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getFinalArc()))
			finalArc = quote.get().getQuoteToLes().stream().findFirst().get().getFinalArc();
		if (quote.get().getQuoteToLes().stream().findFirst().isPresent()
				&& Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getFinalNrc()))
			finalNrc = quote.get().getQuoteToLes().stream().findFirst().get().getFinalNrc();

		List<CompareQuotePrices> quotePricesList = new ArrayList<>();
		getQuotePrices(arcPrice, quote, quotePricesList,
				Objects.isNull(serviceInfoBean.getArc()) ? 0d : serviceInfoBean.getArc(), MACDConstants.ARC, finalArc);
		getQuotePrices(nrcPrice, quote, quotePricesList,
				Objects.isNull(serviceInfoBean.getNrc()) ? 0d : serviceInfoBean.getNrc(), MACDConstants.NRC, finalNrc);
		compareQuotes.setSolutionQuote(getTotalSolutionQuote(quote, serviceInfoBean));
		compareQuotes.setPrices(quotePricesList);
		return compareQuotes;

	}

	/**
	 * Method to fetch new prices from the repository
	 *
	 * @param arcPrice
	 * @param nrcPrice
	 * @param quote
	 */

	private void newQuotesComponentRepo(Map<String, CompareQuotePrices> arcPrice,
			Map<String, CompareQuotePrices> nrcPrice, Optional<Quote> quote) {
		quote.get().getQuoteToLes().stream().forEach(quoteToLe -> {
			quoteToLe.getQuoteToLeProductFamilies().stream().forEach(productFamily -> {
				productFamily.getProductSolutions().stream().forEach(productSolution -> {
					productSolution.getQuoteIllSites().stream()
							.filter(illsite -> illsite.getStatus().equals(CommonConstants.BACTIVE))
							.forEach(illSites -> {
								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductFamily(illSites.getId(),
												productFamily.getMstProductFamily());
								quoteProductComponents.stream().forEach(quoteProductComponent -> {
									QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
											String.valueOf(quoteProductComponent.getId()), MACDConstants.COMPONENTS);
									getNewQuotesARCNRC(arcPrice, nrcPrice, quoteProductComponent, quotePrice);
								});
							});
				});
			});
		});

		List<QuoteNplLink> quoteNplLinkList = nplLinkRepository.findByQuoteIdAndStatus(quote.get().getId(),
				CommonConstants.BACTIVE);
		if (quoteNplLinkList != null && !quoteNplLinkList.isEmpty()) {
			quoteNplLinkList.stream().forEach(link -> {
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndReferenceName(link.getId(), QuoteConstants.NPL_LINK.toString());
				quoteProductComponents.stream().forEach(quoteProductComponent -> {
					QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
							String.valueOf(quoteProductComponent.getId()), MACDConstants.COMPONENTS);
					getNewQuotesARCNRC(arcPrice, nrcPrice, quoteProductComponent, quotePrice);

				});
			});

		}
	}

	/**
	 * Method to get Total Solution Quotes in compare quotes API
	 *
	 * @param quotePricesList
	 */
	private TotalSolutionQuote getTotalSolutionQuote(Optional<Quote> quote, SIServiceInfoBean sIServiceDetailDataBean) {
		TotalSolutionQuote quote1 = new TotalSolutionQuote();
		double oldArc = Objects.isNull(sIServiceDetailDataBean.getArc()) ? 0d : sIServiceDetailDataBean.getArc();
		double oldNrc = Objects.isNull(sIServiceDetailDataBean.getNrc()) ? 0d : sIServiceDetailDataBean.getNrc();
		double newTcv = 0D;
		String termInMonths = null;
		if (quote.get().getQuoteToLes().stream().findFirst().isPresent()
				&& Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getTotalTcv()))
			newTcv = quote.get().getQuoteToLes().stream().findFirst().get().getTotalTcv();
		if (quote.get().getQuoteToLes().stream().findFirst().isPresent()
				&& Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getTermInMonths()))
			termInMonths = quote.get().getQuoteToLes().stream().findFirst().get().getTermInMonths();
		String intValue = termInMonths.replaceAll("[^0-9]", "");
		double oldTcv = (Integer.valueOf(intValue) / 12) * oldArc + oldNrc;
		quote1.setName(MACDConstants.SOLUTION_QUOTE);
		quote1.setOldQuote(oldTcv);
		quote1.setNewQuote(newTcv);
		quote1.setDelta(newTcv - oldTcv);
		quote1.setCurrencyType(MACDConstants.CURRENCY_TYPE);

		return quote1;
	}

	/**
	 * This method fetches and sets the component values/prices
	 *
	 * @param nrcPrice
	 * @param quote
	 * @param compareQuotes
	 * @param quotePricesList
	 */

	private void getQuotePrices(Map<String, CompareQuotePrices> priceMap, Optional<Quote> quote,
			List<CompareQuotePrices> quotePricesList, double oldValue, String name, double finalPrice) {
		CompareQuotePrices compareQuotePrices = new CompareQuotePrices();
		compareQuotePrices.setName(name);
		compareQuotePrices.setOldQuotePrice(oldValue);
		compareQuotePrices.setNewQuotePrice(finalPrice);
		compareQuotePrices.setDelta(compareQuotePrices.getNewQuotePrice() - compareQuotePrices.getOldQuotePrice());
		List<ComponentQuotePrices> ComponentList = new ArrayList<>();

		setComponentValues(priceMap, ComponentList);
		compareQuotePrices.setComponents(ComponentList);
		quotePricesList.add(compareQuotePrices);
	}

	private void setComponentValues(Map<String, CompareQuotePrices> priceMap,
			List<ComponentQuotePrices> ComponentList) {
		priceMap.entrySet().forEach(value -> {
			ComponentQuotePrices componentQuotePrices = new ComponentQuotePrices();
			componentQuotePrices.setComponentName(value.getKey());
			componentQuotePrices.setNewQuote(value.getValue().getNewQuotePrice());
			componentQuotePrices.setOldQuote(0d);
			componentQuotePrices.setDelta(componentQuotePrices.getNewQuote() - componentQuotePrices.getOldQuote());
			ComponentList.add(componentQuotePrices);
		});
	}

	/**
	 * This method fetches the new quotes of both ARC and NRC components
	 * 
	 * @param arcPrice
	 * @param nrcPrice
	 * @param quoteProductComponent
	 * @param quotePrice
	 */
	private void getNewQuotesARCNRC(Map<String, CompareQuotePrices> arcPrice, Map<String, CompareQuotePrices> nrcPrice,
			QuoteProductComponent quoteProductComponent, QuotePrice quotePrice) {
		if (!Objects.isNull(quotePrice)) {
			if (Objects.nonNull(quotePrice.getEffectiveArc())) {
				CompareQuotePrices compareQuotePrices = new CompareQuotePrices();
				if ((!arcPrice.containsKey(quoteProductComponent.getMstProductComponent().getName()))) {
					compareQuotePrices.setNewQuotePrice(quotePrice.getEffectiveArc());
					arcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				} else {
					Double arcValue = arcPrice.get(quoteProductComponent.getMstProductComponent().getName())
							.getNewQuotePrice() + quotePrice.getEffectiveArc();
					compareQuotePrices.setNewQuotePrice(arcValue);
					arcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				}
				arcPrice.remove("Shifting Charges");
			}

			if (Objects.nonNull(quotePrice.getEffectiveNrc())) {
				CompareQuotePrices compareQuotePrices = new CompareQuotePrices();
				if (!nrcPrice.containsKey(quoteProductComponent.getMstProductComponent().getName())) {
					compareQuotePrices.setNewQuotePrice(quotePrice.getEffectiveNrc());
					nrcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				} else {
					Double nrcValue = nrcPrice.get(quoteProductComponent.getMstProductComponent().getName())
							.getNewQuotePrice() + quotePrice.getEffectiveNrc();
					compareQuotePrices.setNewQuotePrice(nrcValue);
					nrcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				}
			}
			nrcPrice.remove("Additional IPs");
		}

	}

	private String convertNewValuetoKbps(String newValue) {
		String bandwidthVal = newValue.substring(0, (newValue.indexOf(MACDConstants.MBPS)) - 1);
		Double newVal = Double.valueOf(bandwidthVal);
		return String.valueOf((int) (newVal * 1024));
	}

	private String convertNewValuetoGbps(String newValue) {
		String bandwidthVal = newValue.substring(0, (newValue.indexOf(MACDConstants.MBPS)) - 1);
		Double newVal = Double.valueOf(bandwidthVal);
		return String.valueOf((int) (newVal / 1000));
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public NplQuoteDetail macdApprovedQuotes(UpdateRequest request, String ipAddress) throws TclCommonException {

		NplQuoteDetail detail = null;
		try {
			detail = new NplQuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);
			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				Map<String, Object> responseCollection = constructOrder(quote, detail);
				order = (Order) responseCollection.get(NplOrderConstants.METHOD_RESPONSE);
				
				if (Objects.nonNull(responseCollection.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP))
						&& responseCollection.containsKey(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP)) {
					Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
							.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);

					persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
				}else  {
					LOGGER.info("Inside else part QUOTE_LINK_ID_ORDER_LINK_MAP value --> {}",responseCollection.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP));
					constructOrderSiteToServiceForCrossConnect(order);
				}
				saveMacdOrderTypeAndOrderCategory(order, quote);
				detail.setOrderId(order.getId());
				detail.setOrderCategory(order.getOrderToLes().stream().findFirst().get().getOrderCategory());
				detail.setOrderType(order.getOrderToLes().stream().findFirst().get().getOrderType());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					omsSfdcService.processSiteDetails(quoteLe);
					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

					/*
					 * TODO: May be required later Boolean nat = (request.getCheckList() == null ||
					 * request.getCheckList().equalsIgnoreCase(CommonConstants.NO)) ? Boolean.FALSE
					 * : Boolean.TRUE;
					 */
					Map<String, String> cofObjectMapper = new HashMap<>();
					// Commnt for test Cross connect just remove after testing
					nplQuotePdfService.processCofPdf(quote.getId(), null, null, true, quoteLe.getId(), cofObjectMapper);
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
					if (cofDetails != null) {
						cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
					}
					User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
					String userEmail = null;
					if (userRepo != null) {
						userEmail = userRepo.getEmailId();
					}

					for (OrderToLe orderToLe : order.getOrderToLes()) {
						nplQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),
								cofObjectMapper);
						break;
					}
					// Trigger orderMail
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
					List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
					for (QuoteDelegation quoteDelegation : quoteDelegate) {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					}
					uploadSSIfNotPresent(quoteLe);
					/**
					 * commented due to requirement change for MSA mapping while optimus journey
					 */
					// uploadMSAIfNotPresent(quoteLe);

				}
			}

			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}

				if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteLe.getQuoteType())) {

					if (Objects.nonNull(quoteLe.getQuoteCategory())
							&& quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				}
			}

			if (order.getQuote().getQuoteToLes().stream().findFirst().isPresent()) {
				QuoteToLe quoteToLe = order.getQuote().getQuoteToLes().stream().findFirst().get();
				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
				if (Objects.nonNull(macdDetail)) {
					macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
					macdDetailRepository.save(macdDetail);
				}
			}
			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileLink(quote);
			}

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;

	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order Method to get save Macd order type and
	 *            order Category
	 *
	 * @param order
	 * @param quote
	 */
	private void saveMacdOrderTypeAndOrderCategory(Order order, Quote quote) {
		order.getOrderToLes().stream().forEach(orderToLe -> {
			orderToLe.setOrderType(quote.getQuoteToLes().stream().findFirst().get().getQuoteType());
			orderToLe.setOrderCategory(quote.getQuoteToLes().stream().findFirst().get().getQuoteCategory());
			orderToLe.setErfServiceInventoryParentOrderId(
					quote.getQuoteToLes().stream().findFirst().get().getErfServiceInventoryParentOrderId());
			orderToLe.setTpsSfdcParentOptyId(quote.getQuoteToLes().stream().findFirst().get().getTpsSfdcParentOptyId());
			orderToLe.setSourceSystem(quote.getQuoteToLes().stream().findFirst().get().getSourceSystem());
			orderToLeRepository.save(orderToLe);
		});
	}

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

			ordersBean = nplOrderService.constructOrder(order);
			ordersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
			ordersBean.setOrderType(order.getOrderToLes().stream().findFirst().get().getOrderType());
			ordersBean.setOrderCategory(order.getOrderToLes().stream().findFirst().get().getOrderCategory());

			List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository
					.findByOrderToLe_Id(order.getOrderToLes().stream().findFirst().get().getId());

			List<Boolean> flagList = new ArrayList<>();
			Boolean[] macdFlagValue = { false };
			if (Objects.nonNull(orderIllSiteToServiceList) && !orderIllSiteToServiceList.isEmpty()) {
				orderIllSiteToServiceList.stream().forEach(orderToService -> {

					try {

						Map<String, Object> macdFlag = macdUtils
								.getMacdInitiatedStatusForNPL(orderToService.getErfServiceInventoryTpsServiceId());
						LOGGER.info("macdFlag" + macdFlag);
						if (Objects.nonNull(macdFlag) && !macdFlag.isEmpty()) {
							if (macdFlag.size() == 1)
								macdFlagValue[0] = (Boolean) macdFlag
										.get(orderToService.getErfServiceInventoryTpsServiceId());
							else
								macdFlagValue[0] = setMacdFlag(macdFlag, macdFlagValue);
						}
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
					flagList.add(macdFlagValue[0]);
				});

			}
			if (flagList.contains(true))
				ordersBean.setIsMacdInitiated(true);

			if (Objects.nonNull(orderIllSiteToServiceList) && !orderIllSiteToServiceList.isEmpty()) {
				LOGGER.info("Service Id: " + orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
				ordersBean.setServiceId(orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
				LOGGER.info("Response Service Id: " + ordersBean.getServiceId());
			}

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBean;

	}

	public NplQuoteDetail approvedMacdManualQuotes(Integer quoteId) throws TclCommonException {
		NplQuoteDetail detail = null;
		try {
			detail = new NplQuoteDetail();
			String sfdcId = null;

			if (Objects.isNull(quoteId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
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
				Map<String, Object> responseCollection = constructOrder(quote, detail);
				order = (Order) responseCollection.get(NplOrderConstants.METHOD_RESPONSE);
				
				if (!order.getOrderCode().startsWith("NDE")) {
				 if(checkO2cEnabled(quote)) { LOGGER.info("OnnetWL");
				 	order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 	order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
				 	orderRepository.save(order); 
				  }
				}


				//persist method should not be called for Cross connect as no link present in cross connect
				Set<ProductSolution> productSolutions = quote.getQuoteToLes().stream().findFirst().get().getQuoteToLeProductFamilies().stream().findFirst().get().getProductSolutions();
				//LOGGER.info("Product solutuon name is ----> " , productSolutions.stream().findFirst().get().getMstProductOffering().getProductName());
				for(ProductSolution sol : productSolutions){
					if(Objects.nonNull(sol.getMstProductOffering().getProductName())){
						LOGGER.info("Product Name is ----> {} ", sol.getMstProductOffering().getProductName());
					}
					if(Objects.nonNull(sol.getMstProductOffering().getProductName()) &&
							!CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(sol.getMstProductOffering().getProductName())){
						LOGGER.info("Entering persistOrderNplLinkWithOrder method for quote ----> {}  ", quote.getQuoteCode());
						Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
								.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);
						persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
					}
					else{
						constructOrderSiteToServiceForCrossConnect(order);
						//constructOrderProductComponentForCrossConnect(order,productSolutions);
					}
				}

				saveMacdOrderTypeAndOrderCategory(order, quote);
				detail.setOrderId(order.getId());

				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					sfdcId = quoteLe.getTpsSfdcOptyId();
					omsSfdcService.processSiteDetails(quoteLe);
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
					for (OrderToLe orderToLe : order.getOrderToLes()) {
						nplQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),
								cofObjectMapper);
						break;
					}
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);

					for (QuoteDelegation quoteDelegation : quoteDelegate) {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					}
				}
			}

			quote.getQuoteToLes().stream().forEach(quoteLe -> {

				if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}

			});

			if (order.getQuote().getQuoteToLes().stream().findFirst().isPresent()) {
				QuoteToLe quoteToLe = order.getQuote().getQuoteToLes().stream().findFirst().get();
				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
				if (Objects.nonNull(macdDetail)) {
					macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
					macdDetailRepository.save(macdDetail);
				}
			}

			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileLink(quote);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	public NplQuoteDetail approvedMacdDocusignQuotes(String quoteuuId) throws TclCommonException {
		NplQuoteDetail detail = null;
		try {
			detail = new NplQuoteDetail();
			Quote quote = quoteRepository.findByQuoteCode(quoteuuId);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}

			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				// order = constructOrder(quote, detail);
				Map<String, Object> responseCollection = constructOrder(quote, detail);
				order = (Order) responseCollection.get(NplOrderConstants.METHOD_RESPONSE);
				if (!order.getOrderCode().startsWith("NDE")) {
				 if (checkO2cEnabled(quote)) { LOGGER.info("OnnetWL");
				 order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
				 orderRepository.save(order); 
				 }
				}
				Set<ProductSolution> productSolutions = quote.getQuoteToLes().stream().findFirst().get()
						.getQuoteToLeProductFamilies().stream().findFirst().get().getProductSolutions();
				// LOGGER.info("Product solutuon name is ----> " ,
				// productSolutions.stream().findFirst().get().getMstProductOffering().getProductName());
				for (ProductSolution sol : productSolutions) {
					if (Objects.nonNull(sol.getMstProductOffering().getProductName())) {
						LOGGER.info("Product Name is ----> {} ", sol.getMstProductOffering().getProductName());
					}
					if (Objects.nonNull(sol.getMstProductOffering().getProductName())
							&& !CommonConstants.MMR_CROSS_CONNECT
									.equalsIgnoreCase(sol.getMstProductOffering().getProductName())) {
						@SuppressWarnings("unchecked")
						Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
								.get(NplOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);
						persistOrderNplLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
					}
				}
				saveMacdOrderTypeAndOrderCategory(order, quote);
				detail.setOrderId(order.getId());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					omsSfdcService.processSiteDetails(quoteLe);
					Date cofSignedDate = new Date();
					DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
					if (docusignAudit != null && docusignAudit.getCustomerSignedDate() != null
							&& (docusignAudit.getStatus().equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString())
									|| docusignAudit.getStatus()
											.equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString()))) {
						cofSignedDate = docusignAudit.getCustomerSignedDate();
					}
					omsSfdcService.processUpdateOpportunity(cofSignedDate, quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
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
						nplQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),
								cofObjectMapper);
						break;
					}
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
					for (QuoteDelegation quoteDelegation : quoteDelegate) {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					}
				}
			}

			quote.getQuoteToLes().stream().forEach(quoteLe -> {
				if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
					quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
					quoteToLeRepository.save(quoteLe);
				}

			});

			if (order.getQuote().getQuoteToLes().stream().findFirst().isPresent()) {
				QuoteToLe quoteToLe = order.getQuote().getQuoteToLes().stream().findFirst().get();
				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
				if (Objects.nonNull(macdDetail)) {
					macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
					macdDetailRepository.save(macdDetail);
				}
			}
			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileLink(quote);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}
	
	private Boolean checkO2cEnabled(Quote quote) {
		Boolean status = false;
		// Disable for Partner orders
		if(Objects.nonNull(quote.getEngagementOptyId())) {
			return true;
		}
		List<ProductSolution> productSolutions = productSolutionRepository.findByReferenceCode(quote.getQuoteCode());
		List<QuoteNplLink> nplLinks = nplLinkRepository.findByQuoteIdAndStatus(quote.getId(), (byte) 1);
		if (nplLinks != null && !nplLinks.isEmpty()) {
			LOGGER.info("CheckO2C QuoteNpl Link exists");
			QuoteNplLink link = nplLinks.get(0);
			List<LinkFeasibility> linkFeasibilityList=linkFeasibilityRepository.findByQuoteNplLink_IdAndIsSelected(link.getId(), (byte) 1);
			if(linkFeasibilityList!=null && !linkFeasibilityList.isEmpty()){
				LOGGER.info("CheckO2C QuoteNpl LinkFeasibility exists");
				LinkFeasibility linkFeasibility=linkFeasibilityList.get(0);
				if(("OnnetWL_NPL".equals(linkFeasibility.getFeasibilityMode()) || "OnnetWL".equals(linkFeasibility.getFeasibilityMode()) || "Onnet Wireline".equals(linkFeasibility.getFeasibilityMode())) 
						&& !isCrossConnectExists(productSolutions)){
					LOGGER.info("CheckO2C QuoteNpl LinkFeasibility Mode OnnetWL");
					status=true;
				}
			}
		}
        else if(nplLinks != null && nplLinks.isEmpty() && productSolutions != null && !productSolutions.isEmpty() && isCrossConnectExists(productSolutions)) {
            LOGGER.info("Triggering O2C for BSO MMR Cross Connect quote code {}", productSolutions.get(0).getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode());
            status = true;
        }
		return status;
	}

	private boolean isCrossConnectExists(List<ProductSolution> productSolutions) {
		if(productSolutions!=null && !productSolutions.isEmpty() && 
				productSolutions.stream().filter(solution->solution.getProductProfileData().contains("MMR Cross Connect")).findAny().isPresent()){
				return true;
		}
		return false;
	}
	
	//added for nde mc
	@Override
	public NplQuoteBean updateLinkMc(NplMcQuoteDetailBean quoteDetail, Integer erfCustomerId, Integer quoteId)
			throws TclCommonException {
		LOGGER.info("inside updateLinkMc"+quoteDetail.getLinks());
		validateSiteInformationMc(quoteDetail, quoteId);
		Integer linkCount = quoteDetail.getLinkCount();
		NplQuoteBean quoteBean = null;
		LOGGER.info("link count{}", linkCount);
		if (linkCount > 0) {

			Integer siteA = null;
			Integer siteB = null;
			Integer siteId = null;
			Integer locA = null;
			Integer locB = null;
			ProductSolution productSolution = null;
			String siteAType = "";
			String siteBType = "";
			String productOfferingName = null;
			try {
				LOGGER.info("Customer Id received is {} ", erfCustomerId);

				User user = getUserId(Utils.getSource());
				Boolean siteChanged = false;
				String serviceId = null;
				MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
				if (quoteToLe.isPresent()) {
					if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
						LOGGER.info("inside deactivateOtherLinks in nde mc");
						deactivateOtherLinks(quoteToLe);
					}
				}
				for (NplLinkDetailBean link : quoteDetail.getLinks()) {
					LOGGER.info("inside link loop");
					List<NplSiteDetail> sites = link.getSites();
					if (quoteToLe.isPresent() && sites.size() == 2 && user != null) {
						for (NplSiteDetail site : sites) {
							QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
									.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
							productOfferingName = link.getOfferingName();
							MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName,
									user);
							productSolution = productSolutionRepository
									.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily,
											productOfferng);
							
								if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
									if (MACDConstants.SHIFT_SITE_SERVICE
											.equalsIgnoreCase(quoteToLe.get().getQuoteCategory())) {
										if (site.getSiteChangeflag() == true) {
											siteChanged = site.getSiteChangeflag();
											serviceId = site.getErfServiceInventoryTpsServiceId();
										}
									} else if (MACDConstants.CHANGE_BANDWIDTH_SERVICE
											.equalsIgnoreCase(quoteToLe.get().getQuoteCategory())) {
										siteChanged = site.getSiteChangeflag();
										serviceId = site.getErfServiceInventoryTpsServiceId();
									}
								}
							
							siteId = processSiteDetailMc(user, productFamily, quoteToLeProductFamily, site,
									productOfferingName, productSolution, quoteToLe.get().getQuote(), siteChanged);
							LOGGER.info("TYPE AND ID FOR SITE:" + site.getSiteType() + "LOCID:"
									+ site.getLocationId()+"siteid"+siteId+"sitechnageFlag:"+siteChanged);
							if (site.getSiteType().equalsIgnoreCase("SiteA")) {
								siteA = siteId;
								siteAType = site.getType().getType();
								locA = site.getLocationId();
							}
							if (site.getSiteType().equalsIgnoreCase("SiteB")) {
								siteB = siteId;
								siteBType = site.getType().getType();
								locB = site.getLocationId();

							}
							siteChanged = false;
						}
						quoteDetail.setQuoteId(quoteId);
						LOGGER.info("serviceId before constructNplLinks {}", serviceId);
						if (siteA != null && siteB != null)
							//constructNplLinks(user, productFamily, quoteDetail.getQuoteId(), siteA, siteB,
									//productSolution, siteAType, siteBType, productOfferingName, locA, locB, serviceId,null,null);
						//added for nde mc multiple ehs id 
						constructNplLinksMacdMc(user, productFamily, quoteDetail.getQuoteId(), siteA, siteB,
								productSolution, siteAType, siteBType, productOfferingName, locA, locB, serviceId,null,null,link.getSolutions().get(0).getComponents());
						
					} else {
						throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
								ResponseResource.R_CODE_NOT_FOUND);
					}

				}

				if (quoteToLe.isPresent()) {
					switch (quoteToLe.get().getQuoteCategory()) {
					case MACDConstants.SHIFT_SITE_SERVICE:
						if (quoteToLe.get().getStage().equals(QuoteStageConstants.MODIFY.getConstantCode())) {
							quoteToLe.get().setStage(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode());
							quoteToLeRepository.save(quoteToLe.get());
						}
						break;
					case MACDConstants.CHANGE_BANDWIDTH_SERVICE:
						if (quoteToLe.get().getStage().equals(QuoteStageConstants.MODIFY.getConstantCode())) {
							quoteToLe.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
							quoteToLeRepository.save(quoteToLe.get());
						}
						break;
					default:
						break;

					}
				}

			} catch (Exception e) {
				LOGGER.error(String.format("Message:  %s", e.getMessage()));
				LOGGER.error("Cause: ", e.getCause());
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}

		}

		
		quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), false);
		return quoteBean;

	}
	
	private void deactivateOtherLinksMc(Optional<QuoteToLe> quoteToLe, Integer linkid) {
		LOGGER.info("Entered into deactivateOtherLinksMc id "+linkid);
		QuoteNplLink link = nplLinkRepository.findByIdAndStatus(linkid, CommonConstants.BACTIVE);
		if (link != null) {
			try {
				procesDeActivateLink(link.getId(), "delete");
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}

		}
	}

	public MACDOrderSummaryResponseBean getOrderSummaryMc(Integer quoteId, Integer quoteLeId)
			throws TclCommonException {
		LOGGER.info("entered into getOrderSummaryMc"+quoteId+quoteLeId);
		MACDOrderSummaryResponseBean macdOrderResponse = new MACDOrderSummaryResponseBean();
		List<MACDOrderSummaryResponse> macdOrderSummaryResponseList = new ArrayList<>();
		SIServiceInfoBean[] siDetailedInfoResponseIAS = null;
		List<SIServiceInfoBean> siServiceInfoResponse = null;
		String changeRequests = null;

		if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) )
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
		
		List<String> serviceIdsList = macdUtils.getServiceIdsMc(quoteToLeOpt.get());
		String serviceIds = serviceIdsList.stream().map(i -> i.trim())
				.collect(Collectors.joining(","));
		LOGGER.info("getOrderSummaryMc serviceIds"+serviceIds);

		if (quoteToLeOpt.isPresent()) {

			// Queue call to get old attribute values from service details
			String iasQueueResponse = (String) mqUtils.sendAndReceive(siServiceDetailsListMcQueue, serviceIds);
			LOGGER.info("getOrderSummaryMc iasQueueResponse"+iasQueueResponse);

			if (StringUtils.isNotBlank(iasQueueResponse)) {
				siDetailedInfoResponseIAS = (SIServiceInfoBean[]) Utils.convertJsonToObject(iasQueueResponse,
						SIServiceInfoBean[].class);
				siServiceInfoResponse = Arrays.asList(siDetailedInfoResponseIAS);
				// Logic to get new attribute values from oms
				LOGGER.info("siServiceInfoResponse size"+siServiceInfoResponse.size());
				siServiceInfoResponse.stream().forEach(serviceIdEntry -> {
					LOGGER.info("inside loop"+serviceIdEntry.getTpsServiceId());
					MACDOrderSummaryResponse response = new MACDOrderSummaryResponse();
					Map<String, String> newAttributesMapPrimary = new HashedMap<>();
					List<MACDAttributesComparisonBean> primaryAttributesList = new ArrayList<>();
					MACDAttributesBean attributesBean = new MACDAttributesBean();
					Map<String, String> oldAttributesMapPrimary = new HashedMap<>();

					getLocationDetails(oldAttributesMapPrimary, serviceIdEntry);
					getCircuitSpeed(oldAttributesMapPrimary, serviceIdEntry);
					getInterfaceDetails(oldAttributesMapPrimary, serviceIdEntry);
					getLocalLoopBandwidthDetails(oldAttributesMapPrimary, serviceIdEntry);
					getServiceAvailabilityDetails(oldAttributesMapPrimary, serviceIdEntry);
					response.setSiteType(serviceIdEntry.getSiteType());

					if (quoteToLeOpt.isPresent()) {

						try {

							List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository
									.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceIdEntry.getTpsServiceId(),
											quoteToLeOpt.get());
							Optional<QuoteIllSiteToService> quoteIllSiteToServiceOpt = quoteIllSiteToService.stream()
									.findFirst();
							if (quoteIllSiteToServiceOpt.isPresent()) {

								List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
										.findByReferenceIdAndReferenceName(
												quoteIllSiteToServiceOpt.get().getQuoteNplLink().getId(),
												QuoteConstants.NPL_LINK.toString());
								LOGGER.info("site type looped {}", response.getSiteType());
								if (response.getSiteType().equalsIgnoreCase(MACDConstants.SI_SITEA)) {
									Optional<QuoteIllSite> illSiteAOpt = illSiteRepository
											.findById(quoteIllSiteToServiceOpt.get().getQuoteNplLink().getSiteAId());
									getLatLongDetails(newAttributesMapPrimary,
											illSiteAOpt.get().getErfLocSitebLocationId());
								} else {
									Optional<QuoteIllSite> illSiteBOpt = illSiteRepository
											.findById(quoteIllSiteToServiceOpt.get().getQuoteNplLink().getSiteBId());
									getLatLongDetails(newAttributesMapPrimary,
											illSiteBOpt.get().getErfLocSitebLocationId());
								}

								getPrimaryInternetPortDetails(newAttributesMapPrimary, quoteProductComponentList,
										response.getSiteType(), oldAttributesMapPrimary, quoteToLeOpt, "");

								getPrimaryLastMileDetails(newAttributesMapPrimary, quoteProductComponentList,
										response.getSiteType());

								getParallelBuildAndParallelRunDays(quoteProductComponentList, response);

							}
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}

					}
					if (!newAttributesMapPrimary.equals(oldAttributesMapPrimary))

					{
						newAttributesMapPrimary.entrySet().stream()
								.filter(att -> !att.getKey().equalsIgnoreCase(MACDConstants.LOCATION_ID)
										&& !((att.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
												|| att.getKey().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))
												&& Objects.nonNull(att.getValue()) && compareNewAndOldValues(
														oldAttributesMapPrimary.get(att.getKey()), att.getValue())))
								.forEach(attribute -> {
									if (Objects.nonNull(attribute.getValue()) && !attribute.getValue()
											.equalsIgnoreCase(oldAttributesMapPrimary.get(attribute.getKey()))) {
										LOGGER.info("Attribute name {}, attribute value {}, old attribute value {}",
												attribute.getKey(), attribute.getValue(),
												oldAttributesMapPrimary.get(attribute.getKey()));
										MACDAttributesComparisonBean bean = new MACDAttributesComparisonBean();
										bean.setAttributeName(attribute.getKey());
										if ((attribute.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
												|| attribute.getKey()
														.equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))) {
											bean.setNewAttributes(convertNewBandwidthValue(attribute.getValue()));
										} else
											bean.setNewAttributes(attribute.getValue());
										bean.setOldAttributes(oldAttributesMapPrimary.get(attribute.getKey()));
										if (Objects.nonNull(attribute.getKey())
												&& attribute.getKey().equalsIgnoreCase(MACDConstants.LAT_LONG)) {
											if (quoteToLeOpt.isPresent()
													&& Objects.nonNull(quoteToLeOpt.get().getQuoteCategory())) {
												LOGGER.info(
														"Location id in quote - {} is different from service inventory - {} for category {}",
														newAttributesMapPrimary.get(MACDConstants.LOCATION_ID),
														oldAttributesMapPrimary.get(MACDConstants.LOCATION_ID),
														quoteToLeOpt.get().getQuoteCategory());
												if (MACDConstants.SHIFT_SITE_SERVICE
														.equals(quoteToLeOpt.get().getQuoteCategory())) {
													bean.setAttributeName(MACDConstants.LOCATION_ID);
													bean.setNewAttributes(
															newAttributesMapPrimary.get(MACDConstants.LOCATION_ID));
													bean.setOldAttributes(
															oldAttributesMapPrimary.get(MACDConstants.LOCATION_ID));
													response.setChangeRequests(response.getChangeRequests() == null
															? MACDConstants.SHIFT_SITE
															: response.getChangeRequests() + " + "
																	+ MACDConstants.SHIFT_SITE);
												} else {
													throw new TclCommonRuntimeException(
															ExceptionConstants.LOCATION_ID_MISMATCH,
															ResponseResource.R_CODE_ERROR);
												}
											}
										} else if (Objects.nonNull(attribute.getKey())
												&& attribute.getKey()
														.equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
												|| attribute.getKey()
														.equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString())) {
											response.setChangeRequests(response.getChangeRequests() == null
													? MACDConstants.CHANGE_BANDWIDTH
													: response.getChangeRequests()
															.contains(MACDConstants.CHANGE_BANDWIDTH)
																	? response.getChangeRequests()
																	: response.getChangeRequests() + " + "
																			+ MACDConstants.CHANGE_BANDWIDTH);
										}
										LOGGER.info("Change reuqest summary before persisting is ---> {} ", response.getChangeRequests());
										primaryAttributesList.add(bean);
									}

								});
					}

					attributesBean.setPrimaryAttributesList(primaryAttributesList);
					response.setQuotesAttributes(attributesBean);
					response.setServiceId(serviceIdEntry.getTpsServiceId());
					if (quoteToLeOpt.isPresent()) {
						quoteToLeOpt.get().setChangeRequestSummary(response.getChangeRequests());
						quoteToLeRepository.save(quoteToLeOpt.get());

					}

					try {
						response.setPricingsList(quoteCompare(quoteId, serviceIdEntry.getSiOrderId(),
								serviceIdEntry.getTpsServiceId(), serviceIdEntry));
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);

					}

					macdOrderSummaryResponseList.add(response);

				});
			}
		}
		LOGGER.info("macdOrderSummaryResponseList"+macdOrderSummaryResponseList.size());
		if (quoteToLeOpt.isPresent()) {
			if (quoteToLeOpt.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
				quoteToLeOpt.get().setStage(QuoteStageConstants.CHANGE_ORDER.getConstantCode());
				quoteToLeRepository.save(quoteToLeOpt.get());
			}
		}

		Optional<String> longest = macdOrderSummaryResponseList.stream()
				.map(MACDOrderSummaryResponse::getChangeRequests).filter(Objects::nonNull)
				.sorted((e1, e2) -> e1.length() > e2.length() ? -1 : 1).findFirst();
		LOGGER.info("Change request string {}", longest);
		if (longest.isPresent())
			macdOrderResponse.setChangeRequests(longest.get());
		macdOrderResponse.setMacdOrderSummaryResponseList(macdOrderSummaryResponseList);

		return macdOrderResponse;

	}
	
	public Boolean isServiceIdExit(SIServiceDetailsBean beanval, List<SIServiceDetailsBean> beanList)
			throws TclCommonException {
		Boolean isIdPresent=false;
		long serviceidCount=0;
		LOGGER.info("Entered into isServiceIdExit"+beanval.getTpsServiceId());
		try {
			 serviceidCount = beanList.stream()
				    .filter(bean -> bean.getTpsServiceId().contains(beanval.getTpsServiceId()))
				    .count();
			 LOGGER.info("isServiceIdExit serviceidCount value"+serviceidCount);
			 if(serviceidCount==0) {
				 isIdPresent=false;
			 }
			 else {
				 isIdPresent=true;
			 }
			
		} 
		catch (Exception e) {
			LOGGER.error(String.format("MessageisServiceIdExit:  %s", e.getMessage()));
			LOGGER.error("CauseisServiceIdExit: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("isIdPresent%%%%%%%%%%%%%%%%%%%"+isIdPresent);
		return isIdPresent;
	}

}
