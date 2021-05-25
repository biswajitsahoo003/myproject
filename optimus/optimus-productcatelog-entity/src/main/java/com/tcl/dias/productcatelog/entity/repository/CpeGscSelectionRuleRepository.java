package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.CpeGscSelectionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CpeGscSelectionRuleRepository extends JpaRepository<CpeGscSelectionRule, Integer> {

	@Query(value = "select * from vw_cpe_gsc_selection_rule where bandwidth_mbps >= :bandwidth and cube_sessions_num >= :cubeLicenses", nativeQuery = true)
	List<CpeGscSelectionRule> findSuitableCpeByBandwidthAndCubeLicenses(String bandwidth, Integer cubeLicenses);

	@Query(value = "select * from vw_cpe_gsc_selection_rule where bandwidth_mbps >= :bandwidth and max_mft_card_num >= :noOfMFTCards", nativeQuery = true)
	List<CpeGscSelectionRule> findSuitableCpeByBandwidthAndNoOfMftCards(String bandwidth, Integer noOfMFTCards);

	@Query(value = "select * from vw_cpe_gsc_selection_rule where bandwidth_mbps >= :bandwidth", nativeQuery = true)
	List<CpeGscSelectionRule> findSuitableCpeByBandwidth(String bandwidth);

	List<CpeGscSelectionRule> findSuitableCpeByBandWidthMbpsGreaterThanEqualAndIsPassthroughFlag(Integer bandwidth, String isPassThroughFlagBasedOnTyepOfCpe);
}
