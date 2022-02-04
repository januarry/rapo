package com.tms.entity;

import java.io.Serializable;
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

import lombok.Data;

@Entity
@Table(name = "question")
@Data
public class QuestionBO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "QUESTION_ID")
	private Integer questionId;

	@Column(name = "TEXT", columnDefinition = "LONGTEXT")
	private  String text;	
	
	@Column(name = "POSITIVE_MARKS")
	private Float positiveMarks;

	@Column(name = "NEGATIVE_MARKS")
	private Float negativeMarks;

	@Column(name = "SOLUTION" ,columnDefinition = "LONGTEXT")
	private String solution;

	@Column(name = "HINT" ,columnDefinition = "LONGTEXT")
	private String hint;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUESTION_TYPE_ID")
	private QuestionTypeBO questionTypeId;

	@Column(name = "QUESTION_STATUS")
	private String questionStatus;

	@Column(name = "SUBJECT_ID")
	private Long subjectId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSING_SECTION_ID")
	private AssignmentSectionBO assignSectionId;

	@Column(name = "UUID")
	private String uuid;

	@Column(name = "CREATED_ON")
	private Date createdOn;	

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "questionId")
	private List<QuestionOptionBO> questionOptions;

}
