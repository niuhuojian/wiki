package service;

import com.github.pagehelper.PageInfo;
import domain.Resource;
import domain.ResourceVo;

import java.util.List;

public interface ResourceService {
    public PageInfo<Resource> findAllResourceByPage(ResourceVo resourceVo);
}
