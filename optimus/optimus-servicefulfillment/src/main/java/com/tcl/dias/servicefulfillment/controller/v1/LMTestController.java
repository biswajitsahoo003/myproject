package com.tcl.dias.servicefulfillment.controller.v1;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.TestOffnetWirelessLMBean;
import com.tcl.dias.servicefulfillment.beans.TestOnnetWirelessLMBean;
import com.tcl.dias.servicefulfillment.beans.TestOnnetWirelineLMBean;
import com.tcl.dias.servicefulfillment.service.LMTestService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Rest point controller for all LM Test tasks.
 *
 * @author Yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/lm-test/task")
public class LMTestController {

	@Autowired
	private LMTestService lmTestService;

	/**
	 * This method is used to Conduct LM Test for Onnet wireline
	 *
	 * @param taskId
	 * @param conductLMTestBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh conductLMTestOnnetWireline
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LMTest.CONDUCT_LM_TEST_ONNET_WIRELINE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TestOnnetWirelineLMBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/conduct-lm-test-onnet-wireline")
	public ResponseResource<TestOnnetWirelineLMBean> conductLMTestOnnetWireline(
			@RequestBody TestOnnetWirelineLMBean conductLMTestBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmTestService.conductLMTestOnnetWireline( conductLMTestBean), Status.SUCCESS);
	}

	/**
	 * This method is used to Conduct LM Test for Onnet Wireless
	 *
	 * @param taskId
	 * @param testOnnetWirelessLMBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh conductLMTestOnnetWireless
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LMTest.CONDUCT_LM_TEST_ONNET_WIRELESS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TestOnnetWirelessLMBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/conduct-lm-test-onnet-wireless")
	public ResponseResource<TestOnnetWirelessLMBean> conductLMTestOnnetWireless(
			@RequestBody TestOnnetWirelessLMBean testOnnetWirelessLMBean) throws TclCommonException {

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmTestService.conductLMTestOnnetWireless( testOnnetWirelessLMBean), Status.SUCCESS);
	}

	/**
	 * This method is used to Conduct LM Test for Offnet Wireless
	 *
	 * @param taskId
	 * @param testOffnetWirelessLMBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh conductLMTestOffnetWireless
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LMTest.CONDUCT_LM_TEST_OFFNET_WIRELESS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TestOffnetWirelessLMBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/conduct-lm-test-offnet-wireless")
	public ResponseResource<TestOffnetWirelessLMBean> conductLMTestOffnetWireless(
			 @RequestBody TestOffnetWirelessLMBean testOffnetWirelessLMBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmTestService.conductLMTestOffnetWireless( testOffnetWirelessLMBean), Status.SUCCESS);
	}

	@ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = TestOffnetWirelessLMBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
            @PostMapping("/saveAttachment")
            public void saveAttachement(
            @RequestBody(required = false) Map<String, Object> map) throws TclCommonException{
            lmTestService.saveAttachement( map);

            }

}
