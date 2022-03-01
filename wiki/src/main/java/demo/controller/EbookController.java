package demo.controller;

import demo.domain.Demo;
import demo.domain.Ebook;
import demo.req.EbookReq;
import demo.resp.CommonResp;
import demo.resp.EbookResp;
import demo.service.DemoService;
import demo.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;


    @RequestMapping("/list")
    public CommonResp list(EbookReq ebookReq){
        CommonResp<List<EbookResp>> resp=new CommonResp<>();
        List<EbookResp> list = ebookService.list(ebookReq);
        resp.setContent(list);
        return resp;
    }


}
