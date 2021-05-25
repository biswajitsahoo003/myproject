package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ProCreation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProCreationRepository extends JpaRepository<ProCreation, Integer> {

    Optional<ProCreation> findByPrNumber(String prNumber);
    Optional<ProCreation> findByPoNumber(String poNumber);
    ProCreation findFirstByServiceIdAndServiceCodeAndVendorCodeAndComponentId(Integer serviceId,String uuid,String vendorCode,Integer cpeComponentId);
    ProCreation findFirstByServiceIdAndServiceCodeAndVendorCodeAndComponentIdAndType(Integer serviceId,String uuid,String vendorCode,Integer cpeComponentId,String type);
    List<ProCreation> findByServiceIdAndServiceCodeAndVendorCodeAndComponentIdAndTypeIn(Integer serviceId,String uuid,String vendorCode,Integer cpeComponentId,List<String> type);
    List<ProCreation> findFirstByServiceIdAndServiceCodeAndComponentIdAndTypeIn(Integer serviceId,String uuid,Integer cpeComponentId,List<String> type);
    @Query(value = "select vendor_code as vendorCode,group_concat(id) as ids from pro_creation where service_id=:serviceId and service_code =:uuid and component_id=:cpeComponentId and type in(:type) group by vendorCode", nativeQuery = true)
    List<Map<String,String>> getIdAndVendorCode(Integer serviceId,String uuid,Integer cpeComponentId,List<String> type);
    ProCreation findFirstByServiceIdAndServiceCodeAndVendorCodeAndComponentIdAndTypeOrderByIdDesc(Integer serviceId,String uuid,String vendorCode,Integer cpeComponentId,String type);

}
