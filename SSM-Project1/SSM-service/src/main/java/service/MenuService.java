package service;

import domain.Menu;

import java.util.List;

public interface MenuService {
    public List<Menu> findSubMenuListByPid(Integer pid);
    public List<Menu> findAllMenu();
    public Menu findMenuById(Integer id);
}
