package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;

/** 
 * Repository. This file holds repository methods of ScProductDetailRepoitory
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface ScProductDetailRepository extends JpaRepository<ScProductDetail, Integer> {

	public List<ScProductDetail> findByScServiceDetailId(Integer scServiceDetailId);

	public Optional<ScProductDetail> findByScServiceDetailIdAndSolutionName(Integer scServiceDetailId,
			String solutionName);

	public ScProductDetail findByCloudCode(String cloudCode);

	public ScProductDetail findFirstByParentCloudCodeOrderByIdDesc(String parentCloudCode);

	public ScProductDetail findFirstByCloudCodeOrderByIdDesc(String cloudCode);
}
