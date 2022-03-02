package demo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import demo.domain.Ebook;
import demo.domain.EbookExample;
import demo.mapper.EbookMapper;
import demo.req.EbookReq;
import demo.resp.EbookResp;
import demo.utils.CopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class EbookService {
    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq ebookReq){

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(ebookReq.getName())){
            //表示如果请求中的name不为空再启动模糊查询
            //通过ebookExample来设置模糊查询
            criteria.andNameLike("%"+ebookReq.getName()+"%");
        }
        PageHelper.startPage(1,3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo=new PageInfo<>(ebookList);
        pageInfo.getTotal();
        pageInfo.getPages();
        List<EbookResp> ebookRespList = CopyUtils.copyList(ebookList, EbookResp.class);
        return ebookRespList;
    }
}
