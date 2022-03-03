package demo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import demo.domain.Category;
import demo.domain.CategoryExample;
import demo.mapper.CategoryMapper;
import demo.req.CategoryQueryReq;
import demo.req.CategorySaveReq;
import demo.resp.CategoryQueryResp;
import demo.resp.PageResp;
import demo.utils.CopyUtils;
import demo.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryReq){

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        PageHelper.startPage(categoryReq.getPage(),categoryReq.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo=new PageInfo<>(categoryList);

        PageResp pageResp=new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(categoryList);
        List<CategoryQueryResp> categoryQueryRespList = CopyUtils.copyList(categoryList, CategoryQueryResp.class);
        return pageResp;
    }

    public void save(CategorySaveReq categorySaveReq){
        Category category=CopyUtils.copy(categorySaveReq,Category.class);
        if(ObjectUtils.isEmpty(categorySaveReq.getId())){
            //新增
            long l = snowFlake.nextId();
            category.setId(l);
            categoryMapper.insert(category);
        }else{
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }

    public List<CategoryQueryResp> all(){

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");

        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryQueryResp> categoryQueryRespList = CopyUtils.copyList(categoryList, CategoryQueryResp.class);
        return categoryQueryRespList;
    }
}
