package dao;

import exc.DaoException;
import model.Course;
import model.Review;

import java.util.List;

/**
 * Created by Keyes on 6/20/2016.
 */
public interface ReviewDAO {
    void add(Review review) throws DaoException;

    List<Review> findAll();

    List<Review> findReviewByCourseId(int courseId);
}
