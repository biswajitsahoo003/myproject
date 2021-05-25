package com.tcl.dias.customer.controller.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.*;
import com.tcl.dias.customer.dto.CustomerLegalEntityDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityProductResponseDto;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.customer.bean.AttachmentBean;
import com.tcl.dias.customer.bean.CallidusIncentiveRequestBean;
import com.tcl.dias.customer.bean.CustomerLegalEntityBean;
import com.tcl.dias.customer.bean.PartnerCommissionsResponse;
import com.tcl.dias.customer.bean.PartnerCustomerLeAttributeBean;
import com.tcl.dias.customer.bean.PartnerMonthlyIncentive;
import com.tcl.dias.customer.bean.PartnerNNIBean;
import com.tcl.dias.customer.bean.PartnerProductClassificationBean;
import com.tcl.dias.customer.dto.AttributesDto;
import com.tcl.dias.customer.dto.CustomerDto;
import com.tcl.dias.customer.service.v1.PartnerCustomerService;
import com.tcl.dias.customer.service.v1.PartnerDashboardPdfReportService;
import com.tcl.dias.customer.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Partner related information for Partner Portal
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/partners")
public class PartnerCustomerController {

    @Autowired
    PartnerCustomerService partnerCustomerService;

    @Autowired
    PartnerDashboardPdfReportService partnerDashboardPdfReportService;

    /**
     * Method returns the partner legal entities by the partner id
     *
     * @param partnerId
     * @return {@link ResponseResource<List<PartnerLegalEntityBean>>}
     */
    @ApiOperation(value = SwaggerConstants.Customer.GET_PARTNER_LEGAL_ENTITIES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @RequestMapping(value = "/{partnerid}/legalentities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<PartnerLegalEntityBean>> getPartnerLegalEntities(
            @PathVariable("partnerid") Integer partnerId) {
        List<PartnerLegalEntityBean> partnerLegalEntities = partnerCustomerService.getPartnerLegalEntitiesByPartnerId(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, partnerLegalEntities,
                Status.SUCCESS);
    }

    /**
     * Method returns the partner classifications
     *
     * @param partnerId
     * @return {@link ResponseResource<PartnerProductClassificationBean>}
     */
    @ApiOperation(value = SwaggerConstants.Customer.GET_PARTNER_LEGAL_ENTITIES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @RequestMapping(value = "/{partnerid}/classification", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<PartnerProductClassificationBean> getPartnerClassifications(
            @PathVariable("partnerid") Integer partnerId) {
        PartnerProductClassificationBean partnerProductClassificationBean = partnerCustomerService.getPartnerClassifications(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, partnerProductClassificationBean,
                Status.SUCCESS);
    }

    /**
     * Method returns the customer legal entities by the partner legal entity id
     *
     * @param partnerId
     * @param partnerLegalId
     * @return
     */
    @ApiOperation(value = SwaggerConstants.Customer.GET_PARTNER_LEGAL_ENTITIES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @RequestMapping(value = "/{partnerid}/legalentities/{partnerlegalid}/customer/legalentities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<CustomerLegalEntityBean> getCustomerLegalEntity(
            @PathVariable("partnerid") Integer partnerId, @PathVariable("partnerlegalid") Integer partnerLegalId) {
        CustomerLegalEntityBean customerLegalEntityBean = partnerCustomerService.getCustomerLegalEntity(partnerLegalId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLegalEntityBean,
                Status.SUCCESS);
    }

    /**
     * Method returns the customer legal entities by the partner legal entity id
     *
     * @param partnerId
     * @param partnerLegalId
     * @return
     */
    @ApiOperation(value = SwaggerConstants.Customer.GET_PARTNER_LEGAL_ENTITIES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @RequestMapping(value = "/{partnerid}/legalentities/{partnerlegalid}/customer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<CustomerDto>> getCustomers(
            @PathVariable("partnerid") Integer partnerId, @PathVariable("partnerlegalid") Integer partnerLegalId) {
        List<CustomerDto> customerDtos = partnerCustomerService.getCustomers(partnerLegalId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerDtos,
                Status.SUCCESS);
    }

    /**
     * Method returns the list of master product family
     *
     * @param partnerId
     * @return
     */
    @ApiOperation(value = SwaggerConstants.Customer.GET_PARTNER_LEGAL_ENTITIES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @RequestMapping(value = "/{partnerId}/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<MstProductFamilyBean>> getProducts(
            @PathVariable("partnerId") Integer partnerId, @RequestParam String classification) throws TclCommonException {
        List<MstProductFamilyBean> mstProductFamilyBeans = partnerCustomerService.getProducts(partnerId, classification);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, mstProductFamilyBeans,
                Status.SUCCESS);
    }

    /**
     * API to upload partner documents during partner onboarding
     * SellWith and SellThrough Requires partner type - new or existing
     * Product document (Service schedule) requires product name
     *
     * @param file
     * @param partnerLeId
     * @param attachmentType
     * @param partnerType
     * @param productName
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.Partner.UPLOAD_PARTNER_DOCUMENTS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/le/upload/documents")
    public ResponseResource<AttachmentBean> uploadPartnerDocuments(@RequestParam("file") MultipartFile file,
                                                                   @RequestParam("partnerLeId") Integer partnerLeId,
                                                                   @RequestParam("attachmentType") String attachmentType,
                                                                   @RequestParam(value = "partnerType", required = false) String partnerType,
                                                                   @RequestParam(value = "productName", required = false) String productName) throws TclCommonException {
        AttachmentBean response = partnerCustomerService.uploadPartnerFiles(file, partnerLeId, attachmentType, partnerType, productName);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get list of partner documents
     * NEW partner doesnt require MSA and addendum document
     * Exisisting partner doesnt require NDA and common T&C document
     *
     * @param partnerLeId
     * @param classification
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.Partner.UPLOAD_PARTNER_DOCUMENTS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/le/{partnerLeId}/documents/list")
    public ResponseResource<List<AttachmentBean>> getPartnerDocumentsList(@PathVariable("partnerLeId") Integer partnerLeId,
                                                                          @RequestParam("classification") String classification,
                                                                          @RequestParam("productName") String productName) {
        List<AttachmentBean> response = partnerCustomerService.getPartnerDocumentsList(partnerLeId, classification, productName);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to download the partner attachment document
     *
     * @param partnerLeId
     * @param attachmentId
     * @return
     */
    @ApiOperation(value = SwaggerConstants.Partner.DOWNLOAD_PARTNER_DOCUMENTS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/le/{partnerLeId}/download/{attachmentId}")
    public ResponseEntity<Resource> getPartnerDocuments(@PathVariable("partnerLeId") Integer partnerLeId,
                                                        @PathVariable("attachmentId") Integer attachmentId) {
        Resource file = partnerCustomerService.getPartnerDocuments(partnerLeId, attachmentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "attachment; filename=" + file.getFilename());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers).body(file);
    }

    /**
     * API to get temporary download url for attachmentID - Object Storage
     *
     * @param attachmentId
     * @return {@link String}
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.Customer.DOWNLOAD_FILE_DOC)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/le/{partnerLeId}/tempdownloadurl/{attachmentId}")
    public ResponseResource<String> getTemporaryDownloadUrlForPartnerDocuments(@PathVariable("partnerLeId") Integer partnerLeId,
                                                                               @PathVariable("attachmentId") Integer attachmentId) {
        String tempDownloadUrl = partnerCustomerService.getAttachmentTempDownloadUrl(partnerLeId, attachmentId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl, Status.SUCCESS);
    }

    /**
     * API to update the NEW partner to existing once a order is placed
     *
     * @param partnerLeId
     * @return {@link PartnerCustomerLeAttributeBean}
     */
    @ApiOperation(value = SwaggerConstants.Partner.UPLOAD_PARTNER_DOCUMENTS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerCustomerLeAttributeBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/le/{partnerLeId}/partnerType")
    public ResponseResource<PartnerCustomerLeAttributeBean> updatePartnerType(@PathVariable("partnerLeId") Integer partnerLeId) {
        PartnerCustomerLeAttributeBean response = partnerCustomerService.updatePartnerType(partnerLeId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get Partner Monthly Incentive details from Callidus and SAP
     *
     * @param partnerId
     * @return {@link String}
     */
    @ApiOperation(value = SwaggerConstants.Partner.GET_CALLIDUS_INFO)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/callidus/sap/incentives")
    public ResponseResource<Map> getPartnerMonthlyIncentives(@PathVariable("partnerId") Integer partnerId) {
        Map<String, PartnerMonthlyIncentive> response = partnerCustomerService.getPartnerMonthlyIncentives(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get Partner Commision Details details from Callidus
     *
     * @param partnerId
     * @return {@link PartnerCommissionsResponse}
     */
    @ApiOperation(value = SwaggerConstants.Partner.GET_CALLIDUS_INFO)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/{partnerId}/callidus/incentives/details")
    public ResponseResource<PartnerCommissionsResponse> getPartnerCommissionDetails(@PathVariable("partnerId") Integer partnerId,
                                                                                    @RequestBody CallidusIncentiveRequestBean callidusIncentiveRequestBean) {
        PartnerCommissionsResponse response = partnerCustomerService.getPartnerCommissionDetails(partnerId, callidusIncentiveRequestBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to download Partner Compensation Details Report from Callidus
     *
     * @param partnerId
     * @param response
     * @return {@link String}
     */
    @ApiOperation(value = SwaggerConstants.Partner.DOWNLOAD_PARTNER_DOCUMENTS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @RequestMapping(value = "/{partnerId}/compensation/detail/report", method = RequestMethod.GET)
    public ResponseResource<String> downloadCompensationReport(@PathVariable("partnerId") Integer partnerId, @RequestParam(value = "reportName") String reportName, HttpServletResponse response) {
        partnerDashboardPdfReportService.processCompensationDetailReport(partnerId, reportName, response);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);
    }

    /**
     * API to get Partner NNI ID
     *
     * @param partnerId
     * @return {@link String}
     */
    @ApiOperation(value = SwaggerConstants.Partner.GET_PARTNER_NNI_ID)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/nni")
    public ResponseResource<PartnerNNIBean> getPartnerNNIID(@PathVariable("partnerId") Integer partnerId) {
        PartnerNNIBean response = partnerCustomerService.getPartnerNNIIDs(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get product based on Partner ID
     *
     * @param partnerId
     * @return {@link String}
     */
    @ApiOperation(value = SwaggerConstants.Partner.GET_PARTNER_PRODUCT_CLASSIFICATION)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = MstProductFamilyBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/productclassification")
    public ResponseResource<Set<MstClassificationProductBean>> getPartnerProductClassification(@PathVariable("partnerId") Integer partnerId)throws TclCommonException {
        Set<MstClassificationProductBean> response = partnerCustomerService.getPartnerProductClassification(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get product based on Partner ID
     *
     * @param partnerId
     * @return {@link String}
     */
    @ApiOperation(value = SwaggerConstants.Partner.GET_PARTNER_PRODUCT_CLASSIFICATION)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = MstProductFamilyBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/productclassificationv1")
    public ResponseResource<Set<MstProductFamilyBean>> getPartnerProductClassificationv1(@PathVariable("partnerId") Integer partnerId)throws TclCommonException {
        Set<MstProductFamilyBean> response = partnerCustomerService.getPartnerProductClassificationv1(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get partner profile based on Partner ID
     *
     * @param partnerId
     * @return {@link String}
     */
    @ApiOperation(value = SwaggerConstants.Partner.GET_PARTNER_PROFILE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerProfileBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/profile")
    public ResponseResource<Set<PartnerProfileBean>> getPartnerProfileByPartnerId(@PathVariable("partnerId") Integer partnerId)throws TclCommonException {
        Set<PartnerProfileBean> response = partnerCustomerService.getPartnerProfileListByPartnerId(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * Method returns the partner classifications
     *
     * @param partnerId
     * @return {@link ResponseResource<PartnerProductClassificationBean>}
     */
    @ApiOperation(value = SwaggerConstants.Customer.GET_PARTNER_LEGAL_ENTITIES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesDto.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @RequestMapping(value = "/{partnerid}/isenabled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> getIsClassificationsForPartner(
            @PathVariable("partnerid") Integer partnerId,@RequestParam(value = "classification") String classification) {
        String response = partnerCustomerService.getIsClassificationsForPartner(partnerId,classification);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * API to get Partner Le's Currency
     *
     * @param partnerLeId
     * @return {@link String}
     */
    @ApiOperation(value = SwaggerConstants.Partner.GET_PARTNER_NNI_ID)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/le/{partnerLeId}/currency")
    public ResponseResource<List<String>> getPartnerLeCurrency(@PathVariable("partnerLeId") Integer partnerLeId) {
        List<String> response = partnerCustomerService.getPartnerLeCurrencies(partnerLeId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * Method returns the partner's all end customer name by the partner id
     *
     * @param partnerId
     * @return {@link ResponseResource<List<PartnerEndCustomerLeBean>>}
     */
    @ApiOperation(value = SwaggerConstants.Customer.GET_PARTNER_END_CUSTOMER_NAME)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @RequestMapping(value = "/{partnerid}/endCustomerlegalentities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<PartnerEndCustomerLeBean>> getEndCustomerLegalEntitiesByPartnerId(
            @PathVariable("partnerid") Integer partnerId) {
        List<PartnerEndCustomerLeBean> endCustomerLesByPartnerId = partnerCustomerService.getEndCustomerLesByPartnerId(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, endCustomerLesByPartnerId,
                Status.SUCCESS);
    }

    /**
     * This Method Takes Partner le Id, product name, service and access type as input
     * and it provides the details of currency,sple and INR Information specific to partner
     *
     * @param product
     * @param partnerLeId
     * @param secsId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.Partner.GET_SUPPLIER_LE_BY_PARTNER_LE)
    @RequestMapping(value = "/le/{partnerLeId}/secs/{secsId}/product/{product}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLegalEntityDto.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    public ResponseResource<Set<CustomerLegalEntityProductResponseDto>> getSupplierLegalEntityDetailsByPartnerLegalIdForService(
            @PathVariable("partnerLeId") Integer partnerLegalEntityId, @PathVariable("secsId") Integer secsId,
            @PathVariable("product") String product,@RequestParam("services") List<String> services) throws TclCommonException {
        Set<CustomerLegalEntityProductResponseDto> customerLegalEntityProductResponseDtos = partnerCustomerService
                .getSupplierLegalEntityDetailsByPartnerLegalIdForService(partnerLegalEntityId, product, secsId,services);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                customerLegalEntityProductResponseDtos, Status.SUCCESS);
    }
}
