package com.tms.repository;

import com.tms.entity.HomeworkOrientationBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface HomeworkOrientationRepository extends JpaRepository<HomeworkOrientationBO,Long> {

	



}