package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaAccessEorDetails;
import com.tcl.dias.networkaugment.entity.entities.NwaOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NwaAccessEorDetailsRepository extends JpaRepository<NwaAccessEorDetails,Integer> {
   //public NwaAccessEorDetails findByOrderId(Integer orderId);
   public NwaAccessEorDetails findByOrderCode(String orderCode);
}
