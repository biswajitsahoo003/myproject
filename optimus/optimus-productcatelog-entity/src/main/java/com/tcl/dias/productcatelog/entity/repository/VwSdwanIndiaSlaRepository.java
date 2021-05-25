package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.VwSdwanSelectIndiaSla;


/**
 * Repository for VwSdwanIndiaSlaRepository
 * @author vpachava
 *
 */
@Repository
public interface VwSdwanIndiaSlaRepository extends JpaRepository<VwSdwanSelectIndiaSla, Integer> {
	
	
	@Query(value = "SELECT tier_type FROM vw_sdwan_select_india_sla where prod_offr_cd=:vendorName and city_nm=:city",nativeQuery=true)
	public String getTierValue(String city, String vendorName);
	
	@Query(value = "SELECT tier_type FROM vw_sdwan_select_india_sla where prod_offr_cd=:vendorName and location_nm=:country",nativeQuery=true)
	public String getTierValueByCountry(String country, String vendorName);

}
