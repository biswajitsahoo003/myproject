package com.tcl.dias.serviceassurance.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceassurance.entity.entities.ServiceCatalogToVariablesMap;

@Repository
public interface ServiceCatalogToVariablesMapRepository extends JpaRepository<ServiceCatalogToVariablesMap, Integer>{
	public List<ServiceCatalogToVariablesMap> findByCatalogId(Integer catalogId);
}
