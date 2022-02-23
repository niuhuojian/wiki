package service;

import domain.Role;
import domain.RoleMenuVo;

import java.util.List;

public interface RoleService {
    public List<Role> findAllRole(Role role);

    public List<Integer> findMenuByRoleId(Integer roleId);

    public void roleContextMenu(RoleMenuVo roleMenuVo);

    public void deleteRole(Integer roleId);
}
