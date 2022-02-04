package com.tms.repository;

import com.tms.entity.StudentAssignmentQuestionsBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface StudentAssignmentQuestionsRepository extends JpaRepository<StudentAssignmentQuestionsBO,Long> {

	



}