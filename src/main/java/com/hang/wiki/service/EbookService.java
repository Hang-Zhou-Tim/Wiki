package com.hang.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.wiki.domain.Ebook;
import com.hang.wiki.domain.EbookExample;
import com.hang.wiki.mapper.EbookMapper;
import com.hang.wiki.req.EbookQueryReq;
import com.hang.wiki.req.EbookSaveReq;
import com.hang.wiki.resp.EbookQueryResp;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookService {
    @Autowired
    private EbookMapper EbookMapper;
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);
    public PageResp<EbookQueryResp> list(EbookQueryReq req){

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebooks = EbookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooks);


        List<EbookQueryResp> ebookList = CopyUtil.copyList(ebooks, EbookQueryResp.class);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookList);


        return pageResp;
    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            EbookMapper.insert(ebook);
        }else{
            EbookMapper.updateByPrimaryKey(ebook);
        }

    }
}
