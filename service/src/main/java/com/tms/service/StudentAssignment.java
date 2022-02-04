package com.tms.service;

import java.util.List;

public interface StudentAssignment {

	public abstract void createAssignmentQuestions(List<String> questionId,boolean isMylibrary, Long assignmentSectionId);

}
