package com.pinyougou.page.service.impl;

        import com.alibaba.dubbo.config.annotation.Service;
        import com.pinyougou.mapper.TbGoodsDescMapper;
        import com.pinyougou.mapper.TbGoodsMapper;
        import com.pinyougou.mapper.TbItemCatMapper;
        import com.pinyougou.mapper.TbItemMapper;
        import com.pinyougou.page.service.ItemPageService;
        import com.pinyougou.pojo.TbGoods;
        import com.pinyougou.pojo.TbGoodsDesc;
        import com.pinyougou.pojo.TbItem;
        import com.pinyougou.pojo.TbItemExample;
        import freemarker.template.Configuration;
        import freemarker.template.Template;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

        import java.io.FileWriter;
        import java.io.Writer;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

@Service
public class ItemPageServiceImpl  implements ItemPageService {
    @Value("${pagedir}")
    private String pagedir;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public boolean genItemHtml(Long goodsId){
        try {
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Map dataModel=new HashMap<>();
            //1.加载商品表数据
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goods", goods);
            //2.加载商品扩展表数据
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goodsDesc", goodsDesc);

            String category1=itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            dataModel.put("category1",category1);
            String category2=itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            dataModel.put("category2",category1);
            String category3=itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            dataModel.put("category3",category1);

            TbItemExample example=new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(goodsId);//根据SPUid查询SKU列表
            criteria.andStatusEqualTo("1");//当前商品状态正常
            example.setOrderByClause("is_default desc");//按照默认进行排序，将默认的排到第一位


            List<TbItem> itemList = itemMapper.selectByExample(example);
            dataModel.put("itemList", itemList);

            Writer out=new FileWriter(pagedir+goodsId+".html");
            template.process(dataModel, out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
