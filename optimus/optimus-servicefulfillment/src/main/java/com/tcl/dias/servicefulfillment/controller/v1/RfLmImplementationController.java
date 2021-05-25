package com.tcl.dias.servicefulfillment.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.ApproveMastCommercialBean;
import com.tcl.dias.servicefulfillment.beans.BhPo;
import com.tcl.dias.servicefulfillment.beans.ColoPO;
import com.tcl.dias.servicefulfillment.beans.ColoRequest;
import com.tcl.dias.servicefulfillment.beans.ConductRfSiteSurveyBean;
import com.tcl.dias.servicefulfillment.beans.CreateMrnBean;
import com.tcl.dias.servicefulfillment.beans.DeliverRfEquipmentDetails;
import com.tcl.dias.servicefulfillment.beans.InstallRfBean;
import com.tcl.dias.servicefulfillment.beans.PoForMastProviderBean;
import com.tcl.dias.servicefulfillment.beans.RfData;
import com.tcl.dias.servicefulfillment.beans.TaskMastRequest;
import com.tcl.dias.servicefulfillment.beans.WoSiteSurveyBean;
import com.tcl.dias.servicefulfillment.service.RfLmService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Rest point controller for all Rf Lm implementation tasks.
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/rf-lm-implementation/task")
public class RfLmImplementationController {

	@Autowired
	RfLmService rfLmService;

	@Autowired
	TaskDataService taskDataService;
	
	/**
	 * This method is used to create MRN.
	 *
	 * @param taskId
	 * @param createMrnBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.RfLmImplementation.CREATE_MRN)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateMrnBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/rf-mrns")
	public ResponseResource<CreateMrnBean> createMrnForRfEquipment(
			@RequestBody CreateMrnBean createMrnBean) throws TclCommonException {
		return new ResponseResource<CreateMrnBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				rfLmService.createMrnForRfEquipment( createMrnBean), Status.SUCCESS);
	}

	/**
	 * This method is used to install RF.
	 *
	 * @param taskId
	 * @param installRfBean
	 * @return InstallRfBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.RfLmImplementation.INSTALL_RF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallRfBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/install-rf-equipment")
	public ResponseResource<InstallRfBean> installRfEquipment(
			@RequestBody InstallRfBean installRfBean) throws TclCommonException {		
		return new ResponseResource<InstallRfBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				rfLmService.installRfEquipment( installRfBean), Status.SUCCESS);
	}

	/**
	 * This method is used to provide work order for site survey
	 *
	 * @param taskId
	 * @param woSiteSurveyBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg provideWoSiteSurvey
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.RfLmImplementation.PROVIDE_WO_SITE_SURVEY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WoSiteSurveyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/provide-wo-rf-site-survey")
	public ResponseResource<WoSiteSurveyBean> provideWoSiteSurvey(
			@RequestBody WoSiteSurveyBean woSiteSurveyBean) throws TclCommonException {
		WoSiteSurveyBean response = (WoSiteSurveyBean) rfLmService.provideWoSiteSurvey( woSiteSurveyBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Save MAST details for a particular TASK.
	 * 
	 * @author arjayapa
	 * @param taskId
	 * @param mastDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.RfLmImplementation.INSTALL_MAST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskMastRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/install-mast")
	public ResponseResource<TaskMastRequest> saveTaskMastDetails(
			@RequestBody TaskMastRequest mastDetailBean) throws TclCommonException {

		return new ResponseResource<TaskMastRequest>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				rfLmService.saveTaskMastDetails( mastDetailBean), Status.SUCCESS);

	}

	

	/**
	 * Audit the Installed MAST in customer site.
	 * 
	 * @author arjayapa
	 * @param taskId                    This method is used to approve mast
	 *                                  commercials
	 *
	 * @param taskId
	 * @param ApproveMastCommercialBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh approveMastCommercials
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.RfLmImplementation.APPROVE_MAST_COMMERCIALS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ApproveMastCommercialBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/approve-mast-commercials")
	public ResponseResource<ApproveMastCommercialBean> approveMastCommercials(
			@RequestBody ApproveMastCommercialBean approveMastCommercialBean) throws TclCommonException {
		return new ResponseResource<ApproveMastCommercialBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				rfLmService.approveMastCommercials( approveMastCommercialBean), Status.SUCCESS);
	}

	/**
	 * This method is used to po for mast provider
	 *
	 * @param taskId
	 * @param PoForMastProviderBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh poForMastProvider
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.RfLmImplementation.PO_FOR_MAST_PROVIDER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = PoForMastProviderBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/mast-vendor-pos")
	public ResponseResource<PoForMastProviderBean> poForMastProvider(
			@RequestBody PoForMastProviderBean poForMastProviderBean) throws TclCommonException {
		return new ResponseResource<PoForMastProviderBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				rfLmService.poForMastProvider( poForMastProviderBean), Status.SUCCESS);
	}


	/**
	 * Deliver RF Equipment.
	 * 
	 * @author arjayapa
	 * @param taskId
	 * @param auditDetails
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.RfLmImplementation.DELIVER_RF_EQUIPMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DeliverRfEquipmentDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/deliver-rf-equipment")
	public ResponseResource<DeliverRfEquipmentDetails> deliverRfEquipment(
			@RequestBody DeliverRfEquipmentDetails rfEquipDetails) throws TclCommonException {

		return new ResponseResource<DeliverRfEquipmentDetails>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				rfLmService.deliverRfEquipment( rfEquipDetails), Status.SUCCESS);

	}

	/**
	 * RF Conduct site survey
	 * 
	 * @param taskId
	 * @param conductSiteSurveyBean
	 * @return conductSiteSurveyBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONDUCT_SITE_SURVEY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ConductRfSiteSurveyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/conduct-site-survey")
	public ResponseResource<ConductRfSiteSurveyBean> saveConductSiteSurveyDetails(
			@RequestBody ConductRfSiteSurveyBean conductSiteSurveyBean) throws TclCommonException {
		ConductRfSiteSurveyBean response = (ConductRfSiteSurveyBean) rfLmService.saveConductSiteSurveyBean(
				conductSiteSurveyBean);
		return new ResponseResource<ConductRfSiteSurveyBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = "check-colo-availability")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ColoRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/check-colo-availability")
	public ResponseResource<ColoRequest> checkColoAvailability(
			@RequestBody ColoRequest coloRequest) throws TclCommonException {
		
		return new ResponseResource<ColoRequest>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, rfLmService.checkColoAvailability(coloRequest),
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = "provide-po-colo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ColoPO.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/provide-po-colo")
	public ResponseResource<ColoPO> providePoColo(
			@RequestBody ColoPO coloPO) throws TclCommonException {
		return new ResponseResource<ColoPO>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, rfLmService.providePoColo(coloPO),
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = "provide-rf-data")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = RfData.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/provide-rf-data")
	public ResponseResource<RfData> provideRfData(
			@RequestBody RfData rfData) throws TclCommonException {
		return new ResponseResource<RfData>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, rfLmService.provideRfData(rfData),
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = "provide-po-bh")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = BhPo.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/provide-po-bh")
	public ResponseResource<BhPo> providePoBh(
			@RequestBody BhPo bhPo) throws TclCommonException {
		return new ResponseResource<BhPo>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, rfLmService.providePoBh(bhPo),
				Status.SUCCESS);
	}

}
