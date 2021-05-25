package com.tcl.dias.serviceactivation.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.beans.IsValidBtsSyncBean;
import com.tcl.dias.serviceactivation.service.ActivationService;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class ValidateBtsSyncListener {
    private static final Logger logger = LoggerFactory.getLogger(ValidateBtsSyncListener.class);

    @Autowired
    private CramerService cramerService;
    
    @Autowired
    private ActivationService activationService;

    @RabbitListener(queuesToDeclare = {@Queue("${queue.validatebtssync}")})
    public String validateBtsSyncListener(Message<String> responseBody) throws TclCommonException {
        logger.info("validateBtsSyncListener invoked");
        try {
        	IsValidBtsSyncBean isValidBtsSyncBean = (IsValidBtsSyncBean) Utils
    				.convertJsonToObject(responseBody.getPayload(), IsValidBtsSyncBean.class);
        	activationService.updateSsIpDetails(isValidBtsSyncBean.getServiceId(), isValidBtsSyncBean.getBtsIP(),isValidBtsSyncBean.getHsuIP());
        } catch (Exception e) {
            logger.error("Error in updating ssIp",e);
        }
        try {
            return Utils.convertObjectToJson(cramerService.isValidBTSSync(responseBody.getPayload()));
        } catch (Exception e) {
            logger.error("Error in bts",e);
        }
        return "";
    }
}
