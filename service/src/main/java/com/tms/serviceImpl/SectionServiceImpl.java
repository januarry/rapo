package com.tms.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tms.api.model.SectionList;
import com.tms.api.model.SectionListSection;
import com.tms.configuration.JwtUtils;
import com.tms.entity.AssignmentQuestionsBO;
import com.tms.entity.AssignmentSectionBO;
import com.tms.entity.HomeworkBO;
import com.tms.repository.AssignmentQuestionsRepository;
import com.tms.repository.HomeworkRepository;
import com.tms.repository.SectionRepository;

import com.tms.service.SectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SectionServiceImpl implements SectionService {
  @Autowired
  SectionRepository sectionRepository;

  @Autowired
  HomeworkRepository homeworkRepository;

  @Autowired
  AssignmentQuestionsRepository assignmentQuestionsRepository;

  @Autowired
  JwtUtils jwtUtils;

  @Override
  public String addSection(Long homeWorkId, String sectionName) {

    HomeworkBO homework = homeworkRepository.findById(homeWorkId).orElse(null);
    AssignmentSectionBO s = new AssignmentSectionBO();
    if (homework != null) {

      homework.setType("ONLINE");
      homework.setUpdatedOn(new Date());
      homework.setUpdatedBy(jwtUtils.getUserId());
      homeworkRepository.save(homework);
    } else {
      return "homework id dosen't found in systmem";
    }

    s.setHomeWorkId(homework);
    s.setActive(true);
    s.setSectionName(sectionName);
    s.setCreatedOn(new Date());
    s.setUpdatedOn(new Date());
    s.setCreatedBy(jwtUtils.getUserId());
    s.setUpdatedBy(jwtUtils.getUserId());
    sectionRepository.save(s);
    return "SUCCESS";
  }

  @Override
  public String updateSection(Long id, String sectionName) {
    AssignmentSectionBO s = sectionRepository.findById(id).orElse(null);

    s.setActive(true);
    s.setSectionName(sectionName);
    s.setUpdatedOn(new Date());
    sectionRepository.save(s);
    return "SUCCESS";

  }

  @Override
  public String deleteSection(Long id) {

    AssignmentSectionBO s = sectionRepository.findById(id).orElse(null);
    s.setActive(false);
    s.setUpdatedOn(new Date());
    s.setUpdatedBy(jwtUtils.getUserId());
    sectionRepository.save(s);
    return "SUCCESS";

  }

  @Override
  public String sectionMarks(Long id, Float positiveMarks, Float negativeMarks) {

    AssignmentSectionBO s = sectionRepository.findById(id).orElse(null);
    s.setPositiveMarks(positiveMarks);
    s.setNegativeMarks(negativeMarks);
    s.setUpdatedOn(new Date());
    s.setUpdatedBy(jwtUtils.getUserId());
    sectionRepository.save(s);
    return "SUCCESS";

  }

  @Override
  public List<SectionList> sectionList(Long homeWorkId) {

    List<SectionList> sectionLists = new ArrayList<>();

    List<AssignmentSectionBO> assignmentSections = sectionRepository.findByHomeWorkId(homeWorkId);

    if (assignmentSections != null) {

      for (AssignmentSectionBO assignmentSection : assignmentSections) {
        SectionListSection sectionList = new SectionListSection();

        SectionList section = new SectionList();

        sectionList.setSectionId(assignmentSection.getId());
        sectionList.setSectionName(assignmentSection.getSectionName());
        sectionList.setPositiveMarks(assignmentSection.getPositiveMarks());
        sectionList.setNegativeMarks(assignmentSection.getNegativeMarks());
        sectionList.setSectionName(assignmentSection.getSectionName());
        section.addSectionItem(sectionList);
        sectionLists.add(section);

      }
    }
    return sectionLists;

  }

  @Override

  public String marksQuestion(Long id, Float positiveMarks, Float negativeMarks) throws Exception {

    AssignmentSectionBO a = sectionRepository.findById(id).orElse(null);
    if (a != null) {

      a.setPositiveMarks(positiveMarks);
      a.setNegativeMarks(negativeMarks);
      a.setActive(true);
      a.setUpdatedOn(new Date());
      sectionRepository.save(a);
    } else {
      throw new Exception("QuestionId not present");

    }

    return "success";

  }

  @Override
  public String deleteQuestion(Long id) {
    AssignmentQuestionsBO a = assignmentQuestionsRepository.findById(id).orElse(null);

    if (a != null) {
      a.setActive(false);
      a.setUpdatedOn(new Date());
      assignmentQuestionsRepository.save(a);
    }

    return "success";

  }

}
