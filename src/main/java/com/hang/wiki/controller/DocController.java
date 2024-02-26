package com.hang.wiki.controller;

import com.hang.wiki.req.DocQueryReq;
import com.hang.wiki.req.DocSaveReq;
import com.hang.wiki.resp.CommonResp;
import com.hang.wiki.resp.DocQueryResp;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private DocService docService;

    @GetMapping("/list")
    public CommonResp<PageResp<DocQueryResp>> list(@Valid DocQueryReq docQueryReq){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();

        PageResp<DocQueryResp> list = docService.list(docQueryReq);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp<List<DocQueryResp>> all(@Valid DocQueryReq docQueryReq){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();

        List<DocQueryResp> all_doc = docService.all();
        resp.setContent(all_doc);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp<PageResp<DocQueryResp>> save(@Valid @RequestBody DocSaveReq req){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<PageResp<DocQueryResp>> delete(@PathVariable Long id){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        docService.delete(id);
        return resp;
    }
}
