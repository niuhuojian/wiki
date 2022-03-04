package demo.controller;

import demo.req.UserQueryReq;
import demo.req.UserSaveReq;
import demo.resp.CommonResp;
import demo.resp.UserQueryResp;
import demo.resp.PageResp;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
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


}
