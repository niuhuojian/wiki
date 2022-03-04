package demo.controller;

import demo.req.UserQueryReq;
import demo.req.UserResetPasswordReq;
import demo.req.UserSaveReq;
import demo.resp.CommonResp;
import demo.resp.UserQueryResp;
import demo.resp.PageResp;
import demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger Log= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;



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
}
