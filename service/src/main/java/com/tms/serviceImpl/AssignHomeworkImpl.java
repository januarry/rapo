package com.tms.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.tms.api.model.AssignQuestions;
import com.tms.api.model.AssignStudents;
import com.tms.api.model.AssignStudentsForHomework;
import com.tms.api.model.AssignmentTestPaper;
import com.tms.api.model.EmployeeInfo;
import com.tms.api.model.FileComment;
import com.tms.api.model.SectionAssignmentTest;
import com.tms.api.model.StudentAssignBatch;
import com.tms.api.model.StudentAssignList;
import com.tms.api.model.StudentBranch;
import com.tms.api.model.StudentClass;
import com.tms.api.model.StudentOrientaion;
import com.tms.api.model.StudentSetionInfo;
import com.tms.api.model.StudentUnassignList;
import com.tms.api.model.Students;
import com.tms.api.model.StudentsHomeworkView;
import com.tms.api.model.SubjectInfo;
import com.tms.configuration.JwtUtils;
import com.tms.entity.AssignmentFileBO;
import com.tms.entity.AssignmentFileCommentBO;
import com.tms.entity.AssignmentQuestionsBO;
import com.tms.entity.AssignmentSectionBO;
import com.tms.entity.HomeworkBO;
import com.tms.entity.HomeworkBatchBO;
import com.tms.entity.HomeworkBranchBO;
import com.tms.entity.HomeworkFilesBO;
import com.tms.entity.HomeworkGradeBO;
import com.tms.entity.HomeworkOrientationBO;
import com.tms.entity.HomeworkSectionBO;
import com.tms.entity.StudentAssignmentBO;
import com.tms.repository.AssignmentFileCommentRepository;
import com.tms.repository.AssignmentFileRepository;
import com.tms.repository.HomeworkFileRepository;
import com.tms.repository.HomeworkRepository;
import com.tms.repository.StudentAssignmentRepository;
import com.tms.repository.SubjectRepository;
import com.tms.service.AssignHomework;
import com.tms.service.EmployeeService;
import com.tms.util.DU;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class AssignHomeworkImpl implements AssignHomework {

	SimpleDateFormat formatDate = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss a");
	SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@Autowired
	HomeworkRepository homeworkRepository;

	@Autowired
	SubjectRepository subjectRepository;

	@Autowired
	StudentAssignmentRepository studentAssignmentRepository;

	@Autowired
	HomeworkFileRepository homeworkFileRepository;

	@Autowired
	AssignmentFileCommentRepository assignmentFileCommentRepository;

	@Autowired
	AssignmentFileRepository assignmentFileRepository;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	QBToken uamToken;

	@Override
	public void assignHomeworkForStudents(AssignStudentsForHomework assignStudentsForHomework) {

		HomeworkBO homeworkVal = homeworkRepository.findById(assignStudentsForHomework.getHomeworkId()).get();
		List<StudentAssignList> admNoList = assignStudentsForHomework.getStudentAssignList();
		List<StudentUnassignList> unAssignList = assignStudentsForHomework.getStudentUnassignList();
		for (StudentAssignList studentAssign : admNoList) {
			StudentAssignmentBO studentAssignmentVal = new StudentAssignmentBO();
			studentAssignmentVal.setAttemptCount(0l);
			studentAssignmentVal.setDownloadCount(0l);
			studentAssignmentVal.setHomeWorkId(homeworkVal);
			studentAssignmentVal.setIsCompleted(false);
			studentAssignmentVal.setCreatedBy(jwtUtils.getUserId());
			studentAssignmentVal.setCreatedOn(new Date());
			studentAssignmentVal.setUpdatedBy(jwtUtils.getUserId());
			studentAssignmentVal.setUpdatedOn(new Date());
			studentAssignmentVal.setStudent(studentAssign.getStudentAdmNo());
			studentAssignmentVal.setIsActive(true);
			studentAssignmentRepository.save(studentAssignmentVal);

		}
		if (unAssignList.size() > 0) {
			for (StudentUnassignList studentAssign : unAssignList) {
				StudentAssignmentBO studentAssignmentVal = studentAssignmentRepository
						.findByStudentAndHomework(studentAssign.getStudentAdmNo(), homeworkVal.getId());
				studentAssignmentVal.setAttemptCount(0l);
				studentAssignmentVal.setDownloadCount(0l);
				studentAssignmentVal.setHomeWorkId(homeworkVal);
				studentAssignmentVal.setIsCompleted(false);
				studentAssignmentVal.setCreatedBy(jwtUtils.getUserId());
				studentAssignmentVal.setCreatedOn(new Date());
				studentAssignmentVal.setUpdatedBy(jwtUtils.getUserId());
				studentAssignmentVal.setUpdatedOn(new Date());
				studentAssignmentVal.setStudent(studentAssign.getStudentAdmNo());
				studentAssignmentVal.setIsActive(false);
				studentAssignmentRepository.save(studentAssignmentVal);

			}
		}
		if (homeworkVal.getStatus().equalsIgnoreCase("SAVE")) {
			homeworkVal.setStatus("ASSIGN-STUDENT");
			homeworkVal.setUpdatedOn(new Date());
			homeworkVal.setUpdatedBy(jwtUtils.getUserId());
			homeworkRepository.save(homeworkVal);
		}
	}

	@Override
	public StudentsHomeworkView assignStudentsForHomeworkView(Long homeworkId) throws UnsupportedEncodingException {

		StudentsHomeworkView stuhwView = new StudentsHomeworkView();

		HomeworkBO homework = homeworkRepository.findById(homeworkId).orElse(null);

		if (homework != null) {
			if (homework.getType().equalsIgnoreCase("OFFLINE")) {
				List<AssignQuestions> assignQuestions = new ArrayList<>();
				for (HomeworkFilesBO hfile : homework.getHomeWorkFiles()) {
					if (hfile.isActive()) {
						AssignQuestions assignquestion = new AssignQuestions();
						assignquestion.setAssignmentFiles(hfile.getLocation());
						assignquestion.setFileSize(BigDecimal.valueOf(hfile.getFileSize()));
						assignquestion.setFileType(hfile.getFileType());
						assignquestion.setName(hfile.getName());
						assignQuestions.add(assignquestion);
					}
				}
				stuhwView.setAssignQuestions(assignQuestions);
			} else {

				for (AssignmentSectionBO assignmentSection : homework.getAssignmentSections()) {

					if (assignmentSection.isActive()) {

						AssignmentTestPaper agnmentTestPaper = new AssignmentTestPaper();

						List<String> qids = new ArrayList<>();
						// HashMap<Long, String> aqids = new HashMap<>();
						for (AssignmentQuestionsBO assignmentQuestions : assignmentSection.getAssignmentQuestions()) {
							if (assignmentQuestions.isActive()) {
								qids.add(assignmentQuestions.getQuestionId());
							}

							// aqids.put(assignmentQuestions.getId(), assignmentQuestions.getQuestionId());
						}

						agnmentTestPaper.setSectionId(assignmentSection.getId());
						agnmentTestPaper.setSectionName(assignmentSection.getSectionName());

						// request body parameters
						HashMap<String, Object> map = new HashMap<>();
						map.put("questionid", qids);
						map.put("pageNumber", 1);
						map.put("size", 300);

						// build the header

						jwtUtils.headers.setContentType(MediaType.APPLICATION_JSON);
						jwtUtils.headers.set("Authorization", uamToken.getUAMToken());
						jwtUtils.entity = new HttpEntity<>(map, jwtUtils.headers);

						SectionAssignmentTest[] sectionAssignmentTest = jwtUtils.restTemplateUtil.getForEntityPost(
								SectionAssignmentTest[].class,
								jwtUtils.queBaseUrl,
								jwtUtils.entity);

						ArrayList<SectionAssignmentTest> arrayList = new ArrayList<SectionAssignmentTest>(
								Arrays.asList(sectionAssignmentTest));

						for (SectionAssignmentTest assignTest : arrayList) {

							assignTest.setQuestionId(assignmentSection.getQuestion(assignTest.getId()).getId());

						}

						// System.out.println(sectionAssignmentTest);
						agnmentTestPaper.assignmentTestPaper(sectionAssignmentTest);
						stuhwView.addAssignmentTestPapersItem(agnmentTestPaper);

					}
				}
			}

			List<StudentBranch> studentBranchs = new ArrayList<>();
			for (HomeworkBranchBO homeworkBranch : homework.getHomeworkBranchList()) {
				StudentBranch studentBranch = new StudentBranch();
				studentBranch.setBranchId(homeworkBranch.getBranchId());
				studentBranch.setBranchName(homeworkBranch.getBranchName());
				studentBranchs.add(studentBranch);
			}
			stuhwView.setStudentBranchs(studentBranchs);

			List<StudentClass> studentClasses = new ArrayList<>();
			for (HomeworkGradeBO homeworkGrade : homework.getHomeworkGradeList()) {
				StudentClass studentClass = new StudentClass();
				studentClass.setClassId(homeworkGrade.getGradeId());
				studentClass.setClassName(homeworkGrade.getGradeName());
				studentClasses.add(studentClass);
			}
			stuhwView.setStudentClasses(studentClasses);
			List<StudentSetionInfo> studentSections = new ArrayList<>();
			for (HomeworkSectionBO homeworkSection : homework.getHomeworkSectionList()) {
				StudentSetionInfo studentSection = new StudentSetionInfo();
				studentSection.setSectionId(homeworkSection.getSectionId());
				studentSection.setSectionName(homeworkSection.getSectionName());
				studentSections.add(studentSection);
			}
			stuhwView.setStudentSections(studentSections);

			List<StudentAssignBatch> studentBatches = new ArrayList<>();
			for (HomeworkBatchBO homeworkBranch : homework.getHomeworkBatchList()) {
				StudentAssignBatch studentBatch = new StudentAssignBatch();
				studentBatch.setBatchId(homeworkBranch.getBatchId());
				studentBatch.setBatchName(homeworkBranch.getBatchName());
				studentBatches.add(studentBatch);
			}
			stuhwView.setStudentBatches(studentBatches);

			List<StudentOrientaion> studentOrientationList = new ArrayList<>();
			for (HomeworkOrientationBO homeworkBranch : homework.getHomeworkOrientationList()) {
				StudentOrientaion studentOrientaion = new StudentOrientaion();
				studentOrientaion.setOrientaionId(homeworkBranch.getOrientationId());
				studentOrientaion.setOrientationName(homeworkBranch.getOrientationName());
				studentOrientationList.add(studentOrientaion);
			}
			stuhwView.setStudentOrientaion(studentOrientationList);

			stuhwView.setDescription(homework.getDescription());

			List<AssignStudents> assignStudents = new ArrayList<AssignStudents>();
			List<AssignStudents> assignStudentList = new ArrayList<AssignStudents>();

			Students studentList = getStudents();
			// System.out.println(studentList.getStudents().get(0).getAdmNo());

			// HashMap<Students,St

			for (StudentAssignmentBO stuassingn : homework.getStudentAssignments()) {
				if (stuassingn.getIsActive()) {
					AssignStudents assignStudent = new AssignStudents();
					assignStudentList = studentList.getStudents().stream()
							.filter(item -> (stuassingn.getStudent() != null
									? stuassingn.getStudent().contains(item.getAdmNo())
									: null))
							.map(a -> new AssignStudents().nameAndAdmNo(a.getAdmNo() + "-" + a.getName())
									.admNo(a.getAdmNo()).name(a.getName())
									.submissionDate(
											stuassingn.getAttemptEndTime() != null
													? DU.format(stuassingn.getAttemptEndTime())
													: null)
									.status(stuassingn.getIsCompleted() ? "Submitted" : "Not submitted")
									.evaluationStatus(stuassingn.getRemarks() != null ? "Done" : "-")
									.marksObtained(stuassingn.getAssignmentResult() != null
											&& !stuassingn.getAssignmentResult().isEmpty()
													? stuassingn.getAssignmentResult().get(0).getTotalMarks()
													: 0f))
							.collect(Collectors.toList());
					for (AssignStudents assignStudentVal : assignStudentList) {
						assignStudent.setNameAndAdmNo(assignStudentVal.getNameAndAdmNo());
						assignStudent.setAdmNo(assignStudentVal.getAdmNo());
						assignStudent.setName(assignStudentVal.getName());
						assignStudent.setSubmissionDate(assignStudentVal.getSubmissionDate());
						assignStudent.setStatus(assignStudentVal.getStatus());
						assignStudent.setMarksObtained(assignStudentVal.getMarksObtained());
						assignStudent.setEvaluationStatus(assignStudentVal.getEvaluationStatus());
					}

					assignStudents.add(assignStudent);
				}

			}

			EmployeeInfo employee = employeeService.getEmployee(homework.getCreatedBy());
			stuhwView.setCreatedby(employee != null ? employee.getEmployeeName() : null);
			stuhwView
					.setCreatedOn(
							homework.getCreatedOn() != null ? DU.format(homework.getCreatedOn(), "dd-MM-yyyy") : "-");
			stuhwView.setHomeWorkId(homework.getId());
			stuhwView.setHomeWorkName(homework.getName());
			stuhwView.setAssignStudents(assignStudents);
			stuhwView.setHomeWorkType(homework.getType());
			stuhwView.setHomeWorkStatus(homework.getStatus());
			stuhwView.setEndDate("" + homework.getEnd_date());
			stuhwView.setStartDate("" + homework.getStart_date());
			SubjectInfo subjectInfo = getSubjectInfo(homework);

			stuhwView.setHomeWorkSubject(subjectInfo != null ? subjectInfo.getSubjectName() : null);

			return stuhwView;
		} else {
			return stuhwView;
		}
	}

	protected Students getStudents() throws UnsupportedEncodingException {
		jwtUtils.headers.set("Authorization", jwtUtils.getToken());
		jwtUtils.setEntity(new HttpEntity(jwtUtils.headers));
		return jwtUtils.restTemplateUtil.getForEntity(Students.class,
				jwtUtils.getURL(jwtUtils.sasBaseUrl + "/api/v1/students"), jwtUtils.getEntity());
	}

	protected SubjectInfo getSubjectInfo(HomeworkBO homework) throws UnsupportedEncodingException {
		jwtUtils.headers.set("Authorization", jwtUtils.getToken());
		jwtUtils.entity = new HttpEntity(jwtUtils.headers);
		return jwtUtils.restTemplateUtil.getForEntity(SubjectInfo.class,
				jwtUtils.getURL(jwtUtils.smsBaseUrl + "/api/v1/subject?subjectId={subjectId}"), jwtUtils.entity,
				homework.getSubject());
	}

	@Override
	public String deletefileComments(Long assignmentfileId) {

		AssignmentFileCommentBO assignmentFile = assignmentFileCommentRepository.findById(assignmentfileId)
				.orElse(null);

		if (assignmentFile != null) {
			assignmentFile.setActive(false);
			assignmentFile.setUpdated_on(new Date());
		} else {
			return "assignment file comments are dosn't matching in system";
		}

		return "Success";
	}

	@Override
	public List<FileComment> getAssignmentFileComments(Long assignmentfileId) {

		List<FileComment> filecomments = new ArrayList<>();

		AssignmentFileBO assignmentFile = assignmentFileRepository.findById(assignmentfileId).orElse(null);

		for (AssignmentFileCommentBO assignmentFileComment : assignmentFile.getAssignmentFileComments()) {
			if (assignmentFileComment.isActive()) {

				FileComment filecoment = new FileComment();

				filecoment.setAssignmentFileId(assignmentFileComment.getId());
				filecoment.setComment(assignmentFileComment.getComments());
				filecomments.add(filecoment);
			}

		}

		return filecomments;
	}

}
