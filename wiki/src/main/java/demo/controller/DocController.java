package demo.controller;

import demo.req.DocQueryReq;
import demo.req.DocSaveReq;
import demo.resp.DocQueryResp;
import demo.resp.CommonResp;
import demo.resp.PageResp;
import demo.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private DocService docService;



    @RequestMapping("/list")
    public CommonResp list(@Valid DocQueryReq docReq){
        CommonResp<PageResp<DocQueryResp>> resp=new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docReq);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId){
        CommonResp<List<DocQueryResp>> resp=new CommonResp<>();
        List<DocQueryResp> all = docService.all(ebookId);
        resp.setContent(all);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq docSaveReq){
        CommonResp commonResp=new CommonResp();
        docService.save(docSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr){
        CommonResp commonResp=new CommonResp();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return commonResp;
    }

    @RequestMapping("/listContent/{id}")
    public CommonResp listContent(@PathVariable Long id){
        CommonResp<String> resp=new CommonResp<>();
        String s = docService.listContent(id);
        resp.setContent(s);
        return resp;
    }


}
