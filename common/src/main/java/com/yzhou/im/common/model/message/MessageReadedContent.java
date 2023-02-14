package com.yzhou.im.common.model.message;

import com.yzhou.im.common.model.ClientInfo;
import lombok.Data;

@Data
public class MessageReadedContent extends ClientInfo {

    private long messageSequence;

    private String fromId;

    private String groupId;

    private String toId;

    private Integer conversationType;

}
