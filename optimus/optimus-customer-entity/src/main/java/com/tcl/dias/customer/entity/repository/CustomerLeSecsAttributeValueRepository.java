package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.CustomerLeAttributeValue;
import com.tcl.dias.customer.entity.entities.CustomerLeSecsAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This file is the repository for CustomerLeSecs AttributeValue Details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface CustomerLeSecsAttributeValueRepository extends JpaRepository<CustomerLeSecsAttributeValue, Integer> {

    List<CustomerLeSecsAttributeValue> findByCustomerLegalEntity_IdAndMstLeAttribute_Id(Integer customerLegalEntityId, Integer mstLeAttributeId);

    @Query(value = "SELECT * from customer_le_secs_attribute_values cus where cus.customer_le_id = :customerLeId " +
            "and cus.mst_le_attributes_id = :mstLeAttributesId and cus.customer_le_secs_id = :secsId " +
            "and (cus.product_name is null or cus.product_name = '' or cus.product_name = :productName)", nativeQuery = true)
    CustomerLeSecsAttributeValue findByCustomerLeIdAndSecsIdAndMstLeAttributesIdAndProductName(
            @Param("customerLeId") Integer customerLeId, @Param("secsId") Integer secsId, @Param("mstLeAttributesId") Integer mstLeAttributesId,
            @Param("productName") String productName);

    @Query(value = "SELECT a.* from customer_le_secs_attribute_values a , mst_le_attributes b where a.customer_le_id=:customerLeId " +
            "and a.customer_le_secs_id=:secsId and a.mst_le_attributes_id=b.id and (b.name='TSA' or b.name='Service Addendum') " +
            "and a.attribute_values=:attachmentId", nativeQuery = true)
    List<CustomerLeSecsAttributeValue> findByCustomerLeIdAndSecsIdAndAttachmentId(
            @Param("customerLeId") Integer customerLeId,
            @Param("secsId") Integer secsId,
            @Param("attachmentId") String attachmentId);

}
