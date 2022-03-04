package demo.controller;

import demo.req.UserLoginReq;
import demo.req.UserQueryReq;
import demo.req.UserResetPasswordReq;
import demo.req.UserSaveReq;
import demo.resp.CommonResp;
import demo.resp.UserLoginResp;
import demo.resp.UserQueryResp;
import demo.resp.PageResp;
import demo.service.UserService;
import demo.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger Log= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SnowFlake snowFlake;


    @RequestMapping("/list")
    public CommonResp list(@Valid UserQueryReq userReq){
        CommonResp<PageResp<UserQueryResp>> resp=new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(userReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq userSaveReq){
        //密码通过md5加密
        userSaveReq.setPassword(DigestUtils.md5DigestAsHex(userSaveReq.getPassword().getBytes()));
        CommonResp commonResp=new CommonResp();
        userService.save(userSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp commonResp=new CommonResp();
        userService.delete(id);
        return commonResp;
    }

    @PostMapping("/resetpassword")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq userResetPasswordReq){
        //密码通过md5加密
        userResetPasswordReq.setPassword(DigestUtils.md5DigestAsHex(userResetPasswordReq.getPassword().getBytes()));
        CommonResp commonResp=new CommonResp();
        userService.resetPassword(userResetPasswordReq);
        return commonResp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq userLoginReq){
        //密码通过md5加密
        userLoginReq.setPassword(DigestUtils.md5DigestAsHex(userLoginReq.getPassword().getBytes()));
        CommonResp<UserLoginResp> commonResp=new CommonResp();
        UserLoginResp userLoginResp=userService.Login(userLoginReq);

        //生成单点登录的token，放入redis中

        Long token = snowFlake.nextId();
        Log.info("生成单点登录的token:{}，放入redis中",token);
        userLoginResp.setToken(token.toString());
        //以token作为key，以userLoginResp作为value
        redisTemplate.opsForValue().set(token,userLoginResp,3600*24, TimeUnit.SECONDS);
        commonResp.setContent(userLoginResp);
        return commonResp;
    }
}
