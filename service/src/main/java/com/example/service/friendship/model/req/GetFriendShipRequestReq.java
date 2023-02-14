package com.example.service.friendship.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetFriendShipRequestReq extends RequestBase {

    @NotBlank(message = "用户id不能为空")
    private String fromId;

}
