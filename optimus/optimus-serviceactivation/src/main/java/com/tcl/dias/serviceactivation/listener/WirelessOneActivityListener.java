package com.tcl.dias.serviceactivation.listener;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.service.ActivationService;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.GetPingStabilityResponse;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WirelessOneActivityListener {

    public static final String REGEX = "#";
    private static final Logger logger = LoggerFactory.getLogger(WirelessOneActivityListener.class);
    @Autowired
    ActivationService activationService;

    @RabbitListener(queuesToDeclare = {@Queue("${queue.wireless1.setpingstability}")})
    public String setPingStabilityListener(Message<String> responseBody) {
        String payload = responseBody.getPayload();
        logger.info("setPingStabilityListener invoked: {}", payload);
        try {
            return activationService.setPingStability(payload);
        } catch (Exception e) {
            logger.error("Error in bts", e);
        }
        return "";
    }

    @RabbitListener(queuesToDeclare = {@Queue("${queue.wireless1.ssdump}")})
    public String getSsDumpListener(Message<String> responseBody) {
        logger.info("getSsDumpListener invoked for response : {}", responseBody.getPayload());
        try {
            SSDumpResponseBean ssDumpDetails = activationService.getSSDumpDetails(responseBody.getPayload());
            if (Objects.nonNull(ssDumpDetails))
                return Utils.convertObjectToJson(ssDumpDetails);
        } catch (Exception e) {
            logger.error("Error in bts", e);
        }
        return "";
    }

    @RabbitListener(queuesToDeclare = {@Queue("${queue.wireless1.getpingtestdetails}")})
    public String getPingTestDetailListener(Message<String> responseBody) {
        logger.info("getPingTestDetailListener invoked for response : {}", responseBody.getPayload());
        try {
            GetPingStabilityResponse pingTestResult = activationService.getPingTestResult(responseBody.getPayload());
            if (Objects.nonNull(pingTestResult))
                return Utils.convertObjectToJson(pingTestResult);
        } catch (Exception e) {
            logger.error("Error in bts", e);
        }
        return "";
    }
}
