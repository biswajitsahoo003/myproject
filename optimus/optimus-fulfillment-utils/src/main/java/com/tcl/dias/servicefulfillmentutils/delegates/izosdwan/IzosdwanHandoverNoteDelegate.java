package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.PdfService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Component("sdwanHandoverNoteDelegate")
public class IzosdwanHandoverNoteDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanHandoverNoteDelegate.class);

	@Autowired
	private PdfService pdfService;

	@Override
	public void execute(DelegateExecution execution) {

		try {
			logger.info("Inside Sdwan Handover Delegate");
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);

			pdfService.processSdwanServiceAcceptancePdf(serviceCode,serviceId);

		} catch (Exception e) {
			logger.error("Exception in Sdwan Handover Note {}",e);
		}

	}

}
