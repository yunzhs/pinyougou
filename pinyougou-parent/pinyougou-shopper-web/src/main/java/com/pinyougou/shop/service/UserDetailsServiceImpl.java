package com.pinyougou.shop.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

/**
 * 自定义认证类
 * @author msj
 * 
 */
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private SellerService sellerService;
	
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//System.out.println("进入了UserDetailsServiceImpl");
		//创建角色的列表
		List<GrantedAuthority> authorities = new ArrayList<>();
		//构建角色对象
		GrantedAuthority authority =new SimpleGrantedAuthority("ROLE_SELLER");
		authorities.add(authority);
		
		//从数据库获取商家登陆信息
		TbSeller seller = sellerService.findOne(username);
        //System.out.println(seller.getPassword());
        if(seller!=null){
			if("1".equals(seller.getStatus())){
				//参数1：用户名 参数2：密码 参数3：权限
				return new User(username, seller.getPassword(), authorities);
			}else{
				return null;
			}
			
		}else{
			return null;
		}
	}

}
