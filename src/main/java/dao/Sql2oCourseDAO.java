package dao;

import exc.DaoException;
import model.Course;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

/**
 * Created by Keyes on 6/20/2016.
 */
public class Sql2oCourseDAO implements CourseDAO{

    private final Sql2o sql2o;

    public Sql2oCourseDAO(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    public void add(Course course) throws DaoException {
        String sql = "INSERT INTO courses(name,url) VALUES (:name, :url)";
        try (Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(course) // takes the name params from pojo properties, so it takes the result of getName() getURL() and pushes them to :name :url
                    .executeUpdate()
                    .getKey();//b/c were doing insert we can get back key that was created

            course.setId(id);
        }catch (Sql2oException ex){
            throw new DaoException(ex, "Problem adding course");
        }
    }

    public List<Course> findAll() {
        try(Connection conn = sql2o.open()){
            return conn.createQuery("SELECT * FROM courses")
                    .executeAndFetch(Course.class);
        }
    }

    @Override
    public Course findById(int id) {
        try (Connection conn = sql2o.open()){
            return conn.createQuery("SELECT * FROM courses WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Course.class);
        }
    }
}
