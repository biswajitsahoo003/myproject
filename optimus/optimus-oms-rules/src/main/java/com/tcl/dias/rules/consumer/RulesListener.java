package com.tcl.dias.rules.consumer;

import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesRequest;
import com.tcl.dias.common.utils.Utils;

import com.tcl.dias.rules.service.CommonRulesService;
import com.tcl.dias.rules.service.TeamsDRRulesService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


/**
 * Listener class for rules
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class RulesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RulesListener.class);

    @Autowired
    TeamsDRRulesService teamsDRRulesService;

    @Autowired
    CommonRulesService commonRulesService;

    /**
     * Listener to fire all common rules
     *
     * @param responseBody
     * @return
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.rules.common}") })
    public String commonRules(Message<String> responseBody) throws TclCommonException {
        String response = "";
        try {
            LOGGER.info("Input received for rules engine :: {}", responseBody.getPayload());
            TeamsDRRulesRequest request = (TeamsDRRulesRequest) Utils.convertJsonToObject(responseBody.getPayload(),
                    TeamsDRRulesRequest.class);
            System.out.println("Calling common rules");
            commonRulesService.executeRules(request);
        } catch (Exception e) {
            LOGGER.error("Error in rules engine", e);
        }
        return response;
    }

    /**
     * Listener to fire all teams dr rules.
     * @param responseBody
     * @return
     * @throws TclCommonException
     */
   @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.rules.teamsdr.segregate}") })
    public String segregateBasedOnProducts(Message<String> responseBody) throws TclCommonException {
        String response = "";
       try {
           LOGGER.info("Input received for rules engine :: {}", responseBody.getPayload());
           TeamsDRRulesRequest request = (TeamsDRRulesRequest) Utils.convertJsonToObject(responseBody.getPayload(),
                   TeamsDRRulesRequest.class);
           response = Utils.convertObjectToJson(teamsDRRulesService.segregate(request));
           LOGGER.info("The response sent from queue :: {}",response);
       } catch (Exception e) {
           LOGGER.error("Error in rules engine", e);
       }
        return response;
    }
}
