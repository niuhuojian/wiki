package service.Impl;

import dao.CourseContentMapper;
import domain.Course;
import domain.CourseLesson;
import domain.CourseSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.CourseContentService;

import java.util.Date;
import java.util.List;
@Service
public class CourseContentServiceImpl implements CourseContentService {
    @Autowired
    private CourseContentMapper courseContentMapper;
    @Override
    public List<CourseSection> findSectionAndLessonByCourseId(Integer courseId) {
        List<CourseSection> list = courseContentMapper.findSectionAndLessonByCourseId(courseId);
        return list;
    }

    @Override
    public Course findCourseByCourseId(Integer courseId) {
        Course course = courseContentMapper.findCourseByCourseId(courseId);
        return course;
    }

    @Override
    public void saveSection(CourseSection courseSection) {
        courseSection.setCreateTime(new Date());
        courseSection.setUpdateTime(new Date());
        //补全信息
        courseContentMapper.saveSection(courseSection);
    }

    @Override
    public void updateSection(CourseSection courseSection) {
        courseSection.setUpdateTime(new Date());
        courseContentMapper.updateSection(courseSection);
    }

    @Override
    public void updateSectionStatus(Integer id,Integer status) {
        CourseSection courseSection = new CourseSection();
        courseSection.setStatus(status);
        courseSection.setId(id);
        courseSection.setUpdateTime(new Date());
        courseContentMapper.updateSectionStatus(courseSection);
    }

    @Override
    public void saveLesson(CourseLesson courseLesson) {
        courseLesson.setCreateTime(new Date());
        courseLesson.setUpdateTime(new Date());
        courseContentMapper.saveLesson(courseLesson);
    }
}
