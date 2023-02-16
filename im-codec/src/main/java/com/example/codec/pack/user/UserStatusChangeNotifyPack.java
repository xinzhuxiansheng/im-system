package com.example.codec.pack.user;

import com.lld.im.common.model.UserSession;
import lombok.Data;
import sun.dc.pr.PRError;

import java.util.List;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
@Data
public class UserStatusChangeNotifyPack {

    private Integer appId;

    private String userId;

    private Integer status;

    private List<UserSession> client;

}
