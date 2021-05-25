package com.tcl.dias.products.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.gsc.service.v1.GscProductServiceMatrixService;
import com.tcl.dias.products.service.v1.ProductsService;

/**
 * Get Product Location Details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscProductLocationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscProductLocationListener.class);

    @Autowired
    ProductsService productsService;

    @Autowired
    GscProductServiceMatrixService gscProductService;

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.product.location.queue}")})
    public String getProductLocation(String request) {
        String response = "";
        try {
            String[] spliter = (request).split(",");
            String productName = spliter[0];
            String serviceName = spliter[1];
            String country = spliter[2];
            response = Utils.convertObjectToJson(productsService
                    .getProductLocationsForService(productName, serviceName, country));
        } catch (Exception e) {
            LOGGER.error("error in getting currency for given country", e);
        }
        return response;
    }

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.product.city.location.queue}")})
    public String getLNSCitiesByCode(String request) {
        String response = "";
        try {
            response = Utils.convertObjectToJson(gscProductService.getLNSCitiesByCode());
        } catch (Exception e) {
            LOGGER.error("error in getting lns cities", e);
        }
        return response;
    }

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.product.acans.city.location.queue}")})
    public String getACANSCitiesByCode(String request) {
        String response = "";
        try {
            response = Utils.convertObjectToJson(gscProductService.getACANSCitiesByCode());
        } catch (Exception e) {
            LOGGER.error("error in getting lns cities", e);
        }
        return response;
    }

}
