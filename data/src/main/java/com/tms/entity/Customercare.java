package com.tms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customercare")
public class Customercare implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "USER_ID")
	private String user_id;
	
	@Column(name = "ISSUE_TYPE")
	private String Issue_type;
	
	@Column(name = "ISSUE")
	private String Issue;
	
	@Column(name = "ISSUE_STATUS")
	private String Issue_status;
	
	@Column(name = "CREATED_DATE")
	private Date created_date;

}
