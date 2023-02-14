package com.example.codec.pack.group;

import lombok.Data;

/**
 * @author: yzhou
 * @description: 解散群通知报文
 **/
@Data
public class DestroyGroupPack {

    private String groupId;

    private Long sequence;

}
