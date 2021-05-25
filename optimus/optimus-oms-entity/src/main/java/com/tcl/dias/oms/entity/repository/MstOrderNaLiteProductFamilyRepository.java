package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tcl.dias.oms.entity.entities.MstOrderNaLiteProductFamily;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.BitSet;
import java.util.List;

/**
 * This file contains the MstProductRepository.java class.
 * Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface MstOrderNaLiteProductFamilyRepository extends JpaRepository<MstOrderNaLiteProductFamily, Integer> {

    @Query(value="select * from mst_order_na_lite_product_family where id=:id",nativeQuery = true)
    public MstOrderNaLiteProductFamily findOrderLiteProduceById(@Param("id") Integer id);


    @Query(value = "SELECT olprod.* from opportunity o\n" +
            "LEFT JOIN engagement_to_opportunity eto on o.id = eto.opty_id \n" +
            "LEFT JOIN engagement e on eto.engagement_id = e.id \n" +
            "LEFT JOIN mst_order_na_lite_product_family olprod on olprod.id = e.product_family_id\n" +
            "where e.partner_id in (:partnerId) and o.is_active=:status and o.is_orderlite='Y' " +
            "group by olprod.id", nativeQuery = true)
    List<MstOrderNaLiteProductFamily> findAllOrderLiteProductByPartnerIds(@Param("partnerId") List<String> partnerId,@Param("status") String status);
}
