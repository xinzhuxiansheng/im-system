package com.yzhou.im.service.user.model.req;

import com.yzhou.im.common.model.RequestBase;
import lombok.Data;

/**
 * @author yzhou
 * @date 2023/2/13
 */
@Data
public class UserId extends RequestBase {
    private String userId;
}
