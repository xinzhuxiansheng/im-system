package com.yzhou.im.common.model.message;

import com.yzhou.im.common.model.ClientInfo;
import lombok.Data;

@Data
public class MessageReciveAckContent extends ClientInfo {

    private Long messageKey;

    private String fromId;

    private String toId;

    private Long messageSequence;


}
