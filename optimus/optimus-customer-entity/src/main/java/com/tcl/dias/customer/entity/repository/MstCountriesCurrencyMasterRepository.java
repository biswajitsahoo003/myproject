package com.tcl.dias.customer.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.MstCountriesCurrencyMaster;

/**
 * Repository contains MstCountriesCurrencyMaster related methods
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstCountriesCurrencyMasterRepository extends JpaRepository<MstCountriesCurrencyMaster, Integer> {

	@Query(value = "SELECT cm.short_name FROM mst_countries_currency_master mccm "
			+ "left join currency_master cm on cm.id = mccm.currency_master_id "
			+ "left join mst_countries mc on  mc.id=mccm.mst_countries_id "
			+ "where mc.name=:countryName", nativeQuery = true)
	String findByCountryName(@Param("countryName") String countryName);

}
