package com.tcl.dias.preparefulfillment.dashboard.controller;

import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.common.servicefulfillment.beans.ScContractInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.servicefulfillmentutils.beans.AttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScChargeLineItemBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScOrderAttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceAttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.ComponentAttributes;
import com.tcl.dias.servicefulfillmentutils.beans.TaskAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.beans.OrderDashboardResponse;
import com.tcl.dias.preparefulfillment.beans.ServiceDashBoardBean;
import com.tcl.dias.preparefulfillment.service.ServiceDashboardService;
import com.tcl.dias.preparefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author MRajakum
 *
 */
@RestController
@RequestMapping("/dashboard/o2c/")
public class O2CDashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(O2CDashboardController.class);

	@Autowired
	ServiceDashboardService serviceDashboardService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_SERVICE_INFO)
	@RequestMapping(value = "service", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderDashboardResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderDashboardResponse> getServiceDetailsByScOrderId(@RequestParam("ikey") String hashKey)
			throws TclCommonException {
		try {
			LOGGER.info("ikey is received {}", hashKey);
			String hashKeyDecrypt = EncryptionUtil.decrypt(hashKey);
			LOGGER.info("ikey is decrypted {}", hashKeyDecrypt);
			if (hashKeyDecrypt.contains("---")) {
				String[] splitter = hashKeyDecrypt.split("---");
				String orderId = splitter[0];
				String serviceId = splitter[1];
				LOGGER.info("Order Id is {} and ServiceId {}", orderId, serviceId);
				OrderDashboardResponse response = serviceDashboardService.getOrderDashboardDetails(orderId);
				List<ServiceDashBoardBean> serviceDashBoards = new ArrayList<>();
				for (ServiceDashBoardBean serviceDashboard : response.getServiceDetails()) {
					if (serviceDashboard.getServiceCode().equals(serviceId)) {
						serviceDashBoards.add(serviceDashboard);
					}
				}
				response.setServiceDetails(serviceDashBoards);
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
						Status.SUCCESS);
			} else {
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						new OrderDashboardResponse(), Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting serviceDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.DASHBOARD.GET_SERVICE_INFO)
	@RequestMapping(value = "service/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderDashboardResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceDashBoardBean> getServiceDetailsByServiceId(@RequestParam("ikey") String hashKey)
			throws TclCommonException {
		try {
			ServiceDashBoardBean response = null;
			LOGGER.info("ikey is received {}", hashKey);
			String hashKeyDecrypt = EncryptionUtil.decrypt(hashKey);
			LOGGER.info("ikey is decrypted {}", hashKeyDecrypt);
			if (hashKeyDecrypt.contains("---")) {
				String[] splitter = hashKeyDecrypt.split("---");
				String orderId = splitter[0];
				String serviceId = splitter[1];
				LOGGER.info("Order Id is {} and ServiceId {}", orderId, serviceId);
				ScServiceDetail scServiceDetails = scServiceDetailRepository.findByUuidAndScOrderUuid(serviceId,
						orderId);
				if (scServiceDetails != null) {
					response = serviceDashboardService.getServiceDashboardDetails(scServiceDetails.getId(), orderId);

					return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
							Status.SUCCESS);
				} else {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}

			} else {
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						new ServiceDashBoardBean(), Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting serviceDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	@RequestMapping(value = "attributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttributesBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<AttributesBean> getComponentAndServiceAttributes(@RequestParam(value = "orderCode", required = false) String orderCode,
																			 @RequestParam(value = "serviceId", required = false) String serviceId,@RequestParam(value = "serviceDetailId", required = false) Integer serviceDetailId, @RequestParam(value = "taskId", required = false) Integer taskId) throws TclCommonException {
		AttributesBean response = serviceDashboardService.getComponentAndServiceAttributes(orderCode, serviceId,serviceDetailId,taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	@PostMapping("component/attributes/save")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public void saveComponentAttributes(@RequestBody ComponentAttributes componentAttributes){
		serviceDashboardService.saveComponentAttributes(componentAttributes);
	}

	@PostMapping("service/attributes/save")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public void saveServiceAttributes(@RequestBody List<ServiceAttributesBean> serviceAttributesBeans){
		serviceDashboardService.saveServiceAttributes(serviceAttributesBeans);
	}

	@PostMapping("servicedetails/save")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public void saveComponentAttributes(@RequestBody ScServiceDetailBean scServiceDetailBean){
		serviceDashboardService.saveScServiceDetails(scServiceDetailBean);
	}

	@PostMapping("service/orders/save")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public void saveServiceOrders(@RequestBody ScOrderBean scOrderBean){
		serviceDashboardService.saveScOrders(scOrderBean);
	}

	@PostMapping("service/contract/save")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public void saveServiceContract(@RequestBody ScContractInfoBean scContractInfoBean){
		serviceDashboardService.saveScContract(scContractInfoBean);
	}


	@PostMapping("order/attributes/save")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public void saveServiceOrderAttributes(@RequestBody List<ScOrderAttributesBean> scOrderAttributesBeans){
		serviceDashboardService.saveServiceOrderAttributes(scOrderAttributesBeans);
	}

	@PostMapping("task/save")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public void saveTask(@RequestBody TaskAdminBean taskAdminBean){
		serviceDashboardService.saveTaskData(taskAdminBean);
	}

	@PostMapping("service/chargelineitem/save")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public void saveServiceChargeLineItem(@RequestBody List<ScChargeLineItemBean> scChargeLineItemBeans){
		serviceDashboardService.saveScChargeLineItems(scChargeLineItemBeans);
	}
}
