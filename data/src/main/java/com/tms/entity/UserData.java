package com.tms.entity;

import java.io.Serializable;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private String admNo;
	
	
	private String password;
	
	
	private String status;
	

}
