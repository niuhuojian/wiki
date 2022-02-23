package controller;

import domain.Menu;
import domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.MenuService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @RequestMapping("/findAllMenu")
    public ResponseResult findAllMenu(){
        List<Menu> allMenu = menuService.findAllMenu();
        return new ResponseResult(true,200,"查询所有信息成功",allMenu);
    }

    @RequestMapping("/findMenuInfoById")
    public ResponseResult findMenuInfoById(Integer id){
        if(id==-1){
            //添加操作，回显中就不需要查询menu信息
            List<Menu> parentMenuList = menuService.findSubMenuListByPid(-1);
            //表中设置了parent_id为-1时，都为父级菜单

            Map<String,Object> map=new HashMap<>();
            map.put("menuInfo",null);
            //新增操作，还没有菜单信息
            map.put("parentMenuList",parentMenuList);
            return new ResponseResult(true,200,"添加回显成功",map);
        }else{
            //修改操作，回显所有的menu信息（当前menu和父级menu）
            Menu menu = menuService.findMenuById(id);

            List<Menu> parentMenuList = menuService.findSubMenuListByPid(-1);
            Map<String,Object> map=new HashMap<>();
            map.put("menuInfo",menu);
            //修改操作，有菜单信息
            map.put("parentMenuList",parentMenuList);
            return new ResponseResult(true,200,"修改回显成功",map);
        }
    }
}
