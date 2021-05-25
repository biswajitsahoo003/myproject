package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Jpa Repository class of Opportunity(Partner) and its entity
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {

    /**
     * Find Opportunity by order code / UUID
     *
     * @param orderCode
     * @return {@link Opportunity}
     */
    Opportunity findByUuid(String orderCode);


    @Query(value ="SELECT o.uuid as quoteCode, olprod.name as productName, o.tps_opty_id as optyId,\n" +
            "o.created_date as optyCreatedDate, o.created_by as optyCreatedBy \n" +
            "from opportunity o\n" +
            "LEFT JOIN engagement_to_opportunity eto on o.id = eto.opty_id \n" +
            "LEFT JOIN engagement e on eto.engagement_id = e.id \n" +
            "LEFT JOIN mst_order_na_lite_product_family olprod on olprod.id = e.product_family_id\n" +
            "where e.partner_id in (:partnerIds) and o.is_active=:status and o.is_orderlite='Y' and e.customer_id in (:customerId)\n" +
            "and (e.erf_cus_customer_le_id in(:custLeIds) or e.erf_cus_customer_le_id is null)\n" +
            "and (olprod.id = (:productId) or (:productId) is null)\n" +
            "group by o.uuid order by o.created_date desc",
            countQuery = "SELECT o.uuid as quoteCode, olprod.name as productName, o.tps_opty_id as optyId,\n" +
            "o.created_date as optyCreatedDate, o.created_by as optyCreatedBy \n" +
            "from opportunity o\n" +
            "LEFT JOIN engagement_to_opportunity eto on o.id = eto.opty_id \n" +
            "LEFT JOIN engagement e on eto.engagement_id = e.id \n" +
            "LEFT JOIN mst_order_na_lite_product_family olprod on olprod.id = e.product_family_id\n" +
            "where e.partner_id in (:partnerIds) and o.is_active=:status and o.is_orderlite='Y' and e.customer_id in (:customerId)\n" +
            "and (e.erf_cus_customer_le_id in(:custLeIds) or e.erf_cus_customer_le_id is null)\n" +
            "group by o.uuid order by o.created_date desc",nativeQuery = true)
    Page<Map<String,Object>> findActiveOrderLiteOpportunity(@Param("customerId") List<Integer> customerId, @Param("custLeIds") Set<Integer> customerLeIds,@Param("partnerIds") Set<Integer> partnerIds,@Param("productId") Integer productId, @Param("status") String status,Pageable pageable);

}
