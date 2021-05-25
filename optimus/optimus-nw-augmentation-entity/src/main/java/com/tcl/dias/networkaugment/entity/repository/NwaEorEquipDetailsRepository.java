package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaEorEquipDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NwaEorEquipDetailsRepository extends CrudRepository<NwaEorEquipDetails,Integer> {

    @Query(value="select * from nwa_eor_equip_details where order_code = :orderCode ",nativeQuery = true)
    public NwaEorEquipDetails findByOrderCode(String orderCode);

    //public NwaEorEquipDetails findByOrderId(Integer orderId);
}
