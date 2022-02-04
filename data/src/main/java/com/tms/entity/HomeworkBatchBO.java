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
@Table(name = "homework_batch")
public class HomeworkBatchBO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
		
	@Column(name = "BATCH_ID")
	private Long batchId;

	@Column(name = "BATCH_NAME")
	private String batchName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HOME_WORK_ID")
	private HomeworkBO homeWorkId;

	
	
}