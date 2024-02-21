package com.hang.wiki.controller;

import com.hang.wiki.req.EbookReq;
import com.hang.wiki.resp.CommonResp;
import com.hang.wiki.resp.EbookResp;
import com.hang.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<List<EbookResp>> list(EbookReq ebookReq){
        CommonResp<List<EbookResp>> resp = new CommonResp<>();

        List<EbookResp> list = ebookService.list(ebookReq.getName());
        resp.setContent(list);
        return resp;
    }
}
