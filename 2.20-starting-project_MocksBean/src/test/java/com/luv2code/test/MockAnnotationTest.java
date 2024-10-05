package com.luv2code.test;


import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes= MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
  private  ApplicationContext context;

    @Autowired
  private  CollegeStudent  studentOne;

    @Autowired
   private StudentGrades studentGrades;

    //we are mocking the application dao dependency on applicationService class
    //so that we can easily apply the unit test on service class in an isolation with minimum
    //dependency configuration requirement
//    @Mock
    @MockBean
    //using MockBean instead of Mock so that using Autowired Annotations you can easily inject any dependency
    //by Mock Annotation you can Inject this Mock Dependency by use of InjectMock Annotation

   private ApplicationDao applicationDao;

    //Application service make use of mock or double by injecting them in a particular
    //this application service class so that it can easily give the expected result as desired

    //this will inject the mock dependency
//    @InjectMocks
    @Autowired
   private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach() {
        studentOne.setFirstname("Eric");
        studentOne.setLastname("Roby");
        studentOne.setEmailAddress("eric.roby@luv2code_school.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When & Verify")
    @Test
    public void assertEqualsTestAddGrades() {

        //Applying mocking on Application dao
        //or set the expectations on dao
        when(applicationDao.addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults())).thenReturn(100.00);

        //Now execute and assert
        assertEquals(100, applicationService.addGradeResultsForSingleClass(
                studentOne.getStudentGrades().getMathGradeResults()));

        //verify how much times the the Mocking Method calls on assert and execution

        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

        //now i know that the Mocking method calls only for once at a tome of execution
        verify(applicationDao,times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

    }
    @DisplayName("Find Gpa")
    @Test
    public void assertEqualsTestFindGpa() {
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
                .thenReturn(88.31);
        assertEquals(88.31, applicationService.findGradePointAverage(studentOne
                .getStudentGrades().getMathGradeResults()));
    }


    @DisplayName("Not Null")
    @Test
    public void testAssertNotNull() {
        when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
                .thenReturn(true);
        assertNotNull(applicationService.checkNull(studentOne.getStudentGrades()
                .getMathGradeResults()), "Object should not be null");
    }
    @DisplayName("Throw runtime error")
    @Test
    public void throwRuntimeError() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });

        verify(applicationDao, times(1)).checkNull(nullStudent);
    }

    @DisplayName("Multiple Stubbing")
    @Test
    public void stubbingConsecutiveCalls() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not throw exception second time");

        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });

        assertEquals("Do not throw exception second time",
                applicationService.checkNull(nullStudent));

        verify(applicationDao, times(2)).checkNull(nullStudent);
    }

}
