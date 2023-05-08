package com.yzhou.im.service.user.model.resp;

import com.yzhou.im.service.user.dao.ImUserDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @author: yzhou
 * @description:
 **/
@Data
public class GetUserInfoResp {

    private List<ImUserDataEntity> userDataItem;

    private List<String> failUser;


}
