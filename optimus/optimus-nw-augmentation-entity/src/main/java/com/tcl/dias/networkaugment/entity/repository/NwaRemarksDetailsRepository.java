package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaLinkedOrderDetails;
import com.tcl.dias.networkaugment.entity.entities.NwaRejectionDetails;
import com.tcl.dias.networkaugment.entity.entities.NwaRemarksDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NwaRemarksDetailsRepository extends CrudRepository<NwaRemarksDetails, Integer> {

    @Query(value="select * from nwa_remarks_details where order_code = :orderCode ",nativeQuery = true)
    public NwaRemarksDetails findByOrderCode(String orderCode);

    @Query(value="select * from nwa_remarks_details where task_name = :taskName ",nativeQuery = true)
    public NwaRemarksDetails findByTaskName(String taskName);

    @Query(value = "SELECT reason FROM nwa_remarks_details WHERE order_code = :orderCode AND task_name :taskName",nativeQuery = true)
    public NwaRemarksDetails findByOrderCodeAndByTaskName(String orderCode, String taskName);

    @Query(value = "SELECT reason FROM nwa_remarks_details WHERE order_id = :serviceId AND task_name :taskName",nativeQuery = true)
    public NwaRemarksDetails findByOrderIdAndByTaskName(Integer serviceId, String taskName);

}
