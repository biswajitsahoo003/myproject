package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaCardDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NwaCardDetailsRepository extends JpaRepository<NwaCardDetails, Integer> {

    @Query(value = "select * from nwa_card_details where order_id = :orderId ",nativeQuery = true)
    NwaCardDetails findByOrderId(Integer orderId);
}
