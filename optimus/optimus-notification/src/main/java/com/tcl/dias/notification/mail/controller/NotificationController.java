package com.tcl.dias.notification.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.docusign.service.DocuSignService;
import com.tcl.dias.notification.beans.ServiceResponse;
import com.tcl.dias.notification.mail.service.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Notification Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/notify")
public class NotificationController {

	@Autowired
	NotificationService notificationService;

	@Autowired
	DocuSignService docusignService;

	/**
	 * Takes file and quote details as input and stores the attachment details
	 * 
	 * @param file
	 * @param qouteId
	 * @param orderId
	 * @param quoteLeId
	 * @param nameOfTheOperation
	 * @return ServiceResponse
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/mail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceResponse> sendFiletoUser(@RequestParam("file") MultipartFile file,
			@RequestParam("email") String email) throws TclCommonException {
		ServiceResponse response = notificationService.processMailAttachment(file, email);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/test", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> sendFiletoUser(@RequestBody String request) throws TclCommonException, IllegalArgumentException{
		notificationService.test(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
}
