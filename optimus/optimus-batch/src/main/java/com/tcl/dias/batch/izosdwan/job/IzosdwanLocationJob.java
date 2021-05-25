package com.tcl.dias.batch.izosdwan.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.apache.commons.lang.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.ByonBulkUploadDetail;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanByonUploadDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;

/**
 * 
 * This is the job class for IZOSDWAN to persist location details
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class IzosdwanLocationJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanLocationJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
			MQUtils mqUtils = appCtx.getBean(MQUtils.class);
			QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository = appCtx
					.getBean(QuoteIzosdwanByonUploadDetailRepository.class);
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository
					.findByStatus(IzosdwanCommonConstants.OPEN);		
			
			QuoteToLeRepository quoteToLeRepository= appCtx.getBean(QuoteToLeRepository.class);
			if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
				List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteIzosdwanByonUploadDetails.get(0).getQuote().getId());
				List<Integer> quoteIds = quoteIzosdwanByonUploadDetails.stream().map(id -> id.getQuote().getId())
						.distinct().collect(Collectors.toList());
				LOGGER.info("Unique quote ids {} size {}", quoteIds, quoteIds.size());
				Map<Integer, List<ByonBulkUploadDetail>> map = new HashMap<>();

				quoteIds.forEach(id -> {
					if (!map.containsKey(id)) {
						List<ByonBulkUploadDetail> byonBulkUploadDetails = convertEntityToBeanForByonDetails(
								quoteIzosdwanByonUploadDetails.stream().filter(det -> det.getQuote().getId().equals(id))
										.collect(Collectors.toList()),quoteToLes,mqUtils);
						map.put(id, byonBulkUploadDetails);
					}
				});
				LOGGER.info("Got OPEN records for getting location details {} in queue {}", map,
						"rabbitmq_izosdwan_byon_detail");
				mqUtils.send("rabbitmq_izosdwan_byon_detail", Utils.convertObjectToJson(map));
				updateStatus(IzosdwanCommonConstants.INPROGRESS, quoteIzosdwanByonUploadDetailRepository,
						quoteIzosdwanByonUploadDetails);
				
			}else {
				updateInQuoteIfUploadIsDone(quoteIzosdwanByonUploadDetailRepository, appCtx);
			}
		} catch (Exception e) {
			LOGGER.error("Error occured while triggering the IzosdwanLocationJob ", e);
		}

	}

	private void updateStatus(String toStatus,
			QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository,
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails) {
		LOGGER.info("Updating status for {} with status {}", quoteIzosdwanByonUploadDetails, toStatus);
		if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails2 = new ArrayList<>();
			quoteIzosdwanByonUploadDetails.stream().forEach(entity -> {
				entity.setStatus(toStatus);
				quoteIzosdwanByonUploadDetails2.add(entity);
			});
			quoteIzosdwanByonUploadDetailRepository.saveAll(quoteIzosdwanByonUploadDetails2);
		}
	}

	
	private List<ByonBulkUploadDetail> convertEntityToBeanForByonDetails(
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails, List<QuoteToLe> quoteToLes, MQUtils mqUtils) {
		List<ByonBulkUploadDetail> byonBulkUploadDetails = new ArrayList<>();
		quoteIzosdwanByonUploadDetails.stream().forEach(quoteIzosdwanByonUploadDetail -> {
			ByonBulkUploadDetail byonBulkUploadDetail = new ByonBulkUploadDetail();
			byonBulkUploadDetail.setAddress(quoteIzosdwanByonUploadDetail.getAddress());
			byonBulkUploadDetail.setCity(quoteIzosdwanByonUploadDetail.getCity());
			byonBulkUploadDetail.setCountry(quoteIzosdwanByonUploadDetail.getCountry());
			byonBulkUploadDetail.setErrorMessageToDisplay(quoteIzosdwanByonUploadDetail.getErrorDetails());
			byonBulkUploadDetail.setId(quoteIzosdwanByonUploadDetail.getId());
			byonBulkUploadDetail.setInternetQuality(quoteIzosdwanByonUploadDetail.getInternetQuality());
			byonBulkUploadDetail.setLocality(quoteIzosdwanByonUploadDetail.getLocality());
			byonBulkUploadDetail.setLocationId(quoteIzosdwanByonUploadDetail.getLocationId());
			byonBulkUploadDetail.setPinCode(quoteIzosdwanByonUploadDetail.getPincode());
			byonBulkUploadDetail.setRetriggerCount(quoteIzosdwanByonUploadDetail.getRetriggerCount());
			byonBulkUploadDetail.setState(quoteIzosdwanByonUploadDetail.getState());
			byonBulkUploadDetail.setStatus(quoteIzosdwanByonUploadDetail.getStatus());
			byonBulkUploadDetail.setId(quoteIzosdwanByonUploadDetail.getId());
			byonBulkUploadDetail.setQuoteId(quoteIzosdwanByonUploadDetail.getQuote().getId());
			byonBulkUploadDetail.setManagementOption(quoteIzosdwanByonUploadDetail.getManagementOption());
			if (!quoteToLes.isEmpty()) {
				QuoteToLe quoteToLe = quoteToLes.get(0);
				if (quoteToLe.getErfCusCustomerLegalEntityId() != null) {
					byonBulkUploadDetail.setErfCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
					//get erf cus customer id
					List<CustomerDetailBean> customerDetailBeans = getCustomerInformation(Arrays.asList(quoteToLe.getErfCusCustomerLegalEntityId()),mqUtils);
					LOGGER.info("got customer id from customer details  by le queue");
					if(customerDetailBeans!=null) {
						byonBulkUploadDetail.setErfCustomerId(customerDetailBeans.stream().findFirst().get().getCustomerId());
					}
				}
			}
			byonBulkUploadDetails.add(byonBulkUploadDetail);
		});
		LOGGER.info("Converted the entity to bean {}", byonBulkUploadDetails);
		return byonBulkUploadDetails;
	}

	private void updateInQuoteIfUploadIsDone(
			QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository,
			ApplicationContext appCtx) {
		LOGGER.info("Inside updateInQuoteIfUploadIsDone");
		QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository = appCtx
				.getBean(QuoteIzoSdwanAttributeValuesRepository.class);
		QuoteRepository quoteRepository = appCtx.getBean(QuoteRepository.class);
		List<Integer> quoteIds = quoteIzosdwanByonUploadDetailRepository.getUploadSuccessQuotes();
		LOGGER.info("Got quote ids {}");
		if (quoteIds != null && !quoteIds.isEmpty()) {
			LOGGER.info("Got quote ids {}", quoteIds.toString());
			List<QuoteIzoSdwanAttributeValues> list = new ArrayList<>();
			quoteIds.stream().forEach(id -> {
				Quote quote = quoteRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
				if (quote != null) {
					List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
							.findByDisplayValueAndQuote(IzosdwanCommonConstants.BYON_INTERNET, quote);
					if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty() && quoteIzoSdwanAttributeValues.get(0).getAttributeValue()!=null && quoteIzoSdwanAttributeValues.get(0).getAttributeValue().equalsIgnoreCase("false")) {
						LOGGER.info("Got attribute details for quote {}", quote.getId());
						quoteIzoSdwanAttributeValues.get(0).setAttributeValue("true");
						list.add(quoteIzoSdwanAttributeValues.get(0));
					}
				}
			});
			if (list != null && !list.isEmpty()) {
				quoteIzoSdwanAttributeValuesRepository.saveAll(list);
			}
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository.findByQuote_idIn(quoteIds);
			updateStatus(IzosdwanCommonConstants.COMPLETED, quoteIzosdwanByonUploadDetailRepository, quoteIzosdwanByonUploadDetails);
		}
	}
	
	/**
	 * @author Madhumiethaa Palanisamy
	 * Get Customer details from customer le Queue
	 *
	 * @param customerLeIds
	 * @return {@link List<CustomerDetailBean>}
	 */
	private List<CustomerDetailBean> getCustomerInformation(List<Integer> customerLeIds, MQUtils mqUtils) {
		List<CustomerDetailBean> customerDetailBeans = null;
		String response = null;
		try {
			response = (String) mqUtils.sendAndReceive("rabbitmq_customer_details_by_le_queue", Utils.convertObjectToJson(customerLeIds));
			LOGGER.info("inside getCustomerInformation with response:{}",response);
		} catch (Exception e) {
		
		}
		if (StringUtils.isNotBlank(response)) {
			customerDetailBeans = Utils.fromJson(response, new TypeReference<List<CustomerDetailBean>>() {
			});
			LOGGER.info("Customer Information from customerLeDetailsQueue{} ",
					customerDetailBeans.stream().findFirst().get().getCustomerId());
		} 
		return customerDetailBeans;
	}
}
