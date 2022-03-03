package demo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import demo.domain.Doc;
import demo.domain.DocExample;
import demo.mapper.DocMapper;
import demo.req.DocQueryReq;
import demo.req.DocSaveReq;
import demo.resp.DocQueryResp;
import demo.resp.PageResp;
import demo.utils.CopyUtils;
import demo.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {
    @Autowired
    private DocMapper docMapper;
    @Autowired
    private SnowFlake snowFlake;

    public PageResp<DocQueryResp> list(DocQueryReq docReq){

        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        PageHelper.startPage(docReq.getPage(),docReq.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo=new PageInfo<>(docList);

        PageResp pageResp=new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(docList);
        List<DocQueryResp> docQueryRespList = CopyUtils.copyList(docList, DocQueryResp.class);
        return pageResp;
    }

    public void save(DocSaveReq docSaveReq){
        Doc doc=CopyUtils.copy(docSaveReq,Doc.class);
        if(ObjectUtils.isEmpty(docSaveReq.getId())){
            //新增
            long l = snowFlake.nextId();
            doc.setId(l);
            docMapper.insert(doc);
        }else{
            //更新
            docMapper.updateByPrimaryKey(doc);
        }
    }

    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

    public List<DocQueryResp> all(){

        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");

        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> docQueryRespList = CopyUtils.copyList(docList, DocQueryResp.class);
        return docQueryRespList;
    }
}
