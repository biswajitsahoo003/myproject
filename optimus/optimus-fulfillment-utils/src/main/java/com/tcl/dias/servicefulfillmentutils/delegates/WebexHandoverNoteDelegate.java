package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.WebexPdfService;

/**
 * @author Ankit 
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Component("webexHandoverNoteDelegate")
public class WebexHandoverNoteDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(WebexHandoverNoteDelegate.class);
	
	@Autowired
    private WebexPdfService webexPdfService;

	@Override
    public void execute(DelegateExecution execution) {

        try {
            logger.info("Inside Handover Delegate");
            Map<String, Object> processMap = execution.getVariables();
            String serviceCode = (String) processMap.get(SERVICE_CODE);
            Integer serviceId = (Integer) processMap.get(SERVICE_ID);

            webexPdfService.processServiceAcceptancePdf(serviceCode,serviceId);

        } catch (Exception e) {
            logger.error("Exception in Handover Note {}",e);
        }

    }
}
