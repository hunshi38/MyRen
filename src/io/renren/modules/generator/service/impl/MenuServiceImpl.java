package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.MenuDao;
import io.renren.modules.generator.entity.MenuEntity;
import io.renren.modules.generator.service.MenuService;



@Service("menuService")
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;
	
	@Override
	public MenuEntity queryObject(Integer id){
		return menuDao.queryObject(id);
	}
	
	@Override
	public List<MenuEntity> queryList(Map<String, Object> map){
		return menuDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return menuDao.queryTotal(map);
	}
	
	@Override
	public void save(MenuEntity menu){
		menuDao.save(menu);
	}
	
	@Override
	public void update(MenuEntity menu){
		menuDao.update(menu);
	}
	
	@Override
	public void delete(Integer id){
		menuDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		menuDao.deleteBatch(ids);
	}
	
}
