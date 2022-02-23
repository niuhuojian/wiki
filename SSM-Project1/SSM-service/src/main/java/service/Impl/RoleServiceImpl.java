package service.Impl;

import dao.RoleMapper;
import domain.Role;
import domain.RoleMenuVo;
import domain.Role_menu_relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.RoleService;

import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findAllRole(Role role) {
        List<Role> allRole = roleMapper.findAllRole(role);
        return allRole;
    }

    @Override
    public List<Integer> findMenuByRoleId(Integer roleId) {
        List<Integer> menuByRoleId = roleMapper.findMenuByRoleId(roleId);
        return menuByRoleId;
    }

    @Override
    public void roleContextMenu(RoleMenuVo roleMenuVo) {
        roleMapper.deleteRoleContextMenu(roleMenuVo.getRoleId());
        //清空中间表的关联关系
        List<Integer> menuIdList = roleMenuVo.getMenuIdList();
        for(Integer menuId:menuIdList){
            Role_menu_relation role_menu_relation = new Role_menu_relation();
            role_menu_relation.setMenuId(menuId);
            role_menu_relation.setRoleId(roleMenuVo.getRoleId());
            role_menu_relation.setCreatedTime(new Date());
            role_menu_relation.setUpdatedTime(new Date());
            role_menu_relation.setCreatedBy("system");
            role_menu_relation.setUpdatedBy("system");
            roleMapper.roleContextMenu(role_menu_relation);
        }
        //遍历逐一添加进关联表
    }

    @Override
    public void deleteRole(Integer roleId) {
        roleMapper.deleteRoleContextMenu(roleId);
        //先清空角色关联的中间表关联关系
        roleMapper.deleteRole(roleId);
    }
}
