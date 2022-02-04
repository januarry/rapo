package com.tms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "question_type")
@Data
public class QuestionTypeBO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "QUESTION_TYPE_ID")
	private Integer questionTypeId;

	@Column(name = "QUESTION_TYPE", length = 30)
	private String questionType;

	@Column(name = "STATUS")
	private Boolean status;	

	@Column(name = "CREATED_ON")
	private Date createdOn;	

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

}
