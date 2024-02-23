package com.hang.wiki.service;

import com.hang.wiki.domain.Ebook;
import com.hang.wiki.domain.EbookExample;
import com.hang.wiki.mapper.EbookMapper;
import com.hang.wiki.resp.EbookResp;
import com.hang.wiki.util.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookService {
    @Autowired
    private EbookMapper EbookMapper;
    
    public List<EbookResp> list(String name){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        List<Ebook> ebooks = EbookMapper.selectByExample(ebookExample);

        List<EbookResp> respList = CopyUtil.copyList(ebooks, EbookResp.class);
        return respList;
    }
}
