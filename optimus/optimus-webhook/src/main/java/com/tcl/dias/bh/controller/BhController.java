package com.tcl.dias.bh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.bh.service.BhService;

/**
 * This file contains the BhController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/bh")
public class BhController {

	@Autowired
	BhService bhService;

	@RequestMapping(value = "/product/notification", method = RequestMethod.POST)
	public void processWebhook(@RequestBody String request) {
		bhService.processProductNotification(request);

	}

}
