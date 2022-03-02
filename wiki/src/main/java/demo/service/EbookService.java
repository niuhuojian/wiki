package demo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import demo.domain.Ebook;
import demo.domain.EbookExample;
import demo.mapper.EbookMapper;
import demo.req.EbookQueryReq;
import demo.req.EbookSaveReq;
import demo.resp.EbookQueryResp;
import demo.resp.PageResp;
import demo.utils.CopyUtils;
import demo.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
@Service
public class EbookService {
    @Autowired
    private EbookMapper ebookMapper;
    @Autowired
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq ebookReq){

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(ebookReq.getName())){
            //表示如果请求中的name不为空再启动模糊查询
            //通过ebookExample来设置模糊查询
            criteria.andNameLike("%"+ebookReq.getName()+"%");
        }
        PageHelper.startPage(ebookReq.getPage(),ebookReq.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo=new PageInfo<>(ebookList);

        PageResp pageResp=new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookList);
        List<EbookQueryResp> ebookQueryRespList = CopyUtils.copyList(ebookList, EbookQueryResp.class);
        return pageResp;
    }

    public void save(EbookSaveReq ebookSaveReq){
        Ebook ebook=CopyUtils.copy(ebookSaveReq,Ebook.class);
        if(ObjectUtils.isEmpty(ebookSaveReq.getId())){
            //新增
            long id = snowFlake.nextId();
            ebook.setId(id);
            ebookMapper.insert(ebook);
        }else{
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }
}
