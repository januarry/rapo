package com.tms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
@Table(name = "assignment_questions")
public class AssignmentQuestionsBO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "QUESTION_ID")
	private String questionId;

	@Column(name ="active")
    private boolean active;

  

	@Column(name ="is_mylibrary")
    private boolean isMylibrary;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade =CascadeType.ALL)
	@JoinColumn(name = "ASSIGNMENT_SECTION_ID")
	private AssignmentSectionBO assignmentSection;
		
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	

}
