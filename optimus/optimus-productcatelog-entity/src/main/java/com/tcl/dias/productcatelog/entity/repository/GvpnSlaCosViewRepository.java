package com.tcl.dias.productcatelog.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.GvpnSlaCosView;
import com.tcl.dias.productcatelog.entity.entities.GvpnSlaCosViewId;

/**
 * @author NAVEEN GUNASEKARAN
 *
 */
@Repository
public interface GvpnSlaCosViewRepository extends JpaRepository<GvpnSlaCosView, GvpnSlaCosViewId> {

	Optional<GvpnSlaCosView> findByCosSchemaName(String cosSchemaNm);
	
	Optional<GvpnSlaCosView> findBySlaIdAndPopTierCd(Integer slaIdNo, String popTierCd);
}
