package service.Impl;

import dao.CourseMapper;
import domain.Course;
import domain.CourseVo;
import domain.Teacher;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.CourseService;

import java.util.Date;
import java.util.List;
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> findCourseByCondition(CourseVo courseVo) {
        List<Course> list = courseMapper.findCourseByCondition(courseVo);
        return list;
    }

    @Override
    public void saveCourseOrTeacher(CourseVo courseVo) {
        Course course = new Course();
        //封装course对象
        BeanUtils.copyProperties(course,courseVo);
        //因为courseVo是接受请求传进的参数的，所以本身带有属性值
        //所以将属性值复制到新的course中，完成course的封装
        Date date = new Date();
        course.setCreateTime(date);
        course.setUpdateTime(date);
        //补全课程信息
        courseMapper.saveCourse(course);
        //保存课程
        int id = course.getId();
        //获取添加后的id值

        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacher,courseVo);
        //和course相同，courseVo内也有讲师信息，所以直接复制过去
        teacher.setCreateTime(date);
        teacher.setUpdateTime(date);
        teacher.setIsDel(0);
        teacher.setCourseId(id);
        courseMapper.saveTeacher(teacher);
    }

    @Override
    public CourseVo findCourseById(Integer id) {
        return courseMapper.findCourseById(id);
    }

    @Override
    public void updateCourseOrTeacher(CourseVo courseVo) {
        Course course = new Course();
        BeanUtils.copyProperties(course,courseVo);
        //封装课程信息

        Date date = new Date();
        course.setUpdateTime(date);
        courseMapper.updateCourse(course);

        int id = course.getId();

        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacher,courseVo);
        teacher.setCourseId(id);
        //补全讲师对应的courseId
        teacher.setUpdateTime(date);
        courseMapper.updateTeacher(teacher);
    }

    @Override
    public void updateCourseStatus(int id, int status) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(status);
        course.setUpdateTime(new Date());
        courseMapper.updateCourseStatus(course);
    }
}
