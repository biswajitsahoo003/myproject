package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.IzoPcSlaView;
import com.tcl.dias.productcatelog.entity.entities.IzoPcSlaViewId;

/**
 *Repository class for IzoPcSlaView entity
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface IzoPcSlaViewRepository extends JpaRepository<IzoPcSlaView,IzoPcSlaViewId>{
	

}
