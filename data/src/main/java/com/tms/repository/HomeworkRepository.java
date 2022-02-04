package com.tms.repository;

import com.tms.entity.HomeworkBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface HomeworkRepository extends JpaRepository<HomeworkBO,Long> {

    List<HomeworkBO> findByActive(boolean active);
}