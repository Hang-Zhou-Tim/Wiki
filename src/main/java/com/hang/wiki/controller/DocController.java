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
import java.util.LinkedList;
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
    //Return all document details under the same ebook.
    @GetMapping("/all/{ebookId}")
    public CommonResp<List<DocQueryResp>> all(@Valid @PathVariable Long ebookId){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();

        List<DocQueryResp> all_doc = docService.all(ebookId);
        resp.setContent(all_doc);
        return resp;
    }

    @GetMapping("/findContent/{id}")
    public CommonResp<String> findContent(@PathVariable Long id){
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp<PageResp<DocQueryResp>> save(@Valid @RequestBody DocSaveReq req){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{ids}")
    public CommonResp<PageResp<DocQueryResp>> delete(@PathVariable String ids){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        String[] idArray = ids.split(",");
        List<Long> idList = new LinkedList<Long>();
        for(String id:idArray){
            idList.add(Long.valueOf(id));
        }
        docService.delete(idList);
        return resp;
    }
}
