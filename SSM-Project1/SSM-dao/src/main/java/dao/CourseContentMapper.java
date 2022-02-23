package dao;

import domain.Course;
import domain.CourseLesson;
import domain.CourseSection;

import java.util.List;

public interface CourseContentMapper {
    public List<CourseSection> findSectionAndLessonByCourseId(Integer courseId);

    public Course findCourseByCourseId(Integer courseId);

    public void saveSection(CourseSection courseSection);

    public void updateSection(CourseSection courseSection);

    public void updateSectionStatus(CourseSection courseSection);

    public void saveLesson(CourseLesson courseLesson);
}
