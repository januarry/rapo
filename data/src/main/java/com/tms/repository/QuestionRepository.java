package com.tms.repository;

import java.util.List;

import com.tms.entity.QuestionBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface QuestionRepository extends JpaRepository<QuestionBO,Integer> {

    QuestionBO findByQuestionId(Integer questionId);
    List<QuestionBO> findByCreatedBy(String createdBy);

}