package dao;

import domain.Menu;

import java.util.List;

public interface MenuMapper {
    public List<Menu> findSubMenuListByPid(Integer pid);
    //查询所有父子菜单信息

    public List<Menu> findAllMenu();

    public Menu findMenuById(Integer id);
}
