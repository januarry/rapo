package com.tms.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.tms.api.model.GetTeacherQuestionLibraryInfo;
import com.tms.api.model.Response;
import com.tms.api.model.TeacherQuestionLibraryInfo;
import com.tms.service.TeacherQuestionLibrary;

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
public class TeacherQuestionLibraryController {

	@Autowired
	TeacherQuestionLibrary teacherLibrary;		

	@RequestMapping(value = "/teacherQuestionLibrary", method = RequestMethod.POST)
	@ApiOperation(value = "Teacher Question Library", response = Response.class)
	public ResponseEntity<Response> teacherHomeworkDraftCreation(
			@RequestBody TeacherQuestionLibraryInfo teacherLibraryInfo) throws ParseException {

				teacherLibrary.createTeacherQuestionLibrary(teacherLibraryInfo);

		return ResponseEntity.ok(new Response().success("True").message("Successfully Created"));

	}

	@RequestMapping(value = "/getTeacherQuestionLibrary", method = RequestMethod.GET)
	@ApiOperation(value = "List of assign teacherQuestionLibrary", response = Response.class)
	public ResponseEntity<List<GetTeacherQuestionLibraryInfo>> assignStudentsForHomeworkView(
			@RequestParam("teacherId") String teacherId) throws ParseException {

				List<GetTeacherQuestionLibraryInfo> stuhwview = new ArrayList<GetTeacherQuestionLibraryInfo>();
		
		stuhwview = teacherLibrary.getTeacherQuestionLibrary(teacherId);
		
		return new ResponseEntity<>(stuhwview, HttpStatus.OK);

	}

	@RequestMapping(value = "/updateTeacherQuestionLibrary", method = RequestMethod.PATCH)
	@ApiOperation(value = "Upodate Teacher Question Library", response = Response.class)
	public ResponseEntity<Response> updateTeacherQuestionLibrary(
			@RequestBody GetTeacherQuestionLibraryInfo updateTeacherLibraryInfo) throws ParseException {

				String updateStatus=teacherLibrary.updateTeacherQuestionLibrary(updateTeacherLibraryInfo);

		return ResponseEntity.ok(new Response().success("Success").message(updateStatus));

	}


	
}
