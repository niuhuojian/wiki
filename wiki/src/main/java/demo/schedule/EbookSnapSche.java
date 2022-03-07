package demo.schedule;

import demo.mapper.MyEbookSnapshotMapper;
import demo.service.DocService;
import demo.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookSnapSche {

    private static final Logger Log= LoggerFactory.getLogger(EbookSnapSche.class);

    @Autowired
    private MyEbookSnapshotMapper mapper;

    @Autowired
    private SnowFlake snowFlake;

    //每30秒更新电子书信息
    @Scheduled(cron = "0/5 * * * * ?")
    public void cron() throws InterruptedException{
        MDC.put("LOG_ID",String.valueOf(snowFlake.nextId()));
        Log.info("生成今日电子书快照开始");
        long l = System.currentTimeMillis();
        mapper.genSnapshot();
        Log.info("生成今日电子书快照结束，耗时{}秒",System.currentTimeMillis()-l);
    }
}
