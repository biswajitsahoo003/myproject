package com.tcl.dias.oms.gsc.controller.v1;

import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscAttachmentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.controller.BaseController;
import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderEnrichmentService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.service.v1.GscProductCatalogService;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * All public APIs of Order related to GSIP Products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/gsc/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class GscOrderController extends BaseController {

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    GscQuotePdfService gscQuotePdfService;

    @Autowired
    GscOrderDetailService gscOrderDetailService;

    @Autowired
    GscProductCatalogService gscProductCatalogService;

    @Autowired
    GscOrderEnrichmentService gscOrderEnrichmentService;

    /**
     * Get a order by ID
     *
     * @param orderId
     * @return GscOrderBean
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_ORDER_DETAILS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{orderId}")
    public ResponseEntity getGscOrderById(@PathVariable("orderId") Integer orderId) {
        return gscOrderService.getGscOrderById(orderId).toEither().fold(this::error, this::success);
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
    @PostMapping(value = "/{orderId}/solutions/{orderSolutionId}/gscorders/{gscOrderId}/configurations/{orderConfigurationId}/attributes")
    public ResponseEntity updateOrderProductComponentAttributes(@PathVariable("orderId") Integer orderId,
                                                                @PathVariable("gscOrderId") Integer orderGscId, @PathVariable("orderSolutionId") Integer solutionId,
                                                                @PathVariable("orderConfigurationId") Integer configurationId,
                                                                @RequestBody GscApiRequest<List<GscOrderProductComponentBean>> attributes) {
        return Try.of(() -> gscOrderService.updateOrderProductComponentAttributes(orderId, solutionId, orderGscId,
                configurationId, attributes.getData())).toEither().fold(this::error, this::success);
    }

    /**
     * Bulk update configuration attributes for multiple solutions and product
     * components
     *
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
    @PostMapping(value = "/{orderId}/solutions/gscorders/configurations/attributes")
    public ResponseEntity updateOrderProductComponentAttributesForSolutions(@PathVariable("orderId") Integer orderId,
                                                                            @RequestBody GscApiRequest<List<GscOrderSolutionBean>> solutions) {
        return Try.of(() -> gscOrderService.updateProductComponentAttributesForSolutions(orderId, solutions.getData()))
                .toEither().fold(this::error, this::success);
    }

    /**
     * Fetch toll free numbers for given configuration
     *
     * @param configurationId
     * @param count
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CONFIGURATION_NUMBERS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscTfnBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/configurations/{orderConfigurationId}/numbers")
    public ResponseEntity getAvailableNumbers(@PathVariable("orderConfigurationId") Integer configurationId,
                                              @RequestParam(required = false) String city,
                                              @RequestParam(defaultValue = "10", required = false) Integer count,
                                              @RequestParam("autoReserve") Boolean autoReserve) {
        return Try.of(() -> gscOrderDetailService.getAvailableNumbers(configurationId, city, count, autoReserve))
                .toEither().fold(this::error, this::success);
    }

    /**
     * Save TFN numbers for specified configuration
     *
     * @param configurationId
     * @param gscTfns
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.RESERVE_CONFIGURATION_NUMBERS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscTfnBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/configurations/{orderConfigurationId}/numbers")
    public ResponseEntity saveNumbers(@PathVariable("orderConfigurationId") Integer configurationId,
                                      @RequestBody List<GscTfnBean> gscTfns) {
        return Try.of(() -> gscOrderDetailService.saveNumbers(configurationId, gscTfns)).toEither().fold(this::error,
                this::success);
    }

    /**
     * Get Order Configuration based on configuration Id
     *
     * @param orderId
     * @param orderGscId
     * @param solutionId
     * @param configurationId
     * @return GscOrderConfigurationBean
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CONFGIGURATIONS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderConfigurationBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{orderId}/solutions/{orderSolutionId}/gscorders/{gscOrderId}/configurations/{orderConfigurationId}")
    public ResponseEntity getOrderConfigurationDetails(@PathVariable("orderId") Integer orderId,
                                                       @PathVariable("gscOrderId") Integer orderGscId, @PathVariable("orderSolutionId") Integer solutionId,
                                                       @PathVariable("orderConfigurationId") Integer configurationId,
                                                       @RequestParam(value = "attributes", defaultValue = "false") Boolean fetchAttributes) {
        return gscOrderDetailService
                .getOrderConfigurationDetails(orderId, solutionId, orderGscId, configurationId, fetchAttributes)
                .toEither().fold(this::error, this::success);
    }

    /**
     * Update one or more order configurations data by id
     *
     * @param orderId
     * @param orderGscId
     * @param solutionId
     * @return GscOrderConfigurationBean
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_CONFIGURATIONS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderConfigurationBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/{orderId}/solutions/{orderSolutionId}/gscorders/{gscOrderId}/configurations")
    public ResponseEntity updateOrderConfigurationDetails(@PathVariable("orderId") Integer orderId,
                                                          @PathVariable("gscOrderId") Integer orderGscId, @PathVariable("orderSolutionId") Integer solutionId,
                                                          @RequestBody GscApiRequest<List<GscOrderConfigurationBean>> configurationsUpdateRequest) {
        return Try
                .success(configurationsUpdateRequest).map(configurationsRequest -> gscOrderDetailService
                        .updateOrderConfigurations(orderId, solutionId, orderGscId, configurationsRequest.getData()))
                .toEither().fold(this::error, this::success);
    }

    /**
     * Generate COF PDF for given ID
     *
     * @param quoteId
     * @param response
     * @return
     * @author Thamizhselvi Perumal
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_COF_PDF)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{quoteId}/cofpdf")
    public ResponseEntity<?> generateCofPdf(@PathVariable("quoteId") Integer quoteId, HttpServletResponse response) {
        return Try.of(() -> gscQuotePdfService.processCofPdf(quoteId, response, false, false)).toEither()
                .fold(this::error, this::success);
    }

    /**
     * Get order legal attributes by id
     *
     * @param orderId
     * @param orderToLeId
     * @return {@link GscOrderProductComponentsAttributeValueBean}
     * @author VISHESH AWASTHI
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderProductComponentsAttributeValueBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{orderId}/legalentities/{orderLeId}/attributes")
    public ResponseEntity getOrderLeAttributes(@PathVariable("orderId") Integer orderId,
                                               @PathVariable("orderLeId") Integer orderToLeId) {
        return Try.of(() -> gscOrderService.getOrderToLeAttributes(orderId, orderToLeId)).toEither().fold(this::error,
                this::success);
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
    @PostMapping(value = "/{orderId}/legalentities/{orderLeId}/attributes")
    public ResponseEntity saveOrderLeAttributes(@PathVariable("orderId") Integer orderId,
                                                @PathVariable("orderLeId") Integer orderToLeId,
                                                @RequestBody GscApiRequest<GscOrderAttributesBean> request) {
        return Try.of(
                () -> gscOrderService.saveOrderToLeAttributes(orderId, orderToLeId, request.getData().getAttributes()))
                .toEither().fold(this::error, this::success);
    }

    /**
     * return order attributes based on order id
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDER_ATTRIBUTES)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderProductComponentsAttributeValueBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{orderId}/attributes")
    public ResponseEntity getOrderAttributes(@PathVariable("orderId") Integer orderId) {
        return Try.success(orderId).map(gscOrderService::getOrderAttributes).toEither().fold(this::error,
                this::success);
    }

    /**
     * save order attributes
     *
     * @param orderId
     * @param attributes
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderProductComponentsAttributeValueBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/{orderId}/attributes")
    public ResponseEntity saveOrderAttributes(@PathVariable("orderId") Integer orderId,
                                              @RequestBody GscApiRequest<GscOrderAttributesBean> attributes) {
        return Try.of(() -> gscOrderService.saveOrderAttributes(orderId, attributes.getData().getAttributes()))
                .toEither().fold(this::error, this::success);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CONFIGURATION_DOCUMENTS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscCountrySpecificDocumentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/configurations/{orderConfigurationId}/documents")
    public ResponseEntity getApplicableDocuments(@PathVariable("orderConfigurationId") Integer configurationId) {
        return Try.of(() -> gscOrderDetailService.getDocumentsForConfigurationId(configurationId)).toEither()
                .fold(this::error, this::success);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPLOAD_CONFIGURATION_DOCUMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscAttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/configurations/{orderConfigurationId}/documents/{documentId}")
    public ResponseEntity uploadConfigurationDocument(@RequestParam("file") MultipartFile file,
                                                      @PathVariable("orderConfigurationId") Integer configurationId,
                                                      @PathVariable("documentId") Integer documentId) {
        return Try.of(() -> gscOrderDetailService.uploadDocumentForConfiguration(file, configurationId, documentId))
                .toEither().fold(this::error, this::success);
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
    @PostMapping(value = "/object/configurations/{orderConfigurationId}/documents/{documentId}")
    public ResponseEntity uploadObjectConfigurationDocument(@RequestParam("file") MultipartFile file,
                                                            @PathVariable("orderConfigurationId") Integer configurationId,
                                                            @PathVariable("documentId") Integer documentId) {
        return Try.of(() -> gscOrderDetailService.uploadObjectConfigurationDocument(file, configurationId)).toEither()
                .fold(this::error, this::success);
    }

    /**
     * API to update the file uploaded details in oms level
     *
     * @param configurationId
     * @param documentId
     * @param requestId
     * @param urlPath
     * @return {@link GscAttachmentBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPLOAD_CONFIGURATION_DOCUMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscAttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/update/object/configurations/{orderConfigurationId}/documents/{documentId}")
    public ResponseEntity updateUploadObjectConfigurationDocument(
            @PathVariable("orderConfigurationId") Integer configurationId,
            @PathVariable("documentId") Integer documentId, @RequestParam("requestId") String requestId,
            @RequestParam(value = "url") String urlPath) {
        return Try.of(() -> gscOrderDetailService.updateUploadObjectConfigurationDocument(configurationId, documentId,
                requestId, urlPath)).toEither().fold(this::error, this::success);
    }

    /**
     * API to download the file stored via object storage.
     *
     * @param configurationId
     * @param documentId
     * @param downloadTemplate
     * @return {@link Resource}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_CONFIGURATION_DOCUMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Resource.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/configurations/{orderConfigurationId}/documents/{documentId}")
    public ResponseEntity downloadConfigurationDocument(@PathVariable("orderConfigurationId") Integer configurationId,
                                                        @PathVariable("documentId") Integer documentId,
                                                        @RequestParam(name = "template", defaultValue = "false", required = false) Boolean downloadTemplate) {
        return gscOrderDetailService.fetchConfigurationAttachmentForId(configurationId, documentId, downloadTemplate)
                .toEither().fold(this::error, resource -> {

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

                });
    }

    /**
     * Get Tiger Management data
     *
     * @param orderId
     * @param type
     * @return
     */
    @GetMapping(value = "/managementdata/{orderId}")
    public ResponseEntity getOrderManagementData(@PathVariable("orderId") Integer orderId, @RequestParam("type") String type) {
        return gscOrderService.getGscOrderManagementData(orderId, type).toEither().fold(this::error, this::success);
    }

    /**
     * Update Order Status
     *
     * @param orderId
     * @param orderToLeId
     * @param status
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ORDER_LE_STATUS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/{orderId}/le/{orderToLeId}/stage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateOrderToLeStatus(@PathVariable("orderId") Integer orderId,
                                                @PathVariable("orderToLeId") Integer orderToLeId, @RequestParam("stage") String status) {
        return Try.of(() -> gscOrderService.updateOrderToLeStatus(orderToLeId, status)).toEither().fold(this::error,
                this::success);

    }

    /**
     * Create Tiger Request for Delete Number and Change Outpulse
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_TIGER_REQUEST)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/update/{orderId}/tiger/request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> updateTigerRequest(@PathVariable("orderId") Integer orderId) {
        return Try.success(orderId)
                .map(gscOrderService::updateTigerRequest)
                .map(ResponseResource::new)
                .get();

    }

    /**
     * download Outbound Prices API
     *
     * @param response
     * @param quoteCode
     * @return {@link HttpServletResponse}
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/outbound/prices")
    public ResponseEntity downloadOutboundPrices(HttpServletResponse response, @RequestParam("code") String quoteCode)
            throws TclCommonException {
        return gscProductCatalogService.downloadOutboundPrices(response, quoteCode).toEither().fold(this::error, resource -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
            headers.add("Access-Control-Allow-Headers", "Content-Type");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            return ResponseEntity.ok().headers(headers).body(resource);
        });
    }

    /**
     * Generate and Save A-Z outbound Prices File API
     *
     * @param response
     * @param quoteCode
     * @return {@link HttpServletResponse}
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/outbound/prices")
    public ResponseEntity generateOutboundPrices(HttpServletResponse response, @RequestParam("code") String quoteCode)
            throws TclCommonException {
        return Try.of(() -> gscProductCatalogService.generateAndSaveOutboundPrices(quoteCode, response)).toEither()
                .fold(this::error, this::success);

    }

    /**
     * @param configurationId
     * @return
     * @link http://www.tatacommunications.com/ This API will return list of TFN
     * numbers for LNS and ACANS configuration grouped by city code and
     * portability
     * @author VISHESH AWASTHI
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_LNS_CONFIGURATION_NUMBERS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscTfnBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping("/configurations/{service_type}/{configurationId}/numbers")
    public ResponseEntity getCityNumberConfigurationList(@PathVariable("configurationId") Integer configurationId,
                                                         @PathVariable("service_type") String serviceType) {
        return Try.of(() -> gscOrderDetailService.getCityNumberConfiguration(configurationId, serviceType)).toEither()
                .fold(this::error, this::success);
    }

    /**
     * Updates the order status and stage
     *
     * @param orderId
     * @param orderToLeId
     * @param orderConfigurationId
     * @param request
     * @return
     * @throws TclCommonException
     * @author VISHESH AWASTHI
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderStatusStageUpdate.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping("/{orderId}/le/{orderToLeId}/configuration/{orderConfigurationId}/stage")
    public ResponseEntity updateGscOrderDetailSiteStatus(@PathVariable("orderId") Integer orderId,
                                                         @PathVariable("orderToLeId") Integer orderToLeId,
                                                         @PathVariable("orderConfigurationId") Integer orderConfigurationId,
                                                         @RequestBody GscApiRequest<GscOrderStatusStageUpdate> request) throws TclCommonException {

        return Try.of(
                () -> gscOrderService.updateOrderConfigurationStageStatus(orderConfigurationId, request.getData()))
                .toEither().fold(this::error, this::success);
    }

    /**
     * Delete TFN numbers for specified configuration based on city
     *
     * @param configurationId
     * @param gscTfns
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.RESERVE_CONFIGURATION_NUMBERS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscTfnBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/configurations/{orderConfigurationId}/servicetypes/{serviceType}/numbers")
    public ResponseEntity deleteNumbers(@PathVariable("orderConfigurationId") Integer configurationId,
                                        @PathVariable("serviceType") String serviceType, @RequestBody List<GscTfnBean> gscTfns) {
        return Try.of(() -> gscOrderDetailService.deleteNumbers(configurationId, gscTfns, serviceType)).toEither()
                .fold(this::error, this::success);
    }

    /**
     * API to get document uploaded status for every configuration
     *
     * @param orderGscId
     * @return {@link GscAttachmentBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CONFIGURATION_DOCUMENTS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscAttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping("/configurations/documents/{orderGscId}")
    public ResponseEntity getDocumentStatus(@PathVariable("orderGscId") Integer orderGscId,
                                            @RequestParam(name = "portingRequired") String portingRequired) {
        return Try.of(() -> gscOrderDetailService.getDocumentStatus(orderGscId, portingRequired)).toEither()
                .fold(this::error, this::success);
    }

    /**
     * Generate and Save A-Z outbound Prices File API
     *
     * @param response
     * @param quoteCode
     * @return {@link HttpServletResponse}
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/outbound/prices/excel")
    public ResponseEntity generateOutboundPricesExcel(HttpServletResponse response, @RequestParam("code") String quoteCode)
            throws TclCommonException {

        return Try.of(() -> gscProductCatalogService.generateAndSaveOutboundPricesExcel(quoteCode, response)).toEither()
                .fold(this::error, this::success);

    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_CONFIGURATION_DOCUMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/configurations/{orderConfigurationId}/temporaryUrl/{documentId}")
    public ResponseEntity downloadConfigurationDocumentTemporaryUrl(@PathVariable("orderConfigurationId") Integer configurationId,
                                                                    @PathVariable("documentId") Integer documentId,
                                                                    @RequestParam(name = "template", defaultValue = "false", required = false) Boolean downloadTemplate) {
        return gscOrderDetailService.getObjectStorageConfigurationAttachmentForId(configurationId, documentId, downloadTemplate)
                .toEither().fold(this::error, this::success);
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
    @GetMapping(value = "/emergency/address/{configuarationId}")
    public ResponseResource<List<String>> getEmergencyAddress(@PathVariable("configuarationId") Integer configuarationId) throws TclCommonException {
        List<String> response = gscOrderService.getEmergencyAddress(configuarationId);
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * API for Order Enrichment Bulk upload excel
     *
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.ORDER_ENRICHMENT_BULK_UPLOAD_EXCEL)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/enrichment/bulk/upload/{orderId}")
    public ResponseResource<CommonValidationResponse> orderEnrichmentExcelBulkUpload(@PathVariable("orderId") Integer orderId,
                                                                                     @RequestParam("file") MultipartFile file)
            throws Exception {
        CommonValidationResponse response = gscOrderEnrichmentService.excelBulkUpload(orderId, file);
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API for download Order Enrichment bulk upload sample excel
     *
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.ORDER_ENRICHMENT_TEMPLATE_EXCEL)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping("/enrichment/template/download/{orderId}")
    public ResponseEntity<String> getGscOrderDetailsExcel(@PathVariable("orderId") Integer orderId,
                                                          HttpServletResponse response) throws TclCommonException, IOException {
        gscOrderEnrichmentService.downloadTemplateExcel(orderId, response);
        return null;
    }


}
