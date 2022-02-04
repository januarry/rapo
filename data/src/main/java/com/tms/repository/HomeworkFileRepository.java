package com.tms.repository;

import java.util.List;

import com.tms.entity.HomeworkBO;
import com.tms.entity.HomeworkFilesBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface HomeworkFileRepository extends JpaRepository<HomeworkFilesBO,Long> {

    List<HomeworkFilesBO> findByHomeWorkId(HomeworkBO homeWorkId);


}