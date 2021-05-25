package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.Map;
import java.util.Objects;

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
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.GetPingStabilityResponse;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("getPingTestDetailDelegate")
public class GetPingTestDetailDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SetPingStabilityDelegate.class);

    @Autowired
    MQUtils mqUtils;

    @Value("${queue.wireless1.getpingtestdetails}")
    private String pingTestQueue;

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("GetPingTestDetailDelegate invoked for {} ", execution.getCurrentActivityId());
        String pingTestResult = "";
        try {
            Map<String, Object> executionVariables = execution.getVariables();
            String serviceCode = (String) executionVariables.getOrDefault(SERVICE_CODE, "");
            String processInstanceId = execution.getProcessInstanceId() + "#" + serviceCode;

            if (StringUtils.isNotEmpty(serviceCode)) {
                pingTestResult = (String) mqUtils.sendAndReceive(pingTestQueue, processInstanceId);
                if (StringUtils.isNotEmpty(pingTestResult)) {
                    GetPingStabilityResponse getPingStabilityResponse = Utils.convertJsonToObject(pingTestResult, GetPingStabilityResponse.class);
                    if (Objects.nonNull(getPingStabilityResponse.getGetPingStability()))
                        execution.setVariable("getPingTestDetailsSuccess", true);
                }
                execution.setVariable("getPingTestDetailsSuccess", false);
            } else
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

        } catch (TclCommonException e) {
            logger.error("Exception occurred in SetPingStabilityDelegate : {}", e);
            execution.setVariable("getPingTestDetailsSuccess", false);
        }
    }
}
