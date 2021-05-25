package com.tcl.dias.oms.isv.controller.v1;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.oms.beans.OrderIllSitetoServiceBean;
import com.tcl.dias.oms.beans.QuoteIllSiteToServiceBean;
import com.tcl.dias.oms.cisco.beans.ShowQuoteType;

import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.gsc.beans.*;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEAttributeService;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEOrderService;

import com.tcl.dias.oms.gsc.beans.GscSipTrunkAttributeBean;
import com.tcl.dias.oms.gsc.beans.GscSpecialTermsConditionsBean;

import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEQuoteService;
import com.tcl.dias.oms.gsc.service.v1.GlobalOutboundRateCardService;

import com.tcl.dias.oms.teamsdr.beans.TeamsDRManualPriceBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRQuoteDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRUnitPriceBean;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDROrderService;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRPdfService;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRPricingFeasibilityService;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRQuoteService;
import com.tcl.dias.oms.webex.util.AuthBean;
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
import org.springframework.transaction.annotation.Propagation;
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

import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.DocusignAuditBean;
import com.tcl.dias.common.beans.FileUrlResponse;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.ManualCommercialUpdate;
import com.tcl.dias.common.beans.MfL2OReportBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceDetailedInfoBean;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.beans.ThirdPartyServiceJobBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.common.sfdc.response.bean.BundledOpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.OpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductServicesResponseBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.CommercialRejectionBean;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.IsvFilterResponse;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.LinkFeasibilityManualBean;
import com.tcl.dias.oms.beans.ManualFeasibilityRequest;
import com.tcl.dias.oms.beans.MultiSiteStatusBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrderIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.OrderLinkRequest;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderSiteRequest;
import com.tcl.dias.oms.beans.OrderSummaryBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.PDRequest;
import com.tcl.dias.oms.beans.QuoteAccessBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteComponentAttributeUpdateRequest;
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
import com.tcl.dias.oms.beans.SiteFeasibilityManualBean;
import com.tcl.dias.oms.beans.TerminationWaiverRequest;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.consumer.SfdcResponseConsumer;
import com.tcl.dias.oms.crossconnect.service.v1.CrossConnectPricingFeasibilityService;
import com.tcl.dias.oms.dashboard.service.v1.DashboardService;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.dto.OrderToLeDto;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gde.beans.FeasibilityCheckResponse;
import com.tcl.dias.oms.gde.beans.GdeOrdersBean;
import com.tcl.dias.oms.gde.beans.GdePricingFeasibilityBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteBean;
import com.tcl.dias.oms.gde.pdf.service.GdeQuotePdfService;
import com.tcl.dias.oms.gde.service.v1.GdeOrderService;
import com.tcl.dias.oms.gde.service.v1.GdePricingFeasibilityService;
import com.tcl.dias.oms.gde.service.v1.GdeQuoteService;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscAttachmentBean;
import com.tcl.dias.oms.gsc.beans.GscManualPricing;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.common.GscPdfHelper;
import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;
import com.tcl.dias.oms.gsc.service.v1.GscExportLRService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.gsc.service.v1.GscProductCatalogService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.gsc.service.v1.GscSlaService;
import com.tcl.dias.oms.gsc.service.v2.GscPricingFeasibilityService2;
import com.tcl.dias.oms.gsc.service.v2.GscQuoteAttributeService2;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.gvpn.macd.service.v1.GvpnMACDService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.ill.macd.service.v1.IllMACDService;
import com.tcl.dias.oms.ill.service.v1.IllOrderService;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.ipc.beans.IPCOrdersBean;
import com.tcl.dias.oms.ipc.beans.pricebean.IpcCommercialBean;
import com.tcl.dias.oms.ipc.beans.pricebean.IpcDiscountBean;
import com.tcl.dias.oms.ipc.beans.pricebean.PricingBean;
import com.tcl.dias.oms.ipc.service.v1.IPCCommercialService;
import com.tcl.dias.oms.ipc.service.v1.IPCOrderService;
import com.tcl.dias.oms.ipc.service.v1.IPCPricingService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuotePdfService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuoteService;
import com.tcl.dias.oms.iwan.service.v1.IwanQuoteService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcOrderService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcPricingFeasibilityService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuotePdfService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuoteService;
import com.tcl.dias.oms.macd.beans.TerminationResponse;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplPricingFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.pdf.service.IllQuotePdfService;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.renewals.service.RenewalsGvpnQuotePdfService;
import com.tcl.dias.oms.renewals.service.RenewalsIllQuotePdfService;
import com.tcl.dias.oms.renewals.service.RenewalsNplQuotePdfService;
import com.tcl.dias.oms.service.CommercialReportService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsExcelService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.service.ReportService;
import com.tcl.dias.oms.service.v1.BundleOmsSfdcService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcUtilService;
import com.tcl.dias.oms.solution.prioritization.service.SolutionPrioritizationService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.dias.oms.termination.service.v1.TerminationService;
import com.tcl.dias.oms.termination.service.v1.TerminationWaiverService;
import com.tcl.dias.oms.webex.beans.WebexLicenseManualPricingBean;
import com.tcl.dias.oms.webex.beans.WebexOrderDataBean;
import com.tcl.dias.oms.webex.beans.WebexQuoteDataBean;
import com.tcl.dias.oms.webex.service.WebexExportLRService;
import com.tcl.dias.oms.webex.service.WebexOrderService;
import com.tcl.dias.oms.webex.service.WebexPricingFeasibilityService;
import com.tcl.dias.oms.webex.service.WebexQuotePdfService;
import com.tcl.dias.oms.webex.service.WebexQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;

/**
 * This file contains the InternalStakeViewController.java class.
 * 
 *
 * @author VIVEK KUMAR Kp
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/isv/v1")
public class InternalStakeViewController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InternalStakeViewController.class);

	@Autowired
	IzoPcPricingFeasibilityService izoPcPricingFeasibilityService;

	@Autowired
	IllOrderService illOrderService;

	@Autowired
	IllQuoteService illQuoteService;

	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;

	@Autowired
	IllQuotePdfService illQuotePdfService;
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Autowired
	IPCOrderService ipcOrderService;

	@Autowired
	IPCPricingService ipcPricingService;
	
	@Autowired
	IPCQuotePdfService ipcQuotePdfService;
	
	@Autowired
	IPCQuoteService ipcQuoteService;
	
	@Autowired
	GscQuoteAttributeService gscQuoteAttributeService;

	/** NPL related Dependency starts */

	@Autowired
	NplOrderService nplOrderService;

	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;

	@Autowired
	NplQuotePdfService nplQuotePdfService;

	@Autowired
	IzoPcQuotePdfService izoPcQuotePdfService;

	@Autowired
	NplQuoteService nplQuoteService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;
	
	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;

	@Autowired
	GvpnOrderService gvpnOrderService;

	@Autowired
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;

	@Autowired
	GvpnQuotePdfService gvpnQuotePdfService;

	@Autowired
	UserService userService;

	@Autowired
	GscOrderService gscOrderService;

	@Autowired
	GscQuotePdfService gscQuotePdfService;

	@Autowired
	GscQuoteService gscQuoteService;

	@Autowired
	IzoPcOrderService izoPcOrderService;

	@Autowired
	GscPricingFeasibilityService gscPricingFeasibilityService;

	@Autowired
	OmsExcelService omsExcelService;

	@Autowired
	GscPdfHelper gscPdfHelper;

	@Autowired
	DocusignService docuSignService;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	IzoPcQuoteService izoPcQuoteService;

	@Autowired
	GscOrderDetailService gscOrderDetailService;
	
	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	GscSlaService gscSlaService;

	@Autowired
	GscExportLRService gscExportLRService;
	
	@Autowired
	ReportService reportService;

	@Autowired
    GscProductCatalogService gscProductCatalogService;

	@Autowired
	CommercialReportService commercialReportService;

	@Autowired
	PartnerService partnerService;
	
	@Autowired
	SolutionPrioritizationService solutionPrioritizationService;
	
	@Autowired
	DashboardService dashboardService;
	
	@Autowired
	IllMACDService illMACDService;
	
	@Autowired
	CrossConnectPricingFeasibilityService crossConnectPricingFeasibilityService;
	
	@Autowired
	GstInService gstInService;
	
	@Autowired
	GdeOrderService gdeOrderService;
	
	@Autowired
	GdeQuotePdfService gdeQuotePdfService;
	
	@Autowired
	GdePricingFeasibilityService gdePricingFeasibilityService;
	
	@Autowired
	GdeQuoteService gdeQuoteService;
	
	@Autowired
	NotificationService notificationService;

	@Autowired
	WebexQuoteService webexQuoteService;

	@Autowired
	GscQuoteAttributeService2 gscQuoteAttributeService2;

	@Autowired
	GscPricingFeasibilityService2 gscPricingFeasibilityService2;

	@Autowired
	WebexOrderService webexOrderService;

	@Autowired
	WebexQuotePdfService webexQuotePdfService;

	@Autowired
	WebexExportLRService webexExportLRService;

	@Autowired
	WebexPricingFeasibilityService webexPricingFeasibilityService;

	@Autowired
	GlobalOutboundRateCardService globalOutboundRateCardService;
	
	@Autowired
	private IPCCommercialService iPCCommercialService;

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
	TerminationWaiverService terminationWaiverService;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Autowired
	TeamsDROrderService teamsDROrderService;

	@Autowired
	TeamsDRPricingFeasibilityService teamsDRPricingFeasibilityService;

	@Autowired
	TeamsDRPdfService teamsDRPdfService;

	@Autowired
	GscMultiLEOrderService gscMultiLEOrderService;
	
	@Autowired
	RenewalsGvpnQuotePdfService renewalsGvpnQuotePdfService;
	
	@Autowired
	RenewalsNplQuotePdfService renewalsNplQuotePdfService;
	
	@Autowired
	GvpnMACDService gvpnMACDService;
	
	@Autowired
	TerminationService terminationService;

	@Autowired
	GscMultiLEAttributeService gscMultiLEAttributeService;
	
	@Autowired
	OmsSfdcUtilService omsSfdcUtilService;

	@Autowired
	GscMultiLEQuoteService gscMultiLEQuoteService;

	/** NPL related Dependency end */

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
		List<OrdersBean> response = illOrderService.getAllOrders();
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
		CommonValidationResponse response = illQuoteService.processValidateMandatoryAttr(quoteId, quoteToLeId);
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
		illOrderService.returnExcel(orderId, response);
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
		String tempDownloadUrl = illQuotePdfService.processApprovedCof(orderId, orderLeId, response, false);
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
		List<OrderSiteProvisionAudit> response = illOrderService.updateOrderSiteStatus(siteId, request);
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
		List<OrderSummaryBean> response = illOrderService.getOrderSummary();
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
		PagedResult<OrderSummaryBean> response = illOrderService.getOrderSummary(page, size, searchText, customerId,
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
		OrdersBean response = illOrderService.getOrderDetails(orderId);
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
		OrderIllSitesWithFeasiblityAndPricingBean response = illOrderService
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
		QuoteIllSitesWithFeasiblityAndPricingBean response = illOrderService
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
		illPricingFeasibilityService.processManualFP(fpRequest, siteId, quoteLeId);
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
		illPricingFeasibilityService.processCustomFP(siteId, quoteId, quoteLeId, response, file);
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
		illPricingFeasibilityService.processManualFeasibility(manualfRequest, siteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * api for editing feasibility response attributes manually
	 * 
	 * @param siteId
	 * @param quoteId
	 * @param quoteLeId
	 * @param manualfRequest
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.EDIT_FEASIBLITY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "gvpn/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/manualfdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> editGvpnManualFeasibility(@PathVariable("siteId") Integer siteId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody ManualFeasibilityRequest manualfRequest) throws TclCommonException {
		gvpnPricingFeasibilityService.processManualFeasibility(manualfRequest, siteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * This api is to edit the feasibility and pricing params manually.
	 * 
	 * @param linkId
	 * @param quoteId
	 * @param quoteLeId
	 * @param manualfRequest
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.EDIT_FEASIBLITY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "npl/quotes/{quoteId}/le/{quoteLeId}/links/{linkId}/manualfdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> editNplManualFeasibility(@PathVariable("linkId") Integer linkId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody com.tcl.dias.oms.npl.pricing.bean.ManualFeasibilityRequest manualfRequest)
			throws TclCommonException {
		nplPricingFeasibilityService.processManualFeasibility(manualfRequest, linkId, quoteLeId);
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
		illPricingFeasibilityService.notifyManualFeasibility(quoteId);
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
		List<QuoteSummaryBean> response = illQuoteService.getQuoteSummary();
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
		PagedResult<QuoteSummaryBean> response = illQuoteService.getQuoteSummary(page, size, searchText, customerId, legalEntityId);
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
		QuoteBean response = illQuoteService.getQuoteDetails(quoteId, feasibleSites, siteproperties, siteId, manualFeasibility);
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
		OrderToLeDto response = illOrderService.getAttributesAndSites(orderToLeId, attributeName);
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
		illOrderService.updateLegalEntityPropertiesIsv(request);
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
		List<OrderProductComponentBean> response = illOrderService.getSiteProperties(siteId, attributeName);

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
		OrderIllSiteBean response = illOrderService.updateSiteProperties(request);
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
		illOrderService.getOrderSummaryForExcel(orderId, response);
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
		IsvFilterResponse isvFilterResponse = illOrderService.getOrdersForFilter();
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
		PagedResult<OrdersBean> response = illOrderService.getOrdersBySearch(stage, productFamilyId, legalEntity,
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
		TempUploadUrlInfo tempUploadUrlInfo = illQuotePdfService.uploadCofPdf(quoteId, file, quoteToLeId);
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
		 tempUploadUrlInfo = illQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
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
		illQuotePdfService.downloadCofPdf(quoteId, response);
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
		QuoteDetail response = illQuoteService.approvedManualQuotes(quoteId, forwardedIp);
		if (o2cEnableFlag.equalsIgnoreCase("true")) {
			LOGGER.info("Entering order to flat table as the flag set was :::: {}", o2cEnableFlag);
			//illQuoteService.processOrderFlatTable(response.getOrderId());
		}else {
			LOGGER.info("Order flat table is disabled");
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	/** Npl related service handler Starts here */

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
	 *            the order details
	 * @param orderId
	 * @return NplPricingFeasibilityBean
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_NPL_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = NplPricingFeasibilityBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/orders/links/{linkId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NplPricingFeasibilityBean> getFeasiblityAndPricingDetailsForOrderNplLink(
			@PathVariable("linkId") Integer linkId) throws TclCommonException {
		NplPricingFeasibilityBean response = nplOrderService.getFeasiblityAndPricingDetails(linkId);
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
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_NPL_ORDER_EXCEL_DOWNLOAD)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = NplPricingFeasibilityBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/orders/{orderId}/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getNplOrderDetailsExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws IOException, TclCommonException {
		nplOrderService.returnExcel(orderId, response);
		return null;

	}

	/**
	 * Biswajit
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateOrderSiteStatus this method
	 *            is used to update the status of order Ill sites
	 * @param orderIlllinkId,
	 *            status
	 * @return ResponseResource
	 * @throws TclCommonException
	 * 
	 * @ApiOperation(value =
	 *                     SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	 * @RequestMapping(value =
	 *                       "npl/orders/{orderId}/le/{orderToLeId}/links/{linkId}/stage",
	 *                       method = RequestMethod.POST, consumes =
	 *                       MediaType.APPLICATION_JSON_VALUE, produces =
	 *                       MediaType.APPLICATION_JSON_VALUE)
	 * @ApiResponses(value = {
	 * @ApiResponse(code = 200, message = Constants.SUCCESS, response =
	 *                   Boolean.class),
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) }) public
	 *                   ResponseResource<Boolean>
	 *                   updateOrderLinkStatus( @PathVariable("orderId") Integer
	 *                   orderId, @PathVariable("orderToLeId") Integer
	 *                   orderToLeId, @PathVariable("linkId") Integer
	 *                   linkId, @RequestBody OrderLinkRequest request) throws
	 *                   TclCommonException { boolean response =
	 *                   nplOrderService.updateOrderLinkStatus(linkId, request);
	 *                   return new ResponseResource<>(ResponseResource.R_CODE_OK,
	 *                   ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	 * 
	 *                   }
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/quotes/links/{linkId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NplPricingFeasibilityBean> getFeasiblityAndPricingDetailsForQuoteNplLink(
			@PathVariable("linkId") Integer linkId) throws TclCommonException {
		NplPricingFeasibilityBean response = nplOrderService.getFeasiblityAndPricingDetailsForQuoteNplBean(linkId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/quotes/{quoteId}/le/{quoteLeId}/links/{linkId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> getNplManualFP(@PathVariable("linkId") Integer linkId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody FPRequest fpRequest) throws TclCommonException {
		nplPricingFeasibilityService.processManualFP(fpRequest, linkId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
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
	@RequestMapping(value = "/npl/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<NplOrdersBean>> getAllNplOrders() throws TclCommonException {
		List<NplOrdersBean> response = nplOrderService.getAllOrders();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
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
	@RequestMapping(value = "/npl/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateNplCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		nplQuotePdfService.processApprovedCof(orderId, orderLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateOrderSiteStatus this method
	 *            is used to update the status of order Ill sites
	 * @param orderIlllinkId,
	 *            status
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@RequestMapping(value = "/npl/orders/{orderId}/le/{orderToLeId}/links/{linkId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> updateNplOrderSiteStatus(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("linkId") Integer linkId,
			@RequestBody OrderLinkRequest request) throws TclCommonException {
		Boolean response = nplOrderService.updateOrderLinkStatus(linkId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
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
	@RequestMapping(value = "/npl/orders/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OrderSummaryBean>> getNplOrderSummary() throws TclCommonException {
		List<OrderSummaryBean> response = nplOrderService.getOrderSummary();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/*
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_SUMMARY)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
	 * response = OrderSummaryBean.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 * 
	 * @RequestMapping(value = "npl/orders/all", method = RequestMethod.GET,
	 * produces = MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseResource<PagedResult<OrderSummaryBean>>
	 * getNplPageOrderSummary(@RequestParam("page") Integer page,
	 * 
	 * @RequestParam("size") Integer size, @RequestParam(required = false, name =
	 * "searchText") String searchText) throws TclCommonException {
	 * PagedResult<OrderSummaryBean> response =
	 * nplOrderService.getOrderSummary(page, size, searchText); return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * response, Status.SUCCESS); }
	 */

	/**
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
	@RequestMapping(value = "/npl/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NplOrdersBean> getNplOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		NplOrdersBean response = nplOrderService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications getAllOrders used to get all orders for
	 *            stake holder
	 * @throws TclCommonException
	 */
	/*
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
	 * response = QuoteSummaryBean.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 * 
	 * @RequestMapping(value = "/npl/quotes/summary", method = RequestMethod.GET,
	 * produces = MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseResource<List<QuoteSummaryBean>> getNplQuoteSummary() throws
	 * TclCommonException { List<QuoteSummaryBean> response =
	 * nplQuoteService.getQuoteSummary(); return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * response, Status.SUCCESS); }
	 */

	/*
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
	 * response = QuoteSummaryBean.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 * 
	 * @RequestMapping(value = "/npl/quotes/all", method = RequestMethod.GET,
	 * produces = MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseResource<PagedResult<QuoteSummaryBean>>
	 * getNplPageQuoteSummary(@RequestParam("page") Integer page,
	 * 
	 * @RequestParam("size") Integer size, @RequestParam(required = false, name =
	 * "searchText") String searchText) throws TclCommonException {
	 * PagedResult<QuoteSummaryBean> response =
	 * nplQuoteService.getQuoteSummary(page, size, searchText); return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * response, Status.SUCCESS); }
	 */

	/**
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
	@RequestMapping(value = "/npl/quotes/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<NplQuoteBean> getNplQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,@RequestParam(required = false, name = "siteproperties") Boolean siteproperties) throws TclCommonException {
		NplQuoteBean response = nplQuoteService.getQuoteDetails(quoteId, feasibleSites,siteproperties);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/*
	 * @RequestMapping(value = "/npl/orders/{orderId}/le/{orderToLeId}/attributes",
	 * method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.Orders.GET_TAX_EXEMPTION_DETAILS)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
	 * response = OrderToLeDto.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) }) public
	 * ResponseResource<OrderToLeDto>
	 * getNplAttributesAndSites(@PathVariable("orderId") Integer orderId,
	 * 
	 * @PathVariable("orderToLeId") Integer orderToLeId,
	 * 
	 * @RequestParam(value = "attributeName", required = false) String
	 * attributeName) throws TclCommonException { OrderToLeDto response =
	 * nplOrderService.getAttributesAndSites(orderToLeId, attributeName); return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * response, Status.SUCCESS); }
	 */

	/**
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited editOrderSites this method is
	 *            used to edit the order ill site info
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/npl/orders/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> updateNplLeaglEntityAttribute(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody List<UpdateRequest> request)
			throws TclCommonException {
		nplOrderService.updateLegalEntityPropertiesIsv(request,orderToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/npl/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderProductComponentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderProductComponentBean>> getNplSiteProperties(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("siteId") Integer siteId,
			@RequestParam(value = "filter", required = false) String attributeName,
			@RequestParam(value = "type", required = false) String type) throws TclCommonException {
		List<OrderProductComponentBean> response = nplOrderService.getSiteProperties(siteId, attributeName, type);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/npl/orders/{orderId}/le/{orderToLeId}/links/{linkId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateNplLinkProperties(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("linkId") Integer linkId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		String response = nplOrderService.updateLinkDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_SITES)
	@RequestMapping(value = "/npl/orders/{orderId}/le/{orderToLeId}/sites", method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<NplQuoteDetail> updateNplOrderSites(
			@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		NplQuoteDetail response = nplOrderService.updateOrderSites(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

		/*
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	 * 
	 * @RequestMapping(value = "/npl/orders/{orderId}/details/download", method =
	 * RequestMethod.GET)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message =
	 * Constants.SUCCESS),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) }) public
	 * ResponseEntity<String> getNplOrderSummaryForExcel(@PathVariable("orderId")
	 * Integer orderId, HttpServletResponse response) throws IOException,
	 * TclCommonException { nplOrderService.getOrderSummaryForExcel(orderId,
	 * response); return null;
	 * 
	 * }
	 */

	/**
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications getOrdersBasedOnLegalEntities used to get
	 *            distinct order status, distinct customerle details, distinct
	 *            product names for all orders
	 * @throws TclCommonException
	 */
	/*
	 * @ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.
	 * GET_DISTINCT_DETAILS_FOR_FILTER)
	 * 
	 * @RequestMapping(value = "/npl/orders/distinctdetails", method =
	 * RequestMethod.GET)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message =
	 * Constants.SUCCESS),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) }) public
	 * ResponseResource<IsvFilterResponse> getNplOrdersBasedOnLegalEntities() throws
	 * IOException, TclCommonException { IsvFilterResponse isvFilterResponse =
	 * nplOrderService.getOrdersForFilter(); return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * isvFilterResponse, Status.SUCCESS); }
	 */

	/**
	 * Get All Orders Based On Search
	 *
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications
	 * @throws TclCommonException
	 */
	/*
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ALL_ORDERS_BY_SEARCH)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message =
	 * Constants.SUCCESS),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 * 
	 * @GetMapping(value = "/npl/orders/search") public
	 * ResponseResource<PagedResult<NplOrdersBean>>
	 * getNplOrders(@RequestParam(required = false) String stage,
	 * 
	 * @RequestParam(required = false) Integer productFamilyId,
	 * 
	 * @RequestParam(required = false) Integer legalEntity, @RequestParam(required =
	 * false) String startDate,
	 * 
	 * @RequestParam(required = false) String endDate, @RequestParam("page") Integer
	 * page,
	 * 
	 * @RequestParam("size") Integer size) throws TclCommonException {
	 * PagedResult<NplOrdersBean> response =
	 * nplOrderService.getOrdersBySearch(stage, productFamilyId, legalEntity,
	 * startDate, endDate, page, size); return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * response, Status.SUCCESS); }
	 */

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
	@RequestMapping(value = "/npl/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload", method = RequestMethod.POST)
	public ResponseResource<TempUploadUrlInfo> uploadNplCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(name="file") MultipartFile file) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = nplQuotePdfService.uploadCofPdf(quoteId, file, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
	}
	
	/**
	 * 
	 * getNplCofPdfUrl
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/npl/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload/url", method = RequestMethod.GET)
	public ResponseResource<TempUploadUrlInfo> getNplCofPdfUrl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam(defaultValue="NEW") String type, HttpServletResponse response) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo;
		if(type.equalsIgnoreCase("RENEWALS")) {
			tempUploadUrlInfo = renewalsNplQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}else {
			tempUploadUrlInfo = nplQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
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
	@RequestMapping(value = "/npl/quotes/{quoteId}/le/{quoteLeId}/manualcof/download", method = RequestMethod.GET)
	public ResponseResource<String> downloadNplCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		nplQuotePdfService.downloadCofPdf(quoteId, response);
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
	@RequestMapping(value = "/npl/quotes/{quoteId}/le/{quoteLeId}/manual/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> approvedNplManualQuotes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletRequest httpServletRequest)
			throws TclCommonException {
		NplQuoteDetail response = nplQuoteService.approvedManualQuotes(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/** Npl related service handler Ends here */

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getQuoteConfiguration used to
	 *            fetch quote details
	 * @param customerId
	 * @param quoteId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
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
	@RequestMapping(value = "/gvpn/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OrdersBean>> getAllOrdersGvpn() throws TclCommonException {
		List<OrdersBean> response = gvpnOrderService.getAllOrders();
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
	@RequestMapping(value = "/gvpn/orders/{orderId}/download", method = RequestMethod.GET)
	public ResponseEntity<String> getOrderDetailsExcelGvpn(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws IOException, TclCommonException {
		gvpnOrderService.returnExcel(orderId, response);
		return null;

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
	@RequestMapping(value = "/gvpn/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSiteProvisionAudit.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderSiteProvisionAudit>> updateOrderSiteStatusGvpn(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("siteId") Integer siteId, @RequestBody OrderSiteRequest request) throws TclCommonException {
		List<OrderSiteProvisionAudit> response = gvpnOrderService.updateOrderSiteStatus(siteId, request);
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
	@RequestMapping(value = "/izopc/orders/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OrderSummaryBean>> getOrderSummaryIzopc() throws TclCommonException {
		List<OrderSummaryBean> response = izoPcOrderService.getOrderSummary();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/izopc/orders/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PagedResult<OrderSummaryBean>> getPageOrderSummaryIzopc(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam(required = false, name = "searchText") String searchText)
			throws TclCommonException {
		PagedResult<OrderSummaryBean> response = izoPcOrderService.getOrderSummary(page, size, searchText);
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
	@RequestMapping(value = "/izopc/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrdersBean> getOrderDetailsIzopc(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		OrdersBean response = izoPcOrderService.getOrderDetails(orderId);
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
	@RequestMapping(value = "/izopc/orders/sites/{siteId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> getFeasiblityAndPricingDetailsForOrderIllSitesIzopc(
			@PathVariable("siteId") Integer siteId) throws TclCommonException {
		OrderIllSitesWithFeasiblityAndPricingBean response = izoPcOrderService
				.getFeasiblityAndPricingDetailsForOrderIllSites(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/izopc/quotes/sites/{siteId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteIllSitesWithFeasiblityAndPricingBean> getFeasiblityAndPricingDetailsForQuoteIllSitesIzopc(
			@PathVariable("siteId") Integer siteId) throws TclCommonException {
		QuoteIllSitesWithFeasiblityAndPricingBean response = izoPcOrderService
				.getFeasiblityAndPricingDetailsForQuoteIllSites(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/izopc/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getManualFPIzopc(@PathVariable("siteId") Integer siteId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody FPRequest fpRequest) throws TclCommonException {
		izoPcPricingFeasibilityService.processManualFP(fpRequest, siteId, quoteLeId);
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
	@RequestMapping(value = "/izopc/quotes/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<QuoteSummaryBean>> getQuoteSummaryIzopc() throws TclCommonException {
		List<QuoteSummaryBean> response = izoPcQuoteService.getQuoteSummary();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/izopc/quotes/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PagedResult<QuoteSummaryBean>> getPageQuoteSummaryIzopc(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam(required = false, name = "searchText") String searchText)
			throws TclCommonException {
		PagedResult<QuoteSummaryBean> response = izoPcQuoteService.getQuoteSummary(page, size, searchText);
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
	@RequestMapping(value = "/izopc/quotes/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<com.tcl.dias.oms.izopc.beans.QuoteBean> getQuoteConfigurationIzopc(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites) throws TclCommonException {
		com.tcl.dias.oms.izopc.beans.QuoteBean response = izoPcQuoteService.getQuoteDetails(quoteId, feasibleSites,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@RequestMapping(value = "/izopc/orders/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_TAX_EXEMPTION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderToLeDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderToLeDto> getAttributesAndSitesIzopc(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId,
			@RequestParam(value = "attributeName", required = false) String attributeName) throws TclCommonException {
		OrderToLeDto response = izoPcOrderService.getAttributesAndSites(orderToLeId, attributeName);
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
	@RequestMapping(value = "/izopc/orders/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateLeaglEntityAttributeIzopc(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody List<UpdateRequest> request)
			throws TclCommonException {
		izoPcOrderService.updateLegalEntityPropertiesIsv(request, orderToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/izopc/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderProductComponent.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderProductComponentBean>> getSitePropertiesIzopc(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("siteId") Integer siteId,
			@RequestParam(value = "filter", required = false) String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> response = izoPcOrderService.getSiteProperties(siteId, attributeName);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/izopc/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderIllSiteBean> updateSitePropertiesIzopc(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		OrderIllSiteBean response = izoPcOrderService.updateSiteProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	@RequestMapping(value = "/izopc/orders/{orderId}/details/download", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<String> getOrderSummaryForExcelIzopc(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws IOException, TclCommonException {
		izoPcOrderService.getOrderSummaryForExcel(orderId, response);
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
	@RequestMapping(value = "/izopc/orders/distinctdetails", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<IsvFilterResponse> getOrdersBasedOnLegalEntitiesIzopc()
			throws IOException, TclCommonException {
		IsvFilterResponse isvFilterResponse = izoPcOrderService.getOrdersForFilter();
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
	@GetMapping(value = "/izopc/orders/search")
	public ResponseResource<PagedResult<OrdersBean>> getOrdersIzopc(@RequestParam(required = false) String stage,
			@RequestParam(required = false) Integer productFamilyId,
			@RequestParam(required = false) Integer legalEntity, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size) throws TclCommonException {
		PagedResult<OrdersBean> response = izoPcOrderService.getOrdersBySearch(stage, productFamilyId, legalEntity,
				startDate, endDate, page, size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
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
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ALL_ORDERS_BY_SEARCH)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/izopc/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdfIzopc(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		izoPcQuotePdfService.processApprovedCof(orderId, orderLeId, response,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
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
	@RequestMapping(value = "/izopc/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload", method = RequestMethod.POST)
	public ResponseResource<TempUploadUrlInfo> uploadCofPdfIzopc(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(name="file") MultipartFile file) throws TclCommonException {

		TempUploadUrlInfo tempUploadUrlInfo = izoPcQuotePdfService.uploadCofPdf(quoteId, file,quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
	}
	
	/**
	 * 
	 * getCofPdfIzopcTempUrl
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/izopc/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload/url", method = RequestMethod.GET)
	public ResponseResource<TempUploadUrlInfo> getCofPdfIzopcTempUrl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {

		TempUploadUrlInfo tempUploadUrlInfo = izoPcQuotePdfService.uploadCofPdf(quoteId, null,quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
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
	@RequestMapping(value = "/izopc/quotes/{quoteId}/le/{quoteLeId}/manual/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<com.tcl.dias.oms.izopc.beans.QuoteDetail> approvedManualQuotesIzopc(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletRequest httpServletRequest)
			throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);

		com.tcl.dias.oms.izopc.beans.QuoteDetail response = izoPcQuoteService.approvedManualQuotes(quoteId,forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
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
	 * returns gsc order details
	 * 
	 * @author VISHESH AWASTHI
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc/orders/{orderId}")
	public ResponseResource<GscOrderDataBean> getGscOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		GscOrderDataBean response = gscOrderService.getGscOrderById(orderId).get();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Updates the order status and stage
	 * 
	 * @author VISHESH AWASTHI
	 * @param orderId
	 * @param orderToLeId
	 * @param order_configuration_id
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderStatusStageUpdate.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/gsc/orders/{orderId}/le/{orderToLeId}/configuration/{order_configuration_id}/stage")
	public ResponseResource<GscOrderStatusStageUpdate> updateGscOrderSiteStatus(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("order_configuration_id") Integer order_configuration_id,
			@RequestBody GscApiRequest<GscOrderStatusStageUpdate> request) throws TclCommonException {
		GscOrderStatusStageUpdate response = gscOrderService.updateOrderConfigurationStageStatus(order_configuration_id,
				request.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * returns gsc quote details
	 * 
	 * @author VISHESH AWASTHI
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_QUOTE_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc/quotes/{quoteId}")
	public ResponseResource<GscQuoteDataBean> getGscQuoteDetails(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		GscQuoteDataBean response = gscQuoteService.getGscQuoteById(quoteId).get();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Generated Cof Pdf based on order id
	 * 
	 * @author MAYANK SHARMA
	 * @param orderId
	 * @param orderLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_QUOTE_PDF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gsc/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdfGsc(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		gscOrderService.getCofPdfByOrderId(orderId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * Generate COF PDF for given ID
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_COF_PDF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc/{quoteId}/cofpdf")
	public ResponseResource generateCofPdf(@PathVariable("quoteId") Integer quoteId, HttpServletResponse response){
		gscQuotePdfService.processCofPdf(quoteId, response, false,  false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * update price details for Gsc Quotes
	 *
	 * @param configurationId
	 * @param quoteId
	 * @param quoteLeId
	 * @param updatePrice
	 * @return {@link GscManualPricing}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "gsc/quotes/{quoteId}/le/{quoteLeId}/configurations/{configurationId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GscManualPricing> processManualGscPricing(
			@PathVariable("configurationId") Integer configurationId, @PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody GscApiRequest<GscManualPricing> updatePrice)
			throws TclCommonException {
		GscManualPricing response = gscPricingFeasibilityService.processManualPricing(quoteId, quoteLeId,
				configurationId, updatePrice.getData());
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
	 * deleteNplQuote - method to delete NPL quotes
	 * 
	 * @author Dinahar Vivekanandan
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_NPL_QUOTE)
	@RequestMapping(value = "/npl/quotes/{quoteId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deleteNplQuote(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = true, name = "action") String actionType) throws TclCommonException {
		nplQuoteService.deleteQuote(quoteId, actionType);
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
	 * download cof pdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@GetMapping(value = "/gsc/quotes/{quoteId}/le/{quoteLeId}/manualcof")
	public ResponseResource<String> downloadGscCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,@RequestParam("action") String action) throws TclCommonException {
		String tempDownloadUrl = gscQuotePdfService.downloadCofPdf(quoteId, response, action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempDownloadUrl, Status.SUCCESS);

	}

	/**
	 * uploads cof pdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@PostMapping(value = "/gsc/quotes/{quoteId}/le/{quoteLeId}/manualcof")
	public ResponseResource<TempUploadUrlInfo> uploadGscCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam("file") MultipartFile file, @RequestParam("action") String action) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo =  gscPdfHelper.uploadCofPdf(quoteId, file, action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
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
		QuotePriceAuditResponse response = illQuoteService.getQuotePriceAudit(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Retry failed or struck order processing with downstream systems
	 * @param orderId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.RETRY_DOWNSTREAM_ORDER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc/orders/{orderIdentifier}/downstream")
	public ResponseResource<String> retryOrderDownstreamProcessing(@PathVariable("orderIdentifier") Integer orderId) {
		return Try.success(orderId)
				.map(gscOrderService::retryDownstreamProcessing)
				.map(ResponseResource::new)
				.get();
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
		OmsAttachmentBean omsAttachmentBean = illQuotePdfService.updateCofUploadedDetails(quoteId, quoteLeId, requestId, url);
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
		String tempDownloadUrl = illQuotePdfService.downloadCofFromStorageContainer(quoteId, quoteLeId, null,null,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}


	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ALL_ORDERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/izopc/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OrdersBean>> getAllOrdersIzoPc() throws TclCommonException {
		List<OrdersBean> response = izoPcOrderService.getAllOrders();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	@RequestMapping(value = "/izopc/orders/{orderId}/download", method = RequestMethod.GET)
	public ResponseEntity<String> getOrderDetailsExcelIzopc(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws IOException, TclCommonException {
		izoPcOrderService.returnExcel(orderId, response);
		return null;

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@RequestMapping(value = "/izopc/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSiteProvisionAudit.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderSiteProvisionAudit>> updateOrderSiteStatusIzopc(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("siteId") Integer siteId, @RequestBody OrderSiteRequest request) throws TclCommonException {
		List<OrderSiteProvisionAudit> response = izoPcOrderService.updateOrderSiteStatus(siteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}


	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn/orders/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OrderSummaryBean>> getOrderSummaryGvpn() throws TclCommonException {
		List<OrderSummaryBean> response = gvpnOrderService.getOrderSummary();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn/orders/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PagedResult<OrderSummaryBean>> getPageOrderSummaryGvpn(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam(required = false, name = "searchText") String searchText)
			throws TclCommonException {
		PagedResult<OrderSummaryBean> response = gvpnOrderService.getOrderSummary(page, size, searchText);
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
	@RequestMapping(value = "/gvpn/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrdersBean> getOrderDetailsGvpn(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		OrdersBean response = gvpnOrderService.getOrderDetails(orderId);
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
	@RequestMapping(value = "/gvpn/orders/sites/{siteId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> getFeasiblityAndPricingDetailsForOrderIllSitesGvpn(
			@PathVariable("siteId") Integer siteId) throws TclCommonException {
		OrderIllSitesWithFeasiblityAndPricingBean response = gvpnOrderService
				.getFeasiblityAndPricingDetailsForOrderIllSites(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn/quotes/sites/{siteId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteIllSitesWithFeasiblityAndPricingBean> getFeasiblityAndPricingDetailsForQuoteIllSitesGvpn(
			@PathVariable("siteId") Integer siteId) throws TclCommonException {
		QuoteIllSitesWithFeasiblityAndPricingBean response = gvpnOrderService
				.getFeasiblityAndPricingDetailsForQuoteIllSites(siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getManualFPGvpn(@PathVariable("siteId") Integer siteId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody FPRequest fpRequest) throws TclCommonException {
		gvpnPricingFeasibilityService.processManualFP(fpRequest, siteId, quoteLeId);
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
	@RequestMapping(value = "/gvpn/quotes/summary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<QuoteSummaryBean>> getQuoteSummaryGvpn() throws TclCommonException {
		List<QuoteSummaryBean> response = gvpnQuoteService.getQuoteSummary();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteSummaryBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn/quotes/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PagedResult<QuoteSummaryBean>> getPageQuoteSummaryGvpn(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam(required = false, name = "searchText") String searchText)
			throws TclCommonException {
		PagedResult<QuoteSummaryBean> response = gvpnQuoteService.getQuoteSummary(page, size, searchText);
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
	@RequestMapping(value = "/gvpn/quotes/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> getQuoteConfigurationGvpn(@PathVariable("quoteId") Integer quoteId, @RequestParam(required = false, name = "feasiblesites") String feasibleSites,
																 @RequestParam(required = false, name = "siteproperties") Boolean siteproperties,
																	@RequestParam(required = false, name = "manualfeasibility") Boolean manualFeasibility ) throws TclCommonException {
		QuoteBean response = gvpnQuoteService.getQuoteDetails(quoteId, feasibleSites,siteproperties, manualFeasibility);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@RequestMapping(value = "/gvpn/orders/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_TAX_EXEMPTION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderToLeDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderToLeDto> getAttributesAndSitesGvpn(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId,
			@RequestParam(value = "attributeName", required = false) String attributeName) throws TclCommonException {
		OrderToLeDto response = gvpnOrderService.getAttributesAndSites(orderToLeId, attributeName);
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
	@RequestMapping(value = "/gvpn/orders/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateLeaglEntityAttributeGvpn(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody List<UpdateRequest> request)
			throws TclCommonException {
		gvpnOrderService.updateLegalEntityPropertiesIsv(request, orderToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/gvpn/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderProductComponent.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderProductComponentBean>> getSitePropertiesGvpn(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("siteId") Integer siteId,
			@RequestParam(value = "filter", required = false) String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> response = gvpnOrderService.getSiteProperties(siteId, attributeName);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/gvpn/orders/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderIllSiteBean> updateSitePropertiesGvpn(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		OrderIllSiteBean response = gvpnOrderService.updateSiteProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	@RequestMapping(value = "/gvpn/orders/{orderId}/details/download", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<String> getOrderSummaryForExcelGvpn(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws IOException, TclCommonException {
		gvpnOrderService.getOrderSummaryForExcel(orderId, response);
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
	@RequestMapping(value = "/gvpn/orders/distinctdetails", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<IsvFilterResponse> getOrdersBasedOnLegalEntitiesGvpn()
			throws IOException, TclCommonException {
		IsvFilterResponse isvFilterResponse = gvpnOrderService.getOrdersForFilter();
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
	@GetMapping(value = "/gvpn/orders/search")
	public ResponseResource<PagedResult<OrdersBean>> getOrdersGvpn(@RequestParam(required = false) String stage,
			@RequestParam(required = false) Integer productFamilyId,
			@RequestParam(required = false) Integer legalEntity, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size) throws TclCommonException {
		PagedResult<OrdersBean> response = gvpnOrderService.getOrdersBySearch(stage, productFamilyId, legalEntity,
				startDate, endDate, page, size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
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
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ALL_ORDERS_BY_SEARCH)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdfGvpn(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		gvpnQuotePdfService.processApprovedCof(orderId, orderLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
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
	@RequestMapping(value = "/gvpn/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload", method = RequestMethod.POST)
	public ResponseResource<TempUploadUrlInfo> uploadCofPdfGvpn(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(name="file") MultipartFile file) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = gvpnQuotePdfService.uploadCofPdf(quoteId, file, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
	}
	
	/**
	 * 
	 * getCofPdfGvpnUrl
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/gvpn/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload/url", method = RequestMethod.GET)
	public ResponseResource<TempUploadUrlInfo> getCofPdfGvpnUrl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response, @RequestParam(defaultValue="NEW") String type) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo;
		
		if(type.equalsIgnoreCase("RENEWALS")) {
			tempUploadUrlInfo = renewalsGvpnQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}else {
		 tempUploadUrlInfo = gvpnQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}	
		
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
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
	@RequestMapping(value = "/gvpn/quotes/{quoteId}/le/{quoteLeId}/manual/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> approvedManualQuotesGvpn(@PathVariable("quoteId") Integer quoteId,
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

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CONFIGURATION_DOCUMENTS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscCountrySpecificDocumentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/gsc/orders/configurations/{order_configuration_id}/documents")
	public ResponseResource<List<GscAttachmentBean>> getApplicableDocuments(@PathVariable("order_configuration_id") Integer configurationId) {
		List<GscAttachmentBean> response = gscOrderDetailService.getDocumentsForConfigurationId(configurationId);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPLOAD_CONFIGURATION_DOCUMENT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscAttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/gsc/orders/configurations/{order_configuration_id}/documents/{document_id}")
	public ResponseResource<GscAttachmentBean> uploadConfigurationDocument(@RequestParam("file") MultipartFile file,
													  @PathVariable("order_configuration_id") Integer configurationId,
													  @PathVariable("document_id") Integer documentId) {
		GscAttachmentBean response =  gscOrderDetailService.uploadDocumentForConfiguration(file, configurationId, documentId);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_CONFIGURATION_DOCUMENT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = Resource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/gsc/orders/configurations/{order_configuration_id}/documents/{document_id}")
	public ResponseEntity<Resource> downloadConfigurationDocument(@PathVariable("order_configuration_id") Integer configurationId,
														@PathVariable("document_id") Integer documentId,
														@RequestParam(name ="template", defaultValue = "false", required = false) Boolean downloadTemplate) {
		Resource resource =  gscOrderDetailService.fetchConfigurationAttachmentForId(configurationId, documentId, downloadTemplate).get();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "filename=" + resource.getFilename());
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return ResponseEntity.ok().headers(headers).body(resource);
	}

	//TODO: will be removed
    /**
     * Bulk update configuration attributes for multiple solutions and product components
     * @param orderId
     * @param solutions
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderSolutionBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/{order_id}/solutions/gscorders/configurations/attributes")
    public ResponseResource<List<GscOrderSolutionBean>> updateOrderProductComponentAttributesForSolutions(@PathVariable("order_id") Integer orderId,
                                                                            @RequestBody GscApiRequest<List<GscOrderSolutionBean>> solutions) {
        List<GscOrderSolutionBean> response = gscOrderService.updateProductComponentAttributesForSolutions(orderId, solutions.getData());
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
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
     * Based on given value update product component attributes
     *
     * @param orderId
     * @param orderGscId
     * @param solutionId
     * @param configurationId
     * @param attributes
     * @return GscOrderProductComponentBean
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderProductComponentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/gsc/orders/{order_id}/solutions/{order_solution_id}/gscorders/{gsc_order_id}/configurations/{order_configuration_id}/attributes")
    public ResponseResource<List<GscOrderProductComponentBean>> updateOrderProductComponentAttributes(@PathVariable("order_id") Integer orderId,
                                                                @PathVariable("gsc_order_id") Integer orderGscId, @PathVariable("order_solution_id") Integer solutionId,
                                                                @PathVariable("order_configuration_id") Integer configurationId,
                                                                @RequestBody GscApiRequest<List<GscOrderProductComponentBean>> attributes) {

        List<GscOrderProductComponentBean> response = gscOrderService.updateOrderProductComponentAttributes(orderId, solutionId, orderGscId,
                configurationId, attributes.getData());
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * Save orders legal attributes by quote id
     *
     * @param orderId
     * @param orderToLeId
     * @param request
     * @return {@link GscOrderProductComponentsAttributeValueBean}
     * @author VISHESH AWASTHI
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderProductComponentsAttributeValueBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/gsc/orders/{order_id}/legalentities/{order_le_id}/attributes")
    public ResponseResource saveOrderLeAttributes(@PathVariable("order_id") Integer orderId,
                                                  @PathVariable("order_le_id") Integer orderToLeId,
                                                  @RequestBody GscApiRequest<GscOrderAttributesBean> request) {
        return new ResponseResource(ResponseResource.R_CODE_OK,
                                    ResponseResource.RES_SUCCESS,
                                    gscOrderService.saveOrderToLeAttributes(orderId, orderToLeId, request.getData().getAttributes()),
                                    Status.SUCCESS);
    }

	/**
	 * Create/Update product component attributes
	 *
	 * @param quoteId
	 * @param quoteGscId
	 * @param solutionId
	 * @param configurationId
	 * @param attributes
	 * @return {@link GscProductComponentBean}
	 * @author VISHESH AWASTHI
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc/{quote_id}/solutions/{solution_id}/gscquotes/{gsc_quote_id}/configurations/{configuration_id}/attributes")
	public ResponseResource updateOrDeleteProductComponentAttributes(@PathVariable("quote_id") Integer quoteId,
																   @PathVariable("gsc_quote_id") Integer quoteGscId, @PathVariable("solution_id") Integer solutionId,
																   @PathVariable("configuration_id") Integer configurationId,
																   @RequestBody GscApiRequest<List<GscProductComponentBean>> attributes) {
		return new ResponseResource(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				gscQuoteAttributeService.updateOrDeleteProductComponentAttributes(quoteId, solutionId,
						quoteGscId, configurationId, attributes.getData()),
				Status.SUCCESS);
	}

	/**
	 * API to get gsc sla details based on accessType
	 *
	 * @param accessType
	 * @return {@link GscSlaBeanListener}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SLA_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSlaBeanListener.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sla/details/{accesstype}")
	public GscSlaBeanListener getGscSlaDetails(@PathVariable("accesstype") String accessType) throws TclCommonException {
		return gscSlaService.getSlaDetails(accessType);
	}

	/**
	 *
	 * generate signed Cof for GSC
	 *
	 * @param orderId
	 * @param orderLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/gsc/orders/{orderId}/le/{orderToLeId}/signed/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateGscSignedCofPdf(@PathVariable("orderId") Integer orderId,
												   @PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = gscQuotePdfService.processApprovedCof(orderId, orderLeId, response, false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempDownloadUrl, Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn/quotes/{quoteId}/sites/{siteId}/attributes/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteDetail> updateGvpnProductAttributes(@RequestBody UpdateRequest updateRequest) throws TclCommonException {
		QuoteDetail response = gvpnQuoteService.editSiteComponent(updateRequest);
	return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
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
	 * Download outbound prices in Excel API
	 *
	 * @param response
	 * @param quoteCode
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws TclCommonException
	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_A_TO_Z_EXCEL_DOWNLOAD)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/gsc/download/outbound/prices")
    public ResponseEntity<String> downloadOutboundPricesInExcel(HttpServletResponse response, @RequestParam("code") String quoteCode) throws IOException, DocumentException, TclCommonException {
        gscProductCatalogService.downloadOutboundPricesInExcel(response, quoteCode);
        return null;
    }

	/**
	 * Get emergency Address API
	 *
	 * @param configuarationId
	 * @return
	 * @throws TclCommonException
	 */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_EMERGENCY_ADDRESS_DETAILS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/gsc/emergency/address/{configuarationId}")
    public ResponseResource<List<String>> getEmergencyAddress(@PathVariable("configuarationId") Integer configuarationId) throws TclCommonException {
        List<String> response = gscOrderService.getEmergencyAddress(configuarationId);
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    /**
     * 
     * Process customer feasiblity for NPL
     * @param linkId
     * @param quoteId
     * @param quoteLeId
     * @param response
     * @param file
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.PROCESS_CUSTOM_FP)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/quotes/{quoteId}/le/{quoteLeId}/link/{linkId}/fpdetails/custom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processCustomerFPNpl(@PathVariable("linkId") Integer linkId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			HttpServletResponse response, @RequestParam("file") MultipartFile file) throws TclCommonException {
		nplPricingFeasibilityService.processCustomFPNpl(linkId, file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
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
	@RequestMapping(value = "gvpn/quotes/{quoteId}/le/{quoteLeId}/manualcofuploaded", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OmsAttachmentBean> updateCofUploadedDetailsGVPN(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("requestId") String requestId, @RequestParam("url") String url )
			throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = gvpnQuotePdfService.updateCofUploadedDetails(quoteId, quoteLeId, requestId, url);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, omsAttachmentBean,
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
		@RequestMapping(value = "npl/quotes/{quoteId}/le/{quoteLeId}/manualcofuploaded", method = RequestMethod.POST)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<OmsAttachmentBean> updateCofUploadedDetailsNPL(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("requestId") String requestId, @RequestParam("url") String url )
				throws TclCommonException {
			OmsAttachmentBean omsAttachmentBean = nplQuotePdfService.updateCofUploadedDetails(quoteId, quoteLeId, requestId, url);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, omsAttachmentBean,
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
		@RequestMapping(value = "izopc/quotes/{quoteId}/le/{quoteLeId}/manualcofuploaded", method = RequestMethod.POST)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<OmsAttachmentBean> updateCofUploadedDetailsIzopc(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("requestId") String requestId, @RequestParam("url") String url )
				throws TclCommonException {
			OmsAttachmentBean omsAttachmentBean = izoPcQuotePdfService.updateCofUploadedDetails(quoteId, quoteLeId, requestId, url);
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
		@RequestMapping(value = "/orders/{orderId}/le/{orderLeId}/manualcof/storage/downloadurl", method = RequestMethod.GET)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<String> DownloadCofBasedOnOrders(@PathVariable("orderId") Integer orderId,
				@PathVariable("orderLeId") Integer orderLeId) throws TclCommonException {
			String tempDownloadUrl = illQuotePdfService.downloadCofFromStorageContainer(null, null, orderId,orderLeId,null);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
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
		@RequestMapping(value = "/gvpn/orders/{orderId}/le/{orderLeId}/manualcof/storage/downloadurl", method = RequestMethod.GET)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<String> DownloadCofBasedOnOrdersGVPN(@PathVariable("orderId") Integer orderId,
				@PathVariable("orderLeId") Integer orderLeId) throws TclCommonException {
			String tempDownloadUrl = gvpnQuotePdfService.downloadCofFromStorageContainer(null, null, orderId,orderLeId,null);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
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
		@RequestMapping(value = "/npl/orders/{orderId}/le/{orderLeId}/manualcof/storage/downloadurl", method = RequestMethod.GET)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<String> DownloadCofBasedOnOrdersNPL(@PathVariable("orderId") Integer orderId,
				@PathVariable("orderLeId") Integer orderLeId) throws TclCommonException {
			String tempDownloadUrl = nplQuotePdfService.downloadCofFromStorageContainer(null, null, orderId,orderLeId,null);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
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
		@RequestMapping(value = "/izopc/orders/{orderId}/le/{orderLeId}/manualcof/storage/downloadurl", method = RequestMethod.GET)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<String> DownloadCofBasedOnOrdersIZopc(@PathVariable("orderId") Integer orderId,
				@PathVariable("orderLeId") Integer orderLeId) throws TclCommonException {
			String tempDownloadUrl = izoPcQuotePdfService.downloadCofFromStorageContainer(null, null, orderId,orderLeId,null);
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
		illQuoteService.updateLegalEntityPropertiesIsvQuote(request, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	/**
	 * Updates Legal entities for npl
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/npl/quotes/{quoteId}/le/{quoteToLeId}/attributes" , method = RequestMethod.POST , consumes =MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<QuoteDetail> updateLegalEntityAttributeNpl(@PathVariable("quoteId") Integer quoteId,
		@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody List<UpdateRequest> request) throws TclCommonException{
		nplQuoteService.updateLegalEntityPropertiesIsvQuote(request, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	/**
	 * Updates Legal entities for izopc
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/izopc/quotes/{quoteId}/le/{quoteToLeId}/attributes" , method = RequestMethod.POST , consumes =MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<QuoteDetail> updateLegalEntityAttributeIzoPc(@PathVariable("quoteId") Integer quoteId,
		@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody List<UpdateRequest> request) throws TclCommonException{
		izoPcQuoteService.updateLegalEntityPropertiesIsvQuote(request, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	/**
	 * Updates Legal entities for npl
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/gvpn/quotes/{quoteId}/le/{quoteToLeId}/attributes" , method = RequestMethod.POST , consumes =MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<QuoteDetail> updateLegalEntityAttributeGvpn(@PathVariable("quoteId") Integer quoteId,
		@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody List<UpdateRequest> request) throws TclCommonException{
		gvpnQuoteService.updateLegalEntityPropertiesIsvQuote(request, quoteToLeId);
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
		illQuoteService.siteNotFeasible(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}
	

	/**
	 * API to upload country documents of customer in object storage.
	 *
	 * @param file
	 * @param configurationId
	 * @param documentId
	 * @return {@link GscAttachmentBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPLOAD_CONFIGURATION_DOCUMENT)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/gsc/orders/object/configurations/{order_configuration_id}/documents/{document_id}")
	public ResponseResource<ServiceResponse> uploadObjectConfigurationDocument(@RequestParam("file") MultipartFile file,
																			   @PathVariable("order_configuration_id") Integer configurationId,
																			   @PathVariable("document_id") Integer documentId) throws TclCommonException {
		ServiceResponse serviceResponse = gscOrderDetailService.uploadObjectConfigurationDocument(file, configurationId);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceResponse,
				Status.SUCCESS);
	}

	/**
	 * API to update the file uploaded details in oms level
	 *
	 * @param configurationId
	 * @param documentId
	 * @param requestId
	 * @param url
	 * @return {@link GscAttachmentBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPLOAD_CONFIGURATION_DOCUMENT)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscAttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/gsc/orders/update/object/configurations/{order_configuration_id}/documents/{document_id}")
	public ResponseResource<GscAttachmentBean> updateUploadObjectConfigurationDocument(
			@PathVariable("order_configuration_id") Integer configurationId,
			@PathVariable("document_id") Integer documentId, @RequestParam("requestId") String requestId,
			@RequestParam(value = "url") String url) {
		GscAttachmentBean response = gscOrderDetailService.updateUploadObjectConfigurationDocument(configurationId, documentId,
				requestId, url);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_CONFIGURATION_DOCUMENT)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/gsc/orders/configurations/{order_configuration_id}/temporaryUrl/{document_id}")
	public ResponseResource<String> downloadConfigurationDocumentTemporaryUrl(@PathVariable("order_configuration_id") Integer configurationId,
																	@PathVariable("document_id") Integer documentId,
																	@RequestParam(name = "template", defaultValue = "false", required = false) Boolean downloadTemplate) {
		String tempDownloadUrl=gscOrderDetailService.getObjectStorageConfigurationAttachmentForId(configurationId, documentId, downloadTemplate).get();
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
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
		illPricingFeasibilityService.processManualPriceDetails(fpRequest, siteId, quoteLeId);
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
		illPricingFeasibilityService.processDiscount(pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
	/**
	 * Used to save the waiver details
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.SAVE_WAIVER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/waiver", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processWaiverForIllTerminations(@RequestBody PDRequest pdRequest) throws TclCommonException {
		illPricingFeasibilityService.processWaiverForTerminations(pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
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
	@RequestMapping(value = "/quotes/gvpn/discount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processDiscountForPriceGvpn(@RequestBody PDRequest pdRequest) throws TclCommonException {
		gvpnPricingFeasibilityService.processDiscount(pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
	/**
	 * Used to save the waiver details for Terminations
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.SAVE_WAIVER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/gvpn/waiver", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processWaiverForGvpnTerminations(@RequestBody PDRequest pdRequest) throws TclCommonException {
		gvpnPricingFeasibilityService.processWaiverForTerminations(pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}	
	
	/**
	 * Used to save the discounted details intl
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.SAVE_DISCOUNT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/gvpn/intl/discount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processDiscountForPriceGvpnIntl(@RequestBody PDRequest pdRequest) throws TclCommonException {
		gvpnPricingFeasibilityService.processDiscountInternational(pdRequest);
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
		Integer approvalLevel = illPricingFeasibilityService.processDiscountApproval(quoteId,pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				approvalLevel, Status.SUCCESS);
	}

	/**
	 * Used to approve the discounted price for gvpn
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.APPROVE_DISCOUNT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/gvpn/discount/approve", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Integer> approveDiscountGvpn(@PathVariable("quoteId") Integer quoteId, @RequestBody List<Integer> pdRequest) throws TclCommonException {
		Integer approvalLevel = gvpnPricingFeasibilityService.processDiscountApprovalGvpn(quoteId,pdRequest);
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
		illQuoteService.updateSiteAttributesIsv(request, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	/**
	 * Approve Manual Quotes for GSC
	 *
	 * @param quoteId
	 * @param httpServletResponse
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/gsc/quotes/{quoteId}/le/{quoteLeId}/manual/approve/quotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<GscOrderDataBean> approveManualQuotesForGsip(@PathVariable("quoteId") Integer quoteId,
																		 @PathVariable("quoteLeId") Integer quoteLeId,
																		 HttpServletRequest httpServletRequest) {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		GscOrderDataBean response = gscOrderService.approveManualQuotes(quoteId, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
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
		String response = illPricingFeasibilityService.getDiscountedPrice(pdRequest);
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
		illQuoteService.updateTermsInMonthsForQuoteToLe(quoteId,quoteToLeId,updateRequest,taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * @author Mohamed Danish A
	 * 
	 * getIPCQuoteConfiguration used to fetch quote details
	 *
	 * @param quoteId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ipc/quotes/{quoteId}")
	public ResponseResource<com.tcl.dias.oms.ipc.beans.QuoteBean> getIPCQuoteConfiguration(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		com.tcl.dias.oms.ipc.beans.QuoteBean response = ipcQuoteService.getQuoteDetails(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * @author Mohamed Danish A
	 * 
	 * uploadIPCCofPdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param nat
	 * @return
	 * @throws TclCommonException
	 */
	@PostMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload")
	public ResponseResource<TempUploadUrlInfo> uploadIPCCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(name="file") MultipartFile file) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = ipcQuotePdfService.uploadCofPdf(quoteId, file, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUploadUrlInfo,
				Status.SUCCESS);
	}

	/**
	 * 
	 * @author Mohamed Danish A
	 * 
	 * uploadIPCCofPdfUrl
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@GetMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload/url")
	public ResponseResource<TempUploadUrlInfo> uploadIPCCofPdfUrl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId,@RequestParam(defaultValue="NEW") String type, HttpServletResponse response) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo;
		if(type.equalsIgnoreCase("RENEWALS")) {
			tempUploadUrlInfo = renewalsGvpnQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}else {
			tempUploadUrlInfo = ipcQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUploadUrlInfo,
				Status.SUCCESS);
	}

	/**
	 * @author Mohamed Danish A
	 *
	 * downloadIPCofFromStorageContainer - api to download the cof document that was uploaded to
	 * the storage container
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOWNLOAD_COF_STORAGE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/manualcof/storage/downloadurl")
	public ResponseResource<String> downloadIPCofFromStorageContainer(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		String tempDownloadUrl = ipcQuotePdfService.downloadCofFromStorageContainer(quoteId, quoteLeId, null, null, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * 
	 * @author Mohamed Danish A
	 * 
	 * downloadIPCCofPdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@GetMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/manualcof/download")
	public ResponseResource<String> downloadIPCCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		ipcQuotePdfService.downloadCofPdf(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	/**
	 * @author Mohamed Danish A
	 *  
	 * updateIPCCofUploadedDetails - api to update the manual cof uploaded details
	 * after the document is stored in the storage container
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_COF_UPLOADED_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/manualcofuploaded")
	public ResponseResource<OmsAttachmentBean> updateIPCCofUploadedDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("requestId") String requestId, @RequestParam("url") String url )
			throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = ipcQuotePdfService.updateCofUploadedDetails(quoteId, quoteLeId, requestId, url);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, omsAttachmentBean,
				Status.SUCCESS);
	}

	/**
	 * @author Mohamed Danish A
	 *
	 * approvedIPCManualQuotes
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param httpServletRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })

	@PostMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/manual/approvequotes")
	public ResponseResource<QuoteDetail> approvedIPCManualQuotes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletRequest httpServletRequest)
			throws TclCommonException {
		QuoteDetail response = ipcQuoteService.approvedManualQuotes(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author Mohamed Danish A
	 * 
	 * approveIPCDiscount - used to approve the discounted IPC price
	 * 
	 * @param quoteId
	 * @return approvalLevel
	 * @throws TclCommonException
	 */
	@PostMapping("/ipc/quotes/{quoteId}/discount/approve")
	public ResponseResource<Integer> approveIPCDiscount(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		Integer approvalLevel = ipcPricingService.processDiscountApproval(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, approvalLevel, Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_SITE_PROPERTIES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ipc/quotes/{quoteId}/le/{quoteToLeId}/properties")
	public ResponseResource<List<AttributeDetail>> getIPCQuoteDiscountProperties(@PathVariable("quoteId") Integer quoteId, 
			@PathVariable("quoteToLeId") Integer quoteLeId) throws TclCommonException {
		List<AttributeDetail> attributeDetails= ipcQuoteService.getIPCDiscountComments(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,	attributeDetails, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_SITE_PROPERTIES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ipc/quotes/{quoteId}/le/{quoteToLeId}/properties")
	public ResponseResource<String> updateIPCQuoteDiscountProperties(@PathVariable("quoteId") Integer quoteId, 
			@PathVariable("quoteToLeId") Integer quoteLeId, @RequestBody List<AttributeDetail> request) throws TclCommonException {
		ipcQuoteService.updateIPCDiscountProperties(quoteLeId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,	ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	/**
	 * @author Mohamed Danish A
	 * 
	 * getIPCQuotePrice used to fetch the IPC quote price.
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param netMarginPercentage
	 * @param finalDiscountPercentage
	 * @return PricingBean
	 * @throws TclCommonException
	 */
	@GetMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/price")
	public ResponseResource<PricingBean> getIPCQuotePrice(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestParam(value="actual", defaultValue="false") Boolean actual,
			@RequestParam(value="currency", required=false) String currency,
			@RequestParam(value="serviceId", required=false) String serviceId,
			@RequestParam(value="additional_discount_percentage", required=false) Double additionalDiscountPercentage,
			@RequestParam(value = "ipc_final_price", required=false) Double ipcFinalPrice,
			@RequestParam(value = "askAccessPrice", required=false) Double askAccessPrice,
			@RequestParam(value = "askAdditionalIpPrice", required=false) Double askAdditionalIpPrice)
			throws TclCommonException {
		PricingBean pricingBean = ipcPricingService.getQuotePrice(quoteId, actual, currency, additionalDiscountPercentage, ipcFinalPrice,askAccessPrice,askAdditionalIpPrice, serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				pricingBean, Status.SUCCESS);
	}

	/**
	 * @author Mohamed Danish A
	 * 
	 * updateIPCQuotePricingDiscount used to update the IPC quote pricing discount.
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param netMarginPercentage
	 * @param finalDiscountPercentage
	 * @return PricingBean
	 * @throws TclCommonException
	 */
	@PostMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/discount")
	public ResponseResource<String> updateIPCQuotePricingDiscount(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestBody IpcDiscountBean ipcDiscountBean)
			throws TclCommonException {
		ipcPricingService.updateQuotePricingDiscount(quoteId, quoteToLeId, ipcDiscountBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.name(), Status.SUCCESS);
	}

	@GetMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/reject")
	public ResponseResource<String> processRejectFlow(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		ipcPricingService.processQuoteForRejectFlow(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.name(), Status.SUCCESS);
	}
	/**
	 * @author Mohamed Danish A
	 * 
	 * getIPCOrderDetails used to fetch all the order details.
	 * 
	 * @param orderId
	 * @return IPCOrdersBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ipc/orders/{orderId}")
	public ResponseResource<IPCOrdersBean> getIPCOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		IPCOrdersBean response = ipcOrderService.getOrderDetails(orderId, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 *@author Mohamed Danish A
	 *
	 * generateCofPdf
	 *
	 * @param orderId
	 * @param orderLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@GetMapping("/ipc/orders/{orderId}/le/{orderToLeId}/cofpdf")
	public ResponseResource<String> generateIPCCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		ipcQuotePdfService.processApprovedCof(orderId, orderLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * @author Mohamed Danish A
	 * 
	 * downloadIPCCofBasedOnOrders - api to download the cof document that was uploaded to
	 * the storage container based on order
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOWNLOAD_COF_STORAGE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ipc/orders/{orderId}/le/{orderLeId}/manualcof/storage/downloadurl")
	public ResponseResource<String> downloadIPCCofBasedOnOrders(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderLeId") Integer orderLeId) throws TclCommonException {
		String tempDownloadUrl = ipcQuotePdfService.downloadCofFromStorageContainer(null, null, orderId,orderLeId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * @author Mohamed Danish A
	 * 
	 * updateIPCOrderStatus this method is used to update the status of IPC order sites
	 * 
	 * @param orderId, orderToLeId, status
	 * @return ResponseResource
	 * @throws TclCommonException
	 * 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSiteProvisionAudit.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ipc/orders/{orderId}/le/{orderToLeId}/stage")
	public ResponseResource<String> updateIPCOrderStatus(@PathVariable("orderId") Integer orderId, 
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestParam("status") String status) throws TclCommonException {
		ipcOrderService.updateOrderToLeStatus(orderToLeId, status,"","");
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "ipc/quotes/{quoteId}/le/{quoteLeId}/cloud/{cloudId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getIPCManualPrice(@PathVariable("cloudId") Integer cloudId,
												@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
												@RequestBody FPRequest fpRequest) throws TclCommonException {
		ipcPricingService.processManualPricing(fpRequest, cloudId, quoteLeId);
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
		String creditCheckStatus = illQuoteService.retriggerCreditCheck(quoteId);
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
	 * retrigger credit check for NPL
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREDITCHECK_RETRIGGER)
	@RequestMapping(value = "/quotes/{quoteId}/creditcheck/npl/retrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> nplRetriggerCreditCheck(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		String creditCheckStatus = nplQuoteService.retriggerCreditCheckNpl(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				creditCheckStatus, Status.SUCCESS);

	}

	/**
	 *
	 * RetriggerCreditCheck query gvpn
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREDITCHECK_RETRIGGER)
	@RequestMapping(value = "/quotes/{quoteId}/creditcheck/gvpn/retrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> reTriggerCreditCheckGvpn(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		String creditCheckStatus = gvpnQuoteService.retriggerCreditCheck(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				creditCheckStatus, Status.SUCCESS);
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
	 * API to upload the custom file and save it in site feasibilities for gvpn intl
	 *
	 * @param siteId
	 * @param quoteId
	 * @param quoteLeId
	 * @param response
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.PROCESS_CUSTOM_FP)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/gvpn/intl/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/fpdetails/custom", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processCustomFPForGvpnIntl(@PathVariable("siteId") Integer siteId,
															   @PathVariable("quoteId") Integer quoteId,
															   @PathVariable("quoteLeId") Integer quoteLeId,
															   HttpServletResponse response,
															   @RequestParam("file") MultipartFile file) throws TclCommonException {
		gvpnPricingFeasibilityService.processCustomFPForGvpnIntl(siteId, quoteId, quoteLeId, response, file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(), Status.SUCCESS);
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
		String cofResponse = illQuotePdfService.replaceCofPdf(quoteId, file, quoteToLeId);
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
		List<String> response = illQuoteService.getServiceRequest(quoteId, serviceType);
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
		illPricingFeasibilityService.processFinalApprovalManual(manualCommercialUpdate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 *
	 * THis methos is used to update Commercial Value Manually for NPL and NDE
	 * @param manualCommercialUpdateNPL
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/manualcommercialupdateNPL", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateCommercialManualNPL(@RequestBody ManualCommercialUpdate manualCommercialUpdateNPL)
			throws TclCommonException {
		nplPricingFeasibilityService.processFinalApprovalManualNPL(manualCommercialUpdateNPL);
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
	 * api for editing feasibility response attributes manually
	 *
	 * @param siteId
	 * @param quoteId
	 * @param quoteLeId
	 * @param manualfRequest
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.EDIT_FEASIBLITY_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "gvpn/macd/intl/quotes/{quoteId}/le/{quoteLeId}/sites/{siteId}/manualfdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> editGvpnMacdIntlManualFeasibility(@PathVariable("siteId") Integer siteId,
														  @PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
														  @RequestBody ManualFeasibilityRequest manualfRequest) throws TclCommonException {
		gvpnPricingFeasibilityService.processManualFeasibilityGvpnMacdIntl(manualfRequest, siteId, quoteLeId);
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
		QuoteTncBean response = illQuoteService.getTnc(quoteId);
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
		String response = illQuoteService.processTnc(quoteId, quoteTncBean);
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
		illQuoteService.updateSiteRejectionSatus(commercialRejectionBean);
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
		illQuoteService.updateQuoteRejectionSatus(commercialRejectionBean);
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
		List<MfL2OReportBean> response = illQuoteService.getOmsAttachments(fIds);
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
	 * Gde Order details by order id
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_ORDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeOrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gde/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GdeOrdersBean> getGdeOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		GdeOrdersBean response = gdeOrderService.getOrderDetailsById(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author archchan
	 * @param orderId
	 * @return GdePricingFeasibilityBean
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.GDE.GET_GDE_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GdePricingFeasibilityBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gde/orders/links/{linkId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GdePricingFeasibilityBean> getFeasiblityAndPricingDetailsForGdeBod(
			@PathVariable("linkId") Integer linkId) throws TclCommonException {
		GdePricingFeasibilityBean response = gdeOrderService.getFeasiblityAndPricingDetails(linkId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
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
	@ApiOperation(value = SwaggerConstants.ApiOperations.GDE.DOWNLOAD_APPROVED_COF)
	@RequestMapping(value = "/gde/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateGdeCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		gdeQuotePdfService.processApprovedCof(orderId, orderLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	/**
	 * Controller to get Gde quotes feasibiity and pricing details
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdePricingFeasibilityBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gde/quotes/links/{linkId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GdePricingFeasibilityBean> getFeasiblityAndPricingDetailsForGde(
			@PathVariable("linkId") Integer linkId) throws TclCommonException {
		GdePricingFeasibilityBean response = gdeOrderService.getFeasiblityAndPricingDetailsForQuoteNplBean(linkId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to update Gde quotes feasibiity and pricing details
	 * @param linkId
	 * @param quoteId
	 * @param quoteLeId
	 * @param fpRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gde/quotes/{quoteId}/le/{quoteLeId}/links/{linkId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> gdeManualPriceUpdate(@PathVariable("linkId") Integer linkId,
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody FPRequest fpRequest) throws TclCommonException {
		gdePricingFeasibilityService.processManualPricing(fpRequest, linkId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
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
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_QUOTE_SUMMARY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeQuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gde/quotes/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GdeQuoteBean> getGdeQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,@RequestParam(required = false, name = "siteproperties") Boolean siteproperties) throws TclCommonException {
		GdeQuoteBean response = gdeQuoteService.getQuoteDetails(quoteId, feasibleSites,siteproperties);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * downloadCofDetails - api to download the GDE cof document that was uploaded to
	 * the storage container
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOWNLOAD_COF_STORAGE)
	@RequestMapping(value = "/gde/orders/{orderId}/le/{orderLeId}/manualcof/storage/downloadurl", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> DownloadCofBasedOnOrdersGDE(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderLeId") Integer orderLeId) throws TclCommonException {
		String tempDownloadUrl = gdeQuotePdfService.downloadCofFromStorageContainer(null, null, orderId,orderLeId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}
	
	/**
	 * Used to approve the discounted price for gvpn
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.APPROVE_DISCOUNT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/{quoteId}/npl/discount/approve", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Integer> approveDiscountNPL(@PathVariable("quoteId") Integer quoteId, @RequestBody List<Integer> pdRequest) throws TclCommonException {
		Integer approvalLevel = nplPricingFeasibilityService.processDiscountApproval(quoteId,pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				approvalLevel, Status.SUCCESS);
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
	@RequestMapping(value = "/quotes/npl/discount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processDiscountNpl(@RequestBody PDRequest pdRequest) throws TclCommonException {
		nplPricingFeasibilityService.processDiscount(pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
	/**
	 * Used to save the Waiver details for Terminations
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.SAVE_WAIVER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quotes/npl/waiver", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processWaiverForNplTerminations(@RequestBody PDRequest pdRequest) throws TclCommonException {
		nplPricingFeasibilityService.processWaiverForTerminations(pdRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}	
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to update or add site properities
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/npl/link/{linkid}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateQuoteSitePropertiesNpl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId, @PathVariable("linkid") Integer linkid,
			@RequestBody SiteAttributeUpdateBean request) throws TclCommonException {
		nplQuoteService.updateSiteAttributesIsv(request, quoteLeId,linkid);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
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
	@RequestMapping(value="/commercial/link/npl/rejection",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateSiteRejectionSatusNpl(@RequestBody CommercialRejectionBean commercialRejectionBean) throws TclCommonException {
		nplQuoteService.updateSiteRejectionSatus(commercialRejectionBean);
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

	@RequestMapping(value="/commercial/quote/npl/rejection",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateQuoteRejectionSatusNplQuote(@RequestBody CommercialRejectionBean commercialRejectionBean) throws TclCommonException {
		nplQuoteService.updateQuoteRejectionSatus(commercialRejectionBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * Get quote operation for UCaaS.
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(path = "/ucaas/quotes/{quoteId}")
	public ResponseResource<WebexQuoteDataBean> getWebexQuoteById(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		WebexQuoteDataBean response = webexQuoteService.getWebexQuoteById(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get order operation for UCaaS.
	 *
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(path = "/ucaas/orders/{orderId}")
	public ResponseResource<WebexOrderDataBean> getWebexOrderById(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		WebexOrderDataBean response = webexOrderService.getWebexOrderById(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * Update GSC product component attributes
	 *
	 * @param quoteId
	 * @param quoteGscId
	 * @param solutionId
	 * @param configurationId
	 * @param attributes
	 * @return {@link GscProductComponentBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc/v2/{quote_id}/solutions/{solution_id}/gscquotes/{gsc_quote_id}/configurations/{configuration_id}/attributes")
	public ResponseResource<List<GscProductComponentBean>> updateProductComponentAttributes(
			@PathVariable("quote_id") Integer quoteId, @PathVariable("gsc_quote_id") Integer quoteGscId,
			@PathVariable("solution_id") Integer solutionId, @PathVariable("configuration_id") Integer configurationId,
			@RequestBody List<GscProductComponentBean> attributes) throws TclCommonException {
		return new ResponseResource<List<GscProductComponentBean>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, gscQuoteAttributeService2.updateOrDeleteProductComponentAttributes(
						quoteId, solutionId, quoteGscId, configurationId, attributes),
				Status.SUCCESS);
	}

	/**
	 * Update price details of GSC Quotes for UCaaS
	 *
	 * @param configurationId
	 * @param quoteId
	 * @param quoteLeId
	 * @param updatePrice
	 * @return {@link GscManualPricing}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gsc/v2/quotes/{quoteId}/le/{quoteLeId}/configurations/{configurationId}/fpdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GscManualPricing> processManualGscPricingForUCaaS(
			@PathVariable("configurationId") Integer configurationId, @PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody GscManualPricing updatePrice)
			throws TclCommonException {
		GscManualPricing response = gscPricingFeasibilityService2.processManualPricing(quoteId, quoteLeId,
				configurationId, updatePrice);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/* getTnc
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
	@RequestMapping(value="/quote/{quoteId}/le/{quoteLeId}/npl/tnc",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<QuoteTncBean> getTncNpl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		QuoteTncBean response = nplQuoteService.getTnc(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	


	/**
	 *
	 * Download COF PDF manually for UCaaS
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@GetMapping(value = "/ucaas/quotes/{quoteId}/download/manualcof")
	public ResponseResource<String> downloadWebexCofPdf(@PathVariable("quoteId") Integer quoteId,
			HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = webexQuotePdfService.downloadCofPdf(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}

	/**
	 * Upload COF manually for UCaaS
	 *
	 * @param quoteId
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@PostMapping(value = "/ucaas/quotes/{quoteId}/upload/manualcof")
	public ResponseResource<TempUploadUrlInfo> uploadWebexCofPdf(@PathVariable("quoteId") Integer quoteId,
			@RequestParam("file") MultipartFile file) throws TclCommonException {
		TempUploadUrlInfo tempDownloadUrl = webexQuotePdfService.uploadCofPdf(quoteId, file);
		return new ResponseResource<TempUploadUrlInfo>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempDownloadUrl, Status.SUCCESS);
	}

	/**
	 *
	 * Generate and download signed COF for UCaaS
	 *
	 * @param orderId
	 * @param orderLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/ucaas/orders/{orderId}/le/{orderToLeId}/signed/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateWebexSignedCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = webexQuotePdfService.processApprovedCof(orderId, orderLeId, response, false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * Save orders legal attributes by quote id for UCaaS
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param request
	 * @return {@link GscOrderAttributesBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucaas/orders/{orderId}/legalentities/{orderLeId}/attributes")
	public ResponseResource<GscOrderAttributesBean> saveWebexOrderLeAttributes(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderLeId") Integer orderToLeId,
			@RequestBody GscOrderAttributesBean request) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexOrderService.saveOrderToLeAttributes(orderId, orderToLeId, request.getAttributes()),
				Status.SUCCESS);
	}

	/**
	 * Used to fetch all the order details in excel sheet format for UCaaS
	 *
	 * @param orderId
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ucaas/orders/{orderId}/download")
	public ResponseResource<HttpServletResponse> getUCaaSOrderDetailsExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexExportLRService.returnExcel(orderId, response), Status.SUCCESS);
	}


	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_TNC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="/quote/{quoteId}/le/{quoteLeId}/npl/tnc",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<String> updateTncNpl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteTncBean quoteTncBean) throws TclCommonException {
		String response = nplQuoteService.processTnc(quoteId, quoteTncBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

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

	@ApiOperation(value = SwaggerConstants.ApiOperations.GDE.GET_FEASIBILITY_STATUS)
	@RequestMapping(value = "/gde/schedule/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> notifyFeasibilityCheck(@RequestBody FeasibilityCheckResponse feasibilityResponse)
			throws TclCommonException {
		String response = gdePricingFeasibilityService.notifyFeasibilityCheck(feasibilityResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);

	}

	@RequestMapping(value = "/duplicate/quote/{quoteId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> notifyFeasibilityCheck(@PathVariable("quoteId") Integer quoteId){
		Boolean response = illPricingFeasibilityService.patchRemoveDuplicatePrice(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);

	}

	/**
	 * download Outbound Prices - latest API
	 *
	 * @param response
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc/quotes/outbound/prices/file")
	public ResponseResource<HttpServletResponse> downloadOutboundPricesFile(HttpServletResponse response,
			@RequestParam("code") String quoteCode,
			@RequestParam(value = "quoteGscId", required = false) Integer quoteGscId)
			throws TclCommonException, IOException, DocumentException {
		globalOutboundRateCardService.getOutboundPricesFile(response, quoteCode, quoteGscId, (byte)0);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * API to check upload Excel Status
	 *
	 * @param quoteCode
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_EXCEL_STATUS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/gsc/negotiated/prices/file/upload/status")
	public ResponseResource<String> checkUploadExcelStatus(@RequestParam(value = "quoteCode", required = true) String quoteCode) throws TclCommonException {
		String attachmentId = globalOutboundRateCardService.uploadExcelStatus(quoteCode, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				attachmentId, Status.SUCCESS);
	}

	/**
	 * API to upload excel template

	 * @param file
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GLOBAL_OUTBOUND_TEMPLATE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/gsc/upload/negotiated/prices/template")
	public ResponseResource<String> globalOutoundNegotiatedPriceTemplate(@RequestParam(value = "file", required = true) MultipartFile file) throws TclCommonException {
		String attachmentId = globalOutboundRateCardService.uploadExcelByObjectOrFileStorageTemplate(file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				attachmentId.toString(), Status.SUCCESS);
	}

	/**
	 * API to check the template attachment Id
	 *
	 * @param quoteCode
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_EXCEL_STATUS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/gsc/negotiated/prices/template")
	public ResponseResource<String> getAttachmentIdOfNegotiatedPricesTemplate() throws TclCommonException {
		String attachmentId = globalOutboundRateCardService.getAttachmentIdOfNegotiatedPricesTemplate();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				attachmentId, Status.SUCCESS);
	}

	/**
	 * API to upload outbound prices
	 *
	 * @param quoteCode
	 * @param file
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/gsc/upload/outbound/prices")
	@Transactional
	public ResponseResource<String> uploadNegotiatedOutboundPricesInExcel(@RequestParam(value = "code", required = true) String quoteCode,
																		  @RequestParam(value = "file", required = true) MultipartFile file) throws TclCommonException {
		String attachmentId = globalOutboundRateCardService.uploadNegotiatedOutboundPricesInExcel(quoteCode, file, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				attachmentId.toString(), Status.SUCCESS);
	}

	@PostMapping("/ipc/quotes/{quoteId}/le/{quoteLeId}/cwb/save")
	public ResponseResource<String> saveCommercial(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestBody IpcCommercialBean ipcCommercialBean)
			throws TclCommonException {
		iPCCommercialService.updateDiscountedQuotePrice(quoteId, quoteToLeId, ipcCommercialBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.name(), Status.SUCCESS);
	}
	
	/**
	 * API to insert into Link_feasibility manually
	 *
	 * @param quoteCode
	 * @param file
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/mf/insert/linkFeasibility")
	public ResponseResource<String> insertInLinkFeasibility(@RequestBody LinkFeasibilityManualBean bean) throws TclCommonException {
		Integer rowId = nplQuoteService.insertLinkFeasibility(bean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				rowId.toString(), Status.SUCCESS);
	}

	
	/**
	 * API to update into Link_feasibility manually
	 *
	 * @param quoteCode
	 * @param file
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/mf/update/linkFeasibility")
	public ResponseResource<String> updateLinkFeasibility(@RequestBody LinkFeasibilityManualBean bean)
			throws TclCommonException {
		nplQuoteService.updateLinkFeasibility(bean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	/**
	 * API to get special terms and conditions
	 *
	 * @param quoteId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_LE_ATTRIBUTES)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSipTrunkAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "gsc/{quoteId}/special/terms/get")
	public ResponseResource<GscSpecialTermsConditionsBean> getSpecialTermsAndConditions(@PathVariable("quoteId") Integer quoteId) {
		GscSpecialTermsConditionsBean response = gscQuoteService.getSpecialTermsAndConditions(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
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
		illQuoteService.updateTermsInMonthsForQuoteToLe(quoteId, quoteToLeId, updateRequest, null);
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
	@RequestMapping(value = "/gvpn/quotes/{quoteId}/le/{quoteLeId}/terminmonths", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonthsGvpn(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest updateRequest) throws TclCommonException {
		gvpnQuoteService.updateTermsInMonthsForQuoteToLe(quoteId, quoteToLeId, updateRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * API to update special terms and conditions
	 *
	 * @param quoteId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_LE_ATTRIBUTES)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSipTrunkAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "gsc/{quoteId}/special/terms")
	public ResponseResource<GscSpecialTermsConditionsBean> updateSpecialTermsAndConditions(@PathVariable("quoteId") Integer quoteId,
																	@RequestBody GscSpecialTermsConditionsBean gscSpecialTermsConditionsBean) {
		GscSpecialTermsConditionsBean response = gscQuoteService.updateSpecialTermsAndConditions(quoteId, gscSpecialTermsConditionsBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
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
	@RequestMapping(value = "npl/quotes/{quoteId}/le/{quoteLeId}/terminmonths", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonthsNpl(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest updateRequest)
			throws TclCommonException {
		nplQuoteService.updateTermsInMonthsForQuoteToLe(quoteId, quoteToLeId, updateRequest);
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
	@RequestMapping(value = "/gvpn/quotes/trigger/feasiblities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@Transactional
	public ResponseResource<String> triggerForFeasibilityBeanGVPN(@RequestBody FeasibilityBean request)
			throws TclCommonException {
		gvpnPricingFeasibilityService.processFeasibility(request.getLegalEntityId(),request.getProductName());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

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
		illPricingFeasibilityService.processFeasibility(request.getLegalEntityId());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	// MOVE TO FEASIBILITY CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/npl/quotes/trigger/feasiblities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerForFeasibilityBeanNPL(@RequestBody com.tcl.dias.oms.npl.pricing.bean.FeasibilityBean request)
			throws TclCommonException {
		nplPricingFeasibilityService.processFeasibility(request.getLegalEntityId());
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
	@RequestMapping(value = "/npl/quotes/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteLeAttributeBean> feasibilityCheckNPL(@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuoteLeAttributeBean response = nplQuoteService.checkQuoteLeFeasibility(quoteToLeId);
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
	@RequestMapping(value = "/gvpn/quotes/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteLeAttributeBean> feasibilityCheckGVPN(@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuoteLeAttributeBean response = gvpnQuoteService.checkQuoteLeFeasibility(quoteToLeId);
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
	@RequestMapping(value = "/quotes/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteLeAttributeBean> feasibilityCheck(@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuoteLeAttributeBean response = illQuoteService.checkQuoteLeFeasibility(quoteToLeId);
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
	
	/**
	 * Upload multi site commercial excel
	 *
	 * @param quoteToLeId
	 * @return FileUrlResponse
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/multisite/bulkupload", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = FileUrlResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<FileUrlResponse> uploadbulksiteExcel(
			@RequestParam(name="file") MultipartFile file,
			@RequestParam("quoteToLeId") Integer quoteToLeId,
		    @RequestParam("quoteId") Integer quoteId,
		    @RequestParam("taskId") Integer taskId,
		    @RequestParam("productName") String productName) throws TclCommonException {
		FileUrlResponse urlInfo = illQuotePdfService.processMultiSiteExcel(quoteId,file, quoteToLeId,taskId,productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, urlInfo,
				Status.SUCCESS);
	}
	/**
	 * This API is to download ILL GVPN Multisite report
	 * @author Phaniteja P
	 * @param response
	 * @param quoteCode
	 * @returns ResponseResource
	 * @throws IOException and TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "ill/gvpn/multisite/report/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getIllGvpnMultiSiteExcel(
			HttpServletResponse response,@RequestParam("quoteCode") String quoteCode) throws IOException, TclCommonException {
		illQuoteService.returnIllGvpnMultisiteReportExcel(response,quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response.getHeader("Content-Disposition"),
				Status.SUCCESS);
	}
	



	
	
	/**
	 * update ExcelFileDetails
	 *
	 * @param quoteToLeId
	 * @return FileUrlResponse
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "multisite/documentuploaded", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = FileUrlResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<FileUrlResponse> updateExcelFileDetails(
		    @RequestParam("referenceId") Integer referenceId,
			@RequestParam("referenceName") String referenceName, 
			@RequestParam("attachmentType") String attachmentType,
			@RequestParam("requestId") String requestId,
			@RequestParam("attachmentId") Integer attachementId,
			@RequestParam("quoteToLeId") Integer quoteToLeId,
			@RequestParam(value = "url") String url) throws TclCommonException {
		FileUrlResponse response = illQuotePdfService.updateDocumentUploadedDetails(quoteToLeId, referenceId,
				referenceName, requestId, attachmentType, url,attachementId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * Method to get the temporary download url for other documents uploaded to the
	 * storage container
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/multisite/tempdownloadurl/documents/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getTempDownloadUrlForDocuments(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = illQuotePdfService.getTempDownloadUrlForDocuments(attachmentId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}

	/**
	 * Update license and SKU ID price values
	 *
	 * @param priceUpdateBean
	 * @param isSku
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_ORDER_EXCEL_DOWNLOAD)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ucaas/quotes/license/manualpricing")
	public ResponseResource<WebexLicenseManualPricingBean> updateLicenseComponentPrices(
			@RequestBody WebexLicenseManualPricingBean priceUpdateBean, @RequestParam(required = false) Boolean isSku)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexPricingFeasibilityService.processManualLicenseComponentPrices(priceUpdateBean, isSku),
				Status.SUCCESS);
	}

	/**
	 * Approve Manual Quotes for GSC
	 *
	 * @param quoteId
	 * @param
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/ucaas/quotes/{quoteId}/le/{quoteLeId}/manual/approve/quotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<WebexOrderDataBean> approveManualQuotesForUcaas(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, HttpServletRequest httpServletRequest)
			throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		WebexOrderDataBean response = webexOrderService.approveManualQuotes(quoteId, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * To update the manual COF uploaded details after the document is stored in the
	 * storage container
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_COF_UPLOADED_DETAILS)
	@RequestMapping(value = "/ucaas/quotes/{quoteId}/le/{quoteLeId}/manualcofuploaded", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OmsAttachmentBean> updateManualCofDetailsForUcaas(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("requestId") String requestId,
			@RequestParam("url") String url) throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = webexQuotePdfService.updateCofUploadedDetails(quoteId, quoteLeId,
				requestId, url);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, omsAttachmentBean,
				Status.SUCCESS);
	}

	/**
	 * Method to test webex access token.
	 *
	 * @return
	 * @throws TclCommonException
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "/ucaas/webexproxytest", method = RequestMethod.POST)
	public ResponseResource<AuthBean> webexProxyTest() throws TclCommonException, InterruptedException {
		AuthBean authBean = webexQuoteService.getAccessToken(0);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, authBean,
				Status.SUCCESS);
	}

	/**
	 * Method to test list quote response
	 *
	 * @param dealId
	 * @return
	 * @throws TclCommonException
	 * @throws InterruptedException
	 * @throws JAXBException
	 */
	@RequestMapping(value = "/ucaas/webexproxytest/listquote", method = RequestMethod.POST)
	public ResponseResource<String> testListQuote(@RequestParam("dealId") String dealId)
			throws TclCommonException, InterruptedException, JAXBException {
		String response = null;
		ShowQuoteType listQuoteResponse = webexQuoteService.testListQuoteResponse(dealId);
		if (Objects.nonNull(listQuoteResponse)) {
			response = Utils.convertObjectToXmlString(listQuoteResponse, ShowQuoteType.class);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to test acquire quote response.
	 *
	 * @param dealId
	 * @return
	 * @throws TclCommonException
	 * @throws InterruptedException
	 * @throws JAXBException
	 */
	@RequestMapping(value = "/ucaas/webexproxytest/acquirequote", method = RequestMethod.POST)
	public ResponseResource<String> testAcquireQuote(@RequestParam("dealId") String dealId)
			throws TclCommonException, InterruptedException, JAXBException {
		String response = null;
		ShowQuoteType acquireQuoteResponse = webexQuoteService.testAcquireQuoteResponse(dealId);
		if (Objects.nonNull(acquireQuoteResponse)) {
			response = Utils.convertObjectToXmlString(acquireQuoteResponse, ShowQuoteType.class);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get quote operation for UCaaS (TeamsDR).
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(path = "/teamsdr/quotes/{quoteId}")
	public ResponseResource<TeamsDRQuoteDataBean> getTeamsDRQuoteById(@PathVariable(name = "quoteId") Integer quoteId,
			@RequestParam(value = "isFilterNeeded", required = false) Boolean isFilterNeeded,
			@RequestParam(value = "productFamily", required = false) String productFamily) throws TclCommonException {
		TeamsDRQuoteDataBean response = teamsDRQuoteService.getTeamsDRQuote(quoteId, isFilterNeeded, productFamily);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get order operation for UCaaS.
	 *
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(path = "/teamsdr/orders/{orderId}")
	public ResponseResource<TeamsDROrderDataBean> getTeamsDROrderById(@PathVariable(name = "orderId") Integer orderId,
			@RequestParam(value = "isFilterNeeded", required = false) Boolean isFilterNeeded,
			@RequestParam(value = "productFamily", required = false) String productFamily) throws TclCommonException {
		TeamsDROrderDataBean response = teamsDROrderService.getTeamsDROrder(orderId, isFilterNeeded, productFamily);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get updated intermediate prices for teamsdr
	 *
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/teamsdr/quotes/updateprice")
	public ResponseResource<TeamsDRUnitPriceBean> getUpdatedPriceOnTab(
			@RequestParam(value = "service", required = false) String service,
			@RequestBody TeamsDRUnitPriceBean request) throws TclCommonException {
		TeamsDRUnitPriceBean response = teamsDRPricingFeasibilityService.getUpdatedPriceOnTab(service, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Process manual price for teamsdr
	 *
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/teamsdr/quotes/{quoteId}/le/{quoteLeId}/fpdetails")
	public ResponseResource<TeamsDRManualPriceBean> processManualFP(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody TeamsDRManualPriceBean request)
			throws TclCommonException {
		TeamsDRManualPriceBean response = teamsDRPricingFeasibilityService.processManualFP(quoteId, quoteLeId, request, true);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * Generate and download signed COF for TeamsDR
	 *
	 * @param orderId
	 * @param orderLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/teamsdr/orders/{orderId}/le/{orderToLeId}/signed/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateTeamsDRSignedCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = teamsDRPdfService.processApprovedCof(orderId, orderLeId, response, false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * Upload COF manually for UCaaS
	 *
	 * @param quoteId
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@PostMapping(value = "/teamsdr/quotes/{quoteId}/le/{quoteLeId}/upload/manualcof")
	public ResponseResource<TempUploadUrlInfo> uploadTeamsDRCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("file") MultipartFile file)
			throws TclCommonException {
		TempUploadUrlInfo tempDownloadUrl = teamsDRPdfService.uploadCofPdf(quoteId, quoteLeId, file);
		return new ResponseResource<TempUploadUrlInfo>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempDownloadUrl, Status.SUCCESS);
	}

	/**
	 * To update the manual COF uploaded details after the document is stored in the
	 * storage container
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_COF_UPLOADED_DETAILS)
	@RequestMapping(value = "/teamsdr/quotes/{quoteId}/le/{quoteLeId}/manualcofuploaded", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OmsAttachmentBean> updateTeamsDRManualCofDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("requestId") String requestId,
			@RequestParam("url") String url) throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = teamsDRPdfService.updateCofUploadedDetails(quoteId, quoteLeId,
				requestId, url);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, omsAttachmentBean,
				Status.SUCCESS);
	}

	/**
	 * Download COF PDF manually for TeamsDR
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@GetMapping(value = "/teamsdr/quotes/{quoteId}/le/{quoteLeId}/download/manualcof")
	public ResponseResource<String> downloadWebexCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = teamsDRPdfService.downloadCofPdf(quoteId, quoteLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * Approve Manual Quotes for TeamsDR
	 *
	 * @param quoteId
	 * @param
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/teamsdr/quotes/{quoteId}/le/{quoteLeId}/manual/approve/quotes",
					method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<TeamsDROrderDataBean> approveManualQuotesForTeamsDR(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		TeamsDROrderDataBean response = teamsDRQuoteService.approveManualQuotes(quoteId, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Approve Manual Quotes for GSC with teamsdr
	 *
	 * @param quoteId
	 * @param httpServletResponse
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/gsc/v2/quotes/{quoteId}/manual/approve/quotes", method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<GscMultiLEOrderDataBean> approveManualQuotesForVoice(
			@PathVariable("quoteId") Integer quoteId, HttpServletRequest httpServletRequest) {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		GscMultiLEOrderDataBean response = gscMultiLEOrderService.approveManualQuote(quoteId, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_UPDATE_WAIVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/updateWaiverApproval", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationResponse> updateTerminationWaiverApproval(@RequestBody TerminationWaiverRequest request) throws TclCommonException {
		TerminationResponse response = terminationWaiverService.updateTerminationWaiverApproval(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
				response, Status.SUCCESS);
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
	@GetMapping(value = "multisite/download/files/{attachmentId}")
	public ResponseEntity<Resource> dowloadfilestorageExcel(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
		Resource file = illQuotePdfService.downloadCommercialFileStorage(attachmentId);
		if (file == null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			return ResponseEntity.ok().headers(headers).body(file);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "filename=" + file.getFilename());
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return ResponseEntity.ok().headers(headers).body(file);
	}
	
	/** To upload the updated excel of ILL GVPN multi site details 
	 * @author Phaniteja P
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.MULTISITE_ILL_GVPN_FILE_UPLOAD)
	@RequestMapping(value = "/multisite/ill/gvpn/file/upload", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachment.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> uploadIllGvpnMultiSiteReport(@RequestParam("file") MultipartFile file, 
			@RequestParam("approvalLevel")String approvalLevel,@RequestParam("attachmentId")Integer attachmentId,
			@RequestParam("quoteCode")String quoteCode,
			@RequestParam("taskId")Integer taskId) throws TclCommonException, IOException {
		illQuoteService.uploadIllGvpnMultiSiteExcel(file,approvalLevel,attachmentId,quoteCode,taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	/** To upload the updated excel of multi site details 
	 * @author Nithya S
	 * @param file, approvalLevel, attachmentId, quoteCode, taskId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.MULTISITE_NPL_FILE_UPLOAD)
	@RequestMapping(value = "/multisite/npl/file/upload", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> uploadNplMultiSiteReport(@RequestParam("file") MultipartFile file, 
			@RequestParam("approvalLevel")String approvalLevel,@RequestParam("attachmentId")Integer attachmentId,
			@RequestParam("quoteCode")String quoteCode,
			@RequestParam("taskId")Integer taskId) throws TclCommonException, IOException {
		nplQuoteService.uploadNplMultiSiteExcel(file,approvalLevel,attachmentId,quoteCode,taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	
	/** To get the status of ill gvpn multisite information 
	 * @author Nithya S
	 * @param quoteCode
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.MULTISITE_ILL_GVPN_STATUS)
	@RequestMapping(value = "/multisite/ill/gvpn/status", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MultiSiteStatusBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<MultiSiteStatusBean> getMultiSiteStatusForIllGvpn(@RequestParam("quoteCode")String quoteCode)
			 throws TclCommonException, IOException {
		MultiSiteStatusBean multiSiteStatusBean = illQuoteService.getMultiSiteStatusForIllGvpn(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, multiSiteStatusBean, Status.SUCCESS);
	}
	
	/** To get the status of npl multisite information 
	 * @author Nithya S
	 * @param quoteCode
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.MULTISITE_NPL_STATUS)
	@RequestMapping(value = "/multisite/npl/status", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MultiSiteStatusBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<MultiSiteStatusBean> getMultiSiteStatusForNpl(@RequestParam("quoteCode")String quoteCode)
			 throws TclCommonException, IOException {
		MultiSiteStatusBean multiSiteStatusBean = nplQuoteService.getMultiSiteStatusForNpl(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, multiSiteStatusBean, Status.SUCCESS);
	}
	
	/**
	 * This API is to download the npl-nde feasibility Report
	 * @author 4016226-Sobhan
	 * @param response
	 * @param linkCode
	 * @returns ResponseResource
	 * @throws IOException and TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "npl/nde/multisite/report/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getNplNdeMultiSiteExcel(
			HttpServletResponse response,@RequestParam("linkCode") String linkCode) throws IOException, TclCommonException {
			LOGGER.info("Calling NplNdeMultiSiteExcel download API");
			nplQuoteService.returnNplNdeMultisiteReportExcel(response,linkCode);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
					response.getHeader("Content-Disposition"), Status.SUCCESS);
	}
	
	@RequestMapping(value = "/patch/ordercomponent", method = RequestMethod.POST)
	public ResponseResource<String> patchComponents(
			@RequestParam(value = "quoteRefId", required = true) Integer quoteReferenceId,
			@RequestParam(value = "orderRefId", required = true) Integer orderReferenceId,
			@RequestParam(value = "quoteRefName", required = true) String quoteReferenceName,
			@RequestParam(value = "componentName", required = false) String componentName)
			throws TclCommonException {
		illQuoteService.patchCopyComponents(quoteReferenceId, quoteReferenceName, orderReferenceId,componentName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	@RequestMapping(value = "/batch/onetime", method = RequestMethod.POST)
	public ResponseResource<Integer> oneTime()
			throws TclCommonException {
		Integer res=dashboardService.oneTimeBatch();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				res, Status.SUCCESS);
	}
	
	@RequestMapping(value = "/batch/mdm/si", method = RequestMethod.POST)
	public ResponseResource<Boolean> mdmoneTime(@RequestParam("quoteCode") String quoteCode)
			throws TclCommonException {
		boolean res=illMACDService.patchMdmBug(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				res, Status.SUCCESS);
	}
	
	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited editsites this method is used to
	 *            edit the ill site info in isv
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.EDIT_SITES)
	@RequestMapping(value = "/macd/{quoteId}/le/{quoteLeId}/sites/{siteId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> editMacdQuoteSites(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		QuoteDetail response = gvpnMACDService.editSiteComponentDetails(quoteId, quoteLeId, siteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
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

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_ALL_DETAILS_BY_QUOTETOLEID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LegalAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ipc/quotes/{quoteId}/le/{quoteToLeId}/attributes")
	public ResponseResource<Set<LegalAttributeBean>> getAllAttributesByQuoteToLeId(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		Set<LegalAttributeBean> response = ipcQuoteService.getAllAttributesByQuoteToLeId(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ipc/quotes/{quoteId}/le/{quoteToLeId}/attributes")
	public ResponseResource<QuoteDetail> persistListOfQuoteLeAttributesForIpc(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		QuoteDetail response = ipcQuoteService.persistListOfQuoteLeAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOWNLOAD_COF_STORAGE)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/trf/storage/downloadurl", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> DownloadTrfBasedOnOrders(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId) throws TclCommonException {
		String tempDownloadUrl = terminationService.downloadTrfFromStorageContainer(null, null, orderId,orderToLeId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}
	
	/**
     * Generate And Save Surcharge outbound Prices API
     *
     * @param response
     * @param quoteCode
     * @return {@link HttpServletResponse}
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUNG_SURCHARGE_PRICES)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/gsc/quotes/surcharge/outbound/prices")
    public ResponseResource<GscOutboundPricesDownloadBean> generateAndSaveSurchargeOutboundPrices(HttpServletResponse response, @RequestParam("code") String quoteCode)
            throws TclCommonException {
    	GscOutboundPricesDownloadBean gscOutboundPricesPricesBean = gscProductCatalogService.generateAndSaveSurchargeOutboundPrices(quoteCode, response);
    	return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,gscOutboundPricesPricesBean,Status.SUCCESS);
    }

	/**
	 * generate and save Outbound Prices - latest API
	 *
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc/quotes/outbound/prices/file")
	public ResponseResource<GscOutboundPricesDownloadBean> downloadOutboundPricesFile(@RequestParam("code") String quoteCode)
			throws TclCommonException, IOException, DocumentException {
		GscOutboundPricesDownloadBean response = globalOutboundRateCardService.generateAndSaveOutboundPricesFile(quoteCode,
				null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	
	/**
     * Generate And Save Surcharge outbound Prices API for Rate File generation
     *
     * @param response
     * @param quoteCode
     * @return {@link HttpServletResponse}
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUNG_SURCHARGE_PRICES_IN_EXCEL)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/gsc/quotes/surcharge/outbound/prices/excel")
    public ResponseResource<GscOutboundPricesDownloadBean> generateAndSaveSurchargeOutboundPricesForRateFileGen(HttpServletResponse response, @RequestParam("code") String quoteCode)
            throws TclCommonException {
    	GscOutboundPricesDownloadBean gscOutboundPricesPricesBean = gscProductCatalogService.generateAndSaveSurchargeOutboundPricesInExcel(quoteCode, response);
    	return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gscOutboundPricesPricesBean, Status.SUCCESS);
                   
    }

	@RequestMapping(value = "/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload/url", method = RequestMethod.POST)
	public ResponseResource<TempUploadUrlInfo> uploadCofPdfUrlrenewals(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam(defaultValue="NEW") String type, HttpServletResponse response) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo;
		if(type.equalsIgnoreCase("RENEWALS")) {
			tempUploadUrlInfo = renewalsIllQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}else {
		 tempUploadUrlInfo = illQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempUploadUrlInfo,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "/npl/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload/url", method = RequestMethod.POST)
	public ResponseResource<TempUploadUrlInfo> getNplCofPdfUrlRenewals(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam(defaultValue="NEW") String type, HttpServletResponse response) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo;
		if(type.equalsIgnoreCase("RENEWALS")) {
			tempUploadUrlInfo = renewalsNplQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}else {
			tempUploadUrlInfo = nplQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
	}
	
	@RequestMapping(value = "/gvpn/quotes/{quoteId}/le/{quoteLeId}/manualcof/upload/url", method = RequestMethod.POST)
	public ResponseResource<TempUploadUrlInfo> getCofPdfGvpnUrlRenewals(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response, @RequestParam(defaultValue="NEW") String type) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo;
		
		if(type.equalsIgnoreCase("RENEWALS")) {
			tempUploadUrlInfo = renewalsGvpnQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}else {
		 tempUploadUrlInfo = gvpnQuotePdfService.uploadCofPdf(quoteId, null, quoteToLeId);
		}	
		
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempUploadUrlInfo, Status.SUCCESS);
	}

	/**
	 * Update GSC product component attributes
	 *
	 * @param quoteId
	 * @param quoteGscId
	 * @param solutionId
	 * @param configurationId
	 * @param attributes
	 * @return {@link GscProductComponentBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
										 response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(
			value = "/gsc/multiLE/{quote_id}/solutions/{solution_id}/gscquotes/{gsc_quote_id}/configurations/{configuration_id}/attributes")
	public ResponseResource<List<GscProductComponentBean>> updateProductComponentAttributesMulti(
			@PathVariable("quote_id") Integer quoteId, @PathVariable("gsc_quote_id") Integer quoteGscId,
			@PathVariable("solution_id") Integer solutionId, @PathVariable("configuration_id") Integer configurationId,
			@RequestBody List<GscProductComponentBean> attributes) throws TclCommonException {
		return new ResponseResource<List<GscProductComponentBean>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, gscMultiLEAttributeService
				.updateOrDeleteProductComponentAttributes(quoteId, solutionId, quoteGscId, configurationId, attributes,
						true), Status.SUCCESS);
	}

	/**
	 * API to check upload Excel Status - MultiLE
	 *
	 * @param quoteCode
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_EXCEL_STATUS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc/multiLE/negotiated/prices/file/upload/status")
	public ResponseResource<String> checkUploadExcelStatusMultiLE(
			@RequestParam(value = "quoteCode", required = true) String quoteCode,
			@RequestParam(value = "quoteLeId", required = true) Integer quoteLeId) throws TclCommonException {
		String attachmentId = globalOutboundRateCardService.uploadExcelStatus(quoteCode, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachmentId,
				Status.SUCCESS);
	}

	/**
	 * API to check the template attachment Id - MultiLE
	 *
	 * @param quoteCode
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_EXCEL_STATUS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc/multiLE/negotiated/prices/template")
	public ResponseResource<String> getAttachmentIdOfNegotiatedPricesTemplateMultiLE() throws TclCommonException {
		String attachmentId = globalOutboundRateCardService.getAttachmentIdOfNegotiatedPricesTemplate();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachmentId,
				Status.SUCCESS);
	}

	/**
	 * generate and save Outbound Prices - MultiLE
	 *
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc/multiLE/quotes/outbound/prices/file")
	public ResponseResource<GscOutboundPricesDownloadBean> saveOutboundPricesFileMultiLE(
			@RequestParam("code") String quoteCode,
			@RequestParam(value = "quoteLeId", required = true) Integer quoteLeId)
			throws TclCommonException, IOException, DocumentException {
		GscOutboundPricesDownloadBean response = globalOutboundRateCardService
				.generateAndSaveOutboundPricesFile(quoteCode, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to upload outbound prices - MultiLE
	 *
	 * @param quoteCode
	 * @param file
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc/multiLE/upload/outbound/prices")
	@Transactional
	public ResponseResource<String> uploadNegotiatedOutboundPricesInExcel(
			@RequestParam(value = "code", required = true) String quoteCode,
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "quoteLeId", required = true) Integer quoteLeId) throws TclCommonException {
		String attachmentId = globalOutboundRateCardService
				.uploadNegotiatedOutboundPricesInExcel(quoteCode, file, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachmentId.toString(),
				Status.SUCCESS);
	}

	/**
	 * API to update special terms and conditions - MultiLE
	 *
	 * @param quoteId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_LE_ATTRIBUTES)
	@ApiResponses(
			value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSipTrunkAttributeBean.class),
					@ApiResponse(code = 403, message = Constants.FORBIDDEN),
					@ApiResponse(code = 422, message = Constants.NOT_FOUND),
					@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "gsc/multiLE/{quoteId}/le/{quoteLeId}/special/terms")
	public ResponseResource<GscSpecialTermsConditionsBean> updateSpecialTermsAndConditions(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody GscSpecialTermsConditionsBean gscSpecialTermsConditionsBean) {
		GscSpecialTermsConditionsBean response = gscMultiLEQuoteService
				.updateSpecialTermsAndConditions(quoteId, quoteLeId, gscSpecialTermsConditionsBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to get special terms and conditions - MultiLE
	 *
	 * @param quoteId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_LE_ATTRIBUTES)
	@ApiResponses(
			value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSipTrunkAttributeBean.class),
					@ApiResponse(code = 403, message = Constants.FORBIDDEN),
					@ApiResponse(code = 422, message = Constants.NOT_FOUND),
					@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "gsc/multiLE/{quoteId}/le/{quoteLeId}/special/terms/get")
	public ResponseResource<GscSpecialTermsConditionsBean> getSpecialTermsAndConditions(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId) {
		GscSpecialTermsConditionsBean response = gscMultiLEQuoteService
				.getSpecialTermsAndConditions(quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * download Outbound Prices - MultiLE
	 *
	 * @param response
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc/multiLE/quotes/outbound/prices/file")
	public ResponseResource<HttpServletResponse> downloadOutboundPricesFileMultiLE(HttpServletResponse response,
			@RequestParam("code") String quoteCode,
			@RequestParam(value = "quoteGscId", required = false) Integer quoteGscId)
			throws TclCommonException, IOException, DocumentException {
		globalOutboundRateCardService.getOutboundPricesFile(response, quoteCode, quoteGscId, (byte) 0);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to upload excel template - MultiLE
	 *
	 * @param file
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GLOBAL_OUTBOUND_TEMPLATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc/multiLE/upload/negotiated/prices/template")
	public ResponseResource<String> globalOutoundNegotiatedPriceTemplateMultiLE(
			@RequestParam(value = "file", required = true) MultipartFile file) throws TclCommonException {
		String attachmentId = globalOutboundRateCardService.uploadExcelByObjectOrFileStorageTemplate(file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, attachmentId.toString(),
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ORDER_TO_CASH_ACTION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/utils/o2cswitch/{orderCode}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateEnableo2cStatus(@PathVariable("orderCode") String orderCode,
															 @RequestParam("ACTION") String action) {
		String response = dashboardService.updateEnableo2cStatus(orderCode, action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_NSQUOTE_STATUS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/utils/nsquote/{quoteCode}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateNsQuoteStatus(@PathVariable("quoteCode") String quoteCode,
			 @RequestParam("ns") String ns) {
		String response = dashboardService.updateNsQuoteStatus(quoteCode, ns);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
     * Update ThirdPartyServiceJobs
     *
     * @param quoteCode
     * @throws TclCommonException
     */
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/utils/thirdpartyservicejob/details")
    public ResponseResource<List<ThirdPartyServiceJobBean>> getThirdPartyServiceJobDetails(@RequestParam("quotecode") String quoteCode)
            throws TclCommonException {
    	List<ThirdPartyServiceJobBean> thirdPartyServiceJobBeanList = omsSfdcUtilService.getThirdPartyServiceJobDetails(quoteCode);
    	return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, thirdPartyServiceJobBeanList, Status.SUCCESS);
                   
    }
    
    /**
     * Update ThirdPartyServiceJobs
     *
     * @param quoteCode
     * @throws TclCommonException
     */
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/utils/thirdpartyservicejob/details/update")
    public ResponseResource<String>  updateThirdPartyServiceJobDetails(@RequestBody ThirdPartyServiceJobBean thirdPartyServiceJobBean)
			throws TclCommonException {
		
		if(thirdPartyServiceJobBean.getId() !=null && thirdPartyServiceJobBean.getRequestPayload() !=null) {
			omsSfdcUtilService.updateThirdPartyServiceJobDetails(thirdPartyServiceJobBean);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
		}
    
    @PostMapping(value = "/utils/odr/remove")
    public ResponseResource<String>  deleteOdrRecords(@RequestParam("orderCode") String orderCode)
			throws TclCommonException {
		String status=dashboardService.deleteOdrRecords(orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,status,
				Status.SUCCESS);
		}
    
    @PostMapping(value = "/utils/quote/secondary/addip")
    public ResponseResource<String>  handleAdditionalIpForSec(@RequestParam("quoteCode") String quoteCode)
			throws TclCommonException {
		String status=dashboardService.handleAdditionalIpForSec(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,status,
				Status.SUCCESS);
		}
    
    @Autowired
    BundleOmsSfdcService bundleOmsSfdcService;
    
    @Autowired
    QuoteToLeRepository quoteToLeRepository;
    
    @PostMapping(value = "/utils/sfdc/create/trigger")
    public ResponseResource<String>  sfdcResume(@RequestBody String responseBody )
			throws TclCommonException {
    	LOGGER.info("reponse message received from SFDC -> {}", responseBody);
		if(responseBody.contains(CommonConstants.SDWAN)) {
			BundledOpportunityResponseBean bundledOpportunityResponseBean=(BundledOpportunityResponseBean)Utils.convertJsonToObject(responseBody, BundledOpportunityResponseBean.class);
			bundleOmsSfdcService.createOpportunityResponse(bundledOpportunityResponseBean);
		}else {
			OpportunityResponseBean opportunityResponseBean = (OpportunityResponseBean) Utils
					.convertJsonToObject(responseBody, OpportunityResponseBean.class);
			omsSfdcService.processSfdcOpportunityCreateResponse(opportunityResponseBean);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,"Success",
				Status.SUCCESS);
		}
    
    @PostMapping(value = "/utils/sfdc/product/trigger")
    public ResponseResource<String>  sfdcProductResume(@RequestBody String responseBody )
			throws TclCommonException {
		LOGGER.info("reponse message received from SFDC -> {}", responseBody);
		try {
			LOGGER.info("update response in oms:{}", responseBody);
			ProductServicesResponseBean productServicesResponseBean = (ProductServicesResponseBean) Utils
					.convertJsonToObject(responseBody, ProductServicesResponseBean.class);
			productServicesResponseBean.getProductId();
			if (productServicesResponseBean.getIsCancel() != null && productServicesResponseBean.getIsCancel()) {
				omsSfdcService.processSfdcProductServiceForCancel(productServicesResponseBean);
			}

			if (productServicesResponseBean.getProductSolutionCode() != null
					&& productServicesResponseBean.getProductSolutionCode().contains("MLC-")) {
				LOGGER.info("multicircuit flow");
				omsSfdcService.processSfdcProductServiceMulticircuit(productServicesResponseBean);
			} else if (productServicesResponseBean.getProductSolutionCode() != null
					&& productServicesResponseBean.getProductSolutionCode().contains("TERM-")) {
				LOGGER.info("termination child opty create product response flow");
				omsSfdcService.processSfdcProductServiceTermination(productServicesResponseBean);
			} else {
				if (productServicesResponseBean.getQuoteToLeId() != null) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository
							.findById(productServicesResponseBean.getQuoteToLeId());
					if (quoteToLe.isPresent()) {
						if (quoteToLe.get().getQuote().getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
							bundleOmsSfdcService.processSfdcProductService(productServicesResponseBean);
						} else {
							omsSfdcService.processSfdcProductService(productServicesResponseBean);
						}
					} else {
						omsSfdcService.processSfdcProductService(productServicesResponseBean);
					}
				} else {
					omsSfdcService.processSfdcProductService(productServicesResponseBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting the product services response", e);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,"Success",
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.GENERATE_UPLOAD_TRF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/termination/{quoteId}/le/{quoteLeId}/generateAndUploadTRF", method = RequestMethod.GET)
	public ResponseResource<String> generateAndUploadTRF(@PathVariable("quoteId") Integer quoteId,
														 @PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		terminationService.generateAndUploadTRFToStorage(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_VA_STAGE_MOVEMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/triggerTerminationVAStageMovement/{quoteCode}", method = RequestMethod.POST)
	public ResponseResource<String> triggerTerminationStageMovementByQuotCode(@PathVariable("quoteCode") String quoteCode) throws TclCommonException {
		terminationService.triggerTerminationQuoteStageToVerbalAgreement(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_VA_STAGE_MOVEMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/createProduct/quoteToLe/{quoteToLe}", method = RequestMethod.POST)
	public ResponseResource<String> createProductInSFDCByQuoteToLeId(@PathVariable("quoteToLe") Integer quoteToLe) throws TclCommonException {
		omsSfdcUtilService.triggerCreateProductInSfdcForGSC(quoteToLe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * @author HARINI SRI REKA J
	 * @link http://www.tatacommunications.com/
	 * @copyright 2021 Tata Communications Limited This method is to
	 *            update billing details in isv
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_BILLING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="utils/quoteToLe/{quoteLeId}/update/billingContact/{billingContactId}",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateBillingContact(@PathVariable("quoteLeId") Integer quoteLeId,
														 @PathVariable("billingContactId") Integer billingContactId) throws TclCommonException {
		String response = illQuoteService.updateBillingDetails(quoteLeId, billingContactId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author HARINI SRI REKA J
	 * @link http://www.tatacommunications.com/
	 * @copyright 2021 Tata Communications Limited This method is to
	 *            update cca details in isv
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_CCA_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="utils/quoteToLe/{quoteLeId}/update",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateCCADetails(@PathVariable("quoteLeId") Integer quoteLeId,
													 @RequestParam("cca") String ccaId) throws TclCommonException {
		String response = illQuoteService.updateCCADetails(quoteLeId, ccaId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author HARINI SRI REKA J
	 * @link http://www.tatacommunications.com/
	 * @copyright 2021 Tata Communications Limited updateLegalEntityProperties
	 * 					this method is used to save attributes in isv
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "quote/{quoteId}/le/{quoteToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

	/**
	 * @author Manisha Manojkumar
	 * @link http://www.tatacommunications.com/
	 * @copyright 2021 Tata Communications Limited This method is to
	 *            update selected response in isv
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE_FEASIBILITY_SELECTION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="utils/site/{siteId}/response/{responseId}/updateFeasibilityResponse/{flag}",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateSelectedResponse(@PathVariable("siteId") Integer siteId,
														   @PathVariable("responseId") Integer responseId,	 @PathVariable("flag") Integer flag) throws TclCommonException {
		String response = illQuoteService.updateSelectedResponse(siteId,responseId, flag);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Manisha Manojkumar
	 * @link http://www.tatacommunications.com/
	 * @copyright 2021 Tata Communications Limited This method is to
	 *            update selected response in isv
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_LINK_FEASIBILITY_SELECTION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value="utils/link/{linkId}/response/{responseId}/updateFeasibilityResponse/{flag}",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateSelectedResponseNPL(@PathVariable("linkId") Integer linkId,
														   @PathVariable("responseId") Integer responseId,	 @PathVariable("flag") Integer flag) throws TclCommonException {
		String response = nplQuoteService.updateSelectedResponseLink(linkId,responseId, flag);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author Vignesh
	 * @copyright 2021 Tata Communications Limited This method is to
	 *            update mf task triggered for IAS and GVPN
	 * @return String response
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.MF_TASK_TRIGGERED)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/task/triggered",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String>  updateMfTaskTriggered(@RequestParam(required = false, name = "siteId") Integer siteId)
			throws TclCommonException {
		    String response = illQuoteService.updateMfTaskTriggered(siteId);	
		    return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
	}
	
	/**
	 * @author Vignesh
	 * @copyright 2021 Tata Communications Limited This method is to
	 *            update mf task triggered for NPL and NDE
	 * @return String response
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.MF_TASK_TRIGGERED)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/npl/mf/task/triggered",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateNplMfTaskTriggered(@RequestParam(required = false, name = "linkId") Integer linkId)
			throws TclCommonException {
		     String response = nplQuoteService.updateNplMfTaskTriggered(linkId);
		     return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
						Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_COMPONENT_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/utils/update/attributes", method = RequestMethod.POST)
    public ResponseResource<String>  updateCpeRouter(@RequestBody QuoteComponentAttributeUpdateRequest updateRequest)
			throws TclCommonException {
		String status = dashboardService.updateCpeRouter(updateRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,status,
				Status.SUCCESS);
		}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_COMPONENT_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/utils/serviceDetails", method = RequestMethod.GET)
    public ResponseResource<List<ServiceDetailedInfoBean>>  serviceInventoryDetails(@RequestParam("serviceId") String serviceId)
			throws TclCommonException {
		List<ServiceDetailedInfoBean> serviceDetails = dashboardService.utilityServiceInventoryDetails(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,serviceDetails,
				Status.SUCCESS);
		}
	
	/**
	 * API to update into Site_feasibility manually
	 *
	 * @param quoteCode
	 * @param file
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.UPLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/mf/update/siteFeasibility")
	public ResponseResource<String> updatesiteFeasibility(@RequestBody SiteFeasibilityManualBean siteFeasibilityManualBean)
			throws TclCommonException {
		illQuoteService.updateSiteFeasibilityResponse(siteFeasibilityManualBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	/**
	 * @author HARINI SRI REKA J
	 * @link http://www.tatacommunications.com/
	 * @copyright 2021 Tata Communications Limited updateSfdcOrderType
	 * 					this method is used to save sfdc order subtype in isv
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_ORDER_TYPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/utils/update/erfOrderType", method = RequestMethod.POST)
	public ResponseResource<String>  updateSfdcOrderType(@RequestBody OrderIllSitetoServiceBean request)
			throws TclCommonException {
		String status = dashboardService.updateSfdcOrderTypeinOrderSitetoService(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,status,
				Status.SUCCESS);
	}

	/**
	 * @author Manisha Manojkumar
	 * @link http://www.tatacommunications.com/
	 * @copyright 2021 Tata Communications Limited fetchFeasibilityPricingPayload
	 * 					this method is used to fetch feasibility and pricing request and resposone for given quote code
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.FETCH_FEASIBILITY_PRICING_PAYLOAD)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/utils/fetchRequestResponse/{quoteCode}", method = RequestMethod.GET)
	public ResponseResource<List<FeasibilityPricingPayloadAudit>>  fetchFeasibilityPricingPayload(@PathVariable String quoteCode)
			throws TclCommonException {
		List<FeasibilityPricingPayloadAudit> feasibilityPricingPayloadAudit= dashboardService.fetchFeasibilityPricingRequestResponse(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,feasibilityPricingPayloadAudit,
				Status.SUCCESS);
	}
}
