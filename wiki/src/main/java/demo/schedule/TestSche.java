//package demo.schedule;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Component
//public class TestSche {
//
//    private static final Logger Log= LoggerFactory.getLogger(TestSche.class);
//
//    //固定时间间隔定时器,fixedRate单位毫秒
//    @Scheduled(fixedRate = 2000)
//    public void simple() throws InterruptedException{
//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
//        String dateString = simpleDateFormat.format(new Date());
////        Thread.sleep(2000);
//        Log.info("每隔5秒执行一次：{}",dateString);
//    }
//
//
//    //自定义cron表达式
//    //定时器是单线程，只有等上一次执行完成，下一次才会在下个时间点执行，错过就错过
//    @Scheduled(cron = "*/3 * * * * ?")
//    public void cron() throws InterruptedException{
//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ssSSS");
//        String dataString=simpleDateFormat.format(new Date());
////        Thread.sleep(1500);
//        Log.info("每隔3秒执行一次:{}",dataString);
//    }
//}
