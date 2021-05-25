package com.tcl.dias.oms.izosdwan.controller.v1;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuotePdfService;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.IzoSdwanCpeBomInterface;
import com.tcl.dias.common.beans.IzoSdwanCpeDetails;
import com.tcl.dias.common.beans.IzoSdwanSiteDetails;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesBean;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesUpdateBean;
import com.tcl.dias.common.beans.LocationInputDetails;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.ProfileSelectionInputDetails;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.common.beans.VutmProfileDetailsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.ByonUploadResponse;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.LocationTemplateRequest;
import com.tcl.dias.oms.beans.ManualFeasibilitySiteBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIzoSdwanAttributeValue;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.SiteDocumentBean;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.izosdwan.beans.ActiveQuoteAndOrder;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPricingServiceBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanQuotePdfBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanSolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.NetworkSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.PricingInformationRequestBean;
import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SEASiteDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SEASiteInfoBean;
import com.tcl.dias.oms.izosdwan.beans.SEASiteUpdateRequest;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeDetails;
import com.tcl.dias.oms.izosdwan.beans.SolutionLevelPricingBreakupDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SolutionPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.VproxyPricingInputDatum;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionRequestBean;
import com.tcl.dias.oms.izosdwan.pdf.beans.CpeBomDetailsCof;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanGvpnPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanIllPricingAndFeasiblityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.pricing.bean.Result;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * This is the controller class for izosdwan
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/v1/izosdwan/quotes")
public class IzosdwanQuoteController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanQuoteService.class);
	@Autowired
	IzosdwanQuoteService izoSdwanQuoteService;

	@Autowired
	IzosdwanIllPricingAndFeasiblityService izosdwanIllPricingAndFeasiblityService;

	@Autowired
	IzosdwanPricingAndFeasibilityService izosdwanPricingService;

	@Autowired
	IzosdwanGvpnPricingAndFeasibilityService izosdwanGvpnPricingAndFeasibilityService;

	@Autowired
	IzosdwanQuotePdfService izosdwanQuotePdfService;

	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<QuoteResponse> createQuote(@RequestBody QuoteDetail request,
			@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		LOGGER.info("Info Is{}", request);
		QuoteResponse response = izoSdwanQuoteService.createQuote(request, customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/solutions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteDetail request) throws TclCommonException {
		QuoteResponse response = izoSdwanQuoteService.persistSolutionDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getQuoteConfiguration used to
	 *            fetch quote details
	 * @param customerId
	 * @param quoteId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,
			@RequestParam(required = false, name = "siteproperities") Boolean siteproperities,
			@RequestParam(required = false, name = "siteid") Integer siteId) throws TclCommonException {
		QuoteBean response = izoSdwanQuoteService.getQuoteDetails(quoteId, feasibleSites, siteproperities, siteId,
				null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * @author vpachava
	 * @link http://www.tatacommunications.com/ getCpeDetails- This method is used
	 *       to get the cpe and bandwidth details
	 * @param bean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SDWAN_CPE_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cpedetails/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<IzoSdwanCpeDetails>> getCpeDetails(@RequestParam("vendorName") String vendorName,
			@RequestParam(required = false, value = "addons") String addons,
			@RequestParam("profileName") String profileName) throws TclCommonException {
		List<IzoSdwanCpeDetails> response = izoSdwanQuoteService.getCpeDetails(vendorName, addons, profileName);
		return new ResponseResource<List<IzoSdwanCpeDetails>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * @author vpachava
	 * @link http://www.tatacommunications.com/ getCpeDetails- This method is used
	 *       to get the cpe site details
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SDWAN_PORT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sitedetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<IzoSdwanSiteDetails>> getCpeSiteDetails() throws TclCommonException {
		List<IzoSdwanSiteDetails> response = izoSdwanQuoteService.getSiteDetails();
		return new ResponseResource<List<IzoSdwanSiteDetails>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * @author vpachava
	 * @link http://www.tatacommunications.com/ getCpeDetails- This method is used
	 *       to get the network summary details
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_NETWORK_SUMMARY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/networksummary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NetworkSummaryDetails> getCpeSiteDetails(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		NetworkSummaryDetails response = izoSdwanQuoteService.getBandwidthDetails(quoteId);

		return new ResponseResource<NetworkSummaryDetails>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * @author mpalanis
	 * @link http://www.tatacommunications.com/ getCofSiteDetails- This method is
	 *       used to get all site type names with total no.of.sites each
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_NETWORK_SUMMARY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/cofsitedetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<SiteTypeDetails>> getSitetypeDetails(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		List<SiteTypeDetails> response = izoSdwanQuoteService.getSiteTypeDetails(quoteId);

		return new ResponseResource<List<SiteTypeDetails>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * @author vpachava
	 * @link http://www.tatacommunications.com/ getCpeDetails- This method is used
	 *       to get the configuration cpe bandwidth details
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONFIGURATION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/sitetype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfigurationSummaryBean> getCpeConfigDetails(@PathVariable("quoteId") Integer quoteId,
			@RequestParam("siteTypeName") String siteTypeName) throws TclCommonException {

		ConfigurationSummaryBean response = izoSdwanQuoteService.getConfigurationCpeDetails(quoteId, siteTypeName);

		return new ResponseResource<ConfigurationSummaryBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONFIGURATION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/viewsites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<ViewSitesSummaryBean>> getViewSitesSummaryDetails(
			@PathVariable("quoteId") Integer quoteId, @RequestParam("type") String type,
			@RequestBody(required = false) LocationInputDetails request) throws TclCommonException {

		List<ViewSitesSummaryBean> response = izoSdwanQuoteService.getSitesBasedOnSiteType(quoteId, type, request);

		return new ResponseResource<List<ViewSitesSummaryBean>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE_PROPERTIES)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/propertiesattribute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateSitePropertiesAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId, @RequestBody List<UpdateRequest> request)
			throws TclCommonException {
		QuoteDetail response = izoSdwanQuoteService.updateSitePropertiesAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/trigger/feasiblities/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerForFeasibilityBean(@RequestBody FeasibilityBean request)
			throws TclCommonException {
		izoSdwanQuoteService.triggerFeasibility(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * 
	 * getQuoteConfigurationBySites
	 * 
	 * @param quoteId
	 * @param feasibleSites
	 * @param siteproperities
	 * @param siteIds
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/sites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> getQuoteConfigurationBySites(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,
			@RequestParam(required = false, name = "siteproperities") Boolean siteproperities,
			@RequestBody List<Integer> siteIds) throws TclCommonException {
		QuoteBean response = izoSdwanQuoteService.getQuoteDetails(quoteId, feasibleSites, siteproperities, null,
				siteIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_FEASIBILITY)
	@RequestMapping(value = "/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteLeAttributeBean> feasibilityCheck(@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuoteLeAttributeBean response = izoSdwanQuoteService.checkQuoteLeFeasibility(quoteToLeId);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * 
	 * getAllAttributesByQuoteToLeId this method is used to get all details by the
	 * quoteToLeId
	 * 
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_ALL_DETAILS_BY_QUOTETOLEID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LegalAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/{quoteId}/le/{quoteToLeId}/attributes")
	public ResponseResource<Set<LegalAttributeBean>> getAllAttributesByQuoteToLeId(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		Set<LegalAttributeBean> response = izoSdwanQuoteService.getAllAttributesByQuoteToLeId(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * persistListOfQuoteLeAttributes - This method is used to persist list of quote
	 * le attributes
	 * 
	 * @param request{quoteId,quoteToLeId,request}
	 * @return ResponseResource<QuoteDetail>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/{quoteId}/le/{quoteToLeId}/attributes")
	public ResponseResource<QuoteDetail> persistListOfQuoteLeAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		QuoteDetail response = izoSdwanQuoteService.persistListOfQuoteLeAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author AnandhiV
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited createDocument this method is
	 *            used to map the legal entity details and supplier entity details
	 *            and tax exception site
	 * @param request{quoteId,illsiteId,CustomerLegalEntityId,SupplierLegalEntityId}
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_DOCUMENT)
	@RequestMapping(value = "{quoteId}/le/{quoteLeId}/contractentity", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateDocumentDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CreateDocumentDto> createDocument(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody CreateDocumentDto request)
			throws TclCommonException {
		CreateDocumentDto documentDto = izoSdwanQuoteService.createDocument(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, documentDto,
				Status.SUCCESS);

	}

	/**
	 * 
	 * THis methos is used to update Term and Months against quote To Le
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param updateRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/terminmonths", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonths(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest updateRequest)
			throws TclCommonException {
		izoSdwanQuoteService.updateTermsInMonthsForQuoteToLe(quoteId, quoteToLeId, updateRequest, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * 
	 * Get pricing details by quote ID
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/pricing", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuotePricingDetailsBean> getPricingInfo(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		QuotePricingDetailsBean quotePricingDetailsBean = izoSdwanQuoteService.getPriceInformationForTheQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quotePricingDetailsBean,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Get pricing details by quote ID
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/pricing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SolutionPricingDetailsBean> getPricingInfoSitewise(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestBody PricingInformationRequestBean pricingInformationRequestBean) throws TclCommonException {
		SolutionPricingDetailsBean solutionPricingDetailsBean = izoSdwanQuoteService
				.getPriceSiteWise(pricingInformationRequestBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				solutionPricingDetailsBean, Status.SUCCESS);

	}

	/**
	 * 
	 * generateQuotePdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/quotepdf", method = RequestMethod.GET)
	public ResponseResource<String> generateQuotePdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = izoSdwanQuoteService.processQuotePdf(quoteId, response, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * 
	 * generateQuotePdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/test", method = RequestMethod.GET)
	public ResponseResource<IzosdwanQuotePdfBean> test(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		IzosdwanQuotePdfBean tempDownloadUrl = izoSdwanQuoteService.test(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * @author vpachava
	 * @param quoteId
	 * @param quoteToLeId
	 * @param inputCurrency
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CURRENCY_CONVERSION)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/currencyconvert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateCurrency(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("currency") String inputCurrency)
			throws TclCommonException {
		izoSdwanQuoteService.updateCurrency(quoteId, quoteToLeId, inputCurrency);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * @author mpalanis This method is used to share the quote PDF file via email.
	 * @param quoteId
	 * @param quoteToLeId
	 * @param email
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/{quoteId}/le/{quoteLeId}/quote/share")
	public ResponseResource<ServiceResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("email") String email)
			throws TclCommonException {
		ServiceResponse response = izoSdwanQuoteService.processMailAttachment(email, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author mpalanis This method is used to get the unique cpe list for the
	 *         quote.
	 * @param quoteId
	 * @param quoteToLeId
	 * @param email
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/cofuniquecpelist", method = RequestMethod.GET)
	public ResponseResource<List<String>> getDistinctCpeDetails(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		List<String> cpeNames = izoSdwanQuoteService.getDistinctCpes(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, cpeNames,
				Status.SUCCESS);
	}

	/**
	 * @author vpachava
	 * @param quoteId
	 * @param quoteToLeId
	 * @param inputCurrency
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CURRENCY_CONVERSION)
	@RequestMapping(value = "/profile/selection", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<VendorProfileDetailsBean>> profileSelectionDetails(
			@RequestBody ProfileSelectionInputDetails inputDetails) throws TclCommonException {
		List<VendorProfileDetailsBean> response = izoSdwanQuoteService.getProfileSelectionDetails(inputDetails);
		return new ResponseResource<List<VendorProfileDetailsBean>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);

	}

	/**
	 * @author vpachava
	 * @param file
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPLOAD_IZOSDWAN_BYON_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/template/upload", method = RequestMethod.POST)
	public ResponseResource<ByonUploadResponse> uploadIzoSdwanByonTemplate(@RequestParam("file") MultipartFile file,
			@RequestParam("quoteId") Integer quoteId)
			throws TclCommonException, IOException, EncryptedDocumentException, InvalidFormatException {
		ByonUploadResponse byonresponse = izoSdwanQuoteService.uploadIzosdwanByonExcel(file, quoteId);
		return new ResponseResource<ByonUploadResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				byonresponse, Status.SUCCESS);
	}

	/**
	 * This Api is used to get both template download and error data or success data
	 * we can download
	 * 
	 * @author vpachava
	 * @param locationTemplate
	 * @param quoteId
	 * @param isTemplatedownload
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOWNLOAD_LOCATION_TEMPLATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulk/download/izosdwan", method = RequestMethod.POST)
	public ResponseResource<String> downloadIzoSdwanByonTemplate(@RequestBody LocationTemplateRequest locationTemplate,
			@RequestParam("quoteId") Integer quoteId, @RequestParam("isTemplatedownload") Boolean isTemplatedownload,
			HttpServletResponse response) throws TclCommonException, IOException {
		izoSdwanQuoteService.downloadIzoSdwanByonTemplate(locationTemplate, response, isTemplatedownload, quoteId);
		return null;
	}

	/**
	 * 
	 * Persist byon details in sites
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.BYON_PERSIST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/byon", method = RequestMethod.GET)
	public ResponseResource<String> persistByonDetailsInSites(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		String response = izoSdwanQuoteService.uploadByonSitesAgainstTheQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author mpalanis
	 * @param quoteId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CGW_BANDWIDTH)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cgw/{quoteId}", method = RequestMethod.POST)
	public ResponseResource<String> getDefaultBandwidthForCgw(@PathVariable("quoteId") Integer quoteId,
			@RequestBody(required = false) List<QuoteIzoSdwanAttributeValue> attributevalue) throws TclCommonException {
		String response = izoSdwanQuoteService.getDefaultBandwidthForCgw(quoteId, attributevalue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Get all attributes present for the quote
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_IZOSDWAN_QUOTE_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/attributes", method = RequestMethod.GET)
	public ResponseResource<List<IzosdwanQuoteAttributesBean>> getQuoteAttributes(
			@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		List<IzosdwanQuoteAttributesBean> response = izoSdwanQuoteService.getAllAttributesByQuoteId(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Get BYON location validation update status
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.BYONUPLOADVALIDATIONDONE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/validate/check", method = RequestMethod.GET)
	public ResponseResource<Boolean> getLocationValidationStatusForQuote(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		Boolean response = izoSdwanQuoteService.getByonLocationValidationStatus(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Check whether BYON data are updated or not in case of 100 % BYON
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.BYON100PUPLOADCHECK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/byon/check", method = RequestMethod.GET)
	public ResponseResource<Boolean> checkUploadDoneOrNot(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		Boolean response = izoSdwanQuoteService.checkByonDetailsUploadedOrNot(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * This API is used to get solution level charges for IZO-SDWAN
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.IZOSDWANSOLUTIONLEVELCHARGES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/price/solution/{quoteId}", method = RequestMethod.GET)
	public ResponseResource<List<IzosdwanSolutionLevelCharges>> getSolutionLevelChargesForIzosdwan(
			@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		List<IzosdwanSolutionLevelCharges> response = izoSdwanQuoteService.getSolutionLevelChargesForIzosdwan(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * This API is used to get solution level charges(breakup) for IZO-SDWAN
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.IZOSDWANSOLUTIONLEVELCHARGES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/price/solution/cgw/{quoteId}", method = RequestMethod.GET)
	public ResponseResource<List<SolutionLevelPricingBreakupDetailsBean>> getSolutionLevelBreakUpChargesForCGW(
			@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		List<SolutionLevelPricingBreakupDetailsBean> response = izoSdwanQuoteService
				.getSolutionLevelBreakUpPricingDetailForCGW(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Add or modify Quote attributes
	 * 
	 * @param izosdwanQuoteAttributesUpdateBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.ADDORMODIFYQUOTEATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/quote/attributes", method = RequestMethod.POST)
	public ResponseResource<String> addOrModifyQuoteAttributes(
			@RequestBody IzosdwanQuoteAttributesUpdateBean izosdwanQuoteAttributesUpdateBean)
			throws TclCommonException {
		String response = izoSdwanQuoteService.addOrModifyQuoteAttribute(izosdwanQuoteAttributesUpdateBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author AnandhiV
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/attribute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateLegalEntityProperties(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		QuoteDetail response = izoSdwanQuoteService.updateLegalEntityProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * Add vProxy solutions to IZOSDWAN quote
	 * 
	 * @author AnandhiV
	 * @param vproxySolutionRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_VPROXY_SOLUTIONS)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/vproxy/solutions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> addVproxySolutions(@RequestBody VproxySolutionRequestBean vproxySolutionRequestBean)
			throws TclCommonException {
		String response = izoSdwanQuoteService.addVproxySolutions(vproxySolutionRequestBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * This Method is used to get all the vproxy profile details
	 * 
	 * @author vpachava
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_VPROXY_SOLUTION_DETAILS)
	@RequestMapping(value = "/vproxy/profile/details", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<VproxySolutionsBean>> getVproxyProfileDetails(@RequestParam("quoteId") Integer quoteId)
			throws TclCommonException {
		List<VproxySolutionsBean> response = izoSdwanQuoteService.getVproxyProfileDetails(quoteId);
		return new ResponseResource<List<VproxySolutionsBean>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);

	}

	/**
	 * This Method is used to persist vproxy pricing information
	 * 
	 * @author vpachava
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.PERSIST_VPROXY_PRICING_INFO)
	@RequestMapping(value = "/vproxy/solution/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> persistVproxySolutionDetails(@RequestParam("quoteId") Integer quoteId)
			throws TclCommonException {
		String response = izoSdwanQuoteService.persistVproxyPricingDetails(quoteId);
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * This Method is used to get all the vproxy profile details
	 * 
	 * @author vpachava
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_VPROXY_SOLUTION_DETAILS)
	@RequestMapping(value = "/vproxy/pricing/details", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<VproxySolutionLevelCharges>> getVproxySolutionLevelPricing(
			@RequestParam("quoteId") Integer quoteId) throws TclCommonException {
		List<VproxySolutionLevelCharges> response = izoSdwanQuoteService.getVproxySolutionLevelCharges(quoteId);
		return new ResponseResource<List<VproxySolutionLevelCharges>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/test/pricing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Map<String, Object>> testPricing(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId) throws TclCommonException {
		Map<String, Object> response = izosdwanIllPricingAndFeasiblityService
				.processPricingForCpeOnlyChangeSite(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * Update quote stage
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteToLeId
	 * @param stageName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_STAGE)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> modifyIzosdwanStage(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId,
			@RequestParam(value = "stageName", required = true) String stageName) throws TclCommonException {
		String response = izoSdwanQuoteService.updateQuoteStage(quoteId, quoteToLeId, stageName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * This is used to get cpe pricing
	 * 
	 * @author mpalanis
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteToLeId}/cpe/sdwan/pricing", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> triggerCpePricing(@PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		Boolean response = izosdwanPricingService.triggerCpeCharges(quoteToLeId,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * This is used to trigger DCF for Quote
	 * 
	 * @author mpalanis
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/pricing/dcf", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> triggerDcf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		Boolean response = izosdwanPricingService.triggerDcfForIzosdwanQuote(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/trigger/pricing/service/{isByonOnly}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processPricingServiceEntriesForTheQuote(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId, @PathVariable("isByonOnly") Boolean isByonOnly)
			throws TclCommonException {
		izosdwanPricingService.putEntryInPricingBatch(quoteId, quoteToLeId, isByonOnly,false,false,CommonConstants.STANDARD);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);

	}

	/**
	 * 
	 * checkTheCompletionOfPricingBatch
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/pricing/check", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> checkTheCompletionOfPricingBatch(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		Boolean response = izoSdwanQuoteService.getPricingServiceCompletionStatusByQuote(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getServiceListByQuote
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/pricing/services", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<IzosdwanPricingServiceBean>> getServiceListByQuote(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		List<IzosdwanPricingServiceBean> response = izoSdwanQuoteService.getPricingServiceListForTheQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * This is used to get cpe pricing
	 * 
	 * @author mpalanis
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteToLeId}/cpe/ias/pricing", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> triggerCpePricingForIasMacd(@PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		Boolean response = izosdwanIllPricingAndFeasiblityService.processPricingForCPEChangeRecords(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * This is used to get cpe pricing
	 * 
	 * @author mpalanis
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteToLeId}/cpe/gvpn/pricing", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> triggerCpePricingGvpnMacd(@PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		Boolean response = izosdwanGvpnPricingAndFeasibilityService.processPricingForCPEChangeRecords(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getServiceListByQuote
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/recalculate", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> recalculate(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		izoSdwanQuoteService.recalculateQuotePrices(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null, Status.SUCCESS);

	}

	/**
	 *
	 * generateQuotePdf
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(value = "nat", required = false) Boolean nat) throws TclCommonException {
		izosdwanQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * 
	 * Trigger CGW Pricing
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/cgw/pricing", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> triggerCgwPricing(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		Boolean response = izosdwanPricingService.triggerCgwRequest(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/ias/pricing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Object> testIad(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody Result result) throws TclCommonException {
		Object response = izosdwanIllPricingAndFeasiblityService.testIas(result, quoteToLeId, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	// MOVE TO ORDER CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {

			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> approvedQuotes(@RequestBody UpdateRequest request,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		QuoteDetail response = izoSdwanQuoteService.approvedQuotes(request, forwardedIp);
		if (o2cEnableFlag.equalsIgnoreCase("true")) {
			LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
			// illQuoteService.processOrderFlatTable(response.getOrderId());
		} else {
			LOGGER.info("Order flat table is disabled");
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/manual/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> approvedManualQuotes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletRequest httpServletRequest)
			throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		QuoteDetail response = izoSdwanQuoteService.approvedManualQuotes(quoteId, forwardedIp);
		if (o2cEnableFlag.equalsIgnoreCase("true")) {
			LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
			// illQuoteService.processOrderFlatTable(response.getOrderId());
		} else {
			LOGGER.info("Order flat table is disabled");
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_QUOTE_IZOSDWAN)
	@RequestMapping(value = "/quotes/{quoteId}/sdwan/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deleteQuoteSdwan(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		izoSdwanQuoteService.deleteQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_QUOTE_IZOSDWAN)
	@RequestMapping(value = "/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deleteAllQuoteSdwan() throws TclCommonException {
		izoSdwanQuoteService.deleteAllQuote();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_DOCUMENTS_QUOTE_LE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/trigger/manualfeasibility/quoteToLe/{quoteToLe}/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<MfDetailsBean> getMfDetailsRequest(@PathVariable("quoteToLe") Integer quoteToLe,
			@RequestBody List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean) throws TclCommonException {
		MfDetailsBean response = izosdwanPricingService.getManualFeasibilityRequestData(manualFeasibilitySiteBean, quoteToLe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_DOCUMENTS_QUOTE_LE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/trigger/manualfeasibility/quoteToLe/{quoteToLe}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processManualFeasibilityFlow(@PathVariable("quoteToLe") Integer quoteToLe,
			@RequestBody List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean) throws TclCommonException {
		izosdwanPricingService.processManualFeasibilityRequest(manualFeasibilitySiteBean, quoteToLe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OPPORTUNITY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/details/opportunity/{quoteId}/{siteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getMfSupportGroup(@PathVariable("quoteId") Integer quoteId, @PathVariable("siteId") Integer siteId) throws TclCommonException {
		OpportunityBean response = izoSdwanQuoteService.getOpportunityDetails(quoteId,siteId);
		response = izoSdwanQuoteService.retrievePriSecSIDsForMFOppurtunity(response,quoteId,siteId);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/lcon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonths(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody List<LconUpdateBean> lconUpdateBeans)
			throws TclCommonException {
		izoSdwanQuoteService.updateLconProperities(quoteId, quoteToLeId, lconUpdateBeans);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	/**
	 * This API download the file from table
	 * 
	 * @param file
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_DOCUMENTS_QUOTE_LE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/solution/document/quoteId/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<SiteDocumentBean>> getSiteDocumentsForQuoteLe(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException, IOException {
		List<SiteDocumentBean> siteDocNames = izoSdwanQuoteService.getSiteDocumentDetails(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, siteDocNames,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPLOAD_SOLUTION_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/solution/upload/siteId/{siteId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> upLoadSiteDocument(@RequestParam("file") MultipartFile file,
													   @PathVariable(value = "siteCode", required = false) String siteCode,
													   @PathVariable("siteId") Integer siteId) throws TclCommonException {
		String response = izoSdwanQuoteService.uploadDocument(siteId, file);
		if(response.equals(CommonConstants.FAILIURE)) {
			return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, response,
					Status.SUCCESS);
		} else {
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}
	}
	
	/**
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param nat
	 * @param name
	 * @param emailId
	 * @param approver
	 * @param httpServletRequest
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/docusign", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processDocusign(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("nat") Boolean nat,
			@RequestParam("name") String name, @RequestParam("email") String emailId, @RequestBody ApproverListBean approver,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		izosdwanQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approver);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	/**
	 * 
	 * This is used to trigger vproxy cost api for Quote
	 * 
	 * @author mpalanis
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/test/vproxy/cost", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> triggerVproxyCostPricing(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		Boolean response = izosdwanPricingService.triggerVproxyCostApiForIzosdwanQuote(quoteId,quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * This is used to trigger vproxy price api for Quote
	 * 
	 * @author mpalanis
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/test/vproxy/price", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> triggerVproxyPricing(@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		Boolean response = izosdwanPricingService.triggerVproxyPriceApiForIzosdwanQuote(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * triggerEmail Trigger email for the delegated user after posting a record in
	 * the quote delegation table with status as open
	 * 
	 * 
	 * @param userId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_EMAIL)
	@RequestMapping(value = "{quoteId}/le/{quoteLeId}/delegate/notification", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<TriggerEmailResponse> triggerEmail(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody TriggerEmailRequest triggerEmailRequest,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		TriggerEmailResponse triggerEmailResponse = izoSdwanQuoteService.processTriggerMail(triggerEmailRequest,
				forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, triggerEmailResponse,
				Status.SUCCESS);

	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_VPROXY_SOLUTIONS)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/vutm/solutions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> addVutmSolutions(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId,@RequestBody VutmProfileDetailsBean vutmProfileDetailsBean)
			throws TclCommonException {
		String response = izoSdwanQuoteService.addVutmSolution(vutmProfileDetailsBean,quoteId,quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * Get eligible locations for vutm
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteLeId
	 * @param location
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_VPROXY_SOLUTIONS)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/vutm/locations", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<SEASiteDetailsBean>> getVutmLocations(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId,@RequestParam(required = false, value = "location") String location)
			throws TclCommonException {
		List<SEASiteDetailsBean> response = izoSdwanQuoteService.getVutmLocationMappingDetails(quoteId, quoteLeId, location);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * Add Or Modify VutmLocationMappings
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteLeId
	 * @param seaSiteDetailsBeans
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_VPROXY_SOLUTIONS)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/vutm/locations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> addOrModifyVutmLocationMappings(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId,@RequestBody SEASiteUpdateRequest seaSiteUpdateRequest)
			throws TclCommonException {
		String response = izoSdwanQuoteService.updateUvtmSiteMappingDetails(quoteId, quoteLeId, seaSiteUpdateRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * Get eligible locations for vutm
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteLeId
	 * @param location
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_VPROXY_SOLUTIONS)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/vutm/locations/details", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SEASiteInfoBean> getVutmLocationsDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId,@RequestParam(required = false, value = "location") String location)
			throws TclCommonException {
		SEASiteInfoBean response = izoSdwanQuoteService.getSiteInformationForVutm(quoteId, quoteLeId, location);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cofcpedetails/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource< Map<String,List<IzoSdwanCpeBomInterface>>> contsructCpeSet(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		Map<String,List<IzoSdwanCpeBomInterface>> response = izoSdwanQuoteService.contsructCpeSet(quoteId);
		return new ResponseResource <>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_VPROXY_SOLUTIONS)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/test/vproxy/price", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getVutmLocationsDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId,@RequestBody VproxyPricingInputDatum vproxyPricingInputDatum)
			throws TclCommonException {
		izosdwanPricingService.testFunction(quoteId, quoteLeId, vproxyPricingInputDatum);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,  ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}
	
	
	/**
	 * @author Madhumiethaa Palanisamy
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/updateO2cComponents", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateO2cComponents(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		izoSdwanQuoteService.updateO2cComponents(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,  ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONTACT_ATTRIBUTE)
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/contactattributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = ContactAttributeInfo.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<ContactAttributeInfo> getContactAttributeDetails(@PathVariable("quoteId") Integer quoteId,
            @PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {

 

        ContactAttributeInfo response = izoSdwanQuoteService.getContactAttributeDetails(quoteLeId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

 

    }
	

	/**
	 * @author mpalanis
	 * @param customerLeId
	 * @param file
	 * @param referenceName
	 * @param attachmentType
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/SSupload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
	      @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	      @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	      @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> upLoadFileSS(@RequestParam(name="file") MultipartFile file,
	                                       @RequestParam("referenceName") String referenceName,
	                                       @RequestParam("attachmentType") String attachmentType) throws TclCommonException {
	   ServiceResponse response = izoSdwanQuoteService.processUploadFileSS(file,referenceName,attachmentType);
	   return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	         Status.SUCCESS);

	}
	
	
	/**
	 * @author mpalanis
	 * @param key
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/SSdownload", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getAttachmentSS(@RequestParam(required = false,name= "qc") String key,
			@RequestParam(required = false,name= "quoteId") Integer id) throws TclCommonException {
		String url = izoSdwanQuoteService.processSSDownloadFile(key,id);
		 return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, url,
		         Status.SUCCESS);
	}
	
	/**
	 * @author mpalanis This method is used to get the active quotes based on customer
	 *         quote.
	 * @param customerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{customerId}/le/{customerLeId}/getActive", method = RequestMethod.GET)
	public ResponseResource<List<ActiveQuoteAndOrder>> getActiveQuoteDetails(@PathVariable("customerId") Integer customerId, @PathVariable("customerLeId") Integer customerLeId)
			throws TclCommonException {
		List<ActiveQuoteAndOrder> response = izoSdwanQuoteService.getActiveQuoteAndOrderDetails(customerId,customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@RequestMapping(value = "/le/{quoteLeId}/checkProcessUpdateProduct", method = RequestMethod.GET)
	public ResponseResource<Boolean> checkProcessUpdateProduct(@PathVariable("quoteLeId") Integer quoteLeId)
			throws TclCommonException {
		 izoSdwanQuoteService.checkProcessUpdateProduct(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Boolean.TRUE,
				Status.SUCCESS);
	}
	
}

