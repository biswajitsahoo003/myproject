package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscPdfService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

@Component("sipHandoverNoteDelegate")
public class SipHandoverNoteDelegate implements JavaDelegate{
	private static final Logger logger = LoggerFactory.getLogger(SipHandoverNoteDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;  
	
	@Autowired
    private GscPdfService gscPdfService;
	
	@Override
    public void execute(DelegateExecution execution) {
		logger.info("SipHandoverNoteDelegate invoked for {}", execution.getCurrentActivityId());
        try {
            workFlowService.processServiceTask(execution);
            Map<String, Object> processMap = execution.getVariables();
            String serviceCode = (String) processMap.get(SERVICE_CODE);
            Integer serviceId = (Integer) processMap.get(SERVICE_ID);
            gscPdfService.processSipHandoverPdf(serviceCode, serviceId, execution.getCurrentActivityId());
			workFlowService.processServiceTaskCompletion(execution);
        } catch (Exception e) {
            logger.error("Exception in SipHandoverNoteDelegate {}",e);
        }
    }

}
