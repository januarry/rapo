package com.tms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_assignment_questions")
public class StudentAssignmentQuestionsBO implements Serializable {


	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CORRECT")
	private boolean correct;

	@Column(name = "CLEAR_RESPONSE")
	private boolean clearResponse;

	@Column(name = "MARK_FOR_REVIEW")
	private boolean markForReview;
	
	@Column(name = "RESPONSE")
	private String response;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSIGNMENT_QUESTION_ID")
	private AssignmentQuestionsBO assignmentQuestion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ASSIGNMENT_ID")
	private StudentAssignmentBO studentAssignment;

	
	@Column(name = "IS_VISITED")
	private boolean isVisited;

	@Column(name = "QUESTION_GAP")
	private Long questionGap;

	@Column(name = "ATTEMPT_TIME")
	private Date attemptTime;

	@Column(name = "MARKS")
	private Float marks;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "CREATED_ON")
	private Date created_on;
	
	@Column(name = "UPDATED_ON")
	private Date updated_on;
	

}
