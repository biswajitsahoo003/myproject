package com.tcl.dias.sfdc.consumer;

import java.util.List;

import com.tcl.dias.common.sfdc.response.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.services.SfdcService;

/**
 * Get Sales Funnel Data for Partner Entity in SFDC
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class SalesFunnelListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesFunnelListener.class);

    @Autowired
    private SfdcService sfdcService;

    /**
     * Get Sales SFDC Data for Partner
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.sales.funnel}")})
    public String getSalesDataForPartner(String request) {
        List<SfdcSalesFunnelResponseBean> salesFunnelResponseBeans;
        String response = "";
        try {
            LOGGER.info("Get Sales Funnel Request input payload : {}",request);
            salesFunnelResponseBeans = sfdcService.getSfdcSalesFunnelData(request);
            LOGGER.info("Get Sales Funnel Response payload : {}", salesFunnelResponseBeans);
           response = Utils.convertObjectToJson(salesFunnelResponseBeans);
        } catch (Exception e) {
            LOGGER.warn("Error in getting Sales Funnel Data", e);
        }
        return response;
    }

    /**
     * Get Deal Registration for Partner
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.deal.registration}")})
    public String getDealRegistrationForPartner(String request) {
        List<DealRegistrationResponseBean> dealRegistrationResponseBeans;
        String response = "";
        try {
            LOGGER.info("Get Sales Funnel Request input payload : {}", request);
            dealRegistrationResponseBeans = sfdcService.getDealRegistrationForPartner(request);
            LOGGER.info("Get Sales Funnel Response payload : {}", dealRegistrationResponseBeans);
           response = Utils.convertObjectToJson(dealRegistrationResponseBeans);
        } catch (Exception e) {
            LOGGER.warn("Error in getting Sales Funnel Data", e);
        }
        return response;
    }

    /**
     * Get Sales SFDC Data for Partner
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.campaign.detail}")})
    public String getCampaignDetailsForPartner(String request) {
        List<SfdcActiveCampaignResponseBean> SfdcActiveCampaignResponseBean;
        String response = "";
        try {
            LOGGER.info("Get Sales Funnel Request input payload : {}",request);
            SfdcActiveCampaignResponseBean = sfdcService.getSfdcCampaignData(request);
            LOGGER.info("Get Sales Funnel Response payload : {}", SfdcActiveCampaignResponseBean);
            response = Utils.convertObjectToJson(SfdcActiveCampaignResponseBean);
        } catch (Exception e) {
            LOGGER.warn("Error in getting Sales Funnel Data", e);
        }
        return response;
    }


    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.create.account.entity.request}")})
    public String createAccountEntityRequest(String request) {
        SfdcAccountEntityCreationResponse sfdcAccountEntityCreationResponse = null;
        String response = "";
        try {
            LOGGER.info("Get Sales Funnel Request input payload : {}",request);
            sfdcAccountEntityCreationResponse = sfdcService.createAccountEntityRequest(request);
            LOGGER.info("Get Sales Funnel Response payload : {}", sfdcAccountEntityCreationResponse);
            response = Utils.convertObjectToJson(sfdcAccountEntityCreationResponse);
        } catch (Exception e) {
            LOGGER.warn("Error in getting Sales Funnel Data", e);
        }
        return response;
    }


    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.update.account.request}")})
    public String updateAccountRequest(String request) {
        AccountUpdationResponse accountUpdationResponse = null;
        String response = "";
        try {
            LOGGER.info("Get Sales Funnel Request input payload : {}",request);
            accountUpdationResponse = sfdcService.updateAccountRequest(request);
            LOGGER.info("Get Sales Funnel Response payload : {}", accountUpdationResponse);
            response = Utils.convertObjectToJson(accountUpdationResponse);
        } catch (Exception e) {
            LOGGER.warn("Error in getting Sales Funnel Data", e);
        }
        return response;
    }
}
