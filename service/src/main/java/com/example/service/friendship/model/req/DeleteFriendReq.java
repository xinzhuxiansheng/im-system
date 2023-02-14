package com.example.service.friendship.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class DeleteFriendReq extends RequestBase {

    @NotBlank(message = "fromId不能为空")
    private String fromId;

    @NotBlank(message = "toId不能为空")
    private String toId;

}
