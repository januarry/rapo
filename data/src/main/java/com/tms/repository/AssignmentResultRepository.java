package com.tms.repository;

import com.tms.entity.AssignmentResultBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository          
public interface AssignmentResultRepository extends JpaRepository<AssignmentResultBO,Long> {
    
}
