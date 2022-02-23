package controller;

import com.github.pagehelper.PageInfo;
import domain.ResponseResult;
import domain.Role;
import domain.User;
import domain.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/findAllUserByPage")
    public ResponseResult findAllUserByPage(@RequestBody UserVo userVo){
        PageInfo pageInfo = userService.findAllUserByPage(userVo);
        return new ResponseResult(true,200,"分页多条件查询成功",pageInfo);
    }

    @RequestMapping("/updateUserStatus")
    public ResponseResult updateUserStatus(Integer id,String status){
        if("ENABLE".equalsIgnoreCase(status)){
            status="DISABLE";
        }else{
            status="ENABLE";
        }
        //此处在于判断原本status状态后，进行取反以达到调整status的目的
        userService.updateUserStatus(id,status);

        return new ResponseResult(true,200,"修改用户状态成功",status);
    }

    @RequestMapping("/login")
    public ResponseResult login(User user, HttpServletRequest request) throws Exception {
        User login = userService.login(user);
        if(login!=null){
            //保护用户名id和access_token到session
            HttpSession session = request.getSession();
            String token= UUID.randomUUID().toString();
            session.setAttribute("access_token",token);
            session.setAttribute("user_id",login.getId());

            //将查询出来的信息响应给前台
            Map<String,Object> map=new HashMap<>();
            map.put("access_token",token);
            map.put("user_id",login.getId());

            return new ResponseResult(true,200,"登录成功",map);
        }else{
            return new ResponseResult(true,400,"用户名错误",null);
        }
    }

    @RequestMapping("/findUserRelationRoleById")
    public ResponseResult findUserRelationRoleById(Integer id){
        List<Role> userRelationRoleById = userService.findUserRelationRoleById(id);
        return new ResponseResult(true,200,"分配角色回显成功",userRelationRoleById);
    }

    @RequestMapping("/userContextRole")
    public ResponseResult userContextRole(@RequestBody UserVo userVo){
        if(userVo.getUserId()!=null){
            userService.userContextRole(userVo);
            return new ResponseResult(true,200,"分配角色成功",null);
        }else{
            return new ResponseResult(false,400,"失败",null);
        }
    }

    @RequestMapping("/getUserPermissions")
    public ResponseResult getUserPermissions(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        //1、获取请求头中的token

        HttpSession session = request.getSession();
        String access_token = (String) session.getAttribute("access_token");
        //2、获取session中的token
        //因为之前登录时token已经存储在session中，所以直接获取即可

        if(token.equals(access_token)){
            Integer user_id = (Integer) request.getSession().getAttribute("user_id");
            //获取用户id
            ResponseResult userPermissions = userService.getUserPermissions(user_id);
            return userPermissions;
        }else{
            return new ResponseResult(false,400,"获取菜单信息失败",null);
        }
    }
    //因为请求没有发送任何参数，也就没有参数传入

    @RequestMapping("/register")
    public ResponseResult register(String phone,String password) throws Exception {
        userService.register(phone,password);
        return new ResponseResult(true,200,"注册成功",null);
    }
}
