package com.ddmh.controller;

import com.alibaba.fastjson.JSON;
import com.ddmh.dto.SelectItemDto;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProducerController {

    @Autowired
    private DefaultMQProducer defaultProducer;

    @Autowired
    private TransactionMQProducer transactionProducer;


    /**
     * 发送普通消息
     */
    @GetMapping("/sendMessage")
    public void sendMsg() {

        for(int i=0;i<100;i++){
            SelectItemDto selectItemDto  = SelectItemDto.builder().label("xxx").value("111").build();
            String json = JSON.toJSONString(selectItemDto);
            Message msg = new Message("user-topic","white", json.getBytes());
            try {
                SendResult result = defaultProducer.send(msg);
                System.out.println("消息id:"+result.getMsgId()+":"+","+"发送状态:"+result.getSendStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 发送事务消息
     * @return
     */
    @GetMapping("/sendTransactionMess")
    public String sendTransactionMsg() {
        SendResult sendResult = null;
        try {
            // a,b,c三个值对应三个不同的状态
            String ms = "c";
            Message msg = new Message("user-topic","white",ms.getBytes());
            // 发送事务消息
            sendResult = transactionProducer.sendMessageInTransaction(msg, (Message msg1, Object arg) -> {
                String value = "";
                if (arg instanceof String) {
                    value = (String) arg;
                }
                if (value == "") {
                    throw new RuntimeException("发送消息不能为空...");
                } else if (value =="a") {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else if (value =="b") {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }, 4);
            System.out.println(sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult.toString();
    }

    /**
     * 支持顺序发送消息
     */
    @GetMapping("/sendMessOrder")
    public void sendMsgOrder() {
        for(int i=0;i<100;i++) {
            SelectItemDto selectItemDto  = SelectItemDto.builder().label("yyy").value("222").build();
            String json = JSON.toJSONString(selectItemDto);

            Message msg = new Message("user-topic", "white", json.getBytes());
            try{
                defaultProducer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        int index = ((Integer) arg) % mqs.size();
                        return mqs.get(index);
                    }
                },i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
