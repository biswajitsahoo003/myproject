package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.CpeBomSdwanRulesView;


/**
 * SDWAN cpe details repository
 * @author vpachava
 *@link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface CpeBomSdwanDetailRepository extends JpaRepository<CpeBomSdwanRulesView, Integer>{

	
	public List<CpeBomSdwanRulesView> findByVendorNameAndAddonCdAndLicCd(String vendorName,String addonCd,String licCd);
	
	@Query(value = "SELECT * from vw_sdwan_bom_rules a where a.vendor_name=:vendorName and a.lic_cd=:licCd and  a. addon_cd is null", nativeQuery = true)
	public List<CpeBomSdwanRulesView> selectByVendorNameAndLicCd(String vendorName,String licCd);

}
