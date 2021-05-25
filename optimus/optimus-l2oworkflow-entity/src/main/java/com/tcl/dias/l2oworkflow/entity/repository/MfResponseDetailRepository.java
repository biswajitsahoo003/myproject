package com.tcl.dias.l2oworkflow.entity.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MfResponseDetail;
/**
 * 
 * This file contains the MfResponseDetailRepository.java class.
 * 
 *
 * @author Kruthika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MfResponseDetailRepository extends JpaRepository<MfResponseDetail, Integer> {

    List<MfResponseDetail> findBySiteId(Integer siteId);

    List<MfResponseDetail> findByQuoteId(Integer quoteId);
    
    void deleteByIdAndTaskId(@Param("id")Integer id, @Param("task_id") Integer TaskID);
    List<MfResponseDetail> findBytaskId(Integer tasID);
    List<MfResponseDetail> findBytaskIdAndFeasibilityType( Integer TaskID, String feasibilityType);
    
    List<MfResponseDetail> findBySiteIdAndTaskId(Integer siteId,Integer TaskID);
    MfResponseDetail findBySiteIdAndTypeAndIsSelected(Integer siteId,String type,Integer isSelected);
    
    MfResponseDetail findBySiteIdAndIsSelected(Integer siteId,Integer isSelected);

    
}
