package com.tcl.dias.servicefulfillment.controller.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.ConfigureCpeBean;
import com.tcl.dias.servicefulfillment.beans.CustomerCPEConfigConfirmationBean;
import com.tcl.dias.servicefulfillment.beans.CustomerCPEInstallationConfirmationBean;
import com.tcl.dias.servicefulfillment.beans.DispatchCPEBean;
import com.tcl.dias.servicefulfillment.beans.GenerateCPEInvoiceBean;
import com.tcl.dias.servicefulfillment.beans.InstallCPEBean;
import com.tcl.dias.servicefulfillment.beans.PoReleaseCpeInstallationBean;
import com.tcl.dias.servicefulfillment.beans.PrCpeInstallationBean;
import com.tcl.dias.servicefulfillment.beans.PrCpeOrderBean;
import com.tcl.dias.servicefulfillment.beans.PoReleaseCpeOrderBean;
import com.tcl.dias.servicefulfillment.beans.ProvideMinBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoForCPEInstallationBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoForCPEOrderBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoForCPESupportBean;
import com.tcl.dias.servicefulfillment.beans.WbsTransferJeopardy;
import com.tcl.dias.servicefulfillment.beans.cpe.MapNamedCustomerBean;
import com.tcl.dias.servicefulfillment.beans.cpe.ProvideWbsglccDetailBean;
import com.tcl.dias.servicefulfillment.beans.cpe.TrackCpeDeliveryBean;
import com.tcl.dias.servicefulfillment.service.CPEImplementationService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMaterialAvailabilityBean;
import com.tcl.dias.servicefulfillmentutils.beans.GenerateMrnforCPETransferBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This Controller is used to perform various operations related to Customer
 * premises equipment task.
 * 
 * @author arjayapa
 */
@RestController
@RequestMapping("v1/cpe-implementation/task")
public class CPEImplementationController {

	@Autowired
	CPEImplementationService cpeImplementationService;

	/**
	 * This method is used to provide PO details for CPE Order.
	 *
	 * @param poForCPEOrder
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author arjayapa
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
				cpeImplementationService.providePoForCPEOrder(poForCPEOrder), Status.SUCCESS);
	}

	/**
	 * This method is used to provide PO for CPE installation.
	 *
	 * @param cpeInstallationBean
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author arjayapa
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.PROVIDE_PO_FOR_CPE_INSTALLATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoForCPEInstallationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/po-cpe-installation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoForCPEInstallationBean> providePoForCPEInstallation(@RequestBody ProvidePoForCPEInstallationBean cpeInstallationBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.providePoForCPEInstallation(cpeInstallationBean), Status.SUCCESS);
	}

	/**
	 * This method is used to prepare PO for CPE support.
	 *
	 * @param cpeSupportBean
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author arjayapa
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.PROVIDE_PO_FOR_CPE_SUPPORT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoForCPESupportBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/po-cpe-support", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoForCPESupportBean> preparePoForCPESupport(@RequestBody ProvidePoForCPESupportBean cpeSupportBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.providePoForCPESupport(cpeSupportBean), Status.SUCCESS);
	}

	/**
	 * This method is used to Generate CPE Invoice.
	 *
	 * @param cpeInvoiceGenerationBean
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author arjayapa
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.GENERATE_CPE_INVOICE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GenerateCPEInvoiceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/generate-cpe-invoice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GenerateCPEInvoiceBean> generateCPEInvoice(@RequestBody GenerateCPEInvoiceBean cpeInvoiceGenerationBean) 
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.generateCPEInvoice(cpeInvoiceGenerationBean), Status.SUCCESS);
	}

	/**
	 * This method is used to Generate MRN for CPE Transfer
	 *
	 * @param taskId
	 * @param generateMrnforCPETransferBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh generateMrnforCPETransfer
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.GENERATE_MRN_FOR_CPE_TRANSFER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GenerateMrnforCPETransferBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/generate-mrn-cpr-transfer")
	public ResponseResource<GenerateMrnforCPETransferBean> generateMrnforCPETransfer(
			@RequestBody GenerateMrnforCPETransferBean generateMrnforCPETransferBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.generateMrnforCPETransfer(generateMrnforCPETransferBean),
				Status.SUCCESS);
	}

	/**
	 * This method is used to Dispatch CPE
	 *
	 * @param taskId
	 * @param dispatchCPEBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh dispatchCpe
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.DISPATCH_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DispatchCPEBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/dispatch-cpe")
	public ResponseResource<DispatchCPEBean> dispatchCpe(
			@RequestBody DispatchCPEBean dispatchCPEBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.dispatchCpe(dispatchCPEBean), Status.SUCCESS);
	}

	/**
	 * This method is used to provideMin
	 *
	 * @param taskId
	 * @param provideMinBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh dispatchCpe
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.PROVIDE_MIN)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DispatchCPEBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/provide-min")
	public ResponseResource<ProvideMinBean> provideMin(
										@RequestBody ProvideMinBean provideMinBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.provideMin(provideMinBean), Status.SUCCESS);
	}

	/**
	 * This method is used to Install CPE
	 *
	 * @param taskId
	 * @param installCPEBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh installCpe
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.INSTALL_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DispatchCPEBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/install-cpe")
	public ResponseResource<InstallCPEBean> installCpe(
			@RequestBody InstallCPEBean installCPEBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.installCpe(installCPEBean), Status.SUCCESS);
	}

	/**
	 * This method is used for Customer CPE Configuration Confirmation
	 *
	 * @param taskId
	 * @param customerCPEConfigConfirmationBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh customCpeConfigConfirmation
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CUSTOMER_CPE_CONFIGURATION_CONFIRMATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerCPEConfigConfirmationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/get-cpe-config-confirmation")
	public ResponseResource<CustomerCPEConfigConfirmationBean> customCpeConfigConfirmation(
			@RequestBody CustomerCPEConfigConfirmationBean customerCPEConfigConfirmationBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.customCpeConfigConfirmation(customerCPEConfigConfirmationBean),
				Status.SUCCESS);
	}

	/**
	 * This method is used for Customer CPE Installation Confirmation
	 *
	 * @param taskId
	 * @param CustomerCPEInstallationConfirmationBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh customCpeInstallationConfirmation
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CUSTOMER_CPE_INSTALLATION_CONFIRMATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerCPEInstallationConfirmationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/get_cpe_installation_confirmation")
	public ResponseResource<CustomerCPEInstallationConfirmationBean> customCpeInstallationConfirmation(
			@RequestBody CustomerCPEInstallationConfirmationBean customerCPEInstallationConfirmationBean)
			throws TclCommonException {
		return new ResponseResource<CustomerCPEInstallationConfirmationBean>(
				ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, cpeImplementationService
						.customCpeInstallationConfirmation(customerCPEInstallationConfirmationBean),
				Status.SUCCESS);
	}
	
	/**
	 * This method is used to Configure CPE
	 *
	 * @param taskId
	 * @param ConfigureCpeBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/configure-cpe")
	public ResponseResource<ConfigureCpeBean> configureCpe(
			@RequestBody ConfigureCpeBean configureCpeBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.installCpe(configureCpeBean), Status.SUCCESS);
	}

	/**
	 * This method is used to map named customer.
	 *
	 * @param taskId
	 * @param mapNamedCustomerBean
	 * @return
	 * @throws TclCommonException
	 * @author vishesh awasthi
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/map-named-customer")
	public ResponseResource<MapNamedCustomerBean> mapNamedCustomer(
										@RequestBody MapNamedCustomerBean mapNamedCustomerBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.mapNamedCustomer(mapNamedCustomerBean), Status.SUCCESS);
	}


	/**
	 * This method is used to map named customer.
	 *
	 * @param taskId
	 * @param confirmMaterialAvailabilityBean
	 * @return
	 * @throws TclCommonException
	 * @author vishesh awasthi
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
				cpeImplementationService.confirmMaterialAvailability(confirmMaterialAvailabilityBean), Status.SUCCESS);
	}


	/**
	 * This method is used to provide wbsglcc details..
	 *
	 * @param taskId
	 * @param provideWbsglccDetailBean
	 * @return
	 * @throws TclCommonException
	 * @author vishesh awasthi
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/provide-wbsglcc-details")
	public ResponseResource<ProvideWbsglccDetailBean> provideWbsglccDetails(
											@RequestBody ProvideWbsglccDetailBean provideWbsglccDetailBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.provideWbsglccDetails(provideWbsglccDetailBean), Status.SUCCESS);
	}

	/**
	 * This method is used to provide wbsglcc details..
	 *
	 * @param taskId
	 * @param trackCpeDeliveryBean
	 * @return
	 * @throws TclCommonException
	 * @author vishesh awasthi
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
				cpeImplementationService.trackCpeDelivery(trackCpeDeliveryBean), Status.SUCCESS);
	}


	/**
	 *
	 * @param taskId
	 * @param prCpeOrderBean
	 * @return
	 * @throws TclCommonException
	 * @author vishesh awasthi
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/pr-cpe-order")
	public ResponseResource<PrCpeOrderBean> pRCpeOrder(
										   @RequestBody PrCpeOrderBean prCpeOrderBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.pRCpeOrder(prCpeOrderBean), Status.SUCCESS);
	}


	/**
	 *
	 * @param taskId
	 * @param poReleaseCpeOrderBean
	 * @return
	 * @throws TclCommonException
	 * @author vishesh awasthi
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
				cpeImplementationService.poReleaseForCpeOrder(poReleaseCpeOrderBean), Status.SUCCESS);
	}


	/**
	 *
	 * @param taskId
	 * @param prCpeInstallationBean
	 * @return
	 * @throws TclCommonException
	 * @author vishesh awasthi
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/pr-cpe-installation")
	public ResponseResource<PrCpeOrderBean> prCpeInstallation(
												@RequestBody PrCpeInstallationBean prCpeInstallationBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.prCpeInstallation(prCpeInstallationBean), Status.SUCCESS);
	}


	/**
	 *
	 * @param taskId
	 * @param poReleaseCpeInstallationBean
	 * @return
	 * @throws TclCommonException
	 * @author vishesh awasthi
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIGURE_CPE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/po-release-cpe-installation")
	public ResponseResource<PrCpeOrderBean> poReleaseCpeInstallation(
														 @RequestBody PoReleaseCpeInstallationBean poReleaseCpeInstallationBean) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.poReleaseCpeInstallation(poReleaseCpeInstallationBean), Status.SUCCESS);
	}
	
	

	/**
	 * This method is used for Customer CPE Configuration Confirmation
	 *
	 * @param taskId
	 * @param customerCPEConfigConfirmationBean
	 * @return
	 * @throws TclCommonException
	 * @author vivek customCpeConfigConfirmation
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CUSTOMER_CPE_CONFIGURATION_CONFIRMATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = WbsTransferJeopardy.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/wbs-jeopardy")
	public ResponseResource<WbsTransferJeopardy> customCpeConfigConfirmation(
			@RequestBody WbsTransferJeopardy WbsTransferJeopardy)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cpeImplementationService.wbsJeopardy(WbsTransferJeopardy),
				Status.SUCCESS);
	}
}
