package com.alexlowe.courses;

import com.alexlowe.courses.dao.Sql2oCourseDAO;
import com.alexlowe.courses.model.Course;
import com.alexlowe.testing.ApiClient;
import com.alexlowe.testing.ApiResponse;
import com.google.gson.Gson;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Keyes on 6/21/2016.
 */
public class ApiTest {

    public static final String PORT = "4567";
    public static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";

    private Connection conn;
    private ApiClient client;
    private Gson gson;
    private Sql2oCourseDAO courseDAO;

    @BeforeClass
    public static void startServer(){
        String[] args = {PORT, TEST_DATASOURCE};
        Api.main(args);
    }

    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    //these run before and after EACH test
    @Before
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + Api.RUNSTRING, "", "");
        conn = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
        courseDAO = new Sql2oCourseDAO(sql2o);
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    private Course newTestCourse() {
        return new Course("Test", "test.com");
    }

    @Test
    public void addingCoursesReturnsCreatedStatuses() throws Exception{
        Map<String, String> values = new HashMap<>();
        values.put("name", "Test");
        values.put("url", "test.com");

        ApiResponse res = client.request("POST", "/courses", gson.toJson(values));

        assertEquals(201, res.getStatus());
    }

    @Test
    public void coursesCanBeAccessedById() throws Exception {
        Course course = newTestCourse();
        courseDAO.add(course);

        ApiResponse res = client.request("GET", "/courses/" + course.getId());
        Course retrieved = gson.fromJson(res.getBody(), Course.class);

        assertEquals(course, retrieved);
    }

    @Test
    public void missingCoursesReturnNotFoundStatus() throws Exception {
        ApiResponse res = client.request("GET", "/courses/42");

        assertEquals(404, res.getStatus());
    }
}