package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceAssetAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
/**
* Repository for asset additional info view
* @author Srinivasa Raghavan
*/
@Repository
public interface SIServiceAssetAdditionalInfoRepository extends JpaRepository<SIServiceAssetAdditionalInfo, Integer> {

    List<SIServiceAssetAdditionalInfo> findByAssetSysIdInAndAttributeNameIn(List<Integer> assetSysIds, List<String> attributeName);

}
