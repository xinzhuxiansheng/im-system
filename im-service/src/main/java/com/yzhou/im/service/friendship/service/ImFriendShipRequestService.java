package com.yzhou.im.service.friendship.service;

import com.yzhou.im.service.friendship.model.req.ApproverFriendRequestReq;
import com.yzhou.im.service.friendship.model.req.FriendDto;
import com.yzhou.im.service.friendship.model.req.ReadFriendShipRequestReq;
import com.yzhou.im.common.ResponseVO;


public interface ImFriendShipRequestService {

    public ResponseVO addFienshipRequest(String fromId, FriendDto dto, Integer appId);

    public ResponseVO approverFriendRequest(ApproverFriendRequestReq req);

    public ResponseVO readFriendShipRequestReq(ReadFriendShipRequestReq req);

    public ResponseVO getFriendRequest(String fromId, Integer appId);
}
