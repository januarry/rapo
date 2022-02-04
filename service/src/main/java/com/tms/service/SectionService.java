package com.tms.service;

import java.util.List;

import com.tms.api.model.SectionList;

public interface SectionService {
    public String addSection(Long homeWorkId, String sectionname);

    public String updateSection(Long id, String sectionName);

    public String deleteSection(Long id);

    public String sectionMarks(Long id, Float positiveMarks, Float negativeMarks);

    public List<SectionList> sectionList(Long homeWorkId);

    public String marksQuestion(Long id, Float positiveMarks, Float negativeMarks) throws Exception;

    public abstract String deleteQuestion(Long id);
}
