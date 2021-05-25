package com.tcl.dias.servicehandover.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicehandover.ipc.service.IpcDataUpdateService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * to update billing data
 * 
 * @author Ram
 */
@RestController
@RequestMapping("/v1/ipc/data")
public class IpcDataUpdateController {

	@Autowired
	IpcDataUpdateService ipcDataUpdateService;

	@PostMapping(value = "/services/{serviceCode}/orders/{orderCode}/commissiondate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateCommissionDateAndDeliveryDate(@PathVariable("serviceCode") String serviceCode,
			@PathVariable("orderCode") String orderCode, @RequestBody Map<String, Object> inputM)
			throws TclCommonException {
		String response = ipcDataUpdateService.updateCommissionDateAndDeliveryDate(serviceCode, orderCode, inputM);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}