package com.tms.repository;

import com.tms.entity.HomeworkBranchBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface HomeworkBranchRepository extends JpaRepository<HomeworkBranchBO,Long> {

	



}