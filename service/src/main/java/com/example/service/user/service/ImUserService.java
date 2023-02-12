package com.example.service.user.service;

import com.example.service.user.dao.ImUserDataEntity;
import com.example.service.user.model.req.*;
import com.example.service.user.model.resp.GetUserInfoResp;
import com.yzhou.im.common.ResponseVO;

public interface ImUserService {

    public ResponseVO importUser(ImportUserReq req);

    public ResponseVO<GetUserInfoResp> getUserInfo(GetUserInfoReq req);

    public ResponseVO<ImUserDataEntity> getSingleUserInfo(String userId , Integer appId);

    public ResponseVO deleteUser(DeleteUserReq req);

    public ResponseVO modifyUserInfo(ModifyUserInfoReq req);

    public ResponseVO login(LoginReq req);

    ResponseVO getUserSequence(GetUserSequenceReq req);
}
