package com.tcl.dias.customer.consumer;

import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.customer.bean.PartnerEndCustomerMappingBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SapCodeRequest;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.constants.ExceptionConstants;
import com.tcl.dias.customer.service.v1.PartnerCustomerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class contains listener method for partner Related Details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class PartnerCustomerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerCustomerListener.class);

    @Autowired
    PartnerCustomerService partnerCustomerService;

    /**
     * Get Partner Legal Entity Details by List of partner le ids
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.partner.legal.entities}")})
    public String getPartnerLegalEntityDetailsByPartnerLeId(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                LOGGER.warn("Partner Legal Entity Queue request is empty");
            }
            List<Integer> partnerLeIds = Utils.fromJson(request, new TypeReference<List<Integer>>() {
            });
            LOGGER.info("Partner Le Ids :: {}", partnerLeIds);
            response = Utils.convertObjectToJson(partnerCustomerService.getPartnerLegalEntities(partnerLeIds));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner legal entity details by partner le id", e);
        }
        return response;
    }

    /**
     * Process Customer Le Name
     *
     * @param request
     * @return
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.name.queue}")})
    public String processCustomerLeNames(String request) throws TclCommonException {
        String response = "";
        CustomerLegalEntityDetailsBean customerLeDetailsBean = new CustomerLegalEntityDetailsBean();
        try {
            LOGGER.info("Queue request received for process :: {}", request);

            String[] req = Utils.fromJson(request, new TypeReference<String[]>() {
            });

            List<CustomerLeBean> customerLeNames = partnerCustomerService.getCustomerLeNames(req[0], req[1]);
            customerLeDetailsBean.setCustomerLeDetails(customerLeNames);
            response = Utils.convertObjectToJson(customerLeDetailsBean);
            LOGGER.info("Queue response received for process :: {}", response);
        } catch (Exception ex) {
            LOGGER.warn("error in getting partner legal entity names for DNB compare", ex);
        }
        return response;
    }

    /**
     * Get Partner Details by partnerID
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.partner.details}")})
    public String getPartnerDetails(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            Integer partnerId = Integer.valueOf(request);
            response = Utils.convertObjectToJson(partnerCustomerService.getPartnerDetails(partnerId));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner details ", e);
        }
        return response;
    }

    /**
     * Get Partner Legal Entity Details by partnerId
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.partner.legal.entity.by.partner}")})
    public String getPartnerLegalEntityDetailsByPartnerId(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            Integer partnerId = Integer.valueOf(request);
            response = Utils.convertObjectToJson(partnerCustomerService.getPartnerLegalEntitiesByPartnerId(partnerId));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner legal entity details by partner id", e);
        }
        return response;
    }

    /**
     * Get Partner Account Name
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.partner.account.name.by.partner}")})
    public String getPartnerAccountName(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            Integer partnerId = Integer.valueOf(request);
            response = partnerCustomerService.getPartnerAccountName(partnerId);
        } catch (Exception e) {
            LOGGER.warn("Error in process partner legal entity details by partner id", e);
        }
        return response!=null?response:"";
    }

    /**
     * Get partner billing accounts
     *
     * @param request
     * @return
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.partner.billing.account.number}") })
    public String getPartnerBillingAccounts(String request) {
        String response = "";
        try {
            String[] a = (request).split(",");
            List<Integer> b = new ArrayList<>();
            for (String c: a){
                b.add(Integer.parseInt(c));
            }
            List<String> billingAccounts = partnerCustomerService
                    .getBillingAccounts(b);
            response = Utils.convertObjectToJson(billingAccounts);
        } catch (Exception e) {
            LOGGER.warn("error in getting partner billing contact details ", e);
        }
        return response;
    }

    /**
     * Get Partner Sap Code Details
     *
     * @param responseBody
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.partner.le.sap.queue}") })
    public String processSapCodeDetails(Message<String> responseBody) throws TclCommonException {
        String response = "";
        try {
            SapCodeRequest req = ((SapCodeRequest) Utils.convertJsonToObject(responseBody.getPayload(), SapCodeRequest.class));
            LeSapCodeResponse resp = partnerCustomerService.getSapCodeBasedOnPartnerLe(req.getCustomerLeIds(),req.getType());

            response = Utils.convertObjectToJson(resp);

        } catch (Exception e) {
            LOGGER.warn("Exception Occured :: {}", e.fillInStackTrace());
        }
        return response;
    }

    @RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.partner.le.secs.queue}") })
    public String processSecsCodeDetails(String request) throws TclCommonException {
        String response = "";
        try {
//            SapCodeRequest req = ((SapCodeRequest) Utils.convertJsonToObject(responseBody.getPayload(), SapCodeRequest.class));
            LOGGER.info("Partner LE ID :: {}", request);
            LeSapCodeResponse leSapCodeResponse = partnerCustomerService.getSecsCodeBasedOnPartnerLe(Integer.valueOf(request));
            response = Utils.convertObjectToJson(leSapCodeResponse);
        } catch (Exception e) {
            LOGGER.warn("Exception Occured :: {}", e.fillInStackTrace());
        }
        return response;
    }


    /**
     * Get Partner Profile by partnerID
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.partner.profile.details}")})
    public String getPartnerProfileByPartnerId(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                LOGGER.warn("Partner Profile Details Queue Request is Empty");
            }
            Integer partnerId = Integer.valueOf(request);
            response = Utils.convertObjectToJson(partnerCustomerService.getPartnerProfileByPartnerId(partnerId));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner profile details ", e);
        }
        return response;
    }

    /**
     * Get Partner Details and LE owner attributes by partnerID
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.partner.details.attributes}")})
    public String getPartnerDetailsWithAttributes(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            Integer partnerId = Integer.valueOf(request);
            response = Utils.convertObjectToJson(partnerCustomerService.getPartnerDetailsWithAttributes(partnerId));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner details and LE owner attributes", e);
        }
        return response;
    }

    /**
     * Get Partner Details By partnerID
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.details.queue}")})
    public String getPartnerDetailsByPartnerIds(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            List<Integer> partnerIds = Utils.fromJson(request, new TypeReference<List<Integer>>() {
            });
            response = Utils.convertObjectToJson(partnerCustomerService.getPartnerDetailsByPartnerIds(partnerIds));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner details and LE owner attributes", e);
        }
        return response;
    }


    /**
     * Get Partner Details by partnerLeID for Notification
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.details.notification.queue}")})
    public String getPartnerDetailsByPartnerIdForNotification(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            String partnerLeId = request;
            response = Utils.convertObjectToJson(partnerCustomerService.getPartnerDetailsForNotification(partnerLeId));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner details and LE owner attributes", e);
        }
        return response;
    }

    /**
     * Get Partner Details by partnerLeID for Notification
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.le.attributes}")})
    public String getPartnerLeAttributes(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            String partnerLeId = request;
            response = Utils.convertObjectToJson(partnerCustomerService.getPartnerLeAttributes(partnerLeId));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner details and LE owner attributes", e);
        }
        return response;
    }

    /**
     * Get partner mapped end customers
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.end.customer}")})
    public String getPartnerEndCustomer(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request)) {
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            }
            Integer partnerId = Integer.valueOf(request);
            response = Utils.convertObjectToJson(partnerCustomerService.getEndCustomerLesByPartnerId(partnerId));
        } catch (Exception e) {
            LOGGER.warn("Error in process partner end customers", e);
        }
        return response;
    }

    /**
     * Get partner customer Details By customerID
     *
     * @param request
     * @return {@link String}
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.customer.details}")})
    public String getCustomerDetailsByCustomerId(String request) {
        String response = "";
        try {
            if (StringUtils.isBlank(request))
                throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
            response = partnerCustomerService.getCustomerDetailsByCustomerId(request);
        } catch (Exception e) {
            LOGGER.warn("Error in process partner details and LE owner attributes", e);
        }
        return response;
    }

    /**
     * Get partner end customer details
     *
     * @param request
     * @throws TclCommonException
     */
    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.partner.endCustomer.mapping}")})
    public String getPartnerEndCustomersDetails(String request){
        String response = "";
        try {
            PartnerEndCustomerMappingBean partnerEndcustomerMappingBean = partnerCustomerService.getPartnerEndCustomerMappingDetails(request);
            response = Utils.convertObjectToJson(partnerEndcustomerMappingBean);
            LOGGER.info("Partner End Customer details response :: {}", response);
        } catch (Exception e) {
            LOGGER.error("Error in fetching temp end customers details {}", e);
        }
        return response;
    }
}
