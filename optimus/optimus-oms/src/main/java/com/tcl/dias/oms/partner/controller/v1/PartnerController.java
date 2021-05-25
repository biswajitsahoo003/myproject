package com.tcl.dias.oms.partner.controller.v1;

import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.common.beans.PartnerTempCustomerDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.sfdc.response.bean.SfdcActiveCampaignResponseBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.PartnerEntityRequest;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.UserDetails;
import com.tcl.dias.oms.entity.entities.MstOrderNaLiteProductFamily;
import com.tcl.dias.oms.partner.beans.ParnterPsamDetail;
import com.tcl.dias.oms.partner.beans.PartnerOpportunityBean;
import com.tcl.dias.oms.partner.beans.PublicDatabaseCustomerBean;
import com.tcl.dias.oms.partner.beans.SfdcCampaignResponseBean;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Controller related to Partner Opportunity API's
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/partners", produces = MediaType.APPLICATION_JSON_VALUE)
public class PartnerController {

    @Autowired
    PartnerService partnerService;

    /**
     * API to create Partner Opportunity
     *
     * @param partnerOpportunityBean
     * @return {@link PartnerOpportunityBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.CREATE_PARTNER_OPPORTUNITY)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerOpportunityBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/opportunities")
    public ResponseResource<PartnerOpportunityBean> createPartnerOpportunity(@RequestBody PartnerOpportunityBean partnerOpportunityBean) {
        PartnerOpportunityBean response = partnerService.createPartnerOpportunity(partnerOpportunityBean,false);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get Partner Opportunity
     *
     * @param opportunityId
     * @return {@link PartnerOpportunityBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_PARTNER_OPPORTUNITY_DETAILS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerOpportunityBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/opportunities/{opportunityId}")
    public ResponseResource<PartnerOpportunityBean> getPartnerOpportunity(@PathVariable("opportunityId") Integer opportunityId) {
        PartnerOpportunityBean response = partnerService.getPartnerOpportunity(opportunityId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get Campaign Names
     *
     * @return {@link List}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_CAMPAIGN_NAMES)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/campaigns")
    public ResponseResource<Map> getCampaignNames() {
        Map<String, List<String>> response = partnerService.getCampaignNames();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get Partner's customer from DNB / Public database
     *
     * @param partnerId
     * @return {@link PublicDatabaseCustomerBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_PARTNER_OPPORTUNITY_DETAILS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = PublicDatabaseCustomerBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/customers")
    public ResponseEntity getCustomersFromPublicDatabase(@PathVariable("partnerId") Integer partnerId) {
        return null;
    }

    /**
     * Upload Opportunity Files
     *
     * @param file
     * @return {@link ServiceResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.UPLOAD_FILE_DOC)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerDocumentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/upload/opportunities/files")
    public ResponseResource<PartnerDocumentBean> uploadOpportunityFileStorage(@RequestParam("file") MultipartFile file) throws TclCommonException {
        PartnerDocumentBean response = partnerService.uploadOpportunityFileStorage(file);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * Upload Opportunity Files via object Storage
     *
     * @param file
     * @return {@link ServiceResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.UPLOAD_FILE_DOC)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerDocumentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/object/upload/opportunities/files")
    public ResponseResource<PartnerDocumentBean> uploadOpportunityObjectStorage(@RequestBody MultipartFile file) throws TclCommonException {
        PartnerDocumentBean response = partnerService.uploadOpportunityObjectStorage(file);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to update the File details uploaded via object storage
     *
     * @param requestId
     * @param path
     * @return {@link ServiceResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.UPLOAD_FILE_DOC)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerDocumentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/update/object/upload/opportunities/files")
    public ResponseResource<PartnerDocumentBean> updateUploadOpportunityObjectStorage(@RequestParam("requestId") String requestId, @RequestParam("url") String path) throws TclCommonException {
        PartnerDocumentBean response = partnerService.updateUploadObjectConfigurationDocument(requestId, path);
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
    @GetMapping(value = "/download/opportunities/files/{attachmentId}")
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
    @GetMapping(value = "/object/download/opportunities/files/{attachmentId}")
    public ResponseResource<String> downloadOpportunityObjectStorage(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
        String response = partnerService.downloadOpportunityObjectStorage(attachmentId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to create Partner Entity in SFDC
     *
     * @param partnerEntityRequest
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.CREATE_PARTNER_ENTITY)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/create/entity")
    public ResponseResource<String> saveCustomerEntityTemp(@RequestBody PartnerEntityRequest partnerEntityRequest) {
        String response = partnerService.createCustomerEntityTemp(partnerEntityRequest);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get Industry and subIndustry Details
     *
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_INDUSTRY_INFO)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/industries")
    public ResponseResource<Map<String, List<Map<String,Object>>>> getIndustryDetails() {
        Map<String, List<Map<String,Object>>> response = partnerService.getIndustryDetails();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * API to get opportunity and customer details by Quote ID
     *
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_CUSTOMER_INFO)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Map.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/quotes/{quoteId}")
    public ResponseResource<PartnerOpportunityBean> getOpportunityByQuoteId(@PathVariable Integer quoteId) {
        PartnerOpportunityBean response = partnerService.getOpportunityByQuoteId(quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * API to confirm general terms of partner user in first time login and update it in audit
     *
     * @param userName
     * @param publicIp
     * @return {@link UserDetails}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.CONFIRM_PARTNER_USER_TERMS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserDetails.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/user/general/terms/confirmation")
    public ResponseResource<UserDetails> confirmGeneralTermsOfPartnerUser(@RequestParam("userName") String userName,
                                                                          @RequestParam("publicIp") String publicIp,
                                                                          HttpServletRequest httpServletRequest) {
        UserDetails response = partnerService.confirmGeneralTermsOfPartnerUser(userName, publicIp, httpServletRequest);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * Update partner location based on customer le id
     *
     * @param quoteLeId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.UPADTE_PARTNER_ENTITY_LOCATION)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/update/{quoteLeId}/le/location/{customerLeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> updatePartnerLeLocation(@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("customerLeId") Integer customerLeId)
            throws TclCommonException {
        Boolean response = partnerService.updatePartnerLeLocation(quoteLeId, customerLeId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }


    /**
     * Update partner location based on customer le id
     *
     * @param quoteLeId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.UPADTE_PARTNER_ENTITY_LOCATION)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/update/{quoteLeId}/le/location", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> updatePartnerLeLocationbyQuoteId(@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam(value="customerLeId", required=true) Integer customerLeId)
            throws TclCommonException {
        Boolean response = partnerService.updatePartnerLeLocation(quoteLeId, customerLeId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * Fetch PSAM email for Partner_entity
     *
     * @param partnerLeId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_PSAM_PARTNER_LE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/psam/{partnerId}/email")
    public ResponseResource<List<ParnterPsamDetail>> getPartnerLePsam(@PathVariable("partnerId") Integer partnerId)
            throws TclCommonException {
        List<ParnterPsamDetail> response = partnerService.getPsamEmailForAllPartnerLe(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_SALES_FUNNEL_DETAILS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = SfdcActiveCampaignResponseBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/campaign/report")
    public ResponseResource<List<SfdcCampaignResponseBean>> getCampaigndetais() {
        List<SfdcCampaignResponseBean> response = partnerService.getCampaigndetais();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }


    /**
     * API to create Partner Order NaLIte
     *
     * @param partnerOpportunityBean
     * @return {@link PartnerOpportunityBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_ORDER_NA_LITE_PRODUCT)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = MstOrderNaLiteProductFamily.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/ordernalite/product")
    public ResponseResource<List<MstOrderNaLiteProductFamily>> getOrderNaLiteProduct() {
        List<MstOrderNaLiteProductFamily> response = partnerService.getOrderNaLiteProduct();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to create Partner Order NaLIte
     *
     * @param partnerOpportunityBean
     * @return {@link PartnerOpportunityBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.CREATE_PARTNER_ORDER_NA_LITE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerOpportunityBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/ordernalite/opportunities")
    public ResponseResource<PartnerOpportunityBean> createPartnerOrderNaLite(@RequestBody PartnerOpportunityBean partnerOpportunityBean) {
        PartnerOpportunityBean response = partnerService.createPartnerOrderNaLite(partnerOpportunityBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }


    /**
     * API to get Partner's customer from DNB / Public database
     *
     * @param partnerId
     * @return {@link CustomerLeBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_PARTNER_OPPORTUNITY_DETAILS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerLeBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{customerId}/customerle")
    public ResponseResource<List<CustomerLeBean>> getCustomerleOmscustomer(@PathVariable("customerId") Integer customerId) {
        List<CustomerLeBean> response=partnerService.getcustomerLeDetails(customerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }


    /**
     * API to get Partner Opportunity
     *
     * @param opportunityId
     * @return {@link PartnerOpportunityBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_PARTNER_OPPORTUNITY_DETAILS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/opportunities/dealstatus")
    public ResponseResource<String> getDealtStatusForOpportunity(@RequestParam(value="quotecode", required=true) String quotecode) {
        String response = partnerService.checkDealRegistrationStatusbyQuoteCode(quotecode);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.CREATE_ENTITY_BY_PARTNER)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Integer.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/{partnerId}/le/{partnerLeId}/temp/customer/entity/creation")
    public ResponseResource<Integer> createOrUpdateEntityByPartner(@PathVariable("partnerId") Integer partnerId,
                                                                   @PathVariable("partnerLeId") Integer partnerLeId,
                                                                   @RequestBody PartnerTempCustomerDetailsBean partnerTempCustomerDetailsBean) {
        Integer response = partnerService.createTempCustomerEntityByPartner(partnerId, partnerLeId, partnerTempCustomerDetailsBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.CREATE_ENTITY_BY_PARTNER)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerTempCustomerDetailsBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/temp/customer/entity/{tempCustomerEntityId}")
    public ResponseResource<PartnerTempCustomerDetailsBean> getPartnerTempCustomerEntity(
            @PathVariable("tempCustomerEntityId") Integer tempCustomerEntityId) {
        PartnerTempCustomerDetailsBean response = partnerService.getCustomerEntity(tempCustomerEntityId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }


    /**
     * Fetch User for Email
     *
     * @RequestParam emailId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_USER_BY_EMAIL)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/email/userdetail")
    public ResponseResource<UserDetails> getUserDetailsByEmail(@RequestParam(value="emailId", required=true) String emailId)
            throws TclCommonException {
        UserDetails response = partnerService.getUserDetailsByEmail(emailId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * API to create Partner Opportunity
     *
     * @param partnerOpportunityBean
     * @return {@link PartnerOpportunityBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.UNARCHIVE_ACCOUNT)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/unarchive/account")
    public ResponseResource<String> unArchiveCustomerAccount(@RequestBody PartnerOpportunityBean partnerOpportunityBean) throws TclCommonException  {
        String response = partnerService.unArchiveCustomerAccount(partnerOpportunityBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

}

