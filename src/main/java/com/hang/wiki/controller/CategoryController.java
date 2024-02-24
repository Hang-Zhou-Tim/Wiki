package com.hang.wiki.controller;

import com.hang.wiki.req.CategoryQueryReq;
import com.hang.wiki.req.CategorySaveReq;
import com.hang.wiki.resp.CategoryQueryResp;
import com.hang.wiki.resp.CommonResp;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp<PageResp<CategoryQueryResp>> list(@Valid CategoryQueryReq categoryQueryReq){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();

        PageResp<CategoryQueryResp> list = categoryService.list(categoryQueryReq);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp<List<CategoryQueryResp>> all(@Valid CategoryQueryReq categoryQueryReq){
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();

        List<CategoryQueryResp> all_category = categoryService.all();
        resp.setContent(all_category);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp<PageResp<CategoryQueryResp>> save(@Valid @RequestBody CategorySaveReq req){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<PageResp<CategoryQueryResp>> delete(@PathVariable Long id){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
