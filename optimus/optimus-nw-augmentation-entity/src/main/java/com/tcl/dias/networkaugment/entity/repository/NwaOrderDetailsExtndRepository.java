package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaOrderDetailsExtnd;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.management.openmbean.OpenDataException;
import java.util.List;
import java.util.Optional;

@Repository
public interface NwaOrderDetailsExtndRepository extends CrudRepository<NwaOrderDetailsExtnd,Integer> {
    public NwaOrderDetailsExtnd findByOrderCode(String orderCode);

    @Query(value = "select next from NwaOrderDetailsExtnd next inner join next.scOrder o where o.id= :orderId")
    public NwaOrderDetailsExtnd findByScOrderId(Integer orderId);

    @Query(value = "select next from NwaOrderDetailsExtnd next inner join next.scOrder o where o.id= :orderId")
    Optional<NwaOrderDetailsExtnd> findByOrderId(Integer orderId);

}
