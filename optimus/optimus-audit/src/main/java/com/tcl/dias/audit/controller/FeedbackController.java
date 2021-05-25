package com.tcl.dias.audit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.audit.dto.FeedbackDto;
import com.tcl.dias.audit.entity.entities.Feedback;
import com.tcl.dias.audit.service.FeedbackService;
import com.tcl.dias.audit.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/feedback")
public class FeedbackController {
	
	@Autowired
	FeedbackService feedbackService;
	
	@ApiOperation(value = SwaggerConstants.Feedback.GET_PAGE_FEEDBACK_DETAILS)
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = FeedbackDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<Feedback>> getPageFeedback(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam(required = false, name = "searchText") String searchText)
			throws TclCommonException {
		PagedResult<Feedback> response = feedbackService.getAllFeedbackDetails(page, size, searchText);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.Feedback.GET_FEEDBACK_DETAILS)
	@RequestMapping(value = "/allFeedbacks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = FeedbackDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<FeedbackDto>> getCustomerDetails() throws TclCommonException {
		List<FeedbackDto> response = feedbackService.getFeedbackDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response ,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.Feedback.SAVE_FEEDBACK_DETAILS)
	@PostMapping("/saveFeedback")
	public ResponseResource<String> addFeedback(@RequestBody FeedbackDto feedbackDto) throws TclCommonException {
		
			feedbackService.createFeedback(feedbackDto);
		
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
		
	}
	
	@ApiOperation(value = SwaggerConstants.Feedback.UPDATE_FEEDBACK_DETAILS)
	@PostMapping("/updateFeedback")
	public ResponseResource<String> updateFeedback(@RequestBody FeedbackDto feedbackDto) throws TclCommonException {
		
			feedbackService.updateFeedback(feedbackDto);
		
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);

	}
	
}
