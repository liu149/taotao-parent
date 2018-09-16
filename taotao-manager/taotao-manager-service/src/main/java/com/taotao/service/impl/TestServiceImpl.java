package com.taotao.service.impl;

import com.taotao.mapper.TestMapper;
import com.taotao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author : liuqi
 * createTime : 2018-09-04
 * description : TODO
 * version : 1.0
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;
    @Override
    public String queryNow() {
        return testMapper.queryNow();
    }
}