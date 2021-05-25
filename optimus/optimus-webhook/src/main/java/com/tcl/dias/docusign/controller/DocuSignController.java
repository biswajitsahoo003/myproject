package com.tcl.dias.docusign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.docusign.service.DocuSignService;

/**
 * This file contains the DocuSignController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/notify")
public class DocuSignController {

	@Autowired
	DocuSignService docusignService;

	@RequestMapping(value = "/docusign/webhook", method = RequestMethod.POST)
	public void processWebhook(@RequestBody String request) {
		docusignService.processWebHook(request);

	}

}
