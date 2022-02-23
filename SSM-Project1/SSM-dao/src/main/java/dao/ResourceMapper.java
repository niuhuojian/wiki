package dao;

import domain.Resource;
import domain.ResourceVo;

import java.util.List;

public interface ResourceMapper {
    public List<Resource> findAllResourceByPage(ResourceVo resourceVo);
}
