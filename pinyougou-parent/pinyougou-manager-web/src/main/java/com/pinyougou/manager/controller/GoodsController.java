package com.pinyougou.manager.controller;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
//import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.TbItem;
//import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

import javax.jms.*;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;
    //    @Reference
//    private ItemSearchService itemSearchService;
    @Autowired
    private Destination queueSolrDestination;//用于发送solr导入的消息
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination queueSolrDeleteDestination;//用户在索引库中删除记录
    @Autowired
    private Destination topicPageDestination;
    @Autowired
    private Destination topicPageDeleteDestination;//用于删除静态网页的消息

//    @Reference(timeout = 40000)
//    private ItemPageService itemPageService;

    /**
     * 生成静态页（测试）
     *
     * @param goodsId
     */
//    @RequestMapping("/genHtml")
//    public void genHtml(Long goodsId) {
//        itemPageService.genItemHtml(goodsId);
//    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(final Long[] ids) {
        try {
            goodsService.delete(ids);
            //itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
            jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(ids);
                }
            });
            //删除页面
            jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(ids);
                }
            });
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }


    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        return goodsService.findPage(goods, page, rows);
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            if ("1".equals(status)) {
                //将数据导入到索引库
                List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids, status);


                //调用搜索接口实现数据批量导入
                if(itemList.size()>0){
                    System.out.println("导入了solr");
                    //itemSearchService.importList(itemList);
                    final String jsonString = JSON.toJSONString(itemList);
                    jmsTemplate.send(queueSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(jsonString);
                        }
                    });
                }else{
                    System.out.println("没有明细数据");
                }


                //将审核通过的商品生成静态页面
                for(final long id:ids){
//					itemPageService.genHtml(id);
                    //向activeMQ发送消息
                    System.out.println("执行到生成静态页面里部");
                    jmsTemplate.send(topicPageDestination, new MessageCreator(){

                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            TextMessage textMessage = session.createTextMessage(id+"");
                            System.out.println("执行到生成静态页面内部");
                            return textMessage;
                        }

                    });
                }

            System.out.println("生成了静态界面");
                }

            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }
}
