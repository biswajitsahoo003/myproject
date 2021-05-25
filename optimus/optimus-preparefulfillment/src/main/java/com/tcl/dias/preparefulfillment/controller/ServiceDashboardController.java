package com.tcl.dias.preparefulfillment.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.beans.OrderDashboardResponse;
import com.tcl.dias.preparefulfillment.beans.PagedResult;
import com.tcl.dias.preparefulfillment.beans.ServiceDashBoardBean;
import com.tcl.dias.preparefulfillment.beans.ServiceRequest;
import com.tcl.dias.preparefulfillment.beans.SolutionViewDetailsBean;
import com.tcl.dias.preparefulfillment.beans.StagePlanBean;
import com.tcl.dias.preparefulfillment.service.ServiceDashboardService;
import com.tcl.dias.preparefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.DashBoardCount;
import com.tcl.dias.servicefulfillmentutils.beans.DashboardAttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.ProcessTaskLogBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.StageCountBean;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * This file contains the LocationController.java class. This class contains API
 * informations related to location which will be consumed by location
 * configuration UI.
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/scorders")
public class ServiceDashboardController {

	@Autowired
	private ServiceDashboardService serviceDashboardService;

	/**
	 * 
	 * getServiceDetailsByScOrderId - Get ServiceDetails By OrderId
	 * 
	 * @param scOrderId
	 * @param country
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_SERVICE_INFO)
	@RequestMapping(value = "/{scOrderId}/service", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderDashboardResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderDashboardResponse> getServiceDetailsByScOrderId(
			@PathVariable("scOrderId") String scOrderId) {
		OrderDashboardResponse response = serviceDashboardService.getOrderDashboardDetails(scOrderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_SERVICE_INFO)
	@RequestMapping(value = "/{scOrderId}/ipc/service", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderDashboardResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderDashboardResponse> getServiceDetailsByScOrderIdIpc(
			@PathVariable("scOrderId") String scOrderId) {
		OrderDashboardResponse response = serviceDashboardService.getOrderDashboardDetailsIpc(scOrderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * getServiceDetailsByServiceId
	 * 
	 * @param scOrderId
	 * @param serviceId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_SERVICEDETAIL_INFO)
	@RequestMapping(value = "/{scOrderCode}/service/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderDashboardResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceDashBoardBean> getServiceDetailsByServiceId(
			@PathVariable("scOrderCode") String scOrderCode, @PathVariable("serviceId") Integer serviceId) {
		ServiceDashBoardBean response = serviceDashboardService.getServiceDashboardDetails(serviceId, scOrderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * getServiceDetailsByServiceId
	 * 
	 * @param scOrderId
	 * @param serviceId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_SERVICEDETAIL_INFO)
	@RequestMapping(value = "/{scOrderCode}/ipc/service/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderDashboardResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceDashBoardBean> getServiceDetailsByServiceIdIpc(
			@PathVariable("scOrderCode") String scOrderCode, @PathVariable("serviceId") Integer serviceId) {
		ServiceDashBoardBean response = serviceDashboardService.getServiceDashboardDetailsIpc(serviceId, scOrderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * getStageCountByScOrderId
	 * 
	 * @param scOrderId
	 * @param country
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_ORDER_DASHBOARD_COUNT)
	@RequestMapping(value = "/{scOrderCode}/stage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<Map<String, String>>> getStageCountByScOrderId(
			@PathVariable("scOrderCode") String scOrderCode) {
		List<Map<String, String>> response = serviceDashboardService.getStageCount(scOrderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * getStageCountByScOrderId
	 * @param scOrderId
	 * @param country
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_ORDER_DASHBOARD_COUNT)
	@RequestMapping(value = "/stage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource <StageCountBean> getInprogressStageCount() {
		return new ResponseResource<StageCountBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				serviceDashboardService.getInprogressStageCount(),
				Status.SUCCESS);
	}

	/**
	 *
	 * getActiveServiceRecordsByStageDefKey
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_SERVICE_RECORDS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/group/stagekey")
	public ResponseResource <List<Map<String, String>>> getServiceRecordsByStageDefKey(
			@RequestParam("stagekey") String stageKey) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceDashboardService.getServiceRecordsByProcessKey(stageKey),
				Status.SUCCESS);
	}


	/**
	 * 
	 * getStageCountByScOrderId
	 * @param scOrderId
	 * @param country
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_ORDER_DASHBOARD_COUNT)
	@RequestMapping(value = "/group/product", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<Map<String, String>>> getServiceIdGroupByProduct() {
		List<Map<String, String>> response = serviceDashboardService.getServiceIdGroupByProduct();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * GetCountBasedOnOrderTypeAndProductName
	 * @param scOrderId
	 * @param country
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET__SERVICE_RECORDS_BY_ORDER_TYPE)
	@RequestMapping(value = "/group/ordertype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<Map<String, String>>> getServiceIdGroupByOrderType(
			@RequestParam("productName") String productName, @RequestParam("orderType") String orderType)
	 {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceDashboardService.getServiceIdGroupByOrderType(productName,orderType),
				Status.SUCCESS);
	}

	/**
	 * 
	 * getProcessTaskLogsByScOrderId
	 * 
	 * @param scOrderId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_ORDER_DASHBOARD_COUNT)
	@RequestMapping(value = "order/{orderCode}/id/{scOrderId}/tasklogs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<ProcessTaskLogBean>> getProcessTaskLogsByScOrderId(
			@PathVariable("scOrderId") Integer scOrderId,@PathVariable("orderCode") String orderCode) {
		List<ProcessTaskLogBean> response = serviceDashboardService.getTaskLogsByScorderId(scOrderId,orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@GetMapping("/customer-search")
	public ResponseResource<List<OrderDashboardResponse>> customerSearch(
			@RequestParam("type")String type,
			@RequestParam("input")String input) {
		List<OrderDashboardResponse> response = null;
		if("SERVICE_ID".equalsIgnoreCase(type) || "INTERNATIONAL_SERVICE_ID".equalsIgnoreCase(type) || "PARENT_SERVICE_ID".equalsIgnoreCase(type)) {
			response = serviceDashboardService.customerSearchByServiceId(type,input);
		}else {
			response = serviceDashboardService.customerSearch(type,input);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@GetMapping("/active-orders")
	public ResponseResource<PagedResult<OrderDashboardResponse>> getActiveOrders(@RequestParam("page")Integer page,
			@RequestParam("size")Integer size,@RequestParam(required=false,name="type")String type,@RequestParam(required=false,name="value")String value) {
		PagedResult<OrderDashboardResponse> response = serviceDashboardService.getActiveOrders(page,size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@PostMapping("/active-orders/filter")
	public ResponseResource<PagedResult<OrderDashboardResponse>> getActiveOrdersBasedOnFilter(@RequestBody ServiceRequest request) {
		PagedResult<OrderDashboardResponse> response = serviceDashboardService.getActiveOrdersBasedOnFilter(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

    /**
     * Returns list of customer le name matching the input.
     *
     * @param customerName
     * @return List<String></>
     */
    @GetMapping("/customer")
    public ResponseResource<List<String>> getCustomerList(@RequestParam("name") String customerName) {
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                serviceDashboardService.getCustomerList(customerName),
                Status.SUCCESS);
    }

	/**
	 * Gets order Code for given service Code.
	 *
	 * @param serviceCode
	 * @return
	 */
	@GetMapping("/service/{serviceCode}")
	public ResponseResource getOrderCode(@PathVariable("serviceCode") String serviceCode){
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				serviceDashboardService.getOrderCode(serviceCode),
				Status.SUCCESS);
	}

	@GetMapping("/details/order/{orderCode}/service/{serviceCode}/id/{serviceId}")
	public ResponseResource getServiceDetails(@PathVariable("serviceId") Integer serviceId,@PathVariable("serviceCode") String serviceCode,@PathVariable("orderCode") String orderCode){
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				serviceDashboardService.getCompleteServiceDetails(serviceId,orderCode,serviceCode),
				Status.SUCCESS);
	}
	
	@GetMapping("/details/order/izosdwan/{orderCode}/service/{serviceCode}/id/{serviceId}")
	public ResponseResource getServiceDetailsIzosdwan(@PathVariable("serviceId") Integer serviceId,@PathVariable("serviceCode") String serviceCode,@PathVariable("orderCode") String orderCode){
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				serviceDashboardService.getCompleteServiceDetailsForIzosdwan(serviceId,orderCode,serviceCode),
				Status.SUCCESS);
	}
	

	/**
	 *
	 * GetCountBasedOnOrderTypeAndProductName
	 * @param scOrderId
	 * @param country
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET__SERVICE_RECORDS_BY_ORDER_TYPE)
	@RequestMapping(value = "/stage/active/service/{serviceCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<StagePlanBean> getActiveStageDetails(@PathVariable("serviceCode") String serviceCode)
	 {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceDashboardService.getActiveStageDetails(serviceCode),
				Status.SUCCESS);
	}

	/**
	 *
	 * getServiceDetailsByOrderCode
	 *
	 * @param orderCode
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_ORDER_DASHBOARD_COUNT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/service/details/order/{orderCode}")
	public ResponseResource <List<ScServiceDetailBean>> getInprogressServiceDetails(@PathVariable("orderCode") String orderCode){
		String[] serviceDetailAvailable = new String[1];
		List<ScServiceDetailBean> response= serviceDashboardService.getInprogressServiceDetails(orderCode, serviceDetailAvailable);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response,
				Status.SUCCESS);
	}

    /**
     *
     * getCountofLMScenarioTypes
     *
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_ORDER_LM_SCENARIO_COUNT)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ArrayList.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping("/orderby/lmscenario")
    public ResponseResource <List<Map<String, String>>> groupbytLMScenarioTypeCount(){
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,serviceDashboardService.getLMScenarioTypeCount(),
                Status.SUCCESS);
    }
    
    
	/**
	 * 
	 * getStageCountByScOrderId
	 * @param scOrderId
	 * @param country
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_ORDER_DASHBOARD_COUNT)
	@RequestMapping(value = "/dashboard/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardCount.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<DashBoardCount> getAllCounts() {
		DashBoardCount response = serviceDashboardService.getAllCounts();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	@RequestMapping(value = "/dashboard/attributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashboardAttributesBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<DashboardAttributesBean> getDashoardAttributes(){
		DashboardAttributesBean response = serviceDashboardService.getDashboardAttributes();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/solution/details/{solutionCode}")
	public ResponseResource<List<SolutionViewDetailsBean>> getSolutionDetailsServices(@PathVariable("solutionCode") String solutionCode) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceDashboardService.getSolutionDetailsServices(solutionCode), Status.SUCCESS);
	}


}
