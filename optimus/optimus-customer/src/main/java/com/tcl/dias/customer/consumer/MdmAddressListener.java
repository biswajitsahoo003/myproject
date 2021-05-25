package com.tcl.dias.customer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.MDMOmsResponseBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.service.v1.CustomerService;

/**
 * Add ContactId to CustomerBilling Info 
 * @link http://www.tatacommunications.com
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class MdmAddressListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MdmAddressListener.class);

    @Autowired
    CustomerService customerService;
    
    /**
     * 
     * getCurrencyByCountry function is used to fetch currency by country
     * @param request
     * @return
     */
    @RabbitListener(queuesToDeclare = {@Queue("${mdm.address.contactid.add}")})
    public void getCurrencyByCountry(String request) {
        try {
            MDMOmsResponseBean mDMResponseBean = (MDMOmsResponseBean) Utils
					.convertJsonToObject(request, MDMOmsResponseBean.class);
			
                  customerService.addContactId(mDMResponseBean);
            } catch (Exception e) {
            LOGGER.error("error in getting currency for given country", e);
        }
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
            LOGGER.error("error in getting currency for given country", e);
        }
        return response!=null?response:"";
    }
}
