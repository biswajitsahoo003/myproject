package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.VwSdwanProductOffering;

/**
 * 
 * SDWAN product offerings view repository class
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface VwSdwanProductOfferingRepository extends JpaRepository<VwSdwanProductOffering, Integer>{
	
	/**
	 * 
	 * Get Vendors available for IZOSDWAN
	 * @author AnandhiV
	 * @return
	 */
	@Query(value = "select distinct vendor_cd from vw_sdwan_product_offerings", nativeQuery = true)
	public List<String> getUniqueVendors();
	
	/**
	 * 
	 * Get offerings details based on vendor and profiles
	 * @author AnandhiV
	 * @param vendor
	 * @return
	 */
	@Query(value = "select distinct profile_cd from vw_sdwan_product_offerings where vendor_cd=:vendor", nativeQuery = true)
	public List<String> getUniqueProfilesForTheVendor(String vendor);
	
	public List<VwSdwanProductOffering> findByProfileCdAndVendorCd(String profile,String vendor);
}
