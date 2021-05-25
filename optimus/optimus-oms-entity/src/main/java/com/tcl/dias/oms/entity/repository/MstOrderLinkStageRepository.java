package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStage;

/** 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */

@Repository
public interface MstOrderLinkStageRepository extends JpaRepository<MstOrderLinkStage, Integer>{
	public MstOrderLinkStage findByName(String name);
}
