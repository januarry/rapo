package com.tms.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.tms.configuration.JwtUtils;
import com.tms.entity.AssignmentQuestionsBO;
import com.tms.entity.AssignmentSectionBO;
import com.tms.repository.AssignmentQuestionsRepository;
import com.tms.repository.SectionRepository;
import com.tms.repository.StudentAssignmentQuestionsRepository;
import com.tms.service.StudentAssignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAssignmentImpl implements StudentAssignment {

    @Autowired
    AssignmentQuestionsRepository assignmentQuestionsRepository;

    @Autowired
    StudentAssignmentQuestionsRepository studentAssignmentQuestionsRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    JwtUtils jwtUtils;

    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public void createAssignmentQuestions(List<String> questionIdList,boolean isMylibrary, Long assignmentSectionId) {
        // TODO Auto-generated method stub
       

        AssignmentSectionBO assignmentSection = sectionRepository.findById(assignmentSectionId).orElse(null);

        for (String questionId : questionIdList) {
            AssignmentQuestionsBO assignmentQuestions = new AssignmentQuestionsBO();

            assignmentQuestions.setAssignmentSection(assignmentSection);
            assignmentQuestions.setQuestionId(String.valueOf(questionId));
            assignmentQuestions.setCreatedOn(new Date());
            assignmentQuestions.setCreatedBy(jwtUtils.getUserId());
            assignmentQuestions.setMylibrary(isMylibrary);
            assignmentQuestions.setActive(true);
            assignmentQuestionsRepository.save(assignmentQuestions);
        }

    }

}
