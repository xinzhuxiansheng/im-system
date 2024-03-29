package com.yzhou.im.service.user.model.req;

import com.yzhou.im.service.user.dao.ImUserDataEntity;
import com.yzhou.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author yzhou
 * @date 2023/2/13
 */
@Data
public class ImportUserReq extends RequestBase {
    private List<ImUserDataEntity> userData;
}
