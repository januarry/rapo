package com.tms.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.tms.api.model.AssignmentTestPaper;
import com.tms.api.model.CreationHomework;
import com.tms.api.model.Homework;
import com.tms.api.model.HomeworkList;
import com.tms.api.model.HomeworkStudentList;
import com.tms.api.model.HomeworkStudentsView;
import com.tms.api.model.SectionAssignmentTest;
import com.tms.api.model.StudentAssignmentFiles;
import com.tms.api.model.StudentBatch;
import com.tms.api.model.StudentBranch;
import com.tms.api.model.StudentClass;
import com.tms.api.model.StudentHomeworkEvaluation;
import com.tms.api.model.StudentHomeworkEvaluationList;
import com.tms.api.model.StudentHomeworkSubmissions;
import com.tms.api.model.StudentProgram;
import com.tms.api.model.StudentSection;
import com.tms.api.model.SubjectInfo;
import com.tms.api.model.TeacherAssignmentFiles;
import com.tms.configuration.JwtUtils;
import com.tms.entity.AssignmentFileBO;
import com.tms.entity.AssignmentFileCommentBO;
import com.tms.entity.AssignmentQuestionsBO;
import com.tms.entity.AssignmentResultBO;
import com.tms.entity.AssignmentSectionBO;
import com.tms.entity.HomeworkBO;
import com.tms.entity.HomeworkBatchBO;
import com.tms.entity.HomeworkBranchBO;
import com.tms.entity.HomeworkFilesBO;
import com.tms.entity.HomeworkGradeBO;
import com.tms.entity.HomeworkOrientationBO;
import com.tms.entity.HomeworkSectionBO;
import com.tms.entity.StudentAssignmentBO;
import com.tms.entity.StudentAssignmentQuestionsBO;
import com.tms.repository.AssignmentFileCommentRepository;
import com.tms.repository.AssignmentFileRepository;
import com.tms.repository.AssignmentResultRepository;
import com.tms.repository.HomeworkBatchRepository;
import com.tms.repository.HomeworkBranchRepository;
import com.tms.repository.HomeworkFileRepository;
import com.tms.repository.HomeworkGradeRepository;
import com.tms.repository.HomeworkOrientationRepository;
import com.tms.repository.HomeworkRepository;
import com.tms.repository.HomeworkSectionRepository;
import com.tms.repository.StudentAssignmentRepository;
import com.tms.repository.SubjectRepository;
import com.tms.service.TeacherHomework;
import com.tms.util.DU;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class TeacherHomeworkImpl implements TeacherHomework {

	@Autowired
	HomeworkRepository homeworkRepository;

	@Autowired
	SubjectRepository subjectRepository;

	@Autowired
	StudentAssignmentRepository studentAssignmentRepository;

	@Autowired
	HomeworkFileRepository homeworkFileRepository;

	@Autowired
	AssignmentFileRepository assignmentFileRepository;

	@Autowired
	AssignmentFileCommentRepository assignmentFileCommentRepository;

	@Autowired
	HomeworkOrientationRepository homeworkOrientationRepository;

	@Autowired
	HomeworkGradeRepository homeworkGradeRepository;

	@Autowired
	HomeworkBatchRepository homeworkBatchRepository;

	@Autowired
	HomeworkBranchRepository homeworkBranchRepository;

	@Autowired
	HomeworkSectionRepository homeworkSectionRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	AssignmentResultRepository assignmentResultRepository;

	@Autowired
	QBToken uamToken;

	SimpleDateFormat formateDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
	SimpleDateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa");

	@Override
	public List<HomeworkList> getHomeworkLists() throws UnsupportedEncodingException {
		List<HomeworkList> homeworkLists = new ArrayList<>();
		HomeworkList homeworkList = null;
		List<HomeworkBO> homeworksList = homeworkRepository.findByActive(true);
		Date presentDate = new Date();

		List<HomeworkBO> homeworksOngingList = homeworksList.stream()
				.filter(a -> presentDate.after(a.getStart_date())
						&& presentDate.before(a.getEnd_date()) && a.getStatus().equalsIgnoreCase("PUBLISH"))
				.sorted((h1, h2) -> (int) (h2.getId() - h1.getId()))
				.collect(Collectors.toList());

		List<HomeworkBO> homeworksUpcomingList = homeworksList.stream()
				.filter(a -> presentDate.before(a.getStart_date()) && a.getStatus().equalsIgnoreCase("PUBLISH"))
				.sorted((h1, h2) -> (int) (h2.getId() - h1.getId()))
				.collect(Collectors.toList());

		List<HomeworkBO> homeworksPastList = homeworksList.stream()
				.filter(a -> presentDate.after(a.getEnd_date()) && a.getStatus().equalsIgnoreCase("PUBLISH"))
				.sorted((h1, h2) -> (int) (h2.getId() - h1.getId()))
				.collect(Collectors.toList());

		List<HomeworkBO> homeworksDraftList = homeworksList.stream()
				.filter(a -> a.getStatus().equalsIgnoreCase("SAVE") || a.getStatus().equalsIgnoreCase("ASSIGN-STUDENT"))
				.sorted((h1, h2) -> (int) (h2.getId() - h1.getId()))
				.collect(Collectors.toList());

		homeworkList = new HomeworkList();
		homeworkList.setHomeworkList(homeWorkLists(homeworksOngingList));
		homeworkList.setHomeworkListType("ONGOING");

		homeworkLists.add(homeworkList);

		homeworkList = new HomeworkList();
		homeworkList.setHomeworkList(homeWorkLists(homeworksUpcomingList));
		homeworkList.setHomeworkListType("UPCOMING");

		homeworkLists.add(homeworkList);

		homeworkList = new HomeworkList();
		homeworkList.setHomeworkList(homeWorkLists(homeworksPastList));
		homeworkList.setHomeworkListType("PAST");

		homeworkLists.add(homeworkList);

		homeworkList = new HomeworkList();
		homeworkList.setHomeworkList(homeWorkLists(homeworksDraftList));
		homeworkList.setHomeworkListType("DRAFT");

		homeworkLists.add(homeworkList);

		return homeworkLists;
	}

	public List<Homework> homeWorkLists(List<HomeworkBO> homeworksList) throws UnsupportedEncodingException {
		List<Homework> homeworkList = new ArrayList<>();

		for (HomeworkBO homeworkBO : homeworksList) {
			Homework homeworkVal = new Homework();

			homeworkVal.setHomeworkId(homeworkBO.getId());
			homeworkVal.setActive(homeworkBO.isActive());

			homeworkVal.setEndDate(DU.format(homeworkBO.getEnd_date()));
			homeworkVal.setHomeworkName(homeworkBO.getName());
			homeworkVal.setHomeworkType(homeworkBO.getType());
			homeworkVal.setStartDate(DU.format(homeworkBO.getStart_date()));
			homeworkVal.setStatus(homeworkBO.getStatus());

			jwtUtils.headers.set("Authorization", jwtUtils.getToken());
			jwtUtils.entity = new HttpEntity(jwtUtils.headers);
			SubjectInfo subjectInfo = jwtUtils.restTemplateUtil.getForEntity(SubjectInfo.class,
					jwtUtils.getURL(jwtUtils.smsBaseUrl + "/api/v1/subject?subjectId={subjectId}"), jwtUtils.entity,
					homeworkBO.getSubject());

			homeworkVal.setSubject(subjectInfo.getSubjectName());
			homeworkVal.setSubjectId(Long.valueOf(subjectInfo.getSubjectId()));

			homeworkVal.setSubmission(homeworkBO.getSubmission());
			homeworkVal.setTotalMarks(homeworkBO.getTotalMarks());

			List<StudentBatch> studentBatchs = new ArrayList<>();
			for (HomeworkBatchBO homeworkBatch : homeworkBO.getHomeworkBatchList()) {
				StudentBatch studentBatch = new StudentBatch();
				studentBatch.setBatchId(homeworkBatch.getBatchId());
				studentBatch.setBatchName(homeworkBatch.getBatchName());
				studentBatchs.add(studentBatch);
			}
			homeworkVal.setStudentBatchs(studentBatchs);

			List<StudentBranch> studentBranchs = new ArrayList<>();
			for (HomeworkBranchBO homeworkBranch : homeworkBO.getHomeworkBranchList()) {
				StudentBranch studentBranch = new StudentBranch();
				studentBranch.setBranchId(homeworkBranch.getBranchId());
				studentBranch.setBranchName(homeworkBranch.getBranchName());
				studentBranchs.add(studentBranch);
			}
			homeworkVal.setStudentBranchs(studentBranchs);

			List<StudentClass> studentClasses = new ArrayList<>();
			for (HomeworkGradeBO homeworkGrade : homeworkBO.getHomeworkGradeList()) {
				StudentClass studentClass = new StudentClass();
				studentClass.setClassId(homeworkGrade.getGradeId());
				studentClass.setClassName(homeworkGrade.getGradeName());
				studentClasses.add(studentClass);
			}
			homeworkVal.setStudentClasses(studentClasses);

			List<StudentProgram> studentPrograms = new ArrayList<>();
			for (HomeworkOrientationBO homeworkProgram : homeworkBO.getHomeworkOrientationList()) {
				StudentProgram studentProgram = new StudentProgram();
				studentProgram.setProgramId(homeworkProgram.getOrientationId());
				studentProgram.setProgramName(homeworkProgram.getOrientationName());
				studentPrograms.add(studentProgram);
			}
			homeworkVal.setStudentPrograms(studentPrograms);

			homeworkList.add(homeworkVal);
		}

		return homeworkList;
	}

	@Override
	public List<StudentHomeworkEvaluationList> studentHomeworkEvaluation(Long homeworkId) {

		List<StudentHomeworkEvaluationList> stuHomeworkList = new ArrayList<>();

		HomeworkBO homeworkVal = homeworkRepository.findById(homeworkId).get();

		List<StudentAssignmentBO> stuAssignmentList = studentAssignmentRepository.findByHomeWorkId(homeworkVal);

		List<StudentAssignmentBO> homeworkSubmitedList = stuAssignmentList.stream()
				.filter(a -> a.getIsCompleted() && a.getResult() == null).collect(Collectors.toList());
		List<StudentAssignmentBO> homeworkEvaluatedList = stuAssignmentList.stream().filter(a -> a.getResult() != null)
				.collect(Collectors.toList());
		List<StudentAssignmentBO> homeworkNotSubmitedList = stuAssignmentList.stream()
				.filter(a -> !a.getIsCompleted() && a.getAttemptCount() > 0 && a.getResult() == null)
				.collect(Collectors.toList());

		StudentHomeworkEvaluationList stuHomeworkVal = new StudentHomeworkEvaluationList();
		stuHomeworkVal.setHomeworkListType("SUBMITED");
		stuHomeworkVal.setListCount(homeworkSubmitedList.size());

		for (StudentAssignmentBO studentAssignmentBO : homeworkSubmitedList) {
			HomeworkStudentList homeworkStudentVal = new HomeworkStudentList();
			homeworkStudentVal.setAssignmentId(studentAssignmentBO.getId());

			homeworkStudentVal.setStudentName(studentAssignmentBO.getStudent());
			homeworkStudentVal.setStudentAdmNo(studentAssignmentBO.getStudent());

			stuHomeworkVal.addStudentsListItem(homeworkStudentVal);
		}

		stuHomeworkList.add(stuHomeworkVal);

		stuHomeworkVal = new StudentHomeworkEvaluationList();
		stuHomeworkVal.setHomeworkListType("EVALUATED");
		stuHomeworkVal.setListCount(homeworkEvaluatedList.size());

		for (StudentAssignmentBO studentAssignmentBO : homeworkEvaluatedList) {
			HomeworkStudentList homeworkStudentVal = new HomeworkStudentList();
			homeworkStudentVal.setAssignmentId(studentAssignmentBO.getId());

			homeworkStudentVal.setStudentName(studentAssignmentBO.getStudent());

			stuHomeworkVal.addStudentsListItem(homeworkStudentVal);
		}

		stuHomeworkList.add(stuHomeworkVal);

		stuHomeworkVal = new StudentHomeworkEvaluationList();
		stuHomeworkVal.setHomeworkListType("NOT SUBMITED");
		stuHomeworkVal.setListCount(homeworkNotSubmitedList.size());

		for (StudentAssignmentBO studentAssignmentBO : homeworkNotSubmitedList) {
			HomeworkStudentList homeworkStudentVal = new HomeworkStudentList();
			homeworkStudentVal.setAssignmentId(studentAssignmentBO.getId());

			homeworkStudentVal.setStudentName(studentAssignmentBO.getStudent());

			stuHomeworkVal.addStudentsListItem(homeworkStudentVal);
		}

		stuHomeworkList.add(stuHomeworkVal);

		return stuHomeworkList;
	}

	@Override
	public List<StudentHomeworkEvaluation> homeworkEvaluationStuList(Long homeworkId, String evaluationType) {
		List<StudentHomeworkEvaluation> studentHomeworkEvaluationList = new ArrayList<>();
		HomeworkBO homeworkVal = homeworkRepository.findById(homeworkId).get();
		List<StudentAssignmentBO> stuAssignmentList = studentAssignmentRepository.findByHomeWorkId(homeworkVal);
		List<StudentAssignmentBO> homeworkEvaluationTypeList = null;
		if (evaluationType.equalsIgnoreCase("SUBMITED")) {
			homeworkEvaluationTypeList = stuAssignmentList.stream()
					.filter(a -> a.getIsCompleted() && a.getResult() == null).collect(Collectors.toList());
		} else if (evaluationType.equalsIgnoreCase("EVALUATED")) {
			homeworkEvaluationTypeList = stuAssignmentList.stream().filter(a -> a.getResult() != null)
					.collect(Collectors.toList());
		} else if (evaluationType.equalsIgnoreCase("NOT SUBMITED")) {
			homeworkEvaluationTypeList = stuAssignmentList.stream()
					.filter(a -> !a.getIsCompleted() && a.getAttemptCount() > 0 && a.getResult() == null)
					.collect(Collectors.toList());
		}

		for (StudentAssignmentBO studentHomeworkEvaluation : homeworkEvaluationTypeList) {
			StudentHomeworkEvaluation studentHomeworkEvaluationVal = new StudentHomeworkEvaluation();

			studentHomeworkEvaluationVal.setHomeworkTotalMarks(homeworkVal.getTotalMarks());
			studentHomeworkEvaluationVal.setSubmittedDate(studentHomeworkEvaluation.getAttemptEndTime());
			List<StudentAssignmentFiles> studentUploadedFiles = new ArrayList<>();
			for (AssignmentFileBO assignmentFileVal : studentHomeworkEvaluation.getStuAssignmentFiles()) {
				if (assignmentFileVal.isActive()) {
					StudentAssignmentFiles studentAssignmentFile = new StudentAssignmentFiles();
					studentAssignmentFile.setFileCreatedOn(DU.format(assignmentFileVal.getCreated_on()));
					studentAssignmentFile.setStuAsignmentId(assignmentFileVal.getAssignmentId().getId());
					studentAssignmentFile.setFileId(assignmentFileVal.getId());
					studentAssignmentFile.setFileLocation(assignmentFileVal.getFile_location());
					studentAssignmentFile.setFileSize(assignmentFileVal.getFile_size());
					studentAssignmentFile.setFileType(assignmentFileVal.getFile_type());
					studentAssignmentFile.setFileUpdatedOn(
							assignmentFileVal.getUpdated_on() != null ? DU.format(assignmentFileVal.getUpdated_on())
									: null);

					studentUploadedFiles.add(studentAssignmentFile);
				}
			}

			studentHomeworkEvaluationVal.setStudentUploadedFiles(studentUploadedFiles);
			studentHomeworkEvaluationList.add(studentHomeworkEvaluationVal);
		}

		return studentHomeworkEvaluationList;
	}

	@Override
	public StudentHomeworkSubmissions studentHomeworkEvaluationView(Long homeworkId)
			throws UnsupportedEncodingException {
		StudentHomeworkSubmissions studentHomeworkSubmissionsView = new StudentHomeworkSubmissions();

		HomeworkBO homeworkVal = homeworkRepository.findById(homeworkId).get();
		List<StudentAssignmentBO> stuAssignmentList = studentAssignmentRepository.findByHomeWorkId(homeworkVal);

		studentHomeworkSubmissionsView.setEndDate(DU.format(homeworkVal.getEnd_date()));

		studentHomeworkSubmissionsView.setHomeworkAbout(homeworkVal.getMetaData());
		studentHomeworkSubmissionsView.setHomeworkName(homeworkVal.getName());
		studentHomeworkSubmissionsView.setStartDate(DU.format(homeworkVal.getStart_date()));
		studentHomeworkSubmissionsView.setStudentCount(stuAssignmentList.size());

		SubjectInfo subjectInfo = getSubjectInfo(homeworkVal);

		studentHomeworkSubmissionsView.setSubject(subjectInfo.getSubjectName());
		studentHomeworkSubmissionsView.setSubjectId(Long.valueOf(subjectInfo.getSubjectId()));

		List<StudentClass> studentClasses = new ArrayList<>();
		for (HomeworkGradeBO homeworkGrade : homeworkVal.getHomeworkGradeList()) {
			StudentClass studentClass = new StudentClass();
			studentClass.setClassId(homeworkGrade.getGradeId());
			studentClass.setClassName(homeworkGrade.getGradeName());
			studentClasses.add(studentClass);
		}
		studentHomeworkSubmissionsView.setStudentClasses(studentClasses);

		List<HomeworkFilesBO> homeworkFilesList = homeworkFileRepository.findByHomeWorkId(homeworkVal);
		List<TeacherAssignmentFiles> teacherAssignmentFilesList = new ArrayList<>();
		for (HomeworkFilesBO assignmentFileVal : homeworkFilesList) {
			TeacherAssignmentFiles studentAssignmentFile = new TeacherAssignmentFiles();
			studentAssignmentFile.setFileCreatedOn(DU.format(assignmentFileVal.getCreatedOn()));
			studentAssignmentFile.setFileId(assignmentFileVal.getId());
			studentAssignmentFile.setFileLocation(assignmentFileVal.getLocation());
			studentAssignmentFile.setFileSize(assignmentFileVal.getFileSize());
			studentAssignmentFile.setFileType(assignmentFileVal.getFileType());
			studentAssignmentFile.setFileUpdatedOn(
					assignmentFileVal.getUpdatedOn() != null ? DU.format(assignmentFileVal.getUpdatedOn())
							: null);

			teacherAssignmentFilesList.add(studentAssignmentFile);
		}

		studentHomeworkSubmissionsView.setHomeworkFiles(teacherAssignmentFilesList);

		List<HomeworkStudentsView> homeworkStudentsViewList = new ArrayList<>();
		for (StudentAssignmentBO studentAssignmentVal : stuAssignmentList) {

			HomeworkStudentsView homeworkStudentsViewVal = new HomeworkStudentsView();
			homeworkStudentsViewVal.setAssignmentId(studentAssignmentVal.getId());
			homeworkStudentsViewVal
					.setEvaluationStatus(studentAssignmentVal.getResult() != null ? "Done" : "Not Done Yet");
			StringBuffer marksObtained = new StringBuffer();
			if (studentAssignmentVal.getResult() != null) {
				marksObtained.append(Float.toString(studentAssignmentVal.getResult().getTotalMarks())).append("/");
				marksObtained.append(homeworkVal.getTotalMarks().toString());
			} else {
				marksObtained.append("-");
			}
			homeworkStudentsViewVal.setMarksObtained(marksObtained.toString());
			homeworkStudentsViewVal.setStatus(studentAssignmentVal.getIsCompleted() ? "SUBMITED" : "NOT SUBMITED");
			homeworkStudentsViewVal.setStudentName(studentAssignmentVal.getStudent());

			homeworkStudentsViewList.add(homeworkStudentsViewVal);
		}

		studentHomeworkSubmissionsView.setStudentsList(homeworkStudentsViewList);

		return studentHomeworkSubmissionsView;
	}

	protected SubjectInfo getSubjectInfo(HomeworkBO homeworkVal) throws UnsupportedEncodingException {
		jwtUtils.headers.set("Authorization", jwtUtils.getToken());
		jwtUtils.entity = new HttpEntity(jwtUtils.headers);
		return jwtUtils.restTemplateUtil.getForEntity(SubjectInfo.class,
				jwtUtils.getURL(jwtUtils.smsBaseUrl + "/api/v1/subject?subjectId={subjectId}"), jwtUtils.entity,
				homeworkVal.getSubject());
	}

	@Override
	public Long createHomework(CreationHomework creationHomework) throws ParseException {
		HomeworkBO homeworkVal = null;
		if (creationHomework.getHomeworkId() != null) {
			homeworkVal = homeworkRepository.findById(creationHomework.getHomeworkId()).get();
			homeworkVal.setStatus("CREATED");
		} else {
			homeworkVal = new HomeworkBO();
			homeworkVal.setStatus("SAVE");

		}

		homeworkVal.setName(creationHomework.getHomeworkName());
		homeworkVal.setActive(true);
		// homeworkVal.setDescription(creationHomework.getHomeworkName());

		Date endDate = df.parse(creationHomework.getEndDate());
		String endDateStr = outputformat.format(endDate);

		Date startDate = df.parse(creationHomework.getStartDate());
		String startDateStr = outputformat.format(startDate);

		homeworkVal.setEnd_date(outputformat.parse(endDateStr));
		if (creationHomework.getEvaluation().getValue().equalsIgnoreCase("Required")
				&& creationHomework.getEvaluationStartDate() != null
				&& creationHomework.getEvaluationEndDate() != null) {
			homeworkVal.setEvaluationEndDate(formateDate.parse(creationHomework.getEvaluationEndDate()));
			homeworkVal.setEvaluationStartDate(formateDate.parse(creationHomework.getEvaluationStartDate()));
		}

		homeworkVal.setEvalution(creationHomework.getEvaluation().getValue());

		homeworkVal.setStart_date(outputformat.parse(startDateStr));

		homeworkVal.setSubject(creationHomework.getSubjectId());
		homeworkVal.setSubmission(creationHomework.getSubmission().getValue());
		homeworkVal.setTeacherRemarks(creationHomework.getTeacherRemarks());
		homeworkVal.setTotalMarks(creationHomework.getTotalMarks());
		homeworkVal.setType("OFFLINE");
		homeworkVal.setCreatedBy(jwtUtils.getUserId());
		homeworkVal.setCreatedOn(new Date());
		homeworkVal.setUpdatedBy(jwtUtils.getUserId());
		homeworkVal.setUpdatedOn(new Date());

		homeworkRepository.save(homeworkVal);

		for (StudentBatch studentBatch : creationHomework.getStudentBatchs()) {
			HomeworkBatchBO homeworkBatchVal = new HomeworkBatchBO();
			homeworkBatchVal.setBatchId(studentBatch.getBatchId());
			homeworkBatchVal.setBatchName(studentBatch.getBatchName());
			homeworkBatchVal.setHomeWorkId(homeworkVal);
			homeworkBatchRepository.save(homeworkBatchVal);
		}

		for (StudentBranch studentBranch : creationHomework.getStudentBranchs()) {
			HomeworkBranchBO homeworkBranchVal = new HomeworkBranchBO();
			homeworkBranchVal.setBranchId(studentBranch.getBranchId());
			homeworkBranchVal.setBranchName(studentBranch.getBranchName());
			homeworkBranchVal.setHomeWorkId(homeworkVal);
			homeworkBranchRepository.save(homeworkBranchVal);
		}

		for (StudentProgram studentProgram : creationHomework.getStudentPrograms()) {
			HomeworkOrientationBO homeworkOrientation = new HomeworkOrientationBO();
			homeworkOrientation.setOrientationId(studentProgram.getProgramId());
			homeworkOrientation.setOrientationName(studentProgram.getProgramName());
			homeworkOrientation.setHomeWorkId(homeworkVal);
			homeworkOrientationRepository.save(homeworkOrientation);
		}

		for (StudentSection studentSection : creationHomework.getStudentSections()) {
			HomeworkSectionBO homeworkSectionVal = new HomeworkSectionBO();
			homeworkSectionVal.setSectionId(studentSection.getSectionId());
			homeworkSectionVal.setSectionName(studentSection.getSectionName());
			homeworkSectionVal.setHomeWorkId(homeworkVal);
			homeworkSectionRepository.save(homeworkSectionVal);

			HomeworkGradeBO homeworkGrade = new HomeworkGradeBO();
			homeworkGrade.setGradeId(studentSection.getClassId());
			homeworkGrade.setGradeName(studentSection.getClassName());
			homeworkGrade.setHomeWorkId(homeworkVal);
			homeworkGradeRepository.save(homeworkGrade);
		}

		return homeworkVal.getId();

	}

	@Override
	public Long setUploadedFileComments(Long uploadFileId, String comments) {

		AssignmentFileBO assignmentFileVal = assignmentFileRepository.findById(uploadFileId).get();

		AssignmentFileCommentBO assignmentFileCommentBO = new AssignmentFileCommentBO();
		assignmentFileCommentBO.setAssignmentFile(assignmentFileVal);
		assignmentFileCommentBO.setComments(comments);
		assignmentFileCommentBO.setCreated_on(new Date());
		assignmentFileCommentBO.setActive(true);
		assignmentFileCommentRepository.save(assignmentFileCommentBO);

		return assignmentFileCommentBO.getId();
	}

	@Override
	public void deleteUploadedFileComments(Long homeworkFileCommentId) {
		AssignmentFileCommentBO assignmentFileCommentVal = assignmentFileCommentRepository
				.getById(homeworkFileCommentId);
		assignmentFileCommentVal.setComments(null);
		assignmentFileCommentVal.setUpdated_on(new Date());
		assignmentFileCommentVal.setActive(false);
		assignmentFileCommentRepository.save(assignmentFileCommentVal);

	}

	@Override
	public void setAssignmentRemarks(Long assignmentId, String remarks, Float marks) {

		StudentAssignmentBO studentAssignmentVal = studentAssignmentRepository.findById(assignmentId).get();

		studentAssignmentVal.setRemarks(remarks);
		AssignmentResultBO assignmentResult = new AssignmentResultBO();
		assignmentResult.setTotalMarks(marks);
		// assignmentResult.setCreatedOn(new Date());
		assignmentResult.setUpdatedOn(new Date());
		assignmentResult.setUpdatedBy(jwtUtils.getUserId());
		// assignmentResult.setCreatedBy(jwtUtils.getUserId());

		assignmentResultRepository.save(assignmentResult);

		studentAssignmentRepository.save(studentAssignmentVal);

	}

	@Override
	public void deleteHomework(Long homeworkId) throws ParseException {

		HomeworkBO homeworkVal = homeworkRepository.findById(homeworkId).get();
		homeworkVal.setStatus("DELETE");
		homeworkVal.setActive(false);
		homeworkVal.setUpdatedOn(new Date());
		homeworkRepository.save(homeworkVal);

	}

	@Override
	public List<AssignmentTestPaper> getStudentAssignmentTest(Long studentAssignmentId) {

		StudentAssignmentBO studentAssignment = studentAssignmentRepository.findById(studentAssignmentId).orElse(null);

		List<AssignmentTestPaper> assignmentTestPapers = new ArrayList<>();

		for (AssignmentSectionBO assignmentSection : studentAssignment.getHomeWorkId().getAssignmentSections()) {
			AssignmentTestPaper agnmentTestPaper = new AssignmentTestPaper();
			if (assignmentSection.isActive()) {

				List<String> qids = new ArrayList<>();
				for (AssignmentQuestionsBO assignmentQuestions : assignmentSection.getAssignmentQuestions()) {
					qids.add(assignmentQuestions.getQuestionId());
				}

				agnmentTestPaper.setSectionId(assignmentSection.getId());
				agnmentTestPaper.setSectionName(assignmentSection.getSectionName());
				agnmentTestPaper.setPositiveMarks(assignmentSection.getPositiveMarks());
				agnmentTestPaper.setNegativeMarks(assignmentSection.getNegativeMarks());

				SectionAssignmentTest[] sectionAssignmentTest = runSectionAssignmentTest(qids);

				ArrayList<SectionAssignmentTest> arrayList = new ArrayList<SectionAssignmentTest>(
						Arrays.asList(sectionAssignmentTest));
				int correctCount = 0;
				int incorrectCount = 0;
				int unAttemptCount = 0;
				for (SectionAssignmentTest assignTest : arrayList) {
					StudentAssignmentQuestionsBO studentAssignmentQuestions = studentAssignment
							.getSectionQuestion(assignmentSection.getId(), assignTest.getId());
					if (studentAssignmentQuestions != null
							&& studentAssignmentQuestions.getAssignmentQuestion().getQuestionId()
									.equalsIgnoreCase(assignTest.getId())) {
						if (studentAssignmentQuestions.isCorrect()) {
							correctCount++;
						} else {
							incorrectCount++;
						}
						assignTest.setCorrect(studentAssignmentQuestions.isCorrect());
						assignTest.setClearResponse(studentAssignmentQuestions.isClearResponse());
						assignTest.setMarkForReview(studentAssignmentQuestions.isMarkForReview());
						assignTest.setIsVisited(studentAssignmentQuestions.isVisited());
						assignTest.setResponse(studentAssignmentQuestions.getResponse());
						assignTest.setQuestionGap(studentAssignmentQuestions.getQuestionGap());
						assignTest
								.setAttemptedTime(DU.format(studentAssignmentQuestions.getAttemptTime(), "dd-MM-yyyy"));
					} else {
						unAttemptCount++;
					}
					agnmentTestPaper.setCorrect(correctCount);
					agnmentTestPaper.setIncorrect(incorrectCount);
					agnmentTestPaper.setUnattempted(unAttemptCount);

				}

				// System.out.println(sectionAssignmentTest);
				// System.out.println(assignmentSection.getId());
				agnmentTestPaper.assignmentTestPaper(sectionAssignmentTest);
				assignmentTestPapers.add(agnmentTestPaper);
			}

		}

		return assignmentTestPapers;
	}

	protected SectionAssignmentTest[] runSectionAssignmentTest(List<String> qids) {
		// request body parameters
		HashMap<String, Object> map = new HashMap<>();
		map.put("questionid", qids);
		map.put("pageNumber", 1);
		map.put("size", 10);

		jwtUtils.headers.setContentType(MediaType.APPLICATION_JSON);
		jwtUtils.headers.set("Authorization", uamToken.getUAMToken());
		jwtUtils.entity = new HttpEntity<>(map, jwtUtils.headers);

		return jwtUtils.restTemplateUtil.getForEntityPost(
				SectionAssignmentTest[].class,
				jwtUtils.queBaseUrl,
				jwtUtils.entity);
	}

	@Override
	public List<AssignmentTestPaper> getHomeworkAssignmentTest(Long homeworkId) {

		// System.out.println("Token" + jwtUtils.getToken());
		HomeworkBO homework = homeworkRepository.findById(homeworkId).orElse(null);

		List<AssignmentTestPaper> assignmentTestPapers = new ArrayList<>();

		for (AssignmentSectionBO assignmentSection : homework.getAssignmentSections()) {
			AssignmentTestPaper agnmentTestPaper = new AssignmentTestPaper();

			if (assignmentSection.isActive()) {

				List<String> qids = new ArrayList<>();

				for (AssignmentQuestionsBO assignmentQuestions : assignmentSection.getAssignmentQuestions()) {
					if (assignmentQuestions.isActive()) {
						qids.add(assignmentQuestions.getQuestionId());
					}

				}

				agnmentTestPaper.setSectionId(assignmentSection.getId());
				agnmentTestPaper.setSectionName(assignmentSection.getSectionName());
				agnmentTestPaper.setPositiveMarks(assignmentSection.getPositiveMarks());
				agnmentTestPaper.setNegativeMarks(assignmentSection.getNegativeMarks());

				SectionAssignmentTest[] sectionAssignmentTest = runSectionAssignmentTest(qids);

				ArrayList<SectionAssignmentTest> arrayList = new ArrayList<SectionAssignmentTest>(
						Arrays.asList(sectionAssignmentTest));

				for (SectionAssignmentTest assignTest : arrayList) {

					assignTest.setQuestionId(assignmentSection.getQuestion(assignTest.getId()).getId());
					assignTest.setLmsNegativemarks(assignmentSection.getNegativeMarks());
					assignTest.setLmsPostiveMarks(assignmentSection.getPositiveMarks());

				}

				// System.out.println(sectionAssignmentTest);
				agnmentTestPaper.assignmentTestPaper(sectionAssignmentTest);
				assignmentTestPapers.add(agnmentTestPaper);

			}
		}

		return assignmentTestPapers;
	}

	@Override

	public void deleteHomeworkFile(Long homeworkfileId) throws ParseException {

		HomeworkFilesBO homeworkfileVal = homeworkFileRepository.findById(homeworkfileId).get();
		
		homeworkfileVal.setActive(false);
		homeworkfileVal.setUpdatedOn(new Date());
		homeworkfileVal.setUpdatedBy(jwtUtils.getUserId());
		homeworkFileRepository.save(homeworkfileVal);

	}

}
