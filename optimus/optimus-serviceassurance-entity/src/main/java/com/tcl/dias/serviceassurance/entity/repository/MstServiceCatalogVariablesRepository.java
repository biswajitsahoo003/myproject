package com.tcl.dias.serviceassurance.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcl.dias.serviceassurance.entity.entities.MstServiceCatalogVariables;
@Repository
public interface MstServiceCatalogVariablesRepository extends JpaRepository<MstServiceCatalogVariables, Integer>{

}
