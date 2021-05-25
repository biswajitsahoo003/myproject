package com.tcl.dias.serviceactivation.datamigration;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains Rest end points for data migration.
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/datamigrate")
public class DataMigrationController {

	@Autowired
	DataMigrationILL dataMigrationIll;
	
	@Autowired
	DataMigrationGVPN dataMigrationGvpn;


	/**
	 *
	 * This method uploads the xlsx file for ILL migration
	 *
	 * @author diksha garg
	 */
	@ApiOperation(value = "Upload excel data ILL")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/ill", method = RequestMethod.POST, consumes = "multipart/form-data")
	public ResponseResource<Object> requestExcelUploadIll(@RequestParam("file") MultipartFile file)
			throws Exception {
		Object response = dataMigrationIll.excelUpload(file);
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?>) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
	
	/**
	 *
	 * This method uploads the xlsx file for GVPN migration
	 *
	 * @author diksha garg
	 */
	@ApiOperation(value = "Upload excel data GVPN")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpn", method = RequestMethod.POST, consumes = "multipart/form-data")
	public ResponseResource<Object> requestExcelUploadGvpn(@RequestParam("file") MultipartFile file)
			throws Exception {
	
		Object response = dataMigrationGvpn.excelUpload(file);
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?>) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
	
	/**
	 *
	 * This method re-migrates failures for ILL
	 *
	 * @author diksha garg
	 * @throws TclCommonException 
	 */
	@ApiOperation(value = "Re-migrate Failures - ILL")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/re-ill", method = RequestMethod.POST)
	public ResponseResource<String> reMigrateIll()throws Exception {
			String response = dataMigrationIll.reMigrateAll();
			Status status = null;
			String responseStatus = "";
			if (response.equals("SUCCESS")) {
				responseStatus = ResponseResource.RES_SUCCESS;
				status = Status.SUCCESS;
			} else {
				responseStatus = ResponseResource.RES_FAILURE;
				status = Status.FAILURE;
			}

			return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
	
	/**
	 *
	 * This method re-migrates specifeid ids for ill or gvpn
	 *
	 * @author diksha garg
	 * @throws TclCommonException 
	 */
	@ApiOperation(value = "Re-migrate Failures - ILL")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/migrate-any", method = RequestMethod.POST)
	public ResponseResource<String> reMigrateAnySpec(@RequestBody List<DataReMigDetailsBean> dataReMigDetails)
			throws Exception {

		String response = null;
		for (DataReMigDetailsBean migId : dataReMigDetails) {
			if (migId.getType().toLowerCase().contains("ill") || migId.getType().toLowerCase().contains("ias"))
				response = dataMigrationIll.migrateSpecInst(migId.getServiceId(), migId.getInstanceId());
			
			if (migId.getType().toLowerCase().contains("gvpn"))
				response = dataMigrationGvpn.migrateSpecInst(migId.getServiceId(),migId.getInstanceId());
		}

		Status status = null;
		String responseStatus = "";
		if (response.equals("SUCCESS")) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
	
	/**
	 *
	 * This method re-migrates failures for GVPN
	 *
	 * @author diksha garg
	 * @throws TclCommonException 
	 */
	@ApiOperation(value = "Re-migrate Failures - GVPN")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/re-gvpn", method = RequestMethod.POST)
	public ResponseResource<String> reMigrateGvpn()throws Exception {
			String response = dataMigrationGvpn.reMigrateAll();
			Status status = null;
			String responseStatus = "";
			if (response.equals("SUCCESS")) {
				responseStatus = ResponseResource.RES_SUCCESS;
				status = Status.SUCCESS;
			} else {
				responseStatus = ResponseResource.RES_FAILURE;
				status = Status.FAILURE;
			}

			return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
	
	/**
	 *
	 * This method uploads the xlsx file for GVPN migration Without Instance Id
	 *
	 * @author diksha garg
	 */
	@ApiOperation(value = "Upload excel data GVPN Without Instance Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gvpnwi", method = RequestMethod.POST, consumes = "multipart/form-data")
	public ResponseResource<Object> requestExcelUploadGvpnWi(@RequestParam("file") MultipartFile file)
			throws Exception {
	
		Object response = dataMigrationGvpn.excelUploadWi(file);
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?>) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
	
	/**
	 *
	 * This method uploads the xlsx file for ILL migration Without Instance Id
	 *
	 * @author diksha garg
	 */
	@ApiOperation(value = "Upload excel data ILL Without Instance Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/illwi", method = RequestMethod.POST, consumes = "multipart/form-data")
	public ResponseResource<Object> requestExcelUploadIllWi(@RequestParam("file") MultipartFile file)
			throws Exception {
	
		Object response = dataMigrationIll.excelUploadWi(file);
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?>) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
}
