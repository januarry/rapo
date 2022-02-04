package com.tms.serviceImpl;

import com.tms.ServiceTest;
import com.tms.api.model.*;
import com.tms.configuration.JwtUtils;
import com.tms.entity.*;
import com.tms.repository.AssignmentFileCommentRepository;
import com.tms.repository.AssignmentFileRepository;
import com.tms.repository.HomeworkRepository;
import com.tms.repository.StudentAssignmentRepository;
import com.tms.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

public class AssignHomeworkImplTest extends ServiceTest {

    private static final String SAVE = "SAVE";
    private static final String OFFLINE = "OFFLINE";
    private static final String JWT_USER_ID = "JWT_123";
    private static final String STUDENT_ADM_ID = "STUDENT_123";
    private static final String DEFAULT_LOCATION = "Oslo";
    private static final String DEFAULT_FILE_TYPE = ".jpg";
    private static final String DEFAULT_FILE_NAME_ASSIGNMENT = "Project";
    private static final String DEFAULT_BRANCH_NAME = "Branch";
    private static final String DEFAULT_GRADE_NAME = "Grade";
    private static final String DEFAULT_SECTION_NAME = "Section";
    private static final String DEFAULT_BATCH_NAME = "Batch";
    private static final String DEFAULT_ORIENTATION_NAME = "Orientation";
    private static final String DEFAULT_HOMEWORK_DESCRIPTION = "Homework_Description";
    private static final String DEFAULT_CREATE_BY = "Default_Createby";
    private static final String DEFAULT_HOMEWORK_NAME = "Homework Name";
    private static final float DEFAULT_FILE_SIZE = 321f;
    private static final long HOMEWORK_ID = 1_000_000L;
    private static final long BRANCH_ID = 1_123L;
    private static final long GRADE_ID = 1_124L;
    private static final long SECTION_ID = 1_125L;
    private static final long BATCH_ID = 1_126L;
    private static final long ORIENTATION_ID = 1_127L;
    private static final long ASSIGNMENT_ID = 1_128L;
    private static final long ASSIGNMENT_FILE_ID = 1_129L;

    @InjectMocks
    @Spy
    private AssignHomeworkImpl mockAssignHomeworkImpl;

    @Mock
    private HomeworkRepository mockHomeworkRepository;

    @Mock
    private StudentAssignmentRepository mockStudentAssignmentRepository;

    @Mock
    private EmployeeService mockEmployeeService;

    @Mock
    private JwtUtils mockJwtUtils;

    @Mock
    private AssignmentFileCommentRepository mockAssignmentFileCommentRepository;

    @Mock
    private AssignmentFileRepository mockAssignmentFileRepository;

    @Test
    void assignHomeworkForStudentsShouldSaveHomework() {
        AssignStudentsForHomework mockAssignStudentsForHomework = mock(AssignStudentsForHomework.class);
        HomeworkBO mockHomeworkBO = mock(HomeworkBO.class);
        when(mockAssignStudentsForHomework.getHomeworkId()).thenReturn(HOMEWORK_ID);
        when(mockAssignStudentsForHomework.getStudentAssignList()).thenReturn(new ArrayList<>());
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomeworkBO));
        when(mockHomeworkBO.getStatus()).thenReturn(SAVE);
        when(mockJwtUtils.getUserId()).thenReturn(JWT_USER_ID);

        mockAssignHomeworkImpl.assignHomeworkForStudents(mockAssignStudentsForHomework);
        verify(mockHomeworkRepository, times(1)).save(mockHomeworkBO);
    }

    @Test
    void assignHomeworkForStudentsShouldSaveStudentAssignmentIfThereAreAssignList() {
        AssignStudentsForHomework mockAssignStudentsForHomework = mock(AssignStudentsForHomework.class);
        HomeworkBO mockHomeworkBO = mock(HomeworkBO.class);
        StudentAssignList mockStudentAssignList = mock(StudentAssignList.class);
        List<StudentAssignList> studentAssigns = new ArrayList<>();
        studentAssigns.add(mockStudentAssignList);
        studentAssigns.add(mockStudentAssignList);

        when(mockStudentAssignList.getStudentAdmNo()).thenReturn(STUDENT_ADM_ID);
        when(mockAssignStudentsForHomework.getHomeworkId()).thenReturn(HOMEWORK_ID);
        when(mockAssignStudentsForHomework.getStudentAssignList()).thenReturn(studentAssigns);
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomeworkBO));
        when(mockHomeworkBO.getStatus()).thenReturn(SAVE);
        when(mockJwtUtils.getUserId()).thenReturn(JWT_USER_ID);

        mockAssignHomeworkImpl.assignHomeworkForStudents(mockAssignStudentsForHomework);
        verify(mockStudentAssignmentRepository, times(2)).save(any());
        verify(mockHomeworkRepository, times(1)).save(mockHomeworkBO);
    }

    @Test
    void assignHomeworkForStudentsShouldSaveStudentAssignmentIfThereAreUnassignList() {
        AssignStudentsForHomework mockAssignStudentsForHomework = mock(AssignStudentsForHomework.class);
        HomeworkBO mockHomeworkBO = mock(HomeworkBO.class);
        StudentUnassignList mockStudentUnassignList = mock(StudentUnassignList.class);
        List<StudentUnassignList> studentUnassigns = new ArrayList<>();
        studentUnassigns.add(mockStudentUnassignList);

        when(mockHomeworkBO.getId()).thenReturn(HOMEWORK_ID);
        when(mockStudentUnassignList.getStudentAdmNo()).thenReturn(STUDENT_ADM_ID);
        when(mockAssignStudentsForHomework.getHomeworkId()).thenReturn(HOMEWORK_ID);
        when(mockAssignStudentsForHomework.getStudentAssignList()).thenReturn(new ArrayList<>());
        when(mockAssignStudentsForHomework.getStudentUnassignList()).thenReturn(studentUnassigns);
        when(mockStudentAssignmentRepository.findByStudentAndHomework(STUDENT_ADM_ID, HOMEWORK_ID))
            .thenReturn(new StudentAssignmentBO());
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomeworkBO));
        when(mockHomeworkBO.getStatus()).thenReturn(SAVE);
        when(mockJwtUtils.getUserId()).thenReturn(JWT_USER_ID);

        mockAssignHomeworkImpl.assignHomeworkForStudents(mockAssignStudentsForHomework);

        verify(mockStudentAssignmentRepository, times(1)).save(any());
        verify(mockHomeworkRepository, times(1)).save(mockHomeworkBO);
    }

    @Test
    void assignStudentsForHomeworkViewShouldReturnValue() throws UnsupportedEncodingException  {
        HomeworkBO mockHomeworkBO = mock(HomeworkBO.class);

        List<HomeworkFilesBO> mockHomeworkFiles = getDefaultHomeworkFilesBO();
        List<HomeworkBranchBO> mockHomeworkBranches = getDefaultHomeworkBranchList();
        List<HomeworkGradeBO> mockHomeworkGrades = getDefaultHomeworkGradeList();
        List<HomeworkSectionBO> mockHomeworkSection = getDefaultHomeworkSectionList();
        List<HomeworkBatchBO> mockHomeworkBatchs = getDefaultHomeworkBatchList();
        List<HomeworkOrientationBO> mockHomeworkOrientations = getDefaultHomeworkOrientationList();

        when(mockHomeworkBO.getType()).thenReturn(OFFLINE);
        when(mockHomeworkBO.getCreatedBy()).thenReturn(DEFAULT_CREATE_BY);
        when(mockHomeworkBO.getName()).thenReturn(DEFAULT_HOMEWORK_NAME);
        when(mockHomeworkBO.getDescription()).thenReturn(DEFAULT_HOMEWORK_DESCRIPTION);
        when(mockHomeworkBO.getHomeWorkFiles()).thenReturn(mockHomeworkFiles);
        when(mockHomeworkBO.getHomeworkBranchList()).thenReturn(mockHomeworkBranches);
        when(mockHomeworkBO.getHomeworkGradeList()).thenReturn(mockHomeworkGrades);
        when(mockHomeworkBO.getHomeworkSectionList()).thenReturn(mockHomeworkSection);
        when(mockHomeworkBO.getHomeworkBatchList()).thenReturn(mockHomeworkBatchs);
        when(mockHomeworkBO.getHomeworkOrientationList()).thenReturn(mockHomeworkOrientations);
        when(mockEmployeeService.getEmployee(DEFAULT_CREATE_BY)).thenReturn(new EmployeeInfo());

        doReturn(new Students()).when(mockAssignHomeworkImpl).getStudents();
        doReturn(new SubjectInfo()).when(mockAssignHomeworkImpl).getSubjectInfo(mockHomeworkBO);

        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomeworkBO));

        StudentsHomeworkView studentsHomeworkView = mockAssignHomeworkImpl.assignStudentsForHomeworkView(HOMEWORK_ID);
        assertThat(studentsHomeworkView.getHomeWorkType(), is(OFFLINE));
        assertThat(studentsHomeworkView.getHomeWorkName(), is(DEFAULT_HOMEWORK_NAME));
    }

    @Test
    void deleteFileCommentsShouldReturnStringSuccess() {
        AssignmentFileCommentBO assignmentFileCommentBO = mock(AssignmentFileCommentBO.class);
        when(mockAssignmentFileCommentRepository.findById(ASSIGNMENT_ID)).thenReturn(Optional.of(assignmentFileCommentBO));

        String message = mockAssignHomeworkImpl.deletefileComments(ASSIGNMENT_ID);
        assertThat(message, is("Success"));
    }

    @Test
    void deleteFileCommentsShouldReturnStringFileDoesntMatching() {
        when(mockAssignmentFileCommentRepository.findById(ASSIGNMENT_ID)).thenReturn(Optional.empty());

        String message = mockAssignHomeworkImpl.deletefileComments(ASSIGNMENT_ID);
        assertThat(message, is("assignment file comments are dosn't matching in system"));
    }

    @Test
    void getAssignmentFileCommentsShouldReturnListOfFileComments() {
        AssignmentFileBO mockAssignmentFile = mock(AssignmentFileBO.class);
        AssignmentFileCommentBO mockAssignmentFileCommentBO = mock(AssignmentFileCommentBO.class);

        when(mockAssignmentFileCommentBO.isActive()).thenReturn(true);
        when(mockAssignmentFileCommentBO.getId()).thenReturn(ASSIGNMENT_FILE_ID);
        when(mockAssignmentFileCommentBO.getComments()).thenReturn("Some comments");
        List<AssignmentFileCommentBO> fileComments = new ArrayList<>();
        fileComments.add(mockAssignmentFileCommentBO);

        when(mockAssignmentFile.getAssignmentFileComments()).thenReturn(fileComments);
        when(mockAssignmentFileRepository.findById(ASSIGNMENT_FILE_ID)).thenReturn(Optional.of(mockAssignmentFile));

        List<FileComment> results = mockAssignHomeworkImpl.getAssignmentFileComments(ASSIGNMENT_FILE_ID);
        assertThat(results.get(0).getComment(), is("Some comments"));
        assertThat(results.get(0).getAssignmentFileId(), is(ASSIGNMENT_FILE_ID));
    }

    private List<HomeworkFilesBO> getDefaultHomeworkFilesBO() {
        List<HomeworkFilesBO> homeworkFiles = new ArrayList<>();
        HomeworkFilesBO mockHomeworkFilesBO = mock(HomeworkFilesBO.class);
        when(mockHomeworkFilesBO.isActive()).thenReturn(true);
        when(mockHomeworkFilesBO.getLocation()).thenReturn(DEFAULT_LOCATION);
        when(mockHomeworkFilesBO.getFileSize()).thenReturn(DEFAULT_FILE_SIZE);
        when(mockHomeworkFilesBO.getFileType()).thenReturn(DEFAULT_FILE_TYPE);
        when(mockHomeworkFilesBO.getName()).thenReturn(DEFAULT_FILE_NAME_ASSIGNMENT);

        homeworkFiles.add(mockHomeworkFilesBO);
        return homeworkFiles;
    }

    private List<HomeworkBranchBO> getDefaultHomeworkBranchList() {
        List<HomeworkBranchBO> homeworkBranchs = new ArrayList<>();
        HomeworkBranchBO homeworkBranch = mock(HomeworkBranchBO.class);
        when(homeworkBranch.getBranchId()).thenReturn(BRANCH_ID);
        when(homeworkBranch.getBranchName()).thenReturn(DEFAULT_BRANCH_NAME);

        homeworkBranchs.add(homeworkBranch);
        return homeworkBranchs;
    }

    private List<HomeworkGradeBO> getDefaultHomeworkGradeList() {
        List<HomeworkGradeBO> homeworkGrades = new ArrayList<>();
        HomeworkGradeBO homeworkGrade = mock(HomeworkGradeBO.class);
        when(homeworkGrade.getGradeId()).thenReturn(GRADE_ID);
        when(homeworkGrade.getGradeName()).thenReturn(DEFAULT_GRADE_NAME);

        homeworkGrades.add(homeworkGrade);
        return homeworkGrades;
    }

    private List<HomeworkSectionBO> getDefaultHomeworkSectionList() {
        List<HomeworkSectionBO> homeworkSections = new ArrayList<>();
        HomeworkSectionBO homeworkSection = mock(HomeworkSectionBO.class);
        when(homeworkSection.getSectionId()).thenReturn(SECTION_ID);
        when(homeworkSection.getSectionName()).thenReturn(DEFAULT_SECTION_NAME);

        homeworkSections.add(homeworkSection);
        return homeworkSections;
    }

    private List<HomeworkBatchBO> getDefaultHomeworkBatchList() {
        List<HomeworkBatchBO> homeworkBatchs = new ArrayList<>();
        HomeworkBatchBO homeworkBatch = mock(HomeworkBatchBO.class);
        when(homeworkBatch.getBatchId()).thenReturn(BATCH_ID);
        when(homeworkBatch.getBatchName()).thenReturn(DEFAULT_BATCH_NAME);

        homeworkBatchs.add(homeworkBatch);
        return homeworkBatchs;
    }

    private List<HomeworkOrientationBO> getDefaultHomeworkOrientationList() {
        List<HomeworkOrientationBO> homeworkOrientations = new ArrayList<>();
        HomeworkOrientationBO homeworkOrientation = mock(HomeworkOrientationBO.class);
        when(homeworkOrientation.getOrientationId()).thenReturn(ORIENTATION_ID);
        when(homeworkOrientation.getOrientationName()).thenReturn(DEFAULT_ORIENTATION_NAME);

        homeworkOrientations.add(homeworkOrientation);
        return homeworkOrientations;
    }

}