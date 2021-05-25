package com.tcl.dias.ticketing.service.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.ticketing.request.PlannedEventsRequest;
import com.tcl.dias.ticketing.response.ErrorResponseDetails;
import com.tcl.dias.ticketing.response.PlannedEventsResponse;
import com.tcl.dias.ticketing.service.category.service.v1.TicketingAbstractService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PlannedEventsService extends TicketingAbstractService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlannedEventsService.class);

	@Value("${plannedevents.details.url}")
	String plannedEventsDetailssUrl;

	@Value("${app.id}")
	String appId;

	@Value("${secret.key}")
	String appSecret;

	@Autowired
	RestClientService restClientService;

	/**
	 * @author ANNE NISHA to filter the ticket details based on some queries
	 * 
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public PlannedEventsResponse getTicketDetails(PlannedEventsRequest plannedEventsRequest) throws TclCommonException {
		PlannedEventsResponse ticketResponse = null;
		ObjectMapper om = new ObjectMapper();
		Map<String, String> req = om.convertValue(plannedEventsRequest, Map.class);
		RestResponse response = restClientService.getWithQueryParam(plannedEventsDetailssUrl, req,
				getBasicAuth(appId, appSecret, getHeader(), null));
		if (response.getStatus() == Status.SUCCESS) {
			return (PlannedEventsResponse) Utils.convertJsonToObject(response.getData(), PlannedEventsResponse.class);
		} else {
			ticketResponse = new PlannedEventsResponse();
			ErrorResponseDetails errorDetails = createErrorResponse(response);
			ticketResponse.setStatus(errorDetails.getStatus());
			ticketResponse.setMessage(errorDetails.getMessage());
		}
		LOGGER.info("getTicketDetails response  {}", response);

		return ticketResponse;
	}

}
