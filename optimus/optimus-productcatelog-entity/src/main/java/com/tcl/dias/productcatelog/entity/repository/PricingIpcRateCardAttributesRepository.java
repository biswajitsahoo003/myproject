package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.PricingIpcRateCardAttribute;

@Repository
public interface PricingIpcRateCardAttributesRepository extends JpaRepository<PricingIpcRateCardAttribute, Integer> {

	List<PricingIpcRateCardAttribute> findAll(
			Specification<PricingIpcRateCardAttribute> pricingIpcRateCardAttributeSpecification);

	@Query(value = "SELECT * FROM pricing_ipc_ratecard_attributes a \r\n"
			+ "WHERE a.customer_id = :customerId AND a.region = :region AND a.country = :country \r\n"
			+ "GROUP BY a.attribute_id \r\n" + "UNION \r\n" + "SELECT * FROM pricing_ipc_ratecard_attributes b \r\n"
			+ "WHERE b.customer_id = :customerId AND b.region IS NULL AND b.country = :country \r\n"
			+ "AND b.attribute_id NOT IN (SELECT a.attribute_id FROM pricing_ipc_ratecard_attributes a\r\n"
			+ "WHERE a.customer_id = :customerId AND a.region = :region AND a.country = :country) \r\n" + "UNION \r\n"
			+ "SELECT * FROM pricing_ipc_ratecard_attributes c \r\n"
			+ "WHERE c.customer_id IS NULL AND c.region IS NULL AND c.country = :country \r\n"
			+ "AND c.attribute_id NOT IN (SELECT a.attribute_id FROM pricing_ipc_ratecard_attributes a \r\n"
			+ "WHERE a.customer_id = :customerId AND a.region = :region AND a.country = :country \r\n"
			+ "GROUP BY a.attribute_id \r\n" + "UNION \r\n"
			+ "SELECT b.attribute_id FROM pricing_ipc_ratecard_attributes b \r\n"
			+ "WHERE b.customer_id = :customerId AND b.region IS NULL AND b.country = :country \r\n"
			+ "AND b.attribute_id NOT IN (SELECT a.attribute_id FROM pricing_ipc_ratecard_attributes a \r\n"
			+ "WHERE a.customer_id = :customerId AND a.region = :region AND a.country = :country) ) \r\n"
			+ "ORDER BY attribute_id ", nativeQuery = true)
	List<PricingIpcRateCardAttribute> findByCustomerAndRegionAndCountry(@Param("customerId") Integer customerId,
			@Param("region") String region, @Param("country") String country);
}
