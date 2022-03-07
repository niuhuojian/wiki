package demo.controller;

import demo.resp.CommonResp;
import demo.resp.StatisticResp;
import demo.service.EbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/snapshot")
public class EbookSnapshotController {

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @RequestMapping("/getdata")
    public CommonResp getData(){
        List<StatisticResp> statisticRespList =ebookSnapshotService.getData();
        CommonResp<List<StatisticResp>> commonResp=new CommonResp<>();
        commonResp.setContent(statisticRespList);
        return commonResp;
    }
}
