package com.tms.repository;

import com.tms.entity.AssignmentQuestionsBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AssignmentQuestionsRepository extends JpaRepository<AssignmentQuestionsBO,Long> {

	



}