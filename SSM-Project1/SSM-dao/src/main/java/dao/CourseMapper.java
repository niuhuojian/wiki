package dao;

import domain.Course;
import domain.CourseVo;
import domain.Teacher;

import java.util.List;

public interface CourseMapper {
    public List<Course> findCourseByCondition(CourseVo courseVo);
    //因为采用模糊查询时，可能会有多个结果
    //而查询时是根据请求的来的CourseVo作为条件进行查询

    public void saveCourse(Course course);

    public void saveTeacher(Teacher teacher);

    public CourseVo findCourseById(Integer id);
    //因为同时回显course和teacher的信息，所以直接使用CourseVo

    public void updateCourse(Course course);
    public void updateTeacher(Teacher teacher);

    public void updateCourseStatus(Course course);


}
