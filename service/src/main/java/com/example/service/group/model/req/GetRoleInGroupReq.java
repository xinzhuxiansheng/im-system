package com.example.service.group.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author: yzhou
 * @description:
 **/
@Data
public class GetRoleInGroupReq extends RequestBase {

    private String groupId;

    private List<String> memberId;
}
