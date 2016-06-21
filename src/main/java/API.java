import com.google.gson.Gson;
import dao.CourseDAO;
import dao.Sql2oCourseDAO;
import model.Course;
import org.sql2o.Sql2o;

import static spark.Spark.after;
import static spark.Spark.post;
import static spark.Spark.get;

/**
 * Created by Keyes on 6/20/2016.
 */
public class API {
    public static void main(String[] args) {
        Sql2o sql2o = new Sql2o("jdbc:h2:~/reviews.db;INIT=PUNSCRIPT from 'classpath:db/init.sql");
        CourseDAO courseDAO = new Sql2oCourseDAO(sql2o);
        Gson gson = new Gson();

        post("/courses", "application/json", (req, res) -> {
             Course course = gson.fromJson(req.body(), Course.class);
            //req.body returns json that they sent across, need to turn this json into obj --gson
            //gson doesn't use setters, it's actually using private fields
            courseDAO.add(course);
            res.status(201);
            return course;
        }, gson::toJson); //method reference can use anything that takes obj and returns string, run toJson and pass in "course"

        get("/courses", "application/json",
                 (req, res) -> courseDAO.findAll(),gson::toJson);

        get("/courses/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            //TODO: what if this is not found?
            Course course = courseDAO.findById(id);
            return course;
        }, gson::toJson);

        after((req, res) ->{  //will be perform after ever request/response
            res.type("application/json");
        });
    }
}
