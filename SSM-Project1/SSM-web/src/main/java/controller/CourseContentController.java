package controller;

import domain.Course;
import domain.CourseLesson;
import domain.CourseSection;
import domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CourseContentService;

import java.util.*;

@RestController
@RequestMapping("/courseContent")
public class CourseContentController {
    @Autowired
    private CourseContentService courseContentService;
    @RequestMapping("/findSectionAndLessonByCourseId")
    public ResponseResult findSectionAndLessonByCourseId(Integer courseId){
        List<CourseSection> list = courseContentService.findSectionAndLessonByCourseId(courseId);
        ResponseResult responseResult = new ResponseResult(true, 200, "响应成功", list);
        return responseResult;
    }
    @RequestMapping("/findCourseByCourseId")
    public ResponseResult findCourseByCourseId(Integer courseId){
        Course course = courseContentService.findCourseByCourseId(courseId);
        ResponseResult responseResult = new ResponseResult(true, 200, "查询课程信息成功", course);
        return responseResult;
    }

    @RequestMapping("/saveOrUpdateSection")
    public ResponseResult saveOrUpdateSection(@RequestBody CourseSection courseSection){
        if(courseSection.getId()==null){
            courseContentService.saveSection(courseSection);
            return new ResponseResult(true,200,"新增章节成功",null);
        }else{
            courseContentService.updateSection(courseSection);
            return new ResponseResult(true,200,"修改章节成功",null);
        }
    }

    @RequestMapping("/updateSectionStatus")
    public ResponseResult updateSectionStatus(Integer id,Integer status){
        courseContentService.updateSectionStatus(id,status);
        Map<String,Object> map=new HashMap<>();
        map.put("status",status);
        ResponseResult responseResult = new ResponseResult(true, 200, "响应成功", map);
        return responseResult;
    }

    @RequestMapping("/saveLesson")
    public ResponseResult saveLesson(@RequestBody CourseLesson courseLesson){
        if(courseLesson.getId()==null){
            courseContentService.saveLesson(courseLesson);
            return new ResponseResult(true,200,"新增课时成功",null);
        }else{
            return new ResponseResult(true,200,"修改课时成功",null);
        }
    }
}
