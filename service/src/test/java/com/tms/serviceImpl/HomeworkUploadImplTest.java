package com.tms.serviceImpl;

import com.tms.ServiceTest;
import com.tms.configuration.JwtUtils;
import com.tms.entity.AssignmentSectionBO;
import com.tms.entity.HomeworkBO;
import com.tms.entity.HomeworkFilesBO;
import com.tms.repository.HomeworkFileRepository;
import com.tms.repository.HomeworkRepository;
import com.tms.repository.SectionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class HomeworkUploadImplTest extends ServiceTest {

    private static final long HOMEWORK_ID = 123L;
    private static final long HOMEWORK_FILE_ID = 124L;
    private static final String USER_ID = "TEST_123";

    @InjectMocks
    @Spy
    private HomeworkUploadImpl mockHomeworkUploadImpl;

    @Mock
    HomeworkRepository mockHomeworkRepository;

    @Mock
    HomeworkFileRepository mockHomeworkFileRepository;

    @Mock
    SectionRepository mockSectionRepository;

    @Mock
    JwtUtils mockJwtUtils;

    @Test
    void teacherUploadingHomeworkShouldReturnSuccessString() {
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] files = new MultipartFile[1];
        File uploadFile = mock(File.class);
        files[0] = file;
        HomeworkBO mockHomework = mock(HomeworkBO.class);
        when(file.getOriginalFilename()).thenReturn("My File.jpg");
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomework));
        when(uploadFile.isFile()).thenReturn(true);
        when(uploadFile.getName()).thenReturn("My File.jpg");
        when(uploadFile.length()).thenReturn(100L);

        doReturn(uploadFile).when(mockHomeworkUploadImpl).convertMultiPartFileToFile(file);
        doReturn("test").when(mockHomeworkUploadImpl)
            .uploadFile(any(MultipartFile.class), any(HomeworkFilesBO.class), eq(false));

        when(mockJwtUtils.getUserId()).thenReturn(USER_ID);

        String message = mockHomeworkUploadImpl.teacherUploadingHomework(HOMEWORK_ID, files, "Description");
        verify(mockHomeworkRepository, times(1)).save(mockHomework);
        verify(mockHomeworkFileRepository, times(1)).save(any(HomeworkFilesBO.class));
        assertThat(message, is("Success"));
    }

    @Test
    void teacherUploadingHomeworkShouldReturnErrorString() {
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] files = new MultipartFile[1];
        files[0] = file;
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.empty());

        String message = mockHomeworkUploadImpl.teacherUploadingHomework(HOMEWORK_ID, files, "Description");

        assertThat(message, is("Homework creation not exit for this id"));
    }

    @Test
    void teacherDeleteHomeworkShouldReturnSuccessString() {
        HomeworkFilesBO mockHomeworkFilesBO = mock(HomeworkFilesBO.class);
        when(mockHomeworkFileRepository.findById(HOMEWORK_FILE_ID)).thenReturn(Optional.of(mockHomeworkFilesBO));
        doNothing().when(mockHomeworkUploadImpl).deleteFile(mockHomeworkFilesBO);

        String message = mockHomeworkUploadImpl.teacherDeleteHomework(HOMEWORK_FILE_ID);
        assertThat(message, is("Success"));
    }

    @Test
    void teacherDeleteHomeworkShouldReturnErrorString() {
        when(mockHomeworkFileRepository.findById(HOMEWORK_FILE_ID)).thenReturn(Optional.empty());

        String message = mockHomeworkUploadImpl.teacherDeleteHomework(HOMEWORK_FILE_ID);
        assertThat(message, is("homework file dosn't matching in system"));
    }

    @Test
    void teacherPublishHomeworkShouldReturnSuccessString() {
        HomeworkBO mockHomework = mock(HomeworkBO.class);
        List<AssignmentSectionBO> mockAssignmentSections = getMockAssignmentSectionBOList();

        when(mockHomework.getAssignmentSections()).thenReturn(mockAssignmentSections);
        when(mockHomework.getType()).thenReturn("ONLINE");
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomework));
        when(mockJwtUtils.getUserId()).thenReturn(USER_ID);

        String message = mockHomeworkUploadImpl.teacherPublishHomework(HOMEWORK_ID);
        verify(mockSectionRepository, times(1)).save(any(AssignmentSectionBO.class));
        assertThat(message, is("Success"));
    }

    @Test
    void teacherPublishHomeworkShouldReturnErrorString() {
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.empty());

        String message = mockHomeworkUploadImpl.teacherPublishHomework(HOMEWORK_ID);
        assertThat(message, is("homework  dosn't matching in system"));
    }

    private List<AssignmentSectionBO> getMockAssignmentSectionBOList() {
        List<AssignmentSectionBO> assignmentSections = new ArrayList<>();
        AssignmentSectionBO mockAssignmentSectionBO = mock(AssignmentSectionBO.class);
        when(mockAssignmentSectionBO.isActive()).thenReturn(true);
        when(mockAssignmentSectionBO.getPositiveMarks()).thenReturn(100f);
        assignmentSections.add(mockAssignmentSectionBO);
        return assignmentSections;
    }

}
