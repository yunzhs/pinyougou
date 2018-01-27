package com.pinyougou.search.service.impl;

import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.search.service.ItemSearchService;

@Component
public class ItemDeleteListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        System.out.println("监听接收到消息2...");
        ObjectMessage objectMessage = (ObjectMessage)message;
        try {
            Long[] ids = (Long[])objectMessage.getObject();
            System.out.println("进入该方法");
            itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
            System.out.println("删除索引库数据");
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
