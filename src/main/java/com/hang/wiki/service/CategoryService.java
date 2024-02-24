package com.hang.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.wiki.domain.Category;
import com.hang.wiki.domain.CategoryExample;
import com.hang.wiki.mapper.CategoryMapper;
import com.hang.wiki.req.CategoryQueryReq;
import com.hang.wiki.req.CategorySaveReq;
import com.hang.wiki.resp.CategoryQueryResp;
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
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    public List<CategoryQueryResp> all(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categories = categoryMapper.selectByExample(categoryExample);

        List<CategoryQueryResp> categoryList = CopyUtil.copyList(categories, CategoryQueryResp.class);

        return categoryList;
    }

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categorys);


        List<CategoryQueryResp> categoryList = CopyUtil.copyList(categorys, CategoryQueryResp.class);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(categoryList);


        return pageResp;
    }

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req,Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
