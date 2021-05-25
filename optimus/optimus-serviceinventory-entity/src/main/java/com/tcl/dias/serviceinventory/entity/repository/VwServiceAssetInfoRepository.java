package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.VwServiceAssetInfo;

/**
 * 
 * This is the repository class for VwServiceAssetInfo
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface VwServiceAssetInfoRepository extends JpaRepository<VwServiceAssetInfo, Integer>{
	
	public List<VwServiceAssetInfo> findByServiceIdAndAssetName(String serviceId,String assetName);
	
	@Query(value = "select * from vw_service_asset_info where service_id=:serviceId and (asset_name=:asset or asset_type=:asset)",nativeQuery =true)
	public List<VwServiceAssetInfo> findCpeAssestes(String serviceId,String asset);
	
//	@Query(value="select sa.asset_name as assetName , sa.asset_sys_id as asset_sys_id,sa.model as cpe_model,sa.scope_of_management as scope_management,sa.owner as ip_address_provided,sa.support_type as cpe_support_type,sa.serial_no as cpe_serial_no, sa.oem_vendor as oem_vendor from  vw_service_asset_info sa where sa.srv_sys_id =:sysid and sa.asset_type=:type order by sa.asset_sys_id desc limit 1", nativeQuery=true)
//	VwServiceAssetInfo getAssetTypeDetails(@Param("sysid") Integer sysid,@Param("type") String type);
}
