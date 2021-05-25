package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderNplLinkSla;

/**
 * This file contains the OrderNplLinkSlaRepository.java class. - repository for
 * OrderNplLinkSla entity
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderNplLinkSlaRepository extends JpaRepository<OrderNplLinkSla, Integer> {
   public List<OrderNplLinkSla> findByOrderNplLink(OrderNplLink orderNplLink);
   
   @Query(value="SELECT sm.sla_name ,osa.sla_value FROM order_npl_link_sla osa ,sla_master sm where sm.id=osa.sla_master_id and osa.order_npl_link_id=:linkId",nativeQuery=true)
   List<Map<String,String>> findByLinkId(@Param("linkId") Integer linkId);
}
