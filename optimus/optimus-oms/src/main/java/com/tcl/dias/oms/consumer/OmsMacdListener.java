package com.tcl.dias.oms.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.MacdFlagServiceRequest;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Listener Class for fetching macd initiated details using a queue call
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * @author Thamizhselvi Perumal
 */

@Service
@Transactional
public class OmsMacdListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmsMacdListener.class);



    @Autowired
    MACDUtils macdUtils;

    /**
     * ProcessMacd Information- This listener method is used for accessing macd details
     *
     * @param responseBody
     * @return
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = { @Queue("${oms.macd.queue}") })
    public String processOmsMacdInformation(String responseBody) throws TclCommonException {
        try {
            if (StringUtils.isBlank(responseBody)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }

            MacdFlagServiceRequest macdFlagServiceRequest = (MacdFlagServiceRequest) Utils.convertJsonToObject(responseBody,
                    MacdFlagServiceRequest.class);

            LOGGER.info("serviceIds"+macdFlagServiceRequest.getMacdServiceIds());
            return Utils.convertObjectToJson(macdUtils.getMacdInitiatedStatus(macdFlagServiceRequest.getMacdServiceIds()));
        } catch (Exception e) {
            LOGGER.error("Error in processing oms Macd information ", e);
        }

        return "";
    }
    
    
    /**
     * ProcessMacd Information- This listener method is used for accessing ipc macd details
     *
     * @param responseBody
     * @return
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = { @Queue("${oms.ipc.macd.queue}") })
    public String processOmsIPCMacdInformation(String request) throws TclCommonException {
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            LOGGER.info("Request::"+request);
            String[] serviceIds = (request).split(",");
            List<String> serviceIdList = new ArrayList<>();
            for(String serviceId:serviceIds){
            	LOGGER.info("ServiceId:: {}", serviceId);
            	serviceIdList.add(serviceId);
            }
            //String serviceId = (String) Utils.convertJsonToObject((String) rabbitMqRequestWrapper.getRequest(),String.class);
            LOGGER.info("serviceIds::"+serviceIdList.size());
            return Utils.convertObjectToJson(macdUtils.getIPCMacdInitiatedStatus(serviceIdList));
        } catch (Exception e) {
            LOGGER.error("Error in processing oms Macd information ", e);
        }
        return "";
    }
    
    /**
     * ProcessMacd Information- This listener method is used for accessing o2c macd details
     *
     * @param responseBody
     * @return
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = { @Queue("${oms.o2c.macd.queue}") })
    public void processOmsO2CMacdInformation(String request) throws TclCommonException {
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            LOGGER.info("O2C MACD Request::"+request);
            String[] o2cDetails = (request).split(",");
            String orderCode=o2cDetails[0];
            String dcLocationCode = null;
            if(o2cDetails.length>1) {
            	dcLocationCode = o2cDetails[1];
            }
            macdUtils.updateO2CMACDOrder(orderCode, dcLocationCode);
        } catch (Exception e) {
            LOGGER.error("Error in processing oms Macd information ", e);
        }
    }

    /**
     * processO2CMacdService - This listener method is used for update service stage
     *
     * @param responseBody
     * @return
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.macd.detail.commissioned}") })
    public void processO2CMacdService(String request) throws TclCommonException {
        LOGGER.info("O2C MACD service details request for commissioned : {}",request);
        try {
            if (Objects.isNull(request) || StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            macdUtils.updateO2CMacdService(request);
        } catch (Exception e) {
            LOGGER.error("Error in processing oms macd service commissioned ", e);
        }
    }
}
