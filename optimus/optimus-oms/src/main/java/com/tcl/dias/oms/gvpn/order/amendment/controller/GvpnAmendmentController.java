package com.tcl.dias.oms.gvpn.order.amendment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.LastMileProviderDetails;
import com.tcl.dias.oms.beans.OrderAmendmentStatusBean;
import com.tcl.dias.oms.beans.QuoteBeanForOrderAmendment;
import com.tcl.dias.oms.beans.QuoteOrderAmendmentBean;
import com.tcl.dias.oms.beans.ServiceDetailBeanForAmendment;
import com.tcl.dias.oms.beans.SiteToServiceMapping;
import com.tcl.dias.oms.beans.SiteUpdateForAmendmentBean;
import com.tcl.dias.oms.gvpn.order.amendment.service.GvpnAmendmentService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/gvpn/order/amendment")
public class GvpnAmendmentController{

    @Autowired
    GvpnAmendmentService gvpnAmendmentService;

    /**
     * Compare Quotes controller method
     *
     * Author SuruchiA
     * @param orderCode
     * @return
     *
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.GET_SERVICE_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceDetailBeanForAmendment.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{orderCode}/servicedetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<ServiceDetailBeanForAmendment>> getAllServiceDetailsForOrder(@PathVariable("orderCode") String orderCode
            ,@RequestParam(required = false, value = "isO2c") boolean isO2c)
            throws TclCommonException {
        List<ServiceDetailBeanForAmendment> response = gvpnAmendmentService.getServiceDetailsForAmendment(orderCode,isO2c);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }


    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.GET_IF_QUOTE_EXISTS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceDetailBeanForAmendment.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{orderCode}/checkquote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteOrderAmendmentBean> getIfQuoteExists(@PathVariable("orderCode") String orderCode)
            throws TclCommonException {
        QuoteOrderAmendmentBean response = gvpnAmendmentService.getIsQuoteCreatedForParentOrderInOrderAmendment(orderCode);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.CREATE_QUOTE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value="/{parentAmendmentOrderCode}/createQuote",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteBeanForOrderAmendment> quoteCreate(@PathVariable("parentAmendmentOrderCode") String parentAmendmentOrderCode,
                                                                    @RequestBody QuoteBeanForOrderAmendment quoteBeanForOrderAmendment) throws TclCommonException {
        QuoteBeanForOrderAmendment response = gvpnAmendmentService.createQuoteForOrderAmendment(quoteBeanForOrderAmendment, parentAmendmentOrderCode);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.UPDATE_LOCATIONS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value="/updatelocations",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Integer> quoteCreate(@RequestBody SiteUpdateForAmendmentBean siteUpdateForAmendmentBean) throws TclCommonException {
        Integer response = gvpnAmendmentService.updateLocationsToCorrespondingSites(siteUpdateForAmendmentBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.GET_LM_PROVIDER_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value="/lastmile/providerlist",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<LastMileProviderDetails>> getLmDetails( @RequestBody List<Integer> sites) throws TclCommonException {
        List<LastMileProviderDetails> response = gvpnAmendmentService.getVendorDetailsForSite(sites);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.UPDATE_FEAS_SITE_AS_NON_FEASIBLE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/quoteToLe/{quoteToLe}/updatefeassite", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> processMFForFeasibleSites(@PathVariable("quoteToLe") Integer quoteToLe,
                                                              @RequestBody List<Integer> siteIds) throws TclCommonException {
        gvpnAmendmentService.updateFeasibleSitesAsMF(siteIds, quoteToLe);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.UPDATE_STAGES)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/quoteToLe/{quoteToLe}/updatestage", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> updateStageAsE(@PathVariable("quoteToLe") Integer quoteToLe,
                                                   @RequestBody String value) throws TclCommonException {
        gvpnAmendmentService.updateVariousStages(value,quoteToLe);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.GET_SITE_TO_SERVICE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value="/quoteToLe/{quoteToLe}/o2cserviceids",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<SiteToServiceMapping>> getServiceMappedToSite(@PathVariable("quoteToLe") Integer quoteToLe,
                                                                               @RequestBody List<Integer> siteIds)  throws TclCommonException {
        List<SiteToServiceMapping> response = gvpnAmendmentService.getO2cServiceIdsForSites(siteIds,quoteToLe);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.GET_SERVICE_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceDetailBeanForAmendment.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{quoteId}/quoteId/{quoteToLe}/quoteToLe/amendmentstatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource saveAmendmentDetails(@PathVariable("quoteId") Integer quoteId
            , @PathVariable("quoteToLe") Integer quoteToLe ,@RequestBody OrderAmendmentStatusBean orderAmendmentStatusBean)
            throws TclCommonException {
        gvpnAmendmentService.saveAmendmentInfo(quoteId, quoteToLe ,orderAmendmentStatusBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.UPDATE_IF_SHIFT_SITE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value="/quoteToLe/{quoteToLe}/isshiftsite",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseResource<String> updateQuoteCategoryIfSiteIsShifted(@PathVariable("quoteToLe") Integer quoteToLe)  throws TclCommonException {
        String response = gvpnAmendmentService.updateCategoryIfSiteShifted(quoteToLe);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderAmendment.UPDATE_LM_PROVIDER_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value="/lastmile/updateprovider",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> updateIsSelectedVendor( @RequestBody LastMileProviderDetails lastMileProviderDetails) throws TclCommonException {
        String response = gvpnAmendmentService.updateLastMileSelectedVendor(lastMileProviderDetails);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

}

