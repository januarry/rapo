package com.tms.serviceImpl;

import com.tms.ServiceTest;
import com.tms.api.model.*;
import com.tms.configuration.JwtUtils;
import com.tms.entity.*;
import com.tms.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TeacherHomeworkImplTest extends ServiceTest {

    private static final String DEFAULT_LOCATION = "Oslo";
    private static final String DEFAULT_STATUS = "PUBLISH";
    private static final String DEFAULT_GRADE_NAME = "Grade";
    private static final String DEFAULT_FILE_TYPE = ".jpg";
    private static final String DEFAULT_FILE_NAME_ASSIGNMENT = "Project";
    private static final String DEFAULT_BATCH_NAME = "Batch";
    private static final String DEFAULT_BRANCH_NAME = "Branch";
    private static final String DEFAULT_PROGRAM_NAME = "Program";
    private static final String DEFAULT_SECTION_NAME = "Section";
    private static final String DEFAULT_CLASS_NAME = "Class";
    private static final String USER_ID = "TEST_123";
    private static final long HOMEWORK_ID = 123L;
    private static final long GRADE_ID = 124L;
    private static final long BATCH_ID = 1_126L;
    private static final long BRANCH_ID = 1_123L;
    private static final long PROGRAM_ID = 1_125L;
    private static final long SECTION_ID = 1_126L;
    private static final long CLASS_ID = 1_127L;
    private static final long UPLOAD_FILE_ID = 1_321L;

    private static final float DEFAULT_FILE_SIZE = 321f;

    @InjectMocks
    @Spy
    private TeacherHomeworkImpl mockTeacherHomeworkImpl;

    @Mock
    private HomeworkRepository mockHomeworkRepository;

    @Mock
    private SubjectRepository mockSubjectRepository;

    @Mock
    private StudentAssignmentRepository mockStudentAssignmentRepository;

    @Mock
    private HomeworkFileRepository mockHomeworkFileRepository;

    @Mock
    private AssignmentFileRepository mockAssignmentFileRepository;

    @Mock
    private AssignmentFileCommentRepository mockAssignmentFileCommentRepository;

    @Mock
    private HomeworkOrientationRepository mockHomeworkOrientationRepository;

    @Mock
    private HomeworkGradeRepository mockHomeworkGradeRepository;

    @Mock
    private HomeworkBatchRepository mockHomeworkBatchRepository;

    @Mock
    private HomeworkBranchRepository mockHomeworkBranchRepository;

    @Mock
    private HomeworkSectionRepository mockHomeworkSectionRepository;

    @Mock
    private JwtUtils mockJwtUtils;

    @Mock
    private AssignmentResultRepository mockAssignmentResultRepository;

    @Mock
    private QBToken mockQBToken;

    @Test
    void getHomeworkListsShouldReturnListOfHomeworks() throws UnsupportedEncodingException {
        HomeworkBO onGoingHomework = mock(HomeworkBO.class);
        HomeworkBO upComingHomework = mock(HomeworkBO.class);
        HomeworkBO pastHomework = mock(HomeworkBO.class);
        HomeworkBO draftHomework = mock(HomeworkBO.class);
        List<HomeworkBO> homeworkBOs = new ArrayList<>();

        Instant beforePresent = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant startDate = Instant.now().plus(1, ChronoUnit.DAYS);
        Instant endDate = Instant.now().plus(2, ChronoUnit.DAYS);
        Instant pastDate = Instant.now().plus(1, ChronoUnit.DAYS);
        when(onGoingHomework.getStart_date()).thenReturn(Date.from(startDate));
        when(onGoingHomework.getEnd_date()).thenReturn(Date.from(endDate));
        when(onGoingHomework.getStatus()).thenReturn(DEFAULT_STATUS);
        when(onGoingHomework.getId()).thenReturn(1L);

        when(upComingHomework.getStart_date()).thenReturn(Date.from(beforePresent));
        when(upComingHomework.getEnd_date()).thenReturn(Date.from(endDate));
        when(upComingHomework.getStatus()).thenReturn(DEFAULT_STATUS);
        when(upComingHomework.getId()).thenReturn(1L);

        when(pastHomework.getStart_date()).thenReturn(Date.from(pastDate));
        when(pastHomework.getEnd_date()).thenReturn(Date.from(pastDate));
        when(pastHomework.getStatus()).thenReturn(DEFAULT_STATUS);
        when(pastHomework.getId()).thenReturn(1L);

        when(draftHomework.getStart_date()).thenReturn(Date.from(pastDate));
        when(draftHomework.getEnd_date()).thenReturn(Date.from(pastDate));
        when(draftHomework.getStatus()).thenReturn("SAVE");
        when(draftHomework.getId()).thenReturn(1L);

        homeworkBOs.add(onGoingHomework);
        homeworkBOs.add(upComingHomework);
        homeworkBOs.add(pastHomework);
        homeworkBOs.add(draftHomework);

        when(mockHomeworkRepository.findByActive(true)).thenReturn(homeworkBOs);
        doReturn(new ArrayList<>()).when(mockTeacherHomeworkImpl).homeWorkLists(anyList());

        List<HomeworkList> results = mockTeacherHomeworkImpl.getHomeworkLists();

        assertThat(results.size(), is(4));
        for (HomeworkList homework : results) {
            if (homework.getHomeworkListType().equalsIgnoreCase("ONGOING")) {
                assertThat(homework.getHomeworkListType(), is("ONGOING"));
            } else if (homework.getHomeworkListType().equalsIgnoreCase("UPCOMING")) {
                assertThat(homework.getHomeworkListType(), is("UPCOMING"));
            } else if (homework.getHomeworkListType().equalsIgnoreCase("PAST")) {
                assertThat(homework.getHomeworkListType(), is("PAST"));
            } else {
                assertThat(homework.getHomeworkListType(), is("DRAFT"));
            }
        }
    }

    @Test
    void studentHomeworkEvaluationShouldReturnListOfStudentHomeworkEvaluations() {
        HomeworkBO mockHomework = mock(HomeworkBO.class);
        List<StudentAssignmentBO> mockStudentAssignmentBOList = getMockStudentAssignmentBOList();

        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomework));
        when(mockStudentAssignmentRepository.findByHomeWorkId(mockHomework)).thenReturn(mockStudentAssignmentBOList);

        List<StudentHomeworkEvaluationList> results = mockTeacherHomeworkImpl.studentHomeworkEvaluation(HOMEWORK_ID);

        assertThat(results.size(), is(3));

        for (StudentHomeworkEvaluationList studentHomeworkEvaluationList : results) {
            if (studentHomeworkEvaluationList.getHomeworkListType().equalsIgnoreCase("SUBMITED")) {
                assertThat(studentHomeworkEvaluationList.getHomeworkListType(), is("SUBMITED"));
            } else if (studentHomeworkEvaluationList.getHomeworkListType().equalsIgnoreCase("NOT SUBMITED")) {
                assertThat(studentHomeworkEvaluationList.getHomeworkListType(), is("NOT SUBMITED"));
            } else {
                assertThat(studentHomeworkEvaluationList.getHomeworkListType(), is("EVALUATED"));
            }
        }
    }

    @Test
    void homeworkEvaluationStuListShouldReturnValue() throws UnsupportedEncodingException {
        List<HomeworkGradeBO> mockHomeworkGrades = getDefaultHomeworkGradeList();
        List<HomeworkFilesBO> mockHomeworkFiles = getDefaultHomeworkFilesBO();

        HomeworkBO mockHomework = mock(HomeworkBO.class);
        when(mockHomework.getEnd_date()).thenReturn(new Date());
        when(mockHomework.getMetaData()).thenReturn("Metadata");
        when(mockHomework.getName()).thenReturn("Homework");
        when(mockHomework.getHomeworkGradeList()).thenReturn(mockHomeworkGrades);

        List<StudentAssignmentBO> mockStudentAssignmentBOList = getMockStudentAssignmentBOList();
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomework));
        when(mockStudentAssignmentRepository.findByHomeWorkId(mockHomework)).thenReturn(mockStudentAssignmentBOList);
        when(mockHomeworkFileRepository.findByHomeWorkId(mockHomework)).thenReturn(mockHomeworkFiles);

        SubjectInfo mockSubjectInfo = mock(SubjectInfo.class);
        when(mockSubjectInfo.getSubjectName()).thenReturn("Subject Info Name");
        when(mockSubjectInfo.getSubjectId()).thenReturn(123);
        doReturn(mockSubjectInfo).when(mockTeacherHomeworkImpl).getSubjectInfo(mockHomework);

        StudentHomeworkSubmissions result = mockTeacherHomeworkImpl.studentHomeworkEvaluationView(HOMEWORK_ID);
        assertThat(result.getHomeworkName(), is("Homework"));
        assertThat(result.getSubject(), is("Subject Info Name"));
        assertThat(result.getSubjectId(), is(123L));
    }

    @Test
    void createHomeworkShouldReturnId() throws Exception {
        List<StudentBatch> mockStudentBatches = getDefaultStudentBatchs();
        List<StudentBranch> mockStudentBranches = getDefaultStudentBranches();
        List<StudentProgram> mockStudentPrograms = getDefaultStudentPrograms();
        List<StudentSection> mockStudentSections = getDefaultStudentSections();

        CreationHomework mockCreationHomework = mock(CreationHomework.class);
        when(mockCreationHomework.getHomeworkId()).thenReturn(123L);
        when(mockCreationHomework.getHomeworkName()).thenReturn("Homework");
        when(mockCreationHomework.getEndDate()).thenReturn("04/02/2022 14:36 PM");
        when(mockCreationHomework.getStartDate()).thenReturn("04/02/2022 10:36 PM");
        when(mockCreationHomework.getEvaluation()).thenReturn(CreationHomework.EvaluationEnum.REQUIRED);
        when(mockCreationHomework.getSubjectId()).thenReturn(123L);
        when(mockCreationHomework.getSubmission()).thenReturn(CreationHomework.SubmissionEnum.REQUIRED);
        when(mockCreationHomework.getTeacherRemarks()).thenReturn("Remark");
        when(mockCreationHomework.getTotalMarks()).thenReturn(100);
        when(mockCreationHomework.getStudentBatchs()).thenReturn(mockStudentBatches);
        when(mockCreationHomework.getStudentBranchs()).thenReturn(mockStudentBranches);
        when(mockCreationHomework.getStudentPrograms()).thenReturn(mockStudentPrograms);
        when(mockCreationHomework.getStudentSections()).thenReturn(mockStudentSections);

        HomeworkBO mockHomework = mock(HomeworkBO.class);
        when(mockHomework.getId()).thenReturn(1000L);
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomework));
        when(mockJwtUtils.getUserId()).thenReturn(USER_ID);

        Long id = mockTeacherHomeworkImpl.createHomework(mockCreationHomework);
        verify(mockHomeworkRepository, times(1)).save(any());
        verify(mockHomeworkBatchRepository, times(1)).save(any());
        verify(mockHomeworkBranchRepository, times(1)).save(any());
        verify(mockHomeworkOrientationRepository, times(1)).save(any());
        verify(mockHomeworkSectionRepository, times(1)).save(any());
        verify(mockHomeworkGradeRepository, times(1)).save(any());
        assertThat(id, is(1000L));
    }

    @Test
    void setUploadedFileCommentsShouldSave() {
        AssignmentFileBO mockAssignmentFileBO = mock(AssignmentFileBO.class);
        when(mockAssignmentFileRepository.findById(UPLOAD_FILE_ID)).thenReturn(Optional.of(mockAssignmentFileBO));

        mockTeacherHomeworkImpl.setUploadedFileComments(UPLOAD_FILE_ID, "Comments");
        verify(mockAssignmentFileCommentRepository, times(1)).save(any());
    }

    @Test
    void deleteUploadedFileCommentsShouldSave() {
        AssignmentFileCommentBO mockAssignmentFileCommentBO = mock(AssignmentFileCommentBO.class);
        when(mockAssignmentFileCommentRepository.getById(UPLOAD_FILE_ID)).thenReturn(mockAssignmentFileCommentBO);

        mockTeacherHomeworkImpl.deleteUploadedFileComments(UPLOAD_FILE_ID);
        verify(mockAssignmentFileCommentRepository, times(1)).save(any());
    }

    @Test
    void setAssignmentRemarksShouldSave() {
        StudentAssignmentBO mockStudentAssignmentBO = mock(StudentAssignmentBO.class);
        when(mockStudentAssignmentRepository.findById(123L)).thenReturn(Optional.of(mockStudentAssignmentBO));
        when(mockJwtUtils.getUserId()).thenReturn(USER_ID);
        mockTeacherHomeworkImpl.setAssignmentRemarks(123L, "remarks", 70f);
        verify(mockAssignmentResultRepository, times(1)).save(any());
        verify(mockStudentAssignmentRepository, times(1)).save(any());
    }

    @Test
    void deleteHomeworkShouldSave() throws Exception {
        HomeworkBO mockHomeworkBO = mock(HomeworkBO.class);
        when(mockHomeworkRepository.findById(123L)).thenReturn(Optional.of(mockHomeworkBO));

        mockTeacherHomeworkImpl.deleteHomework(123L);
        verify(mockHomeworkRepository, times(1)).save(any());
    }

    @Test
    void getStudentAssignmentTestShouldReturnListOfAssignmentTestPaper() {
        List<AssignmentSectionBO> assignmentSections = getMockAssignmentSectionBOList();
        SectionAssignmentTest[] sectionAssignmentTests = new SectionAssignmentTest[1];
        SectionAssignmentTest mockSectionAssignmentTest = mock(SectionAssignmentTest.class);
        when(mockSectionAssignmentTest.getId()).thenReturn("assign-test-id");
        sectionAssignmentTests[0] = mockSectionAssignmentTest;

        StudentAssignmentBO mockStudentAssignmentBO = mock(StudentAssignmentBO.class);
        AssignmentQuestionsBO mockAssignmentQuestionsBO = mock(AssignmentQuestionsBO.class);
        StudentAssignmentQuestionsBO mockStudentAssignmentQuestionsBO = mock(StudentAssignmentQuestionsBO.class);
        HomeworkBO mockHomeworkBO = mock(HomeworkBO.class);

        when(mockAssignmentQuestionsBO.getQuestionId()).thenReturn("assign-test-id");
        when(mockStudentAssignmentQuestionsBO.getAssignmentQuestion()).thenReturn(mockAssignmentQuestionsBO);
        when(mockStudentAssignmentQuestionsBO.isCorrect()).thenReturn(true);
        when(mockStudentAssignmentQuestionsBO.isClearResponse()).thenReturn(true);
        when(mockStudentAssignmentQuestionsBO.isMarkForReview()).thenReturn(false);
        when(mockStudentAssignmentQuestionsBO.isVisited()).thenReturn(false);
        when(mockStudentAssignmentQuestionsBO.getResponse()).thenReturn("Response");
        when(mockStudentAssignmentQuestionsBO.getQuestionGap()).thenReturn(100L);
        when(mockStudentAssignmentQuestionsBO.getAttemptTime()).thenReturn(new Date());
        when(mockStudentAssignmentBO.getSectionQuestion(123L, "assign-test-id")).thenReturn(mockStudentAssignmentQuestionsBO);
        when(mockHomeworkBO.getAssignmentSections()).thenReturn(assignmentSections);
        when(mockStudentAssignmentBO.getHomeWorkId()).thenReturn(mockHomeworkBO);
        when(mockStudentAssignmentRepository.findById(123L)).thenReturn(Optional.of(mockStudentAssignmentBO));
        doReturn(sectionAssignmentTests).when(mockTeacherHomeworkImpl).runSectionAssignmentTest(anyList());

        List<AssignmentTestPaper> result = mockTeacherHomeworkImpl.getStudentAssignmentTest(123L);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSectionId(), is(123L));
        assertThat(result.get(0).getSectionName(), is(DEFAULT_SECTION_NAME));
        assertThat(result.get(0).getPositiveMarks(), is(100f));
        assertThat(result.get(0).getNegativeMarks(), is(-90f));
    }

    @Test
    void getHomeworkAssignmentTestShouldReturnListOfAssignmentTestPaper() {
        List<AssignmentSectionBO> assignmentSections = getMockAssignmentSectionBOList();
        SectionAssignmentTest[] sectionAssignmentTests = new SectionAssignmentTest[1];
        SectionAssignmentTest mockSectionAssignmentTest = mock(SectionAssignmentTest.class);
        when(mockSectionAssignmentTest.getId()).thenReturn("assign-test-id");
        sectionAssignmentTests[0] = mockSectionAssignmentTest;
        HomeworkBO mockHomeworkBO = mock(HomeworkBO.class);

        when(mockHomeworkBO.getAssignmentSections()).thenReturn(assignmentSections);
        when(mockHomeworkRepository.findById(123L)).thenReturn(Optional.of(mockHomeworkBO));
        doReturn(sectionAssignmentTests).when(mockTeacherHomeworkImpl).runSectionAssignmentTest(anyList());
        List<AssignmentTestPaper> result = mockTeacherHomeworkImpl.getHomeworkAssignmentTest(123L);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSectionId(), is(123L));
        assertThat(result.get(0).getSectionName(), is(DEFAULT_SECTION_NAME));
        assertThat(result.get(0).getPositiveMarks(), is(100f));
        assertThat(result.get(0).getNegativeMarks(), is(-90f));
    }

    @Test
    void deleteHomeworkFileShouldSave() throws Exception {
        HomeworkFilesBO mockHomeworkFilesBO = mock(HomeworkFilesBO.class);
        when(mockHomeworkFileRepository.findById(123L)).thenReturn(Optional.of(mockHomeworkFilesBO));
        when(mockJwtUtils.getUserId()).thenReturn(USER_ID);

        mockTeacherHomeworkImpl.deleteHomeworkFile(123L);
        verify(mockHomeworkFileRepository, times(1)).save(any());
    }

    private List<StudentAssignmentBO> getMockStudentAssignmentBOList() {
        List<StudentAssignmentBO> studentAssignments = new ArrayList<>();
        StudentAssignmentBO mockStudentAssignmentSubmitted = mock(StudentAssignmentBO.class);
        StudentAssignmentBO mockStudentAssignmentNotSubmitted = mock(StudentAssignmentBO.class);
        StudentAssignmentBO mockStudentAssignmentEvaluated = mock(StudentAssignmentBO.class);

        AssignmentResultBO mockAssignmentResultBO = mock(AssignmentResultBO.class);
        when(mockAssignmentResultBO.getTotalMarks()).thenReturn(100f);

        when(mockStudentAssignmentSubmitted.getResult()).thenReturn(mockAssignmentResultBO);
        when(mockStudentAssignmentSubmitted.getId()).thenReturn(123L);
        when(mockStudentAssignmentSubmitted.getStudent()).thenReturn("Andy");

        when(mockStudentAssignmentEvaluated.getId()).thenReturn(124L);
        when(mockStudentAssignmentEvaluated.getStudent()).thenReturn("Ray");
        when(mockStudentAssignmentEvaluated.getResult()).thenReturn(mockAssignmentResultBO);

        when(mockStudentAssignmentNotSubmitted.getId()).thenReturn(125L);
        when(mockStudentAssignmentNotSubmitted.getStudent()).thenReturn("Theo");
        when(mockStudentAssignmentNotSubmitted.getIsCompleted()).thenReturn(false);
        when(mockStudentAssignmentNotSubmitted.getAttemptCount()).thenReturn(1L);
        when(mockStudentAssignmentNotSubmitted.getResult()).thenReturn(mockAssignmentResultBO);
        studentAssignments.add(mockStudentAssignmentSubmitted);
        studentAssignments.add(mockStudentAssignmentNotSubmitted);
        studentAssignments.add(mockStudentAssignmentEvaluated);

        return studentAssignments;
    }

    private List<HomeworkGradeBO> getDefaultHomeworkGradeList() {
        List<HomeworkGradeBO> homeworkGrades = new ArrayList<>();
        HomeworkGradeBO homeworkGrade = mock(HomeworkGradeBO.class);
        when(homeworkGrade.getGradeId()).thenReturn(GRADE_ID);
        when(homeworkGrade.getGradeName()).thenReturn(DEFAULT_GRADE_NAME);

        homeworkGrades.add(homeworkGrade);
        return homeworkGrades;
    }

    private List<HomeworkFilesBO> getDefaultHomeworkFilesBO() {
        List<HomeworkFilesBO> homeworkFiles = new ArrayList<>();
        HomeworkFilesBO mockHomeworkFilesBO = mock(HomeworkFilesBO.class);
        when(mockHomeworkFilesBO.isActive()).thenReturn(true);
        when(mockHomeworkFilesBO.getLocation()).thenReturn(DEFAULT_LOCATION);
        when(mockHomeworkFilesBO.getFileSize()).thenReturn(DEFAULT_FILE_SIZE);
        when(mockHomeworkFilesBO.getFileType()).thenReturn(DEFAULT_FILE_TYPE);
        when(mockHomeworkFilesBO.getName()).thenReturn(DEFAULT_FILE_NAME_ASSIGNMENT);
        when(mockHomeworkFilesBO.getCreatedOn()).thenReturn(new Date());
        when(mockHomeworkFilesBO.getUpdatedOn()).thenReturn(new Date());

        homeworkFiles.add(mockHomeworkFilesBO);
        return homeworkFiles;
    }

    private List<StudentBatch> getDefaultStudentBatchs() {
        List<StudentBatch> studentBatchs = new ArrayList<>();
        StudentBatch studentBatch = mock(StudentBatch.class);
        when(studentBatch.getBatchId()).thenReturn(BATCH_ID);
        when(studentBatch.getBatchName()).thenReturn(DEFAULT_BATCH_NAME);

        studentBatchs.add(studentBatch);
        return studentBatchs;
    }

    private List<StudentBranch> getDefaultStudentBranches() {
        List<StudentBranch> studentBranches = new ArrayList<>();
        StudentBranch studentBranch = mock(StudentBranch.class);
        when(studentBranch.getBranchId()).thenReturn(BRANCH_ID);
        when(studentBranch.getBranchName()).thenReturn(DEFAULT_BRANCH_NAME);

        studentBranches.add(studentBranch);
        return studentBranches;
    }

    private List<StudentProgram> getDefaultStudentPrograms() {
        List<StudentProgram> studentPrograms = new ArrayList<>();
        StudentProgram studentProgram = mock(StudentProgram.class);
        when(studentProgram.getProgramId()).thenReturn(PROGRAM_ID);
        when(studentProgram.getProgramName()).thenReturn(DEFAULT_PROGRAM_NAME);

        studentPrograms.add(studentProgram);
        return studentPrograms;
    }

    private List<StudentSection> getDefaultStudentSections() {
        List<StudentSection> studentSections = new ArrayList<>();
        StudentSection studentSection = mock(StudentSection.class);
        when(studentSection.getSectionId()).thenReturn(SECTION_ID);
        when(studentSection.getSectionName()).thenReturn(DEFAULT_SECTION_NAME);
        when(studentSection.getClassId()).thenReturn(CLASS_ID);
        when(studentSection.getClassName()).thenReturn(DEFAULT_CLASS_NAME);

        studentSections.add(studentSection);
        return studentSections;
    }

    private List<AssignmentSectionBO> getMockAssignmentSectionBOList() {
        AssignmentQuestionsBO mockAssignmentQuestionsBO = mock(AssignmentQuestionsBO.class);
        when(mockAssignmentQuestionsBO.getQuestionId()).thenReturn("QuestionID");
        when(mockAssignmentQuestionsBO.isActive()).thenReturn(true);
        when(mockAssignmentQuestionsBO.getId()).thenReturn(123L);
        List<AssignmentQuestionsBO> mockAssignmentQuestions = new ArrayList<>();
        mockAssignmentQuestions.add(mockAssignmentQuestionsBO);

        List<AssignmentSectionBO> assignmentSections = new ArrayList<>();
        AssignmentSectionBO mockAssignmentSectionBO = mock(AssignmentSectionBO.class);
        when(mockAssignmentSectionBO.getQuestion("assign-test-id")).thenReturn(mockAssignmentQuestionsBO);
        when(mockAssignmentSectionBO.getId()).thenReturn(123L);
        when(mockAssignmentSectionBO.getSectionName()).thenReturn(DEFAULT_SECTION_NAME);
        when(mockAssignmentSectionBO.isActive()).thenReturn(true);
        when(mockAssignmentSectionBO.getPositiveMarks()).thenReturn(100f);
        when(mockAssignmentSectionBO.getNegativeMarks()).thenReturn(-90f);
        when(mockAssignmentSectionBO.getAssignmentQuestions()).thenReturn(mockAssignmentQuestions);
        assignmentSections.add(mockAssignmentSectionBO);
        return assignmentSections;
    }

}
