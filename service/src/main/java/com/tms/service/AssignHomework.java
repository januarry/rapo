package com.tms.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.tms.api.model.AssignStudentsForHomework;
import com.tms.api.model.FileComment;
import com.tms.api.model.StudentsHomeworkView;

public interface AssignHomework {

	public abstract void assignHomeworkForStudents(AssignStudentsForHomework assignStudentsForHomework);

	public abstract StudentsHomeworkView assignStudentsForHomeworkView(Long homeworkId) throws UnsupportedEncodingException;

	public abstract String deletefileComments(Long assignmentfileId);

	public abstract List<FileComment> getAssignmentFileComments(Long assignmentfileId);

}
