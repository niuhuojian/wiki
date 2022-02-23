package controller;

import domain.Menu;
import domain.ResponseResult;
import domain.Role;
import domain.RoleMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.MenuService;
import service.RoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @RequestMapping("/findAllRole")
    public ResponseResult findAllRole(@RequestBody Role role){
        List<Role> allRole = roleService.findAllRole(role);
        return new ResponseResult(true,200,"查询所有角色成功",allRole);
    }

    @Autowired
    private MenuService menuService;

    @RequestMapping("/findAllMenu")
    public ResponseResult findSubMenuListByPid(){
        List<Menu> subMenuListByPid = menuService.findSubMenuListByPid(-1);
        //-1表示查询所有的父级菜单
        //因为嵌套查询，所以查询父级菜单时也会查询子级菜单

        Map<String, Object> map = new HashMap<>();
        map.put("parantMenuList",subMenuListByPid);
        return new ResponseResult(true,200,"查询所有的父子信息成功",map);
    }

    @RequestMapping("/findMenuByRoleId")
    public ResponseResult findMenuByRoleId(Integer roleId){
        List<Integer> menuByRoleId = roleService.findMenuByRoleId(roleId);
        return new ResponseResult(true,200,"查询角色关联的菜单信息成功",menuByRoleId);
    }
    @RequestMapping("/roleContextMenu")
    public ResponseResult roleContextMenu(@RequestBody RoleMenuVo roleMenuVo){
        roleService.roleContextMenu(roleMenuVo);
        return new ResponseResult(true,200,"响应成功",null);
    }
    @RequestMapping("/deleteRole")
    public ResponseResult deleteRole(Integer roleId){
        roleService.deleteRole(roleId);
        return new ResponseResult(true,200,"删除角色成功","");
    }
}
