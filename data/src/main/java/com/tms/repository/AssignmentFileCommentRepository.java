package com.tms.repository;

import java.util.List;

import com.tms.entity.AssignmentFileCommentBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentFileCommentRepository extends JpaRepository<AssignmentFileCommentBO, Long> {

    List<AssignmentFileCommentBO> findByAssignmentFile(Long assignmentfileId);

}