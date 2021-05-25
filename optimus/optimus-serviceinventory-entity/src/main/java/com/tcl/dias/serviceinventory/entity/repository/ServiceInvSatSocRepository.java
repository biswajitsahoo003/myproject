package com.tcl.dias.serviceinventory.entity.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SatSocId;
import com.tcl.dias.serviceinventory.entity.entities.VwServiceInvSatSoc;

@Repository
public interface ServiceInvSatSocRepository extends JpaRepository<VwServiceInvSatSoc, SatSocId> {

    @Query(value = "select * from vw_service_inv_SatSoc where SERVICE_ID=:serviceId", nativeQuery = true)
    public Map<String, Object> findByServiceId(@Param("serviceId") String serviceId);

    @Procedure(procedureName = "proc_service_inv_SatSoc", outputParameterName = "out")
    public Object[] procServiceInvSatSoc(@Param("service_id") String serviceCode);
  

}
