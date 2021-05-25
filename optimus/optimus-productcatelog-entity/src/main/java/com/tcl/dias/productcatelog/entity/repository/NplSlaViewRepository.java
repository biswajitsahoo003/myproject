package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcl.dias.productcatelog.entity.entities.NplSlaView;
import com.tcl.dias.productcatelog.entity.entities.NplSlaViewId;

/**
 * Repository class for NPL SLA View
 * 
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface NplSlaViewRepository extends JpaRepository<NplSlaView, NplSlaViewId> {

	List<NplSlaView> findByServiceVarientAndAccessTopology(String serviceVarient, String accessTopology);
	
}
