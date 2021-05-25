package com.tcl.dias.serviceactivation.listener;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Component
public class TxDownTimeSyncListener {
    private static final Logger logger = LoggerFactory.getLogger(TxDownTimeSyncListener.class);

    @Autowired
    private CramerService cramerService;

    @RabbitListener(queuesToDeclare = { @Queue("${queue.txdowntimesync}") })
    @Transactional
    public String triggerTxDownTimeSync(Message<String> responseBody) throws TclCommonException {
        logger.info("triggerTxDownTimeSync invoked");
        try {
            return Utils.convertObjectToJson(cramerService.getTxDownTime(responseBody.getPayload()));
        } catch (Exception e) {
            logger.error("error in triggerTxDownTimeSync", e);
        }
        return "";
    }

}
