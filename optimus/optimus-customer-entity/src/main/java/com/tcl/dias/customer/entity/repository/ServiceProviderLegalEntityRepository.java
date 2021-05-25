package com.tcl.dias.customer.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.ServiceProviderLegalEntity;

import java.util.List;
import java.util.Map;


/**
 *  This file is the repository for ServiceProvider LegalEntity Repository
 * 
 * @author SEKHAR ER
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ServiceProviderLegalEntityRepository extends JpaRepository<ServiceProviderLegalEntity, Integer>  {


    @Query(value = "select splg.id as serviceProviderId, splg.entity_name as supplierEntityName, cm.short_name as currency\n" +
            "from service_provider_legal_entity splg left join sp_le_currency slc on splg.id = slc.sp_le_id\n" +
            "left join currency_master cm on slc.currency_id = cm.id where slc.is_default=1", nativeQuery = true)
    List<Map<String, Object>> findAllSupplierDetails();

    @Query(value = "select * from service_provider_legal_entity where entity_name=UPPER(trim(:supplierName))", nativeQuery = true)
    ServiceProviderLegalEntity findByEntityName(@Param("supplierName") String supplierName);
}
