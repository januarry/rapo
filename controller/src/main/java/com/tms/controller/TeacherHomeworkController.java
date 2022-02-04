package com.tms.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.tms.api.model.AssignmentTestPaper;
import com.tms.api.model.CreationHomework;
import com.tms.api.model.FileComment;
import com.tms.api.model.HomeworkCreationResponse;
import com.tms.api.model.HomeworkList;
import com.tms.api.model.QuestionDeleteresponse;
import com.tms.api.model.QuestionMarksresponse;
import com.tms.api.model.Response;
import com.tms.api.model.SectionList;
import com.tms.api.model.StudentHomeworkEvaluation;
import com.tms.api.model.StudentHomeworkEvaluationList;
import com.tms.api.model.StudentHomeworkSubmissions;
import com.tms.api.model.StudentUploadFileCommentResponse;
import com.tms.service.AssignHomework;
import com.tms.service.HomeworkUpload;
import com.tms.service.SectionService;
import com.tms.service.StudentAssignment;
import com.tms.service.TeacherHomework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Transactional(rollbackOn = Exception.class)
// @RequestMapping("/v1")
public class TeacherHomeworkController {

	@Autowired
	TeacherHomework teacherHomework;

	@Autowired
	AssignHomework assignHomework;

	@Autowired
	HomeworkUpload homeworkUpload;

	@Autowired
	SectionService sectionService;

	@Autowired
	StudentAssignment studentAssignment;

	@RequestMapping(value = "/getAllHomeworks", method = RequestMethod.GET)
	@ApiOperation(value = "Return Home Work All Lists", response = HomeworkList.class)
	public ResponseEntity<List<HomeworkList>> getStudentHomeworkList()
			throws ParseException, UnsupportedEncodingException {

		List<HomeworkList> homeworkVals = new ArrayList<>();

		homeworkVals = teacherHomework.getHomeworkLists();

		log.info("Teacher HomeworkList success");
		return new ResponseEntity<>(homeworkVals, HttpStatus.OK);

	}

	@RequestMapping(value = "/getStudentHomeworkEvaluation", method = RequestMethod.GET)
	@ApiOperation(value = "Student Homework Evaluation", response = StudentHomeworkEvaluationList.class)
	public ResponseEntity<List<StudentHomeworkEvaluationList>> getStudentHomeworkEvaluation(
			@RequestParam("homeworkId") Long homeworkId) {

		List<StudentHomeworkEvaluationList> stuHomeworkList = teacherHomework.studentHomeworkEvaluation(homeworkId);

		return new ResponseEntity<>(stuHomeworkList, HttpStatus.OK);

	}

	@RequestMapping(value = "/getHomeworkEvaluationStudentList", method = RequestMethod.GET)
	@ApiOperation(value = "Student Homework Evaluation", response = StudentHomeworkEvaluation.class)
	public ResponseEntity<List<StudentHomeworkEvaluation>> getHomeworkEvaluationStudentList(
			@RequestParam("homeworkId") Long homeworkId, @RequestParam("evaluationType") String evaluationType) {

		List<StudentHomeworkEvaluation> homeworkStuList = teacherHomework.homeworkEvaluationStuList(homeworkId,
				evaluationType);

		return new ResponseEntity<>(homeworkStuList, HttpStatus.OK);

	}

	@RequestMapping(value = "/getStudentHomeworkSubmissions", method = RequestMethod.GET)
	@ApiOperation(value = "Student Homework Evaluation", response = StudentHomeworkSubmissions.class)
	public ResponseEntity<StudentHomeworkSubmissions> getStudentHomeworkSubmissionsView(
			@RequestParam("homeworkId") Long homeworkId) throws UnsupportedEncodingException {

		StudentHomeworkSubmissions studentHomeworkSubmissionsView = teacherHomework
				.studentHomeworkEvaluationView(homeworkId);

		return new ResponseEntity<>(studentHomeworkSubmissionsView, HttpStatus.OK);

	}

	@RequestMapping(value = "/teacherHomeworkCreation", method = RequestMethod.POST)
	@ApiOperation(value = "Teacher Homework Creation", response = HomeworkCreationResponse.class)
	public ResponseEntity<HomeworkCreationResponse> teacherHomeworkDraftCreation(
			@RequestBody CreationHomework creationHomework) throws ParseException {

		Long homeworkVal = teacherHomework.createHomework(creationHomework);

		return ResponseEntity.ok(new HomeworkCreationResponse().success("True").message("Successfully Drafted Homework")
				.homeworkId(homeworkVal));

	}

	@RequestMapping(value = "/teacherUploadingHomework", method = RequestMethod.POST)
	@ApiOperation(value = "teacher homework uploading files", response = String.class)
	public ResponseEntity<String> teacherUploadingHomework(
			@RequestParam(required = true, name = "homeworkId") Long homeworkId,
			@RequestParam("file[]") MultipartFile[] file, @RequestParam("description") String description) {

		homeworkUpload.teacherUploadingHomework(homeworkId, file, description);
		log.info("Teacher Homework uploading success");
		return new ResponseEntity<>("Success", HttpStatus.OK);

	}

	@RequestMapping(value = "/teacherDeleteHomework", method = RequestMethod.DELETE)
	@ApiOperation(value = "teacher homework uploading files", response = HomeworkList.class)
	public ResponseEntity<String> teacherUploadingHomework(@RequestParam("homeworkfileId") Long homeworkfileId) {

		homeworkUpload.teacherDeleteHomework(homeworkfileId);
		log.info("Teacher Homework uploading success");
		return new ResponseEntity<>("Success", HttpStatus.OK);

	}

	@RequestMapping(value = "/teacherPublishHomework", method = RequestMethod.PUT)
	@ApiOperation(value = "Teacher Publish the homework ", response = String.class)
	public ResponseEntity<Response> teacherPublishHomework(
			@RequestParam(required = true, name = "homeworkId") Long homeworkId) {

		homeworkUpload.teacherPublishHomework(homeworkId);
		log.info("Teacher Publish the homework success");
		return ResponseEntity.ok(new Response().success("Success").message("Homework published Successfully"));

	}

	@RequestMapping(value = "/setUploadedFileComments", method = RequestMethod.POST)
	@ApiOperation(value = "Set Homework Uploaded File Comments", response = StudentUploadFileCommentResponse.class)
	public ResponseEntity<StudentUploadFileCommentResponse> setUploadedFileComments(@RequestParam Long homeworkFileId,
			@RequestParam String comments) throws ParseException {

		Long uploadFileCommentId = teacherHomework.setUploadedFileComments(homeworkFileId, comments);

		return ResponseEntity.ok(new StudentUploadFileCommentResponse().success("True").message("Successfully Done")
				.uploadFileCommentId(uploadFileCommentId));

	}

	@RequestMapping(value = "/deleteUploadedFileComments", method = RequestMethod.PUT)
	@ApiOperation(value = "Set Homework Uploaded File Comments", response = StudentUploadFileCommentResponse.class)
	public ResponseEntity<Response> deleteUploadedFileComments(@RequestParam Long homeworkFileCommentId)
			throws ParseException {

		teacherHomework.deleteUploadedFileComments(homeworkFileCommentId);

		return ResponseEntity.ok(new Response().success("True").message("Successfully Done"));

	}

	@RequestMapping(value = "/setStudentHomeworkRemarks", method = RequestMethod.PUT)
	@ApiOperation(value = "Set StudenHomeworRemarks", response = StudentUploadFileCommentResponse.class)
	public ResponseEntity<Response> setStudentHomeworkRemarks(@RequestParam Long assignmentId,
			@RequestParam String remarks, @RequestParam Float marks) throws ParseException {

		teacherHomework.setAssignmentRemarks(assignmentId, remarks, marks);

		return ResponseEntity.ok(new Response().success("True").message("Successfully Done"));

	}

	@RequestMapping(value = "/deleteHomework", method = RequestMethod.PUT)
	@ApiOperation(value = "Homework Delete", response = HomeworkList.class)
	public ResponseEntity<String> homeworkDelete(@RequestParam("homeworkId") Long homeworkId) throws ParseException {

		teacherHomework.deleteHomework(homeworkId);
		log.info("Teacher Homework deleted successfully");
		return new ResponseEntity<>("Success", HttpStatus.OK);

	}

	@RequestMapping(value = "/deletefilecomments", method = RequestMethod.PUT)
	@ApiOperation(value = "Delete file comments")
	public ResponseEntity<String> deletefileComments(@RequestParam("assignmentfileId") Long assignmentfileId)
			throws ParseException {

		assignHomework.deletefileComments(assignmentfileId);
		log.info("Teacher Delete file comments  successfully");
		return new ResponseEntity<>("Success", HttpStatus.OK);

	}

	@RequestMapping(value = "/addSection", method = RequestMethod.POST)
	public ResponseEntity<String> addSection(@RequestParam("homeWorkId") Long homeWorkId,
			@RequestParam("sectionName") String sectionName) {
		sectionService.addSection(homeWorkId, sectionName);
		log.info("section added success");
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@RequestMapping(value = "/updateSection", method = RequestMethod.PUT)
	public ResponseEntity<String> updateHomework(@RequestParam("Id") Long id,
			@RequestParam("sectionName") String sectionName) {
		sectionService.updateSection(id, sectionName);
		log.info("section updated success");
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteSection", method = RequestMethod.PUT)
	public ResponseEntity<String> deleteSection(@RequestParam("Id") Long id) {
		sectionService.deleteSection(id);
		log.info("section delete success");
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@RequestMapping(value = "/sectionMarks", method = RequestMethod.PUT)
	public ResponseEntity<String> marksSection(@RequestParam("sectionId") Long id,
			@RequestParam("positiveMarks") Float positiveMarks, @RequestParam("negativeMarks") Float negativeMarks) {
		sectionService.sectionMarks(id, positiveMarks, negativeMarks);
		log.info("marks added in section  success");
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@RequestMapping(value = "/getAssignmentfileComments", method = RequestMethod.GET)
	@ApiOperation(value = "Assignment file comments", response = FileComment.class)
	public ResponseEntity<List<FileComment>> getAssignmentfileComments(
			@RequestParam("assignmentfileId") Long assignmentfileId) throws ParseException {

		return new ResponseEntity<>(assignHomework.getAssignmentFileComments(assignmentfileId), HttpStatus.OK);

	}

	@RequestMapping(value = "/createAssignmentQuestions", method = RequestMethod.POST)
	@ApiOperation(value = "Create Assignment Questions", response = Response.class)
	public ResponseEntity<Response> createAssignmentQuestions(@RequestParam List<String> questionId,
			@RequestParam boolean isMylibrary,
			@RequestParam Long assignmentSectionId) {

		studentAssignment.createAssignmentQuestions(questionId, isMylibrary, assignmentSectionId);

		return ResponseEntity.ok(new Response().success("True").message("Successfully Assigned Questions"));

	}

	@RequestMapping(value = "/getSections", method = RequestMethod.GET)
	@ApiOperation(value = "getsections", response = SectionList.class)
	public ResponseEntity<List<SectionList>> getsections(
			@RequestParam("homeWorkId") Long homeWorkId) {

		List<SectionList> sections = sectionService.sectionList(homeWorkId);

		return new ResponseEntity<>(sections, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getStudentResponseAssignmentTest", produces = {
			"application/json" })
	public ResponseEntity<List<AssignmentTestPaper>> getStudentAssignmentTest(
			@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "studentAssignmentId", required = true) Long studentAssignmentId) {

		return new ResponseEntity<>(teacherHomework.getStudentAssignmentTest(studentAssignmentId), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getHomeworkAssignmentTest", produces = { "application/json" })
	public ResponseEntity<List<AssignmentTestPaper>> getHomeworkAssignmentTest(
			@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "homeworkId", required = true) Long homeworkId) {

		return new ResponseEntity<>(teacherHomework.getHomeworkAssignmentTest(homeworkId), HttpStatus.OK);

	}

	@RequestMapping(value = "/questionMarks", method = RequestMethod.PUT)
	@ApiOperation(value = "questionMarks", response = QuestionMarksresponse.class)
	public ResponseEntity<QuestionMarksresponse> marksQuestion(
			@RequestParam("QuestionId") Long id,
			@RequestParam("positiveMarks") Float positiveMarks,
			@RequestParam("negativeMarks") Float negativeMarks) throws Exception {

		sectionService.marksQuestion(id, positiveMarks, negativeMarks);
		log.info("marks added in Question  success");
		QuestionMarksresponse successresponse = new QuestionMarksresponse();
		return new ResponseEntity<>(successresponse, HttpStatus.OK);

	}

	@RequestMapping(value = "/questionDelete", method = RequestMethod.PUT)
	@ApiOperation(value = "questionDelete", response = QuestionDeleteresponse.class)
	public ResponseEntity<QuestionDeleteresponse> deleteQuestion(@RequestParam("QuestionId") Long id) throws Exception {

		sectionService.deleteQuestion(id);
		log.info(" Question deleted success");
		QuestionDeleteresponse successresponse = new QuestionDeleteresponse();
		return new ResponseEntity<>(successresponse, HttpStatus.OK);

	}

	@RequestMapping(value = "/deleteHomeworkFile", method = RequestMethod.PUT)
	@ApiOperation(value = "Homework File Delete", response = HomeworkList.class)
	public ResponseEntity<String> homeworkFileDelete(@RequestParam("homeworkfileId") Long homeworkfileId)
			throws ParseException {
		teacherHomework.deleteHomeworkFile(homeworkfileId);
		log.info("Teacher Homework file deleted successfully");
		return new ResponseEntity<>("Success", HttpStatus.OK);

	}
}
