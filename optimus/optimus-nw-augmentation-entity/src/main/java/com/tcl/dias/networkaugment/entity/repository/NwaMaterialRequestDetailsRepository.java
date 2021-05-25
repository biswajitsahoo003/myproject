package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaMaterialRequestDetails;
import org.springframework.data.repository.CrudRepository;

public interface NwaMaterialRequestDetailsRepository extends CrudRepository<NwaMaterialRequestDetails, Integer> {

    public NwaMaterialRequestDetails findByOrderCode(String orderCode);

}
