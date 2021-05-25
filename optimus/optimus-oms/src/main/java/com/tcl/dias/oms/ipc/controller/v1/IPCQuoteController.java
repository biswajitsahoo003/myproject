package com.tcl.dias.oms.ipc.controller.v1;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.LeOwnerDetailsSfdc;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteTncBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.ipc.beans.QuoteBean;
import com.tcl.dias.oms.ipc.beans.QuoteCloud;
import com.tcl.dias.oms.ipc.beans.SolutionDetail;
import com.tcl.dias.oms.ipc.beans.pricebean.IpcDiscountBean;
import com.tcl.dias.oms.ipc.beans.pricebean.PricingBean;
import com.tcl.dias.oms.ipc.service.v1.IPCPricingService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuotePdfService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuoteService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IPCQuoteController.java class. This class contains all
 * the API's related to Quotes for IPC product
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/ipc/quotes")
public class IPCQuoteController {

    @Autowired
    private IPCQuoteService ipcQuoteService;

    @Autowired
    private IPCQuotePdfService ipcQuotePdfService; 

    @Autowired
    private IPCPricingService ipcPricingService;

    /**
   	 * getVersion - This method is used to get version 
   	 *
   	 * @return ResponseResource<String>
   	 * @throws TclCommonException
   	 */
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> getVersion() {
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "1.0",
                Status.SUCCESS);
    }

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
    @Transactional
    public ResponseResource<QuoteResponse> createQuote(@RequestBody QuoteDetail request,
                                                       @RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
        QuoteResponse response = ipcQuoteService.createQuote(request, customerId);
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
		QuoteTncBean response = ipcQuoteService.getTnc(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_TNC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="/{quoteId}/le/{quoteLeId}/tnc",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateTnc(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteTncBean quoteTncBean) throws TclCommonException {
		String response = ipcQuoteService.processTnc(quoteId, quoteTncBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

    /**
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited getQuoteConfiguration used to
     *            fetch quote details
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
    public ResponseResource<QuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
        QuoteBean response = ipcQuoteService.getQuoteDetails(quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * deleteQuoteCloud this method is used to delete or disable the ill site info
     *
     * @param quoteId,cloudId,customerId,
     *            action
     * @return ResponseResource
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_OR_DISABLE_QUOTE_CLOUD)
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cloud/{cloudId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<QuoteBean> deleteQuoteCloudProduct(@PathVariable("quoteId") Integer quoteId,
                                                         @PathVariable("quoteLeId") Integer quoteLeId,
                                                         @RequestParam(required = false, value = "customerId") Integer customerId,
                                                         @PathVariable("cloudId") Integer cloudId) throws TclCommonException {
        ipcQuoteService.processDeactivateCloudProducts(quoteLeId, cloudId);
        QuoteBean response = ipcQuoteService.getQuoteDetails(quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
   	 * addQuoteCloudProduct - This method is used to add quote cloud product 
   	 *@param quoteId,quoteLeId,request,
     *            customerId
   	 * @return ResponseResource<String>
   	 * @throws TclCommonException
   	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.ADD_QUOTE_CLOUD)
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cloud/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<QuoteBean> addQuoteCloudProduct(@PathVariable("quoteId") Integer quoteId,
                                                            @PathVariable("quoteLeId") Integer quoteLeId,
                                                            @RequestBody QuoteCloud request,
                                                            @RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
        ipcQuoteService.addQuoteCloudProduct(request, quoteId, quoteLeId, customerId);
        QuoteBean response = ipcQuoteService.getQuoteDetails(quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
   	 * deleteQuoteCloudSolution - This method is used to delete quote cloud solution 
   	 *@param quoteId,quoteLeId,request,
     *            customerId
   	 * @return ResponseResource<QuoteBean>
   	 * @throws TclCommonException
   	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_OR_DISABLE_QUOTE_CLOUD)
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cloud/solution/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<QuoteBean> deleteQuoteCloudSolution(@RequestBody List<Integer> request,
                                                                @PathVariable("quoteId") Integer quoteId,
                                                               @PathVariable("quoteLeId") Integer quoteLeId,
                                                               @RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
        ipcQuoteService.processDeactivateCloudSolution(quoteId, quoteLeId, request);
        QuoteBean response = ipcQuoteService.getQuoteDetails(quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

	/**
	 * addComponent used to add component and its attribute to the quote
	 * @param quoteId
	 * @param solutionId
	 * @param componentDetail
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.ADD_QUOTE_COMPONENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/solution/{solutionId}/component/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> addComponent(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("solutionId") Integer solutionId, @RequestBody List<ComponentDetail> request)
			throws TclCommonException {
		QuoteBean response = ipcQuoteService.addUpdateComponentsAndAttr(quoteId, solutionId, null, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * addComponent used to add component and its attribute to the quote
	 * @param quoteId
	 * @param solutionId
	 * @param componentDetail
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@Deprecated
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.ADD_QUOTE_COMPONENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/component/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> addComponent(@PathVariable("quoteId") Integer quoteId, @RequestBody List<SolutionDetail> request) 
			throws TclCommonException {
		QuoteBean response = ipcQuoteService.addUpdateComponentsAndAttr(quoteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	/**
	 * removeComponent used to remove attribute of the quote
	 * @param quoteId
	 * @param List<componentId>
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_QUOTE_COMPONENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/component/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> removeComponent(@PathVariable("quoteId") Integer quoteId, @RequestBody List<Integer> componentId) throws TclCommonException {
		QuoteBean response = ipcQuoteService.removeComponentsAndAttr(quoteId, componentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * addComponent used to add attribute to the quote
	 * @param quoteId
	 * @param componentId
	 * @param attributeDetail
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@Deprecated
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.ADD_QUOTE_COMPONENT_ATTR)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/component/{componentId}/attribute/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> addAttribute(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("componentId") Integer componentId, @RequestBody AttributeDetail request)
			throws TclCommonException {
		QuoteBean response = ipcQuoteService.addUpdateAttributes(quoteId, componentId,request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * removeComponent used to remove the attribute of the quote
	 * @param quoteId
	 * @param List<attributeId>
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@Deprecated
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_QUOTE_COMPONENT_ATTR)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/attribute/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> removeAttribute(@PathVariable("quoteId") Integer quoteId, @RequestBody List<Integer> attributeId)
			throws TclCommonException {
		QuoteBean response = ipcQuoteService.removeAttributes(quoteId, attributeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

    /**
     *
     * This method is used to update Term and Months against quote To Le
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
        ipcQuoteService.updateTermsInMonthsForQuoteToLe(quoteId,quoteToLeId,updateRequest);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);

    }

    /**
    *
    * This method is used to download the quote PDF file.
    * @param quoteId
    * @param quoteToLeId
    * @param response
    * @return 
    * @throws TclCommonException
    */
	@GetMapping("/{quoteId}/le/{quoteLeId}/quotepdf")
	public ResponseResource<String> generateQuotePdf(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = ipcQuotePdfService.processQuotePdf(quoteId, quoteToLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl, Status.SUCCESS);
	}

	/**
    *
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
		ServiceResponse response = ipcQuoteService.processMailAttachment(email, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
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
		QuoteDetail response = ipcQuoteService.approvedQuotes(request, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	 /*
	 * generateCofPdf This method is used to download the COF PDF
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@GetMapping("/{quoteId}/le/{quoteLeId}/cofpdf")
	public ResponseResource<String> generateCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(value = "nat", required = false) Boolean nat) throws TclCommonException {
		ipcQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	@GetMapping("/{quoteId}/le/{quoteLeId}/cofpdf/regenerateAndSave")
	public ResponseResource<String> regenerateAndSaveCof(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		ipcQuotePdfService.regenerateAndSaveCof(quoteId, quoteToLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * 
	 * triggerEmail Trigger email for the delegated user after posting a record in
	 * the quote delegation table with status as open
	 * 
	 * @param userId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_EMAIL)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("{quoteId}/le/{quoteLeId}/delegate/notification")
	public ResponseResource<TriggerEmailResponse> triggerEmail(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody TriggerEmailRequest triggerEmailRequest,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		TriggerEmailResponse triggerEmailResponse = ipcQuoteService.processTriggerMail(triggerEmailRequest,
				forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, triggerEmailResponse,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/docusign")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processDocusign(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("nat") Boolean nat,
			@RequestParam("name") String name, @RequestParam("email") String emailId,
			@RequestBody ApproverListBean approver, HttpServletRequest httpServletRequest) throws TclCommonException {
		ipcQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approver);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
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
		Set<LegalAttributeBean> response = ipcQuoteService.getAllAttributesByQuoteToLeId(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 *	createDocument this method is
	 *            used to map the legal entity details and supplier entity details
	 *            and tax exception site
	 * @param request{quoteId,illsiteId,CustomerLegalEntityId,SupplierLegalEntityId}
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateDocumentDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("{quoteId}/le/{quoteLeId}/contractentity")
	public ResponseResource<CreateDocumentDto> createDocument(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody CreateDocumentDto request)
			throws TclCommonException {
		CreateDocumentDto documentDto = ipcQuoteService.createDocument(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, documentDto, Status.SUCCESS);
	}

	/**
	 * 
	 * getContactAttributeDetails
	 * 
	 * @param quoteId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONTACT_ATTRIBUTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ContactAttributeInfo.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/{quoteId}/le/{quoteLeId}/contactattributes")
	public ResponseResource<ContactAttributeInfo> getContactAttributeDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {

		ContactAttributeInfo response = ipcQuoteService.getContactAttributeDetails(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);

	}

	/**
	 *	updateLegalEntityProperties - This method is
	 *            used to update legal entity properties
	 * @param request{quoteId,quoteToLeId,request}
	 * @return ResponseResource<QuoteDetail>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/{quoteId}/le/{quoteToLeId}/attribute")
	public ResponseResource<QuoteDetail> updateLegalEntityProperties(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		QuoteDetail response = ipcQuoteService.updateLegalEntityProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);

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
		QuoteDetail response = ipcQuoteService.persistListOfQuoteLeAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * triggerAccountManagerCofDownloadNotification - This method triggers an email
	 * to the account manager when the customer downloads a cof
	 * 
	 * 
	 * @param orderId,
	 *        	orderToLeId
	 * 
	 * @return String
	 * @throws TclCommonException
	 */
	@GetMapping("/{quoteId}/le/{quoteLeId}/cofdownload/accountmanagernotification")
	public ResponseEntity<String> triggerAccountManagerCofDownloadNotification(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		ipcQuoteService.cofDownloadAccountManagerNotification(quoteToLeId);
		return null;

	}
	
	/**
	 *	updateQuoteStage - This method is
	 *            used to update quote stage
	 * @param request{quoteId,quoteToLeId,quoteStage}
	 * @return ResponseResource<String>
	 * @throws TclCommonException
	 */
	@PostMapping("/{quoteId}/le/{quoteLeId}/stage/{quoteStage}")
	public ResponseResource<String> updateQuoteStage(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @PathVariable("quoteStage") String quoteStage) throws TclCommonException {
		ipcQuoteService.updateQuoteStage(quoteToLeId, QuoteStageConstants.getByCode(quoteStage));
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 *	triggerPricing - This method is
	 *            used to trigger pricing
	 * @param request{quoteId,quoteToLeId}
	 * @return ResponseResource<String>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_PRICING)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/pricing", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerPricing(@PathVariable("quoteId") Integer quoteId,
												   @PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		ipcPricingService.processPricingRequest(quoteId, quoteToLeId, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 *	updateCurrency - This method is
	 *            used to update currency
	 * @param request{quoteId,quoteToLeId,currency}
	 * @return ResponseResource<String>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CURRENCY_CONVERSION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/{quoteId}/le/{quoteLeId}/currencyconvert")
	public ResponseResource<String> updateCurrency(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("currency") String inputCurrency)
			throws TclCommonException {
		ipcQuoteService.updateCurrency(quoteId, quoteToLeId, inputCurrency);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 *	updateCurrency - This method is
	 *            used to update currency
	 * @param request{quoteId,quoteToLeId,actual,currency,net_margin_percentage,final_discount_percentage}
	 * @return ResponseResource<PricingBean>
	 * @throws TclCommonException
	 */
	@Deprecated
	@GetMapping("/{quoteId}/le/{quoteLeId}/price")
	public ResponseResource<PricingBean> getQuotePricing(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestParam(value="actual", defaultValue="false") Boolean actual,
			@RequestParam(value="currency", required=false) String currency,
			@RequestParam(value="additional_discount_percentage", required=false) Double additionalDiscountPercentage, 
			@RequestParam(value = "ipc_final_price", required=false) Double ipcFinalPrice,
			@RequestParam(value = "askAccessPrice", required=false) Double askAccessPrice,
			@RequestParam(value = "askAdditionalIpPrice", required=false) Double askAdditionalIpPrice)
			throws TclCommonException {
		PricingBean pricingBean = ipcPricingService.getQuotePrice(quoteId, actual, currency, additionalDiscountPercentage, ipcFinalPrice,askAccessPrice,askAdditionalIpPrice, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				pricingBean, Status.SUCCESS);
	}

	/**
	 *	updateCurrency - This method is
	 *            used to update quote price discount
	 * @param request{quoteId,quoteToLeId,customerNetMargin}
	 * @return ResponseResource<String>
	 * @throws TclCommonException
	 */
	@Deprecated
	@PostMapping("/{quoteId}/le/{quoteLeId}/discount")
	public ResponseResource<String> updateQuotePricingDiscount(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestBody IpcDiscountBean ipcDiscountBean)
			throws TclCommonException {
		ipcPricingService.updateQuotePricingDiscount(quoteId, quoteToLeId, ipcDiscountBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.name(), Status.SUCCESS);
	}
	
	/**
	 * This api is used to trigger workflow from get quote.
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_WORKFLOW)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/{quoteId}/le/{quoteLeId}/triggerworkflow")
	public ResponseResource<Boolean> getDiscountDetails(@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		Boolean response = ipcPricingService.triggerWorkFlow(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,	response, Status.SUCCESS);
	}

	/**
	 * used to save and process the ask price
	 * @param quoteId
	 * @param quoteToLeId
	 * @param siteCode
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_ASK_PRICE)
	@PostMapping(value = "/{quoteId}/le/{quoteToLeId}/askprice")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processAskPrice(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody UpdateRequest request) throws TclCommonException {
		ipcPricingService.processAskPrice(quoteId, quoteToLeId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.name(), Status.SUCCESS);
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
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/sfdc/status")
	public ResponseResource<String> updateSdfcStage(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("stage") String stage)
			throws TclCommonException {
		ipcQuoteService.updateSfdcStage(quoteToLeId, stage);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	/**
	 * api to get List of Le Owners for selection in optimus Journey - PIPF - 212
	 * 
	 * @param customerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_LE_OWNER_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = LeOwnerDetailsSfdc.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/customer/{customerId}/le/{customerLeId}/leowners", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<LeOwnerDetailsSfdc>> getLeOwnerDetails(@PathVariable("customerId") Integer customerId,
			@PathVariable("customerLeId") Integer customerLeId) throws TclCommonException {
		List<LeOwnerDetailsSfdc> ownerDetails = ipcQuoteService.getOwnerDetailsForSfdc(customerId, customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ownerDetails,
				Status.SUCCESS);
	}
	
	@PostMapping(value = "/{quoteCode}/triggersfdc/updateproduct")
	public ResponseResource<String> triggerSfdcUpdateProduct(@PathVariable("quoteCode") String quoteCode)
			throws TclCommonException {
		ipcQuoteService.triggerSfdcUpdateProduct(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),
				Status.SUCCESS);
	}
}
