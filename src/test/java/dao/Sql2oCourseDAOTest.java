package dao;

import model.Course;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

/**
 * Created by Keyes on 6/20/2016.
 */
public class Sql2oCourseDAOTest {

    private Sql2oCourseDAO dao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        //when it starts up it will run init script
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", ""); //last 2 params are username/pass
        dao = new Sql2oCourseDAO(sql2o);

        //keep connection open through entire test, so that it isnt wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCourseSetsId() throws Exception {
        Course course = newTestCourse();
        int originalCourseId = course.getId();

        dao.add(course); //this method adds course to db and should change the id, hence assertion below

        assertNotEquals(originalCourseId, course.getId());
    }

    @Test
    public void addedCoursesAreReturnedFromFindAll() throws Exception {
        Course course = newTestCourse();
        dao.add(course);

        assertEquals(1, dao.findAll().size());
    }

    @Test
    public void noCoursesReturnsEmptyList() throws Exception {
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void existingCoursesCanBeFoundById() throws Exception {
        Course course = newTestCourse();
        dao.add(course);

        Course foundCourse = dao.findById(course.getId());

        assertEquals(course, foundCourse);
    }

    private Course newTestCourse() {
        return new Course("Test", "test.com");
    }
}