package controller;

import com.github.pagehelper.PageInfo;
import domain.Resource;
import domain.ResourceVo;
import domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ResourceService;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping("/findAllResource")
    public ResponseResult findAllResourceByPage(@RequestBody ResourceVo resourceVo){
        PageInfo<Resource> allResourceByPage = resourceService.findAllResourceByPage(resourceVo);
        return new ResponseResult(true,200,"资源信息分页多条件查询成功",allResourceByPage);
    }
}
