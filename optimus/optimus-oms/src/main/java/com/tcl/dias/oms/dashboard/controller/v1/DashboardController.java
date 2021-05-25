package com.tcl.dias.oms.dashboard.controller.v1;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.utils.Products;
import com.tcl.dias.oms.entity.entities.MstOrderNaLiteProductFamily;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.DocusignAuditBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.CnResponse;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.OrderConfigurations;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteAccessBean;
import com.tcl.dias.oms.beans.QuoteBeanForOrderAmendment;
import com.tcl.dias.oms.beans.QuoteConfigurations;
import com.tcl.dias.oms.beans.OpportunityConfigurations;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.cancellation.mdminventory.dao.MDMInventoryDAO;
import com.tcl.dias.oms.dashboard.service.v1.DashboardService;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.MstOrderNaLiteProductFamily;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.ill.service.v1.IllOrderService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuoteService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuoteService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.pdf.service.IllQuotePdfService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.dias.common.beans.MstProductFamilyBean;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the DashboardController.java class.
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController {

	@Autowired
	DashboardService dashboardService;

	@Autowired
	IllOrderService illOrderService;

	@Autowired
	IllQuotePdfService illQuotePdfService;

	@Autowired
	IllQuoteService illQuoteService;
	
	@Autowired
	IPCQuoteService ipcQuoteService;

	@Autowired
	IzoPcQuoteService izoPcQuoteService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	DocusignService docuSignService;

	@Autowired
	IzosdwanQuoteService izoSdwanQuoteService;

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DASH_BOARD_DETAILS)
	@RequestMapping(value = "/quotes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteConfigurations> getQuoteDashboardDetails(@RequestParam(value = "page") Integer page,
																		  @RequestParam(value = "size") Integer size,@RequestParam(required=false,value = "customerId") Integer customerId,@RequestParam(value = "deactive",required = false,defaultValue = "false") boolean deactive,
																		  @RequestParam(required=false,value = "productId") Integer productId,@RequestParam(required=false,value = "classification") String classification) throws TclCommonException {
		QuoteConfigurations response = dashboardService.getActiveQuoteConfigurations(page, size,customerId,deactive,productId,classification);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DASH_BOARD_DETAILS)
	@GetMapping(value = "/quotes/v1", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteConfigurations> getQuoteDashboardDetailsV1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size,
			@RequestParam(required = false, value = "customerId") Integer customerId,
			@RequestParam(value = "deactive", required = false, defaultValue = "false") boolean deactive,
			@RequestParam(required = false, value = "productName") String productName,
			@RequestParam(required = false, value = "quoteCode") String quoteCode,
			@RequestParam(required = false, value = "oe",defaultValue = "false") boolean oe,
			@RequestParam(required = false, value = "classification") String classification) throws TclCommonException {
		QuoteConfigurations response = dashboardService.getActiveQuoteConfigurationsV1(page, size, customerId,deactive, productName, classification, quoteCode, oe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}


	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DASH_BOARD_DETAILS)
	@RequestMapping(value = "/quotes/{quoteId}/access", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE )
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> quoteAccess(@PathVariable("quoteId") Integer quoteId,@RequestBody QuoteAccessBean quoteAccessBean){
		dashboardService.processQuoteAccess(quoteId,quoteAccessBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_DETAILS)
	@RequestMapping(value = "/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderConfigurations> getOrderDashboardDetails(@RequestParam(required=false,value = "customerId") Integer customerId) throws TclCommonException {
		OrderConfigurations response = dashboardService.getOrderConfigurationsWithFilter(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_ORDER_DETAILS)
	@RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		OrdersBean response = illOrderService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	@Transactional
	public ResponseResource<String> generateCofPdf(@PathVariable("orderId") Integer orderId,
												   @PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = illQuotePdfService.processApprovedCof(orderId, orderLeId, response, true);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * This method is used to get customer service details for dashboard in customer
	 * portal
	 *
	 * @return List<CustomerOrderDetailsBean>
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_ORDER_DETAILS)
	@RequestMapping(value = "/trackOrderCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Map<String, Integer>> getTrackOrderCount() throws TclCommonException {
		Map<String, Integer> orderCount = dashboardService.getTrackOrderCount();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, orderCount,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getProductTrackOrderCount
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_ORDER_DETAILS)
	@RequestMapping(value = "/productcount", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Map<String, Map<String, Integer>>> getProductTrackOrderCount() throws TclCommonException {
		Map<String, Map<String, Integer>> orderCount = dashboardService.getProductTrackOrderCount();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, orderCount,
				Status.SUCCESS);

	}

	/**
	 * Get DocuSign Audit details for the Quote
	 *
	 * @author ANANDHI VIJAY
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/quotes/{quoteId}/docusign/audit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<DocusignAuditBean> getAuditDocusignDetails(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		DocusignAuditBean response = docuSignService.getDocusignAuditDetails(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 *
	 * Download docusign envelope documents
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/docusignpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateDocusignPdf(@PathVariable("quoteId") Integer quoteId,
														@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = docuSignService.getDocumentBasedOnQuoteCode(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 *
	 * Delete or Resend DocusignDocument
	 *
	 * @param quoteId
	 * @param action
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/quotes/{quoteId}/docusign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> deleteDocusignDocument(@PathVariable("quoteId") Integer quoteId,
														   @RequestParam("action") String action) throws TclCommonException {
		String response = docuSignService.modifyDocusignDocumentBasedOnAction(quoteId, action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * processDocusignRetry- Retry the DocuSign Notification
	 * @param envelopeId
	 * @param envelopeResponse
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/docusign/{envelopeId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processDocusignRetry(@PathVariable("envelopeId") String envelopeId,@RequestBody String envelopeResponse) throws TclCommonException {
		docuSignService.processDocuSignErrorNotificationResponse(envelopeResponse,envelopeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),
				Status.SUCCESS);
	}

	@RequestMapping(value = "/quotes/{quoteId}/docusign/mocktrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> mockTriggerDocusign(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		Boolean status=docuSignService.mockTriggerDocuSign(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, status,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/quotes/{quoteId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deleteQuote(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		illQuoteService.deleteQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DELETE_QUOTE)
	@RequestMapping(value = "/ipc/quotes/{quoteId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deleteIpcQuote(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		ipcQuoteService.deleteQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DELETE_QUOTE)
	@RequestMapping(value = "gvpn/quotes/{quoteId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deleteGvpnQuote(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		gvpnQuoteService.deleteQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DELETE_QUOTE)
	@RequestMapping(value = "izopc/quotes/{quoteId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deleteIzoPcQuote(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		izoPcQuoteService.deleteQuote(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * deleteQuoteSdwan- Delete quote
	 * @pathvariable quoteId
	 * @throws TclCommonException
	 */
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
	
	@RequestMapping(value = "/common/document", method = RequestMethod.GET)
	public ResponseResource<String> downloadDocument(@RequestParam(name = "fileName",required = true) String fileName,
			@RequestParam(name="path",required = true) String path)
			throws TclCommonException {
		String response = dashboardService.getDownloadUrl(fileName, path);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DELETE_QUOTE)
	@PostMapping(value = "/quotes/{quoteId}")
	public ResponseResource<CnResponse> activateDeactivateQuotes(@RequestParam(name = "action",required = true) String action,
			@PathVariable(name="quoteId",required = true) Integer quoteId,@RequestBody Map<String,String> request) {
		CnResponse response=null;
		if(action.equalsIgnoreCase("DEACTIVATE")) {
			response=dashboardService.deactivateQuote(quoteId, request);
		}else if(action.equalsIgnoreCase("ACTIVATE")){
			response=dashboardService.activateQuote(quoteId, request);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DELETE_QUOTE)
	@GetMapping(value = "/quotes/{quoteId}/audit")
	public ResponseResource<List<Map<String, Object>>> getQuoteAudit(@PathVariable(name="quoteId",required = true) Integer quoteId) {
		List<Map<String, Object>> response=null;
			response=dashboardService.getQuoteAudit(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	

	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_PARTNER_QUOTE_CUSTOMER)
	@GetMapping(value = "/quotes/partner/customerdetails")
	public ResponseResource<CustomerBean> getCustomerDetailFromQuote(@RequestParam(name="partnerId",required = false) String partnerId) throws TclCommonException {
		CustomerBean response = dashboardService.getCustomerDetailFromQuote(partnerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_ORDER_NA_LITE_PRODUCT)
	@GetMapping(value = "/quotes/partner/productdetails")
	public ResponseResource<List<MstOrderNaLiteProductFamily>> getProductDetailFromOpty(@RequestParam(name="partnerId",required = false) String partnerId,@RequestParam(value = "deactive",required = false,defaultValue = "false") boolean deactive) throws TclCommonException {
		List<MstOrderNaLiteProductFamily> response = dashboardService.getProductDetailFromOpty(partnerId,deactive);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_PARTNER_QUOTE_CUSTOMER)
	@GetMapping(value = "/opty/partner/customerdetails")
	public ResponseResource<CustomerBean> getCustomerDetailFromOpty(@RequestParam(name="partnerId",required = false) String partnerId, @RequestParam(value = "deactive",required = false,defaultValue = "false") boolean deactive)throws TclCommonException {
		CustomerBean response = dashboardService.getCustomerDetailFromOpty(partnerId,deactive);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DASH_BOARD_DETAILS)
    @RequestMapping(value = "/orderlite", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<OpportunityConfigurations> getOptyDashboardDetails(@RequestParam(value = "page") Integer page,
                                                                          @RequestParam(value = "size") Integer size,@RequestParam(required=false,value = "customerId") Integer customerId,@RequestParam(required=false,value = "productId") Integer productId,
																			   @RequestParam(value = "deactive",required = false,defaultValue = "false") boolean deactive) throws TclCommonException {
        OpportunityConfigurations response = dashboardService.getActiveOptyConfigurations(page, size,customerId,productId,deactive);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DELETE_OPPORTUNITY)
	@PostMapping(value = "/opportunity/{quoteCode}")
	public ResponseResource<CnResponse> deleteOpportunity(@RequestParam(name = "action",required = true) String action,
																 @PathVariable(name="quoteCode",required = true) String quoteCode,@RequestBody Map<String,String> request) {
		CnResponse response=null;
		if(action.equalsIgnoreCase("DEACTIVATE")) {
			response=dashboardService.deactivateOpportunity(quoteCode, request);
		}else if(action.equalsIgnoreCase("ACTIVATE")){
			response=dashboardService.activateOpportunity(quoteCode, request);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.QUOTE_CODE_AUDIT)
	@GetMapping(value = "/opportunity/{quoteCode}/audit")
	public ResponseResource<List<Map<String, Object>>> getOrderLiteAudit(@PathVariable(name="quoteCode",required = true) String quoteCode) {
		List<Map<String, Object>> response=null;
		response=dashboardService.getOrderLiteAudit(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_ORDER_NA_LITE_PRODUCT)
	@GetMapping(value = "/quotes/productdetails")
	public ResponseResource<List<MstProductFamilyBean>> getProductDetailFromQuote(@RequestParam(name="partnerId",required = false) String partnerId, @RequestParam(value = "deactive",required = false,defaultValue = "false") boolean deactive) throws TclCommonException {
		List<MstProductFamilyBean> response = dashboardService.getProductDetailForQuote(partnerId,deactive);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_ORDER_NA_LITE_PRODUCT)
	@GetMapping(value = "/productlistbycustomer/{customerId}")
	public ResponseResource<List<Map<String, Object>>> getProductListbyCustomerId(@PathVariable(name="customerId", required = true) Integer customerId) {
		List<Map<String,Object>> response = dashboardService.getProductListByCustomerId(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
