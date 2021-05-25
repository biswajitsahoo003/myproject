package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.AttributeValueGroupAssoc;

/**
 * 
 * @author Biswajit Sahoo
 *
 */
@Repository
public interface AttributeValueGroupAssocRepository extends JpaRepository<AttributeValueGroupAssoc, Integer> {


}
