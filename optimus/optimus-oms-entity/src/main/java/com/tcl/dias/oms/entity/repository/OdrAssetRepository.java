package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.OdrAsset;
import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 *
 * This file contains repository class of OdrServiceDetail entity
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrAssetRepository extends JpaRepository<OdrAsset, Integer> {

    List<OdrAsset> findByOdrServiceDetail(OdrServiceDetail odrServiceDetail);
}