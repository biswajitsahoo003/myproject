package com.tcl.dias.customer.consumer;

import com.tcl.dias.customer.service.v1.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Get Currency Details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class CurrencyListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyListener.class);

    @Autowired
    CustomerService customerService;

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.country.currency.queue}")})
    public String getCurrencyByCountry(String request) {
        String response = "";
        try {
            response = customerService.getCurrencyDetails(request);
        } catch (Exception e) {
            LOGGER.error("error in getting currency for given country", e);
        }
        return response;
    }

    /**
     * Get Supplier Legal Currency By ID
     *
     * @param request
     * @return
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.supplier.currency.queue}")})
    public String getCurrencyBySupplierLegalId(String request) {
        String response = "";
        try {
            response = customerService.getCurrencyBySupplierLegalId(Integer.valueOf(request));
        } catch (Exception e) {
            LOGGER.error("error in getting currency for given supplier", e);
        }
        return response!=null?response:"";
    }

    /**
     * Get Customer Legal Currency By ID
     *
     * @param request
     * @return
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.currency.queue}")})
    public String getCurrencyByCustomerLegalId(String request) {
        String response = "";
        try {
            response = customerService.getCurrencyByCustomerLegalId(Integer.valueOf(request));
        } catch (Exception e) {
            LOGGER.error("error in getting currency for given customer", e);
        }
        return response!=null?response:"";
    }

    /**
     * Get Supplier Legal Currency By ID
     *
     * @param request
     * @return
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.currency.queue.id}")})
    public String getCurrencyById(String request) {
        String response = "";
        try {
            response = customerService.getCurrencyById(Integer.valueOf(request));
        } catch (Exception e) {
            LOGGER.error("error in getting currency for given id", e);
        }
        return response != null ? response : "";
    }

    /**
     * Get Supplier Legal Currency By ID
     *
     * @param request
     * @return
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.country.currency.by.customer.le.queue}")})
    public String getCountryCurrencyByCustomerLe(String request) {
        String response = "";
        try {
            response = customerService.getCountryCurrencyByCustomerLe(request);
        } catch (Exception e) {
            LOGGER.error("error in getting country and currency for given id", e);
        }
        return response != null ? response : "";
    }

}
