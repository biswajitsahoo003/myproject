package com.tcl.dias.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.QuoteDataBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;

/**
 * Class for Common service methods for audit service
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class CommonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${optimus.audit.save.queue}")
	String auditSaveQueue;

	@Value("${oms.get.quote.data.by.id}")
	String getQuoteByIdQueue;

	@Value("${oms.get.quotetole.data.by.id}")
	String getQuoteToLeByIdQueue;

	@Value("${oms.get.order.by.id}")
	String getOrderById;

	@Value("${oms.get.orderle.by.id}")
	String getOrderLeById;

	/**
	 * getQuoteData to get quote/order data using id
	 * 
	 * @param keyValue
	 * @return
	 */
	public QuoteDataBean getQuoteData(Map<String, Integer> keyValue) {
		List<QuoteDataBean> quoteDataBeanList = new ArrayList<>();
		LOGGER.info("Entering getQuoteData method to get quote/order data ");
		Boolean[] valueFlag = new Boolean[2];
		valueFlag[0] = false;
		keyValue.keySet().stream().forEach(keys -> {
			if (!valueFlag[0]) {
				LOGGER.info("Fetch data for id {} ", keys);
				switch (keys) {
				case CommonConstants.ORDERID:
					quoteDataBeanList.add(setQuoteDataBeans(getOrderById, keyValue.get(keys)));
					valueFlag[0] = true;
					break;
				case CommonConstants.ORDERLEID:
					quoteDataBeanList.add(setQuoteDataBeans(getOrderLeById, keyValue.get(keys)));
					valueFlag[0] = true;
					break;
				case CommonConstants.QUOTEID:
					quoteDataBeanList.add(setQuoteDataBeans(getQuoteByIdQueue, keyValue.get(keys)));
					valueFlag[0] = true;
					break;
				case CommonConstants.QUOTELEID:
					quoteDataBeanList.add(setQuoteDataBeans(getQuoteToLeByIdQueue, keyValue.get(keys)));
					valueFlag[0] = true;
					break;
				default:
					break;
				}
			}
		});
		return quoteDataBeanList.get(0);
	}

	/**
	 * setQuoteDataBeans to get data from queue response
	 * 
	 * @param queueName
	 * @param id
	 * @return
	 */
	private QuoteDataBean setQuoteDataBeans(String queueName, Integer id) {
		QuoteDataBean quoteDataBean = new QuoteDataBean();
		String queueResponse = null;
		try {
			LOGGER.info("MDC Filter token value in before Queue call setQuoteDataBeans {}  and queueName {} ",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), queueName);
			queueResponse = (String) mqUtils.sendAndReceive(queueName, Utils.convertObjectToJson(id));
			LOGGER.info("Response from queue before null check {} : {} ", queueName, queueResponse);
			if (queueResponse != null && StringUtils.isNotBlank(queueResponse)) {
				LOGGER.info("Response from queue after null check {} : {} ", queueName, queueResponse);
				quoteDataBean.setFromValue(queueResponse);
				JSONObject json = new JSONObject(queueResponse);
				if (json.keySet().contains(CommonConstants.QUOTECODE)) {
					quoteDataBean.setQuoteCode(json.getString(CommonConstants.QUOTECODE));
				}
				if (json.keySet().contains(CommonConstants.QUOTECODES)) {
					quoteDataBean.setQuoteCode(json.getString(CommonConstants.QUOTECODES));
				}
				if (json.keySet().contains(CommonConstants.STAGE)) {
					quoteDataBean.setStage(json.getString(CommonConstants.STAGE));
				}
				if (json.keySet().contains(CommonConstants.STAGES)) {
					quoteDataBean.setStage(json.getString(CommonConstants.STAGES));
				}
				if (json.keySet().contains(CommonConstants.ORDERCODE)) {
					quoteDataBean.setQuoteCode(json.getString(CommonConstants.ORDERCODE));
				}
				if (json.keySet().contains(CommonConstants.ORDERCODES)) {
					quoteDataBean.setQuoteCode(json.getString(CommonConstants.ORDERCODES));
				}
				LOGGER.info("setQuoteDataBeans {} quoteDataBean : ", quoteDataBean);
			}
		} catch (Exception e) {
			LOGGER.error("Error occured in setQuoteDataBeans : ", e);
		}
		return quoteDataBean;

	}

}
