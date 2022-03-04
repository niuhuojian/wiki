package demo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import demo.domain.User;
import demo.domain.UserExample;
import demo.mapper.UserMapper;
import demo.req.UserQueryReq;
import demo.req.UserSaveReq;
import demo.resp.UserQueryResp;
import demo.resp.PageResp;
import demo.utils.CopyUtils;
import demo.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {
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
            //新增
            long l = snowFlake.nextId();
            user.setId(l);
            userMapper.insert(user);
        }else{
            //更新
            userMapper.updateByPrimaryKey(user);
        }
    }

    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }
}
