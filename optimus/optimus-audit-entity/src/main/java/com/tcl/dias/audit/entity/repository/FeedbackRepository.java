/**
 * 
 */
package com.tcl.dias.audit.entity.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.audit.entity.entities.Feedback;

/**
 * @author kthayuma
 *
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {

	Page<Feedback> findAllByOrderByCreatedTimeDesc(Pageable pageable);
}
