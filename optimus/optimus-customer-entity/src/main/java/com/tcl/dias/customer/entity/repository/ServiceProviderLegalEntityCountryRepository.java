package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.MstCountry;
import com.tcl.dias.customer.entity.entities.ServiceProviderLegalEntity;
import com.tcl.dias.customer.entity.entities.SpLeCountry;

/**
 * 
 * This file is the repository for ServiceProvider LegalEntity Country Repository
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServiceProviderLegalEntityCountryRepository  extends JpaRepository<SpLeCountry, Integer> {
	
	List<SpLeCountry> findByMstCountryAndIsDefault(MstCountry mstcountry, Byte isDefault);
	
	@Query(value ="SELECT * from sp_le_country where country_id=:countryId and (product_name=:productName or product_name is null)",nativeQuery =true)
	List<SpLeCountry> getSupplierByCountryAndProduct(@Param("countryId") Integer countryId,@Param("productName") String productName);
	
	List<SpLeCountry> findByMstCountry_NameIn(List<String> countries);
	
	List<SpLeCountry> findByServiceProviderLegalEntity(ServiceProviderLegalEntity spLegelId);

	@Query(value ="SELECT * from sp_le_country where (product_name=:productName or product_name is null) and country_id is not null",nativeQuery =true)
	List<SpLeCountry> getSupplierByProductAndCountryNotNull(String productName);
}
