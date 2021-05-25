package com.tcl.dias.oms.isv.controller.v1;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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

import com.tcl.dias.common.beans.IzoSdwanCpeDetails;
import com.tcl.dias.common.beans.IzoSdwanSiteDetails;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesBean;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesUpdateBean;
import com.tcl.dias.common.beans.LocationInputDetails;
import com.tcl.dias.common.beans.ProfileSelectionInputDetails;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ByonUploadResponse;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.LocationTemplateRequest;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuoteIzoSdwanAttributeValue;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuotePriceAuditResponse;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPriceApiRequest;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPricingServiceBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanQuotePdfBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanSolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.NetworkSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.PriceUpdateBean;
import com.tcl.dias.oms.izosdwan.beans.PricingInformationRequestBean;
import com.tcl.dias.oms.izosdwan.beans.PricingUpdateRequest;
import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeDetails;
import com.tcl.dias.oms.izosdwan.beans.SolutionLevelPricingBreakupDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SolutionPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.VproxyChargableComponents;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionRequestBean;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanIllPricingAndFeasiblityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuotePdfService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * This file contains the InternalStakeViewIzosdwanController.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/isv/v1/izosdwan")
public class InternalStakeViewIzosdwanController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanQuoteService.class);
	@Autowired
	IzosdwanQuoteService izoSdwanQuoteService;
	
	@Autowired
	IzosdwanIllPricingAndFeasiblityService izosdwanIllPricingAndFeasiblityService;
	
	@Autowired
	IzosdwanPricingAndFeasibilityService izosdwanPricingAndFeasibilityService;
	
	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;
	
	@Autowired
	IzosdwanQuotePdfService izosdwanQuotePdfService;
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<QuoteResponse> createQuote(@RequestBody QuoteDetail request,
			@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		LOGGER.info("Info Is{}",request);
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
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteDetail request)
			throws TclCommonException {
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
            @RequestParam(required = false, name = "siteid") Integer siteId)
            throws TclCommonException {
        QuoteBean response = izoSdwanQuoteService.getQuoteDetails(quoteId, feasibleSites, siteproperities, siteId,null);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    

    
 
    /**
     * 
     * @author vpachava
     *   @link http://www.tatacommunications.com/ getCpeDetails- This method is used
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
	public ResponseResource<List<IzoSdwanCpeDetails>> getCpeDetails(@RequestParam("vendorName")String vendorName,@RequestParam(required = false,value="addons")String addons,@RequestParam("profileName") String profileName)
			throws TclCommonException {
		List<IzoSdwanCpeDetails> response = izoSdwanQuoteService.getCpeDetails(vendorName,addons,profileName);
		return new ResponseResource<List<IzoSdwanCpeDetails>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	   
	/**
	 *   @author vpachava
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
	public ResponseResource<List<IzoSdwanSiteDetails>> getCpeSiteDetails()
			throws TclCommonException {
		List<IzoSdwanSiteDetails> response = izoSdwanQuoteService.getSiteDetails();
		return new ResponseResource<List<IzoSdwanSiteDetails>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
	/**
	 *   @author vpachava
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

		return new ResponseResource<NetworkSummaryDetails>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * @author mpalanis
	 * @link http://www.tatacommunications.com/ getCofSiteDetails- This method is used
	 *       to get all site type names with total no.of.sites each
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

		return new ResponseResource<List<SiteTypeDetails>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
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
	public ResponseResource<List<ViewSitesSummaryBean>> getViewSitesSummaryDetails(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(value="type",required = false) String type,@RequestBody(required = false)  LocationInputDetails request) throws TclCommonException {

		List<ViewSitesSummaryBean> response = izoSdwanQuoteService.getSitesBasedOnSiteType(quoteId, type,request);

		return new ResponseResource<List<ViewSitesSummaryBean>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE_PROPERTIES)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/propertiesattribute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateSitePropertiesAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId,
			@RequestBody List<UpdateRequest> request) throws TclCommonException {
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
            @RequestBody List<Integer> siteIds)
            throws TclCommonException {
        QuoteBean response = izoSdwanQuoteService.getQuoteDetails(quoteId, feasibleSites, siteproperities, null,siteIds);
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
	 * getAllAttributesByQuoteToLeId
	 *            this method is used to get all details by the quoteToLeId
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
	 *	persistListOfQuoteLeAttributes - This method is
	 *            used to persist list of quote le attributes
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
			@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuotePricingDetailsBean quotePricingDetailsBean = izoSdwanQuoteService.getPriceInformationForTheQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				quotePricingDetailsBean, Status.SUCCESS);
}
	
	/**
	 * 
	 * Get pricing details by quote ID
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
			@PathVariable("quoteLeId") Integer quoteToLeId,@RequestBody PricingInformationRequestBean pricingInformationRequestBean)
			throws TclCommonException {
		SolutionPricingDetailsBean solutionPricingDetailsBean = izoSdwanQuoteService.getPriceSiteWise(pricingInformationRequestBean);
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
    *@author mpalanis
    * This method is used to share the quote PDF file via email.
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
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	/**
	    *@author mpalanis
	    * This method is used to get the unique cpe list for the quote.
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
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeNames, Status.SUCCESS);
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
	public ResponseResource<ByonUploadResponse> uploadIzoSdwanByonTemplate(@RequestParam("file") MultipartFile file,@RequestParam("quoteId") Integer quoteId) throws TclCommonException, IOException, EncryptedDocumentException, InvalidFormatException {
		ByonUploadResponse byonresponse=izoSdwanQuoteService.uploadIzosdwanByonExcel(file,quoteId);
		return new ResponseResource<ByonUploadResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				byonresponse, Status.SUCCESS);
	}
	
	/**This Api is used to get  both template download and error data or success data we can download
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
	 *  Persist byon details in sites
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
	public ResponseResource<String> getDefaultBandwidthForCgw(
			@PathVariable("quoteId") Integer quoteId, @RequestBody(required = false) List<QuoteIzoSdwanAttributeValue> attributevalue)
			throws TclCommonException {
		String response  = izoSdwanQuoteService.getDefaultBandwidthForCgw(quoteId,
				attributevalue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * Get all attributes present for the quote
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
	public ResponseResource<Boolean> getLocationValidationStatusForQuote(
			@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		Boolean response = izoSdwanQuoteService.getByonLocationValidationStatus(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * Check whether BYON data are updated or not in case of 100 % BYON
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
	public ResponseResource<Boolean> checkUploadDoneOrNot(
			@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		Boolean response = izoSdwanQuoteService.checkByonDetailsUploadedOrNot(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is used to get solution level charges for IZO-SDWAN
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
		List<SolutionLevelPricingBreakupDetailsBean> response = izoSdwanQuoteService.getSolutionLevelBreakUpPricingDetailForCGW(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * Add or modify Quote attributes
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
	public ResponseResource<String> addOrModifyQuoteAttributes(@RequestBody IzosdwanQuoteAttributesUpdateBean izosdwanQuoteAttributesUpdateBean) throws TclCommonException {
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
		List<VproxySolutionsBean> response= izoSdwanQuoteService.getVproxyProfileDetails(quoteId);
		return new ResponseResource<List<VproxySolutionsBean>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

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
	public ResponseResource<List<VproxySolutionLevelCharges>> getVproxySolutionLevelPricing(@RequestParam("quoteId") Integer quoteId)
			throws TclCommonException {
		List<VproxySolutionLevelCharges> response= izoSdwanQuoteService.getVproxySolutionLevelCharges(quoteId);
		return new ResponseResource<List<VproxySolutionLevelCharges>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/test/pricing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Map<String,Object>> testPricing(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId)
			throws TclCommonException {
		Map<String,Object> response = izosdwanIllPricingAndFeasiblityService.processPricingForCpeOnlyChangeSite(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/sites/{siteId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteIllSitesWithFeasiblityAndPricingBean> getFeasiblityAndPricingDetailsForQuoteIllSites(
			@PathVariable("siteId") Integer siteId) throws TclCommonException {
		QuoteIllSitesWithFeasiblityAndPricingBean response = izoSdwanQuoteService
				.getFeasiblityAndPricingDetailsForQuoteIllSites(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is used to update price manually for SDWAN quote
	 * @param quoteId
	 * @param quoteLeId
	 * @param pricingUpdateRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.POST_TO_UPDATE_PRICE_MANUALLY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/price/manual", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> updateManualPriceForIzosdwan(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody PricingUpdateRequest pricingUpdateRequest)
			throws TclCommonException {
		Boolean response = izosdwanPricingAndFeasibilityService.updateManualPrice(pricingUpdateRequest, quoteId,
				quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * getQuotePriceAudit - api to get all the quote price audit details based on
	 * the given quote id input
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE_PRICE_AUDIT)
	@RequestMapping(value = "/quotes/{quoteId}/priceaudit", method = RequestMethod.GET)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuotePriceAuditResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuotePriceAuditResponse> getQuotePriceAudit(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		QuotePriceAuditResponse response = izosdwanPricingAndFeasibilityService.getQuotePriceAudit(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	 /**
	 * getServiceListByQuote
	 * @author AnandhiV
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteToLeId}/pricing/services", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<List<IzosdwanPricingServiceBean>> getServiceListByQuote(@PathVariable("quoteId") Integer quoteId,@PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		List<IzosdwanPricingServiceBean> response = izoSdwanQuoteService.getPricingServiceListForTheQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manual/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
			//illQuoteService.processOrderFlatTable(response.getOrderId());
		}else {
			LOGGER.info("Order flat table is disabled");
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * uploadCofPdfUrl
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload/url", method = RequestMethod.GET)
	public ResponseResource<TempUploadUrlInfo> uploadCofPdfUrl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = izosdwanQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUploadUrlInfo,
				Status.SUCCESS);
	}
	
	/* 
	 * updateCofUploadedDetails - api to update the manual cof uploaded details
	 * after the document is stored in the storage container
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_COF_UPLOADED_DETAILS)
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manualcofuploaded", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OmsAttachmentBean> updateCofUploadedDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("requestId") String requestId, @RequestParam("url") String url )
			throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = izosdwanQuotePdfService.updateCofUploadedDetails(quoteId, quoteLeId, requestId, url);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, omsAttachmentBean,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * downloadCofDetails - api to download the cof document that was uploaded to
	 * the storage container
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOWNLOAD_COF_STORAGE)
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manualcof/storage/downloadurl", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> DownloadCofFromStorageContainer(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		String tempDownloadUrl = izosdwanQuotePdfService.downloadCofFromStorageContainer(quoteId, quoteLeId, null,null,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * downloadCofPdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manualcof/download", method = RequestMethod.GET)
	public ResponseResource<String> downloadCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		izosdwanQuotePdfService.downloadCofPdf(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	/**
	 * 
	 * Update price for VPROXY
	 * @param quoteId
	 * @param quoteLeId
	 * @param vproxyChargableComponents
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.POST_TO_UPDATE_PRICE_MANUALLY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/price/manual/vproxy", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateManualPriceForVproxy(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody List<VproxyChargableComponents> vproxyChargableComponents)
			throws TclCommonException {
		izosdwanPricingAndFeasibilityService.updateVproxyPrice(vproxyChargableComponents, quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_COF_UPLOADED_DETAILS)
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manualcofuploaded/file", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OmsAttachmentBean> updateCofUploadedDetailsFile(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("requestId") String requestId, @RequestParam("url") String url,
			@RequestParam(required = false, value ="referenceName") String referenceName )
			throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = izosdwanQuotePdfService.updateCofUploadedDetailsFile(quoteId, quoteLeId, requestId, url,referenceName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, omsAttachmentBean,
				Status.SUCCESS);

	}

}
