package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.GscCountrySpecificDocumentList;

@Repository
public interface GscCountrySpecificDocumentListRepository extends JpaRepository<GscCountrySpecificDocumentList, Integer> {

	Optional<GscCountrySpecificDocumentList> findByDocumentName(String name);

	Optional<GscCountrySpecificDocumentList> findByDocumentNameAndProductNameAndCountryName(String string, String string2, String string3);

	List<GscCountrySpecificDocumentList> findByProductNameAndCountryCode(String productName, String threeDigitCountryCode);
}
