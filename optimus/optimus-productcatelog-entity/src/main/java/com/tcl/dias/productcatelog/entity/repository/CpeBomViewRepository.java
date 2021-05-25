package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.CpeBomView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomViewId;

/**
 * Repository class for CpeBomView entity for database operations
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface CpeBomViewRepository extends JpaRepository<CpeBomView, CpeBomViewId> {

	public List<CpeBomView> findByBomTypeAndPortInterfaceAndRoutingProtocolAndMaxBandwidthGreaterThanEqual(
			String bomType, String portInterface, String routingProtocol, Integer bandwidth);

	public List<CpeBomView> findByBomTypeAndMaxBandwidthGreaterThanEqual(String bomType, Integer bandwidth);

}
