package com.tcl.dias.oms.gvpn.controller.v1;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.*;
import com.tcl.dias.oms.gst.service.GstInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.GvpnSiteDetails;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.BillingRequest;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.ManualFeasibilitySiteBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteTncBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.dto.QuoteDelegationDto;
import com.tcl.dias.oms.dto.UserDto;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnMultiVrfCofAnnexureBean;
import com.tcl.dias.oms.gvpn.pdf.beans.VrfBean;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IllQuoteController.java class. This class contains all
 * the API's related to Quotes for ILL product
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/v1/gvpn/quotes")
public class GvpnQuoteController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GvpnQuoteController.class);

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	GstInService gstInService;

	@Autowired
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;

	@Autowired
	GvpnQuotePdfService gvpnQuotePdfService;
	
	@Autowired
	IllQuoteService illQuoteService;
	
	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;
	
	


	/**
	 * createQuote this method is used to generate quotes
	 * 
	 * @param request{quoteId}
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteResponse> createQuote(@RequestBody QuoteDetail request,
			@RequestParam(required = false, value = "customerId") Integer customerId,@RequestParam(required = false,value="ns") Boolean ns) throws TclCommonException {
		QuoteResponse response = gvpnQuoteService.createQuote(request, customerId,ns);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * getTnc - get special tnc
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_TNC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="/{quoteId}/le/{quoteLeId}/tnc",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<QuoteTncBean> getTnc(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		QuoteTncBean response = illQuoteService.getTnc(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * updateTnc
	 * @param quoteId
	 * @param quoteLeId
	 * @param quoteTncBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_TNC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="/{quoteId}/le/{quoteLeId}/tnc",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateTnc(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteTncBean quoteTncBean) throws TclCommonException {
		String response = illQuoteService.processTnc(quoteId, quoteTncBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * updateSiteInformation this is used to update the sites information
	 * 
	 * @param request{quoteId}
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<QuoteBean> updateSiteInformation(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteDetail request,
			@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		QuoteBean response = gvpnQuoteService.updateSite(request, customerId, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K
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
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,@RequestParam(required = false, name = "siteproperities") Boolean siteproperities) throws TclCommonException {
		QuoteBean response = gvpnQuoteService.getQuoteDetails(quoteId, feasibleSites,siteproperities, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K
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
	@Transactional
	public ResponseResource<CreateDocumentDto> createDocument(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody CreateDocumentDto request)
			throws TclCommonException {
		CreateDocumentDto documentDto = gvpnQuoteService.createDocumentWrapper(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, documentDto,
				Status.SUCCESS);

	}

	/**
	 * deleteIllsites this method is used to delete or disable the ill site info
	 * 
	 * @param quoteId,illsiteId,customerId,
	 *            action
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_OR_DISABLE_SITE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> deactivateSites(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId,
			@RequestParam(required = false, value = "customerId") Integer customerId,
			@PathVariable("siteId") Integer siteId, @RequestParam(required = true, value = "action") String action)
			throws TclCommonException {

		QuoteDetail response = gvpnQuoteService.processDeactivateSites(siteId, quoteId, action);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * getAllUserDetailsByCustomer this method is used get the user details based on
	 * the customer id
	 * 
	 * 
	 * @param customerId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	// TO BE MOVED to USER CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_ALL_USER_DETAILS)
	@RequestMapping(value = "/users", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<UserDto>> getAllUserDetailsByCustomer() throws TclCommonException {

		List<UserDto> response = gvpnQuoteService.getAllUsersByCustomer();
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
		TriggerEmailResponse triggerEmailResponse = gvpnQuoteService.processTriggerMail(triggerEmailRequest,
				forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, triggerEmailResponse,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Customer.GET_CUSTOMER_ID_TRIGGER_EMAIL)
	@RequestMapping(value = "{quoteId}/le/{quoteLeId}/multiplele", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<TriggerEmailResponse> getCustomerEmailIdTriggerEmail(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId)
			throws TclCommonException {
		TriggerEmailResponse response = gvpnQuoteService.getEmailIdAndTriggerEmail(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited * getSiteInfo this method is used
	 *            get the site info
	 * @param quoteId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_INFO_BY_ID)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteResponse> getSiteInfo(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId)
			throws TclCommonException {

		QuoteResponse quoteResponse = gvpnQuoteService.getSiteInfo(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quoteResponse,
				Status.SUCCESS);

	}

	/**
	 * getAllDelegatedUsers this method is used get the all the users with status as
	 * open from quote_delegation
	 * 
	 * 
	 * @param userId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	// MOVE TO USER CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_DELEGATED_USERS)
	@RequestMapping(value = "/delegatedusers", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDelegationDto> getDelegatedUser() throws TclCommonException {

		QuoteDelegationDto quoteDelegationDto = gvpnQuoteService.getQuoteDelegation();

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quoteDelegationDto,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getTaxExemptedSiteInfo method is
	 *            used get the all the taxexempted site
	 * @param quoteId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_TAX_EXCEMPTED_SITES)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}/exempttax", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteResponse> getTaxExemptedSiteInfo(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId)
			throws TclCommonException {

		QuoteResponse quoteResponse = gvpnQuoteService.getTaxExemptedSite(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quoteResponse,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getQuotesDetailsBasedOnCustomer
	 *            quote specific to customer
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	// MOVE to DASHBOARD CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTES_BASED_ON_CUSTOMER)
	@RequestMapping(value = "/details/{customerId}/customers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<QuoteBean>> getQuotesDetailsBasedOnCustomer(
			@PathVariable("customerId") @BaseArgument Integer customerId) throws TclCommonException {
		List<QuoteBean> response = gvpnQuoteService.getQuotesDetailsBasedOnCustomer(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 *            getQuotesDetailsBasedOnCustomerLegalEntity l
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	// MOVE TO DASHBOARD CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTES_BASED_ON_CUSTOMER)
	@RequestMapping(value = "/details/{customerLegalEntityId}/legalentities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<QuoteBean>> getQuotesDetailsBasedOnCustomerLegalEntity(
			@PathVariable("customerLegalEntityId") Integer customerLegalEntityId) throws TclCommonException {
		List<QuoteBean> response = gvpnQuoteService.getQuotesDetailsBasedOnCustomerLegalEntity(customerLegalEntityId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited editsites this method is used to
	 *            edit the ill site info
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.EDIT_SITES)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> editSites(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		QuoteDetail response = gvpnQuoteService.editSiteComponent(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvequotes this method is used
	 *            to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
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
		QuoteDetail response = gvpnQuoteService.approvedQuotes(request, forwardedIp);
		if (o2cEnableFlag.equalsIgnoreCase("true")) {
			LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
			//gvpnQuoteService.processOrderFlatTable(response.getOrderId());
		}else {
			LOGGER.info("Order flat table is disabled");
		}
		
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited dashboard this is used to display
	 *            the dashboard details based on quote
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	// MOVE to DASHBOARD CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DASH_BOARD_DETAILS)
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<DashBoardBean> getDashboardDetails(
			@RequestParam(required = false, value = "legalEntityId") Integer legalEntityId) throws TclCommonException {
		DashBoardBean response = gvpnQuoteService.getDashboardDetails(legalEntityId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateSiteProperties(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		QuoteDetail response = gvpnQuoteService.updateSiteProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE_PROPERTIES)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/{siteId}/propertiesattribute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateSitePropertiesAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		QuoteDetail response = gvpnQuoteService.updateSitePropertiesAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getQuotesDetailsBasedOnCustomer
	 *            quote specific to customer
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_PROPERTIES_ONLY)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/{siteId}/properties", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<QuoteProductComponentBean>> getSiteProperties(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId)
			throws TclCommonException {
		List<QuoteProductComponentBean> response = gvpnQuoteService.getSiteProperties(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K
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
		QuoteDetail response = gvpnQuoteService.updateLegalEntityProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getAllAttributesByQuoteToLeId
	 *            this method is used to get all details by the quoteToLeId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_ALL_DETAILS_BY_QUOTETOLEID)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/attributes", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LegalAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Set<LegalAttributeBean>> getAllAttributesByQuoteToLeId(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		Set<LegalAttributeBean> response = gvpnQuoteService.getAllAttributesByQuoteToLeId(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	// MOVE TO FEASIBILITY CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/trigger/feasiblities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@Transactional
	public ResponseResource<String> triggerForFeasibilityBean(@RequestBody FeasibilityBean request)
			throws TclCommonException {
		gvpnPricingFeasibilityService.processFeasibility(request.getLegalEntityId(),request.getProductName());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> persistListOfQuoteLeAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		QuoteDetail response = gvpnQuoteService.persistListOfQuoteLeAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateQuoteToLeStatus(@PathVariable("quoteLeId") Integer quoteToLeId,
			@PathVariable("quoteId") Integer quoteId, @RequestParam("stage") String status) throws TclCommonException {
		QuoteDetail response = gvpnQuoteService.updateQuoteToLeStatus(quoteToLeId, status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Check the feasibility and pricing detail of the given quote to legal entity
	 * id.
	 * 
	 * @author NAVEEN GUNASEKARAN
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource<QuoteToLeAttributeValueDto>
	 * @throws TclCommonException
	 */
	// MOVE TO FEASIBILITY CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_FEASIBILITY)
	@RequestMapping(value = "/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteLeAttributeBean> feasibilityCheck(@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuoteLeAttributeBean response = gvpnQuoteService.checkQuoteLeFeasibility(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateBillingInfoForSfdc this
	 *            method is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	// MOVE TO SDFC CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_BILLING_INFO_FOR_SFDC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sfdc/billinginfo/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> updateBillingInfoForSfdc(@RequestBody BillingRequest request)
			throws TclCommonException {
		QuoteBean response = new QuoteBean();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Check the feasibility and pricing detail of the given quote to legal entity
	 * id.
	 * 
	 * @author paulraj
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource<QuoteToLeAttributeValueDto>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/pricing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerPricing(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		gvpnPricingFeasibilityService.processPricingRequest(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * 
	 * updateSdfcStage
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param stage
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SFDC_UPDATE_STAGE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sfdc/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateSdfcStage(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("stage") String stage)
			throws TclCommonException {
		gvpnQuoteService.updateSfdcStage(quoteToLeId, stage);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * 
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getContactAttributeDetails
	 * @param quoteId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONTACT_ATTRIBUTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/contactattributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ContactAttributeInfo.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ContactAttributeInfo> getContactAttributeDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {

		ContactAttributeInfo response = gvpnQuoteService.getContactAttributeDetails(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CURRENCY_CONVERSION)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/currencyconvert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateCurrency(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("currency") String inputCurrency)
			throws TclCommonException {
		gvpnQuoteService.updateCurrency(quoteId, quoteToLeId, inputCurrency);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K getCustomerEmailIdTriggerEmail
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Customer.SUPPLIER_UNAVAILABLE)
	@RequestMapping(value = "{quoteId}/le/{quoteLeId}/suppplierunavailable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<TriggerEmailResponse> getSupplierUnavailableTriggerEmail(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId)
			throws TclCommonException {
		TriggerEmailResponse response = gvpnQuoteService.getSupplierUnavailableTriggerEmail(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @author VIVEK KUMAR K generateCofPdf
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

		gvpnQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K generateQuotePdf
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
		String tempDownloadUrl = gvpnQuotePdfService.processQuotePdf(quoteId, response, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/quote/share", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("email") String email)
			throws TclCommonException {
		ServiceResponse response = gvpnQuoteService.processMailAttachment(email, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * triggerAccountManagerCofDownloadNotification - This method triggers an email
	 * to the account manager when the customer downloads a cof
	 * 
	 * 
	 * @param orderId,
	 *            orderToLeId
	 * 
	 * @return String
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cofdownload/accountmanagernotification", method = RequestMethod.GET)
	public ResponseEntity<String> triggerAccountManagerCofDownloadNotification(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		gvpnQuoteService.cofDownloadAccountManagerNotification(quoteToLeId);
		return null;

	}

	/**
	 * 
	 * uploadCofPdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param nat
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/manualcof/upload", method = RequestMethod.POST)
	public ResponseResource<String> uploadCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(name="file") MultipartFile file) throws TclCommonException {
		gvpnQuotePdfService.uploadCofPdf(quoteId, file, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * 
	 * approvedManualQuotes
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param httpServletRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/manual/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> approvedManualQuotes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletRequest httpServletRequest)
			throws TclCommonException {
		QuoteDetail response = gvpnQuoteService.approvedManualQuotes(quoteId);
		if (o2cEnableFlag.equalsIgnoreCase("true")) {
			LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
			//gvpnQuoteService.processOrderFlatTable(response.getOrderId());
		}else {
			LOGGER.info("Order flat table is disabled");
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

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
		gvpnQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approver);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	/**
	 * 
	 * THis methos is used to update Term and Months against quote To Le
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
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest updateRequest) throws TclCommonException {
		gvpnQuoteService.updateTermsInMonthsForQuoteToLe(quoteId,quoteToLeId,updateRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	/**
	 * 
	 * getFeasiblityAndPricingDetailsForQuoteIllSites - this method returns the feasibility and pricing related details based on the given site id.
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sites/{siteId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteIllSitesWithFeasiblityAndPricingBean> getFeasiblityAndPricingDetailsForQuoteIllSites(
			@PathVariable("siteId") Integer siteId) throws TclCommonException {
		QuoteIllSitesWithFeasiblityAndPricingBean response = gvpnQuoteService
				.getFeasiblityAndPricingDetailsForQuoteIllSites(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE_PAGE_CURRENCY)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/quotecurrency", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getQuoteCurrency(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId)
			throws TclCommonException {
		String response = gvpnQuoteService.getQuoteCurrency(quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * THis method is used to update LCON details
	 * @param quoteId
	 * @param quoteToLeId
	 * @param updateRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/lcon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonths(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody List<LconUpdateBean> lconUpdateBeans) throws TclCommonException {
		gvpnQuoteService.updateLconProperities(quoteId,quoteToLeId,lconUpdateBeans);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/approvequotes/hardfix", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {

			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> approvedQuotesHardfix(@RequestBody UpdateRequest request,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		QuoteDetail response = gvpnQuoteService.approvedQuotesForHardFix(request, forwardedIp);
		if (o2cEnableFlag.equalsIgnoreCase("true")) {
			LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
			//gvpnQuoteService.processOrderFlatTable(response.getOrderId());
		}else {
			LOGGER.info("Order flat table is disabled");
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * This controller method is used to trigger the tax exemption notification mail
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/triggertaxexemptionmail/{customerLeId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> triggerTaxExemptionMail(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId,@PathVariable("customerLeId") Integer customerLeId)
			throws TclCommonException {
		String response = gvpnQuoteService.processTriggerTaxExemptionMailForUSSites(quoteId,quoteLeId,customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * updateCurrencyCodeInQuoteToLe is used to update currency code in quoteToLe
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_CURRENCY_IN_QUOTE_TO_LE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/currencycode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateCurrencyCodeInQuoteToLe(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId)
			throws TclCommonException {
		gvpnQuoteService.updateCurrencyCodeInQuoteToLe(quoteId,quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
				Status.SUCCESS);
	}
	/**
	 * Update partner location based on customer le id
	 *
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPADTE_PARTNER_ENTITY_LOCATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/update/{quoteLeId}/partner/le/location/{customerLeId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> updatePartnerLeLocation(@PathVariable("quoteLeId") Integer quoteLeId,@PathVariable("customerLeId") Integer customerLeId)
			throws TclCommonException {
		Boolean response = gvpnQuoteService.updatePartnerLeLocation(quoteLeId,customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * validateOrderForm
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.VALIDATE_ORDERFORM)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CommonValidationResponse> validateOrderForm(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		CommonValidationResponse response = gvpnQuoteService.processValidateMandatoryAttr(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * used to save and process the ask price
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param siteCode
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_ASK_PRICE)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/site/{siteCode}/askprice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> processAskPrice(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId, @PathVariable("siteCode") String siteCode,
			@RequestBody UpdateRequest request) throws TclCommonException {
		QuoteDetail response = gvpnPricingFeasibilityService.processAskPrice(siteCode, quoteToLeId, request, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * This api is used to trigger workflow from get quote.
	 * 
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_WORKFLOW)
	@RequestMapping(value = "quoteLe/{quoteLeId}/triggerworkflow", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> getDiscountDetails(@PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestBody List<GvpnSiteDetails> gvpnsites) throws TclCommonException {
		Boolean response = gvpnPricingFeasibilityService.triggerWorkFlow(quoteToLeId,gvpnsites);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
//	/**
//	 * This api is used to trigger workflow gvpn international  from get quote.
//	 * 
//	 * @param pdRequest
//	 * @return
//	 * @throws TclCommonException
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_WORKFLOW)
//	@RequestMapping(value = "quoteLe/{quoteLeId}/triggerworkflow/intl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	public ResponseResource<Boolean> triggerTaskInternational(@PathVariable("quoteLeId") Integer quoteToLeId,
//			@RequestBody List<String> siteCodes) throws TclCommonException {
//		Boolean response = gvpnPricingFeasibilityService.triggerWorkFlowInternational(quoteToLeId, siteCodes);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//
//	}


	/**
	 * This API is to trigger manual feasibility workflow
	 * @param file
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_WORKFLOW_FOR_MF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/trigger/manualfeasibility/quoteToLe/{quoteToLe}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processManualFeasibilityFlow(@PathVariable("quoteToLe") Integer quoteToLe,
			@RequestBody List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean) throws TclCommonException {
		gvpnPricingFeasibilityService.processManualFeasibilityRequest(manualFeasibilitySiteBean, quoteToLe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
	}

	
	/**
	 * Get oppurtunity details for given siteID and QuoteID
	 * @param quoteId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OPPORTUNITY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/details/opportunity/{quoteId}/{siteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getOppurtunityDetails(@PathVariable("quoteId") Integer quoteId, @PathVariable("siteId") Integer siteId) throws TclCommonException {
		OpportunityBean response = gvpnQuoteService.getOpportunityDetails(quoteId,siteId);
		response = gvpnQuoteService.retrievePriSecSIDsForMFOppurtunity(response,quoteId,siteId);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This api is to trigger feasibility workflow for feasible sites
	 * @param quoteToLe
	 * @param manualFeasibilitySiteBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_DOCUMENTS_QUOTE_LE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/trigger/feasibilityworkflow/quoteToLe/{quoteToLe}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processMFForFeasibleSites(@PathVariable("quoteToLe") Integer quoteToLe,
			@RequestBody List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean) throws TclCommonException {
		gvpnPricingFeasibilityService.processMFRequestForFeasibleSites(manualFeasibilitySiteBean, quoteToLe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
	}
	/**
	 * Api to get gst address for the given gst number
	 * @Author Chetan Chaudhary
	 * @param gstin
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GstAddress.GET_GST_ADDRESS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GstAddressBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/{siteId}/gstin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GstAddressBean> getGstData(@RequestParam("gstin") String gstin, @PathVariable("quoteId") Integer quoteId,
													   @PathVariable("quoteToLeId") Integer quoteToLeId, @PathVariable("siteId") Integer siteId) throws TclCommonException {
		GstAddressBean gstAddressBean = gstInService.getGstDataQuote(gstin, quoteId, quoteToLeId, siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstAddressBean, Status.SUCCESS);
	}
	
	
	/**
	 * 
	 * validateOrderForm
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.VALIDATE_ORDERFORM)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/cof/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CommonValidationResponse> processCofValidator(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		CommonValidationResponse response = gvpnQuoteService.processValidate(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	
	/**
	 * API to trigger feasibility by customer login
	 * @param request
	 * @param isCustomer
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/customer/trigger/feasiblities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@Transactional
	public ResponseResource<String> triggerFeasibilityBasedOnFlag(@RequestBody FeasibilityBean request, @RequestParam("isCustomer") String isCustomer)
			throws TclCommonException {
		gvpnPricingFeasibilityService.processCustomerTriggeredFeasibility(request.getLegalEntityId(),request.getProductName());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_LE_FOR_DEMO)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quoteToLe/{quoteToLe}/demo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> persistDemoOrder(@PathVariable("quoteToLe") Integer quoteToLe,
													 @RequestBody DemoOrderInfo info) throws TclCommonException {
		illQuoteService.persistDemoOrderInfo(quoteToLe,info);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
	}
	
	
	/**
	 * updateVrfSiteInformation this is used to update the VRF information
	 * 
	 * @param request{quoteId}
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/vrf/sites/{siteId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateVrfInformation(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId,@PathVariable("siteId") Integer siteId, @RequestBody UpdateRequest request,
			@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		gvpnQuoteService.updateVrfSiteInfo(request, customerId, quoteId,siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,Constants.SUCCESS,
				Status.SUCCESS);
	}
	
	/**
	 * Api to get vrf data for the given siteId
	 * @param siteId
	 * @param productName
	 * @param quoteId
	 * @return responseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_VRF_DATA)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sites/{siteId}/productName/{productName}/quoteId/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<QuoteProductComponentBean>> getVrfData(@PathVariable("siteId") Integer siteId,
		@PathVariable("productName") String productName, @PathVariable("quoteId") Integer quoteId) 	throws TclCommonException {
		List<QuoteProductComponentBean> response = gvpnPricingFeasibilityService.getVrfData(siteId, productName, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * api to validate if sub category has been updated for NS quotes in NS flow
	 * @param quoteId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SUB_CATEGORY_FOR_NS_QUOTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/subcategory/ns/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CommonValidationResponse> validateSubCategoryForNSQuotes(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		CommonValidationResponse response = gvpnQuoteService.validateSubCategoryForNSQuotes(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Api to get multivrf annexure data for the given siteId
	 * @param siteId
	 * @param productName
	 * @param quoteId
	 * @return responseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_MULTI_VRF_ANNEXURE_DATA)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/annexure/sites/{siteId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VrfBean> getMultiVrfAnnexure(@PathVariable("siteId") List<Integer> siteId,
			@RequestParam(required = true, value = "profileStatus") String profileStatus,
			@RequestParam(required = true, value = "quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		VrfBean response = gvpnPricingFeasibilityService.getMultiVrfAnnexure(siteId,profileStatus,quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/* * api to get primary and secondary LM provider
	 * @param quoteId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 * PIPF- 59
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_LM_PROVIDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/{quoteLeId}/{siteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource< List<LMDetailBean>>getLMProvider(@PathVariable("quoteLeId") Integer quoteLeId,@PathVariable("siteId") Integer siteId) throws TclCommonException {
		List<LMDetailBean> lmDetails = gvpnQuoteService.getLMProvider(quoteLeId,siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, lmDetails,
				Status.SUCCESS);
	}


	/**
	 * api to get List of Le Owners for selection in optimus Journey
	 * @param customerId
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 *
	 * PIPF - 212
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_LE_OWNER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/leowner/{customerId}/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<LeOwnerDetailsSfdc>>getLeOwnerDetails(@PathVariable("customerId") Integer customerId, @PathVariable("quoteId") Integer quoteId,@RequestParam(name = "leId",required = false) Integer leId) throws TclCommonException {
		List<LeOwnerDetailsSfdc> ownerDetails = illQuoteService.getOwnerDetailsForSfdc(customerId,quoteId,leId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ownerDetails,
				Status.SUCCESS);
	}

}
