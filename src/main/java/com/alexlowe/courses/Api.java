package com.alexlowe.courses;

import com.alexlowe.courses.exc.ApiError;
import com.google.gson.Gson;
import com.alexlowe.courses.dao.CourseDAO;
import com.alexlowe.courses.dao.Sql2oCourseDAO;
import com.alexlowe.courses.model.Course;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by Keyes on 6/20/2016.
 */
public class Api {
    public static final java.lang.String RUNSTRING = ";INIT=RUNSCRIPT from 'classpath:db/init.sql'";
    public static void main(String[] args) {
        java.lang.String datasource = "jdbc:h2:~/reviews.db";
        if(args.length > 0){
            if(args.length != 2){
                System.out.println("java Api <port> <datasource>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            datasource = args[1];
        }

        Sql2o sql2o = new Sql2o(datasource + RUNSTRING, "", "");
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
            Course course = courseDAO.findById(id);
            if (course == null){
                throw new ApiError(404, "Could not find course with id " + id);
            }
            return course;
        }, gson::toJson);

        exception(ApiError.class, (exc, req, res) -> {
            ApiError err = (ApiError) exc;
            Map<java.lang.String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatus());
            jsonMap.put("msg", err.getMessage());
            res.type("application/json");
            res.status(err.getStatus());
            res.body(gson.toJson(jsonMap));
        });

        after((req, res) ->{  //will be perform after ever request/response
            res.type("application/json");
        });
    }
}
