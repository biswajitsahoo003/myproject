package com.tcl.dias.oms.service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.CurrencyConversion;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CurrencyConversionRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This file contains the OmsUtilService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class OmsUtilService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsUtilService.class);

	@Autowired
	CurrencyConversionRepository currencyConversionRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	
	@Autowired
	OrderToLeRepository orderToLeRepository;
	
	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@Autowired
	OrdersLeAttributeValueRepository orderLeAttributeValueRepository;

	/**
	 * @author Thamizhselvi Perumal Method to convert currency value from
	 *         existingcurrency format to input currency format
	 * @param existingCurrency
	 * @param inputCurrency
	 * @param currencyValue
	 * @return
	 * @throws TclCommonException
	 */
	public Double convertCurrency(String existingCurrency, String inputCurrency, Double currencyValue) {
		LOGGER.info(" currency value before is  : {} ", currencyValue);
		LOGGER.info("Input Currency first is : {} , Existing Currency first is : {} ", inputCurrency,existingCurrency);
		if (Objects.nonNull(existingCurrency) && Objects.nonNull(inputCurrency)
				&& !existingCurrency.equalsIgnoreCase(inputCurrency)
				&& Objects.nonNull(currencyValue) && currencyValue != 0) {
			String exstnCurConvsnRate = findCurrencyConversionRate(existingCurrency);
			Double usdConversion = currencyValue / Double.parseDouble(exstnCurConvsnRate);
			LOGGER.info("Input Currency is :  {}  and currency value is : {}   ", inputCurrency,currencyValue);
			String inputCurConvsnRate = findCurrencyConversionRate(inputCurrency);
			currencyValue = usdConversion * Double.parseDouble(inputCurConvsnRate);
			LOGGER.info(" currency value after is  : {} ", currencyValue);
		}
		return currencyValue;
	}

	/**
	 * Convert currency values and format with given precision
	 *
	 * @param existingCurrency
	 * @param inputCurrency
	 * @param currencyValue
	 * @return
	 * @throws TclCommonException
	 */
	public Double convertCurrencyWithPrecision(String existingCurrency, String inputCurrency, Double currencyValue,
			Integer precision) {
		LOGGER.info(" currency value before is  : {} ", currencyValue);
		LOGGER.info("Input Currency first is : {} , Existing Currency first is : {} ", inputCurrency, existingCurrency);
		if (Objects.nonNull(existingCurrency) && Objects.nonNull(inputCurrency) && !existingCurrency
				.equalsIgnoreCase(inputCurrency) && Objects.nonNull(currencyValue) && currencyValue != 0) {
			String exstnCurConvsnRate = findCurrencyConversionRate(existingCurrency);
			Double usdConversion = currencyValue / Double.parseDouble(exstnCurConvsnRate);
			LOGGER.info("Input Currency is :  {}  and currency value is : {} , Precision is : {}  ", inputCurrency,
					currencyValue, precision);
			String inputCurConvsnRate = findCurrencyConversionRate(inputCurrency);
			currencyValue = usdConversion * Double.parseDouble(inputCurConvsnRate);
			currencyValue = Utils.setPrecision(currencyValue, precision);
			LOGGER.info(" currency value after is  : {} ", currencyValue);
		}
		return currencyValue;
	}

	/**
	 * @author Thamizhselvi Perumal Method to find currency conversion rate
	 * @param currencyInput
	 * @return
	 */
	public String findCurrencyConversionRate(String currencyInput) {
		String currencyRate = null;
		Optional<CurrencyConversion> currency = currencyConversionRepository
				.findByOutputCurrencyAndStatus(currencyInput, CommonConstants.YES.toUpperCase());
		if (currency.isPresent())
			currencyRate = currency.get().getConversionRate();
		LOGGER.info("Currency rate inside method findCurrencyConversionRate is  : {}",currencyRate);
		return currencyRate;
	}
	/**
	 * 
	 * Update Currency and Terms and condition against quote to le
	 */
	public void changeCurrencyAndTermInMonthsAttributesToQuoteToLe() {
		try {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findAll();
		List<QuoteToLe> updatedQuoteToLes = new ArrayList<>();
		if(quoteToLes!=null) {
			quoteToLes.stream().forEach(quTLe->{
				boolean isModified = false;
				if(quTLe.getCurrencyCode()==null) {
					quTLe.setCurrencyCode(returnAttributeForCurrency(quTLe));
					isModified=true;
				}
				if(quTLe.getTermInMonths()==null) {
					quTLe.setTermInMonths(returnAttributeForTermsInMonth(quTLe));
					if(!isModified) {
						isModified=true;
					}
				}
				if(isModified) {
					updatedQuoteToLes.add(quTLe);
				}
			});
		}
		if(!updatedQuoteToLes.isEmpty()) {
			quoteToLeRepository.saveAll(updatedQuoteToLes);
		}
		}catch(Exception e) {
			LOGGER.info("Exception occured : {}",e.getMessage());
		}
	}
	
	private String returnAttributeForCurrency(QuoteToLe quoteToLe) {
		List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.PAYMENT_CURRENCY, CommonConstants.BACTIVE);
		if(mstOmsAttribute!=null && !mstOmsAttribute.isEmpty()) {
			List<QuoteLeAttributeValue> orAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute.get(0));
			if(orAttributeValues!=null && !orAttributeValues.isEmpty()) {
				return orAttributeValues.stream().findFirst().get().getAttributeValue();
			}
		}
		return null;
	}
	
	private String returnAttributeForTermsInMonth(QuoteToLe quoteToLe) {
		List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS, CommonConstants.BACTIVE);
		if(mstOmsAttribute!=null && !mstOmsAttribute.isEmpty()) {
			List<QuoteLeAttributeValue> orAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute.get(0));
			if(orAttributeValues!=null && !orAttributeValues.isEmpty()) {
				return orAttributeValues.stream().findFirst().get().getAttributeValue();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * Update Currency and Terms and condition against order to le
	 */
	public void changeCurrencyAndTermInMonthsAttributesToOrderToLe() {
		try {
		List<OrderToLe> orderToLes = orderToLeRepository.findAll();
		List<OrderToLe> updatedOrderToLes = new ArrayList<>();
		if(orderToLes!=null) {
			orderToLes.stream().forEach(orTLe->{
				boolean isModified = false;
				if(orTLe.getCurrencyCode()==null) {
					orTLe.setCurrencyCode(returnAttributeForCurrencyOrder(orTLe));
					isModified=true;
				}
				if(orTLe.getTermInMonths()==null) {
					orTLe.setTermInMonths(returnAttributeForTermsInMonthOrder(orTLe));
					if(!isModified) {
						isModified=true;
					}
				}
				if(isModified) {
					updatedOrderToLes.add(orTLe);
				}
			});
		}
		if(!updatedOrderToLes.isEmpty()) {
			orderToLeRepository.saveAll(updatedOrderToLes);
		}
		}catch(Exception e) {
			LOGGER.info("Exception occured : {}",e.getMessage());
		}
	}
	
	private String returnAttributeForCurrencyOrder(OrderToLe orderToLe) {
		List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.PAYMENT_CURRENCY, CommonConstants.BACTIVE);
		if(mstOmsAttribute!=null && !mstOmsAttribute.isEmpty()) {
			Set<OrdersLeAttributeValue> orAttributeValues = orderLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute.get(0), orderToLe);
			if(orAttributeValues!=null && !orAttributeValues.isEmpty()) {
				return orAttributeValues.stream().findFirst().get().getAttributeValue();
			}
		}
		return null;
	}
	
	private String returnAttributeForTermsInMonthOrder(OrderToLe orderToLe) {
		List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS, CommonConstants.BACTIVE);
		if(mstOmsAttribute!=null && !mstOmsAttribute.isEmpty()) {
			Set<OrdersLeAttributeValue> orAttributeValues = orderLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute.get(0), orderToLe);
			if(orAttributeValues!=null && !orAttributeValues.isEmpty()) {
				return orAttributeValues.stream().findFirst().get().getAttributeValue();
			}
		}
		return null;
	}
	
	public BigDecimal convertCurrencyBigDecimal(String existingCurrency, String inputCurrency, BigDecimal currencyValue) {
		LOGGER.info(" currency value before is  : {} ", currencyValue);
		LOGGER.info("Input Currency first is : {} , Existing Currency first is : {} ", inputCurrency,existingCurrency);
		if (Objects.nonNull(existingCurrency) && Objects.nonNull(inputCurrency)
				&& !existingCurrency.equalsIgnoreCase(inputCurrency)
				&& Objects.nonNull(currencyValue) && currencyValue != BigDecimal.ZERO) {
			String exstnCurConvsnRate = findCurrencyConversionRate(existingCurrency);
			
			BigDecimal usdConversion = currencyValue.divide(new BigDecimal(exstnCurConvsnRate), RoundingMode.DOWN);
			LOGGER.info("Input Currency is :  {}  and currency value is : {}   ", inputCurrency,currencyValue);
			String inputCurConvsnRate = findCurrencyConversionRate(inputCurrency);
			currencyValue = usdConversion.multiply(new BigDecimal(inputCurConvsnRate));
			LOGGER.info(" currency value after is  : {} ", currencyValue);
		}
		return currencyValue;
	}
	
}
