package com.yzhou.im.service.user.service;

import com.yzhou.im.service.user.dao.ImUserDataEntity;
import com.yzhou.im.service.user.model.req.*;
import com.yzhou.im.service.user.model.resp.GetUserInfoResp;
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
