package com.example.service.group.model.req;

import com.yzhou.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DestroyGroupReq extends RequestBase {

    @NotNull(message = "群id不能为空")
    private String groupId;

}
