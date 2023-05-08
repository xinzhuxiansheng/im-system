package com.yzhou.im.tcp.reciver.process;

import com.yzhou.im.codec.proto.MessagePack;
import com.yzhou.im.tcp.utils.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;

public abstract class BaseProcess {

    public abstract void processBefore();

    public void process(MessagePack messagePack){
        processBefore();
        NioSocketChannel channel = SessionSocketHolder.get(messagePack.getAppId(),
                messagePack.getToId(), messagePack.getClientType(),
                messagePack.getImei());
        if(channel != null){
            channel.writeAndFlush(messagePack);
        }
        processAfter();
    }

    public abstract void processAfter();

}
