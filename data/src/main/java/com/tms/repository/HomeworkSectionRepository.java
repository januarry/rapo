package com.tms.repository;

import com.tms.entity.HomeworkSectionBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface HomeworkSectionRepository extends JpaRepository<HomeworkSectionBO,Long> {

	



}