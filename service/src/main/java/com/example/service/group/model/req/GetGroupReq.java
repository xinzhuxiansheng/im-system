package com.example.service.group.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

/**
 * @author: yzhou
 * @description:
 **/
@Data
public class GetGroupReq extends RequestBase {

    private String groupId;

}
