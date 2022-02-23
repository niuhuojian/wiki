package controller;

import domain.ResourceCategory;
import domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ResourceCategoryService;

import java.util.List;

@RestController
@RequestMapping("/resourceCategory")
public class ResourceCategoryController {
    @Autowired
    private ResourceCategoryService resourceCategoryService;
    @RequestMapping("/findAllResourceCategory")
    public ResponseResult findAllResourceCategory(){
        List<ResourceCategory> allResourceCategory = resourceCategoryService.findAllResourceCategory();
        return new ResponseResult(true,200,"查询资源分类成功",allResourceCategory);
    }
}
