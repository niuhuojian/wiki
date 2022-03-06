package demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static final Logger Log= LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate redisTemplate;

    //true：表示不存在，放一个key
    //false：表示存在
    public boolean validateRepeat(String key,long second){
        if(redisTemplate.hasKey(key)){
            Log.info("key已存在：{}",key);
            return false;
        }else{
            Log.info("key不存在，放入：{}，过期{}秒",key,second);
            redisTemplate.opsForValue().set(key,key,second, TimeUnit.SECONDS);
            return true;
        }
    }
}
