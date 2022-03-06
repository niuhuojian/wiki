package demo.schedule;

import demo.mapper.DocMapper;
import demo.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DocSche {

    private static final Logger Log= LoggerFactory.getLogger(DocSche.class);

    @Autowired
    private DocService docService;

    //每30秒更新电子书信息
    @Scheduled(cron = "1/5 * * * * ?")
    public void cron() throws InterruptedException{
        docService.updateEbookInfo();
    }
}
