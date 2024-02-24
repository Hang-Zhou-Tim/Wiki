package com.hang.wiki.controller;

import com.hang.wiki.req.EbookReq;
import com.hang.wiki.resp.CommonResp;
import com.hang.wiki.resp.EbookResp;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<PageResp<EbookResp>> list(EbookReq ebookReq){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();

        PageResp<EbookResp> list = ebookService.list(ebookReq);
        resp.setContent(list);
        return resp;
    }
}
