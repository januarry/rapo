package com.tms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import javax.persistence.Entity;

import javax.persistence.FetchType;

import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;

import javax.persistence.Id;

import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@NoArgsConstructor

@AllArgsConstructor
@Entity
@Data
@Table(name = "assignmentSection")

public class AssignmentSectionBO {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "homeWorkId")
	private HomeworkBO homeWorkId;

	@Column(name = "sectionName")
	private String sectionName;

	@Column(name = "active")
	private boolean active;
	
	@Column(name = "positiveMarks")
	private Float positiveMarks;

	@Column(name = "negativeMarks")
	private Float negativeMarks;

	@Column(name = "totalMarks")
	private Float totalMarks;

	@Column(name = "createdOn")
	private Date createdOn;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "updatedOn")
	private Date updatedOn;

	@Column(name = "updatedBy")
	private String updatedBy;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignmentSection")
	private List<AssignmentQuestionsBO> assignmentQuestions;

	public AssignmentQuestionsBO getQuestion(String qId) {

		if (assignmentQuestions != null) {
			return assignmentQuestions.stream()
					.filter(a-> a.getQuestionId().equalsIgnoreCase(qId))
					.findFirst().orElse(null);

		}

		return null;

	}

}