package demo.schedule;

import demo.mapper.DocMapper;
import demo.service.DocService;
import demo.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

    @Autowired
    private SnowFlake snowFlake;

    //每30秒更新电子书信息
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() throws InterruptedException{
        //增加日志流水号
        MDC.put("LOG_ID",String.valueOf(snowFlake.nextId()));
        Log.info("更新电子书信息开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        Log.info("更新电子书信息结束，耗时：{}",System.currentTimeMillis()-start);
    }
}
