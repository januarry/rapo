package com.tms.repository;



import java.util.List;

import com.tms.entity.AssignmentSectionBO;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SectionRepository extends JpaRepository<AssignmentSectionBO, Long >{
  AssignmentSectionBO findById(AssignmentSectionBO Long);
  
  @Query(value = "SELECT a.* from assignment_section a where a.active=1 and a.home_work_id=?1", nativeQuery = true)
  List<AssignmentSectionBO> findByHomeWorkId(Long homeWorkId);

  }
