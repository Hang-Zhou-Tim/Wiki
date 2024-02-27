package com.hang.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.wiki.domain.User;
import com.hang.wiki.domain.UserExample;
import com.hang.wiki.exception.BusinessException;
import com.hang.wiki.exception.BusinessExceptionCode;
import com.hang.wiki.mapper.UserMapper;
import com.hang.wiki.req.UserQueryReq;
import com.hang.wiki.req.UserSaveReq;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.resp.UserQueryResp;
import com.hang.wiki.util.CopyUtil;
import com.hang.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    public PageResp<UserQueryResp> list(UserQueryReq req){

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andNameLike("%" + req.getLoginName() + "%");
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> users = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(users);


        List<UserQueryResp> userList = CopyUtil.copyList(users, UserQueryResp.class);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(userList);


        return pageResp;
    }

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req,User.class);
        if(ObjectUtils.isEmpty(req.getId())){
            if(ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))){
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else{
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else{
            //Business Logic specifies to not update login name.
            user.setLoginName(null);
            // Update
            userMapper.updateByPrimaryKeySelective(user);
        }

    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);

        List<User> userList = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }
        return userList.get(0);


    }
}
