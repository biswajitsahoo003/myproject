package com.tcl.dias.ticketing.service.request.management.controller.v1;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.tcl.dias.beans.FilterRequest;
import com.tcl.dias.beans.UpdateRequestBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.filters.service.v1.FilterService;
import com.tcl.dias.response.beans.ServiceResponseBean;
import com.tcl.dias.snow.response.beans.Result;
import com.tcl.dias.snow.response.beans.SnowResponse;
import com.tcl.dias.ticketing.request.CreateSRRequest;
import com.tcl.dias.ticketing.request.UpdateSRRequest;
import com.tcl.dias.ticketing.response.CreateSRResponse;
import com.tcl.dias.ticketing.response.GetTicketSRResponse;
import com.tcl.dias.ticketing.response.GetTicketStatusResponse;
import com.tcl.dias.ticketing.response.ServiceRequestResponse;
import com.tcl.dias.ticketing.response.TicketingResponse;
import com.tcl.dias.ticketing.service.request.management.service.v1.ServiceRequestManagementService;
import com.tcl.dias.ticketing.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 used
 * for the service request related end point
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/servicerequest")
public class ServiceRequestManagementController {

	@Autowired
	ServiceRequestManagementService serviceRequestManagementService;

	@Autowired
	FilterService filterService;
	/**
	 * used to get the ticket details related to Service request
	 * Management
	 * 
	 * @param filters
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @param requestType
	 * @return ServiceRequestResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceRequestManagement.GET_TICKETS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/details/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceRequestResponse> getTicketDetails(@RequestParam(name = "filters",required=false) String filters,
			@RequestParam(name = "limit",required=false) Integer limit, @RequestParam(name = "offset",required=false) Integer offset,
			@RequestParam(name = "sortBy",required=false) String sortBy, @RequestParam(name = "sortOrder",required=false) String sortOrder,
			@RequestParam(name = "requestType", required = true) String requestType) throws TclCommonException {
		ServiceRequestResponse response = serviceRequestManagementService.getDetails(filters, limit, offset, sortBy,
				sortOrder, requestType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * used to get the ticket details related to Service request
	 * Management
	 * 
	 * @param filters
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @param requestType
	 * @return ServiceRequestResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceRequestManagement.GET_TICKETS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/details/tickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceRequestResponse> getAllTicketDetails(@RequestBody FilterRequest filterRequest ) throws TclCommonException {
		ServiceRequestResponse response = serviceRequestManagementService.getAllTicketDetails(filterRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * create a service request ticket
	 * 
	 * @param request
	 * @return CreateSRResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceRequestManagement.CREATE_TICKETS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<CreateSRResponse>> createTicket(@RequestBody CreateSRRequest request)
			throws TclCommonException {
		List<CreateSRResponse> response = serviceRequestManagementService.createTicketDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * used to get the ticket details by ticket id Management
	 * 
	 * @param ticketId
	 * @return GetTicketSRResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceRequestManagement.GET_TICKET_BY_ID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/{ticketId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GetTicketSRResponse> getTicketDetails(@PathVariable(name = "ticketId") String ticketId)
			throws TclCommonException {
		GetTicketSRResponse response = serviceRequestManagementService.getTicketDetails(ticketId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * used to update the ticket details by ticket id
	 * 
	 * @param ticketId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceRequestManagement.UPDATE_TICKET_BY_ID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/{ticketId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GetTicketSRResponse> updateTicketDetails(@PathVariable(name = "ticketId") String ticketId,
			@RequestBody UpdateSRRequest request) throws TclCommonException {
		GetTicketSRResponse response = serviceRequestManagementService.updateTicketDetails(ticketId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @auth chetan chaudhary
	 * @param searchBy
	 * @param valueFor
	 * @param serviceType
	 * @param requestType
	 * @param type
	 * @param status
	 * @return
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceRequestManagement.FILTERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/filters", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<RestResponse> filters(
			@RequestParam(required = false,name ="searchBy") String searchBy,
			@RequestParam(required = true, name = "valueFor") String valueFor,
			@RequestParam(required = false, name ="serviceType") String serviceType,
			@RequestParam(required = false, name ="requestType") String requestType,
			@RequestParam(required = true, name ="type") String type,
			@RequestParam(required = false, name ="status") String status) throws TclCommonException{
		HashMap<String, String> mapping = new HashMap<>();
		String[] vArray = {"serviceType","requestType","status"};
		if(Arrays.stream(vArray).noneMatch(valueFor::equals)) {
			return new ResponseResource<>(ResponseResource.R_CODE_BAD_REQUEST, ResponseResource.RES_FAILURE, null,
					Status.ERROR);
		}

		if(type.equals("SR")) {
			mapping.put("requestType", "cat_item");
			mapping.put("status", "state");
			mapping.put("serviceType", "u_product_name");


			Map<String, String>  srMapper = Stream
					.of(new AbstractMap.SimpleImmutableEntry<>("Open", "1"),
							new AbstractMap.SimpleImmutableEntry<>("Work In Progress", "2"),
							new AbstractMap.SimpleImmutableEntry<>("Closed Complete", "3"),
							new AbstractMap.SimpleImmutableEntry<>("Closed Incomplete", "4"),
							new AbstractMap.SimpleImmutableEntry<>("Pending", "-5"),
							new AbstractMap.SimpleImmutableEntry<>("On Hold", "6"),
							new AbstractMap.SimpleImmutableEntry<>("Closed Skipped", "7"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			String statusNumber = null;
			
			if (StringUtils.isNoneBlank(status)) {
				statusNumber = srMapper.get(status);
			}
			RestResponse response = filterService.filterValuesForServiceRequest(searchBy, serviceType, requestType, statusNumber, mapping.get(valueFor));
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}
		else if(type.equals("CR")) {
			mapping.put("serviceType", "u_product_name");
			mapping.put("requestType", "u_subcategory");
			mapping.put("status", "state");

			Map<String, String> crMapper = Stream
					.of(new AbstractMap.SimpleImmutableEntry<>("New", "-5"),
							new AbstractMap.SimpleImmutableEntry<>("Assess", "-4"),
							new AbstractMap.SimpleImmutableEntry<>("Authorize", "-3"),
							new AbstractMap.SimpleImmutableEntry<>("Scheduled", "-2"),
							new AbstractMap.SimpleImmutableEntry<>("Implement", "-1"),
							new AbstractMap.SimpleImmutableEntry<>("Review", "0"),
							new AbstractMap.SimpleImmutableEntry<>("Closed", "3"),
							new AbstractMap.SimpleImmutableEntry<>("Canceled", "4"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			String statNum = null;
			if (StringUtils.isNoneBlank(status)) {
				statNum = crMapper.get(status);
			}
			
			String mappedRType = null;
			if (StringUtils.isNoneBlank(requestType)) {

				RestResponse response = filterService.getMapValueForRequestType("u_subcategory");
				if (response != null && response.getData() != null) {
					SnowResponse snowResp = null;
					try {
						snowResp = new ObjectMapper().readValue(response.getData(), SnowResponse.class);

						Optional<Result> possibleMatch = Arrays.asList(snowResp.getResult()).stream()
								.filter(x -> x.getGroupby_fields()[0].getDisplay_value().equalsIgnoreCase(requestType))
								.findFirst();
						if (possibleMatch.isPresent()) {
							mappedRType = possibleMatch.get().getGroupby_fields()[0].getValue();
						}
					} catch (IOException e) {
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					}
				}

			}
			RestResponse response = filterService.filterValuesForChangeRequest(searchBy, serviceType, mappedRType, statNum, mapping.get(valueFor));
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}
		else {
			return new ResponseResource<>(ResponseResource.R_CODE_BAD_REQUEST, ResponseResource.RES_FAILURE, null,
					Status.ERROR);
		}
			
	}
	
	/**
	 * getTicketCountBasedOnStatus used to fetch the count against each ticket
	 * 
	 * @param filterRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceRequestManagement.GET_TICKETS_STATUSWISE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/count/status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<GetTicketStatusResponse>> getTicketsCountForStatus(@RequestBody FilterRequest filterRequest ) throws TclCommonException {
		List<GetTicketStatusResponse> response = serviceRequestManagementService.getTicketsCountForStatus(filterRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author kishore To Download CSV file for Incidents
	 * @param productCode
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws CsvRequiredFieldEmptyException
	 * @throws CsvDataTypeMismatchException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Ticketing.GET_TICKET_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TicketingResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "download/requestDetails", method = RequestMethod.POST)
	public void getIncDetailsFrDownload(@RequestBody FilterRequest filterRequest, HttpServletResponse response)
			throws TclCommonException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		serviceRequestManagementService.getCSVForRequestDetails(filterRequest, response);

	}


}
