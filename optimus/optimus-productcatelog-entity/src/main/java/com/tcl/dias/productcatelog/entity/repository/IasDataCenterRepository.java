package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.ViewIasDataCenterSam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IasDataCenterRepository extends JpaRepository<ViewIasDataCenterSam, Integer> {

        List<ViewIasDataCenterSam> findByDcTypeAndIsActiveAndIdIn(String dcType,String activeFlag,List<Integer> dataCenterIds);

        List<ViewIasDataCenterSam> findByDcTypeAndIsActive(String dcType, String activeFlag);

        List<ViewIasDataCenterSam> findByDcTypeAndTownsDtlAndIsActive(String dcType,String townName,String activeFlag);

@Query(value = "select distinct Towns_dtl from vw_ias_DC_sam where dc_type='IDC' and is_active_ind ='Y'", nativeQuery = true)
	List<String> findDistinctCitiesForDC();
        }

