package com.tcl.dias.servicefulfillmentutils.delegates.teamsdr;

import com.tcl.dias.servicefulfillmentutils.service.v1.TeamsDRHandoverPdfService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.*;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Component("teamsDRHandoverNoteDelegate")
public class TeamsDRHandoverNoteDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(TeamsDRHandoverNoteDelegate.class);

	@Autowired
	private TeamsDRHandoverPdfService pdfService;

	@Override
	public void execute(DelegateExecution execution) {

		try {
			logger.info("Inside TeamsDRHandoverNoteDelegate");
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);

			Integer scComponentId = null;
			if(processMap.containsKey(SC_COMPONENT_ID)){
				scComponentId = (Integer) processMap.get(SC_COMPONENT_ID);
			}

			Integer batchId = null;
			if(processMap.containsKey(GSC_FLOW_GROUP_ID)){
				batchId = (Integer) processMap.get(GSC_FLOW_GROUP_ID);
			}

			pdfService.processHandoverNotePdf(serviceCode,serviceId,scComponentId,batchId);

		} catch (Exception e) {
			logger.error("Exception in Handover Note {}",e);
		}

	}

}
