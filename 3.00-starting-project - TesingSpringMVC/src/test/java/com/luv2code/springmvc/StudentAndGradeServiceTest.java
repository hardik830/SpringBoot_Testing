package com.luv2code.springmvc;

import com.luv2code.springmvc.Dao.StudentDao;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//it is used to access the property defines in the application properties file
//where an H2 database defines of connecting with in Memory Database

@TestPropertySource("/application.properties")
@SpringBootTest
class StudentAndGradeServiceTest {


    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
                "values (1, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')");
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
        //retrieve student
        Optional<CollegeStudent> student = studentDao.findById(1);

        //checking the getting student is true or not
        assertTrue(student.isPresent(),"Return True");

        //Now deleting the student from a database
        studentService.deleteStudent(1);
        Optional<CollegeStudent> deletedStudent = studentDao.findById(1);
        assertFalse(deletedStudent.isPresent(),"Return False");



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

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM student");
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



