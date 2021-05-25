package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("ssDumpDelegate")
public class SsDumpDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SsDumpDelegate.class);

    @Autowired
    MQUtils mqUtils;

    @Value("${queue.wireless1.ssdump}")
    private String ssDumpQueue;

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("SsDumpDelegate invoked for {} ", execution.getCurrentActivityId());
        String ssDumpResponse = "";
        try {
            Map<String, Object> executionVariables = execution.getVariables();
            String serviceCode = (String) executionVariables.getOrDefault(SERVICE_CODE, "");
            String processInstanceId = execution.getProcessInstanceId() + "#" + serviceCode;
            if (StringUtils.isNotEmpty(serviceCode))
                ssDumpResponse = (String) mqUtils.sendAndReceive(ssDumpQueue, processInstanceId);
            else
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

            if (StringUtils.isNotEmpty(ssDumpResponse))
                execution.setVariable("getSsDumpSuccess", true);
            else
                execution.setVariable("getSsDumpSuccess", false);

        } catch (TclCommonException e) {
            logger.error("Exception occurred in SetPingStabilityDelegate : {}", e);
            execution.setVariable("getSsDumpSuccess", false);
        }
    }


}
