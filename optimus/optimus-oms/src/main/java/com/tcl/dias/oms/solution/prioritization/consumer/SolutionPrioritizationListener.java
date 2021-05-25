package com.tcl.dias.oms.solution.prioritization.consumer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.solution.prioritization.service.SolutionPrioritizationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Partner Listner
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class SolutionPrioritizationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolutionPrioritizationListener.class);

    @Autowired
    SolutionPrioritizationService solutionPrioritizationService;

    /**
     * Get All the Applicable products
     *
     * @param requestBody
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = {@Queue("${intl.solution.priotize}")})
    public String getApplicableProducts(String requestBody) throws TclCommonException {
        String response = "";
        try {

        	List<Map<String, Object>> request = Utils.convertJsonToObject(requestBody, List.class);
        	List<Map<String, Object>> responseObj = solutionPrioritizationService.solutionPrioritizeTheInput(request);
        	response=Utils.convertObjectToJson(responseObj);
            LOGGER.info("Partner Applicable Products:: {}", response);
        } catch (Exception e) {
            LOGGER.error("Error in get all master products {}", e);
        }
        return response;
    }
}
