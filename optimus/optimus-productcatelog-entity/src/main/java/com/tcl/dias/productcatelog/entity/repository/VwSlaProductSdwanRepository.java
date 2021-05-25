package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.VwSlaPdtByTierSiteSdwanSelect;

/**
 * Repository for VwSlaProductSdwanRepository
 * @author vpachava
 *
 */
@Repository
public interface VwSlaProductSdwanRepository extends JpaRepository<VwSlaPdtByTierSiteSdwanSelect, Integer>{
	
	@Query(value = "SELECT tier1_value FROM vw_sla_pdt_by_tier_site_sdwan_select where site_type_name=:siteTypeName",nativeQuery=true)
	public String getTier1AndSiteTypeName(String siteTypeName);
	
	@Query(value = "SELECT tier2_value FROM vw_sla_pdt_by_tier_site_sdwan_select where site_type_name=:siteTypeName",nativeQuery=true)
	public String getTier2AndSiteTypeName(String siteTypeName);
	
	@Query(value = "SELECT tier3_value FROM vw_sla_pdt_by_tier_site_sdwan_select where site_type_name=:siteTypeName",nativeQuery=true)
	public String getTier3AndSiteTypeName(String siteTypeName);
	
	
	@Query(value = "SELECT tier4_value FROM vw_sla_pdt_by_tier_site_sdwan_select where site_type_name=:siteTypeName",nativeQuery=true)
	public String getTier4AndSiteTypeName(String siteTypeName);

}
