package com.hang.wiki.controller;

import com.hang.wiki.req.EbookQueryReq;
import com.hang.wiki.req.EbookSaveReq;
import com.hang.wiki.resp.CommonResp;
import com.hang.wiki.resp.EbookQueryResp;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<PageResp<EbookQueryResp>> list(@Valid EbookQueryReq ebookQueryReq){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();

        PageResp<EbookQueryResp> list = ebookService.list(ebookQueryReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp<PageResp<EbookQueryResp>> save(@Valid @RequestBody EbookSaveReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<PageResp<EbookQueryResp>> delete(@PathVariable Long id){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
