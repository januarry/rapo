package com.tms.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import com.tms.api.model.AssignmentTestPaper;
import com.tms.api.model.CreationHomework;
import com.tms.api.model.HomeworkList;
import com.tms.api.model.StudentHomeworkEvaluation;
import com.tms.api.model.StudentHomeworkEvaluationList;
import com.tms.api.model.StudentHomeworkSubmissions;

public interface TeacherHomework {

	public abstract List<HomeworkList> getHomeworkLists() throws UnsupportedEncodingException;

	public abstract List<StudentHomeworkEvaluationList> studentHomeworkEvaluation(Long homeworkId);

	public abstract List<StudentHomeworkEvaluation> homeworkEvaluationStuList(Long homeworkId, String evaluationType);

	public abstract StudentHomeworkSubmissions studentHomeworkEvaluationView(Long homeworkId)
			throws UnsupportedEncodingException;

	public abstract Long createHomework(CreationHomework creationHomework) throws ParseException;

	public abstract Long setUploadedFileComments(Long uploadFileId, String comments);

	public abstract void deleteUploadedFileComments(Long homeworkFileCommentId);

	public abstract void setAssignmentRemarks(Long assignmentId, String remarks, Float marks);

	public abstract void deleteHomework(Long homeworkId) throws ParseException;

	public abstract List<AssignmentTestPaper> getStudentAssignmentTest(Long studentAssignmentId);

	public abstract List<AssignmentTestPaper> getHomeworkAssignmentTest(Long homeworkId);

	public abstract void deleteHomeworkFile(Long homeworkfileId) throws ParseException;

}
