package com.tcl.dias.serviceinventory.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.ViewSiComponentAttributes;

@Repository
public interface ViewSiComponentAttributesRepository extends JpaRepository<ViewSiComponentAttributes, Integer>  {

}
