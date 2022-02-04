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
@Table(name = "assignment_file_comments")
public class AssignmentFileCommentBO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ASSIGNMENT_FILE_ID")
	private AssignmentFileBO assignmentFile;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "CREATED_ON")
	private Date created_on;

	@Column(name = "UPDATED_ON")
	private Date updated_on;

	@Column(name = "ACTIVE")
	private boolean active;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "CREATED_BY")
	private String createdBy;

}
