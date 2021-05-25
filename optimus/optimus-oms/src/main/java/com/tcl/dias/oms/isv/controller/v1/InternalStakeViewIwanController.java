package com.tcl.dias.oms.isv.controller.v1;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.DocusignAuditBean;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.ManualCommercialUpdate;
import com.tcl.dias.common.beans.MfL2OReportBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.CommercialRejectionBean;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.IsvFilterResponse;
import com.tcl.dias.oms.beans.ManualFeasibilityRequest;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrderIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderSiteRequest;
import com.tcl.dias.oms.beans.OrderSummaryBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.PDRequest;
import com.tcl.dias.oms.beans.QuoteAccessBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuotePriceAuditResponse;
import com.tcl.dias.oms.beans.QuoteSiteNotFeasibleBean;
import com.tcl.dias.oms.beans.QuoteSummaryBean;
import com.tcl.dias.oms.beans.QuoteTncBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.SfdcAuditBean;
import com.tcl.dias.oms.beans.SiteAttributeUpdateBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.crossconnect.service.v1.CrossConnectPricingFeasibilityService;
import com.tcl.dias.oms.dashboard.service.v1.DashboardService;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.dto.OrderToLeDto;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderSiteProvisionAudit;
import com.tcl.dias.oms.gsc.service.v1.GlobalOutboundRateCardService;
import com.tcl.dias.oms.gsc.service.v1.GscExportLRService;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.iwan.service.v1.IwanOrderService;
import com.tcl.dias.oms.iwan.service.v1.IwanPricingFeasibilityService;
import com.tcl.dias.oms.iwan.service.v1.IwanQuoteService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.service.IwanQuotePdfService;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.renewals.service.RenewalsGvpnQuotePdfService;
import com.tcl.dias.oms.renewals.service.RenewalsIllQuotePdfService;
import com.tcl.dias.oms.service.CommercialReportService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsExcelService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.service.ReportService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.solution.prioritization.service.SolutionPrioritizationService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the InternalStakeViewController.java class.
 * 
 *
 * @author VIVEK KUMAR Kp
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/isv/v1/iwan")
public class InternalStakeViewIwanController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InternalStakeViewController.class);

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	IwanOrderService iwanOrderService;

	@Autowired
	IwanQuoteService iwanQuoteService;
	 
	@Autowired
	IwanPricingFeasibilityService iwanPricingFeasibilityService;

	@Autowired
	IwanQuotePdfService iwanQuotePdfService;
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;
	 
	@Autowired
	UserService userService;

	@Autowired
	OmsExcelService omsExcelService;
	 
	@Autowired
	DocusignService docuSignService;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	GscExportLRService gscExportLRService;
	
	@Autowired
	ReportService reportService;

	@Autowired
	CommercialReportService commercialReportService;

	@Autowired
	PartnerService partnerService;
	
	@Autowired
	SolutionPrioritizationService solutionPrioritizationService;
	
	@Autowired
	DashboardService dashboardService;
	
	@Autowired
	CrossConnectPricingFeasibilityService crossConnectPricingFeasibilityService;
	
	@Autowired
	GstInService gstInService;
	
	@Autowired
	NotificationService notificationService;

	@Autowired
	GlobalOutboundRateCardService globalOutboundRateCardService;
	
	@Autowired
	RestClientService restClientService;

	@Autowired
	CancellationService cancellationService;
	
	@Autowired
	RenewalsIllQuotePdfService renewalsIllQuotePdfService;
	
	@Value("${system.proxy.host}")
	String proxyHost;

	@Value("${system.proxy.port}")
	Integer proxyPort;

	@Autowired
	RenewalsGvpnQuotePdfService renewalsGvpnQuotePdfService;
	

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications getAllOrders used to get all orders for
	 *            stake holder
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ALL_ORDERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OrdersBean>> getAllOrders() throws TclCommonException {
		List<OrdersBean> response = iwanOrderService.getAllOrders();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.VALIDATE_ORDERFORM)
	@RequestMapping(value = "/quote/{quoteId}/le/{quoteToLeId}/cof/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CommonValidationResponse> processCofValidator(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		CommonValidationResponse response = iwanQuoteService.processValidateMandatoryAttr(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getOrderDetailsExcel
	 * 
	 * @param orderId
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/orders/{orderId}/download", method = RequestMethod.GET)
	public ResponseEntity<String> getOrderDetailsExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws IOException, TclCommonException {
		iwanOrderService.returnExcel(orderId, response);
		return null;

	}

	/**
	 * 
	 * generateCofPdf
	 * 
	 * @param orderId
	 * @param orderLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = iwanQuotePdfService.processApprovedCof(orderId, orderLeId, response, false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempDownloadUrl, Status.SUCCESS);
	}

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateOrderSiteStatus this method
	 *            is used to update the status of order Ill sites
	 * @param orderIllSiteId,
	 *            status
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSiteProvisionAudit.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderSiteProvisionAudit>> updateOrderSiteStatus(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("siteId") Integer siteId, @RequestBody OrderSiteRequest request) throws TclCommonException {
		List<OrderSiteProvisionAudit> response = iwanOrderService.updateOrderSiteStatus(siteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications getAllOrders used to get all orders for
	 *            stake holder
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/orders/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OrderSummaryBean>> getOrderSummary() throws TclCommonException {
		List<OrderSummaryBean> response = iwanOrderService.getOrderSummary();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * 
	 * This API is used to get the order details in ISV page
	 * @param page
	 * @param size
	 * @param searchText
	 * @param customerId
	 * @param legalEntityId
	 * @param optyId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/orders/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PagedResult<OrderSummaryBean>> getPageOrderSummary(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam(required = false, name = "searchText") String searchText,
			@RequestParam(required = false, name = "legalEntityId") Integer legalEntityId,
			@RequestParam(required = false, name = "customerId") Integer customerId) throws TclCommonException {
		PagedResult<OrderSummaryBean> response = iwanOrderService.getOrderSummary(page, size, searchText, customerId,
				legalEntityId, searchText);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
	 *            the order details
	 * @param customerId
	 * @param quoteId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		OrdersBean response = iwanOrderService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author Anandhi Vijayaraghavan
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
	 *            the order details
	 * @param orderId
	 * @return OrderIllSitesWithFeasiblityAndPricingBean
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/orders/sites/{siteId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> getFeasiblityAndPricingDetailsForOrderIllSites(
			@PathVariable("siteId") Integer siteId) throws TclCommonException {
		OrderIllSitesWithFeasiblityAndPricingBean response = iwanOrderService
				.getFeasiblityAndPricingDetailsForOrderIllSites(siteId);
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
		QuoteIllSitesWithFeasiblityAndPricingBean response = iwanOrderService
				.getFeasiblityAndPricingDetailsForQuoteIllSites(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getManualFP(@PathVariable("siteId") Integer siteId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody FPRequest fpRequest) throws TclCommonException {
		iwanPricingFeasibilityService.processManualFP(fpRequest, siteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.PROCESS_CUSTOM_FP)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/fpdetails/custom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processCustomerFP(@PathVariable("siteId") Integer siteId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			HttpServletResponse response, @RequestParam("file") MultipartFile file) throws TclCommonException {
		iwanPricingFeasibilityService.processCustomFP(siteId, quoteId, quoteLeId, response, file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.PROCESS_CUSTOM_FP)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/templates/fpdetails/custom", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> getCustomTemplate(HttpServletResponse response) throws TclCommonException {
		omsExcelService.downloadCustomFpTemplate(response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.PROCESS_CUSTOM_FP)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/templates/fpdetails/intl/custom/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> getCustomTemplateInternational(HttpServletResponse response) throws TclCommonException {
		omsExcelService.downloadCustomFpTemplateIntl(response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.EDIT_FEASIBLITY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/manualfdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> editManualFeasibility(@PathVariable("siteId") Integer siteId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody ManualFeasibilityRequest manualfRequest) throws TclCommonException {
		iwanPricingFeasibilityService.processManualFeasibility(manualfRequest, siteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	
	
	
	
	
	
	
  
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.NOTIFY_FEASIBLITY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/fpdetails/notification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> notifyManualFeasibility(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		iwanPricingFeasibilityService.notifyManualFeasibility(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications getAllOrders used to get all orders for
	 *            stake holder
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<QuoteSummaryBean>> getQuoteSummary() throws TclCommonException {
		List<QuoteSummaryBean> response = iwanQuoteService.getQuoteSummary();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PagedResult<QuoteSummaryBean>> getPageQuoteSummary(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam(required = false, name = "searchText") String searchText,
			@RequestParam(required = false, name = "legalEntityId") Integer legalEntityId,
			@RequestParam(required = false, name = "customerId") Integer customerId)
			throws TclCommonException {
		PagedResult<QuoteSummaryBean> response = iwanQuoteService.getQuoteSummary(page, size, searchText, customerId, legalEntityId);
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
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,
			@RequestParam(required = false, name = "siteproperties") Boolean siteproperties,
			@RequestParam(required = false, name = "siteId") Integer siteId,
			@RequestParam(required = false, name = "manualfeasibility") Boolean manualFeasibility) throws TclCommonException {
		QuoteBean response = iwanQuoteService.getQuoteDetails(quoteId, feasibleSites, siteproperties, siteId, manualFeasibility);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_TAX_EXEMPTION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderToLeDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderToLeDto> getAttributesAndSites(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId,
			@RequestParam(value = "attributeName", required = false) String attributeName) throws TclCommonException {
		OrderToLeDto response = iwanOrderService.getAttributesAndSites(orderToLeId, attributeName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited editOrderSites this method is
	 *            used to edit the order ill site info
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateLeaglEntityAttribute(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody List<UpdateRequest> request)
			throws TclCommonException {
		iwanOrderService.updateLegalEntityPropertiesIsv(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderProductComponent.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderProductComponentBean>> getSiteProperties(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
			@RequestParam(value = "filter", required = false) String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> response = iwanOrderService.getSiteProperties(siteId, attributeName);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderIllSiteBean> updateSiteProperties(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		OrderIllSiteBean response = iwanOrderService.updateSiteProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	@RequestMapping(value = "/orders/{orderId}/details/download", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<String> getOrderSummaryForExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws IOException, TclCommonException {
		iwanOrderService.getOrderSummaryForExcel(orderId, response);
		return null;

	}

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications getOrdersBasedOnLegalEntities used to get
	 *            distinct order status, distinct customerle details, distinct
	 *            product names for all orders
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_DISTINCT_DETAILS_FOR_FILTER)
	@RequestMapping(value = "/orders/distinctdetails", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<IsvFilterResponse> getOrdersBasedOnLegalEntities() throws IOException, TclCommonException {
		IsvFilterResponse isvFilterResponse = iwanOrderService.getOrdersForFilter();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, isvFilterResponse,
				Status.SUCCESS);
	}

	/**
	 * Get All Orders Based On Search
	 *
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ALL_ORDERS_BY_SEARCH)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/orders/search")
	public ResponseResource<PagedResult<OrdersBean>> getOrders(@RequestParam(required = false) String stage,
			@RequestParam(required = false) Integer productFamilyId,
			@RequestParam(required = false) Integer legalEntity, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size) throws TclCommonException {
		PagedResult<OrdersBean> response = iwanOrderService.getOrdersBySearch(stage, productFamilyId, legalEntity,
				startDate, endDate, page, size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
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
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload", method = RequestMethod.POST)
	public ResponseResource<TempUploadUrlInfo> uploadCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(name="file") MultipartFile file) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = iwanQuotePdfService.uploadCofPdf(quoteId, file, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUploadUrlInfo,
				Status.SUCCESS);
	}
	
	
	@RequestMapping(value = "/internal/file/temp", method = RequestMethod.GET)
	public ResponseResource<TempUploadUrlInfo> getTempUrl(@RequestParam("customerCode") String customerCode,@RequestParam("customerLeCode") String customerLeCode) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = fileStorageService.getTempUploadUrl(
				Long.parseLong(tempUploadUrlExpiryWindow),
				customerCode,
				customerLeCode,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUploadUrlInfo,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "/internal/file/documents", method = RequestMethod.POST)
	public ResponseResource<Map<String, Object>> uploadDocument(@RequestParam("outerCode") String outerCode,
			@RequestParam("innerCode") String innerCode, @RequestParam(name = "file") MultipartFile file,
			@RequestParam("fileName") String fileName)
			throws TclCommonException {
		Map<String, Object> response = dashboardService.processUploadDocument(outerCode, innerCode, file,fileName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "/common/document", method = RequestMethod.GET)
	public ResponseResource<String> downloadDocument(@RequestParam(name = "fileName",required = true) String fileName,
			@RequestParam(name="path",required = true) String path)
			throws TclCommonException {
		String response = dashboardService.getDownloadUrl(fileName, path);
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
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam(defaultValue="NEW") String type, HttpServletResponse response) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo;
		if(type.equalsIgnoreCase("RENEWALS")) {
			tempUploadUrlInfo = renewalsIllQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}else {
		 tempUploadUrlInfo = iwanQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUploadUrlInfo,
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
		iwanQuotePdfService.downloadCofPdf(quoteId, response);
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
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manual/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> approvedManualQuotes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletRequest httpServletRequest)
			throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		QuoteDetail response = iwanQuoteService.approvedManualQuotes(quoteId, forwardedIp);
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
	 * getDocusignStatus
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/quotes/{quoteId}/docusign/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Map<String, List<Map<String, Object>>>> getDocusignStatus(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		Map<String, List<Map<String, Object>>> response = docuSignService.getDocusignStatus(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getAuditDocusignDetails
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SFDC_GET)
	@RequestMapping(value = "/quotes/{quoteId}/sfdc/audit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SfdcAuditBean> getSfdcAudit(@PathVariable("quoteId") Integer quoteId,@RequestParam(required = false,name = "fullLog") Boolean fullLog )
			throws TclCommonException {
		SfdcAuditBean response = omsSfdcService.getSfdcAudit(quoteId,fullLog);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SFDC_TRIGGER)
	@RequestMapping(value = "/quotes/{quoteId}/sfdc", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deactivateSfdc(@PathVariable("quoteId") Integer quoteId,@RequestParam("action") String action) throws TclCommonException {
		omsSfdcService.actionSfdc(quoteId,action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SFDC_TRIGGER)
	@RequestMapping(value = "/sfdc/{tpId}/refid/{refId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTpsJob(@PathVariable("tpId") Integer tpsId,@PathVariable("refId") String refId,@RequestBody Map<String,String> request) throws TclCommonException {
		String response =omsSfdcService.updateTpsJob(tpsId,refId,request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);

	}

	/**
	 * 
	 * triggerSfdc
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SFDC_TRIGGER)
	@RequestMapping(value = "/quotes/{quoteId}/sfdc/trigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerSfdc(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		omsSfdcService.triggerSfdc(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	 

	/**
	 * @author ANANDHI VIJAY Delete Docusign Document by QuoteId
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
		QuotePriceAuditResponse response = iwanQuoteService.getQuotePriceAudit(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	 
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetailsExcel used to
	 *            fetch all the order details in excel sheet format
	 * @param orderId
	 * @return
	 * @throws IOException 
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_ORDER_EXCEL_DOWNLOAD)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/gsc/orders/{orderId}/download")
	public ResponseEntity<String> getGscOrderDetailsExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws TclCommonException, IOException {
		gscExportLRService.returnExcel(orderId, response);
		return null;

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
		OmsAttachmentBean omsAttachmentBean = iwanQuotePdfService.updateCofUploadedDetails(quoteId, quoteLeId, requestId, url);
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
		String tempDownloadUrl = iwanQuotePdfService.downloadCofFromStorageContainer(quoteId, quoteLeId, null,null,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}

 
     
    /**
     * 
     * Update Currency and Terms and condition against quote to le
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ALL_ORDERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/attributes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateCurrencyAnTermsInMonthForQuotes() throws TclCommonException {
		omsUtilService.changeCurrencyAndTermInMonthsAttributesToQuoteToLe();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
    
    /**
     * 
     * Update Currency and Terms and condition against order to le
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ALL_ORDERS)
   	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
   			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
   			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
   			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
   	@RequestMapping(value = "/orders/attributes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
   	public ResponseResource<String> updateCurrencyAnTermsInMonthForOrders() throws TclCommonException {
   		omsUtilService.changeCurrencyAndTermInMonthsAttributesToOrderToLe();
   		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ResponseResource.RES_SUCCESS,
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
	public ResponseResource<String> generateQuotePdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = docuSignService.getDocumentBasedOnQuoteCode(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

     	
	/**
	 * Get Manual feasiblity Report details
	 * @author ANANDHI VIJAY
	 * getManualFeasiblityReport
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@RequestMapping(value = "/manualfeasiblityreport", method = RequestMethod.GET)
	public ResponseResource<String> getManualFeasiblityReport(HttpServletResponse response) throws TclCommonException, IOException {
		reportService.getManualFeasiblityReport(response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
	/**
	 *  Get Commercial Report details
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@RequestMapping(value = "/commercialreport", method = RequestMethod.GET)
	public ResponseResource<String> getCommercialReport(HttpServletResponse response) throws TclCommonException, IOException {
		commercialReportService.getCommercialReport(response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
	
	/**
	 * Get Order to Delivery Tracking Report details
	 * @author MUTHUSELVI S
	 * getOrderToDeliveryTrackingReport
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@RequestMapping(value = "/ordertodeliverytrackingreport", method = RequestMethod.GET)
	public ResponseResource<String> getOrderToDeliveryTrackingReport(HttpServletResponse response) throws TclCommonException, IOException {
		//reportService.getOrderToDeliveryTracking(response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}


    /**
     * 
     * Download Customer feasiblity template for NPL
     * @param response
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.PROCESS_CUSTOM_FP)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/templates/fpdetails/custom", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> getCustomTemplateForNpl(HttpServletResponse response) throws TclCommonException {
		omsExcelService.downloadCustomFpTemplateForNpl(response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
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
		@RequestMapping(value = "/orders/{orderId}/le/{orderLeId}/manualcof/storage/downloadurl", method = RequestMethod.GET)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<String> DownloadCofBasedOnOrders(@PathVariable("orderId") Integer orderId,
				@PathVariable("orderLeId") Integer orderLeId) throws TclCommonException {
			String tempDownloadUrl = iwanQuotePdfService.downloadCofFromStorageContainer(null, null, orderId,orderLeId,null);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
					Status.SUCCESS);

		}
		
 
	/**
	 * 
	 * Updates Legal Entities for Ill
	 * @param quoteId
	 * @param quoteToLeId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteToLeId}/attributes" , method = RequestMethod.POST , consumes =MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<QuoteDetail> updateLegalEntityAttribute(@PathVariable("quoteId") Integer quoteId,
		@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody List<UpdateRequest> request) throws TclCommonException{
		iwanQuoteService.updateLegalEntityPropertiesIsvQuote(request, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	 
    
 	/**
	 * Sets the site as not feasible for Ill, Npl, IzoPc, Gvpn
	 * 
	 * @author Kavya Singh
	 * @param quoteToLeId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SITE_NOT_FEASIBLE)
	@RequestMapping(value = "/site/notfeasible", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> siteNotFeasible(@RequestBody QuoteSiteNotFeasibleBean request)
			throws TclCommonException {
		iwanQuoteService.siteNotFeasible(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}
	

 	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/priceDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveManualPriceDetails(@PathVariable("siteId") Integer siteId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody FPRequest fpRequest) throws TclCommonException {
		iwanPricingFeasibilityService.processManualPriceDetails(fpRequest, siteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * Used to save the discounted details
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.SAVE_DISCOUNT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/discount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processDiscountForPrice(@RequestBody PDRequest pdRequest) throws TclCommonException {
		iwanPricingFeasibilityService.processDiscount(pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
 	
	/**
	 * Used to approve the discounted price
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.APPROVE_DISCOUNT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/discount/approve", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Integer> approveDiscount(@PathVariable("quoteId") Integer quoteId, @RequestBody List<Integer> pdRequest) throws TclCommonException {
		Integer approvalLevel = iwanPricingFeasibilityService.processDiscountApproval(quoteId,pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				approvalLevel, Status.SUCCESS);
	}

 	/**
	 * @author ANANDHI VIJAY
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to update or add site properities
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateQuoteSiteProperties(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody SiteAttributeUpdateBean request) throws TclCommonException {
		iwanQuoteService.updateSiteAttributesIsv(request, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

 
	/**
	 * This api is used to get discount details for edited price
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_DISCOUNT_DETAILS)
	@RequestMapping(value = "/{quoteId}/discountdetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getDiscountDetails(@RequestBody PDRequest pdRequest) throws TclCommonException {
		String response = iwanPricingFeasibilityService.getDiscountedPrice(pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);

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
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/terminmonths/{taskId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonths(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest updateRequest,@PathVariable(required = false,value="taskId") Integer taskId) throws TclCommonException {
		iwanQuoteService.updateTermsInMonthsForQuoteToLe(quoteId,quoteToLeId,updateRequest,taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

 
 
 
	/**
	 * API to Get List of PartnerDocumentBean for Oppertunity
	 *
	 * @param OrderCode
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.DOWNLOAD_FILE_DOC)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "partner/opportunities/attachmentIds")
	public ResponseResource<List<PartnerDocumentBean>> getAttachmentIdsForOppertunity(@RequestParam("orderCode") String orderCode) throws TclCommonException {
		List<PartnerDocumentBean> response = partnerService.getAttachmentIdsForOppertunity(orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	/**
	 * API to download Opportunity Files
	 *
	 * @return {@link ServiceResponse}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.DOWNLOAD_FILE_DOC)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Resource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "partner/download/opportunities/files/{attachmentId}")
	public ResponseEntity<Resource> downloadOpportunityFileStorage(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		Resource response = partnerService.downloadOpportunityFileStorage(attachmentId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "attachment; filename=" + response.getFilename());
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return ResponseEntity.ok().headers(headers).body(response);
	}
	
	
	/**
	 * 
	 * RetriggerCreditCheck query
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREDITCHECK_RETRIGGER)
	@RequestMapping(value = "/quotes/{quoteId}/creditcheck/retrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<String> reTriggerCreditCheck(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		String creditCheckStatus = iwanQuoteService.retriggerCreditCheck(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				creditCheckStatus, Status.SUCCESS);

	}

	/**
	 * API to download via object storage
	 *
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.DOWNLOAD_FILE_DOC)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "partner/object/download/opportunities/files/{attachmentId}")
	public ResponseResource<String> downloadOpportunityObjectStorage(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		String response = partnerService.downloadOpportunityObjectStorage(attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	 	
	/**
	 * 
	 * Solution prioritize the input data
	 * @param inputData
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn/solution/prioritize", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> solutionPrioritizeTheRequest(@RequestBody List<Map<String, Object>> inputData) throws TclCommonException {
		List<Map<String, Object>> response = solutionPrioritizationService.solutionPrioritizeTheInput(inputData);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	 

	/**
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ThirdPartyService.CREATE_OPTY_RETRIGGER)
	@RequestMapping(value = "/{quoteId}/createoptyretrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> createOptyRetrigger(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		omsSfdcService.createOptyRetrigger(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ThirdPartyService.CREATE_PRODUCT_RETRIGGER)
	@RequestMapping(value = "/{quoteId}/createproductretrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> createProductRetrigger(@PathVariable("quoteId") Integer quoteId,@RequestParam(name="optyId",required=false) String optyId) throws TclCommonException {
		omsSfdcService.createProductRetrigger(quoteId,optyId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	/**
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/cof/replace", method = RequestMethod.POST)
	public ResponseResource<String> replaceCof(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(name="file") MultipartFile file) throws TclCommonException {
		String cofResponse = iwanQuotePdfService.replaceCofPdf(quoteId, file, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, cofResponse,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * @param orderId
	 * @param orderLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/o2c", method = RequestMethod.POST)
	public ResponseResource<Boolean> triggerOrderToCash(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		Boolean status = dashboardService.processOrderToCash(orderId, orderLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				status, Status.SUCCESS);
	}
	
	
	/**
	 * 
	 * getServiceBean
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SERVICE_REQUEST)
	@RequestMapping(value = "/{quoteId}/servicerequest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<String>> getServiceRequest(@PathVariable("quoteId") Integer quoteId, @RequestParam(value ="serviceType") String serviceType) throws TclCommonException {
		List<String> response = iwanQuoteService.getServiceRequest(quoteId, serviceType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	/**
	 * 
	 * THis methos is used to update Commercial Value Manually
	 * @param ManualCommercialUpdate
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/manualcommercialupdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateCommercialManual(@RequestBody ManualCommercialUpdate manualCommercialUpdate)
			throws TclCommonException {
		iwanPricingFeasibilityService.processFinalApprovalManual(manualCommercialUpdate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	/**
	 * 
	 * THis methos is used to update prie value in cross connect npl
	 * @param ManualCommercialUpdate
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/crossconnect/{quoteId}/le/{quoteLeId}/sites/{siteId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getManualFPCrossConnect(@PathVariable("siteId") Integer siteId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody FPRequest fpRequest) throws TclCommonException {
		crossConnectPricingFeasibilityService.processManualFP(fpRequest, siteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	 
	/**
	*
	* getTnc
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
	@RequestMapping(value="/quote/{quoteId}/le/{quoteLeId}/tnc",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<QuoteTncBean> getTnc(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		QuoteTncBean response = iwanQuoteService.getTnc(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_TNC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="/quote/{quoteId}/le/{quoteLeId}/tnc",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateTnc(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteTncBean quoteTncBean) throws TclCommonException {
		String response = iwanQuoteService.processTnc(quoteId, quoteTncBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	

	/**
	*
	* updateRejectionStatus site level
	* @throws TclCommonException
	*/
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_TNC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="/commercial/site/rejection",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateSiteRejectionSatus(@RequestBody CommercialRejectionBean commercialRejectionBean) throws TclCommonException {
		iwanQuoteService.updateSiteRejectionSatus(commercialRejectionBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	/**
	*
	* updateRejectionStatus
	* @throws TclCommonException
	*/
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_TNC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })

	@RequestMapping(value="/commercial/quote/rejection",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateQuoteRejectionSatus(@RequestBody CommercialRejectionBean commercialRejectionBean) throws TclCommonException {
		iwanQuoteService.updateQuoteRejectionSatus(commercialRejectionBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOCUSIGN_RETRY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="/quote/{quoteId}/le/{quoteLeId}/docusign/retry",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> docusignRetry(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		String response = docuSignService.retryDocusignNotification(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_GST_ADDRESS_DTL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="/address",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<GstAddressBean> getGstAddressDetail(@RequestParam("gstin") String gstin) throws TclCommonException {
		GstAddressBean gstAddressBean = new GstAddressBean();
		LOGGER.info("Inside getGstAddressDetailsss");
		try {
			gstInService.getGstAddress(gstin, gstAddressBean);
		} catch (ParseException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstAddressBean,
				Status.SUCCESS);
	}
	
	/**
	 * API to Get List of feasibility ID attachment ids
	 *
	 * @param OrderCode
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ManualFeasibility.GET_MF_ATTACHMENT)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/mf/getmfl2oReport")
	public ResponseResource<List<MfL2OReportBean>> getAttachmentIdForFeasibilityIds(@RequestBody List<String> fIds) throws TclCommonException {
		List<MfL2OReportBean> response = iwanQuoteService.getOmsAttachments(fIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * @param fIds
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ManualFeasibility.GET_MF_ATTACHMENT)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/notify/mail")
	public ResponseResource<String> notifyMail() throws TclCommonException {
		notificationService.customerPortalInitialNotify("Manoj Kumar", "manojkumar.rajakumaran@tatacommunications.com", "NPL270420RCGUGDJ", "NPL");
		notificationService.customerPortalMidNotify("Manoj Kumar", "manojkumar.rajakumaran@tatacommunications.com", "NPL270420RCGUGDJ", "NPL","https://optimus-uat.tatacommunications.com/optimus/");
		notificationService.customerPortalCreditNotify("Manoj Kumar", "manojkumar.rajakumaran@tatacommunications.com", "NPL270420RCGUGDJ", "Tata Technologies");
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "success", Status.SUCCESS);
	}
	

 

	 
	

	/**
	 * This API is to download the MIS feasibility Report for a given from date and to date
	 * @author Phaniteja P
	 * @param response
	 * @param fromDate
	 * @param toDate
	 * @returns ResponseResource
	 * @throws IOException and TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "npl/mis/report/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getMisReportingExcel(
			HttpServletResponse response,
			@RequestParam @DateTimeFormat(pattern="yyyy/MM/dd") Date fromDate,
			@RequestParam @DateTimeFormat(pattern="yyyy/MM/dd") Date toDate) throws IOException, TclCommonException {
		reportService.returnMisReportExcel(response,fromDate,toDate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	 

	@RequestMapping(value = "/duplicate/quote/{quoteId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> notifyFeasibilityCheck(@PathVariable("quoteId") Integer quoteId){
		Boolean response = iwanPricingFeasibilityService.patchRemoveDuplicatePrice(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);

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
	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/terminmonths", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonths(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest updateRequest)
			throws TclCommonException {
		iwanQuoteService.updateTermsInMonthsForQuoteToLe(quoteId, quoteToLeId, updateRequest, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	 
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DASH_BOARD_DETAILS)
	@RequestMapping(value = "/dashboard/quotes/{quoteId}/access", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE )
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> quoteAccess(@PathVariable("quoteId") Integer quoteId,@RequestBody QuoteAccessBean quoteAccessBean){
		dashboardService.processQuoteAccess(quoteId,quoteAccessBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),
				Status.SUCCESS);

	}


	 

	// MOVE TO FEASIBILITY CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/quotes/trigger/feasiblities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerForFeasibilityBean(@RequestBody FeasibilityBean request)
			throws TclCommonException {
		iwanPricingFeasibilityService.processFeasibility(request.getLegalEntityId());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

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
	@RequestMapping(value = "/quotes/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteLeAttributeBean> feasibilityCheck(@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuoteLeAttributeBean response = iwanQuoteService.checkQuoteLeFeasibility(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_FEASIBILITY)
	@RequestMapping(value = "/opty/update/sitelocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateSiteLocationInSfdcReq(@RequestBody List<String> quoteCodes)
			throws TclCommonException {
		cancellationService.updatePosLocationIdInUpdateSiteReq(quoteCodes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "",
				Status.SUCCESS);

	}
	
//	/**
//	 * Upload multi site commercial excel
//	 *
//	 * @param quoteToLeId
//	 * @return FileUrlResponse
//	 * @throws TclCommonException
//	 */
//	@RequestMapping(value = "/multisite/bulkupload", method = RequestMethod.POST)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = FileUrlResponse.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	public ResponseResource<FileUrlResponse> uploadbulksiteExcel(
//			@RequestParam(name="file") MultipartFile file,
//			@RequestParam("quoteToLeId") Integer quoteToLeId,
//		    @RequestParam("quoteId") Integer quoteId,
//		    @RequestParam("taskId") Integer taskId,
//		    @RequestParam("productName") String productName) throws TclCommonException {
//		FileUrlResponse urlInfo = iwanQuotePdfService.processMultiSiteExcel(quoteId,file, quoteToLeId,taskId,productName);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, urlInfo,
//				Status.SUCCESS);
//	}
//	/**
//	 * This API is to download ILL GVPN Multisite report
//	 * @author Phaniteja P
//	 * @param response
//	 * @param quoteCode
//	 * @returns ResponseResource
//	 * @throws IOException and TclCommonException
//	 */
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@RequestMapping(value = "ill/gvpn/multisite/report/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> getIllGvpnMultiSiteExcel(
//			HttpServletResponse response,@RequestParam("quoteCode") String quoteCode) throws IOException, TclCommonException {
//		iwanQuoteService.returnIllGvpnMultisiteReportExcel(response,quoteCode);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response.getHeader("Content-Disposition"),
//				Status.SUCCESS);
//	}
	



	
	
//	/**
//	 * update ExcelFileDetails
//	 *
//	 * @param quoteToLeId
//	 * @return FileUrlResponse
//	 * @throws TclCommonException
//	 */
//	@RequestMapping(value = "multisite/documentuploaded", method = RequestMethod.POST)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = FileUrlResponse.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	public ResponseResource<FileUrlResponse> updateExcelFileDetails(
//		    @RequestParam("referenceId") Integer referenceId,
//			@RequestParam("referenceName") String referenceName, 
//			@RequestParam("attachmentType") String attachmentType,
//			@RequestParam("requestId") String requestId,
//			@RequestParam("attachmentId") Integer attachementId,
//			@RequestParam("quoteToLeId") Integer quoteToLeId,
//			@RequestParam(value = "url") String url) throws TclCommonException {
//		FileUrlResponse response = iwanQuotePdfService.updateDocumentUploadedDetails(quoteToLeId, referenceId,
//				referenceName, requestId, attachmentType, url,attachementId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//
//	}
//	
//	/**
//	 * Method to get the temporary download url for other documents uploaded to the
//	 * storage container
//	 * @param attachmentId
//	 * @return
//	 * @throws TclCommonException
//	 */
//	@RequestMapping(value = "/multisite/tempdownloadurl/documents/{attachmentId}", method = RequestMethod.GET)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	public ResponseResource<String> getTempDownloadUrlForDocuments(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
//		String tempDownloadUrl = iwanQuotePdfService.getTempDownloadUrlForDocuments(attachmentId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
//				Status.SUCCESS);
//
//	}
//
// 	
//
//	/**
//	 * API to download Opportunity Files
//	 *
//	 * @return {@link ServiceResponse}
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Partner.DOWNLOAD_FILE_DOC)
//	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Resource.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
//	@GetMapping(value = "multisite/download/files/{attachmentId}")
//	public ResponseEntity<Resource> dowloadfilestorageExcel(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
//		Resource file = iwanQuotePdfService.downloadCommercialFileStorage(attachmentId);
//		if (file == null) {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			return ResponseEntity.ok().headers(headers).body(file);
//		}
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		headers.add("Access-Control-Allow-Origin", "*");
//		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
//		headers.add("Access-Control-Allow-Headers", "Content-Type");
//		headers.add("Content-Disposition", "filename=" + file.getFilename());
//		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//		headers.add("Pragma", "no-cache");
//		headers.add("Expires", "0");
//		return ResponseEntity.ok().headers(headers).body(file);
//	}
//	
//	/** To upload the updated excel of ILL GVPN multi site details 
//	 * @author Phaniteja P
//	 * @param quoteId
//	 * @param quoteLeId
//	 * @return
//	 * @throws TclCommonException
//	 * @throws IOException 
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.MULTISITE_ILL_GVPN_FILE_UPLOAD)
//	@RequestMapping(value = "/multisite/ill/gvpn/file/upload", method = RequestMethod.POST)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	public ResponseResource<String> uploadIllGvpnMultiSiteReport(@RequestParam("file") MultipartFile file, 
//			@RequestParam("approvalLevel")String approvalLevel,@RequestParam("attachmentId")Integer attachmentId,
//			@RequestParam("quoteCode")String quoteCode,
//			@RequestParam("taskId")Integer taskId) throws TclCommonException, IOException {
//		iwanQuoteService.uploadIllGvpnMultiSiteExcel(file,approvalLevel,attachmentId,quoteCode,taskId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
//				Status.SUCCESS.toString(), Status.SUCCESS);
//	}
//	
// 	
//	/** To get the status of ill gvpn multisite information 
//	 * @author Nithya S
//	 * @param quoteCode
//	 * @return
//	 * @throws TclCommonException
//	 * @throws IOException 
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.MULTISITE_ILL_GVPN_STATUS)
//	@RequestMapping(value = "/multisite/ill/gvpn/status", method = RequestMethod.GET)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MultiSiteStatusBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	public ResponseResource<MultiSiteStatusBean> getMultiSiteStatusForIllGvpn(@RequestParam("quoteCode")String quoteCode)
//			 throws TclCommonException, IOException {
//		MultiSiteStatusBean multiSiteStatusBean = iwanQuoteService.getMultiSiteStatusForIllGvpn(quoteCode);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, multiSiteStatusBean, Status.SUCCESS);
//	}
//	
//	@RequestMapping(value = "/patch/ordercomponent", method = RequestMethod.POST)
//	public ResponseResource<String> patchComponents(
//			@RequestParam(value = "quoteRefId", required = true) Integer quoteReferenceId,
//			@RequestParam(value = "orderRefId", required = true) Integer orderReferenceId,
//			@RequestParam(value = "quoteRefName", required = true) String quoteReferenceName,
//			@RequestParam(value = "componentName", required = false) String componentName)
//			throws TclCommonException {
//		iwanQuoteService.patchCopyComponents(quoteReferenceId, quoteReferenceName, orderReferenceId,componentName);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
//				Status.SUCCESS.toString(), Status.SUCCESS);
//	}

}
