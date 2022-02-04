package com.tms.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.transaction.Transactional;

import com.tms.api.model.AssignStudentsForHomework;
import com.tms.api.model.Response;
import com.tms.api.model.StudentsHomeworkView;
import com.tms.service.AssignHomework;
import com.tms.service.HomeworkUpload;
import com.tms.service.TeacherHomework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Transactional(rollbackOn = Exception.class)
//@RequestMapping("/v1")
public class StudentAssignmentsController {

	@Autowired
	TeacherHomework teacherHomework;

	@Autowired
	AssignHomework assignHomework;

	@Autowired
	HomeworkUpload homeworkUpload;

	@RequestMapping(value = "/assignStudentsForHomework", method = RequestMethod.POST)
	@ApiOperation(value = "Homework Affinment for Students", response = Response.class)
	public ResponseEntity<Response> assignStudentsForHomework(
			@RequestBody AssignStudentsForHomework assignStudentsForHomework) throws ParseException {

		assignHomework.assignHomeworkForStudents(assignStudentsForHomework);

		return ResponseEntity.ok(new Response().success("True").message("Successfully Assigned Students"));

	}

	@RequestMapping(value = "/assignStudentsForHomeworkView", method = RequestMethod.GET)
	@ApiOperation(value = "List of assign students by homework", response = Response.class)
	public ResponseEntity<StudentsHomeworkView> assignStudentsForHomeworkView(
			@RequestParam("homeworkId") Long homeworkId) throws ParseException, UnsupportedEncodingException {

		StudentsHomeworkView stuhwview = new StudentsHomeworkView();
		
		stuhwview = assignHomework.assignStudentsForHomeworkView(homeworkId);
		return new ResponseEntity<>(stuhwview, HttpStatus.OK);

	}

}
