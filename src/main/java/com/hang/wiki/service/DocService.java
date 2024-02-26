package com.hang.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.wiki.domain.Doc;
import com.hang.wiki.domain.DocExample;
import com.hang.wiki.mapper.DocMapper;
import com.hang.wiki.req.DocQueryReq;
import com.hang.wiki.req.DocSaveReq;
import com.hang.wiki.resp.DocQueryResp;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.util.CopyUtil;
import com.hang.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    public List<DocQueryResp> all(){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> categories = docMapper.selectByExample(docExample);

        List<DocQueryResp> docList = CopyUtil.copyList(categories, DocQueryResp.class);

        return docList;
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
        if(ObjectUtils.isEmpty(req.getId())){
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        }else{
            docMapper.updateByPrimaryKey(doc);
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
}