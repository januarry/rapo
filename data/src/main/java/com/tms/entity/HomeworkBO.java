package com.tms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "homework")
public class HomeworkBO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "ACTIVE")
	private boolean active;
	
	@Column(name = "START_DATE")
	private Date start_date;
	
	@Column(name = "END_DATE")
	private Date end_date;
	
	@Column(name = "SUBJECT")
	private Long subject;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "TOTAL_MARKS")
	private Integer totalMarks;
	
	@Column(name = "EVALUATION_START_DATE")
	private Date evaluationStartDate;
	
	@Column(name = "EVALUATION_END_DATE")
	private Date evaluationEndDate;
	
	@Column(name = "TEACHER_REMARKS")
	private String teacherRemarks;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "META_DATA")
	private String metaData;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "SUBMISSION")
	private String submission;

	@Column(name = "EVALUTION")
	private String evalution;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<HomeworkFilesBO> homeWorkFiles;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<StudentAssignmentBO> studentAssignments ;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<HomeworkBatchBO> homeworkBatchList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<HomeworkBranchBO> homeworkBranchList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<HomeworkGradeBO> homeworkGradeList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<HomeworkOrientationBO> homeworkOrientationList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<HomeworkSectionBO> homeworkSectionList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<AssignmentSectionBO> sections;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "homeWorkId")
	private List<AssignmentSectionBO> assignmentSections;
	
	
}