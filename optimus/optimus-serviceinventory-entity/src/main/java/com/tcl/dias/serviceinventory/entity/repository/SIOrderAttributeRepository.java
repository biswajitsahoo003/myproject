package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIOrderAttribute;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * This file contains the repository class for SIOrderAttributeRepository.java class.
 * 
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface SIOrderAttributeRepository extends JpaRepository<SIOrderAttribute, Integer> {

    SIOrderAttribute findFirstBySiOrderAndAttributeNameOrderByIdDesc(SIOrder siOrder, String attributeName);
    
    SIOrderAttribute findFirstBySiOrderIdAndAttributeNameOrderByIdDesc(Integer siOrderId, String attributeName);
    
    
    List<SIOrderAttribute> findBySiOrder(SIOrder siOrder);

}
