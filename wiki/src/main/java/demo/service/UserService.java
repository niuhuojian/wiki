package demo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import demo.domain.User;
import demo.domain.UserExample;
import demo.exception.BusinessException;
import demo.exception.BusinessExceptionCode;
import demo.mapper.UserMapper;
import demo.req.UserLoginReq;
import demo.req.UserQueryReq;
import demo.req.UserResetPasswordReq;
import demo.req.UserSaveReq;
import demo.resp.UserLoginResp;
import demo.resp.UserQueryResp;
import demo.resp.PageResp;
import demo.utils.CopyUtils;
import demo.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {
    private static final Logger Log= LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq userReq){

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();

        if(!ObjectUtils.isEmpty(userReq.getLoginName())){
            //表示如果请求中的name不为空再启动模糊查询
            //通过userExample来设置模糊查询
            criteria.andLoginNameEqualTo(userReq.getLoginName());
        }
        PageHelper.startPage(userReq.getPage(),userReq.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo=new PageInfo<>(userList);

        PageResp pageResp=new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(userList);
        List<UserQueryResp> userQueryRespList = CopyUtils.copyList(userList, UserQueryResp.class);
        return pageResp;
    }

    public void save(UserSaveReq userSaveReq){
        User user=CopyUtils.copy(userSaveReq,User.class);
        if(ObjectUtils.isEmpty(userSaveReq.getId())){
            if(ObjectUtils.isEmpty(selectByLoginName(userSaveReq.getLoginName()))){
                //新增
                long l = snowFlake.nextId();
                user.setId(l);
                userMapper.insert(user);
            }else{
                //用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else{

            //更新
            //因为前端代码是可以根据浏览器进行绕过检验的
            // 为了进一步保证编辑时用户名安全不被修改，直接设为null，保证更新不会更新到用户名
            user.setLoginName(null);
            //同样的密码编辑时也不能修改
            user.setPassword(null);
            //updateByPrimaryKeySelective表示user有值才更新，没值就不更新
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String LoginName){
        UserExample userExample=new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> users = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(users)){
            return null;
        }else{
            User user = users.get(0);
            return user;
        }
    }

    public void resetPassword(UserResetPasswordReq userResetPasswordReq){
        User user=CopyUtils.copy(userResetPasswordReq,User.class);
        //因为参数只有id和password，id在前端被隐藏无法更改，就只能更改password
        userMapper.updateByPrimaryKeySelective(user);
    }

    public UserLoginResp Login(UserLoginReq userLoginReq){
        User userdb = selectByLoginName(userLoginReq.getLoginName());
        if(ObjectUtils.isEmpty(userdb)){
            Log.info("用户名不存在,{}",userLoginReq.getLoginName());
            //用户名不存在
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else{
          if(userdb.getPassword().equals(userLoginReq.getPassword())){
              //登录成功
              UserLoginResp loginResp = CopyUtils.copy(userdb, UserLoginResp.class);
              return loginResp;
          }else{
              //密码错误
              Log.info("密码错误，输入密码：{}，数据库密码：{}",userLoginReq.getPassword(),userdb.getPassword());
              throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
          }
        }
    }
}
