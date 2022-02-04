package com.tms.service;

import java.util.List;

import com.tms.api.model.GetTeacherQuestionLibraryInfo;
import com.tms.api.model.TeacherQuestionLibraryInfo;

public interface TeacherQuestionLibrary {

	public abstract void createTeacherQuestionLibrary(TeacherQuestionLibraryInfo teacherQuestionLibrary);

	public abstract List<GetTeacherQuestionLibraryInfo> getTeacherQuestionLibrary(String teacherId);

	public abstract String updateTeacherQuestionLibrary(GetTeacherQuestionLibraryInfo updateeacherQuestionLibrary);

}
