package com.tcl.dias.ticketing.listener;

import com.tcl.dias.common.beans.CreateTicketRequestBean;
import com.tcl.dias.common.beans.CreateTicketResponseBean;
import com.tcl.dias.common.beans.GdeBodAttributesBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.ticketing.request.CreateTicketRequest;
import com.tcl.dias.ticketing.response.CreateTicketResponse;
import com.tcl.dias.ticketing.service.v1.TicketingService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Listener class to hold Ticket  queues
 * @author archchan
 *
 */
@Service
public class TicketListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(TicketListener.class);
    
	@Autowired
    TicketingService ticketingService;


    /**
     * Consumer method to create ticket in SNOW 
     * @param requestPayload
     * @return
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.ticket.create}") })
    public String createTicket(String requestPayload) throws TclCommonException {
    	LOGGER.info("Entering createTicket with request payload {} ",requestPayload);
    	CreateTicketRequestBean request = Utils.convertJsonToObject(requestPayload, CreateTicketRequestBean.class);
        List<CreateTicketResponseBean> createTicketResponses = ticketingService.createBodTicket(request);
        return Utils.convertObjectToJson(createTicketResponses);
    }


}
