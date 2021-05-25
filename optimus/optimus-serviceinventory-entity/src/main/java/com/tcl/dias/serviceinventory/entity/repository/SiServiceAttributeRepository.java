package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;

@Repository
public interface SiServiceAttributeRepository extends JpaRepository<SIServiceAttribute, Integer>{
	
	public List<SIServiceAttribute> findBySiServiceDetail(SIServiceDetail siServiceDetail);

	SIServiceAttribute findBySiServiceDetailAndAttributeNameOrderByIdDesc(SIServiceDetail siServiceDetail, String attrName);
	
	@Query(value="select sisa.SI_service_detail_id as serviceDetailId, sisa.attribute_name as attribute_name,sisa.attribute_value as attribute_value from  si_service_attributes sisa where sisa.SI_service_detail_id in (:siServiceDetail) and sisa.attribute_name =:attrName ", nativeQuery=true)
	public List<Map<String,Object>> findBySiServiceDetailIdInAndAttributeName(Set<Integer> siServiceDetail, String attrName);
	
	@Query(value="select sisa.SI_service_detail_id as serviceDetailId, sisa.attribute_name as attribute_name,sisa.attribute_value as attribute_value from  si_service_attributes sisa where sisa.SI_service_detail_id =:siServiceDetailId and sisa.attribute_name =:attrName ", nativeQuery=true)
	public List<Map<String,Object>> findBySiServiceDetailIdAndAttributeName(Integer siServiceDetailId, String attrName);

	List<SIServiceAttribute> findBySiServiceDetail_TpsServiceId(String serviceId);
	@Query(value = "select * from si_service_attributes where SI_service_detail_id =:siServiceDetail and attribute_value =:attributeValue;",nativeQuery=true)
	SIServiceAttribute findBySiServiceDetailId(@Param("siServiceDetail")Integer siServiceDetail, @Param("attributeValue") String attributeValue);
	
	List<SIServiceAttribute> findBySiServiceDetail_IdInAndAttributeNameIn(List<Integer> serviceDetailIds, List<String> attributeNames);

	@Query(value = "Select attrib.id, attrib.attribute_value, tps_service_id from si_service_attributes attrib left join si_service_detail det on det.id = attrib.SI_service_detail_id where tps_service_id in (:sdwanServiceIds) and attribute_name =:attrName", nativeQuery = true)
	List<Map<String, Object>> findByTpsServiceIdAndAttributeName(@Param ("sdwanServiceIds") Set<String> sdwanServiceIds, @Param ("attrName") String attrName);
	
	@Query(value="select * from si_service_attributes where attribute_name=:attributeName and SI_service_detail_id in (:serviceDetailId);", nativeQuery=true)
	SIServiceAttribute findByServiceDetailIdAndAttributeName(@Param ("attributeName") String attributeName,@Param ("serviceDetailId") Integer serviceDetailId);
	
	SIServiceAttribute findBySiServiceDetail_IdInAndAttributeNameIn(Integer serviceDetailIds, String attributeNames);
	
	@Query(value="select * from si_service_attributes where attribute_name=:attributeNames and id in (:serviceDetailIds);", nativeQuery=true)
	List<SIServiceAttribute> findByIdInAndAttributeNameInQuery(String attributeNames,List<Integer> serviceDetailIds);

	@Query(value = "SELECT tps_service_id FROM si_service_attributes attr LEFT JOIN si_service_detail det on det.id = attr.SI_service_detail_id WHERE attribute_name = :attrName AND attribute_value = :attrValue", nativeQuery=true)
	List<String> findTpsServiceIdByAttrNameAndAttrValue(@Param("attrName") String attrName, @Param("attrValue") String attrValue);

	public List<SIServiceAttribute> findBySiServiceDetailAndCategoryIn (SIServiceDetail siServiceDetail, List<String> category);
	
	public List<SIServiceAttribute> findBySiServiceDetailIdAndCategoryIn (Integer siServiceDetailId, List<String> category);
}
