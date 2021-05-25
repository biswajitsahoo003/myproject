package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.PricingIpcRateCard;

@Repository
public interface PricingIpcRateCardRepository extends JpaRepository<PricingIpcRateCard, Integer> {

	List<PricingIpcRateCard> findAll(Specification<PricingIpcRateCard> pricingIpcRateCardSpecification);

	@Query(value = "SELECT * FROM pricing_ipc_ratecard a \r\n"
			+ "WHERE a.customer_id = :customerId AND a.region = :region AND a.country = :country \r\n"
			+ "GROUP BY a.item_id \r\n" + "UNION \r\n" + "SELECT * FROM pricing_ipc_ratecard b \r\n"
			+ "WHERE b.customer_id = :customerId AND b.region IS NULL AND b.country = :country \r\n"
			+ "AND b.item_id NOT IN (SELECT a.item_id FROM pricing_ipc_ratecard a\r\n"
			+ "WHERE a.customer_id = :customerId AND a.region = :region AND a.country = :country) \r\n" + "UNION \r\n"
			+ "SELECT * FROM pricing_ipc_ratecard c \r\n"
			+ "WHERE c.customer_id IS NULL AND c.region IS NULL AND c.country = :country \r\n"
			+ "AND c.item_id NOT IN (SELECT a.item_id FROM pricing_ipc_ratecard a \r\n"
			+ "WHERE a.customer_id = :customerId AND a.region = :region AND a.country = :country \r\n"
			+ "GROUP BY a.item_id \r\n" + "UNION \r\n" + "SELECT b.item_id FROM pricing_ipc_ratecard b \r\n"
			+ "WHERE b.customer_id = :customerId AND b.region IS NULL AND b.country = :country \r\n"
			+ "AND b.item_id NOT IN (SELECT a.item_id FROM pricing_ipc_ratecard a \r\n"
			+ "WHERE a.customer_id = :customerId AND a.region = :region AND a.country = :country) ) \r\n"
			+ "ORDER BY item_id ", nativeQuery = true)
	List<PricingIpcRateCard> findByCustomerAndRegionAndCountry(@Param("customerId") Integer customerId,
			@Param("region") String region, @Param("country") String country);
}
