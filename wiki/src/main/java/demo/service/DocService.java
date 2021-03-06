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
        //要分清楚两者区别
        //Doc内只有该有的属性值，而没有content，所以需要再新增一个content对象存放内容
        Doc doc=CopyUtils.copy(docSaveReq,Doc.class);
        Content content=CopyUtils.copy(docSaveReq, Content.class);
        if(ObjectUtils.isEmpty(docSaveReq.getId())){
            //新增
            long l = snowFlake.nextId();
            doc.setId(l);
            //因为generator生成的sql语句将null值默认置给了对象，所以更改时不起作用
            //在插入时置0
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            //更新
            docMapper.updateByPrimaryKey(doc);
            //包含大字段的更新方法
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
        //这里查询已经是全部字段，包括大字段
        Content content = contentMapper.selectByPrimaryKey(id);
        //文档阅读数加1
        myDocMapper.increaseViewCount(id);
        //如果直接getContent，属性为空时报异常
        if(ObjectUtils.isEmpty(content)){
            return "";
        }
        return content.getContent();
    }

    public void vote(Long id){
        //使用远程IP+文档IP作为唯一key，来保证24小时内不能重复
        String ip= RequestContext.getRemoteAddr();
        //一天只能点赞该文档一次
        //3600*24
        if(redisUtil.validateRepeat("DOC_VOTE_"+id+"_"+ip,5000)){
            myDocMapper.increaseVoteCount(id);
        }else{
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        //推送消息
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String log_id = MDC.get("LOG_ID");
        wsService.sendInfo(docDb.getName()+"被点赞!",log_id);
        //参数1：主题topic
        //参数2：消息内容
//        rocketMQTemplate.convertAndSend("VOTE_TOPIC",docDb.getName()+"被点赞!");
    }




    public void updateEbookInfo(){
        myDocMapper.updateEbookInfo();
    }
}
