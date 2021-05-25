package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceAdditionalInfo;

/**
 * Repository for vw_service_additional_info view
 * 
 * @author Srinivasa Raghavan
 */
@Repository
public interface SIServiceAdditionalInfoRepository extends JpaRepository<SIServiceAdditionalInfo, Integer> {

	/**
	 * finding template details by SDWAN service id
	 *
	 * @param sdwanServiceIds
	 * @return
	 */
	@Query(value = "SELECT si_info.attribute_sys_id as template_id, si_info.attribute_value as template_name, service_info.srv_service_id as srv_service_id FROM vw_service_additional_info si_info\n"
			+ "left join (select sys_id, srv_service_id from vw_si_service_info_all where srv_product_family_id =:productId and srv_service_id in (:sdwanServiceIds)) as service_info on service_info.sys_id = si_info.srv_sys_id where si_info.attribute_name = 'Sdwan_template_name'", nativeQuery = true)
	List<Map<String, Object>> findTemplateBySdwanServiceIds(@Param("sdwanServiceIds") List<String> sdwanServiceIds,  @Param("productId")  Integer productId);

	Optional<SIServiceAdditionalInfo> findBySysIdAndAttributeName(Integer id, String attributeName);

	List<SIServiceAdditionalInfo> findByAttributeNameAndAttributeValue(String attributeName, String attributeValue);

	@Query(value = "SELECT add_info.attribute_value as name, srv_sys_id as sysId, disp_val as dispVal FROM  vw_service_additional_info add_info where add_info.srv_sys_id in (:sysIds) and add_info.attribute_name = :attributeName and attribute_value is not null group by attribute_value", countQuery = "SELECT count(add_info.attribute_value) FROM  vw_service_additional_info add_info where add_info.srv_sys_id in (:sysIds) and add_info.attribute_name = :attributeName and attribute_value is not null group by attribute_value", nativeQuery = true)
	Page<Map<String, Object>> findTemplateNameBySysIdPaginated(List<Integer> sysIds, String attributeName,
			Pageable pageable);

	@Query(value = "SELECT add_info.attribute_value as name, srv_sys_id as sysId, disp_val as dispVal FROM  vw_service_additional_info add_info where add_info.srv_sys_id in (:sysIds) and add_info.attribute_name = :attributeName and attribute_value is not null and (add_info.attribute_value like %:searchText% or (add_info.disp_val like %:searchText%)) group by attribute_value", countQuery = "SELECT count(add_info.attribute_value) FROM  vw_service_additional_info add_info where add_info.srv_sys_id in (:sysIds) and add_info.attribute_name = :attributeName and attribute_value is not null and (add_info.attribute_value like %:searchText% or (add_info.disp_val like %:searchText%)) group by attribute_value", nativeQuery = true)
	Page<Map<String, Object>> findBySysIdAndSearchTextPaginated(List<Integer> sysIds, String attributeName,
			String searchText, Pageable pageable);

	@Query(value = "SELECT add_info.attribute_value as name, srv_sys_id as sysId, disp_val as dispVal FROM  vw_service_additional_info add_info where add_info.srv_sys_id in (:sysIds) and add_info.attribute_name = :attributeName and attribute_value is not null group by attribute_value", nativeQuery = true)
	List<Map<String, Object>> findBySysIdAndAttributeNameGroupByName(List<Integer> sysIds, String attributeName);

	@Query(value = "SELECT add_info FROM  SIServiceAdditionalInfo add_info where add_info.sysId in (:sysIds) and add_info.attributeName = :attributeName and attributeValue is not null")
	List<SIServiceAdditionalInfo> findBySysIdAndAttributeName(List<Integer> sysIds, String attributeName);

	@Query(value = "SELECT si_info.attribute_sys_id as attributeId, si_info.attribute_name as attributeName, si_info.attribute_value as attributeValue,\r\n" +
			"service_info.srv_service_id as serviceId FROM vw_service_additional_info si_info \r\n"+
			"right join (select sys_id, srv_service_id from vw_si_service_info_all \r\n" +
			"LEFT JOIN vw_service_inv_ui_exclusion siuv ON srv_service_id = siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"where siuv.service_id IS NULL and srv_product_family_id =:productId \r\n" +
			"and srv_service_id in (:sdwanServiceIds)) as service_info on service_info.sys_id = si_info.srv_sys_id where si_info.attribute_name in (:attributes)", nativeQuery = true)
	List<Map<String, Object>> findAttributesBySdwanServiceIds(@Param("sdwanServiceIds") List<String> sdwanServiceIds,
			@Param("attributes") List<String> attributes, @Param("productId")  Integer productId);
	
	@Query(value = "select vw.sys_id as sysId, vw.srv_service_id as serviceId, vw.izo_sdwan_srvc_id as izoSdwanSrvcId, \r\n" +
			"attr.attribute_sys_id as attributeId, attr.attribute_name as attributeName, \r\n" +
			"attr.attribute_value as attributeValue, attr.disp_val as displayValue\r\n" +
			"from vw_si_service_info_all vw \r\n" +
			"left JOIN vw_service_inv_ui_exclusion siuv ON izo_sdwan_srvc_id = siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"left join vw_service_additional_info attr on attr.srv_sys_id = vw.sys_id \r\n" +
			"where siuv.service_id IS NULL and \r\n" +
			"vw.sys_id  in (:underlayIds) and attr.attribute_name in (:attributeNames)", nativeQuery = true)
	List<Map<String, Object>> findAttributesByUnderlayServiceIds(@Param("underlayIds") List<Integer> sdwanServiceIds,
			@Param("attributeNames") List<String> attributeNames);
}
