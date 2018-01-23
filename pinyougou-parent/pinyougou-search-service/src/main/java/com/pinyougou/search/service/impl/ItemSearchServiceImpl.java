package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

	
	@Autowired
	private SolrTemplate solrTemplate;
	
	@Override
	public Map<String, Object> search(Map<String, Object> searchMap) {
		Map<String, Object> map= new HashMap<>();//返回的map
		//1.关键字搜索
		Map<String, Object> mapList = searchList(searchMap);
		map.putAll(mapList);
		//2.查询分类条件
		List<String> categoryList = findCategoryList(searchMap);
		map.put("categoryList", categoryList);
		//3.查询品牌和规格
		if(!"".equals(searchMap.get("category"))){//选择了分类
			Map<String, Object> brandAndSpecMap = findBrandAndSpecList((String)searchMap.get("category"));
			map.putAll(brandAndSpecMap);
		}else{
			if(categoryList!=null&&categoryList.size()>0){
				Map<String, Object> brandAndSpecMap = findBrandAndSpecList(categoryList.get(0));
				map.putAll(brandAndSpecMap);
			}
		}
		
		return map;
	}
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	/**
	 * 根据分类名称查询模板id，根据模板id查询品牌和规格数据
	 * @param category
	 * @return
	 */
	private Map<String, Object> findBrandAndSpecList(String category){
		Map<String, Object> map= new HashMap<>();
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);//从缓存中获取模板id
		List<Map> brandList=(List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
		List<Map> specList=(List<Map>) redisTemplate.boundHashOps("specList").get(typeId);
		
		map.put("brandList", brandList);
		map.put("specList", specList);
		
		return map;
	}
	
	/**
	 * 根据关键字分组查询分类列表
	 * @param searchMap
	 * @return
	 */
	private List<String> findCategoryList(Map<String, Object> searchMap){
		List<String> categoryList=new ArrayList<>();
		
		Query query =new SimpleQuery();
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		//设置分组字段 分类
		GroupOptions groupOptions =new GroupOptions();
		groupOptions.addGroupByField("item_category");
		query.setGroupOptions(groupOptions );
		//返回分组页
		GroupPage<TbItem> queryForGroupPage = solrTemplate.queryForGroupPage(query , TbItem.class);
		//返回分组结果集
		GroupResult<TbItem> groupResult = queryForGroupPage.getGroupResult("item_category");
		//返回分组入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		//分组入口集合
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		for(GroupEntry<TbItem> groupEntry:content){
			categoryList.add(groupEntry.getGroupValue());
			System.out.println(groupEntry.getGroupValue());
		}
		return categoryList;
	}
	
	
	/**
	 * 关键字搜索
	 */
	private Map<String, Object> searchList(Map<String, Object> searchMap){
		Map<String, Object> map= new HashMap<>();
		
		HighlightQuery query = new SimpleHighlightQuery();
		
		//高亮设置
		HighlightOptions highlightOptions=new HighlightOptions();
		highlightOptions.addField("item_title");//对title字段进行高亮设置
		highlightOptions.setSimplePrefix("<em style='color:red'>");//设置前缀
		highlightOptions.setSimplePostfix("</em>");//设置后缀
		query.setHighlightOptions(highlightOptions);
		
		//1.1根据关键字进行条件查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//1.2根据分类进行过滤
		if(searchMap.get("category")!=null&&!"".equals(searchMap.get("category"))){
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			filterQuery.addCriteria(filterCriteria );
			query.addFilterQuery(filterQuery );
		}
		
		//1.3根据品牌进行过滤
		if(searchMap.get("brand")!=null&&!"".equals(searchMap.get("brand"))){
			FilterQuery filterQuery= new SimpleFilterQuery();
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            System.out.println(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria );
			query.addFilterQuery(filterQuery);
		}
		
		//1.4根据规格进行过滤
		if(searchMap.get("spec")!=null){
			Map specMap = (Map) searchMap.get("spec");
			for(String key : searchMap.keySet()){
				FilterQuery filterQuery= new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
				filterQuery.addCriteria(filterCriteria );
				query.addFilterQuery(filterQuery);
			}
		}
		//高亮页
		HighlightPage<TbItem> queryForHighlightPage = solrTemplate.queryForHighlightPage(query , TbItem.class);
		//获取高亮入口集合
		List<HighlightEntry<TbItem>> highlighted = queryForHighlightPage.getHighlighted();
		for(HighlightEntry<TbItem> h:highlighted){//对每一条数据进行遍历
			TbItem item = h.getEntity();//得到没有高亮过的对象
			List<Highlight> highlights = h.getHighlights();//高亮对象集合 比如有多个列进行高亮，得到就是高亮字段的集合
			List<String> snipplets = highlights.get(0).getSnipplets();//获取片的集合
			
			item.setTitle(snipplets.get(0));//获取高亮内容，设置到title字段上
		}
		List<TbItem> content = queryForHighlightPage.getContent(); 
		map.put("rows", content);
		return map;
	}

}
