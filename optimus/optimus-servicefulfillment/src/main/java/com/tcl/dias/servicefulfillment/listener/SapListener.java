package com.tcl.dias.servicefulfillment.listener;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.sap.GrnResponses;
import com.tcl.dias.servicefulfillmentutils.beans.sap.GrnSapHanaResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MinResponseBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.PoStatusResponseBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class SapListener {

    @Autowired
    SapService sapService;

    public static final Logger LOGGER = LoggerFactory.getLogger(SapListener.class);

    @RabbitListener(queuesToDeclare = {@Queue("${mq.sap.minstatus.queue}")})
    public void processMinResponse(Message<String> response) throws TclCommonException {
        LOGGER.info(String.format("ProcessMinResponse, Input received : %s", response.getPayload()));
        MinResponseBean minResponseBean = Utils.convertJsonToObject(response.getPayload(), MinResponseBean.class);
        if (LOGGER.isDebugEnabled())
            LOGGER.info(String.format("%s", minResponseBean));
        sapService.processMinResponse(minResponseBean);
    }

    @RabbitListener(queuesToDeclare = {@Queue("${mq.sap.postatus.queue}")})
    public void processPoStatusResponse(Message<String> response) throws TclCommonException {
        LOGGER.info(String.format("ProcessPoStatusResponse, Input received : %s", response.getPayload()));
        PoStatusResponseBean poStatusBean = Utils.convertJsonToObject(response.getPayload(), PoStatusResponseBean.class);
        if (LOGGER.isDebugEnabled())
            LOGGER.info(String.format("%s", poStatusBean));
        sapService.processPoStatusResponse(poStatusBean);
    }

    @RabbitListener(queuesToDeclare = {@Queue("${mq.sap.grnstatus.queue}")})
    public void processGrnStatusResponse(Message<String> response) throws TclCommonException {
        LOGGER.info(String.format("ProcessGRNStatusResponse, Input received : %s", response.getPayload()));
        GrnSapHanaResponse grnStatusBean = Utils.convertJsonToObject(response.getPayload(), GrnSapHanaResponse.class);
        if (LOGGER.isDebugEnabled())
            LOGGER.info(String.format("%s", grnStatusBean));
        sapService.processGrnStatusResponse(grnStatusBean);
    }
}
