package com.tms.entity;

import java.io.Serializable;

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
@Table(name = "homework_grade")
public class HomeworkGradeBO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
		
	@Column(name = "GRADE_ID")
	private Long gradeId;

	@Column(name = "GRADE_NAME")
	private String gradeName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HOME_WORK_ID")
	private HomeworkBO homeWorkId;

	
	
}