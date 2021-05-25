package com.tcl.dias.oms.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.ProfileSelectionInputDetails;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.entity.entities.IzosdwanPricingService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPricingServiceBean;
import com.tcl.dias.oms.izosdwan.beans.PricingUpdateRequest;
import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.service.v1.BundleOmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class IzoSdwanListener {

	@Autowired
	IzosdwanQuoteService izosdwanQuoteService;

	@Autowired
	IzosdwanPricingAndFeasibilityService izosdwanPricingAndFeasibilityService;

	@Autowired
	BundleOmsSfdcService bundleOmsSfdcService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanListener.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.product.cpe.detail}") })
	public String getUniqueCpeDetails(String request) throws TclCommonException {
		String response = "";
		try {

			Integer list = Utils.convertJsonToObject((String) request, Integer.class);
			LOGGER.info("Details in product micro Service {}", list);
			List<String> cpeDet = izosdwanQuoteService.getDistinctCpes(list);
			response = Utils.convertObjectToJson(cpeDet);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.quote.countries}") })
	public String getUniqueCountryDetailsForTheQuote(String request) throws TclCommonException {
		String response = "";
		try {

			Integer quoteId = Utils.convertJsonToObject((String) request, Integer.class);
			LOGGER.info("Quote id received is  {} inside getUniqueCountryDetailsForTheQuote queue call", quoteId);
			List<String> countries = izosdwanQuoteService.getUniqueCountriesForTheQuote(quoteId);
			response = Utils.convertObjectToJson(countries);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.quote.price.service}") })
	public void processIzosdwanQuotePriceServiceRequest(Message<String> requestBody) throws TclCommonException {
		try {
			LOGGER.info("Input received for processIzosdwanQuotePriceServiceRequest {}", requestBody.getPayload());
			String request = requestBody.getPayload();
			if (request != null) {

				List<IzosdwanPricingService> izosdwanPricingServices = IzosdwanUtils.fromJson(request,
						new TypeReference<List<IzosdwanPricingService>>() {
						});
				LOGGER.info("Processing the requests in processIzosdwanQuotePriceServiceRequest", request);
				izosdwanPricingAndFeasibilityService.processIzosdwanPricingServiceEntries(izosdwanPricingServices);
			}

		} catch (Exception e) {

			LOGGER.error("Error in processing processIzosdwanQuotePriceServiceRequest ", e);
			throw new TclCommonException(e);

		}

	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.sfdc.product}") })
	public String processProductServices(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for processProductServices :: {}", request);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(request, HashMap.class);
			bundleOmsSfdcService.processProductServices((Integer) inputMap.get("quoteToLeId"),
					(String) inputMap.get("productName"),(boolean) inputMap.get("isNew"));
			response = "True";
			
		} catch (Exception e) {
			response = "False";
			LOGGER.error("Error in processing processProductServices queue", e);
			throw new TclCommonException(e);

		}
		
		return response;

	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.quote.site.fpdetails}") })
	public String getFeasiblityAndPricingDetailsForQuoteIllSites(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for getFeasiblityAndPricingDetailsForQuoteIllSites :: {}", request);
			Integer siteId = Integer.valueOf((request));
			LOGGER.info("Quote id received is  {} inside getUniqueCountryDetailsForTheQuote queue call", siteId);
			QuoteIllSitesWithFeasiblityAndPricingBean feasPricBean = izosdwanQuoteService
					.getFeasiblityAndPricingDetailsForQuoteIllSites(siteId);
			response = Utils.convertObjectToJson(feasPricBean);
			LOGGER.info("response of getFeasiblityAndPricingDetailsForQuoteIllSites: {}", response);
		} catch (Exception e) {

			LOGGER.error("Error in processing getFeasiblityAndPricingDetailsForQuoteIllSites queue", e);
			throw new TclCommonException(e);
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.quote.site.details}") })
	public String getQuoteDetails(String request) throws TclCommonException {
		QuoteBean quoteBean = null;
		String response = "";
		try {
			LOGGER.info("Input Payload received for getQuoteDetails :: {}", request);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(request, HashMap.class);
			Integer quoteId = Integer.valueOf(inputMap.get("quoteId").toString());
			LOGGER.info("Input Payload received for quoteId :: {}", quoteId);

			List<Integer> siteIds = null;
			if (inputMap.get("siteIds") != null) {
				siteIds = (List<Integer>) inputMap.get("siteIds");
				LOGGER.info("Input Payload received for siteIds :: {}", siteIds);
			}

			String feasiblesites = null;
			if (inputMap.get("feasiblesites") != null) {
				feasiblesites = String.valueOf(inputMap.get("feasiblesites"));
				LOGGER.info("Input Payload received for feasiblesites :: {}", feasiblesites);
			}
			Boolean siteproperities = null;
			if (inputMap.get("siteproperities") != null) {
				siteproperities = Boolean.valueOf(inputMap.get("siteproperities").toString());
				LOGGER.info("Input Payload received for siteproperities :: {}", siteproperities);
			}

			Integer siteId = null;
			if (inputMap.get("siteId") != null) {
				siteId = Integer.valueOf(inputMap.get("siteId").toString());
				LOGGER.info("Input Payload received for siteId :: {}", siteId);
			}

			quoteBean = izosdwanQuoteService.getQuoteDetails(quoteId, feasiblesites, siteproperities, siteId, siteIds);
			response = Utils.convertObjectToJson(quoteBean);
			LOGGER.info("response of getQuoteDetails: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in processing getQuoteDetails queue", e);
			throw new TclCommonException(e);
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.quote.sites.type}") })
	public String getSitesBasedOnSiteType(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for getSitesFromQuote :: {}", request);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(request, HashMap.class);
			Integer quoteId = Integer.valueOf(inputMap.get("quoteId").toString());
			String type = String.valueOf(inputMap.get("type"));
			LOGGER.info("Input Payload received for quoteId :: {}", quoteId);
			LOGGER.info("Input Payload received for type :: {}", type);
			List<ViewSitesSummaryBean> viewSitesSummaryBeans = izosdwanQuoteService.getSitesBasedOnSiteType(quoteId,
					type, null);
			response = Utils.convertObjectToJson(viewSitesSummaryBeans);
			LOGGER.info("response of getSitesBasedOnSiteType: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in processing getSitesBasedOnSiteType queue", e);
			throw new TclCommonException(e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.quote.pricing}") })
	public String getPricingServiceListForTheQuote(String request) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input Payload received for getSitesFromQuote :: {}", request);
			Integer quoteId = Integer.valueOf((request));
			LOGGER.info("Input Payload received for quoteId :: {}", quoteId);
			List<IzosdwanPricingServiceBean> izosdwanPricingServiceBeans = izosdwanQuoteService
					.getPricingServiceListForTheQuote(quoteId);
			response = Utils.convertObjectToJson(izosdwanPricingServiceBeans);
			LOGGER.info("response of getPricingServiceListForTheQuote: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in processing getPricingServiceListForTheQuote queue", e);
			throw new TclCommonException(e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.quote.price.update}") })
	public String updateManualPrice(String request) throws TclCommonException {
		String response = "";
		try {

			LOGGER.info("Input Payload received for updateManualPrice :: {}", request);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(request, HashMap.class);
			Integer quoteId = Integer.valueOf(inputMap.get("quoteId").toString());
			Integer quoteLeId = Integer.valueOf(inputMap.get("quoteLeId").toString());
			String pricingUpdateReq = String.valueOf(inputMap.get("pricingUpdateReq"));
			PricingUpdateRequest pricingUpdateRequest = (PricingUpdateRequest) Utils
					.convertJsonToObject(pricingUpdateReq, PricingUpdateRequest.class);
			LOGGER.info("Input Payload received for quoteId :: {}", quoteId);
			LOGGER.info("Input Payload received for quoteLeId :: {}", quoteLeId);
			LOGGER.info("Input Payload received for PricingUpdateRequest :: {}", pricingUpdateRequest);
			Boolean isSuccess = izosdwanPricingAndFeasibilityService.updateManualPrice(pricingUpdateRequest, quoteId,
					quoteLeId);
			response = Utils.convertObjectToJson(isSuccess);
			LOGGER.info("response of updateManualPrice: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in processing updateManualPrice queue", e);
			throw new TclCommonException(e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.quote.stage.update}") })
	public String updateQuoteStage(String request) throws TclCommonException {

		String response = "";
		Integer quoteId = 0;
		Integer quoteToLeId = 0;
		String stageName = "";
		try {
			LOGGER.info("Input Payload received for updateQuoteStage :: {}", request);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(request,
					HashMap.class);

			if (inputMap.get("quoteId") != null) {
				quoteId = Integer.valueOf(inputMap.get("quoteId").toString());
				LOGGER.info("Input Payload received for quoteId :: {}", quoteId);
			}
			if (inputMap.get("quoteToLeId") != null) {
				quoteToLeId = Integer.valueOf(inputMap.get("quoteToLeId").toString());
				LOGGER.info("Input Payload received for quoteToLeId :: {}", quoteToLeId);
			}

			if (inputMap.get("stageName") != null) {
				stageName = inputMap.get("stageName").toString();
				LOGGER.info("Input Payload received for stageName :: {}", stageName);
			}

			response = izosdwanQuoteService.updateQuoteStage(quoteId, quoteToLeId, stageName);
			LOGGER.info("response of updateQuoteStage: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in processing updateQuoteStage queue", e);
			throw new TclCommonException(e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.profile.selection.details}") })
	public String profileSelectionDetails(String request) throws TclCommonException {

		String response = "";
		try {
			LOGGER.info("Input Payload received for profileSelectionDetails :: {}", request);
			ProfileSelectionInputDetails inputDetails = Utils
					.convertJsonToObject(request, ProfileSelectionInputDetails.class);

			List<VendorProfileDetailsBean> vendorProfileDetailsBeans = izosdwanQuoteService
					.getProfileSelectionDetails(inputDetails);
			response = Utils.convertObjectToJson(vendorProfileDetailsBeans);
			LOGGER.info("response of profileSelectionDetails: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in processing profileSelectionDetails queue", e);
			throw new TclCommonException(e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.update.term.and.months}") })
	public String updateTermAndMonths(String request) throws TclCommonException {

		String response = Utils.convertObjectToJson(false);
		Integer quoteId = 0;
		Integer quoteToLeId = 0;
		UpdateRequest updateRequest = new UpdateRequest();
		try {
			LOGGER.info("Input Payload received for profileSelectionDetails :: {}", request);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(request,
					HashMap.class);

			if (inputMap.get("quoteId") != null) {
				quoteId = Integer.valueOf(inputMap.get("quoteId").toString());
				LOGGER.info("Input Payload received for quoteId :: {}", quoteId);
			}

			if (inputMap.get("quoteToLeId") != null) {
				quoteToLeId = Integer.valueOf(inputMap.get("quoteToLeId").toString());
				LOGGER.info("Input Payload received for quoteToLeId :: {}", quoteToLeId);
			}

			if (inputMap.get("updateRequest") != null) {
				updateRequest = (UpdateRequest) inputMap.get("updateRequest");
				LOGGER.info("Input Payload received for updateRequest :: {}", updateRequest);
			}
			izosdwanQuoteService.updateTermsInMonthsForQuoteToLe(quoteId, quoteToLeId, updateRequest, null);
			response = Utils.convertObjectToJson(true);
		} catch (Exception e) {
			LOGGER.error("Error in processing profileSelectionDetails queue", e);
			throw new TclCommonException(e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.persist.solution.details}") })
	public String persistSolutionDetails(String request) throws TclCommonException {

		String response = "";
		QuoteDetail quoteDetail = new QuoteDetail();
		try {
			LOGGER.info("Input Payload received for persistSolutionDetails :: {}", request);

			quoteDetail = Utils.convertJsonToObject(request, QuoteDetail.class);

			QuoteResponse quoteResponse = izosdwanQuoteService.persistSolutionDetails(quoteDetail);
			response = Utils.convertObjectToJson(quoteResponse);
		} catch (Exception e) {
			LOGGER.error("Error in processing persistSolutionDetails queue", e);
			throw new TclCommonException(e);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.update.site.properties.attributes}") })
	public String updateSitePropertiesAttributes(String request) throws TclCommonException {
		String response = "";
		QuoteDetail quoteDetail = new QuoteDetail();
		List<UpdateRequest> updateRequests = new ArrayList<UpdateRequest>();
		try {
			LOGGER.info("Input Payload received for updateSitePropertiesAttributes :: {}", request);
			updateRequests = Utils.convertJsonToObject(request, List.class);
			quoteDetail = izosdwanQuoteService.updateSitePropertiesAttributes(updateRequests);
			response = Utils.convertObjectToJson(quoteDetail);
		} catch (Exception e) {
			LOGGER.error("Error in processing updateSitePropertiesAttributes queue", e);
			throw new TclCommonException(e);
		}
		return response;

	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.create.quote}") })
	public String createQuote(String request) throws TclCommonException {
		String response = "";
		Integer customerId = 0;
		QuoteDetail quoteDetail = new QuoteDetail();
		QuoteResponse quoteResponse = new QuoteResponse();
		try {
			LOGGER.info("Input Payload received for profileSelectionDetails :: {}", request);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(request,
					HashMap.class);

			if (inputMap.get("customerId") != null) {
				customerId = Integer.valueOf(inputMap.get("customerId").toString());
				LOGGER.info("Input Payload received for customerId :: {}", customerId);
			}

			if (inputMap.get("updateRequest") != null) {
				quoteDetail = (QuoteDetail) inputMap.get("updateRequest");
				LOGGER.info("Input Payload received for updateRequest :: {}", quoteDetail);
			}
			quoteResponse = izosdwanQuoteService.createQuote(quoteDetail, customerId);
			response = Utils.convertObjectToJson(quoteResponse);
		} catch (Exception e) {
			LOGGER.error("Error in processing profileSelectionDetails queue", e);
			throw new TclCommonException(e);
		}
		return response;

	}
}
