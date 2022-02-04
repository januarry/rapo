package com.tms.repository;

import com.tms.entity.QuestionTypeBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionTypeBO,Integer> {
    QuestionTypeBO  findByQuestionTypeId(Integer questionTypeId);


}