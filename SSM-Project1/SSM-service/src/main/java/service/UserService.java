package service;

import com.github.pagehelper.PageInfo;
import domain.ResponseResult;
import domain.Role;
import domain.User;
import domain.UserVo;

import java.util.List;

public interface UserService {
    public PageInfo findAllUserByPage(UserVo userVo);

    public void updateUserStatus(Integer id,String status);

    public User login(User user) throws Exception;

    public List<Role> findUserRelationRoleById(Integer id);

    public void userContextRole(UserVo userVo);

    public ResponseResult getUserPermissions(Integer userid);
    //获取用户权限，进行菜单动态展示

    public void register(String phone,String password) throws Exception;
}
