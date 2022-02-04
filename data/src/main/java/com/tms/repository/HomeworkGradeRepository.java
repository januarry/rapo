package com.tms.repository;

import com.tms.entity.HomeworkGradeBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface HomeworkGradeRepository extends JpaRepository<HomeworkGradeBO,Long> {

	



}