package com.example.service.user.controller;

import com.example.service.user.model.req.ImportUserReq;
import com.example.service.user.service.ImUserService;
import com.yzhou.im.common.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yzhou
 * @date 2023/2/13
 */
@RestController
@RequestMapping("v1/user")
public class ImUserController {

    @Autowired
    ImUserService imUserService;

    @RequestMapping("importUser")
    public ResponseVO importUser(@RequestBody ImportUserReq req, Integer appId) {
        req.setAppId(appId);
        return imUserService.importUser(req);
    }
}
