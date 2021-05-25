package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.MstLeAttribute;
import com.tcl.dias.customer.entity.entities.PartnerLeAttributeValue;

/**
 * Partner Le Attribute Values Entity Repository Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerLeAttributeValueRepository extends JpaRepository<PartnerLeAttributeValue, Integer> {
    List<PartnerLeAttributeValue> findByPartnerLegalEntity_IdAndMstLeAttribute(Integer partnerLeId, MstLeAttribute mstLeAttributeId);
    List<PartnerLeAttributeValue> findByPartnerLegalEntity_IdAndMstLeAttribute_Id(Integer partnerLeId, Integer mstLeAttributeId);

    /**
     * Find By Partner Le ID and Attachment ID
     *
     * @param partnerLeId
     * @param attachmentId
     * @return {@link PartnerLeAttributeValue}
     */
    @Query(value="select * from partner_le_attribute_values attr where attr.partner_le_id = :partnerLeId and attr.attribute_values = :attachmentId",nativeQuery = true)
    Optional<PartnerLeAttributeValue> findByPartnerLeIdAndAttachmentId(@Param("partnerLeId") Integer partnerLeId,
                                                                       @Param("attachmentId") String attachmentId);

    /**
     * Find By Partner Legal Entity Id And MstLeAttribute And Product Name
     *
     * @param partnerLeId
     * @param mstLeAttributeId
     * @param productName
     * @return {@link PartnerLeAttributeValue}
     */
    PartnerLeAttributeValue findByPartnerLegalEntity_IdAndMstLeAttributeAndProductName(Integer partnerLeId, MstLeAttribute mstLeAttributeId, String productName);

    List<PartnerLeAttributeValue> findByPartnerLegalEntity_Id(Integer partnerLeId);
}
