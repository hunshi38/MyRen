package io.renren.modules.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.modules.generator.dao.TablesDao;
import io.renren.modules.generator.entity.TablesEntity;
import io.renren.modules.generator.service.TablesService;



@Service("tablesService")
public class TablesServiceImpl implements TablesService {
	@Autowired
	private TablesDao tablesDao;
	
	@Override
	public TablesEntity queryObject(Integer id){
		return tablesDao.queryObject(id);
	}
	
	@Override
	public List<TablesEntity> queryList(Map<String, Object> map){
		return tablesDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return tablesDao.queryTotal(map);
	}
	
	@Override
	public void save(TablesEntity tables){
		tablesDao.save(tables);
	}
	
	@Override
	public void update(TablesEntity tables){
		tablesDao.update(tables);
	}
	
	@Override
	public void delete(Integer id){
		tablesDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		tablesDao.deleteBatch(ids);
	}
	
}
