package service;

import domain.Course;
import domain.CourseLesson;
import domain.CourseSection;

import java.util.List;

public interface CourseContentService {
    public List<CourseSection> findSectionAndLessonByCourseId(Integer courseId);

    public Course findCourseByCourseId(Integer courseId);

    public void saveSection(CourseSection courseSection);

    public void updateSection(CourseSection courseSection);

    public void updateSectionStatus(Integer id,Integer status);

    public void saveLesson(CourseLesson courseLesson);
}
