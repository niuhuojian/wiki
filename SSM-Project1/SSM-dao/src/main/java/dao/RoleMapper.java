package dao;

import domain.Role;
import domain.Role_menu_relation;

import java.util.List;

public interface RoleMapper {
    public List<Role> findAllRole(Role role);

    public List<Integer> findMenuByRoleId(Integer roleId);
    //查询菜单只需要知道id即可
    //menu对象也行，可以得到更全面的信息

    public void deleteRoleContextMenu(Integer rid);

    public void roleContextMenu(Role_menu_relation roleMenuRelation);
    //向关联表中添加具体记录，所以直接传入一个封装好的对象

    public void deleteRole(Integer roleId);
}
