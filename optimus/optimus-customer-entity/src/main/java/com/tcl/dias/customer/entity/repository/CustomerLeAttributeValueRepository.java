package com.tcl.dias.customer.entity.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CustomerLeAttributeValue;
import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.entities.MstLeAttribute;

/**
 * This file is the repository for CustomerLe AttributeValue Details
 * 
 * @author SEKHAR ER
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CustomerLeAttributeValueRepository extends JpaRepository<CustomerLeAttributeValue, Integer> {

	public List<CustomerLeAttributeValue> findByCustomerLegalEntity(CustomerLegalEntity cuLe);

	public List<CustomerLeAttributeValue> findByCustomerLegalEntity_IdAndMstLeAttribute_Id(
			Integer customerLegalEntityId, Integer mstLeAttributeId);

	@Query(value = "SELECT * from customer_le_attribute_values a where a.customer_le_id=:customerLeId and a.mst_le_attributes_id=:mstLeAttributesId and (a.product_name is null or a.product_name = '' or a.product_name=:productName)", nativeQuery = true)
	Collection<CustomerLeAttributeValue> findByCustomerLeIdAndMstLeAttributesIdAndProductName(
			@Param("customerLeId") Integer customerLeId, @Param("mstLeAttributesId") Integer mstLeAttributesId,
			@Param("productName") String productName);

	List<CustomerLeAttributeValue> findByCustomerLegalEntity_Id(Integer custLegalId);
	
	public List<CustomerLeAttributeValue> findByCustomerLegalEntityAndProductNameAndMstLeAttribute(CustomerLegalEntity customerLegalEntity,String productName,MstLeAttribute mstLeAttribute);
	
	@Query(value = "SELECT *\n" +
			"from customer_le_attribute_values a\n" +
			"where a.customer_le_id = :customerLeId\n" +
			"  and a.product_name = :productName\n" +
			"group by mst_le_attributes_id\n" +
			"union\n" +
			"select *\n" +
			"from customer_le_attribute_values a\n" +
			"where a.customer_le_id = :customerLeId\n" +
			"  and (a.product_name = '' or a.product_name is null)\n" +
			"  and a.mst_le_attributes_id not in (SELECT a.mst_le_attributes_id\n" +
			"                                     from customer_le_attribute_values a\n" +
			"                                     where a.customer_le_id = :customerLeId\n" +
			"                                       and a.product_name = :productName)", nativeQuery = true)
	List<CustomerLeAttributeValue> findByCustomerLeIdAndProductName(
			@Param("customerLeId") Integer customerLeId,
			@Param("productName") String productName);
	
	@Query(value = "SELECT a.* from customer_le_attribute_values a , mst_le_attributes b  where a.customer_le_id=:customerLeId and a.mst_le_attributes_id=b.id and (b.name='MSA' or b.name='Service Schedule') and a.attribute_values= :attachmentId", nativeQuery = true)
	List<CustomerLeAttributeValue> findByCustomerLeIdAndAttachmentId(
			@Param("customerLeId") Integer customerLeId,
			@Param("attachmentId") String attachmentId);

	@Query(value = "SELECT a.* from customer_le_attribute_values a , mst_le_attributes b  where a.customer_le_id=:customerLeId and a.mst_le_attributes_id=b.id and (b.name='MSA' or b.name='Service Schedule' or b.name='Service Schedule For Teamsdr with Voice') and a.attribute_values= :attachmentId", nativeQuery = true)
	List<CustomerLeAttributeValue> findByCustomerLeIdAndAttachmentIdForTeamsDR(
			@Param("customerLeId") Integer customerLeId,
			@Param("attachmentId") String attachmentId);
	
	List<CustomerLeAttributeValue> findByMstLeAttributeIn(List<MstLeAttribute> mstLeAttributesList);
	
	@Query(value="select distinct attribute_values from customer_le_attribute_values where mst_le_attributes_id=:mstLeAttributeId", nativeQuery = true)
    List<String> findByMstLeAttributeId(@Param("mstLeAttributeId") Integer mstLeAttributeId);

	@Query(value ="SELECT * FROM customer_le_attribute_values cla join mst_le_attributes mla on cla.mst_le_attributes_id=mla.id  where cla.customer_le_id=:customerLeId and mla.name='GST_Number'",nativeQuery = true)
	List<CustomerLeAttributeValue> findGstByCustomerLegalEntityId(@Param("customerLeId") Integer customerLeId);

	@Query(value ="SELECT cla.* FROM customer_le_attribute_values cla join mst_le_attributes mla on cla.mst_le_attributes_id=mla.id  where cla.customer_le_id=:customerLeId and mla.name=:attributeName",nativeQuery = true)
	List<CustomerLeAttributeValue> findAccountVerificationByCustomerLeId(@Param("customerLeId") Integer customerLeId,@Param("attributeName") String attributeName);

	List<CustomerLeAttributeValue> findByCustomerLegalEntity_IdAndMstLeAttribute_IdAndProductName(
			Integer customerLegalEntityId, Integer mstLeAttributeId,String productName);
}
