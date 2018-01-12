package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService{
    @Autowired
    private TbBrandMapper tbBrandMapper;
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        Page<TbBrand> brandPage= (Page<TbBrand>) tbBrandMapper.selectByExample(null);

        return new PageResult(brandPage.getTotal(),brandPage.getResult());
    }

    @Override
    public void add(TbBrand tbBrand) {
         tbBrandMapper.insert(tbBrand);
    }

    @Override
    public TbBrand findOne(long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(TbBrand tbBrand) {
        tbBrandMapper.updateByPrimaryKey(tbBrand);
    }

    @Override
    public void delete(long[] ids) {
        for (long id:ids){
            tbBrandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult findPage(TbBrand tbBrand, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        TbBrandExample example=new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if(tbBrand!=null){
            //通过品牌名称
            if(tbBrand.getName()!=null&&!"".equals(tbBrand.getName())){
                criteria.andNameLike("%"+tbBrand.getName()+"%");
            }
            if(tbBrand.getFirstChar()!=null&&!"".equals(tbBrand.getFirstChar())){
                criteria.andNameLike("%"+tbBrand.getFirstChar()+"%");
            }
            //criteria用来改变相关的查询语句
        }
        Page<TbBrand> brandPage= (Page<TbBrand>) tbBrandMapper.selectByExample(example);

        return new PageResult(brandPage.getTotal(),brandPage.getResult());
    }

    @Override
    public List<Map> selectOptionList() {
        return tbBrandMapper.selectOptionList();
    }
}
