package com.tms;

import com.tms.serviceImpl.AssignHomeworkImplTest;
import com.tms.serviceImpl.HomeworkUploadImplTest;
import com.tms.serviceImpl.SectionServiceImplTest;
import com.tms.serviceImpl.TeacherHomeworkImplTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
    AssignHomeworkImplTest.class,
    TeacherHomeworkImplTest.class,
    SectionServiceImplTest.class,
    HomeworkUploadImplTest.class
})
public abstract class ServiceTest {
}
