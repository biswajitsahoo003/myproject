package com.tcl.dias.wfe.controller.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.wfe.dto.WfeDto;
import com.tcl.dias.wfe.service.v1.WorkFlowEngineService;
import com.tcl.dias.wfe.swagger.constants.SwaggerConstants.ApiOperations.WorkFlowEngine;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author Manojkumar R
 *
 */
@RestController
@RequestMapping("/v1/wfe")
public class WorkFlowEngineController {

	@Autowired
	private WorkFlowEngineService wfeService;

	@ApiOperation(value = WorkFlowEngine.GET_BY_WFE_ID)
	@RequestMapping(value = "/{wfeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<WfeDto>> getByWfeId(@PathVariable("wfeId") Integer wfeId) throws TclCommonException {
		try {
			List<WfeDto> newList = new ArrayList<WfeDto>();
			WfeDto wfedto1 = new WfeDto();
			wfedto1.setWfeId(1);
			wfedto1.setName("My First work flow engine");
			wfedto1.setDesc("My First work flow engine rest api microservices ");
			List<String> samples = new ArrayList<String>();
			samples.add("ILL");
			samples.add("GVPN");
			samples.add("GSON");
			samples.add("IP Cloud");
			wfedto1.setSampleList(samples);
			newList.add(wfedto1);

			return new ResponseResource<List<WfeDto>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, newList,
					Status.SUCCESS);
		} /*
			 * catch (TclCommonException tce) { throw new TclCommonException(tce); }
			 */catch (Exception e) {
			throw new TclCommonException(e);
		}

	}

	@ApiOperation(value = "getting the wfe json input")
	@RequestMapping(value = "/post-wfe", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<WfeDto> getInputWFE(@RequestBody WfeDto wfeList) throws TclCommonException {
		try {

			return new ResponseResource<WfeDto>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, wfeList,
					Status.SUCCESS);
		} /*
			 * catch (TclCommonException tce) { throw new TclCommonException(tce); }
			 */catch (Exception e) {
			throw new TclCommonException(e);
		}

	}

}
