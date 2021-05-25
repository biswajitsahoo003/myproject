package com.tcl.dias.oms.bulkfeasibility.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.bulkfeasibility.beans.BulkFeasibilityInfoBean;
import com.tcl.dias.oms.bulkfeasibility.beans.StatusInfoResponseBean;
import com.tcl.dias.oms.bulkfeasibility.service.BulkFeasibilityService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the BulkFeasibilityController.java class.
 * 
 * @author Nithya S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("v1/3dmaps")
public class BulkFeasibilityController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BulkFeasibilityController.class);

	@Autowired
	BulkFeasibilityService bulkFeasibilityService;

	/**
	 * This method used to get the status of uploaded file
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author krutsrin
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BULK_FEASIBILITY.BULK_FILE_UPLOAD)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/file/getstatus/{jobId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource <StatusInfoResponseBean> getStatusOfUploadedFile(@PathVariable("jobId") Integer jobId) throws TclCommonException {
		StatusInfoResponseBean response = bulkFeasibilityService.getStatusOfUploadedFile(jobId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *  This method used to download template for mf batch file upload
	 * @param resp
	 * @throws TclCommonException
	 * @throws TclCommonRuntimeException
	 * @throws IOException
	 * @author krutsrin
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BULK_FEASIBILITY.BULK_FILE_UPLOAD)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/download/bulkfeasibility/template")
	public void downloadTemplate(HttpServletResponse resp) throws TclCommonException, TclCommonRuntimeException, IOException {
		bulkFeasibilityService.downloadTemplate(resp);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	}

	/**
	 * This method used to upload file for feasibility check
	 *
	 * @param userId, jobId
	 * @return statusInfoResponseBean
	 * @throws TclCommonException
	 * @author Nithya S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BULK_FEASIBILITY.BULK_FILE_UPLOAD)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/upload/file", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource <StatusInfoResponseBean> uploadFileForFeasibilityCheck(@RequestPart(name="file") MultipartFile file) throws TclCommonException {
		StatusInfoResponseBean response = bulkFeasibilityService.uploadFileForFeasibilityCheck(file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method used to download the output file
	 *
	 * @param jobId
	 * @throws TclCommonException
	 * @author Nithya S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BULK_FEASIBILITY.BULK_OUTPUT_FILE_DOWNLOAD)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/output/file/download/{jobId}", method = RequestMethod.GET)
	public void downloadOutputFile(@PathVariable("jobId") String jobId, HttpServletResponse response) throws TclCommonException {
		bulkFeasibilityService.outputFileDownload(jobId, response);
	}

	/**
	 * This method used to get all bulk feasibility info
	 *
	 * @param page, size
	 * @return
	 * @throws TclCommonException
	 * @author Nithya S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BULK_FEASIBILITY.GET_ALL_BULK_FEASIBILITY_INFO)
	@RequestMapping(value = "/feasibility/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = BulkFeasibilityInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<BulkFeasibilityInfoBean>> getAllBulkFeasibilityInfo(@RequestParam("page") Integer page, 
			@RequestParam("size") Integer size) throws TclCommonException {
		PagedResult<BulkFeasibilityInfoBean> response = bulkFeasibilityService.getAllBulkFeasibility(page, size);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
