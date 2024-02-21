package com.hang.wiki.service;

import com.hang.wiki.domain.Demo;
import com.hang.wiki.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {
    @Autowired
    private DemoMapper DemoMapper;
    
    public List<Demo> list(){
        return DemoMapper.selectByExample(null);
    }
}
