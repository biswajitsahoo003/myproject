package com.tcl.dias.ticketing.controller.v1;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.tcl.dias.beans.UpdateRequestBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.filters.service.v1.FilterService;
import com.tcl.dias.response.beans.ProductToServiceCatalogBean;
import com.tcl.dias.response.beans.ServiceCatalogVariablesBean;
import com.tcl.dias.response.beans.ServiceResponseBean;
import com.tcl.dias.snow.response.beans.Result;
import com.tcl.dias.snow.response.beans.SnowResponse;
import com.tcl.dias.ticketing.request.CreateRequest;
import com.tcl.dias.ticketing.request.CreateTicketRequest;
import com.tcl.dias.ticketing.response.CreateTicketResponse;
import com.tcl.dias.ticketing.response.GetTicketStatusResponse;
import com.tcl.dias.ticketing.response.TicketingResponse;
import com.tcl.dias.ticketing.service.v1.TicketingService;
import com.tcl.dias.ticketing.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/ticketing")
public class TicketingController {

	@Autowired
	TicketingService ticketingService;

	@Autowired
	FilterService filterService;
	
	/**
	 * @author vivek getTicketDetails
	 * used to get all the ticketing details
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Ticketing.GET_PARTICULAR_TICKET_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateTicketResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/details/{ticketId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateTicketResponse> getTicketDetails(@PathVariable("ticketId") String ticketId)
			throws TclCommonException {
		CreateTicketResponse response = ticketingService.getTicketDetails(ticketId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * @author vivek getTicketDetails
	 * used to get all the ticketing details
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Ticketing.GET_TICKET_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TicketingResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/details", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TicketingResponse> getDetails(@RequestBody UpdateRequestBean requestBean)
			throws TclCommonException {
		TicketingResponse response = ticketingService.getDetails(requestBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author deepika getTicketDetails
	 * used to get all the ticketing details of tickets except Closed and Cancelled status.
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Ticketing.GET_TICKET_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TicketingResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/count/status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<GetTicketStatusResponse>> getTicketStatusCount(@RequestBody UpdateRequestBean requestBean)
			throws TclCommonException {
		List<GetTicketStatusResponse> response = ticketingService.getTicketsCountForStatus(requestBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author vivek getTicketDetails
	 * used to get all the ticketing details
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Ticketing.CREATE_TICKETS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateTicketResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets", method = RequestMethod.POST)
	public ResponseResource<List<CreateTicketResponse>> createTicket(@RequestBody CreateTicketRequest  requestBean)
			throws TclCommonException {
		List<CreateTicketResponse> response = ticketingService.createTicket(requestBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author vivek getTicketDetails
	 * used to get all the ticketing details
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Ticketing.UPDATE_TICKET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateTicketResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/{ticketId}", method = RequestMethod.POST)
	public ResponseResource<CreateTicketResponse> updateTicket(@RequestBody CreateRequest  requestBean,@PathVariable("ticketId") String ticketId)
			throws TclCommonException {
		CreateTicketResponse response = ticketingService.updateTicket(requestBean,ticketId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author Biswajit
	 * used to get service catalog details
	 * @param productCode
	 * @return ResponseResource<List<ProductToServiceCatalogBean>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceCatalog.GET_SERVICE_CATALOG_INFO)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/serviceCatalogInfo", method = RequestMethod.GET)
	public ResponseResource<List<ProductToServiceCatalogBean>> getServiceCatalog(@RequestParam(required = true, name ="productCode") String productCode)
			throws TclCommonException {
		List<ProductToServiceCatalogBean> response = ticketingService.getServiceCatalog(productCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Biswajit
	 * used to get service catalog attribute details
	 * @param catalogId
	 * @return ResponseResource<List<ServiceCatalogVariablesBean>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceCatalog.GET_SERVICE_CATALOG_ATTRIBUTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/serviceAttribute", method = RequestMethod.GET)
	public ResponseResource<List<ServiceCatalogVariablesBean>> getServiceCatalogAttribute(@RequestParam(required = true, name ="catalogId") Integer catalogId)
			throws TclCommonException {
		List<ServiceCatalogVariablesBean> response = ticketingService.getServiceCatalogAttribute(catalogId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author chetan chaudhary
	 * @param searchBy
	 * @param valueFor
	 * @param serviceType
	 * @param issueType
	 * @param impact
	 * @param status
	 * @param startDate
	 * @param endDate
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
			@RequestParam(required = false, name ="searchBy") String searchBy,
			@RequestParam(required = true, name = "valueFor") String valueFor,
			@RequestParam(required = false, name ="serviceType") String serviceType,
			@RequestParam(required = false, name ="issueType") String issueType,
			@RequestParam(required = false, name ="impact") String impact,
			@RequestParam(required = false, name = "status") String status,
			@RequestParam(required = false, name = "startDate") String startDate,
			@RequestParam(required = false, name = "endDate") String endDate)
			throws TclCommonException, JsonParseException, JsonMappingException, IOException {
		HashMap<String, String> mapping = new HashMap<>();
		mapping.put("serviceType", "product");
		mapping.put("issueType", "category");
		mapping.put("impact", "impact");
		mapping.put("status", "state");
		mapping.put("serviceId", "u_service_id");
		String[] vArray = { "serviceId", "serviceType", "issueType", "impact", "status" };
		String[] data = valueFor.split(",");

		if (!(Arrays.asList(vArray).containsAll(Arrays.asList(data)))) {
			return new ResponseResource<>(ResponseResource.R_CODE_BAD_REQUEST, ResponseResource.RES_FAILURE, null,
					Status.ERROR);
		}
		String valueForCondition = null;
		ArrayList<String> strList = new ArrayList<String>();
		if (data.length > 1) {
			Arrays.stream(data).forEach(x -> strList.add(mapping.get(x)));
			valueForCondition = String.join(",", strList);
		} else {
			valueForCondition = mapping.get(data[0]);
		}

		String issueNumber = null;
		if (StringUtils.isNotBlank(issueType)) {
			RestResponse response = filterService.getNumberForIssueType("category");
			if (response != null && response.getData() != null) {
				SnowResponse snowResp = new ObjectMapper().readValue(response.getData(), SnowResponse.class);
				Optional<Result> possibleMatch = Arrays.asList(snowResp.getResult()).stream()
						.filter(x -> x.getGroupby_fields()[0].getDisplay_value().equalsIgnoreCase(issueType))
						.findFirst();
				if (possibleMatch.isPresent()) {
					issueNumber = possibleMatch.get().getGroupby_fields()[0].getValue();
				}
			}
		}
		String statusNumber = null;
		String impactNumber = null;
		if (StringUtils.isNotBlank(status)) {
			Map<String, String> incidentMapper = Collections.unmodifiableMap(new HashMap<String, String>() {
				{
					put("Pending with customer", "-2");
					put("Work in Progress", "2");
					put("Suspended", "-3");
					put("Closed", "3");
					put("Resolved", "6");
					put("Cancelled", "7");
					put("New", "1");
				}
			});

			if (StringUtils.isNotBlank(incidentMapper.get(status))) {
				statusNumber = incidentMapper.get(status);
			}
		}
		if (StringUtils.isNotBlank(impact)) {
			Map<String, String> incidentImpactMapper = Collections.unmodifiableMap(new HashMap<String, String>() {
				{
					put("Total Loss of Service", "4");
					put("Partial Loss", "5");
					put("No Impact", "6");
				}
			});

			if (StringUtils.isNotBlank(incidentImpactMapper.get(impact))) {
				impactNumber = incidentImpactMapper.get(impact);
			}
		}
		RestResponse response = filterService.filterValuesForIncidentRequest(searchBy, serviceType, issueNumber,
				impactNumber, statusNumber, startDate, endDate, valueForCondition);
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
	@RequestMapping(value = "download/incidentDetails", method = RequestMethod.POST)
	public void getIncDetailsFrDownload(@RequestBody UpdateRequestBean requestBean, HttpServletResponse response)
			throws TclCommonException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		ticketingService.getCSVForIncidents(requestBean, response);

	}

}
