package demo.controller;

import demo.req.EbookQueryReq;
import demo.req.EbookSaveReq;
import demo.resp.CommonResp;
import demo.resp.EbookQueryResp;
import demo.resp.PageResp;
import demo.service.EbookService;
import demo.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;



    @RequestMapping("/list")
    public CommonResp list(EbookQueryReq ebookReq){
        CommonResp<PageResp<EbookQueryResp>> resp=new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(ebookReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq ebookSaveReq){
        CommonResp commonResp=new CommonResp();
        ebookService.save(ebookSaveReq);
        return commonResp;
    }


}
