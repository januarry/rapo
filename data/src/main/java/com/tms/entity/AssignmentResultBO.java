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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignment_result")
public class AssignmentResultBO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ASSIGNMENT_ID")
	private StudentAssignmentBO stuAssignment;
	
	@Column(name = "POSITIVE_MARKS")
	private Float positiveMarks;
	
	@Column(name = "NEGATIVE_MARKS")
	private Float negativeMarks;
	
	@Column(name = "TOTAL_MARKS")
	private Float totalMarks;
		
	@Column(name = "CREATED_ON")
	private Date createdOn;
		
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;

}
