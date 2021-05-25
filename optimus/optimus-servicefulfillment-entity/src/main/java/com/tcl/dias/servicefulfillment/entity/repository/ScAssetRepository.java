package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.custom.IGscScAsset;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAsset;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetReserved;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetWithStatus;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * This file contains repository class of OdrServiceDetail entity
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ScAssetRepository extends JpaRepository<ScAsset, Integer> {

    List<ScAsset> findByScServiceDetail(ScServiceDetail scServiceDetail);
    
    List<ScAsset> findByScServiceDetail_id(Integer scServiceDetailId);
    

    @Query(value = "SELECT COUNT(sa.id) AS num FROM sc_asset sa LEFT JOIN sc_asset_relation sar ON sar.sc_asset_id=sa.id AND sar.relation_type='Toll-Free' WHERE sa.sc_service_detail_id=:serviceId AND TYPE='Outpulse' AND sar.id IS NULL", nativeQuery = true)
    Integer findRequiredQty(Integer serviceId);
    
    @Query(value = "SELECT sa.id as AssetId,sa.sc_service_detail_id as AssetServiceDetailId,sa.name as AssetName, sa.`type` as AssetType FROM sc_asset sa LEFT JOIN sc_asset_relation sar ON sar.sc_asset_id=sa.id AND sar.relation_type='Toll-Free' WHERE sa.sc_service_detail_id=:serviceId AND TYPE='Outpulse' AND sar.id IS NULL", nativeQuery = true)
    List<IGscScAsset> getUnAssociatedOutpulse(Integer serviceId);
    
    @Query(value = "SELECT sao.id AS AssetId,sao.sc_service_detail_id AS AssetServiceDetailId,sao.name AS AssetName, sao.`type` AS AssetType FROM sc_asset sa JOIN sc_asset_attributes saa ON saa.sc_asset_id=sa.id AND saa.attribute_name='isPortNumber' LEFT JOIN sc_asset_relation sar ON sar.sc_asset_id=sa.id AND sar.relation_type='Outpulse' LEFT JOIN sc_asset sao ON sar.sc_related_asset_id=sao.id WHERE sa.sc_service_detail_id=:serviceId AND sa.TYPE='Toll-Free' AND saa.attribute_value='yes'", nativeQuery = true)
    List<IGscScAsset> getPortingOutpulse(Integer serviceId);
    
    @Query(value = "SELECT sao.id AS AssetId,sao.sc_service_detail_id AS AssetServiceDetailId,sao.name AS AssetName, sao.`type` AS AssetType FROM sc_asset sa JOIN sc_asset_attributes saa ON saa.sc_asset_id=sa.id AND saa.attribute_name='isUifnProcuredNumber' LEFT JOIN sc_asset_relation sar ON sar.sc_asset_id=sa.id AND sar.relation_type='Outpulse' LEFT JOIN sc_asset sao ON sar.sc_related_asset_id=sao.id WHERE sa.sc_service_detail_id=:serviceId AND sa.TYPE='Toll-Free' AND saa.attribute_value='yes'", nativeQuery = true)
    List<IGscScAsset> getUifnProcuredOutpulse(Integer serviceId);
    
	@Query(value = "SELECT sc.id AS TollfreeId, sc.name AS TollfreeName, sc.`type` AS TollfreeType, scout.id AS OutpulseId, scout.name AS OutpulseName, scout.`type` AS OutpulseType, scoutr.id as RoutingId, scoutr.name as RoutingName, scoutr.`type` as RoutingType, saa.attribute_value AS ReservationId "
			+ "FROM sc_asset sc "
			+ "JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id AND scr.relation_type='Outpulse' "
			+ "JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id "
			+ "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=scout.id and scra.relation_type='Routing-Number' "
			+ "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id "
			+ "JOIN sc_asset_attributes saa ON saa.sc_asset_id=sc.id AND saa.attribute_name='reservationId' "
			+ "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Toll-Free'", nativeQuery = true)
    List<GscScAssetReserved> getReservedTollFreeNumber(Integer serviceId);
	
	@Query(value = "SELECT sc.id AS RoutingId, sc.name AS RoutingName, sc.`type` AS RoutingType, scout.id AS OutpulseId, scout.name AS OutpulseName, scout.`type` AS OutpulseType, saa.attribute_value AS ReservationId, supplier.attribute_value as SupplierId, scouto.id AS TollfreeId, scouto.name AS TollfreeName, scouto.`type` AS TollfreeType\r\n" + 
			"FROM sc_asset sc\r\n" + 
			"JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id AND scr.relation_type='Outpulse'\r\n" + 
			"JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\r\n" + 
			"LEFT JOIN sc_asset_relation scrt ON scrt.sc_asset_id=sc.id and scrt.relation_type='Toll-Free'\r\n" + 
			"LEFT JOIN sc_asset scouto ON scrt.sc_related_asset_id=scouto.id\r\n" +
			"LEFT JOIN sc_asset_attributes saa ON saa.sc_asset_id=sc.id AND saa.attribute_name='routingNoReservationId'\r\n" + 
			"JOIN sc_asset_attributes supplier ON supplier.sc_asset_id=sc.id AND supplier.attribute_name='supplierId'\r\n" + 
			"WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Routing-Number'", nativeQuery = true)
    List<GscScAssetReserved> getReservedRoutingNumber(Integer serviceId);
	
	@Query(value = "SELECT sc.id AS RoutingId, sc.name AS RoutingName, sc.`type` AS RoutingType, scout.id AS OutpulseId, scout.name AS OutpulseName, scout.`type` AS OutpulseType, supplier.attribute_value as SupplierId\r\n" + 
			"FROM sc_asset sc\r\n" + 
			"JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id AND scr.relation_type='Outpulse'\r\n" + 
			"JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\r\n" + 
			"JOIN sc_asset_attributes supplier ON supplier.sc_asset_id=sc.id AND supplier.attribute_name='supplierId'\r\n" + 
			"WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Routing-Number'", nativeQuery = true)
    List<GscScAssetReserved> getAllRoutingNumber(Integer serviceId);
	
	@Query(value = "SELECT sc.id AS RoutingId, sc.name AS RoutingName, sc.`type` AS RoutingType, scout.id AS OutpulseId, scout.name AS OutpulseName, scout.`type` AS OutpulseType, supplier.attribute_value as SupplierId\r\n" + 
			"FROM sc_asset sc\r\n" + 
			"JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id AND scr.relation_type='Outpulse'\r\n" + 
			"JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\r\n" + 
			"JOIN sc_asset_attributes supplier ON supplier.sc_asset_id=sc.id AND supplier.attribute_name='supplierId'\r\n" + 
			"WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Routing-Number' AND sc.`id` IN :routingAssetId", nativeQuery = true)
    List<GscScAssetReserved> getAllRoutingNumber(Integer serviceId, List<Integer> routingAssetId);
	
	@Query(value = "SELECT sc.id AS RoutingId, sc.name AS RoutingName, sc.`type` AS RoutingType, scout.id AS OutpulseId, scout.name AS OutpulseName, scout.`type` AS OutpulseType, supplier.attribute_value as SupplierId\r\n" + 
			"FROM sc_asset sc\r\n" + 
			"JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id AND scr.relation_type='Outpulse'\r\n" + 
			"JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\r\n" + 
			"JOIN sc_asset_attributes supplier ON supplier.sc_asset_id=sc.id AND supplier.attribute_name='supplierId'\r\n" + 
			"WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Routing-Number' AND supplier.attribute_value=:supplierId", nativeQuery = true)
    List<GscScAssetReserved> getAllRoutingNumber(Integer serviceId, String supplierId);

    @Query(value = "select * from sc_asset where sc_service_detail_id=:serviceId and type=:type" , nativeQuery = true)
   	List<ScAsset> findByServiceIdandType(int serviceId, String type);
    
    @Query(value = "SELECT sc.id AS RoutingId, sc.name AS RoutingName, sc.`type` AS RoutingType FROM sc_asset sc JOIN sc_asset_attributes scaa ON scaa.sc_asset_id=sc.id AND scaa.attribute_name='supplierId' WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Routing-Number' AND scaa.attribute_value=:supplierId" , nativeQuery = true)
   	List<GscScAsset> getRoutingNumberBySupplier(Integer serviceId, String supplierId);
    
    @Query(value = "SELECT sc.id AS RoutingId, sc.name AS RoutingName, sc.`type` AS RoutingType FROM sc_asset sc JOIN sc_asset_attributes scaa ON scaa.sc_asset_id=sc.id AND scaa.attribute_name='supplierId' JOIN sc_asset_relation sar ON sar.sc_asset_id=sc.id LEFT JOIN gsc_flow_group_to_asset gta ON gta.sc_asset_id=sar.sc_related_asset_id WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Routing-Number' AND scaa.attribute_value=:supplierId AND gta.gsc_flow_group_id=:flowGroupId" , nativeQuery = true)
   	List<GscScAsset> getRoutingNumberBySupplierByFlowGroup(Integer serviceId, String supplierId, Integer flowGroupId);

    @Query(value = "SELECT sc.id as TollfreeId, sc.name as TollfreeName, sc.`type` as TollfreeType, scout.id as OutpulseId, scout.name as OutpulseName, scout.`type` as OutpulseType, scoutr.id as RoutingId, scoutr.name as RoutingName, scoutr.`type` as RoutingType\n" +
            "FROM sc_asset sc\n" +
            "JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id and scr.relation_type='Outpulse'\n" +
            "JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\n" +
            "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=scout.id and scra.relation_type='Routing-Number'\n" +
            "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\n" +
            "JOIN gsc_flow_group_to_asset gfga ON scoutr.id = gfga.sc_asset_id\n" + 
            "JOIN gsc_flow_group gfg ON gfg.id = gfga.gsc_flow_group_id\n" +
            "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`=:type AND gfg.id=:gscFlowGrpId", nativeQuery = true)
    List<GscScAsset> findByServiceIdandTypeandAssetId(Integer serviceId, String type, Integer gscFlowGrpId);
    
    @Query(value = "SELECT sc.id as TollfreeId, sc.name as TollfreeName, sc.`type` as TollfreeType, scout.id as OutpulseId, scout.name as OutpulseName, scout.`type` as OutpulseType, scoutr.id as RoutingId, scoutr.name as RoutingName, scoutr.`type` as RoutingType\n" +
            "FROM sc_asset sc\n" +
            "JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id and scr.relation_type='Outpulse'\n" +
            "JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\n" +
            "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=scout.id and scra.relation_type='Routing-Number'\n" +
            "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\n" +
            "JOIN gsc_flow_group_to_asset gfga ON sc.id = gfga.sc_asset_id\n" + 
            "JOIN gsc_flow_group gfg ON gfg.id = gfga.gsc_flow_group_id\n" +
            "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`=:type AND gfg.id=:gscFlowGrpId AND sc.status=:status ", nativeQuery = true)
    List<GscScAsset> findByServiceIdandTypeandAssetId(Integer serviceId, String type, Integer gscFlowGrpId, String status);

    @Query(value = "SELECT scout.id AS TollfreeId, scout.name AS TollfreeName, scout.`type` AS TollfreeType, sc.id AS OutpulseId, sc.name AS OutpulseName, sc.`type` AS OutpulseType, scoutr.id AS RoutingId, scoutr.name AS RoutingName, scoutr.`type` AS RoutingType\n" +
            "FROM sc_asset sc\n" +
            "LEFT JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id and scr.relation_type='Toll-Free'\n" +
            "LEFT JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\n" +
            "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=sc.id and scra.relation_type='Routing-Number'\n" +
            "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\n" +
            "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Outpulse' ORDER BY sc.id", nativeQuery = true)
    List<GscScAsset> getTollFreeAndRoutingFromOutpuse(Integer serviceId);
    
    @Query(value = "SELECT scout.id AS TollfreeId, scout.name AS TollfreeName, scout.`type` AS TollfreeType, sc.id AS OutpulseId, sc.name AS OutpulseName, sc.`type` AS OutpulseType, scoutr.id AS RoutingId, scoutr.name AS RoutingName, scoutr.`type` AS RoutingType\n" +
            "FROM sc_asset sc\n" +
            "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=sc.id and scra.relation_type='Routing-Number'\n" +
            "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\n" +
            "LEFT JOIN sc_asset_relation scr ON scr.sc_asset_id=scoutr.id and scr.relation_type='Toll-Free'\n" +
            "LEFT JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\n" +
            "JOIN gsc_flow_group_to_asset gfga ON sc.id = gfga.sc_asset_id\n" + 
            "JOIN gsc_flow_group gfg ON gfg.id = gfga.gsc_flow_group_id\n"+
            "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Outpulse' AND gfg.id=:gscFlowGrpId ORDER BY sc.id", nativeQuery = true)
    List<GscScAsset> getTollFreeAndRoutingFromOutpuseAndOutpluseFlowGrpID(Integer serviceId, Integer gscFlowGrpId);
    
    @Query(value = "SELECT scout.id AS TollfreeId, scout.name AS TollfreeName, scout.`type` AS TollfreeType, sc.id AS OutpulseId, sc.name AS OutpulseName, sc.`type` AS OutpulseType, scoutr.id AS RoutingId, scoutr.name AS RoutingName, scoutr.`type` AS RoutingType\n" +
            "FROM sc_asset sc\n" +
            "LEFT JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id and scr.relation_type='Toll-Free'\n" +
            "LEFT JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\n" +
            "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=sc.id and scra.relation_type='Routing-Number'\n" +
            "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\n" +
            "JOIN gsc_flow_group_to_asset gfga ON scoutr.id = gfga.sc_asset_id\n" + 
            "JOIN gsc_flow_group gfg ON gfg.id = gfga.gsc_flow_group_id\n"+
            "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Outpulse' AND gfg.id=:gscFlowGrpId ORDER BY sc.id", nativeQuery = true)
    List<GscScAsset> getTollFreeAndRoutingFromOutpuseAndFlowGrpID(Integer serviceId, Integer gscFlowGrpId);
    
    @Query(value = "SELECT scout.id AS TollfreeId, scout.name AS TollfreeName, scout.`type` AS TollfreeType, sc.id AS OutpulseId, sc.name AS OutpulseName, sc.`type` AS OutpulseType, scoutr.id AS RoutingId, scoutr.name AS RoutingName, scoutr.`type` AS RoutingType, scoutr.`status` AS Status\n" +
            "FROM sc_asset sc\n" +
            "LEFT JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id and scr.relation_type='Toll-Free'\n" +
            "LEFT JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\n" +
            "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=sc.id and scra.relation_type='Routing-Number'\n" +
            "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\n" +
            "JOIN gsc_flow_group_to_asset gfga ON scoutr.id = gfga.sc_asset_id\n" + 
            "JOIN gsc_flow_group gfg ON gfg.id = gfga.gsc_flow_group_id\n"+
            "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Outpulse' AND gfg.id=:gscFlowGrpId ORDER BY sc.id", nativeQuery = true)
    List<GscScAssetWithStatus> getTollFreeAndRoutingFromOutpuseAndFlowGrpIDWithStatus(Integer serviceId, Integer gscFlowGrpId);
    
    @Query(value = "SELECT scout.id AS TollfreeId, scout.name AS TollfreeName, scout.`type` AS TollfreeType, sc.id AS OutpulseId, sc.name AS OutpulseName, sc.`type` AS OutpulseType, scoutr.id AS RoutingId, scoutr.name AS RoutingName, scoutr.`type` AS RoutingType\n" +
            "FROM sc_asset sc\n" +
            "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=sc.id and scra.relation_type='Routing-Number'\n" +
            "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\n" +
            "LEFT JOIN sc_asset_relation scr ON scr.sc_asset_id=scoutr.id and scr.relation_type='Toll-Free'\n" +
            "LEFT JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\n" +
            "JOIN gsc_flow_group_to_asset gfga ON scoutr.id = gfga.sc_asset_id\n" + 
            "JOIN gsc_flow_group gfg ON gfg.id = gfga.gsc_flow_group_id\n"+
            "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Outpulse' AND gfg.id=:gscFlowGrpId AND scoutr.status=:status ORDER BY sc.id", nativeQuery = true)
    List<GscScAsset> getTollFreeAndRoutingFromOutpuseAndFlowGrpIDAndStatus(Integer serviceId, Integer gscFlowGrpId, String status);
    
    @Query(value = "SELECT scout.id AS TollfreeId, scout.name AS TollfreeName, scout.`type` AS TollfreeType, sc.id AS OutpulseId, sc.name AS OutpulseName, sc.`type` AS OutpulseType, scoutr.id AS RoutingId, scoutr.name AS RoutingName, scoutr.`type` AS RoutingType\n" +
            "FROM sc_asset sc\n" +
            "LEFT JOIN sc_asset_relation scr ON scr.sc_asset_id=sc.id and scr.relation_type='Toll-Free'\n" +
            "LEFT JOIN sc_asset scout ON scr.sc_related_asset_id=scout.id\n" +
            "LEFT JOIN sc_asset_relation scra ON scra.sc_asset_id=sc.id and scra.relation_type='Routing-Number'\n" +
            "LEFT JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\n" +
            "WHERE sc.sc_service_detail_id=:serviceId AND sc.`type`='Outpulse' and scoutr.id in :routingNumberAssetId ORDER BY sc.id", nativeQuery = true)
    List<GscScAsset> getTollFreeAndRoutingFromOutpuse(Integer serviceId, List<Integer> routingNumberAssetId);
	
	@Query(value = " SELECT sc.id AS OutpulseId, sc.name AS OutpulseName, sc.`type` AS OutpulseType,scoutr.id as RoutingId, scoutr.name as RoutingName, scoutr.`type` as RoutingType,scoutt.id AS TollfreeId, scoutt.name AS TollfreeName, scoutt.`type` AS TollfreeType,sca.attribute_value AS isPortingNum, scas.attribute_value as SupplierId  \r\n" + 
			"      FROM sc_asset sc inner JOIN( SELECT sc_related_asset_id FROM sc_asset_relation WHERE relation_type='Outpulse' GROUP BY sc_related_asset_id) scr ON sc.id=scr.sc_related_asset_id\r\n" + 
			"	   JOIN sc_asset_relation scra ON scra.sc_asset_id=sc.id and scra.relation_type='Routing-Number' \r\n" + 
			"      JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\r\n" + 
			"	   JOIN gsc_flow_group_to_asset gfga ON scoutr.id = gfga.sc_asset_id\r\n" + 
			"	   JOIN gsc_flow_group gfg ON gfg.id = gfga.gsc_flow_group_id\r\n" + 
			"      LEFT JOIN sc_asset_relation scrt ON scrt.sc_asset_id=sc.id AND scrt.relation_type='Toll-Free' \r\n" + 
			"      LEFT JOIN sc_asset scoutt ON scrt.sc_related_asset_id=scoutt.id\r\n" + 
			"      JOIN sc_asset_attributes scas ON scoutr.id=scas.sc_asset_id AND scas.attribute_name='supplierId'\r\n" + 
			"      Left JOIN sc_asset_attributes sca ON scrt.sc_related_asset_id=sca.sc_asset_id AND sca.attribute_name='isPortNumber'\r\n" + 
    		"      WHERE sc.sc_service_detail_id=:serviceId AND gfg.id=:gscFlowGroupId ORDER BY sc.id", nativeQuery = true)
	List<GscScAssetReserved> findByServiceIdandAssetId(Integer serviceId,Integer gscFlowGroupId);
	
	@Query(value = " SELECT sc.id AS OutpulseId, sc.name AS OutpulseName, sc.`type` AS OutpulseType,scoutr.id as RoutingId, scoutr.name as RoutingName, scoutr.`type` as RoutingType,scoutt.id AS TollfreeId, scoutt.name AS TollfreeName, scoutt.`type` AS TollfreeType,sca.attribute_value AS isPortingNum, scas.attribute_value as SupplierId  \r\n" + 
			"      FROM sc_asset sc inner JOIN( SELECT sc_related_asset_id FROM sc_asset_relation WHERE relation_type='Outpulse' GROUP BY sc_related_asset_id) scr ON sc.id=scr.sc_related_asset_id\r\n" + 
			"	   JOIN sc_asset_relation scra ON scra.sc_asset_id=sc.id and scra.relation_type='Routing-Number' \r\n" + 
			"      JOIN sc_asset scoutr ON scra.sc_related_asset_id=scoutr.id\r\n" + 
			"	   JOIN gsc_flow_group_to_asset gfga ON scoutr.id = gfga.sc_asset_id\r\n" + 
			"	   JOIN gsc_flow_group gfg ON gfg.id = gfga.gsc_flow_group_id\r\n" + 
			"      LEFT JOIN sc_asset_relation scrt ON scrt.sc_asset_id=sc.id AND scrt.relation_type='Toll-Free' \r\n" + 
			"      LEFT JOIN sc_asset scoutt ON scrt.sc_related_asset_id=scoutt.id\r\n" + 
			"      JOIN sc_asset_attributes scas ON scoutr.id=scas.sc_asset_id AND scas.attribute_name='supplierId'\r\n" + 
			"      Left JOIN sc_asset_attributes sca ON scrt.sc_related_asset_id=sca.sc_asset_id AND sca.attribute_name='isPortNumber'\r\n" + 
    		"      WHERE sc.sc_service_detail_id=:serviceId AND gfg.id=:gscFlowGroupId AND scoutr.status=:status ORDER BY sc.id", nativeQuery = true)
	List<GscScAssetReserved> findByServiceIdandAssetId(Integer serviceId,Integer gscFlowGroupId, String status);
	
	List<ScAsset> findByIdIn(List<Integer> assetIds);
	
	List<ScAsset> findByIdInAndStatus(List<Integer> assetIds,String status);

	List<ScAsset> findByScServiceDetail_idAndStatus(Integer serviceId, String status);

	@Query(value = "SELECT sc.id AS TollfreeId, sc.name AS TollfreeName, sc.`type` AS TollfreeType\r\n" + 
			"       FROM sc_asset sc, sc_asset_attributes saa WHERE sc.id = saa.sc_asset_id\r\n" + 
			"       and sc.sc_service_detail_id=:serviceId AND sc.`type`='Toll-Free' AND saa.attribute_name='isPortNumber'\r\n" + 
			"		AND saa.attribute_value=:isPortingNum ORDER BY sc.id", nativeQuery = true)
	List<GscScAsset> getPortingNumWithNoOutpulse(int serviceId, String isPortingNum);

	List<ScAsset> findByScServiceDetail_idAndType(Integer serviceId, String type);

}
