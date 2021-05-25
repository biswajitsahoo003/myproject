package com.tcl.dias.customer.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.AccountManagerRequestBean;
import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerCodeBean;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLeAccountManagerDetails;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeListListBean;
import com.tcl.dias.common.beans.CustomerLeVO;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.GscAddressDetailBean;
import com.tcl.dias.common.beans.LeCcaRequestBean;
import com.tcl.dias.common.beans.LeGstDetailsBean;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.LeStateInfoBean;
import com.tcl.dias.common.beans.LegalEntityBean;
import com.tcl.dias.common.beans.MSABean;
import com.tcl.dias.common.beans.ObjectStorageListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.beans.SignDetailBean;
import com.tcl.dias.common.beans.SiteGstDetail;

import com.tcl.dias.common.beans.SiteLevelAddressBean;

import com.tcl.dias.common.constants.CommonConstants;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.bean.CustomerLeContactDetailsBean;
import com.tcl.dias.customer.bean.LeStateGstBean;
import com.tcl.dias.customer.constants.ExceptionConstants;
import com.tcl.dias.customer.entity.entities.CustomerLeContact;
import com.tcl.dias.customer.dto.CustomerLegalEntityDto;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This file contains the CustomerDetailsConsumer.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class CustomerDetailsConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDetailsConsumer.class);

	@Autowired
	CustomerService customerService;

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.queue}") })
	public String processCustomerDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		CustomerDetailsBean customerDetailsBean = null;
		try {
			String[] customerIds = (responseBody.getPayload()).split(",");

			for (String id : customerIds) {
				customerDetailsBean = customerService.getcustomerDetails(Integer.valueOf(id));
			}
			response = Utils.convertObjectToJson(customerDetailsBean);
			LOGGER.info("Customer details after object to json: {}", response);

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customerleattr.queue}") })
	public String getCustomerLeAttributes(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			CustomerLeDetailsBean omsDetailsBean = customerService.getLeAttributes(Integer.valueOf(responseBody.getPayload()));
			response = Utils.convertObjectToJson(omsDetailsBean);
		} catch (Exception e) {
			LOGGER.error("error in getting customer le details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cca.customer.le.queue}") })
	public String getLeCca(Message<String> request) throws TclCommonException {
		LOGGER.info("getLeCca request {} ", request.getPayload());
		String response = "";
		try {
			LeCcaRequestBean leCcaRequestBean = Utils.convertJsonToObject(request.getPayload(), LeCcaRequestBean.class);
			if (Objects.nonNull(leCcaRequestBean)) {
				leCcaRequestBean = customerService.getLeCCa(leCcaRequestBean);
				response = Utils.convertObjectToJson(leCcaRequestBean);
				LOGGER.info("getLeCCa response {} ", response);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting Le CCA details ", e);
		}
		return response;
	}

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.le.state.queue}") })
	public String processLeState(Message<String> responseBody) throws TclCommonException {
		String response = "";
		List<LeStateGstBean> customerDetailsBean = null;

		LeStateInfoBean leStateInfoBean = new LeStateInfoBean();
		try {
			String[] customerIds = (responseBody.getPayload()).split(",");

			for (String id : customerIds) {
				customerDetailsBean = customerService.getLeStateGst(null, (Integer.valueOf(id)));
				if (customerDetailsBean != null) {
					customerDetailsBean.forEach(bean -> {
						LeStateInfo info = new LeStateInfo();
						BeanUtils.copyProperties(bean, info);
						leStateInfoBean.getLeStateInfos().add(info);
					});
				}
			}
			response = Utils.convertObjectToJson(leStateInfoBean);

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}
		return response;
	}

	/**
	 * processCustomerLeDetails- This method is used to get all the legal Entities
	 * details for the given le ids input
	 * 
	 * @param responseBody
	 * @return String
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customerle.queue}") })
	public String processCustomerLeDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		CustomerLegalEntityDetailsBean customerLeDetailsBean = new CustomerLegalEntityDetailsBean();
		try {
			LOGGER.info("responseBody.getPayload() {}",responseBody.getPayload());
			String[] customerLeIds = (responseBody.getPayload()).split(",");

			customerLeDetailsBean.setCustomerLeDetails(customerService.getCustomerLeDetails(customerLeIds));
			LOGGER.info("customerLeDetailsBean {}",customerLeDetailsBean);
			response = Utils.convertObjectToJson(customerLeDetailsBean);
			LOGGER.info("processCustomerLeDetails::response  {}",response);

		} catch (Exception e) {

			LOGGER.error("error in getting customer details {} ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customerlename.queue}") })
	public String processCustomerLeNameDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			CustomerLegalEntityDetailsBean customerLeDetailsBean = new CustomerLegalEntityDetailsBean();
			String[] customerLeIds = (responseBody.getPayload()).split(",");
			LOGGER.info("customerLeIds :: {}", customerLeIds);

			List<CustomerLeBean> customerLeDetails = customerService.getCustomerLeNameById(customerLeIds);
			customerLeDetailsBean.setCustomerLeDetails(customerLeDetails);
			response = Utils.convertObjectToJson(customerLeDetailsBean);

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.suplierle.queue}") })
	public String processSuplierLeDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			SPDetails supplierDetails = customerService
					.getSupplierDetails(Integer.valueOf(responseBody.getPayload()));

			response = Utils.convertObjectToJson(supplierDetails);

		} catch (Exception e) {

			LOGGER.error("error in getting supplier details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.billing.contact.queue}") })
	public String processBillingContact(Message<String> responseBody) throws TclCommonException {
		String response = "";
		BillingContact billingContact = new BillingContact();
		try {
			billingContact = customerService
					.getBillingContactById(Integer.valueOf(responseBody.getPayload()));
			response = Utils.convertObjectToJson(billingContact);

		} catch (Exception e) {

			LOGGER.error("error in getting billing contact details ", e);
		}
		return response;
	}

	/**
	 * processCustDetails- This method is used to get all customer details based on
	 * the input customer id
	 * 
	 * @param responseBody
	 * @return String
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.details.queue}") })
	public String processCustDetails(Message<String> responseBody) throws TclCommonException , AmqpRejectAndDontRequeueException{
		LOGGER.info("Inside processCustDetails {}",responseBody);
		String response = "";
		CustomerBean customerBean = new CustomerBean();
		try {
			String[] customerIds = (responseBody.getPayload()).split(",");

			customerBean.setCustomerDetailsSet(customerService.getCustDetails(customerIds));
			response = Utils.convertObjectToJson(customerBean);
			LOGGER.info("Customer details after object to json: {}", response);

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	/**
	 * processCustDetails- This method is used to get all customer details based on
	 * the input customer id
	 *
	 * @param responseBody
	 * @return String
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.rtm.details.queue}") })
	public String processCustRTMDetails(Message<String> responseBody) throws AmqpRejectAndDontRequeueException {
		LOGGER.info("processCustRTMDetails: {}", responseBody.getPayload());
		String response = "";
		CustomerBean customerBean = new CustomerBean();
		try {
			String[] customerIds = (responseBody.getPayload()).split(",");

			customerBean.setCustomerDetailsSet(customerService.getRtmCustDetails(customerIds));
			response = Utils.convertObjectToJson(customerBean);

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}


	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.les.queue}") })
	public String processCustomerLeFromCustomer(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			response = Utils.convertObjectToJson(customerService.getCustomerLesByCustomerId(responseBody.getPayload()));

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.le.by.id}") })
	public String processCustomerLeFromLeId(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			String[] customerIds = (responseBody.getPayload()).split(",");

			response = Utils.convertObjectToJson(customerService.getCustomerLeByLeId(customerIds));

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.le.by.customerids}") })
	public String processCustomerLeByCustomerIds(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			String[] customerIds = (responseBody.getPayload()).split(",");
			LOGGER.info("CustomerIds :: {}", customerIds);

			List<Integer> customerLeIdList = new ArrayList<>();
			if (customerIds != null) {
				for (String id : customerIds) {
					String ids = id.replace("\"", "");
					customerLeIdList.add(Integer.valueOf(ids));
				}
			}
			if (!customerLeIdList.isEmpty()) {
				response = Utils.convertObjectToJson(customerService.getLegalEntitiesForTheCustomers(customerLeIdList));
			}

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	/*@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.le.by.customerid}")})
	public String processCustomerLeByCustomerId(String responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("CustomerIds:: {}", responseBody);
			List<Integer> customerLeIdList = new ArrayList<>();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(responseBody);
			Integer requestId = Integer.valueOf(json.get("request").toString());
			customerLeIdList = Arrays.asList(requestId);
			response = Utils.convertObjectToJson(customerService.getLegalEntitiesForTheCustomers(customerLeIdList));
			LOGGER.info("LegalIds:: {}", response);
		} catch (Exception e) {

			LOGGER.error("erroringettingcustomerdetails", e);
		}
		return response;
	}*/

	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.le.by.customerid}")})
	public String getCustomerLeIdByCustomerId(String request) {
		LOGGER.info("CustomerIds:: {}", request);
		String response = "";
		try {
			List<LegalEntityBean> legalEntityBeans = customerService.getLegalEntitiesForTheCustomers(
					Arrays.asList(Integer.valueOf(request)));
			response = Utils.convertObjectToJson(legalEntityBeans);
			LOGGER.info("LegalIds:: {}", response);
		} catch (Exception e) {
			LOGGER.error("error in getting currency for given country", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.le.update.ss}") })
	public String processCustomerLeSS(String responseBody) throws TclCommonException {
		String response = "";
		try {
			ServiceScheduleBean serviceScheduleBean = (ServiceScheduleBean) Utils
					.convertJsonToObject(responseBody, ServiceScheduleBean.class);

			response = Utils.convertObjectToJson(customerService.mapSSDocumentToLe(serviceScheduleBean));

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customerleattr.product.queue}") })
	public String getCustomerLeAttributesBaseOnProduct(String responseBody) throws TclCommonException {
		String response = "";
		try {
			CustomerLeAttributeRequestBean customerLeAttributeRequestBean = (CustomerLeAttributeRequestBean) Utils
					.convertJsonToObject(responseBody,
							CustomerLeAttributeRequestBean.class);
			CustomerLeDetailsBean omsDetailsBean = customerService
					.getLeAttributesBasedOnProduct(customerLeAttributeRequestBean);

			response = Utils.convertObjectToJson(omsDetailsBean);
		} catch (Exception e) {
			LOGGER.error("error in getting customer le details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.le.update.msa}") })
	public String processCustomerLeMSA(String responseBody) throws TclCommonException {
		String response = "";
		try {
			MSABean mSABean = (MSABean) Utils.convertJsonToObject(responseBody,
					MSABean.class);

			response = Utils.convertObjectToJson(customerService.mapMSADocumentToLe(mSABean));

		} catch (Exception e) {
			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.billing.contact.queue}") })
	public String getCustomerBillingContactDetails(String request) {
		String response = "";
		try {
			List<BillingContact> billingContacts = customerService
					.getBillingContact(Integer.valueOf(request));
			response = Utils.convertObjectToJson(billingContacts);
		} catch (Exception e) {
			LOGGER.error("error in getting customer billing contact details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.billing.contact.billingId.queue}") })
	public String getCustomerBillingContactDetailsByBillingId(String request) {
		String response = "";
		try {
			BillingContact billingContacts = customerService
					.getBillingContactByBillingId(Integer.valueOf(request));
			response = Utils.convertObjectToJson(billingContacts);
		} catch (Exception e) {
			LOGGER.error("error in getting customer billing contact details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.billing.account.number}") })
	public String getCustomerBillingAccounts(String request) {
		String response = "";
		try {
			String[] a = (request).split(",");
			List<Integer> b = new ArrayList<>();
			for (String c : a) {
				b.add(Integer.parseInt(c));
			}
			List<String> billingAccounts = customerService.getBillingAccounts(b);
			response = Utils.convertObjectToJson(billingAccounts);
		} catch (Exception e) {
			LOGGER.error("error in getting customer billing contact details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.contact.details.queue}") })
	public String getCustomerLeContactDetails(String request) {
		String response = "";
		try {
			LOGGER.info("String value of request is ----> {} ", request);
			List<CustomerLeContactDetailBean> customerLeContacts = customerService
					.getContactDetailsByCustomerLeId(Integer.valueOf(request));

			response = Utils.convertObjectToJson(customerLeContacts);
		} catch (Exception e) {
			LOGGER.error("error in getting customer LE contact details ", e);
		}
		LOGGER.info("response is for customer le id ---> {} is ---> {} ",request , response );
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.contact.email.queue}") })
	public String getCustomerLeEmailDetails(String request) {
		String response = "";
		try {
			CustomerContactDetails customerLeContact = customerService
					.getContactDetailsForEmailIdConsumer(request);
			response = Utils.convertObjectToJson(customerLeContact);
		} catch (Exception e) {
			LOGGER.error("error in getting customer LE contact details by email ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.get.all.usg}") })
	public String getAllCustomerDetailsWithLe(String request) {
		String response = "";
		try {
			CustomerLeListListBean customerLeListBeans = customerService.getAllCustomerIdsWithLeIdsForUserGroupAll();
			response = Utils.convertObjectToJson(customerLeListBeans);
		} catch (Exception e) {
			LOGGER.error("error in getting customer details for all user group ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customercode.customerlecode.queue}") })
	public String getCustomerCodeCustomerLeCodeBasedonLeId(String request) {
		String response = "";
		try {
			String[] customerLeIdsList = (request).split(",");

			List<Integer> customerLeIdsIntList = Arrays.asList(customerLeIdsList).stream().map(Integer::parseInt)
					.collect(Collectors.toList());
			List<ObjectStorageListenerBean> objListenerBeanList = customerService
					.getCustomerCodeCustomerLeCodeBasedonLeId(customerLeIdsIntList);
			response = Utils.convertObjectToJson(objListenerBeanList);
		} catch (Exception e) {
			LOGGER.error("error in getting customer details for all user group ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${customercode.customerlecode.queue}") })
	public String getCustomerAndCustomerLeCode(String request) {
		String response = "";
		try {
			CustomerCodeBean customerCodeBean = customerService
					.getCustomerAndCustomerLeCode(Integer.valueOf(request));
			response = Utils.convertObjectToJson(customerCodeBean);
		} catch (Exception e) {
			LOGGER.error("error in getting customer details for all user group ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.site.gst.queue}") })
	public String processSiteStateGst(Message<String> responseBody) throws TclCommonException {
		String response = "";

		try {
			SiteGstDetail siteGstDetail = (SiteGstDetail) Utils
					.convertJsonToObject(responseBody.getPayload(), SiteGstDetail.class);
			return Utils.convertObjectToJson(
					customerService.getLeSiteGst(siteGstDetail.getGstNo(), siteGstDetail.getCustomerLeId()));

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.le.account.mananger}") })
	public String processLegalentityFOrAccountMangerDetails(Message<String> responseBody) throws TclCommonException {
		String response = "";

		try {
			CustomerLeAccountManagerDetails customerLeAccountManagerDetails = customerService
					.getCustomerLeAccountManagerDetails(Integer.parseInt(responseBody.getPayload()));
			if (customerLeAccountManagerDetails != null) {
				response = Utils.convertObjectToJson(customerLeAccountManagerDetails);
			}
			return response;

		} catch (Exception e) {

			LOGGER.error("error in getting stae info ", e);
		}
		return response;
	}

	/**
	 * getAccountManagerName- This method is used for get account manager name
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.account.manager}") })
	public String getAccountManagerName(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			response = customerService.getAccountManagerName(responseBody.getPayload());

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response!=null?response:"";
	}

	/**
	 * getAccountRtmName- This method is used for get account manager name
	 *
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.account.rtm}")})
	public String getAccountRtmName(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			response = customerService.getAccountRtmName(responseBody.getPayload());

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response != null ? response : "";
	}

	/**
	 * getCustomerSegmentAttribute- This method is used for get customer attribute
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${cust.get.segment.attribute}") })
	public String getCustomerAttribute(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			response = customerService.getCustomerAttribute(responseBody.getPayload());

		} catch (Exception e) {
			LOGGER.error("error in getting customer details ", e);
		}
		LOGGER.info("Returning response {}", response);
		return response!=null?response:"";
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.detail.queue}") })
	public String getCustomerDetail(String request) {
		String response = "";
		try {
			CustomerDetailBean customerDetailBean = customerService
					.getCustomerDetailsByCustomerId(Integer.valueOf(request));

			response = Utils.convertObjectToJson(customerDetailBean);
		} catch (Exception e) {
			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.details.by.le.queue}") })
	public String getCustomerDetailsByLeId(String request) {
		String response = "";
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			List<Integer> customerLeIds = Utils.fromJson(request,
					new TypeReference<List<Integer>>() {
					});
			response = Utils.convertObjectToJson(customerService.getCustomerDetailsByLeId(customerLeIds));
		} catch (Exception e) {
			LOGGER.error("Error in getting customer details by le id", e);
		}
		return response;
	}

	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.id.by.le.id}") })
	@SuppressWarnings("unchecked")
	public String getCustomerIdByLeId(String request) {
		String response = "";
		try {
			List<Integer> customerLeIds = Utils.convertJsonToObject(request, List.class);
			response = Utils.convertObjectToJson(customerService.getCustomerForLe(customerLeIds));
		} catch (Exception e) {
			LOGGER.error("Error in getting getCustomerIdByLeId", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.partner.id.by.le.id}") })
	@SuppressWarnings("unchecked")
	public String getPartnerIdByLeId(String request) {
		String response = "";
		try {
			List<Integer> customerLeIds = Utils.convertJsonToObject(request, List.class);
			response = Utils.convertObjectToJson(customerService.getPartnerForLe(customerLeIds));
		} catch (Exception e) {
			LOGGER.error("Error in getting getCustomerIdByLeId", e);
		}
		return response;
	}
	
	/**
	 * 
	 * This method return CRN Information for the given LE
	 * 
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.crn.queue}") })
	public String getCRNDetailsByCustomerLegalEntity(String request) {
		String response = "";
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Integer customerLeId = Integer.valueOf(request);
			response = customerService.getCRNDetailsByCustomerLeId(customerLeId);
		} catch (Exception e) {
			LOGGER.error("Error in getting customer details by le id", e);
		}
		return response!=null?response:"";
	}
	
	
	/**
	 * processCustomerLeDetailsForCredit- This method is used to get all the legal
	 * Entities details for the given le ids input
	 * @author MBEDI
	 * @param responseBody
	 * 
	 * @return String
	 * 
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customerle.credit.queue}") })
	public String processCustomerLeDetailsForCreditCheck(Message<String> responseBody) throws TclCommonException {
		String response = null;
		CustomerLeVO customerLeDetailsBean = new CustomerLeVO();
		try {
			String[] CuLeIdAndProductName = (responseBody.getPayload()).split(",");
			String cuid = CuLeIdAndProductName[0]!=null?CuLeIdAndProductName[0]:"";
			String productName = CuLeIdAndProductName[1]!=null?CuLeIdAndProductName[1]:"";
			customerLeDetailsBean= customerService.getCustomerLeDetailsForCreditCheck
								(Integer.valueOf(cuid),productName);

			response = Utils.convertObjectToJson(customerLeDetailsBean);
		
		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response;
	}
	
	/**
	 * getAccountManagerName- This method is used for get account manager Email
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.account.manager.email}") })
	public String getAccountManagerEmail(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			response = customerService.getAccountManagerEmailId(responseBody.getPayload());

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response!=null?response:"";
	}

	/**
	 * 
	 * Get account Manager region based on customer id and user id
	 * 
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.account.manager.region}") })
	public String getAccountManagerRegionByCustomerIdAndUserId(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			AccountManagerRequestBean accountManagerRequestBean = Utils
					.convertJsonToObject(responseBody.getPayload(), AccountManagerRequestBean.class);

			if (accountManagerRequestBean != null && accountManagerRequestBean.getUserId() != null
					&& accountManagerRequestBean.getCustomerId() != null)
				response = customerService.getRegionByCustomerIdAndUserId(accountManagerRequestBean.getCustomerId(),
						accountManagerRequestBean.getUserId());

		} catch (Exception e) {

			LOGGER.error("error in getting customer details ", e);
		}
		return response!=null?response:"";
	}
	
	/**
	 * 
	 * This method return Latest CRN Information for the given LE
	 * 
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.latest.crn.queue}") })
	public String getLatestCRNDetailsByCustomerLegalEntity(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Integer customerLeId = Integer.valueOf(request);
			response = customerService.getLatestCRNDetailsByCustomerLeId(customerLeId);
		} catch (Exception e) {
			LOGGER.error("Error in getting customer details by le id", e);
		}
		return response;
	}
	  
	/**
	 * This method return SECS code associated with Customer LE ID.
	 * 
	 * @param request
	 * 
	 * @return
	 * 
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.secs.queue}") })
	public String getSECSByCustomerLegalEntity(String request) {

		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Integer customerLeId = Integer.valueOf(request);

			response = customerService.getSECSByCustomerLeId(customerLeId);
		} catch (Exception e) {
			LOGGER.error("Error in getting customer details by le id", e);
		}
		return response;

	}

	/**
	 * This method return SECS code associated with Customer LE ID.
	 *
	 * @param request
	 *
	 * @return
	 *
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.le.secs.queue}") })
	public String getSECSByCustomerLegalEntityForGsc(String request) {

		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Integer customerLeId = Integer.valueOf(request);

			response = customerService.getSECSByCustomerLeIdForGsc(customerLeId);
		} catch (Exception e) {
			LOGGER.error("Error in getting customer secs details by le id", e);
		}
		return response;

	}

	/**
	 * Method to return SECS code associated with Customer LE ID by Flag.
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.secs.queue.by.flag}") })
	public String getSECSByCustomerLegalEntityByFlag(String request) {

		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Integer customerLeId = Integer.valueOf(request);

			response = customerService.getSECSByCustomerLeIdByFlag(customerLeId);
		} catch (Exception e) {
			LOGGER.error("Error in getting customer details by le id by flag", e);
		}
		return response;

	}

	/**
	 * Get DOP Email based on orderValue,accountManagerName and customerSegment
	 * @param dopRequest
	 * @return DOP Email
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.dop.matrix.email.queue}")})
	public String getDopEmail(String dopRequest) {
		String response = null;
		try {
			SignDetailBean dopEmailRequest = (SignDetailBean) Utils.convertJsonToObject((String) dopRequest,
					SignDetailBean.class);
			if (Objects.nonNull(dopEmailRequest)) {
				Map<String, String> dopMapper = customerService.getDopEmail(dopEmailRequest.getAccountManagerName(),
						dopEmailRequest.getCustomerSegment(), dopEmailRequest.getOrderValue());
				response = Utils.convertObjectToJson(dopMapper);
			}
		} catch (TclCommonException e) {
			LOGGER.error("Error in getDopEmail ", e);
		}
		return response;
	}
	
	/**
	 * This method returns the list of countries based on the customerleId
	 *
	 * @param request
	 *
	 * @return
	 *
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.le.countries.queue}") })
	public String getCountriesListForTheCustomerLeId(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Integer customerLeId = Integer.valueOf(request);
			response = customerService.getCountriesListForTheCustomerLeId(customerLeId).stream().findFirst().get();
			LOGGER.info(response);
		} catch (Exception e) {
			LOGGER.error("Error in getting customer details by le id", e);
		}
		return response;
	}

	/**
	 * Get Customer Le CUID
	 * @param
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.le.cuid}")})
	public String getCustomerLeCuID(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			if (Objects.nonNull(request)) {
				response = customerService.getCustomerLeCuID(request);
			}
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching bean information ", e);
		}
		return response;
	}
	
	/**
	 * This method return All SECS code associated with Customer LE ID.
	 * 
	 * @param request
	 * 
	 * @return
	 * 
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.all.secs.queue}") })
	public String getAllSECSByCustomerLegalEntity(String request) {

		String response = null;
		List<String> listSecsCode = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Integer customerLeId = Integer.valueOf(request);
			listSecsCode = customerService.getAllSECSByCustomerLeId(customerLeId);
			if (listSecsCode != null)
				response = Utils.convertObjectToJson(listSecsCode);
		} catch (Exception e) {
			LOGGER.error("Error in getting customer details by le id", e);
		}
		return response;

	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.nde.customer.le.by.custid}") })
	public String processCustomerLeFromLeIdNde(String customerId) throws TclCommonException {
		LOGGER.info("entered into customer listener"+customerId);
		String response = "";
		try {
			response = Utils.convertObjectToJson(customerService.getCustomerLeDetailsByCustId(customerId));
			LOGGER.info("response data "+response);
		} catch (Exception e) {

			LOGGER.error("error in getting customer le details ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.contacts.queue}")})
	public String getCustomerContacts(String customerLeId) throws TclCommonException {
		LOGGER.info("Entering into customer contacts listener"+ customerLeId);
		String response="";
		CustomerLeContactDetailsBean cusLeContactDetailsBean= new CustomerLeContactDetailsBean();
		try {
			cusLeContactDetailsBean = customerService.getContactDetailsForTheCustomerLeId(Integer.valueOf(customerLeId));
			
			if(Objects.nonNull(cusLeContactDetailsBean)) {
				response = Utils.convertObjectToJson(cusLeContactDetailsBean);
			}
		} catch(Exception e) {
			LOGGER.error("Error while fetching customer contacts", e);
		}
		LOGGER.info("Response from customer contacts listener"+ response);
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.getOwnerDetailsSfdc.queue}") })
	public String getLeOwnerDetailsSFDC(String customerId) throws TclCommonException {
		//String fetchOwner="No";
		String[] custInfo = customerId.split(",");
		LOGGER.info("Customer id and le id are --> {} ", custInfo[0], custInfo[1]);
		/*LOGGER.info("entered into customer listener for Le Owner Details "+customerId);
		if(customerId.contains("Yes")){
			fetchOwner="Yes";
			customerId=customerId.replace("Yes","");
			LOGGER.info("Yes block replaced value is ----> {} ", customerId);
		}
		if(customerId.contains("No")){
			fetchOwner="No";
			customerId=customerId.replace("No","");
			LOGGER.info("No block replaced value is ----> {} ", customerId);
		}*/
		String response = "";
		try{
			 response=Utils.convertObjectToJson(customerService.getLeOwnerDetails(Integer.valueOf(custInfo[0]), custInfo[1]));
			LOGGER.info("response data for Le Owner for Customer Id---> {} is ---> {} ", customerId,response);
		} catch (Exception e){
			LOGGER.error("Error in get Le Owner Details from Queue", e);
		}
		return response;
	}

	/**
	 * Get Customer Le CUID
	 * @param
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.leid.cuid}")})
	public String getCustomerLeIdByCuID(String request) {
		String response = null;
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			if (Objects.nonNull(request)) {
				response = customerService.getCustomerLeByCuID(request);
			}
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching bean information ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.code.sfdc.queue}") })
	public String getCustomerCodeForSfdc(String responseBody) throws TclCommonException {
		String response = "";
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			Approver customerBean = (Approver) Utils.convertJsonToObject(responseBody, Approver.class);
			if (Objects.nonNull(customerBean)) {
				response = customerService.getCustomerCodeForSfdc(customerBean);
			}
		} catch (Exception e) {
			LOGGER.error("error in getting customer details for sfdcc ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.contact.by.email.queue}") })
	public String getCustomerContactByCommunicationRecipientEmail(String request) throws TclCommonException {
		CustomerContactDetails customerLeContact = null;
		String response = null;
		try {
			customerLeContact = customerService.getCustomerContactByCommunicationRecipient(request);
			return Utils.convertObjectToJson(customerLeContact);
		} catch (Exception e) {
			LOGGER.error("error in getting customer details by email ", e);
		}
		return response;
	}	
	

	/**
	 * This method is used to get gst address details
	 * 
	 * @param responseBody
	 * @return String
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.billing.gst.queue}") })
	public String getBillingAndGstAddress(Message<String> request) throws TclCommonException {
		LOGGER.info("getBillingAndGstAddress request {} ", request.getPayload());
		String response = "";
		try {
			SiteLevelAddressBean siteLevelAddressBean = Utils.convertJsonToObject(request.getPayload(), SiteLevelAddressBean.class);
			if (Objects.nonNull(siteLevelAddressBean)) {
				siteLevelAddressBean = customerService.getBillingAndGstAddress(siteLevelAddressBean);
				response = Utils.convertObjectToJson(siteLevelAddressBean);
				LOGGER.info("getBillingAndGstAddress response {} ", response);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting Gst Address details ", e);
		}
		return response;
	}
	
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.owner.email.business.segment.queue}") })
	public String getOwnerNameBasedOnBusinessSegment(Message<String> responseBody) throws TclCommonException {
		CustomerContactDetails customerLeContact = null;
		String response = null;
		try {
			String[] CustomerIdAndCustomerLeId = (responseBody.getPayload()).split(",");
			Integer customerId = CustomerIdAndCustomerLeId[0]!=null?Integer.valueOf(CustomerIdAndCustomerLeId[0]): null;
			Integer customerLeId = CustomerIdAndCustomerLeId[1]!=null?Integer.valueOf(CustomerIdAndCustomerLeId[1]):null;
			customerLeContact = customerService.getOwnerName(customerId, customerLeId);
			return Utils.convertObjectToJson(customerLeContact);
		} catch (Exception e) {
			LOGGER.error("error in getting customer details by email ", e);

		}
		return response;
	}
	





	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.find.customer.entity.by.customerId.queue}") })
	public String findCustomerEntityByCustomerId(String request) throws TclCommonException {
		String response = "";
		List<CustomerLegalEntityDto> customerLegalEntityDtos = new ArrayList<CustomerLegalEntityDto>();
		try {
			HashMap<String, String> inputMap = (HashMap) Utils.convertJsonToObject(request, HashMap.class);
			Integer customerId = Integer.valueOf(inputMap.get("customerId").toString());
			Integer customerleId = Integer.valueOf(inputMap.get("customerleId").toString());

			customerLegalEntityDtos = customerService.findCustomerEntityByCustomerId(Integer.valueOf(customerId), null);

			Optional<CustomerLegalEntityDto> filteredCustomerLegalEntityDto = customerLegalEntityDtos.stream()
					.filter(customerLegalEntityDto -> customerLegalEntityDto.getLegalEntityId().toString()
							.equalsIgnoreCase(customerleId.toString()))
					.findFirst();
			if (filteredCustomerLegalEntityDto.isPresent()) {
				response = Utils.convertObjectToJson(filteredCustomerLegalEntityDto.get().getLegalEntityName());
			}
		} catch (Exception e) {
			LOGGER.error("Error while fetching customer contacts", e);
		}
		LOGGER.info("Response from customer contacts listener" + response);
		return response;
	}


	
	/**
	 * To fetch supplier le info
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.customer.sple.details.queue}")})
	public String getSupplierLeDetailsByProductName(String request) {
		String response = "";
		try {
			response = Utils.convertObjectToJson(customerService.getSupplierLEDetails(request));
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching supplier le information ", e);
		}
		LOGGER.info("Response:" + response);
		return response;
	}
	
	/**
	 * get list of gst info based on le
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.le.gst.queue}") })
	public String getAllGstList(String request) throws TclCommonException {
		LOGGER.info("Enter into getAllGstList:::::"+request);
		List<LeGstDetailsBean> leGstDetailsBean = null;
		String response = null;
		try {
			if (request != null) {
				leGstDetailsBean = customerService.getAllLeGstInfo(Integer.parseInt(request));
				LOGGER.info("leGst list size"+leGstDetailsBean.size());
				if (!leGstDetailsBean.isEmpty()) {
					response = Utils.convertObjectToJson(leGstDetailsBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting getAllGstList ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.le.gst.no}") })
	public String getGSTNumberByAttributeValue(String request) throws TclCommonException {
		LOGGER.info("Enter into getGSTNumberByAttributeValue::::: {}", request);
		String response = null;
		try {
			if (Objects.nonNull(request)) {
				response = customerService.getGSTNumberByAttributeValue(request);
			}
		} catch (Exception e) {
			LOGGER.error("error in getting getGSTNumberByAttributeValue :: {} ", e);
		}
		return response;
	}
}
