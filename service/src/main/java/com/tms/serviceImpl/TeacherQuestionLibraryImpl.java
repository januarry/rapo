package com.tms.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.tms.api.model.GetTeacherQuestionLibraryInfo;
import com.tms.api.model.QuestionOptionInfo;
import com.tms.api.model.SubjectInfo;
import com.tms.api.model.TeacherQuestionLibraryInfo;
import com.tms.configuration.JwtUtils;
import com.tms.entity.AssignmentSectionBO;
import com.tms.entity.QuestionBO;
import com.tms.entity.QuestionOptionBO;
import com.tms.entity.QuestionTypeBO;
import com.tms.repository.QuestionOptionRepository;
import com.tms.repository.QuestionRepository;
import com.tms.repository.QuestionTypeRepository;
import com.tms.repository.SectionRepository;
import com.tms.service.TeacherQuestionLibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

@Service
public class TeacherQuestionLibraryImpl implements TeacherQuestionLibrary {

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	QuestionOptionRepository questionOptionRepository;

	@Autowired
	QuestionTypeRepository questionTypeRepository;

	@Autowired
	SectionRepository sectionRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public void createTeacherQuestionLibrary(TeacherQuestionLibraryInfo library) {

		try {
			// QuestionBO question =
			// questionRepository.findByQuestionId(library.getQuestionId());
			QuestionTypeBO questionType = questionTypeRepository.findByQuestionTypeId(library.getQuestionTypeId());
			AssignmentSectionBO assignmentSection = sectionRepository
					.findById(Long.valueOf(library.getAssignSectionId())).orElse(null);
			jwtUtils.headers.set("Authorization", jwtUtils.getToken());
			jwtUtils.entity = new HttpEntity(jwtUtils.headers);
			SubjectInfo subjectInfo = jwtUtils.restTemplateUtil.getForEntity(SubjectInfo.class,
					jwtUtils.getURL(jwtUtils.smsBaseUrl + "/api/v1/subject?subjectId={subjectId}"), jwtUtils.entity,
					library.getSubjectId());
			QuestionBO question = new QuestionBO();
			UUID uuid = UUID.randomUUID();
			question.setUuid(uuid.toString());
			question.setText(library.getText());
			question.setPositiveMarks(library.getPositiveMarks());
			question.setNegativeMarks(library.getNegativeMarks());
			question.setSolution(library.getSolution());
			question.setHint(library.getHint());
			question.setQuestionTypeId(questionType);
			question.setQuestionStatus(library.getQuestionStatus());
			question.setSubjectId(subjectInfo.getSubjectId() != null ? Long.valueOf(subjectInfo.getSubjectId()) : 0);
			question.setAssignSectionId(assignmentSection);
			question.setCreatedOn(new Date());
			question.setCreatedBy(jwtUtils.getUserId() != null ? jwtUtils.getUserId() : null);
			question.setUpdatedOn(new Date());
			question.setUpdatedBy(jwtUtils.getUserId() != null ? jwtUtils.getUserId() : null);
			questionRepository.save(question);
			String[] answers = !library.getAnswer().isEmpty() ? library.getAnswer().split(",") : null;
			if (library.getQuestionOption().size() > 0) {
				int count = 0;
				for (String option : library.getQuestionOption()) {
					Boolean answerStatus = getAnswer(count, answers);
					QuestionOptionBO optionBO = new QuestionOptionBO();
					optionBO.setQuestionId(question);
					optionBO.setQuestionOption(option);
					optionBO.setAnswer(answerStatus);
					questionOptionRepository.save(optionBO);
					count++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean getAnswer(int value, String[] answers) {
		ArrayList<String> answerList = new ArrayList<String>(Arrays.asList(answers));
		Boolean answerStatus = false;
		String ansVal = "";
		if (value == 0) {
			ansVal = "A";
		} else if (value == 1) {
			ansVal = "B";
		} else if (value == 2) {
			ansVal = "C";
		} else if (value == 3) {
			ansVal = "D";
		} else if (value == 4) {
			ansVal = "E";
		}
		if (answerList.contains(ansVal)) {
			answerStatus = true;
		}

		return answerStatus;
	}

	@Override
	public List<GetTeacherQuestionLibraryInfo> getTeacherQuestionLibrary(String teacherId) {

		List<GetTeacherQuestionLibraryInfo> infoList = new ArrayList<GetTeacherQuestionLibraryInfo>();

		List<QuestionBO> libraryList = questionRepository.findByCreatedBy(teacherId);
		infoList = libraryList.stream().map(a -> new GetTeacherQuestionLibraryInfo()
				.text(a.getText()).positiveMarks(a.getPositiveMarks())
				.questionId(a.getQuestionId()).questionTypeId(a.getQuestionTypeId().getQuestionTypeId())
				.negativeMarks(a.getNegativeMarks()).solution(a.getSolution())
				.subjectId(a.getSubjectId())
				.assignSectionId(a.getAssignSectionId().getId())
				.questionOption(a.getQuestionOptions().stream()
						.map(b -> new QuestionOptionInfo().optionId(b.getId())
								.option(b.getQuestionOption()).answer(b.getAnswer()))
						.collect(Collectors.toList()))
				.hint(a.getHint()).questionStatus(a.getQuestionStatus())).collect(Collectors.toList());

		return infoList;

	}

	@Override
	public String updateTeacherQuestionLibrary(GetTeacherQuestionLibraryInfo library) {
		String updateStatus="";
		try {
			QuestionBO question = questionRepository.findByQuestionId(library.getQuestionId());
			if (question != null) {
				QuestionTypeBO questionType = questionTypeRepository.findByQuestionTypeId(library.getQuestionTypeId());
				AssignmentSectionBO assignmentSection = sectionRepository
						.findById(Long.valueOf(library.getAssignSectionId())).orElse(null);
				jwtUtils.headers.set("Authorization", jwtUtils.getToken());
				jwtUtils.entity = new HttpEntity(jwtUtils.headers);
				SubjectInfo subjectInfo = jwtUtils.restTemplateUtil.getForEntity(SubjectInfo.class,
						jwtUtils.getURL(jwtUtils.smsBaseUrl + "/api/v1/subject?subjectId={subjectId}"), jwtUtils.entity,
						library.getSubjectId());
				question.setText(library.getText());
				question.setPositiveMarks(library.getPositiveMarks());
				question.setNegativeMarks(library.getNegativeMarks());
				question.setSolution(library.getSolution());
				question.setHint(library.getHint());
				question.setQuestionTypeId(questionType);
				question.setQuestionStatus(library.getQuestionStatus());
				question.setSubjectId(
						subjectInfo.getSubjectId() != null ? Long.valueOf(subjectInfo.getSubjectId()) : 0);
				question.setAssignSectionId(assignmentSection);
				question.setUpdatedOn(new Date());
				question.setUpdatedBy(jwtUtils.getUserId() != null ? jwtUtils.getUserId() : null);
				questionRepository.save(question);
				if (library.getQuestionOption().size() > 0) {

					for (QuestionOptionInfo option : library.getQuestionOption()) {
						QuestionOptionBO optionBO = questionOptionRepository.findByQuestionIdAndId(question,
								option.getOptionId());
						optionBO.setQuestionOption(option.getOption());
						optionBO.setAnswer(option.getAnswer());
						questionOptionRepository.save(optionBO);

					}
				}
				updateStatus="Updated Successfully";
				return updateStatus;

			}else{
				updateStatus="Question Library not available!!!";
				return updateStatus;
			}
		} catch (Exception e) {
			e.printStackTrace();
			updateStatus="Question Library not available!!!";
			return updateStatus;
		}

	}
}
