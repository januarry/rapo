package com.tms.repository;

import java.util.List;

import com.tms.entity.HomeworkBO;
import com.tms.entity.StudentAssignmentBO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAssignmentRepository extends JpaRepository<StudentAssignmentBO, Long> {

	

	@Query(value = "select * from student_assignment  where STUDENT_ID=?1 order by ATTEMPT_END_TIME desc", nativeQuery = true)
	List<StudentAssignmentBO> findByStudent(String admNo);

	List<StudentAssignmentBO> findByHomeWorkId(HomeworkBO homeworkVal);

	@Query(value = "select * from student_assignment  where STUDENT_ID=?1 and HOMEWORK_ID=?2  and IS_ACTIVE=1 order by ATTEMPT_END_TIME desc", nativeQuery = true)
	StudentAssignmentBO findByStudentAndHomework(String admNo,Long id);

}