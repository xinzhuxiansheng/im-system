package com.example.service.friendship.service;

import com.example.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.example.service.friendship.model.req.DeleteFriendShipGroupMemberReq;
import com.yzhou.im.common.ResponseVO;

public interface ImFriendShipGroupMemberService {

    public ResponseVO addGroupMember(AddFriendShipGroupMemberReq req);

    public ResponseVO delGroupMember(DeleteFriendShipGroupMemberReq req);

    public int doAddGroupMember(Long groupId, String toId);

    public int clearGroupMember(Long groupId);
}
