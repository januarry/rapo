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
@Table(name = "assignment_file")
public class AssignmentFileBO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ASSIGNMENT_ID")
	private StudentAssignmentBO assignmentId;
	
	@Column(name = "FILE_LOCATION")
	private String file_location;
	
	@Column(name = "FILE_TYPE")
	private String file_type;
	
	@Column(name = "FILE_SIZE")
	private float file_size;	
	
	@Column(name = "CREATED_ON")
	private Date created_on;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;	
	
	@Column(name = "UPDATED_ON")
	private Date updated_on;
	
	@Column(name = "FILE_DOWNLOAD_COUNT")
	private Long file_download_count;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignmentFile")
	private List<AssignmentFileCommentBO> assignmentFileComments;

}
