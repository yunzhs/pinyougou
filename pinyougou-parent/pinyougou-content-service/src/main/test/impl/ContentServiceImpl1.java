package impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;
import entity.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/applicationContext*.xml"})
@Service
public class ContentServiceImpl1 {


    @Autowired
    private RedisTemplate redisTemplate;

	@Test
    public void findByCategoryId() {
       // List<TbContent> contentList= (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);

        //redisTemplate.boundHashOps("我").put("是","火车王");//存入缓存

        System.out.println(redisTemplate.boundHashOps("我").get("是"));

    }



}
