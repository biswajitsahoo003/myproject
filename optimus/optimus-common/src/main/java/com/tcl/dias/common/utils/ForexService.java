package com.tcl.dias.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ExchangeRate;
import com.tcl.dias.common.beans.ForexBean;
import com.tcl.dias.common.beans.ForexResponseBean;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * ForexService - This is used to convert the currency
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ForexService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForexService.class);

	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	RestClientService restClientService;

	@Value("${forex.api.username}")
	String forexUserName;

	@Value("${forex.api.password}")
	String forexPassword;

	@Value("${forex.api.url}")
	String forexUrl;

	/**
	 * This method is used to convert the fox currency
	 * 
	 * @param fromCurrency
	 * @param toCurrency
	 * @param fromValue
	 * @return
	 */
	public Double convertCurrency(Currencies fromCurrency, Currencies toCurrency) {
		try {
			ForexBean forexBean = new ForexBean();
			ExchangeRate exchangeRate = new ExchangeRate();
			exchangeRate.setFromCurrency(fromCurrency.toString());
			exchangeRate.setToCurrency(toCurrency.toString());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, -1);
			Date yesterday = calendar.getTime();
			String date = DATE_FORMAT.format(yesterday);
			exchangeRate.setRateDate(date);
			forexBean.setExchangeRate(exchangeRate);
			return processForex(forexBean, 1);
		} catch (Exception e) {
			LOGGER.error("Error in converting currency", e);
			return null;
		}
	}

	private Double processForex(ForexBean forexBean, int count) throws TclCommonException {
		if (count <= 7) {
			RestResponse response = restClientService.postWithBasicAuthentication(forexUrl,
					Utils.convertObjectToJson(forexBean), new HashMap<>(), forexUserName, forexPassword);
			if (response.getStatus() == Status.SUCCESS) {
				LOGGER.info("Response Received is {}", response.getData());
				ForexResponseBean fResponse = (ForexResponseBean) Utils.convertJsonToObject(response.getData(),
						ForexResponseBean.class);
				ExchangeRate exchangeRate = null;
				for (ExchangeRate exchageR : fResponse.getExchangeRate()) {
					exchangeRate = exchageR;
				}
				if (StringUtils.isNotBlank(exchangeRate.getExhangeRate()))
					return Double.valueOf(exchangeRate.getExhangeRate());
				if (exchangeRate.getStatus().equals("Not Successful")) {
					LOGGER.info("Forex is not available so retrying count : {}", count);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.DATE, -(1 + count));
					Date yesterday = calendar.getTime();
					String date = DATE_FORMAT.format(yesterday);
					forexBean.getExchangeRate().setRateDate(date);
					return processForex(forexBean, count + 1);
				}

			}
		}
		return null;
	}
	
	public Double convertCurrencyWithDate(Currencies fromCurrency, Currencies toCurrency,String dateStr) {
		try {
			ForexBean forexBean = new ForexBean();
			ExchangeRate exchangeRate = new ExchangeRate();
			exchangeRate.setFromCurrency(fromCurrency.toString());
			exchangeRate.setToCurrency(toCurrency.toString());
			exchangeRate.setRateDate(dateStr);
			forexBean.setExchangeRate(exchangeRate);
			return processForex(forexBean, 3);
		} catch (Exception e) {
			LOGGER.error("Error in converting currency", e);
			return null;
		}
	}

}
