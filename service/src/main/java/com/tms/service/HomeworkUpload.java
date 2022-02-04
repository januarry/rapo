package com.tms.service;

import org.springframework.web.multipart.MultipartFile;

public interface HomeworkUpload {

    public String teacherUploadingHomework(Long homeworkId, MultipartFile[] file,String description) ;

    public String teacherDeleteHomework(Long homeworkFileId) ;

    public String teacherPublishHomework(Long homeworkId) ;
    
}
