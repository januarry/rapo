package com.tms.repository;

import com.tms.entity.QuestionBO;
import com.tms.entity.QuestionOptionBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOptionBO,Integer> {

    QuestionOptionBO  findByQuestionIdAndId(QuestionBO question,Integer optionId);


}