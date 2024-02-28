package com.hang.wiki.controller;

import com.hang.wiki.req.UserLoginReq;
import com.hang.wiki.req.UserQueryReq;
import com.hang.wiki.req.UserResetPasswordReq;
import com.hang.wiki.req.UserSaveReq;
import com.hang.wiki.resp.CommonResp;
import com.hang.wiki.resp.PageResp;
import com.hang.wiki.resp.UserLoginResp;
import com.hang.wiki.resp.UserQueryResp;
import com.hang.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public CommonResp<PageResp<UserQueryResp>> list(@Valid UserQueryReq userQueryReq){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();

        PageResp<UserQueryResp> list = userService.list(userQueryReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp<PageResp<UserQueryResp>> save(@Valid @RequestBody UserSaveReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @PostMapping("/resetPassword")
    public CommonResp<PageResp<UserQueryResp>> resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        userService.resetUserPassword(req);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp<UserLoginResp> resetPassword(@Valid @RequestBody UserLoginReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        resp.setContent(userLoginResp);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<PageResp<UserQueryResp>> delete(@PathVariable Long id){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }
}
