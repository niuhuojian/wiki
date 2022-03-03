package demo.controller;

import demo.req.CategoryQueryReq;
import demo.req.CategorySaveReq;
import demo.resp.CommonResp;
import demo.resp.CategoryQueryResp;
import demo.resp.PageResp;
import demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategotyController {
    @Autowired
    private CategoryService categoryService;



    @RequestMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq categoryReq){
        CommonResp<PageResp<CategoryQueryResp>> resp=new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryReq);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping("/all")
    public CommonResp all(){
        CommonResp<List<CategoryQueryResp>> resp=new CommonResp<>();
        List<CategoryQueryResp> all = categoryService.all();
        resp.setContent(all);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq categorySaveReq){
        CommonResp commonResp=new CommonResp();
        categoryService.save(categorySaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp commonResp=new CommonResp();
        categoryService.delete(id);
        return commonResp;
    }


}
