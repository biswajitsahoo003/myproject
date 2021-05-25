package com.tcl.dias.oms.partner.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Contains customer details service by user type
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class PartnerCustomerDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerCustomerDetailsService.class);

    @Autowired
    UserInfoUtils userInfoUtils;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${rabbitmq.customer.les.queue}")
    String customerLeByCustomerIdMQ;

    /**
     * Get customer details based on user type
     *
     * @return {@link List <CustomerDetail>}
     * @throws TclCommonException
     */
    public List<CustomerDetail> getCustomerDetailsBasedOnUserType() {
        List<CustomerDetail> customerDetails = new ArrayList<>();

        if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
            List<CustomerDetail> finalCustomerDetails = customerDetails;
            userInfoUtils.getPartnerDetails().forEach(partnerDetail -> {
                String partnerId = String.valueOf(partnerDetail.getPartnerId());
                LOGGER.debug("Partner ID :: {}", partnerId);
                List<Customer> customers = customerRepository.findAllCustomerByPartnerDetails(partnerId);
                LOGGER.debug("customers :: {}", customers);
                customers.forEach(customer -> {
                    Set<Integer> customerLeIds = getCustomerLegalEntityMQ(customer.getErfCusCustomerId());
                    if(customerLeIds.isEmpty()){
                        CustomerDetail customerDetail=constructCustomerDetail(customer, 0);
                        List<CustomerDetail> customerDetailList=new ArrayList<>();
                        customerDetailList.add(customerDetail);
                        finalCustomerDetails.addAll(customerDetailList);
                    }
                    else {
                        finalCustomerDetails.addAll(customerLeIds.stream().map(customerLeId -> constructCustomerDetail(customer, customerLeId)).collect(Collectors.toList()));
                    }
                });
            });
        } else {
            customerDetails = userInfoUtils.getCustomerDetails();
        }
        return customerDetails;
    }
    

    /**
     * Construct customer detail by customer
     *
     * @param customer
     * @param customerLeId
     * @return {@link CustomerDetail}
     */
    private CustomerDetail constructCustomerDetail(Customer customer, Integer customerLeId) {
        CustomerDetail customerDetail = new CustomerDetail();
        customerDetail.setCustomerLeId(customerLeId); // From customer microservice
        customerDetail.setCustomerName(customer.getCustomerName()); // oms table
        customerDetail.setCustomerEmailId(customer.getCustomerEmailId());
        customerDetail.setErfCustomerId(customer.getErfCusCustomerId());
        customerDetail.setCustomerCode(customer.getCustomerCode());
        customerDetail.setCustomerId(customer.getId());
        customerDetail.setStatus(customer.getStatus());
        return customerDetail;
    }

    /**
     * Get Customer Legal entity Ids by partnerId MQ
     *
     * @param customerId
     * @return {@link List}
     * @throws TclCommonException
     */
    private Set<Integer> getCustomerLegalEntityMQ(Integer customerId) {
        Set<Integer> customerLeIds;
        String response;
        try {
            LOGGER.debug("MDC Filter token value in before Queue call get Customer le ids by customer id {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            response = (String) mqUtils.sendAndReceive(customerLeByCustomerIdMQ, Utils.convertObjectToJson(customerId));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_MQ_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        if (Objects.nonNull(response)) {
            customerLeIds = GscUtils.fromJson(response, new TypeReference<Set<Integer>>() {
            });
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
        }
        return customerLeIds;
    }

    public List<CustomerDetail> getOptyCustomerDetailsBasedOnUserType() {
        List<CustomerDetail> customerDetails = new ArrayList<>();

        if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
            List<CustomerDetail> finalCustomerDetails = customerDetails;
            userInfoUtils.getPartnerDetails().forEach(partnerDetail -> {
                String partnerId = String.valueOf(partnerDetail.getPartnerId());
                LOGGER.debug("Partner ID :: {}", partnerId);
                List<String> partnerIds=new ArrayList<>();
                partnerIds.add(partnerId);
                List<Customer> customers = customerRepository.findAllCustomerForOptyByPartnerIds(partnerIds,CommonConstants.Y);
                LOGGER.debug("customers :: {}", customers);
                customers.forEach(customer -> {
                    Set<Integer> customerLeIds = getCustomerLegalEntityMQ(customer.getErfCusCustomerId());
                    if(customerLeIds.isEmpty()){
                        CustomerDetail customerDetail=constructCustomerDetail(customer, 0);
                        List<CustomerDetail> customerDetailList=new ArrayList<>();
                        customerDetailList.add(customerDetail);
                        finalCustomerDetails.addAll(customerDetailList);
                    }
                    else {
                        finalCustomerDetails.addAll(customerLeIds.stream().map(customerLeId -> constructCustomerDetail(customer, customerLeId)).collect(Collectors.toList()));
                    }
                });
            });
        } else {
            customerDetails = userInfoUtils.getCustomerDetails();
        }
        return customerDetails;
    }
}
