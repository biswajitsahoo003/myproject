package com.tcl.dias.ticketing.service.request.management.service.v1;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.tcl.dias.beans.AdditionalVariablesBean;
import com.tcl.dias.beans.CommonVariablesBean;
import com.tcl.dias.beans.CommonVariablesBeanResp;
import com.tcl.dias.beans.FilterRequest;
import com.tcl.dias.beans.ServiceIdDetails;
import com.tcl.dias.beans.TicketsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.constants.ExceptionConstants;
import com.tcl.dias.constants.TicketingParamConstants;
import com.tcl.dias.ticketing.columnStatergy.CustomMappingStrategy;
import com.tcl.dias.ticketing.request.CreateOutSRRequest;
import com.tcl.dias.ticketing.request.CreateSRRequest;
import com.tcl.dias.ticketing.request.UpdateSRRequest;
import com.tcl.dias.ticketing.response.CreateSRResponse;
import com.tcl.dias.ticketing.response.ErrorResponseDetails;
import com.tcl.dias.ticketing.response.GetTicketSRResponse;
import com.tcl.dias.ticketing.response.GetTicketStatusResponse;
import com.tcl.dias.ticketing.response.RequestsCSVDownload;
import com.tcl.dias.ticketing.response.ServiceRequestResponse;
import com.tcl.dias.ticketing.response.ServiceRequestResponseData;
import com.tcl.dias.ticketing.service.category.service.v1.TicketingAbstractService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
All the service request management related
 * Services will be implemented in this class
 * 
 * get
 * 
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class ServiceRequestManagementService extends TicketingAbstractService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestManagementService.class);

	@Value("${service.request.management.url}")
	String serviceRequestManagementUrl;

	@Value("${app.id}")
	String appId;

	@Value("${secret.key}")
	String appSecret;

	@Autowired
	RestClientService restClientService;

	/**
	 * get all tickets related to service request. Bulk get
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

	public ServiceRequestResponse getDetails(String filters, Integer limit, Integer offset, String sortBy,
			String sortOrder, String requestType) throws TclCommonException {
		if (Objects.isNull(requestType))
			throw new TclCommonException(ExceptionConstants.REQUESTTYPE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		ServiceRequestResponse serviceRequestResponse = null;
		RestResponse response = restClientService.getWithQueryParam(serviceRequestManagementUrl,
				getServiceRequestParam(filters, limit!=null ? limit.toString():null, offset!=null ? offset.toString():null, sortBy, sortOrder, requestType),
				getBasicAuth(appId, appSecret, getHeader(), null));
		if (response.getStatus() == Status.SUCCESS) {

			return (ServiceRequestResponse) Utils.convertJsonToObject(response.getData(), ServiceRequestResponse.class);
		} else {
			serviceRequestResponse = new ServiceRequestResponse();
			ErrorResponseDetails errorResponseDetails = createErrorResponse(response);
			serviceRequestResponse.setStatus(errorResponseDetails.getStatus());
			serviceRequestResponse.setMessage(errorResponseDetails.getMessage());
		}
		return serviceRequestResponse;
	}

	/**
	 * create the ticket
	 * 
	 * @param request
	 * @return CreateSRResponse
	 * @throws TclCommonException
	 */

	public List<CreateSRResponse> createTicketDetails(CreateSRRequest request) throws TclCommonException {
		List<CreateSRResponse> srResponseList = new ArrayList<>();
		if (Objects.isNull(request))
			throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		if(request.getCatalogName().equalsIgnoreCase("Create RFO Request") || request.getCatalogName().equalsIgnoreCase("Generic Service Requests")) {
			if(request.getAdditionalVariables()!=null) {
					request.getAdditionalVariables().setChannel("portal");
			}
			else {
				AdditionalVariablesBean additionalVariablesBean=new AdditionalVariablesBean();
				additionalVariablesBean.setChannel("portal");
				request.setAdditionalVariables(additionalVariablesBean);
				
			}
		}
		/** 07/08/2020*/
		LOGGER.info("request.getCommonVariables {}", request.getCommonVariables());
		if (Objects.isNull(request.getCommonVariables())|| request.getCommonVariables().getServiceIdDetails().isEmpty() || Objects.isNull(request.getCommonVariables().getServiceIdDetails()))
			throw new TclCommonException(ExceptionConstants.CREATE_TICKET_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		request.getCommonVariables().getServiceIdDetails().forEach(serviceIdDetails -> {
			
			CreateOutSRRequest createRequest = new CreateOutSRRequest();
			CreateSRResponse createSRResponse = new CreateSRResponse();
			
			BeanUtils.copyProperties(request, createRequest);
			populateCustomerVariables(request,serviceIdDetails,createRequest);
		
		try {
			
		RestResponse response = restClientService.postWithClientRepo(serviceRequestManagementUrl,
				Utils.convertObjectToJson(createRequest), getBasicAuth(appId, appSecret, getHeader(), null));
		LOGGER.info("ServiceRequest response  {}", response);
		if (response.getStatus() == Status.SUCCESS) {
			createSRResponse = (CreateSRResponse) Utils.convertJsonToObject(response.getData(), CreateSRResponse.class);
			createSRResponse.setServiceIdentifier(serviceIdDetails.getServiceIdentifier());

		} else {
			createSRResponse = new CreateSRResponse();
			ErrorResponseDetails errorDetails = createErrorResponse(response);
			createSRResponse.setStatus(errorDetails.getStatus());
			createSRResponse.setMessage(errorDetails.getMessage());
			createSRResponse.setServiceIdentifier(serviceIdDetails.getServiceIdentifier());
		}
		
		} 
		
		catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.CREATE_TICKET_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		srResponseList.add(createSRResponse);
		LOGGER.info("srResponseList {}", srResponseList);
		
	});
		return srResponseList;
	}

	private void populateCustomerVariables(CreateSRRequest createRequest, ServiceIdDetails serviceIdDetails, CreateOutSRRequest createOutRequest) {
		LOGGER.info("Setting the common variables to create input request createRequest {}, serviceIdDetailsList {}",
				createRequest,serviceIdDetails);
		CommonVariablesBeanResp cvb = new CommonVariablesBeanResp();
		CommonVariablesBean commonvariableBean=createRequest.getCommonVariables();
		cvb.setAccount(createRequest.getCommonVariables().getAccount()!=null?createRequest.getCommonVariables().getAccount():"" );
		cvb.setAsset(createRequest.getCommonVariables().getAsset()!=null? createRequest.getCommonVariables().getAsset():"");
		cvb.setContactMailID(createRequest.getCommonVariables().getContactMailID()!=null? createRequest.getCommonVariables().getContactMailID():"");
		cvb.setContactName(createRequest.getCommonVariables().getContactName()!=null? createRequest.getCommonVariables().getContactName():"");
		cvb.setContactNumber(createRequest.getCommonVariables().getContactNumber()!=null? createRequest.getCommonVariables().getContactNumber():"");
		cvb.setCustomerSiteName(serviceIdDetails.getCustomerSiteName()!=null? serviceIdDetails.getCustomerSiteName():"");
		cvb.setDetailDescription(createRequest.getCommonVariables().getDetailDescription()!=null? createRequest.getCommonVariables().getDetailDescription():"");
		cvb.setMultipleServiceIDSAffected(createRequest.getCommonVariables().getMultipleServiceIDSAffected()!=null? createRequest.getCommonVariables().getMultipleServiceIDSAffected():"");
		cvb.setProduct(createRequest.getCommonVariables().getProduct()!=null? createRequest.getCommonVariables().getProduct():"");
		cvb.setRequestedByDate(createRequest.getCommonVariables().getRequestedByDate());
		cvb.setRequestedEndDate(createRequest.getCommonVariables().getRequestedEndDate());
		cvb.setRequestedStartDate(createRequest.getCommonVariables().getRequestedStartDate());
		cvb.setServiceIdentifier(serviceIdDetails.getServiceIdentifier()!=null? serviceIdDetails.getServiceIdentifier():"");
		cvb.setShortDescription(createRequest.getCommonVariables().getShortDescription()!=null? createRequest.getCommonVariables().getShortDescription():"");
		createOutRequest.setCommonVariables(cvb);
		LOGGER.info("Input Request for CommonVariables formed CommonVariablesRespBean {}", createOutRequest.getCommonVariables());
	}

	/**
	 *  used to get the ticket details based on the given input
	 * ticket id
	 * 
	 * @param ticketId
	 * @return GetTicketSRResponse
	 * @throws TclCommonException
	 */

	public GetTicketSRResponse getTicketDetails(String ticketId) throws TclCommonException {
		if (Objects.isNull(ticketId))
			throw new TclCommonException(ExceptionConstants.TICKETID_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		GetTicketSRResponse ticketResponse = null;
		RestResponse response = restClientService.getWithQueryParam(serviceRequestManagementUrl + "/" + ticketId, null,
				getBasicAuth(appId, appSecret, getHeader(), null));
		if (response.getStatus() == Status.SUCCESS) {
			return (GetTicketSRResponse) Utils.convertJsonToObject(response.getData(), GetTicketSRResponse.class);
		} else {
			ticketResponse = new GetTicketSRResponse();
			ErrorResponseDetails errorDetails = createErrorResponse(response);
			ticketResponse.setStatus(errorDetails.getStatus());
			ticketResponse.setMessage(errorDetails.getMessage());
		}
		LOGGER.info("getTicketDetails response  {}", response);

		return ticketResponse;
	}

	/**
	 * used to update the ticket details based on the given
	 * input ticket id
	 * 
	 * @param ticketId
	 * @param request
	 * @return GetTicketSRResponse
	 * @throws TclCommonException
	 */

	public GetTicketSRResponse updateTicketDetails(String ticketId, UpdateSRRequest request) throws TclCommonException {
		if (Objects.isNull(ticketId) || Objects.isNull(request))
			throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		GetTicketSRResponse getResponse = null;
		RestResponse response = restClientService.patch(serviceRequestManagementUrl + "/" + ticketId,
				Utils.convertObjectToJson(request), getBasicAuth(appId, appSecret, getHeader(), null));
		LOGGER.info("updateTicket response  {}", response);
		if (response.getStatus() == Status.SUCCESS) {
			return (GetTicketSRResponse) Utils.convertJsonToObject(response.getData(), GetTicketSRResponse.class);
		} else {
			getResponse = new GetTicketSRResponse();
			ErrorResponseDetails errorDetails = createErrorResponse(response);
			getResponse.setStatus(errorDetails.getStatus());
			getResponse.setMessage(errorDetails.getMessage());
		}
		return getResponse;

	}

	public ServiceRequestResponse getAllTicketDetails(FilterRequest filterRequest) throws TclCommonException {

		if (Objects.isNull(filterRequest))
			throw new TclCommonException(ExceptionConstants.REQUESTTYPE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		ServiceRequestResponse serviceRequestResponse = null;
		RestResponse response = restClientService.getWithQueryParam(serviceRequestManagementUrl,
				getServiceRequestParam(filterRequest.getFilters(),
						filterRequest.getLimit() != null ? filterRequest.getLimit().toString() : null,
						filterRequest.getOffset() != null ? filterRequest.getOffset().toString() : null,
						filterRequest.getSortBy(), filterRequest.getSortOrder(), filterRequest.getRequestType()),
				getBasicAuth(appId, appSecret, getHeader(), null));
		if (response.getStatus() == Status.SUCCESS) {
			return (ServiceRequestResponse) Utils.convertJsonToObject(response.getData(), ServiceRequestResponse.class);
			
		} else {
			serviceRequestResponse = new ServiceRequestResponse();
			ErrorResponseDetails errorResponseDetails = createErrorResponse(response);
			serviceRequestResponse.setStatus(errorResponseDetails.getStatus());
			serviceRequestResponse.setMessage(errorResponseDetails.getMessage());
		}
		return serviceRequestResponse;
	}
	
	public List<GetTicketStatusResponse> getTicketsCountForStatus(FilterRequest filterRequest) throws TclCommonException {
		List<GetTicketStatusResponse> ticketsStatusResponse;
		try {
			if (Objects.isNull(filterRequest))
				throw new TclCommonException(ExceptionConstants.REQUESTTYPE_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			RestResponse response = restClientService.getWithQueryParam(serviceRequestManagementUrl,
					getServiceRequestParam(filterRequest.getFilters(),
							filterRequest.getLimit() != null ? filterRequest.getLimit().toString() : null,
							filterRequest.getOffset() != null ? filterRequest.getOffset().toString() : null,
							filterRequest.getSortBy(), filterRequest.getSortOrder(), filterRequest.getRequestType()),
					getBasicAuth(appId, appSecret, getHeader(), null));
			ticketsStatusResponse = new ArrayList<>();
			if (response.getStatus() == Status.SUCCESS) {
				ServiceRequestResponse serviceRequestResponse = (ServiceRequestResponse) Utils.convertJsonToObject(response.getData(), ServiceRequestResponse.class);
				return getStatusWithCount(serviceRequestResponse);
			}
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.COMMON_ERROR);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return ticketsStatusResponse;
	}

	private List<GetTicketStatusResponse> getStatusWithCount(ServiceRequestResponse serviceRequestResponse) {
		Map<String, Object> tktStatusWithCount = new HashMap<>();

		ServiceRequestResponseData srResponsedata = serviceRequestResponse.getResult();
		List<TicketsBean> tickets = srResponsedata.getTickets();
		System.out.println("### tkts =" + tickets.size());
		tickets.stream().forEach(tkt -> {
			int count = 1;
			String status = tkt.getRequestMetaData().getState();
			if(tktStatusWithCount.containsKey(status)) {
				count = ((int) tktStatusWithCount.get(status))+1;
			} 
			tktStatusWithCount.put(status, count);
		});
		System.out.println("### tktStatusWithCount = " + tktStatusWithCount);
		return getResponseObject(tktStatusWithCount);
	}

	private List<GetTicketStatusResponse> getResponseObject(Map<String, Object> tktStatusWithCount) {
		List<GetTicketStatusResponse> response = new ArrayList<>();
		for (Entry<String, Object> entry : tktStatusWithCount.entrySet()) {
			GetTicketStatusResponse ticketStatuBean = new GetTicketStatusResponse();
			ticketStatuBean.setStatus(entry.getKey());
			ticketStatuBean.setCount((int) entry.getValue());
			response.add(ticketStatuBean);
		}
		return response;
	}
	
	/**
	 * @author kishore To Download CSV - Requests CR/SR
	 * @param filterRequest
	 * @param response
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws CsvRequiredFieldEmptyException
	 * @throws CsvDataTypeMismatchException
	 * 
	 */

	public void getCSVForRequestDetails(FilterRequest filterRequest, HttpServletResponse response) throws TclCommonException {
		List<RequestsCSVDownload> ticketsBean =new ArrayList<>();
		String filename;
		try {
			ServiceRequestResponse serviceRequestResponse =getAllTicketDetails(filterRequest);
			if(serviceRequestResponse.getResult().getMessage().equalsIgnoreCase(TicketingParamConstants.SUCCESS)
					&& !serviceRequestResponse.getResult().getTickets().isEmpty()) {
				ticketsBean = setCsvResponse(serviceRequestResponse.getResult().getTickets());
				
				if(filterRequest.getRequestType().equalsIgnoreCase(TicketingParamConstants.CHANGE))
					filename = TicketingParamConstants.CHANGE_CSV_FILENAME;
				else
					filename = TicketingParamConstants.SERVICE_CSV_FILENAME;
				response.reset();
				response.setContentType("text/csv");
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
				CustomMappingStrategy<RequestsCSVDownload> mappingStrategy = new CustomMappingStrategy<>();
				mappingStrategy.setType(RequestsCSVDownload.class);
				StatefulBeanToCsv<RequestsCSVDownload> writer = new StatefulBeanToCsvBuilder<RequestsCSVDownload>(
						response.getWriter()).withMappingStrategy(mappingStrategy).build();
				// write all users to csv file
				writer.write(ticketsBean);
				
			}
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		}
	}
	
	public List<RequestsCSVDownload> setCsvResponse(List<TicketsBean> ticketsBean){
		List<RequestsCSVDownload> csvReponse=new ArrayList<>();
		ticketsBean.stream().forEach(bean->{
			RequestsCSVDownload requestsCSVDownload = new RequestsCSVDownload();
			requestsCSVDownload.setRequestNo(nullCheckForCsv(bean.getTicketId()));
			requestsCSVDownload.setServiceType(nullCheckForCsv(bean.getCommonVariables().getServiceIdentifier()) + "\n" +
					nullCheckForCsv(bean.getCommonVariables().getProduct()));
			requestsCSVDownload.setRequestType(nullCheckForCsv(bean.getCatalogName()));
			requestsCSVDownload.setStatus(nullCheckForCsv(bean.getRequestMetaData().getState()));
			requestsCSVDownload.setServiceAlais(nullCheckForCsv((String)bean.getRequestMetaData().getAdditionalProperties().get("Service Alias")));
			requestsCSVDownload.setCreationDate(nullCheckForCsv(bean.getRequestMetaData().getCreated()));
			requestsCSVDownload.setUpdatedDate(nullCheckForCsv(bean.getRequestMetaData().getUpdated()));
			csvReponse.add(requestsCSVDownload);
		});
		return csvReponse;
		
	}
	public String nullCheckForCsv(String values) {
		return values == null ? "" : values;
	}
	
}
