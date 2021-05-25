package com.tcl.dias.oms.crossconnect.controller.v1;

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
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.crossconnect.service.v1.CrossConnectPricingFeasibilityService;
import com.tcl.dias.oms.crossconnect.service.v1.CrossConnectQuoteService;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/crossconnect/quotes")
public class CrossConnectQuoteController {

    @Autowired
    CrossConnectQuoteService crossConnectQuoteService;

    @Autowired
    CrossConnectPricingFeasibilityService crossConnectPricingFeasibilityService;

    /**
     * updateSiteForCrossConnect  this is used to update the site information
     *
     * @param quoteId
     * @param request    QuoteDetail Request object
     * @param customerId
     * @return Returns the QuoteBean response
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_CROSS_CONNECT_SITE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{quoteId}/sites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteBean> updateSiteForCrossConnect (@PathVariable("quoteId") @BaseArgument Integer quoteId,
                                                                  @RequestBody QuoteDetail request, @RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {

        QuoteBean response = crossConnectQuoteService.updateCrossConnectSite(request, customerId, quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    /**
     * deactivate or deleting the cross connect site
     *
     * @param quoteId
     * @param siteId
     * @param action
     * @return Returns the QuoteBean response
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_OR_DISABLE_CROSS_CONNECT_SITE)
    @RequestMapping(value = "/{quoteId}/{quoteToLeId}/sites/{siteId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<QuoteDetail> deactivateCrossConnectSites(@PathVariable("quoteId") Integer quoteId,@PathVariable("quoteToLeId") Integer quoteToLeId,
                                                                     @PathVariable("siteId") Integer siteId, @RequestParam(required = true, value = "action") String action)
            throws TclCommonException {

        QuoteDetail response = crossConnectQuoteService.processDeactivateCrossConnectSites(siteId, quoteId, action);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }
    /**
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited editsites this method is used to
     *            edit the ill site info
     * @return ResponseResource
     * @throws TclCommonException
     */

    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.EDIT_CROSS_CONNECT_SITES)
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @Transactional
    public ResponseResource<QuoteDetail> editCorssConnectSite(@PathVariable("quoteId") Integer quoteId,
                                                              @PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,@RequestBody UpdateRequest request) throws TclCommonException {

        QuoteDetail response = crossConnectQuoteService.editCorssConnectSiteComponent(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
    @RequestMapping(value = "/trigger/price", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<String> triggeerPricingCrossconnect(@RequestBody FeasibilityBean request)
            throws TclCommonException {
        crossConnectPricingFeasibilityService.processCrossConnectPricngRequest(request.getLegalEntityId());
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);
    }

    /**
     * @author
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
        QuoteDetail response = crossConnectQuoteService.updateSiteProperties(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }

    /**
     * @author Biswajit Sahoo getSiteProperties get site properties
     * @return ResponseResource
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_PROPERTIES_ONLY)
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}/properties", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<List<QuoteProductComponentBean>> getSiteProperties(@PathVariable("quoteId") Integer quoteId,
                                                                               @PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId)
            throws TclCommonException {
        List<QuoteProductComponentBean> response = crossConnectQuoteService.getSiteProperties(siteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * Get Cross Connect Local IT Contact and Demarcation reference ID
     *
     * @return ResponseResource
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_PROPERTIES_ONLY)
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}/properties/localit/demarcation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<Integer> getLocalITContactAndDemarcationReferenceId(@PathVariable("quoteId") Integer quoteId,
                                                                               @PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId)
            throws TclCommonException {
        Integer response = crossConnectQuoteService.getLocalITContactAndDemarcationReferenceId(siteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }


    /**
     * updateSiteForCrossConnect  this is used to update the site information
     *
     * @param quoteId
     * @param request    QuoteDetail Request object
     * @param customerId
     * @return Returns the QuoteBean response
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_CROSS_CONNECT_SITE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/macd/{quoteId}/sites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteBean> updateSiteForMACDCrossConnect (@PathVariable("quoteId") @BaseArgument Integer quoteId,
                                                                  @RequestBody NplQuoteDetail request, @RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {

        QuoteBean response = crossConnectQuoteService.updateMACDCrossConnectSite(request, request.getCustomerId(), quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
}
