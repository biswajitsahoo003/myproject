package com.tcl.dias.oms.consumer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TaskDetailBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * Oms Quote Listener
 *
 * @author Harini Sri Reka J
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Service
@Transactional
public class OmsTaskListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmsTaskListener.class);

    @Autowired
    IllQuoteService illQuoteService;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;
    /**
     * getting assignee based on task.
     * @param customerId
     * @throws TclCommonException
     */

    @RabbitListener(queuesToDeclare = {@Queue("${cmd.task.queue}")})
    @Transactional
    public void getTaskData(String responseBody) {
        try {
            LOGGER.info("Listener for getting task next assignee details");
            try {
                if (StringUtils.isBlank(responseBody)) {
                    throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
                }
                TaskDetailBean taskDetailBean = (TaskDetailBean) Utils
                        .convertJsonToObject(responseBody, TaskDetailBean.class);
                LOGGER.info("Preparing to set Quote status for QuoteId:" +taskDetailBean.getQuoteId() + "with assignee: " +taskDetailBean.getAssignee());
                List<QuoteToLe> quoteToLe=quoteToLeRepository.findByQuote_Id(taskDetailBean.getQuoteId());
                QuoteToLe quoteToLeOpt=quoteToLe.get(0);
				
				if (!taskDetailBean.getAssignee().equalsIgnoreCase("Commercial updated")) {
					quoteToLeOpt.setCommercialStatus("Pending with " + taskDetailBean.getAssignee());
				}
				  else {
					  quoteToLeOpt.setCommercialStatus(taskDetailBean.getAssignee());
					  }
				 
				quoteToLeRepository.save(quoteToLeOpt);
            } catch (TclCommonException e) {
                LOGGER.error("Error in fetching quote information ", e);
            }
        }catch (Exception e){
            LOGGER.error("Error in fetching task information ", e);
        }
    }
}
