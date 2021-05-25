package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaLinkedOrderDetails;
import org.springframework.data.repository.CrudRepository;

public interface NwaLinkedOrderDetailsRepository extends CrudRepository<NwaLinkedOrderDetails, Integer> {

    public NwaLinkedOrderDetails findByOrderCode(String OrderCode);

}
