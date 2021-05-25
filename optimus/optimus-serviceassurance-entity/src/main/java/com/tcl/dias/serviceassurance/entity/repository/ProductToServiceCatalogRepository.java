package com.tcl.dias.serviceassurance.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcl.dias.serviceassurance.entity.entities.ProductToServiceCatalog;

@Repository
public interface ProductToServiceCatalogRepository extends JpaRepository<ProductToServiceCatalog, Integer>{

	public List<ProductToServiceCatalog> findByErfPrdcatalogProductName(String productName);
}
