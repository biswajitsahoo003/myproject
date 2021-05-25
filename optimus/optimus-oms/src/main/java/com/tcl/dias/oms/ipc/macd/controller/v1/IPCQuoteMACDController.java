package com.tcl.dias.oms.ipc.macd.controller.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.IPCMACDOrderSummaryResponse;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateIpcComponentBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.ipc.beans.QuoteBean;
import com.tcl.dias.oms.ipc.beans.QuoteCloud;
import com.tcl.dias.oms.ipc.beans.SolutionDetail;
import com.tcl.dias.oms.ipc.macd.service.v1.IPCQuoteMACDService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuotePdfService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuoteService;
import com.tcl.dias.oms.macd.beans.MacdQuoteRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IPCQuoteMACDController.java class. This class contains all
 * the API's related to MACD Quotes for IPC product
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/ipc/quote/macd")
public class IPCQuoteMACDController {

    @Autowired
    IPCQuoteMACDService ipcQuoteMACDService;

    @Autowired
    IPCQuotePdfService ipcQuotePdfService;

    @Autowired
    IPCQuoteService ipcQuoteService;
    
    /**
	 * createQuote - This method is used to create a quote 
	 *
	 * @param request
	 * @return ResponseResource<MacdQuoteResponse>
	 * @throws TclCommonException
	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.MACD.PLACE_MACD_REQUEST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<MacdQuoteResponse> createQuote(@RequestBody MacdQuoteRequest request) throws TclCommonException {
        MacdQuoteResponse response=ipcQuoteMACDService.handleMacdRequestToCreateQuote(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
	 * getMACDQuoteDetails - This method is used to get quote details 
	 *
	 * @param quoteId
	 * @return ResponseResource<QuoteBean>
	 * @throws TclCommonException
	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteBean> getMACDQuoteDetails(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
        QuoteBean response = ipcQuoteMACDService.getMACDQuoteDetails(quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
   	 * addQuoteCloudProduct - This method is used to add quote cloud product 
   	 *
   	 * @param quoteId,quoteLeId,request,customerId
   	 * @return ResponseResource<QuoteBean>
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
        ipcQuoteMACDService.addQuoteCloudProduct(request, quoteId, quoteLeId, customerId);
        QuoteBean response = ipcQuoteMACDService.getQuoteDetails(quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
   	 * deleteQuoteCloudSolution - This method is used to delete quote cloud solution 
   	 *
   	 * @param quoteId,quoteLeId,request,customerId
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
   	 * deleteQuoteCloudProduct - This method is used to delete quote cloud product 
   	 *
   	 * @param quoteId,quoteLeId,cloudId,customerId
   	 * @return ResponseResource<QuoteBean>
   	 * @throws TclCommonException
   	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_OR_DISABLE_QUOTE_CLOUD)
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cloud/{cloudId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<QuoteBean> deleteQuoteCloudProduct(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
                                                               @RequestParam(required = false, value = "customerId") Integer customerId,
                                                               @PathVariable("cloudId") Integer cloudId) throws TclCommonException {
        ipcQuoteMACDService.processDeactivateCloudProducts(quoteLeId, cloudId);
        QuoteBean response = ipcQuoteMACDService.getQuoteDetails(quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
   	 * addComponent - This method is used to add component 
   	 *
   	 * @param quoteId,quoteLeId,solutionId,request
   	 * @return ResponseResource<QuoteBean>
   	 * @throws TclCommonException
   	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.ADD_QUOTE_COMPONENT)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{quoteId}/solution/{solutionId}/component/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteBean> addComponent(@PathVariable("quoteId") Integer quoteId,
                                                    @PathVariable("solutionId") Integer solutionId, @RequestBody List<ComponentDetail> request) throws TclCommonException {
        QuoteBean response = ipcQuoteMACDService.addUpdateComponentsAndAttr(quoteId, solutionId, null, request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }
    
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.ADD_QUOTE_COMPONENT)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{quoteId}/solution/{solutionId}/service/{serviceId}/component/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteBean> addComponentForVdom(@PathVariable("quoteId") Integer quoteId,
                                                    @PathVariable("solutionId") Integer solutionId, @PathVariable("serviceId") String serviceId, 
                                                    @RequestBody List<ComponentDetail> request) throws TclCommonException {
        QuoteBean response = ipcQuoteMACDService.addUpdateComponentsAndAttr(quoteId, solutionId, serviceId, request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }
    
    /**
   	 * updateSolutionName - This method is used to update solution name 
   	 *
   	 * @param quoteCloudId,solutionName
   	 * @return ResponseResource<String>
   	 * @throws TclCommonException
   	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SOLUTION_NAME)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/solution/{quoteCloudId}/{solutionName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> updateSolutionName(@PathVariable("quoteCloudId") Integer quoteCloudId,
                                                    @PathVariable("solutionName") String solutionName, @RequestBody SolutionDetail request) throws TclCommonException {
        ipcQuoteMACDService.updateSolutionName(quoteCloudId, solutionName, request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);
    }

    /**
   	 * removeComponent - This method is used to remove component 
   	 *
   	 * @param quoteId,componentId
   	 * @return ResponseResource<QuoteBean>
   	 * @throws TclCommonException
   	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_QUOTE_COMPONENT)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{quoteId}/component/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteBean> removeComponent(@PathVariable("quoteId") Integer quoteId, @RequestBody List<Integer> componentId) throws TclCommonException {
        QuoteBean response = ipcQuoteMACDService.removeComponentsAndAttr(quoteId, componentId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
   	 * shareQuote - This method is used to share quote 
   	 *
   	 * @param quoteId,quoteLeId,email
   	 * @return ResponseResource<ServiceResponse>
   	 * @throws TclCommonException
   	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping("/{quoteId}/le/{quoteLeId}/quote/share")
    public ResponseResource<ServiceResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
                                                        @PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("email") String email) throws TclCommonException {
        ServiceResponse response = ipcQuoteMACDService.processMailAttachment(email, quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
   	 * generateQuotePdf - This method is used to generate quote pdf 
   	 *
   	 * @param quoteId,quoteLeId,response
   	 * @return ResponseResource<String>
   	 * @throws TclCommonException
   	 */
    @GetMapping("/{quoteId}/le/{quoteLeId}/quotepdf")
    public ResponseResource<String> generateQuotePdf(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
        String tempDownloadUrl = ipcQuotePdfService.processQuotePdf(quoteId, quoteToLeId, response);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl, Status.SUCCESS);
    }

    /**
   	 * updateTermAndMonths - This method is used to update term and months 
   	 *
   	 * @param quoteId,quoteLeId,updateRequest
   	 * @return ResponseResource<String>
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
        ipcQuoteMACDService.updateTermsInMonthsForQuoteToLe(quoteId,quoteToLeId,updateRequest);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);
    }

    /**
   	 * updateCurrency - This method is used to update currency 
   	 *
   	 * @param quoteId,quoteLeId,currency
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
                                                   @PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("currency") String inputCurrency) throws TclCommonException {
        ipcQuoteMACDService.updateCurrency(quoteId, quoteToLeId, inputCurrency);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);
    }

    /**
   	 * updateQuoteStage - This method is used to update quote stage 
   	 *
   	 * @param quoteId,quoteLeId,quoteStage
   	 * @return ResponseResource<String>
   	 * @throws TclCommonException
   	 */
    @PostMapping("/{quoteId}/le/{quoteLeId}/stage/{quoteStage}")
    public ResponseResource<String> updateQuoteStage(@PathVariable("quoteId") Integer quoteId,
                                                     @PathVariable("quoteLeId") Integer quoteToLeId, @PathVariable("quoteStage") String quoteStage) throws TclCommonException {
        ipcQuoteMACDService.updateQuoteStage(quoteToLeId, QuoteStageConstants.getByCode(quoteStage));
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);
    }

    /**
   	 * generateCofPdf - This method is used to generate cof pdf 
   	 *
   	 * @param quoteId,quoteLeId,nat
   	 * @return ResponseResource<String>
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
    
    /**
   	 * triggerAccountManagerCofDownloadNotification - This method is used to trigger account manager cof download notification 
   	 *
   	 * @param quoteId,quoteLeId
   	 * @return ResponseResource<String>
   	 * @throws TclCommonException
   	 */
    @GetMapping("/{quoteId}/le/{quoteLeId}/cofdownload/accountmanagernotification")
	public ResponseEntity<String> triggerAccountManagerCofDownloadNotification(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		ipcQuoteService.cofDownloadAccountManagerNotification(quoteToLeId);
		return null;

	}

    /**
   	 * triggerEmail - This method is used to delegate notification 
   	 *
   	 * @param quoteId,quoteLeId,triggerEmailRequest,httpServletRequest
   	 * @return ResponseResource<TriggerEmailResponse>
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
        TriggerEmailResponse triggerEmailResponse = ipcQuoteMACDService.processTriggerMail(triggerEmailRequest,
                forwardedIp);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, triggerEmailResponse,
                Status.SUCCESS);
    }

    /**
   	 * processDocusign - This method is used to process document sign 
   	 *
   	 * @param quoteId,quoteLeId,nat,name,email,approver,httpServletRequest
   	 * @return ResponseResource<String>
   	 * @throws TclCommonException
   	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
    @PostMapping(value = "/{quoteId}/le/{quoteLeId}/docusign")
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<String> processDocusign(@PathVariable("quoteId") Integer quoteId,
                                                    @PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("") Boolean nat,
                                                    @RequestParam("name") String name, @RequestParam("email") String emailId,
                                                    @RequestBody ApproverListBean approver, HttpServletRequest httpServletRequest) throws TclCommonException {
        ipcQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approver);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);
    }

    /**
   	 * approvedQuotes - This method is used to approve quotes 
   	 *
   	 * @param request,httpServletRequest
   	 * @return ResponseResource<QuoteDetail>
   	 * @throws TclCommonException
   	 */
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
        QuoteDetail response = ipcQuoteMACDService.macdApprovedQuotes(request, forwardedIp);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    /**
   	 * updateLegalEntityProperties - This method is used to update legal entity properties 
   	 *
   	 * @param quoteId,quoteToLeId,request
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
		QuoteDetail response = ipcQuoteMACDService.updateLegalEntityProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);

	}
	
	/**
   	 * createDocument - This method is used to create document 
   	 *
   	 * @param quoteId,quoteToLeId,request
   	 * @return ResponseResource<CreateDocumentDto>
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
		CreateDocumentDto documentDto = ipcQuoteMACDService.createDocument(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, documentDto, Status.SUCCESS);
	}
	
	/**
   	 * createDocument - This method is used to persist quote le attributes 
   	 *
   	 * @param quoteId,quoteToLeId,request
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
		QuoteDetail response = ipcQuoteMACDService.persistListOfQuoteLeAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
   	 * getOrderSummary - This method is used to get order summary 
   	 *
   	 * @param quoteId,quoteToLeId,serviceId
   	 * @return ResponseResource<IPCMACDOrderSummaryResponse>
   	 * @throws TclCommonException
   	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.GET_MACD_ORDER_SUMMARY)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/ordersummary", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
	            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<IPCMACDOrderSummaryResponse> getOrderSummary(@PathVariable("quoteId") Integer quoteId,
	            @PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("serviceId") String serviceId)
	            throws TclCommonException {
		IPCMACDOrderSummaryResponse response = ipcQuoteMACDService.getOrderSummary(quoteId, quoteLeId, serviceId);
	        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	                Status.SUCCESS);
	 }
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.ADD_QUOTE_COMPONENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UpdateIpcComponentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/component/attribute/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processComponentAttribute(@PathVariable("quoteId") Integer quoteId,
			@RequestBody List<UpdateIpcComponentBean> request)
			throws TclCommonException {
		String response = ipcQuoteMACDService.addOrUpdateComponentAttr(quoteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
}
