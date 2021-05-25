package com.tcl.dias.serviceactivation.listener;


import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EorIorDependencyListener {
    private static final Logger logger = LoggerFactory.getLogger(EorIorDependencyListener.class);

    @Autowired
    private CramerService cramerService;

    @RabbitListener(queuesToDeclare = {@Queue("${queue.eorior.dependency}")})
    @Transactional
    public String triggerEorIorDependencyCheck(Message<String> responseBody) throws TclCommonException {
        logger.info("triggerEorIorDependencyCheck invoked");
        try {
            return Utils.convertObjectToJson(cramerService.getEORIORDetails(responseBody.getPayload()));
        } catch (Exception e) {
            logger.error("error in triggerEorIorDependencyCheck", e);
        }
        return "";
    }
}
