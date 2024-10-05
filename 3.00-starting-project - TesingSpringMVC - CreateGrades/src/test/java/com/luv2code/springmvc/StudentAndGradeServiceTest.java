package com.luv2code.springmvc;

import com.luv2code.springmvc.Dao.HistoryGradesDao;
import com.luv2code.springmvc.Dao.MathGradesDao;
import com.luv2code.springmvc.Dao.ScienceGradesDao;
import com.luv2code.springmvc.Dao.StudentDao;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//it is used to access the property defines in the application properties file
//where an H2 database defines of connecting with in Memory Database

@TestPropertySource("/application-test.properties")
@SpringBootTest
class StudentAndGradeServiceTest {


    @Autowired
    private JdbcTemplate jdbc;


    @Autowired
    MathGradesDao mathGradeDao;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private HistoryGradesDao historyGradeDao;


    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private StudentDao studentDao;
    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);

        jdbc.execute(sqlAddMathGrade);

        jdbc.execute(sqlAddScienceGrade);

        jdbc.execute(sqlAddHistoryGrade);
    }
    @Test
    public void createStudentService() {

        studentService.createStudent("Chad", "Darby",
                "chad.darby@luv2code_school.com");

        CollegeStudent student = studentDao.
                findByEmailAddress("chad.darby@luv2code_school.com");

        assertEquals("chad.darby@luv2code_school.com",
                student.getEmailAddress(), "find by email");
    }

    @Test
    public void isStudentNullCheck() {

        assertTrue(studentService.checkIfStudentIsNull(1));

        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentService(){
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
        Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(1);

        assertTrue(deletedCollegeStudent.isPresent(), "Return True");

        studentService.deleteStudent(1);

        deletedCollegeStudent = studentDao.findById(1);
        deletedMathGrade = mathGradeDao.findById(1);
        deletedScienceGrade = scienceGradeDao.findById(1);
        deletedHistoryGrade = historyGradeDao.findById(1);

        assertFalse(deletedCollegeStudent.isPresent(), "Return False");
        assertFalse(deletedMathGrade.isPresent());
        assertFalse(deletedScienceGrade.isPresent());
        assertFalse(deletedHistoryGrade.isPresent());



    }
    //sql annotation will load the data from the sql file before performing this test
    @Sql("/insertData.sql")
    @Test
    public void getGradebookService() {

        Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for (CollegeStudent collegeStudent : iterableCollegeStudents) {
            collegeStudents.add(collegeStudent);
        }

        assertEquals(5, collegeStudents.size());
    }

    @Test
    public void createGradeService() {
        // Create the grade
        assertTrue(studentService.createGrade(80.50, 1, "math"));
        assertTrue(studentService.createGrade(80.50, 1, "science"));
        assertTrue(studentService.createGrade(80.50, 1, "history"));

        // Get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);

        // Verify there is grades
        assertTrue(((Collection<MathGrade>) mathGrades).size() == 2,
                "Student has math grades");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2);
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2);
    }

    @Test
    public void deleteGradeService() {
        assertEquals(1, studentService.deleteGrade(1, "math"),
                "Returns student id after delete");

        assertEquals(1, studentService.deleteGrade(1, "science"),
                "Returns student id after delete");
        assertEquals(1, studentService.deleteGrade(1, "history"),
                "Returns student id after delete");
    }
    @Test
    public void createGradeServiceReturnFalse() {
        assertFalse(studentService.createGrade(105, 1, "math"));
        assertFalse(studentService.createGrade(-5, 1, "math"));
        assertFalse(studentService.createGrade(80.50, 2, "math"));
        assertFalse(studentService.createGrade(80.50, 1, "literature"));
    }
    @Test
    public void deleteGradeServiceReturnStudentIdOfZero() {
        assertEquals(0, studentService.deleteGrade(0, "science"),
                "No student should have 0 id");
        assertEquals(0, studentService.deleteGrade(1, "literature"),
                "No student should have a literature class");
    }
    @Test
    public void studentInformation() {

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);

        assertNotNull(gradebookCollegeStudent);
        assertEquals(1, gradebookCollegeStudent.getId());
        assertEquals("Eric", gradebookCollegeStudent.getFirstname());
        assertEquals("Roby", gradebookCollegeStudent.getLastname());
        assertEquals("eric.roby@luv2code_school.com", gradebookCollegeStudent.getEmailAddress());
        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
    }
    @Test
    public void studentInformationServiceReturnNull() {

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);

        assertNull(gradebookCollegeStudent);
    }
    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }
}
//
//    @Autowired
//    private StudentAndGradeService studentService;
//
//    @Autowired
//    private StudentDao studentDao;
//
//    @Test
//    public  void createStudentService(){
//
//        //we perform test driven development to making the service layer
//        //and dao layer
////first we test for service layer
//        studentService.createStudent("Hardik","Gupta","hg979084@gmail.com");
////then for serviceDao
//CollegeStudent student = studentDao.findByEmailAddress("hg979084@gmail.com");
////
//assertEquals("hg979084@gmail.com",student.getEmailAddress(),"EmailAddress Should Be Same");
//
//    }
//
//    //Integration Testing
//
//    //We Perform Integration Testing on Our Embedded Database
//    //first We Have to Know already what's in the database
//    //after apply any Test to the database
//    //then run test
//    //then clean up the database in AfterEach MMethod
//
//    //Autowired the Jdbc Template to perforem some QueryOperation to the database
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//    @BeforeEach
//    public void beforeEach(){
//    jdbcTemplate.execute("insert into student(id,firstname,lastname,email_address)"+
//            "values('1','Hardik','Gupta','hg979084@gmail.com')"
//            );
//    }
//
//    @Test
//    @DisplayName("Set Test TO the Database")
//    public void setTestToTheDataBase(){
//      assertTrue(studentService.checkIfStudentIsNull(1));
//      assertFalse(studentService.checkIfStudentIsNull(0));
//


//
//    @AfterEach
//    public void setAfterEach(){
//        jdbcTemplate.execute("DELETE FROM student");
//    }



