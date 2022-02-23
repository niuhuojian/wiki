package service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dao.ResourceMapper;
import domain.Resource;
import domain.ResourceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ResourceService;

import java.util.List;
@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public PageInfo<Resource> findAllResourceByPage(ResourceVo resourceVo) {
        PageHelper.startPage(resourceVo.getCurrentPage(),resourceVo.getPageSize());
        List<Resource> allResourceByPage = resourceMapper.findAllResourceByPage(resourceVo);
        PageInfo<Resource> pageInfo = new PageInfo<>(allResourceByPage);
        return pageInfo;
    }
}
