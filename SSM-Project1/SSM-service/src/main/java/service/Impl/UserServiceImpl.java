package service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dao.UserMapper;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;
import utils.Md5;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo findAllUserByPage(UserVo userVo) {
        PageHelper.startPage(userVo.getCurrentPage(),userVo.getPageSize());
        List<User> allUserByPage = userMapper.findAllUserByPage(userVo);
        PageInfo<User> pageInfo = new PageInfo<>(allUserByPage);
        return pageInfo;
    }

    @Override
    public void updateUserStatus(Integer id, String status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        user.setUpdate_time(new Date());
        userMapper.updateUserStatus(user);
    }

    @Override
    public User login(User user) throws Exception {
        User login = userMapper.login(user);
        //该user对象包含了密文密码
        if(login!=null && Md5.verify(user.getPassword(),"lagou",login.getPassword())){
            return login;
        }else{
            return null;
        }
    }

    @Override
    public List<Role> findUserRelationRoleById(Integer id) {
        List<Role> userRelationRoleById = userMapper.findUserRelationRoleById(id);
        return userRelationRoleById;
    }

    @Override
    public void userContextRole(UserVo userVo) {
        //1、根据用户id清空中间表关联关系
        userMapper.deleteUserContextRole(userVo.getUserId());

        //2、再次建立关联关系
        List<Integer> roleIdList = userVo.getRoleIdList();
        for(Integer roleId:roleIdList){
            User_Role_relation user_role_relation = new User_Role_relation();
            user_role_relation.setUserId(userVo.getUserId());
            user_role_relation.setRoleId(roleId);
            user_role_relation.setCreatedTime(new Date());
            user_role_relation.setUpdatedTime(new Date());
            user_role_relation.setCreatedBy("system");
            user_role_relation.setUpdatedBy("system");

            userMapper.userContextRole(user_role_relation);
        }
    }

    @Override
    public ResponseResult getUserPermissions(Integer userid) {
        List<Role> roleList = userMapper.findUserRelationRoleById(userid);
        //1、获取用户对应的角色

        List<Integer> roleIdList=new ArrayList<>();
        for(Role role:roleList){
            roleIdList.add(role.getId());
        }
        //2、获取角色ID，保存在list中

        List<Menu> parentMenus = userMapper.findParentMenuByRoleId(roleIdList);
        //3、根据角色id查询父菜单

        for(Menu menu:parentMenus){
            List<Menu> subMenus = userMapper.findSubMenuByPid(menu.getId());
            //查询到关联的子级菜单
            menu.setSubMenuList(subMenus);
            //menu只是有父菜单自己的信息，子菜单的信息还没有被封装进去
        }
        //4、查询子菜单并封装进父菜单

        List<Resource> resourceList = userMapper.findResourceByRoleId(roleIdList);
        //5、获取资源信息

        Map<String,Object> map=new HashMap<>();
        map.put("menuList",parentMenus);
        map.put("resourceList",resourceList);
        return new ResponseResult(true,200,"获取用户权限信息成功",map);
    }

    @Override
    public void register(String phone, String password) throws Exception {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(Md5.md5(password,"lagou"));
        user.setName("hahaha");
        user.setStatus("ENABLE");
        user.setIs_del(0);
        user.setCreate_time(new Date());
        user.setUpdate_time(new Date());
        userMapper.register(user);
    }
}
