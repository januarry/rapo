package com.tms.repository;

import com.tms.entity.AssignmentFileBO;
import com.tms.entity.StudentAssignmentBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AssignmentFileRepository extends JpaRepository<AssignmentFileBO,Long> {

	AssignmentFileBO findByAssignmentId(StudentAssignmentBO stuAssign);



}