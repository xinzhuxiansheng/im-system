package com.example.service.group.model.req;

import com.yzhou.im.common.model.RequestBase;
import lombok.Data;

@Data
public class GetGroupReq extends RequestBase {

    private String groupId;

}
