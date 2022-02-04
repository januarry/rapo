package com.tms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_assignment")
public class StudentAssignmentBO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "HOMEWORK_ID")
	private HomeworkBO homeWorkId;

	@Column(name = "STUDENT_ID")
	private String student;

	@Column(name = "ATTEMPT_START_TIME")
	private Date attemptStartTime;

	@Column(name = "ATTEMPT_END_TIME")
	private Date attemptEndTime;

	@Column(name = "IS_COMPLETED")
	private Boolean isCompleted;

	@Column(name = "ATTEMPT_COUNT")
	private Long attemptCount;

	@Column(name = "META_DATA")
	private String metaData;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "RESULT_ID")
	private AssignmentResultBO result;

	@Column(name = "DOWNLOAD_COUNT")
	private Long downloadCount;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "CREATED_ON")
	private Date createdOn;
		
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignmentId")
	private List<AssignmentFileBO> stuAssignmentFiles;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "studentAssignment")
	private List<StudentAssignmentQuestionsBO> studentAssignmentQuestion;

	public List<StudentAssignmentQuestionsBO> getSectionAssignmentQuestions(Long sectionId) {

		if (studentAssignmentQuestion != null) {
			return studentAssignmentQuestion.stream()
					.filter(a -> a.getAssignmentQuestion().getAssignmentSection().getId() == sectionId)
					.collect(Collectors.toList());

		}

		return null;

	}

	public StudentAssignmentQuestionsBO getSectionQuestion(Long sectionId, String qId) {

		if (studentAssignmentQuestion != null) {
			return studentAssignmentQuestion.stream()
					.filter(a -> a.getAssignmentQuestion().getAssignmentSection().getId() == sectionId
							&& a.getAssignmentQuestion().getQuestionId().equalsIgnoreCase(qId))
					.findFirst().orElse(null);

		}

		return null;

	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stuAssignment")
	private List<AssignmentResultBO> assignmentResult;

}
