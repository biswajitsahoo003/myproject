package com.tcl.dias.oms.partner.consumer;

import java.util.List;

import com.tcl.dias.common.beans.PartnerTempCustomerDetailsBean;
import com.tcl.dias.oms.entity.entities.PartnerTempCustomerDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.MstProductFamilyBean;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Partner Listner
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class PartnerListner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerListner.class);

    @Autowired
    PartnerService partnerService;

    /**
     * Get All the Applicable products
     *
     * @param requestBody
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.product.detail.queue}")})
    public String getApplicableProducts(String requestBody) throws TclCommonException {
        String response = "";
        try {

            List<MstProductFamilyBean> applicableProducts = partnerService.getApplicableProducts();
            LOGGER.info("Partner Applicable Products Object :: {}", applicableProducts);
            response = GscUtils.toJson(applicableProducts);
            LOGGER.info("Partner Applicable Products:: {}", response);
        } catch (Exception e) {
            LOGGER.error("Error in get all master products {}", e);
        }
        return response;
    }

    /**
     * Get All temp end customers of partner
     *
     * @param request
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.temp.end.customers}")})
    public String getTempEndCustomersByPartnerId(String request) throws TclCommonException {
        String response = "";
        try {
            List<PartnerTempCustomerDetailsBean> tempEndCustomersByPartner = partnerService.getTempEndCustomersByPartner(request);
            LOGGER.info("Number of  temp end customers :: {}", tempEndCustomersByPartner.size());
            response = GscUtils.toJson(tempEndCustomersByPartner);
            LOGGER.info("Temporary End customers response :: {}", response);
        } catch (Exception e) {
            LOGGER.error("Error in fetching temp end customers details {}", e);
        }
        return response;
    }
}
