package com.alexlowe.courses.dao;

import com.alexlowe.courses.exc.DaoException;
import com.alexlowe.courses.model.Course;

import java.util.List;

/**
 * Created by Keyes on 6/20/2016.
 */
public interface CourseDAO  {
    void add(Course course) throws DaoException;

    List<Course> findAll();

    Course findById(int id);
}
