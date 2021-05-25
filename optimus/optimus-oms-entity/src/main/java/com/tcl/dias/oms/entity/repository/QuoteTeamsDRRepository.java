package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import com.tcl.dias.oms.entity.entities.QuoteToLe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteTeamsDR;

/**
 * Repository for quoteTeamsDR
 * 
 * @author Srinivas Raghavan
 *
 */
@Repository
public interface QuoteTeamsDRRepository extends JpaRepository<QuoteTeamsDR, Integer> {

	List<QuoteTeamsDR> findByQuoteToLeId(Integer quoteToLeId);

	List<QuoteTeamsDR> findByProductSolutionAndStatus(ProductSolution solution, Byte status);

	QuoteTeamsDR findByProductSolutionIdAndStatus(Integer productSolutionId, Byte status);

	/**
	 * To query based no plan
	 * 
	 * @param quoteToLeId
	 * @return
	 */
	@Query(value = "select s from QuoteTeamsDR s where s.quoteToLe.id=:quoteToLeId and s.profileName like '%plan%'")
	List<QuoteTeamsDR> findByPlan(@Param("quoteToLeId") Integer quoteToLeId);

	Optional<QuoteTeamsDR> findByQuoteToLeIdAndIsConfig(Integer quoteToLeId, Byte isConfig);

	Optional<QuoteTeamsDR> findByQuoteToLeIdAndServiceName(Integer quoteToLeId,String serviceName);

	List<QuoteTeamsDR> findByQuoteToLeIdAndServiceNameIn(Integer quoteToLeId, List<String> serviceNames);

	@Query(value = "select s from QuoteTeamsDR s where s.quoteToLe.id = :quoteToLeId and s.profileName=:profileName and s.serviceName is null")
	QuoteTeamsDR findPlanByQuoteToLeAndProfile(@Param("quoteToLeId") Integer quoteToLeId, @Param("profileName") String profileName);

	@Query(value = "select s from QuoteTeamsDR s where s.quoteToLe.id = :quoteToLeId and s.serviceName != :serviceName")
	List<QuoteTeamsDR> findByQuoteToLeAndNotServiceName(@Param("quoteToLeId") Integer quoteToLeId, @Param("serviceName") String serviceName);

	@Query(value = "select s from QuoteTeamsDR s where s.quoteToLe.id = :quoteToLeId and s.serviceName not in (:serviceNames)")
	List<QuoteTeamsDR> findByQuoteToLeAndNotInServiceNames(@Param("quoteToLeId") Integer quoteToLeId, @Param("serviceNames") List<String> serviceNames);
}
