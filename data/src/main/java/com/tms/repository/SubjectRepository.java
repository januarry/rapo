package com.tms.repository;

import com.tms.entity.SubjectMasterBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SubjectRepository extends JpaRepository<SubjectMasterBO,Long> {


}