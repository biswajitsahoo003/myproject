package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.CpeBomGscBomAttributesView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGscBomAttributesViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CpeBomGscAdditionalAttributesRepository extends JpaRepository<CpeBomGscBomAttributesView, CpeBomGscBomAttributesViewId> {

	List<CpeBomGscBomAttributesView> findByCountryNameAndProductCategory(String country, String productCategory);

	CpeBomGscBomAttributesView findByCountryNameAndProductCategoryAndProductCode(String country, String productCategory, String productCode);
}
