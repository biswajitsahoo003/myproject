package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaRejectionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NwaRejectionDetailsRepository extends JpaRepository<NwaRejectionDetails, Integer> {


    NwaRejectionDetails findByOrderId(Integer orderId);

    NwaRejectionDetails findByRejectionToTaskAndOrderId(String taskDefKey, Integer orderId);

}
