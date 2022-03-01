package demo.service;

import demo.domain.Ebook;
import demo.domain.EbookExample;
import demo.mapper.EbookMapper;
import demo.req.EbookReq;
import demo.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class EbookService {
    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq ebookReq){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //通过ebookExample来设置模糊查询
        criteria.andNameLike("%"+ebookReq.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> ebookRespList=new ArrayList<>();
        for(Ebook ebook:ebookList){
            EbookResp ebookResp=new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            ebookRespList.add(ebookResp);
        }
        return ebookRespList;
    }
}
