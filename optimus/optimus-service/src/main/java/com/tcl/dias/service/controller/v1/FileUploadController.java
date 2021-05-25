package com.tcl.dias.service.controller.v1;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.service.beans.ServiceResponse;
import com.tcl.dias.service.impl.FilesService;
import com.tcl.dias.service.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This controller class contains the services for uploading a file
 * 
 * @author SEKHAR ER
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/v1/files")
public class FileUploadController {

	@Autowired
	FilesService fileUploadService;

	/**
	 * Takes file and quote details as input and stores the attachment details
	 * 
	 * @param file
	 * @param qouteId
	 * @param orderId
	 * @param quoteLeId
	 * @param nameOfTheOperation
	 * @return FileUploadResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.FileUpload.UPLOAD_FILE)
	@RequestMapping(value = "/upload/attachments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ServiceResponse> upLoadFile(@RequestParam("file") MultipartFile file, @RequestParam(value="orderToLeId", required=false) Integer orderToLeId, 
			@RequestParam(value="quoteToLeId", required=false) Integer quoteToLeId, @RequestParam("referenceId") List<Integer> referenceId,
			@RequestParam("referenceName") String referenceName, 
			@RequestParam("attachmentType") String attachmentType)
			throws TclCommonException {
		
		ServiceResponse response = fileUploadService.processUploadFiles(file, orderToLeId, quoteToLeId, attachmentType, referenceId,  referenceName );
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Take file attributes and download the file attachment
	 * 
	 * @param file
	 * @param qouteId
	 * @param orderId
	 * @param quoteLeId
	 * @param nameOfTheOperation
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.FileDownload.DOWNLOAD_FILE)
	@RequestMapping(value = "/download/attachments/{attachmentId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<Resource> getAttachments(@PathVariable("attachmentId") Integer attachmentId)
			throws TclCommonException, IOException {
		Resource file = fileUploadService.getAttachments(attachmentId);
		HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		  headers.add("Access-Control-Allow-Origin", "*");
		  headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		  headers.add("Access-Control-Allow-Headers", "Content-Type");
		  headers.add("Content-Disposition", "filename=" + file.getFilename());
		  headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		  headers.add("Pragma", "no-cache");
		  headers.add("Expires", "0");
		return ResponseEntity.ok().headers(headers).body(file);
		
	}

}
