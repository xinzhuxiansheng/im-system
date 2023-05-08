package com.yzhou.im.service.friendship.service;

import com.yzhou.im.service.friendship.dao.ImFriendShipGroupEntity;
import com.yzhou.im.service.friendship.model.req.AddFriendShipGroupReq;
import com.yzhou.im.service.friendship.model.req.DeleteFriendShipGroupReq;
import com.yzhou.im.common.ResponseVO;

/**
 * @author: yzhou
 * @description:
 **/
public interface ImFriendShipGroupService {

    public ResponseVO addGroup(AddFriendShipGroupReq req);

    public ResponseVO deleteGroup(DeleteFriendShipGroupReq req);

    public ResponseVO<ImFriendShipGroupEntity> getGroup(String fromId, String groupName, Integer appId);

    public Long updateSeq(String fromId, String groupName, Integer appId);
}
