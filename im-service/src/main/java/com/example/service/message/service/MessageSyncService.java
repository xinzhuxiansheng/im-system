package com.example.service.message.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;import com.example.codec.pack.message.MessageReadedPack;
import com.example.codec.pack.message.RecallMessageNotifyPack;
import com.example.service.conversation.service.ConversationService;
import com.example.service.message.dao.ImMessageBodyEntity;
import com.example.service.message.dao.mapper.ImMessageBodyMapper;
import com.example.service.seq.RedisSeq;
import com.example.service.utils.ConversationIdGenerate;
import com.example.service.utils.MessageProducer;
import com.example.service.utils.SnowflakeIdWorker;
import com.yzhou.im.common.ResponseVO;
import com.yzhou.im.common.constant.Constants;
import com.yzhou.im.common.enums.ConversationTypeEnum;
import com.yzhou.im.common.enums.DelFlagEnum;
import com.yzhou.im.common.enums.MessageErrorCode;
import com.yzhou.im.common.enums.command.GroupEventCommand;
import com.yzhou.im.common.enums.command.MessageCommand;
import com.yzhou.im.common.model.ClientInfo;
import com.yzhou.im.common.model.SyncReq;
import com.yzhou.im.common.model.SyncResp;
import com.yzhou.im.common.model.message.MessageReadedContent;
import com.yzhou.im.common.model.message.MessageReciveAckContent;
import com.yzhou.im.common.model.message.OfflineMessageContent;
import com.yzhou.im.common.model.message.RecallMessageContent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MessageSyncService {

    @Autowired
    MessageProducer messageProducer;

    @Autowired
    ConversationService conversationService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ImMessageBodyMapper imMessageBodyMapper;

    @Autowired
    RedisSeq redisSeq;

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;


    public void receiveMark(MessageReciveAckContent messageReciveAckContent){
        messageProducer.sendToUser(messageReciveAckContent.getToId(),
                MessageCommand.MSG_RECIVE_ACK,messageReciveAckContent,messageReciveAckContent.getAppId());
    }

    /**
     * @description: ??????????????????????????????seq???????????????????????????????????????command ???????????????????????????????????????????????????????????????
     * @param
     * @return void
     * @author lld
     */
    public void readMark(MessageReadedContent messageContent) {
        conversationService.messageMarkRead(messageContent);
        MessageReadedPack messageReadedPack = new MessageReadedPack();
        BeanUtils.copyProperties(messageContent,messageReadedPack);
        syncToSender(messageReadedPack,messageContent,MessageCommand.MSG_READED_NOTIFY);
        //???????????????
        messageProducer.sendToUser(messageContent.getToId(),
                MessageCommand.MSG_READED_RECEIPT,messageReadedPack,messageContent.getAppId());
    }

    private void syncToSender(MessageReadedPack pack, MessageReadedContent content, Command command){
        MessageReadedPack messageReadedPack = new MessageReadedPack();
//        BeanUtils.copyProperties(messageReadedContent,messageReadedPack);
        //???????????????????????????
        messageProducer.sendToUserExceptClient(pack.getFromId(),
                command,pack,
                content);
    }

    public void groupReadMark(MessageReadedContent messageReaded) {
        conversationService.messageMarkRead(messageReaded);
        MessageReadedPack messageReadedPack = new MessageReadedPack();
        BeanUtils.copyProperties(messageReaded,messageReadedPack);
        syncToSender(messageReadedPack,messageReaded, GroupEventCommand.MSG_GROUP_READED_NOTIFY
        );
        if(!messageReaded.getFromId().equals(messageReaded.getToId())){
            messageProducer.sendToUser(messageReadedPack.getToId(),GroupEventCommand.MSG_GROUP_READED_RECEIPT
                    ,messageReaded,messageReaded.getAppId());
        }
    }

    public ResponseVO syncOfflineMessage(SyncReq req) {

        SyncResp<OfflineMessageContent> resp = new SyncResp<>();

        String key = req.getAppId() + ":" + Constants.RedisConstants.OfflineMessage + ":" + req.getOperater();
        //???????????????seq
        Long maxSeq = 0L;
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Set set = zSetOperations.reverseRangeWithScores(key, 0, 0);
        if(!CollectionUtils.isEmpty(set)){
            List list = new ArrayList(set);
            DefaultTypedTuple o = (DefaultTypedTuple) list.get(0);
            maxSeq = o.getScore().longValue();
        }

        List<OfflineMessageContent> respList = new ArrayList<>();
        resp.setMaxSequence(maxSeq);

        Set<ZSetOperations.TypedTuple> querySet = zSetOperations.rangeByScoreWithScores(key,
                req.getLastSequence(), maxSeq, 0, req.getMaxLimit());
        for (ZSetOperations.TypedTuple<String> typedTuple : querySet) {
            String value = typedTuple.getValue();
            OfflineMessageContent offlineMessageContent = JSONObject.parseObject(value, OfflineMessageContent.class);
            respList.add(offlineMessageContent);
        }
        resp.setDataList(respList);

        if(!CollectionUtils.isEmpty(respList)){
            OfflineMessageContent offlineMessageContent = respList.get(respList.size() - 1);
            resp.setCompleted(maxSeq <= offlineMessageContent.getMessageKey());
        }

        return ResponseVO.successResponse(resp);
    }

    //???????????????????????????
    //???????????????????????????
    //ack????????????
    //??????????????????
    //???????????????????????????
    public void recallMessage(RecallMessageContent content) {

        Long messageTime = content.getMessageTime();
        Long now = System.currentTimeMillis();

        RecallMessageNotifyPack pack = new RecallMessageNotifyPack();
        BeanUtils.copyProperties(content,pack);

        if(120000L < now - messageTime){
            recallAck(pack,ResponseVO.errorResponse(MessageErrorCode.MESSAGE_RECALL_TIME_OUT),content);
            return;
        }

        QueryWrapper<ImMessageBodyEntity> query = new QueryWrapper<>();
        query.eq("app_id",content.getAppId());
        query.eq("message_key",content.getMessageKey());
        ImMessageBodyEntity body = imMessageBodyMapper.selectOne(query);

        if(body == null){
            //TODO ack?????? ??????????????????????????????
            recallAck(pack,ResponseVO.errorResponse(MessageErrorCode.MESSAGEBODY_IS_NOT_EXIST),content);
            return;
        }

        if(body.getDelFlag() == DelFlagEnum.DELETE.getCode()){
            recallAck(pack,ResponseVO.errorResponse(MessageErrorCode.MESSAGE_IS_RECALLED),content);

            return;
        }

        body.setDelFlag(DelFlagEnum.DELETE.getCode());
        imMessageBodyMapper.update(body,query);

        if(content.getConversationType() == ConversationTypeEnum.P2P.getCode()){

            // ??????fromId?????????
            String fromKey = content.getAppId() + ":" + Constants.RedisConstants.OfflineMessage + ":" + content.getFromId();
            // ??????toId?????????
            String toKey = content.getAppId() + ":" + Constants.RedisConstants.OfflineMessage + ":" + content.getToId();

            OfflineMessageContent offlineMessageContent = new OfflineMessageContent();
            BeanUtils.copyProperties(content,offlineMessageContent);
            offlineMessageContent.setDelFlag(DelFlagEnum.DELETE.getCode());
            offlineMessageContent.setMessageKey(content.getMessageKey());
            offlineMessageContent.setConversationType(ConversationTypeEnum.P2P.getCode());
            offlineMessageContent.setConversationId(conversationService.convertConversationId(offlineMessageContent.getConversationType()
                    ,content.getFromId(),content.getToId()));
            offlineMessageContent.setMessageBody(body.getMessageBody());

            long seq = redisSeq.doGetSeq(content.getAppId() + ":" + Constants.SeqConstants.Message + ":" + ConversationIdGenerate.generateP2PId(content.getFromId(),content.getToId()));
            offlineMessageContent.setMessageSequence(seq);

            long messageKey = SnowflakeIdWorker.nextId();

            redisTemplate.opsForZSet().add(fromKey,JSONObject.toJSONString(offlineMessageContent),messageKey);
            redisTemplate.opsForZSet().add(toKey,JSONObject.toJSONString(offlineMessageContent),messageKey);

            //ack
            recallAck(pack,ResponseVO.successResponse(),content);
            //??????????????????
            messageProducer.sendToUserExceptClient(content.getFromId(),
                    MessageCommand.MSG_RECALL_NOTIFY,pack,content);
            //???????????????
            messageProducer.sendToUser(content.getToId(),MessageCommand.MSG_RECALL_NOTIFY,
                    pack,content.getAppId());
        }else{

        }

    }
    private void recallAck(RecallMessageNotifyPack recallPack, ResponseVO<Object> success, ClientInfo clientInfo) {
        ResponseVO<Object> wrappedResp = success;
        messageProducer.sendToUser(recallPack.getFromId(),
                MessageCommand.MSG_RECALL_ACK, wrappedResp, clientInfo);
    }

}
