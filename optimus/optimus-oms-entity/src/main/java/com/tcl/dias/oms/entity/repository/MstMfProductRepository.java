package com.tcl.dias.oms.entity.repository;
import com.tcl.dias.oms.entity.entities.MstMfProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * For fetching the data from the "mst_mf_product" table
 * @author Chetan Chaudhary
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstMfProductRepository extends JpaRepository<MstMfProductEntity, Integer> {
    public List<MstMfProductEntity> findAll();
}
