package com.tms.serviceImpl;

import com.tms.ServiceTest;
import com.tms.api.model.SectionList;
import com.tms.api.model.SectionListSection;
import com.tms.configuration.JwtUtils;
import com.tms.entity.AssignmentQuestionsBO;
import com.tms.entity.AssignmentSectionBO;
import com.tms.entity.HomeworkBO;
import com.tms.repository.AssignmentQuestionsRepository;
import com.tms.repository.HomeworkRepository;
import com.tms.repository.SectionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SectionServiceImplTest extends ServiceTest {

    private static final String DEFAULT_SECTION_NAME = "Default Section";
    private static final long HOMEWORK_ID = 123L;
    private static final long SECTION_ID = 124L;
    private static final String USER_ID = "TEST_123";

    @InjectMocks
    @Spy
    private SectionServiceImpl mockSectionServiceImpl;

    @Mock
    private SectionRepository mockSectionRepository;

    @Mock
    private HomeworkRepository mockHomeworkRepository;

    @Mock
    private AssignmentQuestionsRepository mockAssignmentQuestionsRepository;

    @Mock
    private JwtUtils mockJwtUtils;

    @Test
    void addSectionShouldReturnSuccessString() {
        HomeworkBO mockHomework = mock(HomeworkBO.class);
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.of(mockHomework));

        String message = mockSectionServiceImpl.addSection(HOMEWORK_ID, DEFAULT_SECTION_NAME);
        verify(mockHomeworkRepository, times(1)).save(mockHomework);
        assertThat(message, is("SUCCESS"));
    }

    @Test
    void addSectionShouldReturnErrorString() {
        HomeworkBO mockHomework = mock(HomeworkBO.class);
        when(mockHomeworkRepository.findById(HOMEWORK_ID)).thenReturn(Optional.empty());

        String message = mockSectionServiceImpl.addSection(HOMEWORK_ID, DEFAULT_SECTION_NAME);
        verify(mockHomeworkRepository, times(0)).save(mockHomework);
        assertThat(message, is("homework id dosen't found in systmem"));
    }

    @Test
    void updateSectionShouldReturnSuccessString() {
        AssignmentSectionBO mockAssignmentSectionBO = mock(AssignmentSectionBO.class);
        when(mockSectionRepository.findById(SECTION_ID)).thenReturn(Optional.of(mockAssignmentSectionBO));

        String message = mockSectionServiceImpl.updateSection(SECTION_ID, DEFAULT_SECTION_NAME);
        verify(mockSectionRepository, times(1)).save(mockAssignmentSectionBO);
        assertThat(message, is("SUCCESS"));
    }

    @Test
    void deleteSectionShouldReturnSuccessString() {
        AssignmentSectionBO mockAssignmentSectionBO = mock(AssignmentSectionBO.class);
        when(mockSectionRepository.findById(SECTION_ID)).thenReturn(Optional.of(mockAssignmentSectionBO));

        String message = mockSectionServiceImpl.deleteSection(SECTION_ID);
        verify(mockAssignmentSectionBO, times(1)).setActive(false);
        verify(mockSectionRepository, times(1)).save(mockAssignmentSectionBO);
        assertThat(message, is("SUCCESS"));
    }

    @Test
    void sectionMarksShouldReturnSuccessString() {
        AssignmentSectionBO mockAssignmentSectionBO = mock(AssignmentSectionBO.class);
        when(mockSectionRepository.findById(SECTION_ID)).thenReturn(Optional.of(mockAssignmentSectionBO));
        when(mockJwtUtils.getUserId()).thenReturn(USER_ID);

        String message = mockSectionServiceImpl.sectionMarks(SECTION_ID, 100f, -90f);
        verify(mockSectionRepository, times(1)).save(mockAssignmentSectionBO);
        assertThat(message, is("SUCCESS"));
    }

    @Test
    void sectionListShouldReturnListOfSection() {
        AssignmentSectionBO mockAssignmentSectionBO = mock(AssignmentSectionBO.class);
        List<AssignmentSectionBO> mockAssignmentSections = new ArrayList<>();
        mockAssignmentSections.add(mockAssignmentSectionBO);

        when(mockAssignmentSectionBO.getId()).thenReturn(SECTION_ID);
        when(mockAssignmentSectionBO.getSectionName()).thenReturn(DEFAULT_SECTION_NAME);
        when(mockAssignmentSectionBO.getPositiveMarks()).thenReturn(100f);
        when(mockAssignmentSectionBO.getNegativeMarks()).thenReturn(-90f);
        when(mockSectionRepository.findByHomeWorkId(HOMEWORK_ID)).thenReturn(mockAssignmentSections);

        List<SectionList> results = mockSectionServiceImpl.sectionList(HOMEWORK_ID);
        List<SectionListSection> sections = results.get(0).getSection();
        assertThat(sections.get(0).getSectionId(), is(SECTION_ID));
        assertThat(sections.get(0).getPositiveMarks(), is(100f));
        assertThat(sections.get(0).getNegativeMarks(), is(-90f));
    }

    @Test
    void marksQuestionShouldReturnSuccessString() throws Exception {
        AssignmentSectionBO mockAssignmentSectionBO = mock(AssignmentSectionBO.class);
        when(mockSectionRepository.findById(SECTION_ID)).thenReturn(Optional.of(mockAssignmentSectionBO));

        String message = mockSectionServiceImpl.marksQuestion(SECTION_ID, 100f, -90f);
        verify(mockSectionRepository, times(1)).save(mockAssignmentSectionBO);
        assertThat(message, is("success"));
    }

    @Test
    void marksQuestionShouldThrowException() throws Exception {
        when(mockSectionRepository.findById(SECTION_ID)).thenReturn(Optional.empty());
        assertThrows(Exception.class,
            () -> mockSectionServiceImpl.marksQuestion(SECTION_ID, 100f, -90f)
        );
    }

    @Test
    void deleteQuestionShouldReturnSuccessString() {
        AssignmentQuestionsBO mockAssignmentQuestionsBO = mock(AssignmentQuestionsBO.class);
        when(mockAssignmentQuestionsRepository.findById(SECTION_ID)).thenReturn(Optional.of(mockAssignmentQuestionsBO));

        String message = mockSectionServiceImpl.deleteQuestion(SECTION_ID);

        verify(mockAssignmentQuestionsBO, times(1)).setActive(false);
        verify(mockAssignmentQuestionsRepository, times(1)).save(mockAssignmentQuestionsBO);
        assertThat(message, is("success"));
    }
}
