package com.hang.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.wiki.domain.Content;
import com.hang.wiki.domain.Doc;
import com.hang.wiki.domain.DocExample;
import com.hang.wiki.exception.BusinessException;
import com.hang.wiki.exception.BusinessExceptionCode;
import com.hang.wiki.mapper.ContentMapper;
import com.hang.wiki.mapper.DocMapper;
import com.hang.wiki.mapper.DocMapperCust;
import com.hang.wiki.req.DocQueryReq;
import com.hang.wiki.req.DocSaveReq;
import com.hang.wiki.resp.DocQueryResp;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.util.CopyUtil;
import com.hang.wiki.util.RedisUtil;
import com.hang.wiki.util.RequestContext;
import com.hang.wiki.util.SnowFlake;
import com.hang.wiki.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {
    @Autowired
    private DocMapper docMapper;

    @Autowired
    private DocMapperCust docMapperCust;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    WsService wsService;



    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    public List<DocQueryResp> all(Long ebookId){
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req){

        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        //if(!ObjectUtils.isEmpty(req.getName())) {
        //    criteria.andNameLike("%" + req.getName() + "%");
        //}
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> categories = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo = new PageInfo<>(categories);


        List<DocQueryResp> docList = CopyUtil.copyList(categories, DocQueryResp.class);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(docList);


        return pageResp;
    }

    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req,Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if(ObjectUtils.isEmpty(req.getId())){
            doc.setId(null);
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            //Auto_Generated id returned back to doc object
            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            docMapper.updateByPrimaryKey(doc);
            int res = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            //If there is a document that does not link to its content before.
            if(res == 0){
                contentMapper.insert(content);
            }
        }

    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<Long> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);

        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increaseViewCount(id);
        String res = content == null ? "" : ObjectUtils.isEmpty(content.getContent()) ? "" : content.getContent();
        return res;
    }

    public void vote(Long id) {
        String ip = RequestContext.getRemoteAddr();
        if(redisUtil.validateRepeat(id+ip,3600*24)){
            docMapperCust.increaseVoteCount(id);
            Doc Document = docMapper.selectByPrimaryKey(id);

            String logId = MDC.get("LOG_ID");
            if(Document!=null) wsService.sendInfo("【" + Document.getName() + "】 is upvoted!", logId);
        }else{
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
    }

    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }
}
