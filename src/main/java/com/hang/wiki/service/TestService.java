package com.hang.wiki.service;

import com.hang.wiki.domain.Test;
import com.hang.wiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;
    
    public List<Test> list(){
        return testMapper.list();
    }
}
