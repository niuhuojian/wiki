package dao;

import domain.*;

import java.util.List;

public interface UserMapper {
    public List<User> findAllUserByPage(UserVo userVo);

    public void updateUserStatus(User user);

    public User login(User user);

    public List<Role> findUserRelationRoleById(Integer id);

    public void deleteUserContextRole(Integer userId);

    public void userContextRole(User_Role_relation userRoleRelation);

    public List<Menu> findParentMenuByRoleId(List<Integer> ids);
    //根据角色id查询顶级菜单（父级菜单，parent_id=-1）
    //因为对应多个角色，所以直接传入多个id值
    public List<Menu> findSubMenuByPid(Integer pid);
    //根据PID，查询子菜单信息
    public List<Resource> findResourceByRoleId(List<Integer> ids);
    //获取用户拥有的资源权限信息
    //因为对应多个角色，所以也传入多个id值

    public void register(User user);
}
