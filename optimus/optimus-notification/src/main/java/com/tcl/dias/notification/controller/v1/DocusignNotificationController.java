package com.tcl.dias.notification.controller.v1;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.docusign.service.DocuSignService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This is controller class for Docusign related API's
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/docusign")
public class DocusignNotificationController {

	@Autowired
	DocuSignService docuSignService;

	/**
	 * 
	 * Download docusign envelope documents
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/docusignpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateDocusignPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = docuSignService.getDocumentBasedOnQuoteCode(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	@RequestMapping(value = "/response", method = RequestMethod.POST)
	public ResponseResource<String> notifyDocusign(@RequestBody String request) throws TclCommonException {
		docuSignService.processWebHook(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
}
