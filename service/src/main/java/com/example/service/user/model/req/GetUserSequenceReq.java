package com.example.service.user.model.req;

import com.yzhou.im.common.model.RequestBase;
import lombok.Data;

@Data
public class GetUserSequenceReq extends RequestBase {

    private String userId;

}
