package com.tcl.dias.ticketing.service.v1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import java.util.Objects;
import java.util.Optional;

import org.json.simple.parser.ParseException;
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
import com.tcl.dias.beans.ContactBean;
import com.tcl.dias.beans.UpdateRequestBean;
import com.tcl.dias.common.beans.CreateRequestBean;
import com.tcl.dias.common.beans.CreateTicketRequestBean;
import com.tcl.dias.common.beans.CreateTicketResponseBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.constants.ExceptionConstants;
import com.tcl.dias.constants.TicketingParamConstants;
import com.tcl.dias.response.beans.ProductToServiceCatalogBean;
import com.tcl.dias.response.beans.ServiceCatalogVariablesBean;
import com.tcl.dias.serviceassurance.entity.entities.MstServiceCatalogVariables;
import com.tcl.dias.serviceassurance.entity.entities.ProductToServiceCatalog;
import com.tcl.dias.serviceassurance.entity.entities.ServiceCatalogToVariablesMap;
import com.tcl.dias.serviceassurance.entity.repository.MstServiceCatalogVariablesRepository;
import com.tcl.dias.serviceassurance.entity.repository.ProductToServiceCatalogRepository;
import com.tcl.dias.serviceassurance.entity.repository.ServiceCatalogToVariablesMapRepository;
import com.tcl.dias.ticketing.columnStatergy.CustomMappingStrategy;
import com.tcl.dias.ticketing.request.CreateRequest;
import com.tcl.dias.ticketing.request.CreateTicketRequest;
import com.tcl.dias.ticketing.response.CreateTicketResponse;
import com.tcl.dias.ticketing.response.ErrorResponseDetails;
import com.tcl.dias.ticketing.response.GetTicketStatusResponse;
import com.tcl.dias.ticketing.response.IncidentCSVDownload;
import com.tcl.dias.ticketing.response.TicketingResponse;
import com.tcl.dias.ticketing.service.category.service.v1.TicketingAbstractService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.constants.TicketingParamConstants.CUSTOMER_LE_NAME;
import static com.tcl.dias.constants.TicketingParamConstants.SERVICE_TYPE;
import static com.tcl.dias.constants.TicketingParamConstants.TICKET_STATUS;

@Service
public class TicketingService extends TicketingAbstractService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TicketingService.class);

	@Value("${ticketing.details.url}")
	String ticketingDetailsUrl;

	@Value("${app.id}")
	String appId;

	@Value("${secret.key}")
	String appSecret;

	@Autowired
	RestClientService restClientService;

	@Autowired
	ProductToServiceCatalogRepository prodToServiceCatalogRepository;
	
	@Autowired
	ServiceCatalogToVariablesMapRepository serviceCatalogToVariablesMapRepository;
	
	@Autowired
	MstServiceCatalogVariablesRepository mstServiceCatalogVariablesRepository;

	/**
	 * @author vivek used to get the particular ticket details getTicketDetails
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public CreateTicketResponse getTicketDetails(String ticketId) throws TclCommonException {
		CreateTicketResponse ticketResponse = null;
		try {
			if (Objects.isNull(ticketId))
				throw new TclCommonException(ExceptionConstants.TICKETID_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			RestResponse response = restClientService.getWithQueryParam(ticketingDetailsUrl + "/" + ticketId, null,
					getBasicAuth(appId, appSecret, getHeader(), null));
			if (response.getStatus() == Status.SUCCESS) {
				return (CreateTicketResponse) Utils.convertJsonToObject(response.getData(), CreateTicketResponse.class);
			} else {
				ticketResponse = new CreateTicketResponse();
				ErrorResponseDetails errorDetails = createErrorResponse(response);
				ticketResponse.setStatus(errorDetails.getStatus());
				ticketResponse.setMessage(errorDetails.getMessage());
			}
			LOGGER.info("getTicketDetails response  {}", response);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return ticketResponse;
	}

	/**
	 * @author vivek getDetails used to get the att ticketing details
	 * @param {@UpdateRequestBean} requestBean object contains input information for ticketing API.
	 * 
	 * @throws TclCommonException
	 * @throws ParseException
	 * @return {@TicketingResponse}
	 */
	public TicketingResponse getDetails(UpdateRequestBean requestBean) throws TclCommonException {
		TicketingResponse ticketingResponse = null;
		try {
			LOGGER.info("getDetails request data from {} to {} ", requestBean.getCreationDateFrom(), requestBean.getCreationDateTo());
			if (Objects.isNull(requestBean))
				throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);

			// if only one date is sent from UI, then throw exception
			if ((Objects.isNull(requestBean.getCreationDateFrom()) && Objects.nonNull(requestBean.getCreationDateTo()))
					|| (Objects.isNull(requestBean.getCreationDateTo())
							&& Objects.nonNull(requestBean.getCreationDateFrom())))
				throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);

			// if both dates are given from UI, check it is on 1 year range.
			if (requestBean.getCreationDateFrom() != null && requestBean.getCreationDateTo() != null) {
				validateFromToDates(requestBean);
			}

			// UI date fields are empty.. populate date restriction for 1 year from current
			// date.
			if (Objects.isNull(requestBean.getCreationDateFrom()) && Objects.isNull(requestBean.getCreationDateTo())) {
				populateDates(requestBean);
			}

			LOGGER.info("ticketing Details request url {}", ticketingDetailsUrl);
			RestResponse response = restClientService.getWithQueryParam(ticketingDetailsUrl,
					getQueryValues(requestBean), getBasicAuth(appId, appSecret, getHeader(), null));
			if (response.getStatus() == Status.SUCCESS) {
				return (TicketingResponse) Utils.convertJsonToObject(response.getData(), TicketingResponse.class);
			} else {
				ticketingResponse = new TicketingResponse();
				ErrorResponseDetails errorDetails = createErrorResponse(response);
				ticketingResponse.setStatus(errorDetails.getStatus());
				ticketingResponse.setMessage(errorDetails.getMessage());
			}
			LOGGER.info("getDetails response  {}", response);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return ticketingResponse;
	}

	private void populateDates(UpdateRequestBean requestBean) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calobj = Calendar.getInstance();
		Date today =calobj.getTime();
		calobj.add(Calendar.YEAR, -1);
		Date prevYrFromToday = calobj.getTime();
		
		requestBean.setCreationDateFrom(df.format(prevYrFromToday));
		requestBean.setCreationDateTo(df.format(today));
	}
	
	
	/**
	 * @author deepikaS getTicketCountBasedOnStatus used to fetch the count against each ticket
	 * @param {@UpdateRequestBean} requestBean object contains input information for ticketing API.
	 * 
	 * @throws TclCommonException
	 * @throws ParseException
	 * @return {@TicketingResponse}
	 */
	public List<GetTicketStatusResponse> getTicketsCountForStatus(UpdateRequestBean requestBean) throws TclCommonException {
		List<GetTicketStatusResponse> ticketsStatusResponse = new ArrayList<>();
		try {
			if (Objects.isNull(requestBean))
				throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			if (Objects.isNull(requestBean.getCreationDateFrom()) || Objects.isNull(requestBean.getCreationDateTo()))
				throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			validateFromToDates(requestBean);
			RestResponse response = restClientService.getWithQueryParam(ticketingDetailsUrl,
					getQueryValues(requestBean), getBasicAuth(appId, appSecret, getHeader(), null));
			if (response.getStatus() == Status.SUCCESS) {
				TicketingResponse ticketingResponse = (TicketingResponse) Utils.convertJsonToObject(response.getData(), TicketingResponse.class);
				ticketsStatusResponse = getStatusWithCount(ticketingResponse);
			}
			LOGGER.trace("getDetails response  {}", response);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.COMMON_ERROR);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return ticketsStatusResponse;
	}

	private void validateFromToDates(UpdateRequestBean requestBean)
			throws java.text.ParseException, TclCommonException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDate fromDate = LocalDate.parse(requestBean.getCreationDateFrom(), formatter);
		LocalDate toDate = LocalDate.parse(requestBean.getCreationDateTo(), formatter);
		Period diff = Period.between(fromDate, toDate);
		if (diff.getYears() > 1) {
			throw new TclCommonException(ExceptionConstants.DATE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * createTicket
	 * 
	 * @author vivek
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public List<CreateTicketResponse> createTicket(CreateTicketRequest requestBean) throws TclCommonException {
		List<CreateTicketResponse> ticketResponseList = new ArrayList<>();
		try {
			if (Objects.isNull(requestBean))
				throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			if (requestBean.getServiceIdentifier().isEmpty() || Objects.isNull(requestBean.getCategory())
					|| Objects.isNull(requestBean.getImpact()))
				throw new TclCommonException(ExceptionConstants.CREATE_TICKET_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			requestBean.getServiceIdentifier().forEach(serviceIdentifier -> {
				CreateRequest request = new CreateRequest();
				CreateTicketResponse ticketResponse = new CreateTicketResponse();
				BeanUtils.copyProperties(requestBean, request);
				request.setServiceIdentifier(serviceIdentifier);
				try {
					RestResponse response;
					String strRequest=Utils.convertObjectToJson(request);
					HttpHeaders header=getBasicAuth(appId, appSecret, getHeader(), null);
					LOGGER.info("Request Url :{} ,RequestBody: {} : Header {}",ticketingDetailsUrl,strRequest,header);
					response = restClientService.postWithClientRepo(ticketingDetailsUrl,
							strRequest, header);

					LOGGER.info("createTicket response  {}", response);
					if (response.getStatus() == Status.SUCCESS) {

						ticketResponse = (CreateTicketResponse) Utils.convertJsonToObject(response.getData(),
								CreateTicketResponse.class);

					} else {
						ErrorResponseDetails errorDetails = createErrorResponse(response);
						ticketResponse.setStatus(errorDetails.getStatus());
						ticketResponse.setMessage(errorDetails.getMessage());
					}
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.CREATE_TICKET_VALIDATION_ERROR,
							ResponseResource.R_CODE_NOT_FOUND);
				}
				ticketResponseList.add(ticketResponse);
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return ticketResponseList;
	}

	/**
	 * Update Ticket
	 *
	 * @param requestBody
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 */
	public CreateTicketResponse updateTicket(CreateRequest requestBody, String ticketId) throws TclCommonException {
		CreateTicketResponse ticketResponse = null;
		try {
			if (Objects.isNull(ticketId))
				throw new TclCommonException(ExceptionConstants.TICKETID_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			if (Objects.isNull(requestBody))
				throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			RestResponse response = restClientService.patch(ticketingDetailsUrl + "/" + ticketId,
					Utils.convertObjectToJson(requestBody), getBasicAuth(appId, appSecret, getHeader(), null));
			LOGGER.info("updateTicket response  {}", response);
			if (response.getStatus() == Status.SUCCESS) {
				return (CreateTicketResponse) Utils.convertJsonToObject(response.getData(), CreateTicketResponse.class);
			} else {
				ticketResponse = new CreateTicketResponse();
				ErrorResponseDetails errorDetails = createErrorResponse(response);
				ticketResponse.setStatus(errorDetails.getStatus());
				ticketResponse.setMessage(errorDetails.getMessage());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return ticketResponse;
	}

	/**
	 * @author vivek getBillingQuery used to get the billing query
	 * @param requestBean
	 * @return
	 */
	private Map<String, String> getQueryValues(UpdateRequestBean requestBean) {
		Map<String, String> params = new HashMap<>();

		if (requestBean != null) {
			if (requestBean.getCorrelationId() != null) {
				params.put(TicketingParamConstants.CORRELATIONID, requestBean.getCorrelationId());

			}
			if (requestBean.getCreationDateFrom() != null) {
				params.put(TicketingParamConstants.CREATIONDATEFROM, requestBean.getCreationDateFrom());

			}
			if (requestBean.getCreationDateTo() != null) {
				params.put(TicketingParamConstants.CREATIONDATETO, requestBean.getCreationDateTo());

			}
			if (requestBean.getTicketId() != null) {
				params.put(TicketingParamConstants.TICKETID, requestBean.getTicketId());

			}
			if (requestBean.getImpact() != null) {
				params.put(TicketingParamConstants.IMPACT, requestBean.getImpact());

			}
			if (requestBean.getIssueType() != null) {
				params.put(TicketingParamConstants.ISSUETYPE, requestBean.getIssueType());

			}
			if (requestBean.getLimit() != null) {
				params.put(TicketingParamConstants.LIMIT, requestBean.getLimit());

			}
			if (requestBean.getOffset() != null) {
				params.put(TicketingParamConstants.OFFSET, requestBean.getOffset());

			}
			
			if (requestBean.getOrgId() != null) {
				params.put(TicketingParamConstants.ORGID, requestBean.getOrgId());

			}
			if (requestBean.getServiceAlias() != null) {
				params.put(TicketingParamConstants.SERVICEALIAS, requestBean.getServiceAlias());

			}
			if (requestBean.getServiceIdentifier() != null) {
				params.put(TicketingParamConstants.SERVICEIDENTIFIER, requestBean.getServiceIdentifier());

			}

			if (requestBean.getSortBy() != null) {
				params.put(TicketingParamConstants.SORTBY, requestBean.getSortBy());

			}
			if (requestBean.getSortOrder() != null) {
				params.put(TicketingParamConstants.SORTORDER, requestBean.getSortOrder());

			}
			if(requestBean.getTicketCreatedFrom()!=null) {
				params.put(TicketingParamConstants.TICKETCREATEDFROM, requestBean.getTicketCreatedFrom());
					
				}
			
			if(requestBean.getTicketCreatedTo()!=null) {
				params.put(TicketingParamConstants.TICKETCREATEDTO, requestBean.getTicketCreatedTo());
			}
			
			
			if(requestBean.getServiceType()!=null) {
				params.put(SERVICE_TYPE, requestBean.getServiceType());
	
			}
			
			if(requestBean.getTicketStatus()!=null && !(requestBean.getTicketStatus().equals("All"))) {
				params.put(TICKET_STATUS, requestBean.getTicketStatus());
	
			}

			if(Objects.nonNull(requestBean.getCustomerLeName())) {
				params.put(CUSTOMER_LE_NAME, requestBean.getCustomerLeName());
			}
			

		}

		return params;

	}

	/**
	 * getServiceCatalogAttribute
	 * 
	 * @param productName
	 * @return List<ProductToServiceCatalogBean>
	 * @throws TclCommonException
	 */
	public List<ProductToServiceCatalogBean> getServiceCatalog(String productName) throws TclCommonException {
		List<ProductToServiceCatalogBean> productToServiceCatalogBeanList = new ArrayList<>();
		try {
			List<ProductToServiceCatalog> productToServiceCatalogList = prodToServiceCatalogRepository
					.findByErfPrdcatalogProductName(productName);
			productToServiceCatalogList.stream().forEach(productToServiceCatalog -> {
				ProductToServiceCatalogBean productToServiceCatalogBean = new ProductToServiceCatalogBean(
						productToServiceCatalog);
				productToServiceCatalogBeanList.add(productToServiceCatalogBean);
			});
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.SERVICE_CATALOG_DETAIL_ERROR, ex,
					ResponseResource.R_CODE_ERROR);
		}
		return productToServiceCatalogBeanList;
	}

	/**
	 * getServiceCatalogAttribute
	 * 
	 * @param catalogId
	 * @return List<ServiceCatalogVariablesBean>
	 * @throws TclCommonException
	 */
	public List<ServiceCatalogVariablesBean> getServiceCatalogAttribute(Integer catalogId) throws TclCommonException {
		List<ServiceCatalogVariablesBean> serviceCatalogVariablesList = new ArrayList<>();
		try {
			List<ServiceCatalogToVariablesMap> productToServiceCatalogList = serviceCatalogToVariablesMapRepository
					.findByCatalogId(catalogId);
			productToServiceCatalogList.stream().forEach(productToServiceCatalog -> {
				Optional<MstServiceCatalogVariables> mstServiceCatalogVariables = mstServiceCatalogVariablesRepository
						.findById(productToServiceCatalog.getCatalogVariableId());
				if (mstServiceCatalogVariables.isPresent()) {

					ServiceCatalogVariablesBean mstServiceCatalogVar = new ServiceCatalogVariablesBean(
							mstServiceCatalogVariables.get());
					serviceCatalogVariablesList.add(mstServiceCatalogVar);
				}
			});
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.SERVICE_CATALOG_ATTRIBUTE_ERROR, ex,
					ResponseResource.R_CODE_ERROR);
		}
		return serviceCatalogVariablesList;
	}
	
	private List<GetTicketStatusResponse> getStatusWithCount(TicketingResponse ticketingResponse) {
		List<CreateTicketResponse> tickets = ticketingResponse.getTickets();
		Map<String, Object> tktStatusWithCount = new HashMap<>();
		tickets.stream().forEach(tktResponse -> {
			int count = 1;
			String status = tktResponse.getTicketStatus();
			if(tktStatusWithCount.containsKey(status)) {
				count = ((int) tktStatusWithCount.get(status))+1;
			} 
			tktStatusWithCount.put(status, count);
		});
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
	 * createTicket for Bod operation failure
	 * 
	 * @author archchan
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public List<CreateTicketResponseBean> createBodTicket(CreateTicketRequestBean requestBean) throws TclCommonException {
		List<CreateTicketResponseBean> ticketResponseList = new ArrayList<>();
		try {
			if (Objects.isNull(requestBean))
				throw new TclCommonException(ExceptionConstants.REQUESTBEAN_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			if (requestBean.getServiceIdentifier().isEmpty() || Objects.isNull(requestBean.getCategory())
					|| Objects.isNull(requestBean.getImpact()))
				throw new TclCommonException(ExceptionConstants.CREATE_TICKET_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			CreateTicketRequest createTicketRequest = new CreateTicketRequest();
			constructCreateTicketRequest(requestBean, createTicketRequest);
			createTicketRequest.getServiceIdentifier().forEach(serviceIdentifier -> {
				
				CreateRequestBean request = new CreateRequestBean();
				CreateTicketResponseBean ticketResponse = new CreateTicketResponseBean();
				BeanUtils.copyProperties(createTicketRequest, request);
				request.setServiceIdentifier(serviceIdentifier);
				try {
					RestResponse response;
					LOGGER.info("Requesting ticket creation with request payload {} ",request);
					response = restClientService.postWithClientRepo(ticketingDetailsUrl,
							Utils.convertObjectToJson(request), getBasicAuth(appId, appSecret, getHeader(), requestBean.getUserId()));

					LOGGER.info("createTicket response  {}", response);
					if (response.getStatus() == Status.SUCCESS) {

						ticketResponse = (CreateTicketResponseBean) Utils.convertJsonToObject(response.getData(),
								CreateTicketResponseBean.class);

					} else {
						ErrorResponseDetails errorDetails = createErrorResponse(response);
						ticketResponse.setStatus(errorDetails.getStatus());
						ticketResponse.setMessage(errorDetails.getMessage());
					}
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.CREATE_TICKET_VALIDATION_ERROR,
							ResponseResource.R_CODE_NOT_FOUND);
				}
				LOGGER.info("createTicket ticketResponse  {}", ticketResponse);
				ticketResponseList.add(ticketResponse);
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return ticketResponseList;
	}

	/**
	 * @author archchan
	 * Method to map value from CreateTicketRequestBean to CreateTicketRequest
	 * @param requestBean
	 * @param createTicketRequest
	 */
	private void constructCreateTicketRequest(CreateTicketRequestBean requestBean, CreateTicketRequest createTicketRequest) {
		createTicketRequest.setImpact(requestBean.getImpact());
		createTicketRequest.setCategory(requestBean.getCategory());
		ContactBean contact = new ContactBean();
		contact.setName(requestBean.getContact().getName());
		contact.setEmail(requestBean.getContact().getEmail());
		contact.setPrimaryPhone(requestBean.getContact().getPrimaryPhone());
		createTicketRequest.setContact(contact);
		createTicketRequest.setDescription(requestBean.getDescription());
		createTicketRequest.setServiceIdentifier(requestBean.getServiceIdentifier());
	}
	
	/**
	 * @author kishore To Download CSV - Incidents
	 * @param requestBean
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws CsvRequiredFieldEmptyException
	 * @throws CsvDataTypeMismatchException
	 * 
	 */
	public void getCSVForIncidents(UpdateRequestBean requestBean, HttpServletResponse response)
			throws TclCommonException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		List<IncidentCSVDownload> csvResponse = new ArrayList<>();
		try {
			TicketingResponse ticketingResponse = getDetails(requestBean);
			if (ticketingResponse.getMessage().equalsIgnoreCase(TicketingParamConstants.SUCCESS)
					&& !ticketingResponse.getTickets().isEmpty()) {
				csvResponse = getIncidentCSVDownloadBean(ticketingResponse.getTickets());
				response.reset();
				String filename = TicketingParamConstants.INCIDENT_CSV_FILENAME;
				response.setContentType("text/csv");
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
				CustomMappingStrategy<IncidentCSVDownload> mappingStrategy = new CustomMappingStrategy<>();
				mappingStrategy.setType(IncidentCSVDownload.class);
				StatefulBeanToCsv<IncidentCSVDownload> writer = new StatefulBeanToCsvBuilder<IncidentCSVDownload>(
						response.getWriter()).withMappingStrategy(mappingStrategy).build();
				// write all users to csv file
				writer.write(csvResponse);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		}
	}

	/**
	 * @author kishore To construct response for CSV download
	 * @param createTicketResponse
	 * @return List<IncidentCSVDownload>
	 */
	public List<IncidentCSVDownload> getIncidentCSVDownloadBean(List<CreateTicketResponse> createTicketResponse) {
		List<IncidentCSVDownload> incidentList = new ArrayList<>();
		createTicketResponse.stream().forEach(bean -> {
			IncidentCSVDownload incidentCSVDownload = new IncidentCSVDownload();
			incidentCSVDownload.setTicketId(nullCheckForCsv(bean.getTicketId()));
			if(nullCheckForCsv(bean.getServiceIdentifier()).equals(nullCheckForCsv(bean.getCramerId())))
				incidentCSVDownload.setServiceIdentifier(
					nullCheckForCsv(bean.getServiceIdentifier()) + "\n" + nullCheckForCsv(bean.getServiceType()));
			else
				incidentCSVDownload.setServiceIdentifier(
						nullCheckForCsv(bean.getServiceIdentifier()) + "\n" + nullCheckForCsv(bean.getServiceType())
						+"\n" +nullCheckForCsv(bean.getCramerId()));
			incidentCSVDownload.setCategory(nullCheckForCsv(bean.getCategory()));
			incidentCSVDownload.setImpact(nullCheckForCsv(bean.getImpact()));
			incidentCSVDownload.setServiceAlias(nullCheckForCsv(bean.getServiceAlias()));
			incidentCSVDownload.setTicketStatus(nullCheckForCsv(bean.getTicketStatus()));
			incidentCSVDownload.setCreationDate(nullCheckForCsv(bean.getCreationDate()));
			incidentCSVDownload.setUpdatedDate(nullCheckForCsv(bean.getUpdatedDate()));
			incidentList.add(incidentCSVDownload);
		});
		return incidentList;
	}

	public String nullCheckForCsv(String values) {
		return values == null ? "" : values;
	}
	

	
}
