package com.tcl.dias.oms.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.SIInfoSearchBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.dto.QuoteDto;
import com.tcl.dias.oms.dto.QuoteToLeDto;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gvpn.termination.service.v1.GvpnTerminationService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.ill.termination.service.v1.IllTerminationService;
import com.tcl.dias.oms.npl.termination.service.v1.NplTerminationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * Oms Quote Listener
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class OmsSalesTaskResponseListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSalesTaskResponseListener.class);
	
	@Autowired
	GvpnTerminationService gvpnTerminationService;
	
	@Autowired
	IllTerminationService illTerminationService;
	
	@Autowired
	NplTerminationService nplTerminationService;
	/**
	 * getting Termination negotiation response.
	 * @throws TclCommonException
	 */
		
	@RabbitListener(queuesToDeclare = { @Queue("${termination.l20.queue}") })
	public String getSalesTaskResponseDetails(String request) {
		String response = "";
	    try {
			LOGGER.info("Enter getOpportunity TerminationNegotiationResponse "+request);
			TerminationNegotiationResponse terminationNegotiationResponse = Utils.convertJsonToObject(request, TerminationNegotiationResponse.class);
			String quoteCode = terminationNegotiationResponse.getOrderCode();
			if(quoteCode.startsWith("GVPN")) {
			  response = gvpnTerminationService.gvpnPersistTerminationNegotiationResponse(terminationNegotiationResponse);
			}else if(quoteCode.startsWith("IAS")) {
			  response = illTerminationService.illPersistTerminationNegotiationResponse(terminationNegotiationResponse);
			}else if(quoteCode.startsWith("NPL") || quoteCode.startsWith("NDE")) {
			  response = nplTerminationService.nplPersistTerminationNegotiationResponse(terminationNegotiationResponse);
			}		
		} catch (Exception e) {
		 	LOGGER.error("Error in fetching TerminationNegotiationResponse information ", e);
	   }
		return response;
	}
	
}
