package com.tms.repository;

import com.tms.entity.HomeworkBatchBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface HomeworkBatchRepository extends JpaRepository<HomeworkBatchBO,Long> {

	



}