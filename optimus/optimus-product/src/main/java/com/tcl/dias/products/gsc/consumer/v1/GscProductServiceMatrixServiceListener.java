package com.tcl.dias.products.gsc.consumer.v1;

import com.tcl.dias.common.beans.ExpectedDeliveryDateBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.gsc.beans.GscProductLocationBean;
import com.tcl.dias.products.gsc.service.v1.GscProductServiceMatrixService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service contains listeners related to GscProductServiceMatrixService
 *
 * @author VISHESH AWASTHI
 */
@Service
public class GscProductServiceMatrixServiceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscProductServiceMatrixServiceListener.class);

    private GscProductServiceMatrixService gscProductServiceMatrixService;

    public GscProductServiceMatrixServiceListener(GscProductServiceMatrixService gscProductServiceMatrixService) {
        this.gscProductServiceMatrixService = gscProductServiceMatrixService;
    }

    /**
     * @param request
     * @return rfsDate(String)
     * @author VISHESH AWASTHI
     */
    @RabbitListener(queuesToDeclare = {@Queue("${expected.delivery.date.queue}")})
    public String estimateDeliveryDate(String request) {
        String expectedDeliveryDate = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            LOGGER.info("Input received for Delivery Date For Service {}", request);
            ExpectedDeliveryDateBean expectedDeliveryDateBean = (ExpectedDeliveryDateBean) Utils.convertJsonToObject(
                    request, ExpectedDeliveryDateBean.class);
            expectedDeliveryDate = gscProductServiceMatrixService.getGscExpectedDateForDelivery(
                    expectedDeliveryDateBean.getService(), expectedDeliveryDateBean.getAccessType(),
                    expectedDeliveryDateBean.getCountry());
        } catch (Exception e) {
            LOGGER.warn("Error in estimaiting the delivery Date for service ", e);
        }
        LOGGER.info("Estimated delivery Date for service :" + expectedDeliveryDate);
        return expectedDeliveryDate != null ? expectedDeliveryDate : "";
    }

    @RabbitListener(queuesToDeclare = {@Queue("${gsc.product.country.city.queue}")})
    public String getCities(Message<String> request) {
        String response = "";
        try {
            GscProductLocationBean gscProductLocationBean = new GscProductLocationBean();
            String requestData = request.getPayload();
            LOGGER.info("Input received for get cities ::  {}", requestData);
            gscProductLocationBean = gscProductServiceMatrixService.getCities(requestData.split(":")[0], requestData.split(":")[1]);
            response = Utils.convertObjectToJson(gscProductLocationBean);
        } catch (Exception e) {
            LOGGER.error("Error in getting cities :: {} ", ExceptionUtils.getStackTrace(e));
        }
        return response;
    }
}
