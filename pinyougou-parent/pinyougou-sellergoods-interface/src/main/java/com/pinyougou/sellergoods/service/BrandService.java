package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

public interface BrandService {
    public List<TbBrand> findAll();
    public PageResult findPage(int pageNo,int pageSize);
    public  void add(TbBrand tbBrand);
    public  TbBrand findOne(long id);
    public  void update(TbBrand tbBrand);
    public  void delete(long [] ids);
    public  PageResult findPage(TbBrand tbBrand,int pageNo,int pageSize);
}
