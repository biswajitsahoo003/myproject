package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscPdfService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component("gscHandoverNoteDelegate")
public class GscHandoverNoteDelegate implements JavaDelegate {
private static final Logger logger = LoggerFactory.getLogger(GscHandoverNoteDelegate.class);
	
	@Autowired
    private GscPdfService gscPdfService;

	@Override
    public void execute(DelegateExecution execution) {
        try {
            logger.info("Inside GSC Handover Note Delegate");
            Map<String, Object> processMap = execution.getVariables();
            String serviceCode = (String) processMap.get(SERVICE_CODE);
            Integer serviceId = (Integer) processMap.get(SERVICE_ID);
            Integer gscFlowGroupId = (Integer) processMap.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
			gscPdfService.processServiceAcceptancePdf(serviceCode, serviceId, gscFlowGroupId);
        } catch (Exception e) {
            logger.error("Exception in GSC Handover Note Delegate {}",e);
        }
    }
}
