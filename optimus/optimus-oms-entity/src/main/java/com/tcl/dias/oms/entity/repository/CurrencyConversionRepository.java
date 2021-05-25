package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.CurrencyConversion;

/**
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Integer> {

	public CurrencyConversion findByInputCurrencyAndOutputCurrency(String inputCurrency, String outputCurrency);

	public List<CurrencyConversion> findByInputCurrency(String inputCurrency);
	
	public Optional<CurrencyConversion> findByIdAndStatus(Integer customer, Byte status);
	
	public Optional<CurrencyConversion> findByOutputCurrencyAndStatus(String outputCurrency, String status);


}
