package demo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import demo.domain.Content;
import demo.domain.Doc;
import demo.domain.DocExample;
import demo.exception.BusinessException;
import demo.exception.BusinessExceptionCode;
import demo.mapper.ContentMapper;
import demo.mapper.DocMapper;
import demo.mapper.MyDocMapper;
import demo.req.DocQueryReq;
import demo.req.DocSaveReq;
import demo.resp.DocQueryResp;
import demo.resp.PageResp;
import demo.utils.CopyUtils;
import demo.utils.RedisUtil;
import demo.utils.RequestContext;
import demo.utils.SnowFlake;
import demo.websocket.WebSocketServer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {
    private static final Logger Log= LoggerFactory.getLogger(DocService.class);

    @Autowired
    private DocMapper docMapper;
    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private MyDocMapper myDocMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WsService wsService;

//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;

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

    @Transactional
    public void save(DocSaveReq docSaveReq){
        //????????????????????????
        //Doc???????????????????????????????????????content??????????????????????????????content??????????????????
        Doc doc=CopyUtils.copy(docSaveReq,Doc.class);
        Content content=CopyUtils.copy(docSaveReq, Content.class);
        if(ObjectUtils.isEmpty(docSaveReq.getId())){
            //??????
            long l = snowFlake.nextId();
            doc.setId(l);
            //??????generator?????????sql?????????null??????????????????????????????????????????????????????
            //???????????????0
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            //??????
            docMapper.updateByPrimaryKey(doc);
            //??????????????????????????????
            int i = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(i == 0){
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

    public List<DocQueryResp> all(Long ebookId){

        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");

        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> docQueryRespList = CopyUtils.copyList(docList, DocQueryResp.class);
        return docQueryRespList;
    }

    public void delete(List<String> ids){
        DocExample docExample=new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String listContent(Long id){
        //???????????????????????????????????????????????????
        Content content = contentMapper.selectByPrimaryKey(id);
        //??????????????????1
        myDocMapper.increaseViewCount(id);
        //????????????getContent???????????????????????????
        if(ObjectUtils.isEmpty(content)){
            return "";
        }
        return content.getContent();
    }

    public void vote(Long id){
        //????????????IP+??????IP????????????key????????????24?????????????????????
        String ip= RequestContext.getRemoteAddr();
        //?????????????????????????????????
        //3600*24
        if(redisUtil.validateRepeat("DOC_VOTE_"+id+"_"+ip,5000)){
            myDocMapper.increaseVoteCount(id);
        }else{
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        //????????????
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String log_id = MDC.get("LOG_ID");
        wsService.sendInfo(docDb.getName()+"?????????!",log_id);
        //??????1?????????topic
        //??????2???????????????
//        rocketMQTemplate.convertAndSend("VOTE_TOPIC",docDb.getName()+"?????????!");
    }




    public void updateEbookInfo(){
        myDocMapper.updateEbookInfo();
    }
}
