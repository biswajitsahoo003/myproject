package com.tcl.dias.oms.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Supplier Legal Entity Listener
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class SupplierLeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierLeListener.class);

    @Autowired
    GscQuoteService gscQuoteService;


    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.notification.mail.supplier.queue}")})
    public void processSupplierMisMatchEmailNotification(String responseBody) {
        try {
            if (StringUtils.isBlank(responseBody)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }

            
            String quoteLeId = responseBody;

            if (StringUtils.isBlank(quoteLeId)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }

            gscQuoteService.triggerMailNotificationSupplierLeMisMatch(Integer.parseInt(quoteLeId));

        } catch (Exception e) {
            LOGGER.error("Error in process supplier legal entity queue information ", e);
        }
    }

}
