package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.TeamsDRProductComponentPrice;

/**
 * Repository for teams DR product component prices
 * 
 * @author Srinivasa Raghavan
 */
@Repository
public interface TeamsDRProductComponentPriceRepository extends JpaRepository<TeamsDRProductComponentPrice, Integer> {

	/**
	 * Find by offeringName
	 */
	TeamsDRProductComponentPrice findBychargeNm(String name);

	/**
	 * Find by offeringName and number of users
	 * 
	 * @param name
	 * @param users
	 */
	@Query(value = "SELECT v FROM TeamsDRProductComponentPrice v where v.chargeNm = :name and :users between v.minUsrRange and v.maxUsrRange")
	TeamsDRProductComponentPrice findBychargeNmAndUsers(@Param("name") String name, @Param("users") Integer users);

}