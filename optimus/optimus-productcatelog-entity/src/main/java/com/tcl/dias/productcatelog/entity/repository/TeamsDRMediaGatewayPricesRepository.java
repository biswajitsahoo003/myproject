package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDRMediaGatewayPrices;

/**
 * Repository for retrieving Media Gateway prices of TeamsDR Product
 * 
 * @author Srinivasa Raghavan
 *
 */
@Repository
public interface TeamsDRMediaGatewayPricesRepository extends JpaRepository<TeamsDRMediaGatewayPrices, Integer> {

	TeamsDRMediaGatewayPrices findByModelCd(String modelCd);

	List<TeamsDRMediaGatewayPrices> findByVendor(String vendor);

	/**
	 * Query to find price by vendor and number of users as parameters
	 * 
	 * @param vendor
	 * @param users
	 * @return
	 */
	@Query(value = "SELECT v from TeamsDRMediaGatewayPrices v where v.vendor = :vendor and :users between v.minUsr and v.maxUsr")
	TeamsDRMediaGatewayPrices findByVendorAndUsers(String vendor, Integer users);
}
