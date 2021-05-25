package com.tcl.dias.servicefulfillment.controller.v1;

import com.tcl.dias.servicefulfillment.beans.DispatchCPEBean;
import com.tcl.dias.servicefulfillment.beans.InstallCpeSdwanBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeInstallationBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.ConfigureCpeBean;
import com.tcl.dias.servicefulfillment.beans.PoReleaseCpeOrderBean;
import com.tcl.dias.servicefulfillment.beans.PrCpeOrderBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoForCPEOrderBean;
import com.tcl.dias.servicefulfillment.beans.cpe.TrackCpeDeliveryBean;
import com.tcl.dias.servicefulfillment.service.CPEImplementationService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMaterialAvailabilityBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This Controller is used to perform various operations related to Customer
 * premises equipment task for SDWAN.
 * 
 * @author Dimple S
 */
@RestController
@RequestMapping("v1/cpe-implementation/task/sdwan")
public class SdwanCPEImplementationController {

	@Autowired
	CPEImplementationService cpeImplementationService;

	
	/**
	 *This method is used to provide PR details for CPE Order.
	 *
	 * @param taskId
	 * @param prCpeOrderBean
	 * @return
	 * @throws TclCommonException
	 * @author Dimple S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/pr-cpe-order")
	public ResponseResource<PrCpeOrderBean> pRCpeOrder(@RequestBody PrCpeOrderBean prCpeOrderBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.manualPrCpeOrder(prCpeOrderBean), Status.SUCCESS);
	}
	
	/**
	 * This method is used to provide PO details for CPE Order.
	 *
	 * @param poForCPEOrder
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author Dimple S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.PROVIDE_PO_FOR_CPE_ORDER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoForCPEOrderBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/po-cpe-order", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoForCPEOrderBean> providePoForCPEOrder(
			@RequestBody ProvidePoForCPEOrderBean poForCPEOrder) throws TclCommonException {
		return new ResponseResource<ProvidePoForCPEOrderBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.manualPoForCPEOrder(poForCPEOrder), Status.SUCCESS);
	}
	
	/**
	 * This method is used to release PO details for CPE Order.
	 * 
	 * @param taskId
	 * @param poReleaseCpeOrderBean
	 * @return
	 * @throws TclCommonException
	 * @author Dimple S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/po-release-for-cpe-order")
	public ResponseResource<PrCpeOrderBean> poReleaseForCpeOrder(
											 @RequestBody PoReleaseCpeOrderBean poReleaseCpeOrderBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.manualPoReleaseForCpeOrder(poReleaseCpeOrderBean), Status.SUCCESS);
	}
	
	/**
	 * This method is used to map named customer.
	 *
	 * @param taskId
	 * @param confirmMaterialAvailabilityBean
	 * @return
	 * @throws TclCommonException
	 * @author Dimple S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/confirm-material-availability")
	public ResponseResource<ConfirmMaterialAvailabilityBean> confirmMaterialAvailability(
						 @RequestBody ConfirmMaterialAvailabilityBean confirmMaterialAvailabilityBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.manualConfirmMaterialAvailability(confirmMaterialAvailabilityBean), Status.SUCCESS);
	}

	/**
	 * This method is used to map named customer.
	 *
	 * @param taskId
	 * @param cpeInstallationBean
	 * @return
	 * @throws TclCommonException
	 * @author Dimple S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.INSTALL_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DispatchCPEBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/install-cpe")
	public ResponseResource<InstallCpeSdwanBean> cpeInstallationBean(
			@RequestBody InstallCpeSdwanBean cpeInstallationBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.installCpeSdwan(cpeInstallationBean), Status.SUCCESS);
	}
	
	/**
	 * Track SDWAN CPE delivery
	 *
	 * @param taskId
	 * @param trackCpeDeliveryBean
	 * @return
	 * @throws TclCommonException
	 * @author Dimple S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/track-cpe-delivery")
	public ResponseResource<TrackCpeDeliveryBean> trackCpeDelivery(
														@RequestBody TrackCpeDeliveryBean trackCpeDeliveryBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.sdwanTrackCpeDelivery(trackCpeDeliveryBean), Status.SUCCESS);
	}
	
	/**
	 * This method is used to International Dispatch CPE 
	 *
	 * @param taskId
	 * @param dispatchCPEBean
	 * @return
	 * @throws TclCommonException
	 * @author Dimple S dispatchCpe
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.DISPATCH_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DispatchCPEBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/dispatch-cpe-international")
	public ResponseResource<DispatchCPEBean> dispatchCpe(
			@RequestBody DispatchCPEBean dispatchCPEBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.sdwanDispatchCpeInternational(dispatchCPEBean), Status.SUCCESS);
	}
	
}
