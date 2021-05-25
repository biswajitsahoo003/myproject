package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CurrencyMaster;

/**
 * This file contains the CurrencyMasterRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CurrencyMasterRepository extends JpaRepository<CurrencyMaster, Integer> {

	@Query(value = "SELECT cm.short_name FROM currency_master cm WHERE ID IN (SELECT currency_id FROM sp_le_currency WHERE sp_le_id = :entityId)", nativeQuery = true)
	List<String> findShortNameById(@Param("entityId") Integer entityId);

	@Query(value = "SELECT cm.short_name FROM currency_master cm WHERE ID IN (SELECT currency_id FROM sp_le_currency WHERE sp_le_id = :entityId AND is_default = :isDefault)", nativeQuery = true)
	List<String> findShortNameByIdAndIsDefault(@Param("entityId") Integer entityId, @Param("isDefault") Integer isDefault);

	@Query(value = "select mc.name as countryName,cm.short_name as currencyName from customer_le_country clc left join mst_countries mc on clc.country_id = mc.id " +
			"left join mst_countries_currency_master mccm on mc.id = mccm.mst_countries_id " +
			"left join currency_master cm on mccm.currency_master_id = cm.id " +
			"where clc.customer_le_id = :customerLeId", nativeQuery = true)
	Map<String, Object> findCurrencyDetailsByLe(@Param("customerLeId") Integer customerLeId);
}