package service;

import domain.Course;
import domain.CourseVo;

import java.util.List;

public interface CourseService {
    public List<Course> findCourseByCondition(CourseVo courseVo);
    public void saveCourseOrTeacher(CourseVo courseVo);
    public CourseVo findCourseById(Integer id);

    public void updateCourseOrTeacher(CourseVo courseVo);

    public void updateCourseStatus(int id,int status);
}


