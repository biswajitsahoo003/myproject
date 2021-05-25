package com.tcl.dias.batch.odr.npl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RestController
@RequestMapping("batch")
public class NplBatchController {
	@Autowired
	NplOdrService nplOdrService;
	
	@RequestMapping(value = "/test/{orderId}/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> test(@PathVariable("orderId") Integer orderId,
			@PathVariable("userName") String userName) throws TclCommonException {
		nplOdrService.processOrderFrost(orderId, userName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}

}
